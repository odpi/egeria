/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.DerivedSchemaElement;

import java.util.Objects;


/**
 * Derived schema elements are used in views to define elements that are calculated using data from other sources.
 * It contains a list of queries and a formula to combine the resulting values.
 */
public class AssetDerivedSchemaType extends AssetPrimitiveSchemaType
{
    protected DerivedSchemaElement             derivedSchemaElementBean;
    protected AssetSchemaImplementationQueries queries;


    /**
     * Bean constructor
     *
     * @param derivedSchemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     * @param queries queries used to create the element
     */
    public AssetDerivedSchemaType(DerivedSchemaElement             derivedSchemaElementBean,
                                  AssetMeanings                    assetMeanings,
                                  AssetSchemaImplementationQueries queries)
    {
        super(derivedSchemaElementBean, assetMeanings);

        if (derivedSchemaElementBean == null)
        {
            this.derivedSchemaElementBean = new DerivedSchemaElement();
        }
        else
        {
            this.derivedSchemaElementBean = derivedSchemaElementBean;
        }

        if (queries == null)
        {
            this.queries = null;
        }
        else
        {
            this.queries = queries.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset descriptor for parent asset
     * @param derivedSchemaElementBean bean containing the schema element properties
     * @param assetMeanings iterator for the asset assetMeanings
     * @param queries queries used to create the element
     */
    public AssetDerivedSchemaType(AssetDescriptor                  parentAsset,
                                  DerivedSchemaElement             derivedSchemaElementBean,
                                  AssetMeanings                    assetMeanings,
                                  AssetSchemaImplementationQueries queries)
    {
        super(parentAsset, derivedSchemaElementBean, assetMeanings);

        if (derivedSchemaElementBean == null)
        {
            this.derivedSchemaElementBean = new DerivedSchemaElement();
        }
        else
        {
            this.derivedSchemaElementBean = derivedSchemaElementBean;
        }

        if (queries == null)
        {
            this.queries = null;
        }
        else
        {
            this.queries = queries.cloneIterator(super.getParentAsset());
        }
    }




    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param templateSchemaElement template object to copy.
     */
    public AssetDerivedSchemaType(AssetDescriptor  parentAsset, AssetDerivedSchemaType templateSchemaElement)
    {
        super(parentAsset, templateSchemaElement);

        if (templateSchemaElement == null)
        {
            this.derivedSchemaElementBean = new DerivedSchemaElement();
            this.queries = null;
        }
        else
        {
            this.derivedSchemaElementBean = templateSchemaElement.getDerivedSchemaElementBean();

            AssetSchemaImplementationQueries queries = templateSchemaElement.getQueries();

            if (queries == null)
            {
                this.queries = null;
            }
            else
            {
                this.queries = queries.cloneIterator(super.getParentAsset());
            }
        }
    }


    /**
     * Return the bean with all of the properties in it.
     *
     * @return derived schema element bean
     */
    protected DerivedSchemaElement  getDerivedSchemaElementBean()
    {
        return derivedSchemaElementBean;
    }


    /**
     * Return the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
     *
     * @return String formula
     */
    public String getFormula() { return derivedSchemaElementBean.getFormula(); }


    /**
     * Return the list of queries that are used to create the derived schema element.
     *
     * @return SchemaImplementationQueries list of queries
     */
    public AssetSchemaImplementationQueries getQueries()
    {
        if (queries == null)
        {
            return null;
        }
        else
        {
            return queries.cloneIterator(super.getParentAsset());
        }
    }


    /**
     * Returns a clone of this object as the abstract SchemaElement class.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return PrimitiveSchemaElement object
     */
    @Override
    public AssetSchemaType cloneAssetSchemaElement(AssetDescriptor parentAsset)
    {
        return new AssetDerivedSchemaType(parentAsset, this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return derivedSchemaElementBean.toString();
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
        if (!(objectToCompare instanceof AssetDerivedSchemaType))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetDerivedSchemaType that = (AssetDerivedSchemaType) objectToCompare;
        return Objects.equals(getDerivedSchemaElementBean(), that.getDerivedSchemaElementBean()) &&
                Objects.equals(getQueries(), that.getQueries());
    }
}