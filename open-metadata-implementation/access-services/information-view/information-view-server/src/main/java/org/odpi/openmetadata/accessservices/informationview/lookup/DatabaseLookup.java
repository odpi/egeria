/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseSource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DatabaseLookup extends EntityLookup<DatabaseSource> {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLookup.class);

    public DatabaseLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog, Constants.DATABASE);
    }


    @Override
    public EntityDetail lookupEntity(final DatabaseSource source){
        EntityDetail databaseEntity = Optional.ofNullable(super.lookupEntity(source))
                                                     .orElseGet(() -> lookupBasedOnHierarchy(source));

        if(log.isDebugEnabled()) {
            log.debug("Database found [{}]", databaseEntity);
        }
        return databaseEntity;
    }

    private EntityDetail lookupBasedOnHierarchy(final DatabaseSource source) {
        EntityDetail optionalEndpointEntity = Optional.ofNullable(parentChain.lookupEntity(source.getEndpointSource()))
                .orElseThrow(() -> ExceptionHandler.buildRetrieveEntityException(Constants.SOURCE, source.getEndpointSource().toString(), null, this.getClass().getName()));
        return Optional.ofNullable(filterBasedOnMatchingProperties(source, optionalEndpointEntity))
                                     .orElseThrow(() -> ExceptionHandler.buildRetrieveEntityException(Constants.SOURCE, source.toString(), null, this.getClass().getName()));
    }

    private EntityDetail filterBasedOnMatchingProperties(DatabaseSource source, EntityDetail endpointEntity) {
        List<EntityDetail> allConnection = omEntityDao.getRelatedEntities(Arrays.asList(endpointEntity.getGUID()), Constants.CONNECTION_TO_ENDPOINT, r -> r.getEntityTwoProxy().getGUID());
        List<EntityDetail> allLinkedDatabasesList = omEntityDao.getRelatedEntities(allConnection.stream().map(InstanceHeader::getGUID).collect(Collectors.toList()),
                                                                                    Constants.CONNECTION_TO_ASSET,
                                                                                    r -> r.getEntityTwoProxy().getGUID());
        return filterEntities(Arrays.asList(Constants.DATABASE), source, allLinkedDatabasesList);
    }


    @Override
    public void setParentChain(EntityLookup parentChain) {
        this.parentChain = parentChain;
    }

    @Override
    protected InstanceProperties getMatchingProperties(DatabaseSource source) {
        InstanceProperties matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(), Constants.NAME, source.getName(), "findDEntity");
        return matchProperties;
    }


}
