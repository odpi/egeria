/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */

package org.odpi.openmetadata.accessservices.subjectarea.properties.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The StatusFilter is used on the API to Filter the status of instances.
 * <ul>
 *     <li>ALL All instances irrespective of status.</li>
 *     <li>ACTIVE: Only active instances.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum StatusFilter implements Serializable
{

    /**
     * All instances irrespective of status.
     */
    ALL(1, "Unknown",   "All instances irrespective of status."),

    /**
     * Only active instances.
     */
    ACTIVE (15, "Active",    "Only active instances.");

    private static final long serialVersionUID = 1L;

    private  int ordinal;
    private  String statusFilterName;
    private  String statusFilterDescription;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param statusName String name
     * @param statusDescription String description
     */
    StatusFilter(int     ordinal,
                 String  statusName,
                 String  statusDescription)
    {
        this.ordinal = ordinal;
        this.statusFilterName = statusName;
        this.statusFilterDescription = statusDescription;
    }


    /**
     * Return the numerical value for the enum.
     *
     * @return int enum value ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the descriptive name for the enum.
     *
     * @return String name
     */
    public String getStatusFilterName() { return statusFilterName; }


    /**
     * Return the description for the enum.
     *
     * @return String description
     */
    public String getStatusFilterDescription() { return statusFilterDescription; }
}
