/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasTypesDef describes the gallery of types sent and returned to Apache Atlas.  It is used to create now types, retrieve them and delete them.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasTypesDef
{
    private List<AtlasEnumDef>             enumDefs             = null;
    private List<AtlasStructDef>           structDefs           = null;
    private List<AtlasClassificationDef>   classificationDefs   = null;
    private List<AtlasEntityDef>           entityDefs           = null;
    private List<AtlasRelationshipDef>     relationshipDefs     = null;
    private List<AtlasBusinessMetadataDef> businessMetadataDefs = null;


    public AtlasTypesDef()
    {
    }


    public List<AtlasEnumDef> getEnumDefs()
    {
        return enumDefs;
    }


    public void setEnumDefs(List<AtlasEnumDef> enumDefs)
    {
        this.enumDefs = enumDefs;
    }


    public List<AtlasStructDef> getStructDefs()
    {
        return structDefs;
    }


    public void setStructDefs(List<AtlasStructDef> structDefs)
    {
        this.structDefs = structDefs;
    }


    public List<AtlasClassificationDef> getClassificationDefs()
    {
        return classificationDefs;
    }


    public void setClassificationDefs(
            List<AtlasClassificationDef> classificationDefs)
    {
        this.classificationDefs = classificationDefs;
    }


    public List<AtlasEntityDef> getEntityDefs()
    {
        return entityDefs;
    }


    public void setEntityDefs(List<AtlasEntityDef> entityDefs)
    {
        this.entityDefs = entityDefs;
    }


    public List<AtlasRelationshipDef> getRelationshipDefs()
    {
        return relationshipDefs;
    }


    public void setRelationshipDefs(List<AtlasRelationshipDef> relationshipDefs)
    {
        this.relationshipDefs = relationshipDefs;
    }


    public List<AtlasBusinessMetadataDef> getBusinessMetadataDefs()
    {
        return businessMetadataDefs;
    }


    public void setBusinessMetadataDefs(List<AtlasBusinessMetadataDef> businessMetadataDefs)
    {
        this.businessMetadataDefs = businessMetadataDefs;
    }


    @Override
    public String toString()
    {
        return "AtlasTypesDef{" +
                       "enumDefs=" + enumDefs +
                       ", structDefs=" + structDefs +
                       ", classificationDefs=" + classificationDefs +
                       ", entityDefs=" + entityDefs +
                       ", relationshipDefs=" + relationshipDefs +
                       ", businessMetadataDefs=" + businessMetadataDefs +
                       '}';
    }
}
