/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.http.HttpHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IGCRestModelGenerator {

    private static final Logger log = LoggerFactory.getLogger(IGCRestModelGenerator.class);

    public static final String PKG_NAME = "org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated";

    public static void main(String[] args) {

        if (args.length < 5) {
            System.out.println("Inadequate parameters provided.");
            printUsage();
            System.exit(1);
        }

        HttpHelper.noStrictSSL();

        IGCRestModelGenerator generator = new IGCRestModelGenerator(args[0], args[1], args[2], args[3]);
        generator.generateForAllIgcTypesInEnvironment(args[4]);

    }

    private static void printUsage() {
        System.out.println("Usage: ");
        System.out.println("  IGCRestModelGenerator hostname port username password directory");
    }

    private static final Pattern INVALID_NAMING_CHARS = Pattern.compile("[()/&$\\- ]");
    private Set<String> IGNORE_TYPES;
    private Set<String> IGNORE_PROPERTIES;
    private Set<String> RESERVED_WORDS;
    private Set<String> QUALIFY_PROPERTIES;
    private Map<String, String> NON_UNIQUE_CLASSNAMES;
    private Map<String, String> ALIAS_OBJECTS;
    private Map<String, String> BASIC_TYPE_TO_JAVA_TYPE;

    private JsonNodeFactory nf = JsonNodeFactory.instance;

    private IGCRestClient igcRestClient;

    public IGCRestModelGenerator(String hostname, String port, String username, String password) {
        IGNORE_TYPES = new HashSet<>();
        IGNORE_TYPES.add("main_object");
        IGNORE_TYPES.add("information_asset");
        IGNORE_PROPERTIES = new HashSet<>();
        IGNORE_PROPERTIES.add("_name");
        IGNORE_PROPERTIES.add("_type");
        IGNORE_PROPERTIES.add("_url");
        IGNORE_PROPERTIES.add("_id");
        IGNORE_PROPERTIES.add("_context");
        IGNORE_PROPERTIES.add("notes");
        RESERVED_WORDS = new HashSet<>();
        RESERVED_WORDS.add("package");
        RESERVED_WORDS.add("final");
        RESERVED_WORDS.add("abstract");
        RESERVED_WORDS.add("default");
        QUALIFY_PROPERTIES = new HashSet<>();
        QUALIFY_PROPERTIES.add("name");
        QUALIFY_PROPERTIES.add("type");
        QUALIFY_PROPERTIES.add("url");
        QUALIFY_PROPERTIES.add("id");
        QUALIFY_PROPERTIES.add("context");
        NON_UNIQUE_CLASSNAMES = new HashMap<>();
        NON_UNIQUE_CLASSNAMES.put("valid_value_list", "ValidValueList");
        NON_UNIQUE_CLASSNAMES.put("validvaluelist", "ValidValueList2");
        NON_UNIQUE_CLASSNAMES.put("valid_value_range", "ValidValueRange");
        NON_UNIQUE_CLASSNAMES.put("validvaluerange", "ValidValueRange2");
        NON_UNIQUE_CLASSNAMES.put("parameter_set", "ParameterSet");
        NON_UNIQUE_CLASSNAMES.put("parameterset", "ParameterSet2");
        NON_UNIQUE_CLASSNAMES.put("function_call", "FunctionCall");
        NON_UNIQUE_CLASSNAMES.put("functioncall", "FunctionCall2");
        ALIAS_OBJECTS = new HashMap<>();
        ALIAS_OBJECTS.put("host_(engine)", "host");
        BASIC_TYPE_TO_JAVA_TYPE = new HashMap<>();
        BASIC_TYPE_TO_JAVA_TYPE.put("string", "String");
        BASIC_TYPE_TO_JAVA_TYPE.put("boolean", "Boolean");
        BASIC_TYPE_TO_JAVA_TYPE.put("datetime", "Date");
        BASIC_TYPE_TO_JAVA_TYPE.put("number", "Number");
        BASIC_TYPE_TO_JAVA_TYPE.put("enum", "String");

        igcRestClient = new IGCRestClient(
                "https://" + hostname + ":" + port,
                IGCRestClient.encodeBasicAuth(username, password)
        );

    }

    public void generateForAllIgcTypesInEnvironment(String directory) {

        System.out.println("Generating POJOs for IGC version: " + igcRestClient.getIgcVersion());
        ArrayNode types = igcRestClient.getTypes();
        for (int i = 0; i < types.size(); i++) {
            String type = types.get(i).path("_id").asText();
            JsonNode properties = igcRestClient.makeRequest(
                    igcRestClient.getBaseURL() + "/ibm/iis/igc-rest/v1/types/" + type + "?showViewProperties=true&showCreateProperties=true",
                    HttpMethod.GET,
                    MediaType.APPLICATION_JSON,
                    null
            );
            createPOJOForType(properties, directory, igcRestClient.getIgcVersion());
        }
        igcRestClient.disconnect();

    }

    private StringBuilder getPropertyHeading(String propertyId, String propertyDisplayName, JsonNode typeObj, String javaType) {
        StringBuilder sb = new StringBuilder();
        sb.append("    /**");
        sb.append(System.lineSeparator());
        sb.append("     * The '" + propertyId + "' property, displayed as '" + propertyDisplayName + "' in the IGC UI.");
        sb.append(System.lineSeparator());
        String typeName = typeObj.path("name").asText();
        if (typeObj.hasNonNull("url")) {
            sb.append("     * <br><br>");
            sb.append(System.lineSeparator());
            if (javaType.equals("ReferenceList")) {
                sb.append("     * Will be a {@link ReferenceList} of {@link " + getClassName(typeName) + "} objects.");
                sb.append(System.lineSeparator());
            } else if (javaType.equals("Reference")) {
                sb.append("     * Will be a single {@link Reference} to a {@link " + getClassName(typeName) + "} object.");
                sb.append(System.lineSeparator());
            }
        } else if (typeName.equals("enum")) {
            ArrayNode validValues = (ArrayNode) typeObj.path("validValues");
            if (validValues != null && validValues.size() > 0) {
                sb.append("     * <br><br>");
                sb.append(System.lineSeparator());
                sb.append("     * Can be one of the following values:");
                sb.append(System.lineSeparator());
                sb.append("     * <ul>");
                sb.append(System.lineSeparator());
                for (int i = 0; i < validValues.size(); i++) {
                    JsonNode validValue = validValues.get(i);
                    sb.append("     *     <li>" + validValue.path("id").asText() + " (displayed in the UI as '" + validValue.path("displayName").asText() + "')</li>");
                    sb.append(System.lineSeparator());
                }
                sb.append("     * </ul>");
                sb.append(System.lineSeparator());
            }
        }
        sb.append("     */");
        sb.append(System.lineSeparator());
        return sb;
    }

    private PropertyDetail getPropertyDetailForPOJO(String name, JsonNode typeObj, int maxNum, String displayName) {

        PropertyDetail detail = new PropertyDetail();

        String type = typeObj.path("name").asText();
        String nominalType = type;

        if (typeObj.hasNonNull("url")) {
            nominalType = "Reference";
        } else if (BASIC_TYPE_TO_JAVA_TYPE.containsKey(type)) {
            nominalType = BASIC_TYPE_TO_JAVA_TYPE.get(type);
        } else if (!BASIC_TYPE_TO_JAVA_TYPE.containsKey(type)) {
            log.warn("Found unknown type: " + type + " (" + typeObj.toString() + ")");
            return null;
        }

        String javaType = "";
        // When there isn't a maxCardinality specified, there can be multiple;
        // UNLESS the data type is boolean (then there is only one value permitted)
        if (maxNum != 1) {
            if (nominalType.equals("Reference")) {
                javaType = "ReferenceList";
            } else if (nominalType.equals("Boolean")) {
                javaType = nominalType;
            } else {
                javaType = "ArrayList<" + nominalType + ">";
            }
        } else {
            javaType = nominalType;
        }
        detail.setJavaType(javaType);

        String propName = name;
        StringBuilder declMember = getPropertyHeading(name, displayName, typeObj, javaType);
        Matcher m = INVALID_NAMING_CHARS.matcher(propName);
        if (m.find()) {
            propName = m.replaceAll("_");
            declMember.append("    @JsonProperty(\"" + name + "\") protected " + javaType + " " + propName + ";");
            declMember.append(System.lineSeparator());
        } else if (RESERVED_WORDS.contains(propName)) {
            propName = "__" + propName;
            declMember.append("    @JsonProperty(\"" + name + "\") protected " + javaType + " " + propName + ";");
            declMember.append(System.lineSeparator());
        } else {
            declMember.append("    protected " + javaType + " " + propName + ";");
            declMember.append(System.lineSeparator());
        }
        detail.setMember(declMember.toString());

        String ccName = getCamelCase(propName);
        if (QUALIFY_PROPERTIES.contains(propName)) {
            ccName = getCamelCase("the_" + propName);
        }

        StringBuilder getSetter = new StringBuilder();
        String getSetPrepend = "    /** @see #" + propName + " */ @JsonProperty(\"" + name + "\") ";
        getSetter.append(getSetPrepend);
        getSetter.append(" public " + javaType + " get" + ccName + "() { return this." + propName + "; }");
        getSetter.append(System.lineSeparator());
        getSetter.append(getSetPrepend);
        getSetter.append(" public void set" + ccName + "(" + javaType + " " + propName + ") { this." + propName + " = " + propName + "; }");
        getSetter.append(System.lineSeparator());
        getSetter.append(System.lineSeparator());

        detail.setGetSet(getSetter.toString());

        return detail;

    }

    private PropertyList getPropertiesForPOJO(BufferedWriter fs, ArrayNode properties) throws IOException {

        PropertyList propertiesList = new PropertyList();

        for (int i = 0; i < properties.size(); i++) {
            JsonNode property = properties.get(i);
            String propName = property.path("name").asText();
            if (propName != null && !propName.equals("null") && !IGNORE_PROPERTIES.contains(propName)) {
                JsonNode typeObj = property.path("type");
                int maxNum = -1;
                if (property.hasNonNull("maxCardinality")) {
                    maxNum = property.path("maxCardinality").asInt();
                }

                PropertyDetail details = getPropertyDetailForPOJO(propName, typeObj, maxNum, property.path("displayName").asText());
                if (details != null) {
                    propertiesList.addMember(details.getMember());
                    propertiesList.addGetterSetter(details.getGetSet());
                    propertiesList.addToAllProperties(propName);
                    if (!details.getJavaType().contains("Reference")) {
                        propertiesList.addNonRelationshipProperty(propName);
                    } else if (details.getJavaType().contains("ReferenceList")) {
                        propertiesList.addRelationshipProperty(propName);
                    }
                }
            }
        }

        List<String> members = propertiesList.getMembers();
        for (int j = 0; j < members.size(); j++) {
            fs.append(members.get(j));
            fs.append(System.lineSeparator());
        }
        List<String> getterSetters = propertiesList.getGettersSetters();
        fs.append(System.lineSeparator());
        for (int k = 0; k < getterSetters.size(); k++) {
            fs.append(getterSetters.get(k));
        }

        return propertiesList;

    }

    private void createPOJOForType(JsonNode jsonProps, String directory, String version) {

        String packageName = PKG_NAME + "." + version;

        String id   = jsonProps.path("_id").asText();
        String name = jsonProps.path("_name").asText();
        String url  = jsonProps.path("_url").asText();

        if (!IGNORE_TYPES.contains(id)) {

            String className = getClassName(id);
            String filename = directory + File.separator + className + ".java";

            try (BufferedWriter fs = new BufferedWriter(new FileWriter(filename))) {

                fs.append("/* SPDX-License-Identifier: Apache-2.0 */");
                fs.append(System.lineSeparator());
                fs.append("/* Copyright Contributors to the ODPi Egeria project. */");
                fs.append(System.lineSeparator());
                fs.append("package " + packageName + ";");
                fs.append(System.lineSeparator());
                fs.append(System.lineSeparator());
                fs.append("import com.fasterxml.jackson.annotation.JsonIgnoreProperties;");
                fs.append(System.lineSeparator());

                if (!ALIAS_OBJECTS.containsKey(id)) {
                    fs.append("import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;");
                    fs.append(System.lineSeparator());
                    fs.append("import com.fasterxml.jackson.annotation.JsonProperty;");
                    fs.append(System.lineSeparator());
                    fs.append("import java.util.Arrays;");
                    fs.append(System.lineSeparator());
                    fs.append("import java.util.Date;");
                    fs.append(System.lineSeparator());
                    fs.append("import java.util.List;");
                    fs.append(System.lineSeparator());
                    fs.append("import java.util.ArrayList;");
                    fs.append(System.lineSeparator());
                }

                fs.append(System.lineSeparator());
                fs.append(getClassHeading(name, id));
                fs.append("@JsonIgnoreProperties(ignoreUnknown=true)");
                fs.append(System.lineSeparator());

                if (ALIAS_OBJECTS.containsKey(id)) {
                    fs.append("public class " + className + " extends " + getClassName(ALIAS_OBJECTS.get(id)) + " {");
                    fs.append(System.lineSeparator() + System.lineSeparator());
                } else {
                    fs.append("public class " + className + " extends Reference {");
                    fs.append(System.lineSeparator() + System.lineSeparator());
                }

                fs.append("    public static String getIgcTypeId() { return \"" + id + "\"; }");
                fs.append(System.lineSeparator());
                fs.append("    public static String getIgcTypeDisplayName() { return \"" + name + "\"; }");
                fs.append(System.lineSeparator());
                fs.append(System.lineSeparator());

                // Only add the list of properties if this object isn't simply an alias for another
                if (!ALIAS_OBJECTS.containsKey(id)) {
                    PropertyList propertyList = new PropertyList();
                    ArrayNode view = nf.arrayNode();
                    if (jsonProps.hasNonNull("viewInfo") && jsonProps.path("viewInfo").hasNonNull("properties")) {
                        view = (ArrayNode) jsonProps.path("viewInfo").path("properties");
                    }
                    if (view.size() > 0) {
                        propertyList = getPropertiesForPOJO(fs, view);
                    }
                    fs.append("    public static Boolean canBeCreated() { return " + jsonProps.hasNonNull("createInfo") + "; }");
                    fs.append(System.lineSeparator());
                    List<String> nonRelationshipProperties = propertyList.getNonRelationshipProperties();
                    fs.append("    public static Boolean includesModificationDetails() { return " + (nonRelationshipProperties.contains("modified_on")) + "; }");
                    fs.append(System.lineSeparator());
                    if (!nonRelationshipProperties.isEmpty()) {
                        fs.append("    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(");
                        fs.append(System.lineSeparator());
                        for (int i = 0; i < nonRelationshipProperties.size() - 1; i++) {
                            fs.append("        \"" + nonRelationshipProperties.get(i) + "\",");
                            fs.append(System.lineSeparator());
                        }
                        fs.append("        \"" + nonRelationshipProperties.get(nonRelationshipProperties.size() - 1) + "\"");
                        fs.append(System.lineSeparator());
                        fs.append("    );");
                        fs.append(System.lineSeparator());
                    } else {
                        fs.append("    private static final List<String> NON_RELATIONAL_PROPERTIES = new ArrayList<>();");
                        fs.append(System.lineSeparator());
                    }
                    List<String> relationshipProperties = propertyList.getRelationshipProperties();
                    if (!relationshipProperties.isEmpty()) {
                        fs.append("    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(");
                        fs.append(System.lineSeparator());
                        for (int i = 0; i < relationshipProperties.size() - 1; i++) {
                            fs.append("        \"" + relationshipProperties.get(i) + "\",");
                            fs.append(System.lineSeparator());
                        }
                        fs.append("        \"" + relationshipProperties.get(relationshipProperties.size() - 1) + "\"");
                        fs.append(System.lineSeparator());
                        fs.append("    );");
                        fs.append(System.lineSeparator());
                    } else {
                        fs.append("    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();");
                        fs.append(System.lineSeparator());
                    }
                    List<String> allProperties = propertyList.getAllProperties();
                    if (!allProperties.isEmpty()) {
                        fs.append("    private static final List<String> ALL_PROPERTIES = Arrays.asList(");
                        fs.append(System.lineSeparator());
                        for (int i = 0; i < allProperties.size() - 1; i++) {
                            fs.append("        \"" + allProperties.get(i) + "\",");
                            fs.append(System.lineSeparator());
                        }
                        fs.append("        \"" + allProperties.get(allProperties.size() - 1) + "\"");
                        fs.append(System.lineSeparator());
                        fs.append("    );");
                        fs.append(System.lineSeparator());
                    } else {
                        fs.append("    private static final List<String> ALL_PROPERTIES = new ArrayList<>();");
                        fs.append(System.lineSeparator());
                    }
                    fs.append("    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }");
                    fs.append(System.lineSeparator());
                    fs.append("    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }");
                    fs.append(System.lineSeparator());
                    fs.append("    public static List<String> getAllProperties() { return ALL_PROPERTIES; }");
                    fs.append(System.lineSeparator());
                }

                fs.append("    public static Boolean is" + className + "(Object obj) { return (obj.getClass() == " + className + ".class); }");
                fs.append(System.lineSeparator());
                fs.append(System.lineSeparator());
                fs.append("}");
                fs.append(System.lineSeparator());

            } catch (IOException e) {
                log.error("Unable to open file output: {}" + filename, e);
            }

        }

    }

    private String getClassHeading(String displayName, String typeName) {
        StringBuilder sb = new StringBuilder();
        sb.append("/**");
        sb.append(System.lineSeparator());
        sb.append(" * POJO for the '");
        sb.append(typeName);
        sb.append("' asset type in IGC, displayed as '");
        sb.append(displayName);
        sb.append("' in the IGC UI.");
        sb.append(System.lineSeparator());
        sb.append(" * <br><br>");
        sb.append(System.lineSeparator());
        sb.append(" * (this code has been generated based on out-of-the-box IGC metadata types;");
        sb.append(System.lineSeparator());
        sb.append(" *  if modifications are needed, eg. to handle custom attributes,");
        sb.append(System.lineSeparator());
        sb.append(" *  extending from this class in your own custom class is the best approach.)");
        sb.append(System.lineSeparator());
        sb.append(" */");
        sb.append(System.lineSeparator());
        return sb.toString();
    }

    private String getClassName(String fromName) {
        if (NON_UNIQUE_CLASSNAMES.containsKey(fromName)) {
            return NON_UNIQUE_CLASSNAMES.get(fromName);
        } else {
            return getCamelCase(fromName);
        }
    }

    protected class PropertyList {

        private List<String> members;
        private List<String> gettersSetters;

        private List<String> nonRelationship;
        private List<String> relationship;
        private List<String> allProperties;

        public PropertyList() {
            members = new ArrayList<>();
            gettersSetters = new ArrayList<>();
            nonRelationship = new ArrayList<>();
            relationship = new ArrayList<>();
            allProperties = new ArrayList<>();
        }

        public void addMember(String member) { members.add(member); }
        public void addGetterSetter(String getterSetter) { gettersSetters.add(getterSetter); }
        public List<String> getMembers() { return this.members; }
        public List<String> getGettersSetters() { return this.gettersSetters; }

        public void addNonRelationshipProperty(String propertyName) { nonRelationship.add(propertyName); }
        public void addRelationshipProperty(String propertyName) { relationship.add(propertyName); }
        public void addToAllProperties(String propertyName) { allProperties.add(propertyName); }
        public List<String> getNonRelationshipProperties() { return this.nonRelationship; }
        public List<String> getRelationshipProperties() { return this.relationship; }
        public List<String> getAllProperties() { return this.allProperties; }

    }

    protected class PropertyDetail {

        private String member;
        private String getSet;
        private String javaType;

        public PropertyDetail() {
            // Nothing to do by default...
        }

        public void setMember(String member) { this.member = member; }
        public void setGetSet(String getSet) { this.getSet = getSet; }
        public void setJavaType(String javaType) { this.javaType = javaType; }

        public String getMember() { return this.member; }
        public String getGetSet() { return this.getSet; }
        public String getJavaType() { return this.javaType; }

    }

    /**
     * Converts an IGC type or property (something_like_this) into a camelcase class name (SomethingLikeThis).
     *
     * @param input
     * @return String
     */
    public static final String getCamelCase(String input) {
        log.debug("Attempting to camelCase from {}", input);
        Matcher m = INVALID_NAMING_CHARS.matcher(input);
        String invalidsRemoved = m.replaceAll("_");
        StringBuilder sb = new StringBuilder(invalidsRemoved.length());
        for (String token : invalidsRemoved.split("_")) {
            if (token.length() > 0) {
                sb.append(token.substring(0, 1).toUpperCase());
                sb.append(token.substring(1).toLowerCase());
            }
        }
        String result = sb.toString();
        log.debug(" ... succeeded to {}", result);
        return result;
    }

}
