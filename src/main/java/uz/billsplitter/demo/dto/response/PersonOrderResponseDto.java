package uz.billsplitter.demo.dto.response;

import java.util.ArrayList;

public record PersonOrderResponseDto (
    String name,
    ArrayList<String> meals ,
    double mealPrice,
    double commission,
    double total
) {}
