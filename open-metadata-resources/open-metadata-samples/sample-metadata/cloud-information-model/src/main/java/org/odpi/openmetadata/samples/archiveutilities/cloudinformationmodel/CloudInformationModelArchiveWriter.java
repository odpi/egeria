/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel;

import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.*;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;

import java.util.*;

/**
 * CloudInformationModelArchiveWriter creates a physical open metadata archive file for the data model and glossary
 * content found in the Cloud Information Model (CIM).
 */
public class CloudInformationModelArchiveWriter extends OMRSArchiveWriter
{
    static final String defaultOpenMetadataArchiveFileName = "content-packs/CloudInformationModel.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9dc75637-92a7-4926-b47b-a3d407546f89";
    private static final String                  archiveName        = "CloudInformationModel";
    private static final String                  archiveLicense     = "SPDX-License-Identifier: Apache-2.0";
    private static final String                  archiveDescription = "Cloud Information Model (CIM) glossary and concept model.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "The Cloud Information Model";
    private static final Date                    creationDate       = new Date(1570383385107L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";

    private final SimpleCatalogArchiveHelper archiveHelper;
    private final OMRSArchiveBuilder         archiveBuilder;

    private final CloudInformationModelParser parser;

    /**
     * Default constructor
     *
     * @param cimModelLocation directory name for the CIM model's JSON-LD files.
     */
    CloudInformationModelArchiveWriter(String cimModelLocation)
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the CIM to be based on the existing open metadata types
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new SimpleCatalogArchiveHelper(archiveBuilder,
                                                            archiveGUID,
                                                            archiveName,
                                                            archiveDescription,
                                                            originatorName,
                                                            creationDate,
                                                            versionNumber,
                                                            versionName,
                                                            archiveName + "GUIDMap.json");

        this.parser = new CloudInformationModelParser(cimModelLocation);
    }


