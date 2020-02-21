/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.odf.metadatamanagement.handlers;


import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.odf.metadatamanagement.mappers.AnnotationMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.discovery.properties.Annotation;
import org.odpi.openmetadata.frameworks.discovery.properties.AnnotationStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnnotationHandler manages the storage and retrieval of metadata relating to annotations
 * as defined in the Open Discovery Framework (ODF).
 */
public class AnnotationHandler
{
    private String                  serviceName;
    private String                  serverName;
    private OMRSRepositoryHelper    repositoryHelper;
    private RepositoryHandler       repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;
    private DataFieldHandler        dataFieldHandler;


    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param dataFieldHandler handler for managing data fields
     */
    public AnnotationHandler(String                  serviceName,
                             String                  serverName,
                             InvalidParameterHandler invalidParameterHandler,
                             RepositoryHandler       repositoryHandler,
                             OMRSRepositoryHelper    repositoryHelper,
                             DataFieldHandler        dataFieldHandler)
    {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.dataFieldHandler = dataFieldHandler;
    }


    /**
     * Return the annotation subtype names.
     *
     * @return list of type names that are subtypes of annotation
     */
    public List<String>  getTypesOfAnnotation()
    {
        return repositoryHelper.getSubTypesOf(serviceName, AnnotationMapper.ANNOTATION_TYPE_NAME);
    }


    /**
     * Return the list of annotation subtype names mapped to their descriptions.
     *
     * @return list of type names that are subtypes of asset
     */
    public Map<String, String> getTypesOfAnnotationDescriptions()
    {
        List<String>        annotationTypeList = repositoryHelper.getSubTypesOf(serviceName, AnnotationMapper.ANNOTATION_TYPE_NAME);
        Map<String, String> annotationDescriptions = new HashMap<>();

        if (annotationTypeList != null)
        {
            for (String  annotationTypeName : annotationTypeList)
            {
                if (annotationTypeName != null)
                {
                    TypeDef annotationTypeDef = repositoryHelper.getTypeDefByName(serviceName, annotationTypeName);

                    if (annotationTypeDef != null)
                    {
                        annotationDescriptions.put(annotationTypeName, annotationTypeDef.getDescription());
                    }
                }
            }

        }

        if (annotationDescriptions.isEmpty())
        {
            return null;
        }

        return annotationDescriptions;
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param userId identifier of calling user
     * @param anchorGUID identifier of the anchor for the annotations.
     * @param anchorGUIDParameterName parameter that passed the identifier of the anchor for the annotations.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation> getAnnotationsLinkedToAnchor(String            userId,
                                                  String            anchorGUID,
                                                  String            anchorGUIDParameterName,
                                                  String            relationshipTypeGUID,
                                                  String            relationshipTypeName,
                                                  int               startingFrom,
                                                  int               maximumResults,
                                                  String            methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        // todo
        return null;
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param userId identifier of calling user
     * @param anchorGUID identifier of the anchor for the annotations.
     * @param anchorGUIDTypeName parameter that passed the type name of the anchor for the annotations.
     * @param anchorGUIDParameterName parameter that passed the identifier of the anchor for the annotations.
     * @param annotationStatus limit the results to this annotation status
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    List<Annotation> getAnnotationsLinkedToAnchor(String            userId,
                                                  String            anchorGUID,
                                                  String            anchorGUIDTypeName,
                                                  String            anchorGUIDParameterName,
                                                  String            relationshipTypeGUID,
                                                  String            relationshipTypeName,
                                                  AnnotationStatus  annotationStatus,
                                                  int               startingFrom,
                                                  int               maximumResults,
                                                  String            methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       userId,
                                                                                       anchorGUID,
                                                                                       anchorGUIDTypeName,
                                                                                       relationshipTypeGUID,
                                                                                       relationshipTypeName,
                                                                                       startingFrom,
                                                                                       queryPageSize,
                                                                                       methodName);

        List<Annotation> results = new ArrayList<>();
        while (iterator.moreToReceive())
        {
            // todo
        }

        return null;
    }





    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param annotationStatus status of the desired annotations - null means all statuses.
     * @param startingFrom starting position in the list
     * @param maximumResults maximum number of annotations that can be returned.
     * @param methodName calling method
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<Annotation>  getExtendedAnnotations(String           userId,
                                                    String           annotationGUID,
                                                    AnnotationStatus annotationStatus,
                                                    int              startingFrom,
                                                    int              maximumResults,
                                                    String           methodName) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String   annotationGUIDParameter = "annotationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameter, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, maximumResults, methodName);

        // todo
        return null;
    }


