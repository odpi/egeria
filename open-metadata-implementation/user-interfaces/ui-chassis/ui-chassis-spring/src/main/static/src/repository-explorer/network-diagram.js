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

                <div id="ins" style="position:relative; overflow: auto; background-color:#FFFFFF">
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

            // selectedMode allows the user to relax the diagram or enforce temporal ordering.
            // It is set by the radio buttons at the top of the diagram.
            selectedMode: {
                type               : String,
                value              : "Temporal",              // possible values: "Temporal" (default) and "Proximal"
                observer           : 'selectedModeChanged'    // Observer called when this property changes
            },

            width : {
                type : Number,
                value : 1200
            },

            height : {
                type : Number,
                value : 1200
            },

            svgData : {
                type : Object
            },

             node_radius : {
                type : Number,
                 value : 15
             },

             link_distance : {
                  type : Number,
                  value : 200
             },

             dragging : {
                 type : Boolean,
                 value : false
             },


             // Nodes represent entities. Each node has an
             // *  id  - set to the entityGUID because it is unique and used by relationships
             // *  x and y positions
             // *  label - derived from entity properties
             myNodes : {
                 type : Array,
                 value : []
             },



             // Links represent relationships. The source and target are the entityGUIDs (node.id)


             myLinks : {
                 type : Array,
                 value : []
             },

             // A map of entityGuid -> node for all known nodes.
             allNodes : {
                  type: Map,
                  value : {}
             },

             numberOfGens : {
                 type : Number,
                 value : 0
             },

             sim : {
                 type : Object,
                 value : null
             },

             svg : {
                 type : Object,
                 value : []
             },

             node : {
                 type : Object,
                 value : []
             },

             link : {
                 type : Object,
                 value : []
             },

             scrolled : {
                 type : Boolean,
                 value : false,
                 notify : true,
                 reflectToAttribute : true
             },

             gen : {
                 type : Number,
                 value : 1
             },

             egeria_primary_color_string : {
                 type : String,
                 value : ""
             },

             egeria_text_color_string : {
                  type : String,
                  value : "#50aaba"
             },

             /*
              * As we discover different repositories assign them a 'color' which is
              * actually a shade of gray.
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
             }

        };
    }




    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();

        // Here's some starter data:
        if (false) {
        this.myNodes =  [ {id:1, x:100, y:100, label:"alice",   gen:1},
                          {id:2, x:200, y:100, label:"bob",     gen:1},
                          {id:3, x:200, y:200, label:"charlie", gen:1}
                        ];

        // Here's some starter data:
        this.myLinks =   [ { id:1, source: 1, target: 2, idx: 0, label:"sibling", gen:1 },
                           { id:2, source: 1, target: 3, idx: 0, label:"manages", gen:1 },
                           { id:3, source: 1, target: 3, idx: 1, label:"knows",   gen:1 },
                           { id:4, source: 2, target: 2, idx: 0, label:"knows",   gen:1 },
                           { id:5, source: 2, target: 2, idx: 1, label:"feeds",   gen:1 },
                           { id:6, source: 2, target: 1, idx: 0, label:"likes",   gen:1 }
                         ];



        this.allNodes = {};
        this.myNodes.forEach(node => {
            this.allNodes[node.id] = node;
        });
        }

        this.repositoryToColor = {};
        this.colorToRepository = {};
        this.possibleColors = ['#EEE','#CCC','#AAA','#888','#666','#444','#222',
                               '#0EE','#0CC','#0AA','#088','#066','#044','#022' ];

        this.render();

        /*
         *  To support dynamic theming of colors we need to detect what the primary color has been
         *  set to - this is done via a CSS variable. For most purposes the CSS variable is
         *  sufficient - but the network-diagram will dynamically color switch as elements are
         *  selected - so we need the primary color accessible at runtime.
         */
        const styles = window.getComputedStyle(this);
        this.egeria_primary_color_string = styles.getPropertyValue('--egeria-primary-color');
        //console.log("network-diagram: egeria primary color is "+this.egeria_primary_color_string);

        //console.log("network-diagram: ready complete");

    }

    setInstanceRetriever(instanceRetriever) {
        this.instanceRetriever = instanceRetriever;
        //console.log("network-diagram: instanceRetriever set "+this.instanceRetriever);
    }


    selectedModeChanged(newValue,oldValue) {
        if (this.sim === null) {
            // Can be called during initialisation prior to render and the sim being created. In this case ignore.
            return;
        }
        //console.log("selectedModeChanged invoked : selectedMode="+ this.selectedMode);
        //if (newValue !== undefined && newValue !== null) {
        //    console.log("selectedModeChanged newValue="+ newValue);
        //}
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

    inEvtFocusEntityChanged(entityGUID) {
        // The focus is now the entity given.
        // There is nothing to do here - highlighting will be handled asynchronously and the
        // display of details is not the responsibility of this diagram component.
        // For now just log to console....
        //console.log("network-diagram: focus-entity-changed to "+entityGUID);
    }

    inEvtFocusRelationshipChanged(relationshipGUID) {
        // The focus is now the relationship given.
        // There is nothing to do here - highlighting will be handled asynchronously and the
        // display of details is not the responsibility of this diagram component.
        // For now just log to console....
        //console.log("network-diagram: focus-relationship-changed to "+relationshipGUID);
    }



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
                  //console.log("network-diagram: graph-extended processing entity "+entityDigest.label);
                  var newNode = {};
                  newNode.id = entityDigest.entityGUID;
                  newNode.label = entityDigest.label;
                  newNode.gen = entityDigest.gen;
                  newNode.metadataCollectionName = entityDigest.metadataCollectionName;
                  // Add nodes in the current vertical displacement according to gen
                  newNode.x = this.width/2;
                  newNode.y = this.yPlacement(newNode);
                  this.myNodes.push(newNode);
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

                // Look through existing links (myLinks) to find multi-edges and set idx accordingly
                var count = 0;
                for (var l in this.myLinks) {
                    var link = this.myLinks[l];
                    if (link.source === newLink.source && link.target === newLink.target) {
                        count = count+1;
                    }
                }
                newLink.idx = count;
                newLink.gen = relationshipDigest.gen;
                newLink.metadataCollectionName = relationshipDigest.metadataCollectionName;

                // Once the force layout has started we need to specify nodes (not array indexes or ids)
                this.myLinks.push(newLink);
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
                  var idx = this.myNodes.indexOf(nodeToRemove);
                  if (idx !== -1) {
                      // Remove node from myNodes array
                      this.myNodes.splice(idx, 1);
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
                for (var idx=0; idx<this.myLinks.length; idx++) {
                    if (this.myLinks[idx].id === r) {
                        // Note the idx so the link can be removed
                        linkFound = true;
                        break;
                    }
                }
                if (linkFound) {
                    this.myLinks.splice(idx,1);
                }
            }
        }

        this.update_diagram();

        this.outEvtGraphReduced();

    }


    clearGraph() {
        this.myNodes = [];
        this.myLinks = [];
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

        //console.log("network-diagram: render called");

        // Clear any startup text or previous diagram content
        //console.log("network-diagram: clear network diagram");
        this.clearNetworkDiagram();

        //console.log("network-diagram: initialize diagram");
        this.initialize_diagram();

        //console.log("network-diagram: update diagram");
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

        this.myNodes = [];
        this.myLinks = [];
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

        this.sim = d3.forceSimulation(this.myNodes)
            .force('horiz', d3.forceX(width/2).strength(0.01))
            .force('vert', d3.forceY().strength(0.1).y(function(d) {return yPlacement(d);}))
            .velocityDecay(0.6)
            .force('repulsion', d3.forceManyBody().strength(-500))
            .alphaDecay(.0005)
            .velocityDecay(0.6)
            .on('tick',this.tick.bind(this))
            .force('link', d3.forceLink().links(this.myLinks)
                .id(this.nodeId)
                .distance(this.link_distance)
                .strength(function(d) { return ls(d);})) ;


        // define arrowhead for links
        this.svg.append("svg:defs").selectAll("marker")
            .data(["end"])
            .enter().append("svg:marker")
            .attr("id", String)
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 25)  // The marker is 10 units long (in x dir) and nodes have radius 15.
            .attr("refY", 0)
            .attr("markerWidth", 4)
            .attr("markerHeight", 4)
            .style("stroke",this.egeria_primary_color_string)
            .style("fill",this.egeria_primary_color_string)
            .attr("orient", "auto")
            .attr("xoverflow", "visible")
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
        //console.log("network-diagram: placing node from gen "+d.gen+" at y "+y+" (perGen is "+perGen+")")

        return y;

    }




    logNodesToConsole() {
        if (this.myNodes !== undefined) {
            this.myNodes.forEach(node => {
                console.log("Node -> "+node.id+" x,y -> "+node.x+","+node.y+" label -> "+node.label+" gen -> "+node.gen);
            });
        }
    }


    logLinksToConsole() {
        if (this.myLinks !== undefined) {
            this.myLinks.forEach(link => {
                console.log("Link -> "+link.id+" source -> "+link.source+" target -> "+link.target);
                console.log("Link -> "+link.id+" source.id -> "+link.source.id+" target.id -> "+link.target.id);
                console.log("Link -> "+link.id+" source.label -> "+link.source.label+" target.label -> "+link.target.label);
            });
        }
    }



    update_diagram() {
        // In order to get the nodes always in front of the links, the nodes need to be re-added to the svg
        // So whenever links changes we need to re-generate all the nodes. Or you do select and re-gen of all
        // nodes in the tick function. The former is probably more efficient.

        console.log('network-diagram: update_diagram');

        // refresh the sim's data
        this.sim.nodes(this.myNodes);  // nodes first - we want the positions to refresh
        this.sim.force('link').links(this.myLinks);  // update the links

        this.updateLinks();
        this.updateNodes();

        this.sim.alpha(0.1);
        this.sim.restart();

    }


    nodeId(d) {
       return d.id;
    }

    getNode(id) {
      return ( this.myNodes.filter(obj => { return obj.id === id  })[0] );
    }

    /*
     * This function saves the current SVG as a PNG image file.
     */
    saveImage() {

        var html = this.svg
            .attr("version", 1.1)
            .attr("xmlns", "http://www.w3.org/2000/svg")
            .node().parentNode.innerHTML;

        var imgsrc = 'data:image/svg+xml;base64,'+ btoa(html);
        var img = '<img src="'+imgsrc+'">';

        var svgdata = d3.select(this.shadowRoot.querySelector('#svgdataurl')).html(img);
        svgdata.html(img);

        var canvas = this.$.canvas;
        var context = canvas.getContext("2d");
        // If you want to reveal the canvas you can set display to block - but beware that it will display in line
        // which displaces other elements - unless you were to locate it at the end of the network-diagram's DOM.
        // canvas.style.display="block";

        var image = new Image;
        image.src = imgsrc;
        var shadowRoot = this.shadowRoot;
        image.onload = function() {
            context.drawImage(image, 0, 0);
            var canvasdata = canvas.toDataURL("image/png");
            var pngimg = '<img src="'+canvasdata+'">';
            d3.select(shadowRoot.querySelector('#pngdataurl')).html(pngimg);
            var a = document.createElement("a");
            a.download = "sample.png";
            a.href = canvasdata;
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

        //this.logNodesToConsole();

        var node_radius = this.node_radius;
        var width       = this.width;
        var height      = this.height;
        var svg         = this.svg;

        // update the visual rendering of the nodes

        // Keep nodes 'on top' of links
        // Re-drawing nodes allows us to change visual attributes like colour too
        svg.selectAll(".node").remove()

        this.node = svg.selectAll(".node")
            .data(this.myNodes)

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

        //this.logLinksToConsole();

        var svg = this.svg;
        var path_func = this.path_func.bind(this);

        this.link = svg.selectAll(".link")
             .data(this.myLinks)
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


    // Request to change focus when node clicked
    nodeClicked(guid) {
        // We need to request a focus change...
        console.log("network-diagram: node-clicked, id is "+guid);
        this.outEvtChangeFocusEntity(guid);
    }

    // Request to change focus when edge clicked
    edgeClicked(guid) {
        // We need to request a focus change...
        console.log("network-diagram: edge-clicked, id is "+guid);
        this.outEvtChangeFocusRelationship(guid);
    }


    /*
     *  This function is called to determine whether the instance is the currently selected item (and therefore
     *  should be visually highlighted).
     */
    highlighted(d) {
        var selectedGUID = null;
        var focusCat = this.instanceRetriever.getFocusInstanceCategory();

        switch (focusCat) {

        case "Entity":
            var selectedEntity = this.instanceRetriever.getFocusEntity();
            if (selectedEntity !== null) {
                selectedGUID = selectedEntity.entityDetail.guid;
            }
            break;

        case "Relationship":
            var selectedRelationship = this.instanceRetriever.getFocusRelationship();
            if (selectedRelationship !== null) {
                selectedGUID = selectedRelationship.relationship.guid;
            }
            break;
        }

        if (d.id === selectedGUID)
            return true;
        else
            return false;
    }


    /*
     *  This function is called to determine whether the color of a node - it may be selected or not, and the user may
     *  have opted to show nodes from different repositories in different colors.
     *  The selected node always appears in egeria-primary-color.
     */
    nodeColor(d) {
        var selectedGUID = null;

        var focusCat = this.instanceRetriever.getFocusInstanceCategory();
        switch (focusCat) {

            case "Entity":
                var selectedEntity = this.instanceRetriever.getFocusEntity();
                if (selectedEntity !== null) {
                    selectedGUID = selectedEntity.entityDetail.guid;
                }
                break;

            case "Relationship":
                var selectedRelationship = this.instanceRetriever.getFocusRelationship();
                if (selectedRelationship !== null) {
                    selectedGUID = selectedRelationship.relationship.guid;
                }
                break;
        }



        var selectedEntity;

        var selectedEntity = this.instanceRetriever.getFocusEntity();
        if (selectedEntity !== null) {
            selectedGUID = selectedEntity.entityDetail.guid;
        }

        /*
         * If instance is the current focus paint it in egeria primary color
         */
        if (d.id === selectedGUID) {
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
                    console.log("Ran out of available colors for repositories!");
                    /*
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

}


window.customElements.define('network-diagram', NetworkDiagram);