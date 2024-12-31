/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel;

import com.github.jsonldjava.utils.JsonUtils;
import org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 * CloudInformationModelParser reads the CloudInformationModel and parses it into a set of Java Beans
 * ready for the archive builder.  It hides the format of the model from the builder.
 *
 * The subject areas contain concept groups which in turn contain the concepts. These may be entities
 * or relationships.  Inside the concepts are the properties that link to the property descriptions
 * found in the property groups.
 */
class CloudInformationModelParser
{
    private static final String AT_GRAPH_TAG                = "@graph";
    private static final String AT_ID_TAG                   = "@id";
    private static final String AT_TYPE_TAG                 = "@type";
    private static final String NAME_TAG                    = "name";
    private static final String ENTITY_GROUPS_TAG           = "entityGroups";
    private static final String DESCRIPTION_TAG             = "description";
    private static final String CLASS_CONCEPTS_TAG          = "classConcepts";
    private static final String PROPERTY_CONCEPTS_TAG       = "propertyConcepts";
    private static final String DOMAIN_TAG                  = "domain";
    private static final String RANGE_TAG                   = "range";
    private static final String SCHEMAS_TAG                 = "schemas";
    private static final String VERSION_TAG                 = "version";
    private static final String PROPERTIES_TAG              = "properties";
    private static final String PATH_TAG                    = "path";
    private static final String NODE_TAG                    = "node";
    private static final String XOR_TAG                     = "xor";
    private static final String MAX_COUNT_TAG               = "maxCount";
    private static final String MIN_COUNT_TAG               = "minCount";
    private static final String DATA_TYPE_TAG               = "datatype";
    private static final String SUB_CLASS_TAG               = "subClassOf";
    private static final String ENTITY_GROUP_TYPE           = "EntityGroup";
    private static final String PROPERTY_GROUP_TYPE         = "PropertyGroup";
    private static final String SHAPE_TYPE                  = "Shape";
    private static final String SUBJECT_AREA_TYPE           = "SubjectArea";
    private static final String SUBJECT_AREA_PROPERTY       = "subjectArea";
    private static final String TARGET_CLASS_PROPERTY       = "targetClass";
    private static final String DATA_TYPE_RANGE             = "DataType";

    private static final String modelName = "Cloud Information Model (CIM)";
    private static final String modelTechnicalName = "CloudInformationModel";
    private static final String modelDescription = "The Cloud Information Model (CIM) is an open source glossary and data model that spans the following subject areas:\n" +
                                                           "\n" +
                                                           "•\tParty – people, their roles and organizations.\n" +
                                                           "•\tProduct – product descriptions, structures and packaging.\n" +
                                                           "•\tSales Order – customer orders for goods and services.\n" +
                                                           "•\tPayment Method – payment methods including cards, coupons and digital wallets.\n" +
                                                           "•\tPayment – individual payments for goods and services.\n" +
                                                           "•\tShipment – shipment of goods and services to the customer to fulfil an order.\n" +
                                                           "\n" +
                                                           "This means it can provide common data structures for new services spanning customer and employee interaction around typical commercial activities such as buying and selling of goods and services.\n" +
                                                           "\n" +
                                                           "The motivation behind the cloud information model is to support organizations who are transforming their digital services to run on a variety of cloud platforms and with their own data centres.  " +
                                                           "Often, they are dealing with systems built on many different generations of technology, with data distributed amongst them.  " +
                                                           "The CIM provides a common language to describe the different types of data.\n" +
                                                           "\n" +
                                                           "The CIM has been created to simplify the growing complexity companies experience when integrating data across different systems" +
                                                           "in order to deliver highly intelligent and personalized customer engagements. " +
                                                           "It standardizes data interoperability by creating a set of guidelines to easily connect systems such as a point of sale system, " +
                                                           "email marketing platform, customer service center, customer relationship management (CRM) system and more. " +
                                                           "Developers will no longer need to spend months creating custom code in order to deliver innovative customer experiences. " +
                                                           "The CIM can be easily adopted and extended within days so that developers can focus on creating new innovations faster that " +
                                                           "deliver a truly connected, personalized and intelligent customer experience. \n";
    private static final String modelLocation = "https://cloudinformationmodel.org/";
    private static final String modelScope = "People, organizations, accounts and contact details, orders and payments.";
    private static final String modelLanguage = "En_US";


