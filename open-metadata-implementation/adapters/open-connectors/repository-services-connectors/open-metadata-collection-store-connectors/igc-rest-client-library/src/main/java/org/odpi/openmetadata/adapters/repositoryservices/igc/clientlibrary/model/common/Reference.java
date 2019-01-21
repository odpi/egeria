/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestConstants;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearch;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchCondition;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchConditionSet;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search.IGCSearchSorting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The ultimate parent object for all IGC assets, it contains only the most basic information common to every single
 * asset in IGC, and present in every single reference to an IGC asset (whether via relationship, search result,
 * etc):<br>
 * <ul>
 *     <li>_name</li>
 *     <li>_type</li>
 *     <li>_id</li>
 *     <li>_url</li>
 *     <li><i>_context</i></li> -- present for <i>almost</i> all assets
 * </ul><br>
 *  POJOs to represent user-defined objects (OpenIGC) should not extend this class directly, but the MainObject class.
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="_type", visible=true, defaultImpl=Reference.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = MainObject.class, name = "main_object")
})
@JsonIgnoreProperties(ignoreUnknown=true)
public class Reference extends ObjectPrinter {

    @JsonIgnore private static final Logger log = LoggerFactory.getLogger(Reference.class);

    /**
     * Used to uniquely identify the object without relying on its ID (RID) remaining static.
     */
    @JsonIgnore private Identity identity = null;

    /**
     * Used to indicate whether this asset has been fully retrieved already (true) or not (false).
     */
    @JsonIgnore private boolean fullyRetrieved = false;

    /**
     * Provides the context to the unique identity of this asset. Note that while this will exist on
     * almost all IGC assets, it is not present on absolutely all of them -- also be aware that without
     * v11.7.0.2+ and an optional parameter it uses, this will always be 'null' in a ReferenceList
     */
    protected ArrayList<Reference> _context = new ArrayList<>();

    /**
     * The '_name' property of a Reference is equivalent to its 'name' property, but will always be
     * populated on a reference ('name' may not yet be populated, depending on whether you have only a reference
     * to the asset, or the full asset itself).
     */
    protected String _name;

    /**
     * The '_type' property defines the type of asset this Reference represents. To allow a Reference to
     * be automatically translated into a Java object, you must first register the Java class that should
     * interpret this data type using {@link IGCRestClient#registerPOJO(Class)}.
     */
    protected String _type;

    /**
     * The '_id' property defines the unique Repository ID (RID) of the asset. This ID should be unique within
     * an instance (environment) of IGC.
     */
    protected String _id;

    /**
     * The '_url' property provides a navigable link directly to the full details of asset this Reference represents,
     * within a given IGC environment.
     */
    protected String _url;

    /** @see #_context */ @JsonProperty("_context") public ArrayList<Reference> getContext() { return this._context; }
    /** @see #_context */ @JsonProperty("_context") public void setContext(ArrayList<Reference> _context) { this._context = _context; }

    /** @see #_name */ @JsonProperty("_name") public String getName() { return this._name; }
    /** @see #_name */ @JsonProperty("_name") public void setName(String _name) { this._name = _name; }

    /** @see #_type */ @JsonProperty("_type") public String getType() { return this._type; }
    /** @see #_type */ @JsonProperty("_type") public void setType(String _type) { this._type = _type; }

    /** @see #_id */ @JsonProperty("_id") public String getId() { return this._id; }
    /** @see #_id */ @JsonProperty("_id") public void setId(String _id) { this._id = _id; }

    /** @see #_url */ @JsonProperty("_url") public String getUrl() { return this._url; }
    /** @see #_url */ @JsonProperty("_url") public void setUrl(String _url) { this._url = _url; }

