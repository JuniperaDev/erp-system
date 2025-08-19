package io.github.erp.internal.acl.financial;

import io.github.erp.service.dto.DealerDTO;

import java.util.Optional;

public interface VendorHarmonizationACL {
    
    Optional<DealerDTO> harmonizeVendorData(DealerDTO vendor);
    
    boolean validateVendorData(DealerDTO vendor);
    
    Optional<String> standardizeVendorCode(DealerDTO vendor);
    
    Optional<String> extractVendorCategory(DealerDTO vendor);
}
