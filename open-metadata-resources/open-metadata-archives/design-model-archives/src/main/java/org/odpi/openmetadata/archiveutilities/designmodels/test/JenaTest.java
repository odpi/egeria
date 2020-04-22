/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.test;


import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

import org.apache.jena.shared.PrefixMapping;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * <ul>
 * <li>the "is A" custom relationship that we use is now transformed to subclassof in OWL
 * license, name and description added to owl:ontology</li>
 * <li>all classes and properties now have rdf:definedBy link to owl:ontology.</li>
 * <li>new area of glossaries is included: analytical requirement and measure terms -
 *  put into OWL as classes and properties.
 *  links to concept terms are created as</li>
 * <li> object properties of analytical requirement classes</li>
 * </ul>
 *
 * member
 *
 * Example
 *          "rdfs:label": {
 *             "@language": "en",
 *             "@value": "Customer"
 *          },
 *          "rdfs:comment": {
 *             "@language": "en",
 *             "@value": "The household or organization that receives services from a provider, including all past and potential future customers of the organization."
 *          },
 *          "rdfs:member": [
 *             {
 *                "@id": "energyandutilities:CustomerCategory"
 *             },
 *             {
 *                "@id": "energyandutilities:ConceptClassification"
 *             }
 *          ],
 *          "rdfs:subClassOf": [
 *             {
 *                "@id": "energyandutilities:PartyConcept"
 *             }
 *          ],
 *          "rdfs:isDefinedBy": {
 *             "@id": "energyandutilities:"
 *          }
 */

public class JenaTest {
    //public static String fileName = "/Users/davidradley/Downloads/Healthcare-OWL-2020-03-03.json";
    public static String fileName = "/Users/davidradley/Downloads/EnergyAndUtilities-2020-04-08-Ontology.json";

//    public static String fileName = "/Users/davidradley/Downloads/Healthcare-2020-03-03-RDF.json";
    public static void  main (String args[]) throws IOException {
//        test1();
//        test2();
  //      test3();
//        test4();
//        test5();
       test6();

    }