    private final Map<String, SubjectArea>         subjectAreaMap         = new HashMap<>();
    private final Map<String, PropertyGroup>       propertyGroupMap       = new HashMap<>();
    private final Map<String, ConceptGroup>        conceptGroupMap        = new HashMap<>();
    private final Map<String, Concept>             conceptNameMap         = new HashMap<>();
    private final Map<String, Concept>             conceptGUIDMap         = new HashMap<>();
    private final Map<String, PropertyDescription> propertyDescriptionMap = new HashMap<>();
    private final List<ConceptGroup>               orphanedConceptGroups  = new ArrayList<>();
    private final List<String>                     errorReport            = new ArrayList<>();


    /**
     * Create a parser passing the location of the model.  This may be a single JSON-LD file or the GitHub file structure beginning
     * at either the src directory or the one with the top-level context.jsonld in it.
     *
     * @param cimLocation location of the model content
     */
    public CloudInformationModelParser(String cimLocation)
    {
        try
        {
            System.out.println("\nModel Name: " + modelName);

            File cimContent = new File(cimLocation);

            /*
             * Extract the content from the file into the maps.  The file is not too particular in
             * the order that elements appear and so it is necessary to perform multiple passes
             * through the content to ensure nothing is missed.
             */
            this.parseSingleFile(cimContent);

            /*
             * Review concepts and determine which are relationships.  A relationship is the domain of 2 links
             * and the range of none.
             */
            this.reviewConcepts();

            /*
             * Perform some referential integrity checks on the results.
             */
            if (! conceptNameMap.isEmpty())
            {
                for (Concept concept : conceptNameMap.values())
                {
                    if (concept != null)
                    {
                        if ((concept.getDescription() == null) || (concept.getDisplayName() == null))
                        {
                            errorReport.add("Missing bead: " + concept.getGUID());
                        }
                    }
                }
            }

            /*
             * Errors found during the processing are added to the error report.
             */
            if (! errorReport.isEmpty())
            {
                System.out.println("Error Report from parsing Source Model");
                for (String error : errorReport)
                {
                    System.out.println(error);
                }
            }
        }
        catch (Exception  error)
        {
            System.out.println("error is " + error);
        }
    }


