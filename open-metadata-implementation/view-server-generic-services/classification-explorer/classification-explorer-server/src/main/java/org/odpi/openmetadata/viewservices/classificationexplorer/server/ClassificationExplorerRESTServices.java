/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
package org.odpi.openmetadata.viewservices.classificationexplorer.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.enums.TermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SearchKeywordHandler;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.StewardshipManagementHandler;
import org.odpi.openmetadata.frameworks.openmetadata.mermaid.CertificationMermaidGraphBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CertificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.FindDigitalResourceOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.LevelIdentifierQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SemanticAssignmentQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
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
    public MetadataElementSummariesResponse getImpactClassifiedElements(String                         serverName,
                                                                        String                         urlMarker,
                                                                        LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getImpactClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
    public MetadataElementSummariesResponse getConfidenceClassifiedElements(String                         serverName,
                                                                            String                         urlMarker,
                                                                            LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getConfidenceClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
    public MetadataElementSummariesResponse getCriticalityClassifiedElements(String                         serverName,
                                                                             String                         urlMarker,
                                                                             LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getCriticalityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
    public MetadataElementSummariesResponse getConfidentialityClassifiedElements(String                         serverName,
                                                                                 String                         urlMarker,
                                                                                 LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getConfidentialityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
    public MetadataElementSummariesResponse getRetentionClassifiedElements(String                         serverName,
                                                                           String                         urlMarker,
                                                                           LevelIdentifierQueryProperties requestBody)
    {
        final String methodName = "getRetentionClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
    public MetadataElementSummariesResponse getSecurityTaggedElements(String                     serverName,
                                                                      String                     urlMarker,
                                                                      SecurityTagQueryProperties requestBody)
    {
        final String methodName = "getSecurityTaggedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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
                                                                       requestBody.getAccessGroups(),
                                                                       requestBody));
            }
            else
            {
                response.setElements(handler.getSecurityTaggedElements(userId,
                                                                       null,
                                                                       null,
                                                                       null,
                                                                       null));
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
    public MetadataElementSummariesResponse getOwnersElements(String            serverName,
                                                              String            urlMarker,
                                                              FilterRequestBody requestBody)
    {
        final String methodName = "getOwnersElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
    public MetadataElementSummariesResponse getMembersOfSubjectArea(String            serverName,
                                                                    String            urlMarker,
                                                                    FilterRequestBody requestBody)
    {
        final String methodName = "getMembersOfSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getMembersOfSubjectArea(userId,
                                                                     requestBody.getFilter(),
                                                                     requestBody));
            }
            else
            {
                response.setElements(handler.getMembersOfSubjectArea(userId, null, null));
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
     * Return information about the elements from a specific origin.
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
    public MetadataElementSummariesResponse getElementsByOrigin(String                              serverName,
                                                                String                              urlMarker,
                                                                FindDigitalResourceOriginProperties requestBody)
    {
        final String methodName = "getElementsByOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummariesResponse response = new MetadataElementSummariesResponse();
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
    public RelatedMetadataElementSummariesResponse getMeanings(String                            serverName,
                                                               String                            urlMarker,
                                                               String                            elementGUID,
                                                               SemanticAssignmentQueryProperties requestBody)
    {
        final String methodName = "getMeanings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getMeanings(userId,
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
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getMeanings(userId,
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
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getSemanticAssignees(String                            serverName,
                                                                        String                            urlMarker,
                                                                        String                            glossaryTermGUID,
                                                                        SemanticAssignmentQueryProperties requestBody)
    {
        final String methodName = "getSemanticAssignees";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getSemanticAssignees(userId,
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
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getSemanticAssignees(userId,
                                                                                             glossaryTermGUID,
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
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getGovernedElements(String         serverName,
                                                                       String         urlMarker,
                                                                       String         governanceDefinitionGUID,
                                                                       ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getGovernedElements(userId,
                                                                                            governanceDefinitionGUID,
                                                                                            null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getGovernedElements(userId,
                                                                                            governanceDefinitionGUID,
                                                                                            requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getGovernedByDefinitions(String         serverName,
                                                                            String         urlMarker,
                                                                            String         elementGUID,
                                                                            ResultsRequestBody requestBody)
    {
        final String methodName = "getGovernedByDefinitions";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getGovernedByDefinitions(userId,
                                                                                                 elementGUID,
                                                                                                 null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getGovernedByDefinitions(userId,
                                                                                                 elementGUID,
                                                                                                 requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getSourceElements(String         serverName,
                                                                     String         urlMarker,
                                                                     String         elementGUID,
                                                                     ResultsRequestBody requestBody)
    {
        final String methodName = "getSourceElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getSourceElements(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getSourceElements(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getElementsSourcedFrom(String         serverName,
                                                                          String         urlMarker,
                                                                          String         elementGUID,
                                                                          ResultsRequestBody requestBody)
    {
        final String methodName = "getElementsSourceFrom";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getElementsSourcedFrom(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getElementsSourcedFrom(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getScopes(String         serverName,
                                                             String         urlMarker,
                                                             String         elementGUID,
                                                             ResultsRequestBody requestBody)
    {
        final String methodName = "getScopes";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getScopes(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getScopes(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getScopedElements(String         serverName,
                                                                     String         urlMarker,
                                                                     String         scopeGUID,
                                                                     ResultsRequestBody requestBody)
    {
        final String methodName = "getScopedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getScopedElements(userId, scopeGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getScopedElements(userId, scopeGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getResourceList(String         serverName,
                                                                   String         urlMarker,
                                                                   String         elementGUID,
                                                                   ResultsRequestBody requestBody)
    {
        final String methodName = "getResourceList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getResourceList(userId, elementGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getResourceList(userId, elementGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getSupportedByResource(String         serverName,
                                                                          String         urlMarker,
                                                                          String         resourceGUID,
                                                                          ResultsRequestBody requestBody)
    {
        final String methodName = "getSupportedByResource";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getSupportedByResource(userId, resourceGUID, null);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getSupportedByResource(userId, resourceGUID, requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
    public RelatedMetadataElementSummariesResponse getLicensedElements(String             serverName,
                                                                       String             urlMarker,
                                                                       String             licenseTypeGUID,
                                                                       ResultsRequestBody requestBody)
    {
        final String methodName = "getLicensedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            RelatedMetadataElementSummaryList summaryList = handler.getLicensedElements(userId, licenseTypeGUID, requestBody);
            if (summaryList != null)
            {
                response.setElements(summaryList.getElementList());
                response.setMermaidGraph(summaryList.getMermaidGraph());
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
    public RelatedMetadataElementSummariesResponse getLicenses(String             serverName,
                                                               String             urlMarker,
                                                               String             elementGUID,
                                                               ResultsRequestBody requestBody)
    {
        final String methodName = "getLicenses";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            RelatedMetadataElementSummaryList summaryList = handler.getLicenses(userId, elementGUID, requestBody);
            if (summaryList != null)
            {
                response.setElements(summaryList.getElementList());
                response.setMermaidGraph(summaryList.getMermaidGraph());
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
    public CertificationElementsResponse getCertifiedElements(String             serverName,
                                                              String             urlMarker,
                                                              String             certificationTypeGUID,
                                                              ResultsRequestBody requestBody)
    {
        final String methodName = "getCertifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CertificationElementsResponse response = new CertificationElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            /*
             * Check that the guid is valid and retrieve the associated element.
             */
            MetadataElementSummary startingElement = handler.getMetadataElementByGUID(userId, certificationTypeGUID, requestBody);;

            if (startingElement != null)
            {
                /*
                 * Does this element have associated certifications
                 */
                RelatedMetadataElementSummaryList summaryList;

                if (requestBody == null)
                {
                    summaryList = handler.getCertifiedElements(userId, certificationTypeGUID, null);

                    getCertificationElements(userId,
                                             startingElement,
                                             summaryList,
                                             handler,
                                             null,
                                             response);
                }
                else
                {
                    summaryList = handler.getCertifiedElements(userId, certificationTypeGUID, requestBody);

                    getCertificationElements(userId,
                                             startingElement,
                                             summaryList,
                                             handler,
                                             requestBody,
                                             response);
                }
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
     * Convert the related metadata element summary into a certification response that includes details of the actors
     * involved in the certification.
     *
     * @param userId calling user
     * @param startingElement requested starting element
     * @param summaryList list of related elements
     * @param handler handler for receiving more metadata
     * @param queryOptions               multiple options to control the query
     * @param response response object to populate
     */
    private void getCertificationElements(String                            userId,
                                          MetadataElementSummary            startingElement,
                                          RelatedMetadataElementSummaryList summaryList,
                                          StewardshipManagementHandler      handler,
                                          QueryOptions                      queryOptions,
                                          CertificationElementsResponse     response)
    {
        response.setStartingElement(startingElement);

        List<CertificationElement> certificationElements = new ArrayList<>();

        /*
         * Set up the list of certified elements
         */
        if ((summaryList != null) && (summaryList.getElementList() != null))
        {
            /*
             * Each certification relationship potentially identifies 3 actors.
             * The code below retrieves the actors to include them in the output.
             */
            Map<String, MetadataElementSummary> actorMap              = new HashMap<>(); // only receive each actor once

            for (RelatedMetadataElementSummary relatedMetadataElementSummary : summaryList.getElementList())
            {
                if (relatedMetadataElementSummary != null)
                {
                    CertificationElement certificationElement = new CertificationElement(relatedMetadataElementSummary);


                    if (relatedMetadataElementSummary.getRelationshipProperties() instanceof CertificationProperties certificationProperties)
                    {
                        certificationElement.setCertifiedBy(getActorSummary(userId,
                                                                            certificationProperties.getCertifiedBy(),
                                                                            certificationProperties.getCertifiedByPropertyName(),
                                                                            actorMap,
                                                                            handler,
                                                                            queryOptions));

                        certificationElement.setCustodian(getActorSummary(userId,
                                                                          certificationProperties.getCustodian(),
                                                                          certificationProperties.getCustodianPropertyName(),
                                                                          actorMap,
                                                                          handler,
                                                                          queryOptions));

                        certificationElement.setRecipient(getActorSummary(userId,
                                                                          certificationProperties.getRecipient(),
                                                                          certificationProperties.getRecipientPropertyName(),
                                                                          actorMap,
                                                                          handler,
                                                                          queryOptions));
                    }

                    certificationElements.add(certificationElement);
                }
            }

            response.setElements(certificationElements);
        }

        /*
         * Set up the mermaid graph
         */
        CertificationMermaidGraphBuilder graphBuilder = new CertificationMermaidGraphBuilder(startingElement,
                                                                                             certificationElements);

        response.setMermaidGraph(graphBuilder.getMermaidGraph());
    }


    /**
     * Retrieve details of a requested actor, unless element has already been retrieved.
     *
     * @param userId calling user
     * @param actorName name of actor
     * @param actorPropertyName property name it represents
     * @param actorMap map of other actors already received
     * @param handler handler for receiving more metadata
     * @param queryOptions               multiple options to control the query
     * @return actor element or null if nothing found
     */
    private MetadataElementSummary getActorSummary(String                              userId,
                                                   String                              actorName,
                                                   String                              actorPropertyName,
                                                   Map<String, MetadataElementSummary> actorMap,
                                                   StewardshipManagementHandler        handler,
                                                   QueryOptions                        queryOptions)
    {
        if (actorName != null)
        {
            if (actorMap.containsKey(actorName))
            {
                return actorMap.get(actorName);
            }
            else
            {
                MetadataElementSummary actorSummary;

                if ((actorPropertyName == null) || (actorPropertyName.equals(OpenMetadataProperty.GUID.name)))
                {
                    try
                    {
                        actorSummary = handler.getMetadataElementByGUID(userId, actorName, queryOptions);
                    }
                    catch (Exception exception)
                    {
                        return null;
                    }
                }
                else
                {
                    try
                    {
                        actorSummary = handler.getMetadataElementByUniqueName(userId,
                                                                              actorName,
                                                                              actorPropertyName,
                                                                              queryOptions);
                    }
                    catch (Exception exception)
                    {
                        return null;
                    }
                }

                if (actorSummary != null)
                {
                    actorMap.put(actorName, actorSummary);
                    return actorSummary;
                }
            }
        }

        return null;
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
    public CertificationElementsResponse getCertifications(String             serverName,
                                                           String             urlMarker,
                                                           String             elementGUID,
                                                           ResultsRequestBody requestBody)
    {
        final String methodName = "getCertifications";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CertificationElementsResponse response = new CertificationElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            /*
             * Check that the guid is valid and retrieve the associated element.
             */
            MetadataElementSummary startingElement;
            if (requestBody == null)
            {
                startingElement = handler.getMetadataElementByGUID(userId, elementGUID, null);
            }
            else
            {
                startingElement = handler.getMetadataElementByGUID(userId, elementGUID, requestBody);
            }

            if (startingElement != null)
            {
                /*
                 * Does this element have associated certifications
                 */
                RelatedMetadataElementSummaryList summaryList;

                if (requestBody == null)
                {
                    summaryList = handler.getCertifications(userId, elementGUID, null);
                    getCertificationElements(userId,
                                             startingElement,
                                             summaryList,
                                             handler,
                                             null,
                                             response);
                }
                else
                {
                    summaryList = handler.getCertifications(userId, elementGUID, requestBody);
                    getCertificationElements(userId,
                                             startingElement,
                                             summaryList,
                                             handler,
                                             requestBody,
                                             response);
                }
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
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public MetadataElementSummaryResponse getMetadataElementByGUID(String      serverName,
                                                                   String      urlMarker,
                                                                   String      elementGUID,
                                                                   GetRequestBody requestBody)
    {
        final String methodName = "getMetadataElementByGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummaryResponse response = new MetadataElementSummaryResponse();
        AuditLog                       auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getMetadataElementByGUID(userId, elementGUID, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public MetadataElementSummaryResponse getMetadataElementByUniqueName(String                     serverName,
                                                                         String                     urlMarker,
                                                                         FindPropertyNameProperties requestBody)
    {
        final String methodName = "getMetadataElementByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        MetadataElementSummaryResponse response = new MetadataElementSummaryResponse();
        AuditLog                       auditLog = null;

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException there is a problem accessing the metadata store
     */
    public GUIDResponse getMetadataElementGUIDByUniqueName(String                     serverName,
                                                           String                     urlMarker,
                                                           FindPropertyNameProperties requestBody)
    {
        final String methodName = "getMetadataElementGUIDByUniqueName";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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
                response.setGUID(handler.getMetadataElementGUIDByUniqueName(userId,
                                                                            requestBody.getPropertyValue(),
                                                                            OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                            requestBody));
            }
            else
            {
                response.setGUID(handler.getMetadataElementGUIDByUniqueName(userId,
                                                                            requestBody.getPropertyValue(),
                                                                            requestBody.getPropertyName(),
                                                                            requestBody));
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
     * Retrieve elements of the requested type name.
     *
     * @param serverName  name of the server instance to connect to
     * @param urlMarker  view service URL marker
     * @param requestBody  open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElements(String         serverName,
                                                        String         urlMarker,
                                                        ResultsRequestBody requestBody)
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
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getElements(userId, null));
            }
            else
            {
                response.setElements(handler.getElements(userId, requestBody));
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElementsByPropertyValue(String                      serverName,
                                                                       String                      urlMarker,
                                                                       FindPropertyNamesProperties requestBody)
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse findElementsByPropertyValue(String                      serverName,
                                                                        String                      urlMarker,
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElementsByClassification(String         serverName,
                                                                        String         urlMarker,
                                                                        String         classificationName,
                                                                        ResultsRequestBody requestBody)
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
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getElementsByClassification(userId, classificationName, requestBody));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
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
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse getElementsByClassificationWithPropertyValue(String                      serverName,
                                                                                         String                      urlMarker,
                                                                                         String                      classificationName,
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
     * @param urlMarker  view service URL marker
     * @param classificationName name of classification
     * @param requestBody properties and optional open metadata type to search on
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataElementSummariesResponse findElementsByClassificationWithPropertyValue(String                      serverName,
                                                                                          String                      urlMarker,
                                                                                          String                      classificationName,
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummariesResponse getRelatedElements(String         serverName,
                                                                      String         urlMarker,
                                                                      String         elementGUID,
                                                                      String         relationshipTypeName,
                                                                      int            startingAtEnd,
                                                                      ResultsRequestBody requestBody)
    {
        final String methodName = "getRelatedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        RelatedMetadataElementSummariesResponse response = new RelatedMetadataElementSummariesResponse();
        AuditLog                                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                RelatedMetadataElementSummaryList summaryList = handler.getRelatedElements(userId,
                                                                                           elementGUID,
                                                                                           relationshipTypeName,
                                                                                           startingAtEnd,
                                                                                           null,
                                                                                           null,
                                                                                           null,
                                                                                           methodName);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getRelatedElements(userId,
                                                                                           elementGUID,
                                                                                           relationshipTypeName,
                                                                                           startingAtEnd,
                                                                                           null,
                                                                                           null,
                                                                                           requestBody,
                                                                                           methodName);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummariesResponse getRelatedElementsWithPropertyValue(String                      serverName,
                                                                                       String                      urlMarker,
                                                                                       String                      elementGUID,
                                                                                       String                      relationshipTypeName,
                                                                                       int                         startingAtEnd,
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
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.getRelatedElements(userId,
                                                                                           elementGUID,
                                                                                           relationshipTypeName,
                                                                                           startingAtEnd,
                                                                                           requestBody.getPropertyValue(),
                                                                                           requestBody.getPropertyNames(),
                                                                                           requestBody,
                                                                                           methodName);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public RelatedMetadataElementSummariesResponse findRelatedElementsWithPropertyValue(String                      serverName,
                                                                                        String                      urlMarker,
                                                                                        String                      elementGUID,
                                                                                        String                      relationshipTypeName,
                                                                                        int                         startingAtEnd,
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
            StewardshipManagementHandler handler = instanceHandler.getStewardshipManagementHandler(userId, serverName, urlMarker, methodName);

            if (requestBody == null)
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName, FindPropertyNamesProperties.class.getName());
            }
            else
            {
                RelatedMetadataElementSummaryList summaryList = handler.findRelatedElementsWithPropertyValue(userId,
                                                                                                             elementGUID,
                                                                                                             relationshipTypeName,
                                                                                                             startingAtEnd,
                                                                                                             requestBody.getPropertyValue(),
                                                                                                             requestBody.getPropertyNames(),
                                                                                                             requestBody);
                if (summaryList != null)
                {
                    response.setElements(summaryList.getElementList());
                    response.setMermaidGraph(summaryList.getMermaidGraph());
                }
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse getRelationships(String             serverName,
                                                                  String             urlMarker,
                                                                  String             relationshipTypeName,
                                                                  ResultsRequestBody requestBody)
    {
        final String methodName = "getRelationships";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse getRelationshipsWithPropertyValue(String                      serverName,
                                                                                   String                      urlMarker,
                                                                                   String                      relationshipTypeName,
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public MetadataRelationshipSummariesResponse findRelationshipsWithPropertyValue(String                      serverName,
                                                                                    String                      urlMarker,
                                                                                    String                      relationshipTypeName,
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public ElementHeaderResponse retrieveInstanceForGUID(String             serverName,
                                                         String             urlMarker,
                                                         String             guid,
                                                         GetRequestBody requestBody)
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getElementsByCategory(String            serverName,
                                                                  String            urlMarker,
                                                                  FilterRequestBody requestBody)
    {
        final String methodName = "getElementsByCategory";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

                response.setMermaidGraph(handler.getMermaidGraph(requestBody.getFilter(), response.getElements()));
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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException there is a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    public OpenMetadataRootElementResponse getSearchKeywordByGUID(String         serverName,
                                                                  String         urlMarker,
                                                                  String         searchKeywordGUID,
                                                                  GetRequestBody requestBody)
    {
        final String methodName = "getSearchKeyword";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse getSearchKeywordsByKeyword(String                  serverName,
                                                                       String                  urlMarker,
                                                                       FilterRequestBody requestBody)
    {
        final String methodName = "getSearchKeywordsByKeyword";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public OpenMetadataRootElementsResponse findSearchKeywords(String                  serverName,
                                                               String                  urlMarker,
                                                               SearchStringRequestBody requestBody)
    {
        final String methodName = "findSearchKeywords";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

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

        restCallLogger.logRESTCallReturn(token, response.toString());
        return response;
    }
}
