/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.MetadataServerUncontactableException;
import org.odpi.openmetadata.accessservices.subjectarea.initialization.SubjectAreaInstanceHandler;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipResponse;
import org.odpi.openmetadata.accessservices.subjectarea.internalresponse.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineBundle;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineBundleFactory;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ResponseFactory;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;


/**
 * SubjectAreaRESTServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaRESTServicesInstance
{
    private static final Logger log = LoggerFactory.getLogger(SubjectAreaRESTServicesInstance.class);
    private static final String className = SubjectAreaRESTServicesInstance.class.getName();

    // The OMRSAPIHelper allows the junits to mock out the omrs layer.
    protected OMRSAPIHelper oMRSAPIHelper = null;
    protected static SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    /**
     * Default constructor
     */
    public SubjectAreaRESTServicesInstance()
    {
    }

    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param restAPIName rest API name for logging
     * @return null if successful or the response containing the error
     */
    protected  SubjectAreaOMASAPIResponse  initializeAPI(String serverName, String userId, String restAPIName)
    {

        return  initializeAPI(serverName, userId,null,null, restAPIName);

    }
    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param from effective from date
     * @param to effective to Date
     * @param restAPIName rest API name for logging
     * @return null if successful or the response containing the error
     */
    protected SubjectAreaOMASAPIResponse initializeAPI(String serverName, String userId, Date from, Date to, String  restAPIName)

    {
        SubjectAreaOMASAPIResponse response = null;

        // initialise omrs API helper with the right instance based on the server name
        OMRSRepositoryConnector omrsConnector = null;
        try
        {
            // this is set as a mock object for junits.
            if (oMRSAPIHelper ==null)
            {
                oMRSAPIHelper = new OMRSAPIHelper(instanceHandler.getAccessServiceName(serverName));
            }
            omrsConnector = instanceHandler.getRepositoryConnector(serverName);
            oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector, restAPIName);
            InputValidator.validateUserIdNotNull(className,  restAPIName, userId);
            InputValidator.validateEffectiveDate(className, restAPIName,to,from);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        }

        return response;
    }

    public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper = oMRSAPIHelper;
    }
    /*
     * Get Term relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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
     * Exceptions returned by the server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws FunctionNotSupportedException   Function not supported
     *
     * Client library Exceptions
     * @throws MetadataServerUncontactableException Unable to contact the server
     * @throws UnexpectedResponseException an unexpected response was returned from the server
     */

    public  SubjectAreaOMASAPIResponse getRelationshipsFromGuid(String serverName,
                                                                String restAPIName,
                                                                String userId,
                                                                String guid,
                                                                Date asOfTime,
                                                                Integer offset,
                                                                Integer pageSize,
                                                                org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SequencingOrder sequencingOrder,
                                                                String sequencingProperty
    ) {
        final String methodName = "getRelationshipsFromGuid";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response =initializeAPI(serverName, userId, methodName);
        if (response == null) {
            try {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
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
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
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
    protected SubjectAreaOMASAPIResponse createLine(String serverName, String restAPIName, String userId,String className,Line line ) {
        String methodName = "createLine";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId +",className=" +className );
        }

        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response ==null)
        {
            ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
            ILineBundle bundle = factory.getInstance(className);
            ILineMapper mapper = bundle.getMapper();

            try
            {
                Relationship omrsRelationship = mapper.mapLineToRelationship(line);
                response = oMRSAPIHelper.callOMRSAddRelationship(restAPIName, userId, omrsRelationship);
                if (response.getResponseCategory() == ResponseCategory.OmrsRelationship)
                {
                    Relationship createdOMRSRelationship = ((RelationshipResponse) response).getRelationship();
                    Line createdLine = mapper.mapRelationshipToLine(createdOMRSRelationship);
                    response = new ResponseFactory().getInstance(className, createdLine);
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;

    }
    protected SubjectAreaOMASAPIResponse getLine(String serverName, String restAPIName, String userId,String className,String guid ) {

        String methodName = "getLine";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null)
        {
            ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
            ILineBundle bundle = factory.getInstance(className);
            ILineMapper mapper = bundle.getMapper();

            try
            {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, guid);
                if (response.getResponseCategory() == ResponseCategory.OmrsRelationship)
                {
                    Relationship createdOMRSRelationship = ((RelationshipResponse) response).getRelationship();
                    Line gotLine = mapper.mapRelationshipToLine(createdOMRSRelationship);
                    response = new ResponseFactory().getInstance(className, gotLine);
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }

        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }

    /**
     * Update the line.
     * @param serverName server name to process the request againts
     * @param restAPIName rest API Name
     * @param userId userid user identifier
     * @param className classname for logging
     * @param line line to update
     * @param isReplace whether the other is a replace or an update
     * @return updated line
     */
    protected SubjectAreaOMASAPIResponse updateLine(String serverName, String restAPIName, String userId,String className,Line line,boolean isReplace )
    {
        String methodName = "updateLine";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",className=" + className, ",isReplace="+isReplace);
        }

        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null)
        {
            ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
            ILineBundle bundle = factory.getInstance(className);
            ILineMapper mapper = bundle.getMapper();

            try
            {
                String relationshipGuid = line.getGuid();
                InputValidator.validateGUIDNotNull(className, methodName, relationshipGuid, "termGuid");
                response = oMRSAPIHelper.callOMRSGetRelationshipByGuid(restAPIName, userId, relationshipGuid);
                if (response.getResponseCategory() == ResponseCategory.OmrsRelationship)
                {
                    Relationship originalRelationship = ((RelationshipResponse) response).getRelationship();
                    Relationship relationshipToUpdate = mapper.mapLineToRelationship(line);

                    if (isReplace)
                    {
                        // use the relationship as supplied
                    } else
                    {
                        if (relationshipToUpdate.getProperties() !=null && relationshipToUpdate.getProperties().getPropertyCount()>0) {
                            Map<String, InstancePropertyValue> updateInstanceProperties = relationshipToUpdate.getProperties().getInstanceProperties();
                            if (originalRelationship.getProperties() != null) {
                                Map<String, InstancePropertyValue> orgInstanceProperties = originalRelationship.getProperties().getInstanceProperties();

                                // if there a property that already exists but is not in the update properties then make sure that value is not overwritten by including it in this update request.
                                for (String orgPropertyName : orgInstanceProperties.keySet()) {
                                    if (!updateInstanceProperties.keySet().contains(orgPropertyName)) {
                                        // make sure the original value is not lost.
                                        updateInstanceProperties.put(orgPropertyName, orgInstanceProperties.get(orgPropertyName));
                                    }
                                }
                            }
                            InstanceProperties instancePropertiesToUpdate = new InstanceProperties();
                            instancePropertiesToUpdate.setInstanceProperties(updateInstanceProperties);
                            // copy over with effectivity dates - honour what is in the request. So null means we lose any effectivity time that are set.
                            instancePropertiesToUpdate.setEffectiveFromTime(line.getEffectiveFromTime());
                            instancePropertiesToUpdate.setEffectiveToTime(line.getEffectiveToTime());
                            relationshipToUpdate.setProperties(instancePropertiesToUpdate);
                        }
                    }
                    if (relationshipToUpdate.getProperties() ==null || relationshipToUpdate.getProperties().getPropertyCount() ==0) {
                        // nothing to update.
                        // TODO may need to change this logic if effectivity updates can be made through this call.
                        SubjectAreaErrorCode errorCode = SubjectAreaErrorCode.LINE_UPDATE_ATTEMPTED_WITH_NO_PROPERTIES;
                        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(className, methodName);
                        log.error(errorMessage);
                        response = new InvalidParameterExceptionResponse(
                                new InvalidParameterException(errorCode.getHTTPErrorCode(), className, methodName, errorMessage, errorCode.getSystemAction(), errorCode.getUserAction())
                                );
                    } else {
                        response = oMRSAPIHelper.callOMRSUpdateRelationship(restAPIName, userId, relationshipToUpdate);
                        if (response.getResponseCategory() == ResponseCategory.OmrsRelationship) {
                            Relationship updatedOmrsRelationship = ((RelationshipResponse) response).getRelationship();
                            Line updatedLine = mapper.mapRelationshipToLine(updatedOmrsRelationship);
                            response = new ResponseFactory().getInstance(className, updatedLine);
                        }
                    }
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        } if (log.isDebugEnabled())
    {
        log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
    }
        return response;
    }
    public SubjectAreaOMASAPIResponse deleteLine(String serverName, String restAPIName, String userId,String className,String guid, Boolean isPurge) {

        String methodName = "deleteLine";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid + ",isPurge="+isPurge);
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null)
        {
            OMRSRepositoryHelper repositoryHelper  = oMRSAPIHelper.getOMRSRepositoryHelper();
            ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
            ILineBundle bundle = factory.getInstance(className);
            ILineMapper mapper = bundle.getMapper();

            try
            {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                StringTokenizer st = new StringTokenizer(className,".");

                String typeName = bundle.getTypeName();
                String source = oMRSAPIHelper.getServiceName();
                String typeGuid = repositoryHelper.getTypeDefByName(source, typeName).getGUID();
                if (isPurge)
                {
                    response = oMRSAPIHelper.callOMRSPurgeRelationship(restAPIName, userId, typeGuid, typeName, guid);
                } else
                {
                    response = oMRSAPIHelper.callOMRSDeleteRelationship(restAPIName, userId, typeGuid, typeName, guid);
                    if (response.getResponseCategory() == ResponseCategory.OmrsRelationship)
                    {
                        Relationship omrsRelationship = ((RelationshipResponse) response).getRelationship();
                        Line deletedLine= mapper.mapRelationshipToLine(omrsRelationship);
                        response = new ResponseFactory().getInstance(className, deletedLine);
                    }
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
    protected SubjectAreaOMASAPIResponse restoreLine(String serverName, String restAPIName, String userId,String className,String guid ) {

        String methodName = "deleteLine";
        if (log.isDebugEnabled())
        {
            log.debug("==> Method: " + methodName + ",userId=" + userId + ",guid=" + guid );
        }
        // initialise omrs API helper with the right instance based on the server name
        SubjectAreaOMASAPIResponse response = initializeAPI(serverName, userId, methodName);
        if (response == null)
        {
            ILineBundleFactory factory = new ILineBundleFactory(oMRSAPIHelper);
            ILineBundle bundle = factory.getInstance(className);
            ILineMapper mapper = bundle.getMapper();

            try
            {
                InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
                response = this.oMRSAPIHelper.callOMRSRestoreRelationship(restAPIName, userId, guid);
                if (response.getResponseCategory() == ResponseCategory.OmrsRelationship)
                {
                    Relationship omrsRelationship = ((RelationshipResponse) response).getRelationship();
                    Line restoredLine =  mapper.mapRelationshipToLine(omrsRelationship);
                    response =new ResponseFactory().getInstance(className, restoredLine);
                }
            } catch (InvalidParameterException e)
            {
                response = OMASExceptionToResponse.convertInvalidParameterException(e);
            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", response=" + response);
        }
        return response;
    }
}
