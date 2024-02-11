/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.converters;

import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.Scope;

import java.beans.PropertyEditorSupport;


/**
 * Special support for the Scope enum.
 */
public class ScopeEnumConverter extends PropertyEditorSupport
{
    /**
     * Convert the enum value from a string.
     *
     * @param text  The string to be parsed.
     * @throws IllegalArgumentException problem with the supplied parameter.
     */
    @Override
    public void setAsText(final String text) throws IllegalArgumentException
    {
        setValue(Scope.fromString(text));
    }
}
