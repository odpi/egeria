/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataEntityDef describes a type of entity in the metadata collection.  It is the simplest OpenMetadataTypeDef that adds
 * no other properties beyond OpenMetadataTypeDef.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataEntityDef extends OpenMetadataTypeDef
{
    private static final long    serialVersionUID = 1L;

    /**
     * Minimal constructor initializes the superclass as an Entity
     */
    public OpenMetadataEntityDef()
    {
        super(OpenMetadataTypeDefCategory.ENTITY_DEF);
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     *
     * @param category    category of this OpenMetadataTypeDef
     * @param guid        unique id for the OpenMetadataTypeDef
     * @param name        unique name for the OpenMetadataTypeDef
     * @param version     active version number for the OpenMetadataTypeDef
     * @param versionName name for the active version of the OpenMetadataTypeDef
     */
    public OpenMetadataEntityDef(OpenMetadataTypeDefCategory category,
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
     * @param template OpenMetadataEntityDef
     */
    public OpenMetadataEntityDef(OpenMetadataEntityDef template)
    {
        super(template);
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataTypeDef
     */
    public OpenMetadataTypeDef cloneFromSubclass()
    {
        return new OpenMetadataEntityDef(this);
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataEntityDef{" +
                "name='" + getName() + '\'' +
                ", superType=" + getSuperType() +
                ", description='" + getDescription() + '\'' +
                ", descriptionGUID='" + getDescriptionGUID() + '\'' +
                ", descriptionWiki='" + getDescriptionWiki() + '\'' +
                ", origin='" + getOrigin() + '\'' +
                ", createdBy='" + getCreatedBy() + '\'' +
                ", updatedBy='" + getUpdatedBy() + '\'' +
                ", createTime=" + getCreateTime() +
                ", updateTime=" + getUpdateTime() +
                ", options=" + getOptions() +
                ", externalStandardTypeMappings=" + getExternalStandardMappings() +
                ", validInstanceStatusList=" + getValidElementStatusList() +
                ", initialStatus=" + getInitialStatus() +
                ", propertiesDefinition=" + getAttributeDefinitions() +
                ", category=" + getCategory() +
                ", version=" + getVersion() +
                ", versionName='" + getVersionName() + '\'' +
                ", GUID='" + getGUID() + '\'' +
                '}';
    }
}
