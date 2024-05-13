package ru.otus.kudaiberdieva.homework5.iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class Box implements Iterable<String>{
    private final List<String> list1;
    private final List<String> list2;
    private final List<String> list3;
    private final List<String> list4;
    private final Logger logger = LoggerFactory.getLogger(Box.class);


    private class BoxIterator implements Iterator<String> {

        private final Iterator<String> iterator1 = list1.iterator();
        private final Iterator<String> iterator2 = list2.iterator();
        private final Iterator<String> iterator3 = list3.iterator();
        private final Iterator<String> iterator4 = list4.iterator();

        @Override
        public boolean hasNext() {
            return iterator1.hasNext() || iterator2.hasNext() || iterator3.hasNext() || iterator4.hasNext();
        }

        @Override
        public String next() {
            if (iterator1.hasNext()) {
                return iterator1.next();
            } else if (iterator2.hasNext()) {
                return iterator2.next();
            } else if (iterator3.hasNext()) {
                return iterator3.next();
            } else if (iterator4.hasNext()) {
                return iterator4.next();
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public Iterator<String> iterator() {
        logger.info("Getting iterator for the box contents");
        return new BoxIterator();
    }

    public Box(List<String> list1, List<String> list2, List<String> list3, List<String> list4) {
        this.list1 = list1;
        this.list2 = list2;
        this.list3 = list3;
        this.list4 = list4;
    }
}
