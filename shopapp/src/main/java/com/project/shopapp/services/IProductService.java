package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws Exception;

    Product getProductByID(Long id) throws Exception;

    Page<Product> getAllProduct(PageRequest pageRequest);

    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;

    void deleteProduct(Long id);

    boolean existByName(String name);

    ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
            throws Exception;
}
