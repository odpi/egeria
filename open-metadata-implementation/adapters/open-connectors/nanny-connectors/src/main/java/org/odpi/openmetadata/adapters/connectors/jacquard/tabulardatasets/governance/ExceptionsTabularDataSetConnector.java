/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.governance;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRelationshipDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * ExceptionsTabularDataSetConnector enables interaction with the list of exceptions stored in open metadata
 * as if it is a tabular data set.
 */
public class ExceptionsTabularDataSetConnector extends OpenMetadataRelationshipDataSetConnectorBase
{
    private static final String myConnectorName = ExceptionsTabularDataSetConnector.class.getName();

    /**
     * Default constructor
     */
    public ExceptionsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.EXCEPTIONS);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    @Override
    public void refreshCache() throws ConnectorCheckedException
    {
        refreshCache(OpenMetadataType.EXCEPTION_RELATIONSHIP.typeName);
    }
}