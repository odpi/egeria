/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.users;


/**
 * UserAccountType indicates whether the user account is for a person or an automated process (non-personal account - NPA).
 */
public enum UserAccountType
{
    /**
     * The account is for an individual who is an employee of the organization
     */
    EMPLOYEE,

    /**
     * The account is for an individual who is a contractor to the organization
     */
    CONTRACTOR,


    /**
     * The account is for an individual who is not a part of the organization
     */
    EXTERNAL,

    /**
     * The account is for a system, service, software component - some form of automation.
     */
    DIGITAL
}
