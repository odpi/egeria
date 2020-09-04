/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The CommonHeader provides the link to the guid and type of element extracted from the open metadata repositories.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = NoteEntryHeader.class, name = "NoteEntryHeader"),
        @JsonSubTypes.Type(value = Resource.class, name = "Resource"),
        @JsonSubTypes.Type(value = IdentifiableUserHeader.class, name = "IdentifiableUserHeader"),
        @JsonSubTypes.Type(value = ContactMethod.class, name = "ContactMethod"),
        @JsonSubTypes.Type(value = ExternalReference.class, name = "ExternalReference"),
        @JsonSubTypes.Type(value = ReferenceableHeader.class, name = "ReferenceableHeader")
})
public abstract class CommonHeader extends CommunityProfileElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String               guid                 = null;


    /**
     * Default constructor
     */
    public CommonHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CommonHeader(CommonHeader template)
    {
        super(template);

        if (template != null)
        {
            this.guid = template.getGUID();
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
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CommonHeader{" +
                "guid='" + guid + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                ", originId='" + getOriginId() + '\'' +
                ", originName='" + getOriginName() + '\'' +
                ", originType='" + getOriginType() + '\'' +
                ", originLicense='" + getOriginLicense() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        CommonHeader that = (CommonHeader) objectToCompare;
        return Objects.equals(guid, that.guid);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), guid);
    }
}
