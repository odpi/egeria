/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.beans.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventCategory;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventOriginator;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventProtocolVersion;
import org.odpi.openmetadata.repositoryservices.events.beans.OMRSEventBean;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * OMRSEventV1 is the OMRSEvent payload for version 1 of the open metadata and governance message exchange.
 * It has different sections for the different types of event.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class OMRSEventV1 extends OMRSEventBean
{
    private static final long    serialVersionUID = 1L;

    private       Date                       timestamp            = null;
    private       OMRSEventOriginator        originator           = null;
    private       OMRSEventCategory          eventCategory        = null;
    private       OMRSEventV1RegistrySection registryEventSection = null;
    private       OMRSEventV1TypeDefSection  typeDefEventSection  = null;
    private       OMRSEventV1InstanceSection instanceEventSection = null;
    private       OMRSEventV1ErrorSection    errorSection         = null;


    /**
     * Default constructor ensures the protocol version id is set in the superclass.
     */
    public OMRSEventV1()
    {
        super(OMRSEventProtocolVersion.V1.getName());
    }


    /**
     * Return the timestamp for when the event was created.
     *
     * @return date and time
     */
    public Date getTimestamp()
    {
        return timestamp;
    }


    /**
     * Set up the timestamp for when the event was created.
     *
     * @param timestamp data/time for the event creation
     */
    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }


    /**
     * Return details about the event originator.  This is to help trace the source of errors or
     * metadata.
     *
     * @return event originator object
     */
    public OMRSEventOriginator getOriginator()
    {
        return originator;
    }

    public void setOriginator(OMRSEventOriginator originator)
    {
        this.originator = originator;
    }

    public OMRSEventCategory getEventCategory()
    {
        return eventCategory;
    }

    public void setEventCategory(OMRSEventCategory eventCategory)
    {
        this.eventCategory = eventCategory;
    }

    public OMRSEventV1RegistrySection getRegistryEventSection()
    {
        return registryEventSection;
    }

    public void setRegistryEventSection(OMRSEventV1RegistrySection registryEventSection)
    {
        this.registryEventSection = registryEventSection;
    }

    public OMRSEventV1TypeDefSection getTypeDefEventSection()
    {
        return typeDefEventSection;
    }

    public void setTypeDefEventSection(OMRSEventV1TypeDefSection typeDefEventSection)
    {
        this.typeDefEventSection = typeDefEventSection;
    }

    public OMRSEventV1InstanceSection getInstanceEventSection()
    {
        return instanceEventSection;
    }

    public void setInstanceEventSection(OMRSEventV1InstanceSection instanceEventSection)
    {
        this.instanceEventSection = instanceEventSection;
    }

    public OMRSEventV1ErrorSection getErrorSection()
    {
        return errorSection;
    }

    public void setErrorSection(OMRSEventV1ErrorSection errorSection)
    {
        this.errorSection = errorSection;
    }

    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OMRSEventV1{" +
                       "timestamp=" + timestamp +
                       ", originator=" + originator +
                       ", eventCategory=" + eventCategory +
                       ", registryEventSection=" + registryEventSection +
                       ", typeDefEventSection=" + typeDefEventSection +
                       ", instanceEventSection=" + instanceEventSection +
                       ", errorSection=" + errorSection +
                       ", protocolVersionId='" + protocolVersionId + '\'' +
                       ", protocolVersionId='" + getProtocolVersionId() + '\'' +
                       '}';
    }
}