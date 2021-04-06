@FunctionalInterface
interface TernaryFunction<T1, T2, T3, R> {
    public R apply(T1 first, T2 second, T3 third);
}
