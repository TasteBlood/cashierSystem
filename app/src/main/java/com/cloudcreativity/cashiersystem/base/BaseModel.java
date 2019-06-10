package com.cloudcreativity.cashiersystem.base;

import android.databinding.ViewDataBinding;

/**
 *
 * @param <A> 继承自Activity
 * @param <B> 继承自ViewDataBinding
 */
public abstract class BaseModel<A,B extends ViewDataBinding> {
    protected A context;
    protected B binding;

    public BaseModel(A context, B binding) {
        this.context = context;
        this.binding = binding;
    }
}
