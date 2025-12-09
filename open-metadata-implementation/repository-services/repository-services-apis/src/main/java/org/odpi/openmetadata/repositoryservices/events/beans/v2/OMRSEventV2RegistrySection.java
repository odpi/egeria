/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.beans.v2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEventType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventV1RegistrySection describes properties that are used exclusively for registry events.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSEventV2RegistrySection implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private OMRSRegistryEventType registryEventType      = null;
    private Date                  registrationTimestamp  = null;
    private String                metadataCollectionName = null;
    private Connection            remoteConnection       = null;


    /**
     * Default constructor
     */
    public OMRSEventV2RegistrySection()
    {
    }


    /**
     * Return the reason for the event.
     *
     * @return enum
     */
    public OMRSRegistryEventType getRegistryEventType()
    {
        return registryEventType;
    }


    /**
     * Set up the reason for the event.
     *
     * @param registryEventType enum
     */
    public void setRegistryEventType(OMRSRegistryEventType registryEventType)
    {
        this.registryEventType = registryEventType;
    }


    /**
     * Return the timestamp for the registration.
     *
     * @return date/time
     */
    public Date getRegistrationTimestamp()
    {
        return registrationTimestamp;
    }


    /**
     * Set up the timestamp for the registration.
     *
     * @param registrationTimestamp date/time
     */
    public void setRegistrationTimestamp(Date registrationTimestamp)
    {
        this.registrationTimestamp = registrationTimestamp;
    }


    /**
     * Return the optional metadata collection name.
     *
     * @return name
     */
    public String getMetadataCollectionName()
    {
        return metadataCollectionName;
    }


    /**
     * Set up the optional metadata collection name.
     *
     * @param metadataCollectionName name
     */
    public void setMetadataCollectionName(String metadataCollectionName)
    {
        this.metadataCollectionName = metadataCollectionName;
    }


    /**
     * Return the connection used to call the remote repository.
     *
     * @return connection
     */
    public Connection getRemoteConnection()
    {
        return remoteConnection;
    }


    /**
     * Set up the connection used to call the remote repository.
     *
     * @param remoteConnection connection
     */
    public void setRemoteConnection(Connection remoteConnection)
    {
        this.remoteConnection = remoteConnection;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OMRSEventV1RegistrySection{" +
                       "registryEventType=" + registryEventType +
                       ", registrationTimestamp=" + registrationTimestamp +
                       ", metadataCollectionName='" + metadataCollectionName + '\'' +
                       ", remoteConnection=" + remoteConnection +
                       '}';
    }
}
