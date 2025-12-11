package uz.billsplitter.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import uz.billsplitter.demo.repository.MenuRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class DemoApplicationTests {

    @Mock
    private MenuRepository menuRepository;

    // Right input values
	@Test
    public void testRightInputValues () {

    }

}
