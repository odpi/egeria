/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.api.GovernanceDefinitionsInterface;
import org.odpi.openmetadata.accessservices.governanceprogram.client.rest.GovernanceProgramRESTClient;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDefinitionStatus;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.PeerDefinitionProperties;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.SupportingDefinitionProperties;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * GovernanceDefinitionManager is the java client for managing the definitions for the governance drivers, policies and controls
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
public class GovernanceDefinitionManager extends GovernanceProgramBaseClient implements GovernanceDefinitionsInterface
{

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceDefinitionManager(String serverName,
                                       String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceDefinitionManager(String     serverName,
                                       String     serverPlatformURLRoot,
                                       String     userId,
                                       String     password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceDefinitionManager(String   serverName,
                                       String   serverPlatformURLRoot,
                                       int      maxPageSize,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, maxPageSize, auditLog);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceDefinitionManager(String     serverName,
                                       String     serverPlatformURLRoot,
                                       String     userId,
                                       String     password,
                                       int        maxPageSize,
                                       AuditLog   auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, maxPageSize, auditLog);
    }


    /**
     * Create a new client that uses the supplied rest client.  This is typically used when called from another OMAG Server.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST servers
     * @param restClient internal client for rest calls
     * @param maxPageSize pre-initialized parameter limit
     *
     * @throws InvalidParameterException bad input parameters
     */
    public GovernanceDefinitionManager(String                      serverName,
                                       String                      serverPlatformURLRoot,
                                       GovernanceProgramRESTClient restClient,
                                       int                         maxPageSize) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, restClient, maxPageSize);
    }


    /* ========================================
     * Governance Definitions
     */

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
    @Override
    public String createGovernanceDefinition(String                         userId,
                                             GovernanceDefinitionProperties properties,
                                             GovernanceDefinitionStatus     initialStatus) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String   methodName = "createGovernanceDefinition";
        final String   urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-definitions";
        final String   propertiesParameterName = "properties";

        return super.createGovernanceDefinition(userId, properties, propertiesParameterName, initialStatus, urlTemplate, methodName);
    }


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
    @Override
    public void  updateGovernanceDefinition(String                         userId,
                                            String                         definitionGUID,
                                            boolean                        isMergeUpdate,
                                            GovernanceDefinitionProperties properties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "updateGovernanceDefinition";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-definitions/{2}/update?isMergeUpdate={3}";

        final String guidParameterName = "definitionGUID";
        final String propertiesParameterName = "properties";

        super.updateGovernanceDefinition(userId, definitionGUID, guidParameterName, isMergeUpdate, properties, propertiesParameterName, urlTemplate, methodName);
    }


    /**
     * Update the status of a governance definition
     *
     * @param userId calling user
     * @param definitionGUID unique identifier
     * @param newStatus new status
     *
     * @throws InvalidParameterException guid, documentIdentifier or userId is null; documentIdentifier is not unique; guid is not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    @Override
    public void setGovernanceDefinitionStatus(String                     userId,
                                              String                     definitionGUID,
                                              GovernanceDefinitionStatus newStatus) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        final String methodName = "setGovernanceDefinitionStatus";
        final String guidParameterName = "definitionGUID";
        final String propertiesParameterName = "newStatus";

        super.updateGovernanceDefinitionStatus(userId,
                                               definitionGUID,
                                               guidParameterName,
                                               newStatus,
                                               propertiesParameterName,
                                               methodName);
    }


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
    @Override
    public void  deleteGovernanceDefinition(String userId,
                                            String definitionGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "deleteGovernanceDefinition";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-definitions/{2}/delete";
        final String guidParameterName = "definitionGUID";

        super.removeReferenceable(userId, definitionGUID, guidParameterName, urlTemplate, methodName);
    }


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
    @Override
    public void linkPeerDefinitions(String                   userId,
                                    String                   definitionOneGUID,
                                    String                   definitionTwoGUID,
                                    String                   relationshipTypeName,
                                    PeerDefinitionProperties properties) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        final String methodName = "linkPeerDefinitions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-definitions/{2}/peers/{3}/link";

        final String definitionOneGUIDParameterName = "definitionOneGUID";
        final String definitionTwoGUIDParameterName = "definitionTwoGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        super.setupRelationship(userId,
                               definitionOneGUID,
                               definitionOneGUIDParameterName,
                               relationshipTypeName,
                               properties,
                               definitionTwoGUID,
                               definitionTwoGUIDParameterName,
                               urlTemplate,
                               methodName);
    }


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
    @Override
    public void unlinkPeerDefinitions(String userId,
                                      String definitionOneGUID,
                                      String definitionTwoGUID,
                                      String relationshipTypeName) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "unlinkPeerDefinitions";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-definitions/{2}/peers/{3}/unlink";

        final String definitionOneGUIDParameterName = "definitionOneGUID";
        final String definitionTwoGUIDParameterName = "definitionTwoGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        super.clearRelationship(userId,
                                definitionOneGUID,
                                definitionOneGUIDParameterName,
                                relationshipTypeName,
                                definitionTwoGUIDParameterName,
                                definitionTwoGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


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
    @Override
    public void setupSupportingDefinition(String                         userId,
                                          String                         definitionGUID,
                                          String                         supportingDefinitionGUID,
                                          String                         relationshipTypeName,
                                          SupportingDefinitionProperties properties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "setupSupportingDefinition";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-definitions/{2}/supporting-definitions/{3}/link";

        final String definitionOneGUIDParameterName = "definitionGUID";
        final String definitionTwoGUIDParameterName = "supportingDefinitionGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        super.setupRelationship(userId,
                                definitionGUID,
                                definitionOneGUIDParameterName,
                                relationshipTypeName,
                                properties,
                                supportingDefinitionGUID,
                                definitionTwoGUIDParameterName,
                                urlTemplate,
                                methodName);
    }


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
    @Override
    public void clearSupportingDefinition(String userId,
                                          String definitionGUID,
                                          String supportingDefinitionGUID,
                                          String relationshipTypeName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "clearSupportingDefinition";
        final String urlTemplate = serverPlatformURLRoot + "/servers/{0}/open-metadata/access-services/governance-program/users/{1}/governance-definitions/{2}/supporting-definitions/{3}/unlink";

        final String definitionOneGUIDParameterName = "definitionGUID";
        final String definitionTwoGUIDParameterName = "supportingDefinitionGUID";
        final String relationshipTypeNameParameterName = "relationshipTypeName";

        invalidParameterHandler.validateName(relationshipTypeName, relationshipTypeNameParameterName, methodName);

        super.clearRelationship(userId,
                                definitionGUID,
                                definitionOneGUIDParameterName,
                                relationshipTypeName,
                                definitionTwoGUIDParameterName,
                                definitionTwoGUIDParameterName,
                                urlTemplate,
                                methodName);
    }
}
