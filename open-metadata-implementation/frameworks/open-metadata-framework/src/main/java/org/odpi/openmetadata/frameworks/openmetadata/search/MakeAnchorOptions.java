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
 * MakeAnchorOptions provides a structure for new anchor properties when linking an element to a new anchor.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MakeAnchorOptions extends MetadataSourceOptions
{
    private boolean makeAnchor      = false;
    private String  anchorScopeGUID = null;



    /**
     * Default constructor
     */
    public MakeAnchorOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MakeAnchorOptions(MakeAnchorOptions template)
    {
        super(template);

        if (template != null)
        {
            makeAnchor      = template.getMakeAnchor();
            anchorScopeGUID = template.getAnchorScopeGUID();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MakeAnchorOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Return whether this element should be classified as its own anchor or not.  The default is false.
     *
     * @return boolean
     */
    public boolean getMakeAnchor()
    {
        return makeAnchor;
    }


    /**
     * Set up whether this element should be classified as its own anchor or not.  The default is false.
     *
     * @param ownAnchor boolean
     */
    public void setMakeAnchor(boolean ownAnchor)
    {
        makeAnchor = ownAnchor;
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
        return "MakeAnchorOptions{" +
                ", makeAnchor=" + makeAnchor +
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
        if (! (objectToCompare instanceof MakeAnchorOptions that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return makeAnchor == that.makeAnchor &&
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
        return Objects.hash(super.hashCode(), makeAnchor, anchorScopeGUID);
    }
}
