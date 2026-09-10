/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OwnershipProperties describes the properties that identifies the owner of an attached element (or the resource it represents).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OwnershipProperties extends ClassificationBeanProperties
{
    private String       owner             = null;
    private String       ownerTypeName     = null;
    private String       ownerPropertyName = null;
    private List<String> userIds           = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public OwnershipProperties()
    {
        super();
        super.typeName = OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.  Retrieve values from the supplied template
     *
     * @param template element to copy
     */
    public OwnershipProperties(OwnershipProperties template)
    {
        super(template);
        if (template != null)
        {
            owner                = template.getOwner();
            ownerTypeName        = template.getOwnerTypeName();
            ownerPropertyName    = template.getOwnerPropertyName();
            userIds              = template.getUserIds();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Returns the name of the owner for this asset.
     *
     * @return owner String
     */
    public String getOwner() {
        return owner;
    }


    /**
     * Set up the name of the owner for this asset.
     *
     * @param owner String name
     */
    public void setOwner(String owner)
    {
        this.owner = owner;
    }


    /**
     * Return the type name of the element representing the owner.
     *
     * @return name
     */
    public String getOwnerTypeName()
    {
        return ownerTypeName;
    }


    /**
     * Set up  the type name of the element representing the owner.
     *
     * @param ownerTypeName name
     */
    public void setOwnerTypeName(String ownerTypeName)
    {
        this.ownerTypeName = ownerTypeName;
    }


    /**
     * Return the property name of the identifier that is representing the owner.
     *
     * @return name
     */
    public String getOwnerPropertyName()
    {
        return ownerPropertyName;
    }


    /**
     * Set up the property name of the identifier that is representing the owner.
     *
     * @param ownerPropertyName name
     */
    public void setOwnerPropertyName(String ownerPropertyName)
    {
        this.ownerPropertyName = ownerPropertyName;
    }


    /**
     * Return the list of user ids that are owners of this element.
     *
     * @return list of userIds
     */
    public List<String> getUserIds()
    {
        return userIds;
    }


    /**
     * Set up the list of user ids that are owners of this element.
     *
     * @param userIds list of userIds
     */
    public void setUserIds(List<String> userIds)
    {
        this.userIds = userIds;
    }


    /**
     * Return any additional properties describing the owner - for example the name and type of the owner as known
     * to the source of the ownership.
     *
     * @return map of property name to value
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Set up any additional properties describing the owner.
     *
     * @param additionalProperties map of property name to value
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OwnershipProperties{" +
                "owner='" + owner + '\'' +
                ", ownerTypeName='" + ownerTypeName + '\'' +
                ", ownerPropertyName='" + ownerPropertyName + '\'' +
                ", userIds=" + userIds +
                ", additionalProperties=" + additionalProperties +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof OwnershipProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(owner, that.owner) &&
                Objects.equals(ownerTypeName, that.ownerTypeName) &&
                Objects.equals(ownerPropertyName, that.ownerPropertyName) &&
                Objects.equals(userIds, that.userIds) &&
                Objects.equals(additionalProperties, that.additionalProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), owner, ownerTypeName, ownerPropertyName, userIds, additionalProperties);
    }
}
