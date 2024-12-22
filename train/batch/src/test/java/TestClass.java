import com.senvu.client.BusinessFeignClient;
import com.senvu.pojo.result.Result;
import com.senvu.train.batch.BatchApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest(classes = BatchApplication.class)
public class TestClass {

    @Autowired
    private BusinessFeignClient businessFeignClient;

    @Test
    public void test01() {
//        DailyTrain train = businessFeignClient.getById(1867539484959268866L);
//        System.out.println("train:" + train);
        Result result = businessFeignClient.genTrain(LocalDate.now());
        System.out.println(result.getCode());
    }


}
