export * from './carbon-icons-list';
        import * as icons from './carbon-icons-list';
        export default (function () { return Object.keys(icons).map(function (key) { return icons[key]; }); })();