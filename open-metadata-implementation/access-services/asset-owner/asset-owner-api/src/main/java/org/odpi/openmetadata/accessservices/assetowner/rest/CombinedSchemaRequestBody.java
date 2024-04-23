/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaAttributeProperties;
import org.odpi.openmetadata.accessservices.assetowner.properties.SchemaTypeProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CombinedSchemaRequestBody carries the parameters for created a new schema to attach to
 * an asset.  A limited number of SchemaAttributes can accompany the SchemaType.
 * If more SchemaAttributes are needed then use addSchemaAttributesToSchema() method.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CombinedSchemaRequestBody extends AssetOwnerOMASAPIRequestBody
{
    protected SchemaTypeProperties            schemaType       = null;
    protected List<SchemaAttributeProperties> schemaAttributes = null;


    /**
     * Default constructor
     */
    public CombinedSchemaRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CombinedSchemaRequestBody(CombinedSchemaRequestBody template)
    {
        super(template);

        if (template != null)
        {
            schemaType = template.getSchemaType();
            schemaAttributes = template.getSchemaAttributes();
        }
    }


    /**
     * Return the type of owner stored in the owner property.
     *
     * @return AssetOwnerType enum
     */
    public SchemaTypeProperties getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up the owner type for this asset.
     *
     * @param schemaType AssetOwnerType enum
     */
    public void setSchemaType(SchemaTypeProperties schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * Return the names of the zones that this asset is a member of.
     *
     * @return list of zone names
     */
    public List<SchemaAttributeProperties> getSchemaAttributes()
    {
        if (schemaAttributes == null)
        {
            return null;
        }
        else if (schemaAttributes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(schemaAttributes);
        }
    }


    /**
     * Set up the names of the zones that this asset is a member of.
     *
     * @param schemaAttributes list of zone names
     */
    public void setSchemaAttributes(List<SchemaAttributeProperties> schemaAttributes)
    {
        this.schemaAttributes = schemaAttributes;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "CombinedSchemaRequestBody{" +
                "schemaType=" + schemaType +
                ", schemaAttributes=" + schemaAttributes +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
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
        CombinedSchemaRequestBody that = (CombinedSchemaRequestBody) objectToCompare;
        return Objects.equals(schemaType, that.schemaType) &&
                Objects.equals(schemaAttributes, that.schemaAttributes);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(schemaType, schemaAttributes);
    }
}
