/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq;


import com.skillup.infrastructure.jooq.tables.Orders;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling indexes of tables in order-app-1.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index ORDERS_IDX_USER_ID = Internal.createIndex(DSL.name("idx_user_id"), Orders.ORDERS, new OrderField[] { Orders.ORDERS.USER_ID }, false);
}
