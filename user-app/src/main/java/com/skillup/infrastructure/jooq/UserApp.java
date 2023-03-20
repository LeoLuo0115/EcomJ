/*
 * This file is generated by jOOQ.
 */
package com.skillup.infrastructure.jooq;


import com.skillup.infrastructure.jooq.tables.User;

import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class UserApp extends SchemaImpl {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>user-app</code>
     */
    public static final UserApp USER_APP = new UserApp();

    /**
     * The table <code>user-app.user</code>.
     */
    public final User USER = User.USER;

    /**
     * No further instances allowed
     */
    private UserApp() {
        super("user-app", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            User.USER);
    }
}
