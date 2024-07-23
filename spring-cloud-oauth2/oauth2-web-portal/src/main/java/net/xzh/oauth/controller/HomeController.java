package net.xzh.oauth.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

/**
 * 单点认证
 * @author CR7
 *
 */
@Slf4j
@RestController
public class HomeController {
    @Value("${zlt.sso.client-id:}")
    private String clientId;

    @Value("${zlt.sso.client-secret:}")
    private String clientSecret;

    @Value("${zlt.sso.redirect-uri:}")
    private String redirectUri;

    @Value("${zlt.sso.access-token-uri:}")
    private String accessTokenUri;

    @Value("${zlt.sso.token-info-uri:}")
    private String tokenInfoUri;

    private final static Map<String, Map<String, Object>> localTokenMap = new HashMap<>();

    @GetMapping("/token/{code}")
    public String tokenInfo(@PathVariable String code) throws UnsupportedEncodingException {
        //获取token
        Map tokenMap = getAccessToken(code);
        String accessToken = (String)tokenMap.get("access_token");
        //获取用户信息
        Map userMap = getUserInfo(accessToken);
        //获取用户角色
        List<Map<String, String>> roles = (List<Map<String, String>>)userMap.get("authorities");
        Map result = new HashMap(2);
        String username = (String)userMap.get("user_name");
        result.put("username", username);
        result.put("roles", roles);
        localTokenMap.put(accessToken, result);

        return accessToken;
    }

    /**
     * 获取token
     */
    public Map getAccessToken(String code) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        byte[] authorization = (clientId + ":" + clientSecret).getBytes("UTF-8");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Auth = encoder.encode(authorization);
        headers.add("Authorization", "Basic " + base64Auth);

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("code", code);
        param.add("grant_type", "authorization_code");
        param.add("redirect_uri", redirectUri);
        param.add("scope", "app");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(accessTokenUri, request , Map.class);
        Map result = response.getBody();
        return result;
    }

    /**
     * 获取用户信息
     * @throws UnsupportedEncodingException 
     */
    public Map getUserInfo(String accessToken) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        byte[] authorization = (clientId + ":" + clientSecret).getBytes("UTF-8");
        BASE64Encoder encoder = new BASE64Encoder();
        String base64Auth = encoder.encode(authorization);
        headers.add("Authorization", "Basic " + base64Auth);

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("token", accessToken);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(param, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenInfoUri, request , Map.class);
        Map result = response.getBody();
        return result;
    }

    @GetMapping("/user")
    public Map<String, Object> user(HttpServletRequest request) {
        String token = request.getParameter("access_token");
        return localTokenMap.get(token);
    }

    @GetMapping("/logoutNotify")
    public void logoutNotify(HttpServletRequest request) {
        String tokens = request.getParameter("tokens");
        log.info("=====logoutNotify: " + tokens);
        if (StrUtil.isNotEmpty(tokens)) {
            for (String accessToken : tokens.split(",")) {
                localTokenMap.remove(accessToken);
            }
        }
    }
}
