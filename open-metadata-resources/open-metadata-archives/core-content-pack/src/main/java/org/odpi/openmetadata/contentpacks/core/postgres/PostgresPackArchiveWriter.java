/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.postgres;

import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls.KafkaAnnotationType;
import org.odpi.openmetadata.adapters.connectors.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetCollectionProvider;
import org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetProvider;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.SoftwareServerTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.ArrayList;
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
            this.addDeployedImplementationType(deployedImplementationType);
        }

        /*
         * Integration Connector Types will link to the deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(new PostgresServerIntegrationProvider());

        /*
         * Set up the connector types for resource connectors introduced by this content pack
         */
        archiveHelper.addConnectorType(new PostgresTabularDataSetProvider());
        archiveHelper.addConnectorType(new PostgresTabularDataSetCollectionProvider());


        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.POSTGRES_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.POSTGRES_CONTENT_PACK);
        this.addTabularDataSetCatalogTemplates(ContentPackDefinition.POSTGRES_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.POSTGRES_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.POSTGRES_CONTENT_PACK);

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
         * Create helper processes
         */
        List<String> additionalSolutionComponents = new ArrayList<>();

        String solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("PostgreSQLServer",
                                                                                         PostgresDeployedImplementationType.POSTGRESQL_SERVER,
                                                                                         "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                         RequestTypeDefinition.CREATE_POSTGRES_SERVER,
                                                                                         SoftwareServerTemplateDefinition.POSTGRES_SERVER_TEMPLATE,
                                                                                         RequestTypeDefinition.SURVEY_POSTGRES_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogServerGovernanceActionProcess("PostgreSQLServer",
                                                                                   PostgresDeployedImplementationType.POSTGRESQL_SERVER,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                   RequestTypeDefinition.CREATE_POSTGRES_SERVER,
                                                                                   SoftwareServerTemplateDefinition.POSTGRES_SERVER_TEMPLATE,
                                                                                   RequestTypeDefinition.CATALOG_POSTGRES_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("PostgreSQLServer",
                                                                                  PostgresDeployedImplementationType.POSTGRESQL_SERVER,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                  RequestTypeDefinition.DELETE_POSTGRES_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("PostgreSQLDatabase",
                                                                                  PostgresDeployedImplementationType.POSTGRESQL_DATABASE,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                  RequestTypeDefinition.CREATE_POSTGRES_DB,
                                                                                  PostgreSQLTemplateType.POSTGRES_DATABASE_TEMPLATE,
                                                                                  RequestTypeDefinition.SURVEY_POSTGRES_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogAssetGovernanceActionProcess("PostgreSQLDatabase",
                                                                                  PostgresDeployedImplementationType.POSTGRESQL_DATABASE,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                  RequestTypeDefinition.CREATE_POSTGRES_DB,
                                                                                  PostgreSQLTemplateType.POSTGRES_DATABASE_TEMPLATE,
                                                                                  RequestTypeDefinition.CATALOG_POSTGRES_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("PostgreSQLDatabase",
                                                                                  PostgresDeployedImplementationType.POSTGRESQL_DATABASE,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                  RequestTypeDefinition.DELETE_POSTGRES_DB);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogAssetGovernanceActionProcess("PostgreSQLDatabaseSchema",
                                                                                  PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                  RequestTypeDefinition.CREATE_POSTGRES_SCHEMA,
                                                                                  PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE,
                                                                                  RequestTypeDefinition.CATALOG_POSTGRES_SCHEMA);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("PostgreSQLDatabaseSchema",
                                                                                  PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-postgres/overview/",
                                                                                  RequestTypeDefinition.DELETE_POSTGRES_SCHEMA);
        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.POSTGRES_CONTENT_PACK, additionalSolutionComponents);
        this.addSolutionLinkingWires(ContentPackDefinition.POSTGRES_CONTENT_PACK);

        /*
         * Saving the GUIDs means that the guids in the archive are stable between runs of the archive writer.
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