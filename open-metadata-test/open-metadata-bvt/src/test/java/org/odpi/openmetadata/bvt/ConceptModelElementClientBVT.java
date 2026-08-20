/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.bvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConceptModelElementClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadAttributeLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptBeadExtensionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.ConceptModelElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.IsAConceptBeadProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.designmodels.TypedByConceptBeadProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * ConceptModelElementClientBVT exercises the create/get/update/find/delete lifecycle of {@link ConceptModelElementClient}, one of the connector
 * context clients, against the running BVT server, together with the concept bead relationships.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class ConceptModelElementClientBVT
{
    @Test
    void conceptModelElementLifecycle() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        ConceptModelElementClient client = connectorContext.getConceptModelElementClient();

        String qualifiedName = "open-metadata-bvt:ConceptModelElement:" + UUID.randomUUID();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        ConceptModelElementProperties createProperties = new ConceptModelElementProperties();
        createProperties.setQualifiedName(qualifiedName);
        createProperties.setDisplayName("BVT ConceptModelElement");
        createProperties.setDescription("Created by the open-metadata-bvt build verification test suite");

        String elementGUID = client.createConceptModelElement(newElementOptions, null, createProperties, null);

        assertNotNull(elementGUID, "createConceptModelElement should return a GUID");

        OpenMetadataRootElement retrievedElement = client.getConceptModelElementByGUID(elementGUID, new GetOptions());

        assertNotNull(retrievedElement, "getConceptModelElementByGUID should find the element that was just created");
        assertEquals(elementGUID, retrievedElement.getElementHeader().getGUID());
        assertEquals(qualifiedName, ((ConceptModelElementProperties) retrievedElement.getProperties()).getQualifiedName());

        ConceptModelElementProperties updateProperties = new ConceptModelElementProperties();
        updateProperties.setQualifiedName(qualifiedName);
        updateProperties.setDisplayName("BVT ConceptModelElement (updated)");

        boolean updateOccurred = client.updateConceptModelElement(elementGUID, new UpdateOptions(), updateProperties);

        assertTrue(updateOccurred, "updateConceptModelElement should report that an update occurred");

        OpenMetadataRootElement updatedElement = client.getConceptModelElementByGUID(elementGUID, new GetOptions());

        assertEquals("BVT ConceptModelElement (updated)", ((ConceptModelElementProperties) updatedElement.getProperties()).getDisplayName());

        List<OpenMetadataRootElement> foundElements = client.findConceptModelElements(qualifiedName, new SearchOptions());

        assertNotNull(foundElements, "findConceptModelElements should return a (possibly empty) list, not null");
        assertTrue(foundElements.stream().anyMatch(element -> elementGUID.equals(element.getElementHeader().getGUID())),
                   "findConceptModelElements should find the element by its qualified name");

        List<OpenMetadataRootElement> namedElements = client.getConceptModelElementsByName(qualifiedName, null);

        assertNotNull(namedElements, "getConceptModelElementsByName should return a (possibly empty) list, not null");

        client.deleteConceptModelElement(elementGUID, new DeleteOptions());

        assertThrows(Exception.class,
                     () -> client.getConceptModelElementByGUID(elementGUID, new GetOptions()),
                     "getConceptModelElementByGUID should no longer find the element after it has been deleted");
    }


    @Test
    void conceptModelRelationships() throws Exception
    {
        ConnectorContextBase       connectorContext = ConnectorContextFactory.newContext();
        ConceptModelElementClient  client           = connectorContext.getConceptModelElementClient();

        NewElementOptions newElementOptions = new NewElementOptions();
        newElementOptions.setIsOwnAnchor(true);

        ConceptModelElementProperties beadOne = new ConceptModelElementProperties();
        beadOne.setTypeName("ConceptBead");
        beadOne.setQualifiedName("open-metadata-bvt:ConceptBead:" + UUID.randomUUID());
        beadOne.setDisplayName("BVT ConceptBead one");

        ConceptModelElementProperties beadTwo = new ConceptModelElementProperties();
        beadTwo.setTypeName("ConceptBead");
        beadTwo.setQualifiedName("open-metadata-bvt:ConceptBead:" + UUID.randomUUID());
        beadTwo.setDisplayName("BVT ConceptBead two");

        String beadOneGUID = client.createConceptModelElement(newElementOptions, null, beadOne, null);
        String beadTwoGUID = client.createConceptModelElement(newElementOptions, null, beadTwo, null);

        client.linkIsAConceptBead(beadOneGUID, beadTwoGUID, new MakeAnchorOptions(), new IsAConceptBeadProperties());
        client.detachIsAConceptBead(beadOneGUID, beadTwoGUID, new DeleteOptions());

        ConceptBeadExtensionProperties extensionProperties = new ConceptBeadExtensionProperties();
        extensionProperties.setRole("BVT extension");

        client.linkConceptBeadExtension(beadOneGUID, beadTwoGUID, new MakeAnchorOptions(), extensionProperties);
        client.detachConceptBeadExtension(beadOneGUID, beadTwoGUID, new DeleteOptions());

        /*
         * ConceptBeadAttributeLink runs from a ConceptBead to a ConceptBeadAttribute, so end 2 has to be an
         * attribute.  ConceptModelElement is the super type of both, so the same client creates it - the
         * subtype comes from the type name carried by the properties.
         */
        ConceptModelElementProperties attributeProperties = new ConceptModelElementProperties();
        attributeProperties.setTypeName("ConceptBeadAttribute");
        attributeProperties.setQualifiedName("open-metadata-bvt:ConceptBeadAttribute:" + UUID.randomUUID());
        attributeProperties.setDisplayName("BVT ConceptBeadAttribute");

        String attributeGUID = client.createConceptModelElement(newElementOptions, null, attributeProperties, null);

        ConceptBeadAttributeLinkProperties attributeLinkProperties = new ConceptBeadAttributeLinkProperties();
        attributeLinkProperties.setPosition(1);

        client.linkConceptBeadAttributeLink(beadOneGUID, attributeGUID, new MakeAnchorOptions(), attributeLinkProperties);
        client.detachConceptBeadAttributeLink(beadOneGUID, attributeGUID, new DeleteOptions());

        client.linkTypedByConceptBead(attributeGUID, beadTwoGUID, new MakeAnchorOptions(), new TypedByConceptBeadProperties());
        client.detachTypedByConceptBead(attributeGUID, beadTwoGUID, new DeleteOptions());

        client.deleteConceptModelElement(attributeGUID, new DeleteOptions());
        client.deleteConceptModelElement(beadOneGUID, new DeleteOptions());
        client.deleteConceptModelElement(beadTwoGUID, new DeleteOptions());
    }
}
