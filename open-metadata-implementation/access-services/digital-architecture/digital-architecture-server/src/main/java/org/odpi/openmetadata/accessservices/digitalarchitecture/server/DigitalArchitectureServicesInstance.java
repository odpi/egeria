/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;

import org.odpi.openmetadata.accessservices.digitalarchitecture.converters.ReferenceDataAssetConverter;
import org.odpi.openmetadata.accessservices.digitalarchitecture.converters.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.ffdc.DigitalArchitectureErrorCode;
import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.LocationHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ValidValuesHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * DigitalArchitectureServicesInstance caches references to objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public class DigitalArchitectureServicesInstance extends OMASServiceInstance
{
    private static AccessServiceDescription myDescription = AccessServiceDescription.DIGITAL_ARCHITECTURE_OMAS;


    private AssetHandler<ReferenceDataAssetElement>  assetHandler;

    private LocationHandler<LocationElement>  locationHandler;

    private ValidValuesHandler<ValidValueElement,
        ValidValueAssignmentConsumerElement,
        ValidValueAssignmentDefinitionElement,
        ValidValueImplAssetElement,
        ValidValueImplDefinitionElement,
        ValidValueMappingElement,
        ReferenceValueAssignmentDefinitionElement,
        ReferenceValueAssignmentItemElement> validValuesHandler;

    /**
     * Set up the handlers for this server.
     *
     * @param repositoryConnector link to the repository responsible for servicing the REST calls.
     * @param supportedZones list of zones that DigitalArchitecture is allowed to serve Assets from.
     * @param auditLog destination for audit log events.
     * @param defaultZones list of zones that DataManager sets up in new Asset instances.
     * @param publishZones list of zones that DataManager sets up in published Asset instances.
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

        this.assetHandler = new AssetHandler<>(new ReferenceDataAssetConverter<>(repositoryHelper, serviceName, serverName),
                                               ReferenceDataAssetElement.class,
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

        this.validValuesHandler = new ValidValuesHandler<>(new ValidValueConverter<>(repositoryHelper, serviceName, serverName),
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
     * Return the handler for managing assets.
     *
     * @return  handler object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    AssetHandler<ReferenceDataAssetElement> getAssetHandler() throws PropertyServerException
    {
        final String methodName = "getAssetHandler";

        validateActiveRepository(methodName);

        return assetHandler;
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
    ValidValuesHandler<ValidValueElement,
            ValidValueAssignmentConsumerElement,
            ValidValueAssignmentDefinitionElement,
            ValidValueImplAssetElement,
            ValidValueImplDefinitionElement,
            ValidValueMappingElement,
            ReferenceValueAssignmentDefinitionElement,
            ReferenceValueAssignmentItemElement> getValidValuesHandler() throws PropertyServerException
    {
        final String methodName = "getValidValuesHandler";

        validateActiveRepository(methodName);

        return validValuesHandler;
    }
}
