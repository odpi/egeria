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
* InheritanceDiagram implements a web component for drawing an Entity inheritance diagram
*
* This component renders a set of inheritance trees (drawn with the roots to the left)
* that contain Egeria entity types. Each tree is collapsible and expandabla to hide/
* show the children of a node. It is possible to click on a type to 'select'
* it - meaning that type becomes the focus type. The focus type is highlighted.
*
* This component is based on the D3 collapsible tree by Michael Bostock
*/

class InheritanceDiagram extends PolymerElement {

    static get template() {
        return html`

            <style include="shared-styles">

                * { font-size: 12px ; font-family: sans-serif; }

            </style>

            <body>

                <div id="inh">
                     <p>
                     Placeholder for inheritance diagram...
                     </p>

                </div>
            </body>

        `;
    }


    static get properties() {
        return {

            // Reference to TypeManager element which this diagram depends on.
            // The TypeManager is created in the DOM of the parent and is passed in
            // once we are all initialised. This avoids any direct dependency on
            // TypeManager.

            typeManager: Object,


            renderedTrees: {
                 type: Array,
                 value: () => { return [];},
                 notify : true
            },


            scrolled : {
                type : Boolean,
                value : false,
                notify : true,
                reflectToAttribute : true
            },



            margin   :  {
                 type : Object,
                 value : () => { return {top: 30, right: 30, bottom: 30, left: 30}; },
                 notify : true,
                 reflectToAttribute : true
             },



            focusType : {
                type : String,
                value : "",
                notify : true,
                reflectToAttribute : true
            },

            treedepth :  {
                type : Number,
                value : 0,
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
        console.log("inheritance-diagram ready");   // TODO - remove
    }


    // TODO - not needed if you adopt property push down instead
    setTypeManager(typeManager) {
        console.log("inheritance-diagram: setting typeManager");   // TODO - remove
        this.typeManager = typeManager;
    }

    inEvtFocusChanged(focusType) {
        this.focusType = focusType;
        // TODO - more to do here   - e.g. highlight, scrolling
    }

    render() {
        console.log("render inheritance diagram");   // TODO - remove

        this.initialiseInheritanceDiagram();


        var drawingArea = this.$.inh;
        console.log("drawingArea is set to: "+drawingArea);   // TODO - remove

        this.createInheritanceTrees(drawingArea);


        console.log("render: call updateRoot - for all trees");   // TODO - remove
        this.updateRoot(drawingArea);

         //scrollSelectedIntoView();  -- TODO look at transitionComplete and work out when to initiate scroll
    }




    initialiseInheritanceDiagram() {

        d3.select('#inh').selectAll("svg").remove();

        this.$.inh.innerHTML = "Cleared down - ready for trees....";  // TODO - remove

        // Experimental TODO - clean up
        var svg = d3.select(this.shadowRoot.querySelector('#inh'))
                    .append("svg")
                    .attr("width",100)
                    .attr("height",100);

        //console.log('svg = '+svg+" width is "+svg.width+" top is "+svg.top);

        //var exSvg = d3.select('#inh').selectAll("svg");
        //console.log('exSVg = '+exSvg+" width is "+exSvg.width+" top is "+exSvg.top);
        //
        var circle = svg.append("circle")
                       .attr('id','test-circle')
                       .attr("r", 20)
                       .attr("cx", 50)
                       .attr("cy", 50)
                       .attr("stroke-width",3)
                       .attr("stroke", "#000")
                       .attr("fill", "#F00");

        // Check you have nuked everything    TODO - redundant
        //var elem = document.getElementById("elem"+selectedTypeName);  // TODO - redundant

        this.renderedTrees = [];
        this.scrolled = false;

        console.log("inheritance diagram initialised");  // TODO - remove
    }


    createInheritanceTrees(drawingArea) {

        /*
         * The inheritance diagram shows the type hierarchy of entity types.
         * The user may have already selected a type (of any category) but it is optional
         */



        /*
         * The inheritance tree needs to be formatted as follows:
         *
         * {
         *   "name": "alpha",
         *   "children": [
         *     {
         *       "name": "beta"
         *     },
         *     {
         *       "name": "gamma"
         *     },
         *   ]
         * }
         */

        /*
         * The createInheritanceTree method will read the Tex object and produce the necessary tree
         */


        /*
         * For any entity types that don't have a supertype - create a tree
         * Those with supertypes will be included in one of the trees.
         * Tree roots are processed in alpha order
         */

        var entities = this.typeManager.getEntities();
        var entityTypesUnsorted = Object.keys(entities);
        var entityTypesSorted = entityTypesUnsorted.sort();
        entityTypesSorted.forEach(entityExpl => {
            if (entities[entityExpl].entityDef.superType == null) {
                var typeName = entities[entityExpl].entityDef.name;
                this.treeDepth = 0;
                var tree = this.createInheritanceTree(typeName);
                this.renderInheritanceDiagram(tree, drawingArea, typeName, this.treeDepth);
            }
        })
    }


    createInheritanceTree(typeName) {

        console.log("create inheritance tree for type :"+typeName);  // TODO - remove
        var inheritanceTree = {};
        // Start at the type with typeName and follow subtype links to compose children
        var node = this.typeManager.getEntity(typeName);
        var childNames = node.subTypeNames;
        inheritanceTree = this.addSubTree(inheritanceTree, typeName, childNames, 1)
        return inheritanceTree;
    }


    addSubTree(tree, name, childNames, nodeDepth) {

        // Add the current node then recurse for the children
        var level = nodeDepth;
        if (nodeDepth > this.treeDepth) {
            this.treeDepth = nodeDepth;
        }
        tree.name = name;
        tree.category = "Entity";
        if (childNames !== null && childNames.length > 0) {
            tree.children = [];
            var childNamesSorted = childNames.sort();
            childNamesSorted.forEach( childName => {
                var node = this.typeManager.getEntity(childName);
                var nodeChildNames = node.subTypeNames;
                var subtree = {};
                this.addSubTree(subtree, childName, nodeChildNames, level + 1);
                tree.children.push(subtree);
            });
        }
        return tree;
    }


    renderInheritanceDiagram(typeHierarchy, drawingArea, typeName, treeDepth) {

        var width = drawingArea.offsetWidth;
        console.log("renderInheritanceDiagram: for type :"+typeName+" width is "+width);   // TODO - remove


        var margin = this.margin;

        var thisTree     = {};
        thisTree.dx      = 30;
        thisTree.dy      = width / treeDepth;
        thisTree.root    = d3.hierarchy(typeHierarchy);
        thisTree.root.x0 = thisTree.dy / 2;
        thisTree.root.y0 = 0;
        thisTree.root.descendants().forEach((d, i) => {
            d.id = i;
            d._children = d.children;
            // For nodes to be initially in collapsed state, set d.children = null;
        });

        thisTree.svg = d3.select(this.shadowRoot.querySelector('#inh'))
                         .append("svg")
                         .attr("width", width)
                         .attr("height", thisTree.dx)
                         .attr("viewBox", [-margin.left, -margin.top, width, thisTree.dx])
                         .style("font", "12px sans-serif")
                         .style("user-select", "none");

        console.log("renderInheritanceDiagram: svg is: "+thisTree.svg);   // TODO - remove

        thisTree.gLink = thisTree.svg
                                 .append("g")
                                 .attr("fill", "none")
                                 .attr("stroke", "#888")
                                 .attr("stroke-opacity", 0.4)
                                 .attr("stroke-width", 1.5);

        thisTree.gNode = thisTree.svg
                                 .append("g")
                                 .attr("cursor", "pointer");

        this.renderedTrees[typeName] = thisTree;

    }


    /*
     * Called when this diagram type is selected - it will refresh all trees in the drawing area.
     */
    updateRoot(drawingArea) {

        var treeNames = Object.keys(this.renderedTrees);

        treeNames.forEach(treeName => {
            console.log("updateRoot for type: "+treeName);   // TODO - remove
            var thisTree = this.renderedTrees[treeName];
            var root = thisTree.root;
            if (root !== undefined) {
                this.update(drawingArea, thisTree, root);
            }
        });

    }


    /*
     * Draw a curved path from parent node to child node
     */
    curvedPath ({source, target}) {
        return 'M' + source.y + ',' + source.x
             + 'C' + (source.y + target.y)/2 + ',' + source.x
             + ' ' + (source.y + target.y)/2 + ',' + target.x
             + ' ' + target.y + ',' + target.x
    }



    /*
     * Update a subtree within the diagram
     * 'subtree' is the node at the root odf the subtree being updated
     */
    update(drawingArea, tree, subtree) {


        var width = drawingArea.offsetWidth;
        var thisTree = tree;
        var sourceTypeName = subtree.data.name;

        console.log("perform update for tree: "+sourceTypeName);   // TODO - remove

        var margin = this.margin;
        var duration = d3.event && d3.event.altKey ? 2500 : 250;

        // Compute the new tree layout.
        var treeLayout = d3.tree();
        treeLayout.nodeSize([thisTree.dx, thisTree.dy])
        treeLayout(thisTree.root);


        var root = thisTree.root;

        // Get the lists of nodes and links
        var nodes = root.descendants();
        var links = root.links();


        // debug   // TODO - remove
        nodes.forEach(node => {
            console.log("node has name: "+node.data.name);
        });

        // Calculate the overall height of the tree (across all the nodes - the tree is drawn horizontally)
        var left  = root;
        var right = root;
        root.eachBefore(node => {
            if (node.x < left.x) left = node;
            if (node.x > right.x) right = node;
        });
        var height = right.x - left.x + margin.top + margin.bottom;

        console.log("height is "+height);  // TODO - remove

        const transition = thisTree.svg
                                   .transition()
                                   .duration(duration)
                                   .attr("height", height)
                                   .attr("viewBox", [-margin.left, left.x - margin.top, width, height])
                                   .tween("resize", window.ResizeObserver ? null : () => () => thisTree.svg.dispatch("toggle"))
                                   .on('end',  () => this.transitionComplete() );



        /*
         * Update the nodes…
         */

        console.log("update the nodes");  // TODO - remove

        const node = thisTree.gNode
                             .selectAll("g")
                             .data(nodes, d => d.id);

        // Enter new nodes at the subtree root's prior position.
        const nodeEnter = node.enter()
                              .append("g")
                              .attr("transform", d => `translate(${subtree.y0},${subtree.x0})`)
                              .attr("fill-opacity", 0)
                              .attr("stroke-opacity", 0)
                              ;

        /*
         * Each node is rendered as a circle with decoration for expand/collapse, plus text label
         */
         nodeEnter.append("circle")
                  .attr('id',d => "elem"+d.data.name)
                  .attr("r", 6)
                  .attr("stroke-width",1)
                  .attr("stroke", "#000")
                  .attr("fill", "#FFF")
                  .on("click", d => {
                       d.children = d.children ? null : d._children;
                       this.update(drawingArea,tree,d);
                  });

         nodeEnter.append("line")
                  .attr("x1", 0).attr("y1", -2).attr("x2", 0).attr("y2", 2)
                  .attr("stroke-width",1)
                  .attr("stroke-linecap","round")
                  .attr("stroke", d => d._children && !d.children ? "#000" : "#FFF")
                  .attr("pointer-events","none");

         nodeEnter.append("line")
                  .attr("x1", -2).attr("y1", 0).attr("x2", 2).attr("y2", 0)
                  .attr("stroke-width",1)
                  .attr("stroke-linecap","round")
                  .attr("stroke", d => d._children ? "#000" : "#FFF")
                  .attr("pointer-events","none");

         /*
          * node text consists of clickable text rendered on top of a shadow to provide contrast with links
          */
         nodeEnter.append("text")
                  .attr("dy", "0.31em")
                  .attr("x", 12)
                  .attr("text-anchor", "start")
                  .text(d => d.data.name)
                  .on("click", d => { this.typeSelected("Entity", d.data.name); })
                  .clone(true)
                  .lower()
                  .attr("stroke-linejoin", "round")
                  .attr("stroke-width", 3)
                  .attr("stroke", "white");

         // Transition nodes to their new position.
         const nodeUpdate = node.merge(nodeEnter).transition(transition)
                                .attr("transform", d => `translate(${d.y},${d.x})`)
                                .attr("fill-opacity", 1)
                                .attr("stroke-opacity", 1);

         // Toggle minus to plus depending on collapsed/expanded state...
         nodeUpdate.select('line')
                   .attr("x1", 0).attr("y1", -2).attr("x2", 0).attr("y2", 2)
                   .attr("stroke", d => d._children && !d.children ? "#000" : "#FFF");

         // Highlight a selected node, if a type has been selected and selectedCategory is Entity
         nodeUpdate.selectAll('text')
                   .attr("fill", d => this.inhHighlight(d) ? "blue" : "black" );


         // Transition exiting nodes to the parent's new position.
         const nodeExit = node.exit().transition(transition).remove()
                                     .attr("transform", d => `translate(${subtree.y},${subtree.x})`)
                                     .attr("fill-opacity", 0)
                                     .attr("stroke-opacity", 0);



         /*
          * Update the links…
          */

         console.log("update the links");  // TODO - remove

         const link = thisTree.gLink
                              .selectAll("path")
                              .data(links, d => d.target.id);

         // Enter any new links at the parent's prior position.
         const linkEnter = link.enter()
                               .append("path")
                               .attr("d", d => {
                                    const o = {x: subtree.x0, y: subtree.y0};
                                    var curve = this.curvedPath( {source : o , target : o } );
                                    return curve;
                               });

          console.log("links entered");

         // Transition links to their new position.
         link.merge(linkEnter)
             .transition(transition)
             .attr("d", d => {
                  const s = {x: d.source.x, y: d.source.y};
                  const t = {x: d.target.x, y: d.target.y};
                  var curve = this.curvedPath( {source : s , target : t } );
                  return curve;
             });

         console.log("links merged");


         // Transition exiting nodes to the parent's new position.
         link.exit()
             .transition(transition)
             .remove()
             .attr('d', function (d) {
                  const o = {x: subtree.x, y: subtree.y};
                  var curve =  this.curvedPath( {source : o , target : o } );
                  return curve;
             });

         console.log("links transitioned");

         // Remember the current positions as prior positions - they will be used to calculate transitions
         root.eachBefore(d => {
             d.x0 = d.x;
             d.y0 = d.y;
         });

    }


    /*
     * If an entity type is selected and the view has not already been scrolled, scroll it now.
     */
    scrollSelectedIntoView(typeToView) {

        if (this.scrolled === false) {

            this.scrolled = true;

            if (typeToView !== undefined && typeToView !== "") {
                var elem = document.getElementById("elem"+typeToView);
                var brect = elem.getBoundingClientRect();
                var drg = document.getElementById('drawing');
                var togo = brect.top-500;
                var inc = 10;
                this.incrementalscroll(drg, togo, inc);   // TODO - fix case
            }
        }
    }

    incrementalscroll(drg, togo, inc) {
        if (Math.abs(togo) < inc) {
            inc = Math.abs(togo);
        }
        var rate = Math.abs(togo) / (10 * inc);
        if (Math.abs(togo) > 0) {
            var dirinc = Math.sign(togo) * inc * rate;
            drg.scrollBy(0, dirinc);
            togo = togo - dirinc;
        }
        if (Math.abs(togo) > inc) {
            setTimeout(function() { this.incrementalscroll(drg,togo,inc); }, 10);
        }
    }

    /*
     * Indicate whether a node should be highlighted
     */
    inhHighlight(d) {

        // Check whether node is selected as focus
        if (this.focusType === d.data.name) {
            return true;
        }
        return false;
    }


    transitionComplete() {

        // Earliest opportunity to scroll accurately
        if (this.focusType !== undefined && this.focusType !== "") {
            this.scrollSelectedIntoView(this.focusType);
        }
    }



    typeSelected(cat, typeName) {
        this.outEvtChangeFocus(typeName);
    }


    /*
     *  Outbound event: change-focus
     */
    outEvtChangeFocus(typeName) {
        var customEvent = new CustomEvent('change-focus', { bubbles: true, composed: true, detail: {source: "inheritance-diagram", focusType: typeName} });
        this.dispatchEvent(customEvent);
    }

}


window.customElements.define('inheritance-diagram', InheritanceDiagram);