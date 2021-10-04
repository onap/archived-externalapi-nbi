from docs_conf.conf import *

branch = 'latest'
master_doc = 'index'

linkcheck_ignore = [
    'http://localhost',
]

intersphinx_mapping = {}

html_last_updated_fmt = '%d-%b-%y %H:%M'

extensions = ['sphinxcontrib.blockdiag', 'sphinxcontrib.redoc']

redoc = [
            {
                'name': 'Hub API',
                'page': 'offeredapis/hub',
                'spec': 'offeredapis/swagger/hub.json',
                'embed': True
            },
            {
                'name': 'Service Catalog API',
                'page': 'offeredapis/service_catalog',
                'spec': 'offeredapis/swagger/service_catalog.json',
                'embed': True
            },
            {
                'name': 'Service Inventory API',
                'page': 'offeredapis/service_inventory',
                'spec': 'offeredapis/swagger/service_inventory.json',
                'embed': True
            },
            {
                'name': 'Service Order API',
                'page': 'offeredapis/service_order',
                'spec': 'offeredapis/swagger/service_order.json',
                'embed': True
            },
            {
                'name': 'Status API',
                'page': 'offeredapis/status',
                'spec': 'offeredapis/swagger/status.json',
                'embed': True
            }
        ]

redoc_uri = 'https://cdn.jsdelivr.net/npm/redoc@next/bundles/redoc.standalone.js'

def setup(app):
    app.add_css_file("css/ribbon.css")
