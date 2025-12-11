package uz.billsplitter.demo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uz.billsplitter.demo.dto.request.OrderRequestDto;
import uz.billsplitter.demo.dto.request.PersonOrderRequestDto;
import uz.billsplitter.demo.dto.response.OrderResponseDto;
import uz.billsplitter.demo.repository.MenuRepository;
import uz.billsplitter.demo.service.impl.BillServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoApplicationTests {
    private final HashMap<String, Double> menu = new HashMap<>();
    @Mock
    private MenuRepository menuRepository;
    @InjectMocks
    private BillServiceImpl billServiceImpl;
    private PersonOrderRequestDto personOrderRequestDto;
    private OrderRequestDto orderRequestDto;

    private void createMockedDb() {
        menu.put("pasta", 100.00);
        menu.put("burger", 200.00);
        menu.put("steak", 300.00);
        menu.put("pizza", 400.00);
    }


    // Right input values
    @Test
    public void testRightInputValues() {

        createMockedDb();

        ArrayList<PersonOrderRequestDto> listOfPersonOrderRequestDto = new ArrayList<>(List.of(
            new PersonOrderRequestDto(
                "Alex",
                new ArrayList<>(Arrays.asList("pizza", "steak" ))
            ),
            new PersonOrderRequestDto(
                "Kevin",
                new ArrayList<>(List.of("burger" ))
            ),
            new PersonOrderRequestDto(
                "Mark",
                new ArrayList<>(List.of("pasta" ))
            )
        ));

        OrderRequestDto request = new OrderRequestDto(
            listOfPersonOrderRequestDto,
            new ArrayList<>(List.of("pizza", "steak" ))
        );

        when(menuRepository.getMenu()).thenReturn(menu);

        OrderResponseDto orderResponseDto = billServiceImpl.split(request);

        assertEquals(3, listOfPersonOrderRequestDto.size());

        assertEquals(700, orderResponseDto.totalCommonMealsAmount());

        var Alex = orderResponseDto.listOfPeople().getFirst();

        assertEquals("Alex", Alex.name());
        assertEquals(700, Alex.personMealPrice());
        assertEquals(140, Alex.personMealPriceCommission());
        assertEquals(233.33, Alex.commonMealsPricePerPerson());
        assertEquals(46.67, Alex.commonMealPriceCommission());
        assertEquals(186.67, Alex.totalCommissions());
        assertEquals(1120.0, Alex.total());
    }

    // Incorrect name of the person order
    @Test
    public void testIncorrectNameOfPersonOrder() {
        createMockedDb();
        ArrayList<PersonOrderRequestDto> listOfPersonOrderRequestDto = new ArrayList<>(List.of(
            new PersonOrderRequestDto(
                "Alex",
                new ArrayList<>(Arrays.asList("pizzas", "steak" )) // There is an extra "s" in the word "pizzas"
            )
        ));

        when(menuRepository.getMenu()).thenReturn(menu);

        OrderRequestDto request = new OrderRequestDto(
            listOfPersonOrderRequestDto,
            new ArrayList<>(List.of("pizza,steak" ))
        );

        assertThrows(IllegalArgumentException.class, () -> billServiceImpl.split(request));
    }

    // Incorrect name of the common name
    @Test
    public void testIncorrectNameOfCommonMealOrder() {
        createMockedDb();
        ArrayList<PersonOrderRequestDto> listOfPersonOrderRequestDto = new ArrayList<>(List.of(
            new PersonOrderRequestDto(
                "Alex",
                new ArrayList<>(Arrays.asList("pizza", "steak" ))
            )
        ));

        when(menuRepository.getMenu()).thenReturn(menu);

        OrderRequestDto request = new OrderRequestDto(
            listOfPersonOrderRequestDto,
            new ArrayList<>(List.of("pizzas,steak" )) // There is an extra "s" in the word "pizzas"
        );

        assertThrows(IllegalArgumentException.class, () -> billServiceImpl.split(request));
    }


}
