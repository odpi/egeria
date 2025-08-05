/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction;

import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.AnnotationStatus;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.converters.OpenMetadataConverterBase;
import org.odpi.openmetadata.frameworks.surveyaction.properties.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The AnnotationStore provides the interface used by a survey action service to
 * store annotations in the open metadata repositories.
 */
public class AnnotationStore
{
    private final OpenMetadataClient openMetadataStore;
    private final String             userId;
    private final String             assetGUID;
    private final String             externalSourceGUID;
    private final String             externalSourceName;
    private       boolean            forLineage              = false;
    private       boolean            forDuplicateProcessing  = false;
    private       boolean            useCurrentEffectiveTime = false;

    private String surveyReportGUID;
    private String reportQualifiedName;
    private       String reportDisplayName;
    private       String surveyDescription;
    private       String surveyPurpose;
    private       String analysisStep        = null;

    private static final PropertyHelper propertyHelper = new PropertyHelper();

    private final AnnotationConverter<Annotation> converter;

    /**
     * Constructor sets up the key parameters for accessing the annotations store and creates
     * a survey report.  This constructor is used by the engine service running a survey action service.
     *
     * @param userId calling user
     * @param assetGUID unique of the asset that describes the resource being surveyed
     * @param openMetadataStore access to the open metadata repositories
     * @param externalSourceGUID unique identifier of the external source that is supplying the survey data
     * @param externalSourceName unique name of the external source that is supplying the survey data
     * @param reportQualifiedName qualified name of report
     * @param reportDisplayName display name of report
     * @param surveyDescription description of report
     * @param surveyPurpose purpose of the survey
     * @param engineActionGUID unique identifier of the engine action that started this report
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public AnnotationStore(String             userId,
                           String             assetGUID,
                           OpenMetadataClient openMetadataStore,
                           String             externalSourceGUID,
                           String             externalSourceName,
                           String             reportQualifiedName,
                           String             reportDisplayName,
                           String             surveyDescription,
                           String             surveyPurpose,
                           String             engineActionGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        this.userId              = userId;
        this.assetGUID           = assetGUID;
        this.openMetadataStore   = openMetadataStore;
        this.externalSourceGUID  = externalSourceGUID;
        this.externalSourceName  = externalSourceName;
        this.reportQualifiedName = reportQualifiedName;
        this.reportDisplayName   = reportDisplayName;
        this.surveyDescription   = surveyDescription;
        this.surveyPurpose       = surveyPurpose;

        this.converter = new AnnotationConverter<>("Survey Action Framework (SAF)",
                                                   openMetadataStore.getServerName());

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        reportQualifiedName);
        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                      reportDisplayName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                      surveyDescription);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.PURPOSE.name,
                                                      surveyPurpose);

        Date startDate = new Date();
        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.START_DATE.name,
                                                    startDate);

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setExternalSourceGUID(externalSourceGUID);
        newElementOptions.setExternalSourceName(externalSourceName);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(assetGUID);
        newElementOptions.setIsOwnAnchor(false);

        newElementOptions.setParentGUID(assetGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName);


        this.surveyReportGUID = openMetadataStore.createMetadataElementInStore(userId,
                                                                               OpenMetadataType.SURVEY_REPORT.typeName,
                                                                               newElementOptions,
                                                                               null,
                                                                               new NewElementProperties(properties),
                                                                               null);

        if ((surveyReportGUID != null) && (engineActionGUID != null))
        {
            openMetadataStore.createRelatedElementsInStore(userId,
                                                           OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeName,
                                                           engineActionGUID,
                                                           surveyReportGUID,
                                                           newElementOptions,
                                                           null);
        }
    }


    /**
     * Allow a subclass to override the survey report GUID.
     *
     * @param surveyReportGUID unique identifier of the survey report.
     */
    protected void setSurveyReportGUID(String surveyReportGUID)
    {
        this.surveyReportGUID = surveyReportGUID;
    }


    /*
     * Methods to control how methods to the open metadata store are called.
     */

    /**
     * Return the forLineage setting.
     *
     * @return boolean
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up the forLineage setting.
     *
     * @param forLineage boolean
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /**
     * Return the forDuplicateProcessing setting.
     *
     * @return boolean
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up the forDuplicateProcessing setting.
     *
     * @param forDuplicateProcessing boolean
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /**
     * Return the boolean setting for whether the current time for requests or null.
     *
     * @return boolean
     */
    public boolean isUseCurrentEffectiveTime()
    {
        return useCurrentEffectiveTime;
    }


    /**
     * Set up the boolean setting for whether the current time for requests or null.
     *
     * @param useCurrentEffectiveTime boolean
     */
    public void setUseCurrentEffectiveTime(boolean useCurrentEffectiveTime)
    {
        this.useCurrentEffectiveTime = useCurrentEffectiveTime;
    }


    /**
     * Return the appropriate effectiveTime for a request.
     *
     * @return date
     */
    private Date getEffectiveTime()
    {
        if (useCurrentEffectiveTime)
        {
            return new Date();
        }

        return null;
    }


    /*
     * Managing the survey report
     */

    /**
     * Return the report identifier for this survey context.  Any new annotations added to this survey context
     * will be linked to this report.
     *
     * @return unique identifier (guid) of the new survey report.
     */
    public String getSurveyReportGUID()
    {
        return surveyReportGUID;
    }


    /**
     * Return the locally defined analysis step.  This value is used in annotations generated in this phase.
     *
     * @return name of analysis step
     */
    public String getAnalysisStep()
    {
        return analysisStep;
    }


