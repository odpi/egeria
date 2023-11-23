/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefLink;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * Maps the properties of InstanceAuditHeaders between persistence and objects.
 */
public abstract class InstanceAuditHeaderMapping extends AbstractMapping {

    private static final String INSTANCE_AUDIT_HEADER = "InstanceAuditHeader";

    private static final String N_HEADER_VERSION = "headerVersion";
    private static final String N_TYPE = "type";
    private static final String N_INSTANCE_PROVENANCE_TYPE = "instanceProvenanceType";
    private static final String N_METADATA_COLLECTION_ID = "metadataCollectionId";
    private static final String N_METADATA_COLLECTION_NAME = "metadataCollectionName";
    private static final String N_REPLICATED_BY = "replicatedBy";
    private static final String N_INSTANCE_LICENSE = "instanceLicense";
    private static final String N_CREATED_BY = "createdBy";
    private static final String N_UPDATED_BY = "updatedBy";
    private static final String N_MAINTAINED_BY = "maintainedBy";
    private static final String N_CREATE_TIME = "createTime";
    private static final String N_UPDATE_TIME = "updateTime";
    private static final String N_VERSION = "version";
    private static final String N_CURRENT_STATUS = "currentStatus";
    private static final String N_STATUS_ON_DELETE = "statusOnDelete";
    private static final String N_MAPPING_PROPERTIES = "mappingProperties";

    // Note that these have no namespace, and therefore not useful on classifications (only entity, relationship)
    public static final String METADATA_COLLECTION_ID = getKeyword(N_METADATA_COLLECTION_ID);
    public static final String METADATA_COLLECTION_NAME = getKeyword(N_METADATA_COLLECTION_NAME);
    public static final String CREATE_TIME = getKeyword(N_CREATE_TIME);
    public static final String UPDATE_TIME = getKeyword(N_UPDATE_TIME);
    public static final String CURRENT_STATUS = getKeyword(N_CURRENT_STATUS);
    public static final String STATUS_ON_DELETE = getKeyword(N_STATUS_ON_DELETE);
    public static final String TYPE_DEF_GUIDS = getKeyword(N_TYPE + ".guids");
    public static final String TYPE_DEF_CATEGORY = getKeyword(N_TYPE + ".category");
    public static final String VERSION = getKeyword(N_VERSION);
    public static final String MAINTAINED_BY = getKeyword(N_MAINTAINED_BY);
    public static final String UPDATED_BY = getKeyword(N_UPDATED_BY);
    public static final String REPLICATED_BY = getKeyword(N_REPLICATED_BY);
    public static final String INSTANCE_PROVENANCE_TYPE = getKeyword(N_INSTANCE_PROVENANCE_TYPE);

    private static final Set<String> KNOWN_PROPERTIES = createKnownProperties();
    private static Set<String> createKnownProperties() {
        Set<String> set = new HashSet<>();
        set.add(N_HEADER_VERSION);
        set.add(N_TYPE);
        set.add(N_INSTANCE_PROVENANCE_TYPE);
        set.add(N_METADATA_COLLECTION_ID);
        set.add(N_METADATA_COLLECTION_NAME);
        set.add(N_REPLICATED_BY);
        set.add(N_INSTANCE_LICENSE);
        set.add(N_CREATED_BY);
        set.add(N_UPDATED_BY);
        set.add(N_MAINTAINED_BY);
        set.add(N_CREATE_TIME);
        set.add(N_UPDATE_TIME);
        set.add(N_VERSION);
        set.add(N_CURRENT_STATUS);
        set.add(N_STATUS_ON_DELETE);
        set.add(N_MAPPING_PROPERTIES);
        return set;
    }

    /**
     * Default constructor.
     * @param xtdbConnector connectivity to XTDB
     */
    protected InstanceAuditHeaderMapping(XTDBOMRSRepositoryConnector xtdbConnector) {
        super(xtdbConnector);
    }

    /**
     * Check whether the specified property is a known base-level Instance property.
     * @param property to check
     * @return boolean
     */
    public static boolean isKnownBaseProperty(String property) {
        return KNOWN_PROPERTIES.contains(property);
    }

