/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas;


import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryCategoryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataCorrelationHeader;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.MetadataElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryCategoryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GlossaryTermProperties;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.NameConflictException;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryAnchorElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryBaseProperties;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryCategoryElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryMemberBaseProperties;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasGlossaryTermElement;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasRelatedCategoryHeader;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties.AtlasRelatedTermHeader;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;
import org.odpi.openmetadata.integrationservices.catalog.connector.GlossaryExchangeService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * ApacheAtlasIntegrationConnector exchanges glossary terms between Apache Atlas and the open metadata ecosystem.
 */
public class ApacheAtlasGlossaryIntegrationModule extends AtlasIntegrationModuleBase
{
    private final static String atlasGUIDPropertyName   = "atlasGUID";

    private String fixedEgeriaGlossaryQualifiedName = null;
    private String fixedEgeriaGlossaryGUID          = null;
    private String atlasGlossaryName                = null;

    private final GlossaryExchangeService glossaryExchangeService;

    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionProperties connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException
     */
    public ApacheAtlasGlossaryIntegrationModule(String                   connectorName,
                                                ConnectionProperties     connectionProperties,
                                                AuditLog                 auditLog,
                                                CatalogIntegratorContext myContext,
                                                String                   targetRootURL,
                                                ApacheAtlasRESTClient    atlasClient,
                                                List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              connectionProperties,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors,
              new String[]{"Glossary", "GlossaryCategory", "GlossaryTerm"});

