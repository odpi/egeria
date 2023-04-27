/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.client.management;

import org.odpi.openmetadata.accessservices.assetmanager.api.management.StewardshipManagementInterface;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.StewardshipExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.RelatedElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetOriginProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.GovernanceClassificationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.OwnerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.RetentionClassificationProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SecurityTagsProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SemanticAssignmentProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SubjectAreaMemberProperties;
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
public class StewardshipManagementClient implements StewardshipManagementInterface
{
    private final StewardshipExchangeClient client;

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipManagementClient(String   serverName,
                                       String   serverPlatformURLRoot,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        client = new StewardshipExchangeClient(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public StewardshipManagementClient(String serverName,
                                       String serverPlatformURLRoot) throws InvalidParameterException
    {
        client = new StewardshipExchangeClient(serverName, serverPlatformURLRoot);
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
    public StewardshipManagementClient(String   serverName,
                                       String   serverPlatformURLRoot,
                                       String   userId,
                                       String   password,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        client = new StewardshipExchangeClient(serverName, serverPlatformURLRoot, userId, password, auditLog);
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
    public StewardshipManagementClient(String                 serverName,
                                       String                 serverPlatformURLRoot,
                                       AssetManagerRESTClient restClient,
                                       int                    maxPageSize,
                                       AuditLog               auditLog) throws InvalidParameterException
    {
        client = new StewardshipExchangeClient(serverName, serverPlatformURLRoot, restClient, maxPageSize, auditLog);
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
    public StewardshipManagementClient(String serverName,
                                       String serverPlatformURLRoot,
                                       String userId,
                                       String password) throws InvalidParameterException
    {
        client = new StewardshipExchangeClient(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
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
                                            String                             elementGUID,
                                            GovernanceClassificationProperties properties,
                                            Date                               effectiveTime,
                                            boolean                            forLineage,
                                            boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                              UserNotAuthorizedException,
                                                                                                              PropertyServerException
    {
        client.setConfidenceClassification(userId, null, null, elementGUID, null, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
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
                                              String  elementGUID,
                                              Date    effectiveTime,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        client.clearConfidenceClassification(userId, null, null, elementGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getConfidenceClassifiedElements(String  userId,
                                                             boolean returnSpecificLevel,
                                                             int     levelIdentifier,
                                                             int     startFrom,
                                                             int     pageSize) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        return client.getConfidenceClassifiedElements(userId,
                                                      null,
                                                      null,
                                                      returnSpecificLevel,
                                                      levelIdentifier,
                                                      startFrom,
                                                      pageSize,
                                                      null,
                                                      false,
                                                      false);
    }



    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
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
                                             String                             elementGUID,
                                             GovernanceClassificationProperties properties,
                                             Date                               effectiveTime,
                                             boolean                            forLineage,
                                             boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException
    {
        client.setCriticalityClassification(userId, null, null, elementGUID, properties, null, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
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
                                               String  elementGUID,
                                               Date    effectiveTime,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        client.clearCriticalityClassification(userId, null, null, elementGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }




    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getCriticalityClassifiedElements(String  userId,
                                                              boolean returnSpecificLevel,
                                                              int     levelIdentifier,
                                                              int     startFrom,
                                                              int     pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        return client.getCriticalityClassifiedElements(userId,
                                                       null,
                                                       null,
                                                       returnSpecificLevel,
                                                       levelIdentifier,
                                                       startFrom,
                                                       pageSize,
                                                       null,
                                                       false,
                                                       false);
    }


    /**
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
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
                                                 String                             elementGUID,
                                                 GovernanceClassificationProperties properties,
                                                 Date                               effectiveTime,
                                                 boolean                            forLineage,
                                                 boolean                            forDuplicateProcessing) throws InvalidParameterException,
                                                                                                                   UserNotAuthorizedException,
                                                                                                                   PropertyServerException
    {
        client.setConfidentialityClassification(userId, null, null, elementGUID, null, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
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
                                                   String  elementGUID,
                                                   Date    effectiveTime,
                                                   boolean forLineage,
                                                   boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        client.clearConfidentialityClassification(userId, null, null, elementGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param userId calling user
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getConfidentialityClassifiedElements(String  userId,
                                                                  boolean returnSpecificLevel,
                                                                  int     levelIdentifier,
                                                                  int     startFrom,
                                                                  int     pageSize) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        return client.getConfidentialityClassifiedElements(userId,
                                                           null,
                                                           null,
                                                           returnSpecificLevel,
                                                           levelIdentifier,
                                                           startFrom,
                                                           pageSize,
                                                           null,
                                                           false,
                                                           false);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to classify
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
                                           String                            elementGUID,
                                           RetentionClassificationProperties properties,
                                           Date                              effectiveTime,
                                           boolean                           forLineage,
                                           boolean                           forDuplicateProcessing) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {
        client.setRetentionClassification(userId, null, null, elementGUID, null, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to unclassify
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
                                             String  elementGUID,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        client.clearRetentionClassification(userId, null, null, elementGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }



    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param userId calling user
     * @param returnSpecificBasisIdentifier should the results be filtered by basisIdentifier?
     * @param basisIdentifier the identifier to filter by (if returnSpecificBasisIdentifier=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getRetentionClassifiedElements(String  userId,
                                                            boolean returnSpecificBasisIdentifier,
                                                            int     basisIdentifier,
                                                            int     startFrom,
                                                            int     pageSize) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        return client.getRetentionClassifiedElements(userId,
                                                     null,
                                                     null,
                                                     returnSpecificBasisIdentifier,
                                                     basisIdentifier,
                                                     startFrom,
                                                     pageSize,
                                                     null,
                                                     false,
                                                     false);
    }


    /**
     * Add or replace the security tags for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
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
                                 String                 elementGUID,
                                 SecurityTagsProperties properties,
                                 Date                   effectiveTime,
                                 boolean                forLineage,
                                 boolean                forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        client.addSecurityTags(userId, null, null, elementGUID, null, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the security tags need to be removed.
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
                                  String  elementGUID,
                                  Date    effectiveTime,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        client.clearSecurityTags(userId, null, null, elementGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getSecurityTaggedElements(String userId,
                                                       int    startFrom,
                                                       int    pageSize) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return client.getSecurityTaggedElements(userId,
                                                null,
                                                null,
                                                startFrom,
                                                pageSize,
                                                null,
                                                false,
                                                false);
    }


    /**
     * Add or replace the ownership classification for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
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
                              String          elementGUID,
                              OwnerProperties properties,
                              Date            effectiveTime,
                              boolean         forLineage,
                              boolean         forDuplicateProcessing) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        client.addOwnership(userId, null, null, elementGUID, null, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the classification needs to be removed.
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
                               String  elementGUID,
                               Date    effectiveTime,
                               boolean forLineage,
                               boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        client.clearOwnership(userId, null, null, elementGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param owner unique identifier for the owner
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getOwnersElements(String userId,
                                               String owner,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        return client.getOwnersElements(userId,
                                        null,
                                        null,
                                        owner,
                                        startFrom,
                                        pageSize,
                                        null,
                                        false,
                                        false);
    }


    /**
     * Add or replace the origin classification for an asset.
     *
     * @param userId calling user
     * @param assetGUID element to link it to - its type must inherit from Asset.
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
                                String                assetGUID,
                                AssetOriginProperties properties,
                                Date                  effectiveTime,
                                boolean               forLineage,
                                boolean               forDuplicateProcessing) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        client.addAssetOrigin(userId, null, null, assetGUID, null, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the origin classification from an asset.
     *
     * @param userId calling user
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
                                 String  assetGUID,
                                 Date    effectiveTime,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        client.clearAssetOrigin(userId, null, null, assetGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Return information about the assets from a specific origin.
     *
     * @param userId calling user
     * @param properties values to search on - null means any value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of the assets
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<AssetElement> getAssetsByOrigin(String                userId,
                                                AssetOriginProperties properties,
                                                int                   startFrom,
                                                int                   pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException

    {
        return client.getAssetsByOrigin(userId,
                                        null,
                                        null,
                                        properties,
                                        startFrom,
                                        pageSize,
                                        null,
                                        false,
                                        false);
    }


    /**
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the metadata element to update
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
                                        String                      elementGUID,
                                        SubjectAreaMemberProperties properties,
                                        Date                        effectiveTime,
                                        boolean                     forLineage,
                                        boolean                     forDuplicateProcessing) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        client.addElementToSubjectArea(userId, null, null, elementGUID, null, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param userId calling user
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
    public void removeElementFromSubjectArea(String  userId,
                                             String  elementGUID,
                                             Date    effectiveTime,
                                             boolean forLineage,
                                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        client.removeElementFromSubjectArea(userId, null, null, elementGUID, null, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param userId calling user
     * @param subjectAreaName unique identifier for the subject area
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public List<ElementStub> getMembersOfSubjectArea(String userId,
                                                     String subjectAreaName,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException

    {
        return client.getMembersOfSubjectArea(userId,
                                              null,
                                              null,
                                              subjectAreaName,
                                              startFrom,
                                              pageSize,
                                              null,
                                              false,
                                              false);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param userId calling user
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
                                        String                       elementGUID,
                                        String                       glossaryTermGUID,
                                        SemanticAssignmentProperties properties,
                                        Date                         effectiveTime,
                                        boolean                      forLineage,
                                        boolean                      forDuplicateProcessing) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        client.setupSemanticAssignment(userId, null, null, elementGUID, glossaryTermGUID, properties, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param userId calling user
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
                                        String  elementGUID,
                                        String  glossaryTermGUID,
                                        Date    effectiveTime,
                                        boolean forLineage,
                                        boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        client.clearSemanticAssignment(userId, null, null, elementGUID, glossaryTermGUID, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GlossaryTermElement> getMeanings(String userId,
                                                 String elementGUID,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException

    {
        return client.getMeanings(userId,
                                  null,
                                  null,
                                  elementGUID,
                                  startFrom,
                                  pageSize,
                                  null,
                                  false,
                                  false);
    }


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getSemanticAssignees(String userId,
                                                     String glossaryTermGUID,
                                                     int    startFrom,
                                                     int    pageSize) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException

    {
        return client.getSemanticAssignees(userId,
                                           null,
                                           null,
                                           glossaryTermGUID,
                                           startFrom,
                                           pageSize,
                                           null,
                                           false,
                                           false);
    }



    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param userId calling user
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
                                                 String  definitionGUID,
                                                 String  elementGUID,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        client.addGovernanceDefinitionToElement(userId, null, null, definitionGUID, elementGUID, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param userId calling user
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
                                                      String  definitionGUID,
                                                      String  elementGUID,
                                                      Date    effectiveTime,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        client.removeGovernanceDefinitionFromElement(userId, null, null, definitionGUID, elementGUID, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<GovernanceDefinitionElement> getGovernedByDefinitions(String userId,
                                                                      String elementGUID,
                                                                      int    startFrom,
                                                                      int    pageSize) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException

    {
        return client.getGovernedByDefinitions(userId,
                                               null,
                                               null,
                                               elementGUID,
                                               startFrom,
                                               pageSize,
                                               null,
                                               false,
                                               false);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param userId calling user
     * @param governanceDefinitionGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @Override
    public List<RelatedElement> getGovernedElements(String userId,
                                                    String governanceDefinitionGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException

    {
        return client.getGovernedElements(userId,
                                          null,
                                          null,
                                          governanceDefinitionGUID,
                                          startFrom,
                                          pageSize,
                                          null,
                                          false,
                                          false);
    }
}
