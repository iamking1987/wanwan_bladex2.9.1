import com.alibaba.fastjson.JSON;
import com.fun.tool.utils.MacUtil;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author wanwan 2022/2/15
 */
public class MacUtilTest {

	@Test
	public void test2() {
		Map<Object, Object> body = new TreeMap<>();
		Map<Object, Object> header = new TreeMap<>();

		header.put("inresponseto", "10bc606fa06c87c0e83705f321f0075d");
		header.put("resultcode","104103");
		header.put("systemtime","20220211115242557");
		header.put("version","1.0");
		header.put("resultdesc","验证签名失败");

		// 组装报文
		Map<Object, Object> requestData = new TreeMap<>();
		requestData.put("header",header);
		requestData.put("body",body);
		//这里使用的JSON工具类要注意一下，JSONUtil.toJsonStr将TreeMap转string时候，顶层的顺序会变
		String mac = MacUtil.hmacsha256("ijjgL64Z7VpwHk24", JSON.toJSONString(requestData));
		System.out.println(mac);
		header.put("mac",mac);
	}
}
