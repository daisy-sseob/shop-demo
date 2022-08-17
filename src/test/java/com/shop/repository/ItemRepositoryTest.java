package com.shop.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.nio.channels.Pipe;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties") // test properties 사용
class ItemRepositoryTest {

	@PersistenceContext
	EntityManager entityManager;

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
	
	// 0 ~ 5번째까지 상품은 SELL 상태로 6번째 상품부터는 SOLD_OUT 상태로 insert
	public void createItemList() {
		for (int i = 0; i < 11; i++) {
			Item toothbrush = Item.builder()
					.itemNm("루셀로 칫솔 " + i)
					.price(10000 + i)
					.itemDetail("루셀로사 에서 만든 칫솔 입니다. S, M, L 다양한 크기가 있습니다." + i)
					.itemSellStatus( (i < 5) ? ItemSellStatus.SELL : ItemSellStatus.SOLD_OUT)
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
		List<Item> items = itemRepository.findByItemNmOrItemDetail("루셀로 5", "루셀로사 에서 만든 칫솔 입니다. S, M, L 다양한 크기가 있습니다.5");
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

	@Test
	@DisplayName("QueryDsl 조회 테스트")
	public void queryDslTest() {
		this.createItemList();
		JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager); // 쿼리 동적 생성
		QItem qItem = QItem.item; // 플러그인을 통해 빌드시 자동으로 생성된 QItem 객체 사용
		JPAQuery<Item> query = jpaQueryFactory
				.selectFrom(qItem)
				.where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
				.where(qItem.itemDetail.like("%" + "다양한 크기가 있습니다.7" + "%"))
				.orderBy(qItem.price.desc());

		List<Item> itemList = query.fetch();
		itemList.forEach(System.out::println);
	}
	
	@Test
	@DisplayName("QuerydslPredicateExecutor 조회 테스트")
	public void QuerydslPredicateExecutorTest() {
		this.createItemList();
		
		QItem qItem = QItem.item; // 플러그인을 통해 빌드시 자동으로 생성된 QItem 객체 사용
		String itemDetail = "%다양한 크기가 있습니다%";
		int price = 10007;
		String itemSellStat = "SELL";

		BooleanBuilder booleanBuilder = new BooleanBuilder()
				.and(qItem.itemDetail.like(itemDetail))
				.and(qItem.price.lt(price))
				.and(qItem.itemSellStatus.eq(ItemSellStatus.valueOf(itemSellStat)));

		Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, PageRequest.of(0, 5));
		System.out.println("itemPagingResult.getTotalElements() = " + itemPagingResult.getTotalElements());;
		System.out.println("itemPagingResult.getNumberOfElements() = " + itemPagingResult.getNumberOfElements());;

		List<Item> items = itemPagingResult.getContent();
		items.forEach(System.out::println);
	}
}