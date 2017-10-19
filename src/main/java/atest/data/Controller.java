package atest.data;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.elasticsearch.common.Strings;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
class Controller {

    @RequestMapping( value = "/ping", method = RequestMethod.GET )
    public @ResponseBody String ping(){
        Message message = Message.OK();
        Map<String, String> env = System.getenv();
        for (String key : env.keySet()) {
            message.add(key, env.get(key));
        }
        return message.toString();
    }

    @RequestMapping(value="/data/id/{id}", method = RequestMethod.GET)
    public @ResponseBody String id(@PathVariable String id){
        try {
            String jsonString = EDBService.getInstance().findByID(id);
            return queryResult(jsonString);
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }

    @RequestMapping(value = "/data/parentid/{parentid}", method = RequestMethod.GET)
    public @ResponseBody String parentid (@PathVariable String parentid){
        try {
            String jsonString = EDBService.getInstance().findByParentID(parentid);
            return queryResult(jsonString);
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }

    @RequestMapping(value = "/data/metaData/{dataName}", method = RequestMethod.GET)
    public @ResponseBody String metaData (@PathVariable String dataName){
        try {
            String jsonString = EDBService.getInstance().findMetaData(dataName);
            String parentID = JsonPath.read(jsonString, "$.hits.hits[0]._source.id");
            if (Strings.isNullOrEmpty(parentID)){
                return Message.Fail().add("MetaDataNotFound", dataName).toString();
            }
            String kidString = EDBService.getInstance().findByParentID(parentID);
            return queryResult(kidString);
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }

    private String queryResult(String kidString) {
        JSONArray jsonArray = JsonPath.read(kidString,"$.hits.hits");
        return Message.OK().add(jsonArray.toJSONString()).toString();
    }

    @RequestMapping(value = "/data/query/{queryString}", method = RequestMethod.GET)
    public @ResponseBody String query (@PathVariable String queryString){
        try {
            String jsonString = EDBService.getInstance().find(queryString);
            return queryResult(jsonString);
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }


}
