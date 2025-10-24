/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.AnnotationHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.surveyreports.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with annotation elements.
 */
public class AnnotationClient extends ConnectorContextClientBase
{
    private final AnnotationHandler annotationHandler;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public AnnotationClient(ConnectorContextBase     parentContext,
                            String                   localServerName,
                            String                   localServiceName,
                            String                   connectorUserId,
                            String                   connectorGUID,
                            String                   externalSourceGUID,
                            String                   externalSourceName,
                            OpenMetadataClient       openMetadataClient,
                            AuditLog                 auditLog,
                            int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.annotationHandler = new AnnotationHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public AnnotationClient(AnnotationClient template,
                            String           specificTypeName)
    {
        super(template);

        this.annotationHandler = new AnnotationHandler(template.annotationHandler, specificTypeName);
    }


    /**
     * Create a new annotation.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createAnnotation(NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   AnnotationProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        String elementGUID = annotationHandler.createAnnotation(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent an annotation using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new annotation.
     *
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing annotation to copy (this will copy all the attachments such as nested content, schema
     *                                     connection etc)
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAnnotationFromTemplate(TemplateOptions        templateOptions,
                                               String                 templateGUID,
                                               ElementProperties      replacementProperties,
                                               Map<String, String>    placeholderProperties,
                                               RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        String elementGUID = annotationHandler.createAnnotationFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of an annotation.
     *
     * @param annotationGUID       unique identifier of the annotation (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateAnnotation(String               annotationGUID,
                                 UpdateOptions        updateOptions,
                                 AnnotationProperties properties) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        annotationHandler.updateAnnotation(connectorUserId, annotationGUID, updateOptions, properties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(annotationGUID);
        }
    }



    /**
     * Create a relationship that links a new annotation to its survey report.  This relationship is typically
     * established during the createAnnotation as the parent relationship.  It is included for completeness.
     *
     * @param surveyReportGUID       unique identifier of the report
     * @param newAnnotationGUID           unique identifier of the  annotation
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void attachAnnotationToReport(String                       surveyReportGUID,
                                         String                       newAnnotationGUID,
                                         MetadataSourceOptions        metadataSourceOptions,
                                         ReportedAnnotationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        annotationHandler.attachAnnotationToReport(connectorUserId, surveyReportGUID, newAnnotationGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an annotation from its report (ReportedAnnotation relationship).
     *
     * @param surveyReportGUID       unique identifier of the report
     * @param annotationGUID           unique identifier of the annotation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAnnotationFromReport(String        surveyReportGUID,
                                           String        annotationGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        annotationHandler.detachAnnotationFromReport(connectorUserId, surveyReportGUID, annotationGUID, deleteOptions);
    }


    /**
     * Attach an annotation to the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAnnotationToDescribedElement(String                         elementGUID,
                                                 String                         annotationGUID,
                                                 MetadataSourceOptions          metadataSourceOptions,
                                                 AssociatedAnnotationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        annotationHandler.linkAnnotationToDescribedElement(connectorUserId, elementGUID, annotationGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an annotation from the element that it is describing (via AssociatedAnnotation relationship).
     *
     * @param elementGUID          unique identifier of the described element
     * @param annotationGUID          unique identifier of the annotation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAnnotationFromDescribedElement(String        elementGUID,
                                                     String        annotationGUID,
                                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        annotationHandler.detachAnnotationFromDescribedElement(connectorUserId, elementGUID, annotationGUID, deleteOptions);
    }


    /**
     * Attach an annotation to the equivalent annotation from the previous run of the survey.
     *
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAnnotationToItsPredecessor(String                        previousAnnotationGUID,
                                               String                        newAnnotationGUID,
                                               MetadataSourceOptions         metadataSourceOptions,
                                               AnnotationExtensionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        annotationHandler.linkAnnotationToItsPredecessor(connectorUserId, previousAnnotationGUID, newAnnotationGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach an annotation from an annotation from the previous run of the survey.
     *
     * @param previousAnnotationGUID          unique identifier of the annotation from the previous run of the survey
     * @param newAnnotationGUID            unique identifier of the annotation from this run of the survey
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAnnotationFromItsPredecessor(String        previousAnnotationGUID,
                                                   String        newAnnotationGUID,
                                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        annotationHandler.detachAnnotationFromItsPredecessor(connectorUserId, previousAnnotationGUID, newAnnotationGUID, deleteOptions);
    }


    /**
     * Attach a schema analysis annotation to a matching schema type.
     *
     * @param annotationGUID       unique identifier of the annotation
     * @param schemaTypeGUID            unique identifier of the schema type
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDiscoveredSchemaType(String                         annotationGUID,
                                         String                         schemaTypeGUID,
                                         MetadataSourceOptions          metadataSourceOptions,
                                         DiscoveredSchemaTypeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        annotationHandler.linkDiscoveredSchemaType(connectorUserId, annotationGUID, schemaTypeGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a schema analysis annotation from a matching schema type.
     *
     * @param annotationGUID              unique identifier of the parent process
     * @param schemaTypeGUID          unique identifier of the child process
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDiscoveredSchemaType(String        annotationGUID,
                                           String        schemaTypeGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        annotationHandler.detachDiscoveredSchemaType(connectorUserId, annotationGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Attach a resource profile log annotation to an asset where the profile data is stored.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkResourceProfileData(String                        annotationGUID,
                                        String                        assetGUID,
                                        MetadataSourceOptions         metadataSourceOptions,
                                        ResourceProfileDataProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        annotationHandler.linkResourceProfileData(connectorUserId, annotationGUID, assetGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a resource profile log annotation from an asset where the profile data is stored.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param assetGUID         unique identifier of the associated asset
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachResourceProfileData(String        annotationGUID,
                                          String        assetGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        annotationHandler.detachResourceProfileData(connectorUserId, annotationGUID, assetGUID, deleteOptions);
    }


    /**
     * Attach a data class annotation to a data class.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param dataClassGUID         unique identifier of the associated data class
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataClassMatch(String                   annotationGUID,
                                   String                   dataClassGUID,
                                   MetadataSourceOptions    metadataSourceOptions,
                                   DataClassMatchProperties relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        annotationHandler.linkDataClassMatch(connectorUserId, annotationGUID, dataClassGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a data class annotation from a data class.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param dataClassGUID         unique identifier of the associated data class
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataClassMatch(String        annotationGUID,
                                     String        dataClassGUID,
                                     DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        annotationHandler.detachDataClassMatch(connectorUserId, annotationGUID, dataClassGUID, deleteOptions);
    }


    /**
     * Attach a request for action annotation to the element that needs attention.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRequestForActionTarget(String                           annotationGUID,
                                           String                           elementGUID,
                                           MetadataSourceOptions            metadataSourceOptions,
                                           RequestForActionTargetProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        annotationHandler.linkRequestForActionTarget(connectorUserId, annotationGUID, elementGUID, metadataSourceOptions, relationshipProperties);
    }


    /**
     * Detach a request for action annotation from its intended target element.
     *
     * @param annotationGUID               unique identifier of the annotation
     * @param elementGUID         unique identifier of the associated element
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRequestForActionTarget(String        annotationGUID,
                                             String        elementGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        annotationHandler.detachRequestForActionTarget(connectorUserId, annotationGUID, elementGUID, deleteOptions);
    }



    /**
     * Delete an annotation.
     *
     * @param annotationGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteAnnotation(String        annotationGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        annotationHandler.deleteAnnotation(connectorUserId, annotationGUID, deleteOptions);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementDelete(annotationGUID);
        }
    }


    /**
     * Returns the list of annotations with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsByName(String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return annotationHandler.getAnnotationsByName(connectorUserId, name, queryOptions);
    }




    /**
     * Returns the list of annotations associated with a particular analysis step.
     *
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsByAnalysisStep(String       name,
                                                                      QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        return annotationHandler.getAnnotationsByAnalysisStep(connectorUserId, name, queryOptions);
    }


    /**
     * Returns the list of annotations with a particular annotation type property.
     *
     * @param name                   deployedImplementationType name of the element to return - match is full text match
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsByAnnotationType(String       name,
                                                                        QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        return annotationHandler.getAnnotationsByAnnotationType(connectorUserId, name, queryOptions);
    }


    /**
     * Returns the annotations created under the supplied survey report.
     *
     * @param surveyReportGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNewAnnotations(String       surveyReportGUID,
                                                           QueryOptions queryOptions) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return annotationHandler.getNewAnnotations(connectorUserId, surveyReportGUID, queryOptions);
    }


    /**
     * Returns the list of annotations that describe the supplied element (AssociatedAnnotation relationship).
     *
     * @param elementGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationsForElement(String       elementGUID,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        return annotationHandler.getAnnotationsForElement(connectorUserId, elementGUID, queryOptions);
    }


    /**
     * Returns the list of annotations that extend the supplied annotation (AnnotationExtension relationship).
     *
     * @param annotationGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAnnotationExtensions(String       annotationGUID,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        return annotationHandler.getAnnotationExtensions(connectorUserId, annotationGUID, queryOptions);
    }


    /**
     * Returns the list of annotations that are extended by the supplied annotation (AnnotationExtension relationship).
     *
     * @param annotationGUID              unique identifier of the starting element
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getPreviousAnnotations(String       annotationGUID,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        return annotationHandler.getPreviousAnnotations(connectorUserId, annotationGUID, queryOptions);
    }


    /**
     * Return the properties of a specific annotation.
     *
     * @param annotationGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getAnnotationByGUID(String     annotationGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return annotationHandler.getAnnotationByGUID(connectorUserId, annotationGUID, getOptions);
    }


    /**
     * Retrieve the list of annotations metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned annotations include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findAnnotations(String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return annotationHandler.findAnnotations(connectorUserId, searchString, searchOptions);
    }
}
