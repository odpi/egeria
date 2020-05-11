/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.responses;

import java.util.Arrays;

import org.odpi.openmetadata.accessservices.cognos.model.ResponseContainerModule;

public class ModuleResponse extends CognosOMASAPIResponse {

	public void setModule(ResponseContainerModule module) {
        this.setData(Arrays.asList(module));
    }

}
