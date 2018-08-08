package shop.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import shop.service.IpService;

@Service
public class IpServiceImpl implements IpService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private RestTemplate restTemplate;
    
    @Autowired
    public IpServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    // 这个方法执行之前会先看缓存中有没有key（即参数ip）对应的value，
    // 没有就会执行该方法，将返回值放入缓存并返回该值；若缓存已存在，则该方法不会执行，直接返回缓存值
    @Cacheable(cacheNames = "ip-to-province", sync = true) 
    @Override
    public String ipToProvince(String ip) {
        Map<String, String> params = new HashMap<>();
        params.put("key", "991ddb41d0ba901fe35396ec2f4a0113");
        params.put("ip", ip);
        
        JsonNode json = restTemplate.getForObject(
                "https://restapi.amap.com/v3/ip?key={key}&ip={ip}", 
                JsonNode.class, // 将响应转换成json树模型
                params);
        
        logger.debug("调用高德ip转地址: " + json.at("/info"));
        
        return json.at("/province").asText();
    }

}
