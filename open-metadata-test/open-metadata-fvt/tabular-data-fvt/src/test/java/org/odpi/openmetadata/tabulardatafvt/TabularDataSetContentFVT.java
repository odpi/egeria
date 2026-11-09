/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.tabulardatafvt;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDataFieldDefinition;
import org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog.ProductDefinitionEnum;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataCollection;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.ReadableTabularDataSource;
import org.odpi.openmetadata.frameworks.connectors.tabulardatasets.TabularColumnDescription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * TabularDataSetContentFVT checks what the data sets say, now that the Coco Pharmaceuticals archive gives them
 * something to say.  {@link TabularDataSetContractFVT} checks the shape of every data set; this suite reads the
 * rows.
 * <br><br>
 * It still avoids pinning the content packs - there are no row counts or names here, so adding a team to Coco
 * does not break it.  What it checks holds whatever the data is:
 * <ul>
 *     <li><b>Identifiers identify.</b>  Every row's identifier columns are filled in and no two rows share
 *     them.  A consumer loading the data set into a table keyed on them would otherwise lose rows or refuse
 *     the load.</li>
 *     <li><b>Data sets that the loaded archives populate have rows.</b>  An empty data set passes every shape check, so a
 *     connector that silently found nothing would otherwise go unnoticed.</li>
 *     <li><b>The joins hold.</b>  The data sets in a family are published to be joined on their unique
 *     identifiers, so a GUID in one has to be found in the data set it refers to.</li>
 * </ul>
 */
@ExtendWith(OMAGPlatformExtension.class)
public class TabularDataSetContentFVT
{
    /**
     * The data sets the product catalogue declares a connector for.
     *
     * @return test cases
     */
    static List<TabularDataSetCatalog.DataSetUnderTest> dataSets()
    {
        return TabularDataSetCatalog.dataSetsUnderTest();
    }


    /**
     * Every row of every single-table data set has its identifier columns filled in, and no two rows share
     * them.
     *
     * @param dataSet the data set to read
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("dataSets")
    @DisplayName("Every row of a data set is identified by its identifier columns")
    void identifiersAreUniqueAndFilledIn(TabularDataSetCatalog.DataSetUnderTest dataSet) throws Exception
    {
        List<String> identifierColumns = new ArrayList<>();

        for (ProductDataFieldDefinition identifier : dataSet.definition().getDataSpecIdentifiers())
        {
            identifierColumns.add(identifier.getDisplayName());
        }

        List<Map<String, String>> rows = readAllRows(dataSet.definition());

        if (rows == null)
        {
            return; // a collection of tables - each member is a product of its own and is checked as one
        }

        Map<List<String>, Integer> rowNumberByKey = new HashMap<>();

        for (int rowNumber = 0; rowNumber < rows.size(); rowNumber++)
        {
            List<String> key = new ArrayList<>();

            for (String identifierColumn : identifierColumns)
            {
                String value = rows.get(rowNumber).get(identifierColumn);

                assertNotNull(value, dataSet.productName() + " row " + rowNumber + " has no value in its" +
                        " identifier column '" + identifierColumn + "': " + rows.get(rowNumber));

                key.add(value);
            }

            Integer earlierRow = rowNumberByKey.put(key, rowNumber);

            if (earlierRow != null)
            {
                fail(dataSet.productName() + " rows " + earlierRow + " and " + rowNumber + " have the same" +
                             " identifiers " + identifierColumns + " = " + key + ", so a consumer keying on them" +
                             " would lose one of them.\n      row " + earlierRow + ": " + rows.get(earlierRow) +
                             "\n      row " + rowNumber + ": " + rows.get(rowNumber));
            }
        }
    }


    /**
     * The data sets that the archives loaded at startup populate: the open metadata types, and the Coco
     * organization and its systems.
     *
     * @return test cases
     */
    static List<TabularDataSetCatalog.DataSetUnderTest> populatedDataSets()
    {
        Set<ProductDefinitionEnum> populated = Set.of(ProductDefinitionEnum.TYPES_LIST,
                                                      ProductDefinitionEnum.DATA_TYPES_LIST,
                                                      ProductDefinitionEnum.PROPERTIES_LIST,
                                                      ProductDefinitionEnum.ATTRIBUTES_FOR_TYPES_LIST,
                                                      ProductDefinitionEnum.ORGANIZATIONS,
                                                      ProductDefinitionEnum.PEOPLE,
                                                      ProductDefinitionEnum.LOCATIONS,
                                                      ProductDefinitionEnum.BUSINESS_CAPABILITIES,
                                                      ProductDefinitionEnum.DEPARTMENTS,
                                                      ProductDefinitionEnum.BUSINESS_CAPABILITY_DEPARTMENTS,
                                                      ProductDefinitionEnum.SERVERS,
                                                      ProductDefinitionEnum.USER_IDENTITIES);

        List<TabularDataSetCatalog.DataSetUnderTest> dataSets = new ArrayList<>();

        for (TabularDataSetCatalog.DataSetUnderTest dataSet : TabularDataSetCatalog.dataSetsUnderTest())
        {
            if (populated.contains(dataSet.definition()))
            {
                dataSets.add(dataSet);
            }
        }

        return dataSets;
    }


