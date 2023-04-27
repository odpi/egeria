/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas;


import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerEventType;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryAnchorElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;
import org.odpi.openmetadata.integrationservices.catalog.connector.GlossaryExchangeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * ApacheAtlasIntegrationConnector catalogues glossary terms in Apache Atlas.
 */
public class ApacheAtlasIntegrationConnector extends CatalogIntegratorConnector implements AssetManagerEventListener
{
    private String glossaryQualifiedName = null;
    private String glossaryGUID          = null;
    private String targetRootURL         = "localhost:21000";

    private CatalogIntegratorContext myContext = null;
    private GlossaryExchangeService  glossaryExchangeService = null;
    private ApacheAtlasRESTClient    atlasClient = null;


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        myContext = super.getContext();

        /*
         * Retrieve the configuration
         */
        EndpointProperties  endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getAddress();
        }

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        if (configurationProperties != null)
        {
            glossaryQualifiedName = configurationProperties.get(ApacheAtlasIntegrationProvider.GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
        }

        /*
         * Record the configuration
         */
        if (auditLog != null)
        {
            if (glossaryQualifiedName == null)
            {
                auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_CONFIGURATION_ALL.getMessageDefinition(connectorName, targetRootURL));
            }
            else
            {
                auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_CONFIGURATION_SPECIFIC.getMessageDefinition(connectorName, targetRootURL, glossaryQualifiedName));
            }
        }

        try
        {
            /*
             * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
             * the processEvent() method is called.
             */
            myContext.registerListener(this);

            /*
             * This creates the client to call Apache Atlas.
             */
            atlasClient = new ApacheAtlasRESTClient("Apache Atlas at " + targetRootURL,
                                                    targetRootURL,
                                                    auditLog);

            /*
             * The glossaryExchangeService provides access to the open metadata API.
             */
            glossaryExchangeService = myContext.getGlossaryExchangeService();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.MISSING_TEMPLATE.getMessageDefinition(connectorName, glossaryQualifiedName),
                                      error);
            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * This method performs two sweeps. It first retrieves the topics from the event broker (Kafka) and validates that are in the
     * catalog - adding or updating them if necessary. The second sweep is to ensure that all the topics catalogued
     * actually exist in the event broker.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            if (glossaryQualifiedName == null)
            {
                List<GlossaryElement> glossaries = glossaryExchangeService.findGlossaries(".*", 0, 0, new Date());

                if (glossaries != null)
                {
                    for (GlossaryElement glossary : glossaries)
                    {
                        this.refreshGlossary(glossary);
                    }
                }
            }
            else
            {
                List<GlossaryElement> glossaries = glossaryExchangeService.getGlossariesByName(glossaryGUID, 0, 0, new Date());

                if (glossaries != null)
                {
                    for (GlossaryElement glossary : glossaries)
                    {
                        this.refreshGlossary(glossary);
                    }
                }
            }
        }
        catch (InvalidParameterException error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.UNABLE_TO_RETRIEVE_TOPICS.getMessageDefinition(connectorName,
                                                                                                          "localhost:9092",
                                                                                                          error.getClass().getName(),
                                                                                                          error.getMessage()),
                                      error);
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.UNABLE_TO_RETRIEVE_TOPICS.getMessageDefinition(connectorName,
                                                                                                                        "localhost:9092",
                                                                                                                        error.getClass().getName(),
                                                                                                                        error.getMessage()),
                                      error);


            }

            throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                             error.getClass().getName(),
                                                                                                                             error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }



    private GlossaryElement getGlossaryElement(String glossaryQualifiedName,
                                               String methodName) throws ConnectorCheckedException
    {
        try
        {
            List<GlossaryElement> egeriaGlossaries = glossaryExchangeService.getGlossariesByName(glossaryQualifiedName, 0, 0, null);

            if (egeriaGlossaries != null)
            {
                for (GlossaryElement glossaryElement : egeriaGlossaries)
                {
                    String qualifiedName = glossaryElement.getGlossaryProperties().getQualifiedName();

                    if (glossaryQualifiedName.equals(qualifiedName))
                    {
                        glossaryGUID = glossaryElement.getElementHeader().getGUID();
                    }
                }
            }

            if (glossaryGUID == null)
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.MISSING_TEMPLATE.getMessageDefinition(connectorName, glossaryQualifiedName));
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.UNABLE_TO_RETRIEVE_TOPICS.getMessageDefinition(connectorName,
                                                                                                          "localhost:9092",
                                                                                                          error.getClass().getName(),
                                                                                                          error.getMessage()),
                                      error);


            }

            throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }

        return null;
    }

    /**
     * Process an event that was published by the Asset Manager OMAS.  This connector is only interested in
     * glossaries and glossary terms.
     *
     * @param event event object
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        ElementHeader         elementHeader = event.getElementHeader();
        AssetManagerEventType eventType     = event.getEventType();

        if (elementHeader != null)
        {
            /*
             * Understand the type of the element that has changed.
             */
            ElementType elementType = elementHeader.getType();
            if (elementType != null)
            {
                List<String> elementTypeNames = new ArrayList<>();

                elementTypeNames.add(elementType.getTypeName());
                if (elementType.getSuperTypeNames() != null)
                {
                    elementTypeNames.addAll(elementType.getSuperTypeNames());
                }

                /*
                 * Is the element of interest?
                 */
                if (elementTypeNames.contains("Glossary"))
                {
                    if (glossaryGUID == null)
                    {
                        /*
                         * Process any glossary
                         */
                        this.refreshGlossary(eventType, elementHeader);
                    }
                    else if (glossaryGUID.equals(elementHeader.getGUID()))
                    {
                        /*
                         * Process the specific glossary
                         */
                        this.refreshGlossary(eventType, elementHeader);
                    }
                }
                else if (elementTypeNames.contains("GlossaryTerm"))
                {
                    if (glossaryGUID == null)
                    {
                        /*
                         * Process any term from any glossary
                         */
                        this.refreshGlossaryTerm(eventType, elementHeader);
                    }
                    else
                    {
                        String anchorGUID = myContext.getAnchorGUID(elementHeader);

                        if (glossaryGUID.equals(anchorGUID))
                        {
                            /*
                             * This term is from the appropriate glossary
                             */
                            this.refreshGlossaryTerm(eventType, elementHeader);
                        }
                    }
                }
            }
        }
    }


    private void refreshGlossary(AssetManagerEventType eventType,
                                 ElementHeader         glossaryHeader)
    {
        try
        {
            GlossaryElement glossary = glossaryExchangeService.getGlossaryByGUID(glossaryHeader.getGUID(), new Date());

            if (glossary != null)
            {
                this.refreshGlossary(glossary);
            }
        }
        catch (InvalidParameterException notFound)
        {
            /*
             * No term is present - was this a delete event?
             */
            if (eventType != AssetManagerEventType.ELEMENT_DELETED)
            {
                /*
                 * Report an error
                 */
            }
        }
        catch (Exception error)
        {
            /*
             * Report an error
             */
        }
    }


    private void refreshGlossary(GlossaryElement glossaryElement)
    {
        if (glossaryElement != null)
        {
            AtlasGlossaryAnchorElement atlasGlossaryAnchor = atlasClient.syncGlossary(glossaryElement);

            if (atlasGlossaryAnchor != null)
            {

            }
        }
    }


    private void refreshGlossaryTerm(AssetManagerEventType eventType,
                                     ElementHeader         glossaryTermHeader)
    {
        try
        {
            GlossaryTermElement glossaryTerm = glossaryExchangeService.getGlossaryTermByGUID(glossaryTermHeader.getGUID(), new Date());

            if (glossaryTerm != null)
            {
                this.refreshGlossaryTerm(glossaryTerm, null);
            }
        }
        catch (InvalidParameterException notFound)
        {
            /*
             * No term is present - was this a delete event?
             */
            if (eventType != AssetManagerEventType.ELEMENT_DELETED)
            {
                /*
                 * Report an error
                 */
            }
        }
        catch (Exception error)
        {
            /*
             * Report an error
             */
        }
    }


    private void refreshGlossaryTerm(GlossaryTermElement        glossaryTerm,
                                     AtlasGlossaryAnchorElement atlasGlossaryAnchor)
    {
        if (glossaryTerm.getElementHeader().getStatus() == ElementStatus.ACTIVE)
        {
            atlasClient.syncGlossaryTerm(glossaryTerm, atlasGlossaryAnchor);
        }
    }


    /**
     * Shutdown glossary monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";


        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                ApacheAtlasAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}
