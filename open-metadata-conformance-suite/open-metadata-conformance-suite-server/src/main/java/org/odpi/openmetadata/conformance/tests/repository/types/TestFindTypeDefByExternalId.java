/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Test that all type definitions are retrieved by findTypeByExternalId.
 */
public class TestFindTypeDefByExternalId extends RepositoryConformanceTestCase
{
    private static final String testCaseId = "repository-find-types-by-external-standard-identifiers";
    private static final String testCaseName = "Repository find type definitions by external standard identifiers test case";

    private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = "All type definitions returned for external standard name ";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = "All type definitions returned for external standard organization name ";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = "All type definitions returned for external standard type name ";

    private static final String successMessage  = "All type definitions returned for external standard mappings.";

    private static final String discoveredProperty_numberOfMappedTypeDefs = "Number of type definitions mapped to external standard identifiers";

    private final List<TypeDef>               allTypeDefs;
    private final Map<String, List<TypeDef>>  standardsToTypeDefs = new HashMap<>();
    private final Map<String, List<TypeDef>>  orgToTypeDefs = new HashMap<>();
    private final Map<String, List<TypeDef>>  idToTypeDefs = new HashMap<>();


    /**
     * Typical constructor sets up superclass and discovered information needed for tests
     *
     * @param workPad place for parameters and results
     * @param allTypeDefs list of all type definitions returned by repository
     */
    public TestFindTypeDefByExternalId(RepositoryConformanceWorkPad workPad,
                                       List<TypeDef>                allTypeDefs)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

        this.allTypeDefs = allTypeDefs;
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        if ((allTypeDefs == null) || (allTypeDefs.isEmpty()))
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfMappedTypeDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
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

            super.addDiscoveredProperty(discoveredProperty_numberOfMappedTypeDefs,
                                        mappedTypeDefs,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

            /*
             * Step through the mapped type definitions and call the search request.
             */
            for (String  standard : standardsToTypeDefs.keySet())
            {
                long start = System.currentTimeMillis();
                List<TypeDef> returnedTypeDefs = metadataCollection.findTypesByExternalID(workPad.getLocalServerUserId(), standard, null, null);
                long elapsedTime = System.currentTimeMillis() - start;
                List<TypeDef> expectedTypeDefs = standardsToTypeDefs.get(standard);

                verifyCondition(((returnedTypeDefs != null) && (returnedTypeDefs.size() == expectedTypeDefs.size())),
                                assertion1,
                                assertionMsg1 + standard,
                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId(),
                                "findTypesByExternalID",
                                elapsedTime);
            }

            for (String  organization : orgToTypeDefs.keySet())
            {
                long start = System.currentTimeMillis();
                List<TypeDef> returnedTypeDefs = metadataCollection.findTypesByExternalID(workPad.getLocalServerUserId(), null, organization, null);
                long elapsedTime = System.currentTimeMillis() - start;
                List<TypeDef> expectedTypeDefs = standardsToTypeDefs.get(organization);

                verifyCondition(((returnedTypeDefs != null) && (returnedTypeDefs.size() == expectedTypeDefs.size())),
                                assertion2,
                                assertionMsg2 + organization,
                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId(),
                                "findTypesByExternalID",
                                elapsedTime);
            }

            for (String  identifier : idToTypeDefs.keySet())
            {
                long start = System.currentTimeMillis();
                List<TypeDef> returnedTypeDefs = metadataCollection.findTypesByExternalID(workPad.getLocalServerUserId(), null, null, identifier);
                long elapsedTime = System.currentTimeMillis() - start;
                List<TypeDef> expectedTypeDefs = standardsToTypeDefs.get(identifier);

                verifyCondition(((returnedTypeDefs != null) && (returnedTypeDefs.size() == expectedTypeDefs.size())),
                                assertion3,
                                assertionMsg3 + identifier,
                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId(),
                                "findTypesByExternalID",
                                elapsedTime);
            }
        }

        super.setSuccessMessage(successMessage);
    }
}
