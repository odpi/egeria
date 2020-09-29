/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Classification bean extends the Classification from the OCF properties package with a default constructor and
 * setter methods.  This means it can be used for REST calls and other JSON based functions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Classification extends ElementControlHeader
{
    private static final long    serialVersionUID = 1L;

    private String               elementTypeId                     = null;
    private long                 elementTypeVersion                = 0;
    private String               elementTypeDescription            = null;

    private ClassificationOrigin classificationOrigin              = ClassificationOrigin.ASSIGNED;
    private String               classificationOriginGUID          = null;

    private String               classificationName                = null;
    private Map<String, Object>  classificationProperties          = null;


    /**
     * Default constructor
     */
    public Classification()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public Classification(Classification    template)
    {
        super(template);

        if (template != null)
        {
            elementTypeId = template.getElementTypeId();
            elementTypeVersion = template.getElementTypeVersion();
            elementTypeDescription = template.getElementTypeDescription();

            classificationOrigin = template.getClassificationOrigin();
            classificationOriginGUID = template.getClassificationOriginGUID();

            classificationName = template.getClassificationName();
            classificationProperties = template.getClassificationProperties();
        }
    }


    /**
     * Set up the unique identifier for the element's type.
     *
     * @param elementTypeId String identifier
     */
    public void setElementTypeId(String elementTypeId)
    {
        this.elementTypeId = elementTypeId;
    }


    /**
     * Return unique identifier for the element's type.
     *
     * @return element type id
     */
    public String getElementTypeId()
    {
        return elementTypeId;
    }


    /**
     * Set up the version number for this element's type
     *
     * @param elementTypeVersion version number for the element type.
     */
    public void setElementTypeVersion(long elementTypeVersion)
    {
        this.elementTypeVersion = elementTypeVersion;
    }


    /**
     * Return the version number for this element's type.
     *
     * @return elementTypeVersion version number for the element type.
     */
    public long getElementTypeVersion()
    {
        return elementTypeVersion;
    }


    /**
     * Set up a short description of this element's type.
     *
     * @param elementTypeDescription set up the description for this element's type
     */
    public void setElementTypeDescription(String elementTypeDescription)
    {
        this.elementTypeDescription = elementTypeDescription;
    }


    /**
     * Return the description for this element's type.
     *
     * @return elementTypeDescription String description for the element type
     */
    public String getElementTypeDescription()
    {
        return elementTypeDescription;
    }


    /**
     * Return whether the classification was added explicitly to this asset or propagated from another entity.
     *
     * @return classification origin enum
     */
    public ClassificationOrigin getClassificationOrigin()
    {
        return classificationOrigin;
    }


    /**
     * Set up the setting for classification origin
     *
     * @param classificationOrigin enum
     */
    public void setClassificationOrigin(ClassificationOrigin classificationOrigin)
    {
        this.classificationOrigin = classificationOrigin;
    }


    /**
     * Return the entity guid that the classification was propagated from (or null if not propagated).
     *
     * @return string guid
     */
    public String getClassificationOriginGUID()
    {
        return classificationOriginGUID;
    }


    /**
     * Set up the origin of the classification.
     *
     * @param classificationOriginGUID string guid
     */
    public void setClassificationOriginGUID(String classificationOriginGUID)
    {
        this.classificationOriginGUID = classificationOriginGUID;
    }


    /**
     * Set up the name of the classification.  This name is the type name defined in a ClassificationDef type definition.
     *
     * @param classificationName  name of classification
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * Return the name of the classification
     *
     * @return String name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @param classificationProperties  properties for the classification
     */
    public void setClassificationProperties(Map<String, Object> classificationProperties)
    {
        this.classificationProperties = classificationProperties;
    }


    /**
     * Return a collection of the additional stored properties for the classification.
     * If no stored properties are present then null is returned.
     *
     * @return properties map
     */
    public Map<String, Object> getClassificationProperties()
    {
        if (classificationProperties == null)
        {
            return null;
        }
        else if (classificationProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(classificationProperties);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Classification{" +
                "elementTypeId='" + elementTypeId + '\'' +
                ", elementTypeVersion=" + elementTypeVersion +
                ", elementTypeDescription='" + elementTypeDescription + '\'' +
                ", classificationOrigin=" + classificationOrigin +
                ", classificationOriginGUID='" + classificationOriginGUID + '\'' +
                ", classificationName='" + classificationName + '\'' +
                ", classificationProperties=" + classificationProperties +
                ", elementSourceServer='" + getElementSourceServer() + '\'' +
                ", elementOrigin=" + getElementOrigin() +
                ", elementMetadataCollectionId='" + getElementMetadataCollectionId() + '\'' +
                ", elementMetadataCollectionName='" + getElementMetadataCollectionName() + '\'' +
                ", elementLicense='" + getElementLicense() + '\'' +
                ", status=" + getStatus() +
                ", elementCreatedBy='" + getElementCreatedBy() + '\'' +
                ", elementUpdatedBy='" + getElementUpdatedBy() + '\'' +
                ", elementMaintainedBy=" + getElementMaintainedBy() +
                ", elementCreateTime=" + getElementCreateTime() +
                ", elementUpdateTime=" + getElementUpdateTime() +
                ", elementVersion=" + getElementVersion() +
                ", mappingProperties=" + getMappingProperties() +
                ", headerVersion=" + getHeaderVersion() +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        Classification that = (Classification) objectToCompare;
        return elementTypeVersion == that.elementTypeVersion &&
                Objects.equals(elementTypeId, that.elementTypeId) &&
                Objects.equals(elementTypeDescription, that.elementTypeDescription) &&
                classificationOrigin == that.classificationOrigin &&
                Objects.equals(classificationOriginGUID, that.classificationOriginGUID) &&
                Objects.equals(classificationName, that.classificationName) &&
                Objects.equals(classificationProperties, that.classificationProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementTypeId, elementTypeVersion, elementTypeDescription, classificationOrigin,
                            classificationOriginGUID, classificationName, classificationProperties);
    }
}
