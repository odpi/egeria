/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredDiscoveryService describes a discovery service that has been registered with a discovery engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredDiscoveryService extends DiscoveryServiceProperties
{
    private static final long   serialVersionUID = 1L;

    private Map<String, Map<String, String>> discoveryRequestTypes = null; /* a map from request types to analysis parameters */


    /**
     * Default constructor
     */
    public RegisteredDiscoveryService()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredDiscoveryService(RegisteredDiscoveryService  template)
    {
        super(template);

        if (template != null)
        {
            discoveryRequestTypes = template.getDiscoveryRequestTypes();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredDiscoveryService(DiscoveryServiceProperties  template)
    {
        super(template);
    }


    /**
     * Return the list of asset discovery types that this discovery service supports.
     *
     * @return Map of request types to analysis parameters
     */
    public Map<String, Map<String, String>> getDiscoveryRequestTypes()
    {
        if (discoveryRequestTypes == null)
        {
            return null;
        }
        else if (discoveryRequestTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(discoveryRequestTypes);
        }
    }


    /**
     * Set up the list of asset types that this discovery service supports.
     *
     * @param discoveryRequestTypes Map of request types to analysis parameters
     */
    public void setDiscoveryRequestTypes(Map<String, Map<String, String>> discoveryRequestTypes)
    {
        this.discoveryRequestTypes = discoveryRequestTypes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RegisteredDiscoveryService{" +
                "discoveryRequestTypes=" + discoveryRequestTypes +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", connection=" + getConnection() +
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        RegisteredDiscoveryService that = (RegisteredDiscoveryService) objectToCompare;
        return Objects.equals(discoveryRequestTypes, that.discoveryRequestTypes);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), discoveryRequestTypes);
    }
}
