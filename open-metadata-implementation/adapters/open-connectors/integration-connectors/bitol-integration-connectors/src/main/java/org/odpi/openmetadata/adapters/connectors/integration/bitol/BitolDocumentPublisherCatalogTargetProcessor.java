/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetProcessorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

/**
 * BitolDocumentPublisherCatalogTargetProcessor regenerates the documents for every digital product that is a member
 * of a DigitalProductCatalog catalog target on each refresh.
 */
public class BitolDocumentPublisherCatalogTargetProcessor extends CatalogTargetProcessorBase
{
    private final BitolDocumentPublisherIntegrationConnector publisher;


    /**
     * Constructor.
     *
     * @param template catalog target information
     * @param catalogTargetContext context for the catalog target
     * @param connectorToTarget connector to the catalog (not used)
     * @param connectorName name of the integration connector
     * @param auditLog logging destination
     * @param publisher connector that generates and publishes the documents
     */
    public BitolDocumentPublisherCatalogTargetProcessor(CatalogTarget                             template,
                                                        CatalogTargetContext                      catalogTargetContext,
                                                        Connector                                 connectorToTarget,
                                                        String                                    connectorName,
                                                        AuditLog                                  auditLog,
                                                        BitolDocumentPublisherIntegrationConnector publisher)
    {
        super(template, catalogTargetContext, connectorToTarget, connectorName, auditLog);

        this.publisher = publisher;
    }


    /**
     * Regenerate the documents for the products in the catalog.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        super.refresh();

        try
        {
            String catalogGUID = super.getCatalogTargetElement().getElementHeader().getGUID();
            int    startFrom   = 0;
            int    maxPageSize = integrationContext.getMaxPageSize();

            List<OpenMetadataRootElement> members = integrationContext.getCollectionClient().getCollectionMembers(catalogGUID,
                                                                                                                  integrationContext.getCollectionClient().getQueryOptions(startFrom, maxPageSize));

            while (members != null)
            {
                for (OpenMetadataRootElement member : members)
                {
                    if ((member != null) && (propertyHelper.isTypeOf(member.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT.typeName)))
                    {
                        publisher.publishDataProduct(member.getElementHeader().getGUID(), integrationContext);
                    }
                }

                startFrom = startFrom + maxPageSize;

                members = integrationContext.getCollectionClient().getCollectionMembers(catalogGUID,
                                                                                        integrationContext.getCollectionClient().getQueryOptions(startFrom, maxPageSize));
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                  error);
        }
    }
}
