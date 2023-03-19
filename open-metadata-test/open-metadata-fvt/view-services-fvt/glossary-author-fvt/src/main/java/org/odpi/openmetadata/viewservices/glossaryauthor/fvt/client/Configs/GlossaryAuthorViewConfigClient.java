/*  SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.Configs;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.viewservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;

import static org.odpi.openmetadata.viewservices.glossaryauthor.fvt.FVTConstants.GLOSSARY_AUTHOR_BASE_URL;

/**
 * The class acts as a wrapper class for calling the REST services for Glossary Author Config related services.
 */

public class GlossaryAuthorViewConfigClient implements GlossaryAuthorViewConfig, ResponseParameterization<Config> {
    private static final String BASE_URL = GLOSSARY_AUTHOR_BASE_URL + "configs";
    protected final GlossaryAuthorViewRestClient client;

    public GlossaryAuthorViewConfigClient(GlossaryAuthorViewRestClient client) {
        this.client = client;
    }


    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }
    /**
     * Get the config
     <p>
     * The result is the configuration
     *
     * @param userId       userId under which the request is performed
     *
     * @return The configuration
     *
     * @throws PropertyServerException something went wrong with the REST call stack.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     * @throws InvalidParameterException one of the parameters is null or invalid
     */
    @Override
    public Config getConfig(String userId) throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        final String methodName = "getConfig";

        String urlTemplate = BASE_URL + "/%s";
        GenericResponse<Config> response = client.getByIdRESTCall(userId,
                                                            "current",
                                                            methodName,
                                                            getParameterizedType(),
                                                            urlTemplate);/*,
                                                            null,
                                                        null,
                                                            null);*/

        return response.head().get();
    }
}
