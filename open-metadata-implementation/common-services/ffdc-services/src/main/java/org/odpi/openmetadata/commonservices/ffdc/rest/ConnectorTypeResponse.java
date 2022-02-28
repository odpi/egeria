/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ConnectorTypeResponse is the response structure used on REST API calls that return a
 * ConnectorType object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorTypeResponse extends FFDCResponseBase
{
    private static final long    serialVersionUID = 1L;

    private ComponentDescription     componentDescription        = null;
    private ConnectorType            connectorType               = null;
    private long                     refreshTimeInterval         = 0L;
    private boolean                  usesBlockingCalls           = false;

    /**
     * Default constructor
     */
    public ConnectorTypeResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorTypeResponse(ConnectorTypeResponse template)
    {
        super(template);

        if (template != null)
        {
            this.componentDescription     = template.getComponentDescription();
            this.connectorType            = template.getConnectorType();
            this.refreshTimeInterval      = template.getRefreshTimeInterval();
            this.usesBlockingCalls        = template.getUsesBlockingCalls();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorTypeResponse(ConnectorReport template)
    {
        if (template != null)
        {
            this.componentDescription     = template.getComponentDescription();
            this.connectorType            = template.getConnectorType();
            this.refreshTimeInterval      = template.getRefreshTimeInterval();
            this.usesBlockingCalls        = template.getUsesBlockingCalls();
        }
    }


    /**
     * Return the component description information that the connector uses to register with the audit log.
     *
     * @return component description structure
     */
    public ComponentDescription getComponentDescription()
    {
        return componentDescription;
    }


    /**
     * Set up the component description information that the connector uses to register with the audit log.
     *
     * @param componentDescription component description structure
     */
    public void setComponentDescription(ComponentDescription componentDescription)
    {
        this.componentDescription = componentDescription;
    }


    /**
     * Return the ConnectorType object.
     *
     * @return connectorType
     */
    public ConnectorType getConnectorType()
    {
        return connectorType;
    }


    /**
     * Set up the ConnectorType object.
     *
     * @param connectorType - connectorType object
     */
    public void setConnectorType(ConnectorType connectorType)
    {
        this.connectorType = connectorType;
    }


    /**
     * Return the number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @return minute count
     */
    public long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
     * is only called at server start up and whenever the refresh REST API request is made to the integration daemon.
     * If the refresh time interval is greater than 0 then additional calls to refresh are added spaced out by the refresh time interval.
     *
     * @param refreshTimeInterval minute count
     */
    public void setRefreshTimeInterval(long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Return if the connector should be started in its own thread to allow it is block on a listening call.
     *
     * @return boolean flag
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it is block on a listening call.
     *
     * @param usesBlockingCalls boolean flag
     */
    public void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectorTypeResponse{" +
                       "componentDescription=" + componentDescription +
                       ", connectorType=" + connectorType +
                       ", refreshTimeInterval=" + refreshTimeInterval +
                       ", usesBlockingCalls=" + usesBlockingCalls +
                       ", exceptionClassName='" + getExceptionClassName() + '\'' +
                       ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                       ", actionDescription='" + getActionDescription() + '\'' +
                       ", relatedHTTPCode=" + getRelatedHTTPCode() +
                       ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                       ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                       ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                       ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                       ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                       ", exceptionProperties=" + getExceptionProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ConnectorTypeResponse that = (ConnectorTypeResponse) objectToCompare;
        return refreshTimeInterval == that.refreshTimeInterval &&
                       usesBlockingCalls == that.usesBlockingCalls &&
                       Objects.equals(componentDescription, that.componentDescription) &&
                       Objects.equals(connectorType, that.connectorType);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), componentDescription, connectorType, refreshTimeInterval, usesBlockingCalls);
    }
}
