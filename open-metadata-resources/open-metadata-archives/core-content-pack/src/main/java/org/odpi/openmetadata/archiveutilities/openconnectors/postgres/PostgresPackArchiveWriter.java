/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.postgres;

import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
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
                                               deployedImplementationType.getWikiLink());
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
        this.addPostgresDatabaseCatalogTemplate();
        this.addPostgresDatabaseSchemaCatalogTemplate();

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
                                                          GovernanceEngineDefinition.POSTGRES_SURVEY_ENGINE);
        
        this.createAndCatalogServerGovernanceActionProcess("PostgreSQLServer",
                                                           PostgresDeployedImplementationType.POSTGRESQL_SERVER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_POSTGRES_SERVER,
                                                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                           RequestTypeDefinition.CATALOG_POSTGRES_SERVER,
                                                           GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE);

        /*
         * Saving the GUIDs means tha the guids in the archive are stable between runs of the archive writer.
         */
        archiveHelper.saveGUIDs();
        archiveHelper.saveUsedGUIDs();
    }


    /**
     * Create a catalog template for a PostgreSQL database
     */
    private void addPostgresDatabaseCatalogTemplate()
    {
        final String                  guid     = "3d398b3f-7ae6-4713-952a-409f3dea8520";
        JDBCResourceConnectorProvider provider = new JDBCResourceConnectorProvider();

        List<PlaceholderPropertyType> placeholderPropertyTypes = PostgresPlaceholderProperty.getPostgresDatabasePlaceholderPropertyTypes();

        this.createDataAssetCatalogTemplate(guid,
                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE,
                                            PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_DESCRIPTION.getPlaceholder(),
                                            PlaceholderProperty.SERVER_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                                            provider.getConnectorType().getGUID(),
                                            "jdbc:postgresql://" +
                                                    PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                    PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                            null,
                                            null,
                                            placeholderPropertyTypes);
    }


    /**
     * Create a catalog template for a PostgreSQL database schema
     */
    private void addPostgresDatabaseSchemaCatalogTemplate()
    {
        final String guid = "82a5417c-d882-4271-8444-4c6a996a8bfc";

        JDBCResourceConnectorProvider provider = new JDBCResourceConnectorProvider();

        List<PlaceholderPropertyType>  placeholderPropertyTypes = PostgresPlaceholderProperty.getPostgresSchemaPlaceholderPropertyTypes();

        this.createDataAssetCatalogTemplate(guid,
                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                                            PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.SCHEMA_DESCRIPTION.getPlaceholder(),
                                            PlaceholderProperty.SERVER_NAME.getPlaceholder() + "." + PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_USER_ID.getPlaceholder(),
                                            PostgresPlaceholderProperty.DATABASE_PASSWORD.getPlaceholder(),
                                            provider.getConnectorType().getGUID(),
                                            "jdbc:postgresql://" +
                                                    PlaceholderProperty.HOST_IDENTIFIER.getPlaceholder() + ":" +
                                                    PlaceholderProperty.PORT_NUMBER.getPlaceholder() + "/" +
                                                    PostgresPlaceholderProperty.DATABASE_NAME.getPlaceholder() + "?currentSchema=" +
                                                    PostgresPlaceholderProperty.SCHEMA_NAME.getPlaceholder(),
                                            null,
                                            null,
                                            placeholderPropertyTypes);
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