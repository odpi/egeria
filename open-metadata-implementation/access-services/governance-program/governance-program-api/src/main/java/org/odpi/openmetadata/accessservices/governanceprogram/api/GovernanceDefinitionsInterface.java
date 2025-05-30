/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.frameworks.openmetadata.enums.GovernanceDefinitionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * The GovernanceDefinitionsInterface supports the definition of the governance drivers, policies and controls
 * that define the motivation, goals and implementation approach for the governance program.
 *
 * Governance drivers document of the business strategy and regulations that provide the motivation behind the governance program. They feed
 * into the governance program's policymaking phase ensuring the governance program is focused on activity that delivers value to the organization.
 * A governance driver could be a governance strategy statement, a business imperative, a regulation or a regulation's article.
 *
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
public interface GovernanceDefinitionsInterface
{
    /**
     * Create a new governance definition.  The type of the definition is located in the properties.
     *
     * @param userId calling user
     * @param properties properties of the definition
     * @param initialStatus what is the initial status for the governance definition - default value is DRAFT
     *
     * @return unique identifier of the definition
     *
     * @throws InvalidParameterException typeName, documentIdentifier or userId is null; documentIdentifier is not unique; typeName is not valid
     * @throws PropertyServerException problem accessing the metadata service
     * @throws UserNotAuthorizedException security access problem
     */
    String createGovernanceDefinition(String                         userId,
                                      GovernanceDefinitionProperties properties,
                                      GovernanceDefinitionStatus    initialStatus) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;

    /**
     * Update an existing governance definition.
     *
     * @param userId calling user
     * @param definitionGUID unique identifier of the definition to update
     * @param isMergeUpdate are unspecified properties unchanged (true) or removed?
     * @param properties properties to update
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  updateGovernanceDefinition(String                         userId,
                                     String                         definitionGUID,
                                     boolean                        isMergeUpdate,
                                     GovernanceDefinitionProperties properties) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Update the status of a governance definition
     *
     * @param userId calling user
     * @param definitionGUID unique identifier
     * @param newStatus new status
     *
     * @throws InvalidParameterException guid, status or userId is null; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setGovernanceDefinitionStatus(String                     userId,
                                       String                     definitionGUID,
                                       GovernanceDefinitionStatus newStatus) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Delete a specific governance definition.
     *
     * @param userId calling user
     * @param definitionGUID unique identifier of the definition to remove
     *
     * @throws InvalidParameterException guid is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  deleteGovernanceDefinition(String userId,
                                     String definitionGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Link two related governance definitions together.  The governance definitions are of the same type as follows:
     * <ul>
     *     <li>A relationship of type GovernanceDriverLink is between two GovernanceDriver definitions</li>
     *     <li>A relationship of type GovernancePolicyLink is between two GovernancePolicy definitions</li>
     *     <li>A relationship of type GovernanceControl is between two GovernanceControl definitions</li>
     * </ul>
     * If the link already exists the description is updated.
     *
     * @param userId calling user
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param relationshipTypeName the name of the relationship to create
     * @param properties description of their relationship
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void linkPeerDefinitions(String                   userId,
                             String                   definitionOneGUID,
                             String                   definitionTwoGUID,
                             String                   relationshipTypeName,
                             PeerDefinitionProperties properties) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the link between two definitions.
     *
     * @param userId calling user
     * @param definitionOneGUID unique identifier of the first definition
     * @param definitionTwoGUID unique identifier of the second definition
     * @param relationshipTypeName the name of the relationship to delete
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void unlinkPeerDefinitions(String userId,
                               String definitionOneGUID,
                               String definitionTwoGUID,
                               String relationshipTypeName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Create a link to show that a governance definition supports the requirements of another governance definition.
     * This supporting relationship is between definitions of different types as follows:
     * <ul>
     *     <li>A relationship of type GovernanceResponse is between a GovernanceDriver and a GovernancePolicy</li>
     *     <li>A relationship of type GovernanceImplementation is between a GovernancePolicy and a GovernanceControl</li>
     * </ul>
     * If the link already exists the rationale is updated.
     *
     * @param userId calling user
     * @param definitionGUID unique identifier of the governance definition
     * @param supportingDefinitionGUID unique identifier of the supporting governance definition
     * @param relationshipTypeName the name of the relationship to create
     * @param properties description of how the supporting definition provides support
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void setupSupportingDefinition(String                         userId,
                                   String                         definitionGUID,
                                   String                         supportingDefinitionGUID,
                                   String                         relationshipTypeName,
                                   SupportingDefinitionProperties properties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Remove the supporting link between two governance definitions.
     *
     * @param userId calling user
     * @param definitionGUID unique identifier of the governance definition
     * @param supportingDefinitionGUID unique identifier of the supporting governance definition
     * @param relationshipTypeName the name of the relationship to delete
     *
     * @throws InvalidParameterException one of the guids is null or not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void clearSupportingDefinition(String userId,
                                   String definitionGUID,
                                   String supportingDefinitionGUID,
                                   String relationshipTypeName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;
}
