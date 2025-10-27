/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ProducedActionTarget characterises one of the action targets that this governance service
 * sets up to pass on to any follow-on steps in a governance action process that it is a part of.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ProducedActionTarget extends ActionTargetType
{
    /**
     * Default constructor
     */
    public ProducedActionTarget()
    {
        super();
        super.setSpecificationPropertyType(SpecificationPropertyType.PRODUCED_ACTION_TARGET);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ProducedActionTarget(ActionTargetType template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ProducedActionTarget{" +
                "} " + super.toString();
    }
}
