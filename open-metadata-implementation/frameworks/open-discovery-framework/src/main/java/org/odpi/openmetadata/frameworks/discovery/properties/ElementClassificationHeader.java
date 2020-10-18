/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ClassificationOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SecurityTags;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementClassificationHeader bean describes the header for a classification associated with an element.
 * The ElementClassification is a generic classification.  Then classes such as SecurityTags are specifically featured
 * classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ElementClassification.class, name = "ElementClassification"),
                @JsonSubTypes.Type(value = SecurityTags.class, name = "SecurityTags")
        })
public class ElementClassificationHeader extends ElementControlHeader
{
    private static final long    serialVersionUID = 1L;

    private String               classificationTypeId          = null;
    private long                 classificationTypeVersion     = 0;
    private String               classificationTypeDescription = null;

    private ClassificationOrigin classificationOrigin          = ClassificationOrigin.ASSIGNED;
    private String               classificationOriginGUID      = null;


    /**
     * Default constructor
     */
    public ElementClassificationHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template template to copy
     */
    public ElementClassificationHeader(ElementClassificationHeader template)
    {
        super(template);

        if (template != null)
        {
            classificationTypeId          = template.getClassificationTypeId();
            classificationTypeVersion     = template.getClassificationTypeVersion();
            classificationTypeDescription = template.getClassificationTypeDescription();

            classificationOrigin          = template.getClassificationOrigin();
            classificationOriginGUID      = template.getClassificationOriginGUID();
        }
    }


    /**
     * Set up the unique identifier for the element's type.
     *
     * @param classificationTypeId String identifier
     */
    public void setClassificationTypeId(String classificationTypeId)
    {
        this.classificationTypeId = classificationTypeId;
    }


    /**
     * Return unique identifier for the element's type.
     *
     * @return element type id
     */
    public String getClassificationTypeId()
    {
        return classificationTypeId;
    }


    /**
     * Set up the version number for this element's type
     *
     * @param classificationTypeVersion version number for the element type.
     */
    public void setClassificationTypeVersion(long classificationTypeVersion)
    {
        this.classificationTypeVersion = classificationTypeVersion;
    }


    /**
     * Return the version number for this element's type.
     *
     * @return elementTypeVersion version number for the element type.
     */
    public long getClassificationTypeVersion()
    {
        return classificationTypeVersion;
    }


    /**
     * Set up a short description of this element's type.
     *
     * @param classificationTypeDescription set up the description for this element's type
     */
    public void setClassificationTypeDescription(String classificationTypeDescription)
    {
        this.classificationTypeDescription = classificationTypeDescription;
    }


    /**
     * Return the description for this element's type.
     *
     * @return elementTypeDescription String description for the element type
     */
    public String getClassificationTypeDescription()
    {
        return classificationTypeDescription;
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementClassificationHeader{" +
                "elementTypeId='" + classificationTypeId + '\'' +
                ", elementTypeVersion=" + classificationTypeVersion +
                ", elementTypeDescription='" + classificationTypeDescription + '\'' +
                ", classificationOrigin=" + classificationOrigin +
                ", classificationOriginGUID='" + classificationOriginGUID + '\'' +
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
        ElementClassificationHeader that = (ElementClassificationHeader) objectToCompare;
        return classificationTypeVersion == that.classificationTypeVersion &&
                Objects.equals(classificationTypeId, that.classificationTypeId) &&
                Objects.equals(classificationTypeDescription, that.classificationTypeDescription) &&
                classificationOrigin == that.classificationOrigin &&
                Objects.equals(classificationOriginGUID, that.classificationOriginGUID);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), classificationTypeId, classificationTypeVersion, classificationTypeDescription, classificationOrigin,
                            classificationOriginGUID);
    }
}
