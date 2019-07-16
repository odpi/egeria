/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewSource;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class DataViewLookup extends EntityLookup<DataViewSource> {

    private static final Logger log = LoggerFactory.getLogger(DataViewLookup.class);

    public DataViewLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog, Constants.INFORMATION_VIEW);
    }

    @Override
    public EntityDetail lookupEntity(DataViewSource source){
        EntityDetail entity = Optional.ofNullable(super.lookupEntity(source))
                                                .orElseGet(() -> findEntity(getMatchingProperties(source), Constants.INFORMATION_VIEW));

        if(log.isDebugEnabled()) {
            log.debug("DataView found [{}]", entity);
        }
        return entity;
    }

    @Override
    protected InstanceProperties getMatchingProperties(DataViewSource source) {
        InstanceProperties matchProperties = new InstanceProperties();
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, matchProperties, Constants.ID, source.getId(), "getMatchingProperties");
        return matchProperties;
    }
}
