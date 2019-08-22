package com.nju.edu.seckill.common;

/**
 * Exposer
 *
 * @blame gao xiang
 */
public class Exposer {

    /**
     * 是否开启秒杀
     */
    private boolean exposed;

    /**
     * 防止用户通过抓包拿到秒杀地址
     */
    private String md5;

    /**
     * 秒杀商品id
     */
    private long id;

    /**
     * 系统当前时间
     */
    private long now;

    /**
     * 秒杀开始时间
     */
    private long start;
    /**
     * 秒杀结束时间
     */
    private long end;

    /**
     * 秒杀进行中
     * @param exposed
     * @param md5
     * @param id
     */
    public Exposer(boolean exposed, String md5, long id) {
        this.exposed = exposed;
        this.md5 = md5;
        this.id = id;
    }

    /**
     * 秒杀未开始
     * @param exposed
     * @param id
     * @param now
     * @param start
     * @param end
     */
    public Exposer(boolean exposed, long id, long now, long start, long end) {
        this.exposed = exposed;
        this.id = id;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    /**
     * 秒杀结束
     * @param exposed
     * @param id
     */
    public Exposer(boolean exposed, long id) {
        this.exposed = exposed;
        this.id = id;
    }
}
