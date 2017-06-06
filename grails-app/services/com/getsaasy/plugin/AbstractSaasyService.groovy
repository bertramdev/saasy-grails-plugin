package com.getsaasy.plugin

import groovyx.net.http.FromServer
import groovyx.net.http.NativeHandlers
import org.springframework.context.i18n.LocaleContextHolder
import java.text.*
import groovyx.net.http.ContentTypes
import groovyx.net.http.HttpBuilder
import groovyx.net.http.HttpVerb

import static groovyx.net.http.ContentTypes.*
import static groovyx.net.http.HttpBuilder.configure
import static groovyx.net.http.HttpVerb.*


abstract class AbstractSaasyService {
	def grailsApplication
	static IDS = 'ids'
	static ATTRIBUTES = 'attributes'

	protected getApiKey() {
		if (grailsApplication.config.saasy.containsKey('apiKey')) {
			return grailsApplication.config.saasy.apiKey
		} else {
			throw new Exception('Missing Saasy API Key configuration')
		}
	}

	protected getBaseUrl() {
		if (grailsApplication.config.saasy.containsKey('baseUrl')) {
			return grailsApplication.config.saasy.baseUrl
		} else {
			throw new Exception('Missing Saasy base URL configuration')
		}
	}

	protected getTokenUrl() {
		if (grailsApplication.config.saasy.containsKey('tokenUrl')) {
			return grailsApplication.config.saasy.tokenUrl
		} else {
			return baseUrl + '/api/auth/createToken'
		}
	}

	protected getTokenTtl() {
		if (grailsApplication.config.saasy.containsKey('tokenTtl')) {
			return grailsApplication.config.saasy.tokenTtl
		} else {
			return '60'
		}
	}

	static String _token = ''

	protected getToken() {
		synchronized(_token) {
			return _token
		}
	}

	protected newToken() {
		synchronized(_token) {
			def apiParams = [
				uri: tokenUrl,
				urlParams:[
					apiKey:apiKey,
					ttl:tokenTtl, 
					format:'json'
				]
			]
			def output = doHttp(apiParams)
			def newToken = output.content?.token
			_token = newToken ?: ''
			return _token
		}
	}


	protected doApiCall(String path, Map urlParams = [:], Map body = null, HttpVerb method = null ) {
    	def apiParams = [:]
    	apiParams.urlParams = [apiKey:apiKey, format:'json', token:token]
    	if (body) {
    		apiParams.body = body
    		if (!method) method = POST
    	}
    	if (!method) method = GET
    	if (urlParams) apiParams.urlParams += urlParams
    	['controller','action','typeName','command'].each {
    		apiParams.urlParams.remove(it)
    	}
    	apiParams.uri = (baseUrl+path)
    	def output = doHttp(apiParams,method)
		log.debug "doApiCall output: ${output}"
    	if (output.status == 401) {
    		apiParams.urlParams.token = newToken()
    		if (apiParams.urlParams.token) output = doHttp(apiParams,method)
    	}
    	if (output.status != 200 ||  output.content?.success == false) {
    		def msg = output.content?.msg 
			if (!msg && output.content?.errorMessages) msg = output.content.errorMessages.join('; ')
			if (!msg && output.content?.error) msg = output.content.error
			if (!msg && output.content?.errMsg) msg = output.content.errMsg

    		if (!msg) {
    			try { msg =  output.message.getReasonPhrase() } catch(Throwable t) {}
    		}
    		if (!msg) {
    			msg = output.message?.toString() ?: 'Unknown exception'
    		}

    		throw new SaasyException(msg, output.status, output.content)
    	}
    	return output.content
	}

