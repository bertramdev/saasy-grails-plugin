package com.getsaasy.plugin

import grails.test.spock.IntegrationSpec

class SaasyServiceInvoiceServiceIntegrationSpec extends IntegrationSpec {

	def saasyServiceInvoiceService
	def grailsApplication

	def setup() {
		grailsApplication.config.saasy.baseUrl = "https://app.stage.getsaasy.com"
//		grailsApplication.config.saasy.apiKey = "6480592d-4037-4bdd-a09b-860b8c4f75ce"
		grailsApplication.config.saasy.apiKey = "4882e756-7ebf-4902-be42-9e3884edb2aa"
    }

    def cleanup() {
    }

	void "test list"() {
		setup:
		def opts = [:]
		println "apiKey: ${grailsApplication.config.saasy.apiKey}"
		when:
		def result = saasyServiceInvoiceService.list(opts)
		then:
		println "result: ${result}"
		result != null
	}
}
