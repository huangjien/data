package atest.data;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class Controller {

    @RequestMapping( value = "/ping", method = RequestMethod.GET )
    public @ResponseBody String ping(){
        return Message.OK().toString();
    }
    @RequestMapping(value="/data/id/{id}", method = RequestMethod.GET)
    public @ResponseBody String id(@PathVariable String id){
        try {
            String jsonString = EDBService.getInstance().findByID(id);
            return Message.OK().add(jsonString).toString();
        } catch (IOException e) {
            e.printStackTrace();
            Message.Fail().add("exception", e.getMessage()).add("stack", Message.stackTrace(e));
        }
        return Message.Fail().toString();
    }

    @RequestMapping(value = "/data/parentid/{parentid}", method = RequestMethod.GET)
    public @ResponseBody String parentid (@PathVariable String parentid){
        try {
            String jsonString = EDBService.getInstance().findByParentID(parentid);
            return Message.OK().add(jsonString).toString();
        } catch (IOException e) {
            e.printStackTrace();
            Message.Fail().add("exception", e.getMessage()).add("stack", Message.stackTrace(e));
        }
        return Message.Fail().toString();
    }

    @RequestMapping(value = "/data/metaData/{dataName}", method = RequestMethod.GET)
    public @ResponseBody String metaData (@PathVariable String dataName){
        try {
            String jsonString = EDBService.getInstance().findMetaData(dataName);
            return Message.OK().add(jsonString).toString();
        } catch (IOException e) {
            e.printStackTrace();
            Message.Fail().add("exception", e.getMessage()).add("stack", Message.stackTrace(e));
        }
        return Message.Fail().toString();
    }


}
