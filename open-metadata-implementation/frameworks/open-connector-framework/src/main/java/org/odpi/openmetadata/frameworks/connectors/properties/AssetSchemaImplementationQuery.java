/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaImplementationQuery;

import java.util.Objects;

/**
 * SchemaImplementationQuery defines a query on a schema attribute that returns all or part of the value for a
 * derived field.
 */
public class AssetSchemaImplementationQuery extends AssetPropertyBase
{
    private static final long     serialVersionUID = 1L;

    protected SchemaImplementationQuery  schemaImplementationQueryBean;
    protected AssetSchemaAttribute       queryTargetElement;


    /**
     * Bean constructor
     *
     * @param schemaImplementationQueryBean bean containing all of the properties
     * @param queryTargetElement target data source for query
     */
    public AssetSchemaImplementationQuery(SchemaImplementationQuery  schemaImplementationQueryBean,
                                          AssetSchemaAttribute       queryTargetElement)
    {
        super(null);

        if (schemaImplementationQueryBean == null)
        {
            this.schemaImplementationQueryBean = new SchemaImplementationQuery();
        }
        else
        {
            this.schemaImplementationQueryBean = schemaImplementationQueryBean;
        }

        if (queryTargetElement == null)
        {
            this.queryTargetElement = null;
        }
        else
        {
            this.queryTargetElement = new AssetSchemaAttribute(super.getParentAsset(), queryTargetElement);
        }
    }


    /**
     * Bean constructor with parent asset
     *
     * @param parentAsset description of the asset that this schema query is attached to.
     * @param schemaImplementationQueryBean bean containing all of the properties
     * @param queryTargetElement target data source for query
     */
    public AssetSchemaImplementationQuery(AssetDescriptor            parentAsset,
                                          SchemaImplementationQuery  schemaImplementationQueryBean,
                                          AssetSchemaAttribute       queryTargetElement)
    {
        super(parentAsset);

        if (schemaImplementationQueryBean == null)
        {
            this.schemaImplementationQueryBean = new SchemaImplementationQuery();
        }
        else
        {
            this.schemaImplementationQueryBean = schemaImplementationQueryBean;
        }

        if (queryTargetElement == null)
        {
            this.queryTargetElement = null;
        }
        else
        {
            this.queryTargetElement = new AssetSchemaAttribute(super.getParentAsset(), queryTargetElement);
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param parentAsset description of the asset that this schema query is attached to.
     * @param template template schema query to copy.
     */
    public AssetSchemaImplementationQuery(AssetDescriptor                parentAsset,
                                          AssetSchemaImplementationQuery template)
    {
        super(parentAsset, template);

        if (template == null)
        {
            this.schemaImplementationQueryBean = new SchemaImplementationQuery();
            this.queryTargetElement = null;
        }
        else
        {
            this.schemaImplementationQueryBean = template.getSchemaImplementationQueryBean();

            AssetSchemaAttribute queryTargetElement = template.getQueryTargetElement();

            if (queryTargetElement == null)
            {
                this.queryTargetElement = null;
            }
            else
            {
                this.queryTargetElement = new AssetSchemaAttribute(super.getParentAsset(), queryTargetElement);
            }
        }
    }


    /**
     * Return the bean with all of the properties - used in cloning.
     *
     * @return schema implementation query bean
     */
    protected SchemaImplementationQuery  getSchemaImplementationQueryBean()
    {
        return schemaImplementationQueryBean;
    }


    /**
     * Return the query id - this is used to identify where the results of this query should be plugged into
     * the other queries or the formula for the parent derived schema element.
     *
     * @return int query identifier
     */
    public int getQueryId() { return schemaImplementationQueryBean.getQueryId(); }


    /**
     * Return the query string for this element.  The query string may have placeholders for values returned
     * by queries that have a lower queryId than this element.
     *
     * @return String query
     */
    public String getQuery() { return schemaImplementationQueryBean.getQuery(); }


    /**
     * Return the name of the query language used in the query.
     *
     * @return queryType String
     */
    public String getQueryType() { return schemaImplementationQueryBean.getQueryType(); }


    /**
     * Return the SchemaAttribute that describes the type of the data source that will be queried to get the
     * derived value.
     *
     * @return SchemaAttribute
     */
    public AssetSchemaAttribute getQueryTargetElement()
    {
        if (queryTargetElement == null)
        {
            return null;
        }
        else
        {
            return new AssetSchemaAttribute(super.getParentAsset(), queryTargetElement);
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return schemaImplementationQueryBean.toString();
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
        if (!(objectToCompare instanceof AssetSchemaImplementationQuery))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        AssetSchemaImplementationQuery that = (AssetSchemaImplementationQuery) objectToCompare;
        return Objects.equals(getSchemaImplementationQueryBean(), that.getSchemaImplementationQueryBean()) &&
                Objects.equals(getQueryTargetElement(), that.getQueryTargetElement());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return schemaImplementationQueryBean.hashCode();
    }
}