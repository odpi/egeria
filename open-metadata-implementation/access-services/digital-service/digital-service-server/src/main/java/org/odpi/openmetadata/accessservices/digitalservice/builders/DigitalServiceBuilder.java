/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalservice.builders;

import org.odpi.openmetadata.accessservices.digitalservice.mappers.DigitalServiceMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Collections;


/**
 * FileSystemBuilder creates the parts for an entity that represents a file system definition.
 */
public class DigitalServiceBuilder extends ReferenceableBuilder
{
    private String displayName = null;
    private String description = null;
    private String versionNumber = null;

    /**
     * Minimal constructor used for searching
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DigitalServiceBuilder(String               qualifiedName,
                                 OMRSRepositoryHelper repositoryHelper,
                                 String               serviceName,
                                 String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);
    }


    /**
     * Create constructor
     *
     * @param displayName short name of the digital service
     * @param description description of the digital service
     * @param versionNumber version of the digital service
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public DigitalServiceBuilder(String                                 displayName,
                                 String                                 description,
                                 String                                 versionNumber,
                                 OMRSRepositoryHelper                   repositoryHelper,
                                 String                                 serviceName,
                                 String                                 serverName)
    {
        super("", Collections.emptyMap(), Collections.emptyMap(), repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
        this.versionNumber = versionNumber;
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

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);

        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      DigitalServiceMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);

        }

        if (versionNumber != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                        properties,
                                                                        DigitalServiceMapper.DIGITAL_SERVICE_VERSION_NUMER,
                                                                        versionNumber,
                                                                        methodName);
        }


        return properties;
    }



    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getClassificationInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = null;

        return properties;
    }
}
