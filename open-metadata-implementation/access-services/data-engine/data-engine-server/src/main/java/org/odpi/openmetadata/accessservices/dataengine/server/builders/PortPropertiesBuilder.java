package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class PortPropertiesBuilder extends ReferenceableBuilder {

    private String displayName;

    PortPropertiesBuilder(String qualifiedName, OMRSRepositoryHelper repositoryHelper, String serviceName,
                          String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
    }

    public PortPropertiesBuilder(String qualifiedName, String displayName, OMRSRepositoryHelper repositoryHelper,
                                 String serviceName, String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
        this.displayName = displayName;
    }

    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    PortPropertiesMapper.DISPLAY_NAME, displayName, methodName);
        }

        return properties;
    }
}
