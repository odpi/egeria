/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.api.ffdc;

import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;

import java.io.Serial;
import java.util.Arrays;


/**
 * This is a simple exception class that can convey the few types of exception
 * that it is sensible to propagate back to the user interface. The ServerAuthor handler will
 * have detected or caught each error condition, audit logged it and will then
 * create a ServerAuthorViewServiceException to capture the essential details that can be
 * used in the UI.
 */
public class ServerAuthorViewServiceException extends OCFCheckedExceptionBase
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     * @param messageDefinition message definition
     * @param className class name for diagnostics purposes
     * @param actionDescription current action
     */
    public ServerAuthorViewServiceException(ExceptionMessageDefinition messageDefinition,
                                            String                     className,
                                            String                     actionDescription)
    {
        super(messageDefinition, className, actionDescription);
    }



    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ServerAuthorViewServiceException{" +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", reportedErrorMessageId='" + getReportedErrorMessageId() + '\'' +
                ", reportedErrorMessageParameters=" + Arrays.toString(getReportedErrorMessageParameters()) +
                ", reportedSystemAction='" + getReportedSystemAction() + '\'' +
                ", reportedUserAction='" + getReportedUserAction() + '\'' +
                ", reportedCaughtException=" + getReportedCaughtException() +
                ", relatedProperties=" + getRelatedProperties() +
                '}';
    }

    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return true;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
}

