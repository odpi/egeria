/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tabulardatafvt;

import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TabularDataSetCatalog says what this suite covers, and it does so by reading the product catalogue rather
 * than by listing anything.
 * <br><br>
 * Every tabular data set Egeria publishes is a digital product, and {@link ProductDefinitionEnum} is where
 * those products are defined - Jacquard builds the catalogue from it, and each entry that has an asset
 * behind it names the connector provider that serves the data.  So the roster of connectors under test is
 * simply "every product definition that names a connector provider", which means a data set added to the
 * catalogue tomorrow is covered tomorrow, with nothing to remember.  That is the same bargain type-fvt
 * makes with the type system.
 * <br><br>
 * The alternative - a hand-kept list, as client-fvt needs for its clients - was not necessary here, and is
 * worth avoiding: the one in client-fvt had quietly lost a case to a map key collision.
 */
class TabularDataSetCatalog
{
    /**
     * Connector providers that serve tabular data but are not reachable from a product definition, each with
     * the reason.  These are the <b>dynamic</b> data sets: one connector serves many tables, chosen at run
     * time, so there is no single product definition that names them.  Their list connectors <i>are</i> in
     * the catalogue and are covered, which is how the dynamic sets are reached.
     */
    static final Map<String, String> NOT_IN_THE_CATALOGUE = new LinkedHashMap<>()
    {{
        put("ReferenceDataSetProvider",
            "serves one reference data set at a time, chosen at run time; reached through " +
                    "ReferenceDataSetListProvider, which is in the catalogue");
        put("ValidMetadataValueDataSetProvider",
            "serves one valid metadata value set at a time, chosen at run time; reached through " +
                    "ValidMetadataValueSetListProvider, which is in the catalogue");
        put("OpenMetadataDataTypesDataSetProvider",
            "serves the data types behind the open metadata types product rather than a product of its own");
        put("SecretsStoresTabularDataSetProvider",
            "not published as a product - a secrets store inventory is not something to hand out");
    }};

    /**
     * Data sets in the catalogue that a connection alone cannot reach, each with the reason.
     * <br><br>
     * A connection carries where the metadata is and who to ask as.  A connector that also needs to be told
     * <i>which</i> element to start from needs that element to exist first, and building it is a different
     * suite's job.
     */
    static final Map<String, String> NEEDS_MORE_THAN_A_CONNECTION = new LinkedHashMap<>()
    {{
        put("DigitalProductFamilyDataSetCollectionConnector",
            "reads one digital product family, named in its startingElementGUID configuration property. " +
                    "The families only exist once the Jacquard Digital Product Loom has built the catalogue, " +
                    "which subscription-fvt stands up - so that is where this connector belongs.");
    }};

    /**
     * The fewest data sets this suite should find in the catalogue.  Not a number to keep up to date - a
     * guard that the catalogue was read at all, so that a refactor which empties it fails loudly rather than
     * leaving every check below passing over nothing.
     */
    static final int FEWEST_EXPECTED_DATA_SETS = 12;


    private TabularDataSetCatalog()
    {
        // no instances
    }


    /**
     * One data set under test: the product definition it belongs to, and the provider that serves it.
     *
     * @param productName the product's name, which names the test case
     * @param definition the product definition
     * @param connectorProvider the provider that serves its data
     */
    record DataSetUnderTest(String                productName,
                            ProductDefinitionEnum definition,
                            ConnectorProvider     connectorProvider)
    {
        @Override
        public String toString()
        {
            return productName;
        }
    }


    /**
     * Return every tabular data set the product catalogue declares a connector for.
     *
     * @return the data sets under test, in catalogue order
     */
    static List<DataSetUnderTest> dataSetsUnderTest()
    {
        List<DataSetUnderTest> dataSets = new ArrayList<>();

        for (ProductDefinitionEnum definition : ProductDefinitionEnum.values())
        {
            ConnectorProvider connectorProvider = definition.getConnectorProvider();

            if ((connectorProvider != null) && (! needsMoreThanAConnection(connectorProvider)))
            {
                dataSets.add(new DataSetUnderTest(definition.getProductName(), definition, connectorProvider));
            }
        }

        return dataSets;
    }


    /**
     * Is this provider's connector one that a connection alone cannot start?
     *
     * @param connectorProvider provider to check
     * @return true when it is listed in {@link #NEEDS_MORE_THAN_A_CONNECTION}
     */
    private static boolean needsMoreThanAConnection(ConnectorProvider connectorProvider)
    {
        String connectorClassName = connectorProvider.getConnectorType() == null
                                            ? null
                                            : connectorProvider.getConnectorType().getConnectorProviderClassName();

        /*
         * Matched on the connector's own class name rather than the provider's, because the reason it cannot
         * be started belongs to the connector.  The provider name is derived from it.
         */
        for (String connectorName : NEEDS_MORE_THAN_A_CONNECTION.keySet())
        {
            String providerName = connectorName.replace("Connector", "Provider");

            if (connectorProvider.getClass().getSimpleName().equals(providerName)
                        || ((connectorClassName != null) && connectorClassName.endsWith(providerName)))
            {
                return true;
            }
        }

        return false;
    }
}
