/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalogintegrator.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerEventClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.integrationservices.catalogintegrator.ffdc.CatalogIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.catalogintegrator.ffdc.CatalogIntegratorErrorCode;

import java.util.List;
import java.util.Map;

/**
 * CatalogIntegratorContext provides a wrapper around the Asset Manager OMAS client.
 * It provides the simplified interface to open metadata needed by the CatalogIntegratorConnector.
 */
public class CatalogIntegratorContext
{
    private static String glossaryExchangeServiceName       = "GlossaryExchangeService";
    private static String dataAssetExchangeServiceName      = "DataAssetExchangeService";
    private static String collaborationExchangeServiceName  = "CollaborationExchangeService";
    private static String infrastructureExchangeServiceName = "InfrastructureExchangeService";
    private static String stewardshipExchangeServiceName    = "StewardshipExchangeService";
    private static String governanceExchangeServiceName     = "GovernanceExchangeService";
    private static String validValuesExchangeServiceName    = "ValidValuesExchangeService";


    private AssetManagerClient       assetManagerClient;
    private GlossaryExchangeService  glossaryExchangeService;
    private AssetManagerEventClient  eventClient;
    private String                   userId;
    private String                   assetManagerGUID;
    private String                   assetManagerName;
    private String                   connectorName;
    private String                   integrationServiceName;
    private PermittedSynchronization permittedSynchronization;
    private Map<String, Object>      serviceOptions;

    private boolean glossaryExchangeActive       = true;
    private boolean dataAssetExchangeActive      = false;
    private boolean collaborationExchangeActive  = false;
    private boolean infrastructureExchangeActive = false;
    private boolean stewardshipExchangeActive    = false;
    private boolean governanceExchangeActive     = false;
    private boolean validValuesExchangeActive    = false;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param assetManagerClient common client to map requests to
     * @param glossaryExchangeClient client for glossary requests
     * @param eventClient client to register for events
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param permittedSynchronization controls the direction of synchronization that should be allowed
     * @param disabledExchangeServices option from the integration service's configuration
     * @param integrationServiceName name of this service
     * @param auditLog logging destination
     */
    public CatalogIntegratorContext(AssetManagerClient       assetManagerClient,
                                    GlossaryExchangeClient   glossaryExchangeClient,
                                    AssetManagerEventClient  eventClient,
                                    String                   userId,
                                    String                   assetManagerGUID,
                                    String                   assetManagerName,
                                    String                   connectorName,
                                    String                   integrationServiceName,
                                    PermittedSynchronization permittedSynchronization,
                                    List<String>             disabledExchangeServices,
                                    AuditLog                 auditLog)
    {
        final String methodName = "CatalogIntegratorContext";

        this.assetManagerClient       = assetManagerClient;
        this.glossaryExchangeService  = new GlossaryExchangeService(glossaryExchangeClient,
                                                                    permittedSynchronization,
                                                                    userId,
                                                                    assetManagerGUID,
                                                                    assetManagerName,
                                                                    connectorName,
                                                                    auditLog);
        this.eventClient              = eventClient;
        this.userId                   = userId;
        this.assetManagerGUID         = assetManagerGUID;
        this.assetManagerName         = assetManagerName;
        this.connectorName            = connectorName;
        this.integrationServiceName   = integrationServiceName;
        this.permittedSynchronization = permittedSynchronization;

        auditLog.logMessage(methodName,
                            CatalogIntegratorAuditCode.PERMITTED_SYNCHRONIZATION.getMessageDefinition(connectorName,
                                                                                                      permittedSynchronization.getName()));

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
     * Update the description of a specific external identifier.
     *
     * @param openMetadataGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void updateExternalIdentifier(String              openMetadataGUID,
                                         String              externalIdentifier,
                                         KeyPattern          keyPattern,
                                         Map<String, String> mappingProperties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        assetManagerClient.updateExternalIdentifier(userId, assetManagerGUID, assetManagerName, openMetadataGUID, externalIdentifier, keyPattern, mappingProperties);
    }


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit points of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public void confirmSynchronization(String openMetadataGUID,
                                       String externalIdentifier) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        assetManagerClient.confirmSynchronization(userId, assetManagerGUID, assetManagerName, openMetadataGUID, externalIdentifier);
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
}
