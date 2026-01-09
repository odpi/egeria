/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.referencedata;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRootDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * ValidValueSetListConnector maintains a tabular data set that lists the valid value sets in open metadata.
 * Valid value sets are valid value definitions with no valid value parent and one or more members.
 */
public class ReferenceDataSetListConnector extends OpenMetadataRootDataSetConnectorBase
{
    private static final String myConnectorName = "ReferenceDataSetListConnector";


    /**
     * Default constructor
     */
    public ReferenceDataSetListConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.REFERENCE_DATA_SET_LIST);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    public void refreshCache() throws ConnectorCheckedException
    {
        refreshCache(OpenMetadataType.REFERENCE_DATA_SET.typeName);
    }
}