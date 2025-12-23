/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportedGovernanceServiceProperties provides a structure for carrying the properties for a SupportedGovernanceService relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedGovernanceServiceProperties extends RelationshipBeanProperties
{
    private String              requestType                      = null;
    private String              serviceRequestType               = null;
    private Map<String, String> requestParameters                = null;
    private boolean             generateConnectorActivityReports = true;
    private DeleteMethod        deleteMethod                     = null;

    /**
     * Default constructor
     */
    public SupportedGovernanceServiceProperties()
    {
        super();
        super.typeName = OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedGovernanceServiceProperties(SupportedGovernanceServiceProperties template)
    {
        if (template != null)
        {
            requestType = template.getRequestType();
            serviceRequestType = template.getServiceRequestType();
            requestParameters = template.getRequestParameters();
            deleteMethod                     = template.getDeleteMethod();
            generateConnectorActivityReports = template.getGenerateConnectorActivityReports();
        }
    }


    /**
     * Return the request type used to call the service.
     *
     * @return string
     */
    public String getRequestType()
    {
        return requestType;
    }


    /**
     * Return the request type used to call the service.
     *
     * @param requestType string
     */
    public void setRequestType(String requestType)
    {
        this.requestType = requestType;
    }

    /**
     * Return the request type that this governance service supports.  The requestType from the caller is mapped
     * to this value if not null.  This enables meaningful request types to be set up in a governance engine that then maps
     * to a request type that the governance service understands.
     *
     * @return name of the request type passed to the governance service (request type used if null)
     */
    public String getServiceRequestType()
    {
        return serviceRequestType;
    }


    /**
     * Set up the request type that this governance service supports.  The requestType from the caller is mapped
     * to this value if not null.  This enables meaningful request types to be set up in a governance engine that then maps
     * to a request type that the governance service understands.
     *
     * @param requestType name of the request type passed to the governance service (request type used if null)
     */
    public void setServiceRequestType(String requestType)
    {
        this.serviceRequestType = requestType;
    }


    /**
     * Return the parameters to pass onto the governance service.
     *
     * @return map of properties
     */
    public Map<String, String> getRequestParameters()
    {
        return requestParameters;
    }


    /**
     * Set up the parameters to pass onto the governance service.
     *
     * @param requestParameters map of properties
     */
    public void setRequestParameters(Map<String, String> requestParameters)
    {
        this.requestParameters = requestParameters;
    }


    /**
     * Return whether connector activity reports should be generated when this service is called.
     *
     * @return boolean
     */
    public boolean getGenerateConnectorActivityReports()
    {
        return generateConnectorActivityReports;
    }


    /**
     * Set up whether connector activity reports should be generated when this service is called.
     *
     * @param generateConnectorActivityReports boolean
     */
    public void setGenerateConnectorActivityReports(boolean generateConnectorActivityReports)
    {
        this.generateConnectorActivityReports = generateConnectorActivityReports;
    }


    /**
     * Return the type of delete method to use.
     *
     * @return enum
     */
    public DeleteMethod getDeleteMethod()
    {
        return deleteMethod;
    }


    /**
     * Set up the type of delete method to use.
     *
     * @param deleteMethod enum
     */
    public void setDeleteMethod(DeleteMethod deleteMethod)
    {
        this.deleteMethod = deleteMethod;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "SupportedGovernanceServiceProperties{" +
                "requestType='" + requestType + '\'' +
                ", serviceRequestType='" + serviceRequestType + '\'' +
                ", requestParameters=" + requestParameters +
                ", generateIntegrationReports=" + generateConnectorActivityReports +
                ", deleteMethod=" + deleteMethod +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        SupportedGovernanceServiceProperties that = (SupportedGovernanceServiceProperties) objectToCompare;
        return generateConnectorActivityReports == that.generateConnectorActivityReports &&
                Objects.equals(requestType, that.requestType) &&
                Objects.equals(serviceRequestType, that.serviceRequestType) &&
                Objects.equals(deleteMethod, that.deleteMethod) &&
                Objects.equals(requestParameters, that.requestParameters);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), requestType, deleteMethod, serviceRequestType, requestParameters, generateConnectorActivityReports);
    }
}
