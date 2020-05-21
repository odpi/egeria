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
 * A KeyPattern defines the type of External Identifier in use for an asset, or the type of Primary Key used within an
 * asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum KeyPattern implements Serializable
{
    LOCAL_KEY     (0,  "Local Key", "Unique key allocated and used within the scope of a single system."),
    RECYCLED_KEY  (1,  "Recycled Key", "Key allocated and used within the scope of a single system that is periodically reused for different records."),
    NATURAL_KEY   (2,  "Natural Key", "Key derived from an attribute of the entity, such as email address, passport number."),
    MIRROR_KEY    (3,  "Mirror Key", "Key value copied from another system."),
    AGGREGATE_KEY (4,  "Aggregate Key", "Key formed by combining keys from multiple systems."),
    CALLERS_KEY   (5,  "Caller's Key", "Key from another system can bey used if system name provided."),
    STABLE_KEY    (6,  "Stable Key", "Key value will remain active even if records are merged."),
    OTHER         (99, "Other", "Another key pattern.");

    private static final long     serialVersionUID = 1L;

    private int            keyPatternCode;
    private String         keyPatternName;
    private String         keyPatternDescription;

    /**
     * Typical Constructor
     *
     * @param keyPatternCode ordinal
     * @param keyPatternName short name
     * @param keyPatternDescription longer explanation
     */
    KeyPattern(int     keyPatternCode, String   keyPatternName, String   keyPatternDescription)
    {
        /*
         * Save the values supplied
         */
        this.keyPatternCode = keyPatternCode;
        this.keyPatternName = keyPatternName;
        this.keyPatternDescription = keyPatternDescription;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int key pattern code
     */
    public int getOrdinal()
    {
        return keyPatternCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getName()
    {
        return keyPatternName;
    }


    /**
     * Return the default description for the key pattern for this enum instance.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return keyPatternDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "KeyPattern{" +
                "keyPatternCode=" + keyPatternCode +
                ", keyPatternName='" + keyPatternName + '\'' +
                ", keyPatternDescription='" + keyPatternDescription + '\'' +
                '}';
    }
}