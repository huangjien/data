package atest.data;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataApplicationTests {

	@BeforeClass
    public static void setUp() throws IOException {
//	    EDBService.getInstance().deleteAll();
	    EDBService.getInstance().initData();
    }

	@Test
	public void contextLoads() {
	}

}
