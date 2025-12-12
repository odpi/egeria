/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opensurvey;

import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AnnotationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.AnnotationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.RequestForActionTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;

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
    private String reportDisplayName;
    private String surveyDescription;
    private String surveyPurpose;
    private String analysisStep = null;

    private final AnnotationHandler annotationHandler;

    private static final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Constructor sets up the key parameters for accessing the annotations store and creates
     * a survey report.  This constructor is used by the engine service running a survey action service.
     *
     * @param userId              calling user
     * @param assetGUID           unique of the asset that describes the resource being surveyed
     * @param openMetadataStore   access to the open metadata repositories
     * @param annotationHandler   client capable of managing annotation
     * @param externalSourceGUID  unique identifier of the external source that is supplying the survey data
     * @param externalSourceName  unique name of the external source that is supplying the survey data
     * @param reportQualifiedName qualified name of report
     * @param reportDisplayName   display name of report
     * @param surveyDescription   description of report
     * @param surveyPurpose       purpose of the survey
     * @param engineActionGUID    unique identifier of the engine action that started this report
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem retrieving annotations from the annotation store.
     */
    public AnnotationStore(String             userId,
                           String             assetGUID,
                           OpenMetadataClient openMetadataStore,
                           AnnotationHandler  annotationHandler,
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
        this.annotationHandler   = annotationHandler;
        this.assetGUID           = assetGUID;
        this.openMetadataStore   = openMetadataStore;
        this.externalSourceGUID  = externalSourceGUID;
        this.externalSourceName  = externalSourceName;
        this.reportQualifiedName = reportQualifiedName;
        this.reportDisplayName   = reportDisplayName;
        this.surveyDescription   = surveyDescription;
        this.surveyPurpose       = surveyPurpose;


        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        reportQualifiedName);
        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DISPLAY_NAME.name,
                                                      reportDisplayName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.AUTHOR.name,
                                                      userId);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                      surveyDescription);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.PURPOSE.name,
                                                      surveyPurpose);

        Date startDate = new Date();
        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.START_TIME.name,
                                                    startDate);

        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.CREATED_TIME.name,
                                                    startDate);

        NewElementOptions newElementOptions = new NewElementOptions();

        newElementOptions.setExternalSourceGUID(externalSourceGUID);
        newElementOptions.setExternalSourceName(externalSourceName);

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(assetGUID);
        newElementOptions.setIsOwnAnchor(false);

        newElementOptions.setParentGUID(assetGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.REPORT_SUBJECT.typeName);


        this.surveyReportGUID = openMetadataStore.createMetadataElementInStore(userId,
                                                                               OpenMetadataType.SURVEY_REPORT.typeName,
                                                                               newElementOptions,
                                                                               null,
                                                                               new NewElementProperties(properties),
                                                                               null);

        if ((surveyReportGUID != null) && (engineActionGUID != null))
        {
            openMetadataStore.createRelatedElementsInStore(userId,
                                                           OpenMetadataType.REPORT_ORIGINATOR.typeName,
                                                           engineActionGUID,
                                                           surveyReportGUID,
                                                           new MakeAnchorOptions(newElementOptions),
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
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem updating the survey report
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
        updateOptions.setMergeUpdate(true);

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
     * @param reportName String report name
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem updating the survey report
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
        updateOptions.setMergeUpdate(true);

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
     * @param reportName String report name
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem updating the survey report
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
        updateOptions.setMergeUpdate(true);

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
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem updating the survey report
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
        updateOptions.setMergeUpdate(true);

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
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem updating the survey report
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
        updateOptions.setMergeUpdate(true);

        openMetadataStore.updateMetadataElementInStore(userId,
                                                       surveyReportGUID,
                                                       updateOptions,
                                                       properties);
    }


    /**
     * Set up the completion message,
     *
     * @param completionMessage new completion message
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem updating the survey report
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
        updateOptions.setMergeUpdate(true);

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
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem retrieving annotations from the annotation store.
     */
    public List<OpenMetadataRootElement> getAnnotationsForElement(String elementGUID) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return annotationHandler.getAnnotationsForElement(userId, elementGUID, this.getQueryOptions());
    }


    /**
     * Return the current list of annotations created by this survey run.
     * This method is used by survey pipeline steps to pick up the annotations
     *
     * @param startFrom starting position in the list.
     * @param pageSize  maximum number of elements that can be returned
     * @return list of annotation (or null if none are registered)
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem retrieving annotations from the annotation store.
     */
    public List<OpenMetadataRootElement> getNewAnnotations(int startFrom,
                                                           int pageSize) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        return annotationHandler.getAnnotationsForElement(userId, surveyReportGUID, this.getQueryOptions(startFrom, pageSize));
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param annotationGUID parent annotation
     * @return list of AnnotationProperties objects
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    there was a problem that occurred within the property server.
     */
    public List<OpenMetadataRootElement> getExtendedAnnotations(String annotationGUID) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return annotationHandler.getAnnotationExtensions(userId, annotationGUID, this.getQueryOptions());
    }


    /**
     * Return a specific annotation stored in the annotation store (previous or new).
     *
     * @param annotationGUID unique identifier of the annotation
     * @return annotation object
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem retrieving the annotation from the annotation store.
     */
    public OpenMetadataRootElement getAnnotation(String annotationGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return annotationHandler.getAnnotationByGUID(userId, annotationGUID, this.getGetOptions());
    }


    /**
     * Add a new annotation to the annotation store as a top level annotation linked directly off of the report.
     *
     * @param annotationProperties  annotation object
     * @param associatedElementGUID unique identifier of the element to associate this annotation with
     *                              (it is associated with the survey report automatically).
     *                              This value may be null to indicate that it is only to be linked to the report.
     * @return unique identifier of new annotation
     * @throws InvalidParameterException  the annotation is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem adding the annotation to the annotation store.
     */
    public String addAnnotation(AnnotationProperties annotationProperties,
                                String associatedElementGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {

        NewElementOptions newElementOptions = new NewElementOptions(this.getMakeAnchorOptions());

        newElementOptions.setInitialStatus(ElementStatus.ACTIVE);
        newElementOptions.setAnchorGUID(assetGUID);
        newElementOptions.setIsOwnAnchor(false);

        newElementOptions.setParentGUID(surveyReportGUID);
        newElementOptions.setParentAtEnd1(true);
        newElementOptions.setParentRelationshipTypeName(OpenMetadataType.REPORTED_ANNOTATION_RELATIONSHIP.typeName);

        String annotationGUID = annotationHandler.createAnnotation(userId,
                                                                   newElementOptions,
                                                                   null,
                                                                   annotationProperties,
                                                                   null);

        if (annotationGUID != null)
        {
            if (associatedElementGUID != null)
            {
                annotationHandler.linkAnnotationToDescribedElement(userId,
                                                                   associatedElementGUID,
                                                                   annotationGUID,
                                                                   new MakeAnchorOptions(newElementOptions),
                                                                   null);
            }

            return annotationGUID;
        }

        return null;
    }


    /**
     * Add a new annotation and link it to an existing annotation.
     *
     * @param parentAnnotationGUID unique identifier of the annotation that this new one is to be attached to
     * @param annotationProperties annotation object
     * @return unique identifier of new annotation
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem saving annotations in the annotation store.
     */
    public String addAnnotationExtension(String parentAnnotationGUID,
                                         AnnotationProperties annotationProperties) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        String annotationGUID = this.addAnnotation(annotationProperties, null);

        if ((annotationGUID != null) && (parentAnnotationGUID != null))
        {
            annotationHandler.linkAnnotationToItsPredecessor(userId,
                                                             parentAnnotationGUID,
                                                             annotationGUID,
                                                             getMakeAnchorOptions(),
                                                             null);
        }

        return null;
    }


    /**
     * Replace the current properties of an annotation.
     *
     * @param annotationProperties new properties
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem updating the annotation in the annotation store.
     */
    public boolean updateAnnotation(String               annotationGUID,
                                    AnnotationProperties annotationProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        UpdateOptions updateOptions = new UpdateOptions(this.getMakeAnchorOptions());
        updateOptions.setMergeUpdate(true);

        return annotationHandler.updateAnnotation(userId,
                                                  annotationGUID,
                                                  updateOptions,
                                                  annotationProperties);
    }


    /**
     * Attach a request for action annotation to the element that needs attention.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRequestForActionTarget(String                           annotationGUID,
                                           String                           elementGUID,
                                           RequestForActionTargetProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        annotationHandler.linkRequestForActionTarget(userId, annotationGUID, elementGUID, getMakeAnchorOptions(), relationshipProperties);
    }


    /**
     * Detach a request for action annotation from its intended target element.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRequestForActionTarget(String        annotationGUID,
                                             String        elementGUID) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        annotationHandler.detachRequestForActionTarget(userId, annotationGUID, elementGUID, new DeleteOptions(getMakeAnchorOptions()));
    }


    /**
     * Remove an annotation from the annotation store.
     *
     * @param annotationGUID unique identifier of the annotation
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user id not authorized to issue this request
     * @throws PropertyServerException    there was a problem deleting the annotation from the annotation store.
     */
    public void deleteAnnotation(String annotationGUID) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        DeleteOptions deleteOptions = new DeleteOptions(this.getMakeAnchorOptions());

        annotationHandler.deleteAnnotation(userId, annotationGUID, deleteOptions);
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
    protected MakeAnchorOptions getMakeAnchorOptions()
    {
        MakeAnchorOptions makeAnchorOptions = new MakeAnchorOptions();

        makeAnchorOptions.setForLineage(forLineage);
        makeAnchorOptions.setForDuplicateProcessing(forDuplicateProcessing);
        makeAnchorOptions.setEffectiveTime(getEffectiveTime());

        makeAnchorOptions.setExternalSourceGUID(externalSourceGUID);
        makeAnchorOptions.setExternalSourceName(externalSourceName);

        return makeAnchorOptions;
    }
}
