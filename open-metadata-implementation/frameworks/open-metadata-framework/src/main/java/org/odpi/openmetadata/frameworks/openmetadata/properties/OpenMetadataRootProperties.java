/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.InformalTagProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.LikeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.RatingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = InformalTagProperties.class, name = "InformalTagProperties"),
                @JsonSubTypes.Type(value = LikeProperties.class, name = "LikeProperties"),
                @JsonSubTypes.Type(value = RatingProperties.class, name = "RatingProperties"),
                @JsonSubTypes.Type(value = ReferenceableProperties.class, name = "ReferenceableProperties"),
                @JsonSubTypes.Type(value = SearchKeywordProperties.class, name = "SearchKeywordProperties"),
        })
public class OpenMetadataRootProperties extends EntityProperties
{
    private String               typeName             = null;
    private Map<String, Object>  extendedProperties   = null;

    /**
     * Default constructor
     */
    public OpenMetadataRootProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public OpenMetadataRootProperties(OpenMetadataRootProperties template)
    {
        super(template);

        if (template != null)
        {
            typeName             = template.getTypeName();
            extendedProperties   = template.getExtendedProperties();
        }
    }


    /**
     * Return the name of the open metadata type for this metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name of the open metadata type for this element.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        return extendedProperties;
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRootProperties{" +
                "typeName='" + typeName + '\'' +
                ", extendedProperties=" + extendedProperties +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        OpenMetadataRootProperties that = (OpenMetadataRootProperties) objectToCompare;
        return Objects.equals(typeName, that.typeName) && Objects.equals(extendedProperties, that.extendedProperties);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), typeName, extendedProperties);
    }
}