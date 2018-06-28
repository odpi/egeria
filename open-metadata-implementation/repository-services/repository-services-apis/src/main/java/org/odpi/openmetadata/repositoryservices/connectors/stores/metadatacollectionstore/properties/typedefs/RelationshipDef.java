/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RelationshipDef describes the type of a relationship.  A relationships links two entities together.
 * The RelationshipDef defines the types of those entities in the RelationshipEndDefs.  It also
 * defines if this relationship allows classifications to propagate through it.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RelationshipDef extends TypeDef
{
    private ClassificationPropagationRule propagationRule          = ClassificationPropagationRule.NONE;
    private RelationshipEndDef            endDef1                  = null;
    private RelationshipEndDef            endDef2                  = null;


    /**
     * Minimal constructor builds an empty RelationshipDef
     */
    public RelationshipDef()
    {
        super(TypeDefCategory.RELATIONSHIP_DEF);
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     *
     * @param category    category of this TypeDef
     * @param guid        unique id for the TypeDef
     * @param name        unique name for the TypeDef
     * @param version     active version number for the TypeDef
     * @param versionName name for the active version of the TypeDef
     */
    public RelationshipDef(TypeDefCategory category,
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
    public RelationshipDef(RelationshipDef templateTypeDef)
    {
        super(templateTypeDef);

        if (templateTypeDef != null)
        {
            this.propagationRule = templateTypeDef.getPropagationRule();
            this.endDef1 = templateTypeDef.getEndDef1();
            this.endDef2 = templateTypeDef.getEndDef2();
        }
    }


    /**
     * Return the rule that determines if classifications are propagated across this relationship.
     *
     * @return ClassificationPropagationRule Enum
     */
    public ClassificationPropagationRule getPropagationRule() { return propagationRule; }


    /**
     * Set up the rule that determines if classifications are propagated across this relationship.
     *
     * @param propagationRule ClassificationPropagationRule Enum
     */
    public void setPropagationRule(ClassificationPropagationRule propagationRule)
    {
        this.propagationRule = propagationRule;
    }


    /**
     * Return the details associated with the first end of the relationship.
     *
     * @return endDef1 RelationshipEndDef
     */
    public RelationshipEndDef getEndDef1()
    {
        return endDef1;
    }


    /**
     * Set up the details associated with the first end of the relationship.
     *
     * @param endDef1 RelationshipEndDef
     */
    public void setEndDef1(RelationshipEndDef endDef1) { this.endDef1 = endDef1; }


    /**
     * Return the details associated with the second end of the relationship.
     *
     * @return endDef2 RelationshipEndDef
     */
    public RelationshipEndDef getEndDef2()
    {
        return endDef2;
    }


    /**
     * Set up the details associated with the second end of the relationship.
     *
     * @param endDef2 RelationshipEndDef
     */
    public void setEndDef2(RelationshipEndDef endDef2) { this.endDef2 = endDef2; }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "RelationshipDef{" +
                ", propagationRule=" + propagationRule +
                ", endDef1=" + endDef1 +
                ", endDef2=" + endDef2 +
                ", superType=" + superType +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
                ", origin='" + origin + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", options=" + options +
                ", externalStandardMappings=" + externalStandardMappings +
                ", validInstanceStatusList=" + validInstanceStatusList +
                ", initialStatus=" + initialStatus +
                ", propertiesDefinition=" + propertiesDefinition +
                ", version=" + version +
                ", versionName='" + versionName + '\'' +
                ", category=" + category +
                ", guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
