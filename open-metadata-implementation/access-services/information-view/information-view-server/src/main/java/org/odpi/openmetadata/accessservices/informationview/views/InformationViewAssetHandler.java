/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.views;

import org.odpi.openmetadata.accessservices.informationview.utils.QualifiedNameUtils;
import org.odpi.openmetadata.accessservices.informationview.views.beans.InformationViewAsset;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class InformationViewAssetHandler implements Callable<InformationViewAsset> {

    private InformationViewEvent event;
    private OMEntityDao omEntityDao;

    public InformationViewAssetHandler(InformationViewEvent event, OMEntityDao omEntityDao) {
        this.event = event;
        this.omEntityDao = omEntityDao;
    }


    public InformationViewAsset call() throws TypeErrorException, InvalidParameterException,
                                              StatusNotSupportedException, PropertyErrorException,
                                              EntityNotKnownException, FunctionNotSupportedException,
                                              PagingErrorException, ClassificationErrorException,
                                              UserNotAuthorizedException, RepositoryErrorException {

        String qualifiedNameForRelationalDbSchemaType = QualifiedNameUtils.buildQualifiedNameForRelationalDbSchemaType(event.getTableSource().getNetworkAddress().split(":")[0], event.getTableSource().getDatabaseName(), event.getTableSource().getSchemaName());
        EntityDetail relationalDbSchemaType = omEntityDao.getEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE, qualifiedNameForRelationalDbSchemaType, false);

        if (relationalDbSchemaType == null) {
            return createInformationView();
        }
        InformationViewAsset informationViewAsset = new InformationViewAsset();
        informationViewAsset.setRelationalDbSchemaType(relationalDbSchemaType);
        return informationViewAsset;

    }

    private InformationViewAsset createInformationView() throws TypeErrorException,
                                                                InvalidParameterException,
                                                                StatusNotSupportedException,
                                                                PropertyErrorException,
                                                                EntityNotKnownException,
                                                                FunctionNotSupportedException,
                                                                PagingErrorException,
                                                                ClassificationErrorException,
                                                                UserNotAuthorizedException,
                                                                RepositoryErrorException {
        InformationViewAsset informationViewAsset = new InformationViewAsset();
        String qualifiedNameForSoftwareServer = QualifiedNameUtils.buildQualifiedName("", Constants.SOFTWARE_SERVER, event.getTableSource().getNetworkAddress().split(":")[0]);
        InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSoftwareServer)
                .withStringProperty(Constants.NAME, qualifiedNameForSoftwareServer)
                .build();
        List<Classification> classificationList = new ArrayList();
        classificationList.add(omEntityDao.buildClassification(Constants.DATABASE_SERVER, Constants.SOFTWARE_SERVER, new InstanceProperties()));
        EntityDetail softwareServerEntity = omEntityDao.addEntity(Constants.SOFTWARE_SERVER,
                qualifiedNameForSoftwareServer, softwareServerProperties, classificationList, true);
        informationViewAsset.setSoftwareServerEntity(softwareServerEntity);

        String qualifiedNameForEndpoint = QualifiedNameUtils.buildQualifiedName("", Constants.ENDPOINT, event.getTableSource().getProtocol() + event.getTableSource().getNetworkAddress()) ;
        InstanceProperties endpointProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NETWORK_ADDRESS, event.getTableSource().getNetworkAddress() )
                .withStringProperty(Constants.PROTOCOL, event.getTableSource().getProtocol())
                .build();
        EntityDetail endpointEntity = omEntityDao.addEntity(Constants.ENDPOINT,
                                                            qualifiedNameForEndpoint,
                                                            endpointProperties,
                                                            false);
        informationViewAsset.setEndpointProperties(endpointEntity);

        omEntityDao.addRelationship(Constants.SERVER_ENDPOINT,
                                    softwareServerEntity.getGUID(),
                                    endpointEntity.getGUID(),
                                    new InstanceProperties());

        String qualifiedNameForConnection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForEndpoint, Constants.CONNECTION, event.getTableSource().getUser());
        InstanceProperties connectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnection)
                .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                .build();
        EntityDetail connectionEntity = omEntityDao.addEntity(Constants.CONNECTION,
                qualifiedNameForConnection, connectionProperties, false);
        informationViewAsset.setConnectionEntity(connectionEntity);

        omEntityDao.addRelationship(Constants.CONNECTION_TO_ENDPOINT,
                endpointEntity.getGUID(),
                connectionEntity.getGUID(),
                new InstanceProperties());


        String qualifiedNameForConnectorType = QualifiedNameUtils.buildQualifiedName("", Constants.CONNECTION_CONNECTOR_TYPE, event.getTableSource().getConnectorProviderName());
        InstanceProperties connectorTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnectorType)
                .withStringProperty(Constants.CONNECTOR_PROVIDER_CLASSNAME, event.getTableSource().getConnectorProviderName())
                .build();
        EntityDetail connectorTypeEntity = omEntityDao.addEntity(Constants.CONNECTOR_TYPE,
                                                                    qualifiedNameForConnectorType, connectorTypeProperties, false);
        informationViewAsset.setConnectorTypeEntity(connectorTypeEntity);

        omEntityDao.addRelationship(Constants.CONNECTION_CONNECTOR_TYPE,
                connectionEntity.getGUID(),
                connectorTypeEntity.getGUID(),
                new InstanceProperties());

        String qualifiedNameForDataStore =  QualifiedNameUtils.buildQualifiedName(qualifiedNameForSoftwareServer, Constants.DATA_STORE, event.getTableSource().getDatabaseName());
        InstanceProperties dataStoreProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataStore)
                .withStringProperty(Constants.NAME, event.getTableSource().getDatabaseName())
                .build();
        EntityDetail dataStore = omEntityDao.addEntity(Constants.DATA_STORE, qualifiedNameForDataStore, dataStoreProperties, true);
        informationViewAsset.setDataStore(dataStore);

        omEntityDao.addRelationship(Constants.CONNECTION_TO_ASSET,
                connectionEntity.getGUID(),
                dataStore.getGUID(),
                new InstanceProperties());

        String qualifiedNameForInformationView = QualifiedNameUtils.buildQualifiedName(qualifiedNameForDataStore, Constants.INFORMATION_VIEW, event.getTableSource().getSchemaName());
        InstanceProperties ivProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForInformationView)
                .withStringProperty(Constants.NAME, event.getTableSource().getSchemaName())
                .withStringProperty(Constants.OWNER, "")
                .withStringProperty(Constants.DESCRIPTION, "This asset is an " + "information " + "view")
                .build();
        EntityDetail informationViewEntity = omEntityDao.addEntity(Constants.INFORMATION_VIEW,
                qualifiedNameForInformationView, ivProperties, true);
        informationViewAsset.setInformationViewEntity(informationViewEntity);

        omEntityDao.addRelationship(Constants.DATA_CONTENT_FOR_DATASET,
                dataStore.getGUID(),
                informationViewEntity.getGUID(),
                new InstanceProperties());

        String qualifiedNameForDbSchemaType = QualifiedNameUtils.buildQualifiedName(qualifiedNameForDataStore, Constants.RELATIONAL_DB_SCHEMA_TYPE, event.getTableSource().getSchemaName() + Constants.TYPE_SUFFIX);
        InstanceProperties dbSchemaTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDbSchemaType)
                .withStringProperty(Constants.DISPLAY_NAME, event.getTableSource().getSchemaName() + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "").build();
        EntityDetail relationalDbSchemaType = omEntityDao.addEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE,
                qualifiedNameForDbSchemaType,
                dbSchemaTypeProperties,
                false);
        informationViewAsset.setRelationalDbSchemaType(relationalDbSchemaType);


        omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                informationViewEntity.getGUID(),
                relationalDbSchemaType.getGUID(),
                new InstanceProperties());
        return informationViewAsset;
    }


}
