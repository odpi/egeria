/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The OpenMetadataTypeDef is the base class for objects that store the properties of an open metadata type
 * definition (call ed a OpenMetadataTypeDef).
 * <p>
 * The different categories of Typedefs are listed in OpenMetadataTypeDefCategory.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = OpenMetadataEntityDef.class, name = "OpenMetadataEntityDef"),
                @JsonSubTypes.Type(value = OpenMetadataRelationshipDef.class, name = "OpenMetadataRelationshipDef"),
                @JsonSubTypes.Type(value = OpenMetadataClassificationDef.class, name = "OpenMetadataClassificationDef"),
        })
public abstract class OpenMetadataTypeDef extends OpenMetadataTypeDefSummary
{
    protected OpenMetadataTypeDefLink            superType                    = null;
    protected String                             description                  = null;
    protected String                             descriptionGUID              = null;
    protected String                             descriptionWiki              = null;
    protected String                             origin                       = null;
    protected String                             createdBy                    = null;
    protected String                             updatedBy                    = null;
    protected Date                               createTime                   = null;
    protected Date                               updateTime                   = null;
    protected Map<String, String>                options                      = null;
    protected List<ExternalStandardTypeMapping>  externalStandardTypeMappings = null;
    protected List<ElementStatus>                validElementStatusList       = null;
    protected ElementStatus                      initialStatus                = null;
    protected List<OpenMetadataTypeDefAttribute> attributeDefinitions         = null;


    /**
     * Default constructor
     */
    protected OpenMetadataTypeDef()
    {
    }


