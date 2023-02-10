package com.skillup.infrastructure.repolmpl;

public interface DomainRecord<D, R> {
    public D toDomain(R r);
    public R toRecord(D d);
}
