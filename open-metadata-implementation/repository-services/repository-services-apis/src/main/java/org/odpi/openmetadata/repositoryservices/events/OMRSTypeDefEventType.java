/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSTypeDefEventType defines the different types of TypeDef events in the open metadata repository services
 * protocol:
 * <ul>
 *     <li>
 *         UNKNOWN_TYPEDEF_EVENT: the event is not recognized by this local server, probably because it is back-level
 *         from other servers in the cluster.  It is logged in the audit log and then ignored.  The metadata exchange
 *         protocol should evolve so that new message types can be ignored by back-level servers without damage
 *         to the cluster's integrity.
 *     </li>
 *     <li>
 *         NEW_TYPEDEF: A new TypeDef has been defined.
 *     </li>
 *     <li>
 *         UPDATED_TYPEDEF: An existing TypeDef has been updated.
 *     </li>
 *     <li>
 *         DELETED_TYPEDEF_EVENT: An existing TypeDef has been deleted.
 *     </li>
 *     <li>
 *         RE_IDENTIFIED_TYPEDEF_EVENT: the guid has been changed for a TypeDef.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OMRSTypeDefEventType implements Serializable
{
    UNKNOWN_TYPEDEF_EVENT                 (0,  "UnknownTypeDefEvent",          "A TypeDef event that is not recognized by the local server."),
    NEW_TYPEDEF_EVENT                     (1,  "NewTypeDef",                   "A new TypeDef has been defined."),
    NEW_ATTRIBUTE_TYPEDEF_EVENT           (2,  "NewAttributeTypeDef",          "A new AttributeTypeDef has been defined."),
    UPDATED_TYPEDEF_EVENT                 (3,  "UpdatedTypeDef",               "An existing TypeDef has been updated."),
    DELETED_TYPEDEF_EVENT                 (4,  "DeletedTypeDef",               "An existing TypeDef has been deleted."),
    DELETED_ATTRIBUTE_TYPEDEF_EVENT       (5,  "DeletedAttributeTypeDef",      "An existing AttributeTypeDef has been deleted."),
    RE_IDENTIFIED_TYPEDEF_EVENT           (6,  "ReIdentifiedTypeDef",          "An existing TypeDef has changed either it guid or its name."),
    RE_IDENTIFIED_ATTRIBUTE_TYPEDEF_EVENT (7,  "ReIdentifiedAttributeTypeDef", "An existing AttributeTypeDef has changed either it guid or its name."),
    TYPEDEF_ERROR_EVENT                   (99, "TypeDefErrorEvent",
                                               "An error has been detected in the exchange of TypeDefs between members of the cohort.");

    private static final long serialVersionUID = 1L;

    private  int    ordinal;
    private  String name;
    private  String description;


    /**
     * Default Constructor sets up the specific values for this instance of the enum.
     *
     * @param ordinal int identifier used for indexing based on the enum.
     * @param name string name used for messages that include the enum.
     * @param description default description for the enum value used when natural resource
     *                                     bundle is not available.
     */
    OMRSTypeDefEventType(int ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the int identifier used for indexing based on the enum.
     *
     * @return int identifier code
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the string name used for messages that include the enum.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the enum value.  This is used when natural resource
     * bundle is not available.
     *
     * @return String default description
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
        return "OMRSTypeDefEventType{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
