/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.builders;

import org.odpi.openmetadata.accessservices.assetconsumer.mappers.GlossaryTermMapper;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders.ReferenceableBuilder;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.ReferenceableMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GlossaryTermBuilder is able to build the properties for a GlossaryTerm entity from a GlossaryTerm bean.
 */
public class GlossaryTermBuilder extends ReferenceableBuilder
{
    private String displayName;
    private String description;
    private String summary      = null;
    private String examples     = null;
    private String abbreviation = null;
    private String usage        = null;


    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of glossary term
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GlossaryTermBuilder(String               qualifiedName,
                               String               displayName,
                               OMRSRepositoryHelper repositoryHelper,
                               String               serviceName,
                               String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
    }


    /**
     * Constructor supporting all properties.
     *
     * @param qualifiedName unique name
     * @param displayName new value for the display name.
     * @param summary short description
     * @param description new description for the glossary term.
     * @param examples  example
     * @param abbreviation abbreviation
     * @param usage usage guidance
     * @param additionalProperties additional properties
     * @param extendedProperties  properties from the subtype.
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GlossaryTermBuilder(String                   qualifiedName,
                               String                   displayName,
                               String                   summary,
                               String                   description,
                               String                   examples,
                               String                   abbreviation,
                               String                   usage,
                               Map<String, String>      additionalProperties,
                               Map<String, Object>      extendedProperties,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName,
                               String                   serverName)
    {
        super(qualifiedName,
              additionalProperties,
              extendedProperties,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.summary = summary;
        this.description = description;
        this.examples = examples;
        this.abbreviation = abbreviation;
        this.usage = usage;
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

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (summary != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.SUMMARY_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (examples != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.EXAMPLES_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (abbreviation != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.ABBREVIATION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        if (usage != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.USAGE_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        return properties;
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getQualifiedNameInstanceProperties(String  methodName)
    {
        return super.getNameInstanceProperties(methodName);
    }


    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getNameInstanceProperties(String  methodName)
    {
        InstanceProperties properties = super.getNameInstanceProperties(methodName);

        if (displayName != null)
        {
            String literalName = repositoryHelper.getExactMatchRegex(displayName);

            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      literalName,
                                                                      methodName);
        }

        return properties;
    }

    /**
     * Return the supplied bean properties that represent a name in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getSearchInstanceProperties(String  methodName)
    {
        InstanceProperties properties = null;

        if (qualifiedName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      ReferenceableMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                                                      qualifiedName,
                                                                      methodName);
        }

        if (displayName != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);
        }

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      GlossaryTermMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);
        }

        return properties;
    }
}
