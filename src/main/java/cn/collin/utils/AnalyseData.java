package cn.collin.utils;

import cn.collin.service.ConnDB;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by collin on 17-5-29.
 */
public class AnalyseData {
    @Autowired
    ConnDB connDB;

    String contractID = "";
    String id1, id2, id3;
    JSONObject indexData = new JSONObject();
    JSONObject jsonResult = new JSONObject();

    private JSONObject jsonObject = new JSONObject();
    private JSONArray jArray = new JSONArray();
    private Map<Integer, String> sortMap = new TreeMap<>();
    JSONObject j1;
    JSONObject j2;
    JSONObject j3;
    int d1;
    int d2;
    int d3;
    String key1;
    String key2;
    String key3;
    long maxcheckTime;
    long maxreplyTime;
    long minCheckTime;
    long minReplyTime;
    long avCheckTime;
    long avReplyTime;
    int[] data;
    long[] reply;
    long[] check;
    String[] labels;
    JSONObject response = new JSONObject();
    JSONArray a1 = new JSONArray();
    JSONArray a2 = new JSONArray();
    JSONArray a3 = new JSONArray();
    String currentTime = "";
    String sql = "";
    private SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss:SSS");

    public JSONObject analyseData (List<Map<String, Object>> list) {
        jArray.clear();
        indexData.clear();
        System.out.println(list.size());
        JSONArray temp1 = composeIndex(list);
        indexData = computeData(temp1);

        return indexData;
    }

    public JSONObject computeData (JSONArray resultArray) {

        data = new int[5];
        labels = new String[5];
        reply = new long[5];
        check = new long[5];
        int size = resultArray.size();
//        System.out.println("size"+size);
        long start = resultArray.getJSONObject(0).getLong("startTime");
        long end = resultArray.getJSONObject(size - 1).getLong("startTime");
        double interval = Math.rint((end - start) / 5);
        long t1 = (long) (start + interval);
        long t2 = (long) (start + 2 * interval);
        long t3 = (long) (start + 3 * interval);
        long t4 = (long) (start + 4 * interval);
        labels[0] = formatter.format((Math.rint(start+t1)/2)).substring(11);
        labels[1] = formatter.format((Math.rint(t1+t2)/2)).substring(11);
        labels[2] = formatter.format((Math.rint(t2+t3)/2)).substring(11);
        labels[3] = formatter.format((Math.rint(t3+t4)/2)).substring(11);
        labels[4] = formatter.format((Math.rint(t4+end)/2)).substring(11);


        for (int i=0; i<size; i++){
            long iStart = resultArray.getJSONObject(i).getLong("startTime");
            long checkInterval = resultArray.getJSONObject(i).getLong("checkInterval");
            long replyInterval = resultArray.getJSONObject(i).getInt("respInterval");
            avCheckTime += checkInterval;
            avReplyTime += replyInterval;
            if (maxcheckTime == 0 && minCheckTime == 0){
                maxcheckTime = checkInterval;
                minCheckTime = checkInterval;
                maxreplyTime = replyInterval;
                minReplyTime = replyInterval;
            } else {
                if (maxreplyTime < replyInterval){
                    maxreplyTime = replyInterval;
                } else if (minReplyTime > replyInterval) {
                    minReplyTime = replyInterval;
                }
                if (maxcheckTime < checkInterval) {
                    maxcheckTime = checkInterval;
                } else if (minCheckTime > checkInterval) {
                    minCheckTime = checkInterval;
                }
            }

            if (iStart < t1) {
                data[0]++;
                reply[0] += replyInterval;
                check[0] += checkInterval;
            } else if (iStart < t2) {
                data[1]++;
                reply[1] += replyInterval;
                check[1] += checkInterval;
            } else if (iStart < t3) {
                data[2]++;
                reply[2] += replyInterval;
                check[2] += checkInterval;
            } else if (iStart < t4) {
                data[3]++;
                reply[3] += replyInterval;
                check[3] += checkInterval;
            } else {
                data[4]++;
                reply[4] += replyInterval;
                check[4] += checkInterval;
            }

        }
        for (int j=0; j<data.length; j++){
            if (data[j] != 0) {
                reply[j] = reply[j]/data[j];
                check[j] = check[j]/data[j];
            }
        }
        avReplyTime = avReplyTime / size;
        avCheckTime = avCheckTime / size;
//        System.out.println("size"+size);

        jsonResult.put("totalAmount", size);
        jsonResult.put("chaincodeId", contractID);
        jsonResult.put("startTime", formatter.format(start));
        jsonResult.put("endTime", formatter.format(end));
        jsonResult.put("maxCheckTime", maxcheckTime);
        jsonResult.put("minCheckTime", minCheckTime);
        jsonResult.put("minReplyTime", minReplyTime);
        jsonResult.put("maxReplyTime", maxreplyTime);
        jsonResult.put("avCheckTime", avCheckTime);
        jsonResult.put("avReplyTime", avReplyTime);
        jsonResult.put("data", data);
        jsonResult.put("labels", labels);
        jsonResult.put("reply", reply);
        jsonResult.put("check", check);
//        System.out.println(jsonObject.get("totalAmount"));
        return jsonResult;
    }

    private JSONArray composeIndex(List<Map<String, Object>> list) {
        JSONArray newArray = new JSONArray();
        System.out.println(list);
        for (Map<String, Object> map : list){
            Long startTime = (long)map.get("invoke_begin");
            Long endTime = (long)map.get("invoke_end");
            Long checkTime = (long)map.get("check_time");
            long respInterval = endTime - startTime;
            long checkInterval = checkTime - startTime;
            jsonObject.put("respInterval", respInterval);
            jsonObject.put("checkInterval", checkInterval);
            jsonObject.put("startTime",startTime);
            newArray.add(jsonObject);
        }
        System.out.println(newArray.toString());
        return newArray;
    }
}
