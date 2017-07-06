package com.sean.arya;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 负责解析UI信息,调用解析方法
 * @author Sean
 *
 */
public class WebLoaderController {
	private WebLoader ui;
	
	public static ConcurrentHashMap<String, String> hasVisited=new ConcurrentHashMap<String, String>();//url,storge
	public static ConcurrentHashMap<String, String> hasDownloaded=new ConcurrentHashMap<String, String>();//url,storge
	private ExecutorService pool= Executors.newFixedThreadPool(10);
	public WebLoaderController( WebLoader ui) {
		this.ui=ui;
	}
	public  void start(){
		//创建目录
		createDirectory();
		//任务
		HashSet<Loader> tasks=new HashSet<>();
		String name=HTMLParse.getFileName(ui.getPageText());
		tasks.add(new Loader(new Node(ui.getPageText(),ui.getStorgeText()+"/html/"+name,"utf-8"),ui.getBaseText(),ui.getPatternText(),ui.getStorgeText()));
		hasVisited.put(ui.getPageText(),ui.getStorgeText()+"/html/"+name);
		while(tasks.size()>0){
			System.out.println("circle");
			try {
				List<Future<HashSet<Node>>> futures=pool.invokeAll(tasks);
				HashSet<Node> nexts=new HashSet<>();
				for(Future<HashSet<Node>> future:futures){
					nexts.addAll(future.get());
				}
				tasks.clear();
				for(Node n:nexts){
					if(!hasVisited.containsKey(n.getUrl())){
						System.out.println("n.url: "+n.getUrl());
						tasks.add(new Loader(n,ui.getBaseText(),ui.getPatternText(),ui.getStorgeText()));
						hasVisited.put(n.getUrl(), n.getStorge());
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	private void createDirectory(){
		File base=new File(ui.getStorgeText());
		if(!base.exists()){
			base.mkdirs();
		}
		File html=new File(base.getAbsolutePath()+"/html");
		File img=new File(base.getAbsolutePath()+"/img");
		File css=new File(base.getAbsolutePath()+"/css");
		File js=new File(base.getAbsolutePath()+"/js");
		File flash=new File(base.getAbsolutePath()+"/flash");
		File frame=new File(base.getAbsolutePath()+"/frame");
		File other=new File(base.getAbsolutePath()+"/other");
			html.mkdir();
			img.mkdir();
			css.mkdir();
			js.mkdir();
			flash.mkdir();
			frame.mkdir();
			other.mkdir();
	}
}
