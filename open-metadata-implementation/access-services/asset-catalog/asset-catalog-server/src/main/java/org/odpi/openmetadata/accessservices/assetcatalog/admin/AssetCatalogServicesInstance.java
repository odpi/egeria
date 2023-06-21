/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.admin;

import lombok.Getter;
import org.odpi.openmetadata.accessservices.assetcatalog.connectors.outtopic.AssetCatalogOutTopicClientProvider;
import org.odpi.openmetadata.accessservices.assetcatalog.converters.AssetCatalogConverter;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.RelationshipHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.service.ClockService;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.commonservices.multitenant.OMASServiceInstance;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;

import java.time.Clock;
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
    private final AssetCatalogHandler assetCatalogHandler;

    /**
     * Return the handler for relationships requests
     * @return handler object
     */
    @Getter
    private final RelationshipHandler relationshipHandler;

    /**
     * Instantiates an AssetCatalogServicesInstance object
     * @param repositoryConnector     link to the repository responsible for servicing the REST calls.
     * @param supportedZones          configurable list of zones that Asset Catalog is allowed to serve Assets from
     * @param auditLog                logging destination
     * @param serverUserName          userId used for server initiated actions
     * @param supportedTypesForSearch default list of supported types for search method
     * @throws NewInstanceException a problem occurred during initialization
     */
    AssetCatalogServicesInstance(OMRSRepositoryConnector repositoryConnector, List<String> supportedZones,
                                 AuditLog auditLog, String serverUserName, String sourceName,
                                 List<String> supportedTypesForSearch,
                                 Connection inTopicEventBusConnection,
                                 Connection outTopicEventBusConnection) throws NewInstanceException {

        super(description.getAccessServiceName() + " OMAS",
                repositoryConnector,
                supportedZones,
                null,
                null,
                auditLog,
                serverUserName,
                repositoryConnector.getMaxPageSize(),
                null,
                null,
                AssetCatalogOutTopicClientProvider.class.getName(),
                outTopicEventBusConnection);

        super.supportedZones = supportedZones;

        if (repositoryHandler != null) {
            AssetCatalogConverter<AssetCatalogBean> assetCatalogConverter =
                    new AssetCatalogConverter<>(repositoryHelper, serviceName, serverName);

            OpenMetadataAPIGenericHandler<AssetCatalogBean> assetHandler =
                    new OpenMetadataAPIGenericHandler<>(new AssetCatalogConverter<>(repositoryHelper, serviceName, serverName),
                            AssetCatalogBean.class, serviceName, serverName, invalidParameterHandler, repositoryHandler,
                            repositoryHelper, localServerUserId, securityVerifier, supportedZones, defaultZones, publishZones,
                            auditLog);
            ClockService clockService = new ClockService(Clock.systemUTC());

            assetCatalogHandler = new AssetCatalogHandler(serverName, sourceName, invalidParameterHandler,
                    repositoryHandler, repositoryHelper, assetHandler, assetCatalogConverter,  errorHandler,
                    supportedZones, supportedTypesForSearch, clockService);

            relationshipHandler = new RelationshipHandler(sourceName, invalidParameterHandler, repositoryHandler,
                    repositoryHelper, assetHandler, errorHandler, clockService);
        } else {
            final String methodName = "new ServiceInstance";
            throw new NewInstanceException(AssetCatalogErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(serverName),
                    this.getClass().getName(),
                    methodName);
        }
    }
}
