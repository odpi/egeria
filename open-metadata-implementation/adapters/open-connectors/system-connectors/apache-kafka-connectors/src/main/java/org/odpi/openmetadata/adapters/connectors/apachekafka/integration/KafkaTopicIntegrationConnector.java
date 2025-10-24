/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.apachekafka.integration;


import org.odpi.openmetadata.adapters.connectors.apachekafka.integration.ffdc.KafkaIntegrationConnectorAuditCode;
import org.odpi.openmetadata.adapters.connectors.apachekafka.resource.ApacheKafkaAdminConnector;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.CatalogTargetIntegrator;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OperationalStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * KafkaTopicIntegrationConnector catalogues active topics in a kafka broker.
 */
public class KafkaTopicIntegrationConnector extends IntegrationConnectorBase implements CatalogTargetIntegrator
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * A map for template qualified name to templateGUID to remove the need to keep retrieving the template
     * on each refresh.
     */
    private final Map<String, String> templateIdentifiers = new HashMap<>();


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * This method performs two sweeps. It first retrieves the topics from the event broker (Kafka) and validates that are in the
     * catalog - adding or updating them if necessary. The second sweep is to ensure that all the topics catalogued
     * actually exist in the event broker.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName="refresh";

        if ((embeddedConnectors != null) && (!embeddedConnectors.isEmpty()))
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector instanceof ApacheKafkaAdminConnector apacheKafkaAdminConnector)
                {
                    try
                    {
                        this.refreshEndpointTarget(apacheKafkaAdminConnector);
                    }
                    catch (ConnectorCheckedException exception)
                    {
                        throw exception;
                    }
                    catch (Exception exception)
                    {
                        auditLog.logException(methodName,
                                              KafkaIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                           exception.getClass().getName(),
                                                                                                                           exception.getMessage()),
                                              exception);
                    }
                }
            }
        }

        this.refreshCatalogTargets(this);
    }


    /**
     * Processes any event broker configured in this connector's connection.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    public void refreshEndpointTarget(ApacheKafkaAdminConnector apacheKafkaAdminConnector) throws ConnectorCheckedException
    {
        final String methodName = "refreshEndpointTarget";

        try
        {
            /*
             * Retrieve the configuration
             */
            Endpoint endpoint = apacheKafkaAdminConnector.getConnection().getEndpoint();

            if ((endpoint != null) && (endpoint.getAddress() != null))
            {
                String targetRootURL         = endpoint.getAddress();
                String templateQualifiedName = getTemplateQualifiedName(apacheKafkaAdminConnector.getConnection().getConfigurationProperties());
                String templateGUID          = null;

                if (templateQualifiedName != null)
                {
                    templateGUID = templateIdentifiers.get(templateQualifiedName);
                }

                // todo this.refreshEventBroker(targetRootURL, templateGUID, templateQualifiedName);
            }
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  KafkaIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                               exception.getClass().getName(),
                                                                                                               exception.getMessage()),
                                  exception);
        }
    }


    /**
     * Return the optional template qualified name from the configuration properties.  If this property is set, it
     * tries to locate the associated template.
     *
     * @param configurationProperties configuration properties
     * @return template qualified name
     */
    private String getTemplateQualifiedName(Map<String, Object> configurationProperties) throws Exception
    {
        final String methodName = "getTemplateQualifiedName";

        String templateQualifiedName = null;

        if (configurationProperties != null)
        {
            templateQualifiedName = configurationProperties.get(KafkaTopicIntegrationProvider.TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
        }

        if (templateQualifiedName != null)
        {
            String templateGUID = templateIdentifiers.get(templateQualifiedName);

            if (templateGUID == null)
            {
                try
                {
                    AssetClient                   assetClient      = integrationContext.getAssetClient();
                    List<OpenMetadataRootElement> templateElements = assetClient.getAssetsByName(templateQualifiedName, assetClient.getQueryOptions());

                    if (templateElements != null)
                    {
                        for (OpenMetadataRootElement templateElement : templateElements)
                        {
                            if ((templateElement != null) && (templateElement.getProperties() instanceof AssetProperties assetProperties))
                            {
                                String qualifiedName = assetProperties.getQualifiedName();

                                if (templateQualifiedName.equals(qualifiedName))
                                {
                                    templateGUID = templateElement.getElementHeader().getGUID();
                                    templateIdentifiers.put(templateQualifiedName, templateGUID);
                                }
                            }
                        }
                    }
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          KafkaIntegrationConnectorAuditCode.MISSING_TEMPLATE.getMessageDefinition(connectorName,
                                                                                                                   templateQualifiedName),
                                          error);

                    throw error;
                }
            }
        }

        return templateQualifiedName;
    }


    /**
     * Perform the required integration logic for the assigned catalog target.
     *
     * @param requestedCatalogTarget the catalog target
     * @throws ConnectorCheckedException there is an unrecoverable error and the connector should stop processing.
     */
    @Override
    public void integrateCatalogTarget(RequestedCatalogTarget requestedCatalogTarget) throws ConnectorCheckedException
    {
        final String methodName = "integrateCatalogTarget";

        if (propertyHelper.isTypeOf(requestedCatalogTarget.getCatalogTargetElement().getElementHeader(), OpenMetadataType.SOFTWARE_SERVER.typeName))
        {
            try
            {
                Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getGUID(), auditLog);

                if ((connector != null) && (connector.getConnection() != null) && (connector.getConnection().getEndpoint() != null))
                {
                    String targetRootURL         = connector.getConnection().getEndpoint().getAddress();
                    String templateQualifiedName = getTemplateQualifiedName(connectionBean.getConfigurationProperties());
                    String templateGUID          = null;

                    if (templateQualifiedName != null)
                    {
                        templateGUID = templateIdentifiers.get(templateQualifiedName);
                    }

                   // todo this.refreshEventBroker(targetRootURL, templateGUID, templateQualifiedName);
                }
            }
            catch (Exception exception)
            {
                auditLog.logException(methodName,
                                      KafkaIntegrationConnectorAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   exception.getClass().getName(),
                                                                                                                   exception.getMessage()),
                                      exception);
            }
        }
        else
        {
            super.throwWrongTypeOfAsset(requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getGUID(),
                                        requestedCatalogTarget.getCatalogTargetElement().getElementHeader().getType().getTypeName(),
                                        DeployedImplementationType.EVENT_BROKER.getAssociatedTypeName(),
                                        connectorName,
                                        methodName);
        }
    }


    /**
     * Look up or create the event broker for this catalog target.
     *
     * @param server element stub of the catalog target
     * @return qualified name of the event broker
     * @throws ConnectorCheckedException connector stopped
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem with the repository
     * @throws UserNotAuthorizedException permissions problem
     */
    private String getEventBrokerQualifiedName(ElementStub server) throws ConnectorCheckedException,
                                                                          InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "getEventBrokerQualifiedName";

        OpenMetadataStore openMetadataStore = integrationContext.getOpenMetadataStore();

        RelatedMetadataElementList capabilities = openMetadataStore.getRelatedMetadataElements(server.getGUID(),
                                                                                                1,
                                                                                                OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                                                                0,
                                                                                                0);

        if ((capabilities != null) && (capabilities.getElementList() != null))
        {
            for (RelatedMetadataElement capability : capabilities.getElementList())
            {
                if (capability != null)
                {
                    if (propertyHelper.isTypeOf(capability.getElement(), OpenMetadataType.EVENT_BROKER.typeName))
                    {
                        ElementProperties elementProperties = capability.getElement().getElementProperties();

                        return propertyHelper.getStringProperty(connectorName,
                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                elementProperties,
                                                                methodName);
                    }
                }
            }
        }

        /*
         * No Event broker is attached to the server. See if it was created by the integration context.
         */
        String eventBrokerQualifiedName = server.getUniqueName() + ":EventBroker";

        OpenMetadataElement eventBroker = openMetadataStore.getMetadataElementByUniqueName(eventBrokerQualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name);

        ElementProperties supportedCapabilityProperties = propertyHelper.addEnumProperty(null,
                                                                                         OpenMetadataProperty.OPERATIONAL_STATUS.name,
                                                                                         OperationalStatus.getOpenTypeName(),
                                                                                         "Owns");

        if (eventBroker == null)
        {
            /*
             * No event broker, so create it
             */
            ElementProperties eventBrokerProperties = propertyHelper.addStringProperty(null,
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                       eventBrokerQualifiedName);

            NewElementOptions newElementOptions = new NewElementOptions(openMetadataStore.getMetadataSourceOptions());

            newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
            newElementOptions.setAnchorGUID(server.getGUID());
            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorScopeGUID(null);
            newElementOptions.setParentGUID(server.getGUID());
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName);

            openMetadataStore.createMetadataElementInStore(OpenMetadataType.EVENT_BROKER.typeName,
                                                           newElementOptions,
                                                           null,
                                                           new NewElementProperties(eventBrokerProperties),
                                                           new NewElementProperties(supportedCapabilityProperties));
        }
        else
        {
            /*
             * Connect the event broker to the server
             */
            openMetadataStore.createRelatedElementsInStore(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                            server.getGUID(),
                                                            eventBroker.getElementGUID(),
                                                            null,
                                                            null,
                                                            supportedCapabilityProperties);
        }

        return eventBrokerQualifiedName;
    }


    /**
     * Shutdown kafka monitoring
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
                                KafkaIntegrationConnectorAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}
