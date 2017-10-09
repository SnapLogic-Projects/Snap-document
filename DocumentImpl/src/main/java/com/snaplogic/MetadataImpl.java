package com.snaplogic;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.fasterxml.uuid.impl.UUIDUtil;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import org.apache.commons.lang3.StringUtils;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class holds the metadata information of a document.
 * <p> A snap author can add metadata information to a document. The metadata entries are added as
 * a key-value pair under a specific namespace. A unique namespace is generated for the snap based
 * on the pipeline and resource name and all the metadata information is added under this unique
 * namespace.
 *
 * @author ksubramanian
 */
public class MetadataImpl implements Metadata {
    private final AtomicReference<UUID> docId = new AtomicReference<>();
    private final Map<String, Namespace> namespaces;
    private static final TimeBasedGenerator timeBasedGenerator = Generators.timeBasedGenerator();
    private static final UUIDUtil uuidUtil = new UUIDUtil();

    /**
     * Merges all the metadata into one single metadata object.
     *
     * @param metadatas
     * @return metadata
     */
    public static MetadataImpl merge(MetadataImpl... metadatas) {
        if (metadatas != null) {
            if (metadatas.length == 0) {
                throw new DocumentException(Messages.NOTHING_TO_MERGE);
            } else if (metadatas.length == 1) {
                return metadatas[0];
            } else {
                MetadataImpl metadata = new MetadataImpl();
                for (MetadataImpl metadataImpl : metadatas) {
                    for (Map.Entry<String, Map<String, String>> entry :
                            metadataImpl.getAsMap().entrySet()) {
                        Namespace namespace = metadata.namespaces.get(entry.getKey());
                        if (namespace == null) {
                            metadata.namespaces.put(entry.getKey(),
                                    new Namespace(entry.getValue()));
                        } else {
                            namespace.merge(entry.getValue());
                        }
                    }
                }
                return metadata;
            }
        } else {
            throw new DocumentException(Messages.NOTHING_TO_MERGE);
        }
    }

    /**
     * Constructs an empty document metadata object.
     */
    public MetadataImpl() {
        namespaces = new HashMap<>();
        addId();
    }

    /**
     * Copy constructor for creating new metadata object.
     *
     * @param docId
     * @param namespaces
     */
    public MetadataImpl(UUID docId, Map<String, Namespace> namespaces) {
        this.docId.compareAndSet(null, docId);
        this.namespaces = namespaces;
        addId();
    }

    public MetadataImpl(final Map<String, Map<String, String>> metadata) {
        namespaces = new HashMap<>();
        this.docId.compareAndSet(null, getId(metadata));
        addId();
        populate(metadata);
    }

    private void populate(final Map<String, Map<String, String>> metadata) {
        for (Map.Entry<String, Map<String, String>> entry : metadata.entrySet()) {
            namespaces.put(entry.getKey(), new Namespace(entry.getValue()));
        }
    }

    /**
     * Adds the metadata entry under the specified namespace.
     *
     * @param namespace Namespace
     * @param key       com.snaplogic.Metadata key
     * @param value     com.snaplogic.Metadata value
     */
    public void add(String namespace, String key, String value) {
        addMetadata(namespace, key, value);
    }

    @Override
    public final String getId() {
        // Late initialization of doc id.
        // Generating a UUID is very expensive and is not used by all the snaps. Only a few snaps
        // make use of Id. Hence making this lazy will save some time for most of the snaps.
        UUID uuid = docId.get();
        if (uuid != null) {
            return uuid.toString();
        } else {
            UUID generatedUuid = timeBasedGenerator.generate();
            if (this.docId.compareAndSet(null, generatedUuid)) {
                addId();
                return generatedUuid.toString();
            } else {
                return docId.get().toString();
            }
        }
    }

    /**
     * Returns this metadata object in map format.
     *
     * @return metadataMap
     */
    public Map<String, Map<String, String>> getAsMap() {
        Map<String, Map<String, String>> metadataMap = new HashMap<>();
        for (Map.Entry<String, Namespace> namespaceEntry : namespaces.entrySet()) {
            metadataMap.put(namespaceEntry.getKey(), namespaceEntry.getValue().metadata);
        }
        return Collections.unmodifiableMap(metadataMap);
    }

