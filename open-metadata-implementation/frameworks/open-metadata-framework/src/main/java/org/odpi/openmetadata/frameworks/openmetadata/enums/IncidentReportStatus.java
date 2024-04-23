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
 * IncidentReportStatus defines the status of an incident report.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum IncidentReportStatus
{
    /**
     * The incident report has been raised but no processing has occurred.
     */
    RAISED       ("24b49c38-b1dc-4c2a-9ffb-f475dcfc1568",  0,  "Raised",  "The incident report has been raised but no processing has occurred.", false),

    /**
     * The incident report has been reviewed, possibly classified but no action has been taken.
     */
    REVIEWED        ("174869e9-6741-4302-9229-ce5c8cbd8512",  1,  "Reviewed",   "The incident report has been reviewed, possibly classified but no action has been taken.", false),

    /**
     * The incident report records a valid incident and work is underway to resolve it.
     */
    VALIDATED         ("b646cc66-953b-4797-a177-6b0bd91f6a09",  2,  "Validated",    "The incident report records a valid incident and work is underway to resolve it.", false),

    /**
     * The reported incident has been resolved.
     */
    RESOLVED      ("5987fe10-73bd-493d-8c9d-25bfc600b105",  3,  "Resolved", "The reported incident has been resolved.", false),

    /**
     * The incident report does not describe a valid incident and has been closed.
     */
    INVALID     ("d99a85d6-d6d8-45fa-9e24-01e9d2aed251",  4,  "Invalid","The incident report does not describe a valid incident and has been closed.", false),

    /**
     * The incident report is valid but has been closed with no action.
     */
    IGNORED        ("52013732-b5b4-4b85-98e1-9b58ca1cd710",  10, "Ignored",   "The incident report is valid but has been closed with no action.", false),

    /**
     * "Another incident report status.
     */
    OTHER           ("7555a3f3-9f44-4c7a-9426-51dbb628108e", 99, "Other",      "\"Another incident report status.", false);

    private static final String ENUM_TYPE_GUID  = "a9d4f64b-fa24-4eb8-8bf6-308926ef2c14";
    private static final String ENUM_TYPE_NAME  = "IncidentReportStatus";

    private static final String ENUM_DESCRIPTION = "Defines the status of an incident report.";
    private static final String ENUM_DESCRIPTION_GUID = "f04543aa-3623-4ad6-af4a-cca56e6eaae5";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0470_INCIDENT_REPORTING;

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
    IncidentReportStatus(String  descriptionGUID,
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
                                                OpenMetadataProperty.INCIDENT_STATUS.name,
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
                                           OpenMetadataProperty.INCIDENT_STATUS.name,
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