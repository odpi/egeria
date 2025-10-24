/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.apacheatlas;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.controls.AtlasDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.*;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;


/**
 * ApacheAtlasPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all Apache Atlas connectors supplied by the egeria project.
 */
public class ApacheAtlasPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public ApacheAtlasPackArchiveWriter()
    {
        super(ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK.getArchiveFileName(),
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
        for (AtlasDeployedImplementationType deployedImplementationType : AtlasDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType.getDeployedImplementationType(),
                                               deployedImplementationType.getAssociatedTypeName(),
                                               deployedImplementationType.getQualifiedName(),
                                               deployedImplementationType.getDescription(),
                                               deployedImplementationType.getWikiLink(),
                                               deployedImplementationType.getIsATypeOf());
        }

        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(new ApacheAtlasRESTProvider());
        archiveHelper.addConnectorType(new ApacheAtlasIntegrationProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK,
                                       IntegrationGroupDefinition.APACHE_ATLAS);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.APACHE_ATLAS_CONTENT_PACK);

        /*
         * Create a sample process
         */
        this.createAndSurveyServerGovernanceActionProcess("ApacheAtlasServer",
                                                          AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_ATLAS_SERVER,
                                                          SoftwareServerTemplateDefinition.APACHE_ATLAS_TEMPLATE,
                                                          RequestTypeDefinition.SURVEY_ATLAS_SERVER,
                                                          AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName());
        
        this.createAndCatalogServerGovernanceActionProcess("ApacheAtlasServer",
                                                           AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_ATLAS_SERVER,
                                                           SoftwareServerTemplateDefinition.APACHE_ATLAS_TEMPLATE,
                                                           RequestTypeDefinition.CATALOG_ATLAS_SERVER,
                                                           AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName());

        this.deleteAsCatalogTargetGovernanceActionProcess("ApacheAtlasServer",
                                                          AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getAssociatedTypeName(),
                                                          AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.DELETE_ATLAS_SERVER,
                                                          AtlasDeployedImplementationType.APACHE_ATLAS_SERVER.getQualifiedName());

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
            ApacheAtlasPackArchiveWriter archiveWriter = new ApacheAtlasPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}