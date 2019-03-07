/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataViewCreator extends DataViewBasicOperation{

    private static final Logger log = LoggerFactory.getLogger(DataViewCreator.class);

    protected DataViewCreator(org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        super(omEntityDao, helper, auditLog);
    }


    /**
     *
     * @param requestBody json describing the data view
     * @param dataViewEntity the entity describing the data view
     * @throws Exception
     */
    public void createDataView(DataViewRequestBody requestBody, EntityDetail dataViewEntity) throws Exception {
            String qualifiedNameForComplexSchemaType = QualifiedNameUtils.buildQualifiedName(requestBody.getEndpointAddress(), Constants.COMPLEX_SCHEMA_TYPE, requestBody.getId() + Constants.TYPE_SUFFIX);
            InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                    .build();
            OMEntityWrapper complexSchemaTypeEntityWrapper = omEntityDao.createOrUpdateEntity(Constants.COMPLEX_SCHEMA_TYPE,
                    qualifiedNameForComplexSchemaType, complexSchemaTypeProperties, null, false);

            log.debug("Created data view schema type {}", complexSchemaTypeEntityWrapper.getEntityDetail().getGUID());
            omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                    dataViewEntity.getGUID(),
                    complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());
            String qualifiedNameForDataView = EntityPropertiesUtils.getStringValueForProperty(dataViewEntity.getProperties(), Constants.QUALIFIED_NAME);
            addElements(qualifiedNameForDataView, complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(), requestBody.getElements());


    }
}
