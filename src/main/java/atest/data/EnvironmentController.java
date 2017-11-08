package atest.data;

import atest.data.services.Environment;
import atest.data.services.EnvironmentService;
import org.springframework.web.bind.annotation.*;

public class EnvironmentController {
    // create an environment
    @RequestMapping( value = "/environment/register/{id}", method = RequestMethod.PUT )
    public @ResponseBody
    String register(@PathVariable String id, @RequestBody String requestBody){
        try {
            String jsonString = EnvironmentService.getInstance().register(Environment.class.getName(),id).update(requestBody);
            return Message.OK().add(jsonString).toString();
        } catch (Exception e) {
            return Message.Exception(e).toString();
        }
    }
    // list environments
    @RequestMapping( value = "/environment/list", method = RequestMethod.GET )
    public @ResponseBody
    String list(){
        try {
            String jsonString = EnvironmentService.getInstance().list();
            return Message.OK().add(jsonString).toString();
        } catch (Exception e) {
            return Message.Exception(e).toString();
        }
    }
    // update an environment
    // delete an environment
}
