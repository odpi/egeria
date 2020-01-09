/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OpenMetadataArchiveTypeStore defines the contents of the TypeStore in an open metadata archive.  The TypeStore
 * contains a list of types used for attributes, a list of type definition (TypeDef) patches to update existing types
 * and a list of TypeDefs for new types of classifications, entities and relationships.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchiveTypeStore extends OpenMetadataArchiveElementHeader
{
    private static final long    serialVersionUID = 1L;

    private List<AttributeTypeDef> attributeTypeDefs = null;
    private List<TypeDefPatch>     typeDefPatches    = null;
    private List<TypeDef>          newTypeDefs       = null;


    /**
     * Default constructor for OpenMetadataArchiveTypeStore relies on variables being initialized in their declaration.
     */
    public OpenMetadataArchiveTypeStore()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataArchiveTypeStore(OpenMetadataArchiveTypeStore   template)
    {
        super(template);

        if (template != null)
        {
            attributeTypeDefs = template.getAttributeTypeDefs();
            typeDefPatches = template.getTypeDefPatches();
            newTypeDefs = template.getNewTypeDefs();
        }
    }


    /**
     * Return the list of attribute types used in this archive.
     *
     * @return list of AttributeTypeDef objects
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
            List<AttributeTypeDef>  clonedList = new ArrayList<>();

            for (AttributeTypeDef  existingElement : attributeTypeDefs)
            {
                clonedList.add(existingElement.cloneFromSubclass());
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of attribute types used in this archive.
     *
     * @param attributeTypeDefs  list of AttributeTypeDef objects
     */
    public void setAttributeTypeDefs(List<AttributeTypeDef> attributeTypeDefs)
    {
        this.attributeTypeDefs = attributeTypeDefs;
    }


    /**
     * Return the list of TypeDef patches from this archive.
     *
     * @return list of TypeDef objects
     */
    public List<TypeDefPatch> getTypeDefPatches()
    {
        if (typeDefPatches == null)
        {
            return null;
        }
        else if (typeDefPatches.isEmpty())
        {
            return null;
        }
        else
        {
            List<TypeDefPatch>  clonedList = new ArrayList<>();

            for (TypeDefPatch  existingElement : typeDefPatches)
            {
                clonedList.add(new TypeDefPatch(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of TypeDef patches from this archive.
     *
     * @param typeDefPatches  list of TypeDef objects
     */
    public void setTypeDefPatches(List<TypeDefPatch> typeDefPatches)
    {
        this.typeDefPatches = typeDefPatches;
    }


    /**
     * Return the list of new TypeDefs in this open metadata archive.
     *
     * @return list of TypeDef objects
     */
    public List<TypeDef> getNewTypeDefs()
    {
        if (newTypeDefs == null)
        {
            return null;
        }
        else if (newTypeDefs.isEmpty())
        {
            return null;
        }
        else
        {
            List<TypeDef>  clonedList = new ArrayList<>();

            for (TypeDef  existingElement : newTypeDefs)
            {
                clonedList.add(existingElement.cloneFromSubclass());
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of new TypeDefs in this open metadata archive.
     *
     * @param newTypeDefs  list of TypeDef objects
     */
    public void setNewTypeDefs(List<TypeDef> newTypeDefs)
    {
        this.newTypeDefs = newTypeDefs;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataArchiveTypeStore{" +
                "attributeTypeDefs=" + attributeTypeDefs +
                ", typeDefPatches=" + typeDefPatches +
                ", newTypeDefs=" + newTypeDefs +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OpenMetadataArchiveTypeStore that = (OpenMetadataArchiveTypeStore) objectToCompare;
        return Objects.equals(getAttributeTypeDefs(), that.getAttributeTypeDefs()) &&
                Objects.equals(getTypeDefPatches(), that.getTypeDefPatches()) &&
                Objects.equals(getNewTypeDefs(), that.getNewTypeDefs());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getAttributeTypeDefs(), getTypeDefPatches(), getNewTypeDefs());
    }
}
