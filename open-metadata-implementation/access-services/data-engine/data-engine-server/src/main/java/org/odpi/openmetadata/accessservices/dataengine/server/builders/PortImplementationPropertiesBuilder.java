package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

public class PortImplementationPropertiesBuilder extends PortPropertiesBuilder {

    private PortType portType;

    /**
     * Constructor when only the qualified name is known.
     *
     * @param qualifiedName    unique name
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    public PortImplementationPropertiesBuilder(String qualifiedName, OMRSRepositoryHelper repositoryHelper,
                                               String serviceName,
                                               String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
    }

    /**
     * Constructor when all the properties are known.
     *
     * @param qualifiedName    unique name
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    public PortImplementationPropertiesBuilder(String qualifiedName, String displayName, PortType portType,
                                               OMRSRepositoryHelper repositoryHelper, String serviceName,
                                               String serverName) {
        super(qualifiedName, displayName, repositoryHelper, serviceName, serverName);
        this.portType = portType;
    }

    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (portType != null) {

            properties = this.addPortTypeProperty(portType, properties);
        }
        return properties;
    }

    private InstanceProperties addPortTypeProperty(PortType portType, InstanceProperties properties) {
        return repositoryHelper.addEnumPropertyToInstance(serviceName, properties, PortPropertiesMapper.PORT_TYPE,
                portType.getOrdinal(), portType.getName(), portType.getDescription(), "addPortTypeProperty");
    }
}
