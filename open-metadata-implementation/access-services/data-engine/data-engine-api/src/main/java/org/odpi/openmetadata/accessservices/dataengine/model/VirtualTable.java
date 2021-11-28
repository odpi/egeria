/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Wrapper class needed for ds-proxy
 */
@EqualsAndHashCode(callSuper = true)
@ToString
@Getter
@Setter
public class VirtualTable extends RelationalTable {
    private  DatabaseSchema databaseSchema;
}
