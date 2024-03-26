/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasSearchParameters describes a query request to Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasSearchParameters
{
    public static final String WILDCARD_CLASSIFICATIONS = "*";
    public static final String ALL_CLASSIFICATIONS      = "_CLASSIFIED";
    public static final String NO_CLASSIFICATIONS       = "_NOT_CLASSIFIED";
    public static final String ALL_ENTITY_TYPES         = "_ALL_ENTITY_TYPES";
    public static final String ALL_CLASSIFICATION_TYPES = "_ALL_CLASSIFICATION_TYPES";

    private String              query                           = null;
    private String              typeName                        = null;
    private String              classification                  = null;
    private String              relationshipName                = null;
    private String              termName                        = null;
    private String              sortBy                          = null;
    private boolean             excludeDeletedEntities          = true;
    private boolean             includeClassificationAttributes = false;
    private boolean             includeSubTypes                 = true;
    private boolean             includeSubClassifications       = true;
    private boolean             excludeHeaderAttributes         = false;
    private int                 limit                           = 0;
    private int                 offset                          = 0;
    private String              marker                          = null;
    private AtlasFilterCriteria entityFilters                   = null;
    private AtlasFilterCriteria tagFilters                      = null;
    private AtlasFilterCriteria relationshipFilters             = null;
    private Set<String>         attributes                      = null;
    private AtlasSortOrder      sortOrder                       = null;


    public AtlasSearchParameters()
    {
    }


    public String getQuery()
    {
        return query;
    }


    public void setQuery(String query)
    {
        this.query = query;
    }


    public String getTypeName()
    {
        return typeName;
    }


    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    public String getClassification()
    {
        return classification;
    }


    public void setClassification(String classification)
    {
        this.classification = classification;
    }


    public String getRelationshipName()
    {
        return relationshipName;
    }


    public void setRelationshipName(String relationshipName)
    {
        this.relationshipName = relationshipName;
    }


    public String getTermName()
    {
        return termName;
    }


    public void setTermName(String termName)
    {
        this.termName = termName;
    }


    public String getSortBy()
    {
        return sortBy;
    }


    public void setSortBy(String sortBy)
    {
        this.sortBy = sortBy;
    }


    public boolean isExcludeDeletedEntities()
    {
        return excludeDeletedEntities;
    }


    public void setExcludeDeletedEntities(boolean excludeDeletedEntities)
    {
        this.excludeDeletedEntities = excludeDeletedEntities;
    }


    public boolean isIncludeClassificationAttributes()
    {
        return includeClassificationAttributes;
    }


    public void setIncludeClassificationAttributes(boolean includeClassificationAttributes)
    {
        this.includeClassificationAttributes = includeClassificationAttributes;
    }


    public boolean isIncludeSubTypes()
    {
        return includeSubTypes;
    }


    public void setIncludeSubTypes(boolean includeSubTypes)
    {
        this.includeSubTypes = includeSubTypes;
    }


    public boolean isIncludeSubClassifications()
    {
        return includeSubClassifications;
    }


    public void setIncludeSubClassifications(boolean includeSubClassifications)
    {
        this.includeSubClassifications = includeSubClassifications;
    }


    public boolean isExcludeHeaderAttributes()
    {
        return excludeHeaderAttributes;
    }


    public void setExcludeHeaderAttributes(boolean excludeHeaderAttributes)
    {
        this.excludeHeaderAttributes = excludeHeaderAttributes;
    }


    public int getLimit()
    {
        return limit;
    }


    public void setLimit(int limit)
    {
        this.limit = limit;
    }


    public int getOffset()
    {
        return offset;
    }


    public void setOffset(int offset)
    {
        this.offset = offset;
    }


    public String getMarker()
    {
        return marker;
    }


    public void setMarker(String marker)
    {
        this.marker = marker;
    }


    public AtlasFilterCriteria getEntityFilters()
    {
        return entityFilters;
    }


    public void setEntityFilters(AtlasFilterCriteria entityFilters)
    {
        this.entityFilters = entityFilters;
    }


    public AtlasFilterCriteria getTagFilters()
    {
        return tagFilters;
    }


    public void setTagFilters(AtlasFilterCriteria tagFilters)
    {
        this.tagFilters = tagFilters;
    }


    public AtlasFilterCriteria getRelationshipFilters()
    {
        return relationshipFilters;
    }


    public void setRelationshipFilters(AtlasFilterCriteria relationshipFilters)
    {
        this.relationshipFilters = relationshipFilters;
    }


    public Set<String> getAttributes()
    {
        return attributes;
    }


    public void setAttributes(Set<String> attributes)
    {
        this.attributes = attributes;
    }


    public AtlasSortOrder getSortOrder()
    {
        return sortOrder;
    }


    public void setSortOrder(AtlasSortOrder sortOrder)
    {
        this.sortOrder = sortOrder;
    }


    @Override
    public String toString()
    {
        return "AtlasSearchParameters{" +
                       "query='" + query + '\'' +
                       ", typeName='" + typeName + '\'' +
                       ", classification='" + classification + '\'' +
                       ", relationshipName='" + relationshipName + '\'' +
                       ", termName='" + termName + '\'' +
                       ", sortBy='" + sortBy + '\'' +
                       ", excludeDeletedEntities=" + excludeDeletedEntities +
                       ", includeClassificationAttributes=" + includeClassificationAttributes +
                       ", includeSubTypes=" + includeSubTypes +
                       ", includeSubClassifications=" + includeSubClassifications +
                       ", excludeHeaderAttributes=" + excludeHeaderAttributes +
                       ", limit=" + limit +
                       ", offset=" + offset +
                       ", marker='" + marker + '\'' +
                       ", entityFilters=" + entityFilters +
                       ", tagFilters=" + tagFilters +
                       ", relationshipFilters=" + relationshipFilters +
                       ", attributes=" + attributes +
                       ", sortOrder=" + sortOrder +
                       '}';
    }
}
