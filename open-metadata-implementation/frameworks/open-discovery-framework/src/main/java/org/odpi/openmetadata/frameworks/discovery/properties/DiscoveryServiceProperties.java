/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * DiscoveryServiceProperties contains the definition of a discovery service.
 * This definition can be associated with multiple discovery engines.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = RegisteredDiscoveryService.class, name = "RegisteredDiscoveryService")
        })

public class DiscoveryServiceProperties extends PropertyBase
{
    private static final long    serialVersionUID = 1L;

    private String              qualifiedName                = null;
    private String              displayName                  = null;
    private String              description                  = null;
    private String              owner                        = null;
    private OwnerType           ownerType                    = null;
    private List<String>        zoneMembership               = null;
    private String              originOrganizationGUID       = null;
    private String              originBusinessCapabilityGUID = null;
    private Map<String, String> otherOriginValues            = null;
    private Map<String, String> additionalProperties         = null;

    private Connection          connection                   = null;


    /**
     * Default constructor
     */
    public DiscoveryServiceProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DiscoveryServiceProperties(DiscoveryServiceProperties  template)
    {
        super(template);

        if (template != null)
        {
            qualifiedName                = template.getQualifiedName();
            displayName                  = template.getDisplayName();
            description                  = template.getDescription();
            owner                        = template.getOwner();
            ownerType                    = template.getOwnerType();
            zoneMembership               = template.getZoneMembership();
            originOrganizationGUID       = template.getOriginOrganizationGUID();
            originBusinessCapabilityGUID = template.getOriginBusinessCapabilityGUID();
            otherOriginValues            = template.getOtherOriginValues();
            additionalProperties         = template.getAdditionalProperties();
            connection                   = template.getConnection();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
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
     * @return OwnerCategory enum
     */
    public OwnerType getOwnerType()
    {
        return ownerType;
    }


    /**
     * Set up the owner type for this asset.
     *
     * @param ownerType OwnerCategory enum
     */
    public void setOwnerType(OwnerType ownerType)
    {
        this.ownerType = ownerType;
    }


    /**
     * Return the names of the zones that this discovery service is a member of.
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
     * Set up the names of the zones that this discovery service is a member of.
     *
     * @param zoneMembership list of zone names
     */
    public void setZoneMembership(List<String> zoneMembership)
    {
        this.zoneMembership = zoneMembership;
    }


    /**
     * Return the unique identifier for the organization that originated this discovery service.
     *
     * @return string guid
     */
    public String getOriginOrganizationGUID()
    {
        return originOrganizationGUID;
    }


    /**
     * Set up the unique identifier for the organization that originated this discovery service.
     *
     * @param originOrganizationGUID string guid
     */
    public void setOriginOrganizationGUID(String originOrganizationGUID)
    {
        this.originOrganizationGUID = originOrganizationGUID;
    }


    /**
     * Return the unique identifier of the business capability that originated this discovery service.
     *
     * @return string guid
     */
    public String getOriginBusinessCapabilityGUID()
    {
        return originBusinessCapabilityGUID;
    }


    /**
     * Set up the unique identifier of the business capability that originated this discovery service.
     *
     * @param originBusinessCapabilityGUID string guid
     */
    public void setOriginBusinessCapabilityGUID(String originBusinessCapabilityGUID)
    {
        this.originBusinessCapabilityGUID = originBusinessCapabilityGUID;
    }


    /**
     * Return the properties that characterize where this discovery service is from.
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
     * Set up the properties that characterize where this discovery service is from.
     *
     * @param otherOriginValues map of name value pairs, all strings
     */
    public void setOtherOriginValues(Map<String, String> otherOriginValues)
    {
        this.otherOriginValues = otherOriginValues;
    }

    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Return the connection used to create a instance of this discovery service.
     *
     * @return Connection object
     */
    public Connection getConnection()
    {
        return connection;
    }


    /**
     * Set up the connection used to create a instance of this discovery service.
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
        return "DiscoveryServiceProperties{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", owner='" + owner + '\'' +
                ", ownerType=" + ownerType +
                ", zoneMembership=" + zoneMembership +
                ", originOrganizationGUID='" + originOrganizationGUID + '\'' +
                ", originBusinessCapabilityGUID='" + originBusinessCapabilityGUID + '\'' +
                ", otherOriginValues=" + otherOriginValues +
                ", additionalProperties=" + additionalProperties +
                ", connection=" + connection +
                ", headerVersion=" + getHeaderVersion() +
                ", elementHeader=" + getElementHeader() +
                ", typeName='" + getTypeName() + '\'' +
                ", extendedProperties=" + getExtendedProperties() +
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
        DiscoveryServiceProperties that = (DiscoveryServiceProperties) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(displayName, that.displayName) &&
                Objects.equals(description, that.description) &&
                Objects.equals(owner, that.owner) &&
                ownerType == that.ownerType &&
                Objects.equals(zoneMembership, that.zoneMembership) &&
                Objects.equals(originOrganizationGUID, that.originOrganizationGUID) &&
                Objects.equals(originBusinessCapabilityGUID, that.originBusinessCapabilityGUID) &&
                Objects.equals(otherOriginValues, that.otherOriginValues) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
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
        return Objects.hash(qualifiedName, displayName, description, owner, ownerType, zoneMembership,
                            originOrganizationGUID, originBusinessCapabilityGUID, otherOriginValues, additionalProperties,
                            connection);
    }
}
