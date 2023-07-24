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
 * OMRSInstanceEventErrorCode defines the list of error codes that are used to record errors in the metadata
 * instance replication process that is used by the repository connectors within the open metadata repository cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OMRSInstanceEventErrorCode implements Serializable
{
    /**
     * No Error - There has been no error detected and so the error code is not in use.
     */
    NOT_IN_USE                (0, "No Error",
                               "There has been no error detected and so the error code is not in use.",
                               null),

    /**
     * Conflicting Instances - There are two metadata instances that have the same unique identifier (guid) but have different types.
     */
    CONFLICTING_INSTANCES     (1, "Conflicting Instances",
                               "There are two metadata instances that have the same unique identifier (guid) but have different types.",
                               OMRSEventErrorCode.CONFLICTING_INSTANCES),

    /**
     * Conflicting Type Version - An instance can not be processed because there is a mismatch in the type definition (TypeDef) version.
     */
    CONFLICTING_TYPE         (2, "Conflicting Type Version",
                               "An instance can not be processed because there is a mismatch in the type definition (TypeDef) version.",
                               OMRSEventErrorCode.CONFLICTING_TYPE),

    /**
     * Unknown Error Code - Unrecognized error code from incoming event.
     */
    UNKNOWN_ERROR_CODE        (99, "Unknown Error Code",
                               "Unrecognized error code from incoming event.",
                               null);

    private static final long serialVersionUID = 1L;

    private final int                ordinal;
    private final String             name;
    private final String             description;
    private final OMRSEventErrorCode encoding;


    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc. with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is no natural
     *                             language resource bundle available.
     * @param encoding code value to use in OMRSEvents
     */
    OMRSInstanceEventErrorCode(int                ordinal,
                               String             name,
                               String             description,
                               OMRSEventErrorCode encoding)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.encoding = encoding;
    }


    /**
     * Return the identifier for the enum, used for indexing arrays etc. with the enum.
     *
     * @return int identifier
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the name for the enum, used for message content.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the enum, used when there is no natural
     * language resource bundle available.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the encoding to use in OMRSEvents.
     *
     * @return String OMRSEvent encoding for this errorCode
     */
    public OMRSEventErrorCode getEncoding()
    {
        return encoding;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSInstanceEventErrorCode{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", encoding=" + encoding +
                '}';
    }
}