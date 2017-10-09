package com.snaplogic;

import java.io.Serializable;
import java.util.Collection;

/**
 * com.snaplogic.LineageEntry represents an entry in the lineage chain with reference to the parent entries and
 * child entries.
 *
 * @author ksubramanian
 * @since 2014
 */
public interface LineageEntry extends Serializable {
    /**
     * Internal snaplogic namespace used by the platform.
     */
    public static final String SL_NAMESPACE = "__snaplogic__";
    /**
     * Message id header that is used to track the lineage of a document.
     */
    public static final String MESSAGE_ID = "message_id";

    /**
     * Returns the unique id of this entry.
     *
     * @return id
     */
    String getId();

    /**
     * Returns the source entries of this lineage entry.
     *
     * @return sources
     */
    Collection<? extends LineageEntry> getSources();

    /**
     * Copies this data lineage entry into a new lineage entry that will be tracked separately.
     *
     * @param copyMessageId
     * @return copiedLineageEntry
     */
    LineageEntry copy(String copyMessageId);

    /**
     * Returns the acknowledgement state of this data entry.
     *
     * @return isAcknowledged
     */
    boolean isAcknowledged();

    /**
     * Acknowledges the processing of this data entry.
     */
    void acknowledge();
}