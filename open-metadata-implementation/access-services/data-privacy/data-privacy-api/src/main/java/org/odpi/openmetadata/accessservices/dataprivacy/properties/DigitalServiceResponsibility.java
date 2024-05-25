/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceResponsibility documents whether the digital service is a data controller or a data processor.
 * <ul>
 *     <li>
 *         UNCLASSIFIED - the digital service's responsibilities are undefined.
 *     </li>
 *     <li>
 *         DATA_CONTROLLER - The digital service controls how data is processed and is responsible for ensuring
 *         there is proper consent for all personal data processed.
 *     </li>
 *     <li>
 *         DATA_PROCESSOR - The digital service is receiving permission to process data from its caller.
 *     </li>
 *     <li>
 *         OTHER - The digital service has a locally defined responsibility.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DigitalServiceResponsibility
{
    UNCLASSIFIED_RESPONSIBILITY (0,  "Unclassified",     "The digital service has no assigned responsibility."),
    DATA_CONTROLLER             (1,  "Data Controller",  "The digital service is a data controller."),
    DATA_PROCESSOR              (2,  "Data Processor",   "The digital service is a data processor."),
    OTHER                       (99, "Other",            "The digital service has a locally defined responsibility.");

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param name default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    DigitalServiceResponsibility(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "DigitalServiceResponsibility{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
