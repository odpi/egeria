/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DatabaseColumnSource;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
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

public class ColumnLookup extends EntityLookup<DatabaseColumnSource> {

    private static final Logger log = LoggerFactory.getLogger(ColumnLookup.class);

    public ColumnLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog);
    }

    @Override
    public EntityDetail lookupEntity(DatabaseColumnSource source) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {
        EntityDetail tableEntity = parentChain.lookupEntity(source.getTableSource());
        if(tableEntity == null)
            return null;

        List<String> relatedEntitiesGuids = getRelatedEntities(tableEntity.getGUID(), Constants.SCHEMA_ATTRIBUTE_TYPE);
        List<EntityDetail> allLinkedColumnsList = getRelatedEntities(relatedEntitiesGuids, Constants.ATTRIBUTE_FOR_SCHEMA);
        EntityDetail columnEntity = lookupEntity(source, allLinkedColumnsList);
        log.debug("Column found [{}]", columnEntity);
        return columnEntity;
    }



    protected InstanceProperties getMatchingProperties(DatabaseColumnSource source) {
        return enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance("", new InstanceProperties(), Constants.NAME, source.getName(), "lookupEntity");
    }
}
