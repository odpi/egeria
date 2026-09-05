/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard;


import org.odpi.openmetadata.adapters.connectors.baudot.controls.BaudotCatalogTarget;
import org.odpi.openmetadata.adapters.connectors.subscriptions.ManageDigitalSubscriptionActionTarget;
import org.odpi.openmetadata.adapters.connectors.subscriptions.ManageDigitalSubscriptionRequestParameter;
import org.odpi.openmetadata.adapters.connectors.jacquard.controls.JacquardConfigurationProperty;
import org.odpi.openmetadata.adapters.connectors.jacquard.ffdc.JacquardAuditCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.ffdc.JacquardErrorCode;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.*;
import org.odpi.openmetadata.adapters.connectors.jacquard.solutionblueprint.*;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetListConnector;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata.ReferenceDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueDataSetProvider;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.validmetadatavalues.ValidMetadataValueSetListConnector;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.controls.TabularDataSetConfigurationProperty;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.integration.connectors.DynamicIntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.OMFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryPropertiesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PersonRoleProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.PerspectiveProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.TabularDataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.connections.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.NoteLogProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionBlueprintProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentActorProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionLinkingWireProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyAssignmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.validvalues.SpecificationPropertyValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;

import java.util.*;


/**
 * OpenMetadataProductsHarvesterConnector converts metadata from the open metadata ecosystem into useful digital
 * products.  The first phase (in the start() method) is to create all the contextual metadata that surrounds the
 * product catalog.  The remaining phases happen in the refresh() method.  It first surveys open metadata looking for
 * metadata that could be a product.  (For example, a valid value set).  It creates an entry in the product catalog
 * for that product and registers the tabular data set asset for the product as a catalog target.
 * <br><br>
 * Once the possible products are in place, it processes the catalog targets.  For each, the appropriate metadata
 * is scanned for changes.  These changes are recorded in the asset's GovernanceMeasurement classification.
 * This triggers the notification watchdog to send the new data to the subscribers via the provisioning pipelines.
 */
public class JacquardIntegrationConnector extends DynamicIntegrationConnectorBase
{
    /*
     * Set everything to null to catch issue where refresh() is called without start() since
     * It will result in NPEs.  This is not expected.
     */
    private String              solutionBlueprintGUID = null;
    private List<String>        anchorScopeGUIDs      = null;
    private Map<String, String> productFolders        = null;
    private Map<String, String> productRoles          = null;
    private Map<String, String> governanceDefinitions = null;
    private Map<String, String> glossaryTerms         = null;
    private Map<String, String> questions             = null;
    private Map<String, String> communities           = null;
    private Map<String, String> communityNoteLogs     = null;
    private Map<String, String> dataFields            = null;
    private Map<String, String> products              = null;

    /*
     * This is the Baudot Subscription Manager: the integration connector that notifies the subscribers of the
     * products' notification types.  Each notification type this connector creates is handed to it as a catalog
     * target.  Its unique identifier comes from this connector's configuration properties, seeded by the content
     * pack; null means no subscription manager is configured, and the notification types are not handed on.
     */
    private String subscriptionManagerGUID = null;

    /*
     * The notification types already handed to the subscription manager, so that they are not added twice.
     * Loaded from the manager's existing catalog targets at start-up, and added to as notification types are
     * handed on.
     */
    private final Set<String> subscriptionManagerCatalogTargets = new HashSet<>();

    /*
     * The product assets that are already this connector's own catalog targets, so that a product is not added
     * again on every refresh.  Loaded at start-up, and added to as products are catalogued.
     */
    private final Set<String> ownCatalogTargets = new HashSet<>();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It sets up the contextual metadata used to fill out the product catalog.
     * This includes the solution blueprint that covers the components involved in managing
     * the Open Metadata Digital Product Catalog.  Then there is the product catalog itself with its
     * internal folders, glossary, and data dictionary.  The glossary is then populated
     * with glossary terms, and the data dictionary is populated with data fields.
     * The guids for these elements are managed in instance variables to allow the products
     * to link to them.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public  void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        try
        {
            logRecord(methodName, JacquardAuditCode.STARTING_CONNECTOR.getMessageDefinition(connectorName,
                                                                                            integrationContext.getMetadataAccessServer(),
                                                                                            integrationContext.getMetadataAccessServerPlatformURLRoot()));

            if ((secretsStoreConnectorMap == null) || (secretsStoreConnectorMap.isEmpty()))
            {
                throw new ConnectorCheckedException(JacquardErrorCode.NO_SECRETS.getMessageDefinition(connectorName),
                                                    this.getClass().getName(),
                                                    methodName);
            }

            /*
             * These governance definitions support the product catalog initiative.
             * They are linked to the product catalog.
             */
            governanceDefinitions = this.getGovernanceDefinitions();

            /*
             * These definitions feature in the solution blueprint.  It also includes
             * two generic roles (developer and jacquard support) that apply to all products.
             * A separate Product Manager role is defined for each product.  The product manager
             * role created below is a solution actor role for the blueprint.
             */
            productRoles          = this.getProductRoles();
            solutionBlueprintGUID = this.getSolutionBlueprint();

            /*
             * The product folders are set up first so that the top-level folder for the product catalog can be
             * the anchor scope for everything else.
             */
            productFolders = this.getProductCatalogFolders();

            /*
             * The subscription manager is located before the products are built, because each product's
             * notification types are handed to it as they are created.  It is another integration connector -
             * the Baudot Subscription Manager - and this connector is told which one by a configuration
             * property that the content pack seeds with the manager's unique identifier.  If it is not
             * configured, the products are still built but their subscribers are never notified, and that is
             * said loudly here because from outside it looks exactly like a subscription that was never asked
             * for.
             */
            this.loadOwnCatalogTargets();

            subscriptionManagerGUID = this.getSubscriptionManagerGUID();

            if (subscriptionManagerGUID == null)
            {
                logRecord(methodName,
                          JacquardAuditCode.NO_SUBSCRIPTION_MANAGER.getMessageDefinition(connectorName,
                                                                                         JacquardConfigurationProperty.SUBSCRIPTION_MANAGER_GUID.getName()));
            }
            else
            {
                this.loadSubscriptionManagerCatalogTargets();
            }

            glossaryTerms         = this.getGlossaryTerms();
            questions             = this.getQuestions();
            communities           = this.getCommunities();
            communityNoteLogs     = this.getCommunityNoteLogs();
            dataFields            = this.getDataFields();
            products              = this.getProducts(subscriptionManagerGUID);
        }
        catch (Exception error)
        {
            /*
             * Check that the error is not caused because the server/platform is shutting down.
             */
            integrationContext.validateIsActive(methodName);

            /*
             * OK so this is really unexpected.
             */
            logRecord(methodName,
                      JacquardAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                  error.getClass().getName(),
                                                                                  methodName,
                                                                                  error.getMessage()));

            throw new ConnectorCheckedException(JacquardErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                            error.getClass().getName(),
                                                                                                            methodName,
                                                                                                            error.getMessage()),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * First make sure that all possible tabular data sets have been harvested.  These are set up as catalog targets.
     * Then process each catalog target.  It will record details of any changes to the catalog target's data.
     *
     * @throws ConnectorCheckedException  a problem with the connector.  It cannot refresh the metadata.
     * @throws UserNotAuthorizedException the connector has been disconnected
     */
    @Override
    public void refresh() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "refresh";

        /*
         * Determine the existing catalog targets - these are tabular data sources that are set up.
         */
        logRecord(methodName, JacquardAuditCode.HARVESTING_CATALOG_TARGETS.getMessageDefinition(integrationContext.getConnectorName()));
        List<RequestedCatalogTarget> requestedCatalogTargets = catalogTargetsManager.retrieveKnownCatalogTargets(integrationContext,
                                                                                                                 this);

        Map<String, RequestedCatalogTarget> existingDataSources = new HashMap<>();

        if (requestedCatalogTargets != null)
        {
            for (RequestedCatalogTarget requestedCatalogTarget : requestedCatalogTargets)
            {
                existingDataSources.put(requestedCatalogTarget.getCatalogTargetName(), requestedCatalogTarget);
            }
        }

        /*
         * Call each of the insight harvesters to check they have their catalog targets set up.
         */
        logRecord(methodName, JacquardAuditCode.HARVESTING_VALID_VALUES.getMessageDefinition(integrationContext.getConnectorName()));
        harvestValidMetadataValues(existingDataSources, subscriptionManagerGUID);
        logRecord(methodName, JacquardAuditCode.HARVESTING_REFERENCE_DATA_SETS.getMessageDefinition(integrationContext.getConnectorName()));
        harvestReferenceDataSets(existingDataSources, subscriptionManagerGUID);

