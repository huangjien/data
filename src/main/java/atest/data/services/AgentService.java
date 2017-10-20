package atest.data.services;

public class AgentService extends Service {
    private static AgentService ourInstance = new AgentService();

    public static AgentService getInstance() {
        return ourInstance;
    }

    private AgentService() {
    }
}
