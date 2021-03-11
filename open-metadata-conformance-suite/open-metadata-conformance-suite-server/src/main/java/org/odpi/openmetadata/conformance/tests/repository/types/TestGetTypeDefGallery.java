/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.tests.repository.types;

import org.odpi.openmetadata.conformance.tests.repository.RepositoryConformanceTestCase;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceProfileRequirement;
import org.odpi.openmetadata.conformance.workbenches.repository.RepositoryConformanceWorkPad;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;

import java.util.ArrayList;
import java.util.List;

public class TestGetTypeDefGallery extends RepositoryConformanceTestCase
{
    private static final  String testCaseId = "repository-get-typedef-gallery";
    private static final  String testCaseName = "Repository get type definition gallery test case";
    private static final  String testCaseSuccessMessage = "Repository type definition gallery retrieved";

    private static final  String assertion1     = testCaseId + "-01";
    private static final  String assertionMsg1  = "TypeDefGallery retrieved.";

    private static final  String discoveredProperty_numberOfAttributeTypeDefs = "Number of supported AttributeTypeDefs";
    private static final  String discoveredProperty_attributeTypeDefs = "Supported AttributeTypeDefs";
    private static final  String discoveredProperty_numberOfTypeDefs = "Number of supported TypeDefs";
    private static final  String discoveredProperty_typeDefs = "Supported TypeDefs";

    List<AttributeTypeDef> attributeTypeDefs = null;
    List<TypeDef>          typeDefs = null;


    /**
     * Typical constructor sets up superclass
     *
     * @param workPad place for parameters and results
     */
    public TestGetTypeDefGallery(RepositoryConformanceWorkPad workPad)
    {
        super(workPad,
              testCaseId,
              testCaseName,
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
              RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
    }


    /**
     * Method implemented by the actual test case.
     *
     * @throws Exception something went wrong with the test.
     */
    protected void run() throws Exception
    {
        OMRSMetadataCollection metadataCollection = super.getMetadataCollection();

        long start = System.currentTimeMillis();
        TypeDefGallery  typeDefGallery = metadataCollection.getAllTypes(workPad.getLocalServerUserId());
        long elapsedTime = System.currentTimeMillis() - start;
        assertCondition((true),
                        assertion1,
                        assertionMsg1,
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId(),
                        "getAllTypes",
                        elapsedTime);

        if (typeDefGallery == null)
        {
            super.addDiscoveredProperty(discoveredProperty_numberOfAttributeTypeDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            super.addDiscoveredProperty(discoveredProperty_numberOfTypeDefs,
                                        0,
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                        RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
        }
        else
        {
            attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
            repositoryConformanceWorkPad.setSupportedAttributeTypeDefsFromRESTAPI(attributeTypeDefs);
            typeDefs = typeDefGallery.getTypeDefs();
            repositoryConformanceWorkPad.setSupportedTypeDefsFromRESTAPI(typeDefs);

            if (attributeTypeDefs == null)
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfAttributeTypeDefs,
                                            0,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }
            else
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfAttributeTypeDefs,
                                            attributeTypeDefs.size(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());

                List<String>    supportedTypes = new ArrayList<>();

                for (AttributeTypeDef   attributeTypeDef : attributeTypeDefs)
                {
                    supportedTypes.add(attributeTypeDef.getName());
                }

                super.addDiscoveredProperty(discoveredProperty_attributeTypeDefs,
                                            supportedTypes,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }

            if (typeDefs == null)
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfTypeDefs,
                                            0,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }
            else
            {
                super.addDiscoveredProperty(discoveredProperty_numberOfTypeDefs,
                                            typeDefs.size(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());


                List<String>    supportedTypes = new ArrayList<>();

                for (TypeDef   typeDef : typeDefs)
                {
                    supportedTypes.add(typeDef.getName());
                }

                super.addDiscoveredProperty(discoveredProperty_typeDefs,
                                            supportedTypes,
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getProfileId(),
                                            RepositoryConformanceProfileRequirement.SUPPORTED_TYPE_QUERIES.getRequirementId());
            }
        }

        super.setSuccessMessage(testCaseSuccessMessage);
    }


    /**
     * Return the list of retrieved attribute type definitions.
     *
     * @return list of AttributeTypeDefs
     */
    public List<AttributeTypeDef> getAttributeTypeDefs()
    {
        return attributeTypeDefs;
    }


    /**
     * Return the list of retrieved type definitions.
     *
     * @return list of TypeDefs
     */
    public List<TypeDef> getTypeDefs()
    {
        return typeDefs;
    }
}
