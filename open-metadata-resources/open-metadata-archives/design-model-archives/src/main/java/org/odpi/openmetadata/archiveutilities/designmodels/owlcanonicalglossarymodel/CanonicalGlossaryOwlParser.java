/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel;

import org.apache.jena.rdf.model.*;
import org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel.properties.GlossaryModel;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;


/**
 * CanonicalGlossaryOwlParser reads the CanonicalGlossaryOwlParserModel and parses it into a set of Java Beans
 * ready for the archive builder.  It hides the format of the model from the builder.
 */
class CanonicalGlossaryOwlParser {
    // JSON-LD keyword
    private static String JSONLD = "JSON-LD";

    // maps of json ld content
    Set<String> containerURIs = new HashSet<>();
    Set<String> classURIs = new HashSet<>();
    Set<String> ontologyURIs = new HashSet<>();
    Set<String> objectPropertyURIs = new HashSet<>();
    Set<String> dataPropertyURIs = new HashSet<>();

    // label map
    Map<String, Literal> labelMap = new HashMap<>();
    // comment map
    Map<String, Literal> commentMap = new HashMap<>();
    //license Map
    Map<String, Literal> licenseMap = new HashMap<>();

    Map<String, Set<String>> memberMap = new HashMap<>();

    private List<String> errorReport = new ArrayList<>();

    private GlossaryModel glossaryModel = new GlossaryModel();

    /**
     * Create a parser passing the location of the model.  This may be a single JSON-LD file or the GitHub file structure beginning
     * at either the src directory or the one with the top-level context.jsonld in it.
     *
     * @param modelLocation location of the model content
     */
    CanonicalGlossaryOwlParser(String modelLocation) throws IOException {
        try {
            System.out.println("\nModel file Name: " + modelLocation);

            File modelContent = new File(modelLocation);

            if (modelContent.isDirectory()) {
                System.out.println("\nDo not support directories");
            } else {
                parseSingleFile(modelLocation);
                validate();
                populateModel();
                displaySummary();
            }

            /*
             * Errors found during the processing are added to the error report.
             */
            if (!errorReport.isEmpty()) {
                System.out.println("Error Report");
                System.out.println(errorReport);
            }
        } catch (Throwable error) {
            System.out.println("error is " + error.toString());
        }
    }

    private void validate() {
        if (ontologyURIs != null && ontologyURIs.size() != 1) {
            System.err.println("Please supply a file that contains 1 and only 1 owl:Ontology. Found " + ontologyURIs.size());
            System.exit(-1);
        }
        Map<String, String> isDefinedByMap = glossaryModel.getIsDefinedByMap();

        if (isDefinedByMap != null) {
            int glossaryContentCount = 0;
            if (classURIs != null) {
                glossaryContentCount = classURIs.size();
            }
            if (objectPropertyURIs != null) {
                glossaryContentCount += objectPropertyURIs.size();
            }
            if (dataPropertyURIs != null) {
                glossaryContentCount += dataPropertyURIs.size();
            }
            if (containerURIs != null) {
                glossaryContentCount += containerURIs.size();
            }
            // TODO remember when we account for classifications (which come through as containers) then we need to amend this check
            if (glossaryContentCount != isDefinedByMap.keySet().size()) {
                System.err.println("Expecting the number of relationships (" + isDefinedByMap.keySet().size() + ")  and glossary elements (" +
                                           glossaryContentCount + ") to be equal.");
                System.exit(-1);
            }
        }
    }


