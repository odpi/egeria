/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.beans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.InformationSupplyChainElement;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * The getters of the information supply chain graphs start with "ISC", which Jackson would otherwise turn into
 * "iscimplementationMermaidGraph" and so on.  Callers find the graphs by their property names, so the JSON must
 * use those names, and read them back.
 */
public class InformationSupplyChainElementJsonTest
{
    @Test
    public void graphPropertiesUseTheirPropertyNames() throws Exception
    {
        InformationSupplyChainElement element = new InformationSupplyChainElement();

        element.setISCImplementationMermaidGraph("implementation");
        element.setISCDataMeshMermaidGraph("mesh");
        element.setISCSystemFabricMermaidGraph("system");
        element.setISCDataFabricMermaidGraph("fabric");
        element.setISCDataMappingMermaidGraph("mapping");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode     json   = mapper.readTree(mapper.writeValueAsString(element));

        assertEquals(json.size(), 6, "only the type discriminator and the five graphs should be written: " + json);
        assertEquals(json.get("class").asText(), "InformationSupplyChainElement");
        assertEquals(json.get("iscImplementationMermaidGraph").asText(), "implementation");
        assertEquals(json.get("iscDataMeshMermaidGraph").asText(), "mesh");
        assertEquals(json.get("iscSystemFabricMermaidGraph").asText(), "system");
        assertEquals(json.get("iscDataFabricMermaidGraph").asText(), "fabric");
        assertEquals(json.get("iscDataMappingMermaidGraph").asText(), "mapping");

        assertEquals(mapper.readValue(mapper.writeValueAsString(element), InformationSupplyChainElement.class), element);
    }
}