        final String methodName = "ApacheAtlasGlossaryIntegrationModule()";

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        if (configurationProperties != null)
        {
            fixedEgeriaGlossaryQualifiedName = configurationProperties.get(ApacheAtlasIntegrationProvider.EGERIA_GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
            atlasGlossaryName = configurationProperties.get(ApacheAtlasIntegrationProvider.ATLAS_GLOSSARY_NAME_CONFIGURATION_PROPERTY).toString();
        }

        /*
         * Record the configuration
         */
        if (auditLog != null)
        {
            if (fixedEgeriaGlossaryQualifiedName == null)
            {
                auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_CONFIGURATION_ALL_EGERIA_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL));
            }
            else
            {
                auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_CONFIGURATION_SPECIFIC_EGERIA_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL,
                                                                                                                                             fixedEgeriaGlossaryQualifiedName));
            }

            if (atlasGlossaryName == null)
            {
                auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_CONFIGURATION_ALL_ATLAS_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL));
            }
            else
            {
                auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_CONFIGURATION_SPECIFIC_ATLAS_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL, atlasGlossaryName));
            }
        }

        /*
         * The glossaryExchangeService provides access to the open metadata API.
         */
        glossaryExchangeService = myContext.getGlossaryExchangeService();
    }


    /* ==============================================================================
     * Standard methods that trigger activity.
     */

    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * <br><br>
     * This method performs two sweeps. It first retrieves the glossary elements from Egeria and synchronizes them with Apache Atlas.
     * The second sweep works through the glossaries in Apache Atlas and ensures that none have been missed out by the first sweep.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        try
        {
            /*
             * Sweep 1 - Egeria to Atlas - but only if the configuration allows the exchange.
             */
            if ((myContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                (myContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
            {
                if (fixedEgeriaGlossaryQualifiedName == null)
                {
                    /*
                     * Exchange all glossaries found in the open metadata ecosystem.
                     */
                    List<GlossaryElement> glossaries = glossaryExchangeService.findGlossaries(".*", 0, 0, new Date());

                    if (glossaries != null)
                    {
                        for (GlossaryElement glossary : glossaries)
                        {
                            this.processEgeriaGlossary(glossary);
                        }
                    }
                }
                else
                {
                    /*
                     * Only exchange the specific named glossary
                     */
                    GlossaryElement glossary = this.getGlossaryElement(fixedEgeriaGlossaryQualifiedName, methodName);

                    if (glossary != null)
                    {
                        this.processEgeriaGlossary(glossary);
                    }
                    else
                    {
                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                ApacheAtlasAuditCode.UNABLE_TO_RETRIEVE_EGERIA_GLOSSARY.getMessageDefinition(connectorName,
                                                                                                                             fixedEgeriaGlossaryQualifiedName));
                        }
                    }
                }
            }

            /*
             * Sweep 2 - Atlas to Egeria - assuming metadata synchronization is set up to copy metadata from Apache Atlas to the open metadata ecosystem.
             */
            if ((myContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                (myContext.getPermittedSynchronization() == PermittedSynchronization.FROM_THIRD_PARTY))
            {
                /*
                 * The Atlas Glossaries are retrieved one at a time. The aim is to look for new glossaries in Apache Atlas that have no presence in
                 * the open metadata ecosystem.
                 */
                int glossaryCount = 0;
                boolean requestedAtlasGlossaryMissing = (atlasGlossaryName != null);

                AtlasGlossaryElement atlasGlossary = atlasClient.getAtlasGlossary(glossaryCount);

                while (atlasGlossary != null)
                {
                    /*
                     * Focus on the Atlas owned glossaries as the Egeria owned glossaries have already been synchronized.
                     */
                    if (! this.isEgeriaOwned(atlasGlossary))
                    {
                        /*
                         * This glossary has never been synchronized with the open metadata ecosystem.
                         */
                        if (atlasGlossaryName == null)
                        {
                            /*
                             * The connector is configured to synchronize all Atlas glossaries.
                             */
                            this.processAtlasGlossary(atlasGlossary);
                        }
                        else if (atlasGlossaryName.equals(atlasGlossary.getName()))
                        {
                            /*
                             * The specifically requested glossary has been found.
                             */
                            this.processAtlasGlossary(atlasGlossary);
                            requestedAtlasGlossaryMissing = false;
                        }
                    }

                    atlasGlossary = atlasClient.getAtlasGlossary(glossaryCount++);
                }

                /*
                 * This message means that the specifically requested Atlas glossary has not been found.
                 * The connector will try again on the next refresh.
                 */
                if (requestedAtlasGlossaryMissing)
                {
                    if (auditLog != null)
                    {
                        auditLog.logMessage(methodName,
                                            ApacheAtlasAuditCode.UNABLE_TO_RETRIEVE_ATLAS_GLOSSARY.getMessageDefinition(connectorName,
                                                                                                                        atlasGlossaryName));
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                     error.getClass().getName(),
                                                                                                     methodName,
                                                                                                     error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               error.getClass().getName(),
                                                                                                               methodName,
                                                                                                               error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.  This connector is only interested in
     * glossaries, glossary categories and glossary terms.   The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        if (! myContext.isRefreshInProgress())
        {
            try
            {
                ElementHeader elementHeader = event.getElementHeader();

                if ((elementHeader != null) && (! isAtlasOwnedElement(elementHeader)))
                {
                    /*
                     * Understand the type of the element that has changed to determine if it is of interest.
                     */
                    if (myContext.isTypeOf(elementHeader, "Glossary"))
                    {
                        if (validGlossaryElement(elementHeader))
                        {
                            processEgeriaGlossary(glossaryExchangeService.getGlossaryByGUID(elementHeader.getGUID(), null));
                        }
                    }
                    else if (myContext.isTypeOf(elementHeader, "GlossaryTerm"))
                    {
                        if (validTermElement(elementHeader))
                        {
                            processEgeriaGlossaryTerm(glossaryExchangeService.getGlossaryTermByGUID(elementHeader.getGUID(), null));
                        }
                    }
                    else if (myContext.isTypeOf(elementHeader, "GlossaryCategory"))
                    {
                        if (validCategoryElement(elementHeader))
                        {
                            processEgeriaGlossaryCategory(glossaryExchangeService.getGlossaryCategoryByGUID(elementHeader.getGUID(), null));
                        }
                    }
                }
            }
            catch (InvalidParameterException error)
            {
                // Ignore as likely to be a deleted element
            }
            catch (Exception error)
            {
                final String methodName = "processEvent";

                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()),
                                          error);
                }
            }
        }
    }


    /* =============================================================================================
     * Extract the guids from elements.  If the GUIDs are null then the element needs to be created.
     */

    /**
     * Check that the open metadata element is from an appropriate glossary.
     *
     * @param elementHeader details of the element to check
     * @return boolean flag - true means it is ok to process the element
     * @throws ConnectorCheckedException unexpected problem connecting to Egeria.
     */
    private boolean validGlossaryElement(ElementHeader elementHeader) throws ConnectorCheckedException
    {
        final String methodName = "validGlossaryElement";

        if (fixedEgeriaGlossaryQualifiedName != null)
        {
            String glossaryGUID = elementHeader.getGUID();

            /*
             * Set up the cached glossary GUID if not already set up.
             */
            if (fixedEgeriaGlossaryGUID == null)
            {
                GlossaryElement glossaryElement = this.getGlossaryElement(fixedEgeriaGlossaryQualifiedName, methodName);

                if (glossaryElement != null)
                {
                    fixedEgeriaGlossaryGUID = glossaryElement.getElementHeader().getGUID();
                }
            }

            return glossaryGUID.equals(fixedEgeriaGlossaryGUID);
        }

        return true;
    }


    /**
     * Check that the open metadata element is from an appropriate glossary.
     *
     * @param elementHeader details of the element to check
     * @return boolean flag - true means it is ok to process the element
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     * @throws ConnectorCheckedException problem with connector mechanism
     */
    private boolean validTermElement(ElementHeader elementHeader) throws ConnectorCheckedException,
                                                                         InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "validTermElement";

        if (fixedEgeriaGlossaryQualifiedName != null)
        {
            GlossaryElement glossary = glossaryExchangeService.getGlossaryForTerm(elementHeader.getGUID(), new Date());

            if (glossary != null)
            {
                /*
                 * Set up the cached glossary GUID if not already set up.
                 */
                if (fixedEgeriaGlossaryGUID == null)
                {
                    GlossaryElement fixedGlossaryElement = this.getGlossaryElement(fixedEgeriaGlossaryQualifiedName, methodName);

                    if (fixedGlossaryElement != null)
                    {
                        fixedEgeriaGlossaryGUID = fixedGlossaryElement.getElementHeader().getGUID();
                    }
                }

                return glossary.getElementHeader().getGUID().equals(fixedEgeriaGlossaryGUID);
            }
            else
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Check that the open metadata element is from an appropriate glossary.
     *
     * @param elementHeader details of the element to check
     * @return boolean flag - true means it is ok to process the element
     * @throws ConnectorCheckedException unexpected problem connecting to Egeria.
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     * @throws ConnectorCheckedException problem with connector mechanism
     */
    private boolean validCategoryElement(ElementHeader elementHeader) throws ConnectorCheckedException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "validCategoryElement";

        if (fixedEgeriaGlossaryQualifiedName != null)
        {
            GlossaryElement glossary = glossaryExchangeService.getGlossaryForCategory(elementHeader.getGUID(), new Date());

            if (glossary != null)
            {
                /*
                 * Set up the cached glossary GUID if not already set up.
                 */
                if (fixedEgeriaGlossaryGUID == null)
                {
                    GlossaryElement fixedGlossaryElement = this.getGlossaryElement(fixedEgeriaGlossaryQualifiedName, methodName);

                    if (fixedGlossaryElement != null)
                    {
                        fixedEgeriaGlossaryGUID = fixedGlossaryElement.getElementHeader().getGUID();
                    }
                }

                return glossary.getElementHeader().getGUID().equals(fixedEgeriaGlossaryGUID);
            }
            else
            {
                return false;
            }
        }

        return true;
    }


    /**
     * Return the unique identifier for the equivalent element in Apache Atlas.
     *
     * @param metadataElement retrieved metadata element
     * @return string guid
     */
    private String getAtlasGUID(MetadataElement metadataElement)
    {
        if (metadataElement.getCorrelationHeaders() != null)
        {
            for (MetadataCorrelationHeader metadataCorrelationHeader : metadataElement.getCorrelationHeaders())
            {
                if (metadataCorrelationHeader.getExternalIdentifierName().equals(atlasGUIDPropertyName))
                {
                    return metadataCorrelationHeader.getExternalIdentifier();
                }
            }
        }

        return null;
    }


    /**
     * Return the unique identifier for the equivalent element in Apache Atlas.
     *
     * @param metadataElement retrieved metadata element
     * @return string guid
     */
    private String retryGetAtlasGUID(GlossaryCategoryElement metadataElement)
    {
        try
        {
            GlossaryCategoryElement refreshedCategory = glossaryExchangeService.getGlossaryCategoryByGUID(metadataElement.getElementHeader().getGUID(),
                                                                                                          new Date());

            return this.getAtlasGUID(refreshedCategory);
        }
        catch (Exception notFound)
        {
            // try again later.
        }

        return null;
    }


    /**
     * Return the unique identifier for the equivalent element in Apache Atlas.
     *
     * @param metadataElement retrieved metadata element
     * @return string guid
     */
    private String retryGetAtlasGUID(GlossaryTermElement metadataElement)
    {
        try
        {
            GlossaryTermElement refreshedTerm = glossaryExchangeService.getGlossaryTermByGUID(metadataElement.getElementHeader().getGUID(),
                                                                                              new Date());

            return this.getAtlasGUID(refreshedTerm);
        }
        catch (Exception notFound)
        {
            // try again later.
        }

        return null;
    }


    /**
     * Return the unique identifier for the equivalent element in the open metadata ecosystem.
     *
     * @param atlasElement retrieved atlas element
     * @return string guid
     */
    private String getEgeriaGUID(AtlasGlossaryBaseProperties atlasElement)
    {
        if (atlasElement.getAdditionalAttributes() != null)
        {
            Object guidProperty = atlasElement.getAdditionalAttributes().get(egeriaGUIDPropertyName);

            if (guidProperty != null)
            {
                return guidProperty.toString();
            }
        }

        return null;
    }


    /* ================================================================
     * Determine which direction that metadata is flowing
     */

    /**
     * Return a flag indicating whether an element retrieved from the open metadata ecosystem.
     *
     * @param elementHeader header from open metadata ecosystem
     * @return boolean flag
     */
    private boolean isAtlasOwnedElement(ElementHeader elementHeader)
    {
        return (elementHeader.getOrigin().getHomeMetadataCollectionId() != null) &&
               (elementHeader.getOrigin().getHomeMetadataCollectionId().equals(myContext.getAssetManagerGUID()));
    }


    /**
     * Return a flag to indicate whether an atlas glossary element is owned by Egeria (open metadata ecosystem)
     * or is owned by Apache Atlas.  This becomes important around delete scenarios.
     *
     * @param properties properties of the Atlas element
     * @return boolean - true means the element originated in the open metadata ecosystem
     */
    private boolean isEgeriaOwned(AtlasGlossaryBaseProperties properties)
    {
        Map<String, Object> additionalAttributes = properties.getAdditionalAttributes();

        return (additionalAttributes != null) &&
               (additionalAttributes.containsKey(egeriaOwnedPropertyName)) &&
               ("true".equals(additionalAttributes.get(egeriaOwnedPropertyName).toString()));
    }


    /* =====================================================================================
     * Take a specific glossary element and determine which way to synchronize metadata.
     */

    /**
     * The connector is about to process a glossary from the open metadata ecosystem.  The origin of this element is unknown.
     *
     * @param egeriaGlossary open metadata glossary
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void processEgeriaGlossary(GlossaryElement egeriaGlossary) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "processEgeriaGlossary";

        if (egeriaGlossary != null)
        {
            /*
             * First stage is to ensure the properties of the glossary root element are synchronized.
             */
            if (isAtlasOwnedElement(egeriaGlossary.getElementHeader()))
            {
                /*
                 * This element originated in Apache Atlas.  The element from the open metadata ecosystem is just a copy.  Therefore,
                 * we refresh its content from the Apache Atlas original.
                 */
                String atlasGlossaryGUID = getAtlasGUID(egeriaGlossary);
                if (atlasGlossaryGUID != null)
                {
                    refreshAtlasGlossaryInEgeria(atlasClient.getAtlasGlossary(atlasGlossaryGUID), egeriaGlossary);
                }
                else if (auditLog != null)
                {
                    /*
                     * Something has caused the external identifier for this
                     */
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.EGERIA_GUID_MISSING.getMessageDefinition("Glossary",
                                                                                                      egeriaGlossary.getElementHeader().getGUID()));
                }
            }
            else
            {
                /*
                 * This element originated in the open metadata ecosystem, so it is possible to copy it directly to Apache Atlas.
                 */
                refreshEgeriaGlossaryInAtlas(egeriaGlossary);
            }

            /*
             * Once the root element is synchronized, the categories and then the terms are synchronized.
             * It is possible that these elements have different ownership to the root glossary element.
             */
            processEgeriaGlossaryCategories(egeriaGlossary);
            processEgeriaGlossaryTerms(egeriaGlossary);
        }
    }


    /**
     * The connector is processing a glossary from the open metadata ecosystem.  It is ready to step through each term in the glossary.
     * The origin of each term is unknown.
     *
     * @param glossary open metadata glossary
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void processEgeriaGlossaryTerms(GlossaryElement glossary) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        if (glossary != null)
        {
            /*
             * Step through the terms in the Egeria Glossary and make sure all term properties are synchronized.
             */
            int startFrom = 0;
            List<GlossaryTermElement> egeriaGlossaryTerms = glossaryExchangeService.getTermsForGlossary(glossary.getElementHeader().getGUID(),
                                                                                                        startFrom,
                                                                                                        myContext.getMaxPageSize(),
                                                                                                        new Date());

            while (egeriaGlossaryTerms != null)
            {
                for (GlossaryTermElement egeriaGlossaryTerm : egeriaGlossaryTerms)
                {
                    this.processEgeriaGlossaryTerm(egeriaGlossaryTerm);
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                egeriaGlossaryTerms = glossaryExchangeService.getTermsForGlossary(glossary.getElementHeader().getGUID(),
                                                                                  startFrom,
                                                                                  myContext.getMaxPageSize(),
                                                                                  new Date());
            }
        }
    }


    /**
     * Copy the content of open metadata ecosystem glossary term to Apache Atlas.
     *
     * @param glossaryTermElement open metadata glossaryTermElement
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    private void processEgeriaGlossaryTerm(GlossaryTermElement glossaryTermElement) throws PropertyServerException,
                                                                                           InvalidParameterException,
                                                                                           UserNotAuthorizedException
    {
        if (glossaryTermElement != null)
        {
            final String methodName = "processEgeriaGlossaryTerm";

            if (isAtlasOwnedElement(glossaryTermElement.getElementHeader()))
            {
                /*
                 * This element originated in Apache Atlas.  The element from the open metadata ecosystem is just a copy.  Therefore,
                 * we refresh its content from the Apache Atlas original.
                 */
                String atlasGlossaryTermGUID = getAtlasGUID(glossaryTermElement);
                if (atlasGlossaryTermGUID != null)
                {
                    refreshAtlasGlossaryTermInEgeria(atlasClient.getAtlasGlossaryTerm(atlasGlossaryTermGUID), glossaryTermElement);
                }
                else if (auditLog != null)
                {
                    /*
                     * Something has caused the external identifier for this
                     */
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.EGERIA_GUID_MISSING.getMessageDefinition("GlossaryTerm",
                                                                                                      glossaryTermElement.getElementHeader().getGUID()));
                }
            }
            else
            {
                /*
                 * This element originated in the open metadata ecosystem, so it is possible to copy it directly to Apache Atlas.
                 */
                refreshEgeriaGlossaryTermInAtlas(glossaryTermElement, this.getDestinationAtlasGlossary(glossaryTermElement.getElementHeader()));
            }
        }
    }


    /**
     * The connector is processing a glossary from the open metadata ecosystem.  It is ready to step through each category in the glossary.
     * The origin of each category is unknown.
     *
     * @param egeriaGlossary open metadata glossary
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void processEgeriaGlossaryCategories(GlossaryElement egeriaGlossary) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        if (egeriaGlossary != null)
        {
            /*
             * Step through the categories in the Egeria Glossary and make sure all category properties are synchronized.
             */
            int startFrom = 0;
            List<GlossaryCategoryElement> egeriaGlossaryCategories = glossaryExchangeService.getCategoriesForGlossary(egeriaGlossary.getElementHeader().getGUID(),
                                                                                                                      startFrom,
                                                                                                                      myContext.getMaxPageSize(),
                                                                                                                      new Date());

            while (egeriaGlossaryCategories != null)
            {
                for (GlossaryCategoryElement egeriaGlossaryCategory : egeriaGlossaryCategories)
                {
                    this.processEgeriaGlossaryCategory(egeriaGlossaryCategory);
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                egeriaGlossaryCategories = glossaryExchangeService.getCategoriesForGlossary(egeriaGlossary.getElementHeader().getGUID(),
                                                                                            startFrom,
                                                                                            myContext.getMaxPageSize(),
                                                                                            new Date());
            }
        }
    }


    /**
     * Copy the content of open metadata ecosystem glossary category to Apache Atlas.
     *
     * @param glossaryCategoryElement open metadata glossaryCategoryElement
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void processEgeriaGlossaryCategory(GlossaryCategoryElement glossaryCategoryElement) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "processEgeriaGlossaryCategory";

        if (isAtlasOwnedElement(glossaryCategoryElement.getElementHeader()))
        {
            /*
             * This element originated in Apache Atlas.  The element from the open metadata ecosystem is just a copy.  Therefore,
             * we refresh its content from the Apache Atlas original.
             */
            String atlasGlossaryCategoryGUID = getAtlasGUID(glossaryCategoryElement);
            if (atlasGlossaryCategoryGUID != null)
            {
                refreshAtlasGlossaryCategoryInEgeria(atlasClient.getAtlasGlossaryCategory(atlasGlossaryCategoryGUID),
                                                     glossaryCategoryElement);
            }
            else if (auditLog != null)
            {
                /*
                 * Something has caused the external identifier for this
                 */
                auditLog.logMessage(methodName,
                                    ApacheAtlasAuditCode.EGERIA_GUID_MISSING.getMessageDefinition("GlossaryCategory",
                                                                                                  glossaryCategoryElement.getElementHeader().getGUID()));
            }
        }
        else
        {
            /*
             * This element originated in the open metadata ecosystem, so it is possible to copy it directly to Apache Atlas.
             */
            refreshEgeriaGlossaryCategoryInAtlas(this.getDestinationAtlasGlossary(glossaryCategoryElement.getElementHeader()),
                                                 glossaryCategoryElement);
        }
    }


    /**
     * Load glossary that originated in Apache Atlas into the open metadata ecosystem.
     *
     * @param atlasGlossaryElement glossary retrieved from Apache Atlas
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    private void processAtlasGlossary(AtlasGlossaryElement atlasGlossaryElement) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "processAtlasGlossary";

        String egeriaGlossaryGUID = this.getEgeriaGUID(atlasGlossaryElement);

        if (egeriaGlossaryGUID == null)
        {
            /*
             * Assume the glossary is Atlas-owned because it has not been synchronized with Egeria.
             */
            this.createAtlasGlossaryInEgeria(atlasGlossaryElement);
            this.processAtlasGlossaryCategories(atlasGlossaryElement);
            this.processAtlasGlossaryTerms(atlasGlossaryElement);
        }
        else if (this.isEgeriaOwned(atlasGlossaryElement))
        {
            GlossaryElement egeriaGlossaryElement = null;

            try
            {
                egeriaGlossaryElement = glossaryExchangeService.getGlossaryByGUID(egeriaGlossaryGUID, new Date());
            }
            catch (InvalidParameterException missingGlossary)
            {
                /*
                 * The Egeria glossary has been unilaterally deleted - so remove the atlas equivalent.
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.EGERIA_GLOSSARY_DELETED.getMessageDefinition(egeriaGlossaryGUID, atlasGlossaryElement.getName()));
                }
            }

            if (egeriaGlossaryElement == null)
            {
                atlasClient.deleteAtlasGlossary(atlasGlossaryElement);
            }
            else
            {
                this.refreshEgeriaGlossaryInAtlas(egeriaGlossaryElement);
                this.processEgeriaGlossaryCategories(egeriaGlossaryElement);
                this.processEgeriaGlossaryTerms(egeriaGlossaryElement);
            }
        }
        else // Atlas Owned
        {
            AtlasGlossaryElement owningAtlasGlossary   = atlasGlossaryElement;
            GlossaryElement      egeriaGlossaryElement = null;

            try
            {
                egeriaGlossaryElement = glossaryExchangeService.getGlossaryByGUID(egeriaGlossaryGUID, new Date());
            }
            catch (InvalidParameterException missingGlossary)
            {
                /*
                 * The Egeria glossary has been unilaterally deleted - so put it back.
                 * First remove the GUID for the deleted glossary from the Apache Atlas Glossary
                 * and then make a new copy the Atlas glossary in the open metadata ecosystem.
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.REPLACING_EGERIA_GLOSSARY.getMessageDefinition(egeriaGlossaryGUID,
                                                                                                            atlasGlossaryElement.getName(),
                                                                                                            connectorName));
                }

                owningAtlasGlossary = this.clearEgeriaGUIDFromGlossary(atlasGlossaryElement);
            }

            this.refreshAtlasGlossaryInEgeria(owningAtlasGlossary, egeriaGlossaryElement);

            this.processAtlasGlossaryCategories(owningAtlasGlossary);
            this.processAtlasGlossaryTerms(owningAtlasGlossary);
        }
    }


    /**
     * Process each glossary term attached to an Atlas glossary.
     *
     * @param atlasGlossaryElement glossary from Atlas which includes a list of terms for the glossary
     * @throws PropertyServerException problem communicating with Egeria or Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     */
    private void processAtlasGlossaryTerms(AtlasGlossaryElement atlasGlossaryElement) throws PropertyServerException,
                                                                                             InvalidParameterException,
                                                                                             UserNotAuthorizedException
    {
        if (atlasGlossaryElement != null)
        {
            if (atlasGlossaryElement.getTerms() != null)
            {
                for (AtlasRelatedTermHeader relatedAtlasTerm : atlasGlossaryElement.getTerms())
                {
                    processAtlasGlossaryTerm(atlasClient.getAtlasGlossaryTerm(relatedAtlasTerm.getTermGuid()), atlasGlossaryElement);
                }
            }
        }
    }


    /**
     * Process an individual terms from an atlas glossary - at this point we do not know if it is owned by Atlas or Egeria.
     *
     * @param atlasGlossaryTerm term to process
     * @param atlasGlossary glossary where it came from
     * @throws PropertyServerException problem communicating with Egeria or Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     */
    private void processAtlasGlossaryTerm(AtlasGlossaryTermElement  atlasGlossaryTerm,
                                          AtlasGlossaryElement      atlasGlossary) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "processAtlasGlossaryTerm";

        String egeriaTermGUID = this.getEgeriaGUID(atlasGlossaryTerm);

        if (egeriaTermGUID == null)
        {
            /*
             * Assume the term is Atlas-owned because it has not been synchronized with Egeria.
             */
            this.createAtlasGlossaryTermInEgeria(atlasGlossaryTerm, this.getEgeriaDestinationGlossaryForElement(atlasGlossaryTerm));
        }
        else if (this.isEgeriaOwned(atlasGlossaryTerm))
        {
            GlossaryTermElement egeriaGlossaryTerm = null;

            try
            {
                egeriaGlossaryTerm = glossaryExchangeService.getGlossaryTermByGUID(egeriaTermGUID, new Date());
            }
            catch (InvalidParameterException missingTerm)
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.EGERIA_GLOSSARY_TERM_DELETED.getMessageDefinition(egeriaTermGUID,
                                                                                                               atlasGlossaryTerm.getName()));
                }
            }

            if (egeriaGlossaryTerm == null)
            {
                atlasClient.deleteAtlasGlossaryTerm(atlasGlossaryTerm);
            }
            else
            {
                this.refreshEgeriaGlossaryTermInAtlas(egeriaGlossaryTerm, atlasGlossary);
            }
        }
        else // Atlas Owned
        {
            AtlasGlossaryTermElement owningAtlasTerm = atlasGlossaryTerm;
            GlossaryTermElement egeriaGlossaryTerm = null;

            try
            {
                egeriaGlossaryTerm = glossaryExchangeService.getGlossaryTermByGUID(egeriaTermGUID, new Date());
            }
            catch (InvalidParameterException missingTerm)
            {
                /*
                 * The Egeria glossary term has been unilaterally deleted - so put it back.
                 * First remove the GUID for the deleted glossary from the Apache Atlas Glossary
                 * and then make a new copy the Atlas glossary in the open metadata ecosystem.
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.REPLACING_EGERIA_GLOSSARY_TERM.getMessageDefinition(egeriaTermGUID,
                                                                                                                 atlasGlossaryTerm.getName(),
                                                                                                                 connectorName));
                }
                owningAtlasTerm = this.clearEgeriaGUIDFromTerm(atlasGlossaryTerm);
            }

            if (egeriaGlossaryTerm == null)
            {
                this.createAtlasGlossaryTermInEgeria(owningAtlasTerm,
                                                     this.getEgeriaDestinationGlossaryForElement(owningAtlasTerm));
            }
            else
            {
                this.refreshAtlasGlossaryTermInEgeria(owningAtlasTerm, egeriaGlossaryTerm);
            }
        }
    }


    /**
     * Step through all the categories in an Atlas glossary to determine what needs to be synchronized.
     *
     * @param atlasGlossaryElement atlas glossary to process
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void processAtlasGlossaryCategories(AtlasGlossaryElement atlasGlossaryElement) throws PropertyServerException,
                                                                                                  InvalidParameterException,
                                                                                                  UserNotAuthorizedException
    {
        if (atlasGlossaryElement != null)
        {
            if (atlasGlossaryElement.getCategories() != null)
            {
                for (AtlasRelatedCategoryHeader relatedAtlasCategory : atlasGlossaryElement.getCategories())
                {
                    processAtlasGlossaryCategory(atlasClient.getAtlasGlossaryCategory(relatedAtlasCategory.getCategoryGuid()),
                                                 atlasGlossaryElement);
                }
            }
        }
    }


    /**
     * Synchronize a specific category found in Apache Atlas - the origin of this element is not known at the start.
     *
     * @param atlasGlossaryCategory category of interest
     * @param atlasGlossary full list of glossary contents
     * @throws PropertyServerException problem communicating with Egeria or Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     */
    private void processAtlasGlossaryCategory(AtlasGlossaryCategoryElement  atlasGlossaryCategory,
                                              AtlasGlossaryElement          atlasGlossary) throws PropertyServerException,
                                                                                                  InvalidParameterException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "processAtlasGlossaryCategory";

        String egeriaCategoryGUID = this.getEgeriaGUID(atlasGlossaryCategory);

        if (egeriaCategoryGUID == null)
        {
            /*
             * Assume the category is Atlas-owned because it has not been synchronized with Egeria.
             */
            this.createAtlasGlossaryCategoryInEgeria(atlasGlossaryCategory, this.getEgeriaDestinationGlossaryForElement(atlasGlossaryCategory));
        }
        else if (this.isEgeriaOwned(atlasGlossaryCategory))
        {
            GlossaryCategoryElement egeriaGlossaryCategory = null;

            try
            {
                egeriaGlossaryCategory = glossaryExchangeService.getGlossaryCategoryByGUID(egeriaCategoryGUID, new Date());
            }
            catch (InvalidParameterException missingCategory)
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.EGERIA_GLOSSARY_CATEGORY_DELETED.getMessageDefinition(egeriaCategoryGUID,
                                                                                                                   atlasGlossaryCategory.getName()));
                }
            }

            if (egeriaGlossaryCategory == null)
            {
                atlasClient.deleteAtlasGlossaryCategory(atlasGlossaryCategory);
            }
            else
            {
                this.refreshEgeriaGlossaryCategoryInAtlas(atlasGlossary, egeriaGlossaryCategory);
            }
        }
        else // Atlas Owned
        {
            AtlasGlossaryCategoryElement owningAtlasCategory = atlasGlossaryCategory;
            GlossaryCategoryElement egeriaGlossaryCategory = null;

            try
            {
                egeriaGlossaryCategory = glossaryExchangeService.getGlossaryCategoryByGUID(egeriaCategoryGUID, new Date());
            }
            catch (InvalidParameterException missingCategory)
            {
                /*
                 * The Egeria glossary category has been unilaterally deleted - so put it back.
                 * First remove the GUID for the deleted glossary from the Apache Atlas Glossary
                 * and then make a new copy the Atlas glossary in the open metadata ecosystem.
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.REPLACING_EGERIA_GLOSSARY_CATEGORY.getMessageDefinition(egeriaCategoryGUID,
                                                                                                                     atlasGlossaryCategory.getName(),
                                                                                                                     connectorName));
                }
                owningAtlasCategory = this.clearEgeriaGUIDFromCategory(atlasGlossaryCategory);
            }

            if (egeriaGlossaryCategory == null)
            {
                this.createAtlasGlossaryCategoryInEgeria(owningAtlasCategory,
                                                         this.getEgeriaDestinationGlossaryForElement(owningAtlasCategory));
            }
            else
            {
                this.refreshAtlasGlossaryCategoryInEgeria(owningAtlasCategory, egeriaGlossaryCategory);
            }
        }
    }


    /* ======================================================================
     * Control the exchange of metadata in one direction.  Callers have determined the correct direction
     * that metadata is flowing.
     */

    /**
     * Copy the content of open metadata ecosystem glossary to Apache Atlas.
     *
     * @param egeriaGlossaryElement open metadata glossary
     */
    private void refreshEgeriaGlossaryInAtlas(GlossaryElement egeriaGlossaryElement)
    {
        if (egeriaGlossaryElement != null)
        {
            AtlasGlossaryElement atlasGlossary = this.getDestinationAtlasGlossary(egeriaGlossaryElement.getElementHeader());

            if (auditLog != null)
            {
                final String methodName = "refreshEgeriaGlossaryTermInAtlas";
                String atlasGlossaryName = null;

                if (atlasGlossary != null)
                {
                    atlasGlossaryName = atlasGlossary.getName();
                }

                auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_EGERIA_GLOSSARY.getMessageDefinition(connectorName,
                                                                                                               egeriaGlossaryElement.getGlossaryProperties().getDisplayName(),
                                                                                                               egeriaGlossaryElement.getElementHeader().getGUID(),
                                                                                                               atlasGlossaryName));
            }
        }
    }


    /**
     * Copy the content of open metadata ecosystem glossary term to Apache Atlas.
     * This is called when a change is made to a glossary term in the open metadata ecosystem.
     * There are lots of strange timing windows with events and so any anomaly found in the open metadata system
     * results in the term being ignored.
     *
     * @param egeriaGlossaryTerm open metadata glossary term
     * @param atlasGlossaryDestination the Atlas glossary where the term is to be copied to
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem communicating either with the open metadata access server or Apache Atlas
     */
    private void refreshEgeriaGlossaryTermInAtlas(GlossaryTermElement  egeriaGlossaryTerm,
                                                  AtlasGlossaryElement atlasGlossaryDestination) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException
    {
        final String methodName = "refreshEgeriaGlossaryTermInAtlas";

        if (egeriaGlossaryTerm != null)
        {
            String atlasGlossaryTermGUID = this.getAtlasGUID(egeriaGlossaryTerm);
            AtlasGlossaryTermElement atlasGlossaryTerm = null;

            if (atlasGlossaryTermGUID == null)
            {
                GlossaryElement egeriaGlossary = null;

                try
                {
                    egeriaGlossary = glossaryExchangeService.getGlossaryForTerm(egeriaGlossaryTerm.getElementHeader().getGUID(), new Date());
                }
                catch (Exception error)
                {
                    /*
                     * The glossary is not found - ignore the term for now - it is probably a timing window and the term is about to be deleted
                     */
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                              error);
                    }
                }

                if (egeriaGlossary != null)
                {
                    String atlasGlossaryGUID = getAtlasGUID(egeriaGlossary);

                    if (atlasGlossaryGUID != null)
                    {
                        atlasGlossaryTerm = this.getAtlasGlossaryTermProperties(egeriaGlossaryTerm, null, atlasGlossaryDestination);

                        int termNameIndex = 1;
                        while (atlasGlossaryTermGUID == null)
                        {
                            try
                            {
                                atlasGlossaryTermGUID = atlasClient.createAtlasGlossaryTerm(atlasGlossaryTerm);
                            }
                            catch (NameConflictException conflictException)
                            {
                                atlasGlossaryTermGUID = retryGetAtlasGUID(egeriaGlossaryTerm);

                                if (atlasGlossaryTermGUID == null)
                                {
                                    if (auditLog != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            ApacheAtlasAuditCode.TERM_ALREADY_EXISTS.getMessageDefinition(atlasGlossaryTerm.getName()));
                                    }

                                    atlasGlossaryTerm.setName(egeriaGlossaryTerm.getGlossaryTermProperties().getDisplayName() + " (" + termNameIndex++ + ")");
                                }
                            }
                        }

                        if (auditLog != null)
                        {

                            String atlasTermName = atlasGlossaryTermGUID;

                            if (atlasGlossaryTerm != null)
                            {
                                atlasTermName = atlasGlossaryTerm.getName();
                            }

                            auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_EGERIA_GLOSSARY_TERM.getMessageDefinition(connectorName,
                                                                                                                                egeriaGlossaryTerm.getGlossaryTermProperties().getDisplayName(),
                                                                                                                                egeriaGlossaryTerm.getElementHeader().getGUID(),
                                                                                                                                atlasTermName));
                        }

                        atlasGlossaryTerm.setGuid(atlasGlossaryTermGUID);
                    }
                }
            }

            if (atlasGlossaryTermGUID != null)
            {
                /*
                 * The term exists in Atlas so just update its properties.
                 */
                if (atlasGlossaryTerm == null)
                {
                    atlasGlossaryTerm = this.getAtlasGlossaryTermProperties(egeriaGlossaryTerm,
                                                                            atlasGlossaryTermGUID,
                                                                            atlasGlossaryDestination);
                }

                this.ensureAtlasExternalIdentifier(egeriaGlossaryTerm, atlasGlossaryTermGUID);

                List<GlossaryCategoryElement> categories = glossaryExchangeService.getCategoriesForTerm(egeriaGlossaryTerm.getElementHeader().getGUID(),
                                                                                                        0,
                                                                                                        myContext.getMaxPageSize(),
                                                                                                        new Date());
                if (categories != null)
                {
                    List<AtlasRelatedCategoryHeader> atlasCategories = new ArrayList<>();

                    for (GlossaryCategoryElement category : categories)
                    {
                        String atlasCategoryGUID = this.getAtlasGUID(category);

                        if (atlasCategoryGUID != null)
                        {
                            AtlasRelatedCategoryHeader atlasCategory = new AtlasRelatedCategoryHeader();

                            atlasCategory.setCategoryGuid(atlasCategoryGUID);

                            atlasCategories.add(atlasCategory);
                        }
                    }

                    if (! atlasCategories.isEmpty())
                    {
                        atlasGlossaryTerm.setCategories(atlasCategories);
                    }
                }

                atlasClient.saveAtlasGlossaryTerm(atlasGlossaryTerm);
            }
        }
    }


    /**
     * Copy the content of open metadata ecosystem glossary to Apache Atlas.
     *
     * @param atlasGlossaryDestination information about the Apache Atlas glossary where the Egeria category is to be copied.
     * @param egeriaGlossaryCategory open metadata glossary category
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem communicating either with the open metadata access server or Apache Atlas
     */
    private void refreshEgeriaGlossaryCategoryInAtlas(AtlasGlossaryElement    atlasGlossaryDestination,
                                                      GlossaryCategoryElement egeriaGlossaryCategory) throws PropertyServerException,
                                                                                                             InvalidParameterException,
                                                                                                             UserNotAuthorizedException
    {
        final String methodName = "refreshEgeriaGlossaryCategoryInAtlas";

        if (egeriaGlossaryCategory != null)
        {
            String atlasGlossaryCategoryGUID = this.getAtlasGUID(egeriaGlossaryCategory);
            AtlasGlossaryCategoryElement atlasGlossaryCategory = null;

            if (atlasGlossaryCategoryGUID == null)
            {
                GlossaryElement egeriaGlossary = null;

                try
                {
                    egeriaGlossary = glossaryExchangeService.getGlossaryForCategory(egeriaGlossaryCategory.getElementHeader().getGUID(), new Date());
                }
                catch (Exception error)
                {
                    /*
                     * The glossary is not found - ignore the term for now - it is probably a timing window and the term is about to be deleted
                     */
                    if (auditLog != null)
                    {
                        auditLog.logException(methodName,
                                              ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             error.getClass().getName(),
                                                                                                             methodName,
                                                                                                             error.getMessage()),
                                              error);
                    }
                }

                if (egeriaGlossary != null)
                {
                    String atlasGlossaryGUID = getAtlasGUID(egeriaGlossary);

                    if (atlasGlossaryGUID != null)
                    {
                        atlasGlossaryCategory = this.getAtlasGlossaryCategoryProperties(egeriaGlossaryCategory, null, atlasGlossaryDestination);

                        int categoryNameIndex = 1;
                        while (atlasGlossaryCategoryGUID == null)
                        {
                            try
                            {
                                atlasGlossaryCategoryGUID = atlasClient.createAtlasGlossaryCategory(atlasGlossaryCategory);
                            }
                            catch (NameConflictException conflictException)
                            {
                                atlasGlossaryCategoryGUID = retryGetAtlasGUID(egeriaGlossaryCategory);

                                if (atlasGlossaryCategoryGUID == null)
                                {
                                    if (auditLog != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            ApacheAtlasAuditCode.CATEGORY_ALREADY_EXISTS.getMessageDefinition(
                                                                    atlasGlossaryCategory.getName()));
                                    }

                                    atlasGlossaryCategory.setName(egeriaGlossaryCategory.getGlossaryCategoryProperties().getDisplayName() + " (" + categoryNameIndex++ + ")");
                                }
                            }
                        }

                        if (auditLog != null)
                        {

                            String atlasCategoryName = atlasGlossaryCategoryGUID;

                            if (atlasGlossaryCategory != null)
                            {
                                atlasCategoryName = atlasGlossaryCategory.getName();
                            }

                            auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_EGERIA_GLOSSARY_CATEGORY.getMessageDefinition(connectorName,
                                                                                                                                    egeriaGlossaryCategory.getGlossaryCategoryProperties().getDisplayName(),
                                                                                                                                    egeriaGlossaryCategory.getElementHeader().getGUID(),
                                                                                                                                    atlasCategoryName));
                        }

                        atlasGlossaryCategory.setGuid(atlasGlossaryCategoryGUID);
                    }
                }
            }

            if (atlasGlossaryCategoryGUID != null)
            {
                this.ensureAtlasExternalIdentifier(egeriaGlossaryCategory, atlasGlossaryCategoryGUID);

                /*
                 * The term exists in Atlas so just update its properties.
                 */
                if (atlasGlossaryCategory == null)
                {
                    atlasGlossaryCategory = this.getAtlasGlossaryCategoryProperties(egeriaGlossaryCategory,
                                                                                    atlasGlossaryCategoryGUID,
                                                                                    atlasGlossaryDestination);
                }

                GlossaryCategoryElement parentCategory = glossaryExchangeService.getGlossaryCategoryParent(egeriaGlossaryCategory.getElementHeader().getGUID(),
                                                                                                           new Date());

                if (parentCategory != null)
                {
                    String atlasParentGUID = this.getAtlasGUID(parentCategory);

                    if (atlasParentGUID != null)
                    {
                        AtlasRelatedCategoryHeader atlasParent = new AtlasRelatedCategoryHeader();
                        atlasParent.setCategoryGuid(atlasParentGUID);

                        atlasGlossaryCategory.setParentCategory(atlasParent);
                    }
                }

                atlasClient.saveAtlasGlossaryCategory(atlasGlossaryCategory);

                if (auditLog != null)
                {
                    String atlasCategoryName = atlasGlossaryCategoryGUID;

                    if (atlasGlossaryCategory != null)
                    {
                        atlasCategoryName = atlasGlossaryCategory.getName();
                    }

                    auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_EGERIA_GLOSSARY_CATEGORY.getMessageDefinition(connectorName,
                                                                                                                            egeriaGlossaryCategory.getGlossaryCategoryProperties().getDisplayName(),
                                                                                                                            egeriaGlossaryCategory.getElementHeader().getGUID(),
                                                                                                                            atlasCategoryName));
                }
            }
        }
    }


    /**
     * Copy the content of the Apache Atlas glossary to the open metadata ecosystem.
     *
     * @param atlasGlossaryElement atlas glossary
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void refreshAtlasGlossaryInEgeria(AtlasGlossaryElement atlasGlossaryElement,
                                              GlossaryElement      egeriaGlossaryElement) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "refreshAtlasGlossaryInEgeria";

        /*
         * The glossary information is successfully retrieved from Apache Atlas.
         */
        if (atlasGlossaryElement != null)
        {
            if (egeriaGlossaryElement == null)
            {
                /*
                 * Create Glossary in Egeria and add the new Glossary's GUID to atlas.
                 */
                atlasGlossaryElement = this.createAtlasGlossaryInEgeria(atlasGlossaryElement);

                String egeriaGlossaryGUID = this.getEgeriaGUID(atlasGlossaryElement);

                if (egeriaGlossaryGUID != null)
                {
                    egeriaGlossaryElement = glossaryExchangeService.getGlossaryByGUID(egeriaGlossaryGUID, new Date());
                }
            }
            else
            {
                GlossaryProperties glossaryProperties = this.getEgeriaGlossaryProperties(atlasGlossaryElement);

                glossaryExchangeService.updateGlossary(getEgeriaGUID(atlasGlossaryElement),
                                                       null,
                                                       true,
                                                       glossaryProperties,
                                                       null);
            }

            if (auditLog != null)
            {
                String egeriaQualifiedName = null;
                String egeriaGUID = null;

                if (egeriaGlossaryElement != null)
                {
                    egeriaQualifiedName = egeriaGlossaryElement.getGlossaryProperties().getQualifiedName();
                    egeriaGUID = egeriaGlossaryElement.getElementHeader().getGUID();
                }

                if (auditLog != null)
                {
                    auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_ATLAS_GLOSSARY.getMessageDefinition(connectorName,
                                                                                                                  atlasGlossaryElement.getName(),
                                                                                                                  egeriaQualifiedName,
                                                                                                                  egeriaGUID));
                }
            }
        }
    }


    /**
     * Create a new glossary in the open metadata ecosystem that matches a  glossary from Atlas.
     *
     * @param atlasGlossaryElement glossary from Atlas that is to be copied to Egeria
     * @return  Atlas glossary updated with GUID from Egeria
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private AtlasGlossaryElement createAtlasGlossaryInEgeria(AtlasGlossaryElement atlasGlossaryElement) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGlossaryElement.getGuid());
        GlossaryProperties           glossaryProperties           = this.getEgeriaGlossaryProperties(atlasGlossaryElement);

        String glossaryGUID = glossaryExchangeService.createGlossary(true,
                                                                     externalIdentifierProperties,
                                                                     glossaryProperties);

        if (auditLog != null)
        {
            final String methodName = "createAtlasGlossaryInEgeria";

            auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_ATLAS_GLOSSARY.getMessageDefinition(connectorName,
                                                                                                          atlasGlossaryElement.getName(),
                                                                                                          glossaryProperties.getQualifiedName(),
                                                                                                          glossaryGUID));
        }

        /*
         * Save the glossaryGUID from Egeria in the Atlas Glossary's AdditionalAttributes.
         */
        Map<String, Object> glossaryAdditionalAttributes = atlasGlossaryElement.getAdditionalAttributes();
        if (glossaryAdditionalAttributes == null)
        {
            glossaryAdditionalAttributes = new HashMap<>();
        }

        glossaryAdditionalAttributes.put(egeriaGUIDPropertyName, glossaryGUID);
        glossaryAdditionalAttributes.put(egeriaOwnedPropertyName, false);
        atlasGlossaryElement.setAdditionalAttributes(glossaryAdditionalAttributes);

        return atlasClient.saveAtlasGlossary(atlasGlossaryElement);
    }


    /**
     * Copy the content of the Apache Atlas glossary term to the open metadata ecosystem.
     *
     * @param atlasGlossaryTerm atlas glossary term
     * @param egeriaGlossaryTerm glossary term to update
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private void refreshAtlasGlossaryTermInEgeria(AtlasGlossaryTermElement atlasGlossaryTerm,
                                                  GlossaryTermElement      egeriaGlossaryTerm) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        GlossaryTermProperties glossaryTermProperties = this.getEgeriaGlossaryTermProperties(atlasGlossaryTerm);

        glossaryExchangeService.updateGlossaryTerm(egeriaGlossaryTerm.getElementHeader().getGUID(),
                                                   atlasGlossaryTerm.getGuid(),
                                                   false,
                                                   glossaryTermProperties,
                                                   new Date());

        if (auditLog != null)
        {
            final String methodName = "refreshAtlasGlossaryTermInEgeria";

            auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_ATLAS_GLOSSARY_TERM.getMessageDefinition(connectorName,
                                                                                                               atlasGlossaryTerm.getName(),
                                                                                                               glossaryTermProperties.getQualifiedName(),
                                                                                                               egeriaGlossaryTerm.getElementHeader().getGUID()));
        }

        this.setUpTermCategoriesInEgeria(egeriaGlossaryTerm.getElementHeader().getGUID(), atlasGlossaryTerm);
        this.setUpTermRelationshipsInEgeria(egeriaGlossaryTerm.getElementHeader().getGUID(), atlasGlossaryTerm);
    }


    /**
     * Set up an Atlas glossary in the open metadata ecosystem.
     *
     * @param atlasGlossaryTerm term retrieved from Apache Atlas
     * @param destinationGlossary Glossary in the open metadata ecosystem to create the term in
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private void createAtlasGlossaryTermInEgeria(AtlasGlossaryTermElement atlasGlossaryTerm,
                                                 GlossaryElement          destinationGlossary) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGlossaryTerm.getGuid());
        GlossaryTermProperties       glossaryTermProperties       = this.getEgeriaGlossaryTermProperties(atlasGlossaryTerm);

        String egeriaTermGUID = glossaryExchangeService.createGlossaryTerm(destinationGlossary.getElementHeader().getGUID(),
                                                                           true,
                                                                           externalIdentifierProperties,
                                                                           glossaryTermProperties,
                                                                           new Date());

        if (auditLog != null)
        {
            final String methodName = "createAtlasGlossaryTermInEgeria";

            auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_ATLAS_GLOSSARY_TERM.getMessageDefinition(connectorName,
                                                                                                               atlasGlossaryTerm.getName(),
                                                                                                               glossaryTermProperties.getQualifiedName(),
                                                                                                               egeriaTermGUID));
        }

        /*
         * Save the egeriaTermGUID from Egeria in the Atlas Glossary Term's AdditionalAttributes.
         */
        Map<String, Object> glossaryAdditionalAttributes = atlasGlossaryTerm.getAdditionalAttributes();
        if (glossaryAdditionalAttributes == null)
        {
            glossaryAdditionalAttributes = new HashMap<>();
        }

        glossaryAdditionalAttributes.put(egeriaGUIDPropertyName, egeriaTermGUID);
        glossaryAdditionalAttributes.put(egeriaOwnedPropertyName, false);
        atlasGlossaryTerm.setAdditionalAttributes(glossaryAdditionalAttributes);

        atlasClient.saveAtlasGlossaryTerm(atlasGlossaryTerm);

        this.setUpTermCategoriesInEgeria(egeriaTermGUID, atlasGlossaryTerm);
        this.setUpTermRelationshipsInEgeria(egeriaTermGUID, atlasGlossaryTerm);
    }


    /**
     * Return the Egeria glossary that matches the glossary where this Atlas element originates from.
     *
     * @param atlasGlossaryMember element to determine the glossary for
     * @return atlas glossary or null
     */
    private GlossaryElement getEgeriaDestinationGlossaryForElement(AtlasGlossaryMemberBaseProperties atlasGlossaryMember)
    {
        if (atlasGlossaryMember != null)
        {
            AtlasGlossaryAnchorElement glossaryAnchorElement = atlasGlossaryMember.getAnchor();

            if (glossaryAnchorElement != null)
            {
                try
                {
                    AtlasGlossaryElement atlasGlossary = atlasClient.getAtlasGlossary(glossaryAnchorElement.getGlossaryGuid());

                    if (atlasGlossary != null)
                    {
                        String egeriaGlossaryGUID = getEgeriaGUID(atlasGlossary);

                        if (egeriaGlossaryGUID != null)
                        {
                            return glossaryExchangeService.getGlossaryByGUID(egeriaGlossaryGUID, new Date());
                        }
                    }
                }
                catch (Exception notFound)
                {
                    /*
                     * The glossary is not found - ignore the element for now - it is probably a timing window, and it is about to be deleted.
                     */
                    if (auditLog != null)
                    {
                        final String methodName = "getEgeriaDestinationGlossaryForElement";
                        auditLog.logException(methodName,
                                              ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                             notFound.getClass().getName(),
                                                                                                             methodName,
                                                                                                             notFound.getMessage()),
                                              notFound);
                    }
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the up-to-date Atlas Glossary to use to update either a glossary term or category.
     * The  Atlas glossary ahs not yet been created, it is created.  If it already exists, it is updated with the latest
     * information from Egeria before being returned.
     *
     * @param elementHeader term or category header.
     * @return atlas glossary element - or null if not possible to synchronize with Apache Atlas
     */
    private AtlasGlossaryElement getDestinationAtlasGlossary(ElementHeader elementHeader)
    {
        try
        {
            GlossaryElement egeriaGlossary = null;

            /*
             * Understand the type of the element since it affects how the glossary is located.
             */
            if (myContext.isTypeOf(elementHeader, "Glossary"))
            {
                egeriaGlossary = glossaryExchangeService.getGlossaryByGUID(elementHeader.getGUID(), null);
            }
            else if (myContext.isTypeOf(elementHeader, "GlossaryTerm"))
            {
                egeriaGlossary = glossaryExchangeService.getGlossaryForTerm(elementHeader.getGUID(), null);
            }
            else if (myContext.isTypeOf(elementHeader, "GlossaryCategory"))
            {
                egeriaGlossary = glossaryExchangeService.getGlossaryForCategory(elementHeader.getGUID(), null);
            }

            if ((egeriaGlossary != null) && (egeriaGlossary.getGlossaryProperties() != null))
            {
                String atlasGlossaryGUID = getAtlasGUID(egeriaGlossary);

                if (atlasGlossaryGUID == null)
                {
                    /*
                     * Need to create the glossary
                     */
                    AtlasGlossaryElement atlasGlossaryElement = this.getAtlasGlossaryProperties(egeriaGlossary, null);

                    atlasGlossaryGUID = atlasClient.createAtlasGlossary(atlasGlossaryElement);

                    this.ensureAtlasExternalIdentifier(egeriaGlossary, atlasGlossaryGUID);
                }
                else
                {
                    /*
                     * Update the glossary properties in case they have changed.
                     */
                    AtlasGlossaryElement atlasGlossaryElement = this.getAtlasGlossaryProperties(egeriaGlossary,
                                                                                                atlasClient.getAtlasGlossary(atlasGlossaryGUID));

                    atlasClient.saveAtlasGlossary(atlasGlossaryElement);

                    this.ensureAtlasExternalIdentifier(egeriaGlossary, atlasGlossaryElement.getGuid());
                }

                if (atlasGlossaryGUID != null)
                {
                    return atlasClient.getAtlasGlossary(atlasGlossaryGUID);
                }

            }
        }
        catch (Exception notFound)
        {
            final String methodName = "getDestinationAtlasGlossary";
            /*
             * The Egeria glossary is not found - ignore the element for now - it is probably a timing window and the term is about to be deleted.
             */
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                     notFound.getClass().getName(),
                                                                                                     methodName + "(" + elementHeader.getGUID() + ")",
                                                                                                     notFound.getMessage()),
                                      notFound);
            }
        }

        return null;
    }


    /**
     * Set up the relationships between a glossary term and its categories defined in Atlas.
     *
     * @param egeriaTermGUID glossary term to work with
     * @param atlasGlossaryTerm atlas equivalent that includes its categories
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private void setUpTermCategoriesInEgeria(String                   egeriaTermGUID,
                                             AtlasGlossaryTermElement atlasGlossaryTerm) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        if (atlasGlossaryTerm.getCategories() != null)
        {
            for (AtlasRelatedCategoryHeader relatedCategory : atlasGlossaryTerm.getCategories())
            {
                AtlasGlossaryCategoryElement atlasGlossaryCategory = atlasClient.getAtlasGlossaryCategory(relatedCategory.getCategoryGuid());

                if (atlasGlossaryCategory != null)
                {
                    String egeriaCategoryGUID = this.getEgeriaGUID(atlasGlossaryCategory);

                    if (egeriaCategoryGUID != null)
                    {
                        glossaryExchangeService.setupTermCategory(egeriaCategoryGUID, egeriaTermGUID, null, new Date());
                    }
                }
            }
        }

        /*
         * This next piece ensures that atlas category relationships that have been deleted are no longer represented in the open metadata ecosystem.
         */
        Map<String, AtlasGlossaryCategoryElement> atlasCategories = getAtlasCategoriesForElement(atlasGlossaryTerm.getCategories());

        int startFrom = 0;
        List<GlossaryCategoryElement> linkedCategories = glossaryExchangeService.getCategoriesForTerm(egeriaTermGUID,
                                                                                                      startFrom,
                                                                                                      myContext.getMaxPageSize(),
                                                                                                      new Date());

        while (linkedCategories != null)
        {
            for (GlossaryCategoryElement egeriaCategory : linkedCategories)
            {
                if (this.isAtlasOwnedElement(egeriaCategory.getElementHeader()))
                {
                    String atlasCategoryGUID = this.getAtlasGUID(egeriaCategory);
                    if (! atlasCategories.containsKey(atlasCategoryGUID))
                    {
                        /*
                         * The category relationship has been deleted in Atlas so should be deleted in the open metadata ecosystem
                         */
                        glossaryExchangeService.clearTermCategory(egeriaCategory.getElementHeader().getGUID(), egeriaTermGUID, new Date());
                    }
                }
            }

            startFrom = startFrom + myContext.getMaxPageSize();
            linkedCategories = glossaryExchangeService.getCategoriesForTerm(egeriaTermGUID,
                                                                            startFrom,
                                                                            myContext.getMaxPageSize(),
                                                                            new Date());
        }
    }


    /**
     * Set up the term to term relationships in the open metadata ecosystem for a term.
     *
     * @param egeriaTermGUID term to work with
     * @param atlasGlossaryTerm details from Atlas to copy
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private void setUpTermRelationshipsInEgeria(String                   egeriaTermGUID,
                                                AtlasGlossaryTermElement atlasGlossaryTerm) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        // todo sort out term-to-term relationships
    }


    /**
     * Copy the content of the Apache Atlas glossary category to the open metadata ecosystem.
     *
     * @param atlasCategory atlas glossary category
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private void refreshAtlasGlossaryCategoryInEgeria(AtlasGlossaryCategoryElement atlasCategory,
                                                      GlossaryCategoryElement      egeriaCategory) throws PropertyServerException,
                                                                                                          InvalidParameterException,
                                                                                                          UserNotAuthorizedException
    {
        GlossaryCategoryProperties glossaryCategoryProperties = this.getEgeriaGlossaryCategoryProperties(atlasCategory);

        glossaryExchangeService.updateGlossaryCategory(egeriaCategory.getElementHeader().getGUID(),
                                                       atlasCategory.getGuid(),
                                                       false,
                                                       glossaryCategoryProperties,
                                                       new Date());

        if (auditLog != null)
        {
            final String methodName = "refreshAtlasGlossaryCategoryInEgeria";

            auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_ATLAS_GLOSSARY_CATEGORY.getMessageDefinition(connectorName,
                                                                                                                   atlasCategory.getName(),
                                                                                                                   glossaryCategoryProperties.getQualifiedName(),
                                                                                                                   egeriaCategory.getElementHeader().getGUID()));
        }

        this.setUpCategoryHierarchyInEgeria(egeriaCategory.getElementHeader().getGUID(), atlasCategory);
    }


    /**
     * Create a new category in the open metadata ecosystem.
     *
     * @param atlasGlossaryCategory category details from atlas to copy
     * @param destinationGlossary Open metadata glossary to add the category to
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private void createAtlasGlossaryCategoryInEgeria(AtlasGlossaryCategoryElement atlasGlossaryCategory,
                                                     GlossaryElement              destinationGlossary) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGlossaryCategory.getGuid());
        GlossaryCategoryProperties   glossaryCategoryProperties   = this.getEgeriaGlossaryCategoryProperties(atlasGlossaryCategory);

        String egeriaCategoryGUID = glossaryExchangeService.createGlossaryCategory(destinationGlossary.getElementHeader().getGUID(),
                                                                                   true,
                                                                                   externalIdentifierProperties,
                                                                                   glossaryCategoryProperties,
                                                                                   (atlasGlossaryCategory.getParentCategory() == null),
                                                                                   new Date());

        if (auditLog != null)
        {
            final String methodName = "createAtlasGlossaryCategoryInEgeria";

            auditLog.logMessage(methodName, ApacheAtlasAuditCode.SYNC_ATLAS_GLOSSARY_CATEGORY.getMessageDefinition(connectorName,
                                                                                                                   atlasGlossaryCategory.getName(),
                                                                                                                   glossaryCategoryProperties.getQualifiedName(),
                                                                                                                   egeriaCategoryGUID));
        }

        /*
         * Save the egeriaCategoryGUID from Egeria in the Atlas Glossary Category's AdditionalAttributes.
         */
        Map<String, Object> glossaryAdditionalAttributes = atlasGlossaryCategory.getAdditionalAttributes();
        if (glossaryAdditionalAttributes == null)
        {
            glossaryAdditionalAttributes = new HashMap<>();
        }

        glossaryAdditionalAttributes.put(egeriaGUIDPropertyName, egeriaCategoryGUID);
        glossaryAdditionalAttributes.put(egeriaOwnedPropertyName, false);
        atlasGlossaryCategory.setAdditionalAttributes(glossaryAdditionalAttributes);

        atlasClient.saveAtlasGlossaryCategory(atlasGlossaryCategory);

        this.setUpCategoryHierarchyInEgeria(egeriaCategoryGUID, atlasGlossaryCategory);
    }


    /**
     * Set up the category hierarchy relationships in the open metadata ecosystem.  This only sets up the parent category.
     * This means it may take multiple calls to refresh() to full establish the category hierarchy.
     *
     * @param egeriaCategoryGUID category to work with
     * @param atlasGlossaryCategory details from Atlas to copy
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private void setUpCategoryHierarchyInEgeria(String                       egeriaCategoryGUID,
                                                AtlasGlossaryCategoryElement atlasGlossaryCategory) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        GlossaryCategoryElement egeriaParentCategory = glossaryExchangeService.getGlossaryCategoryParent(egeriaCategoryGUID, new Date());

        if (egeriaParentCategory == null)
        {
            if (atlasGlossaryCategory.getParentCategory() != null)
            {
                AtlasGlossaryCategoryElement atlasParentCategory = atlasClient.getAtlasGlossaryCategory(atlasGlossaryCategory.getParentCategory().getCategoryGuid());

                if (atlasParentCategory != null)
                {
                    String egeriaParentCategoryGUIDFromAtlas = this.getEgeriaGUID(atlasParentCategory);

                    if (egeriaParentCategoryGUIDFromAtlas != null)
                    {
                        glossaryExchangeService.setupCategoryParent(egeriaParentCategoryGUIDFromAtlas, egeriaCategoryGUID, new Date());
                    }
                }
            }
        }
        else if (atlasGlossaryCategory.getParentCategory() != null)
        {
            /*
             * Both categories have a parent category ... are they the same?
             */
            AtlasGlossaryCategoryElement atlasParentCategory = atlasClient.getAtlasGlossaryCategory(atlasGlossaryCategory.getParentCategory().getCategoryGuid());

            if (atlasParentCategory != null)
            {
                String egeriaParentCategoryGUIDFromAtlas = this.getEgeriaGUID(atlasParentCategory);

                if (! egeriaParentCategory.getElementHeader().getGUID().equals(egeriaParentCategoryGUIDFromAtlas))
                {
                    glossaryExchangeService.clearCategoryParent(egeriaParentCategory.getElementHeader().getGUID(), egeriaCategoryGUID, new Date());
                    glossaryExchangeService.setupCategoryParent(egeriaParentCategoryGUIDFromAtlas, egeriaCategoryGUID, new Date());
                }
            }
        }
        else // egeria has parent category but not in atlas
        {
            if (this.isAtlasOwnedElement(egeriaParentCategory.getElementHeader()))
            {
                glossaryExchangeService.clearCategoryParent(egeriaParentCategory.getElementHeader().getGUID(), egeriaCategoryGUID, new Date());
            }
        }
    }


    /* ======================================
     * Work with Egeria metadata elements
     */


    /**
     * Return an external identifier properties object for an Atlas GUID.
     *
     * @param atlasGUID guid to encode
     * @return properties object
     */
    private ExternalIdentifierProperties getExternalIdentifier(String atlasGUID)
    {
        ExternalIdentifierProperties externalIdentifierProperties = new ExternalIdentifierProperties();
        externalIdentifierProperties.setExternalIdentifier(atlasGUID);
        externalIdentifierProperties.setExternalIdentifierName(atlasGUIDPropertyName);

        return externalIdentifierProperties;
    }


    /**
     * Check that the Atlas GUID is correct stored in the open metadata element.
     *
     * @param egeriaElement element to validate and potentially update
     * @param atlasGUID Unique identifier from Apache Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem connecting with Egeria
     */
    private void ensureAtlasExternalIdentifier(MetadataElement   egeriaElement,
                                               String            atlasGUID) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        /*
         * Check that the Atlas Glossary's GUID is stored as an external identifier.
         */
        if (egeriaElement.getCorrelationHeaders() != null)
        {
            for (MetadataCorrelationHeader header : egeriaElement.getCorrelationHeaders())
            {
                if (atlasGUID.equals(header.getExternalIdentifier()))
                {
                    /*
                     * Atlas GUID is stored as an external identifier.
                     */
                    return;
                }
            }
        }

        /*
         * Set up external Identifier
         */
        ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGUID);

        myContext.addExternalIdentifier(egeriaElement.getElementHeader().getGUID(), egeriaElement.getElementHeader().getType().getTypeName(), externalIdentifierProperties);
    }


    /**
     * Remove the association between an Egeria unique identifier and an Atlas Glossary.
     *
     * @param atlasGlossaryElement Atlas glossary
     * @return updated element
     * @throws PropertyServerException problem updating Atlas
     */
    private AtlasGlossaryElement clearEgeriaGUIDFromGlossary(AtlasGlossaryElement atlasGlossaryElement) throws PropertyServerException
    {
        if (atlasGlossaryElement != null)
        {
            if (atlasGlossaryElement.getAdditionalAttributes() != null)
            {
                Map<String, Object> additionalAttributes = new HashMap<>();

                for (String propertyName : atlasGlossaryElement.getAdditionalAttributes().keySet())
                {
                    if ((! propertyName.equals(egeriaGUIDPropertyName)) && (! propertyName.equals(egeriaOwnedPropertyName)))
                    {
                        additionalAttributes.put(propertyName, atlasGlossaryElement.getAdditionalAttributes().get(propertyName));
                    }
                }

                if (additionalAttributes.isEmpty())
                {
                    additionalAttributes = null;
                }

                atlasGlossaryElement.setAdditionalAttributes(additionalAttributes);

                return atlasClient.saveAtlasGlossary(atlasGlossaryElement);
            }
        }

        return null;
    }


    /**
     * Remove the association between an Egeria unique identifier and an Atlas Glossary Category.
     *
     * @param atlasGlossaryCategory Atlas glossary category
     * @return updated element
     * @throws PropertyServerException problem updating Atlas
     */
    private AtlasGlossaryCategoryElement clearEgeriaGUIDFromCategory(AtlasGlossaryCategoryElement atlasGlossaryCategory) throws PropertyServerException
    {
        if (atlasGlossaryCategory != null)
        {
            if (atlasGlossaryCategory.getAdditionalAttributes() != null)
            {
                Map<String, Object> additionalAttributes = new HashMap<>();

                for (String propertyName : atlasGlossaryCategory.getAdditionalAttributes().keySet())
                {
                    if ((! propertyName.equals(egeriaGUIDPropertyName)) && (! propertyName.equals(egeriaOwnedPropertyName)))
                    {
                        additionalAttributes.put(propertyName, atlasGlossaryCategory.getAdditionalAttributes().get(propertyName));
                    }
                }

                if (additionalAttributes.isEmpty())
                {
                    additionalAttributes = null;
                }

                atlasGlossaryCategory.setAdditionalAttributes(additionalAttributes);

                return atlasClient.saveAtlasGlossaryCategory(atlasGlossaryCategory);
            }
        }

        return null;
    }


    /**
     * Remove the association between an Egeria unique identifier and an Atlas Glossary Term.
     *
     * @param atlasGlossaryTermElement Atlas glossary term
     * @return updated element
     * @throws PropertyServerException problem updating Atlas
     */
    private AtlasGlossaryTermElement clearEgeriaGUIDFromTerm(AtlasGlossaryTermElement atlasGlossaryTermElement) throws PropertyServerException
    {
        if (atlasGlossaryTermElement != null)
        {
            if (atlasGlossaryTermElement.getAdditionalAttributes() != null)
            {
                Map<String, Object> additionalAttributes = new HashMap<>();

                for (String propertyName : atlasGlossaryTermElement.getAdditionalAttributes().keySet())
                {
                    if ((! propertyName.equals(egeriaGUIDPropertyName)) && (! propertyName.equals(egeriaOwnedPropertyName)))
                    {
                        additionalAttributes.put(propertyName, atlasGlossaryTermElement.getAdditionalAttributes().get(propertyName));
                    }
                }

                if (additionalAttributes.isEmpty())
                {
                    additionalAttributes = null;
                }

                atlasGlossaryTermElement.setAdditionalAttributes(additionalAttributes);

                return atlasClient.saveAtlasGlossaryTerm(atlasGlossaryTermElement);
            }
        }

        return null;
    }


    /**
     * Retrieve a specific glossary based on its qualified name.  This glossary is expected to be present.
     *
     * @param glossaryQualifiedName requested glossary
     * @param methodName calling method
     * @return element or null
     * @throws ConnectorCheckedException unexpected exception
     */
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
                        return glossaryElement;
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                     error.getClass().getName(),
                                                                                                     methodName,
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
     * Copy the properties from an Atlas glossary into the Asset Manager Properties bean.
     *
     * @param atlasGlossaryElement source element
     * @return properties bean
     */
    private GlossaryProperties getEgeriaGlossaryProperties(AtlasGlossaryElement  atlasGlossaryElement)
    {
        GlossaryProperties glossaryProperties = new GlossaryProperties();

        glossaryProperties.setQualifiedName("AtlasGlossary." + atlasGlossaryElement.getQualifiedName());
        glossaryProperties.setDisplayName(atlasGlossaryElement.getName());
        if (atlasGlossaryElement.getShortDescription() != null)
        {
            glossaryProperties.setDescription(atlasGlossaryElement.getShortDescription() + "\n\n" + atlasGlossaryElement.getLongDescription());
        }
        else
        {
            glossaryProperties.setDescription(atlasGlossaryElement.getLongDescription());
        }
        glossaryProperties.setLanguage(atlasGlossaryElement.getLanguage());
        glossaryProperties.setUsage(atlasGlossaryElement.getUsage());

        return glossaryProperties;
    }


    /**
     * Set up the properties from an open metadata glossary into an Apache Atlas glossary properties object.
     *
     * @param egeriaGlossary open metadata glossary
     * @param existingGlossary existing glossary element - may be null
     * @return Apache Atlas properties object
     */
    AtlasGlossaryElement getAtlasGlossaryProperties(GlossaryElement      egeriaGlossary,
                                                    AtlasGlossaryElement existingGlossary)
    {
        AtlasGlossaryElement atlasGlossaryElement = new AtlasGlossaryElement();

        if (existingGlossary != null)
        {
            atlasGlossaryElement = existingGlossary;
        }

        atlasGlossaryElement.setName(egeriaGlossary.getGlossaryProperties().getDisplayName());

        if (egeriaGlossary.getGlossaryProperties().getDescription() != null)
        {
            String[] descriptionSentences = egeriaGlossary.getGlossaryProperties().getDescription().split(Pattern.quote(". "));
            if (descriptionSentences.length > 0)
            {
                atlasGlossaryElement.setShortDescription(descriptionSentences[0]);
            }
        }
        atlasGlossaryElement.setLongDescription(egeriaGlossary.getGlossaryProperties().getDescription());
        atlasGlossaryElement.setLanguage(egeriaGlossary.getGlossaryProperties().getLanguage());
        atlasGlossaryElement.setUsage(egeriaGlossary.getGlossaryProperties().getUsage());

        Map<String, Object> additionalAttributes = new HashMap<>();

        additionalAttributes.put(egeriaGUIDPropertyName, egeriaGlossary.getElementHeader().getGUID());
        additionalAttributes.put(egeriaOwnedPropertyName, true);

        atlasGlossaryElement.setAdditionalAttributes(additionalAttributes);

        return atlasGlossaryElement;
    }


    /**
     * Copy the properties from an Atlas glossary term into the Asset Manager Properties bean.
     *
     * @param atlasGlossaryTermElement source element
     * @return properties bean
     */
    private GlossaryTermProperties getEgeriaGlossaryTermProperties(AtlasGlossaryTermElement  atlasGlossaryTermElement)
    {
        GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

        glossaryTermProperties.setQualifiedName("AtlasGlossaryTerm." + atlasGlossaryTermElement.getQualifiedName());
        glossaryTermProperties.setDisplayName(atlasGlossaryTermElement.getName());
        if (atlasGlossaryTermElement.getShortDescription() != null)
        {
            glossaryTermProperties.setDescription(atlasGlossaryTermElement.getShortDescription() + "\n\n" + atlasGlossaryTermElement.getLongDescription());
        }
        else
        {
            glossaryTermProperties.setDescription(atlasGlossaryTermElement.getLongDescription());
        }
        glossaryTermProperties.setAbbreviation(atlasGlossaryTermElement.getAbbreviation());
        glossaryTermProperties.setUsage(atlasGlossaryTermElement.getUsage());

        return glossaryTermProperties;
    }


    /**
     * Set up the properties from an open metadata glossary term into an Apache Atlas glossary term properties object.
     *
     * @param egeriaGlossaryTerm open metadata glossary term
     * @param atlasGlossaryTermGUID unique identifier of glossary term in Atlas
     * @param atlasGlossary glossary details to ensure term name is unique in Atlas
     * @return Apache Atlas properties object
     * @throws PropertyServerException problem calling Atlas
     */
    AtlasGlossaryTermElement getAtlasGlossaryTermProperties(GlossaryTermElement            egeriaGlossaryTerm,
                                                            String                         atlasGlossaryTermGUID,
                                                            AtlasGlossaryElement           atlasGlossary) throws PropertyServerException
    {
        AtlasGlossaryTermElement              atlasGlossaryTermElement;

        if (atlasGlossaryTermGUID != null)
        {
            Map<String, AtlasGlossaryTermElement> existingTerms = this.getAtlasTermsForGlossary(atlasGlossary);

            atlasGlossaryTermElement = atlasClient.getAtlasGlossaryTerm(atlasGlossaryTermGUID);

            atlasGlossaryTermElement.setName(this.getNameForAtlasTerm(egeriaGlossaryTerm.getGlossaryTermProperties().getDisplayName(),
                                                                      atlasGlossaryTermGUID,
                                                                      existingTerms));
        }
        else
        {
            atlasGlossaryTermElement = new AtlasGlossaryTermElement();

            AtlasGlossaryAnchorElement anchorElement = new AtlasGlossaryAnchorElement();
            anchorElement.setGlossaryGuid(atlasGlossary.getGuid());
            anchorElement.setDisplayText(atlasGlossary.getName());

            atlasGlossaryTermElement.setAnchor(anchorElement);
            atlasGlossaryTermElement.setName(egeriaGlossaryTerm.getGlossaryTermProperties().getDisplayName());
        }

        if (egeriaGlossaryTerm.getGlossaryTermProperties().getDescription() != null)
        {
            String[] descriptionSentences = egeriaGlossaryTerm.getGlossaryTermProperties().getDescription().split(Pattern.quote(". "));
            if (descriptionSentences.length > 0)
            {
                atlasGlossaryTermElement.setShortDescription(descriptionSentences[0]);
            }
        }
        atlasGlossaryTermElement.setLongDescription(egeriaGlossaryTerm.getGlossaryTermProperties().getDescription());
        atlasGlossaryTermElement.setAbbreviation(egeriaGlossaryTerm.getGlossaryTermProperties().getAbbreviation());
        atlasGlossaryTermElement.setUsage(egeriaGlossaryTerm.getGlossaryTermProperties().getUsage());

        Map<String, Object> additionalAttributes = new HashMap<>();

        additionalAttributes.put(egeriaGUIDPropertyName, egeriaGlossaryTerm.getElementHeader().getGUID());
        additionalAttributes.put(egeriaOwnedPropertyName, true);

        atlasGlossaryTermElement.setAdditionalAttributes(additionalAttributes);

        return atlasGlossaryTermElement;
    }



    /**
     * Copy the properties from an Atlas glossary category into the Asset Manager Properties bean.
     *
     * @param atlasGlossaryCategoryElement source element
     * @return properties bean
     */
    private GlossaryCategoryProperties getEgeriaGlossaryCategoryProperties(AtlasGlossaryCategoryElement  atlasGlossaryCategoryElement)
    {
        GlossaryCategoryProperties glossaryCategoryProperties = new GlossaryCategoryProperties();

        glossaryCategoryProperties.setQualifiedName("AtlasGlossaryCategory." + atlasGlossaryCategoryElement.getQualifiedName());
        glossaryCategoryProperties.setDisplayName(atlasGlossaryCategoryElement.getName());
        if (atlasGlossaryCategoryElement.getShortDescription() != null)
        {
            glossaryCategoryProperties.setDescription(atlasGlossaryCategoryElement.getShortDescription() + "\n\n" + atlasGlossaryCategoryElement.getLongDescription());
        }
        else
        {
            glossaryCategoryProperties.setDescription(atlasGlossaryCategoryElement.getLongDescription());
        }

        return glossaryCategoryProperties;
    }


    /**
     * Set up the properties from an open metadata glossary category into an Apache Atlas glossary category properties object.
     *
     * @param egeriaGlossaryCategory open metadata glossary category
     * @return Apache Atlas properties object
     * @throws PropertyServerException problem calling Atlas
     */
    AtlasGlossaryCategoryElement getAtlasGlossaryCategoryProperties(GlossaryCategoryElement egeriaGlossaryCategory,
                                                                    String                  atlasGlossaryCategoryGUID,
                                                                    AtlasGlossaryElement    atlasGlossary) throws PropertyServerException
    {
        AtlasGlossaryCategoryElement              atlasGlossaryCategoryElement;

        if (atlasGlossaryCategoryGUID != null)
        {
            Map<String, AtlasGlossaryCategoryElement> existingCategories = this.getAtlasCategoriesForGlossary(atlasGlossary);

            atlasGlossaryCategoryElement = atlasClient.getAtlasGlossaryCategory(atlasGlossaryCategoryGUID);

            atlasGlossaryCategoryElement.setName(this.getNameForAtlasCategory(egeriaGlossaryCategory.getGlossaryCategoryProperties().getDisplayName(),
                                                                              atlasGlossaryCategoryGUID,
                                                                              existingCategories));
        }
        else
        {
            atlasGlossaryCategoryElement = new AtlasGlossaryCategoryElement();

            AtlasGlossaryAnchorElement anchorElement = new AtlasGlossaryAnchorElement();
            anchorElement.setGlossaryGuid(atlasGlossary.getGuid());
            anchorElement.setDisplayText(atlasGlossary.getName());

            atlasGlossaryCategoryElement.setAnchor(anchorElement);

            atlasGlossaryCategoryElement.setName(egeriaGlossaryCategory.getGlossaryCategoryProperties().getDisplayName());
        }

        if (egeriaGlossaryCategory.getGlossaryCategoryProperties().getDescription() != null)
        {
            String[] descriptionSentences = egeriaGlossaryCategory.getGlossaryCategoryProperties().getDescription().split(Pattern.quote(". "));
            if (descriptionSentences.length > 0)
            {
                atlasGlossaryCategoryElement.setShortDescription(descriptionSentences[0]);
            }
        }
        atlasGlossaryCategoryElement.setLongDescription(egeriaGlossaryCategory.getGlossaryCategoryProperties().getDescription());

        Map<String, Object> additionalAttributes = new HashMap<>();

        additionalAttributes.put(egeriaGUIDPropertyName, egeriaGlossaryCategory.getElementHeader().getGUID());
        additionalAttributes.put(egeriaOwnedPropertyName, true);

        atlasGlossaryCategoryElement.setAdditionalAttributes(additionalAttributes);

        return atlasGlossaryCategoryElement;
    }


    /**
     * Calculate the term name to use in Atlas for an open metadata term.  It is a 1-1 mapping unless the Egeria glossary has
     * multiple terms with the same name.  In that case, a bracketed number is added to the end of the term name.
     *
     * @param egeriaTermName display name from Egeria
     * @param atlasTermGUID equivalent term GUID in Atlas (if known)
     * @param atlasGlossaryTermElementMap map of known terms in the destination atlas glossary
     * @return name to use
     */
    private String getNameForAtlasTerm(String                                 egeriaTermName,
                                       String                                 atlasTermGUID,
                                       Map<String, AtlasGlossaryTermElement>  atlasGlossaryTermElementMap)
    {
        /*
         * There are no existing terms so the name from Egeria can be used.
         */
        if (atlasGlossaryTermElementMap == null)
        {
            return egeriaTermName;
        }

        if ((atlasTermGUID != null) && (atlasGlossaryTermElementMap.get(atlasTermGUID) != null))
        {
            /*
             * The term name has already been established in Apache Atlas.  Is it still consistent with Egeria?
             * Only return the existing name if it is consistent.
             */
            String atlasTermName = atlasGlossaryTermElementMap.get(atlasTermGUID).getName();
            if (atlasTermName.startsWith(egeriaTermName))
            {
                return atlasTermName;
            }
        }
        int termCount = 0;

        /*
         * Step through all the existing terms and determine if the term name from Egeria would be unique in Atlas.
         */
        for (AtlasGlossaryTermElement existingAtlasTerm : atlasGlossaryTermElementMap.values())
        {
            if (! egeriaTermName.equals(existingAtlasTerm.getName()))
            {
                /*
                 * There is no direct name match.  Look to see if it is a modified name.
                 */
                String[] termNameParts = egeriaTermName.split(Pattern.quote(" ("));
                if (termNameParts.length > 1)
                {
                    /*
                     * There is a bracket in the name.  This could be a modified name to handle multiple glossary terms within the glossary.
                     */
                    String[] termEndingParts = termNameParts[termNameParts.length - 1].split(Pattern.quote(")"));

                    if (termEndingParts.length == 1)
                    {
                        try
                        {
                            int termIndex = Integer.parseInt(termEndingParts[0]);

                            if (termIndex >= termCount)
                            {
                                termCount = termIndex + 1;
                            }
                        }
                        catch (NumberFormatException notANumber)
                        {
                            // ignore the term
                        }
                    }
                }
            }
            else
            {
                termCount = termCount + 1;
            }
        }

        if (termCount == 0)
        {
            return egeriaTermName;
        }
        else
        {
            return egeriaTermName + " (" + termCount + ")";
        }
    }


    /**
     * Calculate the name to use in Atlas for an open metadata category.  It is a 1-1 mapping unless the Egeria glossary has
     * multiple categories with the same name.  In that case, a bracketed number is added to the end of the category name.
     *
     * @param egeriaCategoryName display name from Egeria
     * @param atlasCategoryGUID equivalent term GUID in Atlas (if known)
     * @param atlasGlossaryCategoryElementMap map of known categories in the destination atlas glossary
     * @return name to use
     */
    private String getNameForAtlasCategory(String                                     egeriaCategoryName,
                                           String                                     atlasCategoryGUID,
                                           Map<String, AtlasGlossaryCategoryElement>  atlasGlossaryCategoryElementMap)
    {
        /*
         * There are no existing terms so the name from Egeria can be used.
         */
        if (atlasGlossaryCategoryElementMap == null)
        {
            return egeriaCategoryName;
        }

        if ((atlasCategoryGUID != null) && (atlasGlossaryCategoryElementMap.get(atlasCategoryGUID) != null))
        {
            /*
             * The category name has already been established in Apache Atlas.  Is it still consistent with Egeria?
             * Only return the existing name if it is consistent.
             */
            String atlasCategoryName = atlasGlossaryCategoryElementMap.get(atlasCategoryGUID).getName();
            if (atlasCategoryName.startsWith(egeriaCategoryName))
            {
                return atlasCategoryName;
            }
        }

        int categoryCount = 0;

        /*
         * Step through all the existing categories and determine if the category name is unique.
         */
        for (AtlasGlossaryCategoryElement existingAtlasCategory : atlasGlossaryCategoryElementMap.values())
        {
            if (! egeriaCategoryName.equals(existingAtlasCategory.getName()))
            {
                /*
                 * There is no direct name match.  Look to see if it is a modified name.
                 */
                String[] categoryNameParts = egeriaCategoryName.split(Pattern.quote(" ("));
                if (categoryNameParts.length > 1)
                {
                    /*
                     * There is a bracket in the name.  This could be a modified name to handle multiple glossary categories within the glossary.
                     */
                    String[] categoryEndingParts = categoryNameParts[categoryNameParts.length - 1].split(Pattern.quote(")"));

                    if (categoryEndingParts.length == 1)
                    {
                        try
                        {
                            int categoryIndex = Integer.parseInt(categoryEndingParts[0]);

                            if (categoryIndex >= categoryCount)
                            {
                                categoryCount = categoryIndex + 1;
                            }
                        }
                        catch (NumberFormatException notANumber)
                        {
                            // ignore the term
                        }
                    }
                }
            }
            else
            {
                categoryCount = categoryCount + 1;
            }
        }

        if (categoryCount == 0)
        {
            return egeriaCategoryName;
        }
        else
        {
            return egeriaCategoryName + " (" + categoryCount + ")";
        }
    }


    /**
     * Load the terms from an atlas glossary into a map for processing.
     *
     * @param atlasGlossary contents of the Apache Atlas glossary
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    private Map<String, AtlasGlossaryTermElement> getAtlasTermsForGlossary(AtlasGlossaryElement atlasGlossary) throws PropertyServerException
    {
        if (atlasGlossary != null)
        {
            if ((atlasGlossary.getTerms() != null) && (! atlasGlossary.getTerms().isEmpty()))
            {
                Map<String, AtlasGlossaryTermElement> atlasTerms = new HashMap<>();

                /*
                 * Load the terms currently stored in Apache Atlas
                 */
                for (AtlasRelatedTermHeader termHeader : atlasGlossary.getTerms())
                {
                    AtlasGlossaryTermElement atlasGlossaryTermElement = atlasClient.getAtlasGlossaryTerm(termHeader.getTermGuid());

                    if ((atlasGlossaryTermElement != null) && (atlasGlossaryTermElement.getGuid() != null))
                    {
                        atlasTerms.put(atlasGlossaryTermElement.getGuid(), atlasGlossaryTermElement);
                    }
                }

                return atlasTerms;
            }
        }

        return null;
    }


    /**
     * Load the categories from an atlas glossary into a map for processing.
     *
     * @param atlasGlossary contents of the Apache Atlas glossary
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    private Map<String, AtlasGlossaryCategoryElement> getAtlasCategoriesForGlossary(AtlasGlossaryElement atlasGlossary) throws PropertyServerException
    {
        if (atlasGlossary != null)
        {
            return getAtlasCategoriesForElement(atlasGlossary.getCategories());
        }

        return null;
    }


    /**
     * Load the categories from an atlas glossary into a map for processing.
     *
     * @param relatedCategories contents of the Apache Atlas glossary
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    private Map<String, AtlasGlossaryCategoryElement> getAtlasCategoriesForElement(List<AtlasRelatedCategoryHeader> relatedCategories) throws PropertyServerException
    {
        if ((relatedCategories != null) && (! relatedCategories.isEmpty()))
        {
            Map<String, AtlasGlossaryCategoryElement> atlasCategories = new HashMap<>();

            /*
             * Load the categories currently stored in Apache Atlas
             */
            for (AtlasRelatedCategoryHeader categoryHeader : relatedCategories)
            {
                AtlasGlossaryCategoryElement atlasGlossaryCategoryElement = atlasClient.getAtlasGlossaryCategory(categoryHeader.getCategoryGuid());

                if ((atlasGlossaryCategoryElement != null) && (atlasGlossaryCategoryElement.getGuid() != null))
                {
                    atlasCategories.put(atlasGlossaryCategoryElement.getGuid(), atlasGlossaryCategoryElement);
                }
            }

            return atlasCategories;
        }

        return null;
    }
}
