package com.IMapp.mappers;


/**
 * @author funmiayinde
 **/
public class PairMapper<K, V> {

    private K k;
    private V v;

    public PairMapper(K k, V v) {
        this.k = k;
        this.v = v;
    }

    public static <T, K> PairMapper<T, K> newPair(T n, K v) {
        return new PairMapper<>(n, v);
    }

    public K getK() {
        return k;
    }

    public V getV() {
        return v;
    }

    @Override
    public int hashCode() {
        int result = k != null ? k.hashCode() : 0;
        result = 31 * result + (v != null ? v.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof PairMapper))
            return false;

        // map the object to pair mapper
        PairMapper pairMapper = (PairMapper) o;

        if (k != null ? !k.equals(pairMapper.k) :pairMapper.k !=null)
            return false;
        if (v !=null ? !v.equals(pairMapper.v) : pairMapper.v != null)
            return false;

        return true;
    }

    @Override
    public String toString() {
        return new StringBuilder("(")
                .append(k)
                .append('=')
                .append(v)
                .append(")").toString();
    }
}
