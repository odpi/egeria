/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.dynamicarchivers.glossary;


import org.odpi.openmetadata.engineservices.archivemanager.connector.ArchiveService;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;

import java.util.*;

/**
 * GlossaryDynamicArchiverConnector catalogs a glossary - either as a single snapshot or as a long running listener.
 */
public class GlossaryDynamicArchiverConnector extends ArchiveService
{
    /*
     * Nan of the glossary to archive - if null, all glossaries are archived.
     */
    private String  glossaryQualifiedName = null;


    /**
     * Call made by the ConnectorProvider to initialize the Connector with the base services.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String               connectorInstanceId,
                           ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        /*
         * Retrieve the configuration properties from the Connection object.  These properties affect all requests to this connector.
         */
        if (configurationProperties != null)
        {
            Object glossaryNameOption = configurationProperties.get(GlossaryDynamicArchiverProvider.GLOSSARY_NAME_PROPERTY);

            if (glossaryNameOption != null)
            {
                glossaryQualifiedName = glossaryNameOption.toString();
            }
        }
    }



    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     *
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException there is a problem within the governance action service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String methodName = "start";

        super.start();

    }
}
