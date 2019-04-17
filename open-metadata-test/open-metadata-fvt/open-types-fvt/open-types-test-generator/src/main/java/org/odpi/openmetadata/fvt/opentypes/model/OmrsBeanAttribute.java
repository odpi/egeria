/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.fvt.opentypes.model;

public class OmrsBeanAttribute {
    public String name = null;
    public String description = null;
    public String type = null;
    public boolean isEnum = false;
    public boolean isMap = false;
    public boolean isList = false;
    public boolean isSet = false;
    // it is useful to process references as attributes- in that case stash the package for reference here.
    public boolean isReference = false;
    public String referencePackage;

    // reference relationship
    public String referenceRelationshipName;
    // TODO unique attribute ?
}
