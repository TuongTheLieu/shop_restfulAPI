package lieu.shopapp.controller;

import com.github.javafaker.Faker;
import jakarta.validation.Valid;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import lieu.shopapp.dtos.ProductDTO;
import lieu.shopapp.dtos.ProductImageDTO;
import lieu.shopapp.models.Product;
import lieu.shopapp.models.ProductImage;
import lieu.shopapp.repositories.ProductRepository;
import lieu.shopapp.responses.ProductListResponse;
import lieu.shopapp.responses.ProductResponse;
import lieu.shopapp.services.ProductService;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
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
import org.springframework.web.server.ResponseStatusException;

import static lieu.shopapp.models.ProductImage.MAXIMUM_IMAGE_PER_PRODUCT;

@RestController
@RequestMapping("${api.prefix}/products")

public class ProductController {
    private final ProductRepository productRepository;
    private final ProductService productService;

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @PostMapping("")
    public ResponseEntity<?> createProduct(@Valid @RequestBody
                                               ProductDTO productDTO,
                                           BindingResult result
    ){
        try {
            if (result.hasErrors()) {
                List<String> errorMessage = result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
                return ResponseEntity.badRequest().body(errorMessage);
            }
            Product product =  productService.createProduct(productDTO);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") Long productId,
                                         @RequestPart("files") List<MultipartFile> files) {
        // List<MultipartFile> files = productDTO.getFiles();
        // nếu không truyền ảnh sẽ bị null và nhảy vào exception nên phải cho mảng rỗng nếu kh truyền ảnh
        try {
            Product existingproduct = productService.getProductById(productId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if (files.size() > MAXIMUM_IMAGE_PER_PRODUCT) {
                return ResponseEntity.badRequest().body("khong the up load qua 5 anh");
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file : files) {
                // nếu có truyền files mà không bỏ ảnh vào sẽ tự gán file name rỗng và size =1 và file.getSize = 0
                if (file.getSize() == 0) {
                    continue;
                }
                if (file.getSize() > 10 * 1024 * 1024) {
                    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("max 10MB");
                }
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("khong phai file anh");
                }
                // lưu file và cập nhật thumbnail trong DTO
                String fileName = storeFile(file);
                ProductImage productImage = productService.createProductImage(
                        existingproduct.getId(),
                        ProductImageDTO.builder()
                                .imageUrl(fileName)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok().body(productImages);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



    // Tránh nhiều người gửi file trùng tên sẽ mất ảnh cũ nên đổi tên file là duy nhất
    private String storeFile(MultipartFile file) throws IOException {
        if (!isImagesFile(file) || file.getOriginalFilename() == null) {   }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID().toString() + "_" + fileName;
        // đường dẫn muốn lưu file
        Path uploadDir = Paths.get("uploads");
        if (!Files.exists(uploadDir)) {
            Files.createDirectory(uploadDir);
        }
        // đường dẫn đầy đủ đến file
        Path destination = Paths.get(uploadDir.toString(), uniqueFileName);
        // sao chép file vào thư mục đích
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFileName;
    }
    private boolean isImagesFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType == null || !contentType.startsWith("image/");
    }

        @GetMapping("")
    public ResponseEntity<ProductListResponse> getAllProducts(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        // tạo Pageable
            PageRequest pageRequest = PageRequest.of(page, limit, Sort.by("createdAt").descending());
            Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
            // lấy tổng số trang
            int totalPage = productPage.getTotalPages();
            List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse.builder()
                        .products(products)
                        .totalPage(totalPage)
                        .build());
    }
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable long productId ) {
        try {
            Product existingproduct = productService.getProductById(productId);
            return ResponseEntity.ok(ProductResponse.fromproduct(existingproduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("Product with id " + productId + " deleted successfully");
    }
   // @PostMapping("/generateFakerProducts")
    private ResponseEntity<String> generateFakerProducts() {
        Faker faker = new Faker();
        for (int i = 0; i < 1000; i++) {
            String productName = faker.commerce().productName();
            if (productService.existBynName(productName)) {
                continue;
            }
            ProductDTO productDTO = ProductDTO.builder()
                    .name(productName)
                    .price((float)faker.number().numberBetween(10, 1000000))
                    .thumbnail("")
                    .description(faker.lorem().sentence())
                    .categoryId((long)faker.number().numberBetween(1,3))
                    .build();
            try {
                productService.createProduct(productDTO);
            }catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.ok().body("success");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id,
                                                    @RequestBody
                                                    ProductDTO productDTO) {
        try {
            Product updateProduct = productService.updateProduct(id, productDTO);
            return ResponseEntity.ok(updateProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
