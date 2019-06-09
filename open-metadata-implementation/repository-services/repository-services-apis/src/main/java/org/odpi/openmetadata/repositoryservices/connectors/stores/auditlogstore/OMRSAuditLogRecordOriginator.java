/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.auditlogstore;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAuditLogRecordOriginator describes the server that originated an audit log record.  This is useful if
 * an organization is aggregating messages from different servers together.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSAuditLogRecordOriginator
{
    private String                   metadataCollectionId = null;
    private String                   serverName           = null;
    private String                   serverType           = null;
    private String                   organizationName     = null;

    /**
     * Default constructor used by parsing engines and other consumers.
     */
    public OMRSAuditLogRecordOriginator()
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
     * @param metadataCollectionId  String guid
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
     * @param serverName  String server name
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
     * @param serverType  String server type
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
     * @param organizationName  String organization name
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
        return "OMRSAuditLogRecordOriginator{" +
                "metadataCollectionId='" + metadataCollectionId + '\'' +
                ", serverName='" + serverName + '\'' +
                ", serverType='" + serverType + '\'' +
                ", organizationName='" + organizationName + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OMRSAuditLogRecordOriginator that = (OMRSAuditLogRecordOriginator) objectToCompare;
        return Objects.equals(getMetadataCollectionId(), that.getMetadataCollectionId()) &&
                Objects.equals(getServerName(), that.getServerName()) &&
                Objects.equals(getServerType(), that.getServerType()) &&
                Objects.equals(getOrganizationName(), that.getOrganizationName());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getMetadataCollectionId(), getServerName(), getServerType(), getOrganizationName());
    }
}
