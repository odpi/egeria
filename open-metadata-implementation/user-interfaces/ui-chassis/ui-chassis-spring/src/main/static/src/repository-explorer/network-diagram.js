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
* NetworkDiagram implements a web component for drawing a network graph of
* interconnected entities and relationships.
*
* This component visualizes a graph of the relationships and entities selected
* by the user.
* It is possible to click on an entity of relationship to 'select' it - meaning
* that the selected entity or relationship becomes the focus for the next
* operation and may be highlighted or displayed by other components. In the
* NetworkDiagram, the focus instance is highlighted.
*
* This component uses the D3 force layout by Michael Bostock
*
* A node has:
* id                     - the guid of the entity this node represents
* gen                    - which gen it is from
* x                      - horiz coord - starts at center of diagram
* y                      - vert coord - starts at center of diagram
* label                  - derived from entity properties, type, etc.
* metadataCollectionName - the name of the metadataCollection this instance originated from
* nodeId() is an accessor function that retrieves id - needed for links
*
* A link has
* id                     - guid of the relationship this link represents
* source                 - end1 node
* target                 - end2 node
* gen                    - which gen it is from
* metadataCollectionName - the name of the metadataCollection this instance originated from
*/


class NetworkDiagram extends PolymerElement {

    static get template() {
        return html`

            <style include="rex-styles">

                .node {
                    stroke: var(--egeria-primary-color);
                    fill: #FFF;
                    stroke-width: 2px;
                    radius: 10px;
                }

                .link {
                    stroke: var(--egeria-primary-color);
                    stroke-width: 2px;
                }



            </style>

            <body>

                <div>
                     <paper-radio-group  id="mode-selection-group"  selected="{{selectedMode}}">
                         <paper-radio-button name="Temporal" selected>Time-based</paper-radio-button>
                         <paper-radio-button name="Proximal"         >Proximity-based</paper-radio-button>
                     </paper-radio-group>

                     <paper-button
                         class="inline-element"
                         style="padding:10px; text-transform:none;"
                         id = "saveButton"
                         raised
                         on-click="saveImage">
                         Save Image
                     </paper-button>

                </div>

                <!--<h2>SVG dataurl:</h2>-->
                <div id="svgdataurl" style="display:none"></div>
                <!--<h2>SVG converted to PNG dataurl via HTML5 CANVAS:</h2>-->
                <div id="pngdataurl" style="display:none"></div>
                <canvas id="canvas" width="1200" height="1200" style="display:none"></canvas>

                <div id="ins" style="position:relative; overflow: auto; background-color:#FFFFFF; padding:0px;">
                     <p>
                     Placeholder for network diagram...
                     </p>
                </div>

            </body>

        `;
    }


    static get properties() {
        return {

            instanceRetriever: Object,

            /*
             * selectedMode allows the user to relax the diagram or enforce temporal ordering.
             * It is set by the radio buttons at the top of the diagram.
             */
            selectedMode: {
                type          : String,
                value         : "Temporal",              // possible values: "Temporal" (default) and "Proximal"
                observer      : 'selectedModeChanged'    // Observer called when this property changes
            },

            width : {
                type  : Number,
                value : 1200
            },

            height : {
                type  : Number,
                value : 1200
            },

            node_radius : {
               type  : Number,
               value : 15
            },

            link_distance : {
                type  : Number,
                value : 200
            },

            /* Nodes represent entities. Each node has:
             *   -  id  - set to the entityGUID because it is unique and used by relationships
             *   -  x,y positions
             *   -  label - derived from entity properties
             */
            nodeArray : {
                type  : Array,
                value : []
            },

            /*
             * Links represent relationships. The source and target are the nodes representing the
             * entities connected by the relationship. Source is always 'entityOne', target is
             * always 'entityTwo'
             */
            linkArray : {
                type  : Array,
                value : []
            },

            /*
             * Map of entityGuid -> node for all known nodes.
             * This is needed to be able to efficiently reference a node by GUID, e.g. because
             * it is referenced by a relationship.
             */
            allNodes : {
                type: Map,
                value : {}
            },

            /*
             * Property for tracking number of gens as graph is extended and reduced.
             * This property is used for layout calculations - for the inter-gen
             * spacing for example
             */
            numberOfGens : {
                type : Number,
                value : 0
            },

            /*
             * Anchor for the D3 force simulation
             */
            sim : {
                type : Object,
                value : null
            },

            /*
             * Anchor for top level SVG element
             */
            svg : {
                type : Object,
                value : []
            },

            /*
             * Properties for selected nodes and links
             */
            node : {
                type : Object,
                value : []
            },

            link : {
                type : Object,
                value : []
            },

            /*
             * Properties for handling color themes. These are defined in CSS styles
             * but because nodes and links are dynamically re-colored (on selection for
             * example) these are needed as string variables.
             */
            egeria_primary_color_string : {
                type : String,
                value : ""
            },

            /*
             * For text labels (which are rendered against white background) the default
             * primary color (aqua) may be too light. Use a darker shade of the primary
             * color for labels. This affects the focus entity or relationship label.
             * If primary is aqua a shade similar to "#50aaba" works well.
             * The color is initialised to empty here and set up as a relative shade
             * when cpt ready.
             */
            egeria_text_color_string : {
                 type : String,
                 value : ""
            },

            /*
             * As different home repositories are discovered, assign each a 'color'.
             * For accessibility these are generally not 'colors' but actually a shades of gray.
             */
            repositoryToColor : {
                type : Map,
                value : {}
            },

            colorToRepository : {
                type : Map,
                value : {}
            },

            possibleColors : {
                type : Array,
                value : []
            },

            /*
             * Property that cam be used for temporary staging of image data
             * Not currently used:
             *  svgDataForImage : {
             *      type : Object
             *  }
             */


        };
    }



