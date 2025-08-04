/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

/**
 * A subject area defines a group of definitions for governing assets related to a specific topic.  The subject area definition defines
 * how the assets related to the topic should be managed.
 */
public class SubjectAreaDefinitionProperties extends ReferenceableProperties
{
    private String subjectAreaName  = null;
    private String usage            = null;
    private String scope            = null;
    private int    domainIdentifier = 0;


    /**
     * Default constructor
     */
    public SubjectAreaDefinitionProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreaDefinitionProperties(SubjectAreaDefinitionProperties template)
    {
        super(template);

        if (template != null)
        {
            this.subjectAreaName = template.getSubjectAreaName();
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
        return "SubjectAreaDefinitionProperties{" +
                "subjectAreaName='" + subjectAreaName + '\'' +
                ", usage='" + usage + '\'' +
                ", scope='" + scope + '\'' +
                ", domainIdentifier=" + domainIdentifier +
                "} " + super.toString();
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
        SubjectAreaDefinitionProperties that = (SubjectAreaDefinitionProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(subjectAreaName, that.subjectAreaName) &&
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
        return Objects.hash(super.hashCode(), getSubjectAreaName(),getUsage(), getScope(), getDomainIdentifier());
    }
}
