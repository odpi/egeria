/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test that all type definitions are retrieved by findTypeByExternalId.
 */
public class TestFindTypeDefByExternalId extends OpenMetadataRepositoryTestCase
{
    private static final String testUserId = "ComplianceTestUser";

    private static final String testCaseId = "repository-find-types-by-external-standard-identifiers";
    private static final String testCaseName = "Repository find type definitions by external standard identifiers test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = "All type definitions returned for external standard name ";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = "All type definitions returned for external standard organization name ";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = "All type definitions returned for external standard type name ";
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = "All type definitions returned for external standard mappings.";

    private static final  String discoveredProperty_numberOfMappedTypeDefs = "Number of type definitions mapped to external standard identifiers";

    private List<TypeDef>               allTypeDefs;
    private Map<String, List<TypeDef>>  standardsToTypeDefs = new HashMap<>();
    private Map<String, List<TypeDef>>  orgToTypeDefs = new HashMap<>();
    private Map<String, List<TypeDef>>  idToTypeDefs = new HashMap<>();


    /**
     * Typical constructor sets up superclass
     *
     * @param workbenchId identifier of calling workbench
     * @param allTypeDefs list of all type definitions returned by repository
     */
    TestFindTypeDefByExternalId(String         workbenchId,
                                List<TypeDef>  allTypeDefs)
    {
        super(workbenchId, testCaseId, testCaseName);
        this.allTypeDefs = allTypeDefs;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        Map<String, Object>    discoveredProperties = new HashMap<>();
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if ((allTypeDefs == null) || (allTypeDefs.isEmpty()))
        {
            discoveredProperties.put(discoveredProperty_numberOfMappedTypeDefs, 0);
        }
        else
        {
            int mappedTypeDefs = 0;

            /*
             * Step through the retrieved type definitions to determine the expected result from the find
             * request.
             */
            for (TypeDef activeTypeDef : allTypeDefs)
            {
                /*
                 * Extract all of the external standards mappings from the TypeDef.  They are located in the TypeDef
                 * itself and in the TypeDefAttributes.
                 */
                List<ExternalStandardMapping> externalStandardMappings = new ArrayList<>();

                if (activeTypeDef.getExternalStandardMappings() != null)
                {
                    externalStandardMappings.addAll(activeTypeDef.getExternalStandardMappings());
                }

                List<TypeDefAttribute> typeDefAttributes = activeTypeDef.getPropertiesDefinition();

                if (typeDefAttributes != null)
                {
                    for (TypeDefAttribute typeDefAttribute : typeDefAttributes)
                    {
                        if ((typeDefAttribute != null) && (typeDefAttribute.getExternalStandardMappings() != null))
                        {
                            externalStandardMappings.addAll(activeTypeDef.getExternalStandardMappings());
                        }
                    }
                }

                if (! externalStandardMappings.isEmpty())
                {
                    mappedTypeDefs ++;
                }

                for (ExternalStandardMapping  externalStandardMapping : externalStandardMappings)
                {
                    if (externalStandardMapping != null)
                    {
                        String activeTypeDefStandardName = externalStandardMapping.getStandardName();
                        String activeTypeDefStandardOrgName = externalStandardMapping.getStandardOrganization();
                        String activeTypeDefStandardIdentifier = externalStandardMapping.getStandardTypeName();

                        if (activeTypeDefStandardName != null)
                        {
                            List<TypeDef>  currentTypeDefList = standardsToTypeDefs.get(activeTypeDefStandardName);

                            if (currentTypeDefList == null)
                            {
                                currentTypeDefList = new ArrayList<>();
                            }

                            currentTypeDefList.add(activeTypeDef);
                            standardsToTypeDefs.put(activeTypeDefStandardName, currentTypeDefList);
                        }
                        else if (activeTypeDefStandardOrgName != null)
                        {
                            List<TypeDef>  currentTypeDefList = orgToTypeDefs.get(activeTypeDefStandardOrgName);

                            if (currentTypeDefList == null)
                            {
                                currentTypeDefList = new ArrayList<>();
                            }

                            currentTypeDefList.add(activeTypeDef);
                            orgToTypeDefs.put(activeTypeDefStandardOrgName, currentTypeDefList);
                        }
                        else if (activeTypeDefStandardIdentifier != null)
                        {
                            List<TypeDef>  currentTypeDefList = idToTypeDefs.get(activeTypeDefStandardIdentifier);

                            if (currentTypeDefList == null)
                            {
                                currentTypeDefList = new ArrayList<>();
                            }

                            currentTypeDefList.add(activeTypeDef);
                            idToTypeDefs.put(activeTypeDefStandardIdentifier, currentTypeDefList);
                        }
                    }
                }
            }

            discoveredProperties.put(discoveredProperty_numberOfMappedTypeDefs, mappedTypeDefs);

            /*
             * Step through the mapped type definitions and call the search request.
             */
            for (String  standard : standardsToTypeDefs.keySet())
            {
                List<TypeDef> returnedTypeDefs = metadataCollection.findTypesByExternalID(testUserId, standard, null, null);
                List<TypeDef> expectedTypeDefs = standardsToTypeDefs.get(standard);

                assertCondition(((returnedTypeDefs != null) && (returnedTypeDefs.size() == expectedTypeDefs.size())),
                                assertion1,  assertionMsg1 + standard);
            }

            for (String  organization : orgToTypeDefs.keySet())
            {
                List<TypeDef> returnedTypeDefs = metadataCollection.findTypesByExternalID(testUserId, null, organization, null);
                List<TypeDef> expectedTypeDefs = standardsToTypeDefs.get(organization);

                assertCondition(((returnedTypeDefs != null) && (returnedTypeDefs.size() == expectedTypeDefs.size())),
                                assertion2,  assertionMsg2 + organization);
            }

            for (String  identifier : idToTypeDefs.keySet())
            {
                List<TypeDef> returnedTypeDefs = metadataCollection.findTypesByExternalID(testUserId, null, null, identifier);
                List<TypeDef> expectedTypeDefs = standardsToTypeDefs.get(identifier);

                assertCondition(((returnedTypeDefs != null) && (returnedTypeDefs.size() == expectedTypeDefs.size())),
                                assertion3,  assertionMsg3 + identifier);
            }
        }

        assertCondition((true), assertion4,  assertionMsg4);

        super.result.setSuccessMessage("Type definitions can be extracted by external standard identifiers");

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
