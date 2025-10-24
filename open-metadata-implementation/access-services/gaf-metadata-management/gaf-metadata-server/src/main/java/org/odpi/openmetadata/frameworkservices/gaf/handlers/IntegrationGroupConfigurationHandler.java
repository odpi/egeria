/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.handlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.ConnectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OCFConnectionConverter;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.opengovernance.properties.IntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.IntegrationGroupElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredIntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.gaf.converters.IntegrationConnectorConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.IntegrationGroupConverter;
import org.odpi.openmetadata.frameworkservices.gaf.converters.RegisteredIntegrationConnectorConverter;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * GovernanceConfigurationHandler provides the open metadata server side implementation of
 * GovernanceConfigurationServer which is part of the Open Governance Framework (ODF).
 */
public class IntegrationGroupConfigurationHandler
{
    private final String                                             serviceName;
    private final String                                             serverName;
    private final RepositoryHandler                                  repositoryHandler;
    private final OMRSRepositoryHelper                               repositoryHelper;
    private final SoftwareCapabilityHandler<IntegrationGroupElement> integrationGroupHandler;
    private final AssetHandler<IntegrationConnectorElement>          integrationConnectorHandler;
    private final ConnectionHandler<Connection>                      connectionHandler;
    private final InvalidParameterHandler                            invalidParameterHandler;
    private final RegisteredIntegrationConnectorConverter            registeredIntegrationConnectorConverter;


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
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog logging destination
     */
    public IntegrationGroupConfigurationHandler(String                             serviceName,
                                                String                             serverName,
                                                InvalidParameterHandler            invalidParameterHandler,
                                                RepositoryHandler                  repositoryHandler,
                                                OMRSRepositoryHelper               repositoryHelper,
                                                String                             localServerUserId,
                                                OpenMetadataServerSecurityVerifier securityVerifier,
                                                List<String>                       supportedZones,
                                                List<String>                       defaultZones,
                                                List<String>                       publishZones,
                                                AuditLog                           auditLog)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.registeredIntegrationConnectorConverter = new RegisteredIntegrationConnectorConverter(repositoryHelper, serviceName);

