/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataClassificationDef stores the properties for the definition of a type of classification.  Many of the properties
 * are inherited from OpenMetadataTypeDef.  OpenMetadataClassificationDef adds a list of Entity Types that this Classification can be
 * connected to and a boolean to indicate if this classification is propagatable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataClassificationDef extends OpenMetadataTypeDef
{
    private static final long    serialVersionUID = 1L;

    private   List<OpenMetadataTypeDefLink> validEntityDefs = null;
    private   boolean                       propagatable    = false;


    /**
     * Minimal constructor sets up an empty OpenMetadataClassificationDef.
     */
    public OpenMetadataClassificationDef()
    {
        super(OpenMetadataTypeDefCategory.CLASSIFICATION_DEF);
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     *
     * @param category    category of this OpenMetadataTypeDef
     * @param guid        unique id for the OpenMetadataTypeDef
     * @param name        unique name for the OpenMetadataTypeDef
     * @param version     active version number for the OpenMetadataTypeDef
     * @param versionName name for active version of the OpenMetadataTypeDef
     */
    public OpenMetadataClassificationDef(OpenMetadataTypeDefCategory category,
                                         String          guid,
                                         String          name,
                                         long            version,
                                         String          versionName)
    {
        super(category, guid, name, version, versionName);
    }


    /**
     * Copy/clone constructor copies values from the supplied template.
     *
     * @param template template to copy
     */
    public OpenMetadataClassificationDef(OpenMetadataClassificationDef template)
    {
        super(template);

        if (template != null)
        {
            this.setValidEntityDefs(template.getValidEntityDefs());

            propagatable = template.isPropagatable();
        }
    }


    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of OpenMetadataTypeDef
     */
    public OpenMetadataTypeDef cloneFromSubclass()
    {
        return new OpenMetadataClassificationDef(this);
    }


    /**
     * Return the list of identifiers for the types of entities that this type of Classification can be connected to.
     *
     * @return List of entity type identifiers
     */
    public List<OpenMetadataTypeDefLink> getValidEntityDefs()
    {
        if (validEntityDefs == null)
        {
            return null;
        }
        else if (validEntityDefs.isEmpty())
        {
            return null;
        }
        else
        {
            List<OpenMetadataTypeDefLink> resultList = new ArrayList<>();

            for (OpenMetadataTypeDefLink typeDefLink : validEntityDefs)
            {
                if (typeDefLink != null)
                {
                    resultList.add(new OpenMetadataTypeDefLink(typeDefLink));
                }
            }

            return resultList;
        }
    }


    /**
     * Set up the list of identifiers for the types of entities that this type of Classification can be connected to.
     *
     * @param validEntityDefs List of entity type identifiers
     */
    public void setValidEntityDefs(List<OpenMetadataTypeDefLink> validEntityDefs)
    {
        if (validEntityDefs == null)
        {
            this.validEntityDefs = null;
        }
        else if (validEntityDefs.isEmpty())
        {
            this.validEntityDefs = null;
        }
        else
        {
            List<OpenMetadataTypeDefLink> resultList = new ArrayList<>();

            for (OpenMetadataTypeDefLink typeDefLink : validEntityDefs)
            {
                if (typeDefLink != null)
                {
                    resultList.add(new OpenMetadataTypeDefLink(typeDefLink));
                }
            }

            this.validEntityDefs = resultList;
        }
    }


    /**
     * Return whether this classification should propagate to other entities if the relationship linking them
     * allows classification propagation.
     *
     * @return boolean flag
     */
    public boolean isPropagatable()
    {
        return propagatable;
    }


    /**
     * Sets up whether this classification should propagate to other entities if the relationship linking them
     * allows classification propagation.
     *
     * @param propagatable boolean flag
     */
    public void setPropagatable(boolean propagatable)
    {
        this.propagatable = propagatable;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataClassificationDef{" +
                "name='" + name + '\'' +
                ", validEntityDefs=" + validEntityDefs +
                ", propagatable=" + propagatable +
                ", superType=" + superType +
                ", description='" + description + '\'' +
                ", descriptionGUID='" + descriptionGUID + '\'' +
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
        OpenMetadataClassificationDef that = (OpenMetadataClassificationDef) objectToCompare;
        return propagatable == that.propagatable &&
                       Objects.equals(validEntityDefs, that.validEntityDefs);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), validEntityDefs, propagatable);
    }
}
