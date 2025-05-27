package com.pbdcompany;//package com.pbdcompany;
//
//
//import com.pbdcompany.entity.Admin;
//import com.pbdcompany.entity.CartItem;
//import com.pbdcompany.entity.Merchant;
//import com.pbdcompany.mapper.AdminMapper;
//import com.pbdcompany.mapper.CartItemMapper;
//import com.pbdcompany.mapper.MerchantMapper;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//@SpringBootTest
//public//springboot单元测试的注解，测试方法在运行时，会自动启动springboot项目，且自动生成IDC容器
//class SpringbootMybatisApplicationTests {
//
//    /*
//        测试1：admin表
//     */
//    @Autowired
//    private AdminMapper adminMapper;
//
//    @Test
//    public void testFindAdmin(){
//        List<Admin> adminList = adminMapper.findAll();
//        adminList.forEach(System.out::println);
//    }
//
//    @Test
//    public void testUpdateAdmin(){
//        adminMapper.update(new Admin(1, "真·李浩", "123456"));
//
//    }
//
//    @Test
//    public void testFindAdminById(){
//        Admin admin = adminMapper.findById(1);
//        System.out.println(admin);
//    }
//
//
//
///*
//    测试3：cartitem表
// */
//
//    @Autowired
//    private CartItemMapper cartItemMapper;
//
//    //注: 插入购物车物品时，需满足外键约束，同时(cartid和productid这一组数据在表中已经存在时，不允许插入)
//    //注：即使插入失败，主键的ID依然会自动递增
//    @Test
//    public void testAddCartItem(){
//        CartItem cartItem = new CartItem(0, 4, 8, 3,"Test", "Test");
//        cartItemMapper.insert(cartItem);
//    }
//
//    @Test
//    public void testUpdateCartItem(){
//        CartItem cartItem = new CartItem(0, 2, 9, 3,"Test5", "Test5");
//        cartItemMapper.update(cartItem);
//    }
//
//    @Test
//    public void testFindCartItemById(){
//        CartItem cartItem = cartItemMapper.findById(2);
//        System.out.println(cartItem);
//    }
//
///*
//    测试4：customer表
//*/
//
//    @Autowired
//    private CustomerMapper customerMapper;
//
//    @Test
//    public void testFindAll(){
//        List<Customer> customerList = customerMapper.findAll();
//        customerList.forEach(System.out::println);
//    }
//
//    //注：如果在尝试删除某条数据时，如果该数据有外键约束，则删除失败，需要先删除外键约束的数据
//    @Test
//    public void testDeleteById(){
//        Integer i = customerMapper.deleteById(110);
//        System.out.println("执行语句影响到的记录数为：" + i);
//    }
//
//
//    //注：由于主键不允许重复且自增，所以类为单位插入数据时，可以将主键设置为0，以避免重复
//    @Test
//    public void testInsert(){
//        Customer customer = new Customer(0, "李浩6", "123456", 987689., "9582164", "山东省临淄区XXXX", "");
//        customerMapper.insert(customer);
//    }
//
//    @Test
//    public void testUpdate(){
//        //Customer customer = new Customer(3, "李浩4", "123456", 1314520.25, "5273461", "离开家很过分打撒");
//        //customerMapper.update(customer);
//
//        //以下是通过不构造对象，只穿递参数的方式进行查询，
//        //这种方式可以避免直接创建对象，在不需要对象的全部参数时可以方便地使用SQL语句
//        customerMapper.update(3, "李浩333", "123456", 1314520.25, "5273461", "离开家很过分打撒");
//    }
//
//    @Test
//    public void testFindById(){
//        Customer 李浩2 = customerMapper.findByUsernameAndPassword("李浩2", "123456");
//        System.out.println(李浩2);
//    }
//
//
//    /*
//    测试5：logisticsinfo表
// */
//    @Autowired
//    private LogisticsinfoMapper logisticsinfoMapper;
//
//    @Test
//    public void testInsertLogisticsinfo(){
//        Logisticsinfo logisticsinfo = new Logisticsinfo(0,2, "物流公司Test", "BT199", "已发货Test");
//        logisticsinfoMapper.insert(logisticsinfo);
//    }
//
//
//    /*
//    测试6：merchant表
// */
//
//    @Autowired
//    private MerchantMapper merchantMapper;
//
//    @Test
//    public void testFindMerchantById(){
//        Merchant merchant = merchantMapper.findById(1);
//        System.out.println(merchant);
//    }
//
//    @Test
//    public void testUpdateMerchant(){
//        merchantMapper.update(new Merchant(2, "商人李浩", "南昌大学Test", "123456", "7778887878556", ""));
//
//    }
//
//
//    /*
//    测试7：orderitem表
// */
//
//    @Autowired
//    private OrderItemMapper orderItemMapper;
//
//    @Test
//    public void testInsertOrderItem(){
//        OrderItem orderItem1 = new OrderItem(0, 2, 3, 4, 799 );
//        OrderItem orderItem2 = new OrderItem(0, 2, 5, 1, 888 );
//
//        orderItemMapper.insert(orderItem1);
//        orderItemMapper.insert(orderItem2);
//    }
//
//
//
//
//    /*
//    测试8：orders表
// */
//
//    @Autowired
//    private OrdersMapper ordersMapper;
//    @Test
//    public void testInsertOrders(){
//        Orders orders = new Orders(0, 2, 200.0,"Test");
//        ordersMapper.insert(orders);
//    }
//
//    @Test
//    public void testFindOrdersById(){
//        Orders orders = ordersMapper.findById(1);
//        System.out.println(orders);
//    }
//
//
//    /*
//    测试9：product表
// */
//
//    @Autowired
//    private ProductMapper productMapper;
//
//    @Test
//    public void testFindProductById(){
//        Product product = productMapper.findById(1);
//        System.out.println(product);
//    }
//
//    @Test
//    public void testfindAllProduct(){
//        List<Product> productList = productMapper.findAll();
//        productList.forEach(System.out::println);
//    }
//
//    @Test
//    public void testInsertProduct(){
//        Product product = new Product(0, 1, 1, "Test", "Test", 255.5);
//        productMapper.insert(product);
//    }
//
//    /*
//    测试10：productcategory表
// */
//
//    @Autowired
//    private ProductCategoryMapper productCategoryMapper;
//
//    @Test
//    public void testFindProductCategoryById(){
//        ProductCategory productCategory = productCategoryMapper.findById(1);
//        System.out.println(productCategory);
//    }
//
//    @Test
//    public void testfindAllCategory(){
//        List<ProductCategory> productCategoryList = productCategoryMapper.findAll();
//        productCategoryList.forEach(System.out::println);
//    }
//
//
//    /*
//    测试11：productimage表
// */
//
//    @Autowired
//    private ProductImageMapper productImageMapper;
//    @Test
//    public void testInsertProductImage(){
//        ProductImage productImage = new ProductImage(0, 2, "Test");
//        productImageMapper.insert(productImage);
//    }
//
//    @Test
//    public void testFindProductImageById(){
//        ProductImage productImage = productImageMapper.findById(1);
//        System.out.println(productImage);
//    }
//
//    @Test
//    public void testDeleteImageById(){
//        productImageMapper.deleteById(100);
//    }
//
//
//    /*
//    测试12：returnexchangerequest表
// */
//
//    @Autowired
//    private ReturnExchangeRequestMapper returnExchangeRequestMapper;
//    @Test
//    public void testInsertReturnExchangeRequest(){
//        ReturnExchangeRequest returnExchangeRequest = new ReturnExchangeRequest(0, 3, 2, "Test", "Test", 0);
//        returnExchangeRequestMapper.insert(returnExchangeRequest);
//    }
//
//    /*
//    测试13：review表
// */
//
//    @Autowired
//    private ReviewMapper reviewMapper;
//
//    //注：此处rating的长度为4,小数点为2时，可以接受50.2和50.333(会截断),但设置长度为3时，则运行错误。
//    @Test
//    public void testInsertReview(){
//        Review review = new Review(0, 2, 1, 50.333, "Test");
//        reviewMapper.insert(review);
//    }
//
//
//    @Test
//    public void testFindReviewById(){
//        Review review = reviewMapper.findById(3);
//        System.out.println(review);
//    }
//
//
//
//
//}
//
//
//
