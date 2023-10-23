package org.prgms.customer;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    //Service Layer에 어노테이션 하나로 트랜잭션 적용
    @Transactional
    public void createdCustomers(List<Customer> customerList){
        customerList.forEach(customerRepository::insert);
    }
}
