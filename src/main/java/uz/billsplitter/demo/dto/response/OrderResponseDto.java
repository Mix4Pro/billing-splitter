package uz.billsplitter.demo.dto.response;

import java.util.ArrayList;

public record OrderResponseDto (
    ArrayList<PersonOrderResponseDto> listOfPeople ,
    ArrayList<String> commonMeals,
    double totalCommonMealsAmount,
    double commonMealsCommission,
    double totalWithoutCommissions,
    double totalCommissions,
    double total
) {
}
