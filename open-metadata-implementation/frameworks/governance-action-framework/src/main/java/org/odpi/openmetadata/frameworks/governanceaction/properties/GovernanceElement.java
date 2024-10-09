/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;


import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernanceElement is the common interface for all metadata elements.  It adds the header information that is stored with the properties.
 * This includes detains of its unique identifier, type and origin.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = EngineActionElement.class, name = "EngineActionElement"),
                @JsonSubTypes.Type(value = GovernanceActionProcessStepElement.class, name = "GovernanceActionProcessStepElement"),
        })

public interface GovernanceElement
{
    /**
     * Return the element header associated with the properties.
     *
     * @return element header object
     */
    ElementHeader getElementHeader();


    /**
     * Set up the element header associated with the properties.
     *
     * @param elementHeader element header object
     */
    void setElementHeader(ElementHeader elementHeader);
}
