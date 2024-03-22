/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasSearchResult describes the response structure for an Atlas search request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasSearchResult
{
    private AtlasQueryType                 queryType        = null;
    private AtlasSearchParameters          searchParameters = null;
    private String                         queryText        = null;
    private String                         type             = null;
    private String                         classification   = null;
    private List<AtlasEntityHeader>        entities         = null;
    private List<AtlasRelationshipHeader>  relations        = null;
    private AtlasAttributeSearchResult     attributes       = null;
    private List<AtlasFullTextResult>      fullTextResult   = null;
    private Map<String, AtlasEntityHeader> referredEntities = null;
    private long                           approximateCount = - 1;
    private String                         nextMarker       = null;


    public AtlasSearchResult()
    {
    }


    public AtlasQueryType getQueryType()
    {
        return queryType;
    }


    public void setQueryType(AtlasQueryType queryType)
    {
        this.queryType = queryType;
    }


    public AtlasSearchParameters getSearchParameters()
    {
        return searchParameters;
    }


    public void setSearchParameters(AtlasSearchParameters searchParameters)
    {
        this.searchParameters = searchParameters;
    }


    public String getQueryText()
    {
        return queryText;
    }


    public void setQueryText(String queryText)
    {
        this.queryText = queryText;
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getClassification()
    {
        return classification;
    }


    public void setClassification(String classification)
    {
        this.classification = classification;
    }


    public List<AtlasEntityHeader> getEntities()
    {
        return entities;
    }


    public void setEntities(List<AtlasEntityHeader> entities)
    {
        this.entities = entities;
    }


    public List<AtlasRelationshipHeader> getRelations()
    {
        return relations;
    }


    public void setRelations(List<AtlasRelationshipHeader> relations)
    {
        this.relations = relations;
    }


    public AtlasAttributeSearchResult getAttributes()
    {
        return attributes;
    }


    public void setAttributes(AtlasAttributeSearchResult attributes)
    {
        this.attributes = attributes;
    }


    public List<AtlasFullTextResult> getFullTextResult()
    {
        return fullTextResult;
    }


    public void setFullTextResult(
            List<AtlasFullTextResult> fullTextResult)
    {
        this.fullTextResult = fullTextResult;
    }


    public Map<String, AtlasEntityHeader> getReferredEntities()
    {
        return referredEntities;
    }


    public void setReferredEntities(
            Map<String, AtlasEntityHeader> referredEntities)
    {
        this.referredEntities = referredEntities;
    }


    public long getApproximateCount()
    {
        return approximateCount;
    }


    public void setApproximateCount(long approximateCount)
    {
        this.approximateCount = approximateCount;
    }


    public String getNextMarker()
    {
        return nextMarker;
    }


    public void setNextMarker(String nextMarker)
    {
        this.nextMarker = nextMarker;
    }


    @Override
    public String toString()
    {
        return "AtlasSearchResult{" +
                       "queryType=" + queryType +
                       ", searchParameters=" + searchParameters +
                       ", queryText='" + queryText + '\'' +
                       ", type='" + type + '\'' +
                       ", classification='" + classification + '\'' +
                       ", entities=" + entities +
                       ", relations=" + relations +
                       ", attributes=" + attributes +
                       ", fullTextResult=" + fullTextResult +
                       ", referredEntities=" + referredEntities +
                       ", approximateCount=" + approximateCount +
                       ", nextMarker='" + nextMarker + '\'' +
                       '}';
    }
}
