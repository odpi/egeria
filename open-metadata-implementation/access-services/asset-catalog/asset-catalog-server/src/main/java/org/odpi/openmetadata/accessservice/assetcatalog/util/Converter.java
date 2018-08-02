/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog.util;

import org.odpi.openmetadata.accessservice.assetcatalog.model.Asset;
import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservice.assetcatalog.model.DataType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservice.assetcatalog.model.SequenceOrderType;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Status;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntitySummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservice.assetcatalog.util.Constants.QUALIFIED_NAME;

@Service
public class Converter {

    private HashMap<String, DataType> dataTypes;

    public Converter() {
        dataTypes = getDataTypeMap();
    }

    public List<AssetDescription> getAssetsDetails(List<EntityDetail> entityDetails) {
        return entityDetails.stream()
                .map(this::getAssetDescription)
                .collect(Collectors.toCollection(() -> new ArrayList<>(entityDetails.size())));
    }

    public AssetDescription getAssetDescription(EntityDetail entityDetail) {
        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGUID(entityDetail.getGUID());
        assetDescription.setMetadataCollectionId(entityDetail.getMetadataCollectionId());
        assetDescription.setDisplayName((String) getPropertyValue(entityDetail.getProperties(), QUALIFIED_NAME));

        assetDescription.setCreatedBy(entityDetail.getCreatedBy());
        assetDescription.setCreateTime(entityDetail.getCreateTime());
        assetDescription.setUpdatedBy(entityDetail.getUpdatedBy());
        assetDescription.setUpdateTime(entityDetail.getUpdateTime());
        assetDescription.setVersion(entityDetail.getVersion());

        assetDescription.setTypeDefName(entityDetail.getType().getTypeDefName());
        assetDescription.setTypeDefDescription(entityDetail.getType().getTypeDefDescription());
        assetDescription.setUrl(entityDetail.getInstanceURL());
        assetDescription.setStatus(getStatus(entityDetail.getStatus().getName()));

        assetDescription.setProperties(getProperties(entityDetail.getProperties()));
        assetDescription.setClassifications(toClassifications(entityDetail.getClassifications()));

        return assetDescription;
    }

    public AssetDescription getAssetDescription(EntitySummary entitySummary) {
        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGUID(entitySummary.getGUID());
        assetDescription.setMetadataCollectionId(entitySummary.getMetadataCollectionId());

        assetDescription.setCreatedBy(entitySummary.getCreatedBy());
        assetDescription.setCreateTime(entitySummary.getCreateTime());
        assetDescription.setUpdatedBy(entitySummary.getUpdatedBy());
        assetDescription.setUpdateTime(entitySummary.getUpdateTime());
        assetDescription.setVersion(entitySummary.getVersion());

        assetDescription.setTypeDefName(entitySummary.getType().getTypeDefName());
        assetDescription.setTypeDefDescription(entitySummary.getType().getTypeDefDescription());
        assetDescription.setUrl(entitySummary.getInstanceURL());
        assetDescription.setStatus(getStatus(entitySummary.getStatus().getName()));

        assetDescription.setClassifications(toClassifications(entitySummary.getClassifications()));
        return assetDescription;
    }

    public List<Relationship> toRelationships(List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship> relationshipsEntity) {
        if (relationshipsEntity == null) {
            return new ArrayList<>();
        }

        return relationshipsEntity.stream()
                .map(this::toRelationship)
                .collect(Collectors.toCollection(() -> new ArrayList<>(relationshipsEntity.size())));
    }

    public Relationship toRelationship(
            org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship rel) {
        Relationship relationship = new Relationship();

        relationship.setGUID(rel.getGUID());
        relationship.setCreatedBy(rel.getCreatedBy());
        relationship.setCreateTime(rel.getCreateTime());

        relationship.setUpdatedBy(rel.getUpdatedBy());
        relationship.setUpdateTime(rel.getUpdateTime());

        relationship.setVersion(rel.getVersion());
        relationship.setStatus(getStatus(rel.getStatus().getName()));

        relationship.setTypeDefName(rel.getType().getTypeDefName());
        relationship.setTypeDefDescription(rel.getType().getTypeDefDescription());

        relationship.setFromEntityPropertyName(rel.getEntityOnePropertyName());
        relationship.setToEntityPropertyName(rel.getEntityTwoPropertyName());

        relationship.setFromEntity(getAsset(rel.getEntityOneProxy()));
        relationship.setToEntity(getAsset(rel.getEntityTwoProxy()));

        return relationship;
    }

    public InstanceProperties getMatchProperties(String matchProperty, String propertyValue) {
        InstanceProperties instanceProperties = new InstanceProperties();
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(propertyValue);
        instanceProperties.setProperty(matchProperty, primitivePropertyValue);
        return instanceProperties;
    }

    public List<InstanceStatus> getInstanceStatuses(Status status) {
        if (status == null) {
            return getActiveInstanceStatusList();
        }

        return getInstanceStatusList(status);
    }

    public SequencingOrder getSequencingOrder(SequenceOrderType orderType) {
        if (orderType == null) {
            return SequencingOrder.ANY;
        }

        return getOrderType(orderType);
    }

