package lieu.shopapp.services;

import lieu.shopapp.dtos.ProductDTO;
import lieu.shopapp.dtos.ProductImageDTO;
import lieu.shopapp.exceptions.DataNotFoundException;
import lieu.shopapp.models.Product;
import lieu.shopapp.models.ProductImage;
import lieu.shopapp.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(long productId) throws Exception ;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(long id);
    boolean existBynName(String name);
    ProductImage createProductImage(long productId, ProductImageDTO productImageDTO) throws Exception;

}
