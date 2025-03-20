package com.sumain.common.entity;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {

    private long total;
    private long size;
    private long current;
    private List<T> records;
}
