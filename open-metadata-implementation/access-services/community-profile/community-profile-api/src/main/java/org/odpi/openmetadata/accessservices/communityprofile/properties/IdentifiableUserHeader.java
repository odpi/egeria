/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * IdentifiableUserHeader describes an element that is linked to a single userId.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserIdentity.class, name = "UserIdentity"),
        @JsonSubTypes.Type(value = UserFeedbackHeader.class, name = "UserFeedbackHeader")
})
public abstract class IdentifiableUserHeader extends CommonHeader
{
    private static final long    serialVersionUID = 1L;

    private String          userId    = null;


    /**
     * Default constructor
     */
    public IdentifiableUserHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public IdentifiableUserHeader(IdentifiableUserHeader template)
    {
        super(template);

        if (template != null)
        {
            this.userId = template.getUserId();
        }
    }


    /**
     * Return the unique name of the user identity for the individual.
     *
     * @return userId string
     */
    public String getUserId()
    {
        return userId;
    }


    /**
     * Set up the unique name of the user identity for the individual.
     *
     * @param userId string
     */
    public void setUserId(String userId)
    {
        this.userId = userId;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "IdentifiableUserHeader{" +
                "userId='" + userId + '\'' +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
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
        IdentifiableUserHeader that = (IdentifiableUserHeader) objectToCompare;
        return Objects.equals(getUserId(), that.getUserId());
    }



    /**
     * Hash code for this object
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getUserId());
    }
}
