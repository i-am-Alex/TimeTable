package com.alex.timetable.routesparser;

import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;


public class HTMLParser {

	private boolean checkIsDigits(String string) {
        if (string == null || string.length() == 0) return false;

        int i = 0;
        char c;
        for (; i < string.length(); i++) {
            c = string.charAt(i);
            if (!(c >= '0' && c <= '9')) return false;
        }
        return true;
    }
	
	/**
	 * Ищем первого дитя по заданной маске имени тега
	 * @param list
	 * @param mask
	 * @return
	 */
	private List<Node> getSubList(List<Node> list, String mask) {
		Iterator<Node> iterator = list.iterator();
		
		if(mask == null) mask = "";
		
		while(iterator.hasNext()) {
			Node tmp = iterator.next();
			if(mask.equals(tmp.nodeName())) return tmp.childNodes();
		}
		return null;
	}

	/**
	 * Получаем дату последнего обновления маршрута.
	 * @param list
	 * @return
	 */
	private String getDateUpdateShedule(List<Node> list) {
		for(Node node: list) {
			String str = node.outerHtml();
			if(str.indexOf(">з<") > 0) {
				for(Node subNode: node.childNodes()) {
					if(subNode instanceof Element) {
						String data1 = ((Element) subNode).text().trim();
						if(data1.indexOf(" з ") > 0) {
							data1 = data1.substring(data1.indexOf(" з ") + 3, data1.indexOf("на"));
							return data1.trim();
						}
					}
				}
				break;
			}
		}
		return "";
	}

	private String getStationTimeFromNode(Node node) {
		String strTime = "";
		if(node != null && node.childNodeSize() > 0) strTime = node.childNodes().get(0).outerHtml().trim();
		if("&nbsp;".equals(strTime)) strTime = "";
		// подставим ноль в конце, чтобы сортировка работала корректно
		if(strTime != null && strTime.length() > 0 && !strTime.startsWith("1") && !strTime.startsWith("2")) strTime = "0" + strTime;
		return strTime;
	}

	private boolean firstColumnHasNumber(Node hiNode) {
		if(hiNode == null || hiNode.childNodeSize() == 0) return false;
		for(Node rec: hiNode.childNodes()) {
			if(rec instanceof Element) {
				return checkIsDigits(((Element) rec).text());
			}
		}
		return false;
	}
	
	public EXTRoute parse(String htmlText, int routeId, String routeNum, int halfStationsNum, boolean isWorkDays) {
		Document doc = null;
		doc = Jsoup.parse(htmlText);
		if(doc == null) return null;
		Elements element = doc.getElementsByTag("tbody");

		EXTRoute route = new EXTRoute();
		route.setRouteId(routeId);
		route.setRouteNumber(routeNum);
		route.setWorkDays((isWorkDays?1:0));
		
		List<Node> list = element.get(0).childNodes(); 
		route.setLastUpdateDate(getDateUpdateShedule(list));
		
		for(Node hiNode:list) {
			if(hiNode instanceof Element) {
				EXTStop stop = new EXTStop();
				int i = 0;
				
				if(firstColumnHasNumber(hiNode)) {
					for(Node subNode: hiNode.childNodes()) {
						if(subNode instanceof Element) {
							EXTStopTime stopTime = new EXTStopTime();
							stopTime.setFirstDirection((i <= halfStationsNum?0:1));
							stopTime.setPosNum(i);
							stopTime.setStationTime(getStationTimeFromNode(subNode));
							stop.getTimeList().add(stopTime);
							i = i + 1;
						}
					}
				}
				route.addStopIfTimeListNotNull(stop);
			}			
		}
		return route;

	}

}