        this.integrationGroupHandler = new SoftwareCapabilityHandler<>(new IntegrationGroupConverter<>(repositoryHelper, serviceName, serverName),
                                                                       IntegrationGroupElement.class,
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

        this.integrationConnectorHandler = new AssetHandler<>(new IntegrationConnectorConverter<>(repositoryHelper, serviceName, serverName),
                                                              IntegrationConnectorElement.class,
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

        this.connectionHandler = new ConnectionHandler<>(new OCFConnectionConverter<>(repositoryHelper, serviceName, serverName),
                                                         Connection.class,
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


    /*
     * Support for integration groups
     */


    /**
     * Return the properties from an integration group definition.  The integration group
     * definition is completely contained in a single entity that can be retrieved
     * from the repository services and converted to a bean.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @param serviceSupportedZones supported zones for calling service
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public IntegrationGroupElement getIntegrationGroupByName(String       userId,
                                                             String       name,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final  String   methodName = "getIntegrationGroupByName";
        final  String   nameParameter = "name";

        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);

        return integrationGroupHandler.getBeanByValue(userId,
                                                      name,
                                                      nameParameter,
                                                      OpenMetadataType.INTEGRATION_GROUP.typeGUID,
                                                      OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                      specificMatchPropertyNames,
                                                      null,
                                                      null,
                                                      SequencingOrder.CREATION_DATE_RECENT,
                                                      null,
                                                      false,
                                                      false,
                                                      serviceSupportedZones,
                                                      null,
                                                      methodName);
    }



    /**
     * Return the properties from an integration connector definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the integration connector definition.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return properties of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public  IntegrationConnectorElement getIntegrationConnectorByGUID(String       userId,
                                                                      String       guid,
                                                                      List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        final  String   methodName = "getIntegrationConnectorByGUID";
        final  String   guidParameter = "guid";

        return integrationConnectorHandler.getAssetWithConnection(userId,
                                                                  guid,
                                                                  guidParameter,
                                                                  OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                  false,
                                                                  false,
                                                                  serviceSupportedZones,
                                                                  new Date(),
                                                                  methodName);
    }


    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param userId identifier of calling user
     * @param integrationConnectorGUID integration connector to search for.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of integration group unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public List<String> getIntegrationConnectorRegistrations(String       userId,
                                                             String       integrationConnectorGUID,
                                                             List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "getAllIntegrationConnectors";
        final String guidParameter = "integrationConnectorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, guidParameter, methodName);

        Date effectiveTime = new Date();

        /*
         * Checks this is a valid, visible service.
         */
        connectionHandler.getEntityFromRepository(userId,
                                                  integrationConnectorGUID,
                                                  guidParameter,
                                                  OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                  null,
                                                  null,
                                                  false,
                                                  false,
                                                  serviceSupportedZones,
                                                  effectiveTime,
                                                  methodName);

        List<Relationship>  relationships = repositoryHandler.getRelationshipsByType(userId,
                                                                                     integrationConnectorGUID,
                                                                                     OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                                     OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeGUID,
                                                                                     OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName,
                                                                                     1,
                                                                                     null,
                                                                                     null,
                                                                                     SequencingOrder.CREATION_DATE_RECENT,
                                                                                     null,
                                                                                     false,
                                                                                     false,
                                                                                     0, 0,
                                                                                     effectiveTime,
                                                                                     methodName);

        List<String> results = new ArrayList<>();

        if (relationships != null)
        {
            for (Relationship relationship : relationships)
            {
                if (relationship != null)
                {
                    if (relationship.getEntityOneProxy().getGUID() != null)
                    {
                        results.add(relationship.getEntityOneProxy().getGUID());
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }



    /**
     * Retrieve a specific integration connector registrations with a particular integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the registered integration connector.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return details of the integration connector and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String       userId,
                                                                                   String       integrationGroupGUID,
                                                                                   String       integrationConnectorGUID,
                                                                                   List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException
    {
        final String methodName = "getRegisteredIntegrationConnector";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";
        final String integrationConnectorGUIDParameter = "integrationConnectorGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(integrationGroupGUID, integrationGroupGUIDParameter, methodName);
        invalidParameterHandler.validateGUID(integrationConnectorGUID, integrationConnectorGUIDParameter, methodName);

        List<Relationship> relationships = repositoryHandler.getRelationshipsBetweenEntities(userId,
                                                                                             integrationConnectorGUID,
                                                                                             OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
                                                                                             integrationGroupGUID,
                                                                                             OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeGUID,
                                                                                             OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName,
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
            if (!relationships.isEmpty())
            {
                return registeredIntegrationConnectorConverter.getBean(this.getIntegrationConnectorByGUID(userId,
                                                                                                          integrationConnectorGUID,
                                                                                                          serviceSupportedZones),
                                                                       relationships.get(0));
            }
        }

        return null;
    }


    /**
     * Retrieve the identifiers of the registered integration connectors with an integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param serviceSupportedZones supported zones for calling service
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public List<RegisteredIntegrationConnectorElement> getRegisteredIntegrationConnectors(String       userId,
                                                                                          String       integrationGroupGUID,
                                                                                          int          startingFrom,
                                                                                          int          maximumResults,
                                                                                          List<String> serviceSupportedZones) throws InvalidParameterException,
                                                                                                                                     UserNotAuthorizedException,
                                                                                                                                     PropertyServerException
    {
        final String methodName = "getRegisteredIntegrationConnectors";
        final String integrationGroupGUIDParameter = "integrationGroupGUID";

        List<Relationship> relationships = integrationGroupHandler.getAttachmentLinks(userId,
                                                                                      integrationGroupGUID,
                                                                                      integrationGroupGUIDParameter,
                                                                                      OpenMetadataType.INTEGRATION_GROUP.typeName,
                                                                                      OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeGUID,
                                                                                      OpenMetadataType.REGISTERED_INTEGRATION_CONNECTOR_RELATIONSHIP.typeName,
                                                                                      null,
                                                                                      OpenMetadataType.INTEGRATION_CONNECTOR.typeName,
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
            List<RegisteredIntegrationConnectorElement> results = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                IntegrationConnectorElement connectorElement = this.getIntegrationConnectorByGUID(userId,
                                                                                                  relationship.getEntityTwoProxy().getGUID(),
                                                                                                  serviceSupportedZones);

                results.add(registeredIntegrationConnectorConverter.getBean(connectorElement, relationship));
            }

            return results;
        }

        return null;
    }
}
