/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceDefinitionsInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.GovernanceDefinitionHandler;


/**
 * GovernanceDefinitionManager is the java client for managing the definitions for the governance drivers, policies and controls
 * that define the motivation, goals and implementation approach for the governance program.
 * Governance drivers document of the business strategy and regulations that provide the motivation behind the governance program. They feed
 * into the governance program's policymaking phase ensuring the governance program is focused on activity that delivers value to the organization.
 * A governance driver could be a governance strategy statement, a business imperative, a regulation or a regulation's article.
 * Governance policies define the goals and best practices for the governance program.  There are three types of governance policies:
 * <ul>
 *     <li>
 *         Governance Principles define the invariants that the organization tries to maintain.
 *     </li>
 *     <li>
 *         Governance Obligations define the requirements coming from regulations and policy makers of the organization.
 *     </li>
 *     <li>
 *         Governance Approaches describe preferred approaches and methods to follow
 *     </li>
 * </ul>
 * Within the definition of each governance policy is a description of what the policy is trying to achieve
 * along with the implications to the organization's operation when they adopt this.
 * These implications help to estimate the cost of the policy's implementation and the activities that need to happen.
 *
 * The governance definitions that define how the governance program is to be implemented.
 * There are two types of governance definitions:
 * <ul>
 *     <li>
 *         Technical Controls define the use of technology to implement governance definitions.  They consist of either:
 *         <ul>
 *             <li>
 *                 GovernanceRule - a rule that need to be enforced to support a requirement of the governance program.
 *             </li>
 *             <li>
 *                 GovernanceProcess - a series of automated steps that need to run to support a requirement of the governance program.
 *             </li>
 *         </ul>
 *     </li>
 *     <li>
 *         Organizational controls define roles, teams and manual procedures that implement an aspect of governance. They consist of either:
 *         <ul>
 *             <li>
 *                 GovernanceResponsibility - a set of responsibilities that can be associated with a governance role
 *             </li>
 *             <li>
 *                 GovernanceProcedure - an manual procedure
 *             </li>
 *         </ul>
 *     </li>
 * </ul>
 * Within the definition of each governance definition is a description of what the control is trying to achieve
 * along with the implications to the organization's operation when they adopt this.
 * These implications help to estimate the cost of the control's implementation and the activities that need to happen.
 */
public class GovernanceDefinitionManager extends GovernanceDefinitionHandler
{
    /**
     * Create a new client.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param accessServiceURLMarker optional access service URL marker used to identify which back end service to call
     * @param auditLog logging destination
     * @param maxPageSize maximum value allowed for page size
     * @param serviceName name of calling service
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceDefinitionManager(String   localServerName,
                                       String   serverName,
                                       String   serverPlatformURLRoot,
                                       AuditLog auditLog,
                                       String   accessServiceURLMarker,
                                       String   serviceName,
                                       int      maxPageSize) throws InvalidParameterException
    {
        super(localServerName, serverName, serverPlatformURLRoot, auditLog, accessServiceURLMarker, serviceName, maxPageSize);
    }
}
