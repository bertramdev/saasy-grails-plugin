package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.Method.*

@Transactional
class SaasyServiceLimitService extends AbstractSaasyService {
    static SERVICE_BASE_PATH = '/api/operation/serviceSubscriptionLimit/'

	//GET
	// /api/operation/serviceSubscriptionLimit/check 
    def check(params) {

    }

	//GET
	// /api/operation/serviceSubscriptionLimit/count 
    def count(params) {

    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def ttl(params) {

    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def increment(params) {

    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def clear(params) {

    }

	//GET
	// /api/operation/serviceSubscriptionLimit/ttl 
    def reset(params) {

    }

}
