/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.reports.componentid;

import org.odpi.openmetadata.adapters.connectors.EgeriaOpenConnectorDefinition;
import org.odpi.openmetadata.samples.governanceactions.clinicaltrials.metadata.CocoOpenConnectorDefinition;
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

        for (EgeriaOpenConnectorDefinition connectorDefinition : EgeriaOpenConnectorDefinition.values())
        {
            ComponentDescription componentDescription = connectorDefinition.getComponentDescription();

            ComponentDescription existingComponentDescription = report.put(componentDescription.getComponentId(), componentDescription);

            if (existingComponentDescription != null)
            {
                System.out.println("Error - componentId conflict between: " + existingComponentDescription.getComponentName() + " and: " + componentDescription.getComponentName());
            }
        }

        for (CocoOpenConnectorDefinition connectorDefinition : CocoOpenConnectorDefinition.values())
        {
            ComponentDescription componentDescription = connectorDefinition.getComponentDescription();

            ComponentDescription existingComponentDescription = report.put(componentDescription.getComponentId(), componentDescription);

            if (existingComponentDescription != null)
            {
                System.out.println("Error - componentId conflict between: " + existingComponentDescription.getComponentName() + " and: " + componentDescription.getComponentName());
            }
        }

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
