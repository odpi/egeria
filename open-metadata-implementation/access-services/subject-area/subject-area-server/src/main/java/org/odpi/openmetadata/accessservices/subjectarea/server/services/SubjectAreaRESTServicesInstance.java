/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.services;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.OMRSRelationshipToLines;
import org.odpi.openmetadata.accessservices.subjectarea.generated.server.SubjectAreaBeansToAccessOMRS;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.line.Line;
import org.odpi.openmetadata.accessservices.subjectarea.responses.OMASExceptionToResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.accessservices.subjectarea.validators.InputValidator;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.odpi.openmetadata.accessservices.subjectarea.initialization.SubjectAreaInstanceHandler;


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
    static private String accessServiceName = null;
    protected static SubjectAreaInstanceHandler instanceHandler = new SubjectAreaInstanceHandler();

    /**
     * Default constructor
     */
    public SubjectAreaRESTServicesInstance()
    {
    }

//    /**
//     * Setup the OMRSAPIHelper with the right instance based on the serverName
//     *
//     * @param serverName serverName, used to identify a server in a multitenant environment.
//     * @return SubjectAreaOMASAPIResponse null if successful
//     */
//    protected void initialiseOMRSAPIHelperForInstance(String serverName) throws MetadataServerUncontactableException
//    {
//        // this is set as a mock object for junits.
//        if (oMRSAPIHelper ==null)
//        {
//            oMRSAPIHelper = new OMRSAPIHelper();
//        }
//
//        OMRSRepositoryConnector omrsConnector = instanceHandler.getRepositoryConnector(serverName);
//        oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector);
    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param methodName method name for logging
     * @return the Subject Area OMAS bean to access OMRS
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    protected SubjectAreaBeansToAccessOMRS initializeAPI(String serverName, String userId, String methodName) throws MetadataServerUncontactableException,
            InvalidParameterException
    {
        return initializeAPI(serverName,userId,null,null,methodName);
    }
    /**
     * Each API call needs to run in the requested tenant - indicated by the serverName and validate the userid.
     * @param serverName server name used to create the instance
     * @param userId userid under which the request will be made
     * @param from effective from date
     * @param to effective to Date
     * @param methodName method name for logging
     * @return the Subject Area OMAS bean to access OMRS
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     */
    protected SubjectAreaBeansToAccessOMRS initializeAPI(String serverName, String userId, Date from, Date to, String methodName) throws MetadataServerUncontactableException, InvalidParameterException
    {
        // this is set as a mock object for junits.
        if (oMRSAPIHelper ==null)
        {
            oMRSAPIHelper = new OMRSAPIHelper();
        }
        // initialise omrs API helper with the right instance based on the server name
        OMRSRepositoryConnector omrsConnector = instanceHandler.getRepositoryConnector(serverName);
        oMRSAPIHelper.setOMRSRepositoryConnector(omrsConnector);
        SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = new SubjectAreaBeansToAccessOMRS();
        subjectAreaOmasREST.setOMRSAPIHelper(this.oMRSAPIHelper);
        InputValidator.validateUserIdNotNull(className, methodName, userId);
        InputValidator.validateEffectiveDate(className,methodName,to,from);
        return subjectAreaOmasREST;
    }

    public void setOMRSAPIHelper(OMRSAPIHelper oMRSAPIHelper)
    {
        this.oMRSAPIHelper = oMRSAPIHelper;
    }
  /*
     * Get Term relationships
     *
     * @param serverName serverName under which this request is performed, this is used in multi tenanting to identify the tenant
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

    public  SubjectAreaOMASAPIResponse getRelationshipsFromGuid(String serverName, String userId,
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

        SubjectAreaOMASAPIResponse response =null;
        try
        {
            // initialise omrs API helper with the right instance based on the server name
            SubjectAreaBeansToAccessOMRS subjectAreaOmasREST = initializeAPI(serverName, userId, methodName);
            InputValidator.validateGUIDNotNull(className, methodName, guid, "guid");
            // if offset or pagesize were not supplied then default them, so they can be converted to primitives.
            if (offset ==null) {
                offset = new Integer(0);
            }
            if (pageSize == null) {
                pageSize = new Integer(0);
            }
            if (sequencingProperty !=null) {
                try {
                    sequencingProperty = URLDecoder.decode(sequencingProperty,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    // TODO error
                }
            }
            SequencingOrder omrsSequencingOrder =  SubjectAreaUtils.convertOMASToOMRSSequencingOrder(sequencingOrder);
            Set<Line> omrsRelationships = getRelationshipsFromGuid(
                    userId,
                    guid,
                    null,
                    offset,
                    asOfTime,
                    sequencingProperty,
                    omrsSequencingOrder,
                    pageSize);
            List<Line> relationshipsToReturn = SubjectAreaUtils.convertOMRSLinesToOMASLines(omrsRelationships);
            int sizeToGet = 0;
            if (omrsRelationships.size() > relationshipsToReturn.size()) {
                sizeToGet = omrsRelationships.size() - relationshipsToReturn.size();
            }

            while (sizeToGet >0) {

                // there are more relationships we need to get
                omrsRelationships = getRelationshipsFromGuid(
                        userId,
                        guid,
                        null,
                        offset + sizeToGet,
                        asOfTime,
                        sequencingProperty,
                        omrsSequencingOrder,
                        sizeToGet);
                if (omrsRelationships != null) {
                    sizeToGet = getSizeToGet(omrsRelationships, relationshipsToReturn);
                    offset = +sizeToGet;
                }
            }
            response =new RelationshipsResponse(relationshipsToReturn);
        } catch (InvalidParameterException e)
        {
            response = OMASExceptionToResponse.convertInvalidParameterException(e);
        } catch (UserNotAuthorizedException e)
        {
            response = OMASExceptionToResponse.convertUserNotAuthorizedException(e);
        } catch (MetadataServerUncontactableException e)
        {
            response = OMASExceptionToResponse.convertMetadataServerUncontactableException(e);
        } catch (UnrecognizedGUIDException e)
        {
            response = OMASExceptionToResponse.convertUnrecognizedGUIDException(e);
        } catch (FunctionNotSupportedException e)
        {
            //should not occur as it is issued when a type guid is supplied - we supply null. TODO appropriate error/log
        }



        if (log.isDebugEnabled())
        {
            log.debug("<== successful method : " + methodName + ",userId=" + userId + ", Response=" + response);
        }
        return response;
    }
    /**
     * Get the relationships keyed off an entity guid.
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
     * @throws MetadataServerUncontactableException not able to communicate with the metadata server.
     * @throws UserNotAuthorizedException the userId passed on the request is not authorized to perform the requested action.
     * @throws InvalidParameterException a parameter is null or an invalid value.
     * @throws UnrecognizedGUIDException the unique identifier (guid) used to request an object is either unrecognized, or is the identifier for a different type of object.
     * @throws FunctionNotSupportedException a function is not supported.
     */
    private Set<Line> getRelationshipsFromGuid(
            String                     userId,
            String                     entityGuid,
            String                     relationshipTypeGuid,
            int                        fromRelationshipElement,
            Date                       asOfTime,
            String                     sequencingProperty,
            SequencingOrder            sequencingOrder,
            int                        pageSize)
            throws MetadataServerUncontactableException,
            UserNotAuthorizedException,
            InvalidParameterException,
            FunctionNotSupportedException,
            UnrecognizedGUIDException {
        final String methodName = "getRelationshipsFromGuid";
        if (log.isDebugEnabled()) {
            log.debug("==> Method: " + methodName + ",userId="+userId+",entity guid="+entityGuid + ",relationship Type Guid="+relationshipTypeGuid);
        }
        InputValidator.validateUserIdNotNull(className,methodName,userId);
        InputValidator.validateGUIDNotNull(className,methodName,entityGuid,"entityGuid");

        Set<Line> lines = null;

        List<Relationship> omrsRelationships = oMRSAPIHelper.callGetRelationshipsForEntity( userId,
                entityGuid,
                relationshipTypeGuid,
                fromRelationshipElement,
                asOfTime,
                sequencingProperty,
                sequencingOrder,
                pageSize);
        if (omrsRelationships !=null) {
            lines =  OMRSRelationshipToLines.convert(omrsRelationships);
        }
        if (log.isDebugEnabled()) {
            log.debug("<== successful method : " + methodName + ",userId="+userId+",guid="+entityGuid);
        }
        return lines;
    }
    private int getSizeToGet(Set<Line> omrsTermRelationships, List<Line> termRelationships) {
        int sizeToGet =0;
        List<Line> moreTermRelationships = SubjectAreaUtils.convertOMRSLinesToOMASLines(omrsTermRelationships);
        if ( moreTermRelationships !=null && moreTermRelationships.size() >0) {
            for (Line moreTermRelationship : moreTermRelationships) {
                termRelationships.add(moreTermRelationship);
            }
            sizeToGet = omrsTermRelationships.size() - moreTermRelationships.size();

        }
        return sizeToGet;
    }
}
