package com.getsaasy.plugin

import grails.test.spock.IntegrationSpec

class SaasyServiceSubscriptionServiceIntegrationSpec extends IntegrationSpec {

	def saasyServiceSubscriptionService
	def grailsApplication
	def testSubscriptionId = 10072

	def setup() {
		grailsApplication.config.saasy.baseUrl = "https://app.stage.getsaasy.com"
//		grailsApplication.config.saasy.apiKey = "6420d2b5-e16c-4394-96e7-e6e49242d9b5"
		grailsApplication.config.saasy.apiKey = "4882e756-7ebf-4902-be42-9e3884edb2aa"
	}

    def cleanup() {
    }

    void "test list"() {
		setup:
		def opts = [:]
		println "apiKey: ${grailsApplication.config.saasy.apiKey}"
		when:
		def result = saasyServiceSubscriptionService.list(opts)
		then:
		println "result: ${result}"
		result != null
    }

	void "test get"() {
		setup:
		def opts = [:]
		opts.id = testSubscriptionId
		println "apiKey: ${grailsApplication.config.saasy.apiKey}"
		when:
		def result = saasyServiceSubscriptionService.get(opts)
		then:
		println "result: ${result}"
		result != null
	}

}
