package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.LocationNotFoundException;
import com.example.vm.dto.UUIDDTO;
import com.example.vm.dto.post.AddressPostDTO;
import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.AddressPutDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.CustomerDetailPayload;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.CustomerListPayload;
import com.example.vm.payload.report.AssignmentCustomerReportListPayload;
import com.example.vm.payload.report.CountByTypeListPayload;
import com.example.vm.repository.ContactRepository;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.VisitTypeRepository;
import com.example.vm.service.formatter.PhoneNumberFormatter;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.awt.AWTEventMulticaster.add;

@Service
public class CustomerService {

    private static final String GEOLOCATION_KEY = "AIzaSyC1rCFrBqu32lHImkYyDBSyfmaxp5YCPao";

    private final CustomerRepository customerRepository;
    private final VisitTypeRepository visitTypeRepository;

    private final ContactRepository contactRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, VisitTypeRepository visitTypeRepository, ContactRepository contactRepository) {
        this.customerRepository = customerRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.contactRepository = contactRepository;
    }

    public ResponseEntity<List<CustomerListPayload>> findAllCustomers() {
        return ResponseEntity.ok(CustomerListPayload.toPayload(customerRepository.findAll()));
    }

    public ResponseEntity<List<CustomerListPayload>> findAllCustomersWhoHasAssignment() {

   return ResponseEntity.ok(CustomerListPayload.toPayload(customerRepository.findAllCustomer()));
    }


    public ResponseEntity<List<CustomerListPayload>> findAllenableCustomers() {
        return ResponseEntity.ok(CustomerListPayload.toPayload(customerRepository.findCustomerByEnabled(1)));
    }

    public ResponseEntity<CustomerDetailPayload> findCustomerByUUID(UUID id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        return ResponseEntity.ok(foundCustomer.toDetailPayload());
    }

    public ResponseEntity<List<AssignmentCustomerReportListPayload>>findCustomer(UUID id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));
        return ResponseEntity.ok(AssignmentCustomerReportListPayload.topayLoad(foundCustomer));

    }


    public ResponseEntity<List<CustomerListPayload>> searchByQuery(String query) {

        List<Customer> customerList = customerRepository.searchCustomersByNameContainingOrAddress_CityContainingOrAddress_AddressLine1ContainingOrAddress_AddressLine2Containing(query, query, query, query);
        return ResponseEntity.ok(CustomerListPayload.toPayload(customerList));
    }

    public ResponseEntity<List<ContactListPayload>> getCustomerContacts(UUID id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        List<Contact> contactList = foundCustomer.getContacts();

        return ResponseEntity.ok(ContactListPayload.toPayload(contactList));
    }

    public ResponseEntity<CustomerDetailPayload> saveNewCustomer(CustomerPostDTO customerRequest) {
        Customer customerToSave;

        AddressPostDTO addressRequest = customerRequest.getAddress();
        if (!addressRequest.getPrecise()) {
            customerToSave = Customer.builder()
                    .name(customerRequest.getName())
                    .address(Address.builder()
                            .addressLine1(addressRequest.getAddressLine1())
                            .addressLine2(addressRequest.getAddressLine2())
                            .isPrecise(addressRequest.getPrecise())
                            .zipcode(addressRequest.getZipcode())
                            .city(addressRequest.getCity())
                            .build())
                    .visitAssignments(new ArrayList<>())
                    .enabled(1)
                    .build();

            try {
                setLngLat(customerToSave, addressRequest.getAddressLine1(), addressRequest.getAddressLine2(), addressRequest.getCity(), addressRequest.getZipcode());

                customerToSave = customerRepository.save(customerToSave);

                return ResponseEntity.status(HttpStatus.CREATED).body(customerToSave.toDetailPayload());

            } catch (Exception e) {
                System.out.println("ERROR " + e.getMessage());
            }

            throw new LocationNotFoundException();
        } else {
             customerToSave = Customer.builder()
                    .name(customerRequest.getName())
                    .address(Address.builder()
                            .addressLine1(addressRequest.getAddressLine1())
                            .addressLine2(addressRequest.getAddressLine2())
                            .zipcode(addressRequest.getZipcode())
                            .city(addressRequest.getCity())
                            .longitude(addressRequest.getLongitude())
                            .latitude(addressRequest.getLatitude())
                            .build())
                    .visitAssignments(new ArrayList<>())
                    .enabled(1)
                    .build();

            customerToSave = customerRepository.save(customerToSave);

        }
        return ResponseEntity.status(HttpStatus.CREATED).body(customerToSave.toDetailPayload());
    }



    public ResponseEntity<CustomerDetailPayload> saveContactToCustomer(UUID id, ContactPostDTO contactRequest) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        List<VisitType> visitTypes = new ArrayList<>();

        for (UUIDDTO uuiddto: contactRequest.getTypes()){
            VisitType visitType = visitTypeRepository.findById(uuiddto.getUuid())
                    .orElseThrow( () -> new EntityNotFoundException(EntityNotFoundException.TYPE_NOT_FOUND));

            visitTypes.add(visitType);
        }

        String formattedNumber = PhoneNumberFormatter.formatPhone(contactRequest.getPhoneNumber());

        contactRequest.setPhoneNumber(formattedNumber);

        Contact newContact = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .email(contactRequest.getEmail())
                .phoneNumber(contactRequest.getPhoneNumber())
                .visitTypes(visitTypes)
                .enabled(1)
                .build();

        foundCustomer.getContacts().add(newContact);
        newContact.setCustomer(foundCustomer);

        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(foundCustomer.toDetailPayload());
    }

    public ResponseEntity<CustomerDetailPayload> updateCustomer(UUID id, CustomerPutDTO customerRequest) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        AddressPutDTO addressRequest = customerRequest.getAddress();

        customerToUpdate.setName(customerRequest.getName());
        customerToUpdate.getAddress().setAddressLine1(addressRequest.getAddressLine1());
        customerToUpdate.getAddress().setAddressLine2(addressRequest.getAddressLine2());
        customerToUpdate.getAddress().setCity(addressRequest.getCity());
        customerToUpdate.getAddress().setZipcode(addressRequest.getZipcode());

        try {
            setLngLat(customerToUpdate, addressRequest.getAddressLine1(), addressRequest.getAddressLine2(), addressRequest.getCity(), addressRequest.getZipcode());

            customerToUpdate = customerRepository.save(customerToUpdate);

            return ResponseEntity.ok(customerToUpdate.toDetailPayload());
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }

        throw new LocationNotFoundException();
    }

    public ResponseEntity<CustomerDetailPayload> enableCustomer(UUID id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        foundCustomer.setEnabled(foundCustomer.getEnabled() == 0 ? 1 : 0);

        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.ok(foundCustomer.toDetailPayload());
    }
        public ResponseEntity <List<CountByTypeListPayload>> countAllCustomer(){
        ArrayList<CountByTypeListPayload>countbyType=new ArrayList<>();
        List<VisitType>visitTypeList=visitTypeRepository.findAll();
        double count=contactRepository.count();
             for(int i = 0; i<visitTypeList.size() ;++i){
             double countOfContact= contactRepository.countContactsByVisitTypesContaining(visitTypeList.get(i));
             System.out.println(visitTypeList.get(i).getName());
             System.out.println(countOfContact);
             double percentage =countOfContact/count;
             System.out.println(count);
             countbyType.add(new CountByTypeListPayload(visitTypeList.get(i).getName(),percentage))   ;
        }
       return  ResponseEntity.ok(countbyType);
    }

    private static void setLngLat(Customer customerToUpdate, String addressLine1, String addressLine2, String city, String zipcode) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(GEOLOCATION_KEY)
                .build();

        System.out.println("SEARCHING FOR LOCATION: " + addressLine1 + " " + addressLine2 + ", " +
                city + " " + zipcode);

        GeocodingResult[] results = GeocodingApi.geocode(context,
                addressLine1 + " " + addressLine2 + ", " +
                        city + " " + zipcode).await();

        customerToUpdate.getAddress().setLatitude(results[0].geometry.location.lat);
        customerToUpdate.getAddress().setLongitude(results[0].geometry.location.lng);

        context.shutdown();
    }


}
