/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.properties.TranslationDetail;

import java.util.List;

/**
 * MultiLanguageClient enables translations of the string properties of a metadata element to be created, maintained and retrieved.
 */
public class MultiLanguageClient extends ConnectorContextClientBase
{
    private final OpenMetadataClient openMetadataClient;


    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID unique identifier of the connector element that describes this connector in the open metadata store(s)
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public MultiLanguageClient(ConnectorContextBase     parentContext,
                               String                   localServerName,
                               String                   localServiceName,
                               String                   connectorUserId,
                               String                   connectorGUID,
                               String                   externalSourceGUID,
                               String                   externalSourceName,
                               OpenMetadataClient       openMetadataClient,
                               AuditLog                 auditLog,
                               int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.openMetadataClient = openMetadataClient;
    }


    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public  void setTranslation(String            elementGUID,
                                TranslationDetail translationDetail) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        openMetadataClient.setTranslation(connectorUserId, elementGUID, translationDetail);
    }


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @throws InvalidParameterException  the language is null or not known or not unique (add locale)
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public void clearTranslation(String elementGUID,
                                 String language,
                                 String locale) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException
    {
        openMetadataClient.clearTranslation(connectorUserId, elementGUID, language, locale);
    }


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @return the properties of the translation or null if there is none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public TranslationDetail getTranslation(String elementGUID,
                                            String language,
                                            String locale) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException
    {
        return openMetadataClient.getTranslation(connectorUserId, elementGUID, language, locale);
    }


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return
     *
     * @return list of translation properties or null if there are none
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    public List<TranslationDetail> getTranslations(String elementGUID,
                                                   int    startFrom,
                                                   int    pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        return openMetadataClient.getTranslations(connectorUserId, elementGUID, startFrom, pageSize);
    }
}
