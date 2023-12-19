/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataTypesMapper;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataValidValues;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveGUIDMap;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    protected EnumElementDef     activeStatus;


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

        this.activeStatus = archiveHelper.getEnumElement(OpenMetadataTypesMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME, 1);
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
     * Create an external reference entity.  This typically describes a publication, webpage book or reference source of information
     * that is from an external organization.
     *
     * @param typeName name of element subtype to use - default is ExternalReference
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
    public String addExternalReference(String               typeName,
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
        final String methodName = "addExternalReference";

        String elementTypeName = OpenMetadataTypesMapper.EXTERNAL_REFERENCE_TYPE_NAME;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REFERENCE_TITLE_PROPERTY_NAME, referenceTitle, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REFERENCE_ABSTRACT_PROPERTY_NAME, referenceAbstract, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.AUTHORS_PROPERTY_NAME, authors, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NUMBER_OF_PAGES_PROPERTY_NAME, numberOfPages, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PAGE_RANGE_PROPERTY_NAME, pageRange, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ORGANIZATION_PROPERTY_NAME, authorOrganization, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REFERENCE_VERSION_PROPERTY_NAME, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PUBLICATION_SERIES_PROPERTY_NAME, publicationSeries, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PUBLICATION_SERIES_VOLUME_PROPERTY_NAME, publicationSeriesVolume, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.EDITION_PROPERTY_NAME, edition, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.URL_PROPERTY_NAME, referenceURL, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PUBLISHER_PROPERTY_NAME, publisher, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.FIRST_PUB_DATE_PROPERTY_NAME, firstPublicationDate, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PUBLICATION_DATE_PROPERTY_NAME, publicationDate, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PUBLICATION_CITY_PROPERTY_NAME, publicationCity, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PUBLICATION_YEAR_PROPERTY_NAME, publicationYear, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PUBLICATION_NUMBERS_PROPERTY_NAME, publicationNumbers, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.LICENSE_PROPERTY_NAME, license, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.COPYRIGHT_PROPERTY_NAME, copyright, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ATTRIBUTION_PROPERTY_NAME, attribution, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail externalReferenceEntity = archiveHelper.getEntityDetail(elementTypeName,
                                                                             idToGUIDMap.getGUID(qualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             null);

        archiveBuilder.addEntity(externalReferenceEntity);

        if (searchKeywords != null)
        {
            for (String keyword : searchKeywords)
            {
                if (keyword != null)
                {
                    String keywordGUID = idToGUIDMap.queryGUID(OpenMetadataTypesMapper.SEARCH_KEYWORD_TYPE_NAME + ":" + keyword);
                    EntityDetail keywordEntity = null;

                    InstanceProperties keywordProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.KEYWORD_PROPERTY_NAME, keyword, methodName);

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.queryEntity(keywordGUID);
                    }

                    if (keywordEntity == null)
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.SEARCH_KEYWORD_TYPE_NAME,
                                                                       idToGUIDMap.getGUID(OpenMetadataTypesMapper.SEARCH_KEYWORD_TYPE_NAME + ":" + keyword),
                                                                       keywordProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                        archiveBuilder.addEntity(keywordEntity);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(externalReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME,
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.REFERENCE_ID_PROPERTY_NAME, referenceId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PAGES_PROPERTY_NAME, pages, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.REFERENCEABLE_TO_EXT_REF_TYPE_NAME,
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

        String elementTypeName = OpenMetadataTypesMapper.RELATED_MEDIA_TYPE_NAME;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        EnumElementDef typeEnumElement = archiveHelper.getEnumElement(OpenMetadataTypesMapper.MEDIA_TYPE_ENUM_TYPE_NAME, mediaType);
        EnumElementDef usageEnumElement = archiveHelper.getEnumElement(OpenMetadataTypesMapper.MEDIA_USAGE_ENUM_TYPE_NAME, defaultMediaUsage);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.AUTHORS_PROPERTY_NAME, authors, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ORGANIZATION_PROPERTY_NAME, authorOrganization, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REFERENCE_VERSION_PROPERTY_NAME, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.URL_PROPERTY_NAME, referenceURL, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.LICENSE_PROPERTY_NAME, license, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.COPYRIGHT_PROPERTY_NAME, copyright, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ATTRIBUTION_PROPERTY_NAME, attribution, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MEDIA_TYPE_PROPERTY_NAME, typeEnumElement.getOrdinal(), typeEnumElement.getValue(), typeEnumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MEDIA_TYPE_OTHER_ID_PROPERTY_NAME, mediaTypeOtherId, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DEFAULT_MEDIA_USAGE_PROPERTY_NAME, usageEnumElement.getOrdinal(), usageEnumElement.getValue(), usageEnumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DEFAULT_MEDIA_USAGE_OTHER_ID_PROPERTY_NAME, defaultMediaUsageOtherId, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
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
                    String keywordGUID = idToGUIDMap.queryGUID(OpenMetadataTypesMapper.SEARCH_KEYWORD_TYPE_NAME + ":" + keyword);
                    EntityDetail keywordEntity = null;

                    InstanceProperties keywordProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.KEYWORD_PROPERTY_NAME, keyword, methodName);

                    if (keywordGUID != null)
                    {
                        keywordEntity = archiveBuilder.queryEntity(keywordGUID);
                    }

                    if (keywordEntity == null)
                    {
                        keywordEntity  = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.SEARCH_KEYWORD_TYPE_NAME,
                                                                       idToGUIDMap.getGUID(OpenMetadataTypesMapper.SEARCH_KEYWORD_TYPE_NAME + ":" + keyword),
                                                                       keywordProperties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);
                        archiveBuilder.addEntity(keywordEntity);
                    }

                    if (keywordEntity != null)
                    {
                        EntityProxy end1 = archiveHelper.getEntityProxy(mediaReferenceEntity);
                        EntityProxy end2 = archiveHelper.getEntityProxy(keywordEntity);

                        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.REFERENCEABLE_TO_SEARCH_KEYWORD_TYPE_NAME,
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

        EnumElementDef enumElement = archiveHelper.getEnumElement(OpenMetadataTypesMapper.MEDIA_USAGE_ENUM_TYPE_NAME, mediaUsage);

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);
        EntityDetail externalReferenceEntity = archiveBuilder.getEntity(mediaReferenceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(referenceableEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(externalReferenceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.MEDIA_ID_PROPERTY_NAME, mediaId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MEDIA_USAGE_PROPERTY_NAME, enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MEDIA_USAGE_OTHER_ID_PROPERTY_NAME, mediaUsageOtherId, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.REFERENCEABLE_TO_RELATED_MEDIA_TYPE_NAME,
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

        EnumElementDef statusEnumElement = archiveHelper.getEnumElement(OpenMetadataTypesMapper.OPERATIONAL_STATUS_ENUM_TYPE_NAME, deploymentStatus);

        InstanceProperties properties = archiveHelper.addDatePropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.DEPLOYMENT_TIME_PROPERTY_NAME, deploymentTime, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DEPLOYER_TYPE_NAME_PROPERTY_NAME, deployerTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DEPLOYER_PROPERTY_NAME_PROPERTY_NAME, deployerPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DEPLOYER_PROPERTY_NAME, deployer, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DEPLOYMENT_STATUS_PROPERTY_NAME, statusEnumElement.getOrdinal(), statusEnumElement.getValue(), statusEnumElement.getDescription(), methodName);

        this.archiveBuilder.addRelationship(this.archiveHelper.getRelationship(OpenMetadataTypesMapper.DEPLOYED_ON_TYPE_NAME, this.idToGUIDMap.getGUID(deployedElementId + "_to_" + deployedOnId + "_deployed_on_relationship"), properties, InstanceStatus.ACTIVE, end1, end2));
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.COORDINATES_PROPERTY_NAME, coordinates, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MAP_PROJECTION_PROPERTY_NAME, mapProjection, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.POSTAL_ADDRESS_PROPERTY_NAME, postalAddress, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TIME_ZONE_PROPERTY_NAME, timeZone, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, OpenMetadataTypesMapper.FIXED_LOCATION_CLASSIFICATION_TYPE_NAME, properties, methodName);
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.LEVEL_PROPERTY_NAME, level, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, securityDescription, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, OpenMetadataTypesMapper.SECURE_LOCATION_CLASSIFICATION_TYPE_NAME, properties, methodName);
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.NETWORK_ADDRESS_PROPERTY_NAME, networkAddress, methodName);

        return addClassifiedLocation(qualifiedName, identifier, displayName, description, additionalProperties, OpenMetadataTypesMapper.CYBER_LOCATION_CLASSIFICATION_TYPE_NAME, properties, methodName);
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
        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        Classification classification = archiveHelper.getClassification(classificationName, classificationProperties, InstanceStatus.ACTIVE);

        classifications.add(classification);

        EntityDetail location = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.LOCATION_TYPE_NAME,
                                                              idToGUIDMap.getGUID(qualifiedName),
                                                              properties,
                                                              InstanceStatus.ACTIVE,
                                                              classifications);

        archiveBuilder.addEntity(location);

        return location.getGUID();
    }


    /**
     * Add the MobileAsset classification to the requested asset.
     *
     * @param assetGUID unique identifier of the element to classify
     */
    public void addMobileAssetClassification(String assetGUID)
    {
        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

        EntityProxy entityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = archiveHelper.getClassification(OpenMetadataTypesMapper.MOBILE_ASSET_CLASSIFICATION_TYPE_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.NESTED_LOCATION_TYPE_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.ADJACENT_LOCATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(location1GUID + "_to_" + location2GUID + "_adjacent_location_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a location to an asset.
     *
     * @param locationQName qualified name of the location
     * @param assetQName qualified name of the asset
     */
    public void addAssetLocationRelationship(String locationQName,
                                             String assetQName)
    {
        String validValueId = idToGUIDMap.getGUID(locationQName);
        String assetId = idToGUIDMap.getGUID(assetQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(assetId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.ASSET_LOCATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(validValueId + "_to_" + assetId + "_asset_location_relationship"),
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.USER_ID_PROPERTY_NAME, userId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISTINGUISHED_NAME_PROPERTY_NAME, distinguishedName, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail userIdentity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.USER_IDENTITY_TYPE_NAME,
                                                                  idToGUIDMap.getGUID(qualifiedName),
                                                                  properties,
                                                                  InstanceStatus.ACTIVE,
                                                                  null);

        archiveBuilder.addEntity(userIdentity);

        return userIdentity.getGUID();
    }


    /**
     * Link a location to a profile.
     *
     * @param profileQName qualified name of the profile
     * @param locationQName qualified name of the location
     * @param associationType identifier that describes the purpose of the association.
     */
    public void addProfileLocationRelationship(String profileQName,
                                               String locationQName,
                                               String associationType)
    {
        final String methodName = "addProfileLocationRelationship";

        String entity1GUID = idToGUIDMap.getGUID(locationQName);
        String entity2GUID = idToGUIDMap.getGUID(profileQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(entity1GUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(entity2GUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.ASSOCIATION_TYPE_PROPERTY_NAME, associationType, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PROFILE_LOCATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(entity1GUID + "_to_" + entity2GUID + "_profile_location_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.ROLE_TYPE_NAME_PROPERTY_NAME, roleTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ROLE_GUID_PROPERTY_NAME, roleGUID, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PROFILE_IDENTITY_RELATIONSHIP_TYPE_NAME,
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
                                   String  name,
                                   String  contactType,
                                   int     contactMethodType,
                                   String  contactMethodService,
                                   String  contactMethodValue)
    {
        final String methodName = "addContactDetails";

        EntityDetail profileEntity = archiveBuilder.getEntity(profileGUID);

        EnumElementDef enumElement = archiveHelper.getEnumElement(OpenMetadataTypesMapper.CONTACT_METHOD_TYPE_ENUM_TYPE_NAME, contactMethodType);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONTACT_TYPE_PROPERTY_NAME, contactType, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONTACT_METHOD_TYPE_PROPERTY_NAME, enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONTACT_METHOD_SERVICE_PROPERTY_NAME, contactMethodService, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONTACT_METHOD_VALUE_PROPERTY_NAME, contactMethodValue, methodName);

        EntityDetail contactDetails = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.CONTACT_DETAILS_TYPE_NAME,
                                                                    idToGUIDMap.getGUID(contactMethodValue),
                                                                    properties,
                                                                    InstanceStatus.ACTIVE,
                                                                    null);

        archiveBuilder.addEntity(contactDetails);

        EntityProxy end1 = archiveHelper.getEntityProxy(profileEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(contactDetails);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CONTACT_THROUGH_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(profileGUID + "_to_" + contactDetails.getGUID() + "_contact_through_relationship"),
                                                                     properties,
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
    public  String addPersonRole(String              suppliedTypeName,
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
        final String methodName = "addPersonRole";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataTypesMapper.PERSON_ROLE_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, scope, methodName);
        if (setHeadCount)
        {
            properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.HEAD_COUNT_PROPERTY_NAME, headCount, methodName);
        }
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
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
        final String methodName = "addPersonRole";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataTypesMapper.PERSON_ROLE_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, scope, methodName);
        if (setHeadCount)
        {
            properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.HEAD_COUNT_PROPERTY_NAME, headCount, methodName);
        }
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
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
     * @param isPublic is this appointment public?
     */
    public void addPersonRoleAppointmentRelationship(String  personQName,
                                                     String  personRoleQName,
                                                     boolean isPublic)
    {
        final String methodName = "addPersonRoleAppointmentRelationship";

        String guid1 = idToGUIDMap.getGUID(personQName);
        String guid2 = idToGUIDMap.getGUID(personRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.IS_PUBLIC_PROPERTY_NAME, isPublic, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PERSON_ROLE_APPOINTMENT_RELATIONSHIP_TYPE_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PEER_RELATIONSHIP_TYPE_NAME,
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
     * @param isPublic is this profile public
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
                             boolean             isPublic,
                             Map<String, String> additionalProperties)
    {
        final String methodName = "addPerson";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PRONOUNS_PROPERTY_NAME, pronouns, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TITLE_PROPERTY_NAME, title, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.INITIALS_PROPERTY_NAME, initials, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.GIVEN_NAMES_PROPERTY_NAME, givenNames, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SURNAME_PROPERTY_NAME, surname, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.FULL_NAME_PROPERTY_NAME, fullName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.JOB_TITLE_PROPERTY_NAME, jobTitle, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.EMPLOYEE_NUMBER_PROPERTY_NAME, employeeNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.EMPLOYEE_TYPE_PROPERTY_NAME, employeeType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PREFERRED_LANGUAGE_PROPERTY_NAME, preferredLanguage, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IS_PUBLIC_PROPERTY_NAME, isPublic, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail person = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.PERSON_TYPE_NAME,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

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
            typeName = OpenMetadataTypesMapper.TEAM_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TEAM_TYPE_PROPERTY_NAME, teamType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail profile = archiveHelper.getEntityDetail(typeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.POSITION_PROPERTY_NAME, position, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TEAM_LEADERSHIP_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_leadership_relationship"),
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.POSITION_PROPERTY_NAME, position, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TEAM_MEMBERSHIP_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a person role as a team's member.
     *
     * @param superTeamQName qualified name of the super team profile
     * @param subTeamQName qualified name of the subteam profile
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

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.DELEGATION_ESCALATION_PROPERTY_NAME, delegationEscalationAuthority, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TEAM_STRUCTURE_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_team_structure_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add a new IT profile.
     *
     * @param assetGUID unique identifier of asset to connect the profile to.
     * @param qualifiedName qualified name of profile
     * @param name display name (preferred name of individual)
     * @param description description (eg job description)
     * @param additionalProperties are there any additional properties to add
     * @return unique identifier of the new profile
     */
    public  String addITProfileToAsset(String              assetGUID,
                                       String              qualifiedName,
                                       String              name,
                                       String              description,
                                       Map<String, String> additionalProperties)
    {
        final String methodName = "addITProfileToAsset";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail profile = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.IT_PROFILE_TYPE_NAME,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

        archiveBuilder.addEntity(profile);

        if (assetGUID != null)
        {
            EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(assetEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(profile);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(assetGUID + "_to_" + profile.getGUID() + "_it_infrastructure_profile_relationship"),
                                                                         properties,
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.ASSIGNMENT_TYPE_PROPERTY_NAME, assignmentType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.ASSIGNMENT_SCOPE_RELATIONSHIP_TYPE_NAME,
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
     * @param projectStatus status of the project
     * @param setCampaignClassification should the Campaign classification be set?
     * @param setTaskClassification should the Task classification be set?
     * @param projectTypeClassification add special classification that defines the type of project - eg GlossaryProject or GovernanceProject
     * @param additionalProperties are there any additional properties to add
     * @param extendedProperties any additional properties associated with a subtype
     * @return unique identifier of the new profile
     */
    public  String addProject(String              suppliedTypeName,
                              String              qualifiedName,
                              String              identifier,
                              String              name,
                              String              description,
                              Date                startDate,
                              Date                plannedEndDate,
                              String              projectStatus,
                              boolean             setCampaignClassification,
                              boolean             setTaskClassification,
                              String              projectTypeClassification,
                              Map<String, String> additionalProperties,
                              Map<String, Object> extendedProperties)
    {
        final String methodName = "addProject";

        String typeName = suppliedTypeName;

        if (typeName == null)
        {
            typeName = OpenMetadataTypesMapper.PROJECT_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.START_DATE_PROPERTY_NAME, startDate, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PLANNED_END_DATE_PROPERTY_NAME, plannedEndDate, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PROJECT_STATUS_PROPERTY_NAME, projectStatus, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        if (setCampaignClassification)
        {
            Classification classification = archiveHelper.getClassification(OpenMetadataTypesMapper.CAMPAIGN_CLASSIFICATION_TYPE_NAME, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }
        if (setTaskClassification)
        {
            Classification classification = archiveHelper.getClassification(OpenMetadataTypesMapper.TASK_CLASSIFICATION_TYPE_NAME, null, InstanceStatus.ACTIVE);

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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PROJECT_HIERARCHY_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a referenceable to its stakeholders.
     *
     * @param referenceableQName qualified name of the parent project
     * @param actorQName qualified name of the subproject
     */
    public void addStakeHolderRelationship(String  referenceableQName,
                                           String  actorQName)
    {
        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(actorQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.STAKEHOLDER_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_stakeholder_relationship"),
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.DEPENDENCY_SUMMARY_PROPERTY_NAME, dependencySummary, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PROJECT_DEPENDENCY_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_dependency_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Link a project to a team.  A project may have multiple teams.
     *
     * @param personQName qualified name of the person profile
     * @param personRoleQName qualified name of the person role
     * @param teamRole role of this team in the project
     */
    public void addProjectTeamRelationship(String  personQName,
                                           String  personRoleQName,
                                           String  teamRole)
    {
        final String methodName = "addProjectTeamRelationship";

        String guid1 = idToGUIDMap.getGUID(personQName);
        String guid2 = idToGUIDMap.getGUID(personRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.TEAM_ROLE_PROPERTY_NAME, teamRole, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PROJECT_TEAM_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_team_relationship"),
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
        String guid1 = idToGUIDMap.getGUID(projectQName);
        String guid2 = idToGUIDMap.getGUID(projectManagerRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.PROJECT_MANAGEMENT_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_project_management_relationship"),
                                                                     null,
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
            typeName = OpenMetadataTypesMapper.COMMUNITY_TYPE_NAME;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MISSION_PROPERTY_NAME, mission, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
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
                                                   int     membershipType)
    {
        final String methodName = "addCommunityMembershipRelationship";

        EnumElementDef enumElement = archiveHelper.getEnumElement(OpenMetadataTypesMapper.COMMUNITY_MEMBERSHIP_TYPE_ENUM_TYPE_NAME, membershipType);

        String guid1 = idToGUIDMap.getGUID(communityQName);
        String guid2 = idToGUIDMap.getGUID(membershipRoleQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.MEMBERSHIP_TYPE_PROPERTY_NAME, enumElement.getOrdinal(), enumElement.getValue(), enumElement.getDescription(), methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.COMMUNITY_MEMBERSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_community_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }



    /**
     * Create a governance definition entity.
     *
     * @param suppliedTypeName type of collection
     * @param classificationName name of classification to attach
     * @param qualifiedName unique name for the collection entity
     * @param displayName display name for the collection
     * @param description description about the collection
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the subtype
     *
     * @return unique identifier for subject area (collectionGUID)
     */
    public String addCollection(String               suppliedTypeName,
                                String               classificationName,
                                String               qualifiedName,
                                String               displayName,
                                String               description,
                                Map<String, String>  additionalProperties,
                                Map<String, Object>  extendedProperties)
    {
        final String methodName = "addCollection";

        String typeName = OpenMetadataTypesMapper.COLLECTION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        List<Classification> classifications = null;

        if (classificationName != null)
        {
            classifications = new ArrayList<>();

            Classification classification = archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);

            classifications.add(classification);
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.MEMBERSHIP_RATIONALE_PROPERTY_NAME, membershipRationale, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(collectionGUID + "_to_" + memberGUID + "_collection_membership_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create a governance domain description entity.
     *
     * @param qualifiedName unique name for the governance domain entity
     * @param domainIdentifier unique identifier of the governance domain
     * @param displayName display name for the governance domain
     * @param description description about the governance domain
     * @param additionalProperties any other properties
     * @param governanceDomainSetGUID unique identifier of the collection for the domain definitions
     *
     * @return unique identifier for the governance domain (governanceDomainGUID)
     */
    public String addGovernanceDomainDescription(String               governanceDomainSetGUID,
                                                 String               qualifiedName,
                                                 int                  domainIdentifier,
                                                 String               displayName,
                                                 String               description,
                                                 Map<String, String>  additionalProperties)
    {
        final String methodName = "addGovernanceDomainDescription";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.GOVERNANCE_DOMAIN_TYPE_NAME,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        if (governanceDomainSetGUID != null)
        {
            addMemberToCollection(governanceDomainSetGUID, newEntity.getGUID(), null);
        }

        return newEntity.getGUID();
    }


    /**
     * Create a governance definition entity.
     *
     * @param suppliedTypeName type of governance definition to add
     * @param qualifiedName unique name for the governance definition entity
     * @param title title for the governance definition
     * @param summary short description for the governance definition
     * @param description description about the governance definition
     * @param scope scope where the governance definition is used
     * @param priority how important is the governance definition
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
                                          String               title,
                                          String               summary,
                                          String               description,
                                          String               scope,
                                          int                  domainIdentifier,
                                          String               priority,
                                          List<String>         implications,
                                          List<String>         outcomes,
                                          List<String>         results,
                                          Map<String, String>  additionalProperties,
                                          Map<String, Object>  extendedProperties)
    {
        final String methodName = "addGovernanceDefinition";

        String typeName = OpenMetadataTypesMapper.GOVERNANCE_DEFINITION_TYPE_NAME;

        if (suppliedTypeName != null)
        {
            typeName = suppliedTypeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TITLE_PROPERTY_NAME, title, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SUMMARY_PROPERTY_NAME, summary, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PRIORITY_PROPERTY_NAME, priority, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IMPLICATIONS_PROPERTY_NAME, implications, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.OUTCOMES_PROPERTY_NAME, outcomes, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.RESULTS_PROPERTY_NAME, results, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(typeName,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

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
        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(governanceDefinitionQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.GOVERNANCE_DEFINITION_SCOPE_TYPE_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.GOVERNED_BY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_governed_by_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }

    /**
     * Link a referenceable to another referenceable to indicate that the second referenceable is providing resources in support of the first.
     *
     * @param referenceableQName qualified name of the referenceable
     * @param resourceQName qualified name of the second referenceable
     * @param resourceUse string description
     * @param watchResource should the resource be watched (boolean)
     */
    public void addResourceListRelationship(String  referenceableQName,
                                            String  resourceQName,
                                            String  resourceUse,
                                            boolean watchResource)
    {
        final String methodName = "addResourceListRelationship";

        String guid1 = idToGUIDMap.getGUID(referenceableQName);
        String guid2 = idToGUIDMap.getGUID(resourceQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid1));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(guid2));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.RESOURCE_USE_PROPERTY_NAME, resourceUse, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.WATCH_RESOURCE_PROPERTY_NAME, watchResource, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.RESOURCE_LIST_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(guid1 + "_to_" + guid2 + "_resource_list_relationship"),
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);

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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.RATIONALE_PROPERTY_NAME, rationale, methodName);

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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ZONE_NAME_PROPERTY_NAME, zoneName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CRITERIA_PROPERTY_NAME, criteria, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.ZONE_TYPE_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.ZONE_HIERARCHY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(broaderGovernanceZoneGUID + "_to_" + nestedGovernanceZoneGUID + "_zone_hierarchy_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
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
        final String methodName = "addAssetZoneMembershipClassification";

        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);
        EntityProxy  entityProxy = archiveHelper.getEntityProxy(assetEntity);

        Classification  classification = archiveHelper.getClassification(OpenMetadataTypesMapper.ASSET_ZONES_CLASSIFICATION_NAME,
                                                                         archiveHelper.addStringArrayPropertyToInstance(archiveRootName,
                                                                                                                        null,
                                                                                                                        OpenMetadataTypesMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                                                        zones,
                                                                                                                        methodName),
                                                                         InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SUBJECT_AREA_NAME_PROPERTY_NAME, subjectAreaName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.USAGE_PROPERTY_NAME, usage, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DOMAIN_IDENTIFIER_PROPERTY_NAME, domainIdentifier, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.SUBJECT_AREA_TYPE_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.SUBJECT_AREA_HIERARCHY_TYPE_NAME,
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
     * @param subjectAreaQualifiedName name of the subject area.  The suggestion is that the name used is the qualified name.
     */
    public void addSubjectAreaClassification(String referenceableGUID,
                                             String subjectAreaQualifiedName)
    {
        final String methodName = "addSubjectAreaClassification";

        EntityDetail referenceableEntity = archiveBuilder.getEntity(referenceableGUID);

        EntityProxy referenceableEntityProxy = archiveHelper.getEntityProxy(referenceableEntity);

        Classification  subjectAreaClassification = archiveHelper.getClassification(OpenMetadataTypesMapper.SUBJECT_AREA_CLASSIFICATION_TYPE_NAME,
                                                                                    archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                              null,
                                                                                                                              OpenMetadataTypesMapper.NAME_PROPERTY_NAME,
                                                                                                                              subjectAreaQualifiedName,
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

        EnumElementDef businessCapabilityTypeEnum = archiveHelper.getEnumElement(OpenMetadataTypesMapper.BUSINESS_CAPABILITY_TYPE_ENUM_TYPE_NAME, OpenMetadataTypesMapper.BUSINESS_CAPABILITY_TYPE_BUSINESS_AREA);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.BUSINESS_CAPABILITY_TYPE_PROPERTY_NAME, businessCapabilityTypeEnum.getOrdinal(), businessCapabilityTypeEnum.getValue(), businessCapabilityTypeEnum.getDescription(), methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail newEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.BUSINESS_CAPABILITY_TYPE_NAME,
                                                               idToGUIDMap.getGUID(qualifiedName),
                                                               properties,
                                                               InstanceStatus.ACTIVE,
                                                               null);

        archiveBuilder.addEntity(newEntity);

        return newEntity.getGUID();
    }


    /**
     * Add an OrganizationalCapability relationship.
     *
     * @param businessCapabilityQName qualified name of the specialized term
     * @param teamQName qualified name of the generalized term
     * @param scope scope of the team's ability to support the business capability
     */
    public void addOrganizationalCapabilityRelationship(String businessCapabilityQName,
                                                        String teamQName,
                                                        String scope)
    {
        final String methodName = "addOrganizationalCapabilityRelationship";

        String end1GUID = idToGUIDMap.getGUID(businessCapabilityQName);
        String end2GUID = idToGUIDMap.getGUID(teamQName);
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end1GUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(end2GUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, scope, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.ORGANIZATIONAL_CAPABILITY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(end1GUID + "_to_" + end2GUID + "_organizational_capability_relationship"),
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

        String elementTypeName = OpenMetadataTypesMapper.DESIGN_MODEL_TYPE_NAME;

        if (typeName != null)
        {
            elementTypeName = typeName;
        }

        List<Classification> entityClassifications = null;

        if (classificationName != null)
        {
            entityClassifications = new ArrayList<>();

            Classification classification = archiveHelper.getClassification(classificationName, null, InstanceStatus.ACTIVE);

            entityClassifications.add(classification);
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, displayName, methodName); // it's an asset
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TECHNICAL_NAME_PROPERTY_NAME, technicalName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.VERSION_NUMBER_PROPERTY_NAME, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.AUTHOR_PROPERTY_NAME, author, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
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

        String elementTypeName = OpenMetadataTypesMapper.DESIGN_MODEL_ELEMENT_TYPE_NAME;

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

            InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.ANCHOR_GUID_PROPERTY_NAME, designModelGUID, methodName);
            classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataTypesMapper.ANCHOR_TYPE_NAME_PROPERTY_NAME, designModelTypeName, methodName);
            Classification     classification           = archiveHelper.getClassification(OpenMetadataTypesMapper.ANCHORS_CLASSIFICATION_TYPE_NAME, classificationProperties, InstanceStatus.ACTIVE);

            entityClassifications.add(classification);
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TECHNICAL_NAME_PROPERTY_NAME, technicalName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.VERSION_NUMBER_PROPERTY_NAME, versionNumber, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.AUTHOR_PROPERTY_NAME, author, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
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

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
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
        EnumElementDef conceptModelDecorationEnum = archiveHelper.getEnumElement(OpenMetadataTypesMapper.CONCEPT_MODEL_DECORATION_ENUM_NAME, conceptModelDecoration);

        EntityProxy end1 = archiveHelper.getEntityProxy(entityOne);
        EntityProxy end2 = archiveHelper.getEntityProxy(entityTwo);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.ATTRIBUTE_NAME_PROPERTY_NAME, attributeName, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.POSITION_PROPERTY_NAME, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MIN_CARDINALITY_PROPERTY_NAME, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MAX_CARDINALITY_PROPERTY_NAME, maxCardinality, methodName);
        properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DECORATION_PROPERTY_NAME, conceptModelDecorationEnum.getOrdinal(), conceptModelDecorationEnum.getValue(), conceptModelDecorationEnum.getDescription(), methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.UNIQUE_VALUES_PROPERTY_NAME, uniqueValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ORDERED_VALUES_PROPERTY_NAME, orderedValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAVIGABLE_PROPERTY_NAME, navigable, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
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

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.POSITION_PROPERTY_NAME, position, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MIN_CARDINALITY_PROPERTY_NAME, minCardinality, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.MAX_CARDINALITY_PROPERTY_NAME, maxCardinality, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.UNIQUE_VALUES_PROPERTY_NAME, uniqueValues, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties,  OpenMetadataTypesMapper.ORDERED_VALUES_PROPERTY_NAME, orderedValues, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.DESIGN_MODEL_GROUP_MEMBERSHIP_NAME,
                                                                     idToGUIDMap.getGUID(conceptBeadGUID + "_to_" + conceptBeadAttributeGUID + "_concept_bead_attribute_link_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
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
     * Create an asset entity.
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

        String assetTypeName = OpenMetadataTypesMapper.ASSET_TYPE_NAME;

        if (typeName != null)
        {
            assetTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.VERSION_IDENTIFIER_PROPERTY_NAME, versionIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(assetTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

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
                                                                                           OpenMetadataTypesMapper.ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                           governanceZones,
                                                                                           methodName);

            Classification classification = archiveHelper.getClassification(OpenMetadataTypesMapper.ASSET_ZONES_CLASSIFICATION_NAME,
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

        String processTypeName = OpenMetadataTypesMapper.PROCESS_TYPE_NAME;

        if (typeName != null)
        {
            processTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.VERSION_IDENTIFIER_PROPERTY_NAME, versionIdentifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.FORMULA_PROPERTY_NAME, formula, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail assetEntity = archiveHelper.getEntityDetail(processTypeName,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(assetEntity);

        return assetEntity.getGUID();
    }


    /**
     * Create a software capability entity.
     *
     * @param typeName name of software capability subtype to use - default is SoftwareCapability
     * @param qualifiedName unique name for the capability
     * @param name display name for the capability
     * @param description description about the capability
     * @param capabilityType type
     * @param capabilityVersion version
     * @param patchLevel patch level
     * @param source source
     * @param additionalProperties any other properties
     * @param extendedProperties properties for subtype
     *
     * @return id for the capability
     */
    public String addSoftwareCapability(String              typeName,
                                        String              qualifiedName,
                                        String              name,
                                        String              description,
                                        String              capabilityType,
                                        String              capabilityVersion,
                                        String              patchLevel,
                                        String              source,
                                        Map<String, String> additionalProperties,
                                        Map<String, Object> extendedProperties)
    {
        final String methodName = "addSoftwareCapability";

        String entityTypeName = OpenMetadataTypesMapper.SOFTWARE_CAPABILITY_TYPE_NAME;

        if (typeName != null)
        {
            entityTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CAPABILITY_TYPE_PROPERTY_NAME, capabilityType, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CAPABILITY_VERSION_PROPERTY_NAME, capabilityVersion, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PATCH_LEVEL_PROPERTY_NAME, patchLevel, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SOURCE_PROPERTY_NAME, source, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addPropertyMapToInstance(archiveRootName, properties, extendedProperties, methodName);

        EntityDetail entity = archiveHelper.getEntityDetail(entityTypeName,
                                                            idToGUIDMap.getGUID(qualifiedName),
                                                            properties,
                                                            InstanceStatus.ACTIVE,
                                                            null);

        archiveBuilder.addEntity(entity);

        return entity.getGUID();
    }


    /**
     * Create the relationship between an asset and its connection.
     *
     * @param assetGUID unique identifier of the asset
     * @param assetSummary summary of the asset from the connection perspective
     * @param connectionGUID unique identifier of the connection to its content
     */
    public void addConnectionForAsset(String assetGUID,
                                      String assetSummary,
                                      String connectionGUID)
    {
        final String methodName = "addConnectionForAsset";

        EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);
        EntityDetail connectionEntity = archiveBuilder.getEntity(connectionGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(connectionEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(assetEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.ASSET_SUMMARY_PROPERTY_NAME, assetSummary, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CONNECTION_TO_ASSET_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(assetGUID + "_to_" + connectionGUID + "_asset_connection_relationship"),
                                                                     properties,
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
        final String methodName = "addDataContentForDataSet";

        EntityDetail dataContentEntity = archiveBuilder.getEntity(dataContentGUID);
        EntityDetail dataSetEntity = archiveBuilder.getEntity(dataSetGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(dataContentEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(dataSetEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUERY_ID_PROPERTY_NAME, queryId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.QUERY_PROPERTY_NAME, query, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(dataContentGUID + "_to_" + dataSetGUID + "_data_content_for_data_set_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the top level schema type for an asset.
     *
     * @param assetGUID unique identifier of asset
     * @param typeName name of asset subtype to use - default is SchemaType
     * @param qualifiedName unique name for the schema type
     * @param displayName display name for the schema type
     * @param description description about the schema type
     * @param additionalProperties any other properties
     *
     * @return id for the schemaType
     */
    public String addTopLevelSchemaType(String              assetGUID,
                                        String              typeName,
                                        String              qualifiedName,
                                        String              displayName,
                                        String              description,
                                        Map<String, String> additionalProperties)
    {
        final String methodName = "addTopLevelSchemaType";

        String schemaTypeTypeName = OpenMetadataTypesMapper.SCHEMA_TYPE_TYPE_NAME;

        if (typeName != null)
        {
            schemaTypeTypeName = typeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(schemaTypeTypeName,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

        archiveBuilder.addEntity(schemaTypeEntity);

        if (assetGUID != null)
        {
            EntityDetail assetEntity = archiveBuilder.getEntity(assetGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(assetEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.ASSET_TO_SCHEMA_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_asset_schema_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return schemaTypeEntity.getGUID();
    }


    /**
     * Create the schema type for an API operation.
     *
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
    public String addAPIOperation(String              apiSchemaTypeGUID,
                                  String              qualifiedName,
                                  String              displayName,
                                  String              description,
                                  String              path,
                                  String              command,
                                  Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIOperation";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PATH_PROPERTY_NAME, path, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.COMMAND_PROPERTY_NAME, command, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail schemaTypeEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.API_OPERATION_TYPE_NAME,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

        archiveBuilder.addEntity(schemaTypeEntity);

        if (apiSchemaTypeGUID != null)
        {
            EntityDetail parentEntity = archiveBuilder.getEntity(apiSchemaTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(parentEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.API_OPERATIONS_RELATIONSHIP_TYPE_NAME,
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
    public String addAPIParameterList(String              apiOperationGUID,
                                      String              relationshipTypeName,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      boolean             required,
                                      Map<String, String> additionalProperties)
    {
        final String methodName = "addAPIParameterList";

        String typeName = OpenMetadataTypesMapper.API_REQUEST_RELATIONSHIP_TYPE_NAME;

        if (relationshipTypeName != null)
        {
            typeName = relationshipTypeName;
        }

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REQUIRED_PROPERTY_NAME, required, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail parameterListEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.API_PARAMETER_LIST_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         null);

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

        String schemaAttributeTypeName = OpenMetadataTypesMapper.API_PARAMETER_TYPE_NAME;

        if (typeName != null)
        {
            schemaAttributeTypeName = typeName;
        }

        String schemaTypeTypeName = OpenMetadataTypesMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME;

        if (schemaTypeName != null)
        {
            schemaTypeTypeName = schemaTypeName;
        }

        InstanceProperties entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        entityProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.POSITION_PROPERTY_NAME, position, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.PARAMETER_TYPE_PROPERTY_NAME, parameterType, methodName);
        entityProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.SCHEMA_TYPE_NAME_PROPERTY_NAME, schemaTypeTypeName, methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataTypesMapper.DATA_TYPE_PROPERTY_NAME, dataType, methodName);
        classificationProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataTypesMapper.LENGTH_PROPERTY_NAME, length, methodName);

        Classification classification = archiveHelper.getClassification(OpenMetadataTypesMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME, classificationProperties, InstanceStatus.ACTIVE);

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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME,
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
     * @param schemaAttributeGUID unique identifier of the child element
     */
    public void addAttributeForSchemaType(String schemaTypeGUID,
                                          String schemaAttributeGUID)
    {
        EntityDetail schemaTypeChoiceEntity = archiveBuilder.getEntity(schemaTypeGUID);
        EntityDetail schemaTypeOptionEntity = archiveBuilder.getEntity(schemaAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(schemaTypeChoiceEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(schemaTypeOptionEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(schemaTypeGUID + "_to_" + schemaAttributeGUID + "_attribute_for_schema_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Create the relationship between a SchemaAttribute element and a child SchemaAttribute element using the NestedSchemaAttribute relationship.
     *
     * @param parentSchemaAttributeGUID unique identifier of the parent element
     * @param childSchemaAttributeGUID unique identifier of the child element
     */
    public void addNestedSchemaAttribute(String parentSchemaAttributeGUID,
                                         String childSchemaAttributeGUID)
    {
        EntityDetail parentSchemaAttributeEntity = archiveBuilder.getEntity(parentSchemaAttributeGUID);
        EntityDetail childSchemaAttributeEntity = archiveBuilder.getEntity(childSchemaAttributeGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(parentSchemaAttributeEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(childSchemaAttributeEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(parentSchemaAttributeGUID + "_to_" + childSchemaAttributeGUID + "_nested_schema_attribute_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
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
     * @param additionalProperties any other properties.
     *
     * @return id for the schema attribute
     */
    public String addSchemaAttribute(String              typeName,
                                     String              schemaTypeName,
                                     String              qualifiedName,
                                     String              displayName,
                                     String              description,
                                     String              dataType,
                                     int                 length,
                                     int                 position,
                                     Map<String, String> additionalProperties)
    {
        final String methodName = "addSchemaAttribute";

        String schemaAttributeTypeName = OpenMetadataTypesMapper.SCHEMA_ATTRIBUTE_TYPE_NAME;

        if (typeName != null)
        {
            schemaAttributeTypeName = typeName;
        }

        String schemaTypeTypeName = OpenMetadataTypesMapper.PRIMITIVE_SCHEMA_TYPE_TYPE_NAME;

        if (schemaTypeName != null)
        {
            schemaTypeTypeName = schemaTypeName;
        }

        InstanceProperties entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        entityProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        entityProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.POSITION_PROPERTY_NAME, position, methodName);
        entityProperties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, entityProperties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.SCHEMA_TYPE_NAME_PROPERTY_NAME, schemaTypeTypeName, methodName);
        classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataTypesMapper.DATA_TYPE_PROPERTY_NAME, dataType, methodName);
        classificationProperties = archiveHelper.addIntPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataTypesMapper.LENGTH_PROPERTY_NAME, length, methodName);

        Classification classification = archiveHelper.getClassification(OpenMetadataTypesMapper.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME, classificationProperties, InstanceStatus.ACTIVE);

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
                                String              endpointGUID)
    {
        final String methodName = "addConnection";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.USER_ID_PROPERTY_NAME, userId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CLEAR_PASSWORD_PROPERTY_NAME, clearPassword, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ENCRYPTED_PASSWORD_PROPERTY_NAME, encryptedPassword, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SECURED_PROPERTIES_PROPERTY_NAME, securedProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONFIGURATION_PROPERTIES_PROPERTY_NAME, configurationProperties, methodName);

        EntityDetail connectionEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.CONNECTION_TYPE_NAME,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

        archiveBuilder.addEntity(connectionEntity);

        if (connectorTypeGUID != null)
        {
            EntityDetail connectorTypeEntity = archiveBuilder.getEntity(connectorTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectionEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connectorType_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (endpointGUID != null)
        {
            EntityDetail endpointEntity = archiveBuilder.getEntity(endpointGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(endpointEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectionEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CONNECTION_ENDPOINT_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_endpoint_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return connectionEntity.getGUID();
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
    protected String addConnectorType(String              connectorCategoryGUID,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
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
        final String methodName = "addConnectorType";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SUPPORTED_ASSET_TYPE_PROPERTY_NAME, supportedAssetTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.EXPECTED_DATA_FORMAT_PROPERTY_NAME, expectedDataFormat, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONNECTOR_PROVIDER_PROPERTY_NAME, connectorProviderClassName, methodName);
        if (connectorFrameworkName != null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONNECTOR_FRAMEWORK_PROPERTY_NAME, connectorFrameworkName, methodName);
        }
        else
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONNECTOR_FRAMEWORK_PROPERTY_NAME, OpenMetadataValidValues.CONNECTOR_FRAMEWORK_DEFAULT, methodName);
        }
        if (connectorInterfaceLanguage != null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONNECTOR_INTERFACE_LANGUAGE_PROPERTY_NAME, connectorInterfaceLanguage, methodName);
        }
        else
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONNECTOR_INTERFACE_LANGUAGE_PROPERTY_NAME, OpenMetadataValidValues.CONNECTOR_INTERFACE_LANGUAGE_DEFAULT, methodName);
        }
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONNECTOR_INTERFACES_PROPERTY_NAME, connectorInterfaces, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TARGET_TECHNOLOGY_SOURCE_PROPERTY_NAME, targetTechnologySource, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TARGET_TECHNOLOGY_NAME_PROPERTY_NAME, targetTechnologyName, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TARGET_TECHNOLOGY_INTERFACES_PROPERTY_NAME, targetTechnologyInterfaces, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TARGET_TECHNOLOGY_VERSIONS_PROPERTY_NAME, targetTechnologyVersions, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.RECOGNIZED_SECURED_PROPERTIES_PROPERTY_NAME, recognizedSecuredProperties, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.RECOGNIZED_ADDITIONAL_PROPERTIES_PROPERTY_NAME, recognizedAdditionalProperties, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.RECOGNIZED_CONFIGURATION_PROPERTIES_PROPERTY_NAME, recognizedConfigurationProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail connectorTypeEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.CONNECTOR_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         null);

        archiveBuilder.addEntity(connectorTypeEntity);

        if (connectorCategoryGUID != null)
        {
            EntityDetail connectorCategoryEntity = archiveBuilder.getEntity(connectorCategoryGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectorCategoryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CONNECTOR_IMPLEMENTATION_CHOICE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connector_category_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TARGET_TECHNOLOGY_SOURCE_PROPERTY_NAME, targetTechnologySource, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.TARGET_TECHNOLOGY_NAME_PROPERTY_NAME, targetTechnologyName, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.RECOGNIZED_SECURED_PROPERTIES_PROPERTY_NAME, recognizedSecuredProperties, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.RECOGNIZED_ADDITIONAL_PROPERTIES_PROPERTY_NAME, recognizedAdditionalProperties, methodName);
        properties = archiveHelper.addBooleanMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.RECOGNIZED_CONFIGURATION_PROPERTIES_PROPERTY_NAME, recognizedConfigurationProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail connectorCategoryEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.CONNECTOR_CATEGORY_TYPE_NAME,
                                                                             idToGUIDMap.getGUID(qualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             null);

        archiveBuilder.addEntity(connectorCategoryEntity);

        if (connectorTypeDirectoryGUID != null)
        {
            EntityDetail connectorTypeDirectoryEntity = archiveBuilder.getEntity(connectorTypeDirectoryGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectorTypeDirectoryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorCategoryEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.COLLECTION_MEMBERSHIP_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connector_type_directory_relationship"),
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        Classification classification = archiveHelper.getClassification(OpenMetadataTypesMapper.CONNECTOR_TYPE_DIRECTORY_TYPE_NAME, null, InstanceStatus.ACTIVE);
        List<Classification> classifications = new ArrayList<>();

        classifications.add(classification);

        EntityDetail connectorTypeDirectoryEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.COLLECTION_TYPE_NAME,
                                                                                  idToGUIDMap.getGUID(qualifiedName),
                                                                                  properties,
                                                                                  InstanceStatus.ACTIVE,
                                                                                  classifications);

        archiveBuilder.addEntity(connectorTypeDirectoryEntity);

        return connectorTypeDirectoryEntity.getGUID();
    }


    /**
     * Create a endpoint entity.
     *
     * @param qualifiedName unique name for the endpoint
     * @param displayName display name for the endpoint
     * @param description description about the endpoint
     * @param networkAddress location of the asset
     * @param protocol protocol to use to connect to the asset
     * @param additionalProperties any other properties.
     *
     * @return id for the endpoint
     */
    public String addEndpoint(String              qualifiedName,
                              String              displayName,
                              String              description,
                              String              networkAddress,
                              String              protocol,
                              Map<String, String> additionalProperties)
    {
        final String methodName = "addEndpoint";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NETWORK_ADDRESS_PROPERTY_NAME, networkAddress, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PROTOCOL_PROPERTY_NAME, protocol, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        EntityDetail endpointEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.ENDPOINT_TYPE_NAME,
                                                                    idToGUIDMap.getGUID(qualifiedName),
                                                                    properties,
                                                                    InstanceStatus.ACTIVE,
                                                                    null);

        archiveBuilder.addEntity(endpointEntity);

        return endpointEntity.getGUID();
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.LANGUAGE_PROPERTY_NAME, language, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.USAGE_PROPERTY_NAME, usage, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        List<Classification> classifications = null;

        if (scope != null)
        {
            Classification  canonicalVocabClassification = archiveHelper.getClassification(OpenMetadataTypesMapper.CANONICAL_VOCAB_CLASSIFICATION_TYPE_NAME,
                                                                                           archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                                     null,
                                                                                                                                     OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME,
                                                                                                                                     scope,
                                                                                                                                     methodName),
                                                                                           InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(canonicalVocabClassification);
        }

        EntityDetail  glossaryEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.GLOSSARY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(glossaryEntity);

        if (externalLink != null)
        {
            String externalLinkQualifiedName = qualifiedName + "_external_link";
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, externalLinkQualifiedName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.URL_PROPERTY_NAME, externalLink, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ORGANIZATION_PROPERTY_NAME, originatorName, methodName);
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.REFERENCE_VERSION_PROPERTY_NAME, versionName, methodName);

            EntityDetail  externalLinkEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.EXTERNAL_GLOSSARY_LINK_TYPE_NAME,
                                                                             idToGUIDMap.getGUID(externalLinkQualifiedName),
                                                                             properties,
                                                                             InstanceStatus.ACTIVE,
                                                                             classifications);

            archiveBuilder.addEntity(externalLinkEntity);

            EntityProxy end1 = archiveHelper.getEntityProxy(glossaryEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(externalLinkEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.EXTERNALLY_SOURCED_GLOSSARY_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_externally_sourced_link_relationship"),
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
        return addGlossaryCategory(glossaryGUID, false, qualifiedName, displayName, description, subjectArea, null);
    }


    /**
     * Add a glossary category to the archive and connect it to its glossary.
     *
     * @param glossaryGUID identifier of the glossary.
     * @param qualifiedName unique name for the category.
     * @param displayName display name for the category.
     * @param description description of the category.
     * @param subjectArea name of the subject area if this category contains terms for the subject area.
     * @param additionalProperties any other properties.
     *
     * @return identifier of the category
     */
    public String addGlossaryCategory(String              glossaryGUID,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              subjectArea,
                                      Map<String, String> additionalProperties)
    {
        return addGlossaryCategory(glossaryGUID, false, qualifiedName, displayName, description, subjectArea, additionalProperties);

    }


    /**
     * Add a glossary category to the archive and connect it to its glossary.
     *
     * @param glossaryGUID identifier of the glossary.
     * @param isRootCategory is this the top-level category for the glossary
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
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              subjectArea,
                                      Map<String, String> additionalProperties)
    {
        final String methodName = "addCategory";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        List<Classification> classifications = new ArrayList<>();

        if (subjectArea != null)
        {
            Classification  subjectAreaClassification = archiveHelper.getClassification(OpenMetadataTypesMapper.SUBJECT_AREA_CLASSIFICATION_TYPE_NAME,
                                                                                        archiveHelper.addStringPropertyToInstance(archiveRootName,
                                                                                                                                  null,
                                                                                                                                  OpenMetadataTypesMapper.NAME_PROPERTY_NAME,
                                                                                                                                  subjectArea,
                                                                                                                                  methodName),
                                                                                        InstanceStatus.ACTIVE);

            classifications.add(subjectAreaClassification);
        }

        if (isRootCategory)
        {
            Classification  rootCategoryClassification = archiveHelper.getClassification(OpenMetadataTypesMapper.ROOT_CATEGORY_CLASSIFICATION_TYPE_NAME,
                                                                                         null,
                                                                                         InstanceStatus.ACTIVE);

            classifications.add(rootCategoryClassification);
        }


        if (classifications.isEmpty())
        {
            classifications = null;
        }

        EntityDetail  categoryEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.GLOSSARY_CATEGORY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     classifications);

        archiveBuilder.addEntity(categoryEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(categoryEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CATEGORY_ANCHOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_category_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

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
        return addTerm(glossaryGUID, categoryGUIDs, false, qualifiedName, displayName, null, description, null, null,null, false, false, false, null, null, null);
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
     * @param isSpineObject term is a spine object
     * @param isSpineAttribute term is a spine attribute
     * @param isContext is this term a context definition?
     * @param contextDescription description to add to the ContextDefinition classification
     * @param contextScope scope to add to the context classification
     * @param additionalProperties any other properties.
     *
     * @return unique identifier of the term
     */
    public String addTerm(String              glossaryGUID,
                          List<String>        categoryIds,
                          boolean             categoriesAsNames,
                          String              qualifiedName,
                          String              displayName,
                          String              summary,
                          String              description,
                          String              examples,
                          String              abbreviation,
                          String              usage,
                          boolean             isSpineObject,
                          boolean             isSpineAttribute,
                          boolean             isContext,
                          String              contextDescription,
                          String              contextScope,
                          Map<String, String> additionalProperties)
    {
        final String methodName = "addTerm";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SUMMARY_PROPERTY_NAME, summary, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.EXAMPLES_PROPERTY_NAME, examples, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ABBREVIATION_PROPERTY_NAME, abbreviation, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.USAGE_PROPERTY_NAME, usage, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);

        if (examples !=null)
        {
            properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.EXAMPLES_PROPERTY_NAME, examples, methodName);
        }

        List<Classification> classifications = null;

        if (isSpineObject)
        {
            Classification  newClassification = archiveHelper.getClassification(OpenMetadataTypesMapper.SPINE_OBJECT_CLASSIFICATION_TYPE_NAME,
                                                                                null,
                                                                                InstanceStatus.ACTIVE);

            classifications = new ArrayList<>();
            classifications.add(newClassification);
        }

        if (isSpineAttribute)
        {
            Classification newClassification = archiveHelper.getClassification(OpenMetadataTypesMapper.SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_NAME,
                                                                               null,
                                                                               InstanceStatus.ACTIVE);

            if (classifications == null)
            {
                classifications = new ArrayList<>();
            }

            classifications.add(newClassification);
        }

        if (isContext)
        {
            InstanceProperties classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, contextDescription, methodName);
            classificationProperties = archiveHelper.addStringPropertyToInstance(archiveRootName, classificationProperties, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, contextScope, methodName);

            Classification  newClassification = archiveHelper.getClassification(OpenMetadataTypesMapper.CONTEXT_DEFINITION_CLASSIFICATION_TYPE_NAME,
                                                                                classificationProperties,
                                                                                InstanceStatus.ACTIVE);

            if (classifications == null)
            {
                classifications = new ArrayList<>();
            }

            classifications.add(newClassification);
        }

        EntityDetail  termEntity = archiveHelper.getEntityDetail(OpenMetadataTypesMapper.GLOSSARY_TERM_TYPE_NAME,
                                                                 idToGUIDMap.getGUID(qualifiedName),
                                                                 properties,
                                                                 InstanceStatus.ACTIVE,
                                                                 classifications);

        archiveBuilder.addEntity(termEntity);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(glossaryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(termEntity);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TERM_ANCHOR_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(qualifiedName + "_term_anchor_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));

        if (categoryIds != null)
        {
            InstanceProperties categorizationProperties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.STATUS_PROPERTY_NAME, activeStatus.getOrdinal(), activeStatus.getValue(), activeStatus.getDescription(), methodName);

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
                    archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TERM_CATEGORIZATION_TYPE_NAME,
                                                                                 idToGUIDMap.getGUID(qualifiedName + "_category_" + categoryId + "_term_categorization_relationship"),
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CATEGORY_HIERARCHY_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(parentCategoryGUID + "_to_" + childCategoryGUID + "_category_hierarchy_relationship"),
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

        EnumElementDef termStatus = archiveHelper.getEnumElement(OpenMetadataTypesMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME, status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.STATUS_PROPERTY_NAME, termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.USED_IN_CONTEXT_RELATIONSHIP_NAME,
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

        EnumElementDef termStatus = archiveHelper.getEnumElement(OpenMetadataTypesMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME, status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.STATUS_PROPERTY_NAME, termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.EXPRESSION_PROPERTY_NAME, expression, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SOURCE_PROPERTY_NAME, source, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.SYNONYM_RELATIONSHIP_NAME,
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

        EnumElementDef termStatus = archiveHelper.getEnumElement(OpenMetadataTypesMapper.TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME, status);

        InstanceProperties properties = archiveHelper.addEnumPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.STATUS_PROPERTY_NAME, termStatus.getOrdinal(), termStatus.getValue(), termStatus.getDescription(), methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TERM_CATEGORIZATION_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(categoryGUID + "_to_" + termGUID + "_term_categorization_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Link a category to an external glossary link with information on which category in the external glossary it corresponds to.
     *
     * @param categoryGUID unique identifier for the category
     * @param externalGlossaryLinkGUID unique identifier for the description of the external glossary (a type of external reference)
     * @param identifier identifier of the category in the external glossary
     * @param description description of the link
     * @param steward steward who created link
     * @param lastVerified last time this was verified
     */
    public void addLibraryCategoryReference(String categoryGUID,
                                            String externalGlossaryLinkGUID,
                                            String identifier,
                                            String description,
                                            String steward,
                                            Date   lastVerified)
    {
        final String methodName = "addLibraryCategoryReference";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(categoryGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(externalGlossaryLinkGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME, steward, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.LAST_VERIFIED_PROPERTY_NAME, lastVerified, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.LIBRARY_CATEGORY_REFERENCE_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(categoryGUID + "_to_" + externalGlossaryLinkGUID + "_library_category_reference_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }




    /**
     * Link a glossary term to an external glossary link with information on which term in the external glossary it corresponds to.
     *
     * @param termGUID unique identifier for the term
     * @param externalGlossaryLinkGUID unique identifier for the description of the external glossary (a type of external reference)
     * @param identifier identifier of the term in the external glossary
     * @param description description of the link
     * @param steward steward who created link
     * @param lastVerified last time this was verified
     */
    public void addLibraryTermReference(String termGUID,
                                        String externalGlossaryLinkGUID,
                                        String identifier,
                                        String description,
                                        String steward,
                                        Date   lastVerified)
    {
        final String methodName = "addLibraryTermReference";

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(termGUID));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(externalGlossaryLinkGUID));

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.IDENTIFIER_PROPERTY_NAME, identifier, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME, steward, methodName);
        properties = archiveHelper.addDatePropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.LAST_VERIFIED_PROPERTY_NAME, lastVerified, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.LIBRARY_TERM_REFERENCE_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(termGUID + "_to_" + externalGlossaryLinkGUID + "_library_term_reference_relationship"),
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.REFERENCEABLE_TO_MORE_INFO_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(describedElementId + "_to_" + describerElementId + "_more_information_relationship"),
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.REFERENCEABLE_TO_MEANING_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(referenceableId + "_to_" + termId + "_semantic_assignment_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    /**
     * Add an is-a-type-of relationship
     *
     * @param specialTermQName qualified name of the specialized term
     * @param generalizedTermQName qualified name of the generalized term
     */
    public void addIsATypeOfRelationship(String specialTermQName , String generalizedTermQName)
    {

        String specializedTermId = idToGUIDMap.getGUID(specialTermQName);
        String generalizedTermId = idToGUIDMap.getGUID(generalizedTermQName);
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(specializedTermId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(generalizedTermId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(specializedTermId + "_to_" + generalizedTermId + "_isatypeof_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }

    public void addHasARelationship(String conceptQName, String propertyQName)
    {
        String conceptId = idToGUIDMap.getGUID(conceptQName);
        String propertyId = idToGUIDMap.getGUID(propertyQName);
        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(propertyId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(conceptId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.TERM_HAS_A_RELATIONSHIP_NAME,
                                                                     idToGUIDMap.getGUID(conceptId + "_to_" + propertyId + "_hasa_relationship"),
                                                                     null,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }


    public void addRelatedTermRelationship(String conceptQName, String propertyQName)
    {
        String conceptId = idToGUIDMap.getGUID(conceptQName);
        String propertyId = idToGUIDMap.getGUID(propertyQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(conceptId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(propertyId));

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.RELATED_TERM_RELATIONSHIP_NAME,
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
     * @param isDeprecated is value active
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
                                boolean             isDeprecated,
                                Map<String, String> additionalProperties)
    {
        return this.addValidValue(typeName, qualifiedName, name, description, null, usage, scope, preferredValue, isDeprecated, false, additionalProperties);
    }


    /**
     * Add a valid value definition/set.
     *
     * @param typeName unique name of the valid value - ie a definition or a set
     * @param qualifiedName unique name of the valid value
     * @param name display name of the valid value
     * @param category category of the valid value
     * @param scope short description of the valid value
     * @param description description of the valid value
     * @param preferredValue preferredValue of the valid value
     * @param usage how is the valid value used
     * @param isDeprecated is value active
     * @param isCaseSensitive is value case sensitive
     * @param additionalProperties any other properties.
     *
     * @return unique identifier of the valid value
     */
    public String addValidValue(String              typeName,
                                String              qualifiedName,
                                String              name,
                                String              description,
                                String              category,
                                String              usage,
                                String              scope,
                                String              preferredValue,
                                boolean             isDeprecated,
                                boolean             isCaseSensitive,
                                Map<String, String> additionalProperties)
    {
        final String methodName = "addValidValue";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NAME_PROPERTY_NAME, name, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.DESCRIPTION_PROPERTY_NAME, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CATEGORY_PROPERTY_NAME, category, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.USAGE_PROPERTY_NAME, usage, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SCOPE_PROPERTY_NAME, scope, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.PREFERRED_VALUE_PROPERTY_NAME, preferredValue, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IS_DEPRECATED_PROPERTY_NAME, isDeprecated, methodName);
        properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IS_CASE_SENSITIVE_PROPERTY_NAME, isCaseSensitive, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, additionalProperties, methodName);


        EntityDetail  validValueEntity = archiveHelper.getEntityDetail(typeName,
                                                                       idToGUIDMap.getGUID(qualifiedName),
                                                                       properties,
                                                                       InstanceStatus.ACTIVE,
                                                                       null);

        archiveBuilder.addEntity(validValueEntity);

        return validValueEntity.getGUID();
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
        final String methodName = "addValidValuesAssignmentRelationship";

        String setId = idToGUIDMap.getGUID(setQName);
        String memberId = idToGUIDMap.getGUID(memberQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(setId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(memberId));

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.IS_DEFAULT_VALUE_PROPERTY_NAME, isDefaultValue, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.VALID_VALUES_MEMBER_RELATIONSHIP_TYPE_NAME,
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

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.CONSISTENT_VALID_VALUES_RELATIONSHIP_TYPE_NAME,
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

        InstanceProperties properties = archiveHelper.addBooleanPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.IS_STRICT_REQUIREMENT_PROPERTY_NAME, strictRequirement, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.VALID_VALUES_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.ASSOCIATION_DESCRIPTION_PROPERTY_NAME, associationDescription, methodName);
        properties = archiveHelper.addIntPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.CONFIDENCE_PROPERTY_NAME, confidence, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_TYPE_NAME_PROPERTY_NAME, stewardTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME_PROPERTY_NAME, stewardPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NOTES_PROPERTY_NAME, notes, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.VALID_VALUES_MAP_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(matchingValue1Id + "_to_" + matchingValueId + "_valid_values_mapping_relationship"),
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
        final String methodName = "addReferenceValueAssignmentRelationship";

        String referenceableId = idToGUIDMap.getGUID(referenceableQName);
        String validValueId = idToGUIDMap.getGUID(validValueQName);

        EntityProxy end1 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(referenceableId));
        EntityProxy end2 = archiveHelper.getEntityProxy(archiveBuilder.getEntity(validValueId));

        InstanceProperties properties = archiveHelper.addIntPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.CONFIDENCE_PROPERTY_NAME, confidence, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME, steward, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_TYPE_NAME_PROPERTY_NAME, stewardTypeName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.STEWARD_PROPERTY_NAME_PROPERTY_NAME, stewardPropertyName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.NOTES_PROPERTY_NAME, notes, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(referenceableId + "_to_" + validValueId + "_reference_value_assignment_relationship"),
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

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.SYMBOLIC_NAME_PROPERTY_NAME, symbolicName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.IMPLEMENTATION_VALUE_PROPERTY_NAME, implementationValue, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ADDITIONAL_VALUES_PROPERTY_NAME, additionalValues, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataTypesMapper.VALID_VALUES_IMPL_RELATIONSHIP_TYPE_NAME,
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

        Classification  classification = archiveHelper.getClassification(OpenMetadataTypesMapper.REFERENCE_DATA_CLASSIFICATION_TYPE_NAME,
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

        InstanceProperties properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, null, OpenMetadataTypesMapper.SECURITY_LABELS_PROPERTY_NAME, securityLabels, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.SECURITY_PROPERTIES_PROPERTY_NAME, securityProperties, methodName);
        properties = archiveHelper.addStringArrayStringMapPropertyToInstance(archiveRootName, properties, OpenMetadataTypesMapper.ACCESS_GROUPS_PROPERTY_NAME, accessGroups, methodName);

        Classification classification = archiveHelper.getClassification(OpenMetadataTypesMapper.SECURITY_TAGS_CLASSIFICATION_TYPE_NAME,
                                                                        properties,
                                                                        InstanceStatus.ACTIVE);

        archiveBuilder.addClassification(archiveHelper.getClassificationEntityExtension(entityProxy, classification));
    }
}
