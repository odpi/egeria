/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.iterator;


/**
 * Return a recommended action based on the state of the elements.
 */
public enum MemberAction
{
    CREATE_INSTANCE_IN_THIRD_PARTY,
    UPDATE_INSTANCE_IN_THIRD_PARTY,
    DELETE_INSTANCE_IN_THIRD_PARTY,
    CREATE_INSTANCE_IN_OPEN_METADATA,
    UPDATE_INSTANCE_IN_OPEN_METADATA,
    DELETE_INSTANCE_IN_OPEN_METADATA,

    NO_ACTION,

    UNKNOWN_ACTION,

    ;


}
