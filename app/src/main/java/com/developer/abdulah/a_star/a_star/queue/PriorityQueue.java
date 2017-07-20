package com.developer.abdulah.a_star.a_star.queue;

import android.support.annotation.NonNull;

import java.util.TreeSet;

import static com.developer.abdulah.a_star.util.U.print;

/**
 * Created by dev on 7/18/17.
 */

public class PriorityQueue<T> {

    public static final String TAG = "@PriorityQueue:";

    private static class Pair<T, K extends Comparable> implements Comparable {

        static final String TAG = "@Pair:";

        T item;
        K priority;

        Pair(T t, K k) {
            this.item = t;
            this.priority = k;
        }

        @Override
        public int compareTo(@NonNull Object o) {
            if (o instanceof Pair) {
                Pair p = (Pair) o;
                return priority.compareTo(p.priority);
            }
            print(TAG, "WARNING: object is not a Pair Object");
            return 1;
        }
    }

    private TreeSet<Pair<T, Double>> raw;

    public PriorityQueue() {
        this.raw = new TreeSet<>();
    }

    public boolean isEmpty() {
        return raw.isEmpty();
    }

    public void add(T elem, double priority) {
        raw.add(new Pair<>(elem, priority));
    }

    public T pop() {
        Pair<T, Double> first = raw.first();
        raw.remove(first);
        return first.item;
    }


    public static void main(String[] args) {
        PriorityQueue<String> queue = new PriorityQueue<>();

        queue.add("Abdullah", 100);
        queue.add("Tahmina", 99.9999);
        queue.add("Tahmina2", 1.2342);

        while (!queue.isEmpty()) {
            print(queue.pop());
        }

    }
}
