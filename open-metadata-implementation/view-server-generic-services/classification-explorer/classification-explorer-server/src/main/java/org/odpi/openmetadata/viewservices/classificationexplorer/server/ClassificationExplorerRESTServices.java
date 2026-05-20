/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.classificationexplorer.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataRelationshipSummaryResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.TermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SearchKeywordHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StewardshipManagementHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.SupplementaryPropertiesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.AssignmentScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataScopeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.feedback.SearchKeywordProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.MoreInformationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.resources.ResourceListProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.SecurityTagQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworkservices.omf.rest.FindRequestBody;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.*;


/**
 * The ClassificationExplorerRESTServices provides the implementation of the Classification Explorer Open Metadata View Service (OMVS).
 * This interface provides view interfaces for glossary UIs.
 */

public class ClassificationExplorerRESTServices extends TokenController
{
    private static final ClassificationExplorerInstanceHandler instanceHandler = new ClassificationExplorerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ClassificationExplorerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public ClassificationExplorerRESTServices()
    {
    }


    /**
     * Classify/reclassify the element (typically a context event, to do or incident report) to indicate the level of
     * impact that the event is expected to have on the organization
     *  The level of impact is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException the full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse setImpactClassification(String                    serverName,
                                                String                   urlMarker,
                                                String                    elementGUID,
                                                NewClassificationRequestBody requestBody)
    {
        final String methodName = "setImpactClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ImpactProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.setImpactClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ImpactProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the impact classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException the full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearImpactClassification(String                          serverName,
                                                  String                   urlMarker,
                                                  String                          elementGUID,
                                                  DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearImpactClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearImpactClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up to date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException a full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse setConfidenceClassification(String                    serverName,
                                                    String                   urlMarker,
                                                    String                    elementGUID,
                                                    NewClassificationRequestBody requestBody)
    {
        final String methodName = "setConfidenceClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ConfidenceProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.setConfidenceClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConfidenceProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConfidenceClassification(String                          serverName,
                                                      String                   urlMarker,
                                                      String                          elementGUID,
                                                      DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearConfidenceClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearConfidenceClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setCriticalityClassification(String                    serverName,
                                                     String                   urlMarker,
                                                     String                    elementGUID,
                                                     NewClassificationRequestBody requestBody)
    {
        final String methodName = "setCriticalityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof CriticalityProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.setCriticalityClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(CriticalityProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearCriticalityClassification(String                          serverName,
                                                       String                   urlMarker,
                                                       String                          elementGUID,
                                                       DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearCriticalityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setConfidentialityClassification(String                       serverName,
                                                         String                       urlMarker,
                                                         String                       elementGUID,
                                                         NewClassificationRequestBody requestBody)
    {
        final String methodName = "setConfidentialityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ConfidentialityProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.setConfidentialityClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConfidentialityProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException a full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConfidentialityClassification(String                          serverName,
                                                           String                          urlMarker,
                                                           String                          elementGUID,
                                                           DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearConfidentialityClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearConfidentialityClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse setRetentionClassification(String                       serverName,
                                                   String                       urlMarker,
                                                   String                       elementGUID,
                                                   NewClassificationRequestBody requestBody)
    {
        final String methodName = "setRetentionClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof RetentionProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.setRetentionClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(RetentionProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearRetentionClassification(String                          serverName,
                                                     String                   urlMarker,
                                                     String                          elementGUID,
                                                     DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearRetentionClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearRetentionClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Add the governance expectations classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addGovernanceExpectations(String                       serverName,
                                                  String                       urlMarker,
                                                  String                       elementGUID,
                                                  NewClassificationRequestBody requestBody)
    {
        final String methodName = "addGovernanceExpectations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Update the governance expectations classification to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse updateGovernanceExpectations(String                          serverName,
                                                     String                          urlMarker,
                                                     String                          elementGUID,
                                                     UpdateClassificationRequestBody requestBody)
    {
        final String methodName = "updateGovernanceExpectations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the governance expectations classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceExpectations(String                          serverName,
                                                    String                          urlMarker,
                                                    String                          elementGUID,
                                                    DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearGovernanceExpectations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearGovernanceExpectations(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Add the governance measurements for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addGovernanceMeasurements(String                       serverName,
                                                  String                       urlMarker,
                                                  String                       elementGUID,
                                                  NewClassificationRequestBody requestBody)
    {
        final String methodName = "addGovernanceMeasurements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the governance measurements for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse updateGovernanceMeasurements(String                          serverName,
                                                     String                          urlMarker,
                                                     String                          elementGUID,
                                                     UpdateClassificationRequestBody requestBody)
    {
        final String methodName = "updateGovernanceMeasurements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the governance measurements classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearGovernanceMeasurements(String                          serverName,
                                                    String                          urlMarker,
                                                    String                          elementGUID,
                                                    DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearGovernanceMeasurements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearGovernanceMeasurements(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }




    /**
     * Add the data scope for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addDataScope(String                       serverName,
                                     String                       urlMarker,
                                     String                       elementGUID,
                                     NewClassificationRequestBody requestBody)
    {
        final String methodName = "addDataScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataScopeProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.addDataScopeClassification(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(DataScopeProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Update the data scope for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse updateDataScope(String                          serverName,
                                        String                          urlMarker,
                                        String                          elementGUID,
                                        UpdateClassificationRequestBody requestBody)
    {
        final String methodName = "updateDataScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataScopeProperties properties)
                {
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.updateDataScopeClassification(userId, elementGUID, properties, requestBody);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the data scope classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearDataScope(String                          serverName,
                                       String                          urlMarker,
                                       String                          elementGUID,
                                       DeleteClassificationRequestBody requestBody)
    {
        final String methodName = "clearDataScope";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearDataScopeClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Add the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of element to attach to
     * @param requestBody list of security labels and properties
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addSecurityTags(String                       serverName,
                                        String                       urlMarker,
                                        String                       elementGUID,
                                        NewClassificationRequestBody requestBody)
    {
        final String methodName = "addSecurityTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID   unique identifier of element
     * @param requestBody properties for the request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearSecurityTags(String                          serverName,
                                          String                          urlMarker,
                                          String                          elementGUID,
                                          DeleteClassificationRequestBody requestBody)
    {
        final String methodName             = "clearSecurityTags";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearSecurityTags(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Add the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addOwnership(String                       serverName,
                                     String                       urlMarker,
                                     String                       elementGUID,
                                     NewClassificationRequestBody requestBody)
    {
        final String   methodName = "addOwnership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element where the classification needs to be cleared from.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearOwnership(String                          serverName,
                                       String                          urlMarker,
                                       String                          elementGUID,
                                       DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearOwnership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearOwnership(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Add the DigitalResourceOrigin classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addOrigin(String                       serverName,
                                  String                       urlMarker,
                                  String                       elementGUID,
                                  NewClassificationRequestBody requestBody)
    {
        final String   methodName = "addOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the DigitalResourceOrigin classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element where the classification needs to be cleared from.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearOrigin(String                          serverName,
                                    String                          urlMarker,
                                    String                          elementGUID,
                                    DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearDigitalResourceOrigin(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Add the zone membership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addZoneMembership(String                       serverName,
                                          String                       urlMarker,
                                          String                       elementGUID,
                                          NewClassificationRequestBody requestBody)
    {
        final String   methodName = "addZoneMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

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
                    StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                    handler.addZoneMembership(userId, elementGUID, properties, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ZoneMembershipProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the zone membership classification from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID element where the classification needs to be cleared from.
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearZoneMembership(String                          serverName,
                                            String                          urlMarker,
                                            String                          elementGUID,
                                            DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearZoneMembership";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearZoneMembership(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field, or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse setupSemanticAssignment(String                     serverName,
                                                String                     urlMarker,
                                                String                     elementGUID,
                                                String                     glossaryTermGUID,
                                                NewRelationshipRequestBody requestBody)
    {
        final String methodName = "setupSemanticAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearSemanticAssignment(String                        serverName,
                                                String                        urlMarker,
                                                String                        elementGUID,
                                                String                        glossaryTermGUID,
                                                DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "clearSemanticAssignment";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearSemanticAssignment(userId, elementGUID, glossaryTermGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link a scope to an element using the ScopedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addScopeToElement(String                     serverName,
                                          String                     urlMarker,
                                          String                     elementGUID,
                                          String                     scopeGUID,
                                          NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addScopeToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove the ScopedBy relationship between a scope and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param scopeGUID identifier of the scope to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeScopeFromElement(String                        serverName,
                                               String                        urlMarker,
                                               String                        elementGUID,
                                               String                        scopeGUID,
                                               DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeScopeFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.removeScopeFromElement(userId, elementGUID, scopeGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link a glossary term to an element using the SupplementaryProperties relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param glossaryTermGUID identifier of the glossary term to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addSupplementaryPropertiesToElement(String                     serverName,
                                                            String                     urlMarker,
                                                            String                     elementGUID,
                                                            String                     glossaryTermGUID,
                                                            NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addSupplementaryPropertiesToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SupplementaryPropertiesProperties supplementaryProperties)
                {
                    handler.addSupplementaryPropertiesToElement(userId,
                                                                elementGUID,
                                                                glossaryTermGUID,
                                                                requestBody,
                                                                supplementaryProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addSupplementaryPropertiesToElement(userId,
                                                                elementGUID,
                                                                glossaryTermGUID,
                                                                requestBody,
                                                                null);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SupplementaryPropertiesProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addSupplementaryPropertiesToElement(userId,
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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove the SupplementaryProperties relationship between a glossary term and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param glossaryTermGUID identifier of the glossary term to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeSupplementaryPropertiesFromElement(String                        serverName,
                                                                 String                        urlMarker,
                                                                 String                        elementGUID,
                                                                 String                        glossaryTermGUID,
                                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeSupplementaryPropertiesFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.removeSupplementaryPropertiesFromElement(userId,
                                                             elementGUID,
                                                             glossaryTermGUID,
                                                             requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Attach an actor to an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param actorGUID identifier of the actor to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse assignActorToElement(String                     serverName,
                                             String                     urlMarker,
                                             String                     elementGUID,
                                             String                     actorGUID,
                                             NewRelationshipRequestBody requestBody)
    {
        final String methodName = "assignActorToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssignmentScopeProperties assignmentScopeProperties)
                {
                    handler.assignActorToElement(userId,
                                                 elementGUID,
                                                 actorGUID,
                                                 requestBody,
                                                 assignmentScopeProperties);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.assignActorToElement(userId,
                                                 elementGUID,
                                                 actorGUID,
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
                handler.assignActorToElement(userId,
                                             elementGUID,
                                             actorGUID,
                                             null,
                                             null);
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
     * Detach an actor from an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker   view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param actorGUID   identifier of the actor to detach
     * @param requestBody properties for relationship request
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse unassignActorFromElement(String                        serverName,
                                                 String                        urlMarker,
                                                 String                        elementGUID,
                                                 String                        actorGUID,
                                                 DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unassignActorFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.unassignActorFromElement(userId, elementGUID, actorGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link a resource to an element using the ResourceList relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addResourceListToElement(String                     serverName,
                                                 String                     urlMarker,
                                                 String                     elementGUID,
                                                 String                     resourceGUID,
                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addResourceListToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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
                    restExceptionHandler.handleInvalidPropertiesObject(ResourceListProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove the ResourceList relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeResourceListFromElement(String                        serverName,
                                                      String                        urlMarker,
                                                      String                        elementGUID,
                                                      String                        resourceGUID,
                                                      DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeResourceListFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.removeResourceListFromElement(userId, elementGUID, resourceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Link a resource to an element using the MoreInformation relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addMoreInformationToElement(String                     serverName,
                                                    String                     urlMarker,
                                                    String                     elementGUID,
                                                    String                     resourceGUID,
                                                    NewRelationshipRequestBody requestBody)
    {
        final String methodName = "addMoreInformationToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

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
                    restExceptionHandler.handleInvalidPropertiesObject(MoreInformationProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Remove the MoreInformation relationship between a resource and an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param resourceGUID identifier of the resource to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse removeMoreInformationFromElement(String                        serverName,
                                                         String                        urlMarker,
                                                         String                        elementGUID,
                                                         String                        resourceGUID,
                                                         DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "removeMoreInformationFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.removeMoreInformationFromElement(userId, elementGUID, resourceGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Adds a search keyword to the element.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param elementGUID  String - unique id for the element.
     * @param requestBody containing type of search keyword enum and the text of the search keyword.
     *
     * @return elementGUID for the new search keyword object or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public GUIDResponse addSearchKeywordToElement(String                   serverName,
                                                  String                   urlMarker,
                                                  String                   elementGUID,
                                                  NewAttachmentRequestBody requestBody)
    {
        final String methodName = "addSearchKeywordToElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse  response = new GUIDResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SearchKeywordHandler handler = instanceHandler.getSearchKeywordHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SearchKeywordProperties searchKeywordProperties)
                {

                    response.setGUID(handler.addSearchKeywordToElement(userId, elementGUID, requestBody, requestBody.getInitialClassifications(), searchKeywordProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SearchKeywordProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, NewAttachmentRequestBody.class.getName());
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
     * Update an existing search keyword.
     *
     * @param serverName   name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param searchKeywordGUID  unique identifier for the search keyword to change.
     * @param requestBody  containing type of search keyword enum and the text of the search keyword.
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid.
     * PropertyServerException - a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public BooleanResponse updateSearchKeyword(String                   serverName,
                                               String                   urlMarker,
                                               String                   searchKeywordGUID,
                                               UpdateElementRequestBody requestBody)
    {
        final String methodName = "updateSearchKeyword";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        BooleanResponse  response = new BooleanResponse();
        AuditLog         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SearchKeywordProperties searchKeywordProperties)
                {
                    SearchKeywordHandler handler = instanceHandler.getSearchKeywordHandler(userId, serverName, urlMarker, methodName);

                    response.setFlag(handler.updateSearchKeyword(userId, searchKeywordGUID, requestBody, searchKeywordProperties));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(SearchKeywordProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, UpdateElementRequestBody.class.getName());
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
     * Removes a search keyword added to the element by this user.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param searchKeywordGUID  String - unique id for the search keyword object
     * @param requestBody optional effective time
     *
     * @return void or
     * InvalidParameterException - one of the parameters is null or invalid or
     * PropertyServerException - a problem updating the element properties in
     *                                   the metadata repository or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    public VoidResponse removeSearchKeywordFromElement(String                   serverName,
                                                       String                   urlMarker,
                                                       String                   searchKeywordGUID,
                                                       DeleteElementRequestBody requestBody)
    {
        final String methodName = "removeSearchKeywordFromElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse  response = new VoidResponse();
        AuditLog      auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SearchKeywordHandler handler = instanceHandler.getSearchKeywordHandler(userId, serverName, urlMarker, methodName);

            handler.deleteSearchKeyword(userId, searchKeywordGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Classify/reclassify the element with the KnownDuplicate classification
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException a full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse setKnownDuplicateClassification(String                       serverName,
                                                        String                       urlMarker,
                                                        String                       elementGUID,
                                                        NewClassificationRequestBody requestBody)
    {
        final String methodName = "setKnownDuplicateClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof KnownDuplicateProperties properties)
                {
                    handler.setKnownDuplicateClassification(userId, elementGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setKnownDuplicateClassification(userId, elementGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(KnownDuplicateProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the KnownDuplicate classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearKnownDuplicateClassification(String                          serverName,
                                                          String                          urlMarker,
                                                          String                          elementGUID,
                                                          DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearKnownDuplicateClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearKnownDuplicateClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }




    /**
     * Create a relationship between two elements that show they represent the same "thing". If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to link
     * @param peerDuplicateGUID identifier of the duplicate to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkElementsAsPeerDuplicates(String                     serverName,
                                                     String                     urlMarker,
                                                     String                     elementGUID,
                                                     String                     peerDuplicateGUID,
                                                     NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkElementsAsPeerDuplicates";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof PeerDuplicateLinkProperties peerDuplicateLinkProperties)
                {
                    handler.linkElementsAsPeerDuplicates(userId,
                                                         elementGUID,
                                                         peerDuplicateGUID,
                                                         peerDuplicateLinkProperties,
                                                         requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkElementsAsPeerDuplicates(userId,
                                                         elementGUID,
                                                         peerDuplicateGUID,
                                                         null,
                                                         requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(PeerDuplicateLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkElementsAsPeerDuplicates(userId,
                                                     elementGUID,
                                                     peerDuplicateGUID,
                                                     null,
                                                     null);
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
     * Remove the PeerDuplicateLink relationship between an element and its duplicate.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to update
     * @param peerDuplicateGUID identifier of the duplicate to link
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkElementsAsPeerDuplicates(String                        serverName,
                                                       String                        urlMarker,
                                                       String                        elementGUID,
                                                       String                        peerDuplicateGUID,
                                                       DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkElementsAsPeerDuplicates";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.unlinkElementsAsPeerDuplicates(userId, elementGUID, peerDuplicateGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Classify/reclassify the element with the ConsolidatedDuplicate classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to classify
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException a full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public VoidResponse setConsolidatedDuplicateClassification(String                       serverName,
                                                               String                       urlMarker,
                                                               String                       elementGUID,
                                                               NewClassificationRequestBody requestBody)
    {
        final String methodName = "setConsolidatedDuplicateClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

                if (requestBody.getProperties() instanceof ConsolidatedDuplicateProperties properties)
                {
                    handler.setConsolidatedDuplicateClassification(userId, elementGUID, properties, requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.setConsolidatedDuplicateClassification(userId, elementGUID, null, requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConsolidatedDuplicateProperties.class.getName(), methodName);
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

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the ConsolidatedDuplicate classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the metadata element to declassify
     * @param requestBody properties for the request
     *
     * @return void or
     *       InvalidParameterException a full path or userId is null or
     *       PropertyServerException problem accessing property server or
     *       UserNotAuthorizedException security access problem
     */
    public VoidResponse clearConsolidatedDuplicateClassification(String                          serverName,
                                                                 String                          urlMarker,
                                                                 String                          elementGUID,
                                                                 DeleteClassificationRequestBody requestBody)
    {
        final String   methodName = "clearConsolidatedDuplicateClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.clearConsolidatedDuplicateClassification(userId, elementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }



    /**
     * Create a ConsolidatedDuplicateLink relationship between an element and one of the source elements of its properties.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that was created with the values from a number of duplicate elements
     * @param sourceElementGUID unique identifier of one of the source elements
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkConsolidatedDuplicateToSourceElement(String                     serverName,
                                                                 String                     urlMarker,
                                                                 String                     elementGUID,
                                                                 String                     sourceElementGUID,
                                                                 NewRelationshipRequestBody requestBody)
    {
        final String methodName = "linkConsolidatedDuplicateToSourceElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ConsolidatedDuplicateLinkProperties consolidatedDuplicateLinkProperties)
                {
                    handler.linkConsolidatedDuplicateToSourceElement(userId,
                                                                     elementGUID,
                                                                     sourceElementGUID,
                                                                     consolidatedDuplicateLinkProperties,
                                                                     requestBody);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkConsolidatedDuplicateToSourceElement(userId,
                                                                     elementGUID,
                                                                     sourceElementGUID,
                                                                     null,
                                                                     requestBody);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ConsolidatedDuplicateLinkProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.linkConsolidatedDuplicateToSourceElement(userId,
                                                                 elementGUID,
                                                                 sourceElementGUID,
                                                                 null,
                                                                 null);
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
     * Remove a ConsolidatedDuplicateLink relationship between an element and one of its source elements.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that was created with the values from a number of duplicate elements
     * @param sourceElementGUID unique identifier of one of the source elements
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException a full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkConsolidatedDuplicateFromSourceElement(String                        serverName,
                                                                     String                        urlMarker,
                                                                     String                        elementGUID,
                                                                     String                        sourceElementGUID,
                                                                     DeleteRelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkConsolidatedDuplicateFromSourceElement";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            handler.unlinkConsolidatedDuplicateFromSourceElement(userId, elementGUID, sourceElementGUID, requestBody);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Return information about the elements classified with the impact classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getImpactClassifiedElements(String                         serverName,
                                                                        String                         urlMarker,
                                                                        LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getImpactClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getImpactClassifiedElements(userId,
                                                                         requestBody.getReturnSpecificLevel(),
                                                                         requestBody.getLevelIdentifier(),
                                                                         requestBody));
            }
            else
            {
                response.setElements(handler.getImpactClassifiedElements(userId,
                                                                         false,
                                                                         0,
                                                                         null));
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
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getConfidenceClassifiedElements(String                         serverName,
                                                                            String                         urlMarker,
                                                                            LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getConfidenceClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getConfidenceClassifiedElements(userId,
                                                                             requestBody.getReturnSpecificLevel(),
                                                                             requestBody.getLevelIdentifier(),
                                                                             requestBody));
            }
            else
            {
                response.setElements(handler.getConfidenceClassifiedElements(userId,
                                                                             false,
                                                                             0,
                                                                             null));
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
     * Return information about the elements classified with the criticality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getCriticalityClassifiedElements(String                         serverName,
                                                                             String                         urlMarker,
                                                                             LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getCriticalityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getCriticalityClassifiedElements(userId,
                                                                              requestBody.getReturnSpecificLevel(),
                                                                              requestBody.getLevelIdentifier(),
                                                                              requestBody));
            }
            else
            {
                response.setElements(handler.getCriticalityClassifiedElements(userId,
                                                                              false,
                                                                              0,
                                                                              null));
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
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getConfidentialityClassifiedElements(String                         serverName,
                                                                                 String                         urlMarker,
                                                                                 LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getConfidentialityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getConfidentialityClassifiedElements(userId,
                                                                                  requestBody.getReturnSpecificLevel(),
                                                                                  requestBody.getLevelIdentifier(),
                                                                                  requestBody));
            }
            else
            {
                response.setElements(handler.getConfidentialityClassifiedElements(userId,
                                                                                  false,
                                                                                  0,
                                                                                  null));
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
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getRetentionClassifiedElements(String                         serverName,
                                                                           String                         urlMarker,
                                                                           LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getRetentionClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getRetentionClassifiedElements(userId,
                                                                            requestBody.getReturnSpecificLevel(),
                                                                            requestBody.getLevelIdentifier(),
                                                                            requestBody));
            }
            else
            {
                response.setElements(handler.getRetentionClassifiedElements(userId,
                                                                            false,
                                                                            0,
                                                                            null));
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
     * Return information about the elements classified with the security tags classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getSecurityTaggedElements(String                     serverName,
                                                                      String                     urlMarker,
                                                                      SecurityTagQueryProperties requestBody)
    {
        final String methodName = "getSecurityTaggedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSecurityTaggedElements(userId,
                                                                       requestBody.getSecurityLabels(),
                                                                       requestBody.getSecurityProperties(),
                                                                       requestBody));
            }
            else
            {
                response.setElements(handler.getSecurityTaggedElements(userId,
                                                                       null,
                                                                       null,
                                                                       null));
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
     * Return information about the elements classified with the confidence classification.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getOwnersElements(String            serverName,
                                                              String            urlMarker,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getOwnersElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getOwnersElements(userId, requestBody.getFilter(), requestBody));
            }
            else
            {
                response.setElements(handler.getOwnersElements(userId, null, null));
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
     * Return information about the elements from a specific origin.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getElementsByOrigin(String                              serverName,
                                                                String                              urlMarker,
                                                                FindDigitalResourceOriginProperties requestBody)
    {
        final String methodName = "getElementsByOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getElementsByOrigin(userId,
                                                                 requestBody.getProperties(),
                                                                 requestBody));
            }
            else
            {
                response.setElements(handler.getElementsByOrigin(userId, null, null));
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
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getMeanings(String                            serverName,
                                                        String                            urlMarker,
                                                        String                            elementGUID,
                                                        SemanticAssignmentQueryProperties requestBody)
    {
        final String methodName = "getMeanings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getMeanings(userId,
                                                                                    elementGUID,
                                                                                    requestBody.getExpression(),
                                                                                    requestBody.getDescription(),
                                                                                    requestBody.getStatus(),
                                                                                    requestBody.getReturnSpecificConfidence(),
                                                                                    requestBody.getConfidence(),
                                                                                    requestBody.getCreatedBy(),
                                                                                    requestBody.getSteward(),
                                                                                    requestBody.getSource(),
                                                                                    requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getMeanings(userId,
                                                                                    elementGUID,
                                                                                    null,
                                                                                    null,
                                                                                    TermAssignmentStatus.VALIDATED,
                                                                                    false,
                                                                                    0,
                                                                                    null,
                                                                                    null,
                                                                                    null,
                                                                                    null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
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
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getSemanticAssignees(String                            serverName,
                                                                 String                            urlMarker,
                                                                 String                            glossaryTermGUID,
                                                                 SemanticAssignmentQueryProperties requestBody)
    {
        final String methodName = "getSemanticAssignees";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getSemanticAssignees(userId,
                                                                                         glossaryTermGUID,
                                                                                         requestBody.getExpression(),
                                                                                         requestBody.getDescription(),
                                                                                         requestBody.getStatus(),
                                                                                         requestBody.getReturnSpecificConfidence(),
                                                                                         requestBody.getConfidence(),
                                                                                         requestBody.getCreatedBy(),
                                                                                         requestBody.getSteward(),
                                                                                         requestBody.getSource(),
                                                                                         requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(glossaryTermGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getSemanticAssignees(userId,
                                                                                             glossaryTermGUID,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             false,
                                                                                             0,
                                                                                             null,
                                                                                             null,
                                                                                             null,
                                                                                             null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(glossaryTermGUID, summaryList));
                }
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
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getGovernedElements(String             serverName,
                                                                String             urlMarker,
                                                                String             governanceDefinitionGUID,
                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getGovernedElements(userId,
                                                                                            governanceDefinitionGUID,
                                                                                            null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(governanceDefinitionGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getGovernedElements(userId,
                                                                                        governanceDefinitionGUID,
                                                                                        requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(governanceDefinitionGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getGovernedByDefinitions(String         serverName,
                                                                     String         urlMarker,
                                                                     String         elementGUID,
                                                                     ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernedByDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getGovernedByDefinitions(userId,
                                                                                             elementGUID,
                                                                                             null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getGovernedByDefinitions(userId,
                                                                                             elementGUID,
                                                                                             requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve the elements linked via a "SourcedFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the governance definition that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getSourceElements(String         serverName,
                                                              String         urlMarker,
                                                              String         elementGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getSourceElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getSourceElements(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getSourceElements(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve the elements linked via a "SourcedFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getElementsSourcedFrom(String         serverName,
                                                                   String         urlMarker,
                                                                   String         elementGUID,
                                                                   ResultsRequestBody requestBody)
    {
        final String methodName = "getElementsSourceFrom";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getElementsSourcedFrom(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getElementsSourcedFrom(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getScopes(String             serverName,
                                                      String             urlMarker,
                                                      String             elementGUID,
                                                      ResultsRequestBody requestBody)
    {
        final String methodName = "getScopes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getScopes(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getScopes(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve the elements linked via a "ScopedBy" relationship to the requested scope.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param scopeGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getScopedElements(String             serverName,
                                                              String             urlMarker,
                                                              String             scopeGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getScopedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getScopedElements(userId, scopeGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(scopeGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getScopedElements(userId, scopeGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(scopeGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve the list of resources assigned to an element via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getResourceList(String             serverName,
                                                            String             urlMarker,
                                                            String             elementGUID,
                                                            ResultsRequestBody requestBody)
    {
        final String methodName = "getResourceList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getResourceList(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getResourceList(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve the list of elements assigned to a resource via the "ResourceList" relationship between two referenceables.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param resourceGUID unique identifier of the element that the returned elements are linked to
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getSupportedByResource(String             serverName,
                                                                   String             urlMarker,
                                                                   String             resourceGUID,
                                                                   ResultsRequestBody requestBody)
    {
        final String methodName = "getSupportedByResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getSupportedByResource(userId, resourceGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(resourceGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getSupportedByResource(userId, resourceGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(resourceGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Return information about the elements linked to a license type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param licenseTypeGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getLicensedElements(String             serverName,
                                                                String             urlMarker,
                                                                String             licenseTypeGUID,
                                                                ResultsRequestBody requestBody)
    {
        final String methodName = "getLicensedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            List<OpenMetadataRootElement> summaryList = handler.getLicensedElements(userId, licenseTypeGUID, requestBody);
            if (summaryList != null)
            {
                response.setElements(summaryList);

                if (requestBody != null)
                {
                    response.setMermaidGraph(handler.getMermaidGraph(licenseTypeGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
                else
                {
                    response.setMermaidGraph(handler.getMermaidGraph(licenseTypeGUID, summaryList));
                }
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
     * Return information about the licenses linked to an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getLicenses(String             serverName,
                                                        String             urlMarker,
                                                        String             elementGUID,
                                                        ResultsRequestBody requestBody)
    {
        final String methodName = "getLicenses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            List<OpenMetadataRootElement> summaryList = handler.getLicenses(userId, elementGUID, requestBody);
            if (summaryList != null)
            {
                response.setElements(summaryList);

                if (requestBody != null)
                {
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
                else
                {
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
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
     * Return information about the elements linked to a certification type.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param certificationTypeGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getCertifiedElements(String             serverName,
                                                                 String             urlMarker,
                                                                 String             certificationTypeGUID,
                                                                 ResultsRequestBody requestBody)
    {
        final String methodName = "getCertifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            List<OpenMetadataRootElement> summaryList = handler.getCertifiedElements(userId, certificationTypeGUID, requestBody);
            if (summaryList != null)
            {
                response.setElements(summaryList);

                if (requestBody != null)
                {
                    response.setMermaidGraph(handler.getMermaidGraph(certificationTypeGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
                else
                {
                    response.setMermaidGraph(handler.getMermaidGraph(certificationTypeGUID, summaryList));
                }
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
     * Return information about the certifications linked to an element.
     *
     * @param serverName name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier for the license
     * @param requestBody additional query parameters
     *
     * @return properties of the license or
     *  InvalidParameterException qualifiedName or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public OpenMetadataRootElementsResponse getCertifications(String             serverName,
                                                              String             urlMarker,
                                                              String             elementGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getCertifications";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            List<OpenMetadataRootElement> summaryList = handler.getCertifications(userId, elementGUID, requestBody);
            if (summaryList != null)
            {
                response.setElements(summaryList);

                if (requestBody != null)
                {
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
                else
                {
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
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
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier for the metadata element
     * @param requestBody only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public OpenMetadataRootElementResponse getRootElementByGUID(String         serverName,
                                                                String         urlMarker,
                                                                String         elementGUID,
                                                                GetRequestBody requestBody)
    {
        final String methodName = "getRootElementByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getRootElementByGUID(userId, elementGUID, requestBody));
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
    public OpenMetadataRootElementResponse getRootElementByUniqueName(String                     serverName,
                                                                      String                     urlMarker,
                                                                      FindPropertyNameProperties requestBody)
    {
        final String methodName = "getMetadataElementByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog                        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else if (requestBody.getPropertyName() == null)
            {
                response.setElement(handler.getMetadataElementByUniqueName(userId,
                                                                           requestBody.getPropertyValue(),
                                                                           OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                           requestBody));
            }
            else
            {
                response.setElement(handler.getMetadataElementByUniqueName(userId,
                                                                           requestBody.getPropertyValue(),
                                                                           requestBody.getPropertyName(),
                                                                           requestBody));
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
    public GUIDResponse getRootElementGUIDByUniqueName(String                     serverName,
                                                       String                     urlMarker,
                                                       FindPropertyNameProperties requestBody)
    {
        final String methodName = "getRootElementGUIDByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else if (requestBody.getPropertyName() == null)
            {
                response.setGUID(handler.getRootElementGUIDByUniqueName(userId,
                                                                        requestBody.getPropertyValue(),
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        requestBody));
            }
            else
            {
                response.setGUID(handler.getRootElementGUIDByUniqueName(userId,
                                                                        requestBody.getPropertyValue(),
                                                                        requestBody.getPropertyName(),
                                                                        requestBody));
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
     * Retrieve elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getRootElementsByType(String             serverName,
                                                                  String             urlMarker,
                                                                  ResultsRequestBody requestBody)
    {
        final String methodName = "getRootElementsByType";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getRootElementsByType(userId, null));
            }
            else
            {
                response.setElements(handler.getRootElementsByType(userId, requestBody));
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
     * Retrieve elements that match the complex query.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findRootElements(String          serverName,
                                                             String          urlMarker,
                                                             FindRequestBody requestBody)
    {
        final String methodName = "findRootElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findRootElements(userId, requestBody.getSearchProperties(), requestBody.getMatchClassifications(), requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindRequestBody.class.getName());
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
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getRootElementsByPropertyValue(String                      serverName,
                                                                           String                      urlMarker,
                                                                           FindPropertyNamesProperties requestBody)
    {
        final String methodName = "getElementsByPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                response.setElements(handler.getElementsByPropertyValue(userId,
                                                                        requestBody.getPropertyValue(),
                                                                        requestBody.getPropertyNames(),
                                                                        requestBody));
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
     * Retrieve elements by a value found in one of the properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findElementsByPropertyValue(String                      serverName,
                                                                        String                      urlMarker,
                                                                        FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findElementsByPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
            else
            {
                response.setElements(handler.findElementsByPropertyValue(userId,
                                                                         requestBody.getPropertyValue(),
                                                                         requestBody.getPropertyNames(),
                                                                         requestBody));
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
     * Retrieve the authored elements that match the search string and optional status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of elements
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse findAuthoredElements(String                    serverName,
                                                                 String                    urlMarker,
                                                                 ContentStatusSearchString requestBody)
    {
        final String methodName = "findAuthoredElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findAuthoredElements(userId, requestBody.getSearchString(), requestBody.getContentStatusList(), requestBody));
            }
            else
            {
                response.setElements(handler.findAuthoredElements(userId, null, null, null));
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
     * Retrieve the authored elements that match the category name and status.
     *
     * @param serverName name of the service to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return a list of elements
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElementsResponse getAuthoredElementsByCategory(String                         serverName,
                                                                          String                         urlMarker,
                                                                          ContentStatusFilterRequestBody requestBody)
    {
        final String methodName = "getAuthoredElementsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getAuthoredElementsByCategory(userId, requestBody.getFilter(), requestBody.getContentStatusList(), requestBody));
            }
            else
            {
                response.setElements(handler.getAuthoredElementsByCategory(userId, null, null, null));
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
     * Retrieve elements with the requested classification name. It is also possible to limit the results
     * by specifying a type name for the elements that should be returned. If no type name is specified then
     * any type of element may be returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getElementsByClassification(String             serverName,
                                                                        String             urlMarker,
                                                                        String             classificationName,
                                                                        ResultsRequestBody requestBody)
    {
        final String methodName = "getElementsByClassification";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getElementsByClassification(userId, classificationName, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getElementsByClassificationWithPropertyValue(String                      serverName,
                                                                                         String                      urlMarker,
                                                                                         String                      classificationName,
                                                                                         FindPropertyNamesProperties requestBody)
    {
        final String methodName = "getElementsByClassificationWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                response.setElements(handler.getElementsByClassification(userId,
                                                                         classificationName,
                                                                         requestBody.getPropertyValue(),
                                                                         requestBody.getPropertyNames(),
                                                                         requestBody,
                                                                         methodName));
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
     * Retrieve elements with the requested classification name and with the requested a value found in
     * one of the classification's properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findElementsByClassificationWithPropertyValue(String                      serverName,
                                                                                          String                      urlMarker,
                                                                                          String                      classificationName,
                                                                                          FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findElementsByClassificationWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                response.setElements(handler.findElementsByClassificationWithPropertyValue(userId,
                                                                                           classificationName,
                                                                                           requestBody.getPropertyValue(),
                                                                                           requestBody.getPropertyNames(),
                                                                                           requestBody));
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
     * Retrieve related elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getRelatedElements(String             serverName,
                                                               String             urlMarker,
                                                               String             elementGUID,
                                                               String             relationshipTypeName,
                                                               int                startingAtEnd,
                                                               ResultsRequestBody requestBody)
    {
        final String methodName = "getRelatedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                List<OpenMetadataRootElement> summaryList = handler.getRelatedElements(userId,
                                                                                       elementGUID,
                                                                                       relationshipTypeName,
                                                                                       startingAtEnd,
                                                                                       null,
                                                                                       null,
                                                                                       null,
                                                                                       methodName);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList));
                }
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getRelatedElements(userId,
                                                                                           elementGUID,
                                                                                           relationshipTypeName,
                                                                                           startingAtEnd,
                                                                                           null,
                                                                                           null,
                                                                                           requestBody,
                                                                                           methodName);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve elements linked via the requested relationship type name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getRelatedElementsWithPropertyValue(String                      serverName,
                                                                                String                      urlMarker,
                                                                                String                      elementGUID,
                                                                                String                      relationshipTypeName,
                                                                                int                         startingAtEnd,
                                                                                FindPropertyNamesProperties requestBody)
    {
        final String methodName = "getRelatedElementsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.getRelatedElements(userId,
                                                                                           elementGUID,
                                                                                           relationshipTypeName,
                                                                                           startingAtEnd,
                                                                                           requestBody.getPropertyValue(),
                                                                                           requestBody.getPropertyNames(),
                                                                                           requestBody,
                                                                                           methodName);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve elements linked via the requested relationship type name and with the relationship's properties
     * specified.  The value must be contained in one of the properties specified (or any property if no property names are specified).
     * An open metadata type name may be supplied to restrict the linked elements that are matched.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findRelatedElementsWithPropertyValue(String                      serverName,
                                                                                 String                      urlMarker,
                                                                                 String                      elementGUID,
                                                                                 String                      relationshipTypeName,
                                                                                 int                         startingAtEnd,
                                                                                 FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findRelatedElementsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                         auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                List<OpenMetadataRootElement> summaryList = handler.findRelatedElementsWithPropertyValue(userId,
                                                                                                         elementGUID,
                                                                                                         relationshipTypeName,
                                                                                                         startingAtEnd,
                                                                                                         requestBody.getPropertyValue(),
                                                                                                         requestBody.getPropertyNames(),
                                                                                                         requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList);
                    response.setMermaidGraph(handler.getMermaidGraph(elementGUID, summaryList, requestBody.getMaxMermaidNodeCount()));
                }
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
     * Retrieve relationships of the requested relationship type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param relationshipTypeName name of relationship
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse getRelationships(String             serverName,
                                                                  String             urlMarker,
                                                                  String             relationshipTypeName,
                                                                  ResultsRequestBody requestBody)
    {
        final String methodName = "getRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        MetadataRelationshipSummariesResponse response = new MetadataRelationshipSummariesResponse();
        AuditLog                              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            MetadataRelationshipSummaryList summaryList = handler.getRelationships(userId,
                                                                                   relationshipTypeName,
                                                                                   null,
                                                                                   null,
                                                                                   requestBody,
                                                                                   methodName);
            if (summaryList != null)
            {
                response.setRelationships(summaryList.getElementList());
                response.setMermaidGraph(summaryList.getMermaidGraph());
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
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param serverName  name of the server instance to connect to
     * @param relationshipTypeName name of relationship
     * @param urlMarker  view service URL marker
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse getRelationshipsWithPropertyValue(String                      serverName,
                                                                                   String                      urlMarker,
                                                                                   String                      relationshipTypeName,
                                                                                   FindPropertyNamesProperties requestBody)
    {
        final String methodName = "getRelationshipsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        MetadataRelationshipSummariesResponse response = new MetadataRelationshipSummariesResponse();
        AuditLog                              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                MetadataRelationshipSummaryList summaryList = handler.getRelationships(userId,
                                                                                       relationshipTypeName,
                                                                                       requestBody.getPropertyValue(),
                                                                                       requestBody.getPropertyNames(),
                                                                                       requestBody,
                                                                                       methodName);

                if (summaryList != null)
                {
                    response.setRelationships(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param relationshipTypeName name of relationship
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse findRelationshipsWithPropertyValue(String                      serverName,
                                                                                    String                      urlMarker,
                                                                                    String                      relationshipTypeName,
                                                                                    FindPropertyNamesProperties requestBody)
    {
        final String methodName = "findRelationshipsWithPropertyValue";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        MetadataRelationshipSummariesResponse response = new MetadataRelationshipSummariesResponse();
        AuditLog                              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                MetadataRelationshipSummaryList summaryList = handler.findRelationshipsWithPropertyValue(userId,
                                                                                                         relationshipTypeName,
                                                                                                         requestBody.getPropertyValue(),
                                                                                                         requestBody.getPropertyNames(),
                                                                                                         requestBody);

                if (summaryList != null)
                {
                    response.setRelationships(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
     * @param urlMarker  view service URL marker
     * @param relationshipGUID unique identifier for the relationship
     * @param requestBody options to control the query
     *
     * @return relationship summary or
     *  InvalidParameterException the unique identifier is null or not known.
     *  UserNotAuthorizedException the governance action service is not able to access the element
     *  PropertyServerException a problem accessing the metadata store
     */
    public MetadataRelationshipSummaryResponse getRelationshipSummaryByGUID(String         serverName,
                                                                            String         urlMarker,
                                                                            String         relationshipGUID,
                                                                            GetRequestBody requestBody)
    {
        final String methodName = "getRelationshipSummaryByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        MetadataRelationshipSummaryResponse response = new MetadataRelationshipSummaryResponse();
        AuditLog                            auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                response.setRelationship(handler.getRelationshipSummaryByGUID(userId, relationshipGUID, null));
            }
            else
            {
                response.setRelationship(handler.getRelationshipSummaryByGUID(userId, relationshipGUID, requestBody));
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
     * Retrieve the header for the instance identified by the supplied unique identifier.
     * It may be an element (entity) or a relationship between elements.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param guid identifier to use in the lookup

     * @param requestBody effective time
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public ElementHeaderResponse retrieveInstanceForGUID(String         serverName,
                                                         String         urlMarker,
                                                         String         guid,
                                                         GetRequestBody requestBody)
    {
        final String methodName = "retrieveInstanceForGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        ElementHeaderResponse response = new ElementHeaderResponse();
        AuditLog              auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                response.setElement(handler.retrieveInstanceForGUID(userId,
                                                                    guid,
                                                                    null));
            }
            else
            {
                response.setElement(handler.retrieveInstanceForGUID(userId, guid, requestBody));
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
     * Retrieve the list of elements with the named category.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getElementsByCategory(String            serverName,
                                                                  String            urlMarker,
                                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getElementsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getRootElementsByName(userId,
                                                                   requestBody.getFilter(),
                                                                   Collections.singletonList(OpenMetadataProperty.CATEGORY.name),
                                                                   requestBody,
                                                                   methodName));

                response.setMermaidGraph(handler.getMermaidGraph(requestBody.getFilter(), response.getElements(), requestBody.getMaxMermaidNodeCount()));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, FilterRequestBody.class.getName());
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
     * Return the requested search keyword.
     *
     * @param serverName name of the server instances for this request
     * @param searchKeywordGUID  unique identifier for the search keyword object.
     * @param urlMarker  view service URL marker
     * @param requestBody optional effective time
     * @return search keyword properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElementResponse getSearchKeywordByGUID(String         serverName,
                                                                  String         urlMarker,
                                                                  String         searchKeywordGUID,
                                                                  GetRequestBody requestBody)
    {
        final String methodName = "getSearchKeyword";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementResponse response = new OpenMetadataRootElementResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            SearchKeywordHandler handler = instanceHandler.getSearchKeywordHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            response.setElement(handler.getSearchKeywordByGUID(userId, searchKeywordGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the list of search keyword metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSearchKeywordsByKeyword(String            serverName,
                                                                       String            urlMarker,
                                                                       FilterRequestBody requestBody)
    {
        final String methodName = "getSearchKeywordsByKeyword";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            SearchKeywordHandler handler = instanceHandler.getSearchKeywordHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSearchKeywordsByName(userId,
                                                                     requestBody.getFilter(),
                                                                     requestBody));
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, FilterRequestBody.class.getName());
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
     * Retrieve the list of search keyword metadata elements that contain the search string.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSearchKeywords(String                  serverName,
                                                               String                  urlMarker,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findSearchKeywords";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        OpenMetadataRootElementsResponse response = new OpenMetadataRootElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            SearchKeywordHandler handler = instanceHandler.getSearchKeywordHandler(userId, serverName, urlMarker, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.findSearchKeywords(userId,
                                                                requestBody.getSearchString(),
                                                                requestBody));
            }
            else
            {
                response.setElements(handler.findSearchKeywords(userId, null, null));
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
