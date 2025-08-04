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
 * AnchorOptions provides a structure for the anchor properties when creating an element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnchorOptions extends MetadataSourceOptions
{
    private String  anchorGUID      = null;
    private boolean isOwnAnchor     = true;
    private String  anchorScopeGUID = null;



    /**
     * Default constructor
     */
    public AnchorOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnchorOptions(AnchorOptions template)
    {
        super(template);

        if (template != null)
        {
            anchorGUID = template.getAnchorGUID();
            isOwnAnchor = template.getIsOwnAnchor();
            anchorScopeGUID = template.getAnchorScopeGUID();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnchorOptions(MetadataSourceOptions template)
    {
        super(template);
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "AnchorOptions{" +
                "anchorGUID='" + anchorGUID + '\'' +
                ", isOwnAnchor=" + isOwnAnchor +
                ", anchorScopeGUID='" + anchorScopeGUID + '\'' +
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
        if (! (objectToCompare instanceof AnchorOptions that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return isOwnAnchor == that.isOwnAnchor &&
                       Objects.equals(anchorGUID, that.anchorGUID) &&
                       Objects.equals(anchorScopeGUID, that.anchorScopeGUID);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), anchorGUID, isOwnAnchor, anchorScopeGUID);
    }
}