    public boolean isFullyRetrieved() { return this.fullyRetrieved; }
    public void setFullyRetrieved() { this.fullyRetrieved = true; }

    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
            "name"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }

    /**
     * This will generally be the most performant method by which to retrieve asset information, when only
     * some subset of properties is required
     *
     * @param igcrest the IGCRestClient connection to use to retrieve the details
     * @param properties a list of the properties to retrieve
     * @param pageSize the maximum number of each of the asset's relationships to return on this request
     * @param sorting the sorting criteria to use for the results
     * @return Reference - the object including only the subset of properties specified
     */
    public Reference getAssetWithSubsetOfProperties(IGCRestClient igcrest,
                                                    String[] properties,
                                                    int pageSize,
                                                    IGCSearchSorting sorting) {
        Reference assetWithProperties = null;
        IGCSearchCondition idOnly = new IGCSearchCondition("_id", "=", this._id);
        IGCSearchConditionSet idOnlySet = new IGCSearchConditionSet(idOnly);
        IGCSearch igcSearch = new IGCSearch(Reference.getAssetTypeForSearch(this.getType()), properties, idOnlySet);
        if (pageSize > 0) {
            igcSearch.setPageSize(pageSize);
        }
        if (sorting != null) {
            igcSearch.addSortingCriteria(sorting);
        }
        ReferenceList assetsWithProperties = igcrest.search(igcSearch);
        if (!assetsWithProperties.getItems().isEmpty()) {
            assetWithProperties = assetsWithProperties.getItems().get(0);
        }
        return assetWithProperties;
    }

    /**
     * This will generally be the most performant method by which to retrieve asset information, when only
     * some subset of properties is required
     *
     * @param igcrest the IGCRestClient connection to use to retrieve the details
     * @param properties a list of the properties to retrieve
     * @param pageSize the maximum number of each of the asset's relationships to return on this request
     * @return Reference - the object including only the subset of properties specified
     */
    public Reference getAssetWithSubsetOfProperties(IGCRestClient igcrest, String[] properties, int pageSize) {
        return getAssetWithSubsetOfProperties(igcrest, properties, pageSize, null);
    }

    /**
     * This will generally be the most performant method by which to retrieve asset information, when only
     * some subset of properties is required
     *
     * @param igcrest the IGCRestClient connection to use to retrieve the details
     * @param properties a list of the properties to retrieve
     * @return Reference - the object including only the subset of properties specified
     */
    public Reference getAssetWithSubsetOfProperties(IGCRestClient igcrest, String[] properties) {
        return getAssetWithSubsetOfProperties(igcrest, properties, igcrest.getDefaultPageSize());
    }

    /**
     * Retrieve the asset details from a minimal reference stub.
     * <br><br>
     * Be sure to first use the IGCRestClient "registerPOJO" method to register your POJO(s) for interpretting the
     * type(s) for which you're interested in retrieving details.
     * <br><br>
     * Note that this will only include the first page of any relationships -- to also retrieve all
     * relationships see getFullAssetDetails.
     *
     * @param igcrest the IGCRestClient connection to use to retrieve the details
     * @return Reference - the object including all of its details
     */
    public Reference getAssetDetails(IGCRestClient igcrest) {
        return igcrest.getAssetById(this._id);
    }

    /**
     * Retrieve all of the asset details, including all relationships, from a minimal reference stub.
     * <br><br>
     * Be sure to first use the IGCRestClient "registerPOJO" method to register your POJO(s) for interpretting the
     * type(s) for which you're interested in retrieving details.
     * <br><br>
     * Note that this is quite a heavy operation, relying on multiple REST calls, to build up what could be a very
     * large object; to simply retrieve the details without all relationships, see getAssetDetails.
     *
     * @param igcrest the IGCRestClient connection to use to retrieve the details and relationships
     * @return Reference - the object including all of its details and relationships
     */
    public Reference getFullAssetDetails(IGCRestClient igcrest) {

        Reference asset = igcrest.getAssetById(this.getId());
        try {
            for (Field f : asset.getClass().getFields()) {
                Class fieldClass = f.getType();
                if (fieldClass == ReferenceList.class) {
                    // Uses reflection to call getAllPages on any ReferenceList fields
                    Object relationship = f.get(asset);
                    if (relationship != null) {
                        ((ReferenceList) relationship).getAllPages(igcrest);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Unable to access field.", e);
        }
        return asset;

    }

    /**
     * Recursively traverses the class hierarchy upwards to find the field.
     *
     * @param name the name of the field to find
     * @param clazz the class in which to search (and recurse upwards on its class hierarchy)
     * @return Field first found (lowest level of class hierarchy), or null if never found
     */
    private static Field recursePropertyByName(String name, Class clazz) {
        Field f = null;
        Class superClazz = clazz.getSuperclass();
        if (superClazz != null) {
            try {
                f = superClazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                f = Reference.recursePropertyByName(name, superClazz);
            }
        }
        return f;
    }

    /**
     * Retrieves the first Field, from anywhere within the class hierarchy (bottom-up), by its name.
     *
     * @param name the name of the field to retrieve
     * @return Field
     */
    public Field getFieldByName(String name) {
        Field field;
        try {
            field = this.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            field = Reference.recursePropertyByName(name, this.getClass());
        }
        return field;
    }

    /**
     * Indicates whether this asset has a particular property (true) or not (false).
     *
     * @param name the name of the property to check
     * @return boolean
     */
    public boolean hasProperty(String name) {
        Field property = getFieldByName(name);
        return (property != null);
    }

    /**
     * Retrieves the value of a property of this asset by the provided name (allows dynamic retrieval of properties).
     *
     * @param name the property name to retrieve
     * @return Object - an object representing that property (eg. String, Reference, etc)
     */
    public Object getPropertyByName(String name) {
        Object value = null;
        Field property = getFieldByName(name);
        if (property != null) {
            try {
                property.setAccessible(true);
                value = property.get(this);
            } catch (IllegalAccessException e) {
                log.error("Unable to access property '" + name + "'", e);
            }
        }
        return value;
    }

    /**
     * Sets the value of a property of this asset by the provided name and value (allows dynamic setting of properties).
     *
     * @param name the property name to set a new value against
     * @param value the new value to use for the property
     */
    public void setPropertyByName(String name, Object value) {
        Field property = getFieldByName(name);
        if (property != null) {
            try {
                property.setAccessible(true);
                property.set(this, value);
            } catch (IllegalAccessException e) {
                log.error("Unable to access property '" + name + "'", e);
            }
        }
    }

    /**
     * Returns true iff the provided object is a relationship (ie. of class Reference or one of its offspring).
     *
     * @param obj the object to check
     * @return Boolean
     */
    public static final Boolean isReference(Object obj) {
        Class clazz = obj.getClass();
        while (clazz != null && clazz != Reference.class) {
            clazz = clazz.getSuperclass();
        }
        return (clazz == Reference.class);
    }

    /**
     * Returns true iff the provided property name of this object is a relationship (ie. of class Reference).
     *
     * @param propertyName the name of the property to check
     * @return Boolean
     */
    public Boolean isReference(String propertyName) {
        Field property = getFieldByName(propertyName);
        return (property.getType() == Reference.class);
    }

    /**
     * Returns true iff the provided object is a list of relationships (ie. of class ReferenceList).
     *
     * @param obj the object to check
     * @return Boolean
     */
    public static final Boolean isReferenceList(Object obj) {
        return (obj.getClass() == ReferenceList.class);
    }

    /**
     * Returns true iff the provided property name of this object is a list of relationships (ie. of class ReferenceList).
     *
     * @param propertyName the name of the property to check
     * @return Boolean
     */
    public Boolean isReferenceList(String propertyName) {
        Field property = getFieldByName(propertyName);
        return (property.getType() == ReferenceList.class);
    }

    /**
     * Returns true iff the provided object is a simple type (String, Number, Boolean, Date, etc).
     *
     * @param obj the object to check
     * @return Boolean
     */
    public static final Boolean isSimpleType(Object obj) {
        return (!Reference.isReference(obj) && !Reference.isReferenceList(obj));
    }

    /**
     * Returns true iff the provided property name of this object is a simple type (String, Number, Boolean, Date, etc).
     *
     * @param propertyName the name of the property to check
     * @return Boolean
     */
    public Boolean isSimpleType(String propertyName) {
        Field property = getFieldByName(propertyName);
        return (property.getType() != Reference.class && property.getType() != ReferenceList.class);
    }

    /**
     * Ensures that the _context of the asset is populated (takes no action if already populated).
     * In addition, if the asset type supports them, will also retrieve and set modification details.
     *
     * @param igcrest a REST API connection to use in populating the context
     * @return boolean indicating whether _context was successfully / already populated (true) or not (false)
     */
    public boolean populateContext(IGCRestClient igcrest) {

        boolean success = true;
        // Only bother retrieving the context if it isn't already present

        if (this._context.isEmpty()) {

            boolean bHasModificationDetails = this.hasModificationDetails();

            IGCSearchCondition idOnly = new IGCSearchCondition("_id", "=", this.getId());
            IGCSearchConditionSet idOnlySet = new IGCSearchConditionSet(idOnly);
            IGCSearch igcSearch = new IGCSearch(this.getType(), idOnlySet);
            if (bHasModificationDetails) {
                igcSearch.addProperties(IGCRestConstants.getModificationProperties());
            }
            igcSearch.setPageSize(2);
            ReferenceList assetsWithCtx = igcrest.search(igcSearch);
            success = (!assetsWithCtx.getItems().isEmpty());
            if (success) {

                Reference assetWithCtx = assetsWithCtx.getItems().get(0);
                this._context = new ArrayList(assetWithCtx.getContext());

                if (bHasModificationDetails) {
                    this.setPropertyByName(IGCRestConstants.MOD_CREATED_ON, assetWithCtx.getPropertyByName(IGCRestConstants.MOD_CREATED_ON));
                    this.setPropertyByName(IGCRestConstants.MOD_CREATED_BY, assetWithCtx.getPropertyByName(IGCRestConstants.MOD_CREATED_BY));
                    this.setPropertyByName(IGCRestConstants.MOD_MODIFIED_ON, assetWithCtx.getPropertyByName(IGCRestConstants.MOD_MODIFIED_ON));
                    this.setPropertyByName(IGCRestConstants.MOD_MODIFIED_BY, assetWithCtx.getPropertyByName(IGCRestConstants.MOD_MODIFIED_BY));
                }

            }

        }

        return success;

    }

    /**
     * Retrieves the semantic identity of the asset.
     *
     * @param igcrest a REST API connection to use in confirming the identity of the asset
     * @return Identity
     */
    public Identity getIdentity(IGCRestClient igcrest) {
        if (this.identity == null) {
            this.populateContext(igcrest);
            this.identity = new Identity(this._context, this.getType(), this.getName());
        }
        return this.identity;
    }

    /**
     * Indicates whether this asset type tracks modification details (true) or not (false).
     *
     * @return boolean
     */
    public boolean hasModificationDetails() { return this.hasProperty("modified_on"); }

    /**
     * Indicates whether IGC assets of the POJO class are capable of being created (true) or not (false).
     *
     * @param pojoClass the POJO for which to check an asset's create-ability
     * @return boolean
     */
    public static boolean isCreatableFromPOJO(Class pojoClass) {
        boolean creatable = false;
        try {
            Method canBeCreated = pojoClass.getMethod("canBeCreated");
            creatable = (Boolean) canBeCreated.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve creatable status from IGC POJO: {}", pojoClass, e);
        }
        return creatable;
    }

    /**
     * Retrieves the IGC asset display name from the provided POJO.
     *
     * @param pojoClass the POJO for which to retrieve an asset's type display name
     * @return String
     */
    public static String getDisplayNameFromPOJO(Class pojoClass) {
        String igcAssetDisplayName = null;
        try {
            Method getIgcTypeDisplayName = pojoClass.getMethod("getIgcTypeDisplayName");
            igcAssetDisplayName = (String) getIgcTypeDisplayName.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve display name from IGC POJO: {}", pojoClass, e);
        }
        return igcAssetDisplayName;
    }

    /**
     * Retrieves the asset type from the provided POJO.
     *
     * @param pojoClass the POJO for which to retrieve an asset's (REST) type
     * @return String
     */
    public static String getAssetTypeFromPOJO(Class pojoClass) {
        String igcAssetType = null;
        try {
            Method getIgcType = pojoClass.getMethod("getIgcTypeId");
            igcAssetType = (String) getIgcType.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve asset type from IGC POJO: {}", pojoClass, e);
        }
        return igcAssetType;
    }

    /**
     * Indicates whether assets of this type can be created via IGC's API (true) or not (false).
     *
     * @param pojoClass the POJO for which to check create-ability
     * @return boolean
     */
    public static boolean canAssetBeCreatedFromPOJO(Class pojoClass) {
        Boolean canBe = false;
        try {
            Method canBeCreated = pojoClass.getMethod("canBeCreated");
            canBe = (Boolean) canBeCreated.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve creation capability from IGC POJO: {}", pojoClass, e);
        }
        return canBe;
    }

    /**
     * Retrieves the list of property names for the asset that are not relationships to other assets.
     *
     * @param pojoClass the POJO for which to retrieve non-relationship property names
     * @return List<String>
     */
    public static List<String> getNonRelationshipPropertiesFromPOJO(Class pojoClass) {
        List<String> list = null;
        try {
            Method getNonRelationshipProperties = pojoClass.getMethod("getNonRelationshipProperties");
            list = (List<String>) getNonRelationshipProperties.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve non-relationship properties from IGC POJO: {}", pojoClass, e);
        }
        return list;
    }

    /**
     * Retrieves the list of property names for the asset that are string-valued.
     *
     * @param pojoClass the POJO for which to retrieve string-valued property names
     * @return List<String>
     */
    public static List<String> getStringPropertiesFromPOJO(Class pojoClass) {
        List<String> list = null;
        try {
            Method getStringProperties = pojoClass.getMethod("getStringProperties");
            list = (List<String>) getStringProperties.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve string properties from IGC POJO: {}", pojoClass, e);
        }
        return list;
    }

    /**
     * Retrieves the list of all property names for the asset.
     *
     * @param pojoClass the POJO for which to retrieve all property names
     * @return List<String>
     */
    public static List<String> getAllPropertiesFromPOJO(Class pojoClass) {
        List<String> list = null;
        try {
            Method getAllProperties = pojoClass.getMethod("getAllProperties");
            list = (List<String>) getAllProperties.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve all properties from IGC POJO: {}", pojoClass, e);
        }
        return list;
    }

    /**
     * Retrieves the list of all paged relationship property names for the asset.
     *
     * @param pojoClass the POJO for which to retrieve the paged relationship property names
     * @return List<String>
     */
    public static List<String> getPagedRelationalPropertiesFromPOJO(Class pojoClass) {
        List<String> list = null;
        try {
            Method getPagedProperties = pojoClass.getMethod("getPagedRelationshipProperties");
            list = (List<String>) getPagedProperties.invoke(null);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("Unable to retrieve paged relationship properties from IGC POJO: {}", pojoClass, e);
        }
        return list;
    }

    /**
     * Indicates whether assets of this type include modification details (true) or not (false).
     *
     * @param pojoClass the POJO for which to check for modification details
     * @return boolean
     */
    public static boolean hasModificationDetails(Class pojoClass) {
        boolean hasModDetails = false;
        if (pojoClass != null) {
            try {
                Method hasModificationDetails = pojoClass.getMethod("includesModificationDetails");
                Boolean bHasModificationDetails = (Boolean) hasModificationDetails.invoke(null);
                hasModDetails = (bHasModificationDetails != null && bHasModificationDetails);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                log.info("Unable to find modification details for class {}", pojoClass);
            }
        }
        return hasModDetails;
    }

    /**
     * Translates the type of asset into what should be used for searching.
     * This is necessary for certain types that are actually pseudo-aliases for other types, to ensure all
     * properties of that asset can be retrieved.
     *
     * @param assetType the asset type for which to retrieve the search type
     * @return String
     */
    public static String getAssetTypeForSearch(String assetType) {
        String typeForSearch = null;
        switch(assetType) {
            case "host_(engine)":
                typeForSearch = "host";
                break;
            case "non_steward_user":
            case "steward_user":
                typeForSearch = "user";
                break;
            default:
                typeForSearch = assetType;
                break;
        }
        return typeForSearch;
    }

    // TODO: eventually handle the '_expand' that exists for data classifications

}
