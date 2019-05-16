/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * NoteMapper provides property name mapping for Note Log Entries and their relationships.
 */
public class NoteMapper
{
    public static final String NOTELOG_TYPE_GUID                         = "646727c7-9ad4-46fa-b660-265489ad96c6";
    public static final String NOTELOG_TYPE_NAME                         = "NoteLog";              /* from Area 1 */
    /* Referenceable */

    public static final String NAME_PROPERTY_NAME                        = "name";                 /* from NoteLog entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";          /* from NoteLog entity */
    public static final String IS_PUBLIC_PROPERTY_NAME                   = "isPublic";            /* from NoteLog entity and AttachedNoteLog relationship */

    public static final String NOTE_LOG_ENTRIES_TYPE_GUID                = "38edecc6-f385-4574-8144-524a44e3e712";
    public static final String NOTE_LOG_ENTRIES_TYPE_NAME                = "AttachedNoteLogEntry";
    /* End1 = NoteLog; End 2 = NoteEntry */
    /* And isPrivateProperty */

}
