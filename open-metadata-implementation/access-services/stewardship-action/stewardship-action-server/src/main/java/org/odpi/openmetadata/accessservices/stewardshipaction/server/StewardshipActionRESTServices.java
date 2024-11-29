/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server;



import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.generichandlers.ElementStubConverter;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DuplicateElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.DuplicateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The StewardshipActionRESTServices provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class StewardshipActionRESTServices
{
    private static final StewardshipActionInstanceHandler instanceHandler = new StewardshipActionInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(StewardshipActionRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public StewardshipActionRESTServices()
    {
    }


    /**
     * Return the connection object for the Stewardship Action OMAS's out topic.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param callerId unique identifier of the caller
     *
     * @return connection object for the out topic or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem retrieving the discovery engine definition.
     */
    public OCFConnectionResponse getOutTopicConnection(String serverName,
                                                       String userId,
                                                       String callerId)
    {
        final String methodName = "getOutTopicConnection";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        OCFConnectionResponse response = new OCFConnectionResponse();
        AuditLog              auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setConnection(instanceHandler.getOutTopicConnection(userId, serverName, methodName, callerId));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /*
     * ==============================================
     * DuplicateManagementInterface
     * ==============================================
     */

    /**
     * Create a simple relationship between two elements.  These elements must be of the same type.  If the relationship already exists,
     * the properties are updated.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody parameters for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse linkElementsAsDuplicates(String                serverName,
                                                 String                userId,
                                                 String                element1GUID,
                                                 String                element2GUID,
                                                 DuplicatesRequestBody requestBody)
    {
        final String methodName = "linkElementsAsDuplicates";

        final String element1GUIDParameterName = "element1GUID";
        final String element2GUIDParameterName = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.linkElementsAsPeerDuplicates(userId,
                                                     element1GUID,
                                                     element1GUIDParameterName,
                                                     element2GUID,
                                                     element2GUIDParameterName,
                                                     false,
                                                     requestBody.getStatusIdentifier(),
                                                     requestBody.getSteward(),
                                                     requestBody.getStewardTypeName(),
                                                     requestBody.getStewardPropertyName(),
                                                     requestBody.getSource(),
                                                     requestBody.getNotes(),
                                                     instanceHandler.getSupportedZones(userId, serverName, methodName),
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
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")

    public VoidResponse unlinkElementsAsDuplicates(String          serverName,
                                                   String          userId,
                                                   String          element1GUID,
                                                   String          element2GUID,
                                                   NullRequestBody requestBody)
    {
        final String methodName = "unlinkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.unlinkElementsAsPeerDuplicates(userId,
                                                   element1GUID,
                                                   element1GUIDParameter,
                                                   element2GUID,
                                                   element2GUIDParameter,
                                                   instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                   methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Classify an element as a known duplicate.  This will mean that it is included in duplicate processing during metadata retrieval requests.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")

    public VoidResponse markElementAsKnownDuplicate(String          serverName,
                                                    String          userId,
                                                    String          elementGUID,
                                                    NullRequestBody requestBody)
    {
        final String methodName = "markElementAsKnownDuplicate";
        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.setClassificationInRepository(userId,
                                                  null,
                                                  null,
                                                  elementGUID,
                                                  elementGUIDParameter,
                                                  OpenMetadataType.REFERENCEABLE.typeName,
                                                  OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                                  OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                  null,
                                                  false,
                                                  true,
                                                  true,
                                                  instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                  null,
                                                  methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the classification that identifies this element as a known duplicate.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")

    public VoidResponse unmarkElementAsKnownDuplicate(String          serverName,
                                                      String          userId,
                                                      String          elementGUID,
                                                      NullRequestBody requestBody)
    {
        final String methodName = "unmarkElementAsKnownDuplicate";

        final String elementGUIDParameter = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.removeClassificationFromRepository(userId,
                                                       null,
                                                       null,
                                                       elementGUID,
                                                       elementGUIDParameter,
                                                       OpenMetadataType.REFERENCEABLE.typeName,
                                                       OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID,
                                                       OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME,
                                                       true,
                                                       true,
                                                       instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                       null,
                                                       methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }

    /**
     * List the elements that are linked as peer duplicates to the requested element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of linked duplicates or
     * InvalidParameterException one of the parameters is null or invalid
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public DuplicatesResponse getPeerDuplicates(String serverName,
                                                String userId,
                                                String elementGUID,
                                                int    startFrom,
                                                int    pageSize)
    {
        final String methodName        = "getPeerDuplicates";
        final String guidParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        DuplicatesResponse response = new DuplicatesResponse();
        AuditLog           auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            List<Relationship> relationships = handler.getAttachmentLinks(userId,
                                                                          elementGUID,
                                                                          guidParameterName,
                                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                                          OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_GUID,
                                                                          OpenMetadataType.PEER_DUPLICATE_LINK_TYPE_NAME,
                                                                          null,
                                                                          OpenMetadataType.REFERENCEABLE.typeName,
                                                                          0,
                                                                          null,
                                                                          null,
                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                          null,
                                                                          true,
                                                                          true,
                                                                          instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                          startFrom,
                                                                          pageSize,
                                                                          null,
                                                                          methodName);

            List<DuplicateElement> results = new ArrayList<>();

            if (relationships != null)
            {
                ElementStubConverter<ElementStub> converter = instanceHandler.getElementStubConverter(userId, serverName, methodName);

                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        DuplicateElement duplicateElement = new DuplicateElement();

                        duplicateElement.setElementHeader(converter.getMetadataElementHeader(ElementStub.class,
                                                                                             relationship,
                                                                                             null,
                                                                                             methodName));

                        if (relationship.getProperties() != null)
                        {
                            DuplicateProperties duplicateProperties = new DuplicateProperties();

                            OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(userId, serverName, methodName);

                            duplicateProperties.setStatusIdentifier(repositoryHelper.getIntProperty(instanceHandler.getServiceName(),
                                                                                                    OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                                                    relationship.getProperties(),
                                                                                                    methodName));
                            duplicateProperties.setSteward(repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                                                              OpenMetadataProperty.STEWARD.name,
                                                                                              relationship.getProperties(),
                                                                                              methodName));
                            duplicateProperties.setStewardTypeName(repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                                                                      OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                                                      relationship.getProperties(),
                                                                                                      methodName));
                            duplicateProperties.setStewardPropertyName(repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                                                                          OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                                                          relationship.getProperties(),
                                                                                                          methodName));
                            duplicateProperties.setSource(repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                                                             OpenMetadataProperty.SOURCE.name,
                                                                                             relationship.getProperties(),
                                                                                             methodName));
                            duplicateProperties.setNotes(repositoryHelper.getStringProperty(instanceHandler.getServiceName(),
                                                                                            OpenMetadataProperty.NOTES.name,
                                                                                            relationship.getProperties(),
                                                                                            methodName));

                            EntityProxy peerProxy = relationship.getEntityOneProxy();

                            if (elementGUID.equals(peerProxy.getGUID()))
                            {
                                peerProxy = relationship.getEntityTwoProxy();
                            }

                            duplicateProperties.setDuplicateGUID(peerProxy.getGUID());

                            duplicateElement.setDuplicateProperties(duplicateProperties);
                        }

                        results.add(duplicateElement);
                    }
                }
            }

            if (! results.isEmpty())
            {
                response.setElements(results);
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
     * Mark an element as a consolidated duplicate (or update the properties if it is already marked as such).
     * This method assumes that a standard create method has been used to create the element first using the values from contributing elements.
     * It is just adding the ConsolidatedDuplicate classification to the element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of the element that contains the consolidated information from a collection of elements
     *                                  that are all duplicates of one another.
     * @param requestBody properties for the relationship
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public VoidResponse markAsConsolidatedDuplicate(String                serverName,
                                                    String                userId,
                                                    String                consolidatedDuplicateGUID,
                                                    DuplicatesRequestBody requestBody)
    {
        final String methodName           = "markAsConsolidatedDuplicate";
        final String elementGUIDParameter = "consolidatedDuplicateGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(userId, serverName, methodName);

                InstanceProperties properties = repositoryHelper.addIntPropertyToInstance(instanceHandler.getServiceName(),
                                                                                          null,
                                                                                          OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                                          requestBody.getStatusIdentifier(),
                                                                                          methodName);

                properties = repositoryHelper.addStringPropertyToInstance(instanceHandler.getServiceName(),
                                                                          properties,
                                                                          OpenMetadataProperty.STEWARD.name,
                                                                          requestBody.getSteward(),
                                                                          methodName);

                properties = repositoryHelper.addStringPropertyToInstance(instanceHandler.getServiceName(),
                                                                          properties,
                                                                          OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                          requestBody.getStewardTypeName(),
                                                                          methodName);

                properties = repositoryHelper.addStringPropertyToInstance(instanceHandler.getServiceName(),
                                                                          properties,
                                                                          OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                          requestBody.getStewardPropertyName(),
                                                                          methodName);

                properties = repositoryHelper.addStringPropertyToInstance(instanceHandler.getServiceName(),
                                                                          properties,
                                                                          OpenMetadataProperty.SOURCE.name,
                                                                          requestBody.getSource(),
                                                                          methodName);

                properties = repositoryHelper.addStringPropertyToInstance(instanceHandler.getServiceName(),
                                                                          properties,
                                                                          OpenMetadataProperty.NOTES.name,
                                                                          requestBody.getNotes(),
                                                                          methodName);

                handler.setClassificationInRepository(userId,
                                                      null,
                                                      null,
                                                      consolidatedDuplicateGUID,
                                                      elementGUIDParameter,
                                                      OpenMetadataType.REFERENCEABLE.typeName,
                                                      OpenMetadataType.CONSOLIDATED_DUPLICATE_TYPE_GUID,
                                                      OpenMetadataType.CONSOLIDATED_DUPLICATE_TYPE_NAME,
                                                      properties,
                                                      false,
                                                      true,
                                                      true,
                                                      instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                      null,
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
     * Create a ConsolidatedDuplicateLink relationship between the consolidated duplicate element and one of its contributing element.
     *
     * @param serverName name of the service to route the request to
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")

    public VoidResponse linkElementToConsolidatedDuplicate(String          serverName,
                                                           String          userId,
                                                           String          consolidatedDuplicateGUID,
                                                           String          contributingElementGUID,
                                                           NullRequestBody requestBody)
    {
        final String methodName = "linkElementToConsolidatedDuplicate";

        final String element1GUIDParameter = "consolidatedDuplicateGUID";
        final String element2GUIDParameter = "contributingElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.linkElementToElement(userId,
                                         null,
                                         null,
                                         consolidatedDuplicateGUID,
                                         element1GUIDParameter,
                                         OpenMetadataType.REFERENCEABLE.typeName,
                                         contributingElementGUID,
                                         element2GUIDParameter,
                                         OpenMetadataType.REFERENCEABLE.typeName,
                                         true,
                                         true,
                                         instanceHandler.getSupportedZones(userId, serverName, methodName),
                                         OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_GUID,
                                         OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_NAME,
                                         null,
                                         null,
                                         (Date)null,
                                         new Date(),
                                         methodName);

        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")

    public VoidResponse unlinkElementFromConsolidatedDuplicate(String          serverName,
                                                               String          userId,
                                                               String          consolidatedDuplicateGUID,
                                                               String          contributingElementGUID,
                                                               NullRequestBody requestBody)
    {
        final String methodName = "unlinkElementFromConsolidatedDuplicate";

        final String element1GUIDParameter = "consolidatedDuplicateGUID";
        final String element2GUIDParameter = "contributingElementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.unlinkElementFromElement(userId,
                                             false,
                                             null,
                                             null,
                                             consolidatedDuplicateGUID,
                                             element1GUIDParameter,
                                             OpenMetadataType.REFERENCEABLE.typeName,
                                             contributingElementGUID,
                                             element2GUIDParameter,
                                             OpenMetadataType.REFERENCEABLE.typeGUID,
                                             OpenMetadataType.REFERENCEABLE.typeName,
                                             true,
                                             true,
                                             instanceHandler.getSupportedZones(userId, serverName, methodName),
                                             OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_GUID,
                                             OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_NAME,
                                             null,
                                             methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * List the elements that are contributing to a consolidating duplicate element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of contributing duplicates or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public ElementStubsResponse getContributingDuplicates(String serverName,
                                                          String userId,
                                                          String consolidatedDuplicateGUID,
                                                          int    startFrom,
                                                          int    pageSize)
    {
        final String methodName        = "getContributingDuplicates";
        final String guidParameterName = "consolidatedDuplicateGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubsResponse response = new ElementStubsResponse();
        AuditLog             auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            List<ElementStub> elementStubs = handler.getAttachedElements(userId,
                                                                         null,
                                                                         null,
                                                                         consolidatedDuplicateGUID,
                                                                         guidParameterName,
                                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                                         OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_GUID,
                                                                         OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_NAME,
                                                                         OpenMetadataType.REFERENCEABLE.typeName,
                                                                         null,
                                                                         null,
                                                                         1,
                                                                         null,
                                                                         null,
                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                         null,
                                                                         true,
                                                                         true,
                                                                         instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                         startFrom,
                                                                         pageSize,
                                                                         null,
                                                                         methodName);

            response.setElements(elementStubs);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return details of the consolidated duplicate for a requested element.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param elementGUID element to query
     *
     * @return header of consolidated duplicated or null if none or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    public ElementStubResponse getConsolidatedDuplicate(String serverName,
                                                        String userId,
                                                        String elementGUID)
    {
        final String methodName        = "getConsolidatedDuplicate";
        final String guidParameterName = "elementGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ElementStubResponse response = new ElementStubResponse();
        AuditLog            auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            Relationship relationship = handler.getUniqueAttachmentLink(userId,
                                                                        elementGUID,
                                                                        guidParameterName,
                                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                                        OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_GUID,
                                                                        OpenMetadataType.CONSOLIDATED_DUPLICATE_LINK_TYPE_NAME,
                                                                        null,
                                                                        OpenMetadataType.REFERENCEABLE.typeName,
                                                                        2,
                                                                        null,
                                                                        null,
                                                                        SequencingOrder.CREATION_DATE_RECENT,
                                                                        null,
                                                                        true,
                                                                        true,
                                                                        instanceHandler.getSupportedZones(userId, serverName, methodName),
                                                                        null,
                                                                        methodName);
            if (relationship != null)
            {
                ElementStubConverter<ElementStub> converter = instanceHandler.getElementStubConverter(userId, serverName, methodName);

                ElementStub elementStub = converter.getElementStub(ElementStub.class, relationship.getEntityTwoProxy(), methodName);

                response.setElement(elementStub);
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
     * Remove the consolidated duplicate element and the links to the elements that contributed to its values.
     *
     * @param serverName name of the service to route the request to.
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of element to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @SuppressWarnings(value = "unused")

    public VoidResponse removeConsolidatedDuplicate(String          serverName,
                                                    String          userId,
                                                    String          consolidatedDuplicateGUID,
                                                    NullRequestBody requestBody)
    {
        final String methodName = "removeConsolidatedDuplicate";

        final String element1GUIDParameter = "consolidatedDuplicateGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            ReferenceableHandler<ElementStub> handler = instanceHandler.getReferenceableHandler(userId, serverName, methodName);

            handler.deleteBeanInRepository(userId,
                                           null,
                                           null,
                                           consolidatedDuplicateGUID,
                                           element1GUIDParameter,
                                           OpenMetadataType.REFERENCEABLE.typeGUID,
                                           OpenMetadataType.REFERENCEABLE.typeName,
                                           null,
                                           null,
                                           true,
                                           true,
                                           instanceHandler.getSupportedZones(userId, serverName, methodName),
                                           null,
                                           methodName);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}