/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.metadataelements.SecurityGroupElement;
import org.odpi.openmetadata.accessservices.communityprofile.properties.SecurityGroupProperties;
import org.odpi.openmetadata.accessservices.communityprofile.rest.ElementStubsResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.SecurityGroupResponse;
import org.odpi.openmetadata.accessservices.communityprofile.rest.SecurityGroupsResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.slf4j.LoggerFactory;

import java.util.Date;


/**
 * The OrganizationRESTServices provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) capability for organization management.  This interface provides support for creating all types of actor profiles and
 * associated user roles in order to describe the structure of an organization.
 */
public class SecurityGroupRESTServices
{
    private static final CommunityProfileInstanceHandler instanceHandler = new CommunityProfileInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(SecurityGroupRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler   restExceptionHandler = new RESTExceptionHandler();


    /**
     * Default constructor
     */
    public SecurityGroupRESTServices()
    {
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
    public VoidResponse  updateSecurityGroup(String                  serverName,
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
        AuditLog                 auditLog = null;

        try
        {
            GovernanceDefinitionHandler<SecurityGroupElement> handler = instanceHandler.getSecurityGroupHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setElementList(handler.getGovernanceDefinitionsByStringParameter(userId,
                                                                                      OpenMetadataType.SECURITY_GROUP_TYPE_GUID,
                                                                                      OpenMetadataType.SECURITY_GROUP_TYPE_NAME,
                                                                                      distinguishedName,
                                                                                      distinguishedNameParameterName,
                                                                                      OpenMetadataType.DISTINGUISHED_NAME_PROPERTY_NAME,
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
                response.setElementList(handler.findGovernanceDefinitions(userId,
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
}
