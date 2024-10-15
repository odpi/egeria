/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.connector;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.StewardshipExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.AssetElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GlossaryTermElement;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.FindPropertyNamesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.AssetOriginProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldQueryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.DataFieldValuesProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityTagsProperties;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.Date;
import java.util.List;


/**
 * StewardshipExchangeService is the context for managing classifications and exceptions and other actions of stewards.
 */
public class StewardshipExchangeService
{
    private final StewardshipExchangeClient stewardshipExchangeClient;
    private final String                    userId;
    private final String                    assetManagerGUID;
    private final String                    assetManagerName;
    private final String                   connectorName;
    private final PermittedSynchronization permittedSynchronization;
    private final AuditLog                 auditLog;

    private boolean forLineage             = false;
    private boolean forDuplicateProcessing = false;

    /**
     * Create a new client to exchange data asset content with open metadata.
     *
     * @param stewardshipExchangeClient client for exchange requests
     * @param permittedSynchronization direction(s) that metadata can flow
     * @param userId integration daemon's userId
     * @param assetManagerGUID unique identifier of the software server capability for the asset manager
     * @param assetManagerName unique name of the software server capability for the asset manager
     * @param connectorName name of the connector using this context
     * @param auditLog logging destination
     */
    StewardshipExchangeService(StewardshipExchangeClient stewardshipExchangeClient,
                               PermittedSynchronization permittedSynchronization,
                               String                    userId,
                               String                    assetManagerGUID,
                               String                    assetManagerName,
                               String                    connectorName,
                               AuditLog                  auditLog)
    {
        this.stewardshipExchangeClient = stewardshipExchangeClient;
        this.permittedSynchronization  = permittedSynchronization;
        this.userId                    = userId;
        this.assetManagerGUID          = assetManagerGUID;
        this.assetManagerName          = assetManagerName;
        this.connectorName             = connectorName;
        this.auditLog                  = auditLog;
    }


    /* ========================================================
     * Set up the forLineage flag
     */

    /**
     * Return whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @return boolean flag
     */
    public boolean isForLineage()
    {
        return forLineage;
    }


    /**
     * Set up whether retrieval requests from this service are to include elements with the Memento classification attached or not.
     *
     * @param forLineage boolean flag
     */
    public void setForLineage(boolean forLineage)
    {
        this.forLineage = forLineage;
    }


    /* ========================================================
     * Set up the forDuplicateProcessing flag
     */

    /**
     * Return whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @return boolean flag
     */
    public boolean isForDuplicateProcessing()
    {
        return forDuplicateProcessing;
    }


    /**
     * Set up whether retrieval requests from this service are to avoid merging duplicates or not.
     *
     * @param forDuplicateProcessing boolean flag
     */
    public void setForDuplicateProcessing(boolean forDuplicateProcessing)
    {
        this.forDuplicateProcessing = forDuplicateProcessing;
    }


    /* =======================================
     * Specialized interface begins
     */

