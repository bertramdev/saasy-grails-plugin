package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.Method.*

@Transactional
class SaasyServicePlanService extends AbstractSaasyService {
    static API_NAME = 'servicePlan'
    static SERVICE_BASE_PATH = '/api/rest/'+API_NAME+'/'

    // GET
    // /api/rest/servicePlan/{id}
    def get(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // GET
    // /api/rest/servicePlan
    def list(params) {
        transformListOutput(doApiCall(SERVICE_BASE_PATH, params))
    }

    // POST
    // /api/rest/servicePlan/{id}
    def create(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, POST))
    }

    // PUT
    // /api/rest/servicePlan/{id}
    def update(params) {
        def body = params.remove(API_NAME)
        transformPostOutput(doApiCall(SERVICE_BASE_PATH, params, body, PUT))
    }

    // DELETE
    // /api/rest/servicePlan/{id}
    def deactivate(params) {
        transformGetSuccess(doApiCall(SERVICE_BASE_PATH, params, null, DELETE))
    }

    // GET
    // /api/servicePlan/activate/{id}
    def activate(params) {
        transformGetSuccess(doApiCall('/api/servicePlan/activate/'+(params.id?:'0'), [externalId:params.externalId], null, GET))
    }

}
