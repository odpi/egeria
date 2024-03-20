/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasAuditOperation describes the operation that an audit event describes.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum AtlasAuditOperation
{
    /**
     * An instance has been purged.
     */
    PURGE("PURGE"),

    /**
     * Instances have been exported.
     */
    EXPORT("EXPORT"),

    /**
     * Instances have been imported.
     */
    IMPORT("IMPORT"),

    /**
     * Import delete replica?
     */
    IMPORT_DELETE_REPL("IMPORT_DELETE_REPL"),

    /**
     * New type created
     */
    TYPE_DEF_CREATE("TYPE_DEF_CREATE"),

    /**
     * Existing type updated.
     */
    TYPE_DEF_UPDATE("TYPE_DEF_UPDATE"),

    /**
     * A type has been deleted.
     */
    TYPE_DEF_DELETE("TYPE_DEF_DELETE"),

    /**
     * Atlas has started.
     */
    SERVER_START("SERVER_START"),

    /**
     * Atlas is active
     */
    SERVER_STATE_ACTIVE("SERVER_STATE_ACTIVE");

    private final String type;


    /**
     * Constructor.
     *
     * @param type name
     */
    AtlasAuditOperation(String type)
    {
        this.type = type;
    }


    /**
     * Return the string name of the operation.
     *
     * @return name
     */
    public String getType()
    {
        return type;
    }
}
