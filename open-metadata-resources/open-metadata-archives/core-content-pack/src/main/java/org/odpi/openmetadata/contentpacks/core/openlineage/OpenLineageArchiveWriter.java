/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.openlineage;

import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.DataAssetTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;


/**
 * OrganizationInsightArchiveWriter creates an open metadata archive that includes the connectors that
 * gather organization insight.
 */
public class OpenLineageArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public OpenLineageArchiveWriter()
    {
        super(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);

        /*
         * Create Egeria's integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK,
                                       IntegrationGroupDefinition.OPEN_LINEAGE);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);

        /*
         * Set up the governance action process that associates a kafka topic with the open lineage listener.
         */
        this.createAndHarvestToAssetGovernanceActionProcess("HarvestOpenLineageEvents",
                                                            DeployedImplementationType.APACHE_KAFKA_TOPIC.getAssociatedTypeName(),
                                                            DeployedImplementationType.APACHE_KAFKA_TOPIC.getDeployedImplementationType(),
                                                            RequestTypeDefinition.CREATE_KAFKA_TOPIC,
                                                            DataAssetTemplateDefinition.KAFKA_TOPIC_TEMPLATE,
                                                            RequestTypeDefinition.HARVEST_OPEN_LINEAGE_TOPIC,
                                                            DeployedImplementationType.APACHE_KAFKA_TOPIC.getQualifiedName());

        /*
         * Define the solution components for this solution.
         */
        super.addSolutionBlueprints(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);
        super.addSolutionLinkingWires(ContentPackDefinition.OPEN_LINEAGE_CONTENT_PACK);

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
            OpenLineageArchiveWriter archiveWriter = new OpenLineageArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}