    /**
     * Minimal constructor is passed the category of the typedef being constructed.
     * The rest of the properties are null.
     *
     * @param category OpenMetadataTypeDefCategory enum
     */
    protected OpenMetadataTypeDef(OpenMetadataTypeDefCategory category)
    {
        super();
        this.category = category;
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     * This should only be used for new TypeDefs.
     *
     * @param category    category of this OpenMetadataTypeDef
     * @param guid        unique id for the OpenMetadataTypeDef
     * @param name        unique name for the OpenMetadataTypeDef
     * @param version     active version number for the OpenMetadataTypeDef
     * @param versionName name for the active version of the OpenMetadataTypeDef
     */
    OpenMetadataTypeDef(OpenMetadataTypeDefCategory category,
                        String          guid,
                        String          name,
                        long            version,
                        String          versionName)
    {
        super(category, guid, name, version, versionName);
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.
     *
     * @param template OpenMetadataTypeDef
     */
    protected OpenMetadataTypeDef(OpenMetadataTypeDef template)
    {
        super(template);

        if (template != null)
        {
            this.superType = template.getSuperType();
            this.description = template.getDescription();
            this.descriptionGUID = template.getDescriptionGUID();
            this.descriptionWiki = template.getDescriptionWiki();
            this.origin = template.getOrigin();
            this.createdBy = template.getCreatedBy();
            this.updatedBy = template.getUpdatedBy();
            this.createTime = template.getCreateTime();
            this.updateTime = template.getUpdateTime();
            this.options = template.getOptions();
            this.externalStandardTypeMappings = template.getExternalStandardMappings();
            this.validElementStatusList = template.getValidElementStatusList();
            this.attributeDefinitions = template.getAttributeDefinitions();
            this.initialStatus = template.getInitialStatus();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataTypeDef
     */
    public abstract OpenMetadataTypeDef cloneFromSubclass();



    /**
     * Return the super type for the OpenMetadataTypeDef (or null if top-level)
     *
     * @return OpenMetadataTypeDefLink for the super type
     */
    public OpenMetadataTypeDefLink getSuperType()
    {
        if (superType == null)
        {
            return null;
        }
        else
        {
            return new OpenMetadataTypeDefLink(superType);
        }
    }


    /**
     * Set up supertype for the OpenMetadataTypeDef.  Only single inheritance is supported.  Use null if this type
     * is top-level.
     *
     * @param superType OpenMetadataTypeDefLink for the super type
     */
    public void setSuperType(OpenMetadataTypeDefLink superType)
    {
        if (superType == null)
        {
            this.superType = null;
        }
        else
        {
            this.superType = new OpenMetadataTypeDefLink(superType);
        }
    }


    /**
     * Return the description of this OpenMetadataTypeDef.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of this OpenMetadataTypeDef.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier (guid) of the valid value that describes this OpenMetadataTypeDef.
     *
     * @return String guid
     */
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Set up the unique identifier (guid) of the valid value definition that describes this OpenMetadataTypeDef.
     *
     * @param descriptionGUID String guid
     */
    public void setDescriptionGUID(String descriptionGUID)
    {
        this.descriptionGUID = descriptionGUID;
    }

    /**
     * Return the URL to the wiki page that gives more information for this type.
     *
     * @return url string
     */
    public String getDescriptionWiki()
    {
        return descriptionWiki;
    }


    /**
     * Set up the URL to the wiki page that gives more information for this type.
     *
     * @param descriptionWiki url string
     */
    public void setDescriptionWiki(String descriptionWiki)
    {
        this.descriptionWiki = descriptionWiki;
    }


    /**
     * Return the unique identifier for metadata collection id where this OpenMetadataTypeDef came from.
     *
     * @return String guid
     */
    public String getOrigin()
    {
        return origin;
    }


    /**
     * Set up the unique identifier for metadata collection id where this OpenMetadataTypeDef came from.
     *
     * @param origin String guid
     */
    public void setOrigin(String origin)
    {
        this.origin = origin;
    }


    /**
     * Return the user name of the person that created this OpenMetadataTypeDef.
     *
     * @return String name
     */
    public String getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set up the user name of the person that created this OpenMetadataTypeDef.
     *
     * @param createdBy String name
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Return the user name of the person that last updated this OpenMetadataTypeDef.
     *
     * @return String name
     */
    public String getUpdatedBy()
    {
        return updatedBy;
    }


    /**
     * Set up the user name of the person that last updated this OpenMetadataTypeDef.
     *
     * @param updatedBy String name
     */
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }


    /**
     * Return the date/time that this OpenMetadataTypeDef was created.
     *
     * @return Date
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    /**
     * Set up the date/time that this OpenMetadataTypeDef was created.
     *
     * @param createTime Date
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    /**
     * Return the date/time that this OpenMetadataTypeDef was last updated.
     *
     * @return Date
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    /**
     * Set up the date/time that this OpenMetadataTypeDef was last updated.
     *
     * @param updateTime Date
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * Return the options for this OpenMetadataTypeDef. These are private properties used by the processors of this OpenMetadataTypeDef
     * and ignored by the OMRS.
     *
     * @return Map from String to String
     */
    public Map<String, String> getOptions()
    {
        return options;
    }


    /**
     * Set up the options for this OpenMetadataTypeDef.  These are private properties used by the processors of this OpenMetadataTypeDef
     * and ignored by the OMRS.
     *
     * @param options Map from String to String
     */
    public void setOptions(Map<String, String> options)
    {
        this.options = options;
    }


    /**
     * Return the list of mappings to external standards.
     *
     * @return ExternalStandardMappings list
     */
    public List<ExternalStandardTypeMapping> getExternalStandardMappings()
    {
        return externalStandardTypeMappings;
    }


    /**
     * Set up the list of mappings to external standards.
     *
     * @param externalStandardTypeMappings ExternalStandardMappings list
     */
    public void setExternalStandardMappings(List<ExternalStandardTypeMapping> externalStandardTypeMappings)
    {
        this.externalStandardTypeMappings = externalStandardTypeMappings;
    }


    /**
     * Return the list of valid statuses for an instance of this OpenMetadataTypeDef.
     *
     * @return list of valid statuses
     */
    public List<ElementStatus> getValidElementStatusList()
    {
        return validElementStatusList;
    }


    /**
     * Set up the list of valid instance statuses supported by this OpenMetadataTypeDef.
     *
     * @param validElementStatusList InstanceStatus Array
     */
    public void setValidElementStatusList(List<ElementStatus> validElementStatusList)
    {
        this.validElementStatusList = validElementStatusList;
    }


    /**
     * Return the initial status setting for an instance of this type.
     *
     * @return InstanceStatus enum
     */
    public ElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status setting for an instance of this type.
     *
     * @param initialStatus InstanceStatus enum
     */
    public void setInitialStatus(ElementStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * Return the list of typeDefAttributes that are either new or changing.
     *
     * @return list of OpenMetadataTypeDefAttribute
     */
    public List<OpenMetadataTypeDefAttribute> getAttributeDefinitions()
    {
        return attributeDefinitions;
    }


    /**
     * Set up the list of AttributeDefs that define the valid properties for this type of classification.
     *
     * @param attributeDefinitions AttributeDefs list
     */
    public void setAttributeDefinitions(List<OpenMetadataTypeDefAttribute> attributeDefinitions)
    {
        this.attributeDefinitions = attributeDefinitions;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataTypeDef{" +
                "name='" + name + '\'' +
                ", superType=" + superType +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
                ", descriptionWiki='" + descriptionWiki + '\'' +
                ", origin='" + origin + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", options=" + options +
                ", externalStandardTypeMappings=" + externalStandardTypeMappings +
                ", validInstanceStatusList=" + validElementStatusList +
                ", initialStatus=" + initialStatus +
                ", propertiesDefinition=" + attributeDefinitions +
                ", version=" + version +
                ", versionName='" + versionName + '\'' +
                ", category=" + category +
                ", guid='" + guid + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OpenMetadataTypeDef openMetadataTypeDef = (OpenMetadataTypeDef) objectToCompare;
        return Objects.equals(superType, openMetadataTypeDef.superType) &&
                       Objects.equals(description, openMetadataTypeDef.description) &&
                       Objects.equals(descriptionGUID, openMetadataTypeDef.descriptionGUID) &&
                       Objects.equals(descriptionWiki, openMetadataTypeDef.descriptionWiki) &&
                       Objects.equals(origin, openMetadataTypeDef.origin) &&
                       Objects.equals(createdBy, openMetadataTypeDef.createdBy) &&
                       Objects.equals(updatedBy, openMetadataTypeDef.updatedBy) &&
                       Objects.equals(createTime, openMetadataTypeDef.createTime) &&
                       Objects.equals(updateTime, openMetadataTypeDef.updateTime) &&
                       Objects.equals(options, openMetadataTypeDef.options) &&
                       Objects.equals(externalStandardTypeMappings, openMetadataTypeDef.externalStandardTypeMappings) &&
                       Objects.equals(validElementStatusList, openMetadataTypeDef.validElementStatusList) &&
                       initialStatus == openMetadataTypeDef.initialStatus &&
                       Objects.equals(attributeDefinitions, openMetadataTypeDef.attributeDefinitions);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), superType, description, descriptionGUID, descriptionWiki, origin, createdBy, updatedBy, createTime,
                            updateTime, options, externalStandardTypeMappings, validElementStatusList, initialStatus, attributeDefinitions);
    }
}
