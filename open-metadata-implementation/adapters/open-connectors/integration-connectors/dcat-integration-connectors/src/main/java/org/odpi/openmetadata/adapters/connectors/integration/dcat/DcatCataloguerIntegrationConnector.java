/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.dcat;

import org.odpi.openmetadata.adapters.connectors.integration.dcat.ffdc.DcatIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentListener;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMapper;
import org.odpi.openmetadata.frameworks.integration.dcat.mapping.DcatMappingResult;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * DcatCataloguerIntegrationConnector catalogs the Data Catalog Vocabulary (DCAT) documents received by the integration
 * daemon.  Each dataset becomes a data set asset with an asset for each of its distributions, each data service
 * becomes a deployed API and each catalog becomes a collection folder.  The connector has no work to do on refresh:
 * it reacts to the documents published by the receivers running in the same integration daemon, and by calls to the
 * integration daemon's REST API.
 */
public class DcatCataloguerIntegrationConnector extends IntegrationConnectorBase implements DcatDocumentListener
{
    private DcatMapper mapper = null;


    /**
     * Default constructor
     */
    public DcatCataloguerIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        if (integrationContext != null)
        {
            mapper = new DcatMapper(integrationContext);
            integrationContext.registerDcatListener(this);
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata
     * repositories.  This connector is driven by the documents it receives so there is nothing to do.
     */
    @Override
    public void refresh()
    {
        // nothing to do
    }


    /**
     * Called each time a DCAT document is published to the integration daemon.
     *
     * @param document bean version of the document
     * @param rawDocument JSON-LD version of the document
     */
    @Override
    public void processDcatDocument(DcatDocument document,
                                    String       rawDocument)
    {
        final String methodName = "processDcatDocument";

        if ((document == null) || (mapper == null))
        {
            return;
        }

        try
        {
            DcatMappingResult result = mapper.catalogueDocument(document, connectorName);

            for (DcatMappingResult.CataloguedResource cataloguedResource : result.getCataloguedResources())
            {
                logRecord(methodName,
                          DcatIntegrationConnectorAuditCode.RESOURCE_CATALOGUED.getMessageDefinition(connectorName,
                                                                                                     cataloguedResource.action().name().toLowerCase(),
                                                                                                     cataloguedResource.elementGUID(),
                                                                                                     cataloguedResource.qualifiedName(),
                                                                                                     cataloguedResource.kind(),
                                                                                                     cataloguedResource.resourceKey()));
            }

            for (String warning : result.getWarnings())
            {
                logRecord(methodName, DcatIntegrationConnectorAuditCode.DOCUMENT_MAPPING_WARNING.getMessageDefinition(connectorName, warning));
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               DcatIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                           error.getClass().getName(),
                                                                                                           methodName,
                                                                                                           error.getMessage()),
                               rawDocument,
                               error);
        }
    }
}
