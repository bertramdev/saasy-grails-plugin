package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.HttpVerb.*

@Transactional
class SaasyServiceSubscriptionProductTypeService extends AbstractSaasyService {

    static API_NAME = 'serviceSubscriptionProduct'
    static SERVICE_BASE_PATH = '/api/rest/'+API_NAME+'/'

    // GET
    // /api/rest/serviceSubscriber/{id}
    def get(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // GET
    // /api/rest/serviceSubscriber
    def list(params) {
        transformListOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // POST
    // /api/rest/serviceSubscriber/{id}
    def create(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, POST))
    }

    // PUT
    // /api/rest/serviceSubscriber/{id}
    def update(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, PUT))
    }

    // POST
    // /api/serviceSubscriber/bulkUpdateAttributes
    def bulkUpdateAttributes(params) {
        def body = [ids:params.remove(IDS), attributes:params.remove('attributes')]
        transformSuccess(doApiCall('/api/'+API_NAME+'/bulkUpdateAttributes', params, body, POST))
    }

    // DELETE
    // /api/rest/serviceSubscriber/{id}
    def delete(params) {
        transformSuccess(doApiCall(SERVICE_BASE_PATH, params, null, DELETE))
    }

}
