package com.sean.arya;


/**
 * ½Úµã
 * @author Sean
 *
 */
public class Node {
	private String url;
	private String storge;
	private String encode="utf-8";

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getStorge() {
		return storge;
	}
	public void setStorge(String storge) {
		this.storge = storge;
	}

	public Node( String url, String storge, String encode) {

		this.url = url;
		this.storge = storge;
		this.encode = encode;
	}
	public boolean equals(Object o){
		if(o instanceof Node){
			Node n=(Node)o;
			if(n.getUrl().equals(url) && n.getStorge().equals(storge)){
				return true;
			}
		}
		return false;
	}
	public int hashCode(){
		return url.hashCode()*31+storge.hashCode();
	}
	public String getEncode() {
		return encode;
	}
	public void setEncode(String encode) {
		this.encode = encode;
	}
}
