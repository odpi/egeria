/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * AnnotationHandler provides methods to define all types of annotations and their relationships
 */
public class AnnotationHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public AnnotationHandler(String             localServerName,
                             AuditLog           auditLog,
                             String             localServiceName,
                             OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.ANNOTATION.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public AnnotationHandler(AnnotationHandler template,
                             String            specificTypeName)
    {
        super(template, specificTypeName);
    }


    /**
     * Create a new annotation.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createAnnotation(String                                userId,
                                   NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   AnnotationProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "createAnnotation";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an annotation using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new annotation.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing annotation to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAnnotationFromTemplate(String                 userId,
                                               TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               ElementProperties      replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of an annotation.
     *
     * @param userId                 userId of user making request.
     * @param annotationGUID       unique identifier of the annotation (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateAnnotation(String               userId,
                                    String               annotationGUID,
                                    UpdateOptions        updateOptions,
                                    AnnotationProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName        = "updateAnnotation";
        final String guidParameterName = "annotationGUID";

        return super.updateElement(userId,
                                   annotationGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Create a relationship that links a new annotation to its survey report.  This relationship is typically
     * established during the createAnnotation as the parent relationship.  It is included for completeness.
     *
     * @param userId                 userId of user making request
     * @param surveyReportGUID       unique identifier of the report
     * @param newAnnotationGUID           unique identifier of the  annotation
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachAnnotationToReport(String                       userId,
                                         String                       surveyReportGUID,
                                         String                       newAnnotationGUID,
                                         MakeAnchorOptions            makeAnchorOptions,
                                         ReportedAnnotationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName            = "attachAnnotationToReport";
        final String end1GUIDParameterName = "surveyReportGUID";
        final String end2GUIDParameterName = "newAnnotationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(surveyReportGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(newAnnotationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                        surveyReportGUID,
                                                        newAnnotationGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an annotation from its report (ReportedAnnotation relationship).
     *
     * @param userId                 userId of user making request.
     * @param surveyReportGUID       unique identifier of the report
     * @param annotationGUID           unique identifier of the annotation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAnnotationFromReport(String        userId,
                                           String        surveyReportGUID,
                                           String        annotationGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachAnnotationFromReport";

        final String end1GUIDParameterName = "surveyReportGUID";
        final String end2GUIDParameterName = "annotationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(surveyReportGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(annotationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                        surveyReportGUID,
                                                        annotationGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an annotation to the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param userId                 userId of user making request
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAnnotationToDescribedElement(String                         userId,
                                                 String                         elementGUID,
                                                 String                         annotationGUID,
                                                 MakeAnchorOptions              makeAnchorOptions,
                                                 AssociatedAnnotationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName            = "linkAnnotationToDescribedElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "annotationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(annotationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        annotationGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an annotation from the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param userId                 userId of user making request.
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAnnotationFromDescribedElement(String        userId,
                                                     String        elementGUID,
                                                     String        annotationGUID,
                                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachDataSetContent";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "annotationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(annotationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        annotationGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an annotation to the equivalent annotation from the previous run of the survey.
     *
     * @param userId                 userId of user making request
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAnnotationToItsPredecessor(String                        userId,
                                               String                        previousAnnotationGUID,
                                               String                        newAnnotationGUID,
                                               MakeAnchorOptions             metadataSourceOptions,
                                               AnnotationExtensionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "linkAnnotationToItsPredecessor";
        final String end1GUIDParameterName = "previousAnnotationGUID";
        final String end2GUIDParameterName = "newAnnotationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(previousAnnotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(newAnnotationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                                        previousAnnotationGUID,
                                                        newAnnotationGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an annotation from an annotation from the previous run of the survey.
     *
     * @param userId                 userId of user making request.
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAnnotationFromItsPredecessor(String        userId,
                                                   String        previousAnnotationGUID,
                                                   String        newAnnotationGUID,
                                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "detachAnnotationFromItsPredecessor";
        final String end1GUIDParameterName = "previousAnnotationGUID";
        final String end2GUIDParameterName = "newAnnotationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(previousAnnotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(newAnnotationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                                        previousAnnotationGUID,
                                                        newAnnotationGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a schema analysis annotation to a matching schema type.
     *
     * @param userId                 userId of user making request
     * @param annotationGUID       unique identifier of the annotation
     * @param schemaTypeGUID            unique identifier of the schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDiscoveredSchemaType(String                         userId,
                                         String                         annotationGUID,
                                         String                         schemaTypeGUID,
                                         MakeAnchorOptions              makeAnchorOptions,
                                         DiscoveredSchemaTypeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName            = "linkDiscoveredSchemaType";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DISCOVERED_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        schemaTypeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a schema analysis annotation from a matching schema type.
     *
     * @param userId                 userId of user making request.
     * @param annotationGUID              unique identifier of the parent process
     * @param schemaTypeGUID          unique identifier of the child process
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDiscoveredSchemaType(String        userId,
                                           String        annotationGUID,
                                           String        schemaTypeGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachDiscoveredSchemaType";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DISCOVERED_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        schemaTypeGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a resource profile log annotation to an asset where the profile data is stored.
     *
     * @param userId                 userId of user making request
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkResourceProfileData(String                        userId,
                                        String                        annotationGUID,
                                        String                        assetGUID,
                                        MakeAnchorOptions             makeAnchorOptions,
                                        ResourceProfileDataProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName            = "linkResourceProfileData";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(assetGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        assetGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a resource profile log annotation from an asset where the profile data is stored.
     *
     * @param userId                 userId of user making request.
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachResourceProfileData(String        userId,
                                          String        annotationGUID,
                                          String        assetGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "detachResourceProfileData";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(assetGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        assetGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a data class annotation to a data class.
     *
     * @param userId                 userId of user making request
     * @param annotationGUID               unique identifier of the annotation
     * @param dataClassGUID         unique identifier of the associated data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataClassMatch(String                   userId,
                                   String                   annotationGUID,
                                   String                   dataClassGUID,
                                   MakeAnchorOptions        makeAnchorOptions,
                                   DataClassMatchProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkDataClassMatch";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "dataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_MATCH_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        dataClassGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data class annotation from a data class.
     *
     * @param userId                 userId of user making request.
     * @param annotationGUID               unique identifier of the annotation
     * @param dataClassGUID         unique identifier of the associated data class
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataClassMatch(String        userId,
                                     String        annotationGUID,
                                     String        dataClassGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachDataClassMatch";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "dataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_MATCH_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        dataClassGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a request for action annotation to the element that needs attention.
     *
     * @param userId                 userId of user making request
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRequestForActionTarget(String                           userId,
                                           String                           annotationGUID,
                                           String                           elementGUID,
                                           MakeAnchorOptions                makeAnchorOptions,
                                           RequestForActionTargetProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkRequestForActionTarget";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.REQUEST_FOR_ACTION_TARGET_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        elementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a request for action annotation from its intended target element.
     *
     * @param userId                 userId of user making request.
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRequestForActionTarget(String        userId,
                                             String        annotationGUID,
                                             String        elementGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachRequestForActionTarget";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.REQUEST_FOR_ACTION_TARGET_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a annotation.
     *
     * @param userId                 userId of user making request.
     * @param annotationGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteAnnotation(String        userId,
                                 String        annotationGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName        = "deleteAnnotation";
        final String guidParameterName = "annotationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, annotationGUID, deleteOptions);
    }


    /**
     * Returns the list of annotations with a particular annotation type, or summary, or expression, or analysis step.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsByName(String       userId,
                                                              String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getAnnotationsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                   OpenMetadataProperty.SUMMARY.name,
                                                   OpenMetadataProperty.EXPRESSION.name,
                                                   OpenMetadataProperty.ANALYSIS_STEP.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of annotations associated with a particular analysis step.
     *
     * @param userId                 userId of user making request
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsByAnalysisStep(String       userId,
                                                                      String       name,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getAnnotationsByAnalysisStep";

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.ANALYSIS_STEP.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Returns the list of annotations with a particular annotation type property.
     *
     * @param userId                 userId of user making request
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsByAnnotationType(String       userId,
                                                                        String       name,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getAnnotationsByAnnotationType";

        List<String> propertyNames = Collections.singletonList(OpenMetadataProperty.ANNOTATION_TYPE.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }



    /**
     * Returns the annotations created under the supplied survey report.
     *
     * @param userId                 userId of user making request
     * @param surveyReportGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNewAnnotations(String       userId,
                                                           String       surveyReportGUID,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "getNewAnnotations";
        final String guidPropertyName = "surveyReportGUID";

        return super.getRelatedRootElements(userId,
                                            surveyReportGUID,
                                            guidPropertyName,
                                            1,
                                            OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                            OpenMetadataType.ANNOTATION.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of annotations that describe the supplied element (AssociatedAnnotation relationship).
     *
     * @param userId                 userId of user making request
     * @param elementGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsForElement(String       userId,
                                                                  String       elementGUID,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getAnnotationsForElement";
        final String guidPropertyName = "elementGUID";

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidPropertyName,
                                            1,
                                            OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                            OpenMetadataType.ANNOTATION.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of annotations that extend the supplied annotation (AnnotationExtension relationship).
     *
     * @param userId                 userId of user making request
     * @param annotationGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationExtensions(String       userId,
                                                                 String       annotationGUID,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getAnnotationExtensions";
        final String guidPropertyName = "annotationGUID";

        return super.getRelatedRootElements(userId,
                                            annotationGUID,
                                            guidPropertyName,
                                            1,
                                            OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                            OpenMetadataType.ANNOTATION.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of annotations that are extended by the supplied annotation (AnnotationExtension relationship).
     *
     * @param userId                 userId of user making request
     * @param annotationGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getPreviousAnnotations(String       userId,
                                                                String       annotationGUID,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getPreviousAnnotations";
        final String guidPropertyName = "annotationGUID";

        return super.getRelatedRootElements(userId,
                                            annotationGUID,
                                            guidPropertyName,
                                            2,
                                            OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                            OpenMetadataType.ANNOTATION.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Return the properties of a specific annotation.
     *
     * @param userId                 userId of user making request
     * @param annotationGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getAnnotationByGUID(String     userId,
                                                       String     annotationGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "getAnnotationByGUID";

        return super.getRootElementByGUID(userId, annotationGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of annotations metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findAnnotations(String        userId,
                                                         String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "findAnnotations";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
