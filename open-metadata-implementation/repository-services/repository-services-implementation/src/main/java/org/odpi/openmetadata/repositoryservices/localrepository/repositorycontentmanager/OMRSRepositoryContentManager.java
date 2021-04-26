/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.events.*;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.rest.server.OMRSRepositoryRESTServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * OMRSRepositoryContentManager supports an in-memory cache of TypeDefs for the local server.  It is used by the OMRS
 * components for constructing metadata instances with valid types.   It ensures that the TypeDefs used in other
 * members of the open metadata repository cohorts that the local server is also a member of are consistent with the
 * local repository.
 *
 * OMRSRepositoryContentManager plays a central role in ensuring the integrity of the metadata in the local repository.
 * It is called from multiple components at different points in the processing.  It presents a different interface
 * to each of these components that is specialized to their needs.
 * <ul>
 *     <li>
 *         OMRSTypeDefEventProcessor: processes inbound events from remote members of the open metadata
 *         repository cohorts that the local repository is connected to.  These incoming TypeDef events need to
 *         be validated against the types used locally and either saved or discarded depending on the exchange rule
 *         setting.
 *     </li>
 *     <li>
 *         OMRSTypeDefManager: provides maintenance methods for managing the TypeDefs in the local cache.
 *     </li>
 * </ul>
 */
public class OMRSRepositoryContentManager extends OMRSTypeDefEventProcessor implements OMRSTypeDefManager
{
    private LocalOMRSRepositoryConnector    localRepositoryConnector       = null;
    private String                          localServerName                = null;
    private String                          localServerUserId;             /* initialized in the constructor */
    private OMRSRepositoryEventManager      outboundRepositoryEventManager = null;
    private String                          openTypesOriginGUID            = null;
    private Map<String, TypeDef>            knownTypeDefGUIDs              = new HashMap<>();
    private Map<String, TypeDef>            knownTypeDefNames              = new HashMap<>();
    private Map<String, AttributeTypeDef>   knownAttributeTypeDefGUIDs     = new HashMap<>();
    private Map<String, AttributeTypeDef>   knownAttributeTypeDefNames     = new HashMap<>();
    private Map<String, TypeDef>            activeTypeDefGUIDs             = new HashMap<>();
    private Map<String, TypeDef>            activeTypeDefNames             = new HashMap<>();
    private Map<String, AttributeTypeDef>   activeAttributeTypeDefGUIDs    = new HashMap<>();
    private Map<String, AttributeTypeDef>   activeAttributeTypeDefNames    = new HashMap<>();
    private Map<String, List<TypeDefLink>>  typeDefSuperTypes              = new HashMap<>();
    private Map<String, InstanceType>       knownInstanceTypes             = new HashMap<>();
    private Map<String, String>             metadataCollectionNames        = new HashMap<>();
    private Map<String, Set<String>>        knownPropertyToTypeDefNames    = new HashMap<>();


    /*
     * The audit log provides a verifiable record of the open metadata archives that have been loaded into
     * the open metadata repository.  The Logger is for standard debug.
     */
    private AuditLog auditLog;

    private static final Logger       log      = LoggerFactory.getLogger(OMRSRepositoryContentManager.class);



    /**
     * Default constructor
     *
     * @param localServerUserId userId to use when processing messages
     * @param auditLog  audit log for this component.
     */
    public OMRSRepositoryContentManager(String   localServerUserId,
                                        AuditLog auditLog)
    {
        super("Local Repository Content (TypeDef) Manager");

        this.localServerUserId = localServerUserId;
        this.auditLog = auditLog;
    }


    /**
     * Saves all of the information necessary to process incoming TypeDef events.
     *
     * @param localRepositoryConnector connector to the local repository
     * @param outboundRepositoryEventManager event manager to call for outbound events used to send out reports
     *                                       of conflicting TypeDefs
     */
    public void setupEventProcessor(LocalOMRSRepositoryConnector      localRepositoryConnector,
                                    OMRSRepositoryEventManager        outboundRepositoryEventManager)
    {
        if (localRepositoryConnector != null)
        {
            this.localRepositoryConnector = localRepositoryConnector;
            this.localServerName = localRepositoryConnector.getLocalServerName();
        }

        this.outboundRepositoryEventManager = outboundRepositoryEventManager;
    }


    /**
     * Save the unique identifier of the open metadata archive.  This is stored in the origin property of
     * all of the open metadata types.  It is needed to support the isOpenType() method.
     *
     * @param openMetadataTypesGUID unique identifier for the open metadata type's archive
     */
    public void setOpenMetadataTypesOriginGUID(String openMetadataTypesGUID)
    {
        openTypesOriginGUID = openMetadataTypesGUID;
    }


    /*
     * ========================
     * OMRSTypeDefManager
     */

    /**
     * Cache a definition of a new TypeDef.  This method assumes the TypeDef has been successfully added to the
     * local repository already and all that is needed is to maintain the cached list of types
     *
     * @param sourceName source of the request (used for logging)
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     */
    @Override
    public void addTypeDef(String  sourceName, TypeDef      newTypeDef)
    {
        this.cacheTypeDef(sourceName, newTypeDef,true);
    }


    /**
     * Cache a definition of a TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param newTypeDef TypeDef structure describing the new TypeDef.
     * @param isLocallySupported indicates whether the TypeDef is supported by the local repository.
     */
    private void cacheTypeDef(String  sourceName, TypeDef      newTypeDef, boolean isLocallySupported)
    {
        knownTypeDefGUIDs.put(newTypeDef.getGUID(), newTypeDef);
        knownTypeDefNames.put(newTypeDef.getName(), newTypeDef);

        if (isLocallySupported)
        {
            activeTypeDefGUIDs.put(newTypeDef.getGUID(), newTypeDef);
            activeTypeDefNames.put(newTypeDef.getName(), newTypeDef);

            log.debug("New Active Type {} from {}. Full TypeDef: {}", newTypeDef.getName(), sourceName, newTypeDef);
        }
        else
        {
            log.debug("New Known Type {} from {}. Full TypeDef: {}", newTypeDef.getName(), sourceName, newTypeDef);
        }
        cacheTypeDefPropertyLookup(sourceName, newTypeDef);
    }


    /**
     * Cache a lookup of the TypeDef's properties by their name, to the names of the TypeDefs in which a property by
     * that name is defined.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDef TypeDef structure describing the new TypeDef.
     */
    private void cacheTypeDefPropertyLookup(String sourceName, TypeDef typeDef)
    {
        // retrieve all properties on the newTypeDef
        String                     typeDefName                   = typeDef.getName();
        List<TypeDefAttribute>     propertiesDefinition          = typeDef.getPropertiesDefinition();

        if (propertiesDefinition != null)
        {
            // for each property, add this typeDef's GUID as one that defines a property with this name
            for (TypeDefAttribute property : propertiesDefinition)
            {
                String propertyName = property.getAttributeName();
                knownPropertyToTypeDefNames.computeIfAbsent(propertyName, k -> new HashSet<>());
                knownPropertyToTypeDefNames.get(propertyName).add(typeDefName);
                log.debug("Cached property '{}' from {}, for lookup under TypeDef: {}", propertyName, sourceName, typeDefName);
            }
        }
    }


    /**
     * Remove a definition of a TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param obsoleteTypeDefGUID unique identifier for the type.
     * @param obsoleteTypeDefName unique name for the type.
     * @param isLocallySupported indicates whether the TypeDef is supported by the local repository.
     */
    private void uncacheTypeDef(String  sourceName,
                                String  obsoleteTypeDefGUID,
                                String  obsoleteTypeDefName,
                                boolean isLocallySupported)
    {
        knownTypeDefGUIDs.remove(obsoleteTypeDefGUID);
        knownTypeDefNames.remove(obsoleteTypeDefName);

        if (isLocallySupported)
        {
            activeTypeDefGUIDs.remove(obsoleteTypeDefGUID);
            activeTypeDefNames.remove(obsoleteTypeDefName);
        }

        log.debug("Removed Type {} from {}", obsoleteTypeDefName, sourceName);
        uncacheTypeDefPropertyLookup(sourceName, obsoleteTypeDefName);
    }


    /**
     * Remove a TypeDef from the reverse property lookup.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDefName unique name for the type.
     */
    private void uncacheTypeDefPropertyLookup(String sourceName, String typeDefName)
    {
        // Not much choice but to iterate through the entire Map...
        for (String propertyName : knownPropertyToTypeDefNames.keySet())
        {
            // ... but the removal operation at least is idempotent (no need to first check it is present in the Set)
            boolean removed = knownPropertyToTypeDefNames.get(propertyName).remove(typeDefName);
            if (removed)
            {
                log.debug("Removed Type {} from {}, from reverse-lookup of property: {}", typeDefName, sourceName, propertyName);
            }
        }
    }


    /**
     * Cache a definition of a new AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param newAttributeTypeDef AttributeTypeDef structure describing the new TypeDef.
     */
    @Override
    public void addAttributeTypeDef(String  sourceName, AttributeTypeDef newAttributeTypeDef)
    {
        this.cacheAttributeTypeDef(sourceName, newAttributeTypeDef, true);
    }


    /**
     * Cache a definition of a new AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param newAttributeTypeDef AttributeTypeDef structure describing the new TypeDef.
     * @param isLocallySupported indicates whether the TypeDef is supported by the local repository.
     */
    private void cacheAttributeTypeDef(String           sourceName,
                                       AttributeTypeDef newAttributeTypeDef,
                                       boolean          isLocallySupported)
    {
        knownAttributeTypeDefGUIDs.put(newAttributeTypeDef.getGUID(), newAttributeTypeDef);
        knownAttributeTypeDefNames.put(newAttributeTypeDef.getName(), newAttributeTypeDef);

        if (isLocallySupported)
        {
            activeAttributeTypeDefGUIDs.put(newAttributeTypeDef.getGUID(), newAttributeTypeDef);
            activeAttributeTypeDefNames.put(newAttributeTypeDef.getName(), newAttributeTypeDef);

            if (log.isDebugEnabled())
            {
                log.debug("New Active Attribute Type " + newAttributeTypeDef.getName() + " from " + sourceName+ ". Full AttributeTypeDef: " + newAttributeTypeDef);
            }
            else
            {
                log.debug("New Known Attribute Type " + newAttributeTypeDef.getName() + " from " + sourceName+ ". Full AttributeTypeDef: " + newAttributeTypeDef);
            }
        }
    }


    /**
     * Update one or more properties of a cached TypeDef.  This method assumes the TypeDef has been successfully
     * updated in the local repository already and all that is needed is to maintain the cached list of types
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDef TypeDef structure.
     */
    @Override
    public void updateTypeDef(String  sourceName, TypeDef   typeDef)
    {
        this.cacheTypeDef(sourceName, typeDef,true);
    }


    /**
     * Delete a cached TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param obsoleteTypeDefGUID String unique identifier for the TypeDef.
     * @param obsoleteTypeDefName String unique name for the TypeDef.
     */
    @Override
    public void deleteTypeDef(String    sourceName,
                              String    obsoleteTypeDefGUID,
                              String    obsoleteTypeDefName)
    {
        if (this.validTypeId(sourceName, obsoleteTypeDefGUID, obsoleteTypeDefName))
        {
            knownTypeDefGUIDs.remove(obsoleteTypeDefGUID);
            knownTypeDefNames.remove(obsoleteTypeDefName);

            if (localRepositoryConnector != null)
            {
                activeTypeDefGUIDs.remove(obsoleteTypeDefGUID);
                activeTypeDefNames.remove(obsoleteTypeDefName);

                log.debug("Deleted Active TypeDef " + obsoleteTypeDefName + " from " + sourceName);
            }
        }
    }


    /**
     * Delete a cached AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param obsoleteAttributeTypeDefGUID String unique identifier for the AttributeTypeDef.
     * @param obsoleteAttributeTypeDefName String unique name for the AttributeTypeDef.
     */
    @Override
    public void deleteAttributeTypeDef(String    sourceName,
                                       String    obsoleteAttributeTypeDefGUID,
                                       String    obsoleteAttributeTypeDefName)
    {
        if (this.validTypeId(sourceName, obsoleteAttributeTypeDefGUID, obsoleteAttributeTypeDefName))
        {
            knownAttributeTypeDefGUIDs.remove(obsoleteAttributeTypeDefGUID);
            knownAttributeTypeDefNames.remove(obsoleteAttributeTypeDefName);

            if (localRepositoryConnector != null)
            {
                activeAttributeTypeDefGUIDs.remove(obsoleteAttributeTypeDefGUID);
                activeAttributeTypeDefNames.remove(obsoleteAttributeTypeDefName);

                if (log.isDebugEnabled())
                {
                    log.debug("Deleted Active AttributeTypeDef " + obsoleteAttributeTypeDefName + " from " + sourceName);
                }
            }
        }
    }


