/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.entities.EntityMapping;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Singleton defining the mapping to the OMRS "SubjectArea" classification.
 */
public class SubjectAreaMapper extends ClassificationMapping {

    private static final Logger log = LoggerFactory.getLogger(SubjectAreaMapper.class);

    private static class Singleton {
        private static final SubjectAreaMapper INSTANCE = new SubjectAreaMapper();
    }
    public static SubjectAreaMapper getInstance() {
        return SubjectAreaMapper.Singleton.INSTANCE;
    }

    private SubjectAreaMapper() {
        super(
                "category",
                "category_path",
                "SubjectArea"
        );
    }

    /**
     * Implements the SubjectArea classification for IGC 'category' assets. If a category comes
     * under any higher-level 'Subject Areas' category, such a category should be treated as subject area
     * and therefore will be mapped to a "SubjectArea" classification in OMRS.
     *
     * @param igcomrsRepositoryConnector
     * @param classifications
     * @param fromIgcObject
     * @param userId
     */
    @Override
    public void addMappedOMRSClassifications(IGCOMRSRepositoryConnector igcomrsRepositoryConnector,
                                             List<Classification> classifications,
                                             Reference fromIgcObject,
                                             String userId) {

        IGCRestClient igcRestClient = igcomrsRepositoryConnector.getIGCRestClient();

        // Retrieve all ancestral category relationships from this IGC object
        ReferenceList categoryPath = (ReferenceList) fromIgcObject.getPropertyByName("category_path");

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
                        EntityMapping.getPrimitivePropertyValue(fromIgcObject.getName())
                );
                try {
                    Classification classification = getMappedClassification(
                            igcomrsRepositoryConnector,
                            "SubjectArea",
                            "GlossaryCategory",
                            classificationProperties,
                            fromIgcObject,
                            userId
                    );
                    classifications.add(classification);
                } catch (RepositoryErrorException e) {
                    log.error("Unable to map classification.", e);
                }

            }
        }

    }

}
