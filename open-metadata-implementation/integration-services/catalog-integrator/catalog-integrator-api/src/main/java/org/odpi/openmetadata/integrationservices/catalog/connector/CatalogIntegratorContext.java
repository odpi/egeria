/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.*;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;
import java.util.Map;

/**
 * CatalogIntegratorContext provides a wrapper around the Asset Manager OMAS client.
 * It provides the simplified interface to open metadata needed by the CatalogIntegratorConnector.
 */
public class CatalogIntegratorContext
{
    private static String collaborationExchangeServiceName  = "CollaborationExchangeService";
    private static String dataAssetExchangeServiceName      = "DataAssetExchangeService";
    private static String glossaryExchangeServiceName       = "GlossaryExchangeService";
    private static String governanceExchangeServiceName     = "GovernanceExchangeService";
    private static String infrastructureExchangeServiceName = "InfrastructureExchangeService";
    private static String lineageExchangeServiceName        = "LineageExchangeService";
    private static String stewardshipExchangeServiceName    = "StewardshipExchangeService";
    private static String validValuesExchangeServiceName    = "ValidValuesExchangeService";


    private AssetManagerClient            assetManagerClient;
    private AssetManagerEventClient       eventClient;
    private CollaborationExchangeService  collaborationExchangeService;
    private DataAssetExchangeService      dataAssetExchangeService;
    private GlossaryExchangeService       glossaryExchangeService;
    private GovernanceExchangeService     governanceExchangeService;
    private InfrastructureExchangeService infrastructureExchangeService;
    private LineageExchangeService        lineageExchangeService;
    private StewardshipExchangeService    stewardshipExchangeService;
    private ValidValuesExchangeService    validValuesExchangeService;
    private String                        userId;
    private String                        assetManagerGUID;
    private String                        assetManagerName;
    private String                        connectorName;
    private String                        integrationServiceName;
    private SynchronizationDirection      synchronizationDirection;

    private boolean glossaryExchangeActive       = true;
    private boolean dataAssetExchangeActive      = false;
    private boolean collaborationExchangeActive  = false;
    private boolean infrastructureExchangeActive = false;
    private boolean lineageExchangeActive        = false;
    private boolean stewardshipExchangeActive    = false;
    private boolean governanceExchangeActive     = false;
    private boolean validValuesExchangeActive    = false;

