/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.events;

import java.io.Serializable;

/**
 * OMRSEventCategory defines the different categories of events that pass through the OMRS Topic.
 * <ul>
 *     <li>
 *         UNKNOWN - this is either an uninitialized event, or the incoming event is not supported by the
 *         local server.
 *     </li>
 *     <li>
 *         REGISTRY - this is an event used by the cohort registries to manage the membership
 *         of the cohort.
 *     </li>
 *     <li>
 *         TYPEDEF - this is an event used by the metadata repository connectors to synchronize the metadata types
 *         (stored in TypeDefs) across the metadata repository cohort.
 *     </li>
 * </ul>
 */
public enum OMRSEventCategory implements Serializable
{
    UNKNOWN (0, "Unknown Event",  "Unknown event category"),
    REGISTRY(1, "Registry Event", "Event used to manage the membership of the metadata repository cohort"),
    TYPEDEF (2, "TypeDef Event",  "Event used to manage the synchronization of TypeDefs within the metadata repository cohort"),
    INSTANCE(3, "Instance Event", "Event used to manage the replication of metadata instances within the metadata repository cohort"),
    GENERIC (99, "Generic Event", "Event used for sending generic messages - typically error messages.");

    private static final long serialVersionUID = 1L;

    private int    categoryCode;
    private String categoryName;
    private String categoryDescription;


    /**
     * Default constructor.
     *
     * @param categoryCode - int category code number
     * @param categoryName - String category name
     * @param categoryDescription - String category description
     */
    OMRSEventCategory(int      categoryCode,
                      String   categoryName,
                      String   categoryDescription)
    {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
    }


    /**
     * Return the code number for the event category.
     *
     * @return int code number
     */
    public int getEventCategoryCode()
    {
        return categoryCode;
    }


    /**
     * Return the name of the event category.
     *
     * @return String name
     */
    public String getEventCategoryName()
    {
        return categoryName;
    }


    /**
     * Return the default description of the event category.  This description is in English and is a default
     * value for the situation when the natural language resource bundle for Event Category is not available.
     *
     * @return String default description
     */
    public String getEventCategoryDescription()
    {
        return categoryDescription;
    }
}
