/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.componentid;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/**
 * ComponentIdReport outputs a list of component identifiers used by popular Egeria services and connectors.
 */
public class ComponentIdReport
{
    private static final String reportFileName = "component-id-report.md";
    private static final String licenseString  = "<!-- SPDX-License-Identifier: CC-BY-4.0 -->\n";
    private static final String copyrightString = "<!-- Copyright Contributors to the Egeria project. -->\n\n";
    private static final String reportHeader = "| ComponentId | ComponentName | ComponentDescription | Component | Home Page | \n| :--- | :--- | :--- | :--- | :--- |\n";
    private static final String snippetString = "\n--8<-- \"snippets/abbr.md\"";

    private static final String fileCohortRegistryConnector = "org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider";
    private static final String directoryMetadataArchiveConnector = "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.DirectoryBasedOpenMetadataArchiveStoreProvider";
    private static final String fileMetadataArchiveConnector = "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider";
    private static final String inMemRepositoryConnector = "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider";
    private static final String readOnlyRepositoryConnector = "org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector.ReadOnlyOMRSRepositoryConnectorProvider";
    private static final String postgresRepositoryConnectorClassName = "org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.PostgresOMRSRepositoryConnectorProvider";
    private static final String restRepositoryConnector = "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider";
    private static final String secretsStoreSecurityConnectorProviderClassName = "org.odpi.openmetadata.metadatasecurity.accessconnector.OpenMetadataAccessSecurityProvider";
    private static final String jdbcResourceConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider";
    private static final String dataFolderResourceConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider";
    private static final String fileFolderResourceConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider";
    private static final String fileResourceConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider";
    private static final String csvFileResourceConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVFileStoreProvider";
    private static final String inmemTopicConnectorProviderClassName = "org.odpi.openmetadata.adapters.eventbus.topic.inmemory.InMemoryOpenMetadataTopicProvider";
    private static final String kafkaTopicConnectorProviderClassName = "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider";
    private static final String atlasIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider";
    private static final String filesIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider";
    private static final String foldersIntegrationConnectorProviderClassName            = "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider";
    private static final String omarchivesIntegrationConnectorProviderClassName            = "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.OMArchiveFilesMonitorIntegrationProvider";
    private static final String csvLineageImporterIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.csvlineageimporter.CSVLineageImporterProvider";
    private static final String jdbcIntegrationConnectorProviderClassName               = "org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider";
    private static final String openAPIIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider";
    private static final String apiBasedOpenLineageLogStoreIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.APIBasedOpenLineageLogStoreProvider";
    private static final String fileBasedOpenLineageLogStoreIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.FileBasedOpenLineageLogStoreProvider";
    private static final String gaOpenLineageIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.GovernanceActionOpenLineageIntegrationProvider";
    private static final String openLineageCataloguerIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageCataloguerIntegrationProvider";
    private static final String openLineageEventReceiverIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageEventReceiverIntegrationProvider";
    private static final String yamlSecretsStoreProviderClassName = "org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreProvider";
    private static final String apacheAtlasRESTConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTProvider";
    private static final String apacheAtlasSurveyServiceProviderClassName = "org.odpi.openmetadata.adapters.connectors.apacheatlas.survey.SurveyApacheAtlasProvider";
    private static final String folderSurveyActionProviderClassName = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfolder.FolderSurveyServiceProvider";
    private static final String fileSurveyActionProviderClassName = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveyfile.FileSurveyServiceProvider";
    private static final String csvFileSurveyActionProviderClassName = "org.odpi.openmetadata.adapters.connectors.surveyaction.surveycsv.CSVSurveyServiceProvider";
    private static final String postgresServerSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresServerSurveyActionProvider";
    private static final String postgresDatabaseSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.postgres.survey.PostgresDatabaseSurveyActionProvider";
    private static final String postgresServerCatalogProviderClassName = "org.odpi.openmetadata.adapters.connectors.postgres.catalog.PostgresServerIntegrationProvider";
    private static final String apacheKafkaAdminProviderClassName = "org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminProvider";
    private static final String apacheKafkaIntegrationProviderClassName = "org.odpi.openmetadata.adapters.connectors.apachekafka.integration.KafkaTopicIntegrationProvider";
    private static final String apacheKafkaSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.apachekafka.survey.SurveyApacheKafkaServerProvider";
    private static final String egeriaPlatformProviderClassName = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.OMAGServerPlatformProvider";
    private static final String egeriaEngineHostProviderClassName = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.EngineHostProvider";
    private static final String egeriaIntegrationDaemonProviderClassName = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.IntegrationDaemonProvider";
    private static final String egeriaMetadataAccessServerProviderClassName = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.MetadataAccessServerProvider";
    private static final String egeriaViewServerProviderClassName = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.servers.ViewServerProvider";
    private static final String egeriaPlatformCataloguerProviderClassName = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.catalog.OMAGServerPlatformCatalogProvider";
    private static final String egeriaPlatformSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.egeriainfrastructure.platform.survey.OMAGServerPlatformSurveyProvider";
    private static final String egeriaIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.egeria.EgeriaCataloguerIntegrationProvider";
    private static final String unityCatalogResourceProviderClassName   = "org.odpi.openmetadata.adapters.connectors.unitycatalog.resource.OSSUnityCatalogResourceProvider";
    private static final String unityCatalogServerSyncProviderClassName   = "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogServerSyncProvider";
    private static final String unityCatalogCatalogSyncProviderClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.sync.OSSUnityCatalogInsideCatalogSyncProvider";
    private static final String unityCatalogServerSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogServerSurveyProvider";
    private static final String unityCatalogProvisionerProviderClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.provision.ProvisionUnityCatalogGovernanceActionProvider";
    private static final String unityCatalogCatalogSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideCatalogSurveyProvider";
    private static final String unityCatalogSchemaSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideSchemaSurveyProvider";
    private static final String unityCatalogVolumeSurveyProviderClassName = "org.odpi.openmetadata.adapters.connectors.unitycatalog.survey.OSSUnityCatalogInsideVolumeSurveyProvider";
    private static final String harvestOpenMetadataProviderClassName = "org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestopenmetadata.HarvestOpenMetadataProvider";
    private static final String harvestSurveysProviderClassName = "org.odpi.openmetadata.adapters.connectors.nannyconnectors.harvestsurveys.HarvestSurveysProvider";
    private static final String productHarvesterProviderClassName = "org.odpi.openmetadata.adapters.connectors.productmanager.OpenMetadataProductsHarvesterProvider";
    private static final String validValueTabularDataSetProviderClassName = "org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ValidValueDataSetProvider";
    private static final String validValueListTabularDataSetProviderClassName = "org.odpi.openmetadata.adapters.connectors.referencedata.tabulardatasets.ValidValueSetListProvider";
    private static final String csvFileTabularDataSetProviderClassName = "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetProvider";
    private static final String csvFileTabularDataSetCollectionProviderClassName = "org.odpi.openmetadata.adapters.connectors.datastore.csvfile.CSVTabularDataSetCollectionProvider";
    private static final String postgresTabularDataSetProviderClassName = "org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetProvider";
    private static final String postgresTabularDataSetCollectionProviderClassName = "org.odpi.openmetadata.adapters.connectors.postgres.tabulardatasource.PostgresTabularDataSetCollectionProvider";
    private static final String karmaPointProviderClassName = "org.odpi.openmetadata.adapters.connectors.organizationinsight.karmapoints.KarmaPointAwardsServiceProvider";
    private static final String monitoredResourceProviderClassName = "org.odpi.openmetadata.adapters.connectors.governanceactions.notifications.MonitoredResourceNotificationServiceProvider";
    private static final String oneTimeProviderClassName = "org.odpi.openmetadata.adapters.connectors.governanceactions.notifications.OneTimeNotificationServiceProvider";
    private static final String periodicRefreshProviderClassName = "org.odpi.openmetadata.adapters.connectors.governanceactions.notifications.PeriodicRefreshNotificationServiceProvider";