    /**
     * Create a new context for a connector.
     *
     * @param assetManagerClient common client to map requests to
     * @param eventClient client to register for events
     * @param collaborationExchangeClient client for collaboration requests
     * @param dataAssetExchangeClient client for data asset requests
     * @param glossaryExchangeClient client for glossary requests
     * @param governanceExchangeClient client for governance requests
     * @param infrastructureExchangeClient client for infrastructure requests
     * @param lineageExchangeClient client for lineage requests
     * @param stewardshipExchangeClient client for stewardship requests
     * @param validValuesExchangeClient client for valid values requests
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param synchronizationDirection controls the direction of synchronization that should be allowed
     * @param disabledExchangeServices option from the integration service's configuration
     * @param integrationServiceName name of this service
     * @param auditLog logging destination
     */
    public CatalogIntegratorContext(AssetManagerClient           assetManagerClient,
                                    AssetManagerEventClient      eventClient,
                                    CollaborationExchangeClient  collaborationExchangeClient,
                                    DataAssetExchangeClient      dataAssetExchangeClient,
                                    GlossaryExchangeClient       glossaryExchangeClient,
                                    GovernanceExchangeClient     governanceExchangeClient,
                                    InfrastructureExchangeClient infrastructureExchangeClient,
                                    LineageExchangeClient        lineageExchangeClient,
                                    StewardshipExchangeClient    stewardshipExchangeClient,
                                    ValidValuesExchangeClient    validValuesExchangeClient,
                                    String                       userId,
                                    String                       assetManagerGUID,
                                    String                       assetManagerName,
                                    String                       connectorName,
                                    String                       integrationServiceName,
                                    SynchronizationDirection     synchronizationDirection,
                                    List<String>                 disabledExchangeServices,
                                    AuditLog                     auditLog)
    {
        final String methodName = "CatalogIntegratorContext";

        this.assetManagerClient       = assetManagerClient;
        this.eventClient              = eventClient;
        this.collaborationExchangeService  = new CollaborationExchangeService(collaborationExchangeClient,
                                                                              synchronizationDirection,
                                                                              userId,
                                                                              assetManagerGUID,
                                                                              assetManagerName,
                                                                              connectorName,
                                                                              auditLog);
        this.dataAssetExchangeService  = new DataAssetExchangeService(dataAssetExchangeClient,
                                                                      synchronizationDirection,
                                                                      userId,
                                                                      assetManagerGUID,
                                                                      assetManagerName,
                                                                      connectorName,
                                                                      auditLog);
        this.glossaryExchangeService  = new GlossaryExchangeService(glossaryExchangeClient,
                                                                    synchronizationDirection,
                                                                    userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    connectorName,
                                                                    auditLog);
        this.governanceExchangeService  = new GovernanceExchangeService(governanceExchangeClient,
                                                                        synchronizationDirection,
                                                                        userId,
                                                                        assetManagerGUID,
                                                                        assetManagerName,
                                                                        connectorName,
                                                                        auditLog);
        this.infrastructureExchangeService  = new InfrastructureExchangeService(infrastructureExchangeClient,
                                                                                synchronizationDirection,
                                                                                userId,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                connectorName,
                                                                                auditLog);
        this.lineageExchangeService  = new LineageExchangeService(lineageExchangeClient,
                                                                  synchronizationDirection,
                                                                  userId,
                                                                  assetManagerGUID,
                                                                  assetManagerName,
                                                                  connectorName,
                                                                  auditLog);
        this.stewardshipExchangeService  = new StewardshipExchangeService(stewardshipExchangeClient,
                                                                          synchronizationDirection,
                                                                          userId,
                                                                          assetManagerGUID,
                                                                          assetManagerName,
                                                                          connectorName,
                                                                          auditLog);
        this.validValuesExchangeService  = new ValidValuesExchangeService(validValuesExchangeClient,
                                                                          synchronizationDirection,
                                                                          userId,
                                                                          assetManagerGUID,
                                                                          assetManagerName,
                                                                          connectorName,
                                                                          auditLog);
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.integrationServiceName   = integrationServiceName;
        this.synchronizationDirection = synchronizationDirection;

        auditLog.logMessage(methodName,
                            CatalogIntegratorAuditCode.PERMITTED_SYNCHRONIZATION.getMessageDefinition(connectorName,
                                                                                                      synchronizationDirection.getName()));

        if (disabledExchangeServices != null)
        {
            for (String exchangeServiceName : disabledExchangeServices)
            {
                if (glossaryExchangeServiceName.equals(exchangeServiceName))
                {
                    glossaryExchangeActive = false;
                }
                else if (dataAssetExchangeServiceName.equals(exchangeServiceName))
                {
                    dataAssetExchangeActive = false;
                }
                else if (collaborationExchangeServiceName.equals(exchangeServiceName))
                {
                    collaborationExchangeActive = false;
                }
                else if (infrastructureExchangeServiceName.equals(exchangeServiceName))
                {
                    infrastructureExchangeActive = false;
                }
                else if (lineageExchangeServiceName.equals(exchangeServiceName))
                {
                    lineageExchangeActive = false;
                }
                else if (stewardshipExchangeServiceName.equals(exchangeServiceName))
                {
                    stewardshipExchangeActive = false;
                }
                else if (governanceExchangeServiceName.equals(exchangeServiceName))
                {
                    governanceExchangeActive = false;
                }
                else if (validValuesExchangeServiceName.equals(exchangeServiceName))
                {
                    validValuesExchangeActive = false;
                }
            }
        }
    }


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     * @param externalIdentifierName name of the identifier in the third party asset manager
     * @param externalIdentifierUsage description of how the open metadata element maps to the identifier
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void addExternalIdentifier(String                   openMetadataElementGUID,
                                      String                   openMetadataElementTypeName,
                                      String                   externalIdentifier,
                                      String                   externalIdentifierName,
                                      String                   externalIdentifierUsage,
                                      KeyPattern               keyPattern,
                                      Map<String, String>      mappingProperties) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        assetManagerClient.addExternalIdentifier(userId,
                                                 assetManagerGUID,
                                                 assetManagerName,
                                                 synchronizationDirection,
                                                 null,
                                                 openMetadataElementGUID,
                                                 openMetadataElementTypeName,
                                                 externalIdentifier,
                                                 externalIdentifierName,
                                                 externalIdentifierUsage,
                                                 connectorName,
                                                 keyPattern,
                                                 mappingProperties);
    }


    /**
     * Update the description of a specific external identifier.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param externalIdentifierName name of the identifier in the third party asset manager
     * @param externalIdentifierUsage description of how the open metadata element maps to the identifier
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateExternalIdentifier(String              openMetadataElementGUID,
                                         String              openMetadataElementTypeName,
                                         String              externalIdentifier,
                                         String              externalIdentifierName,
                                         String              externalIdentifierUsage,
                                         KeyPattern          keyPattern,
                                         Map<String, String> mappingProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        assetManagerClient.updateExternalIdentifier(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    synchronizationDirection,
                                                    null,
                                                    openMetadataElementGUID,
                                                    openMetadataElementTypeName,
                                                    externalIdentifier,
                                                    externalIdentifierName,
                                                    externalIdentifierUsage,
                                                    connectorName,
                                                    keyPattern,
                                                    mappingProperties);
    }


    /**
     * Remove a new external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     * @param externalIdentifierName name of the identifier in the third party asset manager
     * @param externalIdentifierUsage description of how the open metadata element maps to the identifier
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void removeExternalIdentifier(String                   openMetadataElementGUID,
                                         String                   openMetadataElementTypeName,
                                         String                   externalIdentifier,
                                         String                   externalIdentifierName,
                                         String                   externalIdentifierUsage,
                                         KeyPattern               keyPattern,
                                         Map<String, String>      mappingProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        assetManagerClient.removeExternalIdentifier(userId,
                                                    assetManagerGUID,
                                                    assetManagerName,
                                                    synchronizationDirection,
                                                    null,
                                                    openMetadataElementGUID,
                                                    openMetadataElementTypeName,
                                                    externalIdentifier,
                                                    externalIdentifierName,
                                                    externalIdentifierUsage,
                                                    connectorName,
                                                    keyPattern,
                                                    mappingProperties);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit points of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void confirmSynchronization(String openMetadataGUID,
                                       String openMetadataElementTypeName,
                                       String externalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        assetManagerClient.confirmSynchronization(userId,
                                                  assetManagerGUID,
                                                  assetManagerName,
                                                  openMetadataGUID,
                                                  openMetadataElementTypeName,
                                                  externalIdentifier);
    }


    /**
     * Return the list of headers for open metadata elements that are associated with a particular
     * external identifier.
     *
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    List<ElementHeader> getElementsForExternalIdentifier(String externalIdentifier,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        return assetManagerClient.getElementsForExternalIdentifier(userId, assetManagerGUID, assetManagerName, externalIdentifier, startFrom, pageSize);
    }


    /**
     * Return the interface for exchanging collaboration information (comments, likes, reviews, tags).
     *
     * @return collaboration exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public CollaborationExchangeService getCollaborationExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getCollaborationExchangeService";

        if (collaborationExchangeActive)
        {
            return collaborationExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(collaborationExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging data asset information (assets, schemas, connections).
     *
     * @return data asset exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public DataAssetExchangeService getDataAssetExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getDataAssetExchangeService";

        if (dataAssetExchangeActive)
        {
            return dataAssetExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(dataAssetExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging glossary information (glossaries, with categories, terms and relationships)
     *
     * @return glossary exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public GlossaryExchangeService getGlossaryExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getGlossaryExchangeService";

        if (glossaryExchangeActive)
        {
            return glossaryExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(glossaryExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging governance information (policies, rules, ...)
     *
     * @return governance exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public GovernanceExchangeService getGovernanceExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getGovernanceExchangeService";

        if (governanceExchangeActive)
        {
            return governanceExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(governanceExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging infrastructure information (servers, applications, hosts etc).
     *
     * @return infrastructure exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public InfrastructureExchangeService getInfrastructureExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getInfrastructureExchangeService";

        if (infrastructureExchangeActive)
        {
            return infrastructureExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(infrastructureExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging lineage information (processes, ports, schemas, lineage linkage etc).
     *
     * @return lineage exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public LineageExchangeService getLineageExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getLineageExchangeService";

        if (lineageExchangeActive)
        {
            return lineageExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(lineageExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging stewardship information.
     *
     * @return stewardship exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public StewardshipExchangeService getStewardshipExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getStewardshipExchangeService";

        if (stewardshipExchangeActive)
        {
            return stewardshipExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(stewardshipExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }


    /**
     * Return the interface for exchanging valid values and reference data information.
     *
     * @return valid values exchange service
     * @throws UserNotAuthorizedException this option is not enabled in the configuration
     */
    public ValidValuesExchangeService getValidValuesExchangeService() throws UserNotAuthorizedException
    {
        final String methodName = "getValidValuesExchangeService";

        if (validValuesExchangeActive)
        {
            return validValuesExchangeService;
        }
        else
        {
            throw new UserNotAuthorizedException(
                    CatalogIntegratorErrorCode.DISABLED_EXCHANGE_SERVICE.getMessageDefinition(validValuesExchangeServiceName,
                                                                                              integrationServiceName),
                    this.getClass().getName(),
                    methodName,
                    userId);
        }
    }
}
