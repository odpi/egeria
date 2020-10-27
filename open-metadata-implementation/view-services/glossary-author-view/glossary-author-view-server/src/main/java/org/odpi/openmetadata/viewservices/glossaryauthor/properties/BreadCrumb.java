/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria category. */
package org.odpi.openmetadata.viewservices.glossaryauthor.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;

import java.util.StringTokenizer;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static java.lang.Enum.valueOf;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
/**
 * A breadcrumb is an item with in a breadcrumb trail; it has a displayName, guid, path and type. This is useful in user interfaces to be
 * able to display a breadcrumb widget.
 */
public class BreadCrumb {
    private String path;
    private String guid;
    private String displayName;
    private NodeType nodeType;
    BreadCrumb lastBreadCrumb;

    public BreadCrumb(String guid, String displayName, NodeType nodeType) {
        this.guid = guid;
        this.displayName = displayName;
        this.nodeType = nodeType;
    }

    public BreadCrumb(BreadCrumb breadCrumb, BreadCrumb lastBreadCrumb) {
        this.guid = breadCrumb.getGuid();
        this.displayName =  breadCrumb.getDisplayName();
        this.nodeType = breadCrumb.getNodeType();
        this.lastBreadCrumb = lastBreadCrumb;
        setPath();
    }

    private void setPath() {
        StringBuffer sb = new StringBuffer();
        if (lastBreadCrumb != null) {
            sb.append(lastBreadCrumb.getPath());
        }
        sb.append("/nodeType/");
        sb.append(nodeType.name());
        sb.append("/guid/");
        sb.append(guid);
        sb.append("/displayName/");
        sb.append(displayName);
        path = sb.toString();
    }

    public String getGuid() {
        return guid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    /**
     * The path of this breadcrumb locates it within the breadcrumbtrail, relative to the start of the trail.
     * The path is composed of segments of
     * <ol>
     * <li>The nodeType</li>
     * <li>Guid</li>
     * <li>Display name</li>
     * </ol>
     * The path takes the form
     * /nodeType/{nodetype1}/guid/{guid1}/displayName/{displayName1}/nodeType/{nodetype2}/guid/{guid2}/displayName/{displayName2}/.....
     *
     * @return the path
     */
    public String getPath() {
        return path;
    }
}


