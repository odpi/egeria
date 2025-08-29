/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.enums;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataWikiPages;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Defines the type of delete method to use.  This is an option on the delete operation
 * of the view services and can be set up as a configuration option in the connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DeleteMethod implements OpenMetadataEnum
{
    /**
     * Archive the element using the Memento classification so it is still available for lineage queries.
     */
    ARCHIVE(0, "733a7c13-0830-4a81-bedc-f317f81e2673", "Archive",
            "Archive the element using the Memento classification so it is still available for lineage queries.", false),

    /**
     * Delete the element from the active store, however it can still be restored if it has been deleted in error.
     */
    SOFT_DELETE    (1, "db2e5c74-bc09-47de-8a80-22d9206de677","SoftDelete",
                       "Delete the element from the active store, however it can still be restored if it has been deleted in error.", false),

    /**
     * If the element has lineage relationships then archive; otherwise soft-delete."
     */
    LOOK_FOR_LINEAGE (2, "eff96544-c730-4abe-b71e-1524b5859183", "LookForLineage",
                      "If the element has lineage relationships then archive; otherwise soft-delete.", true),

    /**
     * Remove the element from the repository; this can not be restored automatically.
     */
    PURGE (3, "51f6c127-a143-4adb-a14a-d6fb2e93109e", "Purge",
           "Remove the element from the repository; this can not be restored automatically.  If you need it back, you need to look in the repository backups.", false),

    /**
     * Another type of delete.
     */
    OTHER             (99, "332853a9-af08-4210-a9dd-48e40db5ee09","Other",
                       "Another type of delete.", false),
    ;


    private static final String ENUM_TYPE_GUID  = "6d1facbf-fd37-457f-8282-4dd824a9658d";
    private static final String ENUM_TYPE_NAME  = "DeleteMethod";

    private static final String ENUM_DESCRIPTION = "Defines the type of delete function to use.";
    private static final String ENUM_DESCRIPTION_GUID = "d26f371a-812b-42d8-b983-b7c9545ddd0d";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0464_INTEGRATION_GROUPS;

    private final int    ordinal;
    private final String name;
    private final String description;

    private final String descriptionGUID;

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
    DeleteMethod(int     ordinal,
                 String  descriptionGUID,
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DeleteMethod{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault='" +isDefault + '\'' +
            '}';

    }
}
