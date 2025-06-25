/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The AgreementStatus defines the status of an instance of an agreement.  The default is DRAFT
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AgreementStatus
{
    /**
     * Unknown - Unknown instance status.
     */
    UNKNOWN                 (0,  "Unknown",                 "Unknown instance status."),

    /**
     * Draft - The content is incomplete.
     */
    DRAFT                   (1,  "Draft",                   "The content is incomplete."),

    /**
     * Prepared - The content is ready for review.
     */
    PREPARED                (2,  "Prepared",                "The content is ready for review."),

    /**
     * Proposed - The content is in review.
     */
    PROPOSED                (3,  "Proposed",                "The content is in review."),

    /**
     * Approved - The content is approved.
     */
    APPROVED                (4,  "Approved",                "The content is approved."),

    /**
     * Rejected - The request or proposal is rejected.
     */
    REJECTED                (5,  "Rejected",                "The request or proposal is rejected."),


    /**
     * Active - The instance is approved and in use.
     */
    ACTIVE                  (15, "Active",                  "The instance is approved and in use."),

    /**
     * Deprecated - The instance is out of date and should not be used.
     */
    DEPRECATED              (30, "Deprecated",              "The instance is out of date and should not be used."),

    /**
     * Other - The instance is in a locally defined state.
     */
    OTHER                   (50, "Other",                   "The instance is in a locally defined state."),

    ;

    private  final int    ordinal;
    private  final String name;
    private  final String description;


    /**
     * Default constructor sets up the specific values for an enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param name String name
     * @param description String description
     */
    AgreementStatus(int     ordinal,
                    String  name,
                    String  description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
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
    public String getName() { return name; }


    /**
     * Return the description for the enum.
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
        return "DigitalProductStatus{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
