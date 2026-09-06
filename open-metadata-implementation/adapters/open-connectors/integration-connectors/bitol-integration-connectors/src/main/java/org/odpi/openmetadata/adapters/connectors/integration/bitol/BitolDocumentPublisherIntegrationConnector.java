/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataContractGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataProductGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventType;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.Map;

/**
 * BitolDocumentPublisherIntegrationConnector is the outbound side of Egeria's Bitol support.  It generates an Open Data
 * Product Standard (ODPS) document for each digital product, and an Open Data Contract Standard (ODCS) document for each
 * data sharing agreement, and publishes them to the Bitol listeners registered in the same integration daemon (for
 * example the file store connector, which writes them to a directory ready to commit to git).
 * <br><br>
 * Documents are generated when the connector receives an event describing a change to a digital product or agreement,
 * and on each refresh for every product that is a member of a DigitalProductCatalog catalog target (together with the
 * agreements referenced by the product's ports).  A document is only published when its content differs from the last
 * document generated for the same element, so a cataloguer running in the same daemon does not cause an endless
 * exchange of identical documents.
 */
public class BitolDocumentPublisherIntegrationConnector extends DynamicIntegrationConnectorBase implements OpenMetadataEventListener
{
    private DataProductGenerator  productGenerator  = null;
    private DataContractGenerator contractGenerator = null;

    private final Map<String, String> lastPublishedDocuments = new HashMap<>();


