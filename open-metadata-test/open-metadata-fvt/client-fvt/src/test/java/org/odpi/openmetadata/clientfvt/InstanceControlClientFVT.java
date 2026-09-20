/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.clientfvt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataTypesClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * InstanceControlClientFVT covers the six operations that change an instance's <b>control information</b>
 * rather than its properties: re-identify (change its unique identifier), re-type (change its type) and
 * re-home (change which repository masters it), for both metadata elements and relationships.
 * <br><br>
 * These have always existed on the repository interface.  What is new - and what this suite exercises - is
 * the path that carries them up through the repository handler, the generic handler, the OMF handler and the
 * OMF client to the connector context's {@code OpenMetadataStore}, which is the same path the Metadata Expert
 * OMVS takes.  Every call here goes through the <b>enterprise</b> repository connector, because that is the
 * connector the access services are given, so the routing decisions that connector makes are under test even
 * though this server's cohort has only its own repository in it.
 * <br><br>
 * Three of the six route to the home repository, in the same way an ordinary property update does.  Re-home
 * does not, and that asymmetry is the point worth remembering: re-homing is a repository <em>claiming a
 * reference copy it holds</em>, so it has to be executed by the repository that is becoming the new home.
 * The repository that is the home today refuses it, because an instance it masters is not a reference copy
 * for it to claim.  {@link #reHomeIsRefusedForALocallyMasteredElement} pins that down, and
 * {@link #reHomeClaimsAContentPackElementForTheLocalRepository} covers the case that is allowed.
 * <br><br>
 * Cohort behaviour - a reference copy propagated from a second repository, then claimed - is the conformance
 * test suite's job and is not duplicated here.  A content pack gives this suite the one kind of
 * non-locally-mastered instance it can obtain without a second server.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class InstanceControlClientFVT
{
    private final PropertyHelper propertyHelper = new PropertyHelper();


    /**
     * Re-identifying an element moves it to the new GUID: the element is retrievable there, keeps its
     * properties, and the old GUID no longer resolves.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void reIdentifyMetadataElementChangesItsGUID() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore    store            = connectorContext.getOpenMetadataStore();

        String qualifiedName = ClientFvtTestSupport.newQualifiedName("ReIdentifyElement");
        String originalGUID  = createCollection(store, qualifiedName);
        String newGUID       = UUID.randomUUID().toString();
        String survivingGUID = originalGUID;

        try
        {
            store.reIdentifyMetadataElementInStore(originalGUID, new MetadataSourceOptions(), newGUID);
            survivingGUID = newGUID;

            OpenMetadataElement reIdentified = store.getMetadataElementByGUID(newGUID);

            assertNotNull(reIdentified, "The element could not be read back at its new GUID after re-identification");
            assertEquals(newGUID, reIdentified.getElementGUID(), "The element came back with a GUID that is neither the old nor the new one");
            assertEquals(qualifiedName,
                         qualifiedNameOf(reIdentified),
                         "Re-identifying the element disturbed its properties - only its GUID should have changed");

            /*
             * A GUID that no longer names anything is reported as an invalid parameter (a 404), not as an
             * empty result, so "it is really gone" has to be asserted as a refusal.
             */
            assertThrows(InvalidParameterException.class,
                         () -> store.getMetadataElementByGUID(originalGUID),
                         "The element is still retrievable at its original GUID after being re-identified");
        }
        finally
        {
            ClientFvtTestSupport.purgeElement(store, survivingGUID);
        }
    }


    /**
     * Re-typing an element moves it to the new type and leaves its properties alone.  The new type is the
     * supertype of the type the element was created with, read from the running server rather than named
     * here, so this test follows the model rather than having to be updated alongside it.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void reTypeMetadataElementChangesItsType() throws Exception
    {
        ConnectorContextBase    connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore       store            = connectorContext.getOpenMetadataStore();
        OpenMetadataTypesClient typesClient      = connectorContext.getOpenMetadataTypesClient();

        String startingTypeName = OpenMetadataType.ROOT_COLLECTION.typeName;
        String newTypeName      = superTypeOf(typesClient, startingTypeName);

        String qualifiedName = ClientFvtTestSupport.newQualifiedName("ReTypeElement");
        String elementGUID   = createCollection(store, startingTypeName, qualifiedName);

        try
        {
            store.reTypeMetadataElementInStore(elementGUID, new MetadataSourceOptions(), newTypeName);

            OpenMetadataElement reTyped = store.getMetadataElementByGUID(elementGUID);

            assertNotNull(reTyped, "The element could not be read back after being re-typed");
            assertNotNull(reTyped.getType(), "The re-typed element came back with no type at all");
            assertEquals(newTypeName,
                         reTyped.getType().getTypeName(),
                         "The element still reports its original type after being re-typed");
            assertEquals(qualifiedName,
                         qualifiedNameOf(reTyped),
                         "Re-typing the element disturbed its properties - only its type should have changed");
        }
        finally
        {
            ClientFvtTestSupport.purgeElement(store, elementGUID);
        }
    }


    /**
     * Re-identifying a relationship moves it to the new GUID, and the old GUID stops resolving.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void reIdentifyRelationshipChangesItsGUID() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore    store            = connectorContext.getOpenMetadataStore();

        String end1GUID = createCollection(store, ClientFvtTestSupport.newQualifiedName("ReIdentifyRelationshipEnd1"));
        String end2GUID = createCollection(store, ClientFvtTestSupport.newQualifiedName("ReIdentifyRelationshipEnd2"));

        String originalGUID  = store.createRelatedElementsInStore(OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                                                  end1GUID,
                                                                  end2GUID,
                                                                  null,
                                                                  null);
        String newGUID       = UUID.randomUUID().toString();
        String survivingGUID = originalGUID;

        try
        {
            store.reIdentifyRelationshipInStore(originalGUID, new MetadataSourceOptions(), newGUID);
            survivingGUID = newGUID;

            OpenMetadataRelationship reIdentified = store.getRelationshipByGUID(newGUID);

            assertNotNull(reIdentified, "The relationship could not be read back at its new GUID after re-identification");
            assertEquals(newGUID, reIdentified.getRelationshipGUID(), "The relationship came back with an unexpected GUID");
            assertEquals(end1GUID, reIdentified.getElementGUIDAtEnd1(), "Re-identifying the relationship moved its end 1");
            assertEquals(end2GUID, reIdentified.getElementGUIDAtEnd2(), "Re-identifying the relationship moved its end 2");

            assertThrows(InvalidParameterException.class,
                         () -> store.getRelationshipByGUID(originalGUID),
                         "The relationship is still retrievable at its original GUID after being re-identified");
        }
        finally
        {
            ClientFvtTestSupport.purgeRelationship(store, survivingGUID);
            ClientFvtTestSupport.purgeElement(store, end1GUID);
            ClientFvtTestSupport.purgeElement(store, end2GUID);
        }
    }


    /**
     * Re-typing a relationship moves it to the new type while both ends stay put.  As with the element
     * case, the new type is the starting type's supertype read from the running server.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void reTypeRelationshipChangesItsType() throws Exception
    {
        ConnectorContextBase    connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore       store            = connectorContext.getOpenMetadataStore();
        OpenMetadataTypesClient typesClient      = connectorContext.getOpenMetadataTypesClient();

        String startingTypeName = OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName;
        String newTypeName      = superTypeOf(typesClient, startingTypeName);

        String end1GUID = createCollection(store, ClientFvtTestSupport.newQualifiedName("ReTypeRelationshipEnd1"));
        String end2GUID = createCollection(store, ClientFvtTestSupport.newQualifiedName("ReTypeRelationshipEnd2"));

        String relationshipGUID = store.createRelatedElementsInStore(startingTypeName, end1GUID, end2GUID, null, null);

        try
        {
            store.reTypeRelationshipInStore(relationshipGUID, new MetadataSourceOptions(), newTypeName);

            OpenMetadataRelationship reTyped = store.getRelationshipByGUID(relationshipGUID);

            assertNotNull(reTyped, "The relationship could not be read back after being re-typed");
            assertNotNull(reTyped.getRelationshipType(), "The re-typed relationship came back with no type at all");
            assertEquals(newTypeName,
                         reTyped.getRelationshipType().getTypeName(),
                         "The relationship still reports its original type after being re-typed");
            assertEquals(end1GUID, reTyped.getElementGUIDAtEnd1(), "Re-typing the relationship moved its end 1");
            assertEquals(end2GUID, reTyped.getElementGUIDAtEnd2(), "Re-typing the relationship moved its end 2");
        }
        finally
        {
            ClientFvtTestSupport.purgeRelationship(store, relationshipGUID);
            ClientFvtTestSupport.purgeElement(store, end1GUID);
            ClientFvtTestSupport.purgeElement(store, end2GUID);
        }
    }


    /**
     * The local repository takes ownership of a content pack element.
     * <br><br>
     * A content pack is the one source of a non-locally-mastered instance this suite can reach without a
     * second server in a cohort: the elements loaded from {@code CoreContentPack.omarchive} are homed in the
     * archive's own metadata collection, not this server's, so the local repository holds them as reference
     * copies and re-homing them to itself is exactly the operation re-home exists for.
     * <br><br>
     * The claimed element is purged afterwards rather than left behind.  Once it is locally mastered it can
     * be removed, and the next run reloads it from the content pack at server startup, so the repository is
     * back where it started.  Re-homing it back is not an option - the local repository only accepts a
     * re-home request that names <em>itself</em> as the new home.
     * <br><br>
     * This test is the reason {@code validateEntityCanBeRehomed} no longer reads {@code replicatedBy} as
     * ownership.  It used to allow a content pack element to be claimed only when {@code replicatedBy} was
     * set and was <em>not</em> the local repository - but {@code OMRSArchiveManager} sets
     * {@code replicatedBy} to the loading repository, deliberately, so that the content pack is broadcast to
     * the rest of the cohort and counted correctly.  That made a content pack impossible to re-home into the
     * one repository most likely to want it, which is the repository that loaded it.  Replicating an
     * instance and mastering it are separate roles, and only the second one blocks a re-home.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void reHomeClaimsAContentPackElementForTheLocalRepository() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore    store            = connectorContext.getOpenMetadataStore();

        String localCollectionId = localMetadataCollectionId(store);

        OpenMetadataElement contentPackElement = findContentPackElement(store, localCollectionId);

        assertNotNull(contentPackElement,
                      "No element homed outside this server's own repository could be found, so there is nothing to" +
                              " re-home.  This suite loads CoreContentPack.omarchive at startup precisely so that" +
                              " there is - check the archive actually loaded.");

        String claimedGUID = contentPackElement.getElementGUID();

        try
        {
            store.reHomeMetadataElementInStore(claimedGUID,
                                                new MetadataSourceOptions(),
                                                localCollectionId,
                                                "client-fvt local repository");

            OpenMetadataElement claimed = store.getMetadataElementByGUID(claimedGUID);

            assertNotNull(claimed, "The element could not be read back after being re-homed");
            assertNotNull(claimed.getOrigin(), "The re-homed element came back with no origin information");
            assertEquals(localCollectionId,
                         claimed.getOrigin().getHomeMetadataCollectionId(),
                         "The element still reports its original home metadata collection after being re-homed");
        }
        finally
        {
            /*
             * Cascade is deliberately off: this element came from the content pack and may be the anchor for
             * others that this test has no business removing.
             */
            purgeWithoutCascade(store, claimedGUID);
        }
    }


    /**
     * Re-homing an instance the local repository already masters is refused.
     * <br><br>
     * This is the asymmetry that makes re-home different from its two siblings, and it is worth a test of its
     * own rather than a comment: an implementation that routed re-home to the current home repository - the
     * obvious thing to do, since that is right for re-identify and re-type - would fail every re-home call
     * with exactly this exception, and nothing else in the suite would notice.
     *
     * @throws Exception any failure - which is the finding
     */
    @Test
    void reHomeIsRefusedForALocallyMasteredElement() throws Exception
    {
        ConnectorContextBase connectorContext = ConnectorContextFactory.newContext();
        OpenMetadataStore    store            = connectorContext.getOpenMetadataStore();

        String elementGUID       = createCollection(store, ClientFvtTestSupport.newQualifiedName("ReHomeRefused"));
        String localCollectionId = localMetadataCollectionId(store);

        try
        {
            assertThrows(InvalidParameterException.class,
                         () -> store.reHomeMetadataElementInStore(elementGUID,
                                                                   new MetadataSourceOptions(),
                                                                   localCollectionId,
                                                                   "client-fvt local repository"),
                         "The repository allowed an element it already masters to be re-homed to itself.  Re-homing" +
                                 " claims a reference copy, and an instance this repository masters is not one.");
        }
        finally
        {
            ClientFvtTestSupport.purgeElement(store, elementGUID);
        }
    }


    /**
     * Create a Collection carrying just a qualified name.
     *
     * @param store store to create through
     * @param qualifiedName qualified name for the new element
     * @return unique identifier of the new element
     * @throws Exception the element could not be created
     */
    private String createCollection(OpenMetadataStore store,
                                    String            qualifiedName) throws Exception
    {
        return createCollection(store, OpenMetadataType.COLLECTION.typeName, qualifiedName);
    }


    /**
     * Create an element of the requested type carrying just a qualified name.  Only the qualified name is
     * set, so that a re-type to a supertype cannot fail because of a property the supertype does not declare.
     *
     * @param store store to create through
     * @param typeName type of element to create
     * @param qualifiedName qualified name for the new element
     * @return unique identifier of the new element
     * @throws Exception the element could not be created
     */
    private String createCollection(OpenMetadataStore store,
                                    String            typeName,
                                    String            qualifiedName) throws Exception
    {
        NewElementOptions newElementOptions = new NewElementOptions(store.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        ElementProperties properties = propertyHelper.addStringProperty(null,
                                                                        OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                        qualifiedName);

        String elementGUID = store.createMetadataElementInStore(typeName,
                                                                 newElementOptions,
                                                                 null,
                                                                 new NewElementProperties(properties),
                                                                 null);

        assertNotNull(elementGUID, "Creating the " + typeName + " this test works with returned no GUID");

        return elementGUID;
    }


    /**
     * Return the qualified name an element came back with.
     *
     * @param element element to read
     * @return qualified name, or null if it has none
     */
    private String qualifiedNameOf(OpenMetadataElement element)
    {
        return propertyHelper.getStringProperty("client-fvt",
                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                 element.getElementProperties(),
                                                 "qualifiedNameOf");
    }


    /**
     * Ask the running server for the supertype of a type, rather than naming it here.  A test that hard-codes
     * a type pair has to be revisited every time the model moves; this one follows it.
     *
     * @param typesClient types client from the connector context
     * @param typeName type whose supertype is wanted
     * @return name of the supertype
     * @throws Exception the type could not be read
     */
    private String superTypeOf(OpenMetadataTypesClient typesClient,
                               String                  typeName) throws Exception
    {
        OpenMetadataTypeDef typeDef = typesClient.getTypeDefByName(false, false, typeName);

        assertNotNull(typeDef, typeName + " is not a type this server knows about");
        assertNotNull(typeDef.getSuperType(),
                      typeName + " no longer has a supertype, so there is nothing to re-type it to." +
                              "  Pick another type for this test.");

        return typeDef.getSuperType().getName();
    }


    /**
     * Return the metadata collection identifier of this server's own repository, taken from an element the
     * suite creates itself - a locally created element is by definition homed locally.
     *
     * @param store store to work through
     * @return local metadata collection identifier
     * @throws Exception the element could not be created or read
     */
    private String localMetadataCollectionId(OpenMetadataStore store) throws Exception
    {
        String probeGUID = createCollection(store, ClientFvtTestSupport.newQualifiedName("HomeProbe"));

        try
        {
            OpenMetadataElement probe = store.getMetadataElementByGUID(probeGUID);

            assertNotNull(probe, "The probe element could not be read back");
            assertNotNull(probe.getOrigin(), "The probe element came back with no origin information");

            String localCollectionId = probe.getOrigin().getHomeMetadataCollectionId();

            assertNotNull(localCollectionId, "An element created by this suite reports no home metadata collection");

            return localCollectionId;
        }
        finally
        {
            ClientFvtTestSupport.purgeElement(store, probeGUID);
        }
    }


    /**
     * Find an element that this repository holds but does not master - which, on this server, means one that
     * came from the content pack loaded at startup.
     *
     * @param store store to search through
     * @param localCollectionId this repository's own metadata collection identifier
     * @return an element homed elsewhere, or null if there is none
     * @throws Exception the search failed
     */
    private OpenMetadataElement findContentPackElement(OpenMetadataStore store,
                                                       String            localCollectionId) throws Exception
    {
        List<OpenMetadataElement> candidates = store.findMetadataElements(OpenMetadataType.VALID_VALUE_DEFINITION.typeName,
                                                                           null,
                                                                           null,
                                                                           null,
                                                                           0,
                                                                           ClientFvtTestSupport.MAX_PAGE_SIZE);

        if (candidates != null)
        {
            for (OpenMetadataElement candidate : candidates)
            {
                if ((candidate.getOrigin() != null)
                            && (candidate.getOrigin().getHomeMetadataCollectionId() != null)
                            && (! localCollectionId.equals(candidate.getOrigin().getHomeMetadataCollectionId())))
                {
                    assertNotEquals(localCollectionId,
                                    candidate.getOrigin().getHomeMetadataCollectionId(),
                                    "Picked an element this repository masters as the re-home candidate");

                    return candidate;
                }
            }
        }

        return null;
    }


    /**
     * Soft-delete then purge a single element, without cascading into anything anchored to it.
     *
     * @param store store to delete through
     * @param elementGUID element to remove
     */
    private void purgeWithoutCascade(OpenMetadataStore store,
                                     String            elementGUID)
    {
        try
        {
            DeleteOptions softDeleteOptions = new DeleteOptions();

            softDeleteOptions.setDeleteMethod(DeleteMethod.SOFT_DELETE);
            softDeleteOptions.setForLineage(true);

            store.deleteMetadataElementInStore(elementGUID, softDeleteOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort cleanup.
        }

        try
        {
            DeleteOptions purgeOptions = new DeleteOptions();

            purgeOptions.setDeleteMethod(DeleteMethod.PURGE);
            purgeOptions.setForLineage(true);

            store.deleteMetadataElementInStore(elementGUID, purgeOptions);
        }
        catch (Exception ignored)
        {
            // Best-effort cleanup - the next run reloads the content pack anyway.
        }
    }
}
