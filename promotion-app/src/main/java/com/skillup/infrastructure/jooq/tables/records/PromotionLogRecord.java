/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq.tables.records;


import com.skillup.infrastructure.jooq.tables.PromotionLog;

import java.time.LocalDateTime;

import org.jooq.Field;
import org.jooq.Record2;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PromotionLogRecord extends UpdatableRecordImpl<PromotionLogRecord> implements Record5<Long, String, String, String, LocalDateTime> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>promotion-app.promotion_log.order_number</code>.
     */
    public void setOrderNumber(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>promotion-app.promotion_log.order_number</code>.
     */
    public Long getOrderNumber() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>promotion-app.promotion_log.user_id</code>.
     */
    public void setUserId(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>promotion-app.promotion_log.user_id</code>.
     */
    public String getUserId() {
        return (String) get(1);
    }

    /**
     * Setter for <code>promotion-app.promotion_log.promotion_id</code>.
     */
    public void setPromotionId(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>promotion-app.promotion_log.promotion_id</code>.
     */
    public String getPromotionId() {
        return (String) get(2);
    }

    /**
     * Setter for <code>promotion-app.promotion_log.operation_name</code>.
     */
    public void setOperationName(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>promotion-app.promotion_log.operation_name</code>.
     */
    public String getOperationName() {
        return (String) get(3);
    }

    /**
     * Setter for <code>promotion-app.promotion_log.create_time</code>.
     */
    public void setCreateTime(LocalDateTime value) {
        set(4, value);
    }

    /**
     * Getter for <code>promotion-app.promotion_log.create_time</code>.
     */
    public LocalDateTime getCreateTime() {
        return (LocalDateTime) get(4);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record2<Long, String> key() {
        return (Record2) super.key();
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row5<Long, String, String, String, LocalDateTime> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Long, String, String, String, LocalDateTime> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return PromotionLog.PROMOTION_LOG.ORDER_NUMBER;
    }

    @Override
    public Field<String> field2() {
        return PromotionLog.PROMOTION_LOG.USER_ID;
    }

    @Override
    public Field<String> field3() {
        return PromotionLog.PROMOTION_LOG.PROMOTION_ID;
    }

    @Override
    public Field<String> field4() {
        return PromotionLog.PROMOTION_LOG.OPERATION_NAME;
    }

    @Override
    public Field<LocalDateTime> field5() {
        return PromotionLog.PROMOTION_LOG.CREATE_TIME;
    }

    @Override
    public Long component1() {
        return getOrderNumber();
    }

    @Override
    public String component2() {
        return getUserId();
    }

    @Override
    public String component3() {
        return getPromotionId();
    }

    @Override
    public String component4() {
        return getOperationName();
    }

    @Override
    public LocalDateTime component5() {
        return getCreateTime();
    }

    @Override
    public Long value1() {
        return getOrderNumber();
    }

    @Override
    public String value2() {
        return getUserId();
    }

    @Override
    public String value3() {
        return getPromotionId();
    }

    @Override
    public String value4() {
        return getOperationName();
    }

    @Override
    public LocalDateTime value5() {
        return getCreateTime();
    }

    @Override
    public PromotionLogRecord value1(Long value) {
        setOrderNumber(value);
        return this;
    }

    @Override
    public PromotionLogRecord value2(String value) {
        setUserId(value);
        return this;
    }

    @Override
    public PromotionLogRecord value3(String value) {
        setPromotionId(value);
        return this;
    }

    @Override
    public PromotionLogRecord value4(String value) {
        setOperationName(value);
        return this;
    }

    @Override
    public PromotionLogRecord value5(LocalDateTime value) {
        setCreateTime(value);
        return this;
    }

    @Override
    public PromotionLogRecord values(Long value1, String value2, String value3, String value4, LocalDateTime value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PromotionLogRecord
     */
    public PromotionLogRecord() {
        super(PromotionLog.PROMOTION_LOG);
    }

    /**
     * Create a detached, initialised PromotionLogRecord
     */
    public PromotionLogRecord(Long orderNumber, String userId, String promotionId, String operationName, LocalDateTime createTime) {
        super(PromotionLog.PROMOTION_LOG);

        setOrderNumber(orderNumber);
        setUserId(userId);
        setPromotionId(promotionId);
        setOperationName(operationName);
        setCreateTime(createTime);
    }
}
