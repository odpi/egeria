/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;

/**
 * Describes the different types of columns found in the audit log database schema
 */
public enum HarvestOpenMetadataColumn implements PostgreSQLColumn
{
    SYNC_TIME                    ("sync_time", ColumnType.DATE, "Time that a record is made.", true),
    OPEN_METADATA_TYPE           ("open_metadata_type", ColumnType.STRING, "Open metadata type of this row element.", true),
    ASSET_GUID                   ("asset_guid", ColumnType.STRING, "Unique identifier of the asset", true),
    RESOURCE_NAME                ("resource_name", ColumnType.STRING, "Technical name from the resource.", false),
    RESOURCE_DESCRIPTION         ("resource_description", ColumnType.STRING, "Description extracted from the resource.", false),
    QUALIFIED_NAME               ("qualified_name", ColumnType.STRING, "Unique name for associated element.", true),
    VERSION_IDENTIFIER           ("version_identifier", ColumnType.STRING, "Display version identifier - typically in the form V.x.y", false),
    DEPLOYED_IMPLEMENTATION_TYPE ("deployed_implementation_type", ColumnType.STRING, "Technology type for element.", false),
    DISPLAY_NAME                 ("display_name", ColumnType.STRING, "Display name of the associated element", false),
    DISPLAY_DESCRIPTION          ("display_description", ColumnType.STRING, "Business description of the associated element", false),
    DISPLAY_SUMMARY              ("display_summary", ColumnType.STRING, "Summary business description of the associated element", false),
    ABBREVIATION                 ("abbreviation", ColumnType.STRING, "Abbreviation associated with the element", false),
    USAGE                        ("usage", ColumnType.STRING, "Intended usage of element", false),
    ADDITIONAL_PROPERTIES        ("additional_properties", ColumnType.STRING, "Additional properties", false),
    OWNER_GUID                   ("owner_guid", ColumnType.STRING, "Unique identifier of the owner", false),
    OWNER_TYPE_NAME              ("owner_type_name", ColumnType.STRING, "Type name of the owner", false),
    ORIGIN_ORG_GUID              ("origin_org_guid", ColumnType.STRING, "Unique identifier of the organization where the resource comes from", false),
    ORIGIN_BIZ_CAP_GUID          ("origin_biz_cap_guid", ColumnType.STRING, "Unique identifier of the business capability where the resource comes from", false),
    ZONE_NAMES                   ("zone_names", ColumnType.STRING, "List of zones that the asset was a member of", false),
    RESOURCE_LOCATION_GUID       ("resource_loc_guid", ColumnType.STRING, "Unique identifier of the location associated with the resource", false),
    CONFIDENTIALITY_LEVEL        ("confidentiality_level", ColumnType.INT, "Identifier in the confidentiality classification", false),
    CONFIDENCE_LEVEL             ("confidence_level", ColumnType.INT, "Identifier in the confidence classification", false),
    CRITICALITY_LEVEL            ("criticality_level", ColumnType.INT, "Identifier in the criticality classification", false),
    CREATION_TIME                ("creation_time", ColumnType.DATE, "Creation time for the element.", false),
    CREATION_BY                  ("creation_by", ColumnType.STRING, "Creation user for the element.", false),
    LAST_UPDATE_TIME             ("last_update_time", ColumnType.DATE, "Time that the element was last updated.", false),
    LAST_UPDATED_BY              ("last_updated_by", ColumnType.STRING, "User that last updated the element.", false),
    MAINTAINED_BY                ("maintained_by", ColumnType.STRING, "Users that have contributed to the element.", false),
    ARCHIVED                     ("archived", ColumnType.DATE, "The date/time that the asset was archive (null if not archived).", false),
    TAGS                         ("tags", ColumnType.STRING, "List of tags attached to the asset.", false),
    SEMANTIC_TERM_GUID           ("semantic_tag_guid", ColumnType.STRING, "Unique identifier of an associated glossary term", false),


    TYPE_DESCRIPTION             ("type_description", ColumnType.STRING, "Description of an open metadata type", false),
    SUPER_TYPES                  ("super_types", ColumnType.STRING, "List of super type for an open metadata type", false),


    CERTIFICATION_TYPE_GUID      ("certification_type_guid", ColumnType.STRING, "Unique identifier for a certification type", true),
    CERTIFICATION_TITLE          ("certification_title", ColumnType.STRING, "Title for a certification type", false),
    CERTIFICATION_SUMMARY        ("certification_summary", ColumnType.STRING, "Summary for a certification type", false),


    CERTIFICATION_GUID           ("certification_guid", ColumnType.STRING, "Unique identifier for a certification", false),
    REFERENCEABLE_GUID           ("referenceable_guid", ColumnType.STRING, "Unique identifier for a certified referenceable", false),