	protected doHttp(Map params, HttpVerb method = GET, ContentTypes type = groovyx.net.http.ContentTypes.JSON) {
//		HTTPBuilder http = new HTTPBuilder(params.uri ?: params.path)
		def output = [:]

		log.debug "params: ${params}"

		def opts = [:]
		opts.type = type

		if(params.urlParams) {
			def scrubbedKeys = params.urlParams.findAll { it.value }.collect { it.key }
			log.info "doHttp scrubbedKeys: ${scrubbedKeys}"
			params.urlParams = params.urlParams.subMap(scrubbedKeys)
		}

		opts.params = params

		switch(method) {
			case POST:
				output = doPost(opts)
				break
			default:
				output = doGet(opts)
		}
/*
		log.info("doHttp() method: ${method}, type: ${type}, params: ${params}")
		log.debug("DEBUG!!!")

		// if timeouts exist
		if (params.connectionTimeout)
			http.client.params.setParameter('http.connection.timeout', params.connectionTimeout)
		else
			http.client.params.setParameter('http.connection.timeout', 30000)

		if (params.readTimeout)
			http.client.params.setParameter('http.socket.timeout', params.readTimeout)
		else 
			http.client.params.setParameter('http.socket.timeout', 60000)
		def output = [:]
		// make the http request
		try {
			http.request(method, type) { req ->
				if (params.path)
					uri.path = params.path.toString()

				// If we have request headers set them
				if (params.headers) {
					params.headers.each { key, val ->
						headers."${key}" = val.toString()
					}
				}
				headers['Content-Type'] = type.toString()
				log.info "headers: ${headers.dump()}"

				// If supplied username/password default to basic auth
				if (params.username && params.password) {
					def creds = "${params.username}:${params.password}"
					headers.Authorization = "Basic ${creds.getBytes().encodeBase64().toString()}".toString()
				}

				if (params.isForm)
					requestContentType = URLENC

				// if we have urlParams, add those
				if (params.urlParams) uri.query = params.urlParams
				log.info "uri: ${uri?.dump()}"
				log.info "uri.host: ${uri?.host}"
				output.url = uri.toString()
				if (params.body) {
					// TRICKY: For json requests, GStrings in maps can cause bad serialization, so we serialize by hand
					// and set as a String
					if (type == ContentType.JSON && !params.isForm && (params.body instanceof Map || params.body instanceof List))
						body = (params.body as grails.converters.JSON).toString()
					else
						body = params.body
				}

				response.success = { resp, respBody ->
					output.status = resp.status
					output.headers = resp.headers
					output.contentType = resp.contentType
					output.rawText = respBody.text
					switch (type) {
						case TEXT: // if this type, respBody is a reader.  Convert to text
							output.content = respBody.text
							break

						default:
							output.content = respBody // JSON object, or XML object
							break
					}
				}	

				// failure handler
				response.failure = { resp, reader ->
					//println '> FAILURE '+reader?.class
					output << [
						status:resp.status,
						message:resp.statusLine,
						rawText:reader?.text,
						content:reader // if there is any
					]
				}
			}
		}
		catch (Throwable t) {
			output << [
				status:0,
				message:"Failed to use http resource ${output.url}: ${t.class.name} - ${t.message}"
			]
			//log.error(output.message, t)
		}
		//println output
		*/
		return output
	}

	protected transformSuccess(returnMap) throws SaasyException {
		if (!returnMap.success)
			throw new SaasyException(returnMap.msg, returnMap.msgCode, returnMap)

        returnMap.success
	}

	protected transformGetOutput(returnMap) throws SaasyException  {
		if (!returnMap.success)
			throw new SaasyException(returnMap.msg, returnMap.msgCode, returnMap)

        def obj = returnMap.data
        if (obj instanceof List && obj?.size() > 0) obj = obj.getAt(0)
        convert(obj)
	}

	protected transformPostOutput(returnMap) throws SaasyException  {
		if (!returnMap.success)
			throw new SaasyException(returnMap.msg, returnMap.msgCode, returnMap)
        def obj = returnMap.data
        convert(obj)
	}

	protected transformListOutput(returnMap) throws SaasyException  {
		if (!returnMap.success)
			throw new SaasyException(returnMap.msg, returnMap.msgCode, returnMap)
        returnMap.data?.each {obj->
            // convert top level dates
            convert(obj)
            // convert nested dates if necessary
        }
        returnMap.items = returnMap.remove('data')
        returnMap.remove('success')
        if (returnMap.offset) try { returnMap.offset = Integer.parseInt(returnMap.offset.toString())} catch(Throwable t) {}
        if (returnMap.max) try { returnMap.max = Integer.parseInt(returnMap.max.toString())} catch(Throwable t) {}
        returnMap
	}

