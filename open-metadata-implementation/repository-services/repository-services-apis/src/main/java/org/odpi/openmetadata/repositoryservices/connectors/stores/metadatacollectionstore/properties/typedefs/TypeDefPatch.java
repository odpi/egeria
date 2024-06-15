/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.io.Serial;
import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * TypeDefPatch describes a change (patch) to a typeDef's properties, options, external standards mappings or
 * list of valid instance statuses.
 * A patch can be applied to an EntityDef, RelationshipDef or ClassificationDef.
 * Changes to a TypeDef's category or superclasses requires a new type definition.
 * In addition it is not possible to delete an attributeTypeDef through a patch.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefPatch extends TypeDefElementHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    private String                        typeDefGUID              = null;
    private String                        typeDefName              = null;
    private TypeDefStatus                 typeDefStatus            = null;
    private long                          applyToVersion           = 0L;
    private long                          updateToVersion          = 0L;
    private String                        newVersionName           = null;
    private String                        updatedBy                = null;
    private Date                          updateTime               = null;
    private String                        description              = null;
    private String                        descriptionGUID          = null;
    private TypeDefLink                   superType                = null;
    private List<TypeDefAttribute>        propertyDefinitions      = null;
    private Map<String, String>           typeDefOptions           = null;
    private List<ExternalStandardMapping> externalStandardMappings = null;
    private List<InstanceStatus>          validInstanceStatusList  = null;
    private InstanceStatus                initialStatus            = null;
    private List<TypeDefLink>             validEntityDefs          = null;  // ClassificationDefs
    private RelationshipEndDef            endDef1                  = null;  // RelationshipDefs
    private RelationshipEndDef            endDef2                  = null;  // RelationshipDefs
    private boolean                       updateMultiLink          = false; // RelationshipDefs
    private boolean                       multiLink                = false; // RelationshipDefs

    /**
     * Default constructor relies on the initialization of variables in their declaration.
     */
    public TypeDefPatch()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefPatch(TypeDefPatch   template)
    {
        super(template);

        if (template != null)
        {
            typeDefGUID              = template.getTypeDefGUID();
            typeDefName              = template.getTypeDefName();
            typeDefStatus            = template.getTypeDefStatus();
            applyToVersion           = template.getApplyToVersion();
            updateToVersion          = template.getUpdateToVersion();
            newVersionName           = template.getNewVersionName();
            updatedBy                = template.getUpdatedBy();
            updateTime               = template.getUpdateTime();
            description              = template.getDescription();
            descriptionGUID          = template.getDescriptionGUID();
            superType                = template.getSuperType();
            propertyDefinitions      = template.getPropertyDefinitions();
            typeDefOptions           = template.getTypeDefOptions();
            externalStandardMappings = template.getExternalStandardMappings();
            validInstanceStatusList  = template.getValidInstanceStatusList();
            initialStatus            = template.getInitialStatus();
            validEntityDefs          = template.getValidEntityDefs();
            endDef1                  = template.getEndDef1();
            endDef2                  = template.getEndDef2();
            updateMultiLink          = template.getUpdateMultiLink();
            multiLink                = template.getMultiLink();
        }
    }


    /**
     * Return the unique identifier for the affected TypeDef.
     *
     * @return String guid
     */
    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }


    /**
     * Set up the unique identifier for the affected TypeDef.
     *
     * @param typeDefGUID String guid
     */
    public void setTypeDefGUID(String typeDefGUID)
    {
        this.typeDefGUID = typeDefGUID;
    }


    /**
     * Return the unique name for the affected TypeDef.
     *
     * @return String name
     */
    public String getTypeDefName() {
        return typeDefName;
    }


    /**
     * Set up the unique name for the affected TypeDef.
     *
     * @param typeDefName String name
     */
    public void setTypeDefName(String typeDefName) {
        this.typeDefName = typeDefName;
    }


    /**
     * Return any change in status for the TypeDef.
     *
     * @return TypeDefStatus enum
     */
    public TypeDefStatus getTypeDefStatus()
    {
        return typeDefStatus;
    }


    /**
     * Set up a change in the status of the TypeDef
     *
     * @param typeDefStatus enum
     */
    public void setTypeDefStatus(TypeDefStatus typeDefStatus)
    {
        this.typeDefStatus = typeDefStatus;
    }


    /**
     * Return the version number of the TypeDef that this patch applies to.
     *
     * @return long version number
     */
    public long getApplyToVersion() {
        return applyToVersion;
    }


    /**
     * Set up the version number of the TypeDef that this patch applies to.
     *
     * @param applyToVersion long version number
     */
    public void setApplyToVersion(long applyToVersion) {
        this.applyToVersion = applyToVersion;
    }


    /**
     * Return the new version number of the TypeDef.
     *
     * @return long version number
     */
    public long getUpdateToVersion() {
        return updateToVersion;
    }


    /**
     * Set up the new version of the TypeDef.
     *
     * @param updateToVersion long version number
     */
    public void setUpdateToVersion(long updateToVersion) {
        this.updateToVersion = updateToVersion;
    }


    /**
     * Return the new version name ot use once the patch is applied.
     *
     * @return String version name
     */
    public String getNewVersionName()
    {
        return newVersionName;
    }


    /**
     * Set up the new version name ot use once the patch is applied.
     *
     * @param newVersionName String version name
     */
    public void setNewVersionName(String newVersionName)
    {
        this.newVersionName = newVersionName;
    }


    /**
     * Return the user name of the person that last updated the target TypeDef.
     *
     * @return String name
     */
    public String getUpdatedBy()
    {
        return updatedBy;
    }


    /**
     * Set up the user name of the person that last updated the target TypeDef.
     *
     * @param updatedBy String name
     */
    public void setUpdatedBy(String updatedBy)
    {
        this.updatedBy = updatedBy;
    }


    /**
     * Return the date/time that the target TypeDef was last updated.
     *
     * @return Date
     */
    public Date getUpdateTime()
    {
        return updateTime;
    }


    /**
     * Set up the date/time that the target TypeDef was last updated.
     *
     * @param updateTime Date
     */
    public void setUpdateTime(Date updateTime)
    {
        this.updateTime = updateTime;
    }


    /**
     * Return the new description for the TypeDef.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the new description for the TypeDef
     *
     * @param description String description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the unique identifier for the new glossary term that describes the TypeDef.
     *
     * @return String unique identifier
     */
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Set up the unique identifier for the new glossary term that describes the TypeDef.
     *
     * @param descriptionGUID String unique identifier
     */
    public void setDescriptionGUID(String descriptionGUID)
    {
        this.descriptionGUID = descriptionGUID;
    }


    /**
     * Return the new supertype.
     *
     * @return super type link
     */
    public TypeDefLink getSuperType()
    {
        if (superType == null)
        {
            return null;
        }

        return new TypeDefLink(superType);
    }


    /**
     * Set up the new super type.
     *
     * @param superType super type link
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
     * Return the list of typeDefAttributes that are either new or changing.
     *
     * @return list of TypeDefAttribute
     */
    public List<TypeDefAttribute> getPropertyDefinitions()
    {
        if (propertyDefinitions == null)
        {
            return null;
        }
        else if (propertyDefinitions.isEmpty())
        {
            return null;
        }
        else
        {
            List<TypeDefAttribute>  clonedList = new ArrayList<>();

            for (TypeDefAttribute  existingElement : propertyDefinitions)
            {
                clonedList.add(new TypeDefAttribute(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of typeDefAttributes that are either new or changing.
     *
     * @param propertyDefinitions list of AttributeDefs
     */
    public void setPropertyDefinitions(List<TypeDefAttribute> propertyDefinitions)
    {
        this.propertyDefinitions = propertyDefinitions;
    }


    /**
     * Return the TypeDef options for the patch.
     *
     * @return map of TypeDef Options that are new or changing.
     */
    public Map<String, String> getTypeDefOptions()
    {
        if (typeDefOptions == null)
        {
            return null;
        }
        else if (typeDefOptions.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(typeDefOptions);
        }
    }


    /**
     * Set up the TypeDef options for the patch.
     *
     * @param typeDefOptions map of TypeDef Options that are new or changing.
     */
    public void setTypeDefOptions(Map<String, String> typeDefOptions)
    {
        this.typeDefOptions = typeDefOptions;
    }


    /**
     * Return the list of External Standards Mappings that are either new or changing.
     *
     * @return list of external standards mappings
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
     * Set up the list of External Standards Mappings that are either new or changing.
     *
     * @param externalStandardMappings list of external standards mappings
     */
    public void setExternalStandardMappings(List<ExternalStandardMapping> externalStandardMappings)
    {
        this.externalStandardMappings = externalStandardMappings;
    }


    /**
     * Return the list of valid statuses for an instance of this TypeDefPatch.
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
     * Set up the new list of valid statuses for an instance of this TypeDef.
     * This list must be more extensive that the existing list.
     *
     * @param validInstanceStatusList list of valid statuses
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
     * Return a list of EntityDef names this this ClassificationDef can be attached to.
     *
     * @return list of type def links
     */
    public List<TypeDefLink> getValidEntityDefs()
    {
        return validEntityDefs;
    }


    /**
     * Set up a list of EntityDef names this this ClassificationDef can be attached to.
     * This list must be the same or bigger than the previous version.
     *
     * @param validEntityDefs list of type def links
     */
    public void setValidEntityDefs(List<TypeDefLink> validEntityDefs)
    {
        this.validEntityDefs = validEntityDefs;
    }


    /**
     * Return the details associated with the first end of the relationship.
     *
     * @return endDef1 RelationshipEndDef
     */
    public RelationshipEndDef getEndDef1()
    {
        if (endDef1 == null)
        {
            return null;
        }
        else
        {
            return new RelationshipEndDef(endDef1);
        }
    }


    /**
     * Set up the details associated with the first end of the relationship.
     *
     * @param endDef1 RelationshipEndDef
     */
    public void setEndDef1(RelationshipEndDef endDef1) { this.endDef1 = endDef1; }


    /**
     * Return the details associated with the second end of the relationship.
     *
     * @return endDef2 RelationshipEndDef
     */
    public RelationshipEndDef getEndDef2()
    {
        if (endDef2 == null)
        {
            return null;
        }
        else
        {
            return new RelationshipEndDef(endDef2);
        }
    }


    /**
     * Set up the details associated with the second end of the relationship.
     *
     * @param endDef2 RelationshipEndDef
     */
    public void setEndDef2(RelationshipEndDef endDef2) { this.endDef2 = endDef2; }


    /**
     * Return whether the multi-link flag should be updated.
     *
     * @return boolean flag
     */
    public boolean getUpdateMultiLink()
    {
        return updateMultiLink;
    }


    /**
     * Set up whether the multi-link flag should be updated.
     *
     * @param updateMultiLink boolean flag
     */
    public void setUpdateMultiLink(boolean updateMultiLink)
    {
        this.updateMultiLink = updateMultiLink;
    }


    /**
     * Return whether multiple relationships of this type are allowed between the same two entities.
     *
     * @return boolean flag
     */
    public boolean getMultiLink()
    {
        return multiLink;
    }


    /**
     * Set up whether multiple relationships of this type are allowed between the same two entities.
     *
     * @param multiLink boolean flag
     */
    public void setMultiLink(boolean multiLink)
    {
        this.multiLink = multiLink;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "TypeDefPatch{" +
                       "typeDefGUID='" + typeDefGUID + '\'' +
                       ", typeDefName='" + typeDefName + '\'' +
                       ", typeDefStatus=" + typeDefStatus +
                       ", applyToVersion=" + applyToVersion +
                       ", updateToVersion=" + updateToVersion +
                       ", newVersionName='" + newVersionName + '\'' +
                       ", updatedBy='" + updatedBy + '\'' +
                       ", updateTime=" + updateTime +
                       ", description='" + description + '\'' +
                       ", descriptionGUID='" + descriptionGUID + '\'' +
                       ", superType=" + superType +
                       ", propertyDefinitions=" + propertyDefinitions +
                       ", typeDefOptions=" + typeDefOptions +
                       ", externalStandardMappings=" + externalStandardMappings +
                       ", validInstanceStatusList=" + validInstanceStatusList +
                       ", initialStatus=" + initialStatus +
                       ", validEntityDefs=" + validEntityDefs +
                       ", endDef1=" + endDef1 +
                       ", endDef2=" + endDef2 +
                       ", updateMultiLink=" + updateMultiLink +
                       ", multiLink=" + multiLink +
                       ", headerVersion=" + getHeaderVersion() +
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
        TypeDefPatch that = (TypeDefPatch) objectToCompare;
        return applyToVersion == that.applyToVersion &&
                       updateToVersion == that.updateToVersion &&
                       updateMultiLink == that.updateMultiLink &&
                       multiLink == that.multiLink &&
                       Objects.equals(typeDefGUID, that.typeDefGUID) &&
                       Objects.equals(typeDefName, that.typeDefName) &&
                       typeDefStatus == that.typeDefStatus &&
                       Objects.equals(newVersionName, that.newVersionName) &&
                       Objects.equals(updatedBy, that.updatedBy) &&
                       Objects.equals(updateTime, that.updateTime) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(descriptionGUID, that.descriptionGUID) &&
                       Objects.equals(superType, that.superType) &&
                       Objects.equals(propertyDefinitions, that.propertyDefinitions) &&
                       Objects.equals(typeDefOptions, that.typeDefOptions) &&
                       Objects.equals(externalStandardMappings, that.externalStandardMappings) &&
                       Objects.equals(validInstanceStatusList, that.validInstanceStatusList) &&
                       initialStatus == that.initialStatus &&
                       Objects.equals(validEntityDefs, that.validEntityDefs) &&
                       Objects.equals(endDef1, that.endDef1) &&
                       Objects.equals(endDef2, that.endDef2);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(typeDefGUID, typeDefName, typeDefStatus, applyToVersion, updateToVersion, newVersionName, updatedBy, updateTime,
                            description,
                            descriptionGUID, superType, propertyDefinitions, typeDefOptions, externalStandardMappings, validInstanceStatusList,
                            initialStatus, validEntityDefs, endDef1, endDef2, updateMultiLink, multiLink);
    }
}
