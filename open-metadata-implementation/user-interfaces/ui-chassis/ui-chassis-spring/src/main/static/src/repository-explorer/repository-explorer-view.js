/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */
import "../../node_modules/@polymer/paper-input/paper-input.js";
import "../../node_modules/@polymer/paper-material/paper-material.js";
import "../../node_modules/@polymer/iron-form/iron-form.js";
import "../../node_modules/@polymer/iron-a11y-keys/iron-a11y-keys.js";
import "../../node_modules/@polymer/paper-button/paper-button.js";
import "../../node_modules/@polymer/paper-styles/paper-styles.js";
import "../../node_modules/@polymer/paper-dropdown-menu/paper-dropdown-menu.js";
import "../../node_modules/@polymer/paper-listbox/paper-listbox.js";
import "../../node_modules/@polymer/paper-item/paper-item.js";
import "../../node_modules/@polymer/paper-menu-button/paper-menu-button.js";
import "../../node_modules/@polymer/paper-input/paper-input-behavior.js";
import "../../node_modules/@polymer/paper-dialog/paper-dialog.js";
import "../../node_modules/@polymer/paper-dialog-behavior/paper-dialog-behavior.js";
import "../../node_modules/@polymer/iron-localstorage/iron-localstorage.js";
import "../../node_modules/@polymer/iron-ajax/iron-ajax.js";
import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid-selection-column.js";
import "../../node_modules/@vaadin/vaadin-grid/vaadin-grid-sort-column.js";
import "../../node_modules/@vaadin/vaadin-text-field/vaadin-text-field.js";
import "../../node_modules/@vaadin/vaadin-button/vaadin-button.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";

import '../token-ajax.js';
import "../../node_modules/@polymer/paper-checkbox/paper-checkbox.js";
import "../../node_modules/@polymer/paper-radio-button/paper-radio-button.js";
import "../../node_modules/@polymer/paper-radio-group/paper-radio-group.js";

import './type-manager.js';
import './connection-manager.js';
import './instance-retriever.js';
import './filter-manager.js';
import './diagram-manager.js';
import './details-panel.js';
import './graph-controls.js';

import './rex-styles.js';


/**
*
* TypeExplorerView is the top level web component for the Type Explorer UI component.
* It implements the controller component of the design.
* It is responsible for creating the ConnectionManager, TypeManager components
* It is responsible for creating the FocusManager, DiagramManager and DetailsPanel components
*/

