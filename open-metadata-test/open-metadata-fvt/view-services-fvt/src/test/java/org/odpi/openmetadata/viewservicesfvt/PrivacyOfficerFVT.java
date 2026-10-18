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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.collections.CollectionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingDescriptionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingPurposeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingSpecificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DataProcessingTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.DetailedProcessingActionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing.PermittedProcessingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorGovernanceFvtSupport.assertNotLinked;
import static org.odpi.openmetadata.viewservicesfvt.ActorManagerProfilesFVT.newElement;

/**
 * PrivacyOfficerFVT covers every request of the Privacy Officer OMVS, which links the elements that describe
 * how personal data may be processed: the processing permitted for a purpose, the target of a processing
 * action, the specification an element follows, and the breakdown of a processing action into detailed steps.
 * <br>
 * The purpose is a governance definition, created with Governance Officer; the descriptions, actions and the
 * data they process are collections, created with Collection Manager.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PrivacyOfficerFVT
{
    private static final ViewServiceClient privacyOfficer     = new ViewServiceClient(ViewServiceDescription.PRIVACY_OFFICER);
    private static final ViewServiceClient governanceOfficer  = new ViewServiceClient(ViewServiceDescription.GOVERNANCE_OFFICER);
    private static final ViewServiceClient collectionManager  = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);

    private static String purposeGUID      = null;
    private static String descriptionGUID  = null;
    private static String actionGUID       = null;
    private static String detailActionGUID = null;
    private static String dataGUID         = null;


    /**
     * Create the purpose, the processing description, two processing actions and the data they process.
     */
    @BeforeAll
    static void createElements()
    {
        DataProcessingPurposeProperties purposeProperties = new DataProcessingPurposeProperties();

        purposeProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataProcessingPurpose", "order-fulfilment"));
        purposeProperties.setDisplayName("Order fulfilment");

        purposeGUID = governanceOfficer.create("/governance-definitions", newElement(purposeProperties));

        DataProcessingDescriptionProperties descriptionProperties = new DataProcessingDescriptionProperties();

        descriptionProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataProcessingDescription", "ship-orders"));
        descriptionProperties.setDisplayName("Ship orders");

        descriptionGUID = collectionManager.create("/collections", newElement(descriptionProperties));

        actionGUID       = action("read-address");
        detailActionGUID = action("print-label");

        CollectionProperties dataProperties = new CollectionProperties();

        dataProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("Collection", "customer-addresses"));
        dataProperties.setDisplayName("Customer addresses");

        dataGUID = collectionManager.create("/collections", newElement(dataProperties));
    }


    /**
     * The description is the processing permitted for the purpose.
     */
    @Test
    @Order(1)
    void permittedProcessing()
    {
        linkAndUnlink("/data-processing-purposes/" + purposeGUID + "/permitted-processing/" + descriptionGUID,
                      new PermittedProcessingProperties(),
                      purposeGUID,
                      PrivacyOfficerFVT::governanceDefinition,
                      descriptionGUID,
                      OpenMetadataType.PERMITTED_PROCESSING_RELATIONSHIP.typeName);
    }


    /**
     * The customer addresses are the target of the read-address action.
     */
    @Test
    @Order(2)
    void dataProcessingTarget()
    {
        linkAndUnlink("/data-processing-actions/" + actionGUID + "/targets/" + dataGUID,
                      new DataProcessingTargetProperties(),
                      actionGUID,
                      PrivacyOfficerFVT::collection,
                      dataGUID,
                      OpenMetadataType.DATA_PROCESSING_TARGET_RELATIONSHIP.typeName);
    }


    /**
     * The customer addresses are processed as the description specifies.
     */
    @Test
    @Order(3)
    void dataProcessingSpecification()
    {
        linkAndUnlink("/elements/" + dataGUID + "/data-processing-specifications/" + descriptionGUID,
                      new DataProcessingSpecificationProperties(),
                      dataGUID,
                      PrivacyOfficerFVT::collection,
                      descriptionGUID,
                      OpenMetadataType.DATA_PROCESSING_SPECIFICATION_RELATIONSHIP.typeName);
    }


    /**
     * Printing the label is a detailed step of reading the address.
     */
    @Test
    @Order(4)
    void detailedProcessingAction()
    {
        linkAndUnlink("/data-processing-actions/" + actionGUID + "/detailed-processing-actions/" + detailActionGUID,
                      new DetailedProcessingActionProperties(),
                      actionGUID,
                      PrivacyOfficerFVT::collection,
                      detailActionGUID,
                      OpenMetadataType.DETAILED_PROCESSING_ACTION_RELATIONSHIP.typeName);
    }


    /**
     * Delete everything this class created.
     */
    @AfterAll
    static void deleteElements()
    {
        governanceOfficer.call("/governance-definitions/" + purposeGUID + "/delete", new DeleteElementRequestBody());
        ViewServicesFvtTestSupport.assertGone(governanceOfficer, "/governance-definitions/" + purposeGUID + "/retrieve");

        for (String guid : new String[]{descriptionGUID, actionGUID, detailActionGUID, dataGUID})
        {
            collectionManager.call("/collections/" + guid + "/delete", new DeleteElementRequestBody());
            ViewServicesFvtTestSupport.assertGone(collectionManager, "/collections/" + guid + "/retrieve");
        }
    }


    /**
     * Attach a relationship with a label, check it (and its label) from end 1 and its presence from end 2, then
     * detach it and check it has gone from both ends.
     */
    private static void linkAndUnlink(String                                          path,
                                      LabeledRelationshipProperties                   properties,
                                      String                                          end1GUID,
                                      Function<String, OpenMetadataRootElement>       retrieveEnd1,
                                      String                                          end2GUID,
                                      String                                          relationshipTypeName)
    {
        properties.setLabel("fvt-" + relationshipTypeName);
        properties.setDescription("Linked by " + PrivacyOfficerFVT.class.getSimpleName());

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(properties);

        privacyOfficer.call(path + "/attach", link);

        OpenMetadataRootElement end1 = retrieveEnd1.apply(end1GUID);

        assertLinked(end1, relationshipTypeName, end2GUID);
        assertLinked(collection(end2GUID), relationshipTypeName, end1GUID);

        RelatedMetadataElementSummary summary = ActorGovernanceFvtSupport.relationshipTo(end1, relationshipTypeName, end2GUID);

        assertTrue(summary.getRelationshipProperties() instanceof LabeledRelationshipProperties,
                   relationshipTypeName + " relationship has no properties");
        assertEquals(properties.getLabel(), ((LabeledRelationshipProperties) summary.getRelationshipProperties()).getLabel());

        privacyOfficer.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertNotLinked(retrieveEnd1.apply(end1GUID), relationshipTypeName, end2GUID);
        assertNotLinked(collection(end2GUID), relationshipTypeName, end1GUID);
    }


    private static String action(String name)
    {
        DataProcessingActionProperties properties = new DataProcessingActionProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("DataProcessingAction", name));
        properties.setDisplayName(name);

        return collectionManager.create("/collections", newElement(properties));
    }


    private static OpenMetadataRootElement collection(String guid)
    {
        OpenMetadataRootElement element = collectionManager.element("/collections/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "collection " + guid + " is missing");

        return element;
    }


    private static OpenMetadataRootElement governanceDefinition(String guid)
    {
        OpenMetadataRootElement element = governanceOfficer.element("/governance-definitions/" + guid + "/retrieve", new GetRequestBody());

        assertNotNull(element, "governance definition " + guid + " is missing");

        return element;
    }
}
