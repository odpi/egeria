/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLForeignKey;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLTable;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;

import java.util.*;

/**
 * Defines the tables used to observe the open metadata ecosystem.
 */
public enum HarvestOpenMetadataTable implements PostgreSQLTable
{
    /**
     * Descriptions of known assets.
     */
    ASSET("om_asset",
          "Descriptions of known assets.",
          new HarvestOpenMetadataColumn[]{
                  HarvestOpenMetadataColumn.SYNC_TIME,
                  HarvestOpenMetadataColumn.ASSET_GUID},
          new HarvestOpenMetadataColumn[]{
                  HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                  HarvestOpenMetadataColumn.METADATA_COLLECTION_ID,
                  HarvestOpenMetadataColumn.RESOURCE_NAME,
                  HarvestOpenMetadataColumn.RESOURCE_DESCRIPTION,
                  HarvestOpenMetadataColumn.VERSION_IDENTIFIER,
                  HarvestOpenMetadataColumn.QUALIFIED_NAME,
                  HarvestOpenMetadataColumn.DEPLOYED_IMPLEMENTATION_TYPE,
                  HarvestOpenMetadataColumn.DISPLAY_NAME,
                  HarvestOpenMetadataColumn.DISPLAY_DESCRIPTION,
                  HarvestOpenMetadataColumn.DISPLAY_SUMMARY,
                  HarvestOpenMetadataColumn.ABBREVIATION,
                  HarvestOpenMetadataColumn.USAGE,
                  HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES,
                  HarvestOpenMetadataColumn.OWNER_GUID,
                  HarvestOpenMetadataColumn.OWNER_TYPE_NAME,
                  HarvestOpenMetadataColumn.ORIGIN_ORG_GUID,
                  HarvestOpenMetadataColumn.ORIGIN_BIZ_CAP_GUID,
                  HarvestOpenMetadataColumn.ZONE_NAMES,
                  HarvestOpenMetadataColumn.RESOURCE_LOCATION_GUID,
                  HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL,
                  HarvestOpenMetadataColumn.CONFIDENCE_LEVEL,
                  HarvestOpenMetadataColumn.CRITICALITY_LEVEL,
                  HarvestOpenMetadataColumn.LICENSE_TYPE,
                  HarvestOpenMetadataColumn.CREATION_TIME,
                  HarvestOpenMetadataColumn.CREATION_BY,
                  HarvestOpenMetadataColumn.LAST_UPDATE_TIME,
                  HarvestOpenMetadataColumn.LAST_UPDATED_BY,
                  HarvestOpenMetadataColumn.MAINTAINED_BY,
                  HarvestOpenMetadataColumn.ARCHIVED,
                  HarvestOpenMetadataColumn.TAGS,
                  HarvestOpenMetadataColumn.SEMANTIC_TERM_GUID
          }),


