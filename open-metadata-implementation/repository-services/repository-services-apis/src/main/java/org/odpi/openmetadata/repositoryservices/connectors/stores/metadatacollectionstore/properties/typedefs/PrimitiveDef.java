/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PrimitiveDef supports the definition of a primitive type.  This information is managed in the
 * PrimitiveDefCategory.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class PrimitiveDef extends AttributeTypeDef
{
    private  PrimitiveDefCategory   primitiveDefCategory = null;

    /**
     * Default constructor initializes the PrimitiveDef based on the supplied category.
     */
    public PrimitiveDef()
    {
        super(AttributeTypeDefCategory.PRIMITIVE);
    }


    /**
     * Superclass constructor initializes the PrimitiveDef based on the supplied category.
     *
     * @param primitiveDefCategory PrimitiveDefCategory Enum
     */
    public PrimitiveDef(PrimitiveDefCategory  primitiveDefCategory)
    {
        super(AttributeTypeDefCategory.PRIMITIVE);

        this.primitiveDefCategory = primitiveDefCategory;
    }


    /**
     * Copy/clone constructor creates a copy of the supplied template.
     *
     * @param template PrimitiveDef to copy
     */
    public PrimitiveDef(PrimitiveDef template)
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
     * @return subclass of AttributeTypeDef
     */
    public AttributeTypeDef cloneFromSubclass()
    {
        return new PrimitiveDef(this);
    }


    /**
     * Return the type category for this primitive type.
     *
     * @return PrimitiveDefCategory Enum
     */
    public PrimitiveDefCategory getPrimitiveDefCategory() { return primitiveDefCategory; }


    /**
     * Set up the type category for this primitive type.
     *
     * @param primitiveDefCategory PrimitiveDefCategory Enum
     */
    public void setPrimitiveDefCategory(PrimitiveDefCategory primitiveDefCategory)
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
        return "PrimitiveDef{" +
                "primitiveDefCategory=" + primitiveDefCategory +
                ", category=" + category +
                ", guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
                '}';
    }


    /**
     * Verify that supplied object has the same properties.
     *
     * @param o object to test
     * @return result
     */
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof PrimitiveDef))
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        PrimitiveDef that = (PrimitiveDef) o;
        return getPrimitiveDefCategory() == that.getPrimitiveDefCategory();
    }
}