    /**
     * Create a parser passing the directory where the model is located.  This is the src directory or the
     * one with the top-level context.jsonld in it.
     *
     * @param cimContentFile location of the model content
     */
    @SuppressWarnings("unchecked")
    private void parseSingleFile(File   cimContentFile) throws IOException
    {
        System.out.println("\nRetrieving model contents from: " + cimContentFile.getName());

        Map<String, Object> modelJsonLD = (LinkedHashMap<String, Object>) JsonUtils.fromInputStream(new FileInputStream(cimContentFile));
        List<Map<String, Object>> graphJsonLD = (List<Map<String, Object>>) modelJsonLD.get(AT_GRAPH_TAG);

        List<Map<String, Object>> propertyGroupsJsonLD = new ArrayList<>(); // List of reusable properties grouped by topic
        List<Map<String, Object>> entityGroupsJsonLD = new ArrayList<>(); // Groupings of entities and relationships
        List<Map<String, Object>> subjectAreasJsonLD = new ArrayList<>(); // Organization of entity groups into

        if (graphJsonLD != null)
        {
            /*
             * The entries in the graph are in no particularly order.  It is necessary to sort them onto
             * types, so they can be processed in a sensible order.
             */
            for (Map<String, Object> modelEntryJsonLD : graphJsonLD)
            {
                if (modelEntryJsonLD != null)
                {
                    String type = modelEntryJsonLD.get(AT_TYPE_TAG).toString();

                    switch (type)
                    {
                        case PROPERTY_GROUP_TYPE:
                            propertyGroupsJsonLD.add(modelEntryJsonLD);
                            break;
                        case ENTITY_GROUP_TYPE:
                            entityGroupsJsonLD.add(modelEntryJsonLD);
                            break;
                        case SUBJECT_AREA_TYPE:
                            subjectAreasJsonLD.add(modelEntryJsonLD);
                            break;
                    }
                }
            }
        }

        /*
         * The property groups provide the descriptive information about the principle properties in the model.
         * Each property group contains a list of property descriptions.
         */
        for (Map<String, Object> propertyGroupJsonLD : propertyGroupsJsonLD)
        {
            this.getPropertyGroup(propertyGroupJsonLD);
        }

        /*
         * The subject areas define the hierarchy of concept groups (entity groups) and their display names and descriptions.
         */
        for (Map<String, Object> subjectAreaJsonLD : subjectAreasJsonLD)
        {
            if (subjectAreaJsonLD != null)
            {
                SubjectArea subjectArea = new SubjectArea();

                subjectArea.setGUID(getStringValue(subjectAreaJsonLD.get(AT_ID_TAG)));
                subjectArea.setTechnicalName(getStringValue(subjectAreaJsonLD.get(NAME_TAG)));
                subjectArea.setDisplayName(getStringValue(subjectAreaJsonLD.get(NAME_TAG)));
                subjectArea.setDescription(getStringValue(subjectAreaJsonLD.get(DESCRIPTION_TAG)));
                subjectArea.setVersion(getStringValue(subjectAreaJsonLD.get(VERSION_TAG)));

                System.out.println("Subject area name: " + subjectArea.getDisplayName() + " with id: " + subjectArea.getGUID());

                subjectAreaMap.put(subjectArea.getGUID(), subjectArea);

                List<Map<String, Object>> entityGroupDefinitionsJsonLD = (List<Map<String, Object>>) subjectAreaJsonLD.get(ENTITY_GROUPS_TAG);

                if (entityGroupDefinitionsJsonLD != null)
                {
                    for (Map<String, Object> entityGroupDefinitionJsonLD : entityGroupDefinitionsJsonLD)
                    {
                        String conceptGroupGUID   = this.getStringValue(entityGroupDefinitionJsonLD.get(AT_ID_TAG));
                        String conceptGroupDisplayName = this.getStringValue(entityGroupDefinitionJsonLD.get(NAME_TAG));
                        String conceptGroupDescription = this.getStringValue(entityGroupDefinitionJsonLD.get(DESCRIPTION_TAG));

                        ConceptGroup conceptGroup = conceptGroupMap.get(conceptGroupGUID);

                        if (conceptGroup == null)
                        {
                            /*
                             * This is the expected case
                             */
                            conceptGroup = new ConceptGroup();

                            conceptGroup.setGUID(conceptGroupGUID);
                            conceptGroup.setDisplayName(conceptGroupDisplayName);
                            conceptGroup.setDescription(conceptGroupDescription);
                            conceptGroup.setSubjectAreaGUID(subjectArea.getGUID());

                            subjectArea.addConceptGroup(conceptGroupGUID, conceptGroup);

                            conceptGroupMap.put(conceptGroupGUID, conceptGroup);
                        }
                        else
                        {
                            System.out.println("ERROR: duplicate entity group: " + conceptGroupDisplayName + " (" + conceptGroupGUID + ")");
                            errorReport.add("ERROR: duplicate entity group: " + conceptGroupDisplayName + " (" + conceptGroupGUID + ")");
                        }

                    }
                }

            }
        }

        /*
         * Finally, process the entity groups.  Each entity group has two entries - one for the
         * classConcepts and propertyConcepts.  The other for the schema.  In the schema, the
         * Guids for the concepts are introduced in the schemas.  This means we need 4 sweeps
         * through the entity groups:
         *
         *  - Process the classConcepts to establish all the concept groups and concepts with their descriptions.
         *  - Process the schemas for the first time to set up the GUIDs in the concepts.
         *  - Process the schemas for the second time to get the properties (attributes and links)
         *  - Process the propertyConcepts as a validation check to ensure all properties are defined.
         */
        for (Map<String, Object> entityGroupJsonLD : entityGroupsJsonLD)
        {
            String conceptGroupGUID = getStringValue(entityGroupJsonLD.get(AT_ID_TAG));

            String conceptGroupTechnicalName = getStringValue(entityGroupJsonLD.get(NAME_TAG));
            String conceptGroupVersion = getStringValue(entityGroupJsonLD.get(VERSION_TAG));
            String conceptGroupDescription = getStringValue(entityGroupJsonLD.get(DESCRIPTION_TAG));
            String subjectAreaGUID = getStringValue(entityGroupJsonLD.get(SUBJECT_AREA_PROPERTY));

            ConceptGroup conceptGroup = conceptGroupMap.get(conceptGroupGUID);

            if (conceptGroup == null)
            {
                conceptGroup = new ConceptGroup();
                conceptGroup.setGUID(conceptGroupGUID);
                conceptGroup.setDisplayName(conceptGroupTechnicalName);
                conceptGroup.setDescription(conceptGroupDescription);
                conceptGroup.setSubjectAreaGUID(subjectAreaGUID);
            }
            else
            {
                /*
                 * Look for consistency ...
                 */
                if (! subjectAreaGUID.equals(conceptGroup.getSubjectAreaGUID()))
                {
                    String errorText = "ERROR: inconsistent subject area GUID for: " + conceptGroup.getDisplayName() + " (" + conceptGroup.getDisplayName() + ") - " + subjectAreaGUID + " verses " + conceptGroup.getSubjectAreaGUID();

                    System.out.println(errorText);
                    errorReport.add(errorText);
                }
                if (! conceptGroupDescription.equals(conceptGroup.getDescription()))
                {
                    String errorText = "ERROR: inconsistent description for: " + conceptGroup.getDisplayName() + " (" + conceptGroup.getDisplayName() + ") - " + conceptGroupDescription + " verses " + conceptGroup.getDescription();

                    System.out.println(errorText);
                    errorReport.add(errorText);
                }
            }

            conceptGroup.setTechnicalName(conceptGroupTechnicalName);
            conceptGroup.setVersion(conceptGroupVersion);

            List<Map<String, Object>> classConceptsJsonLD = (List<Map<String, Object>>)entityGroupJsonLD.get(CLASS_CONCEPTS_TAG);

            /*
             * Create a concept object for each class and add it to the conceptNameMap and
             * the appropriate concept group.
             */
            if (classConceptsJsonLD != null)
            {
                for (Map<String, Object> classConceptJsonLD : classConceptsJsonLD)
                {
                    if (classConceptJsonLD != null)
                    {
                        this.getConceptsForEntityGroup(conceptGroup, classConceptJsonLD);
                    }
                }
            }
        }

        for (Map<String, Object> entityGroupJsonLD : entityGroupsJsonLD)
        {
            List<Map<String, Object>> schemasJsonLD = (List<Map<String, Object>>)entityGroupJsonLD.get(SCHEMAS_TAG);

            /*
             * The schemas add the GUID of the concept and then use the GUID to link the concepts
             * together.  As the GUIDs are uncovered, the conceptGUIDMap is populated and
             * the GUIDs are added to the concepts.
             */
            if (schemasJsonLD != null)
            {
                /*
                 * This first pass extracts the GUIDs for each concept.
                 */
                for (Map<String, Object> schemaJsonLD : schemasJsonLD)
                {
                    if (schemaJsonLD != null)
                    {
                        this.getGUIDsForConcepts(schemaJsonLD);
                    }
                }
            }
        }

        for (Map<String, Object> entityGroupJsonLD : entityGroupsJsonLD)
        {
            List<Map<String, Object>> schemasJsonLD = (List<Map<String, Object>>)entityGroupJsonLD.get(SCHEMAS_TAG);

            /*
             * This next pass processes the properties (attributes and links).
             */
            if (schemasJsonLD != null)
            {
                for (Map<String, Object> schemaJsonLD : schemasJsonLD)
                {
                    if (schemaJsonLD != null)
                    {
                        this.getPropertiesForConcepts(schemaJsonLD);
                    }
                }
            }
        }

        for (Map<String, Object> entityGroupJsonLD : entityGroupsJsonLD)
        {
            List<Map<String, Object>> propertyConceptsJsonLD = (List<Map<String, Object>>) entityGroupJsonLD.get(PROPERTY_CONCEPTS_TAG);

            /*
             * The property concepts allow a check that all properties have been included in all
             * the right concepts in the model.
             */
            if (propertyConceptsJsonLD != null)
            {
                for (Map<String, Object> propertyConceptJsonLD : propertyConceptsJsonLD)
                {
                    if (propertyConceptJsonLD != null)
                    {
                        this.validatePropertiesForEntityGroup(propertyConceptJsonLD);
                    }
                }
            }
        }

        /*
         * Finally, process the entity groups that are part of the subject areas.
         */
        for (Map<String, Object> entityGroupJsonLD : entityGroupsJsonLD)
        {
            String conceptGroupGUID = getStringValue(entityGroupJsonLD.get(AT_ID_TAG));
            ConceptGroup conceptGroup = conceptGroupMap.get(conceptGroupGUID);

            SubjectArea subjectArea = null;
            String      subjectAreaId = getStringValue(entityGroupJsonLD.get(SUBJECT_AREA_PROPERTY));

            if (subjectAreaId != null)
            {
                subjectArea = subjectAreaMap.get(subjectAreaId);
            }

            if (subjectArea == null)
            {
                System.out.println("WARN: Orphaned concept group: " + conceptGroup.getDisplayName());
                orphanedConceptGroups.add(conceptGroup);
            }
        }
    }