    /**
     * Parse the supplied file using apache Jena into maps containing json-ld content.
     *
     * @param fileName location of the model content
     */
    @SuppressWarnings("unchecked")
    private void parseSingleFile(String fileName) throws IOException {
        System.out.println("\nRetrieving model contents from: " + fileName);

        org.apache.jena.rdf.model.Model m = ModelFactory.createDefaultModel();

        Reader fileReader = new FileReader(fileName);
        org.apache.jena.rdf.model.Model model = m.read(fileReader, null, JSONLD);
        StmtIterator it = model.listStatements();

        while (it.hasNext()) {
            Statement statement = it.next();
            Resource s = statement.getSubject();
            org.apache.jena.rdf.model.Property p = statement.getPredicate();
            RDFNode o = statement.getObject();
            String subjectURI = s.getURI();
            String predicate = p.getLocalName();

            switch (predicate) {
                case "type":
                    if (o.isURIResource()) {

                        String object = o.toString();
                        switch (object) {
                            case "http://www.w3.org/2002/07/owl#Ontology":
                                ontologyURIs.add(subjectURI);
                                break;
                            case "http://www.w3.org/2002/07/owl#Class":
                                classURIs.add(subjectURI);
                                break;
                            case "http://www.w3.org/2000/01/rdf-schema#Container":
                                containerURIs.add(subjectURI);
                                break;
                            case "http://www.w3.org/2002/07/owl#ObjectProperty":
                                objectPropertyURIs.add(subjectURI);
                                break;
                            case "http://www.w3.org/2002/07/owl#DatatypeProperty":
                                dataPropertyURIs.add(subjectURI);
                                break;
                        }
                    } else if (o.isResource()) {
                        // should not happen
                        System.out.println("Not expecting a non uri resource subject:" + subjectURI + ",object: " + o.asResource());

                    } else if (o.isLiteral()) {
                        // does not happen
                        System.out.println("Not expecting type literal");
                    }
                    break;
                case "member":
                    Set<String> containerURIs = glossaryModel.getConceptTermMemberMap().get(subjectURI);
                    if (containerURIs == null) {
                        containerURIs = new HashSet();
                    }
                    containerURIs.add(o.asResource().getURI());
                    memberMap.put(subjectURI, containerURIs);
                    break;
                case "example":
                    Map<String, Set<String>> examplesMap = glossaryModel.getExampleMap();
                    Set<String> examples = examplesMap.get(subjectURI);
                    if (examples == null) {
                        examples = new HashSet();
                    }
                    examples.add((String) o.asLiteral().getValue());
                    examplesMap.put(subjectURI, examples);
                    glossaryModel.setExampleMap(examplesMap);
                    break;
                case "subClassOf":
                    if (o.isResource()) {
                        glossaryModel.getResourceSubClassMap().put(subjectURI, o.asResource().getURI());
                    } else if (o.isLiteral()) {
                        glossaryModel.getLiteralSubClassMap().put(subjectURI, o.asLiteral().getValue().toString());
                    }
                    break;
                case "domain":
                    Map<String, Set<String>> domainsMap = glossaryModel.getDomainsMap();
                    Set<String> domains = domainsMap.get(subjectURI);
                    boolean createDomainsMap = false;
                    if (domains == null) {
                        domains = new HashSet();
                        createDomainsMap = true;
                    }
                    if (o.isResource()) {
                        domains.add(o.asResource().getURI());
                        domainsMap.put(subjectURI, domains);
                        if (createDomainsMap) {
                            glossaryModel.setDomainsMap(domainsMap);
                        }
                    }
                    break;
                case "range":
                    if (o.isResource()) {
                        String rangeURI = o.asResource().getURI();
                        /*
                         * the ranges we are concerned with are those that have URIs pointing to classes.
                         * this check ensure we do not pick up xsd content . Note there are more than one xsd identifier used produced on
                         * different dates, so we are just checking the root URI.
                         */
                        if (!rangeURI.startsWith("http://www.w3.org/")) {
                            Map<String, Set<String>> rangesMap = glossaryModel.getRangesMap();
                            Set<String> ranges = rangesMap.get(subjectURI);
                            boolean createRangesMap = false;
                            if (ranges == null) {
                                ranges = new HashSet();
                                createRangesMap = true;
                            }
                            ranges.add(o.asResource().getURI());
                            rangesMap.put(subjectURI, ranges);
                            if (createRangesMap) {
                                glossaryModel.setRangesMap(rangesMap);
                            }
                        }
                    }
                    break;
                case "label":
                    labelMap.put(subjectURI, o.asLiteral());
                    break;
                case "comment":
                    commentMap.put(subjectURI, o.asLiteral());
                    break;
                case "isDefinedBy":
                    glossaryModel.getIsDefinedByMap().put(subjectURI, o.asResource().getURI());
                    break;
                case "license":
                    licenseMap.put(subjectURI, o.asLiteral());
                    break;
                // the following are recognised but are not supported at this time
                case "versionInfo":
                case "minCardinality":
                case "maxCardinality":
                    break;
            }
        }
    }

