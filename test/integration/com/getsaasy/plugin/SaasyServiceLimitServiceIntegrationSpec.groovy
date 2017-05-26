package com.getsaasy.plugin

import grails.test.spock.IntegrationSpec

class SaasyServiceLimitServiceIntegrationSpec extends IntegrationSpec {

	def saasyServiceLimitService
	def grailsApplication
	def testSubscriptionId = 10072

	def setup() {
		grailsApplication.config.saasy.baseUrl = "https://app.stage.getsaasy.com"
//		grailsApplication.config.saasy.apiKey = "6420d2b5-e16c-4394-96e7-e6e49242d9b5"
    }

    def cleanup() {
    }
/*
    void "test check"() {
		setup:
		def opts = [:]
		opts.subscriptionId = testSubscriptionId
		when:
		def result = saasyServiceLimitService.check(opts)
		then:
		println "result: ${result}"
	}

	void "test count"() {
		setup:
		def opts = [:]
		opts.subscriptionId = testSubscriptionId
		when:
		def result = saasyServiceLimitService.count(opts)
		then:
		println "result: ${result}"
	}
	*/
}
