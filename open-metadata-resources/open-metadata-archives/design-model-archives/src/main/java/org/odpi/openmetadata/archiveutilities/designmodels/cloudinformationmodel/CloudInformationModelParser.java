/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel;

import com.github.jsonldjava.utils.JsonUtils;
import org.odpi.openmetadata.archiveutilities.designmodels.cloudinformationmodel.properties.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 * CloudInformationModelParser reads the CloudInformationModel and parses it into a set of Java Beans
 * ready for the archive builder.  It hides the format of the model from the builder.
 */
class CloudInformationModelParser
{
    private static final String SUBJECT_AREA_FOLDER_NAME    = "/subjectAreas";
    private static final String PROPERTY_GROUPS_FOLDER_NAME = "/propertyGroups";
    private static final String CONCEPTS_FILE_NAME          = "/concepts.jsonld";
    private static final String SCHEMA_FILE_NAME            = "/schema.jsonld";
    private static final String SUBJECT_AREA_DESC_FILE_NAME = "about.jsonld";
    private static final String AT_CONTEXT_TAG              = "@context";
    private static final String AT_ID_TAG                   = "@id";
    private static final String AT_TYPE_TAG                 = "@type";
    private static final String NAME_TAG                    = "name";
    private static final String SUBJECT_AREA_TAG            = "subjectArea";
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
    private static final String AND_TAG                     = "and";
    private static final String MAX_COUNT_TAG               = "maxCount";
    private static final String MIN_COUNT_TAG               = "minCount";
    private static final String DATA_TYPE_TAG               = "dataType";
    private static final String SUB_CLASS_TAG               = "subClassOf";
    private static final String ENTITY_GROUP_TYPE           = "EntityGroup";
    private static final String CLASS_TYPE                  = "Class";
    private static final String MANAGED_PROPERTY_TYPE       = "ManagedProperty";
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
    private static final String modelLocation = "http://cloudinformationmodel.org/";
    private static final String modelScope = "People, organizations, accounts and contact details, orders and payments.";
    private static final String modelLanguage = "En_US";


    private Map<String, SubjectArea>         subjectAreaMap         = new HashMap<>();
    private Map<String, PropertyGroup>       propertyGroupMap       = new HashMap<>();
    private Map<String, Concept>             conceptBeadMap         = new HashMap<>();
    private Map<String, PropertyDescription> propertyDescriptionMap = new HashMap<>();
    private List<String>                     errorReport            = new ArrayList<>();


    /**
     * Create a parser passing the directory where the model is located.  This is the src directory or the
     * one with the top-level context.jsonld in it.
     *
     * @param cimDirectory location of the model content
     */
    CloudInformationModelParser(String cimDirectory) throws IOException
    {
        System.out.println("\nModel Name: " + modelName);

        /*
         * The property groups provide the descriptive information about the properties in the model.
         * Each property group contains a list of property descriptions.
         */
        this.getPropertyGroups(cimDirectory);

        /*
         * The subject areas contain concept groups which in turn contain the concepts. These may be entities
         * or relationships.  Inside the concepts are the properties that link to the property descriptions
         * found in the property groups.
         */
        this.getSubjectAreas(cimDirectory);

        if (! conceptBeadMap.isEmpty())
        {
            for (Concept concept : conceptBeadMap.values())
            {
                if (concept != null)
                {
                    if ((concept.getDescription() == null) || (concept.getDisplayName() == null))
                    {
                        errorReport.add("Missing bead: " + concept.getId());
                    }
                }
            }
        }

        /*
         * Errors found during the processing are added to the error report.
         */
        if (! errorReport.isEmpty())
        {
            System.out.println("Error Report");
            System.out.println(errorReport);
        }
    }


