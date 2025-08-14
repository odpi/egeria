/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OpenMetadataEnum;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * A specifies common values for the assignmentType attribute of
 * the AssignmentScope relationship.
 * <ul>
 *     <li>CONTRIBUTOR - Individual is able to be a contributing member of the initiative.  This is the default.</li>
 *     <li>ADMINISTRATOR - Individual is able to administer the initiative.</li>
 *     <li>LEADER - Individual leads the activity of the initiative.</li>
 *     <li>OWNER - Individual sets the direction of the initiative.</li>
 *     <li>OBSERVER - Individual is observing the work of the initiative, but has no special responsibilities.</li>
 *     <li>SPONSOR - Individual is funding/supporting the work of the initiative.</li>
 *     <li>OTHER - Another meaning.</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AssignmentType implements OpenMetadataEnum
{
    CONTRIBUTOR   (0,  "959cae9b-218f-4254-8a79-721d568372bd",  "Contributor",   "Individual is able to be a contributing member of the initiative.", true),
    ADMINISTRATOR (1,  "35201771-3ca2-4b7a-9dd1-77aa0224abe8",  "Administrator", "Individual is able to administer the initiative.", false),
    LEADER        (2,  "d4f5dfdc-39d6-47c1-8dfb-077b5f7244d7",  "Leader",        "Individual leads the activity of the initiative.", false),
    OWNER         (3,  "6abc57c6-1979-4265-852e-ad8b782a3cb5",  "Owner",        "Individual sets the direction of the initiative.", false),
    OBSERVER      (4,  "c1bd838c-f5be-4464-aee9-bcf0ff07ec32",  "Observer",      "Individual is observing the  work of the initiative, but has no special responsibilities.", false),
    SPONSOR       (5,  "9549988d-5a40-4734-ba72-4d6cf6e1fffb",  "Sponsor",      "Individual is funding/supporting the work of the initiative.", false),
    OTHER         (99, "1c9fd81c-d22a-4aca-b2f3-469088ff7887",  "Other",         "Another meaning.", false);


    private final String         descriptionGUID;
    private final int            ordinal;
    private final String         name;
    private final String         description;
    private final boolean        isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    AssignmentType(int     ordinal,
                   String  descriptionGUID,
                   String  name,
                   String  description,
                   boolean isDefault)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault       = isDefault;
    }



    /**
     * Return the numeric representation of the enumeration.
     *
     * @return int ordinal
     */
    @Override
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */@Override
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    @Override
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    @Override
    public  String getDescriptionGUID()
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
        return isDefault;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "AssignmentType : " + name;
    }
}
