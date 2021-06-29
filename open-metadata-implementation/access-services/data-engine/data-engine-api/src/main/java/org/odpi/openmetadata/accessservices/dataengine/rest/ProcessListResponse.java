/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.rest;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;

import java.util.List;

/**
 * ProcessListResponse is the response structure used on the DE OMAS REST API calls that return a
 * list of process unique identifiers (guids) as a response.
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class ProcessListResponse extends GUIDListResponse {

    private List<String> failedGUIDs;

}