    /**
     * Classify the element to indicate that it describes a data field and supply
     * properties that describe the characteristics of the data values found within.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties descriptive properties for the data field
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setElementAsDataField(String                    elementGUID,
                                      String                    externalIdentifier,
                                      DataFieldValuesProperties properties,
                                      Date                      effectiveTime) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        final String methodName = "setElementAsDataField";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.setElementAsDataField(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the data field designation from the element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearElementAsDataField(String  elementGUID,
                                        String  externalIdentifier,
                                        Date    effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String   methodName = "clearElementAsDataField";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearElementAsDataField(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the elements classified with the DataField classification.
     *
     * @param properties values to match on
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<ElementStub> getDataFieldClassifiedElements(DataFieldQueryProperties properties,
                                                            int                      startFrom,
                                                            int                      pageSize,
                                                            Date                     effectiveTime) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return stewardshipExchangeClient.getDataFieldClassifiedElements(userId, assetManagerGUID, assetManagerName, properties, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate the level of confidence that the organization
     * has that the data is complete, accurate and up-to-date.  The level of confidence is expressed by the
     * levelIdentifier property.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setConfidenceClassification(String                             elementGUID,
                                            String                             externalIdentifier,
                                            GovernanceClassificationProperties properties,
                                            Date                               effectiveTime) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        final String methodName = "setConfidenceClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.setConfidenceClassification(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidence to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConfidenceClassification(String  elementGUID,
                                              String  externalIdentifier,
                                              Date    effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String   methodName = "clearConfidenceClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearConfidenceClassification(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the elements classified with the confidence classification.
     *
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getConfidenceClassifiedElements(boolean returnSpecificLevel,
                                                                        int     levelIdentifier,
                                                                        int     startFrom,
                                                                        int     pageSize,
                                                                        Date    effectiveTime) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return stewardshipExchangeClient.getConfidenceClassifiedElements(userId, assetManagerGUID, assetManagerName, returnSpecificLevel, levelIdentifier, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how critical the element (or associated resource)
     * is to the organization.  The level of criticality is expressed by the levelIdentifier property.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setCriticalityClassification(String                             elementGUID,
                                             GovernanceClassificationProperties properties,
                                             String                             externalIdentifier,
                                             Date                               effectiveTime) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "setCriticalityClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.setCriticalityClassification(userId, assetManagerGUID, assetManagerName, elementGUID, properties, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the criticality classification from the element.  This normally occurs when the organization has lost track of the level of
     * criticality to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearCriticalityClassification(String  elementGUID,
                                               String  externalIdentifier,
                                               Date    effectiveTime) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String   methodName = "clearCriticalityClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearCriticalityClassification(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the elements classified with the criticality classification.
     *
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getCriticalityClassifiedElements(boolean returnSpecificLevel,
                                                                         int     levelIdentifier,
                                                                         int     startFrom,
                                                                         int     pageSize,
                                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        return stewardshipExchangeClient.getCriticalityClassifiedElements(userId, assetManagerGUID, assetManagerName, returnSpecificLevel, levelIdentifier, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Classify/reclassify the element (typically a data field, schema attribute or glossary term) to indicate the level of confidentiality
     * that any data associated with the element should be given.  If the classification is attached to a glossary term, the level
     * of confidentiality is a suggestion for any element linked to the glossary term via the SemanticAssignment classification.
     * The level of confidence is expressed by the levelIdentifier property.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setConfidentialityClassification(String                             elementGUID,
                                                 String                             externalIdentifier,
                                                 GovernanceClassificationProperties properties,
                                                 Date                               effectiveTime) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "setConfidentialityClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.setConfidentialityClassification(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the confidence classification from the element.  This normally occurs when the organization has lost track of the level of
     * confidentiality to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearConfidentialityClassification(String  elementGUID,
                                                   String  externalIdentifier,
                                                   Date    effectiveTime) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String   methodName = "clearConfidentialityClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearConfidentialityClassification(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the elements classified with the confidentiality classification.
     *
     * @param returnSpecificLevel should the results be filtered by levelIdentifier?
     * @param levelIdentifier the identifier to filter by (if returnSpecificLevel=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getConfidentialityClassifiedElements(boolean returnSpecificLevel,
                                                                             int     levelIdentifier,
                                                                             int     startFrom,
                                                                             int     pageSize,
                                                                             Date    effectiveTime) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        return stewardshipExchangeClient.getConfidentialityClassifiedElements(userId, assetManagerGUID, assetManagerName, returnSpecificLevel, levelIdentifier, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Classify/reclassify the element (typically an asset) to indicate how long the element (or associated resource)
     * is to be retained by the organization.  The policy to apply to the element/resource is captured by the retentionBasis
     * property.  The dates after which the element/resource is archived and then deleted are specified in the archiveAfter and deleteAfter
     * properties respectively.
     *
     * @param elementGUID unique identifier of the metadata element to classify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the classification
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setRetentionClassification(String                            elementGUID,
                                           String                            externalIdentifier,
                                           RetentionClassificationProperties properties,
                                           Date                              effectiveTime) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "setRetentionClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.setRetentionClassification(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the retention classification from the element.  This normally occurs when the organization has lost track of, or no longer needs to
     * track the retention period to assign to the element.
     *
     * @param elementGUID unique identifier of the metadata element to unclassify
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearRetentionClassification(String  elementGUID,
                                             String  externalIdentifier,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String   methodName = "clearRetentionClassification";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearRetentionClassification(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the elements classified with the retention classification.
     *
     * @param returnSpecificBasisIdentifier should the results be filtered by basisIdentifier?
     * @param basisIdentifier the identifier to filter by (if returnSpecificBasisIdentifier=true)
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getRetentionClassifiedElements(boolean returnSpecificBasisIdentifier,
                                                                       int     basisIdentifier,
                                                                       int     startFrom,
                                                                       int     pageSize,
                                                                       Date    effectiveTime) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException
    {
        return stewardshipExchangeClient.getRetentionClassifiedElements(userId, assetManagerGUID, assetManagerName, returnSpecificBasisIdentifier, basisIdentifier, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }



    /**
     * Add or replace the security tags for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the security tags
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addSecurityTags(String                 elementGUID,
                                 String                 externalIdentifier,
                                 SecurityTagsProperties properties,
                                 Date                   effectiveTime) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String   methodName = "addSecurityTags";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.addSecurityTags(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the security tags classification from an element.
     *
     * @param elementGUID element where the security tags need to be removed.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearSecurityTags(String  elementGUID,
                                  String  externalIdentifier,
                                  Date    effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException
    {
        final String   methodName = "clearSecurityTags";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearSecurityTags(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getSecurityTaggedElements(int     startFrom,
                                                                  int     pageSize,
                                                                  Date    effectiveTime) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return stewardshipExchangeClient.getSecurityTaggedElements(userId, assetManagerGUID, assetManagerName, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Add or replace the ownership classification for an element.
     *
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the ownership
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addOwnership(String          elementGUID,
                              String          externalIdentifier,
                              OwnerProperties properties,
                              Date            effectiveTime) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        final String   methodName = "addOwnership";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.addOwnership(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the ownership classification from an element.
     *
     * @param elementGUID element where the classification needs to be removed.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearOwnership(String  elementGUID,
                               String  externalIdentifier,
                               Date    effectiveTime) throws InvalidParameterException,
                                                             UserNotAuthorizedException,
                                                             PropertyServerException
    {
        final String   methodName = "clearOwnership";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearOwnership(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param owner unique identifier for the owner
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getOwnersElements(String  owner,
                                                          int     startFrom,
                                                          int     pageSize,
                                                          Date    effectiveTime) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException
    {
        return stewardshipExchangeClient.getOwnersElements(userId, assetManagerGUID, assetManagerName, owner, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Add or replace the origin classification for an asset.
     *
     * @param assetGUID element to link it to - its type must inherit from Asset.
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties details of the origin
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addAssetOrigin(String                assetGUID,
                                String                externalIdentifier,
                                AssetOriginProperties properties,
                                Date                  effectiveTime) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String   methodName = "addAssetOrigin";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.addAssetOrigin(userId, assetManagerGUID, assetManagerName, assetGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the origin classification from an asset.
     *
     * @param assetGUID element where the classification needs to be removed
     * @param externalIdentifier optional matching identifier from an external system
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void clearAssetOrigin(String  assetGUID,
                                 String  externalIdentifier,
                                 Date    effectiveTime) throws InvalidParameterException,
                                                               UserNotAuthorizedException,
                                                               PropertyServerException
    {
        final String   methodName = "clearAssetOrigin";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearAssetOrigin(userId, assetManagerGUID, assetManagerName, assetGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the assets from a specific origin.
     *
     * @param properties values to search on - null means any value
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of the assets
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<AssetElement> getAssetsByOrigin(FindAssetOriginProperties properties,
                                                int                       startFrom,
                                                int                       pageSize,
                                                Date                      effectiveTime) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        return stewardshipExchangeClient.getAssetsByOrigin(userId, assetManagerGUID, assetManagerName, properties, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Classify the element to assert that the definitions it represents are part of a subject area definition.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the element in the external asset manager
     * @param properties qualified name of subject area
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addElementToSubjectArea(String                              elementGUID,
                                        String                              externalIdentifier,
                                        SubjectAreaClassificationProperties properties,
                                        Date                                effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "addElementToSubjectArea";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.addElementToSubjectArea(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the subject area designation from the identified element.
     *
     * @param elementGUID unique identifier of the metadata element to update
     * @param externalIdentifier unique identifier of the equivalent element in the external asset manager
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeElementFromSubjectArea(String  elementGUID,
                                             String  externalIdentifier,
                                             Date    effectiveTime) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        final String   methodName = "removeElementFromSubjectArea";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.removeElementFromSubjectArea(userId, assetManagerGUID, assetManagerName, elementGUID, externalIdentifier, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Return information about the contents of a subject area such as the glossaries, reference data sets and quality definitions.
     *
     * @param subjectAreaName unique identifier for the subject area
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of element stubs
     *
     * @throws InvalidParameterException qualifiedName or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public List<MetadataElementSummary> getMembersOfSubjectArea(String  subjectAreaName,
                                                                int     startFrom,
                                                                int     pageSize,
                                                                Date    effectiveTime) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        return stewardshipExchangeClient.getMembersOfSubjectArea(userId, assetManagerGUID, assetManagerName, subjectAreaName, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Create a semantic assignment relationship between a glossary term and an element (normally a schema attribute, data field or asset).
     * This relationship indicates that the data associated with the element meaning matches the description in the glossary term.
     *
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param properties properties for the relationship
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupSemanticAssignment(String                       elementGUID,
                                        String                       glossaryTermGUID,
                                        SemanticAssignmentProperties properties,
                                        Date                         effectiveTime) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "setupSemanticAssignment";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.setupSemanticAssignment(userId, assetManagerGUID, assetManagerName, elementGUID, glossaryTermGUID, properties, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove a semantic assignment relationship between an element and its glossary term.
     *
     * @param elementGUID unique identifier of the element that is being assigned to the glossary term
     * @param glossaryTermGUID unique identifier of the glossary term that provides the meaning
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearSemanticAssignment(String  elementGUID,
                                        String  glossaryTermGUID,
                                        Date    effectiveTime) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        final String methodName = "clearSemanticAssignment";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.clearSemanticAssignment(userId, assetManagerGUID, assetManagerName, elementGUID, glossaryTermGUID, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the glossary terms linked via a "SemanticAssignment" relationship to the requested element.
     *
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GlossaryTermElement> getMeanings(String  elementGUID,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        return stewardshipExchangeClient.getMeanings(userId, assetManagerGUID, assetManagerName, elementGUID, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the elements linked via a "SemanticAssignment" relationship to the requested glossary term.
     *
     * @param glossaryTermGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelatedMetadataElementSummary> getSemanticAssignees(String  glossaryTermGUID,
                                                                    int     startFrom,
                                                                    int     pageSize,
                                                                    Date    effectiveTime) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return stewardshipExchangeClient.getSemanticAssignees(userId, assetManagerGUID, assetManagerName, glossaryTermGUID, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }



    /**
     * Link a governance definition to an element using the GovernedBy relationship.
     *
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to link
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void addGovernanceDefinitionToElement(String  definitionGUID,
                                                 String  elementGUID,
                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "addGovernanceDefinitionToElement";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.addGovernanceDefinitionToElement(userId, assetManagerGUID, assetManagerName, definitionGUID, elementGUID, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Remove the GovernedBy relationship between a governance definition and an element.
     *
     * @param definitionGUID identifier of the governance definition to link
     * @param elementGUID unique identifier of the metadata element to update
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceDefinitionFromElement(String  definitionGUID,
                                                      String  elementGUID,
                                                      Date    effectiveTime) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String methodName = "removeGovernanceDefinitionFromElement";

        if (permittedSynchronization != PermittedSynchronization.TO_THIRD_PARTY)
        {
            stewardshipExchangeClient.removeGovernanceDefinitionFromElement(userId, assetManagerGUID, assetManagerName, definitionGUID, elementGUID, effectiveTime, forLineage, forDuplicateProcessing);
        }
        else
        {
            throw new UserNotAuthorizedException(CatalogIntegratorErrorCode.NOT_PERMITTED_SYNCHRONIZATION.getMessageDefinition(
                    permittedSynchronization.getName(),
                    connectorName,
                    methodName),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 userId);
        }
    }


    /**
     * Retrieve the governance definitions linked via a "GovernedBy" relationship to the requested element.
     *
     * @param elementGUID unique identifier of the element
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<GovernanceDefinitionElement> getGovernedByDefinitions(String  elementGUID,
                                                                      int     startFrom,
                                                                      int     pageSize,
                                                                      Date    effectiveTime) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException
    {
        return stewardshipExchangeClient.getGovernedByDefinitions(userId, assetManagerGUID, assetManagerName, elementGUID, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the elements linked via a "GovernedBy" relationship to the requested governance definition.
     *
     * @param governanceDefinitionGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelatedMetadataElementSummary> getGovernedElements(String  governanceDefinitionGUID,
                                                                   int     startFrom,
                                                                   int     pageSize,
                                                                   Date    effectiveTime) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        return stewardshipExchangeClient.getGovernedElements(userId, assetManagerGUID, assetManagerName, governanceDefinitionGUID, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were used to create the requested element.  Typically only one element is returned.
     *
     * @param elementGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelatedMetadataElementSummary> getSourceElements(String  elementGUID,
                                                                 int     startFrom,
                                                                 int     pageSize,
                                                                 Date    effectiveTime) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        return stewardshipExchangeClient.getSourceElements(userId, assetManagerGUID, assetManagerName, elementGUID, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve the elements linked via a "SourceFrom" relationship to the requested element.
     * The elements returned were created using the requested element as a template.
     *
     * @param elementGUID unique identifier of the glossary term that the returned elements are linked to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     * @param effectiveTime the time that the retrieved elements must be effective for
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<RelatedMetadataElementSummary> getElementsSourceFrom(String  elementGUID,
                                                                     int     startFrom,
                                                                     int     pageSize,
                                                                     Date    effectiveTime) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        return stewardshipExchangeClient.getElementsSourceFrom(userId, assetManagerGUID, assetManagerName, elementGUID, startFrom, pageSize, effectiveTime, forLineage, forDuplicateProcessing);
    }


    /**
     * Retrieve elements of the requested type name.
     *
     * @param findProperties  open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> getElements(FindProperties     findProperties,
                                                    int                startFrom,
                                                    int                pageSize) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        return stewardshipExchangeClient.getElements(userId,
                                                     findProperties,
                                                     startFrom,
                                                     pageSize,
                                                     forLineage,
                                                     forDuplicateProcessing);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must match exactly.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */

    public List<MetadataElementSummary> getElementsByPropertyValue(FindPropertyNamesProperties findProperties,
                                                                   int                         startFrom,
                                                                   int                         pageSize) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        final String methodName = "getElementsByPropertyValue";

        return stewardshipExchangeClient.getElementsByPropertyValue(userId,
                                                                    findProperties,
                                                                    startFrom,
                                                                    pageSize,
                                                                    forLineage,
                                                                    forDuplicateProcessing);
    }


    /**
     * Retrieve elements by a value found in one of the properties specified.  The value must be contained in the
     * properties rather than needing to be an exact match.
     * An open metadata type name may be supplied to restrict the results.
     *
     * @param findProperties properties and optional open metadata type to search on
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of related elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<MetadataElementSummary> findElementsByPropertyValue(FindPropertyNamesProperties findProperties,
                                                                    int                         startFrom,
                                                                    int                         pageSize) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException
    {
        return stewardshipExchangeClient.findElementsByPropertyValue(userId,
                                                                     findProperties,
                                                                     startFrom,
                                                                     pageSize,
                                                                     forLineage,
                                                                     forDuplicateProcessing);
    }
}