    /**
     * Change the identifiers for a TypeDef.
     *
     * @param sourceName source of the request (used for logging).
     * @param originalTypeDefGUID TypeDef's original unique identifier.
     * @param originalTypeDefName TypeDef's original unique name.
     * @param newTypeDef updated TypeDef with new identifiers.
     */
    @Override
    public void reIdentifyTypeDef(String   sourceName,
                                  String   originalTypeDefGUID,
                                  String   originalTypeDefName,
                                  TypeDef  newTypeDef)
    {
        this.deleteTypeDef(sourceName, originalTypeDefGUID, originalTypeDefName);
        this.addTypeDef(sourceName, newTypeDef);
    }


    /**
     * Change the identifiers for an AttributeTypeDef.
     *
     * @param sourceName source of the request (used for logging).
     * @param originalAttributeTypeDefGUID AttributeTypeDef's original unique identifier.
     * @param originalAttributeTypeDefName AttributeTypeDef's original unique name.
     * @param newAttributeTypeDef updated AttributeTypeDef with new identifiers
     */
    @Override
    public void reIdentifyAttributeTypeDef(String            sourceName,
                                           String            originalAttributeTypeDefGUID,
                                           String            originalAttributeTypeDefName,
                                           AttributeTypeDef  newAttributeTypeDef)
    {
        this.deleteAttributeTypeDef(sourceName, originalAttributeTypeDefGUID, originalAttributeTypeDefName);
        this.addAttributeTypeDef(sourceName, newAttributeTypeDef);
    }


    /**
     * Return the list of property names defined for this TypeDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeDef type definition to work with.
     * @param methodName calling method
     * @return list of String property names
     * @throws TypeErrorException there is an issue with the TypeDef.
     */
    private List<String>  getPropertyNames(String    sourceName,
                                           TypeDef   typeDef,
                                           String    methodName) throws TypeErrorException
    {
        final  String             thisMethodName = "getPropertyNames";
        List<String>              propertyNames = null;

        if (validTypeDef(sourceName, typeDef))
        {
            List<TypeDefAttribute> propertiesDefinition = typeDef.getPropertiesDefinition();

            if ((propertiesDefinition != null) && (propertiesDefinition.size() > 0))
            {
                propertyNames = new ArrayList<>();

                for (TypeDefAttribute  propertyDefinition : propertiesDefinition)
                {
                    if (propertyDefinition != null)
                    {
                        String propertyName = propertyDefinition.getAttributeName();

                        if (propertyName != null)
                        {
                            log.debug(typeDef.getName()  + " from " + sourceName + " has property " + propertyName);

                            propertyNames.add(propertyName);
                        }
                        else
                        {
                            throw new TypeErrorException(OMRSErrorCode.BAD_TYPEDEF_ATTRIBUTE_NAME.getMessageDefinition(sourceName),
                                                         this.getClass().getName(),
                                                         methodName);
                        }
                    }
                    else
                    {
                        throw new TypeErrorException(OMRSErrorCode.NULL_TYPEDEF_ATTRIBUTE.getMessageDefinition(sourceName),
                                                     this.getClass().getName(),
                                                     methodName);
                    }
                }

                /*
                 * If no property names have been extracted then remove the array.
                 */
                if (propertyNames.size() == 0)
                {
                    propertyNames = null;
                }
            }
        }
        else
        {
            throw new TypeErrorException(OMRSErrorCode.BAD_TYPEDEF.getMessageDefinition(thisMethodName,
                                                                                        typeDef.getName(),
                                                                                        sourceName,
                                                                                        methodName),
                                         this.getClass().getName(),
                                         methodName);
        }

        return propertyNames;
    }


    /**
     * Evaluate the superTypes for a type.  The results are cached in typeDefSuperTypes.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeName name of type to process
     * @param methodName calling method
     * @return list of supertype links or null if top level
     */
    private List<TypeDefLink>   getSuperTypes(String    sourceName,
                                              String    typeName,
                                              String    methodName)
    {
        final String  thisMethodName = "getSuperTypes";

        List<TypeDefLink>   typeHierarchy = typeDefSuperTypes.get(typeName);

        if (typeHierarchy == null)
        {
            /*
             * The type hierarchy is not known at this time (it is evaluated lazily).
             */
            typeHierarchy = new ArrayList<>();

            TypeDef typeDef = knownTypeDefNames.get(typeName);

            if (typeDef != null)
            {
                TypeDefLink superTypeLink = typeDef.getSuperType();

                while (superTypeLink != null)
                {
                    String superTypeName = superTypeLink.getName();

                    if (superTypeName != null)
                    {
                        log.debug(typeName + " has super type " + superTypeName);

                        typeHierarchy.add(superTypeLink);

                        /*
                         * Retrieve the TypeDef for this super type
                         */
                        TypeDef superTypeDef = knownTypeDefNames.get(superTypeName);

                        if (superTypeDef != null)
                        {
                            /*
                             * Retrieve the super type for this super typeDef.  It will be null if the type is top-level.
                             */
                            superTypeLink = superTypeDef.getSuperType();
                        }
                        else
                        {
                            log.error(superTypeName + " supertype is not known in TypeDef cache");
                            throwContentManagerLogicError(sourceName, methodName, thisMethodName);
                        }
                    }
                    else
                    {
                        log.error("Corrupted TypeDef cache, no name for " + superTypeLink.toString());
                        throwContentManagerLogicError(sourceName, methodName, thisMethodName);
                    }
                }

                /*
                 * Cache the resulting superType list
                 */
                typeDefSuperTypes.put(typeName, typeHierarchy);
            }
            else
            {
                log.error(typeName + " type is not known in TypeDef cache");
                throwContentManagerLogicError(sourceName, methodName, thisMethodName + "(" + typeName + ")");
            }
        }

        if (typeHierarchy.isEmpty())
        {
            /*
             * The type hierarchy has been evaluated and this type has no supertypes.
             */
            return null;
        }
        else
        {
            /*
             * The type hierarchy is known and can be returned directly.
             */
            return typeHierarchy;
        }
    }


    /**
     * Validate that the type of an entity is of the expected/desired type.  The actual entity may be a subtype
     * of the expected type of course.
     *
     * @param sourceName source of the request (used for logging)
     * @param actualTypeName name of the entity type
     * @param expectedTypeName name of the expected type
     * @return boolean if they match (a null in actualTypeName results in false; a null in expectedType results in true)
     */
    boolean  isTypeOf(String   sourceName,
                      String   actualTypeName,
                      String   expectedTypeName)
    {
        final String methodName = "isTypeOf";

        log.debug("isTypeOf: sourceName = " + sourceName + "; actualTypeName = " + actualTypeName + "; expectedTypeName = " + expectedTypeName);

        if (expectedTypeName == null)
        {
            /*
             * If the expected type name is null, it means that any type is allowed.
             */
            return true;
        }

        /*
         * If the actual type is null then the object retrieved is a bit weird.  It is treated as not
         * matching on type.
         */
        if (actualTypeName == null)
        {
            return false;
        }

        /*
         * Do the obvious first.
         */
        if (actualTypeName.equals(expectedTypeName))
        {
            log.debug("Simple match success");
            return true;
        }

        /*
         * Looking for a match in the superTypes.
         */
        List<TypeDefLink>   typeHierarchy = this.getSuperTypes(sourceName, actualTypeName, methodName);

        if (typeHierarchy != null)
        {
            for (TypeDefLink superType : typeHierarchy)
            {
                if (superType != null)
                {
                    if (expectedTypeName.equals(superType.getName()))
                    {
                        log.debug("SuperType match success");
                        return true;
                    }

                    log.debug("No match with " + superType.getName());
                }
            }
        }

        /*
         * No match found
         */
        return false;
    }


    /**
     * Validate that the type of an entity is of the expected/desired type.  The actual entity may be a subtype
     * of the expected type of course.
     *
     * @param sourceName source of the request (used for logging)
     * @param actualTypeGUID GUID of the entity type
     * @param actualTypeName name of the entity type
     * @param expectedTypeGUID GUID of the expected type
     * @return boolean if they match (a null in either results in false)
     */
    boolean  isTypeOfByGUID(String   sourceName,
                            String   actualTypeGUID,
                            String   actualTypeName,
                            String   expectedTypeGUID)
    {
        final String methodName = "isTypeOfByGUID";

        log.debug("IsTypeOfByGUID: sourceName = " + sourceName + "; actualTypeName = " + actualTypeName + "; expectedTypeGUID = " + expectedTypeGUID);

        if (expectedTypeGUID == null)
        {
            /*
             * If the expected type GUID is null, it means that any type is allowed.
             */
            log.debug("Any type will do");
            return true;
        }

        /*
         * If the actual type is null then the object retrieved is a bit weird.  It is treated as not
         * matching on type.
         */
        if (actualTypeGUID == null)
        {
            log.debug("No type to test against");
            return false;
        }

        /*
         * Do the obvious first.
         */
        if (actualTypeGUID.equals(expectedTypeGUID))
        {
            log.debug("Simple match success");
            return true;
        }

        /*
         * Looking for a match in the superTypes.
         */
       List<TypeDefLink>   typeHierarchy = this.getSuperTypes(sourceName, actualTypeName, methodName);

        if (typeHierarchy != null)
        {
            for (TypeDefLink superType : typeHierarchy)
            {
                if (superType != null)
                {
                    if (expectedTypeGUID.equals(superType.getGUID()))
                    {
                        log.debug("SuperType match success");
                        return true;
                    }
                    log.debug("No match with " + superType.getGUID());
                }
            }
        }

        return false;
    }


    /**
     * Return the InstanceType that matches the supplied type name.  If the type name is not recognized,
     * of the category is incorrect, a logic exception is thrown.
     *
     * @param sourceName source of the request (used for logging)
     * @param category category of the instance type required.
     * @param typeName String type name.
     * @param methodName name of calling method.
     * @return InstanceType object containing TypeDef properties such as unique identifier (guid),
     *                             typeDef name and version name
     * @throws TypeErrorException the type name is not a recognized type or is of the wrong category or there is
     *                              a problem with the cached TypeDef.
     */
    @Override
    public InstanceType getInstanceType(String          sourceName,
                                        TypeDefCategory category,
                                        String          typeName,
                                        String          methodName) throws TypeErrorException
    {
        final String thisMethodName = "getInstanceType";

        if (isValidTypeCategory(sourceName, category, typeName, methodName))
        {
            InstanceType    instanceType = knownInstanceTypes.get(typeName);

            if (instanceType != null)
            {
                return instanceType;
            }

            /*
             * The instance type has not yet been created. (They are created lazily.)
             */
            TypeDef typeDef = knownTypeDefNames.get(typeName);

            if (typeDef != null)
            {
                instanceType = new InstanceType();

                instanceType.setTypeDefCategory(category);
                instanceType.setTypeDefGUID(typeDef.getGUID());
                instanceType.setTypeDefName(typeDef.getName());
                instanceType.setTypeDefVersion(typeDef.getVersion());
                instanceType.setTypeDefDescription(typeDef.getDescription());
                instanceType.setTypeDefDescriptionGUID(typeDef.getDescriptionGUID());
                instanceType.setTypeDefSuperTypes(this.getSuperTypes(sourceName, typeName, methodName));

                /*
                 * Extract the properties for this TypeDef.  These will be augmented with property names
                 * from the super type(s).
                 */
                List<String>      propertyNames = this.getPropertyNames(sourceName, typeDef, methodName);

                /*
                 * If propertyNames is null, it means this TypeDef has no attributes.  However the superType
                 * may have attributes and so we need an array to accumulate the attributes into.
                 */
                if (propertyNames == null)
                {
                    propertyNames = new ArrayList<>();
                }

                /*
                 * Work up the TypeDef hierarchy extracting the property names.
                 */
                TypeDefLink       superTypeLink = typeDef.getSuperType();

                while (superTypeLink != null)
                {
                    String             superTypeName = superTypeLink.getName();

                    if (superTypeName != null)
                    {
                        log.debug(typeName + " from " + sourceName + " has super type " + superTypeName);

                        /*
                         * Retrieve the TypeDef for this super type
                         */
                        TypeDef         superTypeDef  = knownTypeDefNames.get(superTypeName);

                        if (superTypeDef != null)
                        {
                            List<String>      superTypePropertyNames = this.getPropertyNames(sourceName, superTypeDef, methodName);

                            if (superTypePropertyNames != null)
                            {
                                propertyNames.addAll(0, superTypePropertyNames);
                            }

                            /*
                             * Retrieve the super type for this typeDef.  It will be null if the type is top-level.
                             */
                            superTypeLink = superTypeDef.getSuperType();
                        }
                        else
                        {
                            /*
                             * Super type not known so stop processing
                             */
                            log.error(superTypeName + " is not known");
                            throwContentManagerLogicError(sourceName, methodName, thisMethodName);
                        }
                    }
                    else
                    {
                        /*
                         * Super type is invalid, suggests a corrupted cache
                         */
                        log.error("Corrupted TypeDef cache");
                        throwContentManagerLogicError(sourceName, methodName, thisMethodName);
                    }
                }

                /*
                 * Make sure an empty list is converted to null
                 */
                if (propertyNames.size() > 0)
                {
                    instanceType.setValidInstanceProperties(propertyNames);
                }

                /*
                 * Cache the instance type for next time
                 */
                knownInstanceTypes.put(typeName, instanceType);

                return instanceType;
            }
            else
            {
                log.error("TypeDef " + typeName + " should already have been validated");
                throwContentManagerLogicError(sourceName, methodName, thisMethodName);
            }
        }
        else
        {
            throw new TypeErrorException(OMRSErrorCode.BAD_CATEGORY_FOR_TYPEDEF_ATTRIBUTE.getMessageDefinition(sourceName,
                                                                                                               typeName,
                                                                                                               category.getName()),
                                         this.getClass().getName(),
                                         methodName);
        }

        return null;
    }


