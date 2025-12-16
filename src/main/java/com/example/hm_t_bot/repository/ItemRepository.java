package com.example.hm_t_bot.repository;
import com.example.hm_t_bot.model.dto.ItemDto;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    /*
    private Long id;
    private Long sort_num;
    private String name;
    private LocalDateTime create_date;
    private Integer status;
    */
    private final JdbcTemplate jdbcTemplate;


    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public List<ItemDto> getAllItem() {
        return jdbcTemplate.query("""
            select row_number() OVER (ORDER BY create_date desc) as rowNum
                ,id
                ,name
            from item
            where status = 1
            order by create_date desc
        """, (rs, i) ->
                new ItemDto(
                        rs.getLong("rowNum"),
                        rs.getLong("id"),
                        rs.getString("name")
                )
        );
    }




}
