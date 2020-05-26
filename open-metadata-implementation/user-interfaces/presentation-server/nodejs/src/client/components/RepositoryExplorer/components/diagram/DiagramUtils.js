/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

/*
 * DiagramUtils provides a module for utility functions used by the Diagram class
 * These are all stateless workers that perform things like layout calculations.
 * 
 */

import * as d3  from "d3";  // using d3.path, ...




  /*
   * Generic accessor function for nodes
   */
  export function nodeId(d) {
    return d.id;
  };


  export function yPlacement(d, height, numberOfGens) {
    let y = 0;
    let perGen = 0;
    const ymin = height / 6;
    const ymax = 5 * (height / 6);
   

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
    if (numberOfGens <= 5) {
        perGen = (ymax - ymin) / 4;
    }
    else {
        perGen = (ymax - ymin) / (numberOfGens - 1);
    }
    y = ymin + (d.gen-1) * perGen;

    return y;
  }

  export function ls (d) {
    var gen_s = d.source.gen;
    var gen_t = d.target.gen;
    var gen_diff = gen_t - gen_s;
    var mag_diff = Math.max(1,Math.abs(gen_diff));
    return 1.0 / mag_diff;
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
    export function path_func(d, link_distance) {

      let returnMap = {};
      let midpoint = {};

      const path = d3.path();

      if ( d.source.id == d.target.id ) {

          /*
           * Reflexive link
           */
          const cp_offset = 0.15 + (d.idx * 0.1);   /* Indexing is for separation of multi-links */
          const base_rad  = link_distance;          /* Sets base_rad for innermost link = link_distance for
                                                     * non-reflexive links; this is subjective but results
                                                     * in sensible radii for reflexive links
                                                     */
          const link_rad  = base_rad * cp_offset;
          path.moveTo(d.source.x,d.source.y);
          path.arc(d.source.x+link_rad,d.source.y,link_rad,Math.PI,0.999*Math.PI);
          midpoint.x      = d.source.x+1.7*link_rad;    /* Place the label away from the node and its label... */
          midpoint.y      = d.source.y-0.7*link_rad;;
      }
      else {

          /*
           * Non-reflexive link
           */
          
          const dx        = d.target.x - d.source.x;
          const dy        = d.target.y - d.source.y;
          let mid         = {};
          mid.x           = d.source.x + dx/2.0;
          mid.y           = d.source.y + dy/2.0;
          const theta     = Math.atan(dy/dx);
          const psi       = dx < 0 ? theta + Math.PI/2.0 : theta - Math.PI/2.0;
          const ux        = Math.cos(psi);
          const uy        = Math.sin(psi);
          const len       = Math.sqrt(dx**2 + dy**2);
          const cp_offset = 0.2 * (d.idx+1);  
          const cp_dist   = cp_offset * len;
          const cp_x      = cp_dist * ux;
          const cp_y      = cp_dist * uy;
          let cp          = {};
          cp.x            = mid.x + cp_x;
          cp.y            = mid.y + cp_y;
          path.moveTo(d.source.x,d.source.y);
          path.quadraticCurveTo(cp.x,cp.y,d.target.x,d.target.y);
          midpoint.x      = mid.x + 0.5 * cp_x;
          midpoint.y      = mid.y + 0.5 * cp_y;          
      }

      returnMap.path = path.toString();      
      returnMap.midpoint = midpoint;
      return returnMap;

  }

 


  export function  alterShade(color, percent) {

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