    /**
     * The processing of the model content progressively adds information to the concept objects.
     * This method manages the map of concepts.  If this is the first time that the concept
     * is requested, a new bean is created for the map.
     *
     * @param conceptTechnicalName identifier of the concept
     * @return concept bean
     */
    private Concept getConcept(String conceptTechnicalName)
    {
        Concept bead = conceptNameMap.get(conceptTechnicalName);

        if (bead == null) // First call for this name
        {
            bead = new Concept();

            bead.setTechnicalName(conceptTechnicalName);

            conceptNameMap.put(conceptTechnicalName, bead);
        }

        return bead;
    }

    /**
     * Return the string version of the property if not null.
     *
     * @param modelElement value extracted from model.
     * @return string value or null
     */
    private String   getStringValue(Object    modelElement)
    {
        if (modelElement == null)
        {
            return null;
        }
        else
        {
            return modelElement.toString();
        }
    }


    /**
     * The property descriptions are extracted as part of the property group parsing.
     * Later they are then retrieved and linked to a property for each use of the property.
     * This method manages the map of property descriptions so that they can be found during the
     * creation of properties.  It creates a property description object on first use.
     *
     * @param propertyTechnicalName identifier of the property (and property description)
     * @param rangeTag type information for the property
     * @return property description bean
     */
    private PropertyDescription getPropertyDescription(String   propertyTechnicalName,
                                                       String   rangeTag)
    {
        PropertyDescription propertyDescription = propertyDescriptionMap.get(propertyTechnicalName);

        if (propertyDescription == null)
        {
            propertyDescription = new PropertyDescription();

            if (DATA_TYPE_RANGE.equals(rangeTag))
            {
                propertyDescription.setPrimitive(true);
            }
            else
            {
                propertyDescription.setPrimitive(false);
                propertyDescription.setDataTypeId(rangeTag);
            }

            propertyDescriptionMap.put(propertyTechnicalName, propertyDescription);
        }

        return propertyDescription;
    }


