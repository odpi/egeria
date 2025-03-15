/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;

import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.generichandlers.ExternalReferenceHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ExternalReferenceElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences.ExternalReferenceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.generichandlers.ReferenceableHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * ExternalReferenceRESTServices is the server-side for managing external references and their links to all types of governance definitions.
 */
public class ExternalReferenceRESTServices
{
    private static final GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ExternalReferenceRESTServices.class),
                                                                            instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public ExternalReferenceRESTServices()
    {
    }


    /**
     * Create a definition of an external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param requestBody properties for an external reference plus optional element to link the external reference to that will act as an anchor
     *                   - that is, this external reference will be deleted when the element is deleted (once the external reference is linked to the anchor).
     *
     * @return unique identifier of the external reference or
     *  InvalidParameterException qualifiedName or userId is null; qualifiedName is not unique
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public GUIDResponse createExternalReference(String                   serverName,
                                                String                   userId,
                                                ReferenceableRequestBody requestBody)
    {
        final String methodName = "createExternalReference";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ExternalReferenceProperties properties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

                    response.setGUID(handler.createExternalReference(userId,
                                                                     requestBody.getExternalSourceGUID(),
                                                                     requestBody.getExternalSourceName(),
                                                                     requestBody.getAnchorGUID(),
                                                                     properties.getQualifiedName(),
                                                                     properties.getDisplayName(),
                                                                     properties.getResourceDescription(),
                                                                     properties.getURI(),
                                                                     properties.getVersion(),
                                                                     properties.getOrganization(),
                                                                     properties.getAdditionalProperties(),
                                                                     properties.getTypeName(),
                                                                     properties.getExtendedProperties(),
                                                                     properties.getEffectiveFrom(),
                                                                     properties.getEffectiveTo(),
                                                                     new Date(),
                                                                     methodName));
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceProperties.class.getName(), methodName);
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
     * Update the definition of an external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param isMergeUpdate are unspecified properties unchanged (true) or replaced with null?
     * @param requestBody properties to change
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse updateExternalReference(String                      serverName,
                                                String                      userId,
                                                String                      externalReferenceGUID,
                                                boolean                     isMergeUpdate,
                                                ReferenceableRequestBody    requestBody)
    {
        final String methodName = "updateExternalReference";
        final String guidParameterName = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ExternalReferenceProperties properties)
                {
                    auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                    ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

                    handler.updateExternalReference(userId,
                                                    requestBody.getExternalSourceGUID(),
                                                    requestBody.getExternalSourceName(),
                                                    externalReferenceGUID,
                                                    guidParameterName,
                                                    properties.getQualifiedName(),
                                                    properties.getDisplayName(),
                                                    properties.getResourceDescription(),
                                                    properties.getURI(),
                                                    properties.getVersion(),
                                                    properties.getOrganization(),
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
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceProperties.class.getName(), methodName);
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
     * Remove the definition of an external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier of external reference
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse deleteExternalReference(String                    serverName,
                                                String                    userId,
                                                String                    externalReferenceGUID,
                                                ExternalSourceRequestBody requestBody)
    {
        final String methodName = "deleteExternalReference";
        final String guidParameterName = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.removeExternalReference(userId,
                                                requestBody.getExternalSourceGUID(),
                                                requestBody.getExternalSourceName(),
                                                externalReferenceGUID,
                                                guidParameterName,
                                                false,
                                                false,
                                                new Date(),
                                                methodName);
            }
            else
            {
                handler.removeExternalReference(userId,
                                                null,
                                                null,
                                                externalReferenceGUID,
                                                guidParameterName,
                                                false,
                                                false,
                                                new Date(),
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
     * Link an external reference to an object.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID unique identifier (guid) of the external reference details.
     * @param requestBody description for the reference from the perspective of the object that the reference is being attached to.
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse linkExternalReferenceToElement(String                  serverName,
                                                       String                  userId,
                                                       String                  attachedToGUID,
                                                       String                  externalReferenceGUID,
                                                       RelationshipRequestBody requestBody)
    {
        final String methodName = "linkExternalReferenceToElement";

        final String elementGUIDParameterName = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof ExternalReferenceLinkProperties properties)
                {
                    OMRSRepositoryHelper repositoryHelper = instanceHandler.getRepositoryHelper(userId, serverName, methodName);
                    String serviceName = instanceHandler.getServiceName();

                    InstanceProperties relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                                             null,
                                                                                                             OpenMetadataProperty.REFERENCE_ID.name,
                                                                                                             properties.getLinkId(),
                                                                                                             methodName);

                    relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          relationshipProperties,
                                                                                          OpenMetadataProperty.DESCRIPTION.name,
                                                                                          properties.getLinkDescription(),
                                                                                          methodName);

                    relationshipProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                          relationshipProperties,
                                                                                          OpenMetadataProperty.PAGES.name,
                                                                                          properties.getPages(),
                                                                                          methodName);

                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 attachedToGUID,
                                                 elementGUIDParameterName,
                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                 externalReferenceGUID,
                                                 externalReferenceGUIDParameterName,
                                                 OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                 relationshipProperties,
                                                 properties.getEffectiveFrom(),
                                                 properties.getEffectiveTo(),
                                                 new Date(),
                                                 methodName);
                }
                else if (requestBody.getProperties() == null)
                {
                    handler.linkElementToElement(userId,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 attachedToGUID,
                                                 elementGUIDParameterName,
                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                 externalReferenceGUID,
                                                 externalReferenceGUIDParameterName,
                                                 OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                 null,
                                                 null,
                                                 (Date)null,
                                                 new Date(),
                                                 methodName);
                }
            }
            else
            {
                restExceptionHandler.handleInvalidPropertiesObject(ExternalReferenceLinkProperties.class.getName(), methodName);
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
     * Remove the link between an external reference and an element.  If the element is its anchor, the external reference is removed.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external references.
     * @param externalReferenceGUID identifier of the external reference.
     * @param requestBody external source request body
     *
     * @return void or
     *  InvalidParameterException guid, qualifiedName or userId is null; qualifiedName is not unique; guid is not known
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public VoidResponse unlinkExternalReferenceFromElement(String                  serverName,
                                                           String                  userId,
                                                           String                  attachedToGUID,
                                                           String                  externalReferenceGUID,
                                                           RelationshipRequestBody requestBody)
    {
        final String methodName = "unlinkExternalReferenceToElement";
        final String elementGUIDParameterName = "attachedToGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

            if (requestBody != null)
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 requestBody.getExternalSourceGUID(),
                                                 requestBody.getExternalSourceName(),
                                                 attachedToGUID,
                                                 elementGUIDParameterName,
                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                 externalReferenceGUID,
                                                 externalReferenceGUIDParameterName,
                                                 OpenMetadataType.EXTERNAL_REFERENCE.typeGUID,
                                                 OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                 new Date(),
                                                 methodName);
            }
            else
            {
                handler.unlinkElementFromElement(userId,
                                                 false,
                                                 null,
                                                 null,
                                                 attachedToGUID,
                                                 elementGUIDParameterName,
                                                 OpenMetadataType.REFERENCEABLE.typeName,
                                                 externalReferenceGUID,
                                                 externalReferenceGUIDParameterName,
                                                 OpenMetadataType.EXTERNAL_REFERENCE.typeGUID,
                                                 OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                 false,
                                                 false,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                                 OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                 new Date(),
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
     * Return information about a specific external reference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the external reference
     *
     * @return properties of the external reference or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public ExternalReferenceResponse getExternalReferenceByGUID(String serverName,
                                                                String userId,
                                                                String externalReferenceGUID)

    {
        final String methodName = "getExternalReferenceByGUID";
        final String externalReferenceGUIDParameterName = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferenceResponse response = new ExternalReferenceResponse();
        AuditLog                    auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

            response.setElement(handler.getBeanFromRepository(userId,
                                                              externalReferenceGUID,
                                                              externalReferenceGUIDParameterName,
                                                              OpenMetadataType.EXTERNAL_REFERENCE.typeName,
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
     * Retrieve the list of external references for this resourceId.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param requestBody unique reference id assigned by the resource owner (supports wildcards). This is the qualified name of the entity
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public ExternalReferencesResponse findExternalReferencesById(String                  serverName,
                                                                 String                  userId,
                                                                 int                     startFrom,
                                                                 int                     pageSize,
                                                                 SearchStringRequestBody requestBody)
    {
        final String methodName = "findExternalReferencesById";
        final String resourceIdParameterName = "resourceId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferencesResponse response = new ExternalReferencesResponse();
        AuditLog                   auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

                response.setElements(handler.findBeans(userId,
                                                       requestBody.getSearchString(),
                                                       resourceIdParameterName,
                                                       OpenMetadataType.EXTERNAL_REFERENCE.typeGUID,
                                                       OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                       startFrom,
                                                       pageSize,
                                                       null,
                                                       null,
                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                       null,
                                                       false,
                                                       false,
                                                       new Date(),
                                                       methodName));
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
     * Retrieve the list of external references for this URL.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param requestBody URL of the external resource.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public ExternalReferencesResponse getExternalReferencesByURL(String          serverName,
                                                                 String          userId,
                                                                 int             startFrom,
                                                                 int             pageSize,
                                                                 NameRequestBody requestBody)
    {
        final String methodName = "getExternalReferencesByURL";
        final String urlParameterName = "resourceId";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferencesResponse response = new ExternalReferencesResponse();
        AuditLog                   auditLog = null;

        try
        {
            if (requestBody != null)
            {
                auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
                ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

                response.setElements(handler.getExternalReferencesByURL(userId,
                                                                        requestBody.getName(),
                                                                        urlParameterName,
                                                                        startFrom,
                                                                        pageSize,
                                                                        false,
                                                                        false,
                                                                        new Date(),
                                                                        methodName));
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
     * Retrieve the list of external references attached to the supplied object.
     *
     * @param serverName name of the server instance to connect to
     * @param userId the name of the calling user.
     * @param attachedToGUID object linked to external reference.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return links to addition information or
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public ExternalReferencesResponse retrieveAttachedExternalReferences(String serverName,
                                                                         String userId,
                                                                         String attachedToGUID,
                                                                         int    startFrom,
                                                                         int    pageSize)
    {
        final String methodName = "retrieveAttachedExternalReferences";
        final String guidParameterName = "attachedToGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        ExternalReferencesResponse response = new ExternalReferencesResponse();
        AuditLog                   auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ExternalReferenceHandler<ExternalReferenceElement> handler = instanceHandler.getExternalReferencesHandler(userId, serverName, methodName);

            response.setElements(handler.getAttachedElements(userId,
                                                             attachedToGUID,
                                                             guidParameterName,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                                             OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                             OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                             null,
                                                             null,
                                                             2,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             false,
                                                             false,
                                                             startFrom,
                                                             pageSize,
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
     * Return information about the elements linked to a externalReference.
     *
     * @param serverName name of the server instance to connect to
     * @param userId calling user
     * @param externalReferenceGUID unique identifier for the externalReference
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return properties of the external reference
     *  InvalidParameterException externalReferenceGUID or userId is null
     *  PropertyServerException problem accessing property server
     *  UserNotAuthorizedException security access problem
     */
    public RelatedElementsResponse getElementsForExternalReference(String serverName,
                                                                   String userId,
                                                                   String externalReferenceGUID,
                                                                   int    startFrom,
                                                                   int    pageSize)
    {
        final String methodName = "getElementsForExternalReference";
        final String guidParameter = "externalReferenceGUID";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, userId, methodName);

        RelatedElementsResponse response = new RelatedElementsResponse();
        AuditLog                auditLog = null;

        try
        {
            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            ReferenceableHandler<RelatedElement> handler = instanceHandler.getRelatedElementHandler(userId, serverName, methodName);

            response.setElements(handler.getAttachedElements(userId,
                                                             externalReferenceGUID,
                                                             guidParameter,
                                                             OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                             OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeGUID,
                                                             OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                             OpenMetadataType.REFERENCEABLE.typeName,
                                                             null,
                                                             null,
                                                             1,
                                                             null,
                                                             null,
                                                             SequencingOrder.CREATION_DATE_RECENT,
                                                             null,
                                                             false,
                                                             false,
                                                             startFrom,
                                                             pageSize,
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
}
