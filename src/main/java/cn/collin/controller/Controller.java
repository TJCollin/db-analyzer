package cn.collin.controller;

import cn.collin.service.ConnDB;
import cn.collin.users.Users;
import cn.collin.utils.AnalyseData;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Created by collin on 17-5-29.
 */
@RestController
@RequestMapping("/analyser")
public class Controller {
    @Autowired
    private ConnDB connDB;
    JSONObject response = new JSONObject();

    private String contractID;
    private long startTime, endTime;
    AnalyseData analyseData = new AnalyseData();
    @RequestMapping(value = "/query", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String set(@RequestBody Users users) {
        contractID = users.getContractID();
        startTime = users.getStartTime();
        endTime = users.getEndTime();
        List<Map<String, Object>> list = connDB.createRecord(startTime, endTime, contractID);
        if (!list.isEmpty()){

            response = analyseData.analyseData(list);
            response.put("status","ok");
        } else {
            response.put("status", "fail");
        }
        /*for (Map<String, Object> map : list){
            System.out.println(map.get("data"));
        }*/
//        String response = analyseData.analyseData(list);
//        System.out.println(response);
        return response.toString();
    }

}
