/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.parquetfile.footer;

import java.io.IOException;
import java.io.Serial;

/**
 * ParquetFormatException is thrown when a file can be read, but its content is not a Parquet file that
 * the footer reader is able to describe.
 */
public class ParquetFormatException extends IOException
{
    /**
     * The reason why the file cannot be described.
     */
    public enum Reason
    {
        /**
         * The file does not begin and end with the markers that identify a Parquet file.
         */
        NOT_PARQUET,

        /**
         * The file is a Parquet file, but its footer is encrypted and so nothing is known about its content.
         */
        ENCRYPTED_FOOTER,

        /**
         * The file has the Parquet markers, but the footer between them cannot be decoded.
         */
        CORRUPT_FOOTER
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private final Reason reason;


    /**
     * Constructor.
     *
     * @param reason why the file cannot be described
     * @param message details of the problem
     */
    public ParquetFormatException(Reason reason,
                                  String message)
    {
        super(message);

        this.reason = reason;
    }


    /**
     * Return why the file cannot be described.
     *
     * @return reason
     */
    public Reason getReason()
    {
        return reason;
    }
}
