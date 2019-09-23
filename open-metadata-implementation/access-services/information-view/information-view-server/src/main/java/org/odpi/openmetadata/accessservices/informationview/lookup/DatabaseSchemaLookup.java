/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.TableSource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DatabaseSchemaLookup extends EntityLookup<TableSource> {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSchemaLookup.class);

    public DatabaseSchemaLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog, Constants.DEPLOYED_DATABASE_SCHEMA);
    }

    @Override
    public EntityDetail lookupEntity(TableSource source){
        EntityDetail entity = Optional.ofNullable(super.lookupEntity(source))
                                                .orElseGet(() -> lookupBasedOnHierarchy(source));
        if (log.isDebugEnabled()) {
            log.debug("DatabaseSchema found [{}]", entity);
        }
        return entity;
    }

    private EntityDetail lookupBasedOnHierarchy(TableSource source) {
        Optional<EntityDetail> optional = Optional.ofNullable(parentChain.lookupEntity(source.getDatabaseSource()));
        return optional.ofNullable(filterBasedOnMatchingProperties(source, optional))
                        .orElseThrow(() -> ExceptionHandler.buildRetrieveEntityException(Constants.SOURCE, source.toString(), null, this.getClass().getName()));
    }

    private EntityDetail filterBasedOnMatchingProperties(TableSource source, Optional<EntityDetail> optional) {
        List<EntityDetail> allSchemas = omEntityDao.getRelatedEntities(Arrays.asList(optional.get().getGUID()), Constants.DATA_CONTENT_FOR_DATASET, e -> e.getEntityTwoProxy().getGUID());
        return filterEntities(Arrays.asList(Constants.DEPLOYED_DATABASE_SCHEMA, Constants.INFORMATION_VIEW), source, allSchemas);
    }

    @Override
    protected InstanceProperties getMatchingProperties(TableSource source) {
        // GDW - each string property added to matchProperties shoudl be converted to exact match regex
        String sourceSchemaNameRegex = enterpriseConnector.getRepositoryHelper().getExactMatchRegex(source.getSchemaName());
        return enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(),
                Constants.NAME, sourceSchemaNameRegex, "findDatabase");
    }

  
}
