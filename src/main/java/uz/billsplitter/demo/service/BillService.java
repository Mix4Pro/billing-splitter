package uz.billsplitter.demo.service;

import uz.billsplitter.demo.dto.request.OrderRequestDto;
import uz.billsplitter.demo.dto.response.OrderResponseDto;

public interface BillService {
    OrderResponseDto split (OrderRequestDto orderRequestDto);
}
