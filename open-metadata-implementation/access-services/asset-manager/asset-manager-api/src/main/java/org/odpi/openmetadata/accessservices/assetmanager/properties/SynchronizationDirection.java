/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SynchronizationDirection defines the permitted directions of flow of metadata updates between open metadata and a third party
 * technology.  It is setup in the configuration for an integration connector and is enforced in the integration service
 * (in the integration context to be precise).
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum SynchronizationDirection implements Serializable
{
    BOTH_DIRECTIONS   (0, 0,"Both Directions",
                       "Metadata exchange is permitted in both directions.  Synchronization is halted on a specific " +
                               "element if potentially clashing updates have occurred both in the third party technology and " +
                               "open metadata.  Such conflicts are logged on the audit log and resolved through manual stewardship."),
    TO_THIRD_PARTY    (1, 1,"To Third Party",
                       "The third party technology is logically downstream of open metadata.  This means the open metadata " +
                                  "ecosystem is the originator and owner of the metadata being synchronized. Any updates detected " +
                                  "in the third technology are overridden by the latest open metadata values."),
    FROM_THIRD_PARTY  (2, 2,"From Third Party",
                       "The third party technology is logically upstream (the originator and owner of the metadata).  " +
                                  "Any updates made in open metadata are not passed to the third party technology and the " +
                                  "third party technology is requested to refresh the open metadata version."),
    OTHER             (99, 99,"Other",
                       "Another type of synchronization rule - see description property."),
    ;


    private int    ordinal;
    private int    openTypeOrdinal;
    private String name;
    private String description;

    private static final long     serialVersionUID = 1L;


    /**
     * Constructor to set up the instance of this enum.
     *
     * @param ordinal code number
     * @param openTypeOrdinal code number from the equivalent Enum Type
     * @param name default name
     * @param description default description
     */
    SynchronizationDirection(int    ordinal,
                             int    openTypeOrdinal,
                             String name,
                             String description)
    {
        this.ordinal         = ordinal;
        this.openTypeOrdinal = openTypeOrdinal;
        this.name            = name;
        this.description     = description;
    }

    public static final String ENUM_TYPE_GUID  = "973a9f4c-93fa-43a5-a0c5-d97dbd164e78";
    public static final String ENUM_TYPE_NAME  = "PermittedSynchronization";

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
     * Return the code for this enum that comes from the Open Metadata Type that this enum represents.
     *
     * @return int code number
     */
    public int getOpenTypeOrdinal()
    {
        return openTypeOrdinal;
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
     * Return the unique identifier for the open metadata enum type that this enum class represents.
     *
     * @return string guid
     */
    public String getOpenTypeGUID() { return ENUM_TYPE_GUID; }


    /**
     * Return the unique name for the open metadata enum type that this enum class represents.
     *
     * @return string name
     */
    public String getOpenTypeName() { return ENUM_TYPE_NAME; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "KeyPattern{" +
                       "openTypeOrdinal=" + openTypeOrdinal +
                       ", ordinal=" + ordinal +
                       ", name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", openTypeGUID='" + getOpenTypeGUID() + '\'' +
                       ", openTypeName='" + getOpenTypeName() + '\'' +
                       '}';
    }
}