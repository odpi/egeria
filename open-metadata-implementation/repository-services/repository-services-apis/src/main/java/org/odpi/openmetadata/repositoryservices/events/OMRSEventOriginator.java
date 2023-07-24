/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventOriginator is part of an OMRSEvent's header.  It defines the properties of the server/repository
 * that originated the event.  These properties are as follows:
 * <ul>
 *     <li>
 *         metadataCollectionId: the unique identifier of the metadata collection in the
 *         originating server. This is a mandatory property.
 *     </li>
 *     <li>
 *         ServerName: this is a display name for the server that is used in events, messages and UIs to
 *         make it easier for people to understand the origin of metadata.  It is optional.
 *     </li>
 *     <li>
 *         ServerType: this is a descriptive string describing the type of the server.  This might be the
 *         name of the product, or similar identifier. This is an optional property.
 *     </li>
 *     <li>
 *         OrganizationName: this is a descriptive name for the organization that runs/owns the server.  For
 *         an enterprise, it may be the name of a department, geography or division.  If the cohort covers a group
 *         of business partners then it may be their respective company names.  This is an optional field.
 *     </li>
 *     <li>
 *         ProtocolVersion: this is an enumeration that identifies which version of the OMRS event structure
 *         should be used.  In general it should be set to the highest level that all servers in the cohort
 *         can support.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSEventOriginator implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String                   metadataCollectionId = null;
    private String                   serverName           = null;
    private String                   serverType           = null;
    private String                   organizationName     = null;

    /**
     * Default constructor used by parsing engines and other consumers.
     */
    public OMRSEventOriginator()
    {
    }


    /**
     * Returns the unique identifier (guid) of the originating repository's metadata collection.
     *
     * @return String guid
     */
    public String getMetadataCollectionId()
    {
        return metadataCollectionId;
    }


    /**
     * Sets up the unique identifier (guid) of the originating repository.
     *
     * @param metadataCollectionId String guid
     */
    public void setMetadataCollectionId(String metadataCollectionId)
    {
        this.metadataCollectionId = metadataCollectionId;
    }


    /**
     * Return the display name for the server that is used in events, messages and UIs to
     * make it easier for people to understand the origin of metadata.
     *
     * @return String server name
     */
    public String getServerName()
    {
        return serverName;
    }


    /**
     * Set up the display name for the server that is used in events, messages and UIs to
     * make it easier for people to understand the origin of metadata.
     *
     * @param serverName String server name
     */
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Return the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @return String server type
     */
    public String getServerType()
    {
        return serverType;
    }


    /**
     * Set up the descriptive string describing the type of the server.  This might be the
     * name of the product, or similar identifier.
     *
     * @param serverType String server type
     */
    public void setServerType(String serverType)
    {
        this.serverType = serverType;
    }


    /**
     * Return the name of the organization that runs/owns the server.
     *
     * @return String organization name
     */
    public String getOrganizationName()
    {
        return organizationName;
    }


    /**
     * Set up the name of the organization that runs/owns the server.
     *
     * @param organizationName String organization name
     */
    public void setOrganizationName(String organizationName)
    {
        this.organizationName = organizationName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSEventOriginator{" +
                "metadataCollectionId='" + metadataCollectionId + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverType='" + serverType + '\'' +
                ", organizationName='" + organizationName + '\'' +
                '}';
    }
}
