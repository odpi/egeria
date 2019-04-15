/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.DataViewCreationException;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataViewHandler {


    private static final Logger log = LoggerFactory.getLogger(DataViewHandler.class);
    private org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao omEntityDao;
    private OMRSAuditLog auditLog;
    private DataViewCreator dataViewCreator;
    private DataViewUpdater dataViewUpdater;


    public DataViewHandler(OMEntityDao omEntityDao, OMRSRepositoryHelper helper, OMRSAuditLog auditLog) {
        this.omEntityDao = omEntityDao;
        dataViewCreator = new DataViewCreator(omEntityDao, helper, auditLog);
        dataViewUpdater = new DataViewUpdater(omEntityDao, helper, auditLog);
        this.auditLog = auditLog;
    }


    /**
     *
     * @param requestBody - json describing the data view
     * @throws DataViewCreationException
     */
    public void createDataView(DataViewRequestBody requestBody) throws DataViewCreationException {

        log.debug("Creating data view based on payload {}", requestBody);
        try {
            String qualifiedNameForDataView = QualifiedNameUtils.buildQualifiedName(requestBody.getEndpointAddress(), Constants.INFORMATION_VIEW, requestBody.getId());
            InstanceProperties dataViewProperties = new EntityPropertiesBuilder()
                    .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataView)
                    .withStringProperty(Constants.NAME, requestBody.getName())
                    .withStringProperty(Constants.OWNER, requestBody.getAuthor())
                    .withStringProperty(Constants.ID, requestBody.getId())
                    .withStringProperty(Constants.LAST_MODIFIER, requestBody.getLastModifier())
                    .withDateProperty(Constants.LAST_MODIFIED_TIME, requestBody.getLastModifiedTime())
                    .withDateProperty(Constants.CREATE_TIME, requestBody.getCreatedTime())
                    .withStringProperty(Constants.NATIVE_CLASS, requestBody.getNativeClass())
                    .build();


            OMEntityWrapper dataViewWrapper = omEntityDao.createOrUpdateEntity(Constants.INFORMATION_VIEW,
                                                                            qualifiedNameForDataView,
                                                                            dataViewProperties,
                                                                            null,
                                                                            true, true);


            dataViewCreator.createDataView(requestBody, dataViewWrapper.getEntityDetail());

//            if (dataViewWrapper.getEntityStatus().equals(OMEntityWrapper.EntityStatus.NEW)) {
//                dataViewCreator.createDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } else {
//                dataViewUpdater.updateDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } TODO update not implemented yet


        } catch (PagingErrorException | PropertyErrorException | EntityNotKnownException | UserNotAuthorizedException | StatusNotSupportedException | InvalidParameterException | FunctionNotSupportedException | RepositoryErrorException | TypeErrorException | ClassificationErrorException e) {

            log.error(e.getMessage(), e);
            throw new DataViewCreationException(InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getHttpErrorCode(),
                                               DataViewHandler.class.getName(),
                                               InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getFormattedErrorMessage(requestBody.toString(), e.getMessage()),
                                               InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getUserAction(),
                                               InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getSystemAction(),
                                                e,
                                                requestBody.getId());
        }

    }


}
