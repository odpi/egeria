/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.events.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.events.OMRSRegistryEventType;

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
public class OMRSEventV1RegistrySection implements Serializable
{
    private static final long serialVersionUID = 1L;

    private OMRSRegistryEventType registryEventType     = null;
    private Date                  registrationTimestamp = null;
    private Connection            remoteConnection      = null;


    public OMRSEventV1RegistrySection()
    {
    }

    public OMRSRegistryEventType getRegistryEventType()
    {
        return registryEventType;
    }

    public void setRegistryEventType(OMRSRegistryEventType registryEventType)
    {
        this.registryEventType = registryEventType;
    }

    public Date getRegistrationTimestamp()
    {
        return registrationTimestamp;
    }

    public void setRegistrationTimestamp(Date registrationTimestamp)
    {
        this.registrationTimestamp = registrationTimestamp;
    }

    public Connection getRemoteConnection()
    {
        return remoteConnection;
    }

    public void setRemoteConnection(Connection remoteConnection)
    {
        this.remoteConnection = remoteConnection;
    }
}