    /*
     * Element is ready
     */
    ready() {
        // Call super.ready() to initialise node hash...
        super.ready();

        // Here's some starter data:
        if (false) {
        this.nodeArray =  [ {id:1, x:100, y:100, label:"alice",   gen:1},
                          {id:2, x:200, y:100, label:"bob",     gen:1},
                          {id:3, x:200, y:200, label:"charlie", gen:1}
                       ];

        // Here's some starter data:
        this.linkArray =   [ { id:1, source: 1, target: 2, idx: 0, label:"sibling", gen:1 },
                           { id:2, source: 1, target: 3, idx: 0, label:"manages", gen:1 },
                           { id:3, source: 1, target: 3, idx: 1, label:"knows",   gen:1 },
                           { id:4, source: 2, target: 2, idx: 0, label:"knows",   gen:1 },
                           { id:5, source: 2, target: 2, idx: 1, label:"feeds",   gen:1 },
                           { id:6, source: 2, target: 1, idx: 0, label:"likes",   gen:1 }
                         ];



        this.allNodes = {};
        this.nodeArray.forEach(node => {
            this.allNodes[node.id] = node;
        });
        }

        this.repositoryToColor = {};
        this.colorToRepository = {};
        this.possibleColors = ['#EEE','#CCC','#AAA','#888','#666','#444','#222',
                               '#0EE','#0CC','#0AA','#088','#066','#044','#022' ];


        /*
         *  To support dynamic theming of colors we need to detect what the primary color has been
         *  set to - this is done via a CSS variable. For most purposes the CSS variable is
         *  sufficient - but the network-diagram will dynamically color switch as elements are
         *  selected - so we need the primary color accessible at runtime. We also need to
         * set up a slightly dark shade of the primary color for text labels against white
         * background.
         */
        const styles = window.getComputedStyle(this);
        this.egeria_primary_color_string = styles.getPropertyValue('--egeria-primary-color');
        var splitPrimary = this.egeria_primary_color_string.split(' ');
        var strippedPrimary = splitPrimary[splitPrimary.length-1];
        this.egeria_text_color_string = this.alterShade(strippedPrimary, -20);

        /*
         * Finally, render the diagram...
         */
        this.render();
    }

    setInstanceRetriever(instanceRetriever) {
        this.instanceRetriever = instanceRetriever;
    }


    selectedModeChanged(newValue,oldValue) {
        if (this.sim === null) {
            // Can be called during initialisation prior to render and the sim being created. In this case ignore.
            return;
        }
        if (this.selectedMode === "Temporal") {
            var yPlacement = this.yPlacement.bind(this);
            this.sim.force('vert', d3.forceY().strength(0.1).y(function(d) {return yPlacement(d);}));

        }
        else {
              //this.sim.force('vert', null);  -- this allows the graph to sink down the page (or up)
              this.sim.force('vert', d3.forceY(this.height/2).strength(0.001))
        }
        this.sim.alpha(0.1);
        this.sim.restart();
    }



    // Input events

