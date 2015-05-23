package com.getsaasy.plugin

import grails.transaction.Transactional

@Transactional
class SaasyCompositeService extends AbstractSaasyService {
	static SERVICE_BASE_PATH = '/api/rest/compositeServiceSubscriber/'

    // POST
    // /api/rest/compositeServiceSubscriber 
    def saveSubscriberInformation(params) {

    }
}
