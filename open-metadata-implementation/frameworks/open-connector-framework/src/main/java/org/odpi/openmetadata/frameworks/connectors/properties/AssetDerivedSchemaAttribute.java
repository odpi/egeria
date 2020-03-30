/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.DerivedSchemaAttribute;

import java.util.Objects;


/**
 * Derived schema elements are used in views to define elements that are calculated using data from other sources.
 * It contains a list of queries and a formula to combine the resulting values.
 */
public class AssetDerivedSchemaAttribute extends AssetSchemaAttribute
{
    private static final long     serialVersionUID = 1L;

    protected DerivedSchemaAttribute           schemaAttributeBean;
    protected AssetSchemaImplementationQueries queries;


    /**
     * Bean constructor
     *
     * @param schemaAttributeBean bean containing all of the properties
     * @param queries queries used to create the element
     */
    public AssetDerivedSchemaAttribute(DerivedSchemaAttribute           schemaAttributeBean,
                                       AssetSchemaImplementationQueries queries)
    {
        super(schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new DerivedSchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }

        this.queries = queries;
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this schema attribute is attached to.
     * @param schemaAttributeBean bean containing all of the properties
     * @param queries queries used to create the element
     */
    public AssetDerivedSchemaAttribute(AssetDescriptor                  parentAsset,
                                       DerivedSchemaAttribute           schemaAttributeBean,
                                       AssetSchemaImplementationQueries queries)
    {
        super(parentAsset, schemaAttributeBean);

        if (schemaAttributeBean == null)
        {
            this.schemaAttributeBean = new DerivedSchemaAttribute();
        }
        else
        {
            this.schemaAttributeBean = schemaAttributeBean;
        }

        this.queries = queries;
    }


    /**
     * Copy/clone Constructor the parentAsset is passed separately to the template because it is also
     * likely to be being cloned in the same operation and we want the definitions clone to point to the
     * asset clone and not the original asset.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @param template template object to copy.
     */
    public AssetDerivedSchemaAttribute(AssetDescriptor parentAsset, AssetDerivedSchemaAttribute template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.schemaAttributeBean = new DerivedSchemaAttribute();
            this.queries = null;
        }
        else
        {
            this.schemaAttributeBean = template.getDerivedSchemaAttributeBean();

            AssetSchemaImplementationQueries queries = template.getQueries();

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
    protected DerivedSchemaAttribute getDerivedSchemaAttributeBean()
    {
        return schemaAttributeBean;
    }


    /**
     * Return the formula used to combine the values of the queries.  Each query is numbers 0, 1, ... and the
     * formula has placeholders in it to show how the query results are combined.
     *
     * @return String formula
     */
    public String getFormula() { return schemaAttributeBean.getFormula(); }


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
     * Returns a clone of this object.
     *
     * @param parentAsset description of the asset that this schema element is attached to.
     * @return clone of this object
     */
    @Override
    public AssetSchemaAttribute cloneAssetSchemaAttribute(AssetDescriptor parentAsset)
    {
        return new AssetDerivedSchemaAttribute(parentAsset, this);
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetDerivedSchemaAttribute{" +
                "formula='" + getFormula() + '\'' +
                ", attributeName='" + getAttributeName() + '\'' +
                ", elementPosition=" + getElementPosition() +
                ", category=" + getCategory() +
                ", cardinality='" + getCardinality() + '\'' +
                ", minCardinality=" + getMinCardinality() +
                ", maxCardinality=" + getMaxCardinality() +
                ", allowsDuplicateValues=" + allowsDuplicateValues() +
                ", hasOrderedValues=" + hasOrderedValues() +
                ", sortOrder=" + getSortOrder() +
                ", defaultValueOverride='" + getDefaultValueOverride() + '\'' +
                ", attributeType=" + getAttributeType() +
                ", externalSchemaLink=" + getExternalSchemaLink() +
                ", attributeRelationships=" + getAttributeRelationships() +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", meanings=" + getMeanings() +
                ", additionalProperties=" + getAdditionalProperties() +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                '}';
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
        if (!(objectToCompare instanceof AssetDerivedSchemaAttribute))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetDerivedSchemaAttribute that = (AssetDerivedSchemaAttribute) objectToCompare;
        return Objects.equals(getDerivedSchemaAttributeBean(), that.getDerivedSchemaAttributeBean()) &&
                Objects.equals(getQueries(), that.getQueries());
    }
}