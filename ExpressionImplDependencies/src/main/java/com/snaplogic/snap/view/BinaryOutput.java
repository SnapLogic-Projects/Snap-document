package com.snaplogic.snap.view;

import com.snaplogic.Document;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * BinaryOutput is the interface that is used to transmit binary data objects from a snap.
 * Binary data can be transmitted from a snap using the code snippet shown below:
 * {@code
 *      return new BinaryOutput(){
 *          public Document getHeader() {
 *              return header;
 *          }
 *
 *          public void write(WritableByteChannel writeChannel) throws IOException {
 *              IOUtils.copy(readStream, Channels.newOutputStream(writeChannel));
 *          }
 *      };
 * }
 *
 * @author ksubramanian
 */
public interface BinaryOutput {

    /**
     * Returns the header for the binary data.
     *
     * @return header
     */
    Document getHeader();

    /**
     * Writes the binary data into the given write channel.
     *
     * @param writeChannel
     *
     * @throws IOException
     */
    void write(WritableByteChannel writeChannel) throws IOException;
}
