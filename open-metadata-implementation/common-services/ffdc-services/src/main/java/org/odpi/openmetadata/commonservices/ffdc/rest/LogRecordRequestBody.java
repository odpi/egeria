/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LogRecordRequestBody provides a structure for passing a log record as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogRecordRequestBody
{
    private String connectorInstanceId = null;
    private String connectionName      = null;
    private String connectorType       = null;
    private String contextId           = null;
    private String message             = null;

    /**
     * Default constructor
     */
    public LogRecordRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LogRecordRequestBody(LogRecordRequestBody template)
    {
        if (template != null)
        {
            this.connectorInstanceId = template.getConnectorInstanceId();
            this.connectionName = template.getConnectionName();
            this.connectorType = template.getConnectorType();
            this.contextId = template.getContextId();
            this.message = template.getMessage();
        }
    }


    /**
     * Return the instance id of the connector making the log record.
     *
     * @return unique instance id
     */
    public String getConnectorInstanceId()
    {
        return connectorInstanceId;
    }


    /**
     * Set up the instance id of the connector making the log record.
     *
     * @param connectorInstanceId unique instance id
     */
    public void setConnectorInstanceId(String connectorInstanceId)
    {
        this.connectorInstanceId = connectorInstanceId;
    }


    /**
     * Return the name of the connection used to create the connector making the log request.
     *
     * @return connection name
     */
    public String getConnectionName()
    {
        return connectionName;
    }


    /**
     * Set up the name of the connection used to create the connector making the log request.
     *
     * @param connectionName connection name
     */
    public void setConnectionName(String connectionName)
    {
        this.connectionName = connectionName;
    }


    /**
     * Return the type of connector making the log record.
     *
     * @return connector type
     */
    public String getConnectorType()
    {
        return connectorType;
    }


    /**
     * Set up the type of connector making the log record.
     *
     * @param connectorType connector type
     */
    public void setConnectorType(String connectorType)
    {
        this.connectorType = connectorType;
    }


    /**
     * Return the function name, or processId of the activity that the caller is performing.
     *
     * @return context id
     */
    public String getContextId()
    {
        return contextId;
    }


    /**
     * Set up the function name, or processId of the activity that the caller is performing.
     *
     * @param contextId context id
     */
    public void setContextId(String contextId)
    {
        this.contextId = contextId;
    }


    /**
     * Return the actual log message.
     *
     * @return text
     */
    public String getMessage()
    {
        return message;
    }


    /**
     * Set up the actual log message.
     *
     * @param message text
     */
    public void setMessage(String message)
    {
        this.message = message;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "LogRecordRequestBody{" +
                "connectorInstanceId='" + connectorInstanceId + '\'' +
                ", connectionName='" + connectionName + '\'' +
                ", connectorType='" + connectorType + '\'' +
                ", contextId='" + contextId + '\'' +
                ", message='" + message + '\'' +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
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
        LogRecordRequestBody that = (LogRecordRequestBody) objectToCompare;
        return Objects.equals(getConnectorInstanceId(), that.getConnectorInstanceId()) &&
                Objects.equals(getConnectionName(), that.getConnectionName()) &&
                Objects.equals(getConnectorType(), that.getConnectorType()) &&
                Objects.equals(getContextId(), that.getContextId()) &&
                Objects.equals(getMessage(), that.getMessage());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getConnectorInstanceId(), getConnectionName(), getConnectorType(), getContextId(),
                            getMessage());
    }
}
