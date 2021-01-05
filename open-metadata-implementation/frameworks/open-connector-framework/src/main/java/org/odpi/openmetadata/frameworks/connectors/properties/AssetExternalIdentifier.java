/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ExternalIdentifier;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.KeyPattern;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Referenceable;

import java.util.Objects;


/**
 * ExternalIdentifier stores information about an identifier for the asset that is used in an external system.
 * This is used for correlating information about the asset across different systems.
 */
public class AssetExternalIdentifier extends AssetReferenceable
{
    private static final long     serialVersionUID = 1L;

    protected  ExternalIdentifier   externalIdentifierBean;


    /**
     * Bean constructor
     *
     * @param externalIdentifierBean bean containing all of the properties
     */
    public AssetExternalIdentifier(ExternalIdentifier   externalIdentifierBean)
    {
        super(externalIdentifierBean);

        if (externalIdentifierBean == null)
        {
            this.externalIdentifierBean = new ExternalIdentifier();
        }
        else
        {
            this.externalIdentifierBean = externalIdentifierBean;
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset   descriptor for parent asset
     * @param externalIdentifierBean bean containing all of the properties
     */
    public AssetExternalIdentifier(AssetDescriptor      parentAsset,
                                   ExternalIdentifier   externalIdentifierBean)
    {
        super(parentAsset, externalIdentifierBean);

        if (externalIdentifierBean == null)
        {
            this.externalIdentifierBean = new ExternalIdentifier();
        }
        else
        {
            this.externalIdentifierBean = externalIdentifierBean;
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset   descriptor for parent asset
     * @param templateExternalIdentifier   element to copy
     */
    public AssetExternalIdentifier(AssetDescriptor parentAsset, AssetExternalIdentifier templateExternalIdentifier)
    {
        super(parentAsset, templateExternalIdentifier);

        if (templateExternalIdentifier == null)
        {
            this.externalIdentifierBean = new ExternalIdentifier();
        }
        else
        {
            this.externalIdentifierBean = templateExternalIdentifier.getExternalIdentifierBean();
        }
    }


    /**
     * Return the bean with all of the properties - used in the cloning process.
     *
     * @return ExternalIdentifier bean
     */
    protected  ExternalIdentifier  getExternalIdentifierBean()
    {
        return externalIdentifierBean;
    }


    /**
     * Return the external identifier for this asset.
     *
     * @return String identifier
     */
    public String getIdentifier() { return externalIdentifierBean.getIdentifier(); }


    /**
     * Return the description of the external identifier.
     *
     * @return String description
     */
    public String getDescription() { return externalIdentifierBean.getDescription(); }


    /**
     * Return details of how, where and when this external identifier is used.
     *
     * @return String usage
     */
    public String getUsage() { return externalIdentifierBean.getUsage(); }


    /**
     * Return details of the source system where this external identifier comes from.
     *
     * @return String server
     */
    public String getSource() { return externalIdentifierBean.getSource(); }


    /**
     * Return the key pattern that is used with this external identifier.
     *
     * @return KeyPattern enum
     */
    public KeyPattern getKeyPattern() { return externalIdentifierBean.getKeyPattern(); }


    /**
     * Return the scope of this external identifier.  This depends on the key pattern.  It may be a server definition,
     * a reference data set or glossary term.
     *
     * @return Referenceable scope
     */
    public AssetReferenceable getScope()
    {
        Referenceable   scope = externalIdentifierBean.getScope();

        if (scope == null)
        {
            return null;
        }
        else
        {
            return new AssetReferenceable(super.getParentAsset(), scope);
        }
    }


    /**
     * Return the text description of the scope for this external identifier.
     *
     * @return String scope description
     */
    public String getScopeDescription() { return externalIdentifierBean.getScopeDescription(); }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return externalIdentifierBean.toString();
    }



    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetExternalIdentifier that = (AssetExternalIdentifier) objectToCompare;
        return Objects.equals(externalIdentifierBean, that.externalIdentifierBean);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalIdentifierBean);
    }
}