        /*
         * Refresh all the harvested tabular data sources, looking for data changes.
         */
        super.refresh();
    }


    /**
     * Make sure data sources are set up for all valid value sets.
     * It extracts the valid metadata value list from the catalog targets
     *
     * @param existingDataSources existing data source map
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector notifications
     * @throws ConnectorCheckedException problem access the valid value set list
     * @throws UserNotAuthorizedException the user is not authorized to access the catalog (probably shutdown requested)
     */
    private void harvestValidMetadataValues(Map<String, RequestedCatalogTarget> existingDataSources,
                                            String                              subscriptionManagerGUID) throws ConnectorCheckedException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName = "harvestValidValues";

        RequestedCatalogTarget validValueSetList = existingDataSources.get(ProductDefinitionEnum.VALID_METADATA_VALUE_SET_LIST.getCatalogTargetName());

        if (validValueSetList != null)
        {
            Connector connectorToTarget = validValueSetList.getConnectorToTarget();

            if (connectorToTarget instanceof ValidMetadataValueSetListConnector validMetadataValueSetListConnector)
            {
                long recordCount  = validMetadataValueSetListConnector.getRecordCount();
                int  columnNumber = validMetadataValueSetListConnector.getColumnNumber(ProductDataFieldDefinition.PROPERTY_NAME.getDisplayName());

                if (recordCount > 0)
                {
                    for (long rowNumber = 0; rowNumber < recordCount; rowNumber++)
                    {
                        List<String> rowValues = validMetadataValueSetListConnector.readRecord(rowNumber);

                        if ((rowValues != null) && (rowValues.size() > columnNumber))
                        {
                            String propertyName = rowValues.get(columnNumber);

                            /*
                             * Every valid value set goes through, including the ones this connector already has
                             * a data source for.  Refreshing an existing product is a find rather than a build,
                             * and it is also what checks that the product's connection still describes this
                             * deployment - the platform's URL in particular.  Skipping known sets left their
                             * endpoints pointing at whichever platform first catalogued them, for good.
                             */
                            if (propertyName != null)
                            {
                                try
                                {
                                    refreshValidMetadataValueDataSet(propertyName, subscriptionManagerGUID);
                                }
                                catch (Exception error)
                                {
                                    /*
                                     * Check that the error is not caused because the server/platform is shutting down.
                                     */
                                    integrationContext.validateIsActive(methodName);

                                    /*
                                     * OK so this is really unexpected.
                                     */
                                    if (! this.logDuplicateElementDetected(error, methodName))
                                    {
                                        logExceptionRecord(methodName,
                                                           JacquardAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       error.getMessage()),
                                                           error);
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
     * Create a product that represents a single valid metadata value set.
     *
     * @param propertyName unique name of the valid value set
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector notifications
     * @throws InvalidParameterException  an invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException    the repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     */
    private void refreshValidMetadataValueDataSet(String propertyName,
                                                  String subscriptionManagerGUID) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        /*
         * Create a dynamic product definition and add it to the open metadata ecosystem.
         */
        ValidMetadataValueDataSetProvider provider = new ValidMetadataValueDataSetProvider();

        ProductDefinition productDefinition = provider.getProductDefinition(propertyName,
                                                                            super.fromCamelToCanonicalCase(propertyName) + " Valid Values",
                                                                            this.getPropertyDescription(propertyName));

        this.getProduct(productDefinition, subscriptionManagerGUID);
    }


    /**
     * Return the property description for this valid values set.
     *
     * @param propertyName name of an open metadata property
     * @return string
     */
    private String getPropertyDescription(String propertyName)
    {
        for (OpenMetadataProperty openMetadataProperty : OpenMetadataProperty.values())
        {
            if (openMetadataProperty.name.equals(propertyName))
            {
                return openMetadataProperty.description;
            }
        }

        return "Valid values for open metadata property: " + propertyName;
    }


    /**
     * Make sure data sources are set up for all valid value sets.
     * It extracts the valid metadata value list from the catalog targets
     *
     * @param existingDataSources existing data source map
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector notifications
     * @throws ConnectorCheckedException problem access the valid value set list
     * @throws UserNotAuthorizedException the user is not authorized to access the catalog (probably shutdown requested)
     */
    private void harvestReferenceDataSets(Map<String, RequestedCatalogTarget>  existingDataSources,
                                          String                               subscriptionManagerGUID) throws ConnectorCheckedException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "harvestReferenceDataSets";

        RequestedCatalogTarget referenceDataSetList = existingDataSources.get(ProductDefinitionEnum.REFERENCE_DATA_SET_LIST.getCatalogTargetName());

        if (referenceDataSetList != null)
        {
            Connector connectorToTarget = referenceDataSetList.getConnectorToTarget();

            if (connectorToTarget instanceof ReferenceDataSetListConnector referenceDataSetListConnector)
            {
                long recordCount  = referenceDataSetListConnector.getRecordCount();

                int  guidColumnNumber = referenceDataSetListConnector.getColumnNumber(ProductDataFieldDefinition.GUID.getDisplayName());
                int  identifierColumnNumber = referenceDataSetListConnector.getColumnNumber(ProductDataFieldDefinition.IDENTIFIER.getDisplayName());
                int  descriptionColumnNumber = referenceDataSetListConnector.getColumnNumber(ProductDataFieldDefinition.DESCRIPTION.getDisplayName());

                if (recordCount > 0)
                {
                    for (long rowNumber = 0; rowNumber < recordCount; rowNumber++)
                    {
                        List<String> rowValues = referenceDataSetListConnector.readRecord(rowNumber);

                        if ((rowValues != null) && (rowValues.size() > guidColumnNumber))
                        {
                            String referenceDataSetGUID = rowValues.get(guidColumnNumber);
                            String identifier = rowValues.get(identifierColumnNumber);
                            String description = rowValues.get(descriptionColumnNumber);

                            /*
                             * Every reference data set goes through, known or not, for the same reason as the
                             * valid value sets above: a refresh of an existing product is a find, and it keeps
                             * the product's connection current.
                             */
                            if (referenceDataSetGUID != null)
                            {
                                try
                                {
                                    refreshReferenceDataSet(referenceDataSetGUID, identifier, description, subscriptionManagerGUID);
                                }
                                catch (Exception error)
                                {
                                    /*
                                     * Check that the error is not caused because the server/platform is shutting down.
                                     */
                                    integrationContext.validateIsActive(methodName);

                                    /*
                                     * OK so this is really unexpected.
                                     */
                                    if (! this.logDuplicateElementDetected(error, methodName))
                                    {
                                        logExceptionRecord(methodName,
                                                           JacquardAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       error.getMessage()),
                                                           error);
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
     * Create a product that represents a single reference data value set.
     *
     * @param referenceDataSetGUID   unique identifier of the reference data set
     * @param identifier             unique name of the reference data set
     * @param description            description of the reference data set
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector notifications
     * @throws InvalidParameterException  an invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException    the repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     */
    private void refreshReferenceDataSet(String referenceDataSetGUID,
                                         String identifier,
                                         String description,
                                         String subscriptionManagerGUID) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        /*
         * Create a dynamic product definition and add it to the open metadata ecosystem.
         */
        ReferenceDataSetProvider provider = new ReferenceDataSetProvider();

        ProductDefinition productDefinition = provider.getProductDefinition(referenceDataSetGUID,
                                                                            identifier,
                                                                            super.fromCamelToCanonicalCase(identifier) + " Reference Data Set",
                                                                            description);

        this.getProduct(productDefinition, subscriptionManagerGUID);
    }


    /*
     * The error the metadata server reports when a lookup by qualified name finds more than one element.  When
     * it does, the server links the copies as peer duplicates (in DISCOVERED status) before reporting it.
     */
    private static final String MULTIPLE_ENTITIES_FOUND_MESSAGE_ID = "OMAG-GENERIC-HANDLERS-404-002";


    /**
     * Recognise the failure of a lookup that found two elements where there should be one, and log it as what
     * it is: a duplicate for the duplicate manager to resolve, not an unexpected error.
     * <br><br>
     * Uniqueness of a qualified name cannot be guaranteed in a federated environment, so two writers can create
     * the same element at the same instant.  The metadata server has already linked the copies with
     * PeerDuplicateLink relationships by the time this connector hears about it, and the Mendel Automated
     * Duplicate Manager takes it from there - confirming close matches, referring the rest to a steward and
     * consolidating them.  This connector does not choose between the copies; it skips what it was cataloguing
     * and comes back to it on a later refresh, when the copies have been combined.
     *
     * @param error the exception from the lookup
     * @param subject what was being catalogued, for the log
     * @return true if the error was a duplicate and has been logged; false if it is something else
     */
    private boolean logDuplicateElementDetected(Exception error,
                                                String    subject)
    {
        final String methodName = "logDuplicateElementDetected";

        if ((error instanceof OMFCheckedExceptionBase omfError) &&
                (MULTIPLE_ENTITIES_FOUND_MESSAGE_ID.equals(omfError.getReportedErrorMessageId())))
        {
            /*
             * The qualified name is the second insert of the server's message; the message is the most reliable
             * place to read it from since the lookup that failed may be several calls deep.
             */
            String qualifiedName = "unknown";
            String message       = omfError.getReportedErrorMessage();

            if (message != null)
            {
                int start = message.indexOf("with a name of ");
                int end   = message.indexOf(": the identifiers of the returned entities");

                if ((start >= 0) && (end > start))
                {
                    qualifiedName = message.substring(start + "with a name of ".length(), end);
                }
            }

            logRecord(methodName,
                      JacquardAuditCode.DUPLICATE_ELEMENT_DETECTED.getMessageDefinition(connectorName,
                                                                                        qualifiedName,
                                                                                        subject));
            return true;
        }

        return false;
    }


    /**
     * Return the map of qualifiedNames-to-guids for the pre-defined products that make up the
     * fixed part of the product catalog.
     *
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector
     *
     * @return map
     * @throws InvalidParameterException an invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException the repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getProducts(String subscriptionManagerGUID) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        products = new HashMap<>();

        for (ProductDefinition productDefinition : ProductDefinitionEnum.values())
        {
            try
            {
                String productGUID = this.getProduct(productDefinition, subscriptionManagerGUID);

                products.put(productDefinition.getQualifiedName(), productGUID);
            }
            catch (PropertyServerException error)
            {
                /*
                 * A product that exists twice is left to the duplicate manager and the rest of the catalogue is
                 * still built; anything else is as unexpected as it always was.
                 */
                if (! this.logDuplicateElementDetected(error, productDefinition.getProductName()))
                {
                    throw error;
                }
            }
        }

        return products;
    }


    /**
     * Return the unique identifier of a product, either by retrieving it form the open metadata
     * repository or by creating the product.
     *
     * @param productDefinition description of the product
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector
     * @return unique identifier
     * @throws InvalidParameterException an invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException the repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getProduct(ProductDefinition productDefinition,
                              String            subscriptionManagerGUID) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        CollectionClient             collectionClient             = integrationContext.getCollectionClient(OpenMetadataType.DIGITAL_PRODUCT.typeName);
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        final String methodName = "getProduct";

        /*
         * If the product is already defined, then extract its element.  This works for products and product families.
         */
        OpenMetadataRootElement productElement = classificationExplorerClient.getRootElementByUniqueName(productDefinition.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name, this.headerAndProperties(collectionClient));

        DigitalProductProperties digitalProductProperties = this.getDigitalProductProperties(productDefinition);

        if (productElement == null)
        {
            /*
             * First time, so the product needs to be created, along with its subscription options,
             * solution blueprint, data spec, notification type and asset.  The asset needs to be
             * registered as a CatalogTarget.
             */
            digitalProductProperties.setIntroductionDate(new Date());

            NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(true);
            newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);

            if (productDefinition.getFolder() != null)
            {
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentGUID(productFolders.get(productDefinition.getFolder().getQualifiedName()));
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            }

            String productGUID = collectionClient.createCollection(newElementOptions,
                                                                   this.getInitialClassificationProperties(productDefinition.zoneMembership()),
                                                                   digitalProductProperties,
                                                                   null);

            logRecord(methodName,
                      JacquardAuditCode.NEW_OPEN_METADATA_PRODUCT.getMessageDefinition(connectorName,
                                                                                       productGUID,
                                                                                       productDefinition.getProductName()));

            productElement = collectionClient.getCollectionByGUID(productGUID, this.headerAndProperties(collectionClient));
        }
        else
        {
            logRecord(methodName,
                      JacquardAuditCode.RETRIEVING_OPEN_METADATA_PRODUCT.getMessageDefinition(connectorName,
                                                                                              productElement.getElementHeader().getGUID(),
                                                                                              productDefinition.getProductName()));

            if (collectionClient.updateCollection(productElement.getElementHeader().getGUID(),
                                                  collectionClient.getUpdateOptions(true),
                                                  digitalProductProperties))
            {
                logRecord(methodName,
                          JacquardAuditCode.UPDATED_OPEN_METADATA_PRODUCT.getMessageDefinition(connectorName,
                                                                                              productElement.getElementHeader().getGUID(),
                                                                                              productDefinition.getProductName()));
            }
        }

        PersonRoleProperties personRoleProperties = new PersonRoleProperties();

        personRoleProperties.setQualifiedName(productDefinition.getQualifiedName() + "_productManager");
        personRoleProperties.setActorRoleGroups(List.of(ActorRoleGroup.DIGITAL_PRODUCT_MANAGER.getName()));
        personRoleProperties.setDisplayName("Product Manager for " + productDefinition.getProductName());
        personRoleProperties.setDescription(ProductRoleDefinition.PRODUCT_MANAGER.getDescription());

        OpenMetadataRootElement productManagerElement = classificationExplorerClient.getRootElementByUniqueName(personRoleProperties.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name, this.headerAndProperties(collectionClient));

        if (productManagerElement == null)
        {
            /*
             * Link the product manager - each product needs its own product manager.  This project manager is a member
             * of the appropriate community
             */
            ActorRoleClient actorRoleClient = integrationContext.getActorRoleClient();


            AssignmentScopeProperties assignmentScopeProperties = new AssignmentScopeProperties();

            assignmentScopeProperties.setAssignmentType(AssignmentType.OWNER.getDisplayName());
            assignmentScopeProperties.setDescription(AssignmentType.OWNER.getDescription());

            NewElementOptions roleOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());

            roleOptions.setIsOwnAnchor(false);
            roleOptions.setAnchorGUID(productElement.getElementHeader().getGUID());
            roleOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);

            roleOptions.setParentGUID(productElement.getElementHeader().getGUID());
            roleOptions.setParentAtEnd1(false);
            roleOptions.setParentRelationshipTypeName(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName);

            String productManagerGUID = actorRoleClient.createActorRole(roleOptions,
                                                                        null,
                                                                        personRoleProperties,
                                                                        assignmentScopeProperties);

            productManagerElement = classificationExplorerClient.getRootElementByGUID(productManagerGUID, this.headerAndProperties(collectionClient));

            if (productDefinition.getCommunity() != null)
            {
                assignmentScopeProperties.setAssignmentType(AssignmentType.DISCUSSION_LEADER.getDisplayName());
                assignmentScopeProperties.setDescription(AssignmentType.DISCUSSION_LEADER.getDescription());

                classificationExplorerClient.assignActorToElement(communities.get(productDefinition.getCommunity().getQualifiedName()),
                                                                  productManagerGUID,
                                                                  new MakeAnchorOptions(collectionClient.getMetadataSourceOptions()),
                                                                  assignmentScopeProperties);
            }
        }

        /*
         * Link in the license type to the product to show what type of license is granted to the subscriber.
         */
        String licenseTypeGUID = null;

        if (productDefinition.getLicense() != null)
        {
            licenseTypeGUID = governanceDefinitions.get(productDefinition.getLicense().getQualifiedName());

            GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

            GovernedByProperties governedByProperties = new GovernedByProperties();
            governedByProperties.setLabel("subscriber's license");
            governedByProperties.setDescription("This is the license that a subscriber's asset will be given to access the product data.");
            governanceDefinitionClient.addGovernanceDefinitionToElement(productElement.getElementHeader().getGUID(), licenseTypeGUID, new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions()), governedByProperties);
        }

        /*
         * Link the community to the product family if defined
         */
        if ((propertyHelper.isTypeOf(productElement.getElementHeader(), OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName)) && (productDefinition.getCommunity() != null))
        {
            classificationExplorerClient.addScopeToElement(communities.get(productDefinition.getCommunity().getQualifiedName()),
                                                           productElement.getElementHeader().getGUID(),
                                                           new MakeAnchorOptions(classificationExplorerClient.getMetadataSourceOptions()),
                                                           null);
        }

        /*
         * Extract the note log if there is a community for this product
         */
        String communityNoteLogGUID = null;

        if (productDefinition.getCommunity() != null)
        {
            communityNoteLogGUID = communityNoteLogs.get(productDefinition.getCommunity().getQualifiedName());
        }

        /*
         * The data specification lists all the data fields for this product.
         */
        this.addDataSpec(productDefinition, productElement);

        /*
         * The questions are used to guide people to the appropriate product.
         */
        this.addQuestions(productDefinition, productElement);

        /*
         * This asset has a connection to a connector that is able to mine open metadata to create a particular
         * product.  A product family's asset is a tabular data set collection whose connector walks the family
         * and presents each member's data set as a table - so a subscription to the family is delivered from
         * one source, in one pass, and picks up products added to the family later.
         */
        String productAssetGUID = this.addProductAsset(productDefinition, productElement.getElementHeader().getGUID());

        /*
         * The subscription options show up as governance action processes that are configured with the appropriate
         * information.
         */
        this.addSubscriptionTypes(productDefinition, productElement.getElementHeader(), productAssetGUID, licenseTypeGUID, communityNoteLogGUID, productManagerElement.getElementHeader().getGUID(), subscriptionManagerGUID);

        /*
         * Register each product as a catalog target, so it is refreshed.
         */
        /*
         * The product's asset is one of this connector's own catalog targets, so that its data is watched for
         * changes.  It is added once: a catalog target relationship persists with the asset, and adding one on
         * every refresh - as this used to - gave each product a new relationship per run, and the connector a
         * processor per relationship, until starting them all took longer than a refresh cycle.
         * A product family names no catalog target: its asset is a view over its members' assets, which are
         * catalog targets in their own right, so there is nothing of the family's own to watch.
         */
        if ((productAssetGUID != null) && (productDefinition.getCatalogTargetName() != null) && (! ownCatalogTargets.contains(productAssetGUID)))
        {
            AssetClient assetClient = integrationContext.getAssetClient();

            CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

            catalogTargetProperties.setCatalogTargetName(productDefinition.getCatalogTargetName());
            catalogTargetProperties.setConnectionName(productDefinition.getProductName());
            catalogTargetProperties.setPermittedSynchronization(PermittedSynchronization.BOTH_DIRECTIONS);
            catalogTargetProperties.setDeleteMethod(DeleteMethod.LOOK_FOR_LINEAGE);

            assetClient.addCatalogTarget(integrationContext.getIntegrationConnectorGUID(),
                                         productAssetGUID,
                                         new MakeAnchorOptions(assetClient.getMetadataSourceOptions()),
                                         catalogTargetProperties);

            ownCatalogTargets.add(productAssetGUID);
        }

        if (productDefinition.getProductFamilies() != null)
        {
            for (ProductDefinition productGroup : productDefinition.getProductFamilies())
            {
                String productFamilyGUID = products.get(productGroup.getQualifiedName());

                /*
                 * A family that could not be catalogued this refresh - it exists twice, say - has already been
                 * logged; its members are still catalogued and are linked to it once it is back.
                 */
                if (productFamilyGUID != null)
                {
                    CollectionMembershipProperties collectionMembershipProperties = new CollectionMembershipProperties();

                    collectionMembershipProperties.setMembershipType("includes product");

                    collectionClient.addToCollection(productFamilyGUID,
                                                     productElement.getElementHeader().getGUID(),
                                                     new MakeAnchorOptions(collectionClient.getMetadataSourceOptions()),
                                                     collectionMembershipProperties);
                }
            }

            this.monitorMemberAssetForFamilies(productDefinition, productAssetGUID);
        }

        return productElement.getElementHeader().getGUID();
    }


    /**
     * Set up a product's data spec.
     *
     * @param productDefinition description of product
     * @param productElement details of what is currently stored
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addDataSpec(ProductDefinition       productDefinition,
                             OpenMetadataRootElement productElement) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        List<ProductDataFieldDefinition> dataFieldIdentifiers = productDefinition.getDataSpecIdentifiers();
        List<ProductDataFieldDefinition> dataFieldDefinitions = productDefinition.getDataSpecFields();

        if ((dataFieldIdentifiers != null) || (dataFieldDefinitions != null))
        {
            ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

            NewElementOptions newElementOptions = new NewElementOptions(classificationExplorerClient.getMetadataSourceOptions());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);
            newElementOptions.setAnchorGUID(productElement.getElementHeader().getGUID());
            newElementOptions.setParentAtEnd1(true);

            DataSpecProperties dataSpecProperties = new DataSpecProperties();

            dataSpecProperties.setQualifiedName(productDefinition.getQualifiedName() + "_dataSpec");
            dataSpecProperties.setDisplayName("Data Specification for " + productDefinition.getDisplayName());
            dataSpecProperties.setDescription("The data specification lists the fields in the " + productDefinition.getProductName() + " product.");
            dataSpecProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());

            OpenMetadataRootElement dataSpecElement = classificationExplorerClient.getRootElementByUniqueName(dataSpecProperties.getQualifiedName(),
                                                                                                              OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                              this.headerAndProperties(classificationExplorerClient));
            if (dataSpecElement == null)
            {
                CollectionClient    collectionClient    = integrationContext.getCollectionClient(OpenMetadataType.DATA_SPEC_COLLECTION.typeName);


                newElementOptions.setParentGUID(productElement.getElementHeader().getGUID());
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.DATA_DESCRIPTION_RELATIONSHIP.typeName);

                DataDescriptionProperties dataDescriptionProperties = new DataDescriptionProperties();

                dataDescriptionProperties.setLabel("data-specification");
                dataDescriptionProperties.setDescription("Description of the data structure(s) used in this product.  Each data structure is a member of the data specification.");

                String dataSpecGUID = collectionClient.createCollection(newElementOptions,
                                                                        null,
                                                                        dataSpecProperties,
                                                                        dataDescriptionProperties);

                dataSpecElement = collectionClient.getCollectionByGUID(dataSpecGUID, this.headerAndProperties(collectionClient));
            }

            DataStructureClient dataStructureClient = integrationContext.getDataStructureClient();

            DataStructureProperties dataStructureProperties = new DataStructureProperties();

            dataStructureProperties.setQualifiedName(productDefinition.getQualifiedName() + "_dataSpec.dataStructure");
            dataStructureProperties.setDisplayName("Data Structure for " + productDefinition.getDisplayName());
            dataStructureProperties.setDescription("The data structure lists the fields in the " + productDefinition.getProductName() + " product.");
            dataStructureProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
            dataStructureProperties.setNamePatterns(Collections.singletonList(productDefinition.getDataSpecTableName()));

            OpenMetadataRootElement dataStructureElement = classificationExplorerClient.getRootElementByUniqueName(dataStructureProperties.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name, this.headerAndProperties(dataStructureClient));

            if (dataStructureElement == null)
            {
                newElementOptions.setParentGUID(dataSpecElement.getElementHeader().getGUID());
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

                String dataStructureGUID = dataStructureClient.createDataStructure(newElementOptions,
                                                                                   null,
                                                                                   dataStructureProperties,
                                                                                   null);

                int fieldPosition = 1;

                // todo - update to handle changes in the data spec

                if (dataFieldIdentifiers != null)
                {
                    for (ProductDataFieldDefinition dataField : dataFieldIdentifiers)
                    {
                        String dataFieldGUID = dataFields.get(dataField.getQualifiedName());

                        MemberDataFieldProperties memberDataFieldProperties = new MemberDataFieldProperties();

                        memberDataFieldProperties.setCoverageCategory(CoverageCategory.IDENTIFIER);
                        memberDataFieldProperties.setPosition(fieldPosition);
                        fieldPosition++;

                        dataStructureClient.linkMemberDataField(dataStructureGUID,
                                                                dataFieldGUID,
                                                                new MakeAnchorOptions(dataStructureClient.getMetadataSourceOptions()),
                                                                memberDataFieldProperties);
                    }
                }

                if (dataFieldDefinitions != null)
                {
                    for (ProductDataFieldDefinition dataField : dataFieldDefinitions)
                    {
                        String dataFieldGUID = dataFields.get(dataField.getQualifiedName());

                        MemberDataFieldProperties memberDataFieldProperties = new MemberDataFieldProperties();

                        memberDataFieldProperties.setCoverageCategory(CoverageCategory.CORE_DETAIL);
                        memberDataFieldProperties.setPosition(fieldPosition);
                        fieldPosition++;

                        dataStructureClient.linkMemberDataField(dataStructureGUID,
                                                                dataFieldGUID,
                                                                new MakeAnchorOptions(dataStructureClient.getMetadataSourceOptions()),
                                                                memberDataFieldProperties);
                    }
                }
            }
        }
    }


    /**
     * Set up a product's questions.
     *
     * @param productDefinition description of product
     * @param productElement details of what is currently stored
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addQuestions(ProductDefinition       productDefinition,
                              OpenMetadataRootElement productElement) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        List<ProductQuestionDefinition> productQuestionDefinitions = productDefinition.getQuestions();

        if (productQuestionDefinitions != null)
        {
            ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

            for (ProductQuestionDefinition productQuestionDefinition : productQuestionDefinitions)
            {
                String questionGUID = questions.get(productQuestionDefinition.getQualifiedName());

                SupplementaryPropertiesProperties supplementaryPropertiesProperties = new SupplementaryPropertiesProperties();

                supplementaryPropertiesProperties.setLabel("Guiding question");
                supplementaryPropertiesProperties.setDescription("This is the type of question that " + productDefinition.getProductName() + " is designed to answer.");

                classificationExplorerClient.addSupplementaryPropertiesToElement(productElement.getElementHeader().getGUID(),
                                                                                 questionGUID,
                                                                                 classificationExplorerClient.getMakeAnchorOptions(false),
                                                                                 supplementaryPropertiesProperties);
            }
        }
    }


    /**
     * Create a DigitalProductProperties object from a ProductDefinition.
     *
     * @param productDefinition product definition
     * @return DigitalProductProperties object
     */
    private DigitalProductProperties getDigitalProductProperties(ProductDefinition productDefinition)
    {
        DigitalProductProperties digitalProductProperties = new DigitalProductProperties();

        digitalProductProperties.setTypeName(productDefinition.getTypeName()); // maybe family or product
        digitalProductProperties.setQualifiedName(productDefinition.getQualifiedName());
        digitalProductProperties.setDisplayName(productDefinition.getDisplayName());
        digitalProductProperties.setDescription(productDefinition.getDescription());
        digitalProductProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
        digitalProductProperties.setCategory(productDefinition.getCategory());
        digitalProductProperties.setProductName(productDefinition.getProductName());

        /*
         * Some digital products are not in active status but are in development.
         * Assume all digital product families are active and
         * digital products that have a connector provider are active.
         */
        if ((! productDefinition.getTypeName().equals(OpenMetadataType.DIGITAL_PRODUCT.typeName)) ||
                (productDefinition.getConnectorProvider() != null))
        {
            digitalProductProperties.setContentStatus(ContentStatus.ACTIVE);
            digitalProductProperties.setDeploymentStatus(DeploymentStatus.ACTIVE);
        }
        else
        {
            digitalProductProperties.setContentStatus(ContentStatus.APPROVED);
            digitalProductProperties.setDeploymentStatus(DeploymentStatus.UNDER_DEVELOPMENT);
        }

        return digitalProductProperties;
    }


    /**
     * Hand a notification type to the Baudot Subscription Manager, so that it monitors it and notifies its
     * subscribers.
     * <br>
     * Where the manager is not running yet the notification type is held until it is - the manager is started
     * by this connector, and the products are built either side of that.
     *
     * @param notificationTypeGUID the notification type to be monitored
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void registerWithSubscriptionManager(String notificationTypeGUID,
                                                 String subscriptionManagerGUID) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        if ((subscriptionManagerGUID != null) && (notificationTypeGUID != null) && (! subscriptionManagerCatalogTargets.contains(notificationTypeGUID)))
        {
            this.attachNotificationType(subscriptionManagerGUID, notificationTypeGUID);
        }
    }


    /**
     * Hand one notification type to the subscription manager as a catalog target, so that the manager
     * notifies its subscribers.  The manager notices new catalog targets on its next refresh.
     *
     * @param subscriptionManagerGUID the subscription manager integration connector
     * @param notificationTypeGUID the notification type to be looked after
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void attachNotificationType(String subscriptionManagerGUID,
                                        String notificationTypeGUID) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        AssetClient assetClient = integrationContext.getAssetClient();

        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();

        catalogTargetProperties.setCatalogTargetName(BaudotCatalogTarget.NOTIFICATION_TYPE.getName());
        catalogTargetProperties.setPermittedSynchronization(PermittedSynchronization.BOTH_DIRECTIONS);

        assetClient.addCatalogTarget(subscriptionManagerGUID,
                                     notificationTypeGUID,
                                     assetClient.getMakeAnchorOptions(false),
                                     catalogTargetProperties);

        subscriptionManagerCatalogTargets.add(notificationTypeGUID);
    }


    /**
     * Return the unique identifier of the subscription manager from this connector's configuration properties.
     *
     * @return string guid, or null if it is not configured
     */
    private String getSubscriptionManagerGUID()
    {
        Map<String, Object> configurationProperties = connectionBean.getConfigurationProperties();

        if ((configurationProperties != null) &&
                (configurationProperties.get(JacquardConfigurationProperty.SUBSCRIPTION_MANAGER_GUID.getName()) != null))
        {
            return configurationProperties.get(JacquardConfigurationProperty.SUBSCRIPTION_MANAGER_GUID.getName()).toString();
        }

        return null;
    }


    /**
     * Options for reading an element whose header and properties are all this connector will use - which is
     * every element it looks up by name or GUID while building the catalogue, bar the one that reads
     * specification properties (see {@link #withRelationships}).  With the default options the client assembles
     * the element's whole graph to the default depth, one related-elements call per element per level, and a
     * product's graph includes its notification types and everything ever attached to them; verifying 126
     * products cost 7,200 such calls.
     *
     * @param client the client the options are for
     * @return get options with no related elements
     */
    private GetOptions headerAndProperties(ConnectorContextClientBase client)
    {
        GetOptions getOptions = client.getGetOptions();

        getOptions.setGraphQueryDepth(0);

        return getOptions;
    }


    /**
     * Query options for a related-elements query whose results' header and properties are all that will be used.
     *
     * @param client the client the options are for
     * @return query options with no further related elements
     */
    private QueryOptions headerAndPropertiesQuery(ConnectorContextClientBase client)
    {
        QueryOptions queryOptions = client.getQueryOptions();

        queryOptions.setGraphQueryDepth(0);

        return queryOptions;
    }


    /**
     * Options for reading an element together with the elements one hop away over the named relationship
     * types, and nothing else.
     *
     * @param client the client the options are for
     * @param relationshipTypeNames the relationships to follow
     * @return get options
     */
    private GetOptions withRelationships(ConnectorContextClientBase client,
                                         String...                  relationshipTypeNames)
    {
        GetOptions getOptions = client.getGetOptions();

        getOptions.setGraphQueryDepth(1);
        getOptions.setIncludeOnlyRelationships(List.of(relationshipTypeNames));

        return getOptions;
    }


    /**
     * Load this connector's own catalog targets - the product assets whose data it watches - so that a product
     * is not added as a target again on every refresh, and remove the duplicate relationships that earlier
     * versions of this connector left behind by doing exactly that.  Each duplicate was another processor to
     * start on every refresh, for the same product; on a catalogue refreshed daily for a fortnight, starting
     * them all took longer than the refresh cycle.  The first relationship found for each asset is kept.
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void loadOwnCatalogTargets() throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException
    {
        final String methodName = "loadOwnCatalogTargets";

        AssetClient assetClient = integrationContext.getAssetClient();

        ownCatalogTargets.clear();

        int                           duplicatesRemoved = 0;
        int                           startFrom         = 0;
        List<OpenMetadataRootElement> catalogTargets    = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                        assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));

        while ((catalogTargets != null) && (! catalogTargets.isEmpty()))
        {
            for (OpenMetadataRootElement catalogTarget : catalogTargets)
            {
                if ((catalogTarget != null) && (catalogTarget.getElementHeader() != null))
                {
                    String elementGUID = catalogTarget.getElementHeader().getGUID();

                    if (ownCatalogTargets.add(elementGUID))
                    {
                        continue;
                    }

                    /*
                     * Already seen - this relationship is a duplicate of one kept above.
                     */
                    if ((catalogTarget.getRelatedBy() != null) && (catalogTarget.getRelatedBy().getRelationshipHeader() != null))
                    {
                        assetClient.removeCatalogTarget(catalogTarget.getRelatedBy().getRelationshipHeader().getGUID(),
                                                        assetClient.getDeleteOptions(false));
                        duplicatesRemoved++;
                    }
                }
            }

            startFrom      = startFrom + assetClient.getMaxPagingSize();
            catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                           assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));
        }

        if (duplicatesRemoved > 0)
        {
            logRecord(methodName,
                      JacquardAuditCode.DUPLICATE_CATALOG_TARGETS_REMOVED.getMessageDefinition(connectorName,
                                                                                               Integer.toString(duplicatesRemoved),
                                                                                               Integer.toString(ownCatalogTargets.size())));
        }
    }


    /**
     * Load the notification types that the subscription manager already looks after, so that this connector
     * does not hand it the same one twice.  On a catalogue that is already built - the normal case after the
     * first run - this is every notification type there is.
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void loadSubscriptionManagerCatalogTargets() throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException
    {
        AssetClient assetClient = integrationContext.getAssetClient();

        subscriptionManagerCatalogTargets.clear();

        /*
         * Each element returned is the catalog target itself - here, a notification type.
         */
        int                           startFrom      = 0;
        List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(subscriptionManagerGUID,
                                                                                     assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));

        while ((catalogTargets != null) && (! catalogTargets.isEmpty()))
        {
            for (OpenMetadataRootElement catalogTarget : catalogTargets)
            {
                if ((catalogTarget != null) && (catalogTarget.getElementHeader() != null))
                {
                    subscriptionManagerCatalogTargets.add(catalogTarget.getElementHeader().getGUID());
                }
            }

            startFrom      = startFrom + assetClient.getMaxPagingSize();
            catalogTargets = assetClient.getCatalogTargets(subscriptionManagerGUID,
                                                           assetClient.getQueryOptions(startFrom, assetClient.getMaxPagingSize()));
        }
    }



    /**
     * Set up a product's subscription types.  These are governance types configured with an appropriate
     * subscription behaviour.  A customized governance action process for creating a subscription is also set up.
     * When this governance action process runs, it creates the subscription for the requesting
     * actor by linking them to the notification type.
     * <br>
     * Product families offer their subscription types in exactly the same way as a single product, because
     * subscribing to a family is how a consumer takes out one subscription covering every product in it.  A
     * family's asset is a tabular data set collection over its members' data sets, so its subscription options
     * name a source like any product's; what differs is what is watched for changes.  The family's asset never
     * changes itself, so its notification type is linked to each member's asset as that member is catalogued -
     * see {@link #monitorMemberAssetForFamilies}.
     * <br>
     * A product that is not a family and has no asset offers nothing, because there would be nothing to
     * deliver.  Products in that state are ones whose connector has not been written yet: the definition
     * describes the data the product would carry but names no connector provider to produce it.
     *
     * @param productDefinition description of product
     * @param productHeader unique identifier and type for the product
     * @param productAssetGUID unique identifier for the asset that represents the product - for a family, the
     *                         collection over its members' data sets - or null if the product has no asset
     * @param licenseTypeGUID unique identifier for the license type granted to the product subscribers
     * @param communityNoteLogGUID unique identifier of the community's note log
     * @param productManagerGUID unique identifier for the product manager
     * @param subscriptionManagerGUID unique identifier of the subscription manager integration connector
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addSubscriptionTypes(ProductDefinition productDefinition,
                                      ElementHeader     productHeader,
                                      String            productAssetGUID,
                                      String            licenseTypeGUID,
                                      String            communityNoteLogGUID,
                                      String            productManagerGUID,
                                      String            subscriptionManagerGUID) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        /*
         * A product with no asset has nothing to deliver, so offering a subscription to it would promise a
         * delivery that cannot happen.  That is the state of every product whose connector has not been written
         * yet - the definition describes the data and names no connector provider to produce it.  A family
         * always has an asset - the collection over its members - so it always offers its subscriptions.
         */
        if (productAssetGUID == null)
        {
            return;
        }

        if (productDefinition.getSubscriptionTypes() != null)
        {
            for (ProductSubscriptionDefinition productSubscriptionDefinition : productDefinition.getSubscriptionTypes())
            {
                String notificationTypeGUID = addNotificationType(productSubscriptionDefinition,
                                                                  productHeader,
                                                                  productDefinition.getProductName(),
                                                                  productAssetGUID,
                                                                  communityNoteLogGUID,
                                                                  productManagerGUID,
                                                                  subscriptionManagerGUID);

                addSubscriptionGovernanceActionProcess(productDefinition.getProductName(),
                                                       productDefinition.getIdentifier(),
                                                       productHeader.getGUID(),
                                                       productAssetGUID,
                                                       licenseTypeGUID,
                                                       notificationTypeGUID,
                                                       productSubscriptionDefinition,
                                                       productManagerGUID);
            }
        }
    }


    /**
     * Set up a digital product's notification type.  Each are governance action processes configured with an appropriate
     * subscription template.  When the governance action process runs, it creates the subscription for the requesting
     * actor.
     *
     * @param productSubscriptionDefinition description of the subscription type that is supported by the product
     * @param productHeader                 unique identifier and type for the product
     * @param productName                   name of the product
     * @param productAssetGUID              unique identifier for the asset that represents the product
     * @param communityNoteLogGUID          unique identifier of the community's note log
     * @param productManagerGUID            unique identifier for the product manager
     * @param subscriptionManagerGUID        unique identifier of the subscription manager integration connector
     * @return guid
     * @throws InvalidParameterException  an invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException    the repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     *                                    been disconnected.
     */
    private String addNotificationType(ProductSubscriptionDefinition productSubscriptionDefinition,
                                       ElementHeader                 productHeader,
                                       String                        productName,
                                       String                        productAssetGUID,
                                       String                        communityNoteLogGUID,
                                       String                        productManagerGUID,
                                       String                        subscriptionManagerGUID) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        /*
         * The notification of changes to a subscription is managed via a notification type.
         */
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();
        GovernanceDefinitionClient   notificationTypeClient       = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.NOTIFICATION_TYPE.typeName);
        MakeAnchorOptions           makeAnchorOptions            = new MakeAnchorOptions(notificationTypeClient.getMetadataSourceOptions());

        NotificationTypeProperties notificationTypeProperties = getNotificationTypeProperties(productSubscriptionDefinition, productHeader, productName);

        OpenMetadataRootElement notificationTypeElement = classificationExplorerClient.getRootElementByUniqueName(notificationTypeProperties.getQualifiedName(), OpenMetadataProperty.QUALIFIED_NAME.name, this.headerAndProperties(classificationExplorerClient));

        if (notificationTypeElement == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(notificationTypeClient.getMetadataSourceOptions());

            newElementOptions.setAnchorGUID(productHeader.getGUID());
            newElementOptions.setAnchorScopeGUIDs(Collections.singletonList(productHeader.getGUID()));
            newElementOptions.setIsOwnAnchor(false);

            String notificationTypeGUID = notificationTypeClient.createGovernanceDefinition(newElementOptions,
                                                                                            null,
                                                                                            notificationTypeProperties,
                                                                                            null);

            if ((productAssetGUID != null)
                        && (productSubscriptionDefinition.getMultipleNotificationsPermitted())
                        && (productSubscriptionDefinition.isAddMonitoredResource())
                        && (! propertyHelper.isTypeOf(productHeader, OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName)))
            {
                /*
                 * Only need to register the resource with notification types that use changes to the resource to determine
                 * when to issue a notification to the subscribers.
                 *
                 * A product family's asset is a view over its members' assets and never changes itself, so it is
                 * not what the notification type watches.  Its members' assets are linked to this notification
                 * type as each member is catalogued - see monitorMemberAssetForFamilies().
                 */
                MonitoredResourceProperties monitoredResourceProperties = new MonitoredResourceProperties();

                monitoredResourceProperties.setLabel("product asset");
                monitoredResourceProperties.setDescription("This is the product asset that represents the data for the " + productName + " product.");

                notificationTypeClient.linkMonitoredResource(notificationTypeGUID, productAssetGUID, makeAnchorOptions, monitoredResourceProperties);
            }

            NotificationSubscriberProperties notificationSubscriberProperties = new NotificationSubscriberProperties();
            notificationSubscriberProperties.setActivityStatus(ActivityStatus.IN_PROGRESS);

            /*
             * Only link note log to leaf products.
             */
            if ((communityNoteLogGUID != null) && (propertyHelper.isTypeOf(productHeader, OpenMetadataType.DIGITAL_PRODUCT.typeName)))
            {
                notificationSubscriberProperties.setLabel("community notifications");
                notificationSubscriberProperties.setDescription("A note log collects the notifications from the Baudot Subscription Manager based on activity around notification type: " + notificationTypeGUID);

                notificationTypeClient.linkNotificationSubscriber(notificationTypeGUID, communityNoteLogGUID, makeAnchorOptions, notificationSubscriberProperties);
            }

            /*
             * Every product has a product manager.  They receive notifications to enable monitoring of product activity.
             */
            if (productManagerGUID != null)
            {
                notificationSubscriberProperties.setLabel("product manager notifications");
                notificationSubscriberProperties.setDescription("Notifications from the Baudot Subscription Manager related to notification type: " + notificationTypeGUID + " are sent to the product manager.");

                notificationTypeClient.linkNotificationSubscriber(notificationTypeGUID, productManagerGUID, makeAnchorOptions, notificationSubscriberProperties);
            }

            this.registerWithSubscriptionManager(notificationTypeGUID, subscriptionManagerGUID);

            return notificationTypeGUID;
        }
        else
        {
            /*
             * The notification type is already catalogued, and it is still offered to the subscription
             * manager.  Its catalog targets persist with it, so on a catalogue that is already built this is
             * normally a no-op - the manager already has it - but a notification type that was created while
             * no manager was configured, or whose catalog target was removed, is picked up here rather than
             * quietly leaving every subscription to that product undelivered.
             */
            String notificationTypeGUID = notificationTypeElement.getElementHeader().getGUID();

            /*
             * An existing notification type is checked against its definition and brought up to date with a
             * merge, so that nothing else about it is touched.  Two things drift:
             *
             *   - A notification type catalogued before content status was part of a notification type has
             *     none, and the notification manager sends nothing for a notification type that is not ACTIVE.
             *     A product whose notification types are silent has subscriptions that are taken out and never
             *     delivered.
             *   - The notification pattern - whether more than one notification is permitted, and how far apart
             *     they must be - comes from the subscription definition, and the definition is the source of
             *     truth.  A notification type catalogued under an earlier definition keeps the old pattern
             *     otherwise: an evaluation subscription that is meant to deliver once, but was catalogued as
             *     periodic with no minimum interval, has its data delivered again on every refresh of the
             *     subscription manager, and each delivery after the first fails on the rows already there.
             */
            if ((notificationTypeElement.getProperties() instanceof NotificationTypeProperties existingProperties) &&
                    ((existingProperties.getContentStatus() != ContentStatus.ACTIVE) ||
                     (existingProperties.getMultipleNotificationsPermitted() != notificationTypeProperties.getMultipleNotificationsPermitted()) ||
                     (existingProperties.getMinimumNotificationInterval() != notificationTypeProperties.getMinimumNotificationInterval())))
            {
                NotificationTypeProperties updatedProperties = new NotificationTypeProperties();

                updatedProperties.setContentStatus(ContentStatus.ACTIVE);
                updatedProperties.setMultipleNotificationsPermitted(notificationTypeProperties.getMultipleNotificationsPermitted());
                updatedProperties.setMinimumNotificationInterval(notificationTypeProperties.getMinimumNotificationInterval());

                notificationTypeClient.updateGovernanceDefinition(notificationTypeGUID,
                                                                  notificationTypeClient.getUpdateOptions(true),
                                                                  updatedProperties);

                logRecord("addNotificationType",
                          JacquardAuditCode.UPDATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                               OpenMetadataType.NOTIFICATION_TYPE.typeName,
                                                                                               existingProperties.getDisplayName(),
                                                                                               notificationTypeGUID));
            }

            this.registerWithSubscriptionManager(notificationTypeGUID, subscriptionManagerGUID);

            return notificationTypeGUID;
        }
    }


    /**
     * Extract the properties for a notification type from a subscription definition.
     *
     * @param productSubscriptionDefinition description of the subscription type that is supported by the product
     * @param productHeader                 unique identifier and type for the product
     * @param productName                   name of the product
     * @return properties
     */
    private static NotificationTypeProperties getNotificationTypeProperties(ProductSubscriptionDefinition productSubscriptionDefinition,
                                                                            ElementHeader                 productHeader,
                                                                            String                        productName)
    {
        NotificationTypeProperties notificationTypeProperties = new NotificationTypeProperties();

        notificationTypeProperties.setQualifiedName(OpenMetadataType.NOTIFICATION_TYPE.typeName + "::" + productHeader.getGUID() + "::" + productName + "::" + productSubscriptionDefinition.getIdentifier());
        notificationTypeProperties.setIdentifier(productSubscriptionDefinition.getIdentifier());
        notificationTypeProperties.setDisplayName("Notification type for " + productSubscriptionDefinition.getDisplayName() + " for product " + productName);
        notificationTypeProperties.setDescription(productSubscriptionDefinition.getDescription());
        notificationTypeProperties.setDomainIdentifier(GovernanceDomain.DATA_SHARING.getOrdinal());
        notificationTypeProperties.setPlannedStartDate(new Date());
        notificationTypeProperties.setMultipleNotificationsPermitted(productSubscriptionDefinition.getMultipleNotificationsPermitted());
        notificationTypeProperties.setMinimumNotificationInterval(productSubscriptionDefinition.getMinimumNotificationInterval());

        /*
         * A notification type only notifies while it is ACTIVE: the notification manager refuses to send anything
         * for a notification type in any other content status, and it treats no status as not active.  These
         * notification types are live from the moment they are created, so they say so.
         */
        notificationTypeProperties.setContentStatus(ContentStatus.ACTIVE);

        return notificationTypeProperties;
    }


    /**
     * Set up a product's subscription types.  These are governance action processes configured with an appropriate
     * subscription template.  When the governance action process runs, it creates the subscription for the requesting
     * actor.
     *
     * @param productName name of product
     * @param productIdentifier identifier of the product
     * @param productGUID unique identifier of the product
     * @param productAssetGUID unique identifier of the product's asset
     * @param licenseTypeGUID unique identifier of the license type supported to this product
     * @param notificationTypeGUID unique identifier of the notification type driving the subscription (optional)
     * @param productSubscriptionDefinition details of the subscription type
     * @param productManagerGUID unique identifier for the product manager
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void addSubscriptionGovernanceActionProcess(String                        productName,
                                                        String                        productIdentifier,
                                                        String                        productGUID,
                                                        String                        productAssetGUID,
                                                        String                        licenseTypeGUID,
                                                        String                        notificationTypeGUID,
                                                        ProductSubscriptionDefinition productSubscriptionDefinition,
                                                        String                        productManagerGUID) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        String processQualifiedName = OpenMetadataType.PROVISIONING_ACTION_PROCESS.typeName + "::" + productName + "::" + ResourceUse.CREATE_SUBSCRIPTION.getResourceUse() + "::" + productSubscriptionDefinition.getIdentifier();

        OpenMetadataRootElement subscriptionGovernanceActionProcessElement = classificationExplorerClient.getRootElementByUniqueName(processQualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, this.headerAndProperties(classificationExplorerClient));

        if (subscriptionGovernanceActionProcessElement == null)
        {
            /*
             * The subscription notification watchdog manager is running.  Now build the governance action process
             * used to add a new subscription of this type.
             */
            String subscriptionName = productSubscriptionDefinition.getDisplayName() + " for " + productName;
            Map<String, String> additionalRequestParameters = new HashMap<>();

            additionalRequestParameters.put(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_NAME.getName(), subscriptionName);
            additionalRequestParameters.put(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_IDENTIFIER.getName(), productSubscriptionDefinition.getIdentifier() + "-" + productIdentifier);
            additionalRequestParameters.put(ManageDigitalSubscriptionRequestParameter.SUBSCRIPTION_DESCRIPTION.getName(), productSubscriptionDefinition.getDescription());

            String governanceActionProcessGUID = integrationContext.createProcessFromGovernanceActionType(OpenMetadataType.SUBSCRIBING_ACTION_PROCESS.typeName,
                                                                                                          processQualifiedName,
                                                                                                          "Create " + subscriptionName,
                                                                                                          productSubscriptionDefinition.getDescription() + "  Supply the requester (actor entity) as an action target called digitalSubscriptionRequester and the asset where the data is to be sent to as action target named destinationDataSet.",
                                                                                                          GovernanceDomain.DATA_SHARING.getOrdinal(),
                                                                                                          GovernanceActionTypeDefinition.CREATE_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                                                                                                          additionalRequestParameters,
                                                                                                          productGUID,
                                                                                                          Collections.singletonList(productGUID));

            OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();
            List<String>      actionTargetNames = new ArrayList<>();

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                           governanceActionProcessGUID,
                                                           productGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getName()));
            actionTargetNames.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_ITEM.getName());

            /*
             * The source is the product's asset.  For a product family that is the tabular data set collection
             * over its members' data sets, so a subscription to the family is provisioned from one source like
             * any other.  A product that has no asset yet has no source to name.
             */
            if (productAssetGUID != null)
            {
                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                               governanceActionProcessGUID,
                                                               productAssetGUID,
                                                               null,
                                                               null,
                                                               propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getName()));
                actionTargetNames.add(ManageDigitalSubscriptionActionTarget.DIGITAL_SUBSCRIPTION_SOURCE.getName());
            }

            if (licenseTypeGUID != null)
            {
                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                               governanceActionProcessGUID,
                                                               licenseTypeGUID,
                                                               null,
                                                               null,
                                                               propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getName()));
            }
            actionTargetNames.add(ManageDigitalSubscriptionActionTarget.LICENSE_TYPE.getName()); // always remove


            if (notificationTypeGUID != null)
            {
                openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                               governanceActionProcessGUID,
                                                               notificationTypeGUID,
                                                               null,
                                                               null,
                                                               propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.NOTIFICATION_TYPE.getName()));
            }
            actionTargetNames.add(ManageDigitalSubscriptionActionTarget.NOTIFICATION_TYPE.getName()); // always remove

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                           governanceActionProcessGUID,
                                                           productManagerGUID,
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName()));
            actionTargetNames.add(ManageDigitalSubscriptionActionTarget.DIGITAL_PRODUCT_OWNER.getName());

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                           governanceActionProcessGUID,
                                                           governanceDefinitions.get(productSubscriptionDefinition.getServiceLevelObjective().getQualifiedName()),
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.SERVICE_LEVEL_OBJECTIVE.getName()));
            actionTargetNames.add(ManageDigitalSubscriptionActionTarget.SERVICE_LEVEL_OBJECTIVE.getName());

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                           governanceActionProcessGUID,
                                                           GovernanceActionTypeDefinition.PROVISION_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName()));
            actionTargetNames.add(ManageDigitalSubscriptionActionTarget.PROVISIONING_ACTION_TYPE.getName());

            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.TARGET_FOR_GOVERNANCE_ACTION_RELATIONSHIP.typeName,
                                                           governanceActionProcessGUID,
                                                           GovernanceActionTypeDefinition.CANCEL_SUBSCRIPTION.getGovernanceActionTypeGUID(),
                                                           null,
                                                           null,
                                                           propertyHelper.addStringProperty(null, OpenMetadataProperty.ACTION_TARGET_NAME.name, ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName()));
            actionTargetNames.add(ManageDigitalSubscriptionActionTarget.CANCELLING_ACTION_TYPE.getName());

            /*
             * Remove the specification properties for the action targets that have already been supplied.  This means
             * that the remaining specification properties cover the ones that the caller needs to supply.
             */
            GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.GOVERNANCE_ACTION_PROCESS.typeName);

            OpenMetadataRootElement governanceActionProcess = governanceDefinitionClient.getGovernanceDefinitionByGUID(governanceActionProcessGUID, this.withRelationships(governanceDefinitionClient, OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName));

            if (governanceActionProcess.getSpecificationProperties() != null)
            {
                for (RelatedMetadataElementSummary specificationProperties : governanceActionProcess.getSpecificationProperties())
                {
                    if ((specificationProperties != null) && (specificationProperties.getRelationshipProperties() instanceof SpecificationPropertyAssignmentProperties specificationPropertyAssignmentProperties))
                    {
                        if (SpecificationPropertyType.SUPPORTED_ACTION_TARGET.getPropertyType().equals(specificationPropertyAssignmentProperties.getPropertyName()))
                        {
                            if (specificationProperties.getRelatedElement().getProperties() instanceof SpecificationPropertyValueProperties specificationPropertyValueProperties)
                            {
                                if (actionTargetNames.contains(specificationPropertyValueProperties.getPreferredValue()))
                                {
                                    openMetadataStore.deleteRelationshipInStore(specificationProperties.getRelationshipHeader().getGUID());
                                }
                            }
                        }
                        else if (SpecificationPropertyType.SUPPORTED_REQUEST_PARAMETER.getPropertyType().equals(specificationPropertyAssignmentProperties.getPropertyName()))
                        {
                            if (specificationProperties.getRelatedElement().getProperties() instanceof SpecificationPropertyValueProperties specificationPropertyValueProperties)
                            {
                                if (additionalRequestParameters.containsKey(specificationPropertyValueProperties.getPreferredValue()))
                                {
                                    openMetadataStore.deleteRelationshipInStore(specificationProperties.getRelationshipHeader().getGUID());
                                }
                            }
                        }
                    }
                }
            }

            /*
             * Link the new governance action process to the product.
             */
            ResourceListProperties resourceListProperties = new ResourceListProperties();

            resourceListProperties.setResourceUse(ResourceUse.CREATE_SUBSCRIPTION.getResourceUse());
            resourceListProperties.setDescription(ResourceUse.CREATE_SUBSCRIPTION.getDescription());

            classificationExplorerClient.addResourceListToElement(productGUID,
                                                                  governanceActionProcessGUID,
                                                                  classificationExplorerClient.getMakeAnchorOptions(false),
                                                                  resourceListProperties);

            /*
             * Link the new governance action process to the glossary term for more explanation.
             */
            if (productSubscriptionDefinition.getGlossaryTerm() != null)
            {
                String glossaryTermGUID = glossaryTerms.get(productSubscriptionDefinition.getGlossaryTerm().getQualifiedName());
                if (glossaryTermGUID != null)
                {
                    classificationExplorerClient.setupSemanticAssignment(governanceActionProcessGUID,
                                                                         glossaryTermGUID,
                                                                         null,
                                                                         classificationExplorerClient.getMakeAnchorOptions(false));
                }
            }
        }
    }


    /**
     * Link one product's asset to the notification types of every family it belongs to, so that a family's
     * subscribers hear about a change to any product in the family.
     * <br>
     * A family has no asset of its own, so this is the only way its notification types acquire anything to
     * watch.  It is done as each member is catalogued rather than when the family is built, because a family
     * is built before the products that belong to it - which is the same ordering the membership above
     * already relies on.
     * <br>
     * Only the notification types that decide when to notify by watching a resource are linked.  The ones that
     * notify on a fixed interval do not look at a resource at all, so giving them one would say something
     * about the notification that is not true.
     *
     * @param productDefinition description of the member product
     * @param productAssetGUID the member's asset, or null if it does not have one
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private void monitorMemberAssetForFamilies(ProductDefinition productDefinition,
                                               String            productAssetGUID) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        if (productAssetGUID == null)
        {
            return;
        }

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();
        GovernanceDefinitionClient   notificationTypeClient       = integrationContext.getGovernanceDefinitionClient(OpenMetadataType.NOTIFICATION_TYPE.typeName);

        for (ProductDefinition productFamily : productDefinition.getProductFamilies())
        {
            String productFamilyGUID = products.get(productFamily.getQualifiedName());

            if ((productFamilyGUID == null) || (productFamily.getSubscriptionTypes() == null))
            {
                continue;
            }

            for (ProductSubscriptionDefinition productSubscriptionDefinition : productFamily.getSubscriptionTypes())
            {
                if (! productSubscriptionDefinition.getMultipleNotificationsPermitted())
                {
                    continue;
                }

                String notificationTypeQualifiedName = OpenMetadataType.NOTIFICATION_TYPE.typeName + "::" + productFamilyGUID
                                                             + "::" + productFamily.getProductName() + "::"
                                                             + productSubscriptionDefinition.getIdentifier();

                OpenMetadataRootElement notificationTypeElement = classificationExplorerClient.getRootElementByUniqueName(notificationTypeQualifiedName,
                                                                                                                          OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                          this.headerAndProperties(classificationExplorerClient));

                if ((notificationTypeElement != null) && (productSubscriptionDefinition.isAddMonitoredResource()))
                {
                    MonitoredResourceProperties monitoredResourceProperties = new MonitoredResourceProperties();

                    monitoredResourceProperties.setLabel("product asset");
                    monitoredResourceProperties.setDescription("This is the product asset that represents the data for the "
                                                                       + productDefinition.getProductName() + " product, which is part of the "
                                                                       + productFamily.getProductName() + " product family.");

                    notificationTypeClient.linkMonitoredResource(notificationTypeElement.getElementHeader().getGUID(),
                                                                  productAssetGUID,
                                                                  new MakeAnchorOptions(notificationTypeClient.getMetadataSourceOptions()),
                                                                  monitoredResourceProperties);
                }
            }
        }
    }


    /**
     * Set up a product's data spec.
     *
     * @param productDefinition description of product
     * @param productGUID unique identifier of the product
     *
     * @throws InvalidParameterException an invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException the repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String addProductAsset(ProductDefinition productDefinition,
                                   String            productGUID) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "addProductAsset";

        if (productDefinition.getConnectorProvider() != null)
        {
            // todo support updates to the asset
            AssetClient                  assetClient                  = integrationContext.getAssetClient(productDefinition.getAssetTypeName());
            ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

            String qualifiedName = productDefinition.getQualifiedName() + "_" + productDefinition.getAssetIdentifier();

            OpenMetadataRootElement assetElement = classificationExplorerClient.getRootElementByUniqueName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, this.headerAndProperties(classificationExplorerClient));

            String assetGUID;

            if (assetElement == null)
            {
                TabularDataSetProperties dataSetProperties = new TabularDataSetProperties();

                if (productDefinition.getAssetTypeName() != null)
                {
                    dataSetProperties.setTypeName(productDefinition.getAssetTypeName());
                }

                dataSetProperties.setQualifiedName(qualifiedName);
                dataSetProperties.setDisplayName(productDefinition.getAssetIdentifier() + " for " + productDefinition.getDisplayName());
                dataSetProperties.setDescription("This asset represents the source of data for the digital product.");
                dataSetProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());

                NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(productGUID);
                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentGUID(productGUID);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);

                CollectionMembershipProperties collectionMembershipProperties = new CollectionMembershipProperties();
                collectionMembershipProperties.setMembershipType("product data set");

                assetGUID = assetClient.createAsset(newElementOptions,
                                                    null,
                                                    dataSetProperties,
                                                    collectionMembershipProperties);

                /*
                 * Add the "productized" keyword on the asset to identify assets that have been elevated to products.
                 */
                SearchKeywordClient searchKeywordClient = integrationContext.getSearchKeywordClient();

                SearchKeywordProperties searchKeywordProperties = new SearchKeywordProperties();
                searchKeywordProperties.setDisplayName("productized");
                searchKeywordProperties.setDescription("Asset " + assetGUID + " is a part of product " + productGUID + ".");

                searchKeywordClient.addSearchKeywordToElement(assetGUID, searchKeywordClient.getMetadataSourceOptions(), null, searchKeywordProperties);

                /*
                 * Log new product
                 */
                logRecord(methodName,
                          JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                               dataSetProperties.getTypeName(),
                                                                                               dataSetProperties.getDisplayName(),
                                                                                               assetGUID));
            }
            else
            {
                assetGUID = assetElement.getElementHeader().getGUID();
            }

            /*
             * The connection is dealt with whether the asset was just created or was already there.  The
             * original assumption was that an asset which exists has a connection, because the two are created
             * together - but they are created by two calls, and anything that goes wrong between them leaves an
             * asset that can never acquire one: every later pass finds the asset, skips the block that would
             * have built the connection, and moves on.  A product data set without a connection cannot be read,
             * so the product is silently useless.
             */
            this.addProductAssetConnection(productDefinition, productGUID, assetGUID, qualifiedName);

            return assetGUID;
        }

        return null;
    }


    /**
     * Make sure the product's asset has a connection, and that the connection still describes the metadata
     * access server this connector is talking to.
     * <br>
     * The connector type and the endpoint are both checked rather than assumed.  A product catalog outlives the
     * deployment it was built in: the platform can move to a different URL, and a connector provider can be
     * renamed or replaced between releases.  Either leaves a connection that looks complete and fails when
     * something tries to use it.
     *
     * @param productDefinition definition of the product
     * @param productGUID unique identifier of the product - the anchor for everything created here
     * @param assetGUID unique identifier of the product's asset
     * @param qualifiedName qualified name of the product's asset
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected
     */
    private void addProductAssetConnection(ProductDefinition productDefinition,
                                           String            productGUID,
                                           String            assetGUID,
                                           String            qualifiedName) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "addProductAssetConnection";

        String connectorTypeGUID = this.getConnectorTypeGUID(productDefinition.getConnectorProvider());

        if (connectorTypeGUID != null)
        {
            ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

            OpenMetadataRootElement connectionElement = classificationExplorerClient.getRootElementByUniqueName(qualifiedName + "_connection",
                                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                this.headerAndProperties(classificationExplorerClient));

            if (connectionElement == null)
            {
                NewElementOptions newElementOptions = new NewElementOptions(integrationContext.getAssetClient().getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(false);
                newElementOptions.setAnchorGUID(productGUID);

        ConnectionClient connectionClient = integrationContext.getConnectionClient();
        EndpointClient   endpointClient   = integrationContext.getEndpointClient();

        /*
         * Set up the connection for the asset.
         */
        VirtualConnectionProperties connectionProperties = new VirtualConnectionProperties();

        connectionProperties.setQualifiedName(qualifiedName + "_connection");
        connectionProperties.setDisplayName("Asset Connection for " + productDefinition.getDisplayName());
        connectionProperties.setDescription("This connection provides access to the metadata access server that supplied the data for this digital product.");
        connectionProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
        connectionProperties.setUserId(integrationContext.getMyUserId());

        connectionProperties.setConfigurationProperties(this.getConnectionConfigurationProperties(productDefinition, productGUID));

        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentGUID(assetGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.RESOURCE_CONNECTION_RELATIONSHIP.typeName);

        String connectionGUID = connectionClient.createConnection(newElementOptions,
                                                                  null,
                                                                  connectionProperties,
                                                                  null);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       connectionProperties.getTypeName(),
                                                                                       connectionProperties.getDisplayName(), connectionGUID));

        /*
         * Pass on all the secrets stores to the product asset.  These secret stores are set up
         * initially in the content pack for this connector.
         */
        for (String purpose : secretsStoreConnectorMap.keySet())
        {
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentGUID(connectionGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName);

            /*
             * Get secrets store connection used by this connector
             */
            Connection secretsConnectorConnection = secretsStoreConnectorMap.get(purpose).getConnection();
            String     secretsStoreConnectionGUID = secretsConnectorConnection.getGUID();

            if (secretsStoreConnectionGUID == null)
            {
                /*
                 * Create the secret store connection from this connector's secrets store connection.
                 */
                ConnectionProperties secretsStoreConnection = new ConnectionProperties();
                secretsStoreConnection.setQualifiedName(qualifiedName + "::" + purpose + "_secretsStore_connection");
                secretsStoreConnection.setDisplayName(purpose + "Secrets Store Connection for " + productDefinition.getDisplayName());
                secretsStoreConnection.setDescription("This connection provides access to the secrets store for this digital product.");
                secretsStoreConnection.setVersionIdentifier(productDefinition.getVersionIdentifier());

                if (secretsConnectorConnection.getConfigurationProperties() != null)
                {
                    Map<String, Object> secretsStoreConfigProperties = new HashMap<>(secretsConnectorConnection.getConfigurationProperties());
                    secretsStoreConnection.setConfigurationProperties(secretsStoreConfigProperties);
                }

                /*
                 * Embed secrets store connection in product connection.
                 */
                EmbeddedConnectionProperties embeddedConnectionProperties = new EmbeddedConnectionProperties();
                embeddedConnectionProperties.setDisplayName(purpose);

                secretsStoreConnectionGUID = connectionClient.createConnection(newElementOptions, null, secretsStoreConnection, embeddedConnectionProperties);

                logRecord(methodName,
                          JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                               secretsStoreConnection.getTypeName(),
                                                                                               secretsStoreConnection.getDisplayName(),
                                                                                               secretsStoreConnectionGUID));

                /*
                 * Link connector type to connection.
                 */
                connectionClient.linkConnectionConnectorType(secretsStoreConnectionGUID,
                                                             secretsConnectorConnection.getConnectorType().getGUID(),
                                                             new MakeAnchorOptions(connectionClient.getMetadataSourceOptions()),
                                                             null);

                logRecord(methodName,
                          JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                  secretsStoreConnection.getTypeName(),
                                                                                  secretsStoreConnectionGUID,
                                                                                  OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                                  secretsConnectorConnection.getConnectorType().getGUID(),
                                                                                  OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName));

                /*
                 * Add secrets store location as an endpoint.
                 */
                EndpointProperties secretsStoreEndpoint = new EndpointProperties();
                secretsStoreEndpoint.setQualifiedName(qualifiedName + "::" + purpose + "_secretsStore_locationEndpoint");
                secretsStoreEndpoint.setDisplayName(purpose + "Secrets Store Endpoint for " + productDefinition.getDisplayName());
                secretsStoreEndpoint.setNetworkAddress(secretsConnectorConnection.getEndpoint().getNetworkAddress());

                newElementOptions.setParentAtEnd1(true);
                newElementOptions.setParentGUID(secretsStoreConnectionGUID);
                newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

                String secretsStoreEndpointGUID = endpointClient.createEndpoint(newElementOptions, null, secretsStoreEndpoint, null);

                logRecord(methodName,
                          JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                               secretsStoreEndpoint.getTypeName(),
                                                                                               secretsStoreEndpoint.getDisplayName(),
                                                                                               secretsStoreEndpointGUID));

                logRecord(methodName,
                          JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                  secretsStoreConnection.getTypeName(),
                                                                                  secretsStoreConnectionGUID,
                                                                                  secretsStoreEndpoint.getTypeName(),
                                                                                  secretsStoreEndpointGUID,
                                                                                  OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName));
            }
            else
            {
                /*
                 * Embed this connector's secrets store connection in product asset connection.
                 */
                EmbeddedConnectionProperties embeddedConnectionProperties = new EmbeddedConnectionProperties();
                embeddedConnectionProperties.setDisplayName(purpose);

                connectionClient.linkEmbeddedConnection(connectionGUID,
                                                        secretsStoreConnectionGUID,
                                                        new MakeAnchorOptions(connectionClient.getMetadataSourceOptions()),
                                                        embeddedConnectionProperties);

                logRecord(methodName,
                          JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                  connectionProperties.getTypeName(),
                                                                                  connectionGUID,
                                                                                  OpenMetadataType.CONNECTION.typeName,
                                                                                  secretsStoreConnectionGUID,
                                                                                  OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName));
            }
        }


        /*
         * Connect the connection to the connectorType
         */
        connectionClient.linkConnectionConnectorType(connectionGUID,
                                                     connectorTypeGUID,
                                                     new MakeAnchorOptions(connectionClient.getMetadataSourceOptions()),
                                                     null);

        logRecord(methodName,
                  JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                          connectionProperties.getTypeName(),
                                                                          connectionGUID,
                                                                          OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                          connectorTypeGUID,
                                                                          OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName));

        /*
         * Create an endpoint to carry the URL of the platform needed to connect to the metadata store.
         */
        EndpointProperties endpointProperties = new EndpointProperties();

        endpointProperties.setQualifiedName(productDefinition.getQualifiedName() + "_referenceDataSet_platformEndpoint");
        endpointProperties.setDisplayName("Reference data set for " + productDefinition.getDisplayName());
        endpointProperties.setDescription("This endpoint represents the URL of the OMAG Server Platform that hosts the metadata access store.");
        endpointProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
        endpointProperties.setNetworkAddress(integrationContext.getMetadataAccessServerPlatformURLRoot());

        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentGUID(connectionGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

        endpointClient = integrationContext.getEndpointClient();

        String endpointGUID = endpointClient.createEndpoint(newElementOptions, null, endpointProperties, null);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       endpointProperties.getTypeName(),
                                                                                       endpointProperties.getDisplayName(),
                                                                                       endpointGUID));

        logRecord(methodName,
                  JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                          connectionProperties.getTypeName(),
                                                                          connectionGUID,
                                                                          endpointProperties.getTypeName(),
                                                                          endpointGUID,
                                                                          OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName));
            }
            else
            {
                this.verifyProductAssetConnection(productDefinition,
                                                  productGUID,
                                                  connectionElement.getElementHeader().getGUID(),
                                                  connectorTypeGUID);
            }
        }
    }


    /**
     * Check that an existing product asset connection still describes the right thing, and correct it where it
     * does not.
     * <br>
     * Three things are checked, and each is something that can drift after the connection was first written:
     * the connector type (the provider can be renamed or replaced between releases), the endpoint (the platform
     * can move to a different URL), and the server name in the configuration properties (the catalog can be
     * rebuilt against a different metadata access server).  A connection carrying any of these stale reads as
     * complete and only fails when a connector is built from it.
     *
     * @param productDefinition definition of the product
     * @param connectionGUID unique identifier of the existing connection
     * @param connectorTypeGUID unique identifier of the connector type the connection should be using
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected
     */
    private void verifyProductAssetConnection(ProductDefinition productDefinition,
                                              String            productGUID,
                                              String            connectionGUID,
                                              String            connectorTypeGUID) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();
        ConnectionClient             connectionClient             = integrationContext.getConnectionClient();
        EndpointClient               endpointClient               = integrationContext.getEndpointClient();

        /*
         * The connection is at end 1 of both of these relationships, so the element wanted is at the other end.
         */
        this.verifyConnectorType(classificationExplorerClient, connectionClient, connectionGUID, connectorTypeGUID);
        this.verifyEndpoint(classificationExplorerClient, endpointClient, productDefinition, connectionGUID);
        this.verifyConnectionConfigurationProperties(connectionClient, productDefinition, productGUID, connectionGUID);
    }


    /**
     * Make sure the connection is linked to the connector type it should be using, and to no other.
     *
     * @param classificationExplorerClient client to retrieve the current connector type
     * @param connectionClient client to relink the connector type
     * @param connectionGUID unique identifier of the connection
     * @param connectorTypeGUID unique identifier of the connector type the connection should be using
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected
     */
    private void verifyConnectorType(ClassificationExplorerClient classificationExplorerClient,
                                     ConnectionClient             connectionClient,
                                     String                       connectionGUID,
                                     String                       connectorTypeGUID) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "verifyConnectorType";

        List<OpenMetadataRootElement> connectorTypes = classificationExplorerClient.getRelatedRootElements(connectionGUID,
                                                                                                           1,
                                                                                                           OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName,
                                                                                                           this.headerAndPropertiesQuery(classificationExplorerClient));
        boolean correctConnectorTypeInPlace = false;

        if (connectorTypes != null)
        {
            for (OpenMetadataRootElement connectorType : connectorTypes)
            {
                if (connectorType != null)
                {
                    String linkedConnectorTypeGUID = connectorType.getElementHeader().getGUID();

                    if (connectorTypeGUID.equals(linkedConnectorTypeGUID))
                    {
                        correctConnectorTypeInPlace = true;
                    }
                    else
                    {
                        /*
                         * A connection has one connector type.  Leaving the old one attached alongside the new
                         * one would make the connection ambiguous rather than corrected.
                         */
                        connectionClient.detachConnectionConnectorType(connectionGUID,
                                                                       linkedConnectorTypeGUID,
                                                                       connectionClient.getDeleteOptions(false));

                        logRecord(methodName,
                                  JacquardAuditCode.UNLINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                            OpenMetadataType.CONNECTION.typeName,
                                                                                            connectionGUID,
                                                                                            OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                                            linkedConnectorTypeGUID,
                                                                                            OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName));
                    }
                }
            }
        }

        if (! correctConnectorTypeInPlace)
        {
            connectionClient.linkConnectionConnectorType(connectionGUID,
                                                         connectorTypeGUID,
                                                         new MakeAnchorOptions(connectionClient.getMetadataSourceOptions()),
                                                         null);

            logRecord(methodName,
                      JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                              OpenMetadataType.CONNECTION.typeName,
                                                                              connectionGUID,
                                                                              OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                              connectorTypeGUID,
                                                                              OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName));
        }
    }


    /**
     * Make sure the connection's endpoint still carries the platform URL of the metadata access server this
     * connector is talking to.
     *
     * @param classificationExplorerClient client to retrieve the current endpoint
     * @param endpointClient client to create or update the endpoint
     * @param productDefinition definition of the product
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected
     */
    private void verifyEndpoint(ClassificationExplorerClient classificationExplorerClient,
                                EndpointClient               endpointClient,
                                ProductDefinition            productDefinition,
                                String                       connectionGUID) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "verifyEndpoint";

        String networkAddress = integrationContext.getMetadataAccessServerPlatformURLRoot();

        List<OpenMetadataRootElement> endpoints = classificationExplorerClient.getRelatedRootElements(connectionGUID,
                                                                                                      1,
                                                                                                      OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName,
                                                                                                      this.headerAndPropertiesQuery(classificationExplorerClient));
        if (endpoints != null)
        {
            for (OpenMetadataRootElement endpoint : endpoints)
            {
                if ((endpoint != null) && (endpoint.getProperties() instanceof EndpointProperties endpointProperties))
                {
                    if (! networkAddress.equals(endpointProperties.getNetworkAddress()))
                    {
                        EndpointProperties updatedProperties = new EndpointProperties();

                        updatedProperties.setNetworkAddress(networkAddress);

                        /*
                         * A merge update: only the network address is supplied, and a replace-all update would
                         * wipe the endpoint's qualified name and be refused for it.
                         */
                        endpointClient.updateEndpoint(endpoint.getElementHeader().getGUID(),
                                                      endpointClient.getUpdateOptions(true),
                                                      updatedProperties);

                        logRecord(methodName,
                                  JacquardAuditCode.UPDATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                       OpenMetadataType.ENDPOINT.typeName,
                                                                                                       endpointProperties.getDisplayName(),
                                                                                                       endpoint.getElementHeader().getGUID()));
                    }
                }
            }

            return;
        }

        /*
         * The connection has no endpoint at all, so there is nothing to correct - it has to be built.
         */
        EndpointProperties endpointProperties = new EndpointProperties();

        endpointProperties.setQualifiedName(productDefinition.getQualifiedName() + "_referenceDataSet_platformEndpoint");
        endpointProperties.setDisplayName("Reference data set for " + productDefinition.getDisplayName());
        endpointProperties.setDescription("This endpoint represents the URL of the OMAG Server Platform that hosts the metadata access store.");
        endpointProperties.setVersionIdentifier(productDefinition.getVersionIdentifier());
        endpointProperties.setNetworkAddress(networkAddress);

        NewElementOptions newElementOptions = new NewElementOptions(endpointClient.getMetadataSourceOptions());

        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentGUID(connectionGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName);

        String endpointGUID = endpointClient.createEndpoint(newElementOptions, null, endpointProperties, null);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       endpointProperties.getTypeName(),
                                                                                       endpointProperties.getDisplayName(),
                                                                                       endpointGUID));
    }


    /**
     * Make sure the connection's configuration properties still name the metadata access server this connector
     * is talking to.  This is the value the data set connector reads to decide which server to call, so a stale
     * one sends every read to a server that may no longer exist.
     *
     * @param connectionClient client to update the connection
     * @param productDefinition definition of the product
     * @param connectionGUID unique identifier of the connection
     *
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected
     */
    private void verifyConnectionConfigurationProperties(ConnectionClient  connectionClient,
                                                         ProductDefinition productDefinition,
                                                         String            productGUID,
                                                         String            connectionGUID) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "verifyConnectionConfigurationProperties";

        OpenMetadataRootElement connectionElement = connectionClient.getConnectionByGUID(connectionGUID, this.headerAndProperties(connectionClient));

        if ((connectionElement != null) && (connectionElement.getProperties() instanceof ConnectionProperties connectionProperties))
        {
            Map<String, Object> currentProperties = connectionProperties.getConfigurationProperties();
            Map<String, Object> requiredProperties = this.getConnectionConfigurationProperties(productDefinition, productGUID);

            if (! requiredProperties.equals(currentProperties))
            {
                ConnectionProperties updatedProperties = new ConnectionProperties();

                updatedProperties.setConfigurationProperties(requiredProperties);

                /*
                 * A merge update, for the same reason as the endpoint above: only the configuration properties
                 * are supplied, so a replace-all update would wipe the connection's qualified name.
                 */
                connectionClient.updateConnection(connectionGUID,
                                                  connectionClient.getUpdateOptions(true),
                                                  updatedProperties);

                logRecord(methodName,
                          JacquardAuditCode.UPDATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                               OpenMetadataType.CONNECTION.typeName,
                                                                                               connectionProperties.getDisplayName(),
                                                                                               connectionGUID));
            }
        }
    }


    /**
     * Return the configuration properties a product asset's connection needs - the product's own settings, plus
     * the metadata access server and page size that the data set connector reads to know which server to call.
     * A product family's connection also names the family, because its connector presents the family's members
     * and has to know where to start walking.
     *
     * @param productDefinition definition of the product
     * @param productGUID unique identifier of the product
     * @return configuration properties
     */
    private Map<String, Object> getConnectionConfigurationProperties(ProductDefinition productDefinition,
                                                                     String            productGUID)
    {
        Map<String, Object> connectionConfigurationProperties = new HashMap<>();

        if (productDefinition.getConfigurationProperties() != null)
        {
            connectionConfigurationProperties.putAll(productDefinition.getConfigurationProperties());
        }

        if (OpenMetadataType.DIGITAL_PRODUCT_FAMILY.typeName.equals(productDefinition.getTypeName()))
        {
            connectionConfigurationProperties.put(TabularDataSetConfigurationProperty.STARTING_ELEMENT_GUID.getName(), productGUID);
        }

        connectionConfigurationProperties.put(TabularDataSetConfigurationProperty.SERVER_NAME.getName(), integrationContext.getMetadataAccessServer());
        connectionConfigurationProperties.put(TabularDataSetConfigurationProperty.MAX_PAGE_SIZE.getName(), integrationContext.getMaxPageSize());

        return connectionConfigurationProperties;
    }


    /**
     * Return the unique identifier for an asset's connector type.
     *
     * @param connectorProvider name of the connector provider's class
     * @return unique identifier of the connector type
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getConnectorTypeGUID(ConnectorProvider connectorProvider) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        String connectorTypeGUID = null;

        if (connectorProvider != null)
        {
            ConnectorTypeClient connectorTypeClient = integrationContext.getConnectorTypeClient();

            /*
             * This is not strictly necessary - but is present to demonstrate how to search for and
             * create Connector Types
             */
            List<OpenMetadataRootElement> existingConnectorTypes = connectorTypeClient.getConnectorTypesByConnectorProvider(connectorProvider.getClass().getName(), null);

            if (existingConnectorTypes != null)
            {
                for (OpenMetadataRootElement connectorType : existingConnectorTypes)
                {
                    if (connectorType != null)
                    {
                        connectorTypeGUID = connectorType.getElementHeader().getGUID();
                        break;
                    }
                }
            }

            /*
             * This connector type is not currently defined.
             */
            if (connectorTypeGUID == null)
            {
                /*
                 * This is the simplified connector type defined by the Open Connector Framework (OCF).
                 * The connector developer fills out the ideal definition for this connector's connector type.
                 * Typically, connector types are defined in an archive and so the GUID is specified to support
                 * that process.
                 * In this method we are creating a new connector type through the API which means the repository
                 * that stores the entry gets to choose the GUID.
                 */
                ConnectorType ocfConnectorType = connectorProvider.getConnectorType();

                ConnectorTypeProperties connectorTypeProperties = new ConnectorTypeProperties();

                connectorTypeProperties.setQualifiedName(ocfConnectorType.getQualifiedName());
                connectorTypeProperties.setDisplayName(ocfConnectorType.getDisplayName());
                connectorTypeProperties.setDescription(ocfConnectorType.getDescription());
                connectorTypeProperties.setConnectorProviderClassName(connectorProvider.getClass().getName());

                /*
                 * Connector types are reusable, so they are not anchored to anything else.
                 */
                NewElementOptions newElementOptions = new NewElementOptions(connectorTypeClient.getMetadataSourceOptions());

                newElementOptions.setAnchorGUID(null);
                newElementOptions.setIsOwnAnchor(true);

                connectorTypeGUID = connectorTypeClient.createConnectorType(newElementOptions,
                                                                            null,
                                                                            connectorTypeProperties,
                                                                            null);
            }
        }

        return connectorTypeGUID;
    }


    /**
     * Return the map of qualifiedNames-to-guids for the nested collections that make up the
     * Structure of the product catalog.
     *
     * @return map
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getProductCatalogFolders() throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        Map<String, String> productFolderMap = new HashMap<>();

        /*
         * The top level folder needs to be created first so that its guid can be the
         * anchor scope for everything else.
         */
        String topLevelGUID = getProductFolder(ProductFolderDefinition.TOP_LEVEL, productFolderMap);

        productFolderMap.put(ProductFolderDefinition.TOP_LEVEL.getQualifiedName(), topLevelGUID);
        this.anchorScopeGUIDs = Collections.singletonList(topLevelGUID);


        /*
         * All the digital products use the same solution design, so it is attached at the top.
         */
        CollectionClient solutionBlueprintClient = integrationContext.getCollectionClient(OpenMetadataType.SOLUTION_BLUEPRINT.typeName);
        solutionBlueprintClient.linkSolutionDesign(topLevelGUID,
                                                   solutionBlueprintGUID,
                                                   new MakeAnchorOptions(solutionBlueprintClient.getMetadataSourceOptions()),
                                                   null);

        /*
         * This root collection gathers all the product catalogs together.  It is defined in the core content pack.
         */
        final String rootCollectionGUID = "dcec6ddb-317e-4c64-907e-be508ceba6d9";
        CollectionClient collectionClient = integrationContext.getCollectionClient();

        collectionClient.addToCollection(rootCollectionGUID, topLevelGUID, new MakeAnchorOptions(collectionClient.getMetadataSourceOptions()), null);

        /*
         * Link the governance definitions to the product catalog ...
         */
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        classificationExplorerClient.addMoreInformationToElement(topLevelGUID,
                                                                 governanceDefinitions.get(ProductGovernanceDefinition.DIGITAL_PRODUCT_CATALOG.getQualifiedName()),
                                                                 new MakeAnchorOptions(classificationExplorerClient.getMetadataSourceOptions()),
                                                                 null);

        /*
         * Now set up all the other folders.
         */
        for (ProductFolderDefinition productFolderDefinition : ProductFolderDefinition.values())
        {
            if (productFolderDefinition != ProductFolderDefinition.TOP_LEVEL)
            {
                String productFolderGUID = this.getProductFolder(productFolderDefinition, productFolderMap);

                productFolderMap.put(productFolderDefinition.getQualifiedName(), productFolderGUID);
            }
        }

        return productFolderMap;
    }


    /**
     * Return the guid of a product folder.  If it exists, it is retrieved from the metadata repository.
     * Otherwise, a new collection is created of the appropriate type and with the right classification
     * attached.  If the product folder has a parent then it is linked to the parent using the CollectionMembership
     * relationship.
     *
     * @param productFolderDefinition description of the folder to create
     * @return guid of the folder
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getProductFolder(ProductFolderDefinition productFolderDefinition,
                                    Map<String, String>     currentProductFolders) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getProductFolder";

        CollectionClient collectionClient = integrationContext.getCollectionClient();

        /*
         * If the product folder is already present then return its GUID
         */
        List<OpenMetadataRootElement> collections = collectionClient.getCollectionsByName(productFolderDefinition.getQualifiedName(), null);

        if (collections != null)
        {
            for (OpenMetadataRootElement collection : collections)
            {
                if (collection != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      productFolderDefinition.getTypeName(),
                                                                                                      productFolderDefinition.getDisplayName(),
                                                                                                      collection.getElementHeader().getGUID()));

                    return collection.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        CollectionProperties collectionProperties = new CollectionProperties();

        collectionProperties.setTypeName(productFolderDefinition.getTypeName());
        collectionProperties.setQualifiedName(productFolderDefinition.getQualifiedName());
        collectionProperties.setDisplayName(productFolderDefinition.getDisplayName());
        collectionProperties.setDescription(productFolderDefinition.getDescription());
        collectionProperties.setCategory(productFolderDefinition.getCategory());

        Map<String, ClassificationProperties> initialClassifications = null;

        NewElementOptions newElementOptions = new NewElementOptions(collectionClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);

        if (productFolderDefinition.getParent() != null)
        {
            String parentGUID = currentProductFolders.get(productFolderDefinition.getParent().getQualifiedName());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(parentGUID);
            newElementOptions.setParentGUID(parentGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
            initialClassifications = getInitialClassificationProperties(null);
        }

        if (productFolderDefinition.getClassificationName() != null)
        {
            if (initialClassifications == null)
            {
                initialClassifications = new HashMap<>();
            }

            initialClassifications.put(productFolderDefinition.getClassificationName(), null);
        }

        String collectionGUID = collectionClient.createCollection(newElementOptions,
                                                                  initialClassifications,
                                                                  collectionProperties,
                                                                  null);
        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       productFolderDefinition.getTypeName(),
                                                                                       productFolderDefinition.getDisplayName(),
                                                                                       collectionGUID));

        return collectionGUID;
    }


    /**
     * Add all defined terms to the glossary at the requested folder.
     *
     * @return map of glossary term qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getGlossaryTerms() throws InvalidParameterException,
                                                          PropertyServerException,
                                                          UserNotAuthorizedException
    {
        Map<String, String> glossaryTermMap = new HashMap<>();

        for (ProductGlossaryTermDefinition glossaryTermDefinition : ProductGlossaryTermDefinition.values())
        {
            String glossaryTermGUID = this.getGlossaryTerm(glossaryTermDefinition);

            glossaryTermMap.put(glossaryTermDefinition.getQualifiedName(), glossaryTermGUID);
        }

        return glossaryTermMap;
    }


    /**
     * Return the unique identifier of the glossary term either by retrieving an existing glossary term or,
     * when that fails, creating a new one.
     *
     * @param glossaryTermDefinition description of the glossary term
     * @return unique identifier of the glossary term
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getGlossaryTerm(ProductGlossaryTermDefinition glossaryTermDefinition) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getGlossaryTerm";

        GlossaryTermClient glossaryTermClient = integrationContext.getGlossaryTermClient();

        /*
         * Create glossary term properties from definition
         */
        GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

        glossaryTermProperties.setQualifiedName(glossaryTermDefinition.getQualifiedName());
        glossaryTermProperties.setDisplayName(glossaryTermDefinition.getDisplayName());
        glossaryTermProperties.setDescription(glossaryTermDefinition.getDescription());
        glossaryTermProperties.setSummary(glossaryTermDefinition.getSummary());
        glossaryTermProperties.setURL(glossaryTermDefinition.getURL());
        glossaryTermProperties.setAbbreviation(glossaryTermDefinition.getAbbreviation());

        /*
         * If the glossary term is already present then return its GUID
         */
        List<OpenMetadataRootElement> terms = glossaryTermClient.getGlossaryTermsByName(glossaryTermDefinition.getQualifiedName(), null);

        if (terms != null)
        {
            for (OpenMetadataRootElement term : terms)
            {
                if (term != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                                      glossaryTermDefinition.getDisplayName(),
                                                                                                      term.getElementHeader().getGUID()));

                    glossaryTermClient.updateGlossaryTerm(term.getElementHeader().getGUID(), glossaryTermClient.getUpdateOptions(true), glossaryTermProperties);
                    return term.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        NewElementOptions newElementOptions = new NewElementOptions(glossaryTermClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);

        Map<String, ClassificationProperties> initialClassifications = null;

        if (glossaryTermDefinition.getFolder() != null)
        {
            String parentGUID = productFolders.get(glossaryTermDefinition.getFolder().getQualifiedName());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(parentGUID);
            newElementOptions.setParentGUID(parentGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
            initialClassifications = getInitialClassificationProperties(null);
        }

        String glossaryTermGUID = glossaryTermClient.createGlossaryTerm(newElementOptions,
                                                                        initialClassifications,
                                                                        glossaryTermProperties,
                                                                        null);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                       glossaryTermDefinition.getDisplayName(),
                                                                                       glossaryTermGUID));

        return glossaryTermGUID;
    }


    /**
     * Add all defined perspectives.  They are linked and anchored to the perspectives collection.
     *
     * @return map of perspectives qualified names to GUIDs
     * @throws InvalidParameterException  invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException    repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     *                                    been disconnected.
     */
    private Map<String, String> getPerspectives() throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        Map<String, String> perspectivesMap = new HashMap<>();

        for (ProductPerspectiveDefinition perspectiveDefinition : ProductPerspectiveDefinition.values())
        {
            String glossaryTermGUID = this.getPerspective(perspectiveDefinition);

            perspectivesMap.put(perspectiveDefinition.getQualifiedName(), glossaryTermGUID);
        }

        return perspectivesMap;
    }


    /**
     * Return the unique identifier of the perspective either by retrieving an existing perspective or,
     * when that fails, creating a new one.
     *
     * @param perspectiveDefinition description of the perspective
     * @return unique identifier of the perspective
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getPerspective(ProductPerspectiveDefinition perspectiveDefinition) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getPerspective";

        PerspectiveClient perspectiveClient = integrationContext.getPerspectiveClient();

        /*
         * Create perspective properties from the definition
         */
        PerspectiveProperties perspectiveProperties = new PerspectiveProperties();

        perspectiveProperties.setQualifiedName(perspectiveDefinition.getQualifiedName());
        perspectiveProperties.setDisplayName(perspectiveDefinition.getDisplayName());
        perspectiveProperties.setDescription(perspectiveDefinition.getDescription());
        perspectiveProperties.setIdentifier(perspectiveDefinition.getIdentifier());
        perspectiveProperties.setURL(perspectiveDefinition.getURL());

        /*
         * If the perspective is already present then return its GUID
         */
        List<OpenMetadataRootElement> perspectives = perspectiveClient.getPerspectivesByName(perspectiveDefinition.getQualifiedName(), null);

        if (perspectives != null)
        {
            for (OpenMetadataRootElement perspective : perspectives)
            {
                if (perspective != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      OpenMetadataType.PERSPECTIVE.typeName,
                                                                                                      perspectiveDefinition.getDisplayName(),
                                                                                                      perspective.getElementHeader().getGUID()));

                    perspectiveClient.updatePerspective(perspective.getElementHeader().getGUID(), perspectiveClient.getUpdateOptions(true), perspectiveProperties);
                    return perspective.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        NewElementOptions newElementOptions = new NewElementOptions(perspectiveClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);

        Map<String, ClassificationProperties> initialClassifications = null;

        if (perspectiveDefinition.getFolder() != null)
        {
            String parentGUID = productFolders.get(perspectiveDefinition.getFolder().getQualifiedName());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(parentGUID);
            newElementOptions.setParentGUID(parentGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
            initialClassifications = getInitialClassificationProperties(null);
        }

        String perspective = perspectiveClient.createPerspective(newElementOptions,
                                                                 initialClassifications,
                                                                 perspectiveProperties,
                                                                 null);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       OpenMetadataType.PERSPECTIVE.typeName,
                                                                                       perspectiveDefinition.getDisplayName(),
                                                                                       perspective));

        return perspective;
    }



    /**
     * Add all defined questions to the glossary at the requested folder.
     *
     * @return map of glossary term qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getQuestions() throws InvalidParameterException,
                                                      PropertyServerException,
                                                      UserNotAuthorizedException
    {
        Map<String, String> perspectiveMap = getPerspectives();
        Map<String, String> questionMap = new HashMap<>();

        for (ProductQuestionDefinition productQuestionDefinition : ProductQuestionDefinition.values())
        {
            String questionGUID = this.getQuestion(productQuestionDefinition, perspectiveMap);

            questionMap.put(productQuestionDefinition.getQualifiedName(), questionGUID);
        }

        return questionMap;
    }


    /**
     * Return the unique identifier of the glossary term either by retrieving an existing glossary term or,
     * when that fails, creating a new one.
     *
     * @param productQuestionDefinition description of the question glossary term
     * @param perspectiveMap map of perspectives to their unique identifiers
     * @return unique identifier of the glossary term
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getQuestion(ProductQuestionDefinition productQuestionDefinition,
                               Map<String, String>       perspectiveMap) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "getQuestion";

        GlossaryTermClient glossaryTermClient = integrationContext.getGlossaryTermClient();

        /*
         * Create a new glossary term properties object for the question.
         */
        GlossaryTermProperties glossaryTermProperties = new GlossaryTermProperties();

        glossaryTermProperties.setQualifiedName(productQuestionDefinition.getQualifiedName());
        glossaryTermProperties.setDisplayName(productQuestionDefinition.getDisplayName());
        glossaryTermProperties.setDescription(productQuestionDefinition.getDescription());
        glossaryTermProperties.setSummary(productQuestionDefinition.getSummary());
        glossaryTermProperties.setAbbreviation(productQuestionDefinition.getURL());

        /*
         * If the glossary term is already present then return its GUID
         */
        List<OpenMetadataRootElement> questions = glossaryTermClient.getGlossaryTermsByName(productQuestionDefinition.getQualifiedName(), null);

        if (questions != null)
        {
            for (OpenMetadataRootElement question : questions)
            {
                if (question != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                                      productQuestionDefinition.getDisplayName(),
                                                                                                      question.getElementHeader().getGUID()));

                    glossaryTermClient.updateGlossaryTerm(question.getElementHeader().getGUID(), glossaryTermClient.getUpdateOptions(true), glossaryTermProperties);
                    return question.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        NewElementOptions newElementOptions = new NewElementOptions(glossaryTermClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);

        Map<String, ClassificationProperties> initialClassifications;

        if (productQuestionDefinition.getPerspective() != null)
        {
            String perspectiveGUID = perspectiveMap.get(productQuestionDefinition.getPerspective().getQualifiedName());

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(perspectiveGUID);
            newElementOptions.setParentGUID(perspectiveGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(false);

            initialClassifications = new HashMap<>();
        }
        else
        {
            newElementOptions.setIsOwnAnchor(true);
            initialClassifications = getInitialClassificationProperties(null);
        }

        initialClassifications.put(OpenMetadataType.QUESTION_CLASSIFICATION.typeName, null);

        String questionGUID = glossaryTermClient.createGlossaryTerm(newElementOptions,
                                                                    initialClassifications,
                                                                    glossaryTermProperties,
                                                                    null);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                                       productQuestionDefinition.getDisplayName(),
                                                                                       questionGUID));

        if (productQuestionDefinition.getFolder() != null)
        {
            String folderGUID = productFolders.get(productQuestionDefinition.getFolder().getQualifiedName());

            CollectionClient collectionClient = integrationContext.getCollectionClient();

            collectionClient.addToCollection(folderGUID, questionGUID, collectionClient.getMakeAnchorOptions(false), null);
        }

        return questionGUID;
    }


    /**
     * Add all the defined communities.
     *
     * @return map of qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getCommunities() throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        Map<String, String> communityMap = new HashMap<>();

        for (ProductCommunityDefinition productCommunityDefinition : ProductCommunityDefinition.values())
        {
            String communityGUID = this.getCommunity(productCommunityDefinition);

            communityMap.put(productCommunityDefinition.getQualifiedName(), communityGUID);
        }

        return communityMap;
    }


    /**
     * Return the unique identifier of the community either by retrieving an existing community or,
     * when that fails, creating a new one.
     *
     * @param productCommunityDefinition description of the community
     * @return unique identifier of the community
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getCommunity(ProductCommunityDefinition productCommunityDefinition) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getCommunity";

        CommunityClient communityClient = integrationContext.getCommunityClient();

        /*
         * Create community properties from definition
         */
        CommunityProperties communityProperties = new CommunityProperties();

        communityProperties.setQualifiedName(productCommunityDefinition.getQualifiedName());
        communityProperties.setDisplayName(productCommunityDefinition.getDisplayName());
        communityProperties.setDescription(productCommunityDefinition.getDescription());

        /*
         * If the community exists, then return its GUID
         */
        List<OpenMetadataRootElement> existingCommunities = communityClient.getCommunitiesByName(productCommunityDefinition.getQualifiedName(), null);

        if (existingCommunities != null)
        {
            for (OpenMetadataRootElement existingCommunity : existingCommunities)
            {
                if (existingCommunity != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      OpenMetadataType.COMMUNITY.typeName,
                                                                                                      productCommunityDefinition.getDisplayName(),
                                                                                                      existingCommunity.getElementHeader().getGUID()));

                    communityClient.updateCommunity(existingCommunity.getElementHeader().getGUID(), communityClient.getUpdateOptions(true), communityProperties);
                    return existingCommunity.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        NewElementOptions newElementOptions = new NewElementOptions(communityClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);
        newElementOptions.setIsOwnAnchor(true);

        /*
         * Add the Jacquard support role as a member of each community.
         * Product managers are also added later when the product is created.
         */
        newElementOptions.setParentAtEnd1(false);
        newElementOptions.setParentGUID(productRoles.get(ProductRoleDefinition.JACQUARD_SUPPORT.getQualifiedName()));
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName);

        AssignmentScopeProperties assignmentScopeProperties = new AssignmentScopeProperties();
        assignmentScopeProperties.setAssignmentType(AssignmentType.DISCUSSION_LEADER.getDisplayName());
        assignmentScopeProperties.setDescription(AssignmentType.DISCUSSION_LEADER.getDescription());

        String communityGUID = communityClient.createCommunity(newElementOptions,
                                                               this.getInitialClassificationProperties(productCommunityDefinition.zoneMembership()),
                                                               communityProperties,
                                                               assignmentScopeProperties);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       OpenMetadataType.COMMUNITY.typeName,
                                                                                       productCommunityDefinition.getDisplayName(),
                                                                                       communityGUID));

        return communityGUID;
    }


    /**
     * Add all the defined communities.
     *
     * @return map of qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getCommunityNoteLogs() throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        Map<String, String> noteLogMap = new HashMap<>();

        for (ProductCommunityDefinition productCommunityDefinition : ProductCommunityDefinition.values())
        {
            String communityNoteLogGUID = this.getCommunityNoteLog(productCommunityDefinition);

            noteLogMap.put(productCommunityDefinition.getQualifiedName(), communityNoteLogGUID);
        }

        return noteLogMap;
    }


    /**
     * Return the unique identifier of the community note log either by retrieving an existing note log or,
     * when that fails, creating a new one.
     *
     * @param productCommunityDefinition description of the community
     * @return unique identifier of the note log
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getCommunityNoteLog(ProductCommunityDefinition productCommunityDefinition) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getCommunityNoteLog";

        CommunityClient communityClient = integrationContext.getCommunityClient();
        NoteLogClient   noteLogClient   = integrationContext.getNoteLogClient();
        String          communityGUID   = communities.get(productCommunityDefinition.getQualifiedName());

        /*
         * Create community note log properties from definition
         */
        NoteLogProperties noteLogProperties = new NoteLogProperties();

        noteLogProperties.setQualifiedName(productCommunityDefinition.getQualifiedName() + "_noteLog");
        noteLogProperties.setDisplayName("Notifications for " + productCommunityDefinition.getDisplayName());
        noteLogProperties.setDescription("Notifications received for products associated with this community.");

        /*
         * If the community is already present then return its GUID
         */
        OpenMetadataRootElement community = communityClient.getCommunityByGUID(communityGUID, null);

        if ((community != null) && (community.getNoteLogs() != null))
        {
            for (RelatedMetadataElementSummary relatedNoteLog : community.getNoteLogs())
            {
                if (relatedNoteLog != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      OpenMetadataType.NOTE_LOG.typeName,
                                                                                                      "Notifications for " + productCommunityDefinition.getDisplayName(),
                                                                                                      relatedNoteLog.getRelatedElement().getElementHeader().getGUID()));

                    noteLogClient.updateNoteLog(relatedNoteLog.getRelatedElement().getElementHeader().getGUID(), noteLogClient.getUpdateOptions(true), noteLogProperties);
                    return relatedNoteLog.getRelatedElement().getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        NewElementOptions newElementOptions = new NewElementOptions(noteLogClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);
        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorGUID(communityGUID);

        String noteLogGUID = noteLogClient.createNoteLog(communityGUID,
                                                         noteLogClient.getMetadataSourceOptions(),
                                                         null,
                                                         noteLogProperties);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       OpenMetadataType.NOTE_LOG.typeName,
                                                                                       noteLogProperties.getDisplayName(),
                                                                                       noteLogGUID));
        return noteLogGUID;
    }


    /**
     * Set up the map of data field qualified names to guids.
     *
     * @return map of data field qualified names to guids
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getDataFields() throws InvalidParameterException,
                                                       PropertyServerException,
                                                       UserNotAuthorizedException
    {
        Map<String, String> dataFieldMap = new HashMap<>();

        for (ProductDataFieldDefinition dataFieldDefinition : ProductDataFieldDefinition.values())
        {
            String dataFieldGUID = this.getDataField(dataFieldDefinition);

            dataFieldMap.put(dataFieldDefinition.getQualifiedName(), dataFieldGUID);
        }

        return dataFieldMap;
    }


    /**
     * Return the unique identifier of the data field either by retrieving it from the metadata
     * repository or, if that fails, creating a new one.  Note, all data fields are visible,
     * even if the products using them are restricted to prevent duplicates being created due to lack of visibility.
     *
     * @param dataFieldDefinition description of the data field
     * @return unique identifier (guid)
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getDataField(ProductDataFieldDefinition dataFieldDefinition) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "getDataField";

        DataFieldClient dataFieldClient = integrationContext.getDataFieldClient();

        /*
         * Create data field properties from definition
         */
        DataFieldProperties dataFieldProperties = new DataFieldProperties();

        dataFieldProperties.setQualifiedName(dataFieldDefinition.getQualifiedName());
        dataFieldProperties.setDisplayName(dataFieldDefinition.getDisplayName());
        dataFieldProperties.setDescription(dataFieldDefinition.getDescription());
        dataFieldProperties.setDataType(dataFieldDefinition.getDataType().getDisplayName());
        dataFieldProperties.setUnits(dataFieldDefinition.getUnits());
        dataFieldProperties.setIsNullable(dataFieldDefinition.isNullable());
        dataFieldProperties.setDefaultValue(dataFieldDefinition.getDefaultValue());
        dataFieldProperties.setNamePatterns(List.of(dataFieldDefinition.getNamePattern()));

        /*
         * If the data field is already present then return its GUID
         */
        List<OpenMetadataRootElement> existingDataFields = dataFieldClient.getDataFieldsByName(dataFieldDefinition.getQualifiedName(), null);

        if (existingDataFields != null)
        {
            for (OpenMetadataRootElement dataField : existingDataFields)
            {
                if (dataField != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      OpenMetadataType.DATA_FIELD.typeName,
                                                                                                      dataFieldDefinition.getDisplayName(),
                                                                                                      dataField.getElementHeader().getGUID()));

                    dataFieldClient.updateDataField(dataField.getElementHeader().getGUID(), dataFieldClient.getUpdateOptions(true), dataFieldProperties);
                    return dataField.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         */
        NewElementOptions newElementOptions = new NewElementOptions(dataFieldClient.getMetadataSourceOptions());

        String parentGUID = productFolders.get(ProductFolderDefinition.DATA_DICTIONARY.getQualifiedName());

        newElementOptions.setIsOwnAnchor(false);
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);
        newElementOptions.setAnchorGUID(parentGUID);
        newElementOptions.setParentGUID(parentGUID);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        newElementOptions.setParentAtEnd1(true);

        Map<String, ClassificationProperties> initialClassifications = null;

        if (dataFieldDefinition.isIdentifier())
        {
            initialClassifications = new HashMap<>();

            initialClassifications.put(OpenMetadataType.OBJECT_IDENTIFIER_CLASSIFICATION.typeName, null);
        }

        String dataFieldGUID = dataFieldClient.createDataField(newElementOptions,
                                               initialClassifications,
                                               dataFieldProperties,
                                               null);

        if (dataFieldDefinition.getGlossaryTerm() != null)
        {
            String glossaryTermGUID = glossaryTerms.get(dataFieldDefinition.getGlossaryTerm().getQualifiedName());
            if (glossaryTermGUID != null)
            {
                ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

                classificationExplorerClient.setupSemanticAssignment(dataFieldGUID,
                                                                     glossaryTermGUID,
                                                                     null,
                                                                     classificationExplorerClient.getMakeAnchorOptions(false));
            }
        }
        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       OpenMetadataType.DATA_FIELD.typeName,
                                                                                       dataFieldDefinition.getDisplayName(),
                                                                                       dataFieldGUID));

        return dataFieldGUID;
    }


    /**
     * Add all the defined governance definitions.
     *
     * @return map of governance definition qualified names to GUIDs
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getGovernanceDefinitions() throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException
    {
        Map<String, String> governanceDefinitionMap = new HashMap<>();

        for (ProductGovernanceDefinition governanceDefinition : ProductGovernanceDefinition.values())
        {
            String governanceDefinitionGUID = this.getGovernanceDefinition(governanceDefinition);

            governanceDefinitionMap.put(governanceDefinition.getQualifiedName(), governanceDefinitionGUID);
        }

        /*
         * Link the governance driver and policies together.  Typically, these definitions would be created
         * by the governance team.
         */
        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();
        MakeAnchorOptions          makeAnchorOptions = new MakeAnchorOptions(governanceDefinitionClient.getMetadataSourceOptions());

        PeerDefinitionProperties peerDefinitionProperties = new PeerDefinitionProperties();

        peerDefinitionProperties.setLabel("enables");
        peerDefinitionProperties.setDescription("The digital product catalog(s) create a platform for managing data sharing opportunities.");

        governanceDefinitionClient.linkPeerDefinitions(governanceDefinitionMap.get(ProductGovernanceDefinition.DIGITAL_PRODUCT_CATALOG.getQualifiedName()),
                                                       governanceDefinitionMap.get(ProductGovernanceDefinition.ENABLE_DATA_SHARING.getQualifiedName()),
                                                       OpenMetadataType.GOVERNANCE_POLICY_LINK_RELATIONSHIP.typeName,
                                                       makeAnchorOptions,
                                                       peerDefinitionProperties);

        SupportingDefinitionProperties supportingDefinitionProperties = new SupportingDefinitionProperties();

        supportingDefinitionProperties.setRationale("Data sharing helps to ensure that key data generated in one part of the company is available for other teams.");

        governanceDefinitionClient.attachSupportingDefinition(governanceDefinitionMap.get(ProductGovernanceDefinition.DATA_DRIVEN.getQualifiedName()),
                                                              governanceDefinitionMap.get(ProductGovernanceDefinition.ENABLE_DATA_SHARING.getQualifiedName()),
                                                              OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                                                              makeAnchorOptions,
                                                              supportingDefinitionProperties);

        supportingDefinitionProperties.setRationale("Digital product catalogs create a platform for exchange or requirements, ideas, skills and, of course, data.  They demonstrate the focus that senior management is placing on data sharing.");

        governanceDefinitionClient.attachSupportingDefinition(governanceDefinitionMap.get(ProductGovernanceDefinition.DATA_DRIVEN.getQualifiedName()),
                                                              governanceDefinitionMap.get(ProductGovernanceDefinition.DIGITAL_PRODUCT_CATALOG.getQualifiedName()),
                                                              OpenMetadataType.GOVERNANCE_RESPONSE_RELATIONSHIP.typeName,
                                                              makeAnchorOptions,
                                                              supportingDefinitionProperties);

        /*
         * Return the map of governance definitions to allow the product definitions to show which governance
         * definitions are relevant to their governance.
         */
        return governanceDefinitionMap;
    }


    /**
     * Return the unique identifier of the governance definition either by retrieving an existing definition or,
     * when that fails, creating a new one.
     *
     * @param governanceDefinition description of the governance definition
     * @return unique identifier of the glossary term
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getGovernanceDefinition(ProductGovernanceDefinition governanceDefinition) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "getGovernanceDefinition";

        GovernanceDefinitionClient governanceDefinitionClient = integrationContext.getGovernanceDefinitionClient();

        /*
         * Create governance definition properties from definition
         */
        GovernanceDefinitionProperties governanceDefinitionProperties = getGovernanceDefinitionProperties(governanceDefinition);

        /*
         * If the GovernanceDefinition is already present then return its GUID
         */
        List<OpenMetadataRootElement> existingGovernanceDefinitions = governanceDefinitionClient.getGovernanceDefinitionsByName(governanceDefinition.getQualifiedName(), null);

        if (existingGovernanceDefinitions != null)
        {
            for (OpenMetadataRootElement existingGovernanceDefinition : existingGovernanceDefinitions)
            {
                if (existingGovernanceDefinition != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      governanceDefinition.getType(),
                                                                                                      governanceDefinition.getDisplayName(),
                                                                                                      existingGovernanceDefinition.getElementHeader().getGUID()));

                    governanceDefinitionClient.updateGovernanceDefinition(existingGovernanceDefinition.getElementHeader().getGUID(),
                                                                          governanceDefinitionClient.getUpdateOptions(true),
                                                                          governanceDefinitionProperties);

                    return existingGovernanceDefinition.getElementHeader().getGUID();
                }
            }
        }

        /*
         * This is the first time...
         *
         * Each governance definition is created as independent elements, and they are not linked together (yet)
         */
        NewElementOptions newElementOptions = new NewElementOptions(governanceDefinitionClient.getMetadataSourceOptions());
        newElementOptions.setIsOwnAnchor(true);

        String governanceDefinitionGUID = governanceDefinitionClient.createGovernanceDefinition(newElementOptions,
                                                                                                this.getInitialClassificationProperties(null),
                                                                                                governanceDefinitionProperties,
                                                                                                null);

        logRecord(methodName,
                  JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                       governanceDefinition.getType(),
                                                                                       governanceDefinition.getDisplayName(),
                                                                                       governanceDefinitionGUID));

        return governanceDefinitionGUID;
    }


    GovernanceDefinitionProperties getGovernanceDefinitionProperties(ProductGovernanceDefinition governanceDefinition)
    {
        GovernanceDefinitionProperties governanceDefinitionProperties = new GovernanceDefinitionProperties();

        governanceDefinitionProperties.setTypeName(governanceDefinition.getType());
        governanceDefinitionProperties.setQualifiedName(governanceDefinition.getQualifiedName());
        governanceDefinitionProperties.setDisplayName(governanceDefinition.getDisplayName());
        governanceDefinitionProperties.setDescription(governanceDefinition.getDescription());
        governanceDefinitionProperties.setSummary(governanceDefinition.getSummary());
        governanceDefinitionProperties.setIdentifier(governanceDefinition.getIdentifier());
        governanceDefinitionProperties.setDomainIdentifier(governanceDefinition.getDomain());
        governanceDefinitionProperties.setScope(governanceDefinition.getImportance());
        governanceDefinitionProperties.setUsage(governanceDefinition.getImportance());
        governanceDefinitionProperties.setImportance(governanceDefinition.getImportance());
        governanceDefinitionProperties.setImplications(governanceDefinition.getImplications());
        governanceDefinitionProperties.setOutcomes(governanceDefinition.getOutcomes());
        governanceDefinitionProperties.setResults(governanceDefinition.getResults());

        Map<String, Object> extendedProperties = new HashMap<>();
        if (governanceDefinition.getObligations() != null)
        {
            extendedProperties.put(OpenMetadataProperty.OBLIGATIONS.name, governanceDefinition.getObligations());
        }
        if (governanceDefinition.getEntitlements() != null)
        {
            extendedProperties.put(OpenMetadataProperty.ENTITLEMENTS.name, governanceDefinition.getEntitlements());
        }
        if (governanceDefinition.getRestrictions() != null)
        {
            extendedProperties.put(OpenMetadataProperty.RESTRICTIONS.name, governanceDefinition.getRestrictions());
        }

        if (! extendedProperties.isEmpty())
        {
            governanceDefinitionProperties.setExtendedProperties(extendedProperties);
        }

        return governanceDefinitionProperties;
    }


    /**
     * Locates/creates the solution blueprint for the open metadata digital products.
     * All solution components are added to the top-level solution blueprint.  They are anchored to it as well.
     * Other solution blueprints are also anchored to the top-level solution blueprint and become collection members.
     * Their membership is determined by the definitions in the solution component enum.
     *
     * @return guid of the blueprint or null if no blueprint can be created
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getSolutionBlueprint() throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getSolutionBlueprint";

        CollectionClient solutionBlueprintClient = integrationContext.getCollectionClient(OpenMetadataType.SOLUTION_BLUEPRINT.typeName);

        NewElementOptions newElementOptions = new NewElementOptions(solutionBlueprintClient.getMetadataSourceOptions());
        newElementOptions.setAnchorScopeGUIDs(this.anchorScopeGUIDs);
        newElementOptions.setIsOwnAnchor(true);

        String blueprintGUID = findSolutionBlueprint(ProductSolutionBlueprint.ALL, newElementOptions);

        SolutionComponentClient solutionComponentClient = integrationContext.getSolutionComponentClient();
        Map<String, String>     qualifiedNameToGUIDMap  = new HashMap<>();

        /*
         * Once this connector's solution components are linked as duplicates of the equivalent components from the
         * content packs, every request that names one of them has to set forDuplicateProcessing.  Without it, the
         * request is resolved to the surviving element of the duplicate cluster, and this connector ends up
         * maintaining the content pack's component rather than its own.
         */
        QueryOptions componentQueryOptions = solutionComponentClient.getQueryOptions();

        componentQueryOptions.setForDuplicateProcessing(true);

        UpdateOptions componentUpdateOptions = solutionComponentClient.getUpdateOptions(true);

        componentUpdateOptions.setForDuplicateProcessing(true);

        MakeAnchorOptions componentLinkOptions = new MakeAnchorOptions(solutionComponentClient.getMetadataSourceOptions());

        componentLinkOptions.setForDuplicateProcessing(true);

        if (blueprintGUID != null)
        {
            /*
             * This ensures all the solution components and nested blueprints are anchored to the top level
             * solution blueprint - and are linked with the collection membership relationship,
             */
            newElementOptions.setAnchorGUID(blueprintGUID);
            newElementOptions.setParentGUID(blueprintGUID);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
            newElementOptions.setParentAtEnd1(true);

            Set<String> newSolutionComponentQNames = new HashSet<>();

            /*
             * Add the solution components to open metadata.  A map of qualifiedNames to GUIDs is maintained to
             * enable the components to be linked together - and to their solution roles.
             */
            for (ProductSolutionComponent solutionComponentDefinition : ProductSolutionComponent.values())
            {
                String componentQualifiedName = solutionComponentDefinition.getQualifiedName();
                String componentGUID          = null;

                /*
                 * Create solution component properties from definition
                 */
                SolutionComponentProperties solutionComponentProperties = new SolutionComponentProperties();

                solutionComponentProperties.setQualifiedName(componentQualifiedName);
                solutionComponentProperties.setDisplayName(solutionComponentDefinition.getDisplayName());
                solutionComponentProperties.setDescription(solutionComponentDefinition.getDescription());
                solutionComponentProperties.setVersionIdentifier(solutionComponentDefinition.getVersionIdentifier());
                solutionComponentProperties.setSolutionComponentType(solutionComponentDefinition.getComponentType());
                solutionComponentProperties.setPlannedDeployedImplementationType(solutionComponentDefinition.getImplementationType());

                /*
                 * Has the component already been defined?
                 */
                List<OpenMetadataRootElement> solutionComponents = solutionComponentClient.getSolutionComponentsByName(componentQualifiedName, componentQueryOptions);

                if (solutionComponents != null)
                {
                    for (OpenMetadataRootElement solutionComponent : solutionComponents)
                    {
                        if (solutionComponent != null)
                        {
                            /*
                             * Component already exists
                             */
                            componentGUID = solutionComponent.getElementHeader().getGUID();
                            solutionComponentClient.updateSolutionComponent(componentGUID, componentUpdateOptions, solutionComponentProperties);
                            break;
                        }
                    }
                }

                if (componentGUID == null)
                {
                    /*
                     * Create the missing component.
                     */
                    componentGUID = solutionComponentClient.createSolutionComponent(newElementOptions,
                                                                                    null,
                                                                                    solutionComponentProperties,
                                                                                    null);

                    /*
                     * Record the missing component so its wires are also added.
                     */
                    newSolutionComponentQNames.add(solutionComponentProperties.getQualifiedName());

                    logRecord(methodName,
                              JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                   OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                                   solutionComponentDefinition.getDisplayName(),
                                                                                                   componentGUID));
                }

                qualifiedNameToGUIDMap.put(componentQualifiedName, componentGUID);
            }

            /*
             * Link the new components together.
             */
            for (SolutionComponentWire solutionComponentWire : SolutionComponentWire.values())
            {
                if ((newSolutionComponentQNames.contains(solutionComponentWire.getComponent1().getQualifiedName())) || (newSolutionComponentQNames.contains(solutionComponentWire.getComponent2().getQualifiedName())))
                {
                    SolutionLinkingWireProperties solutionLinkingWireProperties = new SolutionLinkingWireProperties();

                    solutionLinkingWireProperties.setLabel(solutionComponentWire.getLabel());
                    solutionLinkingWireProperties.setDescription(solutionComponentWire.getDescription());

                    solutionComponentClient.linkSolutionLinkingWire(qualifiedNameToGUIDMap.get(solutionComponentWire.getComponent1().getQualifiedName()),
                                                                    qualifiedNameToGUIDMap.get(solutionComponentWire.getComponent2().getQualifiedName()),
                                                                    componentLinkOptions,
                                                                    solutionLinkingWireProperties);

                    logRecord(methodName,
                              JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                      OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                      solutionComponentWire.getComponent1().getQualifiedName(),
                                                                                      OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                      solutionComponentWire.getComponent2().getQualifiedName(),
                                                                                      OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName));
                }
            }

            /*
             * Connect Actor Roles to Solution Components
             */
            for (SolutionComponentActor solutionComponentActor : SolutionComponentActor.values())
            {
                SolutionComponentActorProperties solutionComponentActorProperties = new SolutionComponentActorProperties();

                solutionComponentActorProperties.setRole(solutionComponentActor.getRole());
                solutionComponentActorProperties.setDescription(solutionComponentActor.getDescription());

                solutionComponentClient.linkSolutionComponentActor(productRoles.get(solutionComponentActor.getSolutionRole().getQualifiedName()),
                                                                   qualifiedNameToGUIDMap.get(solutionComponentActor.getSolutionComponent().getQualifiedName()),
                                                                   componentLinkOptions,
                                                                   solutionComponentActorProperties);

                logRecord(methodName,
                          JacquardAuditCode.LINKING_ELEMENTS.getMessageDefinition(connectorName,
                                                                                  solutionComponentActor.getSolutionRole().getTypeName(),
                                                                                  solutionComponentActor.getSolutionRole().getQualifiedName(),
                                                                                  OpenMetadataType.SOLUTION_COMPONENT.typeName,
                                                                                  solutionComponentActor.getSolutionComponent().getQualifiedName(),
                                                                                  OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName));
            }

            /*
             * Process the nested solution blueprints.
             */
            for (ProductSolutionBlueprint productSolutionBlueprint : ProductSolutionBlueprint.values())
            {
                /*
                 * Ignore the top-level blueprint.
                 */
                if (! ProductSolutionBlueprint.ALL.getQualifiedName().equals(productSolutionBlueprint.getQualifiedName()))
                {
                    CollectionClient collectionClient = integrationContext.getCollectionClient();

                    /*
                     * Retrieve or create the nested blueprint.
                     */
                    String nestedBlueprintGUID = findSolutionBlueprint(productSolutionBlueprint, newElementOptions);

                    for (ProductSolutionComponent solutionComponentDefinition : ProductSolutionComponent.values())
                    {
                        if ((solutionComponentDefinition.getConsumingBlueprints() != null) && (solutionComponentDefinition.getConsumingBlueprints().contains(productSolutionBlueprint)))
                        {
                            collectionClient.addToCollection(nestedBlueprintGUID,
                                                             qualifiedNameToGUIDMap.get(solutionComponentDefinition.getQualifiedName()),
                                                             componentLinkOptions,
                                                             null);
                        }
                    }

                    for (ProductRoleDefinition productRoleDefinition : ProductRoleDefinition.values())
                    {
                        if ((productRoleDefinition.getConsumingBlueprints() != null) && (productRoleDefinition.getConsumingBlueprints().contains(productSolutionBlueprint)))
                        {
                            collectionClient.addToCollection(nestedBlueprintGUID,
                                                             productRoles.get(productRoleDefinition.getQualifiedName()),
                                                             null,
                                                             null);
                        }
                    }
                }
            }
        }

        /*
         * The solution components described by the content packs cover the same components as this connector's
         * blueprint.  They are linked as duplicates so that the retrieval processing combines them.  This runs on
         * every start - including the starts where the blueprint was already in the repository - because the
         * blueprint may well have been created by a build that did not have this processing in it.
         */
        this.linkDuplicateSolutionComponents(qualifiedNameToGUIDMap, componentQueryOptions);

        return blueprintGUID;
    }


    /**
     * Link the solution components created by this connector to the equivalent solution components that come from
     * the content packs.  Two solution components are equivalent if they have the same display name.  Each pairing
     * is described by a PeerDuplicateLink relationship with a status identifier of VALIDATED, and both ends are
     * classified as a KnownDuplicate.  Both the relationship and the classifications are needed before the retrieval
     * processing combines the components.  Any of these that are already in place are left alone.
     *
     * @param qualifiedNameToGUIDMap map of this connector's solution component qualified names to their unique
     *                               identifiers - a component that is missing from the map is identified by its
     *                               qualified name
     * @param queryOptions options for retrieving the components - must have forDuplicateProcessing set
     */
    private void linkDuplicateSolutionComponents(Map<String, String> qualifiedNameToGUIDMap,
                                                 QueryOptions        queryOptions)
    {
        final String methodName = "linkDuplicateSolutionComponents";

        SolutionComponentClient      solutionComponentClient      = integrationContext.getSolutionComponentClient();
        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        for (ProductSolutionComponent solutionComponentDefinition : ProductSolutionComponent.values())
        {
            /*
             * Pairing up the duplicates is housekeeping that runs alongside the product catalog.  A component that
             * cannot be paired up is reported and skipped: the rest of the components are still processed, and the
             * catalog is still built.
             */
            try
            {
                List<OpenMetadataRootElement> matchingComponents = solutionComponentClient.getSolutionComponentsByName(solutionComponentDefinition.getDisplayName(),
                                                                                                                       queryOptions);

                if (matchingComponents == null)
                {
                    continue;
                }

                /*
                 * Separate this connector's component from its peers.  The map holds the components that this start
                 * created or refreshed; the qualified name identifies the component on the starts where the blueprint
                 * was already in the repository - typically because it was built by an earlier release of this
                 * connector.  The query also matches on qualifiedName and identifier, so only the components whose
                 * display name matches are duplicates.
                 */
                String                        mappedComponentGUID         = qualifiedNameToGUIDMap.get(solutionComponentDefinition.getQualifiedName());
                String                        jacquardComponentGUID       = null;
                ElementHeader                 jacquardComponentHeader     = null;
                List<OpenMetadataRootElement> peerComponents              = new ArrayList<>();
                boolean                       jacquardComponentClassified = false;

                for (OpenMetadataRootElement matchingComponent : matchingComponents)
                {
                    if ((matchingComponent != null) &&
                            (matchingComponent.getElementHeader() != null) &&
                            (matchingComponent.getProperties() instanceof SolutionComponentProperties solutionComponentProperties) &&
                            (solutionComponentDefinition.getDisplayName().equals(solutionComponentProperties.getDisplayName())))
                    {
                        if ((matchingComponent.getElementHeader().getGUID().equals(mappedComponentGUID)) ||
                                (solutionComponentDefinition.getQualifiedName().equals(solutionComponentProperties.getQualifiedName())))
                        {
                            jacquardComponentGUID       = matchingComponent.getElementHeader().getGUID();
                            jacquardComponentHeader     = matchingComponent.getElementHeader();
                            jacquardComponentClassified = (matchingComponent.getElementHeader().getKnownDuplicate() != null);
                        }
                        else
                        {
                            peerComponents.add(matchingComponent);
                        }
                    }
                }

                if ((jacquardComponentGUID == null) || (peerComponents.isEmpty()))
                {
                    /*
                     * This connector has no component with this display name, or its component is the only one.
                     */
                    continue;
                }

                for (OpenMetadataRootElement peerComponent : peerComponents)
                {
                    String        peerComponentGUID   = peerComponent.getElementHeader().getGUID();
                    ElementHeader peerComponentHeader = peerComponent.getElementHeader();

                    /*
                     * The peer comes from a content pack, so this repository does not own it.  Its own metadata
                     * collection has to be named on the requests that change it.
                     */
                    MakeAnchorOptions peerOptions = this.getDuplicateOptions(classificationExplorerClient, peerComponentHeader);

                    /*
                     * The links that are already in place are not recreated.
                     */
                    if (! this.isLinkedAsPeerDuplicate(peerComponent, jacquardComponentGUID))
                    {
                        PeerDuplicateLinkProperties peerDuplicateLinkProperties = new PeerDuplicateLinkProperties();

                        peerDuplicateLinkProperties.setStatusIdentifier(StatusIdentifier.VALIDATED.getOrdinal());
                        peerDuplicateLinkProperties.setSteward(integrationContext.getMyUserId());
                        peerDuplicateLinkProperties.setStewardTypeName(OpenMetadataType.USER_IDENTITY.typeName);
                        peerDuplicateLinkProperties.setStewardPropertyName(OpenMetadataProperty.USER_ID.name);
                        peerDuplicateLinkProperties.setSource(connectorName);
                        peerDuplicateLinkProperties.setNotes("The solution components have the same display name (" +
                                                                     solutionComponentDefinition.getDisplayName() +
                                                                     ") and so describe the same part of the Open Metadata Digital Product Catalog.");

                        classificationExplorerClient.linkElementsAsPeerDuplicates(jacquardComponentGUID,
                                                                                  peerComponentGUID,
                                                                                  peerDuplicateLinkProperties,
                                                                                  peerOptions);

                        logRecord(methodName,
                                  JacquardAuditCode.LINKING_DUPLICATE_SOLUTION_COMPONENTS.getMessageDefinition(connectorName,
                                                                                                               solutionComponentDefinition.getDisplayName(),
                                                                                                               jacquardComponentGUID,
                                                                                                               peerComponentGUID));
                    }

                    if (peerComponentHeader.getKnownDuplicate() == null)
                    {
                        classificationExplorerClient.setKnownDuplicateClassification(peerComponentGUID, null, peerOptions);
                    }
                }

                if (! jacquardComponentClassified)
                {
                    classificationExplorerClient.setKnownDuplicateClassification(jacquardComponentGUID,
                                                                                 null,
                                                                                 this.getDuplicateOptions(classificationExplorerClient, jacquardComponentHeader));
                }
            }
            catch (Exception error)
            {
                logExceptionRecord(methodName,
                                   JacquardAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                               error.getClass().getName(),
                                                                                               methodName + "(" + solutionComponentDefinition.getDisplayName() + ")",
                                                                                               error.getMessage()),
                                   error);
            }
        }
    }


    /**
     * Return the options to use when linking or classifying a solution component as a duplicate.
     * <p>
     * forDuplicateProcessing is set so that the request attaches to the individual component rather than to the
     * surviving element of a duplicate cluster, and the metadata collection that owns the component is named where
     * this repository does not own it.  The components that this connector is pairing up with come from the content
     * packs, so most of them are owned elsewhere - without naming their owner, every request to link or classify
     * them is refused.
     *
     * @param classificationExplorerClient client that the request is made through
     * @param elementHeader header of the element being changed
     * @return options
     */
    private MakeAnchorOptions getDuplicateOptions(ClassificationExplorerClient classificationExplorerClient,
                                                  ElementControlHeader         elementHeader)
    {
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions(classificationExplorerClient.getMetadataSourceOptions());

        makeAnchorOptions.setForDuplicateProcessing(true);

        this.setOwningMetadataCollection(makeAnchorOptions, elementHeader);

        return makeAnchorOptions;
    }


    /**
     * Name the metadata collection that owns an instance as the external source of an update.  Nothing is set for
     * an instance that this repository owns - naming an external source for a local instance would wrongly record
     * it as belonging to somebody else.
     *
     * @param metadataSourceOptions options to fill in
     * @param elementHeader header of the instance being changed
     */
    private void setOwningMetadataCollection(MetadataSourceOptions metadataSourceOptions,
                                             ElementControlHeader  elementHeader)
    {
        if ((elementHeader != null) && (elementHeader.getOrigin() != null))
        {
            ElementOriginCategory originCategory = elementHeader.getOrigin().getOriginCategory();

            if ((originCategory != null) && (originCategory != ElementOriginCategory.LOCAL_COHORT))
            {
                metadataSourceOptions.setExternalSourceGUID(elementHeader.getOrigin().getHomeMetadataCollectionId());
                metadataSourceOptions.setExternalSourceName(elementHeader.getOrigin().getHomeMetadataCollectionName());
            }
        }
    }


    /**
     * Determine whether a solution component is already linked to the supplied element with a PeerDuplicateLink
     * relationship.  The relationships came back with the component, so no further retrieval is needed.
     *
     * @param solutionComponent component to test
     * @param peerGUID unique identifier of the element that it may be linked to
     * @return boolean flag
     */
    private boolean isLinkedAsPeerDuplicate(OpenMetadataRootElement solutionComponent,
                                            String                  peerGUID)
    {
        /*
         * The duplicate link is symmetric, so the peer may be at either end.
         */
        return this.containsElement(solutionComponent.getPeerDuplicateOrigin(), peerGUID) ||
                       this.containsElement(solutionComponent.getPeerDuplicatePartner(), peerGUID);
    }


    /**
     * Determine whether a list of related elements includes a particular element.
     *
     * @param relatedElements list of related elements - may be null
     * @param elementGUID unique identifier of the element to look for
     * @return boolean flag
     */
    private boolean containsElement(List<RelatedMetadataElementSummary> relatedElements,
                                    String                             elementGUID)
    {
        if (relatedElements != null)
        {
            for (RelatedMetadataElementSummary relatedElement : relatedElements)
            {
                if ((relatedElement != null) &&
                        (relatedElement.getRelatedElement() != null) &&
                        (relatedElement.getRelatedElement().getElementHeader() != null) &&
                        (elementGUID.equals(relatedElement.getRelatedElement().getElementHeader().getGUID())))
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Return the guid of a solution blueprint.  If it is not found, a null is returned.
     *
     * @param productSolutionBlueprint unique name of the
     * @return guid
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String findSolutionBlueprint(ProductSolutionBlueprint productSolutionBlueprint,
                                         NewElementOptions        newElementOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "findSolutionBlueprint";

        CollectionClient solutionBlueprintClient = integrationContext.getCollectionClient(OpenMetadataType.SOLUTION_BLUEPRINT.typeName);

        /*
         * Create solution blueprint properties from definition
         */
        SolutionBlueprintProperties solutionBlueprintProperties = this.getSolutionBlueprintProperties(productSolutionBlueprint);

        /*
         * If the solution blueprint is already present then return its GUID,
         */
        List<OpenMetadataRootElement> solutionBlueprints = solutionBlueprintClient.getCollectionsByName(productSolutionBlueprint.getQualifiedName(), null);

        if (solutionBlueprints != null)
        {
            for (OpenMetadataRootElement solutionBlueprint : solutionBlueprints)
            {
                if (solutionBlueprint != null)
                {
                    logRecord(methodName,
                              JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                                      OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                                                      productSolutionBlueprint.getDisplayName(),
                                                                                                      solutionBlueprint.getElementHeader().getGUID()));

                    solutionBlueprintClient.updateCollection(solutionBlueprint.getElementHeader().getGUID(), solutionBlueprintClient.getUpdateOptions(true), solutionBlueprintProperties);
                    return solutionBlueprint.getElementHeader().getGUID();
                }
            }
        }

        /*
         * Create the blueprint as this is the first time through
         */
        String blueprintGUID = solutionBlueprintClient.createCollection(newElementOptions,
                                                                        this.getInitialClassificationProperties(null),
                                                                        solutionBlueprintProperties,
                                                                        null);

        if (blueprintGUID != null)
        {
            logRecord(methodName,
                      JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                           OpenMetadataType.SOLUTION_BLUEPRINT.typeName,
                                                                                           productSolutionBlueprint.getDisplayName(),
                                                                                           blueprintGUID));
        }

        return blueprintGUID;
    }


    /**
     * Create a solution blueprint properties from a solution blueprint enum.
     *
     * @param productSolutionBlueprint enum
     * @return properties
     */
    private SolutionBlueprintProperties getSolutionBlueprintProperties(ProductSolutionBlueprint productSolutionBlueprint)
    {
        SolutionBlueprintProperties solutionBlueprintProperties = new SolutionBlueprintProperties();

        solutionBlueprintProperties.setQualifiedName(productSolutionBlueprint.getQualifiedName());
        solutionBlueprintProperties.setDisplayName(productSolutionBlueprint.getDisplayName());
        solutionBlueprintProperties.setDescription(productSolutionBlueprint.getDescription());
        solutionBlueprintProperties.setVersionIdentifier(productSolutionBlueprint.getVersionIdentifier());

        return solutionBlueprintProperties;
    }



    /**
     * Add all the defined roles for this solution.
     *
     * @return map of qualified names to GUIDs
     * @throws InvalidParameterException an invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException the repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private Map<String, String> getProductRoles() throws InvalidParameterException,
                                                         PropertyServerException,
                                                         UserNotAuthorizedException
    {
        Map<String, String> roleMap = new HashMap<>();

        for (ProductRoleDefinition productRoleDefinition : ProductRoleDefinition.values())
        {
            String roleGUID = this.getProductRole(productRoleDefinition);

            roleMap.put(productRoleDefinition.getQualifiedName(), roleGUID);
        }


        /*
         * Link the Jacquard Support Role to the Jacquard Digital Product Loom connector.
         * There is no need to check whether the relationship is already there since this check is handled by the OMF.
         */
        String productDeveloperRoleGUID = roleMap.get(ProductRoleDefinition.JACQUARD_SUPPORT.getQualifiedName());

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

        classificationExplorerClient.addScopeToElement(productDeveloperRoleGUID,
                                                       integrationContext.getIntegrationConnectorGUID(),
                                                       new MakeAnchorOptions(classificationExplorerClient.getMetadataSourceOptions()),
                                                       null);

        return roleMap;
    }


    /**
     * Set up an individual product role.
     *
     * @param productRoleDefinition role definition
     * @return guid of the role
     * @throws InvalidParameterException invalid parameter passed - probably a bug in this code
     * @throws PropertyServerException repository is probably down
     * @throws UserNotAuthorizedException connector's userId not defined to open metadata, or the connector has
     * been disconnected.
     */
    private String getProductRole(ProductRoleDefinition productRoleDefinition) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "getProductRole";

        ActorRoleClient actorRoleClient = integrationContext.getActorRoleClient();
        NewElementOptions newElementOptions = new NewElementOptions(actorRoleClient.getMetadataSourceOptions());
        newElementOptions.setIsOwnAnchor(true);

        String roleQualifiedName = productRoleDefinition.getQualifiedName();
        String roleGUID          = null;

        /*
         * Create actor role properties from definition
         */
        ActorRoleProperties actorRoleProperties = new ActorRoleProperties();

        actorRoleProperties.setTypeName(productRoleDefinition.getTypeName());
        actorRoleProperties.setActorRoleGroups(productRoleDefinition.getActorRoleGroups());
        actorRoleProperties.setQualifiedName(roleQualifiedName);
        actorRoleProperties.setDisplayName(productRoleDefinition.getDisplayName());
        actorRoleProperties.setDescription(productRoleDefinition.getDescription());
        actorRoleProperties.setIdentifier(productRoleDefinition.getIdentifier());

        List<OpenMetadataRootElement> solutionRoles = actorRoleClient.getActorRolesByName(roleQualifiedName, null);

        if (solutionRoles != null)
        {
            for (OpenMetadataRootElement solutionRole : solutionRoles)
            {
                if (solutionRole != null)
                {
                    roleGUID = solutionRole.getElementHeader().getGUID();
                    actorRoleClient.updateActorRole(roleGUID, actorRoleClient.getUpdateOptions(true), actorRoleProperties);
                    break;
                }
            }
        }

        if (roleGUID == null)
        {
            roleGUID = actorRoleClient.createActorRole(newElementOptions,
                                                       this.getInitialClassificationProperties(null),
                                                       actorRoleProperties,
                                                       null);

            logRecord(methodName,
                      JacquardAuditCode.CREATED_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                           productRoleDefinition.getTypeName(),
                                                                                           productRoleDefinition.getDisplayName(),
                                                                                           roleGUID));
        }
        else
        {
            logRecord(methodName,
                      JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION.getMessageDefinition(connectorName,
                                                                                              productRoleDefinition.getTypeName(),
                                                                                              productRoleDefinition.getDisplayName(),
                                                                                              roleGUID));
        }

        return roleGUID;
    }


    /**
     * Return the initial classification properties for an element.  It includes the
     * zone membership classification.  If the zoneNames is null, the default "digital-products" zone is used.
     *
     * @param zoneNames list of zone names or null
     * @return initial classification properties
     */
    private Map<String, ClassificationProperties> getInitialClassificationProperties(List<String> zoneNames)
    {
        Map<String, ClassificationProperties> classificationPropertiesMap = new HashMap<>();

        ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();

        if (zoneNames != null)
        {
            zoneMembershipProperties.setZoneMembership(zoneNames);
        }
        else
        {
            zoneMembershipProperties.setZoneMembership(List.of(GovernanceZoneName.DIGITAL_PRODUCTS.getZoneName()));
        }

        classificationPropertiesMap.put(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                        zoneMembershipProperties);

        return classificationPropertiesMap;
    }

    /**
     * Create a new catalog target processor (typically inherits from CatalogTargetProcessorBase).
     *
     * @param retrievedCatalogTarget details of the open metadata elements describing the catalog target
     * @param catalogTargetContext specialized context for this catalog target
     * @param connectorToTarget connector to access the target resource
     * @return new processor based on the catalog target information
     */
    @Override
    public RequestedCatalogTarget getNewRequestedCatalogTargetSkeleton(CatalogTarget        retrievedCatalogTarget,
                                                                       CatalogTargetContext catalogTargetContext,
                                                                       Connector            connectorToTarget) throws ConnectorCheckedException
    {
        final String methodName = "getNewRequestedCatalogTargetSkeleton";

        try
        {
            return new JacquardCatalogTargetProcessor(retrievedCatalogTarget,
                                                      catalogTargetContext,
                                                      connectorToTarget,
                                                      connectorName,
                                                      auditLog);
        }
        catch (Exception error)
        {
            /*
             * One product's data set cannot be reached - typically its connection still describes a platform
             * that has moved.  That is logged and the target is returned without a processor, so it is left
             * alone this cycle, rather than thrown.  Throwing aborted the whole refresh before the harvest,
             * and the harvest is what repairs the connection: the product could never recover, and every
             * other product was refreshed by nobody in the meantime.
             */
            logExceptionRecord(methodName,
                               JacquardAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                           error.getClass().getName(),
                                                                                           methodName + "(" + retrievedCatalogTarget.getCatalogTargetName() + ")",
                                                                                           error.getMessage()),
                               error);

            return new RequestedCatalogTarget(retrievedCatalogTarget, catalogTargetContext, connectorToTarget);
        }
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        logRecord(methodName, JacquardAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName,
                                                                                        integrationContext.getMetadataAccessServer(),
                                                                                        integrationContext.getMetadataAccessServerPlatformURLRoot()));

        super.disconnect();
    }
}
