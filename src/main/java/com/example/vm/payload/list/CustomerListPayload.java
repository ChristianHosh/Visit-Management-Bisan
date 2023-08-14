package com.example.vm.payload.list;

import com.example.vm.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class CustomerListPayload {

    private UUID uuid;

    private String name;

    private int enabled;

    public static List<CustomerListPayload> toPayload(List<Customer> customerList) {
        return customerList.stream().map(Customer::toListPayload).toList();
    }
}
