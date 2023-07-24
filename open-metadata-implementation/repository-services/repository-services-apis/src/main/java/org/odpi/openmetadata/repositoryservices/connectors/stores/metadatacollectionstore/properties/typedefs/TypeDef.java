/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The TypeDef is the base class for objects that store the properties of an open metadata type
 * definition (call ed a TypeDef).
 * <p>
 * The different categories of Typedefs are listed in TypeDefCategory.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = EntityDef.class, name = "EntityDef"),
                @JsonSubTypes.Type(value = RelationshipDef.class, name = "RelationshipDef"),
                @JsonSubTypes.Type(value = ClassificationDef.class, name = "ClassificationDef"),
        })
public abstract class TypeDef extends TypeDefSummary
{
    private static final long    serialVersionUID = 1L;

    protected TypeDefLink                   superType                = null;
    protected String                        description              = null;
    protected String                        descriptionGUID          = null;
    protected String                        origin                   = null;
    protected String                        createdBy                = null;
    protected String                        updatedBy                = null;
    protected Date                          createTime               = null;
    protected Date                          updateTime               = null;
    protected Map<String, String>           options                  = null;
    protected List<ExternalStandardMapping> externalStandardMappings = null;
    protected List<InstanceStatus>          validInstanceStatusList  = null;
    protected InstanceStatus                initialStatus            = null;
    protected List<TypeDefAttribute>        propertiesDefinition     = null;


    /**
     * Default constructor
     */
    protected TypeDef()
    {
    }


