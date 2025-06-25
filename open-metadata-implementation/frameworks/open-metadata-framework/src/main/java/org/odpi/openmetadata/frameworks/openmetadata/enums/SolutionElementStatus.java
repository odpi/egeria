/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The SolutionElementStatus defines the status of a solution blueprint, solution component and solution port.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum SolutionElementStatus implements OpenMetadataEnum
{
    /**
     * Draft - The solution element is incomplete.
     */
    DRAFT     (1,1,  "Draft",     "The solution element is incomplete.", "7f4c0690-7234-4116-a03f-53a55f396baf"),

    /**
     * Prepared - The solution element is ready for review.
     */
    PREPARED  (2,2,  "Prepared",  "The solution element is ready for review.", "00534655-09df-427d-9803-e3e4caddb36f"),

    /**
     * Proposed - The solution element is in review.
     */
    PROPOSED  (3,3,  "Proposed",  "The solution element is in review.", "b521291a-6ad0-4117-b995-09b8051e0800"),

    /**
     * Approved - The solution element is approved and ready to be activated.
     */
    APPROVED  (4,4,  "Approved",  "The solution element is approved and ready to be activated.", "21beadc1-d449-4d05-bf91-0bc6c0695b01"),

    /**
     * Rejected - The solution element is rejected and should not be used.
     */
    REJECTED  (5,5,  "Rejected",  "The solution element is rejected and should not be used.", "bcca8de2-d19c-4ddd-8bf5-0843e6d31565"),

    /**
     * Active - The solution element is approved and in use.
     */
    ACTIVE    (6,15, "Active",    "The solution element is approved and in use.", "41abeef0-c748-43ab-83b9-baaa3d702fdc"),

    /**
     * Disabled - The instance is shutdown or disabled.
     */
    DISABLED  (7,21, "Disabled",  "The solution element is shutdown or disabled.", "5e5f6724-0c49-4d7e-926a-206178e320f3"),

    /**
     * Deprecated - The solution element is out of date and should not be used.
     */
    DEPRECATED(8,30, "Deprecated","The solution element is out of date and should not be used.", "fe66b613-495f-488f-890f-dc05d7240897"),

    /**
     * Other - The solution element is in a locally defined state.
     */
    OTHER     (9,50, "Other",     "The solution element is in a locally defined state.", "af6085ae-85f1-4529-9790-cc31f9d73c75"),
    ;


    private final int    ordinal;
    private final int    openTypeOrdinal;
    private final String name;
    private final String description;
    private final String descriptionGUID;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     * @param descriptionGUID unique identifier of the valid value
     */
    SolutionElementStatus(int    ordinal,
                          int    openTypeOrdinal,
                          String name,
                          String description,
                          String descriptionGUID)
    {
        this.ordinal         = ordinal;
        this.openTypeOrdinal = openTypeOrdinal;
        this.name            = name;
        this.description     = description;
        this.descriptionGUID = descriptionGUID;
    }

    /**
     * Return the code for this enum instance
     *
     * @return int key pattern code
     */
    @Override
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return openTypeOrdinal;
    }



    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    @Override
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the key pattern for this enum instance.
     *
     * @return String default description
     */
    @Override
    public String getDescription()
    {
        return description;
    }

    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }

    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return false;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "GlossaryTermStatus{" +
                       "ordinal=" + ordinal +
                       ", openTypeOrdinal=" + openTypeOrdinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }}
