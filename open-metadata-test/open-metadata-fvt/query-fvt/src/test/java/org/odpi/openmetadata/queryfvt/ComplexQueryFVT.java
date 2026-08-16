/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.queryfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.CollectionClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ClassificationCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MatchCriteria;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyComparisonOperator;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyCondition;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchClassifications;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ComplexQueryFVT exercises richer versions of the queries offered by {@code OpenMetadataStore}: nested
 * AND/OR property conditions using a variety of comparison operators, and classification-based searches
 * that combine a classification name with a property condition on one of the classification's own
 * properties (as opposed to a property of the classified element itself).
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ComplexQueryFVT
{
    @Test
    void nestedAndOrPropertyConditions() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String       runPrefix    = QueryFvtTestSupport.newQualifiedName("ComplexNested");
        List<String> createdGUIDs = new ArrayList<>();

        try
        {
            // Three "purposes": Alpha, Beta, Gamma - two elements each.
            String[] purposes = {"Alpha", "Beta", "Gamma"};
            List<String> alphaOrBetaGUIDs = new ArrayList<>();

            for (String purpose : purposes)
            {
                for (int i = 0; i < 2; i++)
                {
                    NewElementOptions newElementOptions = new NewElementOptions();
                    newElementOptions.setIsOwnAnchor(true);

                    CollectionProperties createProperties = new CollectionProperties();
                    createProperties.setQualifiedName(runPrefix + ":" + purpose + ":" + i);
                    createProperties.setDisplayName("query-fvt Complex Collection " + purpose + " " + i);
                    createProperties.setPurpose(purpose);

                    String guid = collectionClient.createCollection(newElementOptions, null, createProperties, null);

                    createdGUIDs.add(guid);

                    if (! "Gamma".equals(purpose))
                    {
                        alphaOrBetaGUIDs.add(guid);
                    }
                }
            }

            // (qualifiedName STARTS_WITH runPrefix) AND (purpose EQ "Alpha" OR purpose EQ "Beta")
            SearchProperties innerOr = new SearchProperties();
            innerOr.setMatchCriteria(MatchCriteria.ANY);

            List<PropertyCondition> orConditions = propertyHelper.addStringProperty(null,
                                                                                     OpenMetadataProperty.PURPOSE.name,
                                                                                     "Alpha",
                                                                                     PropertyComparisonOperator.EQ);
            orConditions = propertyHelper.addStringProperty(orConditions,
                                                             OpenMetadataProperty.PURPOSE.name,
                                                             "Beta",
                                                             PropertyComparisonOperator.EQ);
            innerOr.setConditions(orConditions);

            PropertyCondition nestedCondition = new PropertyCondition();
            nestedCondition.setNestedConditions(innerOr);

            List<PropertyCondition> outerConditions = propertyHelper.addStringProperty(null,
                                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                        runPrefix,
                                                                                        PropertyComparisonOperator.STARTS_WITH);
            outerConditions.add(nestedCondition);

            SearchProperties outerAnd = new SearchProperties();
            outerAnd.setMatchCriteria(MatchCriteria.ALL);
            outerAnd.setConditions(outerConditions);

            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            queryOptions.setPageSize(20);

            List<OpenMetadataElement> results = openMetadataStore.findMetadataElements(outerAnd, null, queryOptions);
            Set<String>               resultGUIDs = results.stream().map(OpenMetadataElement::getElementGUID).collect(Collectors.toSet());

            assertEquals(4, results.size(), "Only the four Alpha/Beta collections should match, not the two Gamma ones");
            assertEquals(new HashSet<>(alphaOrBetaGUIDs), resultGUIDs);
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }


    @Test
    void classificationPropertyValueQuery() throws Exception
    {
        ConnectorContextBase connectorContext  = ConnectorContextFactory.newContext();
        CollectionClient      collectionClient  = connectorContext.getCollectionClient();
        OpenMetadataStore      openMetadataStore = connectorContext.getOpenMetadataStore();
        PropertyHelper         propertyHelper    = new PropertyHelper();

        String       runPrefix    = QueryFvtTestSupport.newQualifiedName("ComplexClassification");
        List<String> createdGUIDs = new ArrayList<>();
        List<String> highConfidentialityGUIDs = new ArrayList<>();

        try
        {
            // Three elements at confidentiality level 1, three at level 4 - the classification search
            // below should only find the level-4 ones.
            int[] levels = {1, 1, 1, 4, 4, 4};

            for (int i = 0; i < levels.length; i++)
            {
                NewElementOptions newElementOptions = new NewElementOptions();
                newElementOptions.setIsOwnAnchor(true);

                CollectionProperties createProperties = new CollectionProperties();
                createProperties.setQualifiedName(runPrefix + ":" + i);
                createProperties.setDisplayName("query-fvt Complex Classification Collection " + i);

                String guid = collectionClient.createCollection(newElementOptions, null, createProperties, null);
                createdGUIDs.add(guid);

                ElementProperties confidentialityProperties = propertyHelper.addIntProperty(null,
                                                                                             OpenMetadataProperty.CONFIDENTIALITY_LEVEL.name,
                                                                                             levels[i]);

                openMetadataStore.classifyMetadataElementInStore(guid,
                                                                  OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                                                  new MetadataSourceOptions(),
                                                                  new NewElementProperties(confidentialityProperties));

                if (levels[i] > 2)
                {
                    highConfidentialityGUIDs.add(guid);
                }
            }

            SearchProperties elementSearchProperties = new SearchProperties();
            elementSearchProperties.setConditions(propertyHelper.addStringProperty(null,
                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                    runPrefix,
                                                                                    PropertyComparisonOperator.STARTS_WITH));

            SearchProperties classificationPropertyConditions = new SearchProperties();
            classificationPropertyConditions.setConditions(propertyHelper.addIntProperty(null,
                                                                                          OpenMetadataProperty.CONFIDENTIALITY_LEVEL.name,
                                                                                          2,
                                                                                          PropertyComparisonOperator.GT));

            ClassificationCondition classificationCondition = new ClassificationCondition();
            classificationCondition.setName(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName);
            classificationCondition.setSearchProperties(classificationPropertyConditions);

            SearchClassifications searchClassifications = new SearchClassifications();
            searchClassifications.setConditions(List.of(classificationCondition));

            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setMetadataElementTypeName(OpenMetadataType.COLLECTION.typeName);
            queryOptions.setPageSize(20);

            List<OpenMetadataElement> results = openMetadataStore.findMetadataElements(elementSearchProperties, searchClassifications, queryOptions);
            Set<String>               resultGUIDs = results.stream().map(OpenMetadataElement::getElementGUID).collect(Collectors.toSet());

            assertEquals(3, results.size(), "Only the three confidentialityLevel=4 collections should match a GT 2 condition");
            assertEquals(new HashSet<>(highConfidentialityGUIDs), resultGUIDs);
        }
        finally
        {
            for (String guid : createdGUIDs)
            {
                QueryFvtTestSupport.purgeElement(openMetadataStore, guid);
            }
        }
    }
}
