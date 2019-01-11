package karatetest;

import com.intuit.karate.junit4.Karate;
import cucumber.api.CucumberOptions;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;


@RunWith(Karate.class)
@CucumberOptions(features =  "classpath:karatetest/sdcnotresponding")
public class KarateRunnerTestWithSDCNotResponding{


    @BeforeClass
    public static void setUp() throws Exception {
        KarateTestConf.setUp("/sdc/v1/catalog/services/1e3feeb0-8e36-46c6-862c-236d9c626439/metadata");
    }

    @AfterClass
    public static void tearsDown() throws Exception {
        KarateTestConf.tearsDown();

    }

}
