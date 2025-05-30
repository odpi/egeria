/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDefinitionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceMetricImplementation;

import java.util.List;

/**
 * The GovernanceProgramReviewInterface supports the periodic review of the governance program.
 * This includes looking at the metrics and the governance zones.
 */
public interface GovernanceProgramReviewInterface
{
    /**
     * Retrieve the governance definition by the unique identifier assigned by this service when it was created.
     *
     * @param userId calling user
     * @param definitionGUID identifier of the governance definition to retrieve
     *
     * @return properties of the governance definition
     *
     * @throws InvalidParameterException guid or userId is null; guid is not recognized
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    GovernanceDefinitionElement getGovernanceDefinitionByGUID(String userId,
                                                              String definitionGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

    /**
     * Return the list of governance definitions associated with a particular governance domain.
     *
     * @param userId calling user
     * @param typeName option type name to restrict retrieval to a specific type
     * @param domainIdentifier identifier of the governance domain - 0 = all domains
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    List<GovernanceDefinitionElement> getGovernanceDefinitionsForDomain(String userId,
                                                                        String typeName,
                                                                        int    domainIdentifier,
                                                                        int    startFrom,
                                                                        int    pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Return the list of governance definitions associated with a unique docId.  In an ideal world, there should be only one.
     *
     * @param userId calling user
     * @param typeName option type name to restrict retrieval to a specific type
     * @param docId unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    List<GovernanceDefinitionElement> getGovernanceDefinitionsForDocId(String userId,
                                                                       String typeName,
                                                                       String docId,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Return the list of governance definitions that match the search string - this can be a regular expression.
     *
     * @param userId calling user
     * @param typeName option type name to restrict retrieval to a specific type
     * @param searchString value to search for
     * @param startFrom where to start from in the list of definition results
     * @param pageSize max number of results to return in one call
     *
     * @return list of governance definitions
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    List<GovernanceDefinitionElement> findGovernanceDefinitions(String userId,
                                                                String typeName,
                                                                String searchString,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Return details of the metrics for a governance definition along with details of where the
     * @param userId calling user
     * @param governanceDefinitionGUID unique name of the governance definition
     * @param startFrom where to start from in the list of definitions
     * @param pageSize max number of results to return in one call
     *
     * @return list of associated metrics and links for retrieving the captured measurements
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    List<GovernanceMetricImplementation> getGovernanceDefinitionMetrics(String userId,
                                                                        String governanceDefinitionGUID,
                                                                        int    startFrom,
                                                                        int    pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Return the list of assets that are members of a particular zone.
     *
     * @param userId calling user
     * @param zoneName unique name of the zone to search for
     * @param subTypeName optional asset subtypeName to limit the results
     * @param startFrom where to start from in the list of assets
     * @param pageSize max number of results to return in one call
     *
     * @return list of headers for assets in the requested zone
     *
     * @throws InvalidParameterException one of the parameters is invalid
     * @throws UserNotAuthorizedException the caller is not authorized to issue the request
     * @throws PropertyServerException the metadata service has problems
     */
    List<ElementStub> getGovernanceZoneMembers(String userId,
                                               String zoneName,
                                               String subTypeName,
                                               int    startFrom,
                                               int    pageSize) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;
}
