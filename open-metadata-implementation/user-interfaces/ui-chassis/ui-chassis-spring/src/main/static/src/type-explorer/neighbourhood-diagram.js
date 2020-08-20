/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import "../../node_modules/@polymer/paper-checkbox/paper-checkbox.js";
import "../../node_modules/@polymer/paper-button/paper-button.js";
import "../../node_modules/@polymer/paper-input/paper-input.js";
import '../shared-styles.js';
import '../token-ajax.js';

/**
 *
 * NeighbourhoodDiagram implements a web component for drawing an Entity neighbourhood diagram
 *
 * This component renders a radial plot with the focus entity type at the centre and the
 * available relationship types radiating from it, with the neighbour entity types at the
 * outer ends of the relationship types. The focus type is highlighted.
 *
 * It is possible to click on a relationship or entity type to 'select' it - if the type is
 * an entity type it becomes the new focus; if it is a relationship type it becomes the new
 * view type (and will be displayed in the details panel).
 *
 * If the user selects any of the neighbouring entity types it causes that type to become
 * the new focus, which puts the newly selected entity type at the center and shows its details
 * in the details panel.
 *
 * If the user selects any of the relationship types, either in the diagram or in
 * the entity's details, this changes the view type to the selected relationship type. If the user
 * subsequently clicks on another entity type from the relationship's details panel, that new
 * entity type becomes the selected focus type and both the diagram and the details panel are updated.
 * Similarly, selecting an entity type from within a classification type displayed in the
 * details panel, the entity type will become the focus.
 *
 *
 * A 'neighbourhood' object looks like this:
 *
 * {
 *   "root": "alpha",
 *   "nodes" : [
 *       { "name" : "beta" } ,
 *       { "name" : "gamma" }
 *   ]
 *   "links": [
 *       {  "source" : "alpha" , "target" : "beta" , "name" : "alphabeta" } ,
 *       {  "source" : "alpha" , "target" : "gamma" , "name" : "alphagamma" }
 *   ]
 * }
 *
 * The code will add x and y coordinates to center the named node and arrange the
 * others around it radially.
 *
 */



class NeighbourhoodDiagram extends PolymerElement {

    static get template() {
        return html`

            <style include="shared-styles">

                * { font-size: 12px ; font-family: sans-serif; }

            </style>

            <body>
                <div id="nhb"  style="background-color:#FFFFFF">
                     <p>
                     Placeholder for neighbourhood diagram...
                     </p>
                </div>
            </body>

        `;
    }


