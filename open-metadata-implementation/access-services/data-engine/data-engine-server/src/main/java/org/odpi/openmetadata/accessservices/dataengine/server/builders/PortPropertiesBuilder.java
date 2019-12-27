/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.builders;

import org.odpi.openmetadata.accessservices.dataengine.model.PortType;
import org.odpi.openmetadata.accessservices.dataengine.server.mappers.PortPropertiesMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * PortPropertiesBuilder is able to build the properties for an Port entity.
 */
public class PortPropertiesBuilder extends ReferenceableBuilder {

    private final String displayName;
    private final PortType portType;

    /**
     * Constructor
     *
     * @param qualifiedName    qualified name
     * @param displayName      display name
     * @param portType         port type
     * @param repositoryHelper helper methods
     * @param serviceName      name of this OMAS
     * @param serverName       name of local server
     */
    public PortPropertiesBuilder(String qualifiedName, String displayName, PortType portType,
                                 OMRSRepositoryHelper repositoryHelper, String serviceName, String serverName) {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
        this.displayName = displayName;
        this.portType = portType;
    }

    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     *
     * @return InstanceProperties object
     *
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String methodName) throws InvalidParameterException {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (displayName != null) {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties,
                    PortPropertiesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        }

        if (portType != null) {
            properties = this.addPortTypeProperty(portType, properties);
        }

        return properties;
    }

    private InstanceProperties addPortTypeProperty(PortType portType, InstanceProperties properties) {
        return repositoryHelper.addEnumPropertyToInstance(serviceName, properties, PortPropertiesMapper.PORT_TYPE_PROPERTY_NAME,
                portType.getOrdinal(), portType.getName(), portType.getDescription(), "addPortTypeProperty");
    }
}
