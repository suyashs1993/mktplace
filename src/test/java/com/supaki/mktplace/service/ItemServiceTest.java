package com.supaki.mktplace.service;
import com.supaki.mktplace.dto.ItemInfo;
import com.supaki.mktplace.entity.Item;
import com.supaki.mktplace.exception.ApiException;
import com.supaki.mktplace.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    ItemRepository itemRepository;

    @InjectMocks
    ItemService itemService;

    Item item;

    ItemInfo itemInfo;


    @BeforeEach
    public void init() throws ApiException{
        item = new Item();
        item.setId(1l);
        item.setItemName("A");
        itemInfo = new ItemInfo("A",null,null,null);

    }
    @Test
    public void addItemExceptionTest() throws ApiException {
        when(itemRepository.findByItemName("A")).thenReturn(Optional.of(item));
        Throwable ex = Assertions.assertThrows(ApiException.class, ()->itemService.addItem(itemInfo));

    }
    @Test
    public void addItemTest() throws ApiException {
        when(itemRepository.findByItemName("A")).thenReturn(Optional.empty());
        itemService.addItem(itemInfo);
        verify(itemRepository, times(1)).save(any(Item.class));

    }
}