    /**
     * The processing of the model content progressively adds information to the concept objects.
     * This method manages the map of concepts.  If this is the first time that the concept
     * is requested, a new bean is created for the map.
     *
     * @param conceptId identifier of the concept
     * @return concept bean
     */
    private Concept getConcept(String   conceptId)
    {
        Concept bead = conceptBeadMap.get(conceptId);

        if (bead == null)
        {
            bead = new Concept(conceptId);
            bead.setId(conceptId);

            conceptBeadMap.put(conceptId, bead);
        }

        return bead;
    }


    /**
     * The property descriptions are extracted as part of the property group parsing.
     * Later they are then retrieved and linked to a property for each use of the property.
     * This method manages the map of property descriptions so that they can be found during the
     * creation of properties.  It creates a property description object on first use.
     *
     * @param propertyId identifier of the property (and property description)
     * @param rangeTag type information for the property
     * @return property description bean
     */
    private PropertyDescription getPropertyDescription(String   propertyId,
                                                       String   rangeTag)
    {
        PropertyDescription propertyDescription = propertyDescriptionMap.get(propertyId);

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

            propertyDescription.setId(propertyId);

            propertyDescriptionMap.put(propertyId, propertyDescription);
        }

        return propertyDescription;
    }


    /**
     * Retrieves a property description.  It expects the property description to be present.
     *
     * @param propertyId identifier of property (and property description)
     * @return property description bean
     */
    private PropertyDescription getPropertyDescription(String   propertyId)
    {
        PropertyDescription propertyDescription = propertyDescriptionMap.get(propertyId);

        if (propertyDescription == null)
        {
            /*
             * All of the property descriptions should have been retrieved during the
             * processing of the property groups.
             */
            System.out.println("ERROR: missing property description: " + propertyId);
            errorReport.add("ERROR: missing property description: " + propertyId);

            propertyDescription = new PropertyDescription();

            propertyDescription.setId(propertyId);
            propertyDescription.setDisplayName(propertyId);

            propertyDescriptionMap.put(propertyId, propertyDescription);
        }

        return propertyDescription;
    }


    /**
     * Step through each or the property groups and extract the relevant property descriptions.
     *
     * @param rootDirectory model directory
     * @throws IOException file/directory not found - probably a logic error
     */
    private void getPropertyGroups(String rootDirectory) throws IOException
    {
        File propertyGroupsDirectory = new File(rootDirectory + PROPERTY_GROUPS_FOLDER_NAME);

        if (propertyGroupsDirectory.isDirectory())
        {
            File[] propertyGroups = propertyGroupsDirectory.listFiles();

            if (propertyGroups != null)
            {
                for (File propertyGroup : propertyGroups)
                {
                    this.getPropertyGroup(propertyGroup);
                }
            }
        }
    }


    /**
     * Step through the content of a property group and extract the property descriptions.
     *
     * @param propertyGroupDirectory location of the property group content
     * @throws IOException file/directory not found - probably a logic error
     */
    private void getPropertyGroup(File propertyGroupDirectory) throws IOException
    {
        if (propertyGroupDirectory.isDirectory())
        {
            String propertyGroupDirectoryName = propertyGroupDirectory.toString();
            String propertyGroupName = propertyGroupDirectory.getName();
            System.out.println("Property group name: " + propertyGroupName);

            File conceptsJsonLDFile = new File(propertyGroupDirectoryName + CONCEPTS_FILE_NAME);

            LinkedHashMap<String, Object> conceptsJsonLD = (LinkedHashMap<String, Object>) JsonUtils.fromInputStream(new FileInputStream(conceptsJsonLDFile));

            if (modelLocation.equals(getStringValue(conceptsJsonLD.get(AT_CONTEXT_TAG))))
            {
                String  versionName = getStringValue(conceptsJsonLD.get(VERSION_TAG));

                PropertyGroup propertyGroup = new PropertyGroup();

                propertyGroup.setId(getStringValue(conceptsJsonLD.get(AT_ID_TAG)));
                propertyGroup.setDisplayName(getStringValue(conceptsJsonLD.get(NAME_TAG)));
                propertyGroup.setVersion(versionName);
                propertyGroup.setTechnicalName(this.getTechnicalName(propertyGroup.getId(), "PropertyGroup"));

                propertyGroupMap.put(propertyGroupName, propertyGroup);

                List<Map<String, Object>>  propertyConcepts = (List<Map<String, Object>>)conceptsJsonLD.get(PROPERTY_CONCEPTS_TAG);

                if (propertyConcepts != null)
                {
                    for (Map<String, Object> propertyDefinition : propertyConcepts)
                    {
                        if (propertyDefinition != null)
                        {
                            String               propertyId = getStringValue(propertyDefinition.get(AT_ID_TAG));
                            PropertyDescription  propertyDescription   = this.getPropertyDescription(propertyId,
                                                                                                     getStringValue(propertyDefinition.get(RANGE_TAG)));

                            propertyDescription.setId(propertyId);
                            propertyDescription.setTechnicalName(propertyId);
                            propertyDescription.setDisplayName(getStringValue(propertyDefinition.get(NAME_TAG)));
                            propertyDescription.setDescription(getStringValue(propertyDefinition.get(DESCRIPTION_TAG)));
                            propertyDescription.setVersion(versionName);

                            if (propertyDescription.isPrimitive())
                            {
                                System.out.println("==> Property name: " + propertyId + " is an attribute ");
                            }
                            else
                            {
                                System.out.println("==> Property name: " + propertyId + " links to " + propertyDescription.getDataTypeId());
                            }

                            propertyGroup.addPropertyDescription(propertyId, propertyDescription);
                            propertyDescriptionMap.put(propertyId, propertyDescription);
                        }
                    }
                }
            }
        }
    }


    String getTechnicalName(String  sourceString,
                            String  objectType)
    {
        if (sourceString != null)
        {
            String splitString[] = sourceString.split(objectType, 2);

            System.out.println("==> Object: " + sourceString + " has a technicalName of " + splitString[0]);

            return splitString[0];
        }
        else
        {
            return null;
        }
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
     * Step through each of the subject areas and extract their content.
     *
     * @param rootDirectory location of the model.
     * @throws IOException file/directory not found - probably a logic error
     */
    private void getSubjectAreas(String rootDirectory) throws IOException
    {
        File subjectAreasDirectory = new File(rootDirectory + SUBJECT_AREA_FOLDER_NAME);

        if (subjectAreasDirectory.isDirectory())
        {
            File[] subjectAreas = subjectAreasDirectory.listFiles();

            if (subjectAreas != null)
            {
                for (File subjectArea : subjectAreas)
                {
                    getSubjectArea(subjectArea);
                }
            }
        }
    }


    /**
     * Retrieve the content of a subject area.  This begins with the entity groups.
     *
     * @param subjectAreaDirectory location of the subject area
     * @throws IOException file/directory not found - probably a logic error
     */
    private void getSubjectArea(File subjectAreaDirectory) throws IOException
    {
        if (subjectAreaDirectory.isDirectory())
        {
            String subjectAreaName = subjectAreaDirectory.getName();
            System.out.println("Subject area name: " + subjectAreaName);

            SubjectArea subjectArea = new SubjectArea();

            File[] subjectAreaFiles = subjectAreaDirectory.listFiles();

            if (subjectAreaFiles != null)
            {
                for (File subjectAreaFile : subjectAreaFiles)
                {
                    if (subjectAreaFile != null)
                    {
                        if (subjectAreaFile.isDirectory())
                        {
                            getEntityGroup(subjectArea, subjectAreaFile);
                        }
                        else if (SUBJECT_AREA_DESC_FILE_NAME.equals(subjectAreaFile.getName()))
                        {
                            LinkedHashMap<String, Object> aboutJsonLD = (LinkedHashMap<String, Object>) JsonUtils.fromInputStream(new FileInputStream(subjectAreaFile));

                            subjectArea.setId(getStringValue(aboutJsonLD.get(AT_ID_TAG)));
                            subjectArea.setTechnicalName(this.getTechnicalName(subjectArea.getId(), "SubjectArea"));
                            subjectArea.setDisplayName(getStringValue(aboutJsonLD.get(NAME_TAG)));
                            subjectArea.setDescription(getStringValue(aboutJsonLD.get(DESCRIPTION_TAG)));

                            subjectAreaMap.put(subjectAreaName, subjectArea);
                        }
                        else if (! subjectAreaFile.getName().endsWith(".png"))
                        {
                            System.out.println("WARNING skipping file named: " + subjectAreaFile.getName());
                            errorReport.add("WARNING skipping file named: " + subjectAreaFile.getName());
                        }
                    }
                }
            }
        }
    }


    /**
     * Retrieves the content from a single entity group (part of a subject area).
     *
     * @param subjectArea owning subject area
     * @param entityGroupDirectory directory where the entity group is located.
     * @throws IOException file/directory not found - probably a logic error
     */
    private void getEntityGroup(SubjectArea  subjectArea,
                                File         entityGroupDirectory) throws IOException
    {
        if (entityGroupDirectory.isDirectory())
        {
            String entityGroupDirectoryName = entityGroupDirectory.toString();
            String entityGroupName = entityGroupDirectory.getName();
            System.out.println("==> Entity Group name: " + entityGroupName);

            File conceptsJsonLDFile = new File(entityGroupDirectoryName + CONCEPTS_FILE_NAME);

            LinkedHashMap<String, Object> conceptsJsonLD = (LinkedHashMap<String, Object>) JsonUtils.fromInputStream(new FileInputStream(conceptsJsonLDFile));

            if ((ENTITY_GROUP_TYPE.equals(getStringValue(conceptsJsonLD.get(AT_TYPE_TAG)))) &&
                (modelLocation.equals(getStringValue(conceptsJsonLD.get(AT_CONTEXT_TAG)))))
            {
                String  versionName = getStringValue(conceptsJsonLD.get(VERSION_TAG));

                ConceptGroup conceptGroup = new ConceptGroup();

                conceptGroup.setId(getStringValue(conceptsJsonLD.get(AT_ID_TAG)));
                conceptGroup.setTechnicalName(this.getTechnicalName(conceptGroup.getId(), "EntityGroup"));
                conceptGroup.setDisplayName(getStringValue(conceptsJsonLD.get(NAME_TAG)));
                conceptGroup.setDescription(getStringValue(conceptsJsonLD.get(DESCRIPTION_TAG)));
                conceptGroup.setVersion(versionName);

                subjectArea.addConceptGroup(conceptGroup.getId(), conceptGroup);

                List<Map<String, Object>>  classConcepts = (List<Map<String, Object>>)conceptsJsonLD.get(CLASS_CONCEPTS_TAG);

                if (classConcepts != null)
                {
                    for (Map<String, Object> conceptProperties : classConcepts)
                    {
                        if (conceptProperties != null)
                        {
                            String  conceptId = getStringValue(conceptProperties.get(AT_ID_TAG));
                            if (conceptId != null)
                            {
                                Concept concept = this.getConcept(conceptId);

                                concept.setId(conceptId);
                                concept.setTechnicalName(conceptId);
                                concept.setDisplayName(getStringValue(conceptProperties.get(NAME_TAG)));
                                concept.setDescription(getStringValue(conceptProperties.get(DESCRIPTION_TAG)));
                                concept.setVersion(versionName);

                                Object conceptPropertiesType = conceptProperties.get(AT_TYPE_TAG);

                                if (conceptPropertiesType == null)
                                {
                                    concept.setReferenceDataSet(false);
                                }
                                else
                                {
                                    concept.setReferenceDataSet(getStringValue(conceptProperties.get(AT_TYPE_TAG)).contains(MANAGED_PROPERTY_TYPE));
                                }

                                Object superClassName = conceptProperties.get(SUB_CLASS_TAG);
                                if (superClassName != null)
                                {
                                    Concept superClass = this.getConcept(superClassName.toString());

                                    superClass.addSubClass(conceptId, conceptId);
                                    concept.setSuperClassId(superClass.getId());

                                    System.out.println("   ==> Concept Bead name: " + conceptId + " is ref data " + concept.isReferenceDataSet() + " and has superclass " + superClassName);
                                }
                                else
                                {
                                    System.out.println("   ==> Concept Bead name: " + conceptId + " is ref data " + concept.isReferenceDataSet());
                                }

                                conceptGroup.addConcept(conceptId, concept);
                            }
                            else
                            {
                                System.out.println("ERROR: no id for concept properties: " + conceptProperties.toString());
                                errorReport.add("ERROR: no id for concept properties: " + conceptProperties.toString());
                            }
                        }
                    }
                }

                List<Map<String, Object>>  propertyConcepts = (List<Map<String, Object>>)conceptsJsonLD.get(PROPERTY_CONCEPTS_TAG);

                if (propertyConcepts != null)
                {
                    for (Map<String, Object> propertyProperties : propertyConcepts)
                    {
                        if (propertyProperties != null)
                        {
                            String              propertyId          = getStringValue(propertyProperties.get(AT_ID_TAG));
                            PropertyDescription propertyDescription = this.getPropertyDescription(propertyId);

                            Object        domainNamesObject = propertyProperties.get(DOMAIN_TAG);
                            List<String>  domainNames = new ArrayList<>((List<String>)domainNamesObject);

                            if (! domainNames.isEmpty())
                            {
                                for (String domainName : domainNames)
                                {
                                    if (domainName != null)
                                    {
                                        Concept concept = getConcept(domainName);

                                        if (propertyDescription.isPrimitive())
                                        {
                                            concept.setAttribute(propertyId, new Attribute(propertyDescription));
                                            System.out.println("       ==> Property name: " + propertyId + " is attribute");
                                        }
                                        else
                                        {
                                            Link link = new Link(propertyDescription);

                                            link.setDomainConceptId(concept.getId());
                                            concept.setDomainOfLink(propertyId, link);

                                            System.out.println("       ==> Property name: " + propertyId + " is link from " + link.getDomainConceptId() + " to " + link.getRangeConceptId());

                                            if (propertyDescription.getDataTypeId() != null)
                                            {
                                                Concept rangeConcept = this.getConcept(
                                                        propertyDescription.getDataTypeId());

                                                rangeConcept.setRangeOfLink(link.getRangeConceptId(), link);
                                            }
                                            else
                                            {
                                                System.out.println("ERROR: no range for property name: " + propertyId);
                                                errorReport.add("ERROR: no range for property name: " + propertyId);
                                            }
                                        }
                                    }
                                }
                            }
                            else
                            {
                                System.out.println("ERROR: no domains for property name: " + propertyId);
                                errorReport.add("ERROR: no domains for property name: " + propertyId);
                            }
                        }
                    }
                }
            }

            File schemaJsonLDFile = new File(entityGroupDirectoryName + SCHEMA_FILE_NAME);
            LinkedHashMap<String, Object> schemaJsonLD = (LinkedHashMap<String, Object>) JsonUtils.fromInputStream(new FileInputStream(schemaJsonLDFile));

            if ((modelLocation.equals(getStringValue(schemaJsonLD.get(AT_CONTEXT_TAG)))) &&
                        (ENTITY_GROUP_TYPE.equals(getStringValue(schemaJsonLD.get(AT_TYPE_TAG)))) &&
                        (entityGroupName.equals(getStringValue(schemaJsonLD.get(AT_ID_TAG)))))
            {
                List<Map<String, Object>>  schemas = (List<Map<String, Object>>)schemaJsonLD.get(SCHEMAS_TAG);

                if (schemas != null)
                {
                    for (Map<String, Object> conceptSchema : schemas)
                    {
                        String    conceptId = getStringValue(conceptSchema.get(AT_ID_TAG));
                        if (conceptId != null)
                        {
                            Concept concept = this.getConcept(conceptId);
                            List<Map<String, Object>> inheritedProperties =
                                    (List<Map<String, Object>>) conceptSchema.get(AND_TAG);

                            if (inheritedProperties == null)
                            {
                                this.getPropertySchemas(concept,
                                                        null,
                                                        (List<Map<String, Object>>) conceptSchema.get(PROPERTIES_TAG));
                            }
                            else
                            {
                                List<Map<String, Object>> properties   = null;
                                String                    superClassId = null;

                                for (Map<String, Object> optionValues : inheritedProperties)
                                {
                                    if (optionValues.get(PROPERTIES_TAG) != null)
                                    {
                                        properties = (List<Map<String, Object>>) optionValues.get(PROPERTIES_TAG);
                                    }
                                    else if (optionValues.get(AT_ID_TAG) != null)
                                    {
                                        superClassId = getStringValue(optionValues.get(AT_ID_TAG));
                                    }
                                    else
                                    {
                                        System.out.println("ERROR: no recognised options for concept: " + conceptId);
                                        errorReport.add("ERROR: no recognised options for concept: " + conceptId);
                                    }
                                }

                                this.getPropertySchemas(concept, superClassId, properties);
                            }
                        }
                        else
                        {
                            System.out.println("ERROR: no id for concept" + conceptSchema.toString());
                            errorReport.add("ERROR: no id for concept" + conceptSchema.toString());
                        }
                    }
                }
            }

            /*
             * Review concepts and determine which are relationships.  A relationship is the domain of 2 links
             * and the range of none.
             */
            for (Concept concept: conceptBeadMap.values())
            {
                if (concept != null)
                {
                    if (concept.getRangeOfLinks() == null)
                    {
                        List<Link>  domainOfLinks = concept.getDomainOfLinks();

                        if (domainOfLinks != null)
                        {
                            if (domainOfLinks.size() == 2)
                            {
                                concept.setRelationship(true);
                                System.out.println("Concept: " + concept.getId() + " is a relationship linking two concepts: " + domainOfLinks.get(0).getId() + " and " + domainOfLinks.get(1).getId());
                            }
                            else
                            {
                                System.out.println("Concept: " + concept.getId() + " is a master-detail record");
                            }
                        }
                        else
                        {
                            System.out.println("Concept: " + concept.getId() + " is isolated - it is not linked to anything" );
                        }
                    }
                    else
                    {
                        System.out.println("Concept: " + concept.getId() + " is linked to by other concepts" );
                    }
                }
            }
        }
    }


    /**
     * Augment the information about the properties of the content with types and cardinalities.
     *
     * @param concept concept to augment
     * @param superClassId its super class
     * @param propertiesSchema schema content
     */
    private void getPropertySchemas(Concept                    concept,
                                    String                     superClassId,
                                    List<Map<String, Object>>  propertiesSchema)
    {
        List<String>  superClassPropertyNames = new ArrayList<>();

        if (superClassId != null)
        {
            Concept superClass = this.getConcept(superClassId);

            List<String> propertyNames = superClass.getAttributeNames();

            if (propertyNames != null)
            {
                superClassPropertyNames.addAll(propertyNames);
            }

            propertyNames = superClass.getDomainOfLinkNames();

            if (propertyNames != null)
            {
                superClassPropertyNames.addAll(propertyNames);
            }

            propertyNames = superClass.getRangeOfLinkNames();

            if (propertyNames != null)
            {
                superClassPropertyNames.addAll(propertyNames);
            }
        }

        for (Map<String, Object> propertyProperties : propertiesSchema)
        {
            if (propertyProperties != null)
            {
                String              propertyId = getStringValue(propertyProperties.get(PATH_TAG));
                PropertyDescription propertyDescription = this.getPropertyDescription(propertyId);
                Property            property;
                String              node = getStringValue(propertyProperties.get(NODE_TAG));
                String              dataType = getStringValue(propertyProperties.get(DATA_TYPE_TAG));
                String              minCount = getStringValue(propertyProperties.get(MIN_COUNT_TAG));
                String              maxCount = getStringValue(propertyProperties.get(MAX_COUNT_TAG));

                if (superClassPropertyNames.contains(propertyId))
                {
                    System.out.println("WARN: Skipping superclass property: " + propertyId);
                }
                else
                {
                    if (propertyDescription.isPrimitive())
                    {
                        if (node != null)
                        {
                            System.out.println("ERROR: Node: " + node + " specified for: " + propertyId + " in concept: " + concept.getId());
                            errorReport.add("ERROR: Node: " + node + " specified for: " + propertyId + " in concept: " + concept.getId());
                        }

                        if (dataType == null)
                        {
                            System.out.println("ERROR: No datatype specified for: " + propertyId + " in concept: " + concept.getId());
                            errorReport.add("ERROR: No datatype specified for: " + propertyId + " in concept: " + concept.getId());
                        }

                        Attribute attribute = concept.getAttribute(propertyId);

                        if (attribute == null)
                        {
                            System.out.println("ERROR: Attribute: " + propertyId + " not known to concept: " + concept.getId());
                            errorReport.add("ERROR: Attribute: " + propertyId + " not known to concept: " + concept.getId());

                            concept.setAttribute(propertyId, new Attribute(propertyDescription));

                            attribute = concept.getAttribute(propertyId);
                        }

                        attribute.setDataType(getStringValue(propertyProperties.get(DATA_TYPE_TAG)));

                        if (attribute.getDataType() == null)
                        {
                            System.out.println("ERROR: No data type for: " + propertyId + " in concept: " + concept.getId());
                            errorReport.add("ERROR: No data type for: " + propertyId + " in concept: " + concept.getId());
                        }

                        property = attribute;
                    }
                    else
                    {
                        if (dataType != null)
                        {
                            System.out.println("ERROR: Datatype: " + dataType + " specified for: " + propertyId + " in concept: " + concept.getId());
                            errorReport.add("ERROR: Datatype: " + dataType + " specified for: " + propertyId + " in concept: " + concept.getId());
                        }

                        if (node == null)
                        {
                            System.out.println("ERROR: No node specified for: " + propertyId + " in concept: " + concept.getId());
                            errorReport.add("ERROR: No node specified for: " + propertyId + " in concept: " + concept.getId());
                        }

                        Link link = concept.getDomainLink(propertyId);

                        if (link == null)
                        {
                            System.out.println("WARN: Link: " + propertyId + " not in domain of concept: " + concept.getId());
                            errorReport.add("WARN: Link: " + propertyId + " not in domain of concept: " + concept.getId());

                            link = concept.getRangeLink(propertyId);

                            if (link == null)
                            {
                                System.out.println("ERROR: Link: " + propertyId + " not known to concept: " + concept.getId());
                                errorReport.add("ERROR: Link: " + propertyId + " not known to concept: " + concept.getId());

                                concept.setDomainOfLink(propertyId, new Link(propertyDescription));
                            }
                        }

                        property = link;
                    }

                    if (property != null)
                    {
                        if (maxCount == null)
                        {
                            property.setSingleton(true);
                        }
                        else
                        {
                            property.setSingleton(false);
                        }

                        if (minCount == null)
                        {
                            property.setOptional(true);
                        }
                        else
                        {
                            property.setOptional(false);
                        }
                    }
                    else
                    {
                        System.out.println("ERROR: Skipping property because content is invalid: " + propertyId);
                        errorReport.add("ERROR: Skipping property because content is invalid: " + propertyId);
                    }
                }
            }
        }
    }


    /**
     * Assemble all of the discovered content and return it to the caller.
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
        model.setConceptBeadMap(conceptBeadMap);

        model.setErrorReport(errorReport);

        return model;
    }
}
