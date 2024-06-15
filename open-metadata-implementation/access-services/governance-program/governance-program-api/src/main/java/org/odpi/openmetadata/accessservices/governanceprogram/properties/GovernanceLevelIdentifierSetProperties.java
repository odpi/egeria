/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import java.util.Objects;

/**
 * GovernanceLevelIdentifierSetProperties describes a collection of level identifiers for a specific governance classification.
 */
public class GovernanceLevelIdentifierSetProperties extends ReferenceableProperties
{
    private int    domainIdentifier       = 0;
    private String classificationName     = null;
    private String identifierPropertyName = null;
    private String displayName            = null;
    private String description            = null;


    /**
     * Default constructor
     */
    public GovernanceLevelIdentifierSetProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceLevelIdentifierSetProperties(GovernanceLevelIdentifierSetProperties template)
    {
        super(template);

        if (template != null)
        {
            this.domainIdentifier = template.getDomainIdentifier();
            this.classificationName = template.getClassificationName();
            this.identifierPropertyName = template.getIdentifierPropertyName();
            this.displayName = template.getDisplayName();
            this.description = template.getDescription();
        }
    }


    /**
     * Return the identifier of the governance domain that this definition belongs to (0=all).
     *
     * @return int
     */
    public int getDomainIdentifier()
    {
        return domainIdentifier;
    }


    /**
     * Set up the identifier of the governance domain that this definition belongs to (0=all).
     *
     * @param domainIdentifier int
     */
    public void setDomainIdentifier(int domainIdentifier)
    {
        this.domainIdentifier = domainIdentifier;
    }


    /**
     * Return the name of the classification that will use this set of level identifiers.
     *
     * @return string name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up the name of the classification that will use this set of level identifiers.
     *
     * @param classificationName string name
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * Return the name of the property in the classification where the level identifier will be set.
     *
     * @return string name
     */
    public String getIdentifierPropertyName()
    {
        return identifierPropertyName;
    }


    /**
     * Set up the name of the property in the classification where the level identifier will be set.
     *
     * @param identifierPropertyName string name
     */
    public void setIdentifierPropertyName(String identifierPropertyName)
    {
        this.identifierPropertyName = identifierPropertyName;
    }


    /**
     * Return the short name for the governance domain set.
     *
     * @return string name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the short name for the governance domain set.
     *
     * @param displayName string name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the description for the governance domain set
     *
     * @return text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the governance domain set
     *
     * @param description text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceLevelIdentifierSetProperties{" +
                       "domainIdentifier=" + domainIdentifier +
                       ", classificationName='" + classificationName + '\'' +
                       ", identifierPropertyName='" + identifierPropertyName + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", extendedProperties=" + getExtendedProperties() +
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
        GovernanceLevelIdentifierSetProperties that = (GovernanceLevelIdentifierSetProperties) objectToCompare;
        return domainIdentifier == that.domainIdentifier &&
                       Objects.equals(classificationName, that.classificationName) &&
                       Objects.equals(identifierPropertyName, that.identifierPropertyName) &&
                       Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDomainIdentifier(), getClassificationName(),
                            getIdentifierPropertyName(), getDisplayName(), getDescription());
    }
}
