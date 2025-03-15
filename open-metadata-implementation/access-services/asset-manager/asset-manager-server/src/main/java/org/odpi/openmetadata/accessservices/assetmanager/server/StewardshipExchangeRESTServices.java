/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.handlers.DataAssetExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.handlers.GlossaryExchangeHandler;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.commonservices.ffdc.rest.ElementStubsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelatedElementsResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.RelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindNameProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.accessservices.assetmanager.rest.AssetElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.ClassificationRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.EffectiveTimeQueryRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceDefinitionsResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.GovernanceDefinitionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldValuesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SoftwareCapabilityHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.slf4j.LoggerFactory;


/**
 * StewardshipExchangeRESTServices provides part of the server-side support for the Asset Owner Open Metadata Access Service (OMAS).
 * There are other REST services that provide specialized methods for specific types of Asset.
 */
public class StewardshipExchangeRESTServices
{
    private static final AssetManagerInstanceHandler instanceHandler      = new AssetManagerInstanceHandler();
    private static final RESTExceptionHandler        restExceptionHandler = new RESTExceptionHandler();
    private static final RESTCallLogger              restCallLogger       = new RESTCallLogger(LoggerFactory.getLogger(StewardshipExchangeRESTServices.class),
                                                                                               instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public StewardshipExchangeRESTServices()
    {
    }


    /**
     * Classify the element to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
    public VoidResponse setDataFieldClassification(String                    serverName,
                                                   String                    userId,
                                                   String                    elementGUID,
                                                   boolean                   forLineage,
                                                   boolean                   forDuplicateProcessing,
                                                   ClassificationRequestBody requestBody)
    {
        final String methodName = "setDataFieldClassification";
        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof DataFieldValuesProperties properties)
                {

                    handler.addDataFieldValuesClassification(userId,
                                                             elementGUID,
                                                             elementGUIDParameter,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             properties.getDefaultValue(),
                                                             properties.getSampleValues(),
                                                             properties.getDataPatterns(),
                                                             properties.getNamePatterns(),
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             requestBody.getEffectiveTime(),
                                                             methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.addDataFieldValuesClassification(userId,
                                                             elementGUID,
                                                             elementGUIDParameter,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             null,
                                                             null,
                                                             null,
                                                             null,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             requestBody.getEffectiveTime(),
                                                             methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(GovernanceClassificationProperties.class.getName(), methodName);
                }
            }
            else
            {
                handler.addDataFieldValuesClassification(userId,
                                                         elementGUID,
                                                         elementGUIDParameter,
                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                         null,
                                                         null,
                                                         null,
                                                         null,
                                                         forLineage,
                                                         forDuplicateProcessing,
                                                         null,
                                                         methodName);
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
     * Remove the data fields classification from the element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
    public VoidResponse clearDataFieldClassification(String                    serverName,
                                                     String                    userId,
                                                     String                    elementGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String   methodName = "clearDataFieldClassification";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeDataFieldValuesClassification(userId,
                                                            elementGUID,
                                                            elementGUIDParameter,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            requestBody.getEffectiveTime(),
                                                            methodName);
            }
            else
            {
                handler.removeDataFieldValuesClassification(userId,
                                                            elementGUID,
                                                            elementGUIDParameter,
                                                            OpenMetadataType.REFERENCEABLE.typeName,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            null,
                                                            methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getDataFieldClassifiedElements(String                      serverName,
                                                               String                      userId,
                                                               int                         startFrom,
                                                               int                         pageSize,
                                                               boolean                     forLineage,
                                                               boolean                     forDuplicateProcessing,
                                                               FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getDataFieldClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierQueryProperties properties)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                                       properties.getReturnSpecificLevel(),
                                                                                       properties.getLevelIdentifier(),
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                                       false,
                                                                                       0,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierQueryProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                   OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                                   false,
                                                                                   0,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   null,
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
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                                    String                    userId,
                                                    String                    elementGUID,
                                                    boolean                   forLineage,
                                                    boolean                   forDuplicateProcessing,
                                                    ClassificationRequestBody requestBody)
    {
        final String methodName = "setConfidenceClassification";
        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceClassificationProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    if (requestBody.getMetadataCorrelationProperties() != null)
                    {
                        handler.addGovernanceActionClassification(userId,
                                                                  requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                  requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                  elementGUID,
                                                                  elementGUIDParameter,
                                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                                  OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeGUID,
                                                                  OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                  properties.getStatus(),
                                                                  properties.getConfidence(),
                                                                  properties.getSteward(),
                                                                  properties.getStewardTypeName(),
                                                                  properties.getStewardPropertyName(),
                                                                  properties.getSource(),
                                                                  properties.getNotes(),
                                                                  properties.getLevelIdentifier(),
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo(),
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName);
                    }
                    else
                    {
                        handler.addGovernanceActionClassification(userId,
                                                                  null,
                                                                  null,
                                                                  elementGUID,
                                                                  elementGUIDParameter,
                                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                                  OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeGUID,
                                                                  OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                  properties.getStatus(),
                                                                  properties.getConfidence(),
                                                                  properties.getSteward(),
                                                                  properties.getStewardTypeName(),
                                                                  properties.getStewardPropertyName(),
                                                                  properties.getSource(),
                                                                  properties.getNotes(),
                                                                  properties.getLevelIdentifier(),
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo(),
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName);
                    }
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
     * @param userId calling user
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
                                                      String                    userId,
                                                      String                    elementGUID,
                                                      boolean                   forLineage,
                                                      boolean                   forDuplicateProcessing,
                                                      ClassificationRequestBody requestBody)
    {
        final String   methodName = "clearConfidenceClassification";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getMetadataCorrelationProperties() != null)
                {
                    handler.removeGovernanceActionClassification(userId,
                                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                 elementGUID,
                                                                 elementGUIDParameter,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeGUID,
                                                                 OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 requestBody.getEffectiveTime(),
                                                                 methodName);
                }
                else
                {
                    handler.removeGovernanceActionClassification(userId,
                                                                 null,
                                                                 null,
                                                                 elementGUID,
                                                                 elementGUIDParameter,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeGUID,
                                                                 OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 requestBody.getEffectiveTime(),
                                                                 methodName);
                }
            }
            else
            {
                handler.removeGovernanceActionClassification(userId,
                                                             null,
                                                             null,
                                                             elementGUID,
                                                             elementGUIDParameter,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeGUID,
                                                             OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getConfidenceClassifiedElements(String                      serverName,
                                                                String                      userId,
                                                                int                         startFrom,
                                                                int                         pageSize,
                                                                boolean                     forLineage,
                                                                boolean                     forDuplicateProcessing,
                                                                FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getConfidenceClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierQueryProperties properties)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                                       properties.getReturnSpecificLevel(),
                                                                                       properties.getLevelIdentifier(),
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                                       false,
                                                                                       0,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierQueryProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                   OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                                                                   false,
                                                                                   0,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   null,
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
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                                     String                    userId,
                                                     String                    elementGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String methodName = "setCriticalityClassification";
        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceClassificationProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    if (requestBody.getMetadataCorrelationProperties() != null)
                    {
                        handler.addGovernanceActionClassification(userId,
                                                                  requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                  requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                  elementGUID,
                                                                  elementGUIDParameter,
                                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                                  OpenMetadataType.CRITICALITY_CLASSIFICATION.typeGUID,
                                                                  OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                  properties.getStatus(),
                                                                  properties.getConfidence(),
                                                                  properties.getSteward(),
                                                                  properties.getStewardTypeName(),
                                                                  properties.getStewardPropertyName(),
                                                                  properties.getSource(),
                                                                  properties.getNotes(),
                                                                  properties.getLevelIdentifier(),
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo(),
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName);
                    }
                    else
                    {
                        handler.addGovernanceActionClassification(userId,
                                                                  null,
                                                                  null,
                                                                  elementGUID,
                                                                  elementGUIDParameter,
                                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                                  OpenMetadataType.CRITICALITY_CLASSIFICATION.typeGUID,
                                                                  OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                  properties.getStatus(),
                                                                  properties.getConfidence(),
                                                                  properties.getSteward(),
                                                                  properties.getStewardTypeName(),
                                                                  properties.getStewardPropertyName(),
                                                                  properties.getSource(),
                                                                  properties.getNotes(),
                                                                  properties.getLevelIdentifier(),
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo(),
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName);
                    }
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
     * @param userId calling user
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
                                                       String                    userId,
                                                       String                    elementGUID,
                                                       boolean                   forLineage,
                                                       boolean                   forDuplicateProcessing,
                                                       ClassificationRequestBody requestBody)
    {
        final String   methodName = "clearCriticalityClassification";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getMetadataCorrelationProperties() != null)
                {
                    handler.removeGovernanceActionClassification(userId,
                                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                 elementGUID,
                                                                 elementGUIDParameter,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.typeGUID,
                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 requestBody.getEffectiveTime(),
                                                                 methodName);
                }
                else
                {
                    handler.removeGovernanceActionClassification(userId,
                                                                 null,
                                                                 null,
                                                                 elementGUID,
                                                                 elementGUIDParameter,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.typeGUID,
                                                                 OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 requestBody.getEffectiveTime(),
                                                                 methodName);
                }
            }
            else
            {
                handler.removeGovernanceActionClassification(userId,
                                                             null,
                                                             null,
                                                             elementGUID,
                                                             elementGUIDParameter,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             OpenMetadataType.CRITICALITY_CLASSIFICATION.typeGUID,
                                                             OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getCriticalityClassifiedElements(String                      serverName,
                                                                 String                      userId,
                                                                 int                         startFrom,
                                                                 int                         pageSize,
                                                                 boolean                     forLineage,
                                                                 boolean                     forDuplicateProcessing,
                                                                 FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getCriticalityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierQueryProperties properties)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                                       properties.getReturnSpecificLevel(),
                                                                                       properties.getLevelIdentifier(),
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                                       false,
                                                                                       0,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierQueryProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                   OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                                                                   false,
                                                                                   0,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   null,
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
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                                         String                    userId,
                                                         String                    elementGUID,
                                                         boolean                   forLineage,
                                                         boolean                   forDuplicateProcessing,
                                                         ClassificationRequestBody requestBody)
    {
        final String methodName = "setConfidentialityClassification";
        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof GovernanceClassificationProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    if (requestBody.getMetadataCorrelationProperties() != null)
                    {
                        handler.addGovernanceActionClassification(userId,
                                                                  requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                  requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                  elementGUID,
                                                                  elementGUIDParameter,
                                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                                  OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeGUID,
                                                                  OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                  properties.getStatus(),
                                                                  properties.getConfidence(),
                                                                  properties.getSteward(),
                                                                  properties.getStewardTypeName(),
                                                                  properties.getStewardPropertyName(),
                                                                  properties.getSource(),
                                                                  properties.getNotes(),
                                                                  properties.getLevelIdentifier(),
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo(),
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName);
                    }
                    else
                    {
                        handler.addGovernanceActionClassification(userId,
                                                                  null,
                                                                  null,
                                                                  elementGUID,
                                                                  elementGUIDParameter,
                                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                                  OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeGUID,
                                                                  OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                  properties.getStatus(),
                                                                  properties.getConfidence(),
                                                                  properties.getSteward(),
                                                                  properties.getStewardTypeName(),
                                                                  properties.getStewardPropertyName(),
                                                                  properties.getSource(),
                                                                  properties.getNotes(),
                                                                  properties.getLevelIdentifier(),
                                                                  forLineage,
                                                                  forDuplicateProcessing,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo(),
                                                                  requestBody.getEffectiveTime(),
                                                                  methodName);
                    }
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
     * @param userId calling user
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
    public VoidResponse clearConfidentialityClassification(String                    serverName,
                                                           String                    userId,
                                                           String                    elementGUID,
                                                           boolean                   forLineage,
                                                           boolean                   forDuplicateProcessing,
                                                           ClassificationRequestBody requestBody)
    {
        final String   methodName = "clearConfidentialityClassification";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getMetadataCorrelationProperties() != null)
                {
                    handler.removeGovernanceActionClassification(userId,
                                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                                 requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                                 elementGUID,
                                                                 elementGUIDParameter,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeGUID,
                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 requestBody.getEffectiveTime(),
                                                                 methodName);
                }
                else
                {
                    handler.removeGovernanceActionClassification(userId,
                                                                 null,
                                                                 null,
                                                                 elementGUID,
                                                                 elementGUIDParameter,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeGUID,
                                                                 OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 requestBody.getEffectiveTime(),
                                                                 methodName);
                }
            }
            else
            {
                handler.removeGovernanceActionClassification(userId,
                                                             null,
                                                             null,
                                                             elementGUID,
                                                             elementGUIDParameter,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeGUID,
                                                             OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             null,
                                                             methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getConfidentialityClassifiedElements(String                      serverName,
                                                                     String                      userId,
                                                                     int                         startFrom,
                                                                     int                         pageSize,
                                                                     boolean                     forLineage,
                                                                     boolean                     forDuplicateProcessing,
                                                                     FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getConfidentialityClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierQueryProperties properties)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                                       properties.getReturnSpecificLevel(),
                                                                                       properties.getLevelIdentifier(),
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                       OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                                       false,
                                                                                       0,
                                                                                       startFrom,
                                                                                       pageSize,
                                                                                       forLineage,
                                                                                       forDuplicateProcessing,
                                                                                       requestBody.getEffectiveTime(),
                                                                                       methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierQueryProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElements(handler.getGovernanceActionClassifiedElements(userId,
                                                                                   OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                                   false,
                                                                                   0,
                                                                                   startFrom,
                                                                                   pageSize,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   null,
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
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                                   String                    userId,
                                                   String                    elementGUID,
                                                   boolean                   forLineage,
                                                   boolean                   forDuplicateProcessing,
                                                   ClassificationRequestBody requestBody)
    {
        final String methodName = "setRetentionClassification";
        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof RetentionClassificationProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    if (requestBody.getMetadataCorrelationProperties() != null)
                    {
                        handler.addRetentionClassification(userId,
                                                           requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                           requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                           elementGUID,
                                                           elementGUIDParameter,
                                                           OpenMetadataType.REFERENCEABLE.typeName,
                                                           properties.getStatus(),
                                                           properties.getConfidence(),
                                                           properties.getSteward(),
                                                           properties.getStewardTypeName(),
                                                           properties.getStewardPropertyName(),
                                                           properties.getSource(),
                                                           properties.getNotes(),
                                                           properties.getRetentionBasis(),
                                                           properties.getAssociatedGUID(),
                                                           properties.getArchiveAfter(),
                                                           properties.getDeleteAfter(),
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           properties.getEffectiveFrom(),
                                                           properties.getEffectiveTo(),
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
                    }
                    else
                    {
                        handler.addRetentionClassification(userId,
                                                           null,
                                                           null,
                                                           elementGUID,
                                                           elementGUIDParameter,
                                                           OpenMetadataType.REFERENCEABLE.typeName,
                                                           properties.getStatus(),
                                                           properties.getConfidence(),
                                                           properties.getSteward(),
                                                           properties.getStewardTypeName(),
                                                           properties.getStewardPropertyName(),
                                                           properties.getSource(),
                                                           properties.getNotes(),
                                                           properties.getRetentionBasis(),
                                                           properties.getAssociatedGUID(),
                                                           properties.getArchiveAfter(),
                                                           properties.getDeleteAfter(),
                                                           forLineage,
                                                           forDuplicateProcessing,
                                                           properties.getEffectiveFrom(),
                                                           properties.getEffectiveTo(),
                                                           requestBody.getEffectiveTime(),
                                                           methodName);
                    }
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
     * @param userId calling user
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
                                                     String                    userId,
                                                     String                    elementGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String   methodName = "clearRetentionClassification";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getMetadataCorrelationProperties() != null)
                {
                    handler.removeRetentionClassification(userId,
                                                          requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                          requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                          elementGUID,
                                                          elementGUIDParameter,
                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
                                                          methodName);
                }
                else
                {
                    handler.removeRetentionClassification(userId,
                                                          null,
                                                          null,
                                                          elementGUID,
                                                          elementGUIDParameter,
                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          requestBody.getEffectiveTime(),
                                                          methodName);
                }
            }
            else
            {
                handler.removeRetentionClassification(userId,
                                                      null,
                                                      null,
                                                      elementGUID,
                                                      elementGUIDParameter,
                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                      forLineage,
                                                      forDuplicateProcessing,
                                                      null,
                                                      methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getRetentionClassifiedElements(String                      serverName,
                                                               String                      userId,
                                                               int                         startFrom,
                                                               int                         pageSize,
                                                               boolean                     forLineage,
                                                               boolean                     forDuplicateProcessing,
                                                               FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getRetentionClassifiedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof LevelIdentifierQueryProperties properties)
                {
                    response.setElements(handler.getRetentionClassifiedElements(userId,
                                                                                properties.getReturnSpecificLevel(),
                                                                                properties.getLevelIdentifier(),
                                                                                startFrom,
                                                                                pageSize,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                requestBody.getEffectiveTime(),
                                                                                methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getRetentionClassifiedElements(userId,
                                                                                false,
                                                                                0,
                                                                                startFrom,
                                                                                pageSize,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                requestBody.getEffectiveTime(),
                                                                                methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(LevelIdentifierQueryProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElements(handler.getRetentionClassifiedElements(userId,
                                                                            false,
                                                                            0,
                                                                            startFrom,
                                                                            pageSize,
                                                                            forLineage,
                                                                            forDuplicateProcessing,
                                                                            null,
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
     * Add or replace the security tags for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId      calling user
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
                                        String                    userId,
                                        String                    elementGUID,
                                        boolean                   forLineage,
                                        boolean                   forDuplicateProcessing,
                                        ClassificationRequestBody requestBody)
    {
        final String methodName             = "addSecurityTags";
        final String assetGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SecurityTagsProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    handler.addSecurityTags(userId,
                                            elementGUID,
                                            assetGUIDParameterName,
                                            OpenMetadataType.REFERENCEABLE.typeName,
                                            properties.getSecurityLabels(),
                                            properties.getSecurityProperties(),
                                            properties.getAccessGroups(),
                                            forLineage,
                                            forDuplicateProcessing,
                                            requestBody.getEffectiveTime(),
                                            methodName);
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
     * @param userId      calling user
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
    @SuppressWarnings(value = "unused")
    public VoidResponse removeSecurityTags(String                    serverName,
                                           String                    userId,
                                           String                    elementGUID,
                                           boolean                   forLineage,
                                           boolean                   forDuplicateProcessing,
                                           ClassificationRequestBody requestBody)
    {
        final String methodName             = "removeSecurityTags";
        final String assetGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeSecurityTags(userId,
                                           elementGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataType.REFERENCEABLE.typeName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
            }
            else
            {
                handler.removeSecurityTags(userId,
                                           elementGUID,
                                           assetGUIDParameterName,
                                           OpenMetadataType.REFERENCEABLE.typeName,
                                           forLineage,
                                           forDuplicateProcessing,
                                           null,
                                           methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getSecurityTaggedElements(String                      serverName,
                                                          String                      userId,
                                                          int                         startFrom,
                                                          int                         pageSize,
                                                          boolean                     forLineage,
                                                          boolean                     forDuplicateProcessing,
                                                          FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getSecurityTaggedElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                response.setElements(handler.getSecurityTagsClassifiedElements(userId,
                                                                               startFrom,
                                                                               pageSize,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               requestBody.getEffectiveTime(),
                                                                               methodName));
            }
            else
            {
                response.setElements(handler.getSecurityTagsClassifiedElements(userId,
                                                                               startFrom,
                                                                               pageSize,
                                                                               forLineage,
                                                                               forDuplicateProcessing,
                                                                               null,
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
     * Add or replace the ownership classification for an element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                     String                    userId,
                                     String                    elementGUID,
                                     boolean                   forLineage,
                                     boolean                   forDuplicateProcessing,
                                     ClassificationRequestBody requestBody)
    {
        final String   methodName = "addOwnership";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof OwnerProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    handler.addOwner(userId,
                                     elementGUID,
                                     elementGUIDParameter,
                                     OpenMetadataType.REFERENCEABLE.typeName,
                                     properties.getOwner(),
                                     properties.getOwnerTypeName(),
                                     properties.getOwnerPropertyName(),
                                     forLineage,
                                     forDuplicateProcessing,
                                     requestBody.getEffectiveTime(),
                                     methodName);
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
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
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
                                       String                    userId,
                                       String                    elementGUID,
                                       boolean                   forLineage,
                                       boolean                   forDuplicateProcessing,
                                       ClassificationRequestBody requestBody)
    {
        final String   methodName = "clearOwnership";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeOwner(userId,
                                    elementGUID,
                                    elementGUIDParameter,
                                    OpenMetadataType.REFERENCEABLE.typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    requestBody.getEffectiveTime(),
                                    methodName);
            }
            else
            {
                handler.removeOwner(userId,
                                    elementGUID,
                                    elementGUIDParameter,
                                    OpenMetadataType.REFERENCEABLE.typeName,
                                    forLineage,
                                    forDuplicateProcessing,
                                    null,
                                    methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getOwnersElements(String                      serverName,
                                                  String                      userId,
                                                  int                         startFrom,
                                                  int                         pageSize,
                                                  boolean                     forLineage,
                                                  boolean                     forDuplicateProcessing,
                                                  FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getOwnersElements";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FindNameProperties properties)
                {
                    response.setElements(handler.getOwnersElements(userId,
                                                                   properties.getName(),
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
                                                                   methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getOwnersElements(userId,
                                                                   null,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
                                                                   methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(FindNameProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElements(handler.getOwnersElements(userId,
                                                               null,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               null,
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
     * Add or replace the origin classification for an asset.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID element to link it to - its type must inherit from Asset.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse addAssetOrigin(String                    serverName,
                                       String                    userId,
                                       String                    assetGUID,
                                       boolean                   forLineage,
                                       boolean                   forDuplicateProcessing,
                                       ClassificationRequestBody requestBody)
    {
        final String   methodName = "addAssetOrigin";
        final String   elementGUIDParameter = "assetGUID";
        final String   organizationGUIDParameter = "properties.organizationGUID";
        final String   businessCapabilityGUIDParameter = "properties.businessCapabilityGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof AssetOriginProperties properties)
                {
                    DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

                    handler.addAssetOrigin(userId,
                                           assetGUID,
                                           elementGUIDParameter,
                                           properties.getOrganizationGUID(),
                                           organizationGUIDParameter,
                                           properties.getBusinessCapabilityGUID(),
                                           businessCapabilityGUIDParameter,
                                           properties.getOtherOriginValues(),
                                           properties.getEffectiveFrom(),
                                           properties.getEffectiveTo(),
                                           false,
                                           forLineage,
                                           forDuplicateProcessing,
                                           requestBody.getEffectiveTime(),
                                           methodName);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(AssetOriginProperties.class.getName(), methodName);
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
     * Remove the origin classification from an asset.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param assetGUID element where the classification needs to be removed.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for classification request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse clearAssetOrigin(String                    serverName,
                                         String                    userId,
                                         String                    assetGUID,
                                         boolean                   forLineage,
                                         boolean                   forDuplicateProcessing,
                                         ClassificationRequestBody requestBody)
    {
        final String   methodName = "clearAssetOrigin";
        final String   elementGUIDParameter = "assetGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            DataAssetExchangeHandler handler = instanceHandler.getDataAssetExchangeHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeAssetOrigin(userId,
                                          assetGUID,
                                          elementGUIDParameter,
                                          forLineage,
                                          forDuplicateProcessing,
                                          requestBody.getEffectiveTime(),
                                          methodName);
            }
            else
            {
                handler.removeAssetOrigin(userId,
                                          assetGUID,
                                          elementGUIDParameter,
                                          forLineage,
                                          forDuplicateProcessing,
                                          null,
                                          methodName);
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
     * Return information about the assets from a specific origin.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public AssetElementsResponse getAssetsByOrigin(String                      serverName,
                                                   String                      userId,
                                                   int                         startFrom,
                                                   int                         pageSize,
                                                   boolean                     forLineage,
                                                   boolean                     forDuplicateProcessing,
                                                   FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getAssetsByOrigin";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        AssetElementsResponse response = new AssetElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            AssetHandler<AssetElement> handler = instanceHandler.getAssetHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FindAssetOriginProperties properties)
                {
                    response.setElementList(handler.getAssetsFromOrigin(userId,
                                                                        properties.getOrganizationGUID(),
                                                                        properties.getBusinessCapabilityGUID(),
                                                                        properties.getOtherOriginValues(),
                                                                        startFrom,
                                                                        pageSize,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        requestBody.getEffectiveTime(),
                                                                        methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElementList(handler.getAssetsFromOrigin(userId,
                                                                        null,
                                                                        null,
                                                                        null,
                                                                        startFrom,
                                                                        pageSize,
                                                                        forLineage,
                                                                        forDuplicateProcessing,
                                                                        requestBody.getEffectiveTime(),
                                                                        methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(FindAssetOriginProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElementList(handler.getAssetsFromOrigin(userId,
                                                                    null,
                                                                    null,
                                                                    null,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    null,
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
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                                String                    userId,
                                                String                    elementGUID,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing,
                                                ClassificationRequestBody requestBody)
    {
        final String   methodName = "addElementToSubjectArea";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SubjectAreaClassificationProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    if (requestBody.getMetadataCorrelationProperties() != null)
                    {
                        handler.addSubjectAreaClassification(userId,
                                                             requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                             requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                             elementGUID,
                                                             elementGUIDParameter,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             properties.getSubjectAreaName(),
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             properties.getEffectiveFrom(),
                                                             properties.getEffectiveTo(),
                                                             requestBody.getEffectiveTime(),
                                                             methodName);
                    }
                    else
                    {
                        handler.addSubjectAreaClassification(userId,
                                                             null,
                                                             null,
                                                             elementGUID,
                                                             elementGUIDParameter,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             properties.getSubjectAreaName(),
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             properties.getEffectiveFrom(),
                                                             properties.getEffectiveTo(),
                                                             requestBody.getEffectiveTime(),
                                                             methodName);
                    }
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
     * @param userId calling user
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
                                                     String                    userId,
                                                     String                    elementGUID,
                                                     boolean                   forLineage,
                                                     boolean                   forDuplicateProcessing,
                                                     ClassificationRequestBody requestBody)
    {
        final String   methodName = "removeElementFromSubjectArea";
        final String   elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeSubjectAreaClassification(userId,
                                                        null,
                                                        null,
                                                        elementGUID,
                                                        elementGUIDParameter,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        null,
                                                        methodName);
            }
            else if (requestBody.getMetadataCorrelationProperties() == null)
            {
                handler.removeSubjectAreaClassification(userId,
                                                        null,
                                                        null,
                                                        elementGUID,
                                                        elementGUIDParameter,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        requestBody.getEffectiveTime(),
                                                        methodName);
            }
            else
            {
                handler.removeSubjectAreaClassification(userId,
                                                        requestBody.getMetadataCorrelationProperties().getExternalScopeGUID(),
                                                        requestBody.getMetadataCorrelationProperties().getExternalScopeName(),
                                                        elementGUID,
                                                        elementGUIDParameter,
                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        requestBody.getEffectiveTime(),
                                                        methodName);
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
     * @param userId calling user
     * @param startFrom    index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for the request
     *
     * @return void or
     *      InvalidParameterException full path or userId is null or
     *      PropertyServerException problem accessing property server or
     *      UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getMembersOfSubjectArea(String                      serverName,
                                                        String                      userId,
                                                        int                         startFrom,
                                                        int                         pageSize,
                                                        boolean                     forLineage,
                                                        boolean                     forDuplicateProcessing,
                                                        FindByPropertiesRequestBody requestBody)
    {
        final String methodName = "getMembersOfSubjectArea";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<ElementStub> handler = instanceHandler.getElementStubHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof FindNameProperties properties)
                {
                    response.setElements(handler.getSubjectAreaMembers(userId,
                                                                       properties.getName(),
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       requestBody.getEffectiveTime(),
                                                                       methodName));
                }
                else if (requestBody.getProperties() == null)
                {
                    response.setElements(handler.getSubjectAreaMembers(userId,
                                                                       null,
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       requestBody.getEffectiveTime(),
                                                                       methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(FindNameProperties.class.getName(), methodName);
                }
            }
            else
            {
                response.setElements(handler.getSubjectAreaMembers(userId,
                                                                   null,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   null,
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
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                                String                  userId,
                                                String                  elementGUID,
                                                String                  glossaryTermGUID,
                                                boolean                 forLineage,
                                                boolean                 forDuplicateProcessing,
                                                RelationshipRequestBody requestBody)
    {
        final String methodName                     = "setupSemanticAssignment";
        final String elementGUIDParameterName       = "elementGUID";
        final String glossaryTermGUIDParameterName  = "glossaryTermGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof SemanticAssignmentProperties properties)
                {
                    SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

                    int statusOrdinal = 0;

                    if (properties.getStatus() != null)
                    {
                        statusOrdinal = properties.getStatus().getOrdinal();
                    }

                    handler.saveSemanticAssignment(userId,
                                                   requestBody.getExternalSourceGUID(),
                                                   requestBody.getExternalSourceName(),
                                                   elementGUID,
                                                   elementGUIDParameterName,
                                                   glossaryTermGUID,
                                                   glossaryTermGUIDParameterName,
                                                   properties.getDescription(),
                                                   properties.getExpression(),
                                                   statusOrdinal,
                                                   properties.getConfidence(),
                                                   properties.getCreatedBy(),
                                                   properties.getSteward(),
                                                   properties.getSource(),
                                                   properties.getEffectiveFrom(),
                                                   properties.getEffectiveTo(),
                                                   forLineage,
                                                   forDuplicateProcessing,
                                                   requestBody.getEffectiveTime(),
                                                   methodName);
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
     * @param userId calling user
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
                                                String                        userId,
                                                String                        elementGUID,
                                                String                        glossaryTermGUID,
                                                boolean                       forLineage,
                                                boolean                       forDuplicateProcessing,
                                                EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName                     = "clearSemanticAssignment";
        final String elementGUIDParameterName       = "elementGUID";
        final String glossaryTermGUIDParameterName  = "glossaryTermGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeSemanticAssignment(userId,
                                                 null,
                                                 null,
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 glossaryTermGUID,
                                                 glossaryTermGUIDParameterName,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 null,
                                                 methodName);
            }
            else
            {
                handler.removeSemanticAssignment(userId,
                                                 requestBody.getAssetManagerGUID(),
                                                 requestBody.getAssetManagerName(),
                                                 elementGUID,
                                                 elementGUIDParameterName,
                                                 glossaryTermGUID,
                                                 glossaryTermGUIDParameterName,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 requestBody.getEffectiveTime(),
                                                 methodName);
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
     * @param userId calling user
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GlossaryTermElementsResponse getMeanings(String                        serverName,
                                                    String                        userId,
                                                    String                        elementGUID,
                                                    int                           startFrom,
                                                    int                           pageSize,
                                                    boolean                       forLineage,
                                                    boolean                       forDuplicateProcessing,
                                                    EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getMeanings";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GlossaryTermElementsResponse response = new GlossaryTermElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GlossaryExchangeHandler handler = instanceHandler.getGlossaryExchangeHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElementList(handler.getMeanings(userId,
                                                            null,
                                                            null,
                                                            elementGUID,
                                                            startFrom,
                                                            pageSize,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            null,
                                                            methodName));
            }
            else
            {
                response.setElementList(handler.getMeanings(userId,
                                                            requestBody.getAssetManagerGUID(),
                                                            requestBody.getAssetManagerName(),
                                                            elementGUID,
                                                            startFrom,
                                                            pageSize,
                                                            forLineage,
                                                            forDuplicateProcessing,
                                                            requestBody.getEffectiveTime(),
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
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getSemanticAssignees(String                        serverName,
                                                        String                        userId,
                                                        String                        glossaryTermGUID,
                                                        int                           startFrom,
                                                        int                           pageSize,
                                                        boolean                       forLineage,
                                                        boolean                       forDuplicateProcessing,
                                                        EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSemanticAssignees";
        final String elementGUIDParameterName = "glossaryTermGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getSemanticAssignments(userId,
                                                                    glossaryTermGUID,
                                                                    elementGUIDParameterName,
                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    null,
                                                                    methodName));
            }
            else
            {
                response.setElements(handler.getSemanticAssignments(userId,
                                                                    glossaryTermGUID,
                                                                    elementGUIDParameterName,
                                                                    OpenMetadataType.REFERENCEABLE.typeName,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    requestBody.getEffectiveTime(),
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
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
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
                                                         String                  userId,
                                                         String                  definitionGUID,
                                                         String                  elementGUID,
                                                         boolean                 forLineage,
                                                         boolean                 forDuplicateProcessing,
                                                         RelationshipRequestBody requestBody)
    {
        final String methodName                   = "addGovernanceDefinitionToElement";
        final String elementGUIDParameterName     = "elementGUID";
        final String definitionGUIDParameterName  = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody != null)
            {

                if (requestBody.getProperties() != null)
                {

                    handler.addGovernedBy(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          definitionGUID,
                                          definitionGUIDParameterName,
                                          elementGUID,
                                          elementGUIDParameterName,
                                          requestBody.getProperties().getEffectiveFrom(),
                                          requestBody.getProperties().getEffectiveTo(),
                                          forLineage,
                                          forDuplicateProcessing,
                                          requestBody.getEffectiveTime(),
                                          methodName);
                }
                else
                {
                    handler.addGovernedBy(userId,
                                          requestBody.getExternalSourceGUID(),
                                          requestBody.getExternalSourceName(),
                                          definitionGUID,
                                          definitionGUIDParameterName,
                                          elementGUID,
                                          elementGUIDParameterName,
                                          null,
                                          null,
                                          forLineage,
                                          forDuplicateProcessing,
                                          requestBody.getEffectiveTime(),
                                          methodName);
                }
            }
            else
            {
                handler.addGovernedBy(userId,
                                      null,
                                      null,
                                      definitionGUID,
                                      definitionGUIDParameterName,
                                      elementGUID,
                                      elementGUIDParameterName,
                                      null,
                                      null,
                                      forLineage,
                                      forDuplicateProcessing,
                                      null,
                                      methodName);
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
     * @param userId calling user
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
                                                              String                        userId,
                                                              String                        definitionGUID,
                                                              String                        elementGUID,
                                                              boolean                       forLineage,
                                                              boolean                       forDuplicateProcessing,
                                                              EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName                   = "removeGovernanceDefinitionFromElement";
        final String elementGUIDParameterName     = "elementGUID";
        final String definitionGUIDParameterName  = "definitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            SoftwareCapabilityHandler<SoftwareCapabilityElement> handler = instanceHandler.getAssetManagerHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                handler.removeGovernedBy(userId,
                                         null,
                                         null,
                                         elementGUID,
                                         elementGUIDParameterName,
                                         definitionGUID,
                                         definitionGUIDParameterName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         null,
                                         methodName);
            }
            else
            {
                handler.removeGovernedBy(userId,
                                         requestBody.getAssetManagerGUID(),
                                         requestBody.getAssetManagerName(),
                                         elementGUID,
                                         elementGUIDParameterName,
                                         definitionGUID,
                                         definitionGUIDParameterName,
                                         forLineage,
                                         forDuplicateProcessing,
                                         requestBody.getEffectiveTime(),
                                         methodName);
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
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the governance definition that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getGovernedElements(String                        serverName,
                                                       String                        userId,
                                                       String                        governanceDefinitionGUID,
                                                       int                           startFrom,
                                                       int                           pageSize,
                                                       boolean                       forLineage,
                                                       boolean                       forDuplicateProcessing,
                                                       EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGovernedElements";
        final String elementGUIDParameterName = "governanceDefinitionGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getGovernedElements(userId,
                                                                 governanceDefinitionGUID,
                                                                 elementGUIDParameterName,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 null,
                                                                 methodName));
            }
            else
            {
                response.setElements(handler.getGovernedElements(userId,
                                                                 governanceDefinitionGUID,
                                                                 elementGUIDParameterName,
                                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 requestBody.getEffectiveTime(),
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
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public GovernanceDefinitionsResponse getGovernedByDefinitions(String                        serverName,
                                                                  String                        userId,
                                                                  String                        elementGUID,
                                                                  int                           startFrom,
                                                                  int                           pageSize,
                                                                  boolean                       forLineage,
                                                                  boolean                       forDuplicateProcessing,
                                                                  EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getGovernedByDefinitions";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GovernanceDefinitionsResponse response = new GovernanceDefinitionsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            GovernanceDefinitionHandler<GovernanceDefinitionElement> handler = instanceHandler.getGovernanceDefinitionHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getGoverningDefinitions(userId,
                                                                     elementGUID,
                                                                     elementGUIDParameterName,
                                                                     OpenMetadataType.REFERENCEABLE.typeName,
                                                                     OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     null,
                                                                     methodName));
            }
            else
            {
                response.setElements(handler.getGoverningDefinitions(userId,
                                                                     elementGUID,
                                                                     elementGUIDParameterName,
                                                                     OpenMetadataType.REFERENCEABLE.typeName,
                                                                     OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing,
                                                                     requestBody.getEffectiveTime(),
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
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the governance definition that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getSourceElements(String                        serverName,
                                                     String                        userId,
                                                     String                        elementGUID,
                                                     int                           startFrom,
                                                     int                           pageSize,
                                                     boolean                       forLineage,
                                                     boolean                       forDuplicateProcessing,
                                                     EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getSourceElements";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getSourceElements(userId,
                                                               elementGUID,
                                                               elementGUIDParameterName,
                                                               OpenMetadataType.REFERENCEABLE.typeName,
                                                               OpenMetadataType.REFERENCEABLE.typeName,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               null,
                                                               methodName));
            }
            else
            {
                response.setElements(handler.getSourceElements(userId,
                                                               elementGUID,
                                                               elementGUIDParameterName,
                                                               OpenMetadataType.REFERENCEABLE.typeName,
                                                               OpenMetadataType.REFERENCEABLE.typeName,
                                                               startFrom,
                                                               pageSize,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               requestBody.getEffectiveTime(),
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
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param serverName  name of the server instance to connect to
     * @param userId calling user
     * @param elementGUID unique identifier of the element that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param requestBody properties for relationship request
     *
     * @return void or
     * InvalidParameterException full path or userId is null or
     * PropertyServerException problem accessing property server or
     * UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getElementsSourceFrom(String                        serverName,
                                                         String                        userId,
                                                         String                        elementGUID,
                                                         int                           startFrom,
                                                         int                           pageSize,
                                                         boolean                       forLineage,
                                                         boolean                       forDuplicateProcessing,
                                                         EffectiveTimeQueryRequestBody requestBody)
    {
        final String methodName = "getElementsSourceFrom";
        final String elementGUIDParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            if (requestBody == null)
            {
                response.setElements(handler.getElementsSourceFrom(userId,
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   null,
                                                                   methodName));
            }
            else
            {
                response.setElements(handler.getElementsSourceFrom(userId,
                                                                   elementGUID,
                                                                   elementGUIDParameterName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   OpenMetadataType.REFERENCEABLE.typeName,
                                                                   startFrom,
                                                                   pageSize,
                                                                   forLineage,
                                                                   forDuplicateProcessing,
                                                                   requestBody.getEffectiveTime(),
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
}
