/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.CategoryHierarchyLinkMapper;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.relationships.TermCategorizationMapper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlossaryCategoryMapper extends ReferenceableMapper {

    private static final Logger log = LoggerFactory.getLogger(GlossaryCategoryMapper.class);

    private static final String T_GLOSSARY_CATEGORY = "GlossaryCategory";
    private static final String C_SUBJECT_AREA = "SubjectArea";

    public GlossaryCategoryMapper(IGCOMRSRepositoryConnector igcomrsRepositoryConnector, String userId) {

        // Start by calling the superclass's constructor to initialise the Mapper
        super(
                igcomrsRepositoryConnector,
                "category",
                "Category",
                T_GLOSSARY_CATEGORY,
                userId
        );

        // The list of properties that should be mapped
        addSimplePropertyMapping("name", "displayName");
        addSimplePropertyMapping("short_description", "description");

        // The classes to use for mapping any relationships
        addRelationshipMapper(CategoryHierarchyLinkMapper.getInstance());
        addRelationshipMapper(TermCategorizationMapper.getInstance());

        addComplexIgcClassification("category_path");
        addComplexOmrsClassification(C_SUBJECT_AREA);

    }

    /**
     * We implement this method to apply any classifications -- since IGC itself doesn't have a "Classification"
     * asset type, we need to apply our own translation between how we're using other IGC asset types and the
     * Classification(s) we want them to represent in OMRS.
     * <br><br>
     * In this example of IGC 'category's, we've used the 'category_path' relationship to see if a category comes
     * under any higher-level 'Subject Areas' category, as such a higher-level category will be used to represent that
     * all sub-categories under it should be treated as subject areas and therefore will be mapped to a
     * "SubjectArea" classification in OMRS.
     */
    @Override
    protected void getMappedClassifications() {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all ancestral category relationships from this IGC object
        ReferenceList categoryPath = (ReferenceList) igcEntity.getPropertyByName("category_path");

        // Only need to continue if there are any parent categories in the path
        if (categoryPath != null) {
            categoryPath.getAllPages(igcRestClient);

            boolean isSubjectArea = false;

            // For each such relationship:
            for (Reference category : categoryPath.getItems()) {
                String categoryName = category.getName();
                // As soon as we find one that starts with Subject Area we can short-circuit out
                if (categoryName != null && categoryName.startsWith("Subject Area")) {
                    isSubjectArea = true;
                    break;
                }
            }

            if (isSubjectArea) {

                InstanceProperties classificationProperties = new InstanceProperties();
                classificationProperties.setProperty(
                        "name",
                        getPrimitivePropertyValue(igcEntity.getName())
                );
                try {
                    Classification classification = getMappedClassification(
                            C_SUBJECT_AREA,
                            T_GLOSSARY_CATEGORY,
                            classificationProperties
                    );
                    omrsClassifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to map classification.", e);
                }

            }
        }

    }

}