    /**
     * Set up the name of the current analysis step.
     *
     * @param analysisStep name
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the survey report
     */
    public void setAnalysisStep(String analysisStep) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        this.analysisStep = analysisStep;

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.ANALYSIS_STEP.name,
                                                                        analysisStep);

        UpdateOptions updateOptions = new UpdateOptions();

        updateOptions.setExternalSourceGUID(externalSourceGUID);
        updateOptions.setExternalSourceName(externalSourceName);
        updateOptions.setMergePropertyUpdate(true);

        openMetadataStore.updateMetadataElementInStore(userId,
                                                       surveyReportGUID,
                                                       updateOptions,
                                                       properties);
    }


    /**
     * Return the unique name of the survey report that will result from this survey request.
     *
     * @return String report name
     */
    public String getReportQualifiedName()
    {
        return reportQualifiedName;
    }


    /**
     * Set up the unique name of the survey report that will result from this survey request.
     * The survey action engine will set up a default fully-qualified name.  This method enables it to be overridden.
     *
     * @param reportName  String report name
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the survey report
     */
    public void setReportQualifiedName(String reportName) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        this.reportQualifiedName = reportName;

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        reportQualifiedName);
        UpdateOptions updateOptions = new UpdateOptions();

        updateOptions.setExternalSourceGUID(externalSourceGUID);
        updateOptions.setExternalSourceName(externalSourceName);
        updateOptions.setMergePropertyUpdate(true);

        openMetadataStore.updateMetadataElementInStore(userId,
                                                       surveyReportGUID,
                                                       updateOptions,
                                                       properties);
    }


    /**
     * Return the display name of the survey report that will result from this survey request.
     *
     * @return String report name
     */
    public String getReportDisplayName()
    {
        return reportDisplayName;
    }


    /**
     * Set up the display name of the survey report that will result from this survey request.
     * The default name is null.
     *
     * @param reportName  String report name
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the survey report
     */
    public void setReportDisplayName(String reportName) throws InvalidParameterException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException
    {
        this.reportDisplayName = reportName;

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.DISPLAY_NAME.name,
                                                                        reportDisplayName);
        UpdateOptions updateOptions = new UpdateOptions();

        updateOptions.setExternalSourceGUID(externalSourceGUID);
        updateOptions.setExternalSourceName(externalSourceName);
        updateOptions.setMergePropertyUpdate(true);

        openMetadataStore.updateMetadataElementInStore(userId,
                                                       surveyReportGUID,
                                                       updateOptions,
                                                       properties);
    }


    /**
     * Return the description for the survey report that will result from this survey request.
     * The default value is null.
     *
     * @return String report description
     */
    public String getSurveyDescription()
    {
        return surveyDescription;
    }


    /**
     * Set up the description for the survey report that will result from this survey request.
     *
     * @param surveyDescription String report description
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the survey report
     */
    public void setSurveyDescription(String surveyDescription) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        this.surveyDescription = surveyDescription;

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.DESCRIPTION.name,
                                                                        surveyDescription);
        UpdateOptions updateOptions = new UpdateOptions();

        updateOptions.setExternalSourceGUID(externalSourceGUID);
        updateOptions.setExternalSourceName(externalSourceName);
        updateOptions.setMergePropertyUpdate(true);

        openMetadataStore.updateMetadataElementInStore(userId,
                                                       surveyReportGUID,
                                                       updateOptions,
                                                       properties);
    }


    /**
     * Return the purpose of the survey.
     *
     * @return string
     */
    public String getSurveyPurpose()
    {
        return surveyPurpose;
    }


    /**
     * Set up the purpose of the survey.
     *
     * @param surveyPurpose string
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the survey report
     */
    public void setSurveyPurpose(String surveyPurpose) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        this.surveyPurpose = surveyPurpose;

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.PURPOSE.name,
                                                                        surveyDescription);
        UpdateOptions updateOptions = new UpdateOptions();

        updateOptions.setExternalSourceGUID(externalSourceGUID);
        updateOptions.setExternalSourceName(externalSourceName);
        updateOptions.setMergePropertyUpdate(true);

        openMetadataStore.updateMetadataElementInStore(userId,
                                                       surveyReportGUID,
                                                       updateOptions,
                                                       properties);
    }


    /**
     * Set up the completion message,
     *
     * @param completionMessage new completion message
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the survey report
     */
    public void setCompletionMessage(String completionMessage) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.COMPLETION_MESSAGE.name,
                                                                        completionMessage);
        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.COMPLETION_TIME.name,
                                                    new Date());

        UpdateOptions updateOptions = new UpdateOptions();

        updateOptions.setExternalSourceGUID(externalSourceGUID);
        updateOptions.setExternalSourceName(externalSourceName);
        updateOptions.setMergePropertyUpdate(true);

        openMetadataStore.updateMetadataElementInStore(userId,
                                                       surveyReportGUID,
                                                       updateOptions,
                                                       properties);
    }


    /*
     * Managing the annotations for the survey report.
     */

    /**
     * Return the list of Annotations created for the element by previous runs of the survey action service.
     * This method follows the AssociatedAnnotation relationship from the supplied element to the
     *
     * @param elementGUID unique identifier of the element to query
     * @param startFrom starting position in the list.
     * @param pageSize maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public List<Annotation>  getAnnotationsForElement(String    elementGUID,
                                                      int       startFrom,
                                                      int       pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "getAnnotationsForElement";

        RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(userId,
                                                                                                          elementGUID,
                                                                                                          1,
                                                                                                          OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                          getQueryOptions(startFrom, pageSize));

        return this.getRelatedAnnotationBeans(relatedMetadataElements, methodName);
    }


    /**
     * Return the current list of annotations created by this survey run.
     * This method is used by survey pipeline steps to pick up the annotations
     *
     * @param startFrom starting position in the list.
     * @param pageSize maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving annotations from the annotation store.
     */
    public List<Annotation>  getNewAnnotations(int startFrom,
                                               int pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String methodName = "getNewAnnotations";

        RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(userId,
                                                                                                            surveyReportGUID,
                                                                                                            1,
                                                                                                            OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName,
                                                                                                            getQueryOptions(startFrom, pageSize));

        return this.getRelatedAnnotationBeans(relatedMetadataElements, methodName);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param annotationGUID parent annotation
     * @param startFrom starting position in the list
     * @param pageSize maximum number of annotations that can be returned.
     *
     * @return list of Annotation objects
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem that occurred within the property server.
     */
    public  List<Annotation>  getExtendedAnnotations(String   annotationGUID,
                                                     int      startFrom,
                                                     int      pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getExtendedAnnotations";

        RelatedMetadataElementList relatedMetadataElements = openMetadataStore.getRelatedMetadataElements(userId,
                                                                                                            annotationGUID,
                                                                                                            1,
                                                                                                            OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                                                                                            getQueryOptions(startFrom, pageSize));

        return this.getRelatedAnnotationBeans(relatedMetadataElements, methodName);
    }


    /**
     * Return a specific annotation stored in the annotation store (previous or new).
     *
     * @param annotationGUID unique identifier of the annotation
     * @return annotation object
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem retrieving the annotation from the annotation store.
     */
    public Annotation  getAnnotation(String    annotationGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "getAnnotation";

        OpenMetadataElement openMetadataElement = openMetadataStore.getMetadataElementByGUID(userId,
                                                                                             annotationGUID,
                                                                                             getGetOptions());
        return getAnnotationBean(openMetadataElement, methodName);
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param annotation annotation object
     * @param associatedElementGUID unique identifier of the element to associate this annotation with
     *                              (it is associated with the survey report automatically).
     *                              This value may be null to indicate that it is only to be linked to the report.
     * @return unique identifier of new annotation
     * @throws InvalidParameterException the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem adding the annotation to the annotation store.
     */
    public String  addAnnotation(Annotation annotation,
                                 String     associatedElementGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        AnnotationBuilder builder = this.getAnnotationBuilder(annotation);

        if (builder != null)
        {
            NewElementOptions newElementOptions = new NewElementOptions(this.getMetadataSourceOptions());

            newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
            newElementOptions.setAnchorGUID(assetGUID);
            newElementOptions.setIsOwnAnchor(false);

            newElementOptions.setParentGUID(surveyReportGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);

            String typeName = OpenMetadataType.ANNOTATION.typeName;

            if (annotation.getOpenMetadataTypeName() != null)
            {
                typeName = annotation.getOpenMetadataTypeName();
            }

            String annotationGUID = openMetadataStore.createMetadataElementInStore(userId,
                                                                                   typeName,
                                                                                   newElementOptions,
                                                                                   null,
                                                                                   new NewElementProperties(builder.getElementProperties()),
                                                                                   null);

            if (annotationGUID != null)
            {
                if (builder.getDataProfileDataGUIDs() != null)
                {
                    for (String dataProfileDataGUID : builder.getDataProfileDataGUIDs())
                    {
                        if (dataProfileDataGUID != null)
                        {
                            openMetadataStore.createRelatedElementsInStore(userId,
                                                                           OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName,
                                                                           annotationGUID,
                                                                           dataProfileDataGUID,
                                                                           newElementOptions,
                                                                           null);
                        }
                    }
                }

                if (builder.getRequestForActionTargetGUIDs() != null)
                {
                    for (String requestForActionTargetGUID : builder.getRequestForActionTargetGUIDs())
                    {
                        if (requestForActionTargetGUID != null)
                        {
                            openMetadataStore.createRelatedElementsInStore(userId,
                                                                           OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName,
                                                                           annotationGUID,
                                                                           requestForActionTargetGUID,
                                                                           newElementOptions,
                                                                           null);
                        }
                    }
                }

                if  (associatedElementGUID != null)
                {
                    openMetadataStore.createRelatedElementsInStore(userId,
                                                                   OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName,
                                                                   associatedElementGUID,
                                                                   annotationGUID,
                                                                   newElementOptions,
                                                                   null);
                }

                return annotationGUID;
            }
        }

        return null;
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param parentAnnotationGUID unique identifier of the annotation that this new one is to be attached to
     * @param annotation annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem saving annotations in the annotation store.
     */
    public String  addAnnotationExtension(String     parentAnnotationGUID,
                                          Annotation annotation) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        String annotationGUID = this.addAnnotation(annotation, null);

        if ((annotationGUID != null) && (parentAnnotationGUID != null))
        {
            openMetadataStore.createRelatedElementsInStore(userId,
                                                           OpenMetadataType.ANNOTATION_EXTENSION_RELATIONSHIP.typeName,
                                                           parentAnnotationGUID,
                                                           annotationGUID,
                                                           getMetadataSourceOptions(),
                                                           null);
        }

        return null;
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param annotation new properties
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem updating the annotation in the annotation store.
     */
    public void  updateAnnotation(Annotation annotation) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        String guid = null;

        if ((annotation != null) && (annotation.getElementHeader() != null))
        {
            guid = annotation.getElementHeader().getGUID();
        }

        AnnotationBuilder builder = getAnnotationBuilder(annotation);

        if (builder != null)
        {
            UpdateOptions updateOptions = new UpdateOptions(this.getMetadataSourceOptions());
            updateOptions.setMergePropertyUpdate(true);

            openMetadataStore.updateMetadataElementInStore(userId,
                                                           guid,
                                                           updateOptions,
                                                           builder.getElementProperties());
        }
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException there was a problem deleting the annotation from the annotation store.
     */
    public void  deleteAnnotation(String   annotationGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        DeleteOptions deleteOptions = new DeleteOptions(this.getMetadataSourceOptions());

        openMetadataStore.deleteMetadataElementInStore(userId, annotationGUID, deleteOptions);
    }


    /**
     * Load the properties in the annotation bean into an Annotation build.  This method has to take into account the different
     * types of annotation beans.  It uses the java class of the Annotation bean to extract the all the properties.
     *
     * @param annotation annotation to save
     * @return builder object loaded with the properties from the annotation
     */
    private AnnotationBuilder getAnnotationBuilder(Annotation annotation)
    {
        if (annotation != null)
        {
            AnnotationBuilder builder = new AnnotationBuilder(annotation.getAnnotationType(),
                                                              annotation.getSummary(),
                                                              annotation.getConfidenceLevel(),
                                                              annotation.getExpression(),
                                                              annotation.getExplanation(),
                                                              annotation.getAnalysisStep(),
                                                              annotation.getJsonProperties(),
                                                              annotation.getAdditionalProperties(),
                                                              annotation.getOpenMetadataTypeName(),
                                                              annotation.getExtendedProperties());

            if (annotation instanceof ClassificationAnnotation classificationAnnotation)
            {
                builder.setClassificationSubtypeProperties(OpenMetadataType.CLASSIFICATION_ANNOTATION.typeName,
                                                           classificationAnnotation.getCandidateClassifications());
            }
            else if (annotation instanceof DataClassAnnotation dataClassAnnotation)
            {
                builder.setDataClassSubtypeProperties(OpenMetadataType.DATA_CLASS_ANNOTATION.typeName,
                                                      dataClassAnnotation.getCandidateDataClassGUIDs(),
                                                      dataClassAnnotation.getMatchingValues(),
                                                      dataClassAnnotation.getNonMatchingValues());
            }
            else if (annotation instanceof ResourceProfileLogAnnotation resourceProfileLogAnnotation)
            {
                builder.setDataProfileLogSubtypeProperties(OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName,
                                                           resourceProfileLogAnnotation.getResourceProfileLogGUIDs());
            }
            else if (annotation instanceof ResourceProfileAnnotation resourceProfileAnnotation)
            {
                builder.setDataProfileSubtypeProperties(OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName,
                                                        resourceProfileAnnotation.getProfilePropertyNames(),
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
            else if (annotation instanceof ResourcePhysicalStatusAnnotation dataSourcePhysicalStatusAnnotation)
            {
                builder.setDataSourcePhysicalStatusSubtypeProperties(OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION.typeName,
                                                                     dataSourcePhysicalStatusAnnotation.getResourceProperties(),
                                                                     dataSourcePhysicalStatusAnnotation.getCreateTime(),
                                                                     dataSourcePhysicalStatusAnnotation.getModifiedTime(),
                                                                     dataSourcePhysicalStatusAnnotation.getSize(),
                                                                     dataSourcePhysicalStatusAnnotation.getEncoding());
            }
            else if (annotation instanceof ResourceMeasureAnnotation resourceMeasureAnnotation)
            {
                builder.setDataSourceMeasurementSubtypeProperties(OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName,
                                                                  resourceMeasureAnnotation.getResourceProperties());
            }
            else if (annotation instanceof QualityAnnotation qualityAnnotation)
            {
                builder.setQualitySubtypeProperties(OpenMetadataType.QUALITY_ANNOTATION.typeName,
                                                    qualityAnnotation.getQualityDimension(),
                                                    qualityAnnotation.getQualityScore());
            }
            else if (annotation instanceof RelationshipAdviceAnnotation relationshipAdviceAnnotation)
            {
                builder.setRelationshipAdviceSubtypeProperties(OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION.typeName,
                                                               relationshipAdviceAnnotation.getRelatedEntityGUID(),
                                                               relationshipAdviceAnnotation.getRelationshipTypeName(),
                                                               relationshipAdviceAnnotation.getRelationshipProperties());
            }
            else if (annotation instanceof RequestForActionAnnotation requestForActionAnnotation)
            {
                builder.setRequestForActionSubtypeProperties(OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName,
                                                             requestForActionAnnotation.getSurveyActivity(),
                                                             requestForActionAnnotation.getActionRequested(),
                                                             requestForActionAnnotation.getActionProperties(),
                                                             requestForActionAnnotation.getActionTargetGUIDs());
            }
            else if (annotation instanceof SchemaAnalysisAnnotation schemaAnalysisAnnotation)
            {
                builder.setSchemaAnalysisSubTypeProperties(OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName,
                                                           schemaAnalysisAnnotation.getSchemaName(),
                                                           schemaAnalysisAnnotation.getSchemaName());
            }
            else if (annotation instanceof SemanticAnnotation semanticAnnotation)
            {
                builder.setSemanticSubTypeProperties(OpenMetadataType.SEMANTIC_ANNOTATION.typeName,
                                                     semanticAnnotation.getInformalTerm(),
                                                     semanticAnnotation.getInformalTopic(),
                                                     semanticAnnotation.getCandidateGlossaryTermGUIDs(),
                                                     semanticAnnotation.getCandidateGlossaryCategoryGUIDs());
            }

            return builder;
        }

        return null;
    }


    /**
     * Convert a list of metadata elements into a list of annotation beans.
     *
     * @param openMetadataElements open metadata elements retrieved from the open metadata store.
     * @param methodName calling method
     *
     * @return list of annotations
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException insufficient authorization
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private List<Annotation> getAnnotationBeans(List<OpenMetadataElement> openMetadataElements,
                                                String                    methodName) throws PropertyServerException,
                                                                                             InvalidParameterException,
                                                                                             UserNotAuthorizedException
    {
        if (openMetadataElements != null)
        {
            List<Annotation> annotations = new ArrayList<>();

            for (OpenMetadataElement openMetadataElement : openMetadataElements)
            {
                annotations.add(this.getAnnotationBean(openMetadataElement, methodName));
            }

            return annotations;
        }

        return null;
    }


    /**
     * Convert a list of metadata elements into a list of annotation beans.
     *
     * @param openMetadataElements open metadata elements retrieved from the open metadata store.
     * @param methodName calling method
     *
     * @return list of annotations
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException insufficient authorization
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private List<Annotation> getRelatedAnnotationBeans(RelatedMetadataElementList openMetadataElements,
                                                       String                      methodName) throws PropertyServerException,
                                                                                                       InvalidParameterException,
                                                                                                       UserNotAuthorizedException
    {
        if ((openMetadataElements != null) && (openMetadataElements.getElementList() != null))
        {
            List<Annotation> annotations = new ArrayList<>();

            for (RelatedMetadataElement openMetadataElement : openMetadataElements.getElementList())
            {
                annotations.add(this.getAnnotationBean(openMetadataElement.getElement(), methodName));
            }

            return annotations;
        }

        return null;
    }


    /**
     * Return the default get options.
     *
     * @return default values
     */
    protected GetOptions getGetOptions()
    {
        GetOptions getOptions = new GetOptions();

        getOptions.setForLineage(forLineage);
        getOptions.setForDuplicateProcessing(forDuplicateProcessing);
        getOptions.setEffectiveTime(getEffectiveTime());

        return getOptions;
    }


    /**
     * Return the default get options.
     *
     * @return default values
     */
    protected QueryOptions getQueryOptions()
    {
        return new QueryOptions(getGetOptions());
    }


    /**
     * Return the default get options.
     *
     * @return default values
     */
    protected QueryOptions getQueryOptions(int startFrom,
                                           int pageSize)
    {
        return new QueryOptions(getGetOptions());
    }


    /**
     * Return the default metadata source options.
     *
     * @return default values
     */
    protected MetadataSourceOptions getMetadataSourceOptions()
    {
        MetadataSourceOptions metadataSourceOptions =  new MetadataSourceOptions();

        metadataSourceOptions.setForLineage(forLineage);
        metadataSourceOptions.setForDuplicateProcessing(forDuplicateProcessing);
        metadataSourceOptions.setEffectiveTime(getEffectiveTime());

        metadataSourceOptions.setExternalSourceGUID(externalSourceGUID);
        metadataSourceOptions.setExternalSourceName(externalSourceName);

        return metadataSourceOptions;
    }


    /**
     * Using the supplied instances, return a new instance of the Annotation bean.
     *
     * @param annotationElement entity that is the root of the collection of entities that make up the content of the bean
     * @param methodName calling method
     *
     * @return bean populated with properties from the instances supplied in the constructor
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException insufficient authorization
     * @throws PropertyServerException there is a problem instantiating the bean
     */
    private Annotation getAnnotationBean(OpenMetadataElement annotationElement,
                                         String              methodName) throws PropertyServerException,
                                                                                InvalidParameterException,
                                                                                UserNotAuthorizedException
    {
        if (annotationElement != null)
        {
            RelatedMetadataElementList relationships = openMetadataStore.getRelatedMetadataElements(userId,
                                                                                                    annotationElement.getElementGUID(),
                                                                                                    0,
                                                                                                    null,
                                                                                                    getQueryOptions());
            if ((relationships != null) && (relationships.getElementList() != null))
            {
                return converter.getAnnotationBean(Annotation.class,
                                                   annotationElement,
                                                   relationships.getElementList(),
                                                   methodName);
            }
            else
            {
                return converter.getAnnotationBean(Annotation.class,
                                                   annotationElement,
                                                   null,
                                                   methodName);
            }
        }

        return null;
    }


    /**
     * AnnotationBuilder supports the creation of the entities and relationships that describe a
     * Survey Action Framework (SAF) Annotation.
     */
    static class AnnotationBuilder
    {
        private String openMetadataTypeName;
        
        /*
         * Attributes supported by all annotations
         */
        private final String               annotationType;
        private final String               summary;
        private final int                  confidenceLevel;
        private final String               expression;
        private final String               explanation;
        private final String               analysisStep;
        private final String               jsonProperties;
        private final Map<String, String>  additionalProperties;
        private final Map<String, Object>  extendedProperties;

        /*
         * Attributes for the ClassificationAnnotation
         */
        private Map<String, String> candidateClassifications = null;

        /*
         * Attributes for the DataClassAnnotation
         */
        private List<String> candidateDataClassGUIDs = null;
        private long         matchingValues = 0;
        private long         nonMatchingValues = 0;

        /*
         * Attributes for the DataProfileLogAnnotation
         */

        private List<String> dataProfileDataGUIDs = null;


        /*
         * Attributes for the DataProfileAnnotation
         */
        private List<String>         profilePropertyNames = null;
        private int                  length               = 0;
        private String               inferredDataType     = null;
        private String               inferredFormat       = null;
        private int                  inferredLength       = 0;
        private int                  inferredPrecision    = 0;
        private int                  inferredScale        = 0;
        private Date                 profileStartDate     = null;
        private Date                 profileEndDate       = null;
        private Map<String, String>  profileProperties    = null;
        private Map<String, Boolean> profileFlags         = null;
        private Map<String, Date>    profileDates         = null;
        private Map<String, Long>    profileCounts        = null;
        private Map<String, Double>  profileDoubles       = null;
        private List<String>         valueList            = null;
        private Map<String, Integer> valueCount           = null;
        private String               valueRangeFrom       = null;
        private String               valueRangeTo         = null;
        private String               averageValue         = null;

        /*
         * Attributes for the DataSourceMeasurementAnnotation and DataSourcePhysicalStatusAnnotation
         */
        private Map<String, String> dataSourceProperties = null;

        private Date createTime   = null;
        private Date modifiedTime = null;
        private long   size           = 0;
        private String encoding       = null;

        /*
         * Attributes for the QualityAnnotation
         */
        private String qualityDimension = null;
        private int    qualityScore     = 0;


        /*
         * Attributes for RelationshipAdviceAnnotation
         */
        private String              relatedEntityGUID = null;
        private String              relationshipTypeName = null;
        private Map<String, String> relationshipProperties = null;

        /*
         * Attributes for RequestForActionAnnotation
         */
        private String surveyActivity  = null;
        private String actionRequested = null;
        private Map<String, String> actionProperties  = null;
        private List<String> requestForActionTargetGUIDs = null;

        /*
         * Attributes for the SchemaAnalysisAnnotation
         */
        private  String schemaName     = null;
        private  String schemaTypeName = null;

        /*
         * Attributes for SemanticAnnotation
         */
        private String       informalTerm = null;
        private String       informalTopic = null;
        private List<String> candidateGlossaryTermGUIDs = null;
        private List<String> candidateGlossaryCategoryGUIDs = null;

        private static final PropertyHelper propertyHelper = new PropertyHelper();


        /**
         * Create a builder to convert the properties of the annotation bean into repository services instances.
         * All the common properties of the annotation is passed to the builder.  Properties from the known subtypes are
         * passed on subsequent method calls.
         *
         * @param annotationType type of annotation
         * @param summary summary of the annotation
         * @param confidenceLevel how confident was discovery service that the results are correct
         * @param expression expression that summarizes the results
         * @param explanation explanation of the results
         * @param analysisStep analysis step in the discovery service that produced this annotation
         * @param jsonProperties JSON properties passed to discovery service
         * @param additionalProperties additional properties
         * @param openMetadataTypeName open metadata type name (or null)

         */
        AnnotationBuilder(String               annotationType,
                          String               summary,
                          int                  confidenceLevel,
                          String               expression,
                          String               explanation,
                          String               analysisStep,
                          String               jsonProperties,
                          Map<String, String>  additionalProperties,
                          String               openMetadataTypeName,
                          Map<String, Object>  extendedProperties)
        {
            this.annotationType       = annotationType;
            this.summary              = summary;
            this.confidenceLevel      = confidenceLevel;
            this.expression           = expression;
            this.explanation          = explanation;
            this.analysisStep         = analysisStep;
            this.jsonProperties       = jsonProperties;
            this.additionalProperties = additionalProperties;
            this.openMetadataTypeName = openMetadataTypeName;
            this.extendedProperties   = extendedProperties;
        }


        /**
         * Return the type name for the annotation.
         *
         * @return string name
         */
        public String getOpenMetadataTypeName()
        {
            if (this.openMetadataTypeName == null)
            {
                return OpenMetadataType.ANNOTATION.typeName;
            }

            return openMetadataTypeName;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param candidateClassifications discovered classifications
         */
        void setClassificationSubtypeProperties(String              annotationTypeName,
                                                Map<String, String> candidateClassifications)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.candidateClassifications = candidateClassifications;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param candidateDataClassGUIDs discovered data class candidates
         * @param matchingValues number of values in the field that match the data class
         * @param nonMatchingValues number of values in the field that do not match the data class
         */
        void setDataClassSubtypeProperties(String       annotationTypeName,
                                           List<String> candidateDataClassGUIDs,
                                           long         matchingValues,
                                           long         nonMatchingValues)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.candidateDataClassGUIDs = candidateDataClassGUIDs;
            this.matchingValues          = matchingValues;
            this.nonMatchingValues       = nonMatchingValues;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param logFileGUIDs list of assetGUIDs for additional profile information
         */
        void setDataProfileLogSubtypeProperties(String       annotationTypeName,
                                                List<String> logFileGUIDs)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }

            this.dataProfileDataGUIDs = logFileGUIDs;
        }


        /**
         * Return the list of log file guids that represent associated profile data that is too big for metadata and
         * has been written to and external data resource.
         *
         * @return list of asset GUIDs or null
         */
        public List<String> getDataProfileDataGUIDs()
        {
            return dataProfileDataGUIDs;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName unique type name of annotation
         * @param profilePropertyNames list of property names filled out in this annotation
         * @param length length of the data field.  Assumes static predefined lengths
         * @param inferredDataType name of the data type that the discovery service believes the field is
         * @param inferredFormat name of the data format that the discovery service believes the field is
         * @param inferredLength length of the data field that has been deduced from the data stored
         * @param inferredPrecision precision of the data field that has been deduced from the data stored
         * @param inferredScale inferred scale used in other properties
         * @param profileStartDate start of profile data capture
         * @param profileEndDate end of profile data capture
         * @param profileProperties the map of properties that make up the profile
         * @param profileFlags a set of boolean flags describing different aspects of the data
         * @param profileDates a set of relevant dates describing different aspects of the data
         * @param profileCounts the map of different profiling counts that have been calculated
         * @param profileDoubles the map of different large profiling counts that have been calculated
         * @param valueList the list of values found in the data field
         * @param valueCount  a map of values to value count for the data field
         * @param valueRangeFrom the lowest value of the data stored in this data field
         * @param valueRangeTo the upper value of the data stored in this data field
         * @param averageValue the average (mean) value of the values stored in the data field
         */
        void setDataProfileSubtypeProperties(String               annotationTypeName,
                                             List<String>         profilePropertyNames,
                                             int                  length,
                                             String               inferredDataType,
                                             String               inferredFormat,
                                             int                  inferredLength,
                                             int                  inferredPrecision,
                                             int                  inferredScale,
                                             Date                 profileStartDate,
                                             Date                 profileEndDate,
                                             Map<String, String>  profileProperties,
                                             Map<String, Boolean> profileFlags,
                                             Map<String, Date>    profileDates,
                                             Map<String, Long>    profileCounts,
                                             Map<String, Double>  profileDoubles,
                                             List<String>         valueList,
                                             Map<String, Integer> valueCount,
                                             String               valueRangeFrom,
                                             String               valueRangeTo,
                                             String               averageValue)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.profilePropertyNames = profilePropertyNames;
            this.length               = length;
            this.inferredDataType     = inferredDataType;
            this.inferredFormat       = inferredFormat;
            this.inferredLength       = inferredLength;
            this.inferredPrecision    = inferredPrecision;
            this.inferredScale        = inferredScale;
            this.profileStartDate     = profileStartDate;
            this.profileEndDate       = profileEndDate;
            this.profileProperties    = profileProperties;
            this.profileFlags         = profileFlags;
            this.profileDates         = profileDates;
            this.profileCounts        = profileCounts;
            this.profileDoubles       = profileDoubles;
            this.valueList            = valueList;
            this.valueCount           = valueCount;
            this.valueRangeFrom       = valueRangeFrom;
            this.valueRangeTo         = valueRangeTo;
            this.averageValue         = averageValue;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param dataSourceProperties properties of the data source
         */
        void setDataSourceMeasurementSubtypeProperties(String              annotationTypeName,
                                                       Map<String, String> dataSourceProperties)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.dataSourceProperties = dataSourceProperties;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param dataSourceProperties properties of the data source
         * @param createTime the date and time that the data source was created
         * @param modifiedTime the time that the file was last modified.
         * @param size the size in bytes of the data source
         * @param encoding the encoding of the data source
         */
        void setDataSourcePhysicalStatusSubtypeProperties(String              annotationTypeName,
                                                          Map<String, String> dataSourceProperties,
                                                          Date                createTime,
                                                          Date                modifiedTime,
                                                          long                size,
                                                          String              encoding)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.dataSourceProperties = dataSourceProperties;
            this.createTime = createTime;
            this.modifiedTime = modifiedTime;
            this.size = size;
            this.encoding = encoding;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param qualityDimension the type of quality being measured
         * @param qualityScore a quality score between 0 and 100 - 100 is the best
         */
        void setQualitySubtypeProperties(String annotationTypeName,
                                         String qualityDimension,
                                         int    qualityScore)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.qualityDimension = qualityDimension;
            this.qualityScore = qualityScore;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param relatedEntityGUID the unique identifier of the object to connect to
         * @param relationshipTypeName the type of relationship to create
         * @param relationshipProperties the properties that should be stored in the relationship
         */
        void setRelationshipAdviceSubtypeProperties(String              annotationTypeName,
                                                    String              relatedEntityGUID,
                                                    String              relationshipTypeName,
                                                    Map<String, String> relationshipProperties)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.relatedEntityGUID = relatedEntityGUID;
            this.relationshipTypeName = relationshipTypeName;
            this.relationshipProperties = relationshipProperties;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param surveyActivity the unique name of the survey activity
         * @param actionRequested the identifier of the type of action that needs to be run
         * @param actionProperties the properties that will guide the governance action
         */
        void setRequestForActionSubtypeProperties(String              annotationTypeName,
                                                  String              surveyActivity,
                                                  String              actionRequested,
                                                  Map<String, String> actionProperties,
                                                  List<String>        requestForActionTargetGUIDs)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.surveyActivity              = surveyActivity;
            this.actionRequested             = actionRequested;
            this.actionProperties            = actionProperties;
            this.requestForActionTargetGUIDs = requestForActionTargetGUIDs;
        }


        /**
         * Return the elements that need action.
         *
         * @return list of guids
         */
        public List<String> getRequestForActionTargetGUIDs()
        {
            return requestForActionTargetGUIDs;
        }



        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param schemaName name of schema
         * @param schemaTypeName type of schema
         */
        void setSchemaAnalysisSubTypeProperties(String annotationTypeName,
                                                String schemaName,
                                                String schemaTypeName)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.schemaName         = schemaName;
            this.schemaTypeName     = schemaTypeName;
        }


        /**
         * Add properties for annotation subtype.
         *
         * @param annotationTypeName type name for this subtype
         * @param informalTerm a string that describes the meaning of this data
         * @param informalTopic a string that describes the topic that this data is about
         * @param candidateGlossaryTermGUIDs a list of unique identifiers of glossary terms that describe the meaning of the data
         * @param candidateGlossaryCategoryGUIDs a list of unique identifiers of glossary categories that describe the topic of the data
         */
        void setSemanticSubTypeProperties(String       annotationTypeName,
                                          String       informalTerm,
                                          String       informalTopic,
                                          List<String> candidateGlossaryTermGUIDs,
                                          List<String> candidateGlossaryCategoryGUIDs)
        {
            if (this.openMetadataTypeName == null)
            {
                this.openMetadataTypeName = annotationTypeName;
            }
            this.informalTerm = informalTerm;
            this.informalTopic = informalTopic;
            this.candidateGlossaryTermGUIDs = candidateGlossaryTermGUIDs;
            this.candidateGlossaryCategoryGUIDs = candidateGlossaryCategoryGUIDs;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @return ElementProperties object
         */
        public ElementProperties getElementProperties()
        {
            ElementProperties properties;

            properties = propertyHelper.addPropertyMap(null, extendedProperties);
           
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.ANNOTATION_TYPE.name,
                                                          annotationType);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.SUMMARY.name,
                                                          summary);

            properties = propertyHelper.addIntProperty(properties,
                                                       OpenMetadataProperty.CONFIDENCE_LEVEL.name,
                                                       confidenceLevel);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.EXPRESSION.name,
                                                          expression);
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.EXPLANATION.name,
                                                          explanation);
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.ANALYSIS_STEP.name,
                                                          analysisStep);
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.JSON_PROPERTIES.name,
                                                          jsonProperties);
            properties = propertyHelper.addStringMapProperty(properties,
                                                             OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                             additionalProperties);
            
            if (OpenMetadataType.CLASSIFICATION_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addClassificationAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.DATA_CLASS_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addDataClassAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addDataProfileAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addResourcePhysicalStatusAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addResourceMeasureAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.QUALITY_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addQualityAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addRelationshipAdviceAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addRequestForActionAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addSchemaAnalysisAnnotationElementProperties(properties);
            }
            else if (OpenMetadataType.SEMANTIC_ANNOTATION.typeName.equals(openMetadataTypeName))
            {
                return addSemanticAnnotationElementProperties(properties);
            }

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addClassificationAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringMapProperty(properties,
                                                             OpenMetadataProperty.CANDIDATE_CLASSIFICATIONS.name,
                                                             candidateClassifications);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addDataClassAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringArrayProperty(properties,
                                                               OpenMetadataProperty.CANDIDATE_DATA_CLASS_GUIDS.name,
                                                               candidateDataClassGUIDs);

            properties = propertyHelper.addLongProperty(properties,
                                                        OpenMetadataProperty.MATCHING_VALUES.name,
                                                        matchingValues);

            properties = propertyHelper.addLongProperty(properties,
                                                        OpenMetadataProperty.NON_MATCHING_VALUES.name,
                                                        nonMatchingValues);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addDataProfileAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringArrayProperty(properties,
                                                               OpenMetadataProperty.PROFILE_PROPERTY_NAMES.name,
                                                               profilePropertyNames);

            properties = propertyHelper.addIntProperty(properties,
                                                       OpenMetadataProperty.LENGTH.name,
                                                       length);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.INFERRED_DATA_TYPE.name,
                                                          inferredDataType);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.INFERRED_FORMAT.name,
                                                          inferredFormat);

            properties = propertyHelper.addIntProperty(properties,
                                                       OpenMetadataProperty.INFERRED_LENGTH.name,
                                                       inferredLength);

            properties = propertyHelper.addIntProperty(properties,
                                                       OpenMetadataProperty.INFERRED_PRECISION.name,
                                                       inferredPrecision);

            properties = propertyHelper.addIntProperty(properties,
                                                       OpenMetadataProperty.INFERRED_SCALE.name,
                                                       inferredScale);

            properties = propertyHelper.addDateProperty(properties,
                                                        OpenMetadataProperty.PROFILE_START_DATE.name,
                                                        profileStartDate);

            properties = propertyHelper.addDateProperty(properties,
                                                        OpenMetadataProperty.PROFILE_END_DATE.name,
                                                        profileEndDate);

            properties = propertyHelper.addStringMapProperty(properties,
                                                             OpenMetadataProperty.PROFILE_PROPERTIES.name,
                                                             profileProperties);

            properties = propertyHelper.addBooleanMapProperty(properties,
                                                              OpenMetadataProperty.PROFILE_FLAGS.name,
                                                              profileFlags);

            properties = propertyHelper.addDateMapProperty(properties,
                                                           OpenMetadataProperty.PROFILE_DATES.name,
                                                           profileDates);

            properties = propertyHelper.addLongMapProperty(properties,
                                                           OpenMetadataProperty.PROFILE_COUNTS.name,
                                                           profileCounts);

            properties = propertyHelper.addDoubleMapProperty(properties,
                                                             OpenMetadataProperty.PROFILE_DOUBLES.name,
                                                             profileDoubles);

            properties = propertyHelper.addStringArrayProperty(properties,
                                                               OpenMetadataProperty.VALUE_LIST.name,
                                                               valueList);

            properties = propertyHelper.addIntMapProperty(properties,
                                                          OpenMetadataProperty.VALUE_COUNT.name,
                                                          valueCount);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.VALUE_RANGE_FROM.name,
                                                          valueRangeFrom);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.VALUE_RANGE_TO.name,
                                                          valueRangeTo);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.AVERAGE_VALUE.name,
                                                          averageValue);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addResourcePhysicalStatusAnnotationElementProperties(ElementProperties properties)
        {
            properties = this.addResourceMeasureAnnotationElementProperties(properties);

            properties = propertyHelper.addDateProperty(properties,
                                                        OpenMetadataProperty.RESOURCE_CREATE_TIME.name,
                                                        createTime);

            properties = propertyHelper.addDateProperty(properties,
                                                        OpenMetadataProperty.RESOURCE_UPDATE_TIME.name,
                                                        modifiedTime);

            properties = propertyHelper.addLongProperty(properties,
                                                        OpenMetadataProperty.SIZE.name,
                                                        size);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.ENCODING_TYPE.name,
                                                          encoding);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addResourceMeasureAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringMapProperty(properties,
                                                             OpenMetadataProperty.RESOURCE_PROPERTIES.name,
                                                             dataSourceProperties);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addQualityAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.QUALITY_DIMENSION.name,
                                                          qualityDimension);

            properties = propertyHelper.addIntProperty(properties,
                                                       OpenMetadataProperty.QUALITY_SCORE.name,
                                                       qualityScore);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addRelationshipAdviceAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.RELATED_ENTITY_GUID.name,
                                                          relatedEntityGUID);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name,
                                                          relationshipTypeName);

            properties = propertyHelper.addStringMapProperty(properties,
                                                             OpenMetadataProperty.RELATIONSHIP_PROPERTIES.name,
                                                             relationshipProperties);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addRequestForActionAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.ACTION_SOURCE_NAME.name,
                                                          surveyActivity);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.ACTION_REQUESTED.name,
                                                          actionRequested);

            properties = propertyHelper.addStringMapProperty(properties,
                                                             OpenMetadataProperty.ACTION_PROPERTIES.name,
                                                             actionProperties);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addSchemaAnalysisAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.SCHEMA_NAME.name,
                                                          schemaName);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.SCHEMA_TYPE.name,
                                                          schemaTypeName);

            return properties;
        }


        /**
         * Return the supplied bean properties in an ElementProperties object for the annotation entity.
         *
         * @param properties properties to fill out
         * @return ElementProperties object
         */
        private ElementProperties addSemanticAnnotationElementProperties(ElementProperties properties)
        {
            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.INFORMAL_TERM.name,
                                                          informalTerm);

            properties = propertyHelper.addStringProperty(properties,
                                                          OpenMetadataProperty.INFORMAL_CATEGORY.name,
                                                          informalTopic);

            properties = propertyHelper.addStringArrayProperty(properties,
                                                               OpenMetadataProperty.CANDIDATE_GLOSSARY_TERM_GUIDS.name,
                                                               candidateGlossaryTermGUIDs);

            properties = propertyHelper.addStringArrayProperty(properties,
                                                               OpenMetadataProperty.CANDIDATE_GLOSSARY_CATEGORY_GUIDS.name,
                                                               candidateGlossaryCategoryGUIDs);

            return properties;
        }
    }


    /**
     * AnnotationConverter provides the generic methods for the Survey Action Framework Annotation beans converters.  Generic classes
     * have limited knowledge of the classes these are working on and this means creating a new instance of a
     * class from within a generic is a little involved.  This class provides the generic method for creating
     * and initializing an Annotation bean.
     */
    static class AnnotationConverter<B> extends OpenMetadataConverterBase<B>
    {
        final static PropertyHelper propertyHelper = new PropertyHelper();

        /**
         * Constructor
         *
         * @param serviceName    name of this component
         * @param serverName     local server name
         */
        public AnnotationConverter(String serviceName,
                                   String serverName)
        {
            super(propertyHelper, serviceName, serverName);
        }


        /**
         * Using the supplied instances, return a new instance of the Connection bean. It may be a Connection or a VirtualConnection.
         *
         * @param beanClass         class of bean that has been requested
         * @param annotationElement entity that is the root of the collection of entities that make up the
         *                          content of the bean
         * @param relationships     relationships linking the entities
         * @param methodName        calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        protected Annotation getAnnotationBean(Class<B>                     beanClass,
                                               OpenMetadataElement          annotationElement,
                                               List<RelatedMetadataElement> relationships,
                                               String                       methodName) throws PropertyServerException
        {
            if ((annotationElement != null) && (annotationElement.getType() != null))
            {
                if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.CLASSIFICATION_ANNOTATION.typeName))
                {
                    return getNewClassificationAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.DATA_CLASS_ANNOTATION.typeName))
                {
                    return getNewDataClassAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.RESOURCE_PROFILE_ANNOTATION.typeName))
                {
                    return getNewDataProfileAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.RESOURCE_PROFILE_LOG_ANNOTATION.typeName))
                {
                    return getNewDataProfileLogAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.RESOURCE_PHYSICAL_STATUS_ANNOTATION.typeName))
                {
                    return getNewDataSourcePhysicalStatusAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.RESOURCE_MEASURE_ANNOTATION.typeName))
                {
                    return getNewDataSourceMeasurementAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.QUALITY_ANNOTATION.typeName))
                {
                    return getNewQualityAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.RELATIONSHIP_ADVICE_ANNOTATION.typeName))
                {
                    return getNewRelatedMetadataElementAdviceAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.REQUEST_FOR_ACTION_ANNOTATION.typeName))
                {
                    return getNewRequestForActionAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.SCHEMA_ANALYSIS_ANNOTATION.typeName))
                {
                    return getNewSchemaAnalysisAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.SEMANTIC_ANNOTATION.typeName))
                {
                    return getNewSemanticAnnotation(beanClass, annotationElement, relationships, methodName);
                }
                else if (propertyHelper.isTypeOf(annotationElement, OpenMetadataType.ANNOTATION.typeName))
                {
                    return getNewAnnotation(beanClass, annotationElement, relationships, methodName);
                }
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the DataClassAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewClassificationAnnotation(Class<B> beanClass,
                                                          OpenMetadataElement primaryEntity,
                                                          List<RelatedMetadataElement> relationships,
                                                          String methodName) throws PropertyServerException
        {
            try
            {
                ClassificationAnnotation annotation = new ClassificationAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setCandidateClassifications(this.removeCandidateClassifications(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewDataClassAnnotation(Class<B> beanClass,
                                                     OpenMetadataElement primaryEntity,
                                                     List<RelatedMetadataElement> relationships,
                                                     String methodName) throws PropertyServerException
        {
            try
            {
                DataClassAnnotation annotation = new DataClassAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setCandidateDataClassGUIDs(this.removeCandidateDataClassGUIDs(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the DataProfileAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewDataProfileAnnotation(Class<B> beanClass,
                                                       OpenMetadataElement primaryEntity,
                                                       List<RelatedMetadataElement> relationships,
                                                       String methodName) throws PropertyServerException
        {
            try
            {
                ResourceProfileAnnotation annotation = new ResourceProfileAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setLength(this.removeLength(remainingProperties));
                annotation.setInferredDataType(this.removeInferredDataType(remainingProperties));
                annotation.setInferredFormat(this.removeInferredFormat(remainingProperties));
                annotation.setInferredLength(this.removeInferredLength(remainingProperties));
                annotation.setInferredPrecision(this.removeInferredPrecision(remainingProperties));
                annotation.setInferredScale(this.removeInferredScale(remainingProperties));
                annotation.setProfileStartDate(this.removeProfileStartDate(remainingProperties));
                annotation.setProfileEndDate(this.removeProfileEndDate(remainingProperties));
                annotation.setProfileProperties(this.removeProfileProperties(remainingProperties));
                annotation.setProfileFlags(this.removeProfileFlags(remainingProperties));
                annotation.setProfileCounts(this.removeProfileCounts(remainingProperties));
                annotation.setProfileDoubles(this.removeProfileDoubles(remainingProperties));
                annotation.setValueList(this.removeValueList(remainingProperties));
                annotation.setValueCount(this.removeValueCount(remainingProperties));
                annotation.setValueRangeFrom(this.removeValueRangeFrom(remainingProperties));
                annotation.setValueRangeTo(this.removeValueRangeTo(remainingProperties));
                annotation.setAverageValue(this.removeAverageValue(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the DataProfileLogAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewDataProfileLogAnnotation(Class<B> beanClass,
                                                          OpenMetadataElement primaryEntity,
                                                          List<RelatedMetadataElement> relationships,
                                                          String methodName) throws PropertyServerException
        {
            try
            {
                ResourceProfileLogAnnotation annotation = new ResourceProfileLogAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);


                List<String> dataProfileLogs = new ArrayList<>();

                if (relationships != null)
                {
                    for (RelatedMetadataElement relationship : relationships)
                    {
                        if (relationship != null)
                        {
                            if (propertyHelper.isTypeOf(relationship, OpenMetadataType.RESOURCE_PROFILE_DATA_RELATIONSHIP.typeName))
                            {
                                OpenMetadataElement logFile = relationship.getElement();

                                if (logFile != null)
                                {
                                    dataProfileLogs.add(logFile.getElementGUID());
                                }
                            }
                        }
                    }
                }

                annotation.setResourceProfileLogGUIDs(dataProfileLogs);

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the DataSourceMeasurementAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewDataSourceMeasurementAnnotation(Class<B> beanClass,
                                                                 OpenMetadataElement primaryEntity,
                                                                 List<RelatedMetadataElement> relationships,
                                                                 String methodName) throws PropertyServerException
        {
            try
            {
                ResourceMeasureAnnotation annotation = new ResourceMeasureAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setResourceProperties(this.removeResourceProperties(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the DataSourcePhysicalStatusAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewDataSourcePhysicalStatusAnnotation(Class<B> beanClass,
                                                                    OpenMetadataElement primaryEntity,
                                                                    List<RelatedMetadataElement> relationships,
                                                                    String methodName) throws PropertyServerException
        {
            try
            {
                ResourcePhysicalStatusAnnotation annotation = new ResourcePhysicalStatusAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setResourceProperties(this.removeResourceProperties(remainingProperties));
                annotation.setCreateTime(this.removeResourceCreateTime(remainingProperties));
                annotation.setModifiedTime(this.removeResourceUpdateTime(remainingProperties));
                annotation.setLastAccessedTime(this.removeResourceLastAccessedTime(remainingProperties));
                annotation.setSize(this.removeSize(remainingProperties));
                annotation.setEncoding(this.removeEncoding(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the QualityAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewQualityAnnotation(Class<B> beanClass,
                                                   OpenMetadataElement primaryEntity,
                                                   List<RelatedMetadataElement> relationships,
                                                   String methodName) throws PropertyServerException
        {
            try
            {
                QualityAnnotation annotation = new QualityAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setQualityDimension(this.removeQualityDimension(remainingProperties));
                annotation.setQualityScore(this.removeQualityScore(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the QualityAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewRelatedMetadataElementAdviceAnnotation(Class<B> beanClass,
                                                                        OpenMetadataElement primaryEntity,
                                                                        List<RelatedMetadataElement> relationships,
                                                                        String methodName) throws PropertyServerException
        {
            try
            {
                RelationshipAdviceAnnotation annotation = new RelationshipAdviceAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setRelatedEntityGUID(this.removeRelatedEntityGUID(remainingProperties));
                annotation.setRelationshipTypeName(this.removeRelationshipTypeName(remainingProperties));
                annotation.setRelationshipProperties(this.removeRelationshipProperties(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the RequestForActionAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewRequestForActionAnnotation(Class<B> beanClass,
                                                            OpenMetadataElement primaryEntity,
                                                            List<RelatedMetadataElement> relationships,
                                                            String methodName) throws PropertyServerException
        {
            try
            {
                RequestForActionAnnotation annotation = new RequestForActionAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setSurveyActivity(this.removeActionSourceName(remainingProperties));
                annotation.setActionRequested(this.removeActionRequested(remainingProperties));
                annotation.setActionProperties(this.removeActionProperties(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                List<String> actionTargetGUIDS = new ArrayList<>();

                if (relationships != null)
                {
                    for (RelatedMetadataElement relationship : relationships)
                    {
                        if (relationship != null)
                        {
                            if (propertyHelper.isTypeOf(relationship, OpenMetadataType.REQUEST_FOR_ACTION_TARGET.typeName))
                            {
                                OpenMetadataElement logFile = relationship.getElement();

                                if (logFile != null)
                                {
                                    actionTargetGUIDS.add(logFile.getElementGUID());
                                }
                            }
                        }
                    }
                }

                annotation.setActionTargetGUIDs(actionTargetGUIDS);
                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewSchemaAnalysisAnnotation(Class<B> beanClass,
                                                          OpenMetadataElement primaryEntity,
                                                          List<RelatedMetadataElement> relationships,
                                                          String methodName) throws PropertyServerException
        {
            try
            {
                SchemaAnalysisAnnotation annotation = new SchemaAnalysisAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setSchemaName(this.removeSchemaName(remainingProperties));
                annotation.setSchemaTypeName(this.removeSchemaType(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the SchemaAnalysisAnnotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewSemanticAnnotation(Class<B> beanClass,
                                                    OpenMetadataElement primaryEntity,
                                                    List<RelatedMetadataElement> relationships,
                                                    String methodName) throws PropertyServerException
        {
            try
            {
                SemanticAnnotation annotation = new SemanticAnnotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                annotation.setInformalTerm(this.removeInformalTerm(remainingProperties));
                annotation.setCandidateGlossaryTermGUIDs(this.removeCandidateGlossaryTermGUIDs(remainingProperties));
                annotation.setInformalTopic(this.removeInformalTopic(remainingProperties));
                annotation.setCandidateGlossaryCategoryGUIDs(this.removeCandidateGlossaryCategoryGUIDs(remainingProperties));

                /*
                 * Any remaining properties are returned in the extended properties.  They are
                 * assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Using the supplied instances, return a new instance of the Annotation bean.
         *
         * @param beanClass     name of the class to create
         * @param primaryEntity entity that is the root of the collection of entities that make up the
         *                      content of the bean
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @return bean populated with properties from the instances supplied in the constructor
         * @throws PropertyServerException there is a problem instantiating the bean
         */
        private Annotation getNewAnnotation(Class<B>                     beanClass,
                                            OpenMetadataElement          primaryEntity,
                                            List<RelatedMetadataElement> relationships,
                                            String                       methodName) throws PropertyServerException
        {
            try
            {
                Annotation annotation = new Annotation();

                ElementProperties remainingProperties = fillInCommonAnnotationProperties(beanClass,
                                                                                         annotation,
                                                                                         primaryEntity,
                                                                                         relationships,
                                                                                         methodName);

                /*
                 * Any remaining properties are returned in the extended properties.
                 * They are assumed to be defined in a subtype.
                 */
                annotation.setExtendedProperties(this.getRemainingExtendedProperties(remainingProperties));

                return annotation;
            }
            catch (ClassCastException error)
            {
                super.handleInvalidBeanClass(beanClass.getName(), error, methodName);
            }

            return null;
        }


        /**
         * Retrieve the annotation properties from an entity and save them in the supplied bean
         *
         * @param annotation    bean to fill
         * @param primaryEntity entity to trawl for values
         * @param relationships relationships linking the entities
         * @param methodName    calling method
         * @throws PropertyServerException there was a problem unpacking the entity
         */
        private ElementProperties fillInCommonAnnotationProperties(Class<B>                     beanClass,
                                                                   Annotation                   annotation,
                                                                   OpenMetadataElement          primaryEntity,
                                                                   List<RelatedMetadataElement> relationships,
                                                                   String                       methodName) throws PropertyServerException
        {
            annotation.setElementHeader(this.getMetadataElementHeader(beanClass, primaryEntity, methodName));

            /*
             * The initial set of values come from the entity properties.  The super class properties are removed from a copy of the entities
             * properties, leaving any subclass properties to be stored in extended properties.
             */
            ElementProperties elementProperties = null;
            if (primaryEntity.getElementProperties() != null)
            {
                elementProperties = new ElementProperties(primaryEntity.getElementProperties());
            }

            annotation.setAnnotationType(this.removeAnnotationType(elementProperties));
            annotation.setSummary(this.removeSummary(elementProperties));
            annotation.setConfidenceLevel(this.removeConfidenceLevel(elementProperties));
            annotation.setExpression(this.removeExpression(elementProperties));
            annotation.setExplanation(this.removeExplanation(elementProperties));
            annotation.setAnalysisStep(this.removeAnalysisStep(elementProperties));
            annotation.setJsonProperties(this.removeJsonProperties(elementProperties));
            annotation.setAdditionalProperties(this.removeAdditionalProperties(elementProperties));

            /*
             * The other entities should include the annotation review and the associated element(s).
             */
            if (relationships != null)
            {
                for (RelatedMetadataElement relationship : relationships)
                {
                    if (relationship != null)
                    {
                        if (propertyHelper.isTypeOf(relationship, OpenMetadataType.ANNOTATION_REVIEW_LINK_RELATIONSHIP.typeName))
                        {
                            annotation.setAnnotationStatus(this.getAnnotationStatusFromProperties(elementProperties));

                            if (propertyHelper.isTypeOf(relationship.getElement(), OpenMetadataType.ANNOTATION_REVIEW.typeName))
                            {
                                ElementProperties properties = new ElementProperties(relationship.getElement().getElementProperties());

                                annotation.setReviewDate(this.removeReviewDate(properties));
                                annotation.setSteward(this.removeSteward(properties));
                                annotation.setReviewComment(this.removeComment(properties));
                            }
                        }
                        else if (propertyHelper.isTypeOf(relationship, OpenMetadataType.ASSOCIATED_ANNOTATION_RELATIONSHIP.typeName))
                        {
                            if (annotation.getAnnotationSubjects() == null)
                            {
                                List<OpenMetadataElement> openMetadataElements = new ArrayList<>();

                                openMetadataElements.add(relationship.getElement());

                                annotation.setAnnotationSubjects(openMetadataElements);
                            }
                            else
                            {
                                annotation.getAnnotationSubjects().add(relationship.getElement());
                            }
                        }
                    }
                }
            }

            return elementProperties;
        }


        /**
         * Retrieve the AnnotationStatus enum property from the instance properties of the Annotation Review Link relationship.
         *
         * @param properties entity properties
         * @return enum value
         */
        private AnnotationStatus getAnnotationStatusFromProperties(ElementProperties properties)
        {
            if (properties != null)
            {
                Map<String, PropertyValue> elementPropertiesMap = properties.getPropertyValueMap();

                PropertyValue instancePropertyValue = elementPropertiesMap.get(OpenMetadataProperty.ANNOTATION_STATUS.name);

                if (instancePropertyValue instanceof EnumTypePropertyValue enumPropertyValue)
                {
                    for (AnnotationStatus annotationStatus : AnnotationStatus.values())
                    {
                        if (annotationStatus.name().equals(enumPropertyValue.getSymbolicName()))
                        {
                            return annotationStatus;
                        }
                    }
                }
            }

            return AnnotationStatus.UNKNOWN_STATUS;
        }
    }
}
