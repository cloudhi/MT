package org.fireking.app.mt.service.grab_webpage;

import java.util.ArrayList;
import java.util.List;

import org.fireking.app.mt.common.MT_URI;
import org.fireking.app.mt.model.GuessForyouEntity;
import org.fireking.app.mt.model.ShopPanicBuyEntity;
import org.fireking.app.mt.model.ShopPanicListEntity;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * ץȡ��ҳ���� <br />
 * i.meituan.com/?city=shanghai
 * 
 * @author join
 *
 */
public class GroupGrab {

	/**
	 * ��ȡ�Ź�ҳ����������ݽڵ�
	 * 
	 * @param city
	 * @return
	 * @throws Exception
	 */
	public Document getDocument(String city) throws Exception {
		String uri = "http://i.meituan.com/?city=" + city;
		// uri = uri.replace("{0}", "shanghai");
		Document doc = Jsoup
				.connect(uri)
				.header("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322)")
				.get();
		return doc;
	}

	/**
	 * ��ȡ�������������Ƽ���Ŀ
	 * 
	 * @throws Exception
	 */
	public static List<ShopPanicBuyEntity> getShop_panic_buying(Document doc)
			throws Exception {
		Element dl = doc.select("dl[class=list qianggou]").first();
		Elements divs = dl.select("div[class=qianggoucard]");
		List<ShopPanicBuyEntity> entitys = new ArrayList<ShopPanicBuyEntity>();
		for (int i = 0; i < divs.size(); i++) {
			Element div = divs.get(i);
			Elements campaign_prices = div.select("div[class=campaign-price]");
			String s_cam = "";
			if (campaign_prices != null && campaign_prices.size() != 0) {
				s_cam = campaign_prices.first().text();
			}
			entitys.add(new ShopPanicBuyEntity(i, div
					.select("div[class=img-container]").first().select("img")
					.attr("src"),
					div.select("div[class=brand]").first().text(),
					div.select("div[class=discount-price]").first().text(),
					s_cam));
		}
		return entitys;
	}

	/**
	 * ��ȡ����ϲ�����Ƽ�����
	 * 
	 * @param city
	 * @return
	 * @throws Exception
	 */
	public List<GuessForyouEntity> getGuessForYou(Document doc)
			throws Exception {
		Elements divs = doc.select("div[class=dealcard]");
		List<GuessForyouEntity> entitys = new ArrayList<GuessForyouEntity>();
		for (int i = 0; i < divs.size(); i++) {
			Element div = divs.get(i);
			String image = div.select("div[class=dealcard-img imgbox]").first()
					.attr("data-src");
			String title = div.select("div[class=dealcard-brand single-line]")
					.first().text();
			String description = div.select("div[class=title text-block]")
					.first().text();
			String discount_price = div.select("strong").first().text() + "Ԫ";
			String sale_count = div.select("span[class=line-right]").first()
					.text();
			entitys.add(new GuessForyouEntity(i, image, title, description,
					discount_price, discount_price, sale_count));
		}
		return entitys;
	}

	/**
	 * ץȡ���������б�����
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ShopPanicListEntity> getPanicList() throws Exception {
		String uri = MT_URI.SHOP_PANIC_BUYING_LIST;
		Document doc = Jsoup
				.connect(uri)
				.header("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; SV1; .NET CLR 1.1.4322)")
				.get();
		Element dds = doc.select("dd[class=deal-container]").first();
		Element dls = dds.select("dl[class=list]").first();
		Elements _dls = dls.select("dd");
		List<ShopPanicListEntity> entitys = new ArrayList<ShopPanicListEntity>();
		for (int i = 0; i < _dls.size(); i++) {
			Element dd = _dls.get(i);
			String image = dd.select("div[class=dealcard-img imgbox]").first()
					.attr("data-src");
			String title = dd.select("div[class=dealcard-brand single-line]")
					.first().text();
			String description = dd.select("div[class=title text-block]")
					.first().text();
			String panic_price = dd.select("span[class=strong-color]").first()
					.select("del").first().text();
			String origin_price = dd.select("div[class=price]").first()
					.select("del").first().text();
			entitys.add(new ShopPanicListEntity(0, image, title, description,
					origin_price, panic_price));
		}
		return entitys;
	}
}
