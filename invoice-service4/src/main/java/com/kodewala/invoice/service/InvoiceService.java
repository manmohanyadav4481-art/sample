
package com.kodewala.invoice.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kodewala.invoice.category.client.CategoryClient;
import com.kodewala.invoice.category.response.CategoryResponse;
import com.kodewala.invoice.entity.InvoiceEntity;
import com.kodewala.invoice.repository.InvoiceRepository;
import com.kodewala.invoice.request.response.InvoiceRequest;
import com.kodewala.invoice.request.response.InvoiceResponse;

@Service
public class InvoiceService {

    private static final Logger log =
            LoggerFactory.getLogger(InvoiceService.class);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CategoryClient categoryClient;

    public InvoiceResponse createInvoice(InvoiceRequest invoiceRequest) {

        log.info("Creating invoice for customer: {}",
                invoiceRequest.getCutomerName());

        // Call Product Category Service through Feign
        CategoryResponse categoryResponse =
                categoryClient.getCategoryById(1L);

        if (categoryResponse != null) {
            log.info("Received category response. Category name: {}",
                    categoryResponse.getName());
        } else {
            log.warn("Category service returned an empty response");
        }

        // Create invoice entity
        InvoiceEntity entity = new InvoiceEntity();

        entity.setCutomerName(invoiceRequest.getCutomerName());
        entity.setGst(invoiceRequest.getGst());
        entity.setStatus("PAID");
        entity.setInvId("INV1234");

        // Save invoice
        entity = invoiceRepository.save(entity);

        log.debug("Saving invoice with invoice ID: {}", entity.getInvId());
        log.info("Invoice saved successfully with database ID: {}",
                entity.getId());

        // Convert entity into response
        InvoiceResponse invoiceResponse = new InvoiceResponse();

        if (entity.getId() > 0) {
            invoiceResponse.setId(entity.getId());
            invoiceResponse.setInvId(entity.getInvId());
            invoiceResponse.setCutomerName(entity.getCutomerName());
            invoiceResponse.setGst(entity.getGst());
            invoiceResponse.setStatus(entity.getStatus());

            log.info("Invoice response created successfully for ID: {}",
                    entity.getId());
        }

        return invoiceResponse;
    }

    @Cacheable(value = "mydatabase", key = "#page + '-' + #size")
    public List<InvoiceResponse> getInvoice(int page, int size) {

        log.info("Fetching invoices from database. Page: {}, Size: {}",
                page, size);

        Pageable pageable = PageRequest.of(page, size);

        Page<InvoiceEntity> result =
                invoiceRepository.findAll(pageable);

        log.info("Fetched {} invoices from database",
                result.getNumberOfElements());

        List<InvoiceResponse> responses = new ArrayList<>();

        for (InvoiceEntity invoiceEntity : result.getContent()) {

            InvoiceResponse invoiceResponse = new InvoiceResponse();

            invoiceResponse.setId(invoiceEntity.getId());
            invoiceResponse.setInvId(invoiceEntity.getInvId());
            invoiceResponse.setCutomerName(
                    invoiceEntity.getCutomerName());
            invoiceResponse.setGst(invoiceEntity.getGst());
            invoiceResponse.setStatus(invoiceEntity.getStatus());

            responses.add(invoiceResponse);
        }

        log.info("Successfully prepared {} invoice responses",
                responses.size());

        return responses;
    }
}