    private void displaySummary() {

        System.out.println("Found " + glossaryModel.getGlossaryLiteralMap().keySet().size() + " glossaries");
        System.out.println("Found " + glossaryModel.getCategoryLiteralMap().keySet().size() + " categories");
        System.out.println("Found " + glossaryModel.getConceptTermLiteralMap().keySet().size() + " concept terms");
        System.out.println("Found " + glossaryModel.getObjectPropertyTermLiteralMap().keySet().size() + "object property terms");
        Map<String, Set<String>> memberMap = glossaryModel.getConceptTermMemberMap();
        Map<String, String> resourceSubClassMap = glossaryModel.getResourceSubClassMap();
        Map<String, String> literalSubClassMap = glossaryModel.getLiteralSubClassMap();
        Map<String, Set<String>> rangesMap = glossaryModel.getRangesMap();
        Map<String, Set<String>> domainsMap = glossaryModel.getDomainsMap();
        Map<String, String> isDefinedByMap = glossaryModel.getIsDefinedByMap();

        if (resourceSubClassMap == null) {
            System.out.println("No resourceSubClassMap");
        } else {
            System.out.println("Found " + resourceSubClassMap.keySet().size() + " resourceSubclasses");
        }
        if (literalSubClassMap == null) {
            System.out.println("No literalSubClassMap");
        } else {
            System.out.println("Found " + literalSubClassMap.keySet().size() + " literalSubClassMap");
        }
        if (rangesMap == null) {
            System.out.println("No ranges");
        } else {
            System.out.println("Found " + rangesMap.keySet().size() + " rangesMap");
        }
        if (domainsMap == null) {
            System.out.println("No domains");
        } else {
            System.out.println("Found " + domainsMap.keySet().size() + " domainsMap");
        }
        if (isDefinedByMap == null) {
            System.out.println("No is defined by");
        } else {
            System.out.println("Found " + isDefinedByMap.keySet().size() + " is defined by Map");
        }
        if (memberMap == null) {
            System.out.println("No members");
        } else {
            System.out.println("Found " + memberMap.keySet().size() + " members Map");
        }

        // we can create an archive from Glossaries, Categories, concept terms (spine Object) and property terms (Spine Attribute) and their label and comments
        // Glossary anchor relationships from isDefined by
        // rdf domain relationship is  has-a.
        // is a from class to class subClass
        // is a type of from property to class subclass
        // for a property term that has a domain that is a resource then this is a has-a
        // for a property term that has a domain that is a literal ignoring for now (or put in additional properties?)
        // synonym ?
        // example
        // related term for other non has-a relationships between terms ?


        // classification ???
        // other relationships
        // subject area content


    }

    private void populateModel() {
        String ontologyURI = ontologyURIs.iterator().next();
        glossaryModel.setModelTechnicalName(ontologyURI);


        // TODO get from tags
        glossaryModel.setModelLanguage("us_en");

        // this covers comment , label and class. Additional properties for unknowns?

        // Member map include mapping of property term to the propertyClassification = spine attribute
        // rdfs:class is a spine object


        // Range has 2 types of entries
        // primatives and relationships
        processLabels();
        processComments();
        processLicense();
        processMembers();

        // TODO get from tags
        glossaryModel.setModelLanguage("us_en");

        Literal glossaryDisplayNameLiteral = glossaryModel.getGlossaryLiteralMap().get(ontologyURI).get("displayName");
        glossaryModel.setModelName((String) glossaryDisplayNameLiteral.getValue());

        Literal glossaryDescriptionLiteral = glossaryModel.getGlossaryLiteralMap().get(ontologyURI).get("description");
        glossaryModel.setModelDescription((String) glossaryDescriptionLiteral.getValue());

        Literal glossaryLicenseLiteral = glossaryModel.getGlossaryLiteralMap().get(ontologyURI).get("license");
        glossaryModel.setLicense((String) glossaryLicenseLiteral.getValue());
    }

    private void processMembers() {
        // split out the members
        for (String member : memberMap.keySet()) {
            if (containerURIs.contains(member)) {
                glossaryModel.getContainmentMemberMap().put(member, memberMap.get(member));
            }
            if (classURIs.contains((member))) {
                glossaryModel.getConceptTermMemberMap().put(member, memberMap.get(member));
            }
            if (objectPropertyURIs.contains(member)) {
                glossaryModel.getPropertyTermMemberMap().put(member, memberMap.get(member));
            }
        }
    }

    private void processLicense() {
        for (String subject : licenseMap.keySet()) {
            if (ontologyURIs.contains(subject)) {
                // found an ontology = Egeria glossary
                Map<String, Map<String, Literal>> glossaryLiteralMap = glossaryModel.getGlossaryLiteralMap();
                Map<String, Literal> literalMap = glossaryLiteralMap.get(subject);
                literalMap = setLicense(subject, literalMap);
                glossaryLiteralMap.put(subject, literalMap);

            }
        }
    }

