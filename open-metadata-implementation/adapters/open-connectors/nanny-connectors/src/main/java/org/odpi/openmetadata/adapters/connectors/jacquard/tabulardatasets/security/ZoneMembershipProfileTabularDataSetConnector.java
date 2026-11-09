/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.security;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.adapters.connectors.jacquard.tabulardatasets.OpenMetadataRowDataSetConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.ZoneMembershipProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Map;
import java.util.TreeSet;


/**
 * ZoneMembershipProfileTabularDataSetConnector lists, for each governance zone, the count of its members of each
 * open metadata type as if they are a tabular data set.  There is one row per zone and type.  The counts come from
 * the zone's ZoneMembershipProfile classification, so a zone that has not been profiled has no rows.
 */
public class ZoneMembershipProfileTabularDataSetConnector extends OpenMetadataRowDataSetConnectorBase
{
    private static final String myConnectorName = ZoneMembershipProfileTabularDataSetConnector.class.getName();


    /**
     * Default constructor
     */
    public ZoneMembershipProfileTabularDataSetConnector()
    {
        super(myConnectorName, ProductDefinitionEnum.ZONE_MEMBERSHIP_PROFILE);
    }


    /**
     * One row per governance zone and each type counted in its membership profile.
     *
     * @throws Exception problem retrieving the metadata
     */
    @Override
    protected void collectRows() throws Exception
    {
        for (OpenMetadataRootElement governanceZone : getAllElements(OpenMetadataType.GOVERNANCE_ZONE.typeName, 0))
        {
            if ((governanceZone.getElementHeader().getZoneMembershipProfile() != null) &&
                    (governanceZone.getElementHeader().getZoneMembershipProfile().getClassificationProperties() instanceof ZoneMembershipProfileProperties profileProperties))
            {
                /*
                 * A type can appear in any of the three counts; each row brings together the counts for one type.
                 */
                TreeSet<String> typeNames = new TreeSet<>();

                addTypeNames(typeNames, profileProperties.getTypeMembership());
                addTypeNames(typeNames, profileProperties.getAnchoredTypeMembership());
                addTypeNames(typeNames, profileProperties.getAllTypeMembership());

                for (String typeName : typeNames)
                {
                    Map<String, String> values = newValues();

                    putValue(values, ProductDataFieldDefinition.OPEN_METADATA_TYPE_NAME, typeName);
                    putValue(values, ProductDataFieldDefinition.TOTAL_TYPE_MEMBERSHIP, getCount(profileProperties.getTypeMembership(), typeName));
                    putValue(values, ProductDataFieldDefinition.ANCHORED_TYPE_MEMBERSHIP, getCount(profileProperties.getAnchoredTypeMembership(), typeName));
                    putValue(values, ProductDataFieldDefinition.ALL_TYPE_MEMBERSHIP, getCount(profileProperties.getAllTypeMembership(), typeName));

                    addRow(governanceZone, values);
                }
            }
        }
    }


    /**
     * Add the type names from one of the counts.
     *
     * @param typeNames accumulating type names
     * @param counts counts keyed by type name
     */
    private void addTypeNames(TreeSet<String>   typeNames,
                              Map<String, Long> counts)
    {
        if (counts != null)
        {
            for (String typeName : counts.keySet())
            {
                if (typeName != null)
                {
                    typeNames.add(typeName);
                }
            }
        }
    }


    /**
     * Return the count for a type, which is zero if the type is not in the count.
     *
     * @param counts counts keyed by type name
     * @param typeName type name
     * @return count
     */
    private long getCount(Map<String, Long> counts,
                          String            typeName)
    {
        if ((counts != null) && (counts.get(typeName) != null))
        {
            return counts.get(typeName);
        }

        return 0L;
    }
}
