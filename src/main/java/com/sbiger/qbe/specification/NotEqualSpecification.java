package com.sbiger.qbe.specification;

import com.sbiger.qbe.ExampleCriteria;

import javax.persistence.criteria.*;

/**
 * Created by sbiger on 2016-12-06.
 */
public class NotEqualSpecification extends AbstractSpecification {
    private final String property;

    private final Object value;

    private final ExampleCriteria.BooleanOperator type;

    public NotEqualSpecification(ExampleCriteria.BooleanOperator type, String property, Object value){
        this.property = property;
        this.value = value;
        this.type = type;
    }

    private Predicate getPredicate(From root, CriteriaBuilder cb, Object value, String field) {
        return null == value ? cb.isNull(root.get(field)) : cb.notEqual(root.get(field), value);
    }

    @Override
    public Predicate toPredicate(From root, CriteriaQuery query, CriteriaBuilder cb) {
        return getPredicate(root, cb, value, property);
    }
}