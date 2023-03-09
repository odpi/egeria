/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainElement;
import org.odpi.openmetadata.accessservices.governanceprogram.metadataelements.GovernanceDomainSetElement;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomainSetProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDomainListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDomainResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDomainSetListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceDomainSetResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.RelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;

import org.odpi.openmetadata.commonservices.generichandlers.CollectionHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDomainHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;


/**
 * GovernanceDomainRESTServices sets up the governance domains that are part of an organization governance.
 * Each governance domain describes a focus for governance.
 */
public class GovernanceDomainRESTServices
{
    private static final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceDomainRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceDomainRESTServices()
    {
    }


    /* =====================================================================================================================
     * The GovernanceDomainSet entity is the top level element in a collection of related governance domains.
     */


    /**
     * Create a new metadata element to represent the root of a Governance Domain Set.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceDomainSet(String                   serverName,
                                                  String                   userId,
                                                  ReferenceableRequestBody requestBody)
    {
        final String methodName = "createGovernanceDomainSet";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceDomainSetProperties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    CollectionHandler<GovernanceDomainSetElement> handler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

                    GovernanceDomainSetProperties properties = (GovernanceDomainSetProperties) requestBody.getProperties();
                    String setGUID = handler.createCollection(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              properties.getQualifiedName(),
                                                              properties.getDisplayName(),
                                                              properties.getDescription(),
                                                              properties.getAdditionalProperties(),
                                                              properties.getTypeName(),
                                                              properties.getExtendedProperties(),
                                                              OpenMetadataAPIMapper.GOVERNANCE_DOMAIN_SET_CLASSIFICATION_NAME,
                                                              properties.getEffectiveFrom(),
                                                              properties.getEffectiveTo(),
                                                              new Date(),
                                                              methodName);

                    response.setGUID(setGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDomainSetProperties.class.getName(), methodName);
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
     * Update the metadata element representing a Governance Domain Set.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     * @param requestBody new properties for this element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceDomainSet(String                   serverName,
                                                  String                   userId,
                                                  String                   governanceDomainSetGUID,
                                                  ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateGovernanceDomainSet";
        final String guidParameter = "governanceDomainSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceDomainSetProperties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    CollectionHandler<GovernanceDomainSetElement> handler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

                    GovernanceDomainSetProperties properties = (GovernanceDomainSetProperties) requestBody.getProperties();

                    handler.updateCollection(userId,
                                             requestBody.getExternalSourceGUID(),
                                             requestBody.getExternalSourceName(),
                                             governanceDomainSetGUID,
                                             guidParameter,
                                             properties.getQualifiedName(),
                                             properties.getDisplayName(),
                                             properties.getDescription(),
                                             properties.getAdditionalProperties(),
                                             properties.getTypeName(),
                                             properties.getExtendedProperties(),
                                             properties.getEffectiveFrom(),
                                             properties.getEffectiveTo(),
                                             false,
                                             false,
                                             false,
                                             new Date(),
                                             methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDomainSetProperties.class.getName(), methodName);
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
     * Remove the metadata element representing a governanceDomainSet.  The governance domains are not deleted.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse removeGovernanceDomainSet(String                    serverName,
                                                  String                    userId,
                                                  String                    governanceDomainSetGUID,
                                                  ExternalSourceRequestBody requestBody)
    {
        final String methodName = "removeGovernanceDomainSet";
        final String guidParameter = "governanceDomainSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler<GovernanceDomainSetElement> setHandler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                setHandler.removeCollection(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            governanceDomainSetGUID,
                                            guidParameter,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
            }
            else
            {
                setHandler.removeCollection(userId,
                                            null,
                                            null,
                                            governanceDomainSetGUID,
                                            guidParameter,
                                            false,
                                            false,
                                            new Date(),
                                            methodName);
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
     * Retrieve the list of governanceDomainSet metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDomainSetListResponse findGovernanceDomainSets(String                  serverName,
                                                                    String                  userId,
                                                                    int                     startFrom,
                                                                    int                     pageSize,
                                                                    SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceDomainSets";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainSetListResponse response = new GovernanceDomainSetListResponse();
        AuditLog                        auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                CollectionHandler<GovernanceDomainSetElement> handler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

                List<GovernanceDomainSetElement> sets = handler.findCollections(userId,
                                                                                requestBody.getSearchString(),
                                                                                searchStringParameterName,
                                                                                startFrom,
                                                                                pageSize,
                                                                                false,
                                                                                false,
                                                                                new Date(),
                                                                                methodName);

                this.addDomainsToSets(userId, sets, instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName), methodName);

                response.setElements(sets);
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
     * Retrieve the list of governanceDomainSet metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param requestBody name to search for
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDomainSetListResponse getGovernanceDomainSetsByName(String          serverName,
                                                                         String          userId,
                                                                         int             startFrom,
                                                                         int             pageSize,
                                                                         NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceDomainSetsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainSetListResponse response = new GovernanceDomainSetListResponse();
        AuditLog                        auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                CollectionHandler<GovernanceDomainSetElement> handler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

                List<GovernanceDomainSetElement> sets = handler.findCollections(userId,
                                                                                requestBody.getName(),
                                                                                nameParameterName,
                                                                                startFrom,
                                                                                pageSize,
                                                                                false,
                                                                                false,
                                                                                new Date(),
                                                                                methodName);

                this.addDomainsToSets(userId, sets, instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName), methodName);

                response.setElements(sets);
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
     * Retrieve the governanceDomainSet metadata element with the supplied unique identifier.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDomainSetResponse getGovernanceDomainSetByGUID(String serverName,
                                                                    String userId,
                                                                    String governanceDomainSetGUID)
    {
        final String methodName = "getGovernanceDomainSetByGUID";
        final String guidParameterName = "governanceDomainSetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainSetResponse response = new GovernanceDomainSetResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler<GovernanceDomainSetElement> handler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

            GovernanceDomainSetElement set = handler.getCollectionByGUID(userId,
                                                                         governanceDomainSetGUID,
                                                                         guidParameterName,
                                                                         false,
                                                                         false,
                                                                         new Date(),
                                                                         methodName);

            this.addDomainsToSet(userId, set, instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName), methodName);

            response.setElement(set);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =====================================================================================================================
     * A Governance Domain describes an area of focus in the governance program.
     */

