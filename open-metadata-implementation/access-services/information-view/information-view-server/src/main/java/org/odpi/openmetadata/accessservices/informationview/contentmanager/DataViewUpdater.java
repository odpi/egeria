/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;

import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataViewUpdater extends DataViewBasicOperation {

    private static final Logger log = LoggerFactory.getLogger(DataViewUpdater.class);

    protected DataViewUpdater(EntitiesCreatorHelper entitiesCreatorHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(entitiesCreatorHelper, helper, auditLog);
    }

    public void updateDataView(DataViewRequestBody requestBody, EntityDetail entityDetail) {
        //TODO
    }
}
