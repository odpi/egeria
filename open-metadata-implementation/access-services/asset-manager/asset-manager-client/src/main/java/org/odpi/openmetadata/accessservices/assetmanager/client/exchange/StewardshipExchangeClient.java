/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.api.exchange.StewardshipExchangeInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.AssetElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.rest.AssetElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.FindByPropertiesRequestBody;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GlossaryTermElementsResponse;
import org.odpi.openmetadata.accessservices.assetmanager.rest.GovernanceDefinitionsResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermAssignmentStatus;
import org.odpi.openmetadata.frameworks.openmetadata.enums.SequencingOrder;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataRelationshipSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindNameProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindPropertyNamesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateFilter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
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
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipExchangeClient(String   serverName,
                                     String   serverPlatformURLRoot,
                                     AuditLog auditLog,
                                     int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog, maxPageSize);
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
    public StewardshipExchangeClient(String serverName,
                                     String serverPlatformURLRoot,
                                     int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize);
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
    public StewardshipExchangeClient(String   serverName,
                                     String   serverPlatformURLRoot,
                                     String   userId,
                                     String   password,
                                     AuditLog auditLog,
                                     int      maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog, maxPageSize);
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
     * @param maxPageSize maximum value allowed for page size
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipExchangeClient(String serverName,
                                     String serverPlatformURLRoot,
                                     String userId,
                                     String password,
                                     int    maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize);
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
     * @param elementGUID unique identifier of the metadata element to declassify
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
    public List<MetadataElementSummary> getConfidenceClassifiedElements(String  userId,
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

        if (returnSpecificLevel)
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                               null,
                                               levelIdentifier,
                                               OpenMetadataProperty.CONFIDENCE_LEVEL_IDENTIFIER.name,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
        else
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.CONFIDENCE_CLASSIFICATION.typeName,
                                               null,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
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
     * @param elementGUID unique identifier of the metadata element to declassify
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
    public List<MetadataElementSummary> getCriticalityClassifiedElements(String  userId,
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

        if (returnSpecificLevel)
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                               null,
                                               levelIdentifier,
                                               OpenMetadataProperty.CRITICALITY_LEVEL_IDENTIFIER.name,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
        else
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName,
                                               null,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
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
     * @param elementGUID unique identifier of the metadata element to declassify
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
    public List<MetadataElementSummary> getConfidentialityClassifiedElements(String  userId,
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

        if (returnSpecificLevel)
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                               null,
                                               levelIdentifier,
                                               OpenMetadataProperty.CONFIDENTIALITY_LEVEL_IDENTIFIER.name,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
        else
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                               null,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
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
     * @param elementGUID unique identifier of the metadata element to declassify
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
    public List<MetadataElementSummary> getRetentionClassifiedElements(String  userId,
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

        if (returnSpecificBasisIdentifier)
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                               null,
                                               basisIdentifier,
                                               OpenMetadataProperty.RETENTION_BASIS_IDENTIFIER.name,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
        else
        {
            return super.getClassifiedElements(userId,
                                               OpenMetadataType.RETENTION_CLASSIFICATION.typeName,
                                               null,
                                               startFrom,
                                               pageSize,
                                               effectiveTime,
                                               forLineage,
                                               forDuplicateProcessing,
                                               methodName);
        }
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
    public List<MetadataElementSummary> getSecurityTaggedElements(String  userId,
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

        return super.getClassifiedElements(userId,
                                           OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                           null,
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
                              OwnershipProperties properties,
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
    public List<MetadataElementSummary> getOwnersElements(String  userId,
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

        FindNameProperties properties = new FindNameProperties();

        properties.setName(owner);

        return super.getClassifiedElements(userId,
                                           OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                           null,
                                           owner,
                                           OpenMetadataProperty.OWNER.name,
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

        AssetElementsResponse restResult = restClient.callMyAssetsPostRESTCall(methodName,
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
    public void addElementToSubjectArea(String                              userId,
                                        String                              assetManagerGUID,
                                        String                              assetManagerName,
                                        String                              elementGUID,
                                        String                              externalIdentifier,
                                        SubjectAreaClassificationProperties properties,
                                        Date                                effectiveTime,
                                        boolean                             forLineage,
                                        boolean                             forDuplicateProcessing) throws InvalidParameterException,
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
    public List<MetadataElementSummary> getMembersOfSubjectArea(String  userId,
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

        FindNameProperties properties = new FindNameProperties();

        properties.setName(subjectAreaName);

        return super.getClassifiedElements(userId,
                                           OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName,
                                           null,
                                           subjectAreaName,
                                           OpenMetadataProperty.SUBJECT_AREA_NAME.name,
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
     * @param properties properties for the relationship
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
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        if (properties != null)
        {
            super.setupRelationship(userId,
                                    assetManagerGUID,
                                    assetManagerName,
                                    elementGUID,
                                    elementGUIDParameterName,
                                    OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                    relationshipTypeNameParameterName,
                                    glossaryTermGUID,
                                    glossaryTermGUIDParameterName,
                                    properties.getEffectiveFrom(),
                                    properties.getEffectiveTo(),
                                    this.getElementProperties(properties),
                                    effectiveTime,
                                    forLineage,
                                    forDuplicateProcessing,
                                    methodName);
        }
        else
        {
            super.setupRelationship(userId,
                                    assetManagerGUID,
                                    assetManagerName,
                                    elementGUID,
                                    elementGUIDParameterName,
                                    OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                    relationshipTypeNameParameterName,
                                    glossaryTermGUID,
                                    glossaryTermGUIDParameterName,
                                    null,
                                    null,
                                    this.getElementProperties(properties),
                                    effectiveTime,
                                    forLineage,
                                    forDuplicateProcessing,
                                    methodName);
        }
    }



    /**
     * Convert a bean into its element properties.
     *
     * @param relationshipProperties bean properties
     * @return element properties
     */
    private ElementProperties getElementProperties(SemanticAssignmentProperties relationshipProperties)
    {
        if (relationshipProperties != null)
        {
            ElementProperties elementProperties = propertyHelper.addStringProperty(null,
                                                                                   OpenMetadataProperty.EXPRESSION.name,
                                                                                   relationshipProperties.getExpression());

            elementProperties = propertyHelper.addIntProperty(elementProperties,
                                                              OpenMetadataProperty.CONFIDENCE.name,
                                                              relationshipProperties.getConfidence());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.DESCRIPTION.name,
                                                                 relationshipProperties.getDescription());

            if (relationshipProperties.getStatus() != null)
            {
                elementProperties = propertyHelper.addEnumProperty(elementProperties,
                                                                   OpenMetadataProperty.STATUS.name,
                                                                   GlossaryTermAssignmentStatus.getOpenTypeName(),
                                                                   relationshipProperties.getStatus().getName());
            }

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD.name,
                                                                 relationshipProperties.getSteward());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                 relationshipProperties.getStewardTypeName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                 relationshipProperties.getStewardPropertyName());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.SOURCE.name,
                                                                 relationshipProperties.getSource());

            elementProperties = propertyHelper.addStringProperty(elementProperties,
                                                                 OpenMetadataProperty.NOTES.name,
                                                                 relationshipProperties.getNotes());

            return elementProperties;
        }

        return null;
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
        final String methodName            = "clearSemanticAssignment";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "nestedDataFieldGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(glossaryTermGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                             elementGUID,
                                                             glossaryTermGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
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

        GlossaryTermElementsResponse restResult = restClient.callMyGlossaryTermsPostRESTCall(methodName,
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
    public List<RelatedMetadataElementSummary> getSemanticAssignees(String  userId,
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

        invalidParameterHandler.validateGUID(glossaryTermGUID, elementGUIDParameterName, methodName);

        return super.getRelatedElements(userId,
                                        glossaryTermGUID,
                                        2,
                                        OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
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
     * @param elementGUID unique identifier of the metadata element to link
     * @param definitionGUID identifier of the governance definition to link
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
                                                 String  elementGUID,
                                                 String  definitionGUID,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName                   = "addGovernanceDefinitionToElement";
        final String elementGUIDParameterName     = "elementGUID";
        final String definitionGUIDParameterName  = "definitionGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        super.setupRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                elementGUIDParameterName,
                                OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                relationshipTypeNameParameterName,
                                definitionGUID,
                                definitionGUIDParameterName,
                                null,
                                null,
                                null,
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
     * @param elementGUID unique identifier of the metadata element to update
     * @param definitionGUID identifier of the governance definition to link
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
                                                      String  elementGUID,
                                                      String  definitionGUID,
                                                      Date    effectiveTime,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName            = "removeGovernanceDefinitionFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "definitionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(definitionGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                             elementGUID,
                                                             definitionGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }




    /**
     * Link a stakeholder to an element using the Stakeholder relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID identifier of the stakeholder to link
     * @param stakeholderGUID unique identifier of the metadata element to link
     * @param stakeholderRole role of the stakeholder
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addStakeholderToElement(String  userId,
                                        String  assetManagerGUID,
                                        String  assetManagerName,
                                        String  elementGUID,
                                        String  stakeholderGUID,
                                        String  stakeholderRole,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName                   = "addStakeholderToElement";
        final String elementGUIDParameterName     = "elementGUID";
        final String stakeholderGUIDParameterName  = "stakeholderGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        super.setupRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                elementGUIDParameterName,
                                OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName,
                                relationshipTypeNameParameterName,
                                stakeholderGUID,
                                stakeholderGUIDParameterName,
                                null,
                                null,
                                null,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


    /**
     * Remove the Stakeholder relationship between a stakeholder (typically Actor) and an element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param stakeholderGUID identifier of the governance definition to link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeStakeholderFromElement(String  userId,
                                             String  assetManagerGUID,
                                             String  assetManagerName,
                                             String  elementGUID,
                                             String  stakeholderGUID,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName            = "removeStakeholderFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "stakeholderGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(stakeholderGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             OpenMetadataType.STAKEHOLDER_RELATIONSHIP.typeName,
                                                             elementGUID,
                                                             stakeholderGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }




    /**
     * Link a scope to an element using the ScopedBy relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID unique identifier of the metadata element to link
     * @param scopeGUID identifier of the governance definition to link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addScopeToElement(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  elementGUID,
                                  String  scopeGUID,
                                  Date    effectiveTime,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String methodName            = "addScopeToElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "scopeGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(scopeGUID, end2GUIDParameterName, methodName);

        super.setupRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                end1GUIDParameterName,
                                OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                relationshipTypeNameParameterName,
                                scopeGUID,
                                end2GUIDParameterName,
                                null,
                                null,
                                null,
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
     * @param elementGUID unique identifier of the metadata element to update
     * @param scopeGUID identifier of the governance definition to link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeScopeFromElement(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  elementGUID,
                                       String  scopeGUID,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName            = "removeScopeFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "scopeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(scopeGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                             elementGUID,
                                                             scopeGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }


    /**
     * Link an element to another element using the MoreInformation relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID identifier of the governance definition to link
     * @param moreInformationGUID unique identifier of the metadata element to link
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void addMoreInformationToElement(String  userId,
                                            String  assetManagerGUID,
                                            String  assetManagerName,
                                            String  elementGUID,
                                            String  moreInformationGUID,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String methodName                   = "addMoreInformationToElement";
        final String elementGUIDParameterName     = "elementGUID";
        final String moreInformationGUIDParameterName  = "moreInformationGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        super.setupRelationship(userId,
                                assetManagerGUID,
                                assetManagerName,
                                elementGUID,
                                elementGUIDParameterName,
                                OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                relationshipTypeNameParameterName,
                                moreInformationGUID,
                                moreInformationGUIDParameterName,
                                null,
                                null,
                                null,
                                effectiveTime,
                                forLineage,
                                forDuplicateProcessing,
                                methodName);
    }


    /**
     * Remove the MoreInformation relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param elementGUID identifier of the governance definition to link
     * @param moreInformationGUID unique identifier of the metadata element to update
     * @param effectiveTime the time that the retrieved elements must be effective for
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public void removeMoreInformationFromElement(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  elementGUID,
                                                 String  moreInformationGUID,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        final String methodName            = "removeMoreInformationFromElement";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "moreInformationGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(moreInformationGUID, end2GUIDParameterName, methodName);

        openMetadataStoreClient.detachRelatedElementsInStore(userId,
                                                             assetManagerGUID,
                                                             assetManagerName,
                                                             OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                                             elementGUID,
                                                             moreInformationGUID,
                                                             forLineage,
                                                             forDuplicateProcessing,
                                                             effectiveTime);
    }

    /**
     * Retrieve the elements linked via a "MoreInformation" relationship to the requested element.
     *
     * @param userId                 calling user
     * @param assetManagerGUID       unique identifier of software capability representing the caller
     * @param assetManagerName       unique name of software capability representing the caller
     * @param elementGUID            unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom              index of the list to start from (0 for start)
     * @param pageSize               maximum number of elements to return.
     * @param effectiveTime          the time that the retrieved elements must be effective for
     * @param forLineage             return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedMetadataElementSummary> getMoreInformationForElements(String userId, String assetManagerGUID, String assetManagerName, String elementGUID, int startFrom, int pageSize, Date effectiveTime, boolean forLineage, boolean forDuplicateProcessing) throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException
    {
        return null;
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

        GovernanceDefinitionsResponse restResult = restClient.callMyGovernanceDefinitionsPostRESTCall(methodName,
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
    public List<RelatedMetadataElementSummary> getGovernedElements(String  userId,
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

        invalidParameterHandler.validateGUID(governanceDefinitionGUID, elementGUIDParameterName, methodName);

        return super.getRelatedElements(userId,
                                        governanceDefinitionGUID,
                                        2,
                                        OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
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
    public List<RelatedMetadataElementSummary> getSourceElements(String  userId,
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

        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return super.getRelatedElements(userId,
                                        elementGUID,
                                        1,
                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
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
    public List<RelatedMetadataElementSummary> getElementsSourceFrom(String  userId,
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

        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        return super.getRelatedElements(userId,
                                        elementGUID,
                                        2,
                                        OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                        startFrom,
                                        pageSize,
                                        effectiveTime,
                                        forLineage,
                                        forDuplicateProcessing,
                                        methodName);
    }



    /**
     * Create a link between a license type and an element that has achieved the license.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID unique identifier of the element
     * @param licenseTypeGUID unique identifier of the license type
     * @param licenseGUID  unique identifier of the license (maybe from an external system)
     * @param start when did the license start
     * @param end when will the license end
     * @param conditions any conditions added to the license
     * @param licensedBy unique name/identifier of the element for the person/organization licensing the element
     * @param licensedByTypeName type of the licensedBy element
     * @param licensedByPropertyName property name for the unique identifier from the licensedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the license status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param licensee unique name/identifier of the element for the person/organization receiving the license
     * @param licenseeTypeName type of the licensee element
     * @param licenseePropertyName property name for the unique identifier from the licensee element
     * @param notes additional information, endorsements etc
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return guid of license relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String licenseElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  elementGUID,
                                 String  licenseTypeGUID,
                                 String  licenseGUID,
                                 Date    start,
                                 Date    end,
                                 String  conditions,
                                 String  licensedBy,
                                 String  licensedByTypeName,
                                 String  licensedByPropertyName,
                                 String  custodian,
                                 String  custodianTypeName,
                                 String  custodianPropertyName,
                                 String  licensee,
                                 String  licenseeTypeName,
                                 String  licenseePropertyName,
                                 String  notes,
                                 Date    effectiveFrom,
                                 Date    effectiveTo,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        return openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                                    elementGUID,
                                                                    licenseTypeGUID,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveFrom,
                                                                    effectiveTo,
                                                                    getLicenseProperties(licenseGUID,
                                                                                         start,
                                                                                         end,
                                                                                         conditions,
                                                                                         licensedBy,
                                                                                         licensedByTypeName,
                                                                                         licensedByPropertyName,
                                                                                         custodian,
                                                                                         custodianTypeName,
                                                                                         custodianPropertyName,
                                                                                         licensee,
                                                                                         licenseeTypeName,
                                                                                         licenseePropertyName,
                                                                                         notes),
                                                                    effectiveTime);
    }


    /**
     * Update the license relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param licenseGUID unique identifier for the relationship
     * @param licenseId  unique identifier of the license (maybe from an external system)
     * @param start when did the license start
     * @param end when will the license end
     * @param conditions any conditions added to the license
     * @param licensedBy unique name/identifier of the element for the person/organization licensing the element
     * @param licensedByTypeName type of the licensedBy element
     * @param licensedByPropertyName property name for the unique identifier from the licensedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the license status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param licensee unique name/identifier of the element for the person/organization receiving the license
     * @param licenseeTypeName type of the licensee element
     * @param licenseePropertyName property name for the unique identifier from the licensee element
     * @param notes additional information, endorsements etc
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateLicense(String  userId,
                              String  externalSourceGUID,
                              String  externalSourceName,
                              String  licenseGUID,
                              String  licenseId,
                              Date    start,
                              Date    end,
                              String  conditions,
                              String  licensedBy,
                              String  licensedByTypeName,
                              String  licensedByPropertyName,
                              String  custodian,
                              String  custodianTypeName,
                              String  custodianPropertyName,
                              String  licensee,
                              String  licenseeTypeName,
                              String  licenseePropertyName,
                              String  notes,
                              boolean replaceAllProperties,
                              boolean forLineage,
                              boolean forDuplicateProcessing,
                              Date    effectiveTime) throws InvalidParameterException,
                                                         UserNotAuthorizedException,
                                                         PropertyServerException
    {
        openMetadataStoreClient.updateRelationshipInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          licenseGUID,
                                                          replaceAllProperties,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          getLicenseProperties(licenseId,
                                                                               start,
                                                                               end,
                                                                               conditions,
                                                                               licensedBy,
                                                                               licensedByTypeName,
                                                                               licensedByPropertyName,
                                                                               custodian,
                                                                               custodianTypeName,
                                                                               custodianPropertyName,
                                                                               licensee,
                                                                               licenseeTypeName,
                                                                               licenseePropertyName,
                                                                               notes),
                                                          effectiveTime);
    }


    /**
     * Set up the properties for a license.
     *
     * @param licenseId  unique identifier of the license (maybe from an external system)
     * @param start when did the license start
     * @param end when will the license end
     * @param conditions any conditions added to the license
     * @param licensedBy unique name/identifier of the element for the person/organization licensing the element
     * @param licensedByTypeName type of the licensedBy element
     * @param licensedByPropertyName property name for the unique identifier from the licensedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the license status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param licensee unique name/identifier of the element for the person/organization receiving the license
     * @param licenseeTypeName type of the licensee element
     * @param licenseePropertyName property name for the unique identifier from the licensee element
     * @param notes additional information, endorsements etc
     * @return element properties
     */
    private ElementProperties getLicenseProperties(String  licenseId,
                                                   Date    start,
                                                   Date    end,
                                                   String  conditions,
                                                   String  licensedBy,
                                                   String  licensedByTypeName,
                                                   String  licensedByPropertyName,
                                                   String  custodian,
                                                   String  custodianTypeName,
                                                   String  custodianPropertyName,
                                                   String  licensee,
                                                   String  licenseeTypeName,
                                                   String  licenseePropertyName,
                                                   String  notes)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.LICENSE_GUID.name,
                                                                        licenseId);

        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.START.name,
                                                    start);

        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.END.name,
                                                    end);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CONDITIONS.name,
                                                      conditions);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.LICENSED_BY.name,
                                                      licensedBy);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.LICENSED_BY_TYPE_NAME.name,
                                                      licensedByTypeName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.LICENSED_BY_PROPERTY_NAME.name,
                                                      licensedByPropertyName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CUSTODIAN.name,
                                                      custodian);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                      custodianTypeName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                      custodianPropertyName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.LICENSEE.name,
                                                      licensee);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.LICENSEE_TYPE_NAME.name,
                                                      licenseeTypeName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.LICENSEE_PROPERTY_NAME.name,
                                                      licenseePropertyName);

        return propertyHelper.addStringProperty(properties,
                                                OpenMetadataProperty.NOTES.name,
                                                notes);

    }


    /**
     * Remove a relationship between two definitions.
     *
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param userId calling user
     * @param licenseGUID unique identifier of the license relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void unlicenseElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  licenseGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        openMetadataStoreClient.deleteRelationshipInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          licenseGUID,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime);
    }


    /**
     * Create a link between a certification type and an element that has achieved the certification.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param elementGUID unique identifier of the element
     * @param certificationTypeGUID unique identifier of the certification type
     * @param certificateGUID  unique identifier of the certificate (maybe from an external system)
     * @param start when did the certification start
     * @param end when will the certification end
     * @param conditions any conditions added to the certification
     * @param certifiedBy unique name/identifier of the element for the person/organization certifying the element
     * @param certifiedByTypeName type of the certifiedBy element
     * @param certifiedByPropertyName property name for the unique identifier from the certifiedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the certification status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param recipient unique name/identifier of the element for the person/organization receiving the certification
     * @param recipientTypeName type of the recipient element
     * @param recipientPropertyName property name for the unique identifier from the recipient element
     * @param notes additional information, endorsements etc
     * @param effectiveFrom starting time for this relationship (null for all time)
     * @param effectiveTo ending time for this relationship (null for all time)
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return guid of certification relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String certifyElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  elementGUID,
                                 String  certificationTypeGUID,
                                 String  certificateGUID,
                                 Date    start,
                                 Date    end,
                                 String  conditions,
                                 String  certifiedBy,
                                 String  certifiedByTypeName,
                                 String  certifiedByPropertyName,
                                 String  custodian,
                                 String  custodianTypeName,
                                 String  custodianPropertyName,
                                 String  recipient,
                                 String  recipientTypeName,
                                 String  recipientPropertyName,
                                 String  notes,
                                 Date    effectiveFrom,
                                 Date    effectiveTo,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        return openMetadataStoreClient.createRelatedElementsInStore(userId,
                                                                    externalSourceGUID,
                                                                    externalSourceName,
                                                                    OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                    elementGUID,
                                                                    certificationTypeGUID,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveFrom,
                                                                    effectiveTo,
                                                                    getCertificationProperties(certificateGUID,
                                                                                               start,
                                                                                               end,
                                                                                               conditions,
                                                                                               certifiedBy,
                                                                                               certifiedByTypeName,
                                                                                               certifiedByPropertyName,
                                                                                               custodian,
                                                                                               custodianTypeName,
                                                                                               custodianPropertyName,
                                                                                               recipient,
                                                                                               recipientTypeName,
                                                                                               recipientPropertyName,
                                                                                               notes),
                                                                    effectiveTime);
    }


    /**
     * Update the certification relationship.
     *
     * @param userId calling user
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param certificationGUID unique identifier for the relationship
     * @param certificateGUID  unique identifier of the certificate (maybe from an external system)
     * @param start when did the certification start
     * @param end when will the certification end
     * @param conditions any conditions added to the certification
     * @param certifiedBy unique name/identifier of the element for the person/organization certifying the element
     * @param certifiedByTypeName type of the certifiedBy element
     * @param certifiedByPropertyName property name for the unique identifier from the certifiedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the certification status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param recipient unique name/identifier of the element for the person/organization receiving the certification
     * @param recipientTypeName type of the recipient element
     * @param recipientPropertyName property name for the unique identifier from the recipient element
     * @param notes additional information, endorsements etc
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateCertification(String  userId,
                                    String  externalSourceGUID,
                                    String  externalSourceName,
                                    String  certificationGUID,
                                    String  certificateGUID,
                                    Date    start,
                                    Date    end,
                                    String  conditions,
                                    String  certifiedBy,
                                    String  certifiedByTypeName,
                                    String  certifiedByPropertyName,
                                    String  custodian,
                                    String  custodianTypeName,
                                    String  custodianPropertyName,
                                    String  recipient,
                                    String  recipientTypeName,
                                    String  recipientPropertyName,
                                    String  notes,
                                    boolean replaceAllProperties,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing,
                                    Date    effectiveTime) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        openMetadataStoreClient.updateRelationshipInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          certificationGUID,
                                                          replaceAllProperties,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          getCertificationProperties(certificateGUID,
                                                                                     start,
                                                                                     end,
                                                                                     conditions,
                                                                                     certifiedBy,
                                                                                     certifiedByTypeName,
                                                                                     certifiedByPropertyName,
                                                                                     custodian,
                                                                                     custodianTypeName,
                                                                                     custodianPropertyName,
                                                                                     recipient,
                                                                                     recipientTypeName,
                                                                                     recipientPropertyName,
                                                                                     notes),
                                                          effectiveTime);
    }


    /**
     * Set up the properties for a certification.
     *
     * @param certificateGUID  unique identifier of the certificate (maybe from an external system)
     * @param start when did the certification start
     * @param end when will the certification end
     * @param conditions any conditions added to the certification
     * @param certifiedBy unique name/identifier of the element for the person/organization certifying the element
     * @param certifiedByTypeName type of the certifiedBy element
     * @param certifiedByPropertyName property name for the unique identifier from the certifiedBy element
     * @param custodian unique name/identifier of the element for the person/organization responsible for maintaining the certification status
     * @param custodianTypeName type of the custodian element
     * @param custodianPropertyName property name for the unique identifier from the custodian element
     * @param recipient unique name/identifier of the element for the person/organization receiving the certification
     * @param recipientTypeName type of the recipient element
     * @param recipientPropertyName property name for the unique identifier from the recipient element
     * @param notes additional information, endorsements etc
     * @return element properties
     */
    private ElementProperties getCertificationProperties(String  certificateGUID,
                                                          Date    start,
                                                          Date    end,
                                                          String  conditions,
                                                          String  certifiedBy,
                                                          String  certifiedByTypeName,
                                                          String  certifiedByPropertyName,
                                                          String  custodian,
                                                          String  custodianTypeName,
                                                          String  custodianPropertyName,
                                                          String  recipient,
                                                          String  recipientTypeName,
                                                          String  recipientPropertyName,
                                                          String  notes)
    {
        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                                        certificateGUID);

        properties = propertyHelper.addDateProperty(properties,
                                                                OpenMetadataProperty.START.name,
                                                                start);

        properties = propertyHelper.addDateProperty(properties,
                                                    OpenMetadataProperty.END.name,
                                                    end);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CONDITIONS.name,
                                                      conditions);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CERTIFIED_BY.name,
                                                      certifiedBy);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CERTIFIED_BY_TYPE_NAME.name,
                                                      certifiedByTypeName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CERTIFIED_BY_PROPERTY_NAME.name,
                                                      certifiedByPropertyName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CUSTODIAN.name,
                                                      custodian);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                      custodianTypeName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                      custodianPropertyName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.RECIPIENT.name,
                                                      recipient);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.RECIPIENT_TYPE_NAME.name,
                                                      recipientTypeName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.RECIPIENT_PROPERTY_NAME.name,
                                                      recipientPropertyName);

        properties = propertyHelper.addStringProperty(properties,
                                                      OpenMetadataProperty.NOTES.name,
                                                      notes);

        return properties;
    }


    /**
     * Remove a certification relationship.
     *
     * @param externalSourceGUID guid of the software capability entity that represented the external source - null for local
     * @param externalSourceName name of the software capability entity that represented the external source
     * @param userId calling user
     * @param certificationGUID unique identifier of the certification relationship
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void decertifyElement(String  userId,
                                 String  externalSourceGUID,
                                 String  externalSourceName,
                                 String  certificationGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        openMetadataStoreClient.deleteRelationshipInStore(userId,
                                                          externalSourceGUID,
                                                          externalSourceName,
                                                          certificationGUID,
                                                          forLineage,
                                                          forDuplicateProcessing,
                                                          effectiveTime);
    }


    /**
     * Retrieve the metadata element using its unique identifier.
     *
     * @param userId calling user
     * @param elementGUID unique identifier for the metadata element
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public MetadataElementSummary getMetadataElementByGUID(String  userId,
                                                           String  elementGUID,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        final String methodName = "getMetadataElementByGUID";
        final String elementGUIDParameterName = "elementGUID";

        invalidParameterHandler.validateGUID(elementGUID, elementGUIDParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId,
                                                                                                   elementGUID,
                                                                                                   forLineage,
                                                                                                   forDuplicateProcessing,
                                                                                                   null,
                                                                                                   effectiveTime);

        return metadataElementSummaryConverter.getNewBean(MetadataElementSummary.class,
                                                          openMetadataElement,
                                                          methodName);
    }


    /**
     * Retrieve the metadata element using its unique name (typically the qualified name).
     *
     * @param userId calling user
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public MetadataElementSummary getMetadataElementByUniqueName(String  userId,
                                                                 String  uniqueName,
                                                                 String  uniquePropertyName,
                                                                 boolean forLineage,
                                                                 boolean forDuplicateProcessing,
                                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        final String methodName = "getMetadataElementByUniqueName";
        final String elementGUIDParameterName = "uniqueName";

        invalidParameterHandler.validateName(uniqueName, elementGUIDParameterName, methodName);

        OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByUniqueName(userId,
                                                                                                         uniqueName,
                                                                                                         uniquePropertyName,
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         null,
                                                                                                         effectiveTime);

        return metadataElementSummaryConverter.getNewBean(MetadataElementSummary.class,
                                                          openMetadataElement,
                                                          methodName);
    }



    /**
     * Retrieve the unique identifier of a metadata element using its unique name (typically the qualified name).
     *
     * @param userId calling user
     * @param uniqueName             unique name for the metadata element
     * @param uniquePropertyName     name of property name to test in the open metadata element - if null "qualifiedName" is used
     * @param forLineage the retrieved element is for lineage processing so include archived elements
     * @param forDuplicateProcessing the retrieved element is for duplicate processing so do not combine results from known duplicates.
     * @param effectiveTime only return the element if it is effective at this time. Null means anytime. Use "new Date()" for now.
     *
     * @return metadata element unique identifier (guid)
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public String getMetadataElementGUIDByUniqueName(String  userId,
                                                     String  uniqueName,
                                                     String  uniquePropertyName,
                                                     boolean forLineage,
                                                     boolean forDuplicateProcessing,
                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        return openMetadataStoreClient.getMetadataElementGUIDByUniqueName(userId, uniqueName, uniquePropertyName, forLineage, forDuplicateProcessing, null, effectiveTime);
    }


    /**
     * Retrieve elements of the requested type name.
     *
     * @param userId calling user
     * @param findProperties details of the search
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<MetadataElementSummary> getElements(String             userId,
                                                    FindProperties     findProperties,
                                                    int                startFrom,
                                                    int                pageSize,
                                                    boolean            forLineage,
                                                    boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "getElements";

        List<OpenMetadataElement> openMetadataElements;

        if (findProperties == null)
        {
            openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                OpenMetadataType.OPEN_METADATA_ROOT.typeName,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                null,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                new Date(),
                                                                                startFrom,
                                                                                pageSize);
        }
        else
        {
            openMetadataElements = openMetadataStoreClient.findMetadataElements(userId,
                                                                                findProperties.getOpenMetadataTypeName(),
                                                                                null,
                                                                                null,
                                                                                findProperties.getLimitResultsByStatus(),
                                                                                findProperties.getAsOfTime(),
                                                                                null,
                                                                                findProperties.getSequencingProperty(),
                                                                                findProperties.getSequencingOrder(),
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                findProperties.getEffectiveTime(),
                                                                                startFrom,
                                                                                pageSize);
        }

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElementsByPropertyValue(String                      userId,
                                                                   FindPropertyNamesProperties findProperties,
                                                                   int                         startFrom,
                                                                   int                         pageSize,
                                                                   boolean                     forLineage,
                                                                   boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                              UserNotAuthorizedException,
                                                                                                                              PropertyServerException
    {
        final String methodName = "getElementsByPropertyValue";
        final String findPropertiesProperty = "findProperties";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateObject(findProperties, findPropertiesProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyValue(), propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyNames(), propertyNamesProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.getMetadataElementsByPropertyValue(userId,
                                                                                                                    findProperties.getOpenMetadataTypeName(),
                                                                                                                    null,
                                                                                                                    findProperties.getPropertyNames(),
                                                                                                                    findProperties.getPropertyValue(),
                                                                                                                    findProperties.getLimitResultsByStatus(),
                                                                                                                    findProperties.getAsOfTime(),
                                                                                                                    findProperties.getSequencingProperty(),
                                                                                                                    findProperties.getSequencingOrder(),
                                                                                                                    forLineage,
                                                                                                                    forDuplicateProcessing,
                                                                                                                    findProperties.getEffectiveTime(),
                                                                                                                    startFrom,
                                                                                                                    pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> findElementsByPropertyValue(String                      userId,
                                                                    FindPropertyNamesProperties findProperties,
                                                                    int                         startFrom,
                                                                    int                         pageSize,
                                                                    boolean                     forLineage,
                                                                    boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                               UserNotAuthorizedException,
                                                                                                                               PropertyServerException
    {
        final String methodName = "findElementsByPropertyValue";
        final String findPropertiesProperty = "findProperties";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateObject(findProperties, findPropertiesProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyValue(), propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyNames(), propertyNamesProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsByPropertyValue(userId,
                                                                                                                     findProperties.getOpenMetadataTypeName(),
                                                                                                                     null,
                                                                                                                     findProperties.getPropertyNames(),
                                                                                                                     findProperties.getPropertyValue(),
                                                                                                                     findProperties.getLimitResultsByStatus(),
                                                                                                                     findProperties.getAsOfTime(),
                                                                                                                     findProperties.getSequencingProperty(),
                                                                                                                     findProperties.getSequencingOrder(),
                                                                                                                     forLineage,
                                                                                                                     forDuplicateProcessing,
                                                                                                                     findProperties.getEffectiveTime(),
                                                                                                                     startFrom,
                                                                                                                     pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }


    /**
     * Retrieve elements with the requested classification name. It is also possible to limit the results
     * by specifying a type name for the elements that should be returned. If no type name is specified then
     * any type of element may be returned.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param findProperties  open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<MetadataElementSummary> getElementsByClassification(String             userId,
                                                                    String             classificationName,
                                                                    FindProperties     findProperties,
                                                                    int                startFrom,
                                                                    int                pageSize,
                                                                    boolean            forLineage,
                                                                    boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                      UserNotAuthorizedException,
                                                                                                                      PropertyServerException
    {
        final String methodName                 = "getElementsByClassification";
        final String classificationNameProperty = "classificationName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);

        List<OpenMetadataElement> elements;

        if (findProperties == null)
        {
            elements = openMetadataStoreClient.getMetadataElementsByClassification(userId,
                                                                                   null,
                                                                                   null,
                                                                                   classificationName,
                                                                                   null,
                                                                                   null,
                                                                                   null,
                                                                                   SequencingOrder.LAST_UPDATE_RECENT,
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   new Date(),
                                                                                   startFrom,
                                                                                   pageSize);
        }
        else
        {
            elements = openMetadataStoreClient.getMetadataElementsByClassification(userId,
                                                                                   findProperties.getOpenMetadataTypeName(),
                                                                                   null,
                                                                                   classificationName,
                                                                                   findProperties.getLimitResultsByStatus(),
                                                                                   findProperties.getAsOfTime(),
                                                                                   findProperties.getSequencingProperty(),
                                                                                   findProperties.getSequencingOrder(),
                                                                                   forLineage,
                                                                                   forDuplicateProcessing,
                                                                                   findProperties.getEffectiveTime(),
                                                                                   startFrom,
                                                                                   pageSize);
        }

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<MetadataElementSummary> getElementsByClassificationWithPropertyValue(String                      userId,
                                                                                     String                      classificationName,
                                                                                     FindPropertyNamesProperties findProperties,
                                                                                     int                         startFrom,
                                                                                     int                         pageSize,
                                                                                     boolean                     forLineage,
                                                                                     boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                                UserNotAuthorizedException,
                                                                                                                                                PropertyServerException
    {
        final String methodName = "getElementsByClassificationWithPropertyValue";
        final String classificationNameProperty = "classificationName";
        final String findPropertiesProperty = "findProperties";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);
        invalidParameterHandler.validateObject(findProperties, findPropertiesProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyValue(), propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyNames(), propertyNamesProperty, methodName);

        List<OpenMetadataElement> elements = openMetadataStoreClient.getMetadataElementsByClassificationPropertyValue(userId,
                                                                                                                      findProperties.getOpenMetadataTypeName(),
                                                                                                                      null,
                                                                                                                      classificationName,
                                                                                                                      findProperties.getPropertyNames(),
                                                                                                                      findProperties.getPropertyValue(),
                                                                                                                      findProperties.getLimitResultsByStatus(),
                                                                                                                      findProperties.getAsOfTime(),
                                                                                                                      findProperties.getSequencingProperty(),
                                                                                                                      findProperties.getSequencingOrder(),
                                                                                                                      forLineage,
                                                                                                                      forDuplicateProcessing,
                                                                                                                      findProperties.getEffectiveTime(),
                                                                                                                      startFrom,
                                                                                                                      pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           elements,
                                                           methodName);
    }


    /**
     * Retrieve elements with the requested classification name and with the requested a value found in
     * one of the classification's properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param classificationName name of classification
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<MetadataElementSummary> findElementsByClassificationWithPropertyValue(String                      userId,
                                                                                      String                      classificationName,
                                                                                      FindPropertyNamesProperties findProperties,
                                                                                      int                         startFrom,
                                                                                      int                         pageSize,
                                                                                      boolean                     forLineage,
                                                                                      boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                                 UserNotAuthorizedException,
                                                                                                                                                 PropertyServerException
    {
        final String methodName = "findElementsByClassificationWithPropertyValue";

        final String classificationNameProperty = "classificationName";
        final String findPropertiesProperty = "findProperties";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateName(classificationName, classificationNameProperty, methodName);
        invalidParameterHandler.validateObject(findProperties, findPropertiesProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyValue(), propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyNames(), propertyNamesProperty, methodName);

        List<OpenMetadataElement> openMetadataElements = openMetadataStoreClient.findMetadataElementsByClassificationPropertyValue(userId,
                                                                                                                                   findProperties.getOpenMetadataTypeName(),
                                                                                                                                   null,
                                                                                                                                   classificationName,
                                                                                                                                   findProperties.getPropertyNames(),
                                                                                                                                   findProperties.getPropertyValue(),
                                                                                                                                   findProperties.getLimitResultsByStatus(),
                                                                                                                                   findProperties.getAsOfTime(),
                                                                                                                                   findProperties.getSequencingProperty(),
                                                                                                                                   findProperties.getSequencingOrder(),
                                                                                                                                   forLineage,
                                                                                                                                   forDuplicateProcessing,
                                                                                                                                   findProperties.getEffectiveTime(),
                                                                                                                                   startFrom,
                                                                                                                                   pageSize);

        return metadataElementSummaryConverter.getNewBeans(MetadataElementSummary.class,
                                                           openMetadataElements,
                                                           methodName);
    }


    /**
     * Retrieve related elements of the requested type name.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the starting end
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param relationshipTypeName name of relationship
     * @param findProperties  open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedMetadataElementSummary> getRelatedElements(String             userId,
                                                                  String             elementGUID,
                                                                  String             relationshipTypeName,
                                                                  int                startingAtEnd,
                                                                  FindProperties     findProperties,
                                                                  int                startFrom,
                                                                  int                pageSize,
                                                                  boolean            forLineage,
                                                                  boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException
    {
        final String methodName = "getRelatedElements";

        RelatedMetadataElementList relatedMetadataElements;

        if (findProperties == null)
        {
            relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                         elementGUID,
                                                                                         startingAtEnd,
                                                                                         relationshipTypeName,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         new Date(),
                                                                                         startFrom,
                                                                                         pageSize);
        }
        else
        {
            relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                         elementGUID,
                                                                                         startingAtEnd,
                                                                                         relationshipTypeName,
                                                                                         null,
                                                                                         null,
                                                                                         null,
                                                                                         SequencingOrder.CREATION_DATE_RECENT,
                                                                                         forLineage,
                                                                                         forDuplicateProcessing,
                                                                                         findProperties.getEffectiveTime(),
                                                                                         startFrom,
                                                                                         pageSize);
        }

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElementSummary> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((findProperties == null) ||
                            (findProperties.getOpenMetadataTypeName() == null) ||
                            (findProperties.getOpenMetadataTypeName().equals(relatedMetadataElement.getElement().getType().getTypeName())))
                    {
                        results.add(relatedMetadataElementSummaryConverter.getNewBean(RelatedMetadataElementSummary.class,
                                                                                      relatedMetadataElement,
                                                                                      methodName));
                    }
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the requested a value
     * found in one of the classification's properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the types of elements returned.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedMetadataElementSummary> getRelatedElementsWithPropertyValue(String                      userId,
                                                                                   String                      elementGUID,
                                                                                   String                      relationshipTypeName,
                                                                                   int                         startingAtEnd,
                                                                                   FindPropertyNamesProperties findProperties,
                                                                                   int                         startFrom,
                                                                                   int                         pageSize,
                                                                                   boolean                     forLineage,
                                                                                   boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                              UserNotAuthorizedException,
                                                                                                                                              PropertyServerException
    {
        final String methodName = "getRelatedElementsWithPropertyValue";
        final String findPropertiesProperty = "findProperties";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateObject(findProperties, findPropertiesProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyValue(), propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyNames(), propertyNamesProperty, methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                  elementGUID,
                                                                                                                  startingAtEnd,
                                                                                                                  relationshipTypeName,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                  forLineage,
                                                                                                                  forDuplicateProcessing,
                                                                                                                  findProperties.getEffectiveTime(),
                                                                                                                  startFrom,
                                                                                                                  pageSize);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElementSummary> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((findProperties.getOpenMetadataTypeName() == null) ||
                            (findProperties.getOpenMetadataTypeName().equals(relatedMetadataElement.getElement().getType().getTypeName())))
                    {
                        ElementProperties relationshipProperties = relatedMetadataElement.getRelationshipProperties();
                        if (relationshipProperties != null)
                        {
                            for (String propertyName : findProperties.getPropertyNames())
                            {
                                if (propertyName != null)
                                {
                                    PropertyValue propertyValue = relationshipProperties.getPropertyValue(propertyName);

                                    if (propertyValue != null)
                                    {
                                        if (findProperties.getPropertyValue().equals(propertyValue.valueAsString()))
                                        {
                                            results.add(relatedMetadataElementSummaryConverter.getNewBean(RelatedMetadataElementSummary.class,
                                                                                                          relatedMetadataElement,
                                                                                                          methodName));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve elements linked via the requested relationship type name and with the relationship's properties
     * specified.  The value must be contained in the by a value found in one of the properties specified.
     * The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the starting element
     * @param relationshipTypeName name of relationship
     * @param startingAtEnd indicates which end to retrieve from (0 is "either end"; 1 is end1; 2 is end 2)
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedMetadataElementSummary> findRelatedElementsWithPropertyValue(String                      userId,
                                                                                    String                      elementGUID,
                                                                                    String                      relationshipTypeName,
                                                                                    int                         startingAtEnd,
                                                                                    FindPropertyNamesProperties findProperties,
                                                                                    int                         startFrom,
                                                                                    int                         pageSize,
                                                                                    boolean                     forLineage,
                                                                                    boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                               UserNotAuthorizedException,
                                                                                                                                               PropertyServerException
    {
        final String methodName = "findRelatedElementsWithPropertyValue";
        final String findPropertiesProperty = "findProperties";
        final String propertyValueProperty = "propertyValue";
        final String propertyNamesProperty = "propertyNames";

        invalidParameterHandler.validateObject(findProperties, findPropertiesProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyValue(), propertyValueProperty, methodName);
        invalidParameterHandler.validateObject(findProperties.getPropertyNames(), propertyNamesProperty, methodName);

        RelatedMetadataElementList relatedMetadataElements = openMetadataStoreClient.getRelatedMetadataElements(userId,
                                                                                                                  elementGUID,
                                                                                                                  startingAtEnd,
                                                                                                                  relationshipTypeName,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  null,
                                                                                                                  SequencingOrder.CREATION_DATE_RECENT,
                                                                                                                  forLineage,
                                                                                                                  forDuplicateProcessing,
                                                                                                                  findProperties.getEffectiveTime(),
                                                                                                                  startFrom,
                                                                                                                  pageSize);

        if ((relatedMetadataElements != null) && (relatedMetadataElements.getElementList() != null))
        {
            List<RelatedMetadataElementSummary> results = new ArrayList<>();

            for (RelatedMetadataElement relatedMetadataElement : relatedMetadataElements.getElementList())
            {
                if (relatedMetadataElement != null)
                {
                    if ((findProperties.getOpenMetadataTypeName() == null) ||
                            (findProperties.getOpenMetadataTypeName().equals(relatedMetadataElement.getElement().getType().getTypeName())))
                    {
                        ElementProperties relationshipProperties = relatedMetadataElement.getRelationshipProperties();
                        if (relationshipProperties != null)
                        {
                            for (String propertyName : findProperties.getPropertyNames())
                            {
                                if (propertyName != null)
                                {
                                    PropertyValue propertyValue = relationshipProperties.getPropertyValue(propertyName);

                                    if (propertyValue != null)
                                    {
                                        if (propertyValue.valueAsString().contains(findProperties.getPropertyValue()))
                                        {
                                            results.add(relatedMetadataElementSummaryConverter.getNewBean(RelatedMetadataElementSummary.class,
                                                                                                          relatedMetadataElement,
                                                                                                          methodName));
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            return results;
        }

        return null;
    }


    /**
     * Retrieve relationships of the requested relationship type name.
     *
     * @param userId calling user
     * @param relationshipTypeName name of relationship
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<MetadataRelationshipSummary> getRelationships(String             userId,
                                                              String             relationshipTypeName,
                                                              FindProperties     findProperties,
                                                              int                startFrom,
                                                              int                pageSize,
                                                              boolean            forLineage,
                                                              boolean            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String methodName = "getRelationships";

        OpenMetadataRelationshipList openMetadataRelationships;

        if (findProperties == null)
        {
            openMetadataRelationships = openMetadataStoreClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                         relationshipTypeName,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         SequencingOrder.LAST_UPDATE_RECENT,
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         new Date(),
                                                                                                         startFrom,
                                                                                                         pageSize);
        }
        else
        {
            openMetadataRelationships = openMetadataStoreClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                         relationshipTypeName,
                                                                                                         null,
                                                                                                         findProperties.getLimitResultsByStatus(),
                                                                                                         findProperties.getAsOfTime(),
                                                                                                         findProperties.getSequencingProperty(),
                                                                                                         findProperties.getSequencingOrder(),
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         findProperties.getEffectiveTime(),
                                                                                                         startFrom,
                                                                                                         pageSize);
        }

        if (openMetadataRelationships != null)
        {
            return metadataRelationshipSummaryConverter.getNewBeans(MetadataRelationshipSummary.class,
                                                                    openMetadataRelationships,
                                                                    methodName);
        }

        return null;
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in
     * one of the relationship's properties specified.  The value must match exactly.
     *
     * @param userId calling user
     * @param relationshipTypeName name of relationship
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<MetadataRelationshipSummary> getRelationshipsWithPropertyValue(String                      userId,
                                                                               String                      relationshipTypeName,
                                                                               FindPropertyNamesProperties findProperties,
                                                                               int                         startFrom,
                                                                               int                         pageSize,
                                                                               boolean                     forLineage,
                                                                               boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                          UserNotAuthorizedException,
                                                                                                                                          PropertyServerException
    {
        final String methodName = "getRelationships";

        OpenMetadataRelationshipList openMetadataRelationships;

        if (findProperties == null)
        {
            openMetadataRelationships = openMetadataStoreClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                         relationshipTypeName,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         SequencingOrder.LAST_UPDATE_RECENT,
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         new Date(),
                                                                                                         startFrom,
                                                                                                         pageSize);
        }
        else
        {
            openMetadataRelationships = openMetadataStoreClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                         relationshipTypeName,
                                                                                                         propertyHelper.getSearchPropertiesByName(findProperties.getPropertyNames(),
                                                                                                                                                  findProperties.getPropertyValue(),
                                                                                                                                                  PropertyComparisonOperator.LIKE,
                                                                                                                                                  TemplateFilter.ALL),
                                                                                                         findProperties.getLimitResultsByStatus(),
                                                                                                         findProperties.getAsOfTime(),
                                                                                                         findProperties.getSequencingProperty(),
                                                                                                         findProperties.getSequencingOrder(),
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         findProperties.getEffectiveTime(),
                                                                                                         startFrom,
                                                                                                         pageSize);
        }

        if (openMetadataRelationships != null)
        {
            return metadataRelationshipSummaryConverter.getNewBeans(MetadataRelationshipSummary.class,
                                                                    openMetadataRelationships,
                                                                    methodName);
        }

        return null;
    }


    /**
     * Retrieve relationships of the requested relationship type name and with the requested a value found in one of
     * the relationship's properties specified.  The value must only be contained in the properties rather than
     * needing to be an exact match.
     *
     * @param userId calling user
     * @param relationshipTypeName name of relationship
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<MetadataRelationshipSummary> findRelationshipsWithPropertyValue(String                      userId,
                                                                                String                      relationshipTypeName,
                                                                                FindPropertyNamesProperties findProperties,
                                                                                int                         startFrom,
                                                                                int                         pageSize,
                                                                                boolean                     forLineage,
                                                                                boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                                           UserNotAuthorizedException,
                                                                                                                                           PropertyServerException
    {
        final String methodName = "getRelationships";

        OpenMetadataRelationshipList openMetadataRelationships;

        if (findProperties == null)
        {
            openMetadataRelationships = openMetadataStoreClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                         relationshipTypeName,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         null,
                                                                                                         SequencingOrder.LAST_UPDATE_RECENT,
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         new Date(),
                                                                                                         startFrom,
                                                                                                         pageSize);
        }
        else
        {
            openMetadataRelationships = openMetadataStoreClient.findRelationshipsBetweenMetadataElements(userId,
                                                                                                         relationshipTypeName,
                                                                                                         propertyHelper.getSearchPropertiesByName(findProperties.getPropertyNames(),
                                                                                                                                                  findProperties.getPropertyValue(),
                                                                                                                                                  PropertyComparisonOperator.LIKE,
                                                                                                                                                  TemplateFilter.ALL),
                                                                                                         findProperties.getLimitResultsByStatus(),
                                                                                                         findProperties.getAsOfTime(),
                                                                                                         findProperties.getSequencingProperty(),
                                                                                                         findProperties.getSequencingOrder(),
                                                                                                         forLineage,
                                                                                                         forDuplicateProcessing,
                                                                                                         findProperties.getEffectiveTime(),
                                                                                                         startFrom,
                                                                                                         pageSize);
        }

        if (openMetadataRelationships != null)
        {
            return metadataRelationshipSummaryConverter.getNewBeans(MetadataRelationshipSummary.class,
                                                                    openMetadataRelationships,
                                                                    methodName);
        }

        return null;
    }


    /**
     * Retrieve the header for the instance identified by the supplied unique identifier.
     * It may be an element (entity) or a relationship between elements.
     *
     * @param userId  name of the server instance to connect to
     * @param guid identifier to use in the lookup
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime effective time
     *
     * @return list of matching elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public ElementHeader retrieveInstanceForGUID(String  userId,
                                                 String  guid,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        try
        {
            OpenMetadataElement openMetadataElement = openMetadataStoreClient.getMetadataElementByGUID(userId, guid, forLineage, forDuplicateProcessing, null, effectiveTime);

            if (openMetadataElement != null)
            {
                ElementHeader elementHeader = new ElementHeader(openMetadataElement);

                elementHeader.setGUID(openMetadataElement.getElementGUID());
                elementHeader.setClassifications(metadataElementSummaryConverter.getElementClassifications(openMetadataElement.getClassifications()));

                return elementHeader;
            }
        }
        catch (InvalidParameterException notFound)
        {
            OpenMetadataRelationship openMetadataRelationship = openMetadataStoreClient.getRelationshipByGUID(userId, guid, forLineage, forDuplicateProcessing, null, effectiveTime);

            if (openMetadataRelationship != null)
            {
                ElementHeader elementHeader = new ElementHeader(openMetadataRelationship);

                elementHeader.setGUID(openMetadataRelationship.getRelationshipGUID());

                return elementHeader;
            }
        }

        return null;
    }
}
