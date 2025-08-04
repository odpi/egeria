/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewElementRequestBody provides a structure used when creating new elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewElementRequestBody extends NewElementOptions
{
    private OpenMetadataRootProperties            properties                   = null;
    private Map<String, ClassificationProperties> initialClassifications       = null;
    private RelationshipProperties                parentRelationshipProperties = null;


    /**
     * Default constructor
     */
    public NewElementRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewElementRequestBody(NewElementRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.properties = template.getProperties();
            this.initialClassifications = template.getInitialClassifications();
            this.parentRelationshipProperties = template.getParentRelationshipProperties();
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
     * Return any properties to include in parent relationship.
     *
     * @return relationship properties
     */
    public RelationshipProperties getParentRelationshipProperties()
    {
        return parentRelationshipProperties;
    }


    /**
     * Set up any properties to include in parent relationship.
     *
     * @param parentRelationshipProperties relationship properties
     */
    public void setParentRelationshipProperties(RelationshipProperties parentRelationshipProperties)
    {
        this.parentRelationshipProperties = parentRelationshipProperties;
    }



    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewElementRequestBody{" +
                "properties=" + properties +
                ", initialClassifications=" + initialClassifications +
                ", parentRelationshipProperties=" + parentRelationshipProperties +
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
        if (! (objectToCompare instanceof NewElementRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(properties, that.properties) &&
                Objects.equals(initialClassifications, that.initialClassifications) &&
                Objects.equals(parentRelationshipProperties, that.parentRelationshipProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), properties, initialClassifications, parentRelationshipProperties);
    }
}
