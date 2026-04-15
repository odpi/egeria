/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.accessconnector;


import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.*;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.metadatasecurity.*;
import org.odpi.openmetadata.metadatasecurity.accessconnector.controls.OpenMetadataSecurityConfigurationProperty;
import org.odpi.openmetadata.metadatasecurity.accessconnector.ffdc.MetadataSecurityAuditCode;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityAuditCode;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataSecurityAccessControl;
import org.odpi.openmetadata.metadatasecurity.properties.OpenMetadataUserAccount;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

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
     * Standard group names
     */
    private String allUsersGroup            = OpenMetadataSecurityConfigurationProperty.ALL_USERS_GROUP.getDefaultValue();
    private String employeeUsersGroup       = OpenMetadataSecurityConfigurationProperty.EMPLOYEES_GROUP.getDefaultValue();
    private String externalUsersGroup       = OpenMetadataSecurityConfigurationProperty.EXTERNAL_USERS_GROUP.getDefaultValue();
    private String contractorUsersGroup     = OpenMetadataSecurityConfigurationProperty.CONTRACTORS_GROUP.getDefaultValue();
    private String digitalUsersGroup        = OpenMetadataSecurityConfigurationProperty.DIGITAL_USERS_GROUP.getDefaultValue();
    private String instanceOwnersGroup      = OpenMetadataSecurityConfigurationProperty.INSTANCE_OWNER_GROUP.getDefaultValue();
    private String existingMaintainersGroup = OpenMetadataSecurityConfigurationProperty.EXISTING_MAINTAINER_GROUP.getDefaultValue();
    private String newMaintainersGroup      = OpenMetadataSecurityConfigurationProperty.NEW_MAINTAINER_GROUP.getDefaultValue();

    private String serverAdministratorControl = OpenMetadataSecurityConfigurationProperty.SERVER_ADMINISTRATOR_CONTROL.getDefaultValue();
    private String serverOperatorsControl     = OpenMetadataSecurityConfigurationProperty.SERVER_OPERATORS_CONTROL.getDefaultValue();
    private String serverInvestigatorsControl = OpenMetadataSecurityConfigurationProperty.SERVER_INVESTIGATORS_CONTROL.getDefaultValue();




    /**
     * Constructor sets up the security policies
     */
    public OpenMetadataAccessSecurityConnector()
    {
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        serverAdministratorControl = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_ADMINISTRATOR_CONTROL.getName(),
                                                                          connectionBean.getConfigurationProperties(),
                                                                          OpenMetadataSecurityConfigurationProperty.SERVER_ADMINISTRATOR_CONTROL.getDefaultValue());

        serverOperatorsControl = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_OPERATORS_CONTROL.getName(),
                                                                      connectionBean.getConfigurationProperties(),
                                                                      OpenMetadataSecurityConfigurationProperty.SERVER_OPERATORS_CONTROL.getDefaultValue());

        serverInvestigatorsControl = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.SERVER_INVESTIGATORS_CONTROL.getName(),
                                                                          connectionBean.getConfigurationProperties(),
                                                                          OpenMetadataSecurityConfigurationProperty.SERVER_INVESTIGATORS_CONTROL.getDefaultValue());


        instanceOwnersGroup   = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.INSTANCE_OWNER_GROUP.getName(),
                                                                          connectionBean.getConfigurationProperties(),
                                                                          OpenMetadataSecurityConfigurationProperty.INSTANCE_OWNER_GROUP.getDefaultValue());
        existingMaintainersGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.EXISTING_MAINTAINER_GROUP.getName(),
                                                                          connectionBean.getConfigurationProperties(),
                                                                          OpenMetadataSecurityConfigurationProperty.EXISTING_MAINTAINER_GROUP.getDefaultValue());
        newMaintainersGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.NEW_MAINTAINER_GROUP.getName(),
                                                                   connectionBean.getConfigurationProperties(),
                                                                   OpenMetadataSecurityConfigurationProperty.NEW_MAINTAINER_GROUP.getDefaultValue());
        allUsersGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.ALL_USERS_GROUP.getName(),
                                                                       connectionBean.getConfigurationProperties(),
                                                                       OpenMetadataSecurityConfigurationProperty.ALL_USERS_GROUP.getDefaultValue());
        employeeUsersGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.EMPLOYEES_GROUP.getName(),
                                                                           connectionBean.getConfigurationProperties(),
                                                                           OpenMetadataSecurityConfigurationProperty.EMPLOYEES_GROUP.getDefaultValue());
        contractorUsersGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.CONTRACTORS_GROUP.getName(),
                                                                        connectionBean.getConfigurationProperties(),
                                                                        OpenMetadataSecurityConfigurationProperty.CONTRACTORS_GROUP.getDefaultValue());
        digitalUsersGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.DIGITAL_USERS_GROUP.getName(),
                                                                         connectionBean.getConfigurationProperties(),
                                                                         OpenMetadataSecurityConfigurationProperty.DIGITAL_USERS_GROUP.getDefaultValue());
        externalUsersGroup = super.getStringConfigurationProperty(OpenMetadataSecurityConfigurationProperty.EXTERNAL_USERS_GROUP.getName(),
                                                                           connectionBean.getConfigurationProperties(),
                                                                           OpenMetadataSecurityConfigurationProperty.EXTERNAL_USERS_GROUP.getDefaultValue());
    }


    /**
     * Return the list of defined users.
     *
     * @param userAccountStatus status of the user - or null for any status
     * @param userAccountType   type of user - or null for any type
     * @return list of userIds
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public List<String> getUserList(UserAccountStatus userAccountStatus,
                                    UserAccountType   userAccountType) throws UserNotAuthorizedException,
                                                                              InvalidParameterException,
                                                                              PropertyServerException
    {
        if (secretsStoreConnectorMap != null)
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        Map<String, UserAccount> users = secretsStoreConnector.getUsers();
                        if (users != null)
                        {
                            List<String> userIds = new ArrayList<>();

                            for (String userId : users.keySet())
                            {
                                if (userId != null)
                                {
                                    UserAccount userAccount = users.get(userId);

                                    if (userAccount != null)
                                    {
                                        if ((userAccountStatus == null) || (userAccountStatus == userAccount.getUserAccountStatus()))
                                        {
                                            if ((userAccountType == null) || (userAccountType == userAccount.getUserAccountType()))
                                            {
                                                userIds.add(userId);
                                            }
                                        }
                                    }
                                }
                            }
                            return userIds;
                        }
                    }
                    catch (ConnectorCheckedException error)
                    {
                        // No users
                    }
                }
            }
        }

        return null;
    }


    /**
     * Retrieve information about a specific user.  This is used during a user's request for a token
     *
     * @param userId calling user
     * @return security properties about the user
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public OpenMetadataUserAccount getUserAccount(String userId) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException
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
                            if (userAccount.getUserAccountStatus() == UserAccountStatus.CREDENTIALS_EXPIRED)
                            {
                                logRecord(methodName, OpenMetadataSecurityAuditCode.EXPIRED_USER.getMessageDefinition(userId));
                            }

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
     * Check that the user is valid.
     *
     * @param userId calling user
     * @param methodName calling method
     * @return user account of exception
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    private OpenMetadataUserAccount getActiveUserAccount(String userId,
                                                         String methodName) throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   PropertyServerException
    {
        OpenMetadataUserAccount userAccount = this.getUserAccount(userId);

        if ((userAccount != null) && (userAccount.getUserAccountStatus() == UserAccountStatus.AVAILABLE))
        {
            return userAccount;
        }

        throwUnknownUser(userId, null, methodName);
        return null; // not reached
    }


    /**
     * Update/create information about a specific user.  This is used to update user details nd reset the password.
     *
     * @param userAccount security properties about the user
     * @throws UserNotAuthorizedException user not recognized
     */
    @Override
    public void setUserAccount(OpenMetadataUserAccount userAccount) throws UserNotAuthorizedException
    {
        final String methodName = "updateUserAccount";

        boolean accountSaved = false;

        if (secretsStoreConnectorMap != null)
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        secretsStoreConnector.saveUser(userAccount.getUserId(), userAccount);
                        accountSaved = true;
                    }
                    catch (ConnectorCheckedException error)
                    {
                        throwUnknownUser(userAccount.getUserId(), error, methodName);
                    }
                }
            }
        }

        if (! accountSaved)
        {
            throwUnknownUser(userAccount.getUserId(), null, methodName);
        }

        logRecord(methodName, OpenMetadataSecurityAuditCode.ADDING_USER.getMessageDefinition(userAccount.getUserId()));
    }


    /**
     * Delete information about a specific user.
     *
     * @param userId      calling user
     * @throws UserNotAuthorizedException user not recognized
     */
    @Override
    public void deleteUserAccount(String userId) throws UserNotAuthorizedException
    {
        final String methodName = "deleteUserAccount";

        boolean accountDeleted = false;

        if (secretsStoreConnectorMap != null)
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        if (secretsStoreConnector.getUser(userId) != null)
                        {
                            secretsStoreConnector.deleteUser(userId);
                            accountDeleted = true;
                        }
                    }
                    catch (ConnectorCheckedException error)
                    {
                        throwUnknownUser(userId, error, methodName);
                    }
                }
            }
        }

        if (! accountDeleted)
        {
            throwUnknownUser(userId, null, methodName);
        }

        logRecord(methodName, OpenMetadataSecurityAuditCode.REMOVING_USER.getMessageDefinition(userId));
    }


    /**
     * Retrieve information about a specific security access control.
     *
     * @param controlName calling user
     * @return security access control
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public OpenMetadataSecurityAccessControl getSecurityAccessControl(String controlName) throws UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(controlName);

        if (securityAccessControl != null)
        {
            return new OpenMetadataSecurityAccessControl(controlName, securityAccessControl);
        }

        return null;
    }


    /**
     * Create/update information about a specific security access control.
     *
     * @param securityAccessControl control properties
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void setSecurityAccessControl(OpenMetadataSecurityAccessControl securityAccessControl) throws UserNotAuthorizedException,
                                                                                                         InvalidParameterException,
                                                                                                         PropertyServerException
    {
        final String methodName = "setSecurityAccessControl";

        boolean accountSaved = false;

        if (secretsStoreConnectorMap != null)
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        secretsStoreConnector.saveSecurityAccessControl(securityAccessControl.getControlName(), securityAccessControl);
                        accountSaved = true;
                    }
                    catch (ConnectorCheckedException error)
                    {
                        throwUnknownControl(securityAccessControl.getControlName(), error, methodName);
                    }
                }
            }
        }

        if (! accountSaved)
        {
            throwUnknownControl(securityAccessControl.getControlName(), null, methodName);
        }

        logRecord(methodName, OpenMetadataSecurityAuditCode.ADDING_CONTROL.getMessageDefinition(securityAccessControl.getControlName()));

    }


    /**
     * Delete information about a specific security access control.
     *
     * @param controlName calling user
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void deleteSecurityAccessControl(String controlName) throws UserNotAuthorizedException,
                                                                                 InvalidParameterException,
                                                                                 PropertyServerException
    {
        final String methodName = "deleteSecurityAccessControl";

        boolean controlDeleted = false;

        if (secretsStoreConnectorMap != null)
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        if (secretsStoreConnector.getSecurityAccessControl(controlName) != null)
                        {
                            secretsStoreConnector.deleteSecurityAccessControl(controlName);
                            controlDeleted = true;
                        }
                    }
                    catch (ConnectorCheckedException error)
                    {
                        throwUnknownControl(controlName, error, methodName);
                    }
                }
            }
        }

        if (! controlDeleted)
        {
            throwUnknownControl(controlName, null, methodName);
        }

        logRecord(methodName, OpenMetadataSecurityAuditCode.REMOVING_CONTROL.getMessageDefinition(controlName));
    }


    /**
     * This method does the lookup of the user and group in the secrets store.
     *
     * @param userId                calling user
     * @param securityAccessControl control
     * @param accessOperation       operation
     * @param isUserOwner           is the user the owner of the element?
     * @param maintainers           list of maintainers
     *
     * @return boolean indicating that they are not members of the group
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    protected boolean validateUserInGroup(String                userId,
                                          SecurityAccessControl securityAccessControl,
                                          boolean               isUserOwner,
                                          List<String>          maintainers,
                                          String                accessOperation) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "validateUserInGroup";

        /*
         * The user must have a valid account.
         */
        OpenMetadataUserAccount userAccount = this.getActiveUserAccount(userId, methodName);

        assert  (userAccount != null);

        /*
         * If there is no security control, then all users can access this element.
         */
        if ((securityAccessControl == null) || (securityAccessControl.getAssociatedSecurityList() == null))
        {
            return true;
        }

        List<String> associatedSecurityList = securityAccessControl.getAssociatedSecurityList().get(accessOperation);

        if (associatedSecurityList == null)
        {
            associatedSecurityList = securityAccessControl.getAssociatedSecurityList().get(AccessOperation.DEFAULT.name());
        }

        /*
         * If there is no security list, then all users can access this element.
         */
        if (associatedSecurityList == null)
        {
            return true;
        }


        /*
         * Does the security list permit any user to access this service?
         */
        if (associatedSecurityList.contains(allUsersGroup))
        {
            return true;
        }



        /*
         * Does the account type match one of the listed groups?
         */
        if (((userAccount.getUserAccountType() == UserAccountType.EMPLOYEE) && (associatedSecurityList.contains(employeeUsersGroup))) ||
            ((userAccount.getUserAccountType() == UserAccountType.CONTRACTOR) && (associatedSecurityList.contains(contractorUsersGroup))) ||
            ((userAccount.getUserAccountType() == UserAccountType.EXTERNAL) && (associatedSecurityList.contains(externalUsersGroup))) ||
            ((userAccount.getUserAccountType() == UserAccountType.DIGITAL) && (associatedSecurityList.contains(digitalUsersGroup))))
        {
            return true;
        }

        if (maintainers != null)
        {
            if (maintainers.contains(userId))
            {
                if (associatedSecurityList.contains(existingMaintainersGroup))
                {
                    return true;
                }
            }
            else
            {
                if (associatedSecurityList.contains(newMaintainersGroup))
                {
                    return true;
                }
            }
        }

        if ((isUserOwner) && (associatedSecurityList.contains(instanceOwnersGroup)))
        {
            return true;
        }

        if (userAccount.getSecurityGroups() != null)
        {
            for (String securityGroupName : userAccount.getSecurityGroups())
            {
                if (associatedSecurityList.contains(securityGroupName))
                {
                    return true;
                }
            }
        }

        if (userAccount.getSecurityRoles() != null)
        {
            for (String securityRoleName : userAccount.getSecurityRoles())
            {
                if (associatedSecurityList.contains(securityRoleName))
                {
                    return true;
                }
            }
        }

        /*
         * Finally, a user has full access to a governance zone that has the same name as the user's userId.
         */
        return ((securityAccessControl.getControlTypeName().equals(OpenMetadataType.GOVERNANCE_ZONE.typeName)
                && (userId.equals(securityAccessControl.getDisplayName()))));
    }


    /**
     * Check that the calling user is authorized to create new servers.
     *
     * @param userId calling user
     *
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForNewServer(String userId) throws UserNotAuthorizedException,
                                                                InvalidParameterException,
                                                                PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serverAdministratorControl);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    public void  validateUserAsOperatorForPlatform(String   userId) throws UserNotAuthorizedException,
                                                                           InvalidParameterException,
                                                                           PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serverOperatorsControl);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserAsInvestigatorForPlatform(String userId) throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serverInvestigatorsControl);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException,
                                                               InvalidParameterException,
                                                               PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serverName);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serverAdministratorControl);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serverOperatorsControl);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException,
                                                                          InvalidParameterException,
                                                                          PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serverInvestigatorsControl);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForService(String userId,
                                        String serviceName) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serviceName);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       PropertyServerException
    {
        SecurityAccessControl securityAccessControl = this.getSecurityAccessControlFromStore(serviceName);

        if (this.validateUserInGroup(userId,
                                     securityAccessControl,
                                     false,
                                     null,
                                     AccessOperation.DEFAULT.name()))
        {
            return;
        }

        super.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
    }


    /**
     * Returns the named security access control
     *
     * @param controlName name of the zone
     * @return security control or null if not found
     */
    SecurityAccessControl getSecurityAccessControlFromStore(String controlName)
    {
        final String methodName = "getSecurityAccessControlFromStore";

        if ((controlName != null) && (secretsStoreConnectorMap != null))
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        SecurityAccessControl accessControl = secretsStoreConnector.getSecurityAccessControl(controlName);

                        if (accessControl != null)
                        {
                            return accessControl;
                        }
                    }
                    catch (Exception secretsStoreError)
                    {
                        super.logRecord(methodName,
                                        MetadataSecurityAuditCode.SECRETS_STORE_EXCEPTION.getMessageDefinition(serverName,
                                                                                                               secretsStoreError.getClass().getName(),
                                                                                                               controlName,
                                                                                                               AccessOperation.DEFAULT.getName(),
                                                                                                               secretsStoreError.getMessage()));

                    }
                }
            }
        }

        return null;
    }


    /**
     * Returns the list of security lists associated with the zone and operation.
     * If none are found, there is no restriction on the use of the zone.
     *
     * @param zoneName name of the zone
     * @param operation operation to check
     * @return list of security groups
     */
    protected List<String> getAssociatedSecurityListForZone(String          zoneName,
                                                            AccessOperation operation)
    {
        final String methodName = "getAssociatedSecurityListForZone";

        if ((zoneName != null) && (secretsStoreConnectorMap != null))
        {
            for (SecretsStoreConnector secretsStoreConnector : secretsStoreConnectorMap.values())
            {
                if (secretsStoreConnector != null)
                {
                    try
                    {
                        SecurityAccessControl accessControl = secretsStoreConnector.getSecurityAccessControl(zoneName);

                        if (accessControl != null)
                        {
                            Map<String, List<String>> associatedSecurityList = accessControl.getAssociatedSecurityList();

                            if (associatedSecurityList != null)
                            {
                                if (associatedSecurityList.get(operation.name()) != null)
                                {
                                    return associatedSecurityList.get(operation.name());
                                }
                                if (associatedSecurityList.get(AccessOperation.DEFAULT.name()) != null)
                                {
                                    return associatedSecurityList.get(AccessOperation.DEFAULT.name());
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
                                                                                                               operation.name(),
                                                                                                               secretsStoreError.getMessage()));

                    }
                }
            }
        }

        return null;
    }


    /**
     * Determine the appropriate setting for the governance zones.
     *
     * @param defaultZoneSetting default setting of the supported zones
     * @param accessOperation list to retrieve
     * @param userId name of the user
     *
     * @return list of supported zones for the user
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    private List<String> getZonesForUser(List<String>    defaultZoneSetting,
                                         String          accessOperation,
                                         String          userId) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException
    {
        final String methodName = "getZonesForUser";

        /*
         * The user must have a valid account.
         */
        OpenMetadataUserAccount userAccount = this.getActiveUserAccount(userId, methodName);

        HashSet<String> returnZones = new HashSet<>();

        if (defaultZoneSetting != null)
        {
            returnZones.addAll(defaultZoneSetting);
        }

        if ((userAccount != null) &&
                (userAccount.getOtherProperties() != null) &&
                (userAccount.getOtherProperties().containsKey(accessOperation) &&
                        (userAccount.getOtherProperties().get(accessOperation) instanceof List<?> zoneAccessList)))
        {
            for (Object zoneName : zoneAccessList)
            {
                if (zoneName instanceof String zoneNameString)
                {
                    returnZones.add(zoneNameString);
                }
            }
        }

        if (returnZones.isEmpty())
        {
            return null;
        }

        return new ArrayList<>(returnZones);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public List<String> getDefaultZonesForUser(List<String> initialZones,
                                               String       typeName,
                                               String       serviceName,
                                               String       userId) throws UserNotAuthorizedException,
                                                                           InvalidParameterException,
                                                                           PropertyServerException
    {
        return this.getZonesForUser(initialZones, "defaultZones", userId);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public List<String> getPublishZonesForUser(List<String> currentZones,
                                               String       typeName,
                                               String       serviceName,
                                               String       userId) throws UserNotAuthorizedException,
                                                                           InvalidParameterException,
                                                                           PropertyServerException
    {
        /*
         * The current zones are not relevant.
         */
        return this.getZonesForUser(null, "publishZones", userId);
    }

    /*============================================================================
     * OpenMetadataElementSecurity assures the access to open metadata elements.
     * There is one set of methods for unanchored elements an one for anchors and their members.
     */


    /**
     * Determine whether the user is permitted to perform the desired operation based on the element's zones.
     *
     * @param userId identifier of user
     * @param classifications list of classifications from entity
     * @param operation operation that they wish to perform
     * @param maintainers list of maintainers of the entity
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    protected boolean validateZoneAccess(String                  userId,
                                         List<Classification>    classifications,
                                         AccessOperation         operation,
                                         List<String>            maintainers,
                                         OMRSRepositoryHelper    repositoryHelper,
                                         String                  serviceName,
                                         String                  methodName) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
        /*
         * The user must have a valid account.
         */
        OpenMetadataUserAccount userAccount = this.getActiveUserAccount(userId, methodName);

        assert userAccount != null;

        /*
         * Other security checks are driven by the zone membership classification
         */
        if (classifications != null)
        {
            List<String> zoneMembership = this.getZoneMembership(classifications,
                                                                 repositoryHelper,
                                                                 serviceName,
                                                                 methodName);

            if ((zoneMembership != null) && (! zoneMembership.isEmpty()))
            {
                int securedZoneCount = 0;
                for (String zoneName : zoneMembership)
                {
                    if (userId.equals(zoneName))
                    {
                        return true;
                    }
                    else if (zoneName != null)
                    {
                        List<String> associatedSecurityList = getAssociatedSecurityListForZone(zoneName, operation);

                        /*
                         * If the zone has no associated security list, then it is not a secured zone and is ignored.
                         * This is different from giving everyone access.
                         */
                        if (associatedSecurityList != null)
                        {
                            securedZoneCount++;

                            /*
                             * Does the security list permit any user to access this service?
                             */
                            if (associatedSecurityList.contains(allUsersGroup))
                            {
                                return true;
                            }

                            /*
                             * Does the account type match one of the listed groups?
                             */
                            if (((userAccount.getUserAccountType() == UserAccountType.EMPLOYEE) && (associatedSecurityList.contains(employeeUsersGroup))) ||
                                    ((userAccount.getUserAccountType() == UserAccountType.CONTRACTOR) && (associatedSecurityList.contains(contractorUsersGroup))) ||
                                    ((userAccount.getUserAccountType() == UserAccountType.EXTERNAL) && (associatedSecurityList.contains(externalUsersGroup))) ||
                                    ((userAccount.getUserAccountType() == UserAccountType.DIGITAL) && (associatedSecurityList.contains(digitalUsersGroup))))
                            {
                                return true;
                            }

                            if (userAccount.getSecurityGroups() != null)
                            {
                                for (String securityGroupName : userAccount.getSecurityGroups())
                                {
                                    if (associatedSecurityList.contains(securityGroupName))
                                    {
                                        return true;
                                    }
                                }
                            }

                            if (userAccount.getSecurityRoles() != null)
                            {
                                for (String securityRoleName : userAccount.getSecurityRoles())
                                {
                                    if (associatedSecurityList.contains(securityRoleName))
                                    {
                                        return true;
                                    }
                                }
                            }

                            List<String> instanceBasedLists = this.getInstanceBasedGroups(userId, classifications, maintainers, repositoryHelper);

                            if (instanceBasedLists != null)
                            {
                                for (String groupName : instanceBasedLists)
                                {
                                    if (associatedSecurityList.contains(groupName))
                                    {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }

                if (securedZoneCount > 0)
                {
                    return false;
                }
            }
        }

        /*
         * If this point is reached, the user has access.
         */
        return true;
    }


    /**
     * Determine if the user is an instance owner based on the classifications and maintainers.
     *
     * @param userId identifier of user
     * @param classifications list of classifications from entity
     * @param maintainers list of maintainers for the entity
     * @param repositoryHelper helper object to access repository services
     * @return list of additional groups the user should be added to based on their relationship with the instance.
     */
    List<String> getInstanceBasedGroups(String               userId,
                                        List<Classification> classifications,
                                        List<String>         maintainers,
                                        OMRSRepositoryHelper repositoryHelper)
    {
        List<String> instanceBasedLists = new ArrayList<>();

        /*
         * The userId is a special group name for private content.
         */
        instanceBasedLists.add(userId);

        if (isUserAnOwner(userId, classifications, repositoryHelper))
        {
            instanceBasedLists.add(instanceOwnersGroup);
        }

        if (maintainers != null)
        {
            if (maintainers.contains(userId))
            {
                instanceBasedLists.add(existingMaintainersGroup);
            }
            else
            {
                instanceBasedLists.add(newMaintainersGroup);
            }
        }

        return instanceBasedLists;
    }


    /**
     * Validate that the user is an owner of the entity.  If no ownership classification
     * is assigned to the element, then the user is considered to be an owner.
     *
     * @param userId user id
     * @param classifications list of classifications
     * @param repositoryHelper repository helper
     * @return true if user is owner or there is no owner
     */
    private boolean isUserAnOwner(String               userId,
                                  List<Classification> classifications,
                                  OMRSRepositoryHelper repositoryHelper)
    {
        final String methodName = "isUserAnOwner";

        if (classifications != null)
        {
            for (Classification classification : classifications)
            {
                if (classification.getName().equals(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName))
                {
                    List<String> owners = repositoryHelper.getStringArrayProperty(connectorName,
                                                                                  OpenMetadataProperty.USER_ID.name,
                                                                                  classification.getProperties(),
                                                                                  methodName);

                    if ((owners != null) && (! owners.contains(userId)))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
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
                                             String                         methodName) throws UserNotAuthorizedException,
                                                                                               InvalidParameterException,
                                                                                               PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (validateZoneAccess(userId,
                               classifications,
                               AccessOperation.CREATE,
                               Collections.singletonList(userId),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedInstanceCreate(userId, entityTypeGUID, methodName);
    }


    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param requestedEntity entity requested by the caller
     * @param isExplicitGetRequest is this entity requested explicitly
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementRead(String               userId,
                                           EntityDetail         requestedEntity,
                                           boolean              isExplicitGetRequest,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName) throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   PropertyServerException
    {
        if (validateZoneAccess(userId,
                               requestedEntity.getClassifications(),
                               AccessOperation.READ,
                               requestedEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        if (isExplicitGetRequest)
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.READ.getName(),
                                           requestedEntity.getGUID(),
                                           requestedEntity.getType().getTypeDefName(),
                                           methodName);
        }
        else
        {
            throwFilteredElement(userId,
                                 AccessOperation.READ.getName(),
                                 requestedEntity.getGUID(),
                                 methodName);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific element and its contents.
     *
     * @param userId calling user
     * @param anchorEntity entity for the anchor (if extracted - may be null)
     * @param requestedEntity entity requested by the caller
     * @param isExplicitGetRequest is this entity requested explicitly
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorMemberRead(String               userId,
                                                EntityDetail         anchorEntity,
                                                EntityDetail         requestedEntity,
                                                boolean              isExplicitGetRequest,
                                                OMRSRepositoryHelper repositoryHelper,
                                                String               serviceName,
                                                String               methodName) throws UserNotAuthorizedException,
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException
    {
        if (repositoryHelper.getClassificationProperties(serviceName,
                                                         requestedEntity.getClassifications(),
                                                         OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                         methodName) != null)
        {
            if (validateZoneAccess(userId,
                                   requestedEntity.getClassifications(),
                                   AccessOperation.READ,
                                   requestedEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            if (isExplicitGetRequest)
            {
                throwUnauthorizedElementAccess(userId,
                                               AccessOperation.READ.getName(),
                                               requestedEntity.getGUID(),
                                               requestedEntity.getType().getTypeDefName(),
                                               methodName);
            }
            else
            {
                throwFilteredElement(userId,
                                     AccessOperation.READ.getName(),
                                     requestedEntity.getGUID(),
                                     methodName);
            }
        }
        else
        {
            if (validateZoneAccess(userId,
                                   anchorEntity.getClassifications(),
                                   AccessOperation.READ,
                                   requestedEntity.getMaintainedBy(), // todo is this right or should it be the anchor entity's?
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.READ.getName(),
                                          anchorEntity.getGUID(),
                                          anchorEntity.getType().getTypeDefName(),
                                          methodName);
        }
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementDetailUpdate(String               userId,
                                                   EntityDetail         originalEntity,
                                                   InstanceProperties   newEntityProperties,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   String               serviceName,
                                                   String               methodName) throws UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           PropertyServerException
    {
        if (validateZoneAccess(userId,
                               originalEntity.getClassifications(),
                               AccessOperation.UPDATE_PROPERTIES,
                               originalEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.UPDATE_PROPERTIES.getName(),
                                       originalEntity.getGUID(),
                                       originalEntity.getType().getTypeDefName(),
                                       methodName);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementStatusUpdate(String               userId,
                                                   EntityDetail         originalEntity,
                                                   InstanceStatus       newStatus,
                                                   OMRSRepositoryHelper repositoryHelper,
                                                   String               serviceName,
                                                   String               methodName) throws UserNotAuthorizedException,
                                                                                           InvalidParameterException,
                                                                                           PropertyServerException
    {
        if (validateZoneAccess(userId,
                               originalEntity.getClassifications(),
                               AccessOperation.UPDATE_STATUS,
                               originalEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.UPDATE_STATUS.getName(),
                                       originalEntity.getGUID(),
                                       originalEntity.getType().getTypeDefName(),
                                       methodName);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementAttach(String               userId,
                                             EntityDetail         startingEntity,
                                             EntityDetail         attachingEntity,
                                             String               relationshipName,
                                             OMRSRepositoryHelper repositoryHelper,
                                             String               serviceName,
                                             String               methodName) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException
    {
        if (! validateZoneAccess(userId,
                                 startingEntity.getClassifications(),
                                 AccessOperation.ATTACH,
                                 startingEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.ATTACH.getName(),
                                           startingEntity.getGUID(),
                                           startingEntity.getType().getTypeDefName(),
                                           methodName);
        }

        if (! validateZoneAccess(userId,
                                 attachingEntity.getClassifications(),
                                 AccessOperation.ATTACH,
                                 attachingEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.ATTACH.getName(),
                                           attachingEntity.getGUID(),
                                           attachingEntity.getType().getTypeDefName(),
                                           methodName);
        }
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementDetach(String               userId,
                                             EntityDetail         startingEntity,
                                             EntityDetail         detachingEntity,
                                             String               relationshipName,
                                             OMRSRepositoryHelper repositoryHelper,
                                             String               serviceName,
                                             String               methodName) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException
    {
        if (! validateZoneAccess(userId,
                                 startingEntity.getClassifications(),
                                 AccessOperation.DETACH,
                                 startingEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.DETACH.getName(),
                                           startingEntity.getGUID(),
                                           startingEntity.getType().getTypeDefName(),
                                           methodName);
        }

        if (! validateZoneAccess(userId,
                                 detachingEntity.getClassifications(),
                                 AccessOperation.DETACH,
                                 detachingEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.DETACH.getName(),
                                           detachingEntity.getGUID(),
                                           detachingEntity.getType().getTypeDefName(),
                                           methodName);
        }
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementAddFeedback(String               userId,
                                                  EntityDetail         originalEntity,
                                                  EntityDetail         feedbackEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException
    {
        if (! validateZoneAccess(userId,
                                 originalEntity.getClassifications(),
                                 AccessOperation.ADD_FEEDBACK,
                                 originalEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedAddFeedback(userId, originalEntity.getGUID(), methodName);
        }

        if (! validateZoneAccess(userId,
                                 feedbackEntity.getClassifications(),
                                 AccessOperation.READ,
                                 feedbackEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.READ.getName(),
                                           feedbackEntity.getGUID(),
                                           feedbackEntity.getType().getTypeDefName(),
                                           methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tags, and likes, to the element.
     *
     * @param userId           identifier of user
     * @param originalEntity   original entity details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementDeleteFeedback(String               userId,
                                                     EntityDetail         originalEntity,
                                                     EntityDetail         feedbackEntity,
                                                     OMRSRepositoryHelper repositoryHelper,
                                                     String               serviceName,
                                                     String               methodName) throws UserNotAuthorizedException,
                                                                                             InvalidParameterException,
                                                                                             PropertyServerException
    {
        if (! validateZoneAccess(userId,
                                 originalEntity.getClassifications(),
                                 AccessOperation.DELETE_FEEDBACK,
                                 originalEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.DELETE_FEEDBACK.getName(),
                                           originalEntity.getGUID(),
                                           originalEntity.getType().getTypeDefName(),
                                           methodName);
        }

        if (! validateZoneAccess(userId,
                                 feedbackEntity.getClassifications(),
                                 AccessOperation.READ,
                                 feedbackEntity.getMaintainedBy(),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.READ.getName(),
                                           feedbackEntity.getGUID(),
                                           feedbackEntity.getType().getTypeDefName(),
                                           methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to add or update a classification on this element.
     *
     * @param userId             identifier of user
     * @param originalEntity     original entity details
     * @param classificationName name of the classification
     * @param classificationProperties new properties
     * @param repositoryHelper   helper for OMRS objects
     * @param serviceName        calling service
     * @param methodName         calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementClassify(String               userId,
                                               EntityDetail         originalEntity,
                                               String               classificationName,
                                               InstanceProperties   classificationProperties,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName,
                                               String               methodName) throws UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       PropertyServerException
    {
        if (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classificationName))
        {
            if (validateZoneAccess(userId,
                                   originalEntity.getClassifications(),
                                   AccessOperation.PUBLISH,
                                   originalEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            List<String> originalZones = this.getZoneMembership(originalEntity.getClassifications(),
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);
            List<String> newZones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                            OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                            classificationProperties,
                                                                            methodName);

            throwUnauthorizedZoneChange(userId, originalEntity.getGUID(), originalZones, newZones, methodName);
        }
        else
        {
            if (validateZoneAccess(userId,
                                   originalEntity.getClassifications(),
                                   AccessOperation.CLASSIFY,
                                   originalEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.CLASSIFY.getName(),
                                           originalEntity.getGUID(),
                                           originalEntity.getType().getTypeDefName(),
                                           methodName);
        }
    }


    /**
     * Extract the zone membership from an entity's classifications.
     *
     * @param classifications  classifications from entity
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @return list of zones or null if no zones
     */
    private List<String> getZoneMembership(List<Classification> classifications,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName)
    {
        InstanceProperties classificationProperties = repositoryHelper.getClassificationProperties(serviceName,
                                                                                                   classifications,
                                                                                                   OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                   methodName);

        return repositoryHelper.getStringArrayProperty(serviceName,
                                                       OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                       classificationProperties,
                                                       methodName);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementDeclassify(String               userId,
                                                 EntityDetail         originalEntity,
                                                 String               classificationName,
                                                 OMRSRepositoryHelper repositoryHelper,
                                                 String               serviceName,
                                                 String               methodName) throws UserNotAuthorizedException,
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException
    {
        if (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classificationName))
        {
            if (validateZoneAccess(userId,
                                   originalEntity.getClassifications(),
                                   AccessOperation.PUBLISH,
                                   originalEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            List<String> originalZones = this.getZoneMembership(originalEntity.getClassifications(),
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);
            List<String> newZones = new ArrayList<>();

            throwUnauthorizedZoneChange(userId, originalEntity.getGUID(), originalZones, newZones, methodName);
        }
        else
        {
            if (validateZoneAccess(userId,
                                   originalEntity.getClassifications(),
                                   AccessOperation.DECLASSIFY,
                                   originalEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            throwUnauthorizedElementAccess(userId,
                                           AccessOperation.DECLASSIFY.getName(),
                                           originalEntity.getGUID(),
                                           originalEntity.getType().getTypeDefName(),
                                           methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create new elements as members of an anchor.
     *
     * @param userId identifier of user
     * @param anchorEntity element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorMemberCreate(String               userId,
                                                  EntityDetail         anchorEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (validateZoneAccess(userId,
                               anchorEntity.getClassifications(),
                               AccessOperation.CREATE,
                               Collections.singletonList(userId),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedAnchorAccess(userId,
                                      AccessOperation.CREATE.getName(),
                                      anchorEntity.getType().getTypeDefName(),
                                      anchorEntity.getGUID(),
                                      methodName);
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to an anchor.  These updates could be to their properties,
     * classifications, and relationships.
     *
     * @param userId identifier of user
     * @param anchorEntity element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorMemberUpdate(String               userId,
                                                  EntityDetail         anchorEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (validateZoneAccess(userId,
                               anchorEntity.getClassifications(),
                               AccessOperation.UPDATE_PROPERTIES,
                               Collections.singletonList(userId),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedAnchorAccess(userId,
                                      AccessOperation.UPDATE_PROPERTIES.getName(),
                                      anchorEntity.getType().getTypeDefName(),
                                      anchorEntity.getGUID(),
                                      methodName);
    }


    /**
     * Tests for whether a specific user should have the right to update the instance status of an element.
     *
     * @param userId identifier of user
     * @param anchorEntity anchor details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorMemberStatusUpdate(String               userId,
                                                        EntityDetail         anchorEntity,
                                                        EntityDetail         originalEntity,
                                                        InstanceStatus       newStatus,
                                                        OMRSRepositoryHelper repositoryHelper,
                                                        String               serviceName,
                                                        String               methodName) throws UserNotAuthorizedException,
                                                                                                InvalidParameterException,
                                                                                                PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (validateZoneAccess(userId,
                               anchorEntity.getClassifications(),
                               AccessOperation.UPDATE_STATUS,
                               Collections.singletonList(userId),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedAnchorAccess(userId,
                                      AccessOperation.UPDATE_STATUS.getName(),
                                      anchorEntity.getType().getTypeDefName(),
                                      anchorEntity.getGUID(),
                                      methodName);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorAttach(String               userId,
                                            EntityDetail         anchorEntity,
                                            EntityDetail         attachingEntity,
                                            String               relationshipName,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               methodName) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (! validateZoneAccess(userId,
                               anchorEntity.getClassifications(),
                               AccessOperation.ATTACH,
                               Collections.singletonList(userId),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.ATTACH.getName(),
                                          anchorEntity.getType().getTypeDefName(),
                                          anchorEntity.getGUID(),
                                          methodName);
        }

        if (validateZoneAccess(userId,
                               attachingEntity.getClassifications(),
                               AccessOperation.READ,
                               attachingEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.READ.getName(),
                                       attachingEntity.getGUID(),
                                       attachingEntity.getType().getTypeDefName(),
                                       methodName);
    }


    /**
     * Tests for whether a specific user should have the right to unlink anchored elements
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param detachingEntity  obsolete element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorDetach(String               userId,
                                            EntityDetail         anchorEntity,
                                            EntityDetail         detachingEntity,
                                            String               relationshipName,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               methodName) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (! validateZoneAccess(userId,
                                 anchorEntity.getClassifications(),
                                 AccessOperation.DETACH,
                                 Collections.singletonList(userId),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.DETACH.getName(),
                                          anchorEntity.getType().getTypeDefName(),
                                          anchorEntity.getGUID(),
                                          methodName);
        }

        if (validateZoneAccess(userId,
                               detachingEntity.getClassifications(),
                               AccessOperation.READ,
                               detachingEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.READ.getName(),
                                       detachingEntity.getGUID(),
                                       detachingEntity.getType().getTypeDefName(),
                                       methodName);
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags, and likes, to the anchor or member element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorAddFeedback(String               userId,
                                                 EntityDetail         anchorEntity,
                                                 EntityDetail         feedbackEntity,
                                                 OMRSRepositoryHelper repositoryHelper,
                                                 String               serviceName,
                                                 String               methodName) throws UserNotAuthorizedException,
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (! validateZoneAccess(userId,
                                 anchorEntity.getClassifications(),
                                 AccessOperation.ADD_FEEDBACK,
                                 Collections.singletonList(userId),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.ADD_FEEDBACK.getName(),
                                          anchorEntity.getType().getTypeDefName(),
                                          anchorEntity.getGUID(),
                                          methodName);
        }

        if (validateZoneAccess(userId,
                               feedbackEntity.getClassifications(),
                               AccessOperation.READ,
                               feedbackEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.READ.getName(),
                                       feedbackEntity.getGUID(),
                                       feedbackEntity.getType().getTypeDefName(),
                                       methodName);
    }


    /**
     * Tests for whether a specific user should have the right to detach feedback - such as comments,
     * ratings, tag,s and likes, to the anchor or member element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param feedbackEntity   feedback element
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorDeleteFeedback(String               userId,
                                                    EntityDetail         anchorEntity,
                                                    EntityDetail         feedbackEntity,
                                                    OMRSRepositoryHelper repositoryHelper,
                                                    String               serviceName,
                                                    String               methodName) throws UserNotAuthorizedException,
                                                                                            InvalidParameterException,
                                                                                            PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (! validateZoneAccess(userId,
                                 anchorEntity.getClassifications(),
                                 AccessOperation.DELETE_FEEDBACK,
                                 Collections.singletonList(userId),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.DELETE_FEEDBACK.getName(),
                                          anchorEntity.getType().getTypeDefName(),
                                          anchorEntity.getGUID(),
                                          methodName);
        }

        if (validateZoneAccess(userId,
                               feedbackEntity.getClassifications(),
                               AccessOperation.READ,
                               feedbackEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.READ.getName(),
                                       feedbackEntity.getGUID(),
                                       feedbackEntity.getType().getTypeDefName(),
                                       methodName);
    }


    /**
     * Tests for whether a specific user should have the right to add or update a classification on this anchor or member element.
     *
     * @param userId                   identifier of user
     * @param anchorEntity             anchor details
     * @param classificationName       name of the classification
     * @param classificationProperties new properties
     * @param repositoryHelper         helper for OMRS objects
     * @param serviceName              calling service
     * @param methodName               calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorClassify(String               userId,
                                              EntityDetail         anchorEntity,
                                              String               classificationName,
                                              InstanceProperties   classificationProperties,
                                              OMRSRepositoryHelper repositoryHelper,
                                              String               serviceName,
                                              String               methodName) throws UserNotAuthorizedException,
                                                                                      InvalidParameterException,
                                                                                      PropertyServerException
    {
        if (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classificationName))
        {
            if (validateZoneAccess(userId,
                                   anchorEntity.getClassifications(),
                                   AccessOperation.PUBLISH,
                                   anchorEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            List<String> originalZones = this.getZoneMembership(anchorEntity.getClassifications(),
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);
            List<String> newZones = repositoryHelper.getStringArrayProperty(serviceName,
                                                                            OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                            classificationProperties,
                                                                            methodName);

            throwUnauthorizedZoneChange(userId, anchorEntity.getGUID(), originalZones, newZones, methodName);
        }
        else
        {
            if (validateZoneAccess(userId,
                                   anchorEntity.getClassifications(),
                                   AccessOperation.CLASSIFY,
                                   anchorEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            throwUnauthorizedAnchorAccess(userId,
                                           AccessOperation.CLASSIFY.getName(),
                                           anchorEntity.getGUID(),
                                           anchorEntity.getType().getTypeDefName(),
                                           methodName);
        }
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorDeclassify(String               userId,
                                                EntityDetail         anchorEntity,
                                                String               classificationName,
                                                OMRSRepositoryHelper repositoryHelper,
                                                String               serviceName,
                                                String               methodName) throws UserNotAuthorizedException,
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException
    {
        if (OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName.equals(classificationName))
        {
            if (validateZoneAccess(userId,
                                   anchorEntity.getClassifications(),
                                   AccessOperation.PUBLISH,
                                   anchorEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            List<String> originalZones = this.getZoneMembership(anchorEntity.getClassifications(),
                                                                repositoryHelper,
                                                                serviceName,
                                                                methodName);
            List<String> newZones = new ArrayList<>();

            throwUnauthorizedZoneChange(userId, anchorEntity.getGUID(), originalZones, newZones, methodName);
        }
        else
        {
            if (validateZoneAccess(userId,
                                   anchorEntity.getClassifications(),
                                   AccessOperation.DECLASSIFY,
                                   anchorEntity.getMaintainedBy(),
                                   repositoryHelper,
                                   serviceName,
                                   methodName))
            {
                return;
            }

            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.DECLASSIFY.getName(),
                                          anchorEntity.getGUID(),
                                          anchorEntity.getType().getTypeDefName(),
                                          methodName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param obsoleteEntity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorMemberDelete(String               userId,
                                                  EntityDetail         anchorEntity,
                                                  EntityDetail         obsoleteEntity,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (! validateZoneAccess(userId,
                                 anchorEntity.getClassifications(),
                                 AccessOperation.DELETE_MEMBER,
                                 Collections.singletonList(userId),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.DELETE_MEMBER.getName(),
                                          anchorEntity.getType().getTypeDefName(),
                                          anchorEntity.getGUID(),
                                          methodName);
        }

        if (validateZoneAccess(userId,
                               obsoleteEntity.getClassifications(),
                               AccessOperation.READ,
                               obsoleteEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.READ.getName(),
                                       obsoleteEntity.getGUID(),
                                       obsoleteEntity.getType().getTypeDefName(),
                                       methodName);
    }


    /**
     * Tests for whether a specific user should have the right to delete an element and all of its contents.
     *
     * @param userId identifier of user
     * @param entity original element details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForElementDelete(String               userId,
                                             EntityDetail         entity,
                                             OMRSRepositoryHelper repositoryHelper,
                                             String               serviceName,
                                             String               methodName) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException
    {
        if (validateZoneAccess(userId,
                               entity.getClassifications(),
                               AccessOperation.DELETE,
                               entity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.DELETE.getName(),
                                       entity.getGUID(),
                                       entity.getType().getTypeDefName(),
                                       methodName);
    }


    /**
     * Tests for whether a specific user should have the right to attach a new anchored element.
     *
     * @param userId           identifier of user
     * @param anchorEntity     anchor details
     * @param newMemberEntity  feedback element
     * @param relationshipName name of the relationship
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName      calling service
     * @param methodName       calling method
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void validateUserForAnchorMemberAdd(String               userId,
                                               EntityDetail         anchorEntity,
                                               EntityDetail         newMemberEntity,
                                               String               relationshipName,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName,
                                               String               methodName) throws UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       PropertyServerException
    {
        /*
         * Check to see if there is a control for the operation
         */
        if (! validateZoneAccess(userId,
                                 anchorEntity.getClassifications(),
                                 AccessOperation.ADD_MEMBER,
                                 Collections.singletonList(userId),
                                 repositoryHelper,
                                 serviceName,
                                 methodName))
        {
            throwUnauthorizedAnchorAccess(userId,
                                          AccessOperation.ADD_MEMBER.getName(),
                                          anchorEntity.getType().getTypeDefName(),
                                          anchorEntity.getGUID(),
                                          methodName);
        }

        if (validateZoneAccess(userId,
                               newMemberEntity.getClassifications(),
                               AccessOperation.READ,
                               newMemberEntity.getMaintainedBy(),
                               repositoryHelper,
                               serviceName,
                               methodName))
        {
            return;
        }

        throwUnauthorizedElementAccess(userId,
                                       AccessOperation.READ.getName(),
                                       newMemberEntity.getGUID(),
                                       newMemberEntity.getType().getTypeDefName(),
                                       methodName);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public EntityDetail selectConnection(String               userId,
                                         EntityDetail         assetEntity,
                                         List<EntityDetail>   connectionEntities,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws UserNotAuthorizedException,
                                                                                 InvalidParameterException,
                                                                                 PropertyServerException
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
                        validateUserForElementRead(userId, connection, true, repositoryHelper, serviceName, methodName);
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException,
                                                                   InvalidParameterException,
                                                                   PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeCreate(String           userId,
                                           String           metadataCollectionName,
                                           AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException,
                                                                    InvalidParameterException,
                                                                    PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeRead(String           userId,
                                         String           metadataCollectionName,
                                         AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException,
                                                                                   InvalidParameterException,
                                                                                   PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to update a typeDef within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef current typeDef details
     * @param patch proposed changes to type
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeUpdate(String       userId,
                                           String       metadataCollectionName,
                                           TypeDef      typeDef,
                                           TypeDefPatch patch) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException,
                                                                      InvalidParameterException,
                                                                      PropertyServerException
    {

    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeDelete(String              userId,
                                           String              metadataCollectionName,
                                           AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException,
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException
    {
    }




    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeReIdentify(String  userId,
                                               String  metadataCollectionName,
                                               TypeDef originalTypeDef,
                                               String  newTypeDefGUID,
                                               String  newTypeDefName) throws UserNotAuthorizedException,
                                                                              InvalidParameterException,
                                                                              PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalAttributeTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForTypeReIdentify(String           userId,
                                               String           metadataCollectionName,
                                               AttributeTypeDef originalAttributeTypeDef,
                                               String           newTypeDefGUID,
                                               String           newTypeDefName) throws UserNotAuthorizedException,
                                                                                       InvalidParameterException,
                                                                                       PropertyServerException
    {
    }


    /*
     * =========================================================================================================
     * Instance Security
     *
     * No specific security checks are made when instances are written and retrieved from the local repository.
     * The methods override the super class that throws a UserNotAuthorizedException on all access/update
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityCreate(String                     userId,
                                             String                     metadataCollectionName,
                                             String                     entityTypeGUID,
                                             InstanceProperties         initialProperties,
                                             List<Classification>       initialClassifications,
                                             InstanceStatus             initialStatus) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return entity to return (maybe altered by the connector)
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public EntityDetail  validateUserForEntityRead(String       userId,
                                                   String       metadataCollectionName,
                                                   EntityDetail instance) throws UserNotAuthorizedException,
                                                                                 InvalidParameterException,
                                                                                 PropertyServerException
    {
        return instance;
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException,
                                                                                 InvalidParameterException,
                                                                                 PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException,
                                                                             InvalidParameterException,
                                                                             PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException,
                                                                              InvalidParameterException,
                                                                              PropertyServerException
    {
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityClassificationAdd(String               userId,
                                                        String               metadataCollectionName,
                                                        EntitySummary        instance,
                                                        String               classificationName,
                                                        InstanceProperties   properties) throws UserNotAuthorizedException,
                                                                                                InvalidParameterException,
                                                                                                PropertyServerException
    {
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
      * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
      * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
      * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityClassificationUpdate(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException,
                                                                                                   InvalidParameterException,
                                                                                                   PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to delete a classification from an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityClassificationDelete(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName) throws UserNotAuthorizedException,
                                                                                                           InvalidParameterException,
                                                                                                           PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException,
                                                                           InvalidParameterException,
                                                                           PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to restore an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityRestore(String       userId,
                                              String       metadataCollectionName,
                                              String       deletedEntityGUID) throws UserNotAuthorizedException,
                                                                                     InvalidParameterException,
                                                                                     PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityReIdentification(String       userId,
                                                       String       metadataCollectionName,
                                                       EntityDetail instance,
                                                       String       newGUID) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the type name of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityReTyping(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException,
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForEntityReHoming(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               String         newHomeMetadataCollectionId,
                                               String         newHomeMetadataCollectionName) throws UserNotAuthorizedException,
                                                                                                    InvalidParameterException,
                                                                                                    PropertyServerException
    {
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
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForRelationshipCreate(String               userId,
                                                   String               metadataCollectionName,
                                                   String               relationshipTypeGUID,
                                                   InstanceProperties   initialProperties,
                                                   EntitySummary        entityOneSummary,
                                                   EntitySummary        entityTwoSummary,
                                                   InstanceStatus       initialStatus) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return relationship to return (maybe altered by the connector)
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public Relationship validateUserForRelationshipRead(String          userId,
                                                        String          metadataCollectionName,
                                                        Relationship    instance) throws UserNotAuthorizedException,
                                                                                         InvalidParameterException,
                                                                                         PropertyServerException
    {
        return instance;
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException,
                                                                                    InvalidParameterException,
                                                                                    PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException,
                                                                                 InvalidParameterException,
                                                                                 PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to restore an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForRelationshipRestore(String       userId,
                                                    String       metadataCollectionName,
                                                    String       deletedRelationshipGUID) throws UserNotAuthorizedException,
                                                                                                 InvalidParameterException,
                                                                                                 PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForRelationshipReIdentification(String       userId,
                                                             String       metadataCollectionName,
                                                             Relationship instance,
                                                             String       newGUID) throws UserNotAuthorizedException,
                                                                                          InvalidParameterException,
                                                                                          PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the type name of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForRelationshipReTyping(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException
    {
    }


    /**
     * Tests for whether a specific user should have the right to change the home of a instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    @Override
    public void  validateUserForRelationshipReHoming(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     String         newHomeMetadataCollectionId,
                                                     String         newHomeMetadataCollectionName) throws UserNotAuthorizedException,
                                                                                                          InvalidParameterException,
                                                                                                          PropertyServerException
    {
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param userId identifier of user
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */

    public boolean  validateEntityReferenceCopySave(String       userId,
                                                    EntityDetail instance) throws UserNotAuthorizedException,
                                                                                  InvalidParameterException,
                                                                                  PropertyServerException
    {
        return true;
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param userId identifier of user
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     * @throws InvalidParameterException  one of the elements is invisible to the requesting user.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws PropertyServerException    unable to retrieve necessary information to make the decision.
     */
    public boolean  validateRelationshipReferenceCopySave(String       userId,
                                                          Relationship instance) throws UserNotAuthorizedException,
                                                                                        InvalidParameterException,
                                                                                        PropertyServerException
    {
        return true;
    }
}
