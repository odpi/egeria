/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.collectionmanager.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewMetadataElementRequestBody provides a structure for passing the properties for a new metadata element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewMetadataElementRequestBody extends NewElementRequestBody
{
    private String                         typeName                     = null;
    private ElementStatus                  initialStatus                = null;
    private Map<String, ElementProperties> initialClassifications       = null;



    /**
     * Default constructor
     */
    public NewMetadataElementRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewMetadataElementRequestBody(NewMetadataElementRequestBody template)
    {
        super(template);

        if (template != null)
        {
            typeName = template.getTypeName();
            initialStatus = template.getInitialStatus();
            initialClassifications = template.getInitialClassifications();
        }
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
     * Return the initial status of the metadata element (typically ACTIVE).
     *
     * @return element status enum value
     */
    public ElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Return the map of classification name to properties describing the initial classification for the new metadata element.
     *
     * @return map of classification name to classification properties (or null for none)
     */
    public Map<String, ElementProperties> getInitialClassifications()
    {
        return initialClassifications;
    }


    /**
     * Set up the map of classification name to properties describing the initial classification for the new metadata element.
     *
     * @param initialClassifications map of classification name to classification properties (or null for none)
     */
    public void setInitialClassifications(Map<String, ElementProperties> initialClassifications)
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
        return "NewMetadataElementRequestBody{" +
                "typeName='" + typeName + '\'' +
                ", initialStatus=" + initialStatus +
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
        if (! (objectToCompare instanceof NewMetadataElementRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(typeName, that.typeName) &&
                       initialStatus == that.initialStatus &&
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
        return Objects.hash(super.hashCode(), typeName, initialStatus, initialClassifications);
    }
}
