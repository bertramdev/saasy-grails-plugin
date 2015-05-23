package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.Method.*

@Transactional
class SaasyServiceOrderService extends AbstractSaasyService {
    static API_NAME = 'serviceOrder'
    static SERVICE_BASE_PATH = '/api/rest/'+API_NAME+'/'

    // GET
    // /api/rest/serviceOrder/{id}
    def get(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // GET
    // /api/rest/serviceOrder
    def list(params) {
        transformListOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // POST
    // /api/rest/serviceOrder/{id}
    def create(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, POST))
    }

    // PUT
    // /api/rest/serviceOrder/{id}
    def update(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, PUT))
    }

    // POST
    // /api/serviceOrder/bulkUpdateAttributes
    def bulkUpdateAttributes(params) {

    }

    // DELETE
    // /api/rest/serviceOrder/{id}
    def cancel(params) {
        transformGetSuccess(doApiCall(SERVICE_BASE_PATH, params, null, DELETE))
    }


}
