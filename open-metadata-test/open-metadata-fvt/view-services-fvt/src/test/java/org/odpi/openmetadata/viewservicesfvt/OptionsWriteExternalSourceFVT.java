/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.UpdateElementRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementOrigin;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.assertRefused;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.assetMaker;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.createAsset;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.dataFile;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.named;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.raw;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.rawOrNull;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.record;
import static org.odpi.openmetadata.viewservicesfvt.OptionsWriteSupport.string;

/**
 * OptionsWriteExternalSourceFVT checks MetadataSourceOptions: externalSourceGUID and externalSourceName.
 * <br>
 * An element created with an external source (a software capability registered in open metadata) has external
 * source provenance: its home metadata collection is the external source.  From then on the repository handler's
 * provenance check (RepositoryErrorHandler.validateProvenance) protects it:
 * <ul>
 *     <li>a request with no external source - a local caller - is refused with OMAG-REPOSITORY-HANDLER-400-008
 *     (LOCAL_CANNOT_CHANGE_EXTERNAL);</li>
 *     <li>a request naming a different external source is refused with OMAG-REPOSITORY-HANDLER-400-007
 *     (WRONG_EXTERNAL_SOURCE);</li>
 *     <li>a request naming the matching external source succeeds.</li>
 * </ul>
 * An element created locally (no external source) has local cohort provenance and can be changed by any caller,
 * with or without an external source.  This is the case of the journal entries that pyegeria deletes: they are
 * created locally by My Profile, so their delete needs no external source.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class OptionsWriteExternalSourceFVT
{
    private static final String LOCAL_CANNOT_CHANGE_EXTERNAL = "OMAG-REPOSITORY-HANDLER-400-008";
    private static final String WRONG_EXTERNAL_SOURCE        = "OMAG-REPOSITORY-HANDLER-400-007";

    private static final List<String> created         = new ArrayList<>();
    private static final List<String> createdExternal = new ArrayList<>();

    private static String externalSourceGUID = null;
    private static String externalSourceName = null;
    private static String otherSourceGUID    = null;
    private static String otherSourceName    = null;


    /**
     * Register two external sources: software capabilities created through Asset Maker.  The external source name
     * is the capability's qualified name.
     */
    @BeforeAll
    static void registerExternalSources()
    {
        SoftwareCapabilityProperties source = named(new SoftwareCapabilityProperties(), "SoftwareCapability", "external-source");

        externalSourceName = source.getQualifiedName();
        externalSourceGUID = record(assetMaker.create("/software-capabilities", newElement(source)), created);

        SoftwareCapabilityProperties other = named(new SoftwareCapabilityProperties(), "SoftwareCapability", "other-external-source");

        otherSourceName = other.getQualifiedName();
        otherSourceGUID = record(assetMaker.create("/software-capabilities", newElement(other)), created);
    }


    /**
     * An element created with an external source records it as its home: origin category EXTERNAL_SOURCE, with
     * the source's GUID and name as the home metadata collection.
     */
    @Test
    void createWithExternalSourceSetsProvenance()
    {
        String guid = createExternalFile("provenance");

        ElementOrigin origin = raw(guid).getOrigin();

        assertNotNull(origin, "the element has no origin");
        assertEquals(ElementOriginCategory.EXTERNAL_SOURCE, origin.getOriginCategory());
        assertEquals(externalSourceGUID, origin.getHomeMetadataCollectionId());
        assertEquals(externalSourceName, origin.getHomeMetadataCollectionName());

        ElementOrigin localOrigin = raw(createAsset(dataFile("local-provenance"), created)).getOrigin();

        assertEquals(ElementOriginCategory.LOCAL_COHORT, localOrigin.getOriginCategory(), "an element created with no external source is not local");
    }


    /**
     * An update of an externally owned element: refused with no external source (400-008) and with the wrong one
     * (400-007), leaving the element unchanged; accepted with the matching one.
     */
    @Test
    void updateOfExternalElementNeedsTheMatchingSource()
    {
        String guid = createExternalFile("update");
        String path = "/assets/" + guid + "/update";

        assertRefused(assetMaker, path, update("Local caller update", null, null), LOCAL_CANNOT_CHANGE_EXTERNAL);
        assertRefused(assetMaker, path, update("Wrong source update", otherSourceGUID, otherSourceName), WRONG_EXTERNAL_SOURCE);

        assertEquals("Description of external-update", string(raw(guid), OpenMetadataProperty.DESCRIPTION.name),
                     "a refused update changed the element");

        assetMaker.post(path, update("Owner update", externalSourceGUID, externalSourceName), BooleanResponse.class);

        assertEquals("Owner update", string(raw(guid), OpenMetadataProperty.DESCRIPTION.name), "the owner's update was not made");
    }


    /**
     * A delete of an externally owned element: refused with no external source (400-008) and with the wrong one
     * (400-007), leaving the element in place; accepted with the matching one, after which it is gone.
     */
    @Test
    void deleteOfExternalElementNeedsTheMatchingSource()
    {
        String guid = createExternalFile("delete");
        String path = "/assets/" + guid + "/delete";

        assertRefused(assetMaker, path, delete(null, null, null), LOCAL_CANNOT_CHANGE_EXTERNAL);
        assertNotNull(rawOrNull(guid, new GetRequestBody()), "the delete with no external source removed the element");

        assertRefused(assetMaker, path, delete(otherSourceGUID, otherSourceName, null), WRONG_EXTERNAL_SOURCE);
        assertNotNull(rawOrNull(guid, new GetRequestBody()), "the delete with the wrong external source removed the element");

        assetMaker.call(path, delete(externalSourceGUID, externalSourceName, null));

        assertNull(rawOrNull(guid, new GetRequestBody()), "the owner's delete did not remove the element");
    }


    /**
     * A purge is a delete too: once the owner has soft-deleted an externally owned element, a local caller cannot
     * purge it (400-008), nor can a different external source (400-007); the owner can.  The element is
     * soft-deleted first so that the purge works on any repository (see OptionsWriteDeleteFVT for PURGE of a
     * live element).
     */
    @Test
    void purgeOfExternalElementNeedsTheMatchingSource()
    {
        String guid = createExternalFile("purge");
        String path = "/assets/" + guid + "/delete";

        assetMaker.call(path, delete(externalSourceGUID, externalSourceName, DeleteMethod.SOFT_DELETE));

        assertRefused(assetMaker, path, delete(null, null, DeleteMethod.PURGE), LOCAL_CANNOT_CHANGE_EXTERNAL);
        assertRefused(assetMaker, path, delete(otherSourceGUID, otherSourceName, DeleteMethod.PURGE), WRONG_EXTERNAL_SOURCE);

        assetMaker.call(path, delete(externalSourceGUID, externalSourceName, DeleteMethod.PURGE));
    }


    /**
     * A locally created element (local cohort provenance) is not protected by the provenance check: it can be
     * updated and deleted by a local caller - the journal entry case - and by a caller naming an external source.
     */
    @Test
    void localElementNeedsNoExternalSource()
    {
        String guid = createAsset(dataFile("local-update-delete"), created);

        assetMaker.post("/assets/" + guid + "/update", update("Local update", null, null), BooleanResponse.class);
        assetMaker.post("/assets/" + guid + "/update", update("Update naming a source", externalSourceGUID, externalSourceName), BooleanResponse.class);

        assertEquals("Update naming a source", string(raw(guid), OpenMetadataProperty.DESCRIPTION.name));

        assetMaker.call("/assets/" + guid + "/delete", delete(null, null, null));

        assertNull(rawOrNull(guid, new GetRequestBody()), "a local caller could not delete a locally created element");
    }


    private static String createExternalFile(String name)
    {
        NewElementRequestBody requestBody = newElement(dataFile("external-" + name));

        requestBody.setExternalSourceGUID(externalSourceGUID);
        requestBody.setExternalSourceName(externalSourceName);

        return record(assetMaker.create("/assets", requestBody), createdExternal);
    }


    private static NewElementRequestBody newElement(org.odpi.openmetadata.frameworks.openmetadata.properties.ReferenceableProperties properties)
    {
        NewElementRequestBody requestBody = new NewElementRequestBody();

        requestBody.setIsOwnAnchor(true);
        requestBody.setProperties(properties);

        return requestBody;
    }


    private static UpdateElementRequestBody update(String description,
                                                   String sourceGUID,
                                                   String sourceName)
    {
        DataFileProperties properties = new DataFileProperties();

        properties.setDescription(description);

        UpdateElementRequestBody requestBody = new UpdateElementRequestBody();

        requestBody.setProperties(properties);
        requestBody.setExternalSourceGUID(sourceGUID);
        requestBody.setExternalSourceName(sourceName);

        return requestBody;
    }


    private static DeleteElementRequestBody delete(String       sourceGUID,
                                                   String       sourceName,
                                                   DeleteMethod deleteMethod)
    {
        DeleteElementRequestBody requestBody = new DeleteElementRequestBody();

        requestBody.setExternalSourceGUID(sourceGUID);
        requestBody.setExternalSourceName(sourceName);

        if (deleteMethod != null)
        {
            requestBody.setDeleteMethod(deleteMethod);
        }

        return requestBody;
    }


    @AfterAll
    static void cleanUp()
    {
        OptionsWriteSupport.purgeAll(createdExternal, externalSourceGUID, externalSourceName);
        OptionsWriteSupport.purgeAll(created);
    }
}