    /*
     * Handle the input event indicating that the focus is now the entity specified.
     */
    inEvtFocusEntityChanged(entityGUID) {

        /* The focus is now the entity with the given GUID.
         * Highlighting will be handled asynchronously but it may be possible
         * to upgrade the label. This situation occurs if the entity was
         * originally loaded as a result of discovering a proxy attached to
         * a relationship, then its label will have been set using the
         * restricted set of properties available on the proxy - i.e. just
         * the unique properties. If this is the case then, now that we have the
         * full entity detail (it is the focus entity) we can update the label to
         * the more preferred label derived from the full entity detail.
         */

        var expEntity = this.instanceRetriever.getFocusEntity();
        if (expEntity !== null) {
            var entityDigest = expEntity.entityDigest;
            var label = entityDigest.label;
            /*
             * Locate the node
             */
            var nodeToUpdate = this.allNodes[entityGUID];
            var idx = this.nodeArray.indexOf(nodeToUpdate);
            if (idx !== -1) {
                /*
                 * Update the node in place in the nodeArray array and allNodes map
                 */
                this.nodeArray[idx].label = label;
                this.allNodes[entityGUID].label = label;

                /*
                 * Finally, update the diagram - this will update the nodes and links
                 * You could leave this to tick() but that would make tick() less efficient.
                 */
                this.update_diagram();
            }
        }
    }

    /*
     * Handle the input event indicating that the focus is now the relationship specified.
     */
    inEvtFocusRelationshipChanged(relationshipGUID) {
        // The focus is now the relationship given.
        // There is nothing to do here - highlighting will be handled asynchronously and the
        // display of details is not the responsibility of this diagram component.
    }


    /*
     * Handle the input event indicating that the graph has additional objects.
     */
    inEvtGraphExtended() {
        // The graph has been added to. We could convey information on the change in the event
        // but for the time being assuming that diagram will ask i-r for the latest gen and
        // redraw.

        var genInfo = this.instanceRetriever.getLatestGen();
        this.numberOfGens = genInfo.numberOfGens;
        var rexTraversal = genInfo.currentGen;

        // Extract the entity and relationship information from the traversal's digests and add them to the
        // existing arrays of nodes and links...
        // e.g.  {id:1, x:100, y:100, label:"alice", gen:1}

        var entityDigests = rexTraversal.entities;
        if (entityDigests != null) {
             for (var e in entityDigests) {
                  var entityDigest = entityDigests[e];
                  var newNode = {};
                  newNode.id = entityDigest.entityGUID;
                  newNode.label = entityDigest.label;
                  newNode.gen = entityDigest.gen;
                  newNode.metadataCollectionName = entityDigest.metadataCollectionName;
                  // Add nodes in the current vertical displacement according to gen
                  newNode.x = this.width/2;
                  newNode.y = this.yPlacement(newNode);
                  this.nodeArray.push(newNode);
                  this.allNodes[newNode.id] = newNode;
             }
        }

        var relationshipDigests = rexTraversal.relationships;
        if (relationshipDigests != null) {
            for (var r in relationshipDigests) {
                var relationshipDigest = relationshipDigests[r];
                var newLink = {};
                newLink.id = relationshipDigest.relationshipGUID;
                newLink.label = relationshipDigest.label;
                newLink.source = this.allNodes[relationshipDigest.end1GUID];
                newLink.target = this.allNodes[relationshipDigest.end2GUID];

                // Attempt to bias nodes so that sources are to the left, targets to the right...
                if (newLink.source.gen === relationshipDigest.gen) {
                    newLink.source.x = newLink.source.x - this.width/4;
                }
                if (newLink.target.gen === relationshipDigest.gen) {
                    newLink.target.x = newLink.target.x + this.width/4;
                }

                // Look through existing links (linkArray) to find multi-edges and set idx accordingly
                var count = 0;
                for (var l in this.linkArray) {
                    var link = this.linkArray[l];
                    if (link.source === newLink.source && link.target === newLink.target) {
                        count = count+1;
                    }
                }
                newLink.idx = count;
                newLink.gen = relationshipDigest.gen;
                newLink.metadataCollectionName = relationshipDigest.metadataCollectionName;

                // Once the force layout has started we need to specify nodes (not array indexes or ids)
                this.linkArray.push(newLink);
            }
        }

        this.update_diagram();
    }