    /**
     * Return a boolean indicating that the type name matches the category.
     *
     * @param sourceName source of the request (used for logging)
     * @param category TypeDefCategory enum value to test
     * @param typeName type name to test
     * @return boolean flag indicating that the type name is of the specified category
     * @throws TypeErrorException the type name is not a recognized type or there is
     *                              a problem with the cached TypeDef.
     */
    @Override
    public boolean    isValidTypeCategory(String            sourceName,
                                          TypeDefCategory   category,
                                          String            typeName,
                                          String            methodName) throws TypeErrorException
    {
        final String  thisMethodName = "isValidTypeCategory";

        if (category == null)
        {
            throwContentManagerLogicError(sourceName, methodName, thisMethodName);
            return false;
        }

        if (typeName == null)
        {
            throwContentManagerLogicError(sourceName, methodName, thisMethodName);
            return false;
        }

        TypeDef   typeDef = knownTypeDefNames.get(typeName);

        if (typeDef != null)
        {
            TypeDefCategory  retrievedTypeDefCategory = typeDef.getCategory();

            if (retrievedTypeDefCategory != null)
            {
                return (category.getOrdinal() == retrievedTypeDefCategory.getOrdinal());
            }
            else
            {
                throwContentManagerLogicError(sourceName, methodName, thisMethodName);
            }
        }
        else
        {
            throw new TypeErrorException(OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE.getMessageDefinition(typeName,
                                                                                                           category.getName(),
                                                                                                           methodName,
                                                                                                           sourceName),
                                         this.getClass().getName(),
                                         methodName);
        }

        return false;
    }


    /**
     * Return boolean indicating if a classification type can be applied to a specified entity.  This
     * uses the list of valid entity types located in the ClassificationDef.
     *
     * @param sourceName source of the request (used for logging)
     * @param classificationTypeName name of the classification's type (ClassificationDef)
     * @param entityTypeName name of the entity's type (EntityDef)
     * @param methodName name of calling method.
     * @return boolean indicating if the classification is valid for the entity.
     */
    @Override
    public boolean    isValidClassificationForEntity(String  sourceName,
                                                     String  classificationTypeName,
                                                     String  entityTypeName,
                                                     String  methodName)
    {
        final String  thisMethodName = "isValidClassificationForEntity";

        try
        {
            if ((isValidTypeCategory(sourceName, TypeDefCategory.CLASSIFICATION_DEF, classificationTypeName, methodName)) &&
                (isValidTypeCategory(sourceName, TypeDefCategory.ENTITY_DEF, entityTypeName, methodName)))
            {
                ClassificationDef  classificationTypeDef = (ClassificationDef) knownTypeDefNames.get(classificationTypeName);

                if (classificationTypeDef != null)
                {
                    List<TypeDefLink>   entityDefs = classificationTypeDef.getValidEntityDefs();

                    if (entityDefs == null || entityDefs.isEmpty())
                    {
                        /*
                         * The classification has no restrictions on which entities it can be attached to.
                         */
                        return true;
                    }
                    else
                    {
                        /*
                         * The classification can only be attached to the entities listed.  Note an empty list
                         * means the classification can not be attached to any entity and it is effectively useless.
                         * The logic checks the entity types parent types, as the parent types may allow the classification.
                         *
                         * The Archive types at this time do not have any ClassificationDefs with supertypes. If we want to support
                         * ClassificationDefs with supertypes then we need to account for any entities that the ClassificationDef
                         * supertype can introduce.
                         */
                        Set<String> entityTypes = new HashSet<>();
                        TypeDef typeDef = getTypeDefByName(entityTypeName);
                        entityTypes.add(entityTypeName);

                        while (typeDef.getSuperType() !=null)
                        {
                            TypeDefLink superTypeLink=typeDef.getSuperType();
                            String parentName= superTypeLink.getName();
                            entityTypes.add(parentName);
                            typeDef  = this.knownTypeDefGUIDs.get(superTypeLink.getGUID());
                        }

                        for (TypeDefLink  allowedEntityDefLink : entityDefs)
                        {
                            if (allowedEntityDefLink != null)
                            {
                                String allowedTypeName = allowedEntityDefLink.getName();
                                if (entityTypes.contains(allowedTypeName))
                                {
                                    return true;
                                }
                            }
                        }

                        return false;
                    }
                }
                else
                {
                    throwContentManagerLogicError(sourceName, methodName, thisMethodName);
                    return false;
                }
            }
            else
            {
                return false;
            }
        }
        catch (TypeErrorException | ClassCastException  typeError)
        {
            throwContentManagerLogicError(sourceName, methodName, thisMethodName);
            return false;
        }
    }


    /**
     * Return the requested type.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeName name of the type
     * @param thisMethodName name of calling method.
     * @param originalMethodName name of original calling method.
     * @return list of InstanceStatus enums
     * @throws TypeErrorException the type name is not recognized.
     */
    private TypeDef getTypeDefFromCache(String  sourceName,
                                        String  typeName,
                                        String  thisMethodName,
                                        String  originalMethodName) throws TypeErrorException
    {
        if (typeName == null)
        {
            this.throwContentManagerLogicError(sourceName, thisMethodName, originalMethodName);
        }

        TypeDef   typeDef = knownTypeDefNames.get(typeName);

        if (typeDef == null)
        {
            throw new TypeErrorException(OMRSErrorCode.TYPEDEF_NAME_NOT_KNOWN.getMessageDefinition(typeName, originalMethodName, sourceName),
                                         this.getClass().getName(),
                                         originalMethodName);
        }

        return typeDef;
    }


    /**
     * Return the initial status value to use for an instance of the supplied type.
     *
     * @param sourceName source of the request (used for logging)
     * @param typeName name of the type to extract the initial status from.
     * @param methodName calling method
     * @return InstanceStatus enum
     * @throws TypeErrorException the type name is not recognized.
     */
    @Override
    public InstanceStatus getInitialStatus(String sourceName,
                                           String typeName,
                                           String methodName) throws TypeErrorException
    {
        final String thisMethodName = "getInitialStatus";

        TypeDef   typeDef = this.getTypeDefFromCache(sourceName, typeName, thisMethodName, methodName);

        return typeDef.getInitialStatus();
    }


    /**
     * Return the URL string to use for direct access to the metadata instance.
     *
     * @param sourceName source of the request (used for logging)
     * @param guid unique identifier for the instance.
     * @return String URL with placeholder for variables such as userId.
     */
    @Override
    public String getEntityURL(String  sourceName, String guid)
    {
        return OMRSRepositoryRESTServices.getEntityURL(localServerName, guid);
    }


    /**
     * Return the URL string to use for direct access to the metadata instance.
     *
     * @param sourceName source of the request (used for logging)
     * @param guid unique identifier for the instance.
     * @return String URL with placeholder for variables such as userId.
     */
    @Override
    public String getRelationshipURL(String  sourceName, String guid)
    {
        return OMRSRepositoryRESTServices.getRelationshipURL(localServerName, guid);
    }


    /**
     * Return the list of typedefs known by the local repository.
     *
     * @return TypeDef gallery
     */
    TypeDefGallery   getActiveTypeDefGallery()
    {
        TypeDefGallery               typeDefGallery               = new TypeDefGallery();

        if (! activeAttributeTypeDefNames.isEmpty())
        {
            typeDefGallery.setAttributeTypeDefs(new ArrayList<>(activeAttributeTypeDefNames.values()));
        }

        if (! activeTypeDefNames.isEmpty())
        {
            typeDefGallery.setTypeDefs(new ArrayList<>(activeTypeDefNames.values()));
        }

        return typeDefGallery;
    }


    /**
     * Return the list of typeDefs active in the local repository.
     *
     * @return TypeDef list
     */
    List<TypeDef>  getActiveTypeDefs()
    {
        List<TypeDef> results = null;

        if (! activeTypeDefGUIDs.isEmpty())
        {
            results = new ArrayList<>(activeTypeDefGUIDs.values());
        }

        return results;
    }


    /**
     * Return the list of attributeTypeDefs active in the local repository.
     *
     * @return AttributeTypeDef list
     */
    List<AttributeTypeDef>  getActiveAttributeTypeDefs()
    {
        List<AttributeTypeDef> results = null;

        if (! activeAttributeTypeDefGUIDs.isEmpty())
        {
            results = new ArrayList<>(activeAttributeTypeDefGUIDs.values());
        }

        return results;
    }


    /**
     * Return the list of typedefs active in the connected cohorts.
     *
     * @return TypeDef gallery
     */
    TypeDefGallery   getKnownTypeDefGallery()
    {
        TypeDefGallery               typeDefGallery               = new TypeDefGallery();

        if (! knownAttributeTypeDefNames.isEmpty())
        {
            typeDefGallery.setAttributeTypeDefs(new ArrayList<>(knownAttributeTypeDefNames.values()));
        }

        if (! knownTypeDefNames.isEmpty())
        {
            typeDefGallery.setTypeDefs(new ArrayList<>(knownTypeDefNames.values()));
        }

        return typeDefGallery;
    }


    /**
     * Return the list of typeDefs active in the connected cohorts.
     *
     * @return TypeDef list
     */
    List<TypeDef>  getKnownTypeDefs()
    {
        List<TypeDef> results = null;

        if (! knownTypeDefGUIDs.isEmpty())
        {
            results = new ArrayList<>(knownTypeDefGUIDs.values());
        }

        return results;
    }


    /**
     * Return the list of attributeTypeDefs active in the local repository.
     *
     * @return AttributeTypeDef list
     */
    List<AttributeTypeDef>  getKnownAttributeTypeDefs()
    {
        List<AttributeTypeDef> results = null;

        if (! knownAttributeTypeDefGUIDs.isEmpty())
        {
            results = new ArrayList<>(knownAttributeTypeDefGUIDs.values());
        }

        return results;
    }


    /**
     * Return the TypeDef identified by the name supplied by the caller.  This is used in the connectors when
     * validating the actual types of the repository with the known open metadata types.  It is looking specifically
     * for types of the same name but with different content.
     *
     * @param typeDefName unique name for the TypeDef
     * @return TypeDef object or null if TypeDef is not known.
     */
    TypeDef  getTypeDefByName(String    typeDefName)
    {
        return knownTypeDefNames.get(typeDefName);
    }


    /**
     * Return the AttributeTypeDef identified by the name supplied by the caller.  This is used in the connectors when
     * validating the actual types of the repository with the known open metadata types.  It is looking specifically
     * for types of the same name but with different content.
     *
     * @param attributeTypeDefName unique name for the TypeDef
     * @return AttributeTypeDef object or null if AttributeTypeDef is not known.
     */
    AttributeTypeDef getAttributeTypeDefByName(String    attributeTypeDefName)
    {
        return knownAttributeTypeDefNames.get(attributeTypeDefName);
    }



    /**
     * Return the TypeDef identified by the guid supplied by the caller.  This call is used when
     * retrieving a type that only the guid is known.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of parameter that supplied the GUID
     * @param typeDefGUID unique identifier for the TypeDef
     * @param methodName calling method
     * @return TypeDef object
     * @throws TypeErrorException unknown or invalid type
     */
    TypeDef  getTypeDef (String    sourceName,
                         String    guidParameterName,
                         String    typeDefGUID,
                         String    methodName) throws TypeErrorException
    {
        final String thisMethodName = "getTypeDef";

        if (typeDefGUID != null)
        {
            TypeDef typeDef = knownTypeDefGUIDs.get(typeDefGUID);

            if (typeDef == null)
            {
                throw new TypeErrorException(OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN.getMessageDefinition(typeDefGUID,
                                                                                                     guidParameterName,
                                                                                                     methodName,
                                                                                                     sourceName),
                                             this.getClass().getName(),
                                             methodName);
            }

            return typeDef;
        }

        throwContentManagerLogicError(sourceName, methodName, thisMethodName);
        return null;
    }


