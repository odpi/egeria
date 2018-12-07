/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.contentmanager;


import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LookupHelper {

    private static final Logger log = LoggerFactory.getLogger(EntitiesCreatorHelper.class);
    private static final Integer PAGE_SIZE = 0;
    private OMRSRepositoryConnector enterpriseConnector;
    private OMRSAuditLog auditLog;

    public LookupHelper(OMRSRepositoryConnector enterpriseConnector, OMRSAuditLog auditLog) {
        this.enterpriseConnector = enterpriseConnector;
        this.auditLog = auditLog;
    }

    public EntityDetail findDatabaseColumn(DatabaseColumnSource databaseColumnSource) {
        String networkAddress = databaseColumnSource.getTableSource().getNetworkAddress();
        InstanceProperties matchProperties = new InstanceProperties();
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", matchProperties, Constants.NETWORK_ADDRESS, networkAddress, "findDatabaseColumn");
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", matchProperties, Constants.PROTOCOL, databaseColumnSource.getTableSource().getProtocol(), "findDatabaseColumn");

        TypeDef typeDef = enterpriseConnector.getRepositoryHelper().getTypeDefByName(Constants.USER_ID, Constants.ENDPOINT);
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
            auditLog.logException("getEntity",
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


    private EntityDetail matchExactlyToUniqueEntity(List<EntityDetail> entities, final InstanceProperties matchingProperties) throws Exception {

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

    private boolean matchProperties(EntityDetail entityDetail, InstanceProperties matchingProperties) {
        InstanceProperties entityProperties = entityDetail.getProperties();
        for (Map.Entry<String, InstancePropertyValue> property : matchingProperties.getInstanceProperties().entrySet()) {

            String actualValue = enterpriseConnector.getRepositoryHelper().getStringProperty("", property.getKey(), entityProperties, "matchProperties");//TODO only string supported for now
            if (!property.getValue().equals(actualValue)) {
                return false;
            }
        }
        return true;

    }
}




