/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RegisteredGovernanceService describes a governance action service that has been registered with a governance engine.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegisteredGovernanceService extends GovernanceServiceProperties
{
    private static final long   serialVersionUID = 1L;

    private Map<String, Map<String, String>> requestTypes = null; /* a map from request types to analysis parameters */


    /**
     * Default constructor
     */
    public RegisteredGovernanceService()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredGovernanceService(RegisteredGovernanceService  template)
    {
        if (template != null)
        {
            requestTypes = template.getRequestTypes();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RegisteredGovernanceService(GovernanceServiceProperties  template)
    {
        super(template);
    }


    /**
     * Return the registered request types and parameters that this governance service supports.
     *
     * @return Map of request types to parameters
     */
    public Map<String, Map<String, String>> getRequestTypes()
    {
        if (requestTypes == null)
        {
            return null;
        }
        else if (requestTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(requestTypes);
        }
    }


    /**
     * Set up the registered request types and parameters that this governance service supports.
     *
     * @param requestTypes Map of request types to parameters
     */
    public void setRequestTypes(Map<String, Map<String, String>> requestTypes)
    {
        this.requestTypes = requestTypes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RegisteredGovernanceService{" +
                "requestTypes=" + requestTypes +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", owner='" + getOwner() + '\'' +
                ", ownerType=" + getOwnerCategory() +
                ", zoneMembership=" + getZoneMembership() +
                ", originOrganizationGUID='" + getOriginOrganizationGUID() + '\'' +
                ", originBusinessCapabilityGUID='" + getOriginBusinessCapabilityGUID() + '\'' +
                ", otherOriginValues=" + getOtherOriginValues() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", connection=" + getConnection() +
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
        RegisteredGovernanceService that = (RegisteredGovernanceService) objectToCompare;
        return Objects.equals(requestTypes, that.requestTypes);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), requestTypes);
    }
}