    /**
     * The property descriptions are extracted as part of the property group parsing.
     * Later they are then retrieved and linked to a property for each use of the property.
     * This method manages the creation of property elements.  They are either attributes or links.
     *
     * @param concept the concept that this property belongs to
     * @param propertyGUID unique identifier of the property to create
     * @param propertyTechnicalName unique name of the property (and unique identifier of property description)
     * @param dataType if the property is an attribute, this is its type
     * @param node if the property is a link, this is the destination (range)
     * @param maxCount indicates cardinality of the property
     * @param minCount indicates cardinality of the property
     */
    @SuppressWarnings("unchecked")
    private void addProperty(Concept concept,
                             String  propertyGUID,
                             String  propertyTechnicalName,
                             String  dataType,
                             Object  node,
                             String  maxCount,
                             String  minCount)
    {
        PropertyDescription propertyDescription = getPropertyDescription(propertyTechnicalName);

        if (dataType != null)
        {
            Attribute attribute = new Attribute(propertyGUID, propertyTechnicalName, propertyDescription);
            attribute.setDataType(dataType);
            attribute.setMaxCount(maxCount);
            attribute.setMinCount(minCount);

            concept.setAttribute(propertyTechnicalName, attribute);
        }
        else if (node != null)
        {
            String  rangeConceptGUID = node.toString();
            Concept rangeConcept = conceptGUIDMap.get(rangeConceptGUID);

            if (rangeConcept != null)
            {
                if (rangeConceptGUID.equals(rangeConcept.getGUID()))
                {
                    Link link = getLink(propertyGUID,
                                        propertyTechnicalName,
                                        propertyDescription,
                                        concept,
                                        rangeConcept,
                                        maxCount,
                                        minCount);

                    concept.setDomainOfLink(propertyTechnicalName, link);
                    rangeConcept.setRangeOfLink(propertyTechnicalName, link);
                }
            }
            else
            {
                /*
                 * The node is probably a list of choices.
                 */
                Map<String, Map<String, List<Map<String, Object>>>> xorMap = (Map<String, Map<String, List<Map<String, Object>>>>)node;
                Map<String, List<Map<String, Object>>> xor = xorMap.get(XOR_TAG);
                List<Map<String, Object>> atList = xor.get("@list");

                if (atList != null)
                {
                    LinkChoice linkChoice = new LinkChoice(propertyGUID, propertyTechnicalName, propertyDescription);

                    for (Map<String, Object> idMap : atList)
                    {
                        Object nodeIdObject = idMap.get(AT_ID_TAG);
                        String nodeIdChoice = getStringValue(nodeIdObject);

                        Concept rangeConceptChoice = conceptGUIDMap.get(nodeIdChoice);

                        if (rangeConceptChoice != null)
                        {
                            if (nodeIdChoice.equals(rangeConceptChoice.getGUID()))
                            {
                                Link link = getLink(propertyGUID,
                                                    propertyTechnicalName,
                                                    propertyDescription,
                                                    concept,
                                                    rangeConceptChoice,
                                                    maxCount,
                                                    minCount);

                                rangeConceptChoice.setRangeOfLink(propertyTechnicalName, link);

                                linkChoice.addLinkChoice(link);
                            }
                        }
                    }

                    concept.setDomainOfLink(propertyTechnicalName, linkChoice);
                }
                else
                {
                    String errorText = "ERROR: invalid node: " + node + " for property " + propertyTechnicalName + " for concept " + concept.getTechnicalName() + "(" + concept.getGUID() + ")";

                    System.out.println(errorText);
                    errorReport.add(errorText);
                }
            }
        }
        else
        {
            String errorText = "ERROR: invalid property: " + propertyTechnicalName +  " for concept " + concept.getTechnicalName() + "(" + concept.getGUID() + ") with neither data type nor node";

            System.out.println(errorText);
            errorReport.add(errorText);
        }
    }


