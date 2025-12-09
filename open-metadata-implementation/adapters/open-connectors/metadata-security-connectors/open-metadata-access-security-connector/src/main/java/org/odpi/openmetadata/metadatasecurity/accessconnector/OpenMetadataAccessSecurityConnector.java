/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.accessconnector;


import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.AccessOperation;
import org.odpi.openmetadata.frameworks.connectors.properties.users.NamedList;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccount;
import org.odpi.openmetadata.frameworks.connectors.properties.users.UserAccountType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.*;
import org.odpi.openmetadata.metadatasecurity.accessconnector.controls.OpenMetadataSecurityConfigurationProperty;
import org.odpi.openmetadata.metadatasecurity.accessconnector.ffdc.MetadataSecurityAuditCode;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.text.MessageFormat;
import java.util.*;


/**
 * OpenMetadataAccessSecurityConnector provides a specific security connector for Egeria's runtime
 * users that overrides the default behavior of the default open metadata security connectors that does
 * not allow any access to anything.  It provides a demonstration of how to implement a security connector that uses
 * an external secrets store.
 */
public class OpenMetadataAccessSecurityConnector extends OpenMetadataSecurityConnector implements OpenMetadataPlatformSecurity,
                                                                                                  OpenMetadataRepositorySecurity,
                                                                                                  OpenMetadataServerSecurity,
                                                                                                  OpenMetadataServiceSecurity,
                                                                                                  OpenMetadataElementSecurity,
                                                                                                  OpenMetadataUserSecurity
{



    /*
     * Policies
     */

    private String serverServiceGroupPattern          = OpenMetadataSecurityConfigurationProperty.SERVER_SERVICE_GROUP_NAME_PATTERN.getDefaultValue();
    private String serverServiceOperationGroupPattern = OpenMetadataSecurityConfigurationProperty.SERVER_SERVICE_OPERATION_GROUP_NAME_PATTERN.getDefaultValue();
    private String elementGroupPattern                = OpenMetadataSecurityConfigurationProperty.ELEMENT_GROUP_NAME_PATTERN.getDefaultValue();
    private String ownershipGroupPattern              = OpenMetadataSecurityConfigurationProperty.OWNER_GROUP_NAME_PATTERN.getDefaultValue();
    private String zoneGroupPattern                   = OpenMetadataSecurityConfigurationProperty.ZONE_GROUP_NAME_PATTERN.getDefaultValue();
    private String serverGroup                        = OpenMetadataSecurityConfigurationProperty.SERVER_GROUP_NAME_PATTERN.getDefaultValue();

    private String serverAdministratorsGroup = OpenMetadataSecurityConfigurationProperty.SERVER_ADMINISTRATOR_GROUP.getDefaultValue();
    private String serverOperatorsGroup      = OpenMetadataSecurityConfigurationProperty.SERVER_OPERATORS_GROUP.getDefaultValue();
    private String serverInvestigatorsGroup  = OpenMetadataSecurityConfigurationProperty.SERVER_INVESTIGATORS_GROUP.getDefaultValue();
    private String dynamicTypeAuthorGroup    = OpenMetadataSecurityConfigurationProperty.DYNAMIC_TYPE_AUTHOR_GROUP.getDefaultValue();
    private String instanceHeaderAuthorGroup = OpenMetadataSecurityConfigurationProperty.INSTANCE_HEADER_AUTHOR_GROUP.getDefaultValue();


    private String personalZonesPolicyGroup  = OpenMetadataSecurityConfigurationProperty.PERSONAL_ZONES_GROUP.getDefaultValue();
    private String stewardshipZonePolicyGroup = OpenMetadataSecurityConfigurationProperty.STEWARDSHIP_ZONES_GROUP.getDefaultValue();

    private String allUserZonesAccessGroup     = OpenMetadataSecurityConfigurationProperty.ALL_USER_ZONES_GROUP.getDefaultValue();
    private String allEmployeeZonesAccessGroup = OpenMetadataSecurityConfigurationProperty.EMPLOYEE_ONLY_ZONES_GROUP.getDefaultValue();
    private String nonExternalZonesAccessGroup = OpenMetadataSecurityConfigurationProperty.NOT_EXTERNAL_ZONES_GROUP.getDefaultValue();
    private String dataLakeZonesAccessGroup    = OpenMetadataSecurityConfigurationProperty.READABLE_ZONES_GROUP.getDefaultValue();
    private String automatedZonesAccessGroup   = OpenMetadataSecurityConfigurationProperty.AUTOMATED_ZONES_GROUP.getDefaultValue();


    /**
     * Constructor sets up the security policies
     */
    public OpenMetadataAccessSecurityConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        serverServiceGroupPattern = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_SERVICE_GROUP_NAME_PATTERN.getName(),
                                                                         connectionBean.getConfigurationProperties(),
                                                                         OpenMetadataSecurityConfigurationProperty.SERVER_SERVICE_GROUP_NAME_PATTERN.getDefaultValue());

        serverServiceOperationGroupPattern = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_SERVICE_OPERATION_GROUP_NAME_PATTERN.getName(),
                                                                                  connectionBean.getConfigurationProperties(),
                                                                                  OpenMetadataSecurityConfigurationProperty.SERVER_SERVICE_OPERATION_GROUP_NAME_PATTERN.getDefaultValue());

        ownershipGroupPattern = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.OWNER_GROUP_NAME_PATTERN.getName(),
                                                                     connectionBean.getConfigurationProperties(),
                                                                     OpenMetadataSecurityConfigurationProperty.OWNER_GROUP_NAME_PATTERN.getDefaultValue());

        elementGroupPattern = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.ELEMENT_GROUP_NAME_PATTERN.getName(),
                                                                   connectionBean.getConfigurationProperties(),
                                                                   OpenMetadataSecurityConfigurationProperty.ELEMENT_GROUP_NAME_PATTERN.getDefaultValue());

        zoneGroupPattern = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.ZONE_GROUP_NAME_PATTERN.getName(),
                                                                connectionBean.getConfigurationProperties(),
                                                                OpenMetadataSecurityConfigurationProperty.ZONE_GROUP_NAME_PATTERN.getDefaultValue());

        serverAdministratorsGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_ADMINISTRATOR_GROUP.getName(),
                                                                         connectionBean.getConfigurationProperties(),
                                                                         OpenMetadataSecurityConfigurationProperty.SERVER_ADMINISTRATOR_GROUP.getDefaultValue());

        serverOperatorsGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_OPERATORS_GROUP.getName(),
                                                                    connectionBean.getConfigurationProperties(),
                                                                    OpenMetadataSecurityConfigurationProperty.SERVER_OPERATORS_GROUP.getDefaultValue());

        serverInvestigatorsGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_INVESTIGATORS_GROUP.getName(),
                                                                        connectionBean.getConfigurationProperties(),
                                                                        OpenMetadataSecurityConfigurationProperty.SERVER_INVESTIGATORS_GROUP.getDefaultValue());

        serverGroup = resolveServerGroupName(super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_GROUP_NAME_PATTERN.getName(),
                                                                                  connectionBean.getConfigurationProperties(),
                                                                                  OpenMetadataSecurityConfigurationProperty.SERVER_GROUP_NAME_PATTERN.getDefaultValue()),
                                             serverName);

        dynamicTypeAuthorGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.DYNAMIC_TYPE_AUTHOR_GROUP.getName(),
                                                                      connectionBean.getConfigurationProperties(),
                                                                      OpenMetadataSecurityConfigurationProperty.DYNAMIC_TYPE_AUTHOR_GROUP.getDefaultValue());

        instanceHeaderAuthorGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.INSTANCE_HEADER_AUTHOR_GROUP.getName(),
                                                                         connectionBean.getConfigurationProperties(),
                                                                         OpenMetadataSecurityConfigurationProperty.INSTANCE_HEADER_AUTHOR_GROUP.getDefaultValue());

        /*
         * The policy groups add simple policies to the zones - to reduce the need to list all users under certain groups.
         */
        personalZonesPolicyGroup   = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.PERSONAL_ZONES_GROUP.getName(),
                                                                          connectionBean.getConfigurationProperties(),
                                                                          OpenMetadataSecurityConfigurationProperty.PERSONAL_ZONES_GROUP.getDefaultValue());
        stewardshipZonePolicyGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.STEWARDSHIP_ZONES_GROUP.getName(),
                                                                          connectionBean.getConfigurationProperties(),
                                                                          OpenMetadataSecurityConfigurationProperty.STEWARDSHIP_ZONES_GROUP.getDefaultValue());

        allUserZonesAccessGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.ALL_USER_ZONES_GROUP.getName(),
                                                                       connectionBean.getConfigurationProperties(),
                                                                       OpenMetadataSecurityConfigurationProperty.ALL_USER_ZONES_GROUP.getDefaultValue());
        allEmployeeZonesAccessGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.EMPLOYEE_ONLY_ZONES_GROUP.getName(),
                                                                           connectionBean.getConfigurationProperties(),
                                                                           OpenMetadataSecurityConfigurationProperty.EMPLOYEE_ONLY_ZONES_GROUP.getDefaultValue());
        dataLakeZonesAccessGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.READABLE_ZONES_GROUP.getName(),
                                                                        connectionBean.getConfigurationProperties(),
                                                                        OpenMetadataSecurityConfigurationProperty.READABLE_ZONES_GROUP.getDefaultValue());
        automatedZonesAccessGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.AUTOMATED_ZONES_GROUP.getName(),
                                                                         connectionBean.getConfigurationProperties(),
                                                                         OpenMetadataSecurityConfigurationProperty.AUTOMATED_ZONES_GROUP.getDefaultValue());
        nonExternalZonesAccessGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.NOT_EXTERNAL_ZONES_GROUP.getName(),
                                                                           connectionBean.getConfigurationProperties(),
                                                                           OpenMetadataSecurityConfigurationProperty.NOT_EXTERNAL_ZONES_GROUP.getDefaultValue());
    }


    /**
     * Retrieve information about a specific user.  This is used during a user's request for a token
     *
     * @param userId calling user
     * @return security properties about the user
     * @throws UserNotAuthorizedException user not recognized - or supplied an incorrect password
     */
    public OpenMetadataUserAccount getUserAccount(String userId) throws UserNotAuthorizedException
    {
        final String methodName = "getUserAccount";

        if (secretsStoreConnectorMap != null)
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        UserAccount userAccount = secretsStoreConnector.getUser(userId);

                        if (userAccount != null)
                        {
                            return new OpenMetadataUserAccount(userId, userAccount);
                        }
                    }
                    catch (ConnectorCheckedException error)
                    {
                        throwUnknownUser(userId, error, methodName);
                    }
                }
            }
        }

        throwUnknownUser(userId, null, methodName);
        return null; // not reached
    }


    /**
     * Return a group name using a pattern.
     *
     * @param pattern pattern to use
     * @param name name of resource
     * @return formatted name or null which means ignore
     */
    protected String resolveServerGroupName(String pattern,
                                            String name)
    {
        if (pattern != null)
        {
            return MessageFormat.format(pattern, name);
        }

        return null;
    }


    /**
     * Return a group name using a pattern.
     *
     * @param pattern pattern to use
     * @param serviceName  name of service
     * @param operationName requested operation
     * @return formatted name or null which means ignore
     */
    protected String resolveServiceGroupName(String pattern,
                                             String serviceName,
                                             String operationName)
    {
        if (pattern != null)
        {
            return MessageFormat.format(pattern, serviceName, operationName);
        }

        return null;
    }


    /**
     * Return a group name using a pattern.
     *
     * @param pattern pattern to use
     * @param qualifiedName qualified name of resource
     * @param operationName requested operation
     * @return formatted name or null which means ignore
     */
    protected String resolveElementGroupName(String pattern,
                                             String qualifiedName,
                                             String operationName)
    {
        if (pattern != null)
        {
            return MessageFormat.format(pattern, qualifiedName, operationName);
        }

        return null;
    }


    /**
     * Return a group name using a pattern.
     *
     * @param pattern pattern to use
     * @param classifications classifications associated with an element
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return formatted name or null which means ignore
     */
    protected String resolveOwnershipGroupName(String               pattern,
                                               List<Classification> classifications,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName,
                                               String               methodName)
    {
        if (pattern != null)
        {
            InstanceProperties ownershipProperties = repositoryHelper.getClassificationProperties(serviceName,
                                                                                                  classifications,
                                                                                                  OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                                                                                  methodName);

            if (ownershipProperties != null)
            {
                return MessageFormat.format(pattern,
                                            repositoryHelper.getStringProperty(serviceName, OpenMetadataProperty.OWNER.name, ownershipProperties, methodName),
                                            repositoryHelper.getStringProperty(serviceName, OpenMetadataProperty.OWNER_TYPE_NAME.name, ownershipProperties, methodName),
                                            repositoryHelper.getStringProperty(serviceName, OpenMetadataProperty.OWNER_PROPERTY_NAME.name, ownershipProperties, methodName));
            }
        }

        return null;
    }


    /**
     * Return a group name using a pattern.
     *
     * @param pattern pattern to use
     * @param serverName  name of server
     * @param serviceName  name of service
     * @param operationName requested operation
     * @return formatted name
     */
    protected String resolveServerServiceOperationGroupName(String pattern,
                                                            String serverName,
                                                            String serviceName,
                                                            String operationName)
    {
        if (pattern != null)
        {
            return MessageFormat.format(pattern, serverName, serviceName, operationName);
        }

        return null;
    }


    /**
     * This method does the lookup of the user and group in the secrets store.
     *
     * @param userId calling user
     * @param groupName group that they should be a member of
     * @return boolean indicating that they are not members of the group
     * @throws UserNotAuthorizedException bad user id
     */
    protected boolean validateUserInGroup(String userId,
                                          String groupName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);

        if (userAccount != null)
        {
            return validateUserInGroup(userAccount, groupName);
        }

        return false;
    }


    /**
     * This method does the lookup of the user and group in the secrets store.
     *
     * @param userAccount calling user
     * @param groupName group that they should be a member of
     * @return boolean indicating that they are not members of the group
     */
    protected boolean validateUserInGroup(OpenMetadataUserAccount userAccount,
                                          String                  groupName)
    {
        final String methodName = "validateUserInGroup";

        if ((userAccount != null) && (secretsStoreConnectorMap != null) && (groupName != null))
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        if ((userAccount.getSecurityGroups() != null) && (userAccount.getSecurityGroups().contains(groupName)))
                        {
                            return true;
                        }

                        NamedList securityGroup = secretsStoreConnector.getNamedList(groupName);

                        if ((securityGroup != null) && (securityGroup.getUserMembers() != null))
                        {
                            if ((checkNameInGroup(userAccount.getUserId(), securityGroup)) ||
                                    (checkNameInGroup(userAccount.getDistinguishedName(), securityGroup)))
                            {
                                return true;
                            }

                            if (userAccount.getSecurityRoles() != null)
                            {
                                for (String securityRole : userAccount.getSecurityRoles())
                                {
                                    if (securityRole != null)
                                    {
                                        if (checkNameInGroup(securityRole, securityGroup))
                                        {
                                            return true;
                                        }
                                    }

                                    NamedList securityRoleList = secretsStoreConnector.getNamedList(securityRole);

                                    if ((securityRoleList != null) && (securityRoleList.getDistinguishedName() != null))
                                    {
                                        if (checkNameInGroup(securityRoleList.getDistinguishedName(), securityGroup))
                                        {
                                            return true;
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            /*
                             * No special group defined so no restrictions
                             */
                            return true;
                        }
                    }
                    catch (Exception secretsStoreError)
                    {
                        super.logRecord(methodName,
                                        MetadataSecurityAuditCode.SECRETS_STORE_EXCEPTION.getMessageDefinition(serverName,
                                                                                                               secretsStoreError.getClass().getName(),
                                                                                                               userAccount.getUserId(),
                                                                                                               groupName,
                                                                                                               secretsStoreError.getMessage()));
                    }
                }
            }
        }

        return false;
    }


    /**
     * This method performs the nested look up of a user's name in the groups.
     *
     * @param name name from the user account
     * @param list list of names
     * @return boolean
     */
    protected boolean checkNameInGroup(String    name,
                                       NamedList list)
    {
        final String methodName = "checkNameInGroup";

        if ((name != null) && (list != null))
        {
            if ((list.getUserMembers() != null) && (list.getUserMembers().contains(name)))
            {
                return true;
            }

            if (list.getListMembers() != null)
            {
                for (String listMember : list.getListMembers())
                {
                    if (listMember != null)
                    {
                        if (secretsStoreConnectorMap != null)
                        {
                            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
                            {
                                if (secretsStoreConnector != null)
                                {
                                    try
                                    {
                                        NamedList listMemberList = secretsStoreConnector.getNamedList(listMember);

                                        if (checkNameInGroup(name, listMemberList))
                                        {
                                            return true;
                                        }
                                    }
                                    catch (Exception secretsStoreError)
                                    {
                                        super.logRecord(methodName,
                                                        MetadataSecurityAuditCode.SECRETS_STORE_EXCEPTION.getMessageDefinition(serverName,
                                                                                                                               secretsStoreError.getClass().getName(),
                                                                                                                               name,
                                                                                                                               list.getDisplayName(),
                                                                                                                               secretsStoreError.getMessage()));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this platform
     */
    public void  validateUserForNewServer(String userId) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, serverAdministratorsGroup))
        {
            return;
        }

        super.validateUserForNewServer(userId);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this platform
     */
    public void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, serverOperatorsGroup))
        {
            return;
        }

        super.validateUserAsOperatorForPlatform(userId);
    }


    /**
     * Check that the calling user is authorized to issue diagnostic requests to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this platform
     */
    public void  validateUserAsInvestigatorForPlatform(String userId) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, serverInvestigatorsGroup))
        {
            return;
        }

        super.validateUserAsInvestigatorForPlatform(userId);
    }


    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    @Override
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, serverGroup))
        {
            return;
        }

        super.validateUserForServer(userId);
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    @Override
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, serverAdministratorsGroup))
        {
            return;
        }

        super.validateUserAsServerAdmin(userId);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    @Override
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, serverOperatorsGroup))
        {
            return;
        }

        super.validateUserAsServerOperator(userId);
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    @Override
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, serverInvestigatorsGroup))
        {
            return;
        }

        super.validateUserAsServerInvestigator(userId);
    }


    /**
     * Check that the calling user is authorized to issue this request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForService(String userId,
                                        String serviceName) throws UserNotAuthorizedException
    {
        String groupName = this.resolveServiceGroupName(serverServiceGroupPattern,
                                                        serverName,
                                                        serviceName);

        if (this.validateUserInGroup(userId, groupName))
        {
            return;
        }

        super.validateUserForService(userId, serviceName);
    }


    /**
     * Check that the calling user is authorized to issue this specific request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     * @param serviceOperationName name of called operation
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException
    {
        String groupName = this.resolveServerServiceOperationGroupName(serverServiceOperationGroupPattern,
                                                                       serverName,
                                                                       serviceName,
                                                                       serviceOperationName);

        if (this.validateUserInGroup(userId, groupName))
        {
            return;
        }

        super.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
    }


    /**
     * Check to see if any of the zones have a specific policy.
     *
     * @param zoneName list of zones to test
     * @param policyGroupName policy group to look in
     * @return boolean
     */
    private boolean isZoneInPolicyGroup(String zoneName,
                                        String policyGroupName)
    {
        final String methodName = "isZoneInPolicyGroup";

        if ((zoneName != null) && (secretsStoreConnectorMap != null))
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        NamedList policyGroup = secretsStoreConnector.getNamedList(policyGroupName);

                        if (policyGroup != null)
                        {
                            List<String> policyGroupZones = policyGroup.getListMembers();

                            if (policyGroupZones != null)
                            {
                                if (policyGroupZones.contains(zoneName))
                                {
                                    return true;
                                }
                            }
                        }
                    }
                    catch (Exception secretsStoreError)
                    {
                        super.logRecord(methodName,
                                        MetadataSecurityAuditCode.SECRETS_STORE_EXCEPTION.getMessageDefinition(serverName,
                                                                                                               secretsStoreError.getClass().getName(),
                                                                                                               zoneName,
                                                                                                               policyGroupName,
                                                                                                               secretsStoreError.getMessage()));

                    }
                }
            }
        }

        return false;
    }


    /**
     * Tests for whether a specific user should have access to an element based on its zones.
     * Various tests are tried - only one has to be successful.  However, the user must have
     * a user account.
     *
     * @param userAccount details of user
     * @param elementZones name of the zones
     * @param zoneAccessType is the asset to be changed
     * @param isUserOwner is the user one of the owner's of the element
     * @param elementCreator who created the element
     * @return whether the user is authorized to access asset in these zones
     */
    private boolean userHasAccessToZones(OpenMetadataUserAccount userAccount,
                                         List<String>            elementZones,
                                         AccessOperation         zoneAccessType,
                                         String                  elementCreator,
                                         boolean                 isUserOwner) throws UserNotAuthorizedException
    {
        if (userAccount != null)
        {
            /*
             * No zones set up so zones do not restrict or provide access
             */
            if ((elementZones == null) || (elementZones.isEmpty()))
            {
                return false;
            }
            else
            {
                /*
                 * If the zone is listed in the user account, the user has the specified access to the assets in the zone.
                 */
                if (userAccount.getZoneAccess() != null)
                {
                    for (String zoneName : elementZones)
                    {
                        if (zoneName != null)
                        {
                            if (userAccount.getZoneAccess().containsKey(zoneName))
                            {
                                List<AccessOperation> zoneAccessTypes = userAccount.getZoneAccess().get(zoneName);

                                if ((zoneAccessTypes != null) && (zoneAccessTypes.contains(zoneAccessType)))
                                {
                                    if (checkZonePolicies(zoneName,
                                                          userAccount,
                                                          elementCreator,
                                                          isUserOwner))
                                    {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                 * If the zone has specific group, check whether the user is in the group. Notice that the group
                 * name optionally includes both the zone name and the type of operation.
                 */
                for (String zoneName : elementZones)
                {
                    if (zoneName != null)
                    {
                        String zoneGroupName = this.resolveElementGroupName(zoneGroupPattern, zoneName, zoneAccessType.getName());

                        if (validateUserInGroup(userAccount, zoneGroupName))
                        {
                            if (checkZonePolicies(zoneName,
                                                  userAccount,
                                                  elementCreator,
                                                  isUserOwner))
                            {
                                return true;
                            }
                        }

                        /*
                         * The user has no explicit access to the asset's zone so check the access groups.
                         */
                        if (checkZoneAccess(zoneName, zoneAccessType, userAccount))
                        {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * The following policies further restrict which elements in a zone the user has access to.
     * When this method is called, it is after the connector has established that the user
     * has access to the zone and operation.  These checks ensure the user has access to the
     * specific instance.
     *
     * @param zoneName name of the zone
     * @param userAccount details of the user
     * @param elementCreator who created the element
     * @param isUserOwner who is the element owner
     * @return boolean indicating whether the user has access to the asset instance
     */
    private boolean checkZonePolicies(String                  zoneName,
                                      OpenMetadataUserAccount userAccount,
                                      String                  elementCreator,
                                      boolean                 isUserOwner)
    {
        if (isZoneInPolicyGroup(zoneName, personalZonesPolicyGroup))
        {
            if (! userAccount.getUserId().equals(elementCreator))
            {
                return false;
            }
        }
        if (isZoneInPolicyGroup(zoneName, stewardshipZonePolicyGroup))
        {
            if (isUserOwner)
            {
                return true;
            }
        }

        /*
         * No special polices apply.
         */
        return true;
    }


    /**
     * Check whether the caller has read access to a zone by way of an access group.
     *
     * @param zoneName zone to test
     * @param zoneAccessType type of requested access
     * @param userAccount calling user
     * @return boolean to indicate whether the user does have read access
     */
    private boolean checkZoneAccess(String                  zoneName,
                                    AccessOperation         zoneAccessType,
                                    OpenMetadataUserAccount userAccount)
    {
        if (zoneAccessType == AccessOperation.READ)
        {
            if (isZoneInPolicyGroup(zoneName, allUserZonesAccessGroup))
            {
                return true;
            }
            if (isZoneInPolicyGroup(zoneName, allEmployeeZonesAccessGroup))
            {
                if (userAccount.getAccountType() == UserAccountType.EMPLOYEE)
                {
                    return true;
                }
            }
            if (isZoneInPolicyGroup(zoneName, automatedZonesAccessGroup))
            {
                if (userAccount.getAccountType() == UserAccountType.DIGITAL)
                {
                    return true;
                }
            }
            if (isZoneInPolicyGroup(zoneName, dataLakeZonesAccessGroup))
            {
                if ((userAccount.getAccountType() == UserAccountType.EMPLOYEE) ||
                    (userAccount.getAccountType() == UserAccountType.CONTRACTOR) ||
                    (userAccount.getAccountType() == UserAccountType.EXTERNAL))
                {
                    return true;
                }
            }
            if (isZoneInPolicyGroup(zoneName, nonExternalZonesAccessGroup))
            {
                return (userAccount.getAccountType() == UserAccountType.EMPLOYEE) ||
                        (userAccount.getAccountType() == UserAccountType.CONTRACTOR);
            }
        }

        /*
         * No special accesses apply.
         */
        return false;
    }


    /**
     * Determine the appropriate setting for the governance zones.
     *
     * @param defaultZoneSetting default setting of the supported zones
     * @param accessOperation operation to perform
     * @param userId name of the user
     *
     * @return list of supported zones for the user
     * @throws UserNotAuthorizedException unknown user
     */
    private List<String> getZonesForUser(List<String>    defaultZoneSetting,
                                         AccessOperation accessOperation,
                                         String          userId) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);

        HashSet<String> returnZones = new HashSet<>();

        if (defaultZoneSetting != null)
        {
            returnZones.addAll(defaultZoneSetting);
        }

        if ((userAccount != null) && (userAccount.getZoneAccess() != null))
        {
            for (String zoneName : userAccount.getZoneAccess().keySet())
            {
                List<AccessOperation> allowedAccess = userAccount.getZoneAccess().get(zoneName);

                if ((allowedAccess != null) && (allowedAccess.contains(accessOperation)))
                {
                    returnZones.add(zoneName);
                }
            }
        }

        if ((defaultZoneSetting == null) && (returnZones.isEmpty()))
        {
            return null;
        }

        return new ArrayList<>(returnZones);
    }


    /**
     * Determine the appropriate setting for the supported zones depending on the user and the
     * default supported zones set up for the service.  This is called whenever an element is accessed.
     *
     * @param supportedZones default setting of the supported zones
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of supported zones for the user
     * @throws UserNotAuthorizedException unknown user
     */
    @Override
    public List<String> getSupportedZonesForUser(List<String>  supportedZones,
                                                 String        typeName,
                                                 String        serviceName,
                                                 String        userId) throws UserNotAuthorizedException
    {
        return this.getZonesForUser(supportedZones, AccessOperation.READ, userId);
    }


    /**
     * Determine the appropriate setting for the default zones depending on the user and the
     * default zones set up for the service.  This is called whenever an element is created.
     *
     * @param initialZones default setting of the default zones
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of default zones for the user
     * @throws UserNotAuthorizedException unknown user
     */
    @Override
    public List<String> getDefaultZonesForUser(List<String> initialZones,
                                               String       typeName,
                                               String       serviceName,
                                               String       userId) throws UserNotAuthorizedException
    {
        return this.getZonesForUser(initialZones, AccessOperation.CREATE, userId);
    }


    /**
     * Determine the appropriate setting for the default zones depending on the user and the
     * default publish zones set up for the service.  This is called whenever an element is published.
     *
     * @param currentZones default setting of the published zones
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of published zones for the user
     * @throws UserNotAuthorizedException unknown user
     */
    @Override
    public List<String> getPublishZonesForUser(List<String> currentZones,
                                               String       typeName,
                                               String       serviceName,
                                               String       userId) throws UserNotAuthorizedException
    {
        return this.getZonesForUser(currentZones, AccessOperation.PUBLISH, userId);
    }


    /**
     * Determine the appropriate setting for the zones depending on the user and the
     * current zones set up for the element.  This is called whenever an element is withdrawn.
     *
     * @param currentZones default setting of the default zones
     * @param typeName type of the element
     * @param serviceName name of the called service
     * @param userId name of the user
     *
     * @return list of published zones for the user
     * @throws UserNotAuthorizedException unknown user
     */
    @Override
    public List<String> getWithdrawZonesForUser(List<String>  currentZones,
                                                String        typeName,
                                                String        serviceName,
                                                String        userId) throws UserNotAuthorizedException
    {
        List<String> publishZones = this.getZonesForUser(null, AccessOperation.PUBLISH, userId);
        HashSet<String> withdrawZones = new HashSet<>();

        if (currentZones != null)
        {
            withdrawZones.addAll(currentZones);
        }

        /*
         * Only remove the publishZones in order to preserve any additional zones added by the original publishing user.
         */
        if (publishZones != null)
        {
            publishZones.forEach(withdrawZones::remove);
        }

        return new ArrayList<>(withdrawZones);
    }


    /*============================================================================
     * OpenMetadataElementSecurity assures the access to open metadata elements.
     * There is one set of methods for unanchored elements an one for anchors and their members.
     */


    /**
     * Determine whether the security tags provide access to the element
     *
     * @param entityGUID unique identifier of entity
     * @param entityTypeName type of entity
     * @param classifications list of classifications from entity
     * @param userAccount calling user
     * @param operation operation that they wish to perform
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return true=the user has access; false=no information on access for this user
     * @throws UserNotAuthorizedException the user is not authorized for this type of access
     */
    protected boolean validateSecurityTagAccess(String                  entityGUID,
                                                String                  entityTypeName,
                                                List<Classification>    classifications,
                                                OpenMetadataUserAccount userAccount,
                                                AccessOperation         operation,
                                                OMRSRepositoryHelper    repositoryHelper,
                                                String                  serviceName,
                                                String                  methodName) throws UserNotAuthorizedException
    {
        if (classifications != null)
        {
            InstanceProperties securityTags = repositoryHelper.getClassificationProperties(serviceName,
                                                                                           classifications,
                                                                                           OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                                                                           methodName);

            if (securityTags != null)
            {
                Map<String, List<String>> accessGroups = repositoryHelper.getStringArrayStringMapFromProperty(serviceName,
                                                                                                              OpenMetadataProperty.ACCESS_GROUPS.name,
                                                                                                              securityTags,
                                                                                                              methodName);

                if (accessGroups != null)
                {
                    List<String> validUsers = accessGroups.get(operation.getName());

                    if (validUsers != null)
                    {
                        if (! validUsers.contains(userAccount.getUserId()))
                        {
                            throwUnauthorizedElementAccess(userAccount.getUserId(), operation.getName(), entityGUID, entityTypeName, methodName);
                        }
                    }
                }
            }
        }

        return false;
    }


    /**
     * Determine whether the security tags provide access to the element
     *
     * @param entityGUID unique identifier of entity
     * @param entityTypeName type of entity
     * @param classifications list of classifications from entity
     * @param userAccount calling user
     * @param createdBy original creator of the entity
     * @param isUserOwner is the user an owner
     * @param operation operation that they wish to perform
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return true=the user has access; false=no information on access for this user
     * @throws UserNotAuthorizedException the user is not authorized for this type of access
     */
    protected boolean validateZoneAccess(String                  entityGUID,
                                         String                  entityTypeName,
                                         List<Classification>    classifications,
                                         OpenMetadataUserAccount userAccount,
                                         String                  createdBy,
                                         boolean                 isUserOwner,
                                         AccessOperation         operation,
                                         OMRSRepositoryHelper    repositoryHelper,
                                         String                  serviceName,
                                         String                  methodName) throws UserNotAuthorizedException
    {
        if (repositoryHelper.isTypeOf(serviceName, entityTypeName, OpenMetadataType.OPEN_METADATA_ROOT.typeName))
        {
            InstanceProperties zoneMembershipProperties = repositoryHelper.getClassificationProperties(serviceName,
                                                                                                       classifications,
                                                                                                       OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                       methodName);

            if (zoneMembershipProperties != null)
            {
                List<String> zoneMembership = repositoryHelper.getStringArrayProperty(serviceName,
                                                                                      OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                                      zoneMembershipProperties,
                                                                                      methodName);

                if (zoneMembership != null)
                {
                    return userHasAccessToZones(userAccount,
                                                zoneMembership,
                                                operation,
                                                createdBy,
                                                isUserOwner);
                }
            }
        }

        return false;
    }



    /**
     * Determine whether the classifications provide access or restrict access to the element
     *
     * @param entityGUID unique identifier of entity
     * @param entityTypeName type of entity
     * @param classifications list of classifications from entity
     * @param userAccount calling user
     * @param operation operation that they wish to perform
     * @param createdBy original creator of the entity
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return true=the user has access; false=no information on access for this user
     * @throws UserNotAuthorizedException the user is not authorized for this type of access to this entity
     */
    protected boolean validateClassificationAccess(String                  entityGUID,
                                                   String                  entityTypeName,
                                                   List<Classification>    classifications,
                                                   OpenMetadataUserAccount userAccount,
                                                   AccessOperation         operation,
                                                   String                  createdBy,
                                                   OMRSRepositoryHelper    repositoryHelper,
                                                   String                  serviceName,
                                                   String                  methodName) throws UserNotAuthorizedException
    {
        if (classifications != null)
        {
            if (validateSecurityTagAccess(entityGUID,
                                          entityTypeName,
                                          classifications,
                                          userAccount,
                                          operation,
                                          repositoryHelper,
                                          serviceName,
                                          methodName))
            {
                return true;
            }

            String ownershipGroupName = resolveOwnershipGroupName(ownershipGroupPattern,
                                                                  classifications,
                                                                  repositoryHelper,
                                                                  serviceName,
                                                                  methodName);

            boolean userIsOwner = this.validateUserInGroup(userAccount, ownershipGroupName);

            return validateZoneAccess(entityGUID,
                                      entityTypeName,
                                      classifications,
                                      userAccount,
                                      createdBy,
                                      userIsOwner,
                                      operation,
                                      repositoryHelper,
                                      serviceName,
                                      methodName);
        }

        return false;
    }


    /**
     * Tests for whether a specific user should have the right to create an element.
     *
     * @param userId identifier of user
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param newProperties properties for new entity
     * @param classifications classifications for new entity
     * @param instanceStatus status for new entity
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to perform this command
     */
    @Override
    public void validateUserForElementCreate(String                         userId,
                                             String                         entityTypeGUID,
                                             String                         entityTypeName,
                                             InstanceProperties             newProperties,
                                             List<Classification>           classifications,
                                             InstanceStatus                 instanceStatus,
                                             OMRSRepositoryHelper           repositoryHelper,
                                             String                         serviceName,
                                             String                         methodName) throws UserNotAuthorizedException
    {
        /*
         * The user must have a valid account.  An exception is thrown if there is no account,
         * or it is not in a state to use.
         */
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);

        /*
         * Check to see if there is a group for the
         */
        if (validateClassificationAccess("<notAllocated>",
                                         entityTypeName,
                                         classifications,
                                         userAccount,
                                         AccessOperation.CREATE,
                                         userId,
                                         repositoryHelper,
                                         serviceName,
                                         methodName))
        {
            return;
        }

        String qualifiedName = repositoryHelper.getStringProperty(serviceName,
                                                                  OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                  newProperties,
                                                                  methodName);

        String groupName = this.resolveElementGroupName(elementGroupPattern,
                                                        qualifiedName,
                                                        AccessOperation.CREATE.getName());

        if (groupName != null)
        {
            this.validateUserInGroup(userId, groupName);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param requestedEntity entity requested by the caller
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    @Override
    public void validateUserForElementRead(String               userId,
                                           EntityDetail         requestedEntity,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param anchorEntity entity for the anchor (if extracted - may be null)
     * @param requestedEntity entity requested by the caller
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    @Override
    public void validateUserForAnchorMemberRead(String               userId,
                                                EntityDetail         anchorEntity,
                                                EntityDetail         requestedEntity,
                                                OMRSRepositoryHelper repositoryHelper,
                                                String               serviceName,
                                                String               methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to update the properties of an element.
     *
     * @param userId identifier of user
     * @param originalEntity original entity details
     * @param newEntityProperties new properties
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDetailUpdate(String               userId,
                                                   EntityDetail         originalEntity,
                                                   InstanceProperties   newEntityProperties,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   String               serviceName,
                                                   String               methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to update the properties of an element.
     *
     * @param userId           identifier of user
     * @param originalEntity   original entity details
     * @param newStatus        new value for status
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementStatusUpdate(String userId, EntityDetail originalEntity, InstanceStatus newStatus, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to link unanchored elements to this element
     *
     * @param userId           identifier of user
     * @param startingEntity   end 1 details
     * @param attachingEntity  end 1 details
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementAttach(String userId, EntityDetail startingEntity, EntityDetail attachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to link unanchored elements to this element
     *
     * @param userId           identifier of user
     * @param startingEntity   end 1 details
     * @param detachingEntity  end 2 details
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDetach(String userId, EntityDetail startingEntity, EntityDetail detachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId           identifier of user
     * @param originalEntity   original entity details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementAddFeedback(String userId, EntityDetail originalEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId           identifier of user
     * @param originalEntity   original entity details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDeleteFeedback(String userId, EntityDetail originalEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to add or update a classification on this element.
     *
     * @param userId             identifier of user
     * @param originalEntity     original entity details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementClassify(String userId, EntityDetail originalEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to remove a classification from this element
     *
     * @param userId             identifier of user
     * @param originalEntity     original entity details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDeclassify(String userId, EntityDetail originalEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an anchor such as glossary terms and categories attached to an element.  These updates could be to their properties,
     * classifications and relationships.
     *
     * @param userId identifier of user
     * @param anchorEntity element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberUpdate(String               userId,
                                                  EntityDetail         anchorEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to update the instance status of an element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberStatusUpdate(String               userId,
                                                        EntityDetail         anchorEntity,
                                                        EntityDetail         originalEntity,
                                                        InstanceStatus       newStatus,
                                                        OMRSRepositoryHelper repositoryHelper,
                                                        String               serviceName,
                                                        String               methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to link unanchored  elements
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param attachingEntity  new element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorAttach(String userId, EntityDetail anchorEntity, EntityDetail attachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to link unanchored  elements
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param detachingEntity  obsolete element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorDetach(String userId, EntityDetail anchorEntity, EntityDetail detachingEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the anchor or member element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorAddFeedback(String userId, EntityDetail anchorEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tags and likes, to the anchor or member element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorDeleteFeedback(String userId, EntityDetail anchorEntity, EntityDetail feedbackEntity, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to add or update a classification on this anchor or member element.
     *
     * @param userId             identifier of user
     * @param anchorEntity       anchor details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorClassify(String userId, EntityDetail anchorEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to remove a classification from this anchor or member element
     *
     * @param userId             identifier of user
     * @param anchorEntity       anchor details
     * @param classificationName name of the classification
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorDeclassify(String userId, EntityDetail anchorEntity, String classificationName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param obsoleteEntity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberDelete(String               userId,
                                                  EntityDetail         anchorEntity,
                                                  EntityDetail         obsoleteEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param entity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForElementDelete(String               userId,
                                             EntityDetail         entity,
                                             OMRSRepositoryHelper repositoryHelper,
                                             String               serviceName,
                                             String               methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }

    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param newMemberEntity  feedback element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this element
     */
    @Override
    public void validateUserForAnchorMemberAdd(String userId, EntityDetail anchorEntity, EntityDetail newMemberEntity, String relationshipName, OMRSRepositoryHelper repositoryHelper, String serviceName, String methodName) throws UserNotAuthorizedException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);
    }


    /**
     * Use the security connector to make a choice on which connection to supply to the requesting user.
     *
     * @param userId calling userId
     * @param assetEntity associated asset - may be null
     * @param connectionEntities list of retrieved connections
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return single connection entity, or null
     * @throws UserNotAuthorizedException the user is not able to use any of the connections
     */
    @Override
    public EntityDetail selectConnection(String               userId,
                                         EntityDetail         assetEntity,
                                         List<EntityDetail>   connectionEntities,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws UserNotAuthorizedException
    {
        UserNotAuthorizedException caughtException = null;
        List<EntityDetail>         visibleConnections = new ArrayList<>();

        if ((connectionEntities != null) && (! connectionEntities.isEmpty()))
        {
            for (EntityDetail connection : connectionEntities)
            {
                if (connection != null)
                {
                    try
                    {
                        validateUserForElementRead(userId, connection, repositoryHelper, serviceName, methodName);
                        visibleConnections.add(connection);
                    }
                    catch (UserNotAuthorizedException error)
                    {
                        caughtException = error;
                    }
                }
            }
        }

        if (visibleConnections.isEmpty())
        {
            if (caughtException != null)
            {
                throw caughtException;
            }

            return null;
        }
        else if (visibleConnections.size() == 1)
        {
            return connectionEntities.get(0);
        }

        /*
         * Randomly pick one
         */
        Random rand = new Random();

        return visibleConnections.get(rand.nextInt(visibleConnections.size()));
    }


    /*
     * =========================================================================================================
     * Type security
     *
     * Any valid user can see the types but only the metadata architects can change them.
     * The logic returns if valid access; otherwise it calls the superclass to throw the
     * user not authorized exception.
     */

    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, dynamicTypeAuthorGroup))
        {
            return;
        }

        super.validateUserForTypeCreate(userId, metadataCollectionName, typeDef);
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String           userId,
                                           String           metadataCollectionName,
                                           AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, dynamicTypeAuthorGroup))
        {
            return;
        }

        super.validateUserForTypeCreate(userId, metadataCollectionName, attributeTypeDef);
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String           userId,
                                         String           metadataCollectionName,
                                         AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to update a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef current typeDef details
     * @param patch proposed changes to type
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeUpdate(String       userId,
                                           String       metadataCollectionName,
                                           TypeDef      typeDef,
                                           TypeDefPatch patch) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, dynamicTypeAuthorGroup))
        {
            return;
        }

        super.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef, patch);
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, dynamicTypeAuthorGroup))
        {
            return;
        }

        super.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String              userId,
                                           String              metadataCollectionName,
                                           AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, dynamicTypeAuthorGroup))
        {
            return;
        }

        super.validateUserForTypeDelete(userId, metadataCollectionName, attributeTypeDef);
    }




    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String  userId,
                                               String  metadataCollectionName,
                                               TypeDef originalTypeDef,
                                               String  newTypeDefGUID,
                                               String  newTypeDefName) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, dynamicTypeAuthorGroup))
        {
            return;
        }

        super.validateUserForTypeReIdentify(userId, metadataCollectionName, originalTypeDef, newTypeDefGUID, newTypeDefName);
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalAttributeTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String           userId,
                                               String           metadataCollectionName,
                                               AttributeTypeDef originalAttributeTypeDef,
                                               String           newTypeDefGUID,
                                               String           newTypeDefName) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, dynamicTypeAuthorGroup))
        {
            return;
        }

        super.validateUserForTypeReIdentify(userId,
                                            metadataCollectionName,
                                            originalAttributeTypeDef,
                                            newTypeDefGUID,
                                            newTypeDefName);
    }


    /*
     * =========================================================================================================
     * Instance Security
     *
     * No specific security checks made when instances are written and retrieved from the local repository.
     * The methods override the super class that throws a user not authorized exception on all access/update
     * requests.
     */

    /**
     * Tests for whether a specific user should have the right to create an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityCreate(String                     userId,
                                             String                     metadataCollectionName,
                                             String                     entityTypeGUID,
                                             InstanceProperties         initialProperties,
                                             List<Classification>       initialClassifications,
                                             InstanceStatus             initialStatus) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return entity to return (maybe altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public EntityDetail  validateUserForEntityRead(String       userId,
                                                   String       metadataCollectionName,
                                                   EntityDetail instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);

        return instance;
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to add a classification to an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationAdd(String               userId,
                                                        String               metadataCollectionName,
                                                        EntitySummary        instance,
                                                        String               classificationName,
                                                        InstanceProperties   properties) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


     /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationUpdate(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to delete a classification from an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationDelete(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to restore an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityRestore(String       userId,
                                              String       metadataCollectionName,
                                              String       deletedEntityGUID) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReIdentification(String       userId,
                                                       String       metadataCollectionName,
                                                       EntityDetail instance,
                                                       String       newGUID) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, instanceHeaderAuthorGroup))
        {
            return;
        }

        super.validateUserForEntityReIdentification(userId, metadataCollectionName, instance, newGUID);
    }


    /**
     * Tests for whether a specific user should have the right to change the type name of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReTyping(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, instanceHeaderAuthorGroup))
        {
            return;
        }

        super.validateUserForEntityReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
    }


    /**
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReHoming(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               String         newHomeMetadataCollectionId,
                                               String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, instanceHeaderAuthorGroup))
        {
            return;
        }

        super.validateUserForEntityReHoming(userId, metadataCollectionName, instance, newHomeMetadataCollectionId, newHomeMetadataCollectionName);
    }


    /**
     * Tests for whether a specific user should have the right to create an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneSummary the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoSummary the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipCreate(String               userId,
                                                   String               metadataCollectionName,
                                                   String               relationshipTypeGUID,
                                                   InstanceProperties   initialProperties,
                                                   EntitySummary        entityOneSummary,
                                                   EntitySummary        entityTwoSummary,
                                                   InstanceStatus       initialStatus) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return relationship to return (maybe altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public Relationship validateUserForRelationshipRead(String          userId,
                                                        String          metadataCollectionName,
                                                        Relationship    instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
        return instance;
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to restore an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipRestore(String       userId,
                                                    String       metadataCollectionName,
                                                    String       deletedRelationshipGUID) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReIdentification(String       userId,
                                                             String       metadataCollectionName,
                                                             Relationship instance,
                                                             String       newGUID) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, instanceHeaderAuthorGroup))
        {
            return;
        }

        super.validateUserForRelationshipReIdentification(userId, metadataCollectionName, instance, newGUID);
    }


    /**
     * Tests for whether a specific user should have the right to change the type name of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReTyping(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, instanceHeaderAuthorGroup))
        {
            return;
        }

        super.validateUserForRelationshipReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReHoming(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     String         newHomeMetadataCollectionId,
                                                     String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (this.validateUserInGroup(userId, instanceHeaderAuthorGroup))
        {
            return;
        }

        super.validateUserForRelationshipReHoming(userId, metadataCollectionName, instance, newHomeMetadataCollectionId, newHomeMetadataCollectionName);
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param userId identifier of user
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */

    public boolean  validateEntityReferenceCopySave(String       userId,
                                                    EntityDetail instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
        return true;
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param userId identifier of user
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    public boolean  validateRelationshipReferenceCopySave(String       userId,
                                                          Relationship instance) throws UserNotAuthorizedException
    {
        this.getUserAccount(userId);
        return true;
    }
}
