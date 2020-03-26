/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.GlossarySummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.LinesResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.server.services.SubjectAreaGlossaryRESTServices;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * SubjectAreaProjectHandler manages Project objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves entities and relationships through the OMRSRepositoryConnector.
 */
public abstract class SubjectAreaHandler {
    private static final Class clazz = SubjectAreaHandler.class;
    private static final String className = clazz.getName();
    private static final Logger log = LoggerFactory.getLogger(clazz);

    protected String serviceName;
    protected String serverName;
    protected RepositoryHandler repositoryHandler;
    protected OMRSRepositoryHelper repositoryHelper;
    protected InvalidParameterHandler invalidParameterHandler;
    protected OMRSAPIHelper oMRSAPIHelper;

    public SubjectAreaHandler(){}

    /**
     * Construct the Subject Area Project Handler
     * needed to operate within a single server instance.
     *
     * @param serviceName             name of the consuming service
     * @param serverName              name of this server instance
     * @param invalidParameterHandler handler for invalid parameters
     * @param repositoryHelper        helper used by the converters
     * @param repositoryHandler       handler for calling the repository services
     * @param errorHandler            handler for repository service errors
     */
    public SubjectAreaHandler(String serviceName,
                              String serverName,
                              InvalidParameterHandler invalidParameterHandler,
                              OMRSRepositoryHelper repositoryHelper,
                              RepositoryHandler repositoryHandler,
                              OMRSAPIHelper oMRSAPIHelper,
                              RepositoryErrorHandler errorHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        // this is set as a mock object for junits.
        if (oMRSAPIHelper ==null)
        {
           this.oMRSAPIHelper = new OMRSAPIHelper(serviceName);
        } else {
           this.oMRSAPIHelper = oMRSAPIHelper;
        }
        this.oMRSAPIHelper.setOmrsRepositoryHelper(repositoryHelper);
        this.oMRSAPIHelper.setOMRSMetadataCollection(repositoryHandler.getMetadataCollection());
    }


    /**
     * Take an entityDetail response and map it to the appropriate Project orientated response
     * @param response entityDetailResponse
     * @return Project response containing the appropriate Project object.
     */
    abstract protected SubjectAreaOMASAPIResponse getResponse( SubjectAreaOMASAPIResponse response);

    /**
     * Get relationships
     *
     * @param restAPIName ret API name
     * @param userId unique identifier for requesting user, under which the request is performed
     * @param guid   guid of the term to get
     * @param asOfTime the relationships returned as they were at this time. null indicates at the current time. If specified, the date is in milliseconds since 1970-01-01 00:00:00.
     * @param offset  the starting element number for this set of results.  This is used when retrieving elements
     *                 beyond the first page of results. Zero means the results start from the first element.
     * @param pageSize the maximum number of elements that can be returned on this request.
     *                 0 means there is not limit to the page size
     * @param sequencingOrder the sequencing order for the results.
     * @param sequencingProperty the name of the property that should be used to sequence the results.
     * @return the relationships associated with the requested guid
     *
     * when not successful the following Exception responses can occur
     * <ul>
     * <li> UserNotAuthorizedException           the requesting user is not authorized to issue this request.</li>
     * <li> MetadataServerUncontactableException not able to communicate with a Metadata respository service.</li>
     * <li> InvalidParameterException            one of the parameters is null or invalid.
     * <li> UnrecognizedGUIDException            the supplied guid was not recognised.</li>
     * <li> ClassificationException              Error processing a classification.</li>
     * <li> StatusNotSupportedException          A status value is not supported.</li>
     * </ul>
     */

