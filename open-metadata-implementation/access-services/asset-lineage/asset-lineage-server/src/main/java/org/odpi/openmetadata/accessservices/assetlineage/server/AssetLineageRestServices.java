/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The AssetLineageRESTServices provides the server-side implementation of the Asset Lineage Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetLineageRestServices {

    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private static final Logger log = LoggerFactory.getLogger(AssetLineageRestServices.class);

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public AssetLineageRestServices() {
        instanceHandler.registerAccessService();
    }

    /**
     * Scan the cohort for Glossary Terms available
     * Publish the context for each Glossary Term on OMAS out Topic
     *
     * @param serverName  name of server instance to call
     * @param userId      the name of the calling user
     * @return a list of unique identifiers (guids) of the available Glossary Terms as a response
     */
    public GUIDListResponse scanGlossaryTerms(String serverName, String userId) {
        GUIDListResponse response = new GUIDListResponse();

        String methodName = "getProcessContext";
        try {
            GlossaryHandler glossaryHandler = instanceHandler.getGlossaryHandler(userId, serverName, methodName);
            List<EntityDetail> glossaryTerms = glossaryHandler.getGlossaryTerms(userId);
            if (CollectionUtils.isEmpty(glossaryTerms)) {
                return response;
            }

            AssetLineagePublisher publisher = instanceHandler.getAssetLineagePublisher(userId, serverName, methodName);

            if (publisher == null) {
                log.debug("Asset Lineage Publisher is not available. " +
                        "The context event for Glossary Terms could not be published on the Asset Lineage OMAS Out Topic.");
                return response;
            }

            publishGlossaryTerms(glossaryTerms, publisher);
            response.setGUIDs(glossaryTerms.stream().map(InstanceHeader::getGUID).collect(Collectors.toList()));
        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
           restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }

    private void publishGlossaryTerms(List<EntityDetail> glossaryTerms, AssetLineagePublisher publisher) {
        glossaryTerms.parallelStream().forEach(glossaryTerm -> {
            try {
                publisher.publishAssetContext(glossaryTerm);
            } catch (OCFCheckedExceptionBase | JsonProcessingException ocfCheckedExceptionBase) {
               log.error("The context for Glossary Term with guid = {} can not be published.", glossaryTerm.getGUID());
            }
        });
    }
}
