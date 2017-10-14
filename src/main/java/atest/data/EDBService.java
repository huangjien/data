package atest.data;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.io.IOException;
import java.util.Collections;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class EDBService {

    private static EDBService _instance;
    RestClient restClient;

    private EDBService() {
        // set up connection
        Header[] headers = {new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                new BasicHeader("Role", "Read")};
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "changeme"));
        restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).setDefaultHeaders(headers)
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                    public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder arg0) {

                        return arg0.setDefaultCredentialsProvider(credentialsProvider);
                    }
                })
                .build();

    }

    public static EDBService getInstance() {
        if (_instance == null)
            _instance = new EDBService();
        return _instance;
    }

    public void insertOrUpdate(String id, String jsonContent) throws IOException {
        Response response = restClient.performRequest("PUT", "/atest/data/" + id, Collections.<String, String>emptyMap(),
                new NStringEntity(jsonContent, APPLICATION_JSON));
        int code = response.getStatusLine().getStatusCode();
        if (code>300 || code <200)
            throw new IOException("Response Code:"+code+"\n"+response.getStatusLine().getReasonPhrase());

    }

    public void delete(String id) throws IOException {
        Response response = restClient.performRequest("DELETE", "/atest/data/" + id, Collections.<String, String>emptyMap(),
                null);
        int code = response.getStatusLine().getStatusCode();
        if (code>300 || code <200)
            throw new IOException("Response Code:"+code+"\n"+response.getStatusLine().getReasonPhrase());
    }

    public String findByID(String id) throws IOException {
        return find("_id:" + id);
    }

    public String findByParentID(String id) throws IOException {
        return find("parent_id:" + id);
    }

    private String find(String search_string) throws IOException {
        Response response = restClient.performRequest("GET", "/atest/data/_search?q=" + search_string, Collections.<String, String>emptyMap());
        int code = response.getStatusLine().getStatusCode();
        if (code>300 || code <200)
            throw new IOException("Response Code:"+code+"\n"+response.getStatusLine().getReasonPhrase());
        return EntityUtils.toString(response.getEntity());
    }

    public static void main(String[] args) {
        try {
            EDBService.getInstance().insertOrUpdate("1", "{\"hello\":\"world\"}");
            System.out.println(EDBService.getInstance().find("name:Sirui"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //insert or update
    //delete
    //find
}
