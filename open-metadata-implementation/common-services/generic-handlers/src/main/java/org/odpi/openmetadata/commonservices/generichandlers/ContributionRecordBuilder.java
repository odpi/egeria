/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Map;


/**
 * ContributionRecordBuilder is able to build the properties for a ContributionRecord entity.
 */
public class ContributionRecordBuilder extends ReferenceableBuilder
{
    private final long karmaPoints;
    private final boolean isPublic;


    /**
     * Constructor.
     *
     * @param qualifiedName unique name for the role
     * @param karmaPoints number of karma points
     * @param isPublic  can this information be shared with colleagues
     * @param additionalProperties additional properties for a role
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a role subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public ContributionRecordBuilder(String               qualifiedName,
                                     long                 karmaPoints,
                                     boolean              isPublic,
                                     Map<String, String>  additionalProperties,
                                     String               typeGUID,
                                     String               typeName,
                                     Map<String, Object>  extendedProperties,
                                     OMRSRepositoryHelper repositoryHelper,
                                     String               serviceName,
                                     String               serverName)
    {
        super(qualifiedName,
              additionalProperties,
              typeGUID,
              typeName,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.karmaPoints = karmaPoints;
        this.isPublic = isPublic;
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

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                   isPublic,
                                                                   methodName);

        return repositoryHelper.addLongPropertyToInstance(serviceName,
                                                          properties,
                                                          OpenMetadataAPIMapper.KARMA_POINTS_PROPERTY_NAME,
                                                          karmaPoints,
                                                          methodName);
    }
}
