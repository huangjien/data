package atest.data;

import com.google.gson.*;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.common.Strings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.UUID;

import static org.apache.http.entity.ContentType.APPLICATION_JSON;

public class EDBService {

    public static final int REDIRECTION_BOUNDRY = 300;
    public static final int SUCCESS_BOUNDRY = 200;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser jsonParser = new JsonParser();
    private static EDBService _instance;
    private final RestClient restClient;

    private EDBService() {
        // set up connection
        Header[] headers = {new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json"),
                new BasicHeader("Role", "Read")};
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "changeme"));
        restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).setDefaultHeaders(headers)
                .setHttpClientConfigCallback(arg0 -> arg0.setDefaultCredentialsProvider(credentialsProvider))
                .build();

    }


    public static EDBService getInstance() {
        if (_instance == null)
            _instance = new EDBService();
        return _instance;
    }

    private void deleteAll() throws IOException {
        Response response = restClient.performRequest("POST", "/atest/_delete_by_query", Collections.emptyMap(),
                new NStringEntity("{\n" +
                        "  \"query\": {\n" +
                        "    \"match_all\": {}\n" +
                        "  }\n" +
                        "}", APPLICATION_JSON));
        int code = response.getStatusLine().getStatusCode();
        if (code> REDIRECTION_BOUNDRY || code < SUCCESS_BOUNDRY)
            throw new IOException("Response Code:"+code+"\n"+response.getStatusLine().getReasonPhrase());
    }

    public void insertOrUpdate(String id, String jsonContent) throws IOException {
        Response response = restClient.performRequest("PUT", "/atest/data/" + id, Collections.emptyMap(),
                new NStringEntity(jsonContent, APPLICATION_JSON));
        int code = response.getStatusLine().getStatusCode();
        if (code> REDIRECTION_BOUNDRY || code < SUCCESS_BOUNDRY)
            throw new IOException("Response Code:"+code+"\n"+response.getStatusLine().getReasonPhrase());

    }

    public void delete(String id) throws IOException {
        Response response = restClient.performRequest("DELETE", "/atest/data/" + id, Collections.emptyMap());
        int code = response.getStatusLine().getStatusCode();
        if (code> REDIRECTION_BOUNDRY || code < SUCCESS_BOUNDRY)
            throw new IOException("Response Code:"+code+"\n"+response.getStatusLine().getReasonPhrase());
    }

    public String findByID(String id) throws IOException {
        return find("id:" + id);
    }

    public String findByParentID(String id) throws IOException {
        return find("parentid:" + id);
    }

    public String find(String search_string) throws IOException {
        Response response = restClient.performRequest("GET", "/atest/data/_search?q="
                + search_string.replace(" ", "%20").replace("+", "%2B"),
                Collections.emptyMap());
        int code = response.getStatusLine().getStatusCode();
        if (code> REDIRECTION_BOUNDRY || code < SUCCESS_BOUNDRY)
            throw new IOException("Response Code:"+code+"\n"+response.getStatusLine().getReasonPhrase());
        return EntityUtils.toString(response.getEntity());
    }

    public static void main(String[] args) {
        try {
            EDBService.getInstance().initData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void initData() throws IOException {
        EDBService.getInstance().deleteAll();
        // init root
        String rootString = "{\n" +
                "    \"name\": \"Root\",\n" +
                "    \"type\": \"Meta\",\n" +
                "    \"id\": \"0000000000000\",\n" +
                "    \"leaf\": false,\n" +
                "    \"children\": []\n" +
                "  }";
        String rootID = EDBService.getInstance().importOneItemFromJsonString(rootString, "");
        // init form
        String formString = "{\n" +
                "    \"name\": \"Forms\",\n" +
                "    \"type\": \"Meta\"\n" +
                "}";
        String formID = EDBService.getInstance().importOneItemFromJsonString(formString, "");
        importArrayItemsFromJsonString(new String(Files.readAllBytes(Paths.get("forms"))), formID);
        // init menu
        String menuString = "{\n" +
                "    \"name\": \"Menu\",\n" +
                "    \"type\": \"Meta\"\n" +
                "}";
        String menuID = EDBService.getInstance().importOneItemFromJsonString(menuString, "");
        importArrayItemsFromJsonString(new String(Files.readAllBytes(Paths.get("menu"))), menuID);
        // init sample
        importArrayItemsFromJsonString(new String(Files.readAllBytes(Paths.get("testingData"))), rootID);
    }
    
    private void importArrayItemsFromJsonString(String arrayString, String parentid) throws IOException {
        JsonArray jsonArray = jsonParser.parse(arrayString).getAsJsonArray();
        for (int i = 0; i < jsonArray.size(); i++) {
            importOneItemFromJsonString(jsonArray.get(i).toString(), parentid);
        }
    }

    private String importOneItemFromJsonString(String rootString, String parentid) throws IOException {
        JsonObject root = jsonParser.parse(rootString).getAsJsonObject();
        String id;
        if (!root.has("id")){
            id = UUID.randomUUID().toString();
            root.addProperty("id", id);
        }
        id = root.get("id").getAsString();
        if (Strings.isNullOrEmpty(id)){
            id = UUID.randomUUID().toString();
            root.remove("id");
            root.addProperty("id", id);
        }
        if(!root.has("parentid")) {
            root.addProperty("parentid", parentid);
        }



        EDBService.getInstance().insertOrUpdate(id, root.toString());
        return id;
    }

    public String findMetaData(String dataName) throws IOException {
        return find("type:Meta AND name:"+dataName);
    }



    //insert or update
    //delete
    //find
}
