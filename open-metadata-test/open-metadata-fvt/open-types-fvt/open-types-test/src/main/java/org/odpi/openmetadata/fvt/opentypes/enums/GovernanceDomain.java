/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.fvt.opentypes.enums;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the governance domains that open metadata seeks to unite.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GovernanceDomain implements Serializable {
    /**
      * The governance domain is not specified.
      */
    Unclassified(0, "The governance domain is not specified.")
,
    /**
      * The data (information) governance domain.
      */
    Data(1, "The data (information) governance domain.")
,
    /**
      * The data privacy domain.
      */
    Privacy(2, "The data privacy domain.")
,
    /**
      * The security governance domain.
      */
    Security(3, "The security governance domain.")
,
    /**
      * The IT infrastructure governance domain.
      */
    ITInfrastructure(4, "The IT infrastructure governance domain.")
,
    /**
      * The software development lifecycle governance domain.
      */
    SoftwareDevelopment(5, "The software development lifecycle governance domain.")
,
    /**
      * The corporate governance domain.
      */
    Corporate(6, "The corporate governance domain.")
,
    /**
      * The physical asset management governance domain.
      */
    AssetManagement(7, "The physical asset management governance domain.")
,
    /**
      * Another governance domain.
      */
    Other(99, "Another governance domain.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an GovernanceDomain enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    GovernanceDomain(int ordinal, String description) {
        this.ordinal = ordinal;
        this.description = description;
    }

    /**
     * Return the numerical value for the enum.
     *
     * @return int enum value ordinal
     */
    public int getOrdinal() { return this.ordinal; }
    /**
     * Return the description for the enum.
     *
     * @return String description
     */
    public String getDescription() { return this.description; }
    /**
     * Return the descriptive name for the GovernanceDomain enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
