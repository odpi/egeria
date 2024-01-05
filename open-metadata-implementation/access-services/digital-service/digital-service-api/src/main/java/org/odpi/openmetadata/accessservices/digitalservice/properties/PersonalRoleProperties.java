/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.digitalservice.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PersonalRoleProperties provides a structure for describe a role assigned to a person.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PersonRoleProperties.class, name = "PersonRoleProperties"),
        })
public class PersonalRoleProperties extends ReferenceableProperties
{
    private String               roleId        = null; /* identifier */
    private String               scope         = null; /* scope */
    private String               title         = null; /* name */
    private String               description   = null; /* description */

    private int                  domainIdentifier = 0; /* Zero means not specific to a governance domain */


    /**
     * Default constructor
     */
    public PersonalRoleProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PersonalRoleProperties(PersonalRoleProperties template)
    {
        super (template);

        if (template != null)
        {
            this.roleId               = template.getRoleId();
            this.scope                = template.getScope();
            this.title                = template.getTitle();
            this.description          = template.getDescription();
            this.domainIdentifier     = template.getDomainIdentifier();
        }
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
     * Return the context in which the person is appointed. This may be an organizational scope,
     * location, or scope of assets.
     *
     * @return string description
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the context in which the person is appointed. This may be an organizational scope,
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
     * Return the description of the job role.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the job role.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the identifier of the governance domain that this role belongs to.  Zero means that the
     * role is not specific to any domain.
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this role belongs to.  Zero means that the
     * role is not specific to any domain.
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "PersonalRoleProperties{" +
                       "roleId='" + roleId + '\'' +
                       ", scope='" + scope + '\'' +
                       ", title='" + title + '\'' +
                       ", description='" + description + '\'' +
                       ", domainIdentifier=" + domainIdentifier +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (! (objectToCompare instanceof PersonalRoleProperties))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        PersonalRoleProperties that = (PersonalRoleProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier && Objects.equals(roleId, that.roleId) &&
                       Objects.equals(scope, that.scope) && Objects.equals(title, that.title) && Objects.equals(description, that.description);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), roleId, scope, title, description, domainIdentifier);
    }
}
