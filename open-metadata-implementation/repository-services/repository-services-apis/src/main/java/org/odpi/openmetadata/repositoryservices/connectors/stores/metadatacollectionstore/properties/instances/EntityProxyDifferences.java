/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

/**
 * Capture the differences between EntityProxy objects.
 */
public class EntityProxyDifferences extends EntitySummaryDifferences {

    private InstancePropertiesDifferences uniquePropertiesDifferences = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDifferences()
    {
        return super.hasDifferences() || hasUniquePropertiesDifferences();
    }

    /**
     * Returns true if the two have any differences in their unique properties, or false if the unique properties
     * are the same.
     *
     * @return boolean
     */
    public boolean hasUniquePropertiesDifferences()
    {
        return uniquePropertiesDifferences != null && uniquePropertiesDifferences.hasDifferences();
    }

    /**
     * Returns the differences between unique properties of these instances, or null if there either are no unique
     * properties on the instance or the unique properties are the same on both instances.
     *
     * @return InstancePropertiesDifferences
     */
    public InstancePropertiesDifferences getUniquePropertiesDifferences()
    {
        return uniquePropertiesDifferences;
    }

    /**
     * Determine if there is a difference between the provided unique properties, and capture either each of those
     * differences or the similarity.
     *
     * @param left the unique properties from the first instance
     * @param right the unique properties from the second instance
     */
    public void checkUniqueProperties(InstanceProperties left, InstanceProperties right)
    {
        uniquePropertiesDifferences = new InstancePropertiesDifferences();
        uniquePropertiesDifferences.check(left, right);
    }

}
