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
 * OMRSTypeDefEventErrorCode defines the list of error codes that are used to record errors in the TypeDef
 * synchronization process that is used by the repository connectors within the open metadata repository cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum OMRSTypeDefEventErrorCode implements Serializable
{
    NOT_IN_USE                     (0,  "No Error",
                                        "There has been no error detected and so the error code is not in use.",
                                        null),
    CONFLICTING_TYPEDEFS           (1,  "ConflictingTypeDefs",
                                        "There are conflicting type definitions (TypeDefs) detected between two " +
                                        "repositories in the open metadata repository cohort.",
                                        OMRSEventErrorCode.CONFLICTING_TYPEDEFS),
    CONFLICTING_ATTRIBUTE_TYPEDEFS (2,  "ConflictingAttributeTypeDefs",
                                        "There are conflicting attribute type definitions (AttributeTypeDefs) detected between two " +
                                        "repositories in the open metadata repository cohort.",
                                        OMRSEventErrorCode.CONFLICTING_ATTRIBUTE_TYPEDEFS),
    TYPEDEF_PATCH_MISMATCH         (3,  "TypeDefPatchMismatch",
                                        "There are different versions of a TypeDef in use in the cohort",
                                        OMRSEventErrorCode.TYPEDEF_PATCH_MISMATCH),
    UNKNOWN_ERROR_CODE             (99, "Unknown Error Code",
                                        "Unrecognized error code from incoming event.",
                                        null);


    private static final long serialVersionUID = 1L;

    private int                ordinal;
    private String             name;
    private String             description;
    private OMRSEventErrorCode encoding;


    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is not natural
     *                             language resource bundle available.
     * @param encoding code value to use in OMRSEvents
     */
    OMRSTypeDefEventErrorCode(int                ordinal,
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
     * Return the identifier for the enum, used for indexing arrays etc with the enum.
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
     * Return the default description for the enum, used when there is not natural
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
        return "OMRSTypeDefEventErrorCode{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", encoding=" + encoding +
                '}';
    }
}