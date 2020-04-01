/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PrimitiveSchemaTypeProperties carries the specialized parameters for creating or updating primitive schema types.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class PrimitiveSchemaTypeProperties extends SimpleSchemaTypeProperties
{
    private static final long     serialVersionUID = 1L;

    /**
     * Default constructor
     */
    public PrimitiveSchemaTypeProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public PrimitiveSchemaTypeProperties(PrimitiveSchemaTypeProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone operator.
     *
     * @param objectToFill schema type object
     * @return filled object
     */
    public PrimitiveSchemaType cloneProperties(PrimitiveSchemaType objectToFill)
    {
        PrimitiveSchemaType clone = objectToFill;

        if (clone == null)
        {
            clone = new PrimitiveSchemaType();
        }

        super.cloneProperties(clone);

        return clone;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PrimitiveSchemaTypeProperties{" +
                "dataType='" + getDataType() + '\'' +
                ", defaultValue='" + getDefaultValue() + '\'' +
                ", versionNumber='" + getVersionNumber() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", usage='" + getUsage() + '\'' +
                ", encodingStandard='" + getEncodingStandard() + '\'' +
                ", namespace='" + getNamespace() + '\'' +
                ", displayName='" + getDisplayName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", classifications=" + getClassifications() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }
}