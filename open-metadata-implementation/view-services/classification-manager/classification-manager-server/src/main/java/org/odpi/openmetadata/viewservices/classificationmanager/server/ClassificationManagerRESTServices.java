/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.classificationmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.management.StewardshipManagementClient;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindPropertyNamesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.viewservices.classificationmanager.rest.RelationshipRequestBody;
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
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse setConfidenceClassification(String                    serverName,
                                                    String                    elementGUID,
                                                    boolean                   forLineage,
                                                    boolean                   forDuplicateProcessing,
                                                    ClassificationRequestBody requestBody)
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
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.setConfidenceClassification(userId,
                                                        elementGUID,
                                                        properties,
                                                        requestBody.getEffectiveTime(),
                                                        forLineage,
                                                        forDuplicateProcessing);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConfidenceClassification(String                    serverName,
                                                      String                    elementGUID,
                                                      boolean                   forLineage,
                                                      boolean                   forDuplicateProcessing,
                                                      EffectiveTimeRequestBody  requestBody)
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
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearConfidenceClassification(userId,
                                                      elementGUID,
                                                      requestBody.getEffectiveTime(),
                                                      forLineage,
                                                      forDuplicateProcessing);
            }
            else
            {
                handler.clearConfidenceClassification(userId,
                                                      elementGUID,
                                                      null,
                                                      forLineage,
                                                      forDuplicateProcessing);
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
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setCriticalityClassification(String                    serverName,
                                                     String                    elementGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
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
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.setCriticalityClassification(userId,
                                                         elementGUID,
                                                         properties,
                                                         requestBody.getEffectiveTime(),
                                                         forLineage,
                                                         forDuplicateProcessing);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearCriticalityClassification(String                    serverName,
                                                       String                    elementGUID,
                                                       boolean                   forLineage,
                                                       boolean                   forDuplicateProcessing,
                                                       EffectiveTimeRequestBody  requestBody)
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
                StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                handler.clearCriticalityClassification(userId,
                                                       elementGUID,
                                                       requestBody.getEffectiveTime(),
                                                       forLineage,
                                                       forDuplicateProcessing);
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
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to classify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setConfidentialityClassification(String                    serverName,
                                                         String                    elementGUID,
                                                         boolean                   forLineage,
                                                         boolean                   forDuplicateProcessing,
                                                         ClassificationRequestBody requestBody)
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
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.setConfidentialityClassification(userId,
                                                             elementGUID,
                                                             properties,
                                                             requestBody.getEffectiveTime(),
                                                             forLineage,
                                                             forDuplicateProcessing);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConfidentialityClassification(String                   serverName,
                                                           String                   elementGUID,
                                                           boolean                  forLineage,
                                                           boolean                  forDuplicateProcessing,
                                                           EffectiveTimeRequestBody requestBody)
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

            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearConfidentialityClassification(userId,
                                                           elementGUID,
                                                           requestBody.getEffectiveTime(),
                                                           forLineage,
                                                           forDuplicateProcessing);
            }
            else
            {
                handler.clearConfidentialityClassification(userId,
                                                           elementGUID,
                                                           null,
                                                           forLineage,
                                                           forDuplicateProcessing);            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setRetentionClassification(String                    serverName,
                                                   String                    elementGUID,
                                                   boolean                   forLineage,
                                                   boolean                   forDuplicateProcessing,
                                                   ClassificationRequestBody requestBody)
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
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.setRetentionClassification(userId,
                                                       elementGUID,
                                                       properties,
                                                       requestBody.getEffectiveTime(),
                                                       forLineage,
                                                       forDuplicateProcessing);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearRetentionClassification(String                   serverName,
                                                     String                   elementGUID,
                                                     boolean                  forLineage,
                                                     boolean                  forDuplicateProcessing,
                                                     EffectiveTimeRequestBody requestBody)
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

            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearRetentionClassification(userId,
                                                     elementGUID,
                                                     requestBody.getEffectiveTime(),
                                                     forLineage,
                                                     forDuplicateProcessing);
            }
            else
            {
                handler.clearRetentionClassification(userId,
                                                     elementGUID,
                                                     null,
                                                     forLineage,
                                                     forDuplicateProcessing);
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
     * Add or replace the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of element to attach to
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addSecurityTags(String                    serverName,
                                        String                    elementGUID,
                                        boolean                   forLineage,
                                        boolean                   forDuplicateProcessing,
                                        ClassificationRequestBody requestBody)
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
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.addSecurityTags(userId,
                                            elementGUID,
                                            properties,
                                            requestBody.getEffectiveTime(),
                                            forLineage,
                                            forDuplicateProcessing);
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
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID   unique identifier of element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearSecurityTags(String                    serverName,
                                          String                    elementGUID,
                                          boolean                   forLineage,
                                          boolean                   forDuplicateProcessing,
                                          ClassificationRequestBody requestBody)
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
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearSecurityTags(userId,
                                          elementGUID,
                                          requestBody.getEffectiveTime(),
                                          forLineage,
                                          forDuplicateProcessing);
            }
            else
            {
                handler.clearSecurityTags(userId,
                                          elementGUID,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing);
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
     * Add or replace the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addOwnership(String                    serverName,
                                     String                    elementGUID,
                                     boolean                   forLineage,
                                     boolean                   forDuplicateProcessing,
                                     ClassificationRequestBody requestBody)
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
                if (requestBody.getProperties() instanceof OwnerProperties properties)
                {
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.addOwnership(userId,
                                         elementGUID,
                                         properties,
                                         requestBody.getEffectiveTime(),
                                         forLineage,
                                         forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(OwnerProperties.class.getName(), methodName);
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
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID element where the classification needs to be cleared from.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearOwnership(String                    serverName,
                                       String                    elementGUID,
                                       boolean                   forLineage,
                                       boolean                   forDuplicateProcessing,
                                       ClassificationRequestBody requestBody)
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
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearOwnership(userId,
                                       elementGUID,
                                       requestBody.getEffectiveTime(),
                                       forLineage,
                                       forDuplicateProcessing);
            }
            else
            {
                handler.clearOwnership(userId,
                                       elementGUID,
                                       null,
                                       forLineage,
                                       forDuplicateProcessing);
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
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addElementToSubjectArea(String                    serverName,
                                                String                    elementGUID,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing,
                                                ClassificationRequestBody requestBody)
    {
        final String methodName = "addElementToSubjectArea";

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
                if (requestBody.getProperties() instanceof SubjectAreaClassificationProperties properties)
                {
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.addElementToSubjectArea(userId,
                                                    elementGUID,
                                                    properties,
                                                    requestBody.getEffectiveTime(),
                                                    forLineage,
                                                    forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SubjectAreaClassificationProperties.class.getName(), methodName);
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
     * Remove the subject area designation from the identified element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeElementFromSubjectArea(String                    serverName,
                                                     String                    elementGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String   methodName = "removeElementFromSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeElementFromSubjectArea(userId,
                                                     elementGUID,
                                                     null,
                                                     forLineage,
                                                     forDuplicateProcessing);
            }
            else
            {
                handler.removeElementFromSubjectArea(userId,
                                                     elementGUID,
                                                     requestBody.getEffectiveTime(),
                                                     forLineage,
                                                     forDuplicateProcessing);
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
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
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
                                                boolean                 forLineage,
                                                boolean                 forDuplicateProcessing,
                                                RelationshipRequestBody requestBody)
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

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SemanticAssignmentProperties properties)
                {
                    StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                    handler.setupSemanticAssignment(userId,
                                                    elementGUID,
                                                    glossaryTermGUID,
                                                    properties,
                                                    requestBody.getEffectiveTime(),
                                                    forLineage,
                                                    forDuplicateProcessing);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SemanticAssignmentProperties.class.getName(), methodName);
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
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearSemanticAssignment(String                        serverName,
                                                String                        elementGUID,
                                                String                        glossaryTermGUID,
                                                boolean                       forLineage,
                                                boolean                       forDuplicateProcessing,
                                                EffectiveTimeQueryRequestBody requestBody)
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
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.clearSemanticAssignment(userId,
                                                elementGUID,
                                                glossaryTermGUID,
                                                null,
                                                forLineage,
                                                forDuplicateProcessing);
            }
            else
            {
                handler.clearSemanticAssignment(userId,
                                                elementGUID,
                                                glossaryTermGUID,
                                                requestBody.getEffectiveTime(),
                                                forLineage,
                                                forDuplicateProcessing);
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
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addGovernanceDefinitionToElement(String                  serverName,
                                                         String                  definitionGUID,
                                                         String                  elementGUID,
                                                         boolean                 forLineage,
                                                         boolean                 forDuplicateProcessing,
                                                         RelationshipRequestBody requestBody)
    {
        final String methodName = "addGovernanceDefinitionToElement";

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
                StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

                handler.addGovernanceDefinitionToElement(userId,
                                                         definitionGUID,
                                                         elementGUID,
                                                         requestBody.getEffectiveTime(),
                                                         forLineage,
                                                         forDuplicateProcessing);
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
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeGovernanceDefinitionFromElement(String                        serverName,
                                                              String                        definitionGUID,
                                                              String                        elementGUID,
                                                              boolean                       forLineage,
                                                              boolean                       forDuplicateProcessing,
                                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "removeGovernanceDefinitionFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeGovernanceDefinitionFromElement(userId,
                                                              elementGUID,
                                                              definitionGUID,
                                                              null,
                                                              forLineage,
                                                              forDuplicateProcessing);
            }
            else
            {
                handler.removeGovernanceDefinitionFromElement(userId,
                                                              elementGUID,
                                                              definitionGUID,
                                                              requestBody.getEffectiveTime(),
                                                              forLineage,
                                                              forDuplicateProcessing);
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
     * Retrieve elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElements(String         serverName,
                                                        int            startFrom,
                                                        int            pageSize,
                                                        boolean        forLineage,
                                                        boolean        forDuplicateProcessing,
                                                        FindProperties requestBody)
    {
        final String methodName = "getElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.getElements(userId,
                                                     requestBody,
                                                     startFrom,
                                                     pageSize,
                                                     forLineage,
                                                     forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElementsByPropertyValue(String                       serverName,
                                                                       int                          startFrom,
                                                                       int                          pageSize,
                                                                       boolean                      forLineage,
                                                                       boolean                      forDuplicateProcessing,
                                                                       FindPropertyNamesProperties  requestBody)
    {
        final String methodName = "getElementsByPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.getElementsByPropertyValue(userId,
                                                                    requestBody,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse findElementsByPropertyValue(String                       serverName,
                                                                        int                          startFrom,
                                                                        int                          pageSize,
                                                                        boolean                      forLineage,
                                                                        boolean                      forDuplicateProcessing,
                                                                        FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findElementsByPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.findElementsByPropertyValue(userId,
                                                                     requestBody,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve elements with the requested classification name. It is also possible to limit the results
     * by specifying a type name for the elements that should be returned. If no type name is specified then
     * any type of element may be returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param classificationName name of classification
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElementsByClassification(String         serverName,
                                                                        String         classificationName,
                                                                        int            startFrom,
                                                                        int            pageSize,
                                                                        boolean        forLineage,
                                                                        boolean        forDuplicateProcessing,
                                                                        FindProperties requestBody)
    {
        final String methodName = "getElementsByClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.getElementsByClassification(userId,
                                                                     classificationName,
                                                                     requestBody,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElementsByClassificationWithPropertyValue(String                       serverName,
                                                                                         String                       classificationName,
                                                                                         int                          startFrom,
                                                                                         int                          pageSize,
                                                                                         boolean                      forLineage,
                                                                                         boolean                      forDuplicateProcessing,
                                                                                         FindPropertyNamesProperties requestBody)
    {
        final String methodName = "getElementsByClassificationWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.getElementsByClassificationWithPropertyValue(userId,
                                                                                      classificationName,
                                                                                      requestBody,
                                                                                      startFrom,
                                                                                      pageSize,
                                                                                      forLineage,
                                                                                      forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value found in
     * one of the classification's properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse findElementsByClassificationWithPropertyValue(String                       serverName,
                                                                                          String                       classificationName,
                                                                                          int                          startFrom,
                                                                                          int                          pageSize,
                                                                                          boolean                      forLineage,
                                                                                          boolean                      forDuplicateProcessing,
                                                                                          FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findElementsByClassificationWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.findElementsByClassificationWithPropertyValue(userId,
                                                                                       classificationName,
                                                                                       requestBody,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve related elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummariesResponse getRelatedElements(String                    serverName,
                                                                      String                    elementGUID,
                                                                      String                    relationshipTypeName,
                                                                      int                       startingAtEnd,
                                                                      int                       startFrom,
                                                                      int                       pageSize,
                                                                      boolean                   forLineage,
                                                                      boolean                   forDuplicateProcessing,
                                                                      FindProperties requestBody)
    {
        final String methodName = "getRelatedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog                               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.getRelatedElements(userId,
                                                            elementGUID,
                                                            relationshipTypeName,
                                                            startingAtEnd,
                                                            requestBody,
                                                            startFrom,
                                                            pageSize,
                                                            forLineage,
                                                            forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummariesResponse getRelatedElementsWithPropertyValue(String                       serverName,
                                                                                       String                       elementGUID,
                                                                                       String                       relationshipTypeName,
                                                                                       int                          startingAtEnd,
                                                                                       int                          startFrom,
                                                                                       int                          pageSize,
                                                                                       boolean                      forLineage,
                                                                                       boolean                      forDuplicateProcessing,
                                                                                       FindPropertyNamesProperties requestBody)
    {
        final String methodName = "getRelatedElementsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.getRelatedElementsWithPropertyValue(userId,
                                                                             elementGUID,
                                                                             relationshipTypeName,
                                                                             startingAtEnd,
                                                                             requestBody,
                                                                             startFrom,
                                                                             pageSize,
                                                                             forLineage,
                                                                             forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the relationship's properties
     * specified.  The value must be contained in the by a value found in one of the properties specified.
     * The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummariesResponse findRelatedElementsWithPropertyValue(String                       serverName,
                                                                                        String                       elementGUID,
                                                                                        String                       relationshipTypeName,
                                                                                        int                          startingAtEnd,
                                                                                        int                          startFrom,
                                                                                        int                          pageSize,
                                                                                        boolean                      forLineage,
                                                                                        boolean                      forDuplicateProcessing,
                                                                                        FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findRelatedElementsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setElements(handler.findRelatedElementsWithPropertyValue(userId,
                                                                              elementGUID,
                                                                              relationshipTypeName,
                                                                              startingAtEnd,
                                                                              requestBody,
                                                                              startFrom,
                                                                              pageSize,
                                                                              forLineage,
                                                                              forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve relationships of the requested relationship type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param relationshipTypeName name of relationship
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse getRelationships(String         serverName,
                                                                  String         relationshipTypeName,
                                                                  int            startFrom,
                                                                  int            pageSize,
                                                                  boolean        forLineage,
                                                                  boolean        forDuplicateProcessing,
                                                                  FindProperties requestBody)
    {
        final String methodName = "getRelatedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataRelationshipSummariesResponse response = new MetadataRelationshipSummariesResponse();
        AuditLog                              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setRelationships(handler.getRelationships(userId,
                                                               relationshipTypeName,
                                                               requestBody,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param serverName  name of the server instance to connect to
     * @param relationshipTypeName name of relationship
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse getRelationshipsWithPropertyValue(String                       serverName,
                                                                                   String                       relationshipTypeName,
                                                                                   int                          startFrom,
                                                                                   int                          pageSize,
                                                                                   boolean                      forLineage,
                                                                                   boolean                      forDuplicateProcessing,
                                                                                   FindPropertyNamesProperties requestBody)
    {
        final String methodName = "getRelationshipsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataRelationshipSummariesResponse response = new MetadataRelationshipSummariesResponse();
        AuditLog                              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setRelationships(handler.getRelationshipsWithPropertyValue(userId,
                                                                                relationshipTypeName,
                                                                                requestBody,
                                                                                startFrom,
                                                                                pageSize,
                                                                                forLineage,
                                                                                forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param serverName  name of the server instance to connect to
     * @param relationshipTypeName name of relationship
     * @param requestBody properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse findRelationshipsWithPropertyValue(String                       serverName,
                                                                                    String                       relationshipTypeName,
                                                                                    int                          startFrom,
                                                                                    int                          pageSize,
                                                                                    boolean                      forLineage,
                                                                                    boolean                      forDuplicateProcessing,
                                                                                    FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findRelationshipsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataRelationshipSummariesResponse response = new MetadataRelationshipSummariesResponse();
        AuditLog                              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            response.setRelationships(handler.findRelationshipsWithPropertyValue(userId,
                                                                                     relationshipTypeName,
                                                                                     requestBody,
                                                                                     startFrom,
                                                                                     pageSize,
                                                                                     forLineage,
                                                                                     forDuplicateProcessing));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /**
     * Retrieve the header for the instance identified by the supplied unique identifier.
     * It may be an element (entity) or a relationship between elements.
     *
     * @param serverName  name of the server instance to connect to
     * @param guid identifier to use in the lookup
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody effective time
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeaderResponse retrieveInstanceForGUID(String                   serverName,
                                                         String                   guid,
                                                         boolean                  forLineage,
                                                         boolean                  forDuplicateProcessing,
                                                         EffectiveTimeRequestBody requestBody)
    {
        final String methodName = "retrieveInstanceForGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        ElementHeaderResponse response = new ElementHeaderResponse();
        AuditLog              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementClient handler = instanceHandler.getStewardshipManagementClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElement(handler.retrieveInstanceForGUID(userId,
                                                                    guid,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    null));
            }
            else
            {
                response.setElement(handler.retrieveInstanceForGUID(userId,
                                                                    guid,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime()));
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
