/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.nanny;

import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgreSQLTemplateType;
import org.odpi.openmetadata.adapters.connectors.postgres.controls.PostgresDeployedImplementationType;
import org.odpi.openmetadata.archiveutilities.openconnectors.ContentPackDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.GovernanceEngineDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.IntegrationGroupDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.RequestTypeDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.postgres.PostgresPackArchiveWriter;
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
                                                            GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                            RequestTypeDefinition.HARVEST_SURVEYS,
                                                            GovernanceEngineDefinition.NANNY_GOVERNANCE_ENGINE,
                                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getQualifiedName());

        this.createAndHarvestToAssetGovernanceActionProcess("HarvestOpenMetadataEcosystem",
                                                            OpenMetadataType.DEPLOYED_DATABASE_SCHEMA.typeName,
                                                            PostgresDeployedImplementationType.POSTGRESQL_DATABASE_SCHEMA.getDeployedImplementationType(),
                                                            RequestTypeDefinition.CREATE_POSTGRES_SCHEMA,
                                                            PostgreSQLTemplateType.POSTGRES_SCHEMA_TEMPLATE,
                                                            GovernanceEngineDefinition.POSTGRES_GOVERNANCE_ENGINE,
                                                            RequestTypeDefinition.HARVEST_OPEN_METADATA,
                                                            GovernanceEngineDefinition.NANNY_GOVERNANCE_ENGINE,
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