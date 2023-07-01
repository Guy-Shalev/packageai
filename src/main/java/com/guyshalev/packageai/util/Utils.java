package com.guyshalev.packageai.util;

import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

public class Utils {
    private static final Logger LOG = LoggerFactory.getLogger(Utils.class);
    public static final String BAD_URL_CALL = "Bad call for: ";


    /**
     * Calling an external API to get geolocation information for an address
     *
     * @param params - the parameters (key,value) for an API call
     * @param url    - the url address of the API
     * @return - the response from the API call
     */
    public static String callRestApi(String url, Map<String, String> params) {
        String apiCallResponse = null;
        HttpGet httpGet = createHttpGetWithParams(url, params);

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            ClassicHttpResponse response = (ClassicHttpResponse) httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                // Read the response content
                apiCallResponse = EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            LOG.error(BAD_URL_CALL + httpGet.getPath() + " Because of: "
                    + e.getMessage() + ", " + Arrays.toString(e.getStackTrace()));
        }

        return apiCallResponse;
    }

    private static HttpGet createHttpGetWithParams(String url, Map<String, String> params) {
        HttpGet httpGet = new HttpGet(url);
        URI uri;
        try {
            URIBuilder uriBuilder = new URIBuilder(httpGet.getUri());
            params.forEach(uriBuilder::addParameter);
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        httpGet.setUri(uri);

        return httpGet;
    }
}
