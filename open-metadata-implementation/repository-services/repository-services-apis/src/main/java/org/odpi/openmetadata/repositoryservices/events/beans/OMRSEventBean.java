/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.beans;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.events.beans.v1.OMRSEventV1;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventBean provides a common root for all bean versions of the OMRSEvent.  It has no content beyond
 * the event's protocol version, but the
 * Jackson annotations declare the valid supported versions in this server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OMRSEventV1.class, name = "OMRSEventV1")
})
public abstract class OMRSEventBean implements Serializable
{
    private static final long serialVersionUID = 1L;

    protected String                     protocolVersionId    = null;


    /**
     * Default constructor used when recreating events from JSON Strings
     */
    public OMRSEventBean()
    {
    }


    /**
     * Constructor used when an instance of the subclass is created.
     *
     * @param protocolVersionId version name
     */
    public OMRSEventBean(String protocolVersionId)
    {
        this.protocolVersionId = protocolVersionId;
    }


    /**
     * Return the protocol version identifier for this event.
     *
     * @return version name
     */
    public String getProtocolVersionId()
    {
        return protocolVersionId;
    }


    /**
     * Set up the protocol version id for this event.
     *
     * @param protocolVersionId version name
     */
    public void setProtocolVersionId(String protocolVersionId)
    {
        this.protocolVersionId = protocolVersionId;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OMRSEventBean{" +
                       "protocolVersionId='" + protocolVersionId + '\'' +
                       '}';
    }
}
