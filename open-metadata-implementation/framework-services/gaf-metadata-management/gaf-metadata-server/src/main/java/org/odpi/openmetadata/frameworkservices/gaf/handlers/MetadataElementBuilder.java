/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.handlers;


import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Date;
import java.util.Map;

/**
 * MetadataElementBuilder is able to build the properties for an entity that extends OpenMetadataRoot.  It works with property maps for the
 * properties and relies on the validation in the repository services to ensure that only valid properties are stored.
 */
public class MetadataElementBuilder extends OpenMetadataAPIGenericBuilder
{
    private Map<String, InstancePropertyValue> propertyMap = null;

    /**
     * Constructor.
     *
     * @param metadataElementTypeGUID type identifier
     * @param metadataElementTypeName type name
     * @param propertyMap map of property names to values
     * @param initialStatus initial status of the element
     * @param effectiveFrom date to make the element active in the governance program (null for now)
     * @param effectiveTo date to remove the element from the governance program (null = until deleted)
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    MetadataElementBuilder(String                             metadataElementTypeGUID,
                           String                             metadataElementTypeName,
                           Map<String, InstancePropertyValue> propertyMap,
                           InstanceStatus                     initialStatus,
                           Date                               effectiveFrom,
                           Date                               effectiveTo,
                           OMRSRepositoryHelper               repositoryHelper,
                           String                             serviceName,
                           String                             serverName)
    {
        super(metadataElementTypeGUID,
              metadataElementTypeName,
              null,
              initialStatus,
              effectiveFrom,
              effectiveTo,
              null,
              repositoryHelper,
              serviceName,
              serverName);

       this.propertyMap = propertyMap;
    }


    /**
     * Constructor.
     *
     * @param propertyMap map of property names and values
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    MetadataElementBuilder(Map<String, InstancePropertyValue> propertyMap,
                           OMRSRepositoryHelper               repositoryHelper,
                           String                             serviceName,
                           String                             serverName)
    {
        super(OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.propertyMap = propertyMap;
    }


    /**
     * Constructor.
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    MetadataElementBuilder(OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_GUID,
              OpenMetadataAPIMapper.OPEN_METADATA_ROOT_TYPE_NAME,
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

        return super.updateInstanceProperties(properties, propertyMap);
    }
}
