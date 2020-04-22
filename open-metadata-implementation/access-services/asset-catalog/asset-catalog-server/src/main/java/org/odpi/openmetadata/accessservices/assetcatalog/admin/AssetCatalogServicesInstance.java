/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.admin;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.RelationshipHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OCFOMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetCatalogServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
class AssetCatalogServicesInstance extends OCFOMASServiceInstance {

    private static final AccessServiceDescription description = AccessServiceDescription.ASSET_CATALOG_OMAS;

    private AssetCatalogHandler assetCatalogHandler;
    private RelationshipHandler relationshipHandler;

    /**
     * @param repositoryConnector     link to the repository responsible for servicing the REST calls.
     * @param supportedZones          configurable list of zones that Asset Catalog is allowed to serve Assets from
     * @param auditLog                logging destination
     * @param localServerUserId       userId used for server initiated actions
     * @param supportedTypesForSearch
     * @throws NewInstanceException a problem occurred during initialization
     */
    AssetCatalogServicesInstance(OMRSRepositoryConnector repositoryConnector,
                                 List<String> supportedZones, AuditLog auditLog,
                                 String localServerUserId, List<String> supportedTypesForSearch) throws NewInstanceException {

        super(description.getAccessServiceName() + " OMAS", repositoryConnector, auditLog, localServerUserId, repositoryConnector.getMaxPageSize());
        super.supportedZones = supportedZones;

        if (repositoryHandler != null) {

            assetCatalogHandler = new AssetCatalogHandler(serverName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                    errorHandler, supportedZones, supportedTypesForSearch);
            relationshipHandler = new RelationshipHandler(invalidParameterHandler, repositoryHandler, repositoryHelper, errorHandler);
        } else {
            final String methodName = "new ServiceInstance";

            AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.OMRS_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

            throw new org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException(errorCode.getHttpErrorCode(), this.getClass().getName(), methodName,
                    errorMessage, errorCode.getSystemAction(), errorCode.getUserAction());
        }
    }


    /**
     * Return the handler for assets requests
     *
     * @return handler object
     */
    AssetCatalogHandler getAssetCatalogHandler() {
        return assetCatalogHandler;
    }


    /**
     * Return the handler for relationships requests
     *
     * @return handler object
     */
    RelationshipHandler getRelationshipHandler() {
        return relationshipHandler;
    }

}