	protected convert(item) {
		if (item instanceof Map) {
			convertDates(item)
			convertFloats(item)
			item.keySet().each {k->
				// cnovert ids
				if (item[k] && (k == 'id' || k.endsWith('Id'))) {
					try { item[k] = Long.parseLong(item[k].toString()) } catch(Throwable t) {}
				}
				else if (item[k] && k.endsWith('Ids')) {
					try { item[k] = item[k].collect{Long.parseLong(it.toString())} } catch(Throwable t) {}
				}
				convert(item[k])
			}
			replaceJSONNulls(item)		
		}
		else if (item instanceof List) {
			item.each {
				convert(it)
			}
		}
		item
	}

    protected replaceJSONNulls( updates ) {
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

	protected convertFloats(obj) {
		['chargeAmount', 'overrideChargeAmount', 'refundAmount', 'overridePrice', 'shippingWeight', 'shippingAmount','taxRate', 'taxAmount', 'originalAmount', 'amount', 'refundedAmount', 'price', 'price2', 'price3', 'price4', 'price5'].each {
			if (obj.containsKey(it) && obj."$it") obj."$it" = Float.parseFloat(obj."$it".toString())
		}
	}

	protected convertDates(obj) {
		['dateCreated', 'lastUpdated', 'datePlaced',  'start', 'end', 'effectiveStart', 'effectiveEnd','canceled', 'expires', 'dateShipped', 'lastInvoiced', 'nextInvoiced', 'lastOrderDate', 'nextOrderDate'].each {
			if (obj.containsKey(it)) obj."$it" = toDate(obj."$it")
		}
	}


	protected toDate(val) {
		if (!val) return val

		def rtn
		if (val instanceof Date)  rtn = val
		if (!rtn) {
			//2014-07-17T22:02:05Z
			try {
				String input = val.toString()
				SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" )
				if ( input.endsWith( "Z" ) ) {
					input = input.substring( 0, input.length() - 1) + "GMT-00:00"
				} else {
					int inset = 6
					String s0 = input.substring( 0, input.length() - inset )
					String s1 = input.substring( input.length() - inset, input.length() )
					input = s0 + "GMT" + s1
				}
				rtn = df.parse( input )
			} catch (Throwable t1) {
				log.error t1
			}
		}
		if (!rtn) {
			try {
				def dateFormat
				try {
					def messageSource = grails.util.Holders.grailsApplication.mainContext['messageSource']
					dateFormat = messageSource.getMessage('com.bertramlabs.plugins.datePicker.dateFormat', null, LocaleContextHolder.locale)
				} catch (Throwable t) {
					log.error t
				}
				dateFormat = dateFormat ?: 'MM/dd/yyyy'
				SimpleDateFormat df = new SimpleDateFormat( dateFormat )
				rtn = df.parse( val.toString() )
			} catch (Throwable t1) {
				log.error t1
			}
		}
		if (!rtn) rtn = val
		return rtn
	}

	def doGet(opts = [:]) {
		def output = [:]
		log.info "doGet opts: ${opts}"
		def params = opts.params ?: [:]
		def type = opts.type ?: JSON.getAt(0)
		log.info "doGet params: ${params}"
		log.info "doGet type: ${type}"
		def result = configure {

			// If we have request headers set them
			if (params.headers) {
				params.headers.each { key, val ->
					request.headers."${key}" = val.toString()
				}
			}
			request.headers['Content-Type'] = type.getAt(0)
			request.contentType = type.getAt(0)
			log.info "goGet headers: ${request.headers.dump()}"
			log.info "goGet request.contentType: ${request.contentType}"

			// If supplied username/password default to basic auth
			if (params.username && params.password) {
				def creds = "${params.username}:${params.password}"
				request.headers.Authorization = "Basic ${creds.getBytes().encodeBase64().toString()}".toString()
			}

			if(params.uri) {
				log.info "doGet request.uri?.dump() before: ${request.uri?.dump()}"
				request.uri = params.uri
				log.info "doGet request.uri?.dump() after: ${request.uri?.dump()}"
				log.info "doGet params.uri: ${params.uri}"
			} else {
				log.info "doGet params.uri doesn't exist"
			}

		}.get {
			if (params.urlParams) {
				def scrubbedKeys = params.urlParams.findAll { it.value }.collect { it.key }
				log.info "doGet scrubbedKeys: ${scrubbedKeys}"
				def scrubbedParams = params.urlParams.subMap(scrubbedKeys)
				log.info "doGet scrubbedParams: ${scrubbedParams}"
				request.uri.query = scrubbedParams
				log.info "doGet request.uri.query after: ${request.uri?.query}"
			} else {
				log.info "doGet params.urlParams doesn't exist"
			}

			if (params.path) {
				log.info "doGet request.uri: ${request.uri}"
				log.info "doGet params.path: ${params.path}"
				request.uri.path = params.path.toString()
			} else {
				log.info "doGet params.path doesn't exist"
			}
			log.info "goGet request: ${request.dump()}"

			response.success { FromServer fs, Object body ->
				log.info "goGet fs: ${fs.dump()}"
				log.info "goGet body.dump(): ${body.dump()}"
				output.url = fs.uri.toString()
				output.status = fs.statusCode
				output.headers = fs.headers
				output.contentType = fs.contentType
				switch(output.contentType) {
					case TEXT:
						output.content = body?.toString()
						break
					default:
						output.content = body
						break
				}
				body
			}
			
			response.failure { FromServer fs, Object body ->
				output.url = fs.uri.toString()
				output.status = fs.statusCode
				output.headers = fs.headers
				output.contentType = fs.contentType
				switch(output.contentType) {
					case TEXT:
						output.content = body?.toString()
						break
					default:
						output.content = body
						break
				}				
			}
		}
		log.info "goGet result: ${result}"
		log.info "goGet output: ${output}"
		output
	}

	def doPost(opts = [:]) {
		def output = [:]
		log.info "doPost opts: ${opts}"
		def params = opts.params ?: [:]
		def type = opts.type ?: JSON.getAt(0)
		log.info "doPost params: ${params}"
		log.info "doPost type: ${type}"
		def result = configure {

			// If we have request headers set them
			if (params.headers) {
				params.headers.each { key, val ->
					request.headers."${key}" = val.toString()
				}
			}
			request.headers['Content-Type'] = type.getAt(0)
			request.contentType = type.getAt(0)
			log.info "goPost headers: ${request.headers.dump()}"

			// If supplied username/password default to basic auth
			if (params.username && params.password) {
				def creds = "${params.username}:${params.password}"
				request.headers.Authorization = "Basic ${creds.getBytes().encodeBase64().toString()}".toString()
			}

			if(params.uri)
				request.uri = params.uri

		}.post {
			if (params.urlParams) request.uri.query = params.urlParams

			if(params.isForm)
				request.contentType = URLENC.getAt(0)

			if (params.path)
				request.uri.path = params.path.toString()

			if(params.body) {
				log.info "goPost body: ${params.body}"
				if(type == JSON.getAt(0) && !params.isForm && (params.body instanceof Map || params.body instanceof List))
					request.body = (params.body as grails.converters.JSON).toString()
				else
					request.body = params.body
			}

			log.info "goPost request: ${request.dump()}"

			response.success { FromServer fs, Object body ->
				log.info "goPost fs: ${fs.dump()}"
				log.info "goPost body.dump(): ${body.dump()}"
				output.url = fs.uri.toString()
				output.status = fs.statusCode
				output.headers = fs.headers
				output.contentType = fs.contentType
				switch(output.contentType) {
					case TEXT:
						output.content = body.toString()
						break
					default:
						output.content = body
						break
				}
				body
			}

			response.failure { FromServer fs, Object body ->
				output.url = fs.uri.toString()
				output.status = fs.statusCode
				output.headers = fs.headers
				output.contentType = fs.contentType
				switch(output.contentType) {
					case TEXT:
						output.content = body?.toString()
						break
					default:
						output.content = body
						break
				}
			}
		}
		log.info "goPost result: ${result}"
		log.info "goPost output: ${output}"
		output
	}
}
