package com.project.shopapp.services;

import com.project.shopapp.dtos.ProductDTO;
import com.project.shopapp.dtos.ProductImageDTO;
import com.project.shopapp.exceptions.DataNotFoundException;
import com.project.shopapp.exceptions.InvalidParamException;
import com.project.shopapp.models.Category;
import com.project.shopapp.models.Product;
import com.project.shopapp.models.ProductImage;
import com.project.shopapp.repositories.CategoryRepository;
import com.project.shopapp.repositories.ProductImageRepository;
import com.project.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existCategory = categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id " + productDTO.getCategoryId()));
        Product newProduct = Product
                .builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .category(existCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductByID(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Cannot find product with id " + id));
    }

    @Override
    public Page<Product> getAllProduct(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existProduct = getProductByID(id);
        if (existProduct != null) {
            Category existCategory = categoryRepository
                    .findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new DataNotFoundException(
                            "Cannot find category with id " + productDTO.getCategoryId()));
            existProduct.setName(productDTO.getName());
            existProduct.setPrice(productDTO.getPrice());
            existProduct.setDescription(productDTO.getDescription());
            existProduct.setThumbnail(productDTO.getThumbnail());
            existProduct.setCategory(existCategory);
            return productRepository.save(existProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(Long productId, ProductImageDTO productImageDTO)
            throws Exception {
        Product existProduct = productRepository
                .findById(productId)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find product with id " + productId));
        ProductImage newProductImage = ProductImage
                .builder()
                .product(existProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        int size = productImageRepository.findByProductId(productId).size();
        if (size >= 5) {
            throw new InvalidParamException("Number of images must be <= 5");
        }
        return productImageRepository.save(newProductImage);
    }
}
