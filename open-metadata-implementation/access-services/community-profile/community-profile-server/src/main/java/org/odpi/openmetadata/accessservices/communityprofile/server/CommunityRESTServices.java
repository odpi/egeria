/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.server;


import org.odpi.openmetadata.accessservices.communityprofile.rest.TemplateRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.CommunityMembershipType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.communities.CommunityProperties;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.CommunityHandler;
import org.odpi.openmetadata.commonservices.generichandlers.PersonRoleHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * CommunityRESTServices provides the API operations to create and maintain community information.
 */
public class CommunityRESTServices
{
    private static final CommunityProfileInstanceHandler   instanceHandler     = new CommunityProfileInstanceHandler();
    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(CommunityRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public CommunityRESTServices()
    {
    }


    /**
     * Create a new metadata element to represent a community.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createCommunity(String                   serverName,
                                        String                   userId,
                                        ReferenceableRequestBody requestBody)
    {
        final String methodName = "createCommunity";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CommunityProperties properties)
                {
                    String communityGUID = handler.createCommunity(userId,
                                                                   requestBody.getExternalSourceGUID(),
                                                                   requestBody.getExternalSourceName(),
                                                                   properties.getQualifiedName(),
                                                                   properties.getName(),
                                                                   properties.getDescription(),
                                                                   properties.getMission(),
                                                                   properties.getAdditionalProperties(),
                                                                   properties.getTypeName(),
                                                                   properties.getExtendedProperties(),
                                                                   properties.getEffectiveFrom(),
                                                                   properties.getEffectiveTo(),
                                                                   new Date(),
                                                                   methodName);

                    if (communityGUID != null)
                    {
                        handler.setVendorProperties(userId,
                                                    communityGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }

                    response.setGUID(communityGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommunityProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Create a new metadata element to represent a community using an existing metadata element as a template.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param templateGUID unique identifier of the metadata element to copy
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createCommunityFromTemplate(String              serverName,
                                                    String              userId,
                                                    String              templateGUID,
                                                    TemplateRequestBody requestBody)
    {
        final String methodName = "createCommunityFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                String communityGUID;

                communityGUID = handler.createCommunityFromTemplate(userId,
                                                                    requestBody.getExternalSourceGUID(),
                                                                    requestBody.getExternalSourceName(),
                                                                    templateGUID,
                                                                    requestBody.getQualifiedName(),
                                                                    requestBody.getDisplayName(),
                                                                    requestBody.getDescription(),
                                                                    methodName);

                response.setGUID(communityGUID);
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Update the metadata element representing a community.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with the existing properties of overlay them?
     * @param requestBody new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateCommunity(String                   serverName,
                                        String                   userId,
                                        String                   communityGUID,
                                        boolean                  isMergeUpdate,
                                        ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateCommunity";
        final String communityGUIDParameterName = "communityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CommunityProperties properties)
                {
                    handler.updateCommunity(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            communityGUID,
                                            communityGUIDParameterName,
                                            properties.getQualifiedName(),
                                            properties.getName(),
                                            properties.getDescription(),
                                            properties.getMission(),
                                            properties.getAdditionalProperties(),
                                            properties.getTypeName(),
                                            properties.getExtendedProperties(),
                                            properties.getEffectiveFrom(),
                                            properties.getEffectiveTo(),
                                            isMergeUpdate,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);

                    if ((! isMergeUpdate) || (properties.getVendorProperties() != null))
                    {
                        handler.setVendorProperties(userId,
                                                    communityGUID,
                                                    properties.getVendorProperties(),
                                                    false,
                                                    false,
                                                    new Date(),
                                                    methodName);
                    }
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommunityProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Create a relationship between a community and a person role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the community in the external data manager
     * @param communityRoleGUID unique identifier of the person role in the external data manager
     * @param requestBody relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse setupCommunityRole(String                  serverName,
                                           String                  userId,
                                           String                  communityGUID,
                                           String                  communityRoleGUID,
                                           RelationshipRequestBody requestBody)
    {
        final String methodName                     = "setupCommunityRole";
        final String communityGUIDParameterName     = "communityGUID";
        final String communityRoleGUIDParameterName = "communityRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                int ordinal = CommunityMembershipType.CONTRIBUTOR.getOrdinal();

                if (requestBody.getProperties() instanceof CommunityMembershipProperties properties)
                {
                    if (properties.getMembershipType() != null)
                    {
                        ordinal = properties.getMembershipType().getOrdinal();
                    }

                    handler.addRoleToCommunity(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               communityGUID,
                                               communityGUIDParameterName,
                                               communityRoleGUID,
                                               communityRoleGUIDParameterName,
                                               ordinal,
                                               properties.getEffectiveFrom(),
                                               properties.getEffectiveTo(),
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addRoleToCommunity(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               communityGUID,
                                               communityGUIDParameterName,
                                               communityRoleGUID,
                                               communityRoleGUIDParameterName,
                                               ordinal,
                                               null,
                                               null,
                                               false,
                                               false,
                                               new Date(),
                                               methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CommunityMembershipProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove a relationship between a community and a role.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the community in the external data manager
     * @param communityRoleGUID unique identifier of the role in the external data manager
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse clearCommunityRole(String                    serverName,
                                           String                    userId,
                                           String                    communityGUID,
                                           String                    communityRoleGUID,
                                           ExternalSourceRequestBody requestBody)
    {
        final String methodName                     = "clearCommunityRole";
        final String communityGUIDParameterName    = "communityGUID";
        final String communityRoleGUIDParameterName = "communityRoleGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeRoleFromCommunity(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                communityGUID,
                                                communityGUIDParameterName,
                                                communityRoleGUID,
                                                communityRoleGUIDParameterName,
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the metadata element representing a community.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param communityGUID unique identifier of the metadata element to remove
     * @param requestBody external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeCommunity(String                    serverName,
                                        String                    userId,
                                        String                    communityGUID,
                                        ExternalSourceRequestBody requestBody)
    {
        final String methodName = "removeCommunity";
        final String communityGUIDParameterName = "communityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

                CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

                handler.removeCommunity(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         communityGUID,
                                         communityGUIDParameterName,
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of community metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CommunitiesResponse findCommunities(String                  serverName,
                                               String                  userId,
                                               SearchStringRequestBody requestBody,
                                               int                     startFrom,
                                               int                     pageSize)
    {
        final String methodName = "findCommunities";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommunitiesResponse response = new CommunitiesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

                List<CommunityElement> communities = handler.findCommunities(userId,
                                                                              requestBody.getSearchString(),
                                                                              searchStringParameterName,
                                                                              startFrom,
                                                                              pageSize,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

                response.setElements(setUpVendorProperties(userId, communities, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the list of community metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param requestBody name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CommunitiesResponse getCommunitiesByName(String          serverName,
                                                    String          userId,
                                                    NameRequestBody requestBody,
                                                    int             startFrom,
                                                    int             pageSize)
    {
        final String methodName = "getCommunitiesByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommunitiesResponse response = new CommunitiesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

                List<CommunityElement> communities = handler.getCommunitiesByName(userId,
                                                                                   requestBody.getName(),
                                                                                   nameParameterName,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   false,
                                                                                   false,
                                                                                   new Date(),
                                                                                   methodName);

                response.setElements(setUpVendorProperties(userId, communities, handler, methodName));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Return information about a person role connected to the named community.
     *
     * @param serverName called server
     * @param userId calling user
     * @param communityGUID unique identifier for the community
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of matching person roles
     *
     *   InvalidParameterException name or userId is null
     *   PropertyServerException problem accessing property server
     *   UserNotAuthorizedException security access problem
     */
    public PersonRolesResponse getRolesForCommunity(String          serverName,
                                                    String          userId,
                                                    String          communityGUID,
                                                    int             startFrom,
                                                    int             pageSize)
    {
        final String methodName         = "getRolesForCommunity";
        final String guidParameterName  = "communityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        PersonRolesResponse response = new PersonRolesResponse();
        AuditLog            auditLog = null;

        try
        {
            PersonRoleHandler<ActorRoleElement> handler = instanceHandler.getPersonRoleHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElements(handler.getCommunityRoles(userId,
                                                            communityGUID,
                                                            guidParameterName,
                                                            startFrom,
                                                            pageSize,
                                                            false,
                                                            false,
                                                            new Date(),
                                                            methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the list of community metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CommunitiesResponse getCommunities(String          serverName,
                                              String          userId,
                                              int             startFrom,
                                              int             pageSize)
    {
        final String methodName = "getCommunities";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommunitiesResponse response = new CommunitiesResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

            List<CommunityElement> communities = handler.getCommunities(userId,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName);

            response.setElements(setUpVendorProperties(userId, communities, handler, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Retrieve the community metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param guid unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public CommunityResponse getCommunityByGUID(String serverName,
                                                String userId,
                                                String guid)
    {
        final String methodName = "getCommunityByGUID";
        final String guidParameterName = "communityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        CommunityResponse response = new CommunityResponse();
        AuditLog          auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            CommunityHandler<CommunityElement> handler = instanceHandler.getCommunityHandler(userId, serverName, methodName);

            CommunityElement community = handler.getCommunityByGUID(userId,
                                                                       guid,
                                                                       guidParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName);

            response.setElement(setUpVendorProperties(userId, community, handler, methodName));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }



    /**
     * Set up the vendor properties in the retrieved elements.
     *
     * @param userId calling user
     * @param retrievedResults results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private List<CommunityElement> setUpVendorProperties(String                              userId,
                                                          List<CommunityElement>             retrievedResults,
                                                          CommunityHandler<CommunityElement> handler,
                                                          String                             methodName) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        if (retrievedResults != null)
        {
            for (CommunityElement element : retrievedResults)
            {
                if (element != null)
                {
                    setUpVendorProperties(userId, element, handler, methodName);
                }
            }
        }

        return retrievedResults;
    }


    /**
     * Set up the vendor properties in the retrieved element.
     *
     * @param userId calling user
     * @param element results from the repositories
     * @param handler handler used to retrieve the vendor properties
     * @param methodName calling method
     *
     * @return updated results
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    private CommunityElement setUpVendorProperties(String                             userId,
                                                   CommunityElement                   element,
                                                   CommunityHandler<CommunityElement> handler,
                                                   String                             methodName) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String elementGUIDParameterName = "element.getElementHeader().getGUID()";

        if (element != null)
        {
            CommunityProperties properties = element.getProperties();

            properties.setVendorProperties(handler.getVendorProperties(userId,
                                                                       element.getElementHeader().getGUID(),
                                                                       elementGUIDParameterName,
                                                                       false,
                                                                       false,
                                                                       new Date(),
                                                                       methodName));
        }

        return element;
    }
}
