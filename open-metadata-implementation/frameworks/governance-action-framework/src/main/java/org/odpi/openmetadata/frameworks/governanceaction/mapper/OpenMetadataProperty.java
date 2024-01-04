/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.mapper;

/**
 * Provides the definition for all properties defined in the open metadata types.
 */
public enum OpenMetadataProperty
{
    /**
     * Unique identifier of an open metadata entity or relationship.
     */
    GUID ("guid", "string", "Unique identifier of an open metadata entity or relationship.", null),

    /**
     * Timestamp when the archive occurred or was detected.
     */
    ARCHIVE_DATE ("archiveDate", "string", "Timestamp when the archive occurred or was detected.", "5235c1b1-fa03-4d17-80e2-7826242dfc75"),

    /**
     * Name of user that performed the archive - or detected the archive.
     */
    ARCHIVE_USER ("archiveUser", "string", "Name of user that performed the archive - or detected the archive.", "fe81b808-3d78-4e84-a1de-b0b273a89bec"),

    /**
     * Name of process that performed the archive - or detected the archive.
     */
    ARCHIVE_PROCESS ("archiveProcess", "string", "Name of process that performed the archive - or detected the archive.", "0aef1b3a-50dc-4a6c-981f-93bf8815e7f4"),

    /**
     * Name of service that created this classification.
     */
    ARCHIVE_SERVICE ("archiveService", "string", "Name of service that created this classification.", "c2170958-998e-4d8c-8e33-e91ef4872c8a"),

    /**
     * Name of method that created this classification.
     */
    ARCHIVE_METHOD ("archiveMethod", "string", "Name of method that created this classification.", "45d558ba-00b4-4a67-9226-590b100da7e9"),

    /**
     * Properties to locate the real-world counterpart in the archive.
     */
    ARCHIVE_PROPERTIES ("archiveProperties", "string", "Properties to locate the real-world counterpart in the archive.", "d149d2d8-f181-4c20-8d68-5460f54bece5"),

    /**
     * The unique identifier of the referenceable that this element is directly or indirectly anchored to.
     */
    ANCHOR_GUID ("anchorGUID", "string", "The unique identifier of the referenceable that this element is directly or indirectly anchored to.", "e7de4efc-6fa4-4942-97eb-e9b5be701875"),

    /**
     * Unique name of the type of the anchor.
     */
    ANCHOR_TYPE_NAME ("anchorTypeName", "string", "Unique name of the type of the anchor.", "605c9dd5-fa79-4457-b299-9169c5567f97"),

    /**
     * Unique identifier for the entity.
     */
    QUALIFIED_NAME ("qualifiedName", "string", "Unique identifier for the entity.", "e31e5b9b-0f96-42a9-8e67-0e3fc66ad305"),

    /**
     * Additional properties for the element.
     */
    ADDITIONAL_PROPERTIES ("additionalProperties", "map<string,string>", "Additional properties for the element.", "534b5665-73d4-4bdc-b83b-1a8fed19dba3"),

    /**
     * Unique identifier for the entity.
     */
    NAME ("name", "string", "Name that the element is known as - not necessarily unique.", "c075e0e7-8ecc-4f81-9ac4-ca3662c3ebe4"),

    /**
     * Display name of the element used for summary tables and titles
     */
    DISPLAY_NAME ( "displayName", "string", "Display name of the element used for summary tables and titles.", "a42ceece-6cd8-4c71-9920-e3c96df3c8bb"),

    /**
     * Description of the element or associated resource in free-text.
     */
    DESCRIPTION ("description", "string", "Description of the element or associated resource in free-text.", "ee09b6f9-e15b-40fb-9799-ef3b98c1de2c"),

