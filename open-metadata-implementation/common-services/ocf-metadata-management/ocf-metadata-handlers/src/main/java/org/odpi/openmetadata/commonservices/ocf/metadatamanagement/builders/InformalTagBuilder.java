/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;

import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.InformalTagMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;


/**
 * RatingBuilder is able to build the properties for a Rating entity.
 */
public class InformalTagBuilder extends RootBuilder
{
    private String  tagName;
    private String  tagDescription;
    private boolean isPublic;
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
        super(repositoryHelper, serviceName, serverName);

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
        super(repositoryHelper, serviceName, serverName);

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
        super(repositoryHelper, serviceName, serverName);

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
                                                                      InformalTagMapper.TAG_NAME_PROPERTY_NAME,
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
                                                                      InformalTagMapper.TAG_NAME_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        if (user != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      InformalTagMapper.USER_PROPERTY_NAME,
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
                                                                      InformalTagMapper.TAG_NAME_PROPERTY_NAME,
                                                                      tagName,
                                                                      methodName);
        }

        if (tagDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      InformalTagMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                      tagDescription,
                                                                      methodName);
        }

        if (user != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      InformalTagMapper.USER_PROPERTY_NAME,
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
                                                                      InformalTagMapper.TAG_NAME_PROPERTY_NAME,
                                                                      tagName,
                                                                      methodName);
        }

        if (tagDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      InformalTagMapper.TAG_DESCRIPTION_PROPERTY_NAME,
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
                                                                      InformalTagMapper.TAG_NAME_PROPERTY_NAME,
                                                                      tagName,
                                                                      methodName);
        }

        if (tagDescription != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      InformalTagMapper.TAG_DESCRIPTION_PROPERTY_NAME,
                                                                      tagDescription,
                                                                      methodName);
        }

        properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                   properties,
                                                                   InformalTagMapper.IS_PUBLIC_PROPERTY_NAME,
                                                                   isPublic,
                                                                   methodName);

        return properties;
    }
}