    /**
     * Return the AttributeTypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that only the guid is known.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier for the AttributeTypeDef
     * @param methodName calling method
     * @return TypeDef object
     * @throws TypeErrorException unknown or invalid type
     */
    AttributeTypeDef  getAttributeTypeDef (String    sourceName,
                                           String    attributeTypeDefGUID,
                                           String    methodName) throws TypeErrorException
    {
        final String thisMethodName = "getAttributeTypeDef";

        if (attributeTypeDefGUID != null)
        {
            AttributeTypeDef attributeTypeDef = knownAttributeTypeDefGUIDs.get(attributeTypeDefGUID);

            if (attributeTypeDef == null)
            {
                throw new TypeErrorException(OMRSErrorCode.BAD_TYPEDEF.getMessageDefinition(thisMethodName,
                                                                                            attributeTypeDefGUID,
                                                                                            sourceName,
                                                                                            methodName),
                                             this.getClass().getName(),
                                             methodName);
            }

            return attributeTypeDef;
        }

        throwContentManagerLogicError(sourceName, methodName, thisMethodName);
        return null;
    }


    /**
     * Return the TypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that should exist.  For example, retrieving the type of a metadata instance.
     *
     * @param sourceName source of the request (used for logging)
     * @param guidParameterName name of parameter that supplied the GUID
     * @param nameParameterName name of parameter that supplied the name
     * @param typeDefGUID unique identifier for the TypeDef
     * @param typeDefName unique name for the TypeDef
     * @param methodName calling method
     * @return TypeDef object or null if the
     * @throws TypeErrorException invalid type
     */
    TypeDef  getTypeDef (String    sourceName,
                         String    guidParameterName,
                         String    nameParameterName,
                         String    typeDefGUID,
                         String    typeDefName,
                         String    methodName) throws TypeErrorException
    {
        if (validTypeId(sourceName, typeDefGUID, typeDefName))
        {
            return knownTypeDefNames.get(typeDefName);
        }
        else
        {
            throw new TypeErrorException(OMRSErrorCode.UNKNOWN_TYPEDEF.getMessageDefinition(sourceName,
                                                                                            typeDefName,
                                                                                            typeDefGUID,
                                                                                            methodName,
                                                                                            nameParameterName,
                                                                                            guidParameterName),
                                         this.getClass().getName(),
                                         methodName);
        }
    }


    /**
     * Return the AttributeTypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that should exist.  For example, retrieving the type definition of a metadata instance's
     * property.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier for the AttributeTypeDef
     * @param attributeTypeDefName unique name for the AttributeTypeDef
     * @param methodName calling method
     * @return TypeDef object
     * @throws TypeErrorException unknown or invalid type
     */
    AttributeTypeDef  getAttributeTypeDef (String    sourceName,
                                           String    attributeTypeDefGUID,
                                           String    attributeTypeDefName,
                                           String    methodName) throws TypeErrorException
    {
        final String thisMethodName = "getAttributeTypeDef";

        if (validTypeId(sourceName, attributeTypeDefGUID, attributeTypeDefName))
        {
            return knownAttributeTypeDefNames.get(attributeTypeDefName);
        }
        else
        {
            throw new TypeErrorException(OMRSErrorCode.BAD_TYPEDEF.getMessageDefinition(thisMethodName,
                                                                                        attributeTypeDefName,
                                                                                        sourceName,
                                                                                        methodName),
                                         this.getClass().getName(),
                                         methodName);
        }
    }


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param enumTypeGUID unique Id of Enum requested
     * @param enumTypeName unique name of enum requested
     * @param ordinal numeric value of property
     * @param methodName calling method name
     * @return instance properties object.
     * @throws TypeErrorException the enum type is not recognized
     */
    InstanceProperties addEnumPropertyToInstance(String             sourceName,
                                                 InstanceProperties properties,
                                                 String             propertyName,
                                                 String             enumTypeGUID,
                                                 String             enumTypeName,
                                                 int                ordinal,
                                                 String             methodName) throws TypeErrorException
    {
        final String thisMethodName = "addEnumPropertyToInstance";

        InstanceProperties  resultingProperties;

        log.debug("Adding property " + propertyName + " for " + methodName);

        if (properties == null)
        {
            log.debug("First property");

            resultingProperties = new InstanceProperties();
        }
        else
        {
            resultingProperties = properties;
        }

        AttributeTypeDef attributeTypeDef = this.getAttributeTypeDef(sourceName, enumTypeGUID, enumTypeName, methodName);

        if (attributeTypeDef instanceof EnumDef)
        {
            EnumDef enumDef = (EnumDef)attributeTypeDef;

            List<EnumElementDef> enumDefValues = enumDef.getElementDefs();

            if (enumDefValues != null)
            {
                for (EnumElementDef  enumElementDef : enumDefValues)
                {
                    if (enumElementDef != null)
                    {
                        if (enumElementDef.getOrdinal() == ordinal)
                        {
                            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();

                            enumPropertyValue.setOrdinal(ordinal);
                            enumPropertyValue.setSymbolicName(enumElementDef.getValue());
                            enumPropertyValue.setDescription(enumElementDef.getDescription());

                            resultingProperties.setProperty(propertyName, enumPropertyValue);

                            return resultingProperties;
                        }
                    }
                }
            }
        }

        throw new TypeErrorException(OMRSErrorCode.BAD_TYPEDEF.getMessageDefinition(thisMethodName,
                                                                                    enumTypeName,
                                                                                    sourceName,
                                                                                    methodName),
                                     this.getClass().getName(),
                                     methodName);
    }


    /**
     * Return the names of all of the properties in the supplied TypeDef and all of its super-types.
     *
     * @param sourceName name of caller.
     * @param typeDef TypeDef to query.
     * @param methodName calling method.
     * @return list of property definitions.
     */
    List<TypeDefAttribute> getAllPropertiesForTypeDef(String  sourceName,
                                                      TypeDef typeDef,
                                                      String  methodName)
    {
        final  String             thisMethodName = "getAllPropertiesForTypeDef";

        if (typeDef == null)
        {
            throwContentManagerLogicError(sourceName, methodName, thisMethodName);
            return null;
        }

        List<TypeDefAttribute>    propertiesDefinition = typeDef.getPropertiesDefinition();

        /*
         * If propertiesDefinition is null, it means the TypeDef has no properties defined.  However the superType
         * may have properties defined and so we need an array to accumulate the property definitions into.
         */
        if (propertiesDefinition == null)
        {
            propertiesDefinition = new ArrayList<>();
        }

        /*
         * Work up the TypeDef hierarchy extracting the property definitions.
         */
        TypeDefLink            superTypeLink = typeDef.getSuperType();

        while (superTypeLink != null)
        {
            TypeDef                superTypeDef                  = this.knownTypeDefGUIDs.get(superTypeLink.getGUID());
            List<TypeDefAttribute> superTypePropertiesDefinition = superTypeDef.getPropertiesDefinition();

            if (superTypePropertiesDefinition != null)
            {
                propertiesDefinition.addAll(superTypePropertiesDefinition);
            }

            superTypeLink = superTypeDef.getSuperType();
        }

        return propertiesDefinition;
    }


    /**
     * Return the names of all of the type definitions that define the supplied property name.
     *
     * @param sourceName name of the caller.
     * @param propertyName property name to query.
     * @param methodName calling method.
     * @return set of names of the TypeDefs that define a property with this name
     */
    Set<String> getAllTypeDefsForProperty(String sourceName,
                                          String propertyName,
                                          String methodName)
    {
        final String               thisMethodName                = "getAllTypeDefsForProperty";

        if (propertyName == null)
        {
            throwContentManagerLogicError(sourceName, methodName, thisMethodName);
            return null;
        }

        return knownPropertyToTypeDefNames.getOrDefault(propertyName, null);
    }