    END_TIMESTAMP                ("end_timestamp", ColumnType.DATE, "The end time", false),
    START_TIMESTAMP              ("start_timestamp", ColumnType.DATE, "The start time", false),


    ELEMENT_GUID                 ("element_guid", ColumnType.STRING, "Unique identifier for an element", true),


    NUM_COMMENTS                 ("num_comments", ColumnType.STRING, "Number of comments attached to the element", false),
    NUM_RATINGS                  ("num_ratings", ColumnType.STRING, "Number of ratings attached to the element", false),
    AVG_RATING                   ("avg_rating", ColumnType.INT, "Average rating value", false),
    NUM_TAGS                     ("num_tags", ColumnType.STRING, "Number of tags attached to the element", false),
    NUM_LIKES                    ("num_likes", ColumnType.STRING, "Number of likes attached to the element", false),


    CONTEXT_EVENT_TYPE_GUID      ("context_event_type_guid", ColumnType.STRING, "Unique identifier for a context event type", true),
    CONTEXT_EVENT_GUID           ("context_event_guid", ColumnType.STRING, "Unique identifier for a context event", true),
    CONTEXT_EVENT_EFFECT         ("context_event_effect", ColumnType.STRING, "Effect of a context event", false),
    PLANNED_START_DATE           ("planned_start_date", ColumnType.DATE, "Planned start date for a context event", false),
    PLANNED_DURATION             ("planned_duration", ColumnType.INT, "Planned duration for a context event", false),
    ACTUAL_DURATION              ("actual_duration", ColumnType.INT, "Actual duration for a context event", false),
    REPEAT_INTERVAL              ("repeat_interval", ColumnType.INT, "Repeat interval for a context event", false),
    PLANNED_COMPLETION_DATE      ("planned_completion_date", ColumnType.DATE, "Planned completion date for a context event", false),
    ACTUAL_COMPLETION_DATE       ("actual_completion_date", ColumnType.DATE, "Actual completion date for a context event", false),
    REFERENCE_EFFECTIVE_FROM     ("reference_effective_from", ColumnType.DATE, "The effectiveFrom to use for elements associated with the context event", false),
    REFERENCE_EFFECTIVE_TO       ("reference_effective_to", ColumnType.DATE, "The effectiveFrom to use for elements associated with the context event", false),


    DATA_FIELD_GUID              ("data_field_guid", ColumnType.STRING, "Unique identifier of a schema attribute", true),
    DATA_FIELD_NAME              ("data_field_name", ColumnType.STRING, "Display name of a schema attribute", false),
    DATA_FIELD_TYPE              ("data_field_type", ColumnType.STRING, "Type of data store in a schema attribute", false),
    HAS_PROFILE                  ("has_profile", ColumnType.BOOLEAN, "Does this data field have a data profile?", false),


    DEPARTMENT_GUID              ("department_guid", ColumnType.STRING, "Unique identifier of a department", true),
    DEPARTMENT_NAME              ("department_name", ColumnType.STRING, "Display name of a department", false),
    DEPARTMENT_IDENTIFIER        ("department_identifier", ColumnType.STRING, "Admin identifier of a department", false),
    MANAGER_PROFILE_GUID         ("manager_profile_guid", ColumnType.STRING, "Unique identifier of managers profile", false),
    PARENT_DEPARTMENT_GUID       ("parent_department_guid", ColumnType.STRING, "Unique identifier of the parent department", false),


    EXTERNAL_IDENTIFIER          ("external_identifier", ColumnType.STRING, "Identifier from a third party system", true),
    EXTERNAL_TYPE_NAME           ("external_type_name", ColumnType.STRING, "Type Identifier from a third party system", false),
    EXTERNAL_SCOPE_GUID          ("external_source_guid", ColumnType.STRING, "Unique identifier of the identifiers external scope element.", false),
    VERSION                      ("version", ColumnType.LONG, "Version of an element", false),
    LAST_CONFIRMED_SYNC_TIME     ("last_confirmed_sync_time", ColumnType.STRING, "Time that Egeria last contacted the external third party.", false),
    EGERIA_OWNED                 ("egeria_owned", ColumnType.BOOLEAN, "Which system is the home (owner) of the element?", false),
    EXTERNAL_USER_ID             ("external_user_id", ColumnType.STRING, "User Id received from a third party system.", false),
    OPEN_METADATA_USER_GUID      ("open_metadata_user_guid", ColumnType.STRING, "Optional matching user profile in Open Metadata", false),


    GLOSSARY_GUID                ("glossary_guid", ColumnType.STRING, "Unique identifier of a glossary", true),
    GLOSSARY_LANGUAGE            ("glossary_language", ColumnType.STRING, "Language of a glossary", false),
    NUM_TERMS                    ("num_terms", ColumnType.INT, "Number of terms", false),
    NUM_CATEGORIES               ("num_categories", ColumnType.INT, "Number of categories", false),
    NUM_LINKED_TERMS             ("num_linked_terms", ColumnType.INT, "Number of terms linked to the categories", false),



