/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;

/**
 * This is the base class for a connected asset.  It is passed to all of the embedded property objects so the name
 * and type can be used for error messages and other diagnostics.  It also carries the URL of the asset in the
 * metadata repository where this is known to enable properties to be retrieved on request.
 */
public abstract class AssetDescriptor extends PropertyBase
{
    /*
     * Derived name and type for use by nested property object for messages/debug.  If these default values
     * are seen it is a sign that the asset properties are not being populated from the metadata repository.
     */
    private String assetName = "<Unknown>";
    private String assetTypeName = "<Unknown>";

    /*
     * URL where the metadata about the asset is located.  It remains null if no repository is known.
     */
    private String url = null;

    /*
     * Unique id for the asset.
     */
    private String guid = null;


    /**
     * Typical constructor - the asset descriptor is effectively empty - and the protected
     * set methods need to be called to add useful content to it.
     */
    public AssetDescriptor()
    {
        /*
         * Nothing to do except call superclass
         */
        super();
    }


    /**
     * Explicit constructor - the asset descriptor is explicitly given the url for the asset.
     *
     * @param guid unique id for the asset
     * @param url URL for the asset in the metadata repository
     */
    public AssetDescriptor(String guid, String  url)
    {
        super();

        this.guid = guid;
        this.url = url;
    }


    /**
     * Copy/clone Constructor - used to copy the asset descriptor for a new consumer.
     *
     * @param templateAssetDescriptor template asset descriptor to copy.
     */
    public AssetDescriptor(AssetDescriptor templateAssetDescriptor)
    {
        super();

        this.guid = templateAssetDescriptor.getGUID();
        this.assetName = templateAssetDescriptor.getAssetName();
        this.assetTypeName = templateAssetDescriptor.getAssetTypeName();
        this.url = templateAssetDescriptor.getURL();
    }


    /**
     * Method to enable a subclass to set up the asset name.
     *
     * @param assetName String name of asset for messages etc
     */
    protected void setAssetName(String     assetName)
    {
        this.assetName = assetName;
    }


    /**
     * Method to enable a subclass to set up the asset type name.
     *
     * @param assetTypeName String new type name
     */
    protected void setAssetTypeName(String    assetTypeName)
    {
        this.assetTypeName = assetTypeName;
    }


    /**
     * Return the unique id for this element.
     *
     * @return guid unique id
     */
    public String getGUID() {
        return guid;
    }


    /**
     * Return the name of the asset - for use in messages and other diagnostics.
     *
     * @return String asset name
     */
    public String getAssetName()
    {
        return assetName;
    }


    /**
     * Return the name of the asset's type - for use in messages and other diagnostics.
     *
     * @return String asset type name
     */
    public String getAssetTypeName()
    {
        return assetTypeName;
    }


    /**
     * Return the URL of the asset in the metadata repository if supported.
     *
     * @return String URL
     */
    public String getURL() { return url; }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetDescriptor{" +
                "assetName='" + assetName + '\'' +
                ", assetTypeName='" + assetTypeName + '\'' +
                ", url='" + url + '\'' +
                ", guid='" + guid + '\'' +
                '}';
    }
}