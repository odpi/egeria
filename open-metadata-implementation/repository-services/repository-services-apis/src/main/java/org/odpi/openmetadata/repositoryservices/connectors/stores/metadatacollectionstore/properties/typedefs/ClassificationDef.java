/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ClassificationDef stores the properties for the definition of a type of classification.  Many of the properties
 * are inherited from TypeDef.  ClassificationDef adds a list of Entity Types that this Classification can be
 * connected to and a boolean to indicate if this classification is propagatable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationDef extends TypeDef
{
    private static final long    serialVersionUID = 1L;

    private   List<TypeDefLink>     validEntityDefs = null;
    private   boolean               propagatable = false;


    /**
     * Minimal constructor sets up an empty ClassificationDef.
     */
    public ClassificationDef()
    {
        super(TypeDefCategory.CLASSIFICATION_DEF);
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     *
     * @param category    category of this TypeDef
     * @param guid        unique id for the TypeDef
     * @param name        unique name for the TypeDef
     * @param version     active version number for the TypeDef
     * @param versionName name for active version of the TypeDef
     */
    public ClassificationDef(TypeDefCategory category,
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
    public ClassificationDef(ClassificationDef   template)
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
     * @return subclass of TypeDef
     */
    public TypeDef cloneFromSubclass()
    {
        return new ClassificationDef(this);
    }


    /**
     * Return the list of identifiers for the types of entities that this type of Classification can be connected to.
     *
     * @return List of entity type identifiers
     */
    public List<TypeDefLink> getValidEntityDefs()
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
            List<TypeDefLink> resultList = new ArrayList<>();

            for (TypeDefLink  typeDefLink : validEntityDefs)
            {
                if (typeDefLink != null)
                {
                    resultList.add(new TypeDefLink(typeDefLink));
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
    public void setValidEntityDefs(List<TypeDefLink> validEntityDefs)
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
            List<TypeDefLink> resultList = new ArrayList<>();

            for (TypeDefLink  typeDefLink : validEntityDefs)
            {
                if (typeDefLink != null)
                {
                    resultList.add(new TypeDefLink(typeDefLink));
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
        return "ClassificationDef{" +
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
        ClassificationDef that = (ClassificationDef) objectToCompare;
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
