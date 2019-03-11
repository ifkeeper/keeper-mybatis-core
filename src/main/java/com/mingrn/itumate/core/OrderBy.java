package com.mingrn.itumate.core;

/**
 * 排序
 *
 * @author MinGRn <br > MinGRn97@gmail.com
 */
public class OrderBy {
	private StringBuilder sb = new StringBuilder();

	public OrderBy add(String column) {
		return add(column, true);
	}

	public OrderBy add(String column, boolean ascend) {
		if (sb.length() > 0) {
			sb.append(", ");
		}
		sb.append(column).append(ascend ? " ASC" : " DESC");

		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}