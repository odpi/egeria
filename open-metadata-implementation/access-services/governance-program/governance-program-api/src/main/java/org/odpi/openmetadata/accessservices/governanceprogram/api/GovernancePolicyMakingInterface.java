/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;


import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernancePrincipleProperties;

import java.util.List;

/**
 * The GovernancePolicyMakingInterface supports the definition of the governance policies that define the goals
 * and best practices for the governance program.  There are three types of governance policies:
 * <ul>
 *     <li>
 *         Governance Principles define the invariants that the organization tries to maintain.
 *     </li>
 *     <li>
 *         Governance
 *     </li>
 * </ul>
 * Within the definition of each governance policy is a description of what the policy is trying to achieve
 * along with the implications to the organization's operation when they adopt this.
 * These implications help to estimate the cost of the policy's implementation and the activities that need to happen.
 */
public interface GovernancePolicyMakingInterface
{

    String createGovernancePrinciple();
    void  updateGovernancePrinciple();
    void  deleteGovernancePrinciple();

    List<String>  getGovernancePrinciples();

    GovernancePrincipleProperties getGovernancePrinciple();

}
