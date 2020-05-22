'use strict';

function DeclarationStore() {
  this.declarations = [];
}

DeclarationStore.prototype = {
  addDeclaration: function(declaration) {
    this.declarations.push(declaration);
  },

  replaceVariables: function(scssString) {
    var replacedString = scssString;

    this.declarations.forEach(function(declaration) {
      var variable = declaration.variable.value;
      var value = declaration.value.value;

      var subsetRegex = new RegExp('\\' + variable + '[\\w_-]', 'g');
      var isSubset = !!replacedString.match(subsetRegex);

      if (!isSubset) {
        var regex = new RegExp('(\\' + variable + ')([\\W\\,]?)', 'g');
        replacedString = replacedString.replace(regex, value + '$2');
      }
    });
    return replacedString;
  }
};

module.exports = DeclarationStore;
