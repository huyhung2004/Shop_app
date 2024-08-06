package com.example.shop.app.services;

import com.example.shop.app.DTO.ProductDTO;
import com.example.shop.app.DTO.ProductImageDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.exception.InvalidParamException;
import com.example.shop.app.models.Category;
import com.example.shop.app.models.Product;
import com.example.shop.app.models.ProductImage;
import com.example.shop.app.repositories.CategoryRepository;
import com.example.shop.app.repositories.ProductImageRepository;
import com.example.shop.app.repositories.ProductRepository;
import com.example.shop.app.responses.ProductResponse;
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
    private  final ProductImageRepository productImageRepository;
    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory= categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                ()-> new DataNotFoundException("Can't find category id :"+productDTO.getCategoryId()));
        Product newProduct= Product.builder()
                .price(productDTO.getPrice())
                .category(existingCategory)
                .thumbnail(productDTO.getThumbnail())
                .description(productDTO.getDescription())
                .name(productDTO.getName())
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id).orElseThrow(()->new DataNotFoundException("Can't find product with id "+id));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        // lấy danh sách sản phẩm theo trang và giới hạn

        return productRepository.findAll(pageRequest).map(ProductResponse::fromProduct);
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct=getProductById(id);
        if (existingProduct!=null){
            Category existingCategory= categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(
                    ()-> new DataNotFoundException("Can't find category id :"+productDTO.getCategoryId()));

            existingProduct.setName(productDTO.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setDescription(productDTO.getDescription());
            return productRepository.save(existingProduct);
        }
        return null;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct= productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);

    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
    @Override
    public ProductImage createProductImage(long id, ProductImageDTO productImageDTO)
            throws DataNotFoundException, InvalidParamException
    {
        Product existingProduct= productRepository.findById(id).orElseThrow(
                ()-> new DataNotFoundException("Can't find product id :"+productImageDTO.getProductId()));

        ProductImage newProductImage=ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();
        //ko chèn quá 5 ảnh
        int size= productImageRepository.findByProductId(id).size();
        if (size >= ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException("Number of image must be <= "+ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
