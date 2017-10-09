import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Objects;
import com.google.common.base.Throwables;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.NullOutputStream;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class represents a document that provides access to the data as well as the metadata.
 *
 * <p>Snap Authors operate on the Documents inside the snap. A Document consists of a data and a
 * metadata section. Each snap can add metadata entries under its own namespace but can read the
 * metadata entries from any namespace.
 *
 * <p>A Json serialized document object is shown below:
 * {@code
 *      {
 *          "data": {
 *              "field": "foo",
 *          },
 *          "meta_data": {
 *              "global":{
 *                  "doc_id" : 1
 *              },
 *              "parse_pipeline.parser": {
 *                  "parser_type":"JSON"
 *              }
 *          }
 *      }
 * }
 *
 * @author ksubramanian
 */
public class DocumentImpl implements Document {
    private class ObservableDoc extends Observable {
        public void acknowledge() {
            setChanged();
            notifyObservers(getMetadata());
        }
    }

    private static final String MD_5 = "MD5";
    private static final String METADATA = "Metadata->";
    private static final String DATA = "Data->";
    private static final String NEW_LINE = "\n";

    private Object data;
    private MessageDigest digest;
    // Used for tracking lineage.
    private volatile LineageEntry lineageEntry;
    private String source = StringUtils.EMPTY;

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    }

    private final AtomicReference<Metadata> metadata = new AtomicReference<>();
    private ObservableDoc observable;

    /**
     * Creates a new document object with null data.
     */
    public DocumentImpl() {
    }

    /**
     * Creates a new document object with the given metadata.
     *
     * @param metadata
     */
    public DocumentImpl(Metadata metadata) {
        this.metadata.set(metadata);
    }

    /**
     * Creates a new document object with the given data.
     *
     * @param data data
     */
    public DocumentImpl(Object data) {
        this.data = data;
    }

    /**
     * Creates a new document object with the given data and metadata.
     *
     * @param data
     * @param metadata
     */
    public DocumentImpl(Object data, Metadata metadata) {
        this.data = data;
        this.metadata.set(metadata);
    }

    @Override
    public void set(final Object data) {
        this.data = data;
    }

    @Override
    public <T> T get(Class<T> type) {
        return type.cast(data);
    }

    @Override
    public Object get() {
        return data;
    }

    @Override
    public Metadata getMetadata() {
        if (metadata.get() == null) {
            metadata.compareAndSet(null, new MetadataImpl());
        }

        return metadata.get();
    }

    @Override
    public Document copy() throws DocumentException {
        DocumentImpl document;
        if (data != null) {
            if (data instanceof Serializable) {
                try {
                    document = new DocumentImpl(DeepUtils.<Object>copy(data));
                } catch (Exception e) {
                    String error = String.format(Messages.ERR_COPY, e.getMessage(),
                            Throwables.getRootCause(e).toString());
                    throw new ExecutionException(e, error)
                            .withReason(error)
                            .withResolution(Messages.RESOLUTION_SERIALIZABLE);
                }
            } else {
                throw new ExecutionException(Messages.DATA_IS_NOT_SERIALIZABLE)
                        .withReason(Messages.DATA_IS_NOT_SERIALIZABLE)
                        .withResolution(Messages.RESOLUTION_SERIALIZABLE);
            }
        } else {
            document = new DocumentImpl();
        }
        if (this.lineageEntry != null) {
            // We are making a copy of this document. So add this new document id to the parent
            // lineage entry.
            document.lineageEntry = this.lineageEntry.copy(lineageEntry.getId());
        }
        document.source = source;
        return document;
    }

    public synchronized void addObserver(Observer documentObserver) {
        if (observable == null) {
            observable = new ObservableDoc();
            observable.addObserver(documentObserver);
        }
    }

    @Override
    public void acknowledge() {
        if (observable != null) {
            observable.acknowledge();
        }
        if (lineageEntry != null) {
            lineageEntry.acknowledge();
        }
    }

    /**
     * Implementation specific method that exposes lineage entry.
     *
     * @return lineageEntry
     */
    public LineageEntry getLineageEntry() {
        return lineageEntry;
    }

    /**
     * Implementation specific method that sets the lineage entry for this document.
     *
     * @param lineageEntry
     */
    public void setLineageEntry(final LineageEntry lineageEntry) {
        this.lineageEntry = lineageEntry;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (obj instanceof Document) {
            final Document other = (Document) obj;
            Object otherData = other.get(Object.class);
            if (this.data != otherData && (this.data == null || !this.data.equals(otherData))) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.data, this.metadata.get());
    }

    @Override
    public byte[] getMd5() {
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance(MD_5);
            } catch (NoSuchAlgorithmException e) {
                throw new ExecutionException(e, Messages.CANNOT_FIND_MD5_MESSAGE_DIGEST_ALGORITHM)
                        .withResolutionAsDefect();
            }
        }
        DigestOutputStream digestOutputStream = new DigestOutputStream(NullOutputStream
                .NULL_OUTPUT_STREAM, digest);

        try {
            digest.reset();
            OBJECT_MAPPER.writeValue(digestOutputStream, data);
            return digest.digest();
        } catch (IOException e) {
            throw new ExecutionException(e, Messages.ERROR_SERIALIZING_DOCUMENT_DURING_DIGEST)
                    .withResolutionAsDefect();
        } finally {
            IOUtils.closeQuietly(digestOutputStream);
        }
    }

    @Override
    public void writeExternal(final ObjectOutput out) throws IOException {
        out.writeObject(metadata.get());
        out.writeObject(data);
    }

    @Override
    public void readExternal(final ObjectInput in) throws IOException, ClassNotFoundException {
        metadata.set((Metadata) in.readObject());
        data = in.readObject();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(METADATA).append(metadata.toString()).append(NEW_LINE);
        return str.append(DATA).append(data).toString();
    }

    public DocumentImpl fillInSource() {
        source = Thread.currentThread().getName();
        return this;
    }
}