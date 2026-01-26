/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.enums.ContentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeploymentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.DataType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.opensurvey.measurements.*;

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
     * Unique identifier of the asset.
     */
    ASSET_GUID("Asset Unique Identifier (GUID)",
               "Asset GUID",
               "Unique identifier of the asset",
               DataType.STRING,
               null,
               null,
               true,
               false),

    /**
     * Unique identifier of the glossary term linked to the element using semantic assignment.
     */
    SEMANTIC_TERM_GUID("Semantic Assignment Glossary Term Unique Identifier (GUID)",
               "Semantic Term GUID",
               "Unique identifier of the glossary term linked to the element using semantic assignment.",
               DataType.STRING,
               null,
               null,
               true,
               false),

    /**
     * Unique identifier of a certification type.
     */
    CERTIFICATION_TYPE_GUID("Certification Type Unique Identifier (GUID)",
                       "Certification Type GUID",
                       "Unique identifier of a certification type.",
                       DataType.STRING,
                       null,
                       null,
                       true,
                       false),

    /**
     * Unique identifier of a certification relationship.
     */
    CERTIFICATION_GUID("Certification Unique Identifier (GUID)",
                       "Certification GUID",
                       "Unique identifier of a certification relationship.",
                       DataType.STRING,
                       null,
                       null,
                       true,
                       false),

    /**
     * Unique identifier of a license relationship.
     */
    LICENSE_GUID("License Unique Identifier (GUID)",
                            "License GUID",
                            "Unique identifier of a license relationship.",
                            DataType.STRING,
                            null,
                            null,
                            true,
                            false),

    /**
     * Unique identifier of a license type.
     */
    LICENSE_TYPE_GUID("License Type Unique Identifier (GUID)",
                            "License Type GUID",
                            "Unique identifier of a license type.",
                            DataType.STRING,
                            null,
                            null,
                            true,
                            false),

    /**
     * Unique identifier of a project.
     */
    PROJECT_GUID("Project Unique Identifier (GUID)",
                 "Project GUID",
                 "Unique identifier of a project.",
                 DataType.STRING,
                 null,
                 null,
                 true,
                 false),

    /**
     * Unique identifier of a profile.
     */
    PROFILE_GUID("Profile Unique Identifier (GUID)",
                 "Profile GUID",
                 "Unique identifier of a profile.",
                 DataType.STRING,
                 null,
                 null,
                 true,
                 false),

    /**
     * Unique identifier of a survey report for an asset.
     */
    SURVEY_REPORT_GUID("Survey Report Unique Identifier (GUID)",
                       "Survey Report GUID",
                       "Unique identifier of a survey report for an asset.",
                       DataType.STRING,
                       null,
                       null,
                       true,
                       false),

    /**
     * Unique identifier of a survey subject (typically an asset).
     */
    SURVEY_SUBJECT_GUID("Survey Subject Unique Identifier (GUID)",
                        "Survey Subject GUID",
                        "Unique identifier of a survey subject (typically an asset).",
                        DataType.STRING,
                        null,
                        null,
                        true,
                        false),

    /**
     * Unique identifier of a survey annotation.
     */
    ANNOTATION_GUID("Survey Annotation Unique Identifier (GUID)",
                        "Annotation GUID",
                        "Unique identifier of a survey annotation.",
                        DataType.STRING,
                        null,
                        null,
                        true,
                        false),

    /**
     * Unique identifier of an engine action.
     */
    ENGINE_ACTION_GUID("Engine Action Unique Identifier (GUID)",
                       "Engine Action GUID",
                       "Unique identifier of an engine action.",
                       DataType.STRING,
                       null,
                       null,
                       true,
                       false),

    /**
     * Unique identifier of an engine action's requester.
     */
    ACTION_REQUEST_GUID("Action Request Unique Identifier (GUID)",
                       "Action Request GUID",
                       "Unique identifier of an engine action's requester.",
                       DataType.STRING,
                       null,
                       null,
                       true,
                       false),

    /**
     * Unique identifier of an engine action's target element.
     */
    ACTION_REQUEST_NAME("Action Request Name",
                        "Action Request Name",
                        "The type of action requested.",
                        DataType.STRING,
                        null,
                        null,
                        true,
                        true),

    /**
     * Unique identifier of an engine action's target element.
     */
    ACTION_TARGET_RELATIONSHIP_GUID("Action Target Relationship Unique Identifier (GUID)",
                        "Action Target Relationship GUID",
                        "Unique identifier of the relationship linking an engine action to one of its target elements to process.",
                        DataType.STRING,
                        null,
                        null,
                        true,
                        false),

    /**
     * Unique identifier of an engine action's target element.
     */
    ACTION_TARGET_GUID("Action Target Unique Identifier (GUID)",
                       "Action Target GUID",
                       "Unique identifier of an engine action's target element to process.",
                       DataType.STRING,
                       null,
                       null,
                       true,
                       false),


    /**
     * Name of an engine action's target element to process.
     */
    ACTION_TARGET_NAME("Action Target Name",
                       "Action Target Name",
                       "Name of an engine action's target element to process.",
                       DataType.STRING,
                       null,
                       null,
                       true,
                       true),

    /**
     * The unique identifier of a location.
     */
    LOCATION_GUID("Location Unique Identifier (GUID)",
                  "Location GUID",
                  "The unique identifier of a location.",
                  DataType.STRING,
                  null,
                  null,
                  true,
                  false),


    /**
     * The unique identifier of an owner.
     */
    OWNER_GUID("Owner Unique Identifier (GUID)",
                  "Owner GUID",
                  "The unique identifier of an owner of the attached element.",
                  DataType.STRING,
                  null,
                  null,
                  true,
                  false),

    /**
     * The unique identifier of an organization where this digital resource originated.
     */
    ORIGIN_ORGANIZATION_GUID("Origin Organization Unique Identifier (GUID)",
               "Owner GUID",
               "The unique identifier of an organization where this digital resource originated.",
               DataType.STRING,
               null,
               null,
               true,
               false),

    /**
     * The unique identifier of a business capability where this digital resource originated.
     */
    ORIGIN_BUSINESS_CAPABILITY_GUID("Origin Business Capability Unique Identifier (GUID)",
               "Owner GUID",
               "The unique identifier of a business capability where this digital resource originated.",
               DataType.STRING,
               null,
               null,
               true,
               false),

    /**
     * Time that the element was created.
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
     * Time that the element was last updated - null means no update has occurred since the element was created.
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
     * Repository status from the element's header indicates wither the element is active, has been soft-deleted, or is invalid.
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
     * Status of the process definition/execution.
     */
    ACTIVITY_STATUS("Activity Status",
                    "Activity Status",
                    OpenMetadataProperty.ACTIVITY_STATUS.description,
                    OpenMetadataProperty.ACTIVITY_STATUS.dataType,
                    null,
                    null,
                    false,
                    true),


    /**
     * Status of the content of this element.
     */
    CONTENT_STATUS("Content Status",
                   "Content Status",
                   OpenMetadataProperty.CONTENT_STATUS.description,
                   OpenMetadataProperty.CONTENT_STATUS.dataType,
                   ContentStatus.ACTIVE.getName(),
                   null,
                   false,
                   true),

    /**
     * Status of the digital resource's deployment.
     */
    DEPLOYMENT_STATUS("Deployment Status",
                      "Deployment Status",
                      OpenMetadataProperty.DEPLOYMENT_STATUS.description,
                      OpenMetadataProperty.DEPLOYMENT_STATUS.dataType,
                      DeploymentStatus.ACTIVE.getName(),
                      null,
                      false,
                      true),

    /**
     * The time when this record was last synchronized.
     */
    SYNC_TIME("Sync Time",
              "Sync Time",
              "The time when this record was last synchronized.",
              DataType.DATE,
              null,
              null,
              true,
              false),

    /**
     * Unique name of an element.
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
     * Readable identifier used to identify the element.
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
     * Property Name - used as the identifier property in valid metadata values sets only
     */
    PROPERTY_NAME("Property Name",
                  "Property Name",
                  OpenMetadataProperty.PROPERTY_NAME.description,
                  OpenMetadataProperty.PROPERTY_NAME.dataType,
                  null,
                  null,
                  true,
                  false),

    /**
     * Short name for the element used in lists and titles.
     */
    DISPLAY_NAME("Display Name",
                 "Display Name",
                 OpenMetadataProperty.DISPLAY_NAME.description,
                 OpenMetadataProperty.DISPLAY_NAME.dataType,
                 null,
                 null,
                 false,
                 true),


    /**
     * Published Version Identifier
     */
    VERSION_IDENTIFIER("Version Identifier",
                       "Version Identifier",
                       OpenMetadataProperty.VERSION_IDENTIFIER.description,
                       OpenMetadataProperty.VERSION_IDENTIFIER.dataType,
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
     * The url link to documentation.
     */
    URL("URL Link",
        "URL",
        "The url link to documentation.",
        DataType.STRING,
        null,
        null,
        false,
        true),

    /**
     * Authors
     */
    AUTHORS("Authors",
            "Authors",
            OpenMetadataProperty.AUTHORS.description,
            OpenMetadataProperty.AUTHORS.dataType,
            null,
            null,
            false,
            true),

    /**
     * Summary
     */
    SUMMARY("Summary",
            "Summary",
            OpenMetadataProperty.SUMMARY.description,
            OpenMetadataProperty.SUMMARY.dataType,
            null,
            null,
            false,
            true),

    /**
     * Mission
     */
    MISSION("Mission",
            "Mission",
            OpenMetadataProperty.MISSION.description,
            OpenMetadataProperty.MISSION.dataType,
            null,
            null,
            false,
            true),

    /**
     * Purpose
     */
    PURPOSE("Purpose",
            "Purpose",
            OpenMetadataProperty.PURPOSE.description,
            OpenMetadataProperty.PURPOSE.dataType,
            null,
            null,
            false,
            true),

    /**
     * Example
     */
    EXAMPLE("Example",
            "Example",
            OpenMetadataProperty.EXAMPLE.description,
            OpenMetadataProperty.EXAMPLE.dataType,
            null,
            null,
            false,
            true),

    /**
     * Resource Name
     */
    RESOURCE_NAME("Resource Name",
                  "Resource Name",
                  OpenMetadataProperty.RESOURCE_NAME.description,
                  OpenMetadataProperty.RESOURCE_NAME.dataType,
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
     * Deployed Implementation Type (Technology Type)
     */
    DEPLOYED_IMPLEMENTATION_TYPE("Deployed Implementation Type (Technology Type)",
                                 "Deployed Implementation Type",
                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.description,
                                 OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.dataType,
                                 null,
                                 null,
                                 false,
                                 true),


    /**
     * Expected Behaviour
     */
    EXPECTED_BEHAVIOUR("Expected Behaviour",
                       "Expected Behaviour",
                       OpenMetadataProperty.EXPECTED_BEHAVIOUR.description,
                       OpenMetadataProperty.EXPECTED_BEHAVIOUR.dataType,
                       null,
                       null,
                       false,
                       true),


    /**
     * The name of the properties bean class to use in REST API calls.
     */
    LOCATION_ROLE("Location Classification Name",
                  "Location Classification Name",
                  "The classification assigned to the location that describes the type of location.",
                  DataType.STRING,
                  null,
                  null,
                  false,
                  true),

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
     * User Id
     */
    USER_ID("User Identifier (userId)",
            "User Id",
            OpenMetadataProperty.USER_ID.description,
            OpenMetadataProperty.USER_ID.dataType,
            null,
            null,
            false,
            true),


    /**
     * Created By User Id
     */
    CREATION_BY("Created By User Identifier (userId)",
            "Created By User Id",
            OpenMetadataProperty.CREATED_BY.description,
            OpenMetadataProperty.CREATED_BY.dataType,
            null,
            null,
            false,
            true),


    /**
     * Last Updated By User Id
     */
    LAST_UPDATED_BY("Last Updated By User Identifier (userId)",
            "Last Updated By User Id",
            OpenMetadataProperty.UPDATED_BY.description,
            OpenMetadataProperty.UPDATED_BY.dataType,
            null,
            null,
            false,
            true),


    /**
     * Maintained By User Ids
     */
    MAINTAINED_BY("Maintained By User Identifiers (userIds)",
            "Maintained By User Ids",
            OpenMetadataProperty.MAINTAINED_BY.description,
            OpenMetadataProperty.USER_ID.dataType,
            null,
            null,
            false,
            true),


    /**
     * Engine Host User Id
     */
    ENGINE_HOST_USER_ID("Engine Host User Identifier (userId)",
                        "Engine Host User Id",
                        "User that ran the survey",
                        DataType.STRING,
                        null,
                        null,
                        false,
                        true),


    /**
     * Initiator User Id
     */
    INITIATOR_USER_ID("Initiator User Identifier (userId)",
                        "Initiator User Id",
                        "User that requested the action.",
                        DataType.STRING,
                        null,
                        null,
                        false,
                        true),

    /**
     * Archive Date
     */
    ARCHIVED("Archive Date",
                       "Archive Date",
                       OpenMetadataProperty.ARCHIVE_DATE.description,
                       OpenMetadataProperty.ARCHIVE_DATE.dataType,
                       null,
                       null,
                       false,
                       true),

    /**
     * Distinguished Name
     */
    DISTINGUISHED_NAME("Distinguished Name",
                       "Distinguished Name",
                       OpenMetadataProperty.DISTINGUISHED_NAME.description,
                       OpenMetadataProperty.DISTINGUISHED_NAME.dataType,
                       null,
                       null,
                       false,
                       true),


    /**
     * List of informal tags attached to this element.
     */
    TAGS("Informal Tags",
                      "Is Case Sensitive",
                      "List of informal tags attached to this element.",
                      DataType.ARRAY_STRING,
                      null,
                      null,
                      false,
                      true),

    /**
     * List of search keywords attached to this element.
     */
    SEARCH_KEYWORDS("Search Keywords",
         "Search Keywords",
         "List of search keywords attached to this element.",
         DataType.ARRAY_STRING,
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
     * Abbreviation
     */
    ABBREVIATION("Abbreviation",
          "Abbreviation",
          OpenMetadataProperty.ABBREVIATION.description,
          OpenMetadataProperty.ABBREVIATION.dataType,
          null,
          null,
          false,
          true),


    /**
     * Additional Properties
     */
    ADDITIONAL_PROPERTIES("Additional Properties",
                          "Additional Properties",
                          OpenMetadataProperty.ADDITIONAL_PROPERTIES.description,
                          OpenMetadataProperty.ADDITIONAL_PROPERTIES.dataType,
                          null,
                          null,
                          false,
                          true),



    /**
     * Source
     */
    SOURCE("Source",
           "Source",
           OpenMetadataProperty.SOURCE.description,
           OpenMetadataProperty.SOURCE.dataType,
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
     * Open Metadata Supertypes
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
     * Asset Type Name
     */
    ASSET_TYPE_NAME("Asset Type Name",
                            "Asset Type Name",
                            "The unique type name of an asset.",
                            DataType.STRING,
                            null,
                            null,
                            false,
                            true),


    /**
     * Owner Type Name
     */
    OWNER_TYPE_NAME("Owner Type Name",
                    "Owner Type Name",
                    "The unique type name of the owner of the associated element.",
                    DataType.STRING,
                    null,
                    null,
                    false,
                    true),


    /**
     * Annotation Type Name
     */
    ANNOTATION_TYPE_NAME("Annotation Type Name",
                    "Annotation Type Name",
                    "The unique type name of an annotation from a survey report.",
                    DataType.STRING,
                    null,
                    null,
                    false,
                    true),

    /**
     * Survey Subject Type Name
     */
    SURVEY_SUBJECT_TYPE_NAME("Survey Subject Type Name",
                         "Survey Subject Type Name",
                         "The unique type name of the subject of a survey report (typically an asset).",
                         DataType.STRING,
                         null,
                         null,
                         false,
                         true),


    /**
     * Action Target Type Name
     */
    ACTION_TARGET_TYPE_NAME("Action Target Type Name",
                         "Action Target Type Name",
                         "The unique type name of an an element that is the target of an action.",
                         DataType.STRING,
                         null,
                         null,
                         false,
                         true),


    /**
     * A list of governance zone names that controls visibility to the associated metadata element.
     */
    ZONE_NAMES("Governance Zone Names",
               "Zone Names",
               "A list of governance zone names that controls visibility to the associated metadata element.",
               DataType.STRING,
               null,
               null,
               false,
               true),

    CONFIDENTIALITY_LEVEL("Confidentiality Level",
                          "Confidentiality Level",
                          "Identifier in the confidentiality classification",
                          DataType.INT,
                          null,
                          null,
                          false,
                          true),

    CONFIDENCE_LEVEL("Confidence Level",
                     "Confidence Level",
                     "How confident (0-100) that the data is correct.",
                     DataType.INT,
                     null,
                     null,
                     false,
                     true),

    CRITICALITY_LEVEL("Criticality Level",
                      "Criticality Level",
                      "Identifier in the criticality classification",
                      DataType.INT,
                      null,
                      null,
                      false,
                      true),


    /**
     * Open Metadata Type Status
     */
    OPEN_METADATA_TYPE_STATUS("Open Metadata Type Status",
                              "Open Metadata Type Status",
                              "The status of an open metadata type.",
                              DataType.STRING,
                              null,
                              null,
                              false,
                              true),

    /**
     * Open Metadata Attribute Name
     */
    OPEN_METADATA_ATTRIBUTE_TYPE_NAME("Open Metadata Attribute Type Name",
                                      "Open Metadata Attribute Type Name",
                                      "The unique name of a type that can be assigned to an open metadata attribute.",
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
                                 "The property name of an open metadata attribute within a type.",
                                 DataType.STRING,
                                 null,
                                 null,
                                 false,
                                 true),

    /**
     * Open Metadata Attribute Status
     */
    OPEN_METADATA_ATTRIBUTE_STATUS("Open Metadata Attribute Status",
                                   "Open Metadata Attribute Status",
                                   "The status of an open metadata attribute.",
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
     * Unique identifier of the metadata collection.
     */
    METADATA_COLLECTION_ID("Metadata Collection Id",
                           "Metadata Collection Id",
                           "Unique identifier of the home metadata collection for an element.",
                           DataType.STRING,
                           null,
                           null,
                           true,
                           false),

    /**
     * Unique name of the metadata collection.
     */
    METADATA_COLLECTION_NAME("Metadata Collection Name",
                           "Metadata Collection Id",
                           "Name of the home metadata collection for an element.",
                           DataType.STRING,
                           null,
                           null,
                           false,
                           true),


    REQUEST_TYPE("Request Type",
                 "Request Type",
                 "The request type of an engine action.",
                 DataType.STRING,
                 null,
                 null,
                 false,
                 true),

    GOVERNANCE_ENGINE_NAME("Governance Engine Name",
                           "Governance Engine Name",
                           "Name of the engine that ran the survey.",
                           DataType.STRING,
                           null,
                           null,
                           false,
                           true),

    END_TIMESTAMP("End Timestamp",
                  "End Timestamp",
                  "The end time of the survey.",
                  DataType.DATE,
                  null,
                  null,
                  false,
                  true),

    START_TIMESTAMP("Start Timestamp",
                    "Start Timestamp",
                    "The start time of the survey.",
                    DataType.DATE,
                    null,
                    null,
                    false,
                    true),

    EXPLANATION("Explanation",
                "Explanation",
                "What does this annotation represent/contain.",
                DataType.STRING,
                null,
                null,
                false,
                true),

    ANALYSIS_STEP("Analysis Step",
                  "Analysis Step",
                  "Which phase of the analysis produced the annotation.",
                  DataType.STRING,
                  null,
                  null,
                  false,
                  true),


    EXPRESSION("Expression",
               "Expression",
               "Formula used to create the annotation (optional).",
               DataType.STRING,
               null,
               null,
               false,
               true),

    JSON_PROPERTIES("JSON Properties",
                    "JSON Properties",
                    "Optional JSON properties associated with the annotation.",
                    DataType.STRING,
                    null,
                    null,
                    false,
                    true),

    MEASUREMENT_NAME("Measurement Name",
                     "Measurement Name",
                     "The name of the measurement, aka annotation type.",
                     DataType.STRING,
                     null,
                     null,
                     false,
                     true),

    MEASUREMENT_DISPLAY_NAME("Measurement Display Value",
                             "Measurement Display Value",
                             "The measurement display description.",
                             DataType.STRING,
                             null,
                             null,
                             false,
                             true),

    MEASUREMENT_VALUE("Measurement String Value",
                      "Measurement String Value",
                      "The value of the measurement.",
                      DataType.STRING,
                      null,
                      null,
                      false,
                      true),

    MEASUREMENT_NUMERIC_VALUE("Measurement Numeric Value",
                              "Measurement Numeric Value",
                              "The value of the measurement.",
                              DataType.INT,
                              null,
                              null,
                              false,
                              true),

    MEASUREMENT_CATEGORY("Measurement Category",
                         "Measurement Category",
                         "The category of the measurement.",
                         DataType.STRING,
                         null,
                         null,
                         false,
                         true),

    RESOURCE_PROPERTIES("Resource Properties",
                        "Resource Properties",
                        "JSON encoded map of properties describing the digital resource.",
                        DataType.STRING,
                        null,
                        null,
                        false,
                        true),

    RESOURCE_CREATION_TIME("Resource Creation Time",
                           "Resource Creation Time",
                           "The creation time of the surveyed digital resource.",
                           DataType.DATE,
                           null,
                           null,
                           false,
                           true),

    RESOURCE_SIZE("Resource Size",
                  "Resource Size",
                  "The size of the surveyed digital resource.",
                  DataType.INT,
                  null,
                  null,
                  false,
                  true),

    FILE_NAME("Filename",
              "Filename",
              FileMetric.FILE_NAME.getDescription(),
              DataType.STRING,
              null,
              null,
              false,
              true),

    FILE_EXTENSION("File Extension",
                   "File Extension",
                   FileMetric.FILE_EXTENSION.getDescription(),
                   DataType.STRING,
                   null,
                   null,
                   false,
                   true),

    PATHNAME("Pathname",
             "Pathname",
             FileMetric.PATH_NAME.getDescription(),
             DataType.STRING,
             null,
             null,
             false,
             true),

    FILE_TYPE("File Type",
              "File Type",
              FileMetric.FILE_TYPE.getDescription(),
              DataType.STRING,
              null,
              null,
              false,
              true),

    ENCODING("File Encoding",
             "File Encoding",
             FileMetric.ENCODING.getDescription(),
             DataType.STRING,
             null,
             null,
             false,
             true),

    CAN_READ("Can Read",
             "Can Read",
             FileMetric.CAN_READ.getDescription(),
             DataType.BOOLEAN,
             null,
             null,
             false,
             true),

    CAN_WRITE("Can Write",
              "Can Write",
              FileMetric.CAN_WRITE.getDescription(),
              DataType.BOOLEAN,
              null,
              null,
              false,
              true),

    CAN_EXECUTE("Can Execute",
                "Can Execute",
                FileMetric.CAN_EXECUTE.getDescription(),
                DataType.BOOLEAN,
                null,
                null,
                false,
                true),

    IS_SYM_LINK("Is Symbolic Link",
                "Is Symbolic Link",
                FileMetric.IS_SYM_LINK.getDescription(),
                DataType.BOOLEAN,
                null,
                null,
                false,
                true),

    IS_HIDDEN("Is Hidden",
              "Is Hidden",
              FileMetric.IS_HIDDEN.getDescription(),
              DataType.BOOLEAN,
              null,
              null,
              false,
              true),

    FILE_CREATION_TIME("File Creation Time",
                       "File Creation Time",
                       FileMetric.CREATION_TIME.getDescription(),
                       DataType.DATE,
                       null,
                       null,
                       false,
                       true),

    LAST_MODIFIED_TIME("Last Modified Time",
                       "Last Modified Time",
                       FileMetric.LAST_MODIFIED_TIME.getDescription(),
                       DataType.DATE,
                       null,
                       null,
                       false,
                       true),

    LAST_ACCESSED_TIME("Last Accessed Time",
                       "Last Accessed Time",
                       FileMetric.LAST_ACCESSED_TIME.getDescription(),
                       DataType.DATE,
                       null,
                       null,
                       false,
                       true),

    FILE_SIZE("File Size",
              "File Size",
              FileMetric.FILE_SIZE.getDescription(),
              DataType.INT,
              null,
              null,
              false,
              true),

    RECORD_COUNT("Record Count",
                 "Record Count",
                 FileMetric.RECORD_COUNT.getDescription(),
                 DataType.INT,
                 null,
                 null,
                 false,
                 true),

    DIRECTORY_NAME("Directory Name",
                   "Directory Name",
                   "Name of a file system directory.",
                   DataType.STRING,
                   null,
                   null,
                   false,
                   true),

    FILE_COUNT("File Count",
               "File Count",
               FileDirectoryMetric.FILE_COUNT.getDescription(),
               DataType.INT,
               null,
               null,
               false,
               true),

    TOTAL_FILE_SIZE("Total File Size",
                    "Total File Size",
                    FileDirectoryMetric.TOTAL_FILE_SIZE.getDescription(),
                    DataType.LONG,
                    null,
                    null,
                    false,
                    true),

    SUB_DIRECTORY_COUNT("Sub Directory Count",
                        "Sub Directory Count",
                        FileDirectoryMetric.SUB_DIRECTORY_COUNT.getDescription(),
                        DataType.INT,
                        null,
                        null,
                        false,
                        true),

    READABLE_FILE_COUNT("Readable File Count",
                        "Readable File Count",
                        FileDirectoryMetric.READABLE_FILE_COUNT.getDescription(),
                        DataType.INT,
                        null,
                        null,
                        false,
                        true),

    WRITEABLE_FILE_COUNT("Writeable File Count",
                         "Writeable File Count",
                         FileDirectoryMetric.WRITEABLE_FILE_COUNT.getDescription(),
                         DataType.INT,
                         null,
                         null,
                         false,
                         true),

    EXECUTABLE_FILE_COUNT("Executable File Count",
                          "Executable File Count",
                          FileDirectoryMetric.EXECUTABLE_FILE_COUNT.getDescription(),
                          DataType.INT,
                          null,
                          null,
                          false,
                          true),

    SYM_LINK_FILE_COUNT("Symbolic Link File Count",
                        "Symbolic Link File Count",
                        FileDirectoryMetric.SYM_LINK_COUNT.getDescription(),
                        DataType.INT,
                        null,
                        null,
                        false,
                        true),

    HIDDEN_FILE_COUNT("Hidden File Count",
                      "Hidden File Count",
                      FileDirectoryMetric.HIDDEN_FILE_COUNT.getDescription(),
                      DataType.INT,
                      null,
                      null,
                      false,
                      true),

    FILE_NAME_COUNT("File Name Count",
                    "File Name Count",
                    FileDirectoryMetric.FILE_NAME_COUNT.getDescription(),
                    DataType.INT,
                    null,
                    null,
                    false,
                    true),

    FILE_EXTENSION_COUNT("File Extension Count",
                         "File Extension Count",
                         FileDirectoryMetric.FILE_EXTENSION_COUNT.getDescription(),
                         DataType.INT,
                         null,
                         null,
                         false,
                         true),

    FILE_TYPE_COUNT("File Type Count",
                    "File Type Count",
                    FileDirectoryMetric.FILE_TYPE_COUNT.getDescription(),
                    DataType.INT,
                    null,
                    null,
                    false,
                    true),

    ASSET_TYPE_COUNT("Asset Type Count",
                     "Asset Type Count",
                     FileDirectoryMetric.ASSET_TYPE_COUNT.getDescription(),
                     DataType.INT,
                     null,
                     null,
                     false,
                     true),

    DEPLOYED_IMPLEMENTATION_TYPE_COUNT("Deployed Implementation Type Count",
                                       "Deployed Implementation Type Count",
                                       FileDirectoryMetric.DEPLOYED_IMPL_TYPE_COUNT.getDescription(),
                                       DataType.INT,
                                       null,
                                       null,
                                       false,
                                       true),

    UNCLASSIFIED_FILE_COUNT("Unclassified File Count",
                            "Unclassified File Count",
                            FileDirectoryMetric.UNCLASSIFIED_FILE_COUNT.getDescription(),
                            DataType.INT,
                            null,
                            null,
                            false,
                            true),

    INACCESSIBLE_FILE_COUNT("Inaccessible File Count",
                            "Inaccessible File Count",
                            FileDirectoryMetric.INACCESSIBLE_FILE_COUNT.getDescription(),
                            DataType.INT,
                            null,
                            null,
                            false,
                            true),

    LAST_FILE_CREATION_TIME("Last File Creation Time",
                            "Last File Creation Time",
                            FileDirectoryMetric.LAST_FILE_CREATION_TIME.getDescription(),
                            DataType.DATE,
                            null,
                            null,
                            false,
                            true),

    LAST_FILE_MODIFICATION_TIME("Last File Modification Time",
                                "Last File Modification Time",
                                FileDirectoryMetric.LAST_FILE_MODIFIED_TIME.getDescription(),
                                DataType.DATE,
                                null,
                                null,
                                false,
                                true),

    LAST_FILE_ACCESSED_TIME("Last File Accessed Time",
                            "Last File Accessed Time",
                            FileDirectoryMetric.LAST_FILE_ACCESSED_TIME.getDescription(),
                            DataType.DATE,
                            null,
                            null,
                            false,
                            true),

    SCHEMA_COUNT("Schema Count",
                 "Schema Count",
                 RelationalDatabaseMetric.SCHEMA_COUNT.getDescription(),
                 DataType.INT,
                 null,
                 null,
                 false,
                 true),

    TABLE_COUNT("Table Count",
                "Table Count",
                RelationalDatabaseMetric.TABLE_COUNT.getDescription(),
                DataType.LONG,
                null,
                null,
                false,
                true),

    VIEW_COUNT("View Count",
               "View Count",
               RelationalDatabaseMetric.VIEW_COUNT.getDescription(),
               DataType.LONG,
               null,
               null,
               false,
               true),

    MAT_VIEW_COUNT("Materialized View Count",
                   "Materialized View Count",
                   RelationalDatabaseMetric.MAT_VIEW_COUNT.getDescription(),
                   DataType.LONG,
                   null,
                   null,
                   false,
                   true),

    COLUMN_COUNT("Column Count",
                 "Column Count",
                 RelationalDatabaseMetric.COLUMN_COUNT.getDescription(),
                 DataType.LONG,
                 null,
                 null,
                 false,
                 true),

    DATA_SIZE("Data Size",
              "Data Size",
              RelationalDatabaseMetric.DATA_SIZE.getDescription(),
              DataType.LONG,
              null,
              null,
              false,
              true),

    TABLE_TYPE("Table Type",
               "Table Type",
               RelationalTableMetric.TABLE_TYPE.getDescription(),
               DataType.STRING,
               null,
               null,
               false,
               true),

    RESOURCE_OWNER("Resource Owner",
                   "Resource Owner",
                   RelationalTableMetric.TABLE_OWNER.getDescription(),
                   DataType.STRING,
                   null,
                   null,
                   false,
                   true),

    NOT_NULL("Not Null",
             "Not Null",
             RelationalColumnMetric.COLUMN_NOT_NULL.getDescription(),
             DataType.BOOLEAN,
             null,
             null,
             false,
             true),

    AVERAGE_WIDTH("Data Width",
                  "Data Width",
                  RelationalColumnMetric.AVERAGE_WIDTH.getDescription(),
                  DataType.INT,
                  null,
                  null,
                  false,
                  true),

    NUMBER_OF_DISTINCT_VALUES("Distinct Value Count",
                              "Distinct Value Count",
                              RelationalColumnMetric.NUMBER_OF_DISTINCT_VALUES.getDescription(),
                              DataType.LONG,
                              null,
                              null,
                              false,
                              true),

    MOST_COMMON_VALUES("Most Common Values",
                       "Most Common Values",
                       RelationalColumnMetric.MOST_COMMON_VALUES.getDescription(),
                       DataType.STRING,
                       null,
                       null,
                       false,
                       true),

    MOST_COMMON_VALUES_FREQUENCY("Most Common Values Frequency",
                                 "Most Common Values Frequency",
                                 RelationalColumnMetric.MOST_COMMON_VALUES_FREQUENCY.getDescription(),
                                 DataType.STRING,
                                 null,
                                 null,
                                 false,
                                 true),

    ROWS_FETCHED("Rows Fetched",
                 "Rows Fetched",
                 RelationalDatabaseMetric.ROWS_FETCHED.getDescription(),
                 DataType.LONG,
                 null,
                 null,
                 false,
                 true),

    ROWS_INSERTED("Rows Inserted",
                  "Rows Inserted",
                  RelationalDatabaseMetric.ROWS_INSERTED.getDescription(),
                  DataType.LONG,
                  null,
                  null,
                  false,
                  true),

    ROWS_UPDATED("Rows Updated",
                 "Rows Updated",
                 RelationalDatabaseMetric.ROWS_UPDATED.getDescription(),
                 DataType.LONG,
                 null,
                 null,
                 false,
                 true),

    ROWS_DELETED("Rows Deleted",
                 "Rows Deleted",
                 RelationalDatabaseMetric.ROWS_DELETED.getDescription(),
                 DataType.LONG,
                 null,
                 null,
                 false,
                 true),

    IS_POPULATED("Is Populated",
                 "Is Populated",
                 RelationalTableMetric.IS_POPULATED.getDescription(),
                 DataType.BOOLEAN,
                 null,
                 null,
                 false,
                 true),

    HAS_INDEXES("Has Indexes",
                "Has Indexes",
                RelationalTableMetric.HAS_INDEXES.getDescription(),
                DataType.BOOLEAN,
                null,
                null,
                false,
                true),

    HAS_RULES("Has Rules",
              "Has Rules",
              RelationalTableMetric.HAS_RULES.getDescription(),
              DataType.BOOLEAN,
              null,
              null,
              false,
              true),

    HAS_TRIGGERS("Has Triggers",
                 "Has Triggers",
                 RelationalTableMetric.HAS_TRIGGERS.getDescription(),
                 DataType.BOOLEAN,
                 null,
                 null,
                 false,
                 true),

    HAS_ROW_SECURITY("Has Row Security",
                     "Has Row Security",
                     RelationalTableMetric.HAS_ROW_SECURITY.getDescription(),
                     DataType.BOOLEAN,
                     null,
                     null,
                     false,
                     true),

    QUERY_DEFINITION("Query Definition",
                     "Query Definition",
                     RelationalTableMetric.QUERY_DEFINITION.getDescription(),
                     DataType.STRING,
                     null,
                     null,
                     false,
                     true),

    SESSION_TIME("Session Time",
                 "Session Time",
                 RelationalDatabaseMetric.SESSION_TIME.getDescription(),
                 DataType.INT,
                 null,
                 null,
                 false,
                 true),

    ACTIVE_TIME("Active Time",
                "Active Time",
                RelationalDatabaseMetric.ACTIVE_TIME.getDescription(),
                DataType.INT,
                null,
                null,
                false,
                true),

    LAST_STATS_RESET("Last Statistics Reset",
                     "Last Statistics Reset",
                     RelationalDatabaseMetric.LAST_STATISTICS_RESET.getDescription(),
                     DataType.DATE,
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
     * @param displayName  display name
     * @param namePattern  technical name
     * @param description  description of the use of this field
     * @param dataType     type for this field
     * @param defaultValue default value to use if not specified
     * @param units        units that are used in numeric fields
     * @param isIdentifier is this field used as a row/record/object identifier?
     * @param isNullable   is this field nullable
     */
    ProductDataFieldDefinition(String displayName,
                               String namePattern,
                               String description,
                               DataType dataType,
                               String defaultValue,
                               String units,
                               boolean isIdentifier,
                               boolean isNullable)
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
        return "DataField::OpenMetadataDigitalProduct::" + displayName;
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
