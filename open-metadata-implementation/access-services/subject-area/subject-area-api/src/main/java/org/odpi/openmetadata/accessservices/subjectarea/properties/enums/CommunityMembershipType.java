/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.subjectarea.properties.enums;
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
 * Type of membership to a community.
 */

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CommunityMembershipType implements Serializable {
    /**
      * Participant in the community.
      */
    Member(0, "Participant in the community.")
,
    /**
      * Administrator of the community.
      */
    Administrator(1, "Administrator of the community.")
,
    /**
      * Leader of the community.
      */
    Leader(2, "Leader of the community.")
,
    /**
      * Observer of the community.
      */
    Observer(3, "Observer of the community.")
,
    /**
      * Another role in the community.
      */
    Other(99, "Another role in the community.")
;
    private final int ordinal;
    private final String description ;
    /**
     * Default constructor sets up the specific values for an CommunityMembershipType enum instance.
     *
     * @param ordinal int enum value ordinal
     * @param description String description
     */
    CommunityMembershipType(int ordinal, String description) {
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
     * Return the descriptive name for the CommunityMembershipType enum instance
     *
     * @return String name
     */
    public String getName() { return this.name(); }
}
