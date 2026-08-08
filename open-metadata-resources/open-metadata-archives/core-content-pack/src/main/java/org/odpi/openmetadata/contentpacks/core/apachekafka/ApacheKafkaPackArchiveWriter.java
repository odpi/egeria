/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.contentpacks.core.apachekafka;

import org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.controls.AtlasAnnotationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.controls.KafkaAnnotationType;
import org.odpi.openmetadata.adapters.connectors.controls.KafkaDeployedImplementationType;
import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider;
import org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerProvider;
import org.odpi.openmetadata.contentpacks.core.ContentPackDefinition;
import org.odpi.openmetadata.contentpacks.core.RequestTypeDefinition;
import org.odpi.openmetadata.contentpacks.core.SoftwareServerTemplateDefinition;
import org.odpi.openmetadata.contentpacks.core.base.ContentPackBaseArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.core.CorePackArchiveWriter;
import org.odpi.openmetadata.contentpacks.core.IntegrationGroupDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.ArrayList;
import java.util.List;


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
            this.addDeployedImplementationType(deployedImplementationType);
        }


        /*
         * Add valid metadata values for the Survey Action Framework standard controls.
         */
        for (KafkaAnnotationType annotationType : KafkaAnnotationType.values())
        {
            this.addAnnotationType(annotationType);
        }

        /*
         * Integration Connector Types may need to link to deployedImplementationType valid value element.
         * This information is in the connector provider.
         */
        archiveHelper.addConnectorType(new ApacheKafkaAdminProvider());
        archiveHelper.addConnectorType(new KafkaTopicIntegrationProvider());
        archiveHelper.addConnectorType(new SurveyApacheKafkaServerProvider());

        /*
         * Add catalog templates
         */
        this.addSoftwareServerCatalogTemplates(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);
        this.addDataAssetCatalogTemplates(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);


        /*
         * Create the default integration group.
         */
        super.addIntegrationGroups(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);
        super.addIntegrationConnectors(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);

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
         * Create helper processes
         */
        List<String> additionalSolutionComponents = new ArrayList<>();

        String solutionComponentGUID = this.createAndSurveyServerGovernanceActionProcess("ApacheKafkaServer",
                                                                                         KafkaDeployedImplementationType.APACHE_KAFKA_SERVER,
                                                                                         "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/",
                                                                                         RequestTypeDefinition.CREATE_KAFKA_SERVER,
                                                                                         SoftwareServerTemplateDefinition.KAFKA_SERVER_TEMPLATE,
                                                                                         RequestTypeDefinition.SURVEY_KAFKA_SERVER);
        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.createAndCatalogServerGovernanceActionProcess("ApacheKafkaServer",
                                                                                   KafkaDeployedImplementationType.APACHE_KAFKA_SERVER,
                                                                                   "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/",
                                                                                   RequestTypeDefinition.CREATE_KAFKA_SERVER,
                                                                                   SoftwareServerTemplateDefinition.KAFKA_SERVER_TEMPLATE,
                                                                                   RequestTypeDefinition.CATALOG_KAFKA_SERVER);
        additionalSolutionComponents.add(solutionComponentGUID);

        solutionComponentGUID = this.deleteAsCatalogTargetGovernanceActionProcess("ApacheKafkaServer",
                                                                                  KafkaDeployedImplementationType.APACHE_KAFKA_SERVER,
                                                                                  "https://egeria-project.org/egeria-solutions/leveraging-apache-kafka/overview/",
                                                                                  RequestTypeDefinition.DELETE_KAFKA_SERVER);
        additionalSolutionComponents.add(solutionComponentGUID);

        /*
         * Define the solution components for this solution.
         */
        this.addSolutionBlueprints(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK, additionalSolutionComponents);
        this.addSolutionLinkingWires(ContentPackDefinition.APACHE_KAFKA_CONTENT_PACK);

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