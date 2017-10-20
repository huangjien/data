package atest.data;

import atest.data.services.Agent;
import atest.data.services.AgentService;
import org.springframework.web.bind.annotation.*;

public class AgentController {
    // register an agent
    @RequestMapping( value = "/agent/register/{id}", method = RequestMethod.PUT )
    public @ResponseBody
    String register(@PathVariable String id, @RequestBody String requestBody){
        try {
            String jsonString = AgentService.getInstance().register(Agent.class.getName(),id).update(requestBody);
            return Message.OK().add(jsonString).toString();
        } catch (Exception e) {
            return Message.Exception(e).toString();
        }
    }
    // request next action
    // submit an action result
}
