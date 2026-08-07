/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.mssql;

import org.odpi.openmetadata.adapters.connectors.controls.MSSQLDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.mssql.catalog.MSSQLServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.mssql.controls.MSSQLTemplateType;
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
 * MSSQLPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all Microsoft SQL Server connectors supplied by the egeria project.
 */
public class MSSQLPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public MSSQLPackArchiveWriter()
    {
        super(ContentPackDefinition.MSSQL_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.MSSQL_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.MSSQL_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.MSSQL_CONTENT_PACK.getArchiveFileName(),
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
        for (MSSQLDeployedImplementationType deployedImplementationType : MSSQLDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType);
        }

        /*
         * Integration Connector Types will link to the deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(new MSSQLServerIntegrationProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.MSSQL_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.MSSQL_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.MSSQL_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.MSSQL_CONTENT_PACK,
                                       IntegrationGroupDefinition.MSSQL);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.MSSQL_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.MSSQL_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.MSSQL_CONTENT_PACK);

        /*
         * Create helper processes
         */
        List<String> additionalSolutionComponents = new ArrayList<>();

        String solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("MSSQLServer",
                                                                                         MSSQLDeployedImplementationType.MSSQL_SERVER,
                                                                                         "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/",
                                                                                         RequestTypeDefinition.CREATE_MSSQL_SERVER,
                                                                                         SoftwareServerTemplateDefinition.MSSQL_SERVER_TEMPLATE,
                                                                                         RequestTypeDefinition.SURVEY_MSSQL_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogServerGovernanceActionProcess("MSSQLServer",
                                                                                   MSSQLDeployedImplementationType.MSSQL_SERVER,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/",
                                                                                   RequestTypeDefinition.CREATE_MSSQL_SERVER,
                                                                                   SoftwareServerTemplateDefinition.MSSQL_SERVER_TEMPLATE,
                                                                                   RequestTypeDefinition.CATALOG_MSSQL_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("MSSQLServer",
                                                                                  MSSQLDeployedImplementationType.MSSQL_SERVER,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/",
                                                                                  RequestTypeDefinition.DELETE_MSSQL_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("MSSQLDatabase",
                                                                                  MSSQLDeployedImplementationType.MSSQL_DATABASE,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/",
                                                                                  RequestTypeDefinition.CREATE_MSSQL_DB,
                                                                                  MSSQLTemplateType.MSSQL_DATABASE_TEMPLATE,
                                                                                  RequestTypeDefinition.SURVEY_MSSQL_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogAssetGovernanceActionProcess("MSSQLDatabase",
                                                                                  MSSQLDeployedImplementationType.MSSQL_DATABASE,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/",
                                                                                  RequestTypeDefinition.CREATE_MSSQL_DB,
                                                                                  MSSQLTemplateType.MSSQL_DATABASE_TEMPLATE,
                                                                                  RequestTypeDefinition.CATALOG_MSSQL_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("MSSQLDatabase",
                                                                                  MSSQLDeployedImplementationType.MSSQL_DATABASE,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-mssql/overview/",
                                                                                  RequestTypeDefinition.DELETE_MSSQL_DB);

        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.MSSQL_CONTENT_PACK, additionalSolutionComponents);
        this.addSolutionLinkingWires(ContentPackDefinition.MSSQL_CONTENT_PACK);

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
            MSSQLPackArchiveWriter archiveWriter = new MSSQLPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
