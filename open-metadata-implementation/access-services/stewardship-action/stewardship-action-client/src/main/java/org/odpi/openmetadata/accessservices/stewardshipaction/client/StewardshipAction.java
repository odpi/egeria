/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.client;

import org.odpi.openmetadata.accessservices.stewardshipaction.api.DuplicateManagementInterface;
import org.odpi.openmetadata.accessservices.stewardshipaction.api.SurveyReportInterface;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.surveyaction.converters.SurveyReportConverter;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DuplicateElement;
import org.odpi.openmetadata.accessservices.stewardshipaction.client.rest.StewardshipActionRESTClient;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.surveyaction.AnnotationStore;
import org.odpi.openmetadata.frameworks.surveyaction.properties.Annotation;
import org.odpi.openmetadata.frameworks.surveyaction.properties.SurveyReport;

import java.util.*;


/**
 * StewardshipAction provides the generic client-side interface for the Stewardship Action Open Metadata Access Service (OMAS).
 * This client is initialized with the URL and name of the server that is running the Stewardship Action OMAS.
 */
public class StewardshipAction implements SurveyReportInterface,
                                          DuplicateManagementInterface

{
    private final String   serverName;               /* Initialized in constructor */
    private final String   serverPlatformURLRoot;    /* Initialized in constructor */
    private       AuditLog auditLog;                 /* Initialized in constructor */

    private static final NullRequestBody nullRequestBody = new NullRequestBody();

    private final InvalidParameterHandler     invalidParameterHandler = new InvalidParameterHandler();
    private final StewardshipActionRESTClient restClient;               /* Initialized in constructor */

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final SurveyReportConverter<SurveyReport> surveyReportConverter;

    private final OpenMetadataStoreClient      openMetadataStoreClient;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipAction(String   serverName,
                             String   serverPlatformURLRoot,
                             AuditLog auditLog,
                             int      maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);
        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
        this.surveyReportConverter   = new SurveyReportConverter<>(propertyHelper,
                                                                   AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                   serverName);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipAction(String serverName,
                             String serverPlatformURLRoot,
                             int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = null;

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
        this.surveyReportConverter   = new SurveyReportConverter<>(propertyHelper,
                                                                   AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                   serverName);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipAction(String   serverName,
                             String   serverPlatformURLRoot,
                             String   userId,
                             String   password,
                             AuditLog auditLog,
                             int      maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
        this.surveyReportConverter   = new SurveyReportConverter<>(propertyHelper,
                                                                   AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                   serverName);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipAction(String serverName,
                             String serverPlatformURLRoot,
                             String userId,
                             String password,
                             int    maxPageSize) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = null;

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
        this.surveyReportConverter   = new SurveyReportConverter<>(propertyHelper,
                                                                   AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                   serverName);

        this.restClient = new StewardshipActionRESTClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server (view service, engine service or
     * integration service typically).
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipAction(String                      serverName,
                             String                      serverPlatformURLRoot,
                             StewardshipActionRESTClient restClient,
                             int                         maxPageSize,
                             AuditLog                    auditLog) throws InvalidParameterException
    {
        final String methodName = "Client Constructor";

        invalidParameterHandler.setMaxPagingSize(maxPageSize);
        invalidParameterHandler.validateOMAGServerPlatformURL(serverPlatformURLRoot, serverName, methodName);

        this.serverName = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.auditLog = auditLog;

        this.openMetadataStoreClient = new OpenMetadataStoreClient(serverName, serverPlatformURLRoot, maxPageSize);
        this.surveyReportConverter   = new SurveyReportConverter<>(propertyHelper,
                                                                   AccessServiceDescription.STEWARDSHIP_ACTION_OMAS.getAccessServiceFullName(),
                                                                   serverName);
        this.restClient = restClient;
    }


    /*
     * ==============================================
     * Survey Reports
     * ==============================================
     */


    /**
     * Return the survey reports linked to the asset.
     *
     * @param userId    calling user
     * @param assetGUID unique identifier of the asset
     * @param startFrom position in the list (used when there are so many reports that paging is needed)
     * @param pageSize  maximum number of elements to return an this call
     * @return list of discovery analysis reports
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    there was a problem that occurred within the property server.
     */
    @Override
    public List<SurveyReport> getSurveyReports(String userId,
                                               String assetGUID,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String methodName = "getSurveyReports";
        final String elementGUIDParameter = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, elementGUIDParameter, methodName);

        List<RelatedMetadataElement> reportElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                         assetGUID,
                                                                                                         1,
                                                                                                         OpenMetadataType.ASSET_SURVEY_REPORT_RELATIONSHIP.typeName,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                                         false,
                                                                                                         false,
                                                                                                         new Date(),
                                                                                                         startFrom,
                                                                                                         pageSize);

        if (reportElements != null)
        {
            List<SurveyReport> surveyReports = new ArrayList<>();

            for (RelatedMetadataElement reportElement : reportElements)
            {
                if (reportElement != null)
                {
                    List<RelatedMetadataElement> engineActionElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                           reportElement.getElement().getElementGUID(),
                                                                                                                           2,
                                                                                                                           OpenMetadataType.ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP.typeName,
                                                                                                                           null,
                                                                                                                           null,
                                                                                                                           null,
                                                                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                           false,
                                                                                                                           false,
                                                                                                                           new Date(),
                                                                                                                           0,
                                                                                                                           0);

                    surveyReports.add(surveyReportConverter.getSurveyReport(SurveyReport.class,
                                                                            reportElement.getElement(),
                                                                            engineActionElements,
                                                                            methodName));
                }
            }

            if (! surveyReports.isEmpty())
            {
                return surveyReports;
            }
        }

        return null;
    }


    /**
     * Return the annotations linked directly to the report.
     *
     * @param userId           identifier of calling user
     * @param reportGUID       identifier of the discovery request.
     * @param startFrom        initial position in the stored list.
     * @param pageSize         maximum number of definitions to return on this call.
     * @return list of annotations
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    there was a problem that occurred within the property server.
     */
    @Override
    public List<Annotation> getSurveyReportAnnotations(String           userId,
                                                       String           reportGUID,
                                                       int              startFrom,
                                                       int              pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getSurveyReportAnnotations";
        final String elementGUIDParameter = "reportGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(reportGUID, elementGUIDParameter, methodName);

        AnnotationStore annotationStore = new AnnotationStore(userId, openMetadataStoreClient, reportGUID, null, null);

        return annotationStore.getNewAnnotations(startFrom, pageSize);
    }


    /**
     * Return any annotations attached to this annotation.
     *
     * @param userId           identifier of calling user
     * @param annotationGUID   anchor annotation
     * @param startFrom        starting position in the list
     * @param pageSize         maximum number of annotations that can be returned.
     * @return list of Annotation objects
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException    there was a problem that occurred within the property server.
     */
    @Override
    public List<Annotation> getExtendedAnnotations(String           userId,
                                                   String           annotationGUID,
                                                   int              startFrom,
                                                   int              pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        AnnotationStore annotationStore = new AnnotationStore(userId, openMetadataStoreClient, null, null, null);

        return annotationStore.getExtendedAnnotations(annotationGUID, startFrom, pageSize);
    }


    /*
     * ==============================================
     * DuplicateManagementInterface
     * ==============================================
     */

    /**
     * Create a simple relationship between two elements.  These elements must be of the same type.  If the relationship already exists,
     * the properties are updated.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     *
     * @throws InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  linkElementsAsDuplicates(String userId,
                                          String element1GUID,
                                          String element2GUID,
                                          int    statusIdentifier,
                                          String steward,
                                          String stewardTypeName,
                                          String stewardPropertyName,
                                          String source,
                                          String notes) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String methodName = "linkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/peer-duplicate-of/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        DuplicatesRequestBody requestBody = new DuplicatesRequestBody();

        requestBody.setStatusIdentifier(statusIdentifier);
        requestBody.setSteward(steward);
        requestBody.setStewardTypeName(stewardTypeName);
        requestBody.setStewardPropertyName(stewardPropertyName);
        requestBody.setSource(source);
        requestBody.setNotes(notes);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        element1GUID,
                                        element2GUID);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  unlinkElementsAsDuplicates(String userId,
                                            String element1GUID,
                                            String element2GUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "unlinkElementsAsDuplicates";

        final String element1GUIDParameter = "element1GUID";
        final String element2GUIDParameter = "element2GUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/peer-duplicate-of/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(element1GUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(element2GUID, element2GUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        element1GUID,
                                        element2GUID);
    }


    /**
     * Classify an element as a known duplicate.  This will mean that it is included in duplicate processing during metadata retrieval requests.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  markElementAsKnownDuplicate(String userId,
                                             String elementGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "markElementAsKnownDuplicate";

        final String elementGUIDParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/known-duplicate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);

        
        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * Remove the classification that identifies this element as a known duplicate.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  unmarkElementAsKnownDuplicate(String userId,
                                               String elementGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "unmarkElementAsKnownDuplicate";

        final String elementGUIDParameter = "elementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/known-duplicate/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameter, methodName);


        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        elementGUID);
    }


    /**
     * List the elements that are linked as peer duplicates to the requested element.
     *
     * @param userId calling user
     * @param elementGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of linked duplicates
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<DuplicateElement> getPeerDuplicates(String userId,
                                                    String elementGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String methodName        = "getPeerDuplicates";
        final String guidParameterName = "elementGUID";
        final String urlTemplate       = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/peer-duplicates?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        DuplicatesResponse restResult = restClient.callDuplicatesGetRESTCall(methodName,
                                                                             urlTemplate,
                                                                             serverName,
                                                                             userId,
                                                                             elementGUID,
                                                                             startFrom,
                                                                             pageSize);

        return restResult.getElements();
    }


    /**
     * Mark an element as a consolidated duplicate (or update the properties if it is already marked as such).
     * This method assumes that a standard create method has been used to create the element first using the values from contributing elements.
     * It is just adding the ConsolidatedDuplicate classification to the element.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of the element that contains the consolidated information from a collection of elements
     *                                  that are all duplicates of one another.
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  markAsConsolidatedDuplicate(String       userId,
                                             String       consolidatedDuplicateGUID,
                                             int          statusIdentifier,
                                             String       steward,
                                             String       stewardTypeName,
                                             String       stewardPropertyName,
                                             String       source,
                                             String       notes) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String methodName = "markAsConsolidatedDuplicate";

        final String element1GUIDParameter = "consolidatedDuplicateGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/consolidated-duplicate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedDuplicateGUID, element1GUIDParameter, methodName);

        DuplicatesRequestBody requestBody = new DuplicatesRequestBody();

        requestBody.setStatusIdentifier(statusIdentifier);
        requestBody.setSteward(steward);
        requestBody.setStewardTypeName(stewardTypeName);
        requestBody.setStewardPropertyName(stewardPropertyName);
        requestBody.setSource(source);
        requestBody.setNotes(notes);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        requestBody,
                                        serverName,
                                        userId,
                                        consolidatedDuplicateGUID);
    }


    /**
     * Create a ConsolidatedDuplicateLink relationship between the consolidated duplicate element and one of its contributing element.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  linkElementToConsolidatedDuplicate(String userId,
                                                    String consolidatedDuplicateGUID,
                                                    String contributingElementGUID) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "linkElementToConsolidatedDuplicate";

        final String element1GUIDParameter = "consolidatedDuplicateGUID";
        final String element2GUIDParameter = "contributingElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/consolidated-duplicate/{2}/contributing-element/{3}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedDuplicateGUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(contributingElementGUID, element2GUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        consolidatedDuplicateGUID,
                                        contributingElementGUID);
    }


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  unlinkElementFromConsolidatedDuplicate(String userId,
                                                        String consolidatedDuplicateGUID,
                                                        String contributingElementGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "unlinkElementFromConsolidatedDuplicate";

        final String element1GUIDParameter = "consolidatedDuplicateGUID";
        final String element2GUIDParameter = "contributingElementGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/consolidated-duplicate/{2}/contributing-elements/{3}/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedDuplicateGUID, element1GUIDParameter, methodName);
        invalidParameterHandler.validateGUID(contributingElementGUID, element2GUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        consolidatedDuplicateGUID,
                                        contributingElementGUID);
    }


    /**
     * List the elements that are contributing to a consolidating duplicate element.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of contributing duplicates
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getContributingDuplicates(String userId,
                                                       String consolidatedDuplicateGUID,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName        = "getContributingDuplicates";
        final String guidParameterName = "consolidatedDuplicateGUID";
        final String urlTemplate       = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/consolidated-duplicate/{2}/contributing-elements?startFrom={3}&pageSize={4}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedDuplicateGUID, guidParameterName, methodName);

        ElementStubsResponse restResult = restClient.callElementStubsGetRESTCall(methodName,
                                                                                 urlTemplate,
                                                                                 serverName,
                                                                                 userId,
                                                                                 consolidatedDuplicateGUID,
                                                                                 startFrom,
                                                                                 pageSize);

        return restResult.getElements();
    }


    /**
     * Return details of the consolidated duplicate for a requested element.
     *
     * @param userId calling user
     * @param elementGUID element to query
     *
     * @return header of consolidated duplicated or null if none
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public ElementStub getConsolidatedDuplicate(String userId,
                                                String elementGUID) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String methodName        = "getConsolidatedDuplicate";
        final String guidParameterName = "elementGUID";
        final String urlTemplate       = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/consolidated-duplicate";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, guidParameterName, methodName);

        ElementStubResponse restResult = restClient.callElementStubGetRESTCall(methodName,
                                                                               urlTemplate,
                                                                               serverName,
                                                                               userId,
                                                                               elementGUID);

        return restResult.getElement();
    }


    /**
     * Remove the consolidated duplicate element and the links to the elements that contributed to its values.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of element to remove
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  removeConsolidatedDuplicate(String userId,
                                             String consolidatedDuplicateGUID) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "removeConsolidatedDuplicate";

        final String element1GUIDParameter = "consolidatedDuplicateGUID";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/stewardship-action/users/{1}/elements/{2}/consolidated-duplicate/delete";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(consolidatedDuplicateGUID, element1GUIDParameter, methodName);

        restClient.callVoidPostRESTCall(methodName,
                                        urlTemplate,
                                        nullRequestBody,
                                        serverName,
                                        userId,
                                        consolidatedDuplicateGUID);
    }

}
