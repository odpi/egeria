/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ValidValueAssociationProperties is a java bean used to associate two valid values.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValueAssociationProperties extends RelationshipBeanProperties
{
    private String              associationName  = null;
    private String              associationType      = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public ValidValueAssociationProperties()
    {
        super();
        super.typeName = OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public ValidValueAssociationProperties(ValidValueAssociationProperties template)
    {
        super(template);

        if (template != null)
        {
            associationName  = template.getAssociationName();
            associationType      = template.getAssociationType();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Returns the name for the association.
     *
     * @return String name
     */
    public String getAssociationName()
    {
        return associationName;
    }


    /**
     * Set up the name for the association.
     *
     * @param associationName String name
     */
    public void setAssociationName(String associationName)
    {
        this.associationName = associationName;
    }


    /**
     * Returns the name for association.
     *
     * @return String value
     */
    public String getAssociationType()
    {
        return associationType;
    }


    /**
     * Set up the type of association.
     *
     * @param associationType String value
     */
    public void setAssociationType(String associationType)
    {
        this.associationType = associationType;
    }


    /**
     * Return the additional values associated with the association.
     *
     * @return name-value pairs for additional values
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up the additional values associated with the association.
     *
     * @param additionalProperties name-value pairs for additional values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ValidValueAssociationProperties{" +
                "associationName='" + associationName + '\'' +
                ", associationType='" + associationType + '\'' +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
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
        ValidValueAssociationProperties asset = (ValidValueAssociationProperties) objectToCompare;
        return Objects.equals(getAssociationName(), asset.getAssociationName()) &&
                Objects.equals(getAssociationType(), asset.getAssociationType()) &&
                Objects.equals(getAdditionalProperties(), asset.getAdditionalProperties());
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAssociationName(), getAssociationType(), getAdditionalProperties());
    }
}
