package com.smartbusiness.businessmanagement.service;

import com.smartbusiness.businessmanagement.dto.request.PosSaleRequest;
import com.smartbusiness.businessmanagement.dto.response.PosSaleResponse;

public interface PosSaleService {

    PosSaleResponse completeSale(
            PosSaleRequest request
    );
}