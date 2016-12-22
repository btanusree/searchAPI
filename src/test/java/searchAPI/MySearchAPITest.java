package searchAPI;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;

@RunWith(value = Parameterized.class)
public class MySearchAPITest {

	 private Map<String, Object> poInputMap = null;

	 public MySearchAPITest(Map<String, Object> map) {
	    this.poInputMap = map;
	 }


	 @Parameters
	 public static List<Map<String, Object>> data() {
	   List<Map<String, Object>> oList = new ArrayList<Map<String, Object>>();
	   Map<String, Object> oMap = new LinkedHashMap<String, Object>();
	   oMap.put("term", "jack+johnson");
	   oMap.put("limit", 1);
	   
	   oList.add(oMap);
	   
	   oMap = new LinkedHashMap<String, Object>();
	   oMap.put("term", "jack");
	   oMap.put("limit", 2);
	   
	   oList.add(oMap);
	   
	   return oList;
	 }
	 

	 @Test
	 public void test() {
	   System.out.println("Parameterized Number is : " + poInputMap.toString());
	   
	   StringBuilder sb = new StringBuilder("https://itunes.apple.com/search");
	   int i = 0;
	   for(Map.Entry<String, Object> oMap : poInputMap.entrySet()) {
		   if(i == 0) {
			   sb.append("?").append(oMap.getKey()).append("=").append(oMap.getValue());
		   }
		   else
			   sb.append("&").append(oMap.getKey()).append("=").append(oMap.getValue());
		   i++;
	   }
	   
	   ExtractableResponse<Response> response = RestAssured.given()
				.get(sb.toString())
				.then()
				.statusCode(200)
			//	.body("resultCount", equalTo(poInputMap.get("limit")))
				.extract();
		String resp = response.asString();
		System.out.println("Resp:" + resp);
		JsonPath jsonObj = new JsonPath(resp);
		
		Assert.assertEquals(poInputMap.get("limit"), jsonObj.get("resultCount"));
	 }
}
