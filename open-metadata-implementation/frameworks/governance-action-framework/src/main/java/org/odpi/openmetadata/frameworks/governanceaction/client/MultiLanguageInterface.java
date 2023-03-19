/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.governanceaction.properties.TranslationDetail;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * MultiLanguageInterface enables translations of the string properties of a metadata element to be created, maintained and retrieved.
 */
public interface MultiLanguageInterface
{
    /**
     * Create or update the translation for a particular language/locale for a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param translationDetail properties of the translation
     *
     * @throws InvalidParameterException  the unique identifier is null or not known.
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    void setTranslation(String            userId,
                        String            elementGUID,
                        TranslationDetail translationDetail) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the translation for a particular language/locale for a metadata element.
     *
     * @param userId caller's userId
     * @param elementGUID unique identifier of the element that this translation is related to
     * @param language language requested
     * @param locale optional locale to qualify which translation if there are multiple translations for the language.
     *
     * @throws InvalidParameterException  the language is null or not known or not unique (add locale)
     * @throws UserNotAuthorizedException the service is not able to access the element
     * @throws PropertyServerException    there is a problem accessing the metadata store
     */
    void clearTranslation(String userId,
                          String elementGUID,
                          String language,
                          String locale) throws InvalidParameterException,
                                                UserNotAuthorizedException,
                                                PropertyServerException;


    /**
     * Retrieve the translation for the matching language/locale.
     *
     * @param userId caller's userId
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
    TranslationDetail getTranslation(String userId,
                                     String elementGUID,
                                     String language,
                                     String locale) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Retrieve all translations associated with a metadata element.
     *
     * @param userId caller's userId
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
    List<TranslationDetail> getTranslations(String userId,
                                            String elementGUID,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;
}