    /**
     * Create a link property.
     *
     * @param propertyGUID unique identifier of the property
     * @param propertyTechnicalName unique name of the property
     * @param propertyDescription full description from the property group
     * @param domainConcept concept at the domain end of the link
     * @param rangeConcept concept at the range end of the link
     * @param maxCount max count for cardinality
     * @param minCount min could for cardinality
     * @return new link
     */
    private Link getLink(String               propertyGUID,
                         String               propertyTechnicalName,
                         PropertyDescription  propertyDescription,
                         Concept              domainConcept,
                         Concept              rangeConcept,
                         String               maxCount,
                         String               minCount)
    {
        Link link = new Link(propertyGUID, propertyTechnicalName, propertyDescription);

        link.setDomainConceptGUID(domainConcept.getGUID());
        link.setDomainConceptGUID(domainConcept.getTechnicalName());

        link.setRangeConceptGUID(rangeConcept.getGUID());
        link.setRangeConceptName(rangeConcept.getTechnicalName());

        link.setMaxCount(maxCount);
        link.setMinCount(minCount);

        return link;
    }

    /**
     * Retrieves a property description.  It expects the property description to be present.
     *
     * @param propertyTechnicalName identifier of property (and property description)
     * @return property description bean
     */
    private PropertyDescription getPropertyDescription(String   propertyTechnicalName)
    {
        PropertyDescription propertyDescription = propertyDescriptionMap.get(propertyTechnicalName);

        if (propertyDescription == null)
        {
            /*
             * All the property descriptions should have been retrieved during the
             * processing of the property groups.
             */
            System.out.println("WARN: property description not in a property group: " + propertyTechnicalName);
            errorReport.add("WARN: property description not in a property group: " + propertyTechnicalName);

            propertyDescription = new PropertyDescription();

            propertyDescription.setTechnicalName(propertyTechnicalName);
            propertyDescription.setDisplayName(propertyTechnicalName);

            propertyDescriptionMap.put(propertyTechnicalName, propertyDescription);
        }

        return propertyDescription;
    }


    /**
     * Step through the content of a property group and extract the property descriptions.
     *
     * @param conceptsJsonLD the property group content
     */
    @SuppressWarnings("unchecked")
    private void getPropertyGroup(Map<String, Object> conceptsJsonLD)
    {
        PropertyGroup propertyGroup = new PropertyGroup();

        propertyGroup.setTechnicalName(getStringValue(conceptsJsonLD.get(AT_ID_TAG)));
        propertyGroup.setDisplayName(getStringValue(conceptsJsonLD.get(NAME_TAG)));

        propertyGroupMap.put(propertyGroup.getTechnicalName(), propertyGroup);

        List<Map<String, Object>>  propertyConceptsJsonLD = (List<Map<String, Object>>)conceptsJsonLD.get(PROPERTY_CONCEPTS_TAG);

        if (propertyConceptsJsonLD != null)
        {
            for (Map<String, Object> propertyConceptJsonLD : propertyConceptsJsonLD)
            {
                if (propertyConceptJsonLD != null)
                {
                    String               propertyTechnicalName = getStringValue(propertyConceptJsonLD.get(AT_ID_TAG));
                    PropertyDescription  propertyDescription   = this.getPropertyDescription(propertyTechnicalName,
                                                                                             getStringValue(propertyConceptJsonLD.get(RANGE_TAG)));

                    propertyDescription.setTechnicalName(propertyTechnicalName);
                    propertyDescription.setDisplayName(getStringValue(propertyConceptJsonLD.get(NAME_TAG)));
                    propertyDescription.setDescription(getStringValue(propertyConceptJsonLD.get(DESCRIPTION_TAG)));

                    if (propertyDescription.isPrimitive())
                    {
                        System.out.println("==> Property name: " + propertyTechnicalName + " is an attribute ");
                    }
                    else if (propertyDescription.getDataTypeId() != null)
                    {
                        System.out.println("==> Property name: " + propertyTechnicalName + " links to " + propertyDescription.getDataTypeId());
                    }
                    else
                    {
                        System.out.println("ERROR: no data type Id for property: " + propertyTechnicalName);
                        errorReport.add("ERROR: no data type Id for property: " + propertyTechnicalName);
                    }

                    propertyGroup.addPropertyDescription(propertyTechnicalName, propertyDescription);
                }
            }
        }
    }



