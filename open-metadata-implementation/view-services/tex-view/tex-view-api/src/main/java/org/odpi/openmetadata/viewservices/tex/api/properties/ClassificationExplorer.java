/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.tex.api.properties;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefAttribute;

import java.util.ArrayList;
import java.util.List;


public class ClassificationExplorer {

    private ClassificationDef classificationDef;
    private List<TypeDefAttribute> inheritedAttributes;
    private List<String>             subTypeNames;


    public ClassificationExplorer(ClassificationDef def) {
        classificationDef = def;
    }

    public ClassificationDef getClassificationDef() { return classificationDef; }

    public List<TypeDefAttribute> getInheritedAttributes() { return inheritedAttributes; }

    public List<String> getSubTypeNames() { return subTypeNames; }

    public void addSubTypeName(String subTypeName) {
        if (subTypeNames == null) {
            subTypeNames = new ArrayList<>();
        }
        subTypeNames.add(subTypeName);
    }

    public void addInheritedAttributes(List<TypeDefAttribute> inheritedAttrs) {
        if (inheritedAttrs != null) {
            if (inheritedAttributes == null) {
                inheritedAttributes = new ArrayList<>();
            }
            inheritedAttributes.addAll(inheritedAttrs);
        }
    }

}

