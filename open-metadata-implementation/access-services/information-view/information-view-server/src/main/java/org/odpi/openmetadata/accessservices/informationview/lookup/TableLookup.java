/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.TableSource;
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

public class TableLookup extends EntityLookup<TableSource> {

    private static final Logger log = LoggerFactory.getLogger(TableLookup.class);

    public TableLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog, Constants.RELATIONAL_TABLE);
    }

    @Override
    public EntityDetail lookupEntity(TableSource source){
        EntityDetail entity = Optional.ofNullable(super.lookupEntity(source))
                                      .orElseGet(() -> lookupStartingFromParent(source));
        if(log.isDebugEnabled()) {
            log.debug("Table found [{}]", entity);
        }
        return entity;
    }

    public EntityDetail lookupStartingFromParent(TableSource source){
        Supplier<InformationViewExceptionBase> exceptionBaseSupplier =
                () -> ExceptionHandler.buildRetrieveEntityException(Constants.SOURCE, source.toString(), null,
                                                                    this.getClass().getName());
        EntityDetail entityDetail = Optional.ofNullable(parentChain.lookupEntity(source))
                                            .orElseThrow(exceptionBaseSupplier) ;

        return Optional.ofNullable(lookup(source, entityDetail))
                       .orElseThrow(exceptionBaseSupplier);
    }

    private EntityDetail lookup(TableSource source, EntityDetail schemaDatabase) {
        List<EntityDetail> allSchemaType = omEntityDao.getRelatedEntities(Arrays.asList(schemaDatabase.getGUID()),
                                                                            Constants.ASSET_SCHEMA_TYPE,
                                                                            r -> r.getEntityTwoProxy().getGUID());
        if (!CollectionUtils.isEmpty(allSchemaType)) {
            List<EntityDetail> allLinkedTablesList =
                    omEntityDao.getRelatedEntities(allSchemaType.stream().map(InstanceHeader::getGUID).collect(Collectors.toList()),
                                                                            Constants.ATTRIBUTE_FOR_SCHEMA,
                                                                            r -> r.getEntityTwoProxy().getGUID());
            return filterEntities(Arrays.asList(Constants.RELATIONAL_TABLE), source, allLinkedTablesList);
        }
        return null;
    }

    @Override
    protected InstanceProperties getMatchingProperties(TableSource source) {
        return enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(),
                Constants.NAME, source.getName(), "lookupEntity");
    }
}
