/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.client;


import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetDescriptor;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetExternalIdentifier;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetExternalIdentifiers;
import org.odpi.openmetadata.frameworks.connectors.properties.AssetPropertyBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import java.util.List;


/**
 * ConnectedAssetExternalIdentifiers provides the open metadata concrete implementation of the
 * Open Connector Framework (OCF) AssetExternalIdentifiers abstract class.
 * Its role is to query the property servers (metadata repository cohort) to
 */
public class ConnectedAssetExternalIdentifiers extends AssetExternalIdentifiers
{
    private String              userId;
    private String              omasServerURL;
    private List<Relationship>  initialElements;


    /**
     * Typical Constructor creates an iterator with the supplied list of elements.
     *
     * @param parentAsset descriptor of parent asset
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     */
    public ConnectedAssetExternalIdentifiers(String              userId,
                                             String              omasServerURL,
                                             ConnectedAsset      parentAsset,
                                             List<Relationship>  initialElements,
                                             int                 totalElementCount,
                                             int                 maxCacheSize)
    {
        super(parentAsset, totalElementCount, maxCacheSize);

        this.userId          = userId;
        this.omasServerURL   = omasServerURL;
        this.initialElements = initialElements;
    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param parentAsset descriptor of parent asset
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    public ConnectedAssetExternalIdentifiers(AssetDescriptor   parentAsset, AssetExternalIdentifiers template)
    {
        super(parentAsset, template);
    }

    /**
     * Clones this iterator.
     *
     * @param parentAsset descriptor of parent asset
     * @return new cloned object.
     */
    protected  AssetExternalIdentifiers cloneIterator(AssetDescriptor parentAsset)
    {
        return new ConnectedAssetExternalIdentifiers(super.parentAsset, this);
    }



    /**
     * Method implemented by a subclass that ensures the cloning process is a deep clone.
     *
     * @param parentAsset descriptor of parent asset
     * @param template object to clone
     * @return new cloned object.
     */
    protected  AssetPropertyBase cloneElement(AssetDescriptor  parentAsset, AssetPropertyBase template)
    {
        return new AssetExternalIdentifier(parentAsset, (AssetExternalIdentifier)template);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    protected  List<AssetPropertyBase> getCachedList(int  cacheStartPointer,
                                                     int  maximumSize) throws PropertyServerException
    {
        return null;
    }

}
