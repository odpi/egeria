/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.metadataexpert.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.metadataexpert.rest.MatchCriteriaListResponse;
import org.odpi.openmetadata.viewservices.metadataexpert.rest.PropertyComparisonOperatorListResponse;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * The MetadataExpertRESTServices provides the server-side implementation of the Metadata Expert Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class MetadataExpertRESTServices extends TokenController
{
    private static final MetadataExpertInstanceHandler instanceHandler = new MetadataExpertInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(MetadataExpertRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public MetadataExpertRESTServices()
    {
    }



    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     * This version of the method allows access to advanced features such as multiple states and
     * effectivity dates.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse createMetadataElementInStore(String                            serverName,
                                                     String                            urlMarker,
                                                     NewOpenMetadataElementRequestBody requestBody)
    {
        final String methodName = "createMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createMetadataElementInStore(userId,
                                                                      requestBody.getTypeName(),
                                                                      requestBody,
                                                                      requestBody.getInitialClassifications(),
                                                                      requestBody.getProperties(),
                                                                      requestBody.getParentRelationshipProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a new metadata element in the metadata store using a template.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the new element
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException the type name, status or one of the properties is invalid
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of element
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse createMetadataElementFromTemplate(String                          serverName,
                                                          String                          urlMarker,
                                                          OpenMetadataTemplateRequestBody requestBody)
    {
        final String methodName = "createMetadataElementFromTemplate";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createMetadataElementFromTemplate(userId,
                                                                           requestBody.getTypeName(),
                                                                           requestBody,
                                                                           requestBody.getTemplateGUID(),
                                                                           requestBody.getReplacementProperties(),
                                                                           requestBody.getReplacementClassifications(),
                                                                           requestBody.getPlaceholderPropertyValues(),
                                                                           requestBody.getParentRelationshipProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a specific metadata element.  The properties must match the type definition associated with the
     * metadata element when it was created.  However, it is possible to update a few properties, or replace all them by
     * the value used in the replaceProperties flag.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new properties
     *
     * @return boolean or
     *  InvalidParameterException either the unique identifier or the properties are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    public BooleanResponse updateMetadataElementInStore(String                      serverName,
                                                        String                      urlMarker,
                                                        String                      metadataElementGUID,
                                                        UpdatePropertiesRequestBody requestBody)
    {
        final String methodName = "updateMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse response = new BooleanResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setFlag(handler.updateMetadataElementInStore(userId,
                                                                      metadataElementGUID,
                                                                      requestBody,
                                                                      requestBody.getProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the zone membership to increase its visibility.  The publishZones  are defined in the user directory.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the user is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse publishMetadataElement(String                    serverName,
                                               String                    urlMarker,
                                               String                    metadataElementGUID,
                                               MetadataSourceRequestBody requestBody)
    {
        final String methodName = "publishMetadataElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            handler.publishMetadataElement(userId, metadataElementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the zone membership to reduce its visibility.  The defaultZones are defined in the user directory.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the user is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse withdrawMetadataElement(String                    serverName,
                                                String                    urlMarker,
                                                String                    metadataElementGUID,
                                                MetadataSourceRequestBody requestBody)
    {
        final String methodName = "withdrawMetadataElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            handler.withdrawMetadataElement(userId, metadataElementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the effectivity dates control the visibility of the element through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody new status values - use null to leave as is
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse updateMetadataElementEffectivityInStore(String                            serverName,
                                                                String                            urlMarker,
                                                                String                            metadataElementGUID,
                                                                UpdateEffectivityDatesRequestBody requestBody)
    {
        final String methodName = "updateMetadataElementEffectivityInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.updateMetadataElementEffectivityInStore(userId,
                                                                metadataElementGUID,
                                                                requestBody,
                                                                requestBody.getEffectiveFrom(),
                                                                requestBody.getEffectiveTo());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody delete request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this element
     *  PropertyServerException a problem with the metadata store
     */
    public  VoidResponse deleteMetadataElementInStore(String                        serverName,
                                                      String                        urlMarker,
                                                      String                        metadataElementGUID,
                                                      OpenMetadataDeleteRequestBody requestBody)
    {
        final String methodName = "deleteMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteMetadataElementInStore(userId, metadataElementGUID, requestBody);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Archive a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to archive this element
     *  PropertyServerException a problem with the metadata store
     */
    public  VoidResponse archiveMetadataElementInStore(String                   serverName,
                                                       String                   urlMarker,
                                                       String                   metadataElementGUID,
                                                       DeleteElementRequestBody requestBody)
    {
        final String methodName = "archiveMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.archiveMetadataElementInStore(userId,
                                                      metadataElementGUID,
                                                      requestBody);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Add a new classification to the metadata element.  Note that only one classification with the same name can be attached to
     * a metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName name of the classification to add (if the classification is already present then use reclassify)
     * @param requestBody properties to store in the new classification.  These must conform to the valid properties associated with the
     *                   classification name
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse classifyMetadataElementInStore(String                                   serverName,
                                                       String                                   urlMarker,
                                                       String                                   metadataElementGUID,
                                                       String                                   classificationName,
                                                       NewOpenMetadataClassificationRequestBody requestBody)
    {
        final String methodName = "classifyMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.classifyMetadataElementInStore(userId,
                                                       metadataElementGUID,
                                                       classificationName,
                                                       requestBody,
                                                       requestBody.getProperties());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties of a classification that is currently attached to a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody new properties for the classification
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way; properties do not match the
     *                                   valid properties associated with the classification's type definition
     *  UserNotAuthorizedException the governance action service is not authorized to update this element/classification
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse reclassifyMetadataElementInStore(String                      serverName,
                                                         String                      urlMarker,
                                                         String                      metadataElementGUID,
                                                         String                      classificationName,
                                                         UpdatePropertiesRequestBody requestBody)
    {
        final String methodName = "reclassifyMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.reclassifyMetadataElementInStore(userId,
                                                         metadataElementGUID,
                                                         classificationName,
                                                         requestBody,
                                                         requestBody.getProperties());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the effectivity dates of a specific classification attached to a metadata element.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse updateClassificationEffectivityInStore(String                            serverName,
                                                               String                            urlMarker,
                                                               String                            metadataElementGUID,
                                                               String                            classificationName,
                                                               UpdateEffectivityDatesRequestBody requestBody)
    {
        final String methodName = "updateClassificationEffectivityInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.updateClassificationEffectivityInStore(userId,
                                                               metadataElementGUID,
                                                               classificationName,
                                                               requestBody,
                                                               requestBody.getEffectiveFrom(),
                                                               requestBody.getEffectiveTo());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the named classification from a specific metadata element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param metadataElementGUID unique identifier of the metadata element to update
     * @param classificationName unique name of the classification to remove
     * @param requestBody null request body
     *
     * @return void or
     *  InvalidParameterException the unique identifier or classification name is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to remove this classification
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse declassifyMetadataElementInStore(String                    serverName,
                                                         String                    urlMarker,
                                                         String                    metadataElementGUID,
                                                         String                    classificationName,
                                                         MetadataSourceRequestBody requestBody)
    {
        final String methodName = "declassifyMetadataElementInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.declassifyMetadataElementInStore(userId,
                                                         metadataElementGUID,
                                                         classificationName,
                                                         requestBody);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a relationship between two metadata elements.  It is important to put the right element at each end of the relationship
     * according to the type definition since this will affect how the relationship is interpreted.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody the properties of the relationship
     *
     * @return unique identifier of the new relationship or
     *  InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     *  PropertyServerException a problem with the metadata store
     */
    public GUIDResponse createRelatedElementsInStore(String                        serverName,
                                                     String                        urlMarker,
                                                     NewRelatedElementsRequestBody requestBody)
    {
        final String methodName = "createRelatedElementsInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setGUID(handler.createRelatedElementsInStore(userId,
                                                                      requestBody.getTypeName(),
                                                                      requestBody.getMetadataElement1GUID(),
                                                                      requestBody.getMetadataElement2GUID(),
                                                                      requestBody,
                                                                      requestBody.getProperties()));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the properties associated with a relationship.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody new properties for the relationship
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     *  UserNotAuthorizedException the governance action service is not authorized to update this relationship
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse updateRelatedElementsInStore(String                      serverName,
                                                     String                      urlMarker,
                                                     String                      relationshipGUID,
                                                     UpdatePropertiesRequestBody requestBody)
    {
        final String methodName = "updateRelatedElementsInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.updateRelationshipInStore(userId,
                                                  relationshipGUID,
                                                  requestBody,
                                                  requestBody.getProperties());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the effectivity dates of a specific relationship between metadata elements.
     * The effectivity dates control the visibility of the classification through specific APIs.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship to update
     * @param requestBody the dates when this element is active / inactive - null for no restriction
     *
     * @return void or
     *  InvalidParameterException either the unique identifier or the status are invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to update this element
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse updateRelatedElementsEffectivityInStore(String                            serverName,
                                                                String                            urlMarker,
                                                                String                            relationshipGUID,
                                                                UpdateEffectivityDatesRequestBody requestBody)
    {
        final String methodName = "updateRelatedElementsEffectivityInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.updateRelationshipEffectivityInStore(userId,
                                                             relationshipGUID,
                                                             requestBody,
                                                             requestBody.getEffectiveFrom(),
                                                             requestBody.getEffectiveTo());
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete a relationship between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier of the relationship to delete
     * @param requestBody  options to control access to open metadata
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse deleteRelatedElementsInStore(String                        serverName,
                                                     String                        urlMarker,
                                                     String                        relationshipGUID,
                                                     OpenMetadataDeleteRequestBody requestBody)
    {
        final String methodName = "deleteRelatedElementsInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.deleteRelationshipInStore(userId,
                                                  relationshipGUID,
                                                  requestBody);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Delete all relationships of a particular type between two metadata elements.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param relationshipTypeName name of the type of relationship to create.  This will determine the types of metadata elements that can be
     *                             related and the properties that can be associated with this relationship.
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param requestBody  options to control access to open metadata
     *
     * @return void or
     *  InvalidParameterException the unique identifier of the relationship is null or invalid in some way
     *  UserNotAuthorizedException the governance action service is not authorized to delete this relationship
     *  PropertyServerException a problem with the metadata store
     */
    public VoidResponse detachRelatedElementsInStore(String                        serverName,
                                                     String                        urlMarker,
                                                     String                        relationshipTypeName,
                                                     String                        metadataElement1GUID,
                                                     String                        metadataElement2GUID,
                                                     OpenMetadataDeleteRequestBody requestBody)
    {
        final String methodName = "detachRelatedElementsInStore";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                handler.detachRelatedElementsInStore(userId,
                                                     relationshipTypeName,
                                                     metadataElement1GUID,
                                                     metadataElement2GUID,
                                                     requestBody);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param urlMarker  view service URL marker

     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByGUID(String         serverName,
                                                                String         elementGUID,
                                                                String         urlMarker,
                                                                GetRequestBody requestBody)
    {
        final String methodName = "getMetadataElementByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog                    auditLog = null;
        OpenMetadataElementResponse response = new OpenMetadataElementResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getMetadataElementByGUID(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataElementResponse getMetadataElementByUniqueName(String                serverName,
                                                                      String                urlMarker,
                                                                      UniqueNameRequestBody requestBody)
    {
        final String methodName = "getMetadataElementByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        OpenMetadataElementResponse response = new OpenMetadataElementResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setElement(handler.getMetadataElementByUniqueName(userId,
                                                                           requestBody.getName(),
                                                                           requestBody.getNamePropertyName(),
                                                                           requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody unique name for the metadata element
     *
     * @return metadata element unique identifier (guid) or
     *  InvalidParameterException the unique identifier is null or not known or
     *  UserNotAuthorizedException the governance action service is not able to access the element or
     *  PropertyServerException a problem accessing the metadata store
     */
    public GUIDResponse getMetadataElementGUIDByUniqueName(String                serverName,
                                                           String                urlMarker,
                                                           UniqueNameRequestBody requestBody)
    {
        final String methodName = "getMetadataElementGUIDByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        GUIDResponse response = new GUIDResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setGUID(handler.getMetadataElementGUIDByUniqueName(userId,
                                                                            requestBody.getName(),
                                                                            requestBody.getNamePropertyName(),
                                                                            requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody the time window required
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse getMetadataElementHistory(String             serverName,
                                                                  String             urlMarker,
                                                                  String             elementGUID,
                                                                  HistoryRequestBody requestBody)
    {
        final String methodName = "getMetadataElementHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getMetadataElementHistory(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve all the versions of an element's classification.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the metadata element
     * @param classificationName name of the classification to retrieve
     * @param urlMarker  view service URL marker
     * @param requestBody the time window required
     *
     * @return list of matching classifications (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public AttachedClassificationsResponse getClassificationHistory(String             serverName,
                                                                    String             urlMarker,
                                                                    String             elementGUID,
                                                                    String             classificationName,
                                                                    HistoryRequestBody requestBody)
    {
        final String methodName = "getMetadataElementHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        AttachedClassificationsResponse response = new AttachedClassificationsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setClassifications(handler.getClassificationHistory(userId, elementGUID, classificationName, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the metadata elements that contain the requested string.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse findMetadataElementsWithString(String                  serverName,
                                                                       String                  urlMarker,
                                                                       SearchStringRequestBody requestBody)
    {
        final String methodName = "findMetadataElementsWithString";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findMetadataElementsWithString(userId,
                                                                            requestBody.getSearchString(),
                                                                            requestBody));
            }
            else
            {
                response.setElements(handler.findMetadataElementsWithString(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }




    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied anchorGUID.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param anchorGUID unique identifier of anchor
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public AnchorSearchMatchesResponse findElementsForAnchor(String                  serverName,
                                                             String                  urlMarker,
                                                             String                  anchorGUID,
                                                             SearchStringRequestBody requestBody)
    {
        final String methodName = "findElementsForAnchor";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        AnchorSearchMatchesResponse response = new AnchorSearchMatchesResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setElement(handler.findElementsForAnchor(userId,
                                                                  requestBody.getSearchString(),
                                                                  anchorGUID,
                                                                  requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied domain name. The results are organized by anchor element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param anchorDomainName name of open metadata type for the domain
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public AnchorSearchMatchesListResponse findElementsInAnchorDomain(String                  serverName,
                                                                      String                  urlMarker,
                                                                      String                  anchorDomainName,
                                                                      SearchStringRequestBody requestBody)
    {
        final String methodName = "findElementsInAnchorDomain";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        AnchorSearchMatchesListResponse response = new AnchorSearchMatchesListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setElements(handler.findElementsInAnchorDomain(userId,
                                                                        requestBody.getSearchString(),
                                                                        anchorDomainName,
                                                                        requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return a list of elements with the requested search string in their (display, resource)name, qualified name,
     * title, text, summary, identifier or description.  The search string is interpreted as a regular expression (RegEx).
     * The breadth of the search is determined by the supplied scope guid. The results are organized by anchor element.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param anchorScopeGUID unique identifier of the scope to use
     * @param requestBody searchString  to retrieve
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public AnchorSearchMatchesListResponse findElementsInAnchorScope(String                  serverName,
                                                                     String                  urlMarker,
                                                                     String                  anchorScopeGUID,
                                                                     SearchStringRequestBody requestBody)
    {
        final String methodName = "findElementsInAnchorScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        AnchorSearchMatchesListResponse response = new AnchorSearchMatchesListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setElements(handler.findElementsInAnchorScope(userId,
                                                                       requestBody.getSearchString(),
                                                                       anchorScopeGUID,
                                                                       requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the metadata elements connected to the supplied element.
     *
     * @param serverName     name of server instance to route request to
     * @param elementGUID unique identifier for the starting metadata element
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param urlMarker  view service URL marker
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    public RelatedMetadataElementListResponse getRelatedMetadataElements(String             serverName,
                                                                         String             elementGUID,
                                                                         String             relationshipTypeName,
                                                                         String             urlMarker,
                                                                         int                startingAtEnd,
                                                                         ResultsRequestBody requestBody)
    {
        final String methodName = "getRelatedMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog                           auditLog = null;
        RelatedMetadataElementListResponse response = new RelatedMetadataElementListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setRelatedElementList(handler.getRelatedMetadataElements(userId,
                                                                              elementGUID,
                                                                              startingAtEnd,
                                                                              relationshipTypeName,
                                                                              requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Return all the elements that are anchored to an asset plus relationships between these elements and to other elements.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker      the identifier of the view service
     * @param elementGUID  unique identifier for the element
     * @param requestBody effective time and asOfTime
     *
     * @return graph of elements or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - a problem retrieving the connected asset properties from the property server or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public OpenMetadataGraphResponse getAnchoredElementsGraph(String             serverName,
                                                              String             urlMarker,
                                                              String             elementGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName    = "getAnchoredElementsGraph";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataGraphResponse response = new OpenMetadataGraphResponse();
        AuditLog                  auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getAnchoredElementsGraph(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the relationships linking to the supplied elements.
     *
     * @param serverName     name of server instance to route request to
     * @param metadataElementAtEnd1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElementAtEnd2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param relationshipTypeName type name of relationships to follow (or null for all)
     * @param urlMarker  view service URL marker
     * @param requestBody only return an element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return list of related elements
     *  InvalidParameterException the unique identifier is null or not known; the relationship type is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataRelationshipListResponse getMetadataElementRelationships(String             serverName,
                                                                                String             metadataElementAtEnd1GUID,
                                                                                String             relationshipTypeName,
                                                                                String             metadataElementAtEnd2GUID,
                                                                                String             urlMarker,
                                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getMetadataElementRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog                             auditLog = null;
        OpenMetadataRelationshipListResponse response = new OpenMetadataRelationshipListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setRelationshipList(handler.getMetadataElementRelationships(userId,
                                                                                 metadataElementAtEnd1GUID,
                                                                                 metadataElementAtEnd2GUID,
                                                                                 relationshipTypeName,
                                                                                 requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return the list of enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    public PropertyComparisonOperatorListResponse getPropertyComparisonOperatorList(String serverName)
    {
        final String methodName = "getPropertyComparisonOperatorList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        PropertyComparisonOperatorListResponse response = new PropertyComparisonOperatorListResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setValues(List.of(PropertyComparisonOperator.values()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /**
     * Return the list of enum values.
     *
     * @param serverName name of the server to route the request to
     * @return list of enum values
     */
    public MatchCriteriaListResponse getMatchCriteriaList(String serverName)
    {
        final String methodName = "getMatchCriteriaList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MatchCriteriaListResponse response = new MatchCriteriaListResponse();
        AuditLog                  auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setValues(List.of(MatchCriteria.values()));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }



    /**
     * Return a list of metadata elements that match the supplied criteria.  The results can be returned over many pages.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return a list of elements matching the supplied criteria; null means no matching elements in the metadata store.
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataElementsResponse findMetadataElements(String          serverName,
                                                             String          urlMarker,
                                                             FindRequestBody requestBody)
    {
        final String methodName = "findMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        OpenMetadataElementsResponse response = new OpenMetadataElementsResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setElements(handler.findMetadataElements(userId,
                                                                  requestBody.getSearchProperties(),
                                                                  requestBody.getMatchClassifications(),
                                                                  requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return a count of the metadata elements that match the supplied criteria.  This has the same search
     * semantics as findMetadataElements(), but returns the number of matching elements rather than the elements
     * themselves.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return the number of elements matching the supplied criteria; or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    public CountResponse countMetadataElements(String          serverName,
                                               String          urlMarker,
                                               FindRequestBody requestBody)
    {
        final String methodName = "countMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog      auditLog = null;
        CountResponse response = new CountResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setCount(handler.countMetadataElements(userId,
                                                                 requestBody.getSearchProperties(),
                                                                 requestBody.getMatchClassifications(),
                                                                 requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return a list of relationships that match the requested conditions.  The results can be received as a series of pages.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return a list of relationships - null means no matching relationships - or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataRelationshipListResponse findRelationshipsBetweenMetadataElements(String                      serverName,
                                                                                         String                      urlMarker,
                                                                                         FindRelationshipRequestBody requestBody)
    {
        final String methodName = "findRelationshipsBetweenMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog                             auditLog = null;
        OpenMetadataRelationshipListResponse response = new OpenMetadataRelationshipListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setRelationshipList(handler.findRelationshipsBetweenMetadataElements(userId,
                                                                                              requestBody.getRelationshipTypeName(),
                                                                                              requestBody.getRelationshipSubtypeGUIDs(),
                                                                                              requestBody.getEnd1EntityGUIDs(),
                                                                                              requestBody.getEnd2EntityGUIDs(),
                                                                                              requestBody.getEndMatchCriteria(),
                                                                                              requestBody.getSearchProperties(),
                                                                                              requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return a count of the relationships that match the requested conditions.  This has the same search
     * semantics as findRelationshipsBetweenMetadataElements(), but returns the number of matching relationships
     * rather than the relationships themselves.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param requestBody properties defining the search criteria
     *
     * @return the number of relationships matching the supplied criteria; or
     *  InvalidParameterException one of the search parameters are is invalid
     *  UserNotAuthorizedException the governance action service is not able to access the elements
     *  PropertyServerException a problem accessing the metadata store
     */
    public CountResponse countRelationshipsBetweenMetadataElements(String                      serverName,
                                                                   String                      urlMarker,
                                                                   FindRelationshipRequestBody requestBody)
    {
        final String methodName = "countRelationshipsBetweenMetadataElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog      auditLog = null;
        CountResponse response = new CountResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

                response.setCount(handler.countRelationshipsBetweenMetadataElements(userId,
                                                                                    requestBody.getRelationshipTypeName(),
                                                                                    requestBody.getRelationshipSubtypeGUIDs(),
                                                                                    requestBody.getEnd1EntityGUIDs(),
                                                                                    requestBody.getEnd2EntityGUIDs(),
                                                                                    requestBody.getEndMatchCriteria(),
                                                                                    requestBody.getSearchProperties(),
                                                                                    requestBody));
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Retrieve the relationship using its unique identifier.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the relationship
     * @param urlMarker  view service URL marker
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    @SuppressWarnings(value = "unused")
    public OpenMetadataRelationshipResponse getRelationshipByGUID(String         serverName,
                                                                  String         relationshipGUID,
                                                                  String         urlMarker,
                                                                  GetRequestBody requestBody)
    {
        final String methodName = "getRelationshipByGUID";
        final String guidParameterName = "relationshipGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog                         auditLog = null;
        OpenMetadataRelationshipResponse response = new OpenMetadataRelationshipResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getRelationshipByGUID(userId, relationshipGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Retrieve all the versions of an element.
     *
     * @param serverName     name of server instance to route request to
     * @param relationshipGUID unique identifier for the metadata element
     * @param urlMarker  view service URL marker
     * @param requestBody the time window required
     *
     * @return list of matching metadata elements (or null if no elements match the name) or
     *  InvalidParameterException the qualified name is null
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataRelationshipListResponse getRelationshipHistory(String             serverName,
                                                                       String             urlMarker,
                                                                       String             relationshipGUID,
                                                                       HistoryRequestBody requestBody)
    {
        final String methodName = "getRelationshipHistory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        AuditLog auditLog = null;
        OpenMetadataRelationshipListResponse response = new OpenMetadataRelationshipListResponse();

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setRelationshipList(handler.getRelationshipHistory(userId, relationshipGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
