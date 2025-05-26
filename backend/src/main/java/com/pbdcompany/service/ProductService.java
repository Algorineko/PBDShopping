@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    // 查询所有商品
    public List<Product> findAll() {
        return productMapper.findAll();
    }

    // 根据ID删除商品
    public void deleteById(int id) {
        productMapper.deleteById(id);
    }

    // 添加商品
    public void insert(Product product) {
        productMapper.insert(product);
    }

    // 更新商品信息
    public void update(Product product) {
        productMapper.update(product);
    }

    // 根据ID查询商品信息
    public Product findById(int id) {
        return productMapper.findById(id);
    }

    // 根据名称或ID搜索商品（顾客使用）
    public List<ProductResponse> findByNameOrId(String name, int id) {
        return productMapper.findByNameOrId(name, id);
    }

    // 查看商家自己的商品（新增）
    public List<ProductInfoResponse> getProductsByMerchant(int merchantId) {
        return productMapper.findByMerchantId(merchantId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // 修改商品信息（新增）
    public boolean updateProduct(UpdateProductRequest request) {
        Product product = productMapper.findById(request.getProductId());

        if (product == null || product.getMerchantId() != request.getMerchantId()) {
            return false; // 商品不存在或无权限
        }

        if (request.getProductName() != null) {
            product.setProductName(request.getProductName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        productMapper.updateSelective(product);
        return true;
    }

    // 转换 Entity -> Response
    private ProductInfoResponse convertToResponse(Product product) {
        if (product == null) return null;
        ProductInfoResponse response = new ProductInfoResponse();
        BeanUtils.copyProperties(response, product);
        return response;
    }
}
