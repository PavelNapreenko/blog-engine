package ru.pnapreenko.blogengine.api.utils;

import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@EqualsAndHashCode(of = {"offset", "limit", "sort"}, callSuper = false)
public class MainPagePostsOffset extends MainPageOffset {

    private final int offset;
    private final int limit;
    private final Sort sort;

    public MainPagePostsOffset(int offset, int limit, Sort sort) {
        if (offset < 0) {
            throw new IllegalArgumentException("Значение offset не может быть меньше 0!");
        }

        if (limit < 1) {
            throw new IllegalArgumentException("Значение limit не может быть меньше 1!");
        }

        this.limit = limit;
        this.offset = offset;
        this.sort = sort;
    }

    public MainPagePostsOffset(int offset, int limit, Sort.Direction direction, String... properties) {
        this(offset, limit, Sort.by(direction, properties));
    }

    public MainPagePostsOffset(int offset, int limit) {
        this(offset, limit, Sort.by(Sort.Direction.ASC,"id"));
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @Override
    public Sort getSort() {
        return sort;
    }

    @Override
    public Pageable next() {
        return new MainPagePostsOffset((int) (getOffset() + getPageSize()), getPageSize(), getSort());
    }

    public MainPagePostsOffset previous() {
        return hasPrevious() ? new MainPagePostsOffset((int) (getOffset() - getPageSize()), getPageSize(), getSort()) : this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new MainPagePostsOffset(0, getPageSize(), getSort());
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