    /**
     * Default constructor
     */
    public BitolDocumentPublisherIntegrationConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
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
            productGenerator  = new DataProductGenerator(integrationContext);
            contractGenerator = new DataContractGenerator(integrationContext);
        }
    }


    /**
     * Process an event from the open metadata server.  Changes to digital products and data sharing agreements cause
     * their documents to be regenerated and published.
     *
     * @param event event object
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        final String methodName = "processEvent";

        if ((event == null) || (event.getElementHeader() == null) || (integrationContext == null))
        {
            return;
        }

        OpenMetadataEventType eventType = event.getEventType();

        if ((eventType != OpenMetadataEventType.NEW_ELEMENT_CREATED) &&
            (eventType != OpenMetadataEventType.ELEMENT_UPDATED) &&
            (eventType != OpenMetadataEventType.REFRESH_ELEMENT_EVENT) &&
            (eventType != OpenMetadataEventType.ELEMENT_CLASSIFIED) &&
            (eventType != OpenMetadataEventType.ELEMENT_RECLASSIFIED))
        {
            return;
        }

        try
        {
            ElementHeader elementHeader = event.getElementHeader();

            if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.DIGITAL_PRODUCT.typeName))
            {
                publishDataProduct(elementHeader.getGUID(), integrationContext);
            }
            else if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.AGREEMENT.typeName))
            {
                publishDataContract(elementHeader.getGUID(), integrationContext);
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


    /**
     * Generate and publish the document for a digital product, if it has changed since it was last published.
     *
     * @param digitalProductGUID product element
     * @param context context to use for reading and publishing
     * @throws Exception problem generating the document
     */
    void publishDataProduct(String             digitalProductGUID,
                            IntegrationContext context) throws Exception
    {
        final String methodName = "publishDataProduct";

        OpenMetadataRootElement product = context.getCollectionClient().getCollectionByGUID(digitalProductGUID, context.getCollectionClient().getGetOptions());

        if ((product == null) || (! propertyHelper.isTypeOf(product.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT.typeName)))
        {
            return;
        }

        DataProduct dataProduct = productGenerator.generateDataProduct(product);
        String      yaml        = BitolDocumentFormatter.toYAML(dataProduct);

        if (publishIfChanged(digitalProductGUID, yaml, () -> context.publishDataProduct(dataProduct)))
        {
            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.DOCUMENT_GENERATED.getMessageDefinition(connectorName,
                                                                                                           BitolDocument.DATA_PRODUCT_KIND,
                                                                                                           dataProduct.getId(),
                                                                                                           dataProduct.getVersion(),
                                                                                                           digitalProductGUID));
        }

        /*
         * The contracts referenced by the product's ports are published with it so that a store receives a complete set.
         */
        if (product.getCollectionMembers() != null)
        {
            for (RelatedMetadataElementSummary member : product.getCollectionMembers())
            {
                if ((member != null) && (member.getRelatedElement() != null) &&
                    (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)))
                {
                    OpenMetadataRootElement component = context.getSolutionComponentClient().getSolutionComponentByGUID(member.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                        context.getSolutionComponentClient().getGetOptions());

                    if ((component != null) && (component.getSolutionPorts() != null))
                    {
                        for (RelatedMetadataElementSummary portSummary : component.getSolutionPorts())
                        {
                            if ((portSummary != null) && (portSummary.getRelatedElement() != null))
                            {
                                OpenMetadataRootElement port = context.getSolutionPortClient().getSolutionPortByGUID(portSummary.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                     context.getSolutionPortClient().getGetOptions());

                                if ((port != null) && (port.getAgreementContents() != null))
                                {
                                    for (RelatedMetadataElementSummary agreement : port.getAgreementContents())
                                    {
                                        if ((agreement != null) && (agreement.getRelatedElement() != null))
                                        {
                                            publishDataContract(agreement.getRelatedElement().getElementHeader().getGUID(), context);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * Generate and publish the document for an agreement, if it is a data sharing agreement and has changed since it
     * was last published.
     *
     * @param agreementGUID agreement element
     * @param context context to use for reading and publishing
     * @throws Exception problem generating the document
     */
    void publishDataContract(String             agreementGUID,
                             IntegrationContext context) throws Exception
    {
        final String methodName = "publishDataContract";

        OpenMetadataRootElement agreement = context.getCollectionClient().getCollectionByGUID(agreementGUID, context.getCollectionClient().getGetOptions());

        if ((agreement == null) || (! propertyHelper.isTypeOf(agreement.getElementHeader(), OpenMetadataType.AGREEMENT.typeName)) || (! isDataSharingAgreement(agreement.getElementHeader())))
        {
            return;
        }

        DataContract dataContract = contractGenerator.generateDataContract(agreement);
        String       yaml         = BitolDocumentFormatter.toYAML(dataContract);

        if (publishIfChanged(agreementGUID, yaml, () -> context.publishDataContract(dataContract)))
        {
            auditLog.logMessage(methodName,
                                BitolIntegrationConnectorAuditCode.DOCUMENT_GENERATED.getMessageDefinition(connectorName,
                                                                                                           BitolDocument.DATA_CONTRACT_KIND,
                                                                                                           dataContract.getId(),
                                                                                                           dataContract.getVersion(),
                                                                                                           agreementGUID));
        }
    }


    /**
     * Return whether an agreement carries the DataSharingAgreement classification.
     *
     * @param elementHeader header of the agreement
     * @return boolean
     */
    private boolean isDataSharingAgreement(ElementHeader elementHeader)
    {
        if (elementHeader.getCollectionKinds() != null)
        {
            for (ElementClassification classification : elementHeader.getCollectionKinds())
            {
                if ((classification != null) && (OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION.typeName.equals(classification.getClassificationName())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Publish the document if its content differs from the last document published for the element.
     *
     * @param elementGUID element the document was generated from
     * @param yaml generated document
     * @param publisher action that publishes the document
     * @return true if the document was published
     */
    private synchronized boolean publishIfChanged(String   elementGUID,
                                                  String   yaml,
                                                  Runnable publisher)
    {
        if (yaml.equals(lastPublishedDocuments.get(elementGUID)))
        {
            return false;
        }

        lastPublishedDocuments.put(elementGUID, yaml);
        publisher.run();

        return true;
    }


    /**
     * Create a new catalog target processor.  DigitalProductCatalog catalog targets have every member product (and the
     * contracts they reference) regenerated on each refresh; other catalog targets are ignored.
     *
     * @param retrievedCatalogTarget the catalog target information retrieved from the open metadata repository
     * @param catalogTargetContext specialized integration context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     * @throws ConnectorCheckedException problem setting up the processor
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException,
                                                                                                                      UserNotAuthorizedException
    {
        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement().getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT_CATALOG.typeName))
        {
            return new BitolDocumentPublisherCatalogTargetProcessor(retrievedCatalogTarget,
                                                                    catalogTargetContext,
                                                                    connectorToTarget,
                                                                    connectorName,
                                                                    auditLog,
                                                                    this);
        }

        return new RequestedCatalogTarget(retrievedCatalogTarget, catalogTargetContext, connectorToTarget);
    }
}
