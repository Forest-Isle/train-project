import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.senvu.MemberApplication;
import com.senvu.train.member.pojo.entity.Passenger;
import com.senvu.train.member.mapper.PassengerMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = MemberApplication.class)
public class TestClass {

    @Autowired
    private PassengerMapper passengerMapper;

    @Test
    public void test01() {
        LambdaQueryWrapper<Passenger> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Passenger::getMemberId,Long.valueOf("1866289322228060160"));
        List<Passenger> passengers = passengerMapper.selectList(wrapper);
        System.out.println(passengers);
    }

    @Test
    public void test02() {
        int current = 1;
        int size = 1;
        LambdaQueryWrapper<Passenger> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Passenger::getMemberId,Long.valueOf("1866289322228060160"));
        Page<Passenger> page = new Page<>(current,size);
        passengerMapper.selectPage(page,wrapper);
        long total = page.getTotal();
        System.out.println(total);
        List<Passenger> records = page.getRecords();
        records.forEach(System.out::println);
    }


}
