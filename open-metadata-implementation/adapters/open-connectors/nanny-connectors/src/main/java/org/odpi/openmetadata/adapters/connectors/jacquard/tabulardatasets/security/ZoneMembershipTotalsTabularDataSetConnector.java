/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * ZoneMembershipTotalsTabularDataSetConnector lists the governance zones and the total count of their members
 * as if they are a tabular data set.  There is one row per governance zone.  The counts come from the zone's
 * ZoneMembershipProfile classification, which is maintained by a zone membership profiling service; they are
 * empty for a zone that has not been profiled.
 */
public class ZoneMembershipTotalsTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = ZoneMembershipTotalsTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public ZoneMembershipTotalsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.ZONE_MEMBERSHIP_TOTALS);
    }


    /**
     * One row per governance zone.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement governanceZone : getAllElements(OpenMetadataType.GOVERNANCE_ZONE.typeName, 0))
        {
            addRow(governanceZone, newValues());
        }
    }
}
