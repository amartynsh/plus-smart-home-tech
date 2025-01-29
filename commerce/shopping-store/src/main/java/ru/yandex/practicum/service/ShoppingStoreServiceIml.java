package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.errorhandler.exceptions.NotFoundException;
import ru.yandex.practicum.mapper.ProductMapper;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.ProductRepository;
import ru.yandex.practicum.utils.Pagination;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShoppingStoreServiceIml implements ShoppingStoreService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @Override
    public List<ProductDto> getProducts(ProductCategory category, Pageable pageable) {

        return productRepository.findAllByProductCategory(category, Pagination.getPageRequest(pageable))
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @Override
    public ProductDto saveProduct(ProductDto productDto) {
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto) {
        if (!productRepository.existsById(productDto.getProductId())) {
            throw new NotFoundException("Продукта с id " + productDto.getProductId() + " не существует");
        }
        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto)));
    }

    @Override
    public boolean deleteProduct(UUID productId) {
        if (!productRepository.existsById(productId)) {
            throw new NotFoundException("Продукта с id " + productId + " не существует");
        }
        Product product = productRepository.findById(productId).get();
        product.setProductState(ProductState.DEACTIVATE);
        productRepository.save(product);
        return true;
    }

    @Transactional
    @Override
    public boolean setProductQuantityState(SetProductQuantityStateRequest request) {
        if (productRepository.findById(request.getProductId()).isPresent()) {
            Product product = productRepository.findById(request.getProductId()).get();
            product.setQuantityState(product.getQuantityState());
            productRepository.save(product);
            return true;
        } else {
            throw new NotFoundException("Продукта с id " + request.getProductId() + " не существует");
        }
    }

    @Override
    public ProductDto getProduct(UUID productId) {
        return productMapper.toDto(productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Продукта с id " + productId + " не существует")));
    }
}
