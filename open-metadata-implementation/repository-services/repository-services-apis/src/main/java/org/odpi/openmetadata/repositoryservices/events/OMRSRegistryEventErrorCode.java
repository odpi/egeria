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
 * OMRSRegistryEventErrorCode defines the list of error codes that are used to record errors in the registration
 * process that is used by the cohort registries when managing the membership of the open metadata repository cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OMRSRegistryEventErrorCode implements Serializable
{
    /**
     * No Error - There has been no error detected and so the error code is not in use.
     */
    NOT_IN_USE                (0, "No Error",
                               "There has been no error detected and so the error code is not in use.",
                               null),

    /**
     * Conflicting metadata collection id - Remote server/repository is using the same metadata collection id as the local server.
     */
    CONFLICTING_COLLECTION_ID (1, "Conflicting metadata collection id",
                               "Remote server/repository is using the same metadata collection id as the local server.",
                               OMRSEventErrorCode.CONFLICTING_COLLECTION_ID),

    /**
     * Unusable Remote Connection to Repository - The remote connection send by one of the member of the cohort is resulting in errors
     * when it is used to create an OMRS Connector to the repository.
     */
    BAD_REMOTE_CONNECTION     (2, "Unusable Remote Connection to Repository",
                                  "The remote connection send by one of the member of the cohort is resulting in errors " +
                                          "when it is used to create an OMRS Connector to the repository.",
                               OMRSEventErrorCode.BAD_REMOTE_CONNECTION),

    /**
     * Unknown Error Code - Unrecognized error code from incoming event.
     */
    UNKNOWN_ERROR_CODE        (99, "Unknown Error Code",
                               "Unrecognized error code from incoming event.",
                               null)
    ;

    private static final long serialVersionUID = 1L;

    private final int                ordinal;
    private final String             name;
    private final String             description;
    private final OMRSEventErrorCode encoding;


    /**
     * Typical constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc. with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is no natural
     *                             language resource bundle available.
     * @param encoding code value to use in OMRSEvents
     */
    OMRSRegistryEventErrorCode(int                  ordinal,
                               String               name,
                               String               description,
                               OMRSEventErrorCode   encoding)
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
        return "OMRSRegistryEventErrorCode{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", encoding=" + encoding +
                '}';
    }
}