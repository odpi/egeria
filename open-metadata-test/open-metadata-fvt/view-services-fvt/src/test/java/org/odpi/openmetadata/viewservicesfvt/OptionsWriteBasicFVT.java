/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewClassificationRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.KnownDuplicateProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.PeerDuplicateLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworkservices.omf.rest.OpenMetadataRelationshipListResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.classification;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.classificationExplorer;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.createAsset;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.dataFile;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.metadataExpert;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.raw;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.rawOrNull;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.record;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.string;

/**
 * OptionsWriteBasicFVT checks the BasicOptions fields on write requests: requestId, effectiveTime, forLineage and
 * forDuplicateProcessing.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OptionsWriteBasicFVT
{
    private static final long DAY = 24L * 60L * 60L * 1000L;

    private static final List<String> created = new ArrayList<>();


    /**
     * requestId correlates a request across the servers it passes through.  RESTCallLogger.logRESTCall puts the
     * caller's requestId on the thread (or a new UUID when the caller supplies none), the REST client passes it on
     * to the metadata access store, the repository records it as the element's lastRequestId, and
     * RESTCallLogger.logRESTCallReturn echoes it on the response.
     */
    @Test
    void requestIdIsEchoedAndRecordedOnTheElement()
    {
        final String createRequestId = "options-write-create-" + OMAGPlatformExtension.RUN_ID;
        final String updateRequestId = "options-write-update-" + OMAGPlatformExtension.RUN_ID;

        NewElementRequestBody createRequestBody = new NewElementRequestBody();

        createRequestBody.setIsOwnAnchor(true);
        createRequestBody.setProperties(dataFile("request-id"));
        createRequestBody.setRequestId(createRequestId);

        GUIDResponse createResponse = assetMaker.post("/assets", createRequestBody, GUIDResponse.class);
        String       guid           = record(createResponse.getGUID(), created);

        assertEquals(createRequestId, createResponse.getRequestId(), "the create response does not echo the requestId");
        assertEquals(createRequestId, raw(guid).getVersions().getLastRequestId(), "the new element does not record the create's requestId");

        DataFileProperties update = new DataFileProperties();

        update.setDescription("Updated with a requestId");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setProperties(update);
        updateRequestBody.setRequestId(updateRequestId);

        BooleanResponse updateResponse = assetMaker.post("/assets/" + guid + "/update", updateRequestBody, BooleanResponse.class);

        assertEquals(updateRequestId, updateResponse.getRequestId(), "the update response does not echo the requestId");
        assertEquals(updateRequestId, raw(guid).getVersions().getLastRequestId(), "the updated element does not record the update's requestId");

        /*
         * Without a requestId, the server makes one up: it is returned, and it is the one the element records.
         */
        update.setDescription("Updated without a requestId");
        updateRequestBody.setRequestId(null);

        BooleanResponse anonymousResponse = assetMaker.post("/assets/" + guid + "/update", updateRequestBody, BooleanResponse.class);

        assertNotNull(anonymousResponse.getRequestId(), "no requestId was generated for a request that supplied none");
        assertNotEquals(updateRequestId, anonymousResponse.getRequestId(), "the previous request's requestId leaked into a later request");
        assertEquals(anonymousResponse.getRequestId(), raw(guid).getVersions().getLastRequestId(),
                     "the element does not record the requestId generated for the update");
    }


    /**
     * effectiveTime on a write is the time at which the elements the write works on must be effective.  An element
     * that only becomes effective in ten days cannot be updated or deleted "as of now", but can be "as of" a time
     * after it becomes effective.  The delete uses SOFT_DELETE - see effectiveTimeOnDefaultDelete for the default
     * delete method.
     */
    @Test
    void effectiveTimeOnWrites()
    {
        Date now          = new Date();
        Date futureStart  = new Date(now.getTime() + (10 * DAY));
        Date afterStart   = new Date(now.getTime() + (20 * DAY));

        DataFileProperties properties = dataFile("effective-time");

        properties.setEffectiveFrom(futureStart);

        String guid = createAsset(properties, created);

        DataFileProperties update = new DataFileProperties();

        update.setDescription("Updated as of a time when the element is effective");

        UpdateElementRequestBody updateNow = new UpdateElementRequestBody();

        updateNow.setProperties(update);
        updateNow.setEffectiveTime(now);

        assetMaker.postForFailure("/assets/" + guid + "/update", updateNow);

        assertEquals("Description of effective-time", string(raw(guid), OpenMetadataProperty.DESCRIPTION.name),
                     "an update as of a time when the element is not effective changed it");

        UpdateElementRequestBody updateLater = new UpdateElementRequestBody();

        updateLater.setProperties(update);
        updateLater.setEffectiveTime(afterStart);

        assetMaker.post("/assets/" + guid + "/update", updateLater, BooleanResponse.class);

        assertEquals(update.getDescription(), string(raw(guid), OpenMetadataProperty.DESCRIPTION.name));

        DeleteElementRequestBody deleteNow = new DeleteElementRequestBody();

        deleteNow.setDeleteMethod(DeleteMethod.SOFT_DELETE);
        deleteNow.setEffectiveTime(now);

        assetMaker.postForFailure("/assets/" + guid + "/delete", deleteNow);

        assertNotNull(rawOrNull(guid, new GetRequestBody()), "a delete as of a time when the element is not effective removed it");

        DeleteElementRequestBody deleteLater = new DeleteElementRequestBody();

        deleteLater.setDeleteMethod(DeleteMethod.SOFT_DELETE);
        deleteLater.setEffectiveTime(afterStart);

        assetMaker.call("/assets/" + guid + "/delete", deleteLater);

        assertNull(rawOrNull(guid, new GetRequestBody()), "the delete as of a time when the element is effective did not remove it");
    }


    /**
     * The same delete as in effectiveTimeOnWrites, with the default delete method (LOOK_FOR_LINEAGE).  The delete
     * as of a time when the element is effective must succeed, as it does with SOFT_DELETE.
     */
    @Test
    void effectiveTimeOnDefaultDelete()
    {
        Date now         = new Date();
        Date futureStart = new Date(now.getTime() + (10 * DAY));
        Date afterStart  = new Date(now.getTime() + (20 * DAY));

        DataFileProperties properties = dataFile("effective-time-default-delete");

        properties.setEffectiveFrom(futureStart);

        String guid = createAsset(properties, created);

        DeleteElementRequestBody deleteLater = new DeleteElementRequestBody();

        deleteLater.setEffectiveTime(afterStart);

        assetMaker.call("/assets/" + guid + "/delete", deleteLater);

        assertNull(rawOrNull(guid, new GetRequestBody()), "the delete as of a time when the element is effective did not remove it");
    }


    /**
     * forLineage lets a request see (and update) mementos.  An element with a lineage relationship (here DataFlow)
     * is archived by a default delete: it gets the Memento classification and stays in the repository so the
     * lineage graph is unbroken.  Reads without forLineage no longer return it; reads with forLineage do.  An update
     * of the memento is refused without forLineage and accepted with it.
     */
    @Test
    void forLineageSeesAndUpdatesMementos()
    {
        String source      = createAsset(dataFile("lineage-source"), created);
        String destination = createAsset(dataFile("lineage-destination"), created);

        OptionsWriteSupport.link(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, source, destination);

        assetMaker.call("/assets/" + source + "/delete", new DeleteElementRequestBody());

        assertNull(rawOrNull(source, new GetRequestBody()), "a memento is returned to a read without forLineage");
        ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + source + "/retrieve");

        OpenMetadataElement memento = rawOrNull(source, OptionsWriteSupport.forLineage());

        assertNotNull(memento, "the archived element is not returned to a read with forLineage");
        assertNotNull(classification(memento, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName), "the archived element has no Memento classification");
        assertNotNull(assetMaker.element("/assets/" + source + "/retrieve", OptionsWriteSupport.forLineage()),
                      "getAssetByGUID with forLineage does not return the memento");

        DataFileProperties update = new DataFileProperties();

        update.setDescription("Updated memento");

        UpdateElementRequestBody withoutForLineage = new UpdateElementRequestBody();

        withoutForLineage.setProperties(update);

        assetMaker.postForFailure("/assets/" + source + "/update", withoutForLineage);

        UpdateElementRequestBody withForLineage = new UpdateElementRequestBody();

        withForLineage.setProperties(update);
        withForLineage.setForLineage(true);

        assetMaker.post("/assets/" + source + "/update", withForLineage, BooleanResponse.class);

        assertEquals("Updated memento", string(raw(source, OptionsWriteSupport.forLineage()), OpenMetadataProperty.DESCRIPTION.name));
    }


    /**
     * A memento can itself be deleted by a request that sets forLineage: without forLineage the memento is
     * invisible to the delete and the request is refused.  SOFT_DELETE is used so the delete really removes it
     * rather than archiving it again.
     */
    @Test
    void forLineageDeletesAMemento()
    {
        String source      = createAsset(dataFile("lineage-purge-source"), created);
        String destination = createAsset(dataFile("lineage-purge-destination"), created);

        OptionsWriteSupport.link(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, source, destination);

        assetMaker.call("/assets/" + source + "/delete", new DeleteElementRequestBody());

        assertNotNull(rawOrNull(source, OptionsWriteSupport.forLineage()), "the element with lineage was not archived");

        DeleteElementRequestBody withoutForLineage = new DeleteElementRequestBody();

        withoutForLineage.setDeleteMethod(DeleteMethod.SOFT_DELETE);

        assetMaker.postForFailure("/assets/" + source + "/delete", withoutForLineage);

        assertNotNull(rawOrNull(source, OptionsWriteSupport.forLineage()), "a delete without forLineage removed a memento");

        DeleteElementRequestBody withForLineage = new DeleteElementRequestBody();

        withForLineage.setDeleteMethod(DeleteMethod.SOFT_DELETE);
        withForLineage.setForLineage(true);

        assetMaker.call("/assets/" + source + "/delete", withForLineage);

        assertNull(rawOrNull(source, OptionsWriteSupport.forLineage()), "the delete with forLineage did not remove the memento");
    }


    /**
     * forDuplicateProcessing turns off deduplication.  Two elements are linked as validated peer duplicates and the
     * first is classified as a KnownDuplicate.  An ordinary read of the first returns the combined view of the
     * peers - the most recently updated peer, with the classifications of both - while a read with
     * forDuplicateProcessing returns the first element as itself.  The PeerDuplicateLink is only visible to
     * forDuplicateProcessing requests.
     */
    @Test
    void forDuplicateProcessingReturnsEachDuplicateAsItself()
    {
        String first  = createAsset(dataFile("duplicate-first"), created);
        String second = createAsset(dataFile("duplicate-second"), created);

        NewClassificationRequestBody knownDuplicate = new NewClassificationRequestBody();

        knownDuplicate.setProperties(new KnownDuplicateProperties());

        classificationExplorer.call("/elements/" + first + "/known-duplicate", knownDuplicate);

        PeerDuplicateLinkProperties linkProperties = new PeerDuplicateLinkProperties();

        linkProperties.setStatusIdentifier(StatusIdentifier.VALIDATED.getOrdinal());
        linkProperties.setSteward(OMAGPlatformExtension.USER_ID);

        NewRelationshipRequestBody link = new NewRelationshipRequestBody();

        link.setProperties(linkProperties);

        classificationExplorer.call("/related-elements/" + first + "/peer-duplicate/" + second + "/attach", link);

        /*
         * Make the second peer the most recently updated one, so the combined view takes its properties.
         */
        DataFileProperties update = new DataFileProperties();

        update.setDescription("Latest description from the second peer");

        UpdateElementRequestBody updateRequestBody = new UpdateElementRequestBody();

        updateRequestBody.setProperties(update);
        updateRequestBody.setForDuplicateProcessing(true);

        assetMaker.post("/assets/" + second + "/update", updateRequestBody, BooleanResponse.class);

        GetRequestBody asItself = new GetRequestBody();

        asItself.setForDuplicateProcessing(true);

        OpenMetadataElement firstAsItself = raw(first, asItself);

        assertEquals(first, firstAsItself.getElementGUID());
        assertEquals("Description of duplicate-first", string(firstAsItself, OpenMetadataProperty.DESCRIPTION.name),
                     "forDuplicateProcessing did not return the first duplicate as itself");

        OpenMetadataElement combined = raw(first);

        assertEquals("Latest description from the second peer", string(combined, OpenMetadataProperty.DESCRIPTION.name),
                     "an ordinary read of a known duplicate did not return the combined (latest peer) view");
        assertNotNull(classification(combined, OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName),
                      "the combined view does not carry the classifications of all the peers");

        assertTrue(peerLinks(first, second, false).isEmpty(), "the PeerDuplicateLink is visible to an ordinary query");
        assertEquals(1, peerLinks(first, second, true).size(), "the PeerDuplicateLink is not visible to a forDuplicateProcessing query");
    }


    private static List<?> peerLinks(String  first,
                                     String  second,
                                     boolean forDuplicateProcessing)
    {
        ResultsRequestBody requestBody = new ResultsRequestBody();

        requestBody.setForDuplicateProcessing(forDuplicateProcessing);

        OpenMetadataRelationshipListResponse response = metadataExpert.post("/metadata-elements/" + first + "/linked-by-type/"
                                                                                    + OpenMetadataType.PEER_DUPLICATE_LINK.typeName + "/to-elements/" + second,
                                                                            requestBody,
                                                                            OpenMetadataRelationshipListResponse.class);

        if ((response.getRelationshipList() == null) || (response.getRelationshipList().getRelationships() == null))
        {
            return List.of();
        }

        return response.getRelationshipList().getRelationships();
    }


    @AfterAll
    static void cleanUp()
    {
        OptionsWriteSupport.purgeAll(created);
    }
}
