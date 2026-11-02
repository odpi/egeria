# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the ODPi Egeria project.
"""
Convert NIST's OSCAL catalogs into the compact JSON resources that SecurityPackArchiveWriter reads to build
SecurityContentPack.omarchive.

Usage:

    python3 convert_oscal_security_catalogs.py <directory holding the OSCAL files> <output directory>

The input directory must hold the two files published in https://github.com/usnistgov/oscal-content:

    nist.gov/SP800-53/rev5/json/NIST_SP-800-53_rev5_catalog-min.json
    nist.gov/CSF/v2.0/json/NIST_CSF_v2.0_catalog-min.json

The output directory is normally src/main/resources/security-catalogs in this module.  The rules are:

* withdrawn controls, categories and subcategories are left out;
* each SP 800-53 control and control enhancement keeps its NIST label (for example AC-2 and AC-2(1)), its title,
  its statement and its discussion;
* parameters in the statements are written the way NIST publishes them, as
  [Assignment: organization-defined ...] or [Selection (one or more): ...; ...];
* each CSF subcategory keeps its identifier, its outcome statement and its implementation examples.

Only the Python standard library is used.
"""

import json
import re
import sys

SP_800_53_FILE = 'NIST_SP-800-53_rev5_catalog-min.json'
CSF_FILE = 'NIST_CSF_v2.0_catalog-min.json'

LICENSE = 'Public domain (U.S. Government work); copyright waived worldwide under CC0 1.0'

INSERT_PARAM = re.compile(r'\{\{\s*insert:\s*param,\s*([^\s}]+)\s*\}\}')


def props(element, name):
    """Return the values of the named OSCAL properties of an element."""
    return [p['value'] for p in element.get('props', []) if p['name'] == name]


def canonical_label(element):
    """Return the NIST label without padding (AC-2 rather than AC-02)."""
    plain = [p['value'] for p in element.get('props', []) if p['name'] == 'label' and 'class' not in p]
    return plain[0] if plain else (props(element, 'label') or [element['id'].upper()])[0]


def withdrawn(element):
    """Return true if the element has been withdrawn from the catalog."""
    return 'withdrawn' in props(element, 'status')


class ParameterRenderer:
    """Replaces OSCAL parameter insertions with the text NIST uses in the published catalog."""

    def __init__(self, catalog):
        self.params = {}
        for group in catalog['groups']:
            for control in group['controls']:
                self._collect(control)

    def _collect(self, control):
        for param in control.get('params', []):
            self.params[param['id']] = param
        for child in control.get('controls', []):
            self._collect(child)

    def param(self, param_id, depth=0):
        param = self.params.get(param_id)
        if param is None:
            return '[Assignment: organization-defined value]'
        if 'select' in param:
            how_many = param['select'].get('how-many')
            choices = [self.text(choice, depth + 1) for choice in param['select'].get('choice', [])]
            head = 'Selection (one or more)' if how_many == 'one-or-more' else 'Selection'
            return '[' + head + ': ' + '; '.join(choices) + ']'
        if param.get('label'):
            return '[Assignment: organization-defined ' + param['label'] + ']'
        return '[Assignment: organization-defined value]'

    def text(self, text, depth=0):
        if depth > 5:
            return text
        return INSERT_PARAM.sub(lambda match: self.param(match.group(1), depth), text)


def render_part(renderer, part, indent=0):
    """Render a statement part and its numbered items as indented lines of text."""
    lines = []
    label = props(part, 'label')
    prose = renderer.text(part.get('prose', '')).strip()
    line = ((label[0] + ' ') if label else '') + prose
    if line.strip():
        lines.append('  ' * indent + line.strip())
    for sub_part in part.get('parts', []):
        if sub_part.get('name') == 'item':
            lines.extend(render_part(renderer, sub_part, indent + (1 if line.strip() else 0)))
    return lines


def part_text(renderer, control, name):
    """Return the text of the named part of a control, such as its statement or guidance."""
    for part in control.get('parts', []):
        if part.get('name') == name:
            return '\n'.join(render_part(renderer, part)).strip()
    return None