    protected  SubjectAreaOMASAPIResponse getRelationshipsFromGuid(
                                                                String restAPIName,
                                                                String userId,
                                                                String guid,
                                                                Date asOfTime,
                                                                Integer offset,
                                                                Integer pageSize,
                                                                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                                String sequencingProperty
                                                               ) {

        SubjectAreaOMASAPIResponse response = null;
        SubjectAreaGlossaryRESTServices glossaryRESTServices = new SubjectAreaGlossaryRESTServices();
        glossaryRESTServices.setOMRSAPIHelper(this.oMRSAPIHelper);
        if (response == null) {
            try {
                InputValidator.validateGUIDNotNull(className, restAPIName, guid, "guid");
                // if offset or pagesize were not supplied then default them, so they can be converted to primitives.
                if (offset == null) {
                    offset = new Integer(0);
                }
                if (pageSize == null) {
                    pageSize = new Integer(0);
                }
                if (sequencingProperty != null) {
                    sequencingProperty = URLDecoder.decode(sequencingProperty, "UTF-8");
                }
                SequencingOrder omrsSequencingOrder = SubjectAreaUtils.convertOMASToOMRSSequencingOrder(sequencingOrder);
                List<Relationship> omrsRelationships = null;
                response = getRelationshipsFromGuid(
                        restAPIName,
                        userId,
                        guid,
                        null,
                        offset,
                        asOfTime,
                        sequencingProperty,
                        omrsSequencingOrder,
                        pageSize);
                if (response.getResponseCategory() == ResponseCategory.OmrsRelationships) {
                    RelationshipsResponse relationshipsResponse = (RelationshipsResponse) response;
                    omrsRelationships = relationshipsResponse.getRelationships();
                    response = SubjectAreaUtils.convertOMRSRelationshipsToOMASLines(oMRSAPIHelper, omrsRelationships);

                    if (response.getResponseCategory() == ResponseCategory.Lines) {
                        LinesResponse linesResponse = (LinesResponse) response;
                        List<Line> linesToReturn = linesResponse.getLines();

                        if (pageSize > 0) {
                            /*
                             * There are  some relationships that do not translate to lines for example CategoryAnchor is represented as the GlossarySummary object embedded in Category
                             * this is important if there is a page size - as we may end up returning less that a pagesize of data.
                             * The following logic attempts to get more relationships that can be turned in to Lines so that the requested page of data is returned.
                             */
                            int sizeToGet = 0;
                            // omrsRelationships.size() should be page size or less
                            if (omrsRelationships.size() > linesToReturn.size()) {
                                sizeToGet = omrsRelationships.size() - linesToReturn.size();
                            }
                            // bump up the offset by whay we have received.
                            offset = offset + omrsRelationships.size();

                            while (sizeToGet > 0) {

                                // there are more relationships we need to get
                                response = getRelationshipsFromGuid(
                                        restAPIName,
                                        userId,
                                        guid,
                                        null,
                                        offset,
                                        asOfTime,
                                        sequencingProperty,
                                        omrsSequencingOrder,
                                        sizeToGet);
                                sizeToGet = 0;
                                if (response.getResponseCategory() == ResponseCategory.OmrsRelationships) {
                                    relationshipsResponse = (RelationshipsResponse) response;
                                    List<Relationship> moreOmrsRelationships = relationshipsResponse.getRelationships();
                                    if (moreOmrsRelationships != null && moreOmrsRelationships.size() > 0) {

                                        response = SubjectAreaUtils.convertOMRSRelationshipsToOMASLines(oMRSAPIHelper, omrsRelationships);
                                        if (response.getResponseCategory() == ResponseCategory.Lines) {
                                            linesResponse = (LinesResponse) response;
                                            List<Line> moreLines = linesResponse.getLines();
                                            if (moreLines != null && !moreLines.isEmpty()) {
                                                // we have found more lines
                                                linesToReturn.addAll(moreLines);
                                                // check whether we need to get more.
                                                sizeToGet = moreOmrsRelationships.size() - moreLines.size();
                                                // bump up the offset by what we have just received.
                                                offset = offset + moreOmrsRelationships.size();
                                            }
                                        }
                                    }

                                }
                            }
                            response = new LinesResponse(linesToReturn);
                        }
                    }
                }
            } catch(InvalidParameterException e){
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            } catch (UnsupportedEncodingException e) {
                // TODO error
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + restAPIName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }
    /**
     * Get the relationships keyed off an entity guid.
     * @param restAPIName rest API name
     * @param userId user identity
     * @param entityGuid  globally unique identifier
     * @param relationshipTypeGuid the guid of the relationship type to restrict the relationships returned to this type. null means return all relationship types.
     * @param fromRelationshipElement the starting element number of the relationships to return.
     *                                This is used when retrieving elements
     *                                beyond the first page of results. Zero means start from the first element.
     * @param asOfTime Date return relationships as they were at some time in the past. null indicates to return relationships as they are now.
     * @param sequencingProperty String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder Enum defining how the results should be ordered.
     * @param pageSize  the maximum number of result classifications that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return {@code List<Line> }
     */
    private SubjectAreaOMASAPIResponse getRelationshipsFromGuid(
            String restAPIName,
            String                     userId,
            String                     entityGuid,
            String                     relationshipTypeGuid,
            int                        fromRelationshipElement,
            Date                       asOfTime,
            String                     sequencingProperty,
            SequencingOrder            sequencingOrder,
            int                        pageSize)
    {
        final String methodName = "getRelationshipsFromGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId="+userId+",entity guid="+entityGuid + ",relationship Type Guid="+relationshipTypeGuid);
        }
        SubjectAreaOMASAPIResponse response = null;

        try {
            InputValidator.validateUserIdNotNull(className,methodName,userId);

            InputValidator.validateGUIDNotNull(className,methodName,entityGuid,"entityGuid");

            response = oMRSAPIHelper.callGetRelationshipsForEntity( restAPIName, userId,
                                                                    entityGuid,
                                                                    relationshipTypeGuid,
                                                                    fromRelationshipElement,
                                                                    asOfTime,
                                                                    sequencingProperty,
                                                                    sequencingOrder,
                                                                    pageSize);
            if (response.getResponseCategory() == ResponseCategory.OmrsRelationships) {
                RelationshipsResponse relationshipsResponse = (RelationshipsResponse) response;
                List<Relationship> omrsRelationships = relationshipsResponse.getRelationships();
                if (omrsRelationships != null) {
                    Set<Relationship> relationshipSet = new HashSet<>(omrsRelationships);
                    response = SubjectAreaUtils.convertOMRSRelationshipsToOMASLines(oMRSAPIHelper, omrsRelationships);
                    // the response if successful will be LinesResponse
                }
            }
        } catch (InvalidParameterException e) {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ",guid=" + entityGuid);
        }
        return response;

    }

    /**
     * This method validated for creation.
     * @param glossaryHandler  glossary handler
     * @param userId  userId under which the request is performed
     * @param methodName method making the call
     * @param suppliedGlossary glossary to validate against.

     * @return SubjectAreaOMASAPIResponse this response is of type ResponseCategory.Category.Glossary if successful, otherwise there is an error response.
     */
    protected SubjectAreaOMASAPIResponse validateGlossarySummaryDuringCreation(
            SubjectAreaGlossaryHandler glossaryHandler,
            String userId,
            String methodName,
            GlossarySummary suppliedGlossary) {

        SubjectAreaOMASAPIResponse response = null;
        String guid =null;
        String relationshipGuid =null;
        /*
         * There needs to be an associated glossary supplied
         * The glossary could be of NodeType Glossary, Taxonomy , Canonical glossary or canonical and taxonomy.
         * The Glossary summary contains 4 identifying fields. We only require one of these fields to be supplied.
         * If more than one is supplied then we look for a glossary matching the supplied guid then matching the name.
         * Note if a relationship guid is supplied - then we reject this request - as the relationship cannot exist before one of its ends exists.
         */

        if (suppliedGlossary == null ) {
            // error - glossary is mandatory
            SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITHOUT_GLOSSARY;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(
                    methodName);
            log.error(errorMessage);
            InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                                        className,
                                                                        methodName,
                                                                        errorMessage,
                                                                        errorCode.getSystemAction(),
                                                                        errorCode.getUserAction());
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } else
        {
            guid = suppliedGlossary.getGuid();
            relationshipGuid = suppliedGlossary.getRelationshipguid();
            if (relationshipGuid != null)
            {
                // glossary relationship cannot exist before the Term exists.
                SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITH_GLOSSARY_RELATIONSHIP;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName, relationshipGuid);
                log.error(errorMessage);
                InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
            if (response == null)
            {
                if (guid == null)
                {
                    // error -  glossary guid is mandatory
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITHOUT_GLOSSARY;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);
                    log.error(errorMessage);
                    InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
                    response = OMASExceptionToResponse.convertInvalidParameterException(e);
                } else
                {
                    // find by guid
                    response = glossaryHandler.getGlossaryByGuid(userId, guid);
                    // set error code in case we failed
                    SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.CREATE_WITH_NON_EXISTANT_GLOSSARY_GUID;
                    if (response.getResponseCategory()!= ResponseCategory.Glossary) {
                        // glossary relationship cannot exist before the Term exists.
                        String errorMessage = errorCode.getErrorMessageId()
                                + errorCode.getFormattedErrorMessage(className,
                                                                     methodName, relationshipGuid);
                        log.error(errorMessage);
                        InvalidParameterException e = new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                                                    className,
                                                                                    methodName,
                                                                                    errorMessage,
                                                                                    errorCode.getSystemAction(),
                                                                                    errorCode.getUserAction());
                        response = OMASExceptionToResponse.convertInvalidParameterException(e);
                    }
                }
            }
        }
        return response;
    }
}
