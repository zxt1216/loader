package com.sean.arya;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Base64Test {

	public static void main(String[] args) throws IOException {
		Document doc=Jsoup.connect("http://www.runoob.com/scala/scala-tutorial.html").timeout(3000).get();
		Elements media = doc.select("[src]");
		for (Element link : media) {
			if (link.tagName().equals("img")) {
				String src = link.attr("src");
				System.out.println(src);
			}
		}

	}

}
