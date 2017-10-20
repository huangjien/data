package atest.data.services;

public class TestingInstance extends Instance {
    private String status = "Idle"; // running, paused, stopped (finished also is stopped)

    public TestingInstance(String id) {
        super(id);
    }

    public boolean isStopped(){
        if (status.equals("stopped")) {
            return true;
        }
        return false;
    }
}
