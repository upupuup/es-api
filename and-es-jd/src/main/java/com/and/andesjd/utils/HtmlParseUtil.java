package com.and.andesjd.utils;

import com.and.andesjd.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author:         jiangzhihong
 * @CreateDate:     2021/4/4 13:47
 */
public class HtmlParseUtil {
	public static void main(String[] args) throws Exception {
		new HtmlParseUtil().parseJD("java").forEach(System.out::println);
	}

	public List<Content> parseJD(String keywords) throws Exception {
		String url = "https://search.jd.com/Search?keyword=" + keywords;
		Document document = Jsoup.parse(new URL(url), 3000);
		Element element = document.getElementById("J_goodsList");
		Elements elements = element.getElementsByTag("li");
		List<Content> goodsList = new ArrayList<>();
		for (Element el : elements) {
			String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
			String price = el.getElementsByClass("p-price").eq(0).text();
			String title = el.getElementsByClass("p-name").eq(0).text();

			goodsList.add(new Content(title, img, price));
		}
		return goodsList;
	}
}