    /**
     * Return the component description for a connector.  This is managed by its connector provider.
     *
     * @param connectorProviderClassName name of the connector provider.
     * @return component description or null
     */
    private ComponentDescription getConnectorDescription(String connectorProviderClassName)
    {
        try
        {
            Class<?> connectorProviderClass = Class.forName(connectorProviderClassName);

            Object potentialConnectorProvider = connectorProviderClass.getConstructor().newInstance();

            if (potentialConnectorProvider instanceof ConnectorProviderBase connectorProvider)
            {
                return connectorProvider.getConnectorComponentDescription();
            }
        }
        catch (Exception error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
        }

        return null;
    }


    /**
     * Add the description of the supplied connector to the report.
     *
     * @param connectorProviderClassName class name of the connector's connector provider where the component description is managed
     * @param report report map
     */
    private void addConnectorDescription(String                             connectorProviderClassName,
                                         Map<Integer, ComponentDescription> report)
    {
        ComponentDescription connectorComponentDescription = this.getConnectorDescription(connectorProviderClassName);

        if (connectorComponentDescription != null)
        {
            ComponentDescription existingComponentDescription = report.put(connectorComponentDescription.getComponentId(), connectorComponentDescription);

            if (existingComponentDescription != null)
            {
                System.out.println("Error - componentId conflict between: " + existingComponentDescription.getComponentName() + " and: " + connectorComponentDescription.getComponentName());
            }
        }
    }

