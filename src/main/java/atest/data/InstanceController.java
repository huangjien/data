package atest.data;

import atest.data.services.TestingInstance;
import atest.data.services.TestingInstanceService;
import org.springframework.web.bind.annotation.*;

public class InstanceController {


    // create an instance
    @RequestMapping( value = "/instance/register/{id}", method = RequestMethod.PUT )
    public @ResponseBody
    String register(@PathVariable String id, @RequestBody String requestBody){
        try {
            String jsonString = TestingInstanceService.getInstance().register(TestingInstance.class.getName(),id).update(requestBody);
            return Message.OK().add(jsonString).toString();
        } catch (Exception e) {
            return Message.Exception(e).toString();
        }
    }
    // add agent (to kick off a test, need at least one agent)
    // add environment (to kick off a test, need at least one environment)
    // kick off an instance
    // pause a running instance
    // resume a paused instance
    // stop an instance (running or paused)
    // delete an instance (cannot delete running, paused instance)
    // get logs of an instance

}
