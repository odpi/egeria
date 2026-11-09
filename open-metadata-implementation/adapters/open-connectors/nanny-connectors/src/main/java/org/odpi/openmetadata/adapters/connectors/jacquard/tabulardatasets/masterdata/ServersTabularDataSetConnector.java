/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.masterdata;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;


/**
 * ServersTabularDataSetConnector lists the software servers catalogued in open metadata as if they are a tabular
 * data set.  There is one row per software server.  The location of the server comes from its KnownLocation
 * relationship.
 */
public class ServersTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = ServersTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public ServersTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.SERVERS);
    }


    /**
     * One row per software server.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement server : getAllElements(OpenMetadataType.SOFTWARE_SERVER.typeName,
                                                             1,
                                                             List.of(OpenMetadataType.KNOWN_LOCATION_RELATIONSHIP.typeName)))
        {
            Map<String, String> values = newValues();

            putValue(values, ProductDataFieldDefinition.ASSET_LOCATION_GUID, getLocationGUID(server));

            addRow(server, values);
        }
    }


    /**
     * Return the unique identifier of the server's location.  A server is normally in one location; if more are
     * recorded, the first is used so that the column can be joined to the location list.
     *
     * @param server server element
     * @return guid or null
     */
    private String getLocationGUID(OpenMetadataRootElement server)
    {
        if (server.getKnownLocations() != null)
        {
            for (RelatedMetadataElementSummary location : server.getKnownLocations())
            {
                if ((location != null) && (location.getRelatedElement() != null))
                {
                    return location.getRelatedElement().getElementHeader().getGUID();
                }
            }
        }

        return null;
    }
}