    /**
     * Return a boolean flag indicating whether the list of TypeDefs passed are compatible with the
     * all known typedefs.
     *
     * A valid TypeDef is one that matches name, GUID and version to the full list of TypeDefs.
     * If a new TypeDef is present, it is added to the enterprise list.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeDefs list of TypeDefs.
     * @param methodName name of calling method
     * @throws RepositoryErrorException a conflicting or invalid TypeDef has been returned
     */
    void   validateEnterpriseTypeDefs(String        sourceName,
                                      List<TypeDef> typeDefs,
                                      String        methodName) throws RepositoryErrorException
    {
        for (TypeDef typeDef : typeDefs)
        {
            if (validTypeId(sourceName, typeDef.getGUID(), typeDef.getName()))
            {
                if (!isKnownType(sourceName, typeDef.getGUID(), typeDef.getName()))
                {
                    knownTypeDefNames.put(typeDef.getName(), typeDef);
                }
            }
            else
            {
                throw new RepositoryErrorException(OMRSErrorCode.CONFLICTING_ENTERPRISE_TYPEDEFS.getMessageDefinition(),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }
    }


    /**
     * Return a boolean flag indicating whether the list of TypeDefs passed are compatible with the
     * all known typedefs.
     *
     * A valid TypeDef is one that matches name, GUID and version to the full list of TypeDefs.
     * If a new TypeDef is present, it is added to the enterprise list.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param attributeTypeDefs list of AttributeTypeDefs.
     * @param methodName name of calling method
     * @throws RepositoryErrorException a conflicting or invalid AttributeTypeDef has been returned
     */
    void   validateEnterpriseAttributeTypeDefs(String                 sourceName,
                                               List<AttributeTypeDef> attributeTypeDefs,
                                               String                 methodName) throws RepositoryErrorException
    {
        for (AttributeTypeDef attributeTypeDef : attributeTypeDefs)
        {
            if (validTypeId(sourceName, attributeTypeDef.getGUID(), attributeTypeDef.getName()))
            {
                if (!isKnownType(sourceName, attributeTypeDef.getGUID(), attributeTypeDef.getName()))
                {
                    knownAttributeTypeDefNames.put(attributeTypeDef.getName(), attributeTypeDef);
                }
            }
            else
            {
                throw new RepositoryErrorException(OMRSErrorCode.CONFLICTING_ENTERPRISE_TYPEDEFS.getMessageDefinition(),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }
    }


    /**
     * Return boolean indicating whether the TypeDef is one of the standard open metadata types.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeGUID unique identifier of the type
     * @param typeName unique name of the type
     * @return boolean result
     */
    boolean isOpenType(String sourceName, String typeGUID, String typeName)
    {
        if (validTypeId(sourceName, typeGUID, typeName))
        {
            TypeDef typeDef = knownTypeDefNames.get(typeName);

            if (typeDef == null)
            {
                return false;
            }

            if (openTypesOriginGUID != null)
            {
                if (openTypesOriginGUID.equals(typeDef.getOrigin()))
                {
                    log.debug("TypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName + " is an open type");
                    return true;
                }
                else
                {
                    log.debug("TypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName + " is NOT an open type");
                }
            }
        }

        return false;
    }


    /**
     * Return boolean indicating whether the TypeDef is one of the standard open metadata types.
     *
     * @param typeGUID unique identifier of the type
     * @return boolean result
     */
    boolean isOpenTypeId(String   typeGUID)
    {
        if (typeGUID != null)
        {
            TypeDef typeDef = knownTypeDefGUIDs.get(typeGUID);
            if (typeDef != null)
            {
                String originGUID = typeDef.getOrigin();

                return openTypesOriginGUID.equals(originGUID);
            }
        }

        return false;
    }


    /**
     * Return boolean indicating whether the (AttributeTypeDef/TypeDef is known, either as an open type, or one defined
     * by one or more of the members of the cohort.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeGUID unique identifier of the type
     * @param typeName unique name of the type
     * @return boolean result
     */
    boolean isKnownType(String sourceName, String typeGUID, String typeName)
    {
        if (this.validTypeId(sourceName, typeGUID, typeName))
        {
            TypeDef  typeDef = knownTypeDefNames.get(typeName);

            if (typeDef == null)
            {
                AttributeTypeDef  attributeTypeDef = knownAttributeTypeDefNames.get(typeName);

                if (attributeTypeDef == null)
                {
                    log.debug("Unknown (Attribute)TypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName);
                    return false;
                }
                else
                {
                    log.debug("Known AttributeTypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName);
                    return true;
                }
            }
            else
            {
                log.debug("Known TypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName);
                return true;
            }
        }
        else
        {
            log.debug("Invalid TypeDef  from " + sourceName + " so can not validate known type");
            return false;
        }
    }


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is known, either as an open type, or one defined
     * by one or more of the members of the cohort.
     *
     * @param typeGUID unique identifier of the type
     * @return boolean result
     */
    boolean isKnownTypeId(String   typeGUID)
    {
        if (typeGUID != null)
        {
            if (knownTypeDefGUIDs.get(typeGUID) != null)
            {
                return true;
            }

            return knownAttributeTypeDefGUIDs.get(typeGUID) != null;
        }

        return false;
    }


    /**
     * Return boolean indicating whether the TypeDef is in use in the repository.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeGUID unique identifier of the type
     * @param typeName unique name of the type
     * @return boolean result
     */
    boolean isActiveType(String sourceName, String typeGUID, String typeName)
    {
        if (this.validTypeId(sourceName, typeGUID, typeName))
        {
            TypeDef  typeDef = activeTypeDefNames.get(typeName);

            if (typeDef == null)
            {
                AttributeTypeDef  attributeTypeDef = activeAttributeTypeDefNames.get(typeName);

                if (attributeTypeDef == null)
                {
                    log.debug("Inactive (Attribute)TypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName);
                    return false;
                }
                else
                {
                    log.debug("Active AttributeTypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName);
                    return true;
                }
            }
            else
            {
                log.debug("Active TypeDef " + typeName + " (GUID = " + typeGUID + ") from " + sourceName);
                return true;
            }
        }
        else
        {
            log.debug("Invalid TypeDef  from " + sourceName + " so can not validate active type");
            return false;
        }
    }


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the local repository.
     *
     * @param typeGUID unique identifier of the type
     * @return boolean result
     */
    boolean isActiveTypeId(String   typeGUID)
    {
        if (typeGUID != null)
        {
            if (activeTypeDefGUIDs.get(typeGUID) != null)
            {
                return true;
            }

            return activeAttributeTypeDefGUIDs.get(typeGUID) != null;
        }

        return false;
    }


    /**
     * Return boolean indicating whether the (Attribute)TypeDef identifiers are valid or not.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeGUID unique identifier of the TypeDef
     * @param typeName unique name of the TypeDef
     * @return boolean result
     */
    boolean validTypeId(String sourceName,
                        String typeGUID,
                        String typeName)
    {
        if (typeName == null)
        {
            /*
             * A null TypeDef name is invalid
             */
            logNullTypeName(sourceName, typeGUID);
            return false;
        }

        if (typeGUID == null)
        {
            /*
             * A null guid is invalid
             */
            logNullTypeGUID(sourceName, typeName);
            return false;
        }

        TypeDef typeDef = knownTypeDefNames.get(typeName);

        if (typeDef != null)
        {
            if (! typeGUID.equals(typeDef.getGUID()))
            {
                logTypeGUIDMismatch(sourceName,
                                    typeGUID,
                                    typeName,
                                    typeDef.getGUID(),
                                    typeDef.getName());

                return false;
            }

            log.debug("Valid TypeDef of " + typeName + " from " + sourceName);
            return true;
        }
        else
        {
            /*
             * This TypeDef is unknown so see if it is an AttributeTypeDef
             */
            AttributeTypeDef   attributeTypeDef = knownAttributeTypeDefNames.get(typeName);

            if (attributeTypeDef == null)
            {
                return false;
            }
            else
            {
                if (!typeGUID.equals(attributeTypeDef.getGUID()))
                {
                    /*
                     * The requested guid does not equal the stored one.
                     */
                    logTypeGUIDMismatch(sourceName,
                                        typeGUID,
                                        typeName,
                                        attributeTypeDef.getGUID(),
                                        attributeTypeDef.getName());
                    return false;
                }
            }

            log.debug("Valid AttributeTypeDef " + typeName + " from " + sourceName);
            return true;
        }
    }


    /**
     * Log that a mismatching type has been supplied to the server - it is probably from a new instance
     * and suggests that two types with the same name have been defined in the cohort.  The conflict needs to be
     * resolved in order to replicate metadata of this type.
     *
     * @param sourceName source of the request
     * @param requestedTypeGUID  type GUID from the request
     * @param requestedTypeName  type name from the request
     */
    private void logUnknownType(String sourceName,
                                String requestedTypeGUID,
                                String requestedTypeName)
    {
        final String  actionDescription = "validate type identifier";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.UNKNOWN_TYPE.getMessageDefinition(sourceName, requestedTypeName, requestedTypeGUID));
    }


    /**
     * Log that a mismatching type has been supplied to the server - it is probably from a new instance
     * and suggests that two types with the same name have been defined in the cohort.  The conflict needs to be
     * resolved in order to replicate metadata of this type.
     *
     * @param sourceName source of the request
     * @param requestedTypeGUID  type GUID from the request
     */
    private void logNullTypeName(String sourceName,
                                 String requestedTypeGUID)
    {
        final String  actionDescription = "validate type identifier";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.NULL_TYPE_NAME.getMessageDefinition(sourceName, requestedTypeGUID));
    }


    /**
     * Log that a mismatching type has been supplied to the server - it is probably from a new instance
     * and suggests that two types with the same name have been defined in the cohort.  The conflict needs to be
     * resolved in order to replicate metadata of this type.
     *
     * @param sourceName source of the request
     * @param requestedTypeName  type name from the request
     */
    private void logNullTypeGUID(String sourceName,
                                 String requestedTypeName)
    {
        final String  actionDescription = "validate type identifier";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.NULL_TYPE_IDENTIFIER.getMessageDefinition(requestedTypeName, sourceName));
    }


    /**
     * Log that a mismatching type has been supplied to the server - it is probably from a new instance
     * and suggests that two types with the same name have been defined in the cohort.  The conflict needs to be
     * resolved in order to replicate metadata of this type.
     *
     * @param sourceName source of the request
     * @param requestedTypeGUID  type GUID from the request
     * @param requestedTypeName  type name from the request
     * @param localTypeGUID type guid from the local cache
     * @param localTypeName type name from the local cache
     */
    private void logTypeGUIDMismatch(String sourceName,
                                     String requestedTypeGUID,
                                     String requestedTypeName,
                                     String localTypeGUID,
                                     String localTypeName)
    {
        final String  actionDescription = "validate type identifier";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.TYPE_IDENTIFIER_MISMATCH.getMessageDefinition(localTypeName,
                                                                                        localTypeGUID,
                                                                                        requestedTypeName,
                                                                                        requestedTypeGUID,
                                                                                        sourceName));
    }


    /**
     * Log that an instance has a null type.
     *
     * @param sourceName source of the instance
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param category type category from the TypeDef
     */
    void logNullInstanceGUID(String          sourceName,
                             String          typeDefGUID,
                             String          typeDefName,
                             TypeDefCategory category)
    {
        final String  actionDescription = "validate type identifiers and category";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.NULL_INSTANCE_ID.getMessageDefinition(sourceName,
                                                                                typeDefName,
                                                                                typeDefGUID,
                                                                                category.getName()));
    }


    /**
     * Log that a request has passed a null instance.
     *
     * @param sourceName source of the instance
     * @param methodName calling method
     */
    void logNullInstance(String          sourceName,
                         String          methodName)
    {
        final String  actionDescription = "validate request parameters";

        auditLog.logMessage(actionDescription, OMRSAuditCode.NULL_INSTANCE.getMessageDefinition(methodName, sourceName));
    }


    /**
     * Return boolean indicating whether the TypeDef identifiers are valid or not.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param category category of TypeDef
     * @return boolean result
     */
    boolean validTypeDefId(String          sourceName,
                           String          typeDefGUID,
                           String          typeDefName,
                           TypeDefCategory category)
    {
        if (! validTypeId(sourceName, typeDefGUID, typeDefName))
        {
            /*
             * Errors already logged
             */
            return false;
        }

        if (category == null)
        {
            logNullTypeCategory(sourceName, typeDefGUID, typeDefName);
            return false;
        }

        TypeDef typeDef = knownTypeDefNames.get(typeDefName);

        if (typeDef != null)
        {
            TypeDefCategory knownTypeDefCategory = typeDef.getCategory();

            if (category.getOrdinal() != knownTypeDefCategory.getOrdinal())
            {
                logUnknownTypeCategory(sourceName, typeDefGUID, typeDefName, category.getName(), knownTypeDefCategory.getName());
                return false;
            }
        }

        return true;
    }


    /**
     * Log that a mismatching type has been supplied to the server - it is probably from a new instance
     * and suggests that two types with the same name have been defined in the cohort.  The conflict needs to be
     * resolved in order to replicate metadata of this type.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     */
    private void logNullTypeCategory(String          sourceName,
                                     String          typeDefGUID,
                                     String          typeDefName)
    {
        final String  actionDescription = "validate type identifiers and category";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.NULL_TYPE_CATEGORY.getMessageDefinition(sourceName, typeDefName, typeDefGUID));
    }


    /**
     * Log that a mismatching type has been supplied to the server - it is probably from a new instance
     * and suggests that two types with the same name have been defined in the cohort.  The conflict needs to be
     * resolved in order to replicate metadata of this type.
     *
     * @param sourceName source of the TypeDef
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param category category of TypeDef
     * @param localTypeCategory type category from the local cache
     */
    private void logUnknownTypeCategory(String sourceName,
                                        String typeDefGUID,
                                        String typeDefName,
                                        String category,
                                        String localTypeCategory)
    {
        final String  actionDescription = "validate type identifiers and category";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.UNKNOWN_TYPE_CATEGORY.getMessageDefinition(sourceName,
                                                                                     typeDefName,
                                                                                     typeDefGUID,
                                                                                     category,
                                                                                     localTypeCategory));
    }


    /**
     * Log that a mismatching type has been supplied to the server - it is probably from a new instance
     * and suggests that two types with the same name have been defined in the cohort.  The conflict needs to be
     * resolved in order to replicate metadata of this type.
     *
     * @param sourceName source of the TypeDef
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param category category of TypeDef
     * @param versionName version name from the request
     * @param localVersionName type version from the local cache
     */
    private void logVersionMismatch(String sourceName,
                                    String typeDefGUID,
                                    String typeDefName,
                                    String category,
                                    String versionName,
                                    String localVersionName)
    {
        final String  actionDescription = "validate type identifiers and category";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.TYPE_VERSION_MISMATCH.getMessageDefinition(sourceName,
                                                                                     typeDefName,
                                                                                     typeDefGUID,
                                                                                     category,
                                                                                     versionName,
                                                                                     localVersionName));
    }


    /**
     * Log that a request to process an instance includes a null metadata collection Id.
     *
     * @param sourceName source of the TypeDef
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param category category of TypeDef
     * @param instanceGUID unique identifier of the instance
     * @param methodName calling method
     */
    void logNullMetadataCollectionId(String sourceName,
                                     String typeDefGUID,
                                     String typeDefName,
                                     String category,
                                     String instanceGUID,
                                     String methodName)
    {
        final String  actionDescription = "validate type identifiers and category";

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.NULL_METADATA_COLLECTION_ID.getMessageDefinition(methodName,
                                                                                           sourceName,
                                                                                           instanceGUID,
                                                                                           typeDefName,
                                                                                           typeDefGUID,
                                                                                           category));
    }


    /**
     * Log that a null type has been supplied to the server.
     *
     * @param sourceName source of the Type Definition
     * @param methodName calling method
     */
    void logNullType(String sourceName,
                     String methodName)
    {
        final String  actionDescription = "validate type object";

        auditLog.logMessage(actionDescription, OMRSAuditCode.NULL_TYPE.getMessageDefinition(methodName, sourceName));
    }


    /**
     * Return boolean indicating whether the AttributeTypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request
     * @param attributeTypeDefGUID unique identifier of the AttributeTypeDef
     * @param attributeTypeDefName unique name of the AttributeTypeDef
     * @param category category for the AttributeTypeDef
     * @return boolean result
     */
    boolean validAttributeTypeDefId(String                   sourceName,
                                    String                   attributeTypeDefGUID,
                                    String                   attributeTypeDefName,
                                    AttributeTypeDefCategory category)
    {
        if (! validTypeId(sourceName, attributeTypeDefGUID, attributeTypeDefName))
        {
            return false;
        }

        if (category == null)
        {
            logNullTypeCategory(sourceName, attributeTypeDefGUID, attributeTypeDefName);
            return false;
        }

        AttributeTypeDef          attributeTypeDef = knownAttributeTypeDefNames.get(attributeTypeDefName);

        if (attributeTypeDef != null)
        {
            AttributeTypeDefCategory knownAttributeTypeDefCategory = attributeTypeDef.getCategory();

            if (category.getOrdinal() != knownAttributeTypeDefCategory.getOrdinal())
            {
                logUnknownTypeCategory(sourceName,
                                       attributeTypeDefGUID,
                                       attributeTypeDefName,
                                       category.getName(),
                                       knownAttributeTypeDefCategory.getName());

                return false;
            }
        }

        return true;
    }


    /**
     * Return boolean indicating whether the TypeDef identifiers are valid or not.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     * @param typeDefVersion version of the type
     * @param typeDefCategory category of the instance described by this TypeDef.
     * @return boolean result
     */
    boolean validTypeDefId(String          sourceName,
                           String          typeDefGUID,
                           String          typeDefName,
                           String          typeDefVersion,
                           TypeDefCategory typeDefCategory)
    {
        if (! validTypeDefId(sourceName, typeDefGUID, typeDefName, typeDefCategory))
        {
            return false;
        }

        TypeDef   typeDef = knownTypeDefNames.get(typeDefName);

        if (typeDef == null)
        {
            log.debug("Unknown TypeDef " + typeDefName + " (GUID = " + typeDefGUID + ") from " + sourceName);

            return true;
        }

        if (!typeDef.getVersionName().equals(typeDefVersion))
        {
            logVersionMismatch(sourceName,
                               typeDefGUID,
                               typeDefName,
                               typeDefCategory.getName(),
                               typeDefVersion,
                               typeDef.getVersionName());
            return false;
        }

        return true;
    }


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier of the TypeDef
     * @param attributeTypeDefName unique name of the TypeDef
     * @param attributeTypeDefVersion version of the type
     * @param category category for the TypeDef
     * @return boolean result
     */
    boolean validAttributeTypeDefId(String                   sourceName,
                                    String                   attributeTypeDefGUID,
                                    String                   attributeTypeDefName,
                                    String                   attributeTypeDefVersion,
                                    AttributeTypeDefCategory category)
    {
        if (! validAttributeTypeDefId(sourceName, attributeTypeDefGUID, attributeTypeDefName, category))
        {
            return false;
        }

        AttributeTypeDef   attributeTypeDef = knownAttributeTypeDefNames.get(attributeTypeDefName);

        if (attributeTypeDef == null)
        {
            log.debug("Unknown TypeDef " + attributeTypeDefName + " (GUID = " + attributeTypeDefGUID + ") from " + sourceName);

            return true;

        }
        if (!attributeTypeDef.getVersionName().equals(attributeTypeDefVersion))
        {
            logVersionMismatch(sourceName,
                               attributeTypeDefGUID,
                               attributeTypeDefName,
                               category.getName(),
                               attributeTypeDefVersion,
                               attributeTypeDef.getVersionName());
            return false;
        }

        return true;
    }


    /**
     * Return boolean indicating whether the supplied TypeDef is valid or not.
     *
     * @param sourceName source of the TypeDef (used for logging)
     * @param typeDef TypeDef to test
     * @return boolean result
     */
    boolean validTypeDef(String         sourceName,
                         TypeDef        typeDef)
    {
        final String methodName = "validTypeDef";

        if (typeDef == null)
        {
            logNullType(sourceName, methodName);
            return false;
        }

        return validTypeDefId(sourceName,
                              typeDef.getGUID(),
                              typeDef.getName(),
                              typeDef.getVersionName(),
                              typeDef.getCategory());
    }


    /**
     * Return boolean indicating whether the supplied AttributeTypeDef is valid or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDef TypeDef to test
     * @return boolean result
     */
    boolean validAttributeTypeDef(String           sourceName,
                                  AttributeTypeDef attributeTypeDef)
    {
        final String methodName = "validAttributeTypeDef";

        if (attributeTypeDef == null)
        {
            logNullType(sourceName, methodName);
            return false;
        }

        return validAttributeTypeDefId(sourceName,
                                       attributeTypeDef.getGUID(),
                                       attributeTypeDef.getName(),
                                       attributeTypeDef.getCategory());

    }


    /**
     * Return boolean indicating whether the supplied TypeDefSummary is valid or not.
     *
     * @param sourceName source of the TypeDefSummary (used for logging)
     * @param typeDefSummary TypeDefSummary to test.
     * @return boolean result.
     */
    boolean validTypeDefSummary(String                sourceName,
                                TypeDefSummary        typeDefSummary)
    {
        final String methodName = "validTypeDefSummary";

        if (typeDefSummary == null)
        {
            logNullType(sourceName, methodName);
            return false;
        }

        return validTypeDefId(sourceName,
                              typeDefSummary.getGUID(),
                              typeDefSummary.getName(),
                              typeDefSummary.getVersionName(),
                              typeDefSummary.getCategory());
    }


    /*
     * ===========================
     * OMRSTypeDefEventProcessor
     */


    /**
     * Process incoming TypeDefEvent based on its type.
     *
     * @param cohortName source of the event (cohort name)
     * @param typeDefEvent event to process
     */
    @Override
    public void sendTypeDefEvent(String           cohortName,
                                 OMRSTypeDefEvent typeDefEvent)
    {
        OMRSTypeDefEventType typeDefEventType       = typeDefEvent.getTypeDefEventType();
        OMRSEventOriginator  typeDefEventOriginator = typeDefEvent.getEventOriginator();

        if ((typeDefEventType != null) && (typeDefEventOriginator != null))
        {
            switch (typeDefEventType)
            {
                case NEW_TYPEDEF_EVENT:
                    this.processNewTypeDefEvent(cohortName,
                                                typeDefEventOriginator.getMetadataCollectionId(),
                                                typeDefEventOriginator.getServerName(),
                                                typeDefEventOriginator.getServerType(),
                                                typeDefEventOriginator.getOrganizationName(),
                                                typeDefEvent.getTypeDef());
                    break;

                case NEW_ATTRIBUTE_TYPEDEF_EVENT:
                    this.processNewAttributeTypeDefEvent(cohortName,
                                                         typeDefEventOriginator.getMetadataCollectionId(),
                                                         typeDefEventOriginator.getServerName(),
                                                         typeDefEventOriginator.getServerType(),
                                                         typeDefEventOriginator.getOrganizationName(),
                                                         typeDefEvent.getAttributeTypeDef());
                    break;

                case UPDATED_TYPEDEF_EVENT:
                    this.processUpdatedTypeDefEvent(cohortName,
                                                    typeDefEventOriginator.getMetadataCollectionId(),
                                                    typeDefEventOriginator.getServerName(),
                                                    typeDefEventOriginator.getServerType(),
                                                    typeDefEventOriginator.getOrganizationName(),
                                                    typeDefEvent.getTypeDefPatch());
                    break;

                case DELETED_TYPEDEF_EVENT:
                    this.processDeletedTypeDefEvent(cohortName,
                                                    typeDefEventOriginator.getMetadataCollectionId(),
                                                    typeDefEventOriginator.getServerName(),
                                                    typeDefEventOriginator.getServerType(),
                                                    typeDefEventOriginator.getOrganizationName(),
                                                    typeDefEvent.getTypeDefGUID(),
                                                    typeDefEvent.getTypeDefName());
                    break;

                case DELETED_ATTRIBUTE_TYPEDEF_EVENT:
                    this.processDeletedAttributeTypeDefEvent(cohortName,
                                                             typeDefEventOriginator.getMetadataCollectionId(),
                                                             typeDefEventOriginator.getServerName(),
                                                             typeDefEventOriginator.getServerType(),
                                                             typeDefEventOriginator.getOrganizationName(),
                                                             typeDefEvent.getTypeDefGUID(),
                                                             typeDefEvent.getTypeDefName());
                    break;

                case RE_IDENTIFIED_TYPEDEF_EVENT:
                    this.processReIdentifiedTypeDefEvent(cohortName,
                                                         typeDefEventOriginator.getMetadataCollectionId(),
                                                         typeDefEventOriginator.getServerName(),
                                                         typeDefEventOriginator.getServerType(),
                                                         typeDefEventOriginator.getOrganizationName(),
                                                         typeDefEvent.getOriginalTypeDefSummary(),
                                                         typeDefEvent.getTypeDef());
                    break;

                case RE_IDENTIFIED_ATTRIBUTE_TYPEDEF_EVENT:
                    this.processReIdentifiedAttributeTypeDefEvent(cohortName,
                                                                  typeDefEventOriginator.getMetadataCollectionId(),
                                                                  typeDefEventOriginator.getServerName(),
                                                                  typeDefEventOriginator.getServerType(),
                                                                  typeDefEventOriginator.getOrganizationName(),
                                                                  typeDefEvent.getOriginalAttributeTypeDef(),
                                                                  typeDefEvent.getAttributeTypeDef());
                    break;

                case TYPEDEF_ERROR_EVENT:
                    OMRSTypeDefEventErrorCode errorCode = typeDefEvent.getErrorCode();

                    if (errorCode != null)
                    {
                        switch(errorCode)
                        {
                            case CONFLICTING_TYPEDEFS:
                                this.processTypeDefConflictEvent(cohortName,
                                                                 typeDefEventOriginator.getMetadataCollectionId(),
                                                                 typeDefEventOriginator.getServerName(),
                                                                 typeDefEventOriginator.getServerType(),
                                                                 typeDefEventOriginator.getOrganizationName(),
                                                                 typeDefEvent.getOriginalTypeDefSummary(),
                                                                 typeDefEvent.getOtherMetadataCollectionId(),
                                                                 typeDefEvent.getOtherTypeDefSummary(),
                                                                 typeDefEvent.getErrorMessage());
                                break;

                            case CONFLICTING_ATTRIBUTE_TYPEDEFS:
                                this.processAttributeTypeDefConflictEvent(cohortName,
                                                                          typeDefEventOriginator.getMetadataCollectionId(),
                                                                          typeDefEventOriginator.getServerName(),
                                                                          typeDefEventOriginator.getServerType(),
                                                                          typeDefEventOriginator.getOrganizationName(),
                                                                          typeDefEvent.getOriginalAttributeTypeDef(),
                                                                          typeDefEvent.getOtherMetadataCollectionId(),
                                                                          typeDefEvent.getOtherAttributeTypeDef(),
                                                                          typeDefEvent.getErrorMessage());
                                break;

                            case TYPEDEF_PATCH_MISMATCH:
                                this.processTypeDefPatchMismatchEvent(cohortName,
                                                                      typeDefEventOriginator.getMetadataCollectionId(),
                                                                      typeDefEventOriginator.getServerName(),
                                                                      typeDefEventOriginator.getServerType(),
                                                                      typeDefEventOriginator.getOrganizationName(),
                                                                      typeDefEvent.getTargetMetadataCollectionId(),
                                                                      typeDefEvent.getTargetTypeDefSummary(),
                                                                      typeDefEvent.getOtherTypeDef(),
                                                                      typeDefEvent.getErrorMessage());
                                break;

                            default:
                                log.debug("Unknown TypeDef event error code; ignoring event");
                                break;
                        }
                    }
                    else
                    {
                        log.debug("Ignored TypeDef event; null error code");
                    }
                    break;

                default:
                    log.debug("Ignored TypeDef event; unknown type");
                    break;
            }
        }
    }


    /**
     * Log an architected exception from processing new type information.
     *
     * @param error exception
     * @param typeName name of type being processed
     * @param actionDescription activity in progress when exception occurred
     * @param sourceName source of the event
     * @param originatorMetadataCollectionId originator's metadata collection Id
     * @param originatorServerName originator's server name
     * @param additionalInformation information about the object being processed
     */
    private void logTypeProcessingException(OMRSCheckedExceptionBase   error,
                                            String                     typeName,
                                            String                     actionDescription,
                                            String                     sourceName,
                                            String                     originatorMetadataCollectionId,
                                            String                     originatorServerName,
                                            String                     additionalInformation)
    {
        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_TYPE_PROCESSING.getMessageDefinition(error.getClass().getName(),
                                                                                                         typeName,
                                                                                                         sourceName,
                                                                                                         originatorServerName,
                                                                                                         originatorMetadataCollectionId,
                                                                                                         error.getReportedErrorMessage()),
                            additionalInformation);
    }


    /**
     * Log an unexpected error from the typeDefProcessor.  This should not occur.
     *
     * @param error exception
     * @param actionDescription activity in progress when this occurred
     * @param sourceName source of the event
     * @param additionalInformation object from event being processed
     */
    private void logUnexpectedException(Exception   error,
                                        String      actionDescription,
                                        String      sourceName,
                                        String      additionalInformation)
    {
        auditLog.logException(actionDescription,
                              OMRSAuditCode.UNHANDLED_EXCEPTION_FROM_TYPE_PROCESSING.getMessageDefinition(sourceName,
                                                                                                          error.getClass().getName(),
                                                                                                          error.getMessage()),
                              additionalInformation,
                              error);
    }


    /**
     * A new TypeDef has been defined either in an archive, or in another member of the cohort.
     *
     * This new TypeDef can be added to the repository if it does not clash with an existing typeDef and the local
     * repository supports dynamic type definitions.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDef details of the new TypeDef
     */
    @Override
    public void processNewTypeDefEvent(String       sourceName,
                                       String       originatorMetadataCollectionId,
                                       String       originatorServerName,
                                       String       originatorServerType,
                                       String       originatorOrganizationName,
                                       TypeDef      typeDef)
    {
        final String   actionDescription = "Process New TypeDef Event";

        OMRSMetadataCollection metadataCollection = null;

        try
        {
            if (localRepositoryConnector != null)
            {
                metadataCollection = localRepositoryConnector.getMetadataCollection();
            }

            if (metadataCollection != null)
            {
                TypeDef currentTypeDef = activeTypeDefNames.get(typeDef.getName());

                /*
                 * If we have already processed this type then the incoming event is an obsolete one.
                 */
                if (currentTypeDef == null)
                {
                    /*
                     * VerifyTypeDef returns true if the typeDef is known and matches the supplied definition.
                     * It returns false if the type is supportable but has not yet been defined.
                     * It throws TypeDefNotSupportedException if the typeDef is not supported and can not
                     * be dynamically defined by the local repository.
                     */
                    if (!metadataCollection.verifyTypeDef(localServerUserId, typeDef))
                    {
                        metadataCollection.addTypeDef(sourceName, typeDef);

                        auditLog.logMessage(actionDescription,
                                            OMRSAuditCode.NEW_TYPE_ADDED.getMessageDefinition(typeDef.getName(),
                                                                                              typeDef.getGUID(),
                                                                                              Long.toString(typeDef.getVersion()),
                                                                                              sourceName));
                    }

                    /*
                     * Either the repository already supports the type, or it has just added it.
                     * Cache information about the type in the repository content manager's maps.
                     */
                    this.cacheTypeDef(sourceName, typeDef, true);
                }
            }
            else
            {
                /*
                 * No local repository so just cache for enterprise repository services.
                 */
                this.cacheTypeDef(sourceName, typeDef, false);
            }
        }
        catch (TypeDefNotSupportedException  fixedTypeSystemResponse)
        {
            /*
             * Adds information about the type to the repository content manager for
             * use by the enterprise repository services (but not local repository).
             */
            this.cacheTypeDef(sourceName, typeDef, false);

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.NEW_TYPE_NOT_SUPPORTED.getMessageDefinition(typeDef.getName(),
                                                                                          typeDef.getGUID(),
                                                                                          Long.toString(typeDef.getVersion())));

            log.debug("TypeDef not added because repository does not support dynamic type definitions: " + typeDef);
            log.debug("TypeDefNotSupportedException:", fixedTypeSystemResponse);
        }
        catch (RepositoryErrorException error)
        {
            /*
             * Adds information about the type to the repository content manager for
             * use by the enterprise repository services (but not local repository).
             */
            this.cacheTypeDef(sourceName, typeDef, false);

            logTypeProcessingException(error,
                                       typeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDef.toString());

            log.debug("TypeDef " + typeDef.getName() + " not added because repository is not available: " + typeDef);
            log.debug("RepositoryErrorException:", error);
        }
        catch (UserNotAuthorizedException error)
        {
            logTypeProcessingException(error,
                                       typeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDef.toString());

            log.debug("TypeDef " + typeDef.getName() + " not added because repository is not authorized: " + typeDef);
            log.debug("RepositoryErrorException:", error);
        }
        catch (TypeDefConflictException error)
        {
            log.debug("TypeDef not added because it conflicts with another TypeDef already in the repository: " + typeDef);
            log.debug("TypeDefConflictException:", error);

            logTypeProcessingException(error,
                                       typeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDef.toString());

            outboundRepositoryEventManager.processTypeDefConflictEvent(sourceName,
                                                                       localRepositoryConnector.getMetadataCollectionId(),
                                                                       localRepositoryConnector.getLocalServerName(),
                                                                       localRepositoryConnector.getLocalServerType(),
                                                                       localRepositoryConnector.getOrganizationName(),
                                                                       typeDef,
                                                                       originatorMetadataCollectionId,
                                                                       knownTypeDefNames.get(typeDef.getName()),
                                                                       error.getReportedErrorMessage());
        }
        catch (InvalidTypeDefException error)
        {
            logTypeProcessingException(error,
                                       typeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDef.toString());

            log.debug("TypeDef not added because repository thinks it is invalid: " + typeDef);
            log.debug("InvalidTypeDefException: ", error);
        }
        catch (TypeDefKnownException error)
        {
            logTypeProcessingException(error,
                                       typeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDef.toString());

            log.debug("TypeDef not added because repository has a logic error: " + typeDef);
            log.debug("TypeDefKnownException: ", error);
        }
        catch (Exception  error)
        {
            logUnexpectedException(error,
                                   actionDescription,
                                   sourceName,
                                   typeDef.toString());

            log.debug("TypeDef not added because repository has an unexpected error: " + typeDef);
            log.debug("Exception: ", error);
        }
    }


