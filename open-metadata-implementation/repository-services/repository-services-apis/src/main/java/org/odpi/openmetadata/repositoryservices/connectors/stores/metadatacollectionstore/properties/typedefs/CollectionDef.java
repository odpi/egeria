/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionDef supports the definition of a collection type.  This information about the generic
 * collection type is managed in the CollectionDefCategory.  It is instantiated with specific primitive
 * types when it is linked to a specific TypeDefAttribute.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionDef extends AttributeTypeDef
{
    @Serial
    private static final long serialVersionUID = 1L;

    private CollectionDefCategory      collectionDefCategory = null;
    private int                        argumentCount         = 0;
    private List<PrimitiveDefCategory> argumentTypes         = null;


    /**
     * Default constructor for Jackson (JSON parsing)
     */
    public CollectionDef()
    {
        super(AttributeTypeDefCategory.COLLECTION);
    }


    /**
     * Typical constructor initializes the CollectionDef based on the supplied category.
     *
     * @param collectionDefCategory CollectionDefCategory Enum
     */
    public CollectionDef(CollectionDefCategory  collectionDefCategory)
    {
        super(AttributeTypeDefCategory.COLLECTION);

        this.collectionDefCategory = collectionDefCategory;
        this.argumentCount = collectionDefCategory.getArgumentCount();
        this.argumentTypes = new ArrayList<>();

        /*
         * Set up the type of the elements stored in the collection as "unknown".  This is like an initialized Java generic.
         */
        for (int i=0; i<argumentCount; i++)
        {
            argumentTypes.add(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
        }
    }


    /**
     * Copy/clone constructor creates a copy of the supplied template.
     *
     * @param template CollectionDef to copy
     */
    public CollectionDef(CollectionDef template)
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
     * @return subclass of AttributeTypeDef
     */
    public AttributeTypeDef cloneFromSubclass()
    {
        return new CollectionDef(this);
    }


    /**
     * Return the type category for this collection type.
     *
     * @return CollectionDefCategory Enum
     */
    public CollectionDefCategory getCollectionDefCategory() { return collectionDefCategory; }


    /**
     * Set up the elements category.
     *
     * @param collectionDefCategory value to use
     */
    public void setCollectionDefCategory(CollectionDefCategory collectionDefCategory)
    {
        this.collectionDefCategory = collectionDefCategory;

        this.argumentCount = collectionDefCategory.getArgumentCount();
        this.argumentTypes = new ArrayList<>();

        /*
         * Set up the type of the elements stored in the collection as "unknown".  This is like an initialized Java generic.
         */
        for (int i=0; i<argumentCount; i++)
        {
            argumentTypes.add(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_UNKNOWN);
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
    public List<PrimitiveDefCategory> getArgumentTypes()
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
    public void setArgumentTypes(List<PrimitiveDefCategory> argumentTypes)
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
        return "CollectionDef{" +
                "name='" + name + '\'' +
                ", collectionDefCategory=" + collectionDefCategory +
                ", argumentCount=" + argumentCount +
                ", argumentTypes=" + argumentTypes +
                ", category=" + category +
                ", guid='" + guid + '\'' +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
                ", descriptionWiki='" + descriptionWiki + '\'' +
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
        CollectionDef that = (CollectionDef) objectToCompare;
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
