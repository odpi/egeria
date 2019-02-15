/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.TableSource;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DatabaseLookup extends EntityLookup<TableSource> {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLookup.class);

    public DatabaseLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog);
    }


    @Override
    public EntityDetail lookupEntity(TableSource source) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {
        EntityDetail endpointEntity = parentChain.lookupEntity(source);
        if(endpointEntity == null)
            return null;
        List<Relationship> relationships = omEntityDao.getRelationships(Constants.CONNECTION_TO_ENDPOINT, endpointEntity.getGUID());
        List<String> allConnectionGuids = relationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toList());
        List<Relationship> allConnectionToDatabaseRelationships = allConnectionGuids.stream().flatMap(e -> {
            try {
                return omEntityDao.getRelationships(Constants.CONNECTION_TO_ASSET, e).stream();
            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage(), exception);
            }
        }).collect(Collectors.toList());

        Set<String> allLinkedDatabasesGuids = allConnectionToDatabaseRelationships.stream().map(e -> e.getEntityTwoProxy().getGUID()).collect(Collectors.toSet());
        List<EntityDetail> allLinkedDatabasesList = allLinkedDatabasesGuids.stream().map(guid -> {
            try {
                return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, guid);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }).collect(Collectors.toList());
        EntityDetail databaseEntity = lookupEntity(source, allLinkedDatabasesList);
        log.info("Database found [{}]", databaseEntity);
        return databaseEntity;
    }

    @Override
    public void setParentChain(EntityLookup parentChain) {
        this.parentChain = parentChain;
    }

    @Override
    protected InstanceProperties getMatchingProperties(TableSource source) {
        InstanceProperties matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(), Constants.NAME, source.getDatabaseName(), "findDEntity");
        return matchProperties;
    }


}
