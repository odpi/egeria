/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.apis;

import org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.ContentPackDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.IntegrationGroupDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;


/**
 * APIPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all open API connectors supplied by the egeria project.
 */
public class APIPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public APIPackArchiveWriter()
    {
        super(ContentPackDefinition.APIS_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.APIS_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.APIS_CONTENT_PACK.getArchiveDescription(),
              ContentPackDefinition.APIS_CONTENT_PACK.getArchiveFileName(),
              new OpenMetadataArchive[]{new CorePackArchiveWriter().getOpenMetadataArchive()});
    }


    /**
     * Implemented by subclass to add the content.
     */
    @Override
    public void getArchiveContent()
    {
        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(null, new OpenAPIMonitorIntegrationProvider());

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.APIS_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.APIS_CONTENT_PACK, IntegrationGroupDefinition.APIS);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.APIS_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.APIS_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.APIS_CONTENT_PACK);

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
            APIPackArchiveWriter archiveWriter = new APIPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}