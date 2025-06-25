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
                                                                   OpenMetadataType.COLLECTION.typeName,
                                                                   OpenMetadataType.COLLECTION.typeName,
                                                                   null,
                                                                   OpenMetadataType.DATA_DICTIONARY_COLLECTION_CLASSIFICATION.typeName,
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
                                                       false,
                                                       false,
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
                                                                             OpenMetadataType.COLLECTION.typeName,
                                                                             OpenMetadataType.COLLECTION.typeName,
                                                                             null,
                                                                             OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
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
                                                                             OpenMetadataType.COLLECTION.typeName,
                                                                             OpenMetadataType.COLLECTION.typeName,
                                                                             null,
                                                                             OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
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
                                                                                            OpenMetadataType.COLLECTION.typeName,
                                                                                            OpenMetadataType.COLLECTION.typeName,
                                                                                            null,
                                                                                            OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
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
                                                            if (! existingProperties.contains(linkOption.getGUID()))
                                                            {
                                                                String attributeQualifiedName = "DataField::" + model.getModelTechnicalName() + "::" + linkOption.getTechnicalName();

                                                                if (linkOption.getGUID() == null)
                                                                {
                                                                    linkOption.setGUID(UUID.randomUUID().toString());
                                                                }

                                                                archiveHelper.setGUID(attributeQualifiedName, linkOption.getGUID());

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
                                                    else if (! existingProperties.contains(link.getGUID()))
                                                    {
                                                        String attributeQualifiedName = "DataField::" + model.getModelTechnicalName() + "::" + link.getTechnicalName();

                                                        if (link.getGUID() == null)
                                                        {
                                                            link.setGUID(UUID.randomUUID().toString());
                                                        }

                                                        archiveHelper.setGUID(attributeQualifiedName, link.getGUID());

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
                 * Add linkages

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
                                                                                "GlossaryCategory::PropertyGroups-" + model.getModelTechnicalName(),
                                                                                "Property Groups for the " + model.getModelName(),
                                                                                "Collections of properties found in the CIM Model.",
                                                                                null,
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
                                                                                       "GlossaryCategory::" + propertyGroup.getGUID() + "::" + propertyGroup.getTechnicalName(),
                                                                                       propertyGroup.getDisplayName(),
                                                                                       propertyGroup.getDescription(),
                                                                                       null);

                    archiveHelper.addCategoryToCategory(propertyGroupsCategoryId, propertyGroupCategoryId);

                    String propertyGroupFolderId = archiveHelper.addCollection(null,
                                                                               dataDictionaryId,
                                                                               OpenMetadataType.COLLECTION.typeName,
                                                                               OpenMetadataType.COLLECTION.typeName,
                                                                               dataDictionaryId,
                                                                               OpenMetadataType.FOLDER_COLLECTION_CLASSIFICATION.typeName,
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
