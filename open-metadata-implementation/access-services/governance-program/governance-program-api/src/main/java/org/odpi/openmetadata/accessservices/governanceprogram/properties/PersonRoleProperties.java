/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Date;
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
public class PersonRoleProperties
{
    private String               qualifiedName = null; /* qualifiedName */
    private String               roleId        = null; /* identifier */
    private String               scope         = null; /* scope */
    private String               title         = null; /* name */
    private String               description   = null; /* description */

    private boolean              headCountLimitSet = false;
    private int                  headCount = 1;

    private Map<String, String>  additionalProperties = null;

    private Date                 effectiveFrom = null;
    private Date                 effectiveTo   = null;

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
            this.qualifiedName        = template.getQualifiedName();
            this.roleId               = template.getRoleId();
            this.scope                = template.getScope();
            this.title                = template.getTitle();
            this.description          = template.getDescription();
            this.headCount            = template.getHeadCount();
            this.headCountLimitSet    = template.getHeadCountLimitSet();
            this.additionalProperties = template.getAdditionalProperties();
            this.effectiveFrom        = template.getEffectiveFrom();
            this.effectiveTo          = template.getEffectiveTo();
            this.typeName             = template.getTypeName();
            this.extendedProperties   = template.getExtendedProperties();
        }
    }


    /**
     * Return the unique name for element in open metadata ecosystem.
     *
     * @return unique name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name for element in open metadata ecosystem.
     *
     * @param qualifiedName unique name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return the unique identifier for this job role/appointment typically from an HR system.
     *
     * @return unique identifier
     */
    public String getRoleId()
    {
        return roleId;
    }


    /**
     * Set up the unique identifier for this job role/appointment.
     *
     * @param roleId unique identifier
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
     * Return the indicator whether the head count limit is set for a person role.
     *
     * @return boolean flag
     */
    public boolean getHeadCountLimitSet()
    {
        return headCountLimitSet;
    }


    /**
     * Set up the indicator whether the head count limit is set for a person role.
     *
     * @param headCountLimitSet boolean flag
     */
    public void setHeadCountLimitSet(boolean headCountLimitSet)
    {
        this.headCountLimitSet = headCountLimitSet;
    }


    /**
     * Return the number of people that can be appointed to this role.
     *
     * @return int
     */
    public int getHeadCount()
    {
        return headCount;
    }


    /**
     * Set up the number of people that can be appointed to this role.
     *
     * @param headCount int
     */
    public void setHeadCount(int headCount)
    {
        this.headCount = headCount;
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
     * Return the date/time that this element is effective from (null means effective from the epoch).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this element is effective from (null means effective from the epoch).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set the date/time that element is effective to (null means that it is effective indefinitely into the future).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
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
                       "qualifiedName='" + qualifiedName + '\'' +
                       ", roleId='" + roleId + '\'' +
                       ", scope='" + scope + '\'' +
                       ", title='" + title + '\'' +
                       ", description='" + description + '\'' +
                       ", headCountLimitSet=" + headCountLimitSet +
                       ", headCount=" + headCount +
                       ", additionalProperties=" + additionalProperties +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
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
        return headCountLimitSet == that.headCountLimitSet &&
                       headCount == that.headCount &&
                       Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(roleId, that.roleId) &&
                       Objects.equals(scope, that.scope) &&
                       Objects.equals(title, that.title) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(additionalProperties, that.additionalProperties) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
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
        return Objects.hash(qualifiedName, roleId, scope, title, description, headCountLimitSet, headCount, additionalProperties, effectiveFrom, effectiveTo, typeName, extendedProperties);
    }
}