    /**
     * Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.
     */
    VERSION_IDENTIFIER ( "versionIdentifier", "string", "Version identifier to allow different versions of the same resource to appear in the catalog as separate assets.", "6e765a3f-04c2-4eca-bd2d-519cae777c03"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    DEPLOYED_IMPLEMENTATION_TYPE ( "deployedImplementationType", "string", "Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.", "2f71cd9f-c614-4531-a5ae-3bcb6a6a1918"),

    /**
     * Name of a particular type of technology.  It is more specific than the open metadata types and increases the precision in which technology is catalogued.  This helps human understanding and enables connectors and other actions to be targeted to the right technology.
     */
    @Deprecated
    TYPE ( "type", "string", "Deprecated property, use deployedImplementationType.", "2f71cd9f-c614-4531-a5ae-3bcb6a6a1918"),

    /**
     * Identifier of the person or process who is accountable for the proper management of the element or related resource.
     */
    OWNER ( "owner", "string", "Identifier of the person or process who is accountable for the proper management of the element or related resource.", "8b3e49d6-987c-4f2f-b624-ba91e5d28b33"),

    /**
     * Type of identifier used for owner property.
     */
    OWNER_TYPE("ownerType", "AssetOwnerType","Type of identifier used for owner property.", "142b2bbf-0f2f-4c1e-b10e-850cd208b31c"),

    /**
     * The list of governance zones that this asset belongs to.
     */
    ZONE_MEMBERSHIP ("zoneMembership","array<string>", "The list of governance zones that this asset belongs to.", "2af69520-6991-4097-aa94-543127b73066"),

    /**
     * Description of the last change to the asset's metadata.
     */
    LATEST_CHANGE ("latestChange", "string", "Description of the last change to the asset's metadata.", "756cf128-12e5-456e-97d7-843e7efec11b"),

    /**
     * Formula that describes the behaviour of the element.  May include placeholders for variables
     */
    FORMULA ( "formula", "string", "Formula that describes the behaviour of the element.  May include placeholders for variables.", "0b35fe46-1c16-4af7-b7b1-ea6951718dc7"),

    /**
     * Format of the expression provided in the formula attribute.
     */
    FORMULA_TYPE ( "formulaType", "string", "Format of the expression provided in the formula attribute.", "dd9b24cb-0359-4915-8b6b-9eae251adc1c"),

    /**
     * Is this element visible to all, or only the author?
     */
    IS_PUBLIC ( "isPublic", "boolean", "Is this element visible to all, or only the author?", "e7a8eaa8-4358-4dd3-8d82-2ab3c118cfe4"),

    /**
     * Description of the technique used to create the sample.
     */
    SAMPLING_METHOD ( "samplingMethod", "string", "Description of the technique used to create the sample.", "1cb00437-adde-4b71-a655-f736f2c8989d"),


    CHANGE_TARGET  ( "changeTarget", "LatestChangeTarget", "The relationship of element that has been changed to the anchor.", "b3a4c3c0-f17f-4380-848a-6c6d985c2dd3"),
    CHANGE_ACTION  ( "changeAction", "LatestChangeAction", "The type of change.", "365c4667-b86a-445f-860b-dc35eac917f2"),
    CLASSIFICATION_NAME ( "classificationName", "string", "The name of the associated classification.", "ef2169c4-c8f3-48b0-9051-cfb2bbb1e5f2"),
    CLASSIFICATION_PROPERTY_NAME ( "classificationPropertyName", "string", "Name of the property in the classification where this value is used.", "5471ec69-a23d-4d89-b134-b30a8a01f435"),
    ATTACHMENT_GUID  ( "attachmentGUID", "string", "If an attached entity or relationship to it changed, this is its unique identifier.", "74d62753-fd42-47c5-a804-29334f394728"),
    ATTACHMENT_TYPE  ( "attachmentType", "string", "If an attached entity or relationship to changed, this is its unique type name.", "b7f9f49a-e227-44d8-ae26-7e9c4f552379"),
    RELATIONSHIP_TYPE  ( "relationshipType", "string", "If an attached entity or relationship to changed, this is its unique type name of the relationship.", "afa67874-fa00-422e-8902-69e5e3cc9e43"),
    USER  ( "user", "string", "The user identifier for the person/system making the change.", "1afa1bb1-a9f0-4739-a45a-ff0d01b867f5"),
    ACTION_DESCRIPTION ( "actionDescription", "string", "Description of the activity.", "2f0a4467-7b72-4dba-a345-d80959d8a3d5"),


    TEMPLATE_NAME( "templateName" , "string", "The display name for the template to help requester choose the template to use.", "32c4a43e-cc81-4d85-af2e-97bd492a8893"),
    TEMPLATE_DESCRIPTION ("templateDescription", "string","The description of the template to help requester choose the template to use.", "1c537e66-a43e-42bc-94f7-055af7696c86" ),
    REPLACEMENT_PROPERTIES ( "replacementProperties", "map<string,string>", "Map of attribute names to values that should be replaced in the template.  These attributes map to the root entity of the template, or, the first occurrence of the attribute in attached relationships, classifications or entities.", "e4bc87ae-b15d-4fd6-ba33-2ed85f699244"),
    PLACEHOLDER_PROPERTIES( "placeholderProperties", "map<string,string>", "Map of placeholder names to values that should be replaced in the template.", "f16227bb-61d7-4ea9-947c-a0415b541542"),

    SOURCE_VERSION_NUMBER ( "sourceVersionNumber", "long", "The version number of the template element when the copy was created.", null ),

    ;


    public final String name;
    public final String type;
    public final String description;
    public final String descriptionGUID;


    /**
     * Constructor.
     *
     * @param name property name
     * @param type property type
     * @param description property description
     * @param descriptionGUID unique identifier of the valid value describing this property
     */
    OpenMetadataProperty(String name,
                         String type,
                         String description,
                         String descriptionGUID)
    {
        this.name = name;
        this.type = type;
        this.description = description;
        this.descriptionGUID = descriptionGUID;
    }
}