    LICENSE_TYPE                 ("license_type_guid", ColumnType.STRING, "Unique identifier for an associated type of licence", false),
    LICENSE_TYPE_GUID            ("license_type_guid", ColumnType.STRING, "Unique identifier for a type of licence", true),
    LICENSE_NAME                 ("license_name", ColumnType.STRING, "Display name of a licence type", false),


    LOCATION_GUID                ("location_guid", ColumnType.STRING, "Unique identifier for a location", true),
    LOCATION_NAME                ("location_name", ColumnType.STRING, "Display name of a location", false),

    METADATA_COLLECTION_ID       ("metadata_collection_id", ColumnType.STRING, "Home metadata collection identifier for the subject element", true),
    METADATA_COLLECTION_NAME     ("metadata_collection_name", ColumnType.STRING, "Home metadata collection name for the subject element", false),
    PROVENANCE_TYPE              ("provenance_type", ColumnType.STRING, "Metadata collection provenance type", false),


    LEVEL_IDENTIFIER             ("level_identifier", ColumnType.INT, "Identifier used in a governance action classification", false),
    CLASSIFICATION_NAME          ("classification_name", ColumnType.STRING, "Name of classification", false),


    RELATIONSHIP_GUID            ("relationship_guid", ColumnType.STRING, "Unique identifier for a relationship", true),
    END_1_GUID                   ("end1_guid", ColumnType.STRING, "Unique identifier for end 1 of a relationship", true),
    END_2_GUID                   ("end2_guid", ColumnType.STRING, "Unique identifier for end 2 of a relationship", true),
    END_1_ATTRIBUTE_NAME         ("end1_attribute_name", ColumnType.STRING, "Name for end 1 of a relationship", true),
    END_2_ATTRIBUTE_NAME         ("end2_attribute_name", ColumnType.STRING, "Name for end 2 of a relationship", true),


    ROLE_GUID                    ("role_guid", ColumnType.STRING, "Unique identifier for an actor role", true),
    ROLE_NAME                    ("role_name", ColumnType.STRING, "Display name for an actor role", false),
    HEADCOUNT                    ("headcount", ColumnType.INT, "Unique identifier for an actor", false),


    TERM_GUID                    ("term_guid", ColumnType.STRING, "Unique identifier for a term", true),
    TERM_NAME                    ("term_name", ColumnType.STRING, "Display name for a term", false),
    TERM_SUMMARY                 ("term_summary", ColumnType.STRING, "Summary for a term", false),
    LAST_FEEDBACK_TIME           ("last_feedback_time", ColumnType.DATE, "The last time that feedback was added", false),
    NUM_LINKED_ELEMENTS          ("num_linked_elements", ColumnType.LONG, "The number of linked elements", true),
    LAST_LINKED_TIME             ("last_linked_time", ColumnType.DATE, "The last time that a semantic assignment was made to this term", false),


    TO_DO_GUID                   ("to_do_guid", ColumnType.STRING, "Unique identifier for a ToDo", true),
    PRIORITY                     ("priority", ColumnType.INT, "Priority for taking action", false),
    DUE_TIME                     ("due_time", ColumnType.DATE, "When should action be complete?", false),
    COMPLETION_TIME              ("completion_time", ColumnType.DATE, "When was action completed?", false),
    ACTIVITY_STATUS              ("activity_status", ColumnType.STRING, "What is the status of this process", false),
    CATEGORY                     ("category", ColumnType.STRING, "What is the category of this element", false),
    TO_DO_SOURCE_GUID            ("to_do_source_guid", ColumnType.STRING, "Unique identifier for the source of the ToDo", false),
    TO_DO_SOURCE_TYPE            ("to_do_source_type", ColumnType.STRING, "Open metadata type for the source of the ToDo", false),
    LAST_REVIEW_TIME             ("last_review_time", ColumnType.DATE, "The last time that the ToDo was reviewed", false),
    SPONSOR_GUID                 ("sponsor_guid", ColumnType.STRING, "Unique identifier for the sponsor of the ToDo", false),
    SPONSOR_TYPE                 ("sponsor_type", ColumnType.STRING, "Open metadata type for the sponsor of the ToDo", false),


    USER_IDENTITY_GUID           ("user_identity_guid", ColumnType.STRING, "Unique identifier for a user identity", true),
    LINKED_USER_IDENTITY_GUID    ("linked_user_identity_guid", ColumnType.STRING, "Unique identifier for a user identity", false),
    USER_ID                      ("user_id", ColumnType.STRING, "User account identifier", true),
    DISTINGUISHED_NAME           ("distinguished_name", ColumnType.STRING, "fully qualified name for a userId", false),


