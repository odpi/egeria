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
 * OMRSEventErrorCode is a merging of the OMRSRegistryEventErrorCode, OMRSTypeDefEventErrorCode and
 * OMRSInstanceEventErrorCode that is used in OMRSEvent.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OMRSEventErrorCode implements Serializable
{
    /**
     * No Error - There has been no error detected and so the error code is not in use.
     */
    NOT_IN_USE                     (0,   "No Error",
                                    "There has been no error detected and so the error code is not in use."),

    /**
     * Invalid event format - The event does not have a recognized format.
     */
    INVALID_EVENT_FORMAT           (1,   "Invalid event format",
                                    "The event does not have a recognized format."),

    /**
     * Invalid registry event format - The event does not have a recognized format.
     */
    INVALID_REGISTRY_EVENT         (2,   "Invalid registry event format",
                                    "The event does not have a recognized format."),

    /**
     * Invalid typedef event format - The event does not have a recognized format.
     */
    INVALID_TYPEDEF_EVENT          (3,   "Invalid typedef event format",
                                    "The event does not have a recognized format."),

    /**
     * Invalid instance event format - The event does not have a recognized format.
     */
    INVALID_INSTANCE_EVENT         (4,   "Invalid instance event format",
                                    "The event does not have a recognized format."),

    /**
     * Conflicting metadata collection id - Remote server/repository is using the same metadata collection id as the local server.
     */
    CONFLICTING_COLLECTION_ID      (11,  "Conflicting metadata collection id",
                                    "Remote server/repository is using the same metadata collection id as the local server."),

    /**
     * Unusable Remote Connection to Repository - The remote connection send by one of the member of the cohort is resulting in errors
     * when it is used to create an OMRS Connector to the repository.
     */
    BAD_REMOTE_CONNECTION          (12,  "Unusable Remote Connection to Repository",
                                    "The remote connection send by one of the member of the cohort is resulting in errors " +
                                            "when it is used to create an OMRS Connector to the repository."),

    /**
     * Conflicting TypeDefs - There are conflicting type definitions (TypeDefs) detected between two repositories in the open metadata repository cohort.
     */
    CONFLICTING_TYPEDEFS           (21,  "Conflicting TypeDefs",
                                    "There are conflicting type definitions (TypeDefs) detected between two " +
                                            "repositories in the open metadata repository cohort."),

    /**
     * Conflicting AttributeTypeDefs - There are conflicting attribute type definitions (AttributeTypeDefs) detected between two
     * repositories in the open metadata repository cohort.
     */
    CONFLICTING_ATTRIBUTE_TYPEDEFS (22,  "Conflicting AttributeTypeDefs",
                                    "There are conflicting attribute type definitions (AttributeTypeDefs) detected between two " +
                                            "repositories in the open metadata repository cohort."),

    /**
     * TypeDefPatchMismatch - There are different versions of a TypeDef in use in the cohort.
     */
    TYPEDEF_PATCH_MISMATCH         (23,  "TypeDefPatch Mismatch",
                                         "There are different versions of a TypeDef in use in the cohort."),

    /**
     * Conflicting Instances - There are two metadata instances that have the same unique identifier (guid) but have different types.
     */
    CONFLICTING_INSTANCES          (31,  "Conflicting Instances",
                                    "There are two metadata instances that have the same unique identifier (guid) but have different types."),

    /**
     * Conflicting Type Version - An instance can not be processed because there is a mismatch in the type definition (TypeDef) version.
     */
    CONFLICTING_TYPE               (32,  "Conflicting Type Version",
                                     "An instance can not be processed because there is a mismatch in the type definition (TypeDef) version."),

    /**
     * Unknown Error Code - Unrecognized error code from incoming event.
     */
    UNKNOWN_ERROR_CODE             (99,  "Unknown Error Code",
                                    "Unrecognized error code from incoming event.");

    private static final long serialVersionUID = 1L;

    private final int                ordinal;
    private final String             name;
    private final String             description;


    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc. with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is no natural
     *                             language resource bundle available.
     */
    OMRSEventErrorCode(int                ordinal,
                       String             name,
                       String             description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
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
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "OMRSEventErrorCode{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
