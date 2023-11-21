/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceVisibility documents who can make use of a digital service.
 * <ul>
 *     <li>
 *         UNCLASSIFIED_VISIBILITY - The digital service definition is a draft.  Typically the initial description is being written up by
 *         the enterprise/data/solution architect in conjunction with the business owner.
 *     </li>
 *     <li>
 *         EXTERNAL_SERVICE - The digital service is generally available to customers, business partners and
 *         integration brokers.
 *     </li>
 *     <li>
 *         PARTNER_SERVICE - The digital service is provided by an external organization (business partner).
 *     </li>
 *     <li>
 *         INTERNAL_SERVICE - The digital service is for internal use by employees.
 *     </li>
 *     <li>
 *         OTHER - The digital service has a locally defined visibility.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DigitalServiceVisibility
{
    UNCLASSIFIED_VISIBILITY (0,  "Unclassified",        "The digital service has no assigned visibility."),
    EXTERNAL_SERVICE        (1,  "External Service",    "The digital service is available to consumers outside of the organization."),
    PARTNER_SERVICE         (2,  "Partner Service",     "The digital service is provided by another organization."),
    INTERNAL_SERVICE        (3,  "Internal Service",    "The digital service is for internal use only."),
    OTHER                   (99, "Other",               "The digital service has a locally defined visibility.");


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
    DigitalServiceVisibility(int  ordinal, String name, String description)
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
     * {@inheritDoc}
     *
     * toString() JSON-style
     */
    @Override
    public String toString()
    {
        return "DigitalServiceVisibility{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
