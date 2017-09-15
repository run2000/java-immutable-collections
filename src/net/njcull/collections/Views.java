package net.njcull.collections;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Predicate;

/**
 * Immutable views backing onto another collection type.
 *
 * @author run2000
 * @version 10/01/2016.
 */
public final class Views {

    private Views() {
    }

    public static <E> ArrayBackedSet<E> setView(List<E> list) {
        return new SetView<>(list);
    }

    public static <E> List<E> listView(ArrayBackedSet<E> coll) {
        return new ListView<E>(coll);
    }

    public static <E> ArrayBackedCollection<E> collectionView(List<E> list) {
        return new CollectionView<>(list);
    }

    private static final class SetView<E> extends AbstractSet<E>
            implements ArrayBackedSet<E>, Serializable {
        private final List<E> m_List;

        private static final long serialVersionUID = -5508753976144187934L;

        SetView(List<E> list) {
            this.m_List = Objects.requireNonNull(list, "list must be non-null");
        }

        @Override
        public void clear() {
            m_List.clear();
        }

        @Override
        public boolean add(E e) {
            return m_List.add(e);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return m_List.addAll(c);
        }

        @Override
        public boolean contains(Object o) {
            return m_List.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return m_List.containsAll(c);
        }

        @Override
        public boolean isEmpty() {
            return m_List.isEmpty();
        }

        public E getAtIndex(int index) {
            return m_List.get(index);
        }

        public int indexOf(E element) {
            return m_List.indexOf(element);
        }

        @Override
        public int indexOfRange(E element, int fromIndex, int toIndex) {
            if(fromIndex < 0 || fromIndex >= size()) {
                throw new IndexOutOfBoundsException("fromIndex: "+ fromIndex);
            }
            if(toIndex < fromIndex || toIndex > size()) {
                throw new IndexOutOfBoundsException("toIndex: " + toIndex);
            }
            if(element == null) {
                for (int i = fromIndex; i < toIndex; i++) {
                    if(element == m_List.get(i)) {
                        return i;
                    }
                }
            } else {
                for (int i = fromIndex; i < toIndex; i++) {
                    if(element.equals(m_List.get(i))) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override
        public int size() {
            return m_List.size();
        }

        @Override
        public Iterator<E> iterator() {
            return m_List.iterator();
        }

        @Override
        public boolean remove(Object o) {
            return m_List.remove(o);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return m_List.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return m_List.retainAll(c);
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            return m_List.removeIf(filter);
        }

        @Override
        public Object[] toArray() {
            return m_List.toArray();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a) {
            return m_List.<T>toArray(a);
        }

        @Override
        public Spliterator<E> spliterator() {
            return m_List.spliterator();
        }

        @Override
        public String toString() {
            return m_List.toString();
        }

        @Override
        public int hashCode() {
            return ArrayBackedSet.hashCode(this);
        }

        public List<E> asList() {
            return m_List;
        }

        /**
         * Deserialization.
         */
        private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
            stream.defaultReadObject();

            // Perform validation
            if (m_List == null) {
                throw new InvalidObjectException("backing list must not be null");
            }
        }
    }

    private static final class ListView<E> extends AbstractRandomAccessList<E>
            implements ArrayBackedCollection<E>, Serializable {
        private final ArrayBackedSet<E> m_Coll;
        private final int m_StartIndex;
        private final int m_EndIndex;

        // Serialization
        private static final long serialVersionUID = -8124463789970217417L;

        ListView(ArrayBackedSet<E> coll) {
            this.m_Coll = Objects.requireNonNull(coll);
            this.m_StartIndex = 0;
            this.m_EndIndex = coll.size();
        }

        ListView(ArrayBackedSet<E> coll, int startIndex, int endIndex) {
            this.m_Coll = Objects.requireNonNull(coll);
            this.m_StartIndex = startIndex;
            this.m_EndIndex = endIndex;

            if((startIndex < 0) || (startIndex >= coll.size())) {
                throw new IllegalArgumentException("start index: " + startIndex);
            }
            if((endIndex < startIndex) || (endIndex > coll.size())) {
                throw new IllegalArgumentException("end index: " + endIndex);
            }
        }

        @Override
        public List<E> subList(int fromIndex, int toIndex) {
            if ((fromIndex < 0) || (fromIndex >= (m_EndIndex - m_StartIndex))) {
                throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
            }
            if ((toIndex < fromIndex) || (toIndex > (m_EndIndex - m_StartIndex))) {
                throw new IndexOutOfBoundsException("toIndex = " + toIndex);
            }
            if(fromIndex == toIndex) {
                return Collections.<E>emptyList();
            }
            return new ListView<E>(m_Coll, m_StartIndex + fromIndex, m_StartIndex + toIndex);
        }

        @Override
        public E get(int index) {
            if((index < 0) || (index >= (m_EndIndex - m_StartIndex))) {
                throw new IndexOutOfBoundsException("index out of bounds");
            }
            return m_Coll.getAtIndex(m_StartIndex + index);
        }

        @SuppressWarnings("unchecked")
        @Override
        public int indexOf(Object o) {
            try {
                int idx = m_Coll.indexOfRange((E) o, m_StartIndex, m_EndIndex);
                return idx >= 0 ? (idx - m_StartIndex) : -1;
            } catch (ClassCastException e) {
                return -1;
            }
        }

        @Override
        public int size() {
            return m_EndIndex - m_StartIndex;
        }

        @Override
        public boolean isEmpty() {
            return (m_EndIndex - m_StartIndex) == 0;
        }

        @Override
        public Spliterator<E> spliterator() {
            return new ImmutableIndexerSpliterator<>(this::get, size(), Spliterator.DISTINCT);
        }

        @Override
        public E getAtIndex(int index) {
            if((index < 0) || (index >= (m_EndIndex - m_StartIndex))) {
                throw new IndexOutOfBoundsException("index out of bounds");
            }
            return m_Coll.getAtIndex(m_StartIndex + index);
        }

        @Override
        public int indexOfRange(E element, int fromIndex, int toIndex) {
            final int size = size();
            if(fromIndex < 0 || fromIndex >= size) {
                throw new IndexOutOfBoundsException("fromIndex: "+ fromIndex);
            }
            if(toIndex < fromIndex || toIndex > size) {
                throw new IndexOutOfBoundsException("toIndex: " + toIndex);
            }
            if(element == null) {
                for (int i = fromIndex; i < toIndex; i++) {
                    if(element == m_Coll.getAtIndex(m_StartIndex + i)) {
                        return i;
                    }
                }
            } else {
                for (int i = fromIndex; i < toIndex; i++) {
                    if(element.equals(m_Coll.getAtIndex(m_StartIndex + i))) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override
        public List<E> asList() {
            return this;
        }

        /**
         * Deserialization.
         */
        private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
            stream.defaultReadObject();

            // Perform validation
            if (m_Coll == null) {
                throw new InvalidObjectException("collection must not be null");
            }
            if((m_StartIndex < 0) || (m_StartIndex >= m_Coll.size())) {
                throw new InvalidObjectException("start index is out of bounds: " + m_StartIndex);
            }
            if((m_EndIndex < m_StartIndex) || (m_EndIndex > m_Coll.size())) {
                throw new IllegalArgumentException("end index is out of bounds: " + m_EndIndex);
            }
        }
    }

    private static final class CollectionView<E> extends AbstractCollection<E>
            implements ArrayBackedCollection<E>, Serializable {
        private final List<E> m_List;

        // Serialization
        private static final long serialVersionUID = -5876575847029641360L;

        CollectionView(List<E> list) {
            this.m_List = Objects.requireNonNull(list, "list must be non-null");
        }

        @Override
        public void clear() {
            m_List.clear();
        }

        @Override
        public boolean add(E e) {
            return m_List.add(e);
        }

        @Override
        public boolean addAll(Collection<? extends E> c) {
            return m_List.addAll(c);
        }

        @Override
        public boolean contains(Object o) {
            return m_List.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return m_List.containsAll(c);
        }

        @Override
        public boolean isEmpty() {
            return m_List.isEmpty();
        }

        @Override
        public int size() {
            return m_List.size();
        }

        @Override
        public Iterator<E> iterator() {
            return m_List.iterator();
        }

        @Override
        public boolean remove(Object o) {
            return m_List.remove(o);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return m_List.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return m_List.retainAll(c);
        }

        @Override
        public boolean removeIf(Predicate<? super E> filter) {
            return m_List.removeIf(filter);
        }

        @Override
        public Object[] toArray() {
            return m_List.toArray();
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T[] toArray(T[] a) {
            return m_List.<T>toArray(a);
        }

        @Override
        public Spliterator<E> spliterator() {
            return m_List.spliterator();
        }

        @Override
        public String toString() {
            return m_List.toString();
        }

        @Override
        public E getAtIndex(int index) {
            return m_List.get(index);
        }

        @Override
        public int indexOf(E element) {
            return m_List.indexOf(element);
        }

        @Override
        public int indexOfRange(E element, int fromIndex, int toIndex) {
            if(fromIndex < 0 || fromIndex >= size()) {
                throw new IndexOutOfBoundsException("fromIndex: "+ fromIndex);
            }
            if(toIndex < fromIndex || toIndex > size()) {
                throw new IndexOutOfBoundsException("toIndex: " + toIndex);
            }
            if(element == null) {
                for (int i = fromIndex; i < toIndex; i++) {
                    if(element == m_List.get(i)) {
                        return i;
                    }
                }
            } else {
                for (int i = fromIndex; i < toIndex; i++) {
                    if(element.equals(m_List.get(i))) {
                        return i;
                    }
                }
            }
            return -1;
        }

        @Override
        public List<E> asList() {
            return m_List;
        }

        /**
         * Deserialization.
         */
        private void readObject(ObjectInputStream stream) throws ClassNotFoundException, IOException {
            stream.defaultReadObject();

            // Perform validation
            if (m_List == null) {
                throw new InvalidObjectException("list cannot be null");
            }
        }
    }
}
