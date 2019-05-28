package org.odpi.openmetadata.accessservices.glossaryview.rest;

public class GlossaryViewEntityDetailFactory {

    private final static String GLOSSARY = "Glossary";
    private final static String CATEGORY = "GlossaryCategory";
    private final static String TERM = "GlossaryTerm";

    public static GlossaryViewEntityDetail build(String entityType){
        GlossaryViewEntityDetail prototype = null;
        if(entityType.equalsIgnoreCase(GLOSSARY)){
            prototype = buildDefaultGlossary();
        }
        if(entityType.equalsIgnoreCase(CATEGORY)){
            prototype = buildDefaultCategory();
        }
        if(entityType.equalsIgnoreCase(TERM)){
            prototype = buildDefaultTerm();
        }
        return prototype;
    }

    private static Glossary buildDefaultGlossary(){
        return new Glossary();
    }

    private static Category buildDefaultCategory(){
        return new Category();
    }

    private static Term buildDefaultTerm(){
        return new Term();
    }

}
