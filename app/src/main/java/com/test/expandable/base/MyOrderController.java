package com.test.expandable.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrderController {


    private void praseJSON(String result) {

        String str = "{ \"code\": \"0000\", \"desc\": null," +
                " \"token\": \"ad762d27-ced6-4092-b415-ddad8ee0b98e__1472123395714\", " +
                "\"msg\": [ { \"amount\": 601, \"consignee\": \"andrea\"," +
                " \"address\": \"天府软件园A区\"," +
                " \"orderItem\": [{ \"thumbnail\": null, \"quantity\": 1, \"price\": 601," +
                " \"name\": \"熊猫座椅\", \"id\": 11 } ], \"freight\": 0, \"orderStatus\": \"unconfirmed\", " +
                "\"productCount\": 1, \"shippingStatus\": \"unshipped\", \"phone\": \"15892999216\", " +
                "\"areaName\": \"四川省成都市\", \"id\": 9, \"sn\": \"20160825202\", " +
                "\"paymentStatus\": \"unpaid\", \"createDate\": 1472123141000 }, { \"amount\": 601, \"consignee\": \"andrea\"," +
                " \"address\": \"天府软件园A区\", \"orderItem\": [{ \"thumbnail\": null, \"quantity\": 1," +
                " \"price\": 601, \"name\": \"熊猫座椅\", \"id\": 10 } ], \"freight\": 0, \"orderStatus\": \"unconfirmed\"," +
                " \"productCount\": 1, \"shippingStatus\": \"unshipped\", \"phone\": \"15892999216\"," +
                " \"areaName\": \"四川省成都市\", \"id\": 8, \"sn\": \"20160825102\", " +
                "\"paymentStatus\": \"unpaid\", \"createDate\": 1472122855000 } ], " +
                "\"page\": { \"total\": 8, \"pageNumber\": 1, \"pageSize\": 2 } }";


        try {
            JSONObject json = new JSONObject(result);
            String code = json.optString("code");
            String desc = json.optString("desc");
            String token = json.optString("token");
            JSONArray msg = json.optJSONArray("msg");

          List<Map<String, String>> mapList = new ArrayList<>();

            for (int a = 0; a < msg.length(); a++) {// 遍历数组
                String amount = msg.optJSONObject(a).optString("amount");
                String consignee = msg.optJSONObject(a).optString("consignee");
                String address = msg.optJSONObject(a).optString("address");
                JSONArray orderItem = msg.optJSONObject(a).optJSONArray("orderItem");

                String freight = msg.optJSONObject(a).optString("freight");
                String orderStatus = msg.optJSONObject(a).optString("orderStatus");
                String productCount = msg.optJSONObject(a).optString("productCount");
                String shippingStatus = msg.optJSONObject(a).optString("shippingStatus");
                String phone = msg.optJSONObject(a).optString("phone");
                String areaName = msg.optJSONObject(a).optString("areaName");
                String id1 = msg.optJSONObject(a).optString("id");
                String sn = msg.optJSONObject(a).optString("sn");
                String paymentStatus = msg.optJSONObject(a).optString("paymentStatus");
                String createDate = msg.optJSONObject(a).optString("createDate");

                Map<String, String> map1 = new HashMap<>();
                map1.put("type",0+"");
                map1.put("code",code);
                map1.put("desc",desc);
                map1.put("token",token);
                map1.put("freight",freight);
                map1.put("orderStatus",orderStatus);
                map1.put("productCount",productCount);
                map1.put("shippingStatus",shippingStatus);
                map1.put("phone",phone);
                map1.put("areaName",areaName);
                map1.put("id1",id1);
                map1.put("sn",sn);
                map1.put("paymentStatus",paymentStatus);
                map1.put("createDate",createDate);
                mapList.add(map1);

                for (int i = 0; i < orderItem.length(); i++) {// 遍历数组
                    Map map2 = new HashMap();
                    String thumbnail = orderItem.optJSONObject(i).optString("thumbnail");
                    String quantity = orderItem.optJSONObject(i).optString("quantity");
                    String price = orderItem.optJSONObject(i).optString("price");
                    String name = orderItem.optJSONObject(i).optString("name");
                    String id = orderItem.optJSONObject(i).optString("id");

                    map2.put("type",2+"");
                    map2.put("thumbnail",thumbnail);
                    map2.put("quantity",quantity);
                    map2.put("price",price);
                    map2.put("name",name);
                    map2.put("id",id);
                    mapList.add(map2);
                }
            }
            JSONObject page = json.optJSONObject("page");
            String total = page.optString("total");
            String pageNumber = page.optString("pageNumber");
            String pageSize = page.optString("pageSize");
            Map<String, String> map3 = new HashMap<>();

            map3.put("type",3+"");
            map3.put("total",total);
            map3.put("pageNumber",pageNumber);
            map3.put("pageSize",pageSize);
            mapList.add(map3);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}