class RepositoryExplorerView extends mixinBehaviors([AppLocalizeBehavior], PolymerElement) {
    static get template() {
        return html`

            <style include="rex-styles">

            </style>

            <body>

                <rex-type-manager id="rexTypeManager"></rex-type-manager>

                <div>

                    <!-- TOP -->

                    <div id="top" style="position:absolute; left:0px; top:100px; height:185px; width:1600px; ">
                        <div style="position:absolute; left:0px; top:0px; height:180px; width:340px; padding: 0px 20px;">
                            <rex-connection-manager id="rexConnectionManager" type-manager="[[theTypeManager]]"></rex-connection-manager>
                        </div>
                        <div style="position:absolute; left:340px; top:0px; height:180px; width:300px; ">
                            <instance-retriever id="rexInstanceRetriever" connection-manager="[[theConnectionManager]]" type-manager="[[theTypeManager]]"></instance-retriever>
                        </div>
                        <div style="position:absolute; left:1270px; top:0px; height:180px; width:100px; ">
                             <paper-button
                                 style="padding:10px; text-align:center; text-transform:none"
                                 id = "aboutButton"
                                 raised
                                 on-click="aboutRex">
                                 Help
                             </paper-button>
                        </div>
                        <hr style="position:absolute; width:1600px; left:0px; top:182px;">
                    </div>

                    <!-- LHS -->

                    <div id="lhs" style="position:absolute; left:0px; top:290px; height:1200px; width:400px; overflow-x: hidden;  overflow: auto; ">
                        <!-- <rex-connection-manager id="rexConnectionManager" type-manager="[[theTypeManager]]"></rex-connection-manager> -->
                        <!-- <instance-retriever id="rexInstanceRetriever" connection-manager="[[theConnectionManager]]" type-manager="[[theTypeManager]]"></instance-retriever> -->
                        <graph-controls id="graphControls" connection-manager="[[theConnectionManager]]" instance-retriever="[[theInstanceRetriever]]"></graph-controls>
                        <hr style="position:relative; width:400px; left:0px; ">
                        <rex-details-panel id="rexDetailsPanel" type-manager="[[theTypeManager]]" instance-retriever="[[theInstanceRetriever]]"></rex-details-panel>
                    </div>

                    <!-- RHS -->

                    <div id="rhs" style="position:absolute;left:400px;top:290px; height:1200px; width:1200px; background-color:#FFFFFF">
                        <rex-diagram-manager id="rexDiagramManager" style="overflow:auto;" type-manager="[[theTypeManager]]" instance-retriever="[[theInstanceRetriever]]"></rex-diagram-manager>
                    </div>

                </div>

                <div>
                    <paper-dialog id="aboutDialog" style=" height:600px; width:800px; padding:20px 0px; ">

                    <!-- This section provides usage information -->

                    <div style="font-size:14px;">
                    <b>
                    Using Repository Explorer (Rex)
                    </b>
                    </div>
                    <div style="height:450px; width:750px; overflow:auto;">
                        <p>
                        The Repository Explorer (Rex) can help visualize the metadata in a repository.
                        It uses OMRS (repository services) interfaces in which there are 3 categories of metadata objects - these are
                        entities, relationships and classifications. The types within each category depend on the set of types supported
                        by the chosen repository. Rex automatically reads the type information from the repository and works within the
                        set of supported types.
                        </p>
                        <b>
                        Set the Repository Server:
                        </b>
                        <p>
                        To get started Rex needs the name and URL of the repository server that you wish to explore.
                        Enter these details in the "Server Name" and "Server URL Root" fields and press the 'Connect' button.
                        Rex will attempt to request the type information from the server so it can populate things like
                        the search filters. Below the server input fields, you will see a status message message.
                        If the connection to the server was successful you should see the message change to "Server OK".
                        If Rex could not connect to the server it will display a warning message and ask you to check the details and retry.
                        </p>
                        <p>
                        Rex does not actually maintain a connection to the server - but each time it needs to get something from the
                        server it will use the server details you have set.
                        </p>
                        <b>
                        Get some initial metadata:
                        </b>
                        <p>
                        Rex needs an object as the starting point of a traversal. You can retrieve a starting object either by entering a
                        GUID or by performing a Search. There are more tips on searching below.
                        You can start with either an entity or a relationship - use whichever is most convenient.
                        The object will appear in the diagram, and its details will be displayed in the details panel on the left.
                        <p>
                        Entities are drawn as circles. Relationships are drawn as lines connecting two entities.
                        Classifications are not shown in the diagram. To see the classifications associated with an entity, refer to the details
                        panel when the entity is selected.
                        <p>
                        You can select an object in the
                        diagram by clicking on it. Whenever an entity or relationship is selected, its details will be shown in the details panel
                        and the selected object will be highlighted in the diagram.
                        <p>
                        An object that is not selected is filled using a color that reflects its home repository.
                        Objects with the same home repository will be shown using the same color.
                        The selected object is highlighted - meaning it will be filled using a different color.
                        The default is Egeria 'aqua' (pale blue), but this can be changed depending on the color scheme that has been
                        applied.
                        <p>
                        If you want to deselect the selected object, click on it again. This can be useful if you want to view the graph with all
                        objects shown in their 'normal' colors.
                        </p>
                        <b>
                        Traverse to find related metadata:
                        </b>
                        <p>
                        When you have a starting object in the diagram, you can use the Explore button to retrieve further objects that are
                        related to the selected object. Rex will always traverse from an entity, so if you retrieved a relationship you will
                        need to click on one of its entities in order to traverse.
                        When you traverse, Rex will explore the neighborhood around the selected entity. It does this by traversing outward
                        along the available relationships to find adjacent entities. This process can be repeated to enlarge the graph of
                        entities and relationships that are displayed.
                        <p>
                        To provide some control about which relationships and entities to include in the traversal, Rex allows you to
                        apply filters before actually performing the traversal. When you press the Explore button, Rex will display the number
                        of relationships and entities (by type) that could be included in the displayed graph. You can inspect the
                        counts and filter by type so that Rex will only retrieve the objects you are really interested in. There is more about
                        filtering below under "Traversal Filtering".
                        </p>
                        <b>
                        Search:
                        </b>
                        <p>
                        The search utility in Rex is fairly basic. It allows you to enter a text string and optionally apply an
                        entity, relationship or classification filter, which limits the search to one particular type. The classification
                        filter is not yet enabled, but that capability should be added soon.
                        <p>
                        The search utility does not include property-based searching, it is just a text-based search. It therefore relies
                        on objects having string-type attributes that can be searched. If an object is of a type that has no string-type
                        attributes then it will not be found by the search. It is possible that property-based searching could be added,
                        but it is not available yet.
                        <p>
                        When you enter a string as search text, it can include regular expression characters. Beware that the level of support
                        for regular expressions varies between repositories. If you are searching an Egeria graph repository or in-memory
                        repository, then you have more freedom than you might have with some other repositories.
                        <p>
                        If the text you are searching for contains special characters, you need to literalise the string.
                        <p>
                        For example, suppose there are two Assets called asset-qn001 and asset-(qn001).
                        If you type asset-qn001 in as the search text, the first asset (only) will match.
                        If you type asset-(qn001) in as the search text, the first asset (only) will match.
                        If you type \Qasset-(qn001)\E in as the search text, the second asset (only) will match.
                        This is because the parentheses are special characters.
                        This example was tested using the Egeria graph repository.
                        </p>
                        <b>
                        Traversal Filtering:
                        </b>
                        <p>
                        When you press Explore to expand the neighborhood around an entity, a dialog box will show how many entities and
                        relationships might be included. The number of occurrences of each type of entity or relationship are shown alongside the type
                        name. You can select which types to include in the traversal. This dialog box also shows numbers of classifications, by
                        type - this refers to the classifications applied to the neighboring entities. If you wish to include a relationship a
                        neighboring entity - you need to select both the relationship type and the entity type. Although this may seem like more work
                        it allows you to be very specific about which relationships to traverse. If there are relationships of the same type to
                        entities of different types, or vice versa, the ability to independently select relationship and entity type enables finer
                        grain traversal.
                        </p>
                        <b>
                        Adding to the graph:
                        </b>
                        <p>
                        If after a number of traversals you realize that you would like to perform an additional traversal from an entity you
                        visited at an earlier stage, just go back select the entity from which you want to perform an additional traversal. Then press
                        the Explore button again. You can set the filters to include the types that you would like to add to the graph.
                        </p>
                        <b>
                        Undoing a change:
                        </b>
                        <p>
                        If you realize that you have added things to the graph that you actually do not want, you can use the Undo button to undo
                        the most recent change. The Undo button can be used repeatedly to unwind back to a state that contains objects you want to
                        keep; you can then traverse from this state to add more objects if you want.
                        </p>
                        <b>
                        Clearing the graph:
                        </b>
                        <p>
                        The Clear button will clear the graph diagram, the GUID input field and the details panel. It effectively resets Rex to the
                        state it was in  when the page was first loaded.
                        </p>
                        <b>
                        Diagram layouts:
                        </b>
                        <p>
                        There is only one type of diagram in the diagram selector - called 'Instance Diagram'. Other types of diagram may be added later.
                        The Instance Diagram is a visualization of the traversed graph, in which entities and relationships are drawn as circles and arcs.
                        Each entity or relationship is labelled according to Rex's built-in labelling scheme, which attempts to find a concise, meaningful
                        and hopefully unique label, based on the properties of the entity or relationship. The GUID (globally unique id) associated with
                        an instance is the best way to identify it, but it is not particularly convenient or memorable. Rex includes the GUID in the details
                        panel and in the traversal history to help to uniquely identify an instance.
                        <p>
                        There are two layout settings within the Instance Diagram, that can be selected using the radio button in the top-left corner of
                        the diagram:
                        <ul>
                        <li>Time-based (default) layout lays objects out starting at the top of the diagram and working vertically downwards with each
                        stage of traversal - so the more recently visited objects will be located toward the bottom of the diagram. Note that the temporal
                        placement is not rigid - if a relationship connects a pair of entities retrieved in different stages of the traversal, it will tend to pull
                        them together, which will distort the temporal sequence. This layout may be helpful while you are building the diagram.
                        <li>Proximity-based layout lays objects out using a straightforward force-directedngraph layout. There is no temporal ordering. This
                        layout may be preferred when you have finished adding to the diagram and would like to lay it out for display or printing.
                        <eul>
                        </p>
                        <b>
                        Tips for using Rex:
                        </b>
                        <ul>
                        <li>
                        Once you have selected the repository server to use and retrieved the first entity or relationship, you may not need to
                        visit the top part of the screen again, so you can scroll down till the diagram and controls are at the top of the browser
                        page.
                        <li>
                        Relationship types do not have many attributes; some have no type-defined attributes at all. If you have trouble searching
                        for a relationship, it may be easier to search for an entity that you believe is connected to the relationship. Alternatively,
                        use another tool to retrieve the relationship GUID and paste that into Rex and use the Get button.
                        <li>
                        At any point during an exploration session you can use the History button to display a dialog that lists where the
                        exploration started and what steps have been used, to arrive at the graph that is currently displayed.
                        <li>
                        At any point during an exploration you can save the current diagram as a .png image file. Before capturing the image,
                        consider clicking on the currently selected object to remove the focus color, if you want to see its home repository
                        color instead.
                        <li>
                        After capturing the history and image, as described above, it is straightforward to copy and paste them into a report
                        document.
                        <li>
                        Rex uses the Metadata Collection API to interrogate the chosen repository. All Egeria repositories support this API, but
                        some functions of the API are optional, so not all repositories support them. For maximum support for graph
                        traversals, if possible choose a repository server that has the Egeria graph repository. Remember that Egeria servers can
                        create reference copies of objects, so you can ask a server with a graph repository about objects that belong to
                        other repositories in the cohort.
                        <li>
                        Rex is not a general graph visualization utility, so it would not be wise to give it a huge graph to draw, either by
                        traversing too many times or by performing a traversal that includes a large number of adjacent entities. Try to limit what
                        you get back from each traversal by using the Traversal Filters. The Traversal Filters only allow you to filter by type,
                        so do not provide very fine-grained selection of relationships or neighboring entities. This is not a limitation of Rex,
                        it is a reflection of the parameters available in the metadata collection interface. To target a particular entity (or
                        small group of entities) within a larger group, it may be better to start with the interesting entity and
                        using traverse from there. It may be possible in future to add a more sophisticated set of traversal filters in Rex, but
                        not all repositories have comprehensive support for the metadata collection interface and Rex can only work with what it can access.
                        </eul>
                        <p>
                        <b>
                        Customization:
                        </b>
                        <p>
                        Rex uses a styled set of colors, so that it can be re-themed. The important colors are defined as CSS variables in the
                        shared-styles.js file. They are called --egeria-primary-color and --egeria-secondary-color.
                        By setting the egeria-primary-color you can achieve different themes.
                        <img>
                        </p>
                    </div>

                    <div class="buttons" style="height:50px; width:750px;">
                        <paper-button dialog-dismiss autofocus>OK</paper-button>
                    </div>

                </paper-dialog>

            </body>

        `;
    }

