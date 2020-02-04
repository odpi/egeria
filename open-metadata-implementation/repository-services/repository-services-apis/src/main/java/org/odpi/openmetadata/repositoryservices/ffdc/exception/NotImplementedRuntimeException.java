/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.ffdc.exception;

/**
 * This exception is used to mark methods in the code that are not yet implemented.  The Git Issue where the implementation
 * is being tracked is included in the message.
 */
public class NotImplementedRuntimeException extends RuntimeException
{
    private static final long    serialVersionUID = 1L;

    public NotImplementedRuntimeException(String   className,
                                          String   methodName,
                                          String   issueName)
    {
        super("Method " + methodName + " in class " + className + " is not yet implemented. Refer to Git Issue " + issueName );
    }
}
