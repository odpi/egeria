/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ExternalIdClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.GlossaryTermClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ApacheAtlasIntegrationProvider;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


/**
 * AtlasGlossaryIntegrationModule exchanges glossary terms between Apache Atlas and the open metadata ecosystem.
 */
public class AtlasGlossaryIntegrationModule extends AtlasRegisteredIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private final static String moduleName                            = "Glossary Integration Module";
    private final static String atlasGlossaryTypeName                 = "AtlasGlossary";
    private final static String atlasGlossaryCategoriesPropertyName   = "categories";
    private final static String atlasGlossaryCategoryTypeName         = "AtlasGlossaryCategory";
    private final static String atlasParentCategoryPropertyName       = "parentCategory";
    private final static String atlasGlossaryTermsPropertyName        = "terms";
    private final static String atlasGlossaryTermTypeName             = "AtlasGlossaryTerm";
    private final static String atlasSemanticAssignmentTypeName       = "AtlasGlossarySemanticAssignment";
    private final static String atlasTermCategorizationTypeName       = "AtlasGlossaryTermCategorization";
    private final static String atlasCategoryHierarchyTypeName        = "AtlasGlossaryCategoryHierarchyLink";
    private final static String atlasGlossaryAnchorPropertyName       = "anchor";
    private final static String atlasShortDescriptionPropertyName     = "shortDescription";
    private final static String atlasLongDescriptionPropertyName      = "longDescription";
    private final static String atlasAdditionalAttributesPropertyName = "additionalAttributes";
    private final static String atlasLanguagePropertyName             = "language";
    private final static String atlasUsagePropertyName                = "usage";
    private final static String atlasExamplesPropertyName             = "examples";
    private final static String atlasAbbreviationPropertyName         = "abbreviation";

    private final static String egeriaGlossaryTypeName         = "Glossary";
    private final static String egeriaGlossaryCategoryTypeName = "GlossaryCategory";
    private final static String egeriaGlossaryTermTypeName     = "GlossaryTerm";

    private String fixedEgeriaGlossaryQualifiedName = null;
    private String fixedEgeriaGlossaryGUID          = null;
    private String atlasGlossaryName                = null;

    private final CollectionClient   glossaryClient;
    private final GlossaryTermClient glossaryTermClient;

    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionDetails connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public AtlasGlossaryIntegrationModule(String                   connectorName,
                                          Connection               connectionDetails,
                                          AuditLog                 auditLog,
                                          IntegrationContext       myContext,
                                          String                   targetRootURL,
                                          ApacheAtlasRESTConnector atlasClient,
                                          List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              moduleName,
              connectionDetails,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors,
              new String[]{"Glossary", "GlossaryCategory", "GlossaryTerm"},
              new String[]{"Glossary", "GlossaryCategory", "GlossaryTerm"});

        final String methodName = "AtlasGlossaryIntegrationModule()";

        Map<String, Object> configurationProperties = connectionDetails.getConfigurationProperties();

        if (configurationProperties != null)
        {
            if (configurationProperties.get(ApacheAtlasIntegrationProvider.EGERIA_GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY) != null)
            {
                fixedEgeriaGlossaryQualifiedName = configurationProperties.get(ApacheAtlasIntegrationProvider.EGERIA_GLOSSARY_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
            }

            if (configurationProperties.get(ApacheAtlasIntegrationProvider.ATLAS_GLOSSARY_NAME_CONFIGURATION_PROPERTY) != null)
            {
                atlasGlossaryName = configurationProperties.get(ApacheAtlasIntegrationProvider.ATLAS_GLOSSARY_NAME_CONFIGURATION_PROPERTY).toString();
            }
        }

        /*
         * Record the configuration
         */
        if (auditLog != null)
        {
            if (fixedEgeriaGlossaryQualifiedName == null)
            {
                auditLog.logMessage(methodName, AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_ALL_EGERIA_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL));
            }
            else
            {
                auditLog.logMessage(methodName, AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_SPECIFIC_EGERIA_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL,
                                                                                                                                                  fixedEgeriaGlossaryQualifiedName));
            }

            if (atlasGlossaryName == null)
            {
                auditLog.logMessage(methodName, AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_ALL_ATLAS_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL));
            }
            else
            {
                auditLog.logMessage(methodName, AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_SPECIFIC_ATLAS_GLOSSARIES.getMessageDefinition(connectorName, targetRootURL, atlasGlossaryName));
            }
        }

        /*
         * The glossaryExchangeService provides access to the open metadata API.
         */
        glossaryClient = myContext.getCollectionClient(OpenMetadataType.GLOSSARY.typeName);
        glossaryTermClient = myContext.getGlossaryTermClient();

        /*
         * Deduplication is turned off so that the connector works with the entities it created rather than
         * entities from other systems that have been linked as duplicates.
         */
        glossaryClient.setForDuplicateProcessing(true);
        glossaryTermClient.setForDuplicateProcessing(true);
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
                    List<OpenMetadataRootElement> glossaries = glossaryClient.findCollections(null, null);

                    if (glossaries != null)
                    {
                        for (OpenMetadataRootElement glossary : glossaries)
                        {
                            this.processGlossaryFromEgeria(glossary);
                        }
                    }
                }
                else
                {
                    /*
                     * Only exchange the specific named glossary
                     */
                    OpenMetadataRootElement glossary = this.getGlossaryByQualifiedName(fixedEgeriaGlossaryQualifiedName, methodName);

                    if (glossary != null)
                    {
                        this.processGlossaryFromEgeria(glossary);
                    }
                    else
                    {
                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                AtlasIntegrationAuditCode.UNABLE_TO_RETRIEVE_EGERIA_GLOSSARY.getMessageDefinition(connectorName,
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
                boolean requestedAtlasGlossaryMissing = (atlasGlossaryName != null);

                int startFrom = 0;
                int pageSize  = myContext.getMaxPageSize();

                List<AtlasEntityHeader> atlasSearchResult = atlasClient.getEntitiesForType(atlasGlossaryTypeName, startFrom, pageSize);

                while ((atlasSearchResult != null) && (! atlasSearchResult.isEmpty()))
                {
                    /*
                     * Found new databases - process each one in turn.
                     */
                    for (AtlasEntityHeader atlasEntityHeader : atlasSearchResult)
                    {
                        String atlasGlossaryGUID = atlasEntityHeader.getGuid();

                        AtlasEntityWithExtInfo atlasGlossaryEntity = atlasClient.getEntityByGUID(atlasGlossaryGUID);

                        if (atlasGlossaryName == null)
                        {
                            /*
                             * The connector is configured to synchronize all Atlas glossaries.
                             */
                            this.processGlossaryFromAtlas(atlasGlossaryEntity);
                        }
                        else if (atlasGlossaryName.equals(this.getAtlasStringProperty(atlasGlossaryEntity.getEntity().getAttributes(), atlasNamePropertyName)))
                        {
                            /*
                             * The specifically requested glossary has been found.
                             */
                            this.processGlossaryFromAtlas(atlasGlossaryEntity);
                            requestedAtlasGlossaryMissing = false;
                        }

                        /*
                         * Retrieve the next page
                         */
                        startFrom = startFrom + pageSize;
                        atlasSearchResult = atlasClient.getEntitiesForType(atlasGlossaryTypeName, startFrom, pageSize);
                    }
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
                                            AtlasIntegrationAuditCode.UNABLE_TO_RETRIEVE_ATLAS_GLOSSARY.getMessageDefinition(connectorName,
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
                                      AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.  This connector is only interested in listening for
     * glossaries, glossary categories and glossary terms.   The listener that calls this method is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    @Override
    public void processEvent(OpenMetadataOutTopicEvent event)
    {
        if (myContext.noRefreshInProgress())
        {
            try
            {
                ElementHeader elementHeader = event.getElementHeader();

                if ((elementHeader != null) && (! isAtlasOwnedElement(elementHeader)))
                {
                    /*
                     * Understand the type of the element that has changed to determine if it is of interest.
                     */
                    if (myContext.isTypeOf(elementHeader, OpenMetadataType.GLOSSARY.typeName))
                    {
                        if (validGlossaryElement(elementHeader))
                        {
                            processGlossaryFromEgeria(glossaryClient.getCollectionByGUID(elementHeader.getGUID(), glossaryClient.getGetOptions()));
                        }
                    }
                    else if (myContext.isTypeOf(elementHeader, OpenMetadataType.GLOSSARY_TERM.typeName))
                    {
                        OpenMetadataRootElement egeriaGlossaryTerm = glossaryTermClient.getGlossaryTermByGUID(elementHeader.getGUID(), null);

                        if (egeriaGlossaryTerm != null)
                        {
                            OpenMetadataRootElement egeriaGlossary = glossaryClient.getGlossaryForTerm(egeriaGlossaryTerm);

                            if ((egeriaGlossary != null) && (validGlossaryElement(egeriaGlossary.getElementHeader())))
                            {
                                AtlasEntityWithExtInfo atlasGlossary = this.getAtlasDestinationGlossaryForEgeriaEntity(egeriaGlossary);

                                processGlossaryTermFromEgeria(egeriaGlossaryTerm, egeriaGlossary, atlasGlossary);
                            }
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
                                          AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
                OpenMetadataRootElement glossaryElement = this.getGlossaryByQualifiedName(fixedEgeriaGlossaryQualifiedName, methodName);

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
     * Return the unique identifier for the equivalent element in Apache Atlas.
     *
     * @param metadataElement retrieved metadata element
     * @return string guid
     */
    private String retryGetAtlasGUID(OpenMetadataRootElement metadataElement)
    {
        try
        {
            OpenMetadataRootElement refreshedTerm = glossaryTermClient.getGlossaryTermByGUID(metadataElement.getElementHeader().getGUID(),
                                                                                         null);

            return this.getAtlasGUID(refreshedTerm);
        }
        catch (Exception notFound)
        {
            // try again later.
        }

        return null;
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
    private void processGlossaryFromEgeria(OpenMetadataRootElement egeriaGlossary) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String methodName = "processGlossaryFromEgeria";

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
                    AtlasEntityWithExtInfo atlasGlossary = atlasClient.getEntityByGUID(atlasGlossaryGUID);

                    updateAtlasGlossaryInEgeria(atlasGlossary, egeriaGlossary.getElementHeader().getGUID());

                    /*
                     * Once the root element is synchronized, the categories and then the terms are synchronized.
                     * It is possible that these elements have different ownership to the root glossary element.
                     */
                    // todo processGlossaryCategoriesFromEgeria(egeriaGlossary, atlasGlossary);
                    processGlossaryTermsFromEgeria(egeriaGlossary, atlasGlossary);
                }
                else
                {
                    /*
                     * Something has caused the external identifier for this entity to disappear
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.EGERIA_GUID_MISSING.getMessageDefinition("Glossary",
                                                                                                           egeriaGlossary.getElementHeader().getGUID()));
                }
            }
            else
            {
                /*
                 * This element originated in the open metadata ecosystem, so it is possible to copy it directly to Apache Atlas.
                 */
                AtlasEntityWithExtInfo atlasGlossary = syncEgeriaGlossaryInAtlas(egeriaGlossary);

                /*
                 * Once the root element is synchronized, the categories and then the terms are synchronized.
                 * It is possible that these elements have different ownership to the root glossary element.
                 */
                // todo processGlossaryCategoriesFromEgeria(egeriaGlossary, atlasGlossary);
                processGlossaryTermsFromEgeria(egeriaGlossary, atlasGlossary);
            }
        }
    }


    /**
     * The connector is processing a glossary from the open metadata ecosystem.  It is ready to step through each term in the glossary.
     * The origin of each term is unknown.
     *
     * @param egeriaGlossary open metadata glossary
     * @param atlasGlossary equivalent atlas glossary
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void processGlossaryTermsFromEgeria(OpenMetadataRootElement egeriaGlossary,
                                                AtlasEntityWithExtInfo  atlasGlossary) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        if (egeriaGlossary != null)
        {
            /*
             * Step through the terms in the Egeria Glossary and make sure all term properties are synchronized.
             */
            int startFrom = 0;
            List<OpenMetadataRootElement> egeriaGlossaryMembers = glossaryClient.getCollectionMembers(egeriaGlossary.getElementHeader().getGUID(),
                                                                                                   glossaryTermClient.getQueryOptions(startFrom, myContext.getMaxPageSize()));

            while (egeriaGlossaryMembers != null)
            {
                for (OpenMetadataRootElement egeriaGlossaryMember : egeriaGlossaryMembers)
                {
                    if (egeriaGlossaryMember != null)
                    {
                        if (propertyHelper.isTypeOf(egeriaGlossaryMember.getElementHeader(), OpenMetadataType.GLOSSARY_TERM.typeName))
                        {
                            this.processGlossaryTermFromEgeria(egeriaGlossaryMember, egeriaGlossary, atlasGlossary);
                        }
                    }
                }

                startFrom = startFrom + myContext.getMaxPageSize();
                egeriaGlossaryMembers = glossaryClient.getCollectionMembers(egeriaGlossary.getElementHeader().getGUID(), glossaryTermClient.getQueryOptions(startFrom, myContext.getMaxPageSize()));
            }
        }
    }


    /**
     * Copy the content of open metadata ecosystem glossary term to Apache Atlas.
     *
     * @param egeriaGlossaryTerm open metadata glossaryTermElement
     * @param egeriaGlossary open metadata glossary
     * @param atlasGlossary equivalent atlas glossary
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void processGlossaryTermFromEgeria(OpenMetadataRootElement egeriaGlossaryTerm,
                                               OpenMetadataRootElement egeriaGlossary,
                                               AtlasEntityWithExtInfo  atlasGlossary) throws PropertyServerException,
                                                                                             InvalidParameterException,
                                                                                             UserNotAuthorizedException
    {
        if (egeriaGlossaryTerm != null)
        {
            final String methodName = "processGlossaryTermFromEgeria";

            if (isAtlasOwnedElement(egeriaGlossaryTerm.getElementHeader()))
            {
                /*
                 * This element originated in Apache Atlas.  The element from the open metadata ecosystem is just a copy.  Therefore,
                 * we refresh its content from the Apache Atlas original.
                 */
                String atlasGlossaryTermGUID = getAtlasGUID(egeriaGlossaryTerm);
                if (atlasGlossaryTermGUID != null)
                {
                    updateAtlasGlossaryTermInEgeria(atlasClient.getEntityByGUID(atlasGlossaryTermGUID), egeriaGlossaryTerm);
                }
                else if (auditLog != null)
                {
                    /*
                     * Something has caused the external identifier for this entity to disappear
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.EGERIA_GUID_MISSING.getMessageDefinition(egeriaGlossaryTermTypeName,
                                                                                                           egeriaGlossaryTerm.getElementHeader().getGUID()));
                }
            }
            else
            {
                /*
                 * This element originated in the open metadata ecosystem, so it is possible to copy it directly to Apache Atlas.
                 */
                this.syncEgeriaGlossaryTermInAtlas(egeriaGlossaryTerm, atlasGlossary);
            }
        }
    }


    /**
     * Load glossary that originated in Apache Atlas into the open metadata ecosystem.
     *
     * @param atlasGlossaryElement glossary retrieved from Apache Atlas
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    private void processGlossaryFromAtlas(AtlasEntityWithExtInfo atlasGlossaryElement) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "processGlossaryFromAtlas";

        String egeriaGlossaryGUID = this.getEgeriaGUID(atlasGlossaryElement);

        if (egeriaGlossaryGUID == null)
        {
            /*
             * Assume the glossary is Atlas-owned because it has not been synchronized with Egeria.
             */
            egeriaGlossaryGUID = this.createAtlasGlossaryInEgeria(atlasGlossaryElement);

            OpenMetadataRootElement egeriaGlossary = glossaryClient.getCollectionByGUID(egeriaGlossaryGUID, null);


            /*
             * The ownership of the categories and terms associated with the glossary is not known.
             */
            // todo this.processGlossaryCategoriesFromAtlas(atlasGlossaryElement, egeriaGlossary);
            this.processGlossaryTermsFromAtlas(atlasGlossaryElement, egeriaGlossary);
        }
        else if (this.isEgeriaOwned(atlasGlossaryElement))
        {
            OpenMetadataRootElement egeriaGlossary = null;

            try
            {
                egeriaGlossary = glossaryClient.getCollectionByGUID(egeriaGlossaryGUID, null);
            }
            catch (InvalidParameterException missingGlossary)
            {
                /*
                 * The Egeria glossary has been unilaterally deleted - so remove the atlas equivalent.
                 */
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.EGERIA_GLOSSARY_DELETED.getMessageDefinition(egeriaGlossaryGUID,
                                                                                                               this.getAtlasStringProperty(atlasGlossaryElement.getEntity().getAttributes(),
                                                                                                                                      atlasNamePropertyName)));
                }
            }

            if (egeriaGlossary == null)
            {
                atlasClient.deleteEntity(atlasGlossaryElement.getEntity().getGuid());
            }
            else
            {
                AtlasEntityWithExtInfo atlasGlossary = this.syncEgeriaGlossaryInAtlas(egeriaGlossary);
                // todo this.processGlossaryCategoriesFromEgeria(egeriaGlossary, atlasGlossary);
                this.processGlossaryTermsFromEgeria(egeriaGlossary, atlasGlossary);
            }
        }
        else // Atlas Owned
        {
            OpenMetadataRootElement egeriaGlossary = null;

            try
            {
                egeriaGlossary = glossaryClient.getCollectionByGUID(egeriaGlossaryGUID, null);
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
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_GLOSSARY.getMessageDefinition(egeriaGlossaryGUID,
                                                                                                                 this.getAtlasStringProperty(atlasGlossaryElement.getEntity().getAttributes(), atlasNamePropertyName),
                                                                                                                 connectorName));
                }

                this.removeEgeriaGUID(atlasGlossaryElement);
            }

            egeriaGlossary = this.syncAtlasGlossaryInEgeria(atlasGlossaryElement);

            if (egeriaGlossary != null)
            {
                this.processGlossaryTermsFromAtlas(atlasGlossaryElement, egeriaGlossary);
            }
        }
    }


    /**
     * Process each glossary term attached to an Atlas glossary.
     *
     * @param atlasGlossaryElement glossary from Atlas which includes a list of terms for the glossary
     * @param egeriaGlossary equivalent glossary in the open metadata ecosystem
     * @throws PropertyServerException problem communicating with Egeria or Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     */
    private void processGlossaryTermsFromAtlas(AtlasEntityWithExtInfo  atlasGlossaryElement,
                                               OpenMetadataRootElement egeriaGlossary) throws PropertyServerException,
                                                                                              InvalidParameterException,
                                                                                              UserNotAuthorizedException
    {
        if ((atlasGlossaryElement != null) &&
                    (atlasGlossaryElement.getEntity() != null) &&
                    (atlasGlossaryElement.getEntity().getRelationshipAttributes() != null))
        {
            List<AtlasEntityWithExtInfo> terms = atlasClient.getRelatedEntities(atlasGlossaryElement, atlasGlossaryTermsPropertyName);
            if (terms != null)
            {
                for (AtlasEntityWithExtInfo relatedAtlasTerm : terms)
                {
                    processGlossaryTermFromAtlas(relatedAtlasTerm, atlasGlossaryElement, egeriaGlossary);
                }
            }
        }
    }


    /**
     * Process an individual term from an atlas glossary - at this point we do not know if it is owned by Atlas or Egeria.
     *
     * @param atlasGlossaryTerm term to process
     * @param atlasGlossary glossary where it came from
     * @param egeriaGlossary equivalent glossary in the open metadata ecosystem
     * @throws PropertyServerException problem communicating with Egeria or Atlas
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     */
    private void processGlossaryTermFromAtlas(AtlasEntityWithExtInfo  atlasGlossaryTerm,
                                              AtlasEntityWithExtInfo  atlasGlossary,
                                              OpenMetadataRootElement egeriaGlossary) throws InvalidParameterException,
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
            this.createAtlasGlossaryTermInEgeria(atlasGlossaryTerm, egeriaGlossary);
        }
        else if (this.isEgeriaOwned(atlasGlossaryTerm))
        {
            OpenMetadataRootElement egeriaGlossaryTerm = null;

            try
            {
                egeriaGlossaryTerm = glossaryTermClient.getGlossaryTermByGUID(egeriaTermGUID, null);
            }
            catch (InvalidParameterException missingTerm)
            {
                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.EGERIA_GLOSSARY_TERM_DELETED.getMessageDefinition(egeriaTermGUID,
                                                                                                                getAtlasStringProperty(atlasGlossaryTerm.getEntity().getAttributes(),
                                                                                                                                  atlasQualifiedNamePropertyName)));
            }

            if (egeriaGlossaryTerm == null)
            {
                atlasClient.deleteEntity(atlasGlossaryTerm.getEntity().getGuid());
            }
            else
            {
                this.syncEgeriaGlossaryTermInAtlas(egeriaGlossaryTerm, atlasGlossary);
            }
        }
        else // Atlas Owned
        {
            OpenMetadataRootElement egeriaGlossaryTerm = null;

            try
            {
                egeriaGlossaryTerm = glossaryTermClient.getGlossaryTermByGUID(egeriaTermGUID, null);
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
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_GLOSSARY_TERM.getMessageDefinition(egeriaTermGUID,
                                                                                                                      this.getAtlasStringProperty(atlasGlossaryTerm.getEntity().getAttributes(), atlasNamePropertyName),
                                                                                                                      connectorName));
                }

                this.removeEgeriaGUID(atlasGlossaryTerm);
            }

            if (egeriaGlossaryTerm == null)
            {
                this.createAtlasGlossaryTermInEgeria(atlasGlossaryTerm, egeriaGlossary);
            }
            else
            {
                this.updateAtlasGlossaryTermInEgeria(atlasGlossaryTerm, egeriaGlossaryTerm);
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
     * @param egeriaGlossary open metadata glossary
     * @return equivalent atlas glossary
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing Egeria or Apache Atlas
     */
    private AtlasEntityWithExtInfo syncEgeriaGlossaryInAtlas(OpenMetadataRootElement egeriaGlossary) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        if (egeriaGlossary != null)
        {
            String atlasGlossaryGUID = getAtlasGUID(egeriaGlossary);

            AtlasEntityWithExtInfo atlasGlossary;

            if (atlasGlossaryGUID == null)
            {
                atlasGlossary = createEgeriaGlossaryInAtlas(egeriaGlossary);
            }
            else
            {
                atlasGlossary = atlasClient.getEntityByGUID(atlasGlossaryGUID);

                if (atlasGlossary != null)
                {
                    atlasGlossary = updateEgeriaGlossaryInAtlas(egeriaGlossary, atlasGlossary);
                }
                else
                {
                    atlasGlossary = createEgeriaGlossaryInAtlas(egeriaGlossary);
                }
            }

            return atlasGlossary;
        }

        return null;
    }


    /**
     * Create a copy of an Egeria Glossary in Apache Atlas.
     *
     * @param egeriaGlossary glossary to copy
     * @return Atlas entity that has been created
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing Egeria or Apache Atlas
     */
    AtlasEntityWithExtInfo createEgeriaGlossaryInAtlas(OpenMetadataRootElement egeriaGlossary) throws PropertyServerException,
                                                                                              InvalidParameterException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "createEgeriaGlossaryInAtlas";

        AtlasEntityWithExtInfo atlasGlossary = this.prepareGlossaryMemberEntity(null,
                                                                                atlasGlossaryTypeName,
                                                                                this.getAtlasGlossaryProperties(egeriaGlossary),
                                                                                null);

        String atlasGlossaryGUID = atlasClient.addEntity(atlasGlossary.getEntity());

        auditLog.logMessage(methodName,
                            AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                  egeriaGlossaryTypeName,
                                                                                                  egeriaGlossary.getElementHeader().getGUID(),
                                                                                                  atlasGlossary.getEntity().getTypeName(),
                                                                                                  atlasGlossaryGUID));

        if (egeriaGlossary.getProperties() instanceof GlossaryProperties glossaryProperties)
        {
            saveEgeriaGUIDInAtlas(atlasGlossaryGUID,
                                  egeriaGlossary.getElementHeader().getGUID(),
                                  glossaryProperties.getQualifiedName(),
                                  egeriaGlossaryTypeName,
                                  true,
                                  true);

            /*
             * Retrieve the new glossary with filled out GUID and correlation.
             */
            atlasGlossary = atlasClient.getEntityByGUID(atlasGlossaryGUID);

            /*
             * Set up external Identifier
             */
            ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGlossaryGUID,
                                                                                           atlasGlossaryTypeName,
                                                                                           atlasGlossary.getEntity().getCreatedBy(),
                                                                                           atlasGlossary.getEntity().getCreateTime(),
                                                                                           atlasGlossary.getEntity().getUpdatedBy(),
                                                                                           atlasGlossary.getEntity().getUpdateTime(),
                                                                                           atlasGlossary.getEntity().getVersion());

            ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(super.getAtlasStringProperty(atlasGlossary.getEntity().getAttributes(), atlasNamePropertyName),
                                                                                       glossaryProperties.getDisplayName(),
                                                                                       PermittedSynchronization.TO_THIRD_PARTY);

            externalIdClient.createExternalId(egeriaGlossary.getElementHeader().getGUID(),
                                              externalIdLinkProperties,
                                              externalIdentifierProperties);



            return atlasGlossary;
        }

        return null;
    }


    /**
     * Copy the latest content of open metadata ecosystem glossary to Apache Atlas.
     *
     * @param egeriaGlossaryElement open metadata glossary
     * @param atlasGlossary glossary retrieved from Apache Atlas
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private AtlasEntityWithExtInfo updateEgeriaGlossaryInAtlas(OpenMetadataRootElement egeriaGlossaryElement,
                                                               AtlasEntityWithExtInfo  atlasGlossary) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "updateEgeriaGlossaryInAtlas";

        if (egeriaGlossaryElement != null)
        {
            if (atlasUpdateRequired(egeriaGlossaryElement,
                                    atlasGlossary.getEntity()))
            {
                /*
                 * Update the glossary properties in case they have changed.
                 */
                AtlasEntityWithExtInfo latestAtlasGlossary = this.prepareGlossaryMemberEntity(atlasGlossary.getEntity().getGuid(),
                                                                                              atlasGlossaryTypeName,
                                                                                              this.getAtlasGlossaryProperties(egeriaGlossaryElement),
                                                                                              null);

                latestAtlasGlossary = atlasClient.updateEntity(latestAtlasGlossary);



                this.updateExternalIdentifierAfterAtlasUpdate(egeriaGlossaryElement,
                                                              latestAtlasGlossary.getEntity());

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.UPDATING_ATLAS_ENTITY.getMessageDefinition(connectorName,
                                                                                                         atlasGlossaryTypeName,
                                                                                                         egeriaGlossaryElement.getElementHeader().getGUID(),
                                                                                                         atlasGlossary.getEntity().getGuid()));

                return latestAtlasGlossary;
            }
            else
            {
                /* todo
                myContext.confirmSynchronization(egeriaGlossaryElement.getElementHeader().getGUID(),
                                                 egeriaGlossaryTypeName,
                                                 atlasGlossary.getEntity().getGuid());

                 */
            }
        }


        return atlasGlossary;
    }


    /**
     * Copy the content of open metadata ecosystem glossary term to Apache Atlas.
     * This is called when a change is made to a glossary term in the open metadata ecosystem.
     * There are lots of strange timing windows with events and so any anomaly found in the open metadata system
     * results in the term being ignored.
     *
     * @param egeriaGlossaryTerm open metadata glossary term
     * @param atlasGlossaryDestination the Atlas glossary where the term is to be copied to
     * @return equivalent atlas glossary term
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem communicating either with the open metadata access server or Apache Atlas
     */
    private AtlasEntityWithExtInfo syncEgeriaGlossaryTermInAtlas(OpenMetadataRootElement    egeriaGlossaryTerm,
                                                                 AtlasEntityWithExtInfo atlasGlossaryDestination) throws PropertyServerException,
                                                                                                                         InvalidParameterException,
                                                                                                                         UserNotAuthorizedException
    {
        if (egeriaGlossaryTerm != null)
        {
            String atlasGlossaryTermGUID = getAtlasGUID(egeriaGlossaryTerm);

            AtlasEntityWithExtInfo atlasGlossaryTerm;

            if (atlasGlossaryTermGUID == null)
            {
                atlasGlossaryTerm = createEgeriaGlossaryTermInAtlas(egeriaGlossaryTerm, atlasGlossaryDestination);
            }
            else
            {
                atlasGlossaryTerm = atlasClient.getEntityByGUID(atlasGlossaryTermGUID);

                if (atlasGlossaryTerm != null)
                {
                    atlasGlossaryTerm = updateEgeriaGlossaryTermInAtlas(egeriaGlossaryTerm, atlasGlossaryTerm, atlasGlossaryDestination);
                }
                else
                {
                    atlasGlossaryTerm = createEgeriaGlossaryTermInAtlas(egeriaGlossaryTerm, atlasGlossaryDestination);
                }
            }

            return atlasGlossaryTerm;
        }

        return null;
    }


    /**
     * Create a copy of an Egeria Glossary Term in Apache Atlas.
     *
     * @param egeriaGlossaryTerm open metadata glossary term
     * @param atlasGlossaryDestination the Atlas glossary where the term is to be copied to
     * @return Atlas entity that has been created
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing Egeria or Apache Atlas
     */
    AtlasEntityWithExtInfo createEgeriaGlossaryTermInAtlas(OpenMetadataRootElement    egeriaGlossaryTerm,
                                                           AtlasEntityWithExtInfo atlasGlossaryDestination) throws PropertyServerException,
                                                                                                                   InvalidParameterException,
                                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "createEgeriaGlossaryTermInAtlas";

        if (egeriaGlossaryTerm.getProperties() instanceof GlossaryTermProperties glossaryTermProperties)
        {
            AtlasEntityWithExtInfo atlasGlossaryTerm = this.prepareGlossaryMemberEntity(null,
                                                                                        atlasGlossaryTermTypeName,
                                                                                        this.getAtlasGlossaryTermProperties(egeriaGlossaryTerm,
                                                                                                                            glossaryTermProperties.getDisplayName(),
                                                                                                                            null,
                                                                                                                            null,
                                                                                                                            atlasGlossaryDestination),
                                                                                        atlasGlossaryDestination);

        String atlasGlossaryTermGUID = atlasClient.addEntity(atlasGlossaryTerm.getEntity());

        auditLog.logMessage(methodName,
                            AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                  egeriaGlossaryTermTypeName,
                                                                                                  egeriaGlossaryTerm.getElementHeader().getGUID(),
                                                                                                  atlasGlossaryTerm.getEntity().getTypeName(),
                                                                                                  atlasGlossaryTermGUID));

        saveEgeriaGUIDInAtlas(atlasGlossaryTermGUID,
                                  egeriaGlossaryTerm.getElementHeader().getGUID(),
                                  glossaryTermProperties.getQualifiedName(),
                                  egeriaGlossaryTermTypeName,
                                  true,
                                  true);

        /*
         * Retrieve the new glossary with filled out GUID and correlation.
         */
        atlasGlossaryTerm = atlasClient.getEntityByGUID(atlasGlossaryTermGUID);

        /*
         * Set up external Identifier
         */
        ExternalIdClient externalIdClient = myContext.getExternalIdClient();

        ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGlossaryTermGUID,
                                                                                       atlasGlossaryTermTypeName,
                                                                                       atlasGlossaryTerm.getEntity().getCreatedBy(),
                                                                                       atlasGlossaryTerm.getEntity().getCreateTime(),
                                                                                       atlasGlossaryTerm.getEntity().getUpdatedBy(),
                                                                                       atlasGlossaryTerm.getEntity().getUpdateTime(),
                                                                                       atlasGlossaryTerm.getEntity().getVersion());

        ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(super.getAtlasStringProperty(atlasGlossaryTerm.getEntity().getAttributes(), atlasNamePropertyName),
                                                                                   glossaryTermProperties.getDisplayName(),
                                                                                   PermittedSynchronization.TO_THIRD_PARTY);

        externalIdClient.createExternalId(egeriaGlossaryTerm.getElementHeader().getGUID(),
                                          externalIdLinkProperties,
                                          externalIdentifierProperties);

        return atlasGlossaryTerm;
    }
    return null;
    }


    /**
     * Copy the latest content of open metadata ecosystem glossary term to Apache Atlas.
     *
     * @param egeriaGlossaryTerm open metadata glossary
     * @param atlasGlossaryTerm glossary retrieved from Apache Atlas
     * @param atlasGlossaryDestination the Atlas glossary where the term is to be copied to
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private AtlasEntityWithExtInfo updateEgeriaGlossaryTermInAtlas(OpenMetadataRootElement    egeriaGlossaryTerm,
                                                                   AtlasEntityWithExtInfo atlasGlossaryTerm,
                                                                   AtlasEntityWithExtInfo atlasGlossaryDestination) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException
    {
        final String methodName = "updateEgeriaGlossaryTermInAtlas";

        if ((egeriaGlossaryTerm != null) &&
                (egeriaGlossaryTerm.getProperties() instanceof GlossaryTermProperties glossaryTermProperties))
        {
            if (atlasUpdateRequired(egeriaGlossaryTerm, atlasGlossaryTerm.getEntity()))
            {
                /*
                 * Update the glossary properties since they have changed.
                 */
                AtlasEntityWithExtInfo latestAtlasGlossaryTerm = this.prepareGlossaryMemberEntity(atlasGlossaryTerm.getEntity().getGuid(),
                                                                                                  atlasGlossaryTermTypeName,
                                                                                                  this.getAtlasGlossaryTermProperties(egeriaGlossaryTerm,
                                                                                                                                      glossaryTermProperties.getDisplayName(),
                                                                                                                                      atlasGlossaryTerm.getEntity().getGuid(),
                                                                                                                                      atlasGlossaryTerm.getEntity().getTypeName(),
                                                                                                                                      atlasGlossaryTerm),
                                                                                                  atlasGlossaryDestination);

                latestAtlasGlossaryTerm = atlasClient.updateEntity(latestAtlasGlossaryTerm);



                this.updateExternalIdentifierAfterAtlasUpdate(egeriaGlossaryTerm,
                                                              latestAtlasGlossaryTerm.getEntity());

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.UPDATING_ATLAS_ENTITY.getMessageDefinition(connectorName,
                                                                                                         atlasGlossaryTermTypeName,
                                                                                                         egeriaGlossaryTerm.getElementHeader().getGUID(),
                                                                                                         atlasGlossaryTerm.getEntity().getGuid()));

                return latestAtlasGlossaryTerm;
            }
            else
            {
                externalIdClient.confirmSynchronization(egeriaGlossaryTerm,
                                                        myContext.getMetadataSourceGUID(),
                                                        atlasGlossaryTerm.getEntity().getGuid());
            }
        }


        return atlasGlossaryTerm;
    }



    /**
     * Copy the contents of the Atlas glossary entity into open metadata.
     *
     * @param atlasGlossaryEntity entity retrieved from Apache Atlas
     *
     * @return equivalent glossary element in open metadata (egeriaDatabaseGUID)
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private OpenMetadataRootElement syncAtlasGlossaryInEgeria(AtlasEntityWithExtInfo  atlasGlossaryEntity) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException
    {
        final String methodName = "syncAtlasGlossaryInEgeria";

        if ((atlasGlossaryEntity != null) && (atlasGlossaryEntity.getEntity() != null))
        {
            String egeriaGlossaryGUID = super.getEgeriaGUID(atlasGlossaryEntity);

            if (egeriaGlossaryGUID == null)
            {
                /*
                 * No record of a previous synchronization with the open metadata ecosystem.
                 */
                egeriaGlossaryGUID = createAtlasGlossaryInEgeria(atlasGlossaryEntity);
            }
            else
            {
                try
                {
                    /*
                     * Does the matching entity in the open metadata ecosystem still exist.  Notice effective time is set to null
                     * to make sure that this entity is found no matter what its effectivity dates are set to.
                     */
                    glossaryClient.getCollectionByGUID(egeriaGlossaryGUID, glossaryClient.getGetOptions());

                    /*
                     * The Egeria equivalent is still in place.
                     */
                    updateAtlasGlossaryInEgeria(atlasGlossaryEntity, egeriaGlossaryGUID);
                }
                catch (InvalidParameterException notKnown)
                {
                    /*
                     * The open metadata ecosystem entity has been deleted - put it back.
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                               egeriaGlossaryTypeName,
                                                                                                               egeriaGlossaryGUID,
                                                                                                               atlasGlossaryEntity.getEntity().getGuid()));
                    removeEgeriaGUID(atlasGlossaryEntity);
                    egeriaGlossaryGUID = createAtlasGlossaryInEgeria(atlasGlossaryEntity);
                }
            }

            if (egeriaGlossaryGUID != null)
            {
                glossaryClient.getCollectionByGUID(egeriaGlossaryGUID, glossaryClient.getGetOptions());
            }
        }

        return null;
    }


    /**
     * Create a new glossary in the open metadata ecosystem that matches a  glossary from Atlas.
     *
     * @param atlasGlossaryEntity glossary from Atlas that is to be copied to Egeria
     * @return  Atlas glossary updated with GUID from Egeria
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws PropertyServerException problem communicating with Egeria
     * @throws UserNotAuthorizedException security problem
     */
    private String createAtlasGlossaryInEgeria(AtlasEntityWithExtInfo atlasGlossaryEntity) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "createAtlasGlossaryInEgeria";

        if (atlasGlossaryEntity != null)
        {
            AtlasEntity atlasEntity = atlasGlossaryEntity.getEntity();

            if (atlasEntity != null)
            {
                ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                               atlasGlossaryTypeName,
                                                                                               atlasEntity.getCreatedBy(),
                                                                                               atlasEntity.getCreateTime(),
                                                                                               atlasEntity.getUpdatedBy(),
                                                                                               atlasEntity.getUpdateTime(),
                                                                                               atlasEntity.getVersion());

                ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(this.getAtlasStringProperty(atlasEntity.getAttributes(), atlasGlossaryName),
                                                                                           null,
                                                                                           PermittedSynchronization.FROM_THIRD_PARTY);

                GlossaryProperties glossaryProperties = this.getEgeriaGlossaryProperties(atlasGlossaryEntity);

                String egeriaGlossaryGUID = glossaryClient.createCollection(new NewElementOptions(glossaryClient.getMetadataSourceOptions()),
                                                                          null,
                                                                            glossaryProperties,
                                                                            null);

                externalIdClient.createExternalId(egeriaGlossaryGUID, externalIdLinkProperties, externalIdentifierProperties);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          egeriaGlossaryTypeName,
                                                                                                          egeriaGlossaryGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));
                /*
                 * Save the egeriaGlossaryGUID from Egeria in the Atlas Glossary's AdditionalAttributes.
                 */
                super.saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                            egeriaGlossaryGUID,
                                            glossaryProperties.getQualifiedName(),
                                            egeriaGlossaryTypeName,
                                            true,
                                            false);

                return egeriaGlossaryGUID;
            }
        }

        return null;
    }


    /**
     * Copy the content of the Apache Atlas glossary to the open metadata ecosystem.
     *
     * @param atlasGlossaryEntity atlas glossary
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security error
     * @throws PropertyServerException problem communicating with Apache Atlas or Egeria
     */
    private void updateAtlasGlossaryInEgeria(AtlasEntityWithExtInfo atlasGlossaryEntity,
                                             String                 egeriaGlossaryGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "updateAtlasGlossaryInEgeria";

        /*
         * The glossary information is successfully retrieved from Apache Atlas.
         */
        if (atlasGlossaryEntity != null)
        {
            AtlasEntity             atlasEntity     = atlasGlossaryEntity.getEntity();
            OpenMetadataRootElement glossaryElement = glossaryClient.getCollectionByGUID(egeriaGlossaryGUID, null);

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(glossaryElement, atlasEntity))
                {
                    GlossaryProperties egeriaGlossaryProperties = this.getEgeriaGlossaryProperties(atlasGlossaryEntity);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              egeriaGlossaryTypeName,
                                                                                                              egeriaGlossaryGUID));

                    glossaryClient.updateCollection(egeriaGlossaryGUID,
                                                    glossaryClient.getUpdateOptions(false),
                                                    egeriaGlossaryProperties);
                }
            }
        }
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
    private void createAtlasGlossaryTermInEgeria(AtlasEntityWithExtInfo  atlasGlossaryTerm,
                                                 OpenMetadataRootElement destinationGlossary) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "createAtlasGlossaryTermInEgeria";

        if (atlasGlossaryTerm != null)
        {
            AtlasEntity atlasEntity = atlasGlossaryTerm.getEntity();

            if (atlasEntity != null)
            {
                ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGlossaryTerm.getEntity().getGuid(),
                                                                                               atlasGlossaryTermTypeName,
                                                                                               atlasEntity.getCreatedBy(),
                                                                                               atlasEntity.getCreateTime(),
                                                                                               atlasEntity.getUpdatedBy(),
                                                                                               atlasEntity.getUpdateTime(),
                                                                                               atlasEntity.getVersion());

                ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(this.getAtlasStringProperty(atlasGlossaryTerm.getEntity().getAttributes(), atlasGlossaryName),
                                                                                           null,
                                                                                           PermittedSynchronization.FROM_THIRD_PARTY);

                GlossaryTermProperties glossaryTermProperties = this.getEgeriaGlossaryTermProperties(atlasGlossaryTerm);


                String egeriaTermGUID = glossaryTermClient.createGlossaryTerm(destinationGlossary.getElementHeader().getGUID(),
                                                                              glossaryTermProperties);

                externalIdClient.createExternalId(egeriaTermGUID,
                                                  externalIdLinkProperties,
                                                  externalIdentifierProperties);

                /*
                 * Todo set up external identifier
                 */

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          egeriaGlossaryTermTypeName,
                                                                                                          egeriaTermGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaTermGUID,
                                      glossaryTermProperties.getQualifiedName(),
                                      egeriaGlossaryTermTypeName,
                                      true,
                                      false);


                // this.setUpTermCategoriesInEgeria(egeriaTermGUID, atlasGlossaryTerm);
                this.setUpTermRelationshipsInEgeria(egeriaTermGUID, atlasGlossaryTerm);
            }
        }
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
    private void updateAtlasGlossaryTermInEgeria(AtlasEntityWithExtInfo  atlasGlossaryTerm,
                                                 OpenMetadataRootElement egeriaGlossaryTerm) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "syncAtlasGlossaryTermInEgeria";

        if (atlasGlossaryTerm != null)
        {
            AtlasEntity atlasEntity = atlasGlossaryTerm.getEntity();

            if (atlasEntity != null)
            {
                String egeriaGlossaryTermGUID = egeriaGlossaryTerm.getElementHeader().getGUID();

                if (this.egeriaUpdateRequired(egeriaGlossaryTerm,
                                              atlasEntity))
                {
                    GlossaryTermProperties glossaryTermProperties = this.getEgeriaGlossaryTermProperties(atlasGlossaryTerm);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              egeriaGlossaryTermTypeName,
                                                                                                              egeriaGlossaryTermGUID));


                    glossaryTermClient.updateGlossaryTerm(egeriaGlossaryTermGUID,
                                                          glossaryClient.getUpdateOptions(true),
                                                          glossaryTermProperties);


                    // todo this.setUpTermCategoriesInEgeria(egeriaGlossaryTermGUID, atlasGlossaryTerm);
                    this.setUpTermRelationshipsInEgeria(egeriaGlossaryTermGUID, atlasGlossaryTerm);
                }
            }
        }
    }


    /**
     * Return the Egeria glossary that matches the glossary where this Atlas element originates from.
     *
     * @param atlasGlossaryMember element to determine the glossary for
     * @return atlas glossary or null
     * @throws PropertyServerException there is a problem communicating with Apache Atlas
     */
    private OpenMetadataRootElement getEgeriaDestinationGlossaryForAtlasEntity(AtlasEntityWithExtInfo atlasGlossaryMember) throws PropertyServerException
    {
        if (atlasGlossaryMember != null)
        {
            AtlasEntityWithExtInfo glossaryAnchorElement = atlasClient.getRelatedEntity(atlasGlossaryMember, atlasGlossaryAnchorPropertyName);

            if (glossaryAnchorElement != null)
            {
                try
                {
                    AtlasEntityWithExtInfo atlasGlossary = atlasClient.getEntityByGUID(glossaryAnchorElement.getEntity().getGuid());

                    if (atlasGlossary != null)
                    {
                        String egeriaGlossaryGUID = getEgeriaGUID(atlasGlossary);

                        if (egeriaGlossaryGUID != null)
                        {
                            return glossaryClient.getCollectionByGUID(egeriaGlossaryGUID, null);
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
                                              AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
     * If the Atlas glossary has not yet been created, it is created.  If it already exists, it is updated with the latest
     * information from Egeria before being returned.
     *
     * @param egeriaGlossary owning glossary from egeria
     * @return atlas glossary element - or null if not possible to synchronize with Apache Atlas
     */
    private AtlasEntityWithExtInfo getAtlasDestinationGlossaryForEgeriaEntity(OpenMetadataRootElement egeriaGlossary)
    {
        try
        {
            if ((egeriaGlossary != null) && (egeriaGlossary.getProperties() instanceof GlossaryProperties glossaryProperties))
            {
                String atlasGlossaryGUID = getAtlasGUID(egeriaGlossary);

                if (atlasGlossaryGUID == null)
                {
                    /*
                     * Need to create the glossary
                     */
                    AtlasEntityWithExtInfo atlasGlossary = this.prepareGlossaryMemberEntity(null,
                                                                                            atlasGlossaryTypeName,
                                                                                            this.getAtlasGlossaryProperties(egeriaGlossary),
                                                                                            null);

                    atlasGlossaryGUID = atlasClient.addEntity(atlasGlossary.getEntity());

                    saveEgeriaGUIDInAtlas(atlasGlossaryGUID,
                                          egeriaGlossary.getElementHeader().getGUID(),
                                          glossaryProperties.getQualifiedName(),
                                          egeriaGlossaryTypeName,
                                          true,
                                          true);

                    this.ensureAtlasExternalIdentifier(egeriaGlossary,
                                                       glossaryProperties.getDisplayName(),
                                                       atlasGlossary.getEntity(),
                                                       atlasGlossaryGUID,
                                                       atlasGlossaryTypeName,
                                                       super.getAtlasStringProperty(atlasGlossary.getEntity().getAttributes(),
                                                                                    atlasNamePropertyName));
                }

                if (atlasGlossaryGUID != null)
                {
                    return atlasClient.getEntityByGUID(atlasGlossaryGUID);
                }
            }
        }
        catch (Exception notFound)
        {
            final String methodName = "getAtlasDestinationGlossaryForEgeriaEntity";
            /*
             * The Egeria glossary is not found - ignore the element for now - it is probably a timing window and the term is about to be deleted.
             */
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          notFound.getClass().getName(),
                                                                                                          methodName + "(" + egeriaGlossary.getElementHeader().getGUID() + ")",
                                                                                                          notFound.getMessage()),
                                      notFound);
            }
        }

        return null;
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
    private void setUpTermRelationshipsInEgeria(String                 egeriaTermGUID,
                                                AtlasEntityWithExtInfo atlasGlossaryTerm) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        // todo sort out term-to-term relationships
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
    private void createAtlasGlossaryCategoryInEgeria(AtlasEntityWithExtInfo  atlasGlossaryCategory,
                                                     OpenMetadataRootElement destinationGlossary) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "createAtlasGlossaryCategoryInEgeria";

        if (atlasGlossaryCategory != null)
        {
            AtlasEntity atlasEntity = atlasGlossaryCategory.getEntity();

            if (atlasEntity != null)
            {
                ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGlossaryCategory.getEntity().getGuid(),
                                                                                               atlasGlossaryCategoryTypeName,
                                                                                               atlasEntity.getCreatedBy(),
                                                                                               atlasEntity.getCreateTime(),
                                                                                               atlasEntity.getUpdatedBy(),
                                                                                               atlasEntity.getUpdateTime(),
                                                                                               atlasEntity.getVersion());

                ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(null,
                                                                                           null,
                                                                                           PermittedSynchronization.FROM_THIRD_PARTY);

                CollectionProperties glossaryCategoryProperties = this.getEgeriaGlossaryCategoryProperties(atlasGlossaryCategory);

                NewElementOptions newElementOptions = new NewElementOptions(glossaryClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(destinationGlossary.getElementHeader().getGUID());
                newElementOptions.setAnchorScopeGUID(destinationGlossary.getElementHeader().getGUID());
                newElementOptions.setParentGUID(destinationGlossary.getElementHeader().getGUID());
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

                String egeriaCategoryGUID = glossaryClient.createCollection(newElementOptions,
                                                                            null,
                                                                            glossaryCategoryProperties,
                                                                            null);

                externalIdClient.createExternalId(egeriaCategoryGUID, externalIdLinkProperties, externalIdentifierProperties);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          egeriaGlossaryCategoryTypeName,
                                                                                                          egeriaCategoryGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaCategoryGUID,
                                      glossaryCategoryProperties.getQualifiedName(),
                                      egeriaGlossaryCategoryTypeName,
                                      true,
                                      false);

                //this.setUpCategoryHierarchyInEgeria(egeriaCategoryGUID, atlasGlossaryCategory);
            }
        }
    }


    /**
     * Is the category a top-level category - ie one with no parent category.
     *
     * @param atlasGlossaryCategory atlas category entity
     * @return boolean flag
     */
    private boolean isTopLevelCategory(AtlasEntityWithExtInfo atlasGlossaryCategory)
    {
        if ((atlasGlossaryCategory != null) && (atlasGlossaryCategory.getEntity() != null))
        {
            Map<String, Object> relationshipAttributes = atlasGlossaryCategory.getEntity().getRelationshipAttributes();

            if (relationshipAttributes != null)
            {
                return (relationshipAttributes.get("parentCategory") == null);
            }
        }

        return true;
    }


    /**
     * Retrieve a specific glossary based on its qualified name.  This glossary is expected to be present.
     * It is passed in as a configuration property.
     *
     * @param glossaryQualifiedName requested glossary
     * @param methodName calling method
     * @return element or null
     * @throws ConnectorCheckedException unexpected exception
     */
    private OpenMetadataRootElement getGlossaryByQualifiedName(String glossaryQualifiedName,
                                                               String methodName) throws ConnectorCheckedException
    {
        try
        {
            List<OpenMetadataRootElement> egeriaGlossaries = glossaryClient.getCollectionsByName(glossaryQualifiedName, glossaryClient.getQueryOptions(0, 0));

            if (egeriaGlossaries != null)
            {
                for (OpenMetadataRootElement glossaryElement : egeriaGlossaries)
                {
                    if ((glossaryElement != null) && (glossaryElement.getProperties() instanceof GlossaryProperties glossaryProperties))
                    {
                        String qualifiedName = glossaryProperties.getQualifiedName();

                        if (glossaryQualifiedName.equals(qualifiedName))
                        {
                            return glossaryElement;
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                          error.getClass().getName(),
                                                                                                          methodName,
                                                                                                          error.getMessage()),
                                      error);


            }

            throw new ConnectorCheckedException(AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }

        return null;
    }


    /* ======================================
     * Transfer properties from one format to another
     */


    /**
     * Copy the properties from an Atlas glossary into the Asset Manager Properties bean.
     *
     * @param atlasGlossaryElement source element
     * @return properties bean
     */
    private GlossaryProperties getEgeriaGlossaryProperties(AtlasEntityWithExtInfo  atlasGlossaryElement)
    {
        GlossaryProperties glossaryProperties = new GlossaryProperties();
        Map<String,Object> atlasAttributes = atlasGlossaryElement.getEntity().getAttributes();

        glossaryProperties.setQualifiedName("AtlasGlossary." + this.getAtlasStringProperty(atlasAttributes, atlasQualifiedNamePropertyName));
        glossaryProperties.setDisplayName(this.getAtlasStringProperty(atlasAttributes, atlasNamePropertyName));
        String shortDescription = this.getAtlasStringProperty(atlasAttributes, atlasShortDescriptionPropertyName);
        if (shortDescription != null)
        {
            glossaryProperties.setDescription(shortDescription + "\n\n" + this.getAtlasStringProperty(atlasAttributes, atlasLongDescriptionPropertyName));
        }
        else
        {
            glossaryProperties.setDescription(this.getAtlasStringProperty(atlasAttributes, atlasLongDescriptionPropertyName));
        }
        glossaryProperties.setLanguage(this.getAtlasStringProperty(atlasAttributes, atlasLanguagePropertyName));
        glossaryProperties.setUsage(this.getAtlasStringProperty(atlasAttributes, atlasUsagePropertyName));

        return glossaryProperties;
    }


    /**
     * Set up the properties from an open metadata glossary into an Apache Atlas glossary properties object.
     *
     * @param egeriaGlossary open metadata glossary
     * @return Apache Atlas properties object
     */
    Map<String, Object> getAtlasGlossaryProperties(OpenMetadataRootElement egeriaGlossary)
    {
        Map<String, Object> properties = new HashMap<>();

        if (egeriaGlossary.getProperties() instanceof GlossaryProperties glossaryProperties)
        {
            properties.put(atlasQualifiedNamePropertyName, glossaryProperties.getQualifiedName());
            properties.put(atlasNamePropertyName, glossaryProperties.getDisplayName());

            if (glossaryProperties.getDescription() != null)
            {
                String[] descriptionSentences = glossaryProperties.getDescription().split(Pattern.quote(". "));
                if (descriptionSentences.length > 0)
                {
                    properties.put(atlasShortDescriptionPropertyName, descriptionSentences[0]);
                }
            }
            properties.put(atlasLongDescriptionPropertyName, glossaryProperties.getDescription());
            properties.put(atlasLanguagePropertyName, glossaryProperties.getLanguage());
            properties.put(atlasUsagePropertyName, glossaryProperties.getUsage());

            if (glossaryProperties.getAdditionalProperties() != null)
            {
                properties.put(atlasAdditionalAttributesPropertyName,glossaryProperties.getAdditionalProperties());
            }
        }

        return properties;
    }


    /**
     * Copy the properties from an Atlas glossary term into the Asset Manager Properties bean.
     *
     * @param atlasGlossaryTermElement source element
     * @return properties bean
     */
    private GlossaryTermProperties getEgeriaGlossaryTermProperties(AtlasEntityWithExtInfo  atlasGlossaryTermElement)
    {
        GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();
        Map<String,Object> atlasAttributes = atlasGlossaryTermElement.getEntity().getAttributes();

        glossaryTermProperties.setQualifiedName("AtlasGlossaryTerm." + this.getAtlasStringProperty(atlasAttributes, atlasQualifiedNamePropertyName));
        glossaryTermProperties.setDisplayName(this.getAtlasStringProperty(atlasAttributes, atlasNamePropertyName));
        String shortDescription = this.getAtlasStringProperty(atlasAttributes, atlasShortDescriptionPropertyName);
        if (shortDescription != null)
        {
            glossaryTermProperties.setDescription(shortDescription + "\n\n" + this.getAtlasStringProperty(atlasAttributes, atlasLongDescriptionPropertyName));
        }
        else
        {
            glossaryTermProperties.setDescription(this.getAtlasStringProperty(atlasAttributes, atlasLongDescriptionPropertyName));
        }
        glossaryTermProperties.setExamples(this.getAtlasStringProperty(atlasAttributes, atlasExamplesPropertyName));
        glossaryTermProperties.setAbbreviation(this.getAtlasStringProperty(atlasAttributes, atlasAbbreviationPropertyName));
        glossaryTermProperties.setUsage(this.getAtlasStringProperty(atlasAttributes, atlasUsagePropertyName));

        return glossaryTermProperties;
    }


    /**
     * Set up the properties from an open metadata glossary term into an Apache Atlas glossary term properties object.
     *
     * @param egeriaGlossaryTerm open metadata glossary term
     * @param egeriaGlossaryTermLastKnownName the display name that was present on the previous synchronization
     * @param atlasGlossaryTermGUID unique identifier from Apache Atlas
     * @param atlasGlossaryTermName unique display name from Apache Atlas
     * @param atlasGlossary destination glossary in Apache Atlas
     * @return Apache Atlas attribute map
     * @throws PropertyServerException problem calling Atlas
     */
    Map<String, Object> getAtlasGlossaryTermProperties(OpenMetadataRootElement egeriaGlossaryTerm,
                                                       String                  egeriaGlossaryTermLastKnownName,
                                                       String                  atlasGlossaryTermGUID,
                                                       String                  atlasGlossaryTermName,
                                                       AtlasEntityWithExtInfo  atlasGlossary) throws PropertyServerException
    {
        Map<String, Object> properties = new HashMap<>();

        if (egeriaGlossaryTerm.getProperties() instanceof GlossaryTermProperties glossaryTermProperties)
        {
            String name;

            if (atlasGlossaryTermGUID == null)
            {
                name = this.getUniqueNameForNewAtlasGlossaryMember(glossaryTermProperties.getDisplayName(),
                                                                   atlasGlossary,
                                                                   atlasGlossaryTermsPropertyName);
            }
            else if (glossaryTermProperties.getDisplayName().equals(egeriaGlossaryTermLastKnownName))
            {
                name = atlasGlossaryTermName;
            }
            else
            {
                name = this.getUniqueNameForRenamedAtlasGlossaryMember(glossaryTermProperties.getDisplayName(),
                                                                       atlasGlossaryTermGUID,
                                                                       atlasGlossary,
                                                                       atlasGlossaryTermsPropertyName);
            }

            properties.put(atlasQualifiedNamePropertyName,
                           this.getAtlasStringProperty(atlasGlossary.getEntity().getAttributes(), atlasQualifiedNamePropertyName) + "@" + name);
            properties.put(atlasNamePropertyName, name);


            if (glossaryTermProperties.getDescription() != null)
            {
                String[] descriptionSentences = glossaryTermProperties.getDescription().split(Pattern.quote(". "));
                if (descriptionSentences.length > 0)
                {
                    properties.put(atlasShortDescriptionPropertyName, descriptionSentences[0]);
                }
            }
            properties.put(atlasLongDescriptionPropertyName, glossaryTermProperties.getDescription());
            properties.put(atlasAbbreviationPropertyName, glossaryTermProperties.getAbbreviation());
            properties.put(atlasUsagePropertyName, glossaryTermProperties.getUsage());

            if (glossaryTermProperties.getAdditionalProperties() != null)
            {
                properties.put(atlasAdditionalAttributesPropertyName, glossaryTermProperties.getAdditionalProperties());
            }
        }

        return properties;
    }


    /**
     * Copy the properties from an Atlas glossary category into the Asset Manager Properties bean.
     *
     * @param atlasGlossaryCategory source element
     * @return properties bean
     */
    private CollectionProperties getEgeriaGlossaryCategoryProperties(AtlasEntityWithExtInfo  atlasGlossaryCategory)
    {
        CollectionProperties glossaryCategoryProperties = new CollectionProperties();
        Map<String, Object>  atlasAttributes            = atlasGlossaryCategory.getEntity().getAttributes();


        glossaryCategoryProperties.setQualifiedName("AtlasGlossaryCategory." +
                                                            this.getAtlasStringProperty(atlasAttributes, atlasQualifiedNamePropertyName));
        glossaryCategoryProperties.setDisplayName(this.getAtlasStringProperty(atlasAttributes, atlasNamePropertyName));

        String shortDescription = this.getAtlasStringProperty(atlasAttributes, atlasShortDescriptionPropertyName);
        if (shortDescription != null)
        {
            glossaryCategoryProperties.setDescription(shortDescription + "\n\n" + this.getAtlasStringProperty(atlasAttributes, atlasLongDescriptionPropertyName));
        }
        else
        {
            glossaryCategoryProperties.setDescription(this.getAtlasStringProperty(atlasAttributes, atlasLongDescriptionPropertyName));
        }

        return glossaryCategoryProperties;
    }


    /**
     * Create the Apache Atlas entity structure for a new Glossary Term or Category.
     *
     * @param memberGUID unique identifier of the Apache Atlas entity (will be null for a create)
     * @param memberTypeName Apache Atlas type name for the member
     * @param memberAttributes map of attributes for entity
     * @param destinationGlossary Apache Atlas glossary where the member resides
     * @return Apache Atlas Entity structure used on create or update
     */
    AtlasEntityWithExtInfo prepareGlossaryMemberEntity(String                 memberGUID,
                                                       String                 memberTypeName,
                                                       Map<String, Object>    memberAttributes,
                                                       AtlasEntityWithExtInfo destinationGlossary)
    {
        AtlasEntityWithExtInfo atlasEntityWithExtInfo = new AtlasEntityWithExtInfo();
        AtlasEntity            atlasEntity            = new AtlasEntity();

        atlasEntity.setGuid(memberGUID);
        atlasEntity.setTypeName(memberTypeName);
        atlasEntity.setAttributes(memberAttributes);

        Map<String, Object> relationshipAttributes = new HashMap<>();

        if (destinationGlossary != null)
        {
            relationshipAttributes.put(atlasGlossaryAnchorPropertyName, this.getAnchorAttributes(memberTypeName, destinationGlossary));

            atlasEntity.setRelationshipAttributes(relationshipAttributes);
        }

        atlasEntityWithExtInfo.setEntity(atlasEntity);

        return atlasEntityWithExtInfo;
    }


    /**
     * Set up the glossary anchor in an atlas entity for either a glossary term or a glossary category.
     *
     * @param memberTypeName type of member
     * @param destinationGlossary anchor glossary
     */
    private Map<String, Object> getAnchorAttributes(String                 memberTypeName,
                                                    AtlasEntityWithExtInfo destinationGlossary)
    {

        Map<String, Object> anchorAttributes = new HashMap<>();

        anchorAttributes.put("guid", destinationGlossary.getEntity().getGuid());

        if (atlasGlossaryTermTypeName.equals(memberTypeName))
        {
            anchorAttributes.put("relationshipType", "AtlasGlossaryTermAnchor");
        }
        else
        {
            anchorAttributes.put("relationshipType", "AtlasGlossaryCategoryAnchor");
        }

        anchorAttributes.put("relationshipStatus", "ACTIVE");

        return anchorAttributes;
    }


    /**
     * Calculate the name to use in Atlas for an open metadata category or term.  It is a 1-1 mapping unless the Egeria glossary has
     * multiple members with the same name.  In that case, a bracketed number is added to the end of the name.
     *
     * @param egeriaMemberName name for the element from the open metadata ecosystem
     * @param atlasGlossary atlas glossary where the member is going
     * @param existingEntitiesParameterName name of the parameter in the glossary that identifies the existing members that the name must not match with
     * @return name to use for the new member
     * @throws PropertyServerException there was a problem communicating with Apache Atlas
     */
    private String getUniqueNameForNewAtlasGlossaryMember(String                 egeriaMemberName,
                                                          AtlasEntityWithExtInfo atlasGlossary,
                                                          String                 existingEntitiesParameterName) throws PropertyServerException
    {
        List<AtlasEntityWithExtInfo> existingEntities = atlasClient.getRelatedEntities(atlasGlossary, existingEntitiesParameterName);

        return this.getUniqueNameForAtlasEntity(egeriaMemberName, existingEntities);
    }


    /**
     * A glossary member (ie term or category) that originates from the open metadata ecosystem has been renamed.
     * This method finds a unique name for the equivalent entity in Apache Atlas, being careful to avoid name clashes.
     *
     * @param newEgeriaMemberName name for the element from the open metadata ecosystem
     * @param atlasMemberGUID unique identifier for the existing Atlas member entity
     * @param atlasGlossary atlas glossary where the atlas member came from
     * @param existingEntitiesParameterName name of the parameter in the glossary that identifies the existing members that the name must not match with
     * @return new name to use for the member in Atlas
     * @throws PropertyServerException there was a problem communicating with Apache Atlas
     */
    private String getUniqueNameForRenamedAtlasGlossaryMember(String                 newEgeriaMemberName,
                                                              String                 atlasMemberGUID,
                                                              AtlasEntityWithExtInfo atlasGlossary,
                                                              String                 existingEntitiesParameterName) throws PropertyServerException
    {
        List<AtlasEntityWithExtInfo> existingEntities = atlasClient.getRelatedEntities(atlasGlossary, existingEntitiesParameterName);
        List<AtlasEntityWithExtInfo> otherExistingEntities = null;

        if (existingEntities != null)
        {
            /*
             * Remove the atlas entity that is about to be renamed from the list.
             */
            otherExistingEntities = new ArrayList<>();

            for (AtlasEntityWithExtInfo existingEntity : existingEntities)
            {
                if ((existingEntity != null) &&
                    (existingEntity.getEntity() != null) &&
                            (! atlasMemberGUID.equals(existingEntity.getEntity().getGuid())))
                {
                    otherExistingEntities.add(existingEntity);
                }
            }
        }

        /*
         * Find a new name.
         */
        return this.getUniqueNameForAtlasEntity(newEgeriaMemberName, otherExistingEntities);
    }


    /**
     * Calculate the name to use in Atlas for an open metadata glossary member.  It is a 1-1 mapping unless Egeria has
     * multiple elements with the same name.  In that case, a bracketed number is added to the end of the name.
     *
     * @param egeriaName display name from Egeria
     * @param existingEntities map of known entities from Atlas
     * @return name to use
     */
    private String getUniqueNameForAtlasEntity(String                        egeriaName,
                                               List<AtlasEntityWithExtInfo>  existingEntities)
    {
        /*
         * There are no existing elements so the name from Egeria can be used.
         */
        if ((existingEntities == null) || (existingEntities.isEmpty()))
        {
            return egeriaName;
        }

        int matchingNameCount = 0;

        /*
         * Step through all the existing entities and determine if the name from Egeria would be unique in Atlas.
         */
        for (AtlasEntityWithExtInfo existingAtlasEntity : existingEntities)
        {
            /*
             * Extract the name from the existing entity.
             */
            String existingEntityName = this.getAtlasStringProperty(existingAtlasEntity.getEntity().getAttributes(),
                                                                    atlasNamePropertyName);

            if (! egeriaName.equals(existingEntityName))
            {
                /*
                 * There is no direct name match.  Look to see if the existing entity has a modified name.
                 */
                String[] termNameParts = existingEntityName.split(Pattern.quote(" ("));

                if ((termNameParts.length > 1) &&          // it has a bracket in the existing element's name
                    (egeriaName.equals(termNameParts[0]))) // the first part of the existing element's name matches the egeria name
                {
                    /*
                     * There is an open bracket in the name.  This could be a modified name to handle multiple glossary terms within the glossary.
                     * Check for the closing bracket in the last part of the name.
                     */
                    String[] termEndingParts = termNameParts[termNameParts.length - 1].split(Pattern.quote(")"));

                    if (termEndingParts.length == 1) // Only one closing bracket - is it a number between the brackets?
                    {
                        try
                        {
                            int matchingNameIndex = Integer.parseInt(termEndingParts[0]);

                            /*
                             * The entities are not processes in neat numerical order so increment the matching name count beyond the
                             * index from the modified name if it is the largest index number seen so far.
                             */
                            if (matchingNameIndex >= matchingNameCount)
                            {
                                matchingNameCount = matchingNameIndex + 1;
                            }
                        }
                        catch (NumberFormatException notANumber)
                        {
                            // Ignore the entity because it does not have one of our modified names
                        }
                    }
                }
            }
            else
            {
                /*
                 * There is a direct match between the egeria name and an existing entity.
                 */
                matchingNameCount = matchingNameCount + 1;
            }
        }

        if (matchingNameCount == 0)
        {
            /*
             * No matches detected
             */
            return egeriaName;
        }
        else
        {
            /*
             * There are matching names - use the next unique name.
             */
            return egeriaName + " (" + matchingNameCount + ")";
        }
    }
}
