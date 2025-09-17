/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.mapper.OpenConnectorsValidValues;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.openmetadata.enums.*;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.AssignmentType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.ResourceUse;
import org.odpi.openmetadata.frameworks.integration.controls.CatalogTargetType;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorProvider;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveGUIDMap;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;

import java.util.*;

/**
 * SimpleCatalogArchiveHelper creates elements used when creating a simple catalog.  This includes assets, their schemas and connections, design models.
 */
public class SimpleCatalogArchiveHelper
{
    protected static final String guidMapFileNamePostFix    = "GUIDMap.json";


    protected OpenMetadataArchiveBuilder archiveBuilder;
    protected OMRSArchiveHelper          archiveHelper;
    protected OMRSArchiveGUIDMap         idToGUIDMap;

    protected String             archiveRootName;
    protected String             originatorName;
    protected String             versionName;


    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     archiveRootName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName)
    {
        this(archiveBuilder,
             archiveGUID,
             archiveName,
             originatorName,
             creationDate,
             versionNumber,
             versionName,
             archiveRootName + guidMapFileNamePostFix);
    }


    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param instanceProvenanceType type of archive.
     * @param license license for the archive contents.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     archiveRootName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName,
                                      InstanceProvenanceType     instanceProvenanceType,
                                      String                     license)
    {
        this(archiveBuilder,
             archiveGUID,
             archiveName,
             originatorName,
             creationDate,
             versionNumber,
             versionName,
             instanceProvenanceType,
             license,
             archiveRootName + guidMapFileNamePostFix);
    }


    /**
     * Constructor passes parameters used to build the open metadata archive's property header.
     * This version is used for multiple dependant archives, and they need to share the guid map.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param guidMapFileName name of the guid map file.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName,
                                      String                     guidMapFileName)
    {
        this(archiveBuilder, archiveGUID, archiveName, originatorName, creationDate, versionNumber, versionName, InstanceProvenanceType.CONTENT_PACK, null, guidMapFileName);
    }


    /**
     * Constructor passes parameters used to build the open metadata archive's property header.
     * This version is used for multiple dependant archives, and they need to share the guid map.
     *
     * @param archiveBuilder builder where content is cached
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive metadata collection.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     * @param instanceProvenanceType type of archive.
     * @param license license for the archive contents.
     * @param guidMapFileName name of the guid map file.
     */
    public SimpleCatalogArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                      String                     archiveGUID,
                                      String                     archiveName,
                                      String                     originatorName,
                                      Date                       creationDate,
                                      long                       versionNumber,
                                      String                     versionName,
                                      InstanceProvenanceType     instanceProvenanceType,
                                      String                     license,
                                      String                     guidMapFileName)
    {
        this.archiveBuilder = archiveBuilder;

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   archiveName,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName,
                                                   instanceProvenanceType,
                                                   license);

        this.idToGUIDMap = new OMRSArchiveGUIDMap(guidMapFileName);

        this.archiveRootName = archiveName;
        this.originatorName = originatorName;
        this.versionName = versionName;
    }


    /**
     * Return the guid of an element based on its qualified name.  This is a lookup in the GUID map not the archive.
     * This means if the qualified name is not known, a new GUID is generated.
     *
     * @param qualifiedName qualified name ot look up
     * @return guid.
     */
    public String getGUID(String qualifiedName)
    {
        return idToGUIDMap.getGUID(qualifiedName);
    }

    /**
     * Return the guid of an element based on its qualified name.  This is a lookup in the GUID map not the archive.
     * This means if the qualified name is not known, a new GUID is generated.
     *
     * @param qualifiedName qualified name ot look up
     * @param guid  fixed unique identifier
     */
    public void setGUID(String qualifiedName,
                        String guid)
    {
        idToGUIDMap.setGUID(qualifiedName, guid);
    }


    /**
     * Return the guid of an element based on its qualified name.  This is a query in the GUID map not the archive.
     * This means if the qualified name is not known, null is returned.
     *
     * @param qualifiedName qualified name ot look up
     * @return guid or null
     */
    public String queryGUID(String qualifiedName)
    {
        return idToGUIDMap.queryGUID(qualifiedName);
    }


    /**
     * Save the GUIDs so that the GUIDs of the elements inside the archive are consistent each time the archive runs.
     */
    public void saveGUIDs()
    {
        System.out.println("GUIDs map size: " + idToGUIDMap.getSize());

        idToGUIDMap.saveGUIDs();
    }


    /**
     * Save the GUIDs so that the GUIDs of the elements inside the archive are consistent each time the archive runs.
     */
    public void saveUsedGUIDs()
    {
        System.out.println("Used GUIDs map size: " + idToGUIDMap.getUsedSize());

        idToGUIDMap.saveUsedGUIDs();
    }


    /**
     * Create an external reference entity.  This typically describes a  webpage link
     * that is from outside open metadata.
     *
     * @param typeName name of element subtype to use - default is ExternalReference
     * @param anchorGUID unique identifier if its anchor (or null)
     * @param anchorTypeName type name of the anchor entity
     * @param anchorDomainName type name of the anchor entity's domain
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param referenceTitle full title from the publication
     * @param referenceAbstract full abstract from the publication
     * @param description description about the element
     * @param authors authors of the element
     * @param referenceURLName name for the URL
     * @param organization organization that the information is from
     * @param versionNumber version number for the element
     * @param referenceURL link to the external source
     * @param license name of the license associated with this external source
     * @param copyright copyright statement associated with this external source
     * @param attribution attribution statement to use when consuming this external source
     * @param searchKeywords list of keywords
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @return unique identifier for new external reference (externalReferenceGUID)
     */
    public String addExternalReference(String               typeName,
                                       String               anchorGUID,
                                       String               anchorTypeName,
                                       String               anchorDomainName,
                                       String               anchorScopeGUID,
                                       String               qualifiedName,
                                       String               displayName,
                                       String               referenceTitle,
                                       String               referenceAbstract,
                                       String               description,
                                       List<String>         authors,
                                       String               referenceURLName,
                                       String               organization,
                                       String               versionNumber,
                                       String               referenceURL,
                                       String               license,
                                       String               copyright,
                                       String               attribution,
                                       List<String>         searchKeywords,
                                       Map<String, String>  additionalProperties,
                                       Map<String, Object>  extendedProperties)
    {
        final String methodName = "addExternalReference";

        String elementTypeName = OpenMetadataType.EXTERNAL_REFERENCE.typeName;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REFERENCE_TITLE.name, referenceTitle, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REFERENCE_ABSTRACT.name, referenceAbstract, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.AUTHORS.name, authors, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ORGANIZATION.name, organization, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.URL.name, referenceURL, methodName);
        if (referenceURL != null)
        {
            String urlName = referenceURLName;

            if (urlName == null)
            {
                urlName = "wikiLink";
            }

            Map<String, String> urls = new HashMap<>();

            urls.put(urlName, referenceURL);

            properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SOURCES.name, urls, methodName);
        }
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.LICENSE.name, license, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.COPYRIGHT.name, copyright, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ATTRIBUTION.name, attribution, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = null;

        if (anchorGUID != null)
        {
            classifications = new ArrayList<>();

            classifications.add(getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        EntityDetail externalReferenceEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                                             idToGUIDMap.getGUID(qualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

        archiveBuilder.addEntity(externalReferenceEntity);

        if (searchKeywords != null)
        {
            for (String keyword : searchKeywords)
            {
                if (keyword != null)
                {
                    String keywordGUID = idToGUIDMap.queryGUID(OpenMetadataType.SEARCH_KEYWORD.typeName + "::" + keyword);
                    EntityDetail keywordEntity = null;

                    InstanceProperties keywordProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.KEYWORD.name, keyword, methodName);

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.queryEntity(keywordGUID);
                    }

                    if (keywordEntity == null)
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(OpenMetadataType.SEARCH_KEYWORD.typeName,
                                                                       idToGUIDMap.getGUID(OpenMetadataType.SEARCH_KEYWORD.typeName + "::" + keyword),
                                                                       keywordProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                        archiveBuilder.addEntity(keywordEntity);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(externalReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName,
                                                                                     idToGUIDMap.getGUID(externalReferenceEntity.getGUID() + "_to_" + keywordEntity.getGUID() + "_search_keyword_link_relationship"),
                                                                                     null,
                                                                                     InstanceStatus.ACTIVE,
                                                                                     end1,
                                                                                     end2));
                    }
                }
            }
        }

        return externalReferenceEntity.getGUID();
    }


    /**
     * Create a cited document entity.  This typically describes a publication, webpage book or reference source of information
     * that is from an external organization.
     *
     * @param typeName name of element subtype to use - default is ExternalReference
     * @param anchorGUID unique identifier if its anchor (or null)
     * @param anchorTypeName type name of the anchor entity
     * @param anchorDomainName type name of the anchor entity's domain
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param referenceTitle full title from the publication
     * @param referenceAbstract full abstract from the publication
     * @param description description about the element
     * @param authors authors of the element
     * @param numberOfPages number of pages in the external source
     * @param pageRange range of pages that is significant
     * @param authorOrganization organization that the information is from
     * @param publicationSeries publication series or journal that the external source is from.
     * @param publicationSeriesVolume volume of the publication series where the external source is found
     * @param edition edition where the external source is from
     * @param versionNumber version number for the element
     * @param referenceURL link to the external source
     * @param publisher publisher of the external source
     * @param firstPublicationDate date this material was first published (ie first version's publication date)
     * @param publicationDate date that this version was published
     * @param publicationCity city that the publisher operates from
     * @param publicationYear year that this version was published
     * @param publicationNumbers list of ISBNs for this external source
     * @param license name of the license associated with this external source
     * @param copyright copyright statement associated with this external source
     * @param attribution attribution statement to use when consuming this external source
     * @param searchKeywords list of keywords
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @return unique identifier for new external reference (externalReferenceGUID)
     */
    public String addCitedDocument(String               typeName,
                                       String               anchorGUID,
                                       String               anchorTypeName,
                                       String               anchorDomainName,
                                       String               anchorScopeGUID,
                                       String               qualifiedName,
                                       String               displayName,
                                       String               referenceTitle,
                                       String               referenceAbstract,
                                       String               description,
                                       List<String>         authors,
                                       int                  numberOfPages,
                                       String               pageRange,
                                       String               authorOrganization,
                                       String               publicationSeries,
                                       String               publicationSeriesVolume,
                                       String               edition,
                                       String               versionNumber,
                                       String               referenceURL,
                                       String               publisher,
                                       Date                 firstPublicationDate,
                                       Date                 publicationDate,
                                       String               publicationCity,
                                       String               publicationYear,
                                       List<String>         publicationNumbers,
                                       String               license,
                                       String               copyright,
                                       String               attribution,
                                       List<String>         searchKeywords,
                                       Map<String, String>  additionalProperties,
                                       Map<String, Object>  extendedProperties)
    {
        final String methodName = "addCitedDocument";

        String elementTypeName = OpenMetadataType.CITED_DOCUMENT.typeName;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REFERENCE_TITLE.name, referenceTitle, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REFERENCE_ABSTRACT.name, referenceAbstract, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.AUTHORS.name, authors, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NUMBER_OF_PAGES.name, numberOfPages, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PAGE_RANGE.name, pageRange, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ORGANIZATION.name, authorOrganization, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PUBLICATION_SERIES.name, publicationSeries, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PUBLICATION_SERIES_VOLUME.name, publicationSeriesVolume, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.EDITION.name, edition, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.URL.name, referenceURL, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PUBLISHER.name, publisher, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataProperty.FIRST_PUB_DATE.name, firstPublicationDate, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PUBLICATION_DATE.name, publicationDate, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PUBLICATION_CITY.name, publicationCity, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PUBLICATION_YEAR.name, publicationYear, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PUBLICATION_NUMBERS.name, publicationNumbers, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.LICENSE.name, license, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.COPYRIGHT.name, copyright, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ATTRIBUTION.name, attribution, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = null;

        if (anchorGUID != null)
        {
            classifications = new ArrayList<>();

            classifications.add(getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        EntityDetail externalReferenceEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                                             idToGUIDMap.getGUID(qualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

        archiveBuilder.addEntity(externalReferenceEntity);

        if (searchKeywords != null)
        {
            for (String keyword : searchKeywords)
            {
                if (keyword != null)
                {
                    String keywordGUID = idToGUIDMap.queryGUID(OpenMetadataType.SEARCH_KEYWORD.typeName + "::" + keyword);
                    EntityDetail keywordEntity = null;

                    InstanceProperties keywordProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.KEYWORD.name, keyword, methodName);

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.queryEntity(keywordGUID);
                    }

                    if (keywordEntity == null)
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(OpenMetadataType.SEARCH_KEYWORD.typeName,
                                                                       idToGUIDMap.getGUID(OpenMetadataType.SEARCH_KEYWORD.typeName + "::" + keyword),
                                                                       keywordProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                        archiveBuilder.addEntity(keywordEntity);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(externalReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName,
                                                                                     idToGUIDMap.getGUID(externalReferenceEntity.getGUID() + "_to_" + keywordEntity.getGUID() + "_search_keyword_link_relationship"),
                                                                                     null,
                                                                                     InstanceStatus.ACTIVE,
                                                                                     end1,
                                                                                     end2));
                    }
                }
            }
        }

        return externalReferenceEntity.getGUID();
    }


    /**
     * Create the relationship between a referenceable and an external reference.
     *
     * @param referenceableGUID unique identifier of the element making the reference
     * @param externalReferenceGUID unique identifier of the external reference
     * @param referenceId unique reference id for this referenceable
     * @param description description of the relevance of the external reference
     * @param pages relevant pages in the external reference
     */
    public void addExternalReferenceLink(String referenceableGUID,
                                         String externalReferenceGUID,
                                         String referenceId,
                                         String description,
                                         String pages)
    {
        final String methodName = "addExternalReferenceLink";

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(externalReferenceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.REFERENCE_ID.name, referenceId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PAGES.name, pages, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + externalReferenceGUID + "_external_reference_link_relationship" + referenceId),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a media reference entity.  This typically describes an image, audio clip or video clip.
     *
     * @param typeName name of element subtype to use - default is RelatedMedia
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param description description about the element
     * @param authors authors of the element
     * @param authorOrganization organization that the information is from
     * @param versionNumber version number for the element
     * @param referenceURL link to the external source
     * @param license name of the license associated with this external source
     * @param copyright copyright statement associated with this external source
     * @param attribution attribution statement to use when consuming this external source
     * @param mediaType type of media
     * @param mediaTypeOtherId if media type is "other" add type here
     * @param defaultMediaUsage usage of media if not supplied on media reference link relationship
     * @param defaultMediaUsageOtherId if default media usage is "other" add usage type here
     * @param searchKeywords list of keywords
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @return unique identifier for new media reference (mediaReferenceGUID)
     */
    public String addMediaReference(String               typeName,
                                    String               qualifiedName,
                                    String               displayName,
                                    String               description,
                                    List<String>         authors,
                                    String               authorOrganization,
                                    String               versionNumber,
                                    String               referenceURL,
                                    String               license,
                                    String               copyright,
                                    String               attribution,
                                    int                  mediaType,
                                    String               mediaTypeOtherId,
                                    int                  defaultMediaUsage,
                                    String               defaultMediaUsageOtherId,
                                    List<String>         searchKeywords,
                                    Map<String, String>  additionalProperties,
                                    Map<String, Object>  extendedProperties)
    {
        final String methodName = "addMediaReference";

        String elementTypeName = OpenMetadataType.RELATED_MEDIA.typeName;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        EnumElementDef typeEnumElement = archiveHelper.getEnumElement(MediaType.getOpenTypeName(), mediaType);
        EnumElementDef usageEnumElement = archiveHelper.getEnumElement(MediaUsage.getOpenTypeName(), defaultMediaUsage);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.AUTHORS.name, authors, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ORGANIZATION.name, authorOrganization, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.URL.name, referenceURL, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.LICENSE.name, license, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.COPYRIGHT.name, copyright, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ATTRIBUTION.name, attribution, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MEDIA_TYPE.name, MediaType.getOpenTypeGUID(), MediaType.getOpenTypeName(), typeEnumElement.getOrdinal(), typeEnumElement.getValue(), typeEnumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MEDIA_TYPE_OTHER_ID.name, mediaTypeOtherId, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEFAULT_MEDIA_USAGE.name, MediaUsage.getOpenTypeGUID(), MediaUsage.getOpenTypeName(), usageEnumElement.getOrdinal(), usageEnumElement.getValue(), usageEnumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEFAULT_MEDIA_USAGE_OTHER_ID.name, defaultMediaUsageOtherId, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail mediaReferenceEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                                          idToGUIDMap.getGUID(qualifiedName),
                                                                          properties,
                                                                          InstanceStatus.ACTIVE,
                                                                          null);

        archiveBuilder.addEntity(mediaReferenceEntity);

        if (searchKeywords != null)
        {
            for (String keyword : searchKeywords)
            {
                if (keyword != null)
                {
                    String keywordGUID = idToGUIDMap.queryGUID(OpenMetadataType.SEARCH_KEYWORD.typeName + "::" + keyword);
                    EntityDetail keywordEntity = null;

                    InstanceProperties keywordProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.KEYWORD.name, keyword, methodName);

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.queryEntity(keywordGUID);
                    }

                    if (keywordEntity == null)
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(OpenMetadataType.SEARCH_KEYWORD.typeName,
                                                                       idToGUIDMap.getGUID(OpenMetadataType.SEARCH_KEYWORD.typeName + "::" + keyword),
                                                                       keywordProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                        archiveBuilder.addEntity(keywordEntity);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(mediaReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SEARCH_KEYWORD_LINK_RELATIONSHIP.typeName,
                                                                                     idToGUIDMap.getGUID(mediaReferenceEntity.getGUID() + "_to_" + keywordEntity.getGUID() + "_search_keyword_link_relationship"),
                                                                                     null,
                                                                                     InstanceStatus.ACTIVE,
                                                                                     end1,
                                                                                     end2));
                    }
                }
            }
        }

        return mediaReferenceEntity.getGUID();
    }


    /**
     * Create the relationship between a referenceable and an external reference.
     *
     * @param referenceableGUID unique identifier of the element making the reference
     * @param mediaReferenceGUID unique identifier of the media reference
     * @param mediaId unique reference id for this media element
     * @param description description of the relevance of the media reference
     * @param mediaUsage type of usage
     * @param mediaUsageOtherId other type of media usage (for example if using a valid value set).
     */
    public void addMediaReferenceLink(String referenceableGUID,
                                      String mediaReferenceGUID,
                                      String mediaId,
                                      String description,
                                      int    mediaUsage,
                                      String mediaUsageOtherId)
    {
        final String methodName = "addMediaReferenceLink";

        EnumElementDef enumElement = archiveHelper.getEnumElement(MediaUsage.getOpenTypeName(), mediaUsage);

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(mediaReferenceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.MEDIA_ID.name, mediaId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MEDIA_USAGE.name, MediaUsage.getOpenTypeGUID(), MediaUsage.getOpenTypeName(), enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MEDIA_USAGE_OTHER_ID.name, mediaUsageOtherId, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.MEDIA_REFERENCE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + mediaReferenceGUID + "_media_reference_relationship" + mediaId),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add the DeployedOn relationship to the archive.
     *
     * @param deployedElementQName qualified name of element being deployed
     * @param deployedOnQName qualified name of target
     * @param deploymentTime time of the deployment
     * @param deployerTypeName type name of the element representing the deployer
     * @param deployerPropertyName property name used to identify the deployer
     * @param deployer identifier of the deployer
     * @param deploymentStatus status of the deployment
     */
    public void addDeployedOnRelationship(String deployedElementQName,
                                          String deployedOnQName,
                                          Date   deploymentTime,
                                          String deployerTypeName,
                                          String deployerPropertyName,
                                          String deployer,
                                          int    deploymentStatus)
    {
        final String methodName = "addDeployedOnRelationship";

        String deployedElementId = this.idToGUIDMap.getGUID(deployedElementQName);
        String deployedOnId = this.idToGUIDMap.getGUID(deployedOnQName);

        EntityProxy end1    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedElementId));
        EntityProxy end2    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedOnId));

        EnumElementDef statusEnumElement = archiveHelper.getEnumElement(OperationalStatus.getOpenTypeName(), deploymentStatus);

        InstanceProperties properties = archiveHelper.addDatePropertyToInstance(archiveRootName, null, OpenMetadataProperty.DEPLOYMENT_TIME.name, deploymentTime, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEPLOYER_TYPE_NAME.name, deployerTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEPLOYER_PROPERTY_NAME.name, deployerPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEPLOYER.name, deployer, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.OPERATIONAL_STATUS.name, OperationalStatus.getOpenTypeGUID(), OperationalStatus.getOpenTypeName(), statusEnumElement.getOrdinal(), statusEnumElement.getValue(), statusEnumElement.getDescription(), methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship(OpenMetadataType.DEPLOYED_ON_RELATIONSHIP.typeName, this.idToGUIDMap.getGUID(deployedElementId + "_to_" + deployedOnId + "_deployed_on_relationship"), properties, InstanceStatus.ACTIVE, end1, end2));
    }



    /**
     * Add the ResourceList relationship to the archive.
     *
     * @param parentQName qualified name of resource consumer
     * @param resourceQName qualified name of resource
     * @param resourceUse use descriptor
     * @param resourceUseDescription description of use
     */
    public void addResourceListRelationship(String parentQName,
                                            String resourceQName,
                                            String resourceUse,
                                            String resourceUseDescription)
    {
        this.addResourceListRelationshipByGUID(this.idToGUIDMap.getGUID(parentQName),
                                               this.idToGUIDMap.getGUID(resourceQName),
                                               resourceUse,
                                               resourceUseDescription);
    }


    /**
     * Add the ResourceList relationship to the archive.
     *
     * @param parentGUID unique identifier of resource consumer
     * @param resourceGUID unique identifier of resource
     * @param resourceUse use descriptor
     * @param resourceUseDescription description of use
     */
    public void addResourceListRelationshipByGUID(String parentGUID,
                                                  String resourceGUID,
                                                  String resourceUse,
                                                  String resourceUseDescription)
    {
        final String methodName = "addResourceListRelationshipByGUID";

        EntityProxy end1    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(parentGUID));
        EntityProxy end2    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(resourceGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                  null,
                                                                                  OpenMetadataProperty.RESOURCE_USE.name,
                                                                                  resourceUse,
                                                                                  methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.DESCRIPTION.name,
                                                               resourceUseDescription,
                                                               methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                               this.idToGUIDMap.getGUID(parentGUID + "_to_" + resourceGUID + "_resource_list_relationship"),
                                                                               properties,
                                                                               InstanceStatus.ACTIVE,
                                                                               end1,
                                                                               end2));
    }


    /**
     * Add the ResourceList relationship to the archive.
     *
     * @param parentGUID unique identifier of resource consumer
     * @param resourceGUID unique identifier of resource
     * @param resourceUse description of use
     * @param resourceUseProperties properties
     * @param resourceUseDescription description
     * @param watchResource should watch?
     */
    public void addResourceListRelationshipByGUID(String              parentGUID,
                                                  String              resourceGUID,
                                                  String              resourceUse,
                                                  String              resourceUseDescription,
                                                  Map<String, String> resourceUseProperties,
                                                  boolean             watchResource)
    {
        final String methodName = "addResourceListRelationshipByGUID";

        EntityProxy end1    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(parentGUID));
        EntityProxy end2    = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(resourceGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.RESOURCE_USE.name, resourceUse, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, resourceUseDescription, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, resourceUseProperties, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.WATCH_RESOURCE.name, watchResource, methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship(OpenMetadataType.RESOURCE_LIST_RELATIONSHIP.typeName,
                                                                               this.idToGUIDMap.getGUID(parentGUID + "_to_" + resourceGUID + "_resource_list_relationship"),
                                                                               properties,
                                                                               InstanceStatus.ACTIVE,
                                                                               end1,
                                                                               end2));
    }



    /**
     * Add a location entity to the archive.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param additionalProperties any additional properties
     * @return unique identifier of the location
     */
    public String addLocation(String              qualifiedName,
                              String              identifier,
                              String              displayName,
                              String              description,
                              Map<String, String> additionalProperties)
    {
        final String methodName = "addLocation";

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, null, null, methodName);
    }


    /**
     * Add a location with a FixedLocation classification.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param coordinates location coordinates
     * @param mapProjection type of location coordinates
     * @param postalAddress full postal address
     * @param timeZone timezone of location
     * @param additionalProperties any additional properties
     * @return unique identifier of the new location
     */
    public String addFixedLocation(String              qualifiedName,
                                   String              identifier,
                                   String              displayName,
                                   String              description,
                                   String              coordinates,
                                   String              mapProjection,
                                   String              postalAddress,
                                   String              timeZone,
                                   Map<String, String> additionalProperties)
    {
        final String methodName = "addFixedLocation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.COORDINATES.name, coordinates, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MAP_PROJECTION.name, mapProjection, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.POSTAL_ADDRESS.name, postalAddress, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TIME_ZONE.name, timeZone, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, OpenMetadataType.FIXED_LOCATION_CLASSIFICATION.typeName, properties, methodName);
    }


    /**
     * Add a location with a SecureLocation classification.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param level level of security
     * @param securityDescription description of security provision
     * @param additionalProperties additional properties for the location
     * @return unique identifier of the new location
     */
    public String addSecureLocation(String              qualifiedName,
                                    String              identifier,
                                    String              displayName,
                                    String              description,
                                    String              level,
                                    String              securityDescription,
                                    Map<String, String> additionalProperties)
    {
        final String methodName = "addSecureLocation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.LEVEL.name, level, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, securityDescription, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, OpenMetadataType.SECURE_LOCATION_CLASSIFICATION.typeName, properties, methodName);
    }


    /**
     * Add a location entity with the CyberLocation classification to the archive.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param networkAddress address of the cyber location
     * @param additionalProperties any additional properties
     * @return unique identifier of the location
     */
    public String addCyberLocation(String              qualifiedName,
                                   String              identifier,
                                   String              displayName,
                                   String              description,
                                   String              networkAddress,
                                   Map<String, String> additionalProperties)
    {
        final String methodName = "addCyberLocation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.NETWORK_ADDRESS.name, networkAddress, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, OpenMetadataType.CYBER_LOCATION_CLASSIFICATION.typeName, properties, methodName);
    }


    /**
     * Add a location entity to the archive.
     *
     * @param qualifiedName unique name of the location
     * @param identifier code value or symbol used to identify the location - typically unique
     * @param displayName display name of the location
     * @param description description of the location
     * @param additionalProperties any additional properties
     * @return unique identifier of the location
     */
    private String addClassifiedLocation(String              qualifiedName,
                                         String              identifier,
                                         String              displayName,
                                         String              description,
                                         Map<String, String> additionalProperties,
                                         String              classificationName,
                                         InstanceProperties  classificationProperties,
                                         String              methodName)
    {
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        Classification classification = archiveHelper.getClassification(classificationName, classificationProperties, InstanceStatus.ACTIVE);

        classifications.add(classification);

        EntityDetail location = archiveHelper.getEntityDetail(OpenMetadataType.LOCATION.typeName,
                                                              idToGUIDMap.getGUID(qualifiedName),
                                                              properties,
                                                              InstanceStatus.ACTIVE,
                                                              classifications);

        archiveBuilder.addEntity(location);

        return location.getGUID();
    }


    /**
     * Add the MobileResource classification to the requested asset.
     *
     * @param elementGUID unique identifier of the element to classify
     */
    public void addMobileResourceClassification(String elementGUID)
    {
        EntityDetail assetEntity = archiveBuilder.getEntity(elementGUID);

        EntityProxy entityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = archiveHelper.getClassification(OpenMetadataType.MOBILE_RESOURCE_CLASSIFICATION.typeName,
                                                                         null,
                                                                         InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }


    /**
     * Create the relationship between a locations and one of the locations nested within it.
     *
     * @param broaderLocationGUID unique identifier of the broader location
     * @param nestedLocationGUID unique identifier of the nested location
     */
    public void addLocationHierarchy(String broaderLocationGUID,
                                     String nestedLocationGUID)
    {
        EntityDetail entity1 = archiveBuilder.getEntity(broaderLocationGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(nestedLocationGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.NESTED_LOCATION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(broaderLocationGUID + "_to_" + nestedLocationGUID + "_nested_location_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two locations as peers using the AdjacentLocation relationship.
     *
     * @param location1GUID unique identifier of the broader location
     * @param location2GUID unique identifier of the nested location
     */
    public void addPeerLocations(String location1GUID,
                                 String location2GUID)
    {
        EntityDetail referenceableEntity = archiveBuilder.getEntity(location1GUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(location2GUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ADJACENT_LOCATION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(location1GUID + "_to_" + location2GUID + "_adjacent_location_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a location to an element.
     *
     * @param locationQName qualified name of the location
     * @param elementQName qualified name of the element
     */
    public void addKnownLocationRelationship(String locationQName,
                                             String elementQName)
    {
        String locationId = idToGUIDMap.getGUID(locationQName);
        String elementId = idToGUIDMap.getGUID(elementQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(elementId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(locationId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.KNOWN_LOCATION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(locationId + "_to_" + elementId + "_known_location_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Add a user identity entity to the archive.
     *
     * @param qualifiedName unique name of the user identity
     * @param userId  name of the user account
     * @param distinguishedName LDAP name for the user
     * @param additionalProperties any additional properties
     * @return unique identifier of the user identity
     */
    public String addUserIdentity(String              qualifiedName,
                                  String              userId,
                                  String              distinguishedName,
                                  Map<String, String> additionalProperties)
    {
        final String methodName = "addUserIdentity";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USER_ID.name, userId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISTINGUISHED_NAME.name, distinguishedName, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        EntityDetail userIdentity = archiveHelper.getEntityDetail(OpenMetadataType.USER_IDENTITY.typeName,
                                                                  idToGUIDMap.getGUID(qualifiedName),
                                                                  properties,
                                                                  InstanceStatus.ACTIVE,
                                                                  null);

        archiveBuilder.addEntity(userIdentity);

        return userIdentity.getGUID();
    }


    /**
     * Create the relationship between a profile and one of its userIds.
     *
     * @param profileGUID unique identifier of the actor profile
     * @param userIdentityGUID unique identifier of the user identity
     * @param roleTypeName type of role that uses this userId
     * @param roleGUID unique identifier of role that uses this userId
     * @param description description of why role uses this userId
     */
    public void addProfileIdentity(String profileGUID,
                                   String userIdentityGUID,
                                   String roleTypeName,
                                   String roleGUID,
                                   String description)
    {
        final String methodName = "addProfileIdentity";

        EntityDetail profileEntity = archiveBuilder.getEntity(profileGUID);
        EntityDetail userIdentityEntity = archiveBuilder.getEntity(userIdentityGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(profileEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(userIdentityEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ROLE_TYPE_NAME.name, roleTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ROLE_GUID.name, roleGUID, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(profileGUID + "_to_" + userIdentityGUID + "_profile_identity_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a contact method to a profile.
     *
     * @param profileGUID unique identifier for a profile
     * @param name name of the contact method
     * @param contactType type of contact - eg home address, work mobile, emergency contact ...
     * @param contactMethodType type of contact address
     * @param contactMethodService service used in the contact method
     * @param contactMethodValue name/account/url used to contact the individual
     */
    public  void addContactDetails(String  profileGUID,
                                   String  profileTypeName,
                                   String  name,
                                   String  contactType,
                                   int     contactMethodType,
                                   String  contactMethodService,
                                   String  contactMethodValue)
    {
        final String methodName = "addContactDetails";

        EntityDetail profileEntity = archiveBuilder.getEntity(profileGUID);

        EnumElementDef enumElement = archiveHelper.getEnumElement(ContactMethodType.getOpenTypeName(), contactMethodType);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONTACT_TYPE.name, contactType, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONTACT_METHOD_TYPE.name, ContactMethodType.getOpenTypeGUID(), ContactMethodType.getOpenTypeName(), enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONTACT_METHOD_SERVICE.name, contactMethodService, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONTACT_METHOD_VALUE.name, contactMethodValue, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(profileGUID, profileTypeName, OpenMetadataType.ACTOR.typeName, null, methodName));

        EntityDetail contactDetails = archiveHelper.getEntityDetail(OpenMetadataType.CONTACT_DETAILS.typeName,
                                                                    idToGUIDMap.getGUID(contactMethodValue),
                                                                    properties,
                                                                    InstanceStatus.ACTIVE,
                                                                    classifications);

        archiveBuilder.addEntity(contactDetails);

        EntityProxy end1 = archiveHelper.getEntityProxy(profileEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(contactDetails);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CONTACT_THROUGH_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(profileGUID + "_to_" + contactDetails.getGUID() + "_contact_through_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new person role.
     *
     * @param suppliedTypeName type name to use for the person role
     * @param qualifiedName qualified name of role
     * @param identifier unique code
     * @param name display name
     * @param description description (eg job description)
     * @param scope scope of role's responsibilities
     * @param setHeadCount should the headcount field be set?
     * @param headCount number of people that may be appointed to the role (default = 1)
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addActorRole(String              suppliedTypeName,
                                String              qualifiedName,
                                String              identifier,
                                String              name,
                                String              description,
                                String              scope,
                                boolean             setHeadCount,
                                int                 headCount,
                                Map<String, String> additionalProperties,
                                Map<String, Object> extendedProperties)
    {
        final String methodName = "addActorRole";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataType.ACTOR_ROLE.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SCOPE.name, scope, methodName);
        if (setHeadCount)
        {
            properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.HEAD_COUNT.name, headCount, methodName);
        }
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(null, typeName, OpenMetadataType.ACTOR.typeName, null, methodName));

        EntityDetail role = archiveHelper.getEntityDetail(typeName,
                                                          idToGUIDMap.getGUID(qualifiedName),
                                                          properties,
                                                          InstanceStatus.ACTIVE,
                                                          classifications);

        archiveBuilder.addEntity(role);

        return role.getGUID();
    }



    /**
     * Add a new person role.
     *
     * @param suppliedTypeName type name to use for the person role
     * @param qualifiedName qualified name of role
     * @param domainIdentifier identifier of governance domain
     * @param identifier unique code
     * @param name display name
     * @param description description (eg job description)
     * @param scope scope of role's responsibilities
     * @param setHeadCount should the headcount field be set?
     * @param headCount number of people that may be appointed to the role (default = 1)
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addGovernanceRole(String              suppliedTypeName,
                                     String              qualifiedName,
                                     int                 domainIdentifier,
                                     String              identifier,
                                     String              name,
                                     String              description,
                                     String              scope,
                                     boolean             setHeadCount,
                                     int                 headCount,
                                     Map<String, String> additionalProperties,
                                     Map<String, Object> extendedProperties)
    {
        final String methodName = "addGovernanceRole";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataType.GOVERNANCE_ROLE.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, identifier, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DOMAIN_IDENTIFIER.name, domainIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SCOPE.name, scope, methodName);
        if (setHeadCount)
        {
            properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.HEAD_COUNT.name, headCount, methodName);
        }
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail role = archiveHelper.getEntityDetail(typeName,
                                                          idToGUIDMap.getGUID(qualifiedName),
                                                          properties,
                                                          InstanceStatus.ACTIVE,
                                                          null);

        archiveBuilder.addEntity(role);

        return role.getGUID();
    }


    /**
     * Link a person profile to a person role.
     *
     * @param personQName qualified name of the person profile
     * @param personRoleQName qualified name of the person role
     * @param isHeadcountSpecified is this appointment public?
     */
    public void addPersonRoleAppointmentRelationship(String  personQName,
                                                     String  personRoleQName,
                                                     boolean isHeadcountSpecified,
                                                     int     headcount)
    {
        final String methodName = "addPersonRoleAppointmentRelationship";

        String guid1 = idToGUIDMap.getGUID(personRoleQName);
        String guid2 = idToGUIDMap.getGUID(personQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = null;

        if (isHeadcountSpecified)
        {
            properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataProperty.HEAD_COUNT.name, headcount, methodName);
        }

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.PERSON_ROLE_APPOINTMENT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_person_role_appointment_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a person profile to another person as a peer.
     *
     * @param person1QName qualified name of the first person profile
     * @param person2QName qualified name of the second person profile
     */
    public void addPeerRelationship(String  person1QName,
                                    String  person2QName)
    {
        String guid1 = idToGUIDMap.getGUID(person1QName);
        String guid2 = idToGUIDMap.getGUID(person2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.PEER_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_peer_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new person profile.
     *
     * @param qualifiedName qualified name of profile
     * @param name display name (preferred name of individual)
     * @param pronouns preferred pronouns
     * @param description description (eg job description)
     * @param title courtesy title
     * @param initials given names initials
     * @param givenNames list of given names
     * @param surname family name
     * @param fullName full legal name
     * @param jobTitle job title
     * @param employeeNumber unique employee contract number
     * @param employeeType type of employee
     * @param preferredLanguage preferred language to communicate with
     * @param additionalProperties are there any additional properties to add
     * @return unique identifier of the new profile
     */
    public  String addPerson(String              qualifiedName,
                             String              name,
                             String              pronouns,
                             String              description,
                             String              initials,
                             String              title,
                             String              givenNames,
                             String              surname,
                             String              fullName,
                             String              jobTitle,
                             String              employeeNumber,
                             String              employeeType,
                             String              preferredLanguage,
                             Map<String, String> additionalProperties)
    {
        final String methodName = "addPerson";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PRONOUNS.name, pronouns, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.COURTESY_TITLE.name, title, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.INITIALS.name, initials, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.GIVEN_NAMES.name, givenNames, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SURNAME.name, surname, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.FULL_NAME.name, fullName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.JOB_TITLE.name, jobTitle, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.EMPLOYEE_NUMBER.name, employeeNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.EMPLOYEE_TYPE.name, employeeType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PREFERRED_LANGUAGE.name, preferredLanguage, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(null, OpenMetadataType.PERSON.typeName, OpenMetadataType.ACTOR.typeName, null, methodName));

        EntityDetail person = archiveHelper.getEntityDetail(OpenMetadataType.PERSON.typeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            classifications);

        archiveBuilder.addEntity(person);

        return person.getGUID();
    }


    /**
     * Add a new team profile.
     *
     * @param suppliedTypeName type name for the team
     * @param qualifiedName qualified name of profile
     * @param name display name (preferred name of individual)
     * @param description description (eg job description)
     * @param teamType type of team
     * @param identifier code value identifier for the team
     * @param additionalProperties are there any additional properties to add
     * @return unique identifier of the new profile
     */
    public  String addTeam(String              suppliedTypeName,
                           String              qualifiedName,
                           String              name,
                           String              description,
                           String              teamType,
                           String              identifier,
                           Map<String, String> additionalProperties)
    {
        final String methodName = "addTeam";

        String typeName = suppliedTypeName;
        if (typeName == null)
        {
            typeName = OpenMetadataType.TEAM.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TEAM_TYPE.name, teamType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, identifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(null, typeName, OpenMetadataType.ACTOR.typeName, null, methodName));

        EntityDetail profile = archiveHelper.getEntityDetail(typeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            classifications);

        archiveBuilder.addEntity(profile);

        return profile.getGUID();
    }


    /**
     * Link a person role as a team's leader.
     *
     * @param personRoleQName qualified name of the person role
     * @param teamQName qualified name of the team profile
     * @param position position of the role
     */
    public void addTeamLeadershipRelationship(String personRoleQName,
                                              String teamQName,
                                              String position)
    {
        final String methodName = "addTeamLeadershipRelationship";

        String guid1 = idToGUIDMap.getGUID(personRoleQName);
        String guid2 = idToGUIDMap.getGUID(teamQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, position, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_leadership_associated_scope_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a person role as a team's member.
     *
     * @param personRoleQName qualified name of the person role
     * @param teamQName qualified name of the team profile
     * @param position position of the role
     */
    public void addTeamMembershipRelationship(String personRoleQName,
                                              String teamQName,
                                              String position)
    {
        final String methodName = "addTeamMembershipRelationship";

        String guid1 = idToGUIDMap.getGUID(personRoleQName);
        String guid2 = idToGUIDMap.getGUID(teamQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, position, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_membership_assignment_scope_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a person role as a team's member.
     *
     * @param superTeamQName qualified name of the super team profile
     * @param subTeamQName qualified name of the sub-team profile
     * @param delegationEscalationAuthority delegationEscalationAuthority of the role
     */
    public void addTeamStructureRelationship(String  superTeamQName,
                                             String  subTeamQName,
                                             boolean delegationEscalationAuthority)
    {
        final String methodName = "addTeamStructureRelationship";

        String guid1 = idToGUIDMap.getGUID(superTeamQName);
        String guid2 = idToGUIDMap.getGUID(subTeamQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DELEGATION_ESCALATION.name, delegationEscalationAuthority, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.TEAM_STRUCTURE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_structure_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new IT profile.
     *
     * @param infrastructureGUID unique identifier of asset/software capability to connect the profile to
     * @param userId userId of the asset/software capability
     * @param qualifiedName qualified name of profile
     * @param name display name (preferred name of individual)
     * @param description description (eg job description)
     * @param additionalProperties are there any additional properties to add
     * @return unique identifier of the new profile
     */
    public  String addITProfile(String              infrastructureGUID,
                                String              userId,
                                String              qualifiedName,
                                String              name,
                                String              description,
                                Map<String, String> additionalProperties)
    {
        final String methodName = "addITProfile";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        EntityDetail profile = archiveHelper.getEntityDetail(OpenMetadataType.IT_PROFILE.typeName,
                                                             idToGUIDMap.getGUID(qualifiedName),
                                                             properties,
                                                             InstanceStatus.ACTIVE,
                                                             null);

        archiveBuilder.addEntity(profile);

        if (infrastructureGUID != null)
        {
            EntityDetail itEntity = archiveBuilder.getEntity(infrastructureGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(itEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(profile);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(infrastructureGUID + "_to_" + profile.getGUID() + "_it_infrastructure_profile_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (userId != null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName + ":UserIdentity", methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USER_ID.name, userId, methodName);

            EntityDetail userIdentity = archiveHelper.getEntityDetail(OpenMetadataType.USER_IDENTITY.typeName,
                                                                      idToGUIDMap.getGUID(qualifiedName + ":UserIdentity"),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

            archiveBuilder.addEntity(userIdentity);

            EntityProxy end1 = archiveHelper.getEntityProxy(profile);
            EntityProxy end2 = archiveHelper.getEntityProxy(userIdentity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.PROFILE_IDENTITY_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(profile.getGUID() + "_to_" + userIdentity.getGUID() + "_profile_identity_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return profile.getGUID();
    }



    /**
     * Link an actor (profile/person role) to a scope.
     *
     * @param actorQName qualified name of the actor
     * @param scopeQName qualified name of the scope
     * @param assignmentType type of the assignment
     * @param description description of the assignment
     */
    public void addAssignmentScopeRelationship(String actorQName,
                                               String scopeQName,
                                               String assignmentType,
                                               String description)
    {
        final String methodName = "addAssignmentScopeRelationship";

        String guid1 = idToGUIDMap.getGUID(actorQName);
        String guid2 = idToGUIDMap.getGUID(scopeQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, assignmentType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_assignment_scope_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new project.
     *
     * @param suppliedTypeName subtype information
     * @param qualifiedName qualified name of project
     * @param identifier unique identifier of project - typically allocated externally
     * @param name display name
     * @param description description
     * @param startDate date the project started
     * @param plannedEndDate date the project is expected to end
     * @param projectPhase lifecycle phase of the project
     * @param projectHealth health of the project's execution
     * @param projectStatus status of the project
     * @param setCampaignClassification should the Campaign classification be set?
     * @param setTaskClassification should the Task classification be set?
     * @param projectTypeClassification add special classification that defines the type of project - eg GlossaryProject or GovernanceProject
     * @param otherClassifications any other classifications, such as Template
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addProject(String               suppliedTypeName,
                              String               qualifiedName,
                              String               identifier,
                              String               name,
                              String               description,
                              Date                 startDate,
                              Date                 plannedEndDate,
                              String               projectPhase,
                              String               projectHealth,
                              String               projectStatus,
                              boolean              setCampaignClassification,
                              boolean              setTaskClassification,
                              String               projectTypeClassification,
                              List<Classification> otherClassifications,
                              Map<String, String>  additionalProperties,
                              Map<String, Object>  extendedProperties)
    {
        final String methodName = "addProject";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataType.PROJECT.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataProperty.START_DATE.name, startDate, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PLANNED_END_DATE.name, plannedEndDate, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PROJECT_PHASE.name, projectPhase, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PROJECT_HEALTH.name, projectHealth, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PROJECT_STATUS.name, projectStatus, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications;

        if (otherClassifications != null)
        {
            classifications = otherClassifications;
        }
        else
        {
            classifications = new ArrayList<>();
        }

        if (setCampaignClassification)
        {
            Classification classification = archiveHelper.getClassification(OpenMetadataType.CAMPAIGN_CLASSIFICATION.typeName, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }
        if (setTaskClassification)
        {
            Classification classification = archiveHelper.getClassification(OpenMetadataType.TASK_CLASSIFICATION.typeName, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }
        if (projectTypeClassification != null)
        {
            Classification classification = archiveHelper.getClassification(projectTypeClassification, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }

        if (classifications.isEmpty())
        {
            classifications = null;
        }

        EntityDetail project = archiveHelper.getEntityDetail(typeName,
                                                             idToGUIDMap.getGUID(qualifiedName),
                                                             properties,
                                                             InstanceStatus.ACTIVE,
                                                             classifications);

        archiveBuilder.addEntity(project);

        return project.getGUID();
    }


    /**
     * Link a project to a subproject.
     *
     * @param projectQName qualified name of the parent project
     * @param subprojectQName qualified name of the subproject
     */
    public void addProjectHierarchyRelationship(String  projectQName,
                                                String  subprojectQName)
    {
        String guid1 = idToGUIDMap.getGUID(projectQName);
        String guid2 = idToGUIDMap.getGUID(subprojectQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.PROJECT_HIERARCHY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a project to another project that it depends on.
     *
     * @param projectQName qualified name of the project
     * @param dependsOnProjectQName qualified name of the project that it depends on
     * @param dependencySummary description of what makes them dependent
     */
    public void addProjectDependencyRelationship(String  projectQName,
                                                 String  dependsOnProjectQName,
                                                 String  dependencySummary)
    {
        final String methodName = "addProjectDependencyRelationship";

        String guid1 = idToGUIDMap.getGUID(projectQName);
        String guid2 = idToGUIDMap.getGUID(dependsOnProjectQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DESCRIPTION.name, dependencySummary, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.PROJECT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_dependency_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Link a project to a team.  A project may have multiple teams.
     *
     * @param projectQName qualified name of the person profile
     * @param personRoleQName qualified name of the person role
     * @param teamRole role of this team in the project
     */
    public void addProjectTeamRelationship(String  projectQName,
                                           String  personRoleQName,
                                           String  teamRole)
    {
        final String methodName = "addProjectTeamRelationship";

        String guid1 = idToGUIDMap.getGUID(personRoleQName);
        String guid2 = idToGUIDMap.getGUID(projectQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, teamRole, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_team_assignment_scope_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a project to a person role that represents the project manager(s).
     *
     * @param projectQName qualified name of the project
     * @param projectManagerRoleQName qualified name of the person role
     */
    public void addProjectManagementRelationship(String  projectQName,
                                                 String  projectManagerRoleQName)
    {
        final String methodName = "addProjectManagementRelationship";

        String guid1 = idToGUIDMap.getGUID(projectManagerRoleQName);
        String guid2 = idToGUIDMap.getGUID(projectQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_management_assignment_scope_relationship"),
                                                                     archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, AssignmentType.LEADER.getName(), methodName),
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new community.
     *
     * @param suppliedTypeName subtype information
     * @param qualifiedName qualified name of community
     * @param name display name
     * @param description description
     * @param mission why is the community formed?
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addCommunity(String              suppliedTypeName,
                                String              qualifiedName,
                                String              name,
                                String              description,
                                String              mission,
                                Map<String, String> additionalProperties,
                                Map<String, Object> extendedProperties)
    {
        final String methodName = "addCommunity";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataType.COMMUNITY.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MISSION.name, mission, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail project = archiveHelper.getEntityDetail(typeName,
                                                             idToGUIDMap.getGUID(qualifiedName),
                                                             properties,
                                                             InstanceStatus.ACTIVE,
                                                             null);

        archiveBuilder.addEntity(project);

        return project.getGUID();
    }


    /**
     * Link a community to a person role.
     *
     * @param communityQName qualified name of the community
     * @param membershipRoleQName qualified name of the membership role
     * @param membershipType ordinal of enum
     */
    public void addCommunityMembershipRelationship(String  communityQName,
                                                   String  membershipRoleQName,
                                                   String  membershipType)
    {
        final String methodName = "addCommunityMembershipRelationship";

        String guid1 = idToGUIDMap.getGUID(membershipRoleQName);
        String guid2 = idToGUIDMap.getGUID(communityQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, membershipType, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_community_membership_assignment_scope_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a collection entity.
     *
     * @param suppliedTypeName type of collection
     * @param anchorGUID unique identifier of the anchor for the collection - if null then own anchor
     * @param anchorTypeName unique type name of the anchor for the collection
     * @param anchorDomainName unique type name of the anchor's domain
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param classificationName name of classification to attach
     * @param qualifiedName unique name for the collection entity
     * @param displayName display name for the collection
     * @param description description about the collection
     * @param category type of collection
     * @param additionalProperties any other properties
     * @param otherClassifications additional classifications for the collection (eg Template)
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for collection (collectionGUID)
     */
    public String addCollection(String               suppliedTypeName,
                                String               anchorGUID,
                                String               anchorTypeName,
                                String               anchorDomainName,
                                String               anchorScopeGUID,
                                String               classificationName,
                                String               qualifiedName,
                                String               displayName,
                                String               description,
                                String               category,
                                Map<String, String>  additionalProperties,
                                List<Classification> otherClassifications,
                                Map<String, Object>  extendedProperties)
    {
        final String methodName = "addCollection";

        String typeName = OpenMetadataType.COLLECTION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        List<Classification> classifications = otherClassifications;

        if (classifications == null)
        {
            classifications = new ArrayList<>();
        }

        if (classificationName != null)
        {
            Classification classification = archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }

        if (anchorTypeName != null)
        {
            classifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        if (classifications.isEmpty())
        {
            classifications = null;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CATEGORY.name, category, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               classifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Add a member to a collection.
     *
     * @param collectionGUID unique identifier of the collection
     * @param memberGUID unique identifier of the member
     * @param membershipRationale why is this member in this collection
     */
    public void addMemberToCollection(String collectionGUID,
                                      String memberGUID,
                                      String membershipRationale)
    {
        final String methodName = "addMemberToCollection";

        EntityDetail entity1 = archiveBuilder.getEntity(collectionGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(memberGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.MEMBERSHIP_RATIONALE.name, membershipRationale, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(collectionGUID + "_to_" + memberGUID + "_collection_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a data field entity.  These are not all of the properties of a data field - but enough for current
     * use cases.
     *
     * @param suppliedTypeName type of data field
     * @param anchorGUID unique identifier of the anchor for the data field - if null then own anchor
     * @param anchorTypeName unique type name of the anchor for the data field
     * @param anchorDomainName unique type name of the anchor's domain
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param classificationName name of classification to attach
     * @param qualifiedName unique name for the data field entity
     * @param displayName display name for the data field
     * @param description description about the data field
     * @param versionIdentifier version
     * @param dataType logical data type
     * @param namespace type of data field
     * @param additionalProperties any other properties
     * @param otherClassifications additional classifications for the data field (eg Template)
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for data field (dataFieldGUID)
     */
    public String addDataField(String               suppliedTypeName,
                               String               anchorGUID,
                               String               anchorTypeName,
                               String               anchorDomainName,
                               String               anchorScopeGUID,
                               String               classificationName,
                               String               qualifiedName,
                               String               displayName,
                               String               description,
                               String               versionIdentifier,
                               String               namespace,
                               String               dataType,
                               Map<String, String>  additionalProperties,
                               List<Classification> otherClassifications,
                               Map<String, Object>  extendedProperties)
    {
        final String methodName = "addDataField";

        String typeName = OpenMetadataType.DATA_FIELD.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        List<Classification> classifications = otherClassifications;

        if (classifications == null)
        {
            classifications = new ArrayList<>();
        }

        if (classificationName != null)
        {
            Classification classification = archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }

        if (anchorTypeName != null)
        {
            classifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        if (classifications.isEmpty())
        {
            classifications = null;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NAMESPACE.name, namespace, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DATA_TYPE.name, dataType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               classifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Add a nested data field to a complex data field.
     *
     * @param parentDataFieldGUID unique identifier of the parent data field
     * @param nestedDataFieldGUID unique identifier of the nested data field
     * @param position position of the data field in the parent
     */
    public void addNestedDataField(String parentDataFieldGUID,
                                   String nestedDataFieldGUID,
                                   int    position,
                                   int    minCardinality,
                                   int    maxCardinality)
    {
        final String methodName = "addNestedDataField";

        EntityDetail entity1 = archiveBuilder.getEntity(parentDataFieldGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(nestedDataFieldGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataProperty.POSITION.name, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MIN_CARDINALITY.name, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MAX_CARDINALITY.name, maxCardinality, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.NESTED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(parentDataFieldGUID + "_to_" + nestedDataFieldGUID + "_nested_data_field_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link data fields.  There are more properties available fore this relationship.  However, this is all
     * that is needed at the moment.
     *
     * @param domainDataFieldGUID unique identifier of the parent data field
     * @param rangeDataFieldGUID unique identifier of the nested data field
     * @param relationshipTypeName name of the relationship
     */
    public void addLinkedDataField(String domainDataFieldGUID,
                                   String rangeDataFieldGUID,
                                   String relationshipTypeName)
    {
        final String methodName = "addLinkedDataField";

        EntityDetail entity1 = archiveBuilder.getEntity(domainDataFieldGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(rangeDataFieldGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.RELATIONSHIP_TYPE_NAME.name, relationshipTypeName, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.LINKED_DATA_FIELD_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(domainDataFieldGUID + "_to_" + rangeDataFieldGUID + "_linked_data_field_relationship_" + relationshipTypeName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a data structure entity.
     *
     * @param suppliedTypeName type of data structure
     * @param anchorGUID unique identifier of the anchor for the data structure - if null then own anchor
     * @param anchorTypeName unique type name of the anchor for the data structure
     * @param anchorDomainName unique type name of the anchor's domain
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the data structure entity
     * @param displayName display name for the data structure
     * @param description description about the data structure
     * @param versionIdentifier version
     * @param namespace type of data structure
     * @param additionalProperties any other properties
     * @param otherClassifications additional classifications for the data structure (eg Template)
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for data structure (dataStructureGUID)
     */
    public String addDataStructure(String               suppliedTypeName,
                                   String               anchorGUID,
                                   String               anchorTypeName,
                                   String               anchorDomainName,
                                   String               anchorScopeGUID,
                                   String               qualifiedName,
                                   String               displayName,
                                   String               description,
                                   String               versionIdentifier,
                                   String               namespace,
                                   Map<String, String>  additionalProperties,
                                   List<Classification> otherClassifications,
                                   Map<String, Object>  extendedProperties)
    {
        final String methodName = "addDataStructure";

        String typeName = OpenMetadataType.DATA_STRUCTURE.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        List<Classification> classifications = otherClassifications;

        if (classifications == null)
        {
            classifications = new ArrayList<>();
        }

        if (anchorTypeName != null)
        {
            classifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        if (classifications.isEmpty())
        {
            classifications = null;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NAMESPACE.name, namespace, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               classifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Add a data field to a data structure.
     *
     * @param dataStructureGUID unique identifier of the data structure
     * @param memberDataFieldGUID unique identifier of the member
     * @param position position of the data field in the parent
     */
    public void addMemberDataField(String dataStructureGUID,
                                   String memberDataFieldGUID,
                                   int    position,
                                   int    minCardinality,
                                   int    maxCardinality)
    {
        final String methodName = "addMemberDataField";

        EntityDetail entity1 = archiveBuilder.getEntity(dataStructureGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(memberDataFieldGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataProperty.POSITION.name, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MIN_CARDINALITY.name, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MAX_CARDINALITY.name, maxCardinality, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(dataStructureGUID + "_to_" + memberDataFieldGUID + "_member_data_field_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Link a term to a data definition using the DataStructureDefinition relationship (0580).
     *
     * @param dataStructureGUID unique identifier of the data structure
     * @param schemaTypeGUID unique identifier of the schema type - may be a template
     */
    public void addSchemaTypeDefinition(String dataStructureGUID,
                                        String schemaTypeGUID)
    {
        EntityDetail entity1 = archiveBuilder.getEntity(dataStructureGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(schemaTypeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SCHEMA_TYPE_DEFINITION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(dataStructureGUID + "_to_" + schemaTypeGUID + "_schema_type_definition_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a term to a data definition using the DataStructureDefinition relationship (0580).
     *
     * @param certificationTypeGUID unique identifier of the certification type
     * @param dataStructureGUID unique identifier of the data structure
     */
    public void addDataStructureDefinition(String certificationTypeGUID,
                                           String dataStructureGUID)
    {
        EntityDetail entity1 = archiveBuilder.getEntity(certificationTypeGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(dataStructureGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(certificationTypeGUID + "_to_" + dataStructureGUID + "_data_structure_definition_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a term to a data definition using the SemanticDefinition relationship (0370).
     *
     * @param referenceableGUID unique identifier of the referenceable
     * @param termGUID unique identifier of the term
     */
    public void addSemanticDefinition(String referenceableGUID,
                                      String termGUID)
    {
        EntityDetail entity1 = archiveBuilder.getEntity(referenceableGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(termGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + termGUID + "_semantic_definition_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a governance definition entity.
     *
     * @param suppliedTypeName type of governance definition to add
     * @param qualifiedName unique name for the governance definition entity
     * @param identifier identifier for the governance definition
     * @param summary short description for the governance definition
     * @param description description about the governance definition
     * @param scope scope where the governance definition is used
     * @param usage how is the governance definition used
     * @param importance how important is the governance definition
     * @param domainIdentifier unique identifier of the governance domain
     * @param implications expected impact of adopting this definition
     * @param outcomes expected outcomes from adopting this definition
     * @param results results from adopting this definition
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for governance definition (governanceDefinitionGUID)
     */
    public String addGovernanceDefinition(String               suppliedTypeName,
                                          String               qualifiedName,
                                          String               identifier,
                                          String               summary,
                                          String               description,
                                          String               scope,
                                          String               usage,
                                          int                  domainIdentifier,
                                          String               importance,
                                          List<String>         implications,
                                          List<String>         outcomes,
                                          List<String>         results,
                                          Map<String, String>  additionalProperties,
                                          Map<String, Object>  extendedProperties)
    {
        final String methodName = "addGovernanceDefinition";

        String typeName = OpenMetadataType.GOVERNANCE_DEFINITION.typeName;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SUMMARY.name, summary, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SCOPE.name, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USAGE.name, usage, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IMPORTANCE.name, importance, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DOMAIN_IDENTIFIER.name, domainIdentifier, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IMPLICATIONS.name, implications, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.OUTCOMES.name, outcomes, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.RESULTS.name, results, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(getAnchorClassification(null,
                                                    typeName,
                                                    OpenMetadataType.GOVERNANCE_DEFINITION.typeName,
                                                    null,
                                                    methodName));

        EntityDetail newEntity = archiveHelper.getEntityDetail(typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               classifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Link a referenceable to a governance definition to define the scope there it applies.
     *
     * @param referenceableQName qualified name of the referenceable
     * @param governanceDefinitionQName qualified name of the governance definition
     */
    public void addGovernanceDefinitionScopeRelationship(String  referenceableQName,
                                                         String  governanceDefinitionQName)
    {
        String guid1 = idToGUIDMap.getGUID(governanceDefinitionQName);
        String guid2 = idToGUIDMap.getGUID(referenceableQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SCOPED_BY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governance_definition_scope_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a referenceable to a governance definition to indicate that it is governed by the governance definition.
     *
     * @param referenceableQName qualified name of the referenceable
     * @param governanceDefinitionQName qualified name of the governance definition
     */
    public void addGovernedByRelationship(String  referenceableQName,
                                          String  governanceDefinitionQName)
    {
        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinitionQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.GOVERNED_BY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governed_by_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two governance definitions at the same level.
     *
     * @param relationshipTypeName type of relationship
     * @param governanceDefinition1QName qualified name of the referenceable
     * @param governanceDefinition2QName qualified name of the governance definition
     * @param description description of the peer relationship
     */
    public void addGovernanceDefinitionPeerRelationship(String  relationshipTypeName,
                                                        String  governanceDefinition1QName,
                                                        String  governanceDefinition2QName,
                                                        String  description)
    {
        final String methodName = "addGovernanceDefinitionPeerRelationship";

        String guid1 = idToGUIDMap.getGUID(governanceDefinition1QName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinition2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(relationshipTypeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governance_definition_" + relationshipTypeName + "_peer_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two governance definitions at the same level.
     *
     * @param relationshipTypeName type of relationship
     * @param governanceDefinition1QName qualified name of the referenceable
     * @param governanceDefinition2QName qualified name of the governance definition
     * @param rationale description of the delegation relationship
     */
    public void addGovernanceDefinitionDelegationRelationship(String  relationshipTypeName,
                                                              String  governanceDefinition1QName,
                                                              String  governanceDefinition2QName,
                                                              String  rationale)
    {
        final String methodName = "addGovernanceDefinitionDelegationRelationship";

        String guid1 = idToGUIDMap.getGUID(governanceDefinition1QName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinition2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.RATIONALE.name, rationale, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(relationshipTypeName,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governance_definition_" + relationshipTypeName + "_delegation_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a governance zone entity.
     *
     * @param qualifiedName unique name for the zone entity
     * @param zoneName unique name for the zone
     * @param displayName display name for the zone
     * @param description description about the zone
     * @param scope scope where the zone is used
     * @param criteria what is the common characteristic of assets in this zone?
     * @param domainIdentifier unique identifier of the governance domain (0 means all/any)
     * @param additionalProperties any other properties
     *
     * @return unique identifier for governance zone (governanceZoneGUID)
     */
    public String addGovernanceZone(String               qualifiedName,
                                    String               zoneName,
                                    String               displayName,
                                    String               description,
                                    String               criteria,
                                    String               scope,
                                    int                  domainIdentifier,
                                    Map<String, String>  additionalProperties)
    {
        final String methodName = "addGovernanceZone";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, zoneName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SCOPE.name, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CRITERIA.name, criteria, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DOMAIN_IDENTIFIER.name, domainIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(OpenMetadataType.GOVERNANCE_ZONE.typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Create the relationship between a governance zone and one of its nested governance zones.
     *
     * @param broaderGovernanceZoneGUID unique identifier of the broader zone
     * @param nestedGovernanceZoneGUID unique identifier of the nested (narrower) zone
     */
    public void addZoneHierarchy(String broaderGovernanceZoneGUID,
                                 String nestedGovernanceZoneGUID)
    {
        EntityDetail entity1 = archiveBuilder.getEntity(broaderGovernanceZoneGUID);
        EntityDetail entity2 = archiveBuilder.getEntity(nestedGovernanceZoneGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entity1);
        EntityProxy end2 = archiveHelper.getEntityProxy(entity2);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ZONE_HIERARCHY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(broaderGovernanceZoneGUID + "_to_" + nestedGovernanceZoneGUID + "_zone_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Return the zone membership classification.
     *
     * @param zones list of zones that the element is a member of.
     */
    public Classification getZoneMembershipClassification(List<String> zones)
    {
        final String methodName = "getZoneMembershipClassification";

        return archiveHelper.getClassification(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                               archiveHelper.addStringArrayPropertyToInstance(archiveRootName,
                                                                                              null,
                                                                                              OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                                              zones,
                                                                                              methodName),
                                               InstanceStatus.ACTIVE);
    }


    /**
     * Add the asset zone membership classification to the requested asset.
     *
     * @param assetGUID unique identifier of the asset to classify
     * @param zones list of zones that the asset is a member of.
     */
    public void addAssetZoneMembershipClassification(String       assetGUID,
                                                     List<String> zones)
    {
        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);
        EntityProxy  entityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = this.getZoneMembershipClassification(zones);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }




    /**
     * Return the asset origin classification.
     *
     * @param organization organization GUID/name
     * @param organizationPropertyName property name used to id the organization
     * @param businessCapability business capability GUID/name
     * @param businessCapabilityPropertyName property name used to id the business capability
     * @param otherOriginValues map of other values
     * @return classification
     */
    public Classification getAssetOriginClassification(String              organization,
                                                       String              organizationPropertyName,
                                                       String              businessCapability,
                                                       String              businessCapabilityPropertyName,
                                                       Map<String, String> otherOriginValues)
    {
        final String methodName = "getAssetOriginClassification";

        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                null,
                                                                                                OpenMetadataProperty.ORGANIZATION.name,
                                                                                                organization,
                                                                                                methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.ORGANIZATION_PROPERTY_NAME.name,
                                                                             organizationPropertyName,
                                                                             methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.BUSINESS_CAPABILITY.name,
                                                                             businessCapability,
                                                                             methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.BUSINESS_CAPABILITY_PROPERTY_NAME.name,
                                                                             businessCapabilityPropertyName,
                                                                             methodName);

        classificationProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName,
                                                                                classificationProperties,
                                                                                OpenMetadataProperty.OTHER_ORIGIN_VALUES.name,
                                                                                otherOriginValues,
                                                                                methodName);

        return archiveHelper.getClassification(OpenMetadataType.DIGITAL_RESOURCE_ORIGIN_CLASSIFICATION.typeName,
                                               classificationProperties,
                                               InstanceStatus.ACTIVE);
    }


    /**
     * Return the confidentiality classification.
     *
     * @param statusIdentifier status of this classification
     * @param confidence how confident that it is correct 0-100
     * @param steward who agreed to the setting
     * @param stewardTypeName that type of element is describing the steward
     * @param stewardPropertyName which property name was used to identify the steward
     * @param source what is the source of this classification
     * @param notes additional notes from the steward
     * @param levelIdentifier what is the level of the classification
     * @return classification
     */
    public Classification getConfidentialityClassification(int    statusIdentifier,
                                                           int    confidence,
                                                           String steward,
                                                           String stewardTypeName,
                                                           String stewardPropertyName,
                                                           String source,
                                                           String notes,
                                                           int    levelIdentifier)
    {
        final String methodName = "getConfidentialityClassification";

        InstanceProperties classificationProperties = archiveHelper.addIntPropertyToInstance(archiveRootName,
                                                                                             null,
                                                                                             OpenMetadataProperty.STATUS_IDENTIFIER.name,
                                                                                             statusIdentifier,
                                                                                             methodName);
        classificationProperties = archiveHelper.addIntPropertyToInstance(archiveRootName,
                                                                          classificationProperties,
                                                                          OpenMetadataProperty.CONFIDENCE.name,
                                                                          confidence,
                                                                          methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.STEWARD.name,
                                                                             steward,
                                                                             methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.STEWARD_TYPE_NAME.name,
                                                                             stewardTypeName,
                                                                             methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.STEWARD_PROPERTY_NAME.name,
                                                                             stewardPropertyName,
                                                                             methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.SOURCE.name,
                                                                             source,
                                                                             methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                             classificationProperties,
                                                                             OpenMetadataProperty.NOTES.name,
                                                                             notes,
                                                                             methodName);
        classificationProperties = archiveHelper.addIntPropertyToInstance(archiveRootName,
                                                                          classificationProperties,
                                                                          OpenMetadataProperty.CONFIDENTIALITY_LEVEL_IDENTIFIER.name,
                                                                          levelIdentifier,
                                                                          methodName);

        return archiveHelper.getClassification(OpenMetadataType.CONFIDENTIALITY_CLASSIFICATION.typeName,
                                               classificationProperties,
                                               InstanceStatus.ACTIVE);
    }



    /**
     * Create a subject area definition entity.
     *
     * @param qualifiedName unique name for the subject area entity
     * @param subjectAreaName unique name for the subject area
     * @param displayName display name for the subject area
     * @param description description about the subject area
     * @param scope scope where the subject area is used
     * @param usage how is the subject area used
     * @param domainIdentifier unique identifier of the governance domain
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for subject area (subjectAreaGUID)
     */
    public String addSubjectAreaDefinition(String               qualifiedName,
                                           String               subjectAreaName,
                                           String               displayName,
                                           String               description,
                                           String               scope,
                                           String               usage,
                                           int                  domainIdentifier,
                                           Map<String, String>  additionalProperties,
                                           Map<String, Object>  extendedProperties)
    {
        final String methodName = "addSubjectAreaDefinition";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, subjectAreaName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SCOPE.name, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USAGE.name, usage, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DOMAIN_IDENTIFIER.name, domainIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Create the relationship between a subject area and one of its nested subject areas.
     *
     * @param broaderSubjectAreaGUID unique identifier of the broader subject area
     * @param nestedSubjectAreaGUID unique identifier of the nested (narrower) subject area
     */
    public void addSubjectAreaHierarchy(String broaderSubjectAreaGUID,
                                        String nestedSubjectAreaGUID)
    {
        EntityDetail referenceableEntity = archiveBuilder.getEntity(broaderSubjectAreaGUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(nestedSubjectAreaGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(broaderSubjectAreaGUID + "_to_" + nestedSubjectAreaGUID + "_subject_area_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add the subject area classification to the requested element.
     *
     * @param referenceableGUID unique identifier of the element to classify
     * @param subjectAreaName name of the subject area.  The suggestion is that the name used is the qualified name.
     */
    public void addSubjectAreaClassification(String referenceableGUID,
                                             String subjectAreaName)
    {
        final String methodName = "addSubjectAreaClassification";

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);

        EntityProxy referenceableEntityProxy = archiveHelper.getEntityProxy(referenceableEntity);

        Classification  subjectAreaClassification = archiveHelper.getClassification(OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName,
                                                                                    archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                              null,
                                                                                                                              OpenMetadataProperty.SUBJECT_AREA_NAME.name,
                                                                                                                              subjectAreaName,
                                                                                                                              methodName),
                                                                                    InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(referenceableEntityProxy, subjectAreaClassification));
    }



    /**
     * Create a business area entity.
     *
     * @param qualifiedName unique name for the business area entity
     * @param identifier unique name for the business area
     * @param displayName display name for the business area
     * @param description description about the business area
     * @param additionalProperties any other properties
     *
     * @return unique identifier for business area (businessAreaGUID)
     */
    public String addBusinessArea(String               qualifiedName,
                                  String               identifier,
                                  String               displayName,
                                  String               description,
                                  Map<String, String>  additionalProperties)
    {
        final String methodName = "addBusinessArea";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IDENTIFIER.name, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.BUSINESS_CAPABILITY_TYPE.name, BusinessCapabilityType.getOpenTypeGUID(), BusinessCapabilityType.getOpenTypeName(), BusinessCapabilityType.BUSINESS_AREA.getOrdinal(), BusinessCapabilityType.BUSINESS_AREA.getName(), BusinessCapabilityType.BUSINESS_AREA.getDescription(), methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(null,
                                                         OpenMetadataType.BUSINESS_CAPABILITY.typeName,
                                                         OpenMetadataType.BUSINESS_CAPABILITY.typeName,
                                                         null,
                                                         methodName));

        EntityDetail newEntity = archiveHelper.getEntityDetail(OpenMetadataType.BUSINESS_CAPABILITY.typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               classifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Add an BusinessCapabilityTeam relationship.
     *
     * @param businessCapabilityQName qualified name of the specialized term
     * @param teamQName qualified name of the generalized term
     * @param assignmentType assignmentType of the team's ability to support the business capability
     * @param description description of the team's ability to support the business capability
     */
    public void addBusinessCapabilityTeamRelationship(String businessCapabilityQName,
                                                      String teamQName,
                                                      String assignmentType,
                                                      String description)
    {
        final String methodName = "addBusinessCapabilityTeamRelationship";

        String end1GUID = idToGUIDMap.getGUID(teamQName);
        String end2GUID = idToGUIDMap.getGUID(businessCapabilityQName);
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end1GUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end2GUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSIGNMENT_TYPE.name, assignmentType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSIGNMENT_SCOPE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(end1GUID + "_to_" + end2GUID + "_assignment_scope_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a design model entity.
     *
     * @param typeName name of element subtype to use - default is DesignModel
     * @param classificationName name of classification the identifies the type of design model
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param technicalName technical name for the element
     * @param description description about the element
     * @param versionNumber version number for the element
     * @param author author of the element
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for new design model (designModelGUID)
     */
    public String addDesignModel(String               typeName,
                                 String               classificationName,
                                 String               qualifiedName,
                                 String               displayName,
                                 String               technicalName,
                                 String               description,
                                 String               versionNumber,
                                 String               author,
                                 Map<String, String>  additionalProperties,
                                 Map<String, Object>  extendedProperties)
    {
        final String methodName = "addDesignModel";

        String elementTypeName = OpenMetadataType.DESIGN_MODEL.typeName;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        List<Classification> entityClassifications = new ArrayList<>();

        entityClassifications.add(this.getAnchorClassification(null, elementTypeName, OpenMetadataType.DESIGN_MODEL.typeName, null, methodName));

        if (classificationName != null)
        {
            Classification classification = archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);

            entityClassifications.add(classification);
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName); // it's an asset
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TECHNICAL_NAME.name, technicalName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.AUTHOR.name, author, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               entityClassifications);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Create a design model element entity.  This may be a group or an element in the model.
     *
     * @param designModelGUID unique identifier of model that owns this element
     * @param designModelTypeName unique type name of model that owns this element
     * @param typeName name of element subtype to use - default is DesignModelElement
     * @param qualifiedName unique name for the element
     * @param displayName display name for the element
     * @param technicalName technical name for the element
     * @param description description about the element
     * @param versionNumber version number for the element
     * @param author author of the element
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return unique identifier for the new model element
     */
    public String addDesignModelElement(String               designModelGUID,
                                        String               designModelTypeName,
                                        String               typeName,
                                        String               qualifiedName,
                                        String               displayName,
                                        String               technicalName,
                                        String               description,
                                        String               versionNumber,
                                        String               author,
                                        Map<String, String>  additionalProperties,
                                        Map<String, Object>  extendedProperties,
                                        List<Classification> classifications)
    {
        final String methodName = "addDesignModelElement";

        String elementTypeName = OpenMetadataType.DESIGN_MODEL_ELEMENT.typeName;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        List<Classification> entityClassifications = classifications;

        if (designModelGUID != null)
        {
            if (entityClassifications == null)
            {
                entityClassifications = new ArrayList<>();
            }

            entityClassifications.add(this.getAnchorClassification(designModelGUID,
                                                                   designModelTypeName,
                                                                   OpenMetadataType.DESIGN_MODEL_ELEMENT.typeName,
                                                                   null,
                                                                   methodName));
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TECHNICAL_NAME.name, technicalName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.AUTHOR.name, author, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               entityClassifications);

        archiveBuilder.addEntity(newEntity);

        if (designModelGUID != null)
        {
            EntityDetail designModelEntity = archiveBuilder.getEntity(designModelGUID);
            EntityDetail designModelElementEntity = archiveBuilder.getEntity(newEntity.getGUID());

            EntityProxy end1 = archiveHelper.getEntityProxy(designModelEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(designModelElementEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.DESIGN_MODEL_GROUP_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(designModelGUID + "_to_" + newEntity.getGUID() + "_design_model_group_membership_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return newEntity.getGUID();
    }


    /**
     * Create the relationship between a design model group and one of its members.
     *
     * @param groupGUID unique identifier of the design model group
     * @param memberGUID unique identifier of the member
     */
    public void addDesignModelGroupMembership(String groupGUID,
                                              String memberGUID)
    {
        EntityDetail designModelGroupEntity = archiveBuilder.getEntity(groupGUID);
        EntityDetail designModelElementEntity = archiveBuilder.getEntity(memberGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(designModelGroupEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(designModelElementEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.DESIGN_MODEL_GROUP_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(groupGUID + "_to_" + memberGUID + "_design_model_group_membership_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a concept bead link and the concept bead at one of its ends.
     *
     * @param conceptBeadLinkGUID unique identifier of the concept bead link entity
     * @param conceptBeadGUID unique identifier of the concept bead
     * @param attributeName name of the attribute for this end
     * @param conceptModelDecoration what type of relationship end
     * @param position in the attributes for the bead
     * @param minCardinality minimum number of the relationships
     * @param maxCardinality maximum number of the relationships
     * @param uniqueValues are the relationship values unique
     * @param orderedValues are the relationship values in any order (using position)
     * @param navigable is it possible to navigate to the concept bead
     */
    public void addConceptBeadRelationshipEnd(String  conceptBeadLinkGUID,
                                              String  conceptBeadGUID,
                                              String  attributeName,
                                              int     conceptModelDecoration,
                                              int     position,
                                              int     minCardinality,
                                              int     maxCardinality,
                                              boolean uniqueValues,
                                              boolean orderedValues,
                                              boolean navigable)
    {
        final String methodName = "addConceptBeadRelationshipEnd";

        EntityDetail   entityOne                  = archiveBuilder.getEntity(conceptBeadLinkGUID);
        EntityDetail   entityTwo                  = archiveBuilder.getEntity(conceptBeadGUID);
        EnumElementDef conceptModelDecorationEnum = archiveHelper.getEnumElement(ConceptModelDecoration.getOpenTypeName(), conceptModelDecoration);

        EntityProxy end1 = archiveHelper.getEntityProxy(entityOne);
        EntityProxy end2 = archiveHelper.getEntityProxy(entityTwo);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ATTRIBUTE_NAME.name, attributeName, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.POSITION.name, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MIN_CARDINALITY.name, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MAX_CARDINALITY.name, maxCardinality, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DECORATION.name, ConceptModelDecoration.getOpenTypeGUID(), ConceptModelDecoration.getOpenTypeName(), conceptModelDecorationEnum.getOrdinal(), conceptModelDecorationEnum.getValue(), conceptModelDecorationEnum.getDescription(), methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.UNIQUE_VALUES.name, uniqueValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ORDERED_VALUES.name, orderedValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NAVIGABLE.name, navigable, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.DESIGN_MODEL_GROUP_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(conceptBeadLinkGUID + "_to_" + conceptBeadGUID + "_concept_bead_relationship_end_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a concept bead link and the concept bead at one of its ends.
     *
     * @param conceptBeadGUID unique identifier of the concept bead entity
     * @param conceptBeadAttributeGUID unique identifier of the concept bead attribute entity
     * @param position in the attributes for the bead
     * @param minCardinality minimum number of the relationships
     * @param maxCardinality maximum number of the relationships
     * @param uniqueValues are the relationship values unique
     * @param orderedValues are the relationship values in any order (using position)
     */
    public void addConceptBeadAttributeLink(String  conceptBeadGUID,
                                            String  conceptBeadAttributeGUID,
                                            int     position,
                                            int     minCardinality,
                                            int     maxCardinality,
                                            boolean uniqueValues,
                                            boolean orderedValues)
    {
        final String methodName = "addConceptBeadAttributeLink";

        EntityDetail   entityOne   = archiveBuilder.getEntity(conceptBeadGUID);
        EntityDetail   entityTwo   = archiveBuilder.getEntity(conceptBeadAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(entityOne);
        EntityProxy end2 = archiveHelper.getEntityProxy(entityTwo);

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataProperty.POSITION.name, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MIN_CARDINALITY.name, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MAX_CARDINALITY.name, maxCardinality, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.UNIQUE_VALUES.name, uniqueValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ORDERED_VALUES.name, orderedValues, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.DESIGN_MODEL_GROUP_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(conceptBeadGUID + "_to_" + conceptBeadAttributeGUID + "_concept_bead_attribute_link_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Adds a property facet entity and links it to the referenceable via the ReferenceableFacet relationship.
     * The PropertyFacet is anchored to the referenceable.
     *
     * @param referenceableGUID referenceable being described
     * @param referenceableTypeName type name of referenceable
     * @param referenceableDomainName domain name of the referenceable type
     * @param referenceableScopeGUID optional scope of the referencable
     * @param referenceableQualifiedName qualified name of the referenceable
     * @param source source of the extra information
     * @param schemaVersion version of the schema for the properties
     * @param description description of the properties (typically vendorProperties)
     * @param facetProperties properties for the facet
     */
    public void addPropertyFacet(String              referenceableGUID,
                                 String              referenceableTypeName,
                                 String              referenceableDomainName,
                                 String              referenceableScopeGUID,
                                 String              referenceableQualifiedName,
                                 String              source,
                                 String              schemaVersion,
                                 String              description,
                                 Map<String, String> facetProperties)
    {
        final String methodName = "addPropertyFacet";

        String qualifiedName = referenceableQualifiedName + "_propertyFacetFrom_" + source + "@" + schemaVersion;

        List<Classification> entityClassifications = new ArrayList<>();

        String facetGUID = idToGUIDMap.getGUID(qualifiedName);

        entityClassifications.add(this.getAnchorClassification(referenceableGUID, referenceableTypeName, referenceableDomainName, referenceableScopeGUID, methodName));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, schemaVersion, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PROPERTIES.name, facetProperties, methodName);

        EntityDetail facetEntity = archiveHelper.getEntityDetail(OpenMetadataType.PROPERTY_FACET.typeName,
                                                                 facetGUID,
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);
        archiveBuilder.addEntity(facetEntity);

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(facetEntity);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.SOURCE.name, source, methodName);

        Relationship relationship = archiveHelper.getRelationship(OpenMetadataType.REFERENCEABLE_FACET_RELATIONSHIP.typeName,
                                                                  this.idToGUIDMap.getGUID(referenceableGUID + "_to_" + facetGUID + "_referenceable_facet_relationship"),
                                                                  properties,
                                                                  InstanceStatus.ACTIVE,
                                                                  end1,
                                                                  end2);
        archiveBuilder.addRelationship(relationship);
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addAsset(String               typeName,
                           String               qualifiedName,
                           String               name,
                           String               description,
                           Map<String, String>  additionalProperties,
                           Map<String, Object>  extendedProperties,
                           List<Classification> classifications)
    {
        return this.addAsset(typeName, qualifiedName, name, null, description, additionalProperties, extendedProperties, classifications);
    }




    /**
     * Create an asset entity with the supplied anchor.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName type name of the anchor
     * @param anchorDomainName domain name of the anchor
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param versionIdentifier version for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addAnchoredAsset(String               typeName,
                                   String               anchorGUID,
                                   String               anchorTypeName,
                                   String               anchorDomainName,
                                   String               anchorScopeGUID,
                                   String               qualifiedName,
                                   String               name,
                                   String               versionIdentifier,
                                   String               description,
                                   Map<String, String>  additionalProperties,
                                   Map<String, Object>  extendedProperties,
                                   List<Classification> classifications)
    {
        final String methodName = "addAsset";

        String assetTypeName = OpenMetadataType.ASSET.typeName;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        List<Classification> entityClassifications = classifications;

        if (entityClassifications == null)
        {
            entityClassifications = new ArrayList<>();
        }

        String guid = idToGUIDMap.getGUID(qualifiedName);

        entityClassifications.add(this.getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(assetTypeName,
                                                                 guid,
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create an asset entity that is anchored to itself.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param versionIdentifier version for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addAsset(String               typeName,
                           String               qualifiedName,
                           String               name,
                           String               versionIdentifier,
                           String               description,
                           Map<String, String>  additionalProperties,
                           Map<String, Object>  extendedProperties,
                           List<Classification> classifications)
    {
        final String methodName = "addAsset";

        String assetTypeName = OpenMetadataType.ASSET.typeName;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        List<Classification> entityClassifications = classifications;

        if (entityClassifications == null)
        {
            entityClassifications = new ArrayList<>();
        }

        String guid = idToGUIDMap.getGUID(qualifiedName);

        entityClassifications.add(this.getAnchorClassification(guid, assetTypeName, OpenMetadataType.ASSET.typeName, null, methodName));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(assetTypeName,
                                                                 guid,
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param description description about the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              name,
                           String              description,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        return this.addAsset(typeName, qualifiedName, name, description, additionalProperties, extendedProperties, null);
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param description description about the asset
     * @param governanceZones list of zones to add to the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              name,
                           String              description,
                           List<String>        governanceZones,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        return this.addAsset(typeName, qualifiedName, name, null, description, governanceZones, additionalProperties, extendedProperties);
    }


    /**
     * Create an asset entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param versionIdentifier version of the asset
     * @param description description about the asset
     * @param governanceZones list of zones to add to the asset
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return id for the asset
     */
    public String addAsset(String              typeName,
                           String              qualifiedName,
                           String              name,
                           String              versionIdentifier,
                           String              description,
                           List<String>        governanceZones,
                           Map<String, String> additionalProperties,
                           Map<String, Object> extendedProperties)
    {
        final String methodName = "addAsset (with governance zones)";

        if (governanceZones == null)
        {
            return this.addAsset(typeName, qualifiedName, name, versionIdentifier, description, additionalProperties, extendedProperties, null);
        }
        else
        {
            List<Classification> classifications = new ArrayList<>();

            InstanceProperties properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName,
                                                                                           null,
                                                                                           OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                                           governanceZones,
                                                                                           methodName);

            Classification classification = archiveHelper.getClassification(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                            properties,
                                                                            InstanceStatus.ACTIVE);

            classifications.add(classification);

            return this.addAsset(typeName, qualifiedName, name, description, additionalProperties, extendedProperties, classifications);
        }
    }


    /**
     * Create a process entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param description description about the asset
     * @param formula description of the logic that this process performs
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addProcess(String               typeName,
                             String               qualifiedName,
                             String               name,
                             String               description,
                             String               formula,
                             Map<String, String>  additionalProperties,
                             Map<String, Object>  extendedProperties,
                             List<Classification> classifications)
    {
        return this.addProcess(typeName, qualifiedName, name, null, description, formula, additionalProperties, extendedProperties, classifications);
    }


    /**
     * Create a process entity.
     *
     * @param typeName name of asset subtype to use - default is Asset
     * @param qualifiedName unique name for the asset
     * @param name display name for the asset
     * @param versionIdentifier version of the asset
     * @param description description about the asset
     * @param formula description of the logic that this process performs
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     * @param classifications list of classifications (if any)
     *
     * @return id for the asset
     */
    public String addProcess(String               typeName,
                             String               qualifiedName,
                             String               name,
                             String               versionIdentifier,
                             String               description,
                             String               formula,
                             Map<String, String>  additionalProperties,
                             Map<String, Object>  extendedProperties,
                             List<Classification> classifications)
    {
        final String methodName = "addProcess";

        String processTypeName = OpenMetadataType.PROCESS.typeName;

        if (typeName != null)
        {
            processTypeName = typeName;
        }

        List<Classification> entityClassifications = classifications;

        if (entityClassifications == null)
        {
            entityClassifications = new ArrayList<>();
        }

        String guid = idToGUIDMap.getGUID(qualifiedName);

        entityClassifications.add(this.getAnchorClassification(guid, processTypeName, OpenMetadataType.ASSET.typeName, null, methodName));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.FORMULA.name, formula, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(processTypeName,
                                                                 guid,
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 entityClassifications);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create a software capability entity.  It the anchor GUID/typeName is supplied, the new capability
     * is anchored to that entity; otherwise it is its own anchor.
     *
     * @param typeName name of software capability subtype to use - default is SoftwareCapability
     * @param qualifiedName unique name for the capability
     * @param name display name for the capability
     * @param description description about the capability
     * @param deployedImplementationType type
     * @param capabilityVersion version
     * @param patchLevel patch level
     * @param source source
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     * @param anchorGUID optional unique identifier for the anchor
     * @param anchorTypeName  type name for the anchor
     * @param anchorDomainName type name of the anchor's domain
     * @param anchorScopeGUID optional unique identifier for an anchor's scope
     *
     * @return id for the capability
     */
    public String addSoftwareCapability(String              typeName,
                                        String              qualifiedName,
                                        String              name,
                                        String              description,
                                        String              deployedImplementationType,
                                        String              capabilityVersion,
                                        String              patchLevel,
                                        String              source,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties,
                                        Classification      classification,
                                        String              anchorGUID,
                                        String              anchorTypeName,
                                        String              anchorDomainName,
                                        String              anchorScopeGUID)
    {
        List<Classification> classifications = null;

        if (classification != null)
        {
            classifications = new ArrayList<>();
            classifications.add(classification);
        }

        return this.addSoftwareCapability(typeName,
                                          qualifiedName,
                                          name,
                                          description,
                                          deployedImplementationType,
                                          capabilityVersion,
                                          patchLevel,
                                          source,
                                          additionalProperties,
                                          extendedProperties,
                                          classifications,
                                          anchorGUID,
                                          anchorTypeName,
                                          anchorDomainName,
                                          anchorScopeGUID);
    }


    /**
     * Create a software capability entity.  It the anchor GUID/typeName is supplied, the new capability
     * is anchored to that entity; otherwise it is its own anchor.
     *
     * @param typeName name of software capability subtype to use - default is SoftwareCapability
     * @param qualifiedName unique name for the capability
     * @param name display name for the capability
     * @param description description about the capability
     * @param deployedImplementationType type
     * @param capabilityVersion version
     * @param patchLevel patch level
     * @param source source
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     * @param suppliedClassifications classifications from the caller
     * @param anchorGUID optional unique identifier for the anchor
     * @param anchorTypeName optional type name for the anchor.
     * @param anchorDomainName domain name of the anchor
     * @param anchorScopeGUID unique identifier of anchor's scope
     *
     * @return id for the capability
     */
    public String addSoftwareCapability(String               typeName,
                                        String               qualifiedName,
                                        String               name,
                                        String               description,
                                        String               deployedImplementationType,
                                        String               capabilityVersion,
                                        String               patchLevel,
                                        String               source,
                                        Map<String, String>  additionalProperties,
                                        Map<String, Object>  extendedProperties,
                                        List<Classification> suppliedClassifications,
                                        String               anchorGUID,
                                        String               anchorTypeName,
                                        String               anchorDomainName,
                                        String               anchorScopeGUID)
    {
        final String methodName = "addSoftwareCapability";

        String entityTypeName = OpenMetadataType.SOFTWARE_CAPABILITY.typeName;

        if (typeName != null)
        {
            entityTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, capabilityVersion, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PATCH_LEVEL.name, patchLevel, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SOURCE.name, source, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        String guid = idToGUIDMap.getGUID(qualifiedName);
        List<Classification> classifications = suppliedClassifications;

        if (classifications == null)
        {
            classifications = new ArrayList<>();
        }

        if (anchorGUID == null)
        {
            classifications.add(getAnchorClassification(guid,
                                                        entityTypeName,
                                                        OpenMetadataType.SOFTWARE_CAPABILITY.typeName,
                                                        null,
                                                        methodName));
        }
        else
        {
            classifications.add(getAnchorClassification(anchorGUID,
                                                        anchorTypeName,
                                                        anchorDomainName,
                                                        anchorScopeGUID,
                                                        methodName));
        }

        EntityDetail entity = archiveHelper.getEntityDetail(entityTypeName,
                                                            guid,
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            classifications);

        archiveBuilder.addEntity(entity);

        return entity.getGUID();
    }


    /**
     * Create the relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param connectionGUID unique identifier of the connection to its content
     */
    public void addConnectionForAsset(String assetGUID,
                                      String connectionGUID)
    {
        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);
        EntityDetail connectionEntity = archiveBuilder.getEntity(connectionGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(assetEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(connectionEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSET_CONNECTION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(assetGUID + "_to_" + connectionGUID + "_asset_connection_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a data set and an asset that is providing all or part of its content.
     *
     * @param dataContentGUID unique identifier of the data store
     * @param dataSetGUID unique identifier of the consuming data set
     */
    public void addDataContentForDataSet(String dataContentGUID,
                                         String dataSetGUID)
    {
        addDataContentForDataSet(dataContentGUID, dataSetGUID, null, null);
    }




    /**
     * Create the relationship between a data set and an asset that is providing all or part of its content.
     *
     * @param dataContentGUID unique identifier of the data store
     * @param dataSetGUID unique identifier of the consuming data set
     * @param queryId identifier of the query used to combine results in a broader formula of the data set
     * @param query query to issue against this data content
     */
    public void addDataContentForDataSet(String dataContentGUID,
                                         String dataSetGUID,
                                         String queryId,
                                         String query)
    {
        addDataSetContent(dataSetGUID, dataContentGUID, queryId, query, null, null);
    }


    /**
     * Create the relationship between a data set and an asset that is providing all or part of its content.
     *
     * @param dataContentGUID unique identifier of the data store
     * @param dataSetGUID unique identifier of the consuming data set
     * @param queryId identifier of the query used to combine results in a broader formula of the data set
     * @param query query to issue against this data content
     * @param queryType type of encoding/language used by the query (eg SQL)
     * @param iscQualifiedName qualified name of associated information supply chain
     */
    public void addDataSetContent(String dataSetGUID,
                                  String dataContentGUID,
                                  String queryId,
                                  String query,
                                  String queryType,
                                  String iscQualifiedName)
    {
        final String methodName = "addDataSetContent";

        EntityDetail dataSetEntity = archiveBuilder.getEntity(dataSetGUID);
        EntityDetail dataContentEntity = archiveBuilder.getEntity(dataContentGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(dataSetEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(dataContentEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUERY_ID.name, queryId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.QUERY.name, query, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.QUERY_TYPE.name, queryType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ISC_QUALIFIED_NAME.name, iscQualifiedName, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(dataSetGUID + "_to_" + dataContentGUID + "_data_set_content_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a lineage relationship between the two elements
     *
     * @param sourceGUID unique identifier of the element at end 1
     * @param destinationGUID unique identifier of the element at end 2
     * @param relationshipType type name of lineage relationship
     * @param label label of the relationship
     * @param informationSupplyChainQName qualified name of information supply chain that this relationship belongs to
     */
    public void addLineageRelationship(String sourceGUID,
                                       String destinationGUID,
                                       String relationshipType,
                                       String label,
                                       String informationSupplyChainQName)
    {
        final String methodName = "addLineageRelationship";

        EntityDetail end1Entity = archiveBuilder.getEntity(sourceGUID);
        EntityDetail end2Entity = archiveBuilder.getEntity(destinationGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(end1Entity);
        EntityProxy end2 = archiveHelper.getEntityProxy(end2Entity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.LABEL.name, label, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ISC_QUALIFIED_NAME.name, informationSupplyChainQName, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(relationshipType,
                                                                     idToGUIDMap.getGUID(sourceGUID + "_to_" + destinationGUID + "_" + relationshipType + "_" + label + "_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a DataStoreEncoding classification containing the supplied properties.
     *
     * @param encoding encoding format
     * @param language encoding language
     * @param description encoding description
     * @param properties additional properties
     * @return classification
     */
    public Classification getDataAssetEncodingClassification(String              encoding,
                                                             String              language,
                                                             String              description,
                                                             Map<String, String> properties)
    {
        final String methodName = "getDataAssetEncodingClassification";

        InstanceProperties instanceProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                          null,
                                                                                          OpenMetadataProperty.ENCODING_TYPE.name,
                                                                                          encoding,
                                                                                          methodName);

        instanceProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                       instanceProperties,
                                                                       OpenMetadataProperty.ENCODING_LANGUAGE.name,
                                                                       language,
                                                                       methodName);

        instanceProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                       instanceProperties,
                                                                       OpenMetadataProperty.ENCODING_DESCRIPTION.name,
                                                                       description,
                                                                       methodName);

        instanceProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName,
                                                                          instanceProperties,
                                                                          OpenMetadataProperty.ENCODING_PROPERTIES.name,
                                                                          properties,
                                                                          methodName);

        return archiveHelper.getClassification(OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName,
                                               instanceProperties,
                                               InstanceStatus.ACTIVE);
    }



    /**
     * Create an Ownership classification containing the supplied properties.
     *
     * @param owner owner
     * @param typeName owner typeName
     * @param propertyName owner propertyName
     * @return classification
     */
    public Classification getOwnershipClassification(String              owner,
                                                     String              typeName,
                                                     String              propertyName)
    {
        final String methodName = "getOwnershipClassification";

        InstanceProperties instanceProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                          null,
                                                                                          OpenMetadataProperty.OWNER.name,
                                                                                          owner,
                                                                                          methodName);

        instanceProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                       instanceProperties,
                                                                       OpenMetadataProperty.OWNER_TYPE_NAME.name,
                                                                       typeName,
                                                                       methodName);

        instanceProperties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                       instanceProperties,
                                                                       OpenMetadataProperty.OWNER_PROPERTY_NAME.name,
                                                                       propertyName,
                                                                       methodName);


        return archiveHelper.getClassification(OpenMetadataType.OWNERSHIP_CLASSIFICATION.typeName,
                                               instanceProperties,
                                               InstanceStatus.ACTIVE);
    }



    /**
     * Create the top level schema type for an asset.
     *
     * @param assetGUID unique identifier of asset
     * @param assetTypeName typename of the anchor
     * @param typeName name of asset subtype to use - default is SchemaType
     * @param qualifiedName unique name for the schema type
     * @param displayName display name for the schema type
     * @param description description about the schema type
     * @param additionalProperties any other properties
     *
     * @return id for the schemaType
     */
    public String addTopLevelSchemaType(String              assetGUID,
                                        String              assetTypeName,
                                        String              typeName,
                                        String              qualifiedName,
                                        String              displayName,
                                        String              description,
                                        Map<String, String> additionalProperties)
    {
        final String methodName = "addTopLevelSchemaType";

        String schemaTypeTypeName = OpenMetadataType.SCHEMA_TYPE.typeName;

        if (typeName != null)
        {
            schemaTypeTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = null;

        if (assetGUID != null)
        {
            classifications = new ArrayList<>();

            classifications.add(this.getAnchorClassification(assetGUID, assetTypeName, OpenMetadataType.ASSET.typeName, null, methodName));
        }
        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(schemaTypeTypeName,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      classifications);

        archiveBuilder.addEntity(schemaTypeEntity);

        if (assetGUID != null)
        {
            EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(assetEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ASSET_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_asset_schema_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return schemaTypeEntity.getGUID();
    }


    /**
     * Create the schema type by do not link it it a parent.
     *
     * @param assetGUID anchor guid
     * @param assetTypeName anchor type name
     * @param typeName type name of schema type
     * @param qualifiedName qualified name
     * @param displayName display name
     * @param description description
     * @param additionalProperties any additional properties
     * @return guid of the schema type
     */
    public String addSchemaType(String              assetGUID,
                                String              assetTypeName,
                                String              typeName,
                                String              qualifiedName,
                                String              displayName,
                                String              description,
                                Map<String, String> additionalProperties)
    {
        final String methodName = "addSchemaType";

        String schemaTypeTypeName = OpenMetadataType.SCHEMA_TYPE.typeName;

        if (typeName != null)
        {
            schemaTypeTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = null;

        if (assetGUID != null)
        {
            classifications = new ArrayList<>();

            classifications.add(this.getAnchorClassification(assetGUID, assetTypeName, OpenMetadataType.ASSET.typeName, null, methodName));
        }
        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(schemaTypeTypeName,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      classifications);

        archiveBuilder.addEntity(schemaTypeEntity);

        return schemaTypeEntity.getGUID();
    }





    /**
     * Create the schema type for an API operation.
     *
     * @param assetGUID anchor guid
     * @param assetTypeName anchor type name
     * @param apiSchemaTypeGUID unique identifier of top level schemaType
     * @param qualifiedName unique name for the schema type
     * @param displayName display name for the schema type
     * @param description description about the schema type
     * @param path the path name for the operation
     * @param command the command to issue eg GET, POST
     * @param additionalProperties any other properties
     *
     * @return id for the schemaType
     */
    public String addAPIOperation(String              assetGUID,
                                  String              assetTypeName,
                                  String              apiSchemaTypeGUID,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              path,
                                  String              command,
                                  Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIOperation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PATH.name, path, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.COMMAND.name, command, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = null;

        if (assetGUID != null)
        {
            classifications = new ArrayList<>();

            classifications.add(this.getAnchorClassification(assetGUID, assetTypeName, OpenMetadataType.ASSET.typeName, null, methodName));
        }

        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(OpenMetadataType.API_OPERATION.typeName,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      classifications);

        archiveBuilder.addEntity(schemaTypeEntity);

        if (apiSchemaTypeGUID != null)
        {
            EntityDetail parentEntity = archiveBuilder.getEntity(apiSchemaTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(parentEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.API_OPERATIONS_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_parent_api_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return schemaTypeEntity.getGUID();
    }


    /**
     * Create a parameter list schema type for an API operation.
     *
     * @param assetGUID anchor guid
     * @param assetTypeName anchor type name
     * @param apiOperationGUID unique identifier of top level schemaType
     * @param relationshipTypeName name of relationship type - default is APIRequest
     * @param qualifiedName unique name for the schema type
     * @param displayName display name for the schema type
     * @param description description about the schema type
     * @param required is this parameter list required
     * @param additionalProperties any other properties
     *
     * @return id for the schemaType
     */
    public String addAPIParameterList(String              assetGUID,
                                      String              assetTypeName,
                                      String              apiOperationGUID,
                                      String              relationshipTypeName,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      boolean             required,
                                      Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIParameterList";

        String typeName = OpenMetadataType.API_REQUEST_RELATIONSHIP.typeName;

        if (relationshipTypeName != null)
        {
            typeName = relationshipTypeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.REQUIRED.name, required, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = null;

        if (assetGUID != null)
        {
            classifications = new ArrayList<>();

            classifications.add(this.getAnchorClassification(assetGUID, assetTypeName, OpenMetadataType.ASSET.typeName, null, methodName));
        }

        EntityDetail parameterListEntity = archiveHelper.getEntityDetail(OpenMetadataType.API_PARAMETER_LIST.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         classifications);

        archiveBuilder.addEntity(parameterListEntity);

        if (apiOperationGUID != null)
        {
            EntityDetail operationEntity = archiveBuilder.getEntity(apiOperationGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(operationEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(parameterListEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_api_parameter_to_operation_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return parameterListEntity.getGUID();
    }


    /**
     * Create a schema attribute with a TypeEmbeddedAttribute classification.
     *
     * @param typeName name of schema attribute subtype to use - default is SchemaAttribute
     * @param schemaTypeName name of schema type subtype to use - default is PrimitiveSchemaType
     * @param qualifiedName unique name for the schema attribute
     * @param displayName display name for the schema attribute
     * @param description description about the schema attribute
     * @param dataType data type for the schema attribute
     * @param length length of the storage used by the schema attribute
     * @param position position in the schema at this level
     * @param parameterType type of parameter
     * @param additionalProperties any other properties.
     *
     * @return id for the schema attribute
     */
    public String addAPIParameter(String              typeName,
                                  String              schemaTypeName,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              dataType,
                                  int                 length,
                                  int                 position,
                                  String              parameterType,
                                  Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIParameter";

        String schemaAttributeTypeName = OpenMetadataType.API_PARAMETER.typeName;

        if (typeName != null)
        {
            schemaAttributeTypeName = typeName;
        }

        String schemaTypeTypeName = OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName;

        if (schemaTypeName != null)
        {
            schemaTypeTypeName = schemaTypeName;
        }

        InstanceProperties entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        entityProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.POSITION.name, position, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.PARAMETER_TYPE.name, parameterType, methodName);
        entityProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.SCHEMA_TYPE_NAME.name, schemaTypeTypeName, methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataProperty.DATA_TYPE.name, dataType, methodName);
        classificationProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataProperty.LENGTH.name, length, methodName);

        Classification classification = archiveHelper.getClassification(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, classificationProperties, InstanceStatus.ACTIVE);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(classification);

        EntityDetail schemaAttributeEntity = archiveHelper.getEntityDetail(schemaAttributeTypeName,
                                                                           idToGUIDMap.getGUID(qualifiedName),
                                                                           entityProperties,
                                                                           InstanceStatus.ACTIVE,
                                                                           classifications);

        archiveBuilder.addEntity(schemaAttributeEntity);

        return schemaAttributeEntity.getGUID();
    }


    /**
     * Create the relationship between a SchemaTypeChoice element and a child element using the SchemaTypeOption relationship.
     *
     * @param schemaTypeChoiceGUID unique identifier of the parent element
     * @param schemaTypeOptionGUID unique identifier of the child element
     */
    public void addSchemaTypeOption(String schemaTypeChoiceGUID,
                                    String schemaTypeOptionGUID)
    {
        EntityDetail schemaTypeChoiceEntity = archiveBuilder.getEntity(schemaTypeChoiceGUID);
        EntityDetail schemaTypeOptionEntity = archiveBuilder.getEntity(schemaTypeOptionGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(schemaTypeChoiceEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeOptionEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SCHEMA_TYPE_OPTION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(schemaTypeChoiceGUID + "_to_" + schemaTypeOptionGUID + "_schema_type_option_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a SchemaType element and a child SchemaAttribute element using the AttributeForSchema relationship.
     *
     * @param schemaTypeGUID unique identifier of the parent element
     * @param position position in the schema at this level
     * @param minCardinality minimum number of instances in the schema at this level
     * @param maxCardinality maximum number of instances in the schema at this level
     * @param schemaAttributeGUID unique identifier of the child element
     */
    public void addAttributeForSchemaType(String schemaTypeGUID,
                                          int    position,
                                          int    minCardinality,
                                          int    maxCardinality,
                                          String schemaAttributeGUID)
    {
        final String methodName = "addAttributeForSchemaType";

        EntityDetail schemaTypeChoiceEntity = archiveBuilder.getEntity(schemaTypeGUID);
        EntityDetail schemaTypeOptionEntity = archiveBuilder.getEntity(schemaAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(schemaTypeChoiceEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeOptionEntity);

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataProperty.POSITION.name, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MIN_CARDINALITY.name, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MAX_CARDINALITY.name, maxCardinality, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(schemaTypeGUID + "_to_" + schemaAttributeGUID + "_attribute_for_schema_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a SchemaAttribute element and a child SchemaAttribute element using the NestedSchemaAttribute relationship.
     *
     * @param parentSchemaAttributeGUID unique identifier of the parent element
     * @param position position in the schema at this level
     * @param minCardinality minimum number of instances in the schema at this level
     * @param maxCardinality maximum number of instances in the schema at this level
     * @param childSchemaAttributeGUID unique identifier of the child element
     */
    public void addNestedSchemaAttribute(String parentSchemaAttributeGUID,
                                         int    position,
                                         int    minCardinality,
                                         int    maxCardinality,
                                         String childSchemaAttributeGUID)
    {
        final String methodName = "addNestedSchemaAttribute";

        EntityDetail parentSchemaAttributeEntity = archiveBuilder.getEntity(parentSchemaAttributeGUID);
        EntityDetail childSchemaAttributeEntity = archiveBuilder.getEntity(childSchemaAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(parentSchemaAttributeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(childSchemaAttributeEntity);

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataProperty.POSITION.name, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MIN_CARDINALITY.name, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MAX_CARDINALITY.name, maxCardinality, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(parentSchemaAttributeGUID + "_to_" + childSchemaAttributeGUID + "_nested_schema_attribute_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a schema attribute with a TypeEmbeddedAttribute classification.
     *
     * @param assetGUID anchor GUID (optional)
     * @param assetTypeName anchorTypeName (needed if assetGUID is set)
     * @param typeName name of schema attribute subtype to use - default is SchemaAttribute
     * @param schemaTypeName name of schema type subtype to use - default is PrimitiveSchemaType
     * @param qualifiedName unique name for the schema attribute
     * @param displayName display name for the schema attribute
     * @param description description about the schema attribute
     * @param dataType data type for the schema attribute
     * @param length length of the storage used by the schema attribute
     * @param encodingStandard encoding standard
     * @param additionalProperties any other properties.
     *
     * @return id for the schema attribute
     */
    public String addSchemaAttribute(String              assetGUID,
                                     String              assetTypeName,
                                     String              typeName,
                                     String              schemaTypeName,
                                     String              qualifiedName,
                                     String              displayName,
                                     String              description,
                                     String              dataType,
                                     int                 length,
                                     String              encodingStandard,
                                     Map<String, String> additionalProperties)
    {
        final String methodName = "addSchemaAttribute";

        String schemaAttributeTypeName = OpenMetadataType.SCHEMA_ATTRIBUTE.typeName;

        if (typeName != null)
        {
            schemaAttributeTypeName = typeName;
        }

        String schemaTypeTypeName = OpenMetadataType.PRIMITIVE_SCHEMA_TYPE.typeName;

        if (schemaTypeName != null)
        {
            schemaTypeTypeName = schemaTypeName;
        }

        InstanceProperties entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        entityProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.LENGTH.name, length, methodName);
        entityProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, entityProperties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.SCHEMA_TYPE_NAME.name, schemaTypeTypeName, methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataProperty.DATA_TYPE.name, dataType, methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataProperty.ENCODING_STANDARD.name, encodingStandard, methodName);

        Classification classification = archiveHelper.getClassification(OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName, classificationProperties, InstanceStatus.ACTIVE);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(classification);

        if (assetGUID != null)
        {
            classifications.add(this.getAnchorClassification(assetGUID, assetTypeName, OpenMetadataType.ASSET.typeName, null, methodName));
        }

        EntityDetail schemaAttributeEntity = archiveHelper.getEntityDetail(schemaAttributeTypeName,
                                                                           idToGUIDMap.getGUID(qualifiedName),
                                                                           entityProperties,
                                                                           InstanceStatus.ACTIVE,
                                                                           classifications);

        archiveBuilder.addEntity(schemaAttributeEntity);

        return schemaAttributeEntity.getGUID();
    }


    /**
     * Create a connection entity.
     *
     * @param qualifiedName unique name for the connection
     * @param displayName display name for the connection
     * @param description description about the connection
     * @param userId userId that the connector should use to connect to the platform that hosts the asset.
     * @param clearPassword possible password for the connector
     * @param encryptedPassword possible password for the connector
     * @param securedProperties properties hidden from the client
     * @param configurationProperties properties used to configure the connector
     * @param additionalProperties any other properties.
     * @param connectorTypeGUID unique identifier for the connector type
     * @param endpointGUID unique identifier for the endpoint of the asset
     * @param anchorGUID unique identifier for the anchor - can be null
     * @param anchorTypeName unique type name of the anchor - set if anchorGUID set.
     * @param anchorDomainName unique type name of the anchor's domain - set if anchorGUID set.
     * @param anchorScopeGUID optional unique identifier of the anchor's scope - set if anchorGUID set
     *
     * @return id for the connection
     */
    public String addConnection(String              qualifiedName,
                                String              displayName,
                                String              description,
                                String              userId,
                                String              clearPassword,
                                String              encryptedPassword,
                                Map<String, String> securedProperties,
                                Map<String, Object> configurationProperties,
                                Map<String, String> additionalProperties,
                                String              connectorTypeGUID,
                                String              endpointGUID,
                                String              anchorGUID,
                                String              anchorTypeName,
                                String              anchorDomainName,
                                String              anchorScopeGUID)
    {
        return this.addConnection(OpenMetadataType.CONNECTION.typeName,
                                  qualifiedName,
                                  displayName,
                                  description,
                                  userId,
                                  clearPassword,
                                  encryptedPassword,
                                  securedProperties,
                                  configurationProperties,
                                  additionalProperties,
                                  connectorTypeGUID,
                                  endpointGUID,
                                  anchorGUID,
                                  anchorTypeName,
                                  anchorDomainName,
                                  anchorScopeGUID);
    }


    /**
     * Create a connection entity.
     *
     * @param qualifiedName unique name for the connection
     * @param displayName display name for the connection
     * @param description description about the connection
     * @param userId userId that the connector should use to connect to the platform that hosts the asset.
     * @param clearPassword possible password for the connector
     * @param encryptedPassword possible password for the connector
     * @param securedProperties properties hidden from the client
     * @param configurationProperties properties used to configure the connector
     * @param additionalProperties any other properties.
     * @param connectorTypeGUID unique identifier for the connector type
     * @param endpointGUID unique identifier for the endpoint of the asset
     * @param anchorGUID unique identifier for the anchor - can be null
     * @param anchorTypeName unique type name of the anchor - set if anchorGUID set
     * @param anchorDomainName unique type name of the anchor's domain - set if anchorGUID set
     * @param anchorScopeGUID optional unique identifier of the anchor's scope - set if anchorGUID set
     *
     * @return id for the connection
     */
    public String addConnection(String              typeName,
                                String              qualifiedName,
                                String              displayName,
                                String              description,
                                String              userId,
                                String              clearPassword,
                                String              encryptedPassword,
                                Map<String, String> securedProperties,
                                Map<String, Object> configurationProperties,
                                Map<String, String> additionalProperties,
                                String              connectorTypeGUID,
                                String              endpointGUID,
                                String              anchorGUID,
                                String              anchorTypeName,
                                String              anchorDomainName,
                                String              anchorScopeGUID)
    {
        final String methodName = "addConnection";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USER_ID.name, userId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CLEAR_PASSWORD.name, clearPassword, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ENCRYPTED_PASSWORD.name, encryptedPassword, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SECURED_PROPERTIES.name, securedProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONFIGURATION_PROPERTIES.name, configurationProperties, methodName);

        List<Classification> entityClassifications = null;

        if (anchorGUID != null)
        {
            entityClassifications = new ArrayList<>();

            entityClassifications.add(getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
        }

        EntityDetail connectionEntity = archiveHelper.getEntityDetail(typeName,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      entityClassifications);

        archiveBuilder.addEntity(connectionEntity);

        if (connectorTypeGUID != null)
        {
            EntityDetail connectorTypeEntity = archiveBuilder.getEntity(connectorTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectionEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CONNECTION_CONNECTOR_TYPE_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connectorType_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (endpointGUID != null)
        {
            EntityDetail endpointEntity = archiveBuilder.getEntity(endpointGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectionEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(endpointEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CONNECT_TO_ENDPOINT_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_endpoint_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return connectionEntity.getGUID();
    }


    /**
     * Add an EmbeddedConnection relationship.
     *
     * @param parentConnectionGUID virtual connection
     * @param position position of child connector
     * @param displayName name of embedded/child connector
     * @param arguments additional properties
     * @param childConnectionGUID embedded connection
     */
    public void addEmbeddedConnection(String              parentConnectionGUID,
                                      int                 position,
                                      String              displayName,
                                      Map<String, Object> arguments,
                                      String              childConnectionGUID)
    {
        final String methodName = "addEmbeddedConnection";

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName,
                                                                               null,
                                                                               OpenMetadataProperty.POSITION.name,
                                                                               position,
                                                                               methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.DISPLAY_NAME.name,
                                                               displayName,
                                                               methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName,
                                                            properties,
                                                            OpenMetadataProperty.ARGUMENTS.name,
                                                            arguments,
                                                            methodName);

        EntityDetail parentEntity = archiveBuilder.getEntity(parentConnectionGUID);
        EntityDetail childEntity = archiveBuilder.getEntity(childConnectionGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(parentEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(childEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.EMBEDDED_CONNECTION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(parentConnectionGUID + "_to_" + childConnectionGUID + "_embedded_connection_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

    }


    /**
     * Create a connector type entity.  It is lined to a connector category if supplied.
     *
     * @param connectorCategoryGUID unique identifier of connector category - or null is not categorized
     * @param connectorProvider OCF Connector Provider
     *
     * @return id for the connector type
     */
    public String addConnectorType(String            connectorCategoryGUID,
                                   ConnectorProvider connectorProvider)
    {
        return this.addConnectorType(connectorCategoryGUID, connectorProvider.getConnectorType());
    }


    /**
     * Create a connector type entity.  It is lined to a connector category if supplied.
     *
     * @param connectorCategoryGUID unique identifier of connector category - or null is not categorized
     * @param connectorProvider OCF Connector Provider
     *
     * @return id for the connector type
     */
    public String addConnectorType(String                       connectorCategoryGUID,
                                   IntegrationConnectorProvider connectorProvider)
    {
        String connectorTypeGUID = this.addConnectorType(connectorCategoryGUID, connectorProvider.getConnectorType());

        if (connectorProvider.getCatalogTargets() != null)
        {
            for (CatalogTargetType catalogTarget : connectorProvider.getCatalogTargets())
            {
                if (catalogTarget != null)
                {
                    DeployedImplementationType deployedImplementationType =
                           DeployedImplementationType.getDefinitionFromDeployedImplementationType(catalogTarget.getDeployedImplementationType());
                    ConnectorType connectorType = connectorProvider.getConnectorType();

                    if ((deployedImplementationType != null) && (connectorType != null))
                    {
                        this.addResourceListRelationship(deployedImplementationType.getQualifiedName(),
                                                         connectorType.getQualifiedName(),
                                                         ResourceUse.CONFIGURE_CONNECTOR.getResourceUse(),
                                                         ResourceUse.CONFIGURE_CONNECTOR.getDescription());
                    }
                }
            }
        }

        return connectorTypeGUID;

    }


    /**
     * Create a connector type entity.  It is lined to a connector category if supplied.
     *
     * @param connectorCategoryGUID unique identifier of connector category - or null is not categorized
     * @param connectorType OCF connector type - comes from the Connector Provider
     *
     * @return id for the connector type
     */
    public String addConnectorType(String        connectorCategoryGUID,
                                   ConnectorType connectorType)
    {
        idToGUIDMap.setGUID(connectorType.getQualifiedName(), connectorType.getGUID());

        try
        {
            return this.addConnectorType(connectorCategoryGUID,
                                         connectorType.getGUID(),
                                         connectorType.getQualifiedName(),
                                         connectorType.getDisplayName(),
                                         connectorType.getDescription(),
                                         connectorType.getSupportedDeployedImplementationType(),
                                         connectorType.getSupportedAssetTypeName(),
                                         connectorType.getExpectedDataFormat(),
                                         connectorType.getConnectorProviderClassName(),
                                         connectorType.getConnectorFrameworkName(),
                                         connectorType.getConnectorInterfaceLanguage(),
                                         connectorType.getConnectorInterfaces(),
                                         connectorType.getTargetTechnologySource(),
                                         connectorType.getTargetTechnologyName(),
                                         connectorType.getTargetTechnologyInterfaces(),
                                         connectorType.getTargetTechnologyVersions(),
                                         connectorType.getRecognizedSecuredProperties(),
                                         connectorType.getRecognizedConfigurationProperties(),
                                         connectorType.getRecognizedAdditionalProperties(),
                                         connectorType.getAdditionalProperties());
        }
        catch (Exception alreadyDefined)
        {
            return connectorType.getGUID();
        }
    }


    /**
     * Create a connector type entity.  It is lined to a connector category if supplied.
     *
     * @param connectorCategoryGUID unique identifier of connector category - or null is not categorized
     * @param connectorTypeGUID fixed unique identifier for connector type - comes from the Connector Provider
     * @param qualifiedName unique name for the connector type
     * @param displayName display name for the connector type
     * @param description description about the connector type
     * @param supportedAssetTypeName type of asset supported by this connector
     * @param deployedImplementationType specific deployed implementation type for assets supported by this connector
     * @param expectedDataFormat format of the data stored in the resource
     * @param connectorProviderClassName code for this type of connector
     * @param connectorFrameworkName name of the framework that the connector implements - default "Open Connector Framework (OCF)"
     * @param connectorInterfaceLanguage programming language of the connector's interface
     * @param connectorInterfaces the interfaces that the connector implements
     * @param targetTechnologySource organization implementing the target technology
     * @param targetTechnologyName name of the target technology
     * @param targetTechnologyInterfaces called interfaces the target technology
     * @param targetTechnologyVersions supported versions of the target technology
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    public String addConnectorType(String              connectorCategoryGUID,
                                   String              connectorTypeGUID,
                                   String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   String              deployedImplementationType,
                                   String              supportedAssetTypeName,
                                   String              expectedDataFormat,
                                   String              connectorProviderClassName,
                                   String              connectorFrameworkName,
                                   String              connectorInterfaceLanguage,
                                   List<String>        connectorInterfaces,
                                   String              targetTechnologySource,
                                   String              targetTechnologyName,
                                   List<String>        targetTechnologyInterfaces,
                                   List<String>        targetTechnologyVersions,
                                   List<String>        recognizedSecuredProperties,
                                   List<String>        recognizedConfigurationProperties,
                                   List<String>        recognizedAdditionalProperties,
                                   Map<String, String> additionalProperties)
    {
        idToGUIDMap.setGUID(qualifiedName, connectorTypeGUID);

        try
        {
            return this.addConnectorType(connectorCategoryGUID,
                                         qualifiedName,
                                         displayName,
                                         description,
                                         deployedImplementationType,
                                         supportedAssetTypeName,
                                         expectedDataFormat,
                                         connectorProviderClassName,
                                         connectorFrameworkName,
                                         connectorInterfaceLanguage,
                                         connectorInterfaces,
                                         targetTechnologySource,
                                         targetTechnologyName,
                                         targetTechnologyInterfaces,
                                         targetTechnologyVersions,
                                         recognizedSecuredProperties,
                                         recognizedConfigurationProperties,
                                         recognizedAdditionalProperties,
                                         additionalProperties);
        }
        catch (Exception alreadyDefined)
        {
            return connectorTypeGUID;
        }
    }


    /**
     * Create a connector type entity.
     *
     * @param connectorCategoryGUID unique identifier of connector category - or null is not categorized
     * @param qualifiedName unique name for the connector type
     * @param displayName display name for the connector type
     * @param description description about the connector type
     * @param deployedImplementationType specific deployed implementation type for assets supported by this connector
     * @param supportedAssetTypeName type of asset supported by this connector
     * @param expectedDataFormat format of the data stored in the resource
     * @param connectorProviderClassName code for this type of connector
     * @param connectorFrameworkName name of the framework that the connector implements - default "Open Connector Framework (OCF)"
     * @param connectorInterfaceLanguage programming language of the connector's interface
     * @param connectorInterfaces the interfaces that the connector implements
     * @param targetTechnologySource organization implementing the target technology
     * @param targetTechnologyName name of the target technology
     * @param targetTechnologyInterfaces called interfaces the target technology
     * @param targetTechnologyVersions supported versions of the target technology
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    protected String addConnectorType(String                          connectorCategoryGUID,
                                      String                          qualifiedName,
                                      String                          displayName,
                                      String                          description,
                                      String                          deployedImplementationType,
                                      String                          supportedAssetTypeName,
                                      String                          expectedDataFormat,
                                      String                          connectorProviderClassName,
                                      String                          connectorFrameworkName,
                                      String                          connectorInterfaceLanguage,
                                      List<String>                    connectorInterfaces,
                                      String                          targetTechnologySource,
                                      String                          targetTechnologyName,
                                      List<String>                    targetTechnologyInterfaces,
                                      List<String>                    targetTechnologyVersions,
                                      List<String>                    recognizedSecuredProperties,
                                      List<String>                    recognizedConfigurationProperties,
                                      List<String>                    recognizedAdditionalProperties,
                                      Map<String, String>             additionalProperties)
    {
        final String methodName = "addConnectorType";

        EntityDetail connectorTypeEntity = archiveBuilder.queryEntity(getGUID(qualifiedName));

        if (connectorTypeEntity == null)
        {
            InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SUPPORTED_DEPLOYED_IMPLEMENTATION_TYPE.name, deployedImplementationType, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SUPPORTED_ASSET_TYPE_NAME.name, supportedAssetTypeName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.EXPECTED_DATA_FORMAT.name, expectedDataFormat, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTOR_PROVIDER_CLASS_NAME.name, connectorProviderClassName, methodName);
            if (connectorFrameworkName != null)
            {
                properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME.name, connectorFrameworkName, methodName);
            }
            else
            {
                properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTOR_FRAMEWORK_NAME.name, OpenConnectorsValidValues.CONNECTOR_FRAMEWORK_DEFAULT, methodName);
            }
            if (connectorInterfaceLanguage != null)
            {
                properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE.name, connectorInterfaceLanguage, methodName);
            }
            else
            {
                properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTOR_INTERFACE_LANGUAGE.name, OpenConnectorsValidValues.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT, methodName);
            }
            properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONNECTOR_INTERFACES.name, connectorInterfaces, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE.name, targetTechnologySource, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TARGET_TECHNOLOGY_NAME.name, targetTechnologyName, methodName);
            properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TARGET_TECHNOLOGY_INTERFACES.name, targetTechnologyInterfaces, methodName);
            properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TARGET_TECHNOLOGY_VERSIONS.name, targetTechnologyVersions, methodName);
            properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES.name, recognizedSecuredProperties, methodName);
            properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES.name, recognizedAdditionalProperties, methodName);
            properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES.name, recognizedConfigurationProperties, methodName);
            properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

            String guid = idToGUIDMap.getGUID(qualifiedName);

            List<Classification> classifications = new ArrayList<>();

            classifications.add(this.getAnchorClassification(guid,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                             OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                             null,
                                                             methodName));

            connectorTypeEntity = archiveHelper.getEntityDetail(OpenMetadataType.CONNECTOR_TYPE.typeName,
                                                                guid,
                                                                properties,
                                                                InstanceStatus.ACTIVE,
                                                                classifications);

            archiveBuilder.addEntity(connectorTypeEntity);

            if (connectorCategoryGUID != null)
            {
                EntityDetail connectorCategoryEntity = archiveBuilder.getEntity(connectorCategoryGUID);

                EntityProxy end1 = archiveHelper.getEntityProxy(connectorCategoryEntity);
                EntityProxy end2 = archiveHelper.getEntityProxy(connectorTypeEntity);

                archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CONNECTOR_IMPLEMENTATION_CHOICE_RELATIONSHIP.typeName,
                                                                             idToGUIDMap.getGUID(qualifiedName + "_connector_implementation_choice_relationship"),
                                                                             null,
                                                                             InstanceStatus.ACTIVE,
                                                                             end1,
                                                                             end2));
            }

            DeployedImplementationType definition = DeployedImplementationType.getDefinitionFromDeployedImplementationType(deployedImplementationType);

            if ((definition != null) && (archiveBuilder.queryEntity(this.idToGUIDMap.getGUID(definition.getQualifiedName())) != null))
            {
                this.addResourceListRelationship(definition.getQualifiedName(),
                                                 qualifiedName,
                                                 ResourceUse.CONFIGURE_CONNECTOR.getResourceUse(),
                                                 ResourceUse.CONFIGURE_CONNECTOR.getDescription());
            }
        }

        return connectorTypeEntity.getGUID();
    }


    /**
     * Create a connector category entity.
     *
     * @param connectorTypeDirectoryGUID unique identifier of connector type directory that this connector belongs to - or null for an independent connector category
     * @param qualifiedName unique name for the connector category
     * @param displayName display name for the connector category
     * @param description description about the connector category
     * @param targetTechnologySource organization implementing the target technology
     * @param targetTechnologyName name of the target technology
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    public    String addConnectorCategory(String               connectorTypeDirectoryGUID,
                                          String               qualifiedName,
                                          String               displayName,
                                          String               description,
                                          String               targetTechnologySource,
                                          String               targetTechnologyName,
                                          Map<String, Boolean> recognizedSecuredProperties,
                                          Map<String, Boolean> recognizedConfigurationProperties,
                                          Map<String, Boolean> recognizedAdditionalProperties,
                                          Map<String, String>  additionalProperties)
    {
        final String methodName = "addConnectorCategory";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TARGET_TECHNOLOGY_SOURCE.name, targetTechnologySource, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TARGET_TECHNOLOGY_NAME.name, targetTechnologyName, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.RECOGNIZED_SECURED_PROPERTIES.name, recognizedSecuredProperties, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.RECOGNIZED_ADDITIONAL_PROPERTIES.name, recognizedAdditionalProperties, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.RECOGNIZED_CONFIGURATION_PROPERTIES.name, recognizedConfigurationProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(connectorTypeDirectoryGUID,
                                                         OpenMetadataType.COLLECTION.typeName,
                                                         OpenMetadataType.COLLECTION.typeName,
                                                         null,
                                                         methodName));

        EntityDetail connectorCategoryEntity = archiveHelper.getEntityDetail(OpenMetadataType.CONNECTOR_CATEGORY.typeName,
                                                                             idToGUIDMap.getGUID(qualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

        archiveBuilder.addEntity(connectorCategoryEntity);

        if (connectorTypeDirectoryGUID != null)
        {
            EntityDetail connectorTypeDirectoryEntity = archiveBuilder.getEntity(connectorTypeDirectoryGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectorTypeDirectoryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorCategoryEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_collection_membership_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return connectorCategoryEntity.getGUID();
    }


    /**
     * Create a connector category entity.
     *
     * @param qualifiedName unique name for the connector type directory
     * @param displayName display name for the connector type directory
     * @param description description about the connector type directory
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    public String addConnectorTypeDirectory(String              qualifiedName,
                                            String              displayName,
                                            String              description,
                                            Map<String, String> additionalProperties)
    {
        final String methodName = "addConnectorTypeDirectory";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        String guid = idToGUIDMap.getGUID(qualifiedName);

        List<Classification> classifications = new ArrayList<>();

        Classification classification = archiveHelper.getClassification(OpenMetadataType.CONNECTOR_TYPE_DIRECTORY_CLASSIFICATION.typeName, null, InstanceStatus.ACTIVE);

        classifications.add(classification);
        classifications.add(getAnchorClassification(guid,
                                                    OpenMetadataType.COLLECTION.typeName,
                                                    OpenMetadataType.COLLECTION.typeName,
                                                    null,
                                                    methodName));

        EntityDetail connectorTypeDirectoryEntity = archiveHelper.getEntityDetail(OpenMetadataType.COLLECTION.typeName,
                                                                                  guid,
                                                                                  properties,
                                                                                  InstanceStatus.ACTIVE,
                                                                                  classifications);

        archiveBuilder.addEntity(connectorTypeDirectoryEntity);

        return connectorTypeDirectoryEntity.getGUID();
    }

    /**
     * Create a Template classification to add to an entity as it is created.
     *
     * @param format format of the file system
     * @param encryption type of encryption
     * @param methodName calling method
     * @return classification object
     */
    public Classification getFileSystemClassification(String              format,
                                                      String              encryption,
                                                      String              methodName)
    {
        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null,
                                                                                                OpenMetadataProperty.FORMAT.name,
                                                                                                format, methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties,
                                                                             OpenMetadataProperty.ENCRYPTION.name,
                                                                             encryption, methodName);


        return archiveHelper.getClassification(OpenMetadataType.FILE_SYSTEM_CLASSIFICATION.typeName,
                                               classificationProperties,
                                               InstanceStatus.ACTIVE);
    }


    /**
     * Create a Template classification to add to an entity as it is created.
     *
     * @param templateName name of template
     * @param templateDescription description of template
     * @param versionIdentifier identifier of the template
     * @param additionalProperties additional properties
     * @param methodName calling method
     * @return classification object
     */
    public Classification getTemplateClassification(String              templateName,
                                                    String              templateDescription,
                                                    String              versionIdentifier,
                                                    Map<String, String> additionalProperties,
                                                    String              methodName)
    {
        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null,
                                                                                                OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                templateName, methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties,
                                                                             OpenMetadataProperty.DESCRIPTION.name,
                                                                             templateDescription, methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties,
                                                                             OpenMetadataProperty.VERSION_IDENTIFIER.name,
                                                                             versionIdentifier, methodName);

        classificationProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, classificationProperties,
                                                                                OpenMetadataProperty.ADDITIONAL_PROPERTIES.name,
                                                                                additionalProperties, methodName);

        return archiveHelper.getClassification(OpenMetadataType.TEMPLATE_CLASSIFICATION.typeName,
                                               classificationProperties, InstanceStatus.ACTIVE);
    }



    /**
     * Add a Template classification to an entity.
     *
     * @param elementGUID guid to attach it to
     * @param templateName name of template
     * @param templateDescription description of template
     * @param versionIdentifier identifier of the template
     * @param additionalProperties additional properties
     * @param methodName calling method
     */
    public void addTemplateClassification(String              elementGUID,
                                          String              templateName,
                                          String              templateDescription,
                                          String              versionIdentifier,
                                          Map<String, String> additionalProperties,
                                          String              methodName)
    {
        EntityDetail entity = archiveBuilder.getEntity(elementGUID);
        EntityProxy entityProxy = archiveHelper.getEntityProxy(entity);
        Classification tempateClassification = this.getTemplateClassification(templateName, templateDescription, versionIdentifier, additionalProperties, methodName);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, tempateClassification));
    }



    /**
     * Return the TemplateSubstitute classification.
     *
     * @return classification
     */
    public Classification getTemplateSubstituteClassification()
    {
        return archiveHelper.getClassification(OpenMetadataType.TEMPLATE_SUBSTITUTE_CLASSIFICATION.typeName,
                                               null, InstanceStatus.ACTIVE);
    }



    /**
     * Create a sourced from relationship.
     *
     * @param resultingElementGUID the element created
     * @param sourceVersionNumber the version of the template used for the creation
     * @param templateElementGUID the template used
     * @param methodName calling method
     */
    public void addSourcedFromRelationship(String resultingElementGUID,
                                           long   sourceVersionNumber,
                                           String templateElementGUID,
                                           String methodName)
    {
        InstanceProperties relationshipProperties = archiveHelper.addLongPropertyToInstance(archiveRootName,
                                                                                            null,
                                                                                            OpenMetadataProperty.SOURCE_VERSION_NUMBER.name,
                                                                                            sourceVersionNumber,
                                                                                            methodName);
        EntityDetail end1Entity = archiveBuilder.getEntity(resultingElementGUID);
        EntityDetail end2Entity = archiveBuilder.getEntity(templateElementGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(end1Entity);
        EntityProxy end2 = archiveHelper.getEntityProxy(end2Entity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SOURCED_FROM_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(resultingElementGUID + "_to_" + templateElementGUID + "_sourced_from_relationship"),
                                                                     relationshipProperties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a catalog template relationship.
     *
     * @param owningElementGUID the element to catalog
     * @param templateElementGUID the template to use
     */
    public void addCatalogTemplateRelationship(String owningElementGUID,
                                               String templateElementGUID)
    {
        EntityDetail end1Entity = archiveBuilder.getEntity(owningElementGUID);
        EntityDetail end2Entity = archiveBuilder.getEntity(templateElementGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(end1Entity);
        EntityProxy end2 = archiveHelper.getEntityProxy(end2Entity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CATALOG_TEMPLATE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(owningElementGUID + "_to_" + templateElementGUID + "_catalog_template_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a endpoint entity.
     *
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the endpoint
     * @param displayName display name for the endpoint
     * @param description description about the endpoint
     * @param networkAddress location of the asset
     * @param protocol protocol to use to connect to the asset
     * @param additionalProperties any other properties.
     *
     * @return id for the endpoint
     */
    public String addEndpoint(String              anchorGUID,
                              String              anchorTypeName,
                              String              anchorDomainName,
                              String              anchorScopeGUID,
                              String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              networkAddress,
                              String              protocol,
                              Map<String, String> additionalProperties)
    {
        return addEndpoint(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, qualifiedName, displayName, description, networkAddress, protocol, additionalProperties, null);
    }

    /**
     * Create a endpoint entity.
     *
     * @param anchorGUID unique identifier of the anchor
     * @param anchorTypeName type of anchor
     * @param anchorDomainName domain of anchor
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param qualifiedName unique name for the endpoint
     * @param displayName display name for the endpoint
     * @param description description about the endpoint
     * @param networkAddress location of the asset
     * @param protocol protocol to use to connect to the asset
     * @param additionalProperties any other properties.
     *
     * @return id for the endpoint
     */
    public String addEndpoint(String              anchorGUID,
                              String              anchorTypeName,
                              String              anchorDomainName,
                              String              anchorScopeGUID,
                              String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              networkAddress,
                              String              protocol,
                              Map<String, String> additionalProperties,
                              Classification      additionalClassification)
    {
        final String methodName = "addEndpoint";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NETWORK_ADDRESS.name, networkAddress, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PROTOCOL.name, protocol, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        if (additionalClassification != null)
        {
            classifications.add(additionalClassification);
        }

        if ((anchorGUID != null) || (anchorTypeName != null))
        {
            classifications.add(this.getAnchorClassification(anchorGUID,
                                                             anchorTypeName,
                                                             anchorDomainName,
                                                             anchorScopeGUID,
                                                             methodName));
        }

        if (classifications.isEmpty())
        {
            classifications = null;
        }

        EntityDetail endpointEntity = archiveHelper.getEntityDetail(OpenMetadataType.ENDPOINT.typeName,
                                                                    idToGUIDMap.getGUID(qualifiedName),
                                                                    properties,
                                                                    InstanceStatus.ACTIVE,
                                                                    classifications);

        archiveBuilder.addEntity(endpointEntity);

        return endpointEntity.getGUID();
    }


    /**
     * Create a server endpoint relationship.
     *
     * @param itInfrastructureGUID the element to link
     * @param endpointGUID the endpoint to use
     */
    public void addServerEndpointRelationship(String itInfrastructureGUID,
                                              String endpointGUID)
    {
        EntityDetail end1Entity = archiveBuilder.getEntity(itInfrastructureGUID);
        EntityDetail end2Entity = archiveBuilder.getEntity(endpointGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(end1Entity);
        EntityProxy end2 = archiveHelper.getEntityProxy(end2Entity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SERVER_ENDPOINT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(itInfrastructureGUID + "_to_" + endpointGUID + "_server_endpoint_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }

    /**
     * Create a glossary entity.  If the external link is specified, the glossary entity is linked to an
     * ExternalGlossaryLink entity.  If the scope is specified, the glossary entity is classified as
     * a CanonicalGlossary.
     *
     * @param qualifiedName unique name for the glossary
     * @param displayName display name for the glossary
     * @param description description about the glossary
     * @param language language that the glossary is written in
     * @param usage how the glossary should be used
     * @param externalLink link to material
     * @param scope scope of the content.
     *
     * @return id for the glossary
     */
    public String addGlossary(String   qualifiedName,
                              String   displayName,
                              String   description,
                              String   language,
                              String   usage,
                              String   externalLink,
                              String   scope)
    {
        return addGlossary(qualifiedName, displayName, description, language, usage, externalLink, scope, null);
    }

    /**
     * Create a glossary entity.  If the external link is specified, the glossary entity is linked to an
     * ExternalGlossaryLink entity.  If the scope is specified, the glossary entity is classified as
     * a CanonicalGlossary.
     *
     * @param qualifiedName unique name for the glossary
     * @param displayName display name for the glossary
     * @param description description about the glossary
     * @param language language that the glossary is written in
     * @param usage how the glossary should be used
     * @param externalLink link to material
     * @param scope scope of the content.
     * @param additionalProperties any other properties.
     *
     * @return id for the glossary
     */
    public String addGlossary(String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              language,
                              String              usage,
                              String              externalLink,
                              String              scope,
                              Map<String, String> additionalProperties)
    {
        final String methodName = "addGlossary";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.LANGUAGE.name, language, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USAGE.name, usage, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        if (scope != null)
        {
            Classification  canonicalVocabClassification = archiveHelper.getClassification(OpenMetadataType.CANONICAL_VOCABULARY_CLASSIFICATION.typeName,
                                                                                           archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                                     null,
                                                                                                                                     OpenMetadataProperty.SCOPE.name,
                                                                                                                                     scope,
                                                                                                                                     methodName),
                                                                                           InstanceStatus.ACTIVE);

            classifications.add(canonicalVocabClassification);
        }

        String guid = idToGUIDMap.getGUID(qualifiedName);

        Classification anchorClassification = this.getAnchorClassification(guid,
                                                                           OpenMetadataType.GLOSSARY.typeName,
                                                                           OpenMetadataType.GLOSSARY.typeName,
                                                                           null,
                                                                           methodName);
        classifications.add(anchorClassification);

        EntityDetail  glossaryEntity = archiveHelper.getEntityDetail(OpenMetadataType.GLOSSARY.typeName,
                                                                     guid,
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(glossaryEntity);

        if (externalLink != null)
        {
            String externalLinkQualifiedName = qualifiedName + "_external_link";
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, externalLinkQualifiedName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.URL.name, externalLink, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ORGANIZATION.name, originatorName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionName, methodName);

            classifications = new ArrayList<>();
            classifications.add(anchorClassification);

            EntityDetail  externalLinkEntity = archiveHelper.getEntityDetail(OpenMetadataType.EXTERNAL_REFERENCE.typeName,
                                                                             idToGUIDMap.getGUID(externalLinkQualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

            archiveBuilder.addEntity(externalLinkEntity);

            EntityProxy end1 = archiveHelper.getEntityProxy(glossaryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(externalLinkEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.EXTERNAL_REFERENCE_LINK_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_external_reference_link_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return glossaryEntity.getGUID();
    }


    /**
     * Add a glossary category to the archive and connect it to its glossary.
     *
     * @param glossaryGUID identifier of the glossary.
     * @param qualifiedName unique name for the category.
     * @param displayName display name for the category.
     * @param description description of the category.
     * @param subjectArea name of the subject area if this category contains terms for the subject area.
     *
     * @return identifier of the category
     */
    public String addGlossaryCategory(String              glossaryGUID,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              subjectArea)
    {
        return addGlossaryCategory(glossaryGUID, false, null, qualifiedName, displayName, description, subjectArea, null);
    }


    /**
     * Add a glossary category to the archive and connect it to its glossary.
     *
     * @param glossaryGUID identifier of the glossary.
     * @param isRootCategory is this the top-level category for the glossary
     * @param parentCategoryGUID is there a parent category?
     * @param qualifiedName unique name for the category.
     * @param displayName display name for the category.
     * @param description description of the category.
     * @param subjectArea name of the subject area if this category contains terms for the subject area.
     * @param additionalProperties any other properties.
     *
     * @return identifier of the category
     */
    public String addGlossaryCategory(String              glossaryGUID,
                                      boolean             isRootCategory,
                                      String              parentCategoryGUID,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              subjectArea,
                                      Map<String, String> additionalProperties)
    {
        final String methodName = "addCategory";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        if (subjectArea != null)
        {
            Classification  subjectAreaClassification = archiveHelper.getClassification(OpenMetadataType.SUBJECT_AREA_CLASSIFICATION.typeName,
                                                                                        archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                                  null,
                                                                                                                                  OpenMetadataProperty.DISPLAY_NAME.name,
                                                                                                                                  subjectArea,
                                                                                                                                  methodName),
                                                                                        InstanceStatus.ACTIVE);

            classifications.add(subjectAreaClassification);
        }

        classifications.add(this.getAnchorClassification(null,
                                                         OpenMetadataType.COLLECTION.typeName,
                                                         OpenMetadataType.COLLECTION.typeName,
                                                         glossaryGUID,
                                                         methodName));

        classifications.add(archiveHelper.getClassification(OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
                                                            null, InstanceStatus.ACTIVE));

        EntityDetail  categoryEntity = archiveHelper.getEntityDetail(OpenMetadataType.COLLECTION.typeName,
                                                                     idToGUIDMap.getGUID(qualifiedName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(categoryEntity);

        if (isRootCategory)
        {
            EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryGUID));
            EntityProxy end2 = archiveHelper.getEntityProxy(categoryEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_category_hierarchy_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (parentCategoryGUID != null)
        {
            EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(parentCategoryGUID));
            EntityProxy end2 = archiveHelper.getEntityProxy(categoryEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_category_collection_membership_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return categoryEntity.getGUID();
    }


    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.
     *
     * @param glossaryGUID unique identifier of the glossary
     * @param categoryGUIDs unique identifiers of the categories
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param description description of the term
     *
     * @return unique identifier of the term
     */
    public String addTerm(String       glossaryGUID,
                          List<String> categoryGUIDs,
                          String       qualifiedName,
                          String       displayName,
                          String       description)
    {
        return addTerm(glossaryGUID, categoryGUIDs, false, qualifiedName, displayName, null, description, null, null,null, false, null, null, null, null);
    }


    /**
     * Add a term and link it to the glossary and an arbitrary number of categories.  Add requested classifications
     *
     * @param glossaryGUID unique identifier of the glossary
     * @param categoryIds unique identifiers of the categories
     * @param categoriesAsNames when true the categories are specified as qualified names, otherwise they are guids.
     * @param qualifiedName unique name of the term
     * @param displayName display name of the term
     * @param summary short description of the term
     * @param description description of the term
     * @param examples examples of the term
     * @param abbreviation abbreviation
     * @param usage how is the term used
     * @param isContext is this term a context definition?
     * @param contextDescription description to add to the ContextDefinition classification
     * @param contextScope scope to add to the context classification
     * @param additionalProperties any other properties.
     * @param additionalClassifications classifications provided by the caller
     *
     * @return unique identifier of the term
     */
    public String addTerm(String               glossaryGUID,
                          List<String>         categoryIds,
                          boolean              categoriesAsNames,
                          String               qualifiedName,
                          String               displayName,
                          String               summary,
                          String               description,
                          String               examples,
                          String               abbreviation,
                          String               usage,
                          boolean              isContext,
                          String               contextDescription,
                          String               contextScope,
                          Map<String, String>  additionalProperties,
                          List<Classification> additionalClassifications)
    {
        final String methodName = "addTerm";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SUMMARY.name, summary, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.EXAMPLES.name, examples, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ABBREVIATION.name, abbreviation, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USAGE.name, usage, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        List<Classification> classifications = additionalClassifications;

        if (classifications == null)
        {
            classifications = new ArrayList<>();
        }

        if (isContext)
        {
            InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DESCRIPTION.name, contextDescription, methodName);
            classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataProperty.SCOPE.name, contextScope, methodName);

            Classification  newClassification = archiveHelper.getClassification(OpenMetadataType.CONTEXT_DEFINITION_CLASSIFICATION.typeName,
                                                                                classificationProperties,
                                                                                InstanceStatus.ACTIVE);

            classifications.add(newClassification);
        }

        classifications.add(this.getAnchorClassification(null,
                                                         OpenMetadataType.GLOSSARY_TERM.typeName,
                                                         OpenMetadataType.GLOSSARY_TERM.typeName,
                                                         glossaryGUID,
                                                         methodName));

        EntityDetail  termEntity = archiveHelper.getEntityDetail(OpenMetadataType.GLOSSARY_TERM.typeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(termEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(termEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_term_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

        if (categoryIds != null)
        {
            InstanceProperties categorizationProperties = archiveHelper.addEnumPropertyToInstance(archiveRootName,
                                                                                                  null,
                                                                                                  OpenMetadataProperty.MEMBERSHIP_STATUS.name,
                                                                                                  CollectionMemberStatus.getOpenTypeGUID(),
                                                                                                  CollectionMemberStatus.getOpenTypeName(),
                                                                                                  CollectionMemberStatus.VALIDATED.getOrdinal(),
                                                                                                  CollectionMemberStatus.VALIDATED.getName(),
                                                                                                  CollectionMemberStatus.VALIDATED.getDescription(),
                                                                                                  methodName);

            for (String  categoryId : categoryIds)
            {
                if (categoryId != null)
                {
                    String categoryGUID = categoryId;

                    if (categoriesAsNames)
                    {
                        categoryGUID = idToGUIDMap.getGUID(categoryId);
                    }

                    end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryGUID));

                    /*
                     * Note properties set to ACTIVE - if you need different properties use addTermToCategory
                     */
                    archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                                 idToGUIDMap.getGUID(qualifiedName + "_category_" + categoryId + "_term_collection_membership_relationship"),
                                                                                 categorizationProperties,
                                                                                 InstanceStatus.ACTIVE,
                                                                                 end1,
                                                                                 end2));
                }
            }
        }

        return termEntity.getGUID();
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param parentCategoryGUID unique identifier for the parent category
     * @param childCategoryGUID unique identifier for the child category
     */
    public void addCategoryToCategory(String  parentCategoryGUID,
                                      String  childCategoryGUID)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(parentCategoryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(childCategoryGUID));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(parentCategoryGUID + "_to_" + childCategoryGUID + "_collection_membership_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a data element to a glossary term.
     *
     * @param dataGUID unique identifier for the element linked to the semantic assignment
     * @param glossaryTermGUID unique identifier for the term
     */
    public void addSemanticAssignment(String  dataGUID,
                                      String  glossaryTermGUID)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(dataGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryTermGUID));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(dataGUID + "_to_" + glossaryTermGUID + "_semantic_assignment_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Link a term to a context referenceable to show that the term is valid in the context of the other.
     *
     * @param contextGUID unique identifier for the context
     * @param termGUID unique identifier for the term
     * @param status ordinal for the relationship status
     * @param description description of the relationship between the term and the category.
     */
    public void addTermToContext(String  contextGUID,
                                 String  termGUID,
                                 int     status,
                                 String  description)
    {
        final String methodName = "addTermToContext";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(contextGUID));

        EnumElementDef termStatus = archiveHelper.getEnumElement(GlossaryTermRelationshipStatus.getOpenTypeName(), status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name, GlossaryTermRelationshipStatus.getOpenTypeGUID(), GlossaryTermRelationshipStatus.getOpenTypeName(), termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.USED_IN_CONTEXT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(contextGUID + "_to_" + termGUID + "_used_in_context_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two terms together to show that one is valid in the context of the other.
     *
     * @param synonymGUID unique identifier for the context
     * @param termGUID unique identifier for the term
     * @param status ordinal for the relationship status
     * @param description description of the relationship between the term and the category.
     * @param expression the expression that indicates how close a synonym this is
     * @param steward the identifier of the steward that created this relationship
     * @param source source of the information that indicates this is a synonym
     */
    public void addTermToSynonym(String  synonymGUID,
                                 String  termGUID,
                                 int     status,
                                 String  description,
                                 String  expression,
                                 String  steward,
                                 String  source)
    {
        final String methodName = "addTermToSynonym";

        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(synonymGUID));
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));

        EnumElementDef termStatus = archiveHelper.getEnumElement(GlossaryTermRelationshipStatus.getOpenTypeName(), status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name, GlossaryTermRelationshipStatus.getOpenTypeGUID(), GlossaryTermRelationshipStatus.getOpenTypeName(), termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.EXPRESSION.name, expression, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.STEWARD.name, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SOURCE.name, source, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SYNONYM_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(synonymGUID + "_to_" + termGUID + "_synonym_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param categoryGUID unique identifier for the parent category
     * @param termGUID unique identifier for the  term
     */
    public void addTermToCategory(String  categoryGUID,
                                  String  termGUID)
    {
        addTermToCategory(categoryGUID, termGUID, 1, null);
    }


    /**
     * Link two categories together as part of the parent child hierarchy.
     *
     * @param categoryGUID unique identifier for the parent category
     * @param termGUID unique identifier for the term
     * @param status ordinal for the relationship status
     * @param description description of the relationship between the term and the category.
     */
    public void addTermToCategory(String  categoryGUID,
                                  String  termGUID,
                                  int     status,
                                  String  description)
    {
        final String methodName = "addTermToCategory";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));

        EnumElementDef termStatus = archiveHelper.getEnumElement(CollectionMemberStatus.getOpenTypeName(), status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataProperty.MEMBERSHIP_STATUS.name, CollectionMemberStatus.getOpenTypeGUID(), CollectionMemberStatus.getOpenTypeName(), termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.MEMBERSHIP_RATIONALE.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(categoryGUID + "_to_" + termGUID + "_term_categorization_collection_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a navigation link from one referenceable to another to show they provide more information.
     *
     * @param describedElementId unique identifier for the element that is referencing the other.
     * @param describerElementId unique identifier for the element being pointed to.
     */
    public void addMoreInformationLink(String  describedElementId,
                                       String  describerElementId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(describedElementId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(describerElementId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.MORE_INFORMATION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(describedElementId + "_to_" + describerElementId + "_more_information_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a licence relationship.
     *
     * @param licensedElementGUID unique identifier for the element that is licensed.
     * @param licenseGUID - unique identifier of the license
     * @param startDate start date for the license
     * @param endDate end date for the license
     * @param conditions conditions
     * @param licensedBy identity of source
     * @param licensedByTypeName type of identity of source
     * @param licensedByPropertyName property name use to identify source
     * @param custodian identity of person responsible for overseeing use
     * @param custodianTypeName type name of custodian
     * @param custodianPropertyName property name used to identify custodian
     * @param licensee identity of person receiving
     * @param licenseeTypeName type of person element
     * @param licenseePropertyName property name use to identify licensee
     * @param entitlements entitlements
     * @param restrictions restrictions
     * @param obligations obligations
     * @param notes notes
     * @param licenseTypeGUID unique identifier for the license type.
     */
    public void addLicense(String              licensedElementGUID,
                           String              licenseGUID,
                           Date                startDate,
                           Date                endDate,
                           String              conditions,
                           String              licensedBy,
                           String              licensedByTypeName,
                           String              licensedByPropertyName,
                           String              custodian,
                           String              custodianTypeName,
                           String              custodianPropertyName,
                           String              licensee,
                           String              licenseeTypeName,
                           String              licenseePropertyName,
                           Map<String, String> entitlements,
                           Map<String, String> restrictions,
                           Map<String, String> obligations,
                           String              notes,
                           String              licenseTypeGUID)
    {
        final String methodName = "addLicense";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(licensedElementGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(licenseTypeGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                  null,
                                                                                  OpenMetadataProperty.LICENSE_GUID.name,
                                                                                  licenseGUID,
                                                                                  methodName);

        properties = archiveHelper.addDatePropertyToInstance(archiveRootName,
                                                             properties,
                                                             OpenMetadataProperty.COVERAGE_START.name,
                                                             startDate,
                                                             methodName);

        properties = archiveHelper.addDatePropertyToInstance(archiveRootName,
                                                             properties,
                                                             OpenMetadataProperty.COVERAGE_END.name,
                                                             endDate,
                                                             methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CONDITIONS.name,
                                                               conditions,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.LICENSED_BY.name,
                                                               licensedBy,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.LICENSED_BY_TYPE_NAME.name,
                                                               licensedByTypeName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.LICENSED_BY_PROPERTY_NAME.name,
                                                               licensedByPropertyName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CUSTODIAN.name,
                                                               custodian,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                               custodianTypeName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                               custodianPropertyName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.LICENSEE.name,
                                                               licensee,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.LICENSEE_TYPE_NAME.name,
                                                               licenseeTypeName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.LICENSEE_PROPERTY_NAME.name,
                                                               licenseePropertyName,
                                                               methodName);

        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName,
                                                                  properties,
                                                                  OpenMetadataProperty.ENTITLEMENTS.name,
                                                                  entitlements,
                                                                  methodName);

        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName,
                                                                  properties,
                                                                  OpenMetadataProperty.RESTRICTIONS.name,
                                                                  restrictions,
                                                                  methodName);

        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName,
                                                                  properties,
                                                                  OpenMetadataProperty.OBLIGATIONS.name,
                                                                  obligations,
                                                                  methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.NOTES.name,
                                                               notes,
                                                               methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.LICENSE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(licensedElementGUID + "_to_" + licenseTypeGUID + "_license_relationship_" + licenseGUID),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a licence relationship.
     *
     * @param certifiedElementGUID unique identifier for the element that is certified.
     * @param certificationGUID - unique identifier of the certification
     * @param startDate start date for the certification
     * @param endDate end date for the certification
     * @param conditions conditions
     * @param certifiedBy identity of source
     * @param certifiedByTypeName type of identity of source
     * @param certifiedByPropertyName property name use to identify source
     * @param custodian identity of person responsible for overseeing use
     * @param custodianTypeName type name of custodian
     * @param custodianPropertyName property name used to identify custodian
     * @param recipient identity of person receiving
     * @param recipientTypeName type of person element
     * @param recipientPropertyName property name use to identify recipient
     * @param notes notes
     * @param certificationTypeGUID unique identifier for the license type.
     */
    public void addCertification(String              certifiedElementGUID,
                                 String              certificationGUID,
                                 Date                startDate,
                                 Date                endDate,
                                 String              conditions,
                                 String              certifiedBy,
                                 String              certifiedByTypeName,
                                 String              certifiedByPropertyName,
                                 String              custodian,
                                 String              custodianTypeName,
                                 String              custodianPropertyName,
                                 String              recipient,
                                 String              recipientTypeName,
                                 String              recipientPropertyName,
                                 String              notes,
                                 String              certificationTypeGUID)
    {
        final String methodName = "addCertification";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(certifiedElementGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(certificationTypeGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                  null,
                                                                                  OpenMetadataProperty.CERTIFICATE_GUID.name,
                                                                                  certificationGUID,
                                                                                  methodName);

        properties = archiveHelper.addDatePropertyToInstance(archiveRootName,
                                                             properties,
                                                             OpenMetadataProperty.COVERAGE_START.name,
                                                             startDate,
                                                             methodName);

        properties = archiveHelper.addDatePropertyToInstance(archiveRootName,
                                                             properties,
                                                             OpenMetadataProperty.COVERAGE_END.name,
                                                             endDate,
                                                             methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CONDITIONS.name,
                                                               conditions,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CERTIFIED_BY.name,
                                                               certifiedBy,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CERTIFIED_BY_TYPE_NAME.name,
                                                               certifiedByTypeName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CERTIFIED_BY_PROPERTY_NAME.name,
                                                               certifiedByPropertyName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CUSTODIAN.name,
                                                               custodian,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CUSTODIAN_TYPE_NAME.name,
                                                               custodianTypeName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.CUSTODIAN_PROPERTY_NAME.name,
                                                               custodianPropertyName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.RECIPIENT.name,
                                                               recipient,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.RECIPIENT_TYPE_NAME.name,
                                                               recipientTypeName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.RECIPIENT_PROPERTY_NAME.name,
                                                               recipientPropertyName,
                                                               methodName);

        properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                               properties,
                                                               OpenMetadataProperty.NOTES.name,
                                                               notes,
                                                               methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CERTIFICATION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(certifiedElementGUID + "_to_" + certificationTypeGUID + "_certification_relationship_" + certificationGUID),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create an approved purpose between a Referenceable and a data processing purpose.
     *
     * @param referenceableGUID identifier of term
     * @param dataProcessingPurposeGUID identifier of the purpose
     */
    public void linkApprovedPurpose(String  referenceableGUID,
                                    String  dataProcessingPurposeGUID)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(dataProcessingPurposeGUID));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.APPROVED_PURPOSE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(dataProcessingPurposeGUID + "_to_" + referenceableGUID + "_approved_purpose_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a semantic assignment between a term and a Referenceable - for example a model element.
     *
     * @param termId identifier of term
     * @param referenceableId identifier of referenceable
     */
    public void linkTermToReferenceable(String  termId,
                                        String  referenceableId)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SEMANTIC_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(referenceableId + "_to_" + termId + "_semantic_assignment_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add related term relationship.
     *
     * @param conceptQName concept qualified name
     * @param propertyQName property qualified name
     */
    public void addRelatedTermRelationship(String conceptQName, String propertyQName)
    {
        String conceptId = idToGUIDMap.getGUID(conceptQName);
        String propertyId = idToGUIDMap.getGUID(propertyQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(conceptId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(propertyId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.RELATED_TERM_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(conceptId + "_to_" + propertyId + "_related_term_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a valid value definition/set.
     *
     * @param typeName unique name of the valid value - ie a definition or a set
     * @param qualifiedName unique name of the valid value
     * @param name display name of the valid value
     * @param scope short description of the valid value
     * @param description description of the valid value
     * @param preferredValue preferredValue of the valid value
     * @param usage how is the valid value used
     * @param userDefinedStatus is value active
     * @param additionalProperties any other properties.
     *
     * @return unique identifier of the valid value
     */
    public String addValidValue(String              typeName,
                                String              qualifiedName,
                                String              name,
                                String              description,
                                String              usage,
                                String              scope,
                                String              preferredValue,
                                String              userDefinedStatus,
                                Map<String, String> additionalProperties)
    {
        return this.addValidValue(null, null, null, null,null, null, typeName, qualifiedName, null, name, description, null, usage, null, scope, preferredValue, userDefinedStatus, false, additionalProperties);
    }


    /**
     * Add a valid value definition/set.
     *
     * @param suppliedValidValueGUID optional unique identifier for the valid value instance
     * @param setGUID unique identifier of parent set
     * @param anchorGUID unique identifier of the anchor (or null)
     * @param anchorTypeName unique displayName of type of anchor (or null)
     * @param anchorDomainName unique displayName of type of anchor's domain (or null)
     * @param anchorScopeGUID unique identifier of the anchor's scope
     * @param typeName unique displayName of the valid value - ie a definition or a set
     * @param qualifiedName unique displayName of the valid value
     * @param displayName display displayName of the valid value
     * @param namespace namespace of the valid value
     * @param scope short description of the valid value
     * @param dataType type for preferred value
     * @param description description of the valid value
     * @param preferredValue preferredValue of the valid value
     * @param usage how is the valid value used
     * @param userDefinedStatus is value active
     * @param isCaseSensitive is value case sensitive
     * @param additionalProperties any other properties.
     *
     * @return unique identifier of the valid value
     */
    public String addValidValue(String              suppliedValidValueGUID,
                                String              setGUID,
                                String              anchorGUID,
                                String              anchorTypeName,
                                String              anchorDomainName,
                                String              anchorScopeGUID,
                                String              typeName,
                                String              qualifiedName,
                                String              category,
                                String              displayName,
                                String              description,
                                String              namespace,
                                String              usage,
                                String              dataType,
                                String              scope,
                                String              preferredValue,
                                String              userDefinedStatus,
                                boolean             isCaseSensitive,
                                Map<String, String> additionalProperties)
    {
        final String methodName = "addValidValue";

        String validValueGUID = suppliedValidValueGUID;

        if (validValueGUID == null)
        {
            validValueGUID = idToGUIDMap.getGUID(qualifiedName);
        }
        else
        {
            idToGUIDMap.setGUID(qualifiedName, validValueGUID);
        }

        if (archiveBuilder.queryEntity(validValueGUID) == null)
        {
            InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NAMESPACE.name, namespace, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USAGE.name, usage, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DATA_TYPE.name, dataType, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SCOPE.name, scope, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PREFERRED_VALUE.name, preferredValue, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.USER_DEFINED_STATUS.name, userDefinedStatus, methodName);
            properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IS_CASE_SENSITIVE.name, isCaseSensitive, methodName);
            properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

            List<Classification> entityClassifications = new ArrayList<>();

            if (anchorGUID != null)
            {
                entityClassifications.add(getAnchorClassification(anchorGUID, anchorTypeName, anchorDomainName, anchorScopeGUID, methodName));
            }
            else
            {
                entityClassifications.add(getAnchorClassification(validValueGUID, typeName, OpenMetadataType.VALID_VALUE_DEFINITION.typeName, null, methodName));
            }

            EntityDetail validValueEntity = archiveHelper.getEntityDetail(typeName,
                                                                          validValueGUID,
                                                                          properties,
                                                                          InstanceStatus.ACTIVE,
                                                                          entityClassifications);

            archiveBuilder.addEntity(validValueEntity);

            if (setGUID != null)
            {
                this.addValidValueMembershipRelationshipByGUID(setGUID, validValueEntity.getGUID(), false);
            }

            return validValueEntity.getGUID();
        }
        else
        {
            return validValueGUID;
        }
    }


    /**
     * Set up an anchor classification.
     *
     * @param anchorGUID unique identifier for the anchor - can be null
     * @param anchorTypeName unique name of the anchor - set if anchorGUID set
     * @param anchorDomainName the typename of the direct subtype of referenceable/open metadata root
     * @param anchorScopeGUID the unique identifier of the anchor's scope (if applicable)
     * @param methodName calling method
     * @return classification
     */
    Classification getAnchorClassification(String anchorGUID,
                                           String anchorTypeName,
                                           String anchorDomainName,
                                           String anchorScopeGUID,
                                           String methodName)
    {
        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null,
                                                                                                OpenMetadataProperty.ANCHOR_GUID.name,
                                                                                                anchorGUID, methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties,
                                                                             OpenMetadataProperty.ANCHOR_TYPE_NAME.name,
                                                                             anchorTypeName,
                                                                             methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties,
                                                                             OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name,
                                                                             anchorDomainName,
                                                                             methodName);

        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties,
                                                                             OpenMetadataProperty.ANCHOR_SCOPE_GUID.name,
                                                                             anchorScopeGUID,
                                                                             methodName);

        return archiveHelper.getClassification(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                               classificationProperties, InstanceStatus.ACTIVE);
    }


    /**
     * Link a valid value as a member of a valid value set.
     *
     * @param setQName qualified name of the set to add to
     * @param memberQName qualified name of the member to add
     * @param isDefaultValue is this the default value (only set to true for one member).
     */
    public void addValidValueMembershipRelationship(String  setQName,
                                                    String  memberQName,
                                                    boolean isDefaultValue)
    {
        String setId = idToGUIDMap.getGUID(setQName);
        String memberId = idToGUIDMap.getGUID(memberQName);

        this.addValidValueMembershipRelationshipByGUID(setId, memberId, isDefaultValue);
    }


    /**
     * Link a valid value as a member of a valid value set.
     *
     * @param setId unique identifier of the set to add to
     * @param memberId unique identifier of the member to add
     * @param isDefaultValue is this the default value (only set to true for one member).
     */
    public void addValidValueMembershipRelationshipByGUID(String  setId,
                                                          String  memberId,
                                                          boolean isDefaultValue)
    {
        final String methodName = "addValidValuesAssignmentRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(setId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(memberId));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, OpenMetadataProperty.IS_DEFAULT_VALUE.name, isDefaultValue, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.VALID_VALUE_MEMBER_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(setId + "_to_" + memberId + "_valid_value_member_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a valid value as a member of a valid value set.
     *
     * @param validValue1QName qualified name of first valid value
     * @param validValue2QName qualified name of second valid value
     */
    public void addConsistentValidValueRelationship(String  validValue1QName,
                                                    String  validValue2QName)
    {
        final String methodName = "addConsistentValidValueRelationship";

        String setId = idToGUIDMap.getGUID(validValue1QName);
        String memberId = idToGUIDMap.getGUID(validValue2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(setId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(memberId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.CONSISTENT_VALID_VALUES_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(setId + "_to_" + memberId + "_consistent_valid_value_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link an element that represents a data field (either directly or indirectly) to a valid value (typically a valid value set).
     *
     * @param dataFieldQName qualified name of element that represents the data field
     * @param validValueQName qualified name of the valid value set/definition
     * @param strictRequirement do the valid values mandate the values stored in the data field?
     */
    public void addValidValuesAssignmentRelationship(String  dataFieldQName,
                                                     String  validValueQName,
                                                     boolean strictRequirement)
    {
        final String methodName = "addValidValuesAssignmentRelationship";

        String dataFieldId = idToGUIDMap.getGUID(dataFieldQName);
        String validValueId = idToGUIDMap.getGUID(validValueQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(dataFieldId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, OpenMetadataProperty.STRICT_REQUIREMENT.name, strictRequirement, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.VALID_VALUES_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(dataFieldId + "_to_" + validValueId + "_valid_values_assignment_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a mapping between two valid values.  Typically they are in different valid value sets.
     *
     * @param matchingValue1QName qualified name of one of the valid values.
     * @param matchingValue2QName qualified name of the other valid value.
     * @param associationDescription a description of the meaning of the association
     * @param confidence how likely is the relationship correct - 0=unlikely; 100=certainty
     * @param steward who was the steward that made the link
     * @param stewardTypeName what is the type of the element used to represent the steward?
     * @param stewardPropertyName what is the name of the property used to represent the steward?
     * @param notes any notes on the relationship.
     */
    public void addValidValuesMappingRelationship(String matchingValue1QName,
                                                  String matchingValue2QName,
                                                  String associationDescription,
                                                  int    confidence,
                                                  String steward,
                                                  String stewardTypeName,
                                                  String stewardPropertyName,
                                                  String notes)
    {
        final String methodName = "addValidValuesMappingRelationship";

        String matchingValue1Id = idToGUIDMap.getGUID(matchingValue1QName);
        String matchingValueId = idToGUIDMap.getGUID(matchingValue2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(matchingValue1Id));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(matchingValueId));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSOCIATION_DESCRIPTION.name, associationDescription, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.CONFIDENCE.name, confidence, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.STEWARD.name, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.STEWARD_TYPE_NAME.name, stewardTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.STEWARD_PROPERTY_NAME.name, stewardPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NOTES.name, notes, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.VALID_VALUES_MAPPING_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(matchingValue1Id + "_to_" + matchingValueId + "_valid_values_mapping_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a relationship between two valid values.
     *
     * @param end1QName qualified name of one of the valid values.
     * @param end2QName qualified name of the other valid value.
     * @param associationName a description of the meaning of the association
     * @param associationType a formal name of the meaning of the association
     * @param additionalProperties other information about the meaning of the association
     */
    public void addValidValueAssociationRelationship(String              end1QName,
                                                     String              end2QName,
                                                     String              associationName,
                                                     String              associationType,
                                                     Map<String, String> additionalProperties)
    {
        final String methodName = "addValidValueAssociationRelationship";

        String end1GUID = idToGUIDMap.getGUID(end1QName);
        String end2GUID = idToGUIDMap.getGUID(end2QName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end1GUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end2GUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ASSOCIATION_NAME.name, associationName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ASSOCIATION_TYPE.name, associationType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.VALID_VALUE_ASSOCIATION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(end1GUID + "_to_" + end2GUID + "_valid_value_association_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a referenceable element to a valid value that is acting as a tag.
     *
     * @param referenceableQName qualified name of referenceable
     * @param validValueQName qualified name of valid value
     * @param confidence how likely is the relationship correct - 0=unlikely; 100=certainty
     * @param steward who was the steward that made the link
     * @param stewardTypeName what is the type of the element used to represent the steward?
     * @param stewardPropertyName what is the name of the property used to represent the steward?
     * @param notes any notes on the relationship.
     */
    public void addReferenceValueAssignmentRelationship(String referenceableQName,
                                                        String validValueQName,
                                                        int    confidence,
                                                        String steward,
                                                        String stewardTypeName,
                                                        String stewardPropertyName,
                                                        String notes)
    {
        String referenceableId = idToGUIDMap.getGUID(referenceableQName);
        String validValueId = idToGUIDMap.getGUID(validValueQName);

        addReferenceValueAssignmentRelationship(referenceableId,
                                                validValueId,
                                                null,
                                                confidence,
                                                steward,
                                                stewardTypeName,
                                                stewardPropertyName,
                                                notes);
    }


    /**
     * Link a referenceable element to a valid value that is acting as a tag.
     *
     * @param referenceableGUID unique identifier of referenceable
     * @param validValueGUID unique identifier of valid value
     * @param attributeName name of associated attribute
     * @param confidence how likely is the relationship correct - 0=unlikely; 100=certainty
     * @param steward who was the steward that made the link
     * @param stewardTypeName what is the type of the element used to represent the steward?
     * @param stewardPropertyName what is the name of the property used to represent the steward?
     * @param notes any notes on the relationship.
     */
    public void addReferenceValueAssignmentRelationship(String referenceableGUID,
                                                        String validValueGUID,
                                                        String attributeName,
                                                        int    confidence,
                                                        String steward,
                                                        String stewardTypeName,
                                                        String stewardPropertyName,
                                                        String notes)
    {
        final String methodName = "addReferenceValueAssignmentRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueGUID));

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataProperty.CONFIDENCE.name, confidence, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ATTRIBUTE_NAME.name, attributeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.STEWARD.name, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.STEWARD_TYPE_NAME.name, stewardTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.STEWARD_PROPERTY_NAME.name, stewardPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.NOTES.name, notes, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + validValueGUID + "_reference_value_assignment_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a referenceable element to a valid value that is acting as a tag.
     *
     * @param referenceableGUID unique identifier of referenceable
     * @param validValueGUID unique identifier of valid value
     * @param propertyType name of associated attribute
     */
    public void addSpecificationPropertyAssignmentRelationship(String referenceableGUID,
                                                               String validValueGUID,
                                                               String propertyType)
    {
        final String methodName = "addSpecificationPropertyAssignmentRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.PROPERTY_NAME.name, propertyType, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(referenceableGUID + "_to_" + validValueGUID + "_specification_property_type_relationship_for_" + propertyType),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a valid value definition to the asset where it is implemented.
     *
     * @param validValueQName qualified name of the valid value
     * @param assetQName qualified name of the asset
     * @param symbolicName property name used in the asset to represent the valid value
     * @param implementationValue value used in the asset to represent the valid value
     * @param additionalValues additional mapping values
     */
    public void addValidValuesImplementationRelationship(String              validValueQName,
                                                         String              assetQName,
                                                         String              symbolicName,
                                                         String              implementationValue,
                                                         Map<String, String> additionalValues)
    {
        final String methodName = "addValidValuesImplementationRelationship";

        String validValueId = idToGUIDMap.getGUID(validValueQName);
        String assetId = idToGUIDMap.getGUID(assetQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(assetId));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.SYMBOLIC_NAME.name, symbolicName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.IMPLEMENTATION_VALUE.name, implementationValue, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_VALUES.name, additionalValues, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.VALID_VALUES_IMPL_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(validValueId + "_to_" + assetId + "_valid_values_implementation_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add the ReferenceData classification to the requested element.
     *
     * @param assetGUID unique identifier of the element to classify
     */
    public void addReferenceDataClassification(String assetGUID)
    {
        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

        EntityProxy referenceableEntityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = archiveHelper.getClassification(OpenMetadataType.REFERENCE_DATA_CLASSIFICATION.typeName,
                                                                         null,
                                                                         InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(referenceableEntityProxy, classification));
    }

    /**
     * Add a SecurityTags classification to the requested element.
     *
     * @param assetGUID unique identifier for the element to classify
     * @param securityLabels list of security labels
     * @param securityProperties map of security properties
     * @param accessGroups access group assignments
     */
    public void addSecurityTagsClassification(String                    assetGUID,
                                              List<String>              securityLabels,
                                              Map<String, Object>       securityProperties,
                                              Map<String, List<String>> accessGroups)
    {
        final String methodName = "addSecurityTagsClassification";

        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

        EntityProxy entityProxy = archiveHelper.getEntityProxy(assetEntity);

        InstanceProperties properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, null, OpenMetadataProperty.SECURITY_LABELS.name, securityLabels, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SECURITY_PROPERTIES.name, securityProperties, methodName);
        properties = archiveHelper.addStringArrayStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ACCESS_GROUPS.name, accessGroups, methodName);

        Classification classification = archiveHelper.getClassification(OpenMetadataType.SECURITY_TAGS_CLASSIFICATION.typeName,
                                                                        properties,
                                                                        InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }


    /**
     * Add the DeployedOn relationship to the archive.
     *
     * @param deployedElementQName qualified name of element being deployed
     * @param deployedOnQName qualified name of target
     * @param deploymentTime time of the deployment
     * @param deployerTypeName type name of the element representing the deployer
     * @param deployerPropertyName property name used to identify the deployer
     * @param deployer identifier of the deployer
     * @param serverCapabilityStatus status of the deployment
     */
    public void addSupportedSoftwareCapabilityRelationship(String deployedElementQName,
                                                           String deployedOnQName,
                                                           Date   deploymentTime,
                                                           String deployerTypeName,
                                                           String deployerPropertyName,
                                                           String deployer,
                                                           int    serverCapabilityStatus)
    {
        final String methodName = "addSupportedSoftwareCapabilityRelationship";

        String deployedElementId = this.idToGUIDMap.getGUID(deployedElementQName);
        String deployedOnId = this.idToGUIDMap.getGUID(deployedOnQName);

        EntityProxy end1 = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedOnId));
        EntityProxy end2 = this.archiveHelper.getEntityProxy(this.archiveBuilder.getEntity(deployedElementId));

        EnumElementDef statusEnumElement = archiveHelper.getEnumElement(OperationalStatus.getOpenTypeName(), serverCapabilityStatus);

        InstanceProperties properties = archiveHelper.addDatePropertyToInstance(archiveRootName, null, OpenMetadataProperty.DEPLOYMENT_TIME.name, deploymentTime, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEPLOYER_TYPE_NAME.name, deployerTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEPLOYER_PROPERTY_NAME.name, deployerPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DEPLOYER.name, deployer, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.OPERATIONAL_STATUS.name, OperationalStatus.getOpenTypeGUID(), OperationalStatus.getOpenTypeName(), statusEnumElement.getOrdinal(), statusEnumElement.getValue(), statusEnumElement.getDescription(), methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName,
                                                                               this.idToGUIDMap.getGUID(deployedOnId + "_to_" + deployedElementId + "_supported_software_capability_relationship"),
                                                                               properties,
                                                                               InstanceStatus.ACTIVE,
                                                                               end1,
                                                                               end2));
    }



    /**
     * Create a ServerPurpose classification.
     *
     * @param classificationName name of the classification
     * @param deployedImplementationType deployed implementation type property
     * @return classification
     */
    public Classification getServerPurposeClassification(String classificationName,
                                                         String deployedImplementationType)
    {
        final String methodName = "getServerPurposeClassification";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                  null,
                                                                                  OpenMetadataProperty.DEPLOYED_IMPLEMENTATION_TYPE.name,
                                                                                  deployedImplementationType,
                                                                                  methodName);

        return this.archiveHelper.getClassification(classificationName, properties, InstanceStatus.ACTIVE);
    }



    /**
     * Create a classification for an Engine software server capability.
     *
     * @param classificationName name of the classification
     * @return classification
     */
    public Classification getEngineClassification(String classificationName)
    {
        return this.archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);
    }


    /**
     * Add a ServerPurpose classification to an IT Infrastructure element.
     *
     * @param elementGUID element to attach the classification
     * @param classificationName name of the classification
     * @param deployedImplementationType deployed implementation type property
     */
    public void addServerPurposeClassification(String elementGUID,
                                               String classificationName,
                                               String deployedImplementationType)
    {
        EntityDetail   assetEntity    = this.archiveBuilder.getEntity(elementGUID);
        EntityProxy    entityProxy    = this.archiveHelper.getEntityProxy(assetEntity);

        Classification classification = this.getServerPurposeClassification(classificationName, deployedImplementationType);

        this.archiveBuilder.addClassification(this.archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }


    /**
     * Add a new information supply chain.
     *
     * @param parentInformationSupplyChainGUID owning information supply chain
     * @param suppliedTypeName type name
     * @param isParentAnchor is the parent guid the anchor?
     * @param anchorScopeGUID the anchor scope
     * @param qualifiedName qualified name
     * @param name display name
     * @param description description
     * @param scope scope of responsibilities
     * @param purposes why is it needed
     * @param owner owner for the Ownership classification
     * @param ownerTypeName owner type for the Ownership classification
     * @param ownerPropertyName owner property name for the Ownership classification
     * @param integrationStyle how is it implemented
     * @param estimatedVolumetrics estimated volumetrics
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addInformationSupplyChain(String              parentInformationSupplyChainGUID,
                                             boolean             isParentAnchor,
                                             String              anchorScopeGUID,
                                             String              suppliedTypeName,
                                             String              qualifiedName,
                                             String              name,
                                             String              description,
                                             String              scope,
                                             List<String>        purposes,
                                             String              integrationStyle,
                                             String              owner,
                                             String              ownerTypeName,
                                             String              ownerPropertyName,
                                             Map<String, String> estimatedVolumetrics,
                                             Map<String, String> additionalProperties,
                                             Map<String, Object> extendedProperties)
    {
        final String methodName = "addInformationSupplyChain";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SCOPE.name, scope, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PURPOSES.name, purposes, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.INTEGRATION_STYLE.name, integrationStyle, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ESTIMATED_VOLUMETRICS.name, estimatedVolumetrics, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        if ((parentInformationSupplyChainGUID != null) && (isParentAnchor))
        {
            classifications.add(this.getAnchorClassification(parentInformationSupplyChainGUID, typeName, OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, anchorScopeGUID, methodName));
        }
        else
        {
            classifications.add(this.getAnchorClassification(null, typeName, OpenMetadataType.INFORMATION_SUPPLY_CHAIN.typeName, anchorScopeGUID, methodName));
        }

        if (owner != null)
        {
            classifications.add(this.getOwnershipClassification(owner, ownerTypeName, ownerPropertyName));
        }

        EntityDetail entity = archiveHelper.getEntityDetail(typeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            classifications);

        archiveBuilder.addEntity(entity);

        if (parentInformationSupplyChainGUID != null)
        {
            addInformationSupplyChainComposition(parentInformationSupplyChainGUID, entity.getGUID());
        }

        return entity.getGUID();
    }



    /**
     * Link an element to its implementation
     *
     * @param derivedFromGUID guid of element that the implementation is derived from
     * @param implementedByGUID guid of the element that described the implementation
     * @param designStep process that created the implementation
     * @param role role performed by the implementation
     * @param transformation transformation used to create the implementation
     * @param description description of the implementation
     */
    public void addImplementedByRelationship(String derivedFromGUID,
                                             String implementedByGUID,
                                             String designStep,
                                             String role,
                                             String transformation,
                                             String description)
    {
        final String methodName = "addImplementedByRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(derivedFromGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(implementedByGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DESIGN_STEP.name, designStep, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ROLE.name, role, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.TRANSFORMATION.name, transformation, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.IMPLEMENTED_BY_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(derivedFromGUID + "_to_" + implementedByGUID + "_implemented_by_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }




    /**
     * Link an element to its implementation
     *
     * @param usedInImplementationOfGUID guid of element that the implementation is derived from
     * @param implementationResourceGUID guid of the element that can be used as part of an implementation
     * @param description description of the implementation
     */
    public void addImplementationResourceRelationship(String usedInImplementationOfGUID,
                                                      String implementationResourceGUID,
                                                      String description)
    {
        final String methodName = "addImplementationResourceRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(usedInImplementationOfGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(implementationResourceGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.IMPLEMENTATION_RESOURCE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(usedInImplementationOfGUID + "_to_" + implementationResourceGUID + "_implementation_resource_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link an information supply chain to one of its segments
     *
     * @param informationSupplyChainGUID guid of information supply chain
     * @param segmentGUID guid of the segment

     */
    public void addInformationSupplyChainComposition(String informationSupplyChainGUID,
                                                     String segmentGUID)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(informationSupplyChainGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(segmentGUID));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.INFORMATION_SUPPLY_CHAIN_COMPOSITION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(informationSupplyChainGUID + "_to_" + segmentGUID + "_information_supply_chain_composition_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link an element to its implementation
     *
     * @param supplyFromGUID guid of first segment
     * @param supplyToGUID guid of follow-on segment
     * @param label label to use in lineage graphs
     * @param description description of the link
     */
    public void addInformationSupplyChainLinkRelationship(String supplyFromGUID,
                                                          String supplyToGUID,
                                                          String label,
                                                          String description)
    {
        final String methodName = "addInformationSupplyChainLinkRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(supplyFromGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(supplyToGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.LABEL.name, label, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.INFORMATION_SUPPLY_CHAIN_LINK_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(supplyFromGUID + "_to_" + supplyToGUID + "_information_supply_chain_link_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Add a new solution blueprint.
     *
     * @param suppliedTypeName type name to use
     * @param qualifiedName qualified name
     * @param name display name
     * @param description description
     * @param versionIdentifier versionIdentifier
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addSolutionBlueprint(String              suppliedTypeName,
                                        String              qualifiedName,
                                        String              name,
                                        String              description,
                                        String              versionIdentifier,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties)
    {
        final String methodName = "addSolutionBlueprint";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataType.SOLUTION_BLUEPRINT.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(null, typeName, OpenMetadataType.SOLUTION_BLUEPRINT.typeName, null, methodName));

        EntityDetail entity = archiveHelper.getEntityDetail(typeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            classifications);

        archiveBuilder.addEntity(entity);

        return entity.getGUID();
    }



    /**
     * Add a new solution component.
     *
     * @param solutionBlueprintGUIDs owning blueprint
     * @param suppliedTypeName type name to use
     * @param qualifiedName qualified name
     * @param name display name
     * @param description description
     * @param versionIdentifier versionIdentifier
     * @param componentType type of component
     * @param implementationType planned deployed implementation type
     * @param roleInSolution role this component takes in the solution
     * @param roleInSolutionDescription description of the role in the solution
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addSolutionComponent(List<String>        solutionBlueprintGUIDs,
                                        String              suppliedTypeName,
                                        String              qualifiedName,
                                        String              name,
                                        String              description,
                                        String              versionIdentifier,
                                        String              componentType,
                                        String              implementationType,
                                        String              roleInSolution,
                                        String              roleInSolutionDescription,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties)
    {
        final String methodName = "addSolutionComponent";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataType.SOLUTION_COMPONENT.typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DISPLAY_NAME.name, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.VERSION_IDENTIFIER.name, versionIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.SOLUTION_COMPONENT_TYPE.name, componentType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.PLANNED_DEPLOYED_IMPLEMENTATION_TYPE.name, implementationType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.ADDITIONAL_PROPERTIES.name, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(this.getAnchorClassification(null, typeName, OpenMetadataType.SOLUTION_COMPONENT.typeName, null, methodName));

        EntityDetail entity = archiveHelper.getEntityDetail(typeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            classifications);

        archiveBuilder.addEntity(entity);

        if (solutionBlueprintGUIDs != null)
        {
            for (String solutionBlueprintGUID : solutionBlueprintGUIDs)
            {
                addSolutionBlueprintCompositionRelationship(solutionBlueprintGUID, entity.getGUID(), roleInSolution, roleInSolutionDescription);
            }
        }

        return entity.getGUID();
    }



    /**
     * Link a solution blueprint to one of its components
     *
     * @param solutionBlueprintGUID guid of blueprint
     * @param solutionComponentGUID guid of component
     * @param role role of the component in the solution
     * @param description description of the role in the solution
     */
    public void addSolutionBlueprintCompositionRelationship(String solutionBlueprintGUID,
                                                            String solutionComponentGUID,
                                                            String role,
                                                            String description)
    {
        final String methodName = "addISolutionBlueprintCompositionRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(solutionBlueprintGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(solutionComponentGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ROLE.name, role, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SOLUTION_BLUEPRINT_COMPOSITION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(solutionBlueprintGUID + "_to_" + solutionComponentGUID + "_solution_blueprint_composition_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link solutions components to show data/control flow.
     *
     * @param solutionComponent1GUID guid of component at end 1
     * @param solutionComponent2GUID guid of component at end 2
     * @param label used in lineage graphs
     * @param description explains the progression of control between components
     * @param iscQualifiedNamesGUIDs optional list of information supply chain segments that his link is part of
     */
    public void addSolutionLinkingWireRelationship(String       solutionComponent1GUID,
                                                   String       solutionComponent2GUID,
                                                   String       label,
                                                   String       description,
                                                   List<String> iscQualifiedNamesGUIDs)
    {
        final String methodName = "addSolutionLinkingWireRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(solutionComponent1GUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(solutionComponent2GUID));

        InstanceProperties properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ISC_QUALIFIED_NAMES.name, iscQualifiedNamesGUIDs, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.LABEL.name, label, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SOLUTION_LINKING_WIRE_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(solutionComponent1GUID + "_to_" + solutionComponent2GUID + "_solution_linking_wire_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link an element to its actor
     *
     * @param solutionActorGUID guid of blueprint
     * @param solutionComponentGUID guid of component
     * @param role role of the component in the solution
     * @param description description of the role in the solution
     */
    public void addSolutionComponentActorRelationship(String solutionActorGUID,
                                                      String solutionComponentGUID,
                                                      String role,
                                                      String description)
    {
        final String methodName = "addSolutionComponentActorRelationship";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(solutionActorGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(solutionComponentGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataProperty.ROLE.name, role, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataProperty.DESCRIPTION.name, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SOLUTION_COMPONENT_ACTOR_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(solutionActorGUID + "_to_" + solutionComponentGUID + "_solution_component_actor_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link an information supply chain to one of its segments
     *
     * @param parentSolutionComponentGUID guid of parent
     * @param childSolutionComponentGUID guid of the child

     */
    public void addSolutionCompositionRelationship(String parentSolutionComponentGUID,
                                                   String childSolutionComponentGUID)
    {
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(parentSolutionComponentGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(childSolutionComponentGUID));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.SOLUTION_COMPOSITION_RELATIONSHIP.typeName,
                                                                     idToGUIDMap.getGUID(parentSolutionComponentGUID + "_to_" + childSolutionComponentGUID + "_solution_composition_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }
}
