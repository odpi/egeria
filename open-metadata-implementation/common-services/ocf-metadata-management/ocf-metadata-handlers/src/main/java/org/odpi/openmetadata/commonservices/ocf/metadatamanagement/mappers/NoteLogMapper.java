/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.mappers;

/**
 * NoteLogMapper provides property name mapping for NoteLogs and their relationships.
 */
public class NoteLogMapper
{
    public static final String NOTE_TYPE_GUID                           = "646727c7-9ad4-46fa-b660-265489ad96c6";
    public static final String NOTE_TYPE_NAME                            = "NoteEntry";              /* from Area 1 */
    /* Referenceable */

    public static final String NAME_PROPERTY_NAME                        = "name";                 /* from NoteLog entity */
    public static final String DESCRIPTION_PROPERTY_NAME                 = "description";          /* from NoteLog entity */
    public static final String IS_PUBLIC_PROPERTY_NAME                   = "isPublic";            /* from NoteLog entity and AttachedNoteLog relationship */

    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID       = "4f798c0c-6769-4a2d-b489-d2714d89e0a4";
    public static final String REFERENCEABLE_TO_NOTE_LOG_TYPE_NAME       = "AttachedNoteLog";
    /* End1 = Referenceable; End 2 = NoteLog */
    /* And isPrivateProperty */

    public static final String NOTE_LOG_ENTRIES_TYPE_GUID                = "38edecc6-f385-4574-8144-524a44e3e712";
    public static final String NOTE_LOG_ENTRIES_TYPE_NAME                = "AttachedNoteLogEntry";
    /* End1 = NoteLog; End 2 = NoteEntry */
    /* And isPrivateProperty */
}
