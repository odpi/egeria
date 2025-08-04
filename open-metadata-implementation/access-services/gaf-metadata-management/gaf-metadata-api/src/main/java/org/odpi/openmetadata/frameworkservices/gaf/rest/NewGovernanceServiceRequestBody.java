/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworkservices.gaf.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * NewGovernanceServiceRequestBody provides a structure for passing the properties of a new governance service
 * as a request body over a REST API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = UpdateGovernanceServiceRequestBody.class, name = "UpdateGovernanceServiceRequestBody")
        })
public class NewGovernanceServiceRequestBody
{
    private String     qualifiedName              = null;
    private String     name                       = null;
    private String     versionIdentifier          = null;
    private String     description                = null;
    private String     deployedImplementationType = null;
    private Connection connection                 = null;


    /**
     * Default constructor
     */
    public NewGovernanceServiceRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public NewGovernanceServiceRequestBody(NewGovernanceServiceRequestBody template)
    {
        if (template != null)
        {
            qualifiedName              = template.getQualifiedName();
            name                       = template.getName();
            versionIdentifier          = template.getVersionIdentifier();
            description                = template.getDescription();
            deployedImplementationType = template.getDeployedImplementationType();
            connection                 = template.getConnection();
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
     * Return the name of the resource that this asset represents.
     *
     * @return string resource name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the resource that this asset represents.
     *
     * @param name string resource name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @return string version name
     */
    public String getVersionIdentifier()
    {
        return versionIdentifier;
    }


    /**
     * Set up the version identifier of the resource.
     *
     * @param versionIdentifier string version name
     */
    public void setVersionIdentifier(String versionIdentifier)
    {
        this.versionIdentifier = versionIdentifier;
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
     * Retrieve the name of the technology used for this data asset.
     *
     * @return string name
     */
    public String getDeployedImplementationType()
    {
        return deployedImplementationType;
    }


    /**
     * Set up the name of the technology used for this data asset.
     *
     * @param deployedImplementationType string name
     */
    public void setDeployedImplementationType(String deployedImplementationType)
    {
        this.deployedImplementationType = deployedImplementationType;
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
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "NewGovernanceServiceRequestBody{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", name='" + name + '\'' +
                ", versionIdentifier='" + versionIdentifier + '\'' +
                ", description='" + description + '\'' +
                ", deployedImplementationType='" + deployedImplementationType + '\'' +
                ", connection=" + connection +
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
        NewGovernanceServiceRequestBody that = (NewGovernanceServiceRequestBody) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                       Objects.equals(name, that.name) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(connection, that.connection);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(qualifiedName, name, description, connection);
    }
}
