/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueNamespace;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ActivityStatus defines the current status for a process.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ActivityStatus implements OpenMetadataEnum
{
    /**
     * The description of the activity has been created and is pending.
     */
    REQUESTED       ("12daebe5-544c-4968-b5f1-583f52ba7f25",  0,  "Requested",  "The description of the activity has been created and is pending.", true),

    /**
     * The activity is approved to run. This means that the mandatory preconditions have been satisfied.
     */
    APPROVED        ("623a7b7a-1f5a-403b-b437-e60e66ffea59",  1,  "Approved",   "The activity is approved to run. This means that the mandatory preconditions have been satisfied.", false),

    /**
     * The activity is waiting for its start time or an actor to claim it.
     */
    WAITING         ("236c4a55-030b-4e78-ab41-ba54921bb11a",  2,  "Waiting",    "The activity is waiting for its start time or an actor to claim it.", false),

    /**
     * The process that will perform the activity is being activated.
     */
    ACTIVATING      ("0f805c52-f05a-44ac-b785-cc705285794a",  3,  "Activating", "The process that will perform the activity is being activated.", false),

    /**
     * The work for the activity is in progress.
     */
    IN_PROGRESS     ("c0568205-7adc-4104-a627-7d584a0cb60b",  4,  "In Progress","The work for the activity is in progress.", false),

    /**
     * The work for the activity has been paused.
     */
    PAUSED          ("a93da12b-2bb8-4dc9-a36c-3b654bcb189b",  5,  "Paused","The work for the activity has been paused.", false),

    /**
     * The work for the activity is in progress.
     */
    FOR_INFO     ("1a98344b-f43f-4bff-9604-323f18a999af",  6,  "For Information","The activity is designed to provide important information to the various actors.", false),

    /**
     * The work for the activity has successfully completed.
     */
    COMPLETED       ("3bc841d3-8e51-475c-9283-ee9c8b7e8670", 10, "Actioned", "The work for the activity has successfully completed.", false),

    /**
     * The activity has not happened because it is not appropriate (for example, created by an automated process as a result of a false positive).
     */
    INVALID         ("6a493851-c089-45a8-877f-c3e98d863c52",  11, "Invalid",    "The activity has not happened because it is not appropriate (for example, created by an automated process as a result of a false positive).", false),

    /**
     * The activity has not been actioned because it is not important, or another activity has superseded it.
     */
    IGNORED         ("5e7c9514-b303-4710-8869-af30b8e10140",  12, "Ignored",    "The activity has not been actioned because it is not important, or another activity has superseded it.",false),

    /**
     * The process that is performing the work (normally an automated process) failed during start up or execution.
     */
    FAILED          ("848d072a-a497-49e9-96c4-af61d3ffbebb",  13, "Failed",     "The process that is performing the work (normally an automated process) failed during start up or execution.", false),

    /**
     * The activity was cancelled by an external caller.
     */
    CANCELLED       ("80ee5577-a0eb-4c18-8b12-0d4f5606851a",  14, "Cancelled",     "The activity was cancelled by an external caller.", false),

    /**
     * The activity was abandoned because it is no longer relevant.
     */
    ABANDONED       ("2911bd8f-4993-4968-b12b-1eade7987093",  15, "Cancelled",     "The activity was abandoned because it is no longer relevant.  Some work may have occurred but is what stopped, probabbly in an inclomple state..", false),

    /**
     * Undefined or user-defined status.
     */
    OTHER           ("1e67023c-4ae4-4f69-b16e-b5e0290641dc", 99, "Other",      "Undefined or user-defined status.", false);

    private static final String ENUM_TYPE_GUID  = "a6e698b0-a4f7-4a39-8c80-db0bb0f972e";
    private static final String ENUM_TYPE_NAME  = "ActivityStatus";

    private static final String ENUM_DESCRIPTION = "Defines the current execution status of a process.";
    private static final String ENUM_DESCRIPTION_GUID = "eb7bba16-b2a0-4e7d-b249-f0be30a50171";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0463_ENGINE_ACTIONS;


    private final int     ordinal;
    private final String  name;
    private final String  description;
    private final String  descriptionGUID;
    private final boolean isDefault;


    /**
     * Default constructor for the enumeration.
     *
     * @param ordinal numerical representation of the enumeration
     * @param descriptionGUID identifier for valid value
     * @param name default string name of the enumeration
     * @param description default string description of the enumeration
     * @param isDefault is this the default value for the enum?
     */
    ActivityStatus(String  descriptionGUID,
                   int     ordinal,
                   String  name,
                   String  description,
                   boolean isDefault)
    {
        this.ordinal = ordinal;
        this.name            = name;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
        this.isDefault = isDefault;
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
     */
    @Override
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
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public static String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Return the description for the open metadata enum type that this enum class represents.
     *
     * @return string description
     */
    public static String getOpenTypeDescription()
    {
        return ENUM_DESCRIPTION;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionGUID()
    {
        return ENUM_DESCRIPTION_GUID;
    }


    /**
     * Return the unique identifier for the valid value element for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public static String getOpenTypeDescriptionWiki()
    {
        return ENUM_DESCRIPTION_WIKI;
    }


    /**
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                null,
                                                name);
    }


    /**
     * Return the namespace for this value.
     *
     * @return string
     */
    public String getNamespace()
    {
        return constructValidValueNamespace(ENUM_TYPE_NAME,
                                            OpenMetadataProperty.ACTIVITY_STATUS.name,
                                            null);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ActivityStatus{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault='" + isDefault + '\'' +
            '}';

    }
}
