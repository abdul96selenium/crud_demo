package com.example.demo.servicetest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
@AutoConfigureDataMongo
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;
    private MockMvc mockMvc;
    @InjectMocks
    private ProductService productService;
    private List<Product> productList;

    @BeforeEach
    public void setUp() {
        productList = new ArrayList<>();
        productList.add(new Product("1", "Product 1", 10.0));
        productList.add(new Product("2", "Product 2", 20.0));
    }

    @Test
    public void testGetAllProducts() {


        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();
        Assertions.assertThat(result).isEqualTo(productList);
    }

    @Test
    public void testGetProductById() {
        Product product = new Product("1", "Test Product", 10.0);

        when(productRepository.findById("1")).thenReturn(Optional.of(product));

        Product result = productService.getProductById("1");
        Assertions.assertThat(result).isEqualTo(product);

    }

    @Test
    public void testGetProductById_NotFound() {
        when(productRepository.findById("1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById("1");
        });
    }

    @Test
    public void testSaveProduct() throws Exception {
        Product product = new Product("1", "Test Product", 10.0);

        given(productRepository.save(Mockito.any(Product.class))).willAnswer((invocation -> invocation.getArgument(0)));
        Product result = productService.saveProduct(product);
        Assertions.assertThat(result).isEqualTo(product);
    }


}
