/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.AssetConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.ElementHeaderConverter;
import org.odpi.openmetadata.accessservices.assetmanager.converters.SoftwareCapabilityConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.FilesAndFoldersHandler;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelationshipElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DataAssetExchangeClient is the server-side for managing Data Assets.
 */
public class DataAssetExchangeHandler extends ExchangeHandlerBase
{
    private final AssetHandler<DataAssetElement> assetHandler;
    private final FilesAndFoldersHandler<SoftwareCapabilityElement, DataAssetElement, DataAssetElement> filesAndFoldersHandler;

    private final static String assetGUIDParameterName = "assetGUID";


    /**
     * Construct the data asset exchange handler with information needed to work with asset related objects
     * for Asset Manager OMAS.
     *
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve instances from.
     * @param defaultZones list of zones that the access service should set in all new instances.
     * @param publishZones list of zones that the access service sets up in published instances.
     * @param auditLog destination for audit log events.
     */
    public DataAssetExchangeHandler(String                             serviceName,
                                    String                             serverName,
                                    InvalidParameterHandler            invalidParameterHandler,
                                    RepositoryHandler                  repositoryHandler,
                                    OMRSRepositoryHelper               repositoryHelper,
                                    String                             localServerUserId,
                                    OpenMetadataServerSecurityVerifier securityVerifier,
                                    List<String>                       supportedZones,
                                    List<String>                       defaultZones,
                                    List<String>                       publishZones,
                                    AuditLog                           auditLog)
    {
        super(serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

        AssetConverter<DataAssetElement> assetConverter = new AssetConverter<>(repositoryHelper, serviceName, serverName);

        assetHandler = new AssetHandler<>(assetConverter,
                                          DataAssetElement.class,
                                          serviceName,
                                          serverName,
                                          invalidParameterHandler,
                                          repositoryHandler,
                                          repositoryHelper,
                                          localServerUserId,
                                          securityVerifier,
                                          supportedZones,
                                          defaultZones,
                                          publishZones,
                                          auditLog);

        filesAndFoldersHandler = new FilesAndFoldersHandler<>(new SoftwareCapabilityConverter<>(repositoryHelper, serviceName, serverName),
                                                              SoftwareCapabilityElement.class,
                                                              assetConverter,
                                                              DataAssetElement.class,
                                                              assetConverter,
                                                              DataAssetElement.class,
                                                              serviceName,
                                                              serverName,
                                                              invalidParameterHandler,
                                                              repositoryHandler,
                                                              repositoryHelper,
                                                              localServerUserId,
                                                              securityVerifier,
                                                              supportedZones,
                                                              defaultZones,
                                                              publishZones,
                                                              auditLog);
    }


    /* ========================================================
     * Managing the externalIds and related correlation properties.
     */



    /**
     * Update each returned element with details of the correlation properties for the supplied asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param results list of elements
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToDataAssets(String                 userId,
                                                      String                 assetManagerGUID,
                                                      String                 assetManagerName,
                                                      List<DataAssetElement> results,
                                                      boolean                forLineage,
                                                      boolean                forDuplicateProcessing,
                                                      Date                   effectiveTime,
                                                      String                 methodName) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        if (results != null)
        {
            for (DataAssetElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                element.getElementHeader().getGUID(),
                                                                                assetGUIDParameterName,
                                                                                OpenMetadataType.ASSET.typeName,
                                                                                assetManagerGUID,
                                                                                assetManagerName,
                                                                                forLineage,
                                                                                forDuplicateProcessing,
                                                                                effectiveTime,
                                                                                methodName));

                    this.getSupplementaryProperties(element.getElementHeader().getGUID(),
                                                    assetGUIDParameterName,
                                                    OpenMetadataType.ASSET.typeName,
                                                    element.getDataAssetProperties(),
                                                    forLineage,
                                                    forDuplicateProcessing,
                                                    effectiveTime,
                                                    methodName);
                }
            }
        }
    }


    /* ======================================================================================
     * The Asset entity is the top level element to describe an implemented data asset such as a data store or data set.
     */

    /**
     * Create a new metadata element to represent the root of an asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param assetProperties properties to store
     * @param forLineage the request is to support lineage retrieval this means entities with the Memento classification can be returned
     * @param forDuplicateProcessing the request is for duplicate processing and so must not deduplicate
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAsset(String                        userId,
                                  MetadataCorrelationProperties correlationProperties,
                                  boolean                       assetManagerIsHome,
                                  DataAssetProperties           assetProperties,
                                  boolean                       forLineage,
                                  boolean                       forDuplicateProcessing,
                                  Date                          effectiveTime,
                                  String                        methodName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        final String propertiesParameterName     = "assetProperties";
        final String qualifiedNameParameterName  = "assetProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(assetProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String typeName = OpenMetadataType.ASSET.typeName;

        if (assetProperties.getTypeName() != null)
        {
            typeName = assetProperties.getTypeName();
        }

        Map<String, Object> assetExtendedProperties = new HashMap<>();
        if (assetProperties.getExtendedProperties() != null)
        {
            assetExtendedProperties.putAll(assetProperties.getExtendedProperties());
        }

        if (assetProperties instanceof DataStoreProperties dataStoreProperties)
        {
            assetExtendedProperties.put(OpenMetadataProperty.PATH_NAME.name, dataStoreProperties.getPathName());
            assetExtendedProperties.put(OpenMetadataProperty.STORE_CREATE_TIME.name, dataStoreProperties.getCreateTime());
            assetExtendedProperties.put(OpenMetadataProperty.STORE_UPDATE_TIME.name, dataStoreProperties.getModifiedTime());
        }

        if (assetExtendedProperties.isEmpty())
        {
            assetExtendedProperties = null;
        }

        String assetGUID = assetHandler.createAssetInRepository(userId,
                                                                this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                                this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                                assetProperties.getQualifiedName(),
                                                                assetProperties.getName(),
                                                                assetProperties.getResourceName(),
                                                                assetProperties.getVersionIdentifier(),
                                                                assetProperties.getResourceDescription(),
                                                                assetProperties.getDeployedImplementationType(),
                                                                assetProperties.getAdditionalProperties(),
                                                                typeName,
                                                                assetExtendedProperties,
                                                                InstanceStatus.ACTIVE,
                                                                assetProperties.getEffectiveFrom(),
                                                                assetProperties.getEffectiveTo(),
                                                                effectiveTime,
                                                                methodName);

        if (assetGUID != null)
        {
            this.maintainSupplementaryProperties(userId,
                                                 assetGUID,
                                                 assetGUIDParameterName,
                                                 OpenMetadataType.ASSET.typeName,
                                                 OpenMetadataType.ASSET.typeName,
                                                 assetProperties.getQualifiedName(),
                                                 assetProperties,
                                                 true,
                                                 forLineage,
                                                 forDuplicateProcessing,
                                                 effectiveTime,
                                                 methodName);

            if (assetProperties instanceof DataStoreProperties dataStoreProperties)
            {
                if ((dataStoreProperties.getEncodingType() != null) ||
                            (dataStoreProperties.getEncodingLanguage() != null) ||
                            (dataStoreProperties.getEncodingDescription() != null) ||
                            (dataStoreProperties.getEncodingProperties() != null))
                {
                    InstanceProperties classificationProperties;

                    classificationProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                            null,
                                                                                            OpenMetadataProperty.ENCODING.name,
                                                                                            dataStoreProperties.getEncodingType(),
                                                                                            methodName);
                    classificationProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                            classificationProperties,
                                                                                            OpenMetadataProperty.ENCODING_LANGUAGE.name,
                                                                                            dataStoreProperties.getEncodingLanguage(),
                                                                                            methodName);
                    classificationProperties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                                            classificationProperties,
                                                                                            OpenMetadataProperty.ENCODING_DESCRIPTION.name,
                                                                                            dataStoreProperties.getEncodingDescription(),
                                                                                            methodName);
                    classificationProperties = repositoryHelper.addStringMapPropertyToInstance(serviceName,
                                                                                               classificationProperties,
                                                                                               OpenMetadataProperty.ENCODING_PROPERTIES.name,
                                                                                               dataStoreProperties.getEncodingProperties(),
                                                                                               methodName);


                    assetHandler.setClassificationInRepository(userId,
                                                               this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                               this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                               assetGUID,
                                                               assetGUIDParameterName,
                                                               OpenMetadataType.DATA_STORE.typeName,
                                                               OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeGUID,
                                                               OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName,
                                                               classificationProperties,
                                                               true,
                                                               forLineage,
                                                               forDuplicateProcessing,
                                                               effectiveTime,
                                                               methodName);
                }
            }

            /*
             * For files and folders
             */
            if (((repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.DATA_FILE.typeName)) ||
                 (repositoryHelper.isTypeOf(serviceName, typeName, OpenMetadataType.DATA_FOLDER.typeName))) &&
                (assetExtendedProperties != null) &&
                (assetExtendedProperties.get(OpenMetadataProperty.PATH_NAME.name) != null))
            {
                final String pathNameParameterName = "assetProperties.getExtendedProperties().get(parameterName)";
                filesAndFoldersHandler.addFileAssetPath(userId,
                                                        this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                        this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                        assetGUID,
                                                        assetGUIDParameterName,
                                                        typeName,
                                                        assetProperties.getExtendedProperties().get(OpenMetadataProperty.PATH_NAME.name).toString(),
                                                        pathNameParameterName,
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);
            }

