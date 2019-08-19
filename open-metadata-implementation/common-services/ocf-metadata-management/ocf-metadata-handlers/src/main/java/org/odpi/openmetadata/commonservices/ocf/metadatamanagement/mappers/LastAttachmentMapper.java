/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * LastAttachmentMapper provides property name mapping for last attachment beans.
 */
public class LastAttachmentMapper
{
    public static final String LAST_ATTACHMENT_TYPE_GUID                 = "ba3c8dfa-42a5-492c-bebc-88fa7492e75a";
    public static final String LAST_ATTACHMENT_TYPE_NAME                 = "LastAttachment";

    public static final String ANCHOR_GUID_PROPERTY_NAME                 = "anchorGUID";                          /* from LastAttachment entity */
    public static final String ANCHOR_TYPE_PROPERTY_NAME                 = "anchorType";                          /* from LastAttachment entity */
    public static final String ATTACHMENT_GUID_PROPERTY_NAME             = "attachmentGUID";                      /* from LastAttachment entity */
    public static final String ATTACHMENT_TYPE_PROPERTY_NAME             = "attachmentType";                      /* from LastAttachment entity */
    public static final String ATTACHMENT_OWNER_PROPERTY_NAME            = "attachmentOwner";                     /* from LastAttachment entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";                         /* from LastAttachment entity */

    public static final String REFERENCEABLE_TO_LAST_ATTACHMENT_TYPE_GUID  = "57e3687e-393e-4c0c-a4f1-a6634075465b";
    public static final String REFERENCEABLE_TO_LAST_ATTACHMENT_TYPE_NAME  = "LastAttachmentLink";
    /* End1 = Referenceable; End 2 = LastAttachment */
}
