/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis;

import org.odpi.openmetadata.adapters.connectors.integration.openapis.properties.OpenAPISchema;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * BeanReferences decides when the OpenAPIMonitorIntegrationConnector should link a value to the shared schema type
 * of one of the specification's component beans rather than catalogue the bean's fields beneath it, and works out
 * the shape of each bean.
 * <ul>
 *     <li>A bean is a component schema with fields of its own, or a subclass of another bean.  Enums, primitives
 *     and empty objects are catalogued inline.</li>
 *     <li>A subclass is written as allOf its superclass's $ref and an object holding its own fields (the object is
 *     absent when it adds none).  Its schema type holds only its own fields and links to its superclass's.</li>
 *     <li>A value whose schema is oneOf (or anyOf) a list of beans may hold any one of them - a choice.</li>
 * </ul>
 */
class BeanReferences
{
    /**
     * Static helpers only.
     */
    private BeanReferences()
    {
    }


    /**
     * Return the names of the component beans that a value is typed by: one name when it is a single bean (directly
     * through its $ref, or as the element type of an array), several when it may be any one of a list of beans
     * (oneOf/anyOf).  A value whose type is not built from beans returns null - including a choice where one of the
     * alternatives is not a bean.
     *
     * @param schema the value's schema as written in the specification - may be null
     * @param componentSchemas the specification's component schemas, keyed by name
     * @return list of bean names, or null if the value is not typed by beans
     */
    static List<String> getReferencedBeans(OpenAPISchema              schema,
                                           Map<String, OpenAPISchema> componentSchemas)
    {
        if ((schema == null) || (componentSchemas == null))
        {
            return null;
        }

        List<String> beanNames = getElementBeans(schema, componentSchemas);

        if (beanNames != null)
        {
            return beanNames;
        }

        OpenAPISchema typeSchema = schema;

        if (schema.get$ref() != null)
        {
            typeSchema = componentSchemas.get(getComponentName(schema.get$ref()));
        }

        if ((typeSchema != null) && ("array".equals(typeSchema.getType())) && (typeSchema.getItems() != null))
        {
            return getElementBeans(typeSchema.getItems(), componentSchemas);
        }

        return null;
    }


    /**
     * Return the beans that a single (non-array) value is typed by.
     *
     * @param schema the value's schema
     * @param componentSchemas the specification's component schemas
     * @return list of bean names, or null
     */
    private static List<String> getElementBeans(OpenAPISchema              schema,
                                                Map<String, OpenAPISchema> componentSchemas)
    {
        if (schema.get$ref() != null)
        {
            String componentName = getComponentName(schema.get$ref());

            if (isBean(componentName, componentSchemas))
            {
                return List.of(componentName);
            }

            OpenAPISchema component = componentSchemas.get(componentName);

            return (component == null) ? null : getChoiceBeans(component, componentSchemas);
        }

        return getChoiceBeans(schema, componentSchemas);
    }


    /**
     * Return the beans listed as the alternatives of a schema - its oneOf or anyOf, or an allOf that does no more than
     * wrap a single $ref (as a generator does to attach a description to a reference).
     *
     * @param schema schema to examine
     * @param componentSchemas the specification's component schemas
     * @return list of bean names (duplicates removed, order kept), or null if any alternative is not a bean
     */
    private static List<String> getChoiceBeans(OpenAPISchema              schema,
                                               Map<String, OpenAPISchema> componentSchemas)
    {
        List<OpenAPISchema> alternatives = (schema.getOneOf() != null) ? schema.getOneOf() : schema.getAnyOf();

        if ((alternatives == null) && (schema.getAllOf() != null) && (schema.getAllOf().size() == 1))
        {
            alternatives = schema.getAllOf();
        }

        if ((alternatives == null) || (alternatives.isEmpty()))
        {
            return null;
        }

        Set<String> beanNames = new LinkedHashSet<>();

        for (OpenAPISchema alternative : alternatives)
        {
            if ((alternative == null) || (alternative.get$ref() == null))
            {
                return null;
            }

            String componentName = getComponentName(alternative.get$ref());

            if (! isBean(componentName, componentSchemas))
            {
                return null;
            }

            beanNames.add(componentName);
        }

        return new ArrayList<>(beanNames);
    }


    /**
     * Return the component name at the end of a $ref such as "#/components/schemas/Connection".
     *
     * @param ref $ref value
     * @return component name
     */
    static String getComponentName(String ref)
    {
        int lastSlash = ref.lastIndexOf('/');

        return (lastSlash >= 0) ? ref.substring(lastSlash + 1) : ref;
    }


    /**
     * Return whether a component is a bean - it has fields of its own, or it is a subclass of a bean.
     *
     * @param componentName name of the component
     * @param componentSchemas the specification's component schemas
     * @return boolean
     */
    static boolean isBean(String                     componentName,
                          Map<String, OpenAPISchema> componentSchemas)
    {
        Set<String> visited = new HashSet<>();
        String      current = componentName;

        while ((current != null) && (visited.add(current)))
        {
            OpenAPISchema component = componentSchemas.get(current);

            if (component == null)
            {
                return false;
            }

            if (! getOwnFields(component).isEmpty())
            {
                return true;
            }

            current = getSuperclassRef(component);
        }

        return false;
    }


