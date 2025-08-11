/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.FindDigitalResourceOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagQueryProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * FindProperties provides the base class for find by property requests.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = LevelIdentifierQueryProperties.class, name = "LevelIdentifierQueryProperties"),
                @JsonSubTypes.Type(value = FindNameProperties.class, name = "FindNameProperties"),
                @JsonSubTypes.Type(value = FindDigitalResourceOriginProperties.class, name = "FindDigitalResourceOriginProperties"),
                @JsonSubTypes.Type(value = FindPropertyNamesProperties.class, name = "FindPropertyNamesProperties"),
                @JsonSubTypes.Type(value = SecurityTagQueryProperties.class, name = "SecurityTagQueryProperties"),
                @JsonSubTypes.Type(value = SemanticAssignmentQueryProperties.class, name = "SemanticAssignmentQueryProperties"),
        })
public class FindProperties extends QueryOptions
{
    private String openMetadataTypeName = null;

    /**
     * Default constructor
     */
    public FindProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieve values from the supplied template
     *
     * @param template element to copy
     */
    public FindProperties(FindProperties template)
    {
        super (template);

        if (template != null)
        {
            openMetadataTypeName = template.getOpenMetadataTypeName();
        }
    }


    /**
     * Return the open metadata type name to filter by.
     *
     * @return string name
     */
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Set up the open metadata type name to filer by.
     *
     * @param openMetadataTypeName string name
     */
    public void setOpenMetadataTypeName(String openMetadataTypeName)
    {
        this.openMetadataTypeName = openMetadataTypeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "FindProperties{" +
                "openMetadataTypeName='" + openMetadataTypeName + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        FindProperties that = (FindProperties) objectToCompare;
        return Objects.equals(openMetadataTypeName, that.openMetadataTypeName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), openMetadataTypeName);
    }
}