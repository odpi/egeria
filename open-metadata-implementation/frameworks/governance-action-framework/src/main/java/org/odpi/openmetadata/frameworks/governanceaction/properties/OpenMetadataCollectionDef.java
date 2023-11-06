/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataCollectionDef supports the definition of a collection type.  This information about the generic
 * collection type is managed in the OpenMetadataCollectionDefCategory.  It is instantiated with specific primitive
 * types when it is linked to a specific OpenMetadataTypeDefAttribute.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataCollectionDef extends OpenMetadataAttributeTypeDef
{
    private static final long    serialVersionUID = 1L;

    private OpenMetadataCollectionDefCategory collectionDefCategory = null;
    private int                               argumentCount         = 0;
    private List<OpenMetadataPrimitiveDefCategory> argumentTypes = null;


    /**
     * Default constructor for Jackson (JSON parsing)
     */
    public OpenMetadataCollectionDef()
    {
        super(OpenMetadataAttributeTypeDefCategory.COLLECTION);
    }


    /**
     * Typical constructor initializes the OpenMetadataCollectionDef based on the supplied category.
     *
     * @param collectionDefCategory OpenMetadataCollectionDefCategory Enum
     */
    public OpenMetadataCollectionDef(OpenMetadataCollectionDefCategory collectionDefCategory)
    {
        super(OpenMetadataAttributeTypeDefCategory.COLLECTION);

        this.collectionDefCategory = collectionDefCategory;
        this.argumentCount = collectionDefCategory.getArgumentCount();
        this.argumentTypes = new ArrayList<>();

        /*
         * Set up the type of the elements stored in the collection as "unknown".  This is like an initialized Java generic.
         */
        for (int i=0; i<argumentCount; i++)
        {
            argumentTypes.add(OpenMetadataPrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
        }
    }


    /**
     * Copy/clone constructor creates a copy of the supplied template.
     *
     * @param template OpenMetadataCollectionDef to copy
     */
    public OpenMetadataCollectionDef(OpenMetadataCollectionDef template)
    {
        super(template);

        if (template != null)
        {
            this.collectionDefCategory = template.getCollectionDefCategory();
            this.argumentCount = template.getArgumentCount();
            this.setArgumentTypes(template.getArgumentTypes());
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataAttributeTypeDef
     */
    public OpenMetadataAttributeTypeDef cloneFromSubclass()
    {
        return new OpenMetadataCollectionDef(this);
    }


    /**
     * Return the type category for this collection type.
     *
     * @return OpenMetadataCollectionDefCategory Enum
     */
    public OpenMetadataCollectionDefCategory getCollectionDefCategory() { return collectionDefCategory; }


    /**
     * Set up the elements category.
     *
     * @param collectionDefCategory value to use
     */
    public void setCollectionDefCategory(OpenMetadataCollectionDefCategory collectionDefCategory)
    {
        this.collectionDefCategory = collectionDefCategory;

        this.argumentCount = collectionDefCategory.getArgumentCount();
        this.argumentTypes = new ArrayList<>();

        /*
         * Set up the type of the elements stored in the collection as "unknown".  This is like an initialized Java generic.
         */
        for (int i=0; i<argumentCount; i++)
        {
            argumentTypes.add(OpenMetadataPrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
        }
    }

    /**
     * Return the number of arguments needed to set up the collection type name.
     *
     * @return int count
     */
    public int getArgumentCount()
    {
        return argumentCount;
    }


    /**
     * Set upi the number of arguments needed to set upi the collection type name.
     *
     * @param argumentCount int count
     */
    public void setArgumentCount(int argumentCount)
    {
        // do nothing
    }

    /**
     * Return the list of argument types set up for this collection.
     *
     * @return list of argument type
     */
    public List<OpenMetadataPrimitiveDefCategory> getArgumentTypes()
    {
        if (argumentTypes == null)
        {
            return null;
        }
        else if (argumentTypes.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(argumentTypes);
        }
    }


    /**
     * Set up the list of argument types.
     *
     * @param argumentTypes list of argument types
     */
    public void setArgumentTypes(List<OpenMetadataPrimitiveDefCategory> argumentTypes)
    {
        this.argumentTypes = argumentTypes;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataCollectionDef{" +
                "name='" + name + '\'' +
                ", collectionDefCategory=" + collectionDefCategory +
                ", argumentCount=" + argumentCount +
                ", argumentTypes=" + argumentTypes +
                ", category=" + category +
                ", guid='" + guid + '\'' +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataCollectionDef that = (OpenMetadataCollectionDef) objectToCompare;
        return argumentCount == that.argumentCount &&
                       collectionDefCategory == that.collectionDefCategory &&
                       Objects.equals(argumentTypes, that.argumentTypes);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), collectionDefCategory, argumentCount, argumentTypes);
    }
}
