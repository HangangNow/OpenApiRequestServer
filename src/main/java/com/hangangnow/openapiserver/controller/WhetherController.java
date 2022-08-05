package com.hangangnow.openapiserver.controller;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class WhetherController {

    @GetMapping("/api/v1/whether")
    public String test() throws IOException, ParseException {
        StringBuilder sb = new StringBuilder("http://apis.data.go.kr/1360000/TourStnInfoService/getTourStnVilageFcst");
        String key = "Yd%2B4JfrCX4n3nNbsxLbLZdXNZoG1PWZLtXL95l8NgEPFJvCNecDBPQKuHUAB9DuNF6RfgLEOPDwCsHVSnaUA%2FQ%3D%3D";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date();
        String date = sdf.format(today);
        String Courese_Id = "58";


        sb.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + key);
        sb.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        sb.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
        sb.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        sb.append("&" + URLEncoder.encode("CURRENT_DATE", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8"));
        sb.append("&" + URLEncoder.encode("HOUR", "UTF-8") + "=" + URLEncoder.encode("24", "UTF-8"));
        sb.append("&" + URLEncoder.encode("COURSE_ID", "UTF-8") + "=" + URLEncoder.encode(Courese_Id, "UTF-8"));
        URL url = new URL(sb.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        System.out.println("conn code = " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb2 = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            sb2.append(line);
        }

        JSONObject result = (JSONObject) new JSONParser().parse(sb2.toString());
        JSONObject response = (JSONObject) result.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items. get("item");

        JSONObject temp;
        StringBuilder out = new StringBuilder();
        for(int i=0; i<item.size();i++)
        {
            temp = (JSONObject) item.get(i);
            out.append("온도 : "+temp.get("th3")+" ");
            out.append("하늘 : " +temp.get("sky")+" ");
            out.append("습도 : " +temp.get("rhm")+" ");
            out.append("강수확률 : " +temp.get("pop")+"\n");
            System.out.println(out);
            out.setLength(0);
        }
        rd.close();
        conn.disconnect();

        return "ok";
    }

}
