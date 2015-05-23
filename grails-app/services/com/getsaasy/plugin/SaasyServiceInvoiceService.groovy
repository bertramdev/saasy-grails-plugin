package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.Method.*

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

    }

    // DELETE
    // /api/rest/serviceInvoice/{id}
    def cancel(params) {
        transformGetSuccess(doApiCall(SERVICE_BASE_PATH, params, null, DELETE))
    }

    // GET
    // /api/rest/serviceInvoice/validate/{id}
    def validate(params) {

    }

    // GET
    // /api/rest/serviceInvoice/authorize/{id}
    def authorize(params) {

    }

    // GET
    // /api/rest/serviceInvoice/refund/{id}
    def refund(params) {

    }

}
