/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.service;

import org.odpi.openmetadata.accessservice.assetcatalog.admin.AssetCatalogAdmin;
import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservice.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.responses.RelationshipsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.util.Converter;
import org.odpi.openmetadata.accessservice.assetcatalog.util.ExceptionUtil;
import org.odpi.openmetadata.adminservices.OMAGAccessServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceRegistration;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The AssetCatalogAssetService provides the server-side implementation of the Asset Catalog Open Metadata
 * Assess Service (OMAS).
 * This service provide the functionality to fetch asset's header, classification and properties.
 */
@Service
public class AssetCatalogAssetService {

    private static OMRSMetadataCollection metadataCollection;
    private Converter converter;
    private ExceptionUtil exceptionUtil;

    @Autowired
    public AssetCatalogAssetService(Converter converter, ExceptionUtil exceptionUtil) {
        AccessServiceDescription myDescription = AccessServiceDescription.ASSET_CATALOG_OMAS;

        AccessServiceRegistration myRegistration = new AccessServiceRegistration(myDescription.getAccessServiceCode(),
                myDescription.getAccessServiceName(),
                myDescription.getAccessServiceDescription(),
                myDescription.getAccessServiceWiki(),
                AccessServiceOperationalStatus.ENABLED,
                AssetCatalogAdmin.class.getName());
        OMAGAccessServiceRegistration.registerAccessService(myRegistration);

        this.converter = converter;
        this.exceptionUtil = exceptionUtil;
    }

    /**
     * Set up the repository connector that will service the REST Calls.
     *
     * @param localRepositoryConnector - link to the local repository responsible for servicing the REST calls.
     *                                 If localRepositoryConnector is null when a REST calls is received, the request
     *                                 is rejected.
     */
    public static void setRepositoryConnector(OMRSRepositoryConnector localRepositoryConnector) {
        try {
            AssetCatalogAssetService.metadataCollection = localRepositoryConnector.getMetadataCollection();
        } catch (Throwable error) {
        }
    }