            this.createExternalIdentifier(userId,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          OpenMetadataType.ASSET.typeName,
                                          correlationProperties,
                                          forLineage,
                                          forDuplicateProcessing,
                                          effectiveTime,
                                          methodName);
        }

        return assetGUID;
    }


    /**
     * Create a new metadata element to represent an asset using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetManagerIsHome ensure that only the asset manager can update this asset
     * @param templateGUID unique identifier of the metadata element to copy
     * @param templateProperties properties that override the template
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createDataAssetFromTemplate(String                        userId,
                                              MetadataCorrelationProperties correlationProperties,
                                              boolean                       assetManagerIsHome,
                                              String                        templateGUID,
                                              TemplateProperties            templateProperties,
                                              String                        methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String templateGUIDParameterName   = "templateGUID";
        final String propertiesParameterName     = "templateProperties";
        final String qualifiedNameParameterName  = "templateProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(templateGUID, templateGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(templateProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(templateProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String assetGUID = assetHandler.addAssetFromTemplate(userId,
                                                             this.getExternalSourceGUID(correlationProperties, assetManagerIsHome),
                                                             this.getExternalSourceName(correlationProperties, assetManagerIsHome),
                                                             templateGUID,
                                                             templateGUIDParameterName,
                                                             OpenMetadataType.ASSET.typeGUID,
                                                             OpenMetadataType.ASSET.typeName,
                                                             templateProperties.getQualifiedName(),
                                                             qualifiedNameParameterName,
                                                             templateProperties.getDisplayName(),
                                                             templateProperties.getVersionIdentifier(),
                                                             templateProperties.getDescription(),
                                                             null,
                                                             templateProperties.getPathName(),
                                                             templateProperties.getNetworkAddress(),
                                                             false,
                                                             false,
                                                             new Date(),
                                                             methodName);
        if (assetGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          OpenMetadataType.ASSET.typeName,
                                          correlationProperties,
                                          false,
                                          false,
                                          null,
                                          methodName);
        }

        return assetGUID;
    }


    /**
     * Update the metadata element representing an asset.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param assetProperties new properties for this element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateDataAsset(String                        userId,
                                MetadataCorrelationProperties correlationProperties,
                                String                        assetGUID,
                                boolean                       isMergeUpdate,
                                DataAssetProperties           assetProperties,
                                boolean                       forLineage,
                                boolean                       forDuplicateProcessing,
                                Date                          effectiveTime,
                                String                        methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String assetGUIDParameterName      = "assetGUID";
        final String propertiesParameterName     = "assetProperties";
        final String qualifiedNameParameterName  = "assetProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);
        invalidParameterHandler.validateObject(assetProperties, propertiesParameterName, methodName);
        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);
        }

        this.validateExternalIdentifier(userId,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        OpenMetadataType.ASSET.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        assetHandler.updateAsset(userId,
                                 this.getExternalSourceGUID(correlationProperties),
                                 this.getExternalSourceName(correlationProperties),
                                 assetGUID,
                                 assetGUIDParameterName,
                                 assetProperties.getQualifiedName(),
                                 assetProperties.getName(),
                                 assetProperties.getResourceName(),
                                 assetProperties.getVersionIdentifier(),
                                 assetProperties.getResourceDescription(),
                                 assetProperties.getDeployedImplementationType(),
                                 assetProperties.getAdditionalProperties(),
                                 OpenMetadataType.ASSET.typeGUID,
                                 OpenMetadataType.ASSET.typeName,
                                 supportedZones,
                                 assetProperties.getExtendedProperties(),
                                 assetProperties.getEffectiveFrom(),
                                 assetProperties.getEffectiveTo(),
                                 isMergeUpdate,
                                 forLineage,
                                 forDuplicateProcessing,
                                 effectiveTime,
                                 methodName);

        this.maintainSupplementaryProperties(userId,
                                             assetGUID,
                                             assetGUIDParameterName,
                                             OpenMetadataType.ASSET.typeName,
                                             OpenMetadataType.ASSET.typeName,
                                             assetProperties.getQualifiedName(),
                                             assetProperties,
                                             isMergeUpdate,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
    }


    /**
     * Add the asset origin classification to an asset.  The method needs to build a before an after image of the
     * asset to perform a security check before the update is pushed to the repository.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param organizationGUIDParameterName parameter name supplying organizationGUID
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param businessCapabilityGUIDParameterName parameter name supplying businessCapabilityGUID
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @param effectiveFrom the time that the elements must be effective from (null for any time)
     * @param effectiveTo the time that the elements must be effective from (null for any time)
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     *
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  addAssetOrigin(String                userId,
                                String                assetGUID,
                                String                assetGUIDParameterName,
                                String                organizationGUID,
                                String                organizationGUIDParameterName,
                                String                businessCapabilityGUID,
                                String                businessCapabilityGUIDParameterName,
                                Map<String, String>   otherOriginValues,
                                Date                  effectiveFrom,
                                Date                  effectiveTo,
                                boolean               isMergeUpdate,
                                boolean               forLineage,
                                boolean               forDuplicateProcessing,
                                Date                  effectiveTime,
                                String                methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        assetHandler.addAssetOrigin(userId,
                                    assetGUID,
                                    assetGUIDParameterName,
                                    organizationGUID,
                                    organizationGUIDParameterName,
                                    businessCapabilityGUID,
                                    businessCapabilityGUIDParameterName,
                                    otherOriginValues, effectiveFrom,
                                    effectiveTo, isMergeUpdate,
                                    forLineage,
                                    forDuplicateProcessing,
                                    effectiveTime,
                                    methodName);
    }


    /**
     * Remove the asset origin classification to an asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param assetGUIDParameterName parameter name supplying assetGUID
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @param methodName calling method
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    public void  removeAssetOrigin(String  userId,
                                   String  assetGUID,
                                   String  assetGUIDParameterName,
                                   boolean forLineage,
                                   boolean forDuplicateProcessing,
                                   Date    effectiveTime,
                                   String  methodName) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException
    {
        assetHandler.removeAssetOrigin(userId, assetGUID, assetGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }

    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishDataAsset(String  userId,
                                 String  assetGUID,
                                 boolean forLineage,
                                 boolean forDuplicateProcessing,
                                 Date    effectiveTime,
                                 String  methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        assetHandler.publishAsset(userId, assetGUID, assetGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawDataAsset(String  userId,
                                  String  assetGUID,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime,
                                  String  methodName) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        assetHandler.withdrawAsset(userId, assetGUID, assetGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeDataAsset(String                        userId,
                                MetadataCorrelationProperties correlationProperties,
                                String                        assetGUID,
                                boolean                       forLineage,
                                boolean                       forDuplicateProcessing,
                                Date                          effectiveTime,
                                String                        methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        OpenMetadataType.ASSET.typeName,
                                        correlationProperties,
                                        forLineage,
                                        forDuplicateProcessing,
                                        effectiveTime,
                                        methodName);

        assetHandler.deleteBeanInRepository(userId,
                                            getExternalSourceGUID(correlationProperties),
                                            getExternalSourceName(correlationProperties),
                                            assetGUID,
                                            assetGUIDParameterName,
                                            OpenMetadataType.ASSET.typeGUID,
                                            OpenMetadataType.ASSET.typeName,
                                            null,
                                            null,
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
    }


    /**
     * Classify the asset to indicate that it can be used as reference data.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setDataAssetAsReferenceData(String  userId,
                                            String  assetGUID,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing,
                                            Date    effectiveTime,
                                            String  methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";
        
        assetHandler.classifyAssetAsReferenceData(userId, assetGUID, assetGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }


    /**
     * Remove the reference data designation from the asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to remove
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearDataAssetAsReferenceData(String  userId,
                                              String  assetGUID,
                                              boolean forLineage,
                                              boolean forDuplicateProcessing,
                                              Date    effectiveTime,
                                              String  methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";
        
        assetHandler.classifyAssetAsReferenceData(userId, assetGUID, assetGUIDParameterName, forLineage, forDuplicateProcessing, effectiveTime, methodName);
    }



    /**
     * Link two asset together.
     * Use information from the relationship type definition to ensure the fromAssetGUID and toAssetGUID are the right way around.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param assetManagerIsHome ensure that only the process manager can update this process
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param relationshipProperties unique identifier for this relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return unique identifier of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupRelatedDataAsset(String                 userId,
                                        String                 assetManagerGUID,
                                        String                 assetManagerName,
                                        boolean                assetManagerIsHome,
                                        String                 relationshipTypeName,
                                        String                 fromAssetGUID,
                                        String                 toAssetGUID,
                                        RelationshipProperties relationshipProperties,
                                        boolean                forLineage,
                                        boolean                forDuplicateProcessing,
                                        Date                   effectiveTime,
                                        String                 methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String fromAssetGUIDParameterName = "fromAssetGUID";
        final String toAssetGUIDParameterName   = "toAssetGUID";
        final String typeNameParameterName      = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fromAssetGUID, fromAssetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(toAssetGUID, toAssetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName, null, serviceName, methodName, repositoryHelper);

        InstanceProperties instanceProperties = null;

        if (relationshipProperties != null)
        {
            if (relationshipProperties instanceof DataContentForDataSetProperties dataContentForDataSetProperties)
            {
                instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, null, OpenMetadataType.QUERY_ID_PROPERTY_NAME, dataContentForDataSetProperties.getQueryId(), methodName);
                instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, instanceProperties, OpenMetadataType.QUERY_PROPERTY_NAME, dataContentForDataSetProperties.getQuery(), methodName);
            }
        }

        if (assetManagerIsHome)
        {
            return assetHandler.linkElementToElement(userId,
                                                     assetManagerGUID,
                                                     assetManagerName,
                                                     fromAssetGUID,
                                                     fromAssetGUIDParameterName,
                                                     OpenMetadataType.ASSET.typeName,
                                                     toAssetGUID,
                                                     toAssetGUIDParameterName,
                                                     OpenMetadataType.ASSET.typeName,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     supportedZones,
                                                     relationshipTypeGUID,
                                                     relationshipTypeName,
                                                     instanceProperties,
                                                     null,
                                                     null,
                                                     effectiveTime,
                                                     methodName);
        }
        else
        {
            return assetHandler.linkElementToElement(userId,
                                                     null,
                                                     null,
                                                     fromAssetGUID,
                                                     fromAssetGUIDParameterName,
                                                     OpenMetadataType.ASSET.typeName,
                                                     toAssetGUID,
                                                     toAssetGUIDParameterName,
                                                     OpenMetadataType.ASSET.typeName,
                                                     forLineage,
                                                     forDuplicateProcessing,
                                                     supportedZones,
                                                     relationshipTypeGUID,
                                                     relationshipTypeName,
                                                     instanceProperties,
                                                     null,
                                                     null,
                                                     effectiveTime,
                                                     methodName);
        }
    }


    /**
     * Retrieve the relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName type name of relationship to create
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public RelationshipElement getAssetRelationship(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  relationshipTypeName,
                                                    String  fromAssetGUID,
                                                    String  toAssetGUID,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing,
                                                    Date    effectiveTime,
                                                    String  methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String fromAssetGUIDParameterName = "fromAssetGUID";
        final String toAssetGUIDParameterName   = "toAssetGUID";
        final String typeNameParameterName      = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fromAssetGUID, fromAssetGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(toAssetGUID, toAssetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName, null, serviceName, methodName, repositoryHelper);

        Relationship  relationship = assetHandler.getUniqueAttachmentLink(userId,
                                                                          fromAssetGUID,
                                                                          fromAssetGUIDParameterName,
                                                                          OpenMetadataType.ASSET.typeName,
                                                                          relationshipTypeGUID,
                                                                          relationshipTypeName,
                                                                          toAssetGUID,
                                                                          OpenMetadataType.ASSET.typeName,
                                                                          2,
                                                                          null,
                                                                          null,
                                                                          SequencingOrder.CREATION_DATE_RECENT,
                                                                          null,
                                                                          forLineage,
                                                                          forDuplicateProcessing,
                                                                          effectiveTime,
                                                                          methodName);

        return getRelationshipElement(relationship, methodName);
    }


    /**
     * Convert an OMRS relationship into an Asset Manager's RelationshipElement.
     *
     * @param relationship retrieved relationship
     * @param methodName calling method
     * @return element to return
     */
    private RelationshipElement getRelationshipElement(Relationship relationship,
                                                       String       methodName) throws PropertyServerException
    {
        RelationshipElement relationshipElement = null;

        if (relationship != null)
        {
            ElementHeaderConverter<ElementHeader> elementHeaderConverter = new ElementHeaderConverter<>(repositoryHelper, serviceName, serverName);

            relationshipElement = new RelationshipElement();

            relationshipElement.setRelationshipHeader(elementHeaderConverter.getNewBean(ElementHeader.class, relationship, methodName));
            relationshipElement.setEnd1GUID(elementHeaderConverter.getNewBean(ElementHeader.class, relationship.getEntityOneProxy(), methodName));
            relationshipElement.setEnd2GUID(elementHeaderConverter.getNewBean(ElementHeader.class, relationship.getEntityTwoProxy(), methodName));

            if (relationship.getProperties() != null)
            {
                if (OpenMetadataType.DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP.typeName.equals(relationship.getType().getTypeDefName()))
                {
                    DataContentForDataSetProperties properties = new DataContentForDataSetProperties();

                    properties.setQueryId(repositoryHelper.getStringProperty(serviceName, OpenMetadataType.QUERY_ID_PROPERTY_NAME, relationship.getProperties(), methodName));
                    properties.setQuery(repositoryHelper.getStringProperty(serviceName, OpenMetadataType.QUERY_PROPERTY_NAME, relationship.getProperties(), methodName));

                    properties.setEffectiveFrom(relationship.getProperties().getEffectiveFromTime());
                    properties.setEffectiveTo(relationship.getProperties().getEffectiveFromTime());

                    relationshipElement.setRelationshipProperties(properties);
                }
                else
                {
                    RelationshipProperties properties = new RelationshipProperties();

                    properties.setEffectiveFrom(relationship.getProperties().getEffectiveFromTime());
                    properties.setEffectiveTo(relationship.getProperties().getEffectiveFromTime());

                    relationshipElement.setRelationshipProperties(properties);
                }
            }
        }

        return relationshipElement;
    }


    /**
     * Update relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName type name of relationship to update
     * @param relationshipGUID unique identifier of the relationship
     * @param relationshipProperties description and/or purpose of the relationship
     * @param isMergeUpdate should the new properties be merged with the existing properties, or replace them entirely
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void   updateAssetRelationship(String                 userId,
                                          String                 assetManagerGUID,
                                          String                 assetManagerName,
                                          String                 relationshipTypeName,
                                          String                 relationshipGUID,
                                          boolean                isMergeUpdate,
                                          RelationshipProperties relationshipProperties,
                                          boolean                forLineage,
                                          boolean                forDuplicateProcessing,
                                          Date                   effectiveTime,
                                          String                 methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        final String relationshipGUIDParameterName = "relationshipGUID";
        final String typeNameParameterName         = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        InstanceProperties instanceProperties = null;

        if (relationshipProperties != null)
        {
            if (relationshipProperties instanceof DataContentForDataSetProperties dataContentForDataSetProperties)
            {
                instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, null, OpenMetadataType.QUERY_ID_PROPERTY_NAME, dataContentForDataSetProperties.getQueryId(), methodName);
                instanceProperties = repositoryHelper.addStringPropertyToInstance(serviceName, instanceProperties, OpenMetadataType.QUERY_PROPERTY_NAME, dataContentForDataSetProperties.getQuery(), methodName);
            }
        }

        assetHandler.updateElementToElementLink(userId,
                                                assetManagerGUID,
                                                assetManagerName,
                                                relationshipGUID,
                                                relationshipGUIDParameterName,
                                                relationshipTypeName,
                                                forLineage,
                                                forDuplicateProcessing,
                                                supportedZones,
                                                isMergeUpdate,
                                                instanceProperties,
                                                effectiveTime,
                                                methodName);
    }


    /**
     * Remove the relationship between two elements.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName type name of relationship to delete
     * @param relationshipGUID unique identifier of the relationship
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetRelationship(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  relationshipTypeName,
                                       String  relationshipGUID,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime,
                                       String  methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String relationshipGUIDParameterName = "relationshipGUID";
        final String typeNameParameterName         = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(relationshipGUID, relationshipGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        assetHandler.deleteRelationship(userId,
                                        assetManagerGUID,
                                        assetManagerName,
                                        relationshipGUID,
                                        relationshipGUIDParameterName,
                                        relationshipTypeName,
                                        forLineage,
                                        forDuplicateProcessing,
                                        assetHandler.getSupportedZones(),
                                        effectiveTime,
                                        methodName);
    }


    /**
     * Retrieve the requested relationships linked from a specific element at end 2.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName type name of relationship to delete
     * @param fromAssetGUID unique identifier of the asset at end 1 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public List<RelationshipElement> getRelatedAssetsAtEnd2(String  userId,
                                                            String  assetManagerGUID,
                                                            String  assetManagerName,
                                                            String  relationshipTypeName,
                                                            String  fromAssetGUID,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String fromAssetGUIDParameterName = "fromAssetGUID";
        final String typeNameParameterName      = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(fromAssetGUID, fromAssetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName, null, serviceName, methodName, repositoryHelper);

        List<Relationship> relationships = assetHandler.getAttachmentLinks(userId,
                                                                           fromAssetGUID,
                                                                           fromAssetGUIDParameterName,
                                                                           OpenMetadataType.ASSET.typeName,
                                                                           relationshipTypeGUID,
                                                                           relationshipTypeName,
                                                                           null,
                                                                           OpenMetadataType.ASSET.typeName,
                                                                           2,
                                                                           null,
                                                                           null,
                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                           null,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           startFrom,
                                                                           pageSize,
                                                                           effectiveTime,
                                                                           methodName);

        if (relationships != null)
        {
            List<RelationshipElement> relationshipElements = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                relationshipElements.add(getRelationshipElement(relationship, methodName));
            }

            return relationshipElements;
        }

        return null;
    }


    /**
     * Retrieve the relationships linked from a specific element at end 2 of the relationship.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param relationshipTypeName type name of relationship to delete
     * @param toAssetGUID unique identifier of the asset at end 2 of the relationship
     * @param startFrom start position for results
     * @param pageSize     maximum number of results
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return unique identifier and properties of the relationship
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @SuppressWarnings(value = "unused")
    public List<RelationshipElement> getRelatedAssetsAtEnd1(String  userId,
                                                            String  assetManagerGUID,
                                                            String  assetManagerName,
                                                            String  relationshipTypeName,
                                                            String  toAssetGUID,
                                                            int     startFrom,
                                                            int     pageSize,
                                                            boolean forLineage,
                                                            boolean forDuplicateProcessing,
                                                            Date    effectiveTime,
                                                            String  methodName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String toAssetGUIDParameterName = "toAssetGUID";
        final String typeNameParameterName    = "relationshipTypeName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(toAssetGUID, toAssetGUIDParameterName, methodName);
        invalidParameterHandler.validateName(relationshipTypeName, typeNameParameterName, methodName);

        String relationshipTypeGUID = invalidParameterHandler.validateTypeName(relationshipTypeName, null, serviceName, methodName, repositoryHelper);

        List<Relationship> relationships = assetHandler.getAttachmentLinks(userId,
                                                                           toAssetGUID,
                                                                           toAssetGUIDParameterName,
                                                                           OpenMetadataType.ASSET.typeName,
                                                                           relationshipTypeGUID,
                                                                           relationshipTypeName,
                                                                           null,
                                                                           OpenMetadataType.ASSET.typeName,
                                                                           1,
                                                                           null,
                                                                           null,
                                                                           SequencingOrder.CREATION_DATE_RECENT,
                                                                           null,
                                                                           forLineage,
                                                                           forDuplicateProcessing,
                                                                           startFrom,
                                                                           pageSize,
                                                                           effectiveTime,
                                                                           methodName);

        if (relationships != null)
        {
            List<RelationshipElement> relationshipElements = new ArrayList<>();

            for (Relationship relationship : relationships)
            {
                relationshipElements.add(getRelationshipElement(relationship, methodName));
            }

            return relationshipElements;
        }

        return null;
    }


    /**
     * Retrieve the list of asset metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> findDataAssets(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  searchString,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        List<DataAssetElement> results = assetHandler.findAssets(userId,
                                                                 searchString,
                                                                 searchStringParameterName,
                                                                 startFrom,
                                                                 pageSize,
                                                                 forLineage,
                                                                 forDuplicateProcessing,
                                                                 effectiveTime,
                                                                 methodName);
        
        addCorrelationPropertiesToDataAssets(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             results,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);
        
        return results;
    }


    /**
     * Step through the assets visible to this caller.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> scanDataAssets(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing,
                                                 Date    effectiveTime,
                                                 String  methodName) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        List<DataAssetElement> results = assetHandler.assetScan(userId,
                                                                OpenMetadataType.ASSET.typeGUID,
                                                                OpenMetadataType.ASSET.typeName,
                                                                startFrom,
                                                                pageSize,
                                                                forLineage,
                                                                forDuplicateProcessing,
                                                                effectiveTime,
                                                                methodName);

        addCorrelationPropertiesToDataAssets(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             results,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);

        return results;
    }


    /**
     * Retrieve the list of asset metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsByName(String  userId,
                                                      String  assetManagerGUID,
                                                      String  assetManagerName,
                                                      String  name,
                                                      int     startFrom,
                                                      int     pageSize,
                                                      boolean forLineage,
                                                      boolean forDuplicateProcessing,
                                                      Date    effectiveTime,
                                                      String  methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String nameParameterName = "name";

        List<DataAssetElement> results = assetHandler.findAssetsByName(userId,
                                                                       OpenMetadataType.ASSET.typeGUID,
                                                                       OpenMetadataType.ASSET.typeName,
                                                                       name,
                                                                       nameParameterName,
                                                                       startFrom,
                                                                       pageSize,
                                                                       forLineage,
                                                                       forDuplicateProcessing,
                                                                       effectiveTime,
                                                                       methodName);

        addCorrelationPropertiesToDataAssets(userId,
                                             assetManagerGUID,
                                             assetManagerName,
                                             results,
                                             forLineage,
                                             forDuplicateProcessing,
                                             effectiveTime,
                                             methodName);

        return results;
    }


    /**
     * Retrieve the list of assets created on behalf of the named asset manager.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<DataAssetElement> getDataAssetsForAssetManager(String  userId,
                                                               String  assetManagerGUID,
                                                               String  assetManagerName,
                                                               int     startFrom,
                                                               int     pageSize,
                                                               boolean forLineage,
                                                               boolean forDuplicateProcessing,
                                                               Date    effectiveTime,
                                                               String  methodName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        final String assetManagerGUIDParameterName = "assetManagerGUID";
        final String assetEntityParameterName = "assetEntity";
        final String assetGUIDParameterName = "assetEntity.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);

        List<DataAssetElement> results = new ArrayList<>();

        List<EntityDetail> assetEntities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                                assetManagerGUID,
                                                                                                assetManagerGUIDParameterName,
                                                                                                OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                                                                OpenMetadataType.ASSET.typeName,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                effectiveTime,
                                                                                                forLineage,
                                                                                                forDuplicateProcessing,
                                                                                                methodName);

        if (assetEntities != null)
        {
            for (EntityDetail assetEntity : assetEntities)
            {
                if (assetEntity != null)
                {
                    DataAssetElement dataAssetElement = assetHandler.getBeanFromEntity(userId,
                                                                                       assetEntity,
                                                                                       assetEntityParameterName,
                                                                                       methodName);

                    if (dataAssetElement != null)
                    {
                        dataAssetElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                             assetEntity.getGUID(),
                                                                                             assetGUIDParameterName,
                                                                                             OpenMetadataType.ASSET.typeName,
                                                                                             assetManagerGUID,
                                                                                             assetManagerName,
                                                                                             forLineage,
                                                                                             forDuplicateProcessing,
                                                                                             effectiveTime,
                                                                                             methodName));

                        this.getSupplementaryProperties(dataAssetElement.getElementHeader().getGUID(),
                                                        assetGUIDParameterName,
                                                        OpenMetadataType.ASSET.typeName,
                                                        dataAssetElement.getDataAssetProperties(),
                                                        forLineage,
                                                        forDuplicateProcessing,
                                                        effectiveTime,
                                                        methodName);

                        results.add(dataAssetElement);
                    }
                }
            }
        }

        if (results.isEmpty())
        {
            return null;
        }
        else
        {
            return results;
        }
    }


    /**
     * Retrieve the asset metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier of the requested metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime when should the elements be effected for - null is anytime; new Date() is now
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public DataAssetElement getDataAssetByGUID(String  userId,
                                               String  assetManagerGUID,
                                               String  assetManagerName,
                                               String  openMetadataGUID,
                                               boolean forLineage,
                                               boolean forDuplicateProcessing,
                                               Date    effectiveTime,
                                               String  methodName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String guidParameterName = "openMetadataGUID";

        DataAssetElement asset = assetHandler.getBeanFromRepository(userId,
                                                                    openMetadataGUID,
                                                                    guidParameterName,
                                                                    OpenMetadataType.ASSET.typeName,
                                                                    forLineage,
                                                                    forDuplicateProcessing,
                                                                    effectiveTime,
                                                                    methodName);

        if (asset != null)
        {
            asset.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                      openMetadataGUID,
                                                                      guidParameterName,
                                                                      OpenMetadataType.ASSET.typeName,
                                                                      assetManagerGUID,
                                                                      assetManagerName,
                                                                      forLineage,
                                                                      forDuplicateProcessing,
                                                                      effectiveTime,
                                                                      methodName));

            this.getSupplementaryProperties(asset.getElementHeader().getGUID(),
                                            assetGUIDParameterName,
                                            OpenMetadataType.ASSET.typeName,
                                            asset.getDataAssetProperties(),
                                            forLineage,
                                            forDuplicateProcessing,
                                            effectiveTime,
                                            methodName);
        }

        return asset;
    }
}
