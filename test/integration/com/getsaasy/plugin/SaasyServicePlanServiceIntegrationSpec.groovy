package com.getsaasy.plugin

import grails.test.spock.IntegrationSpec

class SaasyServicePlanServiceIntegrationSpec extends IntegrationSpec {

	def saasyServicePlanService
	def grailsApplication
	def testServicePlanId = 4

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
		when:
		def result = saasyServicePlanService.list(opts)
		then:
		println "result: ${result}"
		result != null
	}

	void "test get"() {
		setup:
		def opts = [:]
		opts.id = testServicePlanId
		when:
		def result = saasyServicePlanService.get(opts)
		then:
		println "result: ${result}"
		result != null
	}
}
