package com.pbdcompany;

import com.pbdcompany.entity.Customer;
import com.pbdcompany.repository.CustomerRepository;
import com.pbdcompany.service.CustomerDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class CustomerDetailsServiceTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerDetailService customerDetailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 初始化 Mockito 注解
    }

    @Test
    void testLoadUserByUsername_Success() {
        // 创建测试数据
        String username = "testUser";
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setPassword("<PASSWORD>");

        // 模拟仓库返回测试数据
        when(customerRepository.findByUsername(username)).thenReturn(Optional.of(customer));

        // 调用方法进行测试
        UserDetails userDetails = customerDetailService.loadUserByUsername(username);

        // 验证结果
        assertEquals(username, userDetails.getUsername());
    }
    @Test
    void testLoadUserByUsername_NotFound() {
        // Arrange
        String username = "nonExistentUser";
        when(customerRepository.findByUsername(username))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            customerDetailService.loadUserByUsername(username);
        });
        verify(customerRepository, times(1)).findByUsername(username);
    }

}
