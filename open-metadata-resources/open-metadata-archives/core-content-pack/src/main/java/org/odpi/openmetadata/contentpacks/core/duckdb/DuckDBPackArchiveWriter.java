/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.duckdb;

import org.odpi.openmetadata.adapters.connectors.controls.DuckDBDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.duckdb.catalog.DuckDBDatabaseIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.duckdb.controls.DuckDBTemplateType;
import org.odpi.openmetadata.adapters.connectors.duckdb.survey.SurveyDuckDBAnnotationType;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.ArrayList;
import java.util.List;

/**
 * DuckDBPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for the DuckDB connectors supplied by the egeria project.  DuckDB is an embedded/file-based
 * database with no server tier, so - unlike the other vendor connector suites - this content pack only
 * covers the database tier.
 */
public class DuckDBPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public DuckDBPackArchiveWriter()
    {
        super(ContentPackDefinition.DUCKDB_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.DUCKDB_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.DUCKDB_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.DUCKDB_CONTENT_PACK.getArchiveFileName(),
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
        for (DuckDBDeployedImplementationType deployedImplementationType : DuckDBDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType);
        }

        /*
         * Add valid metadata values for the DuckDB-specific federation annotation types.  These are not part of
         * the vendor-neutral SurveyDatabaseAnnotationType set already registered by the CorePackArchiveWriter, so
         * they must be registered here before the DuckDB database survey action service (which produces them) is
         * added to the archive.
         */
        for (SurveyDuckDBAnnotationType annotationType : SurveyDuckDBAnnotationType.values())
        {
            this.addAnnotationType(annotationType);
        }

        /*
         * Integration Connector Types will link to the deployedImplementationType valid value element.
         * This information is in the connector provider.  DuckDB has no server tier, so there is a
         * single connector type for the database.
         */
        archiveHelper.addConnectorType(new DuckDBDatabaseIntegrationProvider());

        /*
         * Add catalog templates.  DuckDB has no server template and no tabular data set package.
         */
        this.addDataAssetCatalogTemplates(ContentPackDefinition.DUCKDB_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.DUCKDB_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.DUCKDB_CONTENT_PACK);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.DUCKDB_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.DUCKDB_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.DUCKDB_CONTENT_PACK);

        /*
         * Create helper processes
         */
        List<String> additionalSolutionComponents = new ArrayList<>();

        String solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("DuckDBDatabase",
                                                                                           DuckDBDeployedImplementationType.DUCKDB_DATABASE,
                                                                                           "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/",
                                                                                           RequestTypeDefinition.CREATE_DUCKDB_DB,
                                                                                           DuckDBTemplateType.DUCKDB_DATABASE_TEMPLATE,
                                                                                           RequestTypeDefinition.SURVEY_DUCKDB_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogAssetGovernanceActionProcess("DuckDBDatabase",
                                                                                    DuckDBDeployedImplementationType.DUCKDB_DATABASE,
                                                                                    "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/",
                                                                                    RequestTypeDefinition.CREATE_DUCKDB_DB,
                                                                                    DuckDBTemplateType.DUCKDB_DATABASE_TEMPLATE,
                                                                                    RequestTypeDefinition.CATALOG_DUCKDB_DATABASE);

        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("DuckDBDatabase",
                                                                                    DuckDBDeployedImplementationType.DUCKDB_DATABASE,
                                                                                    "https://egeria-project.org/egeria-solutions/leveraging-duckdb/overview/",
                                                                                    RequestTypeDefinition.DELETE_DUCKDB_DB);

        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.DUCKDB_CONTENT_PACK, additionalSolutionComponents);
        this.addSolutionLinkingWires(ContentPackDefinition.DUCKDB_CONTENT_PACK);

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
            DuckDBPackArchiveWriter archiveWriter = new DuckDBPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
