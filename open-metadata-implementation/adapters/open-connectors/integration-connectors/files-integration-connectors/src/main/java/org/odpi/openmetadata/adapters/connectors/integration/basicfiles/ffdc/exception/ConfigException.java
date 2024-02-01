/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception;


import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;

import java.io.Serial;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * The ConfigException is thrown by the connector when its configuration is inadequate.
 */
public class ConfigException extends ConnectorCheckedException
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * File in error.
     */
    private final  String fileName;
    private final  String fileNameSource;


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param fileNameSource how was the file name supplied to the connector
     * @param fileName name of the invalid parameter if known
     */
    public ConfigException(ExceptionMessageDefinition messageDefinition,
                           String                     className,
                           String                     actionDescription,
                           String                     fileNameSource,
                           String                     fileName)
    {
        super(messageDefinition, className, actionDescription);

        this.fileNameSource = fileNameSource;
        this.fileName = fileName;
    }


    /**
     * This is the typical constructor used for creating an exception.
     *
     * @param messageDefinition content of message
     * @param className   name of class reporting error
     * @param actionDescription   description of function it was performing when error detected
     * @param fileNameSource how was the file name supplied to the connector
     * @param fileName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConfigException(ExceptionMessageDefinition messageDefinition,
                           String                     className,
                           String                     actionDescription,
                           String                     fileNameSource,
                           String                     fileName,
                           Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, relatedProperties);

        this.fileNameSource = fileNameSource;
        this.fileName = fileName;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     * @param fileNameSource how was the file name supplied to the connector
     * @param fileName name of the invalid parameter if known
     */
    public ConfigException(ExceptionMessageDefinition messageDefinition,
                           String                     className,
                           String                     actionDescription,
                           Exception                  caughtError,
                           String                     fileNameSource,
                           String                     fileName)
    {
        super(messageDefinition, className, actionDescription, caughtError);

        this.fileNameSource = fileNameSource;
        this.fileName = fileName;
    }


    /**
     * This is the constructor used for creating an exception that resulted from a previous error.
     *
     * @param messageDefinition content of message
     * @param className name of class reporting error
     * @param actionDescription description of function it was performing when error detected
     * @param caughtError the error that resulted in this exception.
     * @param fileNameSource how was the file name supplied to the connector
     * @param fileName name of the invalid parameter if known
     * @param relatedProperties  arbitrary properties that may help with diagnosing the problem.
     */
    public ConfigException(ExceptionMessageDefinition messageDefinition,
                           String                     className,
                           String                     actionDescription,
                           Exception                  caughtError,
                           String                     fileNameSource,
                           String                     fileName,
                           Map<String, Object>        relatedProperties)
    {
        super(messageDefinition, className, actionDescription, caughtError, relatedProperties);

        this.fileNameSource = fileNameSource;
        this.fileName = fileName;
    }


    /**
     * Return the source of the file name.
     *
     * @return string name
     */
    public String getFileNameSource()
    {
        return fileNameSource;
    }


    /**
     * Return the invalid parameter's name, if known.
     *
     * @return string name
     */
    public String getFileName()
    {
        return fileName;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "ConfigException{" +
                "fileName='" + fileName + '\'' +
                ", reportedHTTPCode=" + getReportedHTTPCode() +
                ", reportingClassName='" + getReportingClassName() + '\'' +
                ", reportingActionDescription='" + getReportingActionDescription() + '\'' +
                ", errorMessage='" + getReportedErrorMessage() + '\'' +
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
        if (!(objectToCompare instanceof ConfigException))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ConfigException that = (ConfigException) objectToCompare;
        return Objects.equals(getFileName(), that.getFileName());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getFileName());
    }
}