    /**
     * This runs the sample
     *
     * @throws IOException unable to write report
     */
    public void run() throws IOException
    {
        Map<Integer, ComponentDescription> report = new TreeMap<>();

        for (AccessServiceDescription serviceDescription : AccessServiceDescription.values())
        {
            ComponentDescription componentDescription = new AuditLogReportingComponent(serviceDescription.getServiceCode(),
                                                                                       serviceDescription.getServiceDevelopmentStatus(),
                                                                                       serviceDescription.getServiceName(),
                                                                                       serviceDescription.getServiceDescription(),
                                                                                       serviceDescription.getServiceWiki());

            ComponentDescription existingComponentDescription = report.put(componentDescription.getComponentId(), componentDescription);

            if (existingComponentDescription != null)
            {
                System.out.println("Error - componentId conflict between: " + existingComponentDescription.getComponentName() + " and: " + componentDescription.getComponentName());
            }
        }

        for (EngineServiceDescription serviceDescription : EngineServiceDescription.values())
        {
            ComponentDescription componentDescription = new AuditLogReportingComponent(serviceDescription.getEngineServiceCode(),
                                                                                       serviceDescription.getEngineServiceDevelopmentStatus(),
                                                                                       serviceDescription.getEngineServiceName(),
                                                                                       serviceDescription.getEngineServiceDescription(),
                                                                                       serviceDescription.getEngineServiceWiki());

            ComponentDescription existingComponentDescription = report.put(componentDescription.getComponentId(), componentDescription);

            if (existingComponentDescription != null)
            {
                System.out.println("Error - componentId conflict between: " + existingComponentDescription.getComponentName() + " and: " + componentDescription.getComponentName());
            }
        }

        for (ViewServiceDescription serviceDescription : ViewServiceDescription.values())
        {
            ComponentDescription componentDescription = new AuditLogReportingComponent(serviceDescription.getViewServiceCode(),
                                                                                       serviceDescription.getViewServiceDevelopmentStatus(),
                                                                                       serviceDescription.getViewServiceName(),
                                                                                       serviceDescription.getViewServiceDescription(),
                                                                                       serviceDescription.getViewServiceWiki());

            ComponentDescription existingComponentDescription = report.put(componentDescription.getComponentId(), componentDescription);

            if (existingComponentDescription != null)
            {
                System.out.println("Error - componentId conflict between: " + existingComponentDescription.getComponentName() + " and: " + componentDescription.getComponentName());
            }
        }

        for (OMRSAuditingComponent serviceDescription : OMRSAuditingComponent.values())
        {
            ComponentDescription componentDescription = new AuditLogReportingComponent(serviceDescription.getComponentId(),
                                                                                       serviceDescription.getComponentDevelopmentStatus(),
                                                                                       serviceDescription.getComponentName(),
                                                                                       serviceDescription.getComponentDescription(),
                                                                                       serviceDescription.getComponentWikiURL());

            ComponentDescription existingComponentDescription = report.put(componentDescription.getComponentId(), componentDescription);

            if (existingComponentDescription != null)
            {
                System.out.println("Error - componentId conflict between: " + existingComponentDescription.getComponentName() + " and: " + componentDescription.getComponentName());
            }
        }

        this.addConnectorDescription(fileCohortRegistryConnector, report);
        this.addConnectorDescription(directoryMetadataArchiveConnector, report);
        this.addConnectorDescription(fileMetadataArchiveConnector, report);
        this.addConnectorDescription(inMemRepositoryConnector, report);
        this.addConnectorDescription(postgresRepositoryConnectorClassName, report);
        this.addConnectorDescription(readOnlyRepositoryConnector, report);
        this.addConnectorDescription(restRepositoryConnector, report);
        this.addConnectorDescription(secretsStoreSecurityConnectorProviderClassName, report);
        this.addConnectorDescription(jdbcResourceConnectorProviderClassName, report);
        this.addConnectorDescription(csvFileResourceConnectorProviderClassName, report);
        this.addConnectorDescription(fileResourceConnectorProviderClassName, report);
        this.addConnectorDescription(fileFolderResourceConnectorProviderClassName, report);
        this.addConnectorDescription(dataFolderResourceConnectorProviderClassName, report);
        this.addConnectorDescription(inmemTopicConnectorProviderClassName, report);
        this.addConnectorDescription(kafkaTopicConnectorProviderClassName, report);
        this.addConnectorDescription(atlasIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(filesIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(foldersIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(omarchivesIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(csvLineageImporterIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(jdbcIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(openAPIIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(apiBasedOpenLineageLogStoreIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(fileBasedOpenLineageLogStoreIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(gaOpenLineageIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(openLineageCataloguerIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(openLineageEventReceiverIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(yamlSecretsStoreProviderClassName, report);
        this.addConnectorDescription(apacheAtlasRESTConnectorProviderClassName, report);
        this.addConnectorDescription(apacheAtlasSurveyServiceProviderClassName, report);
        this.addConnectorDescription(folderSurveyActionProviderClassName, report);
        this.addConnectorDescription(fileSurveyActionProviderClassName, report);
        this.addConnectorDescription(csvFileSurveyActionProviderClassName, report);
        this.addConnectorDescription(postgresServerCatalogProviderClassName, report);
        this.addConnectorDescription(postgresServerSurveyProviderClassName, report);
        this.addConnectorDescription(postgresDatabaseSurveyProviderClassName, report);
        this.addConnectorDescription(apacheKafkaAdminProviderClassName, report);
        this.addConnectorDescription(apacheKafkaIntegrationProviderClassName, report);
        this.addConnectorDescription(apacheKafkaSurveyProviderClassName, report);
        this.addConnectorDescription(egeriaPlatformProviderClassName, report);
        this.addConnectorDescription(egeriaPlatformCataloguerProviderClassName, report);
        this.addConnectorDescription(egeriaPlatformSurveyProviderClassName, report);
        this.addConnectorDescription(egeriaEngineHostProviderClassName, report);
        this.addConnectorDescription(egeriaIntegrationDaemonProviderClassName, report);
        this.addConnectorDescription(egeriaMetadataAccessServerProviderClassName, report);
        this.addConnectorDescription(egeriaViewServerProviderClassName, report);
        this.addConnectorDescription(egeriaIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(unityCatalogResourceProviderClassName, report);
        this.addConnectorDescription(unityCatalogServerSyncProviderClassName, report);
        this.addConnectorDescription(unityCatalogCatalogSyncProviderClassName, report);
        this.addConnectorDescription(unityCatalogProvisionerProviderClassName, report);
        this.addConnectorDescription(unityCatalogServerSurveyProviderClassName, report);
        this.addConnectorDescription(unityCatalogCatalogSurveyProviderClassName, report);
        this.addConnectorDescription(unityCatalogSchemaSurveyProviderClassName, report);
        this.addConnectorDescription(unityCatalogVolumeSurveyProviderClassName, report);
        this.addConnectorDescription(harvestOpenMetadataProviderClassName, report);
        this.addConnectorDescription(harvestSurveysProviderClassName, report);
        this.addConnectorDescription(productHarvesterProviderClassName, report);
        this.addConnectorDescription(validValueTabularDataSetProviderClassName, report);
        this.addConnectorDescription(validValueListTabularDataSetProviderClassName, report);
        this.addConnectorDescription(postgresTabularDataSetProviderClassName, report);
        this.addConnectorDescription(postgresTabularDataSetCollectionProviderClassName, report);
        this.addConnectorDescription(karmaPointProviderClassName, report);
        this.addConnectorDescription(monitoredResourceProviderClassName, report);
        this.addConnectorDescription(oneTimeProviderClassName, report);
        this.addConnectorDescription(periodicRefreshProviderClassName, report);
        this.addConnectorDescription(csvFileTabularDataSetProviderClassName, report);
        this.addConnectorDescription(csvFileTabularDataSetCollectionProviderClassName, report);

        File reportFile = new File(reportFileName);

        if (reportFile.exists())
        {
            reportFile.delete();
        }

        FileOutputStream fileOutStream = new FileOutputStream(reportFile);

        fileOutStream.write(licenseString.getBytes());
        fileOutStream.write(copyrightString.getBytes());
        fileOutStream.write(reportHeader.getBytes());

        for (Integer componentId : report.keySet())
        {
            ComponentDescription componentDescription = report.get(componentId);

            String reportLine = "| " + componentDescription.getComponentId() + " | " + componentDescription.getComponentName() + " | " + componentDescription.getComponentDescription() + " | " + componentDescription.getComponentDevelopmentStatus().getName() + " | " + componentDescription.getComponentWikiURL() + " |\n";
            fileOutStream.write(reportLine.getBytes());
            System.out.print(reportLine);
        }

        fileOutStream.write(snippetString.getBytes());
    }


    /**
     * Main program that controls the operation of the sample.  The parameters are passed space separated.
     * The file name must be passed as parameter 1.  The other parameters are used to override the
     * sample's default values.
     *
     * @param args 1. file name 2. server name, 3. URL root for the server, 4. client userId
     */
    public static void main(String[] args)
    {
        try
        {
            ComponentIdReport report = new ComponentIdReport();

            report.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}