    /**
     * Retrieve a single annotation by unique identifier.  This call is typically used to retrieve the latest values
     * for an annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     * @param methodName calling method
     *
     * @return Annotation object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public  Annotation        getAnnotation(String   userId,
                                            String   annotationGUID,
                                            String   methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        return null;
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier of the anchor for the annotation
     * @param anchorGUIDParameterName name of parameter
     * @param relationshipTypeGUID guid for the relationship between the anchor and the annotation
     * @param relationshipTypeName name of the relationship between the anchor and the annotation
     * @param annotation annotation object
     * @param methodName calling method
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    private  String addAnnotationToAnchor(String     userId,
                                          String     anchorGUID,
                                          String     anchorGUIDParameterName,
                                          String     relationshipTypeGUID,
                                          String     relationshipTypeName,
                                          Annotation annotation,
                                          String     methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   annotationParameterName = "annotation";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        // todo
        return null;
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param userId identifier of calling user
     * @param anchorAnnotationGUID unique identifier of the annotation that this new one os to be attached to
     * @param annotation annotation object
     * @param methodName calling method
     * @return fully filled out annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    public  Annotation  addAnnotationToAnnotation(String     userId,
                                                  String     anchorAnnotationGUID,
                                                  Annotation annotation,
                                                  String     methodName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        final String   annotationGUIDParameterName = "anchorAnnotationGUID";
        final String   annotationParameterName = "annotation";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/extended-annotations";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorAnnotationGUID, annotationGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        // todo
        return null;
    }


    /**
     * Link an existing annotation to another object.  The anchor object must be a Referenceable.
     *
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be linked to
     * @param annotationGUID unique identifier of the annotation
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating annotations in the annotation store.
     */
    public  void    linkAnnotation(String userId,
                                   String anchorGUID,
                                   String annotationGUID,
                                   String methodName) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String   anchorGUIDParameterName = "anchorGUID";
        final String   annotationGUIDParameterName = "annotationGUID";
        final String   urlTemplate = "/servers/{0}/open-metadata/access-services/discovery-engine/users/{1}/annotations/{2}/related-instances{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        // todo
    }


    /**
     * Remove the relationship between an annotation and another object.
     *
     * @param userId identifier of calling user
     * @param anchorGUID unique identifier that the annotation is to be unlinked from
     * @param annotationGUID unique identifier of the annotation
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating annotations in the annotation store.
     */
    public  void    unlinkAnnotation(String userId,
                                     String anchorGUID,
                                     String annotationGUID,
                                     String methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String   anchorGUIDParameterName = "anchorGUID";
        final String   annotationGUIDParameterName = "annotationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(anchorGUID, anchorGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        // todo
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier
     * @param annotation new properties
     * @param methodName calling method
     *
     * @return fully filled out annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public  Annotation  updateAnnotation(String     userId,
                                         String     annotationGUID,
                                         Annotation annotation,
                                         String     methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String   annotationParameterName = "annotation";
        final String   annotationGUIDParameterName = "annotationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        // todo
        return null;
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    public  void  deleteAnnotation(String   userId,
                                   String   annotationGUID,
                                   String   methodName) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String   annotationGUIDParameterName = "annotationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        // todo
    }
}
