/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * CohortTopicStructure defines the type of topic organization to use in a cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum CohortTopicStructure
{
    /**
     * The cohort members use three topics to exchange information.  One for registration requests, one for
     * type validation and one for exchange of instances stored by the cohort members.  This is the preferred and optimal
     * approach.
     */
    DEDICATED_TOPICS   (0, "Dedicated Cohort Topics",
                       "The cohort members use three topics to exchange information.  One for registration requests, one for " +
                               "type validation and one for exchange of instances stored by the cohort members.  This is the preferred and optimal " +
                               "approach."),

    /**
     * All asynchronous communication between cohort members is via a single topic.  This is the original design and may
     * still be used when communicating with back level cohort members.
     */
    SINGLE_TOPIC    (1, "Single Topic",
                       "All asynchronous communication between cohort members is via a single topic.  This is the original design and may " +
                               "still be used when communicating with back level cohort members."),


    /**
     * Both the single cohort topic and the dedicated topics are set up and used.  This is necessary when the cohort
     * has members with different capabilities.  This configuration may cause some events to be processed twice.
     */
    BOTH_SINGLE_AND_DEDICATED_TOPICS  ( 2,"Both Single and Dedicated Topics",
                       "Both the single cohort topic and the dedicated topics are set up and used.  This is necessary when the cohort" +
                               "has members with different capabilities.  This configuration may cause some events to be processed twice."),
    ;


    private final int    ordinal;
    private final String name;
    private final String description;

    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param name default name
     * @param description default description
     */
    CohortTopicStructure(int    ordinal,
                         String name,
                         String description)
    {
        this.ordinal         = ordinal;
        this.name            = name;
        this.description     = description;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int key pattern code
     */
    public int getOrdinal()
    {
        return ordinal;
    }



    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the key pattern for this enum instance.
     *
     * @return String default description
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
        return "CohortTopicStructure{" +
                       "ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       '}';
    }
}