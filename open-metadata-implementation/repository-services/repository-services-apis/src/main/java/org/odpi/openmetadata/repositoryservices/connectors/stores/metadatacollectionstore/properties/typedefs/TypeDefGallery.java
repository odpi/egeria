/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

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
public class TypeDefGallery extends TypeDefElementHeader
{
    private static final long    serialVersionUID = 1L;

    private List<AttributeTypeDef> attributeTypeDefs = null;
    private List<TypeDef>          typeDefs          = null;


    /**
     * Default constructor
     */
    public TypeDefGallery()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public TypeDefGallery(TypeDefGallery    template)
    {
        if (template != null)
        {
            List<AttributeTypeDef> templateAttributeTypeDefs = template.getAttributeTypeDefs();
            List<TypeDef>          templateTypeDefs          = template.getTypeDefs();

            this.setAttributeTypeDefs(templateAttributeTypeDefs);
            this.setTypeDefs(templateTypeDefs);
        }
    }


    /**
     * Return the list of attribute type definitions from the gallery.
     *
     * @return list of attribute type definitions
     */
    public List<AttributeTypeDef> getAttributeTypeDefs()
    {
        if (attributeTypeDefs == null)
        {
            return null;
        }
        else if (attributeTypeDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(attributeTypeDefs);
        }
    }


    /**
     * Set up the list of attribute type definitions from the gallery.
     *
     * @param attributeTypeDefs list of attribute type definitions
     */
    public void setAttributeTypeDefs(List<AttributeTypeDef> attributeTypeDefs)
    {
        this.attributeTypeDefs = attributeTypeDefs;
    }


    /**
     * Return the list of type definitions from the gallery.
     *
     * @return list of type definitions
     */
    public ArrayList<TypeDef> getTypeDefs()
    {
        if (typeDefs == null)
        {
            return null;
        }
        else if (typeDefs.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(typeDefs);
        }
    }


    /**
     * Set up the list of type definitions from the gallery.
     *
     * @param typeDefs list of type definitions
     */
    public void setTypeDefs(List<TypeDef> typeDefs)
    {
        this.typeDefs = typeDefs;
    }


    /**
     * toString JSON-style
     *
     * @return String of properties
     */
    @Override
    public String toString()
    {
        return "TypeDefGallery{" +
                "attributeTypeDefs=" + attributeTypeDefs +
                ", typeDefs=" + typeDefs +
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
        if (!(objectToCompare instanceof TypeDefGallery))
        {
            return false;
        }
        TypeDefGallery that = (TypeDefGallery) objectToCompare;
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
