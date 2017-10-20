package atest.data.services;

public class TestingInstanceService extends Service {
    private static TestingInstanceService ourInstance = new TestingInstanceService();

    public static TestingInstanceService getInstance() {
        return ourInstance;
    }

    private TestingInstanceService() {
    }
}
