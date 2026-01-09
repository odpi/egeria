/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors;

import org.testng.annotations.Test;

import java.util.Set;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;


/**
 * Verify the EgeriaOpenConnectorDefinition enum contains unique ids with non-null URLs, names and descriptions.
 */
public class EgeriaOpenConnectorDefinitionTest
{
    final Set<Integer> componentIds = new java.util.HashSet<>();
    final Set<String>  connectorTypeGUIDs = new java.util.HashSet<>();
    final Set<String>  connectorQualifiedNames = new java.util.HashSet<>();
    final Set<String>  connectorProviderNames = new java.util.HashSet<>();

    /**
     * Validated the values of the enum.
     */
    @Test public void testAllConnectorDefinitions()
    {
        for (EgeriaOpenConnectorDefinition definition : EgeriaOpenConnectorDefinition.values())
        {
            assertNotNull(definition.getConnectorTypeGUID(), "Connector definition " + definition + " has null connector type GUID.");
            assertNotNull(definition.getConnectorQualifiedName(), "Connector definition " + definition + " has null connector qualified name.");
            assertNotNull(definition.getConnectorDisplayName(), "Connector definition " + definition + " has null connector display name.");
            assertNotNull(definition.getConnectorDescription(), "Connector definition " + definition + " has null connector description.");
            assertNotNull(definition.getConnectorWikiPage(), "Connector definition " + definition + " has null URL.");
            assertNotNull(definition.getConnectorProviderClassName(), "Connector definition " + definition + " has null connector provider class name.");
            assertNotNull(definition.getConnectorDevelopmentStatus(), "Connector definition " + definition + " has null development status.");

            assertFalse(componentIds.contains(definition.getConnectorComponentId()), "Duplicate connector component id: " + definition.getConnectorComponentId() + " discovered in connector definition " + definition + ".");
            componentIds.add(definition.getConnectorComponentId());

            assertFalse(connectorTypeGUIDs.contains(definition.getConnectorTypeGUID()), "Duplicate connector type GUID: " + definition.getConnectorTypeGUID() + " discovered in connector definition " + definition + ".");
            connectorTypeGUIDs.add(definition.getConnectorTypeGUID());

            assertFalse(connectorQualifiedNames.contains(definition.getConnectorQualifiedName()), "Duplicate connector qualified name: " + definition.getConnectorQualifiedName() + " discovered in connector definition " + definition + ".");
            connectorQualifiedNames.add(definition.getConnectorQualifiedName());

            assertFalse(connectorProviderNames.contains(definition.getConnectorProviderClassName()), "Duplicate connector provider name: " + definition.getConnectorProviderClassName() + " discovered in connector definition " + definition + ".");
            connectorProviderNames.add(definition.getConnectorProviderClassName());
        }
    }
}
