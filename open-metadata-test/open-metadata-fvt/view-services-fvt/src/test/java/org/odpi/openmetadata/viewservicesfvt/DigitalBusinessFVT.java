/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.AgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityDependencyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.BusinessSignificantProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DataSharingAgreementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalSupportProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DigitalBusinessFVT covers every request of the Digital Business OMVS: business capability dependencies,
 * digital support for a business capability, the BusinessSignificant classification and the
 * DataSharingAgreement classification.
 * <br>
 * Business capabilities, the supporting element and the agreement are collections, so they are created, read
 * back and deleted with the Collection Manager.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DigitalBusinessFVT
{
    private static final ViewServiceClient digitalBusiness   = new ViewServiceClient(ViewServiceDescription.DIGITAL_BUSINESS);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);

    private static final List<String> collectionsToDelete = new ArrayList<>();

    private static String capabilityGUID           = null;
    private static String supportingCapabilityGUID = null;
    private static String supportingElementGUID    = null;
    private static String agreementGUID            = null;


    /**
     * A business capability that depends on another shows the dependency with its properties until it is
     * detached.
     */
    @Test
    @Order(1)
    void businessCapabilityDependencies()
    {
        capabilityGUID           = createCollection(collectionProperties(new BusinessCapabilityProperties(), "capability"));
        supportingCapabilityGUID = createCollection(collectionProperties(new BusinessCapabilityProperties(), "supporting-capability"));

        BusinessCapabilityDependencyProperties properties = new BusinessCapabilityDependencyProperties();

        properties.setLabel("Relies on");
        properties.setDescription("Ordering relies on payments");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String path = "/business-capabilities/" + capabilityGUID + "/dependencies/" + supportingCapabilityGUID;

        digitalBusiness.call(path + "/attach", requestBody);

        RelatedMetadataElementSummary dependency = RelatedElementChecks.relationship(retrieve(capabilityGUID),
                                                                                     OpenMetadataType.BUSINESS_CAPABILITY_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                     supportingCapabilityGUID);

        assertNotNull(dependency, "the capability does not show the capability it depends on");
        assertEquals("Relies on",
                     assertInstanceOf(BusinessCapabilityDependencyProperties.class, dependency.getRelationshipProperties()).getLabel());

        digitalBusiness.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(capabilityGUID), OpenMetadataType.BUSINESS_CAPABILITY_DEPENDENCY_RELATIONSHIP.typeName, supportingCapabilityGUID),
                    "the capability still shows the detached dependency");
    }


    /**
     * An element that digitally supports a business capability is visible from the capability until it is
     * detached.
     */
    @Test
    @Order(2)
    void digitalSupport()
    {
        supportingElementGUID = createCollection(collectionProperties(new CollectionProperties(), "supporting-element"));

        DigitalSupportProperties properties = new DigitalSupportProperties();

        properties.setLabel("Implements");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(properties);

        String path = "/business-capabilities/" + capabilityGUID + "/digital-support/" + supportingElementGUID;

        digitalBusiness.call(path + "/attach", requestBody);

        assertTrue(RelatedElementChecks.isLinked(retrieve(capabilityGUID), OpenMetadataType.DIGITAL_SUPPORT_RELATIONSHIP.typeName, supportingElementGUID),
                   "the capability does not show the element that supports it");

        digitalBusiness.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertFalse(RelatedElementChecks.isLinked(retrieve(capabilityGUID), OpenMetadataType.DIGITAL_SUPPORT_RELATIONSHIP.typeName, supportingElementGUID),
                    "the capability still shows the detached supporting element");
    }


    /**
     * The BusinessSignificant classification shows on the element with its properties once set, and is gone
     * once cleared.
     */
    @Test
    @Order(3)
    void businessSignificance()
    {
        BusinessSignificantProperties properties = new BusinessSignificantProperties();

        properties.setDescription("Drives revenue");
        properties.setScope("Retail");
        properties.setBusinessCapability(capabilityGUID);

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(properties);

        digitalBusiness.call("/elements/" + supportingElementGUID + "/business-significant", requestBody);

        ElementClassification classification = RelatedElementChecks.classification(retrieve(supportingElementGUID).getElementHeader(),
                                                                                    OpenMetadataType.BUSINESS_SIGNIFICANT_CLASSIFICATION.typeName);

        assertNotNull(classification, "the element was not classified as business significant");
        assertEquals("Drives revenue",
                     assertInstanceOf(BusinessSignificantProperties.class, classification.getClassificationProperties()).getDescription());

        digitalBusiness.call("/elements/" + supportingElementGUID + "/business-significant/remove", new DeleteClassificationRequestBody());

        assertFalse(RelatedElementChecks.classificationNames(retrieve(supportingElementGUID).getElementHeader())
                                        .contains(OpenMetadataType.BUSINESS_SIGNIFICANT_CLASSIFICATION.typeName),
                    "the cleared BusinessSignificant classification is still present");
    }


    /**
     * A refused setBusinessSignificant names the properties class it expects.
     */
    @Test
    @Order(4)
    void businessSignificanceRefusalNamesTheRightProperties()
    {
        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(new DataSharingAgreementProperties());

        JsonNode response = digitalBusiness.postForFailure("/elements/" + supportingElementGUID + "/business-significant", requestBody);

        assertTrue(response.path("exceptionErrorMessage").asText().contains(BusinessSignificantProperties.class.getName()),
                   "the refusal names the wrong properties class: " + response.path("exceptionErrorMessage").asText());
    }


    /**
     * The DataSharingAgreement classification shows on an agreement once set, and is gone once cleared.
     */
    @Test
    @Order(5)
    void dataSharingAgreements()
    {
        agreementGUID = createCollection(collectionProperties(new AgreementProperties(), "agreement"));

        NewClassificationRequestBody requestBody = new NewClassificationRequestBody();

        requestBody.setProperties(new DataSharingAgreementProperties());

        digitalBusiness.call("/agreements/" + agreementGUID + "/data-sharing-agreement", requestBody);

        assertTrue(RelatedElementChecks.classificationNames(retrieve(agreementGUID).getElementHeader())
                                       .contains(OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION.typeName),
                   "the agreement was not classified as a data sharing agreement");

        digitalBusiness.call("/agreements/" + agreementGUID + "/data-sharing-agreement/remove", new DeleteClassificationRequestBody());

        assertFalse(RelatedElementChecks.classificationNames(retrieve(agreementGUID).getElementHeader())
                                        .contains(OpenMetadataType.DATA_SHARING_AGREEMENT_CLASSIFICATION.typeName),
                    "the cleared DataSharingAgreement classification is still present");
    }


    @AfterAll
    static void deleteCollections()
    {
        for (String guid : collectionsToDelete)
        {
            collectionManager.call("/collections/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + guid + "/retrieve");
        }

        collectionsToDelete.clear();
    }


    private static <T extends CollectionProperties> T collectionProperties(T      properties,
                                                                           String name)
    {
        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName(properties.getClass().getSimpleName(), name));
        properties.setDisplayName("Digital business FVT " + name);

        return properties;
    }


    private static String createCollection(CollectionProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        String guid = collectionManager.create("/collections", requestBody);

        collectionsToDelete.add(guid);

        return guid;
    }


    private static OpenMetadataRootElement retrieve(String guid)
    {
        OpenMetadataRootElement element = collectionManager.element("/collections/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "collection " + guid + " is missing");

        return element;
    }
}
