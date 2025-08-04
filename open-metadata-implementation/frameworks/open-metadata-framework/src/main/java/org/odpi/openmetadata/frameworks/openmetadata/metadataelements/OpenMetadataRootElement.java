/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataRootElement contains the properties and header for an element retrieved from the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ActorProfileElement.class, name = "ActorProfileElement"),
                @JsonSubTypes.Type(value = ActorRoleElement.class, name = "ActorRoleElement"),
                @JsonSubTypes.Type(value = AssetElement.class, name = "AssetElement"),
                @JsonSubTypes.Type(value = CollectionHierarchy.class, name = "CollectionHierarchy"),
                @JsonSubTypes.Type(value = ExternalIdElement.class, name = "ExternalIdElement"),
                @JsonSubTypes.Type(value = GovernanceDefinitionGraph.class, name = "GovernanceDefinitionGraph"),
                @JsonSubTypes.Type(value = TemplateElement.class, name = "TemplateElement"),
        })
public class OpenMetadataRootElement extends AttributedMetadataElement
{
    private OpenMetadataRootProperties properties = null;

    /**
     * Default constructor
     */
    public OpenMetadataRootElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataRootElement(OpenMetadataRootElement template)
    {
        super(template);

        if (template != null)
        {
            properties = template.getProperties();
        }
    }


    /**
     * Return the properties for the asset.
     *
     * @return asset properties (using appropriate subclass)
     */
    public OpenMetadataRootProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the asset.
     *
     * @param properties asset properties
     */
    public void setProperties(OpenMetadataRootProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRootElement{" +
                "properties=" + properties +
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
        OpenMetadataRootElement that = (OpenMetadataRootElement) objectToCompare;
        return Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties);
    }
}
