/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea;

import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfig;
import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.configs.SubjectAreaConfigClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.nodes.SubjectAreaNodeClients;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraph;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaGraphClient;
import org.odpi.openmetadata.accessservices.subjectarea.client.relationships.SubjectAreaRelationshipClients;

/**
 * The SubjectArea Open Metadata Access Service (OMAS).
 */
public interface SubjectArea {

    SubjectAreaNodeClients nodeClients();

    /**
     * Get the subject area relationship API class - use this class to issue relationship calls.
     *
     * @return subject area relationship API class
     */
    SubjectAreaRelationshipClients relationshipClients();

    /**
     * Get the subject area config API class - use this class to issue config calls.
     *
     * @return subject area config API class
     */
    SubjectAreaConfigClient subjectAreaConfigClient();

    /**
     * Get the subject area graph API class - use this class to issue graph calls.
     *
     * @return subject area graph API class
     */
    SubjectAreaGraphClient subjectAreaGraphClient();


    /**
     * Server Name under which this request is performed, this is used in multi tenanting to identify the tenant
     *
     * @return serverName name of the server
     */
    String serverName();

    /**
     * Base url used to issue Rest calls
     *
     * @return base url
     */
    String omasServerUrl();
}