/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.odcs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolCustomProperty;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents an access (IAM) role that provides access to the data described by an Open Data Contract
 * Standard (ODCS) data contract. Roles may be defined for the contract as a whole or for an individual server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataContractRole
{
    private String                    id = null;
    private String                    role = null;
    private String                    description = null;
    private String                    access = null;
    private String                    firstLevelApprovers = null;
    private String                    secondLevelApprovers = null;
    private List<BitolCustomProperty> customProperties = null;


    /**
     * Default constructor
     */
    public DataContractRole()
    {
    }


    /**
     * Return the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @return string identifier
     */
    public String getId()
    {
        return id;
    }


    /**
     * Set up the stable identifier of this element (alphanumeric, hyphen and underscore characters only).
     *
     * @param id string identifier
     */
    public void setId(String id)
    {
        this.id = id;
    }


    /**
     * Return the name of the IAM role that provides access to the data.
     *
     * @return string role name
     */
    public String getRole()
    {
        return role;
    }


    /**
     * Set up the name of the IAM role that provides access to the data.
     *
     * @param role string role name
     */
    public void setRole(String role)
    {
        this.role = role;
    }


    /**
     * Return the description of the role and its permissions.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the role and its permissions.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the type of access provided by the role, for example read or write.
     *
     * @return string access type
     */
    public String getAccess()
    {
        return access;
    }


    /**
     * Set up the type of access provided by the role, for example read or write.
     *
     * @param access string access type
     */
    public void setAccess(String access)
    {
        this.access = access;
    }


    /**
     * Return the name(s) of the first-level approver(s) of requests for the role.
     *
     * @return string names
     */
    public String getFirstLevelApprovers()
    {
        return firstLevelApprovers;
    }


    /**
     * Set up the name(s) of the first-level approver(s) of requests for the role.
     *
     * @param firstLevelApprovers string names
     */
    public void setFirstLevelApprovers(String firstLevelApprovers)
    {
        this.firstLevelApprovers = firstLevelApprovers;
    }


    /**
     * Return the name(s) of the second-level approver(s) of requests for the role.
     *
     * @return string names
     */
    public String getSecondLevelApprovers()
    {
        return secondLevelApprovers;
    }


    /**
     * Set up the name(s) of the second-level approver(s) of requests for the role.
     *
     * @param secondLevelApprovers string names
     */
    public void setSecondLevelApprovers(String secondLevelApprovers)
    {
        this.secondLevelApprovers = secondLevelApprovers;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataContractRole{" +
                       "id='" + id + '\'' +
                       ", role='" + role + '\'' +
                       ", description='" + description + '\'' +
                       ", access='" + access + '\'' +
                       ", firstLevelApprovers='" + firstLevelApprovers + '\'' +
                       ", secondLevelApprovers='" + secondLevelApprovers + '\'' +
                       ", customProperties=" + customProperties +
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
        DataContractRole that = (DataContractRole) objectToCompare;
        return Objects.equals(id, that.id) &&
                       Objects.equals(role, that.role) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(access, that.access) &&
                       Objects.equals(firstLevelApprovers, that.firstLevelApprovers) &&
                       Objects.equals(secondLevelApprovers, that.secondLevelApprovers) &&
                       Objects.equals(customProperties, that.customProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(id, role, description, access, firstLevelApprovers, secondLevelApprovers, customProperties);
    }
}
