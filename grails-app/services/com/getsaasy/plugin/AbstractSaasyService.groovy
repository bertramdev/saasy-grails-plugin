package com.getsaasy.plugin

import grails.transaction.Transactional

import org.springframework.context.i18n.LocaleContextHolder
import java.text.*
import com.bertramlabs.plugins.*
import grails.util.Holders
import java.lang.reflect.*
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.springframework.context.i18n.LocaleContextHolder as LCH
import grails.converters.*
import java.io.*
import java.util.regex.Pattern

import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*


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


	protected doApiCall(String path, Map urlParams = [:], Map body = null, Method method = null ) {
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
		//println output
    	if (output.status == 401) {
    		apiParams.urlParams.token = newToken()
    		if (apiParams.urlParams.token) output = doHttp(apiParams,method)
    	}
    	if (output.status != 200 ||  output.content?.success == false) {
    		def msg = output.content?.msg 
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

	/**
	 * Base implementation for making calls to http based resources.  <br/>Example usage
	 * <blockquote>
	 * doHttp(
	 *	  uri:'http://api.paypal.com,
	 *	  path:'v1/payments/payment',
	 *	  body:<a map of post params. Or a json object, or xml>,
	 *	  headers:<a map of request headers>,
	 *	  Method.POST,
	 *	  ContentType.JSON
	 * )
	 * </blockquote>
	 * @param params - map of params (named params from method call)
	 * @param method - The http method as defined in groovyx.net.http.Method
	 * @param type - The content type of the request as defined in groovyx.net.http.ContentType
	 */
	protected doHttp(Map params, Method method = GET, ContentType type = groovyx.net.http.ContentType.JSON) {
		HTTPBuilder http = new HTTPBuilder(params.uri ?: params.path)

		//log.info("doHttp() method: ${method}, type: ${type}, params: ${params}")

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

				// If supplied username/password default to basic auth
				if (params.username && params.password) {
					def creds = "${params.username}:${params.password}"
					headers.Authorization = "Basic ${creds.getBytes().encodeBase64().toString()}".toString()
				}

				if (params.isForm)
					requestContentType = URLENC

				// if we have urlParams, add those
				if (params.urlParams) uri.query = params.urlParams
				println uri
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
				response.failure = { resp ->
					output << [
						status:resp.status,
						message:resp.statusLine,
						content:resp.data // if there is any
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

		return output
	}

	protected transformSuccess(returnMap) {
        returnMap.success
	}

	protected transformGetOutput(returnMap) {
        def obj = returnMap.data
        convert(obj)
	}

	protected transformPostOutput(returnMap) {
        def obj = returnMap.data
        convert(obj)
	}

	protected transformListOutput(returnMap) {
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
		['dateCreated', 'lastUpdated', 'start', 'end', 'effectiveStart', 'effectiveEnd','canceled', 'expires', 'dateShipped', 'lastInvoiced', 'nextInvoiced', 'lastOrderDate', 'nextOrderDate'].each {
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
				println t1
			}
		}
		if (!rtn) {
			try {
				def dateFormat
				try {
					def messageSource = grails.util.Holders.grailsApplication.mainContext['messageSource']
					dateFormat = messageSource.getMessage('com.bertramlabs.plugins.datePicker.dateFormat', null, LocaleContextHolder.locale)
				} catch (Throwable t) {
					println t
				}
				dateFormat = dateFormat ?: 'MM/dd/yyyy'
				SimpleDateFormat df = new SimpleDateFormat( dateFormat )
				rtn = df.parse( val.toString() )
			} catch (Throwable t1) {
				println t1
			}
		}
		if (!rtn) rtn = val
		return rtn
	}
}
