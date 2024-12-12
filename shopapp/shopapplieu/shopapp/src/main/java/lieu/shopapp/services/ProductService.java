package lieu.shopapp.services;

import lieu.shopapp.dtos.ProductDTO;
import lieu.shopapp.dtos.ProductImageDTO;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.exceptions.InvalidException;
import lieu.shopapp.models.Category;
import lieu.shopapp.models.Product;
import lieu.shopapp.models.ProductImage;
import lieu.shopapp.repositories.CategoryRepository;
import lieu.shopapp.repositories.ProductImageRepository;
import lieu.shopapp.repositories.ProductRepository;
import lieu.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static lieu.shopapp.models.ProductImage.MAXIMUM_IMAGE_PER_PRODUCT;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("Category not found with id"+productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(long productId) throws Exception {
        Product getProduct = productRepository.findById(productId).
                orElseThrow(()-> new DataNotFoundException("cannot product with id"+productId));
        return getProduct;
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest).map(ProductResponse::fromproduct
                                                          // ProductResponse.fromproduct(product)
        );
    }

    @Override
    public Product updateProduct(long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        if (existingProduct == null) {
//            Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
//                    .orElseThrow(() -> new DataNotFoundException(
//                            "Category not found with id" + productDTO.getCategoryId()));
            throw new DataNotFoundException("Product not found with id"+id);
        }
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId()).orElseThrow(() -> new DataNotFoundException(
                            "Category not found with id" + productDTO.getCategoryId()));
            existingProduct.setName(productDTO.getName());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setThumbnail(productDTO.getThumbnail());
            existingProduct.setCategory(existingCategory);
            existingProduct.setDescription(productDTO.getDescription());
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(long id) {
        Optional<Product> productOptional = productRepository.findById(id);
        productOptional.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existBynName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception {
        //Product existingProduct = getProductById(productImageDTO.getProductId());
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(()-> new DataNotFoundException(
                        "Product not found with id"+productImageDTO.getProductId()));
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(productImageDTO.getImageUrl())
                .build();

        int size = productImageRepository.findByProductId(productId).size();
        if (size >=MAXIMUM_IMAGE_PER_PRODUCT) {
            throw new InvalidException("ảnh không nhiều hơn "+MAXIMUM_IMAGE_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }

}
