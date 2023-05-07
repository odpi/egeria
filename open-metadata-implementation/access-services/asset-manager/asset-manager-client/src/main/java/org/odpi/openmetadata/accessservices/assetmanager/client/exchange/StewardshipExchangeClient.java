/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.StewardshipExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetOriginProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.DataFieldQueryProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.DataFieldValuesProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FindAssetOriginProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.FindNameProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceClassificationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.LevelIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.OwnerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RetentionClassificationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SecurityTagsProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SemanticAssignmentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SubjectAreaMemberProperties;
import org.odpi.openmetadata.accessservices.assetmanager.rest.AssetElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceDefinitionsResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.Date;
import java.util.List;


/**
 * StewardshipExchangeClient is the client for assigning relationships and classifications that help govern both metadata and its associated
 * resources.
 */
public class StewardshipExchangeClient extends ExchangeClientBase implements StewardshipExchangeInterface
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipExchangeClient(String   serverName,
                                     String   serverPlatformURLRoot,
                                     AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipExchangeClient(String serverName,
                                     String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
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
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipExchangeClient(String   serverName,
                                     String   serverPlatformURLRoot,
                                     String   userId,
                                     String   password,
                                     AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Create a new client that is going to be used in an OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param restClient client that issues the REST API calls
     * @param maxPageSize maximum number of results supported by this server
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipExchangeClient(String                 serverName,
                                     String                 serverPlatformURLRoot,
                                     AssetManagerRESTClient restClient,
                                     int                    maxPageSize,
                                     AuditLog               auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipExchangeClient(String serverName,
                                     String serverPlatformURLRoot,
                                     String userId,
                                     String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Classify the element to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties descriptive properties for the data field
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setElementAsDataField(String                    userId,
                                      String                    assetManagerGUID,
                                      String                    assetManagerName,
                                      String                    elementGUID,
                                      String                    externalIdentifier,
                                      DataFieldValuesProperties properties,
                                      Date                      effectiveTime,
                                      boolean                   forLineage,
                                      boolean                   forDuplicateProcessing) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "setElementAsDataField";
        final String elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/data-field";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the data field designation from the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearElementAsDataField(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  elementGUID,
                                        String  externalIdentifier,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String   methodName = "clearElementAsDataField";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/data-field/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the elements classified with the DataField classification.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param properties values to match on
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getDataFieldClassifiedElements(String                   userId,
                                                            String                   assetManagerGUID,
                                                            String                   assetManagerName,
                                                            DataFieldQueryProperties properties,
                                                            int                      startFrom,
                                                            int                      pageSize,
                                                            Date                     effectiveTime,
                                                            boolean                  forLineage,
                                                            boolean                  forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        final String methodName = "getDataFieldClassifiedElements";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-data-field";

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           properties,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setConfidenceClassification(String                             userId,
                                            String                             assetManagerGUID,
                                            String                             assetManagerName,
                                            String                             elementGUID,
                                            String                             externalIdentifier,
                                            GovernanceClassificationProperties properties,
                                            Date                               effectiveTime,
                                            boolean                            forLineage,
                                            boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        final String methodName = "setConfidenceClassification";
        final String elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/confidence";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearConfidenceClassification(String  userId,
                                              String  assetManagerGUID,
                                              String  assetManagerName,
                                              String  elementGUID,
                                              String  externalIdentifier,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "clearConfidenceClassification";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/confidence/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getConfidenceClassifiedElements(String  userId,
                                                             String  assetManagerGUID,
                                                             String  assetManagerName,
                                                             boolean returnSpecificLevel,
                                                             int     levelIdentifier,
                                                             int     startFrom,
                                                             int     pageSize,
                                                             Date    effectiveTime,
                                                             boolean forLineage,
                                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName = "getConfidenceClassifiedElements";

        LevelIdentifierProperties properties = new LevelIdentifierProperties();
        properties.setLevelIdentifier(levelIdentifier);
        properties.setReturnSpecificLevel(returnSpecificLevel);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-confidence";

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           properties,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setCriticalityClassification(String                             userId,
                                             String                             assetManagerGUID,
                                             String                             assetManagerName,
                                             String                             elementGUID,
                                             GovernanceClassificationProperties properties,
                                             String                             externalIdentifier,
                                             Date                               effectiveTime,
                                             boolean                            forLineage,
                                             boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        final String methodName = "setCriticalityClassification";
        final String elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/criticality";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearCriticalityClassification(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  elementGUID,
                                               String  externalIdentifier,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String   methodName = "clearCriticalityClassification";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/criticality/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getCriticalityClassifiedElements(String  userId,
                                                              String  assetManagerGUID,
                                                              String  assetManagerName,
                                                              boolean returnSpecificLevel,
                                                              int     levelIdentifier,
                                                              int     startFrom,
                                                              int     pageSize,
                                                              Date    effectiveTime,
                                                              boolean forLineage,
                                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "getCriticalityClassifiedElements";

        LevelIdentifierProperties properties = new LevelIdentifierProperties();
        properties.setLevelIdentifier(levelIdentifier);
        properties.setReturnSpecificLevel(returnSpecificLevel);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-criticality";

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           properties,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }


    /**
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setConfidentialityClassification(String                             userId,
                                                 String                             assetManagerGUID,
                                                 String                             assetManagerName,
                                                 String                             elementGUID,
                                                 String                             externalIdentifier,
                                                 GovernanceClassificationProperties properties,
                                                 Date                               effectiveTime,
                                                 boolean                            forLineage,
                                                 boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        final String methodName = "setConfidentialityClassification";
        final String elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/confidentiality";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearConfidentialityClassification(String  userId,
                                                   String  assetManagerGUID,
                                                   String  assetManagerName,
                                                   String  elementGUID,
                                                   String  externalIdentifier,
                                                   Date    effectiveTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String   methodName = "clearConfidentialityClassification";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/confidentiality/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getConfidentialityClassifiedElements(String  userId,
                                                                  String  assetManagerGUID,
                                                                  String  assetManagerName,
                                                                  boolean returnSpecificLevel,
                                                                  int     levelIdentifier,
                                                                  int     startFrom,
                                                                  int     pageSize,
                                                                  Date    effectiveTime,
                                                                  boolean forLineage,
                                                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getConfidentialityClassifiedElements";

        LevelIdentifierProperties properties = new LevelIdentifierProperties();
        properties.setLevelIdentifier(levelIdentifier);
        properties.setReturnSpecificLevel(returnSpecificLevel);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-confidentiality";

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           properties,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setRetentionClassification(String                            userId,
                                           String                            assetManagerGUID,
                                           String                            assetManagerName,
                                           String                            elementGUID,
                                           String                            externalIdentifier,
                                           RetentionClassificationProperties properties,
                                           Date                              effectiveTime,
                                           boolean                           forLineage,
                                           boolean                           forDuplicateProcessing) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        final String methodName = "setRetentionClassification";
        final String elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/retention";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearRetentionClassification(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  elementGUID,
                                             String  externalIdentifier,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String   methodName = "clearRetentionClassification";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/retention/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param returnSpecificBasisIdentifier should the results be filtered by basisIdentifier?
     * @param basisIdentifier the identifier to filter by (if returnSpecificBasisIdentifier=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getRetentionClassifiedElements(String  userId,
                                                            String  assetManagerGUID,
                                                            String  assetManagerName,
                                                            boolean returnSpecificBasisIdentifier,
                                                            int     basisIdentifier,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            Date    effectiveTime,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "getRetentionClassifiedElements";

        LevelIdentifierProperties properties = new LevelIdentifierProperties();
        properties.setLevelIdentifier(basisIdentifier);
        properties.setReturnSpecificLevel(returnSpecificBasisIdentifier);

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-confidentiality";

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           properties,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }



    /**
     * Add or replace the security tags for an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the security tags
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  addSecurityTags(String                 userId,
                                 String                 assetManagerGUID,
                                 String                 assetManagerName,
                                 String                 elementGUID,
                                 String                 externalIdentifier,
                                 SecurityTagsProperties properties,
                                 Date                   effectiveTime,
                                 boolean                forLineage,
                                 boolean                forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String   methodName = "addSecurityTags";
        final String   elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/security-tags";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID element where the security tags need to be removed.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void clearSecurityTags(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  elementGUID,
                                  String  externalIdentifier,
                                  Date    effectiveTime,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String   methodName = "clearSecurityTags";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/security-tags/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getSecurityTaggedElements(String  userId,
                                                       String  assetManagerGUID,
                                                       String  assetManagerName,
                                                       int     startFrom,
                                                       int     pageSize,
                                                       Date    effectiveTime,
                                                       boolean forLineage,
                                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "getSecurityTaggedElements";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-security-tags";

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           null,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }


    /**
     * Add or replace the ownership classification for an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the ownership
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  addOwnership(String          userId,
                              String          assetManagerGUID,
                              String          assetManagerName,
                              String          elementGUID,
                              String          externalIdentifier,
                              OwnerProperties properties,
                              Date            effectiveTime,
                              boolean         forLineage,
                              boolean         forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String   methodName = "addOwnership";
        final String   elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/ownership";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID element where the classification needs to be removed.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void clearOwnership(String  userId,
                               String  assetManagerGUID,
                               String  assetManagerName,
                               String  elementGUID,
                               String  externalIdentifier,
                               Date    effectiveTime,
                               boolean forLineage,
                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "clearOwnership";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/ownership/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param owner unique identifier for the owner
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getOwnersElements(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  owner,
                                               int     startFrom,
                                               int     pageSize,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "getOwnersElements";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-ownership";

        FindNameProperties properties = new FindNameProperties();

        properties.setName(owner);

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           properties,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }


    /**
     * Add or replace the origin classification for an asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetGUID element to link it to - its type must inherit from Asset.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the origin
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void  addAssetOrigin(String                userId,
                                String                assetManagerGUID,
                                String                assetManagerName,
                                String                assetGUID,
                                String                externalIdentifier,
                                AssetOriginProperties properties,
                                Date                  effectiveTime,
                                boolean               forLineage,
                                boolean               forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String   methodName = "addAssetOrigin";
        final String   elementGUIDParameter = "assetGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/assets/{2}/origin";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             assetGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the origin classification from an asset.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param assetGUID element where the classification needs to be removed.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void clearAssetOrigin(String  userId,
                                 String  assetManagerGUID,
                                 String  assetManagerName,
                                 String  assetGUID,
                                 String  externalIdentifier,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String   methodName = "clearAssetOrigin";
        final String   elementGUIDParameter = "assetGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/assets/{2}/origin/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                assetGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the assets from a specific origin.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param properties values to search on - null means any value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of the assets
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<AssetElement> getAssetsByOrigin(String                    userId,
                                                String                    assetManagerGUID,
                                                String                    assetManagerName,
                                                FindAssetOriginProperties properties,
                                                int                       startFrom,
                                                int                       pageSize,
                                                Date                      effectiveTime,
                                                boolean                   forLineage,
                                                boolean                   forDuplicateProcessing) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException
    {
        final String methodName = "getAssetsByOrigin";
        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/assets/by-origin";
        final String requestParamsURLTemplate = "?startFrom={2}&pageSize={3}&forLineage={4}&forDuplicateProcessing={5}";

        invalidParameterHandler.validateUserId(userId, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        FindByPropertiesRequestBody requestBody = new FindByPropertiesRequestBody();

        requestBody.setAssetManagerGUID(assetManagerGUID);
        requestBody.setAssetManagerName(assetManagerName);
        requestBody.setEffectiveTime(effectiveTime);
        requestBody.setProperties(properties);

        AssetElementsResponse restResult = restClient.callAssetsPostRESTCall(methodName,
                                                                             urlTemplate + requestParamsURLTemplate,
                                                                             requestBody,
                                                                             serverName,
                                                                             userId,
                                                                             startFrom,
                                                                             validatedPageSize,
                                                                             forLineage,
                                                                             forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties qualified name of subject area
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addElementToSubjectArea(String                      userId,
                                        String                      assetManagerGUID,
                                        String                      assetManagerName,
                                        String                      elementGUID,
                                        String                      externalIdentifier,
                                        SubjectAreaMemberProperties properties,
                                        Date                        effectiveTime,
                                        boolean                     forLineage,
                                        boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String   methodName = "addElementToSubjectArea";
        final String   elementGUIDParameter = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/subject-area-member";

        super.setReferenceableClassification(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             elementGUID,
                                             elementGUIDParameter,
                                             externalIdentifier,
                                             properties,
                                             urlTemplate,
                                             effectiveTime,
                                             forLineage,
                                             forDuplicateProcessing,
                                             methodName);
    }


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the equivalent element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeElementFromSubjectArea(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  elementGUID,
                                             String  externalIdentifier,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String   methodName = "removeElementFromSubjectArea";
        final String   elementGUIDParameter = "elementGUID";

        final String   urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/subject-area-member/remove";

        super.removeReferenceableClassification(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                elementGUID,
                                                elementGUIDParameter,
                                                externalIdentifier,
                                                urlTemplate,
                                                effectiveTime,
                                                forLineage,
                                                forDuplicateProcessing,
                                                methodName);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param subjectAreaName unique identifier for the subject area
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getMembersOfSubjectArea(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  subjectAreaName,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getMembersOfSubjectArea";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-subject-area-membership";

        FindNameProperties properties = new FindNameProperties();

        properties.setName(subjectAreaName);

        return super.getClassifiedElements(userId,
                                           assetManagerGUID,
                                           assetManagerName,
                                           properties,
                                           urlTemplate,
                                           startFrom,
                                           pageSize,
                                           effectiveTime,
                                           forLineage,
                                           forDuplicateProcessing,
                                           methodName);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void setupSemanticAssignment(String                       userId,
                                        String                       assetManagerGUID,
                                        String                       assetManagerName,
                                        String                       elementGUID,
                                        String                       glossaryTermGUID,
                                        SemanticAssignmentProperties properties,
                                        Date                         effectiveTime,
                                        boolean                      forLineage,
                                        boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        final String methodName                     = "setupSemanticAssignment";
        final String elementGUIDParameterName       = "elementGUID";
        final String glossaryTermGUIDParameterName  = "glossaryTermGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/semantic-assignment/terms/{3}";

        super.setupRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                elementGUIDParameterName,
                                properties,
                                glossaryTermGUID,
                                glossaryTermGUIDParameterName,
                                urlTemplate,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void clearSemanticAssignment(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  elementGUID,
                                        String  glossaryTermGUID,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                     = "clearSemanticAssignment";
        final String elementGUIDParameterName       = "elementGUID";
        final String glossaryTermGUIDParameterName  = "glossaryTermGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/semantic-assignment/terms/{3}/remove";

        super.clearRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                elementGUIDParameterName,
                                glossaryTermGUID,
                                glossaryTermGUIDParameterName,
                                urlTemplate,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryTermElement> getMeanings(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  elementGUID,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName = "getMeanings";
        final String elementGUIDParameterName = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/glossaries/terms/by-semantic-assignment/{2}?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GlossaryTermElementsResponse restResult = restClient.callGlossaryTermsPostRESTCall(methodName,
                                                                                           urlTemplate,
                                                                                           getEffectiveTimeQueryRequestBody(assetManagerGUID, assetManagerName, effectiveTime),
                                                                                           serverName,
                                                                                           userId,
                                                                                           elementGUID,
                                                                                           startFrom,
                                                                                           validatedPageSize,
                                                                                           forLineage,
                                                                                           forDuplicateProcessing);

        return restResult.getElementList();
    }


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getSemanticAssignees(String  userId,
                                                     String  assetManagerGUID,
                                                     String  assetManagerName,
                                                     String  glossaryTermGUID,
                                                     int     startFrom,
                                                     int     pageSize,
                                                     Date    effectiveTime,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "getSemanticAssignees";
        final String elementGUIDParameterName = "glossaryTermGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/by-semantic-assignment/{2}";

        return super.getRelatedElements(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        glossaryTermGUID,
                                        elementGUIDParameterName,
                                        urlTemplate,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        forLineage,
                                        forDuplicateProcessing,
                                        methodName);
    }



    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addGovernanceDefinitionToElement(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  definitionGUID,
                                                 String  elementGUID,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                   = "addGovernanceDefinitionToElement";
        final String elementGUIDParameterName     = "elementGUID";
        final String definitionGUIDParameterName  = "definitionGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/governed-by/definition/{3}";

        super.setupRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                elementGUIDParameterName,
                                null,
                                definitionGUID,
                                definitionGUIDParameterName,
                                urlTemplate,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeGovernanceDefinitionFromElement(String  userId,
                                                      String  assetManagerGUID,
                                                      String  assetManagerName,
                                                      String  definitionGUID,
                                                      String  elementGUID,
                                                      Date    effectiveTime,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName                   = "removeGovernanceDefinitionFromElement";
        final String elementGUIDParameterName     = "elementGUID";
        final String definitionGUIDParameterName  = "definitionGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/governed-by/definition/{3}/remove";

        super.clearRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                elementGUIDParameterName,
                                definitionGUID,
                                definitionGUIDParameterName,
                                urlTemplate,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GovernanceDefinitionElement> getGovernedByDefinitions(String  userId,
                                                                      String  assetManagerGUID,
                                                                      String  assetManagerName,
                                                                      String  elementGUID,
                                                                      int     startFrom,
                                                                      int     pageSize,
                                                                      Date    effectiveTime,
                                                                      boolean forLineage,
                                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        final String methodName = "getGovernedByDefinitions";
        final String elementGUIDParameterName = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/governed-by";

        final String requestParamsURLTemplate = "?startFrom={3}&pageSize={4}&forLineage={5}&forDuplicateProcessing={6}";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        int validatedPageSize = invalidParameterHandler.validatePaging(startFrom, pageSize, methodName);

        GovernanceDefinitionsResponse restResult = restClient.callGovernanceDefinitionsPostRESTCall(methodName,
                                                                                                    urlTemplate + requestParamsURLTemplate,
                                                                                                    getEffectiveTimeQueryRequestBody(assetManagerGUID,
                                                                                                                                     assetManagerName,
                                                                                                                                     effectiveTime),
                                                                                                    serverName,
                                                                                                    userId,
                                                                                                    elementGUID,
                                                                                                    startFrom,
                                                                                                    validatedPageSize,
                                                                                                    forLineage,
                                                                                                    forDuplicateProcessing);

        return restResult.getElements();
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param governanceDefinitionGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getGovernedElements(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  governanceDefinitionGUID,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    Date    effectiveTime,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getGovernedElements";
        final String elementGUIDParameterName = "governanceDefinitionGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/governed-by/{2}";

        return super.getRelatedElements(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        governanceDefinitionGUID,
                                        elementGUIDParameterName,
                                        urlTemplate,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        forLineage,
                                        forDuplicateProcessing,
                                        methodName);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getSourceElements(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  elementGUID,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    Date    effectiveTime,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getSourceElements";
        final String elementGUIDParameterName = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/sourced-from/{2}";

        return super.getRelatedElements(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        urlTemplate,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        forLineage,
                                        forDuplicateProcessing,
                                        methodName);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getElementsSourceFrom(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  elementGUID,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    Date    effectiveTime,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "getElementsSourceFrom";
        final String elementGUIDParameterName = "elementGUID";

        final String urlTemplate = serverPlatformURLRoot + urlTemplatePrefix + "/elements/{2}/sourced-from";

        return super.getRelatedElements(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        elementGUID,
                                        elementGUIDParameterName,
                                        urlTemplate,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        forLineage,
                                        forDuplicateProcessing,
                                        methodName);
    }
}
