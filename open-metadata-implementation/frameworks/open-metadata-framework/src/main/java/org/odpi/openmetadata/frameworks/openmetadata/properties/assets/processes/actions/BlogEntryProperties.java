/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.actions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The "BlogEntry" describes a blog entry published by an actor.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BlogEntryProperties extends NotificationProperties
{
    /**
     * Default constructor
     */
    public BlogEntryProperties()
    {
        super();
        super.typeName = OpenMetadataType.BLOG_ENTRY.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public BlogEntryProperties(NotificationProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.BLOG_ENTRY.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public BlogEntryProperties(ActionProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.BLOG_ENTRY.typeName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "BlogEntryProperties{" +
                "} " + super.toString();
    }
}