    /**
     * Returns the open metadata type archive containing all the elements extracted from the CIM.
     *
     * @return populated open metadata archive object
     */
    private OpenMetadataArchive getOpenMetadataArchive()
    {
        final String methodName = "getOpenMetadataArchive";

        if (parser != null)
        {
            Model model = parser.getModel();

            String  dataDictionaryId = archiveHelper.addCollection(null,
                                                                   null,
                                                                   OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName,
                                                                   OpenMetadataType.DATA_DICTIONARY_COLLECTION.typeName,
                                                                   null,
                                                                   null,
                                                                   "DataDictionary::" + model.getModelName(),
                                                                   model.getModelName(),
                                                                   model.getModelSummary(),
                                                                   null,
                                                                   null,
                                                                   null,
                                                                   null);

            String  glossaryId = archiveHelper.addGlossary("Glossary::" + model.getModelTechnicalName(),
                                                           model.getModelName(),
                                                           model.getModelSummary(),
                                                           model.getModelLanguage(),
                                                           archiveDescription,
                                                           model.getModelLocation(),
                                                           model.getModelScope());

            /*
             * Create a top level term for the model.
             */
            String modelTermId = archiveHelper.addTerm(glossaryId,
                                                       null,
                                                       false,
                                                       "GlossaryTerm::CIMDescription-" + model.getModelTechnicalName(),
                                                       model.getModelName(),
                                                       model.getModelSummary(),
                                                       model.getModelDescription(),
                                                       null,
                                                       null,
                                                       model.getModelUsage(),
                                                       null,
                                                       false,
                                                       null,
                                                       null,
                                                       null,
                                                       null);

            archiveHelper.addMoreInformationLink(dataDictionaryId, modelTermId);

            /*
             * The subject are structure builds out the data dictionary.
             * Create a top level folder to hold the subject areas.
             */
            String topLevelSubjectAreaFolderId = archiveHelper.addCollection(null,
                                                                             dataDictionaryId,
                                                                             OpenMetadataType.COLLECTION_FOLDER.typeName,
                                                                             OpenMetadataType.COLLECTION.typeName,
                                                                             null,
                                                                             null,
                                                                             "Folder::ModelSubjectAreas-" + model.getModelTechnicalName(),
                                                                             "Subject Areas for the " + model.getModelName() + " model",
                                                                             "Collections of related concepts (entities and relationships) found in the CIM Model that describe an area of interest.",
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             null);

            archiveHelper.addMemberToCollection(dataDictionaryId, topLevelSubjectAreaFolderId, null);

            Map<String, SubjectArea> subjectAreaMap = model.getSubjectAreaMap();

            if (subjectAreaMap != null)
            {
                /*
                 * Establish nested model structure and data fields
                 */
                for (SubjectArea subjectArea : subjectAreaMap.values())
                {
                    String subjectAreaFolderId = archiveHelper.addCollection(null,
                                                                             null,
                                                                             OpenMetadataType.COLLECTION_FOLDER.typeName,
                                                                             OpenMetadataType.COLLECTION.typeName,
                                                                             null,
                                                                             null,
                                                                             "Folder::ModelSubjectAreas-" + model.getModelTechnicalName() + "::" + subjectArea.getTechnicalName(),
                                                                             subjectArea.getDisplayName(),
                                                                             subjectArea.getDescription(),
                                                                             null,
                                                                             null,
                                                                             null,
                                                                             null);

                    archiveHelper.addMemberToCollection(topLevelSubjectAreaFolderId, subjectAreaFolderId, null);

                    Map<String, ConceptGroup> conceptGroupMap = subjectArea.getConceptGroups();
                    List<String> existingProperties = new ArrayList<>();

                    if (conceptGroupMap != null)
                    {
                        for (ConceptGroup conceptGroup : conceptGroupMap.values())
                        {
                            if (conceptGroup != null)
                            {
                                String conceptGroupCategoryId = archiveHelper.addCollection(null,
                                                                                            null,
                                                                                            OpenMetadataType.COLLECTION_FOLDER.typeName,
                                                                                            OpenMetadataType.COLLECTION.typeName,
                                                                                            null,
                                                                                            null,
                                                                                            "Folder::ConceptGroup::" + conceptGroup.getGUID() + "::" + conceptGroup.getTechnicalName(),
                                                                                            conceptGroup.getDisplayName(),
                                                                                            conceptGroup.getDescription(),
                                                                                            null,
                                                                                            null,
                                                                                            null,
                                                                                            null);

                                archiveHelper.addMemberToCollection(subjectAreaFolderId, conceptGroupCategoryId, null);

                                List<Concept> concepts = conceptGroup.getConcepts();

                                if (concepts != null)
                                {
                                    for (Concept concept : concepts)
                                    {
                                        if (concept != null)
                                        {
                                            String conceptQualifiedName = "DataField::SubjectArea::" + subjectArea.getTechnicalName() + "::" + concept.getTechnicalName();
                                            archiveHelper.setGUID(conceptQualifiedName, concept.getGUID());

                                            String conceptFieldId = archiveHelper.addDataField(null,
                                                                                               null,
                                                                                               OpenMetadataType.DATA_FIELD.typeName,
                                                                                               OpenMetadataType.DATA_FIELD.typeName,
                                                                                               null,
                                                                                               null,
                                                                                               conceptQualifiedName,
                                                                                               concept.getDisplayName(),
                                                                                               concept.getDescription(),
                                                                                               model.getModelVersion(),
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               null,
                                                                                               null);

                                            assert(conceptFieldId.equals(concept.getGUID()));

                                            archiveHelper.addMemberToCollection(conceptGroupCategoryId, concept.getGUID(), null);

                                            if (concept.getAttributes() != null)
                                            {
                                                for (Attribute attribute : concept.getAttributes())
                                                {
                                                    if (! existingProperties.contains(attribute.getGUID()))
                                                    {
                                                        String attributeQualifiedName = "DataField::" + model.getModelTechnicalName() + "::" + attribute.getTechnicalName();
                                                        archiveHelper.setGUID(attributeQualifiedName, attribute.getGUID());

                                                        String dataFieldId = archiveHelper.addDataField(null,
                                                                                                        null,
                                                                                                        OpenMetadataType.DATA_FIELD.typeName,
                                                                                                        OpenMetadataType.DATA_FIELD.typeName,
                                                                                                        null,
                                                                                                        null,
                                                                                                        attributeQualifiedName,
                                                                                                        attribute.getDisplayName(),
                                                                                                        attribute.getDescription(),
                                                                                                        model.getModelVersion(),
                                                                                                        null,
                                                                                                        attribute.getDataType(),
                                                                                                        null,
                                                                                                        null,
                                                                                                        null);

                                                        assert(dataFieldId.equals(attribute.getGUID()));

                                                        existingProperties.add(attribute.getGUID());
                                                    }

                                                    archiveHelper.addNestedDataField(concept.getGUID(), attribute.getGUID(), 0, 1, 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        for (ConceptGroup conceptGroup : conceptGroupMap.values())
                        {
                            if (conceptGroup != null)
                            {
                                List<Concept> concepts = conceptGroup.getConcepts();

                                if (concepts != null)
                                {
                                    for (Concept concept : concepts)
                                    {
                                        if (concept != null)
                                        {
                                            if (concept.getDomainOfLinks() != null)
                                            {
                                                for (Link link : concept.getDomainOfLinks())
                                                {
                                                    if (link instanceof LinkChoice)
                                                    {
                                                        for (Link linkOption : ((LinkChoice) link).getLinkChoices())
                                                        {
                                                            String attributeQualifiedName = "DataField::" + model.getModelTechnicalName() + "::" + linkOption.getTechnicalName();

                                                            linkOption.setGUID(getDataFieldGUID(archiveHelper.queryGUID(attributeQualifiedName),
                                                                                                linkOption.getGUID(),
                                                                                                attributeQualifiedName));

                                                            if (this.isNewDataField(linkOption.getGUID(), existingProperties))
                                                            {
                                                                String dataFieldId = archiveHelper.addDataField(null,
                                                                                                                null,
                                                                                                                OpenMetadataType.DATA_FIELD.typeName,
                                                                                                                OpenMetadataType.DATA_FIELD.typeName,
                                                                                                                null,
                                                                                                                null,
                                                                                                                attributeQualifiedName,
                                                                                                                link.getDisplayName(),
                                                                                                                link.getDescription(),
                                                                                                                model.getModelVersion(),
                                                                                                                null,
                                                                                                                link.getRangeConceptName(),
                                                                                                                null,
                                                                                                                null,
                                                                                                                null);

                                                                assert(linkOption.getGUID().equals(dataFieldId));

                                                                existingProperties.add(linkOption.getGUID());
                                                            }

                                                            archiveHelper.addNestedDataField(concept.getGUID(), linkOption.getGUID(), 0, 1, 1);
                                                        }
                                                    }
                                                    else
                                                    {
                                                        String attributeQualifiedName = "DataField::" + model.getModelTechnicalName() + "::" + link.getTechnicalName();

                                                        link.setGUID(getDataFieldGUID(archiveHelper.queryGUID(attributeQualifiedName),
                                                                                      link.getGUID(),
                                                                                      attributeQualifiedName));

                                                        if (this.isNewDataField(link.getGUID(), existingProperties))
                                                        {
                                                            String dataFieldId = archiveHelper.addDataField(null,
                                                                                                            null,
                                                                                                            OpenMetadataType.DATA_FIELD.typeName,
                                                                                                            OpenMetadataType.DATA_FIELD.typeName,
                                                                                                            null,
                                                                                                            null,
                                                                                                            attributeQualifiedName,
                                                                                                            link.getDisplayName(),
                                                                                                            link.getDescription(),
                                                                                                            model.getModelVersion(),
                                                                                                            null,
                                                                                                            link.getRangeConceptName(),
                                                                                                            null,
                                                                                                            null,
                                                                                                            null);

                                                            assert(link.getGUID().equals(dataFieldId));

                                                            existingProperties.add(link.getGUID());
                                                        }
                                                    }

                                                    archiveHelper.addNestedDataField(concept.getGUID(), link.getGUID(), 0, 1, 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                /*
                 * Add linkages - todo need to divided up this archive

                for (SubjectArea subjectArea : subjectAreaMap.values())
                {
                    Map<String, ConceptGroup> conceptGroupMap = subjectArea.getConceptGroups();

                    if (conceptGroupMap != null)
                    {
                        for (ConceptGroup conceptGroup : conceptGroupMap.values())
                        {
                            if (conceptGroup != null)
                            {
                                List<Concept> concepts = conceptGroup.getConcepts();

                                if (concepts != null)
                                {
                                    for (Concept concept : concepts)
                                    {
                                        if (concept != null)
                                        {
                                            if (concept.getDomainOfLinks() != null)
                                            {
                                                for (Link link : concept.getDomainOfLinks())
                                                {
                                                    if (link instanceof LinkChoice linkChoice)
                                                    {
                                                        for (Link linkOption : linkChoice.getLinkChoices())
                                                        {
                                                            archiveHelper.addLinkedDataField(linkOption.getGUID(), linkOption.getRangeConceptGUID(), OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName);
                                                        }
                                                    }
                                                }
                                            }

                                            if (concept.getRangeOfLinks() != null)
                                            {
                                                for (Link link : concept.getRangeOfLinks())
                                                {
                                                    if (link instanceof LinkChoice linkChoice)
                                                    {
                                                        for (Link linkOption : linkChoice.getLinkChoices())
                                                        {
                                                            archiveHelper.addLinkedDataField(linkOption.getDomainConceptGUID(), linkOption.getRangeConceptGUID(), link.getTechnicalName());
                                                        }
                                                    }
                                                    else
                                                    {
                                                        archiveHelper.addLinkedDataField(link.getDomainConceptGUID(), link.getRangeConceptGUID(), link.getTechnicalName());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // todo export model content
                }

                 */
            }

            /*
             * Build out the glossary using the property groups and descriptions
             * Create the top level category to hold the property groups
             */
            String propertyGroupsCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                                true,
                                                                                null,
                                                                                "GlossaryCategory::PropertyGroups-" + model.getModelTechnicalName(),
                                                                                "Property Groups for the " + model.getModelName(),
                                                                                "Collections of properties found in the CIM Model.",
                                                                                null);

            Map<String, PropertyGroup> propertyGroupMap = model.getPropertyGroupMap();

            if (propertyGroupMap != null)
            {
                for (PropertyGroup propertyGroup : propertyGroupMap.values())
                {
                    /*
                     * Each property group has a category under the main property groups category.  It is also a
                     * folder in the data dictionary.
                     * Then each property description within the property group is a term linked to its property
                     * group category as long as it has a description.  Each property description term is
                     * linked to the data fields representing each of the attributes linked to the property description.
                     */
                    String propertyGroupCategoryId = archiveHelper.addGlossaryCategory(glossaryId,
                                                                                       false,
                                                                                       propertyGroupsCategoryId,
                                                                                       "GlossaryCategory::" + propertyGroup.getGUID() + "::" + propertyGroup.getTechnicalName(),
                                                                                       propertyGroup.getDisplayName(),
                                                                                       propertyGroup.getDescription(),
                                                                                       null);

                    archiveHelper.addCategoryToCategory(propertyGroupsCategoryId, propertyGroupCategoryId);

                    String propertyGroupFolderId = archiveHelper.addCollection(null,
                                                                               dataDictionaryId,
                                                                               OpenMetadataType.COLLECTION_FOLDER.typeName,
                                                                               OpenMetadataType.COLLECTION.typeName,
                                                                               dataDictionaryId,
                                                                               null,
                                                                               "Folder::" + propertyGroup.getGUID() + "::" + propertyGroup.getTechnicalName(),
                                                                               propertyGroup.getDisplayName(),
                                                                               propertyGroup.getDescription(),
                                                                               null,
                                                                               null,
                                                                               null,
                                                                               null);

                    archiveHelper.addMemberToCollection(dataDictionaryId, propertyGroupFolderId, null);

                    List<String>  categoryList = new ArrayList<>();

                    categoryList.add(propertyGroupCategoryId);

                    List<PropertyDescription> propertyDescriptions = propertyGroup.getPropertyDescriptions();

                    if (propertyDescriptions != null)
                    {
                        for (PropertyDescription propertyDescription : propertyDescriptions)
                        {
                            if ((propertyDescription != null) && (propertyDescription.getDescription() != null))
                            {

                                String propertyTermId = archiveHelper.addTerm(glossaryId,
                                                                              categoryList,
                                                                              propertyGroup.getGUID() + propertyGroup.getTechnicalName() + "::" + propertyDescription.getTechnicalName(),
                                                                              propertyDescription.getDisplayName(),
                                                                              propertyDescription.getDescription());

                                if (propertyDescription.getAttributes() != null)
                                {
                                    for (Attribute attribute : propertyDescription.getAttributes())
                                    {
                                        archiveHelper.addSemanticDefinition(attribute.getGUID(), propertyTermId);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            /*
             * Retrieve the assembled archive content.
             */
            archiveHelper.saveGUIDs();
            return archiveBuilder.getOpenMetadataArchive();
        }
        else
        {
            archiveBuilder.logBadArchiveContent(methodName);
            return null;
        }
    }


    /**
     * Generates and writes out an open metadata archive containing all the elements extracted from the CIM.
     */
    void writeOpenMetadataArchive()
    {
        try
        {
            super.writeOpenMetadataArchive(defaultOpenMetadataArchiveFileName, this.getOpenMetadataArchive());
        }
        catch (Exception error)
        {
            System.out.println("error is " + error);
        }
    }


    /**
     * Return the GUID to use for the data field that represents a link, and record it in the GUID map.
     * <br>
     * The GUID map is what keeps an element's GUID the same from one build of the archive to the next, so a GUID it
     * already holds for the data field is used, whatever the model says.  Only a data field the map does not know yet
     * takes the link's GUID from the model - or, if the model gives it none, a new one.  The link's GUID used to be
     * written over the map's: links without an identifier in the model were given a random GUID on every build, and
     * the GUID map refused to write the archive because the GUIDs of shipped elements had changed.
     *
     * @param mappedGUID GUID the GUID map holds for the data field's qualified name, or null
     * @param modelGUID GUID the model gives the link, or null
     * @param qualifiedName qualified name of the data field
     * @return GUID for the data field
     */
    private String getDataFieldGUID(String mappedGUID,
                                    String modelGUID,
                                    String qualifiedName)
    {
        String guid = chooseDataFieldGUID(mappedGUID, modelGUID);

        if (guid == null)
        {
            return archiveHelper.getGUID(qualifiedName);
        }

        archiveHelper.setGUID(qualifiedName, guid);

        return guid;
    }


    /**
     * Return whether the data field for a link still needs to be added to the archive.  The list of existing properties
     * starts again for each subject area, but a data field's qualified name does not include the subject area, so the
     * same link appearing in two subject areas is the same data field.  It used to be added once for each, under
     * different GUIDs, which also changed the GUID map's entry for it on every build.
     *
     * @param dataFieldGUID GUID of the data field
     * @param existingProperties GUIDs of the properties already added for this subject area
     * @return boolean
     */
    private boolean isNewDataField(String       dataFieldGUID,
                                   List<String> existingProperties)
    {
        return (! existingProperties.contains(dataFieldGUID)) && (archiveBuilder.queryEntity(dataFieldGUID) == null);
    }


    /**
     * Choose the GUID for a data field: the one the GUID map already holds, else the one from the model.  Null means
     * neither is known and a new GUID is needed.
     *
     * @param mappedGUID GUID the GUID map holds for the data field, or null
     * @param modelGUID GUID the model gives the link, or null
     * @return GUID or null
     */
    static String chooseDataFieldGUID(String mappedGUID,
                                      String modelGUID)
    {
        if (mappedGUID != null)
        {
            return mappedGUID;
        }

        return modelGUID;
    }


    /**
     * Main program to initiate the archive writer for the Cloud Information Model (CIM).
     *
     * @param args list of arguments - first one should be the directory where the model
     *             content is located.  Any other arguments passed are ignored.
     */
    public static void main(String[] args)
    {
        String fileName = "cloud-information-model.jsonld";

        if (args.length > 0)
        {
            fileName = args[0];
        }

        try
        {
            CloudInformationModelArchiveWriter archiveWriter = new CloudInformationModelArchiveWriter(fileName);

            archiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}
