/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.FilterRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.infrastructure.SoftwareServerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.EmbeddedDataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.LiteralDataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.ProcessingVariableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.guids;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.newElement;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.search;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.template;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.updateElement;

/**
 * DataEngineerDataValuesFVT covers the Data Engineer OMVS requests for data values (model 0208): the lifecycle
 * of a DataValue and a LiteralDataValue, and the EmbeddedDataValue, ProcessingVariable and DataValueSource
 * relationships.  Each relationship is uni-link, so it is attached and detached using the unique identifiers
 * of its two ends, and is checked from both of them.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataEngineerDataValuesFVT
{
    private static final ViewServiceClient dataEngineer = new ViewServiceClient(ViewServiceDescription.DATA_ENGINEER);
    private static final ViewServiceClient assetMaker   = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static final List<String> dataValuesToDelete = new ArrayList<>();

    private static String dataValueGUID = null;
    private static String literalGUID   = null;
    private static String processGUID   = null;
    private static String storeGUID     = null;


    /**
     * Create the element that is configured with a data value, and the asset where a data value is stored.
     */
    @BeforeAll
    static void createElements()
    {
        processGUID = createServer("configured-server", "Configured server");
        storeGUID   = createServer("value-store", "Value store");
    }


    /**
     * A data value is created, read back, found by name and by search string, updated, and copied from a template.
     */
    @Test
    @Order(1)
    void dataValueLifecycle()
    {
        DataValueProperties properties = new DataValueProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataValue", "model-weights"));
        properties.setDisplayName("Model weights");
        properties.setDataFormat("parquet");
        properties.setLength(2048);
        properties.setDataType("array<double>");
        properties.setUsage("Initial weights for the model run");
        properties.setFingerprint("a1b2c3");
        properties.setFingerprintAlgorithm("SHA-256");
        properties.setHash(123456789012L);
        properties.setHashAlgorithm("xxHash64");

        dataValueGUID = dataEngineer.create("/data-values", newElement(properties));
        dataValuesToDelete.add(dataValueGUID);

        OpenMetadataRootElement dataValue = dataValue(dataValueGUID);

        assertEquals(OpenMetadataType.DATA_VALUE.typeName, dataValue.getElementHeader().getType().getTypeName());

        DataValueProperties retrieved = assertInstanceOf(DataValueProperties.class, dataValue.getProperties());

        assertEquals("parquet", retrieved.getDataFormat());
        assertEquals(2048, retrieved.getLength());
        assertEquals("array<double>", retrieved.getDataType());
        assertEquals("Initial weights for the model run", retrieved.getUsage());
        assertEquals("a1b2c3", retrieved.getFingerprint());
        assertEquals("SHA-256", retrieved.getFingerprintAlgorithm());
        assertEquals(123456789012L, retrieved.getHash());
        assertEquals("xxHash64", retrieved.getHashAlgorithm());

        FilterRequestBody byName = new FilterRequestBody();

        byName.setFilter(properties.getQualifiedName());

        assertEquals(List.of(dataValueGUID), List.copyOf(guids(dataEngineer.elements("/data-values/by-name", byName))));
        assertTrue(guids(dataEngineer.elements("/data-values/by-search-string", search())).contains(dataValueGUID));

        DataValueProperties update = new DataValueProperties();

        update.setUsage("Weights for every model run");

        assertTrue(dataEngineer.post("/data-values/" + dataValueGUID + "/update", updateElement(update), BooleanResponse.class).getFlag(),
                   "the update did not change the data value");

        DataValueProperties updated = assertInstanceOf(DataValueProperties.class, dataValue(dataValueGUID).getProperties());

        assertEquals("Weights for every model run", updated.getUsage());
        assertEquals("parquet", updated.getDataFormat(), "a merge update should keep the data format");
        assertEquals(123456789012L, updated.getHash(), "a merge update should keep the hash");

        DataValueProperties replacement = new DataValueProperties();

        replacement.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataValue", "model-weights-copy"));

        String copyGUID = dataEngineer.create("/data-values/from-template", template(dataValueGUID, replacement));
        dataValuesToDelete.add(copyGUID);

        assertNotEquals(dataValueGUID, copyGUID);

        DataValueProperties copied = assertInstanceOf(DataValueProperties.class, dataValue(copyGUID).getProperties());

        assertEquals(replacement.getQualifiedName(), copied.getQualifiedName());
        assertEquals("parquet", copied.getDataFormat(), "the copy should take the template's data format");
    }


    /**
     * A literal data value holds its data in the element, as a string, a list and a map.  It is created through
     * the same request as a data value and keeps its own type.
     */
    @Test
    @Order(2)
    void literalDataValue()
    {
        LiteralDataValueProperties properties = new LiteralDataValueProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("LiteralDataValue", "learning-rate"));
        properties.setDisplayName("Learning rate");
        properties.setDataFormat("JSON");
        properties.setDataType("double");
        properties.setDataValue("0.01");
        properties.setDataValueList(List.of("0.01", "0.005"));
        properties.setDataValueMap(Map.of("initial", "0.01", "final", "0.005"));

        literalGUID = dataEngineer.create("/data-values", newElement(properties));
        dataValuesToDelete.add(literalGUID);

        OpenMetadataRootElement literal = dataValue(literalGUID);

        assertEquals(OpenMetadataType.LITERAL_DATA_VALUE.typeName, literal.getElementHeader().getType().getTypeName());

        LiteralDataValueProperties retrieved = assertInstanceOf(LiteralDataValueProperties.class, literal.getProperties());

        assertEquals("JSON", retrieved.getDataFormat());
        assertEquals("double", retrieved.getDataType());
        assertEquals("0.01", retrieved.getDataValue());
        assertEquals(List.of("0.01", "0.005"), retrieved.getDataValueList());
        assertEquals(Map.of("initial", "0.01", "final", "0.005"), retrieved.getDataValueMap());

        LiteralDataValueProperties update = new LiteralDataValueProperties();

        update.setDataValue("0.02");

        dataEngineer.post("/data-values/" + literalGUID + "/update", updateElement(update), BooleanResponse.class);

        LiteralDataValueProperties updated = assertInstanceOf(LiteralDataValueProperties.class, dataValue(literalGUID).getProperties());

        assertEquals("0.02", updated.getDataValue());
        assertEquals(List.of("0.01", "0.005"), updated.getDataValueList(), "a merge update should keep the list");
    }


    /**
     * The literal data value is embedded in the data value at a position.
     */
    @Test
    @Order(3)
    void embeddedDataValue()
    {
        String typeName = OpenMetadataType.EMBEDDED_DATA_VALUE_RELATIONSHIP.typeName;
        String path     = "/data-values/" + dataValueGUID + "/embedded-data-values/" + literalGUID;

        EmbeddedDataValueProperties properties = new EmbeddedDataValueProperties();

        properties.setLabel("learningRate");
        properties.setDescription("The learning rate used with the weights");
        properties.setPosition(1);

        dataEngineer.call(path + "/attach", relationship(properties));

        RelatedMetadataElementSummary fromParent = find(dataValue(dataValueGUID).getEmbeddedDataValues(), literalGUID);

        assertNotNull(fromParent, "the embedded data value is not in the parent's embeddedDataValues");
        assertEquals(typeName, fromParent.getRelationshipHeader().getType().getTypeName());

        EmbeddedDataValueProperties retrieved = assertInstanceOf(EmbeddedDataValueProperties.class, fromParent.getRelationshipProperties());

        assertEquals("learningRate", retrieved.getLabel());
        assertEquals("The learning rate used with the weights", retrieved.getDescription());
        assertEquals(1, retrieved.getPosition());

        assertNotNull(find(dataValue(literalGUID).getParentDataValues(), dataValueGUID),
                      "the parent data value is not in the embedded value's parentDataValues");

        dataEngineer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(dataValue(dataValueGUID), typeName, literalGUID);
        assertNotLinked(dataValue(literalGUID), typeName, dataValueGUID);
    }


    /**
     * The server is configured with the literal data value, passed in a position.
     */
    @Test
    @Order(4)
    void processingVariable()
    {
        String typeName = OpenMetadataType.PROCESSING_VARIABLE_RELATIONSHIP.typeName;
        String path     = "/elements/" + processGUID + "/processing-variables/" + literalGUID;

        ProcessingVariableProperties properties = new ProcessingVariableProperties();

        properties.setLabel("rate");
        properties.setDescription("Second parameter");
        properties.setPosition(2);

        dataEngineer.call(path + "/attach", relationship(properties));

        RelatedMetadataElementSummary fromProcess = find(asset(processGUID).getProcessingVariables(), literalGUID);

        assertNotNull(fromProcess, "the data value is not in the element's processingVariables");
        assertEquals(typeName, fromProcess.getRelationshipHeader().getType().getTypeName());

        ProcessingVariableProperties retrieved = assertInstanceOf(ProcessingVariableProperties.class, fromProcess.getRelationshipProperties());

        assertEquals("rate", retrieved.getLabel());
        assertEquals("Second parameter", retrieved.getDescription());
        assertEquals(2, retrieved.getPosition());

        assertNotNull(find(dataValue(literalGUID).getDataValueUses(), processGUID),
                      "the element is not in the data value's dataValueUses");

        dataEngineer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(asset(processGUID), typeName, literalGUID);
        assertNotLinked(dataValue(literalGUID), typeName, processGUID);
    }


    /**
     * The data value is stored in a digital resource that is described by an asset.
     */
    @Test
    @Order(5)
    void dataValueSource()
    {
        String typeName = OpenMetadataType.DATA_VALUE_SOURCE_RELATIONSHIP.typeName;
        String path     = "/data-values/" + dataValueGUID + "/data-value-sources/" + storeGUID;

        DataValueSourceProperties properties = new DataValueSourceProperties();

        properties.setLabel("stored in");
        properties.setDescription("The store holding the weights");

        dataEngineer.call(path + "/attach", relationship(properties));

        RelatedMetadataElementSummary fromDataValue = find(dataValue(dataValueGUID).getDataValueSources(), storeGUID);

        assertNotNull(fromDataValue, "the asset is not in the data value's dataValueSources");
        assertEquals(typeName, fromDataValue.getRelationshipHeader().getType().getTypeName());

        DataValueSourceProperties retrieved = assertInstanceOf(DataValueSourceProperties.class, fromDataValue.getRelationshipProperties());

        assertEquals("stored in", retrieved.getLabel());
        assertEquals("The store holding the weights", retrieved.getDescription());

        assertNotNull(find(asset(storeGUID).getProvidesDataValues(), dataValueGUID),
                      "the data value is not in the asset's providesDataValues");

        dataEngineer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(dataValue(dataValueGUID), typeName, storeGUID);
        assertNotLinked(asset(storeGUID), typeName, dataValueGUID);
    }


    /**
     * Delete everything this class created.
     */
    @AfterAll
    static void deleteElements()
    {
        for (String guid : dataValuesToDelete)
        {
            dataEngineer.call("/data-values/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(dataEngineer, "/data-values/" + guid + "/retrieve");
        }

        for (String guid : new String[]{processGUID, storeGUID})
        {
            if (guid != null)
            {
                assetMaker.call("/assets/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + guid + "/retrieve");
            }
        }
    }


    private static String createServer(String name,
                                       String displayName)
    {
        SoftwareServerProperties properties = new SoftwareServerProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SoftwareServer", name));
        properties.setDisplayName(displayName);

        return assetMaker.create("/assets", newElement(properties));
    }


    private static NewRelationshipRequestBody relationship(RelationshipBeanProperties properties)
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        return requestBody;
    }


    /**
     * Return the summary for the related element with the GUID, or null.
     */
    private static RelatedMetadataElementSummary find(List<RelatedMetadataElementSummary> summaries,
                                                      String                              relatedGUID)
    {
        if (summaries != null)
        {
            for (RelatedMetadataElementSummary summary : summaries)
            {
                if (relatedGUID.equals(summary.getRelatedElement().getElementHeader().getGUID()))
                {
                    return summary;
                }
            }
        }

        return null;
    }


    private static OpenMetadataRootElement dataValue(String guid)
    {
        OpenMetadataRootElement element = dataEngineer.element("/data-values/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "data value " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement asset(String guid)
    {
        OpenMetadataRootElement element = assetMaker.element("/assets/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "asset " + guid + " is missing");

        return element;
    }
}