    public AssetDescriptionResponse getAssetSummaryById(String userId, String assetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            EntitySummary entitySummary = getEntitySummary(userId, assetId);
            if (entitySummary != null) {
                AssetDescription assetDescription = converter.getAssetDescription(entitySummary);
                response.setAssetDescriptionList(Arrays.asList(assetDescription));
            }
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | InvalidParameterException
                | RepositoryErrorException e) {
            exceptionUtil.captureException(response, e);
        }
        return response;
    }

    public AssetDescriptionResponse getAssetDetailsById(String userId, String assetId) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            EntityDetail entityDetail = getEntityDetails(userId, assetId);
            if (entityDetail != null) {
                AssetDescription assetDescription = converter.getAssetDescription(entityDetail);
                response.setAssetDescriptionList(Arrays.asList(assetDescription));
            }
        } catch (UserNotAuthorizedException
                | EntityProxyOnlyException
                | EntityNotKnownException
                | InvalidParameterException
                | RepositoryErrorException e) {
            exceptionUtil.captureException(response, e);
        }
        return response;
    }

    public ClassificationsResponse getClassificationByAssetGUID(String userId, String assetId, Integer limit, Integer offset) {

        ClassificationsResponse response = new ClassificationsResponse();

        try {
            EntitySummary asset = getEntityDetails(userId, assetId);
            if (asset != null) {
                response.setClassifications(converter.toClassifications(asset.getClassifications()));
            }
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | InvalidParameterException
                | EntityProxyOnlyException
                | RepositoryErrorException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public RelationshipsResponse getAssetRelationships(String userId, String assetId,
                                                       String relationshipType, Status status, Integer fromElement,
                                                       Integer pageSize, String property, SequenceOrderType orderType) {

        String relationshipTypeID = getTypeID(userId, relationshipType);
        List<InstanceStatus> instanceStatuses = converter.getInstanceStatuses(status);
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            List<Relationship> relationships = getRelationships(userId,
                    assetId,
                    fromElement,
                    pageSize,
                    property,
                    relationshipTypeID,
                    instanceStatuses,
                    sequencingOrder);

            if (relationships != null) {
                response.setRelationships(converter.toRelationships(relationships));
            }

        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | FunctionNotSupportedException
                | InvalidParameterException
                | RepositoryErrorException
                | TypeErrorException
                | PagingErrorException
                | PropertyErrorException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsByClassificationName(String userId, String assetTypeId,
                                                                  String classificationName, Integer limit, Integer offset,
                                                                  String orderProperty, SequenceOrderType orderType, Status status) {
        List<InstanceStatus> instanceStatuses = converter.getInstanceStatuses(status);
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            List<EntityDetail> entitiesByClassification = metadataCollection.findEntitiesByClassification(userId,
                    assetTypeId,
                    classificationName,
                    null,
                    null,
                    offset,
                    instanceStatuses,
                    null,
                    orderProperty,
                    sequencingOrder,
                    limit);

            if (entitiesByClassification != null) {
                response.setAssetDescriptionList(converter.getAssetsDetails(entitiesByClassification));
            }
        } catch (InvalidParameterException
                | PropertyErrorException
                | FunctionNotSupportedException
                | RepositoryErrorException
                | UserNotAuthorizedException
                | ClassificationErrorException
                | PagingErrorException
                | TypeErrorException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsByProperty(String userId, String assetTypeId,
                                                        String matchProperty, String propertyValue,
                                                        Integer limit, Integer offset,
                                                        SequenceOrderType orderType, String orderProperty,
                                                        Status status) {

        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);
        InstanceProperties matchProperties = null;

        if (matchProperty != null) {
            matchProperties = converter.getMatchProperties(matchProperty, propertyValue);
        }
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            List<EntityDetail> entitiesByProperty = metadataCollection.findEntitiesByProperty(userId,
                    assetTypeId,
                    matchProperties,
                    MatchCriteria.ANY,
                    offset,
                    limitResultsByStatus,
                    null,
                    null,
                    orderProperty,
                    sequencingOrder,
                    limit);
            if (entitiesByProperty != null) {
                response.setAssetDescriptionList(converter.getAssetsDetails(entitiesByProperty));
            }
        } catch (InvalidParameterException
                | RepositoryErrorException
                | TypeErrorException
                | PagingErrorException
                | PropertyErrorException
                | UserNotAuthorizedException
                | FunctionNotSupportedException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetsFromNeighborhood(String userId, String entityGUID, List<String> entityTypesGuid,
                                                              List<String> relationshipTypes, Status relationshipStatus, Integer level) {
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            InstanceGraph entityNeighborhood = getAssetNeighborhood(userId,
                    entityGUID,
                    entityTypesGuid,
                    relationshipTypes,
                    relationshipStatus,
                    level);

            if (entityNeighborhood != null) {
                response.setAssetDescriptionList(converter.getAssetsDetails(entityNeighborhood.getEntities()));
            }
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | FunctionNotSupportedException
                | RepositoryErrorException
                | InvalidParameterException
                | PropertyErrorException
                | TypeErrorException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public RelationshipsResponse getRelationshipsFromNeighborhood(String userId, String entityGUID,
                                                                  List<String> entityTypesGuid, List<String> relationshipTypes,
                                                                  Status relationshipStatus, Integer level) {
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            InstanceGraph entityNeighborhood = getAssetNeighborhood(userId,
                    entityGUID,
                    entityTypesGuid,
                    relationshipTypes,
                    relationshipStatus,
                    level);

            if (entityNeighborhood != null && entityNeighborhood.getRelationships() != null) {
                response.setRelationships(converter.toRelationships(entityNeighborhood.getRelationships()));
            }
        } catch (UserNotAuthorizedException
                | TypeErrorException
                | PropertyErrorException
                | RepositoryErrorException
                | InvalidParameterException
                | FunctionNotSupportedException
                | EntityNotKnownException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getAssetUniverseByGUID(String userId, String assetGUID) {

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        AssetDescription assetDescription = new AssetDescription();
        try {
            EntityDetail entityDetails = getEntityDetails(userId, assetGUID);
            assetDescription = converter.getAssetDescription(entityDetails);
        } catch (UserNotAuthorizedException
                | RepositoryErrorException
                | EntityProxyOnlyException
                | InvalidParameterException
                | EntityNotKnownException e) {
            exceptionUtil.captureException(response, e);
        }

        try {
            List<Relationship> relationships = getRelationships(userId,
                    assetGUID,
                    0,
                    0,
                    null,
                    null,
                    null,
                    null);
            assetDescription.setRelationships(converter.toRelationships(relationships));
        } catch (UserNotAuthorizedException
                | EntityNotKnownException
                | FunctionNotSupportedException
                | InvalidParameterException
                | RepositoryErrorException
                | TypeErrorException
                | PropertyErrorException
                | PagingErrorException e) {
            exceptionUtil.captureException(response, e);
        }

        response.setAssetDescriptionList(Arrays.asList(assetDescription));
        return response;
    }

    public RelationshipsResponse getLinkingRelationships(String userId, String startAssetId, String endAssetId) {
        List<InstanceStatus> limitByStatus = converter.getInstanceStatuses(null);
        RelationshipsResponse response = new RelationshipsResponse();

        try {
            InstanceGraph
                    linkingEntities = metadataCollection.getLinkingEntities(userId, startAssetId, endAssetId, limitByStatus, null);
            if (linkingEntities != null && linkingEntities.getRelationships() != null) {
                response.setRelationships(converter.toRelationships(linkingEntities.getRelationships()));
            }
        } catch (InvalidParameterException
                | RepositoryErrorException
                | PropertyErrorException
                | EntityNotKnownException
                | UserNotAuthorizedException
                | FunctionNotSupportedException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getLinkingAssets(String userId, String startAssetId, String endAssetId) {
        List<InstanceStatus> limitByStatus = converter.getInstanceStatuses(null);
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            InstanceGraph linkingEntities = metadataCollection.getLinkingEntities(userId,
                    startAssetId,
                    endAssetId,
                    limitByStatus,
                    null);
            if (linkingEntities != null && linkingEntities.getEntities() != null) {
                response.setAssetDescriptionList(converter.getAssetsDetails(linkingEntities.getEntities()));
            }
        } catch (InvalidParameterException
                | RepositoryErrorException
                | EntityNotKnownException
                | PropertyErrorException
                | FunctionNotSupportedException
                | UserNotAuthorizedException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }

    public AssetDescriptionResponse getRelatedAssets(String userId, String startAssetId, String instanceType,
                                                     Integer limit, Integer offset,
                                                     SequenceOrderType orderType, String orderProperty, Status status) {
        SequencingOrder sequencingOrder = converter.getSequencingOrder(orderType);
        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(status);
        List<String> instanceTypes = new ArrayList<>();
        instanceTypes.add(instanceType);
        AssetDescriptionResponse response = new AssetDescriptionResponse();

        try {
            List<EntityDetail> relatedEntities = metadataCollection.getRelatedEntities(
                    userId,
                    startAssetId,
                    instanceTypes,
                    offset,
                    limitResultsByStatus,
                    null,
                    null,
                    orderProperty,
                    sequencingOrder,
                    limit);

            if (relatedEntities != null) {
                response.setAssetDescriptionList(converter.getAssetsDetails(relatedEntities));
            }
        } catch (UserNotAuthorizedException
                | FunctionNotSupportedException
                | PropertyErrorException
                | InvalidParameterException
                | EntityNotKnownException
                | TypeErrorException
                | PagingErrorException
                | RepositoryErrorException e) {
            exceptionUtil.captureException(response, e);
        }

        return response;
    }


    public List<AssetDescription> getLastUpdatedAssets(String userId, String entityTypeGUID, Date fromDate, Date toDate, Status status, Integer limit, Integer offset, SequenceOrderType orderType, String orderProperty, Status status1) {
        //TODO: Asset Catalog - Implementation missing
        return null;
    }

    public List<AssetDescription> getLastCreatedAssets(String userId, String assetTypeId, Date fromDate, Date toDate, Status status, Integer limit, Integer offset, SequenceOrderType orderType, String orderProperty, Status status1) {
        //TODO: Asset Catalog - Implementation missing
        return null;
    }

    private EntitySummary getEntitySummary(String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, EntityNotKnownException {
        return metadataCollection.getEntitySummary(userId, assetId);
    }

    private EntityDetail getEntityDetails(String userId, String assetId) throws UserNotAuthorizedException, RepositoryErrorException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        return metadataCollection.getEntityDetail(userId, assetId);
    }

    private String getTypeID(String userId, String relationshipType) {

        if (relationshipType != null) {
            try {
                TypeDef typeDefByName = metadataCollection.getTypeDefByName(userId, relationshipType);
                return typeDefByName.getGUID();
            } catch (InvalidParameterException
                    | RepositoryErrorException
                    | UserNotAuthorizedException
                    | TypeDefNotKnownException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private List<Relationship> getRelationships(String userId, String assetId, Integer fromElement,
                                                Integer pageSize, String property, String relationshipTypeID,
                                                List<InstanceStatus> instanceStatuses, SequencingOrder sequencingOrder)
            throws UserNotAuthorizedException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException,
            PagingErrorException {

        return metadataCollection.getRelationshipsForEntity(
                userId,
                assetId,
                relationshipTypeID,
                fromElement,
                instanceStatuses,
                null,
                property,
                sequencingOrder,
                pageSize);
    }

    private InstanceGraph getAssetNeighborhood(String userId, String entityGUID, List<String> entityTypesGuid,
                                               List<String> relationshipTypes, Status relationshipStatus, Integer level)
            throws UserNotAuthorizedException,
            EntityNotKnownException,
            FunctionNotSupportedException,
            InvalidParameterException,
            RepositoryErrorException,
            PropertyErrorException,
            TypeErrorException {

        List<InstanceStatus> limitResultsByStatus = converter.getInstanceStatuses(relationshipStatus);

        return metadataCollection.getEntityNeighborhood(
                userId,
                entityGUID,
                entityTypesGuid,
                relationshipTypes,
                limitResultsByStatus,
                null,
                null,
                level);
    }

}
