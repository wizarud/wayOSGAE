package com.wayos.appengine.servlet;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wayos.Configuration;
import com.wayos.Context;
import com.wayos.PathStorage;
import com.wayos.context.RemoteContext;
import com.wayos.util.Application;

@SuppressWarnings("serial")
@WebServlet("/wayobotSucker")
public class SuckerServlet extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PathStorage storage = (PathStorage) Application.instance().get(PathStorage.class.getName());
		
		List<String> nameList = nameList();
		
		String [] tokens;
		Context context;
		String contextName;
		String botId;	
		String toPath;
		
		StringBuilder sb = new StringBuilder("Sucking..");
		sb.append(System.lineSeparator());
		
		for (String name:nameList) {
			
			tokens = name.split(",");
			contextName = tokens[0];
			botId = "S-" + tokens[1];
			
			//context = new RemoteContext(contextName, "D0ra3m0n", "https://eoss-wayo-bot.appspot.com/s/", "");
			
			//toPath = Configuration.LIB_PATH + "103014451870896" + "/" + botId + ".context";
			toPath = Configuration.PUBLIC_PATH + "103014451870896" + "/" + botId + ".PNG";
			
			sb.append(toPath);
			
			try {
				
				/*
				context.load();
				
				if (context.prop("title")==null) {
					
					context.prop("title", tokens[1].replace("-", " "));
				}
				
				storage.write(context.toJSONString(), toPath);
				*/
				
				storage.write(new URL("https://eoss-wayo-bot.appspot.com/bin/" + contextName).openStream(), toPath);
				
				sb.append(" ..succeed");
				
			} catch (Exception e) {
				
				sb.append(" ..fail " + e.getMessage());
			}
						
			sb.append(System.lineSeparator());
			
		}
		
		response.setContentType("text/plain");
		response.getWriter().print(sb.toString());
		
	}
	
	private List<String> nameList() {
		
		String [] names = new String[] {
				
			"1833768260014999/1555162117935,Basic-Java1",
			"1833768260014999/1557541158294,Basic-Java2",
			"1833768260014999/1573658496182,Basic-Java3",
			"1833768260014999/176242364d0,Pantip",			
			"10212066546578689/1570620834322,Matee-Maturos",
			"2328221677286952/1571123977472,Patter-Thanawat",
			"3052129414858973/1571124053021,Thip-Freedom",
			"1401621179989522/1571124146717,Thadakorn-Kaewsrithat",
			"2242813852683555/1571124169506,Chakkraphong-Sukkasem",
			"2269514716490226/1571124171665,Phetlada-Khumsai",
			"2344044679051918/1571124179342,Wanwipa-Kaewsombut",
			"2332851780268860/1571124184860,Wichuda-Thongsetitpu",
			"978884465799293/1571124197493,Benjawan-Junkaewhang",
			"2821136117931755/1571124201383,Thanyachanok-Tonchai",
			"2462664607182175/1571124225857,Ning-Natthaa",
			"2459476964326988/1571124248469,Nammon-Natthamon",
			"760063754464135/1571124255943,Kannika-Khontong",
			"1436456203178580/1571124256571,Fon-Thitifii",
			"765247847261381/1571124256604,Bunyarat-Mingyai",
			"690957584758089/1571124256893,Hathaichanok-Khamyang",
			"2339809186147827/1571124257481,Chanita-Sukprom",
			"544617779678738/1571124262104,Nong-Nick",
			"833505003773487/1571124272207,Siriluck-Na-Lampang",
			"2332356043554271/1571124291975,Kanyarut-Pichitsakun",
			"2347481405502666/1571380003309,Thanawan-Wangsrirach",
			"3090929107644360/1571650465112,DaDame-Parinan",
			"794760960989127/1572329714609,New-HiPreecha",
			"1037570016586170/1572330184781,Sakawrat-Tongchalam",
			"1297805853722888/1572330405711,Preaw-Somrudee",
			"2909324039100203/1572333487169,Natthanit-Phetcharat",
			"3154657727940443/1572333508479,Natsinee-Kunthananchok",
			"414432439485015/1572333536963,Meaw-Watn",
			"2315242378602699/1572353035185,Ying-Wasithee-Buaphon",
			"2867398400022324/1582597941665,Porntheera-Dechsonthi",
			"2865585553534515/1582598015017,Thanakorn-Kulsornana",
			"2781553408598747/1582598018184,Pattanapong-Mayomhin",
			"2658327754262799/1583201767671,Chinnawat-Dumnurn",
			"1588566751294075/1583287858975,TO-ZU-NG",
			"2773578566088747/1583288335619,Sorapong-Thepsuwan",
			"2667132260064301/1583894418040,Supachai-WongHan",
			"2579309979021345/1583894152219,Kahoot",
			"1733527623445961/1571123958574,noodle",
			"2399441510343365/1571124105149,pool",
			"2361932404058813/1571124198330,thongu",
			"1529784833829674/1571124213824,suganya",
			"2446141462149702/1572331422201,supaweene",
			"1670450776425462/1572335269443,yayee",
			"1280752972121751/1582597961362,dbms1",
			"2571959289594234/1582597989020,dbms2",
			"724811141381814/1582598020758,construct2",
			"2534051613502349/1582598029858,school",
			"2748542448594266/1582598427474,basiccomputer",
			"2499765523623840/1583201864903,dadada"
		};

		return Arrays.asList(names);
	}

}
