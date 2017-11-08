package atest.data.services;

public class EnvironmentService extends Service {
    private static EnvironmentService ourInstance = new EnvironmentService();

    public static EnvironmentService getInstance() {
        return ourInstance;
    }

    private EnvironmentService() {
    }

}
