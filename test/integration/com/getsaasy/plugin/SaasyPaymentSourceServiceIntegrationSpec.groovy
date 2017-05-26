package com.getsaasy.plugin

import grails.test.spock.IntegrationSpec

class SaasyPaymentSourceServiceIntegrationSpec extends IntegrationSpec {

	def saasyPaymentSourceService
	def grailsApplication

    def setup() {
		grailsApplication.config.saasy.baseUrl = "https://app.stage.getsaasy.com"
//		grailsApplication.config.saasy.apiKey = "6420d2b5-e16c-4394-96e7-e6e49242d9b5"
    }

    def cleanup() {
    }

    void "test list"() {
		setup:
		def opts = [:]
		when:
		def result = saasyPaymentSourceService.list(opts)
		then:
		println "result: ${result}"
		result != null
    }

	void "test get"() {
		setup:
		def opts = [:]
//		opts.subscriberExternalId = 1
		opts.externalId = 'test'
		when:
		def result = saasyPaymentSourceService.get(opts)
		then:
		println "result: ${result}"
		result != null
	}
}
