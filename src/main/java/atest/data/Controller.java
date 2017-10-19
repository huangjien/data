package atest.data;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
class Controller {

    @RequestMapping( value = "/ping", method = RequestMethod.GET )
    public @ResponseBody String ping(){
        return Message.OK().toString();
    }

    @RequestMapping(value="/data/id/{id}", method = RequestMethod.GET)
    public @ResponseBody String id(@PathVariable String id){
        try {
            String jsonString = EDBService.getInstance().findByID(id);
            JSONArray jsonArray = JsonPath.read(jsonString,"$.hits.hits");
            return Message.OK().add(jsonArray.toJSONString()).toString();
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }

    @RequestMapping(value = "/data/parentid/{parentid}", method = RequestMethod.GET)
    public @ResponseBody String parentid (@PathVariable String parentid){
        try {
            String jsonString = EDBService.getInstance().findByParentID(parentid);
            JSONArray jsonArray = JsonPath.read(jsonString,"$.hits.hits");
            return Message.OK().add(jsonArray.toJSONString()).toString();
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }

    @RequestMapping(value = "/data/metaData/{dataName}", method = RequestMethod.GET)
    public @ResponseBody String metaData (@PathVariable String dataName){
        try {
            String jsonString = EDBService.getInstance().findMetaData(dataName);

            JSONArray jsonArray = JsonPath.read(jsonString,"$.hits.hits");
            return Message.OK().add(jsonArray.toJSONString()).toString();
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }

    @RequestMapping(value = "/data/query/{queryString}", method = RequestMethod.GET)
    public @ResponseBody String query (@PathVariable String queryString){
        try {
            String jsonString = EDBService.getInstance().find(queryString.replace(" ", "%20").replace("+", "%2B"));
            JSONArray jsonArray = JsonPath.read(jsonString,"$.hits.hits");
            return Message.OK().add(jsonArray.toJSONString()).toString();
        } catch (IOException e) {
            return Message.Exception(e).toString();
        }
    }


}
