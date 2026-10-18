/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.MetadataSourceRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateWithTemplateRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryTermProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ConfidentialityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.CriticalityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.assertRefused;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.classification;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.classificationNames;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.collectionManager;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.createAsset;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.dataFile;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.forLineage;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.glossaryManager;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.metadataExpert;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.named;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.propertyHelper;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.raw;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.rawOrNull;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.record;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.string;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.stringMap;

/**
 * OptionsWriteUpdateDeleteFVT checks UpdateOptions (mergeUpdate), UpdateWithTemplateOptions (mergeClassifications)
 * and DeleteOptions (cascadedDelete, every DeleteMethod, archiveDate, archiveProcess, archiveProperties).
 * <br>
 * The delete methods are told apart by what is left behind:
 * <ul>
 *     <li>ARCHIVE - the element stays, with a Memento classification, visible only to forLineage requests;</li>
 *     <li>SOFT_DELETE - the element is deleted, invisible even to forLineage requests, but can be restored;</li>
 *     <li>LOOK_FOR_LINEAGE (the default) - ARCHIVE if the element has lineage relationships, otherwise SOFT_DELETE;</li>
 *     <li>PURGE - the element is removed from the repository and cannot be restored;</li>
 *     <li>OTHER - not a delete method the metadata access store implements, so the request is refused.</li>
 * </ul>
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OptionsWriteUpdateDeleteFVT
{
    private static final String DEPENDENT_ELEMENTS_FOUND = "OMAG-GENERIC-HANDLERS-403-005";

    private static final List<String> created = new ArrayList<>();


    /**
     * mergeUpdate=true (the default) changes only the properties in the request; mergeUpdate=false replaces the
     * element's properties with those in the request, so the properties it does not mention are removed.
     */
    @Test
    void mergeUpdate()
    {
        DataFileProperties properties = dataFile("merge-update");

        properties.setFileType("csv");

        String guid = createAsset(properties, created);

        DataFileProperties merge = new DataFileProperties();

        merge.setDescription("Merged description");

        UpdateElementRequestBody mergeRequest = new UpdateElementRequestBody();

        mergeRequest.setMergeUpdate(true);
        mergeRequest.setProperties(merge);

        assetMaker.post("/assets/" + guid + "/update", mergeRequest, BooleanResponse.class);

        OpenMetadataElement merged = raw(guid);

        assertEquals("Merged description", string(merged, OpenMetadataProperty.DESCRIPTION.name));
        assertEquals("csv", string(merged, OpenMetadataProperty.FILE_TYPE.name), "mergeUpdate=true removed a property the request did not mention");
        assertEquals("merge-update", string(merged, OpenMetadataProperty.DISPLAY_NAME.name), "mergeUpdate=true removed a property the request did not mention");

        DataFileProperties replacement = new DataFileProperties();

        replacement.setQualifiedName(properties.getQualifiedName());
        replacement.setDescription("Replacement description");

        UpdateElementRequestBody replaceRequest = new UpdateElementRequestBody();

        replaceRequest.setMergeUpdate(false);
        replaceRequest.setProperties(replacement);

        assetMaker.post("/assets/" + guid + "/update", replaceRequest, BooleanResponse.class);

        OpenMetadataElement replaced = raw(guid);

        assertEquals(properties.getQualifiedName(), string(replaced, OpenMetadataProperty.QUALIFIED_NAME.name));
        assertEquals("Replacement description", string(replaced, OpenMetadataProperty.DESCRIPTION.name));
        assertNull(string(replaced, OpenMetadataProperty.FILE_TYPE.name), "mergeUpdate=false kept a property the request did not mention");
        assertNull(string(replaced, OpenMetadataProperty.DISPLAY_NAME.name), "mergeUpdate=false kept a property the request did not mention");
    }


    /**
     * An update from a template takes the template's properties and classifications.  mergeClassifications=true
     * (the default) adds the template's classifications to the element's own; mergeClassifications=false replaces
     * the element's classifications with the template's.
     */
    @Test
    void mergeClassifications()
    {
        String glossary = record(collectionManager.create("/collections", newElement(named(new GlossaryProperties(), "Glossary", "merge-classifications"), true)), created);

        ConfidentialityProperties confidentiality = new ConfidentialityProperties();

        confidentiality.setConfidentialityLevel(2);

        CriticalityProperties criticality = new CriticalityProperties();

        criticality.setCriticalityLevel(4);

        String template = createTerm(glossary, "template-term", "Summary from the template",
                                     Map.of(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName, confidentiality));
        String merged   = createTerm(glossary, "merge-target", null, Map.of(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName, criticality));
        String replaced = createTerm(glossary, "replace-target", null, Map.of(OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName, criticality));

        UpdateWithTemplateRequestBody mergeRequest = new UpdateWithTemplateRequestBody();

        mergeRequest.setMergeClassifications(true);

        glossaryManager.call("/glossaries/terms/" + merged + "/update/from-template/" + template, mergeRequest);

        UpdateWithTemplateRequestBody replaceRequest = new UpdateWithTemplateRequestBody();

        replaceRequest.setMergeClassifications(false);

        glossaryManager.call("/glossaries/terms/" + replaced + "/update/from-template/" + template, replaceRequest);

        OpenMetadataElement mergedTerm = raw(merged);

        assertEquals("Summary from the template", string(mergedTerm, OpenMetadataProperty.SUMMARY.name), "the update did not take the template's properties");
        assertNotNull(classification(mergedTerm, OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName),
                      "mergeClassifications=true did not add the template's classification");
        assertNotNull(classification(mergedTerm, OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName),
                      "mergeClassifications=true removed the element's own classification");

        OpenMetadataElement replacedTerm = raw(replaced);

        assertNotNull(classification(replacedTerm, OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName),
                      "mergeClassifications=false did not add the template's classification");
        assertNull(classification(replacedTerm, OpenMetadataType.CRITICALITY_CLASSIFICATION.typeName),
                   "mergeClassifications=false kept the element's own classification: " + classificationNames(replacedTerm));
    }


    /**
     * cascadedDelete=false refuses to delete a folder that still holds a file (OMAG-GENERIC-HANDLERS-403-005) and
     * leaves both in place; cascadedDelete=true deletes the folder and the file in it.
     */
    @Test
    void cascadedDelete()
    {
        String folder = createAsset(named(new FileFolderProperties(), "FileFolder", "cascade-folder"), created);

        NewElementRequestBody fileRequestBody = newElement(dataFile("cascade-file"), true);

        fileRequestBody.setParentGUID(folder);
        fileRequestBody.setParentRelationshipTypeName(OpenMetadataType.NESTED_FILE_RELATIONSHIP.typeName);
        fileRequestBody.setParentAtEnd1(true);

        String file = record(assetMaker.create("/assets", fileRequestBody), created);

        DeleteElementRequestBody noCascade = new DeleteElementRequestBody();

        noCascade.setCascadedDelete(false);

        assertRefused(assetMaker, "/assets/" + folder + "/delete", noCascade, DEPENDENT_ELEMENTS_FOUND);

        assertNotNull(rawOrNull(folder, new GetRequestBody()), "the refused delete removed the folder");
        assertNotNull(rawOrNull(file, new GetRequestBody()), "the refused delete removed the file");

        DeleteElementRequestBody cascade = new DeleteElementRequestBody();

        cascade.setCascadedDelete(true);

        assetMaker.call("/assets/" + folder + "/delete", cascade);

        assertNull(rawOrNull(folder, new GetRequestBody()), "cascadedDelete=true did not delete the folder");
        assertNull(rawOrNull(file, new GetRequestBody()), "cascadedDelete=true did not delete the file in the folder");
    }


    /**
     * ARCHIVE keeps the element as a memento - even one with no lineage - and records the archive details in its
     * Memento classification: archiveDate, archiveProcess and archiveProperties as supplied, and the archiveUser.
     */
    @Test
    void archiveRecordsTheArchiveDetails()
    {
        String guid        = createAsset(dataFile("archived"), created);
        Date   archiveDate = new Date(new Date().getTime() - 60000L);

        DeleteElementRequestBody archive = new DeleteElementRequestBody();

        archive.setDeleteMethod(DeleteMethod.ARCHIVE);
        archive.setArchiveDate(archiveDate);
        archive.setArchiveProcess("OptionsWriteUpdateDeleteFVT archive");
        archive.setArchiveProperties(Map.of("archiveLocation", "tape-7"));

        assetMaker.call("/assets/" + guid + "/delete", archive);

        assertNull(rawOrNull(guid, new GetRequestBody()), "an archived element is visible without forLineage");

        OpenMetadataElement memento = rawOrNull(guid, forLineage());

        assertNotNull(memento, "ARCHIVE did not keep the element as a memento");

        AttachedClassification mementoClassification = classification(memento, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName);

        assertNotNull(mementoClassification, "the archived element has no Memento classification");
        assertEquals(archiveDate.getTime(),
                     propertyHelper.getDateProperty("OptionsWriteUpdateDeleteFVT", OpenMetadataProperty.ARCHIVE_DATE.name,
                                                    mementoClassification.getClassificationProperties(), "archive").getTime(),
                     "archiveDate was not recorded");
        assertEquals("OptionsWriteUpdateDeleteFVT archive", string(mementoClassification.getClassificationProperties(), OpenMetadataProperty.ARCHIVE_PROCESS.name),
                     "archiveProcess was not recorded");
        assertEquals(Map.of("archiveLocation", "tape-7"), stringMap(mementoClassification.getClassificationProperties(), OpenMetadataProperty.ARCHIVE_PROPERTIES.name),
                     "archiveProperties were not recorded");
        assertEquals(OMAGPlatformExtension.USER_ID, string(mementoClassification.getClassificationProperties(), OpenMetadataProperty.ARCHIVE_USER.name),
                     "the archiving user was not recorded");
    }


    /**
     * SOFT_DELETE deletes the element even when it has lineage: it is not kept as a memento (a forLineage read does
     * not find it), but it can be restored.
     */
    @Test
    void softDeleteIsRestorable()
    {
        String guid = createAsset(dataFile("soft-deleted"), created);

        OptionsWriteSupport.link(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, guid, createAsset(dataFile("soft-deleted-destination"), created));

        DeleteElementRequestBody softDelete = new DeleteElementRequestBody();

        softDelete.setDeleteMethod(DeleteMethod.SOFT_DELETE);

        assetMaker.call("/assets/" + guid + "/delete", softDelete);

        assertNull(rawOrNull(guid, forLineage()), "SOFT_DELETE archived an element with lineage instead of deleting it");

        metadataExpert.call("/metadata-elements/" + guid + "/restore", new MetadataSourceRequestBody());

        OpenMetadataElement restored = raw(guid);

        assertNull(classification(restored, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName), "the restored element is a memento");
    }


    /**
     * LOOK_FOR_LINEAGE archives an element that has lineage relationships and soft-deletes one that does not.
     */
    @Test
    void lookForLineageChoosesArchiveOrSoftDelete()
    {
        String withLineage    = createAsset(dataFile("look-for-lineage-with"), created);
        String withoutLineage = createAsset(dataFile("look-for-lineage-without"), created);

        OptionsWriteSupport.link(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, withLineage, createAsset(dataFile("look-for-lineage-destination"), created));

        DeleteElementRequestBody lookForLineage = new DeleteElementRequestBody();

        lookForLineage.setDeleteMethod(DeleteMethod.LOOK_FOR_LINEAGE);

        assetMaker.call("/assets/" + withLineage + "/delete", lookForLineage);
        assetMaker.call("/assets/" + withoutLineage + "/delete", lookForLineage);

        OpenMetadataElement memento = rawOrNull(withLineage, forLineage());

        assertNotNull(memento, "LOOK_FOR_LINEAGE did not archive the element with lineage");
        assertNotNull(classification(memento, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName));

        assertNull(rawOrNull(withoutLineage, forLineage()), "LOOK_FOR_LINEAGE archived the element without lineage");

        metadataExpert.call("/metadata-elements/" + withoutLineage + "/restore", new MetadataSourceRequestBody());

        assertNotNull(rawOrNull(withoutLineage, new GetRequestBody()), "the element without lineage was not soft-deleted (it cannot be restored)");
    }


    /**
     * PURGE of a soft-deleted element removes it for good: it can no longer be restored.
     */
    @Test
    void purgeOfADeletedElementCannotBeRestored()
    {
        String guid = createAsset(dataFile("purged-after-delete"), created);

        DeleteElementRequestBody softDelete = new DeleteElementRequestBody();

        softDelete.setDeleteMethod(DeleteMethod.SOFT_DELETE);

        assetMaker.call("/assets/" + guid + "/delete", softDelete);

        DeleteElementRequestBody purge = new DeleteElementRequestBody();

        purge.setDeleteMethod(DeleteMethod.PURGE);

        assetMaker.call("/assets/" + guid + "/delete", purge);

        metadataExpert.postForFailure("/metadata-elements/" + guid + "/restore", new MetadataSourceRequestBody());

        assertNull(rawOrNull(guid, forLineage()), "the purged element is still there");
    }


    /**
     * PURGE of a live element removes it from the repository in one request: it is not returned, even to a
     * forLineage read, and cannot be restored.
     */
    @Test
    void purgeOfALiveElement()
    {
        String guid = createAsset(dataFile("purged-live"), created);

        DeleteElementRequestBody purge = new DeleteElementRequestBody();

        purge.setDeleteMethod(DeleteMethod.PURGE);

        assetMaker.call("/assets/" + guid + "/delete", purge);

        assertNull(rawOrNull(guid, forLineage()), "the purged element is still there");

        metadataExpert.postForFailure("/metadata-elements/" + guid + "/restore", new MetadataSourceRequestBody());
    }


    /**
     * OTHER names no delete the metadata access store implements, so the request is refused as an invalid
     * parameter and the element is left alone.
     */
    @Test
    void otherIsRefused()
    {
        String guid = createAsset(dataFile("delete-method-other"), created);

        DeleteElementRequestBody other = new DeleteElementRequestBody();

        other.setDeleteMethod(DeleteMethod.OTHER);

        String refusal = OptionsWriteSupport.refusal(assetMaker, "/assets/" + guid + "/delete", other);

        assertTrue(refusal.contains("deleteMethod"), "the OTHER delete was refused for the wrong reason: " + refusal);

        OpenMetadataElement element = raw(guid);

        assertNull(classification(element, OpenMetadataType.MEMENTO_CLASSIFICATION.typeName), "the refused delete archived the element");
    }


    private static NewElementRequestBody newElement(org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties properties,
                                                    boolean                                                                     isOwnAnchor)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(isOwnAnchor);
        requestBody.setProperties(properties);

        return requestBody;
    }


    private static String createTerm(String                                glossary,
                                     String                                name,
                                     String                                summary,
                                     Map<String, ClassificationProperties> classifications)
    {
        GlossaryTermProperties properties = named(new GlossaryTermProperties(), "GlossaryTerm", name);

        properties.setSummary(summary);

        NewElementRequestBody requestBody = newElement(properties, false);

        requestBody.setAnchorGUID(glossary);
        requestBody.setParentGUID(glossary);
        requestBody.setParentRelationshipTypeName(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName);
        requestBody.setParentAtEnd1(true);
        requestBody.setInitialClassifications(classifications);

        return record(glossaryManager.create("/glossaries/terms", requestBody), created);
    }


    @AfterAll
    static void cleanUp()
    {
        OptionsWriteSupport.purgeAll(created);
    }
}
