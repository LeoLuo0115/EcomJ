package com.skillup.infrastructure.repoImplement;


public interface DomainRecord<D, R> {
    public D toDomain(R r);
    public R toRecord(D d);
}
