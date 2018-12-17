/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.informationview.events.TableSource;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseSchemaLookup extends EntityLookup<TableSource> {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaLookup.class);

    public DatabaseSchemaLookup(OMRSRepositoryConnector enterpriseConnector, EntitiesCreatorHelper entitiesCreatorHelper, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, entitiesCreatorHelper, parentChain, auditLog);
    }

    @Override
    public EntityDetail lookupEntity(TableSource source) throws Exception {
        EntityDetail database = parentChain.lookupEntity(source);

        List<Relationship> relationships = entitiesCreatorHelper.getRelationships(Constants.DATA_CONTENT_FOR_DATASET, database.getGUID());
        List<String> allSchemaGuids = relationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toList());

        List<EntityDetail> allLinkedSchemaList = allSchemaGuids.stream().map(guid -> {
            try {
                return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, guid);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }).collect(Collectors.toList());
        EntityDetail schemaEntity = lookupEntity(source, allLinkedSchemaList);
        log.info("DatabaseSchema found [{}]", schemaEntity);
        return schemaEntity;

    }

    @Override
    protected InstanceProperties getMatchingProperties(TableSource source) {
        return enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(), Constants.NAME, source.getSchemaName(), "findDatabase");
    }

  
}
