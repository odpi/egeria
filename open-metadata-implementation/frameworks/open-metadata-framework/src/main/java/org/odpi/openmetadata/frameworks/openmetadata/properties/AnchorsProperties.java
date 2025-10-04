/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the properties for the Anchors classification
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnchorsProperties extends ClassificationBeanProperties
{
    private String anchorGUID       = null;
    private String anchorTypeName   = null;
    private String anchorDomainName = null;
    private String anchorScopeGUID  = null;


    /**
     * Default constructor
     */
    public AnchorsProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AnchorsProperties(AnchorsProperties template)
    {
        super(template);

        if (template != null)
        {
            this.anchorGUID       = template.getAnchorGUID();
            this.anchorTypeName   = template.getAnchorTypeName();
            this.anchorDomainName = template.getAnchorDomainName();
            this.anchorScopeGUID  = template.getAnchorScopeGUID();
        }
    }


    /**
     * Return the unique identifier of the anchor.
     *
     * @return string guid
     */
    public String getAnchorGUID()
    {
        return anchorGUID;
    }


    /**
     * Set up the unique identifier of the anchor.
     *
     * @param anchorGUID string guid
     */
    public void setAnchorGUID(String anchorGUID)
    {
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return the type name of the anchor.
     *
     * @return string name
     */
    public String getAnchorTypeName()
    {
        return anchorTypeName;
    }


    /**
     * Set up the type name of the anchor.
     *
     * @param anchorTypeName string name
     */
    public void setAnchorTypeName(String anchorTypeName)
    {
        this.anchorTypeName = anchorTypeName;
    }


    /**
     * Return the domain (type name) of the anchor.
     *
     * @return string name
     */
    public String getAnchorDomainName()
    {
        return anchorDomainName;
    }


    /**
     * Set up the domain (type name) of the anchor.
     *
     * @param anchorDomainName string name
     */
    public void setAnchorDomainName(String anchorDomainName)
    {
        this.anchorDomainName = anchorDomainName;
    }


    /**
     * Return the unique identifier of the anchor's scope.
     *
     * @return string guid
     */
    public String getAnchorScopeGUID()
    {
        return anchorScopeGUID;
    }


    /**
     * Set up the unique identifier of the anchor's scope.
     *
     * @param anchorScopeGUID string guid
     */
    public void setAnchorScopeGUID(String anchorScopeGUID)
    {
        this.anchorScopeGUID = anchorScopeGUID;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AnchorsProperties{" +
                "anchorGUID='" + anchorGUID + '\'' +
                ", anchorTypeName='" + anchorTypeName + '\'' +
                ", anchorDomainName='" + anchorDomainName + '\'' +
                ", anchorScopeGUID='" + anchorScopeGUID + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        AnchorsProperties that = (AnchorsProperties) objectToCompare;
        return Objects.equals(anchorGUID, that.anchorGUID) &&
                Objects.equals(anchorTypeName, that.anchorTypeName) &&
                Objects.equals(anchorDomainName, that.anchorDomainName) &&
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
        return Objects.hash(super.hashCode(), anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID);
    }
}
