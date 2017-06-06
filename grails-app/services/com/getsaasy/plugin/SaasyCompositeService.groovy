package com.getsaasy.plugin

import grails.transaction.Transactional
import static groovyx.net.http.HttpVerb.*

@Transactional
class SaasyCompositeService extends AbstractSaasyService {
	static SERVICE_BASE_PATH = '/api/rest/compositeServiceSubscriber/'

    // POST
    // /api/rest/compositeServiceSubscriber 
    def saveSubscriberInformation(params) {
        def body = [servicePaymentSource:params.remove('servicePaymentSource'), 
            serviceShippingAddress:params.remove('serviceShippingAddress'),
            serviceSubscription:params.remove('serviceSubscription'),
            servicePlan:params.remove('servicePlan'),
            calculateCredit:params.remove('calculateCredit'),
            keepOrderDate:params.remove('keepOrderDate'),
            prorate:params.remove('prorate'),
            serviceSubscriber:params.remove('serviceSubscriber'),
            servicePlanProductTypes:params.remove('servicePlanProductTypes')
        ]
		log.info "saveSubscriberInformation body: ${body}"
		log.info "saveSubscriberInformation params: ${params}"
        doApiCall(SERVICE_BASE_PATH, params, body, POST)
    }

    def ping(params) {
        doApiCall('/api/ping', params, null, GET)
    }
    
}