    /**
     * Extract a member of the EntityGroup's classConcepts list.
     * The classConcepts identify each concept by technical name.
     * The GUIDs need to be added later because the links between concepts use GUIDs to
     * identify the concepts.
     *
     * @param conceptGroup current concept group
     * @param classConceptJsonLD JSON-LD structure with the content in it
     */
    private void getConceptsForEntityGroup(ConceptGroup        conceptGroup,
                                           Map<String, Object> classConceptJsonLD)
    {
        if (classConceptJsonLD != null)
        {
            String conceptTechnicalName = getStringValue(classConceptJsonLD.get(AT_ID_TAG));

            if (conceptTechnicalName != null)
            {
                /*
                 * This will create the concept if it does not exist and add it to the conceptNameMap.
                 */
                Concept concept = this.getConcept(conceptTechnicalName);

                concept.setTechnicalName(conceptTechnicalName);
                concept.setDisplayName(getStringValue(classConceptJsonLD.get(NAME_TAG)));
                concept.setDescription(getStringValue(classConceptJsonLD.get(DESCRIPTION_TAG)));
                concept.setVersion(conceptGroup.getVersion());

                String superClassName = getStringValue(classConceptJsonLD.get(SUB_CLASS_TAG));
                if (superClassName != null)
                {
                    Concept superClass = this.getConcept(superClassName);

                    superClass.addSubClass(conceptTechnicalName);
                    concept.setSuperClassName(superClass.getTechnicalName());

                    System.out.println("     ==> Concept Bead name: " + conceptTechnicalName + " has superclass " + superClassName);
                }
                else
                {
                    System.out.println("     ==> Concept Bead name: " + conceptTechnicalName + " is top level class ");
                }

                conceptGroup.addConcept(conceptTechnicalName, concept);
            }
            else
            {
                System.out.println("ERROR: no id for concept properties: " + classConceptJsonLD);
                errorReport.add("ERROR: no id for concept properties: " + classConceptJsonLD);
            }
        }
    }


    /**
     * Extract a member of the EntityGroup's propertyGroups list.
     *
     * @param propertyConceptJsonLD JSON-LD structure with the content in it
     */
    @SuppressWarnings("unchecked")
    private void validatePropertiesForEntityGroup(Map<String, Object> propertyConceptJsonLD)
    {
        String        propertyTechnicalName = getStringValue(propertyConceptJsonLD.get(AT_ID_TAG));

        Object        domainNamesObject = propertyConceptJsonLD.get(DOMAIN_TAG);
        List<String>  domainNames = new ArrayList<>((List<String>)domainNamesObject);

        if (! domainNames.isEmpty())
        {
            for (String domainName : domainNames)
            {
                if (domainName != null)
                {
                    Concept concept = getConcept(domainName);

                    if (concept.getGUID() != null)
                    {
                        Attribute attribute = concept.getAttribute(propertyTechnicalName);
                        Link      link      = concept.getDomainLink(propertyTechnicalName);

                        if ((attribute == null) && (link == null))
                        {
                            String errorText = "WARN: missing property: " + propertyTechnicalName + " in concept " + domainName;

                            System.out.println(errorText);
                            errorReport.add(errorText);
                        }
                    }
                    else
                    {
                        String errorText = "WARN: missing concept: " + domainName + " for property " + propertyTechnicalName;

                        System.out.println(errorText);
                        errorReport.add(errorText);
                    }
                }
            }
        }
        else
        {
            System.out.println("WARN: no domains for property name: " + propertyTechnicalName);
            errorReport.add("WARN: no domains for property name: " + propertyTechnicalName);
        }
    }


    /**
     * Extract a member of the EntityGroup's schemas list to populate the guids of the concepts.
     *
     * @param schemaJsonLD JSON-LD structure with the content in it
     */
    private void getGUIDsForConcepts(Map<String, Object> schemaJsonLD)
    {
        try
        {
            String schemaType = getStringValue(schemaJsonLD.get(AT_TYPE_TAG));

            if (SHAPE_TYPE.equals(schemaType))
            {
                String conceptTechnicalName = getStringValue(schemaJsonLD.get(TARGET_CLASS_PROPERTY));
                String conceptGUID          = getStringValue(schemaJsonLD.get(AT_ID_TAG));

                if (conceptTechnicalName != null)
                {
                    Concept concept = this.getConcept(conceptTechnicalName);

                    concept.setGUID(conceptGUID);
                    conceptGUIDMap.put(conceptGUID, concept);
                }
                else
                {
                    System.out.println("ERROR: no name for concept: " + schemaJsonLD);
                    errorReport.add("ERROR: no name for concept: " + schemaJsonLD);
                }
            }
        }
        catch (Exception  error)
        {
            System.out.println("error is " + error);
        }
    }


