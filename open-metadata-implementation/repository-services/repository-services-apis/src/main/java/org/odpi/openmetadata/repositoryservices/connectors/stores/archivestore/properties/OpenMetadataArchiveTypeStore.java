/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;

import java.util.ArrayList;
import java.util.List;

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
public class OpenMetadataArchiveTypeStore
{
    private List<AttributeTypeDef> attributeTypeDefs = null;
    private List<TypeDefPatch>     typeDefPatches    = null;
    private List<TypeDef>          newTypeDefs       = null;


    /**
     * Default constructor for OpenMetadataArchiveTypeStore relies on variables being initialized in their declaration.
     */
    public OpenMetadataArchiveTypeStore()
    {
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
        else
        {
            return new ArrayList<>(attributeTypeDefs);
        }
    }


    /**
     * Set up the list of attribute types used in this archive.
     *
     * @param attributeTypeDefs  list of AttributeTypeDef objects
     */
    public void setAttributeTypeDefs(List<AttributeTypeDef> attributeTypeDefs)
    {
        if (attributeTypeDefs == null)
        {
            this.attributeTypeDefs = null;
        }
        else
        {
            this.attributeTypeDefs = new ArrayList<>(attributeTypeDefs);
        }
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
        else
        {
            return new ArrayList<>(typeDefPatches);
        }
    }


    /**
     * Set up the list of TypeDef patches from this archive.
     *
     * @param typeDefPatches  list of TypeDef objects
     */
    public void setTypeDefPatches(List<TypeDefPatch> typeDefPatches)
    {
        if (typeDefPatches == null)
        {
            this.typeDefPatches =  null;
        }
        else
        {
            this.typeDefPatches =  new ArrayList<>(typeDefPatches);
        }
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
        else
        {
            return new ArrayList<>(newTypeDefs);
        }
    }


    /**
     * Set up the list of new TypeDefs in this open metadata archive.
     *
     * @param newTypeDefs  list of TypeDef objects
     */
    public void setNewTypeDefs(List<TypeDef> newTypeDefs)
    {
        if (newTypeDefs == null)
        {
            this.newTypeDefs = null;
        }
        else
        {
            this.newTypeDefs = new ArrayList<>(newTypeDefs);
        }
    }
}
