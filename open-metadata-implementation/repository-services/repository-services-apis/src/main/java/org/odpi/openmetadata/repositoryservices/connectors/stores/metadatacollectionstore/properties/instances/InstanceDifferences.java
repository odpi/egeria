/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import java.util.List;

/**
 * Capture the differences between instances.  Within this object there are the following distinct areas for which
 * differences are identified:
 * <ul>
 *     <li>General properties of the instance, as described in the Differences class from which this inherits.</li>
 *     <li>InstancePropertiesDifferences - list the differences in instance properties, for those instances that
 *          have properties (eg. EntityDetail, Relationship).</li>
 *     <li>ClassificationDifferences - list the differences in classifications, irrespective of the ordering of the
 *          classifications, for those instances that have classifications (eg. EntitySummary, EntityDetail).</li>
 * </ul>
 */
public class InstanceDifferences extends Differences
{

    private InstancePropertiesDifferences instancePropertiesDifferences = null;
    private ClassificationDifferences classificationDifferences = null;

    /**
     * Construct a new set of differences between two instances.
     */
    public InstanceDifferences()
    {
        super();
    }

    /**
     * Returns true if the two have any differences (are not equal), otherwise false.
     *
     * @return boolean
     */
    @Override
    public boolean hasDifferences()
    {
        return super.hasDifferences() || hasInstancePropertiesDifferences() || hasClassificationDifferences();
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
     * Returns true if the two have any differences in their classifications, or false if the classifications are the
     * same.
     *
     * @return boolean
     */
    public boolean hasClassificationDifferences()
    {
        return classificationDifferences != null && classificationDifferences.hasDifferences();
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
     * Returns the differences between classifications of these instances, or null if there are either no
     * classifications on the instance or the classifications are the same on both instances.
     *
     * @return ClassificationDifferences
     */
    public ClassificationDifferences getClassificationDifferences()
    {
        return classificationDifferences;
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

    /**
     * Determine if there is any difference between the provided classifications, and capture either each of those
     * differences or the similarity.  Note that the differences will be calculated ignoring the order in which the
     * classifications are listed for each side.
     *
     * @param left the classifications from the first instance
     * @param right the classifications from the second instance
     */
    public void checkClassifications(List<Classification> left, List<Classification> right)
    {
        classificationDifferences = new ClassificationDifferences();
        classificationDifferences.check(left, right);
    }

}
