/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice.exceptions;

public class IllegalStatusArgument  extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public IllegalStatusArgument(final String message)
    {
        super(message);
    }
}
