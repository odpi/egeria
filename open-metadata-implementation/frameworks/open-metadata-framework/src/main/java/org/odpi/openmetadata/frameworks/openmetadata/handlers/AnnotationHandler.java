/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AnchorsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createAnnotationFromTemplate(String                                userId,
                                               TemplateOptions                       templateOptions,
                                               String                                templateGUID,
                                               EntityProperties                      replacementProperties,
                                               Map<String, ClassificationProperties> replacementClassifications,
                                               Map<String, String>                   placeholderProperties,
                                               RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * The outcome of adding an annotation to a survey report: the annotation's unique identifier and whether
     * it was created by the call or was an existing annotation that has been reused.
     *
     * @param annotationGUID unique identifier of the annotation
     * @param created was the annotation created by this call
     */
    public record AnnotationResult(String  annotationGUID,
                                   boolean created) { }


    /**
     * Add an annotation to a survey report, reusing a matching annotation from an earlier survey if there is
     * one, so that repeated surveys of a resource that has not changed add no new annotations.
     * <br>
     * Annotations are anchored to the element the survey report describes - the report's own anchor - rather
     * than to a report, because one annotation can be reported by many survey reports.  Each report links the
     * annotations it reported through ReportedAnnotation, and each annotation is linked to the element it
     * describes through AssociatedAnnotation.
     * <br>
     * The candidates for reuse are the annotations linked to the described element through
     * AssociatedAnnotation.  One matches when it is of the same type and all its properties are equal, apart
     * from those that identify the survey that created it - see
     * {@link #annotationMatches(AnnotationProperties, OpenMetadataRootElement)}.  Of several matches the
     * oldest is kept.  The others are left over from before annotations were reused: each was created for,
     * and anchored to, a single survey report.  Their reports are linked to the oldest annotation instead and
     * they are deleted - along with any relationships of their own - so this migration happens once for each
     * annotation.
     *
     * @param userId calling user
     * @param anchorGUID unique identifier of the element the survey report describes - the annotation's anchor
     * @param surveyReportGUID unique identifier of the survey report
     * @param describedElementGUID unique identifier of the element the annotation describes (null for the anchor)
     * @param annotationProperties properties of the annotation
     * @param metadataSourceOptions external source, effective time and lineage options for the requests
     * @param queryOptions options for retrieving the existing annotations
     * @return unique identifier of the annotation - new or reused - and whether it was created
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    public AnnotationResult addAnnotationToReport(String                userId,
                                                  String                anchorGUID,
                                                  String                surveyReportGUID,
                                                  String                describedElementGUID,
                                                  AnnotationProperties  annotationProperties,
                                                  MetadataSourceOptions metadataSourceOptions,
                                                  QueryOptions          queryOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        String elementGUID = describedElementGUID;

        if (elementGUID == null)
        {
            elementGUID = anchorGUID;
        }

        /*
         * Find the existing annotations that match the new one, and the oldest of them.
         */
        List<OpenMetadataRootElement> matchingAnnotations = new ArrayList<>();
        OpenMetadataRootElement       oldestAnnotation    = null;

        List<OpenMetadataRootElement> existingAnnotations = this.getAnnotationsForElement(userId, elementGUID, queryOptions);

        if (existingAnnotations != null)
        {
            for (OpenMetadataRootElement existingAnnotation : existingAnnotations)
            {
                if ((existingAnnotation != null) && (this.annotationMatches(annotationProperties, existingAnnotation)))
                {
                    matchingAnnotations.add(existingAnnotation);

                    if ((oldestAnnotation == null) || (this.getCreateTime(existingAnnotation).before(this.getCreateTime(oldestAnnotation))))
                    {
                        oldestAnnotation = existingAnnotation;
                    }
                }
            }
        }

        if (oldestAnnotation == null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

            newElementOptions.setAnchorGUID(anchorGUID);
            newElementOptions.setIsOwnAnchor(false);

            newElementOptions.setParentGUID(surveyReportGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);

            String annotationGUID = this.createAnnotation(userId, newElementOptions, null, annotationProperties, null);

            if (annotationGUID != null)
            {
                this.linkAnnotationToDescribedElement(userId, elementGUID, annotationGUID, new MakeAnchorOptions(metadataSourceOptions), null);
                this.linkResourceProfileLogs(userId, annotationGUID, annotationProperties, metadataSourceOptions);
            }

            return new AnnotationResult(annotationGUID, true);
        }

        String oldestAnnotationGUID = oldestAnnotation.getElementHeader().getGUID();

        /*
         * The oldest annotation is anchored to the report's subject and linked to this report.  An annotation
         * from before annotations were reused is anchored to the report that created it.
         */
        this.anchorAnnotation(userId, oldestAnnotation, anchorGUID, metadataSourceOptions);

        Set<String> oldestAnnotationReportGUIDs = this.getSurveyReportGUIDs(oldestAnnotation);

        if (! oldestAnnotationReportGUIDs.contains(surveyReportGUID))
        {
            this.attachAnnotationToReport(userId, surveyReportGUID, oldestAnnotationGUID, new MakeAnchorOptions(metadataSourceOptions), null);
            oldestAnnotationReportGUIDs.add(surveyReportGUID);
        }

        /*
         * Every other match is a duplicate.  Its reports are linked to the oldest annotation, and it is deleted.
         */
        for (OpenMetadataRootElement duplicateAnnotation : matchingAnnotations)
        {
            String duplicateAnnotationGUID = duplicateAnnotation.getElementHeader().getGUID();

            if (! oldestAnnotationGUID.equals(duplicateAnnotationGUID))
            {
                for (String duplicateReportGUID : this.getSurveyReportGUIDs(duplicateAnnotation))
                {
                    if (! oldestAnnotationReportGUIDs.contains(duplicateReportGUID))
                    {
                        this.attachAnnotationToReport(userId, duplicateReportGUID, oldestAnnotationGUID, new MakeAnchorOptions(metadataSourceOptions), null);
                        oldestAnnotationReportGUIDs.add(duplicateReportGUID);
                    }
                }

                this.deleteAnnotation(userId, duplicateAnnotationGUID, new DeleteOptions(metadataSourceOptions));
            }
        }

        this.linkResourceProfileLogs(userId, oldestAnnotationGUID, annotationProperties, metadataSourceOptions);

        return new AnnotationResult(oldestAnnotationGUID, false);
    }


    /**
     * Link a resource profile log annotation to the log files that hold its detail (ResourceProfileData).
     * A survey writes a new log file each time it runs and names it in resourceProfileLogGUIDs.  That property
     * is carried by these relationships rather than stored with the annotation, so without them the log files
     * are only reachable from the survey report.  When an annotation is reused, the new survey's log file is
     * linked alongside the earlier ones, so the annotation keeps a log from each survey that reported it.
     *
     * @param userId calling user
     * @param annotationGUID unique identifier of the annotation
     * @param annotationProperties properties supplied by the survey
     * @param metadataSourceOptions external source, effective time and lineage options for the requests
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    private void linkResourceProfileLogs(String                userId,
                                         String                annotationGUID,
                                         AnnotationProperties  annotationProperties,
                                         MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        if ((annotationGUID != null) &&
                (annotationProperties instanceof ResourceProfileLogAnnotationProperties resourceProfileLogAnnotationProperties) &&
                (resourceProfileLogAnnotationProperties.getResourceProfileLogGUIDs() != null))
        {
            for (String resourceProfileLogGUID : resourceProfileLogAnnotationProperties.getResourceProfileLogGUIDs())
            {
                if (resourceProfileLogGUID != null)
                {
                    this.linkResourceProfileData(userId,
                                                 annotationGUID,
                                                 resourceProfileLogGUID,
                                                 new MakeAnchorOptions(metadataSourceOptions),
                                                 null);
                }
            }
        }
    }


    /**
     * Determine whether an existing annotation records the same thing as a new one.  It must be of the same
     * type, and all of its properties must be equal apart from those that identify the survey that created
     * it: the qualified name, display name and description, which record when and in which survey it was
     * first discovered.  A resource profile log annotation's log file GUIDs are not compared either: each survey
     * writes a new log file, and the GUIDs are carried by ResourceProfileData relationships rather than stored
     * with the annotation, so an annotation read back never has them.  The effective dates and
     * extended properties are not compared either, because an annotation read back from the repository carries
     * them differently from one that has just been built.
     *
     * @param annotationProperties properties of the new annotation
     * @param existingAnnotation existing annotation
     * @return true if they match
     */
    private boolean annotationMatches(AnnotationProperties    annotationProperties,
                                      OpenMetadataRootElement existingAnnotation)
    {
        if ((annotationProperties != null) &&
            (existingAnnotation.getElementHeader() != null) &&
            (existingAnnotation.getProperties() instanceof AnnotationProperties existingProperties) &&
            (existingProperties.getClass() == annotationProperties.getClass()))
        {
            existingProperties.setQualifiedName(annotationProperties.getQualifiedName());
            existingProperties.setDisplayName(annotationProperties.getDisplayName());
            existingProperties.setDescription(annotationProperties.getDescription());
            existingProperties.setEffectiveFrom(annotationProperties.getEffectiveFrom());
            existingProperties.setEffectiveTo(annotationProperties.getEffectiveTo());
            existingProperties.setExtendedProperties(annotationProperties.getExtendedProperties());

            if ((existingProperties instanceof ResourceProfileLogAnnotationProperties existingLogProperties) &&
                    (annotationProperties instanceof ResourceProfileLogAnnotationProperties newLogProperties))
            {
                existingLogProperties.setResourceProfileLogGUIDs(newLogProperties.getResourceProfileLogGUIDs());
            }

            return annotationProperties.equals(existingProperties);
        }

        return false;
    }


    /**
     * Return the time an annotation was created, so the oldest of several can be chosen.
     *
     * @param annotation annotation
     * @return creation time (the epoch if it is not known, so an annotation with no time is treated as the oldest)
     */
    private Date getCreateTime(OpenMetadataRootElement annotation)
    {
        if ((annotation.getElementHeader().getVersions() != null) && (annotation.getElementHeader().getVersions().getCreateTime() != null))
        {
            return annotation.getElementHeader().getVersions().getCreateTime();
        }

        return new Date(0L);
    }


    /**
     * Return the unique identifiers of the survey reports that an annotation is linked to.
     *
     * @param annotation annotation
     * @return set of survey report GUIDs (may be empty)
     */
    private Set<String> getSurveyReportGUIDs(OpenMetadataRootElement annotation)
    {
        Set<String> surveyReportGUIDs = new HashSet<>();

        if (annotation.getFromSurveyReports() != null)
        {
            for (RelatedMetadataElementSummary surveyReport : annotation.getFromSurveyReports())
            {
                if ((surveyReport != null) && (surveyReport.getRelatedElement() != null) && (surveyReport.getRelatedElement().getElementHeader() != null))
                {
                    surveyReportGUIDs.add(surveyReport.getRelatedElement().getElementHeader().getGUID());
                }
            }
        }

        return surveyReportGUIDs;
    }


    /**
     * Make sure an existing annotation is anchored to the supplied anchor.  An annotation created before
     * annotations were reused is anchored to the survey report that created it.
     *
     * @param userId calling user
     * @param annotation annotation
     * @param anchorGUID unique identifier of the anchor
     * @param metadataSourceOptions external source, effective time and lineage options for the requests
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem with the metadata store
     */
    private void anchorAnnotation(String                  userId,
                                  OpenMetadataRootElement annotation,
                                  String                  anchorGUID,
                                  MetadataSourceOptions   metadataSourceOptions) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        if ((annotation.getElementHeader().getAnchor() != null) &&
            (annotation.getElementHeader().getAnchor().getClassificationProperties() instanceof AnchorsProperties currentAnchor) &&
            (anchorGUID.equals(currentAnchor.getAnchorGUID())))
        {
            return;
        }

        OpenMetadataElement anchor = openMetadataClient.getMetadataElementByGUID(userId, anchorGUID, new GetOptions(metadataSourceOptions));

        AnchorsProperties anchorsProperties = new AnchorsProperties();

        anchorsProperties.setAnchorGUID(anchorGUID);

        if ((anchor != null) && (anchor.getType() != null))
        {
            anchorsProperties.setAnchorTypeName(anchor.getType().getTypeName());
            anchorsProperties.setAnchorDomainName(this.getAnchorDomainName(anchor.getType()));
        }

        UpdateOptions updateOptions = new UpdateOptions(metadataSourceOptions);

        updateOptions.setMergeUpdate(true);

        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            annotation.getElementHeader().getGUID(),
                                                            OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                            updateOptions,
                                                            new OpenMetadataClassificationBuilder().getElementProperties(anchorsProperties));
    }


    /**
     * Return the domain of an anchor's type: the most general type below Referenceable and OpenMetadataRoot,
     * for example Asset for a DataSet.
     *
     * @param anchorType type of the anchor
     * @return domain name
     */
    private String getAnchorDomainName(ElementType anchorType)
    {
        String domainName = anchorType.getTypeName();

        if (anchorType.getSuperTypeNames() != null)
        {
            for (String superTypeName : anchorType.getSuperTypeNames())
            {
                if ((! OpenMetadataType.OPEN_METADATA_ROOT.typeName.equals(superTypeName)) &&
                    (! OpenMetadataType.REFERENCEABLE.typeName.equals(superTypeName)))
                {
                    domainName = superTypeName;
                }
            }
        }

        return domainName;
    }


    /**
     * Update the properties of an annotation.
     *
     * @param userId                 userId of the user making the request.
     * @param annotationGUID       unique identifier of the annotation (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param surveyReportGUID       unique identifier of the report
     * @param newAnnotationGUID           unique identifier of the  annotation
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request.
     * @param surveyReportGUID       unique identifier of the report
     * @param annotationGUID           unique identifier of the annotation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request.
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request.
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * Attach a resource profile log annotation to an asset where the profile data is stored.
     *
     * @param userId                 userId of the user making the request
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request.
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * Attach an annotation to an element that has been matched with the subject of the survey.
     *
     * @param userId                 userId of the user making the request
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element such as data class, data grain, and glossary term
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAnnotationMatch(String                   userId,
                                    String                   annotationGUID,
                                    String                   elementGUID,
                                    MakeAnchorOptions        makeAnchorOptions,
                                    AnnotationMatchProperties relationshipProperties) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName            = "linkAnnotationMatch";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ANNOTATION_MATCH_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        elementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove an AnnotationMatch relationship.
     *
     * @param userId                 userId of the user making the request.
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element such as data class, data grain, and glossary term
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAnnotationMatch(String        userId,
                                      String        annotationGUID,
                                      String        elementGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        final String methodName = "detachAnnotationMatch";
        final String end1GUIDParameterName = "annotationGUID";
        final String end2GUIDParameterName = "elementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(annotationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(elementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ANNOTATION_MATCH_RELATIONSHIP.typeName,
                                                        annotationGUID,
                                                        elementGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a request for action annotation to the element that needs attention.
     *
     * @param userId                 userId of the user making the request
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request.
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request.
     * @param annotationGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param surveyReportGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * Return the number of annotations reported by a survey report (ReportedAnnotation relationship).  The
     * repository counts the relationships itself, so this is far cheaper than retrieving the annotations when
     * only the number is wanted.
     *
     * @param userId                 userId of the user making the request
     * @param surveyReportGUID       unique identifier of the survey report
     * @param queryOptions           multiple options to control the query
     * @return number of annotations
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public long countReportedAnnotations(String       userId,
                                         String       surveyReportGUID,
                                         QueryOptions queryOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "countReportedAnnotations";
        final String guidPropertyName = "surveyReportGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(surveyReportGUID, guidPropertyName, methodName);

        return openMetadataClient.countRelationshipsBetweenMetadataElements(userId,
                                                                            OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                            null,
                                                                            Collections.singletonList(surveyReportGUID),
                                                                            null,
                                                                            null,
                                                                            OpenMetadataType.ANNOTATION.typeName,
                                                                            EndMatchCriteria.BOTH,
                                                                            null,
                                                                            new QueryOptions(queryOptions));
    }


    /**
     * Returns the survey reports that describe the supplied element (ReportSubject relationship), most recently
     * created first, so a caller interested in the latest surveys can retrieve a small first page rather than
     * every report the element has accumulated.
     *
     * @param userId                 userId of the user making the request
     * @param elementGUID            unique identifier of the element that the reports describe
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSurveyReportsForElement(String       userId,
                                                                    String       elementGUID,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "getSurveyReportsForElement";
        final String guidPropertyName = "elementGUID";

        QueryOptions workingQueryOptions = new QueryOptions(queryOptions);

        workingQueryOptions.setMetadataElementTypeName(OpenMetadataType.SURVEY_REPORT.typeName);
        workingQueryOptions.setSequencingOrder(SequencingOrder.CREATION_DATE_RECENT);
        workingQueryOptions.setSequencingProperty(null);

        return super.getRelatedRootElements(userId,
                                            elementGUID,
                                            guidPropertyName,
                                            1,
                                            OpenMetadataType.REPORT_SUBJECT_RELATIONSHIP.typeName,
                                            OpenMetadataType.SURVEY_REPORT.typeName,
                                            workingQueryOptions,
                                            methodName);
    }


    /**
     * Returns the list of annotations that describe the supplied element (AssociatedAnnotation relationship).
     *
     * @param userId                 userId of the user making the request
     * @param elementGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param annotationGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param annotationGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request
     * @param annotationGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
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
