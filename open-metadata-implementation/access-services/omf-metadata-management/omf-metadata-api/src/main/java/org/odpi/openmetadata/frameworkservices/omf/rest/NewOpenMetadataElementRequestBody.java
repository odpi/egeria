/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewOpenMetadataElementRequestBody provides a structure for passing the properties for a new metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewOpenMetadataElementRequestBody extends NewElementOptions
{
    private String                            typeName                     = null;
    private Map<String, NewElementProperties> initialClassifications       = null;
    private NewElementProperties              properties                   = null;
    private NewElementProperties              parentRelationshipProperties = null;



    /**
     * Default constructor
     */
    public NewOpenMetadataElementRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewOpenMetadataElementRequestBody(NewOpenMetadataElementRequestBody template)
    {
        super(template);

        if (template != null)
        {
            typeName = template.getTypeName();
            initialClassifications = template.getInitialClassifications();
            properties = template.getProperties();
            parentRelationshipProperties = template.getParentRelationshipProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewOpenMetadataElementRequestBody(NewElementOptions template)
    {
        super(template);
    }


    /**
     * Return the open metadata type name for the new metadata element.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the open metadata type name for the new metadata element.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the map of classification name to properties describing the initial classification for the new metadata element.
     *
     * @return map of classification name to classification properties (or null for none)
     */
    public Map<String, NewElementProperties> getInitialClassifications()
    {
        return initialClassifications;
    }


    /**
     * Set up the map of classification name to properties describing the initial classification for the new metadata element.
     *
     * @param initialClassifications map of classification name to classification properties (or null for none)
     */
    public void setInitialClassifications(Map<String, NewElementProperties> initialClassifications)
    {
        this.initialClassifications = initialClassifications;
    }


    /**
     * Return the properties for the new metadata element.
     *
     * @return list of properties
     */
    public NewElementProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the new metadata element.
     *
     * @param properties list of properties
     */
    public void setProperties(NewElementProperties properties)
    {
        this.properties = properties;
    }



    /**
     * Return any properties that should be included in the parent relationship.
     *
     * @return element properties
     */
    public NewElementProperties getParentRelationshipProperties()
    {
        return parentRelationshipProperties;
    }


    /**
     * Set up any properties that should be included in the parent relationship.
     *
     * @param parentRelationshipProperties element properties
     */
    public void setParentRelationshipProperties(NewElementProperties parentRelationshipProperties)
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
        return "NewOpenMetadataElementRequestBody{" +
                "typeName='" + typeName + '\'' +
                ", initialClassifications=" + initialClassifications +
                ", properties=" + properties +
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
        if (! (objectToCompare instanceof NewOpenMetadataElementRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(typeName, that.typeName) &&
                       Objects.equals(initialClassifications, that.initialClassifications) &&
                       Objects.equals(properties, that.properties) &&
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
        return Objects.hash(super.hashCode(), typeName, initialClassifications, properties, parentRelationshipProperties);
    }
}
