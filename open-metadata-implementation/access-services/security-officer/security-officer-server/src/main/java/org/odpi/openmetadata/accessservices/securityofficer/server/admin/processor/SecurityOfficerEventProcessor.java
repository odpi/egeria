/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.processor;

import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.handler.SecurityOfficerHandler;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerEventProcessor.class);

    private OMRSRepositoryConnector enterpriseOMRSRepositoryConnector;
    private OpenMetadataTopicConnector openMetadataTopicConnector;
    private SecurityOfficerHandler securityOfficerHandler;

    public SecurityOfficerEventProcessor(OMRSRepositoryConnector enterpriseOMRSRepositoryConnector,
                                         OpenMetadataTopicConnector openMetadataTopicConnector) {
        this.enterpriseOMRSRepositoryConnector = enterpriseOMRSRepositoryConnector;
        this.openMetadataTopicConnector = openMetadataTopicConnector;

        try {
            securityOfficerHandler = new SecurityOfficerHandler(enterpriseOMRSRepositoryConnector);
        } catch (MetadataServerException e) {
            log.error(e.getErrorMessage());
        }
    }

    public void processClassifiedEntity(EntityDetail entityDetail) {
        log.info("The magic box determines the new security tag assigned to the given entity {}.", entityDetail.getGUID());
    }
}
