/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.classificationmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.StewardshipExchangeClient;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.Date;


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
                    StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                    handler.setConfidenceClassification(userId,
                                                        requestBody.getExternalSourceGUID(),
                                                        requestBody.getExternalSourceName(),
                                                        elementGUID,
                                                        null,
                                                        properties,
                                                        requestBody.getEffectiveTime(),
                                                        (forLineage || requestBody.getForLineage()),
                                                        (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearConfidenceClassification(userId,
                                                      requestBody.getExternalSourceGUID(),
                                                      requestBody.getExternalSourceName(),
                                                      elementGUID,
                                                      null,
                                                      requestBody.getEffectiveTime(),
                                                      (forLineage || requestBody.getForLineage()),
                                                      (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
            else
            {
                handler.clearConfidenceClassification(userId,
                                                      null,
                                                      null,
                                                      elementGUID,
                                                      null,
                                                      new Date(),
                                                      forLineage,
                                                      forDuplicateProcessing);
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
                    StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                    handler.setCriticalityClassification(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         elementGUID,
                                                         properties,
                                                         null,
                                                         requestBody.getEffectiveTime(),
                                                         (forLineage || requestBody.getForLineage()),
                                                         (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
                StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                handler.clearCriticalityClassification(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       elementGUID,
                                                       null,
                                                       requestBody.getEffectiveTime(),
                                                       (forLineage || requestBody.getForLineage()),
                                                       (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
                    StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                    handler.setConfidentialityClassification(userId,
                                                             requestBody.getExternalSourceGUID(),
                                                             requestBody.getExternalSourceName(),
                                                             elementGUID,
                                                             null,
                                                             properties,
                                                             requestBody.getEffectiveTime(),
                                                             (forLineage || requestBody.getForLineage()),
                                                             (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConfidentialityClassification(String                    serverName,
                                                           String                    elementGUID,
                                                           boolean                   forLineage,
                                                           boolean                   forDuplicateProcessing,
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

            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearConfidentialityClassification(userId,
                                                           requestBody.getExternalSourceGUID(),
                                                           requestBody.getExternalSourceName(),
                                                           elementGUID,
                                                           null,
                                                           requestBody.getEffectiveTime(),
                                                           (forLineage || requestBody.getForLineage()),
                                                           (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
            else
            {
                handler.clearConfidentialityClassification(userId,
                                                           null,
                                                           null,
                                                           elementGUID,
                                                           null,
                                                           new Date(),
                                                           forLineage,
                                                           forDuplicateProcessing);            }
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
                    StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                    handler.setRetentionClassification(userId,
                                                       requestBody.getExternalSourceGUID(),
                                                       requestBody.getExternalSourceName(),
                                                       elementGUID,
                                                       null,
                                                       properties,
                                                       requestBody.getEffectiveTime(),
                                                       (forLineage || requestBody.getForLineage()),
                                                       (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearRetentionClassification(String                    serverName,
                                                     String                    elementGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
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

            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearRetentionClassification(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     elementGUID,
                                                     null,
                                                     requestBody.getEffectiveTime(),
                                                     (forLineage || requestBody.getForLineage()),
                                                     (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
            else
            {
                handler.clearRetentionClassification(userId,
                                                     null,
                                                     null,
                                                     elementGUID,
                                                     null,
                                                     new Date(),
                                                     forLineage,
                                                     forDuplicateProcessing);
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
                    StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                    handler.addSecurityTags(userId,
                                            requestBody.getExternalSourceGUID(),
                                            requestBody.getExternalSourceName(),
                                            elementGUID,
                                            null,
                                            properties,
                                            requestBody.getEffectiveTime(),
                                            (forLineage || requestBody.getForLineage()),
                                            (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearSecurityTags(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          elementGUID,
                                          null,
                                          requestBody.getEffectiveTime(),
                                          (forLineage || requestBody.getForLineage()),
                                          (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
            else
            {
                handler.clearSecurityTags(userId,
                                          null,
                                          null,
                                          elementGUID,
                                          null,
                                          new Date(),
                                          forLineage,
                                          forDuplicateProcessing);
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
                if (requestBody.getProperties() instanceof OwnershipProperties properties)
                {
                    StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                    handler.addOwnership(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         elementGUID,
                                         null,
                                         properties,
                                         requestBody.getEffectiveTime(),
                                         (forLineage || requestBody.getForLineage()),
                                         (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.clearOwnership(userId,
                                       requestBody.getExternalSourceGUID(),
                                       requestBody.getExternalSourceName(),
                                       elementGUID,
                                       null,
                                       requestBody.getEffectiveTime(),
                                       (forLineage || requestBody.getForLineage()),
                                       (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
            else
            {
                handler.clearOwnership(userId,
                                       null,
                                       null,
                                       elementGUID,
                                       null,
                                       new Date(),
                                       forLineage,
                                       forDuplicateProcessing);
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
                    StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

                    handler.addElementToSubjectArea(userId,
                                                    requestBody.getExternalSourceGUID(),
                                                    requestBody.getExternalSourceName(),
                                                    elementGUID,
                                                    null,
                                                    properties,
                                                    requestBody.getEffectiveTime(),
                                                    (forLineage || requestBody.getForLineage()),
                                                    (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
                                                     MetadataSourceRequestBody requestBody)
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeElementFromSubjectArea(userId,
                                                     null,
                                                     null,
                                                     elementGUID,
                                                     null,
                                                     new Date(),
                                                     forLineage,
                                                     forDuplicateProcessing);
            }
            else
            {
                handler.removeElementFromSubjectArea(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     elementGUID,
                                                     null,
                                                     requestBody.getEffectiveTime(),
                                                     (forLineage || requestBody.getForLineage()),
                                                     (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SemanticAssignmentProperties properties)
                {

                    handler.setupSemanticAssignment(userId,
                                                    requestBody.getExternalSourceGUID(),
                                                    requestBody.getExternalSourceName(),
                                                    elementGUID,
                                                    glossaryTermGUID,
                                                    properties,
                                                    requestBody.getEffectiveTime(),
                                                    (forLineage || requestBody.getForLineage()),
                                                    (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setupSemanticAssignment(userId,
                                                    null,
                                                    null,
                                                    elementGUID,
                                                    glossaryTermGUID,
                                                    null,
                                                    new Date(),
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
                handler.setupSemanticAssignment(userId,
                                                null,
                                                null,
                                                elementGUID,
                                                glossaryTermGUID,
                                                null,
                                                new Date(),
                                                forLineage,
                                                forDuplicateProcessing);
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
    public VoidResponse clearSemanticAssignment(String                    serverName,
                                                String                    elementGUID,
                                                String                    glossaryTermGUID,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing,
                                                MetadataSourceRequestBody requestBody)
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.clearSemanticAssignment(userId,
                                                null,
                                                null,
                                                elementGUID,
                                                glossaryTermGUID,
                                                new Date(),
                                                forLineage,
                                                forDuplicateProcessing);
            }
            else
            {
                handler.clearSemanticAssignment(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                elementGUID,
                                                glossaryTermGUID,
                                                requestBody.getEffectiveTime(),
                                                (forLineage | requestBody.getForLineage()),
                                                (forDuplicateProcessing | requestBody.getForDuplicateProcessing()));
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
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param definitionGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addGovernanceDefinitionToElement(String                  serverName,
                                                         String                  elementGUID,
                                                         String                  definitionGUID,
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {

                handler.addGovernanceDefinitionToElement(userId,
                                                         requestBody.getExternalSourceGUID(),
                                                         requestBody.getExternalSourceName(),
                                                         elementGUID,
                                                         definitionGUID,
                                                         requestBody.getEffectiveTime(),
                                                         (forLineage || requestBody.getForLineage()),
                                                         (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
            else
            {
                handler.addGovernanceDefinitionToElement(userId,
                                                         null,
                                                         null,
                                                         elementGUID,
                                                         definitionGUID,
                                                         new Date(),
                                                         forLineage,
                                                         forDuplicateProcessing);
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
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param definitionGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeGovernanceDefinitionFromElement(String                    serverName,
                                                              String                    elementGUID,
                                                              String                    definitionGUID,
                                                              boolean                   forLineage,
                                                              boolean                   forDuplicateProcessing,
                                                              MetadataSourceRequestBody requestBody)
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeGovernanceDefinitionFromElement(userId,
                                                              null,
                                                              null,
                                                              elementGUID,
                                                              definitionGUID,
                                                              new Date(),
                                                              forLineage,
                                                              forDuplicateProcessing);
            }
            else
            {
                handler.removeGovernanceDefinitionFromElement(userId,
                                                              requestBody.getExternalSourceGUID(),
                                                              requestBody.getExternalSourceName(),
                                                              elementGUID,
                                                              definitionGUID,
                                                              requestBody.getEffectiveTime(),
                                                              (forLineage || requestBody.getForLineage()),
                                                              (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
     * Link a stakeholder to an element using the Stakeholder relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to link
     * @param stakeholderGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addStakeholderToElement(String                  serverName,
                                                String                  elementGUID,
                                                String                  stakeholderGUID,
                                                boolean                 forLineage,
                                                boolean                 forDuplicateProcessing,
                                                RelationshipRequestBody requestBody)
    {
        final String methodName = "addStakeholderToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {

                if (requestBody.getProperties() instanceof StakeholderProperties stakeholderProperties)
                {
                    handler.addStakeholderToElement(userId,
                                                    requestBody.getExternalSourceGUID(),
                                                    requestBody.getExternalSourceName(),
                                                    elementGUID,
                                                    stakeholderGUID,
                                                    stakeholderProperties.getStakeholderRole(),
                                                    requestBody.getEffectiveTime(),
                                                    (forLineage || requestBody.getForLineage()),
                                                    (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
                }
                else
                {
                    handler.addStakeholderToElement(userId,
                                                    requestBody.getExternalSourceGUID(),
                                                    requestBody.getExternalSourceName(),
                                                    elementGUID,
                                                    stakeholderGUID,
                                                    null,
                                                    requestBody.getEffectiveTime(),
                                                    (forLineage || requestBody.getForLineage()),
                                                    (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
                }
            }
            else
            {
                handler.addStakeholderToElement(userId,
                                                null,
                                                null,
                                                elementGUID,
                                                stakeholderGUID,
                                                null,
                                                new Date(),
                                                forLineage,
                                                forDuplicateProcessing);
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
     * Remove the Stakeholder relationship between a stakeholder (typically Actor) and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param elementGUID unique identifier of the metadata element to update
     * @param stakeholderGUID identifier of the governance definition to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeStakeholderFromElement(String                    serverName,
                                                     String                    elementGUID,
                                                     String                    stakeholderGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     MetadataSourceRequestBody requestBody)
    {
        final String methodName = "removeStakeholderFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeStakeholderFromElement(userId,
                                                     null,
                                                     null,
                                                     elementGUID,
                                                     stakeholderGUID,
                                                     new Date(),
                                                     forLineage,
                                                     forDuplicateProcessing);
            }
            else
            {
                handler.removeStakeholderFromElement(userId,
                                                     requestBody.getExternalSourceGUID(),
                                                     requestBody.getExternalSourceName(),
                                                     elementGUID,
                                                     stakeholderGUID,
                                                     requestBody.getEffectiveTime(),
                                                     (forLineage || requestBody.getForLineage()),
                                                     (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
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
                                          boolean                 forLineage,
                                          boolean                 forDuplicateProcessing,
                                          RelationshipRequestBody requestBody)
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.addScopeToElement(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          elementGUID,
                                          scopeGUID,
                                          requestBody.getEffectiveTime(),
                                          (forLineage || requestBody.getForLineage()),
                                          (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
            else
            {
                handler.addScopeToElement(userId,
                                          null,
                                          null,
                                          elementGUID,
                                          scopeGUID,
                                          new Date(),
                                          forLineage,
                                          forDuplicateProcessing);
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
                                               boolean                   forLineage,
                                               boolean                   forDuplicateProcessing,
                                               MetadataSourceRequestBody requestBody)
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
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeScopeFromElement(userId,
                                               null,
                                               null,
                                               elementGUID,
                                               scopeGUID,
                                               new Date(),
                                               forLineage,
                                               forDuplicateProcessing);
            }
            else
            {
                handler.removeScopeFromElement(userId,
                                               requestBody.getExternalSourceGUID(),
                                               requestBody.getExternalSourceName(),
                                               elementGUID,
                                               scopeGUID,
                                               requestBody.getEffectiveTime(),
                                               (forLineage || requestBody.getForLineage()),
                                               (forDuplicateProcessing || requestBody.getForDuplicateProcessing()));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }


    /* =======================================
     * Licenses
     */

    /**
     * Link an element to a license type and include details of the license in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param elementGUID unique identifier of the element being licensed
     * @param licenseTypeGUID unique identifier for the license type
     * @param requestBody the properties of the license
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse licenseElement(String                  serverName,
                                       String                  elementGUID,
                                       String                  licenseTypeGUID,
                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "licenseElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseProperties properties)
                {
                    response.setGUID(handler.licenseElement(userId,
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            elementGUID,
                                                            licenseTypeGUID,
                                                            properties.getLicenseId(),
                                                            properties.getStartDate(),
                                                            properties.getEndDate(),
                                                            properties.getConditions(),
                                                            properties.getLicensedBy(),
                                                            properties.getLicensedByTypeName(),
                                                            properties.getLicensedByPropertyName(),
                                                            properties.getCustodian(),
                                                            properties.getCustodianTypeName(),
                                                            properties.getCustodianPropertyName(),
                                                            properties.getLicensee(),
                                                            properties.getLicenseeTypeName(),
                                                            properties.getLicenseePropertyName(),
                                                            properties.getNotes(),
                                                            properties.getEffectiveFrom(),
                                                            properties.getEffectiveTo(),
                                                            requestBody.getForLineage(),
                                                            requestBody.getForDuplicateProcessing(),
                                                            requestBody.getEffectiveTime()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LicenseTypeProperties.class.getName(), methodName);
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
     * Update the properties of a license.  Remember to include the licenseId in the properties if the element has multiple
     * licenses for the same license type.
     *
     * @param serverName name of the server instance to connect to
     * @param licenseGUID unique identifier for the license type
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody the properties of the license
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateLicense(String                  serverName,
                                      String                  licenseGUID,
                                      boolean                 replaceAllProperties,
                                      RelationshipRequestBody requestBody)
    {
        final String methodName = "updateLicense";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LicenseProperties properties)
                {
                    handler.updateLicense(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          licenseGUID,
                                          properties.getLicenseId(),
                                          properties.getStartDate(),
                                          properties.getEndDate(),
                                          properties.getConditions(),
                                          properties.getLicensedBy(),
                                          properties.getLicensedByTypeName(),
                                          properties.getLicensedByPropertyName(),
                                          properties.getCustodian(),
                                          properties.getCustodianTypeName(),
                                          properties.getCustodianPropertyName(),
                                          properties.getLicensee(),
                                          properties.getLicenseeTypeName(),
                                          properties.getLicenseePropertyName(),
                                          properties.getNotes(),
                                          replaceAllProperties,
                                          requestBody.getForLineage(),
                                          requestBody.getForDuplicateProcessing(),
                                          requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LicenseProperties.class.getName(), methodName);
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
     * Remove the license for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param licenseGUID unique identifier for the license type
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse unlicenseElement(String                    serverName,
                                         String                    licenseGUID,
                                         MetadataSourceRequestBody requestBody)
    {
        final String methodName = "unlicenseElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.unlicenseElement(userId,
                                         null,
                                         null,
                                         licenseGUID,
                                         false,
                                         false,
                                         new Date());
            }
            else
            {
                handler.unlicenseElement(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         licenseGUID,
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }



    /* =======================================
     * Certifications
     */

    /**
     * Link an element to a certification type and include details of the certification in the relationship properties.
     *
     * @param serverName name of the server instance to connect to
     * @param elementGUID unique identifier of the element being certified
     * @param certificationTypeGUID unique identifier for the certification type
     * @param requestBody the properties of the certification
     *
     * @return guid or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse certifyElement(String                  serverName,
                                       String                  elementGUID,
                                       String                  certificationTypeGUID,
                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "certifyElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CertificationProperties properties)
                {
                    response.setGUID(handler.certifyElement(userId,
                                                            requestBody.getExternalSourceGUID(),
                                                            requestBody.getExternalSourceName(),
                                                            elementGUID,
                                                            certificationTypeGUID,
                                                            properties.getCertificateId(),
                                                            properties.getStartDate(),
                                                            properties.getEndDate(),
                                                            properties.getConditions(),
                                                            properties.getCertifiedBy(),
                                                            properties.getCertifiedByTypeName(),
                                                            properties.getCertifiedByPropertyName(),
                                                            properties.getCustodian(),
                                                            properties.getCustodianTypeName(),
                                                            properties.getCustodianPropertyName(),
                                                            properties.getRecipient(),
                                                            properties.getRecipientTypeName(),
                                                            properties.getRecipientPropertyName(),
                                                            properties.getNotes(),
                                                            properties.getEffectiveFrom(),
                                                            properties.getEffectiveTo(),
                                                            requestBody.getForLineage(),
                                                            requestBody.getForDuplicateProcessing(),
                                                            requestBody.getEffectiveTime()));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CertificationTypeProperties.class.getName(), methodName);
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
     * Update the properties of a certification.  Remember to include the certificationId in the properties if the element has multiple
     * certifications for the same certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param certificationGUID unique identifier for the certification type
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param requestBody the properties of the certification
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateCertification(String                  serverName,
                                            String                  certificationGUID,
                                            boolean                 replaceAllProperties,
                                            RelationshipRequestBody requestBody)
    {
        final String methodName = "updateCertification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CertificationProperties properties)
                {
                    handler.updateCertification(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                certificationGUID,
                                                properties.getCertificateId(),
                                                properties.getStartDate(),
                                                properties.getEndDate(),
                                                properties.getConditions(),
                                                properties.getCertifiedBy(),
                                                properties.getCertifiedByTypeName(),
                                                properties.getCertifiedByPropertyName(),
                                                properties.getCustodian(),
                                                properties.getCustodianTypeName(),
                                                properties.getCustodianPropertyName(),
                                                properties.getRecipient(),
                                                properties.getRecipientTypeName(),
                                                properties.getRecipientPropertyName(),
                                                properties.getNotes(),
                                                replaceAllProperties,
                                                requestBody.getForLineage(),
                                                requestBody.getForDuplicateProcessing(),
                                                requestBody.getEffectiveTime());
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CertificationProperties.class.getName(), methodName);
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
     * Remove the certification for an element.
     *
     * @param serverName name of the server instance to connect to
     * @param certificationGUID unique identifier for the certification type
     * @param requestBody external source information.
     *
     * @return void or
     *  InvalidParameterException one of the properties is invalid
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse decertifyElement(String                    serverName,
                                         String                    certificationGUID,
                                         MetadataSourceRequestBody requestBody)
    {
        final String methodName = "decertifyElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipExchangeClient handler = instanceHandler.getStewardshipExchangeClient(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.decertifyElement(userId,
                                         requestBody.getExternalSourceGUID(),
                                         requestBody.getExternalSourceName(),
                                         certificationGUID,
                                         requestBody.getForLineage(),
                                         requestBody.getForDuplicateProcessing(),
                                         requestBody.getEffectiveTime());
            }
            else
            {
                handler.decertifyElement(userId,
                                         null,
                                         null,
                                         certificationGUID,
                                         false,
                                         false,
                                         new Date());
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
