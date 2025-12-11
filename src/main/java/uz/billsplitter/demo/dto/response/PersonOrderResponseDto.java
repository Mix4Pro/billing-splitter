package uz.billsplitter.demo.dto.response;

import java.util.ArrayList;

public record PersonOrderResponseDto (
    String name,
    ArrayList<String> PersonMeals ,
    double personMealPrice,
    double personMealPriceCommission,
    double commonMealsPricePerPerson,
    double commonMealPriceCommission,
    double TotalCommissions,
    double total
) {}
