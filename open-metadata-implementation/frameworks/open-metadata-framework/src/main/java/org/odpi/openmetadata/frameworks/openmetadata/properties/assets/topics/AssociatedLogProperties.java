/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.topics;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The AssociatedLogProperties relationship properties for AssociatedLog.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssociatedLogProperties extends RelationshipBeanProperties
{
    /**
     * Default constructor sets the Connection properties to null.
     */
    public AssociatedLogProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.ASSOCIATED_LOG_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone Constructor to return a copy of a connection object.
     *
     * @param template Connection to copy
     */
    public AssociatedLogProperties(AssociatedLogProperties template)
    {
        super(template);
    }

    /**
     * Standard toString method. Note SecuredProperties and other credential type properties are not displayed.
     * This is deliberate because there is no knowing where the string will be printed.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssociatedLogProperties{} " + super.toString();
    }
}
