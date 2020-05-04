package ru.javaops.masterjava;

import com.google.common.collect.ForwardingList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DecoratorSample {

    public static class AddLoggingList<E> extends ForwardingList<E> {

        private final List<E> delegate = new ArrayList<>();

        @Override
        protected List<E> delegate() {
            return delegate;
        }

        @Override
        public boolean add(E element) {
            System.out.println("Add element " +  element.toString());
            return super.add(element);
        }

        @Override
        public boolean addAll(Collection<? extends E> collection) {
            System.out.println("AddAll");
            return super.addAll(collection);
        }


    }

    public static void main(String[] args) {
        AddLoggingList<String> list = new AddLoggingList<>();
        list.addAll(List.of("Elem3"));
        list.add("NewElement1");
        list.add("NewElement2");
        System.out.println("The end");
    }
}
