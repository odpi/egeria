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
import static org.odpi.openmetadata.frameworks.openmetadata.mapper.OpenMetadataValidValues.constructValidValueQualifiedName;

/**
 * ContactMethodType specifies the contact mechanism to use to contact an individual.
 * <ul>
 *     <li>EMAIL</li>
 *     <li>PHONE</li>
 *     <li>CHAT</li>
 *     <li>PROFILE</li>
 *     <li>ACCOUNT</li>
 *     <li>OTHER</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ContactMethodType implements OpenMetadataEnum
{
    /**
     * Send email.
     */
    EMAIL     (0, "9ae0f271-e59b-4212-a123-0cb2e431a4f5",  "Email",    "Send email.", false),

    /**
     * Call by phone.
     */
    PHONE     (1, "23c948f2-a878-4827-ad9f-d189df16bf34",  "Phone",    "Call by phone.", false),

    /**
     * Send chat message.
     */
    CHAT      (2, "472de896-f9b2-48a9-aed6-728e9006a662",  "Chat",     "Send chat message.", false),

    /**
     * Send comment to personal profile.
     */
    PROFILE   (3, "d6df84bb-96e7-40c6-926f-3728644912c7",  "Profile",  "Send comment to personal profile.", false),

    /**
     * Send comment to a social media account.
     */
    ACCOUNT   (4, "dada5489-a1cb-433e-a233-6b481ee93edb",  "Account",  "Send comment to a social media account.", false),

    /**
     * Another contact mechanism.
     */
    OTHER     (99,"b9e40635-5fa7-4c5d-89e2-ecd77ba4fbab", "Other",    "Another contact mechanism.", false);


    private static final String ENUM_TYPE_GUID  = "30e7d8cd-df01-46e8-9247-a24c5650910d";
    private static final String ENUM_TYPE_NAME  = "ContactMethodType";

    private static final String ENUM_DESCRIPTION = "Type of mechanism to contact an actor.";
    private static final String ENUM_DESCRIPTION_GUID = "f1a4633e-2890-4952-bef2-1ba47d73f885";
    private static final String ENUM_DESCRIPTION_WIKI = OpenMetadataWikiPages.MODEL_0110_ACTORS;

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
    ContactMethodType(int     ordinal,
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
     * Return the qualified name for this value.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return constructValidValueQualifiedName(ENUM_TYPE_NAME,
                                                OpenMetadataProperty.CONTACT_METHOD_TYPE.name,
                                                null,
                                                name);
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "ContactMethodType{" +
            "ordinal=" + ordinal +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", descriptionGUID='" + descriptionGUID + '\'' +
            ", isDefault='" + isDefault + '\'' +
            '}';

    }
}
