package atest.data.services;

public class Agent extends Instance {
    //agent has id, ip, name, status
    TestingInstance testingInstance;
    private Agent(String id) {
        super(id);
    }

    public boolean isAssignable(){
        if(testingInstance == null)
            return true;
        if(testingInstance.isStopped())
            return true;
        return false;
    }
    // register an agent
    public boolean assign(TestingInstance instance) {
        if ( isAssignable() ){
            testingInstance = instance;
            return true;
        }
        return false;
    }

    public boolean unAssign(){
        testingInstance = null;
        return true;
    }
    // request next action
    // submit an action result
}
