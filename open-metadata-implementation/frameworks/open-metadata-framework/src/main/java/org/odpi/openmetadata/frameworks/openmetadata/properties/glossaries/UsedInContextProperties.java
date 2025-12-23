/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * UsedInContextProperties describes a relationship between a glossary term and its context.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class UsedInContextProperties extends GlossaryTermRelationshipProperties
{
    /**
     * Default constructor
     */
    public UsedInContextProperties()
    {
        super();
        super.typeName = OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public UsedInContextProperties(UsedInContextProperties template)
    {
        super(template);
    }

    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "UsedInContextProperties{} " + super.toString();
    }
}
