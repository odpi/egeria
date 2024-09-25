/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.apachekafka;

import org.odpi.openmetadata.adapters.connectors.apachekafka.control.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerProvider;
import org.odpi.openmetadata.archiveutilities.openconnectors.ContentPackDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.GovernanceEngineDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.IntegrationGroupDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.RequestTypeDefinition;
import org.odpi.openmetadata.archiveutilities.openconnectors.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.Date;

/**
 * ApacheKafkaPackArchiveWriter creates an open metadata archive that includes the connector type
 * information for all Apache Kafka connectors supplied by the egeria project.
 */
public class ApacheKafkaPackArchiveWriter extends ContentPackBaseArchiveWriter
{
    /**
     * Default constructor initializes the archive.
     */
    public ApacheKafkaPackArchiveWriter()
    {
        super(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK.getArchiveGUID(),
              ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK.getArchiveName(),
              ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK.getArchiveDescription(),
              new Date(),
              ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK.getArchiveFileName(),
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
        for (KafkaDeployedImplementationType deployedImplementationType : KafkaDeployedImplementationType.values())
        {
            this.addDeployedImplementationType(deployedImplementationType.getDeployedImplementationType(),
                                               deployedImplementationType.getAssociatedTypeName(),
                                               deployedImplementationType.getQualifiedName(),
                                               deployedImplementationType.getCategory(),
                                               deployedImplementationType.getDescription(),
                                               deployedImplementationType.getWikiLink());
        }

        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(null, new ApacheKafkaAdminProvider());
        archiveHelper.addConnectorType(null, new KafkaTopicIntegrationProvider());
        archiveHelper.addConnectorType(null, new SurveyApacheKafkaServerProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);

        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK, IntegrationGroupDefinition.DEFAULT);

        /*
         * Create the default governance engines
         */
        super.createGovernanceEngines(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);

        /*
         * Register the governance services that are going to be in the default governance engines.
         */
        super.createGovernanceServices(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);

        /*
         * Connect the governance engines to the governance services using the request types.
         */
        super.createRequestTypes(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);

        /*
         * Create a sample process
         */
        this.createAndSurveyServerGovernanceActionProcess("ApacheKafkaServer",
                                                          KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getDeployedImplementationType(),
                                                          RequestTypeDefinition.CREATE_KAFKA_SERVER,
                                                          GovernanceEngineDefinition.KAFKA_GOVERNANCE_ENGINE,
                                                          RequestTypeDefinition.SURVEY_KAFKA_SERVER,
                                                          GovernanceEngineDefinition.KAFKA_SURVEY_ENGINE);
        this.createAndCatalogServerGovernanceActionProcess("ApacheKafkaServer",
                                                           KafkaDeployedImplementationType.APACHE_KAFKA_SERVER.getDeployedImplementationType(),
                                                           RequestTypeDefinition.CREATE_KAFKA_SERVER,
                                                           GovernanceEngineDefinition.KAFKA_GOVERNANCE_ENGINE,
                                                           RequestTypeDefinition.CATALOG_KAFKA_SERVER,
                                                           GovernanceEngineDefinition.KAFKA_GOVERNANCE_ENGINE);

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
            ApacheKafkaPackArchiveWriter archiveWriter = new ApacheKafkaPackArchiveWriter();
            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}