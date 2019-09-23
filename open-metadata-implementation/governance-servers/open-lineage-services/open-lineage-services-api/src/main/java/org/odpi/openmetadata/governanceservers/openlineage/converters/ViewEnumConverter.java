/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.converters;

import org.odpi.openmetadata.governanceservers.openlineage.model.View;

import java.beans.PropertyEditorSupport;

public class ViewEnumConverter extends PropertyEditorSupport {

    public void setAsText(final String text) throws IllegalArgumentException {
        setValue(View.fromString(text));
    }

}
