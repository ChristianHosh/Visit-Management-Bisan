package com.example.vm.controller;

import com.example.vm.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@CrossOrigin
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }


    @GetMapping("/receipts")
    public ResponseEntity<?> getAllReceipts(){
        return documentService.getAllReceipts();
    }

    @GetMapping("/receipts/search")
    public ResponseEntity<?> searchReceipts(
       @RequestParam(name = "customer", required = false) Long customerId,
       @RequestParam(name = "user", required = false) String userUsername,
       @RequestParam(name = "type", required = false) String visitTypeName,
       @RequestParam(name = "from", required = false) String startDate,
       @RequestParam(name = "to", required = false) String endDate
    ){
        return documentService.searchReceipts(customerId, userUsername, visitTypeName, startDate, endDate);
    }

}
