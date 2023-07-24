/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasClassificationAssociateRequest seems to be used to do a bulk update of entities with a classification.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasClassificationAssociateRequest
{
    private AtlasClassification       classification;
    private List<String>              entityGuids;
    private List<Map<String, Object>> entitiesUniqueAttributes;
    private String                    entityTypeName;


    public AtlasClassificationAssociateRequest()
    {
    }


    public AtlasClassification getClassification()
    {
        return classification;
    }


    public void setClassification(AtlasClassification classification)
    {
        this.classification = classification;
    }


    public List<String> getEntityGuids()
    {
        return entityGuids;
    }


    public void setEntityGuids(List<String> entityGuids)
    {
        this.entityGuids = entityGuids;
    }


    public List<Map<String, Object>> getEntitiesUniqueAttributes()
    {
        return entitiesUniqueAttributes;
    }


    public void setEntitiesUniqueAttributes(List<Map<String, Object>> entitiesUniqueAttributes)
    {
        this.entitiesUniqueAttributes = entitiesUniqueAttributes;
    }


    public String getEntityTypeName()
    {
        return entityTypeName;
    }


    public void setEntityTypeName(String entityTypeName)
    {
        this.entityTypeName = entityTypeName;
    }


    @Override
    public String toString()
    {
        return "AtlasClassificationAssociateRequest{" +
                       "classification=" + classification +
                       ", entityGuids=" + entityGuids +
                       ", entitiesUniqueAttributes=" + entitiesUniqueAttributes +
                       ", entityTypeName='" + entityTypeName + '\'' +
                       '}';
    }
}