    /**
     * The data sets that the archives loaded at startup populate have rows.
     *
     * @param dataSet the data set to read
     * @throws Exception any failure - which is the finding
     */
    @ParameterizedTest(name = "{0}")
    @MethodSource("populatedDataSets")
    @DisplayName("The data sets that the loaded archives populate have rows")
    void populatedDataSetsHaveRows(TabularDataSetCatalog.DataSetUnderTest dataSet) throws Exception
    {
        List<Map<String, String>> rows = readAllRows(dataSet.definition());

        assertNotNull(rows, dataSet.productName() + " is a collection, not a single data set");
        assertFalse(rows.isEmpty(), dataSet.productName() + " has no rows although the archives loaded at" +
                " startup have elements for it");
    }


    /**
     * Each department-to-capability row refers to a business capability in the capability list and a
     * department in the department list, and some of the capabilities have departments.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    @DisplayName("Business capability departments join to the capability and department lists")
    void businessCapabilityDepartmentsJoin() throws Exception
    {
        Set<String> capabilityGUIDs = columnValues(ProductDefinitionEnum.BUSINESS_CAPABILITIES, ProductDataFieldDefinition.GUID);
        Set<String> departmentGUIDs = columnValues(ProductDefinitionEnum.DEPARTMENTS, ProductDataFieldDefinition.GUID);

        List<Map<String, String>> links = readAllRows(ProductDefinitionEnum.BUSINESS_CAPABILITY_DEPARTMENTS);

        assertNotNull(links);
        assertFalse(links.isEmpty(), "No department is linked to any business capability");

        for (Map<String, String> link : links)
        {
            assertTrue(capabilityGUIDs.contains(link.get(ProductDataFieldDefinition.BUSINESS_CAPABILITY_GUID.getDisplayName())),
                       "Business capability department row refers to a capability that is not in the business" +
                               " capability list: " + link);
            assertTrue(departmentGUIDs.contains(link.get(ProductDataFieldDefinition.DEPARTMENT_GUID.getDisplayName())),
                       "Business capability department row refers to a department that is not in the" +
                               " department list: " + link);
        }
    }


    /**
     * Each department's parent is either another department or an organization, and the organization at the
     * top of its structure is in the organization list.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    @DisplayName("Departments join to their parent team and their organization")
    void departmentsJoinToTheirStructure() throws Exception
    {
        Set<String> organizationGUIDs = columnValues(ProductDefinitionEnum.ORGANIZATIONS, ProductDataFieldDefinition.GUID);
        Set<String> departmentGUIDs   = columnValues(ProductDefinitionEnum.DEPARTMENTS, ProductDataFieldDefinition.GUID);

        List<Map<String, String>> departments = readAllRows(ProductDefinitionEnum.DEPARTMENTS);

        assertNotNull(departments);

        int withAnOrganization = 0;

        for (Map<String, String> department : departments)
        {
            String superTeamGUID    = department.get(ProductDataFieldDefinition.SUPER_TEAM_GUID.getDisplayName());
            String organizationGUID = department.get(ProductDataFieldDefinition.ORGANIZATION_GUID.getDisplayName());

            if (superTeamGUID != null)
            {
                assertTrue(departmentGUIDs.contains(superTeamGUID) || organizationGUIDs.contains(superTeamGUID),
                           "Department's parent team is neither a department nor an organization: " + department);
            }

            if (organizationGUID != null)
            {
                withAnOrganization++;

                assertTrue(organizationGUIDs.contains(organizationGUID),
                           "Department's organization is not in the organization list: " + department);
            }
        }

        assertTrue(withAnOrganization > 0, "No department could be traced up to an organization");
    }


    /**
     * A server's location is in the location list, and some servers have one.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    @DisplayName("Software servers join to the location list")
    void serversJoinToTheirLocations() throws Exception
    {
        Set<String> locationGUIDs = columnValues(ProductDefinitionEnum.LOCATIONS, ProductDataFieldDefinition.GUID);

        List<Map<String, String>> servers = readAllRows(ProductDefinitionEnum.SERVERS);

        assertNotNull(servers);

        int located = 0;

        for (Map<String, String> server : servers)
        {
            String locationGUID = server.get(ProductDataFieldDefinition.ASSET_LOCATION_GUID.getDisplayName());

            if (locationGUID != null)
            {
                located++;

                assertTrue(locationGUIDs.contains(locationGUID),
                           "Server's location is not in the location list: " + server);
            }
        }

        assertTrue(located > 0, "None of the " + servers.size() + " servers has a location");
    }


    /**
     * A user identity's profile is the actor profile it is linked to - not the identity itself - and is in the
     * people list when it is a person.  The profile name comes from that profile.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    @DisplayName("User identities join to their actor profiles")
    void userIdentitiesJoinToTheirProfiles() throws Exception
    {
        Set<String> personGUIDs = columnValues(ProductDefinitionEnum.PEOPLE, ProductDataFieldDefinition.GUID);

        List<Map<String, String>> userIdentities = readAllRows(ProductDefinitionEnum.USER_IDENTITIES);

        assertNotNull(userIdentities);

        int people = 0;

        for (Map<String, String> userIdentity : userIdentities)
        {
            String profileGUID = userIdentity.get(ProductDataFieldDefinition.PROFILE_GUID.getDisplayName());

            if (profileGUID != null)
            {
                assertNotNull(userIdentity.get(ProductDataFieldDefinition.PROFILE_NAME.getDisplayName()),
                              "User identity's profile has no name: " + userIdentity);

                if (personGUIDs.contains(profileGUID))
                {
                    people++;
                }
            }
        }

        assertTrue(people > 0, "None of the " + userIdentities.size() + " user identities is linked to a person in the people list");
    }


    /**
     * Return the values of one column across a data set.
     *
     * @param definition data set
     * @param column column
     * @return values
     * @throws Exception problem reading the data set
     */
    private Set<String> columnValues(ProductDefinitionEnum      definition,
                                     ProductDataFieldDefinition column) throws Exception
    {
        List<Map<String, String>> rows = readAllRows(definition);

        assertNotNull(rows, definition.getProductName() + " is a collection, not a single data set");

        Set<String> values = new HashSet<>();

        for (Map<String, String> row : rows)
        {
            if (row.get(column.getDisplayName()) != null)
            {
                values.add(row.get(column.getDisplayName()));
            }
        }

        return values;
    }


    /**
     * Read every row of a data set, keyed by column name.
     *
     * @param definition data set
     * @return rows, or null if the data set is a collection of tables
     * @throws Exception problem reading the data set
     */
    private List<Map<String, String>> readAllRows(ProductDefinitionEnum definition) throws Exception
    {
        Connector connector = definition.getConnectorProvider().getConnector(TabularDataSetConnections.connectionFor(definition.getProductName()));

        try
        {
            connector.start();

            if ((! (connector instanceof ReadableTabularDataSource dataSource)) ||
                    (connector instanceof ReadableTabularDataCollection))
            {
                return null;
            }

            List<TabularColumnDescription> columns = dataSource.getColumnDescriptions();
            List<Map<String, String>>      rows    = new ArrayList<>();
            long                           count   = dataSource.getRecordCount();

            for (long rowNumber = 0; rowNumber < count; rowNumber++)
            {
                List<String>        record = dataSource.readRecord(rowNumber);
                Map<String, String> row    = new LinkedHashMap<>();

                for (int position = 0; position < columns.size(); position++)
                {
                    row.put(columns.get(position).columnName(), record.get(position));
                }

                rows.add(row);
            }

            return rows;
        }
        finally
        {
            connector.disconnect();
        }
    }
}
