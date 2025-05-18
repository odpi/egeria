/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ProcessContainmentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Date;

/**
 * ProcessBuilder creates the parts of a root repository entity for a process.  The default type of this process
 * is DeployedSoftwareComponent.
 */
public class ProcessBuilder extends AssetBuilder
{
    private String formula                    = null;
    private String formulaType                = null;
    private String implementationLanguage     = null;
    private Date   processStartTime           = null;
    private Date   processEndTime             = null;


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
     * Constructor used when working with entities
     *
     * @param typeGUID unique identifier for the type of this process
     * @param typeName unique name for the type of this process
     * @param qualifiedName unique name
     * @param name display name
     * @param description description* @param formula formula
     * @param formulaType formulaType
     * @param implementationLanguage  language
     * @param deployedImplementationType  deployed implementation type
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProcessBuilder(String               typeGUID,
                   String               typeName,
                   String               qualifiedName,
                   String               name,
                   String               description,
                   String               formula,
                   String               formulaType,
                   String               implementationLanguage,
                   String               deployedImplementationType,
                   OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(qualifiedName,
              name,
              null,
              null,
              description,
              deployedImplementationType,
              null,
              typeGUID,
              typeName,
              null,
              InstanceStatus.ACTIVE,
              repositoryHelper,
              serviceName,
              serverName);

        this.formula = formula;
        this.formulaType = formulaType;
        this.implementationLanguage = implementationLanguage;
    }


    /**
     * Constructor used when working with entities
     *
     * @param typeGUID unique identifier for the type of this process
     * @param typeName unique name for the type of this process
     * @param qualifiedName unique name
     * @param name display name
     * @param description description
     * @param formula formula
     * @param formulaType formulaType
     * @param processStartTime  date
     * @param processEndTime  date
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    ProcessBuilder(String               typeGUID,
                   String               typeName,
                   String               qualifiedName,
                   String               name,
                   String               description,
                   String               formula,
                   String               formulaType,
                   Date                 processStartTime,
                   Date                 processEndTime,
                   OMRSRepositoryHelper repositoryHelper,
                   String               serviceName,
                   String               serverName)
    {
        super(qualifiedName,
              name,
              null,
              null,
              description,
              null,
              null,
              typeGUID,
              typeName,
              null,
              InstanceStatus.ACTIVE,
              repositoryHelper,
              serviceName,
              serverName);

        this.formula = formula;
        this.formulaType = formulaType;
        this.processStartTime = processStartTime;
        this.processEndTime = processEndTime;
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
     * Create the properties for the ProcessHierarchy relationship.
     *
     * @param processContainmentOrdinal ordinal for the containmentType property
     * @return instance properties
     * @throws InvalidParameterException enum type not supported (should not happen)
     */
    public InstanceProperties getProcessHierarchyProperties(int processContainmentOrdinal) throws InvalidParameterException
    {
        final String methodName = "getProcessHierarchyProperties";

        try
        {
            return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                              null,
                                                              OpenMetadataProperty.CONTAINMENT_TYPE.name,
                                                              ProcessContainmentType.getOpenTypeGUID(),
                                                              ProcessContainmentType.getOpenTypeName(),
                                                              processContainmentOrdinal,
                                                              methodName);
        }
        catch (TypeErrorException classificationNotSupported)
        {
            throw new InvalidParameterException(classificationNotSupported, OpenMetadataProperty.KEY_PATTERN.name);
        }
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

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.PROCESS_START_TIME.name,
                                                                processStartTime,
                                                                methodName);

        properties = repositoryHelper.addDatePropertyToInstance(serviceName,
                                                                properties,
                                                                OpenMetadataProperty.PROCESS_END_TIME.name,
                                                                processEndTime,
                                                                methodName);

        return properties;
    }
}
