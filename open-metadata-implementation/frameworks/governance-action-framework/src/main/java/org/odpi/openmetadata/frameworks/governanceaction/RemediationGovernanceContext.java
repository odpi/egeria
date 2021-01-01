/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ActionTargetElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RequestSourceElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;

import java.util.List;
import java.util.Map;

/**
 * RemediationGovernanceContext provides access to details of the remediation request along with access to the
 * metadata store and APIs to enable changes to a wide range of metadata elements.
 *
 * A remediation service is typically making updates to the actionTargetElements. It may use
 * information from the requestSourceElements to make the changes.  For example,
 * a remediation service that is correcting the schema elements for an asset element could find the
 * asset to update in the actionTargetElements and maybe the schema elements in error.
 * The requestSourceElements could then have the schema annotations created by a discovery service.
 * These would guide the
 */
public abstract class RemediationGovernanceContext extends GovernanceContext
{
    /**
     * Constructor sets up the key parameters for processing the request to the governance service.
     *
     * @param userId calling user
     * @param requestType unique identifier of the asset that the annotations should be attached to
     * @param requestParameters name-value properties to control the governance service
     * @param requestSourceElements metadata elements associated with the request to the governance service
     * @param actionTargetElements metadata elements that need to be worked on by the governance service
     * @param openMetadataStore client to the metadata store for use by the governance service
     */
    public RemediationGovernanceContext(String                     userId,
                                        String                     requestType,
                                        Map<String, String>        requestParameters,
                                        List<RequestSourceElement> requestSourceElements,
                                        List<ActionTargetElement>  actionTargetElements,
                                        OpenMetadataStore          openMetadataStore)
    {
        super(userId, requestType, requestParameters, requestSourceElements, actionTargetElements, openMetadataStore);
    }


    /**
     * Create a new metadata element in the metadata store.  The type name comes from the open metadata types.
     * The selected type also controls the names and types of the properties that are allowed.
     *
     * @param metadataElementTypeName type name of the new metadata element
     * @param properties properties of the new metadata element
     *
     * @return unique identifier of the new metadata element
     *
     * @throws InvalidParameterException the type name or one of the properties is invalid
     * @throws UserNotAuthorizedException the governance service is not authorized to create this type of element
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public abstract String createMetadataElement(String                  metadataElementTypeName,
                                                 List<ElementProperties> properties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

    public abstract String updateMetadataElement(String                  metadataElementGUID,
                                                 boolean                 replaceProperties,
                                                 List<ElementProperties> properties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

    public abstract String deleteMetadataElement(String metadataElementGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    public abstract String classifyMetadataElement(String                  metadataElementGUID,
                                                   String                  classificationName,
                                                   List<ElementProperties> properties) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;

    public abstract String reclassifyMetadataElement(String                  metadataElementGUID,
                                                     String                  classificationName,
                                                     boolean                 replaceProperties,
                                                     List<ElementProperties> properties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;

    public abstract String unclassifyMetadataElement(String metadataElementGUID,
                                                     String classificationName) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    public abstract String createRelatedElements(String                  relationshipTypeName,
                                                 String                  metadataElement1GUID,
                                                 String                  metadataElement2GUID,
                                                 List<ElementProperties> properties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

    public abstract String updateRelatedElements(String                  relationshipGUID,
                                                 boolean                 replaceProperties,
                                                 List<ElementProperties> properties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;

    public abstract String deleteRelatedElements(String relationshipGUID) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


}
