/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.events;

        import com.fasterxml.jackson.annotation.JsonAutoDetect;
        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import com.fasterxml.jackson.annotation.JsonInclude;

        import java.io.Serializable;

        import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
        import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SubjectAreaEventType describes the different types of org.odpi.openmetadata.accessservices.subjectarea.common.events produced by the Subject Area OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum SubjectAreaEventType implements Serializable
{
    UNKNOWN_SUBJECTAREA_EVENT                       (0,  "UnknownSubjectAreaEvent",           "A Subject Area event that is not recognized by the local org.odpi.openmetadata.accessservices.subjectarea.server."),

    // Glossary terms
    NEW_GLOSSARYTERM_EVENT                          (1,  "NewGlossaryTerm",                   "A new Node has been defined."),
    UPDATED_GLOSSARYYERM_EVENT                      (2,  "UpdatedGlossaryTerm",               "An existing Node has been updated."),
    DELETED_GLOSSARYTERM_EVENT                      (3,  "DeletedGlossaryTerm",               "An existing  has been deleted."),
    CREATED_RELATIONSHIP_FOR_GLOSSARYTERM_EVENT     (4,  "CreatedRelationshipForGlossaryTerm","Created Relationship for Node."),
    UPDATED_RELATIONSHIP_FOR_GLOSSARYTERM_EVENT     (5,  "UpdatedelationshipForGlossaryTerm", "Updated relationship for Node."),
    DELETED_RELATIONSHIP_FOR_GLOSSARYTERM_EVENT     (6,  "DeletedRelationshipForGlossaryTerm","Deleted relationship for Node."),
    CLASSIFICATION_CHANGE_GLOSSARYTERM_EVENT        (7,  "ClassifiedGlossaryTerm",            "The classification associated with a Node has been changed."),
    COMMENT_CHANGED_RELATING_TO_TERM                (8,  "CommentChangedRelatingToGlossaryTerm", "A comment has been changed relating to a glossary term."),
    RATING_CHANGED_RELATING_TO_TERM                 (9,  "RatingChangedRelatingToGlossaryTerm",  "A rating has been changed relating to a glossary term."),
    TAG_CHANGED_RELATING_TO_TERM                    (10,  "TagChangedRelatingToGlossaryTerm",     "An informal tag has been changed relating to a glossary term."),
    TODO_CHANGED_RELATING_TO_TERM                   (11, "ToDoChangedRelatingToGlossaryTerm",    "A ToDo has been changed relating to a glossary term."),
    NOTE_CHANGED_RELATING_TO_TERM                   (12, "NoteChangedRelatingToGlossaryTerm",    "A note has been changed relating to a glossary term."),
    MEETING_CHANGED_RELATING_TO_TERM                (13, "MeetingChangedRelatingToGlossaryTerm", "A meeting has been changed relating to a glossary term."),
    COLLECTION_CHANGED_RELATING_TO_TERM             (14, "CollectionChangedRelatingToGlossaryTerm", "A collection has been changed relating to aglossary term."),
    ASSET_CHANGED_RELATING_TO_TERM                  (15, "AssetChangedRelatingToGlossaryTerm", "An asset has been changed relating to a glossary term."),

    // Glossary Categories
    NEW_GLOSSARYCATEGORY_EVENT                      (16,  "NewGlossaryCategory",                "A new Glossary Category has been defined."),
    UPDATED_GLOSSARYCATEGORY_EVENT                  (17,  "UpdatedGlossaryCategory",            "An existing Glossary Category has been updated."),
    DELETED_GLOSSARYCATEGORY_EVENT                  (18,  "DeletedGlossaryCategory",            "An existing Glossary Category has been deleted."),
    CREATED_RELATIONSHIP_FOR_GLOSSARYCATEGORY_EVENT (19,  "CreatedRelationshipForGlossaryCategory","Created Relationship for Glossary Category."),
    UPDATED_RELATIONSHIP_FOR_GLOSSARYCATEGORY_EVENT (20,  "UpdatedelationshipForGlossaryCategory", "Updated relationship for Glossary Category."),
    DELETED_RELATIONSHIP_FOR_GLOSSARYCATEGORY_EVENT (21,  "DeletedRelationshipForGlossaryCategory","Deleted relationship for Glossary Category."),
    CLASSIFICATION_CHANGE_GLOSSARYCATERGORY_EVENT   (22,  "ClassifiedGlossaryCategory",         "The classification associated with a Glossary Category has been changed."),
    COMMENT_CHANGED_RELATING_TO_CATEGORY            (23,  "CommentChangedRelatingToGlossaryCategory",   "A comment has been changed relating to a glossary category."),
    RATING_CHANGED_RELATING_TO_CATEGORY             (24,  "RatingChangedRelatingToGlossaryCategory",    "A rating has been changed relating to a glossary category."),
    TAG_CHANGED_RELATING_TO_CATEGORY                (25,  "TagChangedRelatingToGlossaryCategory",       "An informal tag has been changed relating to a glossary category."),
    TODO_CHANGED_RELATING_TO_CATEGORY               (26,  "ToDoChangedRelatingToGlossaryCategory",       "A ToDo has been changed relating to a glossary category."),
    NOTE_CHANGED_RELATING_TO_CATEGORY               (27,  "NoteChangedRelatingToGlossaryCategory",       "A note has been changed relating to a glossary category."),
    MEETING_CHANGED_RELATING_TO_CATEGORY            (28,  "MeetingChangedRelatingToGlossaryCategory",    "A meeting has been changed relating to a glossary category."),
    COLLECTION_CHANGED_RELATING_TO_CATEGORY         (29,  "CollectionChangedRelatingToGlossaryCategory", "A collection has been changed relating to a glossary category."),

    // Glossary
    NEW_GLOSSARY_EVENT                              (30,  "NewGlossary",                        "A new Glossary has been defined."),
    UPDATED_GLOSSARY_EVENT                          (31,  "UpdatedGlossary",                    "An existing Glossary has been updated."),
    DELETED_GLOSSARY_EVENT                          (32,  "DeletedGlossary",                    "An existing Glossary has been deleted."),
    CREATED_RELATIONSHIP_FOR_GLOSSARY_EVENT         (33,  "CreatedRelationshipForGlossary","Created Relationship for Glossary."),
    UPDATED_RELATIONSHIP_FOR_GLOSSARY_EVENT         (34,  "UpdatedelationshipForGlossary", "Updated relationship for Glossary."),
    DELETED_RELATIONSHIP_FOR_GLOSSARY_EVENT         (35,  "DeletedRelationshipForGlossary","Deleted relationship for Glossary."),
    CLASSIFICATION_CHANGE_GLOSSARY_EVENT            (36,  "ClassifiedGlossaryCategory",         "The classification associated with a Glossary has been changed."),
    COMMENT_CHANGED_RELATING_TO_GLOSSARY            (37,  "CommentChangedRelatingToGlossary",   "A comment has been changed relating to a glossary."),
    RATING_CHANGED_RELATING_TO_GLOSSARY             (38,  "RatingChangedRelatingToGlossary",    "A rating has been changed relating to a glossary."),
    TAG_CHANGED_RELATING_TO_GLOSSARY                (39,  "TagChangedRelatingToGlossary",       "An informal tag has been changed relating to a glossary."),
    TODO_CHANGED_RELATING_TO_GLOSSARY               (40,  "ToDoChangedRelatingToGlossary",      "A ToDo has been changed relating to a glossary."),
    NOTE_CHANGED_RELATING_TO_GLOSSARY               (41,  "NoteChangedRelatingToGlossary",      "A note has been changed relating to a glossary."),
    MEETING_CHANGED_RELATING_TO_GLOSSARY            (42,  "MeetingChangedRelatingToGlossary",   "A meeting has been changed relating to a glossary."),
    COLLECTION_CHANGED_RELATING_TO_GLOSSARY         (43,  "CollectionChangedRelatingToGlossary","A collection has been changed relating to a glossary.");


    //Assumption is that we do not need to listen out for relevant type updates or deletes.
    private static final long     serialVersionUID = 1L;

    private  int      eventTypeCode;
    private  String   eventTypeName;
    private  String   eventTypeDescription;


    /**
     * Default Constructor - sets up the specific values for this instance of the enum.
     *
     * @param eventTypeCode - int identifier used for indexing based on the enum.
     * @param eventTypeName - string name used for messages that include the enum.
     * @param eventTypeDescription - default description for the enum value - used when natural resource
     *                                     bundle is not available.
     */
    SubjectAreaEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
    {
        this.eventTypeCode = eventTypeCode;
        this.eventTypeName = eventTypeName;
        this.eventTypeDescription = eventTypeDescription;
    }


    /**
     * Return the int identifier used for indexing based on the enum.
     *
     * @return int identifier code
     */
    public int getEventTypeCode()
    {
        return eventTypeCode;
    }


    /**
     * Return the string name used for messages that include the enum.
     *
     * @return String name
     */
    public String getEventTypeName()
    {
        return eventTypeName;
    }


    /**
     * Return the default description for the enum value - used when natural resource
     * bundle is not available.
     *
     * @return String default description
     */
    public String getEventTypeDescription()
    {
        return eventTypeDescription;
    }
}

