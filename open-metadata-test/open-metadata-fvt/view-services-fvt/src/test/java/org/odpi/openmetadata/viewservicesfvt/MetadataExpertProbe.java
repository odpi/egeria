/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.RelatedMetadataElementListResponse;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * MetadataExpertProbe uses the Metadata Expert's generic relationship query to check, independently of the
 * service under test, which elements are linked to an element by a particular type of relationship.  This lets
 * a test confirm that an attach request really created a relationship of the documented type (and a detach
 * really removed it) whichever end of the relationship the element is at, and whether or not the service's own
 * retrieve request reports that relationship.
 */
class MetadataExpertProbe
{
    private static final ViewServiceClient metadataExpert = new ViewServiceClient(ViewServiceDescription.METADATA_EXPERT);


    /**
     * Return the unique identifiers of the elements linked to an element by relationships of a type.
     *
     * @param elementGUID element to start from
     * @param relationshipTypeName type of relationship
     * @return linked elements' GUIDs (may be empty)
     */
    static List<String> linkedGUIDs(String elementGUID,
                                    String relationshipTypeName)
    {
        RelatedMetadataElementListResponse response = metadataExpert.post("/related-elements/" + elementGUID + "/type/" + relationshipTypeName,
                                                                           new ResultsRequestBody(),
                                                                           RelatedMetadataElementListResponse.class);
        List<String> guids = new ArrayList<>();

        if ((response.getRelatedElementList() != null) && (response.getRelatedElementList().getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : response.getRelatedElementList().getElementList())
            {
                guids.add(relatedMetadataElement.getElement().getElementGUID());
            }
        }

        return guids;
    }


    /**
     * Return the relationship GUIDs linking an element to others by relationships of a type.
     *
     * @param elementGUID element to start from
     * @param relationshipTypeName type of relationship
     * @return relationship GUIDs (may be empty)
     */
    static List<String> relationshipGUIDs(String elementGUID,
                                          String relationshipTypeName)
    {
        RelatedMetadataElementListResponse response = metadataExpert.post("/related-elements/" + elementGUID + "/type/" + relationshipTypeName,
                                                                           new ResultsRequestBody(),
                                                                           RelatedMetadataElementListResponse.class);
        List<String> guids = new ArrayList<>();

        if ((response.getRelatedElementList() != null) && (response.getRelatedElementList().getElementList() != null))
        {
            for (RelatedMetadataElement relatedMetadataElement : response.getRelatedElementList().getElementList())
            {
                guids.add(relatedMetadataElement.getRelationshipGUID());
            }
        }

        return guids;
    }


    /**
     * Return a string property of a relationship.
     *
     * @param relationshipGUID relationship to read
     * @param propertyName name of the property
     * @return its value, or null if it is not set
     */
    static String relationshipStringProperty(String relationshipGUID,
                                             String propertyName)
    {
        OpenMetadataRelationship relationship = metadataExpert.post("/relationships/by-guid/" + relationshipGUID,
                                                                    new GetRequestBody(),
                                                                    OpenMetadataRelationshipResponse.class).getElement();

        assertNotNull(relationship, "relationship " + relationshipGUID + " is missing");

        return new PropertyHelper().getStringProperty("MetadataExpertProbe",
                                                      propertyName,
                                                      relationship.getRelationshipProperties(),
                                                      "relationshipStringProperty");
    }
}
