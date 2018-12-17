/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
        if (argumentTypes == null)
        {
            this.argumentTypes = null;
        }
        else
        {
            this.argumentTypes = new ArrayList<>(argumentTypes);
        }
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
                "collectionDefCategory=" + collectionDefCategory +
                ", argumentCount=" + argumentCount +
                ", argumentTypes=" + argumentTypes +
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
        if (!(o instanceof CollectionDef))
        {
            return false;
        }
        if (!super.equals(o))
        {
            return false;
        }
        CollectionDef that = (CollectionDef) o;
        return getArgumentCount() == that.getArgumentCount() &&
                getCollectionDefCategory() == that.getCollectionDefCategory() &&
                Objects.equals(getArgumentTypes(), that.getArgumentTypes());
    }
}
