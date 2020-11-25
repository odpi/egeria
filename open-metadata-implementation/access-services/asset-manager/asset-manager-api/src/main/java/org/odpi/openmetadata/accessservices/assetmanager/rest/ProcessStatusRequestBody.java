/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessStatus;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GlossaryTermStatusRequestBody describes the request body used to update a glossary term's status.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProcessStatusRequestBody implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private MetadataCorrelationProperties metadataCorrelationProperties = null;
    private ProcessStatus                 processStatus                 = null;


    /**
     * Default constructor
     */
    public ProcessStatusRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public ProcessStatusRequestBody(ProcessStatusRequestBody template)
    {
        if (template != null)
        {
            metadataCorrelationProperties = template.getMetadataCorrelationProperties();
            processStatus = template.getProcessStatus();
        }
    }


    /**
     * Return the properties used to correlate the external metadata element with the open metadata element.
     *
     * @return properties object
     */
    public MetadataCorrelationProperties getMetadataCorrelationProperties()
    {
        return metadataCorrelationProperties;
    }


    /**
     * Set up the properties used to correlate the external metadata element with the open metadata element.
     *
     * @param metadataCorrelationProperties properties object
     */
    public void setMetadataCorrelationProperties(MetadataCorrelationProperties metadataCorrelationProperties)
    {
        this.metadataCorrelationProperties = metadataCorrelationProperties;
    }


    /**
     * Return the status for the element.
     *
     * @return enum object
     */
    public ProcessStatus getProcessStatus()
    {
        return processStatus;
    }


    /**
     * Set up the status for the element.
     *
     * @param processStatus enum object
     */
    public void setProcessStatus(ProcessStatus processStatus)
    {
        this.processStatus = processStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProcessStatusRequestBody{" +
                       "metadataCorrelationProperties=" + metadataCorrelationProperties +
                       ", processStatus=" + processStatus +
                       '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        ProcessStatusRequestBody that = (ProcessStatusRequestBody) objectToCompare;
        return Objects.equals(getMetadataCorrelationProperties(), that.getMetadataCorrelationProperties()) &&
                       Objects.equals(getProcessStatus(), that.getProcessStatus());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), metadataCorrelationProperties, processStatus);
    }
}
