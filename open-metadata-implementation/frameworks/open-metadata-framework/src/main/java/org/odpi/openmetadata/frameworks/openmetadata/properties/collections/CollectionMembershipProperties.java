/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.collections;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CollectionMemberStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CollectionMember describes a member of a collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CollectionMembershipProperties extends RelationshipBeanProperties
{
    private String                 membershipRationale = null;
    private String                 membershipType      = null;
    private String                 expression          = null;
    private int                    confidence          = 0;
    private CollectionMemberStatus membershipStatus    = null;
    private String                 userDefinedStatus   = null;
    private String                 steward             = null;
    private String                 stewardTypeName     = null;
    private String                 stewardPropertyName = null;
    private String                 source              = null;
    private String                 notes               = null;



    /**
     * Default constructor
     */
    public CollectionMembershipProperties()
    {
        super();
        super.typeName = OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public CollectionMembershipProperties(CollectionMembershipProperties template)
    {
        if (template != null)
        {
            membershipRationale = template.getMembershipRationale();
            membershipType      = template.getMembershipType();
            confidence          = template.getConfidence();
            expression          = template.getExpression();
            membershipStatus    = template.getMembershipStatus();
            userDefinedStatus   = template.getUserDefinedStatus();
            source              = template.getSource();
            steward             = template.getSteward();
            stewardTypeName     = template.getStewardTypeName();
            stewardPropertyName = template.getStewardPropertyName();
            notes               = template.getNotes();
        }
    }


    /**
     * Return the rationale or role of the asset in this collection.
     *
     * @return text
     */
    public String getMembershipRationale()
    {
        return membershipRationale;
    }


    /**
     * Set up the rationale or role of the asset in this collection.
     *
     * @param membershipRationale text
     */
    public void setMembershipRationale(String membershipRationale)
    {
        this.membershipRationale = membershipRationale;
    }


    /**
     * Return the type of membership.
     *
     * @return string
     */
    public String getMembershipType()
    {
        return membershipType;
    }


    /**
     * Set up the type of membership
     *
     * @param membershipType string
     */
    public void setMembershipType(String membershipType)
    {
        this.membershipType = membershipType;
    }


    /**
     * Return the expression used to determine the membership.  This is used by automated processes that are determining
     * membership through one or more matching algorithms.  This string helps the steward understand the type of match made.
     *
     * @return string
     */
    public String getExpression()
    {
        return expression;
    }

    /**
     * Set up the expression used to determine the membership.  This is used by automated processes that are determining
     * membership through one or more matching algorithms.  This string helps the steward understand the type of match made.
     *
     * @param expression string
     */
    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    /**
     * Return the confidence level (0-100) that the matching is correct.
     *
     * @return int
     */
    public int getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the confidence level (0-100) that the matching is correct.
     *
     * @param confidence int
     */
    public void setConfidence(int confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Return the status of the membership in the collection.
     *
     * @return enum
     */
    public CollectionMemberStatus getMembershipStatus()
    {
        return membershipStatus;
    }


    /**
     * Set up the status of the membership in the collection.
     *
     * @param membershipStatus enum
     */
    public void setMembershipStatus(CollectionMemberStatus membershipStatus)
    {
        this.membershipStatus = membershipStatus;
    }


    /**
     * Return the status of the membership in the collection.  This status is controlled by the local deployment.
     *
     * @return string
     */
    public String getUserDefinedStatus()
    {
        return userDefinedStatus;
    }


    /**
     * Set up the status of the membership in the collection.  This status is controlled by the local deployment.
     *
     * @param userDefinedStatus string
     */
    public void setUserDefinedStatus(String userDefinedStatus)
    {
        this.userDefinedStatus = userDefinedStatus;
    }


    /**
     * Return the source of information that determined the membership.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of information that determined the membership.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Returns the id of the steward responsible for the mapping.
     *
     * @return String id
     */
    public String getSteward()
    {
        return steward;
    }


    /**
     * Set up the id of the steward responsible for the mapping.
     *
     * @param steward String id
     */
    public void setSteward(String steward)
    {
        this.steward = steward;
    }


    /**
     * Return the type of element that describes the steward.
     *
     * @return type name
     */
    public String getStewardTypeName()
    {
        return stewardTypeName;
    }


    /**
     * Set up the type of element that describes the steward.
     *
     * @param stewardTypeName type name
     */
    public void setStewardTypeName(String stewardTypeName)
    {
        this.stewardTypeName = stewardTypeName;
    }


    /**
     * Return the name of the property that holds the steward's identifier.
     *
     * @return property name
     */
    public String getStewardPropertyName()
    {
        return stewardPropertyName;
    }


    /**
     * Set up the name of the property that holds the steward's identifier.
     *
     * @param stewardPropertyName property name
     */
    public void setStewardPropertyName(String stewardPropertyName)
    {
        this.stewardPropertyName = stewardPropertyName;
    }


    /**
     * Return the additional values associated with the symbolic name.
     *
     * @return string text
     */
    public String getNotes()
    {
        return notes;
    }


    /**
     * Set up the additional values associated with the symbolic name.
     *
     * @param notes string text
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "CollectionMembershipProperties{" +
                "membershipRationale='" + membershipRationale + '\'' +
                ", membershipType='" + membershipType + '\'' +
                ", expression='" + expression + '\'' +
                ", confidence=" + confidence +
                ", membershipStatus=" + membershipStatus +
                ", userDefinedStatus='" + userDefinedStatus + '\'' +
                ", steward='" + steward + '\'' +
                ", stewardTypeName='" + stewardTypeName + '\'' +
                ", stewardPropertyName='" + stewardPropertyName + '\'' +
                ", source='" + source + '\'' +
                ", notes='" + notes + '\'' +
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
        CollectionMembershipProperties that = (CollectionMembershipProperties) objectToCompare;
        return confidence == that.confidence &&
                Objects.equals(membershipRationale, that.membershipRationale) &&
                Objects.equals(membershipType, that.membershipType) &&
                Objects.equals(expression, that.expression) &&
                membershipStatus == that.membershipStatus &&
                Objects.equals(userDefinedStatus, that.userDefinedStatus) &&
                Objects.equals(steward, that.steward) &&
                Objects.equals(stewardTypeName, that.stewardTypeName) &&
                Objects.equals(stewardPropertyName, that.stewardPropertyName) &&
                Objects.equals(source, that.source) &&
                Objects.equals(notes, that.notes);
    }

    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(membershipRationale, membershipType, expression, confidence, membershipStatus, userDefinedStatus, steward, stewardTypeName,
                            stewardPropertyName, source, notes);
    }
}
