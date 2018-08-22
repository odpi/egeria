/* SPDX-License-Identifier: Apache-2.0 */

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
    ACTIVE (15, "Active",    "Active instance in use."),
    DELETED(99, "Deleted",   "Instance that has been deleted and is no longer in use.");

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
