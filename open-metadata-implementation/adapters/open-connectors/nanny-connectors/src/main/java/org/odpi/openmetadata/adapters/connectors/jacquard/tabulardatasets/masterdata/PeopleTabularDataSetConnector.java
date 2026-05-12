/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRootDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * PeopleTabularDataSetConnector enables interaction with the list of people stored in open metadata
 * as if it is a tabular data set.
 */
public class PeopleTabularDataSetConnector extends OpenMetadataRootDataSetConnectorBase
{
    private static final String myConnectorName = PeopleTabularDataSetConnector.class.getName();

    /**
     * Default constructor
     */
    public PeopleTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.PEOPLE);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    @Override
    public void refreshCache() throws ConnectorCheckedException
    {
        refreshCache(OpenMetadataType.PERSON.typeName);
    }
}