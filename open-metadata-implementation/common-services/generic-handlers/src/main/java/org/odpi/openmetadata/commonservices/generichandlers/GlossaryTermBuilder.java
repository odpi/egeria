/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermActivityType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GlossaryTermRelationshipStatus;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
    private String displayName              = null;
    private String description              = null;
    private String summary                  = null;
    private String examples                 = null;
    private String abbreviation             = null;
    private String usage                    = null;
    private String publishVersionIdentifier = null;


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
        super(OpenMetadataType.GLOSSARY_TERM.typeGUID,
              OpenMetadataType.GLOSSARY_TERM.typeName,
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
     * @param publishVersionIdentifier author controlled identifier
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    GlossaryTermBuilder(String               qualifiedName,
                        String               displayName,
                        String               description,
                        String               publishVersionIdentifier,
                        OMRSRepositoryHelper repositoryHelper,
                        String               serviceName,
                        String               serverName)
    {
        super(qualifiedName, repositoryHelper, serviceName, serverName);

        this.displayName = displayName;
        this.description = description;
        this.publishVersionIdentifier = publishVersionIdentifier;
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
    public GlossaryTermBuilder(String               qualifiedName,
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
     * @param publishVersionIdentifier user controlled version identifier
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
                               String                   publishVersionIdentifier,
                               Map<String, String>      additionalProperties,
                               Map<String, Object>      extendedProperties,
                               InstanceStatus           initialStatus,
                               OMRSRepositoryHelper     repositoryHelper,
                               String                   serviceName,
                               String                   serverName)
    {
        super(qualifiedName,
              additionalProperties,
              OpenMetadataType.GLOSSARY_TERM.typeGUID,
              OpenMetadataType.GLOSSARY_TERM.typeName,
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
        this.publishVersionIdentifier = publishVersionIdentifier;
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
                                                                  OpenMetadataProperty.SUMMARY.name,
                                                                  summary,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.EXAMPLES.name,
                                                                  examples,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.ABBREVIATION.name,
                                                                  abbreviation,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.USAGE.name,
                                                                  usage,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.PUBLISH_VERSION_ID.name,
                                                                  publishVersionIdentifier,
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
                                                                    OpenMetadataProperty.ACTIVITY_TYPE.name,
                                                                    GlossaryTermActivityType.getOpenTypeGUID(),
                                                                    GlossaryTermActivityType.getOpenTypeName(),
                                                                    activityType,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.ACTIVITY_TYPE.name);
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
                                                                  OpenMetadataProperty.SCOPE.name,
                                                                  scope,
                                                                  methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
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
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                                                    GlossaryTermRelationshipStatus.getOpenTypeGUID(),
                                                                    GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                                    relationshipStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.STARS.name);
        }

        setEffectivityDates(properties);

        return properties;
    }

    /**
     * Return the supplied bean properties in an InstanceProperties object.
     *
     * @param description description of the relationship
     * @param confidence is the relationship correct?
     * @param expression expression that describes the relationship
     * @param relationshipStatus ordinal for the relationship status enum (draft, active, deprecated, obsolete, other)
     * @param steward user id or name of steward id who assigned the relationship (or approved the discovered value).
     * @param source id of the source of the knowledge of the relationship
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    InstanceProperties getTermRelationshipProperties(String expression,
                                                     int    confidence,
                                                     String description,
                                                     int    relationshipStatus,
                                                     String steward,
                                                     String source,
                                                     String methodName) throws InvalidParameterException
    {
        InstanceProperties properties;

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  null,
                                                                  OpenMetadataProperty.EXPRESSION.name,
                                                                  expression,
                                                                  methodName);

        properties = repositoryHelper.addIntPropertyToInstance(serviceName,
                                                               properties,
                                                               OpenMetadataProperty.CONFIDENCE.name,
                                                               confidence,
                                                               methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.DESCRIPTION.name,
                                                                  description,
                                                                  methodName);


        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name,
                                                                    GlossaryTermRelationshipStatus.getOpenTypeGUID(),
                                                                    GlossaryTermRelationshipStatus.getOpenTypeName(),
                                                                    relationshipStatus,
                                                                    methodName);
        }
        catch (TypeErrorException error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name);
        }

        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.STEWARD.name,
                                                                  steward,
                                                                  methodName);


        properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                  properties,
                                                                  OpenMetadataProperty.SOURCE.name,
                                                                  source,
                                                                  methodName);


        setEffectivityDates(properties);

        return properties;
    }
}
