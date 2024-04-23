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
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueCategory;
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * EngineActionStatus defines the current status for a engine action.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum EngineActionStatus
{
    /**
     * The engine action has been created and is pending.
     */
    REQUESTED       ("12daebe5-544c-4968-b5f1-583f52ba7f25",  0,  "Requested",  "The engine action has been created and is pending.", true),

    /**
     * The engine action is approved to run. This means that the mandatory guards have been satisfied.
     */
    APPROVED        ("623a7b7a-1f5a-403b-b437-e60e66ffea59",  1,  "Approved",   "The engine action is approved to run.  This means that the mandatory guards have been satisfied.", false),

    /**
     * The engine action is waiting for its start time or the engine host to claim it.
     */
    WAITING         ("236c4a55-030b-4e78-ab41-ba54921bb11a",  2,  "Waiting",    "The engine action is waiting for its start time or the right conditions to run.", false),

    /**
     * The governance service for the engine action is being initialized in the governance engine.
     */
    ACTIVATING      ("0f805c52-f05a-44ac-b785-cc705285794a",  3,  "Activating", "The governance service for the engine action is being initialized in the governance engine.", false),

    /**
     * The governance engine is running the associated governance service for the engine action.
     */
    IN_PROGRESS     ("c0568205-7adc-4104-a627-7d584a0cb60b",  4,  "In Progress","The governance engine is running the associated governance service for the engine action.", false),

    /**
     * The governance service for the engine action has successfully completed processing.
     */
    ACTIONED        ("3bc841d3-8e51-475c-9283-ee9c8b7e8670",  10, "Actioned",   "The governance service for the engine action has successfully completed processing.", false),

    /**
     * The engine action has not been run because it is not appropriate (for example, a false positive).
     */
    INVALID         ("6a493851-c089-45a8-877f-c3e98d863c52",  11, "Invalid",    "The engine action has not been run because it is not appropriate (for example, a false positive).", false),

    /**
     * The engine action has not been run because a different engine action was chosen.
     */
    IGNORED         ("5e7c9514-b303-4710-8869-af30b8e10140",  12, "Ignored",    "The engine action has not been run because a different engine action was chosen.",false),

    /**
     * The governance service for the engine action failed to execute.
     */
    FAILED          ("848d072a-a497-49e9-96c4-af61d3ffbebb",  13, "Failed",     "The governance service for the engine action failed to execute.", false),

    /**
     * The engine action was cancelled by an external caller.
     */
    CANCELLED       ("80ee5577-a0eb-4c18-8b12-0d4f5606851a",  14, "Cancelled",     "The engine action was cancelled by an external caller.", false),

    /**
     * Undefined or unknown engine action status.
     */
    OTHER           ("1e67023c-4ae4-4f69-b16e-b5e0290641dc", 99, "Other",      "Undefined or unknown engine action status.", false);

    private static final String ENUM_TYPE_GUID  = "a6e698b0-a4f7-4a39-8c80-db0bb0f972e";
    private static final String ENUM_TYPE_NAME  = "EngineActionStatus";

    private static final String ENUM_DESCRIPTION = "Defines the current execution status of an engine action.";
    private static final String ENUM_DESCRIPTION_GUID = "eb7bba16-b2a0-4e7d-b249-f0be30a50171";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0463_ENGINE_ACTIONS;

    private final String descriptionGUID;

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
    EngineActionStatus(String  descriptionGUID,
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
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the enumeration.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the enumeration.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return  guid
     */
    public  String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
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
                                                OpenMetadataProperty.ACTION_STATUS.name,
                                                null,
                                                name);
    }


    /**
     * Return the category for this value.
     *
     * @return string
     */
    public String getCategory()
    {
        return constructValidValueCategory(ENUM_TYPE_NAME,
                                           OpenMetadataProperty.ACTION_STATUS.name,
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
        return "EngineActionStatus{" + name + "}";
    }
}