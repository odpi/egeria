/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";

import '../shared-styles.js';
import '../token-ajax.js';

/**
*
* DetailsPanel implements a web component for presentation of details of the focus or view type
*
* It should present to the user a summary of the key characteristics of the focus or view type. This includes:
*
*  For entity types:
*    * type name
*    * description
*    * attributes (listing names of types)
*    * relationships (listing attribute names and types and providing a link to the relationship details)
*    * classifications (listing attribute names and types and providing a link to the classification details)
*
*  For relationship types:
*    * type name
*    * description
*    * attributes (listing names of types)
*    * ends (listing entity types and providing a link to the entity details)
*
*  For classification types:
*
*    * type name
*    * description
*    * attributes (listing names of types)
*    * valid entity types (listing entity types and providing a link to the entity details)
*
* The details panel reacts to events that indicate a change of focus (an entity type's details shoudl be shown)
* or a change of view (a relationship or classification details should be shown).
* Because the details panel also includes links to entity, relationship and classification types (as outlined above)
* the details panel also generates events requesting changeFocus or changeView.
*
* When the UI is first loaded there will be no type information, and until a focus or view type is selected there is
* no particular type to display - therefore following initial load and until a focus/view type is selected, the details
* panel will be blank.
*
*/

class RexDetailsPanel extends PolymerElement {

    static get template() {
        return html`

            <style include="rex-styles">

                .linkable {
                    background-color: #CCCCCC;
                    color: black;
                    cursor: pointer;
                    padding: 0px;
                    width: 200px;
                    border: none;
                    text-align: left;
                    outline: none;
                    font-size: 12px;
                }

                .collapsible {
                    background-color: #CCCCCC;
                    color: black;
                    cursor: pointer;
                    padding: 5px;
                    width: 300px;
                    border: none;
                    text-align: left;
                    outline: none;
                    font-size: 12px;
                }

                .content {
                    padding: 10px 0px 10px 10px;
                    display: none;
                    overflow: hidden;
                    background-color: #CCCCCC;
                }

                 .highlight {
                    background-color: #EEEEEE;
                    color: black;
                    padding: 5px;
                    width: 300px;
                    border: 2px;
                    text-align: left;
                    outline: none;
                    font-size: 12px;
                 }

                </style>

                <body>

                    <div style="height:250px; position:relative; padding:0px 20px 20px; ">
                    <div id="detailsDiv" style="position:relative; padding:0px 20px 20px;">
                        Entity or Relationship details will be displayed here when an instance is selected
                    </div>
              </body>

        `; }


    static get properties() {
        return {

            // Reference to TypeManager element which this DetailsPanel depends on.
            // The TypeManager is created in the DOM of the parent and is passed in
            // once we are all initialised. This avoids any direct dependency from DetailsPanel
            // on TypeManager.

            typeManager: Object,

            // Reference to InstanceRetriever element which this DetailsPanel depends on.
            // The InstanceRetriever is created in the DOM of the parent and is passed in
            // once we are all initialised. This avoids any direct dependency from DetailsPanel
            // on InstanceRetriever.

            instanceRetriever: Object

        };
    }




    /*
     * Element is ready
     */
    ready() {
        // Call super.ready() first to initialise node hash...
        super.ready();

    }


    // Inter-component event handlers

    /*
     *  Inbound event: root-entity-loaded
     *
     *  This function is called when a new root has been loaded.
     */
    inEvtRootEntityLoaded(e) {

    }

    /*
     *  Inbound event: focus-entity-changed
     */
    inEvtFocusEntityChanged(guid) {
         // Check whether this is a known entity.
         var expEntity = this.instanceRetriever.getFocusEntity();
         if (expEntity === null) {
             // entity is not known - there is nothing useful to switch to so leave the details panel as is
             return;
         }
         // The entity is known
         this.clearDetails();
         this.displayEntityDetails(expEntity);
    }

    /*
     *  Inbound event: focus-entity-cleared
     */
    inEvtFocusEntityCleared() {
        // Clear the details pane - nothing is selected
        this.clearDetails();
    }

    /*
     *  Inbound event: focus-relationship-cleared
     */
    inEvtFocusRelationshipCleared() {
        // Clear the details pane - nothing is selected
        this.clearDetails();
    }

    /*
     *  Outbound event: change-focus
     */
    outEvtChangeFocus(typeName) {
        var customEvent = new CustomEvent('change-focus',
            {   bubbles: true,
                composed: true,
                detail: {
                    source: "details-panel",
                    focusType: typeName
                }
            }
        );
        this.dispatchEvent(customEvent);
    }

