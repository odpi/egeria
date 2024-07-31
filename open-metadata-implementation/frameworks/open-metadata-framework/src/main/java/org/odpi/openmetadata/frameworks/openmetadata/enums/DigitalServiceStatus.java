/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DigitalServiceStatus tracks the life cycle of a digital service.
 * <ul>
 *     <li>
 *         DRAFT - The digital service definition is a draft.  Typically the initial description is being written up by
 *         the enterprise/data/solution architect in conjunction with the business owner.
 *     </li>
 *     <li>
 *         PROPOSED - The digital service definition is in planning and business review.  Its initial definition is
 *         complete and the team are reviewing it for viability.  This includes a governance review (such as a data
 *         processing impact assessment) that needs to occur before development starts.
 *     </li>
 *     <li>
 *         APPROVED_CONCEPT - The digital service definition is approved for development.  All governance checks are
 *         in place and it is waiting for the development team to get started.
 *     </li>
 *     <li>
 *         UNDER_DEVELOPMENT - The development team is at work.  This covers the technical implementation and the
 *         business development activity necessary to go live with the digital service.
 *     </li>
 *     <li>
 *         APPROVED_FOR_DEPLOYMENT - The digital service definition is approved for deployment.  This means the
 *         software components for the digital service can be deployed and made available to its consumers.
 *     </li>
 *     <li>
 *         ACTIVE - The digital service definition is approved and in use.
 *     </li>
 *     <li>
 *         DEPRECATED - The digital service definition is no longer active.  It may have been replaced by another
 *         digital service or deactivated because it is no longer needed, viable or profitable.
 *     </li>
 *     <li>
 *         OTHER - The digital service definition in a locally defined state.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum

DigitalServiceStatus
{
    DRAFT                   (0,  "Draft",                   "The digital service definition is a draft."),
    PROPOSED                (1,  "Proposed",                "The digital service definition is in planning, feasibility study and business review."),
    APPROVED_CONCEPT        (2,  "Approved Concept",        "The digital service definition is approved for development."),
    UNDER_DEVELOPMENT       (3,  "Under development",       "The digital service definition is being developed."),
    APPROVED_FOR_DEPLOYMENT (4,  "Approved for deployment", "The digital service definition is approved for deployment."),
    ACTIVE                  (5,  "Active",                  "The digital service definition is approved and in use."),
    DEPRECATED              (6,  "Deprecated",              "The digital service definition is no longer active."),
    OTHER                   (99, "Other",                   "The digital service definition in a locally defined state.");


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
    DigitalServiceStatus(int  ordinal, String name, String description)
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
        return "DigitalServiceStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
