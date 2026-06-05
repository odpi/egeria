/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.definitions;

/**
 * Define the linkage between information supply chain segments.
 */
public interface InformationSupplyChainLinkDefinition
{
    /**
     * Retrieve the definition of the first segment in the information supply chain link.
     *
     * @return the definition of the first information supply chain segment
     */
    InformationSupplyChainDefinition getSegment1();

    /**
     * Retrieve the definition of the second segment in the information supply chain link.
     *
     * @return the definition of the second information supply chain segment
     */
    InformationSupplyChainDefinition getSegment2();

    /**
     * Return the relationship label.
     *
     * @return string
     */
    String getLabel();


    /**
     * Return the relationship description.
     *
     * @return string
     */
    String getDescription();
}
