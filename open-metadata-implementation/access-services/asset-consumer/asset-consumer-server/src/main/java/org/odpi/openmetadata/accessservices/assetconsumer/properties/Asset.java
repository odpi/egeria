/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Asset describes the basic properties of an Asset
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Asset
{
    /*
     * URL where the metadata about the asset is located.  It remains null if no repository is known.
     */

    /*
     * Unique id for the asset.
     */
    protected String url              = null;
    protected String guid             = null;
    protected String typeId           = null;
    protected String typeName         = null;
    protected long   typeVersion      = 0;
    protected String typeDescription  = null;
    protected String qualifiedName    = null;
    protected String displayName      = null;
    protected String description      = null;
    protected String owner            = null;

    /**
     * Default constructor
     */
    public Asset()
    {
    }

    public String getURL()
    {
        return url;
    }

    public void setURL(String url)
    {
        this.url = url;
    }

    public String getGUID()
    {
        return guid;
    }

    public void setGUID(String guid)
    {
        this.guid = guid;
    }

    public String getTypeId()
    {
        return typeId;
    }

    public void setTypeId(String typeId)
    {
        this.typeId = typeId;
    }

    public String getTypeName()
    {
        return typeName;
    }

    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }

    public long getTypeVersion()
    {
        return typeVersion;
    }

    public void setTypeVersion(long typeVersion)
    {
        this.typeVersion = typeVersion;
    }

    public String getTypeDescription()
    {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription)
    {
        this.typeDescription = typeDescription;
    }

    public String getQualifiedName()
    {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getOwner()
    {
        return owner;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    @Override
    public String toString()
    {
        return "Asset{" +
                "url='" + url + '\'' +
                ", guid='" + guid + '\'' +
                ", typeId='" + typeId + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeVersion=" + typeVersion +
                ", typeDescription='" + typeDescription + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", URL='" + getURL() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                '}';
    }
}
