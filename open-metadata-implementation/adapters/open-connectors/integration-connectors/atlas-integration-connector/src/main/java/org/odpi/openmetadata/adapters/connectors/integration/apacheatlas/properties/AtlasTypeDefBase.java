/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasTypeDefBase provides the common attributes found in all type definitions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasTypeDefBase
{
    public static final String ATLAS_TYPE_BOOLEAN        = "boolean";
    public static final String ATLAS_TYPE_BYTE           = "byte";
    public static final String ATLAS_TYPE_SHORT          = "short";
    public static final String ATLAS_TYPE_INT            = "int";
    public static final String ATLAS_TYPE_LONG           = "long";
    public static final String ATLAS_TYPE_FLOAT          = "float";
    public static final String ATLAS_TYPE_DOUBLE         = "double";
    public static final String ATLAS_TYPE_BIGINTEGER     = "biginteger";
    public static final String ATLAS_TYPE_BIGDECIMAL     = "bigdecimal";
    public static final String ATLAS_TYPE_STRING         = "string";
    public static final String ATLAS_TYPE_DATE           = "date";
    public static final String ATLAS_TYPE_OBJECT_ID      = "objectid";

    public static final String ATLAS_TYPE_ARRAY_PREFIX    = "array<";
    public static final String ATLAS_TYPE_ARRAY_SUFFIX    = ">";
    public static final String ATLAS_TYPE_MAP_PREFIX      = "map<";
    public static final String ATLAS_TYPE_MAP_KEY_VAL_SEP = ",";
    public static final String ATLAS_TYPE_MAP_SUFFIX      = ">";

    public static final String ATLAS_TYPE_PROCESS        = "Process";
    public static final String ATLAS_TYPE_DATASET        = "DataSet";
    public static final String ATLAS_TYPE_ASSET          = "Asset";
    public static final String ATLAS_TYPE_INFRASTRUCTURE = "Infrastructure";

    public static final String TYPEDEF_OPTION_SUPPORTS_SCHEMA  = "supportsSchema";
    public static final String TYPEDEF_OPTION_SUPPORTS_PROFILE = "supportsProfile";

    public static final String[] ATLAS_PRIMITIVE_TYPES = {
            ATLAS_TYPE_BOOLEAN,
            ATLAS_TYPE_BYTE,
            ATLAS_TYPE_SHORT,
            ATLAS_TYPE_INT,
            ATLAS_TYPE_LONG,
            ATLAS_TYPE_FLOAT,
            ATLAS_TYPE_DOUBLE,
            ATLAS_TYPE_BIGINTEGER,
            ATLAS_TYPE_BIGDECIMAL,
            ATLAS_TYPE_STRING,
    };

    /**
     * The list of types that are valid for relationships. These are the
     * primitive attributes and date.
     */
    public static final String[] ATLAS_RELATIONSHIP_ATTRIBUTE_TYPES = { ATLAS_TYPE_BOOLEAN,
            ATLAS_TYPE_BYTE,
            ATLAS_TYPE_SHORT,
            ATLAS_TYPE_INT,
            ATLAS_TYPE_LONG,
            ATLAS_TYPE_FLOAT,
            ATLAS_TYPE_DOUBLE,
            ATLAS_TYPE_BIGINTEGER,
            ATLAS_TYPE_BIGDECIMAL,
            ATLAS_TYPE_STRING,
            ATLAS_TYPE_DATE
    };

    public static final String[] ATLAS_BUILTIN_TYPES = {
            ATLAS_TYPE_BOOLEAN,
            ATLAS_TYPE_BYTE,
            ATLAS_TYPE_SHORT,
            ATLAS_TYPE_INT,
            ATLAS_TYPE_LONG,
            ATLAS_TYPE_FLOAT,
            ATLAS_TYPE_DOUBLE,
            ATLAS_TYPE_BIGINTEGER,
            ATLAS_TYPE_BIGDECIMAL,
            ATLAS_TYPE_STRING,

            ATLAS_TYPE_DATE,
            ATLAS_TYPE_OBJECT_ID,
    };

    private AtlasTypeCategory category    = null;
    private String            guid        = null;
    private String            createdBy   = null;
    private String            updateBy    = null;
    private long              createTime  = 0L;
    private long              updateTime  = 0L;
    private long              version     = 0L;
    private String            name        = null;
    private String            description = null;
    private String            typeVersion = null;
    private String            serviceType = null;


    /**
     * Default constructor
     */
    public AtlasTypeDefBase()
    {
    }


    public AtlasTypeCategory getCategory()
    {
        return category;
    }


    public void setCategory(AtlasTypeCategory category)
    {
        this.category = category;
    }


    public String getGuid()
    {
        return guid;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public String getCreatedBy()
    {
        return createdBy;
    }


    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    public String getUpdateBy()
    {
        return updateBy;
    }


    public void setUpdateBy(String updateBy)
    {
        this.updateBy = updateBy;
    }


    public long getCreateTime()
    {
        return createTime;
    }


    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }


    public long getUpdateTime()
    {
        return updateTime;
    }


    public void setUpdateTime(long updateTime)
    {
        this.updateTime = updateTime;
    }


    public long getVersion()
    {
        return version;
    }


    public void setVersion(long version)
    {
        this.version = version;
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getTypeVersion()
    {
        return typeVersion;
    }


    public void setTypeVersion(String typeVersion)
    {
        this.typeVersion = typeVersion;
    }


    public String getServiceType()
    {
        return serviceType;
    }


    public void setServiceType(String serviceType)
    {
        this.serviceType = serviceType;
    }
}
