package com.skillup.domain.promotionSql;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class PromotionService {

    // 此时这个promotionRepository 和 stockOperation 虽然地址不同但是，接口所指向的都是同一个实例 JOOQPromotionRepo
    @Autowired
    PromotionRepository promotionRepository;

    // 在业务上代码层面即使使用悲观锁也无法解决高并发的问题
    // 我们需要使用优化乐观锁，在数据库层面解决这个问题
    @Resource(name = "${promotion.stock-strategy}")
    StockOperation stockOperation;

    public PromotionDomain registry(PromotionDomain promotionDomain) {
        promotionRepository.createPromotion(promotionDomain);
        return promotionDomain;
    }

    public PromotionDomain getPromotionById(String promotionId) {
        return promotionRepository.getPromotionById(promotionId);
    }

    public List<PromotionDomain> getPromotionByStatus(Integer promotionStatus) {
        return promotionRepository.getPromotionByStatus(promotionStatus);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean lockStock(String id) {
        return stockOperation.lockStock(id);
    }

    public boolean deductStock(String id) {
        return stockOperation.deductStock(id);
    }

    public boolean revertStock(String id) {
        return stockOperation.revertStock(id);
    }
}
