/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;

public class CategoryMapper extends MainObjectMapper {

    /**
     * Sets the basic criteria to use for mapping between an IGC 'term' object and an OMRS 'GlossaryTerm' object.
     *
     * @param category the IGC 'category' object
     * @param igcomrsRepositoryConnector the IGC repository connector to use for retrieving any additional info required
     * @param userId the userId of the user doing any further detailed information retrievals (currently unused)
     */
    public CategoryMapper(MainObject category, IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(category, igcomrsRepositoryConnector, userId, "Category");

        // Then define the types this Mapper handles
        igcType = "category";               // the IGC type name
        omrsType = "GlossaryCategory";      // the OMRS type name

        // The list of properties that should be mapped (IGC property <-> OMRS property)
        PROPERTIES.put("name", "displayName");
        PROPERTIES.put("short_description", "description");

        // The list of relationships that should be mapped (IGC types <-> OMRS types; IGC property <-> OMRS Relationship)
        RELATIONSHIPS.put(
                "parent_category",
                "CategoryHierarchyLink",
                "subcategories",
                "superCategory"
        );
        RELATIONSHIPS.put(
                "subcategories",
                "CategoryHierarchyLink",
                "superCategory",
                "subcategories"
        );
        RELATIONSHIPS.put(
                "terms",
                "TermCategorization",
                "categories",
                "terms"
        );

    }

    /**
     * No classifications implemented for Categories.
     */
    protected void getMappedClassifications() {}

}
