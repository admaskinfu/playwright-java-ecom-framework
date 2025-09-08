package com.ecom.automation.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

/**
 * SimpleOAuthClient - Simple OAuth 1.0a implementation for WooCommerce API
 * 
 * This class implements OAuth 1.0a authentication exactly as shown in the Postman request.
 * It generates the proper HMAC-SHA1 signature and OAuth parameters.
 */
public class SimpleOAuthClient {

    private static final Logger logger = LoggerFactory.getLogger(SimpleOAuthClient.class);
    
    private final String consumerKey;
    private final String consumerSecret;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SimpleOAuthClient(String consumerKey, String consumerSecret, String baseUrl) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Make a GET request with OAuth 1.0a authentication
     * 
     * @param endpoint The API endpoint (e.g., "/customers")
     * @return SimpleApiResponse with status code and body
     */
    public SimpleApiResponse get(String endpoint) {
        try {
            String url = baseUrl + endpoint;
            
            // Generate OAuth parameters
            Map<String, String> oauthParams = generateOAuthParams("GET", url);
            
            // Build the final URL with OAuth parameters
            StringBuilder urlBuilder = new StringBuilder(url).append("?");
            for (Map.Entry<String, String> entry : oauthParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String finalUrl = urlBuilder.toString();
            
            logger.debug("Making OAuth GET request to: {}", finalUrl);
            
            // Create HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .header("Accept", "application/json")
                .GET()
                .build();
            
            // Execute request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            logger.debug("OAuth GET response - Status: {}, Body: {}", response.statusCode(), response.body());
            
            return new SimpleApiResponse(response.statusCode(), response.body());
            
        } catch (Exception e) {
            logger.error("Failed to execute OAuth GET request: {}", e.getMessage());
            throw new RuntimeException("OAuth GET request failed", e);
        }
    }

    /**
     * Make a POST request with OAuth 1.0a authentication
     * 
     * @param endpoint The API endpoint (e.g., "/customers")
     * @param requestBody The request body object
     * @return SimpleApiResponse with status code and body
     */
    public SimpleApiResponse post(String endpoint, Object requestBody) {
        try {
            String url = baseUrl + endpoint;
            String jsonBody = requestBody != null ? objectMapper.writeValueAsString(requestBody) : "";
            
            // Generate OAuth parameters
            Map<String, String> oauthParams = generateOAuthParams("POST", url);
            
            // Build the final URL with OAuth parameters
            StringBuilder urlBuilder = new StringBuilder(url).append("?");
            for (Map.Entry<String, String> entry : oauthParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String finalUrl = urlBuilder.toString();
            
            logger.debug("Making OAuth POST request to: {}", finalUrl);
            
            // Create HTTP request
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(finalUrl))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json");
            
            if (!jsonBody.isEmpty()) {
                requestBuilder.POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8));
            } else {
                requestBuilder.POST(HttpRequest.BodyPublishers.noBody());
            }
            
            HttpRequest request = requestBuilder.build();
            
            // Execute request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            
            logger.debug("OAuth POST response: {} - {}", response.statusCode(), response.body());
            
            return new SimpleApiResponse(response.statusCode(), response.body());
            
        } catch (Exception e) {
            logger.error("Failed to execute OAuth POST request: {}", e.getMessage());
            throw new RuntimeException("OAuth POST request failed", e);
        }
    }

    /**
     * Generate OAuth 1.0a parameters including HMAC-SHA1 signature
     */
    private Map<String, String> generateOAuthParams(String method, String url) {
        Map<String, String> params = new TreeMap<>();
        
        // OAuth parameters
        params.put("oauth_consumer_key", consumerKey);
        params.put("oauth_signature_method", "HMAC-SHA1");
        params.put("oauth_timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        params.put("oauth_nonce", generateNonce());
        params.put("oauth_version", "1.0");
        
        // Create signature base string
        StringBuilder signatureBase = new StringBuilder();
        signatureBase.append(method).append("&");
        signatureBase.append(urlEncode(url)).append("&");
        
        // Add OAuth parameters to signature base
        StringBuilder paramString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (paramString.length() > 0) paramString.append("&");
            paramString.append(urlEncode(entry.getKey())).append("=").append(urlEncode(entry.getValue()));
        }
        signatureBase.append(urlEncode(paramString.toString()));
        
        // Generate signature
        String signature = generateSignature(signatureBase.toString(), consumerSecret + "&");
        params.put("oauth_signature", signature);
        
        return params;
    }

    /**
     * Generate HMAC-SHA1 signature
     */
    private String generateSignature(String data, String key) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
            javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(key.getBytes(), "HmacSHA1");
            mac.init(secretKeySpec);
            byte[] signature = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(signature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate OAuth signature", e);
        }
    }

    /**
     * Generate a random nonce
     */
    private String generateNonce() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes).replaceAll("[^a-zA-Z0-9]", "");
    }

    /**
     * URL encode a string
     */
    private String urlEncode(String value) {
        try {
            return java.net.URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            return value;
        }
    }

    /**
     * Simple response wrapper
     */
    public static class SimpleApiResponse {
        private final int statusCode;
        private final String body;

        public SimpleApiResponse(int statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public String getBody() {
            return body;
        }
    }
}
