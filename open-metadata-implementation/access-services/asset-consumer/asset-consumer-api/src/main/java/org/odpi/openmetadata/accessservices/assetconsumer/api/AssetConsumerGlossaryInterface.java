/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.MeaningElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * AssetConsumerGlossaryInterface supports the lookup of the meaning of a glossary term.
 */
public interface AssetConsumerGlossaryInterface
{
    /**
     * Return the full definition (meaning) of a term using the unique identifier of the glossary term
     * that contains the definition.
     *
     * @param userId userId of the user making the request.
     * @param guid unique identifier of the glossary term.
     *
     * @return glossary term
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    MeaningElement getMeaning(String userId,
                              String guid) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException;


    /**
     * Return the full definition (meaning) of the terms exactly matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param term name of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<MeaningElement> getMeaningByName(String userId,
                                          String term,
                                          int    startFrom,
                                          int    pageSize) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;

    /**
     * Return the full definition (meaning) of the terms matching the supplied name.
     *
     * @param userId the name of the calling user.
     * @param term name of term.  This may include wild card characters.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return list of glossary terms
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<MeaningElement> findMeanings(String userId,
                                      String term,
                                      int    startFrom,
                                      int    pageSize) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;

    /**
     * Return the list of unique identifiers for assets that are linked to a specific (meaning) either directly or via
     * fields in the schema.
     *
     * @param userId the name of the calling user.
     * @param termGUID unique identifier of term.
     * @param startFrom  index of the list to start from (0 for start)
     * @param pageSize   maximum number of elements to return.
     *
     * @return asset guid list
     * @throws InvalidParameterException the userId is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    List<String> getAssetsByMeaning(String userId,
                                    String termGUID,
                                    int    startFrom,
                                    int    pageSize) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException;
}
