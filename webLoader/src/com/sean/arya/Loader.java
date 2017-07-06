package com.sean.arya;

import java.util.HashSet;
import java.util.concurrent.Callable;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 * 页面多线程执行的实际类
 * @author Sean
 *
 */
public class Loader implements Callable<HashSet<Node>>{
	private Node node;
	private String base;
	private String pattern;
	private String storge;
	private int timeout=30000;
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public Loader(Node node,String base,String pattern,String storge) {
		this.setBase(base);
		this.node = node;
		this.pattern=pattern;
		this.storge=storge;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	@Override
	public HashSet<Node> call() throws Exception {
		Document doc=Jsoup.connect(node.getUrl()).timeout(timeout).get();
		HTMLParse.findAndReplaceDecorattion(doc,storge,WebLoaderController.hasDownloaded,timeout);
		HashSet<Node> nodes=HTMLParse.findAndReplaceHref(doc,base,pattern,storge,timeout);
		HTMLParse.save(doc,node.getStorge(),node.getEncode());
		return nodes;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

}
