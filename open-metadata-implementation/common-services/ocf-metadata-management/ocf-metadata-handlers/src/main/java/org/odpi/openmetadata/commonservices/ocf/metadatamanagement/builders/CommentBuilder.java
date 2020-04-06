/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.builders;


import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers.CommentMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.CommentType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

/**
 * CommentBuilder is able to build the properties for a Like entity.
 */
public class CommentBuilder extends RootBuilder
{
    private CommentType commentType;
    private String      commentText;
    private boolean     isPublic;
    private String      anchorGUID;

    /**
     * Constructor.
     *
     * @param commentType   type of comment enum.
     * @param commentText   String - the text of the comment.
     * @param isPublic      should this be visible to all or private to the caller
     * @param anchorGUID unique identifier of the anchor entity
     * @param repositoryHelper helper methods
     * @param serviceName name of this OMAS
     * @param serverName name of local server
     */
    public CommentBuilder(CommentType          commentType,
                          String               commentText,
                          boolean              isPublic,
                          String               anchorGUID,
                          OMRSRepositoryHelper repositoryHelper,
                          String               serviceName,
                          String               serverName)
    {
        super(repositoryHelper, serviceName, serverName);

        this.commentText = commentText;
        this.commentType = commentType;
        this.isPublic = isPublic;
        this.anchorGUID = anchorGUID;
    }


    /**
     * Return the relationship properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     */
    public InstanceProperties getRelationshipInstanceProperties(String  methodName)
    {
        return repositoryHelper.addBooleanPropertyToInstance(serviceName,
                                                             null,
                                                             CommentMapper.IS_PUBLIC_PROPERTY_NAME,
                                                             isPublic,
                                                             methodName);
    }


    /**
     * Return the entity properties in an InstanceProperties object.
     *
     * @param methodName name of the calling method
     * @return InstanceProperties object
     * @throws InvalidParameterException there is a problem with the properties
     */
    public InstanceProperties getEntityInstanceProperties(String  methodName) throws InvalidParameterException
    {
        InstanceProperties properties = super.getInstanceProperties(methodName);

        if (commentText != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      CommentMapper.TEXT_PROPERTY_NAME,
                                                                      commentText,
                                                                      methodName);
        }

        if (anchorGUID != null)
        {
            properties = repositoryHelper.addStringPropertyToInstance(serviceName,
                                                                      properties,
                                                                      CommentMapper.ANCHOR_GUID_PROPERTY_NAME,
                                                                      anchorGUID,
                                                                      methodName);
        }

        return this.addCommentTypePropertyToInstance(properties, commentType, methodName);
    }


    /**
     * Set up a property value for the CommentType enum property.
     *
     * @param properties   current properties
     * @param commentType  enum value
     * @param methodName   calling method
     *
     * @return  InstanceProperties object with the enum value added
     */
    private InstanceProperties addCommentTypePropertyToInstance(InstanceProperties  properties,
                                                                CommentType commentType,
                                                                String              methodName)
    {
        int                ordinal = 99;
        String             symbolicName = null;
        String             description = null;

        final int    element1Ordinal         = 0;
        final String element1Value           = "GeneralComment";
        final String element1Description     = "General comment.";

        final int    element2Ordinal         = 1;
        final String element2Value           = "Question";
        final String element2Description     = "A question.";

        final int    element3Ordinal         = 2;
        final String element3Value           = "Answer";
        final String element3Description     = "An answer to a previously asked question.";

        final int    element4Ordinal         = 3;
        final String element4Value           = "Suggestion";
        final String element4Description     = "A suggestion for improvement.";

        final int    element5Ordinal         = 4;
        final String element5Value           = "Experience";
        final String element5Description     = "An account of an experience.";

        switch (commentType)
        {
            case STANDARD_COMMENT:
                ordinal = element1Ordinal;
                symbolicName = element1Value;
                description = element1Description;
                break;

            case QUESTION:
                ordinal = element2Ordinal;
                symbolicName = element2Value;
                description = element2Description;
                break;

            case ANSWER:
                ordinal = element3Ordinal;
                symbolicName = element3Value;
                description = element3Description;
                break;

            case SUGGESTION:
                ordinal = element4Ordinal;
                symbolicName = element4Value;
                description = element4Description;
                break;

            case USAGE_EXPERIENCE:
                ordinal = element5Ordinal;
                symbolicName = element5Value;
                description = element5Description;
                break;
        }

        return repositoryHelper.addEnumPropertyToInstance(serviceName,
                                                          properties,
                                                          CommentMapper.TYPE_PROPERTY_NAME,
                                                          ordinal,
                                                          symbolicName,
                                                          description,
                                                          methodName);
    }
}
