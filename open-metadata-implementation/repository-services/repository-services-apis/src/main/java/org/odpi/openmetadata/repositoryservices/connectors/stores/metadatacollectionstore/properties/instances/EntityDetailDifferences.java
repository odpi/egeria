/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

/**
 * Capture the differences between EntityDetail objects.
 */
public class EntityDetailDifferences extends EntitySummaryDifferences {

    private InstancePropertiesDifferences instancePropertiesDifferences = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasDifferences()
    {
        return super.hasDifferences() || hasInstancePropertiesDifferences();
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
