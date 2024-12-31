/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.server;

import org.odpi.openmetadata.accessservices.securitymanager.converters.SecurityManagerOMASConverter;
import org.odpi.openmetadata.commonservices.ffdc.rest.SecurityManagerRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.ActorProfileHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.commonservices.generichandlers.UserIdentityHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The SecurityManagerRESTServices provides the server-side implementation of the services
 * that are generic for all types of security managers.
 */
public class SecurityManagerRESTServices
{
    private static final SecurityManagerInstanceHandler instanceHandler = new SecurityManagerInstanceHandler();

    private static final RESTCallLogger restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(SecurityManagerRESTServices.class),
                                                                                  instanceHandler.getServiceName());
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public SecurityManagerRESTServices()
    {
    }


    /**
     * Return the connection object for the Security Manager OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public OCFConnectionResponse getOutTopicConnection(String serverName,
                                                       String userId,
                                                       String callerId)
    {
        final String methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse response = new OCFConnectionResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Files live on a file system.  This method creates a top level capability for a file system.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody properties of the file system
     *
     * @return unique identifier for the file system or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GUIDResponse   createSecurityManagerInCatalog(String                     serverName,
                                                         String                     userId,
                                                         SecurityManagerRequestBody requestBody)
    {
        final String methodName = "createSecurityManagerInCatalog";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                SoftwareCapabilityHandler<SecurityManagerElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId,
                                                                                                                         serverName,
                                                                                                                         methodName);

                response.setGUID(handler.createSoftwareCapability(userId,
                                                                  requestBody.getExternalSourceGUID(),
                                                                  requestBody.getExternalSourceName(),
                                                                  requestBody.getTypeName(),
                                                                  null,
                                                                  requestBody.getQualifiedName(),
                                                                  requestBody.getDisplayName(),
                                                                  requestBody.getDescription(),
                                                                  requestBody.getDeployedImplementationType(),
                                                                  requestBody.getVersion(),
                                                                  requestBody.getPatchLevel(),
                                                                  requestBody.getSource(),
                                                                  requestBody.getAdditionalProperties(),
                                                                  requestBody.getExtendedProperties(),
                                                                  requestBody.getVendorProperties(),
                                                                  null,
                                                                  null,
                                                                  false,
                                                                  false,
                                                                  new Date(),
                                                                  methodName));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the unique identifier of the integration daemon service.
     *
     * @param serverName name of the server to route the request to.
     * @param userId calling user
     * @param qualifiedName unique name of the integration daemon
     *
     * @return unique identifier of the integration daemon's software server capability or
     * InvalidParameterException  the bean properties are invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException    problem accessing the property server
     */
    public GUIDResponse  getMetadataSourceGUID(String serverName,
                                               String userId,
                                               String qualifiedName)
    {
        final String methodName = "getMetadataSourceGUID";
        final String parameterName = "qualifiedName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SecurityManagerElement> handler = instanceHandler.getSoftwareCapabilityHandler(userId, serverName, methodName);

            response.setGUID(handler.getBeanGUIDByQualifiedName(userId,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeGUID,
                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                qualifiedName,
                                                                parameterName,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }




    /* ========================================
     * Security Groups
     */

    /**
     * Create a new security group.  The type of the definition is located in the requestBody.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody requestBody of the definition
     *
     * @return unique identifier of the definition or
     *  InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     *  PropertyServerException problem accessing the metadata service
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse createSecurityGroup(String                  serverName,
                                            String                  userId,
                                            SecurityGroupProperties requestBody)
    {
        final String   methodName = "createSecurityGroup";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                GovernanceDefinitionHandler<SecurityGroupElement> handler = instanceHandler.getSecurityGroupHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                String groupGUID = handler.createGovernanceDefinition(userId,
                                                                      requestBody.getDocumentIdentifier(),
                                                                      requestBody.getTitle(),
                                                                      requestBody.getSummary(),
                                                                      requestBody.getDescription(),
                                                                      requestBody.getScope(),
                                                                      requestBody.getDomainIdentifier(),
                                                                      requestBody.getPriority(),
                                                                      requestBody.getImplications(),
                                                                      requestBody.getOutcomes(),
                                                                      requestBody.getResults(),
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      null,
                                                                      requestBody.getDistinguishedName(),
                                                                      requestBody.getAdditionalProperties(),
                                                                      requestBody.getTypeName(),
                                                                      requestBody.getExtendedProperties(),
                                                                      null,
                                                                      null,
                                                                      new Date(),
                                                                      methodName);

                response.setGUID(groupGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update an existing security group.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param requestBody properties to update
     *
     * @return void or
     *  InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateSecurityGroup(String                  serverName,
                                            String                  userId,
                                            String                  securityGroupGUID,
                                            boolean                 isMergeUpdate,
                                            SecurityGroupProperties requestBody)
    {
        final String methodName = "updateSecurityGroup";
        final String guidParameterName = "securityGroupGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                GovernanceDefinitionHandler<SecurityGroupElement> handler = instanceHandler.getSecurityGroupHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler.updateGovernanceDefinition(userId,
                                                   securityGroupGUID,
                                                   guidParameterName,
                                                   requestBody.getDocumentIdentifier(),
                                                   requestBody.getTitle(),
                                                   requestBody.getSummary(),
                                                   requestBody.getDescription(),
                                                   requestBody.getScope(),
                                                   requestBody.getDomainIdentifier(),
                                                   requestBody.getPriority(),
                                                   requestBody.getImplications(),
                                                   requestBody.getOutcomes(),
                                                   requestBody.getResults(),
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   null,
                                                   requestBody.getDistinguishedName(),
                                                   requestBody.getAdditionalProperties(),
                                                   requestBody.getTypeName(),
                                                   requestBody.getExtendedProperties(),
                                                   isMergeUpdate,
                                                   null,
                                                   null,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Delete a specific security group.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique identifier of the definition to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException guid is null or not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse  deleteSecurityGroup(String          serverName,
                                             String          userId,
                                             String          securityGroupGUID,
                                             NullRequestBody requestBody)
    {
        final String methodName = "deleteSecurityGroup";
        final String guidParameterName = "securityGroupGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                GovernanceDefinitionHandler<SecurityGroupElement> handler = instanceHandler.getSecurityGroupHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                handler.removeGovernanceDefinition(userId,
                                                   securityGroupGUID,
                                                   guidParameterName,
                                                   false,
                                                   false,
                                                   new Date(),
                                                   methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of security groups associated with a unique distinguishedName.  In an ideal world, there should be only one.
     *
     * @param serverName called server
     * @param userId calling user
     * @param distinguishedName unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public SecurityGroupsResponse getSecurityGroupsForDistinguishedName(String serverName,
                                                                        String userId,
                                                                        String distinguishedName,
                                                                        int    startFrom,
                                                                        int    pageSize)
    {
        final String   methodName = "getSecurityGroupsForDistinguishedName";
        final String   distinguishedNameParameterName = "distinguishedName";


        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SecurityGroupsResponse response = new SecurityGroupsResponse();
        AuditLog               auditLog = null;

        try
        {
            GovernanceDefinitionHandler<SecurityGroupElement> handler = instanceHandler.getSecurityGroupHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getGovernanceDefinitionsByStringParameter(userId,
                                                                                   OpenMetadataType.SECURITY_GROUP_TYPE_GUID,
                                                                                   OpenMetadataType.SECURITY_GROUP_TYPE_NAME,
                                                                                   distinguishedName,
                                                                                   distinguishedNameParameterName,
                                                                                   OpenMetadataProperty.DISTINGUISHED_NAME.name,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   false,
                                                                                   false,
                                                                                   new Date(),
                                                                                   methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the elements that are governed by the supplied security group.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique name of the security group
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for the associated elements or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public ElementStubsResponse getElementsGovernedBySecurityGroup(String serverName,
                                                                   String userId,
                                                                   String securityGroupGUID,
                                                                   int    startFrom,
                                                                   int    pageSize)
    {
        final String methodName = "getElementsGovernedBySecurityGroup";
        final String guidParameterName = "securityGroupGUID";

        // todo
        return null;
    }


    /**
     * Return the list of security groups that match the search string - this can be a regular expression.
     *
     * @param serverName called server
     * @param userId calling user
     * @param requestBody value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of security groups or
     *  InvalidParameterException one of the parameters is invalid
     *  UserNotAuthorizedException the caller is not authorized to issue the request
     *  PropertyServerException the metadata service has problems
     */
    public SecurityGroupsResponse findSecurityGroups(String                  serverName,
                                                     String                  userId,
                                                     int                     startFrom,
                                                     int                     pageSize,
                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findSecurityGroups";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SecurityGroupsResponse response = new SecurityGroupsResponse();
        AuditLog                 auditLog = null;

        try
        {
            if (requestBody != null)
            {
                GovernanceDefinitionHandler<SecurityGroupElement> handler = instanceHandler.getSecurityGroupHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                response.setElements(handler.findGovernanceDefinitions(userId,
                                                                          OpenMetadataType.SECURITY_GROUP_TYPE_NAME,
                                                                          requestBody.getSearchString(),
                                                                          searchStringParameterName,
                                                                          startFrom,
                                                                          pageSize,
                                                                          false,
                                                                          false,
                                                                          new Date(),
                                                                          methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param securityGroupGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException securityGroupGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public SecurityGroupResponse getSecurityGroupByGUID(String serverName,
                                                        String userId,
                                                        String securityGroupGUID)
    {
        final String methodName        = "getSecurityGroupByGUID";
        final String guidParameterName = "securityGroupGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        SecurityGroupResponse response = new SecurityGroupResponse();
        AuditLog             auditLog = null;

        try
        {
            GovernanceDefinitionHandler<SecurityGroupElement> handler = instanceHandler.getSecurityGroupHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getGovernanceDefinitionByGUID(userId,
                                                                      securityGroupGUID,
                                                                      guidParameterName,
                                                                      false,
                                                                      false,
                                                                      new Date(),
                                                                      methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }




    /**
     * Create a UserIdentity.  This is not connected to a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user
     * @param requestBody userId for the new userIdentity
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse createUserIdentity(String                  serverName,
                                           String                  userId,
                                           UserIdentityRequestBody requestBody)
    {
        final String methodName = "createUserIdentity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof UserIdentityProperties)
                {
                    String userIdentityGUID = handler.createUserIdentity(userId,
                                                                         requestBody.getExternalSourceGUID(),
                                                                         requestBody.getExternalSourceName(),
                                                                         null,
                                                                         null,
                                                                         requestBody.getProperties().getQualifiedName(),
                                                                         requestBody.getProperties().getUserId(),
                                                                         requestBody.getProperties().getDistinguishedName(),
                                                                         requestBody.getProperties().getAdditionalProperties(),
                                                                         requestBody.getProperties().getTypeName(),
                                                                         requestBody.getProperties().getExtendedProperties(),
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);

                    response.setGUID(userIdentityGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(UserIdentityProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Update a UserIdentity.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param isMergeUpdate should the supplied properties be overlaid on the existing properties (true) or replace them (false
     * @param requestBody updated properties for the new userIdentity
     *
     * @return void or
     *  InvalidParameterException one of the parameters is invalid.
     *  PropertyServerException  there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public VoidResponse updateUserIdentity(String                  serverName,
                                           String                  userId,
                                           String                  userIdentityGUID,
                                           boolean                 isMergeUpdate,
                                           UserIdentityRequestBody requestBody)
    {
        final String methodName        = "updateUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                if (requestBody.getProperties() instanceof UserIdentityProperties)
                {
                    handler.updateUserIdentity(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               userIdentityGUID,
                                               guidParameterName,
                                               requestBody.getProperties().getQualifiedName(),
                                               requestBody.getProperties().getUserId(),
                                               requestBody.getProperties().getDistinguishedName(),
                                               requestBody.getProperties().getAdditionalProperties(),
                                               requestBody.getProperties().getTypeName(),
                                               requestBody.getProperties().getExtendedProperties(),
                                               isMergeUpdate,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(UserIdentityProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a user identity object.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse deleteUserIdentity(String                    serverName,
                                           String                    userId,
                                           String                    userIdentityGUID,
                                           ExternalSourceRequestBody requestBody)
    {
        final String methodName        = "deleteUserIdentity";
        final String guidParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.deleteUserIdentity(userId,
                                           requestBody.getExternalSourceGUID(),
                                           requestBody.getExternalSourceName(),
                                           userIdentityGUID,
                                           guidParameterName,
                                           false,
                                           false,
                                           new Date(),
                                           methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Link a user identity to a profile.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID the profile to add the identity to.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse  addIdentityToProfile(String                    serverName,
                                              String                    userId,
                                              String                    userIdentityGUID,
                                              String                    profileGUID,
                                              ExternalSourceRequestBody requestBody)
    {
        final String methodName                    = "addIdentityToProfile";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String profileGUIDParameterName      = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.addIdentityToProfile(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             userIdentityGUID,
                                             userIdentityGUIDParameterName,
                                             profileGUID,
                                             profileGUIDParameterName,
                                             null,
                                             null,
                                             null,
                                             null,
                                             null,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove a user identity object.
     *
     * @param serverName name of target server
     * @param userId the name of the calling user.
     * @param userIdentityGUID unique identifier of the UserIdentity
     * @param profileGUID profile to remove it from.
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException  - there is a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeIdentityFromProfile(String                    serverName,
                                                  String                    userId,
                                                  String                    userIdentityGUID,
                                                  String                    profileGUID,
                                                  ExternalSourceRequestBody requestBody)
    {
        final String methodName                    = "removeIdentityFromProfile";
        final String userIdentityGUIDParameterName = "userIdentityGUID";
        final String profileGUIDParameterName      = "profileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                handler.removeIdentifyFromProfile(userId,
                                                  requestBody.getExternalSourceGUID(),
                                                  requestBody.getExternalSourceName(),
                                                  userIdentityGUID,
                                                  userIdentityGUIDParameterName,
                                                  profileGUID,
                                                  profileGUIDParameterName,
                                                  false,
                                                  false,
                                                  new Date(),
                                                  methodName);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of user identity metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentitiesResponse findUserIdentities(String                  serverName,
                                                     String                  userId,
                                                     int                     startFrom,
                                                     int                     pageSize,
                                                     SearchStringRequestBody requestBody)
    {
        final String methodName                 = "findUserIdentities";
        final String searchStringParameterName  = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        UserIdentitiesResponse response = new UserIdentitiesResponse();
        AuditLog                 auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<UserIdentityElement> elements = handler.findBeans(userId,
                                                                       requestBody.getSearchString(),
                                                                       searchStringParameterName,
                                                                       OpenMetadataType.USER_IDENTITY.typeGUID,
                                                                       OpenMetadataType.USER_IDENTITY.typeName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       null,
                                                                       null,
                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                       null,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);
                response.setElements(elements);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of user identity metadata elements with a matching qualified name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentitiesResponse getUserIdentitiesByName(String          serverName,
                                                          String          userId,
                                                          int             startFrom,
                                                          int             pageSize,
                                                          NameRequestBody requestBody)
    {
        final String methodName         = "getUserIdentitiesByName";
        final String nameParameterName  = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        UserIdentitiesResponse response = new UserIdentitiesResponse();
        AuditLog               auditLog = null;

        try
        {
            if (requestBody != null)
            {
                UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);


                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                List<UserIdentityElement> elements = handler.getUserIdentitiesByName(userId,
                                                                                     requestBody.getName(),
                                                                                     nameParameterName,
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     false,
                                                                                     false,
                                                                                     new Date(),
                                                                                     methodName);
                response.setElements(elements);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the userIdentity metadata element with the supplied unique identifier.
     *
     * @param serverName name of target server
     * @param userId calling user
     * @param userIdentityGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public UserIdentityResponse getUserIdentityByGUID(String serverName,
                                                      String userId,
                                                      String userIdentityGUID)
    {
        final String methodName                    = "getUserIdentityByGUID";
        final String userIdentityGUIDParameterName = "userIdentityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        UserIdentityResponse response = new UserIdentityResponse();
        AuditLog             auditLog = null;

        try
        {
            UserIdentityHandler<UserIdentityElement> handler = instanceHandler.getUserIdentityHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            UserIdentityElement element = handler.getUserIdentityByGUID(userId,
                                                                        userIdentityGUID,
                                                                        userIdentityGUIDParameterName,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName);
            response.setElement(element);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }




    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileGUID unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfileResponse getActorProfileByGUID(String serverName,
                                                      String userId,
                                                      String actorProfileGUID)
    {
        final String methodName        = "getActorProfileByGUID";
        final String guidParameterName = "actorProfileGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfileResponse response = new ActorProfileResponse();
        AuditLog             auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getActorProfileByGUID(userId,
                                                              actorProfileGUID,
                                                              guidParameterName,
                                                              OpenMetadataType.ACTOR_PROFILE.typeName,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a specific actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param actorProfileUserId unique identifier for the actor profile
     *
     * @return properties of the actor profile
     *
     *   InvalidParameterException actorProfileUserId or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfileResponse getActorProfileByUserId(String serverName,
                                                        String userId,
                                                        String actorProfileUserId)
    {
        final String methodName        = "getActorProfileByGUID";
        final String nameParameterName = "actorProfileUserId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfileResponse response = new ActorProfileResponse();
        AuditLog             auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getActorProfileForUser(userId,
                                                               actorProfileUserId,
                                                               nameParameterName,
                                                               OpenMetadataType.ACTOR_PROFILE.typeName,
                                                               false,
                                                               false,
                                                               new Date(),
                                                               methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a named actor profile.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the actor profile
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public ActorProfilesResponse getActorProfileByName(String          serverName,
                                                       String          userId,
                                                       int             startFrom,
                                                       int             pageSize,
                                                       NameRequestBody requestBody)
    {
        final String methodName         = "getActorProfileByName";
        final String nameParameterName  = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog              auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getActorProfilesByName(userId,
                                                                requestBody.getName(),
                                                                nameParameterName,
                                                                OpenMetadataType.ACTOR_PROFILE.typeGUID,
                                                                OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                startFrom,
                                                                pageSize,
                                                                false,
                                                                false,
                                                                requestBody.getEffectiveTime(),
                                                                methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of matching profiles for the search string.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody RegEx string to search for
     *
     * @return list of matching actor profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public ActorProfilesResponse findActorProfile(String                  serverName,
                                                  String                  userId,
                                                  int                     startFrom,
                                                  int                     pageSize,
                                                  SearchStringRequestBody requestBody)
    {
        final String methodName                 = "findActorProfile";
        final String searchStringParameterName  = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ActorProfilesResponse response = new ActorProfilesResponse();
        AuditLog              auditLog = null;

        try
        {
            ActorProfileHandler<ActorProfileElement> handler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.findActorProfiles(userId,
                                                           requestBody.getSearchString(),
                                                           searchStringParameterName,
                                                           OpenMetadataType.ACTOR_PROFILE.typeGUID,
                                                           OpenMetadataType.ACTOR_PROFILE.typeName,
                                                           startFrom,
                                                           pageSize,
                                                           false,
                                                           false,
                                                           requestBody.getEffectiveTime(),
                                                           methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return the list of people appointed to a particular role.
     *
     * @param serverName called server
     * @param userId               calling user
     * @param personRoleGUID       unique identifier of the person role
     * @param startFrom            index of the list to start from (0 for start)
     * @param pageSize             maximum number of elements to return
     * @param requestBody        time for appointments, null for full appointment history
     *
     * @return list of appointees or
     *   InvalidParameterException one of the guids is null or not known
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public AppointeesResponse getAppointees(String                   serverName,
                                            String                   userId,
                                            String                   personRoleGUID,
                                            int                      startFrom,
                                            int                      pageSize,
                                            ResultsRequestBody requestBody)
    {
        final String methodName                  = "getAppointees";
        final String personRoleGUIDParameterName = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AppointeesResponse response = new AppointeesResponse();
        AuditLog           auditLog = null;

        try
        {
            PersonRoleHandler<PersonRoleElement>     roleHandler    = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);
            ActorProfileHandler<ActorProfileElement> profileHandler = instanceHandler.getActorProfileHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                // todo the paging is not right
                List<Relationship> appointmentRelationships = roleHandler.getAttachmentLinks(userId,
                                                                                             personRoleGUID,
                                                                                             personRoleGUIDParameterName,
                                                                                             OpenMetadataType.PERSON_ROLE.typeName,
                                                                                             OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeGUID,
                                                                                             OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                                             null,
                                                                                             OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                                             1,
                                                                                             null,
                                                                                             null,
                                                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                                                             null,
                                                                                             false,
                                                                                             false,
                                                                                             startFrom,
                                                                                             pageSize,
                                                                                             requestBody.getEffectiveTime(),
                                                                                             methodName);
                if (appointmentRelationships != null)
                {
                    List<Appointee>                                   appointees       = new ArrayList<>();
                    OMRSRepositoryHelper                              repositoryHelper = roleHandler.getRepositoryHelper();
                    String                                            serviceName      = roleHandler.getServiceName();
                    SecurityManagerOMASConverter<Appointee>           converter        = new SecurityManagerOMASConverter<>(repositoryHelper, serviceName, serverName);
                    RepositoryErrorHandler                            errorHandler     = new RepositoryErrorHandler(repositoryHelper, serviceName, serverName, auditLog);

                    for (Relationship relationship : appointmentRelationships)
                    {
                        if ((relationship != null) && (relationship.getProperties() != null))
                        {
                            if (requestBody.getEffectiveTime() == null)
                            {
                                Appointee appointee = getAppointeeFromRelationship(userId,
                                                                                   relationship,
                                                                                   profileHandler,
                                                                                   converter,
                                                                                   repositoryHelper,
                                                                                   serviceName,
                                                                                   errorHandler,
                                                                                   methodName);

                                appointees.add(appointee);
                            }
                            else
                            {
                                InstanceProperties properties = relationship.getProperties();
                                Date               effectiveTime = requestBody.getEffectiveTime();

                                /*
                                 * Need to retrieve the appointments that are active
                                 */
                                if (((properties.getEffectiveFromTime() == null) || properties.getEffectiveFromTime().before(effectiveTime)) &&
                                        ((properties.getEffectiveToTime() == null) || properties.getEffectiveToTime().after(effectiveTime)))
                                {
                                    Appointee appointee = getAppointeeFromRelationship(userId,
                                                                                       relationship,
                                                                                       profileHandler,
                                                                                       converter,
                                                                                       repositoryHelper,
                                                                                       serviceName,
                                                                                       errorHandler,
                                                                                       methodName);

                                    appointees.add(appointee);
                                }
                            }
                        }
                        else
                        {
                            errorHandler.logBadRelationship(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                            relationship,
                                                            methodName);
                        }
                    }

                    if (!appointees.isEmpty())
                    {
                        response.setElements(appointees);
                    }
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Extract the appointee from the supplied relationship
     *
     * @param userId calling user
     * @param relationship PersonRoleAppointment relationship
     * @param methodName calling method
     *
     * @return populated appointee
     *
     * @throws InvalidParameterException the unique identifier of the governance role is either null or invalid.
     * @throws PropertyServerException the server is not available.
     * @throws UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    private Appointee getAppointeeFromRelationship(String                                             userId,
                                                   Relationship                                       relationship,
                                                   ActorProfileHandler<ActorProfileElement>           profileHandler,
                                                   SecurityManagerOMASConverter<Appointee>            converter,
                                                   OMRSRepositoryHelper                               repositoryHelper,
                                                   String                                             serviceName,
                                                   RepositoryErrorHandler                             errorHandler,
                                                   String                                             methodName) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        final String profileGUIDParameterName = "profileGUID";

        if ((relationship != null) && (relationship.getProperties() != null) && (relationship.getEntityOneProxy() != null) && (relationship.getEntityTwoProxy() != null))
        {
            Appointee appointee = new Appointee();

            InstanceProperties properties = relationship.getProperties();

            ElementHeader elementHeader = converter.getMetadataElementHeader(Appointee.class,
                                                                             relationship,
                                                                             null,
                                                                             methodName);

            appointee.setElementHeader(elementHeader);
            appointee.setStartDate(properties.getEffectiveFromTime());
            appointee.setEndDate(properties.getEffectiveToTime());
            appointee.setIsPublic(repositoryHelper.getBooleanProperty(serviceName,
                                                                      OpenMetadataProperty.IS_PUBLIC.name,
                                                                      relationship.getProperties(),
                                                                      methodName));


            ActorProfileElement profile = profileHandler.getActorProfileByGUID(userId,
                                                                               relationship.getEntityOneProxy().getGUID(),
                                                                               profileGUIDParameterName,
                                                                               OpenMetadataType.ACTOR_PROFILE.typeName,
                                                                               false,
                                                                               false,
                                                                               new Date(),
                                                                               methodName);

            appointee.setProfile(profile);

            return appointee;
        }
        else
        {
            errorHandler.logBadRelationship(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                            relationship,
                                            methodName);
        }

        return null;
    }




    /**
     * Return information about a specific person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param personRoleGUID unique identifier for the person role
     *
     * @return properties of the person role
     *   InvalidParameterException personRoleGUID or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRoleResponse getPersonRoleByGUID(String serverName,
                                                  String userId,
                                                  String personRoleGUID)
    {
        final String methodName        = "getPersonRoleByGUID";
        final String guidParameterName = "personRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRoleResponse response = new PersonRoleResponse();
        AuditLog           auditLog = null;

        try
        {
            PersonRoleHandler<PersonRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElement(handler.getPersonRoleByGUID(userId,
                                                            personRoleGUID,
                                                            guidParameterName,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Return information about a named person role.
     *
     * @param serverName called server
     * @param userId calling user
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody unique name for the actor profile
     *
     * @return list of matching actor profiles (hopefully only one)
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRolesResponse getPersonRoleByName(String          serverName,
                                                   String          userId,
                                                   int             startFrom,
                                                   int             pageSize,
                                                   NameRequestBody requestBody)
    {
        final String methodName         = "getPersonRoleByName";
        final String nameParameterName  = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRolesResponse response = new PersonRolesResponse();
        AuditLog            auditLog = null;

        try
        {
            PersonRoleHandler<PersonRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.getPersonRolesByName(userId,
                                                              requestBody.getName(),
                                                              nameParameterName,
                                                              startFrom, pageSize,
                                                              false,
                                                              false,
                                                              new Date(),
                                                              methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of matching roles for the search string.
     *
     * @param serverName called server
     * @param userId the name of the calling user.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param requestBody RegEx string to search for
     *
     * @return list of matching actor profiles
     *
     *   InvalidParameterException guid invalid or the external references are not correctly specified, or are null.
     *   PropertyServerException the server is not available.
     *   UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public PersonRolesResponse findPersonRole(String                  serverName,
                                              String                  userId,
                                              int                     startFrom,
                                              int                     pageSize,
                                              SearchStringRequestBody requestBody)
    {
        final String methodName                = "findPersonRole";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRolesResponse response = new PersonRolesResponse();
        AuditLog            auditLog = null;

        try
        {
            PersonRoleHandler<PersonRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElements(handler.findPersonRoles(userId,
                                                         requestBody.getSearchString(),
                                                         searchStringParameterName,
                                                         startFrom,
                                                         pageSize,
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
