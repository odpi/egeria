/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Map;

/**
 * ProcessBuilder creates the parts of a root repository entity for a process.  The default type of this process
 * is DeployedSoftwareComponent.
 */
public class ProcessBuilder extends AssetBuilder
{
    private String formula                = null;
    private String formulaType            = null;
    private String implementationLanguage = null;

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
        super(OpenMetadataAPIMapper.DEPLOYED_SOFTWARE_COMPONENT_TYPE_GUID,
              OpenMetadataAPIMapper.DEPLOYED_SOFTWARE_COMPONENT_TYPE_NAME,
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
     * Constructor supporting all entity properties. (Classifications are added separately.)
     *
     * @param qualifiedName unique name
     * @param technicalName new value for the name
     * @param versionIdentifier new value for the versionIdentifier
     * @param technicalDescription new description for the process
     * @param formula description of the logic that is implemented by this process
     * @param formulaType description of the language used in the formula
     * @param implementationLanguage language used to implement this process (DeployedSoftwareComponent and subtypes only)
     * @param additionalProperties additional properties
     * @param typeGUID unique identifier for the type of this process
     * @param typeName unique name for the type of this process
     * @param extendedProperties  properties from the subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProcessBuilder(String               qualifiedName,
                   String               technicalName,
                   String               versionIdentifier,
                   String               technicalDescription,
                   String               formula,
                   String               formulaType,
                   String               implementationLanguage,
                   Map<String, String>  additionalProperties,
                   String               typeGUID,
                   String               typeName,
                   Map<String, Object>  extendedProperties,
                   OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(qualifiedName,
              technicalName,
              versionIdentifier,
              technicalDescription,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.formula = formula;
        this.formulaType = formulaType;
        this.implementationLanguage = implementationLanguage;
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
                                                                  OpenMetadataAPIMapper.FORMULA_PROPERTY_NAME,
                                                                  formula,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.FORMULA_TYPE_PROPERTY_NAME,
                                                                  formulaType,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.IMPLEMENTATION_LANGUAGE_PROPERTY_NAME,
                                                                  implementationLanguage,
                                                                  methodName);

        return properties;
    }
}
