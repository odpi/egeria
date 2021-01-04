/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * SearchKeywordBuilder is able to build the properties for a SearchKeyword entity.
 */
public class SearchKeywordBuilder extends OpenMetadataAPIGenericBuilder
{
    private String keyword;
    private String description;


    /**
     * Constructor used for constructing search arguments.
     *
     * @param keyword name of the keyword
     * @param description description of the meaning of the keyword
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    SearchKeywordBuilder(String               keyword,
                         String               description,
                         OMRSRepositoryHelper repositoryHelper,
                         String               serviceName,
                         String               serverName)
    {
        super(OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_GUID,
              OpenMetadataAPIMapper.SEARCH_KEYWORD_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);

        this.keyword     = keyword;
        this.description = description;
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

        if (keyword != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.KEYWORD_PROPERTY_NAME,
                                                                      keyword,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.KEYWORD_DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        return properties;
    }
}
