/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;

import org.odpi.openmetadata.accessservices.itinfrastructure.connectors.outtopic.ITInfrastructureOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.AssetConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ConnectionConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ConnectorTypeConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ContactMethodConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ControlFlowConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.DataFlowConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.EndpointConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ITProfileConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.LineageMappingConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.PortConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ProcessCallConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.ProcessConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.RelatedAssetConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.SoftwareCapabilityConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.converters.UserIdentityConverter;
import org.odpi.openmetadata.accessservices.itinfrastructure.ffdc.ITInfrastructureErrorCode;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectionElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ConnectorTypeElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ContactMethodElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ControlFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.DataFlowElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.EndpointElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ITProfileElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.LineageMappingElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.PortElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessCallElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.ProcessElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.RelatedAssetElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.UserIdentityElement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ContactDetailsHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ProcessHandler;
import org.odpi.openmetadata.commonservices.generichandlers.RelatedAssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.generichandlers.UserIdentityHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * ITInfrastructureServicesInstance caches references to objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class ITInfrastructureServicesInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.IT_INFRASTRUCTURE_OMAS;

    private ConnectionHandler<ConnectionElement>                 connectionHandler;
    private ConnectorTypeHandler<ConnectorTypeElement>           connectorTypeHandler;
    private EndpointHandler<EndpointElement>                     endpointHandler;
    private ActorProfileHandler<ITProfileElement>                itProfileHandler;
    private UserIdentityHandler<UserIdentityElement>             userIdentityHandler;
    private ContactDetailsHandler<ContactMethodElement>          contactDetailsHandler;
    private SoftwareCapabilityHandler<SoftwareCapabilityElement> softwareCapabilityHandler;
    private AssetHandler<AssetElement>                           assetHandler;
    private RelatedAssetHandler<RelatedAssetElement>             relatedAssetHandler;
    private ProcessHandler<ProcessElement,
                                  PortElement,
                                  DataFlowElement,
                                  ControlFlowElement,
                                  ProcessCallElement,
                                  LineageMappingElement>         processHandler;


    /**
     * Set up the local repository connector that will service the REST Calls.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that IT Infrastructure is allowed to serve Assets from.
     * @param defaultZones list of zones that IT Infrastructure sets up in new Asset instances.
     * @param publishZones list of zones that IT Infrastructure sets up in published Asset instances.
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize max number of results to return on single request.
     * @param outTopicConnection topic of the client side listener
     * @throws NewInstanceException a problem occurred during initialization
     */
    public ITInfrastructureServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                            List<String>            supportedZones,
                                            List<String>            defaultZones,
                                            List<String>            publishZones,
                                            AuditLog                auditLog,
                                            String                  localServerUserId,
                                            int                     maxPageSize,
                                            Connection outTopicConnection) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog,
              localServerUserId,
              maxPageSize,
              null,
              null,
              ITInfrastructureOutTopicClientProvider.class.getName(),
              outTopicConnection);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler != null)
        {
            this.connectionHandler = new ConnectionHandler<>(new ConnectionConverter<>(repositoryHelper, serviceName, serverName),
                                                             ConnectionElement.class,
                                                             serviceName,
                                                             serverName,
                                                             invalidParameterHandler,
                                                             repositoryHandler,
                                                             repositoryHelper,
                                                             localServerUserId,
                                                             securityVerifier,
                                                             supportedZones,
                                                             defaultZones,
                                                             publishZones,
                                                             auditLog);

            this.connectorTypeHandler = new ConnectorTypeHandler<>(new ConnectorTypeConverter<>(repositoryHelper, serviceName, serverName),
                                                                   ConnectorTypeElement.class,
                                                                   serviceName,
                                                                   serverName,
                                                                   invalidParameterHandler,
                                                                   repositoryHandler,
                                                                   repositoryHelper,
                                                                   localServerUserId,
                                                                   securityVerifier,
                                                                   supportedZones,
                                                                   defaultZones,
                                                                   publishZones,
                                                                   auditLog);

            this.endpointHandler = new EndpointHandler<>(new EndpointConverter<>(repositoryHelper, serviceName, serverName),
                                                         EndpointElement.class,
                                                         serviceName,
                                                         serverName,
                                                         invalidParameterHandler,
                                                         repositoryHandler,
                                                         repositoryHelper,
                                                         localServerUserId,
                                                         securityVerifier,
                                                         supportedZones,
                                                         defaultZones,
                                                         publishZones,
                                                         auditLog);

            this.itProfileHandler = new ActorProfileHandler<>(new ITProfileConverter<>(repositoryHelper, serviceName, serverName),
                                                              ITProfileElement.class,
                                                              serviceName,
                                                              serverName,
                                                              invalidParameterHandler,
                                                              repositoryHandler,
                                                              repositoryHelper,
                                                              localServerUserId,
                                                              securityVerifier,
                                                              supportedZones,
                                                              defaultZones,
                                                              publishZones,
                                                              auditLog);

            this.userIdentityHandler = new UserIdentityHandler<>(new UserIdentityConverter<>(repositoryHelper, serviceName, serverName),
                                                                 UserIdentityElement.class,
                                                                 serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHandler,
                                                                 repositoryHelper,
                                                                 localServerUserId,
                                                                 securityVerifier,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog);

            this.contactDetailsHandler = new ContactDetailsHandler<>(new ContactMethodConverter<>(repositoryHelper, serviceName, serverName),
                                                                     ContactMethodElement.class,
                                                                     serviceName,
                                                                     serverName,
                                                                     invalidParameterHandler,
                                                                     repositoryHandler,
                                                                     repositoryHelper,
                                                                     localServerUserId,
                                                                     securityVerifier,
                                                                     supportedZones,
                                                                     defaultZones,
                                                                     publishZones,
                                                                     auditLog);

            this.softwareCapabilityHandler = new SoftwareCapabilityHandler<>(new SoftwareCapabilityConverter<>(repositoryHelper, serviceName, serverName),
                                                                             SoftwareCapabilityElement.class,
                                                                             serviceName,
                                                                             serverName,
                                                                             invalidParameterHandler,
                                                                             repositoryHandler,
                                                                             repositoryHelper,
                                                                             localServerUserId,
                                                                             securityVerifier,
                                                                             supportedZones,
                                                                             defaultZones,
                                                                             publishZones,
                                                                             auditLog);

            this.assetHandler = new AssetHandler<>(new AssetConverter<>(repositoryHelper, serviceName, serverName),
                                                   AssetElement.class,
                                                   serviceName,
                                                   serverName,
                                                   invalidParameterHandler,
                                                   repositoryHandler,
                                                   repositoryHelper,
                                                   localServerUserId,
                                                   securityVerifier,
                                                   supportedZones,
                                                   defaultZones,
                                                   publishZones,
                                                   auditLog);

            this.relatedAssetHandler = new RelatedAssetHandler<>(new RelatedAssetConverter<>(repositoryHelper, serviceName, serverName),
                                                                 RelatedAssetElement.class,
                                                                 serviceName,
                                                                 serverName,
                                                                 invalidParameterHandler,
                                                                 repositoryHandler,
                                                                 repositoryHelper,
                                                                 localServerUserId,
                                                                 securityVerifier,
                                                                 supportedZones,
                                                                 defaultZones,
                                                                 publishZones,
                                                                 auditLog);

            processHandler = new ProcessHandler<>(new ProcessConverter<>(repositoryHelper, serviceName, serverName),
                                                  ProcessElement.class,
                                                  new PortConverter<>(repositoryHelper, serviceName, serverName),
                                                  PortElement.class,
                                                  new DataFlowConverter<>(repositoryHelper, serviceName, serverName),
                                                  DataFlowElement.class,
                                                  new ControlFlowConverter<>(repositoryHelper, serviceName, serverName),
                                                  ControlFlowElement.class,
                                                  new ProcessCallConverter<>(repositoryHelper, serviceName, serverName),
                                                  ProcessCallElement.class,
                                                  new LineageMappingConverter<>(repositoryHelper, serviceName, serverName),
                                                  LineageMappingElement.class,
                                                  serviceName,
                                                  serverName,
                                                  invalidParameterHandler,
                                                  repositoryHandler,
                                                  repositoryHelper,
                                                  localServerUserId,
                                                  securityVerifier,
                                                  supportedZones,
                                                  defaultZones,
                                                  publishZones,
                                                  auditLog);
        }
        else
        {
            throw new NewInstanceException(ITInfrastructureErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);

        }
    }



    /**
     * Return the handler for managing Connection objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectionHandler<ConnectionElement> getConnectionHandler() throws PropertyServerException
    {
        final String methodName = "getConnectionHandler";

        validateActiveRepository(methodName);

        return connectionHandler;
    }



    /**
     * Return the handler for managing ConnectorType objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ConnectorTypeHandler<ConnectorTypeElement> getConnectorTypeHandler() throws PropertyServerException
    {
        final String methodName = "getConnectorTypeHandler";

        validateActiveRepository(methodName);

        return connectorTypeHandler;
    }



    /**
     * Return the handler for managing Endpoint objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    EndpointHandler<EndpointElement> getEndpointHandler() throws PropertyServerException
    {
        final String methodName = "getEndpointHandler";

        validateActiveRepository(methodName);

        return endpointHandler;
    }


    /**
     * Return the handler for managing ITProfile objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ActorProfileHandler<ITProfileElement> getITProfileHandler() throws PropertyServerException
    {
        final String methodName = "getITProfileHandler";

        validateActiveRepository(methodName);

        return itProfileHandler;
    }


    /**
     * Return the handler for managing UserIdentity objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    UserIdentityHandler<UserIdentityElement> getUserIdentityHandler() throws PropertyServerException
    {
        final String methodName = "getUserIdentityHandler";

        validateActiveRepository(methodName);

        return userIdentityHandler;
    }


    /**
     * Return the handler for managing ContactDetails objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ContactDetailsHandler<ContactMethodElement> getContactDetailsHandler() throws PropertyServerException
    {
        final String methodName = "getContactDetailsHandler";

        validateActiveRepository(methodName);

        return contactDetailsHandler;
    }



    /**
     * Return the handler for managing asset objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public AssetHandler<AssetElement> getAssetHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return assetHandler;
    }


    /**
     * Return the handler for managing related asset objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    RelatedAssetHandler<RelatedAssetElement> getRelatedAssetHandler() throws PropertyServerException
    {
        final String methodName = "getRelatedAssetHandler";

        validateActiveRepository(methodName);

        return relatedAssetHandler;
    }


    /**
     * Return the handler for managing SoftwareServerCapabilities objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    SoftwareCapabilityHandler<SoftwareCapabilityElement> getSoftwareCapabilityHandler() throws PropertyServerException
    {
        final String methodName = "getSoftwareServerCapabilityHandler";

        validateActiveRepository(methodName);

        return softwareCapabilityHandler;
    }


    /**
     * Return the handler for managing processes objects.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ProcessHandler<ProcessElement,
                   PortElement,
                   DataFlowElement,
                   ControlFlowElement,
                   ProcessCallElement,
                   LineageMappingElement> getProcessHandler() throws PropertyServerException
    {
        final String methodName = "getProcessHandler";

        validateActiveRepository(methodName);

        return processHandler;
    }
}
