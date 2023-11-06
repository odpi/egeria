/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataPrimitiveDef supports the definition of a primitive type.  This information is managed in the
 * OpenMetadataPrimitiveDefCategory.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataPrimitiveDef extends OpenMetadataAttributeTypeDef
{
    private OpenMetadataPrimitiveDefCategory primitiveDefCategory = null;

    /**
     * Default constructor initializes the OpenMetadataPrimitiveDef based on the supplied category.
     */
    public OpenMetadataPrimitiveDef()
    {
        super(OpenMetadataAttributeTypeDefCategory.PRIMITIVE);
    }


    /**
     * Superclass constructor initializes the OpenMetadataPrimitiveDef based on the supplied category.
     *
     * @param primitiveDefCategory OpenMetadataPrimitiveDefCategory Enum
     */
    public OpenMetadataPrimitiveDef(OpenMetadataPrimitiveDefCategory primitiveDefCategory)
    {
        super(OpenMetadataAttributeTypeDefCategory.PRIMITIVE);

        this.primitiveDefCategory = primitiveDefCategory;
    }


    /**
     * Copy/clone constructor creates a copy of the supplied template.
     *
     * @param template OpenMetadataPrimitiveDef to copy
     */
    public OpenMetadataPrimitiveDef(OpenMetadataPrimitiveDef template)
    {
        super(template);

        if (template != null)
        {
            this.primitiveDefCategory = template.getPrimitiveDefCategory();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataAttributeTypeDef
     */
    public OpenMetadataAttributeTypeDef cloneFromSubclass()
    {
        return new OpenMetadataPrimitiveDef(this);
    }


    /**
     * Return the type category for this primitive type.
     *
     * @return OpenMetadataPrimitiveDefCategory Enum
     */
    public OpenMetadataPrimitiveDefCategory getPrimitiveDefCategory() { return primitiveDefCategory; }


    /**
     * Set up the type category for this primitive type.
     *
     * @param primitiveDefCategory OpenMetadataPrimitiveDefCategory Enum
     */
    public void setPrimitiveDefCategory(OpenMetadataPrimitiveDefCategory primitiveDefCategory)
    {
        this.primitiveDefCategory = primitiveDefCategory;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataPrimitiveDef{" +
                "name='" + name + '\'' +
                ", primitiveDefCategory=" + primitiveDefCategory +
                ", category=" + category +
                ", guid='" + guid + '\'' +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataPrimitiveDef that = (OpenMetadataPrimitiveDef) objectToCompare;
        return primitiveDefCategory == that.primitiveDefCategory;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), primitiveDefCategory);
    }
}
