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
    private String  tagName;
    private String  tagDescription = null;
    private boolean isPublic       = false;
    private String  user;

    /**
     * Constructor used for constructing search arguments on private tags.
     *
     * @param tagName name of the tag
     * @param user user id of the calling user
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public InformalTagBuilder(String               tagName,
                              String               user,
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
        this.user           = user;
    }


    /**
     * Constructor used for constructing search arguments.
     *
     * @param tagName name of the tag
     * @param tagDescription description of the meaning of the tag
     * @param user user id of the calling user
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public InformalTagBuilder(String               tagName,
                              String               tagDescription,
                              String               user,
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
        this.user           = user;
    }


    /**
     * Constructor.
     *
     * @param tagName name of the tag
     * @param tagDescription description of the meaning of the tag
     * @param isPublic should this feedback be shareable?
     * @param user user id of the calling user
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public InformalTagBuilder(String               tagName,
                              String               tagDescription,
                              boolean              isPublic,
                              String               user,
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
        this.user           = user;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getNameInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (tagName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(tagName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME,
                                                                      literalName,
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
    public InstanceProperties getUserNameInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (tagName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(tagName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        if (user != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_USER_PROPERTY_NAME,
                                                                      user,
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
    public InstanceProperties getSearchInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (tagName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME,
                                                                      tagName,
                                                                      methodName);
        }

        if (tagDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                      tagDescription,
                                                                      methodName);
        }

        if (user != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.USER_PROPERTY_NAME,
                                                                      user,
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
    public InstanceProperties getUserSearchInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (tagName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME,
                                                                      tagName,
                                                                      methodName);
        }

        if (tagDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                      tagDescription,
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
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (tagName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_NAME_PROPERTY_NAME,
                                                                      tagName,
                                                                      methodName);
        }

        if (tagDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                      tagDescription,
                                                                      methodName);
        }

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   OpenMetadataAPIMapper.TAG_IS_PUBLIC_PROPERTY_NAME,
                                                                   isPublic,
                                                                   methodName);

        return properties;
    }
}
