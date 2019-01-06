/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CommunityProfileElementHeader provides a common base for all instance information from the access service.
 * It implements Serializable and provides common attributes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CommonHeader.class, name = "CommonHeader"),
        @JsonSubTypes.Type(value = Classification.class, name = "Classification")
})
public abstract class CommunityProfileElementHeader implements Serializable
{
    private static final long serialVersionUID = 1L;

    private String typeName        = null;
    private String typeDescription = null;
    private String originId        = null;
    private String originName      = null;
    private String originType      = null;
    private String originLicense   = null;


    /**
     * Default Constructor sets the properties to nulls
     */
    public CommunityProfileElementHeader()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template object to copy
     */
    public CommunityProfileElementHeader(CommunityProfileElementHeader template)
    {
        if (template != null)
        {
            this.typeName = template.getTypeName();
            this.typeDescription = template.getTypeDescription();
            this.originId = template.getOriginId();
            this.originName = template.getOriginName();
            this.originType = template.getOriginType();
            this.originLicense = template.getOriginLicense();
        }
    }


    /**
     * Return the name for this AssetCollectionMember's type.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Set up the name for this AssetCollectionMember's type.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Return the description for this AssetCollectionMember's type.
     *
     * @return string description
     */
    public String getTypeDescription()
    {
        return typeDescription;
    }


    /**
     * Set up the description for this AssetCollectionMember's type.
     *
     * @param typeDescription string description
     */
    public void setTypeDescription(String typeDescription)
    {
        this.typeDescription = typeDescription;
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommunityProfileElementHeader{" +
                "typeName='" + typeName + '\'' +
                ", typeDescription='" + typeDescription + '\'' +
                ", originId='" + originId + '\'' +
                ", originName='" + originName + '\'' +
                ", originType='" + originType + '\'' +
                ", originLicense='" + originLicense + '\'' +
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
        CommunityProfileElementHeader that = (CommunityProfileElementHeader) objectToCompare;
        return Objects.equals(getTypeName(), that.getTypeName()) &&
                Objects.equals(getTypeDescription(), that.getTypeDescription()) &&
                Objects.equals(getOriginId(), that.getOriginId()) &&
                Objects.equals(getOriginName(), that.getOriginName()) &&
                Objects.equals(getOriginType(), that.getOriginType()) &&
                Objects.equals(getOriginLicense(), that.getOriginLicense());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getTypeName(), getTypeDescription(), getOriginId(), getOriginName(), getOriginType(),
                            getOriginLicense());
    }
}