    /**
     * A new AttributeTypeDef has been defined in an open metadata repository.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param attributeTypeDef details of the new AttributeTypeDef.
     */
    @Override
    public void processNewAttributeTypeDefEvent(String           sourceName,
                                                String           originatorMetadataCollectionId,
                                                String           originatorServerName,
                                                String           originatorServerType,
                                                String           originatorOrganizationName,
                                                AttributeTypeDef attributeTypeDef)
    {
        final String   actionDescription = "Process New AttributeTypeDef Event";

        OMRSMetadataCollection metadataCollection = null;

        try
        {
            if (localRepositoryConnector != null)
            {
                metadataCollection = localRepositoryConnector.getMetadataCollection();
            }

            if (metadataCollection != null)
            {
                /*
                 * VerifyTypeDef returns true if the typeDef is known and matches the supplied definition.
                 * It returns false if the type is supportable but has not yet been defined.
                 * It throws TypeDefNotSupportedException if the typeDef is not supported and can not
                 * be dynamically defined by the local repository.
                 */
                if (!metadataCollection.verifyAttributeTypeDef(localServerUserId, attributeTypeDef))
                {
                    metadataCollection.addAttributeTypeDef(localServerUserId, attributeTypeDef);

                    /*
                     * Update the active TypeDefs as this new TypeDef has been accepted by the local repository.
                     */
                    this.cacheAttributeTypeDef(sourceName, attributeTypeDef, true);

                    activeAttributeTypeDefNames.put(attributeTypeDef.getName(), attributeTypeDef);

                    auditLog.logMessage(actionDescription,
                                        OMRSAuditCode.NEW_TYPE_ADDED.getMessageDefinition(attributeTypeDef.getName(),
                                                                                          attributeTypeDef.getGUID(),
                                                                                          Long.toString(attributeTypeDef.getVersion()),
                                                                                          sourceName));
                }

                /*
                 * Either the repository already supports the type, or it has just added it.
                 * Cache information about the type in the repository content manager's maps.
                 */
                this.cacheAttributeTypeDef(sourceName, attributeTypeDef, true);
            }
            else
            {
                /*
                 * No local repository so just cache for enterprise repository services.
                 */
                this.cacheAttributeTypeDef(sourceName, attributeTypeDef, false);
            }
        }
        catch (TypeDefNotSupportedException fixedTypeSystemResponse)
        {
            /*
             * Adds information about the type to the repository content manager for
             * use by the enterprise repository services (but not local repository).
             */
            this.cacheAttributeTypeDef(sourceName, attributeTypeDef, false);

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.NEW_TYPE_NOT_SUPPORTED.getMessageDefinition(attributeTypeDef.getName(),
                                                                                          attributeTypeDef.getGUID(),
                                                                                          Long.toString(attributeTypeDef.getVersion())),
                                attributeTypeDef.toString());

            log.debug("TypeDef not added because repository does not support dynamic type definitions: " + attributeTypeDef);
            log.debug("TypeDefNotSupportedException:", fixedTypeSystemResponse);
        }
        catch (RepositoryErrorException error)
        {
            /*
             * Adds information about the type to the repository content manager for
             * use by the enterprise repository services (but not local repository).
             */
            this.cacheAttributeTypeDef(sourceName, attributeTypeDef, false);

            logTypeProcessingException(error,
                                       attributeTypeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDef.toString());

            log.debug("TypeDef " + attributeTypeDef.getName() + " not added because repository is not available: " + attributeTypeDef);
            log.debug("RepositoryErrorException:", error);
        }
        catch (UserNotAuthorizedException error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDef.toString());

