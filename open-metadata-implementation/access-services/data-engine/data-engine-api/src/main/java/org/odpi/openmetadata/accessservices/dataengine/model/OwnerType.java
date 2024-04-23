/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetOwnerType defines the identifier used in an Asset's owner property.
 * <ul>
 *     <li>User Identifier - The owner's user id is stored in the owner property.</li>
 *     <li>Profile - The owner's profile unique identifier (guid) is stored in the owner property.</li>
 *     <li>Other - A different identifier for the owner outside of the scope of open metadata has been used.</li>
 * </ul>
 * Being able to use a profile guid in this field allows for Assets to be owned by Teams and Engines as well
 * as people.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Getter
@ToString
public enum OwnerType implements Serializable {
    /**
     * The owner's user id is stored in the owner property.
     */
    USER_ID(0, 0, "UserId", "The owner's user id is stored in the owner property."),
    /**
     * The owner's profile unique identifier (guid) is stored in the owner property.
     */
    PROFILE_ID(1, 1, "ProfileId", "The owner's profile unique identifier (guid) is stored in the owner property."),
    /**
     * A different identifier for the owner outside of the scope of open metadata has been used.
     */
    OTHER(99, 99, "Other", "A different identifier for the owner outside of the scope of open metadata has been used.");

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private static final long serialVersionUID = 1L;

    private static final String ENUM_TYPE_GUID = "9548390c-69f5-4dc6-950d-6feeee257b56";

    private static final String ENUM_TYPE_NAME = "AssetOwnerType";

    /**
     * The code for this enum that comes from the Open Metadata Type that this enum represents
     * -- GETTER --
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     * @return openTypeOrdinal int code number
     */
    private final int openTypeOrdinal;

    /**
     * The numeric representation of the enumeration
     * -- GETTER --
     * Return the numeric representation of the enumeration.
     * @return int ordinal
     */
    private final int ordinal;

    /**
     * The default name of the enumeration
     * -- GETTER --
     * Return the default name of the enumeration.
     * @return String name
     */
    private final String name;

    /**
     * The default description of the enumeration
     * -- GETTER --
     * Return the default description of the enumeration.
     * @return String description
     */
    private final String description;

    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal         code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name            default name
     * @param description     default description
     */
    OwnerType(int ordinal, int openTypeOrdinal, String name, String description) {
        this.ordinal = ordinal;
        this.openTypeOrdinal = openTypeOrdinal;
        this.name = name;
        this.description = description;
    }

    /**
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }

}
