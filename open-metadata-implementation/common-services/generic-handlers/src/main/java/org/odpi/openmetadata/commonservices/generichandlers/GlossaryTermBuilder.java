/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.Map;

/**
 * GlossaryTermBuilder is able to build the properties for a GlossaryTerm entity from a GlossaryTerm bean.
 */
public class GlossaryTermBuilder extends ReferenceableBuilder
{
    private String displayName  = null;
    private String description  = null;
    private String summary      = null;
    private String examples     = null;
    private String abbreviation = null;
    private String usage        = null;


    /**
     * Constructor for when creating relationships and classifications.
     *
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryTermBuilder(OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
              OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
              repositoryHelper,
              serviceName,
              serverName);
    }


    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of glossary term
     * @param description new description for the glossary term.
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryTermBuilder(String               qualifiedName,
                        String               displayName,
                        String               description,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
    }


    /**
     * Constructor when basic properties are known.
     *
     * @param qualifiedName unique name
     * @param repositoryHelper helper methods
     * @param displayName display name of glossary term
     * @param description new description for the glossary term.
     * @param summary short description
     * @param examples  example
     * @param abbreviation abbreviation
     * @param usage usage guidance
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public GlossaryTermBuilder(String        qualifiedName,
                               String               displayName,
                               String               description,
                               String               summary,
                               String               examples,
                               String               abbreviation,
                               String               usage,
                               OMRSRepositoryHelper repositoryHelper,
                               String               serviceName,
                               String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
        this.summary = summary;
        this.examples = examples;
        this.abbreviation = abbreviation;
        this.usage = usage;
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
     * @param initialStatus glossary term status to use when the object is created
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
                               InstanceStatus           initialStatus,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName,
                               String                   serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_GUID,
              OpenMetadataAPIMapper.GLOSSARY_TERM_TYPE_NAME,
              extendedProperties,
              initialStatus,
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
    @Override
    public InstanceProperties getInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
                                                                      displayName,
                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.SUMMARY_PROPERTY_NAME,
                                                                      summary,
                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.EXAMPLES_PROPERTY_NAME,
                                                                      examples,
                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.ABBREVIATION_PROPERTY_NAME,
                                                                      abbreviation,
                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.USAGE_PROPERTY_NAME,
                                                                      usage,
                                                                      methodName);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param activityType ordinal for type of activity
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getActivityTypeProperties(int    activityType,
                                                 String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    null,
                                                                    OpenMetadataAPIMapper.ACTIVITY_TYPE_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.ACTIVITY_TYPE_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.ACTIVITY_TYPE_ENUM_TYPE_NAME,
                                                                    activityType,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.ACTIVITY_TYPE_PROPERTY_NAME);
        }

        setEffectivityDates(properties);

        return properties;
    }



    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param description description of the context
     * @param scope the scope of where the context applies
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    InstanceProperties getContextDescriptionProperties(String description,
                                                       String scope,
                                                       String methodName)
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      null,
                                                                      OpenMetadataAPIMapper.SCOPE_PROPERTY_NAME,
                                                                      scope,
                                                                      methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                      description,
                                                                      methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param description description of the relationship
     * @param relationshipStatus ordinal for the relationship status enum (draft, active, deprecated, obsolete, other)
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getTermCategorizationProperties(String description,
                                                       int    relationshipStatus,
                                                       String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME,
                                                                    relationshipStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.ACTIVITY_TYPE_PROPERTY_NAME);
        }

        setEffectivityDates(properties);

        return properties;
    }

    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param description description of the relationship
     * @param expression expression that describes the relationship
     * @param relationshipStatus ordinal for the relationship status enum (draft, active, deprecated, obsolete, other)
     * @param steward user id or name of steward id who assigned the relationship (or approved the discovered value).
     * @param source id of the source of the knowledge of the relationship
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getTermRelationshipProperties(String expression,
                                                     String description,
                                                     int    relationshipStatus,
                                                     String steward,
                                                     String source,
                                                     String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME,
                                                                  expression,
                                                                  methodName);


        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME,
                                                                  description,
                                                                  methodName);


        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataAPIMapper.STATUS_PROPERTY_NAME,
                                                                    OpenMetadataAPIMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_GUID,
                                                                    OpenMetadataAPIMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME,
                                                                    relationshipStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataAPIMapper.ACTIVITY_TYPE_PROPERTY_NAME);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME,
                                                                  steward,
                                                                  methodName);


        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME,
                                                                  source,
                                                                  methodName);


        setEffectivityDates(properties);

        return properties;
    }
}
