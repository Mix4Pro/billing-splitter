package uz.billsplitter.demo.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.billsplitter.demo.dto.request.OrderRequestDto;
import uz.billsplitter.demo.service.BillService;

@RestController
@AllArgsConstructor
@RequestMapping("/api/bills")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/splitting")
    private ResponseEntity <?> split ( @RequestBody OrderRequestDto orderRequestDto ) {
        System.out.println("/splitting works for real");
        return ResponseEntity.ok(billService.split(orderRequestDto));
    }
}
