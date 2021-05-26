/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonRoleProperties provides a structure for describe a role assigned to a person.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = GovernanceRoleProperties.class, name = "GovernanceRoleProperties")
        })
public abstract class PersonRoleProperties implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private int                  domainIdentifier = 0;

    private String               roleId      = null; /* qualifiedName */
    private String               scope       = null; /* scope */
    private String               title       = null; /* name */
    private String               description = null; /* description */

    private Map<String, String>  additionalProperties = null;

    private String               typeName             = null;
    private Map<String, Object>  extendedProperties   = null;


    /**
     * Default constructor
     */
    public PersonRoleProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonRoleProperties(PersonRoleProperties template)
    {
        if (template != null)
        {
            this.domainIdentifier     = template.getDomainIdentifier();
            this.roleId               = template.getRoleId();
            this.scope                = template.getScope();
            this.title                = template.getTitle();
            this.description          = template.getDescription();
            this.additionalProperties = template.getAdditionalProperties();
            this.typeName             = template.getTypeName();
            this.extendedProperties   = template.getExtendedProperties();
        }
    }


    /**
     * Return the identifier of the governance domain that this zone is managed by.
     *
     * @return int identifier
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this zone is managed by.
     *
     * @param domainIdentifier int identifier
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the unique identifier for this job role/appointment.
     *
     * @return unique name
     */
    public String getRoleId()
    {
        return roleId;
    }


    /**
     * Set up the unique identifier for this job role/appointment.
     *
     * @param roleId unique name
     */
    public void setRoleId(String roleId)
    {
        this.roleId = roleId;
    }


    /**
     * Return the context in which the governance officer is appointed. This may be an organizational scope,
     * location, or scope of assets.
     *
     * @return string description
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the context in which the governance officer is appointed. This may be an organizational scope,
     * location, or scope of assets.
     *
     * @param scope string description
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the job role title.
     *
     * @return string name
     */
    public String getTitle()
    {
        return title;
    }


    /**
     * Set up the job role title.
     *
     * @param title string name
     */
    public void setTitle(String title)
    {
        this.title = title;
    }


    /**
     * Return the description of the job role for this governance appointment.  This may relate to the specific
     * governance responsibilities, or may be their main role if the governance responsibilities are
     * just an adjunct to their main role.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the job role for this governance officer.  This may relate to the specific
     * governance responsibilities, or may be their main role if the governance responsibilities are
     * just an adjunct to their main role.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }



    /**
     * Return the open metadata type name of this object - this is used to create a subtype of
     * the referenceable.  Any properties associated with this subtype are passed as extended properties.
     *
     * @return string type name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the open metadata type name of this object - this is used to create a subtype of
     * the referenceable.  Any properties associated with this subtype are passed as extended properties.
     *
     * @param typeName string type name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the properties that are defined for a subtype of referenceable but are not explicitly
     * supported by the bean.
     *
     * @return map of properties
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that are defined for a subtype of referenceable but are not explicitly
     * supported by the bean.
     *
     * @param extendedProperties map of properties
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonRoleProperties{" +
                       "domainIdentifier=" + domainIdentifier +
                       ", roleId='" + roleId + '\'' +
                       ", scope='" + scope + '\'' +
                       ", title='" + title + '\'' +
                       ", description='" + description + '\'' +
                       ", additionalProperties=" + additionalProperties +
                       ", typeName='" + typeName + '\'' +
                       ", extendedProperties=" + extendedProperties +
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
        PersonRoleProperties that = (PersonRoleProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(roleId, that.roleId) &&
                       Objects.equals(scope, that.scope) &&
                       Objects.equals(title, that.title) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(additionalProperties, that.additionalProperties) &&
                       Objects.equals(typeName, that.typeName) &&
                       Objects.equals(extendedProperties, that.extendedProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(domainIdentifier, roleId, scope, title, description, additionalProperties, typeName,
                            extendedProperties);
    }
}
