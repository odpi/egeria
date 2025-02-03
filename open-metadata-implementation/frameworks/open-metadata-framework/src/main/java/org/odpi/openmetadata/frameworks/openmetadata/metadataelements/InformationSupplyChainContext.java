/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import java.util.List;


/**
 * Describe the associated information supply chains for a solution component/asset.  It may be necessary to
 * navigate through one or more solution components to get to the information supply chain segment and then the
 * supply chain itself.
 *
 * @param parentComponents optional list of parent components necessary to pass through to get to information supply chain
 * @param linkedSegment information supply chain segment
 * @param owningInformationSupplyChain owning information supply chain
 */
public record InformationSupplyChainContext(List<RelatedMetadataElementSummary> parentComponents,
                                            RelatedMetadataElementSummary       linkedSegment,
                                            RelatedMetadataElementSummary       owningInformationSupplyChain)
{
}
