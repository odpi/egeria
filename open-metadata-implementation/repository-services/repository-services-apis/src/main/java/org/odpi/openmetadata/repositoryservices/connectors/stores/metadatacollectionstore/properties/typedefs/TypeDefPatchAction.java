/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefPatchAction defines the types of actions that can be taken to update a TypeDef.  These changes are safe
 * to make while there are active instances using them.  If more extensive changes need to be made to a TypeDef
 * then a new TypeDef should be defined.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum TypeDefPatchAction
{
    ADD_ATTRIBUTES                (1, "AddAttributes",
                                      "Add one or more new attributes to a TypeDef"),
    ADD_OPTIONS                   (2, "AddOptions",
                                      "Extend the current list of options for a TypeDef. These options are used to " +
                                              "help process metadata instances. " +
                                              "They may be different in each TypeDef."),
    UPDATE_OPTIONS                (3, "UpdateOptions",
                                      "Replace the options from a TypeDef. These options are used to help " +
                                              "process metadata instances. " +
                                              "They may be different in each TypeDef."),
    DELETE_OPTIONS                (4,  "DeleteOptions",
                                       "Delete the options from a TypeDef. These options are used to help " +
                                               "process metadata instances. " +
                                               "They may be different in each TypeDef."),
    ADD_EXTERNAL_STANDARDS        (5,  "AddExternalStandardMapping",
                                       "Add a mapping to an external standard either for the TypeDef or the supplied attributes."),
    UPDATE_EXTERNAL_STANDARDS     (6,  "UpdateExternalStandardMapping",
                                       "Update a mapping to an external standard either for the TypeDef or the supplied attributes."),
    DELETE_EXTERNAL_STANDARDS     (7,  "DeleteExternalStandardMapping",
                                       "Remove a mapping to an external standard either for the TypeDef or the supplied attributes."),
    UPDATE_DESCRIPTIONS           (8,  "UpdateDescriptions",
                                       "Update the descriptions and descriptionGUIDs of the TypeDef and its attributes.");


    private int    ordinal;
    private String name;
    private String description;


    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal numeric code for the patch action
     * @param name descriptive name for the patch action
     * @param description description of the patch action
     */
    TypeDefPatchAction(int ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the code value for the patch action.
     *
     * @return int code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the descriptive name for the patch action.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the description of the patch action.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "TypeDefPatchAction{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
