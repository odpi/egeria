/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * EndpointHandler manages Endpoint objects.  These describe the network addresses where services are running.  They are used by connection
 * objects to describe the service that the connector should call.  They are linked to servers to show their network address where the services that
 * they are hosting are running.
 * EndpointHandler runs server-side in the OMAG Server Platform and retrieves Endpoint entities through the OMRSRepositoryConnector via the
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return unique identifier of the endpoint or null
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private String findEndpoint(String   userId,
                                Endpoint endpoint,
                                boolean  forLineage,
                                boolean  forDuplicateProcessing,
                                Date     effectiveTime,
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
                                                     guidParameterName,
                                                     OpenMetadataType.ENDPOINT.typeName,
                                                     null,
                                                     null,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     supportedZones,
                                                     effectiveTime,
                                                     methodName) != null)
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
                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                             OpenMetadataType.CONNECTION.typeGUID,
                                                             OpenMetadataType.CONNECTION.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             effectiveTime,
                                                             methodName);
            }

            if ((retrievedGUID == null) && (endpoint.getDisplayName() != null))
            {
                retrievedGUID = this.getBeanGUIDByUniqueName(userId,
                                                             endpoint.getDisplayName(),
                                                             displayNameParameter,
                                                             OpenMetadataProperty.NAME.name,
                                                             OpenMetadataType.CONNECTION.typeGUID,
                                                             OpenMetadataType.CONNECTION.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             supportedZones,
                                                             effectiveTime,
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
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param parentQualifiedName qualified name of parent - typically the connection
     * @param endpoint object to add
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param methodName calling method
     * @return unique identifier of the endpoint in the repository.
     * @throws InvalidParameterException  the endpoint bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String saveEndpoint(String   userId,
                        String   externalSourceGUID,
                        String   externalSourceName,
                        String   parentQualifiedName,
                        Endpoint endpoint,
                        boolean  forLineage,
                        boolean  forDuplicateProcessing,
                        Date     effectiveTime,
                        String   methodName) throws InvalidParameterException,
                                                    PropertyServerException,
                                                    UserNotAuthorizedException
    {
        String existingEndpointGUID = this.findEndpoint(userId, endpoint, forLineage, forDuplicateProcessing, effectiveTime, methodName);

        if (existingEndpointGUID == null)
        {
            String endpointQualifiedName = endpoint.getQualifiedName();

            if (endpointQualifiedName == null)
            {
                endpointQualifiedName = parentQualifiedName + "-Endpoint";
            }
            return createEndpoint(userId,
                                  externalSourceGUID,
                                  externalSourceName,
                                  null,
                                  endpointQualifiedName,
                                  endpoint.getDisplayName(),
                                  endpoint.getDescription(),
                                  endpoint.getAddress(),
                                  endpoint.getProtocol(),
                                  endpoint.getEncryptionMethod(),
                                  endpoint.getAdditionalProperties(),
                                  null,
                                  null,
                                  null,
                                  null,
                                  effectiveTime,
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
                           null,
                           null,
                           true,
                           null,
                           null,
                           forLineage,
                           forDuplicateProcessing,
                           effectiveTime,
                           methodName);

            return existingEndpointGUID;
        }
    }


    /**
     * Creates a new endpoint and returns the unique identifier for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID the unique identifier for the anchor entity (null for unanchored endpoints)
     * @param qualifiedName      unique name of the endpoint
     * @param displayName    human memorable name for the endpoint - does not need to be unique
     * @param description  (optional) description of the endpoint.  Setting a description, particularly in a public endpoint
     *                        makes the endpoint more valuable to other users and can act as an embryonic glossary term
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param suppliedTypeName name of the subtype for the endpoint or null for standard type
     * @param extendedProperties any properties for a subtype
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param effectiveTime the time that the retrieved elements must be effective for
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
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String nameParameter = "endpoint.qualifiedName";
        final String anchorGUIDParameter = "anchorGUID";

        invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);

        String typeName = OpenMetadataType.ENDPOINT.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.ENDPOINT.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);


        EndpointBuilder builder = new EndpointBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      networkAddress,
                                                      protocol,
                                                      encryptionMethod,
                                                      additionalProperties,
                                                      typeGUID,
                                                      typeName,
                                                      extendedProperties,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        this.addAnchorGUIDToBuilder(userId,
                                    anchorGUID,
                                    anchorGUIDParameter,
                                    false,
                                    false,
                                    effectiveTime,
                                    supportedZones,
                                    builder,
                                    methodName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        return this.createBeanInRepository(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           effectiveTime,
                                           methodName);
    }


    /**
     * Create a new metadata element to represent a connection using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param templateGUID unique identifier of the metadata element to copy
     * @param qualifiedName unique name for the element - used in other configuration
     * @param displayName short display name for the new element
     * @param description description of the new element
     * @param networkAddress network address
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createEndpointFromTemplate(String userId,
                                             String externalSourceGUID,
                                             String externalSourceName,
                                             String templateGUID,
                                             String qualifiedName,
                                             String displayName,
                                             String description,
                                             String networkAddress,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String qualifiedNameParameterName  = "qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        EndpointBuilder builder = new EndpointBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      networkAddress,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        return this.createBeanFromTemplate(userId,
                                           externalSourceGUID,
                                           externalSourceName,
                                           templateGUID,
                                           templateGUIDParameterName,
                                           OpenMetadataType.ENDPOINT.typeGUID,
                                           OpenMetadataType.ENDPOINT.typeName,
                                           qualifiedName,
                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                           builder,
                                           supportedZones,
                                           true,
                                           false,
                                           null,
                                           methodName);
    }


    /**
     * Retrieves an endpoint based on network address.  Returns it if found.  If not creates a new endpoint and returns the unique identifier
     * for it.
     *
     * @param userId           userId of user making request
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param anchorGUID the unique identifier for the anchor entity (null for unanchored endpoints)
     * @param qualifiedName      unique name of the endpoint
     * @param displayName    human memorable name for the endpoint - does not need to be unique
     * @param description  (optional) description of the endpoint.  Setting a description, particularly in a public endpoint
     *                        makes the endpoint more valuable to other users and can act as an embryonic glossary term
     * @param networkAddress the network address (typically the URL but this depends on the protocol)
     * @param protocol the name of the protocol to use to connect to the endpoint
     * @param encryptionMethod encryption method to use when passing data to this endpoint
     * @param additionalProperties name value pairs for values that are not formally defined in the type system
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return GUID for endpoint
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem adding the endpoint properties to the property server.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    String getEndpointForConnection(String              userId,
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
                                    boolean             forLineage,
                                    boolean             forDuplicateProcessing,
                                    Date                effectiveTime,
                                    String              methodName) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String networkAddressParameterName = "networkAddress";

        List<String>  propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataProperty.NETWORK_ADDRESS.name);

        List<EntityDetail> currentEndpoints = this.getEntitiesByValue(userId,
                                                                      networkAddress,
                                                                      networkAddressParameterName,
                                                                      OpenMetadataType.ENDPOINT.typeGUID,
                                                                      OpenMetadataType.ENDPOINT.typeName,
                                                                      propertyNames,
                                                                      true,
                                                                      false,
                                                                      null,
                                                                      null,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      supportedZones,
                                                                      null,
                                                                      0,
                                                                      invalidParameterHandler.getMaxPagingSize(),
                                                                      effectiveTime,
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
                              null,
                              null,
                              null,
                              null,
                              effectiveTime,
                              methodName);
    }


    /**
     * Updates the properties of an existing endpoint.
     *
     * @param userId          userId of user making request.
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
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
     * @param suppliedTypeName name of the subtype for the endpoint or null for standard type
     * @param extendedProperties any properties for a subtype
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                 String              suppliedTypeName,
                                 Map<String, Object> extendedProperties,
                                 boolean             isMergeUpdate,
                                 Date                effectiveFrom,
                                 Date                effectiveTo,
                                 boolean             forLineage,
                                 boolean             forDuplicateProcessing,
                                 Date                effectiveTime,
                                 String              methodName) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String nameParameter = "qualifiedName";

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, nameParameter, methodName);
        }

        String typeName = OpenMetadataType.ENDPOINT.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataType.ENDPOINT.typeName,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        EndpointBuilder builder = new EndpointBuilder(qualifiedName,
                                                      displayName,
                                                      description,
                                                      networkAddress,
                                                      protocol,
                                                      encryptionMethod,
                                                      additionalProperties,
                                                      typeGUID,
                                                      typeName,
                                                      extendedProperties,
                                                      repositoryHelper,
                                                      serviceName,
                                                      serverName);

        builder.setEffectivityDates(effectiveFrom, effectiveTo);

        this.updateBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    endpointGUID,
                                    endpointGUIDParameterName,
                                    typeGUID,
                                    typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    isMergeUpdate,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Remove the metadata element.  This will delete all elements anchored to it.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param guid unique identifier of the metadata element to remove
     * @param guidParameterName parameter supplying the guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeEndpoint(String  userId,
                               String  externalSourceGUID,
                               String  externalSourceName,
                               String  guid,
                               String  guidParameterName,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        this.deleteBeanInRepository(userId,
                                    externalSourceGUID,
                                    externalSourceName,
                                    guid,
                                    guidParameterName,
                                    OpenMetadataType.ENDPOINT.typeGUID,
                                    OpenMetadataType.ENDPOINT.typeName,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the list of endpoints exactly matching the supplied network address.
     *
     * @param userId the calling user
     * @param networkAddress network address of endpoint
     * @param networkAddressParameterName parameter providing endpoint
     * @param serviceSupportedZones list of supported zones for this service
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        List<String>  propertyNames = new ArrayList<>();

        propertyNames.add(OpenMetadataProperty.NETWORK_ADDRESS.name);

        return this.getBeansByValue(userId,
                                    networkAddress,
                                    networkAddressParameterName,
                                    OpenMetadataType.ENDPOINT.typeGUID,
                                    OpenMetadataType.ENDPOINT.typeName,
                                    propertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    serviceSupportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Return the list of endpoints exactly matching the supplied network address.
     *
     * @param userId the calling user
     * @param networkAddress network address of endpoint
     * @param networkAddressParameterName parameter providing endpoint
     * @param startFrom  index of the list ot start from (0 for start)
     * @param pageSize   maximum number of elements to return
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
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
                                                int          startFrom,
                                                int          pageSize,
                                                boolean      forLineage,
                                                boolean      forDuplicateProcessing,
                                                Date         effectiveTime,
                                                String       methodName) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        return this.getEndpointsByNetworkAddress(userId,
                                                 networkAddress,
                                                 networkAddressParameterName,
                                                 supportedZones,
                                                 startFrom,
                                                 pageSize,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);
    }


    /**
     * Retrieve the list of metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param searchStringParameterName name of parameter supplying the search string
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findEndpoints(String  userId,
                                 String  searchString,
                                 String  searchStringParameterName,
                                 int     startFrom,
                                 int     pageSize,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return this.findBeans(userId,
                              searchString,
                              searchStringParameterName,
                              OpenMetadataType.ENDPOINT.typeGUID,
                              OpenMetadataType.ENDPOINT.typeName,
                              null,
                              startFrom,
                              pageSize,
                              forLineage,
                              forDuplicateProcessing,
                              effectiveTime,
                              methodName);
    }


    /**
     * Retrieve the list of metadata elements with a matching qualified name, display name or network address.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param nameParameterName parameter supplying name
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getEndpointsByName(String  userId,
                                      String  name,
                                      String  nameParameterName,
                                      int     startFrom,
                                      int     pageSize,
                                      boolean forLineage,
                                      boolean forDuplicateProcessing,
                                      Date    effectiveTime,
                                      String  methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        List<String> specificMatchPropertyNames = new ArrayList<>();
        specificMatchPropertyNames.add(OpenMetadataProperty.QUALIFIED_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.DISPLAY_NAME.name);
        specificMatchPropertyNames.add(OpenMetadataProperty.NETWORK_ADDRESS.name);

        return this.getBeansByValue(userId,
                                    name,
                                    nameParameterName,
                                    OpenMetadataType.ENDPOINT.typeGUID,
                                    OpenMetadataType.ENDPOINT.typeName,
                                    specificMatchPropertyNames,
                                    true,
                                    null,
                                    null,
                                    forLineage,
                                    forDuplicateProcessing,
                                    supportedZones,
                                    null,
                                    startFrom,
                                    pageSize,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Retrieve the metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     * @param guidParameterName parameter name of guid
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime  the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getEndpointByGUID(String  userId,
                               String  guid,
                               String  guidParameterName,
                               boolean forLineage,
                               boolean forDuplicateProcessing,
                               Date    effectiveTime,
                               String  methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        return this.getBeanFromRepository(userId,
                                          guid,
                                          guidParameterName,
                                          OpenMetadataType.ENDPOINT.typeName,
                                          forLineage,
                                          forDuplicateProcessing,
                                          supportedZones,
                                          effectiveTime,
                                          methodName);

    }
}
