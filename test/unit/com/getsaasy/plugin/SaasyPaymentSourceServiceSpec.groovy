package com.getsaasy.plugin

import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestMixin(GrailsUnitTestMixin)
class SaasyPaymentSourceServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {
		expect:
		1==1
    }
}
