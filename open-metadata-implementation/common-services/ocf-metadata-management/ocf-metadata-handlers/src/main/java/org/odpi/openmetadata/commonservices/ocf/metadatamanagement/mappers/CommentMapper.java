/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * CommentMapper provides property name mapping for Comments and their relationships.
 */
public class CommentMapper
{
    public static final String COMMENT_TYPE_GUID                         = "1a226073-9c84-40e4-a422-fbddb9b84278";
    public static final String COMMENT_TYPE_NAME                         = "Comment";              /* from Area 1 */
    /* Referenceable */

    public static final String TEXT_PROPERTY_NAME                        = "text";          /* from Comment entity */
    public static final String TYPE_PROPERTY_NAME                        = "type";          /* from Comment entity */
    public static final String ANCHOR_GUID_PROPERTY_NAME                 = "anchorGUID";    /* from Comment entity */

    public static final String REFERENCEABLE_TO_COMMENT_TYPE_GUID        = "0d90501b-bf29-4621-a207-0c8c953bdac9";
    public static final String REFERENCEABLE_TO_COMMENT_TYPE_NAME        = "AttachedComment";
    /* End1 = Referenceable; End 2 = Comment */

    public static final String ANSWER_RELATIONSHIP_TYPE_GUID             = "ecf1a3ca-adc5-4747-82cf-10ec590c5c69";
    public static final String ANSWER_RELATIONSHIP_TYPE_NAME             = "AcceptedAnswer";
    /* End1 = Comment; End 2 = Comment */

    public static final String IS_PUBLIC_PROPERTY_NAME                   = "isPublic";            /* from AttachedComment and AcceptedAnswer relationships */
}
