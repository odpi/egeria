/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SubjectAreaFVT covers every request of the Subject Area OMVS: two subject areas (created and deleted with the
 * Collection Manager requests that the Subject Area service's URL marker also serves) are nested with a
 * SubjectAreaHierarchy relationship, which is visible from both ends, and then detached.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SubjectAreaFVT
{
    private static final ViewServiceClient subjectArea = new ViewServiceClient(ViewServiceDescription.SUBJECT_AREA);

    private static String parentGUID = null;
    private static String nestedGUID = null;


    @Test
    @Order(1)
    void createSubjectAreas()
    {
        parentGUID = createSubjectArea("Customer", 1);
        nestedGUID = createSubjectArea("Customer contact details", 1);

        SubjectAreaProperties properties = assertInstanceOf(SubjectAreaProperties.class,
                                                            ClassificationExplorerFvtFixtures.readElement(parentGUID).getProperties());

        assertEquals("Customer", properties.getDisplayName());
        assertEquals("All information about customers", properties.getScope());
    }


    /**
     * linkSubjectAreas nests one subject area in the other; detachSubjectAreas removes the nesting.
     */
    @Test
    @Order(2)
    void nestAndDetachSubjectAreas()
    {
        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();

        requestBody.setProperties(new SubjectAreaHierarchyProperties());

        String path = "/subject-areas/" + parentGUID + "/subject-area-hierarchies/" + nestedGUID;

        subjectArea.call(path + "/attach", requestBody);

        assertEquals(List.of(nestedGUID),
                     ClassificationExplorerFvtFixtures.relatedGUIDs(parentGUID, OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName),
                     "the nested subject area is not linked to its parent");
        assertEquals(List.of(parentGUID),
                     ClassificationExplorerFvtFixtures.relatedGUIDs(nestedGUID, OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName),
                     "the parent subject area is not visible from the nested one");

        subjectArea.call(path + "/detach", new DeleteRelationshipRequestBody());

        assertTrue(ClassificationExplorerFvtFixtures.relatedGUIDs(parentGUID, OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName).isEmpty(),
                   "the subject areas are still nested after detachSubjectAreas");
    }


    @AfterAll
    static void deleteSubjectAreas()
    {
        for (String guid : new String[]{nestedGUID, parentGUID})
        {
            if (guid != null)
            {
                subjectArea.call("/collections/" + guid + "/delete", new DeleteElementRequestBody());
                ViewServicesFvtTestSupport.assertGone(subjectArea, "/collections/" + guid + "/retrieve");
            }
        }
    }


    private static String createSubjectArea(String displayName,
                                            int    domainIdentifier)
    {
        SubjectAreaProperties properties = new SubjectAreaProperties();

        properties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("SubjectArea", displayName));
        properties.setDisplayName(displayName);
        properties.setScope("All information about " + displayName.toLowerCase() + "s");
        properties.setDomainIdentifier(domainIdentifier);

        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return subjectArea.create("/collections", requestBody);
    }
}
