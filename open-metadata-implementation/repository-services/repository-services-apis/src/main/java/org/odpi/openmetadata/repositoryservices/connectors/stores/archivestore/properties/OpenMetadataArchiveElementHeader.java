/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataArchiveElementHeader provides a common base for the content of an open metadata archive.
 * It implements Serializable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = OpenMetadataArchive.class, name = "OpenMetadataArchive"),
        @JsonSubTypes.Type(value = OpenMetadataArchiveProperties.class, name = "OpenMetadataArchiveProperties"),
        @JsonSubTypes.Type(value = OpenMetadataArchiveTypeStore.class, name = "OpenMetadataArchiveTypeStore"),
        @JsonSubTypes.Type(value = OpenMetadataArchiveInstanceStore.class, name = "OpenMetadataArchiveInstanceStore")
})
public abstract class OpenMetadataArchiveElementHeader implements Serializable
{
    private static final long serialVersionUID = 1L;


    /**
     * Default Constructor sets the properties to nulls
     */
    public OpenMetadataArchiveElementHeader()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template object to copy
     */
    public OpenMetadataArchiveElementHeader(OpenMetadataArchiveElementHeader template)
    {
        /*
         * Nothing to do.
         */
    }
}
