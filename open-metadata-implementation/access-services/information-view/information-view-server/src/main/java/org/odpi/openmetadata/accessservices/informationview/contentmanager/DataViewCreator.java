/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.contentmanager;

import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataViewCreator extends DataViewBasicOperation{

    private static final Logger log = LoggerFactory.getLogger(DataViewCreator.class);

    protected DataViewCreator(EntitiesCreatorHelper entitiesCreatorHelper, OMRSAuditLog auditLog) {
        super(entitiesCreatorHelper, auditLog);
    }

        public void createDataView(DataViewRequestBody requestBody, EntityDetail dataViewEntity) throws Exception {
            String qualifiedNameForComplexSchemaType = EntityPropertiesUtils.getStringValueForProperty(dataViewEntity.getProperties(), Constants.QUALIFIED_NAME) + Constants.TYPE_SUFFIX;
            InstanceProperties complexSchemaTypeProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForComplexSchemaType)
                    .build();
            EntityDetailWrapper complexSchemaTypeEntityWrapper = entitiesCreatorHelper.createOrUpdateEntity(Constants.COMPLEX_SCHEMA_TYPE,
                    qualifiedNameForComplexSchemaType, complexSchemaTypeProperties, null, true);

            log.debug("Created data view schema type {}", complexSchemaTypeEntityWrapper.getEntityDetail().getGUID());
            entitiesCreatorHelper.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                    dataViewEntity.getGUID(),
                    complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(),
                    Constants.INFORMATION_VIEW_OMAS_NAME,
                    new InstanceProperties());
            addElements(requestBody.getEndpointAddress(), complexSchemaTypeEntityWrapper.getEntityDetail().getGUID(), requestBody.getElements());


    }
}
