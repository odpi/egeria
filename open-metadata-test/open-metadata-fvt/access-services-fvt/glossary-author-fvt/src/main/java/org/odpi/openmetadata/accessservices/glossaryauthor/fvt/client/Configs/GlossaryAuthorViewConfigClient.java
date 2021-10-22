package org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client.Configs;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.Config;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GenericResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResponseParameterization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.accessservices.glossaryauthor.fvt.client.GlossaryAuthorViewRestClient;

import static org.odpi.openmetadata.accessservices.glossaryauthor.fvt.FVTConstants.GLOSSARY_AUTHOR_BASE_URL;


public class GlossaryAuthorViewConfigClient implements GlossaryAuthorViewConfig, ResponseParameterization<Config> {
 //   public static final String GLOSSARY_AUTHOR_BASE_URL = "/servers/%s/open-metadata/view-services/glossary-author/users/%s/";
    private static final String BASE_URL = GLOSSARY_AUTHOR_BASE_URL + "configs";
    protected final GlossaryAuthorViewRestClient client;

    public GlossaryAuthorViewConfigClient(GlossaryAuthorViewRestClient client) {
        this.client = client;
    }

    @Override
    public Class<? extends GenericResponse> responseType() {
        return SubjectAreaOMASAPIResponse.class;
    }

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
