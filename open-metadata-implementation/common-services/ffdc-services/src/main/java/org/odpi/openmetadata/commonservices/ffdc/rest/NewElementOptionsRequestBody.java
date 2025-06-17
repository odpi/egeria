/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewElementOptionsRequestBody provides a structure for the common properties when creating an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewElementOptionsRequestBody extends MetadataSourceRequestBody
{
    private String            anchorGUID                   = null;
    private boolean           isOwnAnchor                  = false;
    private String            anchorScopeGUID              = null;
    private String            parentGUID                   = null;
    private String            parentRelationshipTypeName   = null;
    private ElementProperties parentRelationshipProperties = null;
    private boolean           parentAtEnd1                 = true;


    /**
     * Default constructor
     */
    public NewElementOptionsRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewElementOptionsRequestBody(NewElementOptionsRequestBody template)
    {
        super(template);

        if (template != null)
        {
            anchorGUID = template.getAnchorGUID();
            isOwnAnchor = template.getIsOwnAnchor();
            anchorScopeGUID = template.getAnchorScopeGUID();
            parentGUID = template.getParentGUID();
            parentRelationshipTypeName = template.getParentRelationshipTypeName();
            parentRelationshipProperties = template.getParentRelationshipProperties();
            parentAtEnd1 = template.getParentAtEnd1();
        }
    }


    /**
     * Return the unique identifier of the element that should be the anchor for the new element. It is set to null if no anchor,
     * or the Anchors classification is included in the initial classifications.
     *
     * @return string guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the element that should be the anchor for the new element. Set to null if no anchor,
     * or the new collection should be its own anchor.
     *
     * @param anchorGUID string guid
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return whether this element should be classified as its own anchor or not.  The default is false.
     *
     * @return boolean
     */
    public boolean getIsOwnAnchor()
    {
        return isOwnAnchor;
    }


    /**
     * Set up whether this element should be classified as its own anchor or not.  The default is false.
     *
     * @param ownAnchor boolean
     */
    public void setIsOwnAnchor(boolean ownAnchor)
    {
        isOwnAnchor = ownAnchor;
    }


    /**
     * Return the unique identifier of the anchor's scope.
     * If this is not supplied, the value set in the anchor entity's Anchors classification is used.
     *
     * @return string guid
     */
    public String getAnchorScopeGUID()
    {
        return anchorScopeGUID;
    }


    /**
     * Set up the unique identifier of the anchor's scope.
     * If this is not supplied, the value set in the anchor entity's Anchors classification is used.
     *
     * @param anchorScopeGUID string guid
     */
    public void setAnchorScopeGUID(String anchorScopeGUID)
    {
        this.anchorScopeGUID = anchorScopeGUID;
    }


    /**
     * Return the optional unique identifier for an element that should be connected to the newly created element.
     * If this property is specified, parentRelationshipTypeName must also be specified.
     *
     * @return string guid
     */
    public String getParentGUID()
    {
        return parentGUID;
    }


    /**
     * Set up the optional unique identifier for an element that should be connected to the newly created element.
     * If this property is specified, parentRelationshipTypeName must also be specified.
     *
     * @param parentGUID string guid
     */
    public void setParentGUID(String parentGUID)
    {
        this.parentGUID = parentGUID;
    }


    /**
     * Return the name of the relationship, if any, that should be established between the new element and the parent element.
     *
     * @return string type name
     */
    public String getParentRelationshipTypeName()
    {
        return parentRelationshipTypeName;
    }


    /**
     * Set up the name of the optional relationship from the newly created element to a parent element.
     *
     * @param parentRelationshipTypeName string type name
     */
    public void setParentRelationshipTypeName(String parentRelationshipTypeName)
    {
        this.parentRelationshipTypeName = parentRelationshipTypeName;
    }


    /**
     * Return any properties that should be included in the parent relationship.
     *
     * @return element properties
     */
    public ElementProperties getParentRelationshipProperties()
    {
        return parentRelationshipProperties;
    }


    /**
     * Set up any properties that should be included in the parent relationship.
     *
     * @param parentRelationshipProperties element properties
     */
    public void setParentRelationshipProperties(ElementProperties parentRelationshipProperties)
    {
        this.parentRelationshipProperties = parentRelationshipProperties;
    }


    /**
     * Return which end any parent entity sits on the relationship.
     *
     * @return boolean
     */
    public boolean getParentAtEnd1()
    {
        return parentAtEnd1;
    }


    /**
     * Set up  which end any parent entity sits on the relationship.
     *
     * @param parentAtEnd1 boolean
     */
    public void setParentAtEnd1(boolean parentAtEnd1)
    {
        this.parentAtEnd1 = parentAtEnd1;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewElementOptionsRequestBody{" +
                "anchorGUID='" + anchorGUID + '\'' +
                ", isOwnAnchor=" + isOwnAnchor +
                ", anchorScopeGUID='" + anchorScopeGUID + '\'' +
                ", parentGUID='" + parentGUID + '\'' +
                ", parentRelationshipTypeName='" + parentRelationshipTypeName + '\'' +
                ", parentRelationshipProperties=" + parentRelationshipProperties +
                ", parentAtEnd1=" + parentAtEnd1 +
                "} " + super.toString();
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
        if (! (objectToCompare instanceof NewElementOptionsRequestBody that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return parentAtEnd1 == that.parentAtEnd1 &&
                       isOwnAnchor == that.isOwnAnchor &&
                       Objects.equals(anchorGUID, that.anchorGUID) &&
                       Objects.equals(anchorScopeGUID, that.anchorScopeGUID) &&
                       Objects.equals(parentGUID, that.parentGUID) &&
                       Objects.equals(parentRelationshipTypeName, that.parentRelationshipTypeName) &&
                       Objects.equals(parentRelationshipProperties, that.parentRelationshipProperties);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), anchorGUID, isOwnAnchor, anchorScopeGUID, parentGUID,
                            parentRelationshipTypeName, parentRelationshipProperties, parentAtEnd1);
    }
}
