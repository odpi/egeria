/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */

package org.odpi.openmetadata.accessservices.subjectarea.properties.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * The Status defines the status of a relationship or an entity in the glossary.  It effectively
 * defines its visibility to different types of queries. Most queries by default will only return instances in the
 * active status.
 * <ul>
 *     <li>UNKNOWN Unknown status of instance.</li>
 *     <li>DRAFT The content is incomplete.</li>
 *     <li>PREPARED The content is ready for review.</li>
 *     <li>PROPOSED The content is in review.</li>
 *     <li>APPROVED Instance approved.</li>
 *     <li>ACTIVE: the instance is in active use.</li>
 *     <li>DELETED: the instance has been deleted and is waiting to be purged.  It is kept in the metadata collection
 *     to support a restore request.  It is not returned on normal queries.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum Status implements Serializable
{

    /**
     * Unknown status of instance
     */
    UNKNOWN(0, "Unknown",   "Unknown status of instance."),

    /**
     * The content incomplete and not ready for review.
     */
    DRAFT(1, "Draft",    "The content incomplete"),

    /**
     * The content is ready for review.
     */
    PREPARED(2, "Prepared",    "The content is ready for review."),

    /**
     * The content is in review
     */
    PROPOSED(3, "Proposed",    "The content is in review."),

    /**
     * The content has been approved.
     */
    APPROVED(4, "Approved",    "Instance approved."),

    /**
     * The instance is in use
     */
    ACTIVE (15, "Active",    "The instance is in use."),

    /**
     * Instance that has been deleted and is no longer in use.
     */
 
    DELETED(90, "Deleted",   "Instance that has been deleted and is no longer in use.");

    private static final long serialVersionUID = 1L;

    private  int ordinal;
    private  String  statusName;
    private  String  statusDescription;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param statusName String name
     * @param statusDescription String description
     */
    Status(int     ordinal,
                   String  statusName,
                   String  statusDescription)
    {
        this.ordinal = ordinal;
        this.statusName = statusName;
        this.statusDescription = statusDescription;
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
    public String getStatusName() { return statusName; }


    /**
     * Return the description for the enum.
     *
     * @return String description
     */
    public String getStatusDescription() { return statusDescription; }
}