    /**
     * Create a new metadata element to represent a governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody new properties for this element
     *
     * @return unique identifier of the new Governance Domain or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GUIDResponse createGovernanceDomain(String                   serverName,
                                               String                   userId,
                                               ReferenceableRequestBody requestBody)
    {
        final String methodName = "createGovernanceDomain";
        final String setGUIDParameter = "setGUID";
        final String domainGUIDParameter = "domainGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceDomainProperties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName);

                    GovernanceDomainProperties properties = (GovernanceDomainProperties) requestBody.getProperties();

                    String domainGUID = domainHandler.createGovernanceDomain(userId,
                                                                             properties.getQualifiedName(),
                                                                             properties.getDisplayName(),
                                                                             properties.getDescription(),
                                                                             properties.getDomainIdentifier(),
                                                                             properties.getAdditionalProperties(),
                                                                             properties.getTypeName(),
                                                                             properties.getExtendedProperties(),
                                                                             new Date(),
                                                                             methodName);

                    if (domainGUID != null)
                    {
                        CollectionHandler<GovernanceDomainSetElement> setHandler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

                        setHandler.addMemberToCollection(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         requestBody.getAnchorGUID(),
                                                         setGUIDParameter,
                                                         domainGUID,
                                                         domainGUIDParameter,
                                                         null,
                                                         properties.getEffectiveFrom(),
                                                         properties.getEffectiveTo(),
                                                         false,
                                                         false,
                                                         new Date(),
                                                         methodName);
                    }

                    response.setGUID(domainGUID);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDomainProperties.class.getName(), methodName);
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
     * Update the metadata element representing a Governance Domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to update
     * @param requestBody new properties for the metadata element
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse updateGovernanceDomain(String                   serverName,
                                               String                   userId,
                                               String                   governanceDomainGUID,
                                               ReferenceableRequestBody requestBody)
    {
        final String methodName = "updateGovernanceDomain";
        final String guidParameter = "governanceDomainGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceDomainProperties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId, serverName,
                                                                                                                                methodName);

                    GovernanceDomainProperties properties = (GovernanceDomainProperties) requestBody.getProperties();

                    domainHandler.updateGovernanceDomain(userId,
                                                         governanceDomainGUID,
                                                         guidParameter,
                                                         properties.getQualifiedName(),
                                                         properties.getDisplayName(),
                                                         properties.getDescription(),
                                                         properties.getDomainIdentifier(),
                                                         properties.getAdditionalProperties(),
                                                         properties.getTypeName(),
                                                         properties.getExtendedProperties(),
                                                         methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceDomainProperties.class.getName(), methodName);
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
     * Remove the metadata element representing a Governance Domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the metadata element to remove
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public VoidResponse deleteGovernanceDomain(String                    serverName,
                                               String                    userId,
                                               String                    governanceDomainGUID,
                                               ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteGovernanceDomain";

        final String guidParameter = "governanceDomainGUID";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName);

            domainHandler.removeGovernanceDomain(userId, governanceDomainGUID, guidParameter, methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Create a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     * @param requestBody relationship request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse addDomainToSet(String                  serverName,
                                       String                  userId,
                                       String                  governanceDomainSetGUID,
                                       String                  governanceDomainGUID,
                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "addDomainToSet";

        final String guid1Parameter = "governanceDomainSetGUID";
        final String guid2Parameter = "governanceDomainGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler<GovernanceDomainSetElement> setHandler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() != null)
                {
                    setHandler.addMemberToCollection(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     governanceDomainSetGUID,
                                                     guid1Parameter,
                                                     governanceDomainGUID,
                                                     guid2Parameter,
                                                     null,
                                                     requestBody.getProperties().getEffectiveFrom(),
                                                     requestBody.getProperties().getEffectiveTo(),
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName);
                }
                else
                {
                    setHandler.addMemberToCollection(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     governanceDomainSetGUID,
                                                     guid1Parameter,
                                                     governanceDomainGUID,
                                                     guid2Parameter,
                                                     null,
                                                     null,
                                                     null,
                                                     false,
                                                     false,
                                                     new Date(),
                                                     methodName);
                }
            }
            else
            {
                setHandler.addMemberToCollection(userId,
                                                 null,
                                                 null,
                                                 governanceDomainSetGUID,
                                                 guid1Parameter,
                                                 governanceDomainGUID,
                                                 guid2Parameter,
                                                 null,
                                                 null,
                                                 null,
                                                 false,
                                                 false,
                                                 new Date(),
                                                 methodName);
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
     * Remove a parent-child relationship between a governance domain set and a governance domain.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainSetGUID unique identifier of the governance domain set
     * @param governanceDomainGUID unique identifier of the governance domain
     * @param requestBody relationship request body
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public VoidResponse removeDomainFromSet(String                    serverName,
                                            String                    userId,
                                            String                    governanceDomainSetGUID,
                                            String                    governanceDomainGUID,
                                            ExternalSourceRequestBody requestBody)
    {
        final String methodName = "removeDomainFromSet";

        final String guid1Parameter = "governanceDomainSetGUID";
        final String guid2Parameter = "governanceDomainGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler<GovernanceDomainSetElement> setHandler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                setHandler.removeMemberFromCollection(userId,
                                                      requestBody.getExternalSourceGUID(),
                                                      requestBody.getExternalSourceName(),
                                                      governanceDomainSetGUID,
                                                      guid1Parameter,
                                                      governanceDomainGUID,
                                                      guid2Parameter,
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName);
            }
            else
            {
                setHandler.removeMemberFromCollection(userId,
                                                      null,
                                                      null,
                                                      governanceDomainSetGUID,
                                                      guid1Parameter,
                                                      governanceDomainGUID,
                                                      guid2Parameter,
                                                      false,
                                                      false,
                                                      new Date(),
                                                      methodName);
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
     * Retrieve the list of Governance Domain metadata elements defined for the governance program.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDomainListResponse getGovernanceDomains(String serverName,
                                                             String userId,
                                                             int    startFrom,
                                                             int    pageSize)
    {
        final String methodName = "getGovernanceDomains";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainListResponse response = new GovernanceDomainListResponse();
        AuditLog                     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId,
                                                                                                                        serverName,
                                                                                                                        methodName);

            response.setElements(domainHandler.getBeansByType(userId,
                                                              OpenMetadataAPIMapper.GOVERNANCE_DOMAIN_TYPE_GUID,
                                                              OpenMetadataAPIMapper.GOVERNANCE_DOMAIN_TYPE_NAME,
                                                              null,
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
     * Retrieve the list of Governance Domain metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName name of the server instance to connect to
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
    public GovernanceDomainListResponse findGovernanceDomains(String                  serverName,
                                                              String                  userId,
                                                              int                     startFrom,
                                                              int                     pageSize,
                                                              SearchStringRequestBody requestBody)
    {
        final String methodName = "findGovernanceDomains";
        final String searchStringParameterName = "searchString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainListResponse response = new GovernanceDomainListResponse();
        AuditLog                     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId,
                                                                                                                            serverName,
                                                                                                                            methodName);

                response.setElements(domainHandler.findGovernanceDomains(userId,
                                                                         requestBody.getSearchString(),
                                                                         searchStringParameterName,
                                                                         startFrom,
                                                                         pageSize,
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
     * Return the list of governance domain sets that a governance domain belong.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the governance domain to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of metadata elements describing the sets associated with the requested governanceDomainSet or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDomainSetListResponse getSetsForGovernanceDomain(String serverName,
                                                                      String userId,
                                                                      String governanceDomainGUID,
                                                                      int    startFrom,
                                                                      int    pageSize)
    {
        final String methodName = "getSetsForGovernanceDomain";

        final String guidParameterName = "governanceDomainGUID";
        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainSetListResponse response = new GovernanceDomainSetListResponse();
        AuditLog                        auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            CollectionHandler<GovernanceDomainSetElement> handler = instanceHandler.getGovernanceDomainSetHandler(userId, serverName, methodName);

            List<GovernanceDomainSetElement> sets = handler.getAttachedElements(userId,
                                                                                governanceDomainGUID,
                                                                                guidParameterName,
                                                                                OpenMetadataAPIMapper.GOVERNANCE_DOMAIN_TYPE_NAME,
                                                                                OpenMetadataAPIMapper.COLLECTION_MEMBERSHIP_TYPE_GUID,
                                                                                OpenMetadataAPIMapper.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                                                                OpenMetadataAPIMapper.COLLECTION_TYPE_NAME,
                                                                                null,
                                                                                null,
                                                                                1,
                                                                                false,
                                                                                false,
                                                                                startFrom,
                                                                                pageSize,
                                                                                new Date(),
                                                                                methodName);

            this.addDomainsToSets(userId, sets, instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName), methodName);

            response.setElements(sets);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the linked domains to each Governance Domain Set in the list.
     *
     * @param userId calling user
     * @param sets sets to update
     * @param domainHandler handler to request the domain information from
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void addDomainsToSets(String                                           userId,
                                  List<GovernanceDomainSetElement>                 sets,
                                  GovernanceDomainHandler<GovernanceDomainElement> domainHandler,
                                  String                                           methodName) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        if (sets != null)
        {
            for (GovernanceDomainSetElement setElement : sets)
            {
                addDomainsToSet(userId, setElement, domainHandler, methodName);
            }
        }
    }


    /**
     * Add the linked domains to a Governance Domain Set.
     *
     * @param userId calling user
     * @param set set to update
     * @param domainHandler handler to request the domain information from
     * @param methodName calling method
     *
     * @throws InvalidParameterException  the input properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    private void addDomainsToSet(String                                           userId,
                                 GovernanceDomainSetElement                       set,
                                 GovernanceDomainHandler<GovernanceDomainElement> domainHandler,
                                 String                                           methodName) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String guidParameterName = "set.getElementHeader().getGUID()";

        List<GovernanceDomainElement> domains = domainHandler.getDomainsInSet(userId,
                                                                              set.getElementHeader().getGUID(),
                                                                              guidParameterName,
                                                                              0,
                                                                              0,
                                                                              false,
                                                                              false,
                                                                              new Date(),
                                                                              methodName);

        set.setDomains(domains);
    }


    /**
     * Retrieve the list of Governance Domain metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the server instance to connect to
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
    public GovernanceDomainListResponse getGovernanceDomainsByName(String          serverName,
                                                                   String          userId,
                                                                   int             startFrom,
                                                                   int             pageSize,
                                                                   NameRequestBody requestBody)
    {
        final String methodName = "getGovernanceDomainsByName";
        final String nameParameterName = "name";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainListResponse response = new GovernanceDomainListResponse();
        AuditLog                     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId,
                                                                                                                            serverName,
                                                                                                                            methodName);

                response.setElements(domainHandler.getGovernanceDomainsByName(userId,
                                                                              requestBody.getName(),
                                                                              nameParameterName,
                                                                              startFrom,
                                                                              pageSize,
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
     * Retrieve the Governance Domain metadata element with the supplied unique identifier assigned when the domain description was stored in
     * the metadata repository.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param governanceDomainGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDomainResponse getGovernanceDomainByGUID(String serverName,
                                                              String userId,
                                                              String governanceDomainGUID)
    {
        final String methodName = "getGovernanceDomainByGUID";
        final String guidParameterName = "governanceDomainGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainResponse response = new GovernanceDomainResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName);

            response.setElement(domainHandler.getGovernanceDomainByGUID(userId, governanceDomainGUID, guidParameterName, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the Governance Domain metadata element with the supplied domain identifier.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param domainIdentifier identifier used to identify the domain
     *
     * @return requested metadata element or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public GovernanceDomainResponse getGovernanceDomainByIdentifier(String serverName,
                                                                    String userId,
                                                                    int    domainIdentifier)
    {
        final String methodName = "getGovernanceDomainByIdentifier";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDomainResponse response = new GovernanceDomainResponse();
        AuditLog                 auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDomainHandler<GovernanceDomainElement> domainHandler = instanceHandler.getGovernanceDomainHandler(userId, serverName, methodName);

            response.setElement(domainHandler.getGovernanceDomainByDomainIdentifier(userId, domainIdentifier, methodName));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
