/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataRelationshipDef describes a type of relationship.  A relationship links two entities together.
 * The OpenMetadataRelationshipDef defines the types of those entities in the RelationshipEndDefs.  It also
 * defines if this relationship allows classifications to propagate through it and how many instances of this
 * relationship type are allowed between two elements in a single direction.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRelationshipDef extends OpenMetadataTypeDef
{
    private OpenMetadataClassificationPropagationRule propagationRule      = OpenMetadataClassificationPropagationRule.NONE;
    private OpenMetadataRelationshipEndDef            endDef1              = null;
    private OpenMetadataRelationshipEndDef            endDef2              = null;
    private OpenMetadataRelationshipCategory          relationshipCategory = null;


    /**
     * Minimal constructor builds an empty OpenMetadataRelationshipDef
     */
    public OpenMetadataRelationshipDef()
    {
        super(OpenMetadataTypeDefCategory.RELATIONSHIP_DEF);
    }


    /**
     * Copy/clone constructor creates a copy of the supplied template.
     *
     * @param templateTypeDef template to copy
     */
    public OpenMetadataRelationshipDef(OpenMetadataRelationshipDef templateTypeDef)
    {
        super(templateTypeDef);

        if (templateTypeDef != null)
        {
            this.propagationRule      = templateTypeDef.getPropagationRule();
            this.endDef1              = templateTypeDef.getEndDef1();
            this.endDef2              = templateTypeDef.getEndDef2();
            this.relationshipCategory = templateTypeDef.getRelationshipCategory();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataTypeDef
     */
    public OpenMetadataTypeDef cloneFromSubclass()
    {
        return new OpenMetadataRelationshipDef(this);
    }


    /**
     * Return the rule that determines if classifications are propagated across this relationship.
     *
     * @return OpenMetadataClassificationPropagationRule Enum
     */
    public OpenMetadataClassificationPropagationRule getPropagationRule() { return propagationRule; }


    /**
     * Set up the rule that determines if classifications are propagated across this relationship.
     *
     * @param propagationRule OpenMetadataClassificationPropagationRule Enum
     */
    public void setPropagationRule(OpenMetadataClassificationPropagationRule propagationRule)
    {
        this.propagationRule = propagationRule;
    }


    /**
     * Return the details associated with the first end of the relationship.
     *
     * @return endDef1 OpenMetadataRelationshipEndDef
     */
    public OpenMetadataRelationshipEndDef getEndDef1()
    {
        if (endDef1 == null)
        {
            return null;
        }
        else
        {
            return new OpenMetadataRelationshipEndDef(endDef1);
        }
    }


    /**
     * Set up the details associated with the first end of the relationship.
     *
     * @param endDef1 OpenMetadataRelationshipEndDef
     */
    public void setEndDef1(OpenMetadataRelationshipEndDef endDef1) {this.endDef1 = endDef1; }


    /**
     * Return the details associated with the second end of the relationship.
     *
     * @return endDef2 OpenMetadataRelationshipEndDef
     */
    public OpenMetadataRelationshipEndDef getEndDef2()
    {
        if (endDef2 == null)
        {
            return null;
        }
        else
        {
            return new OpenMetadataRelationshipEndDef(endDef2);
        }
    }


    /**
     * Set up the details associated with the second end of the relationship.
     *
     * @param endDef2 OpenMetadataRelationshipEndDef
     */
    public void setEndDef2(OpenMetadataRelationshipEndDef endDef2) {this.endDef2 = endDef2; }


    /**
     * Return the relationship category.
     *
     * @return enum
     */
    public OpenMetadataRelationshipCategory getRelationshipCategory()
    {
        return relationshipCategory;
    }


    /**
     * Set up the relationship category.
     *
     * @param relationshipCategory enum
     */
    public void setRelationshipCategory(OpenMetadataRelationshipCategory relationshipCategory)
    {
        this.relationshipCategory = relationshipCategory;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataRelationshipDef{" +
                "propagationRule=" + propagationRule +
                ", endDef1=" + endDef1 +
                ", endDef2=" + endDef2 +
                ", relationshipCategory=" + relationshipCategory +
                "} " + super.toString();
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataRelationshipDef that = (OpenMetadataRelationshipDef) objectToCompare;
        return propagationRule == that.propagationRule &&
               Objects.equals(endDef1, that.endDef1) &&
               Objects.equals(endDef2, that.endDef2) &&
               relationshipCategory == that.relationshipCategory;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), propagationRule, endDef1, endDef2, relationshipCategory);
    }
}