    /**
     * Translate the provided Egeria representation into a XTDB document.
     * @param builder for the XTDB document
     * @param iah Egeria representation from which to map
     * @throws IOException on any error serializing the provided values
     */
    protected static void buildDoc(XtdbDocument.Builder builder, InstanceAuditHeader iah) throws IOException {
        buildDoc(builder, iah, null);
    }

    /**
     * Translate the provided Egeria representation into a XTDB document.
     * @param builder for the XTDB document
     * @param iah Egeria representation from which to map
     * @param namespace by which to qualify the properties
     * @return the latest change date in the header (updateTime or if empty createTime)
     * @throws IOException on any error serializing the provided values
     */
    public static Date buildDoc(XtdbDocument.Builder builder,
                                InstanceAuditHeader iah,
                                String namespace) throws IOException {

        Date updateTime = iah.getUpdateTime();
        Date createTime = iah.getCreateTime();

        List<String> maintainers = iah.getMaintainedBy();
        builder.put(getKeyword(namespace, N_HEADER_VERSION), iah.getHeaderVersion());
        builder.put(getKeyword(namespace, N_METADATA_COLLECTION_ID), iah.getMetadataCollectionId());
        builder.put(getKeyword(namespace, N_METADATA_COLLECTION_NAME), iah.getMetadataCollectionName());
        builder.put(getKeyword(namespace, N_REPLICATED_BY), iah.getReplicatedBy());
        builder.put(getKeyword(namespace, N_INSTANCE_LICENSE), iah.getInstanceLicense());
        builder.put(getKeyword(namespace, N_CREATED_BY), iah.getCreatedBy());
        builder.put(getKeyword(namespace, N_UPDATED_BY), iah.getUpdatedBy());
        builder.put(getKeyword(namespace, N_MAINTAINED_BY), maintainers == null ? null : PersistentVector.create(maintainers));
        builder.put(getKeyword(namespace, N_CREATE_TIME), createTime);
        builder.put(getKeyword(namespace, N_UPDATE_TIME), updateTime);
        builder.put(getKeyword(namespace, N_VERSION), iah.getVersion());

        // Note that for the type, we will break things out a bit to optimise search:
        // - a list of all type GUIDs for this type: its actual type and all of its supertypes (under 'type.guids')
        // Then we'll also serialize the full InstanceType information into the N_TYPE property itself.
        List<String> typeList = new ArrayList<>();
        InstanceType type = iah.getType();
        typeList.add(type.getTypeDefGUID());
        List<TypeDefLink> superTypes = TypeDefCache.getAllSuperTypes(type.getTypeDefGUID());
        for (TypeDefLink superType : superTypes) {
            typeList.add(superType.getGUID());
        }
        builder.put(getKeyword(namespace, N_TYPE + ".guids"), PersistentVector.create(typeList));
        builder.put(getKeyword(namespace, N_TYPE + ".category"), type.getTypeDefCategory().getOrdinal());
        builder.put(getKeyword(namespace, N_TYPE), getEmbeddedSerializedForm(type));
        builder.put(getKeyword(namespace, N_INSTANCE_PROVENANCE_TYPE), EnumPropertyValueMapping.getOrdinalForInstanceProvenanceType(iah.getInstanceProvenanceType()));
        builder.put(getKeyword(namespace, N_CURRENT_STATUS), EnumPropertyValueMapping.getOrdinalForInstanceStatus(iah.getStatus()));
        builder.put(getKeyword(namespace, N_STATUS_ON_DELETE), EnumPropertyValueMapping.getOrdinalForInstanceStatus(iah.getStatusOnDelete()));
        builder.put(getKeyword(namespace, N_MAPPING_PROPERTIES), getEmbeddedSerializedForm(iah.getMappingProperties()));

        return updateTime == null ? createTime : updateTime;

    }

    /**
     * Translate the provided Egeria representation into a XTDB document map.
     * @param doc for the XTDB document
     * @param iah Egeria representation from which to map
     * @param namespace by which to qualify the properties
     * @return a tuple containing the timestamp of the create / update followed by the updated document map itself
     * @throws IOException on any error serializing the provided values
     */
    public static IPersistentVector addToMap(IPersistentMap doc,
                                             InstanceAuditHeader iah,
                                             String namespace) throws IOException {

        Date updateTime = iah.getUpdateTime();
        Date createTime = iah.getCreateTime();

        List<String> maintainers = iah.getMaintainedBy();
        doc = addTypeDetailsToMap(doc, iah.getType(), namespace);
        doc = doc
                .assoc(Keyword.intern(getKeyword(namespace, N_HEADER_VERSION)), iah.getHeaderVersion())
                .assoc(Keyword.intern(getKeyword(namespace, N_METADATA_COLLECTION_ID)), iah.getMetadataCollectionId())
                .assoc(Keyword.intern(getKeyword(namespace, N_METADATA_COLLECTION_NAME)), iah.getMetadataCollectionName())
                .assoc(Keyword.intern(getKeyword(namespace, N_REPLICATED_BY)), iah.getReplicatedBy())
                .assoc(Keyword.intern(getKeyword(namespace, N_INSTANCE_LICENSE)), iah.getInstanceLicense())
                .assoc(Keyword.intern(getKeyword(namespace, N_CREATED_BY)), iah.getCreatedBy())
                .assoc(Keyword.intern(getKeyword(namespace, N_UPDATED_BY)), iah.getUpdatedBy())
                .assoc(Keyword.intern(getKeyword(namespace, N_MAINTAINED_BY)), maintainers == null ? null : PersistentVector.create(maintainers))
                .assoc(Keyword.intern(getKeyword(namespace, N_CREATE_TIME)), createTime)
                .assoc(Keyword.intern(getKeyword(namespace, N_UPDATE_TIME)), updateTime)
                .assoc(Keyword.intern(getKeyword(namespace, N_VERSION)), iah.getVersion())
                .assoc(Keyword.intern(getKeyword(namespace, N_INSTANCE_PROVENANCE_TYPE)), EnumPropertyValueMapping.getOrdinalForInstanceProvenanceType(iah.getInstanceProvenanceType()))
                .assoc(Keyword.intern(getKeyword(namespace, N_CURRENT_STATUS)), EnumPropertyValueMapping.getOrdinalForInstanceStatus(iah.getStatus()))
                .assoc(Keyword.intern(getKeyword(namespace, N_STATUS_ON_DELETE)), EnumPropertyValueMapping.getOrdinalForInstanceStatus(iah.getStatusOnDelete()))
                .assoc(Keyword.intern(getKeyword(namespace, N_MAPPING_PROPERTIES)), getEmbeddedSerializedForm(iah.getMappingProperties()));

        return Tuple.create(updateTime == null ? createTime : updateTime, doc);

    }

    /**
     * Translate the provided Egeria type information into a XTDB document map.
     * @param doc for the XTDB document
     * @param type to update into the document
     * @param namespace by which to qualify the properties
     * @return IPersistentMap containing the updated XTDB document
     * @throws IOException on any error serializing the provided values
     */
    public static IPersistentMap addTypeDetailsToMap(IPersistentMap doc,
                                                     InstanceType type,
                                                     String namespace) throws IOException {

        // Note that for the type, we will break things out a bit to optimise search:
        // - a list of all type GUIDs for this type: its actual type and all of its supertypes (under 'type.guids')
        // Then we'll also serialize the full InstanceType information into the N_TYPE property itself.
        List<String> typeList = new ArrayList<>();
        typeList.add(type.getTypeDefGUID());
        List<TypeDefLink> superTypes = TypeDefCache.getAllSuperTypes(type.getTypeDefGUID());
        for (TypeDefLink superType : superTypes) {
            typeList.add(superType.getGUID());
        }

        return doc
                .assoc(Keyword.intern(getKeyword(namespace, N_TYPE) + ".guids"), PersistentVector.create(typeList))
                .assoc(Keyword.intern(getKeyword(namespace, N_TYPE) + ".category"), type.getTypeDefCategory().getOrdinal())
                .assoc(Keyword.intern(getKeyword(namespace, N_TYPE)), getEmbeddedSerializedForm(type));

    }

    /**
     * Retrieve the instance type details from the provided XTDB document map.
     * @param doc for the XTDB document
     * @param namespace by which the properties are qualified
     * @return InstanceType
     * @throws IOException on any error deserializing the value
     */
    public static InstanceType getTypeFromInstance(IPersistentMap doc,
                                                   String namespace) throws IOException {
        IPersistentMap typeValue = (IPersistentMap) doc.valAt(Keyword.intern(getKeyword(namespace, N_TYPE)));
        return getDeserializedValue(typeValue, mapper.getTypeFactory().constructType(InstanceType.class));
    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param iah into which to map
     * @param doc from which to map
     */
    protected void fromDoc(InstanceAuditHeader iah, XtdbDocument doc) {
        fromDoc(iah, doc, null);
    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param iah into which to map
     * @param doc from which to map
     * @param namespace by which the properties are qualified
     */
    protected void fromDoc(InstanceAuditHeader iah, XtdbDocument doc, String namespace) {

        final String methodName = "fromDoc";
        for (String propertyName : KNOWN_PROPERTIES) {
            String property = getKeyword(namespace, propertyName);
            Object objValue = doc.get(property);
            String value = objValue == null ? null : objValue.toString();
            switch (propertyName) {
                case N_HEADER_VERSION:
                    iah.setHeaderVersion(objValue == null ? 0 : (Long) objValue);
                    break;
                case N_TYPE:
                    iah.setType(getDeserializedValue(xtdbConnector, INSTANCE_AUDIT_HEADER, N_TYPE, (IPersistentMap)objValue, mapper.getTypeFactory().constructType(InstanceType.class)));
                    break;
                case N_INSTANCE_PROVENANCE_TYPE:
                    iah.setInstanceProvenanceType(EnumPropertyValueMapping.getInstanceProvenanceTypeFromOrdinal(xtdbConnector, (Integer) objValue));
                    break;
                case N_METADATA_COLLECTION_ID:
                    iah.setMetadataCollectionId(value);
                    break;
                case N_METADATA_COLLECTION_NAME:
                    iah.setMetadataCollectionName(value);
                    break;
                case N_REPLICATED_BY:
                    iah.setReplicatedBy(value);
                    break;
                case N_INSTANCE_LICENSE:
                    iah.setInstanceLicense(value);
                    break;
                case N_CREATED_BY:
                    iah.setCreatedBy(value);
                    break;
                case N_UPDATED_BY:
                    iah.setUpdatedBy(value);
                    break;
                case N_MAINTAINED_BY:
                    if (objValue != null) {
                        IPersistentVector maintainers = (IPersistentVector) objValue;
                        List<String> maintainerList = new ArrayList<>();
                        ISeq maintainerSeq = maintainers.seq();
                        while (maintainerSeq != null) {
                            Object maintainer = maintainerSeq.first();
                            if (maintainer != null) {
                                maintainerList.add(maintainer.toString());
                            }
                            maintainerSeq = maintainerSeq.next();
                        }
                        iah.setMaintainedBy(maintainerList);
                    } else {
                        iah.setMaintainedBy(null);
                    }
                    break;
                case N_CREATE_TIME:
                    iah.setCreateTime(objValue == null ? null : (Date) objValue);
                    break;
                case N_UPDATE_TIME:
                    iah.setUpdateTime(objValue == null ? null : (Date) objValue);
                    break;
                case N_VERSION:
                    iah.setVersion(objValue == null ? -1 : (Long) objValue);
                    break;
                case N_CURRENT_STATUS:
                    iah.setStatus(EnumPropertyValueMapping.getInstanceStatusFromOrdinal(xtdbConnector, (Integer) objValue));
                    break;
                case N_STATUS_ON_DELETE:
                    iah.setStatusOnDelete(EnumPropertyValueMapping.getInstanceStatusFromOrdinal(xtdbConnector, (Integer) objValue));
                    break;
                case N_MAPPING_PROPERTIES:
                    iah.setMappingProperties(getDeserializedValue(xtdbConnector, INSTANCE_AUDIT_HEADER, N_MAPPING_PROPERTIES, (IPersistentMap)objValue, mapper.getTypeFactory().constructMapType(Map.class, String.class, Serializable.class)));
                    break;
                default:
                    xtdbConnector.logProblem(this.getClass().getName(),
                                             methodName,
                                             XTDBAuditCode.UNMAPPED_PROPERTY,
                                             null,
                                             property,
                                             INSTANCE_AUDIT_HEADER);
                    break;
            }

        }

    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param iah into which to map
     * @param doc from which to map
     * @param namespace by which the properties are qualified
     * @throws IOException on any issue deserializing values
     * @throws InvalidParameterException for any unmapped properties
     */
    protected static void fromMap(InstanceAuditHeader iah,
                                  IPersistentMap doc,
                                  String namespace) throws IOException, InvalidParameterException {

        final String methodName = "fromMap";
        for (String propertyName : KNOWN_PROPERTIES) {
            Keyword property = Keyword.intern(getKeyword(namespace, propertyName));
            Object objValue = doc.valAt(property);
            String value = objValue == null ? null : objValue.toString();
            switch (propertyName) {
                case N_HEADER_VERSION:
                    iah.setHeaderVersion(objValue == null ? 0 : (Long) objValue);
                    break;
                case N_TYPE:
                    iah.setType(getDeserializedValue((IPersistentMap)objValue, mapper.getTypeFactory().constructType(InstanceType.class)));
                    break;
                case N_INSTANCE_PROVENANCE_TYPE:
                    iah.setInstanceProvenanceType(EnumPropertyValueMapping.getInstanceProvenanceTypeFromOrdinal((Integer) objValue));
                    break;
                case N_METADATA_COLLECTION_ID:
                    iah.setMetadataCollectionId(value);
                    break;
                case N_METADATA_COLLECTION_NAME:
                    iah.setMetadataCollectionName(value);
                    break;
                case N_REPLICATED_BY:
                    iah.setReplicatedBy(value);
                    break;
                case N_INSTANCE_LICENSE:
                    iah.setInstanceLicense(value);
                    break;
                case N_CREATED_BY:
                    iah.setCreatedBy(value);
                    break;
                case N_UPDATED_BY:
                    iah.setUpdatedBy(value);
                    break;
                case N_MAINTAINED_BY:
                    if (objValue != null) {
                        IPersistentVector maintainers = (IPersistentVector) objValue;
                        List<String> maintainerList = new ArrayList<>();
                        ISeq maintainerSeq = maintainers.seq();
                        while (maintainerSeq != null) {
                            Object maintainer = maintainerSeq.first();
                            if (maintainer != null) {
                                maintainerList.add(maintainer.toString());
                            }
                            maintainerSeq = maintainerSeq.next();
                        }
                        iah.setMaintainedBy(maintainerList);
                    } else {
                        iah.setMaintainedBy(null);
                    }
                    break;
                case N_CREATE_TIME:
                    iah.setCreateTime(objValue == null ? null : (Date) objValue);
                    break;
                case N_UPDATE_TIME:
                    iah.setUpdateTime(objValue == null ? null : (Date) objValue);
                    break;
                case N_VERSION:
                    iah.setVersion(objValue == null ? -1 : (Long) objValue);
                    break;
                case N_CURRENT_STATUS:
                    iah.setStatus(EnumPropertyValueMapping.getInstanceStatusFromOrdinal((Integer) objValue));
                    break;
                case N_STATUS_ON_DELETE:
                    iah.setStatusOnDelete(EnumPropertyValueMapping.getInstanceStatusFromOrdinal((Integer) objValue));
                    break;
                case N_MAPPING_PROPERTIES:
                    iah.setMappingProperties(getDeserializedValue((IPersistentMap)objValue, mapper.getTypeFactory().constructMapType(Map.class, String.class, Serializable.class)));
                    break;
                default:
                    throw new InvalidParameterException(XTDBErrorCode.UNMAPPABLE_PROPERTY.getMessageDefinition(
                            propertyName), InstanceAuditHeaderMapping.class.getName(), methodName, "property");
            }

        }

    }

}
