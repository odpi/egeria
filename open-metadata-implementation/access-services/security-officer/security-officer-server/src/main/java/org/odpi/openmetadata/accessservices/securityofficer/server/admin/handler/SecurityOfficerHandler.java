/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */

/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.handler;

import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.errorcode.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.exceptions.MetadataServerException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityOfficerHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerHandler.class);
    private OMRSMetadataCollection metadataCollection;

    /**
     * Construct the connection handler with a link to the property handlers's connector and this access service's
     * official name.
     *
     * @param repositoryConnector - connector to the property handlers.
     * @throws MetadataServerException - there is a problem retrieving information from the metadata server
     */
    public SecurityOfficerHandler(OMRSRepositoryConnector repositoryConnector) throws MetadataServerException {
        final String methodName = "SecurityOfficerHandler";

        if (repositoryConnector != null) {
            try {
                this.metadataCollection = repositoryConnector.getMetadataCollection();
            } catch (RepositoryErrorException e) {
                SecurityOfficerErrorCode errorCode = SecurityOfficerErrorCode.NO_METADATA_COLLECTION;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

                throw new MetadataServerException(errorCode.getHttpErrorCode(),
                        this.getClass().getName(),
                        methodName,
                        errorMessage,
                        errorCode.getSystemAction(),
                        errorCode.getUserAction());

            }
        }
    }
}
