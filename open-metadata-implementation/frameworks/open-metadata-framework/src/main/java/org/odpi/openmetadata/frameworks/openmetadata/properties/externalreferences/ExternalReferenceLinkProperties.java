/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.externalreferences;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ExternalReferenceLinkProperties provides a structure for the properties that link an external reference to an object.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExternalReferenceLinkProperties extends LabeledRelationshipProperties
{
    /**
     * Default constructor
     */
    public ExternalReferenceLinkProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExternalReferenceLinkProperties(ExternalReferenceLinkProperties template)
    {
        super (template);
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ExternalReferenceLinkProperties{" +
                "} " + super.toString();
    }
}
