/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.loadobservations;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataCorrelationHeader;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.DataAssetProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermProperties;
import org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.loadobservations.ffdc.LoadObservationsAuditCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.loadobservations.ffdc.LoadObservationsErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementVersions;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;
import org.odpi.openmetadata.frameworks.governanceaction.search.EnumTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.SearchProperties;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;
import org.odpi.openmetadata.integrationservices.catalog.connector.DataAssetExchangeService;
import org.odpi.openmetadata.integrationservices.catalog.connector.GlossaryExchangeService;

import java.sql.Connection;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * LoadObservationsByEgeriaConnector extracts relevant metadata from the open metadata ecosystem into the ObservationsByEgeria database.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by Asset Manager OMAS/Catalog Integrator OMIS.
 */
public class LoadObservationsByEgeriaConnector extends CatalogIntegratorConnector implements AssetManagerEventListener
{
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    /*
     * Names of the database tables
     */
    private static final String assetDatabaseTable                  = "om_asset";
    private static final String assetTypesDatabaseTable             = "om_asset_types";
    private static final String certificationTypeDatabaseTable      = "om_certification_type";
    private static final String certificationsDatabaseTable         = "om_certifications";
    private static final String collaborationActivityDatabaseTable  = "om_collaboration_activity";
    private static final String contributionsDatabaseTable          = "om_contributions";
    private static final String contextEventTypesDatabaseTable      = "om_context_event_types";
    private static final String contextEventsDatabaseTable          = "om_context_events";
    private static final String correlationPropertiesDatabaseTable  = "om_correlation_properties";
    private static final String dataFieldsDatabaseTable             = "om_data_fields";
    private static final String departmentDatabaseTable             = "om_department";
    private static final String externalUserDatabaseTable           = "om_external_user";
    private static final String glossaryDatabaseTable               = "om_glossary";
    private static final String licenseDatabaseTable                = "om_license";
    private static final String locationDatabaseTable               = "om_location";
    private static final String metadataCollectionDatabaseTable     = "om_metadata_collection";
    private static final String referenceLevelsDatabaseTable        = "om_reference_levels";
    private static final String relatedAssetDatabaseTable           = "om_related_assets";
    private static final String roleDatabaseTable                   = "om_role";
    private static final String roleToUserDatabaseTable             = "om_role2user";
    private static final String termActivityDatabaseTable           = "om_term_activity";
    private static final String toDoDatabaseTable                   = "om_todo";
    private static final String userIdentityDatabaseTable           = "om_user_identity";
    private static final String externalTypeAttributesDatabaseTable = "ds_external_type_attributes";
    private static final String externalTypesDatabaseTable          = "ds_external_types";
    private static final String missingFileClassifiersDatabaseTable = "rd_file_classifiers";
    private static final String surveyReportDatabaseTable           = "sr_report";
    private static final String dataSourceMeasurementsDatabaseTable = "sr_data_source_measurement";
    private static final String dataProfileDatabaseTable            = "sr_data_profile";
    private static final String requestForActionDatabaseTable       = "sr_request_for_action";


    /*
     * Column names
     */
    private static final String columnNameSyncTime                   = "sync_time";
    private static final String columnNameLastConfirmedSyncTime      = "last_confirmed_sync_time";
    private static final String columnNameResourceName               = "resource_name";
    private static final String columnNameResourceDescription        = "resource_description";
    private static final String columnNameVersionId                  = "version_id";
    private static final String columnNameDisplayName                = "display_name";
    private static final String columnNameDisplayDescription         = "display_description";
    private static final String columnNameAssetGUID                  = "asset_guid";
    private static final String columnNameAssetType                  = "asset_type";
    private static final String columnNameQualifiedName              = "qualified_name";
    private static final String columnNameDisplaySummary             = "display_summary";
    private static final String columnNameAbbrev                     = "abbrev";
    private static final String columnNameUsage                      = "usage";
    private static final String columnNameTags                       = "tags";
    private static final String columnNameAdditionalProperties       = "additional_properties";
    private static final String columnNameOwnerGUID                  = "owner_guid";
    private static final String columnNameOwnerType                  = "owner_type";
    private static final String columnNameOriginOrgGUID              = "origin_org_guid";
    private static final String columnNameOriginBizCapGUID           = "origin_biz_cap_guid";
    private static final String columnNameZoneNames                  = "zone_names";
    private static final String columnNameDeployedImplementationType = "deployed_implementation_type";
    private static final String columnNameResourceLocGUID            = "resource_loc_guid";
    private static final String columnNameConfidentiality            = "confidentiality";
    private static final String columnNameConfidence                 = "confidence";
    private static final String columnNameCriticality                = "criticality";
    private static final String columnNameMetaCollectionId           = "meta_collection_id";
    private static final String columnNameLicenseGUID                = "license_guid";
    private static final String columnNameLicenseName                = "license_name";
    private static final String columnNameLicenseDescription         = "license_description";
    private static final String columnNameLastUpdateTimestamp        = "last_update_timestamp";
    private static final String columnNameCreationTimestamp          = "creation_timestamp";
    private static final String columnNameExternalIdentifier         = "external_identifier";
    private static final String columnNameLastUpdatedBy              = "last_updated_by";
    private static final String columnNameLastUpdateTime             = "last_update_time";
    private static final String columnNameCreatedBy                  = "created_by";
    private static final String columnNameMaintainedBy               = "maintained_by";
    private static final String columnNameArchived                   = "archived";
    private static final String columnNameVersion                    = "version";
    private static final String columnNameCreationTime               = "creation_time";
    private static final String columnNameTypeName                   = "type_name";
    private static final String columnNameEgeriaOwned                = "egeria_owned";
    private static final String columnNameElementGUID                = "element_guid";
    private static final String columnNameExternalSourceGUID         = "external_source_guid";
    private static final String columnNameMetadataCollectionId       = "metadata_collection_id";
    private static final String columnNameMetadataCollectionName     = "metadata_collection_name";
    private static final String columnNameMetadataCollectionType     = "metadata_collection_type";
    private static final String columnNameDeployedImplType           = "deployed_impl_type";
    private static final String columnNameNumberOfComments           = "num_comments";
    private static final String columnNameNumberOfRatings            = "num_ratings";
    private static final String columnNameAverageRating              = "avg_rating";
    private static final String columnNameNumberOfLikes              = "num_likes";
    private static final String columnNameNumberOfInformalTags       = "num_tags";
    private static final String columnNameEnd1GUID                   = "end1_guid";
    private static final String columnNameEnd1AttributeName          = "end1_attribute_nm";
    private static final String columnNameEnd2GUID                   = "end2_guid";
    private static final String columnNameEnd2AttributeName          = "end2_attribute_nm";
    private static final String columnNameRelationshipTypeName       = "rel_type_nm";
    private static final String columnNameRelationshipGUID           = "relationship_guid";
    private static final String columnNameReferenceableGUID          = "referenceable_guid";
    private static final String columnNameCertificationGUID          = "certification_guid";
    private static final String columnNameCertificationTypeGUID      = "certification_type_guid";
    private static final String columnNameCertificationTitle         = "certification_title";
    private static final String columnNameCertificationSummary       = "certification_summary";
    private static final String columnNameStartDate                  = "start_date";
    private static final String columnNameEndDate                    = "end_date";
    private static final String columnNameGlossaryGUID               = "glossary_guid";
    private static final String columnNameGlossaryName               = "glossary_name";
    private static final String columnNameGlossaryLanguage           = "glossary_language";
    private static final String columnNameGlossaryDescription        = "glossary_description";
    private static final String columnNameClassifications            = "classifications";
    private static final String columnNameNumberOfTerms              = "number_terms";
    private static final String columnNameNumberOfCategories         = "number_categories";
    private static final String columnNameNumberOfLinkedTerms        = "num_linked_terms";
    private static final String columnNameTermGUID                   = "term_guid";
    private static final String columnNameTermName                   = "term_name";
    private static final String columnNameTermSummary                = "term_summary";
    private static final String columnNameLastFeedbackTimestamp      = "last_feedback_timestamp";
    private static final String columnNameNumberLinkedElements       = "number_linked_element";
    private static final String columnNameLastLinkTimestamp          = "last_link_timestamp";
    private static final String columnNameDataFieldGUID              = "data_field_guid";
    private static final String columnNameDataFieldName              = "data_field_name";
    private static final String columnNameSemanticTerm               = "semantic_term";
    private static final String columnNameHasProfile                 = "has_profile";
    private static final String columnNameAssetQualifiedName         = "asset_qualified_name";
    private static final String columnNameConfidentialityLevel       = "confidentiality_level";
    private static final String columnNameLeafType                   = "leaf_type";
    private static final String columnNameTypeDescription            = "type_description";
    private static final String columnNameSuperTypes                 = "super_types";
    private static final String columnNameLocationGUID               = "location_guid";
    private static final String columnNameLocationName               = "location_name";
    private static final String columnNameLocationType               = "location_type";
    private static final String columnNameUserGUID                   = "user_guid";
    private static final String columnNameSnapshotTimestamp          = "snapshot_timestamp";
    private static final String columnNameKarmaPoints                = "karma_points";
    private static final String columnNameDepGUID                    = "dep_id";
    private static final String columnNameDeptName                   = "dep_name";
    private static final String columnNameManager                    = "manager";
    private static final String columnNameParentDepartmentGUID       = "parent_department";
    private static final String columnNameEmployeeNumber             = "employee_num";
    private static final String columnNameUserId                     = "user_id";
    private static final String columnNamePreferredName              = "preferred_name";
    private static final String columnNameOrgName                    = "org_name";
    private static final String columnNameResidentCountry            = "resident_country";
    private static final String columnNameLocation                   = "location";
    private static final String columnNameDistinguishedName          = "distinguished_name";
    private static final String columnNameExternalUser               = "external_user";
    private static final String columnNameUserIdGUID                 = "user_id_guid";
    private static final String columnNameProfileGUID                = "profile_guid";
    private static final String columnNameDepartmentGUID             = "department_id";
    private static final String columnNameRoleGUID                   = "role_guid";
    private static final String columnNameRoleName                   = "role_name";
    private static final String columnNameRoleType                   = "role_type";
    private static final String columnNameHeadCount                  = "headcount";
    private static final String columnNameIdentifier                 = "identifier";
    private static final String columnNameClassificationName         = "classification_name";
    private static final String columnNameText                       = "text";
    private static final String columnNameGUID                       = "guid";
    private static final String columnNameCETypeName                 = "ce_type_name";
    private static final String columnNameDescription                = "description";
    private static final String columnNameEventEffect                = "event_effect";
    private static final String columnNameContextEventType           = "context_event_type";
    private static final String columnNamePlannedStartDate           = "planned_start_date";
    private static final String columnNameActualStartDate            = "actual_start_date";
    private static final String columnNamePlannedDuration            = "planned_duration";
    private static final String columnNameActualDuration             = "actual_duration";
    private static final String columnNameRepeatInterval             = "repeat_interval";
    private static final String columnNamePlannedCompletionDate      = "planned_completion_date";
    private static final String columnNameActualCompletionDate       = "actual_completion_date";
    private static final String columnNameReferenceEffectiveFrom     = "reference_effective_from";
    private static final String columnNameReferenceEffectiveTo       = "reference_effective_to";
    private static final String columnNameTypeVersion                = "type_version";
    private static final String columnNameTypeCategory               = "type_category";
    private static final String columnNameAttributeName              = "attribute_name";
    private static final String columnNameAttributeTypeName          = "attribute_type_name";
    private static final String columnNameNumInstances               = "num_instances";
    private static final String columnNameNumInstancesWithSubtype    = "num_instances_with_subtype";
    private static final String columnNameSurveyGUID                 = "survey_guid";
    private static final String columnNameEngineActionGUID           = "engine_action_guid";
    private static final String columnNameRequestType                = "request_type";
    private static final String columnNameGovernanceEngineName       = "governance_engine_name";
    private static final String columnNameEngineHostUserId           = "engine_host_user_id";
    private static final String columnNameInitiator                  = "initiator"; // engine action createdBy user_id
    private static final String columnNameEndTimestamp               = "end_timestamp";
    private static final String columnNameStartTimestamp             = "start_timestamp";
    private static final String columnNameSRGUID                     = "sr_guid";
    private static final String columnNameSubjectGUID                = "subject_guid";
    private static final String columnNameSubjectType                = "subject_type";
    private static final String columnNameAnnotationGUID             = "annotation_guid";
    private static final String columnNameMeasurementName            = "measurement_name";
    private static final String columnNameMeasurementValue           = "measurement_value";
    private static final String columnNameMeasurementCategory        = "measurement_category";
    private static final String columnNameActionRequestName          = "action_request_name";
    private static final String columnNameActionTargetGUID           = "action_target_guid";
    private static final String columnNameActionTargetType           = "action_target_type";
    private static final String columnNameToDoGUID                   = "todo_guid";
    private static final String columnNameToDoType                   = "todo_type";
    private static final String columnNamePriority                   = "priority";
    private static final String columnNameDueTime                    = "due_time";
    private static final String columnNameCompletionTime             = "completion_time";
    private static final String columnNameLastReviewedTime           = "last_reviewed_time";
    private static final String columnNameStatus                     = "status";
    private static final String columnNameToDoSourceGUID             = "todo_source_guid";
    private static final String columnNameToDoSourceType             = "todo_source_type";
    private static final String columnNameSurveyReportGUID           = "survey_report_guid";
    private static final String columnNamePurpose                    = "purpose";
    private static final String columnNameFilename                   = "filename";
    private static final String columnNameFileExtension              = "file_extension";
    private static final String columnNamePathname                   = "pathname";
    private static final String columnNameFileType                   = "file_type";
    private static final String columnNameEncoding                   = "encoding";
    private static final String columnNameActorGUID                  = "actor_guid";
    private static final String columnNameActorType                  = "actor_type";


    private String                   connectionURL      = null;
    private CatalogIntegratorContext myContext          = null;
    private JDBCResourceConnector    databaseClient     = null;
    private Connection               databaseConnection = null;

    private final PropertyHelper       propertyHelper                    = new PropertyHelper();

    /*
     * These maps contain the details of the columns in the tables that produce
     * a new row each time an update is made.  This is accomplished by having the sync_time
     * column as part of the primary key.
     */

    private final Map<String, Integer> assetTableColumns                 = new HashMap<>();
    private final Map<String, Integer> certificationsTableColumns        = new HashMap<>();
    private final Map<String, Integer> collaborationActivityTableColumns = new HashMap<>();
    private final Map<String, Integer> contributionsTableColumns         = new HashMap<>();
    private final Map<String, Integer> dataFieldsTableColumns            = new HashMap<>();
    private final Map<String, Integer> departmentTableColumns            = new HashMap<>();
    private final Map<String, Integer> glossaryTableColumns              = new HashMap<>();
    private final Map<String, Integer> relatedAssetsTableColumns         = new HashMap<>();
    private final Map<String, Integer> roleTableColumns                  = new HashMap<>();
    private final Map<String, Integer> role2UserTableColumns             = new HashMap<>();
    private final Map<String, Integer> termActivityTableColumns          = new HashMap<>();
    private final Map<String, Integer> toDoTableColumns                  = new HashMap<>();
    private final Map<String, Integer> userIdentityTableColumns          = new HashMap<>();
    private final Map<String, Integer> fileClassifiersTableColumns       = new HashMap<>();

    private DataAssetExchangeService   dataAssetExchangeService = null;
    private GlossaryExchangeService    glossaryExchangeService  = null;
    private OpenMetadataAccess         openMetadataAccess       = null;

    /* ==============================================================================
     * Standard methods that trigger activity.
     */

    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        myContext = super.getContext();

