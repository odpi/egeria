/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.mappers;

/**
 * CommentMapper provides property name mapping for Comments.
 */
public class CommentMapper
{
    public static final String TYPE_NAME                                = "Comment";              /* from Area 1 */
    public static final String RELATIONSHIP_TYPE_NAME                   = "AttachedComment";      /* from Area 1 */
    public static final String ANSWER_RELATIONSHIP_TYPE_NAME            = "AcceptedAnswer";       /* from Area 1 */

    public static final String QUALIFIED_NAME_PROPERTY_NAME             = "qualifiedName";        /* from Referenceable entity */
    public static final String TEXT_PROPERTY_NAME                       = "text";                 /* from Comment entity */
    public static final String TYPE_PROPERTY_NAME                       = "commentType";          /* from Comment entity */
    public static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME      = "additionalProperties"; /* from Referenceable entity */
}
