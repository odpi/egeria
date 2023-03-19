/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The ProcessStatus defines the status of a process.  It effectively defines its visibility to different types of queries.
 * Most queries by default will only return instances in the active status.
 * <ul>
 *     <li>Unknown: Unknown process status.</li>
 *     <li>Draft: The process is incomplete.</li>
 *     <li>Proposed: The process is in review.</li>
 *     <li>Approved: The process is approved.</li>
 *     <li>Active: The process is approved and in use.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ProcessStatus implements Serializable
{
    UNKNOWN   (ElementStatus.UNKNOWN),
    DRAFT     (ElementStatus.DRAFT),
    PROPOSED  (ElementStatus.PROPOSED),
    APPROVED  (ElementStatus.APPROVED),
    ACTIVE    (ElementStatus.ACTIVE);

    private final ElementStatus elementStatus;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param elementStatus status value
     */
    ProcessStatus(ElementStatus elementStatus)
    {
        this.elementStatus         = elementStatus;
    }


    /**
     * Return the equivalent element status
     *
     * @return enum
     */
    public ElementStatus getElementStatus()
    {
        return elementStatus;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int key pattern code
     */
    public int getOrdinal()
    {
        return elementStatus.getOrdinal();
    }


    /**
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return elementStatus.getOrdinal();
    }



    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return elementStatus.getName();
    }


    /**
     * Return the default description for the key pattern for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return elementStatus.getDescription();
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "ProcessStatus{" +
                       "elementStatus=" + elementStatus +
                       '}';
    }}
