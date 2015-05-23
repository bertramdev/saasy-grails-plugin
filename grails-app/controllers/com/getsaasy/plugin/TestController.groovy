package com.getsaasy.plugin

import grails.converters.*

class TestController {
	def grailsApplication
	private getTestService() {
        def typeName = params.typeName ?: ''
        def rtn = grailsApplication.mainContext['saasy'+typeName.capitalize()+'Service']
        if (!rtn) throw new Exception('saasy'+typeName.capitalize()+'Service not found')
        rtn
	}

    def submit() {

    }

    def command() {
        params[params.typeName] = readPayload()
        params.id = params.id ?: params[params.typeName]?.id
        params.externalId = params.externalId ?: params[params.typeName]?.externalId
        def output = testService."${params.command}"(params)
        if (output instanceof Boolean) {
            output = [success:output]

        }
        render output as JSON
    }

    private readPayload() {
        def payload
        if (request.format == 'json') {
            payload = replaceJSONNulls(request.JSON)
        }
        payload
    }

    private replaceJSONNulls( updates ) {
        // replace JSONOBject.NULL with real null
        def nullValueKeyList   = [];
        updates.each {k,v->
          if( v == org.codehaus.groovy.grails.web.json.JSONObject.NULL ) {
              nullValueKeyList.add( k );
          }
        }
        nullValueKeyList.each {k->updates[ k ] = null}
        return updates
    }


}
