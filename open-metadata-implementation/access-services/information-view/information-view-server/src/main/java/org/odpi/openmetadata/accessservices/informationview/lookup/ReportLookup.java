/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;


import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.ReportSource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class ReportLookup extends EntityLookup<ReportSource>  {


    private static final Logger log = LoggerFactory.getLogger(ReportLookup.class);

    public ReportLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog) {
        super(enterpriseConnector, omEntityDao, parentChain, auditLog, Constants.DEPLOYED_DATABASE_SCHEMA);
    }

    @Override
    public EntityDetail lookupEntity(ReportSource source){
        EntityDetail entity = Optional.ofNullable(findEntity(getMatchingProperties(source), Constants.DEPLOYED_REPORT))
                                        .orElseThrow(() -> ExceptionHandler.buildEntityNotFoundException(Constants.SOURCE,
                                                                                                        source.toString(),
                                                                                                        Constants.DEPLOYED_REPORT,
                                                                                                        this.getClass().getName()));
        if(log.isDebugEnabled()) {
            log.debug("Report found [{}]", entity);
        }
        return entity;
    }

    @Override
    protected InstanceProperties getMatchingProperties(ReportSource source) {
        InstanceProperties matchProperties = new InstanceProperties();
        // GDW - each string property added to matchProperties shoudl be converted to exact match regex
        String sourceReportIdRegex = enterpriseConnector.getRepositoryHelper().getExactMatchRegex(source.getReportId());
        matchProperties = enterpriseConnector.getRepositoryHelper().addStringPropertyToInstance(Constants.INFORMATION_VIEW_OMAS_NAME, matchProperties,
                Constants.ID, sourceReportIdRegex, "getMatchingProperties");
        return matchProperties;
    }

}
