/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceServiceElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.SupportedGovernanceServiceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.converters.GovernanceEngineConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.GovernanceServiceConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.RegisteredGovernanceServiceConverter;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;


/**
 * GovernanceConfigurationHandler provides the open metadata server side implementation of
 * GovernanceConfigurationServer which is part of the Open Governance Framework (ODF).
 */
public class GovernanceEngineConfigurationHandler
{
    private final String                                             serviceName;
    private final String                                             serverName;
    private final RepositoryHandler                                  repositoryHandler;
    private final OMRSRepositoryHelper                               repositoryHelper;
    private final SoftwareCapabilityHandler<GovernanceEngineElement> governanceEngineHandler;
    private final AssetHandler<GovernanceServiceElement>             governanceServiceHandler;
    private final InvalidParameterHandler                            invalidParameterHandler;


    /**
     * Construct the governance engine configuration handler caching the objects
     * needed to operate within a single server instance.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param auditLog logging destination
     */
    public GovernanceEngineConfigurationHandler(String                             serviceName,
                                                String                             serverName,
                                                InvalidParameterHandler            invalidParameterHandler,
                                                RepositoryHandler                  repositoryHandler,
                                                OMRSRepositoryHelper               repositoryHelper,
                                                String                             localServerUserId,
                                                OpenMetadataServerSecurityVerifier securityVerifier,
                                                AuditLog                           auditLog)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;

        this.governanceEngineHandler = new SoftwareCapabilityHandler<>(new GovernanceEngineConverter<>(repositoryHelper, serviceName, serverName),
                                                                       GovernanceEngineElement.class,
                                                                       serviceName,
                                                                       serverName,
                                                                       invalidParameterHandler,
                                                                       repositoryHandler,
                                                                       repositoryHelper,
                                                                       localServerUserId,
                                                                       securityVerifier,
                                                                       auditLog);

