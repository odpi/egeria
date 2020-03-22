/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server.spring;

import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/servers/{serverName}/open-metadata/repository-services/users/{userId}/search-indexing")
public class SearchIndexingRepositoryServicesResource {

    private OMRSRepositoryRESTServices restAPI = new OMRSRepositoryRESTServices(false);

    /**
     * Default constructor
     */
    public SearchIndexingRepositoryServicesResource()
    {
    }

}