    /*
     * This is a fairly subtle approach - it clears only the objects that have been removed - this should
     * provide more seamless update of the diagram as existing SVG nodes and locations are retained.
     */
    inEvtGraphBeingReduced() {

        // The graph has been modified by removing something.
        // Identify just what has been removed and extract it from the visual graph data.
        //
        var genInfo = this.instanceRetriever.getLatestGen();
        this.numberOfGens = genInfo.numberOfGens;
        var rexTraversal = genInfo.currentGen;

        // Process the traversal - this time removing the entities and relationships from the graph.

        var entityDigests = rexTraversal.entities;
        if (entityDigests != null) {
             for (var e in entityDigests) {
                 var nodeToRemove = this.allNodes[e];
                  var idx = this.nodeArray.indexOf(nodeToRemove);
                  if (idx !== -1) {
                      // Remove node from nodeArray array
                      this.nodeArray.splice(idx, 1);
                  }
                  // Remove node from allNodes map
                  this.allNodes[e] = undefined;
             }
        }
        var relationshipDigests = rexTraversal.relationships;
        if (relationshipDigests != null) {
            for (var r in relationshipDigests) {
                //var linkToRemove = this.allNodes[r];
                var linkFound = false;
                for (var idx=0; idx<this.linkArray.length; idx++) {
                    if (this.linkArray[idx].id === r) {
                        // Note the idx so the link can be removed
                        linkFound = true;
                        break;
                    }
                }
                if (linkFound) {
                    this.linkArray.splice(idx,1);
                }
            }
        }

        this.update_diagram();

        this.outEvtGraphReduced();

    }


    clearGraph() {
        this.nodeArray = [];
        this.linkArray = [];
        this.allNodes = {};
        this.repositoryToColor = {};
        this.colorToRepository = {};
        this.update_diagram();
    }

    // Output events

    outEvtChangeFocusEntity(entityGUID) {
        var customEvent = new CustomEvent('change-focus-entity', { bubbles: true, composed: true,
                                          detail: {entityGUID: entityGUID, source: "network-diagram"} });
        this.dispatchEvent(customEvent);
    }

    outEvtChangeFocusRelationship(relationshipGUID) {
        var customEvent = new CustomEvent('change-focus-relationship', { bubbles: true, composed: true,
                                            detail: {relationshipGUID: relationshipGUID, source: "network-diagram"} });
        this.dispatchEvent(customEvent);
    }

    outEvtGraphReduced() {
        var customEvent = new CustomEvent('graph-reduced', { bubbles: true, composed: true,
                                          detail: {source: "network-diagram"} });
        this.dispatchEvent(customEvent);
    }

    /*
     * This method initialises the diagram, creates the graph data and renders the graph
     */
    render() {

        // Clear any startup text or previous diagram content
        this.clearNetworkDiagram();

        this.initialize_diagram();

        this.update_diagram();

    }

     /*
      * This method clears any introductory text or previous rendering of the diagram
      */
     clearNetworkDiagram() {

         var myDiv = this.shadowRoot.querySelector('#ins');

         // Clear SVG objects from div
         d3.select(this.shadowRoot.querySelector('#ins')).selectAll("svg").remove();

         // Clear the introductory text...
         this.$.ins.innerHTML = "";
     }




