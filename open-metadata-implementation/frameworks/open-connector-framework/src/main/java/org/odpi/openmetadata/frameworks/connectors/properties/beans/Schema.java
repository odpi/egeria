/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Schema object provides information about how the asset structures the data it supports.  Schemas are typically
 * described as nested structures of linked schema elements.  Schemas can also be reused in other schemas.
 *
 * The schema object can be used to represent a Struct, Array, Set or Map.
 * <ul>
 *     <li>
 *         A Struct has an ordered list of attributes - the position of an attribute is set up as one of its properties.
 *     </li>
 *     <li>
 *         An Array has one schema attribute and a maximum size plus element count.
 *     </li>
 *     <li>
 *         A Set also has one schema attribute and a maximum size plus element count.
 *     </li>
 *     <li>
 *         A Map is a Set of MapSchemaElements
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Schema extends SchemaElement
{
    /*
     * Properties specific to a Schema
     */
    protected SchemaType       schemaType       = null;
    protected int              maximumElements  = 0;


    /**
     * Default constructor
     */
    public Schema()
    {
        super();
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateSchema template object to copy.
     */
    public Schema(Schema templateSchema)
    {
        super(templateSchema);

        if (templateSchema != null)
        {
            schemaType  = templateSchema.getSchemaType();
            maximumElements = templateSchema.getMaximumElements();
        }
    }


    /**
     * Return the type of the schema.
     *
     * @return SchemaType enum
     */
    public SchemaType getSchemaType() { return schemaType; }


    /**
     * Set up the type of schema.
     *
     * @param schemaType SchemaType enum
     */
    public void setSchemaType(SchemaType schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * Return the maximum elements that can be stored in this schema.  This is set up by the caller.
     * Zero means not bounded.  For a STRUCT the max elements are the number of elements in
     * the structure and this value is ignored.
     *
     * @return int maximum number of elements
     */
    public int getMaximumElements()
    {
        if (schemaType == SchemaType.STRUCT)
        {
            return 0;
        }
        else
        {
            return maximumElements;
        }
    }


    /**
     * Set up the maximum elements that can be stored in this schema.  This is set up by the caller.
     * Zero means not bounded.  For a STRUCT the max elements are the number of elements in
     * the structure and this value is ignored.
     *
     * @param maximumElements int maximum number of elements
     */
    public void setMaximumElements(int maximumElements)
    {
        this.maximumElements = maximumElements;
    }

    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return a copy of this schema as a SchemaElement
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new Schema(this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Schema{" +
                "schemaType=" + schemaType +
                ", maximumElements=" + maximumElements +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", type=" + type +
                ", guid='" + guid + '\'' +
                ", url='" + url + '\'' +
                ", classifications=" + classifications +
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
        if (!(objectToCompare instanceof Schema))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        Schema schema = (Schema) objectToCompare;
        return getMaximumElements() == schema.getMaximumElements() &&
                getSchemaType() == schema.getSchemaType();
    }
}