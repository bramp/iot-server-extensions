/*
 *  Copyright (c) 2015, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
app.dependencies = ['store-common'];
app.server = function (ctx) {
    return {
        endpoints: {
            pages: [{
                title: 'Store | Analytics',
                url: 'analytics',
                path: 'analytics.jag',
                secured: true
            }, {
                title: 'Store | Devices',
                url: 'devices',
                path: 'device_listing.jag',
                secured: true
            }, {
                title: 'Store | Device Details',
                url: 'device',
                path: 'device_details.jag',
                secured: true
            }, {
                title: 'Store | Device Events',
                url: 'events',
                path: 'events.jag',
                secured: true
            }, {
                title: 'Store | Dashboard',
                url: 'dashboard',
                path: 'dashboard.jag',
                secured: true
            }, {
                title: 'Store | Groups',
                url: 'groups',
                path: 'groups.jag',
                secured: true
            }, {
                title: 'Store | Policies',
                url: 'policies',
                path: 'policies.jag',
                secured: true
            }, {
                title: 'Store | Users',
                url: 'users',
                path: 'users.jag',
                secured: true
            }, {
                title: 'Store | Advance Search',
                url: 'advanced-search',
                path: 'advanced-search.jag'
            }, {
                url: 'sso-login',
                path: 'sso-auth-login-controller.jag'
            }],
            apis: [{
                url: 'stats',
                path: 'stats-api.jag',
                secured: true
            }, {
                url: 'devices',
                path: 'device-api.jag',
                secured: true
            }, {
                url: 'group',
                path: 'group-api.jag',
                secured: true
            }, {
                url: 'policies',
                path: 'policy-api.jag',
                secured: true
            }, {
                url: 'users',
                path: 'user-api.jag',
                secured: true
            }]
        },
        configs: {
            landingPage: '/assets/deviceType/list',
            disabledAssets: ['ebook', 'api', 'wsdl', 'servicex', 'policy', 'proxy', 'schema', 'sequence', 'uri', 'wadl', 'endpoint', 'swagger', 'restservice', 'comments', 'soapservice', 'service', 'license', 'gadget', 'site', 'server']
        }
    }
};

app.pageHandlers = function (ctx) {
    return {
        onPageLoad: function () {
            if ((ctx.isAnonContext) && (ctx.endpoint.secured)) {
                log.info(ctx.session);
                var loginUrl = ctx.appContext + '/login?requestedPage=' + encodeURIComponent('/store/pages/' + ctx.endpoint.url) + '&ignoreReferer=true';
                ctx.res.sendRedirect(loginUrl);
                return false;
            }
            return true;
        }
    }
};

app.apiHandlers = function (ctx) {
    return {
        onApiLoad: function () {
            if ((ctx.isAnonContext) && (ctx.endpoint.secured)) {
                ctx.res.status = '401';
                return false;
            }
            return true;
        }
    }
};

app.renderer = function (ctx) {
    var decoratorApi = require('/modules/page-decorators.js').pageDecorators;
    var customDecoratorApi = require('../modules/custom-page-decorators.js').pageDecorators;
    return {
        pageDecorators: {
            navigationBar: function (page) {
                return customDecoratorApi.navigationBar(ctx, page, this);
            },
            searchBar: function (page) {
                return decoratorApi.searchBar(ctx, page, this);
            },
            authenticationDetails: function (page) {
                return decoratorApi.authenticationDetails(ctx, page, this);
            },
            recentAssetsOfActivatedTypes: function (page) {
                return decoratorApi.recentAssetsOfActivatedTypes(ctx, page, this);
            },
            popularAssets: function (page) {
                return decoratorApi.popularAssets(ctx, page, this);
            }
        }
    }
};