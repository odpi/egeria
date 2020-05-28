/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


import React, { useRef, useState, useEffect, useContext }  from "react";

import PropTypes                                           from "prop-types";

import * as d3                                             from "d3";

import * as DiagramUtils                                   from "./DiagramUtils";

import { InstancesContext }                                from "../../contexts/InstancesContext";




export default function Diagram(props) {

  // Access instancesContext to get access to focus information
  const instancesContext = useContext(InstancesContext);
  
  /*
   * We need a force-directed sim, which should be created on load of the component.
   * It should be restarted when the data is updated. 
   * We mustn't make it part of the Diagram's state else we'll run into rendering loop issues.
   * So make it soft and initialise it and check it on the calls to useEffect.
   */

  let loc_force;


  /*
   * layoutMode can be switched between Temporal and Proximal - the default is Temporal.
   * Temporal layout mode will cascade generations down the diagram.
   * Proximal layout mode allows the graph to organise itself based on connections.
   */
  const [layoutMode, setLayoutMode] = useState("Temporal");

  const changeLayoutMode = () => {
   
    if (layoutMode === "Proximal") {
      setLayoutMode("Temporal");
    }      
    else {
      setLayoutMode("Proximal"); 
    }
    /*
     * Just a small nudge...
     */
    loc_force.alpha(0.1);
    loc_force.restart();
  }

  const [pinningOption, setPinningOption] = useState(true);
  const pinningRef = useRef();
  pinningRef.current = pinningOption;
  
  
  const width                       = 1070;
  const height                      = 1100;
  const node_radius                 = 10;
  const node_margin                 = 20;  // note that this is only the basis for a computed margin
  const link_distance               = 200;
  const egeria_primary_color_string = "#71ccdc";
  const possibleColors              = ['#EEE','#CCC','#AAA','#888','#666','#444','#222',
                                       '#0EE','#0CC','#0AA','#088','#066','#044','#022' ];

  /*
   *  To support dynamic theming of colors we need to detect what the primary color has been
   *  set to - this is done via a CSS variable. For most purposes the CSS variable is
   *  sufficient - but the network-diagram will dynamically color switch as elements are
   *  selected - so we need the primary color accessible at runtime. We also need to
   *  set up a slightly dark shade of the primary color for text labels against white
   *  background.
   */

   /*
    * NOTE - colors are currently hard-coded rather than being retrieved from computed styles
    * styles = window.getComputedStyle(this);
    * egeria_primary_color_string = styles.getPropertyValue('--egeria-primary-color');
    */
   const splitPrimary             = egeria_primary_color_string.split(' ');
   const strippedPrimary          = splitPrimary[splitPrimary.length-1];   
   const egeria_text_color_string = DiagramUtils.alterShade(strippedPrimary, -20);

  
  /*
   * The use of diagramFocusGUID is to ensure that the instancesContext focus is visible in the 
   * Diagram component. A function can either use it, or call instancesContext.focus directly.
   */ 
  let diagramFocusGUID;


  /*
   * Color mappings are only maintained for the duration of the lifecycle of the Diagram - they are
   * not stateful across renders. 
   */
  let repositoryToColor = {};
  let colorToRepository = {};


  /*
   * Need to retain d3 across calls to render diagram
   */
  const d3Container = useRef(null);

  /*
   * Databind the latest links and add/remove SVG elements accordingly
   */
  const updateLinks = () => {
       
    const svg = d3.select(d3Container.current);
    const links = svg.selectAll(".link")
      .data(props.links)

    links.exit().remove();

    const enter_set = links.enter()
      .append("g")
      .attr('class', 'link')
      .attr("cursor", "pointer")
      .attr('x1', function(d) {return d.source.x;} )
      .attr('y1', function(d) {return d.source.y;} )
      .attr('x2', function(d) {return d.target.x;} )
      .attr('y2', function(d) {return d.target.y;} )
      ;  

    enter_set.append('text')
      .attr('class',             'edgeLabel')
      .attr("fill",              "#CCC")
      .attr("stroke",            "none")
      .attr("font-family",       "sans-serif")
      .attr("font-size",         "10px")
      .attr("stroke-width",      0)     
      .attr("dominant-baseline", function(d) { return (d.source.x > d.target.x) ? "baseline" : "hanging"; } )
      .attr("x",                 function(d) { return DiagramUtils.path_func(d, link_distance).midpoint.x; } )
      .attr("y",                 function(d) { return DiagramUtils.path_func(d, link_distance).midpoint.y; } )      
      .attr('text-anchor',       'middle')
      .text( function(d) { return d.label; } )
      .on("click",                d => { linkClicked(d.id); })  // The link's id is the relationshipGUID      
      .clone(true)
      .lower()
      .attr("stroke-linejoin",    "round")
      .attr("stroke-width",       3)
      .attr("stroke",             "white") ;
  
    enter_set.append('path')
       .attr('class',             'line')
       .attr("cursor",            "pointer")
       .attr('d',                 function(d) { return DiagramUtils.path_func(d, link_distance).path; })
       .attr("fill",              "none")
       .attr('stroke',            egeria_primary_color_string)
       .attr('stroke-width',      '2px')
       // Only place a marker if the link is not reflexive
       .attr("marker-end",        function(d) { return (d.source===d.target)?"none":"url(#end)";})
       .on("click",               d => { linkClicked(d.id); })  // The link's id is the relationshipGUID  
       .lower()
       ;
  
    links.merge(enter_set);
     
  
  };


 

  /*
   * Databind the latest nodes and add/remove SVG elements accordingly
   */
  const updateNodes = () => {  

    if (props.nodes) {
    
      if (props.nodes.length > 0) {        

        /*
         * Assign starting position to any node that doesn't already have one...
         * If a node is attached to a link, bias the starting position to achieve
         * a left-right flow which is easier for the user. Process the linked
         * nodes first, then mop up any that are still adrift.
         */
        props.links.forEach( l => {
          if (l.source.x === null || l.source.y === null) {
            l.source.x = width/4.0;
            l.source.y = DiagramUtils.yPlacement(l.source, height, props.numGens);
          }
          if (l.target.x === null || l.target.y === null) {
            l.target.x = 3.0 * width/4.0;
            l.target.y = DiagramUtils.yPlacement(l.target, height, props.numGens);
          }      
        });
        /* Catch any disconnected nodes */
        props.nodes.forEach( n => {
          if (n.x === null || n.y === null) {
             n.x = width/2;
             n.y = DiagramUtils.yPlacement(n, height, props.numGens);
          }          
        });      
      
      }
    }
  
    const svg = d3.select(d3Container.current);

    svg.selectAll(".node").remove()

    const nodes = svg.selectAll(".node")
      .data(props.nodes)
          
    nodes.exit().remove();

    const enter_set = nodes.enter()
      .append("g")
      .attr('class',  'node')
      .attr("cursor", "pointer")
      .attr('cx',     function(d) {return d.x;} )
      .attr('cy',     function(d) {return d.y;} )
      .call(d3.drag()
          .container(d3Container.current)
          .on("start", dragstarted)
          .on("drag",  dragged)
          .on("end",   dragended) )    
      ;

    enter_set.append('line')
      .attr('x1',            0)
      .attr('y1',            0)
      .attr('x2',            0)
      .attr('y2',            node_radius*2.0)
      .attr('stroke',       egeria_primary_color_string)       
      .attr('stroke-width', '2px')        
      .on("click", d => { if (d3.event.shiftKey) {unpin(d);} else {nodeClicked(d.id); }})   // The node's id is the entityGUID
      ;

    enter_set.append('circle')
      .attr('r',            node_radius)
      .attr('stroke',       egeria_primary_color_string)       
      .attr('stroke-width', '2px')
      .attr('fill',         'white')      
      .on("click", d => { if (d3.event.shiftKey) {unpin(d);} else {nodeClicked(d.id); }})  // The node's id is the entityGUID
      ;

    enter_set.append('text')  
      .attr("fill",         "#444")
      .text( function(d) { return d.label; } )
      .attr("font-family",  "sans-serif")
      .attr("font-size",    "12px")
      .attr("stroke-width", "0")
      .attr("dx",           20)
      .attr("dy",           ".35em")
      .on("click", d => { if (d3.event.shiftKey) {unpin(d);} else {nodeClicked(d.id); }})   // The node's id is the entityGUID
      ;

    /* Check all labels are up to date.
     * This does not yet include the enter_set as they have only just been added
     * and are known to have correct labels
     */
    nodes.select('text')
      .text( function(d) { return d.label; } ) ;

    nodes.merge(enter_set);
  };

 
  const dragstarted = (d) => {
    // NO OP
  }

  const dragged = (d) => {
    if (pinningRef.current) {      
      d.fx = d3.event.x, d.fy = d3.event.y;
    }
    else {     
      d.x = d3.event.x, d.y = d3.event.y;
    }
    /*
     * Provide a gentle nudge
     */
    loc_force.alpha(0.2).restart();
  }
  

  const dragended = (d) => {
    // NO OP
  }

  const unpin = (d) => {
    d.fx = null, d.fy = null;
  }

 
  const nodeClicked = (guid) => {

    props.onNodeClick(guid);
  }

  const linkClicked = (guid) => {

    props.onLinkClick(guid);
  }


  /*
   *  This function is called to determine the color of a node - if the node is selected then a decision
   *  will already have been made about color. So assume it is not selected. Nodes from different repositories 
   *  are filled using different colors.
   */
  const nodeColor = (d) => {

    /*
     * Look up repository name in repositoryColor map, if not found assign next color.
     * This actually assigns gray-shades, starting with the #EEE and darkening by two
     * stops for each new repository found - e.g. #AAA -> #888. There are therefore 8 shades
     * that can be allocated, by which time we are at 100% black. If this number proves to
     * be insufficient, we can shorten the two-stops or assign a single hue, e.g. green.
     */
    let colorString = repositoryToColor[d.metadataCollectionName];
    if (colorString !== undefined) {
      return colorString;
    }
    else {

      // Assign first available color
      let assigned = false;
      for (let col in possibleColors) {
        colorString = possibleColors[col];
        if (colorToRepository[colorString] === undefined) {
          // Color is available
          repositoryToColor[d.metadataCollectionName] = colorString;
          colorToRepository[colorString] = d.metadataCollectionName;
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
        const col = '#000';
        this.repositoryToColor[d.metadataCollectionName] = col;
        return col;
      }
    }
  }



  /*
   * tick function is responsible for updating SVG attributes to match the latest 
   * positions of the nodes, as updated by the force sim.
   */
  const tick = () => {

    const focusGUID = diagramFocusGUID;
   
    const svg = d3.select(d3Container.current);

    const nodes = svg.selectAll(".node");

    // Keep nodes in the viewbox, with a safety margin so that (curved) links are unlikely to stray...
    nodes.attr('cx',function(d) { return d.x = Math.max(node_margin, Math.min(width  - 8 * node_margin, d.x)); });
    nodes.attr('cy',function(d) { return d.y = Math.max(node_margin, Math.min(height -     node_margin, d.y)); });
    nodes.attr('transform', function(d) { return "translate(" + d.x + "," + d.y + ")";});
    

    /*
     * Highlight a selected node, if it is the instance that has been selected or just loaded 
     * (in which case it is selected)
     */

    nodes.selectAll('circle')
      .attr("fill", d => (d.id === focusGUID) ? egeria_primary_color_string : nodeColor(d) );     

      nodes.selectAll('line')      
      .attr('stroke', d => (d.fx !== undefined && d.fx !== null) ? egeria_primary_color_string : "none");

    nodes.selectAll('text')
      .attr("fill", d => (d.id === focusGUID) ? egeria_text_color_string : "#444" );
    
    const links = svg.selectAll(".link")

    links.selectAll('path')
       .attr('d', function(d) { return DiagramUtils.path_func(d, link_distance).path; })
       .lower();

    links.selectAll('text')
       .attr("x", function(d) { return d.x = DiagramUtils.path_func(d, link_distance).midpoint.x; } )
       .attr("y", function(d) { return d.y = DiagramUtils.path_func(d, link_distance).midpoint.y; } )
       .attr("fill", function(d) { return ( (d.id === focusGUID) ? egeria_text_color_string : "#888" );} )
       .attr("dominant-baseline", function(d) { return (d.source.x > d.target.x) ? "baseline" : "hanging"; } )
       .attr("transform" , d => `rotate(${180/Math.PI * Math.atan((d.target.y-d.source.y)/(d.target.x-d.source.x))}, ${d.x}, ${d.y})`)
       .attr("dx", d => { ((d.target.y-d.source.y)<0)? 100.0 : -100.0; })
       .attr("dy", d => {
           ((d.target.x-d.source.x)>0)?
           20.0 * (d.target.x-d.source.x)/(d.target.y-d.source.y) :
           20.0 * (d.source.x-d.target.x)/(d.target.y-d.source.y) ;
           });
            
  };


  const createSim = () => {

    if (!loc_force) {
      loc_force = d3.forceSimulation(props.nodes)
        .force('horiz', d3.forceX(width/2).strength(0.01))
        .force('repulsion', d3.forceManyBody().strength(-500))        
        .alphaDecay(.002)
        .alphaMin(0.001)
        .alphaTarget(0.0005)
        .velocityDecay(0.8)            
        .force('link', d3.forceLink()
          .links(props.links)
          .id(DiagramUtils.nodeId)
          .distance(link_distance)
          .strength(function(d) { return DiagramUtils.ls(d);})) ;      
          
      if (layoutMode === "Temporal") {            
        loc_force.force('vert', d3.forceY().strength(0.1).y(function(d) {return DiagramUtils.yPlacement(d, height, props.numGens);}));
      }      
      else {            
        loc_force.force('vert', d3.forceY(height/2).strength(0.001))
      }

      loc_force.on('tick', tick);  // this does no good if you setForce(loc_force)      
    }
  };

  const createMarker = () => {
    // Define arrowhead for links
    const svg = d3.select(d3Container.current); 
    svg.append("svg:defs").selectAll("marker")
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

  };

  const startSim = () => {
    if (loc_force) {
      loc_force.restart();
    }
  };
  
  const updateData = () => {
    updateLinks();
    updateNodes();
  };

  const setDiagramFocus = () => {    
    diagramFocusGUID = instancesContext.focus.instanceGUID;
  };

  const updatedPinningOption = () => {
   
    setPinningOption(!pinningOption);
    if (pinningOption) {
      /*
       * If pinning was true, nodes may be pinned, so ensure all nodes are unpinned
       */
      props.nodes.forEach(n => unpin(n));

    }
  };


  useEffect(
    () => {      
      if ( d3Container.current ) {       
        createSim();        
        createMarker();                           
        startSim();
      }
      if ( props.nodes || props.links) {   
        updateData();
        startSim();
      }
      if ( instancesContext.focus ) {   
        setDiagramFocus();     
      }
    },
  
    [d3Container.current, props.nodes, props.links, instancesContext.focus, props.onNodeClick, props.onLinkClick, layoutMode]
  )
 
 
  return (
    <div>

      <div>
        <label htmlFor="layoutMode">Layout : </label>
        <input type="radio" 
               id="modeTemporal" 
               name="layoutMode" 
               value="Temporal" 
               checked={layoutMode === "Temporal"}
               onChange={changeLayoutMode}/>
        <label htmlFor="modeTemporal">Time-based</label>
        
        <input type="radio" 
               id="modeProximal" 
               name="layoutMode" 
               value="Proximal" 
               checked={layoutMode === "Proximal"}
               onChange={changeLayoutMode} />
        <label htmlFor="modeProximal">Proximity-based</label>

      </div>

      <br />

      <div>

        <label htmlFor="cbPinning">Pin dragged entities : </label>
        <input type="checkbox" 
               id="cbPinning" 
               name="cbPinning" 
               checked={ pinningOption }
               onChange={updatedPinningOption} 
               value={ pinningOption }  />
      </div>
      
      <br />
      
      <div>
        <svg className="d3-component"
             width={width} 
             height={height} 
             ref={d3Container}>        
        </svg>              
      </div>

    </div>
    
  );
}


Diagram.propTypes = {  
  nodes       : PropTypes.array,
  links       : PropTypes.array,
  numGens     : PropTypes.number,
  onNodeClick : PropTypes.func,
  onLinkClick : PropTypes.func
  
};