    /**
     * Minimal constructor is passed the category of the typedef being constructed.
     * The rest of the properties are null.
     *
     * @param category TypeDefCategory enum
     */
    protected TypeDef(TypeDefCategory category)
    {
        super();
        this.category = category;
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     * This should only be used for new TypeDefs.
     *
     * @param category    category of this TypeDef
     * @param guid        unique id for the TypeDef
     * @param name        unique name for the TypeDef
     * @param version     active version number for the TypeDef
     * @param versionName name for the active version of the TypeDef
     */
    TypeDef(TypeDefCategory category,
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
     * @param template TypeDef
     */
    protected TypeDef(TypeDef template)
    {
        super(template);

        if (template != null)
        {
            this.superType = template.getSuperType();
            this.description = template.getDescription();
            this.descriptionGUID = template.getDescriptionGUID();
            this.origin = template.getOrigin();
            this.createdBy = template.getCreatedBy();
            this.updatedBy = template.getUpdatedBy();
            this.createTime = template.getCreateTime();
            this.updateTime = template.getUpdateTime();
            this.options = template.getOptions();
            this.externalStandardMappings = template.getExternalStandardMappings();
            this.validInstanceStatusList = template.getValidInstanceStatusList();
            this.propertiesDefinition = template.getPropertiesDefinition();
            this.initialStatus = template.getInitialStatus();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of TypeDef
     */
    public abstract TypeDef cloneFromSubclass();



    /**
     * Return the super type for the TypeDef (or null if top-level)
     *
     * @return TypeDefLink for the super type
     */
    public TypeDefLink getSuperType()
    {
        if (superType == null)
        {
            return null;
        }
        else
        {
            return new TypeDefLink(superType);
        }
    }


    /**
     * Set up supertype for the TypeDef.  Only single inheritance is supported.  Use null if this type
     * is top-level.
     *
     * @param superType TypeDefLink for the super type
     */
    public void setSuperType(TypeDefLink superType)
    {
        if (superType == null)
        {
            this.superType = null;
        }
        else
        {
            this.superType = new TypeDefLink(superType);
        }
    }


    /**
     * Return the description of this TypeDef.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of this TypeDef.
     *
     * @param description String
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier (guid) of the glossary term that describes this TypeDef.
     *
     * @return String guid
     */
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Set up the unique identifier (guid) of the glossary term that describes this TypeDef.
     *
     * @param descriptionGUID String guid
     */
    public void setDescriptionGUID(String descriptionGUID)
    {
        this.descriptionGUID = descriptionGUID;
    }


    /**
     * Return the unique identifier for metadata collection id where this TypeDef came from.
     *
     * @return String guid
     */
    public String getOrigin()
    {
        return origin;
    }


    /**
     * Set up the unique identifier for metadata collection id where this TypeDef came from.
     *
     * @param origin String guid
     */
    public void setOrigin(String origin)
    {
        this.origin = origin;
    }


    /**
     * Return the user name of the person that created this TypeDef.
     *
     * @return String name
     */
    public String getCreatedBy()
    {
        return createdBy;
    }


    /**
     * Set up the user name of the person that created this TypeDef.
     *
     * @param createdBy String name
     */
    public void setCreatedBy(String createdBy)
    {
        this.createdBy = createdBy;
    }


    /**
     * Return the user name of the person that last updated this TypeDef.
     *
     * @return String name
     */
    public String getUpdatedBy()
    {
        return updatedBy;
    }


    /**
     * Set up the user name of the person that last updated this TypeDef.
     *
     * @param updatedBy String name
     */
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }


    /**
     * Return the date/time that this TypeDef was created.
     *
     * @return Date
     */
    public Date getCreateTime()
    {
        return createTime;
    }


    /**
     * Set up the date/time that this TypeDef was created.
     *
     * @param createTime Date
     */
    public void setCreateTime(Date createTime)
    {
        this.createTime = createTime;
    }


    /**
     * Return the date/time that this TypeDef was last updated.
     *
     * @return Date
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    /**
     * Set up the date/time that this TypeDef was last updated.
     *
     * @param updateTime Date
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * Return the options for this TypeDef. These are private properties used by the processors of this TypeDef
     * and ignored by the OMRS.
     *
     * @return Map from String to String
     */
    public Map<String, String> getOptions()
    {
        if (options == null)
        {
            return null;
        }
        else if (options.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(options);
        }
    }


    /**
     * Set up the options for this TypeDef.  These are private properties used by the processors of this TypeDef
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
    public List<ExternalStandardMapping> getExternalStandardMappings()
    {
        if (externalStandardMappings == null)
        {
            return null;
        }
        else if (externalStandardMappings.isEmpty())
        {
            return null;
        }
        else
        {
            List<ExternalStandardMapping>  clonedList = new ArrayList<>();

            for (ExternalStandardMapping  existingElement : externalStandardMappings)
            {
                clonedList.add(new ExternalStandardMapping(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of mappings to external standards.
     *
     * @param externalStandardMappings ExternalStandardMappings list
     */
    public void setExternalStandardMappings(List<ExternalStandardMapping> externalStandardMappings)
    {
        this.externalStandardMappings = externalStandardMappings;
    }


    /**
     * Return the list of valid statuses for an instance of this TypeDef.
     *
     * @return list of valid statuses
     */
    public List<InstanceStatus> getValidInstanceStatusList()
    {
        if (validInstanceStatusList == null)
        {
            return null;
        }
        else if (validInstanceStatusList.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(validInstanceStatusList);
        }
    }


    /**
     * Set up the list of valid instance statuses supported by this TypeDef.
     *
     * @param validInstanceStatusList InstanceStatus Array
     */
    public void setValidInstanceStatusList(List<InstanceStatus> validInstanceStatusList)
    {
        this.validInstanceStatusList = validInstanceStatusList;
    }


    /**
     * Return the initial status setting for an instance of this type.
     *
     * @return InstanceStatus enum
     */
    public InstanceStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status setting for an instance of this type.
     *
     * @param initialStatus InstanceStatus enum
     */
    public void setInitialStatus(InstanceStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * Return the list of typeDefAttributes that are either new or changing.
     *
     * @return list of TypeDefAttribute
     */
    public List<TypeDefAttribute> getPropertiesDefinition()
    {
        if (propertiesDefinition == null)
        {
            return null;
        }
        else if (propertiesDefinition.isEmpty())
        {
            return null;
        }
        else
        {
            List<TypeDefAttribute>  clonedList = new ArrayList<>();

            for (TypeDefAttribute  existingElement : propertiesDefinition)
            {
                clonedList.add(new TypeDefAttribute(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of AttributeDefs that define the valid properties for this type of classification.
     *
     * @param propertiesDefinition AttributeDefs list
     */
    public void setPropertiesDefinition(List<TypeDefAttribute> propertiesDefinition)
    {
        this.propertiesDefinition = propertiesDefinition;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "TypeDef{" +
                "name='" + name + '\'' +
                ", superType=" + superType +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
                ", origin='" + origin + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", options=" + options +
                ", externalStandardMappings=" + externalStandardMappings +
                ", validInstanceStatusList=" + validInstanceStatusList +
                ", initialStatus=" + initialStatus +
                ", propertiesDefinition=" + propertiesDefinition +
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
        TypeDef typeDef = (TypeDef) objectToCompare;
        return Objects.equals(superType, typeDef.superType) &&
                       Objects.equals(description, typeDef.description) &&
                       Objects.equals(descriptionGUID, typeDef.descriptionGUID) &&
                       Objects.equals(origin, typeDef.origin) &&
                       Objects.equals(createdBy, typeDef.createdBy) &&
                       Objects.equals(updatedBy, typeDef.updatedBy) &&
                       Objects.equals(createTime, typeDef.createTime) &&
                       Objects.equals(updateTime, typeDef.updateTime) &&
                       Objects.equals(options, typeDef.options) &&
                       Objects.equals(externalStandardMappings, typeDef.externalStandardMappings) &&
                       Objects.equals(validInstanceStatusList, typeDef.validInstanceStatusList) &&
                       initialStatus == typeDef.initialStatus &&
                       Objects.equals(propertiesDefinition, typeDef.propertiesDefinition);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), superType, description, descriptionGUID, origin, createdBy, updatedBy, createTime, updateTime, options,
                            externalStandardMappings, validInstanceStatusList, initialStatus, propertiesDefinition);
    }
}
