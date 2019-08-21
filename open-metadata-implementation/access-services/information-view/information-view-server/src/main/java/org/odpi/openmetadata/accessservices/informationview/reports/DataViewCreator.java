/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataViewCreator extends DataViewBasicOperation{

    private static final Logger log = LoggerFactory.getLogger(DataViewCreator.class);

    protected DataViewCreator(org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, lookupHelper, helper, auditLog);
    }


    /**
     *
     *
     * @param userId
     * @param requestBody json describing the data view
     * @param dataViewEntity the entity describing the data view
     */
    public void createDataView(String userId, DataViewRequestBody requestBody, EntityDetail dataViewEntity) {
            String methodName = "createDataView";
            String qualifiedNameForComplexSchemaType = QualifiedNameUtils.buildQualifiedName(requestBody.getDataView().getEndpointAddress(), Constants.COMPLEX_SCHEMA_TYPE, requestBody.getDataView().getId() + Constants.TYPE_SUFFIX);
            InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                    .build();
            OMEntityWrapper complexSchemaTypeEntityWrapper = omEntityDao.createOrUpdateExternalEntity(userId,
                    Constants.COMPLEX_SCHEMA_TYPE,
                    qualifiedNameForComplexSchemaType,
                    requestBody.getRegistrationGuid(),
                    requestBody.getRegistrationQualifiedName(),
                    complexSchemaTypeProperties,
                    null,
                    false,
                    false);

            log.debug("Created data view schema type {}", complexSchemaTypeEntityWrapper.getEntityDetail().getGUID());
            omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                    dataViewEntity.getGUID(),
                    complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(),
                    new InstanceProperties());

        String qualifiedNameForDataView = helper.getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, Constants.QUALIFIED_NAME, dataViewEntity.getProperties(), methodName);
            addElements(userId, qualifiedNameForDataView, complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(), requestBody.getRegistrationGuid(), requestBody.getRegistrationQualifiedName(), requestBody.getDataView().getElements());
    }
}
