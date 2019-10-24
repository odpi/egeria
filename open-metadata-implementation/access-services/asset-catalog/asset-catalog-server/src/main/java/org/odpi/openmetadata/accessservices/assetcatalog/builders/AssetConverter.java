/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.builders;

import org.odpi.openmetadata.accessservices.assetcatalog.model.Asset;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class AssetConverter {

    private OMRSRepositoryHelper repositoryHelper;

    public AssetConverter(OMRSRepositoryHelper repositoryHelper) {
        this.repositoryHelper = repositoryHelper;
    }

    public List<AssetDescription> getAssetsDetails(List<EntityDetail> entityDetails) {
        return entityDetails.stream()
                .map(this::getAssetDescription)
                .collect(Collectors.toCollection(() -> new ArrayList<>(entityDetails.size())));
    }

    public AssetDescription getAssetDescription(EntityDetail entityDetail) {
        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGuid(entityDetail.getGUID());
        assetDescription.setMetadataCollectionId(entityDetail.getMetadataCollectionId());

        assetDescription.setCreatedBy(entityDetail.getCreatedBy());
        assetDescription.setCreateTime(entityDetail.getCreateTime());
        assetDescription.setUpdatedBy(entityDetail.getUpdatedBy());
        assetDescription.setUpdateTime(entityDetail.getUpdateTime());
        assetDescription.setVersion(entityDetail.getVersion());

        if (entityDetail.getType() != null) {
            assetDescription.setTypeDefName(entityDetail.getType().getTypeDefName());
            assetDescription.setTypeDefDescription(entityDetail.getType().getTypeDefDescription());
        }

        assetDescription.setUrl(entityDetail.getInstanceURL());
        if (entityDetail.getStatus() != null) {
            assetDescription.setStatus(entityDetail.getStatus().getName());
        }

        if (entityDetail.getProperties() != null) {
            assetDescription.setProperties(repositoryHelper.getInstancePropertiesAsMap(entityDetail.getProperties()));
        }
        if (entityDetail.getClassifications() != null) {
            assetDescription.setClassifications(convertClassifications(entityDetail.getClassifications()));
        }

        return assetDescription;
    }

    public List<Relationship> convertRelationships(List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationships) {
        if (relationships == null) {
            return Collections.emptyList();
        }

        return relationships.stream().map(this::convertRelationship).collect(Collectors.toList());
    }

    public Relationship convertRelationship(org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship rel) {
        Relationship relationship = new Relationship();

        relationship.setGuid(rel.getGUID());
        relationship.setCreatedBy(rel.getCreatedBy());
        relationship.setCreateTime(rel.getCreateTime());

        relationship.setUpdatedBy(rel.getUpdatedBy());
        relationship.setUpdateTime(rel.getUpdateTime());

        relationship.setVersion(rel.getVersion());
        if (rel.getStatus() != null) {
            relationship.setStatus(rel.getStatus().getName());
        }

        if (rel.getType() != null) {
            relationship.setTypeDefName(rel.getType().getTypeDefName());
            relationship.setTypeDefDescription(rel.getType().getTypeDefDescription());
        }

        if (rel.getEntityOneProxy() != null) {
            relationship.setFromEntity(getAsset(rel.getEntityOneProxy()));
        }
        if (rel.getEntityTwoProxy() != null) {
            relationship.setToEntity(getAsset(rel.getEntityTwoProxy()));
        }

        return relationship;
    }

    public List<Classification> convertClassifications
            (List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> classificationsFromEntity) {

        if (classificationsFromEntity == null || classificationsFromEntity.isEmpty()) {
            return new ArrayList<>();
        }

        List<Classification> classifications = new ArrayList<>(classificationsFromEntity.size());
        for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification classificationEntity : classificationsFromEntity) {
            Classification classification = new Classification();
            classification.setName(classificationEntity.getName());
            if (classificationEntity.getClassificationOrigin() != null && classificationEntity.getClassificationOrigin().getDescription() != null) {
                classification.setOrigin(classificationEntity.getClassificationOrigin().getDescription());
            }
            classification.setOriginGUID(classificationEntity.getClassificationOriginGUID());

            classification.setCreatedBy(classificationEntity.getCreatedBy());
            classification.setCreateTime(classificationEntity.getCreateTime());

            classification.setUpdatedBy(classificationEntity.getUpdatedBy());
            classification.setUpdateTime(classificationEntity.getUpdateTime());

            classification.setVersion(classificationEntity.getVersion());
            classification.setStatus(classificationEntity.getStatus().getName());
            classification.setTypeDefName(classificationEntity.getType().getTypeDefName());
            classification.setTypeDefDescription(classificationEntity.getType().getTypeDefDescription());
            if (classificationEntity.getProperties() != null) {
                classification.setProperties(repositoryHelper.getInstancePropertiesAsMap(classificationEntity.getProperties()));
            }

            classifications.add(classification);
        }

        return classifications;
    }

    private Asset getAsset(EntityProxy entityProxy) {
        String method = "getAsset";
        Asset asset = new Asset();

        asset.setGuid(entityProxy.getGUID());
        if (entityProxy.getUniqueProperties() != null) {
            asset.setName(repositoryHelper.getStringProperty("userID", Constants.NAME, entityProxy.getUniqueProperties(), method));
        }
        asset.setMetadataCollectionId(entityProxy.getMetadataCollectionId());
        asset.setCreatedBy(entityProxy.getCreatedBy());
        asset.setCreateTime(entityProxy.getCreateTime());
        asset.setUpdatedBy(entityProxy.getUpdatedBy());
        asset.setUpdateTime(entityProxy.getUpdateTime());
        asset.setStatus(entityProxy.getStatus().getName());
        asset.setVersion(entityProxy.getVersion());
        asset.setTypeDefName(entityProxy.getType().getTypeDefName());
        asset.setTypeDefDescription(entityProxy.getType().getTypeDefDescription());

        return asset;
    }
}