    PROFILE_GUID                 ("profile_guid", ColumnType.STRING, "Unique identifier for an actor profile", true),
    EMPLOYEE_NUMBER              ("employee_number", ColumnType.STRING, "Personnel/employee contract number", false),
    PREFERRED_NAME               ("preferred_name", ColumnType.STRING, "Person's preferred name", false),
    ORGANIZATION_NAME            ("organization_name", ColumnType.STRING, "Person's employer", false),
    RESIDENT_COUNTRY             ("resident_country", ColumnType.STRING, "Which country is a person resident", false),
    KARMA_POINTS                 ("karma_points", ColumnType.LONG, "The number of karma points awarded at a point in time", false),


    PROJECT_GUID                 ("project_guid", ColumnType.STRING, "Unique identifier for a project", true),
    PROJECT_IDENTIFIER           ("project_identifier", ColumnType.STRING, "Administration identifier for a project", false),
    PROJECT_STATUS               ("project_status", ColumnType.STRING, "What is the status of this project", false),
    PROJECT_PHASE                ("project_phase", ColumnType.STRING, "What is the phase of this project", false),
    PROJECT_HEALTH               ("project_health", ColumnType.STRING, "What is the health of this project", false),
    PARENT_GUID                  ("parent_guid", ColumnType.STRING, "Unique identifier for the parent of this element", false),


    ASSIGNED_ACTOR_GUID          ("assigned_actor_guid", ColumnType.STRING, "Unique identifier for the actor assigned to action the ToDo", true),
    ASSIGNED_ACTOR_TYPE          ("assigned_actor_type", ColumnType.STRING, "Open metadata type for the actor assigned to action the ToDo", true),
    ASSIGNED_WORK_GUID           ("assigned_work_guid", ColumnType.STRING, "Unique identifier for the work that has been assigned to the actor", true),
    ASSIGNED_WORK_TYPE           ("assigned_work_type", ColumnType.STRING, "Open metadata type for the work that has been assigned to the actor", true),


    COMMUNITY_GUID               ("community_guid", ColumnType.STRING, "Unique identifier for a community", true),
    MISSION                      ("mission", ColumnType.STRING, "Mission for a community", false),


    COLLECTION_GUID              ("collection_guid", ColumnType.STRING, "Unique identifier for a collection", true),
    MEMBER_TYPES                 ("member_types", ColumnType.STRING, "List of types of the members", false),


    PRODUCT_STATUS               ("product_status", ColumnType.STRING, "The status of the digital product.", false),
    PRODUCT_NAME                 ("product_name", ColumnType.STRING, "The display name of the product.", false),
    PRODUCT_TYPE                 ("product_type", ColumnType.STRING, "The type (category) of the digital product.", false),
    INTRODUCTION_DATE            ("introduction_date", ColumnType.DATE, "The data when the product was first made available.", false),
    MATURITY                     ("maturity", ColumnType.STRING, "The level of maturity of the product.", false),
    SERVICE_LIFE                 ("service_life", ColumnType.STRING, "Product service policy.", false),
    CURRENT_VERSION              ("current_version", ColumnType.STRING, "Version identifier of the current version", false),
    NEXT_VERSION                 ("next_version", ColumnType.DATE, "Expected date of the next version.", false),
    WITHDRAW_DATE                ("withdraw_date", ColumnType.DATE, "Planned withdrawal date for the product.", false),


    CLASSIFICATIONS              ("classifications", ColumnType.STRING, "List of classifications associated with the element", false),

    DESCRIPTION                  ("description", ColumnType.STRING, "Description of the element", false),



    ;

    private final String     columnName;
    private final ColumnType columnType;
    private final String     columnDescription;
    private final boolean      isNotNull;


    HarvestOpenMetadataColumn(String     columnName,
                              ColumnType columnType,
                              String     columnDescription,
                              boolean    isNotNull)
    {
        this.columnName        = columnName;
        this.columnType        = columnType;
        this.columnDescription = columnDescription;
        this.isNotNull         = isNotNull;
    }


    /**
     * retrieve the name of the column.
     *
     * @return name
     */
    @Override
    public String getColumnName()
    {
        return columnName;
    }

    /**
     * retrieve the qualified name of the column.
     *
     * @param tableName name of table
     * @return name
     */
    public String getColumnName(String tableName)
    {
        return tableName + "." + columnName;
    }


    /**
     * Return the type of the column.
     *
     * @return ColumnType
     */
    @Override
    public ColumnType getColumnType()
    {
        return columnType;
    }


    /**
     * Return th optional description for the column.
     *
     * @return text
     */
    @Override
    public String getColumnDescription()
    {
        return columnDescription;
    }


    /**
     * Return whether the column is not null;
     *
     * @return boolean
     */
    @Override
    public boolean isNotNull()
    {
        return isNotNull;
    }
}
