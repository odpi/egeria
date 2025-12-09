/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.nanny;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.postgres.PostgresPackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;


/**
 * NannyPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open connectors supplied by the egeria project.
 */
public class NannyPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public NannyPackArchiveWriter()
    {
        super(ContentPackDefinition.NANNY_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.NANNY_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.NANNY_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.NANNY_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive(), new PostgresPackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
/*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.NANNY_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.NANNY_CONTENT_PACK,
                                       IntegrationGroupDefinition.NANNY);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.NANNY_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.NANNY_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.NANNY_CONTENT_PACK);

        this.createAndHarvestToAssetGovernanceActionProcess("HarvestSurveyReports",
                                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getAssociatedTypeName(),
                                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType(),
                                                            RequestTypeDefinition.CREATE_POSTGRES_SCHEMA,
                                                            PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE,
                                                            RequestTypeDefinition.HARVEST_SURVEYS,
                                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName());

        this.createAndHarvestToAssetGovernanceActionProcess("HarvestOpenMetadataEcosystem",
                                                            OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType(),
                                                            RequestTypeDefinition.CREATE_POSTGRES_SCHEMA,
                                                            PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE,
                                                            RequestTypeDefinition.HARVEST_OPEN_METADATA,
                                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName());

        /*
         * Define the solution components for this solution.
         */
        super.addSolutionBlueprints(ContentPackDefinition.NANNY_CONTENT_PACK);
        super.addSolutionLinkingWires(ContentPackDefinition.NANNY_CONTENT_PACK);

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
            NannyPackArchiveWriter archiveWriter = new NannyPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}