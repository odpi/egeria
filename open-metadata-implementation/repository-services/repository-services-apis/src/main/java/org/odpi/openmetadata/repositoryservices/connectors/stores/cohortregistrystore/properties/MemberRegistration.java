/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MemberRegistration is a POJO for storing the information about a metadata repository that is a member
 * of the open metadata repository cohort. This information is saved to disk by the
 * OMRSCohortRegistryStore.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MemberRegistration implements Serializable
{
    private static final long serialVersionUID = 1L;

    /*
     * Information about a metadata repository that is a member of the metadata repository cohort
     */
    private String     metadataCollectionId   = null;
    private String     metadataCollectionName = null;
    private String     serverName             = null;
    private String     serverType             = null;
    private String     organizationName       = null;
    private Date       registrationTime       = null;
    private Connection repositoryConnection   = null;


    /**
     * Default constructor initializes registration information to null.
     */
    public MemberRegistration()
    {
        /*
         * Nothing to do
         */
    }


    /**
     * Copy/clone constructor copies registration information from the template.
     *
     * @param template MemberRegistration properties to copy
     */
    public MemberRegistration(MemberRegistration template)
    {
        if (template != null)
        {
            metadataCollectionId = template.getMetadataCollectionId();
            metadataCollectionName = template.getMetadataCollectionName();
            serverName = template.getServerName();
            serverType = template.getServerType();
            organizationName = template.getOrganizationName();
            registrationTime = template.getRegistrationTime();
            repositoryConnection = template.getRepositoryConnection();

        }
    }


    /**
     * Return the unique identifier of the repository's metadata collection id.
     *
     * @return String metadata collection id
     */
    public String getMetadataCollectionId() { return metadataCollectionId; }


    /**
     * Set up the unique identifier of the repository's metadata collection id.
     *
     * @param metadataCollectionId String guid
     */
    public void setMetadataCollectionId(String metadataCollectionId) { this.metadataCollectionId = metadataCollectionId; }


    /**
     * Return the metadata collection name.  The server name is returned if the metadata collection name has not been explicitly provided.
     *
     * @return string name
     */
    public String getMetadataCollectionName()
    {
        if (metadataCollectionName != null)
        {
            return metadataCollectionName;
        }
        else
        {
            return serverName;
        }
    }


    /**
     * Set up the metadata collection name.  This overrides the default value of the server name.
     *
     * @param metadataCollectionName string name
     */
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        this.metadataCollectionName = metadataCollectionName;
    }


    /**
     * Return the display name for the server.  It is not guaranteed to be unique just confusing for
     * administrators if it is different.  The display name can change over time with no loss of data integrity.
     *
     * @return String display name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set up the display name for the server.  It is not guaranteed to be unique just confusing for
     * administrators if it is different.  The display name can change over time with no loss of data integrity.
     *
     * @param serverName String display name
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Return the type of server.
     *
     * @return String server type
     */
    public String getServerType()
    {
        return serverType;
    }


    /**
     * Set up the type of server.
     *
     * @param serverType String server type
     */
    public void setServerType(String serverType)
    {
        this.serverType = serverType;
    }


    /**
     * Return the name of the organization.
     *
     * @return String name of the organization
     */
    public String getOrganizationName()
    {
        return organizationName;
    }


    /**
     * Set up the name of the organization.
     *
     * @param organizationName String name of the organization
     */
    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }


    /**
     * Return the time that this repository registered with the cohort. (Or null if it has not yet registered.)
     *
     * @return Date object representing the registration time stamp
     */
    public Date getRegistrationTime()
    {
        return registrationTime;
    }


    /**
     * Set up the time that this repository registered with the cohort. (Or null if it has not yet registered.)
     *
     * @param registrationTime Date object representing the registration time stamp
     */
    public void setRegistrationTime(Date registrationTime) { this.registrationTime = registrationTime; }


    /**
     * Return the connection information for a connector that enables remote calls to the repository server.
     *
     * @return Connection object containing the properties of the connection
     */
    public Connection getRepositoryConnection()
    {
        if (repositoryConnection == null)
        {
            return null;
        }
        else
        {
            return new Connection(repositoryConnection);
        }
    }


    /**
     * Set up the connection information for a connector that enables remote calls to the repository server.
     *
     * @param repositoryConnection Connection object containing the properties of the connection
     */
    public void setRepositoryConnection(Connection repositoryConnection)
    {
        this.repositoryConnection = repositoryConnection;
    }


    /**
     * Validate if the values stored match the object to compare.
     *
     * @param objectToCompare test object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof MemberRegistration))
        {
            return false;
        }
        MemberRegistration that = (MemberRegistration) objectToCompare;
        return Objects.equals(getMetadataCollectionId(), that.getMetadataCollectionId()) &&
                Objects.equals(getMetadataCollectionName(), that.getMetadataCollectionName()) &&
                Objects.equals(getServerName(), that.getServerName()) &&
                Objects.equals(getServerType(), that.getServerType()) &&
                Objects.equals(getOrganizationName(), that.getOrganizationName()) &&
                Objects.equals(getRegistrationTime(), that.getRegistrationTime()) &&
                Objects.equals(getRepositoryConnection(), that.getRepositoryConnection());
    }


    /**
     * Hash code base on variable values.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(getMetadataCollectionId(),
                            getMetadataCollectionName(),
                            getServerName(),
                            getServerType(),
                            getOrganizationName(),
                            getRegistrationTime(),
                            getRepositoryConnection());
    }


    /**
     * toString JSON-style
     *
     * @return string containing variable values
     */
    @Override
    public String toString()
    {
        return "MemberRegistration{" +
                "metadataCollectionId='" + metadataCollectionId + '\'' +
                ", metadataCollectionName='" + metadataCollectionName + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverType='" + serverType + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", registrationTime=" + registrationTime +
                ", repositoryConnection=" + repositoryConnection +
                '}';
    }
}