    /**
     * Extract a member of the EntityGroup's schemas list to populate the properties.
     *
     * @param schemaJsonLD JSON-LD structure with the content in it
     */
    @SuppressWarnings("unchecked")
    private void getPropertiesForConcepts(Map<String, Object> schemaJsonLD)
    {
        try
        {
            String schemaType = getStringValue(schemaJsonLD.get(AT_TYPE_TAG));

            if (SHAPE_TYPE.equals(schemaType))
            {
                String conceptTechnicalName = getStringValue(schemaJsonLD.get(TARGET_CLASS_PROPERTY));

                if (conceptTechnicalName != null)
                {
                    Concept                   concept          = this.getConcept(conceptTechnicalName);
                    List<Map<String, Object>> propertiesJsonLD = (List<Map<String, Object>>) schemaJsonLD.get(PROPERTIES_TAG);

                    if (propertiesJsonLD != null)
                    {
                        for (Map<String, Object> propertyJsonLD : propertiesJsonLD)
                        {
                            String propertyGUID = getStringValue(propertyJsonLD.get(AT_TYPE_TAG));
                            String propertyTechnicalName = getStringValue(propertyJsonLD.get(PATH_TAG));
                            String dataType = getStringValue(propertyJsonLD.get(DATA_TYPE_TAG));
                            Object node = propertyJsonLD.get(NODE_TAG);
                            String maxCount = getStringValue(propertyJsonLD.get(MAX_COUNT_TAG));
                            String minCount = getStringValue(propertyJsonLD.get(MIN_COUNT_TAG));

                            addProperty(concept,
                                        propertyGUID,
                                        propertyTechnicalName,
                                        dataType,
                                        node,
                                        maxCount,
                                        minCount);
                        }
                    }
                }
                else
                {
                    System.out.println("ERROR: no name for concept: " + schemaJsonLD);
                    errorReport.add("ERROR: no name for concept: " + schemaJsonLD);
                }
            }
        }
        catch (Exception  error)
        {
            System.out.println("error is " + error);
        }
    }


    /**
     * Review concepts and determine which are relationships.  A relationship is the domain of 2 links
     * and the range of none.
     */
    private void reviewConcepts()
    {
        for (Concept concept: conceptNameMap.values())
        {
            if (concept != null)
            {
                if (concept.getRangeOfLinks() == null)
                {
                    List<Link>  domainOfLinks = concept.getDomainOfLinks();

                    if (domainOfLinks != null)
                    {
                        if ((domainOfLinks.size() == 2) && (concept.getRangeOfLinks() == null))
                        {
                            concept.setRelationship(true);
                            System.out.println("Concept: " + concept.getTechnicalName() + " is a relationship linking two concepts: " + domainOfLinks.get(0).getTechnicalName() + " and " + domainOfLinks.get(1).getTechnicalName());
                        }
                        else
                        {
                            System.out.println("Concept: " + concept.getTechnicalName() + " is a master-detail record");
                        }
                    }
                    else
                    {
                        System.out.println("Concept: " + concept.getTechnicalName() + " is isolated - it is not linked to anything" );
                    }
                }
                else
                {
                    System.out.println("Concept: " + concept.getTechnicalName() + " is linked to by other concepts" );
                }
            }
        }
    }


    /**
     * Assemble all the discovered content and return it to the caller.
     *
     * @return model object with the model content contained in nested beans.
     */
    public Model getModel()
    {
        Model model = new Model();

        model.setModelName(modelName);
        model.setModelTechnicalName(modelTechnicalName);
        model.setModelDescription(modelDescription);
        model.setModelLocation(modelLocation);
        model.setModelScope(modelScope);
        model.setModelLanguage(modelLanguage);

        model.setSubjectAreaMap(subjectAreaMap);
        model.setPropertyGroupMap(propertyGroupMap);
        model.setPropertyDescriptionMap(propertyDescriptionMap);
        model.setConceptBeadMap(conceptNameMap);
        model.setOrphanedConceptGroups(orphanedConceptGroups);

        model.setErrorReport(errorReport);

        return model;
    }
}