    private static void test6() throws FileNotFoundException {


            Model m = ModelFactory.createDefaultModel();

            Reader fileReader = new FileReader(fileName);
            Model model = m.read(fileReader, null, "JSON-LD");
            StmtIterator it = model.listStatements();
            Set<String> containerNames = new HashSet<>();
            Set<String> classNames = new HashSet<>();
            Set<String> ontologyNames = new HashSet<>();
            Set<String> propertyNames = new HashSet<>();
            // member map - containment relationships for categories and classifications
            Map<String, String> memberMap = new HashMap<>();
            // subclasses
            Map<String, String> NoParentConceptTermMap = new HashMap<>();
            Map<String, String> resourceSubClassMap = new HashMap<>();

            // range
             Map<String, String> rangeMap = new HashMap<>();
             // isDefined by -anchors
             Map<String, String>  isDefinedByMap = new HashMap<>();
            // domain property to owning concept
             Map<String, String> domainMap = new HashMap<>();
             // label map
            Map<String,Literal> labelMap = new HashMap<>();
            // comment map
            Map<String,Literal> commentMap = new HashMap<>();

            while (it.hasNext()) {
                Statement statement = it.next();
                Resource s = statement.getSubject();
                Property p = statement.getPredicate();
                RDFNode o = statement.getObject();
                String subjectURI = s.getURI();
                String predicate = p.getLocalName();

                if ("type".equals(predicate)) {
                    if (o.isURIResource()) {
                        if (o.toString().endsWith("#Ontology")) {
                            ontologyNames.add(s.getURI());
                            System.out.println("Ontology URI "+ s.getURI() );
                        } else if (o.toString().endsWith("#Class")) {
                            classNames.add(subjectURI);
                        } else if (o.toString().endsWith("#Container")) {
                            containerNames.add(subjectURI);
                        } else if (o.toString().endsWith("#property")) {
                            propertyNames.add(subjectURI);
                        }
                    } else if (o.isResource()) {
                        // should not happen
                        System.out.println("Not expecting a non uri resource subject:" + subjectURI + ",object: " +o.asResource());

                    } else if (o.isLiteral()) {
                        // does not happen
                        System.out.println("Not expecting type literal");
                   }
                } else if ("member".equals(predicate)) {
                    memberMap.put(subjectURI,o.asResource().getLocalName());
                } else if ("subClassOf".equals(predicate)) {
                    if (o.isResource()) {
                        resourceSubClassMap.put(subjectURI, o.asResource().getLocalName());
                    } else if (o.isLiteral()) {
                        NoParentConceptTermMap.put(subjectURI, o.asLiteral().getValue().toString());
                    }
                }  else if ("domain".equals(predicate)) {
                    domainMap.put(subjectURI,o.asResource().getLocalName());
                } else if ("range".equals(predicate)) {
                    rangeMap.put(subjectURI,o.asResource().getLocalName());
                } else if ("label".equals(predicate)) {
//                    if ("Energy and Utilities Business Vocabulary".equals(o.asLiteral())) {
//                        System.out.println("Stoooop!!!");
//                    }
//                    if (subjectURI.equals("")) {
//                        System.out.println("Stoooop!!!");
//                    }
                    labelMap.put(subjectURI,o.asLiteral());
                } else if ("comment".equals(predicate)) {
                    commentMap.put(subjectURI,o.asLiteral());
                } else if ("isDefinedBy".equals(predicate)){
                    isDefinedByMap.put(subjectURI,o.asResource().getLocalName());
                } else if ("license".equals(predicate)){
                    System.out.println("Found license ");

                } else if ("versionInfo".equals(predicate)){

                } else if ("maxCardinality".equals(predicate)){
                    // not supported yet
                } else if ("minCardinality".equals(predicate)){
                    // not supported yet
                } else {
                    System.out.println("Not expecting predicate " + predicate);
                }
            }

            // derived maps.
        // this covers comment , label and class. Additional properties for unknowns?

        // TODO domain map already maps concept to property terms - HAS-A
        // Member map include mapping of property term to the propertyClassification = spine attribute
        // rdfs:class is a spine object


        // Range has 2 types of entries
        // primatives and relationships
        Map<String, Map<String, Literal>> propertyTermLiteralMap = new HashMap<>(); // rdfs:property
        Map<String, Map<String, Literal>> glossaryLiteralMap = new HashMap<>();    // Owl:Ontology
        Map<String, Map<String, Literal>> categoryLiteralMap = new HashMap<>();    // rdf:container
        Map<String, Map<String, Literal>> conceptTermLiteralMap = new HashMap<>();  // rdfs:class

        // spin down each label and look at it literal and subject. Subject should match property, container or class

        for (String subject:labelMap.keySet()) {
            String labelValue = (String) labelMap.get(subject).getValue();
            if ("Energy and Utilities Business Vocabulary".equals(labelValue)) {
                System.out.println("Stoooop!!!");
            }
            if (ontologyNames.contains(subject)) {
                // found an ontology = Egeria glossary
                Map<String, Literal> literalMap = glossaryLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("label",labelMap.get(subject));
                glossaryLiteralMap.put(subject,literalMap);

            }
            if (containerNames.contains(subject)) {
                // found a container = Egeria Category
                Map<String, Literal> literalMap = categoryLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("label",labelMap.get(subject));
                categoryLiteralMap.put(subject,literalMap);

            }
            if (classNames.contains(subject)) {
                // found a class - this is a term
                Map<String, Literal> literalMap = conceptTermLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("label",labelMap.get(subject));
                conceptTermLiteralMap.put(subject,literalMap);
            }
            if (propertyNames.contains(subject)) {
                // found a property
                Map<String, Literal> literalMap = propertyTermLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("label", labelMap.get(subject));
                propertyTermLiteralMap.put(subject,literalMap);
            }
        }

        for (String subject:commentMap.keySet()) {
            if (ontologyNames.contains(subject)) {
                // found an ontology
                Map<String, Literal> literalMap = glossaryLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("comment",labelMap.get(subject));
                glossaryLiteralMap.put(subject,literalMap);

            }
            if (containerNames.contains(subject)) {
                // found a container
                Map<String, Literal> literalMap = categoryLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("comment",labelMap.get(subject));
                categoryLiteralMap.put(subject,literalMap);

            }
            if (classNames.contains(subject)) {
                // found a container
                Map<String, Literal> literalMap = conceptTermLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("comment",labelMap.get(subject));
                conceptTermLiteralMap.put(subject,literalMap);

            }
            if (propertyNames.contains(subject)) {
                // found a container
                Map<String, Literal> literalMap = propertyTermLiteralMap.get(subject);
                if (literalMap == null) {
                    literalMap = new HashMap();
                }
                literalMap.put("comment", labelMap.get(subject));
                propertyTermLiteralMap.put(subject,literalMap);
            }
        }
        System.out.println("Found " + glossaryLiteralMap.keySet().size() + " glossaries");
        System.out.println("Found " + categoryLiteralMap.keySet().size() + " categories");
        System.out.println("Found " + conceptTermLiteralMap.keySet().size() + " concept terms");
        System.out.println("Found " + propertyTermLiteralMap.keySet().size() + " property terms");
        if (resourceSubClassMap == null ) {
            System.out.println("No resourceSubClassMap");
        } else {
            System.out.println("Found " + resourceSubClassMap.keySet().size() + " resourceSubclasses");
        }
        if (NoParentConceptTermMap == null ) {
            System.out.println("No NoParentConceptTermMap");
        } else {
            System.out.println("Found " + NoParentConceptTermMap.keySet().size() + " NoParentConceptTermMap");
        }
        if (rangeMap == null) {
            System.out.println("No ranges");
        } else {
            System.out.println("Found " + rangeMap.keySet().size() + " rangeMap");
        }
        if (domainMap == null) {
            System.out.println("No domains");
        } else {
            System.out.println("Found " + domainMap.keySet().size() + " domainMap");
        }
        if (isDefinedByMap == null ) {
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

    private static void test5() throws FileNotFoundException {

        Model m = ModelFactory.createDefaultModel();

        Reader fileReader = new FileReader(fileName);
        Model model = m.read(fileReader, null, "JSON-LD");
        StmtIterator it = model.listStatements();
        Set<String> typesSet = new HashSet<>();


        Set<String> containerNames = new HashSet<>();
        Set<String> classNames = new HashSet<>();
        // this covers comment , label and class. Additional properties for unknowns?
        Map<String, Map<String,Literal>> TermPropertyMap = new HashMap<>();



        System.out.println("Labels");
        while (it.hasNext()) {
            Statement statement = it.next();
            Resource s = statement.getSubject();
            Property p = statement.getPredicate();
            RDFNode o = statement.getObject();
            String subjectLocalName = s.getLocalName();
            String predicate = p.getLocalName();
//            if (subjectLocalName.equals("PartyProfessionalServiceAreaConcept")) {
            if ("type".equals(predicate) && o.toString().endsWith("#container" )) {
                System.out.println("subject : " + subjectLocalName + " predicate = " + p.getLocalName() + " object:"+o);
            }

            String subjectandpredicate = " , subject=" + subjectLocalName + " ,predicate=" + p.getLocalName();

            if (o.isLiteral()) {

                if ("label".equals(predicate)) {
                    System.out.println("PropertyTerm " + subjectLocalName + " :: Name(label)=" + o.asLiteral());
                } else if ("comment".equals(predicate)) {
                    System.out.println("Term " + subjectLocalName + " :: Description(comment)=" + o.asLiteral());
                } else if ("maxCardinality".equals(predicate)) {
                    // Ignore
                } else if ("minCardinality".equals(predicate)) {
                    //ignore
                } else {
                    //System.out.println("literal predicate =" + predicate);
                }
            } else if (o.isURIResource()) {
                if ("domain".equals(predicate)) {
                    System.out.println("domain: " + subjectLocalName + " is a property of " + o.asResource().getLocalName());
                } else if ("range".equals(predicate)) {
                    System.out.println("range: " + subjectLocalName + " is an instanceof class " + o.asResource().getLocalName());
                } else if ("type".equals(predicate)) {
                    typesSet.add(subjectLocalName);
                    if (subjectLocalName.equals("Classifications")) {
                        System.out.println("type: " + subjectLocalName + " type relationship" + o.asResource().getLocalName());
                    }
               } else if ("member".equals(predicate)) {
                    System.out.println("member: " + subjectLocalName + " member relationship " + o.asResource().getLocalName());
                } else if ("subClassOf".equals(predicate)) {
                    System.out.println("subClassOf: " + subjectLocalName + " is an instanceof class " + o.asResource().getLocalName());
                } else {
                    // does not happen
                    System.out.println("Unrecognised URIresource " + predicate + " =" + o.asResource() + subjectandpredicate);
                }
            } else if (o.isResource()) {
                // does not happen
                System.out.println("Unrecognised resource " + predicate + " =" + o.asResource() + subjectandpredicate);
            } else {
                // does not happen
                System.out.println("Non-literal not resource predicate =" + predicate + subjectandpredicate);
//            }


//
////            if ( p.getLocalName().equals("type")) {
//            System.out.println("statement =" + statement);
//            System.out.println("s =" + s);
//            System.out.println("p =" + p);
//
//            System.out.println("p.getLocalName() =" + p.getLocalName());
//            System.out.println("p.getNameSpace() =" + p.getNameSpace());
//            System.out.println("p.getOrdinal() =" + p.getOrdinal());
//            System.out.println("o =" + o);
//            if (o.isLiteral()) {
//                System.out.println("o.asLiteral =" + o.asLiteral());
//            } else if (o.isResource()) {
//                Resource resource = o.asResource();
//                System.out.println("o.asResource() =" + resource);
//                System.out.println("o.asResource().getLocalName() =" + resource.getLocalName());
//                System.out.println("o.asResource().getNameSpace() =" + resource.getNameSpace());
////                    System.out.println("o.asResource().getName() =" + resource.getId());
//                if (resource.getLocalName().equals("member")) {
//                    System.out.println("found member");
//                }
//                if (o.isURIResource()) {
//                    System.out.println("o.isURIResource()  ");
//                }
//
//                // System.out.println("o.asResource().getNameURI() =" + o.asResource().getURI());
////                    System.out.println("o.asResource().getPropertyResourceValue() =" + o.asResource().getProperty
////                    System.out.println("o.asResource().getId() =" + o.asResource().getId());
//            } else if (o.isAnon()) {
//                System.out.println("o.isAnon() o.asNode().getName() " + o.asNode().getName());
//
//            }
//            } else if (o.i

//            if (p.getURI().equalsIgnoreCase(
//                    "http://www.w3.org/2004/02/skos/core#broader")
//                    && o.isResource()) {
//                Resource target = o.asResource();
//                if(!hasSubCategory(target.getURI()))
//                    set.add(target.getURI());
//                if(!hasSubCategory(s.getURI()))
//                    set.add(s.getURI());
//            }
            }
        }
//        for (String type:typesSet) {
//            System.out.println("type :"+type );
//        }
    }

//    private static void test4() throws FileNotFoundException {
//        Model m = ModelFactory.createDefaultModel();
//
//        Reader fileReader = new FileReader(fileName) ;
//        Model model = m.read(fileReader, null, "JSON-LD");
//        StmtIterator it = model.node
//        Set<String> set = new HashSet<>();
//
//        System.out.println("Los gehts");
//        while (it.hasNext()) {
//
//        }
//    }

    private static void test3() throws FileNotFoundException {
        Model m = ModelFactory.createDefaultModel();

        Reader fileReader = new FileReader(fileName) ;
        Model model = m.read(fileReader, null, "JSON-LD");
        StmtIterator it = model.listStatements();
        Set<String> set = new HashSet<>();

        System.out.println("Los gehts");
        while (it.hasNext()) {
            Statement s = it.next();
            Resource r = s.getSubject();
            Property p = s.getPredicate();
            RDFNode n = s.getObject();
//            if ( p.getLocalName().equals("type")) {
                System.out.println("s =" + s);
                System.out.println("r =" + r);
                System.out.println("p =" + p);

                System.out.println("p.getLocalName() =" + p.getLocalName());
                System.out.println("p.getNameSpace() =" + p.getNameSpace());
                System.out.println("p.getOrdinal() =" + p.getOrdinal());
                System.out.println("n =" + n);
                if (n.isLiteral()) {
                    System.out.println("n.asLiteral =" + n.asLiteral());
                } else if (n.isResource()){
                    Resource resource =  n.asResource();
                    System.out.println("n.asResource() =" + resource);
                    System.out.println("n.asResource().getLocalName() =" +resource.getLocalName());
                    System.out.println("n.asResource().getNameSpace() =" + resource.getNameSpace());
//                    System.out.println("n.asResource().getName() =" + resource.getId());
                    if (resource.getLocalName().equals("member")){
                        System.out.println("found member");
                    }
                    if (n.isURIResource()) {
                        System.out.println("n.isURIResource()  " );
                    }

                   // System.out.println("n.asResource().getNameURI() =" + n.asResource().getURI());
//                    System.out.println("n.asResource().getPropertyResourceValue() =" + n.asResource().getProperty
//                    System.out.println("n.asResource().getId() =" + n.asResource().getId());
                } else if (n.isAnon()) {
                    System.out.println("n.isAnon() n.asNode().getName() " + n.asNode().getName());

                }
//            } else if (n.i

//            if (p.getURI().equalsIgnoreCase(
//                    "http://www.w3.org/2004/02/skos/core#broader")
//                    && n.isResource()) {
//                Resource target = n.asResource();
//                if(!hasSubCategory(target.getURI()))
//                    set.add(target.getURI());
//                if(!hasSubCategory(r.getURI()))
//                    set.add(r.getURI());
//            }
        }
//        return set;

    }

    private static void test1() throws FileNotFoundException {
        Model m = ModelFactory.createDefaultModel();

        Reader fileReader = new FileReader(fileName) ;
        Model m2 = m.read(fileReader, null, "JSON-LD");
//        Iterator it = m2.listObjects();
//        Iterator it = m2.listStatements();
        Iterator it = m2.listNameSpaces();
        while(it.hasNext()) {
            Object obj = it.next();
            System.out.println(obj);
        }
    }

    private static Model test2() throws IOException {

        org.apache.jena.rdf.model.Model model = ModelFactory.createDefaultModel();
        model.setNsPrefixes(PrefixMapping.Extended.Extended.Standard);
        model.setNsPrefix("xsd", "http://www.w3.org/2001/XMLSchema#");
        model.setNsPrefix("skos", "http://www.w3.org/2004/02/skos/core#");
        model.setNsPrefix("oboInOwl", "http://www.geneontology.org/formats/oboInOwl#");
//        model.setNsPrefixes(prefixes);
        final InputStream is = Files.newInputStream(Paths.get(fileName));
        RDFDataMgr.read(model, is, Lang.RDFXML);
        return model;
    }
}
