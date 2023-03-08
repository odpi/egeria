/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project */
/* Copyright Contributors to the ODPi Egeria term. */
package org.odpi.openmetadata.accessservices.subjectarea.handlers;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.generichandlers.*;


/**
 * SubjectAreaTermHandler manages config objects from the property server.  It runs server-side in the subject Area
 * OMAS and retrieves configuration information.
 */
public class SubjectAreaConfigHandler extends SubjectAreaHandler {
    private static final String className = SubjectAreaConfigHandler.class.getName();

    /**
     * Construct the Subject Area Config Handler
     * needed to operate within a single server instance.
     *
     * @param genericHandler          generic handler
     * @param maxPageSize             maximum page size
     */
    public SubjectAreaConfigHandler(OpenMetadataAPIGenericHandler genericHandler, int maxPageSize) {
        super(genericHandler, maxPageSize);
    }

    /**
     * Get the subject area configuration.
     * @param userId user id of the caller
     * @return config response
     */
    public SubjectAreaOMASAPIResponse<Config> getConfig(String userId) {
        // TODO check the userid
        SubjectAreaOMASAPIResponse<Config> response = new SubjectAreaOMASAPIResponse<>();
        Config config = new Config();
        config.setMaxPageSize(maxPageSize);
        response.addResult(config);
        return response;
    }
}
