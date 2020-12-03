/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.handlers;

import org.odpi.openmetadata.accessservices.assetmanager.converters.AssetConverter;
import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.*;
import org.odpi.openmetadata.accessservices.assetmanager.properties.*;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * DataAssetExchangeClient is the server-side for managing Data Assets, Schemas and Connections.
 */
public class DataAssetExchangeHandler extends ExchangeHandlerBase
{
    private AssetHandler<AssetElement>               assetHandler;

    private final static String assetGUIDParameterName        = "assetGUID";



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

        assetHandler = new AssetHandler<>(new AssetConverter<>(repositoryHelper, serviceName, serverName),
                                          AssetElement.class,
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
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    private void addCorrelationPropertiesToDataAssets(String             userId,
                                                      String             assetManagerGUID,
                                                      String             assetManagerName,
                                                      List<AssetElement> results,
                                                      String             methodName) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        if ((results != null) && (assetManagerGUID != null))
        {
            for (MetadataElement element : results)
            {
                if ((element != null) && (element.getElementHeader() != null) && (element.getElementHeader().getGUID() != null))
                {
                    element.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                 element.getElementHeader().getGUID(),
                                                                                 assetGUIDParameterName,
                                                                                 OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                 assetManagerGUID,
                                                                                 assetManagerName,
                                                                                 methodName));
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
     * @param methodName calling method
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createAsset(String                        userId,
                              MetadataCorrelationProperties correlationProperties,
                              boolean                       assetManagerIsHome,
                              AssetProperties               assetProperties,
                              String                        methodName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String propertiesParameterName     = "assetProperties";
        final String qualifiedNameParameterName  = "assetProperties.qualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateObject(assetProperties, propertiesParameterName, methodName);
        invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((assetManagerIsHome) && (correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        String typeName = OpenMetadataAPIMapper.ASSET_TYPE_NAME;

        if (assetProperties.getTypeName() != null)
        {
            typeName = assetProperties.getTypeName();
        }

        String typeGUID = invalidParameterHandler.validateTypeName(typeName,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                   serviceName,
                                                                   methodName,
                                                                   repositoryHelper);

        int ownerCategory = OwnerCategory.USER_ID.getOpenTypeOrdinal();

        if (assetProperties.getOwnerCategory() != null)
        {
            ownerCategory = assetProperties.getOwnerCategory().getOpenTypeOrdinal();
        }

        String assetGUID = assetHandler.createAssetInRepository(userId,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                assetProperties.getQualifiedName(),
                                                                assetProperties.getTechnicalName(),
                                                                assetProperties.getTechnicalDescription(),
                                                                assetProperties.getZoneMembership(),
                                                                assetProperties.getOwner(),
                                                                ownerCategory,
                                                                assetProperties.getOriginOrganizationGUID(),
                                                                assetProperties.getOriginBusinessCapabilityGUID(),
                                                                assetProperties.getOtherOriginValues(),
                                                                assetProperties.getAdditionalProperties(),
                                                                typeGUID,
                                                                typeName,
                                                                assetProperties.getExtendedProperties(),
                                                                methodName);

        if (assetGUID != null)
        {
            this.maintainSupplementaryProperties(userId,
                                                 assetGUID,
                                                 assetProperties.getQualifiedName(),
                                                 assetProperties,
                                                 false,
                                                 methodName);

            this.createExternalIdentifier(userId,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                          correlationProperties,
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
    public String createAssetFromTemplate(String                        userId,
                                          MetadataCorrelationProperties correlationProperties,
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
                                                             null,
                                                             null,
                                                             templateGUID,
                                                             templateGUIDParameterName,
                                                             OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                                             OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                             templateProperties.getQualifiedName(),
                                                             qualifiedNameParameterName,
                                                             templateProperties.getDisplayName(),
                                                             templateProperties.getDescription(),
                                                             methodName);
        if (assetGUID != null)
        {
            this.createExternalIdentifier(userId,
                                          assetGUID,
                                          assetGUIDParameterName,
                                          OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                          correlationProperties,
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
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateAsset(String                        userId,
                            MetadataCorrelationProperties correlationProperties,
                            String                        assetGUID,
                            boolean                       isMergeUpdate,
                            AssetProperties               assetProperties,
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
        invalidParameterHandler.validateName(assetProperties.getQualifiedName(), qualifiedNameParameterName, methodName);

        this.validateExternalIdentifier(userId,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        // todo - update classifications
        assetHandler.updateAsset(userId,
                                 externalSourceGUID,
                                 externalSourceName,
                                 assetGUID,
                                 assetGUIDParameterName,
                                 assetProperties.getQualifiedName(),
                                 assetProperties.getTechnicalName(),
                                 assetProperties.getTechnicalDescription(),
                                 assetProperties.getAdditionalProperties(),
                                 OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                 OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                 assetProperties.getExtendedProperties(),
                                 isMergeUpdate,
                                 methodName);

        this.maintainSupplementaryProperties(userId,
                                             assetGUID,
                                             assetProperties.getQualifiedName(),
                                             assetProperties,
                                             isMergeUpdate,
                                             methodName);
    }
    

    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Asset Manager OMAS).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to publish
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void publishAsset(String userId,
                             String assetGUID,
                             String methodName) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        assetHandler.publishAsset(userId, assetGUID, assetGUIDParameterName, methodName);
    }


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Asset Manager OMAS.  This is the setting when the database is first created).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to withdraw
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void withdrawAsset(String userId,
                              String assetGUID,
                              String methodName) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        assetHandler.withdrawAsset(userId, assetGUID, assetGUIDParameterName, methodName);
    }


    /**
     * Remove the metadata element representing an asset.  This will delete the asset and all anchored
     * elements such as schema and comments.
     *
     * @param userId calling user
     * @param correlationProperties  properties to help with the mapping of the elements in the external asset manager and open metadata
     * @param assetGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeAsset(String                        userId,
                            MetadataCorrelationProperties correlationProperties,
                            String                        assetGUID,
                            String                        methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        String externalSourceGUID = null;
        String externalSourceName = null;

        if ((correlationProperties != null) && (correlationProperties.getAssetManagerGUID() != null))
        {
            externalSourceGUID = correlationProperties.getAssetManagerGUID();
            externalSourceName = correlationProperties.getAssetManagerName();
        }

        this.validateExternalIdentifier(userId,
                                        assetGUID,
                                        assetGUIDParameterName,
                                        OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                        correlationProperties,
                                        methodName);

        assetHandler.deleteBeanInRepository(userId,
                                            externalSourceGUID,
                                            externalSourceName,
                                            assetGUID,
                                            assetGUIDParameterName,
                                            OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                            OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                            null,
                                            null,
                                            methodName);
    }


    /**
     * Classify the asset to indicate that it can be used as reference data.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to update
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setAssetAsReferenceData(String userId,
                                        String assetGUID,
                                        String methodName) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";
        
        assetHandler.classifyAssetAsReferenceData(userId, assetGUID, assetGUIDParameterName, methodName);
    }


    /**
     * Remove the reference data designation from the asset.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void clearAssetAsReferenceData(String userId,
                                          String assetGUID,
                                          String methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String assetGUIDParameterName = "assetGUID";
        
        assetHandler.classifyAssetAsReferenceData(userId, assetGUID, assetGUIDParameterName, methodName);
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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<AssetElement> findAssets(String userId,
                                         String assetManagerGUID,
                                         String assetManagerName,
                                         String searchString,
                                         int    startFrom,
                                         int    pageSize,
                                         String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        List<AssetElement> results = assetHandler.findAssets(userId,
                                                             searchString,
                                                             searchStringParameterName,
                                                             startFrom,
                                                             pageSize,
                                                             methodName);
        
        addCorrelationPropertiesToDataAssets(userId, assetManagerGUID, assetManagerName, results, methodName);
        
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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<AssetElement>   getAssetsByName(String userId,
                                                String assetManagerGUID,
                                                String assetManagerName,
                                                String name,
                                                int    startFrom,
                                                int    pageSize,
                                                String methodName) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException
    {
        final String nameParameterName = "name";

        List<AssetElement> results = assetHandler.findAssetsByName(userId,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_GUID,
                                                                   OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                   name,
                                                                   nameParameterName,
                                                                   startFrom,
                                                                   pageSize,
                                                                   methodName);

        addCorrelationPropertiesToDataAssets(userId, assetManagerGUID, assetManagerName, results, methodName);

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
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<AssetElement>   getAssetsForAssetManager(String userId,
                                                         String assetManagerGUID,
                                                         String assetManagerName,
                                                         int    startFrom,
                                                         int    pageSize,
                                                         String methodName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String assetManagerGUIDParameterName = "assetManagerGUID";
        final String assetEntityParameterName = "assetEntity";
        final String assetGUIDParameterName = "assetEntity.getGUID()";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(assetManagerGUID, assetManagerGUIDParameterName, methodName);

        List<AssetElement> results = new ArrayList<>();

        List<EntityDetail> assetEntities = externalIdentifierHandler.getElementEntitiesForScope(userId,
                                                                                                assetManagerGUID,
                                                                                                assetManagerGUIDParameterName,
                                                                                                OpenMetadataAPIMapper.SOFTWARE_SERVER_CAPABILITY_TYPE_NAME,
                                                                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                                startFrom,
                                                                                                pageSize,
                                                                                                methodName);

        if (assetEntities != null)
        {
            for (EntityDetail assetEntity : assetEntities)
            {
                if (assetEntity != null)
                {
                    AssetElement assetElement = assetHandler.getBeanFromEntity(userId,
                                                                                        assetEntity,
                                                                                        assetEntityParameterName,
                                                                                        methodName);

                    if (assetElement != null)
                    {
                        assetElement.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                                            assetEntity.getGUID(),
                                                                                            assetGUIDParameterName,
                                                                                            OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                                            assetManagerGUID,
                                                                                            assetManagerName,
                                                                                            methodName));

                        results.add(assetElement);
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
     * @param methodName calling method
     *
     * @return matching metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public AssetElement getAssetByGUID(String userId,
                                       String assetManagerGUID,
                                       String assetManagerName,
                                       String openMetadataGUID,
                                       String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String guidParameterName = "openMetadataGUID";

        AssetElement asset = assetHandler.getBeanFromRepository(userId,
                                                                openMetadataGUID,
                                                                guidParameterName,
                                                                OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                methodName);

        if (asset != null)
        {
            asset.setCorrelationHeaders(this.getCorrelationProperties(userId,
                                                                      openMetadataGUID,
                                                                      guidParameterName,
                                                                      OpenMetadataAPIMapper.ASSET_TYPE_NAME,
                                                                      assetManagerGUID,
                                                                      assetManagerName,
                                                                      methodName));
        }

        return asset;
    }
}
