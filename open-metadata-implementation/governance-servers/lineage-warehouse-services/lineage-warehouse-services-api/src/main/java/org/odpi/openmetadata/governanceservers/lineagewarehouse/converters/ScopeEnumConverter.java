/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.converters;

import org.odpi.openmetadata.governanceservers.lineagewarehouse.model.Scope;

import java.beans.PropertyEditorSupport;

public class ScopeEnumConverter  extends PropertyEditorSupport {

    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(Scope.fromString(text));
    }

}
