package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.response.InvoiceResponse;

public interface InvoiceService {

    InvoiceResponse createInvoice(Long orderId);

    InvoiceResponse getInvoiceByOrderId(Long orderId);

}