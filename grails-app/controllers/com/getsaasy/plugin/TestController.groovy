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

    def index() {

    }

    def command() {
        def output = [success:false]
        try {
            if (params.command == 'create' || params.command == 'udpate')
                params[params.typeName] = readPayload()
            else {
                def body = readPayload()
                body.each {k,v->
                    params[k] = v
                }
            }
            params.id = params.id ?: params[params.typeName]?.id
            params.externalId = params.externalId ?: params[params.typeName]?.externalId
            output = testService."${params.command}"(params)
            if (output instanceof Boolean) {
                output = [success:output]
            }
        } catch (Throwable t) {
            try { output.data = t.data } catch(Throwable t1) {}
            try { output.responseCode = t.responseCode } catch(Throwable t1) {}
            output.throwableToString = t.toString()
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
