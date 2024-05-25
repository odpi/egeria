/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import java.util.Objects;

/**
 * A subject area defines a group of definitions for governing assets related to a specific topic.  The subject area definition defines
 * how the assets related to the topic should be managed.
 */
public class SubjectAreaProperties extends ReferenceableProperties
{
    private String subjectAreaName  = null;
    private String displayName      = null;
    private String description      = null;
    private String usage            = null;
    private String scope            = null;
    private int    domainIdentifier = 0;


    /**
     * Default constructor
     */
    public SubjectAreaProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreaProperties(SubjectAreaProperties template)
    {
        super(template);

        if (template != null)
        {
            this.subjectAreaName = template.getSubjectAreaName();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
            this.usage = template.getUsage();
            this.scope = template.getScope();
            this.domainIdentifier = template.getDomainIdentifier();
        }
    }


    /**
     * Return the name of the subject area - this is added to the SubjectArea classification.
     *
     * @return string name
     */
    public String getSubjectAreaName()
    {
        return subjectAreaName;
    }


    /**
     * Set up the name of the subject area - this is added to the SubjectArea classification.
     *
     * @param subjectAreaName string name
     */
    public void setSubjectAreaName(String subjectAreaName)
    {
        this.subjectAreaName = subjectAreaName;
    }


    /**
     * Return the short name for the governance zone.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the short name for the governance zone.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for the governance zone
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the governance zone
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return details of the usage of this subject area.
     *
     * @return text
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the details of the usage of this subject area.
     *
     * @param usage text
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the definition of the scope of this subject area
     *
     * @return scope definition
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope definition
     *
     * @param scope string definition
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the identifier of the governance domain that this subject area is managed by.
     *
     * @return int identifier
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this subject area is managed by.
     *
     * @param domainIdentifier int identifier
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SubjectAreaProperties{" +
                       "typeName='" + getTypeName() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", subjectAreaName='" + subjectAreaName + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", usage='" + usage + '\'' +
                       ", scope='" + scope + '\'' +
                       ", domainIdentifier=" + domainIdentifier +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SubjectAreaProperties that = (SubjectAreaProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(subjectAreaName, that.subjectAreaName) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(usage, that.usage) &&
                       Objects.equals(scope, that.scope);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getSubjectAreaName(), getDisplayName(), getDescription(), getUsage(), getScope(), getDomainIdentifier());
    }
}
