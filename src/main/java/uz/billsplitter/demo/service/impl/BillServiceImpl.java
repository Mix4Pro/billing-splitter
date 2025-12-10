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

    @Override
    public OrderResponseDto split(OrderRequestDto orderRequestDto) {
        double totalAmount = 0;
        double totalCommissions = 0;

        double amountPerPerson = 0;
        double amountPerPersonCommission = 0;

        double commonMealsOrderedAmount = 0;
        double commonMealsOrderedAmountCommission = 0;

        ArrayList<PersonOrderResponseDto> personOrderResponseDtoList = new ArrayList<>();

        for(PersonOrderRequestDto person_i : orderRequestDto.listOfPeople()) {
            amountPerPerson = calculateAmountPerPerson(person_i,menuRepository);
            amountPerPersonCommission = amountPerPerson * 0.2;
            totalCommissions += amountPerPersonCommission;
            if(amountPerPerson > 0) {
                totalAmount += amountPerPerson + amountPerPersonCommission;

                personOrderResponseDtoList.add(
                    new PersonOrderResponseDto(
                        person_i.name(),
                        person_i.meals(),
                        amountPerPerson,
                        amountPerPersonCommission,
                        amountPerPerson + amountPerPersonCommission
                    )
                );
            }
        }

        commonMealsOrderedAmount = calculateAmountOfCommonMealsOrdered(orderRequestDto.commonMeals(),menuRepository);
        commonMealsOrderedAmountCommission = commonMealsOrderedAmount * 0.2;
        totalCommissions += commonMealsOrderedAmountCommission;

        totalAmount += commonMealsOrderedAmount + commonMealsOrderedAmountCommission;

        return new OrderResponseDto(
            personOrderResponseDtoList,
            orderRequestDto.commonMeals(),
            commonMealsOrderedAmountCommission,
            totalAmount - totalCommissions,
            totalCommissions,
            totalAmount
        );
    }
}
