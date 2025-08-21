/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.ffdc.HarvestOpenMetadataAuditCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.ffdc.HarvestOpenMetadataErrorCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.schema.HarvestOpenMetadataColumn;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.schema.HarvestOpenMetadataTable;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLSchemaDDL;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.*;


/**
 * Extracts relevant metadata from the open metadata ecosystem into the JDBC database.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by the Open Metadata Store.
 */
public class HarvestOpenMetadataCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    private       JDBCResourceConnector    databaseClient = null;
    private final OpenMetadataStore        openMetadataStore;


    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @throws ConnectorCheckedException error
     * @throws UserNotAuthorizedException connector is disconnected
     */
    public HarvestOpenMetadataCatalogTargetProcessor(CatalogTarget            catalogTarget,
                                                     CatalogTargetContext     catalogTargetContext,
                                                     Connector                connectorToTarget,
                                                     String                   connectorName,
                                                     AuditLog                 auditLog) throws ConnectorCheckedException,
                                                                                               UserNotAuthorizedException
    {
        super(catalogTarget, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        this.openMetadataStore = catalogTargetContext.getOpenMetadataStore();
        this.openMetadataStore.setForLineage(true);

        if (super.getConnectorToTarget() instanceof JDBCResourceConnector jdbcResourceConnector)
        {
            this.databaseClient = jdbcResourceConnector;
            this.databaseClient.start();

            String schemaName = super.getStringConfigurationProperty(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(), catalogTarget.getConfigurationProperties());
            if (schemaName == null)
            {
                schemaName = super.getStringConfigurationProperty(PostgresPlaceholderProperty.SCHEMA_NAME.getName(), catalogTarget.getConfigurationProperties());
            }

            loadDDL(databaseClient, schemaName);
        }
    }


    /**
     * Check that the tables for the repository are defined.
     *
     * @param schemaName name of the schema
     * @throws ConnectorCheckedException problem with the DDL
     */
    private void loadDDL(JDBCResourceConnector jdbcResourceConnector,
                         String                schemaName) throws ConnectorCheckedException
    {
        final String methodName = "loadDDL";

        java.sql.Connection databaseConnection = null;

        try
        {
            databaseConnection = jdbcResourceConnector.getDataSource().getConnection();

            PostgreSQLSchemaDDL postgreSQLSchemaDDL = new PostgreSQLSchemaDDL(schemaName,
                                                                              "Observability data for a cohort of OMAG Servers.",
                                                                              HarvestOpenMetadataTable.getTables());
            jdbcResourceConnector.addDatabaseDefinitions(databaseConnection, postgreSQLSchemaDDL.getDDLStatements());
            databaseConnection.commit();
        }
        catch (Exception error)
        {
            if (databaseConnection != null)
            {
                try
                {
                    databaseConnection.rollback();
                }
                catch (Exception  rollbackError)
                {
                    auditLog.logException(methodName,
                                          HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 rollbackError.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 rollbackError.getMessage()),
                                          error);
                }
            }

            throw new ConnectorCheckedException(HarvestOpenMetadataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(schemaName,
                                                                                                                  error.getClass().getName(),
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /* ==============================================================================
     * Standard methods that trigger activity.
     */


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

        java.sql.Connection databaseConnection = null;

        try
        {
            /*
             * This is the connection to the database schema used to store the harvested metadata
             */
            databaseConnection = databaseClient.getDataSource().getConnection();

            /*
             * These clients provide access to open metadata.
             */
            AssetClient    dataAssetClient = integrationContext.getAssetClient(OpenMetadataType.DATA_ASSET.typeName);
            GlossaryClient glossaryClient = integrationContext.getGlossaryClient();

            /*
             * Step through the catalogued metadata elements for each interesting type.  Start with data assets.
             */
            SearchOptions workingSearchOptions = dataAssetClient.getSearchOptions(0, openMetadataStore.getMaxPagingSize());
            workingSearchOptions.setForLineage(true);

            List<OpenMetadataRootElement> dataAssetElements = dataAssetClient.findAssets(null, workingSearchOptions);

            while (dataAssetElements != null)
            {
                for (OpenMetadataRootElement dataAssetElement : dataAssetElements)
                {
                    processDataAsset(databaseConnection, dataAssetElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                dataAssetElements = dataAssetClient.findAssets(null, workingSearchOptions);
            }

            workingSearchOptions.setStartFrom(0);
            List<OpenMetadataRootElement> glossaryElements = glossaryClient.findGlossaries(null, workingSearchOptions);

            while (glossaryElements != null)
            {
                for (OpenMetadataRootElement glossaryElement : glossaryElements)
                {
                    processGlossary(databaseConnection, glossaryElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                glossaryElements = glossaryClient.findGlossaries(null, workingSearchOptions);
            }


            workingSearchOptions.setStartFrom(0);
            workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.TEAM.typeName);
            List<OpenMetadataElement> teamElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);

            while (teamElements != null)
            {
                for (OpenMetadataElement teamElement : teamElements)
                {
                    processTeam(databaseConnection, teamElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                teamElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);
            }


            workingSearchOptions.setStartFrom(0);
            workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.TO_DO.typeName);
            List<OpenMetadataElement> toDoElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);

            while (toDoElements != null)
            {
                for (OpenMetadataElement toDoElement : toDoElements)
                {
                    processToDo(databaseConnection, toDoElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                toDoElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);
            }


            workingSearchOptions.setStartFrom(0);
            workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.ACTOR_ROLE.typeName);
            List<OpenMetadataElement> roleElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);

            while (roleElements != null)
            {
                for (OpenMetadataElement roleElement : roleElements)
                {
                    syncRole(databaseConnection, roleElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                roleElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);
            }


            workingSearchOptions.setStartFrom(0);
            workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.USER_IDENTITY.typeName);
            List<OpenMetadataElement> userIdentityElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);

            while (userIdentityElements != null)
            {
                for (OpenMetadataElement userIdentityElement : userIdentityElements)
                {
                    processUserIdentity(databaseConnection, userIdentityElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                userIdentityElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);
            }

            workingSearchOptions.setStartFrom(0);

            OpenMetadataRelationshipList personRoleAppointments = openMetadataStore.findRelationshipsBetweenMetadataElements(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                                             null,
                                                                                                                             workingSearchOptions);

            while ((personRoleAppointments != null) && (personRoleAppointments.getElementList() != null))
            {
                for (OpenMetadataRelationship personRoleAppointment : personRoleAppointments.getElementList())
                {
                    if (personRoleAppointment != null)
                    {
                        syncRoleToProfile(databaseConnection,
                                          this.getUserIdentityForRole(personRoleAppointment.getElementGUIDAtEnd1(),
                                                                      personRoleAppointment.getElementGUIDAtEnd2()),
                                          personRoleAppointment);
                    }
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());
                personRoleAppointments = openMetadataStore.findRelationshipsBetweenMetadataElements(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                    null,
                                                                                                    workingSearchOptions);
            }

            workingSearchOptions.setStartFrom(0);
            workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.PROJECT.typeName);
            List<OpenMetadataElement> projectElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);

            while (projectElements != null)
            {
                for (OpenMetadataElement projectElement : projectElements)
                {
                    processProject(databaseConnection, projectElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                projectElements = openMetadataStore.findMetadataElements(null,
                                                                         null,
                                                                         workingSearchOptions);
            }


            workingSearchOptions.setStartFrom(0);
            workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.COMMUNITY.typeName);
            List<OpenMetadataElement> communityElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);

            while (communityElements != null)
            {
                for (OpenMetadataElement communityElement : communityElements)
                {
                    processCommunity(databaseConnection, communityElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                communityElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);
            }

            workingSearchOptions.setStartFrom(0);
            workingSearchOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            List<OpenMetadataElement> collectionElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);

            while (collectionElements != null)
            {
                for (OpenMetadataElement collectionElement : collectionElements)
                {
                    processCollection(databaseConnection, collectionElement);
                }

                workingSearchOptions.setStartFrom(workingSearchOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());

                collectionElements = openMetadataStore.findMetadataElements(null, null, workingSearchOptions);
            }

            databaseConnection.commit();
        }
        catch (Exception error)
        {
            if (databaseConnection != null)
            {
                try
                {
                    databaseConnection.rollback();
                }
                catch (Exception  closeError)
                {
                    auditLog.logException(methodName,
                                          HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                 closeError.getClass().getName(),
                                                                                                                 methodName,
                                                                                                                 closeError.getMessage()),
                                          error);
                }
            }

            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                  error);


            throw new ConnectorCheckedException(HarvestOpenMetadataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
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
            RelatedMetadataElementList profileIdentities = openMetadataStore.getRelatedMetadataElements(profileGUID,
                                                                                                        1,
                                                                                                        OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                                                        0,
                                                                                                        openMetadataStore.getMaxPagingSize());


            if ((profileIdentities != null) && (profileIdentities.getElementList() != null))
            {
                for (RelatedMetadataElement profileIdentity : profileIdentities.getElementList())
                {
                    if (profileIdentity != null)
                    {
                        String userIdentityRole = propertyHelper.getStringProperty(connectorName,
                                                                                   OpenMetadataProperty.ROLE_GUID.name,
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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * Process a data asset retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param dataAssetElement description of the asset
     */
    private void processDataAsset(java.sql.Connection     databaseConnection,
                                  OpenMetadataRootElement dataAssetElement)
    {
        final String methodName = "processDataAsset";

        try
        {
            /*
             * Retrieve elements associated directly with the asset.
             */
            String associatedLicenseGUID          = null;
            String associatedResourceLocationGUID = null;

            OpenMetadataElement associatedLocation = this.getAssociatedAssetLocation(databaseConnection, dataAssetElement.getElementHeader().getGUID());
            OpenMetadataElement associatedLicense  = this.getAssociatedLicense(databaseConnection, dataAssetElement.getElementHeader().getGUID());
            String tags                            = this.getAssociatedTags(dataAssetElement.getElementHeader().getGUID());
            String glossaryTermGUID                = this.getAssociatedMeaning(dataAssetElement.getElementHeader().getGUID());

            syncAssetType(databaseConnection, dataAssetElement.getElementHeader().getType());

            if (associatedLocation != null)
            {
                associatedResourceLocationGUID = associatedLocation.getElementGUID();
                syncLocation(databaseConnection, associatedLocation);
            }

            if (associatedLicense != null)
            {
                associatedLicenseGUID = associatedLicense.getElementGUID();
            }

            /*
             * Extract interesting information from the data asset.
             */
            syncDataAsset(databaseConnection,
                          dataAssetElement,
                          associatedResourceLocationGUID,
                          associatedLicenseGUID,
                          tags,
                          glossaryTermGUID);


            if (dataAssetElement.getProperties() instanceof AssetProperties assetProperties)
            {
                /*
                 * Find out about other associated elements
                 */
                findAssociatedElements(databaseConnection,
                                       dataAssetElement.getElementHeader(),
                                       assetProperties.getQualifiedName(),
                                       dataAssetElement.getAlsoKnownAs(),
                                       associatedLicense,
                                       true);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Retrieve details of a location that is connected to the asset via the AssetLocation relationship.
     *
     * @param databaseConnection connection to the database
     * @param assetGUID unique identifier of the asset
     * @return the location (or null if there is not one)
     */
    private OpenMetadataElement getAssociatedAssetLocation(java.sql.Connection databaseConnection,
                                                           String              assetGUID)
    {
        final String methodName = "getAssociatedAssetLocation";

        try
        {
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(assetGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.KNOWN_LOCATION_RELATIONSHIP.typeName,
                                                                                                      0,
                                                                                                      openMetadataStore.getMaxPagingSize());

            /*
             * Return the first location retrieved.
             */
            if ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        // todo it is possible to enhance this logic to give precedence to a location that has the FixedLocation classification.
                        this.syncLocation(databaseConnection, relatedMetadataElement.getElement());
                        return relatedMetadataElement.getElement();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to the database
     * @param profileGUID unique identifier of the profile
     * @return unique identifier of the location (or null if there is not one)
     */
    private String getAssociatedProfileLocation(java.sql.Connection databaseConnection,
                                                String              profileGUID)
    {
        final String methodName = "getAssociatedProfileLocation";

        try
        {
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(profileGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.KNOWN_LOCATION_RELATIONSHIP.typeName,
                                                                                                      0,
                                                                                                      openMetadataStore.getMaxPagingSize());

            /*
             * Return the first location retrieved.
             */
            if ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        // todo it is possible to enhance this logic to give precedence to a location that has the FixedLocation classification.
                        this.syncLocation(databaseConnection, relatedMetadataElement.getElement());
                        return relatedMetadataElement.getElement().getElementGUID();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to the database
     * @param elementGUID unique identifier of the asset
     * @return  the location (or null if there is not one)
     */
    private OpenMetadataElement getAssociatedLicense(java.sql.Connection databaseConnection,
                                                     String              elementGUID)
    {
        final String methodName = "getAssociatedLicense";

        try
        {
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                                                                      0,
                                                                                                      openMetadataStore.getMaxPagingSize());

            /*
             * Return the license retrieved.
             */
            if ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        // todo it is possible to enhance this logic to give precedence to a license that is currently active.
                        this.syncLicense(databaseConnection, relatedMetadataElement.getElement());
                        return relatedMetadataElement.getElement();
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(assetGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                                                      startFrom,
                                                                                                      openMetadataStore.getMaxPagingSize());

            /*
             * Return all attached tags.
             */
            while ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        String tagName = propertyHelper.getStringProperty(connectorName,
                                                                          OpenMetadataProperty.DISPLAY_NAME.name,
                                                                          relatedMetadataElement.getElement().getElementProperties(),
                                                                          methodName);
                        tagStringBuilder.append(tagName).append(":");
                    }
                }

                startFrom = startFrom + openMetadataStore.getMaxPagingSize();
                relatedElements = openMetadataStore.getRelatedMetadataElements(assetGUID,
                                                                               1,
                                                                               OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                               startFrom,
                                                                               openMetadataStore.getMaxPagingSize());

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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                                      0,
                                                                                                      openMetadataStore.getMaxPagingSize());

            /*
             * Return the first glossary term retrieved.
             */
            if ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to the database
     * @param elementHeader unique identifier of the element plus other information from the
     * @param elementQualifiedName unique name of the element
     * @param correlationHeaders information about external collections
     * @param license associated license
     * @param isAsset is this element an asset
     */
    private void findAssociatedElements(java.sql.Connection                 databaseConnection,
                                        ElementHeader                       elementHeader,
                                        String                              elementQualifiedName,
                                        List<RelatedMetadataElementSummary> correlationHeaders,
                                        OpenMetadataElement                 license,
                                        boolean                             isAsset)
    {
        final String methodName = "findAssociatedElements";

        try
        {
            processMetadataCollection(databaseConnection, elementHeader.getOrigin());
            syncCorrelationProperties(databaseConnection, elementHeader, correlationHeaders);
            processUserIds(databaseConnection, elementHeader.getVersions(), elementHeader.getOrigin());

            if (license != null)
            {
                syncLicense(databaseConnection, license);
            }

            int numberOfComments = 0;
            int numberOfRatings = 0;
            int totalStars = 0;
            int numberOfTags = 0;
            int numberOfLikes = 0;

            int startFrom = 0;
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(elementHeader.getGUID(),
                                                                                                      1,
                                                                                                      null,
                                                                                                      startFrom,
                                                                                                      openMetadataStore.getMaxPagingSize());

            while ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
                {
                    String relationshipType = relatedMetadataElement.getType().getTypeName();

                    if (OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName.equals(relationshipType))
                    {
                        numberOfTags++;
                    }
                    else if (OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName.equals(relationshipType))
                    {
                        numberOfLikes++;
                    }
                    else if (OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName.equals(relationshipType))
                    {
                        numberOfRatings++;
                        totalStars = totalStars + countStars(relatedMetadataElement.getElement().getElementProperties().getPropertyValue("stars"));
                    }
                    else if (OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName.equals(relationshipType))
                    {
                        numberOfComments = numberOfComments + 1 + countAttachedComments(relatedMetadataElement.getElement().getElementGUID());
                    }
                    else if (OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName.equals(relationshipType))
                    {
                        syncCertification(databaseConnection, elementHeader.getGUID(), relatedMetadataElement);
                    }
                    else if (OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName.equals(relationshipType))
                    {
                        processAssetSchemaType(databaseConnection, elementHeader.getGUID(), elementQualifiedName, relatedMetadataElement);
                    }

                    if ((isAsset) && propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.ASSET.typeName))
                    {
                        syncRelatedAsset(databaseConnection, openMetadataStore.getRelationshipByGUID(relatedMetadataElement.getRelationshipGUID()));
                    }
                }

                startFrom = startFrom + openMetadataStore.getMaxPagingSize();
                relatedElements = openMetadataStore.getRelatedMetadataElements(elementHeader.getGUID(),
                                                                               1,
                                                                               null,
                                                                               startFrom,
                                                                               openMetadataStore.getMaxPagingSize());
            }

            int averageRatings = 0;

            if ((numberOfRatings > 0) && (totalStars > 0))
            {
                averageRatings = totalStars / numberOfRatings;
            }

            syncCollaborationActivity(databaseConnection, elementHeader.getGUID(), elementHeader.getType().getTypeName(), numberOfComments, numberOfRatings, averageRatings, numberOfTags, numberOfLikes);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
            RelatedMetadataElementList relatedElements = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                                                      1,
                                                                                                      "AttachedComment",
                                                                                                      startFrom,
                                                                                                      openMetadataStore.getMaxPagingSize());

            /*
             * Count all nested comments.
             */
            while ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        commentCount = commentCount + 1 + this.countAttachedComments(relatedMetadataElement.getElement().getElementGUID());
                    }
                }

                startFrom = startFrom + openMetadataStore.getMaxPagingSize();
                relatedElements = openMetadataStore.getRelatedMetadataElements(elementGUID,
                                                                               1,
                                                                               "AttachedComment",
                                                                               startFrom,
                                                                               openMetadataStore.getMaxPagingSize());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to the database
     * @param elementOrigin origin from an element's header
     */
    private void processMetadataCollection(java.sql.Connection databaseConnection,
                                           ElementOrigin       elementOrigin)
    {
        final  String methodName = "processMetadataCollection";

        if (elementOrigin != null)
        {
            String deployedImplementationType = null;

            if (elementOrigin.getOriginCategory() == ElementOriginCategory.EXTERNAL_SOURCE)
            {
                try
                {
                    OpenMetadataElement softwareCapability = openMetadataStore.getMetadataElementByGUID(elementOrigin.getHomeMetadataCollectionId());

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

            syncMetadataCollection(databaseConnection, elementOrigin, deployedImplementationType);
        }
    }



    /**
     * Extract information about a catalogued team or organization.
     *
     * @param databaseConnection connection to the database
     * @param teamElement retrieved element
     */
    private void processTeam(java.sql.Connection databaseConnection,
                             OpenMetadataElement teamElement)
    {
        if (teamElement != null)
        {
            String parentDepartmentGUID = null;
            String managerGUID          = getTeamManagerGUID(teamElement.getElementGUID());

            OpenMetadataElement parentDepartment = this.getAssociatedParentTeam(teamElement.getElementGUID());

            if (parentDepartment != null)
            {
                parentDepartmentGUID = parentDepartment.getElementGUID();
            }


            syncDepartment(databaseConnection, parentDepartmentGUID, managerGUID, teamElement);
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
            // todo - work out leadership assignmentTypes
            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(teamGUID,
                                                                                                              2,
                                                                                                              OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                                              0,
                                                                                                              openMetadataStore.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement personRoleElement : relatedMetadataElements.getElementList())
                {
                    if (personRoleElement != null)
                    {
                        RelatedMetadataElementList appointees = openMetadataStore.getRelatedMetadataElements(teamGUID,
                                                                                                             2,
                                                                                                             OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                             0,
                                                                                                             openMetadataStore.getMaxPagingSize());
                        if ((appointees != null) && (appointees.getElementList() != null))
                        {
                            for (RelatedMetadataElement appointee : appointees.getElementList())
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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return null;
    }


    /**
     * Extract information about a catalogued user identity.
     *
     * @param databaseConnection connection to the database
     * @param userIdentityElement retrieved element
     */
    private void processUserIdentity(java.sql.Connection databaseConnection,
                                     OpenMetadataElement userIdentityElement)
    {
        final String methodName = "processUserIdentity";

        try
        {
            syncUserIdentity(databaseConnection, userIdentityElement);

            RelatedMetadataElement profileElement = null;

            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(userIdentityElement.getElementGUID(),
                                                                                                              2,
                                                                                                              OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                                                              0,
                                                                                                              openMetadataStore.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
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
                syncUserToProfile(databaseConnection, userIdentityElement, profileElement);
                syncActorProfile(databaseConnection, profileElement);

                if (propertyHelper.isTypeOf(profileElement, OpenMetadataType.PERSON.typeName))
                {
                    String organizationName = null;
                    String locationGUID = getAssociatedProfileLocation(databaseConnection, profileElement.getElement().getElementGUID());
                    String departmentId = getAssociatedDepartment(profileElement.getElement().getElementGUID());

                    if (departmentId != null)
                    {
                        organizationName = getAssociatedOrganizationName(departmentId);
                    }

                    syncPerson(databaseConnection,
                               profileElement,
                               departmentId,
                               locationGUID,
                               organizationName);
                }

                RelatedMetadataElement contributionRecord = null;

                relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(profileElement.getElement().getElementGUID(),
                                                                                       1,
                                                                                       OpenMetadataType.CONTRIBUTION_RELATIONSHIP.typeName,
                                                                                       0,
                                                                                       openMetadataStore.getMaxPagingSize());

                if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
                {
                    for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                    {
                        if (relatedMetadataElement != null)
                        {
                            contributionRecord = relatedMetadataElement;
                            break;
                        }
                    }
                }

                if (contributionRecord != null)
                {
                    syncContribution(databaseConnection,
                                     profileElement.getElement().getElementGUID(),
                                     profileElement.getElement().getType().getTypeName(),
                                     contributionRecord);
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued project.
     *
     * @param databaseConnection connection to the database
     * @param projectElement retrieved element
     */
    private void processProject(java.sql.Connection databaseConnection,
                                OpenMetadataElement projectElement)
    {
        final String methodName = "processProject";

        try
        {
            RelatedMetadataElement parentProjectElement = null;

            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(projectElement.getElementGUID(),
                                                                                                              2,
                                                                                                              OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                                                              0,
                                                                                                              openMetadataStore.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        parentProjectElement = relatedMetadataElement;
                        break;
                    }
                }
            }

            syncProject(databaseConnection, projectElement, parentProjectElement);

            relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(projectElement.getElementGUID(),
                                                                                   2,
                                                                                   OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                   0,
                                                                                   openMetadataStore.getMaxPagingSize());


            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                syncActorAssignments(databaseConnection, projectElement, relatedMetadataElements.getElementList());
            }

            relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(projectElement.getElementGUID(),
                                                                                   2,
                                                                                   OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                   0,
                                                                                   openMetadataStore.getMaxPagingSize());


            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                syncActorAssignments(databaseConnection, projectElement, relatedMetadataElements.getElementList());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued community.
     *
     * @param databaseConnection connection to the database
     * @param communityElement retrieved element
     */
    private void processCommunity(java.sql.Connection databaseConnection,
                                  OpenMetadataElement communityElement)
    {
        final String methodName = "processCommunity";

        try
        {
            syncCommunity(databaseConnection, communityElement);

            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(communityElement.getElementGUID(),
                                                                                                              2,
                                                                                                              OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                                              0,
                                                                                                              openMetadataStore.getMaxPagingSize());


            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                syncActorAssignments(databaseConnection, communityElement, relatedMetadataElements.getElementList());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()),
                                  error);
        }
    }



    /**
     * Extract information about a catalogued collection.
     *
     * @param databaseConnection connection to the database
     * @param collectionElement retrieved element
     */
    private void processCollection(java.sql.Connection databaseConnection,
                                   OpenMetadataElement collectionElement)
    {
        final String methodName = "processCollection";

        try
        {
            RelatedMetadataElement parentProjectElement = null;

            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(collectionElement.getElementGUID(),
                                                                                                              2,
                                                                                                              OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                              0,
                                                                                                              openMetadataStore.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        parentProjectElement = relatedMetadataElement;
                        break;
                    }
                }
            }

            long numberOfMembers = 0;
            Set<String> memberTypeSet = new HashSet<>();
            String memberTypes = null;

            int startFrom = 0;
            relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(collectionElement.getElementGUID(),
                                                                                   1,
                                                                                   OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                   startFrom,
                                                                                   openMetadataStore.getMaxPagingSize());

            while ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement memberElement : relatedMetadataElements.getElementList())
                {
                    if (memberElement != null)
                    {
                        numberOfMembers++;
                        memberTypeSet.add(memberElement.getElement().getType().getTypeName());
                    }
                }

                startFrom = startFrom + openMetadataStore.getMaxPagingSize();

                relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(collectionElement.getElementGUID(),
                                                                                       1,
                                                                                       OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                       startFrom,
                                                                                       openMetadataStore.getMaxPagingSize());
            }

            if (! memberTypeSet.isEmpty())
            {
                StringBuilder typeString = new StringBuilder();
                boolean       firstElement = true;

                for (String typeName : memberTypeSet)
                {
                    if (firstElement)
                    {
                        firstElement = false;
                    }
                    else
                    {
                        typeString.append(":");
                    }

                    typeString.append(typeName);
                }

                memberTypes = typeString.toString();
            }

            syncCollection(databaseConnection, collectionElement, parentProjectElement, numberOfMembers, memberTypes);

            AttachedClassification classification = propertyHelper.getClassification(collectionElement, OpenMetadataType.DIGITAL_PRODUCT.typeName);

            if (classification != null)
            {
                syncDigitalProduct(databaseConnection, collectionElement, classification.getClassificationProperties());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()),
                                  error);
        }
    }


    /**
     * Extract information about a catalogued to do.
     *
     * @param databaseConnection  connection to the database
     * @param toDoElement retrieved element
     */
    private void processToDo(java.sql.Connection databaseConnection,
                             OpenMetadataElement toDoElement)
    {
        final String methodName = "processToDo";

        try
        {
            RelatedMetadataElement toDoSourceElement = null;
            RelatedMetadataElement sponsorElement = null;

            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(toDoElement.getElementGUID(),
                                                                                                              2,
                                                                                                              OpenMetadataType.ACTION_REQUESTER_RELATIONSHIP.typeName,
                                                                                                              0,
                                                                                                              openMetadataStore.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        toDoSourceElement = relatedMetadataElement;
                        break;
                    }
                }
            }

            relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(toDoElement.getElementGUID(),
                                                                                   2,
                                                                                   OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                   0,
                                                                                   openMetadataStore.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        sponsorElement = relatedMetadataElement;
                        break;
                    }
                }
            }

            syncToDo(databaseConnection, toDoElement, toDoSourceElement, sponsorElement);

            relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(toDoElement.getElementGUID(),
                                                                                   2,
                                                                                   OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                   0,
                                                                                   openMetadataStore.getMaxPagingSize());


            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                syncActorAssignments(databaseConnection, toDoElement, relatedMetadataElements.getElementList());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
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
            RelatedMetadataElementList roleElements = openMetadataStore.getRelatedMetadataElements(profileGUID,
                                                                                                   1,
                                                                                                   OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                   0,
                                                                                                   openMetadataStore.getMaxPagingSize());

            if ((roleElements != null) && (roleElements.getElementList() != null))
            {
                List<RelatedMetadataElement> teams = new ArrayList<>();

                for (RelatedMetadataElement roleElement : roleElements.getElementList())
                {
                    if (roleElement != null)
                    {
                        RelatedMetadataElementList teamElements = openMetadataStore.getRelatedMetadataElements(roleElement.getElement().getElementGUID(),
                                                                                                               1,
                                                                                                               OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                                                               0,
                                                                                                               openMetadataStore.getMaxPagingSize());
                        if ((teamElements != null) && (teamElements.getElementList() != null))
                        {
                            teams.addAll(teamElements.getElementList());
                        }

                        for (RelatedMetadataElement team : teams)
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
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(departmentGUID,
                                                                                                              2,
                                                                                                              OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                                                                              0,
                                                                                                              openMetadataStore.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
                                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                                superTeam.getElementProperties(),
                                                                methodName);
            superTeam = this.getAssociatedParentTeam(superTeam.getElementGUID());
        }

        return organizationName;
    }


    /**
     * Navigate to the schema attributes to find the data fields.
     *
     * @param databaseConnection connection to the database
     * @param assetGUID unique identifier for the anchoring asset
     * @param assetQualifiedName unique identifier for the anchoring asset
     * @param schemaType details of the related schema type
     */
    private void processAssetSchemaType(java.sql.Connection    databaseConnection,
                                        String                 assetGUID,
                                        String                 assetQualifiedName,
                                        RelatedMetadataElement schemaType)
    {
        final String methodName = "processAssetSchemaType";

        try
        {

            int startFrom = 0;

            /*
             * Start by processing related schema types and schema attributes - this is where we have constructs such as .
             */
            RelatedMetadataElementList relatedMetadataElementList = openMetadataStore.getRelatedMetadataElements(schemaType.getElement().getElementGUID(),
                                                                                                                 1,
                                                                                                                 null,
                                                                                                                 startFrom,
                                                                                                                 openMetadataStore.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    /*
                     * Skip Schema Type Choice
                     */
                    if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.SCHEMA_TYPE.typeName))
                    {
                        processAssetSchemaType(databaseConnection, assetGUID, assetQualifiedName, relatedMetadataElement);
                    }
                }


                startFrom = startFrom + openMetadataStore.getMaxPagingSize();
                relatedMetadataElementList = openMetadataStore.getRelatedMetadataElements(schemaType.getElement().getElementGUID(),
                                                                                          1,
                                                                                          null,
                                                                                          startFrom,
                                                                                          openMetadataStore.getMaxPagingSize());
            }

            /*
             * Now process schema attributes.
             */
            SchemaAttributeClient schemaAttributeClient = integrationContext.getSchemaAttributeClient();
            QueryOptions workingQueryOptions = schemaAttributeClient.getQueryOptions(0, schemaAttributeClient.getMaxPagingSize());
            List<OpenMetadataRootElement> schemaAttributes = integrationContext.getSchemaAttributeClient().getAttributesForSchemaType(schemaType.getElement().getElementGUID(),
                                                                                                         workingQueryOptions);

            while (schemaAttributes != null)
            {
                processSchemaAttributes(databaseConnection, assetGUID, assetQualifiedName, schemaAttributes);

                workingQueryOptions.setStartFrom(workingQueryOptions.getStartFrom() + openMetadataStore.getMaxPagingSize());
                schemaAttributes = schemaAttributeClient.getAttributesForSchemaType(schemaType.getElement().getElementGUID(),
                                                                                    workingQueryOptions);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Navigate to the leaf nodes to find the data fields.
     *
     * @param databaseConnection connection to the database
     * @param assetGUID unique identifier for the anchoring asset
     * @param assetQualifiedName unique identifier for the anchoring asset
     * @param schemaType details of the related schema type
     */
    private void processNestedSchemaAttribute(java.sql.Connection    databaseConnection,
                                              String                 assetGUID,
                                              String                 assetQualifiedName,
                                              RelatedMetadataElement schemaType)
    {
        final String methodName = "processNestedSchemaAttribute";

        // todo need to extend to support APIs and relational tables etc
        try
        {
            SchemaAttributeClient schemaAttributeClient = integrationContext.getSchemaAttributeClient();

            int startFrom = 0;
            List<OpenMetadataRootElement> schemaAttributes = schemaAttributeClient.getNestedSchemaAttributes(schemaType.getElement().getElementGUID(),
                                                                                                             openMetadataStore.getQueryOptions());

            while (schemaAttributes != null)
            {
                processSchemaAttributes(databaseConnection, assetGUID, assetQualifiedName, schemaAttributes);

                startFrom = startFrom + openMetadataStore.getMaxPagingSize();
                schemaAttributes = schemaAttributeClient.getNestedSchemaAttributes(schemaType.getElement().getElementGUID(),
                                                                                   openMetadataStore.getQueryOptions());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Process a list of schema attributes associated with an asset.  It is seeking the leaf nodes of the schema.
     *
     * @param databaseConnection connection to the database
     * @param assetGUID unique identifier for the anchoring asset
     * @param assetQualifiedName unique identifier for the anchoring asset
     * @param schemaAttributes retrieved schema attributes
     */
    private void processSchemaAttributes(java.sql.Connection           databaseConnection,
                                         String                        assetGUID,
                                         String                        assetQualifiedName,
                                         List<OpenMetadataRootElement> schemaAttributes)
    {
        final String methodName = "processSchemaAttributes";

        try
        {
            SchemaAttributeClient schemaAttributeClient = integrationContext.getSchemaAttributeClient();

            for (OpenMetadataRootElement schemaAttribute : schemaAttributes)
            {
                int nestedStartFrom = 0;
                List<OpenMetadataRootElement> nestedSchemaAttributes = schemaAttributeClient.getNestedSchemaAttributes(schemaAttribute.getElementHeader().getGUID(),
                                                                                                                      schemaAttributeClient.getQueryOptions(nestedStartFrom, openMetadataStore.getMaxPagingSize()));
                if (nestedSchemaAttributes == null)
                {
                    /*
                     * Only write details of the leaf nodes
                     */
                    syncDataField(databaseConnection,
                                  assetGUID,
                                  assetQualifiedName,
                                  getAssociatedMeaning(schemaAttribute.getElementHeader().getGUID()),
                                  hasProfile(schemaAttribute.getElementHeader().getGUID()),
                                  schemaAttribute);

                    if (schemaAttribute.getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
                    {
                        findAssociatedElements(databaseConnection,
                                               schemaAttribute.getElementHeader(),
                                               schemaAttributeProperties.getQualifiedName(),
                                               schemaAttribute.getAlsoKnownAs(),
                                               null,
                                               false);
                    }
                }
                else
                {
                    while (nestedSchemaAttributes != null)
                    {
                        nestedStartFrom = nestedStartFrom + openMetadataStore.getMaxPagingSize();
                        nestedSchemaAttributes = schemaAttributeClient.getNestedSchemaAttributes(schemaAttribute.getElementHeader().getGUID(),
                                                                                                 schemaAttributeClient.getQueryOptions(nestedStartFrom, openMetadataStore.getMaxPagingSize()));

                        if (nestedSchemaAttributes != null)
                        {
                            processSchemaAttributes(databaseConnection, assetGUID, assetQualifiedName, nestedSchemaAttributes);
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
            RelatedMetadataElementList reports = openMetadataStore.getRelatedMetadataElements(dataFieldGUID,
                                                                                              1,
                                                                                              OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                              0,
                                                                                              1);

            return (reports != null);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        return false;
    }


    /**
     * Add information to the glossary table.
     *
     * @param databaseConnection connection to the database
     * @param glossaryElement glossary information
     */
    private void processGlossary(java.sql.Connection     databaseConnection,
                                 OpenMetadataRootElement glossaryElement)
    {
        final String methodName = "processGlossary";

        try
        {
            GlossaryTermClient glossaryTermClient = integrationContext.getGlossaryTermClient();

            OpenMetadataElement associatedLicense = getAssociatedLicense(databaseConnection, glossaryElement.getElementHeader().getGUID());

            int numberOfTerms = 0;
            int numberOfCategories = 0;
            int numberOfLinkedTerms = 0;

            if (glossaryElement.getCollectionMembers() != null)
            {
                for (RelatedMetadataElementSummary glossaryMember : glossaryElement.getCollectionMembers())
                {
                    if (glossaryMember != null)
                    {
                        if (propertyHelper.isTypeOf(glossaryMember.getRelatedElement().getElementHeader(), OpenMetadataType.GLOSSARY_TERM.typeName))
                        {
                            numberOfTerms = numberOfLinkedTerms++;

                            OpenMetadataRootElement glossaryTerm = glossaryTermClient.getGlossaryTermByGUID(glossaryMember.getRelatedElement().getElementHeader().getGUID(), new GetOptions());

                            if (glossaryTerm.getRelatedTerms() != null)
                            {
                                numberOfLinkedTerms = numberOfLinkedTerms + glossaryTerm.getRelatedTerms().size();
                            }

                            if (glossaryTerm.getMemberOfCollections() != null)
                            {
                                // todo - does this make sense?
                                numberOfCategories = numberOfCategories + glossaryTerm.getMemberOfCollections().size();
                            }

                            processGlossaryTerm(databaseConnection, glossaryElement.getElementHeader().getGUID(), glossaryTerm);
                        }
                    }
                }
            }

            syncGlossary(databaseConnection, glossaryElement, numberOfTerms, numberOfCategories, numberOfLinkedTerms, associatedLicense);

            if (glossaryElement.getProperties() instanceof GlossaryProperties glossaryProperties)
            {
                findAssociatedElements(databaseConnection,
                                       glossaryElement.getElementHeader(),
                                       glossaryProperties.getQualifiedName(),
                                       glossaryElement.getAlsoKnownAs(),
                                       associatedLicense,
                                       false);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Update the term_activity table with details of a new term.
     *
     * @param databaseConnection connection to the database
     * @param glossaryGUID unique identifier of the owning glossary
     * @param glossaryTerm term to add to the table
     */
    private void processGlossaryTerm(java.sql.Connection     databaseConnection,
                                     String                  glossaryGUID,
                                     OpenMetadataRootElement glossaryTerm)
    {
        final String methodName = "processGlossaryTerm";

        Date lastFeedbackTime       = null;
        int  numberOfLinkedElements = 0;
        Date lastLinkTime           = null;

        try
        {
            int startFrom = 0;
            RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(glossaryTerm.getElementHeader().getGUID(),
                                                                                                              0,
                                                                                                              null,
                                                                                                              startFrom,
                                                                                                              openMetadataStore.getMaxPagingSize());

            while ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
                {
                    String relationshipName = relatedMetadataElement.getType().getTypeName();

                    if ((relationshipName.equals(OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName)) ||
                            (relationshipName.equals(OpenMetadataType.ATTACHED_COMMENT_RELATIONSHIP.typeName)) ||
                            (relationshipName.equals(OpenMetadataType.ATTACHED_LIKE_RELATIONSHIP.typeName)) ||
                            (relationshipName.equals(OpenMetadataType.ATTACHED_RATING_RELATIONSHIP.typeName)))
                    {
                        if ((lastFeedbackTime == null) || (relatedMetadataElement.getVersions().getCreateTime().after(lastFeedbackTime)))
                        {
                            lastFeedbackTime = relatedMetadataElement.getVersions().getCreateTime();
                        }
                    }
                    else if (relationshipName.equals(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName))
                    {
                        numberOfLinkedElements = numberOfLinkedElements + 1;
                        if ((lastLinkTime == null) || (relatedMetadataElement.getVersions().getCreateTime().after(lastLinkTime)))
                        {
                            lastLinkTime = relatedMetadataElement.getVersions().getCreateTime();
                        }
                    }
                }

                startFrom = startFrom + openMetadataStore.getMaxPagingSize();
                relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(glossaryTerm.getElementHeader().getGUID(),
                                                                                       0,
                                                                                       null,
                                                                                       startFrom,
                                                                                       openMetadataStore.getMaxPagingSize());
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }

        syncTermActivity(databaseConnection, glossaryTerm, glossaryGUID, lastFeedbackTime, numberOfLinkedElements, lastLinkTime);
    }


    /**
     * Process a data asset retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param dataAssetElement description of the asset
     * @param associatedResourceLocationGUID unique identifier of the location where the associated resource is located
     * @param associatedLicenceGUID unique identifier of the license for this asset/resource
     * @param tags colon separated list of tags
     * @param semanticAssignmentTermGUID unique identifier of term that is linked to element with a semantic assignment
     */
    private void syncDataAsset(java.sql.Connection     databaseConnection,
                               OpenMetadataRootElement dataAssetElement,
                               String                  associatedResourceLocationGUID,
                               String                  associatedLicenceGUID,
                               String                  tags,
                               String                  semanticAssignmentTermGUID)
    {
        final String methodName = "syncDataAsset";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        HarvestOpenMetadataTable.ASSET.getTableName(),
                                                                                        HarvestOpenMetadataColumn.ASSET_GUID.getColumnName(),
                                                                                        dataAssetElement.getElementHeader().getGUID(),
                                                                                        HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                        HarvestOpenMetadataTable.ASSET.getColumnNameTypeMap());

            Map<String, JDBCDataValue> openMetadataRecord = this.getAssetDataValues(databaseConnection,
                                                                                    dataAssetElement,
                                                                                    associatedResourceLocationGUID,
                                                                                    associatedLicenceGUID,
                                                                                    tags,
                                                                                    semanticAssignmentTermGUID);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
            {
                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.ASSET.getTableName(), openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to the database
     * @param dataAssetElement data asset retrieved from the open metadata ecosystem
     * @param associatedResourceLocationGUID unique identifier of the location where the associated resource is located
     * @param associatedLicenceGUID unique identifier of the license for this asset/resource
     * @param tags colon separated list of tags
     * @param semanticAssignmentTermGUID unique identifier of term that is linked to element with a semantic assignment
     * @return columns
     */
    private Map<String, JDBCDataValue> getAssetDataValues(java.sql.Connection     databaseConnection,
                                                          OpenMetadataRootElement dataAssetElement,
                                                          String                  associatedResourceLocationGUID,
                                                          String                  associatedLicenceGUID,
                                                          String                  tags,
                                                          String                  semanticAssignmentTermGUID)
    {
        if (dataAssetElement != null)
        {
            Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

            ElementHeader       elementHeader       = dataAssetElement.getElementHeader();

            if (dataAssetElement.getProperties() instanceof DataAssetProperties dataAssetProperties)
            {
                /*
                 * Extract classifications
                 */
                if (elementHeader != null)
                {
                    processUserIds(databaseConnection, elementHeader.getVersions(), elementHeader.getOrigin());

                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSET_GUID, elementHeader.getGUID());
                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, elementHeader.getType().getTypeName());
                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.METADATA_COLLECTION_ID, elementHeader.getOrigin().getHomeMetadataCollectionId());

                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_BY, elementHeader.getVersions().getCreatedBy());
                    addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, elementHeader.getVersions().getCreateTime());

                    if (elementHeader.getVersions().getUpdateTime() != null)
                    {
                        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, elementHeader.getVersions().getUpdateTime());
                    }
                    else
                    {
                        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, elementHeader.getVersions().getCreateTime());
                    }
                    if (elementHeader.getVersions().getUpdatedBy() != null)
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATED_BY, elementHeader.getVersions().getUpdatedBy());
                    }
                    else
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATED_BY, elementHeader.getVersions().getCreatedBy());
                    }

                    if ((elementHeader.getVersions().getMaintainedBy() != null) && (!elementHeader.getVersions().getMaintainedBy().isEmpty()))
                    {
                        StringBuilder userString = new StringBuilder(":");

                        for (String user : elementHeader.getVersions().getMaintainedBy())
                        {
                            userString.append(user).append(":");
                        }

                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.MAINTAINED_BY, userString.toString());
                    }
                    else
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.MAINTAINED_BY, ":" + elementHeader.getVersions().getCreatedBy() + ":");
                    }

                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATED_BY, elementHeader.getVersions().getCreatedBy());

                    if (elementHeader.getZoneMembership() != null)
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ZONE_NAMES, this.getZoneNames(elementHeader.getZoneMembership().getClassificationProperties()));
                    }
                    if (elementHeader.getOwnership() != null)
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_GUID, this.getOpenMetadataStringProperty(elementHeader.getOwnership().getClassificationProperties(), OpenMetadataProperty.OWNER.name, 80));
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_TYPE_NAME, this.getOpenMetadataStringProperty(elementHeader.getOwnership().getClassificationProperties(), OpenMetadataProperty.OWNER_TYPE_NAME.name, 40));
                    }
                    if (elementHeader.getDigitalResourceOrigin() != null)
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ORIGIN_ORG_GUID, this.getOpenMetadataStringProperty(elementHeader.getDigitalResourceOrigin().getClassificationProperties(), OpenMetadataProperty.ORGANIZATION.name, 80));
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ORIGIN_BIZ_CAP_GUID, this.getOpenMetadataStringProperty(elementHeader.getDigitalResourceOrigin().getClassificationProperties(), "businessCapability", 80));
                    }
                    if (elementHeader.getMemento() != null)
                    {
                        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ARCHIVED, elementHeader.getMemento().getVersions().getCreateTime());
                    }
                    if (elementHeader.getConfidentiality() != null)
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL, this.getStatusIdentifier(elementHeader.getConfidentiality().getClassificationProperties()));
                    }
                    if (elementHeader.getConfidence() != null)
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENCE_LEVEL, this.getStatusIdentifier(elementHeader.getConfidence().getClassificationProperties()));
                    }
                    if (elementHeader.getCriticality() != null)
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CRITICALITY_LEVEL, this.getStatusIdentifier(elementHeader.getCriticality().getClassificationProperties()));
                    }
                }

                /*
                 * Extract properties
                 */
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESOURCE_NAME, dataAssetProperties.getResourceName());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESOURCE_DESCRIPTION, dataAssetProperties.getDescription());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION_IDENTIFIER, dataAssetProperties.getVersionIdentifier());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, dataAssetProperties.getQualifiedName());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPLOYED_IMPLEMENTATION_TYPE, dataAssetProperties.getDeployedImplementationType());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, dataAssetProperties.getDisplayName());

                if (dataAssetProperties.getAdditionalProperties() != null)
                {
                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES, dataAssetProperties.getAdditionalProperties().toString());
                }

                /*
                 * Extract supplementary properties
                 */
                if (dataAssetElement.getSupplementaryProperties() != null)
                {
                    for (RelatedMetadataElementSummary supplementaryProperties : dataAssetElement.getSupplementaryProperties())
                    {
                        if ((supplementaryProperties != null) && (supplementaryProperties.getRelatedElement().getProperties() != null))
                        {
                            Map<String,String> additionalDescriptiveProperties = supplementaryProperties.getRelatedElement().getProperties();

                            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_DESCRIPTION, additionalDescriptiveProperties.get(OpenMetadataProperty.DISPLAY_NAME.name));
                            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_SUMMARY, additionalDescriptiveProperties.get(OpenMetadataProperty.SUMMARY.name));
                            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ABBREVIATION, additionalDescriptiveProperties.get(OpenMetadataProperty.DESCRIPTION.name));
                            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USAGE, additionalDescriptiveProperties.get(OpenMetadataProperty.USAGE.name));
                        }
                    }
                }

                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LICENSE_TYPE_GUID, associatedLicenceGUID);
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESOURCE_LOCATION_GUID, associatedResourceLocationGUID);
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TAGS, tags);
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SEMANTIC_TERM_GUID, semanticAssignmentTermGUID);

                addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

                return openMetadataRecord;
            }
        }

        return null;
    }


    /**
     * Return the zone names from the AssetZoneMembership classification.
     *
     * @param classificationProperties properties from the relevant classification
     * @return colon separated zone names (colons at either end too)
     */
    private String getZoneNames(Map<String, Object> classificationProperties)
    {
        if (classificationProperties != null)
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
        }

        return "::";
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
     * @param databaseConnection connection to the database
     * @param elementHeader unique identifier of the attached element and other related information
     * @param metadataCorrelationHeaders correlation properties for each synchronized system
     */
    private void syncCorrelationProperties(java.sql.Connection             databaseConnection,
                                           ElementHeader                   elementHeader,
                                           List<RelatedMetadataElementSummary> metadataCorrelationHeaders)
    {
        if (metadataCorrelationHeaders != null)
        {
            for (RelatedMetadataElementSummary metadataCorrelationHeader : metadataCorrelationHeaders)
            {
                syncCorrelationProperties(databaseConnection, elementHeader, metadataCorrelationHeader);
            }
        }
    }


    /**
     * Process the correlation properties for an element retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param elementHeader unique identifier of the attached element and other related information
     * @param metadataCorrelationHeader correlation properties
     */
    private void syncCorrelationProperties(java.sql.Connection           databaseConnection,
                                           ElementHeader                 elementHeader,
                                           RelatedMetadataElementSummary metadataCorrelationHeader)
    {
        final String methodName = "syncCorrelationProperties";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getCorrelationPropertiesDataValues(databaseConnection, elementHeader, metadataCorrelationHeader);

            databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.CORRELATION_PROPERTIES.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);
        }
    }


    /**
     * Convert the correlation properties for an element retrieved from the open metadata ecosystem to database columns.
     *
     * @param databaseConnection connection to the database
     * @param elementHeader unique identifier of the attached element and other related information
     * @param relatedExternalId correlation properties
     * @return columns
     */
    private Map<String, JDBCDataValue> getCorrelationPropertiesDataValues(java.sql.Connection           databaseConnection,
                                                                          ElementHeader                 elementHeader,
                                                                          RelatedMetadataElementSummary relatedExternalId)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        if (elementHeader != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EGERIA_OWNED, (elementHeader.getOrigin().getOriginCategory() != ElementOriginCategory.EXTERNAL_SOURCE));
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ELEMENT_GUID, elementHeader.getGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_SCOPE_GUID, elementHeader.getOrigin().getHomeMetadataCollectionId());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, elementHeader.getType().getTypeName());
        }

        if (relatedExternalId != null)
        {
            if (relatedExternalId.getRelatedElement().getProperties() instanceof ExternalIdProperties externalIdProperties)
            {
                if (externalIdProperties.getExternalInstanceCreatedBy() != null)
                {
                    syncExternalUserId(databaseConnection,
                                       externalIdProperties.getExternalInstanceCreatedBy(),
                                       relatedExternalId.getRelatedElement().getElementHeader().getOrigin().getHomeMetadataCollectionId(),
                                       this.getAssociatedUserIdentity(externalIdProperties.getExternalInstanceCreatedBy()));
                }
                if (externalIdProperties.getExternalInstanceLastUpdatedBy() != null)
                {
                    syncExternalUserId(databaseConnection,
                                       externalIdProperties.getExternalInstanceLastUpdatedBy(),
                                       relatedExternalId.getRelatedElement().getElementHeader().getOrigin().getHomeMetadataCollectionId(),
                                       this.getAssociatedUserIdentity(externalIdProperties.getExternalInstanceLastUpdatedBy()));
                }
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_IDENTIFIER, externalIdProperties.getIdentifier());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATED_BY, externalIdProperties.getExternalInstanceLastUpdatedBy());
                addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, externalIdProperties.getExternalInstanceLastUpdateTime());
                addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, externalIdProperties.getExternalInstanceCreationTime());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_BY, externalIdProperties.getExternalInstanceCreatedBy());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION, externalIdProperties.getExternalInstanceVersion());
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_TYPE_NAME, externalIdProperties.getExternalInstanceTypeName());

                if (relatedExternalId.getRelationshipProperties() != null)
                {
                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_CONFIRMED_SYNC_TIME, relatedExternalId.getRelationshipProperties().get(OpenMetadataProperty.LAST_SYNCHRONIZED.name));
                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES, relatedExternalId.getRelationshipProperties().get(OpenMetadataProperty.MAPPING_PROPERTIES.name));
                }
            }
        }

        return openMetadataRecord;
    }


    /**
     * Process details of a metadata collection retrieved from the open metadata ecosystem.  These can be found in element headers or
     * correlation properties.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param elementOrigin unique identifier of the metadata collection and other information
     * @param deployedImplementationType description of the type of software capability supporting the metadata collection
     */
    private void syncMetadataCollection(java.sql.Connection databaseConnection,
                                        ElementOrigin       elementOrigin,
                                        String              deployedImplementationType)
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

                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.METADATA_COLLECTION.getTableName(), openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.METADATA_COLLECTION_ID, metadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.METADATA_COLLECTION_NAME, metadataCollectionName);

        if (metadataCollectionType != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROVENANCE_TYPE, metadataCollectionType.getName());
        }
        else
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROVENANCE_TYPE, ElementOriginCategory.UNKNOWN.getName());
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPLOYED_IMPLEMENTATION_TYPE, deployedImplementationType);


        return openMetadataRecord;
    }


    /**
     * Process information about a specific asset type.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param elementType details of a specific type
     */
    private void syncAssetType(java.sql.Connection databaseConnection,
                               ElementType         elementType)
    {
        final String methodName = "syncAssetTypes";

        if (elementType != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getAssetTypesDataValues(elementType);

                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.ASSET_TYPE.getTableName(), openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, elementType.getTypeName());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TYPE_DESCRIPTION, elementType.getTypeDescription());

        if (elementType.getSuperTypeNames() != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SUPER_TYPES, this.formatJSONColumn(elementType.getSuperTypeNames()));
        }

        return openMetadataRecord;
    }


    /**
     * Process information about a specific location.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param location details of a specific location
     */
    private void syncLocation(java.sql.Connection databaseConnection,
                              OpenMetadataElement location)
    {
        final String methodName = "syncLocation";

        if (location != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getLocationDataValues(location.getElementGUID(),
                                                                                           propertyHelper.getStringProperty(connectorName,
                                                                                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                            location.getElementProperties(),
                                                                                                                            methodName),
                                                                                           location.getType().getTypeName());

                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.LOCATION.getTableName(), openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LOCATION_GUID, locationGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LOCATION_NAME, locationName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, locationType);

        return openMetadataRecord;
    }


    /**
     * Process information about a specific license.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param license details of a specific license
     */
    private void syncLicense(java.sql.Connection databaseConnection,
                             OpenMetadataElement license)
    {
        final String methodName = "syncLicense";

        if (license != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getLicenseTypeDataValues(license.getElementGUID(),
                                                                                              propertyHelper.getStringProperty(connectorName,
                                                                                                                            "title",
                                                                                                                            license.getElementProperties(),
                                                                                                                            methodName),
                                                                                              propertyHelper.getStringProperty(connectorName,
                                                                                                                            "description",
                                                                                                                            license.getElementProperties(),
                                                                                                                            methodName));

                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.LICENSE_TYPE.getTableName(), openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
    private Map<String, JDBCDataValue> getLicenseTypeDataValues(String licenseGUID,
                                                                String licenseName,
                                                                String licenseDescription)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LICENSE_TYPE_GUID, licenseGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LICENSE_NAME, licenseName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, licenseDescription);
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process the correlation properties for an element retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param elementGUID unique identifier of the related element
     * @param elementTypeName type of related element
     * @param numberOfComments number of attached comments
     * @param numberOfRatings number of attached ratings/reviews
     * @param aveRating average star rating
     * @param numberOfTags number of attached informal tags
     * @param numberOfLikes number of attached likes
     */
    private void syncCollaborationActivity(java.sql.Connection databaseConnection,
                                           String              elementGUID,
                                           String              elementTypeName,
                                           int                 numberOfComments,
                                           int                 numberOfRatings,
                                           int                 aveRating,
                                           int                 numberOfTags,
                                           int                 numberOfLikes)
    {
        final String methodName = "syncCollaborationActivity";

        if ((numberOfComments > 0) || (numberOfRatings > 0) || (numberOfTags > 0) || (numberOfLikes > 0))
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.COLLABORATION_ACTIVITY.getTableName(),
                                                                                            HarvestOpenMetadataColumn.ELEMENT_GUID.getColumnName(),
                                                                                            elementGUID,
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.COLLABORATION_ACTIVITY.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getCollaborationActivityDataValues(elementGUID,
                                                                                                        elementTypeName,
                                                                                                        numberOfComments,
                                                                                                        numberOfRatings,
                                                                                                        aveRating,
                                                                                                        numberOfTags,
                                                                                                        numberOfLikes);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.COLLABORATION_ACTIVITY.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param elementGUID unique identifier of the related element
     * @param elementTypeName type of related element
     * @param numberOfComments number of attached comments
     * @param numberOfRatings number of attached ratings/reviews
     * @param averageRating average star rating
     * @param numberOfTags number of attached informal tags
     * @param numberOfLikes number of attached likes
     * @return columns
     */
    private Map<String, JDBCDataValue> getCollaborationActivityDataValues(String elementGUID,
                                                                          String elementTypeName,
                                                                          int    numberOfComments,
                                                                          int    numberOfRatings,
                                                                          int    averageRating,
                                                                          int    numberOfTags,
                                                                          int    numberOfLikes)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ELEMENT_GUID, elementGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, elementTypeName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_COMMENTS, numberOfComments);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_RATINGS, numberOfRatings);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.AVG_RATING, averageRating);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_TAGS, numberOfTags);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_LIKES, numberOfLikes);

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process any userIds found in the element header.  These maybe from archives and so may not have an associated user identity entity.
     *
     * @param databaseConnection connection to the database
     * @param elementVersions details of the people involved in each version of the element
     * @param elementOrigin details of the metadata source
     */
    private void processUserIds(java.sql.Connection databaseConnection,
                                ElementVersions     elementVersions,
                                ElementOrigin       elementOrigin)
    {
        String metadataCollectionId = elementOrigin.getHomeMetadataCollectionId();

        syncExternalUserId(databaseConnection, elementVersions.getCreatedBy(), metadataCollectionId, getAssociatedUserIdentity(elementVersions.getCreatedBy()));

        if (elementVersions.getUpdatedBy() != null)
        {
            syncExternalUserId(databaseConnection, elementVersions.getUpdatedBy(), metadataCollectionId, getAssociatedUserIdentity(elementVersions.getUpdatedBy()));
        }

        if (elementVersions.getMaintainedBy() != null)
        {
            for (String userId : elementVersions.getMaintainedBy())
            {
                syncExternalUserId(databaseConnection, userId, metadataCollectionId, getAssociatedUserIdentity(userId));
            }
        }
    }


    /**
     * Retrieve the user identity entity for a userId
     *
     * @param userId retrieved userId
     * @return guid of userIdentity entity
     */
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

            List<OpenMetadataElement> userIdentities = openMetadataStore.findMetadataElements(searchProperties,
                                                                                              null,
                                                                                              this.openMetadataStore.getQueryOptions(OpenMetadataType.USER_IDENTITY.typeName,
                                                                                                                                     null,
                                                                                                                                     0,
                                                                                                                                     openMetadataStore.getMaxPagingSize()));

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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to the database
     * @param userId unique identifier of the attached element
     * @param metadataCollectionId unique identifier of the metadata collection
     * @param userIdentityGUID unique identifier of the corresponding user identity entity
     */
    private void syncExternalUserId(java.sql.Connection databaseConnection,
                                    String              userId,
                                    String              metadataCollectionId,
                                    String              userIdentityGUID)
    {
        final String methodName = "syncUserId";

        if (userId != null)
        {
            try
            {
                Map<String, JDBCDataValue> openMetadataRecord = this.getExternalUserDataValues(userId,
                                                                                               metadataCollectionId,
                                                                                               userIdentityGUID);

                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.EXTERNAL_USER.getTableName(), openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @return columns
     */
    private Map<String, JDBCDataValue> getExternalUserDataValues(String userId,
                                                                 String metadataCollectionId,
                                                                 String userIdentityGUID)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_SCOPE_GUID, metadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_USER_ID, userId);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LINKED_USER_IDENTITY_GUID, userIdentityGUID);

        return openMetadataRecord;
    }


    /**
     * Process information about a specific certification.
     *
     * @param databaseConnection connection to the database
     * @param elementGUID unique identifier of associated element
     * @param certification details of a specific certification
     */
    private void syncCertification(java.sql.Connection    databaseConnection,
                                   String                 elementGUID,
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
                syncCertificationType(databaseConnection, certification.getElement());

                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.CERTIFICATION.getTableName(),
                                                                                            HarvestOpenMetadataColumn.CERTIFICATION_GUID.getColumnName(),
                                                                                            elementGUID,
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.CERTIFICATION.getColumnNameTypeMap());

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

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.CERTIFICATION.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.REFERENCEABLE_GUID, elementGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CERTIFICATION_GUID, certificationGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CERTIFICATION_TYPE_GUID, certificationTypeGUID);
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.START_TIMESTAMP, startDate);
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_TIMESTAMP, endDate);
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process information about a specific certificationType.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param certificationType details of a specific certificationType
     */
    private void syncCertificationType(java.sql.Connection databaseConnection,
                                       OpenMetadataElement certificationType)
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

                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.CERTIFICATION_TYPE.getTableName(), openMetadataRecord);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CERTIFICATION_TYPE_GUID, certificationTypeGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CERTIFICATION_TITLE, certificationTypeTitle);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CERTIFICATION_SUMMARY, certificationTypeSummary);

        return openMetadataRecord;
    }



    /**
     * Process information about a specific user's contribution.
     *
     * @param databaseConnection connection to the database
     * @param profileGUID unique identifier of the attached element
     * @param profileTypeName type of profile
     * @param contribution details of a specific contribution
     */
    private void syncContribution(java.sql.Connection    databaseConnection,
                                  String                 profileGUID,
                                  String                 profileTypeName,
                                  RelatedMetadataElement contribution)
    {
        final String methodName = "syncContribution";

        if (contribution != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.CONTRIBUTION.getTableName(),
                                                                                            HarvestOpenMetadataColumn.PROFILE_GUID.getColumnName(),
                                                                                            profileGUID,
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.CONTRIBUTION.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getContributionDataValues(profileGUID,
                                                                                               profileTypeName,
                                                                                               propertyHelper.getLongProperty(connectorName,
                                                                                                                              OpenMetadataProperty.KARMA_POINTS.name,
                                                                                                                              contribution.getElement().getElementProperties(),
                                                                                                                              methodName));

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.CONTRIBUTION.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param profileGUID unique identifier of the attached element
     * @param profileTypeName type of profile
     * @param karmaPoints amount of activity from user
     * @return columns
     */
    private Map<String, JDBCDataValue> getContributionDataValues(String profileGUID,
                                                                 String profileTypeName,
                                                                 long   karmaPoints)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROFILE_GUID, profileGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, profileTypeName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.KARMA_POINTS, karmaPoints);

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }



    /**
     * Process information about a specific schema attribute attached to an asset.
     *
     * @param databaseConnection connection to the database
     * @param assetGUID unique identifier of the attached asset
     * @param assetQualifiedName unique name of the attached asset
     * @param glossaryTermGUID unique identifier of the glossary term attached via SemanticAssignment
     * @param hasProfile does this data filed have at least one SchemaAttributeDefinition (0615) relationship
     * @param schemaAttributeElement details of a specific schemaAttributeElement
     */
    private void syncDataField(java.sql.Connection     databaseConnection,
                               String                  assetGUID,
                               String                  assetQualifiedName,
                               String                  glossaryTermGUID,
                               boolean                 hasProfile,
                               OpenMetadataRootElement schemaAttributeElement)
    {
        final String methodName = "syncDataField";

        if (schemaAttributeElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.DATA_FIELD.getTableName(),
                                                                                            HarvestOpenMetadataColumn.DATA_FIELD_GUID.getColumnName(),
                                                                                            schemaAttributeElement.getElementHeader().getGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.DATA_FIELD.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getDataFieldDataValues(databaseConnection,
                                                                                            assetGUID,
                                                                                            assetQualifiedName,
                                                                                            glossaryTermGUID,
                                                                                            hasProfile,
                                                                                            schemaAttributeElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.DATA_FIELD.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to database
     * @param assetGUID unique identifier of the attached asset
     * @param assetQualifiedName unique name of the attached asset
     * @param glossaryTermGUID unique identifier of the glossary term attached via SemanticAssignment
     * @param hasProfile does this data filed have at least one SchemaAttributeDefinition (0615) relationship
     * @param schemaAttributeElement details of a specific schemaAttributeElement
     * @return columns
     */
    private Map<String, JDBCDataValue> getDataFieldDataValues(java.sql.Connection     databaseConnection,
                                                              String                  assetGUID,
                                                              String                  assetQualifiedName,
                                                              String                  glossaryTermGUID,
                                                              boolean                 hasProfile,
                                                              OpenMetadataRootElement schemaAttributeElement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        processUserIds(databaseConnection, schemaAttributeElement.getElementHeader().getVersions(), schemaAttributeElement.getElementHeader().getOrigin());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DATA_FIELD_GUID, schemaAttributeElement.getElementHeader().getGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION_IDENTIFIER, Long.toString(schemaAttributeElement.getElementHeader().getVersions().getVersion()));

        if (schemaAttributeElement.getProperties() instanceof SchemaAttributeProperties schemaAttributeProperties)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DATA_FIELD_NAME, schemaAttributeProperties.getDisplayName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, schemaAttributeProperties.getDescription());
        }

        if ((schemaAttributeElement.getElementHeader().getSchemaType() != null) && (schemaAttributeElement.getElementHeader().getSchemaType().getClassificationProperties() != null))
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DATA_FIELD_TYPE, schemaAttributeElement.getElementHeader().getSchemaType().getClassificationProperties().get(OpenMetadataProperty.DATA_TYPE.name));
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.HAS_PROFILE, hasProfile);

        if (schemaAttributeElement.getElementHeader().getConfidentiality() != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL, this.getStatusIdentifier(schemaAttributeElement.getElementHeader().getConfidentiality().getClassificationProperties()));
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SEMANTIC_TERM_GUID, glossaryTermGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSET_GUID, assetGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, assetQualifiedName);

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }



    /**
     * Process information about a specific department (team) in the organization.
     *
     * @param databaseConnection connection to the database
     * @param parentDepartmentGUID unique identifier of the parent department
     * @param managerProfileGUID unique identifier of manager's profile
     * @param department details of a specific department
     */
    private void syncDepartment(java.sql.Connection databaseConnection,
                                String              parentDepartmentGUID,
                                String              managerProfileGUID,
                                OpenMetadataElement department)
    {
        final String methodName = "syncDepartment";

        if (department != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.DEPARTMENT.getTableName(),
                                                                                            HarvestOpenMetadataColumn.DEPARTMENT_GUID.getColumnName(),
                                                                                            department.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.DEPARTMENT.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getDepartmentDataValues(parentDepartmentGUID, managerProfileGUID, department);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.DEPARTMENT.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param managerProfileGUID unique identifier of manager's profile
     * @param department details of a specific department
     * @return columns
     */
    private Map<String, JDBCDataValue> getDepartmentDataValues(String              parentDepartmentGUID,
                                                               String              managerProfileGUID,
                                                               OpenMetadataElement department)
    {
        final String methodName = "getDepartmentDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPARTMENT_GUID, department.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPARTMENT_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                      department.getElementProperties(),
                                                                                                                      methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPARTMENT_IDENTIFIER, propertyHelper.getStringProperty(connectorName,
                                                                                                                            OpenMetadataProperty.IDENTIFIER.name,
                                                                                                                            department.getElementProperties(),
                                                                                                                            methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.MANAGER_PROFILE_GUID, managerProfileGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PARENT_DEPARTMENT_GUID, parentDepartmentGUID);

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }



    /**
     * Process a glossary retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param glossaryElement description of the glossary
     * @param numberOfTerms number of terms in this glossary
     * @param numberOfCategories number of categories in this glossary
     * @param numberOfLinkedTerms number of terms categorized
     * @param license attached license (maybe null)
     */
    private void syncGlossary(java.sql.Connection     databaseConnection,
                              OpenMetadataRootElement glossaryElement,
                              int                     numberOfTerms,
                              int                     numberOfCategories,
                              int                     numberOfLinkedTerms,
                              OpenMetadataElement     license)
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
                                                                                        HarvestOpenMetadataTable.GLOSSARY.getTableName(),
                                                                                        HarvestOpenMetadataColumn.GLOSSARY_GUID.getColumnName(),
                                                                                        glossaryElement.getElementHeader().getGUID(),
                                                                                        HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                        HarvestOpenMetadataTable.GLOSSARY.getColumnNameTypeMap());

            Map<String, JDBCDataValue> openMetadataRecord = this.getGlossaryDataValues(databaseConnection,
                                                                                       glossaryElement,
                                                                                       numberOfTerms,
                                                                                       numberOfCategories,
                                                                                       numberOfLinkedTerms,
                                                                                       licenseGUID);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
            {
                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.GLOSSARY.getTableName(), openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to database
     * @param glossaryElement description of the glossary
     * @param numberOfTerms number of terms in this glossary
     * @param numberOfCategories number of categories in this glossary
     * @param numberOfLinkedTerms number of terms categorized
     * @param licenseGUID unique identifier of an attached license
     * @return columns
     */
    private Map<String, JDBCDataValue> getGlossaryDataValues(java.sql.Connection     databaseConnection,
                                                             OpenMetadataRootElement glossaryElement,
                                                             int                     numberOfTerms,
                                                             int                     numberOfCategories,
                                                             int                     numberOfLinkedTerms,
                                                             String                  licenseGUID)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        ElementHeader      elementHeader      = glossaryElement.getElementHeader();

        if (elementHeader != null)
        {
            processUserIds(databaseConnection, elementHeader.getVersions(), elementHeader.getOrigin());

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.GLOSSARY_GUID, elementHeader.getGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.METADATA_COLLECTION_ID, elementHeader.getOrigin().getHomeMetadataCollectionId());


            addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, elementHeader.getVersions().getCreateTime());
            if (elementHeader.getVersions().getUpdateTime() != null)
            {
                addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, elementHeader.getVersions().getUpdateTime());
            }
            else
            {
                addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, elementHeader.getVersions().getCreateTime());
            }

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CLASSIFICATIONS, this.getClassifications(elementHeader.getOtherClassifications()));

            if (elementHeader.getOwnership() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_GUID, this.getOpenMetadataStringProperty(elementHeader.getOwnership().getClassificationProperties(), OpenMetadataProperty.OWNER.name, 80));
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_TYPE_NAME, this.getOpenMetadataStringProperty(elementHeader.getOwnership().getClassificationProperties(), OpenMetadataProperty.OWNER_TYPE_NAME.name, 40));
            }
        }

        if (glossaryElement.getProperties() instanceof GlossaryProperties glossaryProperties)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, glossaryProperties.getQualifiedName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, glossaryProperties.getDisplayName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, glossaryProperties.getDescription());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.GLOSSARY_LANGUAGE, glossaryProperties.getLanguage());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USAGE, glossaryProperties.getUsage());
            if (glossaryProperties.getAdditionalProperties() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES, glossaryProperties.getAdditionalProperties().toString());
            }
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_TERMS, numberOfTerms);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_CATEGORIES, numberOfCategories);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_LINKED_TERMS, numberOfLinkedTerms);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LICENSE_TYPE_GUID, licenseGUID);

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Build up a string representing the classifications attached to an element.
     *
     * @param classifications details of the element
     * @return string or null
     */
    private String getClassifications(List<ElementClassification> classifications)
    {
        if ((classifications != null) && (! classifications.isEmpty()))
        {
            StringBuilder classificationList = new StringBuilder(":");

            for (ElementClassification classification : classifications)
            {
                classificationList.append(classification.getClassificationName()).append(":");
            }

            return classificationList.toString();
        }

        return null;
    }


    /**
     * Build up a string representing the classifications attached to an element.
     *
     * @param classifications details of the element
     * @return string or null
     */
    private String getAttachedClassifications(List<AttachedClassification> classifications)
    {
        if ((classifications != null) && (! classifications.isEmpty()))
        {
            StringBuilder classificationList = new StringBuilder(":");

            for (AttachedClassification classification : classifications)
            {
                classificationList.append(classification.getClassificationName()).append(":");
            }

            return classificationList.toString();
        }

        return null;
    }


    /**
     * Process a glossary term retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param glossaryTermElement glossary term retrieved from the open metadata ecosystem
     * @param glossaryGUID unique identifier of the owning glossary
     * @param lastFeedbackTime last time a type of feedback was added to the term
     * @param numberOfLinkedElements number of semantically linked elements
     * @param lastLinkTime last time a semantic assignment was attached to this term
     */
    private void syncTermActivity(java.sql.Connection     databaseConnection,
                                  OpenMetadataRootElement glossaryTermElement,
                                  String                  glossaryGUID,
                                  Date                    lastFeedbackTime,
                                  int                     numberOfLinkedElements,
                                  Date                    lastLinkTime)
    {
        final String methodName = "syncTermActivity";

        try
        {
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                        HarvestOpenMetadataTable.TERM_ACTIVITY.getTableName(),
                                                                                        HarvestOpenMetadataColumn.TERM_GUID.getColumnName(),
                                                                                        glossaryTermElement.getElementHeader().getGUID(),
                                                                                        HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                        HarvestOpenMetadataTable.TERM_ACTIVITY.getColumnNameTypeMap());

            Map<String, JDBCDataValue> openMetadataRecord = this.getTermActivityDataValues(databaseConnection,
                                                                                           glossaryTermElement,
                                                                                           glossaryGUID,
                                                                                           lastFeedbackTime,
                                                                                           numberOfLinkedElements,
                                                                                           lastLinkTime);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
            {
                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.TERM_ACTIVITY.getTableName(), openMetadataRecord);
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param databaseConnection connection to database
     * @param glossaryTermElement glossary term retrieved from the open metadata ecosystem
     * @param glossaryGUID unique identifier of the owning glossary
     * @param lastFeedbackTime last time a type of feedback was added to the term
     * @param numberOfLinkedElements number of semantically linked elements
     * @param lastLinkTime last time a semantic assignment was attached to this term
     * @return columns
     */
    private Map<String, JDBCDataValue> getTermActivityDataValues(java.sql.Connection     databaseConnection,
                                                                 OpenMetadataRootElement glossaryTermElement,
                                                                 String                  glossaryGUID,
                                                                 Date                    lastFeedbackTime,
                                                                 int                     numberOfLinkedElements,
                                                                 Date                    lastLinkTime)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        ElementHeader elementHeader = glossaryTermElement.getElementHeader();

        if (elementHeader != null)
        {
            processUserIds(databaseConnection, elementHeader.getVersions(), elementHeader.getOrigin());

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TERM_GUID, elementHeader.getGUID());
            addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, elementHeader.getVersions().getCreateTime());

            if (elementHeader.getOwnership() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_GUID, this.getOpenMetadataStringProperty(elementHeader.getOwnership().getClassificationProperties(), OpenMetadataProperty.OWNER.name, 80));
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_TYPE_NAME, this.getOpenMetadataStringProperty(elementHeader.getOwnership().getClassificationProperties(), OpenMetadataProperty.OWNER_TYPE_NAME.name, 40));
            }
            if (elementHeader.getConfidentiality() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL, this.getStatusIdentifier(elementHeader.getConfidentiality().getClassificationProperties()));
            }
            if (elementHeader.getConfidence() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENCE_LEVEL, this.getStatusIdentifier(elementHeader.getConfidence().getClassificationProperties()));
            }
            if (elementHeader.getCriticality() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CRITICALITY_LEVEL, this.getStatusIdentifier(elementHeader.getCriticality().getClassificationProperties()));
            }
        }

        if (glossaryTermElement.getProperties() instanceof GlossaryTermProperties glossaryTermProperties)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TERM_NAME, glossaryTermProperties.getDisplayName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, glossaryTermProperties.getQualifiedName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TERM_SUMMARY, glossaryTermProperties.getSummary());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION_IDENTIFIER, glossaryTermProperties.getVersionIdentifier());
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.GLOSSARY_GUID, glossaryGUID);
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_FEEDBACK_TIME, lastFeedbackTime);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_LINKED_ELEMENTS, numberOfLinkedElements);
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_LINKED_TIME, lastLinkTime);

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process an asset relationship retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param relatedAsset relationship information
     */
    private void syncRelatedAsset(java.sql.Connection      databaseConnection,
                                  OpenMetadataRelationship relatedAsset)
    {
        final String methodName = "syncRelatedAsset";

        if (relatedAsset != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.RELATED_ASSET.getTableName(),
                                                                                            HarvestOpenMetadataColumn.RELATIONSHIP_GUID.getColumnName(),
                                                                                            relatedAsset.getRelationshipGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.RELATED_ASSET.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getRelatedAssetDataValues(relatedAsset);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.RELATED_ASSET.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
    private Map<String, JDBCDataValue> getRelatedAssetDataValues(OpenMetadataRelationship relatedAsset)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_1_GUID, relatedAsset.getElementGUIDAtEnd1());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_2_GUID, relatedAsset.getElementGUIDAtEnd2());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_1_ATTRIBUTE_NAME, relatedAsset.getLabelAtEnd1());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_2_ATTRIBUTE_NAME, relatedAsset.getLabelAtEnd2());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, relatedAsset.getType().getTypeName());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RELATIONSHIP_GUID, relatedAsset.getRelationshipGUID());

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process person role element retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param role role information
     */
    private void syncRole(java.sql.Connection databaseConnection,
                          OpenMetadataElement role)
    {
        final String methodName = "syncRole";

        if (role != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.ROLE.getTableName(),
                                                                                            HarvestOpenMetadataColumn.ROLE_GUID.getColumnName(),
                                                                                            role.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.ROLE.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getRoleDataValues(role);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.ROLE.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ROLE_GUID, openMetadataElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ROLE_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                      openMetadataElement.getElementProperties(),
                                                                                                      methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, openMetadataElement.getType().getTypeName());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.HEADCOUNT, propertyHelper.getIntProperty(connectorName,
                                                                                                    OpenMetadataProperty.HEAD_COUNT.name,
                                                                                                    openMetadataElement.getElementProperties(),
                                                                                                    methodName));
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }



    /**
     * Process an PersonRoleAppointment relationship retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param userGUID unique identifier of associated user identify
     * @param personRoleAppointment relationship information
     */
    private void syncRoleToProfile(java.sql.Connection      databaseConnection,
                                   String                   userGUID,
                                   OpenMetadataRelationship personRoleAppointment)
    {
        final String methodName = "syncRoleToProfile";

        if (personRoleAppointment != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.ROLE_TO_PROFILE.getTableName(),
                                                                                            HarvestOpenMetadataColumn.RELATIONSHIP_GUID.getColumnName(),
                                                                                            personRoleAppointment.getRelationshipGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.ROLE_TO_PROFILE.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getRoleToProfileDataValues(userGUID, personRoleAppointment);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.ROLE_TO_PROFILE.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
    private Map<String, JDBCDataValue> getRoleToProfileDataValues(String                   userGUID,
                                                                  OpenMetadataRelationship personRoleAppointment)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ROLE_GUID, personRoleAppointment.getElementGUIDAtEnd2());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_IDENTITY_GUID, userGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RELATIONSHIP_GUID, personRoleAppointment.getRelationshipGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROFILE_GUID, personRoleAppointment.getElementGUIDAtEnd1());

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.START_TIMESTAMP, personRoleAppointment.getEffectiveFromTime());
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_TIMESTAMP, personRoleAppointment.getEffectiveToTime());

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param toDoElement to do information
     * @param toDoSourceElement  source of the to do
     * @param sponsorElement actor sponsoring the work - may be null
     */
    private void syncToDo(java.sql.Connection    databaseConnection,
                          OpenMetadataElement    toDoElement,
                          RelatedMetadataElement toDoSourceElement,
                          RelatedMetadataElement sponsorElement)
    {
        final String methodName = "syncToDo";

        if (toDoElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.TO_DO.getTableName(),
                                                                                            HarvestOpenMetadataColumn.TO_DO_GUID.getColumnName(),
                                                                                            toDoElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.TO_DO.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getToDoDataValues(toDoElement,
                                                                                       toDoSourceElement,
                                                                                       sponsorElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.TO_DO.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param sponsorElement actor sponsoring the work - may be null
     * @return columns
     */
    private Map<String, JDBCDataValue> getToDoDataValues(OpenMetadataElement    toDoElement,
                                                         RelatedMetadataElement toDoSourceElement,
                                                         RelatedMetadataElement sponsorElement)
    {
        final String methodName = "getToDoDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TO_DO_GUID, toDoElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, toDoElement.getType().getTypeName());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                           toDoElement.getElementProperties(),
                                                                                                           methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                         toDoElement.getElementProperties(),
                                                                                                         methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.DESCRIPTION.name,
                                                                                                                   toDoElement.getElementProperties(),
                                                                                                                   methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                                      OpenMetadataProperty.REQUESTED_TIME.name,
                                                                                                                      toDoElement.getElementProperties(),
                                                                                                                      methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CATEGORY, propertyHelper.getStringProperty(connectorName,
                                                                                                               OpenMetadataProperty.CATEGORY.name,
                                                                                                               toDoElement.getElementProperties(),
                                                                                                               methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PRIORITY, propertyHelper.getIntProperty(connectorName,
                                                                                                   OpenMetadataProperty.PRIORITY.name,
                                                                                                   toDoElement.getElementProperties(),
                                                                                                   methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DUE_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                                 OpenMetadataProperty.DUE_TIME.name,
                                                                                                                 toDoElement.getElementProperties(),
                                                                                                                 methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_REVIEW_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                                         OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                                                                                         toDoElement.getElementProperties(),
                                                                                                                         methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.COMPLETION_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                                        OpenMetadataProperty.COMPLETION_TIME.name,
                                                                                                                        toDoElement.getElementProperties(),
                                                                                                                        methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ACTIVITY_STATUS, propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                                                                                                OpenMetadataProperty.ACTIVITY_STATUS.name,
                                                                                                                                toDoElement.getElementProperties(),
                                                                                                                                methodName));

        if (toDoSourceElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TO_DO_SOURCE_GUID, toDoSourceElement.getElement().getElementGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TO_DO_SOURCE_TYPE, toDoSourceElement.getElement().getType().getTypeName());
        }

        if (sponsorElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SPONSOR_GUID, sponsorElement.getElement().getElementGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SPONSOR_TYPE, sponsorElement.getElement().getType().getTypeName());
        }

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process a project description retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param projectElement to do information
     * @param parentProjectElement  source of the to do
     */
    private void syncProject(java.sql.Connection    databaseConnection,
                             OpenMetadataElement    projectElement,
                             RelatedMetadataElement parentProjectElement)
    {
        final String methodName = "syncProject";

        if (projectElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.PROJECT.getTableName(),
                                                                                            HarvestOpenMetadataColumn.PROJECT_GUID.getColumnName(),
                                                                                            projectElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.PROJECT.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getProjectDataValues(projectElement, parentProjectElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.PROJECT.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a project description from the open metadata ecosystem to database columns.
     *
     * @param projectElement project information
     * @param parentProjectElement  optional parent
     * @return columns
     */
    private Map<String, JDBCDataValue> getProjectDataValues(OpenMetadataElement    projectElement,
                                                            RelatedMetadataElement parentProjectElement)
    {
        final String methodName = "getProjectDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROJECT_GUID, projectElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, projectElement.getType().getTypeName());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                     projectElement.getElementProperties(),
                                                                                                                     methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROJECT_IDENTIFIER, propertyHelper.getStringProperty(connectorName,
                                                                                                                         OpenMetadataProperty.IDENTIFIER.name,
                                                                                                                         projectElement.getElementProperties(),
                                                                                                                         methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                   projectElement.getElementProperties(),
                                                                                                                   methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, propertyHelper.getStringProperty(connectorName,
                                                                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                                                                  projectElement.getElementProperties(),
                                                                                                                  methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                                      OpenMetadataProperty.START_DATE.name,
                                                                                                                      projectElement.getElementProperties(),
                                                                                                                      methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CLASSIFICATIONS, this.getAttachedClassifications(projectElement.getClassifications()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROJECT_PHASE, propertyHelper.getStringProperty(connectorName,
                                                                                                                     OpenMetadataProperty.PROJECT_PHASE.name,
                                                                                                                     projectElement.getElementProperties(),
                                                                                                                     methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROJECT_STATUS, propertyHelper.getStringProperty(connectorName,
                                                                                                                     OpenMetadataProperty.PROJECT_STATUS.name,
                                                                                                                     projectElement.getElementProperties(),
                                                                                                                     methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROJECT_HEALTH, propertyHelper.getStringProperty(connectorName,
                                                                                                                     OpenMetadataProperty.PROJECT_HEALTH.name,
                                                                                                                     projectElement.getElementProperties(),
                                                                                                                     methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PRIORITY, propertyHelper.getIntProperty(connectorName,
                                                                                                            OpenMetadataProperty.PRIORITY.name,
                                                                                                            projectElement.getElementProperties(),
                                                                                                            methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DUE_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                                 OpenMetadataProperty.PLANNED_END_DATE.name,
                                                                                                                 projectElement.getElementProperties(),
                                                                                                                 methodName));

        if (parentProjectElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PARENT_GUID, parentProjectElement.getElement().getElementGUID());
        }

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process a community description retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param communityElement community information
     */
    private void syncCommunity(java.sql.Connection databaseConnection,
                               OpenMetadataElement communityElement)
    {
        final String methodName = "syncCommunity";

        if (communityElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.COMMUNITY.getTableName(),
                                                                                            HarvestOpenMetadataColumn.COMMUNITY_GUID.getColumnName(),
                                                                                            communityElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.COMMUNITY.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getCommunityDataValues(communityElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.COMMUNITY.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a community description from the open metadata ecosystem to database columns.
     *
     * @param communityElement community information
     * @return columns
     */
    private Map<String, JDBCDataValue> getCommunityDataValues(OpenMetadataElement    communityElement)
    {
        final String methodName = "getCommunityDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.COMMUNITY_GUID, communityElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, communityElement.getType().getTypeName());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                     communityElement.getElementProperties(),
                                                                                                                     methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                   communityElement.getElementProperties(),
                                                                                                                   methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, propertyHelper.getStringProperty(connectorName,
                                                                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                                                                  communityElement.getElementProperties(),
                                                                                                                  methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, communityElement.getVersions().getCreateTime());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CLASSIFICATIONS, this.getAttachedClassifications(communityElement.getClassifications()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.MISSION, propertyHelper.getStringProperty(connectorName,
                                                                                                              OpenMetadataProperty.MISSION.name,
                                                                                                              communityElement.getElementProperties(),
                                                                                                              methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param workElement type of work
     * @param assignedActors  assigned actors
     */
    private void syncActorAssignments(java.sql.Connection          databaseConnection,
                                      OpenMetadataElement          workElement,
                                      List<RelatedMetadataElement> assignedActors)
    {
        final String methodName = "syncActorAssignments";

        if (workElement != null)
        {
            if (assignedActors != null)
            {
                for (RelatedMetadataElement assignedActor: assignedActors)
                {
                    if (assignedActor != null)
                    {
                        try
                        {
                            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                                        HarvestOpenMetadataTable.ACTOR_ASSIGNMENTS.getTableName(),
                                                                                                        HarvestOpenMetadataColumn.RELATIONSHIP_GUID.getColumnName(),
                                                                                                        assignedActor.getRelationshipGUID(),
                                                                                                        HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                                        HarvestOpenMetadataTable.ACTOR_ASSIGNMENTS.getColumnNameTypeMap());

                            Map<String, JDBCDataValue> openMetadataRecord = this.getActorAssignmentDataValues(workElement, assignedActor);

                            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                            {
                                databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.ACTOR_ASSIGNMENTS.getTableName(), openMetadataRecord);
                            }
                        }
                        catch (Exception error)
                        {
                            auditLog.logException(methodName,
                                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                         error.getClass().getName(),
                                                                                                                         methodName,
                                                                                                                         error.getMessage()),
                                                  error);
                        }
                    }
                }
            }
        }
    }


    /**
     * Convert a user identity and profile from the open metadata ecosystem to database columns.
     *
     * @param workElement to do information
     * @param assignedActor actor assigned to do the work
     * @return columns
     */
    private Map<String, JDBCDataValue> getActorAssignmentDataValues(OpenMetadataElement    workElement,
                                                                    RelatedMetadataElement assignedActor)
    {

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RELATIONSHIP_GUID, assignedActor.getRelationshipGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, assignedActor.getType().getTypeName());


        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSIGNED_WORK_GUID, workElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSIGNED_WORK_TYPE, workElement.getType().getTypeName());

        if (assignedActor.getElement() != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSIGNED_ACTOR_GUID, assignedActor.getElement().getElementGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSIGNED_ACTOR_TYPE, assignedActor.getElement().getType().getTypeName());
        }

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.START_TIMESTAMP, assignedActor.getEffectiveFromTime());
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_TIMESTAMP, assignedActor.getEffectiveToTime());

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process a collection description retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param collectionElement collection information
     * @param parentCollectionElement optional parent
     * @param numberOfMembers count of the members of the collection
     * @param memberTypes list of the different type names found in the members
     */
    private void syncCollection(java.sql.Connection    databaseConnection,
                                OpenMetadataElement    collectionElement,
                                RelatedMetadataElement parentCollectionElement,
                                long                   numberOfMembers,
                                String                 memberTypes)
    {
        final String methodName = "syncCollection";

        if (collectionElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.COLLECTION.getTableName(),
                                                                                            HarvestOpenMetadataColumn.COLLECTION_GUID.getColumnName(),
                                                                                            collectionElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.COLLECTION.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getCollectionDataValues(collectionElement,
                                                                                             parentCollectionElement,
                                                                                             numberOfMembers,
                                                                                             memberTypes);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.COLLECTION.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a collection description from the open metadata ecosystem to database columns.
     *
     * @param collectionElement collection information
     * @param parentCollectionElement optional parent
     * @param numberOfMembers count of the members of the collection
     * @param memberTypes list of the different type names found in the members
     * @return columns
     */
    private Map<String, JDBCDataValue> getCollectionDataValues(OpenMetadataElement    collectionElement,
                                                               RelatedMetadataElement parentCollectionElement,
                                                               long                   numberOfMembers,
                                                               String                 memberTypes)
    {
        final String methodName = "getCollectionDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.COLLECTION_GUID, collectionElement.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, collectionElement.getType().getTypeName());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                     OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                     collectionElement.getElementProperties(),
                                                                                                                     methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                   collectionElement.getElementProperties(),
                                                                                                                   methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, propertyHelper.getStringProperty(connectorName,
                                                                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                                                                  collectionElement.getElementProperties(),
                                                                                                                  methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, collectionElement.getVersions().getCreateTime());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CLASSIFICATIONS, this.getAttachedClassifications(collectionElement.getClassifications()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_LINKED_ELEMENTS, numberOfMembers);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.MEMBER_TYPES, memberTypes);

        if (parentCollectionElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PARENT_GUID, parentCollectionElement.getElement().getElementGUID());
        }

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }



    /**
     * Process a collection description retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param collectionElement collection information
     * @param digitalProductProperties properties from the DigitalProduct classification
     */
    private void syncDigitalProduct(java.sql.Connection databaseConnection,
                                    OpenMetadataElement collectionElement,
                                    ElementProperties   digitalProductProperties)
    {
        final String methodName = "syncDigitalProduct";

        if (collectionElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.DIGITAL_PRODUCT.getTableName(),
                                                                                            HarvestOpenMetadataColumn.COLLECTION_GUID.getColumnName(),
                                                                                            collectionElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.DIGITAL_PRODUCT.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getDigitalProductDataValues(collectionElement, digitalProductProperties);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.DIGITAL_PRODUCT.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a collection description from the open metadata ecosystem to database columns.
     *
     * @param collectionElement collection information
     * @param digitalProductProperties properties from the DigitalProduct classification
     * @return columns
     */
    private Map<String, JDBCDataValue> getDigitalProductDataValues(OpenMetadataElement collectionElement,
                                                                   ElementProperties   digitalProductProperties)
    {
        final String methodName = "getDigitalProductDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.COLLECTION_GUID, collectionElement.getElementGUID());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PRODUCT_STATUS, propertyHelper.getStringProperty(connectorName,
                                                                                                                     OpenMetadataProperty.USER_DEFINED_STATUS.name,
                                                                                                                     digitalProductProperties,
                                                                                                                     methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PRODUCT_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.PRODUCT_NAME.name,
                                                                                                                   digitalProductProperties,
                                                                                                                   methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PRODUCT_TYPE, propertyHelper.getStringProperty(connectorName,
                                                                                                                  OpenMetadataProperty.CATEGORY.name,
                                                                                                                  digitalProductProperties,
                                                                                                                  methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.INTRODUCTION_DATE, propertyHelper.getDateProperty(connectorName,
                                                                                                                          OpenMetadataProperty.INTRODUCTION_DATE.name,
                                                                                                                          digitalProductProperties,
                                                                                                                          methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.MATURITY, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.MATURITY.name,
                                                                                                                   digitalProductProperties,
                                                                                                                   methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SERVICE_LIFE, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.SERVICE_LIFE.name,
                                                                                                                   digitalProductProperties,
                                                                                                                   methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CURRENT_VERSION, propertyHelper.getStringProperty(connectorName,
                                                                                                                   OpenMetadataProperty.CURRENT_VERSION.name,
                                                                                                                   digitalProductProperties,
                                                                                                                   methodName));
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NEXT_VERSION, propertyHelper.getDateProperty(connectorName,
                                                                                                                     OpenMetadataProperty.NEXT_VERSION_DATE.name,
                                                                                                                     digitalProductProperties,
                                                                                                                     methodName));
        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.WITHDRAW_DATE, propertyHelper.getDateProperty(connectorName,
                                                                                                                      OpenMetadataProperty.WITHDRAW_DATE.name,
                                                                                                                      digitalProductProperties,
                                                                                                                      methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }


    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param userIdentifyElement user identity information
     */
    private void syncUserIdentity(java.sql.Connection databaseConnection,
                                  OpenMetadataElement userIdentifyElement)
    {
        final String methodName = "syncUserIdentity";

        if (userIdentifyElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.USER_IDENTITY.getTableName(),
                                                                                            HarvestOpenMetadataColumn.USER_IDENTITY_GUID.getColumnName(),
                                                                                            userIdentifyElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.USER_IDENTITY.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getUserIdentityDataValues(userIdentifyElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.USER_IDENTITY.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @return columns
     */
    private Map<String, JDBCDataValue> getUserIdentityDataValues(OpenMetadataElement    userIdentifyElement)
    {
        final String methodName = "getUserIdentityDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();


        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_IDENTITY_GUID, userIdentifyElement.getElementGUID());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_ID, propertyHelper.getStringProperty(connectorName,
                                                                                                    OpenMetadataProperty.USER_ID.name,
                                                                                                    userIdentifyElement.getElementProperties(),
                                                                                                    methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISTINGUISHED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                               OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                                                                               userIdentifyElement.getElementProperties(),
                                                                                                               methodName));

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }



    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param personElement  person profile element
     * @param departmentGUID primary team
     * @param locationGUID unique identifier of the location where the person is associated with (ProfileLocation)
     * @param organizationName display name of the organization - from traversing the team hierarchy
     */
    private void syncPerson(java.sql.Connection    databaseConnection,
                            RelatedMetadataElement personElement,
                            String                 departmentGUID,
                            String                 locationGUID,
                            String                 organizationName)
    {
        final String methodName = "syncUserIdentity";

        if (personElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.PERSON.getTableName(),
                                                                                            HarvestOpenMetadataColumn.PROFILE_GUID.getColumnName(),
                                                                                            personElement.getElement().getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.PERSON.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getPersonDataValues(personElement,
                                                                                         departmentGUID,
                                                                                         locationGUID,
                                                                                         organizationName);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.PERSON.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param personElement  person profile element
     * @param departmentGUID primary team
     * @param locationGUID unique identifier of the location where the person is associated with (ProfileLocation)
     * @param organizationName display name of the organization - from traversing the team hierarchy
     * @return columns
     */
    private Map<String, JDBCDataValue> getPersonDataValues(RelatedMetadataElement personElement,
                                                           String                 departmentGUID,
                                                           String                 locationGUID,
                                                           String                 organizationName)
    {
        final String methodName = "getPersonDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        if (personElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROFILE_GUID, personElement.getElement().getElementGUID());

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EMPLOYEE_NUMBER, propertyHelper.getStringProperty(connectorName,
                                                                                                                          OpenMetadataProperty.EMPLOYEE_NUMBER.name,
                                                                                                                          personElement.getElement().getElementProperties(),
                                                                                                                          methodName));

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PREFERRED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                         OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                         personElement.getElement().getElementProperties(),
                                                                                                                         methodName));

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESIDENT_COUNTRY, propertyHelper.getStringProperty(connectorName,
                                                                                                                           OpenMetadataProperty.RESIDENT_COUNTRY.name,
                                                                                                                           personElement.getElement().getElementProperties(),
                                                                                                                           methodName));

        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPARTMENT_GUID, departmentGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LOCATION_GUID, locationGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ORGANIZATION_NAME, organizationName);

        addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());

        return openMetadataRecord;
    }




    /**
     * Process a user identity and profile retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param userIdentifyElement user identity information
     * @param personElement  person profile element
     */
    private void syncUserToProfile(java.sql.Connection    databaseConnection,
                                   OpenMetadataElement    userIdentifyElement,
                                   RelatedMetadataElement personElement)
    {
        final String methodName = "syncUserToProfile";

        if (userIdentifyElement != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.USER_TO_PROFILE.getTableName(),
                                                                                            HarvestOpenMetadataColumn.RELATIONSHIP_GUID.getColumnName(),
                                                                                            userIdentifyElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.USER_TO_PROFILE.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getUserToProfileDataValues(userIdentifyElement, personElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.USER_TO_PROFILE.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * @param profileElement   profile element
     * @return columns
     */
    private Map<String, JDBCDataValue> getUserToProfileDataValues(OpenMetadataElement    userIdentifyElement,
                                                                  RelatedMetadataElement profileElement)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        if ((profileElement != null) && (userIdentifyElement != null))
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RELATIONSHIP_GUID, profileElement.getRelationshipGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROFILE_GUID, profileElement.getElement().getElementGUID());

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_IDENTITY_GUID, userIdentifyElement.getElementGUID());

            addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.START_TIMESTAMP, profileElement.getEffectiveFromTime());
            addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_TIMESTAMP, profileElement.getEffectiveToTime());

            addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());
        }


        return openMetadataRecord;
    }


    /**
     * Process a person profile retrieved from the open metadata ecosystem.
     *
     * @param databaseConnection connection to the database
     * @param personElement  person profile element
     */
    private void syncActorProfile(java.sql.Connection    databaseConnection,
                                  RelatedMetadataElement personElement)
    {
        final String methodName = "syncActorProfile";

        if ((personElement != null) )
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(databaseConnection,
                                                                                            HarvestOpenMetadataTable.ACTOR_PROFILE.getTableName(),
                                                                                            HarvestOpenMetadataColumn.PROFILE_GUID.getColumnName(),
                                                                                            personElement.getElement().getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.ACTOR_PROFILE.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getActorProfileDataValues(personElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.ACTOR_PROFILE.getTableName(), openMetadataRecord);
                }
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Convert a person profile from the open metadata ecosystem to database columns.
     *
     * @param personElement  person profile element
     * @return columns
     */
    private Map<String, JDBCDataValue> getActorProfileDataValues(RelatedMetadataElement personElement)
    {
        final String methodName = "getActorProfileDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        if (personElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROFILE_GUID, personElement.getElement().getElementGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, personElement.getElement().getType().getTypeName());

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                         personElement.getElement().getElementProperties(),
                                                                                                                         methodName));

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                       OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                       personElement.getElement().getElementProperties(),
                                                                                                                       methodName));

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, propertyHelper.getStringProperty(connectorName,
                                                                                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                                                                                      personElement.getElement().getElementProperties(),
                                                                                                                      methodName));

            addDateValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date());
        }

        return openMetadataRecord;
    }



    /**
     * Process information about a specific reference level.  They are just inserted into the database.  Duplicates are ignored.
     *
     * @param databaseConnection connection to the database
     * @param identifier unique identifier of the status identifier
     * @param classificationName type name of the governance action classification
     * @param displayName details of the certification type
     * @param text description of the status identifier
     */
    private void syncReferenceLevels(java.sql.Connection databaseConnection,
                                     int                 identifier,
                                     String              classificationName,
                                     String              displayName,
                                     String              text) throws ConnectorCheckedException
    {
        final String methodName = "syncReferenceLevels";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getReferenceLevelDataValues(identifier, classificationName, displayName, text);

            databaseClient.insertRowIntoTable(databaseConnection, HarvestOpenMetadataTable.REFERENCE_LEVEL.getTableName(), openMetadataRecord);
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                      error.getClass().getName(),
                                                                                                      methodName,
                                                                                                      error.getMessage()),
                                  error);

            throw new ConnectorCheckedException(HarvestOpenMetadataErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LEVEL_IDENTIFIER, identifier);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CLASSIFICATION_NAME, classificationName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, displayName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, text);

        return openMetadataRecord;
    }


    /**
     * Add the value and type to a record used to insert a row into a table.
     *
     * @param openMetadataRecord map containing the column details
     * @param column column definition
     * @param value value of the column
     */
    private void addValueToRow(Map<String, JDBCDataValue> openMetadataRecord,
                               HarvestOpenMetadataColumn  column,
                               Object                     value)
    {
        openMetadataRecord.put(column.getColumnName(), new JDBCDataValue(value, column.getColumnType().getJdbcType()));
    }


    /**
     * Add the value and type to a record used to insert a row into a table.
     *
     * @param openMetadataRecord map containing the column details
     * @param column column definition
     * @param value value of the column
     */
    private void addDateValueToRow(Map<String, JDBCDataValue> openMetadataRecord,
                                   HarvestOpenMetadataColumn  column,
                                   Date                       value)
    {
        if (value != null)
        {
            addValueToRow(openMetadataRecord, column, new java.sql.Timestamp(value.getTime()));
        }
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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

        if ((openMetadataRecord == null) || (openMetadataRecord.isEmpty()))
        {
            return false;
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
}
