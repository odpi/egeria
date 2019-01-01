/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.mapping.classifications;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Identity;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SpineObjectMapper extends ClassificationMapping {

    private static final Logger log = LoggerFactory.getLogger(SpineObjectMapper.class);

    private static class Singleton {
        private static final SpineObjectMapper INSTANCE = new SpineObjectMapper();
    }
    public static SpineObjectMapper getInstance() {
        return SpineObjectMapper.Singleton.INSTANCE;
    }

    private SpineObjectMapper() {
        super(
                "term",
                "assigned_to_terms",
                "SpineObject"
        );
    }

    /**
     * Implements the SpineObject classification for IGC 'term' assets. Any term with a "Spine Objects" ancestor in
     * its category_path will be considered to be a Spine Object (and therefore be given a SpineObject classification).
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

        Identity termIdentity = fromIgcObject.getIdentity(igcomrsRepositoryConnector.getIGCRestClient());
        Identity catIdentity = termIdentity.getParentIdentity();

        if (catIdentity.toString().endsWith("Spine Objects")) {

            try {
                Classification classification = getMappedClassification(
                        igcomrsRepositoryConnector,
                        "SpineObject",
                        "GlossaryTerm",
                        null,
                        fromIgcObject,
                        userId
                );
                classifications.add(classification);
            } catch (RepositoryErrorException e) {
                log.error("Unable to map SpineObject classification.", e);
            }

        }

    }

}
