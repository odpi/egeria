/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.InformationViewExceptionBase;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ColumnLookup extends EntityLookup<DatabaseColumnSource> {

    private static final Logger log = LoggerFactory.getLogger(ColumnLookup.class);

    public ColumnLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog, Constants.RELATIONAL_COLUMN);
    }

    @Override
    public EntityDetail lookupEntity(DatabaseColumnSource source){
        EntityDetail columnEntity = Optional.ofNullable(super.lookupEntity(source))
                                        .orElseGet(() -> lookupBasedOnHierarchy(source));
        if (log.isDebugEnabled()) {
            log.debug("Column found [{}]", columnEntity);
        }
        return columnEntity;
    }

        public EntityDetail lookupBasedOnHierarchy(DatabaseColumnSource source){
            Supplier<InformationViewExceptionBase> exceptionBaseSupplier =
                    () -> ExceptionHandler.buildRetrieveEntityException(Constants.RELATIONAL_COLUMN,
                            source.toString(), null, this.getClass().getName());
            EntityDetail entityDetail = Optional.ofNullable(parentChain.lookupEntity(source.getTableSource()))
                                                        .orElseThrow(exceptionBaseSupplier);
            return Optional.ofNullable(filterBasedOnMatchingProperties(source, entityDetail))
                            .orElseThrow(exceptionBaseSupplier);
        }

        private EntityDetail filterBasedOnMatchingProperties(DatabaseColumnSource source, EntityDetail tableEntity) {
            List<EntityDetail> relatedEntities = omEntityDao.getRelatedEntities(Arrays.asList(tableEntity.getGUID()),
                                                                                Constants.SCHEMA_ATTRIBUTE_TYPE,
                                                                                r -> r.getEntityTwoProxy().getGUID());
            if (!CollectionUtils.isEmpty(relatedEntities)) {
                List<EntityDetail> allColumns = omEntityDao.getRelatedEntities(relatedEntities.stream().map(InstanceHeader::getGUID).collect(Collectors.toList()),
                                                                                Constants.ATTRIBUTE_FOR_SCHEMA,
                                                                                r -> r.getEntityTwoProxy().getGUID());
                return filterEntities(Arrays.asList(Constants.RELATIONAL_COLUMN,
                                      Constants.DERIVED_RELATIONAL_COLUMN),
                                      source,
                                      allColumns);
            }
            return null;
        }

    protected InstanceProperties getMatchingProperties(DatabaseColumnSource source) {
        String methodName = "getMatchingProperties";
        return enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(), Constants.NAME, source.getName(), methodName);
    }
}
