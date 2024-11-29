/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import org.odpi.openmetadata.frameworks.surveyaction.properties.*;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;

/**
 * AnnotationHandler manages the storage and retrieval of metadata relating to annotations
 * as defined in the Survey Action Framework (ODF).  It has both specific support for creating annotations from
 * ODF annotation beans and generic support for retrieving annotations.  The reason for this hybrid approach is that there are a huge range
 * of annotation types in ODF and currently all OMASs that work with discovery metadata use the ODF beans on their API.
 * Therefore, it makes sense to have support for these beans in a common location so that the implementation can be shared.
 * Note: this handler only supports current effective time with lineage and deduplication set to false since this is all the current
 * discovery use cases need.
 */
public class AnnotationHandler<B> extends ReferenceableHandler<B>
{
    /**
     * Construct the annotation handler with information needed to work with B objects.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve B instances from.
     * @param defaultZones list of zones that the access service should set in all new B instances.
     * @param publishZones list of zones that the access service sets up in published B instances.
     * @param auditLog destination for audit log events.
     */
    public AnnotationHandler(OpenMetadataAPIGenericConverter<B> converter,
                             Class<B>                           beanClass,
                             String                             serviceName,
                             String                             serverName,
                             InvalidParameterHandler            invalidParameterHandler,
                             RepositoryHandler                  repositoryHandler,
                             OMRSRepositoryHelper               repositoryHelper,
                             String                             localServerUserId,
                             OpenMetadataServerSecurityVerifier securityVerifier,
                             List<String>                       supportedZones,
                             List<String>                       defaultZones,
                             List<String>                       publishZones,
                             AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);
    }


    /*========================================
     * Retrieve information about supported annotation types.
     */

    /**
     * Return the annotation subtype names.  These are derived from the types defined in the server.
     *
     * @return list of type names that are subtypes of annotation
     */
    public List<String>  getTypesOfAnnotation()
    {
        return repositoryHelper.getSubTypesOf(serviceName, OpenMetadataType.ANNOTATION.typeName);
    }


    /**
     * Return the list of annotation subtype names mapped to their descriptions.
     *
     * @return map of type names that are subtypes of asset to their description
     */
    public Map<String, String> getTypesOfAnnotationDescriptions()
    {
        List<String>        annotationTypeList = repositoryHelper.getSubTypesOf(serviceName, OpenMetadataType.ANNOTATION.typeName);
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


    /*======================================
     * Provide specific methods for managing Annotation beans from the Open Discovery Framework (ODF)
     */


    /**
     * Load the properties in the annotation bean into an Annotation build.  This method has to take into account the different
     * types of annotation beans.  It uses the java class of the Annotation bean to extract the all the properties.
     *
     * @param typeName  type name to use for the entity
     * @param typeGUID  type GUID to use for the entity
     * @param annotation annotation to save
     * @param methodName calling method
     * @return builder object loaded with the properties from the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     */
    private AnnotationBuilder getAnnotationBuilder(String     typeGUID,
                                                   String     typeName,
                                                   Annotation annotation,
                                                   String     methodName) throws InvalidParameterException
    {
        final String  parameterName  = "annotation";

        invalidParameterHandler.validateObject(annotation, parameterName, methodName);

        AnnotationBuilder builder = new AnnotationBuilder(annotation.getAnnotationType(),
                                                          annotation.getSummary(),
                                                          annotation.getConfidenceLevel(),
                                                          annotation.getExpression(),
                                                          annotation.getExplanation(),
                                                          annotation.getAnalysisStep(),
                                                          annotation.getJsonProperties(),
                                                          annotation.getAdditionalProperties(),
                                                          typeGUID,
                                                          typeName,
                                                          annotation.getExtendedProperties(),
                                                          null,
                                                          repositoryHelper,
                                                          serviceName,
                                                          serverName);

        // todo add all the other types
        if (annotation instanceof ClassificationAnnotation classificationAnnotation)
        {
            builder.setClassificationSubtypeProperties(classificationAnnotation.getCandidateClassifications());
        }
        else if (annotation instanceof DataClassAnnotation dataClassAnnotation)
        {
            builder.setDataClassSubtypeProperties(dataClassAnnotation.getCandidateDataClassGUIDs(),
                                                  dataClassAnnotation.getMatchingValues(),
                                                  dataClassAnnotation.getNonMatchingValues());
        }
        else if (annotation instanceof ResourceProfileAnnotation resourceProfileAnnotation)
        {
            builder.setResourceProfileSubtypeProperties(resourceProfileAnnotation.getProfilePropertyNames(),
                                                        resourceProfileAnnotation.getLength(),
                                                        resourceProfileAnnotation.getInferredDataType(),
                                                        resourceProfileAnnotation.getInferredFormat(),
                                                        resourceProfileAnnotation.getInferredLength(),
                                                        resourceProfileAnnotation.getInferredPrecision(),
                                                        resourceProfileAnnotation.getInferredScale(),
                                                        resourceProfileAnnotation.getProfileStartDate(),
                                                        resourceProfileAnnotation.getProfileEndDate(),
                                                        resourceProfileAnnotation.getProfileProperties(),
                                                        resourceProfileAnnotation.getProfileFlags(),
                                                        resourceProfileAnnotation.getProfileDates(),
                                                        resourceProfileAnnotation.getProfileCounts(),
                                                        resourceProfileAnnotation.getProfileDoubles(),
                                                        resourceProfileAnnotation.getValueList(),
                                                        resourceProfileAnnotation.getValueCount(),
                                                        resourceProfileAnnotation.getValueRangeFrom(),
                                                        resourceProfileAnnotation.getValueRangeTo(),
                                                        resourceProfileAnnotation.getAverageValue());
        }
        else if (annotation instanceof ResourcePhysicalStatusAnnotation resourcePhysicalStatusAnnotation)
        {
            builder.setResourcePhysicalStatusSubtypeProperties(resourcePhysicalStatusAnnotation.getResourceProperties(),
                                                               resourcePhysicalStatusAnnotation.getCreateTime(),
                                                               resourcePhysicalStatusAnnotation.getModifiedTime(),
                                                               resourcePhysicalStatusAnnotation.getSize(),
                                                               resourcePhysicalStatusAnnotation.getEncoding());
        }
        else if (annotation instanceof ResourceMeasureAnnotation resourceMeasureAnnotation)
        {
            builder.setResourceMeasurementSubtypeProperties(resourceMeasureAnnotation.getResourceProperties());
        }
        else if (annotation instanceof QualityAnnotation qualityAnnotation)
        {
            builder.setQualitySubtypeProperties(qualityAnnotation.getQualityDimension(),
                                                qualityAnnotation.getQualityScore());
        }
        else if (annotation instanceof RelationshipAdviceAnnotation relationshipAdviceAnnotation)
        {
            builder.setRelationshipAdviceSubtypeProperties(relationshipAdviceAnnotation.getRelatedEntityGUID(),
                                                           relationshipAdviceAnnotation.getRelationshipTypeName(),
                                                           relationshipAdviceAnnotation.getRelationshipProperties());
        }
        else if (annotation instanceof RequestForActionAnnotation requestForActionAnnotation)
        {
            builder.setRequestForActionSubtypeProperties(requestForActionAnnotation.getSurveyActivity(),
                                                         requestForActionAnnotation.getActionRequested(),
                                                         requestForActionAnnotation.getActionProperties());
        }
        else if (annotation instanceof SchemaAnalysisAnnotation schemaAnalysisAnnotation)
        {
            builder.setSchemaAnalysisSubTypeProperties(schemaAnalysisAnnotation.getSchemaName(),
                                                       schemaAnalysisAnnotation.getSchemaName());
        }
        else if (annotation instanceof SemanticAnnotation semanticAnnotation)
        {
            builder.setSemanticSubTypeProperties(semanticAnnotation.getInformalTerm(),
                                                 semanticAnnotation.getInformalTopic(),
                                                 semanticAnnotation.getCandidateGlossaryTermGUIDs(),
                                                 semanticAnnotation.getCandidateGlossaryCategoryGUIDs());
        }

        return builder;
    }


    /**
     * Determine the type identifier of the entity to save.
     *
     * @return unique identifier of the annotation's type
     * @throws InvalidParameterException the type is not a valid annotation type
     */
    private String getAnnotationTypeGUID(String  typeName,
                                         String  methodName) throws InvalidParameterException
    {
        return invalidParameterHandler.validateTypeName(typeName,
                                                        OpenMetadataType.ANNOTATION.typeName,
                                                        serviceName,
                                                        methodName,
                                                        repositoryHelper);
    }



    /**
     * Determine the type name of the entity to save. If the type is defined in the annotation then this value is used.
     * If no typeName is provided in the annotation then we use the class of the bean.
     *
     * @param annotation annotation to save
     * @return unique name of the annotation's type
     */
    private String getAnnotationTypeName(Annotation annotation)
    {
        if (annotation != null)
        {
            if (annotation.getOpenMetadataTypeName() != null)
            {
                return annotation.getOpenMetadataTypeName();
            }
        }

        return OpenMetadataType.ANNOTATION.typeName;
    }


    /**
     * Save a new annotation as an entity.  The calling method will link it to its asset via the
     * discovery analysis report.
     *
     * @param userId calling user
     * @param assetGUID asset that the annotation describes
     * @param annotation annotation to save
     * @param methodName calling method
     * @return unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    private String addNewAnnotation(String     userId,
                                    String     assetGUID,
                                    Annotation annotation,
                                    String     methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        String typeName = this.getAnnotationTypeName(annotation);
        String typeGUID = this.getAnnotationTypeGUID(typeName, methodName);

        AnnotationBuilder builder = getAnnotationBuilder(typeGUID,
                                                         typeName,
                                                         annotation,
                                                         methodName);

        this.addAnchorGUIDToBuilder(userId,
                                    assetGUID,
                                    assetGUIDParameterName,
                                    false,
                                    false,
                                    new Date(),
                                    supportedZones,
                                    builder,
                                    methodName);

        return this.createBeanInRepository(userId,
                                           null,
                                           null,
                                           typeGUID,
                                           typeName,
                                           builder,
                                           new Date(),
                                           methodName);
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param userId identifier of calling user
     * @param surveyReportGUID unique identifier of the discovery analysis report
     * @param annotation annotation object
     * @param methodName calling method
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving adding the annotation to the annotation store.
     */
    public  String  addAnnotationToSurveyReport(String     userId,
                                                String     surveyReportGUID,
                                                Annotation annotation,
                                                String     methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String annotationParameterName = "annotation";
        final String discoveryReportGUIDParameterName = "surveyReportGUID";

        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        Date effectiveTime = new Date();

        String assetGUID          = null;
        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              surveyReportGUID,
                                                              discoveryReportGUIDParameterName,
                                                              OpenMetadataType.SURVEY_REPORT.typeName,
                                                              true,
                                                              false,
                                                              false,
                                                              false,
                                                              supportedZones,
                                                              effectiveTime,
                                                              methodName);

        if (anchorEntity != null)
        {
            assetGUID = anchorEntity.getGUID();
        }

        String annotationGUID = this.addNewAnnotation(userId, assetGUID, annotation, methodName);

        if (annotationGUID != null)
        {
            this.linkElementToElement(userId,
                                      null,
                                      null,
                                      annotationGUID,
                                      annotationParameterName,
                                      OpenMetadataType.ANNOTATION.typeName,
                                      surveyReportGUID,
                                      discoveryReportGUIDParameterName,
                                      OpenMetadataType.SURVEY_REPORT.typeName,
                                      false,
                                      false,
                                      supportedZones,
                                      OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                      null,
                                      null,
                                      null,
                                      effectiveTime,
                                      methodName);
        }

        return annotationGUID;
    }



    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param userId identifier of calling user
     * @param parentAnnotationGUID unique identifier of the annotation that this new one is to be attached to
     * @param annotation annotation object
     * @param methodName calling method
     * @return unique identifier of the new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    public  String  addAnnotationToAnnotation(String     userId,
                                              String     parentAnnotationGUID,
                                              Annotation annotation,
                                              String     methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String parentAnnotationGUIDParameterName = "parentAnnotationGUID";
        final String annotationParameterName = "annotation";

        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        Date effectiveTime = new Date();

        String assetGUID          = null;
        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              parentAnnotationGUID,
                                                              parentAnnotationGUIDParameterName,
                                                              OpenMetadataType.ANNOTATION.typeName,
                                                              true,
                                                              false,
                                                              false,
                                                              false,
                                                              supportedZones,
                                                              effectiveTime,
                                                              methodName);

        if (anchorEntity != null)
        {
            assetGUID = anchorEntity.getGUID();
        }

        String annotationGUID = this.addNewAnnotation(userId, assetGUID, annotation, methodName);

        if (annotationGUID != null)
        {
            this.linkElementToElement(userId,
                                      null,
                                      null,
                                      parentAnnotationGUID,
                                      parentAnnotationGUIDParameterName,
                                      OpenMetadataType.ANNOTATION.typeName,
                                      annotationGUID,
                                      annotationParameterName,
                                      OpenMetadataType.ANNOTATION.typeName,
                                      false,
                                      false,
                                      supportedZones,
                                      OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                      null,
                                      null,
                                      null,
                                      effectiveTime,
                                      methodName);
        }

        return annotationGUID;
    }



    /**
     * Replace the current properties of an annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier
     * @param annotation new properties
     * @param methodName calling method
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public  void  updateAnnotation(String     userId,
                                   String     annotationGUID,
                                   Annotation annotation,
                                   String     methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String annotationParameterName     = "annotation";
        final String annotationGUIDParameterName = "annotationGUID";

        invalidParameterHandler.validateObject(annotation, annotationParameterName, methodName);

        String typeName = this.getAnnotationTypeName(annotation);
        String typeGUID = this.getAnnotationTypeGUID(typeName, methodName);

        AnnotationBuilder builder = getAnnotationBuilder(typeGUID,
                                                         typeName,
                                                         annotation,
                                                         methodName);

        this.updateBeanInRepository(userId,
                                    null,
                                    null,
                                    annotationGUID,
                                    annotationGUIDParameterName,
                                    OpenMetadataType.ANNOTATION.typeGUID,
                                    OpenMetadataType.ANNOTATION.typeName,
                                    false,
                                    false,
                                    supportedZones,
                                    builder.getInstanceProperties(methodName),
                                    false,
                                    new Date(),
                                    methodName);
    }


    /*==============================================
     * Generic annotation methods
     */


    /**
     * Attach an annotation review to an annotation.  The annotation status is stored in the
     * link between the annotation and the annotation review.  The rest of the properties
     * are for the AnnotationReview entity.
     *
     * @param userId calling user
     * @param annotationGUID annotation to attach to
     * @param annotationGUIDParameterName parameter supplying annotationGUID
     * @param annotationStatus status of the annotation as a result of the review
     * @param reviewDate date of the review
     * @param steward steward who performed the review
     * @param comment comments from the steward
     * @param annotationReviewTypeGUID subtype of the annotation review (or null for standard type)
     * @param annotationReviewTypeName subtype of the annotation review (or null for standard type)
     * @param extendedProperties any additional properties from the subtype
     * @param methodName calling method
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public void addAnnotationReview(String              userId,
                                    String              annotationGUID,
                                    String              annotationGUIDParameterName,
                                    int                 annotationStatus,
                                    Date                reviewDate,
                                    String              steward,
                                    String              comment,
                                    String              annotationReviewTypeGUID,
                                    String              annotationReviewTypeName,
                                    Map<String, Object> extendedProperties,
                                    String              methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String annotationReviewGUIDParameterName = "annotationReviewGUID";

        String typeGUID = OpenMetadataType.ANNOTATION_REVIEW.typeGUID;
        String typeName = OpenMetadataType.ANNOTATION_REVIEW.typeName;

        if (annotationReviewTypeGUID != null)
        {
            typeGUID = annotationReviewTypeGUID;
        }

        if (annotationReviewTypeName != null)
        {
            typeName = annotationReviewTypeName;
        }

        Date effectiveTime = new Date();

        AnnotationReviewBuilder builder = new AnnotationReviewBuilder(annotationStatus,
                                                                      reviewDate,
                                                                      steward,
                                                                      comment,
                                                                      typeGUID,
                                                                      typeName,
                                                                      extendedProperties,
                                                                      repositoryHelper,
                                                                      serviceName,
                                                                      serverName);
        EntityDetail anchorEntity = this.validateAnchorEntity(userId,
                                                              annotationGUID,
                                                              annotationGUIDParameterName,
                                                              OpenMetadataType.ANNOTATION.typeName,
                                                              true,
                                                              false,
                                                              false,
                                                              false,
                                                              supportedZones,
                                                              effectiveTime,
                                                              methodName);

        if ((anchorEntity != null) && (anchorEntity.getGUID() != null))
        {
            builder.setAnchors(userId,
                               anchorEntity.getGUID(),
                               anchorEntity.getType().getTypeDefName(),
                               this.getDomainName(anchorEntity),
                               methodName);
        }

        String annotationReviewGUID = this.createBeanInRepository(userId,
                                                                  null,
                                                                  null,
                                                                  typeGUID,
                                                                  typeName,
                                                                  builder,
                                                                  effectiveTime,
                                                                  methodName);

        if (annotationReviewGUID != null)
        {
            this.linkElementToElement(userId,
                                      null,
                                      null,
                                      annotationGUID,
                                      annotationGUIDParameterName,
                                      OpenMetadataType.ANNOTATION.typeName,
                                      annotationReviewGUID,
                                      annotationReviewGUIDParameterName,
                                      OpenMetadataType.ANNOTATION_REVIEW.typeName,
                                      false,
                                      false,
                                      supportedZones,
                                      OpenMetadataType.ANNOTATION_REVIEW_LINK_RELATIONSHIP.typeGUID,
                                      OpenMetadataType.ANNOTATION_REVIEW_LINK_RELATIONSHIP.typeName,
                                      builder.getReviewLinkInstanceProperties(methodName),
                                      null,
                                      null,
                                      effectiveTime,
                                      methodName);
        }
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param userId identifier of calling user
     * @param annotationGUID unique identifier of the annotation
     * @param methodName calling method
     *
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

        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    annotationGUID,
                                    annotationGUIDParameterName,
                                    OpenMetadataType.ANNOTATION.typeGUID,
                                    OpenMetadataType.ANNOTATION.typeName,
                                    null,
                                    null,
                                    false,
                                    false,
                                    new Date(),
                                    methodName);
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
    public  B  getAnnotation(String   userId,
                             String   annotationGUID,
                             String   methodName) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        final String   annotationGUIDParameterName = "annotationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(annotationGUID, annotationGUIDParameterName, methodName);

        EntityDetail entity = repositoryHandler.getEntityByGUID(userId,
                                                                annotationGUID,
                                                                annotationGUIDParameterName,
                                                                OpenMetadataType.ANNOTATION.typeName,
                                                                false,
                                                                false,
                                                                new Date(),
                                                                methodName);

        return converter.getNewBean(beanClass, entity, methodName);
    }


    /**
     * Retrieve the supplementary entities and relationships for an annotation and then
     * call the annotation converter to create a new bean.
     *
     * @param userId calling user
     * @param annotationEntity retrieved entity that represents the annotation
     * @param methodName calling method
     * @return filled out annotation bean
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    private B getAnnotationBean(String         userId,
                                EntityDetail   annotationEntity,
                                String         methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String annotationGUIDParameterName = "annotation.getGUID";

        if (annotationEntity != null)
        {
            List<EntityDetail> supplementaryEntities = null;

            Date effectiveTime = new Date();

            EntityDetail annotationReviewEntity = this.getAttachedEntity(userId,
                                                                         annotationEntity.getGUID(),
                                                                         annotationGUIDParameterName,
                                                                         OpenMetadataType.ANNOTATION.typeName,
                                                                         OpenMetadataType.ANNOTATION_REVIEW_LINK_RELATIONSHIP.typeGUID,
                                                                         OpenMetadataType.ANNOTATION_REVIEW_LINK_RELATIONSHIP.typeName,
                                                                         OpenMetadataType.ANNOTATION_REVIEW.typeName,
                                                                         2,
                                                                         false,
                                                                         false,
                                                                         supportedZones,
                                                                         effectiveTime,
                                                                         methodName);
            if (annotationReviewEntity != null)
            {
                supplementaryEntities = new ArrayList<>();

                supplementaryEntities.add(annotationReviewEntity);
            }

            List<Relationship> annotationRelationships = this.getAllAttachmentLinks(userId,
                                                                                    annotationEntity.getGUID(),
                                                                                    null,
                                                                                    OpenMetadataType.ANNOTATION.typeName,
                                                                                    null,
                                                                                    null,
                                                                                    SequencingOrder.CREATION_DATE_RECENT,
                                                                                    null,
                                                                                    false,
                                                                                    false,
                                                                                    effectiveTime,
                                                                                    methodName);

            return converter.getNewComplexBean(beanClass, annotationEntity, supplementaryEntities, annotationRelationships, methodName);
        }

        return null;
    }


    /**
     * Return the annotations linked to a specific entity (called the root).
     *
     * @param userId identifier of calling user
     * @param rootGUID identifier of the entity that is connected to the annotations.
     * @param rootGUIDParameterName parameter that passed the identifier of the root entity that the annotations are linked directly to.
     * @param rootGUIDTypeName parameter that passed the type name of the anchor for the annotations.
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    private List<B> getLinkedAnnotations(String            userId,
                                         String            rootGUID,
                                         String            rootGUIDParameterName,
                                         String            rootGUIDTypeName,
                                         String            relationshipTypeGUID,
                                         String            relationshipTypeName,
                                         int               startingFrom,
                                         int               pageSize,
                                         String            methodName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        List<EntityDetail> annotationEntities = this.getAttachedEntities(userId,
                                                                         rootGUID,
                                                                         rootGUIDParameterName,
                                                                         rootGUIDTypeName,
                                                                         relationshipTypeGUID,
                                                                         relationshipTypeName,
                                                                         OpenMetadataType.ANNOTATION.typeName,
                                                                         null,
                                                                         null,
                                                                         0,
                                                                         null,
                                                                         null,
                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                         null,
                                                                         false,
                                                                         false,
                                                                         supportedZones,
                                                                         startingFrom,
                                                                         pageSize,
                                                                         new Date(),
                                                                         methodName);

        if ((annotationEntities != null) && (! annotationEntities.isEmpty()))
        {
            List<B> returnBeans = new ArrayList<>();

            for (EntityDetail annotationEntity : annotationEntities)
            {
                if (annotationEntity != null)
                {
                    B annotationBean = this.getAnnotationBean(userId, annotationEntity, methodName);

                    if (annotationBean != null)
                    {
                        returnBeans.add(annotationBean);
                    }
                }
            }

            return returnBeans;
        }

        return null;
    }


    /**
     * Return the annotations linked to the starting element that have the requested status (located in the relationship).
     *
     * @param userId identifier of calling user
     * @param startingElementGUID identifier of the anchor for the annotations.
     * @param startingElementGUIDParameterName parameter that passed the identifier of the startingElementGUID for the annotations.
     * @param startingElementTypeName parameter that passed the type name of the anchor for the annotations.
     * @param requestedAnnotationStatus limit the results to this annotation status
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotation beans of the requested status
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    private List<B> getLinkedAnnotations(String userId,
                                         String startingElementGUID,
                                         String startingElementGUIDParameterName,
                                         String startingElementTypeName,
                                         String relationshipTypeGUID,
                                         String relationshipTypeName,
                                         int    requestedAnnotationStatus,
                                         int    startingFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(startingElementGUID, startingElementGUIDParameterName, methodName);
        int queryPageSize = invalidParameterHandler.validatePaging(startingFrom, pageSize, methodName);

        Date effectiveTime = new Date();
        RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                       invalidParameterHandler,
                                                                                       userId,
                                                                                       startingElementGUID,
                                                                                       startingElementTypeName,
                                                                                       relationshipTypeGUID,
                                                                                       relationshipTypeName,
                                                                                       2,
                                                                                       null,
                                                                                       null,
                                                                                       SequencingOrder.CREATION_DATE_RECENT,
                                                                                       null,
                                                                                       false,
                                                                                       false,
                                                                                       startingFrom,
                                                                                       queryPageSize,
                                                                                       effectiveTime,
                                                                                       methodName);

        List<B> results = new ArrayList<>();

        while ((iterator.moreToReceive() && ((queryPageSize == 0) || (results.size() < queryPageSize))))
        {
            Relationship relationship = iterator.getNext();

            if ((relationship != null) && (relationship.getProperties() != null))
            {
                int annotationStatusOrdinal = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                                      OpenMetadataProperty.ANNOTATION_STATUS.name,
                                                                                      relationship.getProperties(),
                                                                                      methodName);

                if (requestedAnnotationStatus == annotationStatusOrdinal)
                {
                    EntityProxy entityProxy = repositoryHandler.getOtherEnd(startingElementGUID, startingElementTypeName, relationship, 2, methodName);

                    if (entityProxy != null)
                    {
                        final String entityProxyGUIDParameterName = "entityProxy.getGUID";

                        EntityDetail annotationEntity = repositoryHandler.getEntityByGUID(userId,
                                                                                          entityProxy.getGUID(),
                                                                                          entityProxyGUIDParameterName,
                                                                                          OpenMetadataType.ANNOTATION.typeName,
                                                                                          false,
                                                                                          false,
                                                                                          effectiveTime,
                                                                                          methodName);

                        if ((annotationEntity != null) && (annotationEntity.getGUID() != null))
                        {
                            B annotationBean = this.getAnnotation(userId, entityProxy.getGUID(), methodName);

                            if (annotationBean != null)
                            {
                                results.add(annotationBean);
                            }
                        }
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }

        return results;
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param userId identifier of calling user
     * @param surveyReportGUID identifier of the discovery request.
     * @param startingFrom initial position in the stored list.
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B> getSurveyReportAnnotations(String           userId,
                                              String           surveyReportGUID,
                                              int              startingFrom,
                                              int              pageSize,
                                              String           methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String   reportGUIDParameterName = "surveyReportGUID";

        return this.getLinkedAnnotations(userId,
                                         surveyReportGUID,
                                         reportGUIDParameterName,
                                         OpenMetadataType.SURVEY_REPORT.typeName,
                                         OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeGUID,
                                         OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                         startingFrom,
                                         pageSize,
                                         methodName);
    }


    /**
     * Return the annotations linked direction to the report.
     *
     * @param userId identifier of calling user
     * @param surveyReportGUID identifier of the survey report
     * @param annotationStatus status of the desired annotations - null means all statuses.
     * @param startingFrom initial position in the stored list
     * @param pageSize maximum number of definitions to return on this call.
     * @param methodName calling method
     *
     * @return list of annotations
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B> getSurveyReportAnnotations(String           userId,
                                              String           surveyReportGUID,
                                              int              annotationStatus,
                                              int              startingFrom,
                                              int              pageSize,
                                              String           methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String   reportGUIDParameterName = "surveyReportGUID";

        return this.getLinkedAnnotations(userId,
                                         surveyReportGUID,
                                         reportGUIDParameterName,
                                         OpenMetadataType.SURVEY_REPORT.typeName,
                                         OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeGUID,
                                         OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                         annotationStatus,
                                         startingFrom,
                                         pageSize,
                                         methodName);
    }



    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param startingFrom starting position in the list
     * @param pageSize maximum number of annotations that can be returned.
     * @param methodName calling method
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B>  getExtendedAnnotations(String userId,
                                           String annotationGUID,
                                           int    startingFrom,
                                           int    pageSize,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   annotationGUIDParameter = "annotationGUID";

        return this.getLinkedAnnotations(userId,
                                         annotationGUID,
                                         annotationGUIDParameter,
                                         OpenMetadataType.ANNOTATION.typeName,
                                         OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeGUID,
                                         OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                         startingFrom,
                                         pageSize,
                                         methodName);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId identifier of calling user
     * @param annotationGUID anchor annotation
     * @param annotationStatus status of the desired annotations - null means all statuses.
     * @param startingFrom starting position in the list
     * @param pageSize maximum number of annotations that can be returned.
     * @param methodName calling method
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public List<B>  getExtendedAnnotations(String userId,
                                           String annotationGUID,
                                           int    annotationStatus,
                                           int    startingFrom,
                                           int    pageSize,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String   annotationGUIDParameter = "annotationGUID";

        return this.getLinkedAnnotations(userId,
                                         annotationGUID,
                                         annotationGUIDParameter,
                                         OpenMetadataType.ANNOTATION.typeName,
                                         OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeGUID,
                                         OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                         annotationStatus,
                                         startingFrom,
                                         pageSize,
                                         methodName);
    }
}
