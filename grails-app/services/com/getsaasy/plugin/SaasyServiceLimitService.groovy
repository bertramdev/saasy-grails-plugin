package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.Method.*

@Transactional
class SaasyServiceLimitService extends AbstractSaasyService {
    static SERVICE_BASE_PATH = '/api/operation/serviceSubscriptionLimit/'

	//GET
	// /api/operation/serviceSubscriptionLimit/check 
    def check(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH+'check', params))
    }

	//GET
	// /api/operation/serviceSubscriptionLimit/count 
    def count(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH+'count', params))
    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def ttl(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH+'ttl', params))
    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def increment(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH+'increment', params))
    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def clear(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH+'clear', params))
    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def reset(params) {
        transformGetOutput(doApiCall(SERVICE_BASE_PATH+'reset', params))
    }

}
