package com.example.shop.app.controller;

import com.example.shop.app.DTO.ProductDTO;
import com.example.shop.app.DTO.ProductImageDTO;
import com.example.shop.app.exception.DataNotFoundException;
import com.example.shop.app.exception.InvalidParamException;
import com.example.shop.app.models.Product;
import com.example.shop.app.models.ProductImage;
import com.example.shop.app.responses.ProductListResponse;
import com.example.shop.app.responses.ProductResponse;
import com.example.shop.app.services.IProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final IProductService productService;

    //    http://localhost:8088/api/v1/products?page=1&limit=12
    @GetMapping("")
    public ResponseEntity<ProductListResponse> getProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        //tạo pageable từ thông tin trang và giới hạn
        PageRequest pageRequest=PageRequest.of(page, limit, Sort.by("createdAt"));
        Page<ProductResponse> productPage=productService.getAllProducts(pageRequest);
        //lấy tổng số trang
        int totalPages=productPage.getTotalPages();
        List<ProductResponse> products=productPage.getContent();
        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPage(totalPages)
                        .build());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable("id") Long productId) {

        try {
            Product existingProduct =productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromProduct(existingProduct));
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("")
    public ResponseEntity<?> insertCategories(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ){
        try{
            if (result.hasErrors()){
                List<String> errorMessages= result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            Product newProduct=productService.createProduct(productDTO);

            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@ModelAttribute("files") List<MultipartFile> files,
                                          @PathVariable("id") Long productId) throws InvalidParamException, IOException {
        try {
            Product existingProduct=productService.getProductById(productId);
            files = files == null ? new  ArrayList<MultipartFile>(): files;
            if (files.size()>ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
                return ResponseEntity.badRequest().body("only upload max 5 image");
            }
            List<ProductImage> productImages=new ArrayList<>();
            for (MultipartFile file: files) {
                //kiểm tra kích thước file và định dạng
                if (file.getSize()==0){
                    continue;
                }
                if (file.getSize()>10*1024*1024){ // >10MB
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("file is too large! Maximum size is 10MB");
                }
                String contentType =file.getContentType();
                if (contentType ==null|| !contentType.startsWith("image/")){
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("file must be an image");
                }
                //lưu file và cập nhật thumbnail trong DTO
                String fileName = storeFile(file);
                //lưu đối tượng product vào db
                ProductImage productImage = productService.createProductImage(existingProduct.getId(), ProductImageDTO.builder().imageUrl(fileName).build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (DataNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable long id){
        try {
            productService.deleteProduct(id);
            return ResponseEntity.ok(String.format("Product with id %d deleted successfully",id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    private boolean isImageFile(MultipartFile file){
        String contentType=file.getContentType();
        return contentType!=null &&contentType.startsWith("image/");
    }
    private String storeFile(MultipartFile file) throws IOException{
        if (!isImageFile(file)||file.getOriginalFilename()==null){
            throw new IOException("Invalid image format");
        }

        String fileName= StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // thêm UUID vào trc tên file đảm bảo file này là duy nhất
        String uniqueFileName = UUID.randomUUID().toString()+"_"+fileName;
        // Đường dẫn đến thư mục mà muốn lưu file
        java.nio.file.Path uploadDir= Paths.get("uploads");
        // Kiểm tra và tạo thư mục nếu nó không tồn tại
        if (!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        //đường dẫn đầy đủ đến file
        java.nio.file.Path destination =Paths.get(uploadDir.toString(),uniqueFileName);
        //sao chép file vào thư mục đích
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id,@RequestBody ProductDTO productDTO){
        try {
            Product updatedproduct= productService.updateProduct(id,productDTO);
            return ResponseEntity.ok(ProductResponse.fromProduct(updatedproduct));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
//    @PostMapping("/generateFakeProducts")
//    private ResponseEntity<String> generateFakeProducts(){
//        Faker faker=new Faker();
//        for (int i = 0; i < 1000; i++) {
//            String productName=faker.commerce().productName();
//            if (productService.existsByName(productName)){
//                continue;
//            }
//            ProductDTO productDTO=ProductDTO.builder()
//                    .name(productName)
//                    .price((float) faker.number().numberBetween(10,1000000))
//                    .description(faker.lorem().sentence())
//                    .thumbnail("")
//                    .categoryId((long) faker.number().numberBetween(6,8))
//                    .build();
//            try {
//                productService.createProduct(productDTO);
//            } catch (DataNotFoundException e) {
//                return ResponseEntity.badRequest().body(e.getMessage());
//            }
//        }
//        return ResponseEntity.ok("fake product created successfully");
//    }
}
