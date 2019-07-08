/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.reports;

import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityWrapper;
import org.odpi.openmetadata.accessservices.informationview.events.DataViewRequestBody;
import org.odpi.openmetadata.accessservices.informationview.events.SoftwareServerCapabilitySource;
import org.odpi.openmetadata.accessservices.informationview.ffdc.InformationViewErrorCode;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.DataViewCreationException;
import org.odpi.openmetadata.accessservices.informationview.lookup.LookupHelper;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityConflictException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.HomeEntityException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidEntityException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
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
     *
     * @param requestBody - json describing the data view
     * @throws DataViewCreationException
     */
    public void createDataView(DataViewRequestBody requestBody) throws DataViewCreationException {

        log.debug("Creating data view based on payload {}", requestBody);
        SoftwareServerCapabilitySource softwareServerCapabilitySource = dataViewCreator.retrieveSoftwareServerCapability(requestBody.getRegistrationGuid(), requestBody.getRegistrationQualifiedName());
        requestBody.setRegistrationGuid(softwareServerCapabilitySource.getGuid());
        requestBody.setRegistrationQualifiedName(softwareServerCapabilitySource.getQualifiedName());

        try {
            String qualifiedNameForDataView = QualifiedNameUtils.buildQualifiedName(requestBody.getRegistrationQualifiedName(), Constants.INFORMATION_VIEW, requestBody.getDataView().getId());
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


            OMEntityWrapper dataViewWrapper = omEntityDao.saveEntityReferenceCopy(requestBody.getRegistrationGuid(),
                                                                                Constants.INFORMATION_VIEW,
                                                                                qualifiedNameForDataView,
                                                                                dataViewProperties,
                                                                                true,
                                                                                true);


            dataViewCreator.createDataView(requestBody, dataViewWrapper.getEntityDetail());

//            if (dataViewWrapper.getEntityStatus().equals(OMEntityWrapper.EntityStatus.NEW)) {
//                dataViewCreator.createDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } else {
//                dataViewUpdater.updateDataView(requestBody, dataViewWrapper.getEntityDetail());
//            } TODO update not implemented yet


        } catch (PagingErrorException | PropertyErrorException | EntityNotKnownException | UserNotAuthorizedException | StatusNotSupportedException | InvalidParameterException | FunctionNotSupportedException | RepositoryErrorException | TypeErrorException | ClassificationErrorException |InvalidEntityException | EntityConflictException | HomeEntityException e) {
            throw new DataViewCreationException(InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getHttpErrorCode(),
                                               DataViewHandler.class.getName(),
                                               InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getFormattedErrorMessage(requestBody.getDataView().toString(), e.getMessage()),
                                               InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getUserAction(),
                                               InformationViewErrorCode.INFORMATION_VIEW_SUBMIT_EXCEPTION.getSystemAction(),
                                                e,
                                                requestBody.getDataView().getId());
        }

    }


}