    /**
     * Return the bean that a component inherits from - the $ref in its allOf - or null if it is not a subclass of a bean.
     *
     * @param component component schema
     * @param componentSchemas the specification's component schemas
     * @return superclass bean name, or null
     */
    static String getSuperclassName(OpenAPISchema              component,
                                    Map<String, OpenAPISchema> componentSchemas)
    {
        String superclassName = getSuperclassRef(component);

        if ((superclassName != null) && (isBean(superclassName, componentSchemas)))
        {
            return superclassName;
        }

        return null;
    }


    /**
     * Return the name of the first component referenced from a component's allOf.
     *
     * @param component component schema
     * @return component name, or null
     */
    private static String getSuperclassRef(OpenAPISchema component)
    {
        if ((component != null) && (component.getAllOf() != null))
        {
            for (OpenAPISchema part : component.getAllOf())
            {
                if ((part != null) && (part.get$ref() != null))
                {
                    return getComponentName(part.get$ref());
                }
            }
        }

        return null;
    }


    /**
     * Return the fields that a schema declares itself - its own properties plus those of the inline objects in its
     * allOf.  Inherited fields are not included.
     *
     * @param schema schema - may be null
     * @return map of field name to field schema, in declaration order (empty if there are none)
     */
    static Map<String, OpenAPISchema> getOwnFields(OpenAPISchema schema)
    {
        Map<String, OpenAPISchema> fields = new LinkedHashMap<>();

        if (schema != null)
        {
            if (schema.getProperties() != null)
            {
                fields.putAll(schema.getProperties());
            }

            if (schema.getAllOf() != null)
            {
                for (OpenAPISchema part : schema.getAllOf())
                {
                    if ((part != null) && (part.get$ref() == null) && (part.getProperties() != null))
                    {
                        fields.putAll(part.getProperties());
                    }
                }
            }
        }

        return fields;
    }


    /**
     * Return the names of the required fields that a schema declares itself, including those listed by the inline
     * objects in its allOf.
     *
     * @param schema schema - may be null
     * @return set of field names (empty if there are none)
     */
    static Set<String> getOwnRequiredFields(OpenAPISchema schema)
    {
        Set<String> required = new HashSet<>();

        if (schema != null)
        {
            if (schema.getRequired() != null)
            {
                required.addAll(schema.getRequired());
            }

            if (schema.getAllOf() != null)
            {
                for (OpenAPISchema part : schema.getAllOf())
                {
                    if ((part != null) && (part.get$ref() == null) && (part.getRequired() != null))
                    {
                        required.addAll(part.getRequired());
                    }
                }
            }
        }

        return required;
    }


    /**
     * Return the nearest bean that every alternative of a choice inherits from (or is), if there is one.  A
     * polymorphic value is declared as the subclasses of a common superclass - sometimes with the superclass among
     * the alternatives, sometimes not (when it is abstract) - and that superclass is the type the value is declared as.
     *
     * @param beanNames alternatives of the choice
     * @param componentSchemas the specification's component schemas
     * @return name of the nearest common superclass, or null
     */
    static String getChoiceRoot(List<String>               beanNames,
                                Map<String, OpenAPISchema> componentSchemas)
    {
        if ((beanNames == null) || (beanNames.isEmpty()))
        {
            return null;
        }

        for (String candidate : getAncestry(beanNames.get(0), componentSchemas))
        {
            boolean isCommon = true;

            for (String beanName : beanNames)
            {
                if (! getAncestry(beanName, componentSchemas).contains(candidate))
                {
                    isCommon = false;
                    break;
                }
            }

            if (isCommon)
            {
                return candidate;
            }
        }

        return null;
    }


    /**
     * Return the unique name of a choice: the nearest common superclass (or "OneOf" when the alternatives share none)
     * followed by a short hash of the full set of alternatives, so two choices with the same superclass but different
     * alternatives are kept apart and the same set always produces the same name.
     *
     * @param beanNames alternatives of the choice
     * @param componentSchemas the specification's component schemas
     * @return name for the choice
     */
    static String getChoiceName(List<String>               beanNames,
                                Map<String, OpenAPISchema> componentSchemas)
    {
        String root = getChoiceRoot(beanNames, componentSchemas);

        return ((root != null) ? root : "OneOf") + "::" + getShortHash(String.join(",", new TreeSet<>(beanNames)));
    }


    /**
     * Return a bean and the beans it inherits from.
     *
     * @param beanName bean to start from
     * @param componentSchemas the specification's component schemas
     * @return the bean's own name and those of its superclasses
     */
    private static Set<String> getAncestry(String                     beanName,
                                           Map<String, OpenAPISchema> componentSchemas)
    {
        Set<String> ancestry = new LinkedHashSet<>();
        String      current  = beanName;

        while ((current != null) && (ancestry.add(current)))
        {
            current = getSuperclassRef(componentSchemas.get(current));
        }

        return ancestry;
    }


    /**
     * Return the first 8 hex characters of the SHA-256 digest of a string.
     *
     * @param value string to digest
     * @return short hash
     */
    private static String getShortHash(String value)
    {
        try
        {
            byte[]        digest = MessageDigest.getInstance("SHA-256").digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex    = new StringBuilder();

            for (int i = 0; i < 4; i++)
            {
                hex.append(String.format("%02x", digest[i]));
            }

            return hex.toString();
        }
        catch (NoSuchAlgorithmException noSHA256)
        {
            return Integer.toHexString(value.hashCode());
        }
    }
}
