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
 * OpenMetadataRelationshipDef describes the type of a relationship.  A relationships links two entities together.
 * The OpenMetadataRelationshipDef defines the types of those entities in the RelationshipEndDefs.  It also
 * defines if this relationship allows classifications to propagate through it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataRelationshipDef extends OpenMetadataTypeDef
{
    private OpenMetadataClassificationPropagationRule propagationRule = OpenMetadataClassificationPropagationRule.NONE;
    private OpenMetadataRelationshipEndDef            endDef1         = null;
    private OpenMetadataRelationshipEndDef            endDef2         = null;
    private boolean                                   multiLink       = false;


    /**
     * Minimal constructor builds an empty OpenMetadataRelationshipDef
     */
    public OpenMetadataRelationshipDef()
    {
        super(OpenMetadataTypeDefCategory.RELATIONSHIP_DEF);
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     *
     * @param category    category of this OpenMetadataTypeDef
     * @param guid        unique id for the OpenMetadataTypeDef
     * @param name        unique name for the OpenMetadataTypeDef
     * @param version     active version number for the OpenMetadataTypeDef
     * @param versionName name for the active version of the OpenMetadataTypeDef
     */
    public OpenMetadataRelationshipDef(OpenMetadataTypeDefCategory category,
                                       String          guid,
                                       String          name,
                                       long            version,
                                       String          versionName)
    {
        super(category, guid, name, version, versionName);
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
            this.propagationRule = templateTypeDef.getPropagationRule();
            this.endDef1 = templateTypeDef.getEndDef1();
            this.endDef2 = templateTypeDef.getEndDef2();
            this.multiLink = templateTypeDef.getMultiLink();
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
     * Return whether multiple relationships of this type are allowed between the same two entities.
     *
     * @return boolean flag
     */
    public boolean getMultiLink()
    {
        return multiLink;
    }


    /**
     * Set up whether multiple relationships of this type are allowed between the same two entities.
     *
     * @param multiLink boolean flag
     */
    public void setMultiLink(boolean multiLink)
    {
        this.multiLink = multiLink;
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
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", endDef1=" + endDef1 +
                ", endDef2=" + endDef2 +
                ", multiLink=" + multiLink +
                ", superType=" + getSuperType() +
                ", descriptionGUID='" + getDescriptionGUID() + '\'' +
                ", descriptionWiki='" + getDescriptionWiki() + '\'' +
                ", origin='" + getOrigin() + '\'' +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", options=" + getOptions() +
                ", externalStandardTypeMappings=" + getExternalStandardMappings() +
                ", validInstanceStatusList=" + getValidElementStatusList() +
                ", initialStatus=" + getInitialStatus() +
                ", propertiesDefinition=" + getAttributeDefinitions() +
                ", category=" + getCategory() +
                ", propagationRule=" + propagationRule +
                ", version=" + getVersion() +
                ", versionName='" + getVersionName() + '\'' +
                ", GUID='" + getGUID() + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataRelationshipDef that = (OpenMetadataRelationshipDef) objectToCompare;
        return multiLink == that.multiLink &&
                       propagationRule == that.propagationRule &&
                       Objects.equals(endDef1, that.endDef1) &&
                       Objects.equals(endDef2, that.endDef2);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), propagationRule, endDef1, endDef2, multiLink);
    }
}
