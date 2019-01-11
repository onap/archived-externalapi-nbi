package karatetest;

import com.intuit.karate.junit4.Karate;
import cucumber.api.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


@RunWith(Karate.class)
@CucumberOptions(features =  "classpath:karatetest/aainotresponding")
public class KarateRunnerTestsAAINotResponding{

    @BeforeClass
    public static void setUp() throws Exception {
        KarateTestConf.setUp("/aai/v11/business/customers/customer/new");
    }

    @AfterClass
    public static void tearsDown() throws Exception {
        KarateTestConf.tearsDown();
    }
}
