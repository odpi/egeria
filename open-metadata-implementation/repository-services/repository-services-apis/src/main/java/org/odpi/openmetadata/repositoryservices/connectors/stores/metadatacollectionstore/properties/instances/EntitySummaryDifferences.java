/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

/**
 * Capture the differences between EntitySummary objects.  Within this object there are the following distinct areas
 * for which differences are identified:
 * <ul>
 *     <li>ClassificationDifferences - list the differences in classifications, irrespective of the ordering of the
 *          classifications.</li>
 * </ul>
 */
public class EntitySummaryDifferences extends Differences
{
    private ClassificationDifferences classificationDifferences = null;

    /**
     * Returns true if the two have any differences (are not equal), otherwise false.
     *
     * @return boolean
     */
    @Override
    public boolean hasDifferences()
    {
        return super.hasDifferences() || hasClassificationDifferences();
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
     * Determine if there is any difference between the provided classifications, and capture either each of those
     * differences or the similarity.  Note that the differences will be calculated ignoring the order in which the
     * classifications are listed for each side.
     *
     * @param left the classifications from the first instance
     * @param right the classifications from the second instance
     */
    public void checkClassifications(EntitySummary left, EntitySummary right)
    {
        classificationDifferences = new ClassificationDifferences();
        classificationDifferences.check(left, right);
    }
}