        if ((connectionProperties.getUserId() == null) || (connectionProperties.getClearPassword() == null))
        {
            throw new ConnectorCheckedException(LoadObservationsErrorCode.NULL_USER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Retrieve the configuration
         */
        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            connectionURL = endpoint.getAddress();
        }

        if (connectionURL == null)
        {
            throw new ConnectorCheckedException(LoadObservationsErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }


        if (myContext.getAssetManagerName() != null)
        {
            throw new ConnectorCheckedException(LoadObservationsErrorCode.NOT_NULL_ASSET_MANAGER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }


        try
        {
            /*
             * Set up the context clients.
             */
            dataAssetExchangeService = myContext.getDataAssetExchangeService();
            dataAssetExchangeService.setForLineage(true);
            dataAssetExchangeService.setForDuplicateProcessing(true);

            glossaryExchangeService = myContext.getGlossaryExchangeService();
            glossaryExchangeService.setForLineage(true);
            glossaryExchangeService.setForDuplicateProcessing(true);

            openMetadataAccess = myContext.getIntegrationGovernanceContext().getOpenMetadataAccess();

            /*
             * Set up the database client.  Create a connection object that is based on the connection properties passed to
             * this connector, but with the connector type set to return a JDBCResourceConnector.
             */
            JDBCResourceConnectorProvider jdbcResourceProvider = new JDBCResourceConnectorProvider();
            ConnectionProperties          jdbcResourceConnection = new ConnectionProperties(this.connectionProperties,
                                                                                            jdbcResourceProvider.getConnectorType());

            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);

            Connector newConnector = connectorBroker.getConnector(jdbcResourceConnection);

            if (newConnector instanceof JDBCResourceConnector jdbcResourceConnector)
            {
                this.databaseClient = jdbcResourceConnector;
                this.databaseClient.start();
                this.databaseConnection = jdbcResourceConnector.getDataSource().getConnection();
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.BAD_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       connectionURL,
                                                                                                       methodName,
                                                                                                       error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(LoadObservationsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }

        this.fillAssetTableColumns();
        this.fillCertificationsTableColumns();
        this.fillCollaborationActivityTableColumns();
        this.fillContributionsTableColumns();
        this.fillDataFieldsTableColumns();
        this.fillDepartmentTableColumns();
        this.fillGlossaryTableColumns();
        this.fillRelatedAssetsTableColumns();
        this.fillRoleTableColumns();
        this.fillRole2UserTableColumns();
        this.fillTermActivityTableColumns();
        this.fillToDoTableColumns();
        this.fillUserIdentityTableColumns();
        this.fillFileClassifiersTableColumns();
    }


    /**
     * Set up the definitions of the columns in the om_asset table.
     */
    private void fillAssetTableColumns()
    {
        assetTableColumns.put(columnNameResourceName, Types.VARCHAR);
        assetTableColumns.put(columnNameResourceDescription, Types.VARCHAR);
        assetTableColumns.put(columnNameVersionId, Types.VARCHAR);
        assetTableColumns.put(columnNameDisplayName, Types.VARCHAR);
        assetTableColumns.put(columnNameDisplayDescription, Types.VARCHAR);
        assetTableColumns.put(columnNameAssetGUID, Types.VARCHAR);
        assetTableColumns.put(columnNameQualifiedName, Types.VARCHAR);
        assetTableColumns.put(columnNameDisplaySummary, Types.VARCHAR);
        assetTableColumns.put(columnNameAbbrev, Types.VARCHAR);
        assetTableColumns.put(columnNameUsage, Types.VARCHAR);
        assetTableColumns.put(columnNameAdditionalProperties, Types.VARCHAR);
        assetTableColumns.put(columnNameOwnerGUID, Types.VARCHAR);
        assetTableColumns.put(columnNameOwnerType, Types.VARCHAR);
        assetTableColumns.put(columnNameOriginOrgGUID, Types.VARCHAR);
        assetTableColumns.put(columnNameOriginBizCapGUID, Types.VARCHAR);
        assetTableColumns.put(columnNameZoneNames, Types.VARCHAR);
        assetTableColumns.put(columnNameAssetType, Types.VARCHAR);
        assetTableColumns.put(columnNameResourceLocGUID, Types.VARCHAR);
        assetTableColumns.put(columnNameConfidentiality, Types.INTEGER);
        assetTableColumns.put(columnNameConfidence, Types.INTEGER);
        assetTableColumns.put(columnNameCriticality, Types.INTEGER);
        assetTableColumns.put(columnNameMetaCollectionId, Types.VARCHAR);
        assetTableColumns.put(columnNameLicenseGUID, Types.VARCHAR);
        assetTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
        assetTableColumns.put(columnNameLastUpdateTimestamp, Types.TIMESTAMP);
        assetTableColumns.put(columnNameLastUpdatedBy, Types.VARCHAR);
        assetTableColumns.put(columnNameCreationTimestamp, Types.TIMESTAMP);
        assetTableColumns.put(columnNameCreatedBy, Types.VARCHAR);
        assetTableColumns.put(columnNameMaintainedBy, Types.VARCHAR);
        assetTableColumns.put(columnNameArchived, Types.TIMESTAMP);
        assetTableColumns.put(columnNameTags, Types.VARCHAR);
        assetTableColumns.put(columnNameSemanticTerm, Types.VARCHAR);
    }

    /**
     * Set up the definitions of the columns in the om_certifications table.
     */
    private void fillCertificationsTableColumns()
    {
        certificationsTableColumns.put(columnNameReferenceableGUID, Types.VARCHAR);
        certificationsTableColumns.put(columnNameCertificationGUID, Types.VARCHAR);
        certificationsTableColumns.put(columnNameCertificationTypeGUID, Types.VARCHAR);
        certificationsTableColumns.put(columnNameStartDate, Types.TIMESTAMP);
        certificationsTableColumns.put(columnNameEndDate, Types.TIMESTAMP);
    }


    /**
     * Set up the definitions of the columns in the om_collaboration activity table.
     */
    private void fillCollaborationActivityTableColumns()
    {
        collaborationActivityTableColumns.put(columnNameElementGUID, Types.VARCHAR);
        collaborationActivityTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
        collaborationActivityTableColumns.put(columnNameNumberOfComments, Types.INTEGER);
        collaborationActivityTableColumns.put(columnNameNumberOfRatings, Types.INTEGER);
        collaborationActivityTableColumns.put(columnNameAverageRating, Types.INTEGER);
        collaborationActivityTableColumns.put(columnNameNumberOfInformalTags, Types.INTEGER);
        collaborationActivityTableColumns.put(columnNameNumberOfLikes, Types.INTEGER);
    }


    /**
     * Set up the definitions of the columns in the om_contributions table.
     */
    private void fillContributionsTableColumns()
    {
        contributionsTableColumns.put(columnNameUserGUID, Types.VARCHAR);
        contributionsTableColumns.put(columnNameSnapshotTimestamp, Types.TIMESTAMP);
        contributionsTableColumns.put(columnNameKarmaPoints, Types.BIGINT);
    }


    /**
     * Set up the definitions of the columns in the om_data_fields table.
     */
    private void fillDataFieldsTableColumns()
    {
        contributionsTableColumns.put(columnNameDataFieldGUID, Types.VARCHAR);
        contributionsTableColumns.put(columnNameDataFieldName, Types.VARCHAR);
        contributionsTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
        contributionsTableColumns.put(columnNameVersionId, Types.VARCHAR);
        contributionsTableColumns.put(columnNameSemanticTerm, Types.VARCHAR);
        contributionsTableColumns.put(columnNameHasProfile, Types.BOOLEAN);
        contributionsTableColumns.put(columnNameConfidentialityLevel, Types.INTEGER);
        contributionsTableColumns.put(columnNameAssetGUID, Types.VARCHAR);
        contributionsTableColumns.put(columnNameAssetQualifiedName, Types.VARCHAR);
    }


    /**
     * Set up the definitions of the columns in the om_data_fields table.
     */
    private void fillDepartmentTableColumns()
    {
        contributionsTableColumns.put(columnNameDepGUID, Types.VARCHAR);
        contributionsTableColumns.put(columnNameDeptName, Types.VARCHAR);
        contributionsTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
        contributionsTableColumns.put(columnNameManager, Types.VARCHAR);
        contributionsTableColumns.put(columnNameParentDepartmentGUID, Types.VARCHAR);
    }


    /**
     * Set up the definitions of the columns in the om_glossary table.
     */
    private void fillGlossaryTableColumns()
    {
        glossaryTableColumns.put(columnNameGlossaryName, Types.VARCHAR);
        glossaryTableColumns.put(columnNameGlossaryLanguage, Types.VARCHAR);
        glossaryTableColumns.put(columnNameClassifications, Types.VARCHAR);
        glossaryTableColumns.put(columnNameGlossaryDescription, Types.VARCHAR);
        glossaryTableColumns.put(columnNameGlossaryGUID, Types.VARCHAR);
        glossaryTableColumns.put(columnNameQualifiedName, Types.VARCHAR);
        glossaryTableColumns.put(columnNameNumberOfTerms, Types.INTEGER);
        glossaryTableColumns.put(columnNameNumberOfCategories, Types.INTEGER);
        glossaryTableColumns.put(columnNameNumberOfLinkedTerms, Types.INTEGER);
        glossaryTableColumns.put(columnNameUsage, Types.VARCHAR);
        glossaryTableColumns.put(columnNameAdditionalProperties, Types.VARCHAR);
        glossaryTableColumns.put(columnNameOwnerGUID, Types.VARCHAR);
        glossaryTableColumns.put(columnNameOwnerType, Types.VARCHAR);
        glossaryTableColumns.put(columnNameMetaCollectionId, Types.VARCHAR);
        glossaryTableColumns.put(columnNameLicenseGUID, Types.VARCHAR);
        glossaryTableColumns.put(columnNameLastUpdateTimestamp, Types.TIMESTAMP);
        glossaryTableColumns.put(columnNameCreationTimestamp, Types.TIMESTAMP);
        glossaryTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


    /**
     * Set up the definitions of the columns in the om_related_asset table.
     */
    private void fillRelatedAssetsTableColumns()
    {
        relatedAssetsTableColumns.put(columnNameEnd1GUID, Types.VARCHAR);
        relatedAssetsTableColumns.put(columnNameEnd2GUID, Types.VARCHAR);
        relatedAssetsTableColumns.put(columnNameEnd1AttributeName, Types.VARCHAR);
        relatedAssetsTableColumns.put(columnNameEnd2AttributeName, Types.VARCHAR);
        relatedAssetsTableColumns.put(columnNameRelationshipTypeName, Types.VARCHAR);
        relatedAssetsTableColumns.put(columnNameRelationshipGUID, Types.VARCHAR);
        relatedAssetsTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


    /**
     * Set up the definitions of the columns in the om_role table.
     */
    private void fillRoleTableColumns()
    {
        roleTableColumns.put(columnNameRoleGUID, Types.VARCHAR);
        roleTableColumns.put(columnNameRoleName, Types.VARCHAR);
        roleTableColumns.put(columnNameRoleType, Types.VARCHAR);
        roleTableColumns.put(columnNameHeadCount, Types.VARCHAR);
        roleTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


    /**
     * Set up the definitions of the columns in the om_role2user table.
     */
    private void fillRole2UserTableColumns()
    {
        role2UserTableColumns.put(columnNameRoleGUID, Types.VARCHAR);
        role2UserTableColumns.put(columnNameUserGUID, Types.VARCHAR);
        role2UserTableColumns.put(columnNameRelationshipGUID, Types.VARCHAR);
        role2UserTableColumns.put(columnNameStartDate, Types.TIMESTAMP);
        role2UserTableColumns.put(columnNameEndDate, Types.TIMESTAMP);
        role2UserTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


    /**
     * Set up the definitions of the columns in the om_term_activity table.
     */
    private void fillTermActivityTableColumns()
    {
        termActivityTableColumns.put(columnNameTermName, Types.VARCHAR);
        termActivityTableColumns.put(columnNameTermGUID, Types.VARCHAR);
        termActivityTableColumns.put(columnNameQualifiedName, Types.VARCHAR);
        termActivityTableColumns.put(columnNameTermSummary, Types.VARCHAR);
        termActivityTableColumns.put(columnNameVersionId, Types.VARCHAR);
        termActivityTableColumns.put(columnNameOwnerGUID, Types.VARCHAR);
        termActivityTableColumns.put(columnNameOwnerType, Types.VARCHAR);
        termActivityTableColumns.put(columnNameConfidentiality, Types.INTEGER);
        termActivityTableColumns.put(columnNameConfidence, Types.INTEGER);
        termActivityTableColumns.put(columnNameCriticality, Types.INTEGER);
        termActivityTableColumns.put(columnNameLastFeedbackTimestamp, Types.TIMESTAMP);
        termActivityTableColumns.put(columnNameCreationTimestamp, Types.TIMESTAMP);
        termActivityTableColumns.put(columnNameLastLinkTimestamp, Types.TIMESTAMP);
        termActivityTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


    /**
     * Set up the definitions of the columns in the om_todo table.
     */
    private void fillToDoTableColumns()
    {
        toDoTableColumns.put(columnNameToDoGUID, Types.VARCHAR);
        toDoTableColumns.put(columnNameQualifiedName, Types.VARCHAR);
        toDoTableColumns.put(columnNameDisplayName, Types.VARCHAR);
        toDoTableColumns.put(columnNameCreationTime, Types.TIMESTAMP);
        toDoTableColumns.put(columnNameToDoType, Types.VARCHAR);
        toDoTableColumns.put(columnNamePriority, Types.INTEGER);
        toDoTableColumns.put(columnNameDueTime, Types.TIMESTAMP);
        toDoTableColumns.put(columnNameCompletionTime, Types.INTEGER);
        toDoTableColumns.put(columnNameStatus, Types.VARCHAR);
        toDoTableColumns.put(columnNameToDoSourceGUID, Types.VARCHAR);
        toDoTableColumns.put(columnNameToDoSourceType, Types.VARCHAR);
        toDoTableColumns.put(columnNameLastReviewedTime, Types.TIMESTAMP);
        toDoTableColumns.put(columnNameActorGUID, Types.VARCHAR);
        toDoTableColumns.put(columnNameActorType, Types.VARCHAR);
        toDoTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


    /**
     * Set up the definitions of the columns in the om_user_identity table.
     */
    private void fillUserIdentityTableColumns()
    {
        userIdentityTableColumns.put(columnNameEmployeeNumber, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameUserId, Types.VARCHAR);
        userIdentityTableColumns.put(columnNamePreferredName, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameOrgName, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameResidentCountry, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameLocation, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameDistinguishedName, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameUserIdGUID, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameProfileGUID, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameDepartmentGUID, Types.VARCHAR);
        userIdentityTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }



    /**
     * Set up the definitions of the columns in the om_user_identity table.
     */
    private void fillFileClassifiersTableColumns()
    {
        fileClassifiersTableColumns.put(columnNameSurveyReportGUID, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameFilename, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameFileExtension, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNamePathname, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameFileType, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameAssetType, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameDeployedImplementationType, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameEncoding, Types.VARCHAR);
        fileClassifiersTableColumns.put(columnNameSyncTime, Types.TIMESTAMP);
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            /*
             * Step through the catalogued metadata elements for each interesting type.  Start with data assets.
             */
            int startFrom = 0;

            List<DataAssetElement> dataAssetElements = dataAssetExchangeService.findDataAssets(".*",
                                                                                               startFrom,
                                                                                               myContext.getMaxPageSize(),
                                                                                               null);

            while (dataAssetElements != null)
            {
                for (DataAssetElement dataAssetElement : dataAssetElements)
                {
                    processDataAsset(dataAssetElement);
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                dataAssetElements = dataAssetExchangeService.findDataAssets(".*",
                                                                            startFrom,
                                                                            myContext.getMaxPageSize(),
                                                                            null);
            }

            startFrom = 0;
            List<GlossaryElement> glossaryElements = glossaryExchangeService.findGlossaries(".*",
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize(),
                                                                                            null);

            while (glossaryElements != null)
            {
                for (GlossaryElement glossaryElement : glossaryElements)
                {
                    processGlossary(glossaryElement);
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                glossaryElements = glossaryExchangeService.findGlossaries(".*",
                                                                          startFrom,
                                                                          myContext.getMaxPageSize(),
                                                                          null);
            }


            startFrom = 0;
            List<OpenMetadataElement> teamElements = openMetadataAccess.findMetadataElements(OpenMetadataType.TEAM_TYPE_NAME,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             myContext.getMaxPageSize());

            while (teamElements != null)
            {
                for (OpenMetadataElement teamElement : teamElements)
                {
                    processTeam(teamElement);
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                teamElements = openMetadataAccess.findMetadataElements("Team",
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       myContext.getMaxPageSize());
            }


            startFrom = 0;
            List<OpenMetadataElement> toDoElements = openMetadataAccess.findMetadataElements(OpenMetadataType.TO_DO_TYPE_NAME,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             myContext.getMaxPageSize());

            while (toDoElements != null)
            {
                for (OpenMetadataElement toDoElement : toDoElements)
                {
                    processToDo(toDoElement);
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                toDoElements = openMetadataAccess.findMetadataElements(OpenMetadataType.TO_DO_TYPE_NAME,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       myContext.getMaxPageSize());
            }


            startFrom = 0;
            List<OpenMetadataElement> roleElements = openMetadataAccess.findMetadataElements(OpenMetadataType.PERSON_ROLE_TYPE_NAME,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             myContext.getMaxPageSize());

            while (roleElements != null)
            {
                for (OpenMetadataElement roleElement : roleElements)
                {
                    syncRole(roleElement);
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                roleElements = openMetadataAccess.findMetadataElements(OpenMetadataType.PERSON_ROLE_TYPE_NAME,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       myContext.getMaxPageSize());
            }


            startFrom = 0;
            List<OpenMetadataElement> userIdentityElements = openMetadataAccess.findMetadataElements(OpenMetadataType.USER_IDENTITY_TYPE_NAME,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     startFrom,
                                                                                                     myContext.getMaxPageSize());

            while (userIdentityElements != null)
            {
                for (OpenMetadataElement userIdentityElement : userIdentityElements)
                {
                    processUserIdentity(userIdentityElement);
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                userIdentityElements = openMetadataAccess.findMetadataElements(OpenMetadataType.USER_IDENTITY_TYPE_NAME,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               startFrom,
                                                                               myContext.getMaxPageSize());
            }

            startFrom = 0;

            List<RelatedMetadataElements> personRoleAppointments = openMetadataAccess.findRelationshipsBetweenMetadataElements(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME,
                                                                                                                               null,
                                                                                                                               null,
                                                                                                                               null,
                                                                                                                               startFrom,
                                                                                                                               myContext.getMaxPageSize());

            while (personRoleAppointments != null)
            {
                for (RelatedMetadataElements personRoleAppointment : personRoleAppointments)
                {
                    if (personRoleAppointment != null)
                    {
                        syncRoleToUser(this.getUserIdentityForRole(personRoleAppointment.getElementGUIDAtEnd2(),
                                                                   personRoleAppointment.getElementGUIDAtEnd1()),
                                       personRoleAppointment);
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                personRoleAppointments = openMetadataAccess.findRelationshipsBetweenMetadataElements(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     startFrom,
                                                                                                     myContext.getMaxPageSize());
            }


            startFrom = 0;
            List<OpenMetadataElement> surveyReportElements = openMetadataAccess.findMetadataElements(OpenMetadataType.SURVEY_REPORT.typeName,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     startFrom,
                                                                                                     myContext.getMaxPageSize());

            while (surveyReportElements != null)
            {
                for (OpenMetadataElement userIdentityElement : surveyReportElements)
                {
                    processSurveyReport(userIdentityElement);
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                surveyReportElements = openMetadataAccess.findMetadataElements(OpenMetadataType.SURVEY_REPORT.typeName,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               startFrom,
                                                                               myContext.getMaxPageSize());
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(LoadObservationsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }

        try
        {
            /*
             * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
             * the processEvent() method is called.
             */
            myContext.registerListener(this);
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                                 error.getClass().getName(),
                                                                                                                 error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(LoadObservationsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.
     *
     * @param event event object
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        if (! myContext.isRefreshInProgress())
        {
            ElementHeader elementHeader = event.getElementHeader();

            if (myContext.isTypeOf(elementHeader, OpenMetadataType.ASSET.typeName))
            {
                processDataAsset(elementHeader);
            }

            if (myContext.isTypeOf(elementHeader, OpenMetadataType.GLOSSARY_TYPE_NAME))
            {
                processGlossary(elementHeader);
            }

            if (myContext.isTypeOf(elementHeader, OpenMetadataType.USER_IDENTITY_TYPE_NAME))
            {
                processUserIdentity(elementHeader);
            }

            if (myContext.isTypeOf(elementHeader, OpenMetadataType.TO_DO_TYPE_NAME))
            {
                processToDo(elementHeader);
            }

            if (myContext.isTypeOf(elementHeader, OpenMetadataType.SURVEY_REPORT.typeName))
            {
                processSurveyReport(elementHeader);
            }
        }
    }


    /**
     * Navigate to the user identity for this supplied role.
     *
     * @param roleGUID unique identifier for the role to match on
     * @param profileGUID unique identifier of the starting profile
     * @return unique identifier of the associated user identity element
     */
    private String getUserIdentityForRole(String roleGUID,
                                          String profileGUID)
    {
        final String methodName = "getUserIdentityForRole";

        String defaultUserIdentityForRole = null;
        String assignedUserIdentityForRole = null;

        try
        {
            List<RelatedMetadataElement> profileIdentities = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                           1,
                                                                                                           "ProfileIdentity",
                                                                                                           0,
                                                                                                           myContext.getMaxPageSize());


            if (profileIdentities != null)
            {
                for (RelatedMetadataElement profileIdentity : profileIdentities)
                {
                    if (profileIdentity != null)
                    {
                        String userIdentityRole = propertyHelper.getStringProperty(connectorName,
                                                                                   "roleGUID",
                                                                                   profileIdentity.getRelationshipProperties(),
                                                                                   methodName);
                        if (userIdentityRole == null)
                        {
                            defaultUserIdentityForRole = profileIdentity.getElement().getElementGUID();
                        }
                        else if (userIdentityRole.equals(roleGUID))
                        {
                            assignedUserIdentityForRole = profileIdentity.getElement().getElementGUID();
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        if (assignedUserIdentityForRole != null)
        {
            return assignedUserIdentityForRole;
        }
        else
        {
            return defaultUserIdentityForRole;
        }
    }


    /**
     * Process the incoming asset if it is still available.
     * 
     * @param elementHeader incoming element from event
     */
    private void processDataAsset(ElementHeader elementHeader)
    {
        try
        {
            processDataAsset(dataAssetExchangeService.getDataAssetByGUID(elementHeader.getGUID(), null));
        }
        catch (Exception error)
        {
            // Ignore error as the element has probably been deleted.
        }
    }


    /**
     * Process a data asset retrieved from the open metadata ecosystem.
     * 
     * @param dataAssetElement description of the asset
     */
    private void processDataAsset(DataAssetElement dataAssetElement)
    {
        final String methodName = "processDataAsset";

        try
        {
            /*
             * Retrieve elements associated directly with the asset.
             */
            String associatedLicenseGUID          = null;
            String associatedResourceLocationGUID = null;

            OpenMetadataElement associatedLocation = this.getAssociatedAssetLocation(dataAssetElement.getElementHeader().getGUID());
            OpenMetadataElement associatedLicense  = this.getAssociatedLicense(dataAssetElement.getElementHeader().getGUID());
            String tags                            = this.getAssociatedTags(dataAssetElement.getElementHeader().getGUID());
            String glossaryTermGUID                = this.getAssociatedMeaning(dataAssetElement.getElementHeader().getGUID());

            syncAssetType(dataAssetElement.getElementHeader().getType());

            if (associatedLocation != null)
            {
                associatedResourceLocationGUID = associatedLocation.getElementGUID();
                syncLocation(associatedLocation);
            }

            if (associatedLicense != null)
            {
                associatedLicenseGUID = associatedLicense.getElementGUID();
            }

            /*
             * Extract interesting information from the data asset.
             */
            syncDataAsset(dataAssetElement,
                          associatedResourceLocationGUID,
                          associatedLicenseGUID,
                          tags,
                          glossaryTermGUID);


            /*
             * Find out about other associated elements
             */
            findAssociatedElements(dataAssetElement.getElementHeader(),
                                   dataAssetElement.getDataAssetProperties().getQualifiedName(),
                                   dataAssetElement.getCorrelationHeaders(),
                                   associatedLicense,
                                   true);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Retrieve details of a location that is connected to the asset via the AssetLocation relationship.
     *
     * @param assetGUID unique identifier of the asset
     * @return the location (or null if there is not one)
     */
    private OpenMetadataElement getAssociatedAssetLocation(String assetGUID)
    {
        final String methodName = "getAssociatedAssetLocation";

        try
        {
            List<RelatedMetadataElement> relatedElements = openMetadataAccess.getRelatedMetadataElements(assetGUID,
                                                                                                         2,
                                                                                                         "AssetLocation",
                                                                                                         0,
                                                                                                         myContext.getMaxPageSize());

            /*
             * Return the first location retrieved.
             */
            if (relatedElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        // todo it is possible to enhance this logic to give precedence to a location that has the FixedLocation classification.
                        this.syncLocation(relatedMetadataElement.getElement());
                        return relatedMetadataElement.getElement();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Retrieve details of a location that is connected to the profile via the ProfileLocation relationship.
     *
     * @param profileGUID unique identifier of the profile
     * @return unique identifier of the location (or null if there is not one)
     */
    private String getAssociatedProfileLocation(String profileGUID)
    {
        final String methodName = "getAssociatedProfileLocation";

        try
        {
            List<RelatedMetadataElement> relatedElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                         1,
                                                                                                         "ProfileLocation",
                                                                                                         0,
                                                                                                         myContext.getMaxPageSize());

            /*
             * Return the first location retrieved.
             */
            if (relatedElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        // todo it is possible to enhance this logic to give precedence to a location that has the FixedLocation classification.
                        this.syncLocation(relatedMetadataElement.getElement());
                        return relatedMetadataElement.getElement().getElementGUID();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Retrieve details of a license that is connected to the element via the License relationship.
     *
     * @param elementGUID unique identifier of the asset
     * @return  the location (or null if there is not one)
     */
    private OpenMetadataElement getAssociatedLicense(String elementGUID)
    {
        final String methodName = "getAssociatedLicense";

        try
        {
            List<RelatedMetadataElement> relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                                         1,
                                                                                                         "License",
                                                                                                         0,
                                                                                                         myContext.getMaxPageSize());

            /*
             * Return the first location retrieved.
             */
            if (relatedElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        // todo it is possible to enhance this logic to give precedence to a license that is currently active.
                        this.syncLicense(relatedMetadataElement.getElement());
                        return relatedMetadataElement.getElement();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Retrieve details of a license that is connected to the asset via the License relationship.
     *
     * @param assetGUID unique identifier of the asset
     * @return unique identifier of the location (or null if there is not one)
     */
    private String getAssociatedTags(String assetGUID)
    {
        final String methodName = "getAssociatedTags";

        StringBuilder tagStringBuilder = new StringBuilder(":");

        try
        {
            int startFrom = 0;
            List<RelatedMetadataElement> relatedElements = openMetadataAccess.getRelatedMetadataElements(assetGUID,
                                                                                                         1,
                                                                                                         "AttachedTag",
                                                                                                         startFrom,
                                                                                                         myContext.getMaxPageSize());

            /*
             * Return all attached tags.
             */
            while (relatedElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        String tagName = propertyHelper.getStringProperty(connectorName,
                                                                          "tagName",
                                                                          relatedMetadataElement.getElement().getElementProperties(),
                                                                          methodName);
                        tagStringBuilder.append(tagName).append(":");
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                relatedElements = openMetadataAccess.getRelatedMetadataElements(assetGUID,
                                                                                1,
                                                                                "AttachedTag",
                                                                                startFrom,
                                                                                myContext.getMaxPageSize());

            }

            String tagString = tagStringBuilder.toString();

            if (tagString.length() == 1)
            {
                return "::";
            }
            else
            {
                return tagString;
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Retrieve details of a glossary term that is connected to the element via the SemanticAssignment relationship.
     *
     * @param elementGUID unique identifier of the element
     * @return unique identifier of the location (or null if there is not one)
     */
    private String getAssociatedMeaning(String elementGUID)
    {
        final String methodName = "getAssociatedMeaning";

        try
        {
            List<RelatedMetadataElement> relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                                         1,
                                                                                                         "SemanticAssignment",
                                                                                                         0,
                                                                                                         myContext.getMaxPageSize());

            /*
             * Return the first glossary term retrieved.
             */
            if (relatedElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        return relatedMetadataElement.getElement().getElementGUID();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Look for related elements that are populated in side tables.
     *
     * @param elementHeader unique identifier of the element plus other information from the
     * @param elementQualifiedName unique name of the element
     * @param correlationHeaders information about external collections
     * @param license associated license
     * @param isAsset is this element an asset
     */
    private void findAssociatedElements(ElementHeader                   elementHeader,
                                        String                          elementQualifiedName,
                                        List<MetadataCorrelationHeader> correlationHeaders,
                                        OpenMetadataElement             license,
                                        boolean                         isAsset)
    {
        final String methodName = "findAssociatedElements";

        try
        {
            processMetadataCollection(elementHeader.getOrigin());
            syncCorrelationProperties(elementHeader, correlationHeaders);
            processUserIds(elementHeader.getVersions(), elementHeader.getOrigin());

            if (license != null)
            {
                syncLicense(license);
            }

            int numberOfComments = 0;
            int numberOfRatings = 0;
            int totalStars = 0;
            int numberOfTags = 0;
            int numberOfLikes = 0;

            int startFrom = 0;
            List<RelatedMetadataElement> relatedElements = openMetadataAccess.getRelatedMetadataElements(elementHeader.getGUID(),
                                                                                                         1,
                                                                                                         null,
                                                                                                         startFrom,
                                                                                                         myContext.getMaxPageSize());

            while (relatedElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements)
                {
                    String relationshipType = relatedMetadataElement.getType().getTypeName();

                    if ("AttachedTag".equals(relationshipType))
                    {
                        numberOfTags++;
                    }
                    else if ("AttachedLike".equals(relationshipType))
                    {
                        numberOfLikes++;
                    }
                    else if ("AttachedRating".equals(relationshipType))
                    {
                        numberOfRatings++;
                        totalStars = totalStars + countStars(relatedMetadataElement.getElement().getElementProperties().getPropertyValue("stars"));
                    }
                    else if ("AttachedComment".equals(relationshipType))
                    {
                        numberOfComments = numberOfComments + 1 + countAttachedComments(relatedMetadataElement.getElement().getElementGUID());
                    }
                    else if ("Certification".equals(relationshipType))
                    {
                        syncCertification(elementHeader.getGUID(), relatedMetadataElement);
                    }
                    else if ("AssetSchemaType".equals(relationshipType))
                    {
                        processAssetSchemaType(elementHeader.getGUID(), elementQualifiedName, relatedMetadataElement);
                    }

                    if ((isAsset) && myContext.isTypeOf(relatedMetadataElement.getElement().getType(), "Asset"))
                    {
                        syncRelatedAsset(openMetadataAccess.getRelationshipByGUID(relatedMetadataElement.getRelationshipGUID()));
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                relatedElements = openMetadataAccess.getRelatedMetadataElements(elementHeader.getGUID(),
                                                                                1,
                                                                                null,
                                                                                startFrom,
                                                                                myContext.getMaxPageSize());
            }

            int averageRatings = 0;

            if ((numberOfRatings > 0) && (totalStars > 0))
            {
                averageRatings = totalStars / numberOfRatings;
            }

            syncCollaborationActivity(elementHeader.getGUID(), numberOfComments, numberOfRatings, averageRatings, numberOfTags, numberOfLikes);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Count the number of stars indicated by the star rating.
     *
     * @param starRating star rating property from the Rating entity
     * @return star count as an integer
     */
    private int countStars(PropertyValue starRating)
    {
        if (starRating instanceof EnumTypePropertyValue enumTypePropertyValue)
        {
            switch (enumTypePropertyValue.getSymbolicName())
            {
                case "NotRecommended" -> { return 0; }
                case "OneStar"        -> { return 1; }
                case "TwoStar"        -> { return 2; }
                case "ThreeStar"      -> { return 3; }
                case "FourStar"       -> { return 4; }
                case "FiveStar"       -> { return 5; }
            }
        }

        return 0;
    }


    /**
     * Count the nested comments.
     *
     * @param elementGUID starting comment
     * @return count of nested comments
     */
    private int countAttachedComments(String elementGUID)
    {
        final String methodName = "countAttachedComments";

        int commentCount = 0;
        try
        {
            int startFrom = 0;
            List<RelatedMetadataElement> relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                                         1,
                                                                                                         "AttachedComment",
                                                                                                         startFrom,
                                                                                                         myContext.getMaxPageSize());

            /*
             * Count all nested comments.
             */
            while (relatedElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        commentCount = commentCount + 1 + this.countAttachedComments(relatedMetadataElement.getElement().getElementGUID());
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                1,
                                                                                "AttachedComment",
                                                                                startFrom,
                                                                                myContext.getMaxPageSize());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return commentCount;
    }


    /**
     * Capture details of an elements metadata collection.
     *
     * @param elementOrigin origin from an element's header
     */
    private void processMetadataCollection(ElementOrigin elementOrigin)
    {
        final  String methodName = "processMetadataCollection";

        if (elementOrigin != null)
        {
            String deployedImplementationType = null;

            if (elementOrigin.getOriginCategory() == ElementOriginCategory.EXTERNAL_SOURCE)
            {
                try
                {
                    OpenMetadataElement softwareCapability = openMetadataAccess.getMetadataElementByGUID(elementOrigin.getHomeMetadataCollectionId());

                    if (softwareCapability != null)
                    {
                        deployedImplementationType = propertyHelper.getStringProperty(connectorName,
                                                                                      "deployedImplementationType",
                                                                                      softwareCapability.getElementProperties(),
                                                                                      methodName);
                    }
                }
                catch (Exception error)
                {
                    // ignore error
                }
            }

            syncMetadataCollection(elementOrigin, deployedImplementationType);
        }
    }



    /**
     * Extract information about a catalogued team or organization.
     *
     * @param teamElement retrieved element
     */
    private void processTeam(OpenMetadataElement teamElement)
    {
        if (teamElement != null)
        {
            String parentDepartmentGUID = null;
            String managerGUID          = getTeamManagerGUID(teamElement.getElementGUID());;

            OpenMetadataElement parentDepartment = this.getAssociatedParentTeam(teamElement.getElementGUID());

            if (parentDepartment != null)
            {
                parentDepartmentGUID = parentDepartment.getElementGUID();
            }


            syncDepartment(parentDepartmentGUID, managerGUID, teamElement);
        }
    }


    /**
     * Return the unique identifier of the userIdentity of the team's manager.
     *
     * @param teamGUID unique identifier of the team
     * @return unique identifier of the userIdentity of the team's manager
     */
    private String getTeamManagerGUID(String teamGUID)
    {
        final String methodName = "getTeamManagerGUID";

        try
        {
            List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(teamGUID,
                                                                                                                 2,
                                                                                                                 "TeamLeadership",
                                                                                                                 0,
                                                                                                                 myContext.getMaxPageSize());

            if (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement personRoleElement : relatedMetadataElements)
                {
                    if (personRoleElement != null)
                    {
                        List<RelatedMetadataElement> appointees = openMetadataAccess.getRelatedMetadataElements(teamGUID,
                                                                                                                  2,
                                                                                                                  "PersonRoleAppointment",
                                                                                                                  0,
                                                                                                                  myContext.getMaxPageSize());
                        if (appointees != null)
                        {
                            for (RelatedMetadataElement appointee : appointees)
                            {
                                if (appointee != null)
                                {
                                    return this.getUserIdentityForRole(personRoleElement.getElement().getElementGUID(),
                                                                       appointee.getElement().getElementGUID());
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Process a userIdentity.
     *
     * @param elementHeader user Identity header
     */
    private void processUserIdentity(ElementHeader elementHeader)
    {
        final String methodName = "processUserIdentity";

        try
        {
            OpenMetadataElement userIdentity = openMetadataAccess.getMetadataElementByGUID(elementHeader.getGUID());

            if (userIdentity != null)
            {
                processUserIdentity(userIdentity);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued user identity.
     *
     * @param userIdentityElement retrieved element
     */
    private void processUserIdentity(OpenMetadataElement userIdentityElement)
    {
        final String methodName = "processUserIdentity";

        try
        {
            String departmentId = null;
            String locationGUID = null;
            String organizationName = null;
            RelatedMetadataElement profileElement = null;

            List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(userIdentityElement.getElementGUID(),
                                                                                                                 2,
                                                                                                                 "ProfileIdentity",
                                                                                                                 0,
                                                                                                                 myContext.getMaxPageSize());

            if (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        profileElement = relatedMetadataElement;
                        break;
                    }
                }
            }

            if (profileElement != null)
            {
                locationGUID = getAssociatedProfileLocation(profileElement.getElement().getElementGUID());
                departmentId = getAssociatedDepartment(profileElement.getElement().getElementGUID());

                if (departmentId != null)
                {
                    organizationName = getAssociatedOrganizationName(departmentId);
                }
            }

            syncUserIdentity(userIdentityElement,
                             profileElement,
                             departmentId,
                             locationGUID,
                             organizationName);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued to do.
     *
     * @param elementHeader retrieved element
     */
    private void processToDo(ElementHeader elementHeader)
    {
        final String methodName = "processToDo";

        try
        {
            OpenMetadataElement toDoElement = openMetadataAccess.getMetadataElementByGUID(elementHeader.getGUID());

            if (toDoElement != null)
            {
                processToDo(toDoElement);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued to do.
     *
     * @param toDoElement retrieved element
     */
    private void processToDo(OpenMetadataElement toDoElement)
    {
        final String methodName = "processToDo";

        try
        {
            RelatedMetadataElement toDoSourceElement = null;
            RelatedMetadataElement actionAssignmentElement = null;

            List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(toDoElement.getElementGUID(),
                                                                                                                 2,
                                                                                                                 OpenMetadataType.TO_DO_SOURCE_RELATIONSHIP_TYPE_NAME,
                                                                                                                 0,
                                                                                                                 myContext.getMaxPageSize());

            if (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        toDoSourceElement = relatedMetadataElement;
                        break;
                    }
                }
            }

            relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(toDoElement.getElementGUID(),
                                                                                    2,
                                                                                    OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                                    0,
                                                                                    myContext.getMaxPageSize());

            if (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        actionAssignmentElement = relatedMetadataElement;
                        break;
                    }
                }
            }

            syncToDo(toDoElement, toDoSourceElement, actionAssignmentElement);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued survey report.
     *
     * @param elementHeader retrieved element
     */
    private void processSurveyReport(ElementHeader elementHeader)
    {
        final String methodName = "processSurveyReport";

        try
        {
            OpenMetadataElement surveyReportElement = openMetadataAccess.getMetadataElementByGUID(elementHeader.getGUID());

            if (surveyReportElement != null)
            {
                processSurveyReport(surveyReportElement);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued survey report.
     *
     * @param surveyReportElement retrieved element
     */
    private void processSurveyReport(OpenMetadataElement surveyReportElement)
    {
        final String methodName = "processSurveyReport";

        try
        {
            RelatedMetadataElement       relatedAsset                             = null;
            RelatedMetadataElement       relatedEngineAction                      = null;
            List<RelatedMetadataElement> relatedDataProfileAnnotations            = new ArrayList<>();
            List<RelatedMetadataElement> relatedDataProfileLogAnnotations         = new ArrayList<>();
            List<RelatedMetadataElement> relatedDataSourceMeasurementsAnnotations = new ArrayList<>();
            List<RelatedMetadataElement> relatedRequestForActionAnnotations       = new ArrayList<>();

            int startFrom = 0;
            List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                                                 0,
                                                                                                                 null,
                                                                                                                 startFrom,
                                                                                                                 myContext.getMaxPageSize());

            while (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeName))
                        {
                            relatedEngineAction = relatedMetadataElement;
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName))
                        {
                            relatedAsset = relatedMetadataElement;
                        }
                        else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName))
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
                            {
                                relatedRequestForActionAnnotations.add(relatedMetadataElement);
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.DATA_PROFILE_ANNOTATION.typeName))
                            {
                                relatedDataProfileAnnotations.add(relatedMetadataElement);
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.DATA_PROFILE_LOG_ANNOTATION.typeName))
                            {
                                relatedDataProfileLogAnnotations.add(relatedMetadataElement);
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.DATA_SOURCE_MEASUREMENT_ANNOTATION.typeName))
                            {
                                relatedDataSourceMeasurementsAnnotations.add(relatedMetadataElement);
                            }
                        }
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();

                relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(surveyReportElement.getElementGUID(),
                                                                                        0,
                                                                                        null,
                                                                                        startFrom,
                                                                                        myContext.getMaxPageSize());
            }

            syncSurveyReport(surveyReportElement,
                             relatedAsset,
                             relatedEngineAction);

            processRequestForActionAnnotations(surveyReportElement.getElementGUID(),
                                               relatedAsset,
                                               relatedRequestForActionAnnotations);

            processDataProfileAnnotations(surveyReportElement.getElementGUID(),
                                          relatedAsset,
                                          relatedDataProfileAnnotations);

            processDataProfileLogAnnotations(surveyReportElement.getElementGUID(),
                                             relatedAsset,
                                             relatedDataProfileLogAnnotations);

            processDataSourceMeasurementsAnnotations(surveyReportElement.getElementGUID(),
                                                     relatedAsset,
                                                     relatedDataSourceMeasurementsAnnotations);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Add details of request for action annotations to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedRequestForActionAnnotations list of request for action annotations
     */
    private void processRequestForActionAnnotations(String                       surveyReportGUID,
                                                    RelatedMetadataElement       relatedAsset,
                                                    List<RelatedMetadataElement> relatedRequestForActionAnnotations)
    {
        final String methodName = "processRequestForActionAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedRequestForActionAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedActionTarget = null;
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     myContext.getMaxPageSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName))
                            {
                                relatedActionTarget = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + myContext.getMaxPageSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                syncRequestForAction(surveyReportGUID,
                                     relatedAnnotationElement.getElement(),
                                     relatedActionTarget,
                                     relatedAnnotationSubject);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Add details of data profile annotations to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedDataProfileAnnotations list of request for action annotations
     */
    private void processDataProfileAnnotations(String                       surveyReportGUID,
                                               RelatedMetadataElement       relatedAsset,
                                               List<RelatedMetadataElement> relatedDataProfileAnnotations)
    {
        final String methodName = "processDataProfileAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedDataProfileAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     myContext.getMaxPageSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + myContext.getMaxPageSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                syncDataProfile(surveyReportGUID,
                                relatedAnnotationElement.getElement(),
                                relatedAnnotationSubject);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Add details of data profile log annotations to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedDataProfileLogAnnotations list of request for action annotations
     */
    private void processDataProfileLogAnnotations(String                       surveyReportGUID,
                                                  RelatedMetadataElement       relatedAsset,
                                                  List<RelatedMetadataElement> relatedDataProfileLogAnnotations)
    {
        final String methodName = "processDataProfileLogAnnotations";


        for (RelatedMetadataElement relatedAnnotationElement : relatedDataProfileLogAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedLogFile = null;
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     myContext.getMaxPageSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                            else if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.DATA_PROFILE_DATA_RELATIONSHIP.typeName))
                            {
                                relatedLogFile = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + myContext.getMaxPageSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                if (relatedLogFile != null)
                {
                    Connector connector = myContext.getConnectorFactoryService().getConnectorForAsset(relatedLogFile.getElement().getElementGUID());

                    if (connector instanceof CSVFileStoreConnector csvFileStoreConnector)
                    {
                        Map<String, String> measurementValues = new HashMap<>();

                        for (int recordNumber = 0; recordNumber < csvFileStoreConnector.getRecordCount(); recordNumber ++)
                        {
                            List<String> recordValues = csvFileStoreConnector.readRecord(recordNumber);

                            if ((recordValues != null) && (recordValues.size() > 1))
                            {
                                measurementValues.put(recordValues.get(0), recordValues.get(1));
                            }
                        }

                        syncDataProfile(surveyReportGUID,
                                        relatedAnnotationElement.getElement(),
                                        relatedAnnotationSubject,
                                        measurementValues);
                    }
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Add details of data source measurements annotations to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param relatedAsset details of the asset for the resource that was surveyed
     * @param relatedDataSourceMeasurementsAnnotations list of request for action annotations
     */
    private void processDataSourceMeasurementsAnnotations(String                       surveyReportGUID,
                                                          RelatedMetadataElement       relatedAsset,
                                                          List<RelatedMetadataElement> relatedDataSourceMeasurementsAnnotations)
    {
        final String methodName = "processDataSourceMeasurementsAnnotations";

        for (RelatedMetadataElement relatedAnnotationElement : relatedDataSourceMeasurementsAnnotations)
        {
            try
            {
                RelatedMetadataElement relatedAnnotationSubject   = null;
                int startFrom = 0;
                List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                                                     0,
                                                                                                                     null,
                                                                                                                     startFrom,
                                                                                                                     myContext.getMaxPageSize());
                while (relatedMetadataElements != null)
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                    {
                        if (relatedMetadataElement != null)
                        {
                            if (propertyHelper.isTypeOf(relatedMetadataElement, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                            {
                                relatedAnnotationSubject = relatedMetadataElement;
                            }
                        }
                    }

                    startFrom = startFrom + myContext.getMaxPageSize();

                    relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(relatedAnnotationElement.getElement().getElementGUID(),
                                                                                            0,
                                                                                            null,
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize());
                }

                if (relatedAnnotationSubject == null)
                {
                    relatedAnnotationSubject = relatedAsset;
                }

                syncDataSourceMeasurements(surveyReportGUID,
                                           relatedAnnotationElement.getElement(),
                                           relatedAnnotationSubject);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Navigate to the supplied person's department.
     * @param profileGUID unique identifier of the profile
     * @return unique identifier of the department
     */
    private String getAssociatedDepartment(String profileGUID)
    {
        final String methodName = "getAssociatedDepartment";

        try
        {
            List<RelatedMetadataElement> roleElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                      1,
                                                                                                      "PersonRoleAppointment",
                                                                                                      0,
                                                                                                      myContext.getMaxPageSize());

            if (roleElements != null)
            {
                for (RelatedMetadataElement roleElement : roleElements)
                {
                    if (roleElement != null)
                    {
                        List<RelatedMetadataElement> teamElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                                  1,
                                                                                                                  "TeamMembership",
                                                                                                                  0,
                                                                                                                  myContext.getMaxPageSize());

                        if (teamElements == null)
                        {
                            teamElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                         1,
                                                                                         "TeamLeadership",
                                                                                         0,
                                                                                         myContext.getMaxPageSize());
                        }

                        if (teamElements != null)
                        {
                            for (RelatedMetadataElement team : teamElements)
                            {
                                if (team != null)
                                {
                                    return team.getElement().getElementGUID();
                                }
                            }
                        }
                    }
                }
            }

        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Return the team that this department reports to.
     *
     * @param departmentGUID unique identifier of starting team
     * @return super team
     */
    private OpenMetadataElement getAssociatedParentTeam(String departmentGUID)
    {
        final String methodName = "getAssociatedParentTeam";

        try
        {
            List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(departmentGUID,
                                                                                                                 2,
                                                                                                                 "TeamStructure",
                                                                                                                 0,
                                                                                                                 myContext.getMaxPageSize());

            if (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                {
                    if (relatedMetadataElement != null)
                    {
                        return relatedMetadataElement.getElement();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Return the name of the top-level organization.
     *
     * @param departmentGUID unique identifier of the starting department.
     * @return name of top-level team
     */
    private String getAssociatedOrganizationName(String departmentGUID)
    {
        final String methodName = "getAssociatedOrganizationName";

        String organizationName = null;
        OpenMetadataElement superTeam = this.getAssociatedParentTeam(departmentGUID);

        while (superTeam != null)
        {
            organizationName = propertyHelper.getStringProperty(connectorName,
                                                                "name",
                                                                superTeam.getElementProperties(),
                                                                methodName);
            superTeam = this.getAssociatedParentTeam(superTeam.getElementGUID());
        }

        return organizationName;
    }


    /**
     * Navigate to the schema attributes to find the data fields.
     *
     * @param assetGUID unique identifier for the anchoring asset
     * @param assetQualifiedName unique identifier for the anchoring asset
     * @param schemaType details of the related schema type
     */
    private void processAssetSchemaType(String                 assetGUID,
                                        String                 assetQualifiedName,
                                        RelatedMetadataElement schemaType)
    {
        final String methodName = "processAssetSchemaType";

        try
        {
            int startFrom = 0;

            /*
             * Start by processing related schema types.
             */
            List<RelatedMetadataElement> relatedMetadataElementList = openMetadataAccess.getRelatedMetadataElements(schemaType.getElement().getElementGUID(),
                                                                                                                    1,
                                                                                                                    null,
                                                                                                                    startFrom,
                                                                                                                    myContext.getMaxPageSize());
            while (relatedMetadataElementList != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList)
                {
                    if (myContext.isTypeOf(relatedMetadataElement.getElement().getType(), "SchemaType"))
                    {
                        processAssetSchemaType(assetGUID, assetQualifiedName, relatedMetadataElement);
                    }
                }


                startFrom = startFrom + myContext.getMaxPageSize();
                relatedMetadataElementList = openMetadataAccess.getRelatedMetadataElements(schemaType.getElement().getElementGUID(),
                                                                                           1,
                                                                                           null,
                                                                                           startFrom,
                                                                                           myContext.getMaxPageSize());
            }

            /*
             * Now process schema attributes.
             */
            startFrom = 0;
            List<SchemaAttributeElement> schemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaType.getElement().getElementGUID(),
                                                                                                         startFrom,
                                                                                                         myContext.getMaxPageSize(),
                                                                                                         null);

            while (schemaAttributes != null)
            {
                processSchemaAttributes(assetGUID, assetQualifiedName, schemaAttributes);

                startFrom = startFrom + myContext.getMaxPageSize();
                schemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaType.getElement().getElementGUID(),
                                                                                startFrom,
                                                                                myContext.getMaxPageSize(),
                                                                                null);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Navigate to the leaf nodes to find the data fields.
     *
     * @param assetGUID unique identifier for the anchoring asset
     * @param assetQualifiedName unique identifier for the anchoring asset
     * @param schemaType details of the related schema type
     */
    private void processNestedSchemaAttribute(String                 assetGUID,
                                              String                 assetQualifiedName,
                                              RelatedMetadataElement schemaType)
    {
        final String methodName = "processAssetSchemaType";

        // todo need to extend to support APIs and SchemaTypeChoices etc
        try
        {
            int startFrom = 0;
            List<SchemaAttributeElement> schemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaType.getElement().getElementGUID(),
                                                                                                         startFrom,
                                                                                                         myContext.getMaxPageSize(),
                                                                                                         null);

            while (schemaAttributes != null)
            {
                processSchemaAttributes(assetGUID, assetQualifiedName, schemaAttributes);

                startFrom = startFrom + myContext.getMaxPageSize();
                schemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaType.getElement().getElementGUID(),
                                                                                startFrom,
                                                                                myContext.getMaxPageSize(),
                                                                                null);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Process a list of schema attributes associated with an asset.  It is seeking the leaf nodes of the schema.
     *
     * @param assetGUID unique identifier for the anchoring asset
     * @param assetQualifiedName unique identifier for the anchoring asset
     * @param schemaAttributes retrieved schema attributes
     */
    private void processSchemaAttributes(String                       assetGUID,
                                         String                       assetQualifiedName,
                                         List<SchemaAttributeElement> schemaAttributes)
    {
        final String methodName = "processSchemaAttributes";

        try
        {
            for (SchemaAttributeElement schemaAttribute : schemaAttributes)
            {
                int nestedStartFrom = 0;
                List<SchemaAttributeElement> nestedSchemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaAttribute.getElementHeader().getGUID(),
                                                                                                                   nestedStartFrom,
                                                                                                                   myContext.getMaxPageSize(),
                                                                                                                   null);
                if (nestedSchemaAttributes == null)
                {
                    /*
                     * Only write details of the leaf nodes
                     */
                    syncDataField(assetGUID,
                                  assetQualifiedName,
                                  getAssociatedMeaning(schemaAttribute.getElementHeader().getGUID()),
                                  hasProfile(schemaAttribute.getElementHeader().getGUID()),
                                  schemaAttribute);

                    findAssociatedElements(schemaAttribute.getElementHeader(),
                                           schemaAttribute.getSchemaAttributeProperties().getQualifiedName(),
                                           schemaAttribute.getCorrelationHeaders(),
                                           null,
                                           false);
                }
                else
                {
                    while (nestedSchemaAttributes != null)
                    {
                        nestedStartFrom = nestedStartFrom + myContext.getMaxPageSize();
                        nestedSchemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaAttribute.getElementHeader().getGUID(),
                                                                                              nestedStartFrom,
                                                                                              myContext.getMaxPageSize(),
                                                                                              null);

                        if (nestedSchemaAttributes != null)
                        {
                            processSchemaAttributes(assetGUID, assetQualifiedName, nestedSchemaAttributes);
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Detects if there is an associated annotation attached to the asset via the AssociatedAnnotation relationship.
     *
     * @param dataFieldGUID unique identifier of the data field
     * @return boolean flag
     */
    private boolean hasProfile(String dataFieldGUID)
    {
        final String methodName = "hasProfile";

        try
        {
            List<RelatedMetadataElement> reports = openMetadataAccess.getRelatedMetadataElements(dataFieldGUID,
                                                                                                 1,
                                                                                                 OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                 0,
                                                                                                 1);

            return (reports != null);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return false;
    }


    /**
     * Process a glossary received in an event.
     *
     * @param glossaryHeader glossary header
     */
    private void processGlossary(ElementHeader glossaryHeader)
    {
        final String methodName = "processGlossary";

        try
        {
            GlossaryElement glossary = glossaryExchangeService.getGlossaryByGUID(glossaryHeader.getGUID(), null);

            if (glossary != null)
            {
                processGlossary(glossary);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Add information to the glossary table.
     *
     * @param glossary glossary information
     */
    private void processGlossary(GlossaryElement glossary)
    {
        final String methodName = "processGlossary";

        try
        {
            OpenMetadataElement associatedLicense = getAssociatedLicense(glossary.getElementHeader().getGUID());

            int numberOfTerms = 0;
            int numberOfCategories = 0;
            int numberOfLinkedTerms = 0;

            int startFrom = 0;

            List<GlossaryCategoryElement> glossaryCategoryElements = glossaryExchangeService.getCategoriesForGlossary(glossary.getElementHeader().getGUID(),
                                                                                                                      startFrom,
                                                                                                                      myContext.getMaxPageSize(),
                                                                                                                      null);

            while (glossaryCategoryElements != null)
            {
                for (GlossaryCategoryElement glossaryCategoryElement : glossaryCategoryElements)
                {
                    if (glossaryCategoryElement != null)
                    {
                        numberOfCategories++;

                        int catTermsStartFrom = 0;

                        List<GlossaryTermElement> categorizedTerms = glossaryExchangeService.getTermsForGlossaryCategory(glossaryCategoryElement.getElementHeader().getGUID(),
                                                                                                                         null,
                                                                                                                         catTermsStartFrom,
                                                                                                                         myContext.getMaxPageSize(),
                                                                                                                         null);

                        while (categorizedTerms != null)
                        {
                            for (GlossaryTermElement glossaryTermElement : categorizedTerms)
                            {
                                if (glossaryTermElement != null)
                                {
                                    numberOfLinkedTerms++;
                                }
                            }

                            catTermsStartFrom = catTermsStartFrom + myContext.getMaxPageSize();

                            categorizedTerms = glossaryExchangeService.getTermsForGlossaryCategory(glossaryCategoryElement.getElementHeader().getGUID(),
                                                                                                   null,
                                                                                                   catTermsStartFrom,
                                                                                                   myContext.getMaxPageSize(),
                                                                                                   null);
                        }
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                glossaryCategoryElements = glossaryExchangeService.getCategoriesForGlossary(glossary.getElementHeader().getGUID(),
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize(),
                                                                                            null);
            }

            startFrom = 0;

            List<GlossaryTermElement> glossaryTermElements = glossaryExchangeService.getTermsForGlossary(glossary.getElementHeader().getGUID(),
                                                                                                         startFrom,
                                                                                                         myContext.getMaxPageSize(),
                                                                                                         null);

            while (glossaryTermElements != null)
            {
                for (GlossaryTermElement glossaryTermElement : glossaryTermElements)
                {
                    if (glossaryTermElement != null)
                    {
                        numberOfTerms++;

                        processGlossaryTerm(glossary.getElementHeader().getGUID(), glossaryTermElement);
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                glossaryTermElements = glossaryExchangeService.getTermsForGlossary(glossary.getElementHeader().getGUID(),
                                                                                   startFrom,
                                                                                   myContext.getMaxPageSize(),
                                                                                   null);
            }

            syncGlossary(glossary, numberOfTerms, numberOfCategories, numberOfLinkedTerms, associatedLicense);

            findAssociatedElements(glossary.getElementHeader(),
                                   glossary.getGlossaryProperties().getQualifiedName(),
                                   glossary.getCorrelationHeaders(),
                                   associatedLicense,
                                   false);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Update the term_activity table with details of a new term.
     *
     * @param glossaryGUID unique identifier of the owning glossary
     * @param glossaryTerm term to add to the table
     */
    private void processGlossaryTerm(String              glossaryGUID,
                                     GlossaryTermElement glossaryTerm)
    {
        final String methodName = "processGlossaryTerm";

        Date lastFeedbackTime       = null;
        int  numberOfLinkedElements = 0;
        Date lastLinkTime           = null;

        try
        {
            int startFrom = 0;
            List<RelatedMetadataElement> relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(glossaryTerm.getElementHeader().getGUID(),
                                                                                                                 0,
                                                                                                                 null,
                                                                                                                 startFrom,
                                                                                                                 myContext.getMaxPageSize());

            while (relatedMetadataElements != null)
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements)
                {
                    String relationshipName = relatedMetadataElement.getType().getTypeName();

                    switch (relationshipName)
                    {
                        case "AttachedTag", "AttachedComment", "AttachedRating", "AttachedLink" ->
                        {
                            if ((lastFeedbackTime == null) || (relatedMetadataElement.getVersions().getCreateTime().after(lastFeedbackTime)))
                            {
                                lastFeedbackTime = relatedMetadataElement.getVersions().getCreateTime();
                            }
                        }
                        case "SemanticAssignment" ->
                        {
                            numberOfLinkedElements = numberOfLinkedElements + 1;
                            if ((lastLinkTime == null) || (relatedMetadataElement.getVersions().getCreateTime().after(lastLinkTime)))
                            {
                                lastLinkTime = relatedMetadataElement.getVersions().getCreateTime();
                            }
                        }
                    }
                }
                startFrom = startFrom + myContext.getMaxPageSize();
                relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(glossaryTerm.getElementHeader().getGUID(),
                                                                                        0,
                                                                                        null,
                                                                                        startFrom,
                                                                                        myContext.getMaxPageSize());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        syncTermActivity(glossaryTerm, glossaryGUID, lastFeedbackTime, numberOfLinkedElements, lastLinkTime);
    }


    /**
     * Process a data asset retrieved from the open metadata ecosystem.
     *
     * @param dataAssetElement description of the asset
     * @param associatedResourceLocationGUID unique identifier of the location where the associated resource is located
     * @param associatedLicenceGUID unique identifier of the license for this asset/resource
     * @param tags colon separated list of tags
     * @param semanticAssignmentTermGUID unique identifier of term that is linked to element with a semantic assignment
     */
    private void syncDataAsset(DataAssetElement dataAssetElement,
                               String           associatedResourceLocationGUID,
                               String           associatedLicenceGUID,
                               String           tags,
                               String           semanticAssignmentTermGUID)
    {
        final String methodName = "syncDataAsset";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        assetDatabaseTable,
                                                                                        columnNameAssetGUID,
                                                                                        dataAssetElement.getElementHeader().getGUID(),
                                                                                        columnNameSyncTime,
                                                                                        assetTableColumns);

            Map<String, JDBCDataValue> openMetadataRecord = this.getAssetDataValues(dataAssetElement,
                                                                                    associatedResourceLocationGUID,
                                                                                    associatedLicenceGUID,
                                                                                    tags,
                                                                                    semanticAssignmentTermGUID);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
            {
                databaseClient.insertRowIntoTable(databaseConnection, assetDatabaseTable, openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert a data asset from the open metadata ecosystem to database columns.  The information is distributed in the properties
     * and classifications.
     *
     * @param dataAssetElement data asset retrieved from the open metadata ecosystem
     * @param associatedResourceLocationGUID unique identifier of the location where the associated resource is located
     * @param associatedLicenceGUID unique identifier of the license for this asset/resource
     * @param tags colon separated list of tags
     * @param semanticAssignmentTermGUID unique identifier of term that is linked to element with a semantic assignment
     * @return columns
     */
    private Map<String, JDBCDataValue> getAssetDataValues(DataAssetElement dataAssetElement,
                                                          String           associatedResourceLocationGUID,
                                                          String           associatedLicenceGUID,
                                                          String           tags,
                                                          String           semanticAssignmentTermGUID)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        ElementHeader       elementHeader       = dataAssetElement.getElementHeader();
        DataAssetProperties dataAssetProperties = dataAssetElement.getDataAssetProperties();

        processUserIds(elementHeader.getVersions(), elementHeader.getOrigin());

        int confidentiality = 0;
        int criticality     = 0;
        int confidence      = 0;

        if (elementHeader != null)
        {
            openMetadataRecord.put(columnNameAssetGUID, new JDBCDataValue(elementHeader.getGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameAssetType, new JDBCDataValue(elementHeader.getType().getTypeName(), Types.VARCHAR));
            openMetadataRecord.put(columnNameMetaCollectionId,
                                   new JDBCDataValue(elementHeader.getOrigin().getHomeMetadataCollectionId(), Types.VARCHAR));


            openMetadataRecord.put(columnNameCreatedBy, new JDBCDataValue(elementHeader.getVersions().getCreatedBy(), Types.VARCHAR));
            openMetadataRecord.put(columnNameCreationTimestamp,
                                   new JDBCDataValue(new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()), Types.TIMESTAMP));
            if (elementHeader.getVersions().getUpdateTime() != null)
            {
                openMetadataRecord.put(columnNameLastUpdateTimestamp,
                                       new JDBCDataValue(new java.sql.Timestamp(elementHeader.getVersions().getUpdateTime().getTime()), Types.TIMESTAMP));
            }
            else
            {
                openMetadataRecord.put(columnNameLastUpdateTimestamp,
                                       new JDBCDataValue(new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()), Types.TIMESTAMP));
            }
            if (elementHeader.getVersions().getUpdatedBy() != null)
            {
                openMetadataRecord.put(columnNameLastUpdatedBy, new JDBCDataValue(elementHeader.getVersions().getUpdatedBy(), Types.VARCHAR));
            }
            else
            {
                openMetadataRecord.put(columnNameLastUpdatedBy, new JDBCDataValue(elementHeader.getVersions().getCreatedBy(), Types.VARCHAR));
            }

            if ((elementHeader.getVersions().getMaintainedBy() != null) && (! elementHeader.getVersions().getMaintainedBy().isEmpty()))
            {
                StringBuilder userString = new StringBuilder(":");

                for (String user : elementHeader.getVersions().getMaintainedBy())
                {
                    userString.append(user).append(":");
                }

                openMetadataRecord.put(columnNameMaintainedBy, new JDBCDataValue(userString.toString(), Types.VARCHAR));
            }
            else
            {
                openMetadataRecord.put(columnNameMaintainedBy,
                                       new JDBCDataValue(":" + elementHeader.getVersions().getCreatedBy() + ":", Types.VARCHAR));
            }

            openMetadataRecord.put(columnNameLastUpdatedBy, new JDBCDataValue(elementHeader.getVersions().getCreatedBy(), Types.VARCHAR));


            if (elementHeader.getClassifications() != null)
            {
                for (ElementClassification classification : elementHeader.getClassifications())
                {
                    switch (classification.getClassificationName())
                    {
                        case "AssetZoneMembership" -> openMetadataRecord.put(columnNameZoneNames, new JDBCDataValue(
                                this.getZoneNames(classification.getClassificationProperties()), Types.VARCHAR));
                        case "Ownership" ->
                        {
                            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOwnerGUID,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(), "owner",
                                                                                                  80));
                            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOwnerType,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(),
                                                                                                  "ownerTypeName", 40));
                        }
                        case "AssetOrigin" ->
                        {
                            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOriginOrgGUID,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(),
                                                                                                  "organization", 80));
                            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOriginBizCapGUID,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(),
                                                                                                  "businessCapability", 80));
                        }
                        case "Memento" ->
                        {
                            openMetadataRecord.put(columnNameArchived, new JDBCDataValue(classification.getVersions().getCreateTime(), Types.TIMESTAMP));
                        }
                        case "Confidentiality" -> confidentiality = this.getStatusIdentifier(classification.getClassificationProperties());
                        case "Confidence" -> confidence = this.getStatusIdentifier(classification.getClassificationProperties());
                        case "Criticality" -> criticality = this.getStatusIdentifier(classification.getClassificationProperties());
                    }
                }
            }

        }

        openMetadataRecord.put(columnNameConfidentiality, new JDBCDataValue(confidentiality, Types.INTEGER));
        openMetadataRecord.put(columnNameCriticality, new JDBCDataValue(criticality, Types.INTEGER));
        openMetadataRecord.put(columnNameConfidence, new JDBCDataValue(confidence, Types.INTEGER));


        if (dataAssetProperties != null)
        {
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameResourceName,        dataAssetProperties.getTechnicalName());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameResourceDescription, dataAssetProperties.getTechnicalDescription());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameVersionId,           dataAssetProperties.getVersionIdentifier());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameDisplayName,         dataAssetProperties.getDisplayName());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameDisplayDescription,  dataAssetProperties.getDescription());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameQualifiedName,       dataAssetProperties.getQualifiedName());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameDisplaySummary,      dataAssetProperties.getSummary());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameAbbrev,              dataAssetProperties.getAbbreviation());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameUsage,               dataAssetProperties.getUsage());
            if (dataAssetProperties.getAdditionalProperties() != null)
            {
                openMetadataRecord.put(columnNameAdditionalProperties, new JDBCDataValue(dataAssetProperties.getAdditionalProperties().toString(), Types.VARCHAR));
            }
        }

        openMetadataRecord.put(columnNameLicenseGUID, new JDBCDataValue(associatedLicenceGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameResourceLocGUID, new JDBCDataValue(associatedResourceLocationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameTags, new JDBCDataValue(tags, Types.VARCHAR));
        openMetadataRecord.put(columnNameSemanticTerm, new JDBCDataValue(semanticAssignmentTermGUID, Types.VARCHAR));

        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new java.util.Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Only add the property if it is not null.
     *
     * @param openMetadataRecord map of columns
     * @param columnName name of column
     * @param propertyValue value form open metadata ecosystem
     */
    private void addOpenMetadataStringPropertyValue(Map<String, JDBCDataValue> openMetadataRecord,
                                                    String                     columnName,
                                                    String                     propertyValue)
    {
        if (propertyValue != null)
        {
            openMetadataRecord.put(columnName, new JDBCDataValue(propertyValue, Types.VARCHAR));
        }
    }


    /**
     * Return the zone names from the AssetZoneMembership classification.
     *
     * @param classificationProperties properties from the relevant classification
     * @return colon separated zone names (colons at either end too)
     */
    private String getZoneNames(Map<String, Object> classificationProperties)
    {
        if (classificationProperties.get("zoneMembership") instanceof List<?> zoneMembership)
        {
            StringBuilder zonesString = new StringBuilder(":");

            if (zoneMembership.isEmpty())
            {
                zonesString.append(":");
            }
            else
            {
                for (Object zone : zoneMembership)
                {
                    zonesString.append(zone).append(":");
                }
            }

            return zonesString.toString();
        }
        else
        {
            return "::";
        }
    }


    /**
     * Return the status identifier from a governance action classification.
     *
     * @param classificationProperties properties from the relevant classification
     * @return integer
     */
    private Integer getStatusIdentifier(Map<String, Object> classificationProperties)
    {
        if (classificationProperties.get("statusIdentifier") instanceof Integer statusIdentifier)
        {
            return statusIdentifier;
        }
        else
        {
            return 0;
        }
    }


    /**
     * Return a string property from a classification.
     *
     * @param classificationProperties properties from the relevant classification
     * @param propertyName name of property
     * @param maxLength max number of characters
     * @return value of property
     */
    private String getOpenMetadataStringProperty(Map<String, Object> classificationProperties,
                                                 String              propertyName,
                                                 int                 maxLength)
    {
        if (classificationProperties.get(propertyName) instanceof String propertyValue)
        {
            if (propertyValue.length() > maxLength)
            {
                System.out.print("Property name " + propertyName + " is too long; it should only be " + maxLength + "characters: " +  propertyValue);

                return propertyValue.substring(0, maxLength-1);
            }

            return propertyValue;
        }
        else
        {
            return null;
        }
    }


    /**
     * Process the correlation properties for an element retrieved from the open metadata ecosystem.
     *
     * @param elementHeader unique identifier of the attached element and other related information
     * @param metadataCorrelationHeaders correlation properties for each synchronized system
     */
    private void syncCorrelationProperties(ElementHeader                   elementHeader,
                                           List<MetadataCorrelationHeader> metadataCorrelationHeaders)
    {
        if (metadataCorrelationHeaders != null)
        {
            for (MetadataCorrelationHeader metadataCorrelationHeader : metadataCorrelationHeaders)
            {
                syncCorrelationProperties(elementHeader, metadataCorrelationHeader);
            }
        }
    }


    /**
     * Process the correlation properties for an element retrieved from the open metadata ecosystem.
     *
     * @param elementHeader unique identifier of the attached element and other related information
     * @param metadataCorrelationHeader correlation properties
     */
    private void syncCorrelationProperties(ElementHeader             elementHeader,
                                           MetadataCorrelationHeader metadataCorrelationHeader)
    {
        final String methodName = "syncCorrelationProperties";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getCorrelationPropertiesDataValues(elementHeader, metadataCorrelationHeader);

            databaseClient.insertRowIntoTable(databaseConnection, correlationPropertiesDatabaseTable, openMetadataRecord);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert the correlation properties for an element retrieved from the open metadata ecosystem to database columns.
     *
     * @param elementHeader unique identifier of the attached element and other related information
     * @param metadataCorrelationHeader correlation properties
     * @return columns
     */
    private Map<String, JDBCDataValue> getCorrelationPropertiesDataValues(ElementHeader             elementHeader,
                                                                          MetadataCorrelationHeader metadataCorrelationHeader)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        if (elementHeader != null)
        {
            openMetadataRecord.put(columnNameEgeriaOwned,        new JDBCDataValue((elementHeader.getOrigin().getOriginCategory() != ElementOriginCategory.EXTERNAL_SOURCE), Types.BOOLEAN));
            openMetadataRecord.put(columnNameElementGUID,        new JDBCDataValue(elementHeader.getGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameExternalSourceGUID, new JDBCDataValue(elementHeader.getOrigin().getHomeMetadataCollectionId(), Types.VARCHAR));
            openMetadataRecord.put(columnNameTypeName,           new JDBCDataValue(elementHeader.getType().getTypeName(), Types.VARCHAR));
        }

        if (metadataCorrelationHeader != null)
        {
            if (metadataCorrelationHeader.getExternalInstanceCreatedBy() != null)
            {
                syncUserId(metadataCorrelationHeader.getExternalInstanceCreatedBy(),
                           metadataCorrelationHeader.getAssetManagerGUID(),
                           this.getAssociatedUserIdentity(metadataCorrelationHeader.getExternalInstanceCreatedBy()));
            }
            if (metadataCorrelationHeader.getExternalInstanceLastUpdatedBy() != null)
            {
                syncUserId(metadataCorrelationHeader.getExternalInstanceLastUpdatedBy(),
                           metadataCorrelationHeader.getAssetManagerGUID(),
                           this.getAssociatedUserIdentity(metadataCorrelationHeader.getExternalInstanceLastUpdatedBy()));
            }
            if (metadataCorrelationHeader.getLastSynchronized() != null)
            {
                openMetadataRecord.put(columnNameLastConfirmedSyncTime, new JDBCDataValue(new java.sql.Timestamp(metadataCorrelationHeader.getLastSynchronized().getTime()), Types.TIMESTAMP));
            }
            openMetadataRecord.put(columnNameExternalIdentifier,   new JDBCDataValue(metadataCorrelationHeader.getExternalIdentifier(), Types.VARCHAR));
            openMetadataRecord.put(columnNameLastUpdatedBy,        new JDBCDataValue(metadataCorrelationHeader.getExternalInstanceLastUpdatedBy(), Types.VARCHAR));
            openMetadataRecord.put(columnNameLastUpdateTime,       new JDBCDataValue(metadataCorrelationHeader.getExternalInstanceLastUpdateTime(), Types.TIMESTAMP));
            openMetadataRecord.put(columnNameCreatedBy,            new JDBCDataValue(metadataCorrelationHeader.getExternalInstanceCreatedBy(), Types.VARCHAR));
            openMetadataRecord.put(columnNameVersion,              new JDBCDataValue(metadataCorrelationHeader.getExternalInstanceVersion(), Types.BIGINT));
            openMetadataRecord.put(columnNameCreationTime,         new JDBCDataValue(metadataCorrelationHeader.getExternalInstanceCreationTime(), Types.TIMESTAMP));
            openMetadataRecord.put(columnNameAdditionalProperties, new JDBCDataValue(metadataCorrelationHeader.getMappingProperties().toString(), Types.VARCHAR));
        }

        return openMetadataRecord;
    }


    /**
     * Process details of a metadata collection retrieved from the open metadata ecosystem.  These can be found in element headers or
     * correlation properties.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param elementOrigin unique identifier of the metadata collection and other information
     * @param deployedImplementationType description of the type of software capability supporting the metadata collection
     */
    private void syncMetadataCollection(ElementOrigin elementOrigin,
                                        String        deployedImplementationType)
    {
        final String methodName = "syncMetadataCollection";

        if (elementOrigin != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getMetadataCollectionDataValues(elementOrigin.getHomeMetadataCollectionId(),
                                                                                                     elementOrigin.getHomeMetadataCollectionName(),
                                                                                                     elementOrigin.getOriginCategory(),
                                                                                                     deployedImplementationType);

                databaseClient.insertRowIntoTable(databaseConnection, metadataCollectionDatabaseTable, openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the correlation properties for an element retrieved from the open metadata ecosystem to database columns.
     *
     * @param metadataCollectionId unique identifier of the metadata collection
     * @param metadataCollectionName unique name of the metadata collection
     * @param metadataCollectionType type of the metadata collection
     * @param deployedImplementationType description of the type of software capability supporting the metadata collection
     * @return columns
     */
    private Map<String, JDBCDataValue> getMetadataCollectionDataValues(String                metadataCollectionId,
                                                                       String                metadataCollectionName,
                                                                       ElementOriginCategory metadataCollectionType,
                                                                       String                deployedImplementationType)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameMetadataCollectionId,   new JDBCDataValue(metadataCollectionId,   Types.VARCHAR));
        openMetadataRecord.put(columnNameMetadataCollectionName, new JDBCDataValue(metadataCollectionName, Types.VARCHAR));

        if (metadataCollectionType != null)
        {
            openMetadataRecord.put(columnNameMetadataCollectionType, new JDBCDataValue(metadataCollectionType.getName(), Types.VARCHAR));
        }
        else
        {
            openMetadataRecord.put(columnNameMetadataCollectionType, new JDBCDataValue(ElementOriginCategory.UNKNOWN.getName(), Types.VARCHAR));
        }

        openMetadataRecord.put(columnNameDeployedImplType, new JDBCDataValue(deployedImplementationType, Types.VARCHAR));


        return openMetadataRecord;
    }


    /**
     * Process information about a specific asset type.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param elementType details of a specific type
     */
    private void syncAssetType(ElementType elementType)
    {
        final String methodName = "syncAssetTypes";

        if (elementType != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getAssetTypesDataValues(elementType);

                databaseClient.insertRowIntoTable(databaseConnection, assetTypesDatabaseTable, openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the description of a specific type into columns for the asset_types table.
     *
     * @param elementType details of a specific type
     * @return columns
     */
    private Map<String, JDBCDataValue> getAssetTypesDataValues(ElementType elementType)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameLeafType,   new JDBCDataValue(elementType.getTypeName(),   Types.VARCHAR));
        openMetadataRecord.put(columnNameTypeDescription, new JDBCDataValue(elementType.getTypeDescription(), Types.VARCHAR));

        if (elementType.getSuperTypeNames() != null)
        {
            openMetadataRecord.put(columnNameSuperTypes, new JDBCDataValue(this.formatJSONColumn(elementType.getSuperTypeNames()), Types.OTHER));
        }

        return openMetadataRecord;
    }


    /**
     * Process information about a specific location.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param location details of a specific location
     */
    private void syncLocation(OpenMetadataElement location)
    {
        final String methodName = "syncLocation";

        if (location != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getLocationDataValues(location.getElementGUID(),
                                                                                           propertyHelper.getStringProperty(connectorName,
                                                                                                                            "displayName",
                                                                                                                            location.getElementProperties(),
                                                                                                                            methodName),
                                                                                           location.getType().getTypeName());

                databaseClient.insertRowIntoTable(databaseConnection, locationDatabaseTable, openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the description of a specific location into columns for the location table.
     *
     * @param locationGUID unique identifier of the location
     * @param locationName display name of the location
     * @param locationType details of the location's type
     * @return columns
     */
    private Map<String, JDBCDataValue> getLocationDataValues(String locationGUID,
                                                             String locationName,
                                                             String locationType)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameLocationGUID, new JDBCDataValue(locationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameLocationName, new JDBCDataValue(locationName, Types.VARCHAR));
        openMetadataRecord.put(columnNameLocationType, new JDBCDataValue(locationType, Types.VARCHAR));

        return openMetadataRecord;
    }


    /**
     * Process information about a specific license.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param license details of a specific license
     */
    private void syncLicense(OpenMetadataElement license)
    {
        final String methodName = "syncLicense";

        if (license != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getLicenseDataValues(license.getElementGUID(),
                                                                                           propertyHelper.getStringProperty(connectorName,
                                                                                                                            "title",
                                                                                                                            license.getElementProperties(),
                                                                                                                            methodName),
                                                                                           propertyHelper.getStringProperty(connectorName,
                                                                                                                            "description",
                                                                                                                            license.getElementProperties(),
                                                                                                                            methodName));

                databaseClient.insertRowIntoTable(databaseConnection, licenseDatabaseTable, openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the description of a specific location into columns for the location table.
     *
     * @param licenseGUID unique identifier of the location
     * @param licenseName display name of the location
     * @param licenseDescription details of the location's type
     * @return columns
     */
    private Map<String, JDBCDataValue> getLicenseDataValues(String licenseGUID,
                                                            String licenseName,
                                                            String licenseDescription)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameLicenseGUID, new JDBCDataValue(licenseGUID,   Types.VARCHAR));
        openMetadataRecord.put(columnNameLicenseName, new JDBCDataValue(licenseName, Types.VARCHAR));
        openMetadataRecord.put(columnNameLicenseDescription, new JDBCDataValue(licenseDescription, Types.VARCHAR));

        return openMetadataRecord;
    }


    /**
     * Process the correlation properties for an element retrieved from the open metadata ecosystem.
     *
     * @param elementGUID unique identifier of the related element
     * @param numberOfComments number of attached comments
     * @param numberOfRatings number of attached ratings/reviews
     * @param aveRating average star rating
     * @param numberOfTags number of attached informal tags
     * @param numberOfLikes number of attached likes
     */
    private void syncCollaborationActivity(String elementGUID,
                                           int    numberOfComments,
                                           int    numberOfRatings,
                                           int    aveRating,
                                           int    numberOfTags,
                                           int    numberOfLikes)
    {
        final String methodName = "syncCollaborationActivity";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        collaborationActivityDatabaseTable,
                                                                                        columnNameElementGUID,
                                                                                        elementGUID,
                                                                                        columnNameSyncTime,
                                                                                        collaborationActivityTableColumns);

            Map<String, JDBCDataValue> openMetadataRecord = this.getCollaborationActivityDataValues(elementGUID,
                                                                                                    numberOfComments,
                                                                                                    numberOfRatings,
                                                                                                    aveRating,
                                                                                                    numberOfTags,
                                                                                                    numberOfLikes);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
            {
                databaseClient.insertRowIntoTable(databaseConnection, collaborationActivityDatabaseTable, openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert the description of a specific location into columns for the location table.
     *
     * @param elementGUID unique identifier of the related element
     * @param numberOfComments number of attached comments
     * @param numberOfRatings number of attached ratings/reviews
     * @param averageRating average star rating
     * @param numberOfTags number of attached informal tags
     * @param numberOfLikes number of attached likes
     * @return columns
     */
    private Map<String, JDBCDataValue> getCollaborationActivityDataValues(String elementGUID,
                                                                          int    numberOfComments,
                                                                          int    numberOfRatings,
                                                                          int    averageRating,
                                                                          int    numberOfTags,
                                                                          int    numberOfLikes)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameElementGUID,          new JDBCDataValue(elementGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSyncTime,             new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));
        openMetadataRecord.put(columnNameNumberOfComments,     new JDBCDataValue(numberOfComments, Types.INTEGER));
        openMetadataRecord.put(columnNameNumberOfRatings,      new JDBCDataValue(numberOfRatings, Types.INTEGER));
        openMetadataRecord.put(columnNameAverageRating,        new JDBCDataValue(averageRating, Types.INTEGER));
        openMetadataRecord.put(columnNameNumberOfInformalTags, new JDBCDataValue(numberOfTags, Types.INTEGER));
        openMetadataRecord.put(columnNameNumberOfLikes,        new JDBCDataValue(numberOfLikes, Types.INTEGER));

        return openMetadataRecord;
    }


    /**
     * Process any userIds found in the element header.  These maybe from archives and so may not have an associated user identity entity.
     *
     * @param elementVersions details of the people involved in each version of the element
     * @param elementOrigin details of the metadata source
     */
    private void processUserIds(ElementVersions elementVersions,
                                ElementOrigin   elementOrigin)
    {
        String metadataCollectionId = elementOrigin.getHomeMetadataCollectionId();

        syncUserId(elementVersions.getCreatedBy(), metadataCollectionId, getAssociatedUserIdentity(elementVersions.getCreatedBy()));

        if (elementVersions.getUpdatedBy() != null)
        {
            syncUserId(elementVersions.getUpdatedBy(), metadataCollectionId, getAssociatedUserIdentity(elementVersions.getUpdatedBy()));
        }

        if (elementVersions.getMaintainedBy() != null)
        {
            for (String userId : elementVersions.getMaintainedBy())
            {
                syncUserId(userId, metadataCollectionId, getAssociatedUserIdentity(userId));
            }
        }
    }


    private String getAssociatedUserIdentity(String userId)
    {
        final String methodName = "getAssociatedUserIdentity";
        try
        {
            SearchProperties           searchProperties  = new SearchProperties();
            PropertyCondition          propertyCondition = new PropertyCondition();
            PrimitiveTypePropertyValue propertyValue     = new PrimitiveTypePropertyValue();

            propertyValue.setPrimitiveValue(userId);
            propertyValue.setPrimitiveTypeCategory(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING);
            propertyValue.setTypeName(PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING.getName());

            propertyCondition.setProperty("userId");
            propertyCondition.setOperator(PropertyComparisonOperator.EQ);
            propertyCondition.setValue(propertyValue);

            List<PropertyCondition> propertyConditions = new ArrayList<>();
            propertyConditions.add(propertyCondition);

            searchProperties.setMatchCriteria(MatchCriteria.ALL);
            searchProperties.setConditions(propertyConditions);

            List<OpenMetadataElement> userIdentities = openMetadataAccess.findMetadataElements("UserIdentity",
                                                                                               null,
                                                                                               searchProperties,
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               0,
                                                                                               myContext.getMaxPageSize());

            if (userIdentities != null)
            {
                for (OpenMetadataElement userIdentity : userIdentities)
                {
                    if (userIdentity != null)
                    {
                        return userIdentity.getElementGUID();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Add a userId to the external_user table.
     *
     * @param userId unique identifier of the attached element
     * @param metadataCollectionId unique identifier of the metadata collection
     * @param userIdentityGUID unique identifier of the corresponding user identity entity
     */
    private void syncUserId(String userId,
                            String metadataCollectionId,
                            String userIdentityGUID)
    {
        final String methodName = "syncUserId";

        if (userId != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getExternalUserDataValues(userId,
                                                                                               metadataCollectionId,
                                                                                               userIdentityGUID,
                                                                                               null,
                                                                                               null);

                databaseClient.insertRowIntoTable(databaseConnection, externalUserDatabaseTable, openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the description of a specific certification relationship into columns for the certifications table.
     *
     * @param userId unique identifier of the attached element
     * @param metadataCollectionId unique identifier of the metadata collection
     * @param userIdentityGUID unique identifier of the corresponding user identity entity
     * @param startDate when did the certification start?
     * @param endDate when does the certification end; null for indefinitely
     * @return columns
     */
    private Map<String, JDBCDataValue> getExternalUserDataValues(String userId,
                                                                 String metadataCollectionId,
                                                                 String userIdentityGUID,
                                                                 Date   startDate,
                                                                 Date   endDate)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameMetadataCollectionId, new JDBCDataValue(metadataCollectionId, Types.VARCHAR));
        openMetadataRecord.put(columnNameExternalUser, new JDBCDataValue(userId, Types.VARCHAR));
        openMetadataRecord.put(columnNameUserIdGUID, new JDBCDataValue(userIdentityGUID, Types.VARCHAR));

        if (startDate != null)
        {
            openMetadataRecord.put(columnNameStartDate, new JDBCDataValue(new java.sql.Timestamp(startDate.getTime()), Types.TIMESTAMP));
        }

        if (endDate != null)
        {
            openMetadataRecord.put(columnNameEndDate, new JDBCDataValue(new java.sql.Timestamp(endDate.getTime()), Types.TIMESTAMP));
        }

        return openMetadataRecord;
    }


    /**
     * Process information about a specific certification.
     *
     * @param elementGUID unique identifier of associated element
     * @param certification details of a specific certification
     */
    private void syncCertification(String                 elementGUID,
                                   RelatedMetadataElement certification)
    {
        final String methodName = "syncCertification";

        if (certification != null)
        {
            try
            {
                /*
                 * Set up the record for the certification type.
                 */
                syncCertificationType(certification.getElement());

                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            certificationsDatabaseTable,
                                                                                            columnNameCertificationGUID,
                                                                                            elementGUID,
                                                                                            columnNameSyncTime,
                                                                                            certificationsTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getCertificationDataValues(elementGUID,
                                                                                                certification.getRelationshipGUID(),
                                                                                                certification.getElement().getElementGUID(),
                                                                                                propertyHelper.getDateProperty(connectorName,
                                                                                                                               "start",
                                                                                                                               certification.getElement().getElementProperties(),
                                                                                                                               methodName),
                                                                                                propertyHelper.getDateProperty(connectorName,
                                                                                                                               "end",
                                                                                                                               certification.getElement().getElementProperties(),
                                                                                                                               methodName));

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, certificationsDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the description of a specific certification relationship into columns for the certifications table.
     *
     * @param elementGUID unique identifier of the attached element
     * @param certificationGUID unique identifier of the certification relationship
     * @param certificationTypeGUID unique identifier of the certification type element
     * @param startDate when did the certification start?
     * @param endDate when does the certification end; null for indefinitely
     * @return columns
     */
    private Map<String, JDBCDataValue> getCertificationDataValues(String elementGUID,
                                                                  String certificationGUID,
                                                                  String certificationTypeGUID,
                                                                  Date   startDate,
                                                                  Date   endDate)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameReferenceableGUID, new JDBCDataValue(elementGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameCertificationGUID, new JDBCDataValue(certificationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameCertificationTypeGUID, new JDBCDataValue(certificationTypeGUID, Types.VARCHAR));

        if (startDate != null)
        {
            openMetadataRecord.put(columnNameStartDate, new JDBCDataValue(new java.sql.Timestamp(startDate.getTime()), Types.TIMESTAMP));
        }

        if (endDate != null)
        {
            openMetadataRecord.put(columnNameEndDate, new JDBCDataValue(new java.sql.Timestamp(endDate.getTime()), Types.TIMESTAMP));
        }

        return openMetadataRecord;
    }


    /**
     * Process information about a specific certificationType.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param certificationType details of a specific certificationType
     */
    private void syncCertificationType(OpenMetadataElement certificationType)
    {
        final String methodName = "syncCertificationType";

        if (certificationType != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getCertificationTypeDataValues(certificationType.getElementGUID(),
                                                                                                    propertyHelper.getStringProperty(connectorName,
                                                                                                                                     "title",
                                                                                                                                     certificationType.getElementProperties(),
                                                                                                                                     methodName),
                                                                                                    propertyHelper.getStringProperty(connectorName,
                                                                                                                                     "summary",
                                                                                                                                     certificationType.getElementProperties(),
                                                                                                                                     methodName));

                databaseClient.insertRowIntoTable(databaseConnection, certificationTypeDatabaseTable, openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the description of a specific certification relationship into columns for the certifications table.
     *
     * @param certificationTypeGUID unique identifier of the certification type
     * @param certificationTypeTitle display name of the certification type
     * @param certificationTypeSummary details of the certification type
     * @return columns
     */
    private Map<String, JDBCDataValue> getCertificationTypeDataValues(String certificationTypeGUID,
                                                                      String certificationTypeTitle,
                                                                      String certificationTypeSummary)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameCertificationTypeGUID, new JDBCDataValue(certificationTypeGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameCertificationTitle, new JDBCDataValue(certificationTypeTitle, Types.VARCHAR));
        openMetadataRecord.put(columnNameCertificationSummary, new JDBCDataValue(certificationTypeSummary, Types.VARCHAR));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific user's contribution.
     *
     * @param contribution details of a specific contribution
     */
    private void syncContribution(String                 userGUID,
                                  RelatedMetadataElement contribution)
    {
        final String methodName = "syncContribution";

        if (contribution != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            contributionsDatabaseTable,
                                                                                            columnNameUserGUID,
                                                                                            userGUID,
                                                                                            columnNameSnapshotTimestamp,
                                                                                            contributionsTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getContributionDataValues(userGUID,
                                                                                               propertyHelper.getLongProperty(connectorName,
                                                                                                                               "karmaPoints",
                                                                                                                               contribution.getElement().getElementProperties(),
                                                                                                                               methodName));

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSnapshotTimestamp))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, contributionsDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the contributions of a specific user into columns for the contributions table.
     *
     * @param userGUID unique identifier of the attached element
     * @param karmaPoints amount of activity from user
     * @return columns
     */
    private Map<String, JDBCDataValue> getContributionDataValues(String userGUID,
                                                                 long   karmaPoints)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameUserGUID,          new JDBCDataValue(userGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSnapshotTimestamp, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));
        openMetadataRecord.put(columnNameKarmaPoints,       new JDBCDataValue(karmaPoints, Types.BIGINT));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific schema attribute attached to an asset.
     *
     * @param assetGUID unique identifier of the attached asset
     * @param assetQualifiedName unique name of the attached asset
     * @param glossaryTermGUID unique identifier of the glossary term attached via SemanticAssignment
     * @param hasProfile does this data filed have at least one SchemaAttributeDefinition (0615) relationship
     * @param schemaAttributeElement details of a specific schemaAttributeElement
     */
    private void syncDataField(String                 assetGUID,
                               String                 assetQualifiedName,
                               String                 glossaryTermGUID,
                               boolean                hasProfile,
                               SchemaAttributeElement schemaAttributeElement)
    {
        final String methodName = "syncDataField";

        if (schemaAttributeElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            dataFieldsDatabaseTable,
                                                                                            columnNameDataFieldGUID,
                                                                                            schemaAttributeElement.getElementHeader().getGUID(),
                                                                                            columnNameSyncTime,
                                                                                            dataFieldsTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getDataFieldDataValues(assetGUID,
                                                                                            assetQualifiedName,
                                                                                            glossaryTermGUID,
                                                                                            hasProfile,
                                                                                            schemaAttributeElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, dataFieldsDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the details of a specific schema attribute into columns for the data_fields table.
     *
     * @param assetGUID unique identifier of the attached asset
     * @param assetQualifiedName unique name of the attached asset
     * @param glossaryTermGUID unique identifier of the glossary term attached via SemanticAssignment
     * @param hasProfile does this data filed have at least one SchemaAttributeDefinition (0615) relationship
     * @param schemaAttributeElement details of a specific schemaAttributeElement
     * @return columns
     */
    private Map<String, JDBCDataValue> getDataFieldDataValues(String                 assetGUID,
                                                              String                 assetQualifiedName,
                                                              String                 glossaryTermGUID,
                                                              boolean                hasProfile,
                                                              SchemaAttributeElement schemaAttributeElement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        processUserIds(schemaAttributeElement.getElementHeader().getVersions(), schemaAttributeElement.getElementHeader().getOrigin());

        openMetadataRecord.put(columnNameDataFieldGUID,        new JDBCDataValue(schemaAttributeElement.getElementHeader().getGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameDataFieldName,        new JDBCDataValue(schemaAttributeElement.getSchemaAttributeProperties().getDisplayName(), Types.VARCHAR));
        openMetadataRecord.put(columnNameVersionId,            new JDBCDataValue(Long.toString(schemaAttributeElement.getElementHeader().getVersions().getVersion()), Types.VARCHAR));
        openMetadataRecord.put(columnNameSyncTime,             new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));
        openMetadataRecord.put(columnNameSemanticTerm,         new JDBCDataValue(glossaryTermGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameHasProfile,           new JDBCDataValue(hasProfile, Types.BOOLEAN));
        if (schemaAttributeElement.getElementHeader().getClassifications() != null)
        {
            for (ElementClassification classification : schemaAttributeElement.getElementHeader().getClassifications())
            {
                if ("Confidentiality".equals(classification.getClassificationName()))
                {
                    openMetadataRecord.put(columnNameConfidentialityLevel, new JDBCDataValue(this.getStatusIdentifier(classification.getClassificationProperties()), Types.INTEGER));
                }
            }
        }
        openMetadataRecord.put(columnNameAssetQualifiedName,   new JDBCDataValue(assetQualifiedName, Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetGUID,            new JDBCDataValue(assetGUID, Types.VARCHAR));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific department (team) in the organization.
     *
     * @param parentDepartmentGUID unique identifier of the parent department
     * @param manager unique identifier of manager's profile
     * @param department details of a specific department
     */
    private void syncDepartment(String                 parentDepartmentGUID,
                                String                 manager,
                                OpenMetadataElement department)
    {
        final String methodName = "syncDepartment";

        if (department != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            departmentDatabaseTable,
                                                                                            columnNameDepGUID,
                                                                                            department.getElementGUID(),
                                                                                            columnNameSyncTime,
                                                                                            departmentTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getDepartmentDataValues(parentDepartmentGUID, manager, department);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, departmentDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert the contributions of a specific user into columns for the contributions table.
     *
     * @param parentDepartmentGUID unique identifier of the parent department
     * @param manager unique identifier of manager's profile
     * @param department details of a specific department
     * @return columns
     */
    private Map<String, JDBCDataValue> getDepartmentDataValues(String              parentDepartmentGUID,
                                                               String              manager,
                                                               OpenMetadataElement department)
    {
        final String methodName = "getDepartmentDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameDepGUID,              new JDBCDataValue(department.getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameDeptName,             new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                                  "name",
                                                                                                                  department.getElementProperties(),
                                                                                                                  methodName),
                                                                                 Types.VARCHAR));
        openMetadataRecord.put(columnNameSyncTime,             new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));
        openMetadataRecord.put(columnNameManager,              new JDBCDataValue(manager, Types.VARCHAR));
        openMetadataRecord.put(columnNameParentDepartmentGUID, new JDBCDataValue(parentDepartmentGUID, Types.VARCHAR));

        return openMetadataRecord;
    }



    /**
     * Process a glossary retrieved from the open metadata ecosystem.
     *
     * @param glossaryElement description of the glossary
     * @param numberOfTerms number of terms in this glossary
     * @param numberOfCategories number of categories in this glossary
     * @param numberOfLinkedTerms number of terms categorized
     * @param license attached license (maybe null)
     */
    private void syncGlossary(GlossaryElement     glossaryElement,
                              int                 numberOfTerms,
                              int                 numberOfCategories,
                              int                 numberOfLinkedTerms,
                              OpenMetadataElement license)
    {
        final String methodName = "syncGlossary";

        try
        {
            String licenseGUID = null;

            if (license != null)
            {
                licenseGUID = license.getElementGUID();
            }

            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        glossaryDatabaseTable,
                                                                                        columnNameGlossaryGUID,
                                                                                        glossaryElement.getElementHeader().getGUID(),
                                                                                        columnNameSyncTime,
                                                                                        glossaryTableColumns);

            Map<String, JDBCDataValue> openMetadataRecord = this.getGlossaryDataValues(glossaryElement,
                                                                                       numberOfTerms,
                                                                                       numberOfCategories,
                                                                                       numberOfLinkedTerms,
                                                                                       licenseGUID);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
            {
                databaseClient.insertRowIntoTable(databaseConnection, glossaryDatabaseTable, openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert a data asset from the open metadata ecosystem to database columns.  The information is distributed in the properties
     * and classifications.
     *
     * @param glossaryElement description of the glossary
     * @param numberOfTerms number of terms in this glossary
     * @param numberOfCategories number of categories in this glossary
     * @param numberOfLinkedTerms number of terms categorized
     * @param licenseGUID unique identifier of an attached license
     * @return columns
     */
    private Map<String, JDBCDataValue> getGlossaryDataValues(GlossaryElement glossaryElement,
                                                             int             numberOfTerms,
                                                             int             numberOfCategories,
                                                             int             numberOfLinkedTerms,
                                                             String          licenseGUID)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        ElementHeader      elementHeader      = glossaryElement.getElementHeader();
        GlossaryProperties glossaryProperties = glossaryElement.getGlossaryProperties();

        if (elementHeader != null)
        {
            processUserIds(elementHeader.getVersions(), elementHeader.getOrigin());

            openMetadataRecord.put(columnNameGlossaryGUID, new JDBCDataValue(elementHeader.getGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameMetaCollectionId,
                                   new JDBCDataValue(elementHeader.getOrigin().getHomeMetadataCollectionId(), Types.VARCHAR));


            openMetadataRecord.put(columnNameCreationTimestamp,
                                   new JDBCDataValue(new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()), Types.TIMESTAMP));
            if (elementHeader.getVersions().getUpdateTime() != null)
            {
                openMetadataRecord.put(columnNameLastUpdateTimestamp,
                                       new JDBCDataValue(new java.sql.Timestamp(elementHeader.getVersions().getUpdateTime().getTime()), Types.TIMESTAMP));
            }
            else
            {
                openMetadataRecord.put(columnNameLastUpdateTimestamp,
                                       new JDBCDataValue(new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()), Types.TIMESTAMP));
            }

            if ((elementHeader.getClassifications() != null) && (! elementHeader.getClassifications().isEmpty()))
            {
                StringBuilder classifications = new StringBuilder(":");

                for (ElementClassification classification : elementHeader.getClassifications())
                {
                    classifications.append(classification.getClassificationName()).append(":");

                    if (classification.getClassificationName().equals("Ownership"))
                    {
                        addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOwnerGUID,
                                                           this.getOpenMetadataStringProperty(classification.getClassificationProperties(), "owner", 80));
                        addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOwnerType,
                                                           this.getOpenMetadataStringProperty(classification.getClassificationProperties(), "ownerTypeName", 40));
                    }
                }

                openMetadataRecord.put(columnNameClassifications, new JDBCDataValue(classifications.toString(), Types.VARCHAR));
            }
        }

        if (glossaryProperties != null)
        {
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameGlossaryName,        glossaryProperties.getDisplayName());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameGlossaryDescription, glossaryProperties.getDescription());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameGlossaryLanguage,    glossaryProperties.getLanguage());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameQualifiedName,       glossaryProperties.getQualifiedName());
            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameUsage,               glossaryProperties.getUsage());
            if (glossaryProperties.getAdditionalProperties() != null)
            {
                openMetadataRecord.put(columnNameAdditionalProperties, new JDBCDataValue(glossaryProperties.getAdditionalProperties().toString(), Types.VARCHAR));
            }
        }

        openMetadataRecord.put(columnNameNumberOfTerms, new JDBCDataValue(numberOfTerms, Types.INTEGER));
        openMetadataRecord.put(columnNameNumberOfCategories, new JDBCDataValue(numberOfCategories, Types.INTEGER));
        openMetadataRecord.put(columnNameNumberOfLinkedTerms, new JDBCDataValue(numberOfLinkedTerms, Types.INTEGER));
        openMetadataRecord.put(columnNameLicenseGUID, new JDBCDataValue(licenseGUID, Types.VARCHAR));

        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new java.util.Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process a glossary term retrieved from the open metadata ecosystem.
     *
     * @param glossaryTermElement glossary term retrieved from the open metadata ecosystem
     * @param glossaryGUID unique identifier of the owning glossary
     * @param lastFeedbackTime last time a type of feedback was added to the term
     * @param numberOfLinkedElements number of semantically linked elements
     * @param lastLinkTime last time a semantic assignment was attached to this term
     */
    private void syncTermActivity(GlossaryTermElement glossaryTermElement,
                                  String              glossaryGUID,
                                  Date                lastFeedbackTime,
                                  int                 numberOfLinkedElements,
                                  Date                lastLinkTime)
    {
        final String methodName = "syncTermActivity";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        termActivityDatabaseTable,
                                                                                        columnNameTermGUID,
                                                                                        glossaryTermElement.getElementHeader().getGUID(),
                                                                                        columnNameSyncTime,
                                                                                        termActivityTableColumns);

            Map<String, JDBCDataValue> openMetadataRecord = this.getTermActivityDataValues(glossaryTermElement,
                                                                                           glossaryGUID,
                                                                                           lastFeedbackTime,
                                                                                           numberOfLinkedElements,
                                                                                           lastLinkTime);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
            {
                databaseClient.insertRowIntoTable(databaseConnection, termActivityDatabaseTable, openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert a glossary term from the open metadata ecosystem to database columns.  The information is distributed in the properties, relationships
     * and classifications.
     *
     * @param glossaryTermElement glossary term retrieved from the open metadata ecosystem
     * @param glossaryGUID unique identifier of the owning glossary
     * @param lastFeedbackTime last time a type of feedback was added to the term
     * @param numberOfLinkedElements number of semantically linked elements
     * @param lastLinkTime last time a semantic assignment was attached to this term
     * @return columns
     */
    private Map<String, JDBCDataValue> getTermActivityDataValues(GlossaryTermElement glossaryTermElement,
                                                                 String              glossaryGUID,
                                                                 Date                lastFeedbackTime,
                                                                 int                 numberOfLinkedElements,
                                                                 Date                lastLinkTime)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        ElementHeader          elementHeader          = glossaryTermElement.getElementHeader();
        GlossaryTermProperties glossaryTermProperties = glossaryTermElement.getGlossaryTermProperties();

        int confidentiality = 0;
        int criticality     = 0;
        int confidence      = 0;

        if (elementHeader != null)
        {
            processUserIds(elementHeader.getVersions(), elementHeader.getOrigin());

            openMetadataRecord.put(columnNameTermGUID, new JDBCDataValue(elementHeader.getGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameCreationTimestamp,
                                   new JDBCDataValue(new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()), Types.TIMESTAMP));

            if (elementHeader.getClassifications() != null)
            {

                for (ElementClassification classification : elementHeader.getClassifications())
                {
                    switch (classification.getClassificationName())
                    {
                        case "Ownership" ->
                        {
                            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOwnerGUID,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(), "owner",
                                                                                                  80));
                            addOpenMetadataStringPropertyValue(openMetadataRecord, columnNameOwnerType,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(),
                                                                                                  "ownerTypeName", 40));
                        }
                        case "Confidentiality" -> confidentiality = this.getStatusIdentifier(classification.getClassificationProperties());
                        case "Confidence" -> confidence = this.getStatusIdentifier(classification.getClassificationProperties());
                        case "Criticality" -> criticality = this.getStatusIdentifier(classification.getClassificationProperties());
                    }
                }
            }
        }

        openMetadataRecord.put(columnNameConfidentiality, new JDBCDataValue(confidentiality, Types.INTEGER));
        openMetadataRecord.put(columnNameCriticality, new JDBCDataValue(criticality, Types.INTEGER));
        openMetadataRecord.put(columnNameConfidence, new JDBCDataValue(confidence, Types.INTEGER));


        if (glossaryTermProperties != null)
        {
            openMetadataRecord.put(columnNameTermName, new JDBCDataValue(glossaryTermProperties.getDisplayName(), Types.VARCHAR));
            openMetadataRecord.put(columnNameQualifiedName, new JDBCDataValue(glossaryTermProperties.getQualifiedName(), Types.VARCHAR));
            openMetadataRecord.put(columnNameTermSummary, new JDBCDataValue(glossaryTermProperties.getSummary(), Types.VARCHAR));
            openMetadataRecord.put(columnNameVersionId, new JDBCDataValue(glossaryTermProperties.getPublishVersionIdentifier(), Types.VARCHAR));
        }

        openMetadataRecord.put(columnNameGlossaryGUID, new JDBCDataValue(glossaryGUID, Types.VARCHAR));
        if (lastFeedbackTime != null)
        {
            openMetadataRecord.put(columnNameLastFeedbackTimestamp, new JDBCDataValue(new java.sql.Timestamp(lastFeedbackTime.getTime()), Types.VARCHAR));
        }
        openMetadataRecord.put(columnNameNumberLinkedElements, new JDBCDataValue(numberOfLinkedElements, Types.INTEGER));
        if (lastLinkTime != null)
        {
            openMetadataRecord.put(columnNameLastLinkTimestamp, new JDBCDataValue(new java.sql.Timestamp(lastLinkTime.getTime()), Types.TIMESTAMP));
        }

        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new java.util.Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process an asset relationship retrieved from the open metadata ecosystem.
     *
     * @param relatedAsset relationship information
     */
    private void syncRelatedAsset(RelatedMetadataElements relatedAsset)
    {
        final String methodName = "syncRelatedAsset";

        if (relatedAsset != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            relatedAssetDatabaseTable,
                                                                                            columnNameRelationshipGUID,
                                                                                            relatedAsset.getRelationshipGUID(),
                                                                                            columnNameSyncTime,
                                                                                            relatedAssetsTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getRelatedAssetDataValues(relatedAsset);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, relatedAssetDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a relationship from the open metadata ecosystem to database columns.
     *
     * @param relatedAsset relationship information
     * @return columns
     */
    private Map<String, JDBCDataValue> getRelatedAssetDataValues(RelatedMetadataElements relatedAsset)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameEnd1GUID, new JDBCDataValue(relatedAsset.getElementGUIDAtEnd1(), Types.VARCHAR));
        openMetadataRecord.put(columnNameEnd2GUID, new JDBCDataValue(relatedAsset.getElementGUIDAtEnd2(), Types.VARCHAR));
        openMetadataRecord.put(columnNameEnd1AttributeName, new JDBCDataValue(relatedAsset.getLabelAtEnd1(), Types.VARCHAR));
        openMetadataRecord.put(columnNameEnd2AttributeName, new JDBCDataValue(relatedAsset.getLabelAtEnd2(), Types.VARCHAR));
        openMetadataRecord.put(columnNameRelationshipTypeName, new JDBCDataValue(relatedAsset.getType().getTypeName(), Types.VARCHAR));
        openMetadataRecord.put(columnNameRelationshipGUID, new JDBCDataValue(relatedAsset.getRelationshipGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process person role element retrieved from the open metadata ecosystem.
     *
     * @param role role information
     */
    private void syncRole(OpenMetadataElement role)
    {
        final String methodName = "syncRole";

        if (role != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            roleDatabaseTable,
                                                                                            columnNameRoleGUID,
                                                                                            role.getElementGUID(),
                                                                                            columnNameSyncTime,
                                                                                            roleTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getRoleDataValues(role);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, roleDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a role from the open metadata ecosystem to database columns.
     *
     * @param openMetadataElement role information
     * @return columns
     */
    private Map<String, JDBCDataValue> getRoleDataValues(OpenMetadataElement openMetadataElement)
    {
        final String methodName = "getRoleDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameRoleGUID, new JDBCDataValue(openMetadataElement.getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameRoleName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                      "name",
                                                                                                      openMetadataElement.getElementProperties(),
                                                                                                      methodName),
                                                                     Types.VARCHAR));
        openMetadataRecord.put(columnNameRoleType, new JDBCDataValue(openMetadataElement.getType().getTypeName(), Types.VARCHAR));
        openMetadataRecord.put(columnNameHeadCount, new JDBCDataValue(propertyHelper.getIntProperty(connectorName,
                                                                                                    "headCount",
                                                                                                    openMetadataElement.getElementProperties(),
                                                                                                    methodName),
                                                                      Types.INTEGER));
        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }



    /**
     * Process an PersonRoleAppointment relationship retrieved from the open metadata ecosystem.
     *
     * @param userGUID unique identifier of associated user identify
     * @param personRoleAppointment relationship information
     */
    private void syncRoleToUser(String                  userGUID,
                                RelatedMetadataElements personRoleAppointment)
    {
        final String methodName = "syncRoleToUser";

        if (personRoleAppointment != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            roleToUserDatabaseTable,
                                                                                            columnNameRelationshipGUID,
                                                                                            personRoleAppointment.getRelationshipGUID(),
                                                                                            columnNameSyncTime,
                                                                                            role2UserTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getRoleToUserDataValues(userGUID, personRoleAppointment);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, roleToUserDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a PersonRoleAppointment relationship from the open metadata ecosystem to database columns.
     *
     * @param userGUID unique identifier of associated user identify
     * @param personRoleAppointment relationship information
     * @return columns
     */
    private Map<String, JDBCDataValue> getRoleToUserDataValues(String                  userGUID,
                                                               RelatedMetadataElements personRoleAppointment)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameRoleGUID, new JDBCDataValue(personRoleAppointment.getElementGUIDAtEnd2(), Types.VARCHAR));
        openMetadataRecord.put(columnNameUserGUID, new JDBCDataValue(userGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameRelationshipGUID, new JDBCDataValue(personRoleAppointment.getRelationshipGUID(), Types.VARCHAR));

        if (personRoleAppointment.getEffectiveFromTime() != null)
        {
            openMetadataRecord.put(columnNameStartDate, new JDBCDataValue(new java.sql.Timestamp(personRoleAppointment.getEffectiveFromTime().getTime()), Types.TIMESTAMP));
        }

        if (personRoleAppointment.getEffectiveToTime() != null)
        {
            openMetadataRecord.put(columnNameEndDate, new JDBCDataValue(new java.sql.Timestamp(personRoleAppointment.getEffectiveToTime().getTime()), Types.TIMESTAMP));
        }

        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }



    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param toDoElement to do information
     * @param toDoSourceElements  source of the to do
     * @param actionAssignmentElements actor responsible for completing the work - may be null
     */
    private void syncToDo(OpenMetadataElement    toDoElement,
                          RelatedMetadataElement toDoSourceElements,
                          RelatedMetadataElement actionAssignmentElements)
    {
        final String methodName = "syncToDo";

        if (toDoElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            toDoDatabaseTable,
                                                                                            columnNameToDoGUID,
                                                                                            toDoElement.getElementGUID(),
                                                                                            columnNameSyncTime,
                                                                                            toDoTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getToDoDataValues(toDoElement,
                                                                                       toDoSourceElements,
                                                                                       actionAssignmentElements);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, toDoDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a user identity and profile from the open metadata ecosystem to database columns.
     *
     * @param toDoElement to do information
     * @param toDoSourceElement  source of the to do
     * @param actionAssignmentElement actor responsible for completing the work - may be null
     * @return columns
     */
    private Map<String, JDBCDataValue> getToDoDataValues(OpenMetadataElement    toDoElement,
                                                         RelatedMetadataElement toDoSourceElement,
                                                         RelatedMetadataElement actionAssignmentElement)
    {
        final String methodName = "getToDoDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameToDoGUID, new JDBCDataValue(toDoElement.getElementGUID(), Types.VARCHAR));

        openMetadataRecord.put(columnNameQualifiedName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                           toDoElement.getElementProperties(),
                                                                                                           methodName),
                                                                          Types.VARCHAR));

        openMetadataRecord.put(columnNameDisplayName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.NAME.name,
                                                                                                         toDoElement.getElementProperties(),
                                                                                                         methodName),
                                                                        Types.VARCHAR));

        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                        OpenMetadataType.CREATION_TIME_PROPERTY_NAME,
                                                                                                        toDoElement.getElementProperties(),
                                                                                                        methodName),
                                                                         Types.TIMESTAMP));

        openMetadataRecord.put(columnNameToDoType, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                      OpenMetadataType.TO_DO_TYPE_PROPERTY_NAME,
                                                                                                      toDoElement.getElementProperties(),
                                                                                                      methodName),
                                                                     Types.VARCHAR));

        openMetadataRecord.put(columnNamePriority, new JDBCDataValue(propertyHelper.getIntProperty(connectorName,
                                                                                                   OpenMetadataType.PRIORITY_PROPERTY_NAME,
                                                                                                   toDoElement.getElementProperties(),
                                                                                                   methodName),
                                                                     Types.INTEGER));

        openMetadataRecord.put(columnNameDueTime, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                   OpenMetadataType.DUE_TIME_PROPERTY_NAME,
                                                                                                   toDoElement.getElementProperties(),
                                                                                                   methodName),
                                                                    Types.TIMESTAMP));

        openMetadataRecord.put(columnNameLastReviewedTime, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                            OpenMetadataType.LAST_REVIEW_TIME_PROPERTY_NAME,
                                                                                                            toDoElement.getElementProperties(),
                                                                                                            methodName),
                                                                             Types.TIMESTAMP));

        openMetadataRecord.put(columnNameCompletionTime, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                          OpenMetadataType.COMPLETION_TIME_PROPERTY_NAME,
                                                                                                          toDoElement.getElementProperties(),
                                                                                                          methodName),
                                                                           Types.TIMESTAMP));

        openMetadataRecord.put(columnNameStatus, new JDBCDataValue(propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                                                                              OpenMetadataType.STATUS_PROPERTY_NAME,
                                                                                                              toDoElement.getElementProperties(),
                                                                                                              methodName),
                                                                   Types.VARCHAR));

        if (toDoSourceElement != null)
        {
            openMetadataRecord.put(columnNameToDoSourceGUID, new JDBCDataValue(toDoSourceElement.getElement().getElementGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameToDoSourceType, new JDBCDataValue(toDoSourceElement.getElement().getType().getTypeName(), Types.VARCHAR));
        }

        if (actionAssignmentElement != null)
        {
            openMetadataRecord.put(columnNameActorGUID, new JDBCDataValue(actionAssignmentElement.getElement().getElementGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameActorType, new JDBCDataValue(actionAssignmentElement.getElement().getType().getTypeName(), Types.VARCHAR));
        }

        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param userIdentifyElement user identity information
     * @param personElement  person profile element
     * @param departmentGUID primary team
     * @param locationGUID unique identifier of the location where the person is associated with (ProfileLocation)
     * @param organizationName display name of the organization - from traversing the team hierarchy
     */
    private void syncUserIdentity(OpenMetadataElement    userIdentifyElement,
                                  RelatedMetadataElement personElement,
                                  String                 departmentGUID,
                                  String                 locationGUID,
                                  String                 organizationName)
    {
        final String methodName = "syncUserIdentity";

        if (userIdentifyElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            userIdentityDatabaseTable,
                                                                                            columnNameUserIdGUID,
                                                                                            userIdentifyElement.getElementGUID(),
                                                                                            columnNameSyncTime,
                                                                                            userIdentityTableColumns);

                Map<String, JDBCDataValue> openMetadataRecord = this.getUserIdentityDataValues(userIdentifyElement,
                                                                                               personElement,
                                                                                               departmentGUID,
                                                                                               locationGUID,
                                                                                               organizationName);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, userIdentityDatabaseTable, openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a user identity and profile from the open metadata ecosystem to database columns.
     *
     * @param userIdentifyElement user identity information
     * @param personElement  person profile element
     * @param departmentGUID primary team
     * @param locationGUID unique identifier of the location where the person is associated with (ProfileLocation)
     * @param organizationName display name of the organization - from traversing the team hierarchy
     * @return columns
     */
    private Map<String, JDBCDataValue> getUserIdentityDataValues(OpenMetadataElement    userIdentifyElement,
                                                                 RelatedMetadataElement personElement,
                                                                 String                 departmentGUID,
                                                                 String                 locationGUID,
                                                                 String                 organizationName)
    {
        final String methodName = "getUserIdentityDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        if (personElement != null)
        {
            openMetadataRecord.put(columnNameProfileGUID, new JDBCDataValue(personElement.getElement().getElementGUID(), Types.VARCHAR));

            openMetadataRecord.put(columnNameEmployeeNumber, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                                "employeeNumber",
                                                                                                                personElement.getElement().getElementProperties(),
                                                                                                                methodName),
                                                                               Types.VARCHAR));

            openMetadataRecord.put(columnNamePreferredName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                               "name",
                                                                                                               personElement.getElement().getElementProperties(),
                                                                                                               methodName),
                                                                              Types.VARCHAR));

            openMetadataRecord.put(columnNameResidentCountry, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                                 "residentCountry",
                                                                                                                 personElement.getElement().getElementProperties(),
                                                                                                                 methodName),
                                                                                Types.VARCHAR));

        }

        openMetadataRecord.put(columnNameUserIdGUID, new JDBCDataValue(userIdentifyElement.getElementGUID(), Types.VARCHAR));

        openMetadataRecord.put(columnNameUserId, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                    "userId",
                                                                                                    userIdentifyElement.getElementProperties(),
                                                                                                    methodName),
                                                                   Types.VARCHAR));
        openMetadataRecord.put(columnNameDistinguishedName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                               "distinguishedName",
                                                                                                               userIdentifyElement.getElementProperties(),
                                                                                                               methodName),
                                                                              Types.VARCHAR));
        openMetadataRecord.put(columnNameDepartmentGUID, new JDBCDataValue(departmentGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameLocation, new JDBCDataValue(locationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameOrgName, new JDBCDataValue(organizationName, Types.VARCHAR));

        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process information about a survey report.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportElement element describing the survey report
     * @param relatedAsset asset described in the report
     * @param relatedEngineAction details of the engine action that initiated the report
     */
    private void syncSurveyReport(OpenMetadataElement    surveyReportElement,
                                  RelatedMetadataElement relatedAsset,
                                  RelatedMetadataElement relatedEngineAction)
    {
        final String methodName = "syncSurveyReport";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getSurveyReportDataValues(surveyReportElement,
                                                                                           relatedAsset,
                                                                                           relatedEngineAction);

            databaseClient.insertRowIntoTable(databaseConnection, surveyReportDatabaseTable, openMetadataRecord);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

    }


    /**
     * Convert the description of a specific survey report into columns for the sr_report table.
     *
     * @param surveyReportElement element describing the survey report
     * @param relatedAsset asset described in the report
     * @param relatedEngineAction details of the engine action that initiated the report
     * @return columns
     */
    private Map<String, JDBCDataValue> getSurveyReportDataValues(OpenMetadataElement    surveyReportElement,
                                                                 RelatedMetadataElement relatedAsset,
                                                                 RelatedMetadataElement relatedEngineAction)
    {
        final String methodName = "getSurveyReportDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSurveyGUID, new JDBCDataValue(surveyReportElement.getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameQualifiedName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                           surveyReportElement.getElementProperties(),
                                                                                                           methodName),
                                                                          Types.VARCHAR));
        openMetadataRecord.put(columnNameDisplayName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                         surveyReportElement.getElementProperties(),
                                                                                                         methodName),
                                                                        Types.VARCHAR));
        openMetadataRecord.put(columnNameDescription, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.DESCRIPTION.name,
                                                                                                         surveyReportElement.getElementProperties(),
                                                                                                         methodName),
                                                                        Types.VARCHAR));
        openMetadataRecord.put(columnNamePurpose, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                     OpenMetadataProperty.PURPOSE.name,
                                                                                                     surveyReportElement.getElementProperties(),
                                                                                                     methodName),
                                                                    Types.VARCHAR));

        openMetadataRecord.put(columnNameEndTimestamp, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                     OpenMetadataProperty.COMPLETION_DATE.name,
                                                                                                     surveyReportElement.getElementProperties(),
                                                                                                     methodName),
                                                                         Types.TIMESTAMP));
        openMetadataRecord.put(columnNameStartTimestamp, new JDBCDataValue(propertyHelper.getDateProperty(connectorName,
                                                                                                          OpenMetadataProperty.START_DATE.name,
                                                                                                          surveyReportElement.getElementProperties(),
                                                                                                          methodName),
                                                                           Types.TIMESTAMP));

        openMetadataRecord.put(columnNameAssetGUID, new JDBCDataValue(relatedAsset.getElement().getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetType, new JDBCDataValue(relatedAsset.getElement().getType().getTypeName(), Types.VARCHAR));


        openMetadataRecord.put(columnNameEngineActionGUID, new JDBCDataValue(relatedEngineAction.getElement().getElementGUID(), Types.VARCHAR));
        openMetadataRecord.put(columnNameInitiator, new JDBCDataValue(relatedEngineAction.getVersions().getCreatedBy(), Types.VARCHAR));
        openMetadataRecord.put(columnNameRequestType, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                                         relatedEngineAction.getElement().getElementProperties(),
                                                                                                         methodName),
                                                                        Types.VARCHAR));
        openMetadataRecord.put(columnNameGovernanceEngineName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataType.EXECUTOR_ENGINE_NAME_PROPERTY_NAME,
                                                                                                         relatedEngineAction.getElement().getElementProperties(),
                                                                                                         methodName),
                                                                                 Types.VARCHAR));
        openMetadataRecord.put(columnNameEngineHostUserId, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                              OpenMetadataType.PROCESSING_ENGINE_USER_ID_PROPERTY_NAME,
                                                                                                              relatedEngineAction.getElement().getElementProperties(),
                                                                                                              methodName),
                                                                             Types.VARCHAR));


        return openMetadataRecord;
    }


    /**
     * Process information about a request for action annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportGUID unique identifier for the survey report
     * @param requestForActionAnnotation description of the annotation
     * @param relatedActionTarget details of the element that describes where the action should take place
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncRequestForAction(String                 surveyReportGUID,
                                      OpenMetadataElement    requestForActionAnnotation,
                                      RelatedMetadataElement relatedActionTarget,
                                      RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "syncRequestForAction";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getRequestForActionDataValues(surveyReportGUID,
                                                                                               requestForActionAnnotation,
                                                                                               relatedActionTarget,
                                                                                               relatedAnnotationSubject);

            databaseClient.insertRowIntoTable(databaseConnection, requestForActionDatabaseTable, openMetadataRecord);

            if (relatedActionTarget != null)
            {
                processFileClassifiers(surveyReportGUID, relatedActionTarget);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

    }


    /**
     * Convert the description of a specific request ofr action annotation into columns for the sr_request_for_action table.
     *
     * @param surveyReportGUID unique identifier for the survey report
     * @param requestForActionAnnotation description of the annotation
     * @param relatedActionTarget details of the element that describes where the action should take place
     * @param relatedAnnotationSubject element related via the associated annotation
     * @return columns
     */
    private Map<String, JDBCDataValue> getRequestForActionDataValues(String                 surveyReportGUID,
                                                                     OpenMetadataElement    requestForActionAnnotation,
                                                                     RelatedMetadataElement relatedActionTarget,
                                                                     RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "getRequestForActionDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(requestForActionAnnotation.getVersions().getCreateTime(), Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(requestForActionAnnotation.getElementGUID(), Types.VARCHAR));

        if (relatedAnnotationSubject != null)
        {
            openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(relatedAnnotationSubject.getElement().getElementGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(relatedAnnotationSubject.getElement().getType().getTypeName(), Types.VARCHAR));
        }
        else
        {
            openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(null, Types.VARCHAR));
            openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(null, Types.VARCHAR));
        }

        if (relatedActionTarget != null)
        {
            openMetadataRecord.put(columnNameActionTargetGUID, new JDBCDataValue(relatedActionTarget.getElement().getElementGUID(), Types.VARCHAR));
            openMetadataRecord.put(columnNameActionTargetType, new JDBCDataValue(relatedActionTarget.getElement().getType().getTypeName(), Types.VARCHAR));
        }
        else
        {
            openMetadataRecord.put(columnNameActionTargetGUID, new JDBCDataValue(null, Types.VARCHAR));
            openMetadataRecord.put(columnNameActionTargetType, new JDBCDataValue(null, Types.VARCHAR));
        }

        openMetadataRecord.put(columnNameActionRequestName, new JDBCDataValue(propertyHelper.getStringProperty(connectorName,
                                                                                                           OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                                                           requestForActionAnnotation.getElementProperties(),
                                                                                                           methodName),
                                                                              Types.VARCHAR));
        return openMetadataRecord;
    }


    /**
     * Add details of the missing reference data to the database.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param actionTargetElement  details about the file that contains the missing reference data
     */
    private void processFileClassifiers(String                       surveyReportGUID,
                                        RelatedMetadataElement       actionTargetElement) throws UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 ConnectionCheckedException,
                                                                                                 ConnectorCheckedException
    {
        final String methodName = "processFileClassifier";

        Connector connector = myContext.getConnectorFactoryService().getConnectorForAsset(actionTargetElement.getElement().getElementGUID());

        if (connector instanceof CSVFileStoreConnector csvFileStoreConnector)
        {
            Map<String, String> measurementValues = new HashMap<>();

            for (int recordNumber = 0; recordNumber < csvFileStoreConnector.getRecordCount(); recordNumber ++)
            {
                List<String> recordValues = csvFileStoreConnector.readRecord(recordNumber);

                if ((recordValues != null) && (recordValues.size() > 1))
                {
                    syncFileClassifiers(surveyReportGUID,
                                        recordValues.get(0),
                                        recordValues.get(1),
                                        recordValues.get(2),
                                        recordValues.get(3),
                                        recordValues.get(4),
                                        recordValues.get(5),
                                        recordValues.get(6));
                }
            }
        }
    }


    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param fileName  details about the file that contains the missing reference data
     * @param fileExtension  details about the file that contains the missing reference data
     * @param pathName  details about the file that contains the missing reference data
     * @param fileType  details about the file that contains the missing reference data
     * @param assetTypeName  details about the file that contains the missing reference data
     * @param deployedImplementationType  details about the file that contains the missing reference data
     * @param encoding  details about the file that contains the missing reference data
     */
    private void syncFileClassifiers(String surveyReportGUID,
                                     String fileName,
                                     String fileExtension,
                                     String pathName,
                                     String fileType,
                                     String assetTypeName,
                                     String deployedImplementationType,
                                     String encoding)
    {
        final String methodName = "syncFileClassifiers";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        missingFileClassifiersDatabaseTable,
                                                                                        columnNamePathname,
                                                                                        null,
                                                                                        columnNameSyncTime,
                                                                                        toDoTableColumns);

            Map<String, JDBCDataValue> openMetadataRecord = this.getFileClassifierDataValues(surveyReportGUID,
                                                                                             fileName,
                                                                                             fileExtension,
                                                                                             pathName,
                                                                                             fileType,
                                                                                             assetTypeName,
                                                                                             deployedImplementationType,
                                                                                             encoding);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, columnNameSyncTime))
            {
                databaseClient.insertRowIntoTable(databaseConnection, toDoDatabaseTable, openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param fileName  details about the file that contains the missing reference data
     * @param fileExtension  details about the file that contains the missing reference data
     * @param pathName  details about the file that contains the missing reference data
     * @param fileType  details about the file that contains the missing reference data
     * @param assetType  details about the file that contains the missing reference data
     * @param deployedImplementationType  details about the file that contains the missing reference data
     * @param encoding  details about the file that contains the missing reference data
     * @return columns
     */
    private Map<String, JDBCDataValue> getFileClassifierDataValues(String surveyReportGUID,
                                                                   String fileName,
                                                                   String fileExtension,
                                                                   String pathName,
                                                                   String fileType,
                                                                   String assetType,
                                                                   String deployedImplementationType,
                                                                   String encoding)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSurveyReportGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameFilename, new JDBCDataValue(fileName, Types.VARCHAR));
        openMetadataRecord.put(columnNameFileExtension, new JDBCDataValue(fileExtension, Types.VARCHAR));
        openMetadataRecord.put(columnNamePathname, new JDBCDataValue(pathName, Types.VARCHAR));
        openMetadataRecord.put(columnNameFileType, new JDBCDataValue(fileType, Types.VARCHAR));
        openMetadataRecord.put(columnNameAssetType, new JDBCDataValue(assetType, Types.VARCHAR));
        openMetadataRecord.put(columnNameDeployedImplementationType, new JDBCDataValue(deployedImplementationType, Types.VARCHAR));
        openMetadataRecord.put(columnNameEncoding, new JDBCDataValue(encoding, Types.VARCHAR));
        openMetadataRecord.put(columnNameSyncTime, new JDBCDataValue(new java.sql.Timestamp(new Date().getTime()), Types.TIMESTAMP));

        return openMetadataRecord;
    }


    /**
     * Process information about a data profile annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncDataProfile(String                 surveyReportGUID,
                                 OpenMetadataElement    dataProfileAnnotation,
                                 RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "syncDataProfile";

        try
        {
            Map<String, Integer> valueCount = propertyHelper.getIntegerMapFromProperty(connectorName,
                                                                                       OpenMetadataProperty.VALUE_COUNT.name,
                                                                                       dataProfileAnnotation.getElementProperties(),
                                                                                       methodName);

            if (valueCount != null)
            {
                Map<String, String> measurementCounts = new HashMap<>();

                for (String measurementCategory : valueCount.keySet())
                {
                    measurementCounts.put(measurementCategory, measurementCounts.get(measurementCategory));
                }

                syncDataProfile(surveyReportGUID,
                                dataProfileAnnotation,
                                relatedAnnotationSubject,
                                measurementCounts);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }



    /**
     * Process information about a data profile annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataProfileAnnotation element representing the data profile annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     * @param measurementValues map of measurement categories to measurement counts
     */
    private void syncDataProfile(String                 surveyReportGUID,
                                 OpenMetadataElement    dataProfileAnnotation,
                                 RelatedMetadataElement relatedAnnotationSubject,
                                 Map<String, String>    measurementValues)
    {
        final String methodName = "syncDataProfile";

        try
        {
            String subjectGUID = null;
            String subjectType = null;

            if (relatedAnnotationSubject != null)
            {
                subjectGUID = relatedAnnotationSubject.getElement().getElementGUID();
                subjectType = relatedAnnotationSubject.getElement().getType().getTypeName();
            }

            String measurementName = propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                      dataProfileAnnotation.getElementProperties(),
                                                                      methodName);

            if (measurementValues != null)
            {
                for (String valueCountName : measurementValues.keySet())
                {
                    String measurementValue = null;

                    if (measurementValues.get(valueCountName) != null)
                    {
                        measurementValue = measurementValues.get(valueCountName);
                    }

                    Map<String, JDBCDataValue> openMetadataRecord = this.getDataProfileDataValues(surveyReportGUID,
                                                                                                  subjectGUID,
                                                                                                  subjectType,
                                                                                                  dataProfileAnnotation.getElementGUID(),
                                                                                                  dataProfileAnnotation.getVersions().getCreateTime(),
                                                                                                  measurementName,
                                                                                                  valueCountName,
                                                                                                  measurementValue);

                    databaseClient.insertRowIntoTable(databaseConnection, dataProfileDatabaseTable, openMetadataRecord);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert the description of a annotation into columns for the data profile table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param measurementName name of a specific measurement
     * @param measurementCategory name of a category within the specific measurement
     * @param measurementValue value of the category's measurement
     * @return columns
     */
    private Map<String, JDBCDataValue> getDataProfileDataValues(String                 surveyReportGUID,
                                                                String                 subjectGUID,
                                                                String                 subjectType,
                                                                String                 annotationGUID,
                                                                Date                   creationTime,
                                                                String                 measurementName,
                                                                String                 measurementCategory,
                                                                String                 measurementValue)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(subjectGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(subjectType, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(creationTime, Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(annotationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementName, new JDBCDataValue(measurementName, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementCategory, new JDBCDataValue(measurementCategory, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementValue, new JDBCDataValue(measurementValue, Types.VARCHAR));

        return openMetadataRecord;
    }


    /**
     * Process information about a data source measurements annotation.  It is just inserted into the database.
     * Any existing record is overwritten.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param dataSourceMeasurementsAnnotation element representing the data source measurements annotation
     * @param relatedAnnotationSubject element related via the associated annotation
     */
    private void syncDataSourceMeasurements(String                 surveyReportGUID,
                                            OpenMetadataElement    dataSourceMeasurementsAnnotation,
                                            RelatedMetadataElement relatedAnnotationSubject)
    {
        final String methodName = "syncDataSourceMeasurements";

        try
        {
            String subjectGUID = null;
            String subjectType = null;

            if (relatedAnnotationSubject != null)
            {
                subjectGUID = relatedAnnotationSubject.getElement().getElementGUID();
                subjectType = relatedAnnotationSubject.getElement().getType().getTypeName();
            }

            String measurementName = propertyHelper.getStringProperty(connectorName,
                                                                      OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                                      dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                      methodName);

            Map<String, Integer> valueCount = propertyHelper.getIntegerMapFromProperty(connectorName,
                                                                                       OpenMetadataProperty.DATA_SOURCE_PROPERTIES.name,
                                                                                       dataSourceMeasurementsAnnotation.getElementProperties(),
                                                                                       methodName);

            if (valueCount != null)
            {
                for (String valueCountName : valueCount.keySet())
                {
                    String measurementValue = null;

                    if (valueCount.get(valueCountName) != null)
                    {
                        measurementValue = valueCount.get(valueCountName).toString();
                    }

                    Map<String, JDBCDataValue> openMetadataRecord = this.getDataSourceMeasurementsDataValues(surveyReportGUID,
                                                                                                             subjectGUID,
                                                                                                             subjectType,
                                                                                                             dataSourceMeasurementsAnnotation.getElementGUID(),
                                                                                                             dataSourceMeasurementsAnnotation.getVersions().getCreateTime(),
                                                                                                             measurementName,
                                                                                                             valueCountName,
                                                                                                             measurementValue);

                    databaseClient.insertRowIntoTable(databaseConnection, dataSourceMeasurementsDatabaseTable, openMetadataRecord);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert the description of a annotation into columns for the data source measurements table.
     *
     * @param surveyReportGUID unique identifier of the survey report
     * @param subjectGUID unique identifier of the element that this annotation describes
     * @param subjectType unique type name of the element that this annotation describes
     * @param annotationGUID unique identifier for the annotation
     * @param creationTime time that the annotation was created
     * @param measurementName name of a specific measurement
     * @param measurementCategory name of a category within the specific measurement
     * @param measurementValue value of the category's measurement
     * @return columns
     */
    private Map<String, JDBCDataValue> getDataSourceMeasurementsDataValues(String                 surveyReportGUID,
                                                                           String                 subjectGUID,
                                                                           String                 subjectType,
                                                                           String                 annotationGUID,
                                                                           Date                   creationTime,
                                                                           String                 measurementName,
                                                                           String                 measurementCategory,
                                                                           String                 measurementValue)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameSRGUID, new JDBCDataValue(surveyReportGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectGUID, new JDBCDataValue(subjectGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameSubjectType, new JDBCDataValue(subjectType, Types.VARCHAR));
        openMetadataRecord.put(columnNameCreationTime, new JDBCDataValue(creationTime, Types.TIMESTAMP));
        openMetadataRecord.put(columnNameAnnotationGUID, new JDBCDataValue(annotationGUID, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementName, new JDBCDataValue(measurementName, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementCategory, new JDBCDataValue(measurementCategory, Types.VARCHAR));
        openMetadataRecord.put(columnNameMeasurementValue, new JDBCDataValue(measurementValue, Types.VARCHAR));

        return openMetadataRecord;
    }



    /**
     * Process information about a specific reference level.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param identifier unique identifier of the status identifier
     * @param classificationName type name of the governance action classification
     * @param displayName details of the certification type
     * @param text description of the status identifier
     */
    private void syncReferenceLevels(int    identifier,
                                     String classificationName,
                                     String displayName,
                                     String text)
    {
        final String methodName = "syncReferenceLevels";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getReferenceLevelDataValues(identifier, classificationName, displayName, text);

            databaseClient.insertRowIntoTable(databaseConnection, referenceLevelsDatabaseTable, openMetadataRecord);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

    }


    /**
     * Convert the description of a specific certification relationship into columns for the certifications table.
     *
     * @param identifier unique identifier of the status identifier
     * @param classificationName type name of the governance action classification
     * @param displayName details of the certification type
     * @param text description of the status identifier
     * @return columns
     */
    private Map<String, JDBCDataValue> getReferenceLevelDataValues(int    identifier,
                                                                   String classificationName,
                                                                   String displayName,
                                                                   String text)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        openMetadataRecord.put(columnNameIdentifier, new JDBCDataValue(identifier, Types.INTEGER));
        openMetadataRecord.put(columnNameClassificationName, new JDBCDataValue(classificationName, Types.VARCHAR));
        openMetadataRecord.put(columnNameDisplayName, new JDBCDataValue(displayName, Types.VARCHAR));
        openMetadataRecord.put(columnNameText, new JDBCDataValue(text, Types.VARCHAR));

        return openMetadataRecord;
    }



    /**
     * Concert an object into a JSON string.
     *
     * @param columnValue value to convert.
     * @return JSON string
     */
    private String formatJSONColumn(Object columnValue)
    {
        final String methodName = "formatJSONColumn";

        try
        {
            return OBJECT_WRITER.writeValueAsString(columnValue);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  LoadObservationsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Compare the two records and return true if the information from the open metadata ecosystem is
     * different from the information stored in the database.  Any discrepancy
     * found results in a return of true.
     *
     * @param latestStoredRecord record from the database
     * @param openMetadataRecord record from the open metadata ecosystem
     * @param ignoreProperty property to be skipped - typically called sync_time
     * @return boolean flag
     */
    private boolean newInformation(Map<String, JDBCDataValue> latestStoredRecord,
                                   Map<String, JDBCDataValue> openMetadataRecord,
                                   String                     ignoreProperty)
    {
        if (latestStoredRecord == null)
        {
            return true;
        }

        for (String columnName : latestStoredRecord.keySet())
        {
            /*
             * Skip the property if it is to be ignored
             */
            if ((ignoreProperty == null) || (! ignoreProperty.equals(columnName)))
            {
                if (latestStoredRecord.get(columnName) != null)
                {
                    /*
                     * Something is stored in the database - does it match?
                     */
                    JDBCDataValue latestStoredDataValue = latestStoredRecord.get(columnName);
                    JDBCDataValue openMetadataDataValue = openMetadataRecord.get(columnName);

                    if ((openMetadataDataValue == null) || (! openMetadataDataValue.equals(latestStoredDataValue)))
                    {
                        return true;
                    }
                }
                else if (openMetadataRecord.get(columnName) != null)
                {
                    /*
                     * There is no value stored in the database. Is something stored in open metadata?
                     */
                    JDBCDataValue dataValue = openMetadataRecord.get(columnName);

                    if (dataValue.getDataValue() != null)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    /**
     * Free up any connections held since the data source is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public  void disconnect() throws ConnectorCheckedException
    {
        if (databaseClient != null)
        {
            databaseClient.disconnect();
        }

        super.disconnect();
    }
}
