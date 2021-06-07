package io.batch.springbatch.job.custom;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

public class CustomItemReader<T> implements ItemReader<T> {
    private final List<T> items;
    
    public CustomItemReader(List<T> items) {
        this.items = items;
    }
    
    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if(!items.isEmpty()){
            return items.remove(0); // items가 비지 않았을 경우 맨 앞의 item을 리턴하고 items에서 제거
        }
        return null; // items가 비었으면 null을 리턴(chunk의 종료)
    }
}
