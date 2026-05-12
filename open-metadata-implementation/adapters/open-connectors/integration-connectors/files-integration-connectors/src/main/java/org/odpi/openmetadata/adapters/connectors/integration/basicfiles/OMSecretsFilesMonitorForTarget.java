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
import org.odpi.openmetadata.frameworks.openmetadata.builders.OpenMetadataRelationshipBuilder;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.SecretsCollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.UserAccountProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.FileExtension;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.GovernanceZoneName;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;


public class OMSecretsFilesMonitorForTarget extends DataFilesMonitorForTarget
{
    private static final Logger log = LoggerFactory.getLogger(OMSecretsFilesMonitorForTarget.class);

    private final OpenMetadataClassificationBuilder classificationBuilder = new OpenMetadataClassificationBuilder();
    private final OpenMetadataRelationshipBuilder   relationshipBuilder   = new OpenMetadataRelationshipBuilder();


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
     * Refresh the metadata for the folder.
     */
    @Override
    public void refresh()
    {
        super.refresh();

        final String methodName = "refresh";

        try
        {
            processUserIdentities();
            refreshSecurityAccessControls();
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                       error.getClass().getName(),
                                                                                                                       methodName,
                                                                                                                       error.getMessage()));
            }
        }
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
     * Catalog a file.
     *
     * @param file       Java File accessor
     * @param methodName calling method
     * @return unique identifier of the file asset
     */
    protected synchronized String catalogFile(File  file,
                                              String methodName)
    {
        return super.catalogFile(file, methodName);
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
            if (secretsCollection.getDisplayName() == null)
            {
                secretsCollectionProperties.setDisplayName(secretsCollectionName);
            }
            else
            {
                secretsCollectionProperties.setDisplayName(secretsCollection.getDisplayName());
            }
            secretsCollectionProperties.setDescription(secretsCollection.getDescription());

            Map<String, String> additionalProperties = new HashMap<>();

            additionalProperties.put(OpenMetadataProperty.REFRESH_TIME_INTERVAL.name, Long.toString(secretsCollection.getRefreshTimeInterval()));

            secretsCollectionProperties.setAdditionalProperties(additionalProperties);

            DataSetContentProperties dataSetContentProperties = new DataSetContentProperties();

            dataSetContentProperties.setISCQualifiedName(EgeriaInformationSupplyChainDefinition.SECURITY.getQualifiedName());

            /*
             * The secrets collection is not added to the security zone because it needs to be visible to the data
             * engineers configuring cataloguing.
             */
            secretsCollectionGUID = integrationConnector.integrationContext.getAssetClient().createAsset(options,
                                                                                                         null,
                                                                                                         secretsCollectionProperties,
                                                                                                         dataSetContentProperties);
        }

        AssetClient assetClient = integrationConnector.integrationContext.getAssetClient(OpenMetadataType.SECRETS_COLLECTION.typeName);

        OpenMetadataRootElement secretsCollectionElement = assetClient.getAssetByGUID(secretsCollectionGUID, assetClient.getGetOptions());


        /*
         * Users are not catalogued by this connector - however, a profile of the connected users is added
         * as a classification to the secrets collection.
         */
        if (secretsCollection.getUsers() != null)
        {
            UserAccountProfileProperties userAccountProfileProperties = this.getUserAccountProfileProperties(secretsCollection, secretsStoreConnector);

            integrationConnector.integrationContext.getOpenMetadataStore().classifyMetadataElementInStore(secretsCollectionGUID,
                                                                                                          OpenMetadataType.USER_ACCOUNT_PROFILE_CLASSIFICATION.typeName,
                                                                                                          null,
                                                                                                          classificationBuilder.getNewElementProperties(userAccountProfileProperties));
        }

        /*
         * Now check that the security lists are synchronized.
         */
        Map<String, String> listMap;

        if (secretsCollection.getNamedLists() != null)
        {
            /*
             * This will create/update all lists in the secrets store - the map is for easy lookup
             */
            listMap = this.getListMap(secretsCollectionGUID, secretsCollection);
        }
        else
        {
            listMap = new HashMap<>();
        }

        /*
         * Check that none of the existing lists have been deleted.
         */
        if (secretsCollectionElement.getSecurityLists() != null)
        {
            for (RelatedMetadataElementSummary existingSecurityList : secretsCollectionElement.getSecurityLists())
            {
                if ((existingSecurityList != null) &&
                        (existingSecurityList.getRelatedElement().getProperties() instanceof SecurityListProperties securityListProperties))
                {
                    String securityListGUID = listMap.get(securityListProperties.getDisplayName());

                    if (securityListGUID == null)
                    {
                        openMetadataStore.deleteMetadataElementInStore(existingSecurityList.getRelatedElement().getElementHeader().getGUID(), false);
                    }
                }
            }
        }

        /*
         * Finally, work through the security access controls.
         * The relationships to the security list are multi-link. This means that
         * the management of the relationships needs to be more controlled.
         * First, get details of all the existing relationships for each control.
         */
        Map<String, Map<String, Map<String, String>>> existingControlOperations = this.getExistingControlOperations(secretsCollectionElement);

        /*
         * Make sure each defined control is created, linked to the right security lists and
         * the details added to the control map.
         */
        List<String> controlRelationshipList = this.setUpControls(secretsCollectionGUID,
                                                                  secretsCollection,
                                                                  listMap,
                                                                  existingControlOperations);

        /*
         * Now delete the relationships that are no longer required.
         */
        if (secretsCollectionElement.getSecurityAccessControls() != null)
        {
            for (RelatedMetadataElementSummary existingControl : secretsCollectionElement.getSecurityAccessControls())
            {
                if ((existingControl instanceof RelatedMetadataHierarchySummary hierarchySummary) &&
                        (hierarchySummary.getNestedElements() != null))
                {
                    for (RelatedMetadataElementSummary nestedElement : hierarchySummary.getNestedElements())
                    {
                        if (nestedElement != null)
                        {
                            if (! controlRelationshipList.contains(nestedElement.getRelationshipHeader().getGUID()))
                            {
                                openMetadataStore.deleteRelationshipInStore(nestedElement.getRelationshipHeader().getGUID());
                            }
                        }
                    }
                }
            }
        }


        return secretsCollectionGUID;
    }


    /**
     * Retrieve the control map for the secrets collection.
     *
     * @param secretsCollectionGUID unique identifier of the secrets collection
     * @param secretsCollection collection of secrets
     * @param listMap map of security list names to GUIDs
     * @param existingControlOperations existing control operations
     * @return list of AssociatedSecurityList relationship GUIDs that are valid
     */
    private List<String> setUpControls(String                                        secretsCollectionGUID,
                                       SecretsCollection                             secretsCollection,
                                       Map<String, String>                           listMap,
                                       Map<String, Map<String, Map<String, String>>> existingControlOperations) throws UserNotAuthorizedException, InvalidParameterException, PropertyServerException
    {
        List<String> relationshipList = new ArrayList<>();

        OpenMetadataStore          openMetadataStore = integrationConnector.integrationContext.getOpenMetadataStore();
        GovernanceDefinitionClient governanceDefinitionClient = integrationConnector.integrationContext.getGovernanceDefinitionClient(OpenMetadataType.SECURITY_ACCESS_CONTROL.typeName);

        if (secretsCollection.getSecurityAccessControls() != null)
        {
            Map<String, String> controlNameToGUIDMap = new HashMap<>();

            /*
             * Iterate through the security access controls, extracting useful information.
             */
            for (String controlName : secretsCollection.getSecurityAccessControls().keySet())
            {
                if (controlName != null)
                {
                    /*
                     * Retrieve the control from secrets store connector.  The connector is not used because it fills out
                     * all the derived lists for access control validation.
                     * We want the controls modelled here as they are defined.
                     */
                    SecurityAccessControl securityAccessControl = secretsCollection.getSecurityAccessControls().get(controlName);

                    if (securityAccessControl != null)
                    {
                        /*
                         * Create or update the control description in open metadata.
                         */
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

                        String controlQualifiedName = securityAccessControlProperties.getTypeName() + "::" + secretsCollectionGUID + "::" + controlName;

                        securityAccessControlProperties.setQualifiedName(controlQualifiedName);
                        securityAccessControlProperties.setIdentifier(controlName);
                        securityAccessControlProperties.setDisplayName(securityAccessControl.getControlDisplayName());
                        securityAccessControlProperties.setDescription(securityAccessControl.getDescription());

                        if (securityAccessControl.getOtherProperties() != null)
                        {
                            Object criteria = securityAccessControl.getOtherProperties().get(OpenMetadataProperty.CRITERIA.name);

                            if ((criteria != null) && (securityAccessControlProperties instanceof GovernanceZoneProperties governanceZoneProperties))
                            {
                                governanceZoneProperties.setCriteria(criteria.toString());
                            }

                            Object domainIdentifier = securityAccessControl.getOtherProperties().get(OpenMetadataProperty.DOMAIN_IDENTIFIER.name);
                            if (domainIdentifier instanceof Integer integer)
                            {
                                securityAccessControlProperties.setDomainIdentifier(integer);
                            }
                            else if (domainIdentifier instanceof String string)
                            {
                                securityAccessControlProperties.setDomainIdentifier(Integer.parseInt(string));
                            }
                        }

                        String securityAccessControlGUID = openMetadataStore.getMetadataElementGUIDByUniqueName(controlQualifiedName,
                                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name);
                        if (securityAccessControlGUID == null)
                        {
                            NewElementOptions newElementOptions = new NewElementOptions();

                            newElementOptions.setAnchorGUID(secretsCollectionGUID);
                            newElementOptions.setIsOwnAnchor(false);
                            newElementOptions.setParentGUID(secretsCollectionGUID);
                            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.RESOURCE_PERMISSIONS_RELATIONSHIP.typeName);

                            securityAccessControlGUID = governanceDefinitionClient.createGovernanceDefinition(newElementOptions,
                                                                                                              null,
                                                                                                              securityAccessControlProperties,
                                                                                                              null);
                        }
                        else
                        {
                            governanceDefinitionClient.updateGovernanceDefinition(securityAccessControlGUID,
                                                                                  governanceDefinitionClient.getUpdateOptions(false),
                                                                                  securityAccessControlProperties);
                        }

                        controlNameToGUIDMap.put(controlName, securityAccessControlGUID);

                        /*
                         * Now set up the AssociatedSecurityList relationships that describe the permissions for the
                         * control.
                         */
                        Map<String, Map<String, String>> existingOperations = existingControlOperations.get(controlName);

                        if (existingOperations == null)
                        {
                            existingOperations = new HashMap<>();
                        }

                        if (securityAccessControl.getAssociatedSecurityList() != null)
                        {
                            for (String operationName : securityAccessControl.getAssociatedSecurityList().keySet())
                            {
                                if (operationName != null)
                                {
                                    AssociatedSecurityListProperties associatedSecurityListProperties = new AssociatedSecurityListProperties();
                                    associatedSecurityListProperties.setOperationName(operationName);

                                    Map<String, String> existingSecurityLists = existingOperations.get(operationName);

                                    if (existingSecurityLists == null)
                                    {
                                        existingSecurityLists = new HashMap<>();
                                    }

                                    List<String> securityListForControl = securityAccessControl.getAssociatedSecurityList().get(operationName);

                                    if (securityListForControl != null)
                                    {
                                        for (String securityListName : securityListForControl)
                                        {
                                            if (securityListName != null)
                                            {
                                                String securityListGUID = listMap.get(securityListName);

                                                if (securityListGUID != null)
                                                {
                                                    String relationshipGUID = existingSecurityLists.get(securityListGUID);
                                                    if (relationshipGUID == null)
                                                    {
                                                        relationshipGUID = openMetadataStore.createRelatedElementsInStore(OpenMetadataType.ASSOCIATED_SECURITY_LIST_RELATIONSHIP.typeName,
                                                                                                                          securityAccessControlGUID,
                                                                                                                          securityListGUID,
                                                                                                                          openMetadataStore.getMakeAnchorOptions(false),
                                                                                                                          relationshipBuilder.getNewElementProperties(associatedSecurityListProperties));
                                                    }

                                                    relationshipList.add(relationshipGUID);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*
             * Iterate through the security access controls, extracting useful information.
             */
            for (String controlName : secretsCollection.getSecurityAccessControls().keySet())
            {
                if (controlName != null)
                {
                    SecurityAccessControl securityAccessControl = secretsCollection.getSecurityAccessControls().get(controlName);

                    if ((securityAccessControl != null) && (securityAccessControl.getOtherProperties() != null))
                    {
                        Object inheritsFromZone = securityAccessControl.getOtherProperties().get("inheritsFromZone");
                        if (inheritsFromZone != null)
                        {
                            String parentZoneName = inheritsFromZone.toString();
                            if (parentZoneName != null)
                            {
                                String parentZoneGUID = controlNameToGUIDMap.get(parentZoneName);
                                String childZoneName  = controlNameToGUIDMap.get(controlName);

                                if ((parentZoneGUID != null) && (childZoneName != null))
                                {
                                    governanceDefinitionClient.linkGovernanceZones(parentZoneGUID,
                                                                                   childZoneName,
                                                                                   governanceDefinitionClient.getMakeAnchorOptions(false),
                                                                                   null);
                                }
                            }
                        }
                    }
                }
            }
        }

        return relationshipList;
    }

    /**
     * Retrieves summary profile properties for user accounts by analyzing their statuses and types.
     * The method processes user details from the given SecretsCollection and interacts with the
     * provided YAMLSecretsStoreConnector to retrieve additional user account details.
     *
     * @param secretsCollection      the collection of secrets containing user identifiers to process
     * @param secretsStoreConnector  a connector to the secrets store, used to fetch user account details
     * @return a UserAccountProfileProperties object containing aggregated profile data
     *         such as counts of different user account statuses and types, or null if no accounts exist
     * @throws ConnectorCheckedException if an error occurs while retrieving user account information
     */
    private UserAccountProfileProperties getUserAccountProfileProperties(SecretsCollection         secretsCollection,
                                                                         YAMLSecretsStoreConnector secretsStoreConnector) throws ConnectorCheckedException
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

            Map<String, Long> userAccountStatusCounts = new HashMap<>();

            userAccountStatusCounts.put(UserAccountType.EMPLOYEE.getName(), employeeAccountCount);
            userAccountStatusCounts.put(UserAccountType.CONTRACTOR.getName(), contractorAccountCount);
            userAccountStatusCounts.put(UserAccountType.DIGITAL.getName(), digitalAccountCount);
            userAccountStatusCounts.put(UserAccountType.EXTERNAL.getName(), externalAccountCount);

            userAccountProfileProperties.setUserAccountTypes(userAccountStatusCounts);

            userAccountStatusCounts = new HashMap<>();

            userAccountStatusCounts.put(UserAccountStatus.AVAILABLE.getName(), activeAccountCount);
            userAccountStatusCounts.put(UserAccountStatus.CREDENTIALS_EXPIRED.getName(), expiredAccountCount);
            userAccountStatusCounts.put(UserAccountStatus.LOCKED.getName(), lockedAccountCount);
            userAccountStatusCounts.put(UserAccountStatus.DISABLED.getName(), disabledAccountCount);

            userAccountProfileProperties.setUserAccountStatuses(userAccountStatusCounts);

            return userAccountProfileProperties;
        }

        return null;
    }


    /**
     * This will create/update all lists in the secrets store - the returned map is for easy lookup.
     * All lists are created in the "security" zone, so they are only visible to security officers.
     *
     * @param secretsCollectionGUID unique identifier of the secrets collection that is the anchor of the lists.
     * @param secretsCollection content of the secrets collection that needs cataloguing
     * @return lookup map of list name to list GUID
     * @throws InvalidParameterException  invalid parameter
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to perform this operation

     */
    private Map<String, String> getListMap(String            secretsCollectionGUID,
                                           SecretsCollection secretsCollection) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
    {
        final String methodName = "getListMap";

        Map<String, String> listMap = new HashMap<>();

        OpenMetadataStore openMetadataStore = integrationConnector.integrationContext.getOpenMetadataStore();
        CollectionClient  collectionClient  = integrationConnector.integrationContext.getCollectionClient(OpenMetadataType.SECURITY_LIST.typeName);

        NewElementOptions options = new NewElementOptions();

        options.setAnchorGUID(secretsCollectionGUID);
        options.setIsOwnAnchor(false);
        options.setParentGUID(secretsCollectionGUID);
        options.setParentAtEnd1(true);
        options.setParentRelationshipTypeName(OpenMetadataType.SECRETS_COLLECTION_SECURITY_LIST_RELATIONSHIP.typeName);

        Map<String, ClassificationProperties> initialClassifications = new HashMap<>();

        ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();

        zoneMembershipProperties.setZoneMembership(Collections.singletonList(GovernanceZoneName.SECURITY.getZoneName()));
        initialClassifications.put(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName, zoneMembershipProperties);

        /*
         * Iterate through the security groups and roles, extracting useful information.
         * This first pass makes sure the groups are all defined.
         */
        for (String listName : secretsCollection.getNamedLists().keySet())
        {
            if (listName != null)
            {
                String listQualifiedName = OpenMetadataType.SECURITY_LIST.typeName + "::" + secretsCollectionGUID + "::" + listName;
                String listGUID = openMetadataStore.getMetadataElementGUIDByUniqueName(listQualifiedName,
                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name);
                if (listGUID == null)
                {
                    NamedList              namedList = secretsCollection.getNamedLists().get(listName);
                    SecurityListProperties securityListProperties;

                    if (OpenMetadataType.SECURITY_GROUP.typeName.equals(namedList.getListTypeName()))
                    {
                        securityListProperties = new SecurityGroupProperties();
                    }
                    else if (OpenMetadataType.SECURITY_ROLE.typeName.equals(namedList.getListTypeName()))
                    {
                        securityListProperties = new SecurityRoleProperties();
                    }
                    else
                    {
                        securityListProperties = new SecurityListProperties();
                    }

                    securityListProperties.setQualifiedName(listQualifiedName);
                    securityListProperties.setDisplayName(listName);
                    securityListProperties.setDescription(namedList.getDescription());
                    securityListProperties.setDistinguishedName(namedList.getDistinguishedName());

                    listGUID = collectionClient.createCollection(options, initialClassifications, securityListProperties, null);
                }

                listMap.put(listName, listGUID);
            }
        }

        /*
         * Now that all the lists are defined, iterate through the lists again to be sure the relationships are correct.
         */
        for (String listName : secretsCollection.getNamedLists().keySet())
        {
            if (listName != null)
            {
                String listGUID = listMap.get(listName);

                /*
                 * This retrieve should succeed because the element has just been either retrieved or created
                 * in the logic above.  The test for null is a precaution against a timing window where another
                 * process is also editing these elements.
                 */
                OpenMetadataRootElement securityListElement = collectionClient.getCollectionByGUID(listGUID, collectionClient.getGetOptions());

                if (securityListElement != null)
                {
                    NamedList namedList = secretsCollection.getNamedLists().get(listName);

                    if ((namedList.getListMembers() != null) && (!namedList.getListMembers().isEmpty()))
                    {
                        for (String listMemberName : namedList.getListMembers())
                        {
                            if (listMemberName != null)
                            {
                                if (listMap.get(listMemberName) != null)
                                {
                                    /*
                                     * If the membership relationship is already present, this will be a no-op.
                                     */
                                    collectionClient.addToCollection(listGUID,
                                                                     listMap.get(listMemberName),
                                                                     collectionClient.getMakeAnchorOptions(false),
                                                                     null);
                                }
                                else
                                {
                                    auditLog.logMessage(methodName,
                                                        BasicFilesIntegrationConnectorsAuditCode.UNKNOWN_LIST_NAME.getMessageDefinition(connectorName,
                                                                                                                                        listMemberName,
                                                                                                                                        listName,
                                                                                                                                        secretsCollection.getDisplayName(),
                                                                                                                                        secretsCollectionGUID,
                                                                                                                                        listGUID));
                                }
                            }
                        }
                    }

                    if (securityListElement.getCollectionMembers() != null)
                    {
                        /*
                         * Check if any nested lists have been removed since the last time.
                         */
                        for (RelatedMetadataElementSummary collectionMember : securityListElement.getCollectionMembers())
                        {
                            if ((collectionMember != null) && (collectionMember.getRelatedElement().getProperties() instanceof SecurityListProperties securityListProperties))
                            {
                                if (listMap.get(securityListProperties.getDisplayName()) == null)
                                {
                                    collectionClient.removeFromCollection(listGUID,
                                                                          collectionMember.getRelatedElement().getElementHeader().getGUID(),
                                                                          collectionClient.getDeleteOptions(false));
                                }
                            }
                        }
                    }
                }
            }
        }

        return listMap;
    }


    /**
     * Return the list of existing control operations for the given secrets collection element.
     *
     * @param secretsCollectionElement current information about the secrets collection from open metadata
     * @return map of control name to operation name to the list of list names to relationship GUIDs
     */
    Map<String, Map<String, Map<String, String>>> getExistingControlOperations(OpenMetadataRootElement secretsCollectionElement)
    {
        Map<String, Map<String, Map<String, String>>> existingControlOperations = new HashMap<>();

        if ((secretsCollectionElement != null) && (secretsCollectionElement.getSecurityAccessControls() != null))
        {
            for (RelatedMetadataElementSummary control : secretsCollectionElement.getSecurityAccessControls())
            {
                if ((control != null) && (control.getRelatedElement().getProperties() instanceof SecurityAccessControlProperties securityAccessControlProperties))
                {
                    String controlName = securityAccessControlProperties.getIdentifier();

                    Map<String, Map<String, String>> existingOperations = existingControlOperations.get(controlName);

                    if (existingOperations == null)
                    {
                        existingOperations = new HashMap<>();
                    }

                    if (control instanceof RelatedMetadataHierarchySummary hierarchySummary)
                    {
                        if (hierarchySummary.getNestedElements() != null)
                        {
                            for (RelatedMetadataElementSummary nestedList : hierarchySummary.getNestedElements())
                            {
                                if ((nestedList != null) &&
                                        (propertyHelper.isTypeOf(nestedList.getRelationshipHeader(), OpenMetadataType.ASSOCIATED_SECURITY_LIST_RELATIONSHIP.typeName)) &&
                                        (nestedList.getRelationshipProperties() instanceof AssociatedSecurityListProperties associatedSecurityListProperties))
                                {
                                    String operationName = associatedSecurityListProperties.getOperationName();

                                    Map<String, String> existingLists = existingOperations.get(operationName);

                                    if (existingLists == null)
                                    {
                                        existingLists = new HashMap<>();
                                    }

                                    existingLists.put(nestedList.getRelatedElement().getElementHeader().getGUID(),
                                                      nestedList.getRelationshipHeader().getGUID());
                                    existingOperations.put(operationName, existingLists);
                                }
                            }
                        }
                    }

                    existingControlOperations.put(controlName, existingOperations);
                }
            }
        }

        return existingControlOperations;
    }



    /**
     * Ensure all UserIdentity elements are in the security zone.
     * Also, a user is supposed to be able to see its own user identity so the userId is added as a zone.
     *
     * @throws InvalidParameterException  invalid parameter
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException user is not authorized to perform this operation
     */
    private void processUserIdentities() throws InvalidParameterException,
                                                PropertyServerException,
                                                UserNotAuthorizedException
    {
        UserIdentityClient userIdentityClient = integrationConnector.integrationContext.getUserIdentityClient();

        List<OpenMetadataRootElement> userIdentities = userIdentityClient.findUserIdentities(null, userIdentityClient.getSearchOptions());

        if (userIdentities != null)
        {
            for (OpenMetadataRootElement userIdentity : userIdentities)
            {
                if ((userIdentity != null) &&
                        (userIdentity.getProperties() instanceof UserIdentityProperties userIdentityProperties) &&
                        (userIdentityProperties.getUserId() != null))
                {
                    /*
                     * The user identity should be in the security zone.
                     */
                    if (userIdentity.getElementHeader().getZoneMembership() == null)
                    {
                        ZoneMembershipProperties zoneMembershipProperties = new ZoneMembershipProperties();

                        zoneMembershipProperties.setZoneMembership(List.of(GovernanceZoneName.SECURITY.getZoneName(), userIdentityProperties.getUserId()));

                        integrationConnector.integrationContext.getOpenMetadataStore().classifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                                      OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                                      null,
                                                                                                                      classificationBuilder.getNewElementProperties(zoneMembershipProperties));
                    }
                    else if (userIdentity.getElementHeader().getZoneMembership().getClassificationProperties() instanceof ZoneMembershipProperties zoneMembershipProperties)
                    {
                        if (zoneMembershipProperties.getZoneMembership() == null)
                        {
                            zoneMembershipProperties.setZoneMembership(List.of(GovernanceZoneName.SECURITY.getZoneName(), userIdentityProperties.getUserId()));

                            integrationConnector.integrationContext.getOpenMetadataStore().reclassifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                                            OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                                            null,
                                                                                                                            classificationBuilder.getNewElementProperties(zoneMembershipProperties));
                        }
                        else // preserve any existing zone membership
                        {
                            if (! zoneMembershipProperties.getZoneMembership().contains(GovernanceZoneName.SECURITY.getZoneName()))
                            {
                                zoneMembershipProperties.getZoneMembership().add(GovernanceZoneName.SECURITY.getZoneName());
                            }
                            if (! zoneMembershipProperties.getZoneMembership().contains(userIdentityProperties.getUserId()))
                            {
                                zoneMembershipProperties.getZoneMembership().add(userIdentityProperties.getUserId());
                            }

                            integrationConnector.integrationContext.getOpenMetadataStore().reclassifyMetadataElementInStore(userIdentity.getElementHeader().getGUID(),
                                                                                                                            OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                                                                            null,
                                                                                                                            classificationBuilder.getNewElementProperties(zoneMembershipProperties));
                        }
                    }
                }
            }
        }

    }


    /**
     * Refresh the information about the security access controls found in the secrets store file.
     */
    private void refreshSecurityAccessControls()
    {
        final String methodName = "refreshSecurityAccessControls";

        try
        {
            AssetClient fileClient = integrationConnector.integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);

            QueryOptions queryOptions = fileClient.getQueryOptions(0, fileClient.getMaxPagingSize());

            List<OpenMetadataRootElement> matchingFiles = fileClient.getAssetsByDeployedImplementationType(DeployedImplementationType.YAML_SECRETS_COLLECTION_FILE.getDeployedImplementationType(), queryOptions);

            while (matchingFiles != null)
            {
                for (OpenMetadataRootElement file : matchingFiles)
                {
                    if ((file != null) && (file.getElementHeader().getTemplate() == null)) // don't process templates
                    {
                        catalogSecretsCollections(file.getElementHeader().getGUID());
                    }
                }

                queryOptions = fileClient.getQueryOptions(queryOptions.getStartFrom() + fileClient.getMaxPagingSize(), fileClient.getMaxPagingSize());
                matchingFiles = fileClient.getAssetsByDeployedImplementationType(DeployedImplementationType.YAML_SECRETS_COLLECTION_FILE.getDeployedImplementationType(), queryOptions);
            }
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                   error.getClass().getName(),
                                                                                                                   methodName,
                                                                                                                   error.getMessage()));
        }
    }
}
