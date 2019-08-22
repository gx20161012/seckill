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

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", id=" + id +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
