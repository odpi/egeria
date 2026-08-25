/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.messages;

import java.io.Serial;

/**
 * MessageSetParsingException is thrown when a source file declares a message set that the parser cannot read.
 * It fails the build rather than allowing the documentation to quietly lose a message.
 */
public class MessageSetParsingException extends Exception
{
    @Serial
    private static final long serialVersionUID = 1L;

    private final String sourcePath;


    /**
     * Constructor.
     *
     * @param sourcePath path of the source file that could not be parsed
     * @param reason description of what the parser could not understand
     */
    MessageSetParsingException(String sourcePath, String reason)
    {
        super("Unable to document the message set in " + sourcePath + " because " + reason +
                      ".  Either correct the message set, or extend MessageSetParser to understand it.");

        this.sourcePath = sourcePath;
    }


    /**
     * Return the path of the source file that could not be parsed.
     *
     * @return relative path, using "/" separators
     */
    public String getSourcePath() { return sourcePath; }
}