    public List<Classification> toClassifications
            (List<org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification> classificationsFromEntity) {

        if (classificationsFromEntity == null || classificationsFromEntity.isEmpty()) {
            return new ArrayList<>();
        }

        List<Classification> classifications = new ArrayList<>(classificationsFromEntity.size());
        for (org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification classificationEntity : classificationsFromEntity) {
            Classification classification = new Classification();
            classification.setName(classificationEntity.getName());
            classification.setOrigin(classificationEntity.getClassificationOrigin().getDescription());
            classification.setOriginGUID(classificationEntity.getClassificationOriginGUID());

            classification.setCreatedBy(classificationEntity.getCreatedBy());
            classification.setCreateTime(classificationEntity.getCreateTime());

            classification.setUpdatedBy(classificationEntity.getUpdatedBy());
            classification.setUpdateTime(classificationEntity.getUpdateTime());

            classification.setVersion(classificationEntity.getVersion());
            classification.setStatus(getStatus(classificationEntity.getStatus().getName()));
            classification.setTypeDefName(classificationEntity.getType().getTypeDefName());
            classification.setTypeDefDescription(classificationEntity.getType().getTypeDefDescription());
            classification.setProperties(getProperties(classificationEntity.getProperties()));

            classifications.add(classification);
        }

        return classifications;
    }

    private Status getStatus(String statusName) {

        switch (statusName) {
            case "<Unknown>":
                return Status.UNKNOWN;
            case "Proposed":
                return Status.PROPOSED;
            case "Draft":
                return Status.DRAFT;
            case "Prepared":
                return Status.PREPARED;
            case "Active":
                return Status.ACTIVE;
            case "Deleted":
                return Status.DELETED;
            default:
                return Status.UNKNOWN;
        }
    }

    private Asset getAsset(EntityProxy entityProxy) {
        Asset asset = new Asset();

        asset.setGUID(entityProxy.getGUID());
        asset.setMetadataCollectionId(entityProxy.getMetadataCollectionId());
        asset.setCreatedBy(entityProxy.getCreatedBy());
        asset.setCreateTime(entityProxy.getCreateTime());
        asset.setUpdatedBy(entityProxy.getUpdatedBy());
        asset.setUpdateTime(entityProxy.getUpdateTime());
        asset.setStatus(getStatus(entityProxy.getStatus().getName()));
        asset.setVersion(entityProxy.getVersion());
        asset.setTypeDefName(entityProxy.getType().getTypeDefName());
        asset.setTypeDefDescription(entityProxy.getType().getTypeDefDescription());

        return asset;
    }

    private List<InstanceStatus> getActiveInstanceStatusList() {
        List<InstanceStatus> instanceStatuses = new ArrayList<>(1);
        instanceStatuses.add(InstanceStatus.ACTIVE);
        return instanceStatuses;
    }

    private List<InstanceStatus> getInstanceStatusList(Status statusName) {
        List<InstanceStatus> instanceStatuses = new ArrayList<>(1);

        InstanceStatus status = getInstanceStatus(statusName);
        instanceStatuses.add(status);

        return instanceStatuses;
    }

    private InstanceStatus getInstanceStatus(Status statusName) {
        switch (statusName) {
            case ACTIVE:
                return InstanceStatus.ACTIVE;
            case PROPOSED:
                return InstanceStatus.PROPOSED;
            case PREPARED:
                return InstanceStatus.PREPARED;
            case DELETED:
                return InstanceStatus.DELETED;
            case DRAFT:
                return InstanceStatus.DRAFT;
            default:
                return InstanceStatus.UNKNOWN;
        }
    }

    private Map<String, Object> getProperties(InstanceProperties instanceProperties) {
        Map<String, Object> properties = new HashMap<>();

        Iterator<String> propertyNames = instanceProperties.getPropertyNames();
        while ((propertyNames.hasNext())) {
            String propertyName = propertyNames.next();
            Object propertyValue = getPropertyValue(instanceProperties, propertyName);
            properties.put(propertyName, propertyValue);
        }

        return properties;
    }

    private Object getPropertyValue(InstanceProperties instanceProperties, String propertyName) {

        PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
        if (value != null) {
            return value.getPrimitiveValue();
        }

        return null;
    }

    private SequencingOrder getOrderType(SequenceOrderType orderType) {
        switch (orderType) {
            case GUID:
                return SequencingOrder.GUID;
            case LAST_UPDATE_OLDEST:
                return SequencingOrder.LAST_UPDATE_OLDEST;
            case LAST_UPDATE_RECENT:
                return SequencingOrder.LAST_UPDATE_RECENT;
            case PROPERTY_ASCENDING:
                return SequencingOrder.PROPERTY_ASCENDING;
            case PROPERTY_DESCENDING:
                return SequencingOrder.PROPERTY_DESCENDING;
            case CREATION_DATE_OLDEST:
                return SequencingOrder.CREATION_DATE_OLDEST;
            case CREATION_DATE_RECENT:
                return SequencingOrder.CREATION_DATE_RECENT;
            default:
                return SequencingOrder.ANY;
        }
    }

    public DataType getDataTypeDef(PrimitiveDefCategory primitiveValue) {
        if (primitiveValue == null || !dataTypes.containsKey(primitiveValue.getJavaClassName())) {
            return null;
        }

        return dataTypes.get(primitiveValue.getJavaClassName());
    }

    private HashMap<String, DataType> getDataTypeMap() {
        HashMap<String, DataType> dataTypes = new HashMap<>();
        dataTypes.put("java.lang.Boolean", DataType.BOOLEAN);
        dataTypes.put("java.lang.Byte", DataType.BYTE);
        dataTypes.put("java.Lang.Char", DataType.CHAR);
        dataTypes.put("java.lang.Short", DataType.SHORT);
        dataTypes.put("java.lang.Integer", DataType.INT);
        dataTypes.put("java.lang.Long", DataType.LONG);
        dataTypes.put("java.lang.Float", DataType.FLOAT);
        dataTypes.put("java.lang.Double", DataType.DOUBLE);
        dataTypes.put("java.math.BigInteger", DataType.BIG_INTEGER);
        dataTypes.put("java.math.BigDecimal", DataType.BIG_DECIMAL);
        dataTypes.put("java.lang.String", DataType.STRING);
        dataTypes.put("java.util.Date", DataType.DATE);
        return dataTypes;
    }
}
