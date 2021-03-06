package shake_n_bacon;

import providedCode.*;

/**
 * @author <name>
 * @UWNetID <uw net id>
 * @studentID <id number>
 * @email <email address>
 * 
 *        TODO: Replace this comment with your own as appropriate.
 * 
 *        1. You may implement HashTable with open addressing discussed in
 *        class; You can choose one of those three: linear probing, quadratic
 *        probing or double hashing. The only restriction is that it should not
 *        restrict the size of the input domain (i.e., it must accept any key)
 *        or the number of inputs (i.e., it must grow as necessary).
 * 
 *        2. Your HashTable should rehash as appropriate (use load factor as
 *        shown in the class).
 * 
 *        3. To use your HashTable for WordCount, you will need to be able to
 *        hash strings. Implement your own hashing strategy using charAt and
 *        length. Do NOT use Java's hashCode method.
 * 
 *        4. HashTable should be able to grow at least up to 200,000. We are not
 *        going to test input size over 200,000 so you can stop resizing there
 *        (of course, you can make it grow even larger but it is not necessary).
 * 
 *        5. We suggest you to hard code the prime numbers. You can use this
 *        list: http://primes.utm.edu/lists/small/100000.txt NOTE: Make sure you
 *        only hard code the prime numbers that are going to be used. Do NOT
 *        copy the whole list!
 * 
 *        TODO: Develop appropriate tests for your HashTable.
 */
public class HashTable_OA extends DataCounter {
	private DataCount[] table;
	private int primeIndex;
	private int[] primes = {101, 199, 401, 809, 1601, 3203, 6473,
			12043, 25037, 51001, 100057, 200003};
	private Comparator<String> c;
	private Hasher h;
	private int size;


	public HashTable_OA(Comparator<String> c, Hasher h) {
		this.table = new DataCount[primes[primeIndex]];
		this.c = c;
		this.h = h;
	}

	@Override
	public void incCount(String data) {
		int index = findPos(data, table);

		if (table[index] == null) {
			table[index] = new DataCount(data, 1);
			size++;
		} else {
			table[index].count++;
		}

		if ((double) size > (double) table.length / 2.0)
			rehash();
	}


	@Override
	public int getSize() {
		return size;
	}

	@Override
	public int getCount(String data) {
		int index = findPos(data, table);
		if (table[index] == null)
			return 0;
		return table[index].count;
	}

	@Override
	public SimpleIterator getIterator() {
		SimpleIterator itr = new SimpleIterator() {
			private int index = -1;
			private int foundElements;

			/*
			 * Returns the next available DataCount object.
			 * @return the next available DataCount object in the hashtable.
			 */
			@Override
			public DataCount next() {
				if (!hasNext())
					return null;

				index++;
				while(table[index] == null)
					index++;
				foundElements++;
				return table[index];
			}

			/*
			 * Returns whether or not there exists another DataCount object.
			 * @return returns a boolean of whether or not there exists another
			 * DataCount object in the hashtable.
			 */
			@Override
			public boolean hasNext() {
				return foundElements < size;
			}
		};
		return itr;
	}

	private void rehash() {
		primeIndex++;
		DataCount[] biggerTable = new DataCount[primes[primeIndex]];

		for (int i = 0; i < table.length; i++) {
			if (table[i] != null) {
				DataCount dataCount = table[i];
				int index = findPos(dataCount.data, biggerTable);
				biggerTable[index] = dataCount;
			}
		}
		table = biggerTable;
	}

	private int findPos(String data, DataCount[] array) {
		int i = 0;
		int index = h.hash(data) % array.length;
		int tempIndex = -1;

		while(true) {
			tempIndex = (int) (index + Math.pow(i, 2));
			if( tempIndex >= array.length )
				tempIndex -= array.length;
			DataCount dataCount = array[tempIndex];
			if (dataCount == null) {
				return tempIndex;
			} else if (c.compare(dataCount.data, data) == 0) {
				return tempIndex;
			}
			i++;
		}
	}
}