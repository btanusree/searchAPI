package searchAPI;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.ExtractableResponse;
import com.jayway.restassured.response.Response;

@RunWith(value = Parameterized.class)
public class MySearchAPIReadInputFromFileTest {

	 private Map<String, Object> poInputMap = null;

	 public MySearchAPIReadInputFromFileTest(Map<String, Object> map) {
	    this.poInputMap = map;
	 }


	 @Parameters
	 public static List<Object> data() throws Exception {
	   ObjectMapper oObjectMapper = new ObjectMapper();     
	   Object[] oaData = oObjectMapper.readValue(MySearchAPIReadInputFromFileTest.class.getClassLoader().getResource("input.properties"), Object[].class);
	   
	   return Arrays.asList(oaData);
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
