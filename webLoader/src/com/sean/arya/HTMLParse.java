package com.sean.arya;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 负责使用Jsoup处理节点
 * @author Sean
 *
 */
public class HTMLParse {
	//查找替换a href,并返回Node
	public static HashSet<Node> findAndReplaceHref(Document doc,String base,String pattern,String storge,int timeout){
		System.out.println("findAndReplaceHref");
		HashSet<Node> res=new HashSet<Node>();
		Elements alinks=doc.select("a[href]");
		for(Element link:alinks){
			String href=link.attr("abs:href");
			if(href.startsWith(base)){
				String sub=href.substring(base.length());
				String dirs=StringUtils.substringBeforeLast(sub, "/");
				if(dirs.startsWith("/"))dirs=dirs.substring(1);
				String name=getFileName(href);
				if(dirs.startsWith(pattern) || dirs.matches(pattern)){
					//替换
					res.add(new Node(href,storge+"/html/"+name,"utf-8"));
					link.attr("href","../html/"+name);
				}
			}
		}
		return res;
	}
	//查找替换下载 js/css/frame/flash/other/img
	public static void findAndReplaceDecorattion(Document doc,String storge,ConcurrentHashMap<String, String> hasDownloaded,int timeout){
		System.out.println("findAndReplaceDecorattion");
		Elements csslinks=doc.select("link[href]");
		for(Element link:csslinks){
			if (link.attr("rel").equals("stylesheet")) {// 如果rel属性为HtmlFileLink
				String href = link.attr("abs:href");// 得到css样式的href的绝对路径
				// 如http://news.baidu.com/resource/css/search.css
				//下载css
				//css地址也有一些有?参数要从中筛选name
				String name=getFileName(href);
				if(!hasDownloaded.containsKey(href)){
					synchronized(HTMLParse.class){
						if(!hasDownloaded.containsKey(href)){
							download(href,storge+"/css/"+name,timeout);
						}
					}
				}
				//替换css
				link.attr("href","../css/"+name);
			}
		}
		Elements media = doc.select("[src]");
		for (Element link : media) {
			if (link.tagName().equals("img")) {
				String src = link.attr("abs:src");
				if(src.trim().equals("") || src== null){
					String baseSrc=link.attr("src");
					if(baseSrc.startsWith("data:image"))continue;
					else System.out.println("error parse the image url!");
				}
				String name=getFileName(src);
				if(!hasDownloaded.containsKey(src)){
					synchronized(HTMLParse.class){
						if(!hasDownloaded.containsKey(src)){
							download(src,storge+"/img/"+name,timeout);
						}
					}
				}
				//替换
				link.attr("src","../img/"+name);
				continue;
			}
			if (link.tagName().equals("input")) {
				if (link.attr("type").equals("Image")) {
					String src = link.attr("abs:src");
					//对于inline base64编码的的图像数据,abs:src获取到的值为空;故重新获取，如果是base64编码则直接跳过
					if(src.trim().equals("") || src== null){
						String baseSrc=link.attr("src");
						if(baseSrc.startsWith("data:image"))continue;
						else System.out.println("error parse the image url!");
					}
					String name=getFileName(src);
					if(!hasDownloaded.containsKey(src)){
						synchronized(HTMLParse.class){
							if(!hasDownloaded.containsKey(src)){
								download(src,storge+"/img/input."+name,timeout);
							}
						}
					}
					//替换
					link.attr("src","../img/input."+name);
					continue;
				}
			}
			if (link.tagName().equals("javascript")
					|| link.tagName().equals("script")) {
				String src = link.attr("abs:src");
				String name=getFileName(src);
				if(!hasDownloaded.containsKey(src)){
					synchronized(HTMLParse.class){
						if(!hasDownloaded.containsKey(src)){
							download(src,storge+"/js/"+name,timeout);
						}
					}
				}
				//替换
				link.attr("src","../js/"+name);
				continue;
			}
			if (link.tagName().equals("iframe")) {
				String src = link.attr("abs:src");
				String name=getFileName(src);
				if(!hasDownloaded.containsKey(src)){
					synchronized(HTMLParse.class){
						if(!hasDownloaded.containsKey(src)){
							download(src,storge+"/frame/"+name,timeout);
						}
					}
				}
				//替换
				link.attr("src","../frame/"+name);
				continue;
			}
			if (link.tagName().equals("embed")) {
				String src = link.attr("abs:src");
				String name=getFileName(src);
				if(!hasDownloaded.containsKey(src)){
					synchronized(HTMLParse.class){
						if(!hasDownloaded.containsKey(src)){
							download(src,storge+"/flash/"+name,timeout);
						}
					}
				}
				//替换
				link.attr("src","../flash/"+name);
				continue;
			}
		}
	}
	//下载资源
	private static void download(String url,String storge,int timeout){
		try {
			FileUtils.copyURLToFile(new URL(url), new File(storge),timeout,timeout);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void save(Document doc,String storge,String encode) {
		System.out.println("save");
		String html=doc.html();
		html.replaceAll("\n", "\r\n");
		try {
			FileUtils.writeStringToFile(new File(storge), html,encode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String getPostfix(String filename){
		filename = StringUtils.substringAfterLast(filename, ".");
		filename = StringUtils.substringBefore(filename, "?");
		filename = StringUtils.substringBefore(filename, "/");
		filename = StringUtils.substringBefore(filename, "\\");
		filename = StringUtils.substringBefore(filename, "&");
		filename = StringUtils.substringBefore(filename, "$");
		filename = StringUtils.substringBefore(filename, "%");
		filename = StringUtils.substringBefore(filename, "#");
		filename = StringUtils.substringBefore(filename, "@");
		return filename;
	}
	public static String getFileName(String filename){
		String name=new String(filename);
		name=StringUtils.substringBefore(name, "?");
		name=StringUtils.substringBeforeLast(name, ".");
		if(name.contains("/"))name=StringUtils.substringAfterLast(name, "/");
		filename = StringUtils.substringBefore(filename, "?");
		filename = StringUtils.substringAfterLast(filename, ".");		
		filename = StringUtils.substringBefore(filename, "/");
		filename = StringUtils.substringBefore(filename, "\\");
		filename = StringUtils.substringBefore(filename, "&");
		filename = StringUtils.substringBefore(filename, "$");
		filename = StringUtils.substringBefore(filename, "%");
		filename = StringUtils.substringBefore(filename, "#");
		filename = StringUtils.substringBefore(filename, "@");
		return name+"."+filename;
	}
}
