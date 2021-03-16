/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EndpointHandler manages Endpoint objects.  These describe the network addresses where services are running.  They are used by connection
 * objects to describe the service that the connector should call.  They are linked to servers to show their network address where the services that
 * they are hosting are running.
 *
 * EndpointHandler runs  server-side in the OMAG Server Platform and retrieves Endpoint entities through the OMRSRepositoryConnector via the
 * generic handler and repository handler.
 */
public class EndpointHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public EndpointHandler(OpenMetadataAPIGenericConverter<B> converter,
                           Class<B>                           beanClass,
                           String                             serviceName,
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
        super(converter,
              beanClass,
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
     * Find out if the endpoint is already stored in the repository.
     *
     * @param userId     calling user
     * @param endpoint   endpoint to find
     * @param methodName calling method
     * @return unique identifier of the endpoint or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String findEndpoint(String   userId,
                        Endpoint endpoint,
                        String   methodName) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException
    {
        final String guidParameterName      = "endpoint.getGUID";
        final String qualifiedNameParameter = "endpoint.getQualifiedName";
        final String displayNameParameter   = "endpoint.getDisplayName";

        if (endpoint != null)
        {
            if (endpoint.getGUID() != null)
            {
                try
                {
                    if (this.getEntityFromRepository(userId,
                                                     endpoint.getGUID(),
                                                     OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                     methodName,
                                                     guidParameterName) != null)
                    {
                        return endpoint.getGUID();
                    }
                }
                catch (InvalidParameterException notFound)
                {
                    /*
                     * Not found so try something else
                     */
                }
            }

            String retrievedGUID = null;

            if (endpoint.getQualifiedName() != null)
            {
                retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                             endpoint.getQualifiedName(),
                                                             qualifiedNameParameter,
                                                             OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                             OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                             OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                             supportedZones,
                                                             methodName);
            }

            if ((retrievedGUID == null) && (endpoint.getDisplayName() != null))
            {
                retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                             endpoint.getDisplayName(),
                                                             displayNameParameter,
                                                             OpenMetadataAPIMapper.NAME_PROPERTY_NAME,
                                                             OpenMetadataAPIMapper.CONNECTION_TYPE_GUID,
                                                             OpenMetadataAPIMapper.CONNECTION_TYPE_NAME,
                                                             supportedZones,
                                                             methodName);
            }

            return retrievedGUID;
        }

        return null;
    }


    /**
     * Verify that the Endpoint object is stored in the repository and create it if it is not.
     * If the endpoint is located, there is no check that the endpoint values are equal to those in
     * the supplied endpoint object.
     *
     * @param userId   calling userId
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param endpoint object to add
     * @param methodName calling method
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String saveEndpoint(String   userId,
                               String   externalSourceGUID,
                               String   externalSourceName,
                               Endpoint endpoint,
                               String   methodName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        String existingEndpointGUID = this.findEndpoint(userId, endpoint, methodName);

        if (existingEndpointGUID == null)
        {
            return createEndpoint(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  null,
                                  endpoint.getQualifiedName(),
                                  endpoint.getDisplayName(),
                                  endpoint.getDescription(),
                                  endpoint.getAddress(),
                                  endpoint.getProtocol(),
                                  endpoint.getEncryptionMethod(),
                                  endpoint.getAdditionalProperties(),
                                  methodName);
        }
        else
        {
            updateEndpoint(userId,
                           externalSourceGUID,
                           externalSourceName,
                           existingEndpointGUID,
                           null,
                           endpoint.getQualifiedName(),
                           endpoint.getDisplayName(),
                           endpoint.getDescription(),
                           endpoint.getAddress(),
                           endpoint.getProtocol(),
                           endpoint.getEncryptionMethod(),
                           endpoint.getAdditionalProperties(),
                           methodName);

            return existingEndpointGUID;
        }
    }


    /**
     * Creates a new endpoint and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID the unique identifier for the anchor entity (null for unanchored endpoints)
     * @param qualifiedName      unique name of the endpoint
     * @param displayName    human memorable name for the endpoint - does not need to be unique
     * @param description  (optional) description of the endpoint.  Setting a description, particularly in a public endpoint
     *                        makes the endpoint more valuable to other users and can act as an embryonic glossary term
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param methodName calling method
     *
     * @return GUID for new endpoint
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the endpoint properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createEndpoint(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              anchorGUID,
                                 String              qualifiedName,
                                 String              displayName,
                                 String              description,
                                 String              networkAddress,
                                 String              protocol,
                                 String              encryptionMethod,
                                 Map<String, String> additionalProperties,
                                 String              methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        EndpointBuilder builder = new EndpointBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      networkAddress,
                                                      protocol,
                                                      encryptionMethod,
                                                      additionalProperties,
                                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                      null,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        if (anchorGUID != null)
        {
            builder.setAnchors(userId, anchorGUID, methodName);
        }

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                           OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }


    /**
     * Retrieves an endpoint based on network address.  Returns it if found.  If not creates a new endpoint and returns the unique identifier
     * for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param anchorGUID the unique identifier for the anchor entity (null for unanchored endpoints)
     * @param qualifiedName      unique name of the endpoint
     * @param displayName    human memorable name for the endpoint - does not need to be unique
     * @param description  (optional) description of the endpoint.  Setting a description, particularly in a public endpoint
     *                        makes the endpoint more valuable to other users and can act as an embryonic glossary term
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param methodName calling method
     *
     * @return GUID for endpoint
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the endpoint properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String getEndpointForConnection(String              userId,
                                           String              externalSourceGUID,
                                           String              externalSourceName,
                                           String              anchorGUID,
                                           String              qualifiedName,
                                           String              displayName,
                                           String              description,
                                           String              networkAddress,
                                           String              protocol,
                                           String              encryptionMethod,
                                           Map<String, String> additionalProperties,
                                           String              methodName) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String networkAddressParameterName = "networkAddress";

        List<String>  propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME);

        List<EntityDetail> currentEndpoints = this.getEntitiesByValue(userId,
                                                                      networkAddress,
                                                                      networkAddressParameterName,
                                                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                                      propertyNames,
                                                                      true,
                                                                      null,
                                                                      null,
                                                                      false,
                                                                      supportedZones,
                                                                      0,
                                                                      invalidParameterHandler.getMaxPagingSize(),
                                                                      methodName);

        if ((currentEndpoints != null) && (! currentEndpoints.isEmpty()))
        {
            for (EntityDetail endpoint : currentEndpoints)
            {
                if ((endpoint != null) && (endpoint.getGUID() != null))
                {
                    return endpoint.getGUID();
                }
            }
        }

        return createEndpoint(userId,
                              externalSourceGUID,
                              externalSourceName,
                              anchorGUID,
                              qualifiedName,
                              displayName,
                              description,
                              networkAddress,
                              protocol,
                              encryptionMethod,
                              additionalProperties,
                              methodName);
    }


    /**
     * Updates the properties of an existing endpoint.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software server capability entity that represented the external source - null for local
     * @param externalSourceName name of the software server capability entity that represented the external source
     * @param endpointGUID         unique identifier for the endpoint
     * @param endpointGUIDParameterName parameter providing endpointGUID
     * @param qualifiedName      unique name of the endpoint
     * @param displayName    human memorable name for the endpoint - does not need to be unique
     * @param description  (optional) description of the endpoint.  Setting a description, particularly in a public endpoint
     *                        makes the endpoint more valuable to other users and can act as an embryonic glossary term
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param methodName      calling method
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the endpoint properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void   updateEndpoint(String              userId,
                                 String              externalSourceGUID,
                                 String              externalSourceName,
                                 String              endpointGUID,
                                 String              endpointGUIDParameterName,
                                 String              qualifiedName,
                                 String              displayName,
                                 String              description,
                                 String              networkAddress,
                                 String              protocol,
                                 String              encryptionMethod,
                                 Map<String, String> additionalProperties,
                                 String              methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        EndpointBuilder builder = new EndpointBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      networkAddress,
                                                      protocol,
                                                      encryptionMethod,
                                                      additionalProperties,
                                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                                      OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                                      null,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    endpointGUID,
                                    endpointGUIDParameterName,
                                    OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                    OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    true,
                                    methodName);
    }


    /**
     * Count the number of informal endpoints attached to a supplied entity.
     *
     * @param userId     calling user
     * @param elementGUID identifier for the entity that the object is attached to
     * @param methodName calling method
     * @return count of attached objects
     * @throws InvalidParameterException  the parameters are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public int countEndpoints(String userId,
                              String elementGUID,
                              String methodName) throws InvalidParameterException,
                                                        PropertyServerException,
                                                        UserNotAuthorizedException
    {
        return this.countAttachments(userId,
                                     elementGUID,
                                     OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME,
                                     OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_GUID,
                                     OpenMetadataAPIMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                     methodName);
    }



    /**
     * Return the list of endpoints exactly matching the supplied network address.
     *
     * @param userId the the calling user
     * @param networkAddress network address of endpoint
     * @param networkAddressParameterName parameter providing endpoint
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param methodName calling method
     *
     * @return endpoint list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<B> getEndpointsByNetworkAddress(String       userId,
                                                String       networkAddress,
                                                String       networkAddressParameterName,
                                                List<String> serviceSupportedZones,
                                                int          startFrom,
                                                int          pageSize,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        List<String>  propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataAPIMapper.NETWORK_ADDRESS_PROPERTY_NAME);

        return this.getBeansByValue(userId,
                                    networkAddress,
                                    networkAddressParameterName,
                                    OpenMetadataAPIMapper.ENDPOINT_TYPE_GUID,
                                    OpenMetadataAPIMapper.ENDPOINT_TYPE_NAME,
                                    propertyNames,
                                    true,
                                    serviceSupportedZones,
                                    startFrom,
                                    pageSize,
                                    methodName);
    }
}