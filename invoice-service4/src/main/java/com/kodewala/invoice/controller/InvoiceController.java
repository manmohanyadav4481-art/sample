package com.kodewala.invoice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kodewala.invoice.request.response.InvoiceRequest;
import com.kodewala.invoice.request.response.InvoiceResponse;
import com.kodewala.invoice.service.InvoiceService;

@RestController
@RequestMapping("/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/create")
    public ResponseEntity<InvoiceResponse> createInvoice(
            @RequestBody InvoiceRequest invoiceRequest) {

        InvoiceResponse response =
                invoiceService.createInvoice(invoiceRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/listInvoices")
    public ResponseEntity<List<InvoiceResponse>> getAllInvoices(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size) {

        System.out.println("Controller: /listInvoices called");

        List<InvoiceResponse> invoiceResponses =
                invoiceService.getInvoice(page, size);

        return ResponseEntity.ok(invoiceResponses);
    }
}