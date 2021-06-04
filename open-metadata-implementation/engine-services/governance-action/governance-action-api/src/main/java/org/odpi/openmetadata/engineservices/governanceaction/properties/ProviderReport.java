/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * ProviderReport provides details og a governance action service that could potentially run in the Governance Action Open Metadata Engine Service (OMES).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProviderReport implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private ConnectorType connectorType = null;
    private List<String>  supportedRequestTypes = null;
    private List<String>  supportedRequestParameters = null;
    private List<String>  supportedRequestSourceNames = null;
    private List<String>  supportedActionTargetNames = null;
    private List<String>  supportedGuards = null;


    /**
     * Default constructor
     */
    public ProviderReport()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProviderReport(ProviderReport template)
    {
        if (template != null)
        {
            connectorType = template.getConnectorType();
            supportedRequestTypes = template.getSupportedRequestTypes();
            supportedRequestParameters = template.getSupportedRequestParameters();
            supportedRequestSourceNames = template.getSupportedRequestSourceNames();
            supportedActionTargetNames = template.getSupportedActionTargetNames();
            supportedGuards = template.getSupportedGuards();
        }
    }


    /**
     * Return the connector type for this connector.
     *
     * @return OCF ConnectorType object that can be used in connection objects
     */
    public ConnectorType getConnectorType()
    {
        return connectorType;
    }


    /**
     * Set up the connector type for this connector.
     *
     * @param connectorType OCF ConnectorType object
     */
    public void setConnectorType(ConnectorType connectorType)
    {
        this.connectorType = connectorType;
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
        return "ProviderReport{" +
                       "connectorType=" + connectorType +
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
        ProviderReport that = (ProviderReport) objectToCompare;
        return Objects.equals(connectorType, that.connectorType) &&
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
        return Objects.hash(connectorType, supportedRequestTypes, supportedRequestParameters, supportedRequestSourceNames, supportedActionTargetNames,
                            supportedGuards);
    }
}
