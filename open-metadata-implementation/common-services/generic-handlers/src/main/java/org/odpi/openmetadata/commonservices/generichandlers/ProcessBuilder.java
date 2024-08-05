/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * ProcessBuilder creates the parts of a root repository entity for a process.  The default type of this process
 * is DeployedSoftwareComponent.
 */
public class ProcessBuilder extends AssetBuilder
{
    private final String formula     = null;
    private final String formulaType = null;
    private final String implementationLanguage = null;

    /**
     * Creation constructor used when working with classifications
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProcessBuilder(OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeGUID,
              OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Creation constructor used when working with classifications
     *
     * @param typeGUID unique identifier for the type of this process
     * @param typeName unique name for the type of this process
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProcessBuilder(String               typeGUID,
                   String               typeName,
                   OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(typeGUID,
              typeName,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.FORMULA.name,
                                                                  formula,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.FORMULA_TYPE.name,
                                                                  formulaType,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.IMPLEMENTATION_LANGUAGE.name,
                                                                  implementationLanguage,
                                                                  methodName);

        return properties;
    }
}
