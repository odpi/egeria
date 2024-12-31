/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.DataAssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SchemaAttributeElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.ffdc.HarvestOpenMetadataAuditCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.ffdc.HarvestOpenMetadataErrorCode;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.schema.HarvestOpenMetadataColumn;
import org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.schema.HarvestOpenMetadataTable;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.controls.JDBCConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLSchemaDDL;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.*;
import org.odpi.openmetadata.frameworks.governanceaction.search.*;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.catalog.connector.DataAssetExchangeService;
import org.odpi.openmetadata.integrationservices.catalog.connector.GlossaryExchangeService;

import java.util.*;


/**
 * Extracts relevant metadata from the open metadata ecosystem into the JDBC database.
 * The open metadata ecosystem is the home copy so its values will be pushed to the database. The database design matches the
 * beans returned by Asset Manager OMAS/Catalog Integrator OMIS.
 */
public class HarvestOpenMetadataCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private static final ObjectWriter OBJECT_WRITER = new ObjectMapper().writer();

    private       JDBCResourceConnector    databaseClient = null;
    private final DataAssetExchangeService dataAssetExchangeService;
    private final GlossaryExchangeService  glossaryExchangeService;
    private final OpenMetadataAccess       openMetadataAccess;



    /**
     * Constructor
     *
     * @param catalogTarget catalog target information
     * @param connectorToTarget connector to access the target resource
     * @param connectorName name of this integration connector
     * @param auditLog logging destination
     * @param dataAssetExchangeService access to data assets
     * @param glossaryExchangeService access to glossaries
     * @param openMetadataAccess access to open metadata
     * @throws ConnectorCheckedException error
     */
    public HarvestOpenMetadataCatalogTargetProcessor(CatalogTarget            catalogTarget,
                                                     Connector                connectorToTarget,
                                                     String                   connectorName,
                                                     AuditLog                 auditLog,
                                                     DataAssetExchangeService dataAssetExchangeService,
                                                     GlossaryExchangeService  glossaryExchangeService,
                                                     OpenMetadataAccess       openMetadataAccess) throws ConnectorCheckedException
    {
        super(catalogTarget, connectorToTarget, connectorName, auditLog);
        
        this.openMetadataAccess = openMetadataAccess;
        this.dataAssetExchangeService = dataAssetExchangeService;
        this.dataAssetExchangeService.setForLineage(true);
        this.dataAssetExchangeService.setForDuplicateProcessing(true);
        this.glossaryExchangeService = glossaryExchangeService;
        this.glossaryExchangeService.setForLineage(true);
        this.glossaryExchangeService.setForDuplicateProcessing(true);

        if (super.getCatalogTargetConnector() instanceof JDBCResourceConnector jdbcResourceConnector)
        {
            this.databaseClient = jdbcResourceConnector;
            String schemaName = super.getStringConfigurationProperty(JDBCConfigurationProperty.DATABASE_SCHEMA.getName(), catalogTarget.getConfigurationProperties());
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

        try
        {
            PostgreSQLSchemaDDL postgreSQLSchemaDDL = new PostgreSQLSchemaDDL(schemaName,
                                                                              "Observability data for a cohort of OMAG Servers.",
                                                                              HarvestOpenMetadataTable.getTables());
            jdbcResourceConnector.addDatabaseDefinitions(postgreSQLSchemaDDL.getDDLStatements());
        }
        catch (Exception error)
        {
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

        try
        {
            /*
             * Step through the catalogued metadata elements for each interesting type.  Start with data assets.
             */
            int startFrom = 0;

            List<DataAssetElement> dataAssetElements = dataAssetExchangeService.findDataAssets(".*",
                                                                                               startFrom,
                                                                                               openMetadataAccess.getMaxPagingSize(),
                                                                                               null);

            while (dataAssetElements != null)
            {
                for (DataAssetElement dataAssetElement : dataAssetElements)
                {
                    processDataAsset(dataAssetElement);
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                dataAssetElements = dataAssetExchangeService.findDataAssets(".*",
                                                                            startFrom,
                                                                            openMetadataAccess.getMaxPagingSize(),
                                                                            null);
            }

            startFrom = 0;
            List<GlossaryElement> glossaryElements = glossaryExchangeService.findGlossaries(".*",
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize(),
                                                                                            null);

            while (glossaryElements != null)
            {
                for (GlossaryElement glossaryElement : glossaryElements)
                {
                    processGlossary(glossaryElement);
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                glossaryElements = glossaryExchangeService.findGlossaries(".*",
                                                                          startFrom,
                                                                          openMetadataAccess.getMaxPagingSize(),
                                                                          null);
            }


            startFrom = 0;
            List<OpenMetadataElement> teamElements = openMetadataAccess.findMetadataElements(OpenMetadataType.TEAM.typeName,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             openMetadataAccess.getMaxPagingSize());

            while (teamElements != null)
            {
                for (OpenMetadataElement teamElement : teamElements)
                {
                    processTeam(teamElement);
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                teamElements = openMetadataAccess.findMetadataElements("Team",
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       openMetadataAccess.getMaxPagingSize());
            }


            startFrom = 0;
            List<OpenMetadataElement> toDoElements = openMetadataAccess.findMetadataElements(OpenMetadataType.TO_DO.typeName,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             openMetadataAccess.getMaxPagingSize());

            while (toDoElements != null)
            {
                for (OpenMetadataElement toDoElement : toDoElements)
                {
                    processToDo(toDoElement);
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                toDoElements = openMetadataAccess.findMetadataElements(OpenMetadataType.TO_DO.typeName,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       openMetadataAccess.getMaxPagingSize());
            }


            startFrom = 0;
            List<OpenMetadataElement> roleElements = openMetadataAccess.findMetadataElements(OpenMetadataType.PERSON_ROLE.typeName,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             startFrom,
                                                                                             openMetadataAccess.getMaxPagingSize());

            while (roleElements != null)
            {
                for (OpenMetadataElement roleElement : roleElements)
                {
                    syncRole(roleElement);
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                roleElements = openMetadataAccess.findMetadataElements(OpenMetadataType.PERSON_ROLE.typeName,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       startFrom,
                                                                       openMetadataAccess.getMaxPagingSize());
            }


            startFrom = 0;
            List<OpenMetadataElement> userIdentityElements = openMetadataAccess.findMetadataElements(OpenMetadataType.USER_IDENTITY.typeName,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     startFrom,
                                                                                                     openMetadataAccess.getMaxPagingSize());

            while (userIdentityElements != null)
            {
                for (OpenMetadataElement userIdentityElement : userIdentityElements)
                {
                    processUserIdentity(userIdentityElement);
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();

                userIdentityElements = openMetadataAccess.findMetadataElements(OpenMetadataType.USER_IDENTITY.typeName,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               startFrom,
                                                                               openMetadataAccess.getMaxPagingSize());
            }

            startFrom = 0;

            OpenMetadataRelationshipList personRoleAppointments = openMetadataAccess.findRelationshipsBetweenMetadataElements(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                                               null,
                                                                                                                               null,
                                                                                                                               null,
                                                                                                                               null,
                                                                                                                               null,
                                                                                                                               startFrom,
                                                                                                                               openMetadataAccess.getMaxPagingSize());

            while (personRoleAppointments != null)
            {
                for (OpenMetadataRelationship personRoleAppointment : personRoleAppointments.getElementList())
                {
                    if (personRoleAppointment != null)
                    {
                        syncRoleToUser(this.getUserIdentityForRole(personRoleAppointment.getElementGUIDAtEnd2(),
                                                                   personRoleAppointment.getElementGUIDAtEnd1()),
                                       personRoleAppointment);
                    }
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                personRoleAppointments = openMetadataAccess.findRelationshipsBetweenMetadataElements(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     null,
                                                                                                     startFrom,
                                                                                                     openMetadataAccess.getMaxPagingSize());
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
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
            RelatedMetadataElementList profileIdentities = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                                                           0,
                                                                                                           openMetadataAccess.getMaxPagingSize());


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
     * @param assetGUID unique identifier of the asset
     * @return the location (or null if there is not one)
     */
    private OpenMetadataElement getAssociatedAssetLocation(String assetGUID)
    {
        final String methodName = "getAssociatedAssetLocation";

        try
        {
            RelatedMetadataElementList relatedElements = openMetadataAccess.getRelatedMetadataElements(assetGUID,
                                                                                                         2,
                                                                                                         OpenMetadataType.ASSET_LOCATION_RELATIONSHIP.typeName,
                                                                                                         0,
                                                                                                         openMetadataAccess.getMaxPagingSize());

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
                        this.syncLocation(relatedMetadataElement.getElement());
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
     * @param profileGUID unique identifier of the profile
     * @return unique identifier of the location (or null if there is not one)
     */
    private String getAssociatedProfileLocation(String profileGUID)
    {
        final String methodName = "getAssociatedProfileLocation";

        try
        {
            RelatedMetadataElementList relatedElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                         1,
                                                                                                         OpenMetadataType.PROFILE_LOCATION_RELATIONSHIP.typeName,
                                                                                                         0,
                                                                                                         openMetadataAccess.getMaxPagingSize());

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
                        this.syncLocation(relatedMetadataElement.getElement());
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
     * @param elementGUID unique identifier of the asset
     * @return  the location (or null if there is not one)
     */
    private OpenMetadataElement getAssociatedLicense(String elementGUID)
    {
        final String methodName = "getAssociatedLicense";

        try
        {
            RelatedMetadataElementList relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                                         1,
                                                                                                         OpenMetadataType.REFERENCEABLE_TO_LICENSE_TYPE_NAME,
                                                                                                         0,
                                                                                                         openMetadataAccess.getMaxPagingSize());

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
                        this.syncLicense(relatedMetadataElement.getElement());
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
            RelatedMetadataElementList relatedElements = openMetadataAccess.getRelatedMetadataElements(assetGUID,
                                                                                                         1,
                                                                                                         OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                                                         startFrom,
                                                                                                         openMetadataAccess.getMaxPagingSize());

            /*
             * Return all attached tags.
             */
            if ((relatedElements != null) && (relatedElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedElements.getElementList())
                {
                    if (relatedMetadataElement != null)
                    {
                        String tagName = propertyHelper.getStringProperty(connectorName,
                                                                          OpenMetadataProperty.TAG_NAME.name,
                                                                          relatedMetadataElement.getElement().getElementProperties(),
                                                                          methodName);
                        tagStringBuilder.append(tagName).append(":");
                    }
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                relatedElements = openMetadataAccess.getRelatedMetadataElements(assetGUID,
                                                                                1,
                                                                                OpenMetadataType.ATTACHED_TAG_RELATIONSHIP.typeName,
                                                                                startFrom,
                                                                                openMetadataAccess.getMaxPagingSize());

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
            RelatedMetadataElementList relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                                         1,
                                                                                                         OpenMetadataType.SEMANTIC_ASSIGNMENT.typeName,
                                                                                                         0,
                                                                                                         openMetadataAccess.getMaxPagingSize());

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
            RelatedMetadataElementList relatedElements = openMetadataAccess.getRelatedMetadataElements(elementHeader.getGUID(),
                                                                                                         1,
                                                                                                         null,
                                                                                                         startFrom,
                                                                                                         openMetadataAccess.getMaxPagingSize());

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
                    else if (OpenMetadataType.CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME.equals(relationshipType))
                    {
                        syncCertification(elementHeader.getGUID(), relatedMetadataElement);
                    }
                    else if (OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName.equals(relationshipType))
                    {
                        processAssetSchemaType(elementHeader.getGUID(), elementQualifiedName, relatedMetadataElement);
                    }

                    if ((isAsset) && propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.ASSET.typeName))
                    {
                        syncRelatedAsset(openMetadataAccess.getRelationshipByGUID(relatedMetadataElement.getRelationshipGUID()));
                    }
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                relatedElements = openMetadataAccess.getRelatedMetadataElements(elementHeader.getGUID(),
                                                                                1,
                                                                                null,
                                                                                startFrom,
                                                                                openMetadataAccess.getMaxPagingSize());
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
            RelatedMetadataElementList relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                                         1,
                                                                                                         "AttachedComment",
                                                                                                         startFrom,
                                                                                                         openMetadataAccess.getMaxPagingSize());

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

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                relatedElements = openMetadataAccess.getRelatedMetadataElements(elementGUID,
                                                                                1,
                                                                                "AttachedComment",
                                                                                startFrom,
                                                                                openMetadataAccess.getMaxPagingSize());
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
            String managerGUID          = getTeamManagerGUID(teamElement.getElementGUID());

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
            RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(teamGUID,
                                                                                                                 2,
                                                                                                                 OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP.typeName,
                                                                                                                 0,
                                                                                                                 openMetadataAccess.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement personRoleElement : relatedMetadataElements.getElementList())
                {
                    if (personRoleElement != null)
                    {
                        RelatedMetadataElementList appointees = openMetadataAccess.getRelatedMetadataElements(teamGUID,
                                                                                                                  2,
                                                                                                                  OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                                  0,
                                                                                                                  openMetadataAccess.getMaxPagingSize());
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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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

            RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(userIdentityElement.getElementGUID(),
                                                                                                                 2,
                                                                                                                 OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                                                                 0,
                                                                                                                 openMetadataAccess.getMaxPagingSize());

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
     * @param toDoElement retrieved element
     */
    private void processToDo(OpenMetadataElement toDoElement)
    {
        final String methodName = "processToDo";

        try
        {
            RelatedMetadataElement toDoSourceElement = null;
            RelatedMetadataElement actionAssignmentElement = null;

            RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(toDoElement.getElementGUID(),
                                                                                                                 2,
                                                                                                                 OpenMetadataType.TO_DO_SOURCE_RELATIONSHIP.typeName,
                                                                                                                 0,
                                                                                                                 openMetadataAccess.getMaxPagingSize());

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

            relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(toDoElement.getElementGUID(),
                                                                                    2,
                                                                                    OpenMetadataType.ACTION_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                                    0,
                                                                                    openMetadataAccess.getMaxPagingSize());

            if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
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
            RelatedMetadataElementList roleElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                                      0,
                                                                                                      openMetadataAccess.getMaxPagingSize());

            if ((roleElements != null) && (roleElements.getElementList() != null))
            {
                for (RelatedMetadataElement roleElement : roleElements.getElementList())
                {
                    if (roleElement != null)
                    {
                        RelatedMetadataElementList teamElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                                                  1,
                                                                                                                  OpenMetadataType.TEAM_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                                                  0,
                                                                                                                  openMetadataAccess.getMaxPagingSize());

                        if (teamElements == null)
                        {
                            teamElements = openMetadataAccess.getRelatedMetadataElements(profileGUID,
                                                                                         1,
                                                                                         OpenMetadataType.TEAM_LEADERSHIP_RELATIONSHIP.typeName,
                                                                                         0,
                                                                                         openMetadataAccess.getMaxPagingSize());
                        }

                        if ((teamElements != null) && (teamElements.getElementList() != null))
                        {
                            for (RelatedMetadataElement team : teamElements.getElementList())
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
            RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(departmentGUID,
                                                                                                                 2,
                                                                                                                 OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                                                                                 0,
                                                                                                                 openMetadataAccess.getMaxPagingSize());

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
                                                                OpenMetadataProperty.NAME.name,
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
            RelatedMetadataElementList relatedMetadataElementList = openMetadataAccess.getRelatedMetadataElements(schemaType.getElement().getElementGUID(),
                                                                                                                    1,
                                                                                                                    null,
                                                                                                                    startFrom,
                                                                                                                    openMetadataAccess.getMaxPagingSize());
            while ((relatedMetadataElementList != null) && (relatedMetadataElementList.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElementList.getElementList())
                {
                    if (propertyHelper.isTypeOf(relatedMetadataElement.getElement(), OpenMetadataType.SCHEMA_TYPE.typeName))
                    {
                        processAssetSchemaType(assetGUID, assetQualifiedName, relatedMetadataElement);
                    }
                }


                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                relatedMetadataElementList = openMetadataAccess.getRelatedMetadataElements(schemaType.getElement().getElementGUID(),
                                                                                           1,
                                                                                           null,
                                                                                           startFrom,
                                                                                           openMetadataAccess.getMaxPagingSize());
            }

            /*
             * Now process schema attributes.
             */
            startFrom = 0;
            List<SchemaAttributeElement> schemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaType.getElement().getElementGUID(),
                                                                                                         startFrom,
                                                                                                         openMetadataAccess.getMaxPagingSize(),
                                                                                                         null);

            while (schemaAttributes != null)
            {
                processSchemaAttributes(assetGUID, assetQualifiedName, schemaAttributes);

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                schemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaType.getElement().getElementGUID(),
                                                                                startFrom,
                                                                                openMetadataAccess.getMaxPagingSize(),
                                                                                null);
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
                                                                                                         openMetadataAccess.getMaxPagingSize(),
                                                                                                         null);

            while (schemaAttributes != null)
            {
                processSchemaAttributes(assetGUID, assetQualifiedName, schemaAttributes);

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                schemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaType.getElement().getElementGUID(),
                                                                                startFrom,
                                                                                openMetadataAccess.getMaxPagingSize(),
                                                                                null);
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
                                                                                                                   openMetadataAccess.getMaxPagingSize(),
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
                        nestedStartFrom = nestedStartFrom + openMetadataAccess.getMaxPagingSize();
                        nestedSchemaAttributes = dataAssetExchangeService.getNestedAttributes(schemaAttribute.getElementHeader().getGUID(),
                                                                                              nestedStartFrom,
                                                                                              openMetadataAccess.getMaxPagingSize(),
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
            RelatedMetadataElementList reports = openMetadataAccess.getRelatedMetadataElements(dataFieldGUID,
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
                                  HarvestOpenMetadataAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
                                                                                                                      openMetadataAccess.getMaxPagingSize(),
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
                                                                                                                         openMetadataAccess.getMaxPagingSize(),
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

                            catTermsStartFrom = catTermsStartFrom + openMetadataAccess.getMaxPagingSize();

                            categorizedTerms = glossaryExchangeService.getTermsForGlossaryCategory(glossaryCategoryElement.getElementHeader().getGUID(),
                                                                                                   null,
                                                                                                   catTermsStartFrom,
                                                                                                   openMetadataAccess.getMaxPagingSize(),
                                                                                                   null);
                        }
                    }
                }

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                glossaryCategoryElements = glossaryExchangeService.getCategoriesForGlossary(glossary.getElementHeader().getGUID(),
                                                                                            startFrom,
                                                                                            openMetadataAccess.getMaxPagingSize(),
                                                                                            null);
            }

            startFrom = 0;

            List<GlossaryTermElement> glossaryTermElements = glossaryExchangeService.getTermsForGlossary(glossary.getElementHeader().getGUID(),
                                                                                                         startFrom,
                                                                                                         openMetadataAccess.getMaxPagingSize(),
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

                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                glossaryTermElements = glossaryExchangeService.getTermsForGlossary(glossary.getElementHeader().getGUID(),
                                                                                   startFrom,
                                                                                   openMetadataAccess.getMaxPagingSize(),
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
            RelatedMetadataElementList relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(glossaryTerm.getElementHeader().getGUID(),
                                                                                                                 0,
                                                                                                                 null,
                                                                                                                 startFrom,
                                                                                                                 openMetadataAccess.getMaxPagingSize());

            while ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
            {
                for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
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
                startFrom = startFrom + openMetadataAccess.getMaxPagingSize();
                relatedMetadataElements = openMetadataAccess.getRelatedMetadataElements(glossaryTerm.getElementHeader().getGUID(),
                                                                                        0,
                                                                                        null,
                                                                                        startFrom,
                                                                                        openMetadataAccess.getMaxPagingSize());
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
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.ASSET.getTableName(),
                                                                                        HarvestOpenMetadataColumn.ASSET_GUID.getColumnName(),
                                                                                        dataAssetElement.getElementHeader().getGUID(),
                                                                                        HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                        HarvestOpenMetadataTable.ASSET.getColumnNameTypeMap());

            Map<String, JDBCDataValue> openMetadataRecord = this.getAssetDataValues(dataAssetElement,
                                                                                    associatedResourceLocationGUID,
                                                                                    associatedLicenceGUID,
                                                                                    tags,
                                                                                    semanticAssignmentTermGUID);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
            {
                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.ASSET.getTableName(), openMetadataRecord);
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
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSET_GUID, elementHeader.getGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, elementHeader.getType().getTypeName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.METADATA_COLLECTION_ID, elementHeader.getOrigin().getHomeMetadataCollectionId());


            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_BY, elementHeader.getVersions().getCreatedBy());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()));
            if (elementHeader.getVersions().getUpdateTime() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, new java.sql.Timestamp(elementHeader.getVersions().getUpdateTime().getTime()));
            }
            else
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()));
            }
            if (elementHeader.getVersions().getUpdatedBy() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATED_BY, elementHeader.getVersions().getUpdatedBy());
            }
            else
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATED_BY, elementHeader.getVersions().getCreatedBy());
            }

            if ((elementHeader.getVersions().getMaintainedBy() != null) && (! elementHeader.getVersions().getMaintainedBy().isEmpty()))
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


            if (elementHeader.getClassifications() != null)
            {
                for (ElementClassification classification : elementHeader.getClassifications())
                {
                    if (OpenMetadataType.ASSET_ZONES_CLASSIFICATION_NAME.equals(classification.getClassificationName()))
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ZONE_NAMES, this.getZoneNames(classification.getClassificationProperties()));
                    }
                    else if (OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_GUID, this.getOpenMetadataStringProperty(classification.getClassificationProperties(), OpenMetadataProperty.OWNER.name, 80));
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_TYPE_NAME, this.getOpenMetadataStringProperty(classification.getClassificationProperties(), OpenMetadataProperty.OWNER_TYPE_NAME.name, 40));
                    }
                    else if (OpenMetadataType.ASSET_ORIGIN_CLASSIFICATION_NAME.equals(classification.getClassificationName()))
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ORIGIN_ORG_GUID, this.getOpenMetadataStringProperty(classification.getClassificationProperties(),OpenMetadataProperty.ORGANIZATION.name, 80));
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ORIGIN_BIZ_CAP_GUID, this.getOpenMetadataStringProperty(classification.getClassificationProperties(), "businessCapability", 80));
                    }
                    else if (OpenMetadataType.MEMENTO_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ARCHIVED, classification.getVersions().getCreateTime());
                    }
                    else if (OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        this.getStatusIdentifier(classification.getClassificationProperties());
                    }
                    else if (OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        this.getStatusIdentifier(classification.getClassificationProperties());
                    }
                    else if (OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                    {
                        this.getStatusIdentifier(classification.getClassificationProperties());
                    }
                }
            }

        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL, confidentiality);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CRITICALITY_LEVEL, criticality);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENCE_LEVEL, confidence);


        if (dataAssetProperties != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESOURCE_NAME,        dataAssetProperties.getResourceName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESOURCE_DESCRIPTION, dataAssetProperties.getResourceDescription());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION_IDENTIFIER,           dataAssetProperties.getVersionIdentifier());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME,         dataAssetProperties.getDisplayName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_DESCRIPTION,  dataAssetProperties.getDisplayDescription());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME,       dataAssetProperties.getQualifiedName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_SUMMARY,      dataAssetProperties.getDisplaySummary());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ABBREVIATION,              dataAssetProperties.getAbbreviation());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USAGE,               dataAssetProperties.getUsage());
            if (dataAssetProperties.getAdditionalProperties() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES, dataAssetProperties.getAdditionalProperties().toString());
            }
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LICENSE_TYPE_GUID, associatedLicenceGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESOURCE_LOCATION_GUID, associatedResourceLocationGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TAGS, tags);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SEMANTIC_TERM_GUID, semanticAssignmentTermGUID);

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));

        return openMetadataRecord;
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

            databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.CORRELATION_PROPERTIES.getTableName(), openMetadataRecord);
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
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EGERIA_OWNED, (elementHeader.getOrigin().getOriginCategory() != ElementOriginCategory.EXTERNAL_SOURCE));
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ELEMENT_GUID, elementHeader.getGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_SCOPE_GUID, elementHeader.getOrigin().getHomeMetadataCollectionId());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, elementHeader.getType().getTypeName());
        }

        if (metadataCorrelationHeader != null)
        {
            if (metadataCorrelationHeader.getExternalInstanceCreatedBy() != null)
            {
                syncUserId(metadataCorrelationHeader.getExternalInstanceCreatedBy(),
                           metadataCorrelationHeader.getExternalScopeGUID(),
                           this.getAssociatedUserIdentity(metadataCorrelationHeader.getExternalInstanceCreatedBy()));
            }
            if (metadataCorrelationHeader.getExternalInstanceLastUpdatedBy() != null)
            {
                syncUserId(metadataCorrelationHeader.getExternalInstanceLastUpdatedBy(),
                           metadataCorrelationHeader.getExternalScopeGUID(),
                           this.getAssociatedUserIdentity(metadataCorrelationHeader.getExternalInstanceLastUpdatedBy()));
            }
            if (metadataCorrelationHeader.getLastSynchronized() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_CONFIRMED_SYNC_TIME, new java.sql.Timestamp(metadataCorrelationHeader.getLastSynchronized().getTime()));
            }
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_IDENTIFIER, metadataCorrelationHeader.getExternalIdentifier());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATED_BY, metadataCorrelationHeader.getExternalInstanceLastUpdatedBy());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, metadataCorrelationHeader.getExternalInstanceLastUpdateTime());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_BY, metadataCorrelationHeader.getExternalInstanceCreatedBy());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION, metadataCorrelationHeader.getExternalInstanceVersion());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, metadataCorrelationHeader.getExternalInstanceCreationTime());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ADDITIONAL_PROPERTIES, metadataCorrelationHeader.getMappingProperties().toString());
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

                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.METADATA_COLLECTION.getTableName(), openMetadataRecord);
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

                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.ASSET_TYPE.getTableName(), openMetadataRecord);
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
                                                                                                                            OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                            location.getElementProperties(),
                                                                                                                            methodName),
                                                                                           location.getType().getTypeName());

                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.LOCATION.getTableName(), openMetadataRecord);
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

                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.LICENSE_TYPE.getTableName(), openMetadataRecord);
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
    private Map<String, JDBCDataValue> getLicenseDataValues(String licenseGUID,
                                                            String licenseName,
                                                            String licenseDescription)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LICENSE_TYPE_GUID, licenseGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LICENSE_NAME, licenseName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, licenseDescription);

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

        if ((numberOfComments > 0) || (numberOfRatings > 0) || (numberOfTags > 0) || (numberOfLikes > 0))
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.COLLABORATION_ACTIVITY.getTableName(),
                                                                                            HarvestOpenMetadataColumn.ELEMENT_GUID.getColumnName(),
                                                                                            elementGUID,
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.COLLABORATION_ACTIVITY.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getCollaborationActivityDataValues(elementGUID,
                                                                                                        numberOfComments,
                                                                                                        numberOfRatings,
                                                                                                        aveRating,
                                                                                                        numberOfTags,
                                                                                                        numberOfLikes);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.COLLABORATION_ACTIVITY.getTableName(), openMetadataRecord);
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ELEMENT_GUID, elementGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_COMMENTS, numberOfComments);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_RATINGS, numberOfRatings);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.AVG_RATING, averageRating);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_TAGS, numberOfTags);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_LIKES, numberOfLikes);

        return openMetadataRecord;
    }


    /**
     * Process any userIds found in the element header.  These maybe from archives and so may not have an associated user identity entity.
     *
     * @param elementVersions details of the people involved in each version of the element
     * @param elementOrigin details of the metadata source
     */
    private void processUserIds(ElementVersions elementVersions,
                                ElementOrigin elementOrigin)
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
                                                                                               null,
                                                                                               0,
                                                                                               openMetadataAccess.getMaxPagingSize());

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

                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.EXTERNAL_USER.getTableName(), openMetadataRecord);
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.METADATA_COLLECTION_ID, metadataCollectionId);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EXTERNAL_USER_ID, userId);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_IDENTITY_GUID, userIdentityGUID);

        if (startDate != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.START_TIMESTAMP, new java.sql.Timestamp(startDate.getTime()));
        }

        if (endDate != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_TIMESTAMP, new java.sql.Timestamp(endDate.getTime()));
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

                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.CERTIFICATION.getTableName(),
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
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.CERTIFICATION.getTableName(), openMetadataRecord);
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

        if (startDate != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.START_TIMESTAMP, new java.sql.Timestamp(startDate.getTime()));
        }

        if (endDate != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_TIMESTAMP, new java.sql.Timestamp(endDate.getTime()));
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

                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.CERTIFICATION_TYPE.getTableName(), openMetadataRecord);
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
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.CONTRIBUTION.getTableName(),
                                                                                            HarvestOpenMetadataColumn.USER_IDENTITY_GUID.getColumnName(),
                                                                                            userGUID,
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.CONTRIBUTION.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getContributionDataValues(userGUID,
                                                                                               propertyHelper.getLongProperty(connectorName,
                                                                                                                               "karmaPoints",
                                                                                                                               contribution.getElement().getElementProperties(),
                                                                                                                               methodName));

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.CONTRIBUTION.getTableName(), openMetadataRecord);
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
     * @param userGUID unique identifier of the attached element
     * @param karmaPoints amount of activity from user
     * @return columns
     */
    private Map<String, JDBCDataValue> getContributionDataValues(String userGUID,
                                                                 long   karmaPoints)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_IDENTITY_GUID, userGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.KARMA_POINTS, karmaPoints);

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
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.DATA_FIELD.getTableName(),
                                                                                            HarvestOpenMetadataColumn.DATA_FIELD_GUID.getColumnName(),
                                                                                            schemaAttributeElement.getElementHeader().getGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.DATA_FIELD.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getDataFieldDataValues(assetGUID,
                                                                                            assetQualifiedName,
                                                                                            glossaryTermGUID,
                                                                                            hasProfile,
                                                                                            schemaAttributeElement);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.DATA_FIELD.getTableName(), openMetadataRecord);
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DATA_FIELD_GUID, schemaAttributeElement.getElementHeader().getGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DATA_FIELD_NAME, schemaAttributeElement.getSchemaAttributeProperties().getDisplayName());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION_IDENTIFIER, Long.toString(schemaAttributeElement.getElementHeader().getVersions().getVersion()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SEMANTIC_TERM_GUID, glossaryTermGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.HAS_PROFILE, hasProfile);
        if (schemaAttributeElement.getElementHeader().getClassifications() != null)
        {
            for (ElementClassification classification : schemaAttributeElement.getElementHeader().getClassifications())
            {
                if (OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName.equals(classification.getClassificationName()))
                {
                    addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL, this.getStatusIdentifier(classification.getClassificationProperties()));
                }
            }
        }
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, assetQualifiedName);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSET_GUID, assetGUID);

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
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.DEPARTMENT.getTableName(),
                                                                                            HarvestOpenMetadataColumn.DEPARTMENT_GUID.getColumnName(),
                                                                                            department.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.DEPARTMENT.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getDepartmentDataValues(parentDepartmentGUID, manager, department);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.DEPARTMENT.getTableName(), openMetadataRecord);
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPARTMENT_GUID, department.getElementGUID());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPARTMENT_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                                  OpenMetadataProperty.NAME.name,
                                                                                                                  department.getElementProperties(),
                                                                                                                  methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.MANAGER_PROFILE_GUID, manager);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PARENT_DEPARTMENT_GUID, parentDepartmentGUID);

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

            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.GLOSSARY.getTableName(),
                                                                                        HarvestOpenMetadataColumn.GLOSSARY_GUID.getColumnName(),
                                                                                        glossaryElement.getElementHeader().getGUID(),
                                                                                        HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                        HarvestOpenMetadataTable.GLOSSARY.getColumnNameTypeMap());

            Map<String, JDBCDataValue> openMetadataRecord = this.getGlossaryDataValues(glossaryElement,
                                                                                       numberOfTerms,
                                                                                       numberOfCategories,
                                                                                       numberOfLinkedTerms,
                                                                                       licenseGUID);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
            {
                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.GLOSSARY.getTableName(), openMetadataRecord);
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

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.GLOSSARY_GUID, elementHeader.getGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.METADATA_COLLECTION_ID, elementHeader.getOrigin().getHomeMetadataCollectionId());


            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()));
            if (elementHeader.getVersions().getUpdateTime() != null)
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, new java.sql.Timestamp(elementHeader.getVersions().getUpdateTime().getTime()));
            }
            else
            {
                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_UPDATE_TIME, new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()));
            }

            if ((elementHeader.getClassifications() != null) && (! elementHeader.getClassifications().isEmpty()))
            {
                StringBuilder classifications = new StringBuilder(":");

                for (ElementClassification classification : elementHeader.getClassifications())
                {
                    classifications.append(classification.getClassificationName()).append(":");

                    if (classification.getClassificationName().equals(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName))
                    {
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_GUID, this.getOpenMetadataStringProperty(classification.getClassificationProperties(), OpenMetadataProperty.OWNER.name, 80));
                        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_TYPE_NAME, this.getOpenMetadataStringProperty(classification.getClassificationProperties(), OpenMetadataProperty.OWNER_TYPE_NAME.name, 40));
                    }
                }

                addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CLASSIFICATIONS, classifications.toString());
            }
        }

        if (glossaryProperties != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, glossaryProperties.getDisplayName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DESCRIPTION, glossaryProperties.getDescription());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.GLOSSARY_LANGUAGE, glossaryProperties.getLanguage());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, glossaryProperties.getQualifiedName());
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

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));

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
            Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.TERM_ACTIVITY.getTableName(),
                                                                                        HarvestOpenMetadataColumn.TERM_GUID.getColumnName(),
                                                                                        glossaryTermElement.getElementHeader().getGUID(),
                                                                                        HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                        HarvestOpenMetadataTable.TERM_ACTIVITY.getColumnNameTypeMap());

            Map<String, JDBCDataValue> openMetadataRecord = this.getTermActivityDataValues(glossaryTermElement,
                                                                                           glossaryGUID,
                                                                                           lastFeedbackTime,
                                                                                           numberOfLinkedElements,
                                                                                           lastLinkTime);

            if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
            {
                databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.TERM_ACTIVITY.getTableName(), openMetadataRecord);
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

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TERM_GUID, elementHeader.getGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, new java.sql.Timestamp(elementHeader.getVersions().getCreateTime().getTime()));

            if (elementHeader.getClassifications() != null)
            {

                for (ElementClassification classification : elementHeader.getClassifications())
                {
                    switch (classification.getClassificationName())
                    {
                        case "Ownership" ->
                        {
                            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_GUID,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(), OpenMetadataProperty.OWNER.name,
                                                                                                  80));
                            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OWNER_TYPE_NAME,
                                                               this.getOpenMetadataStringProperty(classification.getClassificationProperties(),
                                                                                                  OpenMetadataProperty.OWNER_TYPE_NAME.name, 40));
                        }
                        case "Confidentiality" -> confidentiality = this.getStatusIdentifier(classification.getClassificationProperties());
                        case "Confidence" -> confidence = this.getStatusIdentifier(classification.getClassificationProperties());
                        case "Criticality" -> criticality = this.getStatusIdentifier(classification.getClassificationProperties());
                    }
                }
            }
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENTIALITY_LEVEL, confidentiality);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CRITICALITY_LEVEL, criticality);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CONFIDENCE_LEVEL, confidence);


        if (glossaryTermProperties != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TERM_NAME, glossaryTermProperties.getDisplayName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, glossaryTermProperties.getQualifiedName());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TERM_SUMMARY, glossaryTermProperties.getSummary());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.VERSION_IDENTIFIER, glossaryTermProperties.getPublishVersionIdentifier());
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.GLOSSARY_GUID, glossaryGUID);
        if (lastFeedbackTime != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_FEEDBACK_TIME, new java.sql.Timestamp(lastFeedbackTime.getTime()));
        }
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.NUM_LINKED_ELEMENTS, numberOfLinkedElements);
        if (lastLinkTime != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_LINKED_TIME, new java.sql.Timestamp(lastLinkTime.getTime()));
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));

        return openMetadataRecord;
    }


    /**
     * Process an asset relationship retrieved from the open metadata ecosystem.
     *
     * @param relatedAsset relationship information
     */
    private void syncRelatedAsset(OpenMetadataRelationship relatedAsset)
    {
        final String methodName = "syncRelatedAsset";

        if (relatedAsset != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.RELATED_ASSET.getTableName(),
                                                                                            HarvestOpenMetadataColumn.RELATIONSHIP_GUID.getColumnName(),
                                                                                            relatedAsset.getRelationshipGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.RELATED_ASSET.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getRelatedAssetDataValues(relatedAsset);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.RELATED_ASSET.getTableName(), openMetadataRecord);
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
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));

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
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.ROLE.getTableName(),
                                                                                            HarvestOpenMetadataColumn.ROLE_GUID.getColumnName(),
                                                                                            role.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.ROLE.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getRoleDataValues(role);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.ROLE.getTableName(), openMetadataRecord);
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
                                                                                                      OpenMetadataProperty.NAME.name,
                                                                                                      openMetadataElement.getElementProperties(),
                                                                                                      methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, openMetadataElement.getType().getTypeName());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.HEADCOUNT, propertyHelper.getIntProperty(connectorName,
                                                                                                    OpenMetadataProperty.HEAD_COUNT.name,
                                                                                                    openMetadataElement.getElementProperties(),
                                                                                                    methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));

        return openMetadataRecord;
    }



    /**
     * Process an PersonRoleAppointment relationship retrieved from the open metadata ecosystem.
     *
     * @param userGUID unique identifier of associated user identify
     * @param personRoleAppointment relationship information
     */
    private void syncRoleToUser(String                   userGUID,
                                OpenMetadataRelationship personRoleAppointment)
    {
        final String methodName = "syncRoleToUser";

        if (personRoleAppointment != null)
        {
            try
            {
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.ROLE_TO_PROFILE.getTableName(),
                                                                                            HarvestOpenMetadataColumn.RELATIONSHIP_GUID.getColumnName(),
                                                                                            personRoleAppointment.getRelationshipGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.ROLE_TO_PROFILE.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getRoleToUserDataValues(userGUID, personRoleAppointment);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.ROLE_TO_PROFILE.getTableName(), openMetadataRecord);
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
    private Map<String, JDBCDataValue> getRoleToUserDataValues(String                   userGUID,
                                                               OpenMetadataRelationship personRoleAppointment)
    {
        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ROLE_GUID, personRoleAppointment.getElementGUIDAtEnd2());
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_IDENTITY_GUID, userGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RELATIONSHIP_GUID, personRoleAppointment.getRelationshipGUID());

        if (personRoleAppointment.getEffectiveFromTime() != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.START_TIMESTAMP, new java.sql.Timestamp(personRoleAppointment.getEffectiveFromTime().getTime()));
        }

        if (personRoleAppointment.getEffectiveToTime() != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.END_TIMESTAMP, new java.sql.Timestamp(personRoleAppointment.getEffectiveToTime().getTime()));
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));

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
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.TO_DO.getTableName(),
                                                                                            HarvestOpenMetadataColumn.TO_DO_GUID.getColumnName(),
                                                                                            toDoElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.TO_DO.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getToDoDataValues(toDoElement,
                                                                                       toDoSourceElements,
                                                                                       actionAssignmentElements);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.TO_DO.getTableName(), openMetadataRecord);
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
     * @param actionAssignmentElement actor responsible for completing the work - may be null
     * @return columns
     */
    private Map<String, JDBCDataValue> getToDoDataValues(OpenMetadataElement    toDoElement,
                                                         RelatedMetadataElement toDoSourceElement,
                                                         RelatedMetadataElement actionAssignmentElement)
    {
        final String methodName = "getToDoDataValues";

        Map<String, JDBCDataValue> openMetadataRecord = new HashMap<>();

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TO_DO_GUID, toDoElement.getElementGUID());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.QUALIFIED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                           toDoElement.getElementProperties(),
                                                                                                           methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISPLAY_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                         OpenMetadataProperty.NAME.name,
                                                                                                         toDoElement.getElementProperties(),
                                                                                                         methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.CREATION_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                        OpenMetadataProperty.CREATION_TIME.name,
                                                                                                        toDoElement.getElementProperties(),
                                                                                                        methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.OPEN_METADATA_TYPE, propertyHelper.getStringProperty(connectorName,
                                                                                                      OpenMetadataProperty.TO_DO_TYPE.name,
                                                                                                      toDoElement.getElementProperties(),
                                                                                                      methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PRIORITY, propertyHelper.getIntProperty(connectorName,
                                                                                                   OpenMetadataProperty.PRIORITY.name,
                                                                                                   toDoElement.getElementProperties(),
                                                                                                   methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DUE_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                   OpenMetadataProperty.DUE_TIME.name,
                                                                                                   toDoElement.getElementProperties(),
                                                                                                   methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LAST_REVIEW_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                            OpenMetadataProperty.LAST_REVIEW_TIME.name,
                                                                                                            toDoElement.getElementProperties(),
                                                                                                            methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.COMPLETION_TIME, propertyHelper.getDateProperty(connectorName,
                                                                                                          OpenMetadataProperty.COMPLETION_TIME.name,
                                                                                                          toDoElement.getElementProperties(),
                                                                                                          methodName));

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TO_DO_STATUS, propertyHelper.getEnumPropertySymbolicName(connectorName,
                                                                                                              OpenMetadataProperty.TO_DO_STATUS.name,
                                                                                                              toDoElement.getElementProperties(),
                                                                                                              methodName));

        if (toDoSourceElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TO_DO_SOURCE_GUID, toDoSourceElement.getElement().getElementGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.TO_DO_SOURCE_TYPE, toDoSourceElement.getElement().getType().getTypeName());
        }

        if (actionAssignmentElement != null)
        {
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSIGNED_ACTOR_GUID, actionAssignmentElement.getElement().getElementGUID());
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ASSIGNED_ACTOR_TYPE, actionAssignmentElement.getElement().getType().getTypeName());
        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new Date().getTime());

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
                Map<String, JDBCDataValue> latestStoredRecord = databaseClient.getLatestRow(HarvestOpenMetadataTable.USER_IDENTITY.getTableName(),
                                                                                            HarvestOpenMetadataColumn.USER_IDENTITY_GUID.getColumnName(),
                                                                                            userIdentifyElement.getElementGUID(),
                                                                                            HarvestOpenMetadataColumn.SYNC_TIME.getColumnName(),
                                                                                            HarvestOpenMetadataTable.USER_IDENTITY.getColumnNameTypeMap());

                Map<String, JDBCDataValue> openMetadataRecord = this.getUserIdentityDataValues(userIdentifyElement,
                                                                                               personElement,
                                                                                               departmentGUID,
                                                                                               locationGUID,
                                                                                               organizationName);

                if (this.newInformation(latestStoredRecord, openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME.getColumnName()))
                {
                    databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.USER_IDENTITY.getTableName(), openMetadataRecord);
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
            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PROFILE_GUID, personElement.getElement().getElementGUID());

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.EMPLOYEE_NUMBER, propertyHelper.getStringProperty(connectorName,
                                                                                                                "employeeNumber",
                                                                                                                personElement.getElement().getElementProperties(),
                                                                                                                methodName));

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.PREFERRED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                               "name",
                                                                                                               personElement.getElement().getElementProperties(),
                                                                                                               methodName));

            addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.RESIDENT_COUNTRY, propertyHelper.getStringProperty(connectorName,
                                                                                                                 "residentCountry",
                                                                                                                 personElement.getElement().getElementProperties(),
                                                                                                                 methodName));

        }

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_IDENTITY_GUID, userIdentifyElement.getElementGUID());

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.USER_ID, propertyHelper.getStringProperty(connectorName,
                                                                                                    "userId",
                                                                                                    userIdentifyElement.getElementProperties(),
                                                                                                    methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DISTINGUISHED_NAME, propertyHelper.getStringProperty(connectorName,
                                                                                                               "distinguishedName",
                                                                                                               userIdentifyElement.getElementProperties(),
                                                                                                               methodName));
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.DEPARTMENT_GUID, departmentGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.LOCATION_GUID, locationGUID);
        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.ORGANIZATION_NAME, organizationName);

        addValueToRow(openMetadataRecord, HarvestOpenMetadataColumn.SYNC_TIME, new java.sql.Timestamp(new Date().getTime()));

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
                                     String text) throws ConnectorCheckedException
    {
        final String methodName = "syncReferenceLevels";

        try
        {
            Map<String, JDBCDataValue> openMetadataRecord = this.getReferenceLevelDataValues(identifier, classificationName, displayName, text);

            databaseClient.insertRowIntoTable(HarvestOpenMetadataTable.REFERENCE_LEVEL.getTableName(), openMetadataRecord);
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
