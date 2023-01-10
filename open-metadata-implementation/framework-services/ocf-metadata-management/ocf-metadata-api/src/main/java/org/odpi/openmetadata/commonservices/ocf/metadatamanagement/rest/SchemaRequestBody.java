/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SchemaRequestBody carries the parameters for created a new schema to attach to
 * an asset.  A limited number of SchemaAttributes can accompany the SchemaType.
 * If more SchemaAttributes are needed then use addSchemaAttributesToSchema() method.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SchemaRequestBody extends OCFOMASAPIRequestBody
{
    private static final long    serialVersionUID = 1L;

    protected SchemaType            schemaType       = null;
    protected List<SchemaAttribute> schemaAttributes = null;


    /**
     * Default constructor
     */
    public SchemaRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SchemaRequestBody(SchemaRequestBody template)
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
     * @return OwnerType enum
     */
    public SchemaType getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up the owner type for this asset.
     *
     * @param schemaType OwnerType enum
     */
    public void setSchemaType(SchemaType schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * Return the names of the zones that this asset is a member of.
     *
     * @return list of zone names
     */
    public List<SchemaAttribute> getSchemaAttributes()
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
    public void setSchemaAttributes(List<SchemaAttribute> schemaAttributes)
    {
        this.schemaAttributes = schemaAttributes;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */



    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */




    /**
     * Return hash code based on properties.
     *
     * @return int
     */

}