    initialize_diagram() {

        var width = this.width;
        var height = this.height;

        this.nodeArray = [];
        this.linkArray = [];
        this.allNodes = {};
        this.repositoryToColor = {};
        this.colorToRepository = {};


        this.svg = d3.select(this.shadowRoot.querySelector('#ins'))
            .append("svg")
            .attr('width', width)
            .attr('height', height)
            ;

        // For placement of nodes vertically within diagram use the yPlacement function.
        var yPlacement = this.yPlacement.bind(this);
        var ls = this.ls.bind(this);
        var egeria_primary_color_string = this.egeria_primary_color_string;

        this.sim = d3.forceSimulation(this.nodeArray)
            .force('horiz', d3.forceX(width/2).strength(0.01))
            .force('vert', d3.forceY().strength(0.1).y(function(d) {return yPlacement(d);}))
            .velocityDecay(0.6)
            .force('repulsion', d3.forceManyBody().strength(-500))
            .alphaDecay(.0005)
            .velocityDecay(0.6)
            .on('tick',this.tick.bind(this))
            .force('link', d3.forceLink().links(this.linkArray)
                .id(this.nodeId)
                .distance(this.link_distance)
                .strength(function(d) { return ls(d);})) ;


        // Define arrowhead for links
        this.svg.append("svg:defs").selectAll("marker")
            .data(["end"])
            .enter().append("svg:marker")
            .attr("id", String)
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 25)  // The marker is 10 units long (in x dir) and nodes have radius 15.
            .attr("refY", 0)
            .attr("markerWidth", 4)
            .attr("markerHeight", 4)
            .style("stroke", egeria_primary_color_string)
            .style("fill", egeria_primary_color_string)
            .attr("orient", "auto")
            .append("svg:path")
            .attr("d", "M0,-5L10,0L0,5") ;
    }


    /*
     * Experiment with link-specific link-strength (ls)
     */
    ls(d) {
        var gen_s = d.source.gen;
        var gen_t = d.target.gen;
        var gen_diff = gen_t - gen_s;
        var mag_diff = Math.max(1,Math.abs(gen_diff));
        return 1.0 / mag_diff;
    }


    yPlacement(d) {
        var y = 0;
        var perGen = 0;
        var ymin = this.height / 6;
        var ymax = 5 * (this.height / 6);
        /*
         * Displayable area is limited to between ymin and ymax (default to one-sixth and five sixths of height).
         * diagram knows the number of gens - it retrieves it with the gen info whenever a graph-changed event is handled.
         * Starting gen is indexed as 1.
         * For gens up to and including 5 use an additional sixth of the height; after that shrink the perGen gaps.
         * Vertical placement of a node in gen g is therefore:
         * If numGens <= 5, perGen = (ymax - ymin) / 4;
         * If numGens > 5.  perGen = (ymax - ymin) / (numGens - 1)  // because 1 is the starting gen
         * In either case y = ymin + (g -1) * perGen
         */
        if (this.numberOfGens <= 5) {
            perGen = (ymax - ymin) / 4;
        }
        else {
            perGen = (ymax - ymin) / (this.numberOfGens - 1);
        }
        y = ymin + (d.gen-1) * perGen;

        return y;
    }



    update_diagram() {
        /*
         * In order to get the nodes always in front of the links, the nodes need to be re-added to the svg
         * So whenever links changes we need to re-generate all the nodes. Or you do select and re-gen of all
         * nodes in the tick function. The former is probably more efficient.
         */

        /*
         * Refresh the sim's data
         * Refresh the odes first - this will cause the positions to refresh. Then refresh the links
         */
        this.sim.nodes(this.nodeArray);
        this.sim.force('link').links(this.linkArray);

        this.updateLinks();
        this.updateNodes();

        this.sim.alpha(0.1);
        this.sim.restart();

    }

    /*
     * Generic accessor function for nodes
     */
    nodeId(d) {
       return d.id;
    }

    /*
     * Function to retrieve a specified node
     */
    getNode(id) {
      return ( this.nodeArray.filter(obj => { return obj.id === id  })[0] );
    }

    /*
     * This function saves the current diagram as a PNG image file.
     */
    saveImage() {
        var shadowRoot = this.shadowRoot;

        var canvas = this.$.canvas;
        var context = canvas.getContext("2d");
        var image = new Image;

        var html = this.svg
            .attr("version", 1.1)
            .attr("xmlns", "http://www.w3.org/2000/svg")
            .node().parentNode.innerHTML;

        var imgsrc = 'data:image/svg+xml;base64,'+ btoa(html);

        /*
         * It is possible to render the SVG data but this is only retained for interest..
         *  var img = '<img src="'+imgsrc+'">';
         *  var svgDataForImage = d3.select(this.shadowRoot.querySelector('#svgdataurl')).html(img);
         *  svgDataForImage.html(img);
         *
         * Instead, this function renders a (hidden) canvas and uses that to generate a PNG. If desired
         * the canvas could be revealed - e.g. for user annotation - at this point by setting display to block
         * i.e. canvas.style.display="block";
         * However, the canvas will display inline and displace other elements - so be careful where you
         * locate it, e.g. at the end of the network-diagram's DOM.
         */

        image.src = imgsrc;

        image.onload = function() {
            context.drawImage(image, 0, 0);
            var canvasData = canvas.toDataURL("image/png");
            var pngImage = '<img src="'+canvasData+'">';
            d3.select(shadowRoot.querySelector('#pngdataurl')).html(pngImage);
            var a = document.createElement("a");
            a.download = "EgeriaRepositoryExplorerImageCapture.png";
            a.href = canvasData;
            a.click();
        };

    }

    /*
     * This function performs the continuous update of the diagram to cope with
     * updates to the content or layout and drag and drop operations
     */
    tick() {

        // TODO investigate what can be refined to maximise performance..

        // Try to prevent nodes from drifting off the edge of the diagram area - this will not guarantee that labels
        // stay on board but it will be close.
        // In the cx, cy attribute calculation it allows double node_margin on the right hand side so accommodate a label
        // length roughly equal to 3 times the node_radius, to try to keep labels in view.
        var node_margin = 2 * this.node_radius; // Allow a safety margin so that edges are less likely to stray
        var width       = this.width;
        var height      = this.height;

        // Keep nodes in the viewbox, with a safety margin so that (curved) links are unlikely to stray...
        this.node.attr('cx',function(d) { return d.x = Math.max(node_margin, Math.min(width  - 4 * node_margin, d.x)); });
        this.node.attr('cy',function(d) { return d.y = Math.max(node_margin, Math.min(height -     node_margin, d.y)); });
        this.node.attr('transform', function(d) { return "translate(" + d.x + "," + d.y + ")";});

        // Highlight a selected node, if it is the instance that has been selected or just loaded (in which case it is selected)

        this.node.selectAll('circle')
            .attr("fill", d => this.nodeColor(d) );

        this.node.selectAll('text')
            .attr("fill", d => this.highlighted(d) ? this.egeria_text_color_string : "#444" );

        // For curved paths use the following...
        var path_func = this.path_func.bind(this);

        this.link.selectAll('path')
            .attr('d', function(d) { return path_func(d).path; })
            .lower();

        this.link.selectAll('text')
            .attr("x", function(d) { return d.x = path_func(d).midpoint.x; } )
            .attr("y", function(d) { return d.y = path_func(d).midpoint.y; } )
            .attr("fill", d => this.highlighted(d) ? this.egeria_text_color_string : "#888" )
            .attr("dominant-baseline", function(d) { return (d.source.x > d.target.x) ? "baseline" : "hanging"; } )
            .attr("transform" , d => `rotate(${180/Math.PI * Math.atan((d.target.y-d.source.y)/(d.target.x-d.source.x))}, ${d.x}, ${d.y})`)
            .attr("dx", d => { ((d.target.y-d.source.y)<0)? 100.0 : -100.0; })
            .attr("dy", d => {
                ((d.target.x-d.source.x)>0)?
                20.0 * (d.target.x-d.source.x)/(d.target.y-d.source.y) :
                20.0 * (d.source.x-d.target.x)/(d.target.y-d.source.y) ;
                });

        // For straight lines use the following...
        // selectAll('line') and then set...
        //            .attr('x1', function(d) { return d.source.x; })
        //            .attr('y1', function(d) { return d.source.y; })
        //            .attr('x2', function(d) { return d.target.x; })
        //            .attr('y2', function(d) { return d.target.y; });
        // You will need to set the lineLabel x and y attrs to the mid point (s.x+t.x)/2 etc...


    }



    updateNodes() {

        var node_radius = this.node_radius;
        var width       = this.width;
        var height      = this.height;
        var svg         = this.svg;

        // update the visual rendering of the nodes

        // Keep nodes 'on top' of links
        // Re-drawing nodes allows us to change visual attributes like colour too
        svg.selectAll(".node").remove()

        this.node = svg.selectAll(".node")
            .data(this.nodeArray)

        this.node.exit().remove();

        var dragstarted = this.dragstarted.bind(this);
        var dragged     = this.dragged.bind(this);
        var dragended   = this.dragended.bind(this);

        var enter_set = this.node.enter()
            .append("g")
            .attr('class', 'node')
            .attr("cursor", "pointer")
            .call(d3.drag()
                .container(this)
                .on("start", dragstarted)
                .on("drag", dragged)
                .on("end", dragended) );

        enter_set.append('circle')
            .attr('r',node_radius)
            .attr('stroke',this.egeria_primary_color_string)
            .attr('stroke-width','2px')
            .attr('fill','white')
            .on("click", d => { this.nodeClicked(d.id); })  // The node's id is the entityGUID
            ;

        enter_set.append('text')
            .attr("fill","#444")
            .text( function(d) { return d.label; } )
            .attr("font-family","sans-serif")
            .attr("font-size","12px")
            .attr("stroke-width","0")
            .attr("dx",20)
            .attr("dy",".35em")
            .on("click", d => { this.nodeClicked(d.id);})  // The node's id is the entityGUID
            ;


        // Check all labels are up to date -- this does not yet include the enter_set as they have only just been added
        // and are known to have correct labels
        this.node.select('text')
            .text( function(d) { return d.label; } ) ;

        this.node = this.node.merge(enter_set);
    }


    dragstarted(d) {
        if (!d3.event.active)
            this.sim.alphaTarget(0.3).restart(); // this provides smooth drag behaviour
        d.fx = d.x, d.fy = d.y;
    }

    dragged(d) {
        d.fx = d3.event.x, d.fy = d3.event.y;
    }

    dragended(d) {
        if (!d3.event.active) {
            this.sim.alphaTarget(0);
        }
        d.fx = null, d.fy = null;
    }




    // update the visual rendering of the links
    updateLinks() {

        var svg = this.svg;
        var path_func = this.path_func.bind(this);

        this.link = svg.selectAll(".link")
             .data(this.linkArray)
             .attr('x1', function(d) { return d.source.x; })
             .attr('y1', function(d) { return d.source.y; })
             .attr('x2', function(d) { return d.target.x; })
             .attr('y2', function(d) { return d.target.y; });

        this.link.exit().remove();

        var enter_set = this.link.enter()
              .append("g")
              .attr('class', 'link')
              .attr("cursor", "pointer") ;


        enter_set.append('text')
            .attr('class', 'edgeLabel')
            .attr("fill","#CCC")
            .attr("stroke", "none")
            .attr("font-family","sans-serif")
            .attr("font-size","10px")
            .attr("stroke-width", 0)
            //.attr("alignment-baseline","middle")
            .attr("dominant-baseline", function(d) { return (d.source.x > d.target.x) ? "baseline" : "hanging"; } )
            .attr("x", function(d) { return path_func(d).midpoint.x; } )
            .attr("y", function(d) { return path_func(d).midpoint.y; } )
            // For straight paths you would want the following:
            //    .attr("x", d => (d.source.x+d.target.x)/2)
            //    .attr("y", d => (d.source.y+d.target.y)/2)
            .attr('text-anchor', 'middle')
            .text( function(d) { return d.label; } )
            .on("click", d => { this.edgeClicked(d.id); })   // The edge's id is the relationshipGUID
            .clone(true)
            .lower()
            .attr("stroke-linejoin", "round")
            .attr("stroke-width", 3)
            .attr("stroke", "white") ;


        // For straight lines use the following...
        // enter_set.append('line')
        //                  .attr('class', 'link')
        //                  .attr('x1', function(d) { return d.source.x; })
        //                  .attr('y1', function(d) { return d.source.y; })
        //                  .attr('x2', function(d) { return d.target.x; })
        //                  .attr('y2', function(d) { return d.target.y; })
        //                  .merge(u);

        // For curved paths use the following...
        enter_set.append('path')
            .attr('class', 'line')
            .attr("cursor", "pointer")
            .attr('d', function(d) { return path_func(d).path; })   // d => { path_func(d); }) does not work
            .attr("fill", "none")
            .attr('stroke',this.egeria_primary_color_string)
            .attr('stroke-width','2px')
            // Only place a marker if the link is not reflexive
            .attr("marker-end", function(d) { return (d.source===d.target)?"none":"url(#end)";})
            .on("click", d => { this.edgeClicked(d.id); })   // The edge's id is the relationshipGUID
            .lower()
            ;

        this.link = this.link.merge(enter_set);



    }


    /*
     * This function path_func computes the path for either a reflexive or non-reflexive link.
     * It also calculates the mid-point which can be used as an anchor point for the edge label.
     * Returns a map containing:
     *  {
     *     path     : <path-as-a-string> ,
     *     midpoint : <midpoint-as-map{x:<x>,y:<y>}
     *  }
     *
     */
    path_func(d) {

        var returnMap = {};
        var midpoint = {};

        var path = d3.path();

        if ( d.source.id == d.target.id ) {

            /*
             * Reflexive link
             */
            var cp_offset = 0.15 + (d.idx * 0.1);   /* Indexing is for separation of multi-links */
            var base_rad  = this.link_distance;     /* Sets base_rad for innermost link = link_distance for
                                                     * non-reflexive links; this is subjective but results
                                                     * in sensible radii for reflexive links
                                                     */
            var link_rad  = base_rad * cp_offset;
            path.moveTo(d.source.x,d.source.y);
            path.arc(d.source.x+link_rad,d.source.y,link_rad,Math.PI,0.999*Math.PI);
            midpoint.x    = d.source.x+1.7*link_rad;    /* Place the label away from the node and its label... */
            midpoint.y    = d.source.y-0.7*link_rad;;
        }
        else {

            /*
             * Non-reflexive link
             */
            var dx        = d.target.x - d.source.x;
            var dy        = d.target.y - d.source.y;
            var mid       = {};
            mid.x         = d.source.x + dx/2.0;
            mid.y         = d.source.y + dy/2.0;
            var denom     = dy > 0 ? Math.max(0.001,dy) : Math.min(-0.001,dy);
            var gNormal   = 1.0 * (dx) / denom;
            var unit      = {};
            unit.x        = Math.sign(dy) * Math.sqrt( 1.0 / (1.0 + gNormal**2) );
            unit.y        = -1.0 * gNormal * unit.x;
            var mag       = this.link_distance ;
            var cp_offset = 0.2 * (d.idx+1);        /* Indexing is for separation of multi-links */
            var cp        = {};
            cp.x          = mid.x + cp_offset * mag * unit.x;
            cp.y          = mid.y + cp_offset * mag * unit.y;
            path.moveTo(d.source.x,d.source.y);
            path.quadraticCurveTo(cp.x,cp.y,d.target.x,d.target.y);
            midpoint.x    = mid.x + 0.5 * cp_offset * mag * unit.x;
            midpoint.y    = mid.y + 0.5 * cp_offset * mag * unit.y;
        }

        returnMap.path = path.toString();
        returnMap.midpoint = midpoint;
        return returnMap;

    }


    /*
     * Issue request to change focus when node clicked
     */
    nodeClicked(guid) {
        // Request a focus change...
        this.outEvtChangeFocusEntity(guid);
    }

    /*
     * Issue request to change focus when edge clicked
     */
    edgeClicked(guid) {
        // Request a focus change...
        this.outEvtChangeFocusRelationship(guid);
    }


    /*
     *  This function is called to determine whether the instance is the currently selected item (and therefore
     *  should be visually highlighted).
     */
    highlighted(d) {

        var focusGUID = this.instanceRetriever.getFocusGUID();
        if (focusGUID !== undefined) {
            if (d.id === focusGUID)
                return true;
        }
        return false;
    }


    /*
     *  This function is called to determine whether the color of a node - it may be selected or not, and the user may
     *  have opted to show nodes from different repositories in different colors.
     *  The selected node always appears in egeria-primary-color.
     */
    nodeColor(d) {

        /*
         * If instance is the current focus, paint it in egeria primary color
         */
        if (this.highlighted(d)) {
            return this.egeria_primary_color_string;
        }
        else {
            /*
             * Look up repository name in repositoryColor map, if not found assign next color.
             * This actually assigns gray-shades, starting with the #EEE and darkening by two
             * stops for each new repository found - e.g. #AAA -> #888. There are therefore 8 shades
             * that can be allocated, by which time we are at 100% black. If this number proves to
             * be insufficient, we can shorten the two-stops or assign a single hue, e.g. green.
             */
            var colorString = this.repositoryToColor[d.metadataCollectionName];
            if (colorString !== undefined) {
                return colorString;
            }
            else {
                // Assign first available color
                var assigned = false;
                for (var col in this.possibleColors) {
                    var colorString = this.possibleColors[col];
                    if (this.colorToRepository[colorString] === undefined) {
                        // Color is available
                        this.repositoryToColor[d.metadataCollectionName] = colorString;
                        this.colorToRepository[colorString] = d.metadataCollectionName;
                        return colorString;
                    }
                }
                if (!assigned) {

                    /*
                     * Ran out of available colors for repositories!
                     *
                     * Assign a color that we know is not in the possible colors to this
                     * repo and any further ones we discover. Remember this for consistency
                     * - i.e. this repository will use this color for the remainder of this
                     * exploration. There may be multiple repositories sharing this same color
                     * so do not update the colorToRepository map. If a color frees up it will
                     * be allocated to a new repository, but not to repositories remembered below.
                     */
                    var col = '#000';
                    this.repositoryToColor[d.metadataCollectionName] = col;
                    return col;
                }
            }
        }
    }


    alterShade(color, percent) {

        var R = parseInt(color.substring(1,3),16);
        var G = parseInt(color.substring(3,5),16);
        var B = parseInt(color.substring(5,7),16);

        R = parseInt(R * (100 + percent) / 100);
        G = parseInt(G * (100 + percent) / 100);
        B = parseInt(B * (100 + percent) / 100);

        R = (R<255)?R:255;
        G = (G<255)?G:255;
        B = (B<255)?B:255;

        var RR = ((R.toString(16).length==1)?"0"+R.toString(16):R.toString(16));
        var GG = ((G.toString(16).length==1)?"0"+G.toString(16):G.toString(16));
        var BB = ((B.toString(16).length==1)?"0"+B.toString(16):B.toString(16));

        return "#"+RR+GG+BB;
    }

}


window.customElements.define('network-diagram', NetworkDiagram);