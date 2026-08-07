/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.oracle;

import org.odpi.openmetadata.adapters.connectors.controls.OracleDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.oracle.catalog.OracleServerIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.oracle.controls.OracleTemplateType;
import org.odpi.openmetadata.adapters.connectors.oracle.tabulardatasource.OracleTabularDataSetCollectionProvider;
import org.odpi.openmetadata.adapters.connectors.oracle.tabulardatasource.OracleTabularDataSetProvider;
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
 * OraclePackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all Oracle Database connectors supplied by the egeria project.
 */
public class OraclePackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public OraclePackArchiveWriter()
    {
        super(ContentPackDefinition.ORACLE_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.ORACLE_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.ORACLE_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.ORACLE_CONTENT_PACK.getArchiveFileName(),
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
        for (OracleDeployedImplementationType deployedImplementationType : OracleDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType);
        }

        /*
         * Integration Connector Types will link to the deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(new OracleServerIntegrationProvider());

        /*
         * Set up the connector types for resource connectors introduced by this content pack
         */
        archiveHelper.addConnectorType(new OracleTabularDataSetProvider());
        archiveHelper.addConnectorType(new OracleTabularDataSetCollectionProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.ORACLE_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.ORACLE_CONTENT_PACK);
        this.addTabularDataSetCatalogTemplates(ContentPackDefinition.ORACLE_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.ORACLE_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.ORACLE_CONTENT_PACK,
                                       IntegrationGroupDefinition.ORACLE);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.ORACLE_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.ORACLE_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.ORACLE_CONTENT_PACK);

        /*
         * Create helper processes
         */
        List<String> additionalSolutionComponents = new ArrayList<>();

        String solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("OracleServer",
                                                                                          OracleDeployedImplementationType.ORACLE_SERVER,
                                                                                          "https://egeria-project.org/egeria-solutions/leveraging-oracle/overview/",
                                                                                          RequestTypeDefinition.CREATE_ORACLE_SERVER,
                                                                                          SoftwareServerTemplateDefinition.ORACLE_SERVER_TEMPLATE,
                                                                                          RequestTypeDefinition.SURVEY_ORACLE_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogServerGovernanceActionProcess("OracleServer",
                                                                                    OracleDeployedImplementationType.ORACLE_SERVER,
                                                                                    "https://egeria-project.org/egeria-solutions/leveraging-oracle/overview/",
                                                                                    RequestTypeDefinition.CREATE_ORACLE_SERVER,
                                                                                    SoftwareServerTemplateDefinition.ORACLE_SERVER_TEMPLATE,
                                                                                    RequestTypeDefinition.CATALOG_ORACLE_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("OracleServer",
                                                                                   OracleDeployedImplementationType.ORACLE_SERVER,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-oracle/overview/",
                                                                                   RequestTypeDefinition.DELETE_ORACLE_SERVER);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("OracleDatabase",
                                                                                   OracleDeployedImplementationType.ORACLE_DATABASE,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-oracle/overview/",
                                                                                   RequestTypeDefinition.CREATE_ORACLE_DB,
                                                                                   OracleTemplateType.ORACLE_DATABASE_TEMPLATE,
                                                                                   RequestTypeDefinition.SURVEY_ORACLE_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogAssetGovernanceActionProcess("OracleDatabase",
                                                                                   OracleDeployedImplementationType.ORACLE_DATABASE,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-oracle/overview/",
                                                                                   RequestTypeDefinition.CREATE_ORACLE_DB,
                                                                                   OracleTemplateType.ORACLE_DATABASE_TEMPLATE,
                                                                                   RequestTypeDefinition.CATALOG_ORACLE_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("OracleDatabase",
                                                                                   OracleDeployedImplementationType.ORACLE_DATABASE,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-oracle/overview/",
                                                                                   RequestTypeDefinition.DELETE_ORACLE_DB);

        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.ORACLE_CONTENT_PACK, additionalSolutionComponents);
        this.addSolutionLinkingWires(ContentPackDefinition.ORACLE_CONTENT_PACK);

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
            OraclePackArchiveWriter archiveWriter = new OraclePackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
