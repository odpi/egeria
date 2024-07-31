/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.schema;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LiteralSchemaTypeProperties carries the specialized parameters for creating or updating literal schema types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class LiteralSchemaTypeProperties extends SchemaTypeProperties
{
    private String dataType   = null;
    private String fixedValue = null;

    /**
     * Default constructor
     */
    public LiteralSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param template template object to copy.
     */
    public LiteralSchemaTypeProperties(LiteralSchemaTypeProperties template)
    {
        super(template);

        if (template != null)
        {
            dataType   = template.getDataType();
            fixedValue = template.getFixedValue();
        }
    }


    /**
     * Return the data type for this element.  Null means unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return the fixed value for the element.  Null means fixed value is null.
     *
     * @return String containing fixed value
     */
    public String getFixedValue() { return fixedValue; }


    /**
     * Set up the fixed value for the element.  Null means fixed value is null.
     *
     * @param fixedValue String containing fixed value
     */
    public void setFixedValue(String fixedValue)
    {
        this.fixedValue = fixedValue;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "LiteralSchemaTypeProperties{" +
                "dataType='" + dataType + '\'' +
                ", fixedValue='" + fixedValue + '\'' +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", namespace='" + getNamespace() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", extendedProperties=" + getExtendedProperties() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        LiteralSchemaTypeProperties that = (LiteralSchemaTypeProperties) objectToCompare;
        return Objects.equals(dataType, that.dataType) &&
                       Objects.equals(fixedValue, that.fixedValue);
    }



    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataType, fixedValue);
    }
}