    private void processComments() {
        for (String subject : commentMap.keySet()) {
            if (ontologyURIs.contains(subject)) {
                // found an ontology = Egeria glossary
                Map<String, Map<String, Literal>> glossaryLiteralMap = glossaryModel.getGlossaryLiteralMap();
                Map<String, Literal> literalMap = glossaryLiteralMap.get(subject);
                literalMap = setDescription(subject, literalMap);
                glossaryLiteralMap.put(subject, literalMap);

            }
            if (containerURIs.contains(subject)) {
                // found a container = Egeria Category
                Map<String, Map<String, Literal>> categoryLiteralMap = glossaryModel.getCategoryLiteralMap();
                Map<String, Literal> literalMap = categoryLiteralMap.get(subject);
                literalMap = setDescription(subject, literalMap);
                categoryLiteralMap.put(subject, literalMap);
            }
            if (classURIs.contains(subject)) {

                // found a class = Egeria Spine object
                Map<String, Map<String, Literal>> termLiteralMap = glossaryModel.getConceptTermLiteralMap();
                Map<String, Literal> literalMap = termLiteralMap.get(subject);
                literalMap = setDescription(subject, literalMap);
                termLiteralMap.put(subject, literalMap);

            }
            if (objectPropertyURIs.contains(subject)) {
                // found a property = Egeria spine Attribute
                Map<String, Map<String, Literal>> termLiteralMap = glossaryModel.getObjectPropertyTermLiteralMap();
                Map<String, Literal> literalMap = termLiteralMap.get(subject);
                literalMap = setDescription(subject, literalMap);
                termLiteralMap.put(subject, literalMap);
            }
            if (dataPropertyURIs.contains(subject)) {
                // found a datatype property
                Map<String, Map<String, Literal>> termLiteralMap = glossaryModel.getDatatypePropertyTermLiteralMap();
                Map<String, Literal> literalMap = termLiteralMap.get(subject);
                literalMap = setDescription(subject, literalMap);
                termLiteralMap.put(subject, literalMap);
            }
        }
    }

    private void processLabels() {
        // spin down each label and look at it literal and subject. Subject should match property, container or class

        for (String subject : labelMap.keySet()) {

            if (ontologyURIs.contains(subject)) {
                // found an ontology = Egeria glossary
                Map<String, Map<String, Literal>> glossaryLiteralMap = glossaryModel.getGlossaryLiteralMap();
                Map<String, Literal> literalMap = glossaryLiteralMap.get(subject);
                literalMap = setDisplayName(subject, literalMap);
                glossaryLiteralMap.put(subject, literalMap);
            }
            if (containerURIs.contains(subject)) {
                // found a container = Egeria Category
                Map<String, Map<String, Literal>> categoryLiteralMap = glossaryModel.getCategoryLiteralMap();
                Map<String, Literal> literalMap = categoryLiteralMap.get(subject);
                literalMap = setDisplayName(subject, literalMap);
                categoryLiteralMap.put(subject, literalMap);
            }
            if (classURIs.contains(subject)) {
                // found a class = Egeria Spine object
                Map<String, Map<String, Literal>> termLiteralMap = glossaryModel.getConceptTermLiteralMap();
                Map<String, Literal> literalMap = termLiteralMap.get(subject);
                literalMap = setDisplayName(subject, literalMap);
                termLiteralMap.put(subject, literalMap);
            }
            if (objectPropertyURIs.contains(subject)) {
                // found a object property
                Map<String, Map<String, Literal>> termLiteralMap = glossaryModel.getObjectPropertyTermLiteralMap();
                Map<String, Literal> literalMap = termLiteralMap.get(subject);
                literalMap = setDisplayName(subject, literalMap);
                termLiteralMap.put(subject, literalMap);
            }
            if (dataPropertyURIs.contains(subject)) {
                // found a datatype property
                Map<String, Map<String, Literal>> termLiteralMap = glossaryModel.getDatatypePropertyTermLiteralMap();
                Map<String, Literal> literalMap = termLiteralMap.get(subject);
                literalMap = setDisplayName(subject, literalMap);
                termLiteralMap.put(subject, literalMap);
            }
        }
    }

    private Map<String, Literal> setDisplayName(String subject, Map<String, Literal> literalMap) {
        if (literalMap == null) {
            literalMap = new HashMap<>();
        }
        Literal literal = labelMap.get(subject);
        literalMap.put("displayName", literal);
        return literalMap;
    }

    private Map<String, Literal> setDescription(String subject, Map<String, Literal> literalMap) {
        if (literalMap == null) {
            literalMap = new HashMap<>();
        }
        Literal literal = commentMap.get(subject);
        literalMap.put("description", literal);
        return literalMap;
    }

    private Map<String, Literal> setLicense(String subject, Map<String, Literal> literalMap) {
        if (literalMap == null) {
            literalMap = new HashMap<>();
        }
        Literal literal = licenseMap.get(subject);
        literalMap.put("license", literal);
        return literalMap;
    }


    /**
     * Return the discovered content to the caller.
     *
     * @return model object with the model content contained in nested beans.
     */
    public GlossaryModel getModel() {
        glossaryModel.setErrorReport(errorReport);
        return glossaryModel;
    }
}
