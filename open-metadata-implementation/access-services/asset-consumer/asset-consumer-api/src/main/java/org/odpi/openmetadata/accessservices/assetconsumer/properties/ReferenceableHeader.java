/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceableHeader provides the common properties found in objects that inherit from Referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Asset.class, name = "Asset"),
        @JsonSubTypes.Type(value = GlossaryTerm.class, name = "GlossaryTerm")
})
public abstract class ReferenceableHeader extends AssetConsumerElementHeader
{
    private String                            guid                 = null;
    private String                            typeName             = null;
    private String                            typeDescription      = null;
    private String                            originId             = null;
    private String                            originName           = null;
    private String                            originType           = null;
    private String                            originLicense        = null;
    private String                            qualifiedName        = null;
    private Map<String, Object>               additionalProperties = null;
    private List<ReferenceableClassification> classifications      = null;


    /**
     * Default constructor
     */
    public ReferenceableHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     */
    public ReferenceableHeader(ReferenceableHeader   template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGUID();
            this.typeName = template.getTypeName();
            this.typeDescription = template.getTypeDescription();
            this.originId = template.getOriginId();
            this.originName = template.getOriginName();
            this.originType = template.getOriginType();
            this.originLicense = template.getOriginLicense();
            this.qualifiedName = template.getQualifiedName();
            this.additionalProperties = template.getAdditionalProperties();
            this.classifications = template.getClassifications();
        }
    }


    /**
     * Return the unique identifier for this asset.
     *
     * @return string guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this asset.
     *
     * @param guid string guid for this asset
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the name for this Asset's type.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name for this Asset's type.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the description for this Asset's type.
     *
     * @return string description
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }


    /**
     * Return the unique identifier of this metadata element's origin (also known as the home metadata collection id).
     *
     * @return guid
     */
    public String getOriginId()
    {
        return originId;
    }


    /**
     * Set up the unique identifier of this metadata element's origin (also known as the home metadata collection id).
     *
     * @param originId guid
     */
    public void setOriginId(String originId)
    {
        this.originId = originId;
    }


    /**
     * Set up the name of the metadata element's origin.  This may be null.
     *
     * @return name
     */
    public String getOriginName()
    {
        return originName;
    }


    /**
     * Return the name of the metadata element's origin.  This may be null.
     *
     * @param originName name
     */
    public void setOriginName(String originName)
    {
        this.originName = originName;
    }


    /**
     * Return the type of the metadata element's origin.
     *
     * @return descriptive type name
     */
    public String getOriginType()
    {
        return originType;
    }


    /**
     * Set up the type of the metadata element's origin.
     *
     * @param originType descriptive type name
     */
    public void setOriginType(String originType)
    {
        this.originType = originType;
    }


    /**
     * Return the license for the metadata element set up by the origin.
     * Null means unrestricted.
     *
     * @return license string
     */
    public String getOriginLicense()
    {
        return originLicense;
    }


    /**
     * Set up the license for the metadata element set up by the origin.
     * Null means unrestricted.
     *
     * @param originLicense
     */
    public void setOriginLicense(String originLicense)
    {
        this.originLicense = originLicense;
    }


    /**
     * Set up the description for this Asset's type.
     *
     * @param typeDescription string description
     */
    public void setTypeDescription(String typeDescription)
    {
        this.typeDescription = typeDescription;
    }


    /**
     * Return the unique name for this asset.
     *
     * @return string name
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up the unique name for this asset.
     *
     * @param qualifiedName string name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Return any additional properties associated with the asset.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the asset.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return the list of active classifications for this asset.
     *
     * @return list of classification objects
     */
    public List<ReferenceableClassification> getClassifications()
    {
        if (classifications == null)
        {
            return null;
        }
        else if (classifications.isEmpty())
        {
            return null;
        }
        else
        {
            List<ReferenceableClassification>  clonedList = new ArrayList<>();

            for (ReferenceableClassification  existingElement : classifications)
            {
                clonedList.add(new ReferenceableClassification(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of active classifications for this asset.
     *
     * @param classifications list of classification objects
     */
    public void setClassifications(List<ReferenceableClassification> classifications)
    {
        this.classifications = classifications;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ReferenceableHeader{" +
                "guid='" + guid + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", classifications=" + classifications +
                ", GUID='" + getGUID() + '\'' +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ReferenceableHeader that = (ReferenceableHeader) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getTypeDescription(), that.getTypeDescription()) &&
                Objects.equals(getQualifiedName(), that.getQualifiedName()) &&
                Objects.equals(getAdditionalProperties(), that.getAdditionalProperties()) &&
                Objects.equals(getClassifications(), that.getClassifications());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, getTypeName(), getTypeDescription(), getQualifiedName(), getAdditionalProperties(),
                            getClassifications());
    }
}