def convert_sp_800_53(input_dir):
    catalog = json.load(open(input_dir + '/' + SP_800_53_FILE))['catalog']
    renderer = ParameterRenderer(catalog)

    def control_entry(control):
        return {'id': canonical_label(control),
                'title': control['title'],
                'statement': part_text(renderer, control, 'statement'),
                'discussion': part_text(renderer, control, 'guidance'),
                'sortId': (props(control, 'sort-id') or [None])[0]}

    families = []
    for group in catalog['groups']:
        family = {'id': group['id'].upper(), 'title': group['title'], 'controls': []}
        for control in group['controls']:
            if withdrawn(control):
                continue
            entry = control_entry(control)
            entry['enhancements'] = [control_entry(e) for e in control.get('controls', []) if not withdrawn(e)]
            family['controls'].append(entry)
        families.append(family)

    metadata = catalog['metadata']
    return {'source': 'https://github.com/usnistgov/oscal-content/blob/main/nist.gov/SP800-53/rev5/json/' + SP_800_53_FILE,
            'license': LICENSE,
            'title': 'NIST SP 800-53 Rev. 5 Security and Privacy Controls for Information Systems and Organizations',
            'version': metadata.get('version'),
            'lastModified': metadata.get('last-modified'),
            'url': 'https://csrc.nist.gov/pubs/sp/800/53/r5/upd1/final',
            'families': families}


def convert_csf(input_dir):
    catalog = json.load(open(input_dir + '/' + CSF_FILE))['catalog']

    def statement(element):
        for part in element.get('parts', []):
            if part.get('name') in ('statement', 'overview'):
                return part.get('prose', '').strip()
        return None

    def examples(element):
        return [part.get('prose', '').strip() for part in element.get('parts', []) if part.get('name') == 'guidance']

    functions = []
    for function in catalog['groups']:
        function_entry = {'id': function['id'], 'title': function['title'],
                          'label': (props(function, 'label') or [function['title']])[0],
                          'description': statement(function), 'categories': []}
        for category in function.get('controls', []):
            if withdrawn(category):
                continue
            category_entry = {'id': category['id'], 'title': category['title'],
                              'label': (props(category, 'label') or [category['title']])[0],
                              'description': statement(category), 'subcategories': []}
            for subcategory in category.get('controls', []):
                if withdrawn(subcategory):
                    continue
                category_entry['subcategories'].append({'id': subcategory['id'],
                                                        'statement': statement(subcategory),
                                                        'examples': examples(subcategory)})
            function_entry['categories'].append(category_entry)
        functions.append(function_entry)

    metadata = catalog['metadata']
    return {'source': 'https://github.com/usnistgov/oscal-content/blob/main/nist.gov/CSF/v2.0/json/' + CSF_FILE,
            'license': LICENSE,
            'title': 'The NIST Cybersecurity Framework (CSF) 2.0',
            'version': metadata.get('version'),
            'lastModified': metadata.get('last-modified'),
            'url': 'https://www.nist.gov/cyberframework',
            'functions': functions}


def main():
    if len(sys.argv) != 3:
        print(__doc__)
        sys.exit(1)

    input_dir, output_dir = sys.argv[1], sys.argv[2]

    sp_800_53 = convert_sp_800_53(input_dir)
    json.dump(sp_800_53, open(output_dir + '/nist-sp-800-53-rev5.json', 'w'), indent=1, ensure_ascii=True)

    csf = convert_csf(input_dir)
    json.dump(csf, open(output_dir + '/nist-csf-2.0.json', 'w'), indent=1, ensure_ascii=True)

    controls = sum(len(f['controls']) for f in sp_800_53['families'])
    enhancements = sum(len(c['enhancements']) for f in sp_800_53['families'] for c in f['controls'])
    categories = sum(len(f['categories']) for f in csf['functions'])
    subcategories = sum(len(c['subcategories']) for f in csf['functions'] for c in f['categories'])

    print('SP 800-53: ' + str(len(sp_800_53['families'])) + ' families, ' + str(controls) + ' controls, ' +
          str(enhancements) + ' enhancements')
    print('CSF 2.0: ' + str(len(csf['functions'])) + ' functions, ' + str(categories) + ' categories, ' +
          str(subcategories) + ' subcategories')


if __name__ == '__main__':
    main()
