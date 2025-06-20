/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import java.util.List;


/**
 * Describe the associated information supply chains for a solution component/asset.  It may be necessary to
 * navigate through one or more solution components to get to the information supply chain itself.
 *
 * @param parentComponents optional list of parent components necessary to pass through to get to information supply chain
 * @param owningInformationSupplyChains list of information supply chain directly connected to one or more parent components
 */
public record InformationSupplyChainContext(List<RelatedMetadataElementSummary> parentComponents,
                                            List<RelatedMetadataElementSummary> owningInformationSupplyChains)
{
}
