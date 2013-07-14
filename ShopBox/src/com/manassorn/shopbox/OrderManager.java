package com.manassorn.shopbox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import com.manassorn.shopbox.value.BillItem;
import com.manassorn.shopbox.value.BillProductItem;
import com.manassorn.shopbox.value.BillSubTotalItem;
import com.manassorn.shopbox.value.BillSupplementItem;
import com.manassorn.shopbox.value.Order;
import com.manassorn.shopbox.value.OrderProduct;
import com.manassorn.shopbox.value.Product;
import com.manassorn.shopbox.value.Supplement;

public class OrderManager {
	private Order order;
	private ArrayList<BillItem> billItems;
	private boolean changed = true;

	public OrderManager(Order order) {
		this.order = order;
		this.billItems = new ArrayList<BillItem>();
	}

	public Order getOrder() {
		return order;
	}

	// public void setOrder(Order order) {
	// this.order = order;
	// }
	public double getTotal() {
		List<BillItem> billItems = getBillItems();
		BillSubTotalItem total = (BillSubTotalItem) billItems.get(billItems.size() - 1);
		return total.getSubTotal();
	}

	public ArrayList<BillItem> getBillItems() {
		if (changed) {
			billItems.clear();
			if (getOrderProductCount() == 0) {
				return billItems;
			}

			double subTotal = 0;
			int seq = 0;
			for (OrderProduct orderProduct : order.getOrderProducts()) {
				BillProductItem billProductItem = new BillProductItem(seq++,
						orderProduct.getProduct(), orderProduct.getAmount());
				billItems.add(billProductItem);
				subTotal += orderProduct.getAmount() * orderProduct.getProduct().getPrice();
			}
			billItems.add(new BillSubTotalItem(seq++, subTotal));

			List<Supplement> supplementList = order.getSupplements();
			MultiTreeMap<Integer, Supplement> supplementMap = createSupplementMap(supplementList);
			for (int priority : supplementMap.keySet()) {
				List<Supplement> supplements = supplementMap.get(priority);
				double sumSupplementTotal = 0;
				for (Supplement supplement : supplements) {
					BillSupplementItem billSupplementItem = new BillSupplementItem(seq++, supplement);
					double supplementTotal = supplement.calcSupplementTotal(subTotal);
					billSupplementItem.setTotal(supplementTotal);
					billItems.add(billSupplementItem);

					sumSupplementTotal += supplementTotal;
				}

				subTotal += sumSupplementTotal;
				billItems.add(new BillSubTotalItem(seq++, subTotal));
			}
			changed = false;
		}
		return this.billItems;
	}

	public void addProduct(Product product, int amount) {
		addProduct(new OrderProduct(product, amount));
	}

	public void addProduct(OrderProduct productItem) {
		List<OrderProduct> productItems = order.getOrderProducts();
		int index = findOrderProduct(productItem.getProduct().getId());
		if (index >= 0) {
			int amount = productItem.getAmount();
			int oldAmount = productItems.get(index).getAmount();
			productItem.setAmount(oldAmount + amount);
			updateAmount(productItem);
		} else {
			if (productItem.getAmount() > 0) {
				productItems.add(productItem);
			}
		}
		changed = true;
	}

	public void removeProduct(Product product) {
		removeProduct(product.getId());
	}
	
	public void removeProduct(int productId) {
		List<OrderProduct> productItems = order.getOrderProducts();
		int index = findOrderProduct(productId);
		if (index >= 0) {
			productItems.remove(index);
			changed = true;
		} else {
			// TODO
		}
	}

	public void updateAmount(Product product, int newAmount) {
		updateAmount(new OrderProduct(product, newAmount));
	}

	public void updateAmount(OrderProduct productItem) {
		List<OrderProduct> productItems = order.getOrderProducts();
		int index = findOrderProduct(productItem.getProduct().getId());
		if (index >= 0) {
			OrderProduct original = productItems.get(index);
			int newAmount = productItem.getAmount();
			original.setAmount(newAmount);
			// move to last
			productItems.remove(index);
			if (original.getAmount() > 0) {
				productItems.add(original);
			}
			changed = true;
		} else {
			throw new NoSuchElementException();
		}
	}

	public void addSupplement(Supplement supplement) {
		List<Supplement> supplements = order.getSupplements();
		supplements.add(supplement);
		changed = true;
	}

	public void removeSupplement(Supplement supplement) {
		this.removeSupplement(supplement.getId());
	}
	
	public void removeSupplement(int supplementId) {
		List<Supplement> supplements = order.getSupplements();
		Iterator<Supplement> it = supplements.iterator();
		while(it.hasNext()) {
			if(it.next().getId() == supplementId) {
				it.remove();
				changed = true;
				break;
			}
		}
	}

	public OrderProduct getLastOrderProduct() {
		List<OrderProduct> productItems = order.getOrderProducts();
		if (productItems.size() == 0)
			return null;
		return productItems.get(productItems.size() - 1);
	}

	public int getOrderProductCount() {
		return order.getOrderProductCount();
	}

	// public void notifyDataSetChanged() {
	// changed = true;
	// }

	private int findOrderProduct(int productId) {
		List<OrderProduct> productItems = order.getOrderProducts();
		Iterator<OrderProduct> it = productItems.iterator();
		int i = 0;
		while (it.hasNext()) {
			if (it.next().getProduct().getId() == productId) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private MultiTreeMap<Integer, Supplement> createSupplementMap(List<Supplement> list) {
		MultiTreeMap<Integer, Supplement> map = new MultiTreeMap<Integer, Supplement>();
		for (Supplement supplement : list) {
			map.put(supplement.getPriority(), supplement);
		}
		return map;
	}

	private class MultiTreeMap<K, V> extends TreeMap<K, List<V>> {

		public List<V> put(K key, V value) {
			if (containsKey(key)) {
				List<V> l = get(key);
				l.add(value);
				return l;
			} else {
				List<V> l = new ArrayList<V>();
				l.add(value);
				return put(key, l);
			}
		}

	}
}
