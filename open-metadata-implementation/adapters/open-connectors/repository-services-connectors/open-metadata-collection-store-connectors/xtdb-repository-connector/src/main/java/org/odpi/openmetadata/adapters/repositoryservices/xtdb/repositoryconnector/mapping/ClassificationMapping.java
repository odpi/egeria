/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.*;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.io.IOException;
import java.util.*;

/**
 * Maps the properties of Classifications between persistence and objects.
 *
 * The idea is to map Classifications into a XTDB data model that flattens their structure into the EntitySummary
 * structure itself (so they are always retrieved with the EntitySummary document), using the following convention:
 * <code>
 *     {
 *         ...
 *         :classifications.Confidentiality/type :type/(GUID)
 *         :classifications.Confidentiality/instanceLicense ""
 *         :classifications.Confidentiality/createTime #inst "2021-01-26T16:35:37.504-00:00"
 *         :classifications.Confidentiality.classificationProperties/level ...
 *         ...
 *         :classifications.AnotherClassification/type :type/(GUID)
 *         :classifications.AnotherClassification/createTime #inst "2021-01-26T16:30:37.504-00:00"
 *         :classifications.AnotherClassification.classificationProperties/property ...
 *         ...
 *     }
 * </code>
 * In this way, each classification can be kept separate from other classifications, and a single classification's
 * value remains mutually-exclusive with any other values for that classification (due to the unique reference name of
 * the properties of that classification).
 */
public class ClassificationMapping extends InstanceAuditHeaderMapping {

    private static final String NAMESPACE = getKeyword(EntitySummaryMapping.N_CLASSIFICATIONS);

    private static final String CLASSIFICATION = "classification";

    private static final String N_CLASSIFICATION_TYPE = "type";
    private static final String N_CLASSIFICATION_ORIGIN = "classificationOrigin";
    private static final String N_CLASSIFICATION_ORIGIN_GUID = "classificationOriginGUID";

    public static final String CLASSIFICATION_PROPERTIES_NS = "classificationProperties";
    public static final String N_LAST_CLASSIFICATION_CHANGE = "lastClassificationChange";

    public static final String LAST_CLASSIFICATION_CHANGE = getKeyword(N_LAST_CLASSIFICATION_CHANGE);

    private static final Set<String> KNOWN_PROPERTIES = createKnownProperties();
    private static Set<String> createKnownProperties() {
        Set<String> set = new HashSet<>();
        set.add(N_CLASSIFICATION_ORIGIN);
        set.add(N_CLASSIFICATION_ORIGIN_GUID);
        return set;
    }

    private List<Classification> classifications;
    private XtdbDocument xtdbDoc;

