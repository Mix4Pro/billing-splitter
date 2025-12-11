package uz.billsplitter.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.billsplitter.demo.dto.request.OrderRequestDto;
import uz.billsplitter.demo.dto.request.PersonOrderRequestDto;
import uz.billsplitter.demo.dto.response.OrderResponseDto;
import uz.billsplitter.demo.dto.response.PersonOrderResponseDto;
import uz.billsplitter.demo.repository.MenuRepository;
import uz.billsplitter.demo.service.BillService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService{

    @Autowired
    private MenuRepository menuRepository;

    private double calculateAmountPerPerson (PersonOrderRequestDto person , MenuRepository menu) {
        double totalAmount = 0;
        for(String personMeal : person.meals()) {

            Double price = menu.getMenu().get(personMeal);

            if(price == null) {
                throw new IllegalArgumentException("There is no meal like this ( " + personMeal + " ) in the menu");
            }

            totalAmount += price;
        }

        return totalAmount;
    }

    private double calculateAmountOfCommonMealsOrdered (ArrayList<String> meals , MenuRepository menu) {
        double totalAmount = 0;
        for(String mealOrder : meals) {

            Double price = menu.getMenu().get(mealOrder);

            if(price == null) {
                throw new IllegalArgumentException("There is no meal like this ( " + mealOrder + " ) in the menu");
            }
            totalAmount += price;
        }

        return totalAmount;
    }

    private double roundTheAmount (double amount ) {
        return new BigDecimal(amount).setScale(2,RoundingMode.HALF_UP).doubleValue();
    }

    @Override
    public OrderResponseDto split(OrderRequestDto orderRequestDto) {
        // Total amounts
        double totalAmount = 0;
        double totalCommissions = 0;

        // Each person amounts
        double amountPerPerson = 0;
        double amountPerPersonCommission = 0;
        double amountPerPersonOfCommonMeals = 0;
        double amountPerPersonOfCommonMealsWithCommission = 0;

        // Common meals ordered amounts
        double commonMealsOrderedAmount = 0;
        double commonMealsOrderedAmountCommission = 0;

        ArrayList<PersonOrderResponseDto> personOrderResponseDtoList = new ArrayList<>();

        int peopleNumber = orderRequestDto.listOfPeople().size();

        commonMealsOrderedAmount = calculateAmountOfCommonMealsOrdered(orderRequestDto.commonMeals(),menuRepository);
        commonMealsOrderedAmountCommission = commonMealsOrderedAmount * 0.2;
        totalCommissions += commonMealsOrderedAmountCommission;

        totalAmount += commonMealsOrderedAmount + commonMealsOrderedAmountCommission;

        for(PersonOrderRequestDto person_i : orderRequestDto.listOfPeople()) {
            amountPerPerson = calculateAmountPerPerson(person_i,menuRepository); // With no commission
            if(amountPerPerson > 0) {

                amountPerPersonCommission = amountPerPerson * 0.2; // Commission only
                amountPerPersonOfCommonMeals = commonMealsOrderedAmount / peopleNumber; // No commission price for common meal per person
                amountPerPersonOfCommonMealsWithCommission = commonMealsOrderedAmountCommission / peopleNumber; // Commission only for common meal per person

                totalCommissions += amountPerPersonCommission;
                totalAmount += amountPerPerson + amountPerPersonCommission;

                personOrderResponseDtoList.add(
                    new PersonOrderResponseDto(
                        person_i.name(),
                        person_i.meals(),
                        amountPerPerson,
                        amountPerPersonCommission,
                        roundTheAmount(amountPerPersonOfCommonMeals),
                        roundTheAmount(amountPerPersonOfCommonMealsWithCommission),
                        roundTheAmount(amountPerPersonCommission + amountPerPersonOfCommonMealsWithCommission),
                        roundTheAmount((amountPerPerson + amountPerPersonCommission) +
                            (amountPerPersonOfCommonMealsWithCommission + amountPerPersonOfCommonMeals))
                    )
                );
            }
        }



        return new OrderResponseDto(
            personOrderResponseDtoList,
            orderRequestDto.commonMeals(),
            roundTheAmount(commonMealsOrderedAmount),
            roundTheAmount(commonMealsOrderedAmountCommission),
            roundTheAmount(totalAmount - totalCommissions),
            roundTheAmount(totalCommissions),
            roundTheAmount(totalAmount)
        );
    }
}
