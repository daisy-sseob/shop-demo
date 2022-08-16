package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

	List<Item> findByItemNm(String itemNm);

	List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
	
	List<Item> findByPriceLessThan(Integer price);
	
	List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

	@Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
	List<Item> findByItemDetail(String itemDetail);
	
	@Query(value = "select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
	List<Item> findByItemDetailNative(String itemDetail);
	
}