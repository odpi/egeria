/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.classificationmanager.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StewardshipManagementHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The ClassificationManagerRESTServices provides the implementation of the Classification ManagerOpen Metadata View Service (OMVS).
 * This interface provides view interfaces for infrastructure and ops users.
 */

public class ClassificationManagerRESTServices extends TokenController
{
    private static final ClassificationManagerInstanceHandler instanceHandler = new ClassificationManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ClassificationManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public ClassificationManagerRESTServices()
    {
    }


    /**
     * Classify/reclassify the element (typically a context event, to do or incident report) to indicate the level of
     * impact that the event is expected to have on the organization
     *  The level of impact is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse setImpactClassification(String                    serverName,
                                                String                    elementGUID,
                                                NewClassificationRequestBody requestBody)
    {
        final String methodName = "setImpactClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceClassificationProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.setImpactClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceClassificationProperties.class.getName(), methodName);
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
     * Remove the impact classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearImpactClassification(String                    serverName,
                                                  String                    elementGUID,
                                                  MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearImpactClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearImpactClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse setConfidenceClassification(String                    serverName,
                                                    String                    elementGUID,
                                                    NewClassificationRequestBody requestBody)
    {
        final String methodName = "setConfidenceClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceClassificationProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.setConfidenceClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceClassificationProperties.class.getName(), methodName);
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
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConfidenceClassification(String                    serverName,
                                                      String                    elementGUID,
                                                      MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearConfidenceClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearConfidenceClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setCriticalityClassification(String                    serverName,
                                                     String                    elementGUID,
                                                     NewClassificationRequestBody requestBody)
    {
        final String methodName = "setCriticalityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceClassificationProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.setCriticalityClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceClassificationProperties.class.getName(), methodName);
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
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearCriticalityClassification(String                    serverName,
                                                       String                    elementGUID,
                                                       MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearCriticalityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                handler.clearCriticalityClassification(userId, elementGUID, requestBody);
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
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setConfidentialityClassification(String                    serverName,
                                                         String                    elementGUID,
                                                         NewClassificationRequestBody requestBody)
    {
        final String methodName = "setConfidentialityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceClassificationProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.setConfidentialityClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceClassificationProperties.class.getName(), methodName);
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
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConfidentialityClassification(String                    serverName,
                                                           String                    elementGUID,
                                                           MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearConfidentialityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearConfidentialityClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setRetentionClassification(String                    serverName,
                                                   String                    elementGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "setRetentionClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof RetentionClassificationProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.setRetentionClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(RetentionClassificationProperties.class.getName(), methodName);
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
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearRetentionClassification(String                    serverName,
                                                     String                    elementGUID,
                                                     MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearRetentionClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearRetentionClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the governance expectations classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addGovernanceExpectations(String                    serverName,
                                                  String                    elementGUID,
                                                  NewClassificationRequestBody requestBody)
    {
        final String methodName = "addGovernanceExpectations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceExpectationsProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.addGovernanceExpectations(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceExpectationsProperties.class.getName(), methodName);
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
     * Update the governance expectations classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse updateGovernanceExpectations(String                    serverName,
                                                     String                    elementGUID,
                                                     UpdateClassificationRequestBody requestBody)
    {
        final String methodName = "updateGovernanceExpectations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceExpectationsProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.updateGovernanceExpectations(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceExpectationsProperties.class.getName(), methodName);
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
     * Remove the governance expectations classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceExpectations(String                    serverName,
                                                    String                    elementGUID,
                                                    MetadataSourceRequestBody requestBody)
    {
        final String methodName = "clearGovernanceExpectations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearGovernanceExpectations(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the governance measurements for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addGovernanceMeasurements(String                    serverName,
                                                  String                    elementGUID,
                                                  NewClassificationRequestBody requestBody)
    {
        final String methodName = "addGovernanceMeasurements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceMeasurementsProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.addGovernanceMeasurements(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceMeasurementsProperties.class.getName(), methodName);
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
     * Update the governance measurements for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse updateGovernanceMeasurements(String                          serverName,
                                                     String                          elementGUID,
                                                     UpdateClassificationRequestBody requestBody)
    {
        final String methodName = "updateGovernanceMeasurements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceMeasurementsProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.updateGovernanceMeasurements(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceMeasurementsProperties.class.getName(), methodName);
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
     * Remove the governance measurements classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceMeasurements(String                    serverName,
                                                    String                    elementGUID,
                                                    MetadataSourceRequestBody requestBody)
    {
        final String methodName = "clearGovernanceMeasurements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearGovernanceMeasurements(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addSecurityTags(String                    serverName,
                                        String                    elementGUID,
                                        NewClassificationRequestBody requestBody)
    {
        final String methodName = "addSecurityTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SecurityTagsProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.addSecurityTags(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SecurityTagsProperties.class.getName(), methodName);
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
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearSecurityTags(String                    serverName,
                                          String                    elementGUID,
                                          MetadataSourceRequestBody requestBody)
    {
        final String methodName             = "clearSecurityTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearSecurityTags(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addOwnership(String                    serverName,
                                     String                    elementGUID,
                                     NewClassificationRequestBody requestBody)
    {
        final String   methodName = "addOwnership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof OwnershipProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.addOwnership(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(OwnershipProperties.class.getName(), methodName);
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
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element where the classification needs to be cleared from.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearOwnership(String                    serverName,
                                       String                    elementGUID,
                                       MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearOwnership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearOwnership(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Add the DigitalResourceOrigin classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addOrigin(String                    serverName,
                                  String                    elementGUID,
                                  NewClassificationRequestBody requestBody)
    {
        final String   methodName = "addOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DigitalResourceOriginProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.addDigitalResourceOrigin(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DigitalResourceOriginProperties.class.getName(), methodName);
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
     * Remove the DigitalResourceOrigin classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element where the classification needs to be cleared from.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearOrigin(String                    serverName,
                                    String                    elementGUID,
                                    MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearDigitalResourceOrigin(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Add the zone membership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addZoneMembership(String                    serverName,
                                          String                    elementGUID,
                                          NewClassificationRequestBody requestBody)
    {
        final String   methodName = "addZoneMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ZoneMembershipProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

                    handler.addZoneMembership(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(OwnershipProperties.class.getName(), methodName);
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
     * Remove the zone membership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element where the classification needs to be cleared from.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearZoneMembership(String                    serverName,
                                            String                    elementGUID,
                                            MetadataSourceRequestBody requestBody)
    {
        final String   methodName = "clearZoneMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearZoneMembership(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse setupSemanticAssignment(String                  serverName,
                                                String                  elementGUID,
                                                String                  glossaryTermGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupSemanticAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SemanticAssignmentProperties properties)
                {
                    handler.setupSemanticAssignment(userId,
                                                    elementGUID,
                                                    glossaryTermGUID,
                                                    properties,
                                                    requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupSemanticAssignment(userId,
                                                    elementGUID,
                                                    glossaryTermGUID,
                                                    null,
                                                    requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SemanticAssignmentProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.setupSemanticAssignment(userId,
                                                elementGUID,
                                                glossaryTermGUID,
                                                null,
                                                null);
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
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearSemanticAssignment(String                   serverName,
                                                String                   elementGUID,
                                                String                   glossaryTermGUID,
                                                DeleteRequestBody requestBody)
    {
        final String methodName = "clearSemanticAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.clearSemanticAssignment(userId, elementGUID, glossaryTermGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a scope to an element using the ScopedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addScopeToElement(String                  serverName,
                                          String                  elementGUID,
                                          String                  scopeGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addScopeToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ScopedByProperties scopedByProperties)
                {
                    handler.addScopeToElement(userId,
                                              elementGUID,
                                              scopeGUID,
                                              requestBody,
                                              scopedByProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addScopeToElement(userId,
                                              elementGUID,
                                              scopeGUID,
                                              requestBody,
                                              null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ScopedByProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addScopeToElement(userId,
                                          elementGUID,
                                          scopeGUID,
                                          null,
                                          null);
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
     * Remove the ScopedBy relationship between a scope and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeScopeFromElement(String                    serverName,
                                               String                    elementGUID,
                                               String                    scopeGUID,
                                               DeleteRequestBody requestBody)
    {
        final String methodName = "removeScopeFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.removeScopeFromElement(userId, elementGUID, scopeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /**
     * Link a resource to an element using the ResourceList relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addResourceListToElement(String                  serverName,
                                                 String                  elementGUID,
                                                 String                  resourceGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addResourceListToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ResourceListProperties resourceListProperties)
                {
                    handler.addResourceListToElement(userId,
                                                     elementGUID,
                                                     resourceGUID,
                                                     requestBody,
                                                     resourceListProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addResourceListToElement(userId,
                                                     elementGUID,
                                                     resourceGUID,
                                                     requestBody,
                                                     null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ScopedByProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addResourceListToElement(userId,
                                                 elementGUID,
                                                 resourceGUID,
                                                 null,
                                                 null);
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
     * Remove the ResourceList relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeResourceListFromElement(String            serverName,
                                                      String            elementGUID,
                                                      String            resourceGUID,
                                                      DeleteRequestBody requestBody)
    {
        final String methodName = "removeResourceListFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.removeResourceListFromElement(userId, elementGUID, resourceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Link a resource to an element using the MoreInformation relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addMoreInformationToElement(String                  serverName,
                                                    String                  elementGUID,
                                                    String                  resourceGUID,
                                                    NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addMoreInformationToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof MoreInformationProperties moreInformationProperties)
                {
                    handler.addMoreInformationToElement(userId,
                                                        elementGUID,
                                                        resourceGUID,
                                                        requestBody,
                                                        moreInformationProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addMoreInformationToElement(userId,
                                                        elementGUID,
                                                        resourceGUID,
                                                        requestBody,
                                                        null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ScopedByProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addMoreInformationToElement(userId,
                                                    elementGUID,
                                                    resourceGUID,
                                                    null,
                                                    null);
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
     * Remove the MoreInformation relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeMoreInformationFromElement(String            serverName,
                                                         String            elementGUID,
                                                         String            resourceGUID,
                                                         DeleteRequestBody requestBody)
    {
        final String methodName = "removeMoreInformationFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, methodName);

            handler.removeMoreInformationFromElement(userId, elementGUID, resourceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
