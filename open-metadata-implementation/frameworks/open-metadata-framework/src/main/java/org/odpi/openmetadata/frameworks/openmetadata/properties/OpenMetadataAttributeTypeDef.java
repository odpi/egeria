/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The OpenMetadataAttributeTypeDef class is used to identify the type of an attribute.  These can be:
 * <ul>
 *     <li>OpenMetadataPrimitiveDef</li>
 *     <li>OpenMetadataCollectionDef</li>
 *     <li>OpenMetadataEnumDef</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenMetadataPrimitiveDef.class, name = "OpenMetadataPrimitiveDef"),
        @JsonSubTypes.Type(value = OpenMetadataCollectionDef.class, name = "OpenMetadataCollectionDef"),
        @JsonSubTypes.Type(value = OpenMetadataEnumDef.class, name = "OpenMetadataEnumDef")
})
public abstract class OpenMetadataAttributeTypeDef extends OpenMetadataTypeDefElementHeader
{
    protected long                                 version         = 0L;
    protected String                               versionName     = null;
    protected OpenMetadataAttributeTypeDefCategory category        = null;
    protected String                               guid            = null;
    protected String                               name            = null;
    protected String                               description     = null;
    protected String                               descriptionGUID = null;


    /**
     * Default constructor
     */
    protected OpenMetadataAttributeTypeDef()
    {
        super();
    }


    /**
     * Minimal constructor is passed the category of the attribute type. Note that since
     * OpenMetadataAttributeTypeDef is an abstract class, this method can only be called from a subclass.
     *
     * @param category category of this OpenMetadataTypeDef
     */
    protected OpenMetadataAttributeTypeDef(OpenMetadataAttributeTypeDefCategory category)
    {
        super();
        this.category = category;
    }


    /**
     * Typical constructor is passed the values that describe the type.  Note that since
     * OpenMetadataAttributeTypeDef is an abstract class, this method can only be called from a subclass.
     *
     * @param category category of this OpenMetadataTypeDef
     * @param guid unique id for the OpenMetadataTypeDef
     * @param name unique name for the OpenMetadataTypeDef
     */
    protected OpenMetadataAttributeTypeDef(OpenMetadataAttributeTypeDefCategory category,
                                           String                               guid,
                                           String                               name)
    {
        super();

        this.category = category;
        this.guid = guid;
        this.name = name;
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.  Note that since
     * OpenMetadataAttributeTypeDef is an abstract class, this method can only be called from a subclass.
     *
     * @param template OpenMetadataAttributeTypeDef
     */
    protected OpenMetadataAttributeTypeDef(OpenMetadataAttributeTypeDef template)
    {
        super(template);

        if (template != null)
        {
            this.version = template.getVersion();
            this.versionName = template.getVersionName();
            this.category = template.getCategory();
            this.guid = template.getGUID();
            this.name = template.getName();
            this.description = template.getDescription();
            this.descriptionGUID = template.getDescriptionGUID();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataAttributeTypeDef
     */
    public abstract OpenMetadataAttributeTypeDef cloneFromSubclass();



    /**
     * Return the version of the OpenMetadataAttributeTypeDef.  Versions are created when an OpenMetadataAttributeTypeDef's properties
     * are changed.  If a description is updated, then this does not create a new version.
     *
     * @return String version number
     */
    public long getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the OpenMetadataAttributeTypeDef.  Versions are created when an OpenMetadataAttributeTypeDef's properties
     * are changed.  If a description is updated, then this does not create a new version.
     *
     * @param version long version number
     */
    public void setVersion(long version)
    {
        this.version = version;
    }


    /**
     * Return the version name, which is a more of a human readable form of the version number.
     * It can be used to show whether the change is a minor or major update.
     *
     * @return String version name
     */
    public String getVersionName()
    {
        return versionName;
    }


    /**
     * Set up the version name, which is a more of a human readable form of the version number.
     * It can be used to show whether the change is a minor or major update.
     *
     * @param versionName String version name
     */
    public void setVersionName(String versionName)
    {
        this.versionName = versionName;
    }


    /**
     * Return the category of the OpenMetadataTypeDef.
     *
     * @return OpenMetadataAttributeTypeDefCategory enum
     */
    public OpenMetadataAttributeTypeDefCategory getCategory() { return category; }


    /**
     * Set up the category of the OpenMetadataTypeDef.
     *
     * @param category OpenMetadataAttributeTypeDefCategory enum
     */
    public void setCategory(OpenMetadataAttributeTypeDefCategory category) {this.category = category; }


    /**
     * Return the unique identifier for this OpenMetadataTypeDef.
     *
     * @return String guid
     */
    public String getGUID() { return guid; }


    /**
     * Set up the unique identifier for this OpenMetadataTypeDef.
     *
     * @param guid String guid
     */
    public void setGUID(String guid) { this.guid = guid; }


    /**
     * Return the type name for this OpenMetadataTypeDef.  In simple environments, the type name is unique but where metadata
     * repositories from different vendors are in operation it is possible that 2 types may have a name clash.  The
     * GUID is the reliable unique identifier.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Set up the type name for this OpenMetadataTypeDef.  In simple environments, the type name is unique but where metadata
     * repositories from different vendors are in operation it is possible that 2 types may have a name clash.  The
     * GUID is the reliable unique identifier.
     *
     * @param name String name
     */
    public void setName(String name) { this.name = name; }


    /**
     * Return the short description of this OpenMetadataAttributeTypeDef.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the short description of this OpenMetadataAttributeTypeDef.
     *
     * @param description String description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier of the glossary term that describes this OpenMetadataAttributeTypeDef.  Null means there
     * is no known glossary term.
     *
     * @return String guid
     */
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Set up the unique identifier of the glossary term that describes this OpenMetadataAttributeTypeDef.  Null means there
     * is no known glossary term.
     *
     * @param descriptionGUID String guid
     */
    public void setDescriptionGUID(String descriptionGUID)
    {
        this.descriptionGUID = descriptionGUID;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataAttributeTypeDef{" +
                "version=" + version +
                ", versionName='" + versionName + '\'' +
                ", category=" + category +
                ", guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
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
        OpenMetadataAttributeTypeDef that = (OpenMetadataAttributeTypeDef) objectToCompare;
        return version == that.version &&
                       Objects.equals(versionName, that.versionName) &&
                       category == that.category &&
                       Objects.equals(guid, that.guid) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(descriptionGUID, that.descriptionGUID);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(version, versionName, category, guid, name, description, descriptionGUID);
    }
}

