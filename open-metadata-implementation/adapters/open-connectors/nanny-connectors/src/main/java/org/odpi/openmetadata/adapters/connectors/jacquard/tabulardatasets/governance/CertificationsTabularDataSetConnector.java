/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.governance;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRelationshipDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


/**
 * CertificationsTabularDataSetConnector enables interaction with the list of certifications stored in open metadata
 * as if it is a tabular data set.
 */
public class CertificationsTabularDataSetConnector extends OpenMetadataRelationshipDataSetConnectorBase
{
    private static final String myConnectorName = CertificationsTabularDataSetConnector.class.getName();

    /**
     * Default constructor
     */
    public CertificationsTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.CERTIFICATIONS);
    }


    /**
     * Refresh any cached values.
     *
     * @throws ConnectorCheckedException unable to refresh
     */
    @Override
    public void refreshCache() throws ConnectorCheckedException
    {
        refreshCache(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName);
    }
}