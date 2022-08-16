package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
}