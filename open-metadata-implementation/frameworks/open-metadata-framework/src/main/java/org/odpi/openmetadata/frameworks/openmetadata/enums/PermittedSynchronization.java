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
 * PermittedSynchronization defines the permitted directions of flow of metadata updates between open metadata and a third party
 * technology.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum PermittedSynchronization implements OpenMetadataEnum
{
    /**
     * Both Directions - Metadata exchange is permitted in both directions.  Synchronization is halted on a specific
     * element if potentially clashing updates have occurred both in the third party technology and
     * open metadata.  Such conflicts are logged on the audit log and resolved through manual stewardship.
     */
    BOTH_DIRECTIONS   (0, "3ecbbab2-8c60-49be-97cd-be1d0bca49e9","Both Directions",
                       "Metadata exchange is permitted in both directions.  Synchronization is halted on a specific " +
                               "element if potentially clashing updates have occurred both in the third party technology and " +
                               "open metadata.  Such conflicts are logged on the audit log and resolved through manual stewardship.", true),

    /**
     * To Third Party - The third party technology is logically downstream of open metadata.  This means the open metadata
     * ecosystem is the originator and owner of the metadata being synchronized. Any updates detected
     * in the third technology are overridden by the latest open metadata values.
     */
    TO_THIRD_PARTY    (1, "7fe0046d-8b1a-4931-b9b7-18279c6f449a","To Third Party",
                       "The third party technology is logically downstream of open metadata.  This means the open metadata " +
                                  "ecosystem is the originator and owner of the metadata being synchronized. Any updates detected " +
                                  "in the third technology are overridden by the latest open metadata values.", false),

    /**
     * From Third Party - The third party technology is logically upstream (the originator and owner of the metadata).
     * Any updates made in open metadata are not passed to the third party technology and the
     * third party technology is requested to refresh the open metadata version.
     */
    FROM_THIRD_PARTY  (2, "9ed53b29-f843-4af2-b510-44683d84b2d0","From Third Party",
                       "The third party technology is logically upstream (the originator and owner of the metadata).  " +
                                  "Any updates made in open metadata are not passed to the third party technology and the " +
                                  "third party technology is requested to refresh the open metadata version.", false),

    /**
     * Other - Another type of synchronization rule - see description property.
     */
    OTHER             (99, "332853a9-af08-4210-a9dd-48e40db5ee09","Other",
                       "Another type of synchronization rule - see description property.", false),
    ;


    private static final String ENUM_TYPE_GUID  = "973a9f4c-93fa-43a5-a0c5-d97dbd164e78";
    private static final String ENUM_TYPE_NAME  = "PermittedSynchronization";

    private static final String ENUM_DESCRIPTION = "Defines the permitted direction of exchange of metadata with a third party technology.";
    private static final String ENUM_DESCRIPTION_GUID = "c6233ba5-1438-4391-90c5-569d1f54746e";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0017_EXTERNAL_IDENTIFIERS;

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
    PermittedSynchronization(int     ordinal,
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
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PermittedSynchronization{ name=" + name + " }";
    }
}