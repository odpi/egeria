/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefGalleryResponse contains details of the AttributeTypeDefs and full TypeDefs supported by a rep
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataTypeDefGallery extends OpenMetadataTypeDefElementHeader
{
    private List<OpenMetadataAttributeTypeDef> openMetadataAttributeTypeDefs = null;
    private List<OpenMetadataTypeDef>          openMetadataTypeDefs          = null;


    /**
     * Default constructor
     */
    public OpenMetadataTypeDefGallery()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public OpenMetadataTypeDefGallery(OpenMetadataTypeDefGallery template)
    {
        if (template != null)
        {
            List<OpenMetadataAttributeTypeDef> templateOpenMetadataAttributeTypeDefs = template.getAttributeTypeDefs();
            List<OpenMetadataTypeDef>          templateOpenMetadataTypeDefs          = template.getTypeDefs();

            this.setAttributeTypeDefs(templateOpenMetadataAttributeTypeDefs);
            this.setTypeDefs(templateOpenMetadataTypeDefs);
        }
    }


    /**
     * Return the list of attribute type definitions from the gallery.
     *
     * @return list of attribute type definitions
     */
    public List<OpenMetadataAttributeTypeDef> getAttributeTypeDefs()
    {
        return openMetadataAttributeTypeDefs;
    }


    /**
     * Set up the list of attribute type definitions from the gallery.
     *
     * @param openMetadataAttributeTypeDefs list of attribute type definitions
     */
    public void setAttributeTypeDefs(List<OpenMetadataAttributeTypeDef> openMetadataAttributeTypeDefs)
    {
        this.openMetadataAttributeTypeDefs = openMetadataAttributeTypeDefs;
    }


    /**
     * Return the list of type definitions from the gallery.
     *
     * @return list of type definitions
     */
    public List<OpenMetadataTypeDef> getTypeDefs()
    {
        return openMetadataTypeDefs;
    }


    /**
     * Set up the list of type definitions from the gallery.
     *
     * @param openMetadataTypeDefs list of type definitions
     */
    public void setTypeDefs(List<OpenMetadataTypeDef> openMetadataTypeDefs)
    {
        this.openMetadataTypeDefs = openMetadataTypeDefs;
    }


    /**
     * toString JSON-style
     *
     * @return String of properties
     */
    @Override
    public String toString()
    {
        return "OpenMetadataTypeDefGallery{" +
                "openMetadataAttributeTypeDefs=" + openMetadataAttributeTypeDefs +
                ", openMetadataTypeDefs=" + openMetadataTypeDefs +
                '}';
    }


    /**
     * Verify that supplied object has the same properties.
     *
     * @param objectToCompare object to test
     * @return result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof OpenMetadataTypeDefGallery))
        {
            return false;
        }
        OpenMetadataTypeDefGallery that = (OpenMetadataTypeDefGallery) objectToCompare;
        return Objects.equals(getAttributeTypeDefs(), that.getAttributeTypeDefs()) &&
                Objects.equals(getTypeDefs(), that.getTypeDefs());
    }


    /**
     * Code number for hash maps
     *
     * @return hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getAttributeTypeDefs(), getTypeDefs());
    }
}