    /**
     * Descriptions of known asset types.
     */
    ASSET_TYPE("om_asset_type",
               "Descriptions of known asset types.",
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.OPEN_METADATA_TYPE},
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.TYPE_DESCRIPTION,
                       HarvestOpenMetadataColumn.SUPER_TYPES}),


    /**
     * Descriptions of known certification types.
     */
    CERTIFICATION_TYPE("om_certification_type",
                       "Descriptions of known certification types.",
                       new HarvestOpenMetadataColumn[]{
                               HarvestOpenMetadataColumn.CERTIFICATION_TYPE_GUID},
                       new HarvestOpenMetadataColumn[]{
                               HarvestOpenMetadataColumn.CERTIFICATION_TITLE,
                               HarvestOpenMetadataColumn.CERTIFICATION_SUMMARY}),


    /**
     * Descriptions of known certifications.
     */
    CERTIFICATION("om_certification",
                  "Descriptions of known certifications.",
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.SYNC_TIME,
                          HarvestOpenMetadataColumn.CERTIFICATION_GUID},
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.CERTIFICATION_TYPE_GUID,
                          HarvestOpenMetadataColumn.REFERENCEABLE_GUID,
                          HarvestOpenMetadataColumn.START_TIMESTAMP,
                          HarvestOpenMetadataColumn.END_TIMESTAMP}),


    /**
     * Counts of the collaboration elements attached to particular elements.
     */
    COLLABORATION_ACTIVITY("om_collaboration_activity",
                           "Counts of the collaboration elements attached to particular elements.",
                           new HarvestOpenMetadataColumn[]{
                                   HarvestOpenMetadataColumn.SYNC_TIME,
                                   HarvestOpenMetadataColumn.ELEMENT_GUID},
                           new HarvestOpenMetadataColumn[]{
                                   HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                                   HarvestOpenMetadataColumn.NUM_COMMENTS,
                                   HarvestOpenMetadataColumn.NUM_RATINGS,
                                   HarvestOpenMetadataColumn.AVG_RATING,
                                   HarvestOpenMetadataColumn.NUM_TAGS,
                                   HarvestOpenMetadataColumn.NUM_LIKES}),


    /**
     * Types of known context events.
     */
    // todo
    CONTEXT_EVENT_TYPE("om_context_event_type",
                       "Types of known context events.",
                       new HarvestOpenMetadataColumn[]{
                               HarvestOpenMetadataColumn.CONTEXT_EVENT_TYPE_GUID},
                       new HarvestOpenMetadataColumn[]{
                               HarvestOpenMetadataColumn.QUALIFIED_NAME,
                               HarvestOpenMetadataColumn.DISPLAY_NAME,
                               HarvestOpenMetadataColumn.DESCRIPTION}),


    /**
     * Known context events.
     */
    // todo
    CONTEXT_EVENT("om_context_event",
                  "Known context events.",
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.CONTEXT_EVENT_GUID},
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.QUALIFIED_NAME,
                          HarvestOpenMetadataColumn.DISPLAY_NAME,
                          HarvestOpenMetadataColumn.DESCRIPTION,
                          HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                          HarvestOpenMetadataColumn.CONTEXT_EVENT_EFFECT,
                          HarvestOpenMetadataColumn.PLANNED_START_DATE,
                          HarvestOpenMetadataColumn.PLANNED_DURATION,
                          HarvestOpenMetadataColumn.ACTUAL_DURATION,
                          HarvestOpenMetadataColumn.REPEAT_INTERVAL,
                          HarvestOpenMetadataColumn.PLANNED_COMPLETION_DATE,
                          HarvestOpenMetadataColumn.ACTUAL_COMPLETION_DATE,
                          HarvestOpenMetadataColumn.REFERENCE_EFFECTIVE_FROM,
                          HarvestOpenMetadataColumn.REFERENCE_EFFECTIVE_TO,
                          HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES}),


    /**
     * Individual contribution levels by karma points.
     */
    CONTRIBUTION("om_contribution",
                 "Individual contribution levels by karma points.",
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.SYNC_TIME,
                         HarvestOpenMetadataColumn.PROFILE_GUID},
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                         HarvestOpenMetadataColumn.KARMA_POINTS}),


    /**
     * Details of external identifiers and associated header information from third party elements.
     */
    CORRELATION_PROPERTIES("om_correlation_properties",
                           "Details of external identifiers and associated header information from third party elements.",
                           new HarvestOpenMetadataColumn[]{
                                   HarvestOpenMetadataColumn.LAST_CONFIRMED_SYNC_TIME,
                                   HarvestOpenMetadataColumn.EXTERNAL_IDENTIFIER,
                                   HarvestOpenMetadataColumn.EXTERNAL_SCOPE_GUID,
                                   HarvestOpenMetadataColumn.ELEMENT_GUID},
                           new HarvestOpenMetadataColumn[]{
                                   HarvestOpenMetadataColumn.CREATION_BY,
                                   HarvestOpenMetadataColumn.CREATION_TIME,
                                   HarvestOpenMetadataColumn.LAST_UPDATED_BY,
                                   HarvestOpenMetadataColumn.LAST_UPDATE_TIME,
                                   HarvestOpenMetadataColumn.VERSION,
                                   HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                                   HarvestOpenMetadataColumn.EGERIA_OWNED,
                                   HarvestOpenMetadataColumn.EXTERNAL_TYPE_NAME,
                                   HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES
    }),


    /**
     * Descriptions of the catalogued data fields.
     */
    DATA_FIELD("om_data_field",
               "Descriptions of the catalogued data fields.",
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.SYNC_TIME,
                       HarvestOpenMetadataColumn.DATA_FIELD_GUID},
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.DATA_FIELD_NAME,
                       HarvestOpenMetadataColumn.DATA_FIELD_TYPE,
                       HarvestOpenMetadataColumn.VERSION_IDENTIFIER,
                       HarvestOpenMetadataColumn.DESCRIPTION,
                       HarvestOpenMetadataColumn.HAS_PROFILE,
                       HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL,
                       HarvestOpenMetadataColumn.SEMANTIC_TERM_GUID,
                       HarvestOpenMetadataColumn.ASSET_GUID,
                       HarvestOpenMetadataColumn.QUALIFIED_NAME
               }),


    /**
     * Descriptions of the known departments.
     */
    DEPARTMENT("om_department",
               "Descriptions of the known departments.",
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.SYNC_TIME,
                       HarvestOpenMetadataColumn.DEPARTMENT_GUID},
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.DEPARTMENT_NAME,
                       HarvestOpenMetadataColumn.DEPARTMENT_IDENTIFIER,
                       HarvestOpenMetadataColumn.MANAGER_PROFILE_GUID,
                       HarvestOpenMetadataColumn.PARENT_DEPARTMENT_GUID
               }),


    /**
     * Descriptions of users working with integrated third party technologies.
     */
    EXTERNAL_USER("om_external_user",
                  "Descriptions of users working with integrated third party technologies.",
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.EXTERNAL_SCOPE_GUID,
                          HarvestOpenMetadataColumn.EXTERNAL_USER_ID},
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.LINKED_USER_IDENTITY_GUID}),


    /**
     * Descriptions of glossaries defined in open metadata.
     */
    GLOSSARY("om_glossary",
             "Descriptions of glossaries defined in open metadata.",
             new HarvestOpenMetadataColumn[]{
                     HarvestOpenMetadataColumn.GLOSSARY_GUID,
                     HarvestOpenMetadataColumn.SYNC_TIME},
             new HarvestOpenMetadataColumn[]{
                     HarvestOpenMetadataColumn.CLASSIFICATIONS,
                     HarvestOpenMetadataColumn.QUALIFIED_NAME,
                     HarvestOpenMetadataColumn.DISPLAY_NAME,
                     HarvestOpenMetadataColumn.DESCRIPTION,
                     HarvestOpenMetadataColumn.GLOSSARY_LANGUAGE,
                     HarvestOpenMetadataColumn.USAGE,
                     HarvestOpenMetadataColumn.NUM_TERMS,
                     HarvestOpenMetadataColumn.NUM_CATEGORIES,
                     HarvestOpenMetadataColumn.NUM_LINKED_TERMS,
                     HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES,
                     HarvestOpenMetadataColumn.OWNER_GUID,
                     HarvestOpenMetadataColumn.OWNER_TYPE_NAME,
                     HarvestOpenMetadataColumn.METADATA_COLLECTION_ID,
                     HarvestOpenMetadataColumn.LICENSE_TYPE,
                     HarvestOpenMetadataColumn.CREATION_TIME,
                     HarvestOpenMetadataColumn.LAST_UPDATE_TIME}),


    /**
     * Descriptions of license types defined in open metadata.
     */
    LICENSE_TYPE("om_license_type",
                 "Descriptions of license types defined in open metadata.",
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.LICENSE_TYPE_GUID,
                         HarvestOpenMetadataColumn.SYNC_TIME},
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.LICENSE_NAME,
                         HarvestOpenMetadataColumn.DESCRIPTION}),


    /**
     * Descriptions of locations defined in open metadata.
     */
    LOCATION("om_location",
             "Descriptions of locations defined in open metadata.",
             new HarvestOpenMetadataColumn[]{
                     HarvestOpenMetadataColumn.LOCATION_GUID},
             new HarvestOpenMetadataColumn[]{
                     HarvestOpenMetadataColumn.LOCATION_NAME,
                     HarvestOpenMetadataColumn.OPEN_METADATA_TYPE}),


    /**
     * Details of the metadata collections within open metadata.
     */
    METADATA_COLLECTION("om_metadata_collection",
                        "Details of the metadata collections within open metadata.",
                        new HarvestOpenMetadataColumn[]{
                                HarvestOpenMetadataColumn.METADATA_COLLECTION_ID},
                        new HarvestOpenMetadataColumn[]{
                                HarvestOpenMetadataColumn.METADATA_COLLECTION_NAME,
                                HarvestOpenMetadataColumn.PROVENANCE_TYPE,
                                HarvestOpenMetadataColumn.DEPLOYED_IMPLEMENTATION_TYPE}),


    /**
     * Identifiers used in the governance action classifications.
     */
    REFERENCE_LEVEL("om_reference_level",
                    "Identifiers used in the governance action classifications.",
                    new HarvestOpenMetadataColumn[]{
                            HarvestOpenMetadataColumn.LEVEL_IDENTIFIER,
                            HarvestOpenMetadataColumn.CLASSIFICATION_NAME},
                    new HarvestOpenMetadataColumn[]{
                            HarvestOpenMetadataColumn.DISPLAY_NAME,
                            HarvestOpenMetadataColumn.DESCRIPTION}),


    /**
     * Details of relationships between assets.
     */
    RELATED_ASSET("om_related_asset",
                  "Details of relationships between assets.",
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.RELATIONSHIP_GUID,
                          HarvestOpenMetadataColumn.SYNC_TIME},
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                          HarvestOpenMetadataColumn.END_1_GUID,
                          HarvestOpenMetadataColumn.END_1_ATTRIBUTE_NAME,
                          HarvestOpenMetadataColumn.END_2_GUID,
                          HarvestOpenMetadataColumn.END_2_ATTRIBUTE_NAME}),


    /**
     * Details of user roles in open metadata.
     */
    ROLE("om_role",
         "Details of user roles in open metadata.",
         new HarvestOpenMetadataColumn[]{
                 HarvestOpenMetadataColumn.ROLE_GUID,
                 HarvestOpenMetadataColumn.SYNC_TIME},
         new HarvestOpenMetadataColumn[]{
                 HarvestOpenMetadataColumn.ROLE_NAME,
                 HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                 HarvestOpenMetadataColumn.HEADCOUNT}),


    /**
     * Details of assignments of people to actor roles in open metadata.
     */
    ROLE_TO_PROFILE("om_role_to_profile",
                 "Details of assignments of people to actor profiles in open metadata.",
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.RELATIONSHIP_GUID,
                         HarvestOpenMetadataColumn.SYNC_TIME},
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.ROLE_GUID,
                         HarvestOpenMetadataColumn.USER_IDENTITY_GUID,
                         HarvestOpenMetadataColumn.PROFILE_GUID,
                         HarvestOpenMetadataColumn.START_TIMESTAMP,
                         HarvestOpenMetadataColumn.END_TIMESTAMP}),


    /**
     * Details of amount of feedback and linked assets associated with a glossary term.
     */
    TERM_ACTIVITY("om_term_activity",
                  "Details of amount of feedback and linked assets associated with a glossary term.",
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.TERM_GUID,
                          HarvestOpenMetadataColumn.SYNC_TIME},
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.TERM_NAME,
                          HarvestOpenMetadataColumn.QUALIFIED_NAME,
                          HarvestOpenMetadataColumn.TERM_SUMMARY,
                          HarvestOpenMetadataColumn.VERSION_IDENTIFIER,
                          HarvestOpenMetadataColumn.OWNER_GUID,
                          HarvestOpenMetadataColumn.OWNER_TYPE_NAME,
                          HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL,
                          HarvestOpenMetadataColumn.CONFIDENCE_LEVEL,
                          HarvestOpenMetadataColumn.CRITICALITY_LEVEL,
                          HarvestOpenMetadataColumn.LAST_FEEDBACK_TIME,
                          HarvestOpenMetadataColumn.CREATION_TIME,
                          HarvestOpenMetadataColumn.NUM_LINKED_ELEMENTS,
                          HarvestOpenMetadataColumn.LAST_LINKED_TIME,
                          HarvestOpenMetadataColumn.GLOSSARY_GUID
                  }),


    /**
     * Details of to dos raised and acted upon in open metadata.
     */
    TO_DO("om_to_do",
          "Details of to dos raised and acted upon in open metadata.",
          new HarvestOpenMetadataColumn[]{
                  HarvestOpenMetadataColumn.TO_DO_GUID,
                  HarvestOpenMetadataColumn.SYNC_TIME},
          new HarvestOpenMetadataColumn[]{
                  HarvestOpenMetadataColumn.QUALIFIED_NAME,
                  HarvestOpenMetadataColumn.TO_DO_TYPE,
                  HarvestOpenMetadataColumn.DISPLAY_NAME,
                  HarvestOpenMetadataColumn.DESCRIPTION,
                  HarvestOpenMetadataColumn.CREATION_TIME,
                  HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                  HarvestOpenMetadataColumn.PRIORITY,
                  HarvestOpenMetadataColumn.DUE_TIME,
                  HarvestOpenMetadataColumn.LAST_REVIEW_TIME,
                  HarvestOpenMetadataColumn.COMPLETION_TIME,
                  HarvestOpenMetadataColumn.TO_DO_STATUS,
                  HarvestOpenMetadataColumn.TO_DO_SOURCE_GUID,
                  HarvestOpenMetadataColumn.TO_DO_SOURCE_TYPE,
                  HarvestOpenMetadataColumn.SPONSOR_GUID,
                  HarvestOpenMetadataColumn.SPONSOR_TYPE
          }),


    /**
     * Details of open metadata user identities.
     */
    USER_IDENTITY("om_user_identity",
                  "Details of open metadata user identities.",
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.USER_IDENTITY_GUID,
                          HarvestOpenMetadataColumn.SYNC_TIME},
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.USER_ID,
                          HarvestOpenMetadataColumn.DISTINGUISHED_NAME}),


    /**
     * Details of a person.
     */
    PERSON("om_person",
                  "Details of a person.",
           new HarvestOpenMetadataColumn[]{
                   HarvestOpenMetadataColumn.PROFILE_GUID,
                   HarvestOpenMetadataColumn.SYNC_TIME},
           new HarvestOpenMetadataColumn[]{
                   HarvestOpenMetadataColumn.EMPLOYEE_NUMBER,
                   HarvestOpenMetadataColumn.PREFERRED_NAME,
                   HarvestOpenMetadataColumn.ORGANIZATION_NAME,
                   HarvestOpenMetadataColumn.RESIDENT_COUNTRY,
                   HarvestOpenMetadataColumn.LOCATION_GUID,
                   HarvestOpenMetadataColumn.DEPARTMENT_GUID
           }),


    /**
     * Details of an actor profile.
     */
    ACTOR_PROFILE("om_actor_profile",
                  "Details of an actor profile.",
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.PROFILE_GUID,
                          HarvestOpenMetadataColumn.SYNC_TIME},
                  new HarvestOpenMetadataColumn[]{
                          HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                          HarvestOpenMetadataColumn.QUALIFIED_NAME,
                          HarvestOpenMetadataColumn.DISPLAY_NAME,
                          HarvestOpenMetadataColumn.DESCRIPTION
                  }),


    /**
     * Details of assignments of user identities to actor profiles in open metadata.
     */
    USER_TO_PROFILE("om_user_to_profile",
                 "Details of assignments of user identities to actor profiles in open metadata.",
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.RELATIONSHIP_GUID,
                         HarvestOpenMetadataColumn.SYNC_TIME},
                 new HarvestOpenMetadataColumn[]{
                         HarvestOpenMetadataColumn.PROFILE_GUID,
                         HarvestOpenMetadataColumn.USER_IDENTITY_GUID,
                         HarvestOpenMetadataColumn.START_TIMESTAMP,
                         HarvestOpenMetadataColumn.END_TIMESTAMP}),


    /**
     * Details of projects recorded in open metadata.
     */
    PROJECT("om_project",
            "Details of projects recorded in open metadata.",
            new HarvestOpenMetadataColumn[]{
                    HarvestOpenMetadataColumn.PROJECT_GUID,
                    HarvestOpenMetadataColumn.SYNC_TIME},
            new HarvestOpenMetadataColumn[]{
                    HarvestOpenMetadataColumn.QUALIFIED_NAME,
                    HarvestOpenMetadataColumn.PROJECT_IDENTIFIER,
                    HarvestOpenMetadataColumn.DISPLAY_NAME,
                    HarvestOpenMetadataColumn.DESCRIPTION,
                    HarvestOpenMetadataColumn.CREATION_TIME,
                    HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                    HarvestOpenMetadataColumn.CLASSIFICATIONS,
                    HarvestOpenMetadataColumn.PRIORITY,
                    HarvestOpenMetadataColumn.DUE_TIME,
                    HarvestOpenMetadataColumn.PROJECT_PHASE,
                    HarvestOpenMetadataColumn.PROJECT_STATUS,
                    HarvestOpenMetadataColumn.PROJECT_HEALTH,
                    HarvestOpenMetadataColumn.PARENT_GUID
            }),



    /**
     * Details of communities recorded in open metadata.
     */
    COMMUNITY("om_community",
            "Details of communities recorded in open metadata.",
            new HarvestOpenMetadataColumn[]{
                    HarvestOpenMetadataColumn.COMMUNITY_GUID,
                    HarvestOpenMetadataColumn.SYNC_TIME},
            new HarvestOpenMetadataColumn[]{
                    HarvestOpenMetadataColumn.QUALIFIED_NAME,
                    HarvestOpenMetadataColumn.DISPLAY_NAME,
                    HarvestOpenMetadataColumn.DESCRIPTION,
                    HarvestOpenMetadataColumn.CREATION_TIME,
                    HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                    HarvestOpenMetadataColumn.CLASSIFICATIONS,
                    HarvestOpenMetadataColumn.MISSION
            }),


    /**
     * Details of assignments of actors to work in open metadata.
     */
    ACTOR_ASSIGNMENTS("om_actor_assignments",
                    "Details of assignments of actors to work in open metadata.",
                    new HarvestOpenMetadataColumn[]{
                            HarvestOpenMetadataColumn.RELATIONSHIP_GUID,
                            HarvestOpenMetadataColumn.SYNC_TIME},
                    new HarvestOpenMetadataColumn[]{
                            HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                            HarvestOpenMetadataColumn.ASSIGNED_ACTOR_GUID,
                            HarvestOpenMetadataColumn.ASSIGNED_ACTOR_TYPE,
                            HarvestOpenMetadataColumn.ASSIGNED_WORK_GUID,
                            HarvestOpenMetadataColumn.ASSIGNED_WORK_TYPE,
                            HarvestOpenMetadataColumn.START_TIMESTAMP,
                            HarvestOpenMetadataColumn.END_TIMESTAMP}),


    /**
     * Details of collections in use in open metadata.
     */
    COLLECTION("om_collection",
              "Details of collections in use in open metadata.",
              new HarvestOpenMetadataColumn[]{
                      HarvestOpenMetadataColumn.COLLECTION_GUID,
                      HarvestOpenMetadataColumn.SYNC_TIME},
              new HarvestOpenMetadataColumn[]{
                      HarvestOpenMetadataColumn.QUALIFIED_NAME,
                      HarvestOpenMetadataColumn.DISPLAY_NAME,
                      HarvestOpenMetadataColumn.DESCRIPTION,
                      HarvestOpenMetadataColumn.CREATION_TIME,
                      HarvestOpenMetadataColumn.OPEN_METADATA_TYPE,
                      HarvestOpenMetadataColumn.CLASSIFICATIONS,
                      HarvestOpenMetadataColumn.NUM_LINKED_ELEMENTS,
                      HarvestOpenMetadataColumn.MEMBER_TYPES,
                      HarvestOpenMetadataColumn.PARENT_GUID
              }),

    /**
     * Details of collections in use in open metadata.
     */
    DIGITAL_PRODUCT("om_digital_product",
               "Details of the digital products in open metadata.",
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.COLLECTION_GUID,
                       HarvestOpenMetadataColumn.SYNC_TIME},
               new HarvestOpenMetadataColumn[]{
                       HarvestOpenMetadataColumn.PRODUCT_STATUS,
                       HarvestOpenMetadataColumn.PRODUCT_NAME,
                       HarvestOpenMetadataColumn.PRODUCT_TYPE,
                       HarvestOpenMetadataColumn.INTRODUCTION_DATE,
                       HarvestOpenMetadataColumn.MATURITY,
                       HarvestOpenMetadataColumn.SERVICE_LIFE,
                       HarvestOpenMetadataColumn.CURRENT_VERSION,
                       HarvestOpenMetadataColumn.NEXT_VERSION,
                       HarvestOpenMetadataColumn.WITHDRAW_DATE
               }),

    ;

    private final String                      tableName;
    private final String                      tableDescription;
    private final HarvestOpenMetadataColumn[] primaryKeys;
    private final HarvestOpenMetadataColumn[] dataColumns;


    /**
     * Define a repository table.
     *
     * @param tableName name of the table
     * @param tableDescription description of the table
     * @param primaryKeys list of primary keys
     * @param dataColumns list of additional columns
     */
    HarvestOpenMetadataTable(String                      tableName,
                             String                      tableDescription,
                             HarvestOpenMetadataColumn[] primaryKeys,
                             HarvestOpenMetadataColumn[] dataColumns)
    {
        this.tableName        = tableName;
        this.tableDescription = tableDescription;
        this.primaryKeys      = primaryKeys;
        this.dataColumns      = dataColumns;
    }


    /**
     * Return the name of the table.
     *
     * @return name
     */
    @Override
    public String getTableName()
    {
        return tableName;
    }




    /**
     * Return the name of the table.
     *
     * @param schemaName name of schema
     * @return name
     */
    @Override
    public String getTableName(String schemaName)
    {
        return schemaName + "." + tableName;
    }



    /**
     * Return the description of the table.
     *
     * @return text
     */
    @Override
    public String getTableDescription()
    {
        return tableDescription;
    }


    /**
     * Return the columns that are primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getPrimaryKeys()
    {
        if (primaryKeys != null)
        {
            return Arrays.asList(primaryKeys);
        }

        return null;
    }


    /**
     * Return the columns that are not primary keys.
     *
     * @return list of columns
     */
    @Override
    public List<PostgreSQLColumn> getDataColumns()
    {
        if (dataColumns != null)
        {
            return Arrays.asList(dataColumns);
        }

        return null;
    }


    /**
     * Return the name to type map for the columns in this table.
     *
     * @return map
     */
    public Map<String, Integer> getColumnNameTypeMap()
    {
        Map<String, Integer> columnNameTypeMap = new HashMap<>();

        if (primaryKeys != null)
        {
            for (HarvestOpenMetadataColumn column: primaryKeys)
            {
                columnNameTypeMap.put(column.getColumnName(), column.getColumnType().getJdbcType());
            }
        }

        if (dataColumns != null)
        {
            for (HarvestOpenMetadataColumn column: dataColumns)
            {
                columnNameTypeMap.put(column.getColumnName(), column.getColumnType().getJdbcType());
            }
        }


        return columnNameTypeMap;
    }


    /**
     * Return the list of foreign keys for this table.
     *
     * @return list
     */
    @Override
    public List<PostgreSQLForeignKey> getForeignKeys()
    {
        return null;
    }


    /**
     * Return the tables for schema building.
     *
     * @return list of tables
     */
    public static List<PostgreSQLTable> getTables()
    {
        return new ArrayList<>(Arrays.asList(HarvestOpenMetadataTable.values()));
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "HarvestOpenMetadataTable{" + tableName + "}";
    }
}
