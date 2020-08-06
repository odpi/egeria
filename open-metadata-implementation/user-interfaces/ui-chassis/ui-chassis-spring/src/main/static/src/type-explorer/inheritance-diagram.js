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
* that contain Egeria entity types. Each tree is collapsible and expandable to hide/
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

                <div id="inh" style="position:relative; overflow-x: hidden;  overflow: auto; background-color:#FFFFFF"">
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
    }


    setTypeManager(typeManager) {
        this.typeManager = typeManager;
    }



    // Input events


    inEvtFocusChanged(focusType) {
        this.focusType = focusType;
        var drawingArea = this.$.inh;
        this.updateRoot(drawingArea);
    }



    /*
     * This method initialises the diagram, creates and renders all trees
     */
    render(focusType) {

        // Render inheritance diagram

        this.initialiseInheritanceDiagram();

        // Record the focus type passed by the diagram manager - for cases where the focus is already
        // set and the diagram is created.
        this.focusType = focusType;

        var diagram = this.$.inh;

        this.createInheritanceTrees(diagram);

        // call updateRoot - for all trees
        this.updateRoot(diagram);

    }



    /*
     * This method clears any introductory text or previous rendering of the diagram, and resets control properties
     */
    initialiseInheritanceDiagram() {

        d3.select('#inh').selectAll("svg").remove();

        // Clear the introductory text...
        this.$.inh.innerHTML = "";

        // Initialise control properties...
        this.renderedTrees = [];
        this.scrolled = false;

    }


    /*
     * This method iterates over the known entity types and creates a separate tree for
     * any that have no supertype (i.e. the entity is itself a root)
     * For each such root, create the inheritance tree and render it
     */
    createInheritanceTrees(drawingArea) {

        /*
         * The inheritance diagram shows the type hierarchy of entity types.
         * The user may have already selected a type (of any category) but it is optional
         */


        /*
         * Each inheritance tree needs to be formatted as follows:
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
                this.renderInheritanceTree(tree, drawingArea, typeName, this.treeDepth);
            }
        })
    }


    /*
     * This method creates the inheritance tree for a single root entity
     */
    createInheritanceTree(typeName) {

        var inheritanceTree = {};

        // Start at the type with typeName and follow subtype links to compose children
        var node = this.typeManager.getEntity(typeName);
        var childNames = node.subTypeNames;
        inheritanceTree = this.addSubTree(inheritanceTree, typeName, childNames, 1)
        return inheritanceTree;
    }


    /*
     * Recursively work down the tree adding subtrees...
     */
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


    /*
     * Render one inheritance tree
     */
    renderInheritanceTree(typeHierarchy, drawingArea, typeName, treeDepth) {

        var width = drawingArea.offsetWidth;
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

        thisTree.gLink = thisTree.svg
                                 .append("g")
                                 .attr("fill", "none")
                                 .attr("stroke", "#888")
                                 .attr("stroke-opacity", 0.4)
                                 .attr("stroke-width", 1.5);

        thisTree.gNode = thisTree.svg
                                 .append("g")
                                 .attr("cursor", "pointer");

        // Remember this tree
        this.renderedTrees[typeName] = thisTree;

    }


    /*
     * Refresh all trees in the diagram.
     * This method is called on initial rendering and if the focus type is changed
     */
    updateRoot(drawingArea) {

        var treeNames = Object.keys(this.renderedTrees);

        treeNames.forEach(treeName => {
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

        // Since an update is being performed, unset scrolled so that on transition completion
        // the code will re-evaluate the scroll position
        this.scrolled = false;

        var width = drawingArea.offsetWidth;
        var thisTree = tree;
        var sourceTypeName = subtree.data.name;

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

        // Calculate the overall height of the tree (across all the nodes - the tree is drawn horizontally)
        var left  = root;
        var right = root;
        root.eachBefore(node => {
            if (node.x < left.x) left = node;
            if (node.x > right.x) right = node;
        });
        var height = right.x - left.x + margin.top + margin.bottom;

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


         // Transition links to their new position.
         link.merge(linkEnter)
             .transition(transition)
             .attr("d", d => {
                  const s = {x: d.source.x, y: d.source.y};
                  const t = {x: d.target.x, y: d.target.y};
                  var curve = this.curvedPath( {source : s , target : t } );
                  return curve;
             });


         // Transition exiting nodes to the parent's new position.
         link.exit()
             .transition(transition)
             .remove()
             .attr('d', function (d) {
                  const o = {x: subtree.x, y: subtree.y};
                  var curve =  this.curvedPath( {source : o , target : o } );
                  return curve;
             });


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

                var elem = this.shadowRoot.querySelector('#elem'+typeToView);

                // scrollIntoView almost works but the options do not work across browsers,
                // including Safari, so it does not center and is not smooth. The lack of centering
                // means it doesn't unscroll a scrolled diagram
                // The following is what we might *like* to do:
                // elem.scrollIntoView({behavior: "smooth", block:"center", inline:"center"});

                // Instead of scrollIntoView - try to persist with the incremental scrolling
                // which does work outside of a web component...
                var brect = elem.getBoundingClientRect();
                var togo = brect.top-500;
                var inc = 10;

                var drg = this.$.inh;
                this.incrementalScroll(drg, togo, inc);

            }
        }
    }

    incrementalScroll(drg, togo, inc) {
        if (Math.abs(togo) < inc) {
            inc = Math.abs(togo);
        }
        var rate = Math.abs(togo) / (10 * inc);
        if (Math.abs(togo) > 0) {
            var dirinc = Math.sign(togo) * inc * rate;
            // scrollBy does not seem to work when in a web component
            // scrollIntoView (which could be called from scrollSelectedIntoView() almost works
            // but the center and smooth options are not well-supported across browsers
            drg.scrollBy(0, dirinc);
            togo = togo - dirinc;
        }
        if (Math.abs(togo) > inc) {
            setTimeout( () => this.incrementalScroll(drg,togo,inc) , 10);
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


    /*
     * Because all types in the inheritance diagram are entity types, the selection of a
     * type will request a change of focus.
     */
    typeSelected(cat, typeName) {
        this.outEvtChangeFocus(typeName);
    }


    /*
     *  Outbound event: change-focus
     */
    outEvtChangeFocus(typeName) {
        var customEvent = new CustomEvent('change-focus',
            {   bubbles: true,
                composed: true,
                detail: {source: "inheritance-diagram",
                focusType: typeName}
            }
        );
        this.dispatchEvent(customEvent);
    }

}


window.customElements.define('inheritance-diagram', InheritanceDiagram);