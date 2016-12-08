package com.sbiger.qbe;

import com.sbiger.qbe.specification.*;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sbiger on 2016-11-24.
 */
public class Example<T> implements ExampleQuery, ExampleCriteria {
    private List<JoinClass> joinClassList;
    private List<AbstractSpecification> orClassList;
    private List<AbstractSpecification> andClassList;

    public Example(){
        this.joinClassList = new ArrayList<JoinClass>();
        this.orClassList = new ArrayList<AbstractSpecification>();
        this.andClassList = new ArrayList<AbstractSpecification>();
    }

    public static ExampleQuery create(){
        return new Example();
    }

    @Override
    public Example<T> join(Class c) {
        return innerJoin(c);
    }

    @Override
    public Example<T> innerJoin(Class c) {
        return join(c,JoinType.INNER);
    }

    @Override
    public Example<T> leftJoin(Class c) {
        return join(c, JoinType.LEFT);
    }

    @Override
    public Example<T> rightJoin(Class c) {
        return join(c, JoinType.RIGHT);
    }

    @Override
    public Example<T> join(Class c, JoinType type) {
        joinClassList.add(new JoinClass(c, type));
        return this;
    }

    @Override
    public ExampleCriteria<T> or() {
        return this;
    }

    @Override
    public ExampleCriteria equal(BooleanOperator type, Boolean condition, String property, Object value) {
        if (condition) {
            if (type.equals(BooleanOperator.OR)) {
                orClassList.add(new EqualSpecification(type, property, value));
            } else {
                andClassList.add(new EqualSpecification(type, property, value));
            }
        }
        return this;
    }
    @Override
    public ExampleCriteria andEqual(Boolean condition, String property, Object value) {
        equal(BooleanOperator.AND, condition, property, value);
        return this;
    }

    @Override
    public ExampleCriteria orEqual(Boolean condition, String property, Object value) {
        equal(BooleanOperator.OR, condition, property, value);
        return this;
    }

    @Override
    public ExampleCriteria andEqual(String property, Object value) {
        equal(BooleanOperator.AND, true, property, value);
        return this;
    }

    @Override
    public ExampleCriteria orEqual(String property, Object value) {
        equal(BooleanOperator.OR, true, property, value);
        return this;
    }

    @Override
    public ExampleCriteria notEqual(BooleanOperator type, Boolean condition, String property, Object value) {
        if (condition) {
            if (type.equals(BooleanOperator.OR)) {
                orClassList.add(new NotEqualSpecification(type, property, value));
            } else {
                andClassList.add(new NotEqualSpecification(type, property, value));
            }
        }
        return this;
    }

    @Override
    public ExampleCriteria andNotEqual(Boolean condition, String property, Object value) {
        return notEqual(BooleanOperator.AND, condition, property, value);
    }

    @Override
    public ExampleCriteria orNotEqual(Boolean condition, String property, Object value) {
        return notEqual(BooleanOperator.OR, condition, property, value);
    }

    @Override
    public ExampleCriteria andNotEqual(String property, Object value) {
        return notEqual(BooleanOperator.AND, true, property, value);
    }

    @Override
    public ExampleCriteria orNotEqual(String property, Object value) {
        return notEqual(BooleanOperator.OR, true, property, value);
    }

    @Override
    public ExampleCriteria like(BooleanOperator type, Boolean condition, String property, String value) {
        if (condition) {
            if (type.equals(BooleanOperator.OR)) {
                orClassList.add(new LikeSpecification(type, property, value));
            } else {
                andClassList.add(new LikeSpecification(type, property, value));
            }
        }
        return this;
    }

    @Override
    public ExampleCriteria andLike(BooleanOperator type, Boolean condition, String property, String value) {
        return like(BooleanOperator.AND, condition, property, value);
    }

    @Override
    public ExampleCriteria orLike(BooleanOperator type, Boolean condition, String property, String value) {
        return like(BooleanOperator.OR, condition, property, value);
    }

    @Override
    public ExampleCriteria andLike(BooleanOperator type, String property, String value) {
        return like(BooleanOperator.AND, true, property, value);
    }

    @Override
    public ExampleCriteria orLike(BooleanOperator type, String property, String value) {
        return like(BooleanOperator.OR, true, property, value);
    }

    @Override
    public ExampleCriteria notLike(BooleanOperator type, Boolean condition, String property, String value) {
        if (condition) {
            if (type.equals(BooleanOperator.OR)) {
                orClassList.add(new NotLikeSpecification(type, property, value));
            } else {
                andClassList.add(new NotLikeSpecification(type, property, value));
            }
        }
        return this;
    }

    @Override
    public ExampleCriteria andNotLike(BooleanOperator type, Boolean condition, String property, String value) {
        return notLike(BooleanOperator.AND, condition, property, value);
    }

    @Override
    public ExampleCriteria orNotLike(BooleanOperator type, Boolean condition, String property, String value) {
        return notLike(BooleanOperator.OR, condition, property, value);
    }

    @Override
    public ExampleCriteria andNotLike(BooleanOperator type, String property, String value) {
        return notLike(BooleanOperator.AND, true, property, value);
    }

    @Override
    public ExampleCriteria orNotLike(BooleanOperator type, String property, String value) {
        return notLike(BooleanOperator.OR, true, property, value);
    }

    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder cb) {
        From from = root;
        for (JoinClass c : joinClassList) {
            from = from.join(String.valueOf(c.getClassName()), c.getType());
        }

        final From finalFrom = from;
        Predicate[] ands = this.andClassList.stream()
                .map(spec -> spec.toPredicate(finalFrom, query, cb))
                .toArray(Predicate[]::new);

        Predicate[] ors = this.orClassList.stream()
                .map(spec -> spec.toPredicate(finalFrom, query, cb))
                .toArray(Predicate[]::new);

        return cb.or(cb.and(ands), cb.or(ors));
    }

    class JoinClass{
        JoinType type;
        String classname;
        public JoinClass(Class c, JoinType type) {
            this.type = type;
            this.classname = c.getName();
        }
        public JoinType getType() {return type;}
        public String getClassName() { return classname; }
    }
}