        this.governanceServiceHandler = new AssetHandler<>(new GovernanceServiceConverter<>(repositoryHelper, serviceName, serverName),
                                                           GovernanceServiceElement.class,
                                                           serviceName,
                                                           serverName,
                                                           invalidParameterHandler,
                                                           repositoryHandler,
                                                           repositoryHelper,
                                                           localServerUserId,
                                                           securityVerifier,
                                                           auditLog);
    }


    /**
     * Return the properties from a governance engine definition.  The governance engine
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public GovernanceEngineElement getGovernanceEngineByName(String       userId,
                                                             String       name) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final  String   methodName = "getGovernanceEngineByName";
        final  String   nameParameter = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);

        return governanceEngineHandler.getBeanByValue(userId,
                                                      name,
                                                      nameParameter,
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeGUID,
                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                      specificMatchPropertyNames,
                                                      null,
                                                      null,
                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                      null,
                                                      false,
                                                      false,
                                                      null,
                                                      methodName);
    }


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public  GovernanceServiceElement getGovernanceServiceByGUID(String       userId,
                                                                String       guid) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final  String   methodName = "getGovernanceServiceByGUID";
        final  String   guidParameter = "guid";

        return governanceServiceHandler.getAssetWithConnection(userId,
                                                               guid,
                                                               guidParameter,
                                                               OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName);
    }


    /**
     * Register a governance service with a specific governance engine.   Both the
     * governance service and the governance engine already exist, so it is
     * just a question of creating a relationship between them.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType list of governance request types that this governance service is able to process.
     * @param serviceRequestType request type supported by the service
     * @param defaultAnalysisParameters list of analysis parameters that are passed to the governance service (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    @SuppressWarnings(value = "unused")
    public void registerGovernanceServiceWithEngine(String              userId,
                                                    String              governanceEngineGUID,
                                                    String              governanceServiceGUID,
                                                    String              governanceRequestType,
                                                    String              serviceRequestType,
                                                    Map<String, String> defaultAnalysisParameters,
                                                    List<String>        serviceSupportedZones) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "registerGovernanceServiceWithEngine";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";
        final String governanceRequestTypeParameter = "governanceRequestType";

        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);
        invalidParameterHandler.validateName(governanceRequestType, governanceRequestTypeParameter, methodName);

        /*
         * First check if this request type has already been registered.
         */
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       governanceEngineGUID,
                                                                                       OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                       OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       false,
                                                                                       false,
                                                                                       0,
                                                                                       invalidParameterHandler.getMaxPagingSize(),
                                                                                       null,
                                                                                       methodName);


        while (iterator.moreToReceive())
        {
            Relationship supportedGovernanceService = iterator.getNext();

            if (supportedGovernanceService != null)
            {
                String existingRequestType = repositoryHelper.getStringProperty(serviceName,
                                                                                OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                supportedGovernanceService.getProperties(),
                                                                                methodName);

                if (governanceRequestType.equals(existingRequestType))
                {
                    /*
                     * The request type is already registered.  Is it registered to the same governance service?
                     */
                    EntityProxy existingGovernanceServiceProxy = supportedGovernanceService.getEntityTwoProxy();

                    if ((existingGovernanceServiceProxy != null) && (governanceServiceGUID.equals(existingGovernanceServiceProxy.getGUID())))
                    {
                        /*
                         * The existing registration is for the requested governance service.  All that needs to be done
                         * is to set the request parameters to match the supplied values.
                         */
                        InstanceProperties properties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                                        supportedGovernanceService.getProperties(),
                                                                                                        OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                                        defaultAnalysisParameters,
                                                                                                        methodName);

                        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                  properties,
                                                                                  OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                                                  serviceRequestType,
                                                                                  methodName);
                        repositoryHandler.updateRelationshipProperties(userId,
                                                                       null,
                                                                       null,
                                                                       supportedGovernanceService,
                                                                       properties,
                                                                       methodName);
                        return;
                    }
                    else
                    {
                        /*
                         * Delete the service registration and when this drops out of the loop, the new registration will be added.
                         */
                        repositoryHandler.removeRelationship(userId,
                                                             null,
                                                             null,
                                                             supportedGovernanceService,
                                                             methodName);
                    }
                }
            }
        }

        /*
         * If this code executes it means that the governance service can be registered with the governance engine
         * using the supplied request type.
         */

        InstanceProperties instanceProperties = new InstanceProperties();

        repositoryHelper.addStringPropertyToInstance(serviceName,
                                                     instanceProperties,
                                                     OpenMetadataProperty.REQUEST_TYPE.name,
                                                     governanceRequestType,
                                                     methodName);

        instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                          instanceProperties,
                                                                          OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                                          serviceRequestType,
                                                                          methodName);

        instanceProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                             instanceProperties,
                                                                             OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                             defaultAnalysisParameters,
                                                                             methodName);

        governanceEngineHandler.multiLinkElementToElement(userId,
                                                          null,
                                                          null,
                                                          governanceEngineGUID,
                                                          governanceEngineGUIDParameter,
                                                          OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                          governanceServiceGUID,
                                                          governanceServiceGUIDParameter,
                                                          OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                          false,
                                                          false,
                                                          OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                          OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                          instanceProperties,
                                                          new Date(),
                                                          methodName);
    }


    /**
     * Retrieve a specific governance service registrations with a particular governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the registered governance service.
     *
     * @return details of the governance service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public RegisteredGovernanceServiceElement getRegisteredGovernanceService(String       userId,
                                                                             String       governanceEngineGUID,
                                                                             String       governanceServiceGUID) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceService";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";
        final String governanceServiceGUIDParameter = "governanceServiceGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, governanceEngineGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(governanceServiceGUID, governanceServiceGUIDParameter, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                             governanceServiceGUID,
                                                                                             OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                                             governanceEngineGUID,
                                                                                             OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                             OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                                                             1,
                                                                                             null,
                                                                                             null,
                                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                                             null,
                                                                                             false,
                                                                                             false,
                                                                                             null,
                                                                                             methodName);


        if (relationships != null)
        {
            RegisteredGovernanceServiceConverter converter = new RegisteredGovernanceServiceConverter(repositoryHelper, serviceName);

            return converter.getBean(this.getGovernanceServiceByGUID(userId, governanceServiceGUID), relationships);
        }

        return null;
    }


    /**
     * Retrieve the identifiers of the registered governance services with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public List<RegisteredGovernanceServiceElement> getRegisteredGovernanceServices(String       userId,
                                                                                    String       governanceEngineGUID,
                                                                                    int          startingFrom,
                                                                                    int          maximumResults) throws InvalidParameterException,
                                                                                                                        UserNotAuthorizedException,
                                                                                                                        PropertyServerException
    {
        final String methodName = "getRegisteredGovernanceServices";
        final String governanceEngineGUIDParameter = "governanceEngineGUID";

        List<Relationship> relationships = governanceEngineHandler.getAttachmentLinks(userId,
                                                                                      governanceEngineGUID,
                                                                                      governanceEngineGUIDParameter,
                                                                                      OpenMetadataType.GOVERNANCE_ENGINE.typeName,
                                                                                      OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeGUID,
                                                                                      OpenMetadataType.SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP.typeName,
                                                                                      null,
                                                                                      OpenMetadataType.GOVERNANCE_SERVICE.typeName,
                                                                                      2,
                                                                                      null,
                                                                                      null,
                                                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                                                      null,
                                                                                      false,
                                                                                      false,
                                                                                      startingFrom,
                                                                                      maximumResults,
                                                                                      new Date(),
                                                                                      methodName);

        if (relationships != null)
        {
            Map<String, RegisteredGovernanceServiceElement>  governanceServices = new HashMap<>();

            for (Relationship relationship : relationships)
            {
                /*
                 * Process Governance Service (end 2)
                 */
                EntityProxy end2 = relationship.getEntityTwoProxy();

                RegisteredGovernanceServiceElement governanceService = governanceServices.get(end2.getGUID());

                if (governanceService == null)
                {
                    try
                    {
                        GovernanceServiceElement newElement = this.getGovernanceServiceByGUID(userId, end2.getGUID());

                        if (newElement != null)
                        {
                            governanceService = new RegisteredGovernanceServiceElement(newElement);

                            governanceServices.put(end2.getGUID(), governanceService);
                        }
                    }
                    catch (Exception notKnown)
                    {
                        /* ignore */
                    }
                }

                if (governanceService != null)
                {
                    /*
                     * Build the request type list for the service.
                     */
                    String requestType = repositoryHelper.getStringProperty(serviceName,
                                                                            OpenMetadataProperty.REQUEST_TYPE.name,
                                                                            relationship.getProperties(),
                                                                            methodName);

                    if (requestType != null)
                    {
                        SupportedGovernanceServiceProperties relationshipProperties = new SupportedGovernanceServiceProperties();

                        relationshipProperties.setRequestType(repositoryHelper.getStringProperty(serviceName,
                                                                                                 OpenMetadataProperty.REQUEST_TYPE.name,
                                                                                                 relationship.getProperties(),
                                                                                                 methodName));
                        relationshipProperties.setServiceRequestType(repositoryHelper.getStringProperty(serviceName,
                                                                                                        OpenMetadataProperty.SERVICE_REQUEST_TYPE.name,
                                                                                                        relationship.getProperties(),
                                                                                                        methodName));
                        relationshipProperties.setRequestParameters(repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                                              OpenMetadataProperty.REQUEST_PARAMETERS.name,
                                                                                                              relationship.getProperties(),
                                                                                                              methodName));

                        relationshipProperties.setGenerateConnectorActivityReports(repositoryHelper.getBooleanProperty(serviceName,
                                                                                                                       OpenMetadataProperty.GENERATE_CONNECTOR_ACTIVITY_REPORT.name,
                                                                                                                       relationship.getProperties(),
                                                                                                                       methodName));
                        Map<String, SupportedGovernanceServiceProperties> requestTypes = governanceService.getProperties().getRequestTypes();

                        if (requestTypes == null)
                        {
                            requestTypes = new HashMap<>();
                        }

                        requestTypes.put(requestType, relationshipProperties);

                        governanceService.getProperties().setRequestTypes(requestTypes);
                    }
                }
            }

            if (! governanceServices.isEmpty())
            {
                return new ArrayList<>(governanceServices.values());
            }
        }

        return null;
    }
}
