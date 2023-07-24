/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasRelationshipDef describes a relationship between two entities.
 * <p>
 * As with other typeDefs the AtlasRelationshipDef has a name. Once created the RelationshipDef has a guid.
 * The name and the guid are the 2 ways that the RelationshipDef is identified.
 * <p>
 * RelationshipDefs have 2 ends, each of which specify cardinality, an EntityDef type name and name and optionally
 * whether the end is a container.
 * <p>
 * RelationshipDefs can have AttributeDefs - though only primitive types are allowed. <br>
 * RelationshipDefs have an AtlasRelationshipCategory specifying the UML type of relationship required <br>
 * RelationshipDefs also have an AtlasPropagateTag - indicating which way tags could flow over the relationships.
 * <p>
 * The way EntityDefs and RelationshipDefs are intended to be used is that EntityDefs will define AttributeDefs these AttributeDefs
 * will not specify an EntityDef type name as their types.
 * <p>
 * RelationshipDefs introduce new attributes to the entity instances. For example
 * <p>
 * EntityDef A might have attributes attr1,attr2,attr3 <br>
 * EntityDef B might have attributes attr4,attr5,attr6 <br>
 * RelationshipDef AtoB might define 2 ends <br>
 *
 * <pre>
 *   end1:  type A, name attr7
 *   end2:  type B, name attr8  </pre>
 *
 * <p>
 * When an instance of EntityDef A is created, it will have attributes attr1,attr2,attr3,attr7 <br>
 * When an instance of EntityDef B is created, it will have attributes attr4,attr5,attr6,attr8
 * <p>
 * In this way relationshipDefs can be authored separately from entityDefs and can inject relationship attributes into
 * the entity instances.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasRelationshipDef extends AtlasTypeDefBase
{
    private AtlasRelationshipCategory relationshipCategory = null;
    private String                    relationshipLabel    = null;
    private AtlasPropagateTags        propagateTags        = null;
    private AtlasRelationshipEndDef   endDef1              = null;
    private AtlasRelationshipEndDef   endDef2              = null;


    public AtlasRelationshipDef()
    {
    }


    public AtlasRelationshipCategory getRelationshipCategory()
    {
        return relationshipCategory;
    }


    public void setRelationshipCategory(
            AtlasRelationshipCategory relationshipCategory)
    {
        this.relationshipCategory = relationshipCategory;
    }


    public String getRelationshipLabel()
    {
        return relationshipLabel;
    }


    public void setRelationshipLabel(String relationshipLabel)
    {
        this.relationshipLabel = relationshipLabel;
    }


    public AtlasPropagateTags getPropagateTags()
    {
        return propagateTags;
    }


    public void setPropagateTags(AtlasPropagateTags propagateTags)
    {
        this.propagateTags = propagateTags;
    }


    public AtlasRelationshipEndDef getEndDef1()
    {
        return endDef1;
    }


    public void setEndDef1(AtlasRelationshipEndDef endDef1)
    {
        this.endDef1 = endDef1;
    }


    public AtlasRelationshipEndDef getEndDef2()
    {
        return endDef2;
    }


    public void setEndDef2(AtlasRelationshipEndDef endDef2)
    {
        this.endDef2 = endDef2;
    }


    @Override
    public String toString()
    {
        return "AtlasRelationshipDef{" +
                       "relationshipCategory=" + relationshipCategory +
                       ", relationshipLabel='" + relationshipLabel + '\'' +
                       ", propagateTags=" + propagateTags +
                       ", endDef1=" + endDef1 +
                       ", endDef2=" + endDef2 +
                       ", category=" + getCategory() +
                       ", guid='" + getGuid() + '\'' +
                       ", createdBy='" + getCreatedBy() + '\'' +
                       ", updateBy='" + getUpdateBy() + '\'' +
                       ", createTime=" + getCreateTime() +
                       ", updateTime=" + getUpdateTime() +
                       ", version=" + getVersion() +
                       ", name='" + getName() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", typeVersion='" + getTypeVersion() + '\'' +
                       ", serviceType='" + getServiceType() + '\'' +
                       '}';
    }
}
