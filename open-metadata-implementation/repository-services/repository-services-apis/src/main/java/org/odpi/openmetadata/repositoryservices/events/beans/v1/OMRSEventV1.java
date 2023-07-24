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

import java.io.Serial;
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
    @Serial
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
     * Return details about the event originator.  This is to help trace the source of errors or metadata.
     *
     * @return event originator object
     */
    public OMRSEventOriginator getOriginator()
    {
        return originator;
    }


    /**
     * Set up details about the event originator.  This is to help trace the source of errors or metadata.
     *
     * @param originator event originator object
     */
    public void setOriginator(OMRSEventOriginator originator)
    {
        this.originator = originator;
    }


    /**
     * Return whether this is a registry, type or instance event.
     *
     * @return category
     */
    public OMRSEventCategory getEventCategory()
    {
        return eventCategory;
    }


    /**
     * Set up whether this is a registry, type or instance event.
     *
     * @param eventCategory category
     */
    public void setEventCategory(OMRSEventCategory eventCategory)
    {
        this.eventCategory = eventCategory;
    }


    /**
     * Return the properties specific to a registry event.
     *
     * @return registry section
     */
    public OMRSEventV1RegistrySection getRegistryEventSection()
    {
        return registryEventSection;
    }


    /**
     * Set up the properties specific to a registry event.
     *
     * @param registryEventSection registry section
     */
    public void setRegistryEventSection(OMRSEventV1RegistrySection registryEventSection)
    {
        this.registryEventSection = registryEventSection;
    }


    /**
     * Return the properties specific to a type event.
     *
     * @return type section
     */
    public OMRSEventV1TypeDefSection getTypeDefEventSection()
    {
        return typeDefEventSection;
    }


    /**
     * Set up the properties specific to a type event.
     *
     * @param typeDefEventSection type section
     */
    public void setTypeDefEventSection(OMRSEventV1TypeDefSection typeDefEventSection)
    {
        this.typeDefEventSection = typeDefEventSection;
    }


    /**
     * Return the properties specific to an instance event.
     *
     * @return instance section
     */
    public OMRSEventV1InstanceSection getInstanceEventSection()
    {
        return instanceEventSection;
    }


    /**
     * Set up the properties specific to an instance event.
     *
     * @param instanceEventSection instance section
     */
    public void setInstanceEventSection(OMRSEventV1InstanceSection instanceEventSection)
    {
        this.instanceEventSection = instanceEventSection;
    }


    /**
     * Return the properties specific to an error event.
     *
     * @return error section
     */
    public OMRSEventV1ErrorSection getErrorSection()
    {
        return errorSection;
    }


    /**
     * Set up the properties specific to an error event.
     *
     * @param errorSection error section
     */
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