/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import {PolymerElement, html} from '@polymer/polymer/polymer-element.js';
import {setPassiveTouchGestures, setRootPath} from '@polymer/polymer/lib/utils/settings.js';
import '@polymer/app-layout/app-drawer/app-drawer.js';
import '@polymer/app-layout/app-drawer-layout/app-drawer-layout.js';
import '@polymer/app-layout/app-header/app-header.js';
import '@polymer/app-layout/app-header-layout/app-header-layout.js';
import '@polymer/app-layout/app-scroll-effects/app-scroll-effects.js';
import '@polymer/app-layout/app-toolbar/app-toolbar.js';
import '@polymer/app-route/app-location.js';
import '@polymer/app-route/app-route.js';
import '@polymer/iron-localstorage/iron-localstorage.js';
import '@polymer/iron-pages/iron-pages.js';
import '@polymer/iron-selector/iron-selector.js';
import '@polymer/paper-icon-button/paper-icon-button.js';
import '@polymer/paper-menu-button/paper-menu-button.js';
import '@polymer/paper-item/paper-item.js';
import '@polymer/paper-button';
import '@polymer/paper-dropdown-menu/paper-dropdown-menu.js';
import '@polymer/paper-listbox/paper-listbox.js';
import '@polymer/paper-item/paper-item.js';
import '@polymer/paper-menu-button';
import '@polymer/iron-icon/iron-icon.js';
import '@polymer/iron-icons/iron-icons.js';
import './my-icons.js';
import './token-ajax.js';
import './login-view.js';
import './user-options-menu';

// Gesture events like tap and track generated from touch will not be
// preventable, allowing for better scrolling performance.
setPassiveTouchGestures(true);

// Set Polymer's root path to the same value we passed to our service worker
// in `index.html`.
setRootPath(MyAppGlobals.rootPath);

class MyApp extends PolymerElement {
    static get template() {
        return html`
      <style>
        :host {
          --app-primary-color: 	#71ccdc;
          --app-secondary-color: #24272a;
          display: block;
        }
        
        
        
        app-drawer-layout:not([narrow]) [drawer-toggle] {
          display: none;
        }

        app-header {
          color: #fff;
          background-color: var(--app-primary-color);
        }

        app-header paper-icon-button {
          --paper-icon-button-ink-color: white;
        }

        .drawer-list {
          margin: 0 20px;
        }

        .drawer-list a {
          display: block;
          padding: 0 16px;
          text-decoration: none;
          color: var(--app-secondary-color);
          line-height: 40px;
        }

        .drawer-list a.iron-selected {
          color: #24272a;
          font-weight: bold;
        }
        
      </style>
      <iron-localstorage name="my-app-storage" value="{{token}}"></iron-localstorage>
      
      
      <app-location route="{{route}}" url-space-regex="^[[rootPath]]"></app-location>

      <app-route route="{{route}}" pattern="[[rootPath]]:page" data="{{routeData}}" tail="{{subroute}}"></app-route>

        <template is="dom-if" if="[[!token]]"  restamp="true">
            <login-view id="loginView" token="{{token}}"></login-view>
        </template>
      
        <template is="dom-if" if="[[token]]"  restamp="true">
            <app-drawer-layout fullbleed="" narrow="{{narrow}}">
            <!-- Drawer content -->
            <app-drawer id="drawer" slot="drawer" swipe-open="[[narrow]]">
              <app-toolbar style="height: 72px">
                <img src="../images/Logo_trademark.jpg" height="60" style="margin: 15px 30px 0 30px"/>
              </app-toolbar>
              <iron-selector selected="[[page]]" attr-for-selected="name" class="drawer-list" role="navigation">
                <a name="asset-search" href="[[rootPath]]search">Asset search</a>
                <a name="data-view" href="[[rootPath]]data">Data view</a>
              </iron-selector>
            </app-drawer>
    
                <!-- Main content -->
                <app-header-layout>
        
                  <app-header slot="header" condenses="" reveals="" effects="waterfall">
                    <app-toolbar>
                      <paper-icon-button icon="my-icons:menu" drawer-toggle=""></paper-icon-button>
                      <div main-title="">Asset Catalog search</div>
                      <user-options token="[[token]]"></user-options>
                    </app-toolbar>
                  </app-header>
        
                  <iron-pages selected="[[page]]" attr-for-selected="name" role="main">
                    <asset-search-view name="search"></asset-search-view>
                    <data-view name="data"></data-view>
                    <my-view2 name="view2"></my-view2>
                    <my-view3 name="view3"></my-view3>
                    <my-view404 name="view404"></my-view404>

                  </iron-pages>
                </app-header-layout>
            </app-drawer-layout>
         </template>
    `;
    }

    static get properties() {
        return {
            page: {
                type: String,
                reflectToAttribute: true,
                observer: '_pageChanged'
            },
            token: {
                type: Object,
                notify: true,
                observer: '_tokenChanged'
            },
            routeData: Object,
            subroute: Object,
            pages: {
                type: Array,
                value: ['search', 'data', 'view2', 'view3']
            },
            feedback: {
                type: Object,
                notify: true,
                observer: '_feedbackChanged'
            }
        };
    }

    static get observers() {
        return [
            '_routePageChanged(routeData.page)'
        ];
    }

    ready(){
        super.ready();
        this.addEventListener('logout', this._onLogout);
        this.addEventListener('open-page', this._onPageChanged);
        this.addEventListener('show-feedback', this._onFeedbackChanged);
    }

    _routePageChanged(page) {
        // Show the corresponding page according to the route.
        //
        // If no page was found in the route data, page will be an empty string.
        // Show 'search' in that case. And if the page doesn't exist, show 'view404'.

        if (!page) {
            this.page = 'search';
        } else if (this.pages.indexOf(page) !== -1) {
            this.page = page;
        } else {
            this.page = 'search';
        }

        // Close a non-persistent drawer when the page & route are changed.
        if (this.page!='login' && this.$.drawer && !this.$.drawer.persistent) {
            this.$.drawer.close();

        }
    }

    _onPageChanged(event) {
        this.page = event.model.item.page;
        console.log("_onPageChanged... " + this.page);
    }

    _onPageChanged(event) {
        this.page = event.model.item.page;
        console.log("_onPageChanged... " + this.page);
    }

    _onLogout(event) {
        //TODO invalidate token from server
        this.token = null;
    }

    _tokenChanged(newValue, oldValue) {
        console.debug('token changed to new value: ' + newValue)
    }

    _pageChanged(page) {
        // Import the page component on demand.
        //
        // Note: `polymer build` doesn't like string concatenation in the import
        // statement, so break it up.
        switch (page) {
            case 'data':
                import('./data-view.js');
                break;
            case 'view2':
                import('./my-view2.js');
                break;
            case 'view3':
                import('./my-view3.js');
                break;
            case 'view404':
                import('./my-view404.js');
                break;
            case 'search' :
                import('./asset-search-view.js');
                break;
        }
    }
    

}

window.customElements.define('my-app', MyApp);
