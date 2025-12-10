package uz.billsplitter.demo.dto.request;

import java.util.ArrayList;

public record OrderRequestDto (
    ArrayList<PersonOrderRequestDto> listOfPeople ,
    ArrayList<String> commonMeals
) {}