    @Override
    public String lookup(String namespaceRegex, String key) {
        Pattern pattern = Pattern.compile(namespaceRegex);
        for (String group : namespaces.keySet()) {
            Matcher matcher = pattern.matcher(group);
            boolean result = matcher.matches();
            if (result) {
                String value = namespaces.get(group).get(key);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public Metadata copy() throws DocumentException {
        Map<String, Namespace> copyOfNamespaces = DeepUtils.copy(this.namespaces);
        return new MetadataImpl(uuidUtil.uuid(this.docId.toString()), copyOfNamespaces);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        if (obj instanceof MetadataImpl) {
            final MetadataImpl that = (MetadataImpl) obj;
            if (!namespaces.equals(that.namespaces)) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(namespaces);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).
                add("Namespaces", this.namespaces).
                toString();
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        Set<Map.Entry<String, Namespace>> entries = this.namespaces.entrySet();
        out.writeInt(entries.size());
        for (Map.Entry<String, Namespace> namespaceEntry : entries) {
            out.writeUTF(namespaceEntry.getKey());
            namespaceEntry.getValue().writeExternal(out);
        }
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        int size = in.readInt();
        for (int i = 0; i < size; i++){
            String namespaceKey = in.readUTF();
            Namespace namespace = new Namespace();
            namespace.readExternal(in);
            namespaces.put(namespaceKey, namespace);
        }
    }

    //----------------------------------- Private class & methods -------------------------------//
    /**
     * Adds the metadata information (key-value) into the namespace container corresponding to the
     * specified snap.
     * <p> By taking the snap object, we are restricting the snap authors to add metadata
     * information only in their own namespace. They cannot possibly get hold of any other snap
     * object in the pipeline.
     *
     * @param snapNamespace
     * @param key
     * @param value
     */
    private void addMetadata(String snapNamespace, String key, String value) {
        Namespace namespace = namespaces.get(snapNamespace);
        if (namespace == null) {
            // Create and add new namespace for this snap.
            namespace = new Namespace();
            namespaces.put(snapNamespace, namespace);
        }
        namespace.set(key, value);
    }

    /**
     * Adds the automatically generated document that is unique within JCC.
     */
    private void addId() {
        UUID uuid = docId.get();
        if (uuid != null) {
            Namespace globalNamespace = new Namespace();
            globalNamespace.set(SdkConstants.DOC_ID, uuid.toString());
            namespaces.put(SdkConstants.GLOBAL_NAMESPACE, globalNamespace);
        }
        // TODO Also, add CC_Id as uuid is not unique across cc.
    }

    /**
     * Returns the id defined in the global namespace section of the metadata map.
     *
     * @param metadataMap
     * @return id
     */
    private UUID getId(Map<String, Map<String, String>> metadataMap) {
        Map<String, String> globalNamespace = metadataMap.get(SdkConstants.GLOBAL_NAMESPACE);
        if (globalNamespace != null) {
            String uuidString = globalNamespace.get(SdkConstants.DOC_ID);
            if (uuidString != null) {
                return uuidUtil.uuid(uuidString);
            }
        }
        return null;
    }

    /**
     * This is the namespace specific to a snap under which the metadata entries of that snap will
     * be stored.
     *
     * <p> Purpose of this class is to simply and make the code more readable.
     * Map<String, Namespace> looks more readable that Map<String, Map<String, String>>.
     */
    @VisibleForTesting
    static class Namespace implements Externalizable {

        private static final transient int INITIAL_CAPACITY = 2;
        @VisibleForTesting
        protected static final String PATTERN_APPEND = "%s,%s";
        private final Map<String, String> metadata  = new HashMap<>(INITIAL_CAPACITY);
        @VisibleForTesting
        protected static final String VALUE_SEPARATOR = ",";
        private static final Pattern PATTERN_SPLIT = Pattern.compile(VALUE_SEPARATOR);

        public Namespace() {
        }

        public Namespace(final Map<String, String> entries) {
            setAll(entries);
        }

        /**
         * Adds all the entries in the given map to the metadata map of this object.
         *
         * @param entries
         */
        private void setAll(Map<String, String> entries) {
            metadata.putAll(entries);
        }

        /**
         * Merges the given value map into this namespace.
         *
         * @param value
         */
        public void merge(final Map<String, String> value) {
            for (Map.Entry<String, String> entry : value.entrySet()) {
                String metadataValue = metadata.get(entry.getKey());
                String entryValue = entry.getValue();
                if (StringUtils.isNotBlank(entryValue)) {
                    if (metadataValue == null) {
                        metadata.put(entry.getKey(), entryValue);
                    } else {
                        String[] splits = PATTERN_SPLIT.split(metadataValue);
                        boolean isMatchFound = false;
                        for (String split : splits) {
                            if (entryValue.equals(split)) {
                                isMatchFound = true;
                                // Found match. We don't have to add this entry to the metadata map.
                                break;
                            }
                        }
                        if (!isMatchFound) {
                            // We only add the metadata when none of the split matches the given
                            // entry value.
                            metadata.put(entry.getKey(), String.format(PATTERN_APPEND,
                                    metadataValue, entryValue));
                        }
                    }
                }
            }
        }

        /**
         * Adds the metadata information to this namespace.
         *
         * @param key
         * @param value
         */
        public void set(String key, String value) {
            this.metadata.put(key, value);
        }

        /**
         * Returns the metadata information in this namespace for this key.
         *
         * @param key
         *
         * @return metadataValue
         */
        public String get(String key) {
            return this.metadata.get(key);
        }

        @Override
        public void writeExternal(final ObjectOutput out) throws IOException {
            Set<Map.Entry<String,String>> entries = metadata.entrySet();
            out.writeInt(entries.size());
            for (Map.Entry<String, String> entry : entries) {
                out.writeUTF(entry.getKey());
                out.writeUTF(entry.getValue());
            }
        }

        @Override
        public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
            int size = in.readInt();
            for (int i = 0; i < size; i++) {
                String key = in.readUTF();
                String value = in.readUTF();
                metadata.put(key, value);
            }
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            final Namespace namespace = (Namespace) o;

            if (!metadata.equals(namespace.metadata)) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            return metadata.hashCode();
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).
                    add("com.snaplogic.Metadata", metadata).
                    toString();
        }
    }
}