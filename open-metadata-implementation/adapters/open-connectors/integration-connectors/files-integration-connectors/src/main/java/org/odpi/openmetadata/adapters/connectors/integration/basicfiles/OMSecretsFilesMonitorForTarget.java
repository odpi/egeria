/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.adapters.connectors.EgeriaInformationSupplyChainDefinition;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsFileConnector;
import org.odpi.openmetadata.adapters.connectors.secretsstore.yaml.YAMLSecretsStoreConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.users.*;
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataClassificationBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.UserIdentityClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.SecretsCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileExtension;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceZoneName;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;


public class OMSecretsFilesMonitorForTarget extends DataFilesMonitorForTarget
{
    private static final Logger log = LoggerFactory.getLogger(OMSecretsFilesMonitorForTarget.class);

    private final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();


    /**
     * Construct the monitor for a specific catalog target.
     *
     * @param connectorName name of associated connector
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param configurationProperties parameters to further modify the behaviour of the connector
     * @param integrationConnector associated connector
     * @param dataFolderElement Egeria element for this directory
     * @param auditLog logging destination
     */
    public OMSecretsFilesMonitorForTarget(String                                    connectorName,
                                          String                                    sourceName,
                                          String                                    pathName,
                                          String                                    catalogTargetGUID,
                                          DeleteMethod                              deleteMethod,
                                          Map<String,String>                        templates,
                                          Map<String, Object>                       configurationProperties,
                                          BasicFilesMonitorIntegrationConnectorBase integrationConnector,
                                          OpenMetadataRootElement                   dataFolderElement,
                                          AuditLog                                  auditLog)
    {
        super(connectorName,
              sourceName,
              pathName,
              catalogTargetGUID,
              deleteMethod,
              templates,
              configurationProperties,
              integrationConnector,
              dataFolderElement,
              auditLog);
    }


