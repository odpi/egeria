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
 * AtlasAttributeDef describes a single attribute of an Apache Atlas type definition.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasAttributeDef
{
    public static final int    DEFAULT_SEARCH_WEIGHT                    = - 1;
    public static final String SEARCH_WEIGHT_ATTR_NAME                  = "searchWeight";
    public static final String INDEX_TYPE_ATTR_NAME                     = "indexType";
    public static final String ATTR_DEF_OPTION_SOFT_REFERENCE           = "isSoftReference";
    public static final String ATTR_DEF_OPTION_APPEND_ON_PARTIAL_UPDATE = "isAppendOnPartialUpdate";
    public static final int    COUNT_NOT_SET                            = -1;

    private final       String STRING_TRUE                              = "true";


    private String                   name                  = null;
    private String                   typeName              = null;
    private boolean                  isOptional            = true;
    private AtlasCardinality         cardinality           = null;
    private int                      valuesMinCount        = COUNT_NOT_SET;
    private int                      valuesMaxCount        = COUNT_NOT_SET;
    private boolean                  isUnique              = false;
    private boolean                  isIndexable           = false;
    private boolean                  includeInNotification = false;
    private String                   defaultValue          = null;
    private String                   description           = null;
    private int                      searchWeight          = DEFAULT_SEARCH_WEIGHT;
    private AtlasIndexType           indexType             = null;
    private List<AtlasConstraintDef> constraints           = null;
    private Map<String, String>      options               = null;
    private String                   displayName           = null;


    public AtlasAttributeDef()
    {
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getTypeName()
    {
        return typeName;
    }


    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    public boolean isOptional()
    {
        return isOptional;
    }


    public void setOptional(boolean optional)
    {
        isOptional = optional;
    }


    public AtlasCardinality getCardinality()
    {
        return cardinality;
    }


    public void setCardinality(AtlasCardinality cardinality)
    {
        this.cardinality = cardinality;
    }


    public int getValuesMinCount()
    {
        return valuesMinCount;
    }


    public void setValuesMinCount(int valuesMinCount)
    {
        this.valuesMinCount = valuesMinCount;
    }


    public int getValuesMaxCount()
    {
        return valuesMaxCount;
    }


    public void setValuesMaxCount(int valuesMaxCount)
    {
        this.valuesMaxCount = valuesMaxCount;
    }


    public boolean isUnique()
    {
        return isUnique;
    }


    public void setUnique(boolean unique)
    {
        isUnique = unique;
    }


    public boolean isIndexable()
    {
        return isIndexable;
    }


    public void setIndexable(boolean indexable)
    {
        isIndexable = indexable;
    }


    public boolean isIncludeInNotification()
    {
        return includeInNotification;
    }


    public void setIncludeInNotification(boolean includeInNotification)
    {
        this.includeInNotification = includeInNotification;
    }


    public String getDefaultValue()
    {
        return defaultValue;
    }


    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public int getSearchWeight()
    {
        return searchWeight;
    }


    public void setSearchWeight(int searchWeight)
    {
        this.searchWeight = searchWeight;
    }


    public AtlasIndexType getIndexType()
    {
        return indexType;
    }


    public void setIndexType(AtlasIndexType indexType)
    {
        this.indexType = indexType;
    }


    public List<AtlasConstraintDef> getConstraints()
    {
        return constraints;
    }


    public void setConstraints(List<AtlasConstraintDef> constraints)
    {
        this.constraints = constraints;
    }


    public Map<String, String> getOptions()
    {
        return options;
    }


    public void setOptions(Map<String, String> options)
    {
        this.options = options;
    }


    public String getDisplayName()
    {
        return displayName;
    }


    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    @Override
    public String toString()
    {
        return "AtlasAttributeDef{" +
                       "name='" + name + '\'' +
                       ", typeName='" + typeName + '\'' +
                       ", isOptional=" + isOptional +
                       ", cardinality=" + cardinality +
                       ", valuesMinCount=" + valuesMinCount +
                       ", valuesMaxCount=" + valuesMaxCount +
                       ", isUnique=" + isUnique +
                       ", isIndexable=" + isIndexable +
                       ", includeInNotification=" + includeInNotification +
                       ", defaultValue='" + defaultValue + '\'' +
                       ", description='" + description + '\'' +
                       ", searchWeight=" + searchWeight +
                       ", indexType=" + indexType +
                       ", constraints=" + constraints +
                       ", options=" + options +
                       ", displayName='" + displayName + '\'' +
                       '}';
    }
}
