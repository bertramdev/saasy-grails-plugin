package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.Method.*

@Transactional
class SaasyServicePlanProductTypeService extends AbstractSaasyService {
    static API_NAME = 'servicePlanProductType'
    static SERVICE_BASE_PATH = '/api/rest/'+API_NAME+'/'

    // GET
    // /api/rest/servicePlanProductType/{id}
    def get(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // GET
    // /api/rest/servicePlanProductType
    def list(params) {
        transformListOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // POST
    // /api/rest/servicePlanProductType/{id}
    def create(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, POST))
    }

    // PUT
    // /api/rest/servicePlanProductType/{id}
    def update(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, PUT))
    }


    // DELETE
    // /api/rest/servicePlanProductType/{id}
    def cancel(params) {

    }



}
