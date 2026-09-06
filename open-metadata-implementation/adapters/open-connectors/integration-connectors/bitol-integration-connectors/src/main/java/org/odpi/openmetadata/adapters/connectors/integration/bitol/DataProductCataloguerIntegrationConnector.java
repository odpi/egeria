/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentListener;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.BitolMappingResult;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataProductMapper;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * DataProductCataloguerIntegrationConnector registers a Bitol listener with the integration daemon and catalogues each
 * Open Data Product Standard (ODPS) document it receives as a DigitalProduct in open metadata, along with the product's
 * ports (as a solution component with solution ports), team (as scoped person roles), support channels (as contact
 * details), authoritative definitions (as external references) and management ports (as endpoints).  The mapping is
 * performed by DataProductMapper from the Open Integration Framework so that the same logic can be used by the
 * Product Manager view service.
 */
public class DataProductCataloguerIntegrationConnector extends IntegrationConnectorBase implements BitolDocumentListener
{
    private DataProductMapper mapper = null;


    /**
     * Default constructor
     */
    public DataProductCataloguerIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.  This registers the listener.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        if (integrationContext != null)
        {
            mapper = new DataProductMapper(integrationContext);

            integrationContext.registerBitolListener(this);
        }
    }


    /**
     * The documents arrive through the listener so there is nothing to do on refresh.
     */
    @Override
    public void refresh()
    {
        // nothing to do
    }


    /**
     * Catalogue a data product.
     *
     * @param dataProduct parsed document (null if the raw document could not be parsed into the bean)
     * @param rawDocument the document as received (YAML or JSON)
     */
    @Override
    public void processDataProduct(DataProduct dataProduct,
                                   String      rawDocument)
    {
        final String methodName = "processDataProduct";

        if ((dataProduct == null) || (mapper == null))
        {
            /*
             * Documents that could not be parsed are handled by the store connector.
             */
            return;
        }

        try
        {
            if (dataProduct.getId() == null)
            {
                auditLog.logMessage(methodName,
                                    BitolIntegrationConnectorAuditCode.DOCUMENT_WITHOUT_ID.getMessageDefinition(connectorName,
                                                                                                                BitolDocument.DATA_PRODUCT_KIND,
                                                                                                                getDocumentStart(rawDocument)));
                return;
            }

            BitolMappingResult result = mapper.catalogueDataProduct(dataProduct, connectorName);

            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.DOCUMENT_CATALOGUED.getMessageDefinition(connectorName,
                                                                                                            result.getAction().name().toLowerCase(),
                                                                                                            result.getElementGUID(),
                                                                                                            result.getKind(),
                                                                                                            result.getDocumentId(),
                                                                                                            result.getDocumentVersion()));

            for (String warning : result.getWarnings())
            {
                auditLog.logMessage(methodName,
                                    BitolIntegrationConnectorAuditCode.DOCUMENT_MAPPING_WARNING.getMessageDefinition(connectorName,
                                                                                                                     result.getKind(),
                                                                                                                     result.getDocumentId(),
                                                                                                                     result.getDocumentVersion(),
                                                                                                                     warning));
            }
        }
        catch (Exception error)
        {
            auditLog.logException(methodName,
                                  BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                  rawDocument,
                                  error);
        }
    }


    /**
     * Return the start of a document for use in an audit log message.
     *
     * @param rawDocument document (may be null)
     * @return first few hundred characters
     */
    private String getDocumentStart(String rawDocument)
    {
        final int maxLength = 300;

        if (rawDocument == null)
        {
            return "<no raw document>";
        }

        if (rawDocument.length() > maxLength)
        {
            return rawDocument.substring(0, maxLength) + " ...";
        }

        return rawDocument;
    }
}
