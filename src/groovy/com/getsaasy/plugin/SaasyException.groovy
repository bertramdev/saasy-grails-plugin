package com.getsaasy.plugin

class SaasyException extends RuntimeException {
	public int responseCode = 500
	public Map data

	public SaasyException() {
		super('Request could not be completed')
	} 

	public SaasyException(String msg) {
		super(msg)
	} 

	public SaasyException(int code) {
		super()
		this.responseCode = code
	} 


	public SaasyException(String msg, int code) {
		super(msg)
		this.responseCode = code
	} 
	
	public SaasyException(String msg, Throwable cause) {
		super(msg, cause)
	} 

	public SaasyException(int code, Throwable cause) {
		super(cause)
		this.responseCode = code
	} 


	public SaasyException(String msg, int code, Throwable cause) {
		super(msg, cause)
		this.responseCode = code
	} 

	public SaasyException(Map dat) {
		super('Request could not be completed')
		this.data = dat
	} 

	public SaasyException(String msg, Map dat) {
		super(msg)
		this.data = dat
	} 

	public SaasyException(int code, Map dat) {
		super()
		this.responseCode = code
		this.data = dat
	} 


	public SaasyException(String msg, int code, Map dat) {
		super(msg)
		this.responseCode = code
		this.data = dat
	} 
	
	public SaasyException(String msg, Throwable cause, Map dat) {
		super(msg, cause)
		this.data = dat
	} 

	public SaasyException(int code, Throwable cause, Map dat) {
		super(cause)
		this.responseCode = code
		this.data = dat
	} 


	public SaasyException(String msg, int code, Throwable cause, Map dat) {
		super(msg, cause)
		this.responseCode = code
		this.data = dat
	} 

}