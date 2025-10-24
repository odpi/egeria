/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.productmanager.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

/**
 * The ProductDataFieldDefinition contains the data fields used to populate the open metadata digital product
 * data dictionary and data specs.
 */
public enum ProductDataFieldDefinition
{
    /**
     * Globally Unique Identifier
     */
    GUID("Globally Unique Identifier (GUID)",
         "GUID",
         OpenMetadataProperty.GUID.description,
         OpenMetadataProperty.GUID.dataType,
         null,
         null,
         true,
         false),

    /**
     * Element Create Time
     */
    CREATE_TIME("Element Create Time",
                "Create Time",
                OpenMetadataProperty.CREATE_TIME.description,
                OpenMetadataProperty.CREATE_TIME.dataType,
                null,
                "milliseconds",
                false,
                false),

    /**
     * Element Last Update Time
     */
    UPDATE_TIME("Element Last Update Time",
                "Update Time",
                OpenMetadataProperty.UPDATE_TIME.description,
                OpenMetadataProperty.UPDATE_TIME.dataType,
                null,
                "milliseconds",
                false,
                false),

    /**
     * Qualified Name
     */
    QUALIFIED_NAME("Qualified Name",
                   "Qualified Name",
                   OpenMetadataProperty.QUALIFIED_NAME.description,
                   OpenMetadataProperty.QUALIFIED_NAME.dataType,
                   null,
                   null,
                   false,
                   false),

    /**
     * Display Name
     */
    DISPLAY_NAME("Qualified Name",
                 "Qualified Name",
                 OpenMetadataProperty.DISPLAY_NAME.description,
                 OpenMetadataProperty.DISPLAY_NAME.dataType,
                 null,
                 null,
                 false,
                 true),

    /**
     * Description
     */
    DESCRIPTION("Description",
                "Description",
                OpenMetadataProperty.DESCRIPTION.description,
                OpenMetadataProperty.DESCRIPTION.dataType,
                null,
                null,
                false,
                true),

    /**
     * Category
     */
    CATEGORY("Category",
             "Category",
             OpenMetadataProperty.CATEGORY.description,
             OpenMetadataProperty.CATEGORY.dataType,
             null,
             null,
             false,
             true),

    /**
     * Identifier
     */
    IDENTIFIER("Identifier",
               "Identifier",
               OpenMetadataProperty.IDENTIFIER.description,
               OpenMetadataProperty.IDENTIFIER.dataType,
               null,
               null,
               false,
               true),

    /**
     * Element Status
     */
    ELEMENT_STATUS("Element Status",
                   "Element Status",
                   OpenMetadataProperty.CURRENT_STATUS.description,
                   OpenMetadataProperty.CURRENT_STATUS.dataType,
                   null,
                   null,
                   false,
                   false),

    /**
     * Sync Time
     */
    SYNC_TIME("Sync Time",
              "Sync Time",
              "The type when a monitoring product published its insight report.",
              DataType.DATE,
              null,
              null,
              true,
              false),


    /**
     * Subtypes
     */
    OPEN_METADATA_SUBTYPES("Open Metadata Subtypes",
                           "Open Metadata Subtypes",
                           "A list of subtypes for the open metadata type",
                           DataType.ARRAY_STRING,
                           null,
                           null,
                           false,
                           true),

    /**
     * Open Metadata Super Types
     */
    OPEN_METADATA_SUPER_TYPES("Open Metadata Super Types",
                              "Open Metadata Super Types",
                              "A list of the types that this open metadata type inherits from.",
                              DataType.ARRAY_STRING,
                              null,
                              null,
                              false,
                              true),

    /**
     * Open Metadata Type Name
     */
    OPEN_METADATA_TYPE_NAME("Open Metadata Type Name",
                            "Open Metadata Type",
                            "The unique name of an open metadata type.",
                            DataType.STRING,
                            null,
                            null,
                            false,
                            true),


    /**
     * Open Metadata Type Name
     */
    OPEN_METADATA_TYPE_GUID("Open Metadata Type GUID",
                            "Open Metadata Type GUID",
                            "The unique identifier of an open metadata type.",
                            DataType.STRING,
                            null,
                            null,
                            false,
                            true),

    /**
     * Open Metadata Attribute Name
     */
    OPEN_METADATA_ATTRIBUTE_NAME("Open Metadata Attribute Name",
                                 "Open Metadata Attribute Name",
                                 "The unique name of an open metadata attribute.",
                                 DataType.STRING,
                                 null,
                                 null,
                                 false,
                                 true),

    /**
     * The url link to documentation.
     */
    WIKI_LINK("Wiki URL Link",
              "Wiki Link",
              "The url link to documentation.",
              DataType.STRING,
              null,
              null,
              false,
              true),

    /**
     * The name of the properties bean class to use in REST API calls.
     */
    BEAN_CLASS_NAME("Bean Class Name",
                    "Bean Class Name",
                    "The name of the properties bean class to use in REST API calls.",
                    DataType.STRING,
                    null,
                    null,
                    false,
                    true),

    /**
     * The name of the properties bean class to use in REST API calls.
     */
    LOCATION_CLASSIFICATION_NAME("Location Classification Name",
                                 "Location Classification Name",
                                 "The classification assigned to the location that describes the type of location.",
                                 DataType.STRING,
                                 null,
                                 null,
                                 false,
                                 true),

    /**
     * The unique identifier of a location.
     */
    LOCATION_GUID("Location GUID",
                  "Location GUID",
                  "The unique identifier of a location.",
                  DataType.STRING,
                  null,
                  null,
                  true,
                  false),

