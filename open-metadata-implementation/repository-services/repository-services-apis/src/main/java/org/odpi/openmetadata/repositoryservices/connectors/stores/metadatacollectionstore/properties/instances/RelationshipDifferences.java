/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

/**
 * Capture the differences between relationship instances.
 */
public class RelationshipDifferences extends Differences {

    private InstancePropertiesDifferences instancePropertiesDifferences = null;
    private EntityProxyDifferences entityProxyOneDifferences;
    private EntityProxyDifferences entityProxyTwoDifferences;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDifferences()
    {
        return super.hasDifferences() || hasInstancePropertiesDifferences() || hasEntityProxyDifferences();
    }

    /**
     * Returns true if there are any differences in the entity proxies for the relationships.
     *
     * @return boolean
     */
    public boolean hasEntityProxyDifferences()
    {
        return hasEntityProxyOneDifferences() || hasEntityProxyTwoDifferences();
    }

    /**
     * Returns true if there are any differences between the first entity proxy on the relationships.
     *
     * @return boolean
     */
    public boolean hasEntityProxyOneDifferences()
    {
        return entityProxyOneDifferences != null && entityProxyOneDifferences.hasDifferences();
    }

    /**
     * Returns true if there are any differences between the second entity proxy on the relationships.
     *
     * @return boolean
     */
    public boolean hasEntityProxyTwoDifferences()
    {
        return entityProxyTwoDifferences != null && entityProxyTwoDifferences.hasDifferences();
    }

    /**
     * Set the differences between the first entity proxy of the two relationships.
     *
     * @param entityProxyOneDifferences the differences calculated for entity proxy one
     */
    public void setEntityProxyOneDifferences(EntityProxyDifferences entityProxyOneDifferences)
    {
        this.entityProxyOneDifferences = entityProxyOneDifferences;
    }

    /**
     * Returns the differences between the first entity proxy of the two relationships.
     *
     * @return EntityProxyDifferences
     */
    public EntityProxyDifferences getEntityProxyOneDifferences()
    {
        return entityProxyOneDifferences;
    }

    /**
     * Set the differences between the second entity proxy of the two relationships.
     *
     * @param entityProxyTwoDifferences the differences calculated for entity proxy two
     */
    public void setEntityProxyTwoDifferences(EntityProxyDifferences entityProxyTwoDifferences)
    {
        this.entityProxyTwoDifferences = entityProxyTwoDifferences;
    }

    /**
     * Returns the differences between the second entity proxy of the two relationships.
     *
     * @return EntityProxyDifferences
     */
    public EntityProxyDifferences getEntityProxyTwoDifferences()
    {
        return entityProxyTwoDifferences;
    }

    /**
     * Returns true if the two have any differences in their instance properties, or false if the instance properties
     * are the same.
     *
     * @return boolean
     */
    public boolean hasInstancePropertiesDifferences()
    {
        return instancePropertiesDifferences != null && instancePropertiesDifferences.hasDifferences();
    }

    /**
     * Returns the differences between instance properties of these instances, or null if there either are no instance
     * properties on the instance or the instance properties are the same on both instances.
     *
     * @return InstancePropertiesDifferences
     */
    public InstancePropertiesDifferences getInstancePropertiesDifferences()
    {
        return instancePropertiesDifferences;
    }

    /**
     * Determine if there is a difference between the provided instance properties, and capture either each of those
     * differences or the similarity.
     *
     * @param left the instance properties from the first instance
     * @param right the instance properties from the second instance
     */
    public void checkInstanceProperties(InstanceProperties left, InstanceProperties right)
    {
        instancePropertiesDifferences = new InstancePropertiesDifferences();
        instancePropertiesDifferences.check(left, right);
    }

}
