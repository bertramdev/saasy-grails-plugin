package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.Method.*

@Transactional
class SaasyServiceSubscriptionService extends AbstractSaasyService {
    static API_NAME = 'serviceSubscription'
    static SERVICE_BASE_PATH = '/api/rest/'+API_NAME+'/'

    // GET
    // /api/rest/serviceSubscription/{id}
    def get(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // GET
    // /api/rest/serviceSubscription
    def list(params) {
        transformListOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // POST
    // /api/rest/serviceSubscription/{id}
    def create(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, POST))
    }

    // PUT
    // /api/rest/serviceSubscription/{id}
    def update(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, PUT))
    }

    // POST
    // /api/serviceSubscription/bulkUpdateAttributes
    def bulkUpdateAttributes(params) {
        def body = [ids:params.remove(IDS), attributes:params.remove('attributes')]
        transformSuccess(doApiCall('/api/'+API_NAME+'/bulkUpdateAttributes', params, body, POST))
    }

    // DELETE
    // /api/rest/serviceSubscription/{id}
    def deactivate(params) {
        transformSuccess(doApiCall(SERVICE_BASE_PATH, params, null, DELETE))
    }

    // GET
    // /api/serviceSubscription/failedSubscriptions
    def failedSubscriptions() {
        transformListOutput(doApiCall('/api/'+API_NAME+'/failedSubscriptions', null, null, GET))
    }

}
