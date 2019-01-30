/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.lookup;

import org.odpi.openmetadata.accessservices.assetlineage.contentmanager.EntitiesCreatorHelper;
import org.odpi.openmetadata.accessservices.assetlineage.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.assetlineage.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ColumnLookup extends EntityLookup<DatabaseColumnSource> {

    private static final Logger log = LoggerFactory.getLogger(ColumnLookup.class);

    public ColumnLookup(OMRSRepositoryConnector enterpriseConnector, EntitiesCreatorHelper entitiesCreatorHelper, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, entitiesCreatorHelper, parentChain, auditLog);
    }

    @Override
    public EntityDetail lookupEntity(DatabaseColumnSource source) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {
        EntityDetail tableEntity = parentChain.lookupEntity(source.getTableSource());
        if(tableEntity == null)
            return null;
        List<Relationship> relationships = entitiesCreatorHelper.getRelationships(Constants.SCHEMA_ATTRIBUTE_TYPE, tableEntity.getGUID());
        List<String> allTableTypeGuids = relationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toList());

        List<Relationship> allTableTypeToColumnRelationships = allTableTypeGuids.stream().flatMap(e -> {
            try {
                return entitiesCreatorHelper.getRelationships(Constants.ATTRIBUTE_FOR_SCHEMA, e).stream();
            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
        }).collect(Collectors.toList());

        Set<String> allLinkedColumnsGuids = allTableTypeToColumnRelationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toSet());
        List<EntityDetail> allLinkedColumnsList = allLinkedColumnsGuids.stream().map(guid -> {
            try {
                return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, guid);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }).collect(Collectors.toList());

        EntityDetail columnEntity = lookupEntity(source, allLinkedColumnsList);
        log.info("Column found [{}]", columnEntity);
        return columnEntity;
    }



    protected InstanceProperties getMatchingProperties(DatabaseColumnSource source) {
        return enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(), Constants.NAME, source.getName(), "lookupEntity");
    }
}
