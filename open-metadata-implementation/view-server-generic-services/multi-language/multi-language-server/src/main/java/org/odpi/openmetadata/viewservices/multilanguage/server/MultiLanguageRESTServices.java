/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.multilanguage.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.translations.TranslationDetailProperties;
import org.odpi.openmetadata.frameworkservices.omf.rest.TranslationDetailResponse;
import org.odpi.openmetadata.frameworkservices.omf.rest.TranslationListResponse;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The MultiLanguageRESTServices provides the server-side implementation of the Multi Language Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class MultiLanguageRESTServices extends TokenController
{
    private static final MultiLanguageInstanceHandler instanceHandler = new MultiLanguageInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(MultiLanguageRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public MultiLanguageRESTServices()
    {
    }


    /*
     * =====================================================================================================================
     * Translations
     *
     * MultiLanguageInterface, implemented by OpenMetadataClient, maintains the TranslationDetail elements and their
     * TranslationLink relationships to the elements they translate.
     */

    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param requestBody properties of the translation
     *
     * @return void or
     * InvalidParameterException  the unique identifier is null or not known
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    public VoidResponse setTranslation(String                serverName,
                                       String                urlMarker,
                                       String                elementGUID,
                                       NewElementRequestBody requestBody)
    {
        final String methodName = "setTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName, requestBody);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            if (requestBody != null)
            {
                if (requestBody.getProperties() instanceof TranslationDetailProperties translationDetailProperties)
                {
                    handler.setTranslation(userId, elementGUID, requestBody.getInitialClassifications(), translationDetailProperties);
                }
                else
                {
                    restExceptionHandler.handleInvalidPropertiesObject(TranslationDetailProperties.class.getName(), methodName);
                }
            }
            else
            {
                restExceptionHandler.handleNoRequestBody(userId, methodName, serverName);
            }
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language
     *
     * @return void or
     * InvalidParameterException  the language is null or not known or not unique (add locale)
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    public VoidResponse clearTranslation(String serverName,
                                         String urlMarker,
                                         String elementGUID,
                                         String language,
                                         String locale)
    {
        final String methodName = "clearTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            handler.clearTranslation(userId, elementGUID, language, locale);
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language
     *
     * @return the properties of the translation or null if there is none or
     * InvalidParameterException  the unique identifier is null or not known
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    public TranslationDetailResponse getTranslation(String serverName,
                                                    String urlMarker,
                                                    String elementGUID,
                                                    String language,
                                                    String locale)
    {
        final String methodName = "getTranslation";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TranslationDetailResponse response = new TranslationDetailResponse();
        AuditLog                  auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setElement(handler.getTranslation(userId, elementGUID, language, locale));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param serverName name of the server to route the request to
     * @param urlMarker  view service URL marker
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none or
     * InvalidParameterException  the unique identifier is null or not known
     * UserNotAuthorizedException the service is not able to access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    public TranslationListResponse getTranslations(String serverName,
                                                   String urlMarker,
                                                   String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize)
    {
        final String methodName = "getTranslations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        TranslationListResponse response = new TranslationListResponse();
        AuditLog                auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            OpenMetadataClient handler = instanceHandler.getOpenMetadataHandler(userId, serverName, urlMarker, methodName);

            response.setElements(handler.getTranslations(userId, elementGUID, startFrom, pageSize));
        }
        catch (Throwable error)
        {
            restExceptionHandler.captureRuntimeExceptions(response, error, methodName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);
        return response;
    }
}