    /**
     * Construct a mapping from a Classification (to map to a XTDB representation).
     * @param xtdbConnector connectivity to XTDB
     * @param classifications from which to map
     */
    public ClassificationMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                                 List<Classification> classifications) {
        super(xtdbConnector);
        this.classifications = classifications;
    }

    /**
     * Construct a mapping from a XTDB map (to map to an Egeria representation).
     * @param xtdbConnector connectivity to XTDB
     * @param xtdbDoc from which to map
     */
    public ClassificationMapping(XTDBOMRSRepositoryConnector xtdbConnector,
                                 XtdbDocument xtdbDoc) {
        super(xtdbConnector);
        this.xtdbDoc = xtdbDoc;
    }

    /**
     * Check whether the specified property is a known base-level Classification property.
     * @param property to check
     * @return boolean
     */
    public static boolean isKnownBaseProperty(String property) {
        return KNOWN_PROPERTIES.contains(property);
    }

    /**
     * Add the details of the mapping to the provided XtdbDocument builder.
     * @param builder into which to add the classification details
     */
    public void addToXtdbDoc(XtdbDocument.Builder builder) {

        if (classifications != null) {
            String lastClassificationName = null;
            try {
                Date latestChange = null;
                List<String> classificationNames = new ArrayList<>();
                for (Classification classification : classifications) {
                    String classificationName = classification.getName();
                    lastClassificationName = classificationName;
                    classificationNames.add(classificationName);
                    String qualifiedNamespace = getNamespaceForClassification(classificationName);
                    Date latestClassification = InstanceAuditHeaderMapping.buildDoc(builder, classification, qualifiedNamespace);
                    if (latestChange == null || latestChange.before(latestClassification)) {
                        latestChange = latestClassification;
                    }
                    builder.put(getKeyword(qualifiedNamespace, N_CLASSIFICATION_ORIGIN_GUID), classification.getClassificationOriginGUID());
                    builder.put(getKeyword(qualifiedNamespace, N_CLASSIFICATION_ORIGIN), getSymbolicNameForClassificationOrigin(classification.getClassificationOrigin()));
                    InstancePropertiesMapping.addToDoc(xtdbConnector, builder, classification.getType(), classification.getProperties());
                }
                // Add the list of classification names, for easing search
                builder.put(NAMESPACE, PersistentVector.create(classificationNames));
                // Add the latest change to any classification for internal tracking of validity
                builder.put(getKeyword(N_LAST_CLASSIFICATION_CHANGE), latestChange);
            } catch (IOException e) {
                xtdbConnector.logProblem(ClassificationMapping.class.getName(),
                                         "addToXtdbDoc",
                                         XTDBAuditCode.SERIALIZATION_FAILURE,
                                         e,
                                         "<unknown>",
                                         lastClassificationName,
                                         e.getClass().getName());
            }
        }

    }

    /**
     * Add the details of the provided classification to the XTDB document map.
     * @param doc document map into which to add the classification details
     * @param classification to add into the doc
     * @return IPersistentMap the updated document map
     * @throws IOException on any error serializing the classification
     * @throws InvalidParameterException on any type-related property error
     */
    public static IPersistentMap addToMap(IPersistentMap doc,
                                          Classification classification)
            throws IOException, InvalidParameterException {

        // Setup relative to any existing classifications (upsert)
        IPersistentVector classificationNames = (IPersistentVector) doc.valAt(Keyword.intern(NAMESPACE));
        String classificationName = classification.getName();
        if (classificationNames == null) {
            classificationNames = PersistentVector.create(classificationName);
        } else {
            boolean alreadyThere = false;
            for (int i = 0; i < classificationNames.length() && !alreadyThere; i++) {
                String existingName = (String) classificationNames.nth(i);
                if (existingName.equals(classificationName)) {
                    alreadyThere = true;
                }
            }
            if (!alreadyThere) {
                classificationNames = classificationNames.cons(classificationName);
            }
        }
        String qualifiedNamespace = getNamespaceForClassification(classificationName);
        IPersistentVector tuple = InstanceAuditHeaderMapping.addToMap(doc, classification, qualifiedNamespace);
        Date timestamp = (Date) tuple.nth(0);
        doc = (IPersistentMap) tuple.nth(1);
        doc = InstancePropertiesMapping.addToMap(doc, classification.getType().getTypeDefGUID(), classification.getProperties());
        // Add the list of classification names, for easing search
        return doc
                .assoc(Keyword.intern(getKeyword(qualifiedNamespace, N_CLASSIFICATION_ORIGIN_GUID)), classification.getClassificationOriginGUID())
                .assoc(Keyword.intern(getKeyword(qualifiedNamespace, N_CLASSIFICATION_ORIGIN)), getSymbolicNameForClassificationOrigin(classification.getClassificationOrigin()))
                .assoc(Keyword.intern(NAMESPACE), classificationNames)
                .assoc(Keyword.intern(getKeyword(N_LAST_CLASSIFICATION_CHANGE)), timestamp);

    }

    /**
     * Remove all details of the provided classification name from the XTDB document map.
     * @param doc document map from which to remove the classification details
     * @param classificationName to remove from the doc
     * @return IPersistentMap the updated document map
     * @throws ClassificationErrorException if the provided document map does not contain the specified classification
     */
    @SuppressWarnings("unchecked")
    public static IPersistentMap removeFromMap(IPersistentMap doc,
                                               String classificationName) throws ClassificationErrorException {

        final String methodName = "removeFromMap";
        IPersistentVector classificationNames = (IPersistentVector) doc.valAt(Keyword.intern(NAMESPACE));
        if (classificationNames == null) {
            throw new ClassificationErrorException(XTDBErrorCode.ENTITY_NOT_CLASSIFIED.getMessageDefinition(
                    classificationName,
                    (String) doc.valAt(Constants.XTDB_PK)),
                                                   ClassificationMapping.class.getName(),
                                                   methodName);
        } else {
            List<String> newNames = new ArrayList<>();
            boolean found = false;
            for (int i = 0; i < classificationNames.length() && !found; i++) {
                String existingName = (String) classificationNames.nth(i);
                if (existingName.equals(classificationName)) {
                    found = true;
                } else {
                    newNames.add(existingName);
                }
            }
            if (!found) {
                throw new ClassificationErrorException(XTDBErrorCode.ENTITY_NOT_CLASSIFIED.getMessageDefinition(
                        classificationName,
                        (String) doc.valAt(Constants.XTDB_PK)),
                                                       ClassificationMapping.class.getName(),
                                                       methodName);
            }
            classificationNames = PersistentVector.create(newNames);
        }

        String qualifiedNamespace = getNamespaceForClassification(classificationName);
        Iterator<MapEntry> entries = (Iterator<MapEntry>) doc.iterator();
        while (entries.hasNext()) {
            MapEntry entry = entries.next();
            Object key = entry.getKey();
            String keyName = key.toString().substring(1); // remove the ':' from the keyword
            if (keyName.startsWith(qualifiedNamespace)) {
                doc = doc.without(key);
            }
        }

        // Add the updated list of classifications and the last classification timestamp to note the removal in history
        return doc
                .assoc(Keyword.intern(NAMESPACE), classificationNames)
                .assoc(Keyword.intern(getKeyword(N_LAST_CLASSIFICATION_CHANGE)), new Date());

    }

    /**
     * Validates that the provided metadata instance possesses the specified classification.
     *
     * @param instance to confirm possesses the classification
     * @param classificationName to confirm the instance possesses
     * @param className class called
     * @param methodName method called
     * @throws ClassificationErrorException if the instance does not possess the classification
     */
    public static void validateHasClassification(IPersistentMap instance,
                                                 String classificationName,
                                                 String className,
                                                 String methodName) throws ClassificationErrorException {
        IPersistentVector classificationNames = (IPersistentVector) instance.valAt(Keyword.intern(NAMESPACE));
        boolean exists = false;
        if (classificationNames != null) {
            for (int i = 0; i < classificationNames.length() && !exists; i++) {
                String candidate = (String) classificationNames.nth(i);
                exists = classificationName.equals(candidate);
            }
        }
        if (!exists) {
            String entityGUID = (String) instance.valAt(Constants.XTDB_PK);
            throw new ClassificationErrorException(XTDBErrorCode.ENTITY_NOT_CLASSIFIED.getMessageDefinition(
                    classificationName, entityGUID), className, methodName);
        }
    }

    /**
     * Map from XTDB to Egeria.
     * @return {@code List<Classification>}
     * @see #ClassificationMapping(XTDBOMRSRepositoryConnector, XtdbDocument)
     */
    public List<Classification> toEgeria() {

        if (classifications != null) {
            return classifications;
        } else if (xtdbDoc == null) {
            return null;
        } else {
            return fromDoc();
        }

    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @return {@code List<Classification>}
     */
    protected List<Classification> fromDoc() {

        List<Classification> list = new ArrayList<>();
        // Start by retrieving the list of classification names
        IPersistentVector classificationNames = (IPersistentVector) xtdbDoc.get(NAMESPACE);

        if (classificationNames != null) {
            // Then, for each classification associated with the document...
            for (int i = 0; i < classificationNames.length(); i++) {

                String classificationName = (String) classificationNames.nth(i);
                String namespaceForClassification = getNamespaceForClassification(classificationName);

                Classification classification = new Classification();
                classification.setName(classificationName);
                super.fromDoc(classification, xtdbDoc, namespaceForClassification);

                // Retrieve its embedded type details (doing this rather than going to TypeDef from repositoryHelper,
                // since these could change over history of the document)
                IPersistentMap embeddedType = (IPersistentMap) xtdbDoc.get(getKeyword(namespaceForClassification, N_CLASSIFICATION_TYPE));
                InstanceType classificationType = getDeserializedValue(xtdbConnector, CLASSIFICATION, N_CLASSIFICATION_TYPE, embeddedType, mapper.getTypeFactory().constructType(InstanceType.class));

                // And use these to retrieve the property mappings for this classification (only)
                InstanceProperties ip = InstancePropertiesMapping.getFromDoc(xtdbConnector, classificationType, xtdbDoc);

                if (ip != null) {
                    classification.setProperties(ip);
                }

                String originGuid = (String) xtdbDoc.get(getKeyword(namespaceForClassification, N_CLASSIFICATION_ORIGIN_GUID));
                classification.setClassificationOriginGUID(originGuid);
                String originSymbolicName = (String) xtdbDoc.get(getKeyword(namespaceForClassification, N_CLASSIFICATION_ORIGIN));
                ClassificationOrigin classificationOrigin = getClassificationOriginFromSymbolicName(xtdbConnector, originSymbolicName);
                classification.setClassificationOrigin(classificationOrigin);

                list.add(classification);
            }
        }

        return list.isEmpty() ? null : list;

    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param doc from which to map
     * @return {@code List<Classification>}
     * @throws IOException on any issue deserializing values
     * @throws InvalidParameterException for any unmapped properties
     */
    public static List<Classification> fromMap(IPersistentMap doc) throws IOException, InvalidParameterException {

        List<Classification> list = new ArrayList<>();
        // Start by retrieving the list of classification names
        IPersistentVector classificationNames = (IPersistentVector) doc.valAt(Keyword.intern(NAMESPACE));

        if (classificationNames != null) {
            // Then, for each classification associated with the document...
            for (int i = 0; i < classificationNames.length(); i++) {

                String classificationName = (String) classificationNames.nth(i);
                String namespaceForClassification = getNamespaceForClassification(classificationName);

                Classification classification = new Classification();
                classification.setName(classificationName);
                InstanceAuditHeaderMapping.fromMap(classification, doc, namespaceForClassification);

                // Retrieve its embedded type details (doing this rather than going to TypeDef from repositoryHelper,
                // since these could change over history of the document)
                InstanceType classificationType = getTypeFromInstance(doc, namespaceForClassification);

                // And use these to retrieve the property mappings for this classification (only)
                InstanceProperties ip = InstancePropertiesMapping.getFromMap(classificationType, doc);

                if (ip != null) {
                    classification.setProperties(ip);
                }

                String originGuid = (String) doc.valAt(Keyword.intern(getKeyword(namespaceForClassification, N_CLASSIFICATION_ORIGIN_GUID)));
                classification.setClassificationOriginGUID(originGuid);
                String originSymbolicName = (String) doc.valAt(Keyword.intern(getKeyword(namespaceForClassification, N_CLASSIFICATION_ORIGIN)));
                ClassificationOrigin classificationOrigin = getClassificationOriginFromSymbolicName(originSymbolicName);
                classification.setClassificationOrigin(classificationOrigin);

                list.add(classification);
            }
        }

        return list.isEmpty() ? null : list;

    }

    /**
     * Given a classification name and qualifying namespace, convert into a qualified name that can be used for the
     * classification-specific namespace.
     * @param root namespace
     * @param classificationName of the classification
     * @return String qualified namespace
     */
    public static String getNamespaceForClassification(String root, String classificationName) {
        return root + "." + classificationName;
    }

    /**
     * Given a fully-qualified classification namespace and a root, parse out the name of the classification.
     * @param root namespace
     * @param qualifiedNamespace fully-qualified classification namespace
     * @return String classification name
     */
    public static String getClassificationNameFromNamespace(String root, String qualifiedNamespace) {
        String remainder = qualifiedNamespace.substring(root.length() + 1);
        if (remainder.contains(".")) {
            int firstDot = remainder.indexOf(".");
            return remainder.substring(0, firstDot);
        } else {
            return remainder;
        }
    }

    /**
     * Given a classification name (on its own), convert it into a qualified name that can be used for the namespace.
     * @param classificationName to translate
     * @return String qualified namespace
     */
    public static String getNamespaceForClassification(String classificationName) {
        return getNamespaceForClassification(NAMESPACE, classificationName);
    }

    /**
     * Convert the provided symbolic name into its ClassificationOrigin.
     * @param xtdbConnector connectivity to the repository
     * @param symbolicName to convert
     * @return ClassificationOrigin
     */
    public static ClassificationOrigin getClassificationOriginFromSymbolicName(XTDBOMRSRepositoryConnector xtdbConnector, String symbolicName) {
        final String methodName = "getClassificationOriginFromSymbolicName";
        for (ClassificationOrigin b : ClassificationOrigin.values()) {
            if (b.getName().equals(symbolicName)) {
                return b;
            }
        }
        xtdbConnector.logProblem(ClassificationMapping.class.getName(),
                                 methodName,
                                 XTDBAuditCode.NON_EXISTENT_ENUM,
                                 null,
                                 "ClassificationOrigin",
                                 symbolicName);
        return null;
    }

    /**
     * Convert the provided symbolic name into its ClassificationOrigin.
     * @param symbolicName to convert
     * @return ClassificationOrigin
     * @throws InvalidParameterException if there is no such symbolic name
     */
    public static ClassificationOrigin getClassificationOriginFromSymbolicName(String symbolicName) throws InvalidParameterException {
        final String methodName = "getClassificationOriginFromSymbolicName";
        for (ClassificationOrigin b : ClassificationOrigin.values()) {
            if (b.getName().equals(symbolicName)) {
                return b;
            }
        }
        throw new InvalidParameterException(XTDBErrorCode.UNKNOWN_ENUMERATED_VALUE.getMessageDefinition(
                symbolicName, "ClassificationOrigin"), ClassificationMapping.class.getName(), methodName, "symbolicName");
    }

    /**
     * Convert the provided ClassificationOrigin into its symbolic name.
     * @param co to convert
     * @return String
     */
    public static String getSymbolicNameForClassificationOrigin(ClassificationOrigin co) {
        return co == null ? null : co.getName();
    }

    /**
     * Retrieve the namespace for properties of the classification
     * @param qualifiedRoot the classification-qualified root for the namespace
     * @return String
     */
    public static String getNamespaceForProperties(String qualifiedRoot) {
        return qualifiedRoot + "." + CLASSIFICATION_PROPERTIES_NS;
    }

}
