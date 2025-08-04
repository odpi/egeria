/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties;

import java.util.Objects;

/**
 * ExecutionPointProperties describes the behavior of a component that automates a governance action.  There are three types:
 * <ul>
 *     <li>ControlPointDefinition - describes the choices for a decision maker on how to react to a detected situation.</li>
 *     <li>VerificationPointDefinition - describes a test to detect that a desired state or situation is true or not.</li>
 *     <li>EnforcementPointDefinition - description of an action that is taken to enforce governance.</li>
 * </ul>.
 */
public class ExecutionPointProperties extends ReferenceableProperties
{

    /**
     * Default constructor
     */
    public ExecutionPointProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ExecutionPointProperties(ReferenceableProperties template)
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
        return "ExecutionPointProperties{" +
                "} " + super.toString();
    }
}
