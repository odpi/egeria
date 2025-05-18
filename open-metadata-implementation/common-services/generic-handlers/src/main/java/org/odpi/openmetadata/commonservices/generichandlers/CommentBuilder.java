/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.frameworks.openmetadata.enums.CommentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;


/**
 * CommentBuilder is able to build the properties for a Comment entity.
 */
public class CommentBuilder extends ReferenceableBuilder
{
    private final int     commentType;
    private final String  commentText;
    private final boolean isPublic;

    /**
     * Constructor.
     *
     * @param qualifiedName unique name (qualifiedName) for the comment
     * @param commentType   ordinal of comment enum.
     * @param commentText   String - the text of the comment.
     * @param isPublic      should this be visible to all or private to the caller
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public CommentBuilder(String               qualifiedName,
                          int                  commentType,
                          String               commentText,
                          boolean              isPublic,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(qualifiedName,
              OpenMetadataType.COMMENT.typeGUID,
              OpenMetadataType.COMMENT.typeName,
              repositoryHelper,
              serviceName,
              serverName);

        this.commentText = commentText;
        this.commentType = commentType;
        this.isPublic = isPublic;
    }


    /**
     * Return the relationship properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getRelationshipInstanceProperties(String  methodName)
    {
        InstanceProperties properties = repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                                                      null,
                                                                                      OpenMetadataProperty.IS_PUBLIC.name,
                                                                                      isPublic,
                                                                                      methodName);

        setEffectivityDates(properties);

        return properties;
    }


    /**
     * Return the entity properties in an InstanceProperties object.
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
                                                                  OpenMetadataProperty.TEXT.name,
                                                                  commentText,
                                                                  methodName);

        try
        {
            properties = repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                                    properties,
                                                                    OpenMetadataProperty.COMMENT_TYPE.name,
                                                                    CommentType.getOpenTypeGUID(),
                                                                    CommentType.getOpenTypeName(),
                                                                    commentType,
                                                                    methodName);
        }
        catch (TypeErrorException  error)
        {
            throw new InvalidParameterException(error, OpenMetadataProperty.COMMENT_TYPE.name);
        }

        return properties;
    }
}
