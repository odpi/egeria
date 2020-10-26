/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ElementType bean provides details of the type information associated with a metadata element.
 * It also includes the control information related the element's origin and how this element was created and maintained.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementType extends ElementControlHeader
{
    private static final long     serialVersionUID = 1L;

    private String       elementTypeId          = null;
    private String       elementTypeName        = null;
    private List<String> elementSuperTypeNames  = null;
    private long         elementTypeVersion     = 0;
    private String       elementTypeDescription = null;


    /**
     * Default constructor
     */
    public ElementType()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param templateType type to clone
     */
    public ElementType(ElementType templateType)
    {
        super(templateType);

        if (templateType != null)
        {
            elementTypeId = templateType.getElementTypeId();
            elementTypeName = templateType.getElementTypeName();
            elementSuperTypeNames = templateType.getElementSuperTypeNames();
            elementTypeVersion = templateType.getElementTypeVersion();
            elementTypeDescription = templateType.getElementTypeDescription();
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
     * Set up the name of this element's type
     *
     * @param elementTypeName String name
     */
    public void setElementTypeName(String elementTypeName)
    {
        this.elementTypeName = elementTypeName;
    }


    /**
     * Return name of element's type.
     *
     * @return elementTypeName
     */
    public String getElementTypeName()
    {
        return elementTypeName;
    }


    /**
     * Set up the list of type names for this type's supertypes.
     *
     * @param elementSuperTypeNames list of type names
     */
    public void setElementSuperTypeNames(List<String> elementSuperTypeNames)
    {
        this.elementSuperTypeNames = elementSuperTypeNames;
    }


    /**
     * Return the list of type names for this type's supertypes.
     *
     * @return list of type names
     */
    public List<String> getElementSuperTypeNames()
    {
        if (elementSuperTypeNames == null)
        {
            return null;
        }
        else if (elementSuperTypeNames.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(elementSuperTypeNames);
        }
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementType{" +
                "elementTypeId='" + elementTypeId + '\'' +
                ", elementTypeName='" + elementTypeName + '\'' +
                ", elementSuperTypeNames=" + elementSuperTypeNames +
                ", elementTypeVersion=" + elementTypeVersion +
                ", elementTypeDescription='" + elementTypeDescription + '\'' +
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
        ElementType that = (ElementType) objectToCompare;
        return elementTypeVersion == that.elementTypeVersion &&
                Objects.equals(elementTypeId, that.elementTypeId) &&
                Objects.equals(elementTypeName, that.elementTypeName) &&
                Objects.equals(elementSuperTypeNames, that.elementSuperTypeNames) &&
                Objects.equals(elementTypeDescription, that.elementTypeDescription);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementTypeId, elementTypeName, elementSuperTypeNames, elementTypeVersion, elementTypeDescription);
    }
}
