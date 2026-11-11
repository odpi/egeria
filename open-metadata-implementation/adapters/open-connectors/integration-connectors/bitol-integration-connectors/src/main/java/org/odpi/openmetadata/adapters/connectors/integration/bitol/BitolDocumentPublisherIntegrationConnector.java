/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.bitol;

import org.odpi.openmetadata.adapters.connectors.integration.bitol.ffdc.BitolIntegrationConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.integration.bitol.BitolDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.bitol.common.BitolDocument;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataContractGenerator;
import org.odpi.openmetadata.frameworks.integration.bitol.mapping.DataContractMapper;
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
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * BitolDocumentPublisherIntegrationConnector is the outbound side of Egeria's Bitol support.  It generates an Open Data
 * Product Standard (ODPS) document for each digital product, and an Open Data Contract Standard (ODCS) document for each
 * data sharing agreement, and publishes them to the Bitol listeners registered in the same integration daemon (for
 * example the file store connector, which writes them to a directory ready to commit to git).
 * <br><br>
 * Documents are generated on each refresh: for every product that is a member of a DigitalProductCatalog catalog
 * target (together with the agreements referenced by the product's ports), and for each digital product or agreement
 * that an event has reported a change to since the last refresh.  Each element is generated at most once per refresh,
 * however many events arrived for it and however many products share it.  Generating a document takes thousands of
 * reads, and a product receives many events in a burst - create, update, classify, reclassify - so generating on
 * every event left the connector hours behind its events on a busy repository.  Each process that the lineage shows assembling a product's data
 * becomes an input port of the product; a process with no data contract gets a sparsely populated draft one, catalogued
 * and published with the product so that it can be filled out later.
 * <br><br>
 * A document is only published when its content differs from the last document generated for the same element, so a
 * cataloguer running in the same daemon does not cause an endless exchange of identical documents.
 */
public class BitolDocumentPublisherIntegrationConnector extends DynamicIntegrationConnectorBase implements OpenMetadataEventListener
{
    private DataProductGenerator  productGenerator  = null;
    private DataContractGenerator contractGenerator = null;
    private DataContractMapper    contractMapper    = null;

    private final Map<String, String> lastPublishedDocuments = new HashMap<>();

    /*
     * Digital products and agreements that events have reported changes to since the last refresh.
     */
    private final Set<String> pendingProductGUIDs   = new LinkedHashSet<>();
    private final Set<String> pendingAgreementGUIDs = new LinkedHashSet<>();

    /*
     * Elements whose documents have been generated during the current refresh.
     */
    private final Set<String> generatedThisRefresh = new HashSet<>();


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
            contractMapper    = new DataContractMapper(integrationContext);
        }
    }


    /**
     * Generate the documents for the catalog targets' products, and then for the products and agreements that
     * events have reported changes to and that the catalog targets did not cover.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during the refresh
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        synchronized (generatedThisRefresh)
        {
            generatedThisRefresh.clear();
        }

        super.refresh();

        if (integrationContext == null)
        {
            return;
        }

        List<String> productGUIDs;
        List<String> agreementGUIDs;

        synchronized (pendingProductGUIDs)
        {
            productGUIDs   = new ArrayList<>(pendingProductGUIDs);
            agreementGUIDs = new ArrayList<>(pendingAgreementGUIDs);

            pendingProductGUIDs.clear();
            pendingAgreementGUIDs.clear();
        }

        for (String productGUID : productGUIDs)
        {
            try
            {
                publishDataProduct(productGUID, integrationContext);
            }
            catch (UserNotAuthorizedException error)
            {
                throw error;
            }
            catch (Exception error)
            {
                logExceptionRecord(methodName,
                                   BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                   error);
            }
        }

        for (String agreementGUID : agreementGUIDs)
        {
            try
            {
                publishDataContract(agreementGUID, integrationContext);
            }
            catch (UserNotAuthorizedException error)
            {
                throw error;
            }
            catch (Exception error)
            {
                logExceptionRecord(methodName,
                                   BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                   error);
            }
        }
    }


    /**
     * Process an event from the open metadata server.  A change to a digital product or data sharing agreement is
     * noted, and its document is regenerated and published by the next refresh.
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

            synchronized (pendingProductGUIDs)
            {
                if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.DIGITAL_PRODUCT.typeName))
                {
                    pendingProductGUIDs.add(elementHeader.getGUID());
                }
                else if (propertyHelper.isTypeOf(elementHeader, OpenMetadataType.AGREEMENT.typeName))
                {
                    pendingAgreementGUIDs.add(elementHeader.getGUID());
                }
            }
        }
        catch (Exception error)
        {
            logExceptionRecord(methodName,
                               BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                               error);
        }
    }


    /**
     * Reads the element that an event is about.
     */
    @FunctionalInterface
    interface EventElementReader
    {
        /**
         * Retrieve the element.
         *
         * @return element (or null)
         * @throws Exception problem retrieving it
         */
        OpenMetadataRootElement read() throws Exception;
    }


    /**
     * Retrieve the element that an event is about.  The element may no longer be there by the time the event is
     * processed, or it may not be visible to this connector's user - a digital product that is still a proposal
     * (Promise classification), for example, or one in a zone the user cannot see.  The repository reports both
     * as an invalid parameter.  Neither is a document this connector should publish, so null is returned and the
     * event is skipped.
     *
     * @param reader retrieves the element
     * @return element, or null if it is not available to this connector
     * @throws Exception any other problem retrieving the element
     */
    static OpenMetadataRootElement readEventElement(EventElementReader reader) throws Exception
    {
        try
        {
            return reader.read();
        }
        catch (InvalidParameterException notAvailable)
        {
            return null;
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

        if (! isFirstGenerationThisRefresh(digitalProductGUID))
        {
            return;
        }

        OpenMetadataRootElement product = readEventElement(() -> context.getCollectionClient().getCollectionByGUID(digitalProductGUID, context.getCollectionClient().getGetOptions()));

        if ((product == null) || (! propertyHelper.isTypeOf(product.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT.typeName)))
        {
            return;
        }

        /*
         * The input port for each process that assembles the product's data needs a data contract.  A sparsely
         * populated draft is catalogued for any process that has none, so that it can be filled out later.
         */
        List<String> inputContractGUIDs = new ArrayList<>();
        String       productName        = getProductName(product);

        for (String processGUID : productGenerator.getAssemblingProcessGUIDs(product))
        {
            try
            {
                inputContractGUIDs.addAll(contractMapper.ensureInputContract(processGUID, productName));
            }
            catch (Exception error)
            {
                logExceptionRecord(methodName,
                                   BitolIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                methodName,
                                                                                                                error.getMessage()),
                                   error);
            }
        }

        DataProduct dataProduct = productGenerator.generateDataProduct(product);
        String      yaml        = BitolDocumentFormatter.toYAML(dataProduct);

        if (publishIfChanged(digitalProductGUID, yaml, () -> context.publishGeneratedDataProduct(dataProduct)))
        {
            logRecord(methodName,
                      BitolIntegrationConnectorAuditCode.DOCUMENT_GENERATED.getMessageDefinition(connectorName,
                                                                                                 BitolDocument.DATA_PRODUCT_KIND,
                                                                                                 dataProduct.getId(),
                                                                                                 dataProduct.getVersion(),
                                                                                                 digitalProductGUID));
        }

        /*
         * The contracts referenced by the product's ports are published with it so that a store receives a complete set.
         */
        for (String agreementGUID : inputContractGUIDs)
        {
            publishDataContract(agreementGUID, context);
        }

        if (product.getCollectionMembers() != null)
        {
            for (RelatedMetadataElementSummary member : product.getCollectionMembers())
            {
                if ((member != null) && (member.getRelatedElement() != null) &&
                    (propertyHelper.isTypeOf(member.getRelatedElement().getElementHeader(), OpenMetadataType.SOLUTION_COMPONENT.typeName)))
                {
                    /*
                     * Only the component's solution ports are used.
                     */
                    GetOptions componentOptions = context.getSolutionComponentClient().getGetOptions();

                    componentOptions.setGraphQueryDepth(1);
                    componentOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName));

                    OpenMetadataRootElement component = context.getSolutionComponentClient().getSolutionComponentByGUID(member.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                        componentOptions);

                    if ((component != null) && (component.getSolutionPorts() != null))
                    {
                        for (RelatedMetadataElementSummary portSummary : component.getSolutionPorts())
                        {
                            if ((portSummary != null) && (portSummary.getRelatedElement() != null))
                            {
                                /*
                                 * Only the agreements that the port is an item of are used.
                                 */
                                GetOptions portOptions = context.getSolutionPortClient().getGetOptions();

                                portOptions.setGraphQueryDepth(1);
                                portOptions.setIncludeOnlyRelationships(List.of(OpenMetadataType.AGREEMENT_ITEM_RELATIONSHIP.typeName));

                                OpenMetadataRootElement port = context.getSolutionPortClient().getSolutionPortByGUID(portSummary.getRelatedElement().getElementHeader().getGUID(),
                                                                                                                     portOptions);

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
     * Return the name of a product for the description of its draft input contracts.
     *
     * @param product product element
     * @return name
     */
    private String getProductName(OpenMetadataRootElement product)
    {
        if (product.getProperties() instanceof DigitalProductProperties properties)
        {
            if (properties.getProductName() != null)
            {
                return properties.getProductName();
            }
            if (properties.getDisplayName() != null)
            {
                return properties.getDisplayName();
            }

            return properties.getQualifiedName();
        }

        return product.getElementHeader().getGUID();
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

        if (! isFirstGenerationThisRefresh(agreementGUID))
        {
            return;
        }

        OpenMetadataRootElement agreement = readEventElement(() -> context.getCollectionClient().getCollectionByGUID(agreementGUID, context.getCollectionClient().getGetOptions()));

        if ((agreement == null) || (! propertyHelper.isTypeOf(agreement.getElementHeader(), OpenMetadataType.AGREEMENT.typeName)) || (! isDataSharingAgreement(agreement.getElementHeader())))
        {
            return;
        }

        DataContract dataContract = contractGenerator.generateDataContract(agreement);
        String       yaml         = BitolDocumentFormatter.toYAML(dataContract);

        if (publishIfChanged(agreementGUID, yaml, () -> context.publishGeneratedDataContract(dataContract)))
        {
            logRecord(methodName,
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
     * Record that an element's document is being generated in this refresh, and return whether it is the first time.
     *
     * @param elementGUID product or agreement
     * @return true if it has not been generated yet in this refresh
     */
    boolean isFirstGenerationThisRefresh(String elementGUID)
    {
        synchronized (generatedThisRefresh)
        {
            return generatedThisRefresh.add(elementGUID);
        }
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
