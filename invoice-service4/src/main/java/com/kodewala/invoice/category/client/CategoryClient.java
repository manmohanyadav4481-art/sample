
package com.kodewala.invoice.category.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.kodewala.invoice.category.response.CategoryResponse;

@FeignClient(name = "product-category-6")
public interface CategoryClient {

    @GetMapping("/category/{id}")
    CategoryResponse getCategoryById(@PathVariable("id") Long id);
}
