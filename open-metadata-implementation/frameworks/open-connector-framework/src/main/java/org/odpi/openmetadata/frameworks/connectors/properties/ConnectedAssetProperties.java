/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PropertyBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ConnectedAssetProperties is associated with a Connector.  Connectors provide access to
 * assets.   ConnectedAssetProperties returns properties (metadata) about the connector's asset.
 *
 * It is a generic interface for all types of open metadata assets.  However, it assumes the asset's metadata model
 * inherits from <b>Asset</b> (see model 0010 in Area 0).
 *
 * The ConnectedAssetProperties returns metadata about the asset at three levels of detail:
 * <ul>
 *     <li><b>assetSummary</b> used for displaying details of the asset in summary lists or hover text</li>
 *     <li><b>assetDetail</b> used to display all the information known about the asset with summaries
 *     of the relationships to other metadata entities</li>
 *     <li><b>assetUniverse</b> used to define the broader context for the asset</li>
 * </ul>
 *
 * ConnectedAssetProperties is a base class for the connector's metadata API that returns null,
 * for the asset's properties.  Metadata repository implementations extend this class to add their
 * implementation of the refresh() method that calls to the metadata repository to populate the metadata properties.
 */
public abstract class ConnectedAssetProperties extends PropertyBase
{
    private static final long     serialVersionUID = 1L;

    /*
     * AssetUniverse extends AssetDetails which extends AssetSummary.  The interaction with the metadata repository
     * pulls the asset universe in one single network interaction and the caller can then explore the metadata
     * property by property without incurring many network interactions (unless there are too many instances
     * of a particular type of property and one of the iterators is forced to use paging).
     *
     * If null is returned, the connector is not linked to a metadata repository.
     */
    protected AssetUniverse assetProperties = null;

    /**
     * Typical constructor.
     */
    public ConnectedAssetProperties()
    {
        /*
         * Nothing to do except initialize superclass.
         */
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param templateProperties template to copy.
     */
    public ConnectedAssetProperties(ConnectedAssetProperties templateProperties)
    {
        super(templateProperties);

        if (templateProperties != null)
        {
            AssetUniverse templateAssetUniverse = templateProperties.getAssetUniverse();
            if (templateAssetUniverse != null)
            {
                assetProperties = new AssetUniverse(templateAssetUniverse);
            }
        }
    }


    /**
     * Returns the summary information organized in the assetSummary structure.
     *
     * @return AssetSummary summary object
     */
    public AssetSummary getAssetSummary() { return assetProperties; }



    /**
     * Returns detailed information about the asset organized in the assetDetail structure.
     *
     * @return AssetDetail detail object
     */
    public AssetDetail getAssetDetail() { return assetProperties; }


    /**
     * Returns all the detail of the asset and information connected to it in organized in the assetUniverse
     * structure.
     *
     * @return AssetUniverse universe object
     */
    public AssetUniverse getAssetUniverse() { return assetProperties; }


    /**
     * Request the values in the ConnectedAssetProperties are refreshed with the current values from the
     * metadata repository.
     *
     * @throws PropertyServerException there is a problem connecting to the server to retrieve metadata.
     * @throws UserNotAuthorizedException the userId associated with the connector is not authorized to
     *                                    access the asset properties.
     */
    public abstract void refresh() throws PropertyServerException, UserNotAuthorizedException;


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ConnectedAssetProperties{" +
                "assetProperties=" + assetProperties +
                '}';
    }
}