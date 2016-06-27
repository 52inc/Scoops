package com.ftinc.scoop.internal;

import com.ftinc.scoop.binding.IBinding;

import java.util.List;

/**
 * Created by r0adkll on 6/25/16.
 */

public interface ToppingBinder<T> {
    List<IBinding> bind(T target);
}
