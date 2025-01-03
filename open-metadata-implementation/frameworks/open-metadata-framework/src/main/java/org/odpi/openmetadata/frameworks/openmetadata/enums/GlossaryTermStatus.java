/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The GlossaryTermStatus defines the status of a glossary term.  It effectively
 * defines its visibility to different types of queries.  Most queries by default will only return instances in the
 * active status.
 * <ul>
 *     <li>Unknown: Unknown instance status.</li>
 *     <li>Draft: The content is incomplete.</li>
 *     <li>Proposed: The content is in review.</li>
 *     <li>Approved: The content is approved.</li>
 *     <li>Active: The instance is approved and in use.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum GlossaryTermStatus implements OpenMetadataEnum
{
    /**
     * Draft - The term is incomplete.
     */
    DRAFT     (1,1,  "Draft",     "The term is incomplete.", "bb1b89c9-52b2-404d-ad3a-3f7c3e996d45"),

    /**
     * Prepared - The term is ready for review.
     */
    PREPARED  (2,2,  "Prepared",  "The term is ready for review.", "af441f31-59d2-4126-bcc0-e4ab32fe8f61"),

    /**
     * Proposed - The term is in review.
     */
    PROPOSED  (3,3,  "Proposed",  "The term is in review.", "e62e7283-4d06-4225-81d2-c8d885256ee1"),

    /**
     * Approved - The term is approved and ready to be activated.
     */
    APPROVED  (4,4,  "Approved",  "The term is approved and ready to be activated.", "243e69c8-1dc8-4a1d-9086-2dedfc25b214"),

    /**
     * Rejected - The term is rejected and should not be used.
     */
    REJECTED  (5,5,  "Rejected",  "The term is rejected and should not be used.", "f71e90f6-27b0-490b-84ec-c8f5c9ddd707"),

    /**
     * Active - The term is approved and in use.
     */
    ACTIVE    (6,15, "Active",    "The term is approved and in use.", "373b8d0b-1797-4ff1-b1a1-1b3cba8f9ab9"),

    /**
     * Deprecated - The term is out of date and should not be used.
     */
    DEPRECATED(7,30, "Deprecated","The term is out of date and should not be used.", "81f3626f-2fd2-4bc1-bb82-698289fbd18e"),

    /**
     * Other - The term is in a locally defined state.
     */
    OTHER     (8,50, "Other",     "The term is in a locally defined state.", "ce8bd25c-0ebb-4191-9769-af6e006b80ad"),
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
    GlossaryTermStatus(int    ordinal,
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
