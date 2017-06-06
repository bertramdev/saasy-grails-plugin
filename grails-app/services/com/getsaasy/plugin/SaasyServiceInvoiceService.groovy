package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.HttpVerb.*

@Transactional
class SaasyServiceInvoiceService extends AbstractSaasyService {
    static API_NAME = 'serviceInvoice'
    static SERVICE_BASE_PATH = '/api/rest/'+API_NAME+'/'

    // GET
    // /api/rest/serviceInvoice/{id}
    def get(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // GET
    // /api/rest/serviceInvoice
    def list(params) {
        transformListOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // PUT
    // /api/rest/serviceInvoice/{id}
    def update(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, PUT))
    }

    // POST
    // /api/serviceInvoice/bulkUpdateAttributes
    def bulkUpdateAttributes(params) {
        def body = [ids:params.remove(IDS), attributes:params.remove('attributes')]
        transformSuccess(doApiCall('/api/'+API_NAME+'/bulkUpdateAttributes', params, body, POST))
    }

    // DELETE
    // /api/rest/serviceInvoice/{id}
    def cancel(params) {
        transformSuccess(doApiCall(SERVICE_BASE_PATH, params, null, DELETE))
    }

    // GET
    // /api/rest/serviceInvoice/validate/{id}
    def validate(params) {
        transformSuccess(doApiCall(SERVICE_BASE_PATH+'validate', params, null, DELETE))
    }

    // GET
    // /api/rest/serviceInvoice/authorize/{id}
    def authorize(params) {
        transformSuccess(doApiCall(SERVICE_BASE_PATH+'authorize', params, null, DELETE))
    }

    // GET
    // /api/rest/serviceInvoice/refund/{id}
    def refund(params) {
        transformSuccess(doApiCall(SERVICE_BASE_PATH+'refund', params, null, DELETE))
    }

}
