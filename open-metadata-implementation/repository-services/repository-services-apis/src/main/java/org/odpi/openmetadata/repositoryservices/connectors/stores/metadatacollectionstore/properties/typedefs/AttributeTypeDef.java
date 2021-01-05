/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The AttributeTypeDef class is used to identify the type of an attribute.  These can be:
 * <ul>
 *     <li>PrimitiveDef</li>
 *     <li>CollectionDef</li>
 *     <li>EnumDef</li>
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
        @JsonSubTypes.Type(value = PrimitiveDef.class, name = "PrimitiveDef"),
        @JsonSubTypes.Type(value = CollectionDef.class, name = "CollectionDef"),
        @JsonSubTypes.Type(value = EnumDef.class, name = "EnumDef")
})
public abstract class AttributeTypeDef extends TypeDefElementHeader
{
    private static final long    serialVersionUID = 1L;

    protected long                     version         = 0L;
    protected String                   versionName     = null;
    protected AttributeTypeDefCategory category        = null;
    protected String                   guid            = null;
    protected String                   name            = null;
    protected String                   description     = null;
    protected String                   descriptionGUID = null;


    /**
     * Default constructor
     */
    protected AttributeTypeDef()
    {
        super();
    }


    /**
     * Minimal constructor is passed the category of the attribute type. Note that since
     * AttributeTypeDef is an abstract class, this method can only be called from a subclass.
     *
     * @param category category of this TypeDef
     */
    protected AttributeTypeDef(AttributeTypeDefCategory   category)
    {
        super();
        this.category = category;
    }


    /**
     * Typical constructor is passed the values that describe the type.  Note that since
     * AttributeTypeDef is an abstract class, this method can only be called from a subclass.
     *
     * @param category category of this TypeDef
     * @param guid unique id for the TypeDef
     * @param name unique name for the TypeDef
     */
    protected AttributeTypeDef(AttributeTypeDefCategory   category,
                               String                     guid,
                               String                     name)
    {
        super();

        this.category = category;
        this.guid = guid;
        this.name = name;
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.  Note that since
     * AttributeTypeDef is an abstract class, this method can only be called from a subclass.
     *
     * @param template AttributeTypeDef
     */
    protected AttributeTypeDef(AttributeTypeDef template)
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
     * @return subclass of AttributeTypeDef
     */
    public abstract AttributeTypeDef cloneFromSubclass();



    /**
     * Return the version of the AttributeTypeDef.  Versions are created when an AttributeTypeDef's properties
     * are changed.  If a description is updated, then this does not create a new version.
     *
     * @return String version number
     */
    public long getVersion()
    {
        return version;
    }


    /**
     * Set up the version of the AttributeTypeDef.  Versions are created when an AttributeTypeDef's properties
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
     * Return the category of the TypeDef.
     *
     * @return AttributeTypeDefCategory enum
     */
    public AttributeTypeDefCategory getCategory() { return category; }


    /**
     * Set up the category of the TypeDef.
     *
     * @param category AttributeTypeDefCategory enum
     */
    public void setCategory(AttributeTypeDefCategory category) { this.category = category; }


    /**
     * Return the unique identifier for this TypeDef.
     *
     * @return String guid
     */
    public String getGUID() { return guid; }


    /**
     * Set up the unique identifier for this TypeDef.
     *
     * @param guid String guid
     */
    public void setGUID(String guid) { this.guid = guid; }


    /**
     * Return the type name for this TypeDef.  In simple environments, the type name is unique but where metadata
     * repositories from different vendors are in operation it is possible that 2 types may have a name clash.  The
     * GUID is the reliable unique identifier.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Set up the type name for this TypeDef.  In simple environments, the type name is unique but where metadata
     * repositories from different vendors are in operation it is possible that 2 types may have a name clash.  The
     * GUID is the reliable unique identifier.
     *
     * @param name String name
     */
    public void setName(String name) { this.name = name; }


    /**
     * Return the short description of this AttributeTypeDef.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the short description of this AttributeTypeDef.
     *
     * @param description String description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier of the glossary term that describes this AttributeTypeDef.  Null means there
     * is no known glossary term.
     *
     * @return String guid
     */
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Set up the unique identifier of the glossary term that describes this AttributeTypeDef.  Null means there
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
        return "AttributeTypeDef{" +
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
        AttributeTypeDef that = (AttributeTypeDef) objectToCompare;
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

