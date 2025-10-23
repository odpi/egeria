/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewAttachmentRequestBody provides a structure used when creating new elements where the parent relationship is fixed.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = NewFeedbackRequestBody.class, name = "NewFeedbackRequestBody"),
        })
public class NewAttachmentRequestBody extends MetadataSourceOptions
{
    private OpenMetadataRootProperties            properties             = null;
    private Map<String, ClassificationProperties> initialClassifications = null;

    /**
     * Default constructor
     */
    public NewAttachmentRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewAttachmentRequestBody(NewAttachmentRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
            this.initialClassifications = template.getInitialClassifications();
        }
    }


    /**
     * Return the properties of the new element.
     *
     * @return properties
     */
    public OpenMetadataRootProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties of the new element.
     *
     * @param properties properties
     */
    public void setProperties(OpenMetadataRootProperties properties)
    {
        this.properties = properties;
    }


    /**
     * Return the map of classification names to classification properties to include in the entity creation request.
     *
     * @return map
     */
    public Map<String, ClassificationProperties> getInitialClassifications()
    {
        return initialClassifications;
    }


    /**
     * Set up map of classification names to classification properties to include in the entity creation request.
     *
     * @param initialClassifications map
     */
    public void setInitialClassifications(Map<String, ClassificationProperties> initialClassifications)
    {
        this.initialClassifications = initialClassifications;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewFeedbackRequestBody{" +
                "properties=" + properties +
                ", initialClassifications=" + initialClassifications +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof NewAttachmentRequestBody that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(properties, that.properties) &&
                Objects.equals(initialClassifications, that.initialClassifications);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, initialClassifications);
    }
}
