package uz.billsplitter.demo.dto.request;

import java.util.ArrayList;

public record PersonOrderRequestDto(
    String name,
    ArrayList<String> meals
) {}
