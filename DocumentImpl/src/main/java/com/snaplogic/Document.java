package com.snaplogic;

import java.io.Externalizable;

public interface Document extends Externalizable {

    /**
     * Sets the data.
     *
     * @param data com.snaplogic.Document data
     */
    void set(Object data);

    /**
     * Returns the data that is set for this document.
     *
     * @param klass Class type for the return value
     * @return data
     */
    <T> T get(Class<T> klass);

    /**
     * Returns the raw data object.
     *
     * @return data
     */
    Object get();

    /**
     * Returns the metadata for this snap.
     *
     * @return metadata
     */
    Metadata getMetadata();

    /**
     * Creates a new copy of the document.
     *
     * @return copy of the document
     * @throws DocumentException
     */
    Document copy() throws DocumentException;

    /**
     * Acknowledge that the document processing was successful.
     */
    void acknowledge();

    /**
     * Returns the md5 value for the document value.
     *
     * @return the md5
     */
    byte[] getMd5();
}