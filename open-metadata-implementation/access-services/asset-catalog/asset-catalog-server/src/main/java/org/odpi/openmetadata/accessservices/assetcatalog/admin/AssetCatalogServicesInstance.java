/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.admin;

import lombok.Getter;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.RelationshipHandler;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.util.List;

/**
 * AssetCatalogServicesInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
class AssetCatalogServicesInstance extends OMASServiceInstance {

    private static final AccessServiceDescription description = AccessServiceDescription.ASSET_CATALOG_OMAS;

    /**
     * Return the handler for assets requests
     * @return handler object
     */
    @Getter
    private AssetCatalogHandler assetCatalogHandler;

    /**
     * Return the handler for relationships requests
     * @return handler object
     */
    @Getter
    private RelationshipHandler relationshipHandler;

    /**
     * @param repositoryConnector     link to the repository responsible for servicing the REST calls.
     * @param supportedZones          configurable list of zones that Asset Catalog is allowed to serve Assets from
     * @param auditLog                logging destination
     * @param serverUserName          userId used for server initiated actions
     * @param supportedTypesForSearch default list of supported types for search method
     * @throws NewInstanceException a problem occurred during initialization
     */
    AssetCatalogServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones,
                                 AuditLog auditLog, String serverUserName, String sourceName,
                                 List<String> supportedTypesForSearch) throws NewInstanceException {

        super(description.getAccessServiceName() + " OMAS", repositoryConnector, auditLog, serverUserName, repositoryConnector.getMaxPageSize());
        super.supportedZones = supportedZones;

        if (repositoryHandler != null) {

            assetCatalogHandler = new AssetCatalogHandler(serverName, sourceName, invalidParameterHandler, repositoryHandler, repositoryHelper,
                    errorHandler, supportedZones, supportedTypesForSearch);
            relationshipHandler = new RelationshipHandler(sourceName, invalidParameterHandler, repositoryHandler, repositoryHelper, errorHandler);
        } else {
            final String methodName = "new ServiceInstance";
            throw new NewInstanceException(AssetCatalogErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(methodName),
                    this.getClass().getName(),
                    methodName);
        }
    }
}
