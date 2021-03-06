package com.sky.service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sky.vo.WeatherResponse;
import com.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Weather Data Collection Service.
 * 
 * @since 1.0.0 2017年11月26日
 * @author <a href="https://waylau.com">Way Lau</a> 
 */
@Service
public class WeatherDataCollectionServiceImpl implements WeatherDataCollectionService {
	private static final String WEATHER_URI = "http://wthrcdn.etouch.cn/weather_mini?";
	private final static Logger logger = LoggerFactory.getLogger(WeatherDataCollectionServiceImpl.class);

	private static final long TIME_OUT = 1800L; // 1800s
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Override
	public void syncDateByCityId(String cityId) {
		String uri = WEATHER_URI + "citykey=" + cityId;
		this.saveWeatherData(uri);
	}

	@Override
	public WeatherResponse getDataByCityId(String cityId) {
		String uri = WEATHER_URI + "citykey=" + cityId;
		return this.doGetWeahter(uri);
	}

	@Override
	public WeatherResponse getDataByCityName(String cityName) {
		String uri = WEATHER_URI + "city=" + cityName;
		return this.doGetWeahter(uri);
	}

	/**
	 * 把天气数据放在缓存
	 * @param uri
	 */
	private void saveWeatherData(String uri) {
		String key = uri;
		String strBody = null;
		ValueOperations<String, String>  ops = stringRedisTemplate.opsForValue();
		// 调用服务接口来获取
 		ResponseEntity<String> respString = restTemplate.getForEntity(uri, String.class);

 		if (respString.getStatusCodeValue() == 200) {
			try {
				strBody = StringUtil.conventFromGzip(respString.getBody());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// 数据写入缓存
		ops.set(key, strBody, TIME_OUT, TimeUnit.SECONDS);

	}

	private WeatherResponse doGetWeahter(String uri) {
		String key = uri;
		String strBody = null;
		ObjectMapper mapper = new ObjectMapper();
		WeatherResponse resp = null;
		ValueOperations<String, String>  ops = stringRedisTemplate.opsForValue();
		// 先查缓存，缓存有的取缓存中的数据
		if (stringRedisTemplate.hasKey(key)) {
			logger.info("Redis has data");
			strBody = ops.get(key);
		} else {
			logger.info("Redis don't has data");
			// 缓存没有，抛出异常
			throw new RuntimeException("Don't has data!");
		}
		try {
			resp = mapper.readValue(strBody, WeatherResponse.class);
		} catch (IOException e) {
			//e.printStackTrace();
			logger.error("Error!",e);
		}

		return resp;
	}
}
