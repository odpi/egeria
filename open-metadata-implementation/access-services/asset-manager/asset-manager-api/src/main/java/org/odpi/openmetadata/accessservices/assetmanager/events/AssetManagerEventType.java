/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetManagerEventType describes the different types of events produced by the Asset Manager OMAS.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AssetManagerEventType implements Serializable
{
    /*
     * General Events
     */
    UNKNOWN_ASSET_MANAGER_EVENT   (0,  "Unknown Event",  "An event that is not recognized by the local server."),
    REFRESH_ELEMENT_EVENT         (1,  "Refresh Element",  "Check the synchronization of an element and update it in open metadata as necessary."),

    /*
     * Events relating to glossary exchange
     */
    NEW_GLOSSARY_CREATED                             (10,  "New Glossary",                       "A new glossary has been created."),
    GLOSSARY_UPDATED                                 (11,  "Glossary Updated",                   "A glossary's properties has been updated."),
    GLOSSARY_DELETED                                 (12,  "Glossary Deleted",                   "A glossary and all its terms, categories and relationships has been created."),
    GLOSSARY_CLASSIFIED                              (13,  "Glossary Classified",                "A classification has been added to a glossary."),
    GLOSSARY_RECLASSIFIED                            (14,  "Glossary Reclassified",              "The properties for a classification attached to a glossary have been updated."),
    GLOSSARY_DECLASSIFIED                            (15,  "Glossary Declassified",              "A classification has been removed from a glossary."),
    NEW_CATEGORY_ADDED_TO_GLOSSARY                   (16,  "New Glossary Category",              "A new glossary category has been created."),
    CATEGORY_UPDATED_IN_GLOSSARY                     (17,  "Glossary Category Updated",          "The properties for a glossary category have been updated."),
    CATEGORY_DELETED_FROM_GLOSSARY                   (18,  "Glossary Category Deleted",          "A glossary category has been deleted."),
    CATEGORY_CLASSIFIED                              (19,  "Glossary Category Classified",       "A classification has been added to a glossary category."),
    CATEGORY_RECLASSIFIED                            (20,  "Glossary Category Reclassified",     "The properties for a classification attached to a glossary category have been updated."),
    CATEGORY_DECLASSIFIED                            (21,  "Glossary Category Declassified",     "A classification has been removed from a glossary category."),
    CATEGORY_ATTACHED_TO_GLOSSARY_CATEGORY           (22,  "Glossary Category Parent Assigned",  "A glossary category has been linked to a parent category."),
    CATEGORY_DETACHED_FROM_GLOSSARY_CATEGORY         (23,  "Glossary Category Parent Removed",   "The parent category has been removed from a glossary category."),
    NEW_TERM_ADDED_TO_GLOSSARY                       (24,  "New Glossary Term",                  "A new glossary term has been created."),
    TERM_UPDATED_IN_GLOSSARY                         (25,  "Glossary Term Updated",              "A glossary term has been updated."),
    TERM_DELETED_FROM_GLOSSARY                       (26,  "Glossary Term Deleted",              "A glossary term has been deleted."),
    TERM_CLASSIFIED                                  (27,  "Glossary Term Classified",           "A classification has been added to a glossary term."),
    TERM_RECLASSIFIED                                (28,  "Glossary Term Reclassified",         "The properties for a classification attached to a glossary term have been updated."),
    TERM_DECLASSIFIED                                (29,  "Glossary Term Declassified",         "A classification has been removed from a glossary term."),
    TERM_ATTACHED_TO_GLOSSARY_CATEGORY               (30,  "Glossary Term Categorized",          "A glossary term as been linked to a glossary category."),
    TERM_REATTACHED_TO_GLOSSARY_CATEGORY             (31,  "Glossary Term Recategorized",        "The properties associated with a term categorization have been updated."),
    TERM_DETACHED_FROM_GLOSSARY_CATEGORY             (32,  "Glossary Term Decategorized",        "A glossary term has been unlined from a glossary category."),
    TERM_TO_TERM_RELATIONSHIP_ADDED_TO_GLOSSARY      (33,  "Term to Term Relationship Added",    "A new relationship has been established between two glossary terms."),
    TERM_TO_TERM_RELATIONSHIP_UPDATED                (34,  "Term to Term Relationship Updated",  "The properties for a term to term relationship have been updated."),
    TERM_TO_TERM_RELATIONSHIP_DELETED_FROM_GLOSSARY  (35,  "Term to Term Relationship Deleted",  "A relationship between two terms has been deleted."),

    ;

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
    AssetManagerEventType(int eventTypeCode, String eventTypeName, String eventTypeDescription)
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


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetManagerEventType{" +
                "eventTypeCode=" + eventTypeCode +
                ", eventTypeName='" + eventTypeName + '\'' +
                ", eventTypeDescription='" + eventTypeDescription + '\'' +
                '}';
    }
}