    static get properties() {
        return {

            // Reference to TypeManager element which this ConnectionManager depends on.
            // The TypeManager is created in the DOM of the parent and is passed in
            // once we are all initialised. This avoids any direct dependency from ConnectionManager
            // on TypeManager.

            typeManager : Object,

            focusType : {
                type : String,
                value : "",
                notify : true,
                reflectToAttribute : true
            },

            hood : Object,

            margin :  {
                type : Object,
                value : () => { return {top: 0, right: 0, bottom: 0, left: 0}; },
                notify : true,
                reflectToAttribute : true
            }

        };
    }




    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();
    }


    setTypeManager(typeManager) {
        this.typeManager = typeManager;
    }


    // Input events


    /*
     * This is the only event of interest to the neighbourhood diagram.
     * If the focus changes the diagram needs to be re-drawn with the new focus at the centre
     */
    inEvtFocusChanged(focusType) {
        this.focusType = focusType;
        this.render(focusType);
    }


    /*
     * This method initialises and renders the diagram
     * Unlike the inheritance diagram, this diagram relies on a focus type having been selected.
     * It is therefore passed on the render function.
     */
    render(focusType) {

        // Check focusType is set...
        if (focusType === undefined || focusType === "") {
            alert("Please select an entity type so it's neighbourhood can be displayed");
            return;
        }

        this.initialiseNeighbourhoodDiagram();

        // Record the focus type passed by the diagram manager - in all cases the focus is already
        // set when this diagram is created.
        this.focusType = focusType;

        var diagram = this.$.nhb;

        this.hood = this.createNeighbourhood(focusType);

        this.renderNeighbourhoodDiagram(diagram);

    }

    /*
     * This method clears any introductory text or previous rendering of the diagram
     */
    initialiseNeighbourhoodDiagram() {

        d3.select('#nhb').selectAll("svg").remove();

        // Clear the introductory text...
        this.$.nhb.innerHTML = "";

        this.hood = {};
        this.margin = {top: 0, right: 0, bottom: 0, left: 0};

    }




   // Convert type explorer information into an entity type neighbourhood

    /*
     * The neighbourhood object needs to be formatted as follows:
     *
     * {
     *   "root" :
     *   {
     *       "category" : "Entity";
     *       "name"     : "typeName" ,
     *       "data"     : eex
     *   }
     *
     *   "nodes" : [
     *       {
     *           "category" : "Entity" | "Relationship" ,
     *           "name"     : "typeName" ,
     *           "data"     : eex
     *       }
     *   ]
     *
     *   "links": [
     *       {  "source" : "alpha" , "target" : "beta" , "role" : "roleName" } ,
     *       {  "source" : "alpha" , "target" : "gamma" , "role" : "roleName" }
     *   ]
     * }
     */

    createNeighbourhood(typeName) {

        var eex = this.typeManager.getEntity(typeName);

        var root = {};
        root.name = typeName;
        root.data = eex;
        root.category = "Entity";

        var n = {};
        n.root = root;
        n.nodes = [];
        n.links = [];

        // Form a list of all relationships in alpha order
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

            var rex = this.typeManager.getRelationship(relName);
            var end1 = rex.relationshipDef.endDef1;
            var end2 = rex.relationshipDef.endDef2;
            var end1TypeName = end1.entityType.name;
            var end2TypeName = end2.entityType.name;

            var closerEnd;
            var remoteEntity;
            var remoteTypeName;
            var roleRemote;
            var roleRoot;
            var cardRemote;
            var cardRoot;
            var numRemote;
            var numRoot;


            // Match the currently selected type to one end of the relationship type.

            // Test whether this type is a type of end1 - and get the distance
            var dist1 = this.entityIsTypeOf(typeName,end1TypeName);

            // Test whether this type is a type of end2 - and get the distance
            var dist2 = this.entityIsTypeOf(typeName,end2TypeName);

            // Choose the shorter valid distance - for max. entropy

            // Dispense with the obvious error case first:
            if (dist1 < 0 && dist2 < 0) {
                alert("ERROR: neither end of relationship type "+relName+" is suitable for type "+typeName);
                // Return (from this loop function) without adding the relationship
                return;
            }

            // We know that at least one of dist1, dist2 is >=0, and should select the lower of the two.
            // If they are equal then can arbitrarily select end1.
            //
            // Simplest tests are:
            //   if dist1 < 0     - it must be end2
            //   if dist2 >= 0    - if dist1 > dist2 it must be end2
            //   else             - it must be end1
            //
            // Also set end roles


            if (dist1 < 0 || (dist2 >= 0 && dist1 > dist2) ) {
                closerEnd = end2TypeName;
                remoteTypeName = end1TypeName;
                roleRemote = end1.attributeName;
                roleRoot = end2.attributeName;
                cardRemote = this.convertCardinalityToSymbol(end1.attributeCardinality);
                cardRoot = this.convertCardinalityToSymbol(end2.attributeCardinality);
                numRemote = "1";
                numRoot = "2";
            }

            else {
                closerEnd = end1TypeName;
                remoteTypeName = end2TypeName;
                roleRemote = end2.attributeName;
                roleRoot = end1.attributeName;
                cardRemote = this.convertCardinalityToSymbol(end2.attributeCardinality);
                cardRoot   = this.convertCardinalityToSymbol(end1.attributeCardinality);
                numRemote = "2";
                numRoot = "1";
            }

            // Set up nodes
            var remoteEntityX = this.typeManager.getEntity(remoteTypeName);

            var relNode = {};
            relNode.name = relName;
            relNode.data = rex;
            relNode.category = "Relationship";
            relNode.numRoot = numRoot;
            relNode.inherited = relationshipEntries[relName].inherited;
            n.nodes.push(relNode);

            var remoteNode = {};
            remoteNode.name = remoteTypeName;
            remoteNode.data = remoteEntityX;
            remoteNode.category = "Entity";
            n.nodes.push(remoteNode);

            var innerLink = {};
            innerLink.source = n.root;
            innerLink.target = relNode;
            innerLink.name = "("+cardRoot+")"+":"+roleRoot;
            n.links.push(innerLink);

            var outerLink = {};
            outerLink.source = relNode;
            outerLink.target = remoteNode;
            outerLink.name = "("+cardRemote+")"+":"+roleRemote;
            n.links.push(outerLink);


        });

        return n;

    }

    /*
     * Method to convert Egeria cardinality into a displayable symbol
     */
    convertCardinalityToSymbol(card) {
        var symbol;
        switch (card) {
            case "AT_MOST_ONE" :
                symbol = "1";
                break;
            case "ANY_NUMBER" :
                symbol = "*";
                break;
        }
        return symbol;
    }


    /*
     * Is this entity type a [sub-]type of another entity type? i.e. is it the same or a subtype?
     * Parameters are type names.
     * Returns the distance within the inheritance tree or -1 if not a match (type/subtype)
     */
     entityIsTypeOf(thisType,thatType) {

         if (thisType === thatType) {
             return 0;
         }

         var distance = 0;
         var thisX = this.typeManager.getEntity(thisType);
         var superType = thisX.entityDef.superType;
         while (superType !== undefined) {
             distance = distance+1;
             var superTypeName = superType.name;
             if (superTypeName === thatType) {
                 return distance;
             }
             var superTypeX = this.typeManager.getEntity(superTypeName);
             superType = superTypeX.entityDef.superType;
         }
         // If you get to here there has not been a match...
         return -1;
     }





    /*
     * This method examines the current hood and displays it in the 'diagram' element
     */
    renderNeighbourhoodDiagram(diagram) {

        var margin = this.margin;

        // Although we know the offset width and height, the diagram is approximately circular
        // so set the effective height to the width.
        var drg_width  = diagram.offsetWidth;
        var drg_height = diagram.offsetHeight;
        var width = drg_width;
        var height = width;

        this.hood.svg = d3.select(this.shadowRoot.querySelector('#nhb'))
                     .append("svg")
                     .attr("width", width)
                     .attr("height", height)
                     .attr("viewBox", [-margin.left, -margin.top, width, height])
                     .style("font", "12px sans-serif")
                     .style("user-select", "none");

        this.hood.gLink = this.hood.svg
                             .append("g")
                             .attr("fill", "none")
                             .attr("stroke", "#CCC")
                             .attr("stroke-opacity", 0.4)
                             .attr("stroke-width", 1.5);

        this.hood.gNode = this.hood.svg
                             .append("g")
                             .attr("cursor", "pointer");

        this.nhbdUpdate(diagram);

    }




    /*
     * Update a subtree within the diagram
     * 'subtree' is the node at the root odf the subtree being updated
     */
    nhbdUpdate(diagram) {

        var margin = this.margin;
        var duration = d3.event && d3.event.altKey ? 2500 : 250;

        // Although we know the offset width and height, the diagram is approximately circular
        // so set the effective height to the width.
        var drg_width  = diagram.offsetWidth;
        var drg_height = diagram.offsetHeight;
        var width = drg_width;
        var height = width;

        var root = this.hood.root;
        var rootTypeName = root.name;

        // Compute the new layout.

        this.hood.root.x = width / 2;   //!
        this.hood.root.y = height / 2;  //!


        var neighbours = this.hood.nodes;
        if (neighbours !== undefined) {

            var radius = 0.7 * width / 2;

            var center = {};
            center.x = width / 2;
            center.y = height / 2;

            var numNodes = neighbours.length;
            var numRels = numNodes / 2;
            var angle = Math.PI * 2 / numRels;
            var ang_offset = angle / 2;

            // Place the relationships first.
            var index = 0;
            neighbours.forEach((nhbr) => {
                if (nhbr.category === "Relationship") {
                    var ang = index * angle - Math.PI / 2 + ang_offset;
                    nhbr.x = radius / 2 * Math.cos(ang) + width/2;
                    nhbr.y = radius / 2 * Math.sin(ang) + height/2;
                    index = index+1;
                }
            })

            // Place the remote entities relative to the relationships. Note that this relies
            // on consistent ordering between the two lists.

            index = 0;
            neighbours.forEach((nhbr) => {
                if (nhbr.category === "Entity") {
                    var ang = index * angle - Math.PI / 2 + ang_offset;;
                    nhbr.x = radius * Math.cos(ang) + width/2;
                    nhbr.y = radius * Math.sin(ang) + height/2;
                    index = index+1;
                }
            })
        }
        else {
            // Neighbourhood root has no neighbours... odd but may as well give up now
            return;
        }



        // Get the lists of nodes and links
        var nodes = [];
        if (this.hood.nodes !== undefined) {
            nodes = this.hood.nodes;
        }
        else {
            // Neighbourhood root has no nodes... odd but may as well give up now
            return;
        }
        nodes.push(this.hood.root);
        var links = this.hood.links;

        const transition = this.hood.svg
                                   .transition()
                                   .duration(duration)
                                   .attr("height", height)
                                   .attr("viewBox", [-margin.left, -margin.top, width, height])
                                   .tween("resize", window.ResizeObserver ? null : () => () => this.hood.svg.dispatch("toggle"));


        /*
         * Update the nodes…
         */

        const node = this.hood.gNode
                         .selectAll("g")
                         .data(nodes, d => d.name);

        // Enter new nodes at the root's current position.
        const nodeEnter = node.enter()
                              .append("g")
                              .attr("transform", d => `translate(${root.x},${root.y})`)
                              .attr("fill-opacity", 0)
                              .attr("stroke-opacity", 0);



        /*
         * Each node is rendered as a circle plus text label
         */


        /*
         * node text consists of clickable text rendered on top of a shadow to provide contrast with links
         */

        nodeEnter.append("text")
                 .attr("dy", "0.31em")
                 .attr("x", d => (d.category === "Relationship" ? this.locateRelationshipLabelX(d, width, height) : this.locateEntityLabelX(d, width, height)))
                 .attr("y", d => (d.category === "Relationship" ? this.locateRelationshipLabelY(d, width, height) : this.locateEntityLabelY(d, width, height)))
                 .attr("text-anchor", d => this.locateLabelAnchor(d,width,height))
                 .style("font-style", d => d.inherited ? "italic" : "normal")
                 .text(d => d.name)
                 .on("click", d => { this.nodeSelected(d.category, d.name); })
                 .clone(true)
                 .lower()
                 .attr("stroke-linejoin", "round")
                 .attr("stroke-width", 3)
                 .attr("stroke", "white");


        nodeEnter.append("circle")
                 .lower()
                 .attr("r", 6)
                 .attr("stroke-opacity", d => (d.category === "Entity" ? 1 : 0))
                 .attr("stroke-width",1)
                 .attr("stroke", "#000")
                 .attr("fill", d => (d.category === "Relationship" ? "none" : "#FFF"))
                 .on("click", d => { this.nodeSelected(d.category, d.name); });


        nodeEnter.append("polyline")
                 .lower()
                 .attr("points","-4 -8, 0 8, 4 -8")
                 .attr("stroke-width",1)
                 .attr("stroke-opacity", d => (d.category === "Relationship" ? 1 : 0))
                 .attr("stroke", "#CCC")
                 .attr("fill", "none")
                 .attr("transform" , d => `rotate(${ ((d.numRoot==="1")?180:0) + ( (d.x===root.x)?(d.y<root.y?0:180): 180/Math.PI *  Math.atan( (d.y-root.y) / (d.x-root.x) ) + (d.x>root.x?90:-90) ) },0,0)`)


        // Transition nodes to their new position.
        const nodeUpdate = node.merge(nodeEnter)
                               .transition(transition)
                               .attr("transform", d => `translate(${d.x},${d.y})`)
                               .attr("fill-opacity", 1)
                               .attr("stroke-opacity", 1);



        // Highlight a selected node, if a type has been selected and selectedCategory is Entity
        nodeUpdate.selectAll('text')
                  .attr("fill", d => this.nhbHighlight(d) ? "blue" : "black" );


        // Transition exiting nodes to the root's position.
        const nodeExit = node.exit()
                             .transition(transition).remove()
                             .attr("transform", d => `translate(${root.x},${root.y})`)
                             .attr("fill-opacity", 0)
                             .attr("stroke-opacity", 0);



        /*
         * Update the links…
         */

        const link = this.hood.gLink
                              .selectAll("g")
                              .data(links, d => d.name);

        const linkEnter = link.enter()
                              .append("g");

        /*
         * link text consists of non-clickable text rendered on top of a shadow to provide contrast with links
         */
        linkEnter.append("text")
                 .attr("fill", "#888")
                 .attr("stroke", "none")
                 .attr("stroke-width", 0)
                 .attr("dy", "0.31em")
                 .attr("x", d => (d.source.x+9*d.target.x)/10)
                 .attr("y", d => (d.source.y+9*d.target.y)/10)
                 .attr("text-anchor", d => ( (d.target.x >= width/2) ? "end" : "start" ) )
                 .attr("transform" , d => `rotate(${180/Math.PI * Math.atan((d.target.y-d.source.y)/(d.target.x-d.source.x))},${(d.source.x+9*d.target.x)/10},${(d.source.y+9*d.target.y)/10})`)
                 .text(d => d.name)
                 .clone(true)
                 .lower()
                 .attr("stroke-linejoin", "round")
                 .attr("stroke-width", 3)
                 .attr("stroke", "white");

        linkEnter.append("path")
                 .attr("fill-opacity", 1)
                 .attr("stroke-opacity", 1)
                 .attr("d", d => {
                     const src = {x: d.source.x, y: d.source.y};
                     const tgt = {x: d.target.x, y: d.target.y};
                     return this.straightPath( {source : src , target : tgt } );
                 })
                 .lower();


        // Transition links to their new position.
        const linkUpdate = link.merge(linkEnter)
                               .transition(transition)
                               .attr("fill-opacity", 1)
                               .attr("stroke-opacity", 1);

        // Transition exiting nodes to the root's position.
        link.exit()
            .transition(transition)
            .remove()
            .attr('d', function (d) {
                const o = {x: root.x, y: root.y};
                return this.straightPath( {source : o , target : o } );
            });

    }




    /*
     * Draw a straight path from source node to target node
     */
    straightPath ({source, target}) {
        return 'M' + source.x + ',' + source.y
             + 'L' + target.x + ',' + target.y;
    }




    /*
     * Indicate whether a node should be highlighted - only the focus is highlighted so applies to Entity types only
     */
    nhbHighlight(d) {
        // Check whether node is selected as focus
        if (this.focusType === d.name) {
            return true;
        }
        return false;
    }












    // UTILITY FUNCTIONS


    locateRelationshipLabelX(d, width, height) {
        var x;
        x = (d.x-width/2)*.05 + (0.2*Math.abs(d.y - height/2) > Math.abs(d.x - width/2) ? (width/2 - d.x)*.5 : 0);
        return x;
    }

    locateEntityLabelX(d, width, height) {
        var x;
        x = (d.x-width/2)*.05;
        return x;
    }

    locateRelationshipLabelY(d, width, height) {
        var y;
        y = (d.y-height/2)*.15 + (0.15*Math.abs(d.y - height/2) > Math.abs(d.x - width/2) ? (d.y-height/2)*.08 : 0);
        return y;
    }


    locateEntityLabelY(d, width, height) {
        var y;
        y = (d.y-height/2)*.05;
        return y;
    }


    locateLabelAnchor(d,width,height) {
        return (0.05*Math.abs(d.y - height/2) > Math.abs(d.x - width/2) ? "middle" : ((d.x < width/2) ? "end" : ((d.x > width/2) ? "start" : "middle")) );
    }



    // Outbound events

    /*
     * Types in the neighbourhood diagram are either entity or relationship types.
     * Selection of an entity type will cause a request focus change to be sent,
     * meanwhile selection of a relationship type will send a change view request
     */

     nodeSelected(cat, typeName) {
         if (cat === "Entity") {
             this.entitySelected(typeName);
         }
         else if (cat === "Relationship") {
             this.relationshipSelected(typeName);
         }
     }

     entitySelected(typeName) {
         this.outEvtChangeFocus(typeName);
     }

     relationshipSelected(typeName) {
         this.outEvtChangeView("Relationship", typeName);
     }


     /*
      *  Outbound event: change-focus
      */
     outEvtChangeFocus(typeName) {
         var customEvent = new CustomEvent('change-focus',
             {   bubbles: true,
                 composed: true,
                 detail: {
                     source: "neighbourhood-diagram",
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
              {   bubbles: true,
                  composed: true,
                  detail: {
                      source: "neighbourhood-diagram",
                      viewCategory: cat,
                      viewType: typeName
                  }
              }
          );
          this.dispatchEvent(customEvent);
      }


}


window.customElements.define('neighbourhood-diagram', NeighbourhoodDiagram);