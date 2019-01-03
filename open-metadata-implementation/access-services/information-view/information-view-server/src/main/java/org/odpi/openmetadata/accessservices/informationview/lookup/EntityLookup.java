/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;


import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class EntityLookup<T extends Source> {

    private static final Integer PAGE_SIZE = 0;
    private static final Logger log = LoggerFactory.getLogger(EntityLookup.class);
    protected OMRSRepositoryConnector enterpriseConnector;
    protected EntitiesCreatorHelper entitiesCreatorHelper;
    protected OMRSAuditLog auditLog;
    protected EntityLookup parentChain;

    public EntityLookup(OMRSRepositoryConnector enterpriseConnector, EntitiesCreatorHelper entitiesCreatorHelper, EntityLookup parentChain, OMRSAuditLog auditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.entitiesCreatorHelper = entitiesCreatorHelper;
        this.auditLog = auditLog;
        this.parentChain = parentChain;
    }

    public abstract EntityDetail lookupEntity(T source) throws Exception;

    public void setParentChain(EntityLookup parentChain) {
        this.parentChain = parentChain;
    }

    public EntityDetail lookupEntity(T source, List<EntityDetail> list) throws Exception {
        InstanceProperties matchProperties = getMatchingProperties(source);
        return matchExactlyToUniqueEntity(list, matchProperties);
    }

    protected abstract InstanceProperties getMatchingProperties(T source);


    public EntityDetail findEntity(InstanceProperties matchProperties, String typeDefName) {
        TypeDef typeDef = enterpriseConnector.getRepositoryHelper().getTypeDefByName(Constants.USER_ID, typeDefName);
        List<EntityDetail> existingEntities;
        try {
            existingEntities = enterpriseConnector.getMetadataCollection()
                    .findEntitiesByProperty(Constants.USER_ID,
                            typeDef.getGUID(),
                            matchProperties,
                            MatchCriteria.ALL,
                            0,
                            Collections.singletonList(InstanceStatus.ACTIVE),
                            null,
                            null,
                            null,
                            SequencingOrder.ANY,
                            PAGE_SIZE);
            return matchExactlyToUniqueEntity(existingEntities, matchProperties);
        } catch (Exception e) {
            InformationViewErrorCode auditCode = InformationViewErrorCode.GET_ENTITY_EXCEPTION;
            auditLog.logException("findEntity",
                    auditCode.getErrorMessageId(),
                    OMRSAuditLogRecordSeverity.EXCEPTION,
                    auditCode.getFormattedErrorMessage("matchingProperties: " + matchProperties),
                    "entity with properties{" + matchProperties + "}",
                    auditCode.getSystemAction(),
                    auditCode.getUserAction(),
                    e);

            throw new RuntimeException(e);
        }
    }


    public EntityDetail matchExactlyToUniqueEntity(List<EntityDetail> entities, final InstanceProperties matchingProperties) throws RuntimeException {

        if (entities != null && !entities.isEmpty()) {
            List<EntityDetail> filteredEntities = entities.stream().filter(e -> matchProperties(e, matchingProperties)).collect(Collectors.toList());
            if (filteredEntities == null || filteredEntities.isEmpty()) {
                throw new RuntimeException("No entity matches the criteria.");
            }
            if (filteredEntities.size() > 1) {
                throw new RuntimeException("Too many entities are matching the criteria.");//TODO exception
            }
            return filteredEntities.get(0);
        }
        return null;
    }

    public boolean matchProperties(EntityDetail entityDetail, InstanceProperties matchingProperties) {
        InstanceProperties entityProperties = entityDetail.getProperties();
        for (Map.Entry<String, InstancePropertyValue> property : matchingProperties.getInstanceProperties().entrySet()) {

            String actualValue = enterpriseConnector.getRepositoryHelper().getStringProperty("", property.getKey(), entityProperties, "matchProperties");//TODO only string supported for now
            if (!((PrimitivePropertyValue)property.getValue()).getPrimitiveValue().equals(actualValue)) {
                return false;
            }
        }
        return true;
    }


}
