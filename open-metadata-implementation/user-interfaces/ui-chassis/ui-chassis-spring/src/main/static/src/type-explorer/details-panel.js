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
* The details panel reacts to events that indicate a change of focus (an entity type's details should be shown)
* or a change of view (a relationship or classification details should be shown).
* Because the details panel also includes links to entity, relationship and classification types (as outlined above)
* the details panel also generates events requesting changeFocus or changeView.
*
* When the UI is first loaded there will be no type information, and until a focus or view type is selected there is
* no particular type to display - therefore following initial load and until a focus/view type is selected, the details
* panel will be blank.
*
*/

class DetailsPanel extends PolymerElement {

    static get template() {
        return html`


            <style include="shared-styles">

                * { font-size: 12px ; font-family: sans-serif; }

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
                    padding: 10px 18px;
                    display: none;
                    overflow: hidden;
                    background-color: #CCCCCC;
                }

                </style>

                <body>

                    <div id="detailsDiv" style="padding:20px;">
                        Type details will be displayed here when a type is selected
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


            // The details panel includes links back tot he current focus type, which will always
            // be an Entity category type. Whenever a focusChanged event is received it remembers
            // new focusType. This creates a cached copy of the focus type name so that the details
            // panel component does not need to keep asking the TypeManager.

            focusType: String


        };
    }




    /*
     * Element is ready
     */
    ready() {
        // Call super.ready() first to initialise node hash...
        super.ready();
        this.focusType = undefined;
    }


    // Inter-component event handlers

    /*
     *  Inbound event: types-loaded
     */
    inEvtTypesLoaded(e) {
       // New type information is available - clear the cached focus type and the details...
       this.focusType = undefined;
       this.clearDetails();
    }

    /*
     *  Inbound event: focus-changed
     */
    inEvtFocusChanged(focusType) {
         this.focusType = focusType;
         this.clearDetails();
         this.displayEntityDetails(focusType);
    }

    /*
     *  Inbound event: view-changed
     */
    inEvtViewChanged(category,viewType) {
        this.clearDetails();

        if (category === "Relationship") {
            this.displayRelationshipDetails(viewType);
        }
        else if (category === "Classification") {
            this.displayClassificationDetails(viewType);
        }
        else {
            this.displayEnumDetails(viewType);
        }
    }

     /*
      *  Outbound event: change-focus
      */
     outEvtChangeFocus(typeName) {
         var customEvent = new CustomEvent('change-focus', { bubbles: true, composed: true, detail: {source: "details-panel", focusType: typeName} });
         this.dispatchEvent(customEvent);
     }

     /*
      *  Outbound event: change-view
      */
     outEvtChangeView(category, typeName) {
         var customEvent = new CustomEvent('change-view', { bubbles: true, composed: true, detail: {source: "details-panel", viewCategory: category, viewType: typeName} });
         this.dispatchEvent(customEvent);
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


    displayEntityDetails(typeName) {

        var typeCategory = "Entity";
        var details = this.$.detailsDiv;
        details.innerHTML = "";

        // Add type header
        
        var boldnode = document.createElement('strong');
        var textnode = document.createTextNode(typeCategory+" type: "+typeName);
        boldnode.appendChild(textnode);
        var paranode = document.createElement("p");
        paranode.appendChild(boldnode);
        details.appendChild(paranode);


        this.entityAddDescription(details, typeName);
        this.entityAddAttributes(details, typeName);
        this.entityAddRelationships(details, typeName);
        this.entityAddClassifications(details, typeName);
    }



    entityAddDescription(details, typeName) {

        var eex = this.typeManager.getEntity(typeName);
        var description = eex.entityDef.description;
        var textnode = document.createTextNode(description);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
    }


     entityAddAttributes(details, typeName) {

        var textnode = document.createTextNode("Attributes:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);
        var eex = this.typeManager.getEntity(typeName);
        var attributeEntries = {};

        // Inherited attributes
        var inheritedProps = eex.inheritedAttributes;
        if (inheritedProps !== undefined) {
            var inheritedPropsSorted = inheritedProps.sort();
            inheritedPropsSorted.forEach(inheritedProp => {
                attributeEntries[inheritedProp.attributeName] = {};
                attributeEntries[inheritedProp.attributeName].inherited = true;
                attributeEntries[inheritedProp.attributeName].attributeTypeName = inheritedProp.attributeType.name;
            });
        }

        // Local attributes
        var localProps = eex.entityDef.propertiesDefinition;
        if (localProps !== undefined) {
            var localPropsSorted = localProps.sort();
            localPropsSorted.forEach(localProp => {
                attributeEntries[localProp.attributeName] = {};
                attributeEntries[localProp.attributeName].inherited = false;
                attributeEntries[localProp.attributeName].attributeTypeName = localProp.attributeType.name;
            });
        }

        var attributeNamesUnsorted = Object.keys(attributeEntries);
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

            // Look up the attributeTypeName in the enums map - if it is found display as a link, otherwise just the primitive type name
            var attrTypeName = attributeEntries[attrName].attributeTypeName;
            if (this.typeManager.getEnum(attrTypeName) !== undefined) {
                this.appendEnumLink(itemnode, attrTypeName);
            }
            else {
                var attrTypeNode = document.createTextNode(attrTypeName);
                itemnode.appendChild(attrTypeNode);
            }
            listnode.appendChild(itemnode);
        });

    }


    entityAddRelationships(details, typeName) {

        var textnode = document.createTextNode("Relationships:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var eex = this.typeManager.getEntity(typeName);

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



     entityAddClassifications(details, typeName) {

        var textnode = document.createTextNode("Classifications:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        var eex = this.typeManager.getEntity(typeName);

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


    /*
     * The user may have already selected a focus type - or not.
     * If so, include a back link to the focus type; if not then just present the relationship details (no back link)
     *
     */
    displayRelationshipDetails(typeName) {

        var typeCategory = "Relationship";
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


        // Add type header

        var boldnode = document.createElement('strong');
        var textnode = document.createTextNode(typeCategory+" type: "+typeName);
        boldnode.appendChild(textnode);
        var paranode = document.createElement("p");
        paranode.appendChild(boldnode);
        details.appendChild(paranode);

        this.relationshipAddDescription(details, typeName);
        this.relationshipAddAttributes(details, typeName);
        this.relationshipAddEntities(details, typeName);


    }


    relationshipAddDescription(details, typeName) {

        var rex = this.typeManager.getRelationship(typeName);
        var description = rex.relationshipDef.description;
        var textnode = document.createTextNode(description);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

    }



    relationshipAddAttributes(details, typeName) {

        var textnode = document.createTextNode("Attributes:");
        details.appendChild(textnode);

        var rex = this.typeManager.getRelationship(typeName);

        var attributeEntries = {};

        // There are only local attributes

        // Local attributes
        var localProps = rex.relationshipDef.propertiesDefinition;
        if (localProps !== undefined) {
            var localPropsSorted = localProps.sort();
            localPropsSorted.forEach(localProp => {
                attributeEntries[localProp.attributeName] = {};
                attributeEntries[localProp.attributeName].inherited = false;
                attributeEntries[localProp.attributeName].attributeTypeName = localProp.attributeType.name;
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
                if (this.typeManager.getEnum(attrTypeName) !== undefined) {
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



    relationshipAddEntities(details, typeName) {

        var rex = this.typeManager.getRelationship(typeName);
        var textnode = document.createTextNode("Entity @ end1:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);


        // Display the type for end1
        var end1 = rex.relationshipDef.endDef1;
        if (end1 !== undefined) {
            // Type name
            var entityTypeName = end1.entityType.name;
            var typenode = document.createElement("li");
            this.appendEntityLink(typenode, entityTypeName);
            listnode.appendChild(typenode);

            // Display the cardinality for end1
            var attributeCardinality = end1.attributeCardinality;
            var cardnode = document.createElement("li");
            var cardtext = document.createTextNode("Cardinality: "+attributeCardinality);
            cardnode.appendChild(cardtext);
            listnode.appendChild(cardnode);

            // Display the attribute name for end1
             var attributeName = end1.attributeName;
             var namenode = document.createElement("li");
             var nametext = document.createTextNode("Attribute Name: "+attributeName);
             namenode.appendChild(nametext);
             listnode.appendChild(namenode);
        }

        var textnode = document.createTextNode("Entity @ end2:");
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);
        var listnode = document.createElement("ul");
        details.appendChild(listnode);

        // Display the type for end2
        var end2 = rex.relationshipDef.endDef2;
        if (end2 !== undefined) {
            // Type name
            var entityTypeName = end2.entityType.name;
            var typenode = document.createElement("li");
            this.appendEntityLink(typenode, entityTypeName);
            listnode.appendChild(typenode);

            // Display the cardinality for end2
            var attributeCardinality = end2.attributeCardinality;
            var cardnode = document.createElement("li");
            var cardtext = document.createTextNode("Cardinality: "+attributeCardinality);
            cardnode.appendChild(cardtext);
            listnode.appendChild(cardnode);

            // Display the attribute name for end2
            var attributeName = end2.attributeName;
            var namenode = document.createElement("li");
            var nametext = document.createTextNode("Attribute Name: "+attributeName);
            namenode.appendChild(nametext);
            listnode.appendChild(namenode);
        }

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


        var boldnode = document.createElement('strong');
        var textnode = document.createTextNode(typeCategory+" type: "+typeName);
        boldnode.appendChild(textnode);
        var paranode = document.createElement("p");
        paranode.appendChild(boldnode);
        details.appendChild(paranode);


        this.classificationAddDescription(details, typeName);
        this.classificationAddAttributes(details, typeName);
        this.classificationAddEntities(details, typeName);

    }


    classificationAddDescription(details, typeName) {

        var cex = this.typeManager.getClassification(typeName);
        var description = cex.classificationDef.description;
        var textnode = document.createTextNode(description);
        var paranode = document.createElement("p");
        paranode.appendChild(textnode);
        details.appendChild(paranode);

    }

     classificationAddAttributes(details, typeName) {

        var textnode = document.createTextNode("Attributes:");
        details.appendChild(textnode);


        var cex = this.typeManager.getClassification(typeName);

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
                if (this.typeManager.getEnum(attrTypeName) !== undefined) {
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

        var cex = this.typeManager.getClassification(typeName);

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
        var eex = this.typeManager.getEnum(typeName);
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
        var eex = this.typeManager.getEnum(typeName);
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



}

window.customElements.define('details-panel', DetailsPanel);