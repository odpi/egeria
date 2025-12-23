/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.actors;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ContactThroughProperties describes the properties for the ContactThrough relationship between an actor profile and
 * a contact details element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ContactThroughProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor
     */
    public ContactThroughProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ContactThroughProperties(ContactThroughProperties template)
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
        return "ContactThroughProperties{} " + super.toString();
    }
}
