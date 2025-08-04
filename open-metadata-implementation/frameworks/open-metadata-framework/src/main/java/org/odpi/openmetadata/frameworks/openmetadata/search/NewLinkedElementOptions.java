/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewLinkedElementOptions provides a structure for a new element that may link to a parent element when creating an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class NewLinkedElementOptions extends AnchorOptions
{
    private String                 openMetadataTypeName         = null;
    private String                 parentGUID                   = null;
    private String                 parentRelationshipTypeName   = null;
    private boolean                parentAtEnd1                 = true;


    /**
     * Default constructor
     */
    public NewLinkedElementOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewLinkedElementOptions(NewLinkedElementOptions template)
    {
        super(template);

        if (template != null)
        {
            openMetadataTypeName = template.getOpenMetadataTypeName();
            parentGUID = template.getParentGUID();
            parentRelationshipTypeName = template.getParentRelationshipTypeName();
            parentAtEnd1 = template.getParentAtEnd1();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewLinkedElementOptions(AnchorOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewLinkedElementOptions(MetadataSourceOptions template)
    {
        super(template);
    }



    /**
     * Return the open metadata type name for the element to create.
     *
     * @return string name
     */
    public String getOpenMetadataTypeName()
    {
        return openMetadataTypeName;
    }


    /**
     * Set up the open metadata type name for the element to create.
     *
     * @param openMetadataTypeName string name
     */
    public void setOpenMetadataTypeName(String openMetadataTypeName)
    {
        this.openMetadataTypeName = openMetadataTypeName;
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
        return "NewLinkedElementOptions{" +
                "openMetadataTypeName='" + openMetadataTypeName + '\'' +
                ", parentGUID='" + parentGUID + '\'' +
                ", parentRelationshipTypeName='" + parentRelationshipTypeName + '\'' +
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
        if (! (objectToCompare instanceof NewLinkedElementOptions that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return parentAtEnd1 == that.parentAtEnd1 &&
                Objects.equals(openMetadataTypeName, that.openMetadataTypeName) &&
                Objects.equals(parentGUID, that.parentGUID) &&
                Objects.equals(parentRelationshipTypeName, that.parentRelationshipTypeName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), openMetadataTypeName, parentGUID, parentRelationshipTypeName, parentAtEnd1);
    }
}
