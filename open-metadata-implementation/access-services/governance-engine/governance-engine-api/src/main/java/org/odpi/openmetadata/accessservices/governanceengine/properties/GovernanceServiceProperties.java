/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceServiceProperties contains the definition of a governance service.
 * This definition can be associated with multiple governance engines.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = RegisteredGovernanceService.class, name = "RegisteredGovernanceService")
        })

public class GovernanceServiceProperties extends ReferenceableProperties
{
    private String              displayName                  = null;
    private String              description                  = null;
    private String              owner                        = null;
    private String              ownerTypeName                = null;
    private String              ownerPropertyName            = null;
    private List<String>        zoneMembership               = null;
    private String              originOrganizationGUID       = null;
    private String              originBusinessCapabilityGUID = null;
    private Map<String, String> otherOriginValues            = null;

    private Connection          connection                   = null;


    /**
     * Default constructor
     */
    public GovernanceServiceProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceServiceProperties(GovernanceServiceProperties  template)
    {
        super(template);

        if (template != null)
        {
            displayName                  = template.getDisplayName();
            description                  = template.getDescription();
            owner                        = template.getOwner();
            ownerTypeName                = template.getOwnerTypeName();
            ownerPropertyName            = template.getOwnerPropertyName();
            zoneMembership               = template.getZoneMembership();
            originOrganizationGUID       = template.getOriginOrganizationGUID();
            originBusinessCapabilityGUID = template.getOriginBusinessCapabilityGUID();
            otherOriginValues            = template.getOtherOriginValues();
            connection                   = template.getConnection();
        }
    }


    /**
     * Returns the stored display name property for the asset.
     * If no display name is available then null is returned.
     *
     * @return String name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the stored display name property for the asset.
     *
     * @param displayName String name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Returns the stored description property for the asset.
     * If no description is provided then null is returned.
     *
     * @return description String text
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the stored description property associated with the asset.
     *
     * @param description String text
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Returns the name of the owner for this asset.
     *
     * @return owner String
     */
    public String getOwner()
    {
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
     * Return the type of owner stored in the owner property.
     *
     * @return String name
     */
    public String getOwnerTypeName()
    {
        return ownerTypeName;
    }


    /**
     * Set up the type of owner stored in the owner property.
     *
     * @param ownerTypeName String name
     */
    public void setOwnerTypeName(String ownerTypeName)
    {
        this.ownerTypeName = ownerTypeName;
    }


    /**
     * Return the property name used to identifier the owner.
     *
     * @return String name
     */
    public String getOwnerPropertyName()
    {
        return ownerPropertyName;
    }


    /**
     * Set up the property name used to identifier the owner.
     *
     * @param ownerPropertyName String name
     */
    public void setOwnerPropertyName(String ownerPropertyName)
    {
        this.ownerPropertyName = ownerPropertyName;
    }


    /**
     * Return the names of the zones that this governance service is a member of.
     *
     * @return list of zone names
     */
    public List<String> getZoneMembership()
    {
        if (zoneMembership == null)
        {
            return null;
        }
        else if (zoneMembership.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(zoneMembership);
        }
    }


    /**
     * Set up the names of the zones that this governance service is a member of.
     *
     * @param zoneMembership list of zone names
     */
    public void setZoneMembership(List<String> zoneMembership)
    {
        this.zoneMembership = zoneMembership;
    }


    /**
     * Return the unique identifier for the organization that originated this governance service.
     *
     * @return string guid
     */
    public String getOriginOrganizationGUID()
    {
        return originOrganizationGUID;
    }


    /**
     * Set up the unique identifier for the organization that originated this governance service.
     *
     * @param originOrganizationGUID string guid
     */
    public void setOriginOrganizationGUID(String originOrganizationGUID)
    {
        this.originOrganizationGUID = originOrganizationGUID;
    }


    /**
     * Return the unique identifier of the business capability that originated this governance service.
     *
     * @return string guid
     */
    public String getOriginBusinessCapabilityGUID()
    {
        return originBusinessCapabilityGUID;
    }


    /**
     * Set up the unique identifier of the business capability that originated this governance service.
     *
     * @param originBusinessCapabilityGUID string guid
     */
    public void setOriginBusinessCapabilityGUID(String originBusinessCapabilityGUID)
    {
        this.originBusinessCapabilityGUID = originBusinessCapabilityGUID;
    }


    /**
     * Return the properties that characterize where this governance service is from.
     *
     * @return map of name value pairs, all strings
     */
    public Map<String, String> getOtherOriginValues()
    {
        if (otherOriginValues == null)
        {
            return null;
        }
        else if (otherOriginValues.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(otherOriginValues);
        }
    }


    /**
     * Set up the properties that characterize where this governance service is from.
     *
     * @param otherOriginValues map of name value pairs, all strings
     */
    public void setOtherOriginValues(Map<String, String> otherOriginValues)
    {
        this.otherOriginValues = otherOriginValues;
    }


    /**
     * Return the connection used to create a instance of this governance service.
     *
     * @return Connection object
     */
    public Connection getConnection()
    {
        return connection;
    }


    /**
     * Set up the connection used to create a instance of this governance service.
     *
     * @param connection connection object
     */
    public void setConnection(Connection connection)
    {
        this.connection = connection;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernanceServiceProperties{" +
                       "qualifiedName='" + getQualifiedName() + '\'' +
                       ", displayName='" + displayName + '\'' +
                       ", description='" + description + '\'' +
                       ", owner='" + owner + '\'' +
                       ", ownerTypeName='" + ownerTypeName + '\'' +
                       ", ownerPropertyName='" + ownerPropertyName + '\'' +
                       ", zoneMembership=" + zoneMembership +
                       ", originOrganizationGUID='" + originOrganizationGUID + '\'' +
                       ", originBusinessCapabilityGUID='" + originBusinessCapabilityGUID + '\'' +
                       ", otherOriginValues=" + otherOriginValues +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", connection=" + connection +
                       '}';
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernanceServiceProperties that = (GovernanceServiceProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(owner, that.owner) &&
                       Objects.equals(ownerTypeName, that.ownerTypeName) &&
                       Objects.equals(ownerPropertyName, that.ownerPropertyName) &&
                       Objects.equals(zoneMembership, that.zoneMembership) &&
                       Objects.equals(originOrganizationGUID, that.originOrganizationGUID) &&
                       Objects.equals(originBusinessCapabilityGUID, that.originBusinessCapabilityGUID) &&
                       Objects.equals(otherOriginValues, that.otherOriginValues) &&
                       Objects.equals(connection, that.connection);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description, owner, ownerTypeName, ownerPropertyName, zoneMembership, originOrganizationGUID,
                            originBusinessCapabilityGUID, otherOriginValues, connection);
    }
}
