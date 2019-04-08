/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.TableSource;
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

public class DatabaseLookup extends EntityLookup<TableSource> {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLookup.class);

    public DatabaseLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog);
    }


    @Override
    public EntityDetail lookupEntity(TableSource source) throws UserNotAuthorizedException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException, EntityNotKnownException {
        EntityDetail parentEntity = parentChain.lookupEntity(source);
        if(parentEntity == null)
            return null;
        List<String> allConnectionGuids = getRelatedEntities(parentEntity.getGUID(), Constants.CONNECTION_TO_ENDPOINT);
        List<EntityDetail> allLinkedDatabasesList = getRelatedEntities(allConnectionGuids, Constants.CONNECTION_TO_ASSET);

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