    /*
     *  Outbound event: change-focus
     */
    outEvtChangeView(cat, typeName) {
        var customEvent = new CustomEvent('change-view',
            {
                bubbles: true,
                composed: true,
                detail: {
                    source: "details-panel",
                    viewCategory: cat,
                    viewType: typeName
                }
            }
        );
        this.dispatchEvent(customEvent);
    }




    /*
     *  Inbound event: focus-relationship-changed
     */
    inEvtFocusRelationshipChanged(guid) {
        // Check whether this is a known relationship.
        var expRelationship = this.instanceRetriever.getFocusRelationship();
        if (expRelationship === null) {
            // relationship is not known - there is nothing useful to switch to so leave the details panel as is
            return;
        }
        // The relationship is known
        this.clearDetails();
        this.displayRelationshipDetails(expRelationship);
    }

    inEvtGraphCleared() {
        this.clearDetails();
    }



    // Component logic



    // Clear down the details panel
    // In the UI, once a type has become the focus or view type there will always be a type selected,
    // so there is no need to reinstate the introductory text about selecting a type - it is sufficient
    // to simply clear the details text.
    clearDetails() {
        var details = this.$.detailsDiv;
        details.innerHTML = "";
    }


    // ENTITY DETAILS


    /* The desirable order of entity details is as follows:
     * GUID (not interesting but it is the guaranteed unique id of the instance)
     * Version
     * Status (and status-on-delete iff deleted)
     * Properties
     *   type-defined attribute properties - as name : value
     * Classifications
     * Type information (collapsible)
     *   typeName
     *   typeGUID
     *   typeVersion
     * Home
     *   metadatCollectionName
     *   metadataCollectionId
     * Control Properties
     *   createdBy/createdTime
     *   updatedBy/updateTime
     *   maintainedBy
     * Additional fields:
     *   instanceURL
     *   instanceLicense
     *   instanceProvenanceType
     *   replicatedBy
     */



    displayEntityDetails(expEntity) {

        var entity = expEntity.entityDetail;
        var entityDigest = expEntity.entityDigest;
        var label = entityDigest.label;
        var gen = entityDigest.gen;

        var typeCategory = "Entity";
        var details = this.$.detailsDiv;
        details.innerHTML = "";

        // Add type header
        details.innerHTML = "Entity: "+label+"  [added in gen "+gen+"]";

        // GUID
        this.entityAddGUID(details, entity);

        // Type
        this.entityAddTypeName(details, entity)

        // Version
        this.entityAddVersion(details, entity);

        // Status
        this.entityAddStatus(details, entity);

        // Properties
        this.entityAddInstanceProperties(details, entity);

        // Classifications
        this.entityAddClassifications(details, entity);

        // Home
        this.entityAddHome(details, entity);

        // Control properties
        this.entityAddControlInformation(details, entity)

        // Detailed Type
        this.entityAddDetailedTypeInformation(details, entity)

    }

    entityAddGUID(details, entity) {
        var guid = entity.guid;
        var textnode = document.createTextNode("GUID : "+guid);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
    }


    entityAddVersion(details, entity) {
        var version = entity.version;
        var textnode = document.createTextNode("Version : "+version);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
    }


    entityAddStatus(details, entity) {
         var currentStatus = entity.status;
         var textnode = document.createTextNode("Status : "+currentStatus);
         var paranode = document.createElement("p");
         paranode.appendChild(textnode);
         details.appendChild(paranode);
         // statusOnDelete - displayed IFF current status is DELETED
         if (currentStatus === "DELETED") {
             var statusOnDelete = entity.statusOnDelete;
             var textnode = document.createTextNode("statusOnDelete : "+statusOnDelete);
             var paranode = document.createElement("p");
             paranode.appendChild(textnode);
             details.appendChild(paranode);
         }
     }

