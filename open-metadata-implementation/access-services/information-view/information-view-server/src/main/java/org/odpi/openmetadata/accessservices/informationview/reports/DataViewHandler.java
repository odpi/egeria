/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.DataViewCreationException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataViewHandler {


    private static final Logger log = LoggerFactory.getLogger(DataViewHandler.class);
    private org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao;
    private OMRSAuditLog auditLog;
    private DataViewCreator dataViewCreator;
    private DataViewUpdater dataViewUpdater;


    public DataViewHandler(OMEntityDao omEntityDao, LookupHelper lookupHelper, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        this.omEntityDao = omEntityDao;
        dataViewCreator = new DataViewCreator(omEntityDao,lookupHelper, helper, auditLog);
        dataViewUpdater = new DataViewUpdater(omEntityDao, lookupHelper, helper, auditLog);
        this.auditLog = auditLog;
    }


    /**
     * Creating data view based on payload
     *
     * @param userId calling user
     * @param requestBody json describing the data view
     * @return unique identifier for the guid
     * @throws DataViewCreationException unable to create view
     */
    public String createDataView(String userId, DataViewRequestBody requestBody) throws DataViewCreationException {

        log.debug("Creating data view based on payload {}", requestBody);
        SoftwareServerCapabilitySource softwareServerCapabilitySource = dataViewCreator.retrieveSoftwareServerCapability(requestBody.getRegistrationGuid(), requestBody.getRegistrationQualifiedName());
        requestBody.setRegistrationGuid(softwareServerCapabilitySource.getGuid());
        requestBody.setRegistrationQualifiedName(softwareServerCapabilitySource.getQualifiedName());

            String qualifiedNameForDataView = QualifiedNameUtils.buildQualifiedName("", Constants.INFORMATION_VIEW, requestBody.getDataView().getId());
            InstanceProperties dataViewProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataView)
                    .withStringProperty(Constants.NAME, requestBody.getDataView().getName())
                    .withStringProperty(Constants.OWNER, requestBody.getDataView().getAuthor())
                    .withStringProperty(Constants.ID, requestBody.getDataView().getId())
                    .withStringProperty(Constants.LAST_MODIFIER, requestBody.getDataView().getLastModifier())
                    .withDateProperty(Constants.LAST_MODIFIED_TIME, requestBody.getDataView().getLastModifiedTime())
                    .withDateProperty(Constants.CREATE_TIME, requestBody.getDataView().getCreatedTime())
                    .withStringProperty(Constants.NATIVE_CLASS, requestBody.getDataView().getNativeClass())
                    .build();


            OMEntityWrapper dataViewWrapper = omEntityDao.createOrUpdateExternalEntity(userId,
                                                                                Constants.INFORMATION_VIEW,
                                                                                qualifiedNameForDataView,
                                                                                requestBody.getRegistrationGuid(),
                                                                                requestBody.getRegistrationQualifiedName(),
                                                                                dataViewProperties,
                                                                                null,
                                                                                true,
                                                                    true);


            dataViewCreator.createDataView(userId, requestBody, dataViewWrapper.getEntityDetail());

//            if (dataViewWrapper.getEntityStatus().equals(OMEntityWrapper.EntityStatus.NEW)) {
//                dataViewCreator.createDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } else {
//                dataViewUpdater.updateDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } TODO update not implemented yet
            return dataViewWrapper.getEntityDetail().getGUID();
        }
    }