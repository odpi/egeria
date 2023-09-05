/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.componentid;

import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.EngineServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLogReportingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
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
    private static final String reportHeader = "| ComponentId | ComponentName | ComponentDescription | Component Home Page | \n| :--- | :--- | :--- | :--- |\n";
    private static final String snippetString = "\n--8<-- \"snippets/abbr.md\"";

    private static final String fileCohortRegistryConnector = "org.odpi.openmetadata.adapters.repositoryservices.cohortregistrystore.file.FileBasedRegistryStoreProvider";
    private static final String directoryMetadataArchiveConnector = "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.DirectoryBasedOpenMetadataArchiveStoreProvider";
    private static final String fileMetadataArchiveConnector = "org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider";
    private static final String inMemRepositoryConnector = "org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider";
    private static final String readOnlyRepositoryConnector = "org.odpi.openmetadata.adapters.repositoryservices.readonly.repositoryconnector.ReadOnlyOMRSRepositoryConnectorProvider";
    private static final String restRepositoryConnector = "org.odpi.openmetadata.adapters.repositoryservices.rest.repositoryconnector.OMRSRESTRepositoryConnectorProvider";
    private static final String platformSecurityConnectorProviderClassName = "org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaPlatformSecurityProvider";
    private static final String serverSecurityConnectorProviderClassName = "org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaServerSecurityProvider";
    private static final String jdbcResourceConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnectorProvider";
    private static final String kafkaTopicConnectorProviderClassName = "org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicProvider";
    private static final String atlasIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ApacheAtlasIntegrationProvider";
    private static final String egeriaIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.egeria.EgeriaCataloguerIntegrationProvider";
    private static final String filesIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFilesMonitorIntegrationProvider";
    private static final String foldersIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.basicfiles.DataFolderMonitorIntegrationProvider";
    private static final String jdbcIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.jdbc.JDBCIntegrationConnectorProvider";
    private static final String kafkaIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.kafka.KafkaMonitorIntegrationProvider";
    private static final String openAPIIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openapis.OpenAPIMonitorIntegrationProvider";
    private static final String apiBasedOpenLineageLogStoreIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.APIBasedOpenLineageLogStoreProvider";
    private static final String fileBasedOpenLineageLogStoreIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.FileBasedOpenLineageLogStoreProvider";
    private static final String gaOpenLineageIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.GovernanceActionOpenLineageIntegrationProvider";
    private static final String openLineageCataloguerIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageCataloguerIntegrationProvider";
    private static final String openLineageEventReceiverIntegrationConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.integration.openlineage.OpenLineageEventReceiverIntegrationProvider";
    private static final String envVarSecretsStoreProviderClassName = "org.odpi.openmetadata.adapters.connectors.secretsstore.envar.EnvVarSecretsStoreProvider";
    private static final String apacheAtlasRESTConnectorProviderClassName = "org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTProvider";


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
            ComponentDescription componentDescription = new AuditLogReportingComponent(serviceDescription.getAccessServiceCode(),
                                                                                       serviceDescription.getAccessServiceDevelopmentStatus(),
                                                                                       serviceDescription.getAccessServiceName(),
                                                                                       serviceDescription.getAccessServiceDescription(),
                                                                                       serviceDescription.getAccessServiceWiki());

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

        for (IntegrationServiceDescription serviceDescription : IntegrationServiceDescription.values())
        {
            ComponentDescription componentDescription = new AuditLogReportingComponent(serviceDescription.getIntegrationServiceCode(),
                                                                                       serviceDescription.getIntegrationServiceDevelopmentStatus(),
                                                                                       serviceDescription.getIntegrationServiceName(),
                                                                                       serviceDescription.getIntegrationServiceDescription(),
                                                                                       serviceDescription.getIntegrationServiceWiki());

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
        this.addConnectorDescription(readOnlyRepositoryConnector, report);
        this.addConnectorDescription(restRepositoryConnector, report);
        this.addConnectorDescription(platformSecurityConnectorProviderClassName, report);
        this.addConnectorDescription(serverSecurityConnectorProviderClassName, report);
        this.addConnectorDescription(jdbcResourceConnectorProviderClassName, report);
        this.addConnectorDescription(kafkaTopicConnectorProviderClassName, report);
        this.addConnectorDescription(atlasIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(egeriaIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(filesIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(foldersIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(jdbcIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(kafkaIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(openAPIIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(apiBasedOpenLineageLogStoreIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(fileBasedOpenLineageLogStoreIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(gaOpenLineageIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(openLineageCataloguerIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(openLineageEventReceiverIntegrationConnectorProviderClassName, report);
        this.addConnectorDescription(envVarSecretsStoreProviderClassName, report);
        this.addConnectorDescription(apacheAtlasRESTConnectorProviderClassName, report);

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
