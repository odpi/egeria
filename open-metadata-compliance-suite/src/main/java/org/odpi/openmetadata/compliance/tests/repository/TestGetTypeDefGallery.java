/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.compliance.tests.repository;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestGetTypeDefGallery extends OpenMetadataRepositoryTestCase
{
    private static final  String testUserId = "ComplianceTestUser";

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


    /**
     * Default constructor sets up superclass
     */
    TestGetTypeDefGallery(String   workbenchId)
    {
        super(workbenchId, testCaseId, testCaseName);
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

        TypeDefGallery  typeDefGallery = metadataCollection.getAllTypes(testUserId);
        assertCondition((true), assertion1, assertionMsg1);

        if (typeDefGallery == null)
        {
            discoveredProperties.put(discoveredProperty_numberOfAttributeTypeDefs, 0);
            discoveredProperties.put(discoveredProperty_numberOfTypeDefs, 0);
        }
        else
        {
            attributeTypeDefs = typeDefGallery.getAttributeTypeDefs();
            typeDefs = typeDefGallery.getTypeDefs();

            if (attributeTypeDefs == null)
            {
                discoveredProperties.put(discoveredProperty_numberOfAttributeTypeDefs, 0);
            }
            else
            {
                discoveredProperties.put(discoveredProperty_numberOfAttributeTypeDefs, attributeTypeDefs.size());

                List<String>    supportedTypes = new ArrayList<>();

                for (AttributeTypeDef   attributeTypeDef : attributeTypeDefs)
                {
                    supportedTypes.add(attributeTypeDef.getName());
                }

                discoveredProperties.put(discoveredProperty_attributeTypeDefs, supportedTypes);
            }

            if (typeDefs == null)
            {
                discoveredProperties.put(discoveredProperty_numberOfTypeDefs, 0);
            }
            else
            {
                discoveredProperties.put(discoveredProperty_numberOfTypeDefs, typeDefs.size());

                List<String>    supportedTypes = new ArrayList<>();

                for (TypeDef   typeDef : typeDefs)
                {
                    supportedTypes.add(typeDef.getName());
                }

                discoveredProperties.put(discoveredProperty_typeDefs, supportedTypes);
            }
        }

        super.result.setSuccessMessage(testCaseSuccessMessage);

        super.result.setDiscoveredProperties(discoveredProperties);
    }
}
