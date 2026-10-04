/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.governanceactions.stewardship;

import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.governanceactions.ffdc.GovernanceActionConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.AuditLogMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.opengovernance.GeneralGovernanceActionService;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.CompletionStatus;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElementList;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

/**
 * DeleteAssetGovernanceActionConnector deleted an asset and passes its GUID as an action target for follow on work.
 */
public class DeleteAssetGovernanceActionConnector extends GeneralGovernanceActionService
{
    /**
     * Default constructor
     */
    public DeleteAssetGovernanceActionConnector()
    {
    }


    /**
     * Indicates that the governance action service is completely configured and can begin processing.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() at the start of your overriding version.
     *
     * @throws ConnectorCheckedException a problem within the governance action service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        final String methodName = "start";

        super.start();

        try
        {
            List<String>              outputGuards        = new ArrayList<>();
            CompletionStatus          completionStatus;
            AuditLogMessageDefinition messageDefinition;
            String                    templateGUID;

            templateGUID = getProperty(ManageAssetRequestParameter.TEMPLATE_GUID.getName(), null);

            if (templateGUID == null)
            {
                messageDefinition = GovernanceActionConnectorsAuditCode.NO_TEMPLATE_GUID.getMessageDefinition(governanceServiceName);
                outputGuards.add(ManageAssetGuard.MISSING_TEMPLATE.getName());
                completionStatus = ManageAssetGuard.MISSING_TEMPLATE.getCompletionStatus();
            }
            else
            {
                String assetGUID = governanceContext.getOpenMetadataStore().getMetadataElementFromTemplate(null,
                                                                                                            null,
                                                                                                            true,
                                                                                                            null,
                                                                                                            null,
                                                                                                            null,
                                                                                                            templateGUID,
                                                                                                            null,
                                                                                                            null,
                                                                                                            governanceContext.getRequestParameters(),
                                                                                                            null,
                                                                                                            null,
                                                                                                            null,
                                                                                                            true);

                OpenMetadataElement assetElement = governanceContext.getOpenMetadataStore().getMetadataElementByGUID(assetGUID);

                messageDefinition = GovernanceActionConnectorsAuditCode.NEW_ASSET_DELETED.getMessageDefinition(governanceServiceName,
                                                                                                               assetElement.getType().getTypeName(),
                                                                                                               propertyHelper.getStringProperty(governanceServiceName,
                                                                                                                                                OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                                                assetElement.getElementProperties(),
                                                                                                                                                methodName),
                                                                                                               assetGUID);

                /*
                 * The delete is made on behalf of the metadata collection that an integration connector keeps
                 * for the asset.  A catalog target processor records what it synchronizes from the asset under
                 * that collection, as an external source, and anchors those elements to the asset; a cascaded
                 * delete that does not name the source is refused permission to remove them - so the asset
                 * could not be deleted at all once it had been synchronized.  An asset with no metadata
                 * collection is deleted on behalf of its own source, which for most assets is the local cohort.
                 */
                DeleteOptions deleteOptions = new DeleteOptions(governanceContext.getOpenMetadataStore().getDeleteOptions(true));

                OpenMetadataElement metadataCollection = this.getMetadataCollection(assetElement.getElementGUID());

                if (metadataCollection != null)
                {
                    deleteOptions.setExternalSourceGUID(propertyHelper.getStringProperty(governanceServiceName,
                                                                                         OpenMetadataProperty.MANAGED_METADATA_COLLECTION_ID.name,
                                                                                         metadataCollection.getElementProperties(),
                                                                                         methodName));
                    deleteOptions.setExternalSourceName(propertyHelper.getStringProperty(governanceServiceName,
                                                                                         OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                         metadataCollection.getElementProperties(),
                                                                                         methodName));
                }
                else if ((assetElement.getOrigin() != null) &&
                         (assetElement.getOrigin().getOriginCategory() == ElementOriginCategory.EXTERNAL_SOURCE))
                {
                    deleteOptions.setExternalSourceGUID(assetElement.getOrigin().getHomeMetadataCollectionId());
                    deleteOptions.setExternalSourceName(assetElement.getOrigin().getHomeMetadataCollectionName());
                }

                governanceContext.getOpenMetadataStore().deleteMetadataElementInStore(assetElement.getElementGUID(), deleteOptions);

                completionStatus = ManageAssetGuard.DELETE_COMPLETE.getCompletionStatus();
                outputGuards.add(ManageAssetGuard.DELETE_COMPLETE.getName());
            }

            logRecord(methodName, messageDefinition);

            governanceContext.recordCompletionStatus(completionStatus, outputGuards, null, null, messageDefinition);
        }
        catch (Exception error)
        {
            throw new ConnectorCheckedException(GovernanceActionConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(governanceServiceName,
                                                                                                                              error.getClass().getName(),
                                                                                                                              error.getMessage()),
                                                error.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Return the metadata collection that an integration connector keeps for an asset, if there is one.  It is
     * reached through the asset's inventory catalog capability, which uses the collection as an asset - the
     * arrangement CatalogTargetProcessorBase.setUpMetadataSource creates.
     *
     * @param assetGUID unique identifier of the asset
     * @return metadata collection element, or null
     * @throws InvalidParameterException invalid parameter
     * @throws UserNotAuthorizedException the service is no longer active
     * @throws PropertyServerException repository error
     */
    private OpenMetadataElement getMetadataCollection(String assetGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        RelatedMetadataElementList capabilities = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(assetGUID,
                                                                                                                      0,
                                                                                                                      OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                                                                                      0,
                                                                                                                      0);

        if ((capabilities != null) && (capabilities.getElementList() != null))
        {
            for (RelatedMetadataElement capability : capabilities.getElementList())
            {
                if ((capability != null) && (capability.getElement() != null) &&
                    (propertyHelper.isTypeOf(capability.getElement(), OpenMetadataType.INVENTORY_CATALOG.typeName)))
                {
                    RelatedMetadataElementList consumedAssets = governanceContext.getOpenMetadataStore().getRelatedMetadataElements(capability.getElement().getElementGUID(),
                                                                                                                                    0,
                                                                                                                                    OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                                                                                                                                    0,
                                                                                                                                    0);

                    if ((consumedAssets != null) && (consumedAssets.getElementList() != null))
                    {
                        for (RelatedMetadataElement consumedAsset : consumedAssets.getElementList())
                        {
                            if ((consumedAsset != null) && (consumedAsset.getElement() != null) &&
                                (propertyHelper.isTypeOf(consumedAsset.getElement(), OpenMetadataType.METADATA_COLLECTION.typeName)))
                            {
                                return consumedAsset.getElement();
                            }
                        }
                    }
                }
            }
        }

        return null;
    }
}
