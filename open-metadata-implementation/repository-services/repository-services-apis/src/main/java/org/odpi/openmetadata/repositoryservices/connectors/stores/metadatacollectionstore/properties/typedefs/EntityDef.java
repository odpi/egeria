/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * EntityDef describes a type of entity in the metadata collection.  It is the simplest TypeDef that adds
 * no other properties beyond TypeDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class EntityDef extends TypeDef
{
    private static final long    serialVersionUID = 1L;

    /**
     * Minimal constructor initializes the superclass as an Entity
     */
    public EntityDef()
    {
        super(TypeDefCategory.ENTITY_DEF);
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     *
     * @param category    category of this TypeDef
     * @param guid        unique id for the TypeDef
     * @param name        unique name for the TypeDef
     * @param version     active version number for the TypeDef
     * @param versionName name for the active version of the TypeDef
     */
    public EntityDef(TypeDefCategory category,
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
     * @param template EntityDef
     */
    public EntityDef(EntityDef   template)
    {
        super(template);
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of TypeDef
     */
    public TypeDef cloneFromSubclass()
    {
        return new EntityDef(this);
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "EntityDef{" +
                "name='" + getName() + '\'' +
                ", superType=" + getSuperType() +
                ", description='" + getDescription() + '\'' +
                ", descriptionGUID='" + getDescriptionGUID() + '\'' +
                ", origin='" + getOrigin() + '\'' +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", options=" + getOptions() +
                ", externalStandardMappings=" + getExternalStandardMappings() +
                ", validInstanceStatusList=" + getValidInstanceStatusList() +
                ", initialStatus=" + getInitialStatus() +
                ", propertiesDefinition=" + getPropertiesDefinition() +
                ", category=" + getCategory() +
                ", version=" + getVersion() +
                ", versionName='" + getVersionName() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                '}';
    }
}
