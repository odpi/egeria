/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * MeaningMapper provides property name mapping for a Location object.
 */
public class MeaningMapper
{
    public static final String MEANING_TYPE_GUID             = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";
    public static final String MEANING_TYPE_NAME             = "GlossaryTerm";
    /* Referenceable */

    public static final String DISPLAY_NAME_PROPERTY_NAME    = "displayName";             /* from GlossaryTerm entity */
    public static final String SUMMARY_PROPERTY_NAME         = "summary";                 /* from GlossaryTerm entity */
    public static final String DESCRIPTION_PROPERTY_NAME     = "description";             /* from GlossaryTerm entity */

    public static final String MEANING_TO_REFERENCEABLE_TYPE_GUID      = "e6670973-645f-441a-bec7-6f5570345b92";
    public static final String MEANING_TO_REFERENCEABLE_TYPE_NAME      = "SemanticAssignment";
    /* End1 = Referenceable; End 2 = GlossaryTerm */
}