    /**
     * Return the unique identifier of a new metadata element describing the file.
     *
     * @param typeName subtype name for file
     * @param properties basic properties to use
     * @param encodingProperties properties for DataAssetEncoding classification
     * @return unique identifier (guid)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileToCatalog(String                      typeName,
                                          DataFileProperties          properties,
                                          DataAssetEncodingProperties encodingProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        String fileAssetGUID = super.addDataFileToCatalog(OpenMetadataType.ARCHIVE_FILE.typeName,
                                                          properties,
                                                          null);

        if (FileExtension.OM_SECRETS.getFileExtension().equals(properties.getFileExtension()))
        {
            this.catalogSecretsCollections(fileAssetGUID);
        }
        else
        {
            log.debug("ignoring file " + properties);
        }

        return fileAssetGUID;
    }


    /**
     * Return the unique identifier of a new metadata element describing the file created using the supplied template.
     *
     * @param assetTypeName type of asset to create
     * @param fileTemplateGUID template to use
     * @param replacementProperties properties from the template to replace
     * @param placeholderProperties values to use to replace placeholders in the template
     * @return unique identifier (guid)
     * @throws ConnectorCheckedException connector has been shutdown
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileViaTemplate(String              assetTypeName,
                                            String              fileTemplateGUID,
                                            ElementProperties   replacementProperties,
                                            Map<String, String> placeholderProperties) throws ConnectorCheckedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        String fileGUID = super.addDataFileViaTemplate(assetTypeName, fileTemplateGUID, replacementProperties, placeholderProperties);

        if (FileExtension.OM_SECRETS.getFileExtension().equals(placeholderProperties.get(PlaceholderProperty.FILE_EXTENSION.getName())))
        {
            this.catalogSecretsCollections(fileGUID);
        }
        else
        {
            log.debug("ignoring file " + placeholderProperties);
        }

        return fileGUID;
    }


    /**
     * Catalog the secrets collections in the archive file.
     *
     * @param fileAssetGUID unique identifier of the file to open
     * @throws UserNotAuthorizedException security problems
     */
    private void catalogSecretsCollections(String fileAssetGUID) throws UserNotAuthorizedException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException
    {
        final String methodName = "catalogSecretsCollections";

        try
        {
            Connector connector = integrationConnector.integrationContext.getConnectedAssetContext().getConnectorForAsset(fileAssetGUID, auditLog);

            if (connector instanceof YAMLSecretsFileConnector yamlSecretsFileConnector)
            {
                yamlSecretsFileConnector.start();
                SecretsStore secretsStore = yamlSecretsFileConnector.getSecretsStore();

                if (secretsStore != null)
                {
                    for (String secretsCollectionName : secretsStore.getSecretsCollections().keySet())
                    {
                        yamlSecretsFileConnector.setSecretsCollectionName(secretsCollectionName);

                        SecretsCollection secretsCollection = secretsStore.getSecretsCollections().get(secretsCollectionName);

                        if (secretsCollection != null)
                        {
                            String secretsCollectionGUID = catalogSecretsCollection(secretsCollectionName,
                                                                                    secretsCollection,
                                                                                    fileAssetGUID,
                                                                                    yamlSecretsFileConnector);
                        }
                    }
                }
            }
        }
        catch (ClassCastException | RepositoryErrorException | ConnectionCheckedException | ConnectorCheckedException error)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   error.getMessage()));
        }
    }


    /**
     * Catalog the secrets collection in the metadata repository.
     *
     * @param secretsCollectionName name of the secrets collection
     * @param secretsCollection     collection of secrets
     * @param fileAssetGUID         unique identifier of the file asset
     * @return unique identifier of the secrets collection
     * @throws UserNotAuthorizedException security problems
     * @throws InvalidParameterException  invalid parameter
     * @throws PropertyServerException    problem accessing the property server
     * @throws ConnectorCheckedException  Connector is not initialized
     */
    private String catalogSecretsCollection(String                    secretsCollectionName,
                                            SecretsCollection         secretsCollection,
                                            String                    fileAssetGUID,
                                            YAMLSecretsStoreConnector secretsStoreConnector) throws UserNotAuthorizedException,
                                                                                                    InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    ConnectorCheckedException
    {
        final String methodName = "catalogSecretsCollection";

        OpenMetadataStore openMetadataStore = integrationConnector.integrationContext.getOpenMetadataStore();

        String qualifiedName = OpenMetadataType.SECRETS_COLLECTION.typeName + "::" + fileAssetGUID + "::" + secretsCollectionName;

        String secretsCollectionGUID = openMetadataStore.getMetadataElementGUIDByUniqueName(qualifiedName,
                                                                                            OpenMetadataProperty.QUALIFIED_NAME.name);
        if (secretsCollectionGUID == null)
        {
            NewElementOptions options = new NewElementOptions();

            options.setIsOwnAnchor(true);
            options.setParentGUID(fileAssetGUID);
            options.setParentAtEnd1(false);
            options.setParentRelationshipTypeName(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName);

            SecretsCollectionProperties secretsCollectionProperties = new SecretsCollectionProperties();

            secretsCollectionProperties.setQualifiedName(qualifiedName);
            secretsCollectionProperties.setResourceName(secretsCollectionName);
            secretsCollectionProperties.setDisplayName(secretsCollection.getDisplayName());
            secretsCollectionProperties.setDescription(secretsCollection.getDescription());

            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put(OpenMetadataProperty.REFRESH_TIME_INTERVAL.name, Long.toString(secretsCollection.getRefreshTimeInterval()));

            secretsCollectionProperties.setAdditionalProperties(additionalProperties);

            DataSetContentProperties dataSetContentProperties = new DataSetContentProperties();

            dataSetContentProperties.setISCQualifiedName(EgeriaInformationSupplyChainDefinition.SECURITY.getQualifiedName());

            /*
             * The secrets collection is not added to the security zone because it need to be visible to the data
             * engineers configuring cataloguing.
             */
            secretsCollectionGUID = integrationConnector.integrationContext.getAssetClient().createAsset(options,
                                                                                                         null,
                                                                                                         secretsCollectionProperties,
                                                                                                         dataSetContentProperties);
        }

        Map<String, String> userMap = new HashMap<>();
        Map<String, String> listMap = new HashMap<>();

        if (secretsCollection.getUsers() != null)
        {
            long userAccountCount       = 0L;
            long employeeAccountCount   = 0L;
            long contractorAccountCount = 0L;
            long externalAccountCount   = 0L;
            long digitalAccountCount    = 0L;
            long activeAccountCount     = 0L;
            long expiredAccountCount    = 0L;
            long lockedAccountCount     = 0L;
            long disabledAccountCount   = 0L;

            /*
             * Iterate through user accounts and extract useful information.
             */
            for (String userId : secretsCollection.getUsers().keySet())
            {
                /*
                 * Retrieve user account from secrets store connector.  The connector is
                 * used because it means the lists are properly filled out.
                 */
                UserAccount userAccount = secretsStoreConnector.getUser(userId);

                if (userAccount != null)
                {
                    userAccountCount++;

                    if (userAccount.getUserAccountStatus() == UserAccountStatus.AVAILABLE)
                    {
                        activeAccountCount++;
                    }
                    else if (userAccount.getUserAccountStatus() == UserAccountStatus.CREDENTIALS_EXPIRED)
                    {
                        expiredAccountCount++;
                    }
                    else if (userAccount.getUserAccountStatus() == UserAccountStatus.LOCKED)
                    {
                        lockedAccountCount++;
                    }
                    else if (userAccount.getUserAccountStatus() == UserAccountStatus.DISABLED)
                    {
                        disabledAccountCount++;
                    }

                    if (userAccount.getUserAccountType() == UserAccountType.EMPLOYEE)
                    {
                        employeeAccountCount++;
                    }
                    else if (userAccount.getUserAccountType() == UserAccountType.CONTRACTOR)
                    {
                        contractorAccountCount++;
                    }
                    else if (userAccount.getUserAccountType() == UserAccountType.EXTERNAL)
                    {
                        externalAccountCount++;
                    }
                    else if (userAccount.getUserAccountType() == UserAccountType.DIGITAL)
                    {
                        digitalAccountCount++;
                    }

                    String userIdentityGUID = processUserIdentity(userId, userAccount);
                    if (userIdentityGUID != null)
                    {
                        userMap.put(userId, userIdentityGUID);
                    }
                }
            }

            if (userAccountCount > 0)
            {
                UserAccountProfileProperties userAccountProfileProperties = new UserAccountProfileProperties();

                userAccountProfileProperties.setUserAccountCount(userAccountCount);
                userAccountProfileProperties.setActiveAccountCount(activeAccountCount);
                userAccountProfileProperties.setExpiredAccountCount(expiredAccountCount);
                userAccountProfileProperties.setLockedAccountCount(lockedAccountCount);
                userAccountProfileProperties.setDisabledAccountCount(disabledAccountCount);
                userAccountProfileProperties.setEmployeeAccountCount(employeeAccountCount);
                userAccountProfileProperties.setContractorAccountCount(contractorAccountCount);
                userAccountProfileProperties.setDigitalAccountCount(digitalAccountCount);
                userAccountProfileProperties.setExternalAccountCount(externalAccountCount);

                integrationConnector.integrationContext.getOpenMetadataStore().classifyMetadataElementInStore(secretsCollectionGUID,
                                                                                                              OpenMetadataType.USER_ACCOUNT_PROFILE_CLASSIFICATION.typeName,
                                                                                                              null,
                                                                                                              classificationBuilder.getNewElementProperties(userAccountProfileProperties));
            }
        }

        if (secretsCollection.getNamedLists() != null)
        {
            CollectionClient collectionClient = integrationConnector.integrationContext.getCollectionClient(OpenMetadataType.SECURITY_LIST.typeName);

            /*
             * Iterate through the security groups and roles, extracting useful information.
             */
            for (String listName : secretsCollection.getNamedLists().keySet())
            {
                NewElementOptions options = new NewElementOptions();

                options.setAnchorGUID(secretsCollectionGUID);
                options.setIsOwnAnchor(false);
                options.setParentGUID(secretsCollectionGUID);
                options.setParentAtEnd1(false);
                options.setParentRelationshipTypeName(OpenMetadataType.SECRETS_COLLECTION_SECURITY_LIST_RELATIONSHIP.typeName);


                if (listName != null)
                {

                }
            }
        }

        if (secretsCollection.getSecurityAccessControls() != null)
        {
            /*
             * Iterate through the security groups and roles, extracting useful information.
             */
            for (String controlName : secretsCollection.getSecurityAccessControls().keySet())
            {
                if (controlName != null)
                {
                    /*
                     * Retrieve the control from secrets store connector.  The connector is
                     * used because it means the controls are properly filled out.
                     */
                    SecurityAccessControl securityAccessControl = secretsStoreConnector.getSecurityAccessControl(controlName);

                    if (securityAccessControl != null)
                    {
                        String controlQualifiedName = OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName + "::" + secretsCollectionGUID + "::" + controlName;
                        SecurityAccessControlProperties securityAccessControlProperties = new SecurityAccessControlProperties();

                        if (OpenMetadataType.GOVERNANCE_ZONE.typeName.equals(securityAccessControl.getControlTypeName()))
                        {
                            securityAccessControlProperties = new GovernanceZoneProperties();
                        }
                        else if (OpenMetadataType.SERVICE_ACCESS_CONTROL.typeName.equals(securityAccessControl.getControlTypeName()))
                        {
                            ServiceAccessControlProperties serviceAccessControlProperties= new ServiceAccessControlProperties();

                            serviceAccessControlProperties.setMappingProperties(securityAccessControl.getMappingProperties());
                            securityAccessControlProperties = serviceAccessControlProperties;
                        }

                        securityAccessControlProperties.setQualifiedName(controlQualifiedName);
                        securityAccessControlProperties.setIdentifier(controlName);
                        securityAccessControlProperties.setDisplayName(securityAccessControl.getDisplayName());
                        securityAccessControlProperties.setDescription(securityAccessControl.getDescription());

                        String securityAccessControlGUID = openMetadataStore.getMetadataElementGUIDByUniqueName(qualifiedName,
                                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name);
                        if (securityAccessControlGUID == null)
                        {
                            NewElementOptions options = new NewElementOptions();
                        }
                    }
                }
            }
        }

        return secretsCollectionGUID;
    }


    /**
     * If there is a corresponding userIdentity, set up appropriate classifications and return its GUID.
     *
     * @param userId      userId for user account
     * @param userAccount corresponding user account
     * @return guid or null
     * @throws InvalidParameterException  invalid parameter
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to perform this operation
     */
    private String processUserIdentity(String      userId,
                                       UserAccount userAccount) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        UserIdentityClient userIdentityClient = integrationConnector.integrationContext.getUserIdentityClient();

        OpenMetadataRootElement userIdentity = userIdentityClient.getUserIdentityByUserId(userId, null);

        if (userIdentity != null)
        {
            /*
             * Decorate the user identity with the groups and roles from the user account.  The security
             * connector does an iterative navigation through the lists to return all the security groups
             * and roles for the user.  This means we can take the lists from the user account and
             * add them to the user identity.
             */
            SecurityListMembershipProperties securityListMembershipProperties = new SecurityListMembershipProperties();

            securityListMembershipProperties.setSecurityGroups(userAccount.getSecurityGroups());
            securityListMembershipProperties.setSecurityRoles(userAccount.getSecurityRoles());

            if (userIdentity.getElementHeader().getSecurityListMembership() == null)
            {
                integrationConnector.integrationContext.getOpenMetadataStore().classifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                              OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                              null,
                                                                                                              classificationBuilder.getNewElementProperties(securityListMembershipProperties));
            }
            else
            {
                integrationConnector.integrationContext.getOpenMetadataStore().reclassifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                                OpenMetadataType.SECURITY_LIST_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                                null,
                                                                                                                classificationBuilder.getNewElementProperties(securityListMembershipProperties));
            }

            /*
             * The user identity should be in the security zone.
             */
            if (userIdentity.getElementHeader().getZoneMembership() == null)
            {
                ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();

                zoneMembershipProperties.setZoneMembership(Collections.singletonList(GovernanceZoneName.SECURITY.getZoneName()));

                integrationConnector.integrationContext.getOpenMetadataStore().classifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                              OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                              null,
                                                                                                              classificationBuilder.getNewElementProperties(zoneMembershipProperties));
            }
            else if (userIdentity.getElementHeader().getZoneMembership().getClassificationProperties() instanceof ZoneMembershipProperties zoneMembershipProperties)
            {
                if (zoneMembershipProperties.getZoneMembership() == null)
                {
                    zoneMembershipProperties.setZoneMembership(Collections.singletonList(GovernanceZoneName.SECURITY.getZoneName()));

                    integrationConnector.integrationContext.getOpenMetadataStore().reclassifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                                    OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                                    null,
                                                                                                                    classificationBuilder.getNewElementProperties(zoneMembershipProperties));
                }
                else if (!zoneMembershipProperties.getZoneMembership().contains(GovernanceZoneName.SECURITY.getZoneName()))
                {
                    zoneMembershipProperties.getZoneMembership().add(GovernanceZoneName.SECURITY.getZoneName());

                    integrationConnector.integrationContext.getOpenMetadataStore().reclassifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                                    OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                                    null,
                                                                                                                    classificationBuilder.getNewElementProperties(zoneMembershipProperties));
                }
            }
            return userIdentity.getElementHeader().getGUID();
        }

        return null;
    }
}
