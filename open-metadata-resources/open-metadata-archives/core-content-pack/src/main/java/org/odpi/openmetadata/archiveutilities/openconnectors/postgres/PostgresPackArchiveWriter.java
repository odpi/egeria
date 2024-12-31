/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.postgres;

import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresPlaceholderProperty;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.ContentPackDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.GovernanceEngineDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.IntegrationGroupDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.RequestTypeDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.Date;
import java.util.List;

/**
 * PostgresPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all PostgreSQL connectors supplied by the egeria project.
 */
public class PostgresPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public PostgresPackArchiveWriter()
    {
        super(ContentPackDefinition.POSTGRES_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.POSTGRES_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.POSTGRES_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.POSTGRES_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Add valid metadata values for deployedImplementationType.  The GUIDs are saved in a look-up map
         * to make it easy to link other elements to these valid values later.
         */
        for (PostgresDeployedImplementationType deployedImplementationType : PostgresDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType.getDeployedImplementationType(),
                                               deployedImplementationType.getAssociatedTypeName(),
                                               deployedImplementationType.getQualifiedName(),
                                               deployedImplementationType.getCategory(),
                                               deployedImplementationType.getDescription(),
                                               deployedImplementationType.getWikiLink(),
                                               deployedImplementationType.getIsATypeOf());
        }

        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(null, new PostgresServerIntegrationProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.POSTGRES_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.POSTGRES_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.POSTGRES_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.POSTGRES_CONTENT_PACK,
                                       IntegrationGroupDefinition.POSTGRES);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.POSTGRES_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.POSTGRES_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.POSTGRES_CONTENT_PACK);

        /*
         * Create a sample process
         */
        this.createAndSurveyServerGovernanceActionProcess("PostgreSQLServer",
                                                          PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_POSTGRES_SERVER,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          RequestTypeDefinition.SURVEY_POSTGRES_SERVER,
                                                          GovernanceEngineDefinition.POSTGRES_SURVEY_ENGINE,
                                                          PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName());
        
        this.createAndCatalogServerGovernanceActionProcess("PostgreSQLServer",
                                                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_POSTGRES_SERVER,
                                                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                           RequestTypeDefinition.CATALOG_POSTGRES_SERVER,
                                                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName());

        this.deleteAsCatalogTargetGovernanceActionProcess("PostgreSQLServer",
                                                          PostgresDeployedImplementationType.POSTGRESQL_SERVER.getAssociatedTypeName(),
                                                          PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_POSTGRES_SERVER,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          PostgresDeployedImplementationType.POSTGRESQL_SERVER.getQualifiedName());

        this.createAndSurveyServerGovernanceActionProcess("PostgreSQLDatabase",
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_POSTGRES_DB,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          RequestTypeDefinition.SURVEY_POSTGRES_DATABASE,
                                                          GovernanceEngineDefinition.POSTGRES_SURVEY_ENGINE,
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName());

        this.createAndCatalogAssetGovernanceActionProcess("PostgreSQLDatabase",
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName(),
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_POSTGRES_DB,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          RequestTypeDefinition.CATALOG_POSTGRES_DATABASE,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName());

        this.deleteAsCatalogTargetGovernanceActionProcess("PostgreSQLDatabase",
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getAssociatedTypeName(),
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_POSTGRES_DB,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE.getQualifiedName());

        this.createAndCatalogAssetGovernanceActionProcess("PostgreSQLDatabaseSchema",
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName(),
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_POSTGRES_SCHEMA,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          RequestTypeDefinition.CATALOG_POSTGRES_SCHEMA,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName());

        this.deleteAsCatalogTargetGovernanceActionProcess("PostgreSQLDatabaseSchema",
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName(),
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_POSTGRES_SCHEMA,
                                                          GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                          PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName());

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * Main program to initiate the archive writer for the connector types for data store connectors supported by
     * the Egeria project.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            PostgresPackArchiveWriter archiveWriter = new PostgresPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}