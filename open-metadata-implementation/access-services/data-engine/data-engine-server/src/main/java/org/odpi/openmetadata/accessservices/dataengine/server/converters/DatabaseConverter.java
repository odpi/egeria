package org.odpi.openmetadata.accessservices.dataengine.server.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericConverter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class DatabaseConverter<B> extends OpenMetadataAPIGenericConverter<B> {
    public DatabaseConverter(OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(repositoryHelper, serviceName, serverName);
    }
}
