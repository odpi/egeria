/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.db2luw;

import org.odpi.openmetadata.adapters.connectors.controls.DB2LUWDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.db2luw.catalog.DB2LUWServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.db2luw.controls.DB2LUWTemplateType;
import org.odpi.openmetadata.adapters.connectors.db2luw.tabulardatasource.DB2LUWTabularDataSetCollectionProvider;
import org.odpi.openmetadata.adapters.connectors.db2luw.tabulardatasource.DB2LUWTabularDataSetProvider;
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
 * DB2LUWPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all Db2 for Linux, UNIX and Windows connectors supplied by the egeria project.
 */
public class DB2LUWPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public DB2LUWPackArchiveWriter()
    {
        super(ContentPackDefinition.DB2LUW_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.DB2LUW_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.DB2LUW_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.DB2LUW_CONTENT_PACK.getArchiveFileName(),
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
        for (DB2LUWDeployedImplementationType deployedImplementationType : DB2LUWDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType);
        }

        /*
         * Integration Connector Types will link to the deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(new DB2LUWServerIntegrationProvider());

        /*
         * Set up the connector types for resource connectors introduced by this content pack
         */
        archiveHelper.addConnectorType(new DB2LUWTabularDataSetProvider());
        archiveHelper.addConnectorType(new DB2LUWTabularDataSetCollectionProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.DB2LUW_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.DB2LUW_CONTENT_PACK);
        this.addTabularDataSetCatalogTemplates(ContentPackDefinition.DB2LUW_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.DB2LUW_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.DB2LUW_CONTENT_PACK);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.DB2LUW_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.DB2LUW_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.DB2LUW_CONTENT_PACK);

        /*
         * Create helper processes
         */
        List<String> additionalSolutionComponents = new ArrayList<>();

        String solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("DB2LUWServer",
                                                                                          DB2LUWDeployedImplementationType.DB2LUW_SERVER,
                                                                                          "https://egeria-project.org/egeria-solutions/leveraging-db2luw/overview/",
                                                                                          RequestTypeDefinition.CREATE_DB2LUW_SERVER,
                                                                                          SoftwareServerTemplateDefinition.DB2LUW_SERVER_TEMPLATE,
                                                                                          RequestTypeDefinition.SURVEY_DB2LUW_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogServerGovernanceActionProcess("DB2LUWServer",
                                                                                    DB2LUWDeployedImplementationType.DB2LUW_SERVER,
                                                                                    "https://egeria-project.org/egeria-solutions/leveraging-db2luw/overview/",
                                                                                    RequestTypeDefinition.CREATE_DB2LUW_SERVER,
                                                                                    SoftwareServerTemplateDefinition.DB2LUW_SERVER_TEMPLATE,
                                                                                    RequestTypeDefinition.CATALOG_DB2LUW_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("DB2LUWServer",
                                                                                   DB2LUWDeployedImplementationType.DB2LUW_SERVER,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-db2luw/overview/",
                                                                                   RequestTypeDefinition.DELETE_DB2LUW_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("DB2LUWDatabase",
                                                                                   DB2LUWDeployedImplementationType.DB2LUW_DATABASE,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-db2luw/overview/",
                                                                                   RequestTypeDefinition.CREATE_DB2LUW_DB,
                                                                                   DB2LUWTemplateType.DB2LUW_DATABASE_TEMPLATE,
                                                                                   RequestTypeDefinition.SURVEY_DB2LUW_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogAssetGovernanceActionProcess("DB2LUWDatabase",
                                                                                   DB2LUWDeployedImplementationType.DB2LUW_DATABASE,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-db2luw/overview/",
                                                                                   RequestTypeDefinition.CREATE_DB2LUW_DB,
                                                                                   DB2LUWTemplateType.DB2LUW_DATABASE_TEMPLATE,
                                                                                   RequestTypeDefinition.CATALOG_DB2LUW_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("DB2LUWDatabase",
                                                                                   DB2LUWDeployedImplementationType.DB2LUW_DATABASE,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-db2luw/overview/",
                                                                                   RequestTypeDefinition.DELETE_DB2LUW_DB);

        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.DB2LUW_CONTENT_PACK, additionalSolutionComponents);
        this.addSolutionLinkingWires(ContentPackDefinition.DB2LUW_CONTENT_PACK);

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
            DB2LUWPackArchiveWriter archiveWriter = new DB2LUWPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
