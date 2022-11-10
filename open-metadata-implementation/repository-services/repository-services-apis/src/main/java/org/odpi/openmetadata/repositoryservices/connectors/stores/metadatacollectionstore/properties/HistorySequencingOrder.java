/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * HistorySequencingOrder is used for retrieval of history against a metadata collection.  It defines how the results
 * should be ordered before they are returned.  This is particularly important when the results are to returned
 * over multiple pages since the caller does not have all the results at once to perform the sort themselves.
 *
 * The sequencing order values are:
 * <ul>
 *     <li>
 *         FORWARDS: return the historical versions in chronological order, such that the first element is an older
 *         version then the second element, which is older than the third, and so on.
 *     </li>
 *     <li>
 *         BACKWARDS: return the historical versions in reverse chronological order, such that the first element is a
 *         more recent version then the second element, which is more recent than the third, and so on.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum HistorySequencingOrder implements Serializable
{
    FORWARDS             (0, "Forwards",  "Chronological order (oldest first)."),
    BACKWARDS            (1, "Backwards", "Reverse chronological order (newest first).");

    private static final long serialVersionUID = 1L;

    private final int     ordinal;
    private final String  name;
    private final String  description;

    /**
     * Constructor to set up a single instances of the enum.
     *
     * @param ordinal numerical representation of the sequencing order
     * @param name default string name of the sequencing order
     * @param description default string description of the sequencing order
     */
    HistorySequencingOrder(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }

    /**
     * Return the numeric representation of the sequencing order.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the sequencing order.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the sequencing order.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "HistorySequencingOrder{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