    static get properties() {
        return {
            theTypeManager: Object,
            theInstanceRetriever: Object,
            theConnectionManager: Object,
            theDiagramManager : Object
        };
    }

    ready() {
        // Call super.ready() first to initialise node hash...
        super.ready();

        this.initialise();

        this.theTypeManager       = this.$.rexTypeManager;
        this.theInstanceRetriever = this.$.rexInstanceRetriever;
        this.theConnectionManager = this.$.rexConnectionManager;
        this.theDiagramManager    = this.$.rexDiagramManager;


        // Because diagram-manager is dynamically creating a custom element (instance-diagram) prior to us
        // completing this top level ready function, the diagram manager needs to set the i-r for the i-d
        // and cannot have done so until now....
        this.theDiagramManager.setInstanceRetriever(this.theInstanceRetriever);

        //console.log("Repository-explorer-view ready completed");
    }

    initialise() {

        //console.log("rex initialise");

        // This class implements the event listeners that orchestrate via function calls to the child components.
        // The events are as follows:

        //console.log("rex add listeners for types-loaded");
        this.addEventListener('types-loaded', function (e) {
            this.$.rexConnectionManager.inEvtTypesLoaded();
            this.$.rexInstanceRetriever.inEvtTypesLoaded();
        });

        this.addEventListener('types-not-loaded', function (e) {
            // Generate an alert for the error condition
            alert( "Event :" + 'types-not-loaded' + ' from ' + e.detail.source);
            this.$.rexConnectionManager.inEvtTypesNotLoaded();
        });


        this.addEventListener('entity-not-loaded', function (e) {
            // Generate an alert for the error condition
            alert( "Event :" + 'entity-not-loaded' + ' from ' + e.detail.source);
        });

        this.addEventListener('relationship-not-loaded', function (e) {
            // Generate an alert for the error condition
            alert( "Event :" + 'relationship-not-loaded' + ' from ' + e.detail.source);
        });

        this.addEventListener('change-focus-entity', function (e) {
            var entityGUID = e.detail.entityGUID;
            this.$.rexInstanceRetriever.inEvtChangeFocusEntity(entityGUID);
        });

        this.addEventListener('change-focus-relationship', function (e) {
            var relationshipGUID = e.detail.relationshipGUID;
            this.$.rexInstanceRetriever.inEvtChangeFocusRelationship(relationshipGUID);
        });

        this.addEventListener('focus-entity-changed', function (e) {
            var guid = e.detail.guid;
            this.$.rexDetailsPanel.inEvtFocusEntityChanged(guid);
            this.$.rexDiagramManager.inEvtFocusEntityChanged(guid);
            this.$.rexInstanceRetriever.inEvtFocusEntityChanged(guid);
        });

        this.addEventListener('focus-entity-cleared', function (e) {
            this.$.rexDetailsPanel.inEvtFocusEntityCleared();
            this.$.rexInstanceRetriever.inEvtFocusEntityCleared();
         });

        this.addEventListener('focus-relationship-changed', function (e) {
            var guid = e.detail.guid;
            this.$.rexDetailsPanel.inEvtFocusRelationshipChanged(guid);
            this.$.rexDiagramManager.inEvtFocusRelationshipChanged(guid);
            this.$.rexInstanceRetriever.inEvtFocusRelationshipChanged(guid);
        });

        this.addEventListener('focus-relationship-cleared', function (e) {
            this.$.rexDetailsPanel.inEvtFocusRelationshipCleared();
            this.$.rexInstanceRetriever.inEvtFocusRelationshipCleared();
        });

        this.addEventListener('graph-extended', function (e) {
            this.$.rexDiagramManager.inEvtGraphExtended();
            this.$.graphControls.inEvtGraphExtended();
        });

        this.addEventListener('pre-traversal-loaded', function (e) {
             this.$.graphControls.inEvtPreTraversalLoaded();
        });

        this.addEventListener('pre-traversal-not-loaded', function (e) {
            // Generate an alert for the error condition
            alert( "Event :" + 'pre-traversal-not-loaded' + ' from ' + e.detail.source);
        });

        this.addEventListener('traversal-not-loaded', function (e) {
             // Generate an alert for the error condition
             alert( "Event :" + 'traversal-not-loaded' + ' from ' + e.detail.source);
        });

        this.addEventListener('graph-cleared', function (e) {
             this.$.rexInstanceRetriever.inEvtGraphCleared();
             this.$.rexDiagramManager.inEvtGraphCleared();
             this.$.rexDetailsPanel.inEvtGraphCleared();
             this.$.graphControls.inEvtGraphCleared();
        });

        this.addEventListener('undo', function (e) {
             this.$.rexInstanceRetriever.inEvtUndoPhaseOne();
        });

        this.addEventListener('graph-being-reduced', function (e) {
            this.$.rexDiagramManager.inEvtGraphBeingReduced();
        });

        this.addEventListener('graph-reduced', function (e) {
            this.$.rexInstanceRetriever.inEvtUndoPhaseTwo();
            this.$.graphControls.inEvtGraphReduced();
        });

    }

    aboutRex() {
        this.$.aboutDialog.open();
    }
}

window.customElements.define('repository-explorer-view', RepositoryExplorerView);