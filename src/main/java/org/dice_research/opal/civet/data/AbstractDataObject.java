package org.dice_research.opal.civet.data;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.dice_research.opal.civet.exceptions.ParsingException;

/**
 * Abstract {@link DataObject}.
 * 
 * @author Adrian Wilke
 */
public abstract class AbstractDataObject<TYPE> implements DataObject<TYPE> {

	private String id;
	private List<TYPE> values;

	/**
	 * Creates a new data-object of same type as given source and with same ID as
	 * given source.
	 * 
	 * @param dataObject Source data-object
	 * @throws IOException          If type of source data-object is unknown.
	 * @throws NullPointerException if the one of given IDs is null.
	 */
	public static DataObject<?> create(DataObject<?> dataObject) throws IOException, NullPointerException {
		if (dataObject.getType().equals(String.class)) {
			return new StringDataObject(dataObject.getId());
		} else if (dataObject.getType().equals(Integer.class)) {
			return new IntegerDataObject(dataObject.getId());
		} else {
			throw new IOException("Unknown type: " + dataObject.getType());
		}
	}

	/**
	 * Sets id. Creates empty list of values.
	 * 
	 * @throws NullPointerException if the given ID is null.
	 */
	public AbstractDataObject(String id) throws NullPointerException {
		if (id == null) {
			throw new NullPointerException("Data object ID is null");
		} else {
			this.id = id;
			this.values = new LinkedList<TYPE>();
		}
	}

	/**
	 * Sets id and sets value.
	 * 
	 * @throws NullPointerException if the given ID or the given value is null.
	 */
	public AbstractDataObject(String id, TYPE value) throws NullPointerException {

		// Set ID. Create empty list of values.
		this(id);

		if (value == null) {
			throw new NullPointerException("Data object value is null");
		} else {
			this.values.add(value);
		}
	}

	/**
	 * Sets id and sets list of values.
	 * 
	 * @throws NullPointerException if the given ID or the given list of values is
	 *                              null.
	 */
	public AbstractDataObject(String id, List<TYPE> values) throws NullPointerException {

		// Set ID. Create empty list of values.
		this(id);

		// Replace list of values by given list.
		if (values == null) {
			throw new NullPointerException("Data object values is null");
		} else {
			this.values = values;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public String getId() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isEmpty() {
		return values.isEmpty();
	}

	/**
	 * {@inheritDoc}
	 */
	public List<TYPE> getValues() {
		return values;
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> setValue(TYPE value) throws NullPointerException {
		if (value == null) {
			throw new NullPointerException("Data object value is null");
		} else {
			this.values = new LinkedList<TYPE>();
			this.values.add(value);
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> setValues(List<TYPE> values) throws NullPointerException {
		if (values == null) {
			throw new NullPointerException("Data object values is null");
		} else {
			this.values = values;
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> addValue(TYPE value) throws NullPointerException {
		if (value == null) {
			throw new NullPointerException("Data object value is null");
		} else {
			this.values.add(value);
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> addValues(List<TYPE> values) throws NullPointerException {
		if (values == null) {
			throw new NullPointerException("Data object values is null");
		} else {
			this.values.addAll(values);
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> addValuesUnique(DataObject<?> dataObject) throws NullPointerException, ClassCastException {
		if (dataObject == null) {
			throw new NullPointerException("Data object is null");
		} else {
			if (dataObject instanceof IntegerDataObject) {
				for (Integer value : IntegerDataObject.cast(dataObject).getValues()) {
					if (!this.values.contains(value)) {
						IntegerDataObject.cast(this).addValue(value);
					}
				}
			} else if (dataObject instanceof StringDataObject) {
				for (String value : StringDataObject.cast(dataObject).getValues()) {
					if (!this.values.contains(value)) {
						StringDataObject.cast(this).addValue(value);
					}
				}
			} else {
				throw new ClassCastException("Can not cast " + dataObject);
			}
			return this;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public DataObject<TYPE> addValueUnique(String value) throws NullPointerException, ParsingException {
		if (value == null) {
			throw new NullPointerException("Value is null");
		} else {
			if (!this.values.contains(value)) {
				addValue(value);
			}
			return this;
		}
	}

	@Override
	public String toString() {
		return getId() + " [" + values.size() + "]";
	}

}