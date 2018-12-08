/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Handles mapping the most basic of IGC objects' attributes to OMRS entity attributes.
 */
public abstract class ReferenceMapper {

    public static final String SOURCE_NAME = "IGC";
    public static final String IGC_REST_GENERATED_MODEL_PKG = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated";

    protected IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    protected Reference me;

    protected String igcType;
    protected String igcPOJO;
    protected String omrsType;
    protected String userId;

    protected EntitySummary summary;
    protected EntityDetail detail;
    protected ArrayList<Relationship> relationships;
    protected ArrayList<Classification> classifications;
    protected ArrayList<String> alreadyMappedProperties;

    public ReferenceMapper(Reference me, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {
        this.me = me;
        this.igcomrsRepositoryConnector = igcomrsRepositoryConnector;
        this.userId = userId;
        this.relationships = new ArrayList<>();
        this.alreadyMappedProperties = new ArrayList<>();
    }

    protected EntitySummary getSummary() { return this.summary; }
    protected EntityDetail getDetail() { return this.detail; }
    protected List<Relationship> getRelationships() { return this.relationships; }
    protected List<Classification> getClassifications() { return this.classifications; }
    protected List<String> getAlreadyMappedProperties() { return this.alreadyMappedProperties; }
    protected void addAlreadyMappedProperty(String propertyName) { this.alreadyMappedProperties.add(propertyName); }
    protected void addAlreadyMappedProperties(List<String> propertyNames) { this.alreadyMappedProperties.addAll(propertyNames); }

    public abstract EntitySummary getOMRSEntitySummary();
    public abstract EntityDetail getOMRSEntityDetail();
    public abstract List<Relationship> getOMRSRelationships(String          relationshipTypeGUID,
                                                            int             fromRelationshipElement,
                                                            SequencingOrder sequencingOrder,
                                                            int             pageSize);

    /**
     * Sets up the private 'classifications' member with all classifications for this object;
     * this method should be overridden if there are any implementation-specific classifications that need
     * to be catered for.
     */
    protected abstract void getMappedClassifications();

    protected void mapIGCToOMRSEntitySummary(List<String> classificationProperies) {
        if (summary == null) {
            summary = new EntitySummary();
            summary.setGUID(me.getId());
            summary.setInstanceURL(me.getUrl());
        }
    }

    protected void mapIGCToOMRSEntityDetail(PropertyMappingSet mappings, List<String> classificationProperties) {
        if (detail == null) {
            try {
                detail = igcomrsRepositoryConnector.getRepositoryHelper().getSkeletonEntity(
                        SOURCE_NAME,
                        igcomrsRepositoryConnector.getMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        userId,
                        omrsType
                );
            } catch (TypeErrorException e) {
                e.printStackTrace();
            }
            detail.setStatus(InstanceStatus.ACTIVE);
            detail.setGUID(me.getId());
            detail.setInstanceURL(me.getUrl());
        }
    }

    /**
     * Returns the OMRS PrimitivePropertyValue represented by the provided value.
     *
     * @param value the value to represent as an OMRS PrimitivePropertyValue
     * @return PrimitivePropertyValue
     */
    public static PrimitivePropertyValue getPrimitivePropertyValue(Object value) {
        PrimitivePropertyValue propertyValue = new PrimitivePropertyValue();
        PrimitiveDef primitiveDef = new PrimitiveDef();
        if (value instanceof Boolean) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_BOOLEAN);
        } else if (value instanceof Date) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_DATE);
        } else if (value instanceof Integer) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        } else if (value instanceof Number) {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_FLOAT);
        } else {
            primitiveDef.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        }
        propertyValue.setPrimitiveValue(value);
        propertyValue.setPrimitiveDefCategory(primitiveDef.getPrimitiveDefCategory());
        propertyValue.setTypeGUID(primitiveDef.getGUID());
        propertyValue.setTypeName(primitiveDef.getName());
        return propertyValue;
    }

    /**
     * Merge together the values of all the provided arrays.
     * (From: https://stackoverflow.com/questions/80476/how-can-i-concatenate-two-arrays-in-java)
     *
     * @param first first array to merge
     * @param rest subsequent arrays to merge
     * @return T[]
     */
    @SafeVarargs
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        int totalLength = first.length;
        for (T[] array : rest) {
            totalLength += array.length;
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }


    public static EntityProxy getEntityProxyForObject(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                                      Reference igcObj,
                                                      String omrsTypeName,
                                                      String userId) throws RepositoryErrorException {

        final String methodName = "getEntityProxyForObject";
        final String className = "ReferenceMapper";

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();
        String igcType = igcObj.getType();
        PrimitivePropertyValue qualifiedName = null;

        EntityProxy entityProxy = null;

        if (igcType != null) {
            // Construct 'qualifiedName' from the Identity of the object (label is a special case as a non-MainObject)
            MainObject igcMObj = null;
            if (igcType.equals("label")) {
                qualifiedName = ReferenceMapper.getPrimitivePropertyValue("(label)=" + igcObj.getName());
            } else {
                try {
                    igcMObj = (MainObject) igcObj;
                    qualifiedName = ReferenceMapper.getPrimitivePropertyValue(igcMObj.getIdentity(igcRestClient).toString());
                } catch (ClassCastException e) {
                    OMRSErrorCode errorCode = OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE;
                    String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(igcType,
                            null,
                            methodName,
                            igcomrsRepositoryConnector.getRepositoryName());
                    throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                            className,
                            methodName,
                            errorMessage,
                            errorCode.getSystemAction(),
                            errorCode.getUserAction());
                }
            }

            // 'qualifiedName' is the only unique InstanceProperty we need on an EntityProxy
            InstanceProperties uniqueProperties = new InstanceProperties();
            uniqueProperties.setProperty("qualifiedName", qualifiedName);

            try {
                entityProxy = igcomrsRepositoryConnector.getRepositoryHelper().getNewEntityProxy(
                        SOURCE_NAME,
                        igcomrsRepositoryConnector.getMetadataCollectionId(),
                        InstanceProvenanceType.LOCAL_COHORT,
                        userId,
                        omrsTypeName,
                        uniqueProperties,
                        null
                );
                entityProxy.setGUID(igcObj.getId());

                if (igcMObj != null) {
                    entityProxy.setCreatedBy(igcMObj.getCreatedBy());
                    entityProxy.setCreateTime(igcMObj.getCreatedOn());
                    entityProxy.setUpdatedBy(igcMObj.getModifiedBy());
                    entityProxy.setUpdateTime(igcMObj.getModifiedOn());
                }

            } catch (TypeErrorException e) {
                e.printStackTrace();
            }
        }

        return entityProxy;

    }

}