    entityAddInstanceProperties(details, entity) {

        var textnode = document.createTextNode("Properties:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var properties = entity.properties;

        if (properties !== undefined) {
            this.displayProperties(properties, listnode);
        }
        else {
            var itemnode = document.createElement("li");
            itemnode.innerHTML = "list is empty";
            listnode.append(itemnode);
        }

    }


    entityProxyAddUniqueProperties(details, entityProxy) {

        var textnode = document.createTextNode("Unique Properties:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var properties = entityProxy.uniqueProperties;
        if (properties !== undefined) {
            this.displayProperties(properties, listnode);
        }
        else {
            var itemnode = document.createElement("li");
            itemnode.innerHTML = "list is empty";
            listnode.append(itemnode);
        }
    }

    displayProperties(props, listelement) {

        var propertyNamesUnsorted = props.propertyNames;
        var propertyNamesSorted = propertyNamesUnsorted.sort();

        propertyNamesSorted.forEach(propName => {

            var itemnode = document.createElement("li");
            listelement.appendChild(itemnode);

            // property category
            var ipCat = props.instanceProperties[propName].instancePropertyCategory;

            // Handle at least the following property categories
            // PRIMITIVE
            // ENUM
            // MAP
            // ARRAY
            switch (ipCat) {

                case "PRIMITIVE" :
                    var ppCat = props.instanceProperties[propName].primitiveDefCategory;
                    var ppvalue = props.instanceProperties[propName].primitiveValue;
                    itemnode.innerHTML = propName + " : " + ppvalue;
                    break;

                case "ENUM" :
                    var epOrd = props.instanceProperties[propName].ordinal;
                    var epSymName = props.instanceProperties[propName].symbolicName;
                    itemnode.innerHTML = propName + " : " + epSymName;
                    break;

                case "MAP" :
                    var propMap = props.instanceProperties[propName].mapValues;  // InstanceProperties
                    // recurse -
                    itemnode.innerHTML = propName + " :";
                    var listnode = document.createElement("ul");
                    itemnode.appendChild(listnode);
                    this.displayProperties(propMap, listnode)
                    break;

                case "ARRAY":
                     var propArray = props.instanceProperties[propName].arrayValues;  // InstanceProperties
                     // recurse -
                     itemnode.innerHTML = propName + " :";
                     var listnode = document.createElement("ul");
                     itemnode.appendChild(listnode);
                     this.displayProperties(propArray, listnode)
                     break;

                default:
                    var value = "<not supported yet>";
                    itemnode.innerHTML = propName + " : " + value;
                    break;
            }


        });
    }


    entityAddClassifications(details, entity) {

        var textnode = document.createTextNode("Classifications:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);


        // Present a list of all classifications in alpha order.
        var classificationList = entity.classifications;

        if (classificationList !== undefined) {

            var classificationNamesUnsorted = [];
            var c = 0;

            var listnode = document.createElement("ul");
            details.appendChild(listnode);

            classificationList.forEach(cls => {
                classificationNamesUnsorted[c] = cls.name;
                c++;
            });

            var classificationNamesSorted = classificationNamesUnsorted.sort();

            classificationNamesSorted.forEach(clsName => {
                var itemnode = document.createElement("li");
                itemnode.innerHTML = clsName;
                // Append a collapsible section that shows the classification properties and origin....
                // TODO It would be good to include classification props and origin
                listnode.appendChild(itemnode);
             });
        }
        else {
            // Create an explicitly empty list - keep similar formatting to non-empty case
            var listnode = document.createElement("ul");
            details.appendChild(listnode);
            var itemnode = document.createElement("li");
            itemnode.innerHTML = "list is empty";
            listnode.appendChild(itemnode);
        }

    }


    entityAddTypeName(details, entity) {

       var typeName = entity.type.typeDefName;
       var textnode = document.createTextNode("Type: "+typeName);
       var paranode = document.createElement("p");
       paranode.appendChild(textnode);
       details.appendChild(paranode);
    }


    entityAddDetailedTypeInformation(details, entity) {

        var typeName = entity.type.typeDefName;

        // Collapsible section on type information
        var button = document.createElement("button");
        button.className = "collapsible";
        button.innerHTML = "Detailed type information";

        var div = document.createElement("div");
        div.className = "content";

        var textnode = document.createTextNode("typeName : "+typeName);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        div.appendChild(paranode);

        var typeGUID = entity.type.typeDefGUID;
        var textnode = document.createTextNode("typeGUID : "+typeGUID);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        div.appendChild(paranode);

        var typeVersion = entity.type.typeVersion;
        var textnode = document.createTextNode("typeVersion : "+typeVersion);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        div.appendChild(paranode);

        details.append(button);
        details.append(div);

        button.addEventListener("click", function() {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.display === "block") {
                content.style.display = "none";
            } else {
                content.style.display = "block";
            }
          });

    }



     entityAddHome(details, entity) {

        var textnode = document.createTextNode("Home Repository:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        // metadataCollectionName
        var metadataCollectionName = entity.metadataCollectionName;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "metadataCollectionName : "+metadataCollectionName;
        listnode.appendChild(itemnode);

        // metadataCollectionId
        var metadataCollectionId = entity.metadataCollectionId;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "metadataCollectionId : <br>  "+metadataCollectionId;
        listnode.appendChild(itemnode);

     }


    entityAddControlInformation(details, entity) {

        var textnode = document.createTextNode("OMRS Control Properties:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        // createdBy
        var createdBy = entity.createdBy;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "createdBy : "+createdBy;
        listnode.appendChild(itemnode);

        // createTime - Date
        var createTime = entity.createTime;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "createTime : "+createTime;
        listnode.appendChild(itemnode);

        // updatedBy
        var updatedBy = entity.updatedBy;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "updatedBy : "+updatedBy;
        listnode.appendChild(itemnode);

        // updateTime - Date
        var updateTime = entity.updateTime;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "updateTime : "+updateTime;
        listnode.appendChild(itemnode);

        // maintainedBy - List<String>
        var maintainerList = entity.maintainedBy;

        var maintainersNode = document.createElement("li");
        maintainersNode.innerHTML = "maintainedBy : ";
        listnode.appendChild(maintainersNode);

        // list
        var maintainersSubList = document.createElement("ul");
        maintainersNode.appendChild(maintainersSubList);

        if (maintainerList !== undefined) {
            // add each maintainer
            maintainerList.forEach(maintainer => {
                var itemnode = document.createElement("li");
                itemnode.innerHTML = maintainer;
                maintainersSubList.append(itemnode);
            });
        }
        else {
            var itemnode = document.createElement("li");
            itemnode.innerHTML = "list is empty";
            maintainersSubList.append(itemnode);
        }

        // instanceURL
        var instanceURL = entity.instanceURL;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "instanceURL : "+instanceURL;
        listnode.appendChild(itemnode);

        // instanceLicense
        var instanceLicense = entity.instanceLicense;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "instanceLicense : "+instanceLicense;
        listnode.appendChild(itemnode);

        // instanceProvenanceType - enum
        var instanceProvenanceType = entity.instanceProvenanceType;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "instanceProvenanceType : "+instanceProvenanceType;
        listnode.appendChild(itemnode);

        // replicatedBy
        var replicatedBy = entity.replicatedBy;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "replicatedBy : "+replicatedBy;
        listnode.appendChild(itemnode);

    }


    entityAddRelationships(details, typeName) {

        var textnode = document.createTextNode("Relationships:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var eex = this.typeManager.getEntityType(typeName);

        // Present a list of all relationships in alpha order, with inherited relationships in italics.

        var relationshipEntries = {};

        // Inherited relationships
        var inheritedRelNames = eex.inheritedRelationshipNames;
        if (inheritedRelNames !== undefined) {
            inheritedRelNames.forEach(inheritedRelName => {
                relationshipEntries[inheritedRelName]={};
                relationshipEntries[inheritedRelName].inherited=true;
            });
        }

        // Local relationships
        var relationshipNames = eex.relationshipNames;
        if (relationshipNames !== undefined) {
            relationshipNamesSorted = relationshipNames.sort();
            relationshipNamesSorted.forEach(relationshipName => {
                relationshipEntries[relationshipName]={};
                relationshipEntries[relationshipName].inherited=false;
            });
        }


        var relationshipNamesUnsorted = Object.keys(relationshipEntries);
        var relationshipNamesSorted = relationshipNamesUnsorted.sort();

        relationshipNamesSorted.forEach(relName => {

            var itemnode = document.createElement("li");
            this.appendRelationshipLink(itemnode, relName, relationshipEntries[relName].inherited);
            listnode.appendChild(itemnode);

        });
    }



     entityAddClassificationsOld(details, typeName) {

        var textnode = document.createTextNode("Classifications:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var eex = this.typeManager.getEntityType(typeName);

        // Present a list of all classifications in alpha order, with inherited classifications in italics.

        var classificationEntries = {};

        // Inherited classifications
        var inheritedClsNames = eex.inheritedClassificationNames;
        if (inheritedClsNames !== undefined) {
            inheritedClsNames.forEach(inheritedClsName => {
                classificationEntries[inheritedClsName]={};
                classificationEntries[inheritedClsName].inherited=true;
            });
        }

        // Local classifications
        var classificationNames = eex.classificationNames;
        if (classificationNames !== undefined) {
            classificationNamesSorted = classificationNames.sort();
            classificationNamesSorted.forEach(classificationName => {
                classificationEntries[classificationName]={};
                classificationEntries[classificationName].inherited=false;
            });
        }


        var classificationNamesUnsorted = Object.keys(classificationEntries);
        var classificationNamesSorted = classificationNamesUnsorted.sort();

        classificationNamesSorted.forEach(clsName => {

            var itemnode = document.createElement("li");
            this.appendClassificationLink(itemnode, clsName, classificationEntries[clsName].inherited === true);
            listnode.appendChild(itemnode);

        });
    }



    // RELATIONSHIP DETAILS


    /* The desirable order of relationship details is as follows:
     * GUID (not interesting but it is the guaranteed unique id of the instance)
     * Version
     * Status (and status-on-delete iff deleted)
     * Properties
     *   type-defined attribute properties - as name : value
     * Entity Proxies for ends of relationship
     * Type information (collapsible)
     *   typeName
     *   typeGUID
     *   typeVersion
     * Home
     *   metadatCollectionName
     *   metadataCollectionId
     * Control Properties
     *   createdBy/createdTime
     *   updatedBy/updateTime
     *   maintainedBy
     * Additional fields:
     *   instanceURL
     *   instanceLicense
     *   instanceProvenanceType
     *   replicatedBy
     */



    displayRelationshipDetails(expRelationship) {

        var relationship = expRelationship.relationship;
        var relationshipDigest = expRelationship.relationshipDigest;
        var label = relationshipDigest.label;
        var gen = relationshipDigest.gen;

        var typeCategory = "Relationship";
        var details = this.$.detailsDiv;
        details.innerHTML = "";

        // Add type header
        details.innerHTML = "Relationship: "+label+"  [added in gen "+gen+"]";

        // GUID
        this.relationshipAddGUID(details, relationship);

        // Type
        this.relationshipAddTypeName(details, relationship)

        // Version
        this.relationshipAddVersion(details, relationship);

        // Status
        this.relationshipAddStatus(details, relationship);

        // Properties
        this.relationshipAddInstanceProperties(details, relationship);

        // Relationship Ends
        this.relationshipAddEnds(details, relationship);

        // Home
        this.relationshipAddHome(details, relationship);

        // Control properties
        this.relationshipAddControlInformation(details, relationship)

        // Detailed Type
        this.relationshipAddDetailedTypeInformation(details, relationship)

    }

    relationshipAddGUID(details, relationship) {
        // GUID - long
        var guid = relationship.guid;
        var textnode = document.createTextNode("GUID : "+guid);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

    }

    relationshipAddVersion(details, relationship) {
        // version - long
        var version = relationship.version;
        var textnode = document.createTextNode("Version : "+version);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

    }

    relationshipAddStatus(details, relationship) {

         // currentStatus - enum; use the getter name not the name of the property
         var currentStatus = relationship.status;
         //('Details Panel: relationship status is '+currentStatus);
         var textnode = document.createTextNode("Status : "+currentStatus);
         var paranode = document.createElement("p");
         paranode.appendChild(textnode);
         details.appendChild(paranode);

         // statusOnDelete - enum -  used IFF current status is DELETED
         if (currentStatus === "DELETED") {
             var statusOnDelete = relatiosnhip.statusOnDelete;
             var textnode = document.createTextNode("statusOnDelete : "+statusOnDelete);
             var paranode = document.createElement("p");
             paranode.appendChild(textnode);
             details.appendChild(paranode);
         }

     }

    relationshipAddInstanceProperties(details, relationship) {

        var textnode = document.createTextNode("Properties:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var properties = relationship.properties;
        if (properties !== undefined) {
            this.displayProperties(properties, listnode);
        }
        else {
            var itemnode = document.createElement("li");
            itemnode.innerHTML = "list is empty";
            listnode.append(itemnode);
        }
    }


    // For each entity proxy we want to see GUID, version, status, properties, typeName, metadataCollectionId, metadataCollectionName
    // If the user wants to see entity control information they can click on the entity.
    // properties are under uniqueProperties.instanceProperties
    relationshipAddEnds(details, relationship) {

        var textnode = document.createTextNode("Relationship Ends:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        // End 1
        var entityOneProxy = relationship.entityOneProxy;
        var textnode = document.createTextNode("Entity One:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        this.displayEntityProxy(details, entityOneProxy);

        // End 2
        var entityTwoProxy = relationship.entityTwoProxy;
        var textnode = document.createTextNode("Entity Two:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        this.displayEntityProxy(details, entityTwoProxy);

    }

    displayEntityProxy(details, entityProxy) {

        var proxyBox = document.createElement("div");
        proxyBox.className = "highlight";
        details.appendChild(proxyBox);

        // GUID
        this.entityAddGUID(proxyBox, entityProxy);
        // TypeName
        this.entityAddTypeName(proxyBox, entityProxy)
        // Version
        this.entityAddVersion(proxyBox, entityProxy);
        // Status
        this.entityAddStatus(proxyBox, entityProxy);
        // Properties
        this.entityProxyAddUniqueProperties(proxyBox, entityProxy);
        // Home
        this.entityAddHome(proxyBox, entityProxy);

    }

    relationshipAddTypeName(details, relationship) {

        var typeName = relationship.type.typeDefName;
        var textnode = document.createTextNode("Type: "+typeName);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
    }


    relationshipAddDetailedTypeInformation(details, relationship) {

        var typeName = relationship.type.typeDefName;

        // Collapsible section on type information
        var button = document.createElement("button");
        button.className = "collapsible";
        button.innerHTML = "Detailed type information";


        var div = document.createElement("div");
        div.className = "content";

        var textnode = document.createTextNode("typeName : "+typeName);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        div.appendChild(paranode);

        var typeGUID = relationship.type.typeDefGUID;
        var textnode = document.createTextNode("typeGUID : "+typeGUID);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        div.appendChild(paranode);

        var typeVersion = relationship.type.typeVersion;
        var textnode = document.createTextNode("typeVersion : "+typeVersion);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        div.appendChild(paranode);

        details.append(button);
        details.append(div);

        button.addEventListener("click", function() {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.display === "block") {
                content.style.display = "none";
            } else {
                content.style.display = "block";
            }
          });

    }


    relationshipAddHome(details, relationship) {

        var textnode = document.createTextNode("Home Repository:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        // metadataCollectionName
        var metadataCollectionName = relationship.metadataCollectionName;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "metadataCollectionName : "+metadataCollectionName;
        listnode.appendChild(itemnode);

        // metadataCollectionId
        var metadataCollectionId = relationship.metadataCollectionId;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "metadataCollectionId : <br>  "+metadataCollectionId;
        listnode.appendChild(itemnode);

     }


    relationshipAddControlInformation(details, relationship) {

        var textnode = document.createTextNode("OMRS Control Properties:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        // createdBy
        var createdBy = relationship.createdBy;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "createdBy : "+createdBy;
        listnode.appendChild(itemnode);

        // createTime - Date
        var createTime = relationship.createTime;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "createTime : "+createTime;
        listnode.appendChild(itemnode);

        // updatedBy
        var updatedBy = relationship.updatedBy;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "updatedBy : "+updatedBy;
        listnode.appendChild(itemnode);

        // updateTime - Date
        var updateTime = relationship.updateTime;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "updateTime : "+updateTime;
        listnode.appendChild(itemnode);

        // maintainedBy - List<String>
        var maintainerList = relationship.maintainedBy;

        var maintainersNode = document.createElement("li");
        maintainersNode.innerHTML = "maintainedBy : ";
        listnode.appendChild(maintainersNode);

        // list
        var maintainersSubList = document.createElement("ul");
        maintainersNode.appendChild(maintainersSubList);

        if (maintainerList !== undefined) {
            // add each maintainer
            maintainerList.forEach(maintainer => {
                var itemnode = document.createElement("li");
                itemnode.innerHTML = maintainer;
                maintainersSubList.append(itemnode);
            });
        }
        else {
            var itemnode = document.createElement("li");
            itemnode.innerHTML = "list is empty";
            maintainersSubList.append(itemnode);
        }

        // instanceURL
        var instanceURL = relationship.instanceURL;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "instanceURL : "+instanceURL;
        listnode.appendChild(itemnode);

        // instanceLicense
        var instanceLicense = relationship.instanceLicense;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "instanceLicense : "+instanceLicense;
        listnode.appendChild(itemnode);

        // instanceProvenanceType - enum
        var instanceProvenanceType = relationship.instanceProvenanceType;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "instanceProvenanceType : "+instanceProvenanceType;
        listnode.appendChild(itemnode);

        // replicatedBy
        var replicatedBy = relationship.replicatedBy;
        var itemnode = document.createElement("li");
        itemnode.innerHTML = "replicatedBy : "+replicatedBy;
        listnode.appendChild(itemnode);

    }



    // CLASSIFICATION DETAILS


    /*
     * The user may have already selected a focus type - or not.
     * If so, include a back link to the focus type; if not then just present the relationship details (no back link)
     *
     */
    displayClassificationDetails(typeName) {

        var typeCategory = "Classification";
        var details = this.$.detailsDiv;
        details.innerHTML = "";

        if (this.focusType !== undefined) {
            var para = document.createElement("div");
            para.innerHTML = "Return to focus entity type: ";
            details.append(para);
            var button = document.createElement("button");
            button.className = "linkable";
            button.innerHTML = this.focusType;
            button.addEventListener('click', () => this.entityLinkHandler(this.focusType) );
            para.append(button);
        }

        var textnode = document.createTextNode("Classification Type: "+typeName);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

        this.classificationAddDescription(details, typeName);
        this.classificationAddAttributes(details, typeName);
        this.classificationAddEntities(details, typeName);

    }


    classificationAddDescription(details, typeName) {

        var textnode = document.createTextNode("Description:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var cex = this.typeManager.getClassificationType(typeName);
        var description = cex.classificationDef.description;
        var textnode = document.createTextNode(description);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

    }

     classificationAddAttributes(details, typeName) {

        var textnode = document.createTextNode("Attributes:");
        details.appendChild(textnode);

        var cex = this.typeManager.getClassificationType(typeName);

        var attributeEntries = {};

        // There are only local attributes - currently...

        // Local attributes
        var localProps = cex.classificationDef.propertiesDefinition;
        if (localProps !== undefined) {
            var localPropsSorted = localProps.sort();
            localPropsSorted.forEach(localProp => {
                attributeEntries[localProp.attributeName]={};
                attributeEntries[localProp.attributeName].inherited=false;
                attributeEntries[localProp.attributeName].attributeTypeName=localProp.attributeType.name;
            });
        }

        var attributeNamesUnsorted = Object.keys(attributeEntries);

        if (attributeNamesUnsorted.length > 0) {

            var listnode = document.createElement("ul");
            details.appendChild(listnode);

            var attributeNamesSorted = attributeNamesUnsorted.sort();

            attributeNamesSorted.forEach(attrName => {

                var itemnode = document.createElement("li");
                var attrNameNode = document.createTextNode(attrName);
                if (attributeEntries[attrName].inherited === true) {
                    // display in italics
                    var element = document.createElement("i");
                    itemnode.appendChild(element);
                    element.appendChild(attrNameNode);
                }
                else {
                    itemnode.appendChild(attrNameNode);
                }

                var separatorNode = document.createTextNode(" : ");
                itemnode.appendChild(separatorNode);

                var attrTypeName = attributeEntries[attrName].attributeTypeName;
                if (this.typeManager.getEnumType(attrTypeName) !== undefined) {
                    this.appendEnumLink(itemnode, attrTypeName);
                }
                else {
                    var attrTypeNode = document.createTextNode(attrTypeName);
                    itemnode.appendChild(attrTypeNode);
                }
                listnode.appendChild(itemnode);

            });
        }
        else {
            var nonenode = document.createTextNode(" <none>");
            details.appendChild(nonenode);
        }
    }



    classificationAddEntities(details, typeName) {

        var textnode = document.createTextNode("Valid entity types:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var validEntityEntries = [];

        var cex = this.typeManager.getClassificationType(typeName);

        // Local attributes - there are only local attributes - currently...
        var validEntities = cex.classificationDef.validEntityDefs;
        if (validEntities !== undefined) {
            validEntities.forEach(validEntity => {
                validEntityEntries.push(validEntity.name);
            });
        }


        var validEntityEntriesSorted = validEntityEntries.sort();

         validEntityEntriesSorted.forEach(validEntity => {
             var itemnode = document.createElement("li");
             this.appendEntityLink(itemnode, validEntity);
             listnode.appendChild(itemnode);

         });

    }


    // Enum details are displayed in the detail area with a back button to return to the current focus type, if any
    displayEnumDetails(typeName) {

         var typeCategory = "Enum";
         var details = this.$.detailsDiv;
         details.innerHTML = "";

         if (this.focusType !== undefined) {
             var para = document.createElement("div");
             para.innerHTML = "Return to focus entity type: ";
             details.append(para);
             var button = document.createElement("button");
             button.className = "linkable";
             button.innerHTML = this.focusType;
             button.addEventListener('click', () => this.entityLinkHandler(this.focusType) );
             para.append(button);
         }

         var textnode = document.createTextNode("Enum Type: "+typeName);
         var paranode = document.createElement("p");
         paranode.appendChild(textnode);
         details.appendChild(paranode);

         this.enumAddDescription(details, typeName);
         this.enumAddValues(details, typeName);
    }

    enumAddDescription(details, typeName) {

        var textnode = document.createTextNode("Description:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var eex = this.typeManager.getEnumType(typeName);
        var description = eex.description;
        var textnode = document.createTextNode(description);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

    }


    enumAddValues(details, typeName) {

        var listnode = document.createElement("ul");
        details.appendChild(listnode);
        // Find the enum elements...
        var eex = this.typeManager.getEnumType(typeName);
        var elements = eex.elementDefs;

        elements.forEach(element => {
            var ordinal = element.ordinal;
            var value = element.value;
            var description  = element.description;
            var itemnode = document.createElement("li");
            var element = document.createTextNode(ordinal +" : "+ value +" : "+ description);
            itemnode.appendChild(element);
            listnode.appendChild(itemnode);
        });

    }




    //
    // UTILITY FUNCTIONS
    //


    appendEntityLink(itemnode, itemName) {

        var button = document.createElement("button");
        button.className = "linkable";
        button.innerHTML = "Type: "+itemName;
        itemnode.appendChild(button);
        button.addEventListener('click', () => this.entityLinkHandler(itemName) );

    }


    appendRelationshipLink(itemnode, itemName, inherited) {

        var button = document.createElement("button");
        button.className = "collapsible";
        if (inherited) {
            button.innerHTML="<i>"+itemName+"</i>";
        }
        else {
            button.innerHTML=itemName;
        }

        var div = document.createElement("div");
        div.className = "content";

        this.relationshipAddAttributes(div, itemName);

        var paranode = document.createElement("p");
        div.appendChild(paranode);

        var detailButton = document.createElement("button");
        detailButton.className = "linkable";
        detailButton.innerHTML = "More Details";
        div.appendChild(detailButton);
        detailButton.addEventListener('click', () => this.relationshipLinkHandler(itemName) );

        itemnode.append(button);
        itemnode.append(div);
        button.addEventListener("click", function() {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.display === "block") {
                content.style.display = "none";
            } else {
                content.style.display = "block";
            }
          });
    }


    appendClassificationLink(itemnode, itemName, inherited) {

        var button = document.createElement("button");
        button.className = "collapsible";
        if (inherited) {
            button.innerHTML="<i>"+itemName+"</i>";
        }
        else {
            button.innerHTML=itemName;
        }

        var div = document.createElement("div");
        div.className = "content";

        this.classificationAddAttributes(div, itemName);

        var paranode = document.createElement("p");
        div.appendChild(paranode);

        var detailButton = document.createElement("button");
        detailButton.className = "linkable";
        detailButton.innerHTML = "More Details";
        div.appendChild(detailButton);
        detailButton.addEventListener('click', () => this.classificationLinkHandler(itemName) );

        itemnode.append(button);
        itemnode.append(div);
        button.addEventListener("click", function() {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.display === "block") {
                content.style.display = "none";
            } else {
                content.style.display = "block";
            }
        });
    }

    appendEnumLink(itemnode, itemName) {
        var button = document.createElement("button");
        button.className = "linkable";
        button.innerHTML = itemName;
        itemnode.appendChild(button);
        button.addEventListener('click', () => this.enumLinkHandler(itemName) );
    }


    entityLinkHandler(typeName) {
        // Generate the outbound change focus event...
        this.outEvtChangeFocus(typeName);
    }


   relationshipLinkHandler(typeName) {
        // Generate the outbound change view event...
        this.outEvtChangeView("Relationship",typeName);
   }

   classificationLinkHandler(typeName) {
        // Generate the outbound change view event...
        this.outEvtChangeView("Classification",typeName);
   }

   enumLinkHandler(typeName) {
        // Generate the outbound change view event...
        this.outEvtChangeView("Enum",typeName);
   }




}

window.customElements.define('rex-details-panel', RexDetailsPanel);