package com.nju.edu.seckill.exception;

/**
 * SeckillCloseException
 *
 * @blame gao xiang
 */
public class SeckillCloseException extends SeckillException {
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