            log.debug("TypeDef " + attributeTypeDef.getName() + " not added because repository is not authorized: " + attributeTypeDef);
            log.debug("RepositoryErrorException:", error);
        }
        catch (TypeDefConflictException error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDef.toString());

            log.debug("TypeDef not added because it conflicts with another TypeDef already in the repository: " + attributeTypeDef);
            log.debug("TypeDefConflictException:", error);

            outboundRepositoryEventManager.processAttributeTypeDefConflictEvent(sourceName,
                                                                                localRepositoryConnector.getMetadataCollectionId(),
                                                                                localRepositoryConnector.getLocalServerName(),
                                                                                localRepositoryConnector.getLocalServerType(),
                                                                                localRepositoryConnector.getOrganizationName(),
                                                                                attributeTypeDef,
                                                                                originatorMetadataCollectionId,
                                                                                knownAttributeTypeDefNames.get(
                                                                                        attributeTypeDef.getName()),
                                                                                null);
        }
        catch (InvalidTypeDefException error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDef.toString());

            log.debug("TypeDef not added because repository thinks it is invalid: " + attributeTypeDef);
            log.debug("InvalidTypeDefException: ", error);
        }
        catch (TypeDefKnownException error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDef.getName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDef.toString());

            log.debug("TypeDef not added because repository has a logic error: " + attributeTypeDef);
            log.debug("TypeDefKnownException: ", error);
        }
        catch (Exception  error)
        {
            logUnexpectedException(error,
                                   actionDescription,
                                   sourceName,
                                   attributeTypeDef.toString());

            log.debug("TypeDef not added because repository has an unexpected error: " + attributeTypeDef);
            log.debug("Exception: ", error);
        }
    }


    /**
     * An existing TypeDef has been updated in a remote metadata repository.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefPatch details of the new version of the TypeDef
     */
    @Override
    public void processUpdatedTypeDefEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           TypeDefPatch typeDefPatch)
    {
        final String methodName = "processUpdatedTypeDefEvent";
        final String actionDescription = "Process TypeDef Patch Event";

        OMRSMetadataCollection metadataCollection = null;

        try
        {
            if (localRepositoryConnector != null)
            {
                metadataCollection = localRepositoryConnector.getMetadataCollection();
            }

            if (metadataCollection != null)
            {
                TypeDef currentTypeDef = activeTypeDefNames.get(typeDefPatch.getTypeDefName());

                if (currentTypeDef != null)
                {
                    /*
                     * The local repository supports the type
                     */
                    if (currentTypeDef.getVersion() == typeDefPatch.getApplyToVersion())
                    {
                        /*
                         * The patch is relevant to the current level of the type
                         */
                        TypeDef updatedTypeDef = metadataCollection.updateTypeDef(localServerUserId, typeDefPatch);

                        log.debug("Patch successfully applied:" + updatedTypeDef);

                        auditLog.logMessage(actionDescription,
                                            OMRSAuditCode.TYPE_UPDATED.getMessageDefinition(updatedTypeDef.getName(),
                                                                                            updatedTypeDef.getGUID(),
                                                                                            Long.toString(updatedTypeDef.getVersion()),
                                                                                            sourceName));

                        this.cacheTypeDef(sourceName, updatedTypeDef, true);
                    }
                }
            }
            else
            {
                TypeDef originalTypeDef = getTypeDefByName(typeDefPatch.getTypeDefName());

                OMRSRepositoryPropertiesUtilities utilities = new OMRSRepositoryPropertiesUtilities();

                TypeDef updatedTypeDef = utilities.applyPatch(sourceName,
                                                              originalTypeDef,
                                                              typeDefPatch,
                                                              methodName);

                this.cacheTypeDef(sourceName, updatedTypeDef, false);
            }
        }
        catch (FunctionNotSupportedException  error)
        {
            logTypeProcessingException(error,
                                       typeDefPatch.getTypeDefName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefPatch.toString());

            if (log.isDebugEnabled())
            {
                log.debug("Patch not applied because repository does not support patching: " + typeDefPatch);
            }
        }
        catch (RepositoryErrorException  error)
        {
            logTypeProcessingException(error,
                                       typeDefPatch.getTypeDefName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefPatch.toString());

            if (log.isDebugEnabled())
            {
                log.debug("Patch not applied because repository is not available: " + typeDefPatch);
            }
        }
        catch (UserNotAuthorizedException  error)
        {
            logTypeProcessingException(error,
                                       typeDefPatch.getTypeDefName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefPatch.toString());

            if (log.isDebugEnabled())
            {
                log.debug("Patch not applied because repository is not authorized to make this change: " + typeDefPatch);
            }
        }
        catch (TypeDefNotKnownException  error)
        {
            logTypeProcessingException(error,
                                       typeDefPatch.getTypeDefName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefPatch.toString());

            if (log.isDebugEnabled())
            {
                log.debug("Patch not applied because TypeDef does not exist: " + typeDefPatch);
                log.debug("TypeDefNotKnownException: ", error);
            }
        }
        catch (PatchErrorException error)
        {
            logTypeProcessingException(error,
                                       typeDefPatch.getTypeDefName(),
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefPatch.toString());

            if (log.isDebugEnabled())
            {
                log.debug("Patch not applied because it is invalid: " + typeDefPatch);
                log.debug("PatchErrorException: ", error);
            }
        }
        catch (Exception error)
        {
            logUnexpectedException(error, actionDescription, sourceName, typeDefPatch.toString());

            if (log.isDebugEnabled())
            {
                log.debug("Patch not applied because of an error " + typeDefPatch);
                log.debug("Exception: ", error);
            }
        }
    }


    /**
     * An existing TypeDef has been deleted in a remote metadata repository.  Both the name and the
     * GUID are provided to ensure the right TypeDef is deleted in other cohort member repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     */
    @Override
    public void processDeletedTypeDefEvent(String      sourceName,
                                           String      originatorMetadataCollectionId,
                                           String      originatorServerName,
                                           String      originatorServerType,
                                           String      originatorOrganizationName,
                                           String      typeDefGUID,
                                           String      typeDefName)
    {
        final String actionDescription = "Process TypeDef Delete Event";

        OMRSMetadataCollection metadataCollection = null;

        try
        {
            if (localRepositoryConnector != null)
            {
                metadataCollection = localRepositoryConnector.getMetadataCollection();
            }

            if (metadataCollection != null)
            {
                metadataCollection.deleteTypeDef(localServerUserId, typeDefGUID, typeDefName);

                log.debug("type def successfully deleted: " + typeDefGUID);

                this.uncacheTypeDef(sourceName, typeDefGUID, typeDefName, true);
            }
            else
            {
                this.uncacheTypeDef(sourceName, typeDefGUID, typeDefName, false);

            }
        }
        catch (UserNotAuthorizedException  error)
        {
            logTypeProcessingException(error,
                                       typeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefName + " (" + typeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("TypeDef not deleted because repository is not authorized to make these changes: " + typeDefName);
            }
        }
        catch (FunctionNotSupportedException  error)
        {
            logTypeProcessingException(error,
                                       typeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefName + " (" + typeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("TypeDef not deleted because repository does not support it: " + typeDefName);
            }
        }
        catch (RepositoryErrorException  error)
        {
            logTypeProcessingException(error,
                                       typeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefName + " (" + typeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("TypeDef not deleted because repository is not available: " + typeDefName);
            }
        }
        catch (TypeDefNotKnownException  error)
        {
            logTypeProcessingException(error,
                                       typeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       typeDefName + " (" + typeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("Delete not applied because TypeDef does not exist: " + typeDefName);
                log.debug("TypeDefNotKnownException: ", error);
            }
        }
        catch (Exception error)
        {
            logUnexpectedException(error, actionDescription, sourceName, typeDefName + " (" + typeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("Type not deleted because of an error " + typeDefName);
                log.debug("Exception: ", error);
            }
        }
    }


    /**
     * An existing AttributeTypeDef has been deleted in an open metadata repository.  Both the name and the
     * GUID are provided to ensure the right AttributeTypeDef is deleted in other cohort member repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param attributeTypeDefGUID unique identifier of the AttributeTypeDef
     * @param attributeTypeDefName unique name of the AttributeTypeDef
     */
    @Override
    public void processDeletedAttributeTypeDefEvent(String      sourceName,
                                                    String      originatorMetadataCollectionId,
                                                    String      originatorServerName,
                                                    String      originatorServerType,
                                                    String      originatorOrganizationName,
                                                    String      attributeTypeDefGUID,
                                                    String      attributeTypeDefName)
    {
        final String actionDescription = "Process AttributeTypeDef Delete Event";

        OMRSMetadataCollection metadataCollection = null;

        try
        {
            if (localRepositoryConnector != null)
            {
                metadataCollection = localRepositoryConnector.getMetadataCollection();
            }

            if (metadataCollection != null)
            {
                metadataCollection.deleteTypeDef(localServerUserId, attributeTypeDefGUID, attributeTypeDefName);

                log.debug("type def successfully deleted: " + attributeTypeDefGUID);

                this.uncacheTypeDef(sourceName, attributeTypeDefGUID, attributeTypeDefName, true);
            }
            else
            {
                this.uncacheTypeDef(sourceName, attributeTypeDefGUID, attributeTypeDefName, false);

            }
        }
        catch (UserNotAuthorizedException  error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDefName + " (" + attributeTypeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("AttributeTypeDef not deleted because repository is not authorized to make these changes: " + attributeTypeDefName);
            }
        }
        catch (FunctionNotSupportedException  error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDefName + " (" + attributeTypeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("AttributeTypeDef not deleted because repository does not support delete: " + attributeTypeDefName);
            }
        }
        catch (RepositoryErrorException  error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDefName + " (" + attributeTypeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("AttributeTypeDef not deleted because repository is not available: " + attributeTypeDefName);
            }
        }
        catch (TypeDefNotKnownException  error)
        {
            logTypeProcessingException(error,
                                       attributeTypeDefName,
                                       actionDescription,
                                       sourceName,
                                       originatorMetadataCollectionId,
                                       originatorServerName,
                                       attributeTypeDefName + " (" + attributeTypeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("Delete not applied because TypeDef does not exist: " + attributeTypeDefName);
                log.debug("TypeDefNotKnownException: ", error);
            }
        }
        catch (Exception error)
        {
            logUnexpectedException(error, actionDescription, sourceName, attributeTypeDefName + " (" + attributeTypeDefGUID + ")");

            if (log.isDebugEnabled())
            {
                log.debug("Type not deleted because of an error " + attributeTypeDefName);
                log.debug("Exception: ", error);
            }
        }
    }


    /**
     * Process an event that changes either the name or guid of a TypeDef.  It is resolving a Conflicting TypeDef Error.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary details of the original TypeDef
     * @param typeDef updated TypeDef with new identifiers inside.
     */
    @Override
    public void processReIdentifiedTypeDefEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                TypeDef        typeDef)
    {
        // Todo
    }


    /**
     * Process an event that changes either the name or guid of an AttributeTypeDef.
     * It is resolving a Conflicting AttributeTypeDef Error.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalAttributeTypeDef description of original AttributeTypeDef
     * @param attributeTypeDef updated AttributeTypeDef with new identifiers inside.
     */
    @Override
    public void processReIdentifiedAttributeTypeDefEvent(String           sourceName,
                                                         String           originatorMetadataCollectionId,
                                                         String           originatorServerName,
                                                         String           originatorServerType,
                                                         String           originatorOrganizationName,
                                                         AttributeTypeDef originalAttributeTypeDef,
                                                         AttributeTypeDef attributeTypeDef)
    {
        // TODO
    }


    /**
     * Process a detected conflict in type definitions (TypeDefs) used in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originatorTypeDefSummary details of the TypeDef in the event originator
     * @param otherMetadataCollectionId the metadataCollection using the conflicting TypeDef
     * @param conflictingTypeDefSummary the details of the TypeDef in the other metadata collection
     * @param errorMessage details of the error that occurs when the connection is used.
     */
    @Override
    public void processTypeDefConflictEvent(String         sourceName,
                                            String         originatorMetadataCollectionId,
                                            String         originatorServerName,
                                            String         originatorServerType,
                                            String         originatorOrganizationName,
                                            TypeDefSummary originatorTypeDefSummary,
                                            String         otherMetadataCollectionId,
                                            TypeDefSummary conflictingTypeDefSummary,
                                            String         errorMessage)
    {
        final String actionDescription = "Received TypeDef Conflict Event";

        String additionalInformation = "Originator's type: ";
        String typeName = "<null>";
        String typeGUID = "<null>";

        if (originatorTypeDefSummary != null)
        {
            additionalInformation += originatorTypeDefSummary.toString();
            typeName = originatorTypeDefSummary.getName();
            typeGUID = originatorTypeDefSummary.getGUID();
        }
        else
        {
            additionalInformation += "<null>";
        }

        additionalInformation += "; conflicting type: ";

        if (conflictingTypeDefSummary != null)
        {
            additionalInformation += conflictingTypeDefSummary.toString();
        }
        else
        {
            additionalInformation += "<null>";
        }

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.REMOTE_TYPE_CONFLICT.getMessageDefinition(typeName,
                                                                                    typeGUID,
                                                                                    originatorServerName,
                                                                                    originatorMetadataCollectionId,
                                                                                    otherMetadataCollectionId,
                                                                                    errorMessage),
                            additionalInformation);
    }


    /**
     * Process a detected conflict in the attribute type definitions (AttributeTypeDefs) used in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originatorAttributeTypeDef- description of the AttributeTypeDef in the event originator.
     * @param otherMetadataCollectionId the metadataCollection using the conflicting AttributeTypeDef.
     * @param conflictingAttributeTypeDef description of the AttributeTypeDef in the other metadata collection.
     * @param errorMessage details of the error that occurs when the connection is used.
     */
    @Override
    public void processAttributeTypeDefConflictEvent(String           sourceName,
                                                     String           originatorMetadataCollectionId,
                                                     String           originatorServerName,
                                                     String           originatorServerType,
                                                     String           originatorOrganizationName,
                                                     AttributeTypeDef originatorAttributeTypeDef,
                                                     String           otherMetadataCollectionId,
                                                     AttributeTypeDef conflictingAttributeTypeDef,
                                                     String           errorMessage)
    {
        final String actionDescription = "Received TypeDef Conflict Event";

        String additionalInformation = "Originator's type: ";
        String typeName = "<null>";
        String typeGUID = "<null>";

        if (originatorAttributeTypeDef != null)
        {
            additionalInformation += originatorAttributeTypeDef.toString();
            typeName = originatorAttributeTypeDef.getName();
            typeGUID = originatorAttributeTypeDef.getGUID();
        }
        else
        {
            additionalInformation += "<null>";
        }

        additionalInformation += "; conflicting type: ";

        if (conflictingAttributeTypeDef != null)
        {
            additionalInformation += conflictingAttributeTypeDef.toString();
        }
        else
        {
            additionalInformation += "<null>";
        }

        auditLog.logMessage(actionDescription,
                            OMRSAuditCode.REMOTE_TYPE_CONFLICT.getMessageDefinition(typeName,
                                                                                    typeGUID,
                                                                                    originatorServerName,
                                                                                    originatorMetadataCollectionId,
                                                                                    otherMetadataCollectionId,
                                                                                    errorMessage),
                            additionalInformation);
    }


    /**
     * A TypeDef from another member in the cohort is at a different version than the local repository.  This may
     * create some inconsistencies in the different copies of instances of this type in different members of the
     * cohort.  The recommended action is to update all TypeDefs to the latest version.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId identifier of the metadata collection that is reporting a TypeDef at a
     *                                   different level to the local repository.
     * @param targetTypeDefSummary details of the target TypeDef
     * @param otherTypeDef details of the TypeDef in the local repository.
     */
    @Override
    public void processTypeDefPatchMismatchEvent(String         sourceName,
                                                 String         originatorMetadataCollectionId,
                                                 String         originatorServerName,
                                                 String         originatorServerType,
                                                 String         originatorOrganizationName,
                                                 String         targetMetadataCollectionId,
                                                 TypeDefSummary targetTypeDefSummary,
                                                 TypeDef        otherTypeDef,
                                                 String         errorMessage)
    {
        // TODO
    }


    /*
     * ===========================
     * Metadata Collections
     */


    /**
     * Remember the metadata collection name for this metadata collection Id. If the metadata collection id
     * is null, it is ignored.
     *
     * @param metadataCollectionId unique identifier (guid) for the metadata collection.
     * @param metadataCollectionName display name for the metadata collection (can be null).
     */
    synchronized void registerMetadataCollection(String    metadataCollectionId,
                                                 String    metadataCollectionName)
    {
        if (metadataCollectionId != null)
        {
            metadataCollectionNames.put(metadataCollectionId, metadataCollectionName);
        }
    }


    /**
     * Return the metadata collection name (or null) for a metadata collection id.
     *
     * @param metadataCollectionId unique identifier (guid) for the metadata collection.
     * @return display name
     */
    public synchronized String getMetadataCollectionName(String    metadataCollectionId)
    {
        if (metadataCollectionId != null)
        {
            return metadataCollectionNames.get(metadataCollectionId);
        }
        else
        {
            return null;
        }
    }


    /* ========================
     * Private error handling
     */


    /**
     * Throws a logic error exception when the repository content manager is called with invalid parameters.
     * Normally this means the repository content manager methods have been called in the wrong order.
     *
     * @param sourceName source of the request (used for logging)
     * @param originatingMethodName method that called the repository validator
     * @param localMethodName local method that deleted the error
     */
    private void throwContentManagerLogicError(String     sourceName,
                                               String     originatingMethodName,
                                               String     localMethodName)
    {
        throw new OMRSLogicErrorException(OMRSErrorCode.CONTENT_MANAGER_LOGIC_ERROR.getMessageDefinition(sourceName,
                                                                                                         localMethodName,
                                                                                                         originatingMethodName),
                                          this.getClass().getName(),
                                          localMethodName);
    }
}
