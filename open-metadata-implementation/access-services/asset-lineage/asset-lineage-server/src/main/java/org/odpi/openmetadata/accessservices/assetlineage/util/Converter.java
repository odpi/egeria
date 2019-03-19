/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetlineage.util;

import org.odpi.openmetadata.accessservices.assetlineage.model.Classification;
import org.odpi.openmetadata.accessservices.assetlineage.model.Relationship;
import org.odpi.openmetadata.accessservices.assetlineage.model.*;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.*;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING;

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
        assetDescription.setGuid(entityDetail.getGUID());
        assetDescription.setMetadataCollectionId(entityDetail.getMetadataCollectionId());
        assetDescription.setDisplayName((String) getPropertyValue(entityDetail.getProperties(), Constants.NAME));

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
        if (entityDetail.getClassifications() != null) {
            assetDescription.setClassifications(toClassifications(entityDetail.getClassifications()));
        }

        return assetDescription;
    }

    public AssetDescription getAssetDescription(EntitySummary entitySummary) {
        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGuid(entitySummary.getGUID());
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

        if (entitySummary.getClassifications() != null && !entitySummary.getClassifications().isEmpty()) {
            assetDescription.setClassifications(toClassifications(entitySummary.getClassifications()));
        }

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

        relationship.setGuid(rel.getGUID());
        relationship.setCreatedBy(rel.getCreatedBy());
        relationship.setCreateTime(rel.getCreateTime());

        relationship.setUpdatedBy(rel.getUpdatedBy());
        relationship.setUpdateTime(rel.getUpdateTime());

        relationship.setVersion(rel.getVersion());
        relationship.setStatus(getStatus(rel.getStatus().getName()));

        relationship.setTypeDefName(rel.getType().getTypeDefName());
        relationship.setTypeDefDescription(rel.getType().getTypeDefDescription());

        relationship.setFromEntity(getAsset(rel.getEntityOneProxy()));
        relationship.setToEntity(getAsset(rel.getEntityTwoProxy()));

        return relationship;
    }

    public InstanceProperties getMatchProperties(String matchProperty, String propertyValue) {
        InstanceProperties instanceProperties = new InstanceProperties();
        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
        primitivePropertyValue.setPrimitiveDefCategory(OM_PRIMITIVE_TYPE_STRING);
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
            if (classificationEntity.getClassificationOrigin() != null && classificationEntity.getClassificationOrigin().getDescription() != null) {
                classification.setOrigin(classificationEntity.getClassificationOrigin().getDescription());
            }
            classification.setOriginGUID(classificationEntity.getClassificationOriginGUID());

            classification.setCreatedBy(classificationEntity.getCreatedBy());
            classification.setCreateTime(classificationEntity.getCreateTime());

            classification.setUpdatedBy(classificationEntity.getUpdatedBy());
            classification.setUpdateTime(classificationEntity.getUpdateTime());

            classification.setVersion(classificationEntity.getVersion());
            classification.setStatus(getStatus(classificationEntity.getStatus().getName()));
            classification.setTypeDefName(classificationEntity.getType().getTypeDefName());
            classification.setTypeDefDescription(classificationEntity.getType().getTypeDefDescription());
            if (classificationEntity.getProperties() != null) {
                Map<String, Object> properties = getProperties(classificationEntity.getProperties());
                if (properties != null && !properties.isEmpty()) {
                    classification.setProperties(properties);
                }
            }

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

        asset.setGuid(entityProxy.getGUID());
        if (entityProxy.getUniqueProperties() != null) {
            asset.setName((String) getPropertyValue(entityProxy.getUniqueProperties(), Constants.NAME));
        }
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

        if (instanceProperties.getPropertyValue(propertyName) instanceof PrimitivePropertyValue) {
            PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
            if (value != null) {
                return value.getPrimitiveValue();
            }
        } else if (instanceProperties.getPropertyValue(propertyName) instanceof EnumPropertyValue) {
            EnumPropertyValue value = (EnumPropertyValue) instanceProperties.getPropertyValue(propertyName);
            if (value != null) {
                return value.getDescription();
            }

        }

        return null;
    }

    public String getStringPropertyValue(InstanceProperties instanceProperties, String propertyName) {

        PrimitivePropertyValue value = (PrimitivePropertyValue) instanceProperties.getPropertyValue(propertyName);
        if (value != null && value.getPrimitiveDefCategory().equals(OM_PRIMITIVE_TYPE_STRING)) {
            return (String) value.getPrimitiveValue();
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

    public DataType getColumnTypeValue(EntityDetail columnType) {
        PrimitivePropertyValue value = (PrimitivePropertyValue) columnType.getProperties().getPropertyValue(Constants.TYPE);

        if (value != null) {
            PrimitiveDefCategory primitiveValue = value.getPrimitiveDefCategory();
            return getDataTypeDef(primitiveValue);
        }

        return null;
    }

    private DataType getDataTypeDef(PrimitiveDefCategory primitiveValue) {
        if (primitiveValue == null || !dataTypes.containsKey(primitiveValue.getJavaClassName())) {
            return null;
        }

        return dataTypes.get(primitiveValue.getJavaClassName());
    }

    private HashMap<String, DataType> getDataTypeMap() {
        HashMap<String, DataType> dataTypesMap = new HashMap<>();
        dataTypesMap.put("java.lang.Boolean", DataType.BOOLEAN);
        dataTypesMap.put("java.lang.Byte", DataType.BYTE);
        dataTypesMap.put("java.Lang.Char", DataType.CHAR);
        dataTypesMap.put("java.lang.Short", DataType.SHORT);
        dataTypesMap.put("java.lang.Integer", DataType.INT);
        dataTypesMap.put("java.lang.Long", DataType.LONG);
        dataTypesMap.put("java.lang.Float", DataType.FLOAT);
        dataTypesMap.put("java.lang.Double", DataType.DOUBLE);
        dataTypesMap.put("java.math.BigInteger", DataType.BIG_INTEGER);
        dataTypesMap.put("java.math.BigDecimal", DataType.BIG_DECIMAL);
        dataTypesMap.put("java.lang.String", DataType.STRING);
        dataTypesMap.put("java.util.Date", DataType.DATE);
        return dataTypesMap;
    }

    public Map<String, Object> getAdditionalPropertiesFromEntity(InstanceProperties properties, String propertyName, OMRSRepositoryHelper helper) {
        InstanceProperties mapProperty = getMapInstanceProperties(propertyName, properties, helper);
        if (mapProperty == null) {
            return null;
        }

        Iterator<String> additionalPropertyNames = mapProperty.getPropertyNames();
        if (additionalPropertyNames == null) {
            return null;
        }

        Map<String, Object> additionalPropertiesMap = new HashMap<>();

        while (additionalPropertyNames.hasNext()) {
            String additionalPropertyName = additionalPropertyNames.next();
            InstancePropertyValue additionalPropertyValue = mapProperty.getPropertyValue(additionalPropertyName);

            if (additionalPropertyValue != null) {
                final Object primitivePropertyValue = getPrimitivePropertyValue(additionalPropertyValue);
                additionalPropertiesMap.put(additionalPropertyName, primitivePropertyValue);
            }
        }

        return additionalPropertiesMap;
    }

    private InstanceProperties getMapInstanceProperties(String propertyName, InstanceProperties properties, OMRSRepositoryHelper helper) {
        return helper.getMapProperty(AccessServiceDescription.ASSET_CATALOG_OMAS.getAccessServiceName(),
                propertyName,
                properties,
                "getMapInstanceProperties");
    }

    private Object getPrimitivePropertyValue(InstancePropertyValue additionalPropertyValue) {
        if (additionalPropertyValue != null &&
                additionalPropertyValue.getInstancePropertyCategory() == InstancePropertyCategory.PRIMITIVE) {
            PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) additionalPropertyValue;
            return primitivePropertyValue.getPrimitiveValue();
        }

        return null;
    }

    public Map<String, String> getMapProperties(InstanceProperties properties) {
        Map<String, String> attributes = new HashMap<>();

        if (properties != null) {

            Map<String, InstancePropertyValue> instanceProperties = properties.getInstanceProperties();
            if (instanceProperties != null) {
                for (Map.Entry<String, InstancePropertyValue> property : instanceProperties.entrySet()) {
                    if (property.getValue() != null) {
                        String propertyValue = getStringForPropertyValue(property.getValue());
                        if (!propertyValue.equals("")) {
                            attributes.put(property.getKey(), propertyValue);
                        }
                    }
                }
            }
        }

        return attributes;
    }

    private String getStringForPropertyValue(InstancePropertyValue ipv) {

        if (ipv instanceof PrimitivePropertyValue) {
            PrimitiveDefCategory primtype =
                    ((PrimitivePropertyValue) ipv).getPrimitiveDefCategory();
            switch (primtype) {
                case OM_PRIMITIVE_TYPE_STRING:
                    return (String) ((PrimitivePropertyValue) ipv).getPrimitiveValue();
                case OM_PRIMITIVE_TYPE_INT:
                case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                case OM_PRIMITIVE_TYPE_BIGINTEGER:
                case OM_PRIMITIVE_TYPE_BOOLEAN:
                case OM_PRIMITIVE_TYPE_BYTE:
                case OM_PRIMITIVE_TYPE_CHAR:
                case OM_PRIMITIVE_TYPE_DATE:
                case OM_PRIMITIVE_TYPE_DOUBLE:
                case OM_PRIMITIVE_TYPE_FLOAT:
                case OM_PRIMITIVE_TYPE_LONG:
                case OM_PRIMITIVE_TYPE_SHORT:
                    return ((PrimitivePropertyValue) ipv).getPrimitiveValue().toString();
                case OM_PRIMITIVE_TYPE_UNKNOWN:
                default:
                    return "";
            }
        } else {
            if (ipv instanceof EnumPropertyValue) {
                return ((EnumPropertyValue) ipv).getSymbolicName();
            } else {
                return "";
            }
        }
    }
}