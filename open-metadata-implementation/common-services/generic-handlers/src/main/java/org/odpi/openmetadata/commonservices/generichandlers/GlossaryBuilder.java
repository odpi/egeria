/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Map;

/**
 * GlossaryBuilder creates the parts for an entity that represents a glossary.
 */
public class GlossaryBuilder extends ReferenceableBuilder
{
    private String displayName = null;
    private String description = null;
    private String language    = null;
    private String usage       = null;


    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the glossary
     * @param displayName short display name for the glossary
     * @param description description of the glossary
     * @param language the language used in the definitions with in the glossary
     * @param usage intended usage of the glossary
     * @param additionalProperties additional properties for a glossary
     * @param typeGUID unique identifier of this element's type
     * @param typeName unique name of this element's type
     * @param extendedProperties  properties for a glossary subtype
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryBuilder(String               qualifiedName,
                    String               displayName,
                    String               description,
                    String               language,
                    String               usage,
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

        this.displayName = displayName;
        this.description = description;
        this.language = language;
        this.usage = usage;
    }


    /**
     * Create constructor - when templating
     *
     * @param qualifiedName unique name for the glossary
     * @param displayName short display name for the glossary
     * @param description description of the glossary
     * @param language the language used in the definitions with in the glossary
     * @param usage intended usage of the glossary
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GlossaryBuilder(String        qualifiedName,
                           String               displayName,
                           String               description,
                           String               language,
                           String               usage,
                           OMRSRepositoryHelper repositoryHelper,
                           String               serviceName,
                           String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataType.GLOSSARY.typeGUID,
              OpenMetadataType.GLOSSARY.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
        this.language = language;
        this.usage = usage;
    }
    /**
     * Create constructor
     *
     * @param qualifiedName unique name for the glossary
     * @param displayName short display name for the glossary
     * @param description description of the glossary
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryBuilder(String               qualifiedName,
                    String               displayName,
                    String               description,
                    OMRSRepositoryHelper repositoryHelper,
                    String               serviceName,
                    String               serverName)
    {
        super(qualifiedName,
              null,
              OpenMetadataType.GLOSSARY.typeGUID,
              OpenMetadataType.GLOSSARY.typeName,
              null,
              repositoryHelper,
              serviceName,
              serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Classification constructor
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryBuilder(OMRSRepositoryHelper repositoryHelper,
                    String               serviceName,
                    String               serverName)
    {
        super(OpenMetadataType.GLOSSARY.typeGUID,
              OpenMetadataType.GLOSSARY.typeName,
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

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                  displayName,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.LANGUAGE.name,
                                                                  language,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.USAGE.name,
                                                                  usage,
                                                                  methodName);

        return properties;
    }


    /**
     * Return the bean properties describing an editing glossary in an InstanceProperties object.
     *
     * @param description the purpose of the glossary
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getEditingGlossaryProperties(String description,
                                                    String methodName)
    {
        InstanceProperties properties = null;

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                                      description,
                                                                      methodName);

        }

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a staging glossary in an InstanceProperties object.
     *
     * @param description the purpose of the glossary
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getStagingGlossaryProperties(String description,
                                                    String methodName)
    {
        InstanceProperties properties = null;

        if (description != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataProperty.DESCRIPTION.name,
                                                                      description,
                                                                      methodName);

        }

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing a taxonomy in an InstanceProperties object.
     *
     * @param organizingPrinciple the factor used to organize the category hierarchy that forms the taxonomy
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getTaxonomyProperties(String organizingPrinciple,
                                             String methodName)
    {
        InstanceProperties properties = null;

        if (organizingPrinciple != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataProperty.ORGANIZING_PRINCIPLE.name,
                                                                      organizingPrinciple,
                                                                      methodName);

        }

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the bean properties describing the breadth of a glossary's applicability in an InstanceProperties object.
     *
     * @param scope the breadth of coverage
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getCanonicalVocabularyProperties(String scope,
                                                        String methodName)
    {
        InstanceProperties properties = null;

        if (scope != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataProperty.SCOPE.name,
                                                                      scope,
                                                                      methodName);

        }

        setEffectivityDates(properties);

        return properties;
    }
}
