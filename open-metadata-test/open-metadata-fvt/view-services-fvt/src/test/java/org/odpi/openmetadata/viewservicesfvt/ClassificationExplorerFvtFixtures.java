/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Fixture helpers shared by ClassificationExplorerFVT and the smaller view service FVTs written alongside it
 * (AssetCatalogFVT, LineageLinkerFVT, MultiLanguageFVT, NotificationManagerFVT, SubjectAreaFVT, DataEngineerFVT):
 * creating and deleting the data sets and governance definitions they work on, and reading an element's
 * classifications and related elements through the Classification Explorer.
 */
class ClassificationExplorerFvtFixtures
{
    static final ViewServiceClient assetMaker             = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);
    static final ViewServiceClient governanceOfficer      = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);
    static final ViewServiceClient classificationExplorer = new ViewServiceClient(ViewServiceDescription.CLASSIFICATION_EXPLORER);


    /**
     * Create a data set that is its own anchor.
     *
     * @param name distinguishing name
     * @param displayName display name
     * @return its GUID
     */
    static String createDataSet(String name,
                                String displayName)
    {
        DataSetProperties properties = new DataSetProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataSet", name));
        properties.setDisplayName(displayName);
        properties.setDescription("Created by the view-services-fvt.");

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return assetMaker.create("/assets", requestBody);
    }


    /**
     * Delete an asset and check it has gone.
     *
     * @param assetGUID asset to delete (ignored if null)
     */
    static void deleteAsset(String assetGUID)
    {
        if (assetGUID != null)
        {
            assetMaker.call("/assets/" + assetGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + assetGUID + "/retrieve");
        }
    }


    /**
     * Create a governance definition with the Governance Officer.
     *
     * @param properties its properties
     * @return its GUID
     */
    static String createGovernanceDefinition(GovernanceDefinitionProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return governanceOfficer.create("/governance-definitions", requestBody);
    }


    /**
     * Delete a governance definition and check it has gone.
     *
     * @param definitionGUID definition to delete (ignored if null)
     */
    static void deleteGovernanceDefinition(String definitionGUID)
    {
        if (definitionGUID != null)
        {
            governanceOfficer.call("/governance-definitions/" + definitionGUID + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/governance-definitions/" + definitionGUID + "/retrieve");
        }
    }


    /**
     * Read any element through the Classification Explorer.
     *
     * @param elementGUID element to read
     * @return the element
     */
    static OpenMetadataRootElement readElement(String elementGUID)
    {
        OpenMetadataRootElement element = classificationExplorer.element("/elements/" + elementGUID, new GetRequestBody());

        assertNotNull(element, "element " + elementGUID + " is missing");

        return element;
    }


    /**
     * Return the GUIDs of the elements linked to an element by a relationship type.
     *
     * @param elementGUID starting element
     * @param relationshipTypeName relationship type
     * @return GUIDs of related elements
     */
    static List<String> relatedGUIDs(String elementGUID,
                                     String relationshipTypeName)
    {
        return guids(classificationExplorer.elements("/elements/" + elementGUID + "/by-relationship/" + relationshipTypeName,
                                                     new ResultsRequestBody()));
    }


    /**
     * The GUIDs of a list of elements.
     *
     * @param elements elements
     * @return their GUIDs
     */
    static List<String> guids(List<OpenMetadataRootElement> elements)
    {
        List<String> guids = new ArrayList<>();

        for (OpenMetadataRootElement element : elements)
        {
            guids.add(element.getElementHeader().getGUID());
        }

        return guids;
    }


    /**
     * Find a classification on an element by name.  The element header holds each classification in its own
     * property (impact, confidence, ...) or in one of its lists, so every one of them is checked.
     *
     * @param element element to check
     * @param classificationName name of the classification
     * @return the classification, or null
     */
    static ElementClassification classification(OpenMetadataRootElement element,
                                                String                  classificationName)
    {
        for (ElementClassification classification : allClassifications(element.getElementHeader()))
        {
            if (classificationName.equals(classification.getClassificationName()))
            {
                return classification;
            }
        }

        return null;
    }


    /**
     * Gather all the classifications from an element header.
     *
     * @param header header
     * @return classifications
     */
    static List<ElementClassification> allClassifications(ElementHeader header)
    {
        List<ElementClassification> classifications = new ArrayList<>();

        for (Method method : ElementHeader.class.getMethods())
        {
            if (method.getName().startsWith("get") && (method.getParameterCount() == 0))
            {
                try
                {
                    Object value = method.invoke(header);

                    if (value instanceof ElementClassification classification)
                    {
                        classifications.add(classification);
                    }
                    else if (value instanceof List<?> list)
                    {
                        for (Object item : list)
                        {
                            if (item instanceof ElementClassification classification)
                            {
                                classifications.add(classification);
                            }
                        }
                    }
                }
                catch (ReflectiveOperationException error)
                {
                    throw new IllegalStateException("Could not read " + method.getName() + " from the element header", error);
                }
            }
        }

        return classifications;
    }
}
