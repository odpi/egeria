/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewRelatedElementsRequestBody provides a structure for passing the properties for a new relationship between metadata elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewRelatedElementsRequestBody extends UpdateRequestBody
{
    private String            typeName             = null;
    private String            metadataElement1GUID = null;
    private String            metadataElement2GUID = null;
    private Date              effectiveFrom        = null;
    private Date              effectiveTo          = null;
    private ElementProperties properties           = null;



    /**
     * Default constructor
     */
    public NewRelatedElementsRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewRelatedElementsRequestBody(NewRelatedElementsRequestBody template)
    {
        super (template);

        if (template != null)
        {
            typeName = template.getTypeName();
            metadataElement1GUID = template.getMetadataElement1GUID();
            metadataElement2GUID = template.getMetadataElement2GUID();
            effectiveFrom = template.getEffectiveFrom();
            effectiveTo = template.getEffectiveTo();
            properties = template.getProperties();

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
     * Set up the unique identifier of the metadata element at end 1 of the relationship.
     *
     * @param metadataElement1GUID String guid
     */
    public void setMetadataElement1GUID(String metadataElement1GUID)
    {
        this.metadataElement1GUID = metadataElement1GUID;
    }


    /**
     * Returns the unique identifier of the metadata element at end 1 of the relationship.
     *
     * @return string guid
     */
    public String getMetadataElement1GUID()
    {
        return metadataElement1GUID;
    }



    /**
     * Set up the unique identifier of the metadata element at end 2 of the relationship.
     *
     * @param metadataElement2GUID String guid
     */
    public void setMetadataElement2GUID(String metadataElement2GUID)
    {
        this.metadataElement2GUID = metadataElement2GUID;
    }


    /**
     * Returns the unique identifier of the metadata element at end 2 of the relationship.
     *
     * @return string guid
     */
    public String getMetadataElement2GUID()
    {
        return metadataElement2GUID;
    }


    /**
     * Return the date/time that this new relationship becomes effective in the governance program (null means immediately).
     *
     * @return date object
     */
    public Date getEffectiveFrom()
    {
        return effectiveFrom;
    }


    /**
     * Set up the date/time that this new relationship becomes effective in the governance program (null means immediately).
     *
     * @param effectiveFrom date object
     */
    public void setEffectiveFrom(Date effectiveFrom)
    {
        this.effectiveFrom = effectiveFrom;
    }


    /**
     * Return the date/time when the new relationship is no longer effective in the governance program (null means until deleted).
     *
     * @return date object
     */
    public Date getEffectiveTo()
    {
        return effectiveTo;
    }


    /**
     * Set up the date/time when the new relationship is no longer effective in the governance program (null means until deleted).
     *
     * @param effectiveTo date object
     */
    public void setEffectiveTo(Date effectiveTo)
    {
        this.effectiveTo = effectiveTo;
    }


    /**
     * Return the properties for the new relationship.
     *
     * @return list of properties
     */
    public ElementProperties getProperties()
    {
        return properties;
    }


    /**
     * Set up the properties for the new relationship.
     *
     * @param properties list of properties
     */
    public void setProperties(ElementProperties properties)
    {
        this.properties = properties;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewRelatedElementsRequestBody{" +
                       "externalSourceGUID='" + getExternalSourceGUID() + '\'' +
                       ", externalSourceName='" + getExternalSourceName() + '\'' +
                       ", typeName='" + typeName + '\'' +
                       ", metadataElement1GUID='" + metadataElement1GUID + '\'' +
                       ", metadataElement2GUID='" + metadataElement2GUID + '\'' +
                       ", effectiveFrom=" + effectiveFrom +
                       ", effectiveTo=" + effectiveTo +
                       ", properties=" + properties +
                       ", forLineage=" + getForLineage() +
                       ", forDuplicateProcessing=" + getForDuplicateProcessing() +
                       ", effectiveTime=" + getEffectiveTime() +
                       '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        NewRelatedElementsRequestBody that = (NewRelatedElementsRequestBody) objectToCompare;
        return Objects.equals(typeName, that.typeName) &&
                       Objects.equals(metadataElement1GUID, that.metadataElement1GUID) &&
                       Objects.equals(metadataElement2GUID, that.metadataElement2GUID) &&
                       Objects.equals(effectiveFrom, that.effectiveFrom) &&
                       Objects.equals(effectiveTo, that.effectiveTo) &&
                       Objects.equals(properties, that.properties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), typeName, metadataElement1GUID, metadataElement2GUID, effectiveFrom, effectiveTo, properties);
    }
}
