package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties") // test properties 사용
class ItemRepositoryTest {

	@Autowired
	ItemRepository itemRepository;
	
	@Test
	@DisplayName("상품 저장 테스트")
	public void createItemTest() {
		Item toothbrush = Item.builder()
				.itemNm("루셀로 칫솔")
				.price(10000)
				.itemDetail("루셀로사 에서 만든 칫솔 입니다. S, M, L 다양한 크기가 있습니다.")
				.itemSellStatus(ItemSellStatus.SELL)
				.stockNumber(199)
				.regTime(LocalDateTime.now())
				.updateTime(LocalDateTime.now())
				.build();

		Item saveToothBrush = itemRepository.save(toothbrush);
		assertEquals(199, saveToothBrush.getStockNumber());
	}
	
	public void createItemList() {
		for (int i = 0; i < 11; i++) {
			Item toothbrush = Item.builder()
					.itemNm("루셀로 칫솔 " + i)
					.price(10000 + i)
					.itemDetail("루셀로사 에서 만든 칫솔 입니다. S, M, L 다양한 크기가 있습니다." + i)
					.itemSellStatus(ItemSellStatus.SELL)
					.stockNumber(199)
					.regTime(LocalDateTime.now())
					.updateTime(LocalDateTime.now())
					.build();

			itemRepository.save(toothbrush);
		}
	}
	
	@Test
	@DisplayName("상품명 조회 테스트")
	public void findByItemNmTest() {
		this.createItemList();
		List<Item> items = itemRepository.findByItemNm("루셀로 칫솔 9");
		assertEquals("루셀로 칫솔 9", items.get(0).getItemNm());
	}
	
	@Test
	@DisplayName("상품명 or 상품상세 조회 테스트")
	public void findByItemNmOrItemDetailTest() {
		this.createItemList();
		List<Item> items = itemRepository.findByItemNmOrItemDetail("루셀로 5","루셀로사 에서 만든 칫솔 입니다. S, M, L 다양한 크기가 있습니다.5");
		assertEquals("루셀로 칫솔 5", items.get(0).getItemNm());
	}
	
	@Test
	@DisplayName("상품 가격 LessThan 테스트")
	public void findByPriceLessThanTest() {
		this.createItemList();
		List<Item> items = itemRepository.findByPriceLessThan(10006);
		assertTrue(items.get(0).getPrice() < 10006);
	}
	
	@Test
	@DisplayName("상품 가격 내림차순 조회 테스트")
	public void findByPriceLessThanOrderByPriceDescTest() {
		this.createItemList();
		List<Item> items = itemRepository.findByPriceLessThanOrderByPriceDesc(10006);
		items.forEach(System.out::println);
	}
	
	@Test
	@DisplayName("JPQL을 이용한 상품 상세 조회 테스트")
	public void findByItemDetailJPQLTest() {
		this.createItemList();
		List<Item> items = itemRepository.findByItemDetail("다양한 크기가 있습니다.7");
		assertEquals("루셀로사 에서 만든 칫솔 입니다. S, M, L 다양한 크기가 있습니다.7", items.get(0).getItemDetail());
	}
	@Test
	@DisplayName(" Native query를 이용한 상품 상세 조회 테스트")
	public void findByItemDetailNativeQueryTest() {
		this.createItemList();
		List<Item> items = itemRepository.findByItemDetailNative("다양한 크기가 있습니다.7");
		assertEquals("루셀로사 에서 만든 칫솔 입니다. S, M, L 다양한 크기가 있습니다.7", items.get(0).getItemDetail());
	}
}