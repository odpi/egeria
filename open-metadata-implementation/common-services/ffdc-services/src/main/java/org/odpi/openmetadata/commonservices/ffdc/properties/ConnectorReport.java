/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ConnectorReport is a collection of information provided by a connector provider that describes the operation of
 * a connector.  It is designed to aid an administrator setting up the configuration for a connector.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ConnectorReport implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private ComponentDescription componentDescription        = null;
    private ConnectorType        connectorType               = null;
    private long                 refreshTimeInterval         = 0L;
    private boolean              usesBlockingCalls           = false;
    private List<String>         supportedRequestTypes       = null;
    private List<String>         supportedRequestParameters  = null;
    private List<String>         supportedRequestSourceNames = null;
    private List<String>         supportedActionTargetNames  = null;
    private List<String>         supportedGuards             = null;


    /**
     * Default constructor
     */
    public ConnectorReport()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectorReport(ConnectorReport template)
    {
        if (template != null)
        {
            this.componentDescription        = template.getComponentDescription();
            this.connectorType               = template.getConnectorType();
            this.refreshTimeInterval         = template.getRefreshTimeInterval();
            this.usesBlockingCalls           = template.getUsesBlockingCalls();
            this.supportedRequestTypes       = template.getSupportedRequestTypes();
            this.supportedRequestParameters  = template.getSupportedRequestParameters();
            this.supportedRequestSourceNames = template.getSupportedRequestSourceNames();
            this.supportedActionTargetNames  = template.getSupportedActionTargetNames();
            this.supportedGuards             = template.getSupportedGuards();
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
     * Return the recommended number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
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
     * Set up the recommended number of minutes between each call to the connector to refresh the metadata.  Zero means that refresh
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
     * Return if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @return boolean flag
     */
    public boolean getUsesBlockingCalls()
    {
        return usesBlockingCalls;
    }


    /**
     * Set up if the connector should be started in its own thread to allow it to block on a listening call.
     *
     * @param usesBlockingCalls boolean flag
     */
    public void setUsesBlockingCalls(boolean usesBlockingCalls)
    {
        this.usesBlockingCalls = usesBlockingCalls;
    }


    /**
     * The request types returned are those that affect the governance action service's behaviour.  Other request types may be used
     * to call the governance action service but they result in default behaviour.
     *
     * @return list of request types with special meaning
     */
    public List<String> getSupportedRequestTypes()
    {
        return supportedRequestTypes;
    }


    /**
     * Set up the supported request types.
     *
     * @param supportedRequestTypes list of request types with special meaning
     */
    public void setSupportedRequestTypes(List<String> supportedRequestTypes)
    {
        this.supportedRequestTypes = supportedRequestTypes;
    }


    /**
     * The request parameters returned are used by the governance action service to control its behaviour.
     *
     * @return list of parameter names with special meaning
     */
    public List<String> getSupportedRequestParameters()
    {
        return supportedRequestParameters;
    }


    /**
     * Set up request parameters returned are used by the governance action service to control its behaviour.
     *
     * @param supportedRequestParameters list of parameter names with special meaning
     */
    public void setSupportedRequestParameters(List<String> supportedRequestParameters)
    {
        this.supportedRequestParameters = supportedRequestParameters;
    }


    /**
     * The request source names returned are the request source names that affect the governance action service's behaviour.  Other request
     * source names may be used in a call the governance action service but they result in default behaviour.
     *
     * @return list of request source names with special meaning
     */
    public List<String> getSupportedRequestSourceNames()
    {
        return supportedRequestSourceNames;
    }


    /**
     * Set up the request source names.
     *
     * @param supportedRequestSourceNames list of request source names with special meaning
     */
    public void setSupportedRequestSourceNames(List<String> supportedRequestSourceNames)
    {
        this.supportedRequestSourceNames = supportedRequestSourceNames;
    }


    /**
     * The action target names returned are those that affect the governance action service's behaviour.  Other action target names may be used
     * in a call the governance action service but they result in default behaviour.
     *
     * @return list of action target names with special meaning
     */
    public List<String> getSupportedActionTargetNames()
    {
        return supportedActionTargetNames;
    }


    /**
     * Set up the supported action target names
     *
     * @param supportedActionTargetNames list of action target names with special meaning
     */
    public void setSupportedActionTargetNames(List<String> supportedActionTargetNames)
    {
        this.supportedActionTargetNames = supportedActionTargetNames;
    }


    /**
     * The guards describe the output assessment from the governance action service.  The list returned is the complete list of
     * guards to expect from the governance action service.  They are used when defining governance action processes that choreograph
     * the execution of governance action services using the guards to determine the path in the process to take.
     *
     * @return list of guards produced by this service
     */
    public List<String> getSupportedGuards()
    {
        return supportedGuards;
    }


    /**
     * Set up the supported guards
     *
     * @param supportedGuards list of guards produced by this service
     */
    public void setSupportedGuards(List<String> supportedGuards)
    {
        this.supportedGuards = supportedGuards;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectorReport{" +
                       "componentDescription=" + componentDescription +
                       ", connectorType=" + connectorType +
                       ", refreshTimeInterval=" + refreshTimeInterval +
                       ", usesBlockingCalls=" + usesBlockingCalls +
                       ", supportedRequestTypes=" + supportedRequestTypes +
                       ", supportedRequestParameters=" + supportedRequestParameters +
                       ", supportedRequestSourceNames=" + supportedRequestSourceNames +
                       ", supportedActionTargetNames=" + supportedActionTargetNames +
                       ", supportedGuards=" + supportedGuards +
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
        ConnectorReport that = (ConnectorReport) objectToCompare;
        return refreshTimeInterval == that.refreshTimeInterval &&
                       usesBlockingCalls == that.usesBlockingCalls &&
                       Objects.equals(componentDescription, that.componentDescription) &&
                       Objects.equals(connectorType, that.connectorType) &&
                       Objects.equals(supportedRequestTypes, that.supportedRequestTypes) &&
                       Objects.equals(supportedRequestParameters, that.supportedRequestParameters) &&
                       Objects.equals(supportedRequestSourceNames, that.supportedRequestSourceNames) &&
                       Objects.equals(supportedActionTargetNames, that.supportedActionTargetNames) &&
                       Objects.equals(supportedGuards, that.supportedGuards);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(componentDescription, connectorType, refreshTimeInterval, usesBlockingCalls, supportedRequestTypes,
                            supportedRequestParameters, supportedRequestSourceNames, supportedActionTargetNames, supportedGuards);
    }
}
