/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.discoveryservices;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetUniverse;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAnnotationStore;
import org.odpi.openmetadata.frameworks.discovery.DiscoveryAssetCatalogStore;
import org.odpi.openmetadata.frameworks.discovery.properties.SuspectDuplicateAnnotation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DuplicateSuspectDiscoveryService is a discovery service that looks for assets in the asset catalog that seem to be
 * duplicates of the asset supplied in the context.
 */
public class DuplicateSuspectDiscoveryService extends AuditableDiscoveryService
{
    private static final String   QUALIFIED_NAME_PROPERTY = "qualifiedName";
    private static final String   DISPLAY_NAME_PROPERTY = "displayName";
    private static final String   NETWORK_ADDRESS_PROPERTY = "Connection::Endpoint::address";

    private final Map<String, List<String>> suspectDuplicateReport = new HashMap<>();

    /**
     * Indicates that the discovery service is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the discovery service.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        final String  methodName = "start";

        super.start();

        try
        {
            String        assetGUID = discoveryContext.getAssetGUID();
            AssetUniverse asset     = discoveryContext.getAssetStore().getAssetProperties();

            if (asset == null)
            {
                super.logNoAsset(assetGUID, methodName);
                return;
            }

            /*
             * Extract the information from the requested asset that will be used to identify duplicates.
             */
            List<String> endpointNetworkAddresses = super.getNetworkAddresses(asset);
            String       qualifiedName            = asset.getQualifiedName();
            String       displayName              = asset.getDisplayName();

            DiscoveryAssetCatalogStore assetCatalogStore = discoveryContext.getAssetCatalogStore();

            /*
             * Just pull in up to the maximum page size - if locating more duplicates that this upper limit then there are bigger problems.
             * Duplicate assets are returned based on qualified name, display name and endpoint address.
             */
            List<String> retrievedAssetGUIDs = assetCatalogStore.getAssetsByQualifiedName(qualifiedName,0, assetCatalogStore.getMaxPageSize());
            this.captureDuplicateSuspects(retrievedAssetGUIDs, QUALIFIED_NAME_PROPERTY);

            if (displayName != null)
            {
                retrievedAssetGUIDs = assetCatalogStore.getAssetsByName(displayName,0, assetCatalogStore.getMaxPageSize());
                this.captureDuplicateSuspects(retrievedAssetGUIDs, DISPLAY_NAME_PROPERTY);
            }

            if (endpointNetworkAddresses != null)
            {
                for (String networkAddress : endpointNetworkAddresses)
                {
                    if (networkAddress != null)
                    {
                        retrievedAssetGUIDs = assetCatalogStore.getAssetsByName(displayName,0, assetCatalogStore.getMaxPageSize());
                        this.captureDuplicateSuspects(retrievedAssetGUIDs, NETWORK_ADDRESS_PROPERTY);
                    }
                }
            }

            recordAnnotations();
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            super.handleUnexpectedException(methodName, error);
        }
    }


    /**
     * Capture details of the assets that match on a specific property.
     *
     * @param duplicateSuspectGUIDs list of matching unique identifiers
     * @param propertyName property name that they match on
     */
    private void captureDuplicateSuspects(List<String>    duplicateSuspectGUIDs,
                                          String          propertyName)
    {
        if (duplicateSuspectGUIDs != null)
        {
            for (String duplicateSuspectGUID : duplicateSuspectGUIDs)
            {
                if (duplicateSuspectGUID != null)
                {
                    List<String>  duplicateSuspectRecord = suspectDuplicateReport.get(duplicateSuspectGUID);

                    if (duplicateSuspectRecord == null)
                    {
                        duplicateSuspectRecord = new ArrayList<>();
                    }

                    duplicateSuspectRecord.add(propertyName);
                    suspectDuplicateReport.put(duplicateSuspectGUID, duplicateSuspectRecord);
                }
            }
        }
    }


    /**
     * Take the suspect duplicates report and turn it into annotations.
     *
     * @throws InvalidParameterException problem with the guid
     * @throws UserNotAuthorizedException problem with user id
     * @throws PropertyServerException problem connecting to metadata server
     */
    private void recordAnnotations() throws InvalidParameterException,
                                            UserNotAuthorizedException,
                                            PropertyServerException
    {
        final String  methodName = "recordAnnotations";

        DiscoveryAnnotationStore annotationStore = discoveryContext.getAnnotationStore();

        if (suspectDuplicateReport.isEmpty())
        {
            /*
             * This asset is unique - create an annotation to show there are no duplicate suspects.
             */
            SuspectDuplicateAnnotation  annotation = new SuspectDuplicateAnnotation();

            annotation.setAnnotationType("Unique Asset");
            annotation.setSummary("No matching assets found based on qualified name, display name and endpoint addresses.");

            annotationStore.addAnnotationToDiscoveryReport(annotation);
        }
        else
        {
            /*
             * Create a duplicate suspect annotation for each asset found in the suspect duplicate report.
             */
            for (String    suspectGUID : suspectDuplicateReport.keySet())
            {
                if (suspectGUID != null)
                {
                    SuspectDuplicateAnnotation  annotation = new SuspectDuplicateAnnotation();

                    annotation.setAnnotationType("Duplicate Suspect Asset");
                    annotation.setSummary("This asset appears to refer to the same physical asset.");
                    annotation.setMatchingPropertyNames(suspectDuplicateReport.get(suspectGUID));

                    annotationStore.addAnnotationToDiscoveryReport(annotation);
                }
            }
        }
    }
}
