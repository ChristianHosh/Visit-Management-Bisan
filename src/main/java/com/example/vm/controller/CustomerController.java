package com.example.vm.controller;

import com.example.vm.controller.error.exception.LocationNotFoundException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.AddressPutDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.payload.CustomerPayload;
import com.example.vm.service.AddressService;
import com.example.vm.service.ContactService;
import com.example.vm.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ContactService contactService;
    private final AddressService addressService;

    @Autowired
    public CustomerController(CustomerService customerService, ContactService contactService, AddressService addressService) {
        this.customerService = customerService;
        this.contactService = contactService;
        this.addressService = addressService;
    }

    @GetMapping("")
    public ResponseEntity<List<CustomerPayload>> getAllCustomers() {
        List<Customer> customerList = customerService.findAllCustomers();

        return new ResponseEntity<>(toCustomerPayloadList(customerList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<List<CustomerPayload>> searchByCustomerName(@RequestParam("name") String name){
        List<Customer> customerList = customerService.searchByName(name);

        return new ResponseEntity<>(toCustomerPayloadList(customerList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "city")
    public ResponseEntity<List<CustomerPayload>> searchByCustomerCity(@RequestParam("city") String city){
        List<Customer> customerList = customerService.searchByAddressCity(city);

        return new ResponseEntity<>(toCustomerPayloadList(customerList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "address")
    public ResponseEntity<List<CustomerPayload>> searchByCustomerAddress(@RequestParam("address") String address){
        List<Customer> customerList = customerService.searchByAddressLine(address);

        return new ResponseEntity<>(toCustomerPayloadList(customerList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<List<Contact>> getContactsByCustomerUUID(@PathVariable UUID id) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Contact> contactList = customer.getContacts();

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Customer> saveNewCustomer(@RequestBody @Valid CustomerPostDTO customerRequest) {
        Customer savedCustomer = customerService.saveNewCustomer(customerRequest);

        if (savedCustomer == null)
            throw new LocationNotFoundException();
            //THROW LOCATION NOT FOUND EXCEPTION

        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<Contact> SaveContactToCustomerByUUID(@PathVariable UUID id, @RequestBody @Valid ContactPostDTO contactRequest) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            throw new UserNotFoundException();


        Contact savedContact = contactService.saveNewContact(customer, contactRequest);

        return new ResponseEntity<>(savedContact, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @RequestBody @Valid CustomerPutDTO customerupdated) {
        Customer customerToUpdate = customerService.findCustomerByUUID(id);
        if (customerToUpdate == null)
            throw new UserNotFoundException();

      Customer updatedCustomer = customerService.updateCustomer(customerToUpdate,customerupdated);
      return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<Address> updateCustomerAddress (@PathVariable UUID id, @RequestBody @Valid AddressPutDTO updatedAddress ) {
        Customer customerToUpdate = customerService.findCustomerByUUID(id);

        if (customerToUpdate == null)
            throw new UserNotFoundException();

        Address updatedCustomer = addressService.updateCustomerAddress(customerToUpdate.getAddress(), updatedAddress);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<Customer> enableCustomer(@PathVariable UUID id) {
        Customer customerToEnable = customerService.findCustomerByUUID(id);

        if (customerToEnable == null)
            throw new UserNotFoundException();

        customerToEnable = customerService.enableCustomer(customerToEnable);

        return new ResponseEntity<>(customerToEnable, HttpStatus.OK);
    }

    private static List<CustomerPayload> toCustomerPayloadList(List<Customer> customerList) {
        return customerList.stream().map(Customer::toPayload).toList();
    }
}