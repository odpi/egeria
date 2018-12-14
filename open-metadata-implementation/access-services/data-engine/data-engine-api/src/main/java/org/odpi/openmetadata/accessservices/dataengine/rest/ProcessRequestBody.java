/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProcessRequestBody extends DataEngineOMASAPIRequestBody {
    private String name;

    private String displayName;

    private List<String> inputs;

    private List<String> outputs;
}
