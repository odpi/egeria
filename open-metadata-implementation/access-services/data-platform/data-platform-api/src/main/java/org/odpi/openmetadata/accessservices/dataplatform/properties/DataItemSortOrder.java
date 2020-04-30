/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataItemSortOrder is used for schema attributes that may have multiple instances.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum DataItemSortOrder implements Serializable
{
    UNKNOWN    (0, "<Unknown>",  "The sort order is not specified."),
    ASCENDING  (1, "Ascending",  "The attribute instances are organized so that the " +
                                                                 "smallest/lowest value is first and the rest of the instances follow in " +
                                                                 "ascending order."),
    DESCENDING (2, "Descending", "The attribute instances are organized so that the " +
                                                                 "largest/highest value is first and the rest of the instances follow in " +
                                                                 "descending order."),
    UNSORTED   (3, "Unsorted",   "The instances of the schema attribute may appear in any order.");


    private int    codeValue;
    private String codeName;
    private String description;

    private static final long     serialVersionUID = 1L;

    /**
     * Constructor to set up the instance of this enum.
     *
     * @param codeValue code number
     * @param codeName default name
     * @param description default description
     */
    DataItemSortOrder(int codeValue, String codeName, String description)
    {
        this.codeValue   = codeValue;
        this.codeName    = codeName;
        this.description = description;
    }


    /**
     * Return the code for this enum used for indexing based on the enum value.
     *
     * @return int code number
     */
    public int getOrdinal()
    {
        return codeValue;
    }


    /**
     * Return the default name for this enum type.
     *
     * @return String name
     */
    public String getName()
    {
        return codeName;
    }


    /**
     * Return the default description for this enum.
     *
     * @return String description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataItemSortOrder{" +
                "codeValue=" + codeValue +
                ", codeName='" + codeName + '\'' +
                ", description='" + description + '\'' +
                ", ordinal=" + getOrdinal() +
                ", name='" + getName() + '\'' +
                '}';
    }
}
