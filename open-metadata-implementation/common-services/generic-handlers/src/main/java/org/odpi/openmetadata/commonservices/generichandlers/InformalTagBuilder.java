/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * InformalTagBuilder is able to build the properties for an InformalTag entity.
 */
public class InformalTagBuilder extends OpenMetadataAPIGenericBuilder
{
    private final String  tagName;
    private final String  tagDescription;
    private final boolean isPublic;


    /**
     * Constructor.
     *
     * @param tagName name of the tag
     * @param tagDescription description of the meaning of the tag
     * @param isPublic should this feedback be shareable?
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    InformalTagBuilder(String               tagName,
                       String               tagDescription,
                       boolean              isPublic,
                       OMRSRepositoryHelper repositoryHelper,
                       String               serviceName,
                       String               serverName)
    {
        super(OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_GUID,
              OpenMetadataAPIMapper.INFORMAL_TAG_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.tagName        = tagName;
        this.tagDescription = tagDescription;
        this.isPublic       = isPublic;
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
                                                                  OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME,
                                                                  tagName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                  tagDescription,
                                                                  methodName);

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.TAG_IS_PUBLIC_PROPERTY_NAME,
                                                                   isPublic,
                                                                   methodName);

        return properties;
    }
}
