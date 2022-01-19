/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.glossaryauthor.fvt;

/**
 * Test exception for FVT
 */
public class GlossaryAuthorFVTCheckedException extends Exception
{
    /**
     * This is the typical constructor used for creating a SubjectAreaFVTCheckedException.
     *
     * @param message Error message
     */
    public GlossaryAuthorFVTCheckedException(String message)
    {
       super(message);
    }


    /**
     * This is the constructor used for creating a SubjectAreaFVTCheckedException that resulted from a previous error.
     *
     * @param message Error message
     * @param e the error that resulted in this exception.
     * */
    public GlossaryAuthorFVTCheckedException(String message, Exception e)
    {
       super(message,e);
    }
}