    /**
     * The coordinates of a fixed location.
     */
    LOCATION_COORDINATES("Location Coordinates",
                         "Coordinates",
                         OpenMetadataProperty.COORDINATES.description,
                         OpenMetadataProperty.COORDINATES.dataType,
                         null,
                         null,
                         false,
                         true),

    /**
     * The map projection of a fixed location.
     */
    LOCATION_MAP_PROJECTION("Location Map Projection",
                            "Map Projection",
                            OpenMetadataProperty.MAP_PROJECTION.description,
                            OpenMetadataProperty.MAP_PROJECTION.dataType,
                            null,
                            null,
                            false,
                            true),

    /**
     * The postal address of a fixed location.
     */
    LOCATION_POSTAL_ADDRESS("Location Postal Address",
                            "Postal Address",
                            OpenMetadataProperty.POSTAL_ADDRESS.description,
                            OpenMetadataProperty.POSTAL_ADDRESS.dataType,
                            null,
                            null,
                            false,
                            true),

    /**
     * The postal address of a fixed location.
     */
    NETWORK_ADDRESS("Network Address",
                    "Network Address",
                    OpenMetadataProperty.NETWORK_ADDRESS.description,
                    OpenMetadataProperty.NETWORK_ADDRESS.dataType,
                    null,
                    null,
                    false,
                    true),

    /**
     * Namespace
     */
    NAMESPACE("Namespace",
              "Namespace",
              OpenMetadataProperty.NAMESPACE.description,
              OpenMetadataProperty.NAMESPACE.dataType,
              null,
              null,
              false,
              true),


    /**
     * Preferred Value
     */
    PREFERRED_VALUE("Preferred Value",
                    "Preferred Value",
                    OpenMetadataProperty.PREFERRED_VALUE.description,
                    OpenMetadataProperty.PREFERRED_VALUE.dataType,
                    null,
                    null,
                    false,
                    true),

    /**
     * Is Case Sensitive?
     */
    IS_CASE_SENSITIVE("Is Case Sensitive?",
                      "Is Case Sensitive",
                      OpenMetadataProperty.IS_CASE_SENSITIVE.description,
                      OpenMetadataProperty.IS_CASE_SENSITIVE.dataType,
                      null,
                      null,
                      false,
                      true),


    /**
     * Is Nullable?
     */
    IS_NULLABLE("Is Nullable?",
                "Is Nullable",
                OpenMetadataProperty.IS_NULLABLE.description,
                OpenMetadataProperty.IS_NULLABLE.dataType,
                null,
                null,
                false,
                true),


    /**
     * Data Type
     */
    DATA_TYPE("Data Type",
              "Data Type",
              OpenMetadataProperty.DATA_TYPE.description,
              OpenMetadataProperty.DATA_TYPE.dataType,
              DataType.STRING.getName(),
              null,
              false,
              true),


    /**
     * Scope
     */
    SCOPE("Scope",
          "Scope",
          OpenMetadataProperty.SCOPE.description,
          OpenMetadataProperty.SCOPE.dataType,
          null,
          null,
          false,
          true),


    /**
     * Usage
     */
    USAGE("Usage",
          "Usage",
          OpenMetadataProperty.USAGE.description,
          OpenMetadataProperty.USAGE.dataType,
          null,
          null,
          false,
          true),

    /**
     * Is Default Value?
     */
    IS_DEFAULT_VALUE("Is Default Value?",
                     "Is Default Value",
                     OpenMetadataProperty.IS_DEFAULT_VALUE.description,
                     OpenMetadataProperty.IS_DEFAULT_VALUE.dataType,
                     null,
                     null,
                     false,
                     true),


    ;

    private final String   displayName;
    private final String   namePattern;
    private final String   description;
    private final DataType dataType;
    private final String   defaultValue;
    private final String   units;
    private final boolean  isIdentifier;
    private final boolean  isNullable;


    /**
     * The constructor creates an instance of the enum
     *
     * @param displayName   display name
     * @param namePattern   technical name
     * @param description   description of the use of this field
     * @param dataType type for this field
     * @param defaultValue default value to use if not specified
     * @param units units that are used in numeric fields
     * @param isIdentifier is this field used as a row/record/object identifier?
     * @param isNullable is this field nullable
     */
    ProductDataFieldDefinition(String   displayName,
                               String   namePattern,
                               String   description,
                               DataType dataType,
                               String   defaultValue,
                               String   units,
                               boolean  isIdentifier,
                               boolean  isNullable)
    {
        this.displayName  = displayName;
        this.namePattern  = namePattern;
        this.description  = description;
        this.dataType     = dataType;
        this.defaultValue = defaultValue;
        this.units        = units;
        this.isIdentifier = isIdentifier;
        this.isNullable   = isNullable;
    }


    /**
     * Return the unique name of this element.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return "DataField::OpenMetadataDigitalProducts::" + displayName;
    }


    /**
     * Return the display name for this field
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the technical name used for this data field.
     *
     * @return string
     */
    public String getNamePattern()
    {
        return namePattern;
    }


    /**
     * Return the description of the data field.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the data type of this field.
     *
     * @return enum
     */
    public DataType getDataType()
    {
        return dataType;
    }


    /**
     * Return the optional default value for this item.
     *
     * @return string
     */
    public String getDefaultValue()
    {
        return defaultValue;
    }


    /**
     * Return the units used for numeric fields.
     *
     * @return string
     */
    public String getUnits()
    {
        return units;
    }


    /**
     * Return whether this data field is used as an identifier.
     *
     * @return boolean
     */
    public boolean isIdentifier()
    {
        return isIdentifier;
    }


    /**
     * Return whether this data field is nullable.
     *
     * @return boolean
     */
    public boolean isNullable()
    {
        return isNullable;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductDataFieldDefinition{" + displayName + '}';
    }
}
