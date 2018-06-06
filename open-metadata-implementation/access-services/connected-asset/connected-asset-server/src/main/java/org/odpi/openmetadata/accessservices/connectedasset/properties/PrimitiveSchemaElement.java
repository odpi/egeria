/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.connectedasset.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PrimitiveSchemaElement describes a schema element that has a primitive type.  This class stores which
 * type of primitive type it is an a default value if supplied.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PrimitiveSchemaElement extends SchemaElement
{
    private  String     dataType = null;
    private  String     defaultValue = null;

    /**
     * Defauly constructor
     */
    public PrimitiveSchemaElement()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param templateSchemaElement - schema element to copy
     */
    public PrimitiveSchemaElement(PrimitiveSchemaElement templateSchemaElement)
    {
        super(templateSchemaElement);

        if (templateSchemaElement != null)
        {
            dataType = templateSchemaElement.getDataType();
            defaultValue = templateSchemaElement.getDefaultValue();
        }
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String DataType
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the name of the data type for this element.  Null means unknown data type.
     *
     * @param dataType - String DataType
     */
    public void setDataType(String dataType) { this.dataType = dataType; }


    /**
     * Return the default value for the element.  Null means no default value set up.
     *
     * @return String containing default value
     */
    public String getDefaultValue() { return defaultValue; }


    /**
     * Set up the default value for the element.  Null means no default value.
     *
     * @param defaultValue - String containing default value
     */
    public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @return PrimitiveSchemaElement object
     */
    @Override
    public SchemaElement cloneSchemaElement()
    {
        return new PrimitiveSchemaElement(this);
    }
}