package com.easy.java.starter.seata.autoconfire;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangliang181230
 */
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String s, Response response) {
		String errorContent;
		try {
			errorContent = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new MyFeignException("999","获取body失败", e);
		}
		Map<String, Object> result = JSON.parseObject(errorContent, Map.class);
//		MyFeignException myFeignException = new MyFeignException(String.valueOf(result.get("status")), String.valueOf(result.get("message")));
//		return myFeignException;
		throw new MyFeignException(String.valueOf(result.get("status")), String.valueOf(result.get("message")));
	}

}
