/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;

import org.odpi.openmetadata.commonservices.generichandlers.ConnectionConverter;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectorTypeConverter;
import org.odpi.openmetadata.commonservices.generichandlers.EndpointConverter;
import org.odpi.openmetadata.commonservices.generichandlers.LocationConverter;
import org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc.DigitalArchitectureErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DigitalArchitectureServicesInstance caches references to the runtime objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DigitalArchitectureServicesInstance extends OMASServiceInstance
{
    private final static AccessServiceDescription myDescription = AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS;


    private final ReferenceableHandler<ElementHeader>     referenceableHandler;
    private final AssetHandler<AssetElement> assetHandler;

    private final ConnectionHandler<ConnectionElement>       connectionHandler;
    private final ConnectorTypeHandler<ConnectorTypeElement> connectorTypeHandler;
    private final EndpointHandler<EndpointElement>           endpointHandler;

    private final LocationHandler<LocationElement>  locationHandler;

    private final ReferenceDataHandler<ValidValueElement,
                                              ValidValueAssignmentConsumerElement,
                                              ValidValueAssignmentDefinitionElement,
                                              ValidValueImplAssetElement,
                                              ValidValueImplDefinitionElement,
                                              ValidValueMappingElement,
                                              ReferenceValueAssignmentDefinitionElement,
                                              ReferenceValueAssignmentItemElement> referenceDataHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DigitalArchitecture is allowed to serve Assets from.
     * @param auditLog destination for audit log events.
     * @param defaultZones list of zones that Digital Architecture sets up in new Asset instances.
     * @param publishZones list of zones that Digital Architecture sets up in published Asset instances.
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum number of results that can be returned on a single call
     * @throws NewInstanceException a problem occurred during initialization
     */
    public DigitalArchitectureServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                               List<String>            supportedZones,
                                               List<String>            defaultZones,
                                               List<String>            publishZones,
                                               AuditLog                auditLog,
                                               String                  localServerUserId,
                                               int                     maxPageSize) throws NewInstanceException
    {
        super(myDescription.getAccessServiceFullName(),
              repositoryConnector,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog,
              localServerUserId,
              maxPageSize);

        final String methodName = "new ServiceInstance";

        if (repositoryHandler == null)
        {
           throw new NewInstanceException(DigitalArchitectureErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                                          this.getClass().getName(),
                                          methodName);

        }

        this.referenceableHandler = new ReferenceableHandler<>(new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName),
                                                               ElementHeader.class,
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

        this.connectorTypeHandler = new ConnectorTypeHandler<>(new ConnectorTypeConverter<>(repositoryHelper, serviceName,serverName),
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

        this.locationHandler = new LocationHandler<>(new LocationConverter<>(repositoryHelper, serviceName, serverName),
                                                     LocationElement.class,
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

        this.referenceDataHandler = new ReferenceDataHandler<>(new ValidValueConverter<>(repositoryHelper, serviceName, serverName),
                                                               ValidValueElement.class,
                                                               new ValidValueAssignmentConsumerConverter<>(repositoryHelper, serviceName, serverName),
                                                               ValidValueAssignmentConsumerElement.class,
                                                               new ValidValueAssignmentDefinitionConverter<>(repositoryHelper, serviceName, serverName),
                                                               ValidValueAssignmentDefinitionElement.class,
                                                               new ValidValueImplAssetConverter<>(repositoryHelper, serviceName, serverName),
                                                               ValidValueImplAssetElement.class,
                                                               new ValidValueImplDefinitionConverter<>(repositoryHelper, serviceName, serverName),
                                                               ValidValueImplDefinitionElement.class,
                                                               new ValidValueMappingConverter<>(repositoryHelper, serviceName, serverName),
                                                               ValidValueMappingElement.class,
                                                               new ReferenceValueAssignmentDefinitionConverter<>(repositoryHelper, serviceName, serverName),
                                                               ReferenceValueAssignmentDefinitionElement.class,
                                                               new ReferenceValueAssignmentItemConverter<>(repositoryHelper, serviceName, serverName),
                                                               ReferenceValueAssignmentItemElement.class,
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


    /**
     * Return the handler for managing events.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    public ReferenceableHandler<ElementHeader> getReferenceableHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return referenceableHandler;
    }


    /**
     * Return the handler for managing assets.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<AssetElement> getAssetHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return assetHandler;
    }


    /**
     * Return the handler for managing ConnectionElement objects.
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
     * Return the handler for managing ConnectorTypeElement objects.
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
        final String methodName = "getSchemaTypeHandler";

        validateActiveRepository(methodName);

        return endpointHandler;
    }

    /**
     * Return the handler for managing locations.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    LocationHandler<LocationElement> getLocationHandler() throws PropertyServerException
    {
        final String methodName = "getLocationHandler";

        validateActiveRepository(methodName);

        return locationHandler;
    }


    /**
     * Return the handler for managing valid values.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    ReferenceDataHandler<ValidValueElement,
            ValidValueAssignmentConsumerElement,
            ValidValueAssignmentDefinitionElement,
            ValidValueImplAssetElement,
            ValidValueImplDefinitionElement,
            ValidValueMappingElement,
            ReferenceValueAssignmentDefinitionElement,
            ReferenceValueAssignmentItemElement> getReferenceDataHandler() throws PropertyServerException
    {
        final String methodName = "getReferenceDataHandler";

        validateActiveRepository(methodName);

        return referenceDataHandler;
    }
}
