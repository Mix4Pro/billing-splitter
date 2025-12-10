package uz.billsplitter.demo.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;

@Repository
@Getter
@Setter
public class MenuRepository {
    private HashMap<String,Double> menu = new HashMap<>();

    public MenuRepository () {
        menu.put("pasta",100.00);
        menu.put("burger",200.00);
        menu.put("steak",300.00);
        menu.put("pizza",400.00);
    }
}
//140 40